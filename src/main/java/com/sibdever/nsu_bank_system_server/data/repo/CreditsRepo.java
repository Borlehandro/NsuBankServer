package com.sibdever.nsu_bank_system_server.data.repo;

import com.sibdever.nsu_bank_system_server.data.model.Credit;
import org.springframework.data.repository.CrudRepository;

public interface CreditsRepo extends CrudRepository<Credit, Integer> {

}
