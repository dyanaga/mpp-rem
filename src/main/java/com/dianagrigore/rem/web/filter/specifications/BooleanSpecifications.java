package com.dianagrigore.rem.web.filter.specifications;

import com.dianagrigore.rem.web.exception.CommonUtilsException;
import com.dianagrigore.rem.web.filter.FieldFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;

public final class BooleanSpecifications {
    public static <T> Specification<T> fieldEquals(String fieldName, Boolean value) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(fieldName), value));
    }

    public static <T> Specification<T> fieldNotEquals(String fieldName, Boolean value) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(fieldName), value));
    }

    public static <T> Specification<T> getSpecification(@NonNull FieldFilter fieldFilter) {
        switch (fieldFilter.getFilterOperation()) {
            case EQUALS:
                return fieldEquals(fieldFilter.getFieldName(), Boolean.valueOf(fieldFilter.getValue()));
            case NOT_EQUALS:
                return fieldNotEquals(fieldFilter.getFieldName(), Boolean.valueOf(fieldFilter.getValue()));
        }
        throw new CommonUtilsException(String.format("Invalid boolean operation for fieldFilter=[%s]", fieldFilter));
    }

    public static <T> void appendSpecifications(List<Specification<T>> specifications, Map<String, FieldFilter> fieldFiltersMap, List<String> fields) {
        for (var field : fields) {
            if (fieldFiltersMap.containsKey(field)) {
                specifications.add(getSpecification(fieldFiltersMap.get(field)));
            }
        }
    }

}
