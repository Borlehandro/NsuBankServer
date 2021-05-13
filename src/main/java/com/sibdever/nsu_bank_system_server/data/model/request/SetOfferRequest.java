package com.sibdever.nsu_bank_system_server.data.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SetOfferRequest {
    private int clientId;
    private int offerId;
}
