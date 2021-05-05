package com.sibdever.nsu_bank_system_server.data.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@IdClass(CreditTableId.class)
public class CreditsTable {

    public CreditsTable() {
    }

    public CreditsTable(Credit credit, Payment payment, double expectedPayout, double paymentOfPercents, double paymentOfDebt, double balanceAfterPayment, LocalDateTime timestamp) {
        this.credit = credit;
        this.payment = payment;
        this.expectedPayout = expectedPayout;
        this.paymentOfPercents = paymentOfPercents;
        this.paymentOfDebt = paymentOfDebt;
        this.balanceAfterPayment = balanceAfterPayment;
        this.timestamp = timestamp;
    }

    public CreditsTable(Credit credit, Payment payment, double expectedPayout, double paymentOfPercents, double paymentOfDebt, double balanceAfterPayment, CreditStatus creditStatusAfterPayment, LocalDateTime timestamp) {
        this.credit = credit;
        this.payment = payment;
        this.expectedPayout = expectedPayout;
        this.paymentOfPercents = paymentOfPercents;
        this.paymentOfDebt = paymentOfDebt;
        this.balanceAfterPayment = balanceAfterPayment;
        this.creditStatusAfterPayment = creditStatusAfterPayment;
        this.timestamp = timestamp;
    }

    @Id
    @OneToOne
    private Credit credit;

    @OneToOne
    private Payment payment;

    private double expectedPayout;

    private double paymentOfPercents;

    private double paymentOfDebt;

    private double balanceAfterPayment;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private CreditStatus creditStatusAfterPayment = CreditStatus.ACTIVE;

    @Id
    private LocalDateTime timestamp;

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
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
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}