package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule;

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
	private Date startDate;
	private Date endDate;
	private Date creatingDate;
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

	public Offer(long id, String name, Date startDate, Date endDate, Date creatingDate, int status, long byUser,String ruleName,long ruleID) {
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

    public Offer(String name, Date startDate, Date endDate, Date creatingDate, int status, long byUser,String ruleName,long ruleID) {
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
		if (d.after(startDate) && d.before(endDate)) {
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

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Date getCreatingDate() {
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

	public void setEndDate(Date endDate) {
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

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

    public void setId(long id) {
        this.id = id;
    }

    public void setCreatingDate(Date creatingDate) {
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