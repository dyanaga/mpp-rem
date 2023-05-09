package com.dianagrigore.rem.utils.expand;

import java.util.Objects;

public class ExpandBuilder {
    private static final String DELIMITER = ",";

    private String value;

    private ExpandBuilder() {

    }

    private ExpandBuilder(String value) {
        this.value = value + DELIMITER;
    }

    public static ExpandBuilder of() {
        return new ExpandBuilder();
    }

    public static ExpandBuilder of(ExpandableFields field) {
        return new ExpandBuilder(field.getStringValue());
    }

    public static ExpandBuilder of(String value) {
        return new ExpandBuilder(value);
    }

    public static ExpandBuilder of(ExpandBuilder builder) {
        return new ExpandBuilder(builder.toString());
    }

    public ExpandBuilder and(ExpandableFields field) {
        if (Objects.isNull(value)) {
            this.value = field.getStringValue() + DELIMITER;
        } else {
            this.value += field.getStringValue() + DELIMITER;
        }
        return this;
    }

    public boolean contains(ExpandableFields field) {
        if (Objects.isNull(value)) {
            return false;
        }
        return this.value.contains(field.getStringValue());
    }

    public ExpandBuilder remove(ExpandableFields field) {
        if (Objects.isNull(value)) {
            return this;
        }
        this.value = this.value.replace(field.getStringValue(), "");
        return this;
    }

    @Override
    public String toString() {
        return value;
    }
}
