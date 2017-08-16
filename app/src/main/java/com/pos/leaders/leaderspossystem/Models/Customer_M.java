package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 7/3/2017.
 */

public class Customer_M {

    private long id;
    private String name;
    private String birthday;
    private String gender;
    private String email;
    private String job;
    private String phoneNumber;
    private String address;
    private boolean hide;
    private String city;
    private int club;

    public Customer_M(long id, String name, String birthday, String gender, String email, String job, String phoneNumber, String address, boolean hide , String city, int club) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.email = email;
        this.job = job;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.hide = hide;
        this.city=city;
        this.club=club;
    }

    public Customer_M(Customer_M customer){
        this(customer.getId(),customer.getName(),customer.getBirthday(),customer.getGender(),
                customer.getEmail(),customer.getJob(),customer.getPhoneNumber(),customer.getAddress(),customer.getHide(),customer.getCity(),customer.getClub());
    }


    /**   public Customer_M(int id, String name, String birthday, String gender, String email, String job, String phoneNumber, String address, boolean hide) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.email = email;
        this.job = job;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.hide = hide;
    }**/

    public boolean isHide() {
     return hide;
 }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getClub() {
        return club;
    }

    public void setClub(int club) {
        this.club = club;
    }

    public long getId() {
     return id;
 }

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getJob() {
        return job;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public boolean getHide() {
        return hide;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustmerName(){
        return this.name;
    }

}
