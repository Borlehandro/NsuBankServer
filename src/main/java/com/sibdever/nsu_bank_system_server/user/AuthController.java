package com.sibdever.nsu_bank_system_server.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    // TODO Return JWT (Use security)
    public void register(HttpServletResponse response, HttpServletRequest request, @RequestBody UserCredentials userCredentials) throws WrongCredentialsException {
        authService.register(userCredentials);
    }

    @GetMapping("/test")
    public String test() {
        return "OK";
    }

}