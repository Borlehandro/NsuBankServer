package com.sibdever.nsu_bank_system_server.data.filtering.credit_table;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public enum CreditTableCriteriaKey {

    CREDIT_SUM(List.of("id", "credit", "sum")),
    TABLE_DATE(List.of("id", "timestamp")),
    CREDIT_MONTH(List.of("id", "credit", "monthPeriod")),
    CREDIT_START_DATE(List.of("id", "credit", "startDate")),
    CREDIT_PERCENTS(List.of("id", "credit", "offer", "percentsPerMonth")),
    CREDIT_STATUS(List.of("id", "credit", "status")),
    CLIENT_STATUS(List.of("id", "credit", "client", "clientStatus")),
    CREDIT_PROFIT_MARGIN(List.of("id", "credit", "profitMargin"));

    private @NonNull
    final List<String> fieldNames;
}
