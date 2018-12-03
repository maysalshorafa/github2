package com.pos.leaders.leaderspossystem.syncposservice;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by KARAM on 26/07/2017.
 */

public class MessageTransmit {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String AUTHORIZATION = "Authorization";
    private static final String CONTENT_LENGTH = "content-length";

    private String domainURL;
    private OkHttpClient client;

    public MessageTransmit(String domainURL){
        if(domainURL.length()>0){
            char c = domainURL.charAt(domainURL.length() - 1);
            if(c!='/')
                domainURL += "/";}
        this.domainURL = domainURL;
        client = new OkHttpClient();
    }

    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(domainURL + url).post(body).build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public String authPost(String url, String json,String token) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder().url(domainURL + url).post(body).addHeader(AUTHORIZATION, token).addHeader(CONTENT_LENGTH,String.valueOf(body.contentLength()+body.contentType().toString().length())).build();
        Log.i("Message req", request.toString());
        Response response = client.newCall(request).execute();

        Log.w("Response Code", response.code()+"");
        return response.body().string();
    }

    /**   public String authPut(String url, String json,String token) throws IOException {
     RequestBody body = RequestBody.create(JSON, json);
     Request request = new Request.Builder().url(domainURL + url).put(body).addHeader(AUTHORIZATION, token).build();
     Response response = client.newCall(request).execute();

     return response.body().string();
     }**/
    public String authPut(String url, String json,String token, long id) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        JSONObject jsonObject;
        Request request;

        try {
            jsonObject = new JSONObject(json);
            request = new Request.Builder().url(domainURL + url+"/"+id).put(body).addHeader(AUTHORIZATION, token).build();
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error";
        }
        Response response = client.newCall(request).execute();

        return response.body().string();
    }
    public String authGet(String url,String token) throws IOException {
        Log.i("url", domainURL + url);
        Request request = new Request.Builder().url(domainURL + url).addHeader(AUTHORIZATION, token).build();
        Response response = client.newCall(request).execute();
        Log.i("response code", response.code() + "");
        return response.body().string();
    }

    public String authDelete(String url,String id,String token) throws IOException {
        Request request = new Request.Builder().url(domainURL + url + "/" + id).delete().addHeader(AUTHORIZATION, token).build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public String get(String url) throws IOException {
        Request request = new Request.Builder().url(domainURL + url).build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }
    public String getCurrency(String url,String token) throws IOException {
        Request request = new Request.Builder().url(domainURL + url).addHeader(AUTHORIZATION, token).build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }
    public String authPutInvoice(String url, String json,String token, String id) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        JSONObject jsonObject;
        Request request;

        try {
            jsonObject = new JSONObject(json);
            request = new Request.Builder().url(domainURL + url+"/"+id).put(body).addHeader(AUTHORIZATION, token).build();
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error";
        }
        Response response = client.newCall(request).execute();

        return response.body().string();
    }
    public String authUpdateGeneralLedger(String url,String id,String token,double amount) throws IOException {
        RequestBody body = RequestBody.create(JSON, "");
        Request request = new Request.Builder().url(domainURL + url + "/" + id+"/"+amount).put(body).addHeader(AUTHORIZATION, token).build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }


}

