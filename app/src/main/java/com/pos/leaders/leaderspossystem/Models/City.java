package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 10/12/2017.
 */

public class City {
    private long cityId;

    public City(long cityId, String name) {
        this.cityId = cityId;
        this.name = name;
    }

    public City() {
    }

    public long getCityId() {
        return cityId;
    }

    public City(String name) {
        this.name = name;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name ;

}
