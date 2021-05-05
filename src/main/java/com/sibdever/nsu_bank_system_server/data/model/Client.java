package com.sibdever.nsu_bank_system_server.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sibdever.nsu_bank_system_server.data.model.Offer;

import javax.persistence.*;

@Entity
@Table(name = "clients")
public class Client {

    public Client(@JsonProperty String fullName) {
        this.fullName = fullName;
    }

    public Client() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public int getId() {
        return id;
    }
}