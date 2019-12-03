package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.BoInventory;
import com.pos.leaders.leaderspossystem.Models.Inventory;
import com.pos.leaders.leaderspossystem.Models.ProductInventory;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class InventoryDbAdapter {
    public static final String INVENTORY_TABLE_NAME = "Inventory";
    protected static final String INVENTORY_COLUMN_ID = "id";
    protected static final String INVENTORY_COLUMN_NAME = "name";
    protected static final String INVENTORY_COLUMN_INVENTORY_ID = "inventory_id";
    protected static final String INVENTORY_COLUMN_PRODUCTS_ID_WITH_QUANTITY_LIST = "productsIdWithQuantityList";
    protected static final String INVENTORY_COLUMN_BRANCH_ID = "branchId";
    protected static final String INVENTORY_COLUMN_HIDE = "hide";

    public static final String DATABASE_CREATE = "CREATE TABLE Inventory ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`name` TEXT NOT NULL , `inventory_id` INTEGER, " +
            "`productsIdWithQuantityList` TEXT NOT NULL,`branchId` INTEGER DEFAULT 0 , `hide` INTEGER DEFAULT 0)";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;


    public InventoryDbAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public InventoryDbAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }
    public long insertEntry(String name, long inventoryId,String productsIdWithQuantityList ,int branchId,int hide) {
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        Inventory inventory = new Inventory(Util.idHealth(this.db, INVENTORY_TABLE_NAME, INVENTORY_COLUMN_ID), name,inventoryId,productsIdWithQuantityList,branchId,hide);
        try {
            close();
            return insertEntry(inventory);
        } catch (SQLException ex) {
            Log.d("InventoryDB", "inserting Entry at " + INVENTORY_COLUMN_NAME + ": " + ex.getMessage());
            return -1;
        }}
    public long insertEntry(Inventory inventory) {
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

        val.put(INVENTORY_COLUMN_ID, inventory.getId());
        val.put(INVENTORY_COLUMN_NAME, inventory.getName());
        val.put(INVENTORY_COLUMN_INVENTORY_ID, inventory.getInventoryId());
        val.put(INVENTORY_COLUMN_PRODUCTS_ID_WITH_QUANTITY_LIST, inventory.getProductsIdWithQuantityList());
        val.put(INVENTORY_COLUMN_BRANCH_ID,inventory.getBranchId());
        val.put(INVENTORY_COLUMN_HIDE,inventory.getHide());
        try {

            return db.insert(INVENTORY_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("InventoryDB insert", "inserting Entry at " + INVENTORY_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
        }
    public long deleteEntryBo(Inventory inventory) {
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(INVENTORY_COLUMN_HIDE, 1);

        String where = INVENTORY_COLUMN_INVENTORY_ID + " = ?";
        try {
            db.update(INVENTORY_TABLE_NAME, updatedValues, where, new String[]{inventory.getInventoryId() + ""});
            close();
            return 1;
        } catch (SQLException ex) {
            Log.e("Inventory DB delete", "enable hide Entry at " + INVENTORY_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public long updateEntryBo(BoInventory inventory) {
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        InventoryDbAdapter inventoryDbAdapter = new InventoryDbAdapter(context);
        inventoryDbAdapter.open();
        ProductInventoryDbAdapter productInventoryDbAdapter = new ProductInventoryDbAdapter(context);
        productInventoryDbAdapter.open();
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(INVENTORY_COLUMN_ID, inventory.getId());
        val.put(INVENTORY_COLUMN_NAME, inventory.getName());
        val.put(INVENTORY_COLUMN_INVENTORY_ID, inventory.getInventoryId());
        val.put(INVENTORY_COLUMN_PRODUCTS_ID_WITH_QUANTITY_LIST, inventory.getProductsIdWithQuantityList().toString());
        val.put(INVENTORY_COLUMN_BRANCH_ID,inventory.getBranchId());
        val.put(INVENTORY_COLUMN_HIDE,inventory.getHide());
        HashMap<String,Integer> productHashMap=inventory.getProductsIdWithQuantityList();
        Iterator itr = productHashMap.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry entry = (Map.Entry) itr.next();
            productInventoryDbAdapter.open();
            String key = (String) entry.getKey();
            Integer value = (Integer) entry.getValue();
           ProductInventory productInventory= productInventoryDbAdapter.getProductInventoryByID(Long.parseLong(key));
            Log.d("pppppp",productInventory.toString());
            productInventoryDbAdapter.updateEntry(Long.parseLong(key),value);
            productInventoryDbAdapter.close();
        }
        try {
            String where = INVENTORY_COLUMN_ID + " = ?";
            db.update(INVENTORY_TABLE_NAME, val, where, new String[]{inventory.getId() + ""});
            close();
            return 1;
        } catch (SQLException ex) {
            return 0;
        }
    }
 public Inventory getLastRow() throws Exception {
     if(db.isOpen()){

     }else {
         try {
             open();
         }
         catch (SQLException ex) {
             Log.d("Exception",ex.toString());
         }
     }
     Inventory inventory = null;
     Cursor cursor = db.rawQuery("select * from " + INVENTORY_TABLE_NAME , null);
     if (cursor.getCount() < 1) // zReport Not Exist
     {
         cursor.close();
         throw new Exception("there is no rows on inventory  Table");
     }
     cursor.moveToFirst();
     inventory = makeInventory(cursor);
     cursor.close();
        close();
     return inventory;
 }

    private Inventory makeInventory(Cursor cursor) {
        //    public Inventory(long id, String name, long inventory_id, String productsIdWithQuantityList, int branchId, int hide) {

        try {
                Inventory d =new Inventory(Long.parseLong(cursor.getString(cursor.getColumnIndex(INVENTORY_COLUMN_ID))),
                        cursor.getString(cursor.getColumnIndex(INVENTORY_COLUMN_NAME)),
                        Long.parseLong(cursor.getString(cursor.getColumnIndex(INVENTORY_COLUMN_INVENTORY_ID))),
                        cursor.getString(cursor.getColumnIndex(INVENTORY_COLUMN_PRODUCTS_ID_WITH_QUANTITY_LIST)),Integer.parseInt(cursor.getString(cursor.getColumnIndex(INVENTORY_COLUMN_BRANCH_ID))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(INVENTORY_COLUMN_HIDE))));

                return d;
            } catch (Exception ex) {
                Inventory d = new Inventory(Long.parseLong(cursor.getString(cursor.getColumnIndex(INVENTORY_COLUMN_ID))),
                        cursor.getString(cursor.getColumnIndex(INVENTORY_COLUMN_NAME)),
                        Long.parseLong(cursor.getString(cursor.getColumnIndex(INVENTORY_COLUMN_INVENTORY_ID))),
                        cursor.getString(cursor.getColumnIndex(INVENTORY_COLUMN_PRODUCTS_ID_WITH_QUANTITY_LIST)),Integer.parseInt(cursor.getString(cursor.getColumnIndex(INVENTORY_COLUMN_BRANCH_ID))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(INVENTORY_COLUMN_HIDE))));

                return d;
            }

    }

}
