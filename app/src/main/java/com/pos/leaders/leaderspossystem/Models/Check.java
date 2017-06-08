package com.pos.leaders.leaderspossystem.Models;

import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.Date;

/**
 * Created by KARAM on 02/11/2016.
 */

public class Check {
	private int id;
	private int checkNum;
	private int bankNum;
	private int branchNum;
	private int accountNum;
	private double amount;
	private Date date;
	private boolean isDeleted;
	private int saleId;

	// region Constructors

	public Check(int id, int checkNum, int bankNum, int branchNum, int accountNum, double amount, Date date, boolean isDeleted, int saleId) {
		this.accountNum = accountNum;
		this.amount = amount;
		this.bankNum = bankNum;
		this.branchNum = branchNum;
		this.checkNum = checkNum;
		this.date = date;
		this.id = id;
		this.isDeleted = isDeleted;
		this.saleId = saleId;
	}

	public Check(int checkNum, int bankNum, int branchNum, int accountNum, double amount, Date date, boolean isDeleted) {
		this.accountNum = accountNum;
		this.amount = amount;
		this.bankNum = bankNum;
		this.branchNum = branchNum;
		this.checkNum = checkNum;
		this.date = date;
		this.isDeleted = isDeleted;
	}

	public Check(){

	}

	public Check(Check check){
		this(check.getCheckNum(),check.getBankNum(),check.getBranchNum(),check.getAccountNum(),check.getAmount(),check.getDate(),false);
	}

	//endregion

	//region Getters

	public int getAccountNum() {
		return accountNum;
	}

	public double getAmount() {
		return amount;
	}

	public int getBankNum() {
		return bankNum;
	}

	public int getBranchNum() {
		return branchNum;
	}

	public int getCheckNum() {
		return checkNum;
	}

	public Date getDate() {
		return date;
	}

	public int getId() {
		return id;
	}

	public int getSaleId() {
		return saleId;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	//endregion

	//region Setters

	public void setSaleId(int saleId) {
		this.saleId = saleId;
	}

	public void setAccountNum(int accountNum) {
		this.accountNum = accountNum;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setBankNum(int bankNum) {
		this.bankNum = bankNum;
	}

	public void setBranchNum(int branchNum) {
		this.branchNum = branchNum;
	}

	public void setCheckNum(int checkNum) {
		this.checkNum = checkNum;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	//endregion

	//region Methods

	@Override
	public String toString() {
		return "Check{" +
				"accountNum=" + accountNum +
				", id=" + id +
				", checkNum=" + checkNum +
				", bankNum=" + bankNum +
				", branchNum=" + branchNum +
				", amount=" + amount +
				", date=" + date +
				", saleId=" + saleId +
				'}';
	}

    public String BKMVDATA(int rowNumber, String companyID, Date date,int parentNumber) {

		String s = "320",OP="+",paymentType="2",cardType="0";

		if(amount<0) {
            s = "330";
            OP = "-";
        }

        return ("D120" + String.format(Util.locale, "%09d", rowNumber) + companyID + s + String.format(Util.locale, "%020d", saleId) + String.format(Util.locale, "%04d", saleId) +
                paymentType + String.format(Util.locale, "%010d", bankNum) + String.format(Util.locale, "%010d", branchNum) + String.format(Util.locale, "%015d", accountNum) +
                String.format(Util.locale, "%010d", checkNum) + DateConverter.getYYYYMMDD(this.date) + OP + Util.x12V99(amount) + cardType + Util.spaces(20) +
                "0" + Util.spaces(7) + DateConverter.getYYYYMMDD(date) + String.format(Util.locale, "%07d", parentNumber) + Util.spaces(60));
    }

	//endregion

}
