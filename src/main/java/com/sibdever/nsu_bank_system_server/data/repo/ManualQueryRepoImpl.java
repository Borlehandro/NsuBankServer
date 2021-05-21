package com.sibdever.nsu_bank_system_server.data.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import java.util.List;

@Repository
public class ManualQueryRepoImpl implements ManualQueryRepo {

    @Autowired
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<Tuple> executeQuery(String queryText) {
        return (List<Tuple>) entityManager.createNativeQuery(queryText, Tuple.class).getResultList();
    }
}
