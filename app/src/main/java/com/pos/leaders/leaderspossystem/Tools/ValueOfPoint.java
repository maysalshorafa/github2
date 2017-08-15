package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;

/**
 * Created by Win8.1 on 7/26/2017.
 */

public class ValueOfPoint {
    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        ValueOfPoint.id = id;
    }

    public static double getValue() {
        return value;
    }

    public static void setValue(int value) {
        ValueOfPoint.value = value;
    }

    public static String getCreate_Date() {
        return create_Date;
    }

    public static void setCreate_Date(String create_Date) {
        ValueOfPoint.create_Date = create_Date;
    }

    public static int id ;
    public static double value;
    public static String create_Date ;
    public ValueOfPoint(int id, double value, String create_Date) {
        this.id=id;
        this.value=value;
        this.create_Date=create_Date;
    }

    public ValueOfPoint(Context context) {
    }

}
