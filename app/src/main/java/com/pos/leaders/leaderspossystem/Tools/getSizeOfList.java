package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Win8.1 on 5/20/2020.
 */

public class getSizeOfList {
    public static List<? extends Serializable> getSizeList(Context context){
        int size=-1;boolean enableCurrency=false;
        List<CurrencyType> currencyTypesList = null;
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(context);
        currencyTypeDBAdapter.open();
        currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
        currencyTypeDBAdapter.close();
        if (currencyTypesList!=null){
        size=currencyTypesList.size();}
        if (SETTINGS.enableCurrencies){
            enableCurrency=true;
        }
        return Arrays.asList(size,enableCurrency);
    }
}
