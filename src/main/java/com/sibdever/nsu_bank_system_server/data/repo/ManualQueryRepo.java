package com.sibdever.nsu_bank_system_server.data.repo;

import javax.persistence.Tuple;
import java.util.List;

public interface ManualQueryRepo {
    List<Tuple> executeQuery(String queryText);
}
