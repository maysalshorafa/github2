package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.OfferCategory;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    protected static final String OFFER_CATEGORY_COLUMN_BRANCH_ID = "branchId";

    public static final String DATABASE_CREATE = "CREATE TABLE OfferCategory ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`name` TEXT NOT NULL , `creatingDate` TIMESTAMP NOT NULL DEFAULT current_timestamp, " +
            "`byEmployee` INTEGER, `productIdList` TEXT NOT NULL, branchId INTEGER DEFAULT 0, FOREIGN KEY(`byEmployee`) REFERENCES `employees.id` )";
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


    public long insertEntry(String name, long byUser, List<String> productIdList, int branchId) {
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
        val.put(OFFER_CATEGORY_COLUMN_PRODUCT_ID_LIST, String.valueOf(category.getProductsIdList()));
        val.put(OFFER_CATEGORY_COLUMN_CREATINGDATE, String.valueOf(category.getCreatedAt()));
        val.put(OFFER_CATEGORY_COLUMN_BRANCH_ID,category.getBranchId());

        try {

            return db.insert(OFFER_CATEGORY_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("CategoryOffer insert", "inserting Entry at " + OFFER_CATEGORY_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }
    public List<OfferCategory>getOfferCategoryByProductId(long productId){
            List<OfferCategory> tempOfferCategoryList = new ArrayList<OfferCategory>();
        List<OfferCategory> offerCategoryList = new ArrayList<OfferCategory>();

        Cursor cursor = db.rawQuery("select * from " + OFFER_CATEGORY_TABLE_NAME + " order by " + OFFER_CATEGORY_COLUMN_ID + " desc", null);
            cursor.moveToFirst();

            while(!cursor.isAfterLast()){
                tempOfferCategoryList.add(createOfferCategoryObject(cursor));
                cursor.moveToNext();
            }

        List<String>productIdList=new ArrayList<>();
        for (int i= 0; i<tempOfferCategoryList.size();i++){
                productIdList=tempOfferCategoryList.get(i).getProductsIdList();
            Log.d("tessst11",tempOfferCategoryList.toString()+"  ");

            for (int a=0;a<productIdList.size();a++){

                if(String.valueOf(productIdList.get(a).replaceAll("\\s+","")).equals(String.valueOf(productId))){
                 //   Log.d("tessst",Long.parseLong(productIdList.get(a)) + "   "+String.valueOf(productId));

                    offerCategoryList.add(tempOfferCategoryList.get(i));
                }
            }
            }
        return offerCategoryList;

    }

        public OfferCategory createOfferCategoryObject(Cursor cursor) {
        List<String> result = Arrays.asList( cursor.getString(cursor.getColumnIndex(OFFER_CATEGORY_COLUMN_PRODUCT_ID_LIST)).substring(1,  cursor.getString(cursor.getColumnIndex(OFFER_CATEGORY_COLUMN_PRODUCT_ID_LIST)).length() - 1).split(","));
        return new OfferCategory(Long.parseLong(cursor.getString(cursor.getColumnIndex(OFFER_CATEGORY_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(OFFER_CATEGORY_COLUMN_NAME)),
                Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(OFFER_CATEGORY_COLUMN_CREATINGDATE))),result,
                cursor.getLong(cursor.getColumnIndex(OFFER_CATEGORY_COLUMN_BYUSER)),cursor.getInt(cursor.getColumnIndex(OFFER_CATEGORY_COLUMN_BRANCH_ID)));
    }

}
