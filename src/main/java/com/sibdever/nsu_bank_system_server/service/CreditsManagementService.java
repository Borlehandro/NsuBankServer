package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.ClientStatus;
import com.sibdever.nsu_bank_system_server.data.model.entities.*;
import com.sibdever.nsu_bank_system_server.data.repo.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

@Service
public class CreditsManagementService {

    private final CreditsRepo creditsRepo;
    private final ClientsRepo clientsRepo;
    private final CreditTableRepo creditTableRepo;
    private final PaymentsRepo paymentsRepo;
    private final CreditHistoryRepo creditHistoryRepo;
    private final DayStatisticRepo dayStatisticRepo;
    
    private Clock clock;

    public CreditsManagementService(CreditsRepo creditsRepo, ClientsRepo clientsRepo, CreditTableRepo creditTableRepo, PaymentsRepo paymentsRepo, CreditHistoryRepo creditHistoryRepo, DayStatisticRepo dayStatisticRepo, Clock clock) {
        this.creditsRepo = creditsRepo;
        this.clientsRepo = clientsRepo;
        this.creditTableRepo = creditTableRepo;
        this.paymentsRepo = paymentsRepo;
        this.creditHistoryRepo = creditHistoryRepo;
        this.dayStatisticRepo = dayStatisticRepo;
        this.clock = clock;
    }

    // Caller method is Transactional
    public Credit giveCredit(Client client, Offer offer, int monthPeriod, double sum, PaymentChannel payTo) {
        // Todo use BigDecimal
        var credit = new Credit(
                client,
                LocalDateTime.now(clock),
                monthPeriod,
                sum,
                sum + sum * (payTo.getFeePercents() / 100d),
                offer);

        credit = creditsRepo.saveAndFlush(credit);
        client.setActiveCredit(credit);
        clientsRepo.saveAndFlush(client);
        creditHistoryRepo.save(new CreditHistory(client, credit, CreditStatus.ACTIVE, credit.getStartDate()));
        paymentsRepo.save(new Payment(client, credit, new PaymentDetails(credit.getStartDate(), PaymentType.RELEASE, payTo, sum)));

        var finalTime = credit.getStartDate().plus(monthPeriod, ChronoUnit.MONTHS);
        double monthPayout = calculateMonthPayout(credit.getBalance(), offer.getPercentsPerMonth(), monthPeriod);
        double currentBalance = credit.getBalance();

        for (LocalDateTime time = credit.getStartDate().plus(1, ChronoUnit.MONTHS);
             time.isBefore(finalTime) || time.isEqual(finalTime);
             time = time.plus(1, ChronoUnit.MONTHS)) {

            double paymentOfPercents = calculatePaymentOfPercents(offer.getPercentsPerMonth(), currentBalance);
            double paymentOfDebt = calculatePaymentOfDebt(monthPayout, paymentOfPercents);
            currentBalance = calculateBalanceAfterPayment(currentBalance, paymentOfDebt);

            creditTableRepo.save(new CreditsTable(
                    new CreditTableId(credit, time),
                    monthPayout,
                    paymentOfPercents,
                    paymentOfDebt,
                    currentBalance,
                    Math.abs(currentBalance) < 0.01d ? CreditStatus.CLOSED : CreditStatus.ACTIVE
            ));
        }
        return credit;
    }

    // Caller method is Transactional
    private void updateCreditTable(List<CreditsTable> rowsToUpdate, double startBalance, double monthPercent) {
        double monthPayout = calculateMonthPayout(startBalance, monthPercent, rowsToUpdate.size());
        double currentBalance = startBalance;
        for (CreditsTable row : rowsToUpdate) {
            double paymentOfPercents = calculatePaymentOfPercents(monthPercent, currentBalance);
            double paymentOfDebt = calculatePaymentOfDebt(monthPayout, paymentOfPercents);
            currentBalance = calculateBalanceAfterPayment(currentBalance, paymentOfDebt);
            row.setPaymentOfDebt(paymentOfDebt);
            row.setPaymentOfPercents(paymentOfPercents);
            row.setExpectedPayout(monthPayout);
            row.setBalanceAfterPayment(currentBalance);
            row.setCreditStatusAfterPayment(Math.abs(currentBalance) < 0.01d ? CreditStatus.CLOSED : CreditStatus.ACTIVE);
        }
    }

    // Caller method is Transactional
    public void recalculateCreditTableWithDailyPayments(Credit credit, Set<Payment> payments, LocalDate currentDay) {
        var afterTime = payments.stream().findFirst().get().getPaymentDetails().getTimestamp();
        double sum = calculatePaymentsSum(payments);
        System.out.println("Wanna pay: " + sum);
        double fee = calculateSummaryFee(payments);
        var timetableRowsToCalculate =
                creditTableRepo.findAllById_CreditAndId_TimestampAfterOrderById_Timestamp(credit, afterTime);

        var currentMonthRow = timetableRowsToCalculate.get(0);

        var previousPayments = currentMonthRow.getPayment();

        if (previousPayments != null) {
            if (!previousPayments.isEmpty()) {
                sum += currentMonthRow.getRealPayout();
            }
            previousPayments.addAll(payments);
        } else currentMonthRow.setPayment(payments);

        currentMonthRow.setPayment(previousPayments);
        currentMonthRow.setRealPayout(sum - fee);
        currentMonthRow.setFee(fee + currentMonthRow.getFee());

        // Month payment completed
        // Recalculate future table rows
        if (currentMonthRow.getRealPayout() >= currentMonthRow.getExpectedPayout()) {
            currentMonthRow.setExpectedPayout(currentMonthRow.getRealPayout());

            double paymentOfPercents =
                    calculatePaymentOfPercents(credit.getOffer().getPercentsPerMonth(), credit.getBalance());

            double paymentOfDebt = calculatePaymentOfDebt(currentMonthRow.getExpectedPayout(), paymentOfPercents);

            currentMonthRow.setPaymentOfPercents(paymentOfPercents);
            currentMonthRow.setPaymentOfDebt(paymentOfDebt);
            credit.setBalance(credit.getBalance() - paymentOfDebt);
            currentMonthRow.setBalanceAfterPayment(credit.getBalance());

            if (credit.getBalance() >= 0) {
                credit.setCashInflow(credit.getCashInflow() + currentMonthRow.getRealPayout());
            } else {
                // Pay back if overpay
                // Todo save payment with service
                paymentsRepo.save(
                        new Payment(
                                credit.getClient(),
                                credit,
                                new PaymentDetails(
                                        LocalDateTime.now(clock),
                                        PaymentType.RELEASE,
                                        PaymentChannel.BANK_ACCOUNT,
                                        Math.abs(credit.getBalance())
                                )
                        )
                );
                credit.setCashInflow(credit.getCashInflow() + currentMonthRow.getRealPayout() - Math.abs(credit.getBalance()));
                credit.setBalance(0.00d);
            }
            // Close credit. If expired - it will removed.
            if (Math.abs(credit.getBalance()) < 0.01d) {
                credit.setStatus(CreditStatus.CLOSED);
                credit.getClient().setActiveCredit(null);
                if (!credit.getClient().getClientStatus().equals(ClientStatus.BLOCKED)) {
                    if (credit.getClient().getOffer() != null)
                        credit.getClient().setClientStatus(ClientStatus.OFFERED_WITHOUT_CREDIT);
                    else
                        credit.getClient().setClientStatus(ClientStatus.WITHOUT_OFFER);
                }
                creditHistoryRepo.save(new CreditHistory(credit.getClient(), credit, CreditStatus.CLOSED, LocalDateTime.now(clock)));
                currentMonthRow.setCreditStatusAfterPayment(CreditStatus.CLOSED);
                creditTableRepo.deleteAll(timetableRowsToCalculate.subList(1, timetableRowsToCalculate.size()));
            } else {
                // Remove expired
                if(credit.getStatus().equals(CreditStatus.EXPIRED)) {
                    credit.setStatus(CreditStatus.ACTIVE);
                    currentMonthRow.setCreditStatusAfterPayment(CreditStatus.ACTIVE);
                    creditHistoryRepo.save(new CreditHistory(credit.getClient(), credit, CreditStatus.ACTIVE, LocalDateTime.now(clock)));
                }

                updateCreditTable(
                        timetableRowsToCalculate.subList(1, timetableRowsToCalculate.size()),
                        credit.getBalance(),
                        credit.getOffer().getPercentsPerMonth());
            }
        } else {
            credit.setCashInflow(credit.getCashInflow() + currentMonthRow.getRealPayout());
        }
        credit.setProfitMargin(credit.getCashInflow() / credit.getSum());
        dayStatisticRepo.save(new DayStatistic(currentDay, credit.getCashInflow(), credit.getProfitMargin(), credit));
        creditsRepo.saveAndFlush(credit);
    }

    public void handleExpired(CreditsTable expiredRecord) {
        var credit = expiredRecord.getId().getCredit();
        var offer = credit.getOffer();
        double outstandingDebt = expiredRecord.getExpectedPayout() - expiredRecord.getRealPayout();
        double forfeit = outstandingDebt * offer.getLateFeePercents();

        expiredRecord.setExpectedPayout(expiredRecord.getRealPayout());

        double paymentOfPercents =
                calculatePaymentOfPercents(offer.getPercentsPerMonth(), credit.getBalance());
        double paymentOfDebt = calculatePaymentOfDebt(expiredRecord.getExpectedPayout(), paymentOfPercents);
        expiredRecord.setPaymentOfPercents(paymentOfPercents);
        expiredRecord.setPaymentOfDebt(paymentOfDebt);

        // Todo use big decimal
        credit.setBalance(credit.getBalance() - paymentOfDebt + forfeit);
        expiredRecord.setBalanceAfterPayment(credit.getBalance());
        expiredRecord.setForfeit(forfeit);

        expiredRecord.setCreditStatusAfterPayment(CreditStatus.EXPIRED);
        credit.setStatus(CreditStatus.EXPIRED);
        creditHistoryRepo.save(new CreditHistory(credit.getClient(), credit, CreditStatus.EXPIRED, LocalDateTime.now(clock)));

        var nextRecordsList =
                creditTableRepo.findAllById_CreditAndId_TimestampAfterOrderById_Timestamp(
                        expiredRecord.getId().getCredit(),
                        expiredRecord.getId().getTimestamp()
                );
        if(!nextRecordsList.isEmpty()) {
            updateCreditTable(nextRecordsList, credit.getBalance(), offer.getPercentsPerMonth());
        } else {
            // Add last month record
            double newMonthPayout = calculateMonthPayout(credit.getBalance(), offer.getPercentsPerMonth(), 1);
            double newPaymentOfPercents = calculatePaymentOfPercents(offer.getPercentsPerMonth(), credit.getBalance());
            double newPaymentOfDebt  = calculatePaymentOfDebt(newMonthPayout, newPaymentOfPercents);
            creditTableRepo.save(new CreditsTable(
                    new CreditTableId(
                            credit, expiredRecord.getId().getTimestamp().plus(1, ChronoUnit.MONTHS)
                    ),
                    newMonthPayout,
                    newPaymentOfPercents,
                    newPaymentOfDebt,
                    0.0,
                    CreditStatus.CLOSED
            ));
        }
    }

    private double calculatePaymentsSum(Set<Payment> payments) {
        return payments.stream().reduce(BigDecimal.ZERO,
                (sum, payment) -> sum.add(BigDecimal.valueOf(payment.getPaymentDetails().getPaymentSum())),
                BigDecimal::add).doubleValue();
    }

    private double calculateSummaryFee(Set<Payment> payments) {
        return payments.stream().reduce(BigDecimal.ZERO,
                (sum, payment) -> sum
                        .add(BigDecimal.valueOf(payment.getPaymentDetails().getPaymentSum())
                                .multiply(BigDecimal.valueOf(payment.getPaymentDetails().getChannel().getFeePercents())
                                        .divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP))
                        ),
                BigDecimal::add).doubleValue();
    }

    private double calculateMonthPayout(double sum, double monthPercent, int months) {

        BigDecimal monthPercentDecimal = BigDecimal.valueOf(monthPercent)
                .divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP);

        System.out.println("monthPercentDecimal: " + monthPercentDecimal);
        BigDecimal q = (BigDecimal.ONE.add(monthPercentDecimal)).pow(months);
        System.out.println("Q: " + q);
        BigDecimal numerator = BigDecimal.valueOf(sum)
                .multiply(monthPercentDecimal)
                .multiply(q);
        System.out.println("numerator: " + numerator);
        BigDecimal denominator = q.subtract(BigDecimal.ONE);
        System.out.println("denominator: " + denominator);
        BigDecimal monthPayout = numerator.divide(denominator, 3, RoundingMode.HALF_UP);
        return monthPayout.doubleValue();
    }

    private double calculatePaymentOfPercents(double monthPercent, double currentBalance) {
        BigDecimal monthPercentDecimal = BigDecimal.valueOf(monthPercent)
                .divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP);
        return BigDecimal.valueOf(currentBalance).multiply(monthPercentDecimal).doubleValue();
    }

    private double calculatePaymentOfDebt(double monthPayout, double paymentOfPercents) {
        return BigDecimal.valueOf(monthPayout).subtract(BigDecimal.valueOf(paymentOfPercents)).doubleValue();
    }

    private double calculateBalanceAfterPayment(double currentBalance, double paymentOfDebt) {
        return BigDecimal.valueOf(currentBalance).subtract(BigDecimal.valueOf(paymentOfDebt)).doubleValue();
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }
}