package com.sibdever.nsu_bank_system_server.data.repo;

import com.sibdever.nsu_bank_system_server.data.model.ReportRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.EntityManager;
import javax.persistence.SqlResultSetMapping;
import java.util.List;

@Repository
public class FullReportRepoImpl implements FullReportRepo {

    @Autowired
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<ReportRecord> getFullReportTest() {
        var query = entityManager.createNamedQuery("FullStatisticReportQuery");
        var test = query.getResultList();
        System.out.println(test);
        return (List<ReportRecord>) query.getResultList();
    }
}
