package com.sibdever.nsu_bank_system_server.data.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

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

    @OneToOne
    @NonNull
    private Credit credit;

    @NonNull
    @Embedded
    private PaymentDetails paymentDetails;

    @Column(nullable = false)
    private boolean handled = false;

}