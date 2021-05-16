package com.sibdever.nsu_bank_system_server.data.repo;

import com.sibdever.nsu_bank_system_server.data.model.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CreditsRepo extends JpaRepository<Credit, Integer> {

    @Query(value = """
            select credit, payment from Payment payment
                join payment.credit credit
                where payment.paymentDetails.timestamp > :date_time
                    and payment.paymentDetails.timestamp > credit.startDate
            """)
    List<Object[]> findAllJoinPaymentsAfterTime(@Param("date_time") LocalDateTime dateTime);
}
