package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by KARAM on 19/10/2016.
 */

public class OfferRule {
    private int id;
    private String Name;

    // Constructor
    public OfferRule(int id, String name) {
        this.id = id;
        Name = name;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    @Override
    public String toString() {
        return "OfferRule{" +
                "id=" + id +
                ", Name='" + Name + '\'' +
                '}';
    }
}