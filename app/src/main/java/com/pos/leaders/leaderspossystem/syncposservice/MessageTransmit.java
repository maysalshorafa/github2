package com.pos.leaders.leaderspossystem.syncposservice;


import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Authenticator;
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

    private String domainURL;
    private OkHttpClient client;

    public MessageTransmit(String domainURL){
        char c=domainURL.charAt(domainURL.length() - 1);
        if(c!='/')
            domainURL += "/";
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
        Request request = new Request.Builder().url(domainURL + url).post(body).addHeader(AUTHORIZATION, token).build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public String authPut(String url, String json,String token) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(domainURL + url).put(body).addHeader(AUTHORIZATION, token).build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public String authGet(String url,String token) throws IOException {
        Request request = new Request.Builder().url(domainURL + url).addHeader(AUTHORIZATION, token).build();
        Response response = client.newCall(request).execute();

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
}
