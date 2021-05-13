package com.sibdever.nsu_bank_system_server.security;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CredentialsAuthenticationRequest {
    private String username;
    private String password;
}
