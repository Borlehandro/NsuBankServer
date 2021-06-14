package com.sibdever.nsu_bank_system_server.controller.crud;

import com.sibdever.nsu_bank_system_server.data.model.entities.Credit;
import com.sibdever.nsu_bank_system_server.service.CrudCreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crud/credit")
public class CrudCreditsController {
    @Autowired
    private CrudCreditService service;

    @GetMapping("/find-all")
    public Page<Credit> findAll(Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/find")
    public Credit find(int id) {
        return service.findById(id);
    }

    @PostMapping("/delete")
    public void delete(int id) {
        service.delete(id);
    }

    @PostMapping("/edit")
    public void edit(@RequestBody Credit credit) {
        service.edit(credit);
    }

}
