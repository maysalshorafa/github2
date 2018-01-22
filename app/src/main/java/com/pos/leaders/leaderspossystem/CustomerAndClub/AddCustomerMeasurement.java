package com.pos.leaders.leaderspossystem.CustomerAndClub;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
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
                customerClubName.setText(": "+group.getname());
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
                String elementType =jsonObject.getString("unit");  // json unit (elementType)
                //end

                //EditText Region
                if(elementType.equalsIgnoreCase("E")){
                    LinearLayout lLayout= new LinearLayout(getApplicationContext());
                    lLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT); // or set height to any fixed value you want
                    lp.setMargins(5,5,5,5);
                    lLayout.setLayoutParams(lp);
                    //draw dynamically text view and store it in layout using "name" parameter from json object
                    String eachData =jsonObject.getString("name");
                    TextView customOptionsName = new TextView(AddCustomerMeasurement.this);
                    customOptionsName.setTextSize(20);
                    customOptionsName.setTextColor(Color.BLACK);
                    customOptionsName.setPadding(0, 15, 0, 15);
                    customOptionsName.setText(eachData);
                    final LinearLayout.LayoutParams TLParams = new LinearLayout.LayoutParams(80, LinearLayout.LayoutParams.WRAP_CONTENT);
                    customOptionsName.setLayoutParams(TLParams);
                    lLayout.addView(customOptionsName);
                    // end

                    //draw dynamically Edit Text and store it in layout if type Parameter in json object is "E"
                    final EditText editTextTypeOptions = new EditText(AddCustomerMeasurement.this);
                    editTextTypeOptions.setTextSize(20);
                    editTextTypeOptions.setTextColor(Color.BLACK);
                    editTextTypeOptions.setBackground(getResources().getDrawable(R.drawable.bubble));
                    final LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(400,50);
                    lParams.gravity = Gravity.CENTER;
                    editTextTypeOptions.setLayoutParams(lParams);
                    editTextTypeOptions.setPadding(0, 15, 0, 15);
                    lLayout.addView(editTextTypeOptions);
                    if(editTextTypeOptions.getText().toString()!="") {
                        editText.put(dynamicVariableId, editTextTypeOptions); // put edit text in edit text list
                    }
                    if((noOfCustomOpt%2)==0){
                        viewElementLayoutEven.addView(lLayout);

                    }
                    else{
                        viewElementLayout.addView(lLayout);
                    }
                    if(jsonObject.getString("type").equalsIgnoreCase("string")){
                    editTextTypeOptions.setInputType(InputType.TYPE_CLASS_TEXT);
                    }
                    else {
                        editTextTypeOptions.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);

                    }
                }
                //End EditText Region

                //CheckBox
                if(elementType.equalsIgnoreCase("C")){
                    //draw dynamically CheckBox and store it in layout if type Parameter in json object is "C"
                    final CheckBox chk = new CheckBox(AddCustomerMeasurement.this);
                    chk.setText(jsonObject.getString("name"));
                    chk.setTextSize(20);
                    chk.setTextColor(Color.BLACK);
                        viewElementLayoutEven.addView(chk);


                    checkBoxes.put(dynamicVariableId,chk); // add check box to check box list

                }

                //end CheckBox
                // Spinner Region
                if(elementType.equalsIgnoreCase("S")){
                    //draw dynamically text view and store it in layout using "name" parameter from json object
                    String eachData =jsonObject.getString("name");
                    TextView customOptionsName = new TextView(AddCustomerMeasurement.this);
                    customOptionsName.setTextSize(20);
                    customOptionsName.setTypeface(null, Typeface.BOLD);
                    customOptionsName.setTextColor(Color.BLACK);
                    customOptionsName.setPadding(0, 15, 0, 15);
                    customOptionsName.setText(eachData);
                    final LinearLayout.LayoutParams TLParams = new LinearLayout.LayoutParams(400,80);
                    customOptionsName.setLayoutParams(TLParams);
                    viewElementLayout.addView(customOptionsName);
                    // end
                    //draw dynamically Spinner and store it in layout if type Parameter in json object is "S"
                    Spinner spinnerTypeOptions = new Spinner(AddCustomerMeasurement.this);
                    if((noOfCustomOpt%2)==0){
                        viewElementLayoutEven.addView(spinnerTypeOptions);

                    }
                    else{
                        viewElementLayout.addView(spinnerTypeOptions);
                    }                    //end
                }
                //end Spinner Region

                //RadioButton Region
                if(elementType.equalsIgnoreCase("R")){
                    //draw dynamically RadioButton and store it in layout if type Parameter in json object is "R"
                    RadioButton radioType = new RadioButton(AddCustomerMeasurement.this);
                    radioType.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    radioType.setText(jsonObject.getString("name"));
                    radioType.setTextSize(20);
                    radioType.setTextColor(Color.BLACK);
                    if((noOfCustomOpt%2)==0){
                        viewElementLayoutEven.addView(radioType);

                    }
                    else{
                        viewElementLayout.addView(radioType);
                    }
                    //end
                }

            }
        } catch (JSONException e) {

            e.printStackTrace();
        }


        addMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(); // add element to listsOfValue
                Toast.makeText(AddCustomerMeasurement.this, measurementValueList.toString(), Toast.LENGTH_LONG).show();
                // insert in data base
                customerMeasurementDBAdapter.open();
                measurementsDetailsDBAdapter.open();
                long measurementId = customerMeasurementDBAdapter.insertEntry(customerId, SESSION._USER.getId(),new Date().getTime()); // insert in customerMeasurement Table
                if(measurementId>0){
                    for (int i=0 ; i<measurementValueList.size();i++){
                        if(isValidValue(measurementValueList.get(i),measurementValueListValueType.get(i))) {
                            measurementsDetailsDBAdapter.insertEntry(measurementId, measurementDynamicVariableId.get(i),measurementValueList.get(i)); //insert list of measurement in measurement details

                        }else {
                            Toast.makeText(AddCustomerMeasurement.this, "Fail Insert  "+measurementValueList.get(i) + " Please Insert Value In Type "+measurementValueListValueType.get(i), Toast.LENGTH_LONG).show();

                        }
                    }

                    customerMeasurementDBAdapter.close();
                    measurementsDetailsDBAdapter.close();
                }else {
                    Toast.makeText(AddCustomerMeasurement.this, "Fail Insert ...try again", Toast.LENGTH_LONG).show();
                }

                clear();
            }
        });
        cancelMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public static boolean isValidValue(String measurementValue, String type) {  // this method to test if the value insert from customer valid with type value in json
        if (type.equalsIgnoreCase("double")) // Double Number Case
        {

            if (Util.isDouble(measurementValue)||Util.isFloat(measurementValue)) {

                return true;
            }

            return false;
        }
        if (type.equalsIgnoreCase("float"))  // Float Number Case
        {

            if (Util.isFloat(measurementValue)) { // calculate no of floating point in float number
                int integerPlaces = measurementValue.indexOf('.');
                int decimalPlaces = measurementValue.length() - integerPlaces - 1;
                if (decimalPlaces <= 7) {
                    return true;
                } else {
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
                    return true;
                } else {
                    return false;
                }
            }

            return false;
        }
        if (type.equalsIgnoreCase("long")) { //Long Number Case
            if (Util.isLong(measurementValue))
                return true;
            else
                return false;
        }


        if (type.equalsIgnoreCase("integer")) { //Integer Number Case

            if (Util.isInteger(measurementValue))
                return true;
            else
                return false;


        }

        if (type.equalsIgnoreCase("string")) { // String Case
            if(measurementValue.equals(""))
                return false;
            if (Util.isString(measurementValue)) {

                return true;
            }

            return false;

        }
        if (type.equalsIgnoreCase("bit")) { //Bit Number Case
            if (Util.isBit(measurementValue)) {

                return true;
            }

            return false;

        }
        if (type.equalsIgnoreCase("boolean")) { //Boolean Number Case
            if (Util.isBoolean(measurementValue)) {

                return true;
            }

            return false;

        }

        return false;
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
                String elementType =jsonObject.getString("unit");  // json unit (elementType)}
                if(elementType.equalsIgnoreCase("E")){
                    EditText e = editText.get(dynamicVariableId);
                    // add editText Value and id if it enable
                    measurementValueList.add(e.getText().toString());
                    measurementDynamicVariableId.add(dynamicVariableId);
                    measurementValueListValueType.add(type);

                }
                if(elementType.equalsIgnoreCase("C")){
                    CheckBox checkBox = checkBoxes.get(dynamicVariableId);
                    if(checkBox.isChecked()) {
                        //if type is boolean the value insert must be 0 or 1 else if type is string or any thing the value inserted is the value of parameter "name" in json
                        if(jsonObject.getString("name").equalsIgnoreCase("True")){
                            measurementValueList.add("1");
                            measurementDynamicVariableId.add(dynamicVariableId);
                            measurementValueListValueType.add(type);
                        }
                        else   if(jsonObject.getString("name").equalsIgnoreCase("False")){
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
                String type = jsonObject.getString("type");
                String elementType =jsonObject.getString("unit");  // json unit (elementType)}
                if(elementType.equals("E")){
                    EditText e = editText.get(dynamicVariableId);
                    // add editText Value and id if it enable
                    e.setText("");

                }
                if(elementType.equals("C")){
                    CheckBox checkBox = checkBoxes.get(dynamicVariableId);
                    checkBox.setChecked(false);
                }

            } }catch (JSONException e) {

            e.printStackTrace();

        }

    }
}
