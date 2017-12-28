package com.pos.leaders.leaderspossystem.Tools;

import android.database.Cursor;

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
	public static PrinterType printer = PrinterType.SM_S230I;
	public static boolean enableCurrencies = true;
	public static int decimalNumbers = 0 ;

	public static boolean enableCustomerMeasurement = true;

	public static boolean creditCardEnable = true;

	//public static String BO_SERVER_URL = "http://172.16.0.44:8080/leadBO/webapi/";
	//public static String BO_SERVER_URL = "http://185.118.252.26:8080/leadBO/webapi";
	public static String BO_SERVER_URL = "http://192.168.1.106:8080/leadBO/webapi/";

}
