package com.sibdever.nsu_bank_system_server.controller;

import com.sibdever.nsu_bank_system_server.data.model.entities.Credit;
import com.sibdever.nsu_bank_system_server.data.model.request.ClientLockRequest;
import com.sibdever.nsu_bank_system_server.data.model.request.GiveCreditRequest;
import com.sibdever.nsu_bank_system_server.data.model.request.SetOfferRequest;
import com.sibdever.nsu_bank_system_server.exception.WrongCredentialsException;
import com.sibdever.nsu_bank_system_server.service.ClientsManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/client")
public class ClientsManagementController {

    @Autowired
    private ClientsManagementService clientsManagementService;

    @PostMapping("/lock")
    public void lockClient(@RequestBody ClientLockRequest request) throws WrongCredentialsException {
        clientsManagementService.lockClient(
                request.getClientId(),
                Duration.ofDays(request.getDays())
                        .plusHours(request.getHours())
                        .plusMinutes(request.getMinutes())
                        .plusSeconds(request.getSeconds())
        );
    }

    @PostMapping("/unlock")
    public void unlockClient(int clientId) throws WrongCredentialsException {
        clientsManagementService.unlockClient(clientId);
    }

    @PostMapping("/set-offer")
    public void setOffer(@RequestBody SetOfferRequest request) throws WrongCredentialsException {
        clientsManagementService.setClientOffer(request.getClientId(), request.getOfferId());
    }

    @PostMapping("/give-credit")
    public Credit giveCredit(@RequestBody GiveCreditRequest request) throws WrongCredentialsException {
        return clientsManagementService.giveCredit(
                request.getClientId(),
                request.getOfferId(),
                request.getMonthPeriod(),
                request.getSum(),
                request.getPaymentChannel());
    }
}