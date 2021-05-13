package com.sibdever.nsu_bank_system_server.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {
    private final List<? extends GrantedAuthority> authorities;
    private final String role;
    private final String username;
    private final String password;
    private final boolean accountNonExpired;
    private final boolean credentialsNonExpired;
    private final boolean accountNonLocked;

    @Override
    public boolean isEnabled() {
        return accountNonLocked;
    }
}
