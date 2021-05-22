package com.sibdever.nsu_bank_system_server.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReportRecord {

    public ReportRecord(Integer day, Integer month, Integer year, Double totalRelease, Double totalRefund, Double expiredCreditsPercent, Double totalProfit, Double profitPlus) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.totalRelease = totalRelease;
        this.totalRefund = totalRefund;
        this.expiredCreditsPercent = expiredCreditsPercent;
        this.totalProfit = totalProfit;
        this.profitPlus = profitPlus;
    }

    private Integer day;
    private Integer month;
    private Integer year;
    private Double totalRelease;
    private Double totalRefund;
    private Double expiredCreditsPercent;
    private Double totalProfit;
    private Double profitPlus; // ?
}
