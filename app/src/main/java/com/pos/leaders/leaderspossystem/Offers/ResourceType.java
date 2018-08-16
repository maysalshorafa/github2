package com.pos.leaders.leaderspossystem.Offers;

import java.util.HashMap;
import java.util.Map;

public enum ResourceType {
    OFFER("offer"),
    PRODUCT("product"),
    CATEGORY("category"),
    MULTIPRODUCT("multiProduct");

    ResourceType(final String value) {
        this.value = value;
    }
    private String value;
    public String getValue() {
        return value;
    }

    private static final Map<String, ResourceType> map = new HashMap<>();
    static {
        for (ResourceType en : values()) {
            map.put(en.value, en);
        }
    }

    public static ResourceType valueFor(String name) {
        return map.get(name);
    }
}
