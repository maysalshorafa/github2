package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.OfferCategory;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.sql.Timestamp;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 2/17/2019.
 */

public class OfferCategoryDbAdapter {
    // Table Name
    public static final String OFFER_CATEGORY_TABLE_NAME = "OfferCategory";
    // Column Names
    protected static final String OFFER_CATEGORY_COLUMN_ID = "id";
    protected static final String OFFER_CATEGORY_COLUMN_NAME = "name";
    protected static final String OFFER_CATEGORY_COLUMN_CREATINGDATE = "creatingDate";
    protected static final String OFFER_CATEGORY_COLUMN_BYUSER = "byEmployee";
    protected static final String OFFER_CATEGORY_COLUMN_PRODUCT_ID_LIST = "productIdList";
    public static final String DATABASE_CREATE = "CREATE TABLE OfferCategory ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`name` TEXT NOT NULL , `creatingDate` TIMESTAMP NOT NULL DEFAULT current_timestamp, " +
            "`byEmployee` INTEGER, `productIdList` TEXT NOT NULL, FOREIGN KEY(`byEmployee`) REFERENCES `employees.id` )";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;


    public OfferCategoryDbAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public OfferCategoryDbAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }


    public long insertEntry(String name, long byUser,String productIdList,int branchId) {
            OfferCategory offerCategory = new OfferCategory(Util.idHealth(this.db, OFFER_CATEGORY_TABLE_NAME, OFFER_CATEGORY_COLUMN_ID), name, new Timestamp(System.currentTimeMillis()), productIdList, byUser,branchId);
        sendToBroker(MessageType.ADD_OFFER_CATEGORY, offerCategory, this.context);

        try {
            return insertEntry(offerCategory);
        } catch (SQLException ex) {
            Log.d("OfferCategory insert", "inserting Entry at " + OFFER_CATEGORY_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public long insertEntry(OfferCategory category) {
        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(OFFER_CATEGORY_COLUMN_ID, category.getOfferCategoryId());
        val.put(OFFER_CATEGORY_COLUMN_NAME, category.getName());
        val.put(OFFER_CATEGORY_COLUMN_BYUSER, category.getByEmployee());
        val.put(OFFER_CATEGORY_COLUMN_PRODUCT_ID_LIST, category.getProductsIdList());
        val.put(OFFER_CATEGORY_COLUMN_CREATINGDATE, String.valueOf(category.getCreatedAt()));

        try {

            return db.insert(OFFER_CATEGORY_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("CategoryOffer insert", "inserting Entry at " + OFFER_CATEGORY_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }


}
