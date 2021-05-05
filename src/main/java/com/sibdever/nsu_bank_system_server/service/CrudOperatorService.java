package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.Operator;
import com.sibdever.nsu_bank_system_server.data.repo.OperatorsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrudOperatorService {
    @Autowired
    OperatorsRepo operatorsRepo;

    public List<Operator> findAll() {
        return operatorsRepo.findAll();
    }

    public Operator findByUsername(String username) {
        return operatorsRepo.findOperatorByUsername(username);
    }

    public void deleteByUsername(String username) {
        operatorsRepo.delete(operatorsRepo.findOperatorByUsername(username));
    }

}
