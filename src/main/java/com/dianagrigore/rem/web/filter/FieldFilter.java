package com.dianagrigore.rem.web.filter;

public class FieldFilter {
    private FilterOperation filterOperation;
    private String fieldName;
    private String value;

    public FilterOperation getFilterOperation() {
        return filterOperation;
    }

    public FieldFilter setFilterOperation(FilterOperation filterOperation) {
        this.filterOperation = filterOperation;
        return this;
    }

    public String getFieldName() {
        return fieldName;
    }

    public FieldFilter setFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public String getValue() {
        return value;
    }

    public FieldFilter setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"FieldFilter\", " +
                "\"filterOperation\":" + (filterOperation == null ? "null" : filterOperation) + ", " +
                "\"fieldName\":" + (fieldName == null ? "null" : "\"" + fieldName + "\"") + ", " +
                "\"value\":" + (value == null ? "null" : "\"" + value + "\"") +
                "}";
    }
}
