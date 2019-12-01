package com.pos.leaders.leaderspossystem.Balance;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;
import com.pos.leaders.leaderspossystem.Balance.Exception.CanNotOpenDeviceConnectionException;
import com.pos.leaders.leaderspossystem.Balance.Exception.NoDevicesAvailableException;
import com.pos.leaders.leaderspossystem.Balance.Exception.SendRequestException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeviceHelper {
    public static final int DATA_LENGTH = 6;
    private final Context context;
    private List<UsbSerialDriver> availableDrivers = null;
    private UsbManager usbManager;
    public UsbSerialPort port = null;


    private final static byte[] READ_DATA = new byte[]{0x57};

    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private SerialInputOutputManager mSerialIoManager;

    public DeviceHelper(Context context) {
        usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        this.context = context;
    }

    public List<UsbSerialDriver> getAvailableDrivers() {
        availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);
        return availableDrivers.isEmpty() ? null : availableDrivers;
    }

    public void openConnection(UsbSerialDriver driver) throws NoDevicesAvailableException, CanNotOpenDeviceConnectionException {
        // close old connection
        close();

        //refresh device list
        getAvailableDrivers();
        if (availableDrivers == null) {
            throw new NoDevicesAvailableException("There is no devices connected.");
        }

        UsbDeviceConnection connection = usbManager.openDevice(driver.getDevice());

        if(connection == null){
            throw new CanNotOpenDeviceConnectionException("Can not open device connection.");
        }

        port = driver.getPorts().get(0);

        try{
            port.open(connection);
            //110, 150, 300, 1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200, 230400, 460800, 921600
            port.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            port.setDTR(true);
            port.setRTS(true);
        } catch (IOException e) {
            throw new CanNotOpenDeviceConnectionException("Can not open device connection.", e);
        }
    }

    public void close(){
        if (port != null) {
            try {
                port.close();
            } catch (IOException e) {
            } finally {
                port = null;
            }
        }

        if(!mExecutor.isShutdown()||!mExecutor.isTerminated()){
            mExecutor.shutdown();
        }
    }

    public void sendReadRequest() throws SendRequestException {
        try {
            port.write(READ_DATA, 1000);
        } catch (IOException e) {
            throw new SendRequestException("Send request exception.", e);
        }
    }

    public void startListenerAtDevice(UsbSerialDriver driver) throws SendRequestException, CanNotOpenDeviceConnectionException, NoDevicesAvailableException {
        openConnection(driver);

       /* mSerialIoManager = new SerialInputOutputManager(port, listener);
        mExecutor.submit(mSerialIoManager);*/

        sendReadRequest();
    }
}
