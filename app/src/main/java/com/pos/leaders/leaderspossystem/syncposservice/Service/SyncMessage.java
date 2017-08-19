package com.pos.leaders.leaderspossystem.syncposservice.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import com.pos.leaders.leaderspossystem.syncposservice.DBHelper.Broker;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageResult;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;
import com.pos.leaders.leaderspossystem.syncposservice.MessagesCreator;
import com.pos.leaders.leaderspossystem.syncposservice.Model.BrokerMessage;
import com.pos.leaders.leaderspossystem.Tools.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class SyncMessage extends Service {

    /**
     *
     *
     *

     //start

     Intent intent = new Intent(MainActivity.this, SyncMessage.class);
     intent.putExtra(SyncMessage.API_DOMAIN_SYNC_MESSAGE, "https://jsonplaceholder.typicode.com");
     startService(intent);


     //stop

     Intent intent = new Intent(MainActivity.this, SyncMessage.class);
     stopService(intent);


     */

    public static final String API_DOMAIN_SYNC_MESSAGE = "API_DOMAIN_SYNC_MESSAGE";

    private static final String TAG = "SyncMessageService";
    private static String token = SESSION.token;

    private Broker broker;
    private MessageTransmit messageTransmit;

    private boolean isRunning = false;

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");
        broker = new Broker(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Service onStartCommand");
        messageTransmit = new MessageTransmit(intent.getStringExtra(API_DOMAIN_SYNC_MESSAGE));
        if(!isRunning) {
            Log.i(TAG, "Create New Thread");

            isRunning = true;
            //Creating new thread for my service
            //Always write your long running tasks in a separate thread, to avoid ANR
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //Your logic that service will perform will be placed here
                    //In this example we are just looping and waits for 1000 milliseconds in each loop.
                    while (isRunning) {
                        Log.i(TAG, "Service running");
                        broker.open();
                        List<BrokerMessage> bms = broker.getAllNotSyncedCommand();
                        for (BrokerMessage bm : bms) {
                            try {
                                if(!doSync(bm))
                                    break;
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        broker.close();

                        try {
                            getSync();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            stopSelf();
                        }

                        try {
                            Thread.sleep(2 * 60 * 1000);//2 min thread sleep
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //Stop service once it finishes its task
                    stopSelf();
                }
            }).start();
        }
        else{
            Log.i(TAG, "Thread is running");
        }
        return Service.START_STICKY;
    }

    private void getSync() throws IOException, JSONException {
        String res = messageTransmit.authGet(ApiURL.Sync, token);
        if(!res.equals(MessageResult.Invalid)){
            JSONObject jsonObject = new JSONObject(res);

            //todo: execute the command

            String resp = messageTransmit.authPost(ApiURL.Sync, MessagesCreator.acknowledge(jsonObject.getInt(MessageKey.Ak)), token);
            if (resp.equals(MessageResult.Invalid)) {
                //stop
                return;
            }else if(resp.equals(MessageResult.OK)){
                getSync();
            }
        } else if(res.equals(MessageResult.Invalid)) {
            //todo: there is no update is coming
        }
        else {
            //todo: error 401,404,403
        }
    }

    private boolean executeMessage(JSONObject jsonObject) throws JSONException {
        if(jsonObject.has(MessageKey.MessageType)) {
            String msgType = jsonObject.getString(MessageKey.MessageType);
            String msgData = jsonObject.getString(MessageKey.Data);

            switch (msgType) {

                case MessageType.ADD_CLUB:
                    break;
                case MessageType.UPDATE_CLUB:
                    break;
                case MessageType.DELETE_CLUB:
                    break;


                case MessageType.ADD_CUSTOMER:
                    break;
                case MessageType.UPDATE_CUSTOMER:
                    break;
                case MessageType.DELETE_CUSTOMER:
                    break;


                case MessageType.ADD_ORDER:
                    break;
                case MessageType.UPDATE_ORDER:
                    break;
                case MessageType.DELETE_ORDER:
                    break;


                case MessageType.ADD_PRODUCT:
                    break;
                case MessageType.UPDATE_PRODUCT:
                    break;
                case MessageType.DELETE_PRODUCT:
                    break;


                case MessageType.ADD_USER:
                    break;
                case MessageType.UPDATE_USER:
                    break;
                case MessageType.DELETE_USER:
                    break;
            }
        }else{
            //todo: does not have message type
        }

        return true;
    }


    private boolean doSync(BrokerMessage bm) throws IOException, JSONException {

        if(!isConnected(this))
            return false;

        JSONObject jsonObject = new JSONObject(bm.getCommand());
        String res = "";
        String msgType = jsonObject.getString(MessageKey.MessageType);

        switch (msgType){

            case MessageType.ADD_CLUB:
                res = messageTransmit.authPost(ApiURL.Club, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CLUB:
                res = messageTransmit.authPut(ApiURL.Club, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_CLUB:
                res = messageTransmit.authDelete(ApiURL.Club, jsonObject.getString(MessageKey.Data), token);
                break;


            case MessageType.ADD_CUSTOMER:
                res = messageTransmit.authPost(ApiURL.Customers, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CUSTOMER:
                res = messageTransmit.authPut(ApiURL.Customers, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_CUSTOMER:
                res = messageTransmit.authDelete(ApiURL.Customers, jsonObject.getString(MessageKey.Data), token);
                break;


            case MessageType.ADD_ORDER:
                res = messageTransmit.authPost(ApiURL.Order, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_ORDER:
                res = messageTransmit.authPut(ApiURL.Order, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_ORDER:
                res = messageTransmit.authDelete(ApiURL.Order, jsonObject.getString(MessageKey.Data), token);
                break;


            case MessageType.ADD_PRODUCT:
                res = messageTransmit.authPost(ApiURL.Product, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_PRODUCT:
                res = messageTransmit.authPut(ApiURL.Product, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_PRODUCT:
                res = messageTransmit.authDelete(ApiURL.Product, jsonObject.getString(MessageKey.Data), token);
                break;


            case MessageType.ADD_USER:
                res = messageTransmit.authPost(ApiURL.Users, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_USER:
                res = messageTransmit.authPut(ApiURL.Users, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_USER:
                res = messageTransmit.authDelete(ApiURL.Users, jsonObject.getString(MessageKey.Data), token);
                break;
        }

        try {
            if(res.toLowerCase().equals("false"))
                return false;
            else if(!res.toLowerCase().equals("true")){
                JSONObject object = new JSONObject(res);
            }
        } catch (JSONException e) {
            return false;
        }

        return true;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        Log.i(TAG, "Service onDestroy");
    }
}