package com.sibdever.nsu_bank_system_server.data.filtering.payments;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public enum PaymentCriteriaKey {

    CREDIT_SUM(List.of("credit", "sum")),
    TIMESTAMP(List.of("paymentDetails", "timestamp")),
    PAYMENT_TYPE(List.of("paymentDetails", "type")),
    PAYMENT_CHANNEL(List.of("paymentDetails", "channel")),
    PAYMENT_SUM(List.of("paymentDetails", "paymentSum")),
    CREDIT_MONTH(List.of("credit", "monthPeriod")),
    CREDIT_START_DATE(List.of("credit", "startDate")),
    CREDIT_PERCENTS(List.of("credit", "offer", "percentsPerMonth")),
    CREDIT_STATUS(List.of("credit", "status")),
    CLIENT_STATUS(List.of("client", "clientStatus")),
    CREDIT_PROFIT_MARGIN(List.of("credit", "profitMargin"));

    private @NonNull
    final List<String> fieldNames;
}
