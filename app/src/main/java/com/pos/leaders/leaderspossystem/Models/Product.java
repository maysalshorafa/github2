package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
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
    private String productCode;
    private String barCode;
    private String description;
    private double priceWithTax;
    private double costPrice;
    private boolean withTax=true;
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
    private ProductUnit unit;
    private double weight;
    private  double priceWithOutTax;

    private String currencyType= SETTINGS.currencyCode;

    private int branchId;
    private long offerId=0;
    private double lastCostPriceInventory;
    private boolean withSerialNumber;

    @JsonIgnore
    private List<Integer> offersIDs = new ArrayList<Integer>();

    @JsonIgnore
    private List<Long> groupsId = null;

    public Product(int i, String string) {

    }


    //Product with -1 value on accountingId this is a general product


    public double getPriceWithOutTax() {
        return priceWithOutTax;
    }

    public void setPriceWithOutTax(double priceWithOutTax) {
        this.priceWithOutTax = priceWithOutTax;
    }

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



    public Product(long productId, String productCode, String barCode, String description, double priceWithTax, double costPrice, boolean withTax, Timestamp createdAt, boolean hide, long categoryId, long byEmployee, int withPos, int withPointSystem, String sku, ProductStatus status, String displayName, double regularPrice, int stockQuantity, boolean manageStock, boolean inStock, ProductUnit unit, double weight, String currencyType, int branchId, long offerId, double lastCostPriceInventory, boolean withSerialNumber,double priceWithOutTax) {
        this.productId = productId;
        this.productCode = productCode;
        this.barCode = barCode;
        this.description = description;
        this.priceWithTax = priceWithTax;
        this.costPrice = costPrice;
        this.withTax = withTax;
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
        this.unit=unit;
        this.weight=weight;
        this.currencyType=currencyType;
        this.branchId=branchId;
        this.offerId=offerId;
        this.lastCostPriceInventory=lastCostPriceInventory;
        this.withSerialNumber=withSerialNumber;
        this.priceWithOutTax=priceWithOutTax;
    }

    public Product(long productId, String productCode, String barCode, String description, double priceWithTax, double costPrice, boolean withTax, Timestamp createdAt, long categoryId, long byEmployee, int withPos, int withPointSystem, String sku, ProductStatus status, String displayName, double regularPrice, int stockQuantity, boolean manageStock, boolean inStock, ProductUnit unit, double weight, String currencyType, int branchId, long offerId, double lastCostPriceInventory, boolean withSerialNumber,double priceWithOutTax) {

        this.productId = productId;
        this.productCode =productCode;
        this.barCode = barCode;
        this.description = description;
        this.priceWithTax = priceWithTax;
        this.costPrice = costPrice;
        this.withTax = withTax;
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
        this.unit=unit;
        this.weight=weight;
        this.currencyType=currencyType;
        this.branchId=branchId;
        this.offerId=offerId;
        this.lastCostPriceInventory=lastCostPriceInventory;
        this.withSerialNumber=withSerialNumber;
        this.priceWithOutTax=priceWithOutTax;
    }

    public Product(long productId, String productCode, String displayName, double priceWithTax, long byEmployee) {
        this.productId = productId;
        this.productCode = productCode;
        this.displayName = displayName;
        this.priceWithTax = priceWithTax;
        this.byEmployee = byEmployee;
    }

    public Product(long productId, String productCode, String displayName, double priceWithTax, String barCode, String sku, long categoryId, long byEmployee) {
        this.productId = productId;
        this.productCode = productCode;
        this.priceWithTax = priceWithTax;
        this.barCode=barCode;
        this.categoryId =categoryId;
        this.withTax=true;
        this.barCode = barCode;
        this.withTax = true;
        this.byEmployee = byEmployee;
        this.displayName = displayName;
        this.sku = sku;
    }

    public Product(String productCode, String displayName, double priceWithTax, double costPrice, String barCode, String sku, long byEmployee) {
        this.productCode = productCode;
        this.priceWithTax = priceWithTax;
        this.costPrice = costPrice;
        this.barCode = barCode;
        this.withTax = true;
        this.byEmployee = byEmployee;
        this.displayName = displayName;
        this.sku = sku;
    }

    public Product(long productId, String productCode, String displayName, double priceWithTax, long byEmployee, String barCode, String sku, ProductUnit productUnit) {
        this.productId = productId;
        this.productCode = productCode;
        this.priceWithTax = priceWithTax;
        this.byEmployee = byEmployee;
        this.barCode = barCode;
        this.displayName = displayName;
        this.sku = sku;
        this.unit=productUnit;
    }

    public Product(Product product) {
        this(product.getProductId(), product.getProductCode(), product.getBarCode(), product.getDescription(),
                product.getPriceWithTax(), product.getCostPrice(), product.isWithTax(),
                product.getCreatedAt(), product.isHide(), product.getCategoryId(), product.getByEmployee(),
                product.getWithPos(), product.getWithPointSystem(), product.getSku(), product.getStatus(), product.getDisplayName(),
                product.getRegularPrice(), product.getStockQuantity(), product.isManageStock(), product.isInStock(),product.getUnit(),product.getWeight(),product.getCurrencyType(),product.getBranchId(),product.getOfferId(),product.getLastCostPriceInventory(),product.isWithSerialNumber(), product.getPriceWithOutTax());

    }

    public Product() {
    }

    //endregion

    // region Getters


    public boolean isWithSerialNumber() {
        return withSerialNumber;
    }

    public void setWithSerialNumber(boolean withSerialNumber) {
        this.withSerialNumber = withSerialNumber;
    }

    public long getOfferId() {
        return offerId;
    }

    public void setOfferId(long offerId) {
        this.offerId = offerId;
    }

    public String getCurrencyType() {
        return currencyType;
    }
    public int getBranchId() {
        return branchId;
    }

    public long getProductId() {
        return productId;
    }

    public String getDisplayName() {
        return displayName;
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

    public double getPriceWithTax() {
        return priceWithTax;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public boolean isWithTax() {
        return withTax;
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

    public double getWeight() {
        return weight;
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



    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }
    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
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

    public void setPriceWithTax(double priceWithTax) {
        this.priceWithTax = priceWithTax;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public void setWithTax(boolean withTax) {
        this.withTax = withTax;
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

    public void setWeight(double weight) {
        this.weight = weight;
    }
    //endregion


    public double getLastCostPriceInventory() {
        return lastCostPriceInventory;
    }

    public void setLastCostPriceInventory(double lastCostPriceInventory) {
        this.lastCostPriceInventory = lastCostPriceInventory;
    }

    public ProductUnit getUnit() {
        return unit;
    }

    public void setUnit(ProductUnit unit) {
        this.unit = unit;
    }


    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productCode='" + productCode + '\'' +
                ", barCode='" + barCode + '\'' +
                ", description='" + description + '\'' +
                ", priceWithTax=" + priceWithTax +
                ", priceWithOutTax=" + priceWithOutTax +
                ", costPrice=" + costPrice +
                ", withTax=" + withTax +
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
                ", unit=" + unit +
                ", weight=" + weight +
                ", currencyType='" + currencyType + '\'' +
                ", branchId=" + branchId +
                ", offerId=" + offerId +
                ", lastCostPriceInventory=" + lastCostPriceInventory +
                ", withSerialNumber=" + withSerialNumber +
                ", offersIDs=" + offersIDs +
                ", groupsId=" + groupsId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Product product = (Product) o;
        if(productCode==null||product.productCode==null)
            return false;
        if (!productCode.equals(product.productCode))
            return false;

        return barCode.equals(product.barCode);

    }

    @Override
    public int hashCode() {
        int result=(productCode!=null)?productCode.hashCode():0;
        result = 31 * result + barCode.hashCode();
        return result;
    }

    public String BKMVDATA(int rowNumber, String companyID) {
        String OP = "+";
        if (productCode.length() > 49)
            productCode = productCode.substring(0, 49);
        productCode = "its cool";
        return "M100" + String.format(Util.locale, "%09d", rowNumber) + companyID + String.format(Util.locale, "%20s", barCode)
                + String.format(Util.locale, "%20s", barCode) + String.format(Util.locale, "%20s", productId) + String.format("%50s", productCode)
                + Util.spaces(10) + Util.spaces(30) + String.format(new Locale("he"), "%20s", "Unit")
                + OP + Util.x9V99(0)
                + OP + Util.x9V99(0)
                + OP + Util.x9V99(0)
                + Util._8V99(0)
                + Util._8V99(0) + Util.spaces(50);
    }
}
