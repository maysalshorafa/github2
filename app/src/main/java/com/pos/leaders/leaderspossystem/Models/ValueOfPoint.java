package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 7/26/2017.
 */

public class ValueOfPoint {
    public long valueOfPointId;
    public double value;
    public long createdAt;

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

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public ValueOfPoint(long valueOfPointId, double value, long createdAt) {
        this.valueOfPointId = valueOfPointId;
        this.value=value;
        this.createdAt = createdAt;
    }
}
