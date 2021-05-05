package com.sibdever.nsu_bank_system_server.data.model;

import com.sibdever.nsu_bank_system_server.data.model.Operator;

import javax.persistence.*;

@Entity
public class PasswordResetTokens {

    public PasswordResetTokens() {}

    public PasswordResetTokens(Operator operator, String resetToken) {
        this.operator = operator;
        this.resetToken = resetToken;
    }

    @Id
    private int operatorId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "operator_id")
    private Operator operator;

    private String resetToken;

    public Operator getOperator() {
        return operator;
    }

    public String getResetToken() {
        return resetToken;
    }
}
