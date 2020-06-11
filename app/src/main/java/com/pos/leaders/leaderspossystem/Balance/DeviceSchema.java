package com.pos.leaders.leaderspossystem.Balance;

public class DeviceSchema {
    private String name;
    private int port;
    private String manufacture;

    public DeviceSchema() {
    }

    public DeviceSchema(String name, int port, String manufacture) {
        this.name = name;
        this.port = port;
        this.manufacture = manufacture;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public String getManufacture() {
        return manufacture;
    }

    // Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }
}
