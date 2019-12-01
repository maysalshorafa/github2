package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Invoice;
import com.pos.leaders.leaderspossystem.Tools.Util;

/**
 * Created by Win8.1 on 10/17/2018.
 */

public class InvoiceDBAdapter {
    public static final String INVOICE_TABLE_NAME = "invoice";
    // Column Names
    protected static final String INVOICE_COLUMN_ID = "id";
    protected static final String INVOICE_COLUMN_NAME = "invoiceID";
    protected static final String INVOICE_COLUMN_CUSTOMER_ID = "customerID";
    protected static final String INVOICE_COLUMN_DISENABLED = "hide";

    public static final String DATABASE_CREATE = "CREATE TABLE invoice ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`invoiceID` TEXT NOT NULL ,"+"`customerID` INTEGER , "+"`hide` INTEGER DEFAULT 0 )";

    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;


    public InvoiceDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public InvoiceDBAdapter open() throws SQLException {
        try {
            this.db = dbHelper.getWritableDatabase();
            return this;

        } catch (SQLException s) {
            new Exception("Error with DB Open");
            return this;

        }
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }


    public long insertEntry(String invoiceId,long customerID) {
        Invoice invoice = new Invoice(Util.idHealth(this.db, INVOICE_TABLE_NAME, INVOICE_COLUMN_NAME), invoiceId,customerID);
        try {
            return insertEntry(invoice);
        } catch (SQLException ex) {
            Log.e("Invoice insert", "inserting Entry at " + INVOICE_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public long insertEntry(Invoice invoice) {
        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(INVOICE_COLUMN_ID, invoice.getId());
        val.put(INVOICE_COLUMN_NAME, invoice.getInvoiceId());
        val.put(INVOICE_COLUMN_CUSTOMER_ID,invoice.getCustomerID());
        try {

            return db.insert(INVOICE_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("InvoiceDB insert", "inserting Entry at " + INVOICE_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }
}
