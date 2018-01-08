package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

/**
 * Created by KARAM on 30/10/2016.
 */

public class SETTINGS {
	public static String companyID ;
	public static String language ;
	public static double tax;
	public static String companyName ;
	public static String returnNote ;
	public static int endOfInvoice ;
	//public static String ccNumber = "0962549";
	public static String ccNumber ;
	//public static String ccPassword = "0962549";
	public static String ccPassword ;
	public static String posID;
    public static boolean LOADED_DATA ;

	public static String customer_name ;
	public static String printer ;
	public static boolean enableCurrencies ;
	public static int decimalNumbers  ;

	public static boolean enableCustomerMeasurement;

	public static boolean creditCardEnable;

	//public static String BO_SERVER_URL = "http://172.16.0.44:8080/leadBO/webapi/";
	//public static String BO_SERVER_URL = "http://185.118.252.26:8080/leadBO/webapi";
	public static String BO_SERVER_URL = "http://192.168.1.11:8080/webapi/";

}
