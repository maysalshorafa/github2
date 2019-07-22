package com.pos.leaders.leaderspossystem.Models;

import java.util.HashMap;

/**
 * Created by Win8.1 on 7/22/2019.
 */

public class BoInventory {
    private long id;
    private String name;
    private long inventoryId;
    private HashMap productsIdWithQuantityList;
    private int branchId;
    private int hide;


    public BoInventory() {
    }

    public BoInventory(long id, String name, long inventoryId, HashMap productsIdWithQuantityList, int branchId, int hide) {
        this.id = id;
        this.name = name;
        this.inventoryId = inventoryId;
        this.productsIdWithQuantityList = productsIdWithQuantityList;
        this.branchId = branchId;
        this.hide = hide;
    }
    public BoInventory(String name, long inventoryId, HashMap productsIdWithQuantityList, int branchId, int hide) {
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

    public HashMap getProductsIdWithQuantityList() {
        return productsIdWithQuantityList;
    }

    public void setProductsIdWithQuantityList(HashMap productsIdWithQuantityList) {
        this.productsIdWithQuantityList = productsIdWithQuantityList;
    }

    @Override
    public String toString() {
        return "BoInventory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", inventoryId=" + inventoryId +
                ", productsIdWithQuantityList=" + productsIdWithQuantityList +
                ", branchId=" + branchId +
                ", hide=" + hide +
                '}';
    }
}
