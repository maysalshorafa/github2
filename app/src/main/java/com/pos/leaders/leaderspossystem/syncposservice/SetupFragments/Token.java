package com.pos.leaders.leaderspossystem.syncposservice.SetupFragments;

import android.content.Context;
import android.content.SharedPreferences;

import com.pos.leaders.leaderspossystem.AccessToken;
import com.pos.leaders.leaderspossystem.SetupNewPOSOnlineActivity;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;

public class Token{
    public static String getToken(Context context) throws InterruptedException {
        SharedPreferences prefs = context.getSharedPreferences(SetupNewPOSOnlineActivity.BO_CORE_ACCESS_TOKEN, Context.MODE_PRIVATE);
        if(prefs.contains(MessageKey.Token)){
            String token = prefs.getString(MessageKey.Token, null);
            return token;
        }else{
            //there is no token file
            AccessToken accessToken = new AccessToken();
            accessToken.execute(context);
            getToken(context);
        }
        return getToken(context);
    }
    public static String readToken(Context context){
        SharedPreferences prefs = context.getSharedPreferences(SetupNewPOSOnlineActivity.BO_CORE_ACCESS_TOKEN, Context.MODE_PRIVATE);
        String token = null;
        if(prefs.contains(MessageKey.Token))
            token = prefs.getString(MessageKey.Token, null);
        return token;
    }
}
