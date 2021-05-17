package com.sibdever.nsu_bank_system_server;

import com.sibdever.nsu_bank_system_server.data.model.entities.*;
import com.sibdever.nsu_bank_system_server.exception.WrongCredentialsException;
import com.sibdever.nsu_bank_system_server.service.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CreditHistoryTests extends ApplicationTests {

    @Autowired
    private CreditHistoryService creditHistoryService;
    @Autowired
    private PaymentsManagementService paymentsManagementService;
    @Autowired
    private DailyScheduledService dailyScheduledService;

    private static Client testClient;
    private static Credit testCredit;

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
        var trueTable = creditHistoryService.getCreditTableForCredit(testCredit.getId());
        assertNotNull(trueTable);
        assertFalse(trueTable.isEmpty());
        var clientActiveTable = creditHistoryService.getActiveCreditTable(testClient.getId());
        assertNotNull(clientActiveTable);
        assertFalse(clientActiveTable.isEmpty());
        assertArrayEquals(trueTable.toArray(), clientActiveTable.toArray());
        var allCreditsTableForClient = creditHistoryService
                .getAllCreditTablesForClient(testClient.getId());
        assertEquals(1, allCreditsTableForClient.keySet().size());
        assertTrue(allCreditsTableForClient.containsKey(testCredit));
        assertArrayEquals(trueTable.toArray(), allCreditsTableForClient.get(testCredit).toArray());
    }

    @Test
    @Transactional
    public void testCreditHistoryActive() {
        var creditHistory = creditHistoryService.getCreditHistory(testCredit.getId());
        assertEquals(1, creditHistory.size());
        assertEquals(CreditStatus.ACTIVE, creditHistory.get(0).getCreditStatus());
        var allCreditsHistoryForClient = creditHistoryService
                .getAllCreditsHistoryForClient(testClient.getId());
        assertEquals(1, allCreditsHistoryForClient.keySet().size());
        assertTrue(allCreditsHistoryForClient.containsKey(testCredit));
        assertArrayEquals(creditHistory.toArray(), allCreditsHistoryForClient.get(testCredit).toArray());
    }

    @Test
    @Transactional
    public void testCreditHistoryClosed() throws WrongCredentialsException {
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

        var creditHistory = creditHistoryService.getCreditHistory(testCredit.getId());

        assertEquals(2, creditHistory.size());
        assertEquals(CreditStatus.ACTIVE, creditHistory.get(0).getCreditStatus());
        assertEquals(CreditStatus.CLOSED, creditHistory.get(1).getCreditStatus());
    }

}
