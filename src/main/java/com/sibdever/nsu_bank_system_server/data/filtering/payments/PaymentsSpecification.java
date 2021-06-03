package com.sibdever.nsu_bank_system_server.data.filtering.payments;

import com.sibdever.nsu_bank_system_server.data.filtering.CriteriaOperator;
import com.sibdever.nsu_bank_system_server.data.model.entities.CreditsTable;
import com.sibdever.nsu_bank_system_server.data.model.entities.Payment;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public static PaymentsSpecificationBuilder builder() {
        return new PaymentsSpecificationBuilder();
    }

    public static class PaymentsSpecificationBuilder {

        private final List<PaymentSearchCriteria> criteriaList;

        private PaymentsSpecificationBuilder() {
            criteriaList = new ArrayList<>();
        }

        public PaymentsSpecificationBuilder with(String key, String operator, String value) {
            criteriaList.add(new PaymentSearchCriteria(
                    PaymentCriteriaKey.valueOf(key.toUpperCase()),
                    CriteriaOperator.ofSymbol(operator),
                    value)
            );
            return this;
        }

        public Optional<PaymentsSpecification> build() {
            return this.criteriaList
                    .stream()
                    .map(PaymentsSpecification::new)
                    .reduce(
                            (specification, specification2)
                                    -> (PaymentsSpecification) specification.and(specification2)
                    );
        }
    }

}
