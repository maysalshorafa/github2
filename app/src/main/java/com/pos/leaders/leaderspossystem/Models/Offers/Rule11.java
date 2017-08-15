package com.pos.leaders.leaderspossystem.Models.Offers;

/**
 * Created by Win8.1 on 8/2/2017.
 */

public class Rule11 {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Rule11( int amount, int discountAmount,int contain,int club_contain) {
        this.amount = amount;
        this.discountAmount = discountAmount;
        this.contain=contain;
        this.club_contain=club_contain;
    }

    private   int id ;
    private   int amount;
    private  int discountAmount;

    public int getClub_contain() {
        return club_contain;
    }

    public void setClub_contain(int club_contain) {
        this.club_contain = club_contain;
    }

    private  int club_contain;

    public int getContain() {
        return contain;
    }

    public void setContain(int contain) {
        this.contain = contain;
    }

    private  int contain;






}
