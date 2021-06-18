package com.sibdever.nsu_bank_system_server.controller;

import com.sibdever.nsu_bank_system_server.data.model.ReportRecord;
import com.sibdever.nsu_bank_system_server.service.FullStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/full-statistic")
public class FullStatisticController {

    @Autowired
    private FullStatisticService fullStatisticService;

    @GetMapping
    public List<ReportRecord> getFullStatistic() {
        return fullStatisticService.getFullStatistic();
    }
}
