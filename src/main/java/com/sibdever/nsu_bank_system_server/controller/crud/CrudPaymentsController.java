package com.sibdever.nsu_bank_system_server.controller.crud;

import com.sibdever.nsu_bank_system_server.data.model.entities.Payment;
import com.sibdever.nsu_bank_system_server.service.CrudPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crud/payment")
public class CrudPaymentsController {

    @Autowired
    private CrudPaymentService service;

    @GetMapping("/find-all")
    public Iterable<Payment> findAll() {
        return service.findAll();
    }

}
