package com.sibdever.nsu_bank_system_server.data.repo;

import com.sibdever.nsu_bank_system_server.data.model.entities.Payment;
import com.sibdever.nsu_bank_system_server.data.model.entities.PaymentChannel;
import com.sibdever.nsu_bank_system_server.data.model.entities.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentsRepo extends JpaRepository<Payment, Integer>, JpaSpecificationExecutor<Payment> {

}
