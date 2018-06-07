package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 6/21/2017.
 */

public class Club {

    private long clubId;
    private String name ;
    private String description;
    private int type;
    private float percent;
    private int amount;
    private int point;
    private boolean hide=false;

    public Club() {
    }

    public Club(long clubId, String name, String description, int type, float percent, int amount, int point, boolean hide) {
        this.clubId = clubId;
        this.name=name;
        this.description=description;
        this.type=type;
        this.percent = percent;
        this.amount=amount;
        this.point=point;
    }
    public Club(long clubId, String name, String description, int type, float percent, int amount, int point) {
        this.clubId = clubId;
        this.name=name;
        this.description=description;
        this.type=type;
        this.percent = percent;
        this.amount=amount;
        this.point=point;
    }

    public long getClubId() {
        return clubId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getType() {
        return type;
    }

    public float getPercent() {
        return percent;
    }

    public int getAmount() {
        return amount;
    }

    public int getPoint() {
        return point;
    }

    public boolean isHide() {
        return hide;
    }

    public void setClubId(long clubId) {
        this.clubId = clubId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }
}
