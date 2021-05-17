package com.sibdever.nsu_bank_system_server.data.repo;

import com.sibdever.nsu_bank_system_server.data.model.Payment;
import com.sibdever.nsu_bank_system_server.data.model.PaymentChannel;
import com.sibdever.nsu_bank_system_server.data.model.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentsRepo extends JpaRepository<Payment, Integer> {
    List<Payment> findAllByPaymentDetails_ChannelOrderByPaymentDetails_Timestamp(PaymentChannel channel);
    List<Payment> findAllByPaymentDetails_TypeOrderByPaymentDetails_Timestamp(PaymentType type);
    List<Payment> findAllByClient_IdOrderByPaymentDetails_Timestamp(int clientId);
    List<Payment> findAllByCredit_IdOrderByPaymentDetails_Timestamp(int creditId);
}
