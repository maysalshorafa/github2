package com.credix.pinpad.jni;

public class PinPadAPI {

    // On Linux use: "CredixPinPadDriver_jni" - no 'lib' prefix
    // On Windows use: "libCredixPinPadDriver_jni"
    static {
        System.loadLibrary("CredixPinPadDriver_jni");
    }

    // Native private method declaration
    private native int nativeOpen(String address, String username, String password);
    private native int nativeClose();
    private native int nativeSendRequest(int[] session, String type, String data);
    private native int nativeIsResponseReady(int session, int msTimeout);
    private native void nativeGetResponse(int session, byte[] response, int responseLength);


    // Public interface
    public Integer open(String address, String username, String password) {
        return nativeOpen(address, username, password);
    }

    public Integer close() {
        return nativeClose();
    }

    public Integer sendRequest(PinPadSession session, String type, String data) {
        int[] nativeSession = new int[1];
        int ret = nativeSendRequest(nativeSession, type, data);
        session.set(nativeSession[0]);
        return ret;
    }

    public Integer isResponseReady(PinPadSession session, Integer msTimeout) {
        return nativeIsResponseReady(session.get(), msTimeout);
    }


    public void getResponse(PinPadSession session, PinPadResponse response, Integer responseLength) {
        byte[] nativeResponse = new byte[responseLength];
        nativeGetResponse(session.get(), nativeResponse, responseLength);
        response.set(new String(nativeResponse,0,responseLength-1)); // last byte is null termination
    }

}
