package com.pos.leaders.leaderspossystem.CustomerAndClub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CityDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.GroupAdapter;
import com.pos.leaders.leaderspossystem.Models.City;
import com.pos.leaders.leaderspossystem.Models.Customer_M;
import com.pos.leaders.leaderspossystem.Models.Group;

import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;


import java.util.ArrayList;
import java.util.List;

public class AddNewCoustmer  extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Intent intent ;
    EditText etCustomerFirstName, etCustomerLastName, etStreet, etJob, etEmail, etPhoneNo,etHouseNumber,etPostalCode,etCountry,etCountryCode;
    Button btAddCustomer,btCancel;
    Spinner selectCitySpinner, selectClubSpinner;
    CustomerDBAdapter customerDBAdapter;
    Customer_M customer;
    private List<City> cityList=null;
    private List<Group> groupList=null;
    ArrayList<Integer> permissions_name;
    RadioButton maleRadioButton, femaleRadioButton;
    RadioGroup radioGender ;
    String gender=null;
    final List<String> club = new ArrayList<String>();
    long clubID;
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
        if (bundle != null) {
           long i = (long) bundle.get("id");
            customer = customerDBAdapter.getCustmerByID(i);
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
            //The key argument here must match that used in the other activity
        }

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        });

        btAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _custmerName = etCustomerFirstName.getText().toString();
                Intent intent;
                if (customer == null) {
                    if (_custmerName != "") {
                        if (customerDBAdapter.availableCustomerrName(_custmerName)) {
                            if (etCustomerFirstName.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert First Name", Toast.LENGTH_LONG).show();
                            } else if (etCustomerLastName.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert Last Name", Toast.LENGTH_LONG).show();
                            }
                            else if (etEmail.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert email", Toast.LENGTH_LONG).show();
                            }
                            else if (gender.equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert gender", Toast.LENGTH_LONG).show();
                            }
                            else if (etJob.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert Job", Toast.LENGTH_LONG).show();
                            }
                            else if (etPhoneNo.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert phoneno", Toast.LENGTH_LONG).show();
                            }else if (etStreet.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert Street", Toast.LENGTH_LONG).show();
                            }
                            else if (selectClubSpinner.getSelectedItem().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please Selesct Club", Toast.LENGTH_LONG).show();
                            }else if (selectCitySpinner.getSelectedItem().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please Select City", Toast.LENGTH_LONG).show();
                            }
                            else if (etHouseNumber.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert house Number", Toast.LENGTH_LONG).show();
                            } else if (etPostalCode.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert Postal Code", Toast.LENGTH_LONG).show();
                            } else if (etCountry.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert Country", Toast.LENGTH_LONG).show();
                            } else if (etCountryCode.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert Country Code", Toast.LENGTH_LONG).show();
                            }

                            else {
                                for (int i = 0; i < groupList.size(); i++) {
                                    Group group =groupList.get(i);
                                    if(group.getname()==selectClubSpinner.getSelectedItem().toString()){
                                        clubID=group.getId();
                                    }


                                }
                                    long i = customerDBAdapter.insertEntry(etCustomerFirstName.getText().toString(),
                                            etCustomerLastName.getText().toString(), gender,  etEmail.getText().toString(),
                                            etJob.getText().toString(),etPhoneNo.getText().toString(),etStreet.getText().toString() ,
                                            (int) selectCitySpinner.getSelectedItemId(),clubID,
                                            etHouseNumber.getText().toString(),etPostalCode.getText().toString(),
                                            etCountry.getText().toString(),etCountryCode.getText().toString());
                                if (i > 0) {
                                    Log.i("success", "adding new customer");
                                    intent = new Intent(AddNewCoustmer.this, CustmerManagementActivity.class);
                                    startActivity(intent);
                                    //// TODO: 17/10/2016 sucess to add entity
                                } else {
                                    Log.e("error", "can`t add customer");
                                    Toast.makeText(getApplicationContext(), "Can`t add customer please try again", Toast.LENGTH_LONG).show();
                                    //// TODO: 17/10/2016 error with adding entity
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Customer name is not available, try to use another Custmer name", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    // Edit mode
                    if (_custmerName != "") {
                        if ((customerDBAdapter.availableCustomerrName(_custmerName)) || _custmerName == customer.getCustmerName()) {
                            if (etCustomerFirstName.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert First Name", Toast.LENGTH_LONG).show();
                            } else if (etCustomerLastName.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert Last Name", Toast.LENGTH_LONG).show();
                            }
                            else if (etEmail.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert email", Toast.LENGTH_LONG).show();
                            }
                            else if (gender.equals("")) {
                                gender="";
                                Toast.makeText(getApplicationContext(), "Please insert gender", Toast.LENGTH_LONG).show();
                            }
                            else if (etJob.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert Job", Toast.LENGTH_LONG).show();
                            }
                            else if (etPhoneNo.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert phoneno", Toast.LENGTH_LONG).show();
                            }else if (etStreet.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert Street", Toast.LENGTH_LONG).show();
                            }
                            else if (selectClubSpinner.getSelectedItem().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please Selesct Club", Toast.LENGTH_LONG).show();
                            }else if (selectCitySpinner.getSelectedItem().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please Select City", Toast.LENGTH_LONG).show();
                            }
                            else if (etHouseNumber.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert house Number", Toast.LENGTH_LONG).show();
                            } else if (etPostalCode.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert Postal Code", Toast.LENGTH_LONG).show();
                            } else if (etCountry.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert Country", Toast.LENGTH_LONG).show();
                            } else if (etCountryCode.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Please insert Country Code", Toast.LENGTH_LONG).show();
                            }
                            else {
                                try {
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
                                    customerDBAdapter.updateEntry(customer);
                                    Log.i("success Edit", customer.toString());
                                    intent = new Intent(AddNewCoustmer.this, CustmerManagementActivity.class);
                                    startActivity(intent);
                                } catch (Exception ex) {
                                    Log.e("error can`t edit customer", ex.getMessage().toString());
                                    Toast.makeText(getApplicationContext(), "Can`t edit customer please try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Custmer name is not available, try to use another customer name", Toast.LENGTH_LONG).show();
                        }
                    }
                }}
        });

    }

    private void init() {
        CityDbAdapter cityDbAdapter = new CityDbAdapter(AddNewCoustmer.this);
        cityDbAdapter.open();
        GroupAdapter groupAdapter = new GroupAdapter(AddNewCoustmer.this);
        groupAdapter.open();

        etCustomerFirstName = (EditText) findViewById(R.id.etCustomerFirstName);
        etCustomerLastName = (EditText) findViewById(R.id.etCustomerLastName);
        etStreet = (EditText) findViewById(R.id.etCustomerStreet);
        radioGender = (RadioGroup) findViewById(R.id.customerGender);
        maleRadioButton=(RadioButton)findViewById(R.id.male);
        femaleRadioButton=(RadioButton)findViewById(R.id.female);
        etJob = (EditText) findViewById(R.id.etCustomerJob);
        etEmail = (EditText) findViewById(R.id.etCustomerEmail);
        etPhoneNo = (EditText) findViewById(R.id.etCustomerPhoneNumber);
        etCountry = (EditText) findViewById(R.id.etCustomerCountry);
        etCountryCode = (EditText) findViewById(R.id.etCustomerCountryCode);
        etHouseNumber = (EditText) findViewById(R.id.etHouseNumber);
        etPostalCode = (EditText) findViewById(R.id.etCustomerPostalCode);
        btAddCustomer = (Button) findViewById(R.id.add_Custmer);
        btCancel = (Button) findViewById(R.id.addCustmer_BTCancel);

//        dateFormatter = new SimpleDateFormat(UtilityDateFormater.Format1);
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
            club.add(groupList.get(i).getname());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, club);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter to spinner
        selectClubSpinner.setAdapter(dataAdapter1);
        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                int childCount = group.getChildCount();
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);


                    if(btn.getId()==R.id.male){
                        btn.setText("Male");
                    }else{
                        btn.setText("Female");
                    }
                    if (btn.getId() == checkedId) {

                        gender=btn.getText().toString();// here gender will contain M or F.

                    }

                }

                Log.e("Gender",gender);
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