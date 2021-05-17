package com.sibdever.nsu_bank_system_server;


import com.sibdever.nsu_bank_system_server.data.filtering.CreditSearchCriteria;
import com.sibdever.nsu_bank_system_server.data.filtering.CreditsSpecification;
import com.sibdever.nsu_bank_system_server.data.filtering.Operation;
import com.sibdever.nsu_bank_system_server.data.model.entities.Client;
import com.sibdever.nsu_bank_system_server.data.model.entities.Credit;
import com.sibdever.nsu_bank_system_server.data.model.entities.Offer;
import com.sibdever.nsu_bank_system_server.data.model.entities.PaymentChannel;
import com.sibdever.nsu_bank_system_server.data.repo.ClientsRepo;
import com.sibdever.nsu_bank_system_server.data.repo.CreditsRepo;
import com.sibdever.nsu_bank_system_server.exception.WrongCredentialsException;
import com.sibdever.nsu_bank_system_server.service.ClientsManagementService;
import com.sibdever.nsu_bank_system_server.service.CrudClientService;
import com.sibdever.nsu_bank_system_server.service.CrudOfferService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditFiltersTests extends ApplicationTests {

    static Client testClient1;
    static Client testClient2;
    static Offer offer1;

    @Autowired
    private CreditsRepo creditsRepo;
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
        offer1 = crudOfferService.createOffer(new Offer("test", 1, 1, 100, 0, 1000000));
        clientsManagementService.setClientOffer(testClient1.getId(), offer1.getId());
        clientsManagementService.setClientOffer(testClient2.getId(), offer1.getId());
    }

    @Test
    @Transactional
    public void testClientNameFilter() throws WrongCredentialsException {
        clientsManagementService.giveCredit(testClient1.getId(), offer1.getId(), 12, 50000, PaymentChannel.CREDIT_CARD);
        clientsManagementService.giveCredit(testClient2.getId(), offer1.getId(), 12, 9000, PaymentChannel.CREDIT_CARD);
        CreditsSpecification creditsSpecification = new CreditsSpecification(new CreditSearchCriteria("id", Operation.EQUALS, 2));
        assertEquals(1, creditsRepo.findAll(creditsSpecification).size());
        creditsSpecification = new CreditsSpecification(new CreditSearchCriteria("sum", Operation.GREATER, 10000));
        assertEquals(1, creditsRepo.findAll(creditsSpecification).size());
    }
}
