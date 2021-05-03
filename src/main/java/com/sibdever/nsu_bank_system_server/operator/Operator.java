package com.sibdever.nsu_bank_system_server.operator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "operators")
public class Operator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String fullName;

    @Column(name = "password_hash", nullable = false)
    // TODO Use password hashing from security
    private String password;

    @Enumerated(EnumType.STRING)
    private OperatorRole role;

    @ElementCollection(targetClass = OperatorAuthority.class, fetch = FetchType.EAGER)
    @JoinTable(name = "users_authorities", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "authority", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<OperatorAuthority> userAuthorities;

    // For password resetting
    @Column(nullable = false)
    private String secretPhrase;

    @Column(nullable = false)
    private boolean accountNonExpired = true;

    @Column(nullable = false)
    private boolean credentialsNonExpired = true;

    @Column(nullable = false)
    private boolean accountNonLocked = true;

    public Operator(){}

    public Operator(String username, String fullName, String password, String secretPhrase) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.secretPhrase = secretPhrase;
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

    public OperatorRole getRole() {
        return role;
    }

    public List<OperatorAuthority> getUserAuthorities() {
        return userAuthorities;
    }

    public String getSecretPhrase() {
        return secretPhrase;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }
}