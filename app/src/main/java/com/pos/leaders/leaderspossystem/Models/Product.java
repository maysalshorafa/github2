package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by KARAM on 18/10/2016.
 */

public class Product {
    private long productId;
    private String name;
    private String barCode;
    private String description;
    private double price;
    private double costPrice;
    private boolean withTax;
    private boolean weighable;
    private Timestamp createdAt;
    private boolean hide;
    private long categoryId;
    private long byEmployee;
    private int withPos;
    private int withPointSystem;

    private String sku;
    private ProductStatus status;
    private String displayName;
    private double regularPrice;
    private int stockQuantity;
    private boolean manageStock;
    private boolean inStock;


    @JsonIgnore
    private List<Integer> offersIDs = new ArrayList<Integer>();

    @JsonIgnore
    private List<Long> groupsId = null;

    public Product(int i, String string) {

    }


    //Product with -1 value on accountingId this is a general product


    public void setProductId(long productId) {
        this.productId = productId;
    }

    public void setWithPos(int withPos) {
        this.withPos = withPos;
    }

    public void setWithPointSystem(int withPointSystem) {
        this.withPointSystem = withPointSystem;
    }

    public int getWithPos() {
        return withPos;

    }

    public int getWithPointSystem() {
        return withPointSystem;
    }

    // region Constructor



    public Product(long productId, String name, String barCode, String description, double price, double costPrice, boolean withTax, boolean weighable, Timestamp createdAt, boolean hide, long categoryId, long byEmployee, int withPos, int withPointSystem, String sku, ProductStatus status, String displayName, double regularPrice, int stockQuantity, boolean manageStock, boolean inStock) {

        this.productId = productId;
        this.name = name;
        this.barCode = barCode;
        this.description = description;
        this.price = price;
        this.costPrice = costPrice;
        this.withTax = withTax;
        this.weighable = weighable;
        this.createdAt = createdAt;
        this.hide = hide;
        this.categoryId = categoryId;
        this.byEmployee = byEmployee;
        this.withPos = withPos;
        this.withPointSystem = withPointSystem;
        this.sku = sku;
        this.status = status;
        this.displayName = displayName;
        this.regularPrice = regularPrice;
        this.stockQuantity = stockQuantity;
        this.manageStock = manageStock;
        this.inStock = inStock;
    }


    public Product(long productId, String name, String barCode, String description, double price, double costPrice, boolean withTax, boolean weighable, Timestamp createdAt, long categoryId, long byEmployee, int withPos, int withPointSystem, String sku, ProductStatus status, String displayName, double regularPrice, int stockQuantity, boolean manageStock, boolean inStock) {

        this.productId = productId;
        this.name = name;
        this.barCode = barCode;
        this.description = description;
        this.price = price;
        this.costPrice = costPrice;
        this.withTax = withTax;
        this.weighable = weighable;
        this.createdAt = createdAt;
        this.categoryId = categoryId;
        this.byEmployee = byEmployee;
        this.withPos = withPos;
        this.withPointSystem = withPointSystem;
        this.sku = sku;
        this.status = status;
        this.displayName = displayName;
        this.regularPrice = regularPrice;
        this.stockQuantity = stockQuantity;
        this.manageStock = manageStock;
        this.inStock = inStock;
    }

    public Product(long productId, String name,String displayName, double price, long byEmployee) {
        this.productId = productId;
        this.name = name;
        this.displayName = displayName;
        this.price = price;
        this.byEmployee = byEmployee;
    }

    public Product(long productId, String name,String displayName, double price, String barCode,String sku, long categoryId, long byEmployee) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.barCode=barCode;
        this.categoryId =categoryId;
        this.withTax=true;
        this.barCode = barCode;
        this.withTax = true;
        this.byEmployee = byEmployee;
        this.displayName = displayName;
        this.sku = sku;
    }

    public Product(String name,String displayName, double price, double costPrice, String barCode,String sku, long byEmployee) {
        this.name = name;
        this.price = price;
        this.costPrice = costPrice;
        this.barCode = barCode;
        this.withTax = true;
        this.byEmployee = byEmployee;
        this.displayName = displayName;
        this.sku = sku;
    }

    public Product(long productId, String name, String displayName, double price, long byEmployee, String barCode,String sku) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.byEmployee = byEmployee;
        this.barCode = barCode;
        this.displayName = displayName;
        this.sku = sku;
    }

    public Product(Product product) {
        this(product.getProductId(), product.getName(), product.getBarCode(), product.getDescription(),
                product.getPrice(), product.getCostPrice(), product.isWithTax(), product.isWeighable(),
                product.getCreatedAt(), product.isHide(), product.getCategoryId(), product.getByEmployee(),
                product.getWithPos(), product.getWithPointSystem(), product.getSku(), product.getStatus(), product.getDisplayName(),
                product.getRegularPrice(), product.getStockQuantity(), product.isManageStock(), product.isInStock());
    }

    public Product() {
    }

    //endregion

    // region Getters


    public long getProductId() {
        return productId;
    }

    public String getDisplayName() {
        return displayName;
    }
    public String getName() {
        return name;
    }

    public String getSku() {
        return sku;
    }
    public String getBarCode() {
        return barCode;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public boolean isWithTax() {
        return withTax;
    }

    public boolean isWeighable() {
        return weighable;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public boolean isHide() {
        return hide;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public long getByEmployee() {
        return byEmployee;
    }



    public ProductStatus getStatus() {
        return status;
    }

    public double getRegularPrice() {
        return regularPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public boolean isManageStock() {
        return manageStock;
    }

    public boolean isInStock() {
        return inStock;
    }

    public List<Integer> getOffersIDs() {
        if (offersIDs.size() == 0)
            return null;
        return offersIDs;
    }

    public List<Long> getGroupsId() {
        return groupsId;
    }


    //endregion

    //region Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public void setWithTax(boolean withTax) {
        this.withTax = withTax;
    }

    public void setWeighable(boolean weighable) {
        this.weighable = weighable;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public void setByEmployee(long byEmployee) {
        this.byEmployee = byEmployee;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setRegularPrice(double regularPrice) {
        this.regularPrice = regularPrice;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setManageStock(boolean manageStock) {
        this.manageStock = manageStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public void setGroupsId(List<Long> groupsId) {
        this.groupsId = groupsId;
    }
    //endregion


    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", barCode='" + barCode + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", costPrice=" + costPrice +
                ", withTax=" + withTax +
                ", weighable=" + weighable +
                ", createdAt=" + createdAt +
                ", hide=" + hide +
                ", categoryId=" + categoryId +
                ", byEmployee=" + byEmployee +

                ", withPos=" + withPos +
                ", withPointSystem=" + withPointSystem +
                ", sku='" + sku + '\'' +
                ", status=" + status +
                ", displayName='" + displayName + '\'' +
                ", regularPrice=" + regularPrice +
                ", stockQuantity=" + stockQuantity +
                ", manageStock=" + manageStock +
                ", inStock=" + inStock +
                ", offersIDs=" + offersIDs +
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
        if (name.length() > 49)
            name = name.substring(0, 49);
        name = "its cool";
        return "M100" + String.format(Util.locale, "%09d", rowNumber) + companyID + String.format(Util.locale, "%20s", barCode)
                + String.format(Util.locale, "%20s", barCode) + String.format(Util.locale, "%20s", productId) + String.format("%50s", name)
                + Util.spaces(10) + Util.spaces(30) + String.format(new Locale("he"), "%20s", "Unit")
                + OP + Util.x9V99(0)
                + OP + Util.x9V99(0)
                + OP + Util.x9V99(0)
                + Util._8V99(0)
                + Util._8V99(0) + Util.spaces(50);
    }
}
