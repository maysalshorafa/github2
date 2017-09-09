package com.pos.leaders.leaderspossystem.Models;

import java.util.Date;

/**
 * Created by KARAM on 16/10/2016.
 */

public class User {
    private long id;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private Date creatingDate;
    private boolean hide;
    private String phoneNumber;
    private double present;
    private double hourlyWage;
    private String permissionsName;

    public void setId(long id) {
        this.id = id;
    }

    public void setPermissionsName(String permissionsName) {
        this.permissionsName = permissionsName;
    }

    public User(long id, String userName, String password, String firstName, String lastName, Date creatingDate, boolean hide, String phoneNumber, double present, double hourlyWage , String permissionsName) {
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
        this.permissionsName = permissionsName;
    }
    public User(User user){
        this(user.getId(),user.getUserName(),user.getPassword(),user.getFirstName(),
                user.getLastName(),user.getCreatingDate(),user.isHide(),user.getPhoneNumber(),user.getPresent(),user.getHourlyWage(),user.getPermtionName());
    }
    public User(){}

    public String getFullName(){
        return this.firstName+" "+this.lastName;
    }
    public String getUserName(){
        return this.userName;
    }
    public boolean getAvailableUser(){
        return !hide;
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

    public Date getCreatingDate() {
        return creatingDate;
    }

    public double getHourlyWage() {
        return hourlyWage;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getPermtionName() {
        return permissionsName;
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
                ", permissionsName=" + permissionsName + '\''+

                '}';
    }
}
