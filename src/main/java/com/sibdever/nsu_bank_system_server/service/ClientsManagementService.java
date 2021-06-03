package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.ClientStatus;
import com.sibdever.nsu_bank_system_server.data.model.entities.*;
import com.sibdever.nsu_bank_system_server.data.repo.ClientLocksRepo;
import com.sibdever.nsu_bank_system_server.data.repo.ClientsRepo;
import com.sibdever.nsu_bank_system_server.data.repo.CrudOffersRepo;
import com.sibdever.nsu_bank_system_server.data.repo.OffersHistoryRepo;
import com.sibdever.nsu_bank_system_server.exception.WrongCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    @Transactional
    public void lockClient(int id, Duration lockingDuration) throws WrongCredentialsException {
        var opt = clientsRepo.findById(id);
        if (opt.isPresent()) {
            var client = opt.get();
            var now = LocalDateTime.now();
            client.setClientStatus(ClientStatus.BLOCKED);
            locksRepo.save(new ClientLocking(client, now, now.plus(lockingDuration)));
            clientsRepo.save(client);
        } else {
            throw new WrongCredentialsException("Client not found");
        }
    }

    @Transactional
    public Client addClient(Client client) {
        return clientsRepo.save(client);
    }

    @Transactional
    public void setClientOffer(int clientId, int offerId) throws WrongCredentialsException {
        var optClient = clientsRepo.findById(clientId);
        var optOffer = offersRepo.findById(offerId);
        if (optClient.isPresent() && optOffer.isPresent()) {
            var client = optClient.get();
            checkClientLock(client);
            var offer = optOffer.get();
            client.setOffer(offer);
            if(client.getActiveCredit() == null)
                client.setClientStatus(ClientStatus.OFFERED_WITHOUT_CREDIT);
            offersHistoryRepo.save(new OfferHistoryRecord(new OffersHistoryId(client, offer, LocalDateTime.now())));
        } else {
            throw new WrongCredentialsException("Client not found");
        }
    }

    @Transactional
    public Credit giveCredit(int clientId, int offerId, int monthPeriod, double sum, PaymentChannel paymentChannel)
            throws WrongCredentialsException {
        var optClient = clientsRepo.findById(clientId);
        if (optClient.isPresent()) {
            var client = optClient.get();
            checkClientLock(client);
            if (client.getOffer() != null) {
                var offer = client.getOffer();
                if(!client.getClientStatus().equals(ClientStatus.WITH_CREDIT)) {
                    if (offerId == offer.getId()) {
                        if (sum <= offer.getMaximumSum()
                                && sum >= offer.getMinimumSum()
                                && monthPeriod <= offer.getMaximumMonthPeriod()
                                && monthPeriod >= offer.getMinimumMonthPeriod()) {
                            client.setClientStatus(ClientStatus.WITH_CREDIT);
                            return creditsService.giveCredit(client, offer, monthPeriod, sum, paymentChannel);
                        } else
                            throw new WrongCredentialsException("Wrong credit parameters");
                    } else
                        throw new WrongCredentialsException("Wrong offer id");
                } else
                    throw new WrongCredentialsException("Already has credit");
            } else
                throw new WrongCredentialsException("This client has no offer");

        } else
            throw new WrongCredentialsException("Client not found");
    }

    @Transactional
    public void unlockClient(int clientId) throws WrongCredentialsException {
        var opt = clientsRepo.findById(clientId);
        if(opt.isPresent()) {
            var optLock = locksRepo.findById(clientId);
            optLock.ifPresent(clientLocking -> locksRepo.delete(clientLocking));
        } else
            throw new WrongCredentialsException("Client not found");
    }

    private void checkClientLock(Client client) throws WrongCredentialsException {
        var optLock = locksRepo.findById(client.getId());
        if (optLock.isPresent()) {
            if (optLock.get().getLockingEnd().isAfter(LocalDateTime.now())) {
                throw new WrongCredentialsException("Client is locked");
            } else {
                if(client.getOffer() != null)
                    if(client.getActiveCredit() != null)
                        client.setClientStatus(ClientStatus.WITH_CREDIT);
                    else
                        client.setClientStatus(ClientStatus.OFFERED_WITHOUT_CREDIT);
                else
                    client.setClientStatus(ClientStatus.WITHOUT_OFFER);
                locksRepo.delete(optLock.get());
                locksRepo.flush();
            }
        }
    }

}
