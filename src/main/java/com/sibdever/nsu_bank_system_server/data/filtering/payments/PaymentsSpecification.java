package com.sibdever.nsu_bank_system_server.data.filtering.payments;

import com.sibdever.nsu_bank_system_server.data.model.entities.CreditsTable;
import com.sibdever.nsu_bank_system_server.data.model.entities.Payment;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

import static com.sibdever.nsu_bank_system_server.data.filtering.CriteriaOperator.*;

@RequiredArgsConstructor
public class PaymentsSpecification implements Specification<Payment> {

    @NonNull
    private final PaymentSearchCriteria criteria;

    @Override
    @SuppressWarnings({"unchecked"})
    public Predicate toPredicate(@NonNull Root<Payment> root,
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
    private Expression<? extends Comparable> generateExpression(Root<Payment> root) {
        Path<?> expr = root;
        for (String fieldName : criteria.getKey().getFieldNames()) {
            expr = expr.get(fieldName);
        }
        return (Path<? extends Comparable>) expr;
    }

}
