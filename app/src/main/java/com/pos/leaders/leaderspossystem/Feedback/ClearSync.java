package com.pos.leaders.leaderspossystem.Feedback;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.pos.leaders.leaderspossystem.AccessToken;
import com.pos.leaders.leaderspossystem.SplashScreenActivity;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.syncposservice.DBHelper.Broker;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;
import com.pos.leaders.leaderspossystem.syncposservice.Model.BrokerMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static com.pos.leaders.leaderspossystem.SetupNewPOSOnlineActivity.BO_CORE_ACCESS_AUTH;
import static com.pos.leaders.leaderspossystem.SetupNewPOSOnlineActivity.BO_CORE_ACCESS_TOKEN;

public class ClearSync extends AsyncTask<Context, Void, String> {
    MessageTransmit messageTransmitV1;
    MessageTransmit messageTransmitFeedback;
    Context context;
    ProgressDialog progressDialog;

    public ClearSync(Context context){
        messageTransmitV1 = new MessageTransmit(SETTINGS.BO_SERVER_URL_V2);
        messageTransmitFeedback = new MessageTransmit(SETTINGS.FEEDBACK_SERVER);

        this.context = context;
        this.progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected String doInBackground(Context... contexts) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("companyName", SETTINGS.companyName);
            jsonObject.put("companyId", SETTINGS.companyID);
            jsonObject.put("deviceId", SESSION.POS_ID_NUMBER);


            Broker broker = new Broker(context);
            broker.open();

            List<BrokerMessage> brokerMessages = broker.getAllNotSyncedCommand();

            int bugsCounter = 0;
            for (BrokerMessage bm : brokerMessages) {
                JSONObject json = new JSONObject(jsonObject.toString());
                try {
                    if (ClearSyncTable.doSyncV1(bm, messageTransmitV1, SESSION.token,context)) {
                        broker.Synced(bm.getId());
                    } else {
                        broker.Synced(bm.getId());
                        //send bug
                        json.put("method", "SYNC_ERROR");
                        json.put("content", bm.toString());
                        messageTransmitFeedback.post("bugs", json.toString());
                        bugsCounter++;
                    }
                } catch (Exception e) {
                    bugsCounter++;
                    json.put("method", "FORMAT_ERROR");
                    json.put("content", bm.toString());
                    try {
                        messageTransmitFeedback.post("bugs", json.toString());
                        broker.Synced(bm.getId());
                    } catch (IOException e1) {
                        try {
                            messageTransmitFeedback.post("bugs", json.toString());
                            broker.Synced(bm.getId());
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }

            JSONObject notificationJson = new JSONObject();
            notificationJson.put("oldVersion", 1);
            notificationJson.put("newVersion", 2);
            notificationJson.put("companyName", SETTINGS.companyName);
            notificationJson.put("companyId", SETTINGS.companyID);
            notificationJson.put("deviceId", SESSION.POS_ID_NUMBER);
            notificationJson.put("haveBugsOnUpdate", (bugsCounter != 0));

            try {
                messageTransmitFeedback.post("notification", notificationJson.toString());
            } catch (IOException e) {
                try {
                    messageTransmitFeedback.post("notification", notificationJson.toString());
                } catch (IOException e1) {
                    try {
                        messageTransmitFeedback.post("notification", notificationJson.toString());
                    } catch (IOException e2) {

                    }
                }
            }
            broker.close();

        } catch (JSONException e) {

        }

        return "";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setTitle("Processing before update.");
        progressDialog.show();

        if(SETTINGS.companyName==null) {
            SplashScreenActivity.readSettings(context);
        }

        AccessToken accessToken = new AccessToken(context);
        accessToken.setMessageTransmit(new MessageTransmit(SETTINGS.BO_SERVER_URL_V2));
        accessToken.execute(context);

        //load pos id from shared file
        SharedPreferences sharedpreferences = context.getSharedPreferences(BO_CORE_ACCESS_AUTH, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(MessageKey.PosId)) {
            int posID = sharedpreferences.getInt(MessageKey.syncNumber, 1);
            SESSION.POS_ID_NUMBER = posID;
        }
        //load token from shared file
        sharedpreferences = context.getSharedPreferences(BO_CORE_ACCESS_TOKEN, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(MessageKey.Token)) {
            String token = sharedpreferences.getString(MessageKey.Token, null);
            SESSION.token = token;
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            progressDialog.cancel();
        } catch (IllegalArgumentException iae) {
            if(iae.getMessage()!=null) {
                Log.e("closing dialog", iae.getMessage(), iae);
            }
        }
    }
}
