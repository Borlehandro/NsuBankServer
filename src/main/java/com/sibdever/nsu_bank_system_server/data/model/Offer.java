package com.sibdever.nsu_bank_system_server.data.model;

import javax.persistence.*;

@Entity
@Table(name = "offers")
public class Offer {

    public Offer() {
    }

    public Offer(String name, double percentsPerMonth) {
        this.name = name;
        this.percentsPerMonth = percentsPerMonth;
    }

    public Offer(String name, double percentsPerMonth, int minimumMonthPeriod, int maximumMonthPeriod, int minimumSum, int maximumSum) {
        this.name = name;
        this.percentsPerMonth = percentsPerMonth;
        this.minimumMonthPeriod = minimumMonthPeriod;
        this.maximumMonthPeriod = maximumMonthPeriod;
        this.minimumSum = minimumSum;
        this.maximumSum = maximumSum;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private String name;

    @Column(nullable = false)
    private double percentsPerMonth;

    private int minimumMonthPeriod = -1;

    private int maximumMonthPeriod = -1;

    private int minimumSum;

    private int maximumSum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPercentsPerMonth() {
        return percentsPerMonth;
    }

    public void setPercentsPerMonth(double percentPerMonth) {
        this.percentsPerMonth = percentPerMonth;
    }

    public int getMinimumMonthPeriod() {
        return minimumMonthPeriod;
    }

    public void setMinimumMonthPeriod(int minimumPeriod) {
        this.minimumMonthPeriod = minimumPeriod;
    }

    public int getMaximumMonthPeriod() {
        return maximumMonthPeriod;
    }

    public void setMaximumMonthPeriod(int maximumPeriod) {
        this.maximumMonthPeriod = maximumPeriod;
    }

    public int getMinimumSum() {
        return minimumSum;
    }

    public void setMinimumSum(int minimumSum) {
        this.minimumSum = minimumSum;
    }

    public int getMaximumSum() {
        return maximumSum;
    }

    public void setMaximumSum(int maximumSum) {
        this.maximumSum = maximumSum;
    }
}
