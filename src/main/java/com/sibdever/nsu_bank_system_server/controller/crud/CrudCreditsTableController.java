package com.sibdever.nsu_bank_system_server.controller.crud;

import com.sibdever.nsu_bank_system_server.data.model.entities.CreditsTable;
import com.sibdever.nsu_bank_system_server.service.CrudCreditTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/crud/credits-table")
public class CrudCreditsTableController {

    @Autowired
    private CrudCreditTableService service;

    @GetMapping("/find")
    public List<CreditsTable> find(@RequestParam int creditId) {
        return service.findByCreditId(creditId);
    }

    @GetMapping("/find-all")
    public Iterable<CreditsTable> find() {
        return service.findAll();
    }
}
