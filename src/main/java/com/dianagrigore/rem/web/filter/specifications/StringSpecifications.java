package com.dianagrigore.rem.web.filter.specifications;

import com.dianagrigore.rem.web.exception.CommonUtilsException;
import com.dianagrigore.rem.web.filter.FieldFilter;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class StringSpecifications {
    public static <T> Specification<T> fieldEquals(String fieldName, String value) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(getFieldValue(fieldName, root), value));
    }

    public static <T> Specification<T> fieldNotEquals(String fieldName, String value) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.notEqual(getFieldValue(fieldName, root), value));
    }

    public static <T> Specification<T> fieldLike(String fieldName, String value) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(getFieldValue(fieldName, root)), "%" + value.toUpperCase() + "%"));
    }

    public static <T> Specification<T> fieldIn(String fieldName, Collection<String> values) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            Predicate in = getFieldValue(fieldName, root).in(values);
            criteriaQuery.where(in);
            return in;
        });
    }

    private static <T> Path<String> getFieldValue(String fieldName, Root<T> root) {
        if (fieldName.contains(".")) {
            List<String> pathToValue = new ArrayList<>(Arrays.asList(fieldName.split("\\.")));
            Join<Object, Object> join = root.join(pathToValue.remove(0));
            while (pathToValue.size() > 1) {
                join = join.join(pathToValue.remove(0));
            }
            return join.get(pathToValue.get(0));
        }
        return root.get(fieldName);
    }

    public static <T> Specification<T> getSpecification(@NonNull FieldFilter fieldFilter) {

        switch (fieldFilter.getFilterOperation()) {
            case EQUALS:
                return fieldEquals(fieldFilter.getFieldName(), fieldFilter.getValue());
            case NOT_EQUALS:
                return fieldNotEquals(fieldFilter.getFieldName(), fieldFilter.getValue());
            case LIKE:
                return fieldLike(fieldFilter.getFieldName(), fieldFilter.getValue());
            case IN:
                return fieldIn(fieldFilter.getFieldName(), Arrays.asList(fieldFilter.getValue().split(",")));
        }
        throw new CommonUtilsException(String.format("Invalid string operation for fieldFilter=[%s]", fieldFilter));
    }

    public static <T> void appendSpecifications(List<Specification<T>> specifications, Map<String, FieldFilter> fieldFiltersMap, List<String> fields) {
        for (var field : fields) {
            if (fieldFiltersMap.containsKey(field)) {
                specifications.add(getSpecification(fieldFiltersMap.get(field)));
            }
        }
    }

}
