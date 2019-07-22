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

    public ProductInventory() {
    }

    public ProductInventory(long productInventoryId, long productId, int qty, String operation, long byEmployee, int hide, int branchId) {
        this.productInventoryId = productInventoryId;
        this.productId = productId;
        this.qty = qty;
        this.operation = operation;
        this.byEmployee = byEmployee;
        this.hide = hide;
        this.branchId = branchId;
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
                '}';
    }

}
