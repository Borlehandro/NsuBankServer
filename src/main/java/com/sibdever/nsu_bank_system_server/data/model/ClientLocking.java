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
public class ClientLocking {

    @Id
    @Column(name = "client_id")
    private int id;

    @OneToOne
    @JoinColumn(name = "client_id")
    @MapsId
    @NonNull
    private Client client;

    @Column(nullable = false)
    @NonNull
    private LocalDateTime lockingBegin;

    @Column(nullable = false)
    @NonNull
    private LocalDateTime lockingEnd;
}
