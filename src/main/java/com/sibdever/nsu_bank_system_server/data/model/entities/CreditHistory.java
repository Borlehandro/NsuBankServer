package com.sibdever.nsu_bank_system_server.data.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "credits_history")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CreditHistory {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    @NonNull
    private Client client;

    @OneToOne
    @NonNull
    private Credit credit;

    @NonNull
    private CreditStatus creditStatus;

    @NonNull
    private LocalDateTime timestamp;

}