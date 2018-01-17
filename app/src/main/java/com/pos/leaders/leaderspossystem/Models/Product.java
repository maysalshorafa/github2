package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by KARAM on 18/10/2016.
 */

public class Product {
    private long id;
    private String name;
    private String barCode;
    private String description;
    private double price;
    private double costPrice;
    private boolean withTax;
    private boolean weighable;
    @JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date creatingDate;
    private boolean hide;
    private long departmentId;
    private long byUser;
    private int with_pos;
    private int with_point_system;
    @JsonIgnore
    private List<Integer> offersIDs=new ArrayList<Integer>();

    public Product(int i, String string) {

    }




    //Product with -1 value on id this is a general product


    public void setId(long id) {
        this.id = id;
    }

    public void setWith_pos(int with_pos) {
        this.with_pos = with_pos;
    }

    public void setWith_point_system(int with_point_system) {
        this.with_point_system = with_point_system;
    }

    public int getWith_pos() {
        return with_pos;

    }

    public int getWith_point_system() {
        return with_point_system;
    }

    // region Constructor
    public Product(long id, String name, String barCode, String description,
                   double price, double costPrice, boolean withTax, boolean weighable,
                   Date creatingDate, boolean hide, long departmentId, long byUser , int with_pos, int with_point_system) {
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
        this.with_pos=with_pos;
        this.with_point_system=with_point_system;
    }

    public Product(long id, String name, String barCode, String description,
                   double price, double costPrice, boolean withTax, boolean weighable,
                   Date creatingDate, long departmentId, long byUser , int with_pos, int with_point_system) {
        this.id = id;
        this.name = name;
        this.barCode = barCode;
        this.price = price;
        this.costPrice = costPrice;
        this.description = description;
        this.withTax = withTax;
        this.weighable = weighable;
        this.creatingDate = creatingDate;
        this.departmentId = departmentId;
        this.byUser = byUser;
        this.with_pos=with_pos;
        this.with_point_system=with_point_system;
    }

    public Product(long id, String name,double price, long byUser) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.byUser = byUser;
    }
    public Product(long id, String name,double price,String barCode,long departmentID,long byUser) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.barCode=barCode;
        this.departmentId=departmentID;
        this.withTax=true;
        this.byUser = byUser;
    }

    public Product(String name,double price,double costPrice,String barCode,long byUser) {
        this.name = name;
        this.price = price;
        this.costPrice = costPrice;
        this.barCode=barCode;
        this.withTax=true;
        this.byUser = byUser;
    }

    public Product(Product product){
        this(product.getId(),product.getName(),product.getBarCode(),product.getDescription(),
                product.getPrice(),product.getCostPrice(),product.isWithTax(),product.isWeighable(),
                product.getCreatingDate(),product.isHide(),product.getDepartmentId(),product.getByUser(),product.getWith_pos(),product.getWith_point_system());
    }

    public Product(){}

    //endregion

    // region Getters

    public long getId() {
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

    public long getDepartmentId() {
        return departmentId;
    }

    public long getByUser() {
        return byUser;
    }

    public List<Integer> getOffersIDs() {
        if(offersIDs.size()==0)
            return null;
        return offersIDs;
    }

    //endregion

	//region Setters

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}

	public void setDepartmentId(long departmentId) {
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

    public void setOffersIDs(List<Integer> offersIDs) {
        this.offersIDs = offersIDs;
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
                ", with_pos=" + with_pos +
                ", with_point_system=" + with_point_system +

                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Product product = (Product) o;

        if (!name.equals(product.name))
            return false;

        return barCode.equals(product.barCode);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + barCode.hashCode();
        return result;
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
