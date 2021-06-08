package com.sibdever.nsu_bank_system_server.controller;

import com.sibdever.nsu_bank_system_server.data.model.entities.Payment;
import com.sibdever.nsu_bank_system_server.data.model.request.PaymentRequest;
import com.sibdever.nsu_bank_system_server.exception.WrongCredentialsException;
import com.sibdever.nsu_bank_system_server.service.PaymentsManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentsManagementController {
    

    @Autowired
    private PaymentsManagementService service;

    @PostMapping("/process")
    public @ResponseBody
    Payment processPayment(@RequestBody PaymentRequest request) throws WrongCredentialsException {
        return service.processPayment(request.getUserId(), request.getCreditId(), request.getDetails());
    }
}
