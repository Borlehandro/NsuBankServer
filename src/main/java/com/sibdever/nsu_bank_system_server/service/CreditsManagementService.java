package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.*;
import com.sibdever.nsu_bank_system_server.data.repo.CreditTableRepo;
import com.sibdever.nsu_bank_system_server.data.repo.CreditsRepo;
import com.sibdever.nsu_bank_system_server.data.repo.PaymentsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

@Service
public class CreditsManagementService {

    @Autowired
    private CreditsRepo creditsRepo;
    @Autowired
    private CreditTableRepo creditTableRepo;
    @Autowired
    private PaymentsRepo paymentsRepo;

    // Caller method is Transactional
    public Credit giveCredit(Client client, Offer offer, int monthPeriod, double sum, PaymentChannel payTo) {
        // Todo use BigDecimal
        var credit = new Credit(
                client,
                LocalDateTime.now(),
                monthPeriod,
                sum,
                sum + sum * (payTo.getCommissionPercents() / 100d),
                offer);

        var creditRes = creditsRepo.saveAndFlush(credit);
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
        return creditRes;
    }

    private void updateCreditTimetable(List<CreditsTable> rowsToUpdate, double startBalance, double monthPercent) {
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
    // Todo calculate commission!
    public void recalculateCreditTableWithDailyPayments(Credit credit, Set<Payment> payments) {
        var afterTime = payments.stream().findFirst().get().getPaymentDetails().getTimestamp();
        double sum = calculatePaymentsSum(payments);
        var timetableRowsToCalculate =
                creditTableRepo.findAllByCreditIdWhereDateAfterOrEquals(credit.getId(), afterTime);

        var currentMonthRow = timetableRowsToCalculate.get(0);

        var previousPayments = currentMonthRow.getPayment();

        if(previousPayments != null) {
            if(!previousPayments.isEmpty())
                sum += currentMonthRow.getExpectedPayout();
            previousPayments.addAll(payments);
        } else currentMonthRow.setPayment(payments);

        currentMonthRow.setExpectedPayout(sum);
        double paymentOfPercents =
                calculatePaymentOfPercents(credit.getOffer().getPercentsPerMonth(), credit.getBalance());
        double paymentOfDebt = calculatePaymentOfDebt(sum, paymentOfPercents);
        currentMonthRow.setPaymentOfPercents(paymentOfPercents);
        currentMonthRow.setPaymentOfDebt(paymentOfDebt);
        currentMonthRow.setBalanceAfterPayment(credit.getBalance() - paymentOfDebt);
        credit.setBalance(credit.getBalance() - paymentOfDebt);
        if(Math.abs(credit.getBalance()) < 0.01d)
            credit.setStatus(CreditStatus.CLOSED);
        else
            updateCreditTimetable(timetableRowsToCalculate.subList(1, timetableRowsToCalculate.size()), credit.getBalance(), credit.getOffer().getPercentsPerMonth());
    }

    public double calculatePaymentsSum(Set<Payment> payments) {
        return payments.stream().reduce(BigDecimal.ZERO,
                (sum, payment) -> sum.add(BigDecimal.valueOf(payment.getPaymentDetails().getPaymentSum())),
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
}