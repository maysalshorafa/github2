package com.pos.leaders.leaderspossystem.Models.Permission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.leaders.leaderspossystem.Models.Employee;

import java.util.List;

/**
 * Created by KARAM on 29/10/2016.
 */

public class EmployeesPermissions {
	//region Attribute
	private long employeePermissionId;
	private long employeeId;
	private long permissionId;
	@JsonIgnore
	private Employee employee;
	@JsonIgnore
	private List<Permissions> permissions;

	//endregion

	//region Constructors

	public EmployeesPermissions() {
	}

	public EmployeesPermissions(long employeePermissionId, long employeeId, long permissionId) {
		this.employeePermissionId = employeePermissionId;
		this.employeeId = employeeId;
		this.permissionId = permissionId;
	}
	public EmployeesPermissions(long permissionId, long employeeId) {
		this.permissionId = permissionId;
		this.employeeId = employeeId;
	}

	public EmployeesPermissions(Permissions permissions, Employee employee) {
		this.permissionId = permissions.getId();
		this.employeeId =employee.getEmployeeId();
		this.employee = employee;
	}

	//endregion

	//region Getters
	public long getEmployeePermissionId() {
		return employeePermissionId;
	}

	public long getPermissionId() {
		return permissionId;
	}

	public List<Permissions> getPermissions() {
		return permissions;
	}

	public Employee getUser() {
		return employee;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	//endregion

	//region Setters

	public void setPermissions(List<Permissions> permissions) {
		this.permissions = permissions;
	}

	public void setUser(Employee user) {
		this.employee = user;
	}

	public void setEmployeePermissionId(long employeePermissionId) {
		this.employeePermissionId = employeePermissionId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public void setPermissionId(long permissionId) {
		this.permissionId = permissionId;
	}
	//endregion

	//region Methods

	@Override
	public String toString() {
		return "EmployeesPermissions{" +
				"employeeId=" + employeeId +
				", employee=" + employee +
				", permissionId=" + permissionId +
				", permissions=" + permissions +
				'}';
	}

	//endregion
}
