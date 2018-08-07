package com.pos.leaders.leaderspossystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CategoryDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Category;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.ProductStatus;
import com.pos.leaders.leaderspossystem.Tools.ProductCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.ProductCategoryGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

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

    Button btSave,btnCancel;
    EditText etName,etBarcode,etDescription,etPrice,etCostPrice,etDisplayName,etSku,etStockQuantity;
    Switch swWithTax,swWeighable,swManageStock;
	static ListView lvDepartment;
    Map<String,Long> departmentMap=new HashMap<String,Long>();


	String selectedProduct="";
    String selectedDepartment="";
    int with_pos=1;
    int with_point_system=1;

	View previouslySelectedProductItem = null;
    View previouslySelectedItem = null;

	ProductCatalogGridViewAdapter adapter;
	private Product editableProduct;
    long check;
    long depID;
    boolean withTax , withWeighable=false, manageStock = true;
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
        etPrice=(EditText)findViewById(R.id.ETPrice);
        etCostPrice=(EditText)findViewById(R.id.ETCostPrice);

        etDisplayName = (EditText) findViewById(R.id.ETDDisplayName);
        etSku = (EditText) findViewById(R.id.ETSku);
        etStockQuantity = (EditText) findViewById(R.id.ETStockQuantity);
        swManageStock = (Switch) findViewById(R.id.SWManageStock);

        swWithTax=(Switch)findViewById(R.id.SWWithTax);
        swWeighable=(Switch)findViewById(R.id.SWWeighable);

        productDBAdapter = new ProductDBAdapter(this);
        productDBAdapter.open();

        //region Add product button
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addEditProduct())
                setNewProduct();
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
                    withTax=true; //edit here
                }else{
                    withTax=false; //edit here
                }

            }
        });
        swWeighable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                if(isChecked){
                    withWeighable=true; //edit here
                }else{
                    withWeighable=false; //edit here
                }

            }
        });

        swManageStock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                manageStock = b;
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
                    etName.setText(editableProduct.getName());
                    etDisplayName.setText(editableProduct.getDisplayName());
                    etBarcode.setText(editableProduct.getBarCode());
                    etSku.setText(editableProduct.getSku());
                    etDescription.setText(editableProduct.getDescription());
                    etCostPrice.setText(editableProduct.getCostPrice() + "");
                    etPrice.setText(editableProduct.getPrice() + "");
                    etStockQuantity.setText(editableProduct.getStockQuantity() + "");
                    swWithTax.setChecked(editableProduct.isWithTax());
                    swWeighable.setChecked(editableProduct.isWeighable());
                    Category d = departmentDBAdapter.getDepartmentByID(editableProduct.getCategoryId());
                    swManageStock.setChecked(editableProduct.isManageStock());
                    selectedDepartment = d.getName();
                    for (Category dep : listDepartment) {
                        if (dep.getCategoryId() == (editableProduct.getCategoryId())) {
                            dep.setChecked(true);
                        }
                    }
                    departmentGridViewAdapter.updateRecords(listDepartment);

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
                        etPrice.setEnabled(false);
                        etDisplayName.setEnabled(false);
                        etStockQuantity.setEnabled(false);
                        etSku.setEnabled(false);
                        ProductCatalogActivity.Product_Management_View =0;

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private boolean addEditProduct() {
        double price=0 , costPrice=0;
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
            etBarcode.setText(newBarCode);

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
        } else if (etPrice.getText().toString().equals("") ) {
            Toast.makeText(getApplicationContext(), getString(R.string.insert_product_price), Toast.LENGTH_LONG).show();
            etPrice.setBackgroundResource(R.drawable.backtext);
        }

        if (editableProduct == null) {
            boolean availableBarCode= productDBAdapter.isValidSku(etSku.getText().toString());
            boolean availableProductName= productDBAdapter.availableProductName(etName.getText().toString());

            if(availableProductName&&availableBarCode&& !etPrice.getText().toString().equals("")){
                    for (Category d : listDepartment) {
                        if (d.isChecked()) {
                            depID = d.getCategoryId();
                        }
                    }
       
          
                if(!etPrice.getText().toString().equals("")){
                    price=Double.parseDouble(etPrice.getText().toString());
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
                check = productDBAdapter.insertEntry(etName.getText().toString(), etBarcode.getText().toString(),
                        etDescription.getText().toString(), price, costPrice, withTax,
                        withWeighable, depID, SESSION._EMPLOYEE.getEmployeeId(), with_pos, with_point_system,
                        etSku.getText().toString(), ProductStatus.PUBLISHED, etDisplayName.getText().toString(), price, stockQuantity, manageStock, (stockQuantity > 0));
                if (check > 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.success_to_add_product), Toast.LENGTH_LONG).show();
                    return true;
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.fail_to_add_product), Toast.LENGTH_LONG).show();
                }
                }
                else {
                if(!availableProductName) {
                    etName.setBackgroundResource(R.drawable.backtext);
                    Toast.makeText(getApplicationContext(), getString(R.string.product_name_not_available), Toast.LENGTH_LONG).show();
                    return false;
                }
                if(!availableBarCode) {
                    etBarcode.setBackgroundResource(R.drawable.backtext);
                    Toast.makeText(getApplicationContext(), getString(R.string.product_barcode_not_available), Toast.LENGTH_LONG).show();
                    return false;
                }

                }

        } else {
            if (etSku.getText().toString().equals("")) {
                etSku.setText(etBarcode.getText().toString());
            }
            if(!etPrice.getText().toString().equals("")){
                price=Double.parseDouble(etPrice.getText().toString());
            }else {
                price=editableProduct.getPrice();
            }
            if(!etCostPrice.getText().toString().equals("")){
                costPrice=Double.parseDouble(etCostPrice.getText().toString());
            }
            else {
                costPrice=editableProduct.getCostPrice();
            }
            for (Category d : listDepartment) {
            if (etStockQuantity.getText().toString().equals("")) {
                stockQuantity = 0;
            }else{
                stockQuantity = Integer.parseInt(etStockQuantity.getText().toString());
            }
           
                if (d.isChecked()) {
                depID = d.getCategoryId();               }
            }
            //// TODO: 27/10/2016 edit product
            editableProduct.setName(etName.getText().toString());
            editableProduct.setDisplayName(etDisplayName.getText().toString());
            editableProduct.setBarCode(etBarcode.getText().toString());
            editableProduct.setSku(etSku.getText().toString());
            editableProduct.setDescription(etDescription.getText().toString());
            editableProduct.setStockQuantity(stockQuantity);
            editableProduct.setPrice(price);
            editableProduct.setCostPrice(costPrice);
            editableProduct.setWithTax(withTax);
            editableProduct.setWeighable(withWeighable);
            editableProduct.setCategoryId(depID);
            editableProduct.setManageStock(manageStock);
            editableProduct.setInStock(stockQuantity>0);

            try {
                productDBAdapter.updateEntry(editableProduct);
                Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.success_to_update_product), Toast.LENGTH_SHORT);
                onBackPressed();
                return true;
            } catch (Exception ex) {
                Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.error_to_update_product), Toast.LENGTH_SHORT);
            }
            //setNewProduct();
        }
        return false;
    }

	private void setNewProduct() {
        etName.setBackgroundResource(R.drawable.catalogproduct_item_bg);
        etBarcode.setBackgroundResource(R.drawable.catalogproduct_item_bg);
        etPrice.setBackgroundResource(R.drawable.catalogproduct_item_bg);
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
        etPrice.setText("");
        etStockQuantity.setText("");
        etCostPrice.setText("");
        swWithTax.setChecked(false);
        swWeighable.setChecked(false);
        swManageStock.setChecked(false);
        editableProduct = null;
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
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}