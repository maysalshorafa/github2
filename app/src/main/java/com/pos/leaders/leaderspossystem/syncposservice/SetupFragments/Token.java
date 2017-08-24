package com.pos.leaders.leaderspossystem.syncposservice.SetupFragments;

import android.content.Context;
import android.content.SharedPreferences;

import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;

public class Token{
    public static String getToken(Context context) throws InterruptedException {
        SharedPreferences prefs = context.getSharedPreferences(OnlineSetup.BO_CORE_ACCESS_TOKEN, Context.MODE_PRIVATE);
        if(prefs.contains(MessageKey.Token)){
            String token = prefs.getString(MessageKey.Token, null);
            return token;
        }else{
            //there is no token file
            AccessToken accessToken = new AccessToken();
            accessToken.execute(context);
            Thread.sleep(2000);
            getToken(context);
        }
        return null;
    }
}
