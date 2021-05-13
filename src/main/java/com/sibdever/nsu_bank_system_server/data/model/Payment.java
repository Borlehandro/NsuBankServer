package com.sibdever.nsu_bank_system_server.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    @NonNull
    private Client client;

    @Column(nullable = false)
    @NonNull
    private ZonedDateTime timestamp;

    @OneToOne
    @NonNull
    private Credit credit;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @NonNull
    private PaymentType type;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @NonNull
    private PaymentChannel channel;

    @Column(nullable = false)
    @NonNull
    private Double paymentSum;
}