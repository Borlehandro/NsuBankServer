package com.sibdever.nsu_bank_system_server.client;

import com.sibdever.nsu_bank_system_server.offer.Offer;

import javax.persistence.*;

@Entity
@Table(name = "clients")
public class Client {

    public Client(String fullName, boolean blocked) {
        this.fullName = fullName;
        this.blocked = blocked;
    }

    public Client() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private boolean blocked = false;

    @OneToOne
    private Offer offer;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}