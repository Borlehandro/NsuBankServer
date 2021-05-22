package com.sibdever.nsu_bank_system_server.data.repo;

import com.sibdever.nsu_bank_system_server.data.model.entities.Credit;
import com.sibdever.nsu_bank_system_server.data.model.entities.CreditTableId;
import com.sibdever.nsu_bank_system_server.data.model.entities.CreditsTable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CreditTableRepo extends CrudRepository<CreditsTable, CreditTableId>,
        JpaSpecificationExecutor<CreditsTable> {
    List<CreditsTable> findAllById_Credit_IdOrderById_Timestamp(int id);

    List<CreditsTable> findAllById_CreditAndId_TimestampAfterOrderById_Timestamp(
            @Param("credit_id") Credit credit,
            @Param("after_time") LocalDateTime dateTime
    );

    @Query("""
            select credit_table.id.credit, credit_table from CreditsTable credit_table
                where credit_table.id.credit.client.id = :client_id
            """)
    List<Object[]> findAllGroupingByClientId(@Param("client_id") int clientId);

    @Query("""
            select credit_table from CreditsTable credit_table
                join Client client
                    on (credit_table.id.credit.id = client.activeCredit.id and client.id = :client_id)
            """)
    List<CreditsTable> findAllByClientIdIsActive(@Param("client_id") int clientId);

    @Query(value = """
    select * from credits_table
        where (credits_table.real_payout < credits_table.expected_payout)
        and ((credits_table.timestamp)::::date = (:date_now)::::date)
    """, nativeQuery = true)
    List<CreditsTable> findAllInThisMonthWhereRealPayoutLessThanExpected(
            @Param("date_now") LocalDateTime currentDate);
}
