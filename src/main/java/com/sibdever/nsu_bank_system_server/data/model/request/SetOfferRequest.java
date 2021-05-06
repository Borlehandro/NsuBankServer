package com.sibdever.nsu_bank_system_server.data.model.request;

public class SetOfferRequest {

    private int clientId;
    private int offerId;

    public SetOfferRequest() {
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }
}
