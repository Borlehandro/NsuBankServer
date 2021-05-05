package com.sibdever.nsu_bank_system_server.data.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ClientLocking {

    public ClientLocking() {
    }

    public ClientLocking(Client client, LocalDateTime lockingBegin, LocalDateTime lockingEnd) {
        this.client = client;
        this.lockingBegin = lockingBegin;
        this.lockingEnd = lockingEnd;
    }

    @Id
    @Column(name = "client_id")
    private int id;

    @OneToOne
    @JoinColumn(name = "client_id")
    @MapsId
    private Client client;

    @Column(nullable = false)
    private LocalDateTime lockingBegin;

    @Column(nullable = false)
    private LocalDateTime lockingEnd;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDateTime getLockingBegin() {
        return lockingBegin;
    }

    public void setLockingBegin(LocalDateTime lockingBegin) {
        this.lockingBegin = lockingBegin;
    }

    public LocalDateTime getLockingEnd() {
        return lockingEnd;
    }

    public void setLockingEnd(LocalDateTime lockingEnd) {
        this.lockingEnd = lockingEnd;
    }
}
