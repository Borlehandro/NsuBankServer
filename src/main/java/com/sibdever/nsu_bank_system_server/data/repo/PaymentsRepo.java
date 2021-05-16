package com.sibdever.nsu_bank_system_server.data.repo;

import com.sibdever.nsu_bank_system_server.data.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentsRepo extends JpaRepository<Payment, Integer> {

}
