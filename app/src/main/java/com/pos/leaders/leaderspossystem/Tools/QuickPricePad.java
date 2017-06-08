package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by KARAM on 09/02/2017.
 */

public class QuickPricePad {
    private static final String MyPREFERENCES = "QuickPressValues" ;
    private static SharedPreferences sharedpreferences;
    public static float read(Context context,int id){
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getFloat(id+"",0.0f);
    }
    public static void write(Context context,int id,float value){
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putFloat(id + "", value);
        editor.commit();
    }



}
