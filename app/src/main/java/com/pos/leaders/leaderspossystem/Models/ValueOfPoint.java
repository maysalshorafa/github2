package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 7/26/2017.
 */

public class ValueOfPoint {
    public long id ;
    public double value;
    public String create_Date ;

    public ValueOfPoint() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCreate_Date() {
        return create_Date;
    }

    public void setCreate_Date(String create_Date) {
        this.create_Date = create_Date;
    }

    public ValueOfPoint(long id, double value, String create_Date) {
        this.id=id;
        this.value=value;
        this.create_Date=create_Date;
    }
}
