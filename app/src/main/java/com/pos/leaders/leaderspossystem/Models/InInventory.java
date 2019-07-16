package com.pos.leaders.leaderspossystem.Models;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Created by Win8.1 on 7/15/2019.
 */

public class InInventory {
    private Timestamp creatingDate;
    private Provider provider;
    private String type;
    private long byEmployee;
    private long inventoryId;
    private HashMap productsIdWithQuantityList;

    public InInventory(String type , Timestamp creatingDate, long inventoryId , HashMap productsIdWithQuantityList, long byEmployee) {
        this.inventoryId = inventoryId;
        this.productsIdWithQuantityList = productsIdWithQuantityList;
        this.creatingDate = creatingDate;
        this.type = type;
        this.byEmployee = byEmployee;
    }

    public InInventory() {
    }

    public Timestamp getCreatingDate() {
        return creatingDate;
    }

    public void setCreatingDate(Timestamp creatingDate) {
        this.creatingDate = creatingDate;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getByEmployee() {
        return byEmployee;
    }

    public void setByEmployee(long byEmployee) {
        this.byEmployee = byEmployee;
    }

    public long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public HashMap getProductsIdWithQuantityList() {
        return productsIdWithQuantityList;
    }

    public void setProductsIdWithQuantityList(HashMap productsIdWithQuantityList) {
        this.productsIdWithQuantityList = productsIdWithQuantityList;
    }

    @Override
    public String toString() {
        return "InInventory{" +
                "creatingDate=" + creatingDate +
                ", provider=" + provider +
                ", type='" + type + '\'' +
                ", byEmployee=" + byEmployee +
                ", inventoryId=" + inventoryId +
                ", productsIdWithQuantityList=" + productsIdWithQuantityList +
                '}';
    }
}
