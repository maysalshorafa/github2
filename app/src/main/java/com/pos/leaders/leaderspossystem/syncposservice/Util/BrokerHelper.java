package com.pos.leaders.leaderspossystem.syncposservice.Util;


import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.syncposservice.DBHelper.Broker;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.Model.BrokerMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by KARAM on 27/08/2017.
 */

public class BrokerHelper {

    public static void sendToBroker(String msgType, Object obj,Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(MessageKey.MessageType, msgType);
            ObjectMapper objectMapper = new ObjectMapper();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            objectMapper.setDateFormat(dateFormat);
            String jsonInString = objectMapper.writeValueAsString(obj);
            JSONObject data = new JSONObject(jsonInString);
            Log.d("offerBOBefore",data.toString());
            jsonObject.put(MessageKey.Data, data);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Broker broker = new Broker(context);
        broker.open();

        BrokerMessage brokerMessage = new BrokerMessage(jsonObject.toString());
        long res = broker.insertEntry(brokerMessage);
        broker.close();
    }
}
