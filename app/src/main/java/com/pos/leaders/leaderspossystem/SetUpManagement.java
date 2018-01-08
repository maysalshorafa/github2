package com.pos.leaders.leaderspossystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Tools.PrinterType;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

public class SetUpManagement extends AppCompatActivity {
    CheckBox currencyCheckBox , creditCardCheckBox , customerMeasurementCheckBox ;
    Spinner printerTypeSpinner , floatPointSpinner;
    Button saveButton ;
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY = "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CREDIT_CARD = "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CREDIT_CARD";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CUSTOMER_MEASUREMENT = "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CUSTOMER_MEASUREMENT";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_FLOAT_POINT = "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_FLOAT_POINT";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PRINTER_TYPE= "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PRINTER_TYPE";
    boolean currencyEnable , creditCardEnable , customerMeasurementEnable = false ;
    int noOfPoint;
    String printerType ;
    ArrayAdapter<String> spinnerArrayAdapter;
    ArrayAdapter<Integer> floatPointSpinnerArrayAdapter;

    public final static String POS_Management = "POS_Management";
    public static Context context = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_set_up_management);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //wlp.gravity = Gravity.CENTER_VERTICAL;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = (float) 0.6;
        window.setAttributes(wlp);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.7), (int) (height * 0.55));
        context = this;
        SharedPreferences preferences = getSharedPreferences(POS_Management, MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        currencyCheckBox = (CheckBox) findViewById(R.id.setUpManagementCurrencyCheckBox);
        creditCardCheckBox = (CheckBox) findViewById(R.id.setUpManagementCreditCardCheckBox);
        customerMeasurementCheckBox = (CheckBox) findViewById(R.id.setUpManagementCustomerMeasurementCheckBox);
        floatPointSpinner= (Spinner) findViewById(R.id.setUpManagementFloatPointSpinner);
        printerTypeSpinner = (Spinner) findViewById(R.id.setUpManagementPrinterTypeSpinner);
        saveButton = (Button)findViewById(R.id.setUpManagementSaveButton);
        String printer[] = {PrinterType.BTP880.name(),PrinterType.HPRT_TP805.name(),PrinterType.SUNMI_T1.name(),PrinterType.SM_S230I.name(),PrinterType.WINTEC_BUILDIN.name()};
        final Integer []floatPoint = {0,1,2,3};

        spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, printer);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        floatPointSpinnerArrayAdapter =new ArrayAdapter<Integer>(this,   android.R.layout.simple_spinner_item, floatPoint);
        floatPointSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        printerTypeSpinner.setAdapter(spinnerArrayAdapter);
        floatPointSpinner.setAdapter(floatPointSpinnerArrayAdapter);
        currencyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    currencyEnable = true;
                }
                else{
                   currencyEnable=false;
                }
            }
        });
        creditCardCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                   creditCardEnable=true;
                }
                else{
                    creditCardEnable=false;
                }

            }
        });
        customerMeasurementCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                  customerMeasurementEnable=true;
                }
                else{
                   customerMeasurementEnable=false;
                }
            }
        });
        printerTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                printerType = spinnerArrayAdapter.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        floatPointSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                noOfPoint = floatPointSpinnerArrayAdapter.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean(LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY,currencyEnable);
                editor.putBoolean(LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CREDIT_CARD,creditCardEnable);
                editor.putBoolean(LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CUSTOMER_MEASUREMENT,customerMeasurementEnable);
                editor.putInt(LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_FLOAT_POINT,noOfPoint);
                editor.putString(LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PRINTER_TYPE,printerType);
                editor.apply();
                Intent i = new Intent(SetUpManagement.this, SplashScreenActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

    }
}
