package com.sibdever.nsu_bank_system_server.user;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsersRepository userRepository;

    public AuthService(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(UserCredentials credentials) throws WrongCredentialsException {
        // TODO Catch particular exception
        try {
            userRepository.save(new User(credentials.getUsername(), credentials.getFullName(), credentials.getPassword()));
        } catch (Exception e) {
            throw new WrongCredentialsException("Wrong register credentials.", e);
        }
    }
}