package com.pos.leaders.leaderspossystem.Tools;

import android.graphics.Bitmap;

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

	public static PrinterType printer ;
	public static boolean enableCurrencies ;
	public static int decimalNumbers  ;

	public static boolean enableCustomerMeasurement;

	public static boolean creditCardEnable;
	public static boolean pinpadEnable;
	public static boolean timerState = false;
	//public static String BO_SERVER_URL = "http://172.16.0.44:8080/CORE-0.0.1-SNAPSHOT/webapi/";

	//public static String BO_SERVER_URL = "http://185.118.252.26:8080/leadBO/webapi";

	//public static String BO_SERVER_URL = "http://18.220.245.144:8080/webapi";

//	public static String BO_SERVER_URL = "http://192.168.1.26:8080/";

	//public static String BO_SERVER_URL = "http://192.168.1.26:8080/";

	//public static String BO_SERVER_URL = "http://192.168.1.25:8080/webapi-0.0.2/";
	//public static String BO_SERVER_URL = "https://api.leadpos.io/webapi/";

	public static String BO_SERVER_URL = "http://ec2-18-220-97-11.us-east-2.compute.amazonaws.com:8080/webapi-0.0.2";


	//public static String BO_SERVER_URL = "http://192.168.1.26:8080/";
	//public static String BO_SERVER_URL = "https://api.leadpos.io/webapi/";

	//public static String BO_SERVER_URL = "http://ec2-18-220-97-11.us-east-2.compute.amazonaws.com:8080/webapi-0.0.2";
//	public static String BO_SERVER_URL = "http://192.168.1.26:8080/";
	//public static String BO_SERVER_URL = "https://api.leadpos.io/webapi/";

	//public static String BO_SERVER_URL = "https://api.leadpos.io/webapi";
	//public static String BO_SERVER_URL = "http://api_01.leadpos.io:8080/webapi";

	//public static String BO_SERVER_URL = "http://ec2-18-220-245-144.us-east-2.compute.amazonaws.com:8080/webapi";


	//public static String BO_SERVER_URL = "http://192.168.1.26:8080/";
	//public static final String UPDATER_API_URL_PATH = "http://192.168.1.19:8090/updateApk/";

	//public static final String UPDATER_API_URL_PATH = "http://185.118.252.26:8091/updateApkNV/";
	public static final String UPDATER_API_URL_PATH = "/updateApk/";
	//public static final String UPDATER_SERVER_URL = "http://185.118.252.26:8090";
	public static final String UPDATER_SERVER_URL = "http://185.118.252.26:8070";
	//public static final String UPDATER_SERVER_URL = "http://192.168.1.109:8090";

	public static Bitmap copyInvoiceBitMap ; // add here to avoid recycle bitmap exception when used reference between two different activity




}
