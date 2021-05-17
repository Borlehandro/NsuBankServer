package com.sibdever.nsu_bank_system_server.data.repo;

import com.sibdever.nsu_bank_system_server.data.model.CreditTableId;
import com.sibdever.nsu_bank_system_server.data.model.CreditsTable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CreditTableRepo extends CrudRepository<CreditsTable, CreditTableId> {
    @Query("select ct from CreditsTable ct where ct.id.credit.id = ?1")
    List<CreditsTable> findAllByCreditId(int id);

    @Query("""
                select table from CreditsTable table
                    where table.id.credit.id = :credit_id
                    and table.id.timestamp >= :after_time
            """)
    List<CreditsTable> findAllByCreditIdWhereDateAfterOrEquals(
            @Param("credit_id") int creditId,
            @Param("after_time") LocalDateTime dateTime
    );

    @Query("""
    select credit_table.id.credit, credit_table from CreditsTable credit_table
        where credit_table.id.credit.client.id = :client_id
    """)
    List<Object[]> findAllByClientId(@Param("client_id") int clientId);

    @Query("""
    select credit_table from CreditsTable credit_table
        join Client client
            on (credit_table.id.credit.id = client.activeCredit.id and client.id = :client_id)
    """)
    List<CreditsTable> findAllByClientIdIsActive(@Param("client_id") int clientId);
}
