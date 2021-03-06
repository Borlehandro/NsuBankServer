package com.sibdever.nsu_bank_system_server.controller.crud;

import com.sibdever.nsu_bank_system_server.data.model.entities.Operator;
import com.sibdever.nsu_bank_system_server.service.CrudOperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/crud/operator")
public class CrudOperatorsController {
    @Autowired
    private CrudOperatorService crudOperatorService;

    @GetMapping("/find-all")
    public Page<Operator> findAll(Pageable pageable) {
        return crudOperatorService.findAll(pageable);
    }

    @GetMapping("/find")
    public Operator findByUsername(@RequestParam String username) {
        return crudOperatorService.findByUsername(username);
    }

    @PostMapping("/delete")
    public void delete(@RequestParam String username) {
        crudOperatorService.deleteByUsername(username);
    }
}