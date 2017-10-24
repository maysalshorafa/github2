package com.pos.leaders.leaderspossystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.*;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyOperationDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyReturnsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencysDBAdapter;

/**
 * Created by Karam on 16/10/2016.
 */

public class DbHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;


    protected static final String DATABASE_NAME = "POSDB.db";
    protected static final String CONTACTS_TABLE_NAME = "contacts";
    protected static final String CONTACTS_COLUMN_ID = "id";
    protected static final String CONTACTS_COLUMN_NAME = "name";
    protected static final String CONTACTS_COLUMN_EMAIL = "email";
    protected static final String CONTACTS_COLUMN_STREET = "street";
    protected static final String CONTACTS_COLUMN_CITY = "place";
    protected static final String CONTACTS_COLUMN_PHONE = "phone";
    private HashMap hp;

    private Context context;

    //21
    public DbHelper(Context context)
    {
        super(context, DATABASE_NAME ,null,1);
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

        //offers
        db.execSQL(Rule1DBAdapter.DATABASE_CREATE);
        db.execSQL(Rule3DbAdapter.DATABASE_CREATE);
        db.execSQL(Rule5DBAdapter.DATABASE_CREATE);
        db.execSQL(Rule7DbAdapter.DATABASE_CREATE);
        db.execSQL(Rule8DBAdapter.DATABASE_CREATE);
        db.execSQL(Rule11DBAdapter.DATABASE_CREATE);
        db.execSQL(OfferRuleDBAdapter.DATABASE_CREATE);
        db.execSQL(OfferDBAdapter.DATABASE_CREATE);

        db.execSQL(GroupAdapter.DATABASE_CREATE);//Club
        db.execSQL(CustomerDBAdapter.DATABASE_CREATE);
        db.execSQL(UsedPoint.DATABASE_CREATE);
        db.execSQL(Sum_PointDbAdapter.DATABASE_CREATE);
        db.execSQL(ValueOfPointDB.DATABASE_CREATE);

        db.execSQL(SaleDBAdapter.DATABASE_CREATE);
        db.execSQL(ChecksDBAdapter.DATABASE_CREATE);
        db.execSQL(DepartmentDBAdapter.DATABASE_CREATE);

        db.execSQL(OrderDBAdapter.DATABASE_CREATE);
        db.execSQL(PaymentDBAdapter.DATABASE_CREATE);

        db.execSQL(CashPaymentDBAdapter.DATABASE_CREATE);
        db.execSQL(CurrencyOperationDBAdapter.DATABASE_CREATE);
        db.execSQL(CurrencyReturnsDBAdapter.DATABASE_CREATE);
       db.execSQL(CurrencysDBAdapter.DATABASE_CREATE);
        db.execSQL(CurrencyTypeDBAdapter.DATABASE_CREATE);

        db.execSQL(PermissionsDBAdapter.DATABASE_CREATE);
        db.execSQL(ProductDBAdapter.DATABASE_CREATE);
        db.execSQL(ProductOfferDBAdapter.DATABASE_CREATE);

        db.execSQL(ScheduleWorkersDBAdapter.DATABASE_CREATE);
        db.execSQL(SettingsDBAdapter.DATABASE_CREATE);
        db.execSQL("insert into " + SettingsDBAdapter.SETTINGS_TABLE_NAME + "  values (1,'','','',0,'',0,'0','0');");
        db.execSQL(UserDBAdapter.DATABASE_CREATE);
        db.execSQL("insert into "+UserDBAdapter.USERS_TABLE_NAME+"  values (1,'Dev','Ops','tec','"+new Date().getTime()+"','1234',0,046316969,20,35,'main screen');");
        db.execSQL("insert into "+UserDBAdapter.USERS_TABLE_NAME+"  values (2,'john','mas','fo','"+new Date().getTime()+"','147',0,046316969,20,35,'sales man');");
        db.execSQL("insert into "+UserDBAdapter.USERS_TABLE_NAME+"  values (3,'karam','karam','jabareen','"+new Date().getTime()+"','741',0,046316969,20,35,'sales man');");

        db.execSQL(UserPermissionsDBAdapter.DATABASE_CREATE);
        db.execSQL(ZReportDBAdapter.DATABASE_CREATE);
        db.execSQL(AReportDBAdapter.DATABASE_CREATE);
        db.execSQL(CityDbAdapter.DATABASE_CREATE);
        db.execSQL(CustomerAssetDB.DATABASE_CREATE);


        List<String> tblNames = tablesName(db);
        String dbc = IdsCounterDBAdapter.DATABASE_CREATE(tblNames);
        db.execSQL(dbc);
        db.execSQL(IdsCounterDBAdapter.INIT(tblNames));



        // Currency Statment
        db.execSQL("insert into "+CurrencysDBAdapter.Currency_TABLE_NAME+"  values (1 , 'Shekel','INS','Israel',1.0,'"+new Date().getTime()+"');");
        db.execSQL("insert into "+CurrencysDBAdapter.Currency_TABLE_NAME+"  values (2 , 'Dollar','USD','USA',3.491,'"+new Date().getTime()+"');");
        db.execSQL("insert into "+CurrencysDBAdapter.Currency_TABLE_NAME+"  values (3 , 'Pound','GBP','Great Britain',4.5974,'"+new Date().getTime()+"');");
        db.execSQL("insert into "+CurrencysDBAdapter.Currency_TABLE_NAME+"  values (4 , 'Euro','EUR','EMU',4.1002,'"+new Date().getTime()+"');");
//Currency Type
        db.execSQL("insert into "+CurrencyTypeDBAdapter.CurrencyType_TABLE_NAME+"  values (1 , 'Shekel');");
        db.execSQL("insert into "+CurrencyTypeDBAdapter.CurrencyType_TABLE_NAME+"  values (2 , 'Dollar');");
        db.execSQL("insert into "+CurrencyTypeDBAdapter.CurrencyType_TABLE_NAME+"  values (3 , 'Pound');");
        db.execSQL("insert into "+CurrencyTypeDBAdapter.CurrencyType_TABLE_NAME+"  values (4 , 'Euro');");

        db.execSQL("INSERT INTO `city` VALUES (0,'TULKAREM');");
        db.execSQL("INSERT INTO `city` VALUES (1,'NUBLUS');");
        db.execSQL("INSERT INTO `club` VALUES (1,'type1','type1',1,0.2,0,0,0);");
        db.execSQL("INSERT INTO `club` VALUES (2,'type2','type2',2,0,50,200,0);");
        db.execSQL("INSERT INTO `club` VALUES (3,'type3','type3',3,0,0,0,0);");
        db.execSQL("INSERT INTO `customer` VALUES (1,'mays','11/8/1994','female','mays94alshorafa@gmail.com','aa','200','tulkarem',0,0,1,0,0,0,0);");
        db.execSQL("INSERT INTO `customer` VALUES (2,'heraa','11/8/1994','male','mays94alshorafa@gmail.com','aa','200','tulkarem',0,0,1,0,0,0,0);");
        db.execSQL("INSERT INTO departments (id, name,byUser) VALUES (3, 'FOOD',1);");
        db.execSQL("INSERT INTO `permissions` VALUES (0,'main screen');");
        db.execSQL("INSERT INTO `permissions` VALUES (1,'report');");
        db.execSQL("INSERT INTO `permissions` VALUES (2,'product');");
        db.execSQL("INSERT INTO `permissions` VALUES (3,'department');");
        db.execSQL("INSERT INTO `permissions` VALUES (4,'back up');");
        db.execSQL("INSERT INTO `permissions` VALUES (5,'offers');");
        db.execSQL("INSERT INTO `permissions` VALUES (6,'tax');");
        db.execSQL("INSERT INTO `permissions` VALUES (7,'main screen');");
        db.execSQL("INSERT INTO `permissions` VALUES (8,'customer club');");
        db.execSQL("INSERT INTO products (id, name,barcode,description,price,costPrice,depId,byUser,status) VALUES (8, 'FOOD1',10,'aa',10,10,3,1,1);");
        //db.execSQL("INSERT INTO `users` (id, userName,firstName,lastName,pwd,phoneNumber,present,hourlyWage,permissions_name) VALUES(3,'mays','mays','mays','',12345,'046316969',20,35,'main screen');");
        db.execSQL("INSERT INTO value_ofPoint (id, value) VALUES (1,.5);");

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
        // TODO Auto-generated method stub
        // Log the version upgrade.
        Log.w("TaskDBAdapter", "Upgrading from version " +oldVersion + " to " +newVersion + ", which will destroy all old data");
        //db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    /*
    * users table
    * insert to table
    * get user by id
    * get all users
    * get by User Name and Password
     */
    public boolean insertUser (String userName, String firstName, String lastName, String creatingDate, String pwd,
                               Integer hide,String phoneNumber,double present,double hourlyWage)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userName", userName);
        contentValues.put("firstName", firstName);
        contentValues.put("lastName", lastName);
        contentValues.put("visitDate", creatingDate);
        contentValues.put("pwd", pwd);
        contentValues.put("hide", hide);
        contentValues.put("phoneNumber", phoneNumber);
        contentValues.put("present", present);
        contentValues.put("hourlyWage", hourlyWage);
        db.insert("users", null, contentValues);
        return true;
    }

   /** public User getUserByID(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from users where id='"+id+"'", null );
        res.moveToFirst();
        User user=null;
        user=new User(getColumnValueInt("id",res),getColumnValue("userName",res),getColumnValue("pwd",res)
                ,getColumnValue("firstName",res),getColumnValue("lastName",res), DateConverter.stringToDate(getColumnValue("visitDate",res))
                ,getColumnValueBoolean("hide",res),getColumnValue("phoneNumber",res)
                ,Double.parseDouble(getColumnValue("present",res)),Double.parseDouble(getColumnValue("hourlyWage",res),getColumnValue("")));
        return user;
    }**/

    private boolean getColumnValueBoolean(String Column, Cursor cr) {
        int b=cr.getInt(cr.getColumnIndex(Column));
        if(b==0)
            return false;
        return true;
    }

    private String getColumnValue(String Column,Cursor cr){
        return cr.getString(cr.getColumnIndex(Column));
    }

    private int getColumnValueInt(String Column, Cursor cr){
        return cr.getInt(cr.getColumnIndex(Column));
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateUser (Integer id, String userName, String firstName, String lastName, Date creatingDate, String pwd,
                               Integer hide, String phoneNumber, double present, double hourlyWage)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userName", userName);
        contentValues.put("firstName", firstName);
        contentValues.put("lastName", lastName);
        contentValues.put("visitDate", creatingDate.getTime());
        contentValues.put("pwd", pwd);
        contentValues.put("hide", hide);
        contentValues.put("phoneNumber", phoneNumber);
        contentValues.put("present", present);
        contentValues.put("hourlyWage", hourlyWage);
        db.update("users", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean deleteUser (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("hide", 1);
        db.update("users",contentValues,"id = ? ",new String[]{Integer.toString(id)});
        return true;
    }
    public int login(String userName,String Password){
        SQLiteDatabase db=this.getReadableDatabase();
        int id=0;

        Cursor res;
        try {
            Log.w("user name is:",userName);
            Log.w("password is :",Password);
            res = db.rawQuery("select * from users where userName='" + userName + "' and pwd='" + Password+"'", null);
            // res=db.query("users", new String[]{"id","userName","pwd"},"userName=? and pwd=?",new String[]{userName,Password},null,null,null);
            res.moveToFirst();
            Log.e("count of rows :",res.getCount()+"");

            if(res.getCount()>0){
                id=res.getInt(res.getColumnIndex("id"));
                Log.i("user id",id+"");
                res.close();
                return id;
            }
            res.close();
            return id;
        }
        catch (Exception sqlEX){

            Log.e("error",sqlEX.getMessage());
            ///// TODO: 17/10/2016 log error
            return id;
        }
    }

    public ArrayList<String> getAllUsers()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from users", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex("firstName")));
            res.moveToNext();
        }
        return array_list;
    }
}