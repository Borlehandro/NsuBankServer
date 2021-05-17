package com.sibdever.nsu_bank_system_server;

import com.sibdever.nsu_bank_system_server.data.model.entities.Client;
import com.sibdever.nsu_bank_system_server.data.model.entities.Offer;
import com.sibdever.nsu_bank_system_server.data.model.entities.PaymentChannel;
import com.sibdever.nsu_bank_system_server.exception.WrongCredentialsException;
import com.sibdever.nsu_bank_system_server.service.ClientsManagementService;
import com.sibdever.nsu_bank_system_server.service.CrudClientService;
import com.sibdever.nsu_bank_system_server.service.CrudOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(value = "SpringBootTest.WebEnvironment.MOCK", classes = NsuBankSystemServerApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:integration-test.properties")
public class ApplicationTests {

}
