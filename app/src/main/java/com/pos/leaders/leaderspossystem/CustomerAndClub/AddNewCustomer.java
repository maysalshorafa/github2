package com.pos.leaders.leaderspossystem.CustomerAndClub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CityDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClubAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.Models.City;
import com.pos.leaders.leaderspossystem.Models.Club;
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.util.ArrayList;
import java.util.List;

public class AddNewCustomer extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String street="" , job="" , email="" , houseNo="" , postalCode="" , country="" , countryCode="";
    int cityId=0;
    EditText etCustomerFirstName, etCustomerLastName, etStreet, etJob, etEmail, etPhoneNo, etHouseNumber, etPostalCode, etCountry, etCountryCode;
    Button btAddCustomer, btCancel;
    Spinner selectCitySpinner, selectClubSpinner;
    CustomerDBAdapter customerDBAdapter;
    Customer customer;
    LinearLayout secondCustomerInformation ;
    private List<City> cityList = null;
    private List<Club> groupList = null;
    ArrayList<Integer> permissions_name;
    RadioButton maleRadioButton, femaleRadioButton;
    RadioGroup radioGender;
    String gender = "";
    final List<String> club = new ArrayList<String>();
    long clubID=0;
    long customerId;
    ImageView advanceFeature;
    TextView advance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_new_coustmer);
        TitleBar.setTitleBar(this);
        init();
        customer = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            permissions_name = getIntent().getIntegerArrayListExtra("permissions_name");
        }
        if(CustmerManagementActivity.Customer_Management_Edit==10){
            btAddCustomer.setText(R.string.edit);
            CustmerManagementActivity.Customer_Management_Edit=0;
        }
        if(CustmerManagementActivity.Customer_Management_View==9){
            btAddCustomer.setVisibility(View.GONE);
            etCustomerFirstName.setEnabled(false);
            etCustomerLastName.setEnabled(false);
            etCountry.setEnabled(false);
            etStreet.setEnabled(false);
            etJob.setEnabled(false);
            etEmail.setEnabled(false);
            etPhoneNo.setEnabled(false);
            etHouseNumber.setEnabled(false);
            etPostalCode.setEnabled(false);
            etCountryCode.setEnabled(false);
            CustmerManagementActivity.Customer_Management_View=0;
        }
        if (bundle != null) {
             customerId = (long) bundle.get("id");
            customer = customerDBAdapter.getCustomerByID(customerId);
            etCustomerFirstName.setText(customer.getFirstName());
            etCustomerLastName.setText(customer.getLastName());
           etJob.setText(customer.getJob());
            etEmail.setText(customer.getEmail());
            etPhoneNo.setText(customer.getPhoneNumber());
            etStreet.setText(customer.getStreet());
            etHouseNumber.setText(customer.getHouseNumber());
            etPostalCode.setText(customer.getPostalCode());
            etCountry.setText(customer.getCountry());
            etCountryCode.setText(customer.getCountryCode());
            btAddCustomer.setText(getResources().getText(R.string.edit));
            if (customer.getGender().equalsIgnoreCase( getString(R.string.male)) ){
                radioGender.check(R.id.male);
            } else if(customer.getGender().equalsIgnoreCase( getString(R.string.female)) ){
                radioGender.check(R.id.female);
            }
             for (int i = 0; i < groupList.size(); i++) {
             Club group = groupList.get(i);
             if (group.getClubId() == customer.getClub()) {
             selectClubSpinner.setSelection(i);

             }
             }
            for (int i = 0; i < cityList.size(); i++) {
                City city = cityList.get(i);
                if (city.getCityId() == customer.getCity()) {
                    selectCitySpinner.setSelection(i);

                }
            }

        }

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewCustomer.this, com.pos.leaders.leaderspossystem.CustomerAndClub.Customer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);             }
        });
        advanceFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advanceFeature.setVisibility(View.INVISIBLE);
                advance.setVisibility(View.INVISIBLE);
              secondCustomerInformation.setVisibility(View.VISIBLE);
                  }
        });

        btAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _customerName = etCustomerFirstName.getText().toString();
                Intent intent;
                if (customer == null) {
                    if (!_customerName.equals("")) {
                        if(secondCustomerInformation.getVisibility()== View.VISIBLE){
                        street=etStreet.getText().toString();
                        job=etJob.getText().toString();
                        email=etEmail.getText().toString();
                        houseNo=etHouseNumber.getText().toString();
                        postalCode=etPostalCode.getText().toString();
                        country=etCountry.getText().toString();
                        countryCode=etCountryCode.getText().toString();
                            cityId=  (int) selectCitySpinner.getSelectedItemId();

                        }
                        for (int i = 0; i < groupList.size(); i++) {
                            Club group = groupList.get(i);
                            if (group.getName().equalsIgnoreCase( selectClubSpinner.getSelectedItem().toString())) {
                                clubID = group.getClubId();
                            }


                        }
                            if (etCustomerFirstName.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), getString(R.string.please_insert_first_name), Toast.LENGTH_LONG).show();
                            } else if (etCustomerLastName.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), getString(R.string.please_insert_last_name), Toast.LENGTH_LONG).show();
                            } else if (etPhoneNo.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), getString(R.string.please_insert_phone_no), Toast.LENGTH_LONG).show();
                            } else {
                                long i = customerDBAdapter.insertEntry(etCustomerFirstName.getText().toString(),
                                        etCustomerLastName.getText().toString(), gender, email, job, etPhoneNo.getText().toString(), street, cityId, clubID, houseNo, etPostalCode.getText().toString(),
                                       country, countryCode,0);
                                if (i > 0) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.success_adding_new_customer), Toast.LENGTH_LONG).show();
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    onBackPressed();
                                } else {
                                    Toast.makeText(getApplicationContext(), getString(R.string.can_not_add_customer_please_try_again), Toast.LENGTH_SHORT).show();
                                }
                            }
                    }
                } else {
                    // Edit mode
                    if (_customerName != "") {
                        if(secondCustomerInformation.getVisibility()== View.VISIBLE){
                            street=etStreet.getText().toString();
                            job=etJob.getText().toString();
                            email=etEmail.getText().toString();
                            houseNo=etHouseNumber.getText().toString();
                            postalCode=etPostalCode.getText().toString();
                            country=etCountry.getText().toString();
                            countryCode=etCountryCode.getText().toString();
                            for (int i = 0; i < cityList.size(); i++) {
                                City city = cityList.get(i);
                                if (city.getName().equalsIgnoreCase(selectCitySpinner.getSelectedItem().toString())) {
                                    cityId = (int) city.getCityId();
                                }
                            }
                        }
                        for (int i = 0; i < groupList.size(); i++) {
                            Club group = groupList.get(i);
                            if (group.getName() .equalsIgnoreCase(selectClubSpinner.getSelectedItem().toString())) {
                                clubID= group.getClubId();

                            }
                        }
                        if (etCustomerFirstName.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), getString(R.string.please_insert_first_name), Toast.LENGTH_LONG).show();
                        } else if (etCustomerLastName.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), getString(R.string.please_insert_last_name), Toast.LENGTH_LONG).show();
                        } else if (etPhoneNo.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), getString(R.string.please_insert_phone_no), Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                customer.setFirstName(etCustomerFirstName.getText().toString());
                                customer.setLastName(etCustomerLastName.getText().toString());
                                customer.setJob(job);
                                customer.setEmail(email);
                                customer.setPhoneNumber(etPhoneNo.getText().toString());
                                customer.setStreet(street);
                                customer.setHouseNumber(houseNo);
                                customer.setPostalCode(postalCode);
                                customer.setCountry(country);
                                customer.setCountryCode(countryCode);
                                customer.setGender(gender);
                                customer.setClub(clubID);
                                customer.setCity(cityId);
                                customerDBAdapter.updateEntry(customer);
                                customerDBAdapter.updateEntry(customer);
                                Toast.makeText(getApplicationContext(), getString(R.string.success_edit_customer), Toast.LENGTH_SHORT).show();

                                Log.i("success Edit", customer.toString());
                                onBackPressed();
                            } catch (Exception ex) {
                                Log.e("error can`t edit customer", ex.getMessage().toString());
                                Toast.makeText(getApplicationContext(), getString(R.string.can_not_edit_customer_please_try_again), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
            }
        });

    }

    private void init() {

        CityDbAdapter cityDbAdapter = new CityDbAdapter(AddNewCustomer.this);
        cityDbAdapter.open();
        ClubAdapter groupAdapter = new ClubAdapter(AddNewCustomer.this);
        groupAdapter.open();
        secondCustomerInformation=(LinearLayout)findViewById(R.id.secondCustomerInformation);
        etCustomerFirstName = (EditText) findViewById(R.id.etCustomerFirstName);
        etCustomerLastName = (EditText) findViewById(R.id.etCustomerLastName);
        etStreet = (EditText) findViewById(R.id.etCustomerStreet);
        radioGender = (RadioGroup) findViewById(R.id.customerGender);
        maleRadioButton = (RadioButton) findViewById(R.id.male);
        femaleRadioButton = (RadioButton) findViewById(R.id.female);
        etJob = (EditText) findViewById(R.id.etCustomerJob);
        etEmail = (EditText) findViewById(R.id.etCustomerEmail);
        etPhoneNo = (EditText) findViewById(R.id.etCustomerPhoneNumber);
        etCountry = (EditText) findViewById(R.id.etCustomerCountry);
        etCountryCode = (EditText) findViewById(R.id.etCustomerCountryCode);
       etHouseNumber = (EditText) findViewById(R.id.etHouseNumber);
        etPostalCode = (EditText) findViewById(R.id.etCustomerPostalCode);
        btAddCustomer = (Button) findViewById(R.id.add_Custmer);
        btCancel = (Button) findViewById(R.id.addCustmer_BTCancel);
        advanceFeature=(ImageView)findViewById(R.id.advanceFeature);
        advance=(TextView)findViewById(R.id.advance);
        customerDBAdapter = new CustomerDBAdapter(this);
        customerDBAdapter.open();
        selectCitySpinner = (Spinner) findViewById(R.id.customerCitySpinner);
        selectClubSpinner = (Spinner) findViewById(R.id.customerClubSpinner);
        selectCitySpinner.setOnItemSelectedListener(this);
        selectClubSpinner.setOnItemSelectedListener(this);
        final List<String> city = new ArrayList<String>();
        cityList = cityDbAdapter.getAllCity();
        for (int i = 0; i < cityList.size(); i++) {
            city.add(cityList.get(i).getName());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, city);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        selectCitySpinner.setAdapter(dataAdapter);


        groupList = groupAdapter.getAllGroup();
        for (int i = 0; i < groupList.size(); i++) {
            club.add(groupList.get(i).getName());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, club);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter to spinner
        selectClubSpinner.setAdapter(dataAdapter1);
        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.male:
                        gender=getString(R.string.male);
                        break;
                    case R.id.female:
                        gender=getString(R.string.female);
                        break;

                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
