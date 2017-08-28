package com.pos.leaders.leaderspossystem.syncposservice.Util;


import android.content.Context;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.syncposservice.DBHelper.Broker;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;
import com.pos.leaders.leaderspossystem.syncposservice.Model.BrokerMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KARAM on 27/08/2017.
 */

public class BrokerHelper {

    public static void sendToBroker(String msgType, Object obj,Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(MessageKey.MessageType, msgType);
            ObjectMapper objectMapper = new ObjectMapper();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            objectMapper.setDateFormat(dateFormat);
            String jsonInString = objectMapper.writeValueAsString(obj);
            JSONObject data = new JSONObject(jsonInString);
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
