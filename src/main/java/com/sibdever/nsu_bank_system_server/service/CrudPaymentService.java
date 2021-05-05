package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.Payment;
import com.sibdever.nsu_bank_system_server.data.repo.CrudPaymentsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrudPaymentService {

    @Autowired
    private CrudPaymentsRepo repo;

    public Iterable<Payment> findAll() {
        return repo.findAll();
    }
}
