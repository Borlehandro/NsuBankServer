package com.sibdever.nsu_bank_system_server.data.model;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "payments")
public class Payment {

    public Payment() {
    }

    public Payment(Client client, ZonedDateTime timestamp, Credit credit, PaymentType type, PaymentChannel channel, double paymentSum) {
        this.client = client;
        this.timestamp = timestamp;
        this.credit = credit;
        this.type = type;
        this.channel = channel;
        this.paymentSum = paymentSum;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    private Client client;

    @Column(nullable = false)
    private ZonedDateTime timestamp;

    @OneToOne
    private Credit credit;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private PaymentType type;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private PaymentChannel channel;

    private double paymentSum;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public double getPaymentSum() {
        return paymentSum;
    }

    public void setPaymentSum(double paymentSum) {
        this.paymentSum = paymentSum;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public PaymentChannel getChannel() {
        return channel;
    }

    public void setChannel(PaymentChannel channel) {
        this.channel = channel;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;
        Payment payment = (Payment) o;
        return id == payment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}