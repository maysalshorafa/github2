package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Models.Sale;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by KARAM on 19/10/2016.
 */

public class SaleDBAdapter {
	protected static final String SALES_TABLE_NAME = "sales";
	// Column Names
	protected static final String SALES_COLUMN_ID = "id";
	protected static final String SALES_COLUMN_BYUSER = "byUser";
	protected static final String SALES_COLUMN_SALEDATE = "saleDate";
	protected static final String SALES_COLUMN_REPLACEMENTNOTE = "replacementNote";
	protected static final String SALES_COLUMN_CANCELED = "canceled";
	protected static final String SALES_COLUMN_TOTALPRICE = "totalPrice";
	protected static final String SALES_COLUMN_TOTALPAID = "totalPaid";
	protected static final String SALES_COLUMN__custmer_id = "custmer_id";
	protected static final String SALES_COLUMN__custmer_name = "custmer_name";


	public static final String DATABASE_CREATE = "CREATE TABLE sales ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, `byUser` INTEGER, `saleDate` TEXT DEFAULT current_timestamp, " +
			"`replacementNote` INTEGER DEFAULT 0, `canceled` INTEGER DEFAULT 0, totalPrice REAL, totalPaid REAL, custmer_id  INTEGER DEFAULT 0 ,custmer_name  TEXT, " +
			"FOREIGN KEY(`byUser`) REFERENCES `users.id`)";
	// Variable to hold the database instance
	private SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DbHelper dbHelper;

	public SaleDBAdapter(Context context) {
		this.context = context;
		this.dbHelper = new DbHelper(context);
	}

	public SaleDBAdapter open() throws SQLException {
		this.db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}

	public SQLiteDatabase getDatabaseInstance() {
		return db;
	}



	public long insertEntry(long byUser, Date saleDate, int replacementNote, boolean canceled, double totalPrice,double totalPaid,long custmer_id,String custmer_name) {

		Sale sale = new Sale(Util.idHealth(this.db, SALES_TABLE_NAME, SALES_COLUMN_ID), byUser, saleDate, replacementNote, canceled, totalPrice, totalPaid, custmer_id, custmer_name);

		sendToBroker(MessageType.ADD_SALE, sale, this.context);

		try {
			return insertEntry(sale);
		} catch (SQLException ex) {
			Log.e("Sales DB insert", "inserting Entry at " + SALES_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}

	public long insertEntry(Sale sale ,long _custmer_id,String custmer_name) {
		return insertEntry(sale.getByUser(), sale.getSaleDate(), sale.getReplacementNote(), sale.isCancelling(), sale.getTotalPrice(),sale.getTotalPaid(),_custmer_id,custmer_name);
	}
	public long insertEntry(Sale sale){
		ContentValues val = new ContentValues();
		val.put(SALES_COLUMN_ID,sale.getId());
		//Assign values for each row.
		val.put(SALES_COLUMN_BYUSER, sale.getByUser());
		val.put(SALES_COLUMN_SALEDATE, sale.getSaleDate().getTime());
		val.put(SALES_COLUMN_REPLACEMENTNOTE, sale.getReplacementNote());
		val.put(SALES_COLUMN_CANCELED, sale.isCancelling()?1:0);
		val.put(SALES_COLUMN_TOTALPRICE, sale.getTotalPrice());
		val.put(SALES_COLUMN_TOTALPAID, sale.getTotalPaid());
		val.put(SALES_COLUMN__custmer_id, sale.getCustomer_id());
		val.put(SALES_COLUMN__custmer_name, sale.getCustomer_name());

		try {
			return db.insert(SALES_TABLE_NAME, null, val);
		} catch (SQLException ex) {
			Log.e("Sales DB insert", "inserting Entry at " + SALES_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}

	public Sale getSaleByID(long id) {
		Sale sale = null;
		Cursor cursor = db.rawQuery("select * from " + SALES_TABLE_NAME + " where id='" + id + "'", null);
		if (cursor.getCount() < 1) // UserName Not Exist
		{
			cursor.close();
			return sale;
		}
		cursor.moveToFirst();
		sale = new Sale(makeSale(cursor));
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

	public void updateEntry(Sale sale) {
		ContentValues val = new ContentValues();
		//Assign values for each row.
		val.put(SALES_COLUMN_BYUSER, sale.getByUser());
		val.put(SALES_COLUMN_SALEDATE, sale.getSaleDate().getTime());
		val.put(SALES_COLUMN_REPLACEMENTNOTE, sale.getReplacementNote());
		val.put(SALES_COLUMN_CANCELED, sale.isCancelling());
		val.put(SALES_COLUMN_TOTALPRICE, sale.getTotalPrice());

		String where = SALES_COLUMN_ID + " = ?";
		db.update(SALES_TABLE_NAME, val, where, new String[]{sale.getId() + ""});
	}

	public List<Sale> getAllSales() {
		List<Sale> salesList = new ArrayList<Sale>();

		Cursor cursor = db.rawQuery("select * from " + SALES_TABLE_NAME + " order by id desc", null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			salesList.add(makeSale(cursor));
			cursor.moveToNext();
		}

		return salesList;
	}

	public List<Sale> getAllUserSales(int userId) {
		List<Sale> userSaleList = new ArrayList<Sale>();
		List<Sale> saleList = getAllSales();
		for (Sale d : saleList) {
			if (d.getByUser() == userId)
				userSaleList.add(d);
		}
		return userSaleList;
	}

	public List<Sale> getBetween(long from, long to){
		List<Sale> saleList = new ArrayList<Sale>();
		Cursor cursor = db.rawQuery("select * from "+SALES_TABLE_NAME+" where "+SALES_COLUMN_ID+" <= "+to+" and "+SALES_COLUMN_ID +" >= "+from,null);
		//Cursor cursor = db.rawQuery("select * from "+SALES_TABLE_NAME+" where "+SALES_COLUMN_SALEDATE+" <= "+to+" and "+SALES_COLUMN_SALEDATE +" >= "+from,null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			saleList.add(makeSale(cursor));
			cursor.moveToNext();
		}

		return saleList;
	}

	public List<Sale> getBetweenTwoDates(long from, long to){
		List<Sale> saleList = new ArrayList<Sale>();
		Cursor cursor = db.rawQuery("select * from "+SALES_TABLE_NAME+" where "+SALES_COLUMN_SALEDATE+" <= "+to+" and "+SALES_COLUMN_SALEDATE +" >= "+from,null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			if (cursor.getInt(cursor.getColumnIndex(SALES_COLUMN_CANCELED)) < 1)
				saleList.add(makeSale(cursor));
			cursor.moveToNext();
		}

		return saleList;
	}

	public Sale getLast(){
		Sale sale = null;
		Cursor cursor = db.rawQuery("select * from " + SALES_TABLE_NAME + " order by id desc", null);

		if (cursor.getCount() < 1) // don`t have any sale yet
		{
			cursor.close();
			return sale;
		}
		cursor.moveToFirst();
		sale = new Sale(makeSale(cursor));
		cursor.close();

		return sale;
	}

	private Sale makeSale(Cursor cursor){
		try {
			return new Sale(Long.parseLong(cursor.getString(cursor.getColumnIndex(SALES_COLUMN_ID))),
					Integer.parseInt(cursor.getString(cursor.getColumnIndex(SALES_COLUMN_BYUSER))),
					DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(SALES_COLUMN_SALEDATE))),
					cursor.getInt(cursor.getColumnIndex(SALES_COLUMN_REPLACEMENTNOTE)),
					Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(SALES_COLUMN_CANCELED))),
					cursor.getDouble(cursor.getColumnIndex(SALES_COLUMN_TOTALPRICE)),
					cursor.getDouble(cursor.getColumnIndex(SALES_COLUMN_TOTALPAID)),
					Integer.parseInt(cursor.getString(cursor.getColumnIndex(SALES_COLUMN__custmer_id))),cursor.getString(cursor.getColumnIndex(SALES_COLUMN__custmer_name)));
		}
		catch (Exception ex){
			return new Sale(Long.parseLong(cursor.getString(cursor.getColumnIndex(SALES_COLUMN_ID))),
					Integer.parseInt(cursor.getString(cursor.getColumnIndex(SALES_COLUMN_BYUSER))),
					DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(SALES_COLUMN_SALEDATE))),
					cursor.getInt(cursor.getColumnIndex(SALES_COLUMN_REPLACEMENTNOTE)),
					Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(SALES_COLUMN_CANCELED))),
					cursor.getDouble(cursor.getColumnIndex(SALES_COLUMN_TOTALPRICE)),
					cursor.getDouble(cursor.getColumnIndex(SALES_COLUMN_TOTALPAID)),
					0,"");
		}

	}
}
