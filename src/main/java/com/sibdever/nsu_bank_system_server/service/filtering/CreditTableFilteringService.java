package com.sibdever.nsu_bank_system_server.service.filtering;

import com.sibdever.nsu_bank_system_server.data.filtering.credit_table.CreditsTableSpecification;
import com.sibdever.nsu_bank_system_server.data.model.entities.CreditsTable;
import com.sibdever.nsu_bank_system_server.data.repo.CreditTableRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CreditTableFilteringService {

    @Autowired
    private CreditTableRepo creditTableRepo;

    public Page<CreditsTable> getCreditTableListBySpecification(CreditsTableSpecification specification, Pageable pageable) {
        return creditTableRepo.findAll(specification, pageable);
    }

}
