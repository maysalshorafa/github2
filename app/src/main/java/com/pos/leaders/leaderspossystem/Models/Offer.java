package com.pos.leaders.leaderspossystem.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by KARAM on 23/10/2016.
 */

public class Offer {

	//region Attribute
	private int id;
	private String name;
	private Date startDate;
	private Date endDate;
	private Date creatingDate;
	private int clubId;
	private int status;

	//region Attribute Objects
		private List<Product> products;
		private User user;
		private OfferRule rule;
	//endregion

	//endregion

	//region Constructors



	public Offer(int id, String name, Date startDate, Date endDate, Date creatingDate, int status, int clubId) {
		this.id=id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.creatingDate = creatingDate;
		this.status=status;
		this.clubId = clubId;
	}

	public Offer(int id, String name, Date startDate, Date endDate, Date creatingDate, List<Product> products, User user, OfferRule rule){
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.creatingDate = creatingDate;

		this.products=products;
		this.user=user;
		this.rule=rule;
;
	}
	public Offer(int id, String name, Date startDate, Date endDate, Date creatingDate,boolean enable, int byUser, int ruleId,double x,double y,double z,double p,double a, double b, double c, double d, double e, double f, double g, double h) {
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.creatingDate = creatingDate;

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


	public int getClubId() {
		return clubId;
	}

	public int getStatus() {
		return status;
	}

	public List<Product> getProducts()
	{
		return  this.products;
	}

	public User getUser()
	{
		return user;
	}

	public OfferRule getRule(){
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


	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public void setRule(OfferRule rule) {
		this.rule = rule;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}



	public void setUser(User user) {
		this.user = user;
	}


	//endregion

}