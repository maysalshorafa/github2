package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.R;
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
	protected static final String ORDER_COLUMN_CANCELING_ORDER_ID = "cancellingOrderId";
	protected static final String ORDER_COLUMN_SALES_BEFORE_TAX = "salesBeforeTax";
	protected static final String ORDER_COLUMN_SALES_WITH_TAX = "salesWithTax";


	public static final String DATABASE_CREATE = "CREATE TABLE _Order( `id` INTEGER PRIMARY KEY AUTOINCREMENT, `byEmployee` INTEGER, `order_date` TIMESTAMP DEFAULT current_timestamp, " +
			"`replacementNote` INTEGER DEFAULT 0, `status` INTEGER DEFAULT 0, total_price REAL, total_paid_amount REAL, salesBeforeTax REAL, salesWithTax REAL, customerId  INTEGER DEFAULT 0 ,customer_name  TEXT,cartDiscount REAL DEFAULT 0.0, key  TEXT , numberDiscount REAL DEFAULT 0.0, cancellingOrderId INTEGER DEFAULT 0," +
			"FOREIGN KEY(`byEmployee`) REFERENCES `employees.id`)";
    public static final String DATABASE_UPDATE_FROM_V2_TO_V3[] = {"alter table _Order rename to _Order_v3;", DATABASE_CREATE + "; ",
            "insert into _Order (id,byEmployee,order_date,replacementNote,status,total_price,total_paid_amount,customerId,customer_name,cartDiscount,key,numberDiscount,cancellingOrderId,) " +
                    "select id,byEmployee,order_date,replacementNote,status,total_price,total_paid_amount,customerId,customer_name,cartDiscount,key,numberDiscount,0 from _Order_v3;"};

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



	public long insertEntry(long byUser, Timestamp saleDate, int replacementNote, boolean canceled, double totalPrice, double totalPaid, long custmer_id, String custmer_name,double cartDiscount,double numberDiscount,long cancellingOrderId,double salesBeforeTax,
							double salesWithTax) {
		if(db.isOpen()){

		}else {
			try {
				open();
			}
			catch (SQLException ex) {
				Log.d("Exception",ex.toString());
			}
		}
		Generators key = new Generators().setLength(6);
		Order order = new Order(Util.idHealth(this.db, ORDER_TABLE_NAME, ORDER_COLUMN_ID), byUser, saleDate, replacementNote, canceled, totalPrice, totalPaid, custmer_id, custmer_name,cartDiscount,key.generate(),numberDiscount,cancellingOrderId,salesBeforeTax,salesWithTax);

		sendToBroker(MessageType.ADD_ORDER, order, this.context);

		try {
			close();
			return insertEntry(order);
		} catch (SQLException ex) {
			Log.e("Sales DB insert", "inserting Entry at " + ORDER_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}
	public long invoiceInsertEntry(long byUser, Timestamp saleDate, int replacementNote, boolean canceled, double totalPrice, double totalPaid, long custmer_id, String custmer_name,double cartDiscount,double numberDiscount) {
		if(db.isOpen()){

		}else {
			try {
				open();
			}
			catch (SQLException ex) {
				Log.d("Exception",ex.toString());
			}
		}
		Generators key = new Generators().setLength(6);
		Order order = new Order(Util.idHealth(this.db, ORDER_TABLE_NAME, ORDER_COLUMN_ID), byUser, saleDate, replacementNote, canceled, totalPrice, totalPaid, custmer_id, custmer_name,cartDiscount,key.generate(),numberDiscount);

		//    sendToBroker(MessageType.ADD_ORDER, order, this.context);

		try {
			close();
			return insertEntry(order);
		} catch (SQLException ex) {
			Log.e("Sales DB insert", "inserting Entry at " + ORDER_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}


	public long insertEntry(Order order , long _custmer_id, String custmer_name,boolean invoiceStatus,double salesBeforeTax,double salesWithTax) {
		if(db.isOpen()){

		}else {
			try {
				open();
			}
			catch (SQLException ex) {
				Log.d("Exception",ex.toString());
			}
		}
		if(!invoiceStatus){
			close();
			return insertEntry(order.getByUser(), order.getCreatedAt(), order.getReplacementNote(), order.isStatus(), order.getTotalPrice(),order.getTotalPaidAmount(),_custmer_id,custmer_name,order.getCartDiscount(),order.getNumberDiscount(),order.getCancellingOrderId(),salesBeforeTax,salesWithTax);
		}
		close();
		return invoiceInsertEntry(order.getByUser(), order.getCreatedAt(), order.getReplacementNote(), order.isStatus(), order.getTotalPrice(),order.getTotalPaidAmount(),_custmer_id,custmer_name,order.getCartDiscount(),order
				.getNumberDiscount());

	}
	public long insertEntry(Order order){
		if(db.isOpen()){

		}else {
			try {
				open();
			}
			catch (SQLException ex) {
				Log.d("Exception",ex.toString());
			}
		}
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
		val.put(ORDER_COLUMN_CANCELING_ORDER_ID,order.getCancellingOrderId());
		val.put(ORDER_COLUMN_SALES_BEFORE_TAX,order.getSalesBeforeTax());
		val.put(ORDER_COLUMN_SALES_WITH_TAX,order.getSalesWithTax());

		try {
			return db.insert(ORDER_TABLE_NAME, null, val);
		} catch (SQLException ex) {
			Log.e("Order DB insert", "inserting Entry at " + ORDER_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}
	public long insertEntryDuplicate(Order order){
		if(db.isOpen()){

		}else {
			try {
				open();
			}
			catch (SQLException ex) {
				Log.d("Exception",ex.toString());
			}
		}
		ContentValues val = new ContentValues();
		val.put(ORDER_COLUMN_ID,Util.idHealth(this.db, ORDER_TABLE_NAME, ORDER_COLUMN_ID));
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
		val.put(ORDER_COLUMN_CANCELING_ORDER_ID,order.getCancellingOrderId());
		val.put(ORDER_COLUMN_SALES_BEFORE_TAX,order.getSalesBeforeTax());
		val.put(ORDER_COLUMN_SALES_WITH_TAX,order.getSalesWithTax());

		try {
			sendToBroker(MessageType.ADD_ORDER, order, this.context);
			return db.insert(ORDER_TABLE_NAME, null, val);
		} catch (SQLException ex) {
			Log.e("Order DB insert", "inserting Entry at " + ORDER_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}
	public Order getOrderById(long id) {
		if(db.isOpen()){

		}else {
			try {
				open();
			}
			catch (SQLException ex) {
				Log.d("Exception",ex.toString());
			}
		}
		Order order = null;
		Cursor cursor = db.rawQuery("select * from " + ORDER_TABLE_NAME + " where id='" + id + "'", null);
		if (cursor.getCount() < 1) // UserName Not Exist
		{
			cursor.close();
			close();
			return order;
		}
		cursor.moveToFirst();
		order = new Order(makeSale(cursor));
		cursor.close();
		close();

		return order;
	}

	public long deleteEntry(long id) {
		if(db.isOpen()){

		}else {
			try {
				open();
			}
			catch (SQLException ex) {
				Log.d("Exception",ex.toString());
			}
		}
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
			close();
			return 1;
		} catch (SQLException ex) {
			Log.e("sales DB deleteEntry", "enable hide Entry at " + ORDER_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}

	public void updateEntry(Order sale) {
		if(db.isOpen()){

		}else {
			try {
				open();
			}
			catch (SQLException ex) {
				Log.d("Exception",ex.toString());
			}
		}
		ContentValues val = new ContentValues();
		//Assign values for each row.
		val.put(ORDER_COLUMN_BYUSER, sale.getByUser());
		val.put(ORDER_COLUMN_REPLACEMENTNOTE, sale.getReplacementNote());
		val.put(ORDER_COLUMN_STATUS, sale.isStatus());
		val.put(ORDER_COLUMN_TOTALPRICE, sale.getTotalPrice());
		val.put(ORDER_COLUMN_ORDER_DISCOUNT,sale.getCartDiscount());
		val.put(ORDER_COLUMN_ORDER_KEY,sale.getOrderKey());
		val.put(ORDER_COLUMN_ORDER_NUMBER_DISCOUNT,sale.getNumberDiscount());
		val.put(ORDER_COLUMN_CANCELING_ORDER_ID,sale.getCancellingOrderId());
		val.put(ORDER_COLUMN_ORDERDATE, String.valueOf(new Timestamp(System.currentTimeMillis())));
		val.put(ORDER_COLUMN_SALES_BEFORE_TAX,sale.getSalesBeforeTax());
		val.put(ORDER_COLUMN_SALES_WITH_TAX,sale.getSalesWithTax());
		String where = ORDER_COLUMN_ID + " = ?";
		db.update(ORDER_TABLE_NAME, val, where, new String[]{sale.getOrderId() + ""});
		OrderDBAdapter orderDBAdapter =new OrderDBAdapter(context);
		orderDBAdapter.open();
		Order a= orderDBAdapter.getOrderById(sale.getOrderId());
		a.setCancellingOrderId(sale.getCancellingOrderId());
		sendToBroker(MessageType.UPDATE_ORDER,a,context);
		close();
	}

	public List<Order> getAllSales() {
		if(db.isOpen()){

		}else {
			try {
				open();
			}
			catch (SQLException ex) {
				Log.d("Exception",ex.toString());
			}
		}
		List<Order> salesList = new ArrayList<Order>();

		Cursor cursor = db.rawQuery("select * from " + ORDER_TABLE_NAME + " order by id desc", null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			salesList.add(makeSale(cursor));
			cursor.moveToNext();
			close();
		}
		close();

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
		try {
			if(db.isOpen()){

			}else {
				try {
					open();
				}
				catch (SQLException ex) {
					Log.d("Exception",ex.toString());
				}
			}
		Cursor cursor = db.rawQuery("select * from "+ ORDER_TABLE_NAME +" where "+ ORDER_COLUMN_ID +" <= "+to+" and "+ ORDER_COLUMN_ID +" >= "+from+ " and id like '%"+SESSION.POS_ID_NUMBER+"%'",null);
		//Cursor cursor = db.rawQuery("select * from "+ORDER_DETAILS_TABLE_NAME+" where "+ORDER_COLUMN_ORDERDATE+" <= "+to+" and "+ORDER_COLUMN_ORDERDATE +" >= "+from,null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			saleList.add(makeSale(cursor));
			cursor.moveToNext();

			Log.d("test","test");
		}
		Log.d("test2",saleList.toString()+"list");

			close();

		} catch (Exception e) {
			Log.d("exception",e.toString());

		}
		return saleList;
	}

	public List<Order> getBetweenByOrder(long from, long to, List<Long> idOrder){
		List<Order> saleList = new ArrayList<Order>();

       for (int i=0; i<idOrder.size();i++){
		try {
			if(db.isOpen()){

			}else {
				try {
					open();
				}
				catch (SQLException ex) {
					Log.d("Exception",ex.toString());
				}
			}

			Cursor cursor =db.rawQuery("select * from "+ ORDER_TABLE_NAME +" where "+ ORDER_COLUMN_ID +"="+idOrder.get(i)+" and "+ ORDER_COLUMN_ORDERDATE +" between datetime("+from+"/1000, 'unixepoch') and datetime("+to+"/1000, 'unixepoch')",null);
			//Cursor cursor = db.rawQuery("select * from "+ ORDER_TABLE_NAME +" where "+ ORDER_COLUMN_ID +"="+idOrder.get(i),null);
			//Cursor cursor = db.rawQuery("select * from "+ORDER_DETAILS_TABLE_NAME+" where "+ORDER_COLUMN_ORDERDATE+" <= "+to+" and "+ORDER_COLUMN_ORDERDATE +" >= "+from,null);

			Log.d("iii", "jjj");
			while (cursor.moveToNext()){
				saleList.add(makeSale(cursor));
				Log.d("Ddddfffd", String.valueOf(makeSale(cursor)));
			}

     close();


		} catch (Exception e) {
Log.d("eee",e.toString());
		}}
		return saleList;
	}

	public List<Order> getBetweenOrder(long from, long to){
		List<Order> saleList = new ArrayList<Order>();

		if(db.isOpen()){

		}else {
			try {
				open();
			}
			catch (SQLException ex) {
				Log.d("Exception",ex.toString());
			}
		}
			try {

				Cursor cursor =db.rawQuery("select * from "+ ORDER_TABLE_NAME +" where "+ ORDER_COLUMN_ORDERDATE +" between datetime("+from+"/1000, 'unixepoch') and datetime("+to+"/1000, 'unixepoch')",null);
				//Cursor cursor = db.rawQuery("select * from "+ ORDER_TABLE_NAME +" where "+ ORDER_COLUMN_ID +"="+idOrder.get(i),null);
				//Cursor cursor = db.rawQuery("select * from "+ORDER_DETAILS_TABLE_NAME+" where "+ORDER_COLUMN_ORDERDATE+" <= "+to+" and "+ORDER_COLUMN_ORDERDATE +" >= "+from,null);

				Log.d("iii", "jjj");
				while (cursor.moveToNext()){
					saleList.add(makeSale(cursor));
					Log.d("Ddddfffd", String.valueOf(makeSale(cursor)));
				}


    close();

			} catch (Exception e) {
				Log.d("exception",e.toString());

			}
		return saleList;
	}
	public List<Order> getBetweenTwoSalesForClosingReport(long from, long to){
		List<Order> saleList = new ArrayList<Order>();
		try {
			if (db.isOpen()){

			}
			else {
				open();
			}
		Cursor cursor = db.rawQuery("select * from "+ ORDER_TABLE_NAME +" where "+ ORDER_COLUMN_ID +" <= "+to+" and "+ ORDER_COLUMN_ID +" > "+from+" and id like '%"+SESSION.POS_ID_NUMBER+"%'",null);
		//Cursor cursor = db.rawQuery("select * from "+ORDER_DETAILS_TABLE_NAME+" where "+ORDER_COLUMN_ORDERDATE+" <= "+to+" and "+ORDER_COLUMN_ORDERDATE +" >= "+from,null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			saleList.add(makeSale(cursor));
			cursor.moveToNext();
		}

  close();
		} catch (Exception e) {
			Log.d("exception",e.toString());

		}
		return saleList;
	}

	public List<Order> getBetweenTwoDates(long from, long to){
		List<Order> orderList = new ArrayList<Order>();
		try {
			if(db.isOpen()){

			}else {
				try {
					open();
				}
				catch (SQLException ex) {
					Log.d("Exception",ex.toString());
				}
			}
		Cursor cursor = db.rawQuery("select * from " + ORDER_TABLE_NAME + " where " + ORDER_COLUMN_ORDERDATE + " between datetime("+from+"/1000, 'unixepoch') and datetime("+to+"/1000, 'unixepoch')", null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			if (cursor.getInt(cursor.getColumnIndex(ORDER_COLUMN_STATUS)) < 1)
				orderList.add(makeSale(cursor));
			cursor.moveToNext();
		}

   close();

		} catch (Exception e) {
			Log.d("exception",e.toString());

		}
		return orderList;
	}


	public List<Order> search(String hint,int from,int count,String type){
		List<Order> orderList = new ArrayList<Order>();
		Cursor cursor=null;
		try {
			if(db.isOpen()){

			}else {
				try {
					open();
				}
				catch (SQLException ex) {
					Log.d("Exception",ex.toString());
				}
			}
		if(type.equals(context.getString(R.string.price))){

			cursor = db.rawQuery("select * from " + ORDER_TABLE_NAME + " where " + ORDER_COLUMN_TOTALPRICE + " like '%" +
					hint + "%' " +" and " +" id like '%"+SESSION.POS_ID_NUMBER+"%'"+
					" order by id desc limit " + from + "," + count, null);

		}else if(type.equals(context.getString(R.string.customer))){

					cursor = db.rawQuery("select * from " + ORDER_TABLE_NAME + " where " + ORDER_COLUMN_CUSTOMER_NAME + " like '%" +
							hint + "%' "+" and " +" id like '%"+SESSION.POS_ID_NUMBER+"%'"+
							" order by id desc limit " + from + "," + count, null);

		} else if(type.equals(context.getString(R.string.date))){

			cursor = db.rawQuery("select * from " + ORDER_TABLE_NAME + " where " + ORDER_COLUMN_ORDERDATE + " like '%" +
					hint + "%' "+" and " +" id like '%"+SESSION.POS_ID_NUMBER+"%'"+
					" order by id desc limit " + from + "," + count, null);
		}
		else if(type.equals(context.getString(R.string.sale_id))){

			cursor = db.rawQuery("select * from " + ORDER_TABLE_NAME + " where " + ORDER_COLUMN_ID + " like '%" +
					hint + "%' "+" and " +" id like '%"+SESSION.POS_ID_NUMBER+"%'"+
					" order by id desc limit " + from + "," + count, null);
		}else if(type.equals(context.getString(R.string.all))){
			cursor = db.rawQuery("select * from " + ORDER_TABLE_NAME + " where " +" id like '%"+SESSION.POS_ID_NUMBER+"%'"+
					" order by id desc limit " + from + "," + count, null);
		}

		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			orderList.add(makeSale(cursor));
			cursor.moveToNext();
		}

       close();

		} catch (Exception e) {
			Log.d("exception",e.toString());

		}
		return orderList;
	}

	public List<Order> lazyLoad(int offset,int count){
		List<Order> orderList = new ArrayList<Order>();
		try {
			if(db.isOpen()){

			}else {
				try {
					open();
				}
				catch (SQLException ex) {
					Log.d("Exception",ex.toString());
				}
			}
		Cursor cursor = db.rawQuery("select * from " + ORDER_TABLE_NAME + " where " +" id like '%"+SESSION.POS_ID_NUMBER+"%'"+
				" order by id desc limit " + offset + "," + count, null);
		cursor.moveToFirst();


		while (!cursor.isAfterLast()) {
			orderList.add(makeSale(cursor));
			cursor.moveToNext();
		}

   close();


		} catch (Exception e) {
			Log.d("exception",e.toString());

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
		if(db.isOpen()){

		}else {
			try {
				open();
			}
			catch (SQLException ex) {
				Log.d("Exception",ex.toString());
			}
		}
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
		close();
		Log.d("LasstSlae",sale.toString());
		return sale;
	}
	public Order getFirst(){
		if(db.isOpen()){

		}else {
			try {
				open();
			}
			catch (SQLException ex) {
				Log.d("Exception",ex.toString());
			}
		}
		Order sale = null;
		Cursor cursor = db.rawQuery("select * from " + ORDER_TABLE_NAME + " where id like '"+ SESSION.POS_ID_NUMBER+"%' order by id asc", null);

		if (cursor.getCount() < 1) // don`t have any sale yet
		{
			cursor.close();
			close();
			return sale;
		}
		cursor.moveToFirst();
		sale = new Order(makeSale(cursor));
		cursor.close();
       close();
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
					cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_ORDER_KEY)),cursor.getDouble(cursor.getColumnIndex(ORDER_COLUMN_ORDER_NUMBER_DISCOUNT)),
					Long.parseLong(cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_CANCELING_ORDER_ID)))
					,
					cursor.getDouble(cursor.getColumnIndex(ORDER_COLUMN_SALES_BEFORE_TAX)),
					cursor.getDouble(cursor.getColumnIndex(ORDER_COLUMN_SALES_WITH_TAX))
			);
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
					cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_ORDER_KEY)),cursor.getDouble(cursor.getColumnIndex(ORDER_COLUMN_ORDER_NUMBER_DISCOUNT)),
					Long.parseLong(cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_CANCELING_ORDER_ID)))
					,
					cursor.getDouble(cursor.getColumnIndex(ORDER_COLUMN_SALES_BEFORE_TAX))
					,
					cursor.getDouble(cursor.getColumnIndex(ORDER_COLUMN_SALES_WITH_TAX)));
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
	public static String addColumnLong(String columnName) {
		String dbc = "ALTER TABLE " + ORDER_TABLE_NAME
				+ " add column " + columnName + " INTEGER  DEFAULT '' ;";
		return dbc;
	}
}

