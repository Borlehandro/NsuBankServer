package com.sibdever.nsu_bank_system_server.controller.crud;

import com.sibdever.nsu_bank_system_server.data.model.entities.Credit;
import com.sibdever.nsu_bank_system_server.service.CrudCreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crud/credit")
public class CrudCreditsController {
    @Autowired
    private CrudCreditService service;

    @GetMapping("/find-all")
    public Iterable<Credit> findAll() {
        return service.findAll();
    }

    @GetMapping("/find")
    public Credit find(int id) {
        return service.findById(id);
    }

}
