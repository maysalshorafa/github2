package com.pos.leaders.leaderspossystem.Models;

import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by KARAM on 02/11/2016.
 */

public class Check {
	private long checkId;
	private long checkNum;
	private long bankNum;
	private long branchNum;
	private long accountNum;
	private double amount;
	private Timestamp createdAt;
	private boolean deleted;
	private long orderId;

	// region Constructors



	public Check(long checkId, long checkNum, long bankNum, long branchNum, long accountNum, double amount, Timestamp date, boolean deleted, long orderId) {
		this.accountNum = accountNum;
		this.amount = amount;
		this.bankNum = bankNum;
		this.branchNum = branchNum;
		this.checkNum = checkNum;
		this.createdAt = date;
		this.checkId = checkId;
		this.deleted = deleted;
		this.orderId = orderId;
	}

	public Check(long checkNum, long bankNum, long branchNum, long accountNum, double amount, Timestamp date, boolean deleted) {
		this.accountNum = accountNum;
		this.amount = amount;
		this.bankNum = bankNum;
		this.branchNum = branchNum;
		this.checkNum = checkNum;
		this.createdAt = date;
		this.deleted = deleted;
	}

	public Check(){

	}

	public Check(Check check){
		this(check.getCheckNum(),check.getBankNum(),check.getBranchNum(),check.getAccountNum(),check.getAmount(),check.getCreatedAt(),false);
	}

	//endregion

	//region Getters

	public long getAccountNum() {
		return accountNum;
	}

	public double getAmount() {
		return amount;
	}

	public long getBankNum() {
		return bankNum;
	}

	public long getBranchNum() {
		return branchNum;
	}

	public long getCheckNum() {
		return checkNum;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public long getCheckId() {
		return checkId;
	}

	public long getOrderId() {
		return orderId;
	}

	public boolean isDeleted() {
		return deleted;
	}

	//endregion

	//region Setters

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public void setAccountNum(long accountNum) {
		this.accountNum = accountNum;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setBankNum(long bankNum) {
		this.bankNum = bankNum;
	}

	public void setBranchNum(long branchNum) {
		this.branchNum = branchNum;
	}

	public void setCheckNum(long checkNum) {
		this.checkNum = checkNum;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    //endregion

	//region Methods

	@Override
	public String toString() {
		return "{"+"\"@type\":" + "\"Check\""+
				",\"accountNum\":" + accountNum +
				", \"checkId\":" + checkId +
				",\"checkNum\":" + checkNum +
				",\"bankNum\":" + bankNum +
				",\" branchNum\":" + branchNum +
				",\"amount\":" + amount +
				",\"createdAt\":"+"\""+ createdAt.getTime()+"\"" +
				",\"orderId\":" + orderId +
				"}";
	}


    public String BKMVDATA(int rowNumber, String companyID, Date date,long parentNumber) {

		String s = "320",OP="+",paymentType="2",cardType="0";

		if(amount<0) {
            s = "330";
            OP = "-";
        }

        return ("D120" + String.format(Util.locale, "%09d", rowNumber) + companyID + s + String.format(Util.locale, "%020d", orderId) + String.format(Util.locale, "%04d", orderId) +
                paymentType + String.format(Util.locale, "%010d", bankNum) + String.format(Util.locale, "%010d", branchNum) + String.format(Util.locale, "%015d", accountNum) +
                String.format(Util.locale, "%010d", checkNum) + DateConverter.getYYYYMMDD(new Date(String.valueOf(this.createdAt))) + OP + Util.x12V99(amount) + cardType + Util.spaces(20) +
                "0" + Util.spaces(7) + DateConverter.getYYYYMMDD(date) + String.format(Util.locale, "%07d", parentNumber) + Util.spaces(60));
    }

	//endregion

}
