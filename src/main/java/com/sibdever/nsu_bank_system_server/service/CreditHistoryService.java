package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.entities.Credit;
import com.sibdever.nsu_bank_system_server.data.model.entities.CreditHistory;
import com.sibdever.nsu_bank_system_server.data.model.entities.CreditsTable;
import com.sibdever.nsu_bank_system_server.data.repo.CreditHistoryRepo;
import com.sibdever.nsu_bank_system_server.data.repo.CreditTableRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CreditHistoryService {

    @Autowired
    private CreditHistoryRepo creditHistoryRepo;
    @Autowired
    private CreditTableRepo creditTableRepo;

    // History
    public List<CreditHistory> getCreditHistory(int creditId) {
        return creditHistoryRepo.findAllByCredit_IdOrderByTimestamp(creditId);
    }

    public Map<Credit, List<CreditHistory>> getAllCreditsHistoryForClient(int clientId) {
        return creditHistoryRepo.findAllByClient_IdOrderByTimestamp(clientId)
                .stream()
                .collect(Collectors.groupingBy(CreditHistory::getCredit));
    }

    // Credit table
    public List<CreditsTable> getCreditTableForCredit(int creditId) {
        return creditTableRepo.findAllById_Credit_IdOrderById_Timestamp(creditId);
    }

    public Map<Credit, List<CreditsTable>> getAllCreditTablesForClient(int clientId) {
        return creditTableRepo.findAllGroupingByClientId(clientId)
                .stream()
                .collect(Collectors.groupingBy(
                        (Object[] item) -> (Credit) item[0],
                        Collectors.mapping((Object[] item) -> (CreditsTable)item[1], Collectors.toList())
                ));
    }

    public List<CreditsTable> getActiveCreditTable(int clientId) {
        return creditTableRepo.findAllByClientIdIsActive(clientId);
    }
}