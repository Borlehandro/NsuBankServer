package com.sibdever.nsu_bank_system_server.data.model.request;

import com.sibdever.nsu_bank_system_server.data.model.entities.PaymentDetails;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentRequest {
    private int userId;
    private int creditId;
    private PaymentDetails details;
}
