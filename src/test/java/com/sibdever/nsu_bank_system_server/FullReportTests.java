package com.sibdever.nsu_bank_system_server;

import com.sibdever.nsu_bank_system_server.data.model.entities.Client;
import com.sibdever.nsu_bank_system_server.data.repo.ClientsRepo;
import com.sibdever.nsu_bank_system_server.data.repo.FullReportRepoImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FullReportTests extends ApplicationTests {

    static Client testClient;

    @Autowired
    private FullReportRepoImpl fullReportRepo;

    @BeforeAll
    public static void init(@Autowired ClientsRepo clientsRepo) {
        testClient = clientsRepo.saveAndFlush(new Client("TestFullName"));
    }
    @Test
    public void testFullReport() {
        fullReportRepo.getFullReportTest(testClient.getId()).forEach(System.out::println);
    }
}
