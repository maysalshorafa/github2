package com.pos.leaders.leaderspossystem.Offers;

public enum OfferStatus {
    ACTIVE ("active"),
    INACTIVE("inactive");

    OfferStatus(final String value) {
        this.value = value;
    }
    private String value;
    public String getValue() {
        return value;
    }

}
