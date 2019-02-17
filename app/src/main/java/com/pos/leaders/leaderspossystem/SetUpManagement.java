package com.pos.leaders.leaderspossystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.Tools.PrinterType;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.ArrayList;
import java.util.List;

public class SetUpManagement extends AppCompatActivity {
    CheckBox currencyCheckBox, creditCardCheckBox,cbPinpad, customerMeasurementCheckBox;
    Spinner printerTypeSpinner, floatPointSpinner , branchSpinner;
    Button saveButton, editButton;
    ImageView currencyImage, customerMeasurementImage, creditCardImage,ivPinpad;
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY = "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CREDIT_CARD = "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CREDIT_CARD";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PIN_PAD = "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PIN_PAD";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CUSTOMER_MEASUREMENT = "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CUSTOMER_MEASUREMENT";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_FLOAT_POINT = "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_FLOAT_POINT_2";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PRINTER_TYPE = "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PRINTER_TYPE";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_BRANCH_ID = "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_BRANCH_ID";

    boolean currencyEnable, creditCardEnable,pinpadEnable, customerMeasurementEnable = false;
    int noOfPoint;
    String printerType;
    ArrayAdapter<String> spinnerArrayAdapter;
    ArrayAdapter<Integer> floatPointSpinnerArrayAdapter;
    Integer floatPoint[];
    String printer[];
    public final static String POS_Management = "POS_Management";
    public static Context context = null;
    int branchId;
    final List<String> branch = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_set_up_management);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //wlp.gravity = Gravity.CENTER_VERTICAL;
        context = this;
        currencyCheckBox = (CheckBox) findViewById(R.id.setUpManagementCurrencyCheckBox);
        creditCardCheckBox = (CheckBox) findViewById(R.id.setUpManagementCreditCardCheckBox);

        cbPinpad = (CheckBox) findViewById(R.id.setUpManagementCreditCardPinPadCheckBox);

        customerMeasurementCheckBox = (CheckBox) findViewById(R.id.setUpManagementCustomerMeasurementCheckBox);
        floatPointSpinner = (Spinner) findViewById(R.id.setUpManagementFloatPointSpinner);
        printerTypeSpinner = (Spinner) findViewById(R.id.setUpManagementPrinterTypeSpinner);
        branchSpinner = (Spinner) findViewById(R.id.setUpManagementBranchSpinner);
        saveButton = (Button) findViewById(R.id.setUpManagementSaveButton);
        editButton = (Button) findViewById(R.id.setUpManagementEditButton);
        currencyImage = (ImageView) findViewById(R.id.currencyImage);

        creditCardImage = (ImageView) findViewById(R.id.creditCardImage);
        ivPinpad = (ImageView) findViewById(R.id.creditCardPinPadImage);

        customerMeasurementImage = (ImageView) findViewById(R.id.customerMeasurementImage);
        printer = new String[]{PrinterType.BTP880.name(), PrinterType.HPRT_TP805.name(), PrinterType.SUNMI_T1.name(), PrinterType.SM_S230I.name(), PrinterType.WINTEC_BUILDIN.name()};
        floatPoint = new Integer[]{2, 0, 1, 3};
        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, printer);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view

        floatPointSpinnerArrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, floatPoint);
        floatPointSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view

        printerTypeSpinner.setAdapter(spinnerArrayAdapter);
        floatPointSpinner.setAdapter(floatPointSpinnerArrayAdapter);
        branch.add(getString(R.string.all));
        branch.add(getString(R.string.pos_branch));
        final ArrayAdapter<String> dataAdapterBranch = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, branch);
        dataAdapterBranch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchSpinner.setAdapter(dataAdapterBranch);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            saveButton.setVisibility(View.GONE);
            editButton.setVisibility(View.VISIBLE);
            PosManagementValue();
        }
        currencyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    currencyEnable = true;
                } else {
                    currencyEnable = false;
                }
            }
        });
        creditCardCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    creditCardEnable = true;
                } else {
                    creditCardEnable = false;
                }

            }
        });
        cbPinpad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pinpadEnable = true;
                } else {
                    pinpadEnable = false;
                }
            }
        });
        customerMeasurementCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    customerMeasurementEnable = true;
                } else {
                    customerMeasurementEnable = false;
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
        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if(branchSpinner.getSelectedItem().toString().equals(getString(R.string.all))){
                   branchId=0;
                   SETTINGS.enableAllBranch=true;
               }else {
                   SETTINGS.enableAllBranch=false;
               }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences(POS_Management, MODE_PRIVATE);
                final SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY, currencyEnable);
                editor.putBoolean(LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CREDIT_CARD, creditCardEnable);
                editor.putBoolean(LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PIN_PAD, pinpadEnable);

                editor.putBoolean(LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CUSTOMER_MEASUREMENT, customerMeasurementEnable);
                editor.putString(LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_FLOAT_POINT, noOfPoint+"");
                editor.putString(LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PRINTER_TYPE, printerType);
                editor.putString(LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_FLOAT_POINT, branchId+"");

                editor.apply();

                if (Util.isFirstLaunch(SetUpManagement.this, false)) {
                    Intent i = new Intent(SetUpManagement.this, SetupNewPOSOnlineActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                } else {
                    finish();
                }


            }
        });
        currencyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SetUpManagement.this, R.string.active_currency_service_in_pos, Toast.LENGTH_LONG).show();
            }
        });
        creditCardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SetUpManagement.this, R.string.active_credit_card_service_in_pos, Toast.LENGTH_LONG).show();
            }
        });
        ivPinpad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SetUpManagement.this, R.string.active_pin_pad_service_in_pos, Toast.LENGTH_LONG).show();
            }
        });
        customerMeasurementImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SetUpManagement.this, R.string.active_customer_measurement_service_in_pos, Toast.LENGTH_LONG).show();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences cSharedPreferences = getSharedPreferences(POS_Management, MODE_PRIVATE);
                final SharedPreferences.Editor editor = cSharedPreferences.edit();
                if (cSharedPreferences != null) {
                    //CreditCard
                    if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CREDIT_CARD)) {
                        if(creditCardCheckBox.isChecked()){
                            creditCardEnable=true;
                        }else {
                            creditCardEnable=false;
                        }
                        editor.putBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CREDIT_CARD, creditCardEnable);
                        SETTINGS.creditCardEnable = creditCardEnable;
                    }

                    //PinPad
                    if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PIN_PAD)) {
                        if(creditCardCheckBox.isChecked()){
                            pinpadEnable=true;
                        }else {
                            pinpadEnable=false;
                        }
                        editor.putBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PIN_PAD, pinpadEnable);
                        SETTINGS.pinpadEnable = pinpadEnable;
                    }
                    //Currency
                    if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY)) {
                        if(currencyCheckBox.isChecked()){
                            currencyEnable=true;
                        }else {
                            currencyEnable=false;
                        }
                        editor.putBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY, currencyEnable);
                        SETTINGS.enableCurrencies = currencyEnable;
                    }
                    //CustomerMeasurement
                    if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CUSTOMER_MEASUREMENT)) {
                        if(customerMeasurementCheckBox.isChecked()){
                            customerMeasurementEnable=true;
                        }else {
                            customerMeasurementEnable=false;
                        }
                        editor.putBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CUSTOMER_MEASUREMENT, customerMeasurementEnable);
                        SETTINGS.enableCustomerMeasurement = customerMeasurementEnable;
                    }
                    //FloatPoint
                    if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_FLOAT_POINT)) {
                        int editNoOfPoint = noOfPoint;
                        editor.putString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_FLOAT_POINT, editNoOfPoint+"");
                        SETTINGS.decimalNumbers = editNoOfPoint;
                    }
                    //PrinterType
                    if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PRINTER_TYPE)) {
                        String editPrinterType = printerType;
                        editor.putString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PRINTER_TYPE, editPrinterType);
                        PrinterType printer = PrinterType.valueOf(editPrinterType);
                        SETTINGS.printer = printer;
                    }
                    //BranchId
                    if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_BRANCH_ID)) {
                        int branch = branchId;
                        editor.putString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_BRANCH_ID, branch+"");
                        if(branchId==0) {
                            SETTINGS.enableAllBranch = true;
                        }else {
                            SETTINGS.enableAllBranch=false;
                        }
                    }
                }
                editor.apply();
                Toast.makeText(SetUpManagement.this, R.string.success_edit_POS_setting, Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(i);
            }
        });

    }

    private void PosManagementValue() {
        /// sharedPreferences for Setting
        SharedPreferences cSharedPreferences = getSharedPreferences(POS_Management, MODE_PRIVATE);
        if (cSharedPreferences != null) {
            //CreditCard
            if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CREDIT_CARD)) {
                boolean creditCardEnable = cSharedPreferences.getBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CREDIT_CARD, false);
                if (creditCardEnable) {
                    creditCardCheckBox.setChecked(true);
                }
            }

            // end CreditCard

            //pin pad
            if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PIN_PAD)) {
                boolean pinpadEnable = cSharedPreferences.getBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PIN_PAD, false);
                if (pinpadEnable) {
                    cbPinpad.setChecked(true);

                }
            }
            //Currency
            if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY)) {
                boolean currencyEnable = cSharedPreferences.getBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY, false);
                if (currencyEnable) {
                    currencyCheckBox.setChecked(true);
                }
            }

            //end
            //CustomerMeasurement
            if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CUSTOMER_MEASUREMENT)) {
                boolean customerMeasurementEnable = cSharedPreferences.getBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CUSTOMER_MEASUREMENT, false);
                if (customerMeasurementEnable) {
                    customerMeasurementCheckBox.setChecked(true);
                }
            }

            //end
            //FloatPoint
            if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_FLOAT_POINT)) {
                int floatP = Integer.parseInt(cSharedPreferences.getString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_FLOAT_POINT, "2"));
                for (int i = 0; i < floatPoint.length; i++) {
                    if (floatPoint[i] == floatP) {
                        int spinnerPosition = floatPointSpinnerArrayAdapter.getPosition(floatP);
                        floatPointSpinner.setSelection(spinnerPosition);

                    }
                }
            }

            //end
            //PrinterType
            if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PRINTER_TYPE)) {
                String printerType = cSharedPreferences.getString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PRINTER_TYPE, PrinterType.HPRT_TP805.name());
                PrinterType p = PrinterType.valueOf(printerType);
                for (int i = 0; i < printer.length; i++) {
                    if (printer[i] == p.name()) {
                        int spinnerPosition = spinnerArrayAdapter.getPosition(p.name());
                        printerTypeSpinner.setSelection(spinnerPosition);

                    }
                }
            }
            //BranchId
            if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_BRANCH_ID)) {
                int branchI = Integer.parseInt(cSharedPreferences.getString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_BRANCH_ID, "0"));
                    if (branchI==0) {
                        branchSpinner.setSelection(0);

                }else {
                        branchSpinner.setSelection(1);
                    }
            }

            //end


        }
    }
}
