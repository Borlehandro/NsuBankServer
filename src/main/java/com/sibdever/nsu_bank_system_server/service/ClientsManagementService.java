package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.ClientLocking;
import com.sibdever.nsu_bank_system_server.data.repo.ClientLocksRepo;
import com.sibdever.nsu_bank_system_server.data.repo.ClientsRepo;
import com.sibdever.nsu_bank_system_server.exception.WrongCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

@Service
public class ClientsManagementService {

    @Autowired
    private ClientsRepo clientsRepo;
    @Autowired
    private ClientLocksRepo locksRepo;

    public void lockClient(int id, Duration lockingDuration) throws WrongCredentialsException {
        var opt = clientsRepo.findById(id);

        if(opt.isPresent()) {
            var client = opt.get();
            var now = LocalDateTime.now();
            locksRepo.save(new ClientLocking(client, now, now.plus(lockingDuration)));
            client.setBlocked(true);
            clientsRepo.save(client);
        } else {
            throw new WrongCredentialsException("Client not found");
        }
    }

}
