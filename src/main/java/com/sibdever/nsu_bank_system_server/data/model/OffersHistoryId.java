package com.sibdever.nsu_bank_system_server.data.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
public class OffersHistoryId implements Serializable {

    public OffersHistoryId() {
    }

    public OffersHistoryId(Client client, Offer offer, LocalDateTime timestamp) {
        this.client = client;
        this.offer = offer;
        this.timestamp = timestamp;
    }

    @OneToOne
    private Client client;

    @OneToOne
    private Offer offer;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
