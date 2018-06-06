package com.pos.leaders.leaderspossystem.Tools;

import android.util.Pair;

import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.CreditCardPayment;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.Permission.UserPermissions;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.ScheduleWorkers;
import com.pos.leaders.leaderspossystem.Models.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KARAM on 18/10/2016.
 */

public class SESSION {
	public static User _USER = null;
	public static OrderDetails _ORDER;
	public static List<OrderDetails> _ORDERS;
	public static Order _SALE;
	public static ScheduleWorkers _SCHEDULEWORKERS;
	public static UserPermissions _USERPERMISSIONS;
	public static CreditCardPayment _TEMP_CREDITCARD_PAYMNET;
	public static List<Product> _TEMPOFFERPRODUCTS;
	public static List<Pair<Integer, Order>> _SALES;
	public static List<Check> _CHECKS_HOLDER;
	public static int TEMP_NUMBER = 0;
	public static int POS_ID_NUMBER = 0;
	public static long firstIDOffset = 10000000000000000L;

	public static void _LogOut() {
		SESSION._USER = null;
		SESSION._ORDERS = null;
		SESSION._SALE = null;
		SESSION._SCHEDULEWORKERS = null;
		SESSION._USERPERMISSIONS = null;
		SESSION._CHECKS_HOLDER = null;
		TEMP_NUMBER = 0;
		SESSION._ORDER = null;
	}

	public static void _Rest() {
		SESSION._ORDERS = new ArrayList<OrderDetails>();
		SESSION._SALE = new Order(SESSION._USER.getUserId(),new Timestamp(System.currentTimeMillis()), 0, false, 0, 0);
		SESSION._CHECKS_HOLDER = new ArrayList<Check>();
		SESSION._ORDER = null;

	}


	//public static String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrYXJhbSJ9.qU2JoZ74qnkiCzUYCnMq4viTY5Nk8HcE0vFMx0Famww";
	//public static String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYWpkIn0.Sukg7BnOunEClUy3_nMoop4jkDb8kdMoUNNNhX9NTyI";
	public static String token = "";

	public static InternetStatus internetStatus = InternetStatus.ERROR;
	public static SyncStatus syncStatus = SyncStatus.DISABLED;
}
