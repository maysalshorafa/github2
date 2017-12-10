package com.pos.leaders.leaderspossystem.Printer.SUNMI_T1;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;


import com.pos.leaders.leaderspossystem.Printer.BitmapInvoice;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

import com.sunmi.aidl.*;

public class AidlUtil {
    private static final String SERVICE＿PACKAGE = "woyou.aidlservice.jiuiv5";
    private static final String SERVICE＿ACTION = "woyou.aidlservice.jiuiv5.IWoyouService";

    private IWoyouService woyouService;

    private static AidlUtil mAidlUtil = new AidlUtil();
    private Context context;

    private AidlUtil() {
        MSCardService msCardService=new MSCardService(){

            @Override
            public IBinder asBinder() {
                return null;
            }

            @Override
            public int getMSReaderStatus() throws RemoteException {
                return 0;
            }

            @Override
            public void readRawMSCard(int timeOut, callback call) throws RemoteException {

            }

            @Override
            public boolean setTempFormat(String[] decorate, int[] order) throws RemoteException {
                return false;
            }
        };
    }

    public static AidlUtil getInstance() {
        return mAidlUtil;
    }

    /**
     * 连接服务
     *
     * @param context context
     */
    public void connectPrinterService(Context context) {
        this.context = context.getApplicationContext();
        Intent intent = new Intent();
        intent.setPackage(SERVICE＿PACKAGE);
        intent.setAction(SERVICE＿ACTION);
        context.getApplicationContext().startService(intent);
        context.getApplicationContext().bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    /**
     * 断开服务
     *
     * @param context context
     */
    public void disconnectPrinterService(Context context) {
        if (woyouService != null) {
            context.getApplicationContext().unbindService(connService);
            woyouService = null;
        }
    }

    public boolean isConnect() {
        return woyouService != null;
    }

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
        }
    };



    public List<String> getPrinterInfo() {
        if (woyouService == null) {
            Toast.makeText(context,"Printer Connect Error！", Toast.LENGTH_LONG).show();
            return null;
        }

        List<String> info = new ArrayList<>();
        try {
            info.add(woyouService.getPrinterSerialNo());
            info.add(woyouService.getPrinterModal());
            info.add(woyouService.getPrinterVersion());
            info.add(woyouService.getPrintedLength()+"");
            info.add("");
            //info.add(woyouService.getServiceVersion());
            PackageManager packageManager = context.getPackageManager();
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(SERVICE＿PACKAGE, 0);
                if(packageInfo != null){
                    info.add(packageInfo.versionName);
                    info.add(packageInfo.versionCode+"");
                }else{
                    info.add("");info.add("");
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return info;
    }

    public void initPrinter() {
        if (woyouService == null) {
            Toast.makeText(context,"Printer Connect Error！", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.printerInit(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }



    public void printBitmap(Bitmap bitmap) {
        if (woyouService == null) {
            Toast.makeText(context,"Printer Connect Error！", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            //woyouService.setAlignment(1, null);
            woyouService.printBitmap(bitmap, null);
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }



    public void feed(){
        if (woyouService == null) {
            Toast.makeText(context,"Printer Connect Error！", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public void cut(){
        if (woyouService == null) {
            Toast.makeText(context,"Printer Connect Error！", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            woyouService.cutPaper(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void openCashBox(){
        if (woyouService == null) {
            Toast.makeText(context,"Printer Connect Error！", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            woyouService.openDrawer(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void print3Line(){
        if (woyouService == null) {
            Toast.makeText(context,"Printer Connect Error！",Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }



    public int getPrintMode(){
        if(woyouService == null){
            Toast.makeText(context,"Printer Connect Error！", Toast.LENGTH_LONG).show();
            return -1;
        }

        int res;
        try {
            res =  woyouService.getPrinterMode();
        } catch (RemoteException e) {
            e.printStackTrace();
            res = -1;
        }
        return res;
    }
}
