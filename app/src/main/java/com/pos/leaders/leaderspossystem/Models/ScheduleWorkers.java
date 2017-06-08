package com.pos.leaders.leaderspossystem.Models;

import java.util.Date;

/**
 * Created by KARAM on 19/10/2016.
 */

public class ScheduleWorkers {
    private int id;
    private int userId;
    private Date date;
    private Date startTime;
    private Date exitTime;

    // Constructors
    public ScheduleWorkers(int id, int userId, Date date, Date startTime, Date exitTime) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.startTime = startTime;
        this.exitTime = exitTime;
    }

    public ScheduleWorkers(int id, int userId, Date date, Date startTime) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.startTime = startTime;
    }

    public ScheduleWorkers(int userId,Date date,Date startTime){
        this.userId=userId;
        this.date=date;
        this.startTime=startTime;
    }

    public ScheduleWorkers(ScheduleWorkers scheduleWorkers){
        this(scheduleWorkers.getId(),scheduleWorkers.getUserId(),scheduleWorkers.getDate(),scheduleWorkers.getStartTime(),scheduleWorkers.getExitTime());
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public Date getDate() {
        return date;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getExitTime() {
        return exitTime;
    }


    // Setters

    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }

	@Override
	public String toString() {
		return "ScheduleWorkers{" +
				"date=" + date +
				", id=" + id +
				", userId=" + userId +
				", startTime=" + startTime +
				", exitTime=" + exitTime +
				'}';
	}
}
