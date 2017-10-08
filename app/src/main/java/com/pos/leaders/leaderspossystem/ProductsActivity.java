package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.DepartmentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Department;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Tools.ProductCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KARAM on 18/10/2016.
 */

public class ProductsActivity  extends AppCompatActivity {
    android.support.v7.app.ActionBar actionBar;


    private DepartmentDBAdapter departmentDBAdapter;
    private ProductDBAdapter productDBAdapter;
    ArrayAdapter<String> LAdapter;
    List<Department> listDepartment;
    List<String> departmentsName;

    Button btSave,btnCancel;
    EditText etName,etBarcode,etDescription,etPrice,etCostPrice;
    Switch swWithTax,swWeighable;
	static ListView lvDepatment;
    Map<String,Long> departmentMap=new HashMap<String,Long>();


	String selectedProduct="";
    String selectedDepartment="";
    int with_pos=1;
    int with_point_system=1;

	View previouslySelectedProductItem = null;
    View previouslySelectedItem = null;

	ProductCatalogGridViewAdapter adapter;
	private Product editableProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_products);


        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.title_bar,
                null);

        // Set up your ActionBar
        actionBar = getSupportActionBar();
        // TODO: Remove the redundant calls to getSupportActionBar()
        //       and use variable actionBar instead
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        Calendar ca = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        // You customization
        final int actionBarColor = getResources().getColor(R.color.primaryColor);
        actionBar.setBackgroundDrawable(new ColorDrawable(actionBarColor));

        final TextView actionBarTitle = (TextView) findViewById(R.id.editText8);
        actionBarTitle.setText(format.format(ca.getTime()));
        final TextView actionBarSent = (TextView) findViewById(R.id.editText9);
        actionBarSent.setText("POSID  "+ SESSION.POS_ID_NUMBER);


        final TextView actionBarStaff = (TextView) findViewById(R.id.editText10);
        actionBarStaff.setText(SESSION._USER.getFullName());
        //region Init
        final TextView actionBarLocations = (TextView) findViewById(R.id.editText11);
        actionBarLocations.setText(" "+SESSION._USER.getPermtionName());

        // Get Refferences of Views
        lvDepatment=(ListView)findViewById(R.id.LVDepartment);

        btSave=(Button)findViewById(R.id.productActivity_btnSave);
		btnCancel=(Button)findViewById(R.id.productActivity_btnCancel);
        etName=(EditText)findViewById(R.id.ETName);
        etBarcode=(EditText)findViewById(R.id.ETBarCode);
        etDescription=(EditText)findViewById(R.id.ETDescription);
        etPrice=(EditText)findViewById(R.id.ETPrice);
        etCostPrice=(EditText)findViewById(R.id.ETCostPrice);
        swWithTax=(Switch)findViewById(R.id.SWWithTax);
        swWeighable=(Switch)findViewById(R.id.SWWeighable);


        productDBAdapter=new ProductDBAdapter(this);
        productDBAdapter.open();




        //region Add product button

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/**
                WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

            if (wifi.isWifiEnabled()) {
                    final String send_name = etName.getText().toString();
                    final String send_Barcode = etBarcode.getText().toString();
                    final String send_Description = etDescription.getText().toString();
                    final String send_Price = etPrice.getText().toString();
                    final String send_CostPrice = etCostPrice.getText().toString();
                    final String send_swWithTax = swWithTax.getText().toString();
                    final String send_swWeighable = swWeighable.getText().toString();



                    class Async extends AsyncTask<Void, Void, String> {

                        ProgressDialog progressDialog;

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "up date", Toast.LENGTH_LONG).show();
                            //  Toast.makeText(TestPHPandAndroid.this,s,Toast.LENGTH_LONG).show();


                        }

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();

                            progressDialog = ProgressDialog.show(ProductsActivity.this, "", "please waiiting", false, false);

                        }

                        @Override
                        protected String doInBackground(Void... voids) {

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put(Config.PRODUCTS_NAME, send_name);
                            hashMap.put(Config.PRODUCTS_BARCODE, send_Barcode);
                            hashMap.put(Config.PRODUCTS_COSTPRICE, send_CostPrice);
                            hashMap.put(Config.PRODUCTS_WEIGHABLE, send_swWeighable);
                            hashMap.put(Config.PRODUCTS_WITHTAX, send_swWithTax);
                            hashMap.put(Config.PRODUCTS_DESCRIPTION, send_Description);
                            hashMap.put(Config.PRODUCTS_PRICE, send_Price);
                            hashMap.put(Config.PRODUCTS_status, send_status);



                            RequestHander requestHander = new RequestHander();
                            String post = requestHander.sendPostRequest(Config.insert_Product_url, hashMap);

                            return post;
                        }
                    }
                    Async async = new Async();
                    async.execute();


//wifi is enabled}**/

///to save on sqllite
                if (etName.getText().toString().equals("")) {
                    return;
                } else if (etBarcode.getText().toString().equals("")) {
                    return;
                } else if (etPrice.getText().toString().equals("") || etCostPrice.getText().toString().equals("")) {
                    return;
                }


                if (editableProduct == null) {
                    if (selectedDepartment.equals("")) {
                        Toast.makeText(getApplicationContext(), "please select an department", Toast.LENGTH_LONG).show();
                    } else {
                        int check = productDBAdapter.insertEntry(etName.getText().toString(), etBarcode.getText().toString(),
                                etDescription.getText().toString(), Double.parseDouble(etPrice.getText().toString()),
                                Double.parseDouble(etCostPrice.getText().toString()), swWithTax.isChecked(),
                                swWeighable.isChecked(), departmentMap.get(selectedDepartment), SESSION._USER.getId(),with_pos,with_point_system);
                        if (check == 1) {
                            Toast.makeText(getApplicationContext(), "success to add product", Toast.LENGTH_LONG).show();


                        } else {
                            Toast.makeText(getApplicationContext(), "fail to add product", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    //// TODO: 27/10/2016 edit product
                    editableProduct.setName(etName.getText().toString());
                    editableProduct.setBarCode(etBarcode.getText().toString());
                    editableProduct.setDescription(etDescription.getText().toString());
                    editableProduct.setPrice(Double.parseDouble(etPrice.getText().toString()));
                    editableProduct.setCostPrice(Double.parseDouble(etCostPrice.getText().toString()));
                    editableProduct.setWithTax(swWithTax.isChecked());
                    editableProduct.setWeighable(swWeighable.isChecked());
                    editableProduct.setDepartmentId(departmentMap.get(selectedDepartment));
                    try {
                        productDBAdapter.updateEntry(editableProduct);
                        Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.success_to_update_product), Toast.LENGTH_SHORT);
                        onBackPressed();
                    } catch (Exception ex) {
                        Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.error_to_update_product), Toast.LENGTH_SHORT);
                    }


                    //gvProduct.setAdapter(adapter);
                    //setNewProduct();


                }

       /**         else{

                    send_status="0";
                if (etName.getText().toString().equals("")) {
                    return;
                } else if (etBarcode.getText().toString().equals("")) {
                    return;
                } else if (etPrice.getText().toString().equals("") || etCostPrice.getText().toString().equals("")) {
                    return;
                }


                if (editableProduct == null) {
                    if (selectedDepartment.equals("")) {
                        Toast.makeText(getApplicationContext(), "please select an department", Toast.LENGTH_LONG).show();
                    } else {
                        int check = productDBAdapter.insertEntry(etName.getText().toString(), etBarcode.getText().toString(),
                                etDescription.getText().toString(), Double.parseDouble(etPrice.getText().toString()),
                                Double.parseDouble(etCostPrice.getText().toString()), swWithTax.isChecked(),
                                swWeighable.isChecked(), departmentMap.get(selectedDepartment), SESSION._USER.getId());
                        if (check == 1) {
                            Toast.makeText(getApplicationContext(), "success to add product", Toast.LENGTH_LONG).show();


                        } else {
                            Toast.makeText(getApplicationContext(), "fail to add product", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    //// TODO: 27/10/2016 edit product
                    editableProduct.setName(etName.getText().toString());
                    editableProduct.setBarCode(etBarcode.getText().toString());
                    editableProduct.setDescription(etDescription.getText().toString());
                    editableProduct.setPrice(Double.parseDouble(etPrice.getText().toString()));
                    editableProduct.setCostPrice(Double.parseDouble(etCostPrice.getText().toString()));
                    editableProduct.setWithTax(swWithTax.isChecked());
                    editableProduct.setWeighable(swWeighable.isChecked());
                    editableProduct.setDepartmentId(departmentMap.get(selectedDepartment));
                    try {
                        productDBAdapter.updateEntry(editableProduct);
                        Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.success_to_update_product), Toast.LENGTH_SHORT);
                        onBackPressed();
                    } catch (Exception ex) {
                        Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.error_to_update_product), Toast.LENGTH_SHORT);
                    }


                    //gvProduct.setAdapter(adapter);
                    //setNewProduct();
             hashMap= productDBAdapter.getProductByStatus(send_status);






                }**/



                }







            } );

        //endregion

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    departmentDBAdapter.close();
                    productDBAdapter.close();}
                catch (Exception ex){}
                onBackPressed();
            }
        });


/*
		gvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.i("item clicked",productsList.get(position).toString());

				view.setSelected(true);

				if (previouslySelectedProductItem != null)
				{
					previouslySelectedProductItem.setBackgroundColor(
							getResources().getColor(R.color.transparent));
				}
				view.setBackgroundColor(getResources().getColor(R.color.pressed_color));
				selectedProduct =gvProduct.getItemAtPosition(position).toString();
				previouslySelectedProductItem = view;
				editableProduct=productsList.get(position);


				etName.setText(editableProduct.getName());
				etBarcode.setText(editableProduct.getBarCode());
				etDescription.setText(editableProduct.getDescription());
				etCostPrice.setText(editableProduct.getCostPrice()+"");
				etPrice.setText(editableProduct.getPrice()+"");
				swWithTax.setChecked(editableProduct.isWithTax());
				swWeighable.setChecked(editableProduct.isWeighable());
				Department d=departmentDBAdapter.getDepartmentByID(editableProduct.getDepartmentId());
				selectedDepartment=d.getName();
				for (int i=0;i<lvDepatment.getChildCount();i++){
					if(listDepartment.get(i).getName().equals(d.getName())){
						selectItemDepartments(lvDepatment.getChildAt(i));
					}
				}
				btAddProduct.setText(getResources().getText(R.string.edit_product));
			}
		});
		*/

/*		btNew.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setNewProduct();
			}
		});

		btDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});*/

        // Department list
        departmentDBAdapter = new DepartmentDBAdapter(this);
        departmentDBAdapter.open();


        departmentsName=new ArrayList<String>();
        listDepartment = departmentDBAdapter.getAllDepartments();
        //departmentDBAdapter.close();

        for (Department d : listDepartment) {
            departmentsName.add(d.getName());
            departmentMap.put(d.getName(),d.getId());
        }

        LAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,departmentsName);
        lvDepatment.setAdapter(LAdapter);
        lvDepatment.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        lvDepatment.setItemChecked(1,true);


        lvDepatment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                view.setSelected(true);
				selectItemDepartments(view);

                selectedDepartment=lvDepatment.getItemAtPosition(position).toString();


            }
        });


        Bundle extras = getIntent().getExtras();

        if(extras!=null) {
            try {
                etBarcode.setText(extras.getString("barcode"));
            } catch (Exception ex) {

            }
            try {
                editableProduct = productDBAdapter.getProductByID(extras.getInt("productID"));
                etName.setText(editableProduct.getName());
                etBarcode.setText(editableProduct.getBarCode());
                etDescription.setText(editableProduct.getDescription());
                etCostPrice.setText(editableProduct.getCostPrice()+"");
                etPrice.setText(editableProduct.getPrice()+"");
                swWithTax.setChecked(editableProduct.isWithTax());
                swWeighable.setChecked(editableProduct.isWeighable());
                Department d=departmentDBAdapter.getDepartmentByID(editableProduct.getDepartmentId());
                selectedDepartment=d.getName();

                for (int i=0;i<lvDepatment.getChildCount();i++){
                    if(listDepartment.get(i).getName().equals(d.getName())){
                        selectItemDepartments(lvDepatment.getChildAt(i));
                    }
                }

            } catch (Exception ex) {

            }
        }


    }


	private void setNewProduct(){
		if(previouslySelectedItem!=null)
			previouslySelectedItem.setBackgroundColor(
					getResources().getColor(R.color.transparent));
		previouslySelectedItem=null;
		if(previouslySelectedProductItem!=null)
			previouslySelectedProductItem.setBackgroundColor(
					getResources().getColor(R.color.transparent));
		previouslySelectedProductItem=null;
		etName.setText("");
		etBarcode.setText("");
		etDescription.setText("");
		etPrice.setText("");
		etCostPrice.setText("");
		swWithTax.setChecked(false);
		swWeighable.setChecked(false);

		editableProduct=null;
	}

	private void selectItemDepartments(View v){
		if(previouslySelectedItem!=null)
			previouslySelectedItem.setBackgroundColor(getResources().getColor(R.color.transparent));
		previouslySelectedItem=v;
		v.setBackgroundColor(getResources().getColor(R.color.pressed_color));


	}








}
