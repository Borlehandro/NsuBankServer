package com.sibdever.nsu_bank_system_server.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReportRecord {

    public ReportRecord(String period, Double totalRelease, Double totalRefund, Double expiredCreditsPercent, Double totalProfit) {
        this.period = period;
        this.totalRelease = totalRelease;
        this.totalRefund = totalRefund;
        this.expiredCreditsPercent = expiredCreditsPercent;
        this.totalProfit = totalProfit;
    }

    private String period;
    private Double totalRelease;
    private Double totalRefund;
    private Double expiredCreditsPercent;
    private Double totalProfit;
}
