package com.pos.leaders.leaderspossystem.Models;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Win8.1 on 2/17/2019.
 */

public class OfferCategory {
    private long offerCategoryId;
    private String name;
    private Timestamp createdAt;
    private List<String> productsIdList;

    private long byEmployee;
    private int branchId;
    private boolean hide ;

    public OfferCategory() {
    }

    public OfferCategory(long offerCategoryId, String name, Timestamp createdAt,  List<String> productsIdList, long byEmployee,int branchId,boolean hide) {
        this.offerCategoryId = offerCategoryId;
        this.name = name;
        this.createdAt = createdAt;
        this.productsIdList = productsIdList;
        this.byEmployee = byEmployee;
        this.branchId=branchId;
        this.hide=hide;
    }

    public long getOfferCategoryId() {
        return offerCategoryId;
    }

    public void setOfferCategoryId(long offerCategoryId) {
        this.offerCategoryId = offerCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }


    public long getByEmployee() {
        return byEmployee;
    }

    public void setByEmployee(long byEmployee) {
        this.byEmployee = byEmployee;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public List<String> getProductsIdList() {
        return productsIdList;
    }

    public void setProductsIdList(List<String> productsIdList) {
        this.productsIdList = productsIdList;
    }

    @Override
    public String toString() {
        return "OfferCategory{" +
                "offerCategoryId=" + offerCategoryId +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", productsIdList='" + productsIdList + '\'' +
                ", byEmployee=" + byEmployee +
                ", branchId=" + branchId +
                        ", hide=" + hide +
                '}';
    }
}
