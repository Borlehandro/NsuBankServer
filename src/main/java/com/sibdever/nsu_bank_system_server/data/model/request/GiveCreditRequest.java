package com.sibdever.nsu_bank_system_server.data.model.request;

import com.sibdever.nsu_bank_system_server.data.model.PaymentChannel;

public class GiveCreditRequest {

    public GiveCreditRequest() {
    }

    private int clientId;
    private int offerId;
    private int monthPeriod;
    private double sum;
    private PaymentChannel paymentChannel;

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public int getMonthPeriod() {
        return monthPeriod;
    }

    public void setMonthPeriod(int monthPeriod) {
        this.monthPeriod = monthPeriod;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public PaymentChannel getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(PaymentChannel paymentChannel) {
        this.paymentChannel = paymentChannel;
    }
}
