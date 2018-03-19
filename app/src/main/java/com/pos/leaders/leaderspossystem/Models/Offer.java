package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule;
import com.pos.leaders.leaderspossystem.Tools.CustomerDateAndTimeDeserialize;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by KARAM on 23/10/2016.
 * Updated by KARAM on 01/08/2017.
 */

public class Offer {
	@JsonIgnore
    public static final int Active = 1;
	@JsonIgnore
    public static final int Inactive = 0;


	//region Attribute
	private long id;
	private String name;
	private long startDate;
	private long endDate;
	private long creatingDate;
	private int status;
	private long byUser;
    private String ruleName;
    private long ruleID;

    //region Attribute Objects
	@JsonIgnore
    private List<Product> products;
	@JsonIgnore
    private List<Club> clubs;
	@JsonIgnore
    private User user;
	@JsonIgnore
    private Rule rule;
	//endregion

	//endregion

	//region Constructors

    public Offer(){

    }

	public Offer(long id, String name, long startDate, long endDate, long creatingDate, int status, long byUser,String ruleName,long ruleID) {
		this.id=id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.creatingDate = creatingDate;
		this.status=status;
        this.byUser = byUser;
        this.ruleName = ruleName;
        this.ruleID = ruleID;
    }

    public Offer(String name, long startDate, long endDate, long creatingDate, int status, long byUser,String ruleName,long ruleID) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.creatingDate = creatingDate;
        this.status=status;
        this.byUser = byUser;
        this.ruleName = ruleName;
        this.ruleID = ruleID;
    }


	//endregion

	//region Method

	public boolean isValid() {
		Date d = new Date();
		if (d.after(new Date(startDate)) && d.before(new Date(endDate))) {
			return true;
		}
		return false;
	}

	public void addProduct(Product p){
        if(products.contains(p)){
            products.remove(p);
        }
        else {
		    if(products==null)
			    products=new ArrayList<Product>();
		    products.add(p);
        }
	}

	//endregion

	//region Getters

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getStartDate() {
		return startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public long getCreatingDate() {
		return creatingDate;
	}

	public int getStatus() {
		return status;
	}

    public long getByUser() {
        return byUser;
    }

    public String getRuleName() {
        return ruleName;
    }

    public long getRuleID() {
        return ruleID;
    }

    public List<Product> getProducts() {
        return products;
    }

	public List<Club> getClubs() {
		return clubs;
	}

	public Rule getRule() {
		return rule;
	}

	//endregion

	//region Setters

	public void setStatus(int status) {
		this.status = status;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public void setName(String name) {
		this.name = name;
	}

    public void setByUser(long byUser) {
        this.byUser = byUser;
    }

    public void setProducts(List<Product> products) {
		this.products = products;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

    public void setId(long id) {
        this.id = id;
    }

    public void setCreatingDate(long creatingDate) {
        this.creatingDate = creatingDate;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public void setRuleID(long ruleID) {
        this.ruleID = ruleID;
    }

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public void setClubs(List<Club> clubs) {
		this.clubs = clubs;
	}

	//endregion

}