package com.sibdever.nsu_bank_system_server.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PaymentDetails {
    @Column(nullable = false)
    @NonNull
    private LocalDateTime timestamp;

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
