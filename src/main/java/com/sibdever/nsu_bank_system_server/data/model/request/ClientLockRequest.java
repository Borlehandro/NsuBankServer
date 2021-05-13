package com.sibdever.nsu_bank_system_server.data.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientLockRequest {
    private int clientId;
    private int days;
    private int hours;
    private int minutes;
    private int seconds;
}