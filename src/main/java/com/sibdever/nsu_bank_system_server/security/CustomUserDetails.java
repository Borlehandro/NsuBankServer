package com.sibdever.nsu_bank_system_server.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final List<? extends GrantedAuthority> grantedAuthorities;
    private final String role;
    private final String username;
    private final String password;
    private final boolean accountNonExpired;
    private final boolean credentialsNonExpired;
    private final boolean accountNonLocked;

    public CustomUserDetails(List<? extends GrantedAuthority> grantedAuthorities,
                             String role,
                             String username,
                             String password,
                             boolean accountNonExpired,
                             boolean credentialsNonExpired,
                             boolean accountNonLocked) {

        this.grantedAuthorities = grantedAuthorities;
        this.role = role;
        this.username = username;
        this.password = password;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return accountNonLocked;
    }

    public String getRole() {
        return role;
    }
}
