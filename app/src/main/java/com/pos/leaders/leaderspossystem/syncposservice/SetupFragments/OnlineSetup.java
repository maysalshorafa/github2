package com.pos.leaders.leaderspossystem.syncposservice.SetupFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;
import com.pos.leaders.leaderspossystem.syncposservice.MessagesCreator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;


/**
 * Created by KARAM on 10/08/2017.
 */

public class OnlineSetup extends Fragment {
    public final static String BO_CORE_ACCESS_AUTH = "BOCOREACCESSAUTH";
    public final static String BO_CORE_ACCESS_TOKEN = "BOCOREACCESSTOKEN";
    protected static String posPass = null;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.online_setup, container, false);
        Button verify = (Button) rootView.findViewById(R.id.online_setup_bt_verify);
        final EditText etKey = (EditText) rootView.findViewById(R.id.online_setup_et_key);
        final Context context = OnlineSetup.super.getContext();
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = etKey.getText().toString();
                if(!s.equals("")&&s.length()==6){
                    String uuid = UUID.randomUUID().toString();
                    ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.show();

                    StartConnection startConnection = new StartConnection();
                    startConnection.execute(etKey.getText().toString(),uuid);
                    try {
                        Thread.sleep(2000);//wait to async task finish
                        if(startConnection.isCancelled()){
                            //the async task is finished
                            if(posPass!=null){
                                //success
                                //write to shared pref
                                SharedPreferences sharedpreferences = context.getSharedPreferences(BO_CORE_ACCESS_AUTH, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(MessageKey.PosID, uuid);
                                editor.putString(MessageKey.PosPass, posPass);
                                editor.commit();

                                //call new activity //get access token

                            }else{
                                //fail
                                Toast.makeText(context, "Try Again With True Key.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            //timed out
                            startConnection.cancel(true);//stop async task
                            Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return rootView;
    }
}

class StartConnection extends AsyncTask<String,Void,String>{
    MessageTransmit messageTransmit;

    StartConnection(){
        messageTransmit = new MessageTransmit("");
    }

    @Override
    protected void onPostExecute(String aVoid) {
        OnlineSetup.posPass = aVoid;
        super.onPostExecute(aVoid);
    }

    @Override
    protected String doInBackground(String... args) {//args{key,uuid}
        String key = args[0];
        String uniqueID = args[1];
        String initRes= "";
        String posPass=null;
        try {
            initRes = messageTransmit.post(ApiURL.InitConnection, MessagesCreator.initConnection(key, uniqueID));
            JSONObject jsonObject = new JSONObject(initRes);
            posPass = jsonObject.getString(MessageKey.PosPass);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            return posPass;
        }
    }
}


class AccessToken extends AsyncTask<Context,Void,Boolean>{
    MessageTransmit messageTransmit;

    AccessToken(){
        messageTransmit = new MessageTransmit("");
    }

    @Override
    protected Boolean doInBackground(Context... contexts) {
        Context context = contexts[0];
        SharedPreferences prefs = context.getSharedPreferences(OnlineSetup.BO_CORE_ACCESS_AUTH, Context.MODE_PRIVATE);
        String posID = prefs.getString(MessageKey.PosID, null);
        String posPass = prefs.getString(MessageKey.PosPass, null);

        if(posID!=null&&posPass!=null){
            //call api auth
            try {
                String authRes = messageTransmit.post(ApiURL.Authentication, MessagesCreator.authentication(posID,posPass));
                JSONObject jsonObject = new JSONObject(authRes);
                String token = jsonObject.getString(MessageKey.Token);

                SharedPreferences sharedpreferences = context.getSharedPreferences(OnlineSetup.BO_CORE_ACCESS_TOKEN, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(MessageKey.Token, token);
                editor.commit();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}

