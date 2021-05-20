package com.sibdever.nsu_bank_system_server.data.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class DayStatistic {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    @NonNull
    private LocalDate date;

    @Column(nullable = false)
    @NonNull
    private Double cashInflow;

    @Column(nullable = false)
    @NonNull
    private Double profitMargin;

    @OneToOne
    @NonNull
    private Credit credit;
}
