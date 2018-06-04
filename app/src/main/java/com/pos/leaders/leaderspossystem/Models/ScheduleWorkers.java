package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by KARAM on 19/10/2016.
 */

public class ScheduleWorkers {
    private long scheduleWorkersId;
    private long userId;
    private long date;
    private long startTime;
    private long exitTime;

    // Constructors
    public ScheduleWorkers(long scheduleWorkersId, long userId, long date, long startTime, long exitTime) {
        this.scheduleWorkersId = scheduleWorkersId;
        this.userId = userId;
        this.date = date;
        this.startTime = startTime;
        this.exitTime = exitTime;
    }

    public ScheduleWorkers(long scheduleWorkersId, long userId, long date, long startTime) {
        this.scheduleWorkersId = scheduleWorkersId;
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
        this(scheduleWorkers.getScheduleWorkersId(),scheduleWorkers.getUserId(),scheduleWorkers.getDate(),scheduleWorkers.getStartTime(),scheduleWorkers.getExitTime());
    }

    public ScheduleWorkers() {
    }

    // Getters
    public long getScheduleWorkersId() {
        return scheduleWorkersId;
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
				", accountingId=" + scheduleWorkersId +
				", userId=" + userId +
				", startTime=" + startTime +
				", exitTime=" + exitTime +
				'}';
	}
}
