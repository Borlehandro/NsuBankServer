package com.sibdever.nsu_bank_system_server.data.repo;

import com.sibdever.nsu_bank_system_server.data.model.ClientLocking;
import org.springframework.data.repository.CrudRepository;

public interface ClientLocksRepo extends CrudRepository<ClientLocking, Integer> {

}
