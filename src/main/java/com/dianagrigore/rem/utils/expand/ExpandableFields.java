package com.dianagrigore.rem.utils.expand;

public enum ExpandableFields {
    USERS("users"),
    CREATOR("creator"),
    USER("user"),
    LISTING("listing"),
    OFFERS("offers"),
    LISTINGS("listings"),
    LISTINGS_CREATED("listingsCreated"),
    WRITTEN_REVIEWS("writtenReviews"),
    REVIEWS("reviews");

    private final String stringValue;

    ExpandableFields(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
