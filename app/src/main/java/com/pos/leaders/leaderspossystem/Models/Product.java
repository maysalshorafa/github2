package com.pos.leaders.leaderspossystem.Models;

import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.Date;
import java.util.Locale;

/**
 * Created by KARAM on 18/10/2016.
 */

public class Product {
    private int id;
    private String name;
    private String barCode;
    private String description;
    private double price;
    private double costPrice;
    private boolean withTax;
    private boolean weighable;
    private Date creatingDate;
    private boolean hide;
    private int departmentId;
    private int byUser;


    //Product with -1 value on id this is a general product


    // region Constructor
    public Product(int id, String name, String barCode, String description,
                   double price, double costPrice, boolean withTax, boolean weighable,
                   Date creatingDate, boolean hide, int departmentId, int byUser) {
        this.id = id;
        this.name = name;
        this.barCode = barCode;
        this.price = price;
        this.costPrice = costPrice;
        this.description = description;
        this.withTax = withTax;
        this.weighable = weighable;
        this.creatingDate = creatingDate;
        this.hide = hide;
        this.departmentId = departmentId;
        this.byUser = byUser;
    }
    public Product(int id, String name,double price, int byUser) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.byUser = byUser;
    }
    public Product(int id, String name,double price,String barCode,int departmentID,int byUser) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.barCode=barCode;
        this.departmentId=departmentID;
        this.withTax=true;
        this.byUser = byUser;
    }

    public Product(Product product){
        this(product.getId(),product.getName(),product.getBarCode(),product.getDescription(),
                product.getPrice(),product.getCostPrice(),product.isWithTax(),product.isWeighable(),
                product.getCreatingDate(),product.isHide(),product.getDepartmentId(),product.getByUser());
    }

    public Product(int id, String name) {
        this.id = id;
        this.name = name;
    }

    //endregion

    // region Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBarCode() {
        return barCode;
    }

    public double getPrice() {
        return price;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public String getDescription() {
        return description;
    }

    public boolean isWithTax() {
        return withTax;
    }

    public boolean isWeighable() {
        return weighable;
    }

    public Date getCreatingDate() {
        return creatingDate;
    }

    public boolean isHide() {
        return hide;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public int getByUser() {
        return byUser;
    }

    //endregion

	//region Setters

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setWeighable(boolean weighable) {
		this.weighable = weighable;
	}

	public void setWithTax(boolean withTax) {
		this.withTax = withTax;
	}

	//endregion

    @Override
    public String toString() {
        return "Product{" +
                "barCode='" + barCode + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", costPrice=" + costPrice +
                ", withTax=" + withTax +
                ", weighable=" + weighable +
                ", creatingDate=" + creatingDate +
                ", hide=" + hide +
                ", departmentId=" + departmentId +
                ", byUser=" + byUser +
                '}';
    }

    public String BKMVDATA(int rowNumber, String companyID) {
        String OP = "+";
        if(name.length()>49)
            name = name.substring(0, 49);
        name = "its cool";
        return "M100" + String.format(Util.locale, "%09d", rowNumber) + companyID + String.format(Util.locale, "%20s", barCode)
                + String.format(Util.locale, "%20s", barCode) + String.format(Util.locale, "%20s", id) + String.format("%50s", name)
                + Util.spaces(10) + Util.spaces(30) + String.format(new Locale("he"), "%20s", "Unit")
                + OP + Util.x9V99(0)
                + OP + Util.x9V99(0)
                + OP + Util.x9V99(0)
                + Util._8V99(0)
                + Util._8V99(0) + Util.spaces(50);
    }
}
