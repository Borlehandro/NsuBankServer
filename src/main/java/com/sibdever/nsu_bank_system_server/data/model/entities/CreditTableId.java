package com.sibdever.nsu_bank_system_server.data.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CreditTableId implements Serializable {

    @OneToOne
    @NonNull
    private Credit credit;

    @Column(nullable = false)
    @NonNull
    private LocalDateTime timestamp;

}