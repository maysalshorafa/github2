package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.Offer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KARAM on 23/10/2016.
 */

public class ProductOfferDBAdapter {
	// Table Name
	protected static final String PRODUCTOFFER_TABLE_NAME = "productOffer";
	// Column Names
	protected static final String PRODUCTOFFER_COLUMN_ID = "id";
	protected static final String PRODUCTOFFER_COLUMN_PRODUCTID = "productId";
	protected static final String PRODUCTOFFER_COLUMN_OFFERID = "offerId";

	public static final String DATABASE_CREATE = "CREATE TABLE productOffer ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, `productId` int , `offerId` int ," +
			" FOREIGN KEY(`productId`) REFERENCES `products.id`,FOREIGN KEY(`offerId`) REFERENCES `offers.id`)";
	// Variable to hold the database instance
	private SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DbHelper dbHelper;

	public ProductOfferDBAdapter(Context context) {
		this.context = context;
		this.dbHelper = new DbHelper(context);
	}

	public ProductOfferDBAdapter open() throws SQLException {
		this.db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}

	public SQLiteDatabase getDatabaseInstance() {
		return db;
	}

	public int insertEntry(int productId, int offerId) {
		ContentValues val = new ContentValues();
		//Assign values for each row.
		val.put(PRODUCTOFFER_COLUMN_PRODUCTID, productId);
		val.put(PRODUCTOFFER_COLUMN_OFFERID, offerId);

		try {
			return (int) db.insert(PRODUCTOFFER_TABLE_NAME, null, val);
		} catch (SQLException ex) {
			Log.e("insert into DataBase", "inserting Entry at " + PRODUCTOFFER_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}

	public int insertEntry(List<Product> products, int offerId) {
		int count = 0, i;
		for (Product product : products) {
			i = insertEntry(product.getId(), offerId);
			if (i > 0) {
				count++;
			} else {
				// fail to add this item
				return count;
			}
		}
		// number of all inserted item
		return count + 1;
	}

	public List<Product> getAllProductOffer(Offer offer) {
		List<Product> products = new ArrayList<Product>();
		Cursor cursor = db.rawQuery("select * from " + PRODUCTOFFER_TABLE_NAME + " where " + PRODUCTOFFER_COLUMN_OFFERID + "='" + offer.getId() + "'", null);
		if (cursor.getCount() < 1) // Not Exist
		{
			cursor.close();
			return products;
		}
		cursor.moveToFirst();

		ProductDBAdapter productDBAdapter = new ProductDBAdapter(context);
		productDBAdapter.open();

		while (!cursor.isAfterLast()) {
			products.add(new Product(productDBAdapter.getProductByID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCTOFFER_COLUMN_PRODUCTID))))));
			cursor.moveToNext();
		}
		cursor.close();

		return products;
	}

	public boolean deleteEntry(int id) {
		return db.delete(PRODUCTOFFER_TABLE_NAME, PRODUCTOFFER_COLUMN_ID + "=?", new String[]{id + ""}) > 0;
	}

	public boolean deleteEntry(int productId,int offerId) {
		return db.delete(PRODUCTOFFER_TABLE_NAME , PRODUCTOFFER_COLUMN_PRODUCTID + "=? and " + PRODUCTOFFER_COLUMN_OFFERID + "=? ", new String[]{productId + "", offerId + ""}) > 0;
	}
}
