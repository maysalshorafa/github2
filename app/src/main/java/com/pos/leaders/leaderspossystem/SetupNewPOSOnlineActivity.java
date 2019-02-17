package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;
import com.pos.leaders.leaderspossystem.syncposservice.MessagesCreator;
import com.pos.leaders.leaderspossystem.syncposservice.Service.SyncMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

public class SetupNewPOSOnlineActivity extends Activity {
    public final static String BO_CORE_ACCESS_AUTH = "BOCOREACCESSAUTH";
    public final static String BO_CORE_ACCESS_TOKEN = "BOCOREACCESSTOKEN";
    protected static String posPass = null;
    protected static String posPrefix = null;
    public static String companyName = null;
    public static Integer syncNumber = null;
    protected static String uuid = null;

    protected static boolean restart = false;
    protected static Context context = null;

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
        context = this;
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
                if((!key.equals(""))){
                    uuid = UUID.randomUUID().toString();
                    StartConnection startConnection = new StartConnection();
                    startConnection.execute(etKey.getText().toString(),uuid);
                }
            }


        });
        //endregion

    }

    @Override
    protected void onStop() {
        context = null;
        super.onStop();
    }

    @Override
    public void onResume()
    {
        Log.i("SetupNewPOSOnline", "onResume");
        super.onResume();
        if(restart) {
            restart = false;
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}

class StartConnection extends AsyncTask<String,Void,String> {
    private MessageTransmit messageTransmit;

    StartConnection() {
        messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
    }

    final ProgressDialog progressDialog = new ProgressDialog(SetupNewPOSOnlineActivity.context);
    final ProgressDialog progressDialog2 = new ProgressDialog(SetupNewPOSOnlineActivity.context);

    @Override
    protected void onPreExecute() {
        progressDialog.setTitle("Please Wait");
        progressDialog2.setTitle("Please Wait");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {//args{key,uuid}
        String key = args[0];
        String uniqueID = args[1];
        String initRes = "";
        String posPass = null, posPrefix = null, companyName = null;
        Integer syncNumber = null;

        try {
            initRes = messageTransmit.post(ApiURL.InitConnection, MessagesCreator.initConnection(key, uniqueID));
            Log.e("messageResult", initRes);
            JSONObject jsonObject = new JSONObject(initRes);

            if (jsonObject.getInt(MessageKey.status) == 200) {
                JSONObject responseBody = jsonObject.getJSONObject(MessageKey.responseBody);
                posPass = responseBody.getString(MessageKey.PosPass);
                posPrefix = responseBody.getString(MessageKey.PosID);
                companyName = responseBody.getString(MessageKey.companyName);
                syncNumber = responseBody.getInt(MessageKey.syncNumber);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        SetupNewPOSOnlineActivity.posPass = posPass;
        SetupNewPOSOnlineActivity.posPrefix = posPrefix;
        SetupNewPOSOnlineActivity.companyName = companyName;
        SetupNewPOSOnlineActivity.syncNumber = syncNumber;

        return posPass;
    }

    @Override
    protected void onPostExecute(String s) {

        //region rr
        //the async task is finished
        if (s != null) {
            //success
            //write to shared pref
            SharedPreferences sharedpreferences = SetupNewPOSOnlineActivity.context.getSharedPreferences(SetupNewPOSOnlineActivity.BO_CORE_ACCESS_AUTH, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(MessageKey.PosID, SetupNewPOSOnlineActivity.uuid);
            editor.putString(MessageKey.PosPass, SetupNewPOSOnlineActivity.posPass);
            editor.putString(MessageKey.PosId, SetupNewPOSOnlineActivity.posPrefix);
            editor.putString(MessageKey.companyName, SetupNewPOSOnlineActivity.companyName);
            editor.putInt(MessageKey.syncNumber, SetupNewPOSOnlineActivity.syncNumber);
            editor.apply();

            //SESSION.POS_ID_NUMBER = Integer.parseInt(SetupNewPOSOnlineActivity.posPrefix);
            SESSION.POS_ID_NUMBER = SetupNewPOSOnlineActivity.syncNumber;


            //call new activity //get access token
            AccessToken accessToken = new AccessToken(SetupNewPOSOnlineActivity.context);
            accessToken.execute(SetupNewPOSOnlineActivity.context);

            while (!accessToken.isCancelled()) {
                //Log.i("AccessToken Status", accessToken.getStatus().toString());
                // waiting until finished protected String[] doInBackground(Void... params)
            }

            final String token = SESSION.token;

            //null response
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog2.setTitle("Starting the system.");
                    progressDialog2.show();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    updateSettings(token);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    progressDialog2.cancel();
                    SetupNewPOSOnlineActivity.restart = true;
                    final Intent i = new Intent(SetupNewPOSOnlineActivity.context, SplashScreenActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    SetupNewPOSOnlineActivity.context.startActivity(i);

                    super.onPostExecute(aVoid);
                }
            }.execute();
        } else {
            //fail
            Toast.makeText(SetupNewPOSOnlineActivity.context, "Try Again With True Key.", Toast.LENGTH_SHORT).show();
        }
        progressDialog.cancel();
        super.onPostExecute(s);

        //endregion
    }


    private void updateSettings(String token) {

        MessageTransmit messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
        try {
            String res = messageTransmit.authGet(ApiURL.CompanyCredentials, token);
            Log.e("CCC", res);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(res);
            }
            catch (JSONException e){
                JSONArray jsonArray = new JSONArray(res);
                jsonObject = jsonArray.getJSONObject(0);
            }
            if(jsonObject.getString(MessageKey.status).equals("200")) {
                //03-11 16:18:47.482 20608-20721/com.pos.leaders.leaderspossystem E/CCC: {"logTag":"CompanyCredentials Resource","status":"200","responseType":"All objects are successfully returned","responseBody":[{"companyName":"LeadTest","companyID":1,"tax":17.0,"returnNote":"thanks","endOfReturnNote":14,"ccun":"null","ccpw":"null"},{"companyName":"LeadTest","companyID":2,"tax":17.0,"returnNote":"thanks","endOfReturnNote":14,"ccun":"null","ccpw":"null"}]}

                JSONObject respnse;

                try {
                    respnse = jsonObject.getJSONObject(MessageKey.responseBody);
                }
                catch (JSONException e){
                    JSONArray jsonArray = jsonObject.getJSONArray(MessageKey.responseBody);
                    respnse = jsonArray.getJSONObject(0);
                }

                SettingsDBAdapter settingsDBAdapter = new SettingsDBAdapter(SetupNewPOSOnlineActivity.context);
                settingsDBAdapter.open();
                int i = settingsDBAdapter.updateEntry(respnse.getString(MessageKey.companyID), respnse.getString(MessageKey.companyName), SESSION.POS_ID_NUMBER + "",
                        (float) respnse.getDouble(MessageKey.tax), respnse.getString(MessageKey.returnNote), respnse.getInt(MessageKey.endOfReturnNote),
                        respnse.getString(MessageKey.CCUN), respnse.getString(MessageKey.CCPW),respnse.getInt(MessageKey.branchId));
                settingsDBAdapter.close();
                if (i == 1) {
                    Util.isFirstLaunch(SetupNewPOSOnlineActivity.context, true);
                    //finish();
                } else {
                    Log.e("setup",jsonObject.getString(MessageKey.responseType));
                    //Toast.makeText(SetupNewPOSOnlineActivity.context, SetupNewPOSOnlineActivity.context.getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Log.e("setup",jsonObject.getString(MessageKey.responseType));
                //Toast.makeText(SetupNewPOSOnlineActivity.context, SetupNewPOSOnlineActivity.context.getString(R.string.try_again)+": ", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }
}

