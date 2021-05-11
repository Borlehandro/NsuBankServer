package com.sibdever.nsu_bank_system_server.data.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "offers_history")
public class OfferHistoryRecord {

    @EmbeddedId
    private OffersHistoryId id;

    public OfferHistoryRecord() {
    }

    public OfferHistoryRecord(OffersHistoryId id) {
        this.id = id;
    }

    public OffersHistoryId getId() {
        return id;
    }

    public void setId(OffersHistoryId id) {
        this.id = id;
    }
}
