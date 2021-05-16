package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.Payment;
import com.sibdever.nsu_bank_system_server.data.model.PaymentDetails;
import com.sibdever.nsu_bank_system_server.data.repo.ClientsRepo;
import com.sibdever.nsu_bank_system_server.data.repo.CreditsRepo;
import com.sibdever.nsu_bank_system_server.data.repo.PaymentsRepo;
import com.sibdever.nsu_bank_system_server.exception.WrongCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PaymentsManagementService {

    @Autowired
    private ClientsRepo clientsRepo;
    @Autowired
    private CreditsRepo creditsRepo;
    @Autowired
    private PaymentsRepo paymentsRepo;

    @Transactional
    public Payment processPayment(int clientId, int creditId, PaymentDetails details) throws WrongCredentialsException {
        var optClient = clientsRepo.findById(clientId);
        if(optClient.isPresent()) {
            var client = optClient.get();
            var optCredit = creditsRepo.findById(creditId);
            if(optCredit.isPresent()) {
                var credit = optCredit.get();
                if(credit.getClient().getId() == client.getId()) {
                    return paymentsRepo.save(new Payment(client, credit, details));
                } else throw new WrongCredentialsException(
                        String.format("Wrong creditId %d for clientId %d", creditId, clientId)
                );
            } else throw new WrongCredentialsException("Credit does not exist");
        } else
            throw new WrongCredentialsException("Client does not exist");
    }
}
