package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by KARAM on 31/05/2018.
 */

public class CompositeId {
    private int deviceID;
    private int id;

    public CompositeId(int deviceID, int id) {
        this.deviceID = deviceID;
        this.id = id;
    }

    public CompositeId() {
    }

    public int getDeviceID() {
        return deviceID;
    }

    public int getId() {
        return id;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public void setId(int id) {
        this.id = id;
    }
}
