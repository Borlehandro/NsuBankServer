package com.sibdever.nsu_bank_system_server.data.repo;

import com.sibdever.nsu_bank_system_server.data.model.CreditTableId;
import com.sibdever.nsu_bank_system_server.data.model.CreditsTable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditTableRepo extends CrudRepository<CreditsTable, Integer> {
    @Query("select ct from CreditsTable ct where ct.credit.id = ?1")
    List<CreditsTable> findAllByCreditId(int id);
}
