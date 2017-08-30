package com.pos.leaders.leaderspossystem.Models.Offers;

/**
 * Created by Win8.1 on 8/2/2017.
 */

public class Rule11 {

    private long id ;
    private double amount;
    private double discountAmount;
    private int clubContain;
    private int contain;

    public Rule11( double amount, double discountAmount,int contain,int clubContain) {
        this.amount = amount;
        this.discountAmount = discountAmount;
        this.contain = contain;
        this.clubContain = clubContain;
    }

    public Rule11(long id, double amount, double discountAmount, int clubContain, int contain) {
        this.id = id;
        this.amount = amount;
        this.discountAmount = discountAmount;
        this.clubContain = clubContain;
        this.contain = contain;
    }

    public Rule11() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getClubContain() {
        return clubContain;
    }

    public void setClubContain(int clubContain) {
        this.clubContain = clubContain;
    }

    public int getContain() {
        return contain;
    }

    public void setContain(int contain) {
        this.contain = contain;
    }
}
