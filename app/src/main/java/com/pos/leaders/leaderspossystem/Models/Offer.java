package com.pos.leaders.leaderspossystem.Models;

import com.pos.leaders.leaderspossystem.Models.Offers.Rule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by KARAM on 23/10/2016.
 * Updated by KARAM on 01/08/2017.
 */

public class Offer {
    public static final int Active = 1;
    public static final int Inactive = 0;


	//region Attribute
	private int id;
	private String name;
	private Date startDate;
	private Date endDate;
	private Date creatingDate;
	private int status;
	private int byUser;
    private String ruleName;
    private int ruleID;

    //region Attribute Objects
    private List<Product> products;
    private List<Group> clubs;
    private User user;
    private Rule rule;
	//endregion

	//endregion

	//region Constructors

    public Offer(){

    }

	public Offer(int id, String name, Date startDate, Date endDate, Date creatingDate, int status, int byUser,String ruleName,int ruleID) {
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

    public Offer(String name, Date startDate, Date endDate, Date creatingDate, int status, int byUser,String ruleName,int ruleID) {
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

	public int getId() {
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

    public int getByUser() {
        return byUser;
    }

    public String getRuleName() {
        return ruleName;
    }

    public int getRuleID() {
        return ruleID;
    }

    public List<Product> getProducts() {
        return products;
    }

	public List<Group> getClubs() {
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

    public void setByUser(int byUser) {
        this.byUser = byUser;
    }

    public void setProducts(List<Product> products) {
		this.products = products;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

    public void setId(int id) {
        this.id = id;
    }

    public void setCreatingDate(Date creatingDate) {
        this.creatingDate = creatingDate;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public void setRuleID(int ruleID) {
        this.ruleID = ruleID;
    }

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	public void setClubs(List<Group> clubs) {
		this.clubs = clubs;
	}

	//endregion

}