package com.dianagrigore.rem.utils.expand;

public enum ExpandableFields {
    USERS("users"),
    USER("user"),
    LISTING("listing"),
    OFFERS("offers");

    private final String stringValue;

    ExpandableFields(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
