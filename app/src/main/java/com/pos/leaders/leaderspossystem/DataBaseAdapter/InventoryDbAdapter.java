package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Inventory;
import com.pos.leaders.leaderspossystem.Tools.Util;


public class InventoryDbAdapter {
    public static final String INVENTORY_TABLE_NAME = "Inventory";
    protected static final String INVENTORY_COLUMN_ID = "id";
    protected static final String INVENTORY_COLUMN_NAME = "name";
    protected static final String INVENTORY_COLUMN_INVENTORY_ID = "inventory_id";
    protected static final String INVENTORY_COLUMN_PRODUCTS_ID_WITH_QUANTITY_LIST = "productsIdWithQuantityList";
    protected static final String INVENTORY_COLUMN_BRANCH_ID = "branch_id";
    protected static final String INVENTORY_COLUMN_HIDE = "hide";

    public static final String DATABASE_CREATE = "CREATE TABLE Inventory ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`name` TEXT NOT NULL , `inventory_id` INTEGER, " +
            "`productsIdWithQuantityList` TEXT NOT NULL, `productsIdWithQuantityList` TEXT NOT NULL,`branchId` INTEGER DEFAULT 0 , `hide` INTEGER DEFAULT 0)";
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
        Inventory inventory = new Inventory(Util.idHealth(this.db, INVENTORY_TABLE_NAME, INVENTORY_COLUMN_ID), name,inventoryId,productsIdWithQuantityList,branchId,hide);
        try {
            return insertEntry(inventory);
        } catch (SQLException ex) {
            Log.e("InventoryDB insert", "inserting Entry at " + INVENTORY_COLUMN_NAME + ": " + ex.getMessage());
            return -1;
        }}
    public long insertEntry(Inventory inventory) {
        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(INVENTORY_COLUMN_BRANCH_ID, inventory.getId());
        val.put(INVENTORY_COLUMN_NAME, inventory.getName());
        val.put(INVENTORY_COLUMN_INVENTORY_ID, inventory.getInventory_id());
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
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(INVENTORY_COLUMN_HIDE, 1);

        String where = INVENTORY_COLUMN_INVENTORY_ID + " = ?";
        try {
            db.update(INVENTORY_TABLE_NAME, updatedValues, where, new String[]{inventory.getInventory_id() + ""});
            return 1;
        } catch (SQLException ex) {
            Log.e("Inventory DB delete", "enable hide Entry at " + INVENTORY_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

 /*   public long updateEntryBo(Inventory inventory) {
        InventoryDbAdapter departmentDBAdapter = new CategoryDBAdapter(context);
        departmentDBAdapter.open();
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(CATEGORY_COLUMN_NAME, department.getName());
        val.put(CATEGORY_COLUMN_BYUSER, department.getByUser());
        val.put(CATEGORY_COLUMN_DISENABLED, department.isHide());
        val.put(CATEGORY_COLUMN_BRANCH_ID,department.getBranchId());

        try {
            String where = CATEGORY_COLUMN_ID + " = ?";
            db.update(CATEGORY_TABLE_NAME, val, where, new String[]{department.getCategoryId() + ""});
            Category d=departmentDBAdapter.getDepartmentByID(department.getCategoryId());
            Log.d("Update object",d.toString());
            departmentDBAdapter.close();
            return 1;
        } catch (SQLException ex) {
            return 0;
        }
    }*/
}
