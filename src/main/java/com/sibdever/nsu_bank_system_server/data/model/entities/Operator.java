package com.sibdever.nsu_bank_system_server.data.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "operators")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Operator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    @NonNull
    private String username;

    @Column(nullable = false)
    @NonNull
    private String fullName;

    @Column(name = "password_hash", nullable = false)
    @NonNull
    private String password;

    @Enumerated(EnumType.STRING)
    private OperatorRole role;

    @ElementCollection(targetClass = OperatorAuthority.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "operator_authorities", joinColumns = @JoinColumn(name = "operator_id"))
    @Column(name = "authority", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<OperatorAuthority> userAuthorities;

    // For password resetting
    @Column(nullable = false)
    @NonNull
    private String secretPhrase;

    @Column(nullable = false)
    private boolean accountNonExpired = true;

    @Column(nullable = false)
    private boolean credentialsNonExpired = true;

    @Column(nullable = false)
    private boolean accountNonLocked = true;
}