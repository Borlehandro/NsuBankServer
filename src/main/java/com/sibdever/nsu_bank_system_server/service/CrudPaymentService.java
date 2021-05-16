package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.Payment;
import com.sibdever.nsu_bank_system_server.data.repo.PaymentsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CrudPaymentService {

    @Autowired
    private PaymentsRepo repo;

    public Iterable<Payment> findAll() {
        return repo.findAll();
    }
}
