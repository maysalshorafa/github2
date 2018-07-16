package com.pos.leaders.leaderspossystem.syncposservice;


import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

/**
 * Created by KARAM on 27/07/2017.
 */

public class MessagesCreator {
    /**
     *
     * @param key
     * @param uniqueIdentifier
     * @return text json of init connection command
     * @throws JSONException
     */
    public static String initConnection(String key,String uniqueIdentifier) throws JSONException {
        JSONObject object = new JSONObject();
        object.put(MessageKey.Key, key);
        object.put(MessageKey.PosID, uniqueIdentifier);
        return object.toString();
    }

    /**
     *
     * @param posID
     * @param PosPass
     * @param companyName
     * @return text json of authentication command
     * @throws JSONException
     */
    public static String authentication(String posID,String PosPass,String companyName) throws JSONException {
        JSONObject object = new JSONObject();
        object.put(MessageKey.PosID, posID);//posID
        object.put(MessageKey.PosPass, PosPass);//posPass
        object.put(MessageKey.companyName, companyName);//companyName
        return object.toString();
    }

    /**
     *
     * @param token
     * @return ping text message
     * @throws JSONException
     */
    public static String ping(String token) throws JSONException {
        JSONObject object = new JSONObject();
        object.put(MessageKey.Token, token);
        return object.toString();
    }

    public static String acknowledge(int ackNumber) throws JSONException {
        JSONObject object = new JSONObject();
        object.put(MessageKey.Acknowledge, ackNumber);
        return object.toString();
    }

    public static String ackTrackID(List<Integer> numbers) throws JSONException {
        JSONObject object = new JSONObject();
        StringBuilder strbul  = new StringBuilder();
        if(numbers.size()!=0){
            strbul.append("");
            Iterator<Integer> iter = numbers.iterator();
            while(iter.hasNext())
            {
                strbul.append(iter.next());
                if(iter.hasNext()){
                    strbul.append(",");
                }
            }
        }else {
            strbul.append(MessageKey.False);
        }
        object.put(MessageKey.Aks, strbul.toString());
        return object.toString();
    }

    public static String Club(){
        return "";}
}
