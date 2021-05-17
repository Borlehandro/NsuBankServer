package com.sibdever.nsu_bank_system_server.data.filtering;

import com.sibdever.nsu_bank_system_server.data.filtering.Operation;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditSearchCriteria {
    private String fieldName;
    private Operation operation;
    private Object value;

//    // Credit
//    private int id;
//    private LocalDateTime startDate;
//    private int mouthPeriod;
//    private LocalDateTime endTime;
//    private double sum;
//    private double balance;
//    private CreditStatus status;
//
//    // Offer
//    private int offerId;
//    private double percentsPerMonth;
//    private int offerMinimumMonthPeriod;
//    private int offerMaximumMonthPeriod;
//    private int offerMinimumSum;
//    private int offerMaximumSum;
//
//    // Client
//    private String clientName;
//    private ClientStatus clientStatus;
}
