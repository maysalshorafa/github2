package com.pos.leaders.leaderspossystem.Offers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Win8.1 on 7/31/2018.
 */

public enum Action {
    ACTION("action"),
    PRICE_FOR_PRODUCT("Price for Product"),
    GET_GIFT_PRODUCT("Get gift product"),
    NAME("name"),
    VALUE("value");

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
