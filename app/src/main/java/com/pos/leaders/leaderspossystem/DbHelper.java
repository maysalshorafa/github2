package com.pos.leaders.leaderspossystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pos.leaders.leaderspossystem.Backup.Backup;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CategoryDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ChecksDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CityDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClosingReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClosingReportDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClubAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CreditCardPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyOperationDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyReturnsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerAssetDB;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerMeasurementAdapter.CustomerMeasurementDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerMeasurementAdapter.MeasurementDynamicVariableDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerMeasurementAdapter.MeasurementsDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.DepositAndPullReportDetailsDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.DrawerDepositAndPullReportDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeePermissionsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.GroupDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.GroupsResourceDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.IdsCounterDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.InventoryDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.InvoiceDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.LincessDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferCategoryDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferRuleDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PermissionsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PosInvoiceDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PosSettingDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductInventoryDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductOfferDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProviderDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule11DBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule1DBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule3DbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule5DBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule7DbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule8DBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ScheduleWorkersDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.SettingsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Sum_PointDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UsedPointDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ValueOfPointDB;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.XReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportCountDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Feedback.ClearSync;
import com.pos.leaders.leaderspossystem.Tools.BufferDbEmail;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Karam on 16/10/2016.
 */

public class DbHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    public static final int DATABASE_VERSION = 11;

    protected static final String DATABASE_NAME = "POSDB.db";

    Context context;

    public static boolean DATABASE_ENABEL_ALTER_COLUMN = false;
    public DbHelper(Context context)
    {
        super(context, DATABASE_NAME ,null,DATABASE_VERSION);
        this.context = context;
    }

    public List<String> tablesName(SQLiteDatabase db){
        Cursor c=db.rawQuery("SELECT name FROM sqlite_master WHERE type='table';", null);
        List<String> tablesNames=new ArrayList<String>();
        c.moveToFirst();
        while (!c.isAfterLast()){
            tablesNames.add(c.getString(0));
            c.moveToNext();
        }
        return tablesNames;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // if Version is one this method called
        //offers
        db.execSQL(Rule1DBAdapter.DATABASE_CREATE);
        db.execSQL(Rule3DbAdapter.DATABASE_CREATE);
        db.execSQL(Rule5DBAdapter.DATABASE_CREATE);
        db.execSQL(Rule7DbAdapter.DATABASE_CREATE);
        db.execSQL(Rule8DBAdapter.DATABASE_CREATE);
        db.execSQL(Rule11DBAdapter.DATABASE_CREATE);
        db.execSQL(OfferRuleDBAdapter.DATABASE_CREATE);
        db.execSQL(OfferDBAdapter.DATABASE_CREATE);
        db.execSQL(ZReportDBAdapter.DATABASE_CREATE);
        db.execSQL(OpiningReportDBAdapter.DATABASE_CREATE);
        db.execSQL(OpiningReportDetailsDBAdapter.DATABASE_CREATE);
        db.execSQL(CityDbAdapter.DATABASE_CREATE);
        db.execSQL(ClubAdapter.DATABASE_CREATE);//Club
        db.execSQL(CustomerDBAdapter.DATABASE_CREATE);
        db.execSQL(UsedPointDBAdapter.DATABASE_CREATE);
        db.execSQL(Sum_PointDbAdapter.DATABASE_CREATE);
        db.execSQL(ValueOfPointDB.DATABASE_CREATE);

        db.execSQL(OrderDBAdapter.DATABASE_CREATE);
        db.execSQL(ChecksDBAdapter.DATABASE_CREATE);
        db.execSQL(CategoryDBAdapter.DATABASE_CREATE);
        db.execSQL(CustomerAssetDB.DATABASE_CREATE);

        db.execSQL(OrderDetailsDBAdapter.DATABASE_CREATE);
        db.execSQL(PaymentDBAdapter.DATABASE_CREATE);
        db.execSQL(CreditCardPaymentDBAdapter.DATABASE_CREATE);

        db.execSQL(CashPaymentDBAdapter.DATABASE_CREATE);
        db.execSQL(CurrencyOperationDBAdapter.DATABASE_CREATE);
        db.execSQL(CurrencyReturnsDBAdapter.DATABASE_CREATE);

        db.execSQL(CurrencyDBAdapter.DATABASE_CREATE);

        db.execSQL(CurrencyTypeDBAdapter.DATABASE_CREATE);

        db.execSQL(PermissionsDBAdapter.DATABASE_CREATE);
        db.execSQL(ProductDBAdapter.DATABASE_CREATE);
        db.execSQL(ProductOfferDBAdapter.DATABASE_CREATE);

        db.execSQL(ScheduleWorkersDBAdapter.DATABASE_CREATE);
        db.execSQL(SettingsDBAdapter.DATABASE_CREATE);

        db.execSQL(GroupDbAdapter.DATABASE_CREATE);
        db.execSQL(GroupsResourceDbAdapter.DATABASE_CREATE);
        db.execSQL(ClosingReportDBAdapter.DATABASE_CREATE);
        db.execSQL(ClosingReportDetailsDBAdapter.DATABASE_CREATE);
        db.execSQL(InvoiceDBAdapter.DATABASE_CREATE);
        db.execSQL(PosInvoiceDBAdapter.DATABASE_CREATE);
        db.execSQL(ZReportCountDbAdapter.DATABASE_CREATE);
        db.execSQL("insert into " + SettingsDBAdapter.SETTINGS_TABLE_NAME + "  values (1,'','','',0,'',0,'0','0',0,'','','');");
        db.execSQL(EmployeeDBAdapter.DATABASE_CREATE);
        db.execSQL("insert into "+ EmployeeDBAdapter.EMPLOYEE_TABLE_NAME +"  values (1,'user1','user','user','"+new Timestamp(System.currentTimeMillis())+"','1234',0,046316969,20,35,0);");
        db.execSQL("insert into "+ EmployeeDBAdapter.EMPLOYEE_TABLE_NAME +"  values (2,'master','master','master','"+new Timestamp(System.currentTimeMillis())+"','123456',0,046316969,20,35,0);");
        db.execSQL(EmployeePermissionsDBAdapter.DATABASE_CREATE);
        db.execSQL("insert into "+PermissionsDBAdapter.PERMISSIONS_TABLE_NAME+"  values (1 , 'sales cart');");
        db.execSQL("insert into "+PermissionsDBAdapter.PERMISSIONS_TABLE_NAME+"  values (2 , 'report');");
        db.execSQL("insert into "+PermissionsDBAdapter.PERMISSIONS_TABLE_NAME+"  values (3 , 'product');");
        db.execSQL("insert into "+PermissionsDBAdapter.PERMISSIONS_TABLE_NAME+"  values (4 , 'category');");
        db.execSQL("insert into "+PermissionsDBAdapter.PERMISSIONS_TABLE_NAME+"  values (5 , 'employee');");
        db.execSQL("insert into "+PermissionsDBAdapter.PERMISSIONS_TABLE_NAME+"  values (6 , 'Schedule Workers');");
        db.execSQL("insert into "+PermissionsDBAdapter.PERMISSIONS_TABLE_NAME+"  values (7 , 'back up');");
        db.execSQL("insert into "+PermissionsDBAdapter.PERMISSIONS_TABLE_NAME+"  values (8 , 'settings');");
        db.execSQL("insert into "+PermissionsDBAdapter.PERMISSIONS_TABLE_NAME+"  values (9 , 'user club');");
        db.execSQL("insert into "+PermissionsDBAdapter.PERMISSIONS_TABLE_NAME+"  values (10 , 'sales man');");
        db.execSQL("insert into "+PermissionsDBAdapter.PERMISSIONS_TABLE_NAME+"  values (11 , 'offers');");
        db.execSQL("insert into "+PermissionsDBAdapter.PERMISSIONS_TABLE_NAME+"  values (12 , 'inventoryManagement');");

        db.execSQL("insert into "+ EmployeePermissionsDBAdapter.USERPERMISSIONS_TABLE_NAME+" values(1,1,1);");
        db.execSQL("insert into "+ EmployeePermissionsDBAdapter.USERPERMISSIONS_TABLE_NAME+" values(2,2,1);");
        db.execSQL("insert into "+ EmployeePermissionsDBAdapter.USERPERMISSIONS_TABLE_NAME+" values(3,2,3);");
        db.execSQL("insert into "+ EmployeePermissionsDBAdapter.USERPERMISSIONS_TABLE_NAME+" values(4,2,4);");
        db.execSQL("insert into "+ EmployeePermissionsDBAdapter.USERPERMISSIONS_TABLE_NAME+" values(5,2,5);");
        db.execSQL("insert into "+ EmployeePermissionsDBAdapter.USERPERMISSIONS_TABLE_NAME+" values(6,2,6);");
        db.execSQL("insert into "+ EmployeePermissionsDBAdapter.USERPERMISSIONS_TABLE_NAME+" values(7,2,7);");
        db.execSQL("insert into "+ EmployeePermissionsDBAdapter.USERPERMISSIONS_TABLE_NAME+" values(8,2,8);");
        db.execSQL("insert into "+ EmployeePermissionsDBAdapter.USERPERMISSIONS_TABLE_NAME+" values(9,2,9);");
        db.execSQL("insert into "+ EmployeePermissionsDBAdapter.USERPERMISSIONS_TABLE_NAME+" values(10,2,10);");
        db.execSQL("insert into "+ EmployeePermissionsDBAdapter.USERPERMISSIONS_TABLE_NAME+" values(11,2,2);");
        db.execSQL("insert into "+ EmployeePermissionsDBAdapter.USERPERMISSIONS_TABLE_NAME+" values(12,2,11);");
        db.execSQL("insert into "+ EmployeePermissionsDBAdapter.USERPERMISSIONS_TABLE_NAME+" values(13,2,12);");

        /**  db.execSQL("insert into "+Rule1DBAdapter.RULE1_TABLE_NAME+" values(11,2,4.2,2.3);");
         db.execSQL("insert into "+Rule1DBAdapter.RULE1_TABLE_NAME+" values(12,2,3.2,2.3);");
         db.execSQL("insert into "+Rule1DBAdapter.RULE1_TABLE_NAME+" values(13,2,2.3,2.3);");**/
        db.execSQL(PosSettingDbAdapter.DATABASE_CREATE);
        db.execSQL(ProviderDbAdapter.DATABASE_CREATE);
        db.execSQL(InventoryDbAdapter.DATABASE_CREATE);
        db.execSQL(ProductInventoryDbAdapter.DATABASE_CREATE);
        db.execSQL(DrawerDepositAndPullReportDbAdapter.DATABASE_CREATE);
        db.execSQL(DepositAndPullReportDetailsDbAdapter.DATABASE_CREATE);
        db.execSQL(LincessDBAdapter.DATABASE_CREATE);


        // Currency Statment

        Date date=new Date();


    /*   db.execSQL("insert into "+ CurrencyDBAdapter.CURRENCY_TABLE_NAME +"  values (0 , 'Shekel','ILS','Israel Shekel',1,'"+new Timestamp(System.currentTimeMillis())+"');");
        db.execSQL("insert into "+ CurrencyDBAdapter.CURRENCY_TABLE_NAME +"  values (1 , 'Dollar','USD','USA',3.491,'"+new Timestamp(System.currentTimeMillis())+"');");
        db.execSQL("insert into "+ CurrencyDBAdapter.CURRENCY_TABLE_NAME +"  values (2 , 'Pound','GBP','Great Britain',4.5974,'"+new Timestamp(System.currentTimeMillis())+"');");
        db.execSQL("insert into "+ CurrencyDBAdapter.CURRENCY_TABLE_NAME +"  values (3 , 'Euro','EUR','Euro Member Countries',4.1002,'"+new Timestamp(System.currentTimeMillis())+"');");*/

        //Currency Type
        /*db.execSQL("insert into "+CurrencyTypeDBAdapter.CurrencyType_TABLE_NAME+"  values (0 , 'ILS');");
        db.execSQL("insert into "+CurrencyTypeDBAdapter.CurrencyType_TABLE_NAME+"  values (1 , 'USD');");
        db.execSQL("insert into "+CurrencyTypeDBAdapter.CurrencyType_TABLE_NAME+"  values (2 , 'GBP');");
        db.execSQL("insert into "+CurrencyTypeDBAdapter.CurrencyType_TABLE_NAME+"  values (3 , 'EUR');");*/

        db.execSQL("insert into "+ValueOfPointDB.ValueOfPoint_TABLE_NAME+"  values (1,.5,'"+new Timestamp(System.currentTimeMillis())+"');");
//        db.execSQL("insert into "+CustomerDBAdapter.CUSTOMER_TABLE_NAME+"  values (1,'test1','test1','female','11/8/1994','example@gmail.com','coder','123',0,'1',1,1,'1',1,'1',0.0,0.0,0.0);");


        db.execSQL("insert into "+CityDbAdapter.City_TABLE_NAME+"  values (0,'Hifa');");

        db.execSQL("insert into "+ ClubAdapter.Group_TABLE_NAME+"  values (0,'General','General',0,0,0,0,0,0);");
        db.execSQL("insert into "+ ClubAdapter.Group_TABLE_NAME+"  values (1,'Sever','Sever',1,.2,0,0,0,0);");
        db.execSQL("insert into "+ ClubAdapter.Group_TABLE_NAME+"  values (2,'Golden','Golden',2,0,50,200,0,0);");
        db.execSQL("insert into "+ EmployeeDBAdapter.EMPLOYEE_TABLE_NAME +"  values (4,'test1','test1','test1','"+new Timestamp(System.currentTimeMillis())+"','12',0,046316969,20,35,0);");

        db.execSQL(CustomerMeasurementDBAdapter.DATABASE_CREATE);
        db.execSQL(MeasurementsDetailsDBAdapter.DATABASE_CREATE);
        db.execSQL(MeasurementDynamicVariableDBAdapter.DATABASE_CREATE);
        db.execSQL(XReportDBAdapter.DATABASE_CREATE);
        db.execSQL(OfferCategoryDbAdapter.DATABASE_CREATE);
        db.execSQL("insert into "+MeasurementDynamicVariableDBAdapter.MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME+"  values (1,'משקל','Double','KG',0);");
        db.execSQL("insert into "+MeasurementDynamicVariableDBAdapter.MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME+"  values (2,'יד ימין 1','Double','CM',0);");
        db.execSQL("insert into "+MeasurementDynamicVariableDBAdapter.MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME+"  values (3,'יד ימין 2','Double','CM',0);");
        db.execSQL("insert into "+MeasurementDynamicVariableDBAdapter.MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME+"  values (4,'יד שמאל 1','Double','CM',0);");
        db.execSQL("insert into "+MeasurementDynamicVariableDBAdapter.MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME+"  values (5,'יד שמאל 2','Double','CM',0);");
        db.execSQL("insert into "+MeasurementDynamicVariableDBAdapter.MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME+"  values (6,'אחוז שומן 1','Double','%',0);");
        db.execSQL("insert into "+MeasurementDynamicVariableDBAdapter.MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME+"  values (7,'אחוז שומן 2','Double','%',0);");
        db.execSQL("insert into "+MeasurementDynamicVariableDBAdapter.MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME+"  values (8,'מותניים','Double','CM',0);");
        db.execSQL("insert into "+MeasurementDynamicVariableDBAdapter.MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME+"  values (9,'חזה','Double','CM',0);");
        db.execSQL("insert into "+MeasurementDynamicVariableDBAdapter.MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME+"  values (10,'רגל ימין 1','Double','CM',0);");
        db.execSQL("insert into "+MeasurementDynamicVariableDBAdapter.MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME+"  values (11,'רגל ימין 2','Double','CM',0);");
        db.execSQL("insert into "+MeasurementDynamicVariableDBAdapter.MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME+"  values (12,'רגל שמאל 1','Double','CM',0);");
        db.execSQL("insert into "+MeasurementDynamicVariableDBAdapter.MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME+"  values (13,'רגל שמאל 2','Double','CM',0);");
        db.execSQL("insert into "+MeasurementDynamicVariableDBAdapter.MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME+"  values (14,'סוג חלבון','String','E',0);");

        List<String> tblNames = tablesName(db);
        String dbc = IdsCounterDBAdapter.DATABASE_CREATE(tblNames);
        db.execSQL(dbc);
        db.execSQL(IdsCounterDBAdapter.INIT(tblNames));

//        db.execSQL("INSERT INTO products (id, name,barcode,description,price,costPrice,categoryId,byEmployee,status) VALUES (8, 'Test',10,'Test',10,10,1,1,1);");
    }

    public int insertFromFile(Context context, int resourceId) throws IOException {
        DbHelper dbHelper = new DbHelper(context);
        this.db = dbHelper.getWritableDatabase();
        // Reseting Counter
        int result = 0;

        // Open the resource
        InputStream insertsStream = context.getResources().openRawResource(resourceId);
        BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

        // Iterate through lines (assuming each insert has its own line and theres no other stuff)
        while (insertReader.ready()) {
            String insertStmt = insertReader.readLine();
            db.execSQL(insertStmt);
            result++;
        }
        insertReader.close();

        // returning number of inserted rows
        return result;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            switch (oldVersion) {
                case 1:
                    db.execSQL(ProductDBAdapter.DATABASE_UPDATE_FROM_V1_TO_V2[0]);
                    db.execSQL(ProductDBAdapter.DATABASE_UPDATE_FROM_V1_TO_V2[1]);
                    db.execSQL(ProductDBAdapter.DATABASE_UPDATE_FROM_V1_TO_V2[2]);
                    db.execSQL(CategoryDBAdapter.DATABASE_UPDATE_FROM_V1_TO_V2);
                    db.execSQL(PermissionsDBAdapter.FIX_DEPARTMENT_CATEGORY_V2);

                    db.execSQL("drop table offers;");
                    db.execSQL(OfferDBAdapter.DATABASE_CREATE);

                    db.execSQL(GroupDbAdapter.DATABASE_CREATE);
                    db.execSQL(IdsCounterDBAdapter.addColumn(GroupDbAdapter.GROUP_TABLE_NAME));

                    db.execSQL(GroupsResourceDbAdapter.DATABASE_CREATE);
                    db.execSQL(IdsCounterDBAdapter.addColumn(GroupsResourceDbAdapter.GROUPS_RESOURCES_TABLE_NAME));
                    db.execSQL(IdsCounterDBAdapter.addColumn(CategoryDBAdapter.CATEGORY_TABLE_NAME));
                    db.execSQL("update " + IdsCounterDBAdapter.IDS_COUNTER_TABLE_NAME + " set " + CategoryDBAdapter.CATEGORY_TABLE_NAME + "=departments;");
                    db.execSQL("insert into " + PermissionsDBAdapter.PERMISSIONS_TABLE_NAME + "  values (11 , 'inventoryManagement');");
                    db.execSQL("insert into " + EmployeePermissionsDBAdapter.USERPERMISSIONS_TABLE_NAME + " values(12,2,11);");
                    db.execSQL(CurrencyOperationDBAdapter.DATABASE_UPDATE_FROM_V1_TO_V2[0]);
                    db.execSQL(CurrencyOperationDBAdapter.DATABASE_UPDATE_FROM_V1_TO_V2[1]);
                    db.execSQL(CurrencyOperationDBAdapter.DATABASE_UPDATE_FROM_V1_TO_V2[2]);
                    db.execSQL("drop table currency_operation;");
                    db.execSQL(CurrencyOperationDBAdapter.DATABASE_CREATE);
                    //update user permissions

                    ClearSync clearSync = new ClearSync(context);
                    clearSync.execute(context);
                    break;
                case 2:

                    db.execSQL(OrderDBAdapter.addColumnReal("cartDiscount"));
                    db.execSQL(OrderDBAdapter.addColumnReal("numberDiscount"));
                    db.execSQL(CashPaymentDBAdapter.addColumn("currencyRate"));
                    db.execSQL(CashPaymentDBAdapter.addColumn("actualCurrencyRate"));
                    db.execSQL(ZReportDBAdapter.DATABASE_UPDATE_FROM_V2_TO_V3[0]);
                    db.execSQL(ZReportDBAdapter.DATABASE_UPDATE_FROM_V2_TO_V3[1]);
                    db.execSQL(ZReportDBAdapter.DATABASE_UPDATE_FROM_V2_TO_V3[2]);
                    db.execSQL(OrderDetailsDBAdapter.addColumnText("key"));
                    db.execSQL(OrderDBAdapter.addColumnText("key"));
                    db.execSQL(PaymentDBAdapter.addColumn("key"));
                    db.execSQL(PosInvoiceDBAdapter.DATABASE_CREATE);
                    db.execSQL(ClosingReportDetailsDBAdapter.DATABASE_CREATE);
                    db.execSQL(CustomerDBAdapter.addColumn("customerType"));
                    db.execSQL(CustomerDBAdapter.addColumn("customerCode"));
                    db.execSQL(CustomerDBAdapter.addColumn("customerIdentity"));
                    db.execSQL(ClosingReportDBAdapter.DATABASE_CREATE);
                    db.execSQL(IdsCounterDBAdapter.addColumn("closing_report"));
                    db.execSQL(IdsCounterDBAdapter.addColumn("closing_report_details"));
                    db.execSQL(IdsCounterDBAdapter.addColumn("pos_invoice"));
                    db.execSQL("update customer set phoneNumber=1 where id =921530436797;");
                    ClearSync clearSync1 = new ClearSync(context);
                    clearSync1.execute(context);
                    if(!SETTINGS.BufferEmail) {
                        try {
                            Backup.BackupBufferDB();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        BufferDbEmail.sendLogFileBufferDb();
                        SETTINGS.BufferEmail=true;
                    }
                    break;
                case 3:
                    db.execSQL(OrderDBAdapter.DATABASE_UPDATE_FROM_V2_TO_V3[0]);
                    db.execSQL(OrderDBAdapter.DATABASE_UPDATE_FROM_V2_TO_V3[1]);
                    db.execSQL(OrderDBAdapter.DATABASE_UPDATE_FROM_V2_TO_V3[2]);
                    BufferDbEmail.sendLogFileBufferDb();
                    break;
                case 4:
                    db.execSQL(IdsCounterDBAdapter.addColumn("x_report"));
                    db.execSQL(XReportDBAdapter.DATABASE_CREATE);
                    db.execSQL(ProductDBAdapter.addColumnInteger("currencyType"));
                    db.execSQL(ProductDBAdapter.addColumnInteger("branchId"));
                    db.execSQL(ProductDBAdapter.addColumnInteger("offerId"));
                    db.execSQL(IdsCounterDBAdapter.addColumn("OfferCategory"));
                    db.execSQL(OfferCategoryDbAdapter.DATABASE_CREATE);
                    db.execSQL(CategoryDBAdapter.addColumnInteger("branchId"));
                    db.execSQL(ClubAdapter.addColumnInteger("branchId"));
                    db.execSQL(CustomerDBAdapter.addColumnInteger("branchId"));
                    db.execSQL(EmployeeDBAdapter.addColumnInteger("branchId"));
                    db.execSQL(OrderDetailsDBAdapter.addColumnLong("offerId"));
                    db.execSQL(SettingsDBAdapter.addColumnInteger("branchId"));
                    ClearSync clearSync2 = new ClearSync(context);
                    clearSync2.execute(context);

                    break;
                case 5:
                    DATABASE_ENABEL_ALTER_COLUMN = true;
                    db.execSQL(IdsCounterDBAdapter.addColumn("Provider"));
                    db.execSQL(IdsCounterDBAdapter.addColumn("PosSetting"));
                    db.execSQL(IdsCounterDBAdapter.addColumn("Inventory"));
                    db.execSQL(IdsCounterDBAdapter.addColumn("product_inventory"));
                    db.execSQL(IdsCounterDBAdapter.addColumn("z_report_count"));
                    db.execSQL(IdsCounterDBAdapter.addColumn("depositAndPull"));
                    db.execSQL(IdsCounterDBAdapter.addColumn("depositAndPullDetails"));
                    db.execSQL(ZReportCountDbAdapter.DATABASE_CREATE);
                    db.execSQL(ProviderDbAdapter.DATABASE_CREATE);
                    db.execSQL(PosSettingDbAdapter.DATABASE_CREATE);
                    db.execSQL(InventoryDbAdapter.DATABASE_CREATE);
                    db.execSQL(ProductInventoryDbAdapter.DATABASE_CREATE);
                    db.execSQL(OrderDBAdapter.addColumnReal("salesBeforeTax"));
                    db.execSQL(OrderDBAdapter.addColumnReal("salesWithTax"));

                    db.execSQL(ProductDBAdapter.addColumnReal("lastCostPriceInventory"));
                    db.execSQL(ProductDBAdapter.addColumnInteger("with_serial_number"));
                    db.execSQL(CurrencyOperationDBAdapter.addColumnText("payment_way"));
                    db.execSQL(DrawerDepositAndPullReportDbAdapter.DATABASE_CREATE);
                    db.execSQL(DepositAndPullReportDetailsDbAdapter.DATABASE_CREATE);
                    db.execSQL(OrderDetailsDBAdapter.addColumnLong("productSerialNo"));
                    db.execSQL(OrderDetailsDBAdapter.addColumnText("SerialNo"));
                    db.execSQL(OrderDetailsDBAdapter.addColumnReal("paid_amount_after_tax"));
                    db.execSQL(XReportDBAdapter.addColumnReal("pullReportAmount"));
                    db.execSQL(XReportDBAdapter.addColumnReal("depositReportAmount"));
                    db.execSQL(XReportDBAdapter.addColumnReal("salesBeforeTaxReport"));
                    db.execSQL(XReportDBAdapter.addColumnReal("salesWithTaxReport"));
                    db.execSQL(XReportDBAdapter.addColumnReal("totalTaxReport"));
                    db.execSQL(ZReportDBAdapter.addColumnReal("pullReportAmount"));
                    db.execSQL(ZReportDBAdapter.addColumnReal("depositReportAmount"));
                    db.execSQL(ZReportDBAdapter.addColumnText("closeOpenReport"));
                    db.execSQL(ZReportDBAdapter.addColumnReal("salesBeforeTaxReport"));
                    db.execSQL(ZReportDBAdapter.addColumnReal("salesWithTaxReport"));
                    db.execSQL(ZReportDBAdapter.addColumnReal("totalTaxReport"));
                    db.execSQL(PosSettingDbAdapter.addColumnText("companyStatus"));
                    db.execSQL("insert into "+PermissionsDBAdapter.PERMISSIONS_TABLE_NAME+"  values (12 , 'inventoryManagement');");
                    db.execSQL("insert into "+ EmployeePermissionsDBAdapter.USERPERMISSIONS_TABLE_NAME+" values(13,2,12);");
                    ClearSync clearSync3 = new ClearSync(context);
                    clearSync3.execute(context);

                break;
                case 6:
                    db.execSQL(IdsCounterDBAdapter.addColumn("depositAndPull"));
                    db.execSQL(IdsCounterDBAdapter.addColumn("depositAndPullDetails"));
                    db.execSQL(OrderDBAdapter.addColumnReal("salesBeforeTax"));
                    db.execSQL(OrderDBAdapter.addColumnReal("salesWithTax"));
                    db.execSQL(ZReportDBAdapter.addColumnReal("salesBeforeTaxReport"));
                    db.execSQL(ZReportDBAdapter.addColumnReal("salesWithTaxReport"));
                    db.execSQL(ZReportDBAdapter.addColumnReal("totalTaxReport"));
                    db.execSQL(XReportDBAdapter.addColumnReal("salesBeforeTaxReport"));
                    db.execSQL(XReportDBAdapter.addColumnReal("salesWithTaxReport"));
                    db.execSQL(XReportDBAdapter.addColumnReal("totalTaxReport"));
                    db.execSQL(OrderDetailsDBAdapter.addColumnText("SerialNo"));
                    db.execSQL(PosSettingDbAdapter.addColumnText("companyStatus"));
                    break;
                case 7:
                    db.execSQL(OrderDBAdapter.addColumnReal("salesBeforeTax"));
                    db.execSQL(OrderDBAdapter.addColumnReal("salesWithTax"));
                    db.execSQL(ZReportDBAdapter.addColumnReal("salesBeforeTaxReport"));
                    db.execSQL(ZReportDBAdapter.addColumnReal("salesWithTaxReport"));
                    db.execSQL(ZReportDBAdapter.addColumnReal("totalTaxReport"));
                    db.execSQL(XReportDBAdapter.addColumnReal("salesBeforeTaxReport"));
                    db.execSQL(XReportDBAdapter.addColumnReal("salesWithTaxReport"));
                    db.execSQL(XReportDBAdapter.addColumnReal("totalTaxReport"));
                    db.execSQL(OrderDetailsDBAdapter.addColumnText("SerialNo"));
                    db.execSQL(PosSettingDbAdapter.addColumnText("companyStatus"));
                    break;

                case 8:
                    db.execSQL(PosSettingDbAdapter.addColumnText("companyStatus"));
                    break;

                case 9:
                    db.execSQL(LincessDBAdapter.DATABASE_CREATE);
                    db.execSQL(IdsCounterDBAdapter.addColumn(LincessDBAdapter.POS_LINCESS_TABLE_NAME));
                    db.execSQL(ZReportDBAdapter.DATABASE_UPDATE_FROM_V9_TO_V10[0]);
                    db.execSQL(ZReportDBAdapter.DATABASE_UPDATE_FROM_V9_TO_V10[1]);
                    db.execSQL(ZReportDBAdapter.DATABASE_UPDATE_FROM_V9_TO_V10[2]);

                    db.execSQL(XReportDBAdapter.DATABASE_UPDATE_FROM_V9_TO_V10[0]);
                    db.execSQL(XReportDBAdapter.DATABASE_UPDATE_FROM_V9_TO_V10[1]);
                    db.execSQL(XReportDBAdapter.DATABASE_UPDATE_FROM_V9_TO_V10[2]);

                    db.execSQL(ZReportCountDbAdapter.DATABASE_UPDATE_FROM_V9_TO_V10[0]);
                    db.execSQL(ZReportCountDbAdapter.DATABASE_UPDATE_FROM_V9_TO_V10[1]);
                    db.execSQL(ZReportCountDbAdapter.DATABASE_UPDATE_FROM_V9_TO_V10[2]);

                    db.execSQL("drop table Currency;");
                    db.execSQL(CurrencyDBAdapter.DATABASE_CREATE);
                    db.execSQL("insert into "+ CurrencyDBAdapter.CURRENCY_TABLE_NAME +"  values (0 , 'Shekel','ILS','Israel',1,'"+new Timestamp(System.currentTimeMillis())+"');");
                    db.execSQL("drop table CurrencyType;");
                    db.execSQL(CurrencyTypeDBAdapter.DATABASE_CREATE);
                    db.execSQL("insert into "+CurrencyTypeDBAdapter.CurrencyType_TABLE_NAME+"  values (0 , 'ILS');");
                    db.execSQL("update products set currencyType='ILS' where currencyType ='0';");
                    db.execSQL(PosSettingDbAdapter.addColumnInteger("duplicateInvoice"));
                    db.execSQL("update PosSetting set duplicateInvoice='0';");
                    db.execSQL(SettingsDBAdapter.addColumnText("currency_code"));
                    db.execSQL(SettingsDBAdapter.addColumnText("currency_symbol"));
                    db.execSQL(SettingsDBAdapter.addColumnText("country"));
                    db.execSQL("update tbl_settings set currency_code='ILS';");
                    db.execSQL("update tbl_settings set currency_symbol='₪';");
                    db.execSQL("update tbl_settings set country='Israel';");
                    SharedPreferences cSharedPreferences = context.getSharedPreferences("POS_Management", context.MODE_PRIVATE);
                    final SharedPreferences.Editor editor = cSharedPreferences.edit();
                    if (cSharedPreferences != null) {
                        if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY)) {
                            editor.putBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY, false);
                            editor.apply();
                            SETTINGS.enableCurrencies = false;
                            db.execSQL("update PosSetting set enableCurrency='0';");
                        }
                    }

                    db.execSQL(OrderDBAdapter.addColumnReal("salesTotalSaved"));

                    break;
                case 10:
                    db.execSQL(OrderDetailsDBAdapter.DATABASE_UPDATE_FROM_V9_TO_V10[0]);
                    db.execSQL(OrderDetailsDBAdapter.DATABASE_UPDATE_FROM_V9_TO_V10[1]);
                    db.execSQL(OrderDetailsDBAdapter.DATABASE_UPDATE_FROM_V9_TO_V10[2]);
                    break;

            }
        } catch (SQLException e) {
            Log.i("onUpgrade", e.getMessage(), e);
        }
    }


}
