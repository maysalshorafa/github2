package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.DocumentType;
import com.pos.leaders.leaderspossystem.Models.PosInvoice;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Win8.1 on 12/10/2018.
 */

public class PosInvoiceDBAdapter {
    public static final String POS_INVOICE_TABLE_NAME = "pos_invoice";
    // Column Names
    protected static final String POS_INVOICE_COLUMN_ID = "id";
    protected static final String POS_INVOICE_COLUMN_BO_ID = "boID";
    protected static final String POS_INVOICE_COLUMN_AMOUNT = "amount";
    protected static final String POS_INVOICE_COLUMN_LAST_Z_REPORT = "lastZReportId";
    protected static final String POS_INVOICE_COLUMN_TYPE = "type";
    protected static final String POS_INVOICE_COLUMN_STATUS = "status";
    protected static final String POS_INVOICE_COLUMN_PAYMENT_METHOD = "paymentMethod";



    public static final String DATABASE_CREATE = "CREATE TABLE pos_invoice ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, " +"`boID` TEXT DEFAULT ''," +
            "`amount` REAL DEFAULT 0 ," +"`lastZReportId` INTEGER DEFAULT 0 ," +"`type` TEXT DEFAULT ''," +"`status` TEXT DEFAULT ''," +"`paymentMethod` TEXT DEFAULT '')";

    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;


    public PosInvoiceDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public PosInvoiceDBAdapter open() throws SQLException {

            this.db = dbHelper.getWritableDatabase();
            return this;

    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }


    public long insertEntry(double amount,long zReportId,String type,String status,String boID,String paymentMethod) {
        PosInvoice invoice = new PosInvoice(Util.idHealth(this.db, POS_INVOICE_TABLE_NAME, POS_INVOICE_COLUMN_ID), amount,zReportId,type,status,boID,paymentMethod);

        try {
            return insertEntry(invoice);
        } catch (SQLException ex) {
            Log.e("Invoice insert", "inserting Entry at " + POS_INVOICE_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public long insertEntry(PosInvoice invoice) {
        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(POS_INVOICE_COLUMN_ID, invoice.getId());
        val.put(POS_INVOICE_COLUMN_AMOUNT, invoice.getAmount());
        val.put(POS_INVOICE_COLUMN_LAST_Z_REPORT,invoice.getLastZReportId());
        val.put(POS_INVOICE_COLUMN_TYPE,invoice.getType());
        val.put(POS_INVOICE_COLUMN_STATUS,invoice.getStatus());
        val.put(POS_INVOICE_COLUMN_BO_ID,invoice.getBoID());
        val.put(POS_INVOICE_COLUMN_PAYMENT_METHOD,invoice.getPaymentMethod());
        try {

            return db.insert(POS_INVOICE_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("InvoiceDB insert", "inserting Entry at " + POS_INVOICE_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }
    public List<PosInvoice> getPosInvoice(long zReportId){
        List<PosInvoice> posInvoices = new ArrayList<PosInvoice>();

        Cursor cursor = db.rawQuery("select * from " + POS_INVOICE_TABLE_NAME + " where "+POS_INVOICE_COLUMN_LAST_Z_REPORT+" = "+zReportId+ " and " + POS_INVOICE_COLUMN_TYPE +  " = "+"'"+DocumentType.INVOICE.getValue() +"'", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            posInvoices.add(makePosInvoice(cursor));
            cursor.moveToNext();
        }

        return posInvoices;
    }

    public List<PosInvoice> getPosInvoiceList(long zReportId,String status){

        List<PosInvoice> posInvoices = new ArrayList<PosInvoice>();

        try {
            if(dbHelper==null) {
                open();
            }

            Cursor cursor = db.rawQuery("select * from " + POS_INVOICE_TABLE_NAME + " where "+POS_INVOICE_COLUMN_LAST_Z_REPORT+" = "+zReportId+ " and " + POS_INVOICE_COLUMN_STATUS + " = "+ "'"+ status + "' "+ " and " + POS_INVOICE_COLUMN_TYPE +  " = "+"'"+DocumentType.INVOICE.getValue() +"'", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            posInvoices.add(makePosInvoice(cursor));
            cursor.moveToNext();
        }

        }catch (Exception e){
            e.printStackTrace();
        }
        return posInvoices;
    }
    public List<PosInvoice> getPosInvoiceListByType(long zReportId,String type,String paymentMethod){
        List<PosInvoice> posInvoices = new ArrayList<PosInvoice>();

        try {
            if(dbHelper==null) {
                open();
            }

            Cursor cursor = db.rawQuery("select * from " + POS_INVOICE_TABLE_NAME + " where " + POS_INVOICE_COLUMN_LAST_Z_REPORT + " = " + zReportId + " and " + POS_INVOICE_COLUMN_TYPE + " = " + "'" + type + "'" + " and " + POS_INVOICE_COLUMN_PAYMENT_METHOD + " = " + "'" + paymentMethod + "'", null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                posInvoices.add(makePosInvoice(cursor));
                cursor.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return posInvoices;
    }
    private PosInvoice makePosInvoice(Cursor c){
        return new PosInvoice(c.getLong(c.getColumnIndex(POS_INVOICE_COLUMN_ID)),
                c.getDouble(c.getColumnIndex(POS_INVOICE_COLUMN_AMOUNT)),
                c.getLong(c.getColumnIndex(POS_INVOICE_COLUMN_LAST_Z_REPORT)),
                c.getString(c.getColumnIndex(POS_INVOICE_COLUMN_TYPE)),
                c.getString(c.getColumnIndex(POS_INVOICE_COLUMN_STATUS)),
                c.getString(c.getColumnIndex(POS_INVOICE_COLUMN_BO_ID)),
                c.getString(c.getColumnIndex(POS_INVOICE_COLUMN_PAYMENT_METHOD)));
    }
    public void updateEntry(PosInvoice invoice) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(POS_INVOICE_COLUMN_ID, invoice.getId());
        val.put(POS_INVOICE_COLUMN_AMOUNT, invoice.getAmount());
        val.put(POS_INVOICE_COLUMN_LAST_Z_REPORT,invoice.getLastZReportId());
        val.put(POS_INVOICE_COLUMN_TYPE,invoice.getType());
        val.put(POS_INVOICE_COLUMN_STATUS,invoice.getStatus());
        val.put(POS_INVOICE_COLUMN_BO_ID,invoice.getBoID());
        val.put(POS_INVOICE_COLUMN_PAYMENT_METHOD,invoice.getPaymentMethod());
        String where = POS_INVOICE_COLUMN_ID + " = ?";
        db.update(POS_INVOICE_TABLE_NAME, val, where, new String[]{invoice.getId() + ""});
    }
    public PosInvoice getPodInvoiceByBoId(String id) {
        PosInvoice posInvoice = null;
        Cursor c = db.rawQuery("select * from " + POS_INVOICE_TABLE_NAME + " where boID='" + id + "'", null);
        if (c.getCount() < 1) // UserName Not Exist
        {
            c.close();
            return posInvoice;
        }
        c.moveToFirst();
        posInvoice =  new PosInvoice(c.getLong(c.getColumnIndex(POS_INVOICE_COLUMN_ID)),
                c.getDouble(c.getColumnIndex(POS_INVOICE_COLUMN_AMOUNT)),
                c.getLong(c.getColumnIndex(POS_INVOICE_COLUMN_LAST_Z_REPORT)),
                c.getString(c.getColumnIndex(POS_INVOICE_COLUMN_TYPE)),
                c.getString(c.getColumnIndex(POS_INVOICE_COLUMN_STATUS)),
                c.getString(c.getColumnIndex(POS_INVOICE_COLUMN_BO_ID)),
                c.getString(c.getColumnIndex(POS_INVOICE_COLUMN_PAYMENT_METHOD)));
        c.close();

        return posInvoice;
    }
}
