package com.sibdever.nsu_bank_system_server.data.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OperatorRegisterCredentials {
    private String username;
    private String fullName;
    private String password;
    private String secretPhrase;
}
