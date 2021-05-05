package com.sibdever.nsu_bank_system_server.data.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class CreditTableId implements Serializable {

    protected Credit credit;
    protected LocalDateTime timestamp;

    public CreditTableId(){}

    public CreditTableId(Credit credit, LocalDateTime timestamp) {
        this.credit = credit;
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreditTableId that)) return false;
        return credit.equals(that.credit) && timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(credit, timestamp);
    }
}