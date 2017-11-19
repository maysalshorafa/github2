package com.pos.leaders.leaderspossystem.Printer;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;

import HPRTAndroidSDK.HPRTPrinterHelper;

/**
 * Created by KARAM on 16/11/2017.
 */

public class HPRT_TP805 {

    private static HPRTPrinterHelper HPRTPrinter;
    private UsbManager mUsbManager=null;
    private static UsbDevice device=null;
    private static PendingIntent mPermissionIntent=null;
    private static final String ACTION_USB_PERMISSION = "com.pos.leaders.leaderspossystem.Printer.HPRTSDKSample";


    public static boolean connect(Context context){
        mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        context.registerReceiver(mUsbReceiver, filter);
        HPRTPrinter = new HPRTPrinterHelper(context, "TP805");
        //USB not need call "iniPort"
        UsbManager mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

        boolean HavePrinter = false;
        while(deviceIterator.hasNext())
        {
            device = deviceIterator.next();
            int count = device.getInterfaceCount();
            for (int i = 0; i < count; i++)
            {
                UsbInterface intf = device.getInterface(i);
                if (intf.getInterfaceClass() == 7)
                {
                    HavePrinter=true;
                    mUsbManager.requestPermission(device, mPermissionIntent);
                }
            }
        }
        return HavePrinter;
    }


    private static BroadcastReceiver mUsbReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            try
            {
                String action = intent.getAction();
                //Toast.makeText(thisCon, "now:"+System.currentTimeMillis(), Toast.LENGTH_LONG).show();
                //HPRTPrinterHelper.WriteLog("1.txt", "fds");
                if (ACTION_USB_PERMISSION.equals(action))
                {
                    synchronized (this)
                    {
                        device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false))
                        {
                            if(HPRTPrinterHelper.PortOpen(device)!=0)
                            {
                                HPRTPrinter=null;
                                Log.i("HPRTSDKSample", "Connect Error!");
                                return;
                            }
                            else {
                                Log.i("HPRTSDKSample", "Connect Success!");
                                return;
                            }

                        }
                        else
                        {
                            return;
                        }
                    }
                }
                if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action))
                {
                    device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device != null)
                    {
                        HPRTPrinterHelper.PortClose();
                    }
                }
            }
            catch (Exception e)
            {
                Log.e("HPRTSDKSample", (new StringBuilder("Activity_Main --> mUsbReceiver ")).append(e.getMessage()).toString());
            }
        }
    };

}
