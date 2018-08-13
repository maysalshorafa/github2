package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Created by KARAM on 8/12/2018.
 */

public class Group {
    private long id;
    private String name;

    @JsonIgnore
    private List<Product> productList = null;

    @JsonIgnore
    private List<Long> productsSku = null;

    public Group(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Group() {

    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public List<Long> getProductsSku() {
        return productsSku;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public void setProductsSku(List<Long> productsSku) {
        this.productsSku = productsSku;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", productList=" + productList +
                ", productsSku=" + productsSku +
                '}';
    }
}
