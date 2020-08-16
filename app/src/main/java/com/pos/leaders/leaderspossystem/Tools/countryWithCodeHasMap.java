package com.pos.leaders.leaderspossystem.Tools;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Win8.1 on 4/20/2020.
 */

public enum countryWithCodeHasMap {
    Israel("ILS"),
    USA("USD"),
    GreatBritain("GBP"),
    Japan("JPY"),
    EMU("EUR"),
    Australia("AUD"),
    Canada("CAD"),
    Denmark("DKK"),
    Norway("NOK"),
    Sweden("SEK"),
    Switzerland("CHF"),
    Jordan("JOD"),
    SouthAfrica("ZAR"),
    Lebanon("LBP") ,
    Egypt("EGP");
    countryWithCodeHasMap(final String v) {
        this.v = v;
    }
    private String v;
    public String getValue() {
        return v;
    }
    private static final Map<String, countryWithCodeHasMap> map = new HashMap<>();
    static {
        for (countryWithCodeHasMap en : values()) {
            map.put(en.v, en);
        }
    }
}
