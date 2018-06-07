package com.credix.pinpad.jni;

public class PinPadSession {
    private Integer value;

    public PinPadSession(Integer value){
        this.value = value;
    }

    public PinPadSession() {
        this(-1);
    }

    public Integer get(){
        return this.value;
    }

    public void set(Integer value){
        this.value = value;
    }
}
