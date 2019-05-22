package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 5/19/2019.
 */

public class BoScheduleWorkers {
    private long scheduleWorkersId;
    private long userId;
    private long date;
    private long startTime;
    private long exitTime;

    // Constructors
    public BoScheduleWorkers(long scheduleWorkersId, long userId, long date, long startTime) {
        this.scheduleWorkersId = scheduleWorkersId;
        this.userId = userId;
        this.date = date;
        this.startTime = startTime;
    }



    public BoScheduleWorkers(long userId,long date,long startTime){
        this.userId=userId;
        this.date=date;
        this.startTime=startTime;
    }

    public BoScheduleWorkers(ScheduleWorkers scheduleWorkers){
        this(scheduleWorkers.getScheduleWorkersId(),scheduleWorkers.getUserId(),scheduleWorkers.getDate(),scheduleWorkers.getStartTime());
    }

    public BoScheduleWorkers() {
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


    @Override
    public String toString() {
        return "ScheduleWorkers{" +
                "date=" + date +
                ", accountingId=" + scheduleWorkersId +
                ", userId=" + userId +
                ", startTime=" + startTime +
                '}';
    }
}
