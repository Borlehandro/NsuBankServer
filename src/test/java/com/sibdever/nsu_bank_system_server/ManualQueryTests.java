package com.sibdever.nsu_bank_system_server;

import com.sibdever.nsu_bank_system_server.data.model.entities.Client;
import com.sibdever.nsu_bank_system_server.data.model.entities.Offer;
import com.sibdever.nsu_bank_system_server.data.repo.ClientsRepo;
import com.sibdever.nsu_bank_system_server.data.repo.CrudOffersRepo;
import com.sibdever.nsu_bank_system_server.service.ManualQueryService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class ManualQueryTests extends ApplicationTests {

    @Autowired
    private ManualQueryService manualQueryService;

    @BeforeAll
    @Rollback(value = false)
    public static void fillTables(@Autowired ClientsRepo clientsRepo, @Autowired CrudOffersRepo offersRepo) {
        var client1 = clientsRepo.save(new Client("testClient1"));
        var client2 = clientsRepo.save(new Client("testClient2"));
        clientsRepo.save(new Client("testClient3"));
        clientsRepo.save(new Client("testClient4"));
        var offer = offersRepo.save(
                new Offer("test", 2.1, 1, 12, 0, 10000));
        client1.setOffer(offer);
        client2.setOffer(offer);
    }

    @Test
    public void testQuery() {
        System.out.println(manualQueryService.executeManualQuery("""
                select clients.full_name from clients
                """));
    }
}
