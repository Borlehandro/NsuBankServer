package com.sibdever.nsu_bank_system_server.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditsTable {

    @EmbeddedId
    @NonNull
    private CreditTableId id;

    public CreditsTable(@NonNull CreditTableId creditTableId,
                        double expectedPayout,
                        double paymentOfPercents,
                        double paymentOfDebt,
                        double balanceAfterPayment,
                        @NonNull CreditStatus creditStatusAfterPayment) {
        this.expectedPayout = expectedPayout;
        this.paymentOfPercents = paymentOfPercents;
        this.paymentOfDebt = paymentOfDebt;
        this.balanceAfterPayment = balanceAfterPayment;
        this.creditStatusAfterPayment = creditStatusAfterPayment;
        this.id = creditTableId;
    }

    public CreditsTable(@NonNull CreditTableId creditTableId,
                        Set<Payment> payment,
                        double expectedPayout,
                        double paymentOfPercents,
                        double paymentOfDebt,
                        double balanceAfterPayment) {
        this.payment = payment;
        this.expectedPayout = expectedPayout;
        this.paymentOfPercents = paymentOfPercents;
        this.paymentOfDebt = paymentOfDebt;
        this.balanceAfterPayment = balanceAfterPayment;
        this.id = creditTableId;
    }

    public CreditsTable(@NonNull CreditTableId creditTableId,
                        Set<Payment> payment,
                        double expectedPayout,
                        double paymentOfPercents,
                        double paymentOfDebt,
                        double balanceAfterPayment,
                        double fee) {
        this.payment = payment;
        this.expectedPayout = expectedPayout;
        this.paymentOfPercents = paymentOfPercents;
        this.paymentOfDebt = paymentOfDebt;
        this.balanceAfterPayment = balanceAfterPayment;
        this.id = creditTableId;
        this.fee = fee;
    }

    // Todo use LAZY
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Payment> payment;

    private double expectedPayout;

    private double paymentOfPercents;

    private double paymentOfDebt;

    private double balanceAfterPayment;

    private double fee;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @NonNull
    private CreditStatus creditStatusAfterPayment = CreditStatus.ACTIVE;

}