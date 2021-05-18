package com.sibdever.nsu_bank_system_server;


import com.sibdever.nsu_bank_system_server.data.filtering.credits_table.CreditTableCriteriaKey;
import com.sibdever.nsu_bank_system_server.data.filtering.credits_table.CreditTableSearchCriteria;
import com.sibdever.nsu_bank_system_server.data.filtering.credits_table.CreditsTableSpecification;
import com.sibdever.nsu_bank_system_server.data.filtering.CriteriaOperator;
import com.sibdever.nsu_bank_system_server.data.model.entities.Client;
import com.sibdever.nsu_bank_system_server.data.model.entities.Offer;
import com.sibdever.nsu_bank_system_server.data.model.entities.PaymentChannel;
import com.sibdever.nsu_bank_system_server.data.repo.ClientsRepo;
import com.sibdever.nsu_bank_system_server.data.repo.CreditTableRepo;
import com.sibdever.nsu_bank_system_server.exception.WrongCredentialsException;
import com.sibdever.nsu_bank_system_server.service.ClientsManagementService;
import com.sibdever.nsu_bank_system_server.service.CrudClientService;
import com.sibdever.nsu_bank_system_server.service.CrudOfferService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditTableFiltersTests extends ApplicationTests {

    static Client testClient1;
    static Client testClient2;
    static Offer offer1;
    static Offer offer2;

    @Autowired
    private CreditTableRepo creditTableRepo;
    @Autowired
    private ClientsManagementService clientsManagementService;

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
    public void testCreditSumFilter() throws WrongCredentialsException {
        var credit1 = clientsManagementService.giveCredit(testClient1.getId(), offer1.getId(), 12, 50000.0, PaymentChannel.CREDIT_CARD);
        var credit2 = clientsManagementService.giveCredit(testClient2.getId(), offer1.getId(), 12, 9000.0, PaymentChannel.CREDIT_CARD);
        var creditsTableSpecification1 = new CreditsTableSpecification(
                new CreditTableSearchCriteria(
                        CreditTableCriteriaKey.CREDIT_SUM,
                        CriteriaOperator.EQUALS,
                        50000.0
                )
        );
        var creditsTableSpecification2 = new CreditsTableSpecification(
                new CreditTableSearchCriteria(
                        CreditTableCriteriaKey.CREDIT_SUM,
                        CriteriaOperator.GREATER,
                        9000.0
                )
        );
        var creditsTableSpecification3 = new CreditsTableSpecification(
                new CreditTableSearchCriteria(
                        CreditTableCriteriaKey.CREDIT_SUM,
                        CriteriaOperator.GREATER_OR_EQUALS,
                        9000.0
                )
        );
        var res = creditTableRepo.findAll(creditsTableSpecification1);
        res.forEach(item -> {
            System.out.println(item);
            assertEquals(credit1.getId(), item.getId().getCredit().getId());
        });
        res = creditTableRepo.findAll(creditsTableSpecification2);
        res.forEach(item -> {
            System.out.println(item);
            assertEquals(credit1.getId(), item.getId().getCredit().getId());
        });
        res = creditTableRepo.findAll(creditsTableSpecification3);
        assertEquals(12,
                res.stream()
                        .filter(item -> (item.getId().getCredit().getId() == credit1.getId()))
                        .count()
        );
        assertEquals(12,
                res.stream()
                        .filter(item -> (item.getId().getCredit().getId() == credit2.getId()))
                        .count()
        );
    }

    @Test
    @Transactional
    public void testMultipleSpecificationsSearch() throws WrongCredentialsException {

        var credit1 = clientsManagementService.giveCredit(testClient1.getId(), offer1.getId(), 12, 50000.0, PaymentChannel.CREDIT_CARD);
        var credit2 = clientsManagementService.giveCredit(testClient2.getId(), offer1.getId(), 12, 9000.0, PaymentChannel.CREDIT_CARD);
        var credit3 = clientsManagementService.giveCredit(testClient1.getId(), offer1.getId(), 9, 50000.0, PaymentChannel.CREDIT_CARD);

        var specification1 = new CreditsTableSpecification(
                new CreditTableSearchCriteria(
                        CreditTableCriteriaKey.CREDIT_SUM,
                        CriteriaOperator.EQUALS,
                        50000.0
                )
        );
        var specification2 = new CreditsTableSpecification(
                new CreditTableSearchCriteria(
                        CreditTableCriteriaKey.CREDIT_MONTH,
                        CriteriaOperator.EQUALS,
                        12
                )
        );
        var specification3 = new CreditsTableSpecification(
                new CreditTableSearchCriteria(
                        CreditTableCriteriaKey.TABLE_DATE,
                        CriteriaOperator.GREATER,
                        credit1.getStartDate().plus(1, ChronoUnit.MONTHS)
                )
        );
        var res = creditTableRepo.findAll(
                Specification
                        .where(specification1)
                        .and(specification2)
                        .and(specification3));
        assertEquals(11, res.size());
        res.forEach(item -> {
            System.out.println(item);
            assertEquals(credit1.getId(), item.getId().getCredit().getId());
        });
    }
}
