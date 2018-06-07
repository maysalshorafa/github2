package com.pos.leaders.leaderspossystem.Models.CustomerMeasurement;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Win8.1 on 12/14/2017.
 */

public class CustomerMeasurement {
    private long customerMeasurementId;
    private long customerId;
    private long userId;
    private Timestamp visitDate;
    private List<MeasurementsDetails>measurementsDetailsList;
    //Constructors

    public CustomerMeasurement() {
    }

    public CustomerMeasurement(long customerMeasurementId, long customerId, long userId, Timestamp visitDate) {
        this.customerMeasurementId = customerMeasurementId;
        this.customerId = customerId;
        this.userId = userId;
        this.visitDate = visitDate;
    }
    public CustomerMeasurement(CustomerMeasurement customerMeasurement) {
        this(customerMeasurement.getCustomerMeasurementId(),customerMeasurement.getCustomerId(),customerMeasurement.getUserId(),customerMeasurement.getVisitDate());
    }
    //end
    //Getters
    public long getCustomerMeasurementId() {
        return customerMeasurementId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public long getUserId() {
        return userId;
    }

    public Timestamp getVisitDate() {
        return visitDate;
    }

    public List<MeasurementsDetails> getMeasurementsDetailsList() {
        return measurementsDetailsList;
    }
    //end
    //Setters
    public void setCustomerMeasurementId(long customerMeasurementId) {
        this.customerMeasurementId = customerMeasurementId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setVisitDate(Timestamp visitDate) {
        this.visitDate = visitDate;
    }

    public void setMeasurementsDetailsList(List<MeasurementsDetails> measurementsDetailsList) {
        this.measurementsDetailsList = measurementsDetailsList;
    }
    //end

    @Override
    public String toString() {
        return "CustomerMeasurement{" +
                "customerMeasurementId=" + customerMeasurementId +
                ", customerId=" + customerId +
                ", userId=" + userId +
                ", visitDate=" + visitDate +
                ", measurementsDetailsList=" + measurementsDetailsList +
                '}';
    }

    //recordVisit method return List of MeasurementDetails
}
