package com.pos.leaders.leaderspossystem.Models.Permission;

/**
 * Created by KARAM on 30/10/2016.
 * Modified by KARAM on 12/11/2017.
 */

public class Permissions {

	public static final String PERMISSIONS_ROLE_ROOT = "root";
	public static final String PERMISSIONS_ROLE_ADMIN = "Admin";
	public static final String PERMISSIONS_ROLE_POS = "pos";
	public static final String PERMISSIONS_ROLE_PRODUCT_MANAGEMENT = "Product_Management";
	public static final String PERMISSIONS_ROLE_DEPARTMENT_MANAGEMENT = "Department_Management";
	public static final String PERMISSIONS_ROLE_USER = "User";
	public static final String PERMISSIONS_ROLE_OFFERS = "Offers";
	public static final String PERMISSIONS_ROLE_PERMISSIONS = "Permissions";
	public static final String PERMISSIONS_ROLE_CANCELING_SALE = "CancelingSale";
	public static final String PERMISSIONS_ROLE_GENERAL_ITEM = "GeneralItem";
	public static final String PERMISSIONS_ROLE_REPORTS = "Reports";
	public static final String PERMISSIONS_ROLE_R = "";

	public static final String PERMISSIONS_MAIN_SCREEN = "main screen";
	public static final String PERMISSIONS_REPORT = "report";
	public static final String PERMISSIONS_PRODUCT = "product";
	public static final String PERMISSIONS_DEPARTMENT = "department";
	public static final String PERMISSIONS_USER = "user";
	public static final String PERMISSIONS_OFFER = "offer";
	public static final String PERMISSIONS_BACK_UP = "back up";
	public static final String PERMISSIONS_SETTINGS = "settings";
	public static final String PERMISSIONS_USER_CLUB = "user club";
	public static final String PERMISSIONS_SALES_MAN = "sales man";

	public static final String[] PERMISSIONS_NAMES = new String[]{PERMISSIONS_MAIN_SCREEN, PERMISSIONS_REPORT, PERMISSIONS_PRODUCT, PERMISSIONS_DEPARTMENT,
			PERMISSIONS_USER, PERMISSIONS_OFFER, PERMISSIONS_BACK_UP, PERMISSIONS_SETTINGS, PERMISSIONS_USER_CLUB, PERMISSIONS_SALES_MAN};

	static public final String[] PERMISSIONS_ROLE = new String[]{PERMISSIONS_ROLE_ROOT, PERMISSIONS_ROLE_ADMIN, PERMISSIONS_ROLE_POS,
			PERMISSIONS_ROLE_PRODUCT_MANAGEMENT, PERMISSIONS_ROLE_DEPARTMENT_MANAGEMENT, PERMISSIONS_ROLE_USER,
			PERMISSIONS_ROLE_OFFERS, PERMISSIONS_ROLE_PERMISSIONS, PERMISSIONS_ROLE_CANCELING_SALE,
			PERMISSIONS_ROLE_GENERAL_ITEM, PERMISSIONS_ROLE_REPORTS, PERMISSIONS_ROLE_R};

	//region Attribute

	private long id;
	private String name;
	private boolean checked = false;

	//endregion

	//region Constructors


	public Permissions() {
	}

	public Permissions(long id, String name) {
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

	public long getId() {
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

	public void setId(long id) {
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