package com.sibdever.nsu_bank_system_server.data.filtering.credit_table;

import com.sibdever.nsu_bank_system_server.data.filtering.CriteriaOperator;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditTableSearchCriteria {
    private CreditTableCriteriaKey key;
    private CriteriaOperator operator;
    private Object value;
}
