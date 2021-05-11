package com.sibdever.nsu_bank_system_server;

import com.sibdever.nsu_bank_system_server.data.model.Client;
import com.sibdever.nsu_bank_system_server.data.model.Offer;
import com.sibdever.nsu_bank_system_server.data.repo.ClientsRepo;
import com.sibdever.nsu_bank_system_server.data.repo.CrudOffersRepo;
import com.sibdever.nsu_bank_system_server.data.repo.OffersHistoryRepo;
import com.sibdever.nsu_bank_system_server.service.ClientsManagementService;
import com.sibdever.nsu_bank_system_server.service.CrudClientService;
import com.sibdever.nsu_bank_system_server.service.CrudOfferService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class NsuBankSystemServerApplicationTests {

	@Autowired
	private OffersHistoryRepo offersHistoryRepo;

	private static Client client;
	private static Offer offer;

	@BeforeAll
	public static void init(@Autowired CrudClientService crudClientService,
							@Autowired ClientsManagementService clientsManagementService,
							@Autowired CrudOfferService crudOfferService) {
		client = crudClientService.createClient(new Client("Test client"));
		offer = crudOfferService.createOffer(new Offer("test", 1.0, 1, 28, 1000, 1000000));
		assertDoesNotThrow(() -> clientsManagementService.setClientOffer(client.getId(), offer.getId()));
	}

	@Test
	void testOfferHistory() {
		var history = offersHistoryRepo.findAll();
		assertEquals(1, List.of(history).size());
		var item = history.iterator().next();
		assertEquals(offer.getId(), item.getId().getOffer().getId());
		assertEquals(client.getId(), item.getId().getClient().getId());
	}

	@BeforeAll
	public static void clear(@Autowired ClientsRepo clientsRepo,
							 @Autowired OffersHistoryRepo offersHistoryRepo,
							 @Autowired CrudOffersRepo crudOffersRepo) {
		clientsRepo.delete(client);
		crudOffersRepo.delete(offer);
		offersHistoryRepo.deleteAll();
	}

}
