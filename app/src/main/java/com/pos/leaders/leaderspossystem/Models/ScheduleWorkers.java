package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pos.leaders.leaderspossystem.Tools.CustomerDateAndTimeDeserialize;

import java.util.Date;

/**
 * Created by KARAM on 19/10/2016.
 */

public class ScheduleWorkers {
    private long id;
    private long userId;
    private long date;
    private long startTime;
    private long exitTime;

    // Constructors
    public ScheduleWorkers(long id, long userId, long date, long startTime, long exitTime) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.startTime = startTime;
        this.exitTime = exitTime;
    }

    public ScheduleWorkers(long id, long userId, long date, long startTime) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.startTime = startTime;
    }

    public ScheduleWorkers(long userId,long date,long startTime){
        this.userId=userId;
        this.date=date;
        this.startTime=startTime;
    }

    public ScheduleWorkers(ScheduleWorkers scheduleWorkers){
        this(scheduleWorkers.getId(),scheduleWorkers.getUserId(),scheduleWorkers.getDate(),scheduleWorkers.getStartTime(),scheduleWorkers.getExitTime());
    }

    public ScheduleWorkers() {
    }

    // Getters
    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public long getDate() {
        return date;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getExitTime() {
        return exitTime;
    }


    // Setters
    public void setExitTime(long exitTime) {
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
