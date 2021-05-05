package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.Operator;
import com.sibdever.nsu_bank_system_server.data.model.request.OperatorRegisterCredentials;
import com.sibdever.nsu_bank_system_server.data.repo.OperatorsRepo;
import com.sibdever.nsu_bank_system_server.exception.WrongCredentialsException;
import com.sibdever.nsu_bank_system_server.data.model.PasswordResetTokens;
import com.sibdever.nsu_bank_system_server.data.repo.PasswordResetTokensRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class AuthService {

    private final OperatorsRepo operatorsRepo;
    private final PasswordResetTokensRepo passwordResetTokensRepo;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthService(OperatorsRepo operatorsRepo, PasswordResetTokensRepo passwordResetTokensRepo, PasswordEncoder encoder) {
        this.operatorsRepo = operatorsRepo;
        this.passwordResetTokensRepo = passwordResetTokensRepo;
        this.encoder = encoder;
    }

    public void register(OperatorRegisterCredentials credentials) {
        operatorsRepo.save(new Operator(
                credentials.getUsername(),
                credentials.getFullName(),
                encoder.encode(credentials.getPassword()),
                credentials.getSecretPhrase()));
    }

    public String generateTokenForPasswordReset(String username, String secretPhrase) throws WrongCredentialsException {
        var operator = operatorsRepo.findOperatorByUsername(username);
        if (operator.getSecretPhrase().equals(secretPhrase) && operator.isAccountNonLocked()) {
            operator.setCredentialsNonExpired(false);
            String stringForToken = operator.getSecretPhrase() + System.currentTimeMillis();
            try {
                String token =
                        Base64.getUrlEncoder().withoutPadding().encodeToString(
                                MessageDigest
                                        .getInstance("SHA-256")
                                        .digest(stringForToken.getBytes(StandardCharsets.UTF_8)));

                passwordResetTokensRepo.save(new PasswordResetTokens(operator, token));
                return token;
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new WrongCredentialsException("Wrong username or secret phrase");
        }
    }

    // Todo use normal logging
    // Todo deactivate current JWT after password rest!
    public void resetPassword(String token, String newPassword) throws WrongCredentialsException {
        var resetToken = passwordResetTokensRepo.findFirstByResetToken(token);
        if (resetToken != null) {
            System.out.println("On reset: " + resetToken.getOperator().getUsername());
            resetToken.getOperator().setPassword(newPassword);
            resetToken.getOperator().setCredentialsNonExpired(true);
            resetToken.getOperator().setPassword(encoder.encode(newPassword));
            operatorsRepo.save(resetToken.getOperator());
            passwordResetTokensRepo.delete(resetToken);
        } else throw new WrongCredentialsException("Wrong reset token");
    }
}