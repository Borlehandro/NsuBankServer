package com.sibdever.nsu_bank_system_server.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class OffersHistoryId implements Serializable {

    @OneToOne
    @NonNull
    private Client client;

    @OneToOne
    @NonNull
    private Offer offer;

    @Column(nullable = false)
    @NonNull
    private LocalDateTime timestamp;

}
