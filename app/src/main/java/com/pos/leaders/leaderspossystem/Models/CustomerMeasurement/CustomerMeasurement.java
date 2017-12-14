package com.pos.leaders.leaderspossystem.Models.CustomerMeasurement;

import java.util.List;

/**
 * Created by Win8.1 on 12/14/2017.
 */

public class CustomerMeasurement {
    private long id;
    private long customerId;
    private long userId;
    private long visitDate;
    private  List<MeasurementsDetails>measurementsDetailsList;
    //Constructors

    public CustomerMeasurement() {
    }

    public CustomerMeasurement(long id, long customerId, long userId, long visitDate) {
        this.id = id;
        this.customerId = customerId;
        this.userId = userId;
        this.visitDate = visitDate;
    }
    //end
    //Getters
    public long getId() {
        return id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public long getUserId() {
        return userId;
    }

    public long getVisitDate() {
        return visitDate;
    }

    public List<MeasurementsDetails> getMeasurementsDetailsList() {
        return measurementsDetailsList;
    }
    //end
    //Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setVisitDate(long visitDate) {
        this.visitDate = visitDate;
    }

    public void setMeasurementsDetailsList(List<MeasurementsDetails> measurementsDetailsList) {
        this.measurementsDetailsList = measurementsDetailsList;
    }
    //end

    @Override
    public String toString() {
        return "CustomerMeasurement{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", userId=" + userId +
                ", visitDate=" + visitDate +
                ", measurementsDetailsList=" + measurementsDetailsList +
                '}';
    }

    //recordVisit method return List of MeasurementDetails
}
