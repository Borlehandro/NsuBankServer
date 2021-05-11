package com.sibdever.nsu_bank_system_server.data.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PasswordResetTokens {

    public PasswordResetTokens() {}

    public PasswordResetTokens(Operator operator, String resetToken, int timeToLiveSeconds, LocalDateTime generationTime) {
        this.operator = operator;
        this.resetToken = resetToken;
        this.timeToLiveSeconds = timeToLiveSeconds;
        this.generationTime = generationTime;
    }

    @Id
    private int operatorId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "operator_id")
    private Operator operator;

    @Column(nullable = false)
    private String resetToken;

    @Column(nullable = false)
    private int timeToLiveSeconds;

    @Column(nullable = false)
    private LocalDateTime generationTime;

    public Operator getOperator() {
        return operator;
    }

    public String getResetToken() {
        return resetToken;
    }

    public int getTimeToLiveSeconds() {
        return timeToLiveSeconds;
    }

    public LocalDateTime getGenerationTime() {
        return generationTime;
    }
}
