package com.pos.leaders.leaderspossystem.Tools;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Win8.1 on 4/20/2020.
 */

public enum symbolWithCodeHashMap {
    USD("$"),
    GBP("£"),
    JPY("¥"),
    EUR("€"),
    AUD("$"),
    CAD("$"),
    DKK("kr"),
    NOK("kr"),
    SEK("kr"),
    CHF("CHF"),
    JOD("JOD"),
    ZAR("R"),
    LBP("£") ,
    EGP("£");

    symbolWithCodeHashMap(final String v) {
        this.v = v;
    }
    private String v;
    public String getValue() {
        return v;
    }
    private static final Map<String, symbolWithCodeHashMap> map = new HashMap<>();
    static {
        for (symbolWithCodeHashMap en : values()) {
            map.put(en.v, en);
        }
    }
}
