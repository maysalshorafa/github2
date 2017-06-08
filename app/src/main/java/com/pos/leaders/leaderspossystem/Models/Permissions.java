package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by KARAM on 30/10/2016.
 */

public class Permissions {

	public static final String PERMISSIONS_ROLE_ROOT = "root";
	public static final String PERMISSIONS_ROLE_ADMIN = "Admin";
	public static final String PERMISSIONS_ROLE_POS = "pos";
	public static final String PERMISSIONS_ROLE_PRODUCT_MANAGEMENT = "Product_Management";
	public static final String PERMISSIONS_ROLE_DEPARTMENT__MANAGEMENT = "Department_Management";
	public static final String PERMISSIONS_ROLE_USER = "Users";
	public static final String PERMISSIONS_ROLE_OFFERS = "Offers";
	public static final String PERMISSIONS_ROLE_PERMISSIONS = "Permissions";
	public static final String PERMISSIONS_ROLE_CANCELING_SALE = "CancelingSale";
	public static final String PERMISSIONS_ROLE_GENERAL_ITEM = "GeneralItem";
	public static final String PERMISSIONS_ROLE_REPORTS = "Reports";
	public static final String PERMISSIONS_ROLE_R = "";

	static public final String[] PERMISSIONS_ROLE = new String[]{PERMISSIONS_ROLE_ROOT, PERMISSIONS_ROLE_ADMIN, PERMISSIONS_ROLE_POS,
			PERMISSIONS_ROLE_PRODUCT_MANAGEMENT, PERMISSIONS_ROLE_DEPARTMENT__MANAGEMENT, PERMISSIONS_ROLE_USER,
			PERMISSIONS_ROLE_OFFERS, PERMISSIONS_ROLE_PERMISSIONS, PERMISSIONS_ROLE_CANCELING_SALE,
			PERMISSIONS_ROLE_GENERAL_ITEM, PERMISSIONS_ROLE_REPORTS, PERMISSIONS_ROLE_R};

	//region Attribute

	private int id;
	private String name;
	private boolean checked = false;

	//endregion

	//region Constructors

	public Permissions(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Permissions(String name) {
		this.name = name;
	}

	public Permissions(Permissions p) {
		this(p.getId(), p.getName());
	}

	//endregion

	//region Getters

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isChecked() {
		return checked;
	}

	//endregion

	//region Setters

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	//endregion

	//region Methods

	@Override
	public String toString() {
		return "Permissions{" +
				"id=" + id +
				", name='" + name + '\'' +
				", checked=" + checked +
				'}';
	}


	//endregion
}