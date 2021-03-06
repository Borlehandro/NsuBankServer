package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.entities.CreditsTable;
import com.sibdever.nsu_bank_system_server.data.repo.CreditTableRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CrudCreditTableService {
    @Autowired
    private CreditTableRepo repo;

    public Iterable<CreditsTable> findAll() {
        return repo.findAll();
    }

    public List<CreditsTable> findByCreditId(int id) {
        return repo.findAllById_Credit_IdOrderById_Timestamp(id);
    }

}
