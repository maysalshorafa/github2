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
    private double amount;
    private int point;
    private boolean hide=false;
    private int branchId;
    private double valueOfPoint;

    public Club() {
    }

    public Club(long clubId, String name, String description, int type, float percent, double amount, int point, boolean hide,int branchId,double valueOfPoint) {
        this.clubId = clubId;
        this.name=name;
        this.description=description;
        this.type=type;
        this.percent = percent;
        this.amount=amount;
        this.point=point;
        this.hide=hide;
        this.branchId=branchId;
        this.valueOfPoint=valueOfPoint;
    }
    public Club(long clubId, String name, String description, int type, float percent, double amount, int point,int branchId,double valueOfPoint) {
        this.clubId = clubId;
        this.name=name;
        this.description=description;
        this.type=type;
        this.percent = percent;
        this.amount=amount;
        this.point=point;
        this.branchId=branchId;
        this.valueOfPoint=valueOfPoint;
    }
    public Club(Club club) {
        this(club.getClubId(),club.getName(),club.getDescription(),club.getType(),club.getPercent(),club.getAmount(),club.getPoint(),club.getBranchId(),club.getValueOfPoint());
    }

    public double getValueOfPoint() {
        return valueOfPoint;
    }

    public void setValueOfPoint(double valueOfPoint) {
        this.valueOfPoint = valueOfPoint;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
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

    public double getAmount() {
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

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    @Override
    public String toString() {
        return "Club{" +
                "clubId=" + clubId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", percent=" + percent +
                ", amount=" + amount +
                ", point=" + point +
                ", hide=" + hide +
                ", branchId=" + branchId +
                ", valueOfPoint=" + valueOfPoint +
                '}';
    }
}
