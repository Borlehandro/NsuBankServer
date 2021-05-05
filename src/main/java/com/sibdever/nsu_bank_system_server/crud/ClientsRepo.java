package com.sibdever.nsu_bank_system_server.crud;

import com.sibdever.nsu_bank_system_server.client.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientsRepo extends CrudRepository<Client, Integer> {
    List<Client> findAllByFullName(String fullName);
}
