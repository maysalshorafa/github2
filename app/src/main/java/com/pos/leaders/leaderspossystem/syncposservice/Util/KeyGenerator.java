package com.pos.leaders.leaderspossystem.syncposservice.Util;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Created by KARAM on 27/07/2017.
 */

public class KeyGenerator {
    public static String uniqueIdentifier(Context context){
        UUID uuid = null;
        String java_uuid = UUID.randomUUID().toString();
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        // Use the Android ID unless it's broken, in which case fallback on deviceId,
        // unless it's not available, then fallback on a random number which we store
        // to a prefs file

        try {
            if (!"9774d56d682e549c".equals(androidId)) {
                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
            } else {
                final String deviceId = ((TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE )).getDeviceId();
                uuid = deviceId!=null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return uuid.toString();
    }

    public static String uniqueIdentifier(){
        UUID uuid = null;
        String java_uuid = UUID.randomUUID().toString();
        return java_uuid;
    }
}
