package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Karam on 8/12/2018.
 */

public class GroupsProducts {
    private long id;
    private long productSku;
    private long groupId;

    public GroupsProducts() {
    }

    public GroupsProducts(long productSku, long groupId) {
        this.productSku = productSku;
        this.groupId = groupId;
    }

    public GroupsProducts(long id, long productSku, long groupId) {
        this.id = id;
        this.productSku = productSku;
        this.groupId = groupId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductSku() {
        return productSku;
    }

    public void setProductSku(long productSku) {
        this.productSku = productSku;
    }


    @Override
    public String toString() {
        return "GroupsProducts{" +
                "id=" + id +
                ", productSku=" + productSku +
                ", groupId=" + groupId +
                '}';
    }
}
