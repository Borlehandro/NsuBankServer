package com.sibdever.nsu_bank_system_server.data.filtering.credit_history;

import com.sibdever.nsu_bank_system_server.data.filtering.CriteriaOperator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditHistorySearchCriteria {
    private CreditHistoryCriteriaKey key;
    private CriteriaOperator operator;
    private Object value;
}
