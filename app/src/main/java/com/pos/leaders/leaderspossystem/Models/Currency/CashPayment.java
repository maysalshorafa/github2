package com.pos.leaders.leaderspossystem.Models.Currency;

import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.Sale;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.Date;
import java.util.Locale;

/**
 * Created by Win8.1 on 9/27/2017.
 */

public class CashPayment {
    private long id;
    private long saleId;
    private double amount;
    private long currency_type;
    private long createDate;

    public CashPayment() {
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }



    public long getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(long currency_type) {
        this.currency_type = currency_type;
    }




    // Constructors
    public CashPayment(long id,  long saleId, double amount,long currency_type,long createDate) {
        this.id = id;
        this.amount = amount;
        this.saleId = saleId;
        this.currency_type=currency_type;
        this.createDate=createDate;
    }

    public CashPayment(CashPayment p) {
        this(p.getId(),  p.getSaleId(),p.getAmount(),p.getCurrency_type(),p.getCreateDate());
    }

    // Getters
    public long getId() {
        return id;
    }


    public long getSaleId() {
        return saleId;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ",currency_type ='" + currency_type + '\'' +
                ", amount='" + amount + '\'' +
                ", saleId=" + saleId +
                '}';
    }


}
