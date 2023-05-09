package com.dianagrigore.rem.web.filter.specifications;

import com.dianagrigore.rem.web.exception.CommonUtilsException;
import com.dianagrigore.rem.web.filter.FieldFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public final class DateSpecifications {
    private static final Logger logger = LoggerFactory.getLogger(DateSpecifications.class);
    private static final String DEFAULT_DATE_FORMAT_PATTERN = "dd-MM-yyyy";
    private static final String DEFAULT_DATETIME_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(DEFAULT_DATE_FORMAT_PATTERN);
    public static final SimpleDateFormat DEFAULT_DATETIME_FORMAT = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT_PATTERN);

    public static <T> Specification<T> fieldEqualsDate(@NonNull String fieldName, @NonNull Date value) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(fieldName), value));
    }

    public static <T> Specification<T> fieldBetweenDates(@NonNull String fieldName, @NonNull Date firstDate, @NonNull Date secondDate) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(root.get(fieldName), firstDate, secondDate));
    }

    public static <T> Specification<T> fieldBetweenDates(@NonNull String fieldName, @NonNull String firstDate, @NonNull String secondDate) {
        SimpleDateFormat format = firstDate.length() == DEFAULT_DATE_FORMAT_PATTERN.length() ? DEFAULT_DATE_FORMAT : DEFAULT_DATETIME_FORMAT;
        try {
            return fieldBetweenDates(fieldName, format.parse(firstDate), format.parse(secondDate));
        } catch (ParseException e) {
            logger.error("Error while parsing dates [{}] and [{}]", firstDate, secondDate, e);
            throw new CommonUtilsException(String.format("Date [%s] or [%s] has an invalid format! It should be [%s]", firstDate, secondDate, format));
        }
    }

    public static <T> void addFromUntilIfNeeded(@NonNull Map<String, FieldFilter> fieldFiltersMap, @NonNull List<Specification<T>> specifications) {
        addFromUntilIfNeeded(fieldFiltersMap, specifications, "timestamp");
    }

    public static <T> void addFromUntilIfNeeded(@NonNull Map<String, FieldFilter> fieldFiltersMap, @NonNull List<Specification<T>> specifications, @NonNull String fieldName) {
        if (fieldFiltersMap.containsKey("from") && fieldFiltersMap.containsKey("until")) {
            FieldFilter from = fieldFiltersMap.get("from");
            FieldFilter until = fieldFiltersMap.get("until");
            specifications.add(DateSpecifications.fieldBetweenDates(fieldName, from.getValue(), until.getValue()));
        }
    }

}
