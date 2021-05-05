package com.sibdever.nsu_bank_system_server.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OperatorRegisterCredentials {
    private String username;
    private String fullName;
    private String password;
    private String secretPhrase;

    public OperatorRegisterCredentials(
            @JsonProperty("username") String username,
            @JsonProperty("fullName") String fullName,
            @JsonProperty("password") String password,
            @JsonProperty("secretPhrase") String secretPhrase) {

        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.secretPhrase = secretPhrase;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }

    public String getSecretPhrase() {
        return secretPhrase;
    }

}
