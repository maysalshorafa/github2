package com.pos.leaders.leaderspossystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CategoryDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Category;
import com.pos.leaders.leaderspossystem.Models.Currency.Currency;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.ProductStatus;
import com.pos.leaders.leaderspossystem.Models.ProductUnit;
import com.pos.leaders.leaderspossystem.Tools.ProductCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.ProductCategoryGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.Tools.updateCurrencyType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KARAM on 18/10/2016.
 */

public class ProductsActivity  extends AppCompatActivity  {
    private CategoryDBAdapter departmentDBAdapter;
    private ProductDBAdapter productDBAdapter;
    ArrayAdapter<String> LAdapter;
    List<Category> listDepartment;
    List<String> departmentsName;
    Spinner productUnitSp , SpProductCurrency , SpProductBranch;
    Button btSave,btnCancel;
    EditText etName,etBarcode,etDescription, etPriceWithTax,etCostPrice,etInventoryCostPrice,etDisplayName,etSku,etStockQuantity,etProductWeight , etPriceWithOutTax;
    Switch swWithTax,swManageStock ,swWithSerialNo;
    static ListView lvDepartment;
    Map<String,Long> departmentMap=new HashMap<String,Long>();


    String selectedProduct="";
    String selectedDepartment="";
    int with_pos=1;
    int with_point_system=1;

    View previouslySelectedProductItem = null;
    View previouslySelectedItem = null;

    ProductCatalogGridViewAdapter adapter;
    private Product editableProduct , lastProduct;
    long check;
    long depID;

    /////Defualt new product withTax and manageStock
    boolean withTax=true , manageStock=true ,withSerialNo= false;
    ProductUnit unit ;
    LinearLayout llWeight;
    String currencyType="";
    List<Currency> currenciesList;
    private List<CurrencyType> currencyTypesList = null;
    private List<String> currenciesNames = null;
    int branchId=0,positionItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_products);

        TitleBar.setTitleBar(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get Refferences of Views
        lvDepartment =(ListView)findViewById(R.id.LVDepartment);

        btSave=(Button)findViewById(R.id.productActivity_btnSave);
        btnCancel=(Button)findViewById(R.id.productActivity_btnCancel);
        etName=(EditText)findViewById(R.id.ETName);
        etBarcode=(EditText)findViewById(R.id.ETBarCode);
        etDescription=(EditText)findViewById(R.id.ETDescription);
        etPriceWithTax =(EditText)findViewById(R.id.ETPrice_withTax);
        etPriceWithOutTax=(EditText)findViewById(R.id.ETPrice_withOutTax);
        etCostPrice=(EditText)findViewById(R.id.ETCostPrice);
        etInventoryCostPrice=(EditText)findViewById(R.id.ETInventoryCostPrice);

        etDisplayName = (EditText) findViewById(R.id.ETDDisplayName);
        etSku = (EditText) findViewById(R.id.ETSku);
        etStockQuantity = (EditText) findViewById(R.id.ETStockQuantity);
        swManageStock = (Switch) findViewById(R.id.SWManageStock);

        swWithTax=(Switch)findViewById(R.id.SWWithTax);
        //swWithTax.setChecked(false);
        swWithSerialNo=(Switch)findViewById(R.id.SWWithSerialNo);
        productUnitSp = (Spinner)findViewById(R.id.SpProductUnit);
        SpProductCurrency = (Spinner)findViewById(R.id.SpProductCurrency);
        if (!SETTINGS.enableCurrencies){
            SpProductCurrency.setEnabled(false);}
        llWeight = (LinearLayout)findViewById(R.id.llWeight);
        etProductWeight = (EditText)findViewById(R.id.ETWeight);
        SpProductBranch = (Spinner)findViewById(R.id.SpBranchId);
        final List<String> productBranch = new ArrayList<String>();
        productBranch.add(getString(R.string.all));
        productBranch.add(getString(R.string.pos_branch));
        final ArrayAdapter<String> dataAdapterBranch = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, productBranch);
        dataAdapterBranch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpProductBranch.setAdapter(dataAdapterBranch);
        productDBAdapter = new ProductDBAdapter(this);
        productDBAdapter.open();
        final List<ProductUnit>productUnit = new ArrayList<ProductUnit>();
        productUnit.add(ProductUnit.QUANTITY);
        productUnit.add(ProductUnit.BARCODEWITHWEIGHT);
        productUnit.add(ProductUnit.BARCODEWITHPRICE);
        productUnit.add(ProductUnit.GENERALPRICEPRODUCT);

        productUnit.add(ProductUnit.WEIGHT);
        final List<String>productUnitString = new ArrayList<String>();
        productUnitString.add(ProductUnit.QUANTITY.getValue());
        productUnitString.add(ProductUnit.BARCODEWITHWEIGHT.getValue());
        productUnitString.add(ProductUnit.BARCODEWITHPRICE.getValue());
        productUnitString.add(ProductUnit.GENERALPRICEPRODUCT.getValue());

        productUnitString.add(ProductUnit.WEIGHT.getValue());
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, productUnitString);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        productUnitSp.setAdapter(dataAdapter);
        //region spinner
        //Getting default currencies name and values
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(this);
        currencyTypeDBAdapter.open();
        currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
        currencyTypeDBAdapter.close();
        //get currency value
        CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(ProductsActivity.this);
        currencyDBAdapter.open();
        currenciesList = currencyDBAdapter.getAllCurrencyLastUpdate(currencyTypesList);
        currencyDBAdapter.close();

        currenciesNames = new ArrayList<String>();
        for (int i = 0; i < currencyTypesList.size(); i++) {
            currenciesNames.add(currencyTypesList.get(i).getType());
        }


        for (int i=0;i<currenciesNames.size();i++){
            if (currenciesNames.get(i).equals(SETTINGS.currencyCode))
                positionItem=i;
        }

        // Creating adapter for spinner
        final ArrayAdapter<String> dataAdapterCurrency = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currenciesNames);
        // Drop down layout style - list view with radio button
        // attaching data adapter to spinner
        dataAdapterCurrency.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        SpProductCurrency.setAdapter(dataAdapterCurrency);
        SpProductCurrency.setSelection(positionItem);


        //region Add product button
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addEditProduct())
                    setNewProduct();
            }
        });



        //set Price with Tax
        etPriceWithOutTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (withTax){
                if (!etPriceWithOutTax.getText().toString().isEmpty()&&!etPriceWithOutTax.getText().toString().equals("")) {
                    Double priceWithTax = priceWithTax(Double.valueOf(etPriceWithOutTax.getText().toString()));
                    etPriceWithTax.setText(Util.makePrice(priceWithTax));
                }}
                else {
                    if (!etPriceWithOutTax.getText().toString().isEmpty()&&!etPriceWithOutTax.getText().toString().equals("")) {
                        Double priceWithTax = Double.valueOf(etPriceWithOutTax.getText().toString());
                        etPriceWithTax.setText(Util.makePrice(priceWithTax));
                    }
                }
            }
        });


//        set Price without Tax
        etPriceWithTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (withTax){
                if (!etPriceWithTax.getText().toString().isEmpty()&&!etPriceWithTax.getText().toString().equals("")) {
                    Double priceWithOutTax = priceWithOutTax(Double.valueOf(etPriceWithTax.getText().toString()));
                    etPriceWithOutTax.setText(Util.makePrice(priceWithOutTax));
                }}
                else {
                    if (!etPriceWithTax.getText().toString().isEmpty()&&!etPriceWithTax.getText().toString().equals("")) {
                        Double priceWithOutTax = Double.valueOf(etPriceWithTax.getText().toString());
                        etPriceWithOutTax.setText(Util.makePrice(priceWithOutTax));
                    }
                }
            }
        });



        //endregion

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    departmentDBAdapter.close();
                    productDBAdapter.close();}
                catch (Exception ex){}
                /** Intent intent = new Intent(ProductsActivity.this, ProductCatalogActivity.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 startActivity(intent); **/
                onBackPressed();
            }
        });

        // Category list
        departmentDBAdapter = new CategoryDBAdapter(this);
        departmentDBAdapter.open();


        departmentsName=new ArrayList<String>();
        listDepartment = departmentDBAdapter.getAllDepartments();
        final ProductCategoryGridViewAdapter departmentGridViewAdapter = new ProductCategoryGridViewAdapter(this, listDepartment);

        lvDepartment.setAdapter(departmentGridViewAdapter);
        lvDepartment.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        //categoryDBAdapter.close();

        for (Category d : listDepartment) {
            departmentsName.add(d.getName());
            departmentMap.put(d.getName(),d.getCategoryId());
        }
/**
 LAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,departmentsName);
 lvDepartment.setAdapter(LAdapter);
 lvDepartment.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
 lvDepartment.setItemChecked(1,true);
 **/

        lvDepartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                for (Category category:listDepartment){
                    category.setChecked(false);
                }
                Category department = listDepartment.get(position);
                department.setChecked(!department.isChecked());
                listDepartment.set(position, department);
                departmentGridViewAdapter.updateRecords(listDepartment);
            }
        });
        swWithTax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                if(isChecked){
                    withTax=false; //edit here
                    if (!etPriceWithTax.getText().toString().isEmpty()&&!etPriceWithTax.getText().toString().equals("")) {
                        Double priceWithOutTax = Double.valueOf(etPriceWithTax.getText().toString());
                        etPriceWithOutTax.setText(Util.makePrice(priceWithOutTax));
                        etPriceWithTax.setText(Util.makePrice(priceWithOutTax));
                    }
                   else if (!etPriceWithOutTax.getText().toString().isEmpty()&&!etPriceWithOutTax.getText().toString().equals("")) {
                        Double priceWithOutTax = Double.valueOf(etPriceWithOutTax.getText().toString());
                        etPriceWithTax.setText(Util.makePrice(priceWithOutTax));
                    }

                }else{
                    withTax=true; //edit here
                    if (!etPriceWithTax.getText().toString().isEmpty()&&!etPriceWithTax.getText().toString().equals("")) {
                        Double priceWithOutTax = priceWithOutTax(Double.valueOf(etPriceWithTax.getText().toString()));
                        etPriceWithOutTax.setText(Util.makePrice(priceWithOutTax));
                    }

                }

            }
        });
        swWithSerialNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                if(isChecked){
                    withSerialNo=true; //edit here
                }else{
                    withSerialNo=false; //edit here
                }

            }
        });

        swManageStock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    manageStock=false;
                    Log.d("manageStock",manageStock+"");
                }
                else {
                    manageStock=true;
                    Log.d("manageStock",manageStock+"");
                }


            }
        });
        Log.d("manageStockdefult",manageStock+"");
        productUnitSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unit = productUnit.get(position);
                if(unit.getValue().equalsIgnoreCase(ProductUnit.BARCODEWITHWEIGHT.getValue())){
                    llWeight.setVisibility(View.VISIBLE);
                }else {
                    llWeight.setVisibility(View.GONE);
                }
                if(unit.getValue().equalsIgnoreCase(ProductUnit.BARCODEWITHPRICE.getValue())){
                    llWeight.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Bundle extras = getIntent().getExtras();

        if(extras!=null) {

            if (extras.containsKey("barcode")) {
                try {
                    etBarcode.setText(extras.getString("barcode"));
                } catch (Exception ex) {
                }
            }

            if (extras.containsKey("productID")) {
                try {
                    editableProduct = productDBAdapter.getProductByID(extras.getLong("productID"));
                    Log.d("editableProduct",editableProduct.toString());

                    etName.setText(editableProduct.getProductCode());
                    etDisplayName.setText(editableProduct.getDisplayName());
                    etBarcode.setText(editableProduct.getBarCode());
                    etSku.setText(editableProduct.getSku());
                    etDescription.setText(editableProduct.getDescription());
                    etCostPrice.setText(editableProduct.getCostPrice() + "");
                    etInventoryCostPrice.setText(editableProduct.getLastCostPriceInventory() + "");
                    etPriceWithTax.setText(editableProduct.getPriceWithTax() + "");
                    etPriceWithOutTax.setText(editableProduct.getPriceWithOutTax() + "");
                    etStockQuantity.setText(editableProduct.getStockQuantity() + "");
                  //  swWithTax.setChecked(editableProduct.isWithTax());
                 if (!editableProduct.isWithTax()) {
                     swWithTax.setChecked(true);
                 }
                    swWithSerialNo.setChecked(editableProduct.isWithSerialNumber());
                    if(editableProduct.getUnit().getValue().equals(ProductUnit.WEIGHT.getValue())){
                        llWeight.setVisibility(View.VISIBLE);
                        etProductWeight.setText(editableProduct.getWeight()+"");
                    }else {
                        llWeight.setVisibility(View.GONE);
                    }
                    Category d = departmentDBAdapter.getDepartmentByID(editableProduct.getCategoryId());
                    swManageStock.setChecked(editableProduct.isManageStock());
                    for (int i = 0; i < productUnit.size(); i++) {
                        ProductUnit productUnit1 = productUnit.get(i);
                        if (productUnit1 == editableProduct.getUnit()) {
                            productUnitSp.setSelection(i);

                        }
                    }
                    for (int i = 0; i < currencyTypesList.size(); i++) {
                        CurrencyType currencyType1 = currencyTypesList.get(i);
                        if (editableProduct.getCurrencyType().equals("0")){
                            updateCurrencyType.updateCurrencyToShekl(ProductsActivity.this,editableProduct);
                        }
                        if (currencyType1.getType() == editableProduct.getCurrencyType()) {
                            SpProductCurrency.setSelection(i);

                        }
                    }
                    selectedDepartment = d.getName();
                    for (Category dep : listDepartment) {
                        if (dep.getCategoryId() == (editableProduct.getCategoryId())) {
                            dep.setChecked(true);
                        }
                    }
                    departmentGridViewAdapter.updateRecords(listDepartment);
                    if(editableProduct.getBranchId()==0){
                        SpProductBranch.setSelection(0);
                    }else {
                        SpProductBranch.setSelection(1);
                    }
                    if (ProductCatalogActivity.Product_Management_Edit == 8) {
                        btSave.setText(getString(R.string.edit));
                        ProductCatalogActivity.Product_Management_Edit =0;

                    }
                    if (ProductCatalogActivity.Product_Management_View == 9) {
                        btSave.setVisibility(View.GONE);
                        etName.setEnabled(false);
                        etBarcode.setEnabled(false);
                        etDescription.setEnabled(false);
                        etCostPrice.setEnabled(false);
                        etInventoryCostPrice.setEnabled(false);
                        etPriceWithTax.setEnabled(false);
                        etPriceWithOutTax.setEnabled(false);
                        etDisplayName.setEnabled(false);
                        etStockQuantity.setEnabled(false);
                        etSku.setEnabled(false);
                        if(llWeight.getVisibility()==View.VISIBLE){}
                        etProductWeight.setEnabled(false);
                        ProductCatalogActivity.Product_Management_View =0;

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }else {
            try {
                lastProduct = productDBAdapter.getLastRow();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(editableProduct==null){
                if(lastProduct!=null){
                    if(Util.isInteger(lastProduct.getProductCode())){
                        long a = Long.parseLong(lastProduct.getProductCode())+1;
                        etName.setText(a+"");
                    }else {
                        etName.setText(lastProduct.getProductCode().concat("1"));
                    }
                }}
        }

    }

    private boolean addEditProduct() {
        double price=0 , costPrice=0 , weight=0 , inventoryCostPrice=0 ,priceWithOut=0;
        int stockQuantity = 0;
        String tempBarcode = etBarcode.getText().toString();
        String newBarCode="";

        if (tempBarcode.contains("\r\n")) {
            newBarCode =tempBarcode.replace("\r\n", "");
        } else if (tempBarcode.contains("\r")) {
            newBarCode = tempBarcode.replace("\r", "");
        } else if (tempBarcode.contains("\n")) {
            newBarCode =tempBarcode.replace("\n", "");
        }else {
            newBarCode=tempBarcode;
        }

        if (editableProduct == null) {
            String currency = SpProductCurrency.getSelectedItem().toString();
            String currencyId=null;
            for(int i=0; i<currencyTypesList.size();i++) {
                if (currencyTypesList.get(i).getType() == currency) {
                    currencyId = currencyTypesList.get(i).getType();
                }
            }
            if(SpProductBranch.getSelectedItem().toString().equals(getString(R.string.all))){
                branchId=0;
            }else {
                branchId= SETTINGS.branchId;

            }
            if(!etBarcode.getText().toString().equals("")) {
                etBarcode.setText(newBarCode);
            }
            ///to save on sqlite
            if (etName.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), getString(R.string.insert_product_name), Toast.LENGTH_LONG).show();
                etName.setBackgroundResource(R.drawable.backtext);
            } else if (etDisplayName.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), getString(R.string.insert_product_dispaly_name), Toast.LENGTH_LONG).show();
                etDisplayName.setBackgroundResource(R.drawable.backtext);
            } else if (etBarcode.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), getString(R.string.insert_product_barcode), Toast.LENGTH_LONG).show();
                etBarcode.setBackgroundResource(R.drawable.backtext);
            } else if (etPriceWithTax.getText().toString().equals("") ) {
                Toast.makeText(getApplicationContext(), getString(R.string.insert_product_price), Toast.LENGTH_LONG).show();
                etPriceWithTax.setBackgroundResource(R.drawable.backtext);
            }
            else if(llWeight.getVisibility()==View.VISIBLE&&etProductWeight.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), getString(R.string.insert_product_weight), Toast.LENGTH_LONG).show();
                etProductWeight.setBackgroundResource(R.drawable.backtext);
            }

            else{
                boolean availableBarCode= productDBAdapter.isValidSku(etSku.getText().toString());
                boolean availableProductName= productDBAdapter.availableProductName(etName.getText().toString());
                for (Category d : listDepartment) {
                    if (d.isChecked()) {
                        depID = d.getCategoryId();
                    }
                }
                if(availableProductName&&availableBarCode&& !etPriceWithTax.getText().toString().equals("")){
                    if(depID==0){
                        Toast.makeText(ProductsActivity.this,R.string.please_insert_category_name,Toast.LENGTH_LONG).show();

                    }else {
                        if(!etPriceWithTax.getText().toString().equals("")){
                            price=Double.parseDouble(etPriceWithTax.getText().toString());
                        }
                        if(!etPriceWithOutTax.getText().toString().equals("")){
                            priceWithOut=Double.parseDouble(etPriceWithOutTax.getText().toString());
                        }
                        if (etSku.getText().toString().equals("")) {
                            etSku.setText(etBarcode.getText().toString());
                        }
                        if (etStockQuantity.getText().toString().equals("")) {
                            stockQuantity = 0;
                        }else{
                            stockQuantity = Integer.parseInt(etStockQuantity.getText().toString());
                        }
                        if(!etCostPrice.getText().toString().equals("")){
                            costPrice=Double.parseDouble(etCostPrice.getText().toString());
                        }
                        if(!etInventoryCostPrice.getText().toString().equals("")){
                            inventoryCostPrice=Double.parseDouble(etInventoryCostPrice.getText().toString());
                        }
                        if(!etProductWeight.getText().toString().equals("")){
                            weight=Double.parseDouble(etProductWeight.getText().toString());
                        }
                        check = productDBAdapter.insertEntry(etName.getText().toString(), etBarcode.getText().toString(),
                                etDescription.getText().toString(), price, costPrice, withTax, depID, SESSION._EMPLOYEE.getEmployeeId(), with_pos, with_point_system,
                                etSku.getText().toString(), ProductStatus.PUBLISHED, etDisplayName.getText().toString(), price, stockQuantity, manageStock, (stockQuantity > 0),unit,weight,currencyId,branchId,0,inventoryCostPrice,withSerialNo,priceWithOut);


                        if (check > 0) {
                            Toast.makeText(getApplicationContext(), getString(R.string.success_to_add_product), Toast.LENGTH_LONG).show();
                            return true;
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.fail_to_add_product), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else {
                    if(!availableProductName) {
                        etName.setBackgroundResource(R.drawable.backtext);
                        Toast.makeText(getApplicationContext(), getString(R.string.product_name_not_available), Toast.LENGTH_LONG).show();
                        return false;
                    }
                    if(!availableBarCode) {
                        etSku.setBackgroundResource(R.drawable.backtext);
                        Toast.makeText(getApplicationContext(), getString(R.string.product_barcode_not_available), Toast.LENGTH_LONG).show();
                        return false;
                    }

                }}


        } else {

            String currency = SpProductCurrency.getSelectedItem().toString();
            String currencyId=null;
            for(int i=0; i<currencyTypesList.size();i++){
                if(currencyTypesList.get(i).getType()==currency){
                    currencyId=  currencyTypesList.get(i).getType();
                }
            }

            if(SpProductBranch.getSelectedItem().toString().equals(getString(R.string.all))){
                branchId=0;
            }else {
                branchId= SETTINGS.branchId;

            }
            for (Category d : listDepartment) {
                if (d.isChecked()) {
                    depID = d.getCategoryId();
                }
            }
            if(!etBarcode.getText().toString().equals("")) {
                etBarcode.setText(newBarCode);
            }
            ///to save on sqlite
            if (etName.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), getString(R.string.insert_product_name), Toast.LENGTH_LONG).show();
                etName.setBackgroundResource(R.drawable.backtext);
            } else if (etDisplayName.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), getString(R.string.insert_product_dispaly_name), Toast.LENGTH_LONG).show();
                etDisplayName.setBackgroundResource(R.drawable.backtext);
            } else if (etBarcode.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), getString(R.string.insert_product_barcode), Toast.LENGTH_LONG).show();
                etBarcode.setBackgroundResource(R.drawable.backtext);
            } else if (etSku.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), getString(R.string.insert_product_sku), Toast.LENGTH_LONG).show();
                etSku.setBackgroundResource(R.drawable.backtext);
            } else if (etPriceWithTax.getText().toString().equals("") ) {
                Toast.makeText(getApplicationContext(), getString(R.string.insert_product_price), Toast.LENGTH_LONG).show();
                etPriceWithTax.setBackgroundResource(R.drawable.backtext);
            }
            else if(llWeight.getVisibility()==View.VISIBLE&&etProductWeight.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), getString(R.string.insert_product_weight), Toast.LENGTH_LONG).show();
                etProductWeight.setBackgroundResource(R.drawable.backtext);
            }else {
                if (etSku.getText().toString().equals("")) {
                    etSku.setText(etBarcode.getText().toString());
                }
                if(!etPriceWithTax.getText().toString().equals("")){
                    price=Double.parseDouble(etPriceWithTax.getText().toString());
                }else {
                    price=editableProduct.getPriceWithTax();
                }
                if(!etPriceWithOutTax.getText().toString().equals("")){
                    priceWithOut=Double.parseDouble(etPriceWithOutTax.getText().toString());
                }else {
                    priceWithOut=editableProduct.getPriceWithOutTax();
                }
                if(!etCostPrice.getText().toString().equals("")){
                    costPrice=Double.parseDouble(etCostPrice.getText().toString());
                }
                else {
                    costPrice=editableProduct.getCostPrice();
                }
                if(!etInventoryCostPrice.getText().toString().equals("")){
                    inventoryCostPrice=Double.parseDouble(etInventoryCostPrice.getText().toString());
                }
                else {
                    inventoryCostPrice=editableProduct.getLastCostPriceInventory();
                }
                if (etStockQuantity.getText().toString().equals("")) {
                    stockQuantity = 0;
                }else{
                    stockQuantity = Integer.parseInt(etStockQuantity.getText().toString());
                }
                if(llWeight.getVisibility()==View.VISIBLE&&!(etProductWeight.getText().toString().equals(""))){
                    weight=Double.parseDouble(etProductWeight.getText().toString());
                }
                //// TODO: 27/10/2016 edit product

                if(depID==0){
                    Toast.makeText(ProductsActivity.this,R.string.please_insert_category_name,Toast.LENGTH_LONG).show();
                }else {
                    editableProduct.setCategoryId(depID);
                    editableProduct.setProductCode(etName.getText().toString());
                    editableProduct.setDisplayName(etDisplayName.getText().toString());
                    editableProduct.setBarCode(etBarcode.getText().toString());
                    editableProduct.setSku(etSku.getText().toString());
                    editableProduct.setDescription(etDescription.getText().toString());
                    editableProduct.setStockQuantity(stockQuantity);
                    editableProduct.setPriceWithTax(price);
                    editableProduct.setPriceWithOutTax(priceWithOut);
                    editableProduct.setCostPrice(costPrice);
                    editableProduct.setLastCostPriceInventory(inventoryCostPrice);
                    editableProduct.setWithTax(withTax);
                    editableProduct.setManageStock(manageStock);
                    editableProduct.setInStock(stockQuantity>0);
                    editableProduct.setUnit(unit);
                    editableProduct.setCurrencyType(currencyId);
                    editableProduct.setBranchId(branchId);

                    if(llWeight.getVisibility()==View.VISIBLE) {
                        editableProduct.setWeight(weight);
                    }
                    try {
                        productDBAdapter.open();
                        productDBAdapter.updateEntry(editableProduct);
                        Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.success_to_update_product), Toast.LENGTH_SHORT);
                        onBackPressed();
                        return true;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.error_to_update_product), Toast.LENGTH_SHORT);
                    }
                }

                //setNewProduct();
            }
        }
        return false;
    }

    private void setNewProduct() {
        etName.setBackgroundResource(R.drawable.catalogproduct_item_bg);
        etBarcode.setBackgroundResource(R.drawable.catalogproduct_item_bg);
        etPriceWithTax.setBackgroundResource(R.drawable.catalogproduct_item_bg);
        etPriceWithOutTax.setBackgroundResource(R.drawable.catalogproduct_item_bg);
        if (previouslySelectedItem != null)
            previouslySelectedItem.setBackgroundColor(
                    getResources().getColor(R.color.transparent));
        previouslySelectedItem = null;
        if (previouslySelectedProductItem != null)
            previouslySelectedProductItem.setBackgroundColor(
                    getResources().getColor(R.color.transparent));
        previouslySelectedProductItem = null;
        etName.setText("");
        etDisplayName.setText("");
        etBarcode.setText("");
        etSku.setText("");
        etDescription.setText("");
        etPriceWithTax.setText("");
        etStockQuantity.setText("");
        etCostPrice.setText("");
        etInventoryCostPrice.setText("");
        swWithTax.setChecked(false);
        swWithSerialNo.setChecked(false);
        swManageStock.setChecked(false);
        editableProduct = null;
        etProductWeight.setText("");
    }

    private void selectItemDepartments(View v) {
        if (previouslySelectedItem != null)
            previouslySelectedItem.setBackgroundColor(getResources().getColor(R.color.transparent));
        previouslySelectedItem = v;
        v.setBackgroundColor(getResources().getColor(R.color.pressed_color));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {CurrencyReturnsCustomDialogActivity.REQUEST_CURRENCY_RETURN_ACTIVITY_CODE=false;
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public double priceWithTax(Double price){
        Double priceWithTax = price * (1 + (SETTINGS.tax / 100));
        return priceWithTax;
    }

    public double priceWithOutTax(Double price){
        Double priceWithOutTax = price / (1 + (SETTINGS.tax / 100));
        return priceWithOutTax;
    }
}