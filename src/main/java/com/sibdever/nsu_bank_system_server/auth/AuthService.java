package com.sibdever.nsu_bank_system_server.auth;

import com.sibdever.nsu_bank_system_server.operator.Operator;
import com.sibdever.nsu_bank_system_server.operator.OperatorRegisterCredentials;
import com.sibdever.nsu_bank_system_server.operator.OperatorsRepository;
import com.sibdever.nsu_bank_system_server.operator.WrongCredentialsException;
import com.sibdever.nsu_bank_system_server.password_reset.PasswordResetTokens;
import com.sibdever.nsu_bank_system_server.password_reset.PasswordResetTokensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class AuthService {

    private final OperatorsRepository userRepository;
    private final PasswordResetTokensRepository passwordResetTokensRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthService(OperatorsRepository userRepository, PasswordResetTokensRepository passwordResetTokensRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.passwordResetTokensRepository = passwordResetTokensRepository;
        this.encoder = encoder;
    }

    public void login(OperatorRegisterCredentials credentials) {
    }

    public void register(OperatorRegisterCredentials credentials) {
        userRepository.save(new Operator(
                credentials.getUsername(),
                credentials.getFullName(),
                encoder.encode(credentials.getPassword()),
                credentials.getSecretPhrase()));
    }

    public String generateTokenForPasswordReset(String username, String secretPhrase) throws WrongCredentialsException {
        var operator = userRepository.findOperatorByUsername(username);
        if (operator.getSecretPhrase().equals(secretPhrase) && operator.isAccountNonLocked()) {
            operator.setCredentialsNonExpired(false);
            String stringForToken = operator.getSecretPhrase() + System.currentTimeMillis();
            try {
                String token =
                        Base64.getUrlEncoder().withoutPadding().encodeToString(
                                MessageDigest
                                        .getInstance("SHA-256")
                                        .digest(stringForToken.getBytes(StandardCharsets.UTF_8)));

                passwordResetTokensRepository.save(new PasswordResetTokens(operator, token));
                return token;
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new WrongCredentialsException("Wrong username or secret phrase");
        }
    }

    // Todo use normal logging
    public void resetPassword(String token, String newPassword) throws WrongCredentialsException {
        var resetToken = passwordResetTokensRepository.findFirstByResetToken(token);
        if (resetToken != null) {
            System.out.println("On reset: " + resetToken.getOperator().getUsername());
            resetToken.getOperator().setPassword(newPassword);
            resetToken.getOperator().setCredentialsNonExpired(true);
            resetToken.getOperator().setPassword(encoder.encode(newPassword));
            userRepository.save(resetToken.getOperator());
        } else throw new WrongCredentialsException("Wrong reset token");
    }

}