package com.pos.leaders.leaderspossystem.syncposservice;


import android.util.Log;

import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
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
    private static  OkHttpClient.Builder defaultHttpClient;
    private String domainURL;
    private String domainURLQuery;
    private OkHttpClient client;
    OkHttpClient eagerClient;

    public MessageTransmit(){}
    public MessageTransmit(String domainURL){
        Log.d("MessageTransmit1",domainURL);
        if(domainURL.length()>0){
            char c = domainURL.charAt(domainURL.length() - 1);
            if(c!='/')
                domainURL += "/";}
        else {
            domainURL= SETTINGS.BO_SERVER_URL;
            char c = domainURL.charAt(domainURL.length() - 1);
            if(c!='/')
                domainURL += "/";
        }
        Log.d("MessageTransmit2",domainURL);
        this.domainURL = domainURL;
        Log.d("MessageTransmit3", this.domainURL);
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }
    public MessageTransmit(String domainURLQuery, final String from,  final String to){
        if(domainURLQuery.length()>0){
            char c = domainURLQuery.charAt(domainURLQuery.length() - 1);
            if(c!='/')
                domainURLQuery += "/";}
        this.domainURLQuery=domainURLQuery;
        defaultHttpClient =
                new OkHttpClient.Builder()
        .addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("fromDate", from)
                        .addQueryParameter("toDate", to)
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        eagerClient = defaultHttpClient
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

    }

    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(domainURL + url).post(body).build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public String authPost(String url, String json,String token) throws IOException {
        if(token==null){
            token= SESSION.token;
        }
        try {
            RequestBody body = RequestBody.create(JSON, json);

            Request request = new Request.Builder().url(domainURL + url).post(body).addHeader(AUTHORIZATION, token).addHeader(CONTENT_LENGTH,String.valueOf(body.contentLength()+body.contentType().toString().length())).build();
            Log.d("Message req", request.toString());
            Response response = client.newCall(request).execute();
            Log.d("Response Code", response.code()+"");
            return response.body().string();
        }catch (Exception e){
            Log.d("Toooken",token.toString());
        }
       return "";
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
            Log.d("domainURL",domainURL);
            Log.d("url",url);
            request = new Request.Builder().url(domainURL + url+"/"+id).put(body).addHeader(AUTHORIZATION, token).build();
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error";
        }
        Response response = client.newCall(request).execute();

        return response.body().string();
    }
    public String authGet(String url,String token) throws IOException {
        Log.d("authGEt","authGetFun");
        Response response=null;
        if (url!=null){
            if (domainURL!=null){
                Request request = new Request.Builder().url(domainURL + url).addHeader(AUTHORIZATION, token).build();
                if (request!=null){
                    response = client.newCall(request).execute();}
                else {
                    Log.d("requestNull","null");
                }
                Log.i("response code", response.code() + "");
                Log.d("response Body",request.body()+"request");}
            else {
                Log.d("domainUrlNull","domainIsNull");
            }
        }
        else {
            Log.d("nullUrlAuthGet","null");}
        Log.d("finishauthGet","finishAth");
        return response.body().string();
    }

    public String authDelete(String url,String id,String token) throws IOException {
        Request request = new Request.Builder().url(domainURL + url + "/" + id).delete().addHeader(AUTHORIZATION, token).build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }
    public String getALLSalesProduct(String url,String token) throws IOException {
        Request request = new Request.Builder().url(domainURLQuery + url).addHeader(AUTHORIZATION, token).build();
        Response response =eagerClient.newCall(request).execute();
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

