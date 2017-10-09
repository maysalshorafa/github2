package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.SettingsDBAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;
import com.pos.leaders.leaderspossystem.syncposservice.MessagesCreator;
import com.pos.leaders.leaderspossystem.syncposservice.Service.SyncMessage;
import com.pos.leaders.leaderspossystem.syncposservice.SetupFragments.Token;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

public class SetupNewPOSOnlineActivity extends Activity {
    public final static String BO_CORE_ACCESS_AUTH = "BOCOREACCESSAUTH";
    public final static String BO_CORE_ACCESS_TOKEN = "BOCOREACCESSTOKEN";
    protected static String posPass = null;
    protected static String posPrefix = null;

    private EditText etKey;
    private Button btConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setup_new_posonline);
        etKey = (EditText) findViewById(R.id.setuponlinepos_etKey);
        btConnect = (Button) findViewById(R.id.setuponlinepos_btConnect);

        //region check internet connection
        if(!SyncMessage.isConnected(this)) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getString(R.string.internet_access));
            alertDialog.setMessage(getString(R.string.internet_not_connected));
            alertDialog.setIcon(R.drawable.ic_setting);
            alertDialog.setCancelable(false);

            alertDialog.setButton(getString(R.string.retry), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //restart the activity
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });

            alertDialog.show();
        }

        //endregion

        //region connect button
        btConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = etKey.getText().toString();
                if((!key.equals(""))&&(key.length()==6)){
                    String uuid = UUID.randomUUID().toString();
                    final ProgressDialog progressDialog = new ProgressDialog(SetupNewPOSOnlineActivity.this);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.show();

                    StartConnection startConnection = new StartConnection();
                    startConnection.execute(etKey.getText().toString(),uuid);
                    try {
                        Thread.sleep(4000);//wait to async task finish
                        startConnection.cancel(true);
                        if(startConnection.isCancelled()){
                            //the async task is finished
                            if(posPass!=null){
                                Log.i("init connection2", "result pass" + posPass);
                                //success
                                //write to shared pref
                                SharedPreferences sharedpreferences = SetupNewPOSOnlineActivity.this.getSharedPreferences(BO_CORE_ACCESS_AUTH, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(MessageKey.PosID, uuid);
                                editor.putString(MessageKey.PosPass, posPass);
                                editor.putString(MessageKey.PosId, posPrefix);
                                editor.apply();

                                SESSION.POS_ID_NUMBER = Integer.parseInt(posPrefix);

                                //call new activity //get access token

                                final String token=Token.getToken(SetupNewPOSOnlineActivity.this);

                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(Void... params) {
                                        updateSettings(token);
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        progressDialog.cancel();
                                        finish();
                                        super.onPostExecute(aVoid);
                                    }
                                }.execute();
                            }else{
                                //fail
                                Toast.makeText(SetupNewPOSOnlineActivity.this, "Try Again With True Key.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            //timed out
                            startConnection.cancel(true);//stop async task
                            Toast.makeText(SetupNewPOSOnlineActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progressDialog.cancel();
                }
            }


        });
        //endregion

    }

    private void updateSettings(String token) {
        MessageTransmit messageTransmit = new MessageTransmit("");
        try {
            String res=messageTransmit.authGet(ApiURL.CompanyCredentials, token);
            JSONObject jsonObject = new JSONObject(res);


            SettingsDBAdapter settingsDBAdapter=new SettingsDBAdapter(this);
            settingsDBAdapter.open();
            int i = settingsDBAdapter.updateEntry(jsonObject.getString(MessageKey.companyID),jsonObject.getString(MessageKey.companyName),SESSION.POS_ID_NUMBER+"",
                    (float) jsonObject.getDouble(MessageKey.tax),jsonObject.getString(MessageKey.returnNote),jsonObject.getInt(MessageKey.endOfReturnNote),
                    jsonObject.getString(MessageKey.CCUN),jsonObject.getString(MessageKey.CCPW));
            settingsDBAdapter.close();
            if(i==1){
                Util.isFirstLaunch(this, true);
                //finish();
            }
            else{
                Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_SHORT).show();
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

}

class StartConnection extends AsyncTask<String,Void,String> {
    private MessageTransmit messageTransmit;

    StartConnection(){
        messageTransmit = new MessageTransmit("");
    }
    @Override
    protected String doInBackground(String... args) {//args{key,uuid}
        String key = args[0];
        String uniqueID = args[1];
        String initRes= "";
        String posPass=null,posPrefix=null;
        try {
            initRes = messageTransmit.post(ApiURL.InitConnection, MessagesCreator.initConnection(key, uniqueID));
            JSONObject jsonObject = new JSONObject(initRes);
            posPass = jsonObject.getString(MessageKey.PosPass);
            posPrefix = jsonObject.getString(MessageKey.PosId);

            Log.i("init connection1", "result pass" + posPass);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        SetupNewPOSOnlineActivity.posPass = posPass;
        SetupNewPOSOnlineActivity.posPrefix = posPrefix;
        return posPass;
    }
}


