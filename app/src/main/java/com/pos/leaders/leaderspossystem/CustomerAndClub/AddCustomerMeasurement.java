package com.pos.leaders.leaderspossystem.CustomerAndClub;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CityDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClubAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerMeasurementAdapter.CustomerMeasurementDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerMeasurementAdapter.MeasurementDynamicVariableDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerMeasurementAdapter.MeasurementsDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.Models.City;
import com.pos.leaders.leaderspossystem.Models.Club;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AddCustomerMeasurement extends AppCompatActivity {
    //CustomerMeasurement Variable
    ArrayList<HashMap<String, String>> storeList = new ArrayList<HashMap<String, String>>(); //List to store dynamic variable measurement
    HashMap< Long,EditText> editText = new HashMap<Long,EditText>();
    HashMap< Long,CheckBox> checkBoxes =  new HashMap<Long, CheckBox>();
    public List<String> measurementValueList= new ArrayList<String>(); //list of measured value
    List<Long> measurementDynamicVariableId =new ArrayList<Long>(); //list of measured value id
    List<String>measurementValueListValueType= new ArrayList<String>(); //list of measured value type
    //end
    long customerId;
    TextView etCustomerName , customerClubName , customerAddress , customerPhoneNumber , customerEmail , tvNoOfMeasurement;
    private List<Club> groupList = null;
    private List<City> cityList = null;
    CustomerMeasurementDBAdapter customerMeasurementDBAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_customer_measurement);
        TitleBar.setTitleBar(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            customerId = (long) bundle.get("id");
        }
        CustomerDBAdapter customerDBAdapter=new CustomerDBAdapter(this);
        customerDBAdapter.open();
        final com.pos.leaders.leaderspossystem.Models.Customer customer = customerDBAdapter.getCustomerByID(customerId);
        etCustomerName =(TextView) findViewById(R.id.etCustomerName);
        customerClubName =(TextView) findViewById(R.id.etCustomerClubName);
        customerAddress =(TextView) findViewById(R.id.etCustomerAddress);
        customerPhoneNumber =(TextView) findViewById(R.id.etCustomerPhoneNumber);
        customerEmail =(TextView)findViewById(R.id.etCustomerEmail);
        tvNoOfMeasurement =(TextView)findViewById(R.id.noOfCustomerMeasurement);
        etCustomerName.setText(": "+customer.getFirstName()+" "+customer.getLastName());
        customerMeasurementDBAdapter = new CustomerMeasurementDBAdapter(AddCustomerMeasurement.this);
        customerMeasurementDBAdapter.open();
        CityDbAdapter cityDbAdapter = new CityDbAdapter(AddCustomerMeasurement.this);
        cityDbAdapter.open();
        cityList = cityDbAdapter.getAllCity();
        for (int i = 0; i < cityList.size(); i++) {
            City city = cityList.get(i);
            if (city.getId() == customer.getCity()) {
                customerAddress.setText(customer.getHouseNumber()+"-"+customer.getStreet()+"-"+city.getName()+"-"+customer.getCountry());
            }        }
        customerPhoneNumber.setText(customer.getPhoneNumber());
        ClubAdapter groupAdapter = new ClubAdapter(AddCustomerMeasurement.this);
        groupAdapter.open();
        groupList = groupAdapter.getAllGroup();
        for (int i = 0; i < groupList.size(); i++) {
            Club group = groupList.get(i);
            if (group.getId() == customer.getClub()) {
                customerClubName.setText(": "+group.getName());
            }
        }
        customerEmail.setText(customer.getEmail());
        int noOfMeasurement= customerMeasurementDBAdapter.noOfCustomerMeasurement(customer.getId());
        tvNoOfMeasurement.setText(": "+noOfMeasurement);
        addCustomerMeasurement();
    }
    private void addCustomerMeasurement() {
        // {E,C,R,S} mean {EditText,CheckBox , RadioButton , Spinner}
        LinearLayout viewElementLayout = (LinearLayout) findViewById(R.id.customOptionLLODD); //to store element in it
        LinearLayout viewElementLayoutEven = (LinearLayout) findViewById(R.id.customOptionLLEVEN); //to store element in it
        Button addMeasurement = (Button)findViewById(R.id.addMeasurement);
        Button  cancelMeasurement = (Button)findViewById(R.id.cancelMeasurement);
        final CustomerMeasurementDBAdapter customerMeasurementDBAdapter = new CustomerMeasurementDBAdapter(this);
        final MeasurementsDetailsDBAdapter measurementsDetailsDBAdapter = new MeasurementsDetailsDBAdapter(this);
        MeasurementDynamicVariableDBAdapter measurementDynamicVariableDBAdapter =   new MeasurementDynamicVariableDBAdapter(this);
        measurementDynamicVariableDBAdapter.open();

        // ArrayList to store all dynamic variable measurements as json array

        storeList = measurementDynamicVariableDBAdapter.getAllMeasurementDynamicVariable();
        JSONObject jsonObject = new JSONObject();

        //draw element dynamically using storeList

        try {
            for (int noOfCustomOpt = 0; noOfCustomOpt < storeList.size(); noOfCustomOpt++) {
                jsonObject = new JSONObject(storeList.get(noOfCustomOpt));
                // get information from json
                Long dynamicVariableId=jsonObject.getLong("id");  //json id
                String elementType =jsonObject.getString("type");  // json type (elementType)
                //end
                //CheckBox
                if(elementType.equalsIgnoreCase("boolean")){
                    //draw dynamically CheckBox and store it in layout if type Parameter in json object is "C"
                    final CheckBox chk = new CheckBox(AddCustomerMeasurement.this);
                    chk.setText(jsonObject.getString("name"));
                    chk.setTextSize(20);
                    chk.setTextColor(Color.BLACK);
                    viewElementLayoutEven.addView(chk);
                    checkBoxes.put(dynamicVariableId,chk); // add check box to check box list
                    //end CheckBox
                }
                else {
                    LinearLayout lLayout = new LinearLayout(getApplicationContext());
                    lLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); // or set height to any fixed value you want
                    lLayout.setLayoutParams(lp);
                    lLayout.setPadding(16, 16, 16, 16);
                    //draw dynamically text view and store it in layout using "name" parameter from json object
                    String eachData = jsonObject.getString("name");
                    TextView customOptionsName = new TextView(AddCustomerMeasurement.this);
                    customOptionsName.setTextSize(20);
                    customOptionsName.setTextColor(Color.BLACK);
                    customOptionsName.setPadding(16, 16, 16, 16);
                    customOptionsName.setText(eachData);
                    final LinearLayout.LayoutParams TLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    customOptionsName.setLayoutParams(TLParams);
                    lLayout.addView(customOptionsName);
                    // end
                    //draw dynamically Edit Text and store it in layout if type Parameter in json object is "E"
                    final EditText editTextTypeOptions = new EditText(AddCustomerMeasurement.this);
                    editTextTypeOptions.setTextSize(20);
                    editTextTypeOptions.setTextColor(Color.BLACK);
                    editTextTypeOptions.setBackground(getResources().getDrawable(R.drawable.bubble));
                    final LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lParams.gravity = Gravity.CENTER;
                    lParams.gravity = Gravity.CENTER;
                    editTextTypeOptions.setLayoutParams(lParams);
                    editTextTypeOptions.setPadding(16, 16, 16, 16);
                    lLayout.addView(editTextTypeOptions);
                    if (editTextTypeOptions.getText().toString() != "") {
                        editText.put(dynamicVariableId, editTextTypeOptions); // put edit text in edit text list
                    }
                    if ((noOfCustomOpt % 2) == 0) {
                        viewElementLayoutEven.addView(lLayout);

                    } else {
                        viewElementLayout.addView(lLayout);
                    }

                    if (elementType.equalsIgnoreCase("string")) {
                        editTextTypeOptions.setInputType(InputType.TYPE_CLASS_TEXT);
                    } else if (elementType.equalsIgnoreCase("integer")) {
                        editTextTypeOptions.setInputType(InputType.TYPE_CLASS_NUMBER);
                    } else {
                        editTextTypeOptions.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                    }
                }

                //End EditText Region
            }
            }
        catch (JSONException e) {

            e.printStackTrace();
        }


        addMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(); // add element to listsOfValue
                // insert in data base
                customerMeasurementDBAdapter.open();
                measurementsDetailsDBAdapter.open();
                        if(isValidValue(measurementValueList,measurementValueListValueType)) {
                            long measurementId = customerMeasurementDBAdapter.insertEntry(customerId, SESSION._USER.getId(),new Date().getTime()); // insert in customerMeasurement Table
                            if(measurementId>0){
                                for (int a=0 ; a<measurementValueList.size();a++){
                                    measurementsDetailsDBAdapter.insertEntry(measurementId, measurementDynamicVariableId.get(a),measurementValueList.get(a)); //insert list of measurement in measurement details
                                    }
                                }
                            clear();
                            customerMeasurementDBAdapter.close();
                                measurementsDetailsDBAdapter.close();
                            }
                            else {
                                Toast.makeText(AddCustomerMeasurement.this, "Fail Insert ...try again", Toast.LENGTH_LONG).show();
                            measurementValueList= new ArrayList<String>(); //list of measured value
                            measurementDynamicVariableId =new ArrayList<Long>(); //list of measured value id
                            measurementValueListValueType= new ArrayList<String>(); //list of measured value type

                        }

                        }




        });
        cancelMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public boolean isValidValue(List<String> measurementValueList, List<String> measurementValueListValueType) {  // this method to test if the value insert from customer valid with type value in json
        for (int i=0 ; i<measurementValueList.size();i++){
        String measurementValue = measurementValueList.get(i);
        String type = measurementValueListValueType.get(i);
            if(!measurementValue.equals("")){
                if (type.equalsIgnoreCase("double")){ // Double Number Case
            if (Util.isDouble(measurementValue)||Util.isFloat(measurementValue)) {

              //  return true;
            }
            else {
                Toast.makeText(AddCustomerMeasurement.this, "Fail Insert  " + measurementValueList.get(i) + " Please Insert Value In Type " + measurementValueListValueType.get(i), Toast.LENGTH_LONG).show();

                return false;
            }
        }
        if (type.equalsIgnoreCase("float"))  // Float Number Case
        {

            if (Util.isFloat(measurementValue)) { // calculate no of floating point in float number
                int integerPlaces = measurementValue.indexOf('.');
                int decimalPlaces = measurementValue.length() - integerPlaces - 1;
                if (decimalPlaces <= 7) {
                  //  return true;
                } else {
                    Toast.makeText(AddCustomerMeasurement.this, "Fail Insert  " + measurementValueList.get(i) + " Please Insert Value In Type " + measurementValueListValueType.get(i), Toast.LENGTH_LONG).show();
                    return false;
                }
            }

            return false;
        }
        if (type.equalsIgnoreCase("decimal")) { //Decimal Number Case

            if (Util.isDecimal(measurementValue)||Util.isDouble(measurementValue)||Util.isFloat(measurementValue)) { // calculate no of floating point in decimal number
                int integerPlaces = measurementValue.indexOf('.');
                int decimalPlaces = measurementValue.length() - integerPlaces - 1;
                if (decimalPlaces <= 29) {
                   // return true;
                } else {
                    Toast.makeText(AddCustomerMeasurement.this, "Fail Insert  " + measurementValueList.get(i) + " Please Insert Value In Type " + measurementValueListValueType.get(i), Toast.LENGTH_LONG).show();

                    return false;
                }
            }

            return false;
        }
        if (type.equalsIgnoreCase("long")) { //Long Number Case
            if (Util.isLong(measurementValue)){
               // return true;
            }
            else{
                Toast.makeText(AddCustomerMeasurement.this, "Fail Insert  " + measurementValueList.get(i) + " Please Insert Value In Type " + measurementValueListValueType.get(i), Toast.LENGTH_LONG).show();

                return false;
            }
        }


        if (type.equalsIgnoreCase("integer")) { //Integer Number Case

            if (Util.isInteger(measurementValue)) {
               // return true;
            }
            else {
                Toast.makeText(AddCustomerMeasurement.this, "Fail Insert  " + measurementValueList.get(i) + " Please Insert Value In Type " + measurementValueListValueType.get(i), Toast.LENGTH_LONG).show();
                return false;
            }
        }

        if (type.equalsIgnoreCase("string")) { // String Case
            if(measurementValue.equals("")){
                Toast.makeText(AddCustomerMeasurement.this, "Fail Insert  " + measurementValueList.get(i) + " Please Insert Value In Type " + measurementValueListValueType.get(i), Toast.LENGTH_LONG).show();

                return false;
            }
            if (Util.isString(measurementValue)) {

             //   return true;
            }

            else {
                Toast.makeText(AddCustomerMeasurement.this, "Fail Insert  " + measurementValueList.get(i) + " Please Insert Value In Type " + measurementValueListValueType.get(i), Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (type.equalsIgnoreCase("bit")) { //Bit Number Case
            if (Util.isBit(measurementValue)) {

              //  return true;
            }else {
                Toast.makeText(AddCustomerMeasurement.this, "Fail Insert  " + measurementValueList.get(i) + " Please Insert Value In Type " + measurementValueListValueType.get(i), Toast.LENGTH_LONG).show();

            }

            return false;

        }
        if (type.equalsIgnoreCase("boolean")) { //Boolean Number Case
            if (Util.isBoolean(measurementValue)) {

                return true;
            }
            else {
                Toast.makeText(AddCustomerMeasurement.this, "Fail Insert  " + measurementValueList.get(i) + " Please Insert Value In Type " + measurementValueListValueType.get(i), Toast.LENGTH_LONG).show();
           return false; }
        }

    }
        }
    return true;
    }
    //End is Valid Method
    public  void add() {
        JSONObject  jsonObject ;
        try {
            for (int noOfCustomOpt = 0; noOfCustomOpt < storeList.size(); noOfCustomOpt++) {
                jsonObject = new JSONObject(storeList.get(noOfCustomOpt));
                // get information from json
                long   dynamicVariableId=jsonObject.getLong("id");  //json id
                String type = jsonObject.getString("type");
                if(type.equalsIgnoreCase("boolean")){
                    CheckBox checkBox = checkBoxes.get(dynamicVariableId);
                    if(checkBox.isChecked()) {
                        //if type is boolean the value insert must be 0 or 1 else if type is string or any thing the value inserted is the value of parameter "name" in json
                        if(jsonObject.getString("name").equalsIgnoreCase("True")){
                            measurementValueList.add("1");
                            measurementDynamicVariableId.add(dynamicVariableId);
                            measurementValueListValueType.add(type);
                        }
                        else if(jsonObject.getString("name").equalsIgnoreCase("False")){
                            measurementValueList.add("0");
                            measurementDynamicVariableId.add(dynamicVariableId);
                            measurementValueListValueType.add(type);
                        }else {
                            measurementValueList.add(jsonObject.getString("name"));
                            measurementDynamicVariableId.add(dynamicVariableId);
                            measurementValueListValueType.add(type);
                        }
                    }
                }
                else {
                    EditText e = editText.get(dynamicVariableId);
                    // add editText Value and id if it enable
                    if(!e.getText().equals("")){
                    measurementValueList.add(e.getText().toString());
                    measurementDynamicVariableId.add(dynamicVariableId);
                    measurementValueListValueType.add(type);
                }
                }

            } }catch (JSONException e) {

            e.printStackTrace();

        }

    }
    public  void clear() { // if user end first measurement and want to add another measurement so must clear {list , element....}
        measurementValueList= new ArrayList<String>(); //list of measured value
        measurementDynamicVariableId =new ArrayList<Long>(); //list of measured value id
        measurementValueListValueType= new ArrayList<String>(); //list of measured value type

        JSONObject  jsonObject ;
        try {
            for (int noOfCustomOpt = 0; noOfCustomOpt < storeList.size(); noOfCustomOpt++) {
                jsonObject = new JSONObject(storeList.get(noOfCustomOpt));
                // get information from json
                long   dynamicVariableId=jsonObject.getLong("id");  //json id
                String elementType =jsonObject.getString("type");  // json type (elementType)}
                if(elementType.equalsIgnoreCase("boolean")){
                    CheckBox checkBox = checkBoxes.get(dynamicVariableId);
                    checkBox.setChecked(false);
                }
                if(editText.get(dynamicVariableId)!=null) {
                    EditText e = editText.get(dynamicVariableId);
                    e.setText("");
                }

            }
        }catch (JSONException e) {

            e.printStackTrace();

        }
        onBackPressed();

    }
}
