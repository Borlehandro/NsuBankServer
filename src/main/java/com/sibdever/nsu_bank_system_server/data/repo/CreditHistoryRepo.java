package com.sibdever.nsu_bank_system_server.data.repo;

import com.sibdever.nsu_bank_system_server.data.model.CreditHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditHistoryRepo extends JpaRepository<CreditHistory, Integer> {
    List<CreditHistory> findAllByCredit_IdOrderByTimestamp(int creditId);
    List<CreditHistory> findAllByClient_IdOrderByTimestamp(int creditId);
}
