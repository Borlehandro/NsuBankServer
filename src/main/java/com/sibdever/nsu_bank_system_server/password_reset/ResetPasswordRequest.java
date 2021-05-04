package com.sibdever.nsu_bank_system_server.password_reset;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResetPasswordRequest {
    private String username;
    private String secretPhrase;

    public ResetPasswordRequest(
            @JsonProperty("username") String username,
            @JsonProperty("secretPhrase") String secretPhrase) {
        this.username = username;
        this.secretPhrase = secretPhrase;
    }

    public String getUsername() {
        return username;
    }

    public String getSecretPhrase() {
        return secretPhrase;
    }
}
