package com.sibdever.nsu_bank_system_server.data.model;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "credits")
public class Credit {

    public Credit() {
    }

    public Credit(Client client, ZonedDateTime startDate, int mouthPeriod, double sum) {
        this.client = client;
        this.startDate = startDate;
        this.mouthPeriod = mouthPeriod;
        this.sum = sum;
        this.balance = sum;
    }

    public Credit(Client client, ZonedDateTime startDate, int mouthPeriod, double sum, double balance, Offer offer) {
        this.client = client;
        this.startDate = startDate;
        this.mouthPeriod = mouthPeriod;
        this.sum = sum;
        this.balance = balance;
        this.offer = offer;
    }

    public Credit(Client client, ZonedDateTime startDate, int mouthPeriod, double sum, double balance) {
        this.client = client;
        this.startDate = startDate;
        this.mouthPeriod = mouthPeriod;
        this.sum = sum;
        this.balance = balance;
        this.offer = client.getOffer();
    }

    public Credit(Client client, ZonedDateTime startDate, int mouthPeriod, double sum, double balance, CreditStatus status) {
        this.client = client;
        this.startDate = startDate;
        this.mouthPeriod = mouthPeriod;
        this.sum = sum;
        this.balance = balance;
        this.status = status;
        this.offer = client.getOffer();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    private Client client;

    @OneToOne
    private Offer offer;

    @Column(nullable = false)
    private ZonedDateTime startDate;

    private int mouthPeriod;

    private double sum;

    private double balance;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private CreditStatus status = CreditStatus.ACTIVE;

    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public int getMouthPeriod() {
        return mouthPeriod;
    }

    public void setMouthPeriod(int mouthPeriod) {
        this.mouthPeriod = mouthPeriod;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public CreditStatus getStatus() {
        return status;
    }

    public void setStatus(CreditStatus status) {
        this.status = status;
    }
}