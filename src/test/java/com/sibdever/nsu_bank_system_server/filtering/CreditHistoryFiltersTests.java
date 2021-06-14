package com.sibdever.nsu_bank_system_server.filtering;

import com.sibdever.nsu_bank_system_server.ApplicationTests;
import com.sibdever.nsu_bank_system_server.data.filtering.CriteriaOperator;
import com.sibdever.nsu_bank_system_server.data.filtering.credit_history.CreditHistoryCriteriaKey;
import com.sibdever.nsu_bank_system_server.data.filtering.credit_history.CreditHistorySearchCriteria;
import com.sibdever.nsu_bank_system_server.data.filtering.credit_history.CreditsHistorySpecification;
import com.sibdever.nsu_bank_system_server.data.model.entities.*;
import com.sibdever.nsu_bank_system_server.data.repo.ClientsRepo;
import com.sibdever.nsu_bank_system_server.data.repo.CreditHistoryRepo;
import com.sibdever.nsu_bank_system_server.exception.WrongCredentialsException;
import com.sibdever.nsu_bank_system_server.service.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CreditHistoryFiltersTests extends ApplicationTests {
    static Client testClient1;
    static Client testClient2;
    static Offer offer1;
    static Offer offer2;

    @Autowired
    private CreditHistoryRepo creditHistoryRepo;
    @Autowired
    private ClientsManagementService clientsManagementService;
    @Autowired
    private PaymentsManagementService paymentsManagementService;
    @Autowired
    private DailyScheduledService dailyScheduledService;

    @BeforeAll
    @Rollback(value = false)
    public static void createClients(@Autowired CrudClientService crudClientService,
                                     @Autowired CrudOfferService crudOfferService,
                                     @Autowired ClientsManagementService clientsManagementService,
                                     @Autowired ClientsRepo clientsRepo) throws WrongCredentialsException {
        testClient1 = crudClientService.saveClient(new Client("TestClient1"));
        testClient2 = crudClientService.saveClient(new Client("TestClient2"));
        clientsRepo.flush();
        var testList = crudClientService.findAll();
        assertEquals(2, testList.size());
        offer1 = crudOfferService.createOffer(new Offer("test", 1, 1, 100, 0, 1_000_000));
        offer2 = crudOfferService.createOffer(new Offer("test2", 2, 12, 36, 0, 10_000_000));
        clientsManagementService.setClientOffer(testClient1.getId(), offer1.getId());
        clientsManagementService.setClientOffer(testClient2.getId(), offer1.getId());
    }

    @Test
    @Transactional
    public void testFilteringByCreditProfitMargin() throws WrongCredentialsException {
        var credit1 = clientsManagementService.giveCredit(
                testClient1.getId(),
                offer1.getId(),
                12,
                100_000.0,
                PaymentChannel.BANK_ACCOUNT);
        var credit2 = clientsManagementService.giveCredit(
                testClient2.getId(),
                offer1.getId(),
                12,
                100_000.0,
                PaymentChannel.BANK_ACCOUNT);

        paymentsManagementService.processPayment(
                testClient1.getId(),
                credit1.getId(),
                new PaymentDetails (
                        LocalDateTime.now(),
                        PaymentType.REFUND,
                        PaymentChannel.BANK_ACCOUNT,
                        60_000.0
                )
        );

        dailyScheduledService.manageDailyPayments();

        var spec1 = new CreditsHistorySpecification(
                new CreditHistorySearchCriteria(
                        CreditHistoryCriteriaKey.CREDIT_PROFIT_MARGIN,
                        CriteriaOperator.GREATER_OR_EQUALS,
                        0.5)
        );
        var res = creditHistoryRepo.findAll(spec1);
        assertEquals(1, res.size());
        assertEquals(credit1.getId(), res.get(0).getCredit().getId());
    }

    @Test
    @Transactional
    public void testFilteringByStatuses() throws WrongCredentialsException {
        var credit1 = clientsManagementService.giveCredit(
                testClient1.getId(),
                offer1.getId(),
                12,
                100_000.0,
                PaymentChannel.BANK_ACCOUNT);

        var credit2 = clientsManagementService.giveCredit(
                testClient2.getId(),
                offer1.getId(),
                12,
                100_000.0,
                PaymentChannel.BANK_ACCOUNT);

        paymentsManagementService.processPayment(
                testClient1.getId(),
                credit1.getId(),
                new PaymentDetails (
                        LocalDateTime.now(),
                        PaymentType.REFUND,
                        PaymentChannel.BANK_ACCOUNT,
                        110_000.0
                )
        );

        paymentsManagementService.processPayment(
                testClient2.getId(),
                credit2.getId(),
                new PaymentDetails (
                        LocalDateTime.now(),
                        PaymentType.REFUND,
                        PaymentChannel.BANK_ACCOUNT,
                        80_000.0
                )
        );

        dailyScheduledService.manageDailyPayments();

        var spec1 = new CreditsHistorySpecification(
                new CreditHistorySearchCriteria(
                        CreditHistoryCriteriaKey.CREDIT_PROFIT_MARGIN,
                        CriteriaOperator.GREATER_OR_EQUALS,
                        0.5)
        );
        var spec2 = new CreditsHistorySpecification(
                new CreditHistorySearchCriteria(
                        CreditHistoryCriteriaKey.CREDIT_STATUS_NOW,
                        CriteriaOperator.EQUALS,
                        CreditStatus.CLOSED)
        );
        var spec3 = new CreditsHistorySpecification(
                new CreditHistorySearchCriteria(
                        CreditHistoryCriteriaKey.CREDIT_STATUS_IN_RECORD,
                        CriteriaOperator.EQUALS,
                        CreditStatus.ACTIVE)
        );

        System.out.println(creditHistoryRepo.findAll());
        var res = creditHistoryRepo.findAll(spec1);
        assertEquals(3, res.size());
        assertEquals(2, res.stream().filter(item -> item.getCredit().getId() == credit1.getId()).count());
        assertEquals(1, res.stream().filter(item -> item.getCredit().getId() == credit2.getId()).count());
        res = creditHistoryRepo.findAll(Specification.where(spec1).and(spec2));
        assertEquals(2, res.size());
        assertEquals(credit1.getId(), res.get(0).getCredit().getId());
        assertEquals(credit1.getId(), res.get(1).getCredit().getId());
        res = creditHistoryRepo.findAll(Specification.where(spec1).and(spec2).and(spec3));
        assertEquals(1, res.size());
        System.out.println(res.get(0).getCredit().getProfitMargin());
        assertEquals(credit1.getId(), res.get(0).getCredit().getId());
    }

}