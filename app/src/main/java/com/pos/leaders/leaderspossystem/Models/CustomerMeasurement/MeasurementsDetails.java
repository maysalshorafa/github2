package com.pos.leaders.leaderspossystem.Models.CustomerMeasurement;

/**
 * Created by Win8.1 on 12/14/2017.
 */

public class MeasurementsDetails {
    private long id;
    private long measurementId;
    private  long dynamicVarId;
    private String value;
    private MeasurementDynamicVariable measurementDynamicVariable;
    //Constructors
    public MeasurementsDetails() {
    }

    public MeasurementsDetails(long id, long measurementId, long dynamicVarId, String value) {
        this.id = id;
        this.measurementId = measurementId;
        this.dynamicVarId = dynamicVarId;
        this.value = value;
    }
    public MeasurementsDetails(MeasurementsDetails measurementsDetails) {
        this(measurementsDetails.getId(),measurementsDetails.getMeasurementId(),measurementsDetails.getDynamicVarId(),measurementsDetails.getValue());
    }
    // end

    //Getters

    public long getId() {
        return id;
    }

    public long getMeasurementId() {
        return measurementId;
    }

    public long getDynamicVarId() {
        return dynamicVarId;
    }

    public String getValue() {
        return value;
    }

    public MeasurementDynamicVariable getMeasurementDynamicVariable() {
        return measurementDynamicVariable;
    }
    // end
    //Setters

    public void setId(long id) {
        this.id = id;
    }

    public void setMeasurementId(long measurementId) {
        this.measurementId = measurementId;
    }

    public void setDynamicVarId(long dynamicVarId) {
        this.dynamicVarId = dynamicVarId;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setMeasurementDynamicVariable(MeasurementDynamicVariable measurementDynamicVariable) {
        this.measurementDynamicVariable = measurementDynamicVariable;
    }
    //end

    @Override
    public String toString() {
        return "MeasurementsDetails{" +
                "id=" + id +
                ", measurementId=" + measurementId +
                ", dynamicVarId=" + dynamicVarId +
                ", value='" + value + '\'' +
                ", measurementDynamicVariable=" + measurementDynamicVariable +
                '}';
    }
}
