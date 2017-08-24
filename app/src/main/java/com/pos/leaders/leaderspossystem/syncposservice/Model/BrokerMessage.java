package com.pos.leaders.leaderspossystem.syncposservice.Model;

import java.util.Date;

/**
 * Created by KARAM on 26/07/2017.
 */

public class BrokerMessage {

    private int id;
    private String command;
    private boolean isSynced;
    private Date createdDate;
    private int byUser;

    public BrokerMessage(){}

    public BrokerMessage(int id, String command, boolean isSynced, Date createdDate, int byUser) {
        this.id = id;
        this.command = command;
        this.isSynced = isSynced;
        this.createdDate = createdDate;
        this.byUser = byUser;
    }

    public BrokerMessage(int id, String command, int isSynced, long createdDate, int byUser) {
        this(id, command, (isSynced!=0), new Date(createdDate), byUser);
    }

    public BrokerMessage(int id, String command, boolean isSynced) {
        this.id = id;
        this.command = command;
        this.isSynced = isSynced;
    }
    public BrokerMessage(String command) {
        this.command = command;
        this.isSynced = false;
    }
    public BrokerMessage(int id, String command, int isSynced) {
        this(id, command, (isSynced!=0));
    }
    public int getId() {
        return id;
    }

    public String getCommand() {
        return command;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public int getByUser() {
        return byUser;
    }
}
