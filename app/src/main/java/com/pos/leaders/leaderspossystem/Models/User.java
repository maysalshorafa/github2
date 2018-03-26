package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pos.leaders.leaderspossystem.Models.Permission.Permissions;
import com.pos.leaders.leaderspossystem.Tools.CustomerDateAndTimeDeserialize;

import java.util.Date;
import java.util.List;

/**
 * Created by KARAM on 16/10/2016.
 */

public class User {
    private long id;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private long creatingDate;
    private boolean hide;
    private String phoneNumber;
    private double present;
    private double hourlyWage;
    private String permissionsName;

    @JsonIgnore
    private List<Permissions> permissionsList;

    public void setId(long id) {
        this.id = id;
    }



    public User(long id, String userName, String password, String firstName, String lastName, long creatingDate, boolean hide, String phoneNumber, double present, double hourlyWage ) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.creatingDate = creatingDate;
        this.hide = hide;
        this.phoneNumber = phoneNumber;
        this.present = present;
        this.hourlyWage = hourlyWage;
    }
    public User(User user){
        this(user.getId(),user.getUserName(),user.getPassword(),user.getFirstName(),
                user.getLastName(),user.getCreatingDate(),user.isHide(),user.getPhoneNumber(),user.getPresent(),user.getHourlyWage());
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

    public long getId() {
        return id;
    }


    public String getLastName() {
        return lastName;
    }

    public boolean isHide() {
        return hide;
    }

    public long getCreatingDate() {
        return creatingDate;
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

    public void setCreatingDate(long creatingDate) {
        this.creatingDate = creatingDate;
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
                "creatingDate=" + creatingDate +
                ", id=" + id +
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
