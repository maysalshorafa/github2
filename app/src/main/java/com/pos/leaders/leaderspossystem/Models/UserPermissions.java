package com.pos.leaders.leaderspossystem.Models;

import java.util.List;

/**
 * Created by KARAM on 29/10/2016.
 */

public class UserPermissions {

	//region Attribute

	private int userId;
	private int permissionId;

	private User user;
	private List<Permissions> permissions;

	//endregion

	//region Constructors

	public UserPermissions(int permissionId, int userId) {
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

	public int getPermissionId() {
		return permissionId;
	}

	public List<Permissions> getPermissions() {
		return permissions;
	}

	public User getUser() {
		return user;
	}

	public int getUserId() {
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
