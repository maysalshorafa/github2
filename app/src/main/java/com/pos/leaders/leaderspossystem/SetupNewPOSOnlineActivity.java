package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.InventoryDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.LincessDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PosSettingDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.SettingsDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.PosSetting;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.CompanyStatus;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.PrinterType;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.ThisApp;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;
import static com.pos.leaders.leaderspossystem.SetUpManagement.POS_Currency;
import static com.pos.leaders.leaderspossystem.SetupNewPOSOnlineActivity.context;
import static com.pos.leaders.leaderspossystem.SetupNewPOSOnlineActivity.posSetting;

public class SetupNewPOSOnlineActivity extends Activity {
    public final static String BO_CORE_ACCESS_AUTH = "BOCOREACCESSAUTH";
    public final static String BO_CORE_ACCESS_TOKEN = "BOCOREACCESSTOKEN";
    protected static String posPass = null;
    protected static String posPrefix = null;
    protected static String currencyCode=null;
    protected static String currencySymbol=null;
    protected static String country=null;
    public static String companyName = null;
    protected static  List<String> ArrayCurrencySelect = new ArrayList<String>();
    public static Integer syncNumber = null;
    protected static String uuid = null;

    protected static boolean restart = false;
    protected static Context context = null;
    protected  static  PosSetting posSetting;


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
        ThisApp.setCurrentActivity(this);
        context = SetupNewPOSOnlineActivity.this;


      SharedPreferences sharedPreferencesCurrency = getSharedPreferences(POS_Currency, MODE_PRIVATE);
       SetupNewPOSOnlineActivity.currencyCode= sharedPreferencesCurrency.getString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_CURRENCY_CODE, null);
        SetupNewPOSOnlineActivity.currencySymbol=sharedPreferencesCurrency.getString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_CURRENCY_SYMBOL, null);
        SetupNewPOSOnlineActivity.country=sharedPreferencesCurrency.getString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_COUNTRY,null);
        String strJson = sharedPreferencesCurrency.getString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_LIST_CURRENCY_TYPE,null);

        SetupNewPOSOnlineActivity.ArrayCurrencySelect.add(SetupNewPOSOnlineActivity.currencyCode);

            if (strJson != null) {
                try {
                    JSONArray jsonArray = new JSONArray(strJson);
                    for(int index = 0;index < jsonArray.length(); index++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(index);
                        String currencyCode = jsonObject.getString("Key");
                        SetupNewPOSOnlineActivity.ArrayCurrencySelect.add(currencyCode);

                    }
                    Log.d("keyyy",ArrayCurrencySelect.toString());
                   /* for(int i=0; i<jArray.length(); i++)
                    {

                    }*/


                } catch (JSONException e) {

                }
            }

         /*   for (int i=0;i<SetupNewPOSOnlineActivity.ArrayCurrencySelect.size();i++){
                if (SetupNewPOSOnlineActivity.ArrayCurrencySelect.get(i).equals(SetupNewPOSOnlineActivity.currencyCode)){
                    SetupNewPOSOnlineActivity.ArrayCurrencySelect.remove(i);
                }
            }*/

        Log.d("finalArraySelectList",SetupNewPOSOnlineActivity.ArrayCurrencySelect.toString());



        //region check internet connection
        if(!SyncMessage.isConnected(this)) {
            AlertDialog alertDialog = new AlertDialog.Builder(SetupNewPOSOnlineActivity.this).create();
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


        //get  Currency









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

    final ProgressDialog progressDialog = new ProgressDialog(ThisApp.getContext());
    final ProgressDialog progressDialog2 = new ProgressDialog(ThisApp.getContext());

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
            JSONObject responseBody = null;
            if (jsonObject.getInt(MessageKey.status) == 200) {
                 responseBody = jsonObject.getJSONObject(MessageKey.responseBody);
                posPass = responseBody.getString(MessageKey.PosPass);
                posPrefix = responseBody.getString(MessageKey.PosID);
                companyName = responseBody.getString(MessageKey.companyName);
                syncNumber = responseBody.getInt(MessageKey.syncNumber);


            }
           /* Log.d("messageResultONSTART",initRes);
            Log.d("jsonObjectOnStart",responseBody.toString());*/


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
            SharedPreferences sharedpreferences = context.getSharedPreferences(SetupNewPOSOnlineActivity.BO_CORE_ACCESS_AUTH, Context.MODE_PRIVATE);
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
            AccessToken accessToken = new AccessToken(context);
            accessToken.execute(context);

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
                    companyLicense(token);
                    updateSettings(token);
                    updateInventory(token);

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if (progressDialog2.isShowing()){
                        progressDialog2.cancel();}
                }
            }.execute();
        } else {
            //fail
            Toast.makeText(context, "Try Again With True Key.", Toast.LENGTH_SHORT).show();
        }
        progressDialog.cancel();
        super.onPostExecute(s);

        //endregion
    }


    private void updateSettings(String token) {

        MessageTransmit messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
        try {
            String res = messageTransmit.authGet(ApiURL.CompanyCredentials, token);
            Log.d("companyCrediti",res);
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
               String currencyCOde=null,currencySymbol=null,country=null;


                currencyCOde =respnse.getString(MessageKey.currencyCode);
                currencySymbol=respnse.getString(MessageKey.currencySymbol);
                country=respnse.getString(MessageKey.country);


                if (currencyCOde!=null && currencySymbol!=null&&country!=null) {
                    if (currencyCOde.equals(SetupNewPOSOnlineActivity.currencyCode) && currencySymbol.equals(SetupNewPOSOnlineActivity.currencySymbol)) {

                    } else {
                        currencyCOde=SetupNewPOSOnlineActivity.currencyCode;
                        currencySymbol=SetupNewPOSOnlineActivity.currencySymbol;
                        country=SetupNewPOSOnlineActivity.country;
                    }
                }


               SettingsDBAdapter settingsDBAdapter = new SettingsDBAdapter(SetupNewPOSOnlineActivity.context);
                settingsDBAdapter.open();
                int i = settingsDBAdapter.updateEntry(respnse.getString(MessageKey.companyID), respnse.getString(MessageKey.companyName), SESSION.POS_ID_NUMBER + "",
                        (float) respnse.getDouble(MessageKey.tax), respnse.getString(MessageKey.returnNote), respnse.getInt(MessageKey.endOfReturnNote),
                        respnse.getString(MessageKey.CCUN), respnse.getString(MessageKey.CCPW),respnse.getInt(MessageKey.branchId),SetupNewPOSOnlineActivity.currencyCode,SetupNewPOSOnlineActivity.currencySymbol,SetupNewPOSOnlineActivity.country
                        );
                settingsDBAdapter.close();
             /**   if (i == 1) {
                    Util.isFirstLaunch(SetupNewPOSOnlineActivity.context, true);
                    //finish();
                } else {
                    Log.e("setup",jsonObject.getString(MessageKey.responseType));
                    //Toast.makeText(SetupNewPOSOnlineActivity.context, SetupNewPOSOnlineActivity.context.getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                }*/
            }
            else {
                Log.e("setup",jsonObject.getString(MessageKey.responseType));
                //Toast.makeText(SetupNewPOSOnlineActivity.context, SetupNewPOSOnlineActivity.context.getString(R.string.try_again)+": ", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }
    private void companyLicense(String token) {

        MessageTransmit messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
        try {
            String res = messageTransmit.authGet(ApiURL.CompanyLicense+"/Active", token);
            Log.d("companyLicense",res);
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
                    Log.d("respnsCompanyLicense",respnse.toString());
                }
                catch (JSONException e){
                    JSONArray jsonArray = jsonObject.getJSONArray(MessageKey.responseBody);
                    respnse = jsonArray.getJSONObject(0);
                }
                String dueDate=null,activationDate=null;
                LincessDBAdapter lincessDBAdapter = new LincessDBAdapter(context);
                lincessDBAdapter.open();

                activationDate =respnse.getString(MessageKey.activationDate);
                dueDate=respnse.getString(MessageKey.dueDate);

                long millisecond = Long.parseLong(activationDate);

                SETTINGS.dueDate= DateConverter.toDate(Long.parseLong(dueDate));
                Timestamp ts = new Timestamp(Long.parseLong(dueDate));
                Timestamp ts2=new Timestamp(Long.parseLong(activationDate));

                long LincesID = lincessDBAdapter.insertEntry(respnse.getString(MessageKey.companyId),respnse.getString(MessageKey.note),respnse.getString(MessageKey.branchId), ts2 , ts, CONSTANT.ACTIVE);

            }
            else {
                Log.e("setup",jsonObject.getString(MessageKey.responseType));
                //Toast.makeText(SetupNewPOSOnlineActivity.context, SetupNewPOSOnlineActivity.context.getString(R.string.try_again)+": ", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }
    private void updateInventory(String token) {

        MessageTransmit messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
        try {
            String res = messageTransmit.authGet(ApiURL.INVENTORY+"/forPos", token);
            Log.e("resInventory", res);
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

                InventoryDbAdapter inventoryDbAdapter = new InventoryDbAdapter(context);
                inventoryDbAdapter.open();
                long i = inventoryDbAdapter.insertEntry(respnse.getString("name"), respnse.getLong("inventoryId"), respnse.getString("productsIdWithQuantityList"),
                        respnse.getInt("branchId"), 0);
                inventoryDbAdapter.close();


                if (i>= 1) {
                    SettingsDBAdapter settingsDBAdapter = new SettingsDBAdapter(context);
                    settingsDBAdapter.open();
                    settingsDBAdapter.updateEntry( SETTINGS.companyID,SETTINGS.companyName,  SETTINGS.posID, (float) SETTINGS.tax, SETTINGS.returnNote,SETTINGS.endOfInvoice,SETTINGS.ccNumber,SETTINGS.ccPassword,
                            respnse.getInt("branchId"),SETTINGS.currencyCode,SETTINGS.currencySymbol,SETTINGS.country);
                    SharedPreferences cSharedPreferences = context.getSharedPreferences("POS_Management", MODE_PRIVATE);
                    boolean creditCardEnable = cSharedPreferences.getBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CREDIT_CARD, false);
                    boolean pinPadEnable = cSharedPreferences.getBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PIN_PAD, false);
                    boolean currencyEnable = cSharedPreferences.getBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY, false);
                    boolean customerMeasurementEnable = cSharedPreferences.getBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CUSTOMER_MEASUREMENT, false);
                    int floatP = Integer.parseInt(cSharedPreferences.getString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_FLOAT_POINT, "2"));
                    String printerType = cSharedPreferences.getString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PRINTER_TYPE, PrinterType.HPRT_TP805.name());
                    int branchI = Integer.parseInt(cSharedPreferences.getString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_BRANCH_ID, "0"));
                    String companyStatus = cSharedPreferences.getString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_COMPANY_STATUS, CompanyStatus.BO_AUTHORIZED_DEALER.name());

                    PackageInfo pInfo = null;
                    Log.d("CompanyStatus4",companyStatus);
                    try {
                        pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    String verCode = pInfo.versionName;
                    PosSettingDbAdapter posSettingDbAdapter = new PosSettingDbAdapter(context);
                    posSettingDbAdapter.open();
                    posSettingDbAdapter.insertEntry(currencyEnable,creditCardEnable,pinPadEnable,customerMeasurementEnable,floatP,printerType,companyStatus,verCode, DbHelper.DATABASE_VERSION+"",branchI,SetupNewPOSOnlineActivity.currencyCode,SetupNewPOSOnlineActivity.currencySymbol,SetupNewPOSOnlineActivity.country,SETTINGS.   enableDuplicateInvoice);
                    posSetting=posSettingDbAdapter.getPosSettingID();
                    addPosSetting(posSetting);
                    Log.d("idPosSettingPO",posSetting.toString());
                    Util.isFirstLaunch(context, true);
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


    private void addPosSetting(PosSetting posSetting) {

        MessageTransmit messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
        final String token = SESSION.token;
        try {

            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(posSetting);
            String res = messageTransmit.authPost(ApiURL.PosSetting,jsonString, token);
            Log.d("ADD_POS_SETTING",res);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(res);
                if(jsonObject.getString(MessageKey.status).equals("200")) {
                    SetupNewPOSOnlineActivity.restart = true;
                    CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(SetupNewPOSOnlineActivity.context);
                    currencyTypeDBAdapter.open();
                    for (int i=0;i<SetupNewPOSOnlineActivity.ArrayCurrencySelect.size();i++){

                        currencyTypeDBAdapter.insertEntry(new CurrencyType(i,SetupNewPOSOnlineActivity.ArrayCurrencySelect.get(i)));
                        currencyTypeDBAdapter.insertEntry(SetupNewPOSOnlineActivity.ArrayCurrencySelect.get(i));}
                    final Intent i = new Intent(SetupNewPOSOnlineActivity.context, SplashScreenActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    SetupNewPOSOnlineActivity.context.startActivity(i);

                }
                else {
                    Log.d("addPossSetting",jsonObject.getString(MessageKey.responseType));
                }
            }
            catch (JSONException e){
               Log.e("exceptionAddPosSetting",e.toString());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}




