package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 5/2/2019.
 */

public class Inventory {
    private long id;
    private String name;
    private long inventoryId;
    private String productsIdWithQuantityList;
    private int branchId;
    private int hide;


    public Inventory() {
    }

    public Inventory(long id, String name, long inventoryId, String productsIdWithQuantityList, int branchId, int hide) {
        this.id = id;
        this.name = name;
        this.inventoryId = inventoryId;
        this.productsIdWithQuantityList = productsIdWithQuantityList;
        this.branchId = branchId;
        this.hide = hide;
    }
    public Inventory(String name, long inventoryId, String productsIdWithQuantityList, int branchId, int hide) {
        this.name = name;
        this.inventoryId = inventoryId;
        this.productsIdWithQuantityList = productsIdWithQuantityList;
        this.branchId = branchId;
        this.hide = hide;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getProductsIdWithQuantityList() {
        return productsIdWithQuantityList;
    }

    public void setProductsIdWithQuantityList(String productsIdWithQuantityList) {
        this.productsIdWithQuantityList = productsIdWithQuantityList;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getHide() {
        return hide;
    }

    public void setHide(int hide) {
        this.hide = hide;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", inventoryId=" + inventoryId +
                ", productsIdWithQuantityList='" + productsIdWithQuantityList + '\'' +
                ", branchId=" + branchId +
                ", hide=" + hide +
                '}';
    }
}
