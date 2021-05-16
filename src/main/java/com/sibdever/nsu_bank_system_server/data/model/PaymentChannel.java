package com.sibdever.nsu_bank_system_server.data.model;

public enum PaymentChannel {
    BANK_ACCOUNT(0), CREDIT_CARD(1), YOO_MONEY(3), QIWI(3);

    PaymentChannel(int feePercents) {
        this.feePercents = feePercents;
    }

    private final int feePercents;

    public int getFeePercents() {
        return feePercents;
    }
}
