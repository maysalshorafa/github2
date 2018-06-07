package com.credix.pinpad.jni;

public class PinPadResponse {
    private String value;

    public PinPadResponse(String value){
        this.value = value;
    }

    public PinPadResponse() {
        this("");
    }

    public String get(){
        return this.value;
    }

    public void set(String value){
        this.value = value;
    }
}
