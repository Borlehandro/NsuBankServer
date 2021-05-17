package com.sibdever.nsu_bank_system_server.data.model.entities;

import org.springframework.security.core.GrantedAuthority;

public enum OperatorAuthority {
    READ_TEST,
    WRITE_TEST;

    public GrantedAuthority toGrantedAuthority() {
        return (GrantedAuthority) this::name;
    }
}