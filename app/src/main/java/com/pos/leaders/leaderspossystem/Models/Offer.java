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
	private boolean enable;
	private int byUser;
	private int ruleId;

	private double x;
	private double y;
	private double z;
	private double p;

	private double a;
	private double b;
	private double c;
	private double d;
	private double e;
	private double f;
	private double g;
	private double h;

	//region Attribute Objects
		private List<Product> products;
		private User user;
		private OfferRule rule;
	//endregion

	//endregion

	//region Constructors

	public Offer(int id, String name, Date startDate, Date endDate, Date creatingDate,boolean enable, int byUser, int ruleId) {
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.creatingDate = creatingDate;
		this.enable=enable;
		this.byUser = byUser;
		this.ruleId = ruleId;
	}

	public Offer(String name, Date startDate, Date endDate, Date creatingDate,boolean enable, int byUser, int ruleId) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.creatingDate = creatingDate;
		this.enable=enable;
		this.byUser = byUser;
		this.ruleId = ruleId;
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

		this.byUser = user.getId();
		this.ruleId = rule.getId();
	}
	public Offer(int id, String name, Date startDate, Date endDate, Date creatingDate,boolean enable, int byUser, int ruleId,double x,double y,double z,double p,double a, double b, double c, double d, double e, double f, double g, double h) {
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.creatingDate = creatingDate;
		this.enable=enable;
		this.byUser = byUser;
		this.ruleId = ruleId;
		this.x=x;
		this.y=y;
		this.z=z;
		this.p=p;
		this.a=a;
		this.b=b;
		this.c=c;
		this.d=d;
		this.e=e;
		this.f=f;
		this.g=g;
		this.h=h;
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

	@Override
	public String toString() {
		return "Offer{" +
				"a=" + a +
				", id=" + id +
				", name='" + name + '\'' +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", creatingDate=" + creatingDate +
				", enable=" + enable +
				", byUser=" + byUser +
				", ruleId=" + ruleId +
				", x=" + x +
				", y=" + y +
				", z=" + z +
				", p=" + p +
				", b=" + b +
				", c=" + c +
				", d=" + d +
				", e=" + e +
				", f=" + f +
				", g=" + g +
				", h=" + h +
				", products={" + products +"}"+
				", user=" + user +
				", rule=" + rule +
				'}';
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

	public int getByUser() {
		return byUser;
	}

	public int getRuleId() {
		return ruleId;
	}

	public boolean isEnable() {
		return enable;
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

	public double getA() {
		return a;
	}

	public double getB() {
		return b;
	}

	public double getC() {
		return c;
	}

	public double getD() {
		return d;
	}

	public double getE() {
		return e;
	}

	public double getF() {
		return f;
	}

	public double getG() {
		return g;
	}

	public double getH() {
		return h;
	}

	public double getP() {
		return p;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	//endregion

	//region Setters

	public void setA(double a) {
		this.a = a;
	}

	public void setB(double b) {
		this.b = b;
	}

	public void setC(double c) {
		this.c = c;
	}

	public void setCreatingDate(Date creatingDate) {
		this.creatingDate = creatingDate;
	}

	public void setD(double d) {
		this.d = d;
	}

	public void setE(double e) {
		this.e = e;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setF(double f) {
		this.f = f;
	}

	public void setG(double g) {
		this.g = g;
	}

	public void setH(double h) {
		this.h = h;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setP(double p) {
		this.p = p;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public void setRule(OfferRule rule) {
		this.rule = rule;
	}

	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public void setUser(User user) {
		this.user = user;
	}


	//endregion

}