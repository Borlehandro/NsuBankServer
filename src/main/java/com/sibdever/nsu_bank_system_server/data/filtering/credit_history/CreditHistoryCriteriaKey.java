package com.sibdever.nsu_bank_system_server.data.filtering.credit_history;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public enum CreditHistoryCriteriaKey {

    CREDIT_SUM(List.of("credit", "sum")),
    TIMESTAMP(List.of("timestamp")),
    CREDIT_MONTH(List.of("credit", "monthPeriod")),
    CREDIT_START_DATE(List.of("credit", "startDate")),
    CREDIT_PERCENTS(List.of("credit", "offer", "percentsPerMonth")),
    CREDIT_STATUS_NOW(List.of("credit", "status")),
    CREDIT_STATUS_IN_RECORD(List.of("creditStatus")),
    CLIENT_STATUS(List.of("credit", "client", "clientStatus")),
    CREDIT_PROFIT_MARGIN(List.of("credit", "profitMargin"));

    private @NonNull
    final List<String> fieldNames;
}
