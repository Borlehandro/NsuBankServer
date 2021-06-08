package com.sibdever.nsu_bank_system_server.data.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PaymentDetails {
    @Column(nullable = false)
    @NonNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
