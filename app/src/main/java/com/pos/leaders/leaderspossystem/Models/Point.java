package com.pos.leaders.leaderspossystem.Models;

import android.content.Context;

/**
 * Created by Win8.1 on 7/26/2017.
 */

public class Point {
    public int saleId ;
    public int point;
    public int customer_id;

    public int getCustmer_id() {
        return customer_id;
    }

    public void setCustmer_id(int custmer_id) {
        this.customer_id = custmer_id;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public Point(int saleId, int point,int custmer_id) {
        this.saleId = saleId;
        this.point = point;
        this.customer_id = custmer_id;
    }

    public Point(Context context) {
    }


    public Point() {
    }
}
