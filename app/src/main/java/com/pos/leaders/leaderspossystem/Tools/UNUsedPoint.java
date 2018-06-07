package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;

/**
 * Created by Win8.1 on 8/8/2017.
 */

public class UNUsedPoint {
    public int saleId ;
    public int point;

    public int getCustmer_id() {
        return custmer_id;
    }

    public void setCustmer_id(int custmer_id) {
        this.custmer_id = custmer_id;
    }

    public int custmer_id;

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

    public UNUsedPoint(int saleId, int point,int custmer_id) {
        this.saleId=saleId;
        this.point=point;
        this.custmer_id=custmer_id;
    }

    public UNUsedPoint(Context context) {
    }



}
