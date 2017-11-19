package com.pos.leaders.leaderspossystem;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.CustomerAndClub.CustmerManagementActivity;
import com.pos.leaders.leaderspossystem.CustomerAndClub.Customer;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.DepartmentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Department;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Tools.ProductCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KARAM on 18/10/2016.
 */

public class ProductsActivity  extends AppCompatActivity {
    private DepartmentDBAdapter departmentDBAdapter;
    private ProductDBAdapter productDBAdapter;
    ArrayAdapter<String> LAdapter;
    List<Department> listDepartment;
    List<String> departmentsName;

    Button btSave,btnCancel,btnContinue;
    EditText etName,etBarcode,etDescription,etPrice,etCostPrice;
    Switch swWithTax,swWeighable;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_products);

        TitleBar.setTitleBar(this);

        // Get Refferences of Views
        lvDepartment =(ListView)findViewById(R.id.LVDepartment);

        btSave=(Button)findViewById(R.id.productActivity_btnSave);
		btnCancel=(Button)findViewById(R.id.productActivity_btnCancel);
        etName=(EditText)findViewById(R.id.ETName);
        etBarcode=(EditText)findViewById(R.id.ETBarCode);
        etDescription=(EditText)findViewById(R.id.ETDescription);
        etPrice=(EditText)findViewById(R.id.ETPrice);
        etCostPrice=(EditText)findViewById(R.id.ETCostPrice);
        swWithTax=(Switch)findViewById(R.id.SWWithTax);
        swWeighable=(Switch)findViewById(R.id.SWWeighable);
        btnContinue=(Button)findViewById(R.id.productActivity_btnContinue);

        productDBAdapter=new ProductDBAdapter(this);
        productDBAdapter.open();

        //region Add product button
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEditProduct();
                onBackPressed();
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

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addEditProduct())
                    setNewProduct();

            }
        });

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
        lvDepartment.setAdapter(LAdapter);
        lvDepartment.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        lvDepartment.setItemChecked(1,true);


        lvDepartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                view.setSelected(true);
				selectItemDepartments(view);

                selectedDepartment= lvDepartment.getItemAtPosition(position).toString();


            }
        });


        Bundle extras = getIntent().getExtras();

        if(extras!=null) {
            try {
                etBarcode.setText(extras.getString("barcode"));
            } catch (Exception ex) {

            }
            try {
                editableProduct = productDBAdapter.getProductByID(extras.getLong("productID"));
                etName.setText(editableProduct.getName());
                etBarcode.setText(editableProduct.getBarCode());
                etDescription.setText(editableProduct.getDescription());
                etCostPrice.setText(editableProduct.getCostPrice()+"");
                etPrice.setText(editableProduct.getPrice()+"");
                swWithTax.setChecked(editableProduct.isWithTax());
                swWeighable.setChecked(editableProduct.isWeighable());
                Department d=departmentDBAdapter.getDepartmentByID(editableProduct.getDepartmentId());
                btnContinue.setVisibility(View.GONE);
                selectedDepartment=d.getName();

                for (int i = 0; i< lvDepartment.getChildCount(); i++){
                    if(listDepartment.get(i).getName().equals(d.getName())){
                        selectItemDepartments(lvDepartment.getChildAt(i));
                    }
                }

            } catch (Exception ex) {

            }
        }
    }

    private boolean addEditProduct() {
        ///to save on sqlite
        if (etName.getText().toString().equals("")) {
            return false;
        } else if (etBarcode.getText().toString().equals("")) {
            return false;
        } else if (etPrice.getText().toString().equals("") || etCostPrice.getText().toString().equals("")) {
            return false;
        }

        if (editableProduct == null) {
            if (selectedDepartment.equals("")) {
                Toast.makeText(getApplicationContext(), getString(R.string.department_not_selected), Toast.LENGTH_LONG).show();
            } else {
                check = productDBAdapter.insertEntry(etName.getText().toString(), etBarcode.getText().toString(),
                        etDescription.getText().toString(), Double.parseDouble(etPrice.getText().toString()),
                        Double.parseDouble(etCostPrice.getText().toString()), swWithTax.isChecked(),
                        swWeighable.isChecked(), departmentMap.get(selectedDepartment), SESSION._USER.getId(), with_pos, with_point_system);
                if (check > 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.success_to_add_product), Toast.LENGTH_LONG).show();
                    return true;
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.fail_to_add_product), Toast.LENGTH_LONG).show();
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
                return true;
            } catch (Exception ex) {
                Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.error_to_update_product), Toast.LENGTH_SHORT);
            }
            //setNewProduct();
        }
        return false;
    }

	private void setNewProduct() {
        if (previouslySelectedItem != null)
            previouslySelectedItem.setBackgroundColor(
                    getResources().getColor(R.color.transparent));
        previouslySelectedItem = null;
        if (previouslySelectedProductItem != null)
            previouslySelectedProductItem.setBackgroundColor(
                    getResources().getColor(R.color.transparent));
        previouslySelectedProductItem = null;
        etName.setText("");
        etBarcode.setText("");
        etDescription.setText("");
        etPrice.setText("");
        etCostPrice.setText("");
        swWithTax.setChecked(false);
        swWeighable.setChecked(false);
        btnContinue.setVisibility(View.VISIBLE);
        editableProduct = null;
    }

	private void selectItemDepartments(View v) {
        if (previouslySelectedItem != null)
            previouslySelectedItem.setBackgroundColor(getResources().getColor(R.color.transparent));
        previouslySelectedItem = v;
        v.setBackgroundColor(getResources().getColor(R.color.pressed_color));
    }
}