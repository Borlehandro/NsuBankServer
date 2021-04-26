package com.sibdever.nsu_bank_system_server.operator;

import javax.persistence.*;

@Entity
@Table(name = "operators")
public class Operator {
    @Id
    @GeneratedValue
    int id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String fullName;

    @Column(name = "password_hash", nullable = false)
    // TODO Use password hashing from security
    private String password;

    public Operator(){}

    public Operator(String username, String fullName, String password) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}