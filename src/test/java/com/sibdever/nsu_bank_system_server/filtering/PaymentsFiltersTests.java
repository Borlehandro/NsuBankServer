package com.sibdever.nsu_bank_system_server.filtering;

import com.sibdever.nsu_bank_system_server.ApplicationTests;
import com.sibdever.nsu_bank_system_server.data.filtering.CriteriaOperator;
import com.sibdever.nsu_bank_system_server.data.filtering.credit_history.CreditHistoryCriteriaKey;
import com.sibdever.nsu_bank_system_server.data.filtering.credit_history.CreditHistorySearchCriteria;
import com.sibdever.nsu_bank_system_server.data.filtering.credit_history.CreditsHistorySpecification;
import com.sibdever.nsu_bank_system_server.data.filtering.payments.PaymentCriteriaKey;
import com.sibdever.nsu_bank_system_server.data.filtering.payments.PaymentSearchCriteria;
import com.sibdever.nsu_bank_system_server.data.filtering.payments.PaymentsSpecification;
import com.sibdever.nsu_bank_system_server.data.model.entities.*;
import com.sibdever.nsu_bank_system_server.data.repo.ClientsRepo;
import com.sibdever.nsu_bank_system_server.data.repo.CreditTableRepo;
import com.sibdever.nsu_bank_system_server.data.repo.PaymentsRepo;
import com.sibdever.nsu_bank_system_server.exception.WrongCredentialsException;
import com.sibdever.nsu_bank_system_server.service.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentsFiltersTests extends ApplicationTests {
    static Client testClient1;
    static Client testClient2;
    static Offer offer1;
    static Offer offer2;

    @Autowired
    private PaymentsRepo paymentsRepo;
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
        testClient1 = crudClientService.createClient(new Client("TestClient1"));
        testClient2 = crudClientService.createClient(new Client("TestClient2"));
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
    public void testFilteringByCreditProfitMarginAndType() throws WrongCredentialsException {
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
                new PaymentDetails(
                        LocalDateTime.now(),
                        PaymentType.REFUND,
                        PaymentChannel.BANK_ACCOUNT,
                        1_000.0
                )
        );

        paymentsManagementService.processPayment(
                testClient1.getId(),
                credit1.getId(),
                new PaymentDetails(
                        LocalDateTime.now(),
                        PaymentType.REFUND,
                        PaymentChannel.BANK_ACCOUNT,
                        2_000.0
                )
        );

        paymentsManagementService.processPayment(
                testClient1.getId(),
                credit1.getId(),
                new PaymentDetails(
                        LocalDateTime.now(),
                        PaymentType.REFUND,
                        PaymentChannel.BANK_ACCOUNT,
                        60_000.0
                )
        );

        paymentsManagementService.processPayment(
                testClient2.getId(),
                credit2.getId(),
                new PaymentDetails(
                        LocalDateTime.now(),
                        PaymentType.REFUND,
                        PaymentChannel.BANK_ACCOUNT,
                        65_000.0
                )
        );

        dailyScheduledService.manageDailyPayments();

        var spec1 = new PaymentsSpecification(
                new PaymentSearchCriteria(
                        PaymentCriteriaKey.CREDIT_PROFIT_MARGIN,
                        CriteriaOperator.GREATER_OR_EQUALS,
                        0.5)
        );
        var spec2 = new PaymentsSpecification(
                new PaymentSearchCriteria(
                        PaymentCriteriaKey.PAYMENT_TYPE,
                        CriteriaOperator.EQUALS,
                        PaymentType.REFUND)
        );

        var res = paymentsRepo.findAll(spec1);
        assertEquals(6, res.size());
        res = paymentsRepo.findAll(Specification.where(spec1).and(spec2));
        assertEquals(4, res.size());
    }

}
