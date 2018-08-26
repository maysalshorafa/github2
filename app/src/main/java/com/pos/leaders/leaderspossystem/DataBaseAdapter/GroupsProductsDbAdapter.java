package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.GroupsProducts;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karam on 8/12/2018.
 */

public class GroupsProductsDbAdapter {
    public static final String GROUPS_PRODUCTS_TABLE_NAME ="groupsProducts";

    private static final String GROUPS_PRODUCTS_COLUMN_ID ="id";
    private static final String GROUPS_PRODUCTS_COLUMN_PRODUCT_SKU ="productSku";
    private static final String GROUPS_PRODUCTS_COLUMN_GROUP_ID ="groupId";

    public static final String DATABASE_CREATE ="CREATE TABLE "+GROUPS_PRODUCTS_TABLE_NAME+ " ( `"+GROUPS_PRODUCTS_COLUMN_ID+"` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`"+ GROUPS_PRODUCTS_COLUMN_PRODUCT_SKU +"` INTEGER NOT NULL, " +
            "`"+ GROUPS_PRODUCTS_COLUMN_GROUP_ID +"` INTEGER NOT NULL);";
    // Variable to hold the database instance
    public SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public GroupsProductsDbAdapter(Context context) {
        this.context = context;
        this.dbHelper=new DbHelper(context);
    }

    public GroupsProductsDbAdapter open() throws SQLException {
        this.db=dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        db.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }

    public long insertEntry(long productSku,long offerGroupId) {
        GroupsProducts groupsProducts = new GroupsProducts(Util.idHealth(this.db, GROUPS_PRODUCTS_TABLE_NAME, GROUPS_PRODUCTS_COLUMN_ID),productSku, offerGroupId);
        try {
            return insertEntry(groupsProducts);
        } catch (SQLException ex) {
            Log.e("GroupsProducts insert", "inserting Entry at " + GROUPS_PRODUCTS_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public long insertEntry(GroupsProducts groupsProducts) {
        ContentValues val = new ContentValues();

        val.put(GROUPS_PRODUCTS_COLUMN_ID, groupsProducts.getId());
        val.put(GROUPS_PRODUCTS_COLUMN_PRODUCT_SKU, groupsProducts.getProductSku());
        val.put(GROUPS_PRODUCTS_COLUMN_GROUP_ID, groupsProducts.getGroupId());

        try {

            return db.insert(GROUPS_PRODUCTS_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("GroupsProducts insert", "inserting Entry at " + GROUPS_PRODUCTS_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }

    public List<Long> getProductsSkusByGroupId(long groupId) {
        List<Long> products = null;

        Cursor cursor = db.rawQuery("select * from " + GROUPS_PRODUCTS_TABLE_NAME + " where " + GROUPS_PRODUCTS_COLUMN_GROUP_ID + "='" + groupId + "' order by desc;", null);

        products = new ArrayList<>();

        if (cursor.getCount() > 0 ) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                products.add(cursor.getLong(cursor.getColumnIndex(GROUPS_PRODUCTS_COLUMN_PRODUCT_SKU)));
                cursor.moveToNext();
            }
        }

        cursor.close();

        return products;
    }

    public List<Long> getGroupsIdByProductSku(String productSku){
        List<Long> groups = null;

        Cursor cursor = db.rawQuery("select * from " + GROUPS_PRODUCTS_TABLE_NAME + " where " + GROUPS_PRODUCTS_COLUMN_PRODUCT_SKU + "='" + productSku + "';", null);

        if (cursor.getCount() > 0 ) {
            cursor.moveToFirst();
            groups = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                groups.add(cursor.getLong(cursor.getColumnIndex(GROUPS_PRODUCTS_COLUMN_GROUP_ID)));
                cursor.moveToNext();
            }
        }
        cursor.close();

        return groups;
    }
    public List<Long> getGroupsIdByProductCategory(long productCategory){
        List<Long> groups = null;

        Cursor cursor = db.rawQuery("select * from " + GROUPS_PRODUCTS_TABLE_NAME + " where " + GROUPS_PRODUCTS_COLUMN_PRODUCT_SKU + "='" + productCategory + "';", null);

        if (cursor.getCount() > 0 ) {
            cursor.moveToFirst();
            groups = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                groups.add(cursor.getLong(cursor.getColumnIndex(GROUPS_PRODUCTS_COLUMN_GROUP_ID)));
                cursor.moveToNext();
            }
        }
        cursor.close();

        return groups;
    }

    public boolean deleteOfferGroup(long offerGroup) {
        return db.delete(GROUPS_PRODUCTS_TABLE_NAME, GROUPS_PRODUCTS_COLUMN_GROUP_ID + "=" + offerGroup, null) > 0;
    }

    private GroupsProducts makeOfferGroup(Cursor cursor) {
        try {
            return new GroupsProducts(Long.parseLong(cursor.getString(cursor.getColumnIndex(GROUPS_PRODUCTS_COLUMN_ID))), Long.parseLong(cursor.getString(cursor.getColumnIndex(GROUPS_PRODUCTS_COLUMN_PRODUCT_SKU))),Long.parseLong(cursor.getString(cursor.getColumnIndex(GROUPS_PRODUCTS_COLUMN_GROUP_ID))));
        } catch (Exception ex) {
            return null;

        }

    }
}
