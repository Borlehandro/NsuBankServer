package com.sibdever.nsu_bank_system_server.operator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    // TODO Return JWT (Use security)
    public void register(@RequestBody OperatorCredentials operatorCredentials) throws WrongCredentialsException {
        authService.login(operatorCredentials);
    }

    @GetMapping("/test")
    public String test() {
        return "OK";
    }

}