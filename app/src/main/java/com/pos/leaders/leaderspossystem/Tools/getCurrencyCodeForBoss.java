package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.SettingsDBAdapter;

/**
 * Created by LeadPos on 7/19/2020.
 */

public class getCurrencyCodeForBoss {

    public  static String getCurrencyCodeDefault(Context context){
        String defultCurrency = null;

        SettingsDBAdapter settingsDBAdapter=new SettingsDBAdapter(context);
        settingsDBAdapter.open();
        if (SETTINGS.currencyCode.isEmpty()||SETTINGS.currencyCode.equals("null")){
            boolean haveSetting= settingsDBAdapter.GetSettings();
            if (haveSetting){
                defultCurrency=SETTINGS.currencyCode;
            }
        }
        else {
            defultCurrency=SETTINGS.currencyCode;

        }
        settingsDBAdapter.close();
        return defultCurrency;
    }
}
