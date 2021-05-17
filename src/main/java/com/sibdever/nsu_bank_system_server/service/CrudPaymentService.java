package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.entities.Payment;
import com.sibdever.nsu_bank_system_server.data.model.entities.PaymentChannel;
import com.sibdever.nsu_bank_system_server.data.model.entities.PaymentType;
import com.sibdever.nsu_bank_system_server.data.repo.PaymentsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CrudPaymentService {

    @Autowired
    private PaymentsRepo repo;

    public Iterable<Payment> findAll() {
        return repo.findAll();
    }

    public List<Payment> findAllByChannel(PaymentChannel channel) {
        return repo.findAllByPaymentDetails_ChannelOrderByPaymentDetails_Timestamp(channel);
    }

    public List<Payment> findAllByType(PaymentType type) {
        return repo.findAllByPaymentDetails_TypeOrderByPaymentDetails_Timestamp(type);
    }

    public List<Payment> findAllByCreditId(int creditId) {
        return repo.findAllByCredit_IdOrderByPaymentDetails_Timestamp(creditId);
    }

    public List<Payment> findAllByClientId(int clientId) {
        return repo.findAllByClient_IdOrderByPaymentDetails_Timestamp(clientId);
    }


}
