package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 7/26/2017.
 */

public class ValueOfPoint {
    public long valueOfPointId;
    public double value;
    public long create_Date ;

    public ValueOfPoint() {
    }

    public long getValueOfPointId() {
        return valueOfPointId;
    }

    public void setValueOfPointId(long valueOfPointId) {
        this.valueOfPointId = valueOfPointId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public long getCreate_Date() {
        return create_Date;
    }

    public void setCreate_Date(long create_Date) {
        this.create_Date = create_Date;
    }

    public ValueOfPoint(long valueOfPointId, double value, long create_Date) {
        this.valueOfPointId = valueOfPointId;
        this.value=value;
        this.create_Date=create_Date;
    }
}
