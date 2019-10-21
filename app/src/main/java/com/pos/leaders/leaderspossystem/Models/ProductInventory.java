package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 7/21/2019.
 */

public class ProductInventory {
    private long productInventoryId;
    private long productId;
    private int qty;
    private  String operation;
    private long byEmployee;
    private int hide;
    private int branchId;
    private String name;
    private  double price;
    private  int tempCount;
    public long getProductInventoryId() {
        return productInventoryId;
    }

    public void setProductInventoryId(long productInventoryId) {
        this.productInventoryId = productInventoryId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public long getByEmployee() {
        return byEmployee;
    }

    public void setByEmployee(long byEmployee) {
        this.byEmployee = byEmployee;
    }

    public int getHide() {
        return hide;
    }

    public void setHide(int hide) {
        this.hide = hide;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTempCount() {
        return tempCount;
    }

    public void setTempCount(int tempCount) {
        this.tempCount = tempCount;
    }

    public ProductInventory() {
    }

    public ProductInventory(long productInventoryId, long productId, int qty, String operation, long byEmployee, int hide, int branchId, String name, double price) {
        this.productInventoryId = productInventoryId;
        this.productId = productId;
        this.qty = qty;
        this.operation = operation;
        this.byEmployee = byEmployee;
        this.hide = hide;
        this.branchId = branchId;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "ProductInventory{" +
                "productInventoryId=" + productInventoryId +
                ", productId=" + productId +
                ", qty=" + qty +
                ", operation='" + operation + '\'' +
                ", byEmployee=" + byEmployee +
                ", hide=" + hide +
                ", branchId=" + branchId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", tempCount=" + tempCount +
                '}';
    }
}
