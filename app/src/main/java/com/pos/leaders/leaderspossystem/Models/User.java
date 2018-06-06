package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.leaders.leaderspossystem.Models.Permission.Permissions;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by KARAM on 16/10/2016.
 */

public class User {
    private long userId;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private Timestamp createdAt;
    private boolean hide;
    private String phoneNumber;
    private double present;
    private double hourlyWage;
    private String permissionsName;

    @JsonIgnore
    private List<Permissions> permissionsList;

    public void setUserId(long userId) {
        this.userId = userId;
    }



    public User(long userId, String userName, String password, String firstName, String lastName, Timestamp createdAt, boolean hide, String phoneNumber, double present, double hourlyWage ) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
        this.hide = hide;
        this.phoneNumber = phoneNumber;
        this.present = present;
        this.hourlyWage = hourlyWage;
    }
    public User(User user){
        this(user.getUserId(),user.getUserName(),user.getPassword(),user.getFirstName(),
                user.getLastName(),user.getCreatedAt(),user.isHide(),user.getPhoneNumber(),user.getPresent(),user.getHourlyWage());
    }
    public User(){}

    @JsonIgnore
    public String getFullName(){
        return this.firstName+" "+this.lastName;
    }
    public String getUserName(){
        return this.userName;
    }
    public double getPresent(){
        return this.present;
    }
    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public long getUserId() {
        return userId;
    }


    public String getLastName() {
        return lastName;
    }

    public boolean isHide() {
        return hide;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public double getHourlyWage() {
        return hourlyWage;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    public void setPassword(String password){
        this.password=password;
    }

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setHourlyWage(double hourlyWage) {
		this.hourlyWage = hourlyWage;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setPresent(double present) {
		this.present = present;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

    public List<Permissions> getPermissionsList() {
        return permissionsList;
    }

    public void setPermissionsList(List<Permissions> permissionsList) {
        this.permissionsList = permissionsList;
    }

    public String getPermissionsName() {
        return permissionsName;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public void setPermissionsName(String permissionsName) {
        this.permissionsName = permissionsName;
    }

    @Override
    public String toString() {
        return "User{" +
                "createdAt=" + createdAt +
                ", accountingId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", hide=" + hide +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", present=" + present +
                ", hourlyWage=" + hourlyWage +

                '}';
    }
}
