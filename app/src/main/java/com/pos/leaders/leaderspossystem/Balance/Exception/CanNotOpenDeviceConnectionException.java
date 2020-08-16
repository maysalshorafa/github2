package com.pos.leaders.leaderspossystem.Balance.Exception;

public class CanNotOpenDeviceConnectionException extends Exception {
    public CanNotOpenDeviceConnectionException(String message) {
        super(message);
    }
    public CanNotOpenDeviceConnectionException(String message,Throwable throwable) {
        super(message,throwable);
    }
}
