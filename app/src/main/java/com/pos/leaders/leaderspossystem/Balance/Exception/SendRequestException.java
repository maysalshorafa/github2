package com.pos.leaders.leaderspossystem.Balance.Exception;

public class SendRequestException extends Exception {
    public SendRequestException(String message) {
        super(message);
    }
    public SendRequestException(String message,Throwable throwable) {
        super(message,throwable);
    }
}
