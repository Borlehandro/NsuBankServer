package com.sibdever.nsu_bank_system_server.user;

import javax.persistence.*;

@Entity
@Table(name = "users_list")
public class User {
    @Id
    @GeneratedValue
    int id;

    @Column(unique = true)
    private String username;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "password_hash")
    // TODO Use password hashing from security
    private String password;

    public User(){}

    public User(String username, String fullName, String password) {
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