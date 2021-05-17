package com.sibdever.nsu_bank_system_server.data.model.request;

import com.sibdever.nsu_bank_system_server.data.model.entities.PaymentChannel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GiveCreditRequest {
    private int clientId;
    private int offerId;
    private int monthPeriod;
    private double sum;
    private PaymentChannel paymentChannel;
}
