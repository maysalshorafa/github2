package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by LeadPos on 18/11/19.
 */

public class SumOfSalesPerProduct {
    public String _id;
    public double total;
    public double count;



    public SumOfSalesPerProduct(String idProduct, double total, double countSalesProduct) {
      this._id=idProduct;
        this.total=total;
        this.count=countSalesProduct;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "SumOfSalesPerProduct{" +
                "_id=" + _id +
                ", total=" + total +
                ", count=" + count +
                '}';
    }
}
