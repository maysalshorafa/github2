package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Currency.Currency;

/**
 * Created by Win8.1 on 4/7/2020.
 */

public class ConverterCurrency {

    public static int convertFromToCurrency(){
        int priceAfterConvert=0;
        if (SETTINGS.currencyCode.equals("ILS")){
             priceAfterConvert=0;
        }
        else if(SETTINGS.currencyCode.equals("USD")){
             priceAfterConvert=1;
        }
        else if (SETTINGS.currencyCode.equals("GBP")){
             priceAfterConvert=2;
        }
        else if (SETTINGS.currencyCode.equals("EUR")){
             priceAfterConvert=3;
        }

        return priceAfterConvert;
    }




    public static  double getRateCurrency(String typeCurrency, Context context){

        CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(context);
        currencyDBAdapter.open();
        Currency currency = currencyDBAdapter.getCurrencyByCode(typeCurrency);
        currencyDBAdapter.close();

        return currency.getRate();
    }

}
