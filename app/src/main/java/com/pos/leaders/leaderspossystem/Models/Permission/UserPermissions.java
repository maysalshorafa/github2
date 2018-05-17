package com.pos.leaders.leaderspossystem.Models.Permission;

import android.content.Context;
import android.view.View;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.leaders.leaderspossystem.AddUserActivity;
import com.pos.leaders.leaderspossystem.Models.User;

import java.util.List;

/**
 * Created by KARAM on 29/10/2016.
 */

public class UserPermissions {
	//region Attribute
	private long id;
	private long userId;
	private long permissionId;
	@JsonIgnore
	private User user;
	@JsonIgnore
	private List<Permissions> permissions;

	//endregion

	//region Constructors

	public UserPermissions() {
	}

	public UserPermissions(long id, long userId, long permissionId) {
		this.id = id;
		this.userId = userId;
		this.permissionId = permissionId;
	}
	public UserPermissions(long permissionId, long userId) {
		this.permissionId = permissionId;
		this.userId = userId;
	}

	public UserPermissions(Permissions permissions, User user) {
		this.permissionId = permissions.getId();
		this.userId=user.getId();
		this.user = user;
	}

	//endregion

	//region Getters
	public long getId() {
		return id;
	}

	public long getPermissionId() {
		return permissionId;
	}

	public List<Permissions> getPermissions() {
		return permissions;
	}

	public User getUser() {
		return user;
	}

	public long getUserId() {
		return userId;
	}

	//endregion

	//region Setters

	public void setPermissions(List<Permissions> permissions) {
		this.permissions = permissions;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public void setPermissionId(long permissionId) {
		this.permissionId = permissionId;
	}
	//endregion

	//region Methods

	@Override
	public String toString() {
		return "UserPermissions{" +
				"userId=" + userId +
				", user=" + user +
				", permissionId=" + permissionId +
				", permissions=" + permissions +
				'}';
	}

	//endregion
}
