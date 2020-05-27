package com.pos.leaders.leaderspossystem.SettingsTab;

import android.os.AsyncTask;

import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Win8.1 on 3/26/2018.
 */

public class StartBoVersionSettingConnection extends AsyncTask<String,Void,String> {

    private MessageTransmit messageTransmit;

    public StartBoVersionSettingConnection() {
        messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
    }

    @Override
    protected String doInBackground(String... params) {
        return null;
    }



    @Override
    protected void onPreExecute() {

    }

    @Override
    public void onPostExecute(String s) {

        final String token = SESSION.token;

        //null response
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                updateBOSettings(token);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

            }
        }.execute();


        //endregion
    }
    public static void updateBOSettings(String token) {
        JSONObject jsonObject = null;
        MessageTransmit messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
        try {

            String res = messageTransmit.authGet(ApiURL.Version, token);

            try {
                if(res!=null && !res.isEmpty() ){
                jsonObject = new JSONObject(res);
                BOPOSVersionSettings.jsonObject = jsonObject;}

            }
            catch (JSONException e){
                JSONArray jsonArray = new JSONArray(res);
                jsonObject = jsonArray.getJSONObject(0);
                BOPOSVersionSettings.jsonObject = jsonObject;

            }


         }catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


}
