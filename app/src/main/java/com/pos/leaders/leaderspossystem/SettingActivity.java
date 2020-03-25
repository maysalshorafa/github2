package com.pos.leaders.leaderspossystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.SettingsDBAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SettingActivity extends AppCompatActivity {
    EditText etCompanyName, etPrivateCompany, etTax, etTerminalNumber, etTerminalPassword,etInvoiceNote;
    Button btSave, btnEditPosSetting;
    CheckBox currencyCheckBox , creditCardCheckBox , customerMeasurementCheckBox ;
    TextView floatPointNo , printerTypeTv ;
    public static final String LEAD_POS_RESULT_INTENT_SETTING_ENABLE_EDIT = "LEAD_POS_RESULT_INTENT_SETTING_ENABLE_EDIT";
    protected static Context context = null;
    public static final String LOG_TAG = "Json_Object";
    public static JSONObject jsonObject;
    public  CharSequence s ="";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);

        TitleBar.setTitleBar(this);
        context = this;
        etCompanyName = (EditText) findViewById(R.id.settings_etCompanyName);
        etCompanyName.setEnabled(false);
        etPrivateCompany = (EditText) findViewById(R.id.settings_etPC);
        etPrivateCompany.setEnabled(false);
        etTax = (EditText) findViewById(R.id.settings_etTax);
        etTax.setEnabled(false);
        etInvoiceNote = (EditText) findViewById(R.id.settings_etInvoiceNote);
        etInvoiceNote.setEnabled(false);
        etTerminalNumber = (EditText) findViewById(R.id.settings_etTNum);
        etTerminalNumber.setEnabled(false);
        etTerminalPassword = (EditText) findViewById(R.id.settings_etTPass);
        etTerminalPassword.setEnabled(false);
        btSave = (Button) findViewById(R.id.settings_btSave);
        btnEditPosSetting = (Button)findViewById(R.id.settings_editPosSetting);
        currencyCheckBox = (CheckBox) findViewById(R.id.setUpManagementCurrencyCheckBox);
        creditCardCheckBox = (CheckBox) findViewById(R.id.setUpManagementCreditCardCheckBox);
        customerMeasurementCheckBox = (CheckBox) findViewById(R.id.setUpManagementCustomerMeasurementCheckBox);
        floatPointNo = (TextView)findViewById(R.id.noOfFloatPoint);
        printerTypeTv = (TextView)findViewById(R.id.printerType);
        floatPointNo.setText(SETTINGS.decimalNumbers+" ");
        printerTypeTv.setText(SETTINGS.printer.toString());
        if(SETTINGS.enableCurrencies){
            currencyCheckBox.setChecked(true);
        }
        if(SETTINGS.creditCardEnable){
            creditCardCheckBox.setChecked(true);
        }
        if(SETTINGS.enableCustomerMeasurement){
            customerMeasurementCheckBox.setChecked(true);
        }
        getSettings();

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPopup();
            }
        });
        btnEditPosSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, SetUpManagement.class);
                i.putExtra(LEAD_POS_RESULT_INTENT_SETTING_ENABLE_EDIT,true);
                startActivity(i);
            }
        });

    }

    public void getSettings() {
        etCompanyName.setText(SETTINGS.companyName);
        etPrivateCompany.setText(SETTINGS.companyID);
        etTax.setText(SETTINGS.tax + "");
        etTerminalNumber.setText(SETTINGS.ccNumber);
        etTerminalPassword.setText(SETTINGS.ccPassword);
        etInvoiceNote.setText(SETTINGS.returnNote);
    }
    public  void callPopup() {
        final StartSettingConnection s1 = new StartSettingConnection();
        s1.onPostExecute("a");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            final String  CompanyName =  SettingActivity.jsonObject.getString(MessageKey.companyName);
            final String PrivateCompany =SettingActivity.jsonObject.getString(MessageKey.companyID);
            final float Tax = (float) jsonObject.getDouble(MessageKey.tax);
            final String TerminalNumber =   jsonObject.getString(MessageKey.CCUN);
            final String TerminalPassword =jsonObject.getString(MessageKey.CCPW);
            final int InvoiceNote =jsonObject.getInt(MessageKey.endOfReturnNote);

            s =SettingActivity.context.getString(R.string.company_name)
                    + ":"+CompanyName +"\n"+SettingActivity.context.getString(R.string.private_company)+":"+PrivateCompany +"\n"+SettingActivity.context.getString(R.string.tax)+":"+Tax+"\n"+SettingActivity.context.getString(R.string.terminal_number)+":"+TerminalNumber +"\n"
                    +SettingActivity.context.getString(R.string.terminal_password)+":"+TerminalPassword +"\n"+ SettingActivity.context.getString(R.string.invoice_note)+ ":"+InvoiceNote;
            new AlertDialog.Builder(SettingActivity.this)
                    .setTitle(getString(R.string.update_general_setting))
                    .setMessage(getString(R.string.if_you_want_to_update_general_setting_click_ok)+"\n"+s)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                           SettingsDBAdapter settingsDBAdapter = new SettingsDBAdapter(SettingActivity.context);
                             settingsDBAdapter.open();
                            // int i = settingsDBAdapter.updateEntry(PrivateCompany, CompanyName,  SESSION.POS_ID_NUMBER +"", Tax, String.valueOf(InvoiceNote), 0, TerminalNumber, TerminalPassword);
                            settingsDBAdapter.close();

                             /**if (i == 1) {
                                 SETTINGS.companyName=CompanyName;
                                 SETTINGS.companyID=PrivateCompany;
                                 SETTINGS.tax=Tax;
                                 SETTINGS.endOfInvoice=InvoiceNote;
                                 SETTINGS.ccNumber=TerminalNumber;
                                 SETTINGS.ccPassword=TerminalPassword;
                                        Toast.makeText(SettingActivity.context,getString(R.string.success_to_save_settings), Toast.LENGTH_SHORT).show();
                            finish();
                             } else {
                                Toast.makeText(SettingActivity.context,getString(R.string.fail_to_save_settings), Toast.LENGTH_SHORT).show();
                             }*/
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void updateSettings(String token) {
        JSONObject jsonObject = null;
        MessageTransmit messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
        try {
                String res = messageTransmit.authGet(ApiURL.CompanyCredentials, token);
                jsonObject = new JSONObject(res);
                try {

                    if (!jsonObject.getString(MessageKey.status).equals("200"))
                        return;
                    SettingActivity.jsonObject = jsonObject.getJSONObject(MessageKey.responseBody);
                    Log.i(SettingActivity.LOG_TAG, jsonObject.toString());

                } catch (JSONException e) {
                    JSONArray jsonArray = jsonObject.getJSONArray(MessageKey.responseBody);
                    jsonObject = jsonArray.getJSONObject(0);
                    SettingActivity.jsonObject = jsonObject;
                    Log.i(SettingActivity.LOG_TAG, jsonObject.toString());

                }


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

}
class StartSettingConnection extends AsyncTask<String,Void,String> {

    private MessageTransmit messageTransmit;

    StartSettingConnection() {
        messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
    }

    @Override
    protected String doInBackground(String... params) {
        return null;
    }



    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String s) {

            final String token = SESSION.token;

            //null response
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    SettingActivity.updateSettings(token);

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {

                }
            }.execute();


        //endregion
    }


}



