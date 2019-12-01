package com.pos.leaders.leaderspossystem.Balance.Exception;

public class NoDevicesAvailableException extends Exception {
    public NoDevicesAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
    public NoDevicesAvailableException(String message) {
        super(message);
    }
}
