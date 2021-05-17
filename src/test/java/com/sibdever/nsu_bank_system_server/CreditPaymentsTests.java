package com.sibdever.nsu_bank_system_server;

import com.sibdever.nsu_bank_system_server.data.model.*;
import com.sibdever.nsu_bank_system_server.data.repo.ClientsRepo;
import com.sibdever.nsu_bank_system_server.data.repo.CreditsRepo;
import com.sibdever.nsu_bank_system_server.service.ClientsManagementService;
import com.sibdever.nsu_bank_system_server.service.CreditsManagementService;
import com.sibdever.nsu_bank_system_server.service.CrudOfferService;
import com.sibdever.nsu_bank_system_server.service.PaymentsManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CreditPaymentsTests extends ApplicationTests {

	@Autowired
	private ClientsRepo clientsRepo;
	@Autowired
	private CrudOfferService crudOfferService;
	@Autowired
	private ClientsManagementService clientsManagementService;
	@Autowired
	private CreditsManagementService creditsManagementService;
	@Autowired
	private CreditsRepo creditsRepo;
	@Autowired
	private PaymentsManagementService paymentsManagementService;

	@Transactional
	@Test
	public void testCreditPaymentSelection() {
		var client = clientsRepo.save(new Client("Test"));
		var offer = crudOfferService.createOffer(new Offer("test", 1.0, 1, 100, 0, 100000000));
		Credit[] credit = new Credit[1];
		Payment[] payments = new Payment[2];
		assertDoesNotThrow(() -> clientsManagementService.setClientOffer(client.getId(), offer.getId()));
		assertDoesNotThrow(() -> credit[0] = clientsManagementService.giveCredit(client.getId(), offer.getId(), 30, 10000.0, PaymentChannel.BANK_ACCOUNT));
		assertDoesNotThrow(() -> payments[0] = paymentsManagementService.processPayment(client.getId(), credit[0].getId(), new PaymentDetails(LocalDateTime.now().plus(1, ChronoUnit.DAYS), PaymentType.REFUND, PaymentChannel.BANK_ACCOUNT, 100.0)));
		assertDoesNotThrow(() -> payments[1] = paymentsManagementService.processPayment(client.getId(), credit[0].getId(), new PaymentDetails(LocalDateTime.now().plus(2, ChronoUnit.DAYS), PaymentType.REFUND, PaymentChannel.BANK_ACCOUNT, 120.0)));

		var creditPayments  = creditsRepo
				.findAllJoinPaymentsAfterTime(credit[0].getStartDate()
						.plus(1, ChronoUnit.DAYS)
						.plus(1, ChronoUnit.HOURS))
				.stream()
				.collect(Collectors.groupingBy(
						(Object[] item) -> (Credit)item[0],
						Collectors.mapping((Object[] item) -> (Payment)item[1], Collectors.toList())
				));

		assertTrue(creditPayments.containsKey(credit[0]));
		assertFalse(creditPayments.get(credit[0]).contains(payments[0]));
		assertTrue(creditPayments.get(credit[0]).contains(payments[1]));
		assertEquals(1, creditPayments.get(credit[0]).size());
	}

}
