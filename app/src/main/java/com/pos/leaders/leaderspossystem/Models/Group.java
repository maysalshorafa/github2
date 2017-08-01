package com.pos.leaders.leaderspossystem.Models;

import android.content.Context;

/**
 * Created by Win8.1 on 6/21/2017.
 */

public class Group {
    public static String name ;
    public static int id;

    public static  String description;
    public static  int type;
    public static  float parcent;
    public static  int amount;

    public Group(int id, String name, String description, int type, float parcent, int amount, int point) {
    this.id=id;
        this.name=name;
        this.description=description;
        this.type=type;
        this.parcent=parcent;
        this.amount=amount;
        this.point=point;
    }

    public Group(Context context) {
    }

    public Group(String name) {
        this.name = name;
    }


    public static void setname(String groupname) {
        Group.name = groupname;
    }

    public static void setId(int id) {
        Group.id = id;
    }

    public static void setDescription(String description) {
        Group.description = description;
    }

    public static void setType(int type) {
        Group.type = type;
    }

    public static void setParcent(float parcent) {
        Group.parcent = parcent;
    }

    public static void setAmount(int amount) {
        Group.amount = amount;
    }

    public static void setPoint(int point) {
        Group.point = point;
    }


    public static String getname() {
        return name;
    }

    public static int getId() {
        return id;
    }

    public static String getDescription() {
        return description;
    }

    public static int getType() {
        return type;
    }

    public static float getParcent() {
        return parcent;
    }

    public static int getAmount() {
        return amount;
    }

    public static int getPoint() {
        return point;
    }

    public static  int point;




}
