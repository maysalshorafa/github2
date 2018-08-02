package com.pos.leaders.leaderspossystem.Offers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Win8.1 on 7/31/2018.
 */

public enum Action {
    Price_for_Product("Price for Product"),
    Get_gift_product("Get gift product"),
    name("name"),
    value("value");

    Action(final String v) {
        this.v = v;
    }
    private String v;
    public String getV() {
        return v;
    }
    private static final Map<String, Action> map = new HashMap<>();
    static {
        for (Action en : values()) {
            map.put(en.v, en);
        }
    }

    public static Action valueFor(String name) {
        return map.get(name);
    }
}
