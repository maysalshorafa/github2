package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by KARAM on 18/10/2016.
 */

public class Category {
    private long categoryId;
    private String name;

    private Timestamp createdAt;

    private long byUser;
    private boolean hide;
    private boolean checked = false;
    @JsonIgnore
    private List<Product> products;

    // region Constructor



    public Category(long categoryId, String name, Timestamp createdAt, long byUser, boolean hide) {
        this.categoryId = categoryId;
        this.name = name;
        this.createdAt = createdAt;
        this.byUser = byUser;
        this.hide = hide;
    }

    public Category(String name, Timestamp createdAt, long byUser) {
        this.name = name;
        this.createdAt = createdAt;
        this.byUser = byUser;
        this.hide = false;
    }

    public Category(Category c) {
        new Category(c.getCategoryId(), c.getName(), c.getCreatedAt(), c.getByUser(), c.isHide());
    }

    public Category() {

    }

    public Category(int categoryId, String כללי, Timestamp createdAt, int byUser, int i) {
    }
    //endregion

	// region Setters

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public void setName(String name) {
		this.name = name;
	}

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
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

    public long getCategoryId() {
        return categoryId;
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
		return "Category{" +
				"byUser=" + byUser +
				", categoryId=" + categoryId +
				", name='" + name + '\'' +
				", createdAt=" + createdAt +
				", hide=" + hide +
				", products={" + products +
				"}}";
	}
}

