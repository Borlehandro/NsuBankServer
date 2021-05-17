package com.sibdever.nsu_bank_system_server;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(value = "SpringBootTest.WebEnvironment.MOCK", classes = NsuBankSystemServerApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:integration-test.properties")
public class ApplicationTests {

}
