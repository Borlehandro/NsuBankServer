package com.sibdever.nsu_bank_system_server.data.repo;

import com.sibdever.nsu_bank_system_server.data.model.CreditHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CreditHistoryRepo extends JpaRepository<CreditHistory, Integer> {
    @Query("""
        select history from CreditHistory history
            where history.credit.id = :credit_id
            order by history.timestamp
        """)
    List<CreditHistory> findAllByCreditId(@Param("credit_id") int creditId);

    @Query("""
    select history from CreditHistory history
        where history.client.id = :client_id
        order by history.timestamp
    """)
    List<CreditHistory> findAllForClient(@Param("client_id") int creditId);
}
