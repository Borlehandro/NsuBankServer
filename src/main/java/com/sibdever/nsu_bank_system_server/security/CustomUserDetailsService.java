package com.sibdever.nsu_bank_system_server.security;

import com.sibdever.nsu_bank_system_server.data.model.Operator;
import com.sibdever.nsu_bank_system_server.data.model.OperatorAuthority;
import com.sibdever.nsu_bank_system_server.data.repo.OperatorsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final OperatorsRepo repository;

    @Autowired
    public CustomUserDetailsService(OperatorsRepo repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Operator operator = repository.findOperatorByUsername(username);
        return new CustomUserDetails(
                operator.getUserAuthorities().stream().map(OperatorAuthority::toGrantedAuthority).collect(Collectors.toList()),
                operator.getRole() != null ? operator.getRole().name() : null,
                operator.getUsername(),
                operator.getPassword(),
                operator.isAccountNonExpired(),
                operator.isCredentialsNonExpired(),
                operator.isAccountNonLocked());
    }
}
