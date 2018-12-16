package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Tools.Generators;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by KARAM on 19/10/2016.
 */

public class OrderDBAdapter {
	protected static final String ORDER_TABLE_NAME = "_Order";
	// Column Names
	protected static final String ORDER_COLUMN_ID = "id";
	protected static final String ORDER_COLUMN_BYUSER = "byEmployee";
	protected static final String ORDER_COLUMN_ORDERDATE = "order_date";
	protected static final String ORDER_COLUMN_REPLACEMENTNOTE = "replacementNote";
	protected static final String ORDER_COLUMN_STATUS = "status";
	protected static final String ORDER_COLUMN_TOTALPRICE = "total_price";
	protected static final String ORDER_COLUMN_TOTALPAID = "total_paid_amount";
	protected static final String ORDER_COLUMN_CUSTOMER_ID = "customerId";
	protected static final String ORDER_COLUMN_CUSTOMER_NAME = "customer_name";
	protected static final String ORDER_COLUMN_ORDER_DISCOUNT = "cartDiscount";
	protected static final String ORDER_COLUMN_ORDER_KEY = "key";
	protected static final String ORDER_COLUMN_ORDER_NUMBER_DISCOUNT = "numberDiscount";

	public static final String DATABASE_CREATE = "CREATE TABLE _Order( `id` INTEGER PRIMARY KEY AUTOINCREMENT, `byEmployee` INTEGER, `order_date` TIMESTAMP DEFAULT current_timestamp, " +
			"`replacementNote` INTEGER DEFAULT 0, `status` INTEGER DEFAULT 0, total_price REAL, total_paid_amount REAL, customerId  INTEGER DEFAULT 0 ,customer_name  TEXT,cartDiscount REAL DEFAULT 0.0, key  TEXT , numberDiscount REAL DEFAULT 0.0," +
			"FOREIGN KEY(`byEmployee`) REFERENCES `employees.id`)";

	// Variable to hold the database instance
	private SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DbHelper dbHelper;

	public OrderDBAdapter(Context context) {
		this.context = context;
		this.dbHelper = new DbHelper(context);
	}

	public OrderDBAdapter open() throws SQLException {
		this.db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}

	public SQLiteDatabase getDatabaseInstance() {
		return db;
	}



	public long insertEntry(long byUser, Timestamp saleDate, int replacementNote, boolean canceled, double totalPrice, double totalPaid, long custmer_id, String custmer_name,double cartDiscount,double numberDiscount) {
		Generators key = new Generators().setLength(6);
		Order order = new Order(Util.idHealth(this.db, ORDER_TABLE_NAME, ORDER_COLUMN_ID), byUser, saleDate, replacementNote, canceled, totalPrice, totalPaid, custmer_id, custmer_name,cartDiscount,key.generate(),numberDiscount);

		sendToBroker(MessageType.ADD_ORDER, order, this.context);

		try {
			return insertEntry(order);
		} catch (SQLException ex) {
			Log.e("Sales DB insert", "inserting Entry at " + ORDER_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}
	public long invoiceInsertEntry(long byUser, Timestamp saleDate, int replacementNote, boolean canceled, double totalPrice, double totalPaid, long custmer_id, String custmer_name,double cartDiscount,double numberDiscount) {
		Generators key = new Generators().setLength(6);
		Order order = new Order(Util.idHealth(this.db, ORDER_TABLE_NAME, ORDER_COLUMN_ID), byUser, saleDate, replacementNote, canceled, totalPrice, totalPaid, custmer_id, custmer_name,cartDiscount,key.generate(),numberDiscount);

		//    sendToBroker(MessageType.ADD_ORDER, order, this.context);

		try {
			return insertEntry(order);
		} catch (SQLException ex) {
			Log.e("Sales DB insert", "inserting Entry at " + ORDER_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}


	public long insertEntry(Order order , long _custmer_id, String custmer_name,boolean invoiceStatus) {
		if(!invoiceStatus){
		return insertEntry(order.getByUser(), order.getCreatedAt(), order.getReplacementNote(), order.isStatus(), order.getTotalPrice(),order.getTotalPaidAmount(),_custmer_id,custmer_name,order.getCartDiscount(),order.getNumberDiscount());
	}
	return invoiceInsertEntry(order.getByUser(), order.getCreatedAt(), order.getReplacementNote(), order.isStatus(), order.getTotalPrice(),order.getTotalPaidAmount(),_custmer_id,custmer_name,order.getCartDiscount(),order
	.getNumberDiscount());

	}
	public long insertEntry(Order order){
        ContentValues val = new ContentValues();
        val.put(ORDER_COLUMN_ID,order.getOrderId());
        //Assign values for each row.
        val.put(ORDER_COLUMN_BYUSER, order.getByUser());
        val.put(ORDER_COLUMN_REPLACEMENTNOTE, order.getReplacementNote());
        val.put(ORDER_COLUMN_STATUS, order.isStatus()?1:0);
        val.put(ORDER_COLUMN_TOTALPRICE, order.getTotalPrice());
        val.put(ORDER_COLUMN_TOTALPAID, order.getTotalPaidAmount());
        val.put(ORDER_COLUMN_CUSTOMER_ID, order.getCustomerId());
        val.put(ORDER_COLUMN_CUSTOMER_NAME, order.getCustomer_name());
		val.put(ORDER_COLUMN_ORDER_DISCOUNT,order.getCartDiscount());
		val.put(ORDER_COLUMN_ORDER_KEY,order.getOrderKey());
		val.put(ORDER_COLUMN_ORDER_NUMBER_DISCOUNT,order.getNumberDiscount());


		try {
            return db.insert(ORDER_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("Order DB insert", "inserting Entry at " + ORDER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

	public Order getOrderById(long id) {
		Order order = null;
		Cursor cursor = db.rawQuery("select * from " + ORDER_TABLE_NAME + " where id='" + id + "'", null);
		if (cursor.getCount() < 1) // UserName Not Exist
		{
			cursor.close();
			return order;
		}
		cursor.moveToFirst();
		order = new Order(makeSale(cursor));
		cursor.close();

		return order;
	}

	public long deleteEntry(long id) {
        OrderDBAdapter orderDBAdapter=new OrderDBAdapter(context);
        orderDBAdapter.open();
		// Define the updated row content.
		ContentValues updatedValues = new ContentValues();
		// Assign values for each row.
		updatedValues.put(ORDER_COLUMN_STATUS, 1);

		String where = ORDER_COLUMN_ID + " = ?";
		try {
			 db.update(ORDER_TABLE_NAME, updatedValues, where, new String[]{id + ""});
            Order order = orderDBAdapter.getOrderById(id);
			sendToBroker(MessageType.DELETE_ORDER, order, this.context);
			return 1;
		} catch (SQLException ex) {
			Log.e("sales DB deleteEntry", "enable hide Entry at " + ORDER_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}

	public void updateEntry(Order sale) {
		ContentValues val = new ContentValues();
		//Assign values for each row.
		val.put(ORDER_COLUMN_BYUSER, sale.getByUser());
		val.put(ORDER_COLUMN_REPLACEMENTNOTE, sale.getReplacementNote());
		val.put(ORDER_COLUMN_STATUS, sale.isStatus());
		val.put(ORDER_COLUMN_TOTALPRICE, sale.getTotalPrice());
		val.put(ORDER_COLUMN_ORDER_DISCOUNT,sale.getCartDiscount());
		val.put(ORDER_COLUMN_ORDER_KEY,sale.getOrderKey());
		val.put(ORDER_COLUMN_ORDER_NUMBER_DISCOUNT,sale.getNumberDiscount());
		String where = ORDER_COLUMN_ID + " = ?";
		db.update(ORDER_TABLE_NAME, val, where, new String[]{sale.getOrderId() + ""});
	}

	public List<Order> getAllSales() {
		List<Order> salesList = new ArrayList<Order>();

		Cursor cursor = db.rawQuery("select * from " + ORDER_TABLE_NAME + " order by id desc", null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			salesList.add(makeSale(cursor));
			cursor.moveToNext();
		}

		return salesList;
	}

	public List<Order> getAllUserSales(int userId) {
		List<Order> userSaleList = new ArrayList<Order>();
		List<Order> saleList = getAllSales();
		for (Order d : saleList) {
			if (d.getByUser() == userId)
				userSaleList.add(d);
		}
		return userSaleList;
	}

	public List<Order> getBetween(long from, long to){
		List<Order> saleList = new ArrayList<Order>();
		Cursor cursor = db.rawQuery("select * from "+ ORDER_TABLE_NAME +" where "+ ORDER_COLUMN_ID +" <= "+to+" and "+ ORDER_COLUMN_ID +" >= "+from,null);
		//Cursor cursor = db.rawQuery("select * from "+ORDER_DETAILS_TABLE_NAME+" where "+ORDER_COLUMN_ORDERDATE+" <= "+to+" and "+ORDER_COLUMN_ORDERDATE +" >= "+from,null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			saleList.add(makeSale(cursor));
			cursor.moveToNext();
		}

		return saleList;
	}
    public List<Order> getBetweenTwoSalesForClosingReport(long from, long to){
        List<Order> saleList = new ArrayList<Order>();
        Cursor cursor = db.rawQuery("select * from "+ ORDER_TABLE_NAME +" where "+ ORDER_COLUMN_ID +" <= "+to+" and "+ ORDER_COLUMN_ID +" > "+from,null);
        //Cursor cursor = db.rawQuery("select * from "+ORDER_DETAILS_TABLE_NAME+" where "+ORDER_COLUMN_ORDERDATE+" <= "+to+" and "+ORDER_COLUMN_ORDERDATE +" >= "+from,null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            saleList.add(makeSale(cursor));
            cursor.moveToNext();
        }

        return saleList;
    }

	public List<Order> getBetweenTwoDates(long from, long to){
		List<Order> orderList = new ArrayList<Order>();

		Cursor cursor = db.rawQuery("select * from " + ORDER_TABLE_NAME + " where " + ORDER_COLUMN_ORDERDATE + " between datetime("+from+"/1000, 'unixepoch') and datetime("+to+"/1000, 'unixepoch')", null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			if (cursor.getInt(cursor.getColumnIndex(ORDER_COLUMN_STATUS)) < 1)
				orderList.add(makeSale(cursor));
			cursor.moveToNext();
		}
		return orderList;
	}

	public List<Order> search(String str,int offset,int count){
		List<Order> orderList = new ArrayList<Order>();
		String price = "";
		try{
			price = " or " + ORDER_COLUMN_TOTALPRICE + "=" + Integer.parseInt(str);
		} catch (Exception e){}


		Cursor cursor = db.rawQuery("select * from " + ORDER_TABLE_NAME + " where " + ORDER_COLUMN_CUSTOMER_NAME + " like '%" + str + "%'" +
				price +
						" or " + ORDER_COLUMN_ORDERDATE + " like '%" + str + "%'"+" or " + ORDER_COLUMN_ID + " like '%" + str + "%'"+
				" and " + ORDER_COLUMN_STATUS + " < 1" +
				" order by id desc limit " + offset + "," + count, null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			orderList.add(makeSale(cursor));
			cursor.moveToNext();
		}
		return orderList;
	}

	public List<Order> lazyLoad(int offset,int count){
		List<Order> orderList = new ArrayList<Order>();

		Cursor cursor = db.rawQuery("select * from " + ORDER_TABLE_NAME + " where " +
				ORDER_COLUMN_STATUS + " < 1" +
				" order by id desc limit " + offset + "," + count, null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			orderList.add(makeSale(cursor));
			cursor.moveToNext();
		}
		return orderList;
	}
	/*public List<Order> getListBetweenTwoDates(long from, long to,List<Order>orderList){
		List<Order>orders=new ArrayList<Order>() ;

		for (int i=0;i<orderList.size();i++){
			if(orderList.get(i).getCreatedAt().after(new Timestamp(from))&&orderList.get(i).getCreatedAt().before(new Timestamp(to))){
				orders.add(orderList.get(i));
			}
		}
		Log.d("order",orderList.toString());
		return orders;
	}*/

	public Order getLast(){
		Order sale = null;
		Cursor cursor = db.rawQuery("select * from " + ORDER_TABLE_NAME + " where id like '"+ SESSION.POS_ID_NUMBER+"%' order by id desc", null);

		if (cursor.getCount() < 1) // don`t have any sale yet
		{
			cursor.close();
			return sale;
		}
		cursor.moveToFirst();
		sale = new Order(makeSale(cursor));
		cursor.close();

		return sale;
	}
	public Order getFirst(){
		Order sale = null;
		Cursor cursor = db.rawQuery("select * from " + ORDER_TABLE_NAME + " where id like '"+ SESSION.POS_ID_NUMBER+"%' order by id asc", null);

		if (cursor.getCount() < 1) // don`t have any sale yet
		{
			cursor.close();
			return sale;
		}
		cursor.moveToFirst();
		sale = new Order(makeSale(cursor));
		cursor.close();

		return sale;
	}

	private Order makeSale(Cursor cursor){
		try {
			return new Order(Long.parseLong(cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_ID))),
					Long.parseLong(cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_BYUSER))),
					Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_ORDERDATE))),
					cursor.getInt(cursor.getColumnIndex(ORDER_COLUMN_REPLACEMENTNOTE)),
					Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_STATUS))),
					cursor.getDouble(cursor.getColumnIndex(ORDER_COLUMN_TOTALPRICE)),
					cursor.getDouble(cursor.getColumnIndex(ORDER_COLUMN_TOTALPAID)),
					Long.parseLong(cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_CUSTOMER_ID))),cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_CUSTOMER_NAME)),cursor.getDouble(cursor.getColumnIndex(ORDER_COLUMN_ORDER_DISCOUNT)),
					cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_ORDER_KEY)),cursor.getDouble(cursor.getColumnIndex(ORDER_COLUMN_ORDER_NUMBER_DISCOUNT)));
		}
		catch (Exception ex){
			return new Order(Long.parseLong(cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_ID))),
					Long.parseLong(cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_BYUSER))),
					Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_ORDERDATE))),
					cursor.getInt(cursor.getColumnIndex(ORDER_COLUMN_REPLACEMENTNOTE)),
					Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_STATUS))),
					cursor.getDouble(cursor.getColumnIndex(ORDER_COLUMN_TOTALPRICE)),
					cursor.getDouble(cursor.getColumnIndex(ORDER_COLUMN_TOTALPAID)),
					0,"",cursor.getDouble(cursor.getColumnIndex(ORDER_COLUMN_ORDER_DISCOUNT)),
					cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_ORDER_KEY)),cursor.getDouble(cursor.getColumnIndex(ORDER_COLUMN_ORDER_NUMBER_DISCOUNT)));
		}

	}
	public static String addColumnReal(String columnName) {
		String dbc = "ALTER TABLE " + ORDER_TABLE_NAME
				+ " add column " + columnName + " REAL default 0.0;";
		return dbc;
	}
	public static String addColumnText(String columnName) {
		String dbc = "ALTER TABLE " + ORDER_TABLE_NAME
				+ " add column " + columnName + " TEXT  DEFAULT '' ;";
		return dbc;
	}
}
