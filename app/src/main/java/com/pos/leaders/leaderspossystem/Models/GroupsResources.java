package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Karam on 8/12/2018.
 */

public class GroupsResources {
    private long id;
    private String resourceId;
    private long groupId;

    public GroupsResources() {
    }

    public GroupsResources(String resourceId, long groupId) {
        this.resourceId = resourceId;
        this.groupId = groupId;
    }

    public GroupsResources(long id, String resourceId, long groupId) {
        this.id = id;
        this.resourceId = resourceId;
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

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }


    @Override
    public String toString() {
        return "GroupsResources{" +
                "id=" + id +
                ", resourceId=" + resourceId +
                ", groupId=" + groupId +
                '}';
    }
}
