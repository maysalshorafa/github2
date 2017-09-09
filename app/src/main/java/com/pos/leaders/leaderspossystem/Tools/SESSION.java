package com.pos.leaders.leaderspossystem.Tools;

import android.util.Pair;

import com.pos.leaders.leaderspossystem.Models.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by KARAM on 18/10/2016.
 */

public class SESSION {
	public static User _USER;
	public static List<Order> _ORDERS;
	public static Sale _SALE;
	public static ScheduleWorkers _SCHEDULEWORKERS;
	public static UserPermissions _USERPERMISSIONS;
	public static List<Product> _TEMPOFFERPRODUCTS;
	public static List<Pair<Integer, Sale>> _SALES;
	public static List<Check> _CHECKS_HOLDER;
    public static int TEMP_NUMBER = 0;
	public static int POS_ID_NUMBER = 2;
	public static long firstIDOffset = 10000000000000000L;

	public static void _LogOut() {
		SESSION._USER = null;
		SESSION._ORDERS = null;
		SESSION._SALE = null;
		SESSION._SCHEDULEWORKERS = null;
		SESSION._USERPERMISSIONS = null;
		SESSION._CHECKS_HOLDER = null;
        TEMP_NUMBER = 0;
	}

	public static void _Rest() {
		SESSION._ORDERS = new ArrayList<Order>();
		SESSION._SALE = new Sale(SESSION._USER.getId(), new Date(), 0, false, 0, 0);
		SESSION._CHECKS_HOLDER = new ArrayList<Check>();
	}


	//public static String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrYXJhbSJ9.qU2JoZ74qnkiCzUYCnMq4viTY5Nk8HcE0vFMx0Famww";
	public static String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYWpkIn0.Sukg7BnOunEClUy3_nMoop4jkDb8kdMoUNNNhX9NTyI";
}

