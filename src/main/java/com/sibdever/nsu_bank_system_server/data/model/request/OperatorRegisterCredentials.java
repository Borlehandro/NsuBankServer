package com.sibdever.nsu_bank_system_server.data.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperatorRegisterCredentials {
    private String username;
    private String fullName;
    private String password;
    private String secretPhrase;
}
