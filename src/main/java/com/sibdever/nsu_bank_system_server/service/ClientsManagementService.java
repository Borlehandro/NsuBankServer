package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.*;
import com.sibdever.nsu_bank_system_server.data.repo.ClientLocksRepo;
import com.sibdever.nsu_bank_system_server.data.repo.ClientsRepo;
import com.sibdever.nsu_bank_system_server.data.repo.CrudOffersRepo;
import com.sibdever.nsu_bank_system_server.data.repo.OffersHistoryRepo;
import com.sibdever.nsu_bank_system_server.exception.WrongCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class ClientsManagementService {

    @Autowired
    private ClientsRepo clientsRepo;
    @Autowired
    private CrudOffersRepo offersRepo;
    @Autowired
    private ClientLocksRepo locksRepo;
    @Autowired
    private CreditsManagementService creditsService;
    @Autowired
    private OffersHistoryRepo offersHistoryRepo;

    public void lockClient(int id, Duration lockingDuration) throws WrongCredentialsException {
        var opt = clientsRepo.findById(id);

        if(opt.isPresent()) {
            var client = opt.get();
            var now = LocalDateTime.now();
            locksRepo.save(new ClientLocking(client, now, now.plus(lockingDuration)));
            clientsRepo.save(client);
        } else {
            throw new WrongCredentialsException("Client not found");
        }
    }

    public void setClientOffer(int clientId, int offerId) throws WrongCredentialsException {
        var optClient = clientsRepo.findById(clientId);
        var optOffer = offersRepo.findById(offerId);

        if(optClient.isPresent() && optOffer.isPresent()) {
            var client = optClient.get();
            var offer = optOffer.get();
            client.setOffer(offer);
            offersHistoryRepo.save(new OfferHistoryRecord(new OffersHistoryId(client, offer, LocalDateTime.now())));
            clientsRepo.save(client);
        } else {
            throw new WrongCredentialsException("Client not found");
        }
    }

    public Credit giveCredit(int clientId, int offerId, int monthPeriod, double sum, PaymentChannel paymentChannel)
            throws WrongCredentialsException {

        var optClient = clientsRepo.findById(clientId);
        if(optClient.isPresent()) {
            var client = optClient.get();
            if(client.getOffer() != null) {
                var offer = client.getOffer();
                System.out.println("AFTER GET OFFER: " + offer);
                System.out.println("AFTER GET OFFER: " + offer.getPercentsPerMonth());
                if(offerId == offer.getId()) {
                    if (sum <= offer.getMaximumSum()
                            && sum >= offer.getMinimumSum()
                            && monthPeriod <= offer.getMaximumMonthPeriod()
                            && monthPeriod >= offer.getMinimumMonthPeriod()) {
                        return creditsService.giveCredit(client, offer, monthPeriod, sum, paymentChannel);
                    } else
                        throw new WrongCredentialsException("Wrong credit parameters");
                } else
                    throw new WrongCredentialsException("Wrong offer id");
            } else
                throw new WrongCredentialsException("This client has no offer");

        } else
            throw new WrongCredentialsException("Client not found");
    }

}
