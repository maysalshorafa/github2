package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.OfferCategory;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
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
    protected static final String OFFER_CATEGORY_COLUMN_HIDE = "hide";

    public static final String DATABASE_CREATE = "CREATE TABLE OfferCategory ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`name` TEXT NOT NULL , `creatingDate` TIMESTAMP NOT NULL DEFAULT current_timestamp, " +
            "`byEmployee` INTEGER, `productIdList` TEXT NOT NULL, branchId INTEGER DEFAULT 0, hide INTEGER DEFAULT 0 ,FOREIGN KEY(`byEmployee`) REFERENCES `employees.id` )";
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
            OfferCategory offerCategory = new OfferCategory(Util.idHealth(this.db, OFFER_CATEGORY_TABLE_NAME, OFFER_CATEGORY_COLUMN_ID), name, new Timestamp(System.currentTimeMillis()), productIdList, byUser,branchId,false);

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
        try {
            if(dbHelper==null) {
                open();
            }
        Cursor cursor = db.rawQuery("select * from " + OFFER_CATEGORY_TABLE_NAME + " where " +OFFER_CATEGORY_COLUMN_HIDE+"=0  order by " + OFFER_CATEGORY_COLUMN_ID + " desc", null);
            cursor.moveToFirst();

            while(!cursor.isAfterLast()){
                tempOfferCategoryList.add(createOfferCategoryObject(cursor));
                cursor.moveToNext();
            }

        List<String>productIdList=new ArrayList<>();
        for (int i= 0; i<tempOfferCategoryList.size();i++){
                productIdList=tempOfferCategoryList.get(i).getProductsIdList();

            for (int a=0;a<productIdList.size();a++){
                if(String.valueOf(productIdList.get(a).replaceAll("\\s+","")).equals(String.valueOf(productId))){
                    offerCategoryList.add(tempOfferCategoryList.get(i));
                }
            }
            }
            close();
        } catch (Exception e) {
            Log.d("exxx",e.toString());
        }
        return offerCategoryList;

    }

        public OfferCategory createOfferCategoryObject(Cursor cursor) {
        List<String> result = Arrays.asList( cursor.getString(cursor.getColumnIndex(OFFER_CATEGORY_COLUMN_PRODUCT_ID_LIST)).substring(1,  cursor.getString(cursor.getColumnIndex(OFFER_CATEGORY_COLUMN_PRODUCT_ID_LIST)).length() - 1).split(","));
        return new OfferCategory(Long.parseLong(cursor.getString(cursor.getColumnIndex(OFFER_CATEGORY_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(OFFER_CATEGORY_COLUMN_NAME)),
                Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(OFFER_CATEGORY_COLUMN_CREATINGDATE))),result,
                cursor.getLong(cursor.getColumnIndex(OFFER_CATEGORY_COLUMN_BYUSER)),cursor.getInt(cursor.getColumnIndex(OFFER_CATEGORY_COLUMN_BRANCH_ID)),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(OFFER_CATEGORY_COLUMN_HIDE))));
    }
    public long updateEntryBo(OfferCategory offerCategory) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(OFFER_CATEGORY_COLUMN_ID, offerCategory.getOfferCategoryId());
        val.put(OFFER_CATEGORY_COLUMN_NAME, offerCategory.getName());
        val.put(OFFER_CATEGORY_COLUMN_BYUSER, offerCategory.getByEmployee());
        val.put(OFFER_CATEGORY_COLUMN_CREATINGDATE, String.valueOf(offerCategory.getCreatedAt()));
        val.put(OFFER_CATEGORY_COLUMN_BRANCH_ID, offerCategory.getBranchId());
        val.put(OFFER_CATEGORY_COLUMN_PRODUCT_ID_LIST, String.valueOf(offerCategory.getProductsIdList()));
        try {
            String where = OFFER_CATEGORY_COLUMN_ID + " = ?";
            db.update(OFFER_CATEGORY_TABLE_NAME, val, where, new String[]{offerCategory.getOfferCategoryId() + ""});
            return 1;
        } catch (SQLException ex) {
            return 0;
        }
    }
    public OfferCategory getOfferById(long id){
        Cursor cursor = db.rawQuery("select * from " + OFFER_CATEGORY_TABLE_NAME + " where "+OFFER_CATEGORY_COLUMN_ID+"='" + id + "'", null);
        if (cursor.getCount() < 1) // Offer Not Exist
        {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        return createOfferCategoryObject(cursor);
    }
    public long deleteEntryBo(OfferCategory offerCategory) {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(OFFER_CATEGORY_COLUMN_HIDE, 1);

        String where = OFFER_CATEGORY_COLUMN_ID + " = ?";
        try {
            db.update(OFFER_CATEGORY_TABLE_NAME, updatedValues, where, new String[]{offerCategory.getOfferCategoryId() + ""});
            return 1;
        } catch (SQLException ex) {
            Log.e("OfferCategory delete", "enable hide " + OFFER_CATEGORY_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }
    public List<OfferCategory> getAllCategoryOffer() {
        List<OfferCategory> departmentList = new ArrayList<OfferCategory>();
        try {
            if(dbHelper==null) {
                open();
            }
        Cursor cursor=null;
        if(SETTINGS.enableAllBranch) {
            cursor  = db.rawQuery("select * from " + OFFER_CATEGORY_TABLE_NAME + " where " + OFFER_CATEGORY_COLUMN_HIDE + "=0 order by id desc", null);
        }else {
            cursor  = db.rawQuery("select * from " + OFFER_CATEGORY_TABLE_NAME + " where " + OFFER_CATEGORY_COLUMN_BRANCH_ID + " = "+ SETTINGS.branchId+ " and " + OFFER_CATEGORY_COLUMN_HIDE + "=0 order by id desc", null);
        }
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            departmentList.add(createOfferCategoryObject(cursor));
            cursor.moveToNext();
        }
            close();
        } catch (Exception e) {
            Log.d("exxx",e.toString());
        }
        return departmentList;
    }
    public int deleteEntry(long id) {
        OfferCategoryDbAdapter offerCategoryDbAdapter=new OfferCategoryDbAdapter(context);
        offerCategoryDbAdapter.open();
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(OFFER_CATEGORY_COLUMN_HIDE, 1);

        String where = OFFER_CATEGORY_COLUMN_ID + " = ?";
        try {
            db.update(OFFER_CATEGORY_TABLE_NAME, updatedValues, where, new String[]{id + ""});
            OfferCategory offerCategory = offerCategoryDbAdapter.getOfferById(id);
            sendToBroker(MessageType.DELETE_OFFER_CATEGORY, offerCategory, this.context);
            return 1;
        } catch (SQLException ex) {
            Log.e("OfferCategory DB delete", "enable hide Entry at " + OFFER_CATEGORY_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }
    public void updateEntry(OfferCategory offerCategory) {
        OfferCategoryDbAdapter offerCategoryDbAdapter = new OfferCategoryDbAdapter(context);
        offerCategoryDbAdapter.open();
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(OFFER_CATEGORY_COLUMN_ID, offerCategory.getOfferCategoryId());
        val.put(OFFER_CATEGORY_COLUMN_NAME, offerCategory.getName());
        val.put(OFFER_CATEGORY_COLUMN_BYUSER, offerCategory.getByEmployee());
        val.put(OFFER_CATEGORY_COLUMN_CREATINGDATE, String.valueOf(offerCategory.getCreatedAt()));
        val.put(OFFER_CATEGORY_COLUMN_BRANCH_ID, offerCategory.getBranchId());
        val.put(OFFER_CATEGORY_COLUMN_PRODUCT_ID_LIST, String.valueOf(offerCategory.getProductsIdList()));
        String where = OFFER_CATEGORY_COLUMN_ID + " = ?";
        db.update(OFFER_CATEGORY_TABLE_NAME, val, where, new String[]{offerCategory.getOfferCategoryId() + ""});
        OfferCategory d=offerCategoryDbAdapter.getOfferById(offerCategory.getOfferCategoryId());
        Log.d("Update object",d.toString());
        sendToBroker(MessageType.UPDATE_OFFER_CATEGORY, d, this.context);
        offerCategoryDbAdapter.close();
    }
}
