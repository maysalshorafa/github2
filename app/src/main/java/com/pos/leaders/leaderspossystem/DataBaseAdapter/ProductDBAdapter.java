package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.ProductStatus;
import com.pos.leaders.leaderspossystem.Models.ProductUnit;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by KARAM on 18/10/2016.
 */

public class ProductDBAdapter {

      //Table name
    public static final String PRODUCTS_TABLE_NAME = "products";
    //column names
    protected static final String PRODUCTS_COLUMN_ID = "id";
    protected static final String PRODUCTS_COLUMN_NAME = "name";
    protected static final String PRODUCTS_COLUMN_BARCODE = "barcode";
    protected static final String PRODUCTS_COLUMN_DESCRIPTION = "description";
    protected static final String PRODUCTS_COLUMN_PRICE = "price";
    protected static final String PRODUCTS_COLUMN_COSTPRICE = "costPrice";
    protected static final String PRODUCTS_COLUMN_WITHTAX = "withTax";
    protected static final String PRODUCTS_COLUMN_CREATINGDATE = "creatingDate";
    protected static final String PRODUCTS_COLUMN_DISENABLED = "hide";
    protected static final String PRODUCTS_COLUMN_CATEGORYID = "categoryId";

    protected static final String PRODUCTS_COLUMN_BYUSER = "byEmployee";

    protected static final String PRODUCTS_COLUMN_with_pos = "with_pos";
    protected static final String PRODUCTS_COLUMN_with_point_system = "with_point_system";
    protected static final String PRODUCTS_COLUMN_STATUS = "status";


    protected static final String PRODUCTS_COLUMN_DISPLAY_NAME = "displayName";
    protected static final String PRODUCTS_COLUMN_REGULAR_PRICE = "regularPrice";
    protected static final String PRODUCTS_COLUMN_STOCK_QUANTITY = "stockQuantity";
    protected static final String PRODUCTS_COLUMN_MANAGE_STOCK = "manageStock";
    protected static final String PRODUCTS_COLUMN_IN_STOCK = "inStock";
    protected static final String PRODUCTS_COLUMN_SKU = "sku";
    protected static final String PRODUCTS_COLUMN_UNIT = "unit";
    protected static final String PRODUCTS_COLUMN_WEIGHT = "weight";


    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    public static final String DATABASE_CREATE = "CREATE TABLE " + PRODUCTS_TABLE_NAME + " ( `" + PRODUCTS_COLUMN_ID + "` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`" + PRODUCTS_COLUMN_NAME + "` TEXT UNIQUE, `" + PRODUCTS_COLUMN_BARCODE + "` TEXT , `" + PRODUCTS_COLUMN_DESCRIPTION + "` TEXT," +
            "`" + PRODUCTS_COLUMN_DISPLAY_NAME + "` TEXT NOT NULL, `" + PRODUCTS_COLUMN_SKU + "` TEXT UNIQUE , `" + PRODUCTS_COLUMN_REGULAR_PRICE + "` REAL," +
            "`" + PRODUCTS_COLUMN_PRICE + "` REAL NOT NULL, `" + PRODUCTS_COLUMN_COSTPRICE + "` REAL, `" + PRODUCTS_COLUMN_WITHTAX + "` INTEGER NOT NULL DEFAULT 1, " +
            "`" + PRODUCTS_COLUMN_STOCK_QUANTITY + "` INTEGER NOT NULL DEFAULT 1, `" + PRODUCTS_COLUMN_MANAGE_STOCK + "` INTEGER NOT NULL DEFAULT 1, `" + PRODUCTS_COLUMN_IN_STOCK + "` INTEGER NOT NULL DEFAULT 1, " +
            "`" +PRODUCTS_COLUMN_CREATINGDATE + "` TIMESTAMP NOT NULL DEFAULT current_timestamp, " +
            "`" + PRODUCTS_COLUMN_DISENABLED + "` INTEGER DEFAULT 0, `" + PRODUCTS_COLUMN_CATEGORYID + "` INTEGER NOT NULL, " +
            "`" + PRODUCTS_COLUMN_BYUSER + "` INTEGER NOT NULL, `" + PRODUCTS_COLUMN_STATUS + "` TEXT NOT NULL DEFAULT 'PUBLISHED' , " +
            "`" + PRODUCTS_COLUMN_with_pos + "` INTEGER NOT NULL DEFAULT 1, `" + PRODUCTS_COLUMN_with_point_system + "` INTEGER NOT NULL DEFAULT 1 ,`"+PRODUCTS_COLUMN_UNIT + "` TEXT NOT NULL DEFAULT 'quantity' , '"+PRODUCTS_COLUMN_WEIGHT+"' REAL DEFAULT 0.0 )";

    public static final String DATABASE_UPDATE_FROM_V1_TO_V2[] = {"alter table products rename to product_v1;", DATABASE_CREATE + "; ",
            "insert into products (id,name,displayName,barcode,sku,description,price,costPrice,regularPrice,withTax,creatingDate,hide,categoryId,byEmployee,with_pos,with_point_system) " +
                    "select id,name,name,barcode,barcode,description,price,costPrice,price,withTax,creatingDate,hide,depId,byEmployee,with_pos,with_point_system from product_v1;"};

    // Variable to hold the database instance
    public SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public ProductDBAdapter(Context context) {
        this.context = context;
        this.dbHelper=new DbHelper(context);
    }

    public ProductDBAdapter open() throws SQLException {
        this.db=dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        db.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }

    public long insertEntry(String name, String barCode, String description, double price, double costPrice,
                            boolean withTax, long categoryId, long byUser , int pos, int point_system,
                            String sku, ProductStatus status, String displayName, double regularPrice, int stockQuantity, boolean manageStock, boolean inStock, ProductUnit unit,double weight) {
        Product p = new Product(Util.idHealth(this.db, PRODUCTS_TABLE_NAME, PRODUCTS_COLUMN_ID), name, barCode, description, price,
                costPrice, withTax,  new Timestamp(System.currentTimeMillis()), categoryId, byUser, pos, point_system, sku, status, displayName, regularPrice, stockQuantity, manageStock, inStock,unit,weight);


        long id = insertEntry(p);
        if (id > 0) {
          /*  Product boProduct = p;
            boProduct.setName(Util.getString(boProduct.getDisplayName()));
            boProduct.setDescription(Util.getString(boProduct.getDescription()));
            boProduct.setBarCode(Util.getString(boProduct.getSku()));*/
            sendToBroker(MessageType.ADD_PRODUCT, p, this.context);
        }
        return id;
    }

    public long insertEntry(Product p) {
        Product product = getProductByBarCode(p.getSku());
            if (p.getStockQuantity() > 0) {
                p.setStatus(ProductStatus.ACTIVE);
            } else if (p.getStockQuantity() == 0) {
                p.setStatus(ProductStatus.OUT_OF_STOCKS);
            }


        if (product != null) {
            Log.i("Inserting product", "barcode is exist");
            if (product.getStatus() == ProductStatus.DELETED) {
                p.setProductId(product.getProductId());
                updateEntry(p);
                return p.getProductId();
            }
            return product.getProductId();
        }
        Product productCheckName = null;
        productCheckName = getByProductName(p.getProductCode());
        if (productCheckName != null) {
            Log.i("Inserting product", "name is busy");
            return productCheckName.getProductId();
        }

        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(PRODUCTS_COLUMN_ID, p.getProductId());
        val.put(PRODUCTS_COLUMN_NAME, p.getProductCode());
        val.put(PRODUCTS_COLUMN_BARCODE, p.getBarCode());
        val.put(PRODUCTS_COLUMN_DESCRIPTION, p.getDescription());
        val.put(PRODUCTS_COLUMN_PRICE, p.getPrice());
        val.put(PRODUCTS_COLUMN_COSTPRICE, p.getCostPrice());
        val.put(PRODUCTS_COLUMN_WITHTAX, p.isWithTax());
        val.put(PRODUCTS_COLUMN_CATEGORYID, p.getCategoryId());
        val.put(PRODUCTS_COLUMN_BYUSER, p.getByEmployee());
        val.put(PRODUCTS_COLUMN_BYUSER, p.getByEmployee());
        val.put(PRODUCTS_COLUMN_with_pos,p.getWithPos());
        val.put(PRODUCTS_COLUMN_with_point_system,p.getWithPointSystem());
        val.put(PRODUCTS_COLUMN_SKU, p.getSku());
        val.put(PRODUCTS_COLUMN_STATUS, p.getStatus().getValue());
        val.put(PRODUCTS_COLUMN_DISPLAY_NAME, p.getDisplayName());
        val.put(PRODUCTS_COLUMN_REGULAR_PRICE, p.getRegularPrice());
        val.put(PRODUCTS_COLUMN_STOCK_QUANTITY, p.getStockQuantity());
        val.put(PRODUCTS_COLUMN_MANAGE_STOCK, p.isManageStock());
        val.put(PRODUCTS_COLUMN_IN_STOCK, p.isInStock());
        val.put(PRODUCTS_COLUMN_UNIT,p.getUnit().getValue());
        val.put(PRODUCTS_COLUMN_WEIGHT,p.getWeight());
        try {
            return db.insert(PRODUCTS_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("ProductDB insertEntry", "inserting Entry at " + PRODUCTS_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public double getProductPrice(long id){
        Cursor cursor = db.rawQuery("select * from " + PRODUCTS_TABLE_NAME + " where id='" + id + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return -1.0;
        }
        cursor.moveToFirst();
        return Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_PRICE)));
    }


    public Product getProductByID(long id) {
        if(id==-1){
            return new Product(-1, context.getResources().getString(R.string.general));
        }
        Product product = null;
        Cursor cursor = db.rawQuery("select * from " + PRODUCTS_TABLE_NAME + " where id='" + id + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return product;
        }
        cursor.moveToFirst();
        product = makeProduct(cursor);
        cursor.close();

        return product;
    }

    public int getProductsCount() {
        String countQuery = "SELECT  * FROM " + PRODUCTS_TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public Product getProductByBarCode(String barcode){
        Product product = null;
        Cursor cursor = db.rawQuery("select * from " + PRODUCTS_TABLE_NAME + " where barcode='" + barcode + "' or sku='" + barcode + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return product;
        }
        cursor.moveToFirst();
        product = makeProduct(cursor);
        cursor.close();

        return product;
    }

    public int deleteEntry(long id) {
        ProductDBAdapter productDBAdapter=new ProductDBAdapter(context);
        productDBAdapter.open();
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(PRODUCTS_COLUMN_DISENABLED, 1);
        updatedValues.put(PRODUCTS_COLUMN_STATUS, ProductStatus.DELETED.getValue());

        String where = PRODUCTS_COLUMN_ID + " = ?";
        try {
            db.update(PRODUCTS_TABLE_NAME, updatedValues, where, new String[]{id + ""});
            //delete all offers for this product
            new OfferDBAdapter(context).deleteEntryByResourceId(id);
            Product product=productDBAdapter.getProductByID(id);
            sendToBroker(MessageType.DELETE_PRODUCT, product, this.context);
            return 1;
        } catch (SQLException ex) {
            Log.e("Product deleteEntry", "enable hide Entry at " + PRODUCTS_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }
    public long deleteEntryBo(Product product) {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(PRODUCTS_COLUMN_DISENABLED, 1);

        String where = PRODUCTS_COLUMN_ID + " = ?";
        try {
            db.update(PRODUCTS_TABLE_NAME, updatedValues, where, new String[]{product.getProductId() + ""});
            //delete all offers for this product
            new OfferDBAdapter(context).deleteEntryByResourceId(product.getProductId());
            return 1;
        } catch (SQLException ex) {
            Log.e("Product deleteEntry", "enable hide Entry at " + PRODUCTS_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public void updateEntry(Product product) {
        ProductDBAdapter productDBAdapter = new ProductDBAdapter(context);
        productDBAdapter.open();
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(PRODUCTS_COLUMN_NAME, product.getProductCode());
        val.put(PRODUCTS_COLUMN_BARCODE, product.getBarCode());
        val.put(PRODUCTS_COLUMN_DESCRIPTION, product.getDescription());
        val.put(PRODUCTS_COLUMN_PRICE, product.getPrice());
        val.put(PRODUCTS_COLUMN_COSTPRICE, product.getCostPrice());
        val.put(PRODUCTS_COLUMN_WITHTAX, product.isWithTax());
        val.put(PRODUCTS_COLUMN_CATEGORYID, product.getCategoryId());
        val.put(PRODUCTS_COLUMN_BYUSER, product.getByEmployee());
        val.put(PRODUCTS_COLUMN_BYUSER, product.getByEmployee());
        val.put(PRODUCTS_COLUMN_SKU, product.getSku());
        val.put(PRODUCTS_COLUMN_STATUS, product.getStatus().getValue());
        val.put(PRODUCTS_COLUMN_DISPLAY_NAME, product.getDisplayName());
        val.put(PRODUCTS_COLUMN_REGULAR_PRICE, product.getRegularPrice());
        val.put(PRODUCTS_COLUMN_STOCK_QUANTITY, product.getStockQuantity());
        val.put(PRODUCTS_COLUMN_MANAGE_STOCK, product.isManageStock());
        val.put(PRODUCTS_COLUMN_IN_STOCK, product.isInStock());
        val.put(PRODUCTS_COLUMN_UNIT,product.getUnit().getValue());
        val.put(PRODUCTS_COLUMN_WEIGHT,product.getWeight());
        String where = PRODUCTS_COLUMN_ID + " = ?";
        db.update(PRODUCTS_TABLE_NAME, val, where, new String[]{product.getProductId() + ""});
        Product p=productDBAdapter.getProductByID(product.getProductId());
        Log.d("Update Object",p.toString());
        sendToBroker(MessageType.UPDATE_PRODUCT, p, this.context);
        productDBAdapter.close();
    }
    public long updateEntryBo(Product product) {
        ProductDBAdapter productDBAdapter = new ProductDBAdapter(context);
        productDBAdapter.open();
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(PRODUCTS_COLUMN_NAME, product.getProductCode());
        val.put(PRODUCTS_COLUMN_BARCODE, product.getBarCode());
        val.put(PRODUCTS_COLUMN_DESCRIPTION, product.getDescription());
        val.put(PRODUCTS_COLUMN_PRICE, product.getPrice());
        val.put(PRODUCTS_COLUMN_COSTPRICE, product.getCostPrice());
        val.put(PRODUCTS_COLUMN_WITHTAX, product.isWithTax());
        val.put(PRODUCTS_COLUMN_CATEGORYID, product.getCategoryId());
        val.put(PRODUCTS_COLUMN_BYUSER, product.getByEmployee());
        val.put(PRODUCTS_COLUMN_BYUSER, product.getByEmployee());
        val.put(PRODUCTS_COLUMN_SKU, product.getSku());
        val.put(PRODUCTS_COLUMN_STATUS, product.getStatus().getValue());
        val.put(PRODUCTS_COLUMN_DISPLAY_NAME, product.getDisplayName());
        val.put(PRODUCTS_COLUMN_REGULAR_PRICE, product.getRegularPrice());
        val.put(PRODUCTS_COLUMN_STOCK_QUANTITY, product.getStockQuantity());
        val.put(PRODUCTS_COLUMN_MANAGE_STOCK, product.isManageStock());
        val.put(PRODUCTS_COLUMN_IN_STOCK, product.isInStock());
        val.put(PRODUCTS_COLUMN_UNIT,product.getUnit().getValue());
        val.put(PRODUCTS_COLUMN_WEIGHT,product.getWeight());
        try {
            String where = PRODUCTS_COLUMN_ID + " = ?";
            db.update(PRODUCTS_TABLE_NAME, val, where, new String[]{product.getProductId() + ""});
            Product p=productDBAdapter.getProductByID(product.getProductId());
            Log.d("Update Object",p.toString());
            productDBAdapter.close();
            return 1;
        } catch (SQLException ex) {
            return 0;
        }
    }

    public List<Product> getAllProducts(){
        List<Product> productsList =new ArrayList<Product>();

        Cursor cursor =  db.rawQuery( "select * from "+PRODUCTS_TABLE_NAME +" where "+ PRODUCTS_COLUMN_DISENABLED +"=0 order by id desc", null );
        cursor.moveToFirst();


        while(!cursor.isAfterLast()){
            productsList.add(makeProduct(cursor));
            cursor.moveToNext();
        }

        return productsList;
    }

	public List<Product> getAllProductsByCategory(long categoryId){
		List<Product> productsList =new ArrayList<Product>();

		Cursor cursor =  db.rawQuery( "select * from "+PRODUCTS_TABLE_NAME+" where "+ PRODUCTS_COLUMN_CATEGORYID +"="+categoryId+" and "+ PRODUCTS_COLUMN_DISENABLED +"=0 order by id desc", null );
		cursor.moveToFirst();

		while(!cursor.isAfterLast()){
            productsList.add(makeProduct(cursor));
            cursor.moveToNext();
		}

		return productsList;
	}

    public List<Product> getAllProductsByCategory(long categoryId, int from , int count){
        List<Product> productsList =new ArrayList<Product>();

        Cursor cursor =  db.rawQuery( "select * from "+PRODUCTS_TABLE_NAME+" where "+ PRODUCTS_COLUMN_CATEGORYID +"="+categoryId+" and "+PRODUCTS_COLUMN_DISENABLED+"=0 order by id desc limit "+from+","+count, null );

        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            productsList.add(makeProduct(cursor));
            cursor.moveToNext();
        }

        return productsList;
    }

    public List<Product> getTopProducts(int from ,int count){
        List<Product> productsList =new ArrayList<Product>();
        //SELECT * FROM table limit 100, 200
        Cursor cursor =  db.rawQuery( "select * from "+PRODUCTS_TABLE_NAME +" where "+ PRODUCTS_COLUMN_DISENABLED +"=0 order by id desc limit "+from+","+count, null );
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            productsList.add(makeProduct(cursor));
            cursor.moveToNext();
        }

        return productsList;
    }

    private Product makeProduct(Cursor cursor){
        int withTaxValue = cursor.getInt(cursor.getColumnIndex(PRODUCTS_COLUMN_WITHTAX));

        boolean withTaxStatus ,weighableStatus =false ;
        if(withTaxValue==1){
            withTaxStatus=true;
        }else {
            withTaxStatus=false;
        }

        Product p = new Product(
                Long.parseLong(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_BARCODE)),
                cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_DESCRIPTION)),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_PRICE))),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_COSTPRICE))),
                withTaxStatus, Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_CREATINGDATE))),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_DISENABLED))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_CATEGORYID))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_BYUSER))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_with_pos))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_with_point_system))),
                cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_SKU)),
                ProductStatus.valueOf(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_STATUS))),
                cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_DISPLAY_NAME)),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_REGULAR_PRICE))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_STOCK_QUANTITY))),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_MANAGE_STOCK))),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_IN_STOCK))), ProductUnit.valueOf(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_UNIT)).toUpperCase()),Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_WEIGHT)))
        );
        if(p.getDescription()==null){
            p.setDescription("");
        }
        if(p.getDisplayName()==null){
            p.setDisplayName("");
        }
        if(Double.isNaN(p.getCostPrice())){
            p.setCostPrice(0.0f);
        }
        if(Double.isNaN(p.getRegularPrice())){
            p.setRegularPrice(p.getPrice());
        }
        return p;
    }
    public boolean availableProductName(String productName) {
        Cursor cursor = db.query(PRODUCTS_TABLE_NAME, null, PRODUCTS_COLUMN_NAME + "=?", new String[]{productName}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            //Product Name not available
            return false;
        }
        // Product Name available
        return true;
    }
    public Product getByProductName(String productName) {
        Cursor cursor;
        try {
            cursor = db.query(PRODUCTS_TABLE_NAME, null, PRODUCTS_COLUMN_NAME + "=?", new String[]{productName}, null, null, null);
        } catch (Exception e){
            return null;
        }
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            //Product Name not available
            return makeProduct(cursor);
        }
        // Product Name available
        return null;
    }

    public List<Product> getAllProductsByHint(String hint , int from , int count ){
        List<Product> productsList =new ArrayList<Product>();

        Cursor cursor =  db.rawQuery("select * from " + PRODUCTS_TABLE_NAME +" where "+ PRODUCTS_COLUMN_BARCODE +" like '%"+
                hint+"%' OR " + PRODUCTS_COLUMN_DESCRIPTION+" like '%"+ hint +"%' OR "+PRODUCTS_COLUMN_NAME+" like '%"+ hint+"%'" +" and "+ PRODUCTS_COLUMN_DISENABLED +"=0 order by id desc limit "+from+","+count, null );

        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            productsList.add(makeProduct(cursor));
            cursor.moveToNext();
        }

        return productsList;
    }
    // methode to test if barcode is UNIQUE
    public Boolean isValidSku(String sku) {
        Cursor cursor = db.query(PRODUCTS_TABLE_NAME, null, PRODUCTS_COLUMN_SKU + "=?", new String[]{sku}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {

            return false;
        }
        return true;
    }
    public Product getLastRow() throws Exception {
        Product product = null;
        Cursor cursor = db.rawQuery("select * from " + PRODUCTS_TABLE_NAME +" order by id desc", null);
        if (cursor.getCount() < 1) // zReport Not Exist
        {
            cursor.close();
            throw new Exception("there is no rows on  product Table");
        }
        cursor.moveToFirst();
        product = makeProduct(cursor);
        cursor.close();

        return product;
    }
}
