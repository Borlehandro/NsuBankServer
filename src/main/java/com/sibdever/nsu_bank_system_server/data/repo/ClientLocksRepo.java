package com.sibdever.nsu_bank_system_server.data.repo;

import com.sibdever.nsu_bank_system_server.data.model.entities.ClientLocking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientLocksRepo extends JpaRepository<ClientLocking, Integer> {

}
