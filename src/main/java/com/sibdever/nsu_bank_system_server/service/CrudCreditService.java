package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.Credit;
import com.sibdever.nsu_bank_system_server.data.repo.CreditsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CrudCreditService {
    @Autowired
    CreditsRepo creditsRepo;

    public Iterable<Credit> findAll() {
        return creditsRepo.findAll();
    }

    public Credit findById(int id) {
        return creditsRepo.findById(id).get();
    }
}