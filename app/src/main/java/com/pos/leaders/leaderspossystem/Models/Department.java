package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by KARAM on 18/10/2016.
 */

public class Department {
    private long departmentId;
    private String name;

    private Timestamp createdAt;

    private long byUser;
    private boolean hide;
    private boolean checked = false;
    @JsonIgnore
    private List<Product> products;

    // region Constructor



    public Department(long departmentId, String name, Timestamp createdAt, long byUser, boolean hide) {
        this.departmentId = departmentId;
        this.name = name;
        this.createdAt = createdAt;
        this.byUser = byUser;
        this.hide = hide;
    }

    public Department(String name, Timestamp createdAt, long byUser) {
        this.name = name;
        this.createdAt = createdAt;
        this.byUser = byUser;
        this.hide = false;
    }

    public Department(Department d) {
        new Department(d.getDepartmentId(), d.getName(), d.getCreatedAt(), d.getByUser(), d.isHide());
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

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setByUser(long byUser) {
        this.byUser = byUser;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    //endregion

	// region Getters

    public long getDepartmentId() {
        return departmentId;
    }

    public String getName() {
        return name;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
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
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    public boolean isChecked() {
        return checked;
    }
	//endregion

	@Override
	public String toString() {
		return "Department{" +
				"byUser=" + byUser +
				", depId=" + departmentId +
				", name='" + name + '\'' +
				", createdAt=" + createdAt +
				", hide=" + hide +
				", products={" + products +
				"}}";
	}
}

