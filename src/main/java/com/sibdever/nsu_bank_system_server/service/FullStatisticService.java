package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.ReportRecord;
import com.sibdever.nsu_bank_system_server.data.repo.FullReportRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FullStatisticService {
    @Autowired
    private FullReportRepoImpl fullReportRepo;

    public List<ReportRecord> getFullStatistic() {
        return fullReportRepo.getFullReportTest();
    }

}
