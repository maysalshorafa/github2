package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.PosDocument;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 8/17/2020.
 */

public class PosDocumentTableDbAdapter {
    public static final String POS_DOCUMENT_TABLE_NAME = "PosDocument";
    // Column Names
    protected static final String POS_DOCUMENT_COLUMN_ID = "id";
    protected static final String POS_DOCUMENT_COLUMN_BO_ID = "boID";
    protected static final String POS_DOCUMENT_COLUMN_TYPE = "type";
    protected static final String POS_DOCUMENT_COLUMN_DATA_DOCUMENT = "dataDocument";



    public static final String DATABASE_CREATE = "CREATE TABLE PosDocument ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, " +"`boID` TEXT DEFAULT ''," +
          "`type` TEXT DEFAULT '',"  +"`dataDocument` TEXT DEFAULT '')";

    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;


    public PosDocumentTableDbAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public PosDocumentTableDbAdapter open() throws SQLException {

        this.db = dbHelper.getWritableDatabase();
        return this;

    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }


    public long insertEntryWithOnLine(String boID,String type,String documentDate) {
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }

        PosDocument invoice = new PosDocument(Util.idHealth(this.db, POS_DOCUMENT_TABLE_NAME, POS_DOCUMENT_COLUMN_ID),type,boID,documentDate);

        try {
            close();
            return insertEntry(invoice);
        } catch (SQLException ex) {
            Log.e("Invoice insert", "inserting Entry at " + POS_DOCUMENT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }


    public long insertEntryWithOffLine(String boID,String type,String documentDate) {
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }

        PosDocument invoice = new PosDocument(Util.idHealth(this.db, POS_DOCUMENT_TABLE_NAME, POS_DOCUMENT_COLUMN_ID),type,boID,documentDate);
        sendToBroker(MessageType.ADD_POS_DOCUMENT, invoice, this.context);

        try {
            close();
            return insertEntry(invoice);
        } catch (SQLException ex) {
            Log.e("Invoice insert", "inserting Entry at " + POS_DOCUMENT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public long insertEntry(PosDocument invoice) {
        Log.d("trdgjukiol",invoice.toString());
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

        val.put(POS_DOCUMENT_COLUMN_ID, invoice.getPosDocumentId());
        val.put(POS_DOCUMENT_COLUMN_BO_ID, invoice.getBoID());
        val.put(POS_DOCUMENT_COLUMN_TYPE,invoice.getType());
        val.put(POS_DOCUMENT_COLUMN_DATA_DOCUMENT,invoice.getDocumentData());

        try {

            return db.insert(POS_DOCUMENT_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("InvoiceDB insert", "inserting Entry at " + POS_DOCUMENT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }

    public long updateEntryBo(long posDocumentId , String posDocumentBoId) {
        Log.d("testUppppp",posDocumentBoId);
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        PosDocumentTableDbAdapter posDocumentTableDbAdapter = new PosDocumentTableDbAdapter(context);
        posDocumentTableDbAdapter.open();
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(POS_DOCUMENT_COLUMN_BO_ID, posDocumentBoId);
        try {
            String where = POS_DOCUMENT_COLUMN_ID + " = ?";
            db.update(POS_DOCUMENT_TABLE_NAME, val, where, new String[]{posDocumentId + ""});
            close();
            return 1;
        } catch (SQLException ex) {
            return 0;
        }
    }

}
