package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.entities.Credit;
import com.sibdever.nsu_bank_system_server.data.repo.CreditsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CrudCreditService {
    @Autowired
    CreditsRepo creditsRepo;

    public Page<Credit> findAll(Pageable pageable) {
        return creditsRepo.findAll(pageable);
    }

    public Credit findById(int id) {
        return creditsRepo.findById(id).get();
    }

    public void delete(int id) {
        creditsRepo.findById(id).ifPresent((value) -> creditsRepo.delete(value));
    }

    public void edit(Credit credit) {
        creditsRepo.save(credit);
    }
}