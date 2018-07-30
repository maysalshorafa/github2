package com.pos.leaders.leaderspossystem.Offers;

public enum ResourceType {
    PRODUCT("product"),
    CATEGORY("category");

    ResourceType(final String value) {
        this.value = value;
    }
    private String value;
    public String getValue() {
        return value;
    }
}
