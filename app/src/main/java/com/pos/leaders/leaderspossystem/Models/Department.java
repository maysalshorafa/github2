package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.List;

/**
 * Created by KARAM on 18/10/2016.
 */

public class Department {
    private long id;
    private String name;
    private Date CreatingDate;
    private long byUser;
    private boolean hide;

    @JsonIgnore
    private List<Product> products;

    // region Constructor

    public Department(long id, String name, Date creatingDate, long byUser, boolean hide) {
        this.id = id;
        this.name = name;
        CreatingDate = creatingDate;
        this.byUser = byUser;
        this.hide = hide;
    }

    public Department(Department d) {
        new Department(d.getId(), d.getName(), d.getCreatingDate(), d.getByUser(), d.isHide());
    }

    public Department() {
    }

    //endregion

	// region Setters

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public void setName(String name) {
		this.name = name;
	}

    public void setId(long id) {
        this.id = id;
    }

    public void setCreatingDate(Date creatingDate) {
        CreatingDate = creatingDate;
    }

    public void setByUser(long byUser) {
        this.byUser = byUser;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    //endregion

	// region Getters

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getCreatingDate() {
        return CreatingDate;
    }

    public long getByUser() {
        return byUser;
    }

    public boolean isHide() {
        return hide;
    }

	public List<Product> getProducts() {
		return products;
	}

	@JsonIgnore
	public int getProductCount(){
		return products.size();
	}

	//endregion

	@Override
	public String toString() {
		return "Department{" +
				"byUser=" + byUser +
				", id=" + id +
				", name='" + name + '\'' +
				", CreatingDate=" + CreatingDate +
				", hide=" + hide +
				", products={" + products +
				"}}";
	}
}

