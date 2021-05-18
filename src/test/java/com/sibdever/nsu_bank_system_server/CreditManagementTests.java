package com.sibdever.nsu_bank_system_server;

import com.sibdever.nsu_bank_system_server.data.model.entities.*;
import com.sibdever.nsu_bank_system_server.data.repo.CreditsRepo;
import com.sibdever.nsu_bank_system_server.exception.WrongCredentialsException;
import com.sibdever.nsu_bank_system_server.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
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
    private CreditHistoryService creditHistoryService;

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

        assertEquals(1000.0, updatedTable.get(0).getExpectedPayout());
        System.out.println("FEE:" + updatedTable.get(0).getFee());
        assertTrue(Math.abs(updatedTable.get(0).getFee() - 30) < 1);
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
        history.sort(Comparator.comparing(CreditHistory::getTimestamp));
        assertEquals(2, history.size());
        assertEquals(CreditStatus.ACTIVE, history.get(0).getCreditStatus());
        assertEquals(CreditStatus.CLOSED, history.get(1).getCreditStatus());
        assertNull(testClient.getActiveCredit());
    }
}
