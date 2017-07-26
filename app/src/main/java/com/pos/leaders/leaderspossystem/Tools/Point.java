package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;

/**
 * Created by Win8.1 on 7/26/2017.
 */

public class Point {
    public static int saleId ;
    public static int point;

    public static int getCustmer_id() {
        return custmer_id;
    }

    public static void setCustmer_id(int custmer_id) {
        Point.custmer_id = custmer_id;
    }

    public static int custmer_id;

    public static int getSaleId() {
        return saleId;
    }

    public static void setSaleId(int saleId) {
        Point.saleId = saleId;
    }

    public static int getPoint() {
        return point;
    }

    public static void setPoint(int point) {
        Point.point = point;
    }

    public Point(int saleId, int point,int custmer_id) {
        this.saleId=saleId;
        this.point=point;
this.custmer_id=custmer_id;
    }

    public Point(Context context) {
    }




}
