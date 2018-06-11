package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Order;
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
	protected static final String SALES_TABLE_NAME = "_Order";
	// Column Names
	protected static final String SALES_COLUMN_ID = "id";
	protected static final String SALES_COLUMN_BYUSER = "byUser";
	protected static final String SALES_COLUMN_SALEDATE = "order_date";
	protected static final String SALES_COLUMN_REPLACEMENTNOTE = "replacementNote";
	protected static final String SALES_COLUMN_CANCELED = "status";
	protected static final String SALES_COLUMN_TOTALPRICE = "total_price";
	protected static final String SALES_COLUMN_TOTALPAID = "total_paid_amount";
	protected static final String SALES_COLUMN_CUSTOMER_ID = "customerId";
	protected static final String SALES_COLUMN_CUSTOMER_NAME = "customer_name";


	public static final String DATABASE_CREATE = "CREATE TABLE _Order( `id` INTEGER PRIMARY KEY AUTOINCREMENT, `byUser` INTEGER, `order_date` TIMESTAMP DEFAULT current_timestamp, " +
			"`replacementNote` INTEGER DEFAULT 0, `status` INTEGER DEFAULT 0, total_price REAL, total_paid_amount REAL, customerId  INTEGER DEFAULT 0 ,customer_name  TEXT, " +
			"FOREIGN KEY(`byUser`) REFERENCES `users.id`)";
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



	public long insertEntry(long byUser, Timestamp saleDate, int replacementNote, boolean canceled, double totalPrice, double totalPaid, long custmer_id, String custmer_name) {
        Order sale = new Order(Util.idHealth(this.db, SALES_TABLE_NAME, SALES_COLUMN_ID), byUser, saleDate, replacementNote, canceled, totalPrice, totalPaid, custmer_id, custmer_name);

        sendToBroker(MessageType.ADD_ORDER, sale, this.context);

        try {
            return insertEntry(sale);
        } catch (SQLException ex) {
            Log.e("Sales DB insert", "inserting Entry at " + SALES_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
	}

	public long insertEntry(Order sale , long _custmer_id, String custmer_name) {
		return insertEntry(sale.getByUser(), sale.getCreatedAt(), sale.getReplacementNote(), sale.isStatus(), sale.getTotalPrice(),sale.getTotalPaidAmount(),_custmer_id,custmer_name);
	}
	public long insertEntry(Order sale){
        ContentValues val = new ContentValues();
        val.put(SALES_COLUMN_ID,sale.getOrderId());
        //Assign values for each row.
        val.put(SALES_COLUMN_BYUSER, sale.getByUser());
        val.put(SALES_COLUMN_REPLACEMENTNOTE, sale.getReplacementNote());
        val.put(SALES_COLUMN_CANCELED, sale.isStatus()?1:0);
        val.put(SALES_COLUMN_TOTALPRICE, sale.getTotalPrice());
        val.put(SALES_COLUMN_TOTALPAID, sale.getTotalPaidAmount());
        val.put(SALES_COLUMN_CUSTOMER_ID, sale.getCustomerId());
        val.put(SALES_COLUMN_CUSTOMER_NAME, sale.getCustomer_name());

        try {
            return db.insert(SALES_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("Sales DB insert", "inserting Entry at " + SALES_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

	public Order getSaleByID(long id) {
		Order sale = null;
		Cursor cursor = db.rawQuery("select * from " + SALES_TABLE_NAME + " where id='" + id + "'", null);
		if (cursor.getCount() < 1) // UserName Not Exist
		{
			cursor.close();
			return sale;
		}
		cursor.moveToFirst();
		sale = new Order(makeSale(cursor));
		cursor.close();

		return sale;
	}

	public long deleteEntry(long id) {
		// Define the updated row content.
		ContentValues updatedValues = new ContentValues();
		// Assign values for each row.
		updatedValues.put(SALES_COLUMN_CANCELED, 1);

		String where = SALES_COLUMN_ID + " = ?";
		try {
			return db.update(SALES_TABLE_NAME, updatedValues, where, new String[]{id + ""});
		} catch (SQLException ex) {
			Log.e("sales DB deleteEntry", "enable hide Entry at " + SALES_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}

	public void updateEntry(Order sale) {
		ContentValues val = new ContentValues();
		//Assign values for each row.
		val.put(SALES_COLUMN_BYUSER, sale.getByUser());
		val.put(SALES_COLUMN_REPLACEMENTNOTE, sale.getReplacementNote());
		val.put(SALES_COLUMN_CANCELED, sale.isStatus());
		val.put(SALES_COLUMN_TOTALPRICE, sale.getTotalPrice());

		String where = SALES_COLUMN_ID + " = ?";
		db.update(SALES_TABLE_NAME, val, where, new String[]{sale.getOrderId() + ""});
	}

	public List<Order> getAllSales() {
		List<Order> salesList = new ArrayList<Order>();

		Cursor cursor = db.rawQuery("select * from " + SALES_TABLE_NAME + " order by id desc", null);
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
		Cursor cursor = db.rawQuery("select * from "+SALES_TABLE_NAME+" where "+SALES_COLUMN_ID+" <= "+to+" and "+SALES_COLUMN_ID +" >= "+from,null);
		//Cursor cursor = db.rawQuery("select * from "+SALES_TABLE_NAME+" where "+SALES_COLUMN_SALEDATE+" <= "+to+" and "+SALES_COLUMN_SALEDATE +" >= "+from,null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			saleList.add(makeSale(cursor));
			cursor.moveToNext();
		}

		return saleList;
	}

	public List<Order> getBetweenTwoDates(long from, long to){
		List<Order> saleList = new ArrayList<Order>();
		Cursor cursor = db.rawQuery("select * from "+SALES_TABLE_NAME+" where "+SALES_COLUMN_SALEDATE+" <= "+to+" and "+SALES_COLUMN_SALEDATE +" >= "+from+" order by "+SALES_COLUMN_SALEDATE+" DESC",null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			if (cursor.getInt(cursor.getColumnIndex(SALES_COLUMN_CANCELED)) < 1)
				saleList.add(makeSale(cursor));
			cursor.moveToNext();
		}

		return saleList;
	}

	public Order getLast(){
		Order sale = null;
		Cursor cursor = db.rawQuery("select * from " + SALES_TABLE_NAME + " where id like '"+ SESSION.POS_ID_NUMBER+"%' order by id desc", null);

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
			return new Order(Long.parseLong(cursor.getString(cursor.getColumnIndex(SALES_COLUMN_ID))),
					Long.parseLong(cursor.getString(cursor.getColumnIndex(SALES_COLUMN_BYUSER))),
					Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(SALES_COLUMN_SALEDATE))),
					cursor.getInt(cursor.getColumnIndex(SALES_COLUMN_REPLACEMENTNOTE)),
					Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(SALES_COLUMN_CANCELED))),
					cursor.getDouble(cursor.getColumnIndex(SALES_COLUMN_TOTALPRICE)),
					cursor.getDouble(cursor.getColumnIndex(SALES_COLUMN_TOTALPAID)),
					Long.parseLong(cursor.getString(cursor.getColumnIndex(SALES_COLUMN_CUSTOMER_ID))),cursor.getString(cursor.getColumnIndex(SALES_COLUMN_CUSTOMER_NAME)));
		}
		catch (Exception ex){
			return new Order(Long.parseLong(cursor.getString(cursor.getColumnIndex(SALES_COLUMN_ID))),
					Long.parseLong(cursor.getString(cursor.getColumnIndex(SALES_COLUMN_BYUSER))),
					Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(SALES_COLUMN_SALEDATE))),
					cursor.getInt(cursor.getColumnIndex(SALES_COLUMN_REPLACEMENTNOTE)),
					Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(SALES_COLUMN_CANCELED))),
					cursor.getDouble(cursor.getColumnIndex(SALES_COLUMN_TOTALPRICE)),
					cursor.getDouble(cursor.getColumnIndex(SALES_COLUMN_TOTALPAID)),
					0,"");
		}

	}
}
