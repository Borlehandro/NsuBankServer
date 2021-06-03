package com.sibdever.nsu_bank_system_server.service.filtering;

import com.sibdever.nsu_bank_system_server.data.filtering.credit_history.CreditsHistorySpecification;
import com.sibdever.nsu_bank_system_server.data.model.entities.CreditHistory;
import com.sibdever.nsu_bank_system_server.data.repo.CreditHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CreditsHistoryFilteringService {

    @Autowired
    private CreditHistoryRepo creditHistoryRepo;

    public Page<CreditHistory> getCreditsHistoryBySpecification(CreditsHistorySpecification specification, Pageable pageable) {
        return creditHistoryRepo.findAll(specification, pageable);
    }
}
