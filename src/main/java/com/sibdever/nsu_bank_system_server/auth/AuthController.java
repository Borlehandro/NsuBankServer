package com.sibdever.nsu_bank_system_server.auth;

import com.sibdever.nsu_bank_system_server.operator.OperatorRegisterCredentials;
import com.sibdever.nsu_bank_system_server.operator.WrongCredentialsException;
import com.sibdever.nsu_bank_system_server.password_reset.ResetPasswordRequest;
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

    @PostMapping("/register")
    public void register(@RequestBody OperatorRegisterCredentials operatorRegisterCredentials) {
        authService.register(operatorRegisterCredentials);
    }

    @GetMapping("/require-password-reset")
    public String requirePasswordReset(@RequestBody ResetPasswordRequest request) throws WrongCredentialsException {
        return authService.generateTokenForPasswordReset(request.getUsername(), request.getSecretPhrase());
    }

    // Todo use normal logging
    @PostMapping("/reset-password/{token}")
    public void resetPassword(
            @PathVariable String token,
            @RequestParam String newPassword) throws WrongCredentialsException {
        System.out.println("Reset request: " + token);
        authService.resetPassword(token, newPassword);
    }
}