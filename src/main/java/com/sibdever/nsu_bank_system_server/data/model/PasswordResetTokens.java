package com.sibdever.nsu_bank_system_server.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class PasswordResetTokens {

    @Id
    private int operatorId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "operator_id")
    @NonNull
    private Operator operator;

    @Column(nullable = false)
    @NonNull
    private String resetToken;

    @Column(nullable = false)
    @NonNull
    private Integer timeToLiveSeconds;

    @Column(nullable = false)
    @NonNull
    private LocalDateTime generationTime;

}
