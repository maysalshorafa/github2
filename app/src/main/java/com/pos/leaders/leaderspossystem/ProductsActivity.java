package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.DepartmentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Department;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Tools.ProductCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KARAM on 18/10/2016.
 */

public class ProductsActivity extends Activity {

    private DepartmentDBAdapter departmentDBAdapter;
    private ProductDBAdapter productDBAdapter;
    ArrayAdapter<String> LAdapter;
    List<Department> listDepartment;
    List<String> departmentsName;

    Button btSave,btnCancel;
    EditText etName,etBarcode,etDescription,etPrice,etCostPrice;
    Switch swWithTax,swWeighable;
	static ListView lvDepatment;
    Map<String,Integer> departmentMap=new HashMap<String,Integer>();


	String selectedProduct="";
    String selectedDepartment="";

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

        btSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (etName.getText().toString().equals("")) {
                    return;
                } else if(etBarcode.getText().toString().equals("")) {
                    return;
                } else if(etPrice.getText().toString().equals("")||etCostPrice.getText().toString().equals("")){
                    return;
                }





				if(editableProduct==null) {
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
				}
				else{
					//// TODO: 27/10/2016 edit product
					editableProduct.setName(etName.getText().toString());
					editableProduct.setBarCode(etBarcode.getText().toString());
					editableProduct.setDescription(etDescription.getText().toString());
					editableProduct.setPrice(Double.parseDouble(etPrice.getText().toString()));
					editableProduct.setCostPrice(Double.parseDouble(etCostPrice.getText().toString()));
					editableProduct.setWithTax(swWithTax.isChecked());
					editableProduct.setWeighable(swWeighable.isChecked());
					editableProduct.setDepartmentId(departmentMap.get(selectedDepartment));
                    try{
                        productDBAdapter.updateEntry(editableProduct);
                        Toast.makeText(getBaseContext(),getBaseContext().getString(R.string.success_to_update_product),Toast.LENGTH_SHORT);
                        onBackPressed();
                    }
                    catch (Exception ex){
                        Toast.makeText(getBaseContext(),getBaseContext().getString(R.string.error_to_update_product),Toast.LENGTH_SHORT);
                    }


					//gvProduct.setAdapter(adapter);
					//setNewProduct();


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
