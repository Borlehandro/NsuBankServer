package com.sibdever.nsu_bank_system_server.data.repo;

import com.sibdever.nsu_bank_system_server.data.model.entities.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CreditsRepo extends JpaRepository<Credit, Integer> {

    @Query(value = """
            select credit, payment from Payment payment
                join payment.credit credit
                where payment.handled = false and payment.paymentDetails.type = 1
            """)
    List<Object[]> findAllWherePaymentsNotHandled();
}
