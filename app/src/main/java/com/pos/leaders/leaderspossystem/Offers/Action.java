package com.pos.leaders.leaderspossystem.Offers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Win8.1 on 7/31/2018.
 */

public enum Action {
    ACTION("action"),
    PRICE_FOR_PRODUCT("PRICE_FOR_PRODUCT"),
    GET_GIFT_PRODUCT("GET_GIFT_PRODUCT"),
    GET_DISCOUNT("Getdiscount"),
    NAME("name"),
    VALUE("value"),
    RESOURCE_TYPE("resourceType"),
    RESOURCES_LIST("resourcesList"),
    QUANTITY("quantity"),
    SAME_RESOURCE("sameResource");

    Action(final String v) {
        this.v = v;
    }
    private String v;
    public String getValue() {
        return v;
    }
    private static final Map<String, Action> map = new HashMap<>();
    static {
        for (Action en : values()) {
            map.put(en.v, en);
        }
    }
}
