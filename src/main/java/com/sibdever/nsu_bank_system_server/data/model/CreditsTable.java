package com.sibdever.nsu_bank_system_server.data.model;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CreditsTable {

    @EmbeddedId
    private CreditTableId id;

    public CreditsTable() {
    }

    public CreditsTable(CreditTableId creditTableId, double expectedPayout, double paymentOfPercents, double paymentOfDebt, double balanceAfterPayment, CreditStatus creditStatusAfterPayment) {
        this.expectedPayout = expectedPayout;
        this.paymentOfPercents = paymentOfPercents;
        this.paymentOfDebt = paymentOfDebt;
        this.balanceAfterPayment = balanceAfterPayment;
        this.creditStatusAfterPayment = creditStatusAfterPayment;
        this.id = creditTableId;
    }

    public CreditsTable(CreditTableId creditTableId, Payment payment, double expectedPayout, double paymentOfPercents, double paymentOfDebt, double balanceAfterPayment) {
        this.payment = payment;
        this.expectedPayout = expectedPayout;
        this.paymentOfPercents = paymentOfPercents;
        this.paymentOfDebt = paymentOfDebt;
        this.balanceAfterPayment = balanceAfterPayment;
        this.id = creditTableId;
    }

    public CreditsTable(CreditTableId creditTableId, Payment payment, double expectedPayout, double paymentOfPercents, double paymentOfDebt, double balanceAfterPayment, CreditStatus creditStatusAfterPayment) {
        this.id = creditTableId;
        this.payment = payment;
        this.expectedPayout = expectedPayout;
        this.paymentOfPercents = paymentOfPercents;
        this.paymentOfDebt = paymentOfDebt;
        this.balanceAfterPayment = balanceAfterPayment;
        this.creditStatusAfterPayment = creditStatusAfterPayment;
    }

    @OneToOne
    private Payment payment;

    private double expectedPayout;

    private double paymentOfPercents;

    private double paymentOfDebt;

    private double balanceAfterPayment;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private CreditStatus creditStatusAfterPayment = CreditStatus.ACTIVE;

    public Credit getCredit() {
        return id.getCredit();
    }

    public CreditTableId getId() {
        return id;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public double getExpectedPayout() {
        return expectedPayout;
    }

    public void setExpectedPayout(double expectedPayout) {
        this.expectedPayout = expectedPayout;
    }

    public double getPaymentOfPercents() {
        return paymentOfPercents;
    }

    public void setPaymentOfPercents(double paymentOfPercents) {
        this.paymentOfPercents = paymentOfPercents;
    }

    public double getPaymentOfDebt() {
        return paymentOfDebt;
    }

    public void setPaymentOfDebt(double paymentOfDebt) {
        this.paymentOfDebt = paymentOfDebt;
    }

    public double getBalanceAfterPayment() {
        return balanceAfterPayment;
    }

    public void setBalanceAfterPayment(double balanceAfterPayment) {
        this.balanceAfterPayment = balanceAfterPayment;
    }

    public CreditStatus getCreditStatusAfterPayment() {
        return creditStatusAfterPayment;
    }

    public void setCreditStatusAfterPayment(CreditStatus creditStatusAfterPayment) {
        this.creditStatusAfterPayment = creditStatusAfterPayment;
    }

    public LocalDateTime getTimestamp() {
        return id.getTimestamp();
    }
}