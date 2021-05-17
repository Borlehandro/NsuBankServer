package com.sibdever.nsu_bank_system_server.data.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "clients")
@Getter
@Setter
@ToString(exclude = "activeCredit")
@EqualsAndHashCode(exclude = "activeCredit")
@NoArgsConstructor
@RequiredArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @NonNull
    private String fullName;

    @OneToOne
    private Offer offer;

    @OneToOne
    private Credit activeCredit;
}