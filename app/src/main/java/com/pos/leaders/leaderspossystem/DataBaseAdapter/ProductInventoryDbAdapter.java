package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.ProductInventory;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Win8.1 on 7/21/2019.
 */

public class ProductInventoryDbAdapter {
    // Table Name
    public static final String PRODUCT_INVENTORY_TABLE_NAME = "product_inventory";
    // Column Names
    protected static final String PRODUCT_INVENTORY_COLUMN_ID = "id";
    protected static final String PRODUCT_INVENTORY_COLUMN_PRODUCT_ID = "productId";
    protected static final String PRODUCT_INVENTORY_COLUMN_QTY = "qty";
    protected static final String  PRODUCT_INVENTORY_OPERATION = "operation";
    protected static final String PRODUCT_INVENTORY_COLUMN_BY_EMPLOYEE = "byEmployee";
    protected static final String PRODUCT_INVENTORY_COLUMN_HIDE = "hide";
    protected static final String PRODUCT_INVENTORY_COLUMN_BRANCH_ID = "branchId";
    protected static final String PRODUCT_INVENTORY_COLUMN_NAME = "name";
    protected static final String PRODUCT_INVENTORY_COLUMN_PRICE = "price";

    public static final String DATABASE_CREATE = "CREATE TABLE product_inventory ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`name` TEXT NOT NULL ," +   "`operation` TEXT NOT NULL ," + "`productId` INTEGER  , `qty` INTEGER, " +  "`price` REAL NOT NULL ," +
            "`byEmployee` INTEGER, `hide` INTEGER DEFAULT 0,`branchId` INTEGER DEFAULT 0 )";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;


    public ProductInventoryDbAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public ProductInventoryDbAdapter open() throws SQLException {
            this.db = dbHelper.getWritableDatabase();
            return this;

    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public long insertEntry(long productId , int qty , String operation ,long byUser,int branchId , int hide,String name , double price) {
        ProductInventory productInventory = new ProductInventory(Util.idHealth(this.db, PRODUCT_INVENTORY_TABLE_NAME, PRODUCT_INVENTORY_COLUMN_ID), productId, qty, operation, byUser,branchId,hide,name,price);
        try {
            return insertEntry(productInventory);
        } catch (SQLException ex) {
            Log.e("productInventory insert", "inserting Entry at " + PRODUCT_INVENTORY_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public long insertEntry(ProductInventory productInventory) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(PRODUCT_INVENTORY_COLUMN_ID, productInventory.getProductInventoryId());
        val.put(PRODUCT_INVENTORY_COLUMN_PRODUCT_ID, productInventory.getProductId());
        val.put(PRODUCT_INVENTORY_OPERATION, productInventory.getOperation());
        val.put(PRODUCT_INVENTORY_COLUMN_BY_EMPLOYEE, productInventory.getByEmployee());
        val.put(PRODUCT_INVENTORY_COLUMN_HIDE, productInventory.getHide());
        val.put(PRODUCT_INVENTORY_COLUMN_BRANCH_ID,productInventory.getBranchId());
        val.put(PRODUCT_INVENTORY_COLUMN_QTY,productInventory.getQty());
        val.put(PRODUCT_INVENTORY_COLUMN_NAME,productInventory.getName());
        val.put(PRODUCT_INVENTORY_COLUMN_PRICE,productInventory.getPrice());


        try {

            return db.insert(PRODUCT_INVENTORY_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("productInventory insert", "inserting Entry at " + PRODUCT_INVENTORY_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }

    public void updateEntry(long productId,int qty) {
        ProductInventoryDbAdapter productInventoryDbAdapter = new ProductInventoryDbAdapter(context);
        productInventoryDbAdapter.open();
        ProductInventory productInventory = productInventoryDbAdapter.getProductInventoryByID(productId);

        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(PRODUCT_INVENTORY_COLUMN_PRODUCT_ID, productInventory.getProductId());
        val.put(PRODUCT_INVENTORY_OPERATION, productInventory.getOperation());
        val.put(PRODUCT_INVENTORY_COLUMN_BY_EMPLOYEE, productInventory.getByEmployee());
        val.put(PRODUCT_INVENTORY_COLUMN_HIDE, productInventory.getHide());
        val.put(PRODUCT_INVENTORY_COLUMN_BRANCH_ID,productInventory.getBranchId());
        val.put(PRODUCT_INVENTORY_COLUMN_NAME,productInventory.getName());
        val.put(PRODUCT_INVENTORY_COLUMN_PRICE,productInventory.getPrice());
        val.put(PRODUCT_INVENTORY_COLUMN_QTY,qty);
        String where = PRODUCT_INVENTORY_COLUMN_ID + " = ?";
        db.update(PRODUCT_INVENTORY_TABLE_NAME, val, where, new String[]{productInventory.getProductInventoryId() + ""});
        productInventoryDbAdapter.close();
    }
    public ProductInventory getProductInventoryByID(long id) {
        ProductInventory productInventory = null;
        try {
            if(dbHelper==null) {
                open();
            }
        Cursor cursor = db.rawQuery("select * from " + PRODUCT_INVENTORY_TABLE_NAME + " where productId='" + id + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return productInventory;
        }
        cursor.moveToFirst();
        productInventory = new ProductInventory( Long.parseLong(cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_ID))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_PRODUCT_ID))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_QTY))),
                cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_OPERATION)),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_BY_EMPLOYEE))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_HIDE))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_BRANCH_ID))),
                cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_NAME)), Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_PRICE))));
        cursor.close();
        }
        catch (Exception e) {
            Log.d("exception",e.toString());

            }
        return productInventory;
    }
    public List<ProductInventory> getAllProducts(){
        List<ProductInventory> productsList =new ArrayList<ProductInventory>();
        Cursor cursor=null;
        if(SETTINGS.enableAllBranch) {
            cursor = db.rawQuery("select * from " + PRODUCT_INVENTORY_TABLE_NAME + " where " + PRODUCT_INVENTORY_COLUMN_HIDE + "=0 order by id desc", null);
        }else {
            cursor = db.rawQuery("select * from " + PRODUCT_INVENTORY_TABLE_NAME + " where " + PRODUCT_INVENTORY_COLUMN_BRANCH_ID + " = "+ SETTINGS.branchId+ " and " + PRODUCT_INVENTORY_COLUMN_HIDE + "=0 order by id desc", null);

        }
        cursor.moveToFirst();


        while(!cursor.isAfterLast()){
            productsList.add(makeProduct(cursor));
            cursor.moveToNext();
        }

        return productsList;
    }
    public List<ProductInventory> getTopProducts(int from ,int count){
        List<ProductInventory> productsList =new ArrayList<ProductInventory>();

        try {
            if(dbHelper==null) {
                open();
            }
            Cursor cursor=null;
            if(SETTINGS.enableAllBranch) {

                cursor = db.rawQuery("select * from " + PRODUCT_INVENTORY_TABLE_NAME + " where " + PRODUCT_INVENTORY_COLUMN_HIDE +"=0 order by id desc limit "+from+","+count, null);
            }else {
                cursor = db.rawQuery("select * from " + PRODUCT_INVENTORY_TABLE_NAME + " where " + PRODUCT_INVENTORY_COLUMN_BRANCH_ID + " = "+ SETTINGS.branchId+ " and " + PRODUCT_INVENTORY_COLUMN_HIDE+"=0 order by id desc limit "+from+","+count, null);

            }
            cursor.moveToFirst();

            while(!cursor.isAfterLast()){
                productsList.add(makeProduct(cursor));
                cursor.moveToNext();
            }


        } catch (Exception e) {
            Log.d("exception",e.toString());

        }
        Log.d("productsListInventory",productsList.size()+"size");
        return productsList;
    }
    private ProductInventory makeProduct(Cursor cursor) {
        try {
            ProductInventory d =new ProductInventory( Long.parseLong(cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_ID))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_PRODUCT_ID))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_QTY))),
                    cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_OPERATION)),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_BY_EMPLOYEE))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_HIDE))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_BRANCH_ID))),
                    cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_NAME)), Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_PRICE))));

            return d;
        } catch (Exception ex) {
            ProductInventory d = new ProductInventory( Long.parseLong(cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_ID))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_PRODUCT_ID))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_QTY))),
                    cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_OPERATION)),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_BY_EMPLOYEE))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_HIDE))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_BRANCH_ID))),
                    cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_NAME)), Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCT_INVENTORY_COLUMN_PRICE))));

            return d;
        }
    }
}
