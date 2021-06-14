package com.sibdever.nsu_bank_system_server.service.filtering;

import com.sibdever.nsu_bank_system_server.data.filtering.payments.PaymentsSpecification;
import com.sibdever.nsu_bank_system_server.data.model.entities.Payment;
import com.sibdever.nsu_bank_system_server.data.repo.PaymentsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentsFilteringService {

    @Autowired
    private PaymentsRepo paymentsRepo;

    public Page<Payment> getPaymentsPageBySpecification(PaymentsSpecification specification, Pageable pageable) {
        return paymentsRepo.findAll(specification, pageable);
    }

    public Page<Payment> getPaymentsPage(Pageable pageable) {
        return paymentsRepo.findAll(pageable);

    }

}
