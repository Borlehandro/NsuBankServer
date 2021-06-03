package com.sibdever.nsu_bank_system_server.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sibdever.nsu_bank_system_server.NsuBankSystemServerApplication;
import com.sibdever.nsu_bank_system_server.data.model.entities.*;
import com.sibdever.nsu_bank_system_server.data.model.request.OperatorRegisterCredentials;
import com.sibdever.nsu_bank_system_server.exception.WrongCredentialsException;
import com.sibdever.nsu_bank_system_server.service.ClientsManagementService;
import com.sibdever.nsu_bank_system_server.service.CrudOfferService;
import com.sibdever.nsu_bank_system_server.service.PaymentsManagementService;
import com.sibdever.nsu_bank_system_server.service.filtering.PaymentsFilteringService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(value = "SpringBootTest.WebEnvironment.MOCK", classes = NsuBankSystemServerApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:integration-test.properties")
public class PaymentsListTests {

    @Autowired
    private MockMvc mockMvc;

//    @MockBean
//    private PaymentsFilteringService paymentsFilteringService;

    @Autowired
    private ClientsManagementService clientsManagementService;
    @Autowired
    private CrudOfferService offerService;
    @Autowired
    private PaymentsManagementService paymentsManagementService;

    private static String operatorToken;
    private static String operatorName;
    private static String password;

    @BeforeAll
    @Rollback(value = false)
    public static void registerUser(@Autowired MockMvc mockMvc) throws Exception {
        operatorName = "tester";
        password = "123456";
        var credentials = new OperatorRegisterCredentials(
                operatorName,
                "Alex B",
                password,
                "test_test"
        );

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(credentials))
        ).andExpect(status().isOk());

        // Todo refactor
        MvcResult result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"tester\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andReturn();
        operatorToken = result.getResponse().getHeader("Authorization");
    }

    @Test
    @Transactional
    void testPaymentsListController() throws Exception {
        var client = clientsManagementService.addClient(new Client("TesterClient"));
        var offer = offerService.createOffer(new Offer("test", 1.0, 1, 20, 0, 1000000000));
        clientsManagementService.setClientOffer(client.getId(), offer.getId());
        var credit = clientsManagementService.giveCredit(client.getId(), offer.getId(), 12, 5000, PaymentChannel.CREDIT_CARD);
        System.out.println(operatorToken);

        for (int i = 0; i < 10; ++i) {
            var payment =
                    new PaymentDetails(
                            LocalDateTime.now(),
                            PaymentType.REFUND,
                            PaymentChannel.CREDIT_CARD,
                            100.0
                    );
            paymentsManagementService.processPayment(client.getId(), credit.getId(), payment);
            payment.setPaymentSum(2000.0);
            paymentsManagementService.processPayment(client.getId(), credit.getId(), payment);

        }

        MvcResult result = mockMvc.perform(get("/list/payments")
                .header("Authorization", operatorToken)
                .param("filter", "PAYMENT_SUM>:1000")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

}
