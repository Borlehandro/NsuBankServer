package com.sibdever.nsu_bank_system_server.operator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperatorsRepository extends JpaRepository<Operator, Integer> {

    Operator findOperatorByUsername(String username);

}