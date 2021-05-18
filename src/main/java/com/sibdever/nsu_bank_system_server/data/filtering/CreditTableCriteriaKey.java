package com.sibdever.nsu_bank_system_server.data.filtering;

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
    CREDIT_PERCENTS(List.of("id", "credit", "offer", "percentsPerMonth"));

    private @NonNull
    final List<String> fieldNames;
}
