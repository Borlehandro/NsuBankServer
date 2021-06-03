package com.sibdever.nsu_bank_system_server;

import com.sibdever.nsu_bank_system_server.data.model.entities.*;
import com.sibdever.nsu_bank_system_server.data.repo.CreditTableRepo;
import com.sibdever.nsu_bank_system_server.data.repo.CreditsRepo;
import com.sibdever.nsu_bank_system_server.data.repo.DayStatisticRepo;
import com.sibdever.nsu_bank_system_server.data.repo.PaymentsRepo;
import com.sibdever.nsu_bank_system_server.exception.WrongCredentialsException;
import com.sibdever.nsu_bank_system_server.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CreditManagementTests extends ApplicationTests {

    private static Client testClient;
    private static Credit testCredit;

    @Autowired
    private PaymentsManagementService paymentsManagementService;
    @Autowired
    private DailyScheduledService dailyScheduledService;
    @Autowired
    private CrudCreditTableService creditTableService;
    @Autowired
    private CrudCreditService creditService;
    @Autowired
    private CreditsRepo creditsRepo;
    @Autowired
    private DayStatisticRepo dayStatisticRepo;
    @Autowired
    private CreditHistoryService creditHistoryService;
    @Autowired
    private CreditTableRepo creditTableRepo;
    @Autowired
    private PaymentsRepo paymentsRepo;
    @Autowired
    private ClientsManagementService clientsManagementService;

    @BeforeAll
    @Rollback(false)
    public static void createClientAndCredit(@Autowired CrudClientService crudClientService,
                                             @Autowired CrudOfferService crudOfferService,
                                             @Autowired ClientsManagementService clientsManagementService) throws WrongCredentialsException {
        testClient = crudClientService.createClient(new Client("TestClient"));
        assertEquals(1, List.of(crudClientService.findAll()).size());
        var offer = crudOfferService.createOffer(new Offer("test", 1, 1, 100, 0, 1000000));
        clientsManagementService.setClientOffer(testClient.getId(), offer.getId());
        testCredit = clientsManagementService.giveCredit(testClient.getId(), offer.getId(), 12, 10000, PaymentChannel.CREDIT_CARD);
    }

    @Test
    @Transactional
    public void testCreditTable() {
        creditTableService.findByCreditId(testCredit.getId()).forEach(row -> {
            System.out.println(row.getExpectedPayout());
            Assertions.assertTrue(row.getExpectedPayout() <= 898.0 && row.getExpectedPayout() >= 897);
        });
    }

    @Test
    @Transactional
    public void testSmallPayments() throws WrongCredentialsException {

        var oldTable = creditTableService.findByCreditId(testCredit.getId());
        oldTable.sort(Comparator.comparing(tableRow -> tableRow.getId().getTimestamp()));

        paymentsManagementService.processPayment(
                testClient.getId(),
                testCredit.getId(),
                new PaymentDetails(
                        testCredit.getStartDate().plus(1, ChronoUnit.HOURS),
                        PaymentType.REFUND,
                        PaymentChannel.YOO_MONEY,
                        100.0)
        );

        dailyScheduledService.manageDailyPayments();
        var updatedTable = creditTableService.findByCreditId(testCredit.getId());
        updatedTable.sort(Comparator.comparing(tableRow -> tableRow.getId().getTimestamp()));

        // Test that was not recalculated
        for (int i = 0; i < oldTable.size(); ++i) {
            assertEquals(oldTable.get(i).getExpectedPayout(), updatedTable.get(i).getExpectedPayout());
            assertEquals(oldTable.get(i).getPaymentOfDebt(), updatedTable.get(i).getPaymentOfDebt());
            assertEquals(oldTable.get(i).getPaymentOfPercents(), updatedTable.get(i).getPaymentOfPercents());
            assertEquals(oldTable.get(i).getBalanceAfterPayment(), updatedTable.get(i).getBalanceAfterPayment());
            assertEquals(oldTable.get(i).getCreditStatusAfterPayment(), updatedTable.get(i).getCreditStatusAfterPayment());
        }

        // Test real payout, fee and profit
        assertEquals(97.0, updatedTable.get(0).getRealPayout());
        assertEquals(3.0, updatedTable.get(0).getFee());
        assertEquals(97.0, updatedTable.get(0).getId().getCredit().getCashInflow());
        assertEquals(0.0097, updatedTable.get(0).getId().getCredit().getProfitMargin());

        var statistic = dayStatisticRepo.findAll();
        assertEquals(1, statistic.size());
        assertEquals(97.0, statistic.get(0).getCashInflow());
        assertEquals(0.0097, statistic.get(0).getProfitMargin());
    }

    @Test
    @Transactional
    public void testDailySchedule() throws WrongCredentialsException {

        paymentsManagementService.processPayment(
                testClient.getId(),
                testCredit.getId(),
                new PaymentDetails(
                        testCredit.getStartDate().plus(1, ChronoUnit.HOURS),
                        PaymentType.REFUND,
                        PaymentChannel.YOO_MONEY,
                        1000.0)
        );

        dailyScheduledService.manageDailyPayments();
        var updatedTable = creditTableService.findByCreditId(testCredit.getId());
        updatedTable.sort(Comparator.comparing(tableRow -> tableRow.getId().getTimestamp()));

        assertEquals(970.0, updatedTable.get(0).getExpectedPayout());
        System.out.println("FEE:" + updatedTable.get(0).getFee());
        assertEquals(30.0, updatedTable.get(0).getFee());
        assertTrue(Math.abs(updatedTable.get(0).getBalanceAfterPayment() - 9231.0) < 1);
        System.out.println("LAST: " + updatedTable.get(updatedTable.size() - 1));
        updatedTable.subList(1, updatedTable.size()).forEach(
                (item) -> {
                    System.out.println(item.getExpectedPayout());
                    assertTrue(Math.abs(item.getExpectedPayout() - 890.0) < 1);
                }
        );
        // Todo fix this refresh
        var updatedCredit = creditsRepo.findById(testCredit.getId()).get();
        System.out.println(updatedCredit.getCashInflow());
        System.out.println(updatedCredit.getProfitMargin());
        assertEquals(970.0, updatedCredit.getCashInflow());
        assertEquals(970.0 / updatedCredit.getSum(), updatedCredit.getProfitMargin());
    }

    @Test
    @Transactional
    public void testCreditClosing() throws WrongCredentialsException {
        paymentsManagementService.processPayment(
                testClient.getId(),
                testCredit.getId(),
                new PaymentDetails(
                        testCredit.getStartDate().plus(1, ChronoUnit.HOURS),
                        PaymentType.REFUND,
                        PaymentChannel.BANK_ACCOUNT,
                        10201.0)
        );

        dailyScheduledService.manageDailyPayments();

        var updatedTable = creditTableService.findByCreditId(testCredit.getId());
        updatedTable.forEach(System.out::println);
        updatedTable.sort(Comparator.comparing(tableRow -> tableRow.getId().getTimestamp()));

        assertEquals(CreditStatus.CLOSED, updatedTable.get(0).getCreditStatusAfterPayment());
        assertEquals(CreditStatus.CLOSED, creditService.findById(testCredit.getId()).getStatus());
        assertEquals(1, updatedTable.size());

        var history = creditHistoryService.getCreditHistory(testCredit.getId());
        System.out.println(history);
        history.sort(Comparator.comparing(CreditHistory::getTimestamp));
        assertEquals(2, history.size());
        assertEquals(CreditStatus.ACTIVE, history.get(0).getCreditStatus());
        assertEquals(CreditStatus.CLOSED, history.get(1).getCreditStatus());
        assertNull(testClient.getActiveCredit());
    }

    // Todo write late pay tests

    @Test
    @Transactional
    public void testLatePayNotLastMonth() throws WrongCredentialsException {

        paymentsManagementService.processPayment(
                testClient.getId(),
                testCredit.getId(),
                new PaymentDetails(
                        testCredit.getStartDate(),
                        PaymentType.REFUND,
                        PaymentChannel.YOO_MONEY,
                        100.0)
        );

        System.out.println("Payment time:" + paymentsRepo.findAll().get(0).getPaymentDetails().getTimestamp());

        dailyScheduledService.manageDailyPayments();

        var oldTable = creditTableService.findByCreditId(testCredit.getId());
        oldTable.sort(Comparator.comparing(tableRow -> tableRow.getId().getTimestamp()));

        Clock clock = Clock.fixed(
                LocalDateTime.now().plus(1, ChronoUnit.MONTHS).minus(1, ChronoUnit.DAYS).toInstant(ZoneOffset.UTC),
                ZoneId.systemDefault());

        System.out.println("OLD");
        oldTable.forEach(item -> System.out.println(item.getId().getTimestamp() + " " + item.getExpectedPayout() + "  " + item.getRealPayout()));
        System.out.println();

        // Move time
        dailyScheduledService.setClock(clock);

        oldTable.forEach(item -> System.out.println(item.getExpectedPayout() + "  " + item.getRealPayout()));

        dailyScheduledService.manageDailyPayments();

        var updatedTable = creditTableService.findByCreditId(testCredit.getId());
        System.out.println("Up:");
        updatedTable.forEach(item -> System.out.println(item.getId().getTimestamp() + " " + item.getExpectedPayout() + "  " + item.getRealPayout()));

        var updatedCredit = creditsRepo.findById(testCredit.getId()).get();
        assertEquals(CreditStatus.EXPIRED, updatedCredit.getStatus());
        assertEquals(10104, (int) updatedCredit.getBalance());
        assertEquals(12, updatedTable.size());
        assertEquals(101.04, updatedTable.get(1).getPaymentOfPercents());
        assertEquals(873, (int) updatedTable.get(1).getPaymentOfDebt());
        assertEquals(9230, (int) updatedTable.get(1).getBalanceAfterPayment());
    }

    @Test
    @Transactional
    public void testActiveAfterLatePay() throws WrongCredentialsException {
        paymentsManagementService.processPayment(
                testClient.getId(),
                testCredit.getId(),
                new PaymentDetails(
                        testCredit.getStartDate(),
                        PaymentType.REFUND,
                        PaymentChannel.YOO_MONEY,
                        100.0)
        );

        System.out.println("Payment time:" + paymentsRepo.findAll().get(0).getPaymentDetails().getTimestamp());

        dailyScheduledService.manageDailyPayments();

        var oldTable = creditTableService.findByCreditId(testCredit.getId());
        oldTable.sort(Comparator.comparing(tableRow -> tableRow.getId().getTimestamp()));

        Clock clock = Clock.fixed(
                LocalDateTime.now().plus(1, ChronoUnit.MONTHS).toInstant(ZoneOffset.UTC),
                ZoneId.systemDefault());

        oldTable.forEach(item -> System.out.println(item.getExpectedPayout() + "  " + item.getRealPayout()));
        System.out.println();

        // June
        dailyScheduledService.setClock(clock);

        oldTable.forEach(item -> System.out.println(item.getExpectedPayout() + "  " + item.getRealPayout()));

        dailyScheduledService.manageDailyPayments();

        clock = Clock.fixed(
                LocalDateTime.now().plus(2, ChronoUnit.MONTHS).toInstant(ZoneOffset.UTC),
                ZoneId.systemDefault());

        // July
        dailyScheduledService.setClock(clock);

        paymentsManagementService.processPayment(
                testClient.getId(),
                testCredit.getId(),
                new PaymentDetails(
                        testCredit.getStartDate().plus(2, ChronoUnit.MONTHS).minus(1, ChronoUnit.DAYS),
                        PaymentType.REFUND,
                        PaymentChannel.BANK_ACCOUNT,
                        1000.0)
        );

        dailyScheduledService.manageDailyPayments();

        var updatedCredit = creditsRepo.findById(testCredit.getId()).get();
        var updatedTable = creditTableService.findByCreditId(testCredit.getId());
        assertEquals(CreditStatus.ACTIVE, updatedCredit.getStatus());
        assertEquals(12,updatedTable.size());
    }

    @Test
    @Transactional
    public void testClosedAfterLatePay() throws WrongCredentialsException {
        paymentsManagementService.processPayment(
                testClient.getId(),
                testCredit.getId(),
                new PaymentDetails(
                        testCredit.getStartDate(),
                        PaymentType.REFUND,
                        PaymentChannel.YOO_MONEY,
                        100.0)
        );

        System.out.println("Payment time:" + paymentsRepo.findAll().get(0).getPaymentDetails().getTimestamp());

        dailyScheduledService.manageDailyPayments();

        var oldTable = creditTableService.findByCreditId(testCredit.getId());
        oldTable.sort(Comparator.comparing(tableRow -> tableRow.getId().getTimestamp()));

        Clock clock = Clock.fixed(
                LocalDateTime.now().plus(1, ChronoUnit.MONTHS).toInstant(ZoneOffset.UTC),
                ZoneId.systemDefault());

        oldTable.forEach(item -> System.out.println(item.getExpectedPayout() + "  " + item.getRealPayout()));
        System.out.println();

        // June
        dailyScheduledService.setClock(clock);

        oldTable.forEach(item -> System.out.println(item.getExpectedPayout() + "  " + item.getRealPayout()));

        dailyScheduledService.manageDailyPayments();

        clock = Clock.fixed(
                LocalDateTime.now().plus(2, ChronoUnit.MONTHS).toInstant(ZoneOffset.UTC),
                ZoneId.systemDefault());

        // July
        dailyScheduledService.setClock(clock);

        paymentsManagementService.processPayment(
                testClient.getId(),
                testCredit.getId(),
                new PaymentDetails(
                        testCredit.getStartDate().plus(2, ChronoUnit.MONTHS).minus(1, ChronoUnit.DAYS),
                        PaymentType.REFUND,
                        PaymentChannel.BANK_ACCOUNT,
                        10300.0)
        );

        dailyScheduledService.manageDailyPayments();

        var updatedCredit = creditsRepo.findById(testCredit.getId()).get();
        assertEquals(0, (int) updatedCredit.getBalance());
        assertEquals(CreditStatus.CLOSED, updatedCredit.getStatus());
    }
}
