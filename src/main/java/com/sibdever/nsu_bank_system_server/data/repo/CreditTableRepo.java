package com.sibdever.nsu_bank_system_server.data.repo;

import com.sibdever.nsu_bank_system_server.data.model.entities.Credit;
import com.sibdever.nsu_bank_system_server.data.model.entities.CreditTableId;
import com.sibdever.nsu_bank_system_server.data.model.entities.CreditsTable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CreditTableRepo extends CrudRepository<CreditsTable, CreditTableId>,
        JpaSpecificationExecutor<CreditsTable> {
    List<CreditsTable> findAllById_Credit_Id(int id);

    @Query("""
            select table from CreditsTable table
                     where table.id.credit.id = :credit_id
                     and table.id.timestamp >= :after_time
             """)
    List<CreditsTable> findAllByCreditIdAndDateAfter(
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

    @Query("""
    select credit_table from CreditsTable credit_table
        where credit_table.id.timestamp between :start_time and :end_time
    """)
    List<CreditsTable> findAllWhereTimestampBetween(
            @Param("start_time") LocalDateTime startTime,
            @Param("end_time") LocalDateTime endTime);
}
