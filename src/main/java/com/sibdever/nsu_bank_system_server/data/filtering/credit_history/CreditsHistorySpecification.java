package com.sibdever.nsu_bank_system_server.data.filtering.credit_history;

import com.sibdever.nsu_bank_system_server.data.filtering.CriteriaOperator;
import com.sibdever.nsu_bank_system_server.data.filtering.credit_table.CreditTableCriteriaKey;
import com.sibdever.nsu_bank_system_server.data.filtering.credit_table.CreditTableSearchCriteria;
import com.sibdever.nsu_bank_system_server.data.filtering.credit_table.CreditsTableSpecification;
import com.sibdever.nsu_bank_system_server.data.model.entities.CreditHistory;
import com.sibdever.nsu_bank_system_server.data.model.entities.CreditsTable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sibdever.nsu_bank_system_server.data.filtering.CriteriaOperator.*;

@RequiredArgsConstructor
public class CreditsHistorySpecification implements Specification<CreditHistory> {

    @NonNull
    private final CreditHistorySearchCriteria criteria;

    @Override
    @SuppressWarnings({"unchecked"})
    public Predicate toPredicate(@NonNull Root<CreditHistory> root,
                                 @NonNull CriteriaQuery<?> criteriaQuery,
                                 @NonNull CriteriaBuilder criteriaBuilder) {

        // Todo refactor getting
        if (criteria.getOperator().equals(EQUALS)) {
            return criteriaBuilder.equal(generateExpression(root), criteria.getValue());
        } else if (criteria.getOperator().equals(NOT_EQUALS)) {
            return criteriaBuilder.notEqual(generateExpression(root), criteria.getValue());
        } else if (criteria.getOperator().equals(GREATER)) {
            return criteriaBuilder.greaterThan(generateExpression(root), (Comparable) criteria.getValue());
        } else if (criteria.getOperator().equals(GREATER_OR_EQUALS)) {
            return criteriaBuilder.greaterThanOrEqualTo(generateExpression(root), (Comparable) criteria.getValue());
        } else if (criteria.getOperator().equals(LESS)) {
            return criteriaBuilder.lessThan(generateExpression(root), (Comparable) criteria.getValue());
        } else if (criteria.getOperator().equals(LESS_OR_EQUALS)) {
            return criteriaBuilder.lessThanOrEqualTo(generateExpression(root), (Comparable) criteria.getValue());
        }
        return null;
    }

    // Todo refactor
    @SuppressWarnings({"unchecked"})
    private Expression<? extends Comparable> generateExpression(Root<CreditHistory> root) {
        Path<?> expr = root;
        for (String fieldName : criteria.getKey().getFieldNames()) {
            expr = expr.get(fieldName);
        }
        return (Path<? extends Comparable>) expr;
    }

    public static CreditHistorySpecificationBuilder builder() {
        return new CreditHistorySpecificationBuilder();
    }

    public static class CreditHistorySpecificationBuilder {

        private final List<CreditHistorySearchCriteria> criteriaList;

        private CreditHistorySpecificationBuilder() {
            criteriaList = new ArrayList<>();
        }

        public CreditHistorySpecificationBuilder with(String key, String operator, String value) {
            criteriaList.add(new CreditHistorySearchCriteria(
                    CreditHistoryCriteriaKey.valueOf(key.toUpperCase()),
                    CriteriaOperator.ofSymbol(operator),
                    value)
            );
            return this;
        }

        public Optional<CreditsHistorySpecification> build() {
            return this.criteriaList
                    .stream()
                    .map(CreditsHistorySpecification::new)
                    .reduce(
                            (specification, specification2)
                                    -> (CreditsHistorySpecification) specification.and(specification2)
                    );
        }
    }

}
