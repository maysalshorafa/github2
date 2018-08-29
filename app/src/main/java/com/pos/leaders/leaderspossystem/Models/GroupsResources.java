package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Karam on 8/12/2018.
 */

public class GroupsResources {
    private long id;
    private long resourceId;
    private long groupId;

    public GroupsResources() {
    }

    public GroupsResources(long resourceId, long groupId) {
        this.resourceId = resourceId;
        this.groupId = groupId;
    }

    public GroupsResources(long id, long resourceId, long groupId) {
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

    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
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
