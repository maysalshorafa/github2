package com.pos.leaders.leaderspossystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;
import com.pos.leaders.leaderspossystem.syncposservice.MessagesCreator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AccessToken extends AsyncTask<Context,Void,Boolean> {
    MessageTransmit messageTransmit;

    public AccessToken(){
        messageTransmit = new MessageTransmit("");
    }

    @Override
    protected Boolean doInBackground(Context... contexts) {
        Context context = contexts[0];
        SharedPreferences prefs = context.getSharedPreferences(SetupNewPOSOnlineActivity.BO_CORE_ACCESS_AUTH, Context.MODE_PRIVATE);
        String posID = prefs.getString(MessageKey.PosID, null);
        String posPass = prefs.getString(MessageKey.PosPass, null);

        if(posID!=null&&posPass!=null){
            //call api auth
            try {
                String authRes = messageTransmit.post(ApiURL.Authentication, MessagesCreator.authentication(posID,posPass));
                JSONObject jsonObject = new JSONObject(authRes);
                String token = jsonObject.getString(MessageKey.Token);

                SharedPreferences sharedpreferences = context.getSharedPreferences(SetupNewPOSOnlineActivity.BO_CORE_ACCESS_TOKEN, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(MessageKey.Token, token);
                editor.apply();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
