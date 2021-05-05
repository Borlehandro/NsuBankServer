package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.CreditsTable;
import com.sibdever.nsu_bank_system_server.data.repo.CreditTableRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrudCreditTableService {
    @Autowired
    private CreditTableRepo repo;

    public Iterable<CreditsTable> findAll() {
        return repo.findAll();
    }

    public List<CreditsTable> findByCreditId(int id) {
        return repo.findAllByCreditId(id);
    }

}
