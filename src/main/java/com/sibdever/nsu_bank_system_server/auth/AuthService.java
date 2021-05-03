package com.sibdever.nsu_bank_system_server.auth;

import com.sibdever.nsu_bank_system_server.operator.OperatorCredentials;
import com.sibdever.nsu_bank_system_server.operator.OperatorsRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final OperatorsRepository userRepository;

    public AuthService(OperatorsRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void login(OperatorCredentials credentials) {

    }
}