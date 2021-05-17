package com.sibdever.nsu_bank_system_server.data.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "offers_history")
public class OfferHistoryRecord {
    @EmbeddedId
    private OffersHistoryId id;
}
