package com.sibdever.nsu_bank_system_server.data.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import java.util.List;

@Repository
public class FullReportRepoImpl implements FullReportRepo {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Tuple> getFullReportTest(int clientId) {
        return null;
    }
}
