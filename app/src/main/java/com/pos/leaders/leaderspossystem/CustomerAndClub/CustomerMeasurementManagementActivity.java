package com.pos.leaders.leaderspossystem.CustomerAndClub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerAssetDB;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerMeasurementAdapter.CustomerMeasurementDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerMeasurementAdapter.MeasurementDynamicVariableDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerMeasurementAdapter.MeasurementsDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UserDBAdapter;
import com.pos.leaders.leaderspossystem.Models.*;
import com.pos.leaders.leaderspossystem.Models.CustomerMeasurement.CustomerMeasurement;
import com.pos.leaders.leaderspossystem.Models.CustomerMeasurement.MeasurementDynamicVariable;
import com.pos.leaders.leaderspossystem.Models.CustomerMeasurement.MeasurementsDetails;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.SalesManDetailsGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Win8.1 on 12/20/2017.
 */

public class CustomerMeasurementManagementActivity  extends AppCompatActivity {
    CustomerMeasurementCatalogGridViewAdapter adapter;
    List<MeasurementsDetails> measurementsDetailsList;
    MeasurementsDetailsDBAdapter measurementsDetailsDBAdapter;
    MeasurementDynamicVariableDBAdapter measurementDynamicVariableDBAdapter;
    CustomerMeasurementDBAdapter customerMeasurementDBAdapter;
    GridView gvCustomerMeasurement;
    EditText etSearch ;
    TextView etCustomerName;
    List<MeasurementsDetails> All_MeasurementDetailsList;
    boolean userScrolled=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.customer_measurement_management );

        TitleBar.setTitleBar(this);
        Bundle bundle = getIntent().getExtras();
        long customerId = (long) bundle.get("id");
        CustomerDBAdapter customerDBAdapter=new CustomerDBAdapter(this);
        customerDBAdapter.open();
        com.pos.leaders.leaderspossystem.Models.Customer customer = customerDBAdapter.getCustomerByID(customerId);
        measurementDynamicVariableDBAdapter = new MeasurementDynamicVariableDBAdapter(CustomerMeasurementManagementActivity.this);
        measurementDynamicVariableDBAdapter.open();
        customerMeasurementDBAdapter = new CustomerMeasurementDBAdapter(CustomerMeasurementManagementActivity.this);
        customerMeasurementDBAdapter.open();
        etCustomerName =(TextView) findViewById(R.id.etCustomerName);
        etCustomerName.setText(customer.getFirstName()+" "+customer.getLastName());
        etSearch = (EditText) findViewById(R.id.etSearch);
        gvCustomerMeasurement = (GridView) findViewById(R.id.Management_GVSalesManSalesDetails);
        measurementsDetailsDBAdapter = new MeasurementsDetailsDBAdapter(this);
        measurementsDetailsDBAdapter.open();
        measurementsDetailsList = measurementsDetailsDBAdapter.getAllMeasurementsDetails(customerId);
        measurementsDetailsDBAdapter.close();
        All_MeasurementDetailsList = measurementsDetailsList;
        adapter = new CustomerMeasurementCatalogGridViewAdapter(this, measurementsDetailsList);
        gvCustomerMeasurement.setAdapter(adapter);
        All_MeasurementDetailsList = measurementsDetailsList;
        etSearch.setText("");
        etSearch.setHint("Search..");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        etSearch.setFocusable(true);
        etSearch.requestFocus();
        gvCustomerMeasurement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        gvCustomerMeasurement.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (userScrolled && firstVisibleItem + visibleItemCount == totalItemCount) {

                    userScrolled = false;
                    //  loadMoreProduct();
                }
            }
        });


        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etSearch.setFocusable(true);
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                gvCustomerMeasurement.setTextFilterEnabled(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                measurementsDetailsList = new ArrayList<MeasurementsDetails>();
                String word = etSearch.getText().toString();

                if (!word.equals("")) {
                    for (MeasurementsDetails c : All_MeasurementDetailsList) {
                        MeasurementDynamicVariable measurementDynamicVariable = measurementDynamicVariableDBAdapter.getMeasurementDynamicVariableByID(c.getDynamicVarId());
                        CustomerMeasurement customerMeasurement = customerMeasurementDBAdapter.getCustomerMeasurementByID(c.getMeasurementId());
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        if (measurementDynamicVariable.getType().contains(word)||(measurementDynamicVariable.getName().contains(word))
                                ||(c.getValue()).contains(word)|(format.format(customerMeasurement.getVisitDate())).contains(word.toLowerCase())) {
                            measurementsDetailsList.add(c);

                        }
                    }
                }

                else {
                    measurementsDetailsList = All_MeasurementDetailsList;
                }
                adapter = new CustomerMeasurementCatalogGridViewAdapter(getApplicationContext(), measurementsDetailsList);

                gvCustomerMeasurement.setAdapter(adapter);


            }
        });


    }
}

