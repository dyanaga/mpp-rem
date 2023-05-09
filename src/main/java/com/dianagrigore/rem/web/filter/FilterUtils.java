package com.dianagrigore.rem.web.filter;


import com.dianagrigore.rem.web.exception.CommonUtilsException;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Static custom filter library that supports some of the basic and most used fiql filters.
 */
public final class FilterUtils {

    public static final String DELIMITER = ";";
    public static final String EQUALS = "==";
    public static final String NOT_EQUALS = "!=";
    public static final String LIKE = "=like=";
    public static final String IN = "=in=";
    public static final String LESS = "<";
    public static final String GREATER = ">";
    public static final String ACTIVE = "active";

    private static final FieldFilter DEFAULT_ACTIVE_FILTER = new FieldFilter()
            .setFieldName(FilterUtils.ACTIVE)
            .setFilterOperation(FilterOperation.EQUALS)
            .setValue("true");

    public static Map<String, FieldFilter> getFieldFiltersMap(String filter) {
        return getFieldFilters(filter)
                .stream()
                .collect(Collectors.toMap(FieldFilter::getFieldName, fieldFilter -> fieldFilter));
    }

    public static Map<String, FieldFilter> getFieldFiltersMapWithActive(String filter) {
        Map<String, FieldFilter> fieldFiltersMap = getFieldFiltersMap(filter);
        addActiveFilterIfNotExists(fieldFiltersMap);
        return fieldFiltersMap;
    }

    public static List<FieldFilter> getFieldFilters(String filter) {

        if (StringUtils.isNotBlank(filter)) {
            return Arrays.stream(filter.split(DELIMITER))
                    .map(operation -> {
                        if (operation.contains(EQUALS)) {
                            return getFieldFilter(operation, EQUALS, FilterOperation.EQUALS);
                        }
                        if (operation.contains(NOT_EQUALS)) {
                            return getFieldFilter(operation, NOT_EQUALS, FilterOperation.NOT_EQUALS);
                        }
                        if (operation.contains(LIKE)) {
                            return getFieldFilter(operation, LIKE, FilterOperation.LIKE);
                        }
                        if (operation.contains(IN)) {
                            return getFieldFilter(operation, IN, FilterOperation.IN);
                        }
                        if (operation.contains(LESS)) {
                            return getFieldFilter(operation, LESS, FilterOperation.LESS);
                        }
                        if (operation.contains(GREATER)) {
                            return getFieldFilter(operation, GREATER, FilterOperation.GREATER);
                        }
                        throw new CommonUtilsException(String.format("Invalid filter %s!", operation));
                    })
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    public static void addActiveFilterIfNotExists(Map<String, FieldFilter> fieldFilterMap) {
        if (!fieldFilterMap.containsKey(FilterUtils.ACTIVE)) {
            fieldFilterMap.put(FilterUtils.ACTIVE, DEFAULT_ACTIVE_FILTER);
        }
    }

    private static FieldFilter getFieldFilter(String operation, String stringOperation, FilterOperation filterOperation) {
        String[] split = operation.split(stringOperation);
        return getFieldFilter(split[0], filterOperation, split.length > 1 ? split[1] : "");
    }

    private static FieldFilter getFieldFilter(String field, FilterOperation filterOperation, String value) {
        return new FieldFilter()
                .setFieldName(field)
                .setFilterOperation(filterOperation)
                .setValue(value);
    }
}
