package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.Client;
import com.sibdever.nsu_bank_system_server.data.repo.ClientsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientsCrudService {
    @Autowired
    private ClientsRepo clientsRepo;

    public Client createClient(Client client) {
        return clientsRepo.save(client);
    }

    public void deleteClientById(int id) {
        clientsRepo.findById(id).ifPresent((client) -> clientsRepo.delete(client));
    }

    public List<Client> findAllByFullName(String fullName) {
        return clientsRepo.findAllByFullName(fullName);
    }

    public Iterable<Client> findAll() {
        return clientsRepo.findAll();
    }

}
