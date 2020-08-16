package com.pos.leaders.leaderspossystem.Models.Currency;

/**
 * Created by Win8.1 on 4/20/2020.
 */

public class ChooseCurrencyList {
    public String key;
    public String value;
    public ChooseCurrencyList(String _key,String _vale ) {
        key = _key;
        value= _vale;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
