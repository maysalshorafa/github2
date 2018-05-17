package com.pos.leaders.leaderspossystem.SettingsTab;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.SplashScreenActivity;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Win8.1 on 3/25/2018.
 */

public class BOPOSVersionSettings  extends Fragment {
    TextView posVersionNo , posDbVersionNo ,boVersionNo ,boDbVersionNo , FeVersionNo  ;
    public static JSONObject jsonObject;
    public final static String BO_SETTING = "BO_SETTING";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.bo_pos_version_settings, container, false);
        posVersionNo = (TextView) v.findViewById(R.id.pos_version_no);
        posDbVersionNo = (TextView) v.findViewById(R.id.pos_db_version_no);
        boVersionNo = (TextView) v.findViewById(R.id.bo_version_no);
        boDbVersionNo = (TextView) v.findViewById(R.id.bo_db_version_no);
        FeVersionNo = (TextView) v.findViewById(R.id.fe_version_no);
        try {
            PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            int version = pInfo.versionCode;
            posVersionNo.setText(version+"");

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        posDbVersionNo.setText(DbHelper.DATABASE_VERSION+"");
        getBoSetting();
        return v;
    }
    public  void getBoSetting() {
        final SharedPreferences cSharedPreferences = getContext().getSharedPreferences(BO_SETTING, MODE_PRIVATE);
        final SharedPreferences.Editor editor = cSharedPreferences.edit();
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
                    if(BOPOSVersionSettings.jsonObject!=null){
                    if(BOPOSVersionSettings.jsonObject.getString(MessageKey.version).equals(null)){
                        if (cSharedPreferences.contains(SplashScreenActivity.LEAD_POS_RESULT_INTENT_BO_SETTING_ACTIVITY_BO_VERSION)) {
                            String boV= cSharedPreferences.getString(SplashScreenActivity.LEAD_POS_RESULT_INTENT_BO_SETTING_ACTIVITY_BO_VERSION, "");
                            boVersionNo.setText(boV);
                        }
                    }else {
                        boVersionNo.setText(BOPOSVersionSettings.jsonObject.getString(MessageKey.version));
                        editor.putString(SplashScreenActivity.LEAD_POS_RESULT_INTENT_BO_SETTING_ACTIVITY_BO_VERSION, BOPOSVersionSettings.jsonObject.getString(MessageKey.version));
                    }


                    if(BOPOSVersionSettings.jsonObject.getString(MessageKey.dbVersion).equals(null)){
                        if (cSharedPreferences.contains(SplashScreenActivity.LEAD_POS_RESULT_INTENT_BO_SETTING_ACTIVITY_BO_DB_VERSION)) {
                            String boDV= cSharedPreferences.getString(SplashScreenActivity.LEAD_POS_RESULT_INTENT_BO_SETTING_ACTIVITY_BO_DB_VERSION, "");
                            boDbVersionNo.setText(boDV);
                        }
                    }else {
                        boDbVersionNo.setText(BOPOSVersionSettings.jsonObject.getString(MessageKey.dbVersion));
                        editor.putString(SplashScreenActivity.LEAD_POS_RESULT_INTENT_BO_SETTING_ACTIVITY_BO_DB_VERSION, BOPOSVersionSettings.jsonObject.getString(MessageKey.dbVersion));
                    }


                    if(BOPOSVersionSettings.jsonObject.getString(MessageKey.FEVersion).equals(null)){
                        if (cSharedPreferences.contains(SplashScreenActivity.LEAD_POS_RESULT_INTENT_BO_SETTING_ACTIVITY_FE_VERSION)) {
                            String FV= cSharedPreferences.getString(SplashScreenActivity.LEAD_POS_RESULT_INTENT_BO_SETTING_ACTIVITY_FE_VERSION, "");
                            FeVersionNo.setText(FV);
                        }
                    }else {
                        FeVersionNo.setText(BOPOSVersionSettings.jsonObject.getString(MessageKey.FEVersion));
                        editor.putString(SplashScreenActivity.LEAD_POS_RESULT_INTENT_BO_SETTING_ACTIVITY_FE_VERSION, BOPOSVersionSettings.jsonObject.getString(MessageKey.FEVersion));
                    }
                    editor.apply();
                    }
                    else {
                        if (cSharedPreferences.contains(SplashScreenActivity.LEAD_POS_RESULT_INTENT_BO_SETTING_ACTIVITY_BO_VERSION)) {
                            String boV= cSharedPreferences.getString(SplashScreenActivity.LEAD_POS_RESULT_INTENT_BO_SETTING_ACTIVITY_BO_VERSION, "");
                            boVersionNo.setText(boV);
                        }
                        if (cSharedPreferences.contains(SplashScreenActivity.LEAD_POS_RESULT_INTENT_BO_SETTING_ACTIVITY_BO_DB_VERSION)) {
                            String boDV= cSharedPreferences.getString(SplashScreenActivity.LEAD_POS_RESULT_INTENT_BO_SETTING_ACTIVITY_BO_DB_VERSION, "");
                            boDbVersionNo.setText(boDV);
                        }
                        if (cSharedPreferences.contains(SplashScreenActivity.LEAD_POS_RESULT_INTENT_BO_SETTING_ACTIVITY_FE_VERSION)) {
                            String FV= cSharedPreferences.getString(SplashScreenActivity.LEAD_POS_RESULT_INTENT_BO_SETTING_ACTIVITY_FE_VERSION, "");
                            FeVersionNo.setText(FV);
                        }
                    }
                    dialog.cancel();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                final StartBoVersionSettingConnection s1 = new StartBoVersionSettingConnection();
                s1.onPostExecute("a");
                return null;
            }
        }.execute();


    }


}


