package com.sibdever.nsu_bank_system_server.data.repo;

import javax.persistence.Tuple;
import java.util.List;

public interface FullReportRepo {
    List<Tuple> getFullReportTest(int clientId);
}
