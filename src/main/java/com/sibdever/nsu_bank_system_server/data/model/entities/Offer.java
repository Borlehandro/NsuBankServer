package com.sibdever.nsu_bank_system_server.data.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "offers")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Offer implements Serializable {

    public Offer(String name,
                 double percentsPerMonth,
                 int minimumMonthPeriod,
                 int maximumMonthPeriod,
                 int minimumSum,
                 int maximumSum) {
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
    @NonNull
    private Double percentsPerMonth;

    private Integer minimumMonthPeriod;

    private Integer maximumMonthPeriod;

    private Integer minimumSum;

    private Integer maximumSum;
}
