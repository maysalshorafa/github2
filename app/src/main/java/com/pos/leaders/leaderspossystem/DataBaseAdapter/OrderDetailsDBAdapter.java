package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.util.ArrayList;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by KARAM on 19/10/2016.
 */

public class OrderDetailsDBAdapter {
	// Table Name
	protected static final String ORDER_DETAILS_TABLE_NAME = "OrderDetails";
	// Column Names
	protected static final String ORDER_DETAILS_COLUMN_ID = "id";
	protected static final String ORDER_DETAILES_COLUMN_PRODUCTID = "product_id";
	protected static final String ORDER_DETAILS_COLUMN_QUANTITY = "quantity";
	protected static final String ORDER_DETAILES_COLUMN_USEROFFER = "userOffer";
	protected static final String ORDER_DETAILS_COLUMN_ORDER_ID = "order_id";
	protected static final String ORDER_DETAILS_COLUMN_PAID_AMOUNT = "paid_amount";
	protected static final String ORDER_DETAILS_COLUMN_UNIT_PRICE = "unit_price";
	protected static final String ORDER_DETAILES_COLUMN_DISCOUNT = "discount";
	protected static final String ORDER_DETAILS_COLUMN_CUSTMER_ASSEST_ID = "custmerAssestID";
	protected static final String ORDER_DETAILES_COLUMN_KEY = "key";


	public static final String DATABASE_CREATE = "CREATE TABLE `OrderDetails` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `userOffer` REAL , `product_id` INTEGER," +
			" `quantity` INTEGER, `order_id` INTEGER, " +
			" '" + ORDER_DETAILS_COLUMN_PAID_AMOUNT + "' REAL , '" + ORDER_DETAILS_COLUMN_UNIT_PRICE + "' REAL, '" + ORDER_DETAILES_COLUMN_DISCOUNT + "' REAL , '" + ORDER_DETAILS_COLUMN_CUSTMER_ASSEST_ID + "' INTEGER , " +
			ORDER_DETAILES_COLUMN_KEY + " TEXT , " +
			"FOREIGN KEY(`product_id`) REFERENCES `products.id`, FOREIGN KEY(`order_id`) REFERENCES `_Order.id` )";
    // Variable to hold the database instance
	private SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DbHelper dbHelper;

	private static boolean isEmpty = true;

	public OrderDetailsDBAdapter(Context context) {
		this.context = context;
		this.dbHelper = new DbHelper(context);
	}

	public OrderDetailsDBAdapter open() throws SQLException {
		this.db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}

	public SQLiteDatabase getDatabaseInstance() {
		return db;
	}

	public long insertEntry(OrderDetails o){
        ContentValues val = new ContentValues();
        val.put(ORDER_DETAILS_COLUMN_ID, o.getOrderDetailsId());
        val.put(ORDER_DETAILES_COLUMN_PRODUCTID, o.getProductId());
        val.put(ORDER_DETAILS_COLUMN_QUANTITY, o.getQuantity());
        val.put(ORDER_DETAILES_COLUMN_USEROFFER, o.getUserOffer());
        val.put(ORDER_DETAILS_COLUMN_ORDER_ID, o.getOrderId());
        val.put(ORDER_DETAILS_COLUMN_PAID_AMOUNT, o.getPaidAmount());
        val.put(ORDER_DETAILS_COLUMN_UNIT_PRICE, o.getUnitPrice());
        val.put(ORDER_DETAILES_COLUMN_DISCOUNT, o.getDiscount());
		val.put(ORDER_DETAILS_COLUMN_CUSTMER_ASSEST_ID,o.getCustomer_assistance_id());
		val.put(ORDER_DETAILES_COLUMN_KEY,o.getOrderKey());
		try {
            return db.insert(ORDER_DETAILS_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("ORDER_DETAILS DB insert", "inserting Entry at " + ORDER_DETAILS_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
	}

	public long insertEntry(long productId, int counter, double userOffer, long saleId, double price, double original_price, double discount,long custmerAssestID , String orderDetailsKey) {
		OrderDetails o = new OrderDetails(Util.idHealth(this.db, ORDER_DETAILS_TABLE_NAME, ORDER_DETAILS_COLUMN_ID), productId, counter, userOffer, saleId, price, original_price, discount,custmerAssestID,orderDetailsKey);
		sendToBroker(MessageType.ADD_ORDER_DETAILS, o, this.context);

		try {
			long insertResult = insertEntry(o);
			return insertResult;
		} catch (SQLException ex) {
			Log.e("ORDER_DETAILS DB insertEntry", "inserting Entry at " + ORDER_DETAILS_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}
	public long insertEntryFromInvoice(long productId, int counter, double userOffer, long saleId, double price, double original_price, double discount,long custmerAssestID  , String orderDetailsKey) {
		OrderDetails o = new OrderDetails(Util.idHealth(this.db, ORDER_DETAILS_TABLE_NAME, ORDER_DETAILS_COLUMN_ID), productId, counter, userOffer, saleId, price, original_price, discount,custmerAssestID,orderDetailsKey);
		// sendToBroker(MessageType.ADD_ORDER_DETAILS, o, this.context);

		try {
			long insertResult = insertEntry(o);
			return insertResult;
		} catch (SQLException ex) {
			Log.e("ORDER_DETAILS DB insertEntry", "inserting Entry at " + ORDER_DETAILS_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}

	public List<OrderDetails> getOrderBySaleID(long saleID){
		List<OrderDetails> saleOrderList=new ArrayList<OrderDetails>();
		Cursor cursor =  db.rawQuery( "select * from "+ ORDER_DETAILS_TABLE_NAME +" where "+ ORDER_DETAILS_COLUMN_ORDER_ID +"="+saleID, null );
		cursor.moveToFirst();

		while(!cursor.isAfterLast()){
			saleOrderList.add(make(cursor));
			cursor.moveToNext();
		}
		return saleOrderList;
	}

	private OrderDetails make(Cursor cursor){
		return new OrderDetails(Long.parseLong(cursor.getString(cursor.getColumnIndex(ORDER_DETAILS_COLUMN_ID))),
				cursor.getLong(cursor.getColumnIndex(ORDER_DETAILES_COLUMN_PRODUCTID)),
				Integer.parseInt(cursor.getString(cursor.getColumnIndex(ORDER_DETAILS_COLUMN_QUANTITY))),
				Double.parseDouble(cursor.getString(cursor.getColumnIndex(ORDER_DETAILES_COLUMN_USEROFFER))),
				Long.parseLong(cursor.getString(cursor.getColumnIndex(ORDER_DETAILS_COLUMN_ORDER_ID))),
				cursor.getDouble(cursor.getColumnIndex(ORDER_DETAILS_COLUMN_PAID_AMOUNT)),
				cursor.getDouble(cursor.getColumnIndex(ORDER_DETAILS_COLUMN_UNIT_PRICE)),
				cursor.getDouble(cursor.getColumnIndex(ORDER_DETAILES_COLUMN_DISCOUNT)),
				cursor.getLong(cursor.getColumnIndex(ORDER_DETAILS_COLUMN_CUSTMER_ASSEST_ID)),
				cursor.getString(cursor.getColumnIndex(ORDER_DETAILES_COLUMN_KEY)));
	}
	public static String addColumnReal(String columnName) {
		String dbc = "ALTER TABLE " + ORDER_DETAILS_TABLE_NAME
				+ " add column " + columnName + " REAL default 0.0;";
		return dbc;
	}
	public static String addColumnText(String columnName) {
		String dbc = "ALTER TABLE " + ORDER_DETAILS_TABLE_NAME
				+ " add column " + columnName + " TEXT  DEFAULT '' ;";
		return dbc;
	}
}
