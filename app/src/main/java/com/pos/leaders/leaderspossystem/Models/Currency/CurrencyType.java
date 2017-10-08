package com.pos.leaders.leaderspossystem.Models.Currency;

/**
 * Created by Win8.1 on 9/25/2017.
 */

public class CurrencyType {

    private long id;
    private String type;

    public CurrencyType(long id, String type) {
        this.id = id;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
