package com.sibdever.nsu_bank_system_server.data.filtering;

import com.sibdever.nsu_bank_system_server.data.model.entities.CreditsTable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

import static com.sibdever.nsu_bank_system_server.data.filtering.CreditTableCriteriaOperator.*;

@RequiredArgsConstructor
public class CreditsTableSpecification implements Specification<CreditsTable> {

    @NonNull
    private final CreditTableSearchCriteria criteria;

    @Override
    public Predicate toPredicate(@NonNull Root<CreditsTable> root,
                                 @NonNull CriteriaQuery<?> criteriaQuery,
                                 @NonNull CriteriaBuilder criteriaBuilder) {

        // Todo refactor getting
        if (criteria.getOperator().equals(EQUALS)) {
                return criteriaBuilder.equal(generateExpression(root), criteria.getValue());
        } else if(criteria.getOperator().equals(GREATER)) {
            return criteriaBuilder.greaterThan(generateExpression(root), (Comparable)criteria.getValue());
        } else if(criteria.getOperator().equals(GREATER_OR_EQUALS)) {
            return criteriaBuilder.greaterThanOrEqualTo(generateExpression(root), (Comparable)criteria.getValue());
        }

        return null;
    }

    private Expression<? extends Comparable> generateExpression(Root<CreditsTable> root) {
        Path<?> expr = root;
        for (String fieldName : criteria.getKey().getFieldNames()) {
            expr = expr.get(fieldName);
        }
        return (Path<? extends Comparable>) expr;
    }

}
