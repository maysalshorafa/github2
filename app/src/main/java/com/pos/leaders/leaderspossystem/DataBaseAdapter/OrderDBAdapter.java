package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.util.ArrayList;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by KARAM on 19/10/2016.
 */

public class OrderDBAdapter {
	// Table Name
	protected static final String ORDER_TABLE_NAME = "_order";
	// Column Names
	protected static final String ORDER_COLUMN_ID = "id";
	protected static final String ORDER_COLUMN_PRODUCTID = "productId";
	protected static final String ORDER_COLUMN_COUNTER = "counter";
	protected static final String ORDER_COLUMN_USEROFFER = "userOffer";
	protected static final String ORDER_COLUMN_SALEID = "saleId";

	protected static final String ORDER_COLUMN_PRICE = "price";
	protected static final String ORDER_COLUMN_ORIGINAL_PRICE = "original_price";
	protected static final String ORDER_COLUMN_DISCOUNT = "discount";
	protected static final String ORDER_COLUMN_custmerAssestID = "custmerAssestID";


    public static final String DATABASE_CREATE = "CREATE TABLE `_order` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `userOffer` REAL , `productId` INTEGER," +
            " `counter` INTEGER, `saleId` INTEGER, " +
            " '" + ORDER_COLUMN_PRICE + "' REAL , '" + ORDER_COLUMN_ORIGINAL_PRICE + "' REAL, '" + ORDER_COLUMN_DISCOUNT + "' REAL , '" +ORDER_COLUMN_custmerAssestID + "' INTEGER , " +
            "FOREIGN KEY(`productId`) REFERENCES `products.id`, FOREIGN KEY(`saleId`) REFERENCES `sales.id` )";
    // Variable to hold the database instance
	private SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DbHelper dbHelper;

	private static boolean isEmpty = true;

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

	public long insertEntry(long productId, int counter, double userOffer, long saleId) {
		ContentValues val = new ContentValues();
		if(isEmpty){
            val.put(ORDER_COLUMN_ID, Util.idHealth(this.db, ORDER_TABLE_NAME, ORDER_COLUMN_ID));
			isEmpty = false;
		}
		//Assign values for each row.
		val.put(ORDER_COLUMN_PRODUCTID, productId);
		val.put(ORDER_COLUMN_COUNTER, counter);
		val.put(ORDER_COLUMN_USEROFFER, userOffer);
		val.put(ORDER_COLUMN_SALEID, saleId);
		try {
			return db.insert(ORDER_TABLE_NAME, null, val);
		} catch (SQLException ex) {
			Log.e("Order DB insert", "inserting Entry at " + ORDER_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}




	public long insertEntry(Order o){
        ContentValues val = new ContentValues();
        val.put(ORDER_COLUMN_ID, o.getId());
        val.put(ORDER_COLUMN_PRODUCTID, o.getProductId());
        val.put(ORDER_COLUMN_COUNTER, o.getCount());
        val.put(ORDER_COLUMN_USEROFFER, o.getUserOffer());
        val.put(ORDER_COLUMN_SALEID, o.getSaleId());
        val.put(ORDER_COLUMN_PRICE, o.getPrice());
        val.put(ORDER_COLUMN_ORIGINAL_PRICE, o.getOriginal_price());
        val.put(ORDER_COLUMN_DISCOUNT, o.getDiscount());
		val.put(ORDER_COLUMN_custmerAssestID,o.getCustmerAssestId());
        try {
            return db.insert(ORDER_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("Order DB insert", "inserting Entry at " + ORDER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
	}

    public long insertEntry(long productId, int counter, double userOffer, long saleId, double price, double original_price, double discount,long custmerAssestID) {
        Order o = new Order(Util.idHealth(this.db, ORDER_TABLE_NAME, ORDER_COLUMN_ID), productId, counter, userOffer, saleId, price, original_price, discount,custmerAssestID);
        sendToBroker(MessageType.ADD_ORDER, o, this.context);

        try {
            long insertResult = insertEntry(o);
            return insertResult;
        } catch (SQLException ex) {
            Log.e("Order DB insertEntry", "inserting Entry at " + ORDER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

	public Order getOrderByID(long id) {
		Order order = null;
		Cursor cursor = db.rawQuery("select * from " + ORDER_TABLE_NAME + " where id='" + id + "'", null);
		if (cursor.getCount() < 1) // UserName Not Exist
		{
			cursor.close();
			return order;
		}
		cursor.moveToFirst();
        order = new Order(make(cursor));
        cursor.close();

		return order;
	}


	public void updateEntry(Order order) {
		ContentValues val = new ContentValues();
		//Assign values for each row.
		val.put(ORDER_COLUMN_PRODUCTID, order.getProductId());
		val.put(ORDER_COLUMN_COUNTER, order.getCount());
		val.put(ORDER_COLUMN_USEROFFER, order.getUserOffer());
		val.put(ORDER_COLUMN_SALEID, order.getSaleId());

		String where = ORDER_COLUMN_ID + " = ?";
		db.update(ORDER_TABLE_NAME, val, where, new String[]{order.getId() + ""});
	}

	public List<Order> getAllOrders(){
		List<Order> ordersList =new ArrayList<Order>();

		Cursor cursor =  db.rawQuery( "select * from "+ORDER_TABLE_NAME, null );
		cursor.moveToFirst();

		while(!cursor.isAfterLast()){
			ordersList.add(make(cursor));
			cursor.moveToNext();
		}

		return ordersList;
	}

	public List<Order> getOrderBySaleID(long saleID){
		List<Order> saleOrderList=new ArrayList<Order>();
		Cursor cursor =  db.rawQuery( "select * from "+ORDER_TABLE_NAME+" where "+ORDER_COLUMN_SALEID+"="+saleID, null );
		cursor.moveToFirst();

		while(!cursor.isAfterLast()){
			saleOrderList.add(make(cursor));
			cursor.moveToNext();
		}
		return saleOrderList;
	}
	private Order make(Cursor cursor){
		return new Order(Long.parseLong(cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_ID))),
				Long.parseLong(cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_PRODUCTID))),
				Integer.parseInt(cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_COUNTER))),
				Double.parseDouble(cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_USEROFFER))),
				Long.parseLong(cursor.getString(cursor.getColumnIndex(ORDER_COLUMN_SALEID))),
                cursor.getDouble(cursor.getColumnIndex(ORDER_COLUMN_PRICE)),
                cursor.getDouble(cursor.getColumnIndex(ORDER_COLUMN_ORIGINAL_PRICE)),
                cursor.getDouble(cursor.getColumnIndex(ORDER_COLUMN_DISCOUNT)));
	}
}
