package com.sibdever.nsu_bank_system_server.data.filtering;

import com.sibdever.nsu_bank_system_server.data.model.entities.Credit;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
public class CreditsSpecification implements Specification<Credit> {

    @NonNull
    private final CreditSearchCriteria criteria;

    @Override
    public Predicate toPredicate(@NonNull Root<Credit> root,
                                 @NonNull CriteriaQuery<?> criteriaQuery,
                                 @NonNull CriteriaBuilder criteriaBuilder) {

        if(criteria.getOperation().equals(Operation.EQUALS)) {
            return criteriaBuilder.equal(root.get(criteria.getFieldName()), criteria.getValue().toString());
        }

        return null;
    }
}
