package com.pos.leaders.leaderspossystem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.AReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.AReportDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CategoryDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ChecksDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CityDbAdapter;
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
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeePermissionsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.IdsCounterDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferRuleDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PermissionsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductOfferDBAdapter;
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
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;

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

    public static final int DATABASE_VERSION = 2;

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
        db.execSQL(AReportDBAdapter.DATABASE_CREATE);
        db.execSQL(AReportDetailsDBAdapter.DATABASE_CREATE);
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

        db.execSQL("insert into " + SettingsDBAdapter.SETTINGS_TABLE_NAME + "  values (1,'','','',0,'',0,'0','0');");
        db.execSQL(EmployeeDBAdapter.DATABASE_CREATE);
        db.execSQL("insert into "+ EmployeeDBAdapter.EMPLOYEE_TABLE_NAME +"  values (1,'user1','user','user','"+new Timestamp(System.currentTimeMillis())+"','1234',0,046316969,20,35);");
        db.execSQL("insert into "+ EmployeeDBAdapter.EMPLOYEE_TABLE_NAME +"  values (2,'master','master','master','"+new Timestamp(System.currentTimeMillis())+"','123456',0,046316969,20,35);");
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
      /**  db.execSQL("insert into "+Rule1DBAdapter.RULE1_TABLE_NAME+" values(11,2,4.2,2.3);");
        db.execSQL("insert into "+Rule1DBAdapter.RULE1_TABLE_NAME+" values(12,2,3.2,2.3);");
        db.execSQL("insert into "+Rule1DBAdapter.RULE1_TABLE_NAME+" values(13,2,2.3,2.3);");**/


        // Currency Statment

        Date date=new Date();


        db.execSQL("insert into "+ CurrencyDBAdapter.CURRENCY_TABLE_NAME +"  values (0 , 'Shekel','ILS','Israel Shekel',1,'"+new Timestamp(System.currentTimeMillis())+"');");
        db.execSQL("insert into "+ CurrencyDBAdapter.CURRENCY_TABLE_NAME +"  values (1 , 'Dollar','USD','USA',3.491,'"+new Timestamp(System.currentTimeMillis())+"');");
        db.execSQL("insert into "+ CurrencyDBAdapter.CURRENCY_TABLE_NAME +"  values (2 , 'Pound','GBP','Great Britain',4.5974,'"+new Timestamp(System.currentTimeMillis())+"');");
        db.execSQL("insert into "+ CurrencyDBAdapter.CURRENCY_TABLE_NAME +"  values (3 , 'Euro','EUR','Euro Member Countries',4.1002,'"+new Timestamp(System.currentTimeMillis())+"');");

        //Currency Type
        db.execSQL("insert into "+CurrencyTypeDBAdapter.CurrencyType_TABLE_NAME+"  values (0 , 'ILS');");
        db.execSQL("insert into "+CurrencyTypeDBAdapter.CurrencyType_TABLE_NAME+"  values (1 , 'USD');");
        db.execSQL("insert into "+CurrencyTypeDBAdapter.CurrencyType_TABLE_NAME+"  values (2 , 'GBP');");
        db.execSQL("insert into "+CurrencyTypeDBAdapter.CurrencyType_TABLE_NAME+"  values (3 , 'EUR');");

        db.execSQL("insert into "+ValueOfPointDB.ValueOfPoint_TABLE_NAME+"  values (1,.5,'"+new Timestamp(System.currentTimeMillis())+"');");
        db.execSQL("insert into "+ CategoryDBAdapter.CATEGORY_TABLE_NAME +" values(1, 'כללי','"+ new Timestamp(System.currentTimeMillis()) +"',1,0);");
        db.execSQL("insert into "+CustomerDBAdapter.CUSTOMER_TABLE_NAME+"  values (1,'test1','test1','female','11/8/1994','example@gmail.com','coder','123',0,'1',1,1,'1',1,'1',0.0);");

        db.execSQL("insert into "+CityDbAdapter.City_TABLE_NAME+"  values (0,'Hifa');");

        db.execSQL("insert into "+ ClubAdapter.Group_TABLE_NAME+"  values (0,'General','General',0,0,0,0,0);");
        db.execSQL("insert into "+ ClubAdapter.Group_TABLE_NAME+"  values (1,'Sever','Sever',1,.2,0,0,0);");
        db.execSQL("insert into "+ ClubAdapter.Group_TABLE_NAME+"  values (2,'Golden','Golden',2,0,50,200,0);");
        db.execSQL("insert into "+ EmployeeDBAdapter.EMPLOYEE_TABLE_NAME +"  values (4,'test1','test1','test1','"+new Timestamp(System.currentTimeMillis())+"','12',0,046316969,20,35);");

        db.execSQL(CustomerMeasurementDBAdapter.DATABASE_CREATE);
        db.execSQL(MeasurementsDetailsDBAdapter.DATABASE_CREATE);
        db.execSQL(MeasurementDynamicVariableDBAdapter.DATABASE_CREATE);
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
        switch (oldVersion) {
            case 1:
                db.execSQL(ProductDBAdapter.DATABASE_UPDATE_FROM_V1_TO_V2);
        }
    }
}