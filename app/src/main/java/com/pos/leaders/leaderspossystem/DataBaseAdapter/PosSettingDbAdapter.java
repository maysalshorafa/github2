package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.PosSetting;
import com.pos.leaders.leaderspossystem.SetUpManagement;
import com.pos.leaders.leaderspossystem.Tools.CompanyStatus;
import com.pos.leaders.leaderspossystem.Tools.PrinterType;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import static android.content.Context.MODE_PRIVATE;
import static com.pos.leaders.leaderspossystem.SetUpManagement.POS_Company_status;
import static com.pos.leaders.leaderspossystem.SetUpManagement.POS_Management;
import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 6/24/2019.
 */

public class PosSettingDbAdapter {
    // Table Name
    public static final String POS_SETTING_TABLE_NAME = "PosSetting";
    public static final String POS_SETTING_COLUMN_ID = "id";
    public static final String POS_SETTING_COLUMN_ENABLE_CURRENCY = "enableCurrency";
    public static final String POS_SETTING_COLUMN_ENABLE_CREDIT_CARD = "enableCreditCard";
    public static final String POS_SETTING_COLUMN_ENABLE_PIN_PAD = "enablePinPad";
    public static final String POS_SETTING_COLUMN_ENABLE_CUSTOMER_MEASUREMENT = "enableCustomerMeasurement";
    public static final String POS_SETTING_COLUMN_FLOAT_POINT = "noOfFloatPoint";
    public static final String POS_SETTING_COLUMN_PRINTER_TYPE = "printerType";
    public static final String POS_SETTING_COLUMN_POS_VERSION_NO = "posVersionNo";
    public static final String POS_SETTING_COLUMN_POS_DB_VERSION_NO = "posDbVersionNo";
    protected static final String POS_SETTING_COLUMN_BRANCH_ID= "branchId";
    protected static final String POS_SETTING_COLUMN_COMPANY_STATUS= "companyStatus";
    public static final String DATABASE_CREATE = "CREATE TABLE PosSetting ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`enableCurrency` INTEGER DEFAULT 0 , `enableCreditCard` INTEGER DEFAULT 0, " +
            "`enablePinPad` INTEGER DEFAULT 0, `enableCustomerMeasurement` INTEGER DEFAULT 0,`noOfFloatPoint` INTEGER DEFAULT 0,`posVersionNo` TEXT ,`posDbVersionNo` TEXT , `printerType` TEXT  , `companyStatus` TEXT  ,`branchId` INTEGER DEFAULT 0 )";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;


    public PosSettingDbAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public PosSettingDbAdapter open() throws SQLException {
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
    public long insertEntry( boolean enableCurrency, boolean enableCreditCard, boolean enablePinPad, boolean enableCustomerMeasurement, int noOfFloatPoint, String printerType,String companyStatus, String posVersionNo, String posDbVersionNo, int branchId){
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        PosSetting posSetting = new PosSetting(Util.idHealth(this.db, POS_SETTING_TABLE_NAME, POS_SETTING_COLUMN_ID),enableCurrency,enableCreditCard,enablePinPad,enableCustomerMeasurement,noOfFloatPoint,printerType,companyStatus,posVersionNo,posDbVersionNo,branchId);
        sendToBroker(MessageType.ADD_POS_SETTING, posSetting, this.context);

        try {
            close();
            return insertEntry(posSetting);
        } catch (SQLException ex) {
            Log.e("PosSettingDB insert", "inserting Entry at " + POS_SETTING_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }
    public long insertEntry(PosSetting posSetting) {
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

        val.put(POS_SETTING_COLUMN_ID, posSetting.getPosSettingId());
        val.put(POS_SETTING_COLUMN_ENABLE_CURRENCY, posSetting.isEnableCurrency() ? 1 : 0);
        val.put(POS_SETTING_COLUMN_ENABLE_CREDIT_CARD, posSetting.isEnableCreditCard() ? 1 : 0);
        val.put(POS_SETTING_COLUMN_ENABLE_CUSTOMER_MEASUREMENT, posSetting.isEnableCustomerMeasurement() ? 1 : 0);
        val.put(POS_SETTING_COLUMN_ENABLE_PIN_PAD, posSetting.isEnablePinPad() ? 1 : 0);
        val.put(POS_SETTING_COLUMN_FLOAT_POINT,posSetting.getNoOfFloatPoint());
        val.put(POS_SETTING_COLUMN_POS_VERSION_NO,posSetting.getPosVersionNo());
        val.put(POS_SETTING_COLUMN_POS_DB_VERSION_NO,posSetting.getPosDbVersionNo());
        val.put(POS_SETTING_COLUMN_BRANCH_ID,posSetting.getBranchId());
        val.put(POS_SETTING_COLUMN_PRINTER_TYPE,posSetting.getPrinterType());
        val.put(POS_SETTING_COLUMN_COMPANY_STATUS,posSetting.getCompanyStatus());
        try {

            return db.insert(POS_SETTING_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("PosSettingDB insert", "inserting Entry at " + POS_SETTING_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }
    public long updateEntryBo(PosSetting posSetting) {
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
        val.put(POS_SETTING_COLUMN_ENABLE_CURRENCY, posSetting.isEnableCurrency() ? 1 : 0);
        val.put(POS_SETTING_COLUMN_ENABLE_CREDIT_CARD, posSetting.isEnableCreditCard() ? 1 : 0);
        val.put(POS_SETTING_COLUMN_ENABLE_CUSTOMER_MEASUREMENT, posSetting.isEnableCustomerMeasurement() ? 1 : 0);
        val.put(POS_SETTING_COLUMN_ENABLE_PIN_PAD, posSetting.isEnablePinPad() ? 1 : 0);
        val.put(POS_SETTING_COLUMN_FLOAT_POINT,posSetting.getNoOfFloatPoint());
        val.put(POS_SETTING_COLUMN_POS_VERSION_NO,posSetting.getPosVersionNo());
        val.put(POS_SETTING_COLUMN_POS_DB_VERSION_NO,posSetting.getPosDbVersionNo());
        val.put(POS_SETTING_COLUMN_BRANCH_ID,posSetting.getBranchId());
        val.put(POS_SETTING_COLUMN_PRINTER_TYPE,posSetting.getPrinterType());
        val.put(POS_SETTING_COLUMN_COMPANY_STATUS,posSetting.getCompanyStatus());

        SharedPreferences cSharedPreferences = context.getSharedPreferences(POS_Management, MODE_PRIVATE);
        final SharedPreferences.Editor editor = cSharedPreferences.edit();
        if (cSharedPreferences != null) {
            //CreditCard
            if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CREDIT_CARD)) {
                editor.putBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CREDIT_CARD, posSetting.isEnableCreditCard());
                SETTINGS.creditCardEnable = posSetting.isEnableCreditCard();
            }

            //PinPad
            if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PIN_PAD)) {
                editor.putBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PIN_PAD, posSetting.isEnablePinPad());
                SETTINGS.pinpadEnable = posSetting.isEnablePinPad();
            }
            //Currency
            if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY)) {
                editor.putBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY, posSetting.isEnableCurrency());
                SETTINGS.enableCurrencies = posSetting.isEnableCurrency();
            }
            //CustomerMeasurement
            if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CUSTOMER_MEASUREMENT)) {
                editor.putBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CUSTOMER_MEASUREMENT, posSetting.isEnableCustomerMeasurement());
                SETTINGS.enableCustomerMeasurement = posSetting.isEnableCustomerMeasurement();
            }
            //FloatPoint
            if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_FLOAT_POINT)) {
                int editNoOfPoint = posSetting.getNoOfFloatPoint();
                editor.putString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_FLOAT_POINT, editNoOfPoint+"");
                SETTINGS.decimalNumbers = editNoOfPoint;
            }
            //PrinterType
            if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PRINTER_TYPE)) {
                String editPrinterType = posSetting.getPrinterType();
                editor.putString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PRINTER_TYPE, editPrinterType);
                PrinterType printer = PrinterType.valueOf(editPrinterType);
                SETTINGS.printer = printer;
            }
            //BranchId
            if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_BRANCH_ID)) {
                int branch = posSetting.getBranchId();
                editor.putString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_BRANCH_ID, branch+"");
                if(branch==0) {
                    SETTINGS.enableAllBranch = true;
                }else {
                    SETTINGS.enableAllBranch=false;
                }

            }

        }
        SharedPreferences sharedPreferencesCompanyStatus = context.getSharedPreferences(POS_Company_status, MODE_PRIVATE);
        final SharedPreferences.Editor editorCompanyStauts = cSharedPreferences.edit();
        if (cSharedPreferences != null) {
            //CompanyStatus
            if (sharedPreferencesCompanyStatus.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_COMPANY_STATUS)) {
                String editCompanyStatus = posSetting.getCompanyStatus();

                Log.d("companyPosSetting1","1");
                //    Log.d("companyPosSetting1",editCompanyStatus);
                editorCompanyStauts.putString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_COMPANY_STATUS, editCompanyStatus);
                CompanyStatus companyStatus = CompanyStatus.valueOf(editCompanyStatus);
                SETTINGS.company = companyStatus;
            }
        }
        editor.apply();
        editorCompanyStauts.apply();
        try {
            String where = POS_SETTING_COLUMN_ID + " = ?";
            db.update(POS_SETTING_TABLE_NAME, val, where, new String[]{posSetting.getPosSettingId() + ""});
            close();
            return 1;
        } catch (SQLException ex) {
            return 0;
        }
    }

    public void updateEntry(PosSetting posSetting) {
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
        PosSettingDbAdapter posSettingDbAdapter = new PosSettingDbAdapter(context);
        posSettingDbAdapter.open();
        //Assign values for each row.
        val.put(POS_SETTING_COLUMN_ENABLE_CURRENCY, posSetting.isEnableCurrency() ? 1 : 0);
        val.put(POS_SETTING_COLUMN_ENABLE_CREDIT_CARD, posSetting.isEnableCreditCard() ? 1 : 0);
        val.put(POS_SETTING_COLUMN_ENABLE_CUSTOMER_MEASUREMENT, posSetting.isEnableCustomerMeasurement() ? 1 : 0);
        val.put(POS_SETTING_COLUMN_ENABLE_PIN_PAD, posSetting.isEnablePinPad() ? 1 : 0);
        val.put(POS_SETTING_COLUMN_FLOAT_POINT,posSetting.getNoOfFloatPoint());
        val.put(POS_SETTING_COLUMN_POS_VERSION_NO,posSetting.getPosVersionNo());
        val.put(POS_SETTING_COLUMN_POS_DB_VERSION_NO,posSetting.getPosDbVersionNo());
        val.put(POS_SETTING_COLUMN_BRANCH_ID,posSetting.getBranchId());
        val.put(POS_SETTING_COLUMN_PRINTER_TYPE,posSetting.getPrinterType());
        val.put(POS_SETTING_COLUMN_COMPANY_STATUS,posSetting.getCompanyStatus());

        String where = POS_SETTING_COLUMN_ID + " = ?";
        db.update(POS_SETTING_TABLE_NAME, val, where, new String[]{posSetting.getPosSettingId() + ""});
        PosSetting d=posSettingDbAdapter.getPosSettingByID(posSetting.getPosSettingId());
        Log.d("Update object",d.toString());
        sendToBroker(MessageType.UPDATE_CATEGORY, d, this.context);
        posSettingDbAdapter.close();
        close();
    }

    public PosSetting getPosSettingByID(long id) {
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        PosSetting posSetting = null;
        Cursor cursor = db.rawQuery("select * from " + POS_SETTING_TABLE_NAME + " where id='" + id + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            close();
            return posSetting;
        }
        cursor.moveToFirst();
        posSetting = new PosSetting(id,  Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(POS_SETTING_COLUMN_ENABLE_CURRENCY))),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(POS_SETTING_COLUMN_ENABLE_CREDIT_CARD))),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(POS_SETTING_COLUMN_ENABLE_PIN_PAD))),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(POS_SETTING_COLUMN_ENABLE_CUSTOMER_MEASUREMENT))),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(POS_SETTING_COLUMN_FLOAT_POINT))),
               cursor.getString(cursor.getColumnIndex(POS_SETTING_COLUMN_PRINTER_TYPE)),
                cursor.getString(cursor.getColumnIndex(POS_SETTING_COLUMN_COMPANY_STATUS)),cursor.getString(cursor.getColumnIndex(POS_SETTING_COLUMN_POS_VERSION_NO)),
                        cursor.getString(cursor.getColumnIndex(POS_SETTING_COLUMN_POS_DB_VERSION_NO)), Integer.parseInt(cursor.getString(cursor.getColumnIndex(POS_SETTING_COLUMN_BRANCH_ID))));
        cursor.close();
  close();
        return posSetting;
    }


    public static String addColumnText(String columnName) {
        String dbc = "ALTER TABLE " + POS_SETTING_TABLE_NAME
                + " add column " + columnName + " TEXT  DEFAULT '' ;";
        return dbc;
    }
}
