package com.pos.leaders.leaderspossystem.Models;

import java.util.Date;
import java.util.List;

/**
 * Created by KARAM on 18/10/2016.
 */

public class Department {
    private int id;
    private String name;
    private Date CreatingDate;
    private int byUser;
    private boolean hide;

    private List<Product> products;

    // region Constructor

    public Department(int id, String name, Date creatingDate, int byUser, boolean hide) {
        this.id = id;
        this.name = name;
        CreatingDate = creatingDate;
        this.byUser = byUser;
        this.hide = hide;
    }

    public Department(Department d) {
        new Department(d.getId(), d.getName(), d.getCreatingDate(), d.getByUser(), d.isHide());
    }

	//endregion

	// region Setters

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public void setName(String name) {
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

    public Date getCreatingDate() {
        return CreatingDate;
    }

    public int getByUser() {
        return byUser;
    }

    public boolean isHide() {
        return hide;
    }

	public List<Product> getProducts() {
		return products;
	}

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

