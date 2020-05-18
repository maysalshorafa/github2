package com.pos.leaders.leaderspossystem.SettingsTab;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.SettingsDBAdapter;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.SettingActivity;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.ThisApp;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;
import com.pos.leaders.leaderspossystem.syncposservice.Service.SyncMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.pos.leaders.leaderspossystem.SettingsTab.GeneralSettings.context;

/**
 * Created by Win8.1 on 3/25/2018.
 */

public class GeneralSettings extends Fragment {
    public static boolean checkInternetConnectionVariable;
    TextView etCompanyName, etPrivateCompany, etTax, etTerminalNumber, etTerminalPassword,etInvoiceNote , returnNote;
    Button btSave ;
    public static JSONObject jsonObject;
    public  CharSequence s ="";
    protected static Context context = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=getContext();
        ThisApp.setCurrentActivity(getActivity());

        View v= inflater.inflate(R.layout.general_setting_fragment, container, false);
        etCompanyName = (TextView) v.findViewById(R.id.settings_etCompanyName);
        etCompanyName.setEnabled(false);
        etPrivateCompany = (TextView)  v.findViewById(R.id.settings_etPC);
        etPrivateCompany.setEnabled(false);
        etTax = (TextView)  v.findViewById(R.id.settings_etTax);
        etTax.setEnabled(false);
        etInvoiceNote = (TextView)  v.findViewById(R.id.settings_etInvoiceNote);
        etInvoiceNote.setEnabled(false);
        etTerminalNumber = (TextView)  v.findViewById(R.id.settings_etTNum);
        etTerminalNumber.setEnabled(false);
        etTerminalPassword = (TextView)  v.findViewById(R.id.settings_etTPass);
        etTerminalPassword.setEnabled(false);
        returnNote = (TextView)  v.findViewById(R.id.settings_etTPass);
        btSave = (Button)  v.findViewById(R.id.settings_btSave);
        getSettings();
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPopup();
            }
        });

        return v;
    }
    public void getSettings() {
        etCompanyName.setText(SETTINGS.companyName);
        etPrivateCompany.setText(SETTINGS.companyID);
        etTax.setText(Util.makePrice(SETTINGS.tax));
        etTerminalNumber.setText(SETTINGS.ccNumber);
        etTerminalPassword.setText(SETTINGS.ccPassword);
        etInvoiceNote.setText(SETTINGS.returnNote);
    }
    public  void callPopup() {

        final ProgressDialog dialog=new ProgressDialog(getContext());
        dialog.setTitle(getContext().getString(R.string.wait_for_finish));
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {

                dialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                                if(checkInternetConnectionVariable){
                                if(GeneralSettings.jsonObject!=null) {
                                    final String CompanyName = GeneralSettings.jsonObject.getString(MessageKey.companyName);
                                    final String PrivateCompany = GeneralSettings.jsonObject.getString(MessageKey.companyID);
                                    final float Tax = (float) jsonObject.getDouble(MessageKey.tax);
                                    final String TerminalNumber = jsonObject.getString(MessageKey.CCUN);
                                    final String TerminalPassword = jsonObject.getString(MessageKey.CCPW);
                                    final int InvoiceNote = jsonObject.getInt(MessageKey.endOfReturnNote);
                                    final String ReturnNote = jsonObject.getString(MessageKey.returnNote);
                                    if (SETTINGS.company.equals("BO_EXEMPT_DEALER")){
                                        s = getContext().getString(R.string.company_name) + ":" + CompanyName + "\n"
                                                + getContext().getString(R.string.privet_company_status) + ":" + PrivateCompany + "\n" +
                                                getContext().getString(R.string.invoice_note) + ":" + InvoiceNote  +  "\n"
                                                +"Return Note" + ":" + ReturnNote + "\n"+
                                                getContext().getString(R.string.tax) + ":" + Tax + "\n"
                                                + getContext().getString(R.string.terminal_number) + ":" + TerminalNumber + "\n"+
                                                getContext().getString(R.string.terminal_password) + ":" + TerminalPassword + "\n" ;
                                    }
                                   else {
                                        s = getContext().getString(R.string.company_name) + ":" + CompanyName + "\n"
                                                + getContext().getString(R.string.private_company) + ":" + PrivateCompany + "\n" +
                                                getContext().getString(R.string.invoice_note) + ":" + InvoiceNote  +  "\n"
                                                +"Return Note" + ":" + ReturnNote + "\n"+
                                                getContext().getString(R.string.tax) + ":" + Tax + "\n"
                                                + getContext().getString(R.string.terminal_number) + ":" + TerminalNumber + "\n"+
                                                getContext().getString(R.string.terminal_password) + ":" + TerminalPassword + "\n" ;
                                    }

                                    new AlertDialog.Builder(getContext())
                                            .setTitle(getString(R.string.update_general_setting))
                                            .setMessage(getString(R.string.if_you_want_to_update_general_setting_click_ok) + "\n" + s)
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    SettingsDBAdapter settingsDBAdapter = new SettingsDBAdapter(getContext());
                                                    settingsDBAdapter.open();

                                                  /**  int i = settingsDBAdapter.updateEntry(PrivateCompany, CompanyName, SESSION.POS_ID_NUMBER + "", Tax, ReturnNote, InvoiceNote, TerminalNumber, TerminalPassword);
                                                    settingsDBAdapter.close();

                                                    if (i == 1) {
                                                        SETTINGS.companyName = CompanyName;
                                                        SETTINGS.companyID = PrivateCompany;
                                                        SETTINGS.tax = Tax;
                                                        SETTINGS.endOfInvoice = InvoiceNote;
                                                        SETTINGS.ccNumber = TerminalNumber;
                                                        SETTINGS.ccPassword = TerminalPassword;
                                                        SETTINGS.returnNote=ReturnNote;
                                                        Toast.makeText(getContext(), getString(R.string.success_to_save_settings), Toast.LENGTH_SHORT).show();
                                                        //finish();

                                                    } else {
                                                        Toast.makeText(getContext(), getString(R.string.fail_to_save_settings), Toast.LENGTH_SHORT).show();
                                                    }**/
                                                }
                                            })
                                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // do nothing
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
                                }else{
                                    Toast.makeText(getContext(), getString(R.string.fail_to_make_internet_connection), Toast.LENGTH_SHORT).show();
                                }
                                dialog.cancel();

                    } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected Void doInBackground(Void... params) {
                final StartCompanyCredentialsConnection s1 = new StartCompanyCredentialsConnection();
                s1.onPostExecute("a");
                return null;
            }
        }.execute();



    }
    public static void updateSettings(String token) throws JSONException {
        JSONObject jsonObject = null;
        MessageTransmit messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
        try {

            String res = messageTransmit.authGet(ApiURL.CompanyCredentials, token);
            jsonObject = new JSONObject(res);
            try {

                if(!jsonObject.getString(MessageKey.status).equals("200"))
                    return;
                GeneralSettings.jsonObject = jsonObject.getJSONObject(MessageKey.responseBody);
                Log.i(SettingActivity.LOG_TAG, jsonObject.toString());


            }
            catch (JSONException e){
                JSONArray jsonArray = jsonObject.getJSONArray(MessageKey.responseBody);
                jsonObject = jsonArray.getJSONObject(0);
                GeneralSettings.jsonObject = jsonObject;
                Log.i(SettingActivity.LOG_TAG, jsonObject.toString());

            }

        } catch (IOException e1) {
        e1.printStackTrace();
        }


    }

}
class StartCompanyCredentialsConnection extends AsyncTask<String,Void,String> {

    private MessageTransmit messageTransmit;

    StartCompanyCredentialsConnection() {
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
                if (SyncMessage.isConnected(context)) {
                    try {
                        GeneralSettings.checkInternetConnectionVariable=true;
                        GeneralSettings.updateSettings(token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    GeneralSettings.checkInternetConnectionVariable=false;
                    Log.d("fail", "fail internet connection");

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

            }
        }.execute();


        //endregion
    }

}
