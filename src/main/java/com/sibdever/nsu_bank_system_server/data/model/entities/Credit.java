package com.sibdever.nsu_bank_system_server.data.model.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "credits")
@Getter
@Setter
@ToString(exclude = "client")
@EqualsAndHashCode(exclude = {"client", "startDate"})
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Credit {

    public Credit(@NonNull Client client,
                  @NonNull LocalDateTime startDate,
                  int monthPeriod,
                  double sum,
                  double balance,
                  @NonNull Offer offer) {
        this.client = client;
        this.startDate = startDate;
        this.monthPeriod = monthPeriod;
        this.sum = sum;
        this.balance = balance;
        this.offer = offer;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    @NonNull
    private Client client;

    @OneToOne
    @NonNull
    private Offer offer;

    @Column(nullable = false)
    @NonNull
    private LocalDateTime startDate;

    private int monthPeriod;

    private double sum;

    private double balance;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @NonNull
    private CreditStatus status = CreditStatus.ACTIVE;
}