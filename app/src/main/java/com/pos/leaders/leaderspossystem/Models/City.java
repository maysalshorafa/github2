package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 10/12/2017.
 */

public class City {
    private long id;

    public City(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public City() {
    }

    public long getId() {
        return id;
    }

    public City(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name ;

}
