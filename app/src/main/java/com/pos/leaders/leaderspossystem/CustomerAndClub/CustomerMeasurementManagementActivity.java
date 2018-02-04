package com.pos.leaders.leaderspossystem.CustomerAndClub;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CityDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClubAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerMeasurementAdapter.CustomerMeasurementDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerMeasurementAdapter.MeasurementDynamicVariableDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerMeasurementAdapter.MeasurementsDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.Models.City;
import com.pos.leaders.leaderspossystem.Models.Club;
import com.pos.leaders.leaderspossystem.Models.CustomerMeasurement.CustomerMeasurement;
import com.pos.leaders.leaderspossystem.Models.CustomerMeasurement.MeasurementDynamicVariable;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Win8.1 on 12/20/2017.
 */

public class CustomerMeasurementManagementActivity  extends AppCompatActivity {
    CustomerMeasurementCatalogGridViewAdapter adapter;
    List<CustomerMeasurement> customerMeasurementList;
    MeasurementsDetailsDBAdapter measurementsDetailsDBAdapter;
    MeasurementDynamicVariableDBAdapter measurementDynamicVariableDBAdapter;
    CustomerMeasurementDBAdapter customerMeasurementDBAdapter;
    ListView gvCustomerMeasurement;
    TextView etCustomerName , customerClubName , customerAddress , customerPhoneNumber , customerEmail , tvNoOfMeasurement;
    List<CustomerMeasurement> All_CustomerMeasurementList;
    boolean userScrolled=true;
    View selectedItem;
    ArrayList<HashMap<String, String>> storeList = new ArrayList<HashMap<String, String>>(); //List to store list of measurement
    private List<Club> groupList = null;
    private List<City> cityList = null;

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
        final com.pos.leaders.leaderspossystem.Models.Customer customer = customerDBAdapter.getCustomerByID(customerId);
        measurementDynamicVariableDBAdapter = new MeasurementDynamicVariableDBAdapter(CustomerMeasurementManagementActivity.this);
        measurementDynamicVariableDBAdapter.open();
        customerMeasurementDBAdapter = new CustomerMeasurementDBAdapter(CustomerMeasurementManagementActivity.this);
        customerMeasurementDBAdapter.open();
        etCustomerName =(TextView) findViewById(R.id.etCustomerName);
        customerClubName =(TextView) findViewById(R.id.etCustomerClubName);
        customerAddress =(TextView) findViewById(R.id.etCustomerAddress);
        customerPhoneNumber =(TextView) findViewById(R.id.etCustomerPhoneNumber);
        customerEmail =(TextView)findViewById(R.id.etCustomerEmail);
        tvNoOfMeasurement =(TextView)findViewById(R.id.noOfCustomerMeasurement);
        etCustomerName.setText(": "+customer.getFirstName()+" "+customer.getLastName());
        CityDbAdapter cityDbAdapter = new CityDbAdapter(CustomerMeasurementManagementActivity.this);
        cityDbAdapter.open();
        cityList = cityDbAdapter.getAllCity();
        for (int i = 0; i < cityList.size(); i++) {
            City city = cityList.get(i);
            if (city.getId() == customer.getCity()) {
                customerAddress.setText(customer.getHouseNumber()+"-"+customer.getStreet()+"-"+city.getName()+"-"+customer.getCountry());
            }        }
        customerPhoneNumber.setText(customer.getPhoneNumber());
        ClubAdapter groupAdapter = new ClubAdapter(CustomerMeasurementManagementActivity.this);
        groupAdapter.open();
        groupList = groupAdapter.getAllGroup();
        for (int i = 0; i < groupList.size(); i++) {
            Club group = groupList.get(i);
            if (group.getId() == customer.getClub()) {
                customerClubName.setText(": "+group.getname());
            }
        }
        customerEmail.setText(customer.getEmail());
      int noOfMeasurement= customerMeasurementDBAdapter.noOfCustomerMeasurement(customer.getId());
        tvNoOfMeasurement.setText(": "+noOfMeasurement);
        gvCustomerMeasurement = (ListView) findViewById(R.id.Management_GVCustomerMeasurementDetails);
        measurementsDetailsDBAdapter = new MeasurementsDetailsDBAdapter(this);
        measurementsDetailsDBAdapter.open();
        customerMeasurementList = customerMeasurementDBAdapter.getCustomerMeasurementByCustomerId(customerId);
        All_CustomerMeasurementList = customerMeasurementList;
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.list_adapter_customer_measurement_header, gvCustomerMeasurement, false);
        gvCustomerMeasurement.addHeaderView(header, null, false);
        adapter = new CustomerMeasurementCatalogGridViewAdapter(this, R.layout.grid_view_item_customer_measurement,customerMeasurementList);
        gvCustomerMeasurement.setAdapter(adapter);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        gvCustomerMeasurement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               removeItemSelection();
                LinearLayout viewElementLayout = (LinearLayout) findViewById(R.id.customer_measurementLayout); //to store element in it
                viewElementLayout.setGravity(Gravity.CENTER);
                viewElementLayout.setBackground(getResources().getDrawable(R.drawable.shadow_box_style));
                viewElementLayout.removeAllViews();
                viewElementLayout.setVisibility(View.VISIBLE);
                view=viewElementLayout;
                selectedItem = view;
                long measurementId = customerMeasurementList.get(position-1).getId();
                storeList =measurementsDetailsDBAdapter.getAllMeasurementDetail(measurementId);
                JSONObject jsonObject = new JSONObject();
                //draw element dynamically using storeList
                try {
                    for (int noOfCustomOpt = 0; noOfCustomOpt < storeList.size(); noOfCustomOpt++) {
                        jsonObject = new JSONObject(storeList.get(noOfCustomOpt));
                        // get information from json
                        Long dynamicVariableId = jsonObject.getLong("dynamicVarId");  //json dynamicVarId
                        MeasurementDynamicVariable measurementDynamicVariable= measurementDynamicVariableDBAdapter.getMeasurementDynamicVariableByID(dynamicVariableId); // getName of dynamicVariableMeasurement
                        String name = measurementDynamicVariable.getName();
                        String elementValue = jsonObject.getString("value");  // json value
                        if(measurementDynamicVariable.getType().equalsIgnoreCase("boolean")){
                            if(elementValue.equals("1")){
                                elementValue="TRUE";
                            }else  if(elementValue.equals("0")){
                                elementValue="FALSE";
                            }
                        }

                        LinearLayout lLayout= new LinearLayout(getApplicationContext());
                        lLayout.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT); // or set height to any fixed value you want
                        lLayout.setLayoutParams(lp);
                        TextView customOptionsName = new TextView(CustomerMeasurementManagementActivity.this);
                        customOptionsName.setTextSize(20);
                        customOptionsName.setTextColor(Color.BLACK);
                        customOptionsName.setGravity(Gravity.CENTER);
                        customOptionsName.setText(name + " : ");
                        lLayout.addView(customOptionsName);
                        TextView customOptionsValue = new TextView(CustomerMeasurementManagementActivity.this);
                        customOptionsValue.setTextSize(20);
                        customOptionsValue.setTextColor(Color.BLACK);
                        customOptionsValue.setGravity(Gravity.CENTER);
                        customOptionsValue.setText(elementValue);
                        lLayout.addView(customOptionsValue);
                        viewElementLayout.addView(lLayout);
                        TextView line = new TextView(CustomerMeasurementManagementActivity.this);
                        line.setTextColor(Color.BLACK);
                        line.setText("----------------------------------------");
                        LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); // or set height to any fixed value you want
                        lLayout.setLayoutParams(lineLp);
                        viewElementLayout.addView(line);
                    }
                }catch (JSONException e){

                }
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
    }
    private void removeItemSelection() {
        gvCustomerMeasurement.setSelection(-1);
        if (selectedItem != null) {
          selectedItem.findViewById(R.id.customer_measurementLayout).setVisibility(View.GONE);
        }
    }
}

