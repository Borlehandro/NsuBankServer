package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.entities.Operator;
import com.sibdever.nsu_bank_system_server.data.repo.OperatorsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CrudOperatorService {
    @Autowired
    OperatorsRepo operatorsRepo;

    public Page<Operator> findAll(Pageable pageable) {
        return operatorsRepo.findAll(pageable);
    }

    public Operator findByUsername(String username) {
        return operatorsRepo.findOperatorByUsername(username);
    }

    public void deleteByUsername(String username) {
        operatorsRepo.delete(operatorsRepo.findOperatorByUsername(username));
    }

}
