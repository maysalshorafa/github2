package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import com.pos.leaders.leaderspossystem.DataBaseAdapter.PosSettingDbAdapter;
import com.pos.leaders.leaderspossystem.Models.Currency.ChooseCurrencyList;
import com.pos.leaders.leaderspossystem.Models.PosSetting;
import com.pos.leaders.leaderspossystem.Tools.CompanyStatus;
import com.pos.leaders.leaderspossystem.Tools.PrinterType;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.ServerUrl;
import com.pos.leaders.leaderspossystem.Tools.ThisApp;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.Tools.countryWithCodeHasMap;
import com.pos.leaders.leaderspossystem.Tools.symbolWithCodeHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SetUpManagement extends AppCompatActivity {
    CheckBox currencyCheckBox, creditCardCheckBox,cbPinpad, customerMeasurementCheckBox;
    Spinner printerTypeSpinner, floatPointSpinner , branchSpinner,SelectServer,CompanyStatusSpinner,currencyCodeSpinner,currencyCountrySpinner;
    Button saveButton, editButton;
    ImageView currencyImage, customerMeasurementImage, creditCardImage,ivPinpad;
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY = "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CREDIT_CARD = "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CREDIT_CARD";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PIN_PAD = "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PIN_PAD";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CUSTOMER_MEASUREMENT = "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CUSTOMER_MEASUREMENT";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_FLOAT_POINT = "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_FLOAT_POINT_2";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PRINTER_TYPE = "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PRINTER_TYPE";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_BRANCH_ID = "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_BRANCH_ID";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_SERVER_URL= "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_SERVER_URL";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_COMPANY_STATUS= "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_COMPANY_STATUS";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_CURRENCY_CODE="LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_CURRENCY_CODE";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_CURRENCY_SYMBOL="LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_CURRENCY_SYMBOL";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_COUNTRY="LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_COUNTRY";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_LIST_CURRENCY_TYPE="LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_LIST_CURRENCY_LIST";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY_CODE_LIST="LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY_CODE_LIST";
    public static final String LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_DUPLICATE_INVOICE = "LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_DUPLICATE_INVOICE";




    boolean currencyEnable, creditCardEnable,pinpadEnable, customerMeasurementEnable = false;
    int noOfPoint;
    String printerType , serverUrl,CompanyStatusSelect,currencySymbolSelect,currencyCodeSelect,currencyCountrySelect,codeDebendCountry;
    ArrayAdapter<String> spinnerArrayAdapter,companyStatusArryAdapter,currencyCodeArrayAdapter,currencySymbolArrayAdapter;
    ArrayAdapter<Integer> floatPointSpinnerArrayAdapter;
    Integer floatPoint[];
    String [] CompanyStautsList;
    String printer[],codeList[],countryList[];
    public final static String POS_Management = "POS_Management";
    public final static String POS_Company_status ="Company_Status";
    public final static String POS_Currency = "POS_Currency";
    public static Context context = null;
    int branchId,companyStatusID=0;
    final List<String> branch = new ArrayList<String>();
    final List<String> ServerUrL = new ArrayList<String>();
    List<String> ArrayCurrency = new ArrayList<String>();
    boolean[] checkedCurrency;
    JSONArray jsonArray1 = new JSONArray();
    JSONObject jsontemp ;
    List<ChooseCurrencyList> list = new ArrayList<ChooseCurrencyList>();
    int positionCodeDependCountry;

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
        ThisApp.setCurrentActivity(this);

        context = this;



        currencyCheckBox = (CheckBox) findViewById(R.id.setUpManagementCurrencyCheckBox);
        creditCardCheckBox = (CheckBox) findViewById(R.id.setUpManagementCreditCardCheckBox);

        cbPinpad = (CheckBox) findViewById(R.id.setUpManagementCreditCardPinPadCheckBox);

        customerMeasurementCheckBox = (CheckBox) findViewById(R.id.setUpManagementCustomerMeasurementCheckBox);
        floatPointSpinner = (Spinner) findViewById(R.id.setUpManagementFloatPointSpinner);
        printerTypeSpinner = (Spinner) findViewById(R.id.setUpManagementPrinterTypeSpinner);
        SelectServer=(Spinner) findViewById(R.id.setUpServer);
        branchSpinner = (Spinner) findViewById(R.id.setUpManagementBranchSpinner);
        currencyCodeSpinner=(Spinner) findViewById(R.id.setUpCurrencyCode);
        currencyCountrySpinner=(Spinner) findViewById(R.id.setUpCurrencyCountry);
        CompanyStatusSpinner =(Spinner) findViewById(R.id.setUpCompanyStatus);
        saveButton = (Button) findViewById(R.id.setUpManagementSaveButton);
        editButton = (Button) findViewById(R.id.setUpManagementEditButton);
        currencyImage = (ImageView) findViewById(R.id.currencyImage);


        creditCardImage = (ImageView) findViewById(R.id.creditCardImage);
        ivPinpad = (ImageView) findViewById(R.id.creditCardPinPadImage);

        customerMeasurementImage = (ImageView) findViewById(R.id.customerMeasurementImage);
        printer = new String[]{PrinterType.BTP880.name(), PrinterType.HPRT_TP805.name(), PrinterType.SUNMI_T1.name(), PrinterType.SM_S230I.name(), PrinterType.WINTEC_BUILDIN.name()};
         ;
        CompanyStautsList =new String[]{CompanyStatus.BO_COMPANY.name(),
                CompanyStatus.BO_AUTHORIZED_DEALER.name(),
                CompanyStatus.BO_EXEMPT_DEALER.name()};

       codeList =new String[]{countryWithCodeHasMap.Israel.getValue(),countryWithCodeHasMap.Australia.getValue(),countryWithCodeHasMap.Canada.getValue(),countryWithCodeHasMap.Denmark.getValue(),
               countryWithCodeHasMap.Egypt.getValue(),countryWithCodeHasMap.EMU.getValue(),countryWithCodeHasMap.GreatBritain.getValue(),
               countryWithCodeHasMap.Japan.getValue(),countryWithCodeHasMap.Jordan.getValue(),countryWithCodeHasMap.Lebanon.getValue(),
               countryWithCodeHasMap.Norway.getValue(),countryWithCodeHasMap.SouthAfrica.getValue(),countryWithCodeHasMap.USA.getValue(),
               countryWithCodeHasMap.Sweden.getValue(),countryWithCodeHasMap.Switzerland.getValue()};

        floatPoint = new Integer[]{2, 0, 1, 3};

        countryList=new String[]{String.valueOf(countryWithCodeHasMap.Israel),String.valueOf(countryWithCodeHasMap.Australia), String.valueOf(countryWithCodeHasMap.Canada), String.valueOf(countryWithCodeHasMap.Denmark),
                String.valueOf(countryWithCodeHasMap.Egypt), String.valueOf(countryWithCodeHasMap.EMU), String.valueOf(countryWithCodeHasMap.GreatBritain),
                String.valueOf(countryWithCodeHasMap.Japan), String.valueOf(countryWithCodeHasMap.Jordan),
                String.valueOf(countryWithCodeHasMap.Lebanon), String.valueOf(countryWithCodeHasMap.Norway),
                String.valueOf(countryWithCodeHasMap.SouthAfrica), String.valueOf(countryWithCodeHasMap.USA), String.valueOf(countryWithCodeHasMap.Sweden),
                String.valueOf(countryWithCodeHasMap.Switzerland)};

        companyStatusArryAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CompanyStautsList);
        companyStatusArryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view);
        CompanyStatusSpinner.setAdapter(companyStatusArryAdapter);


        currencyCodeArrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, codeList);
        currencyCodeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view);
        currencyCodeSpinner.setAdapter(currencyCodeArrayAdapter);


        currencySymbolArrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countryList);
        currencySymbolArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view);
        currencyCountrySpinner.setAdapter(currencySymbolArrayAdapter);

        spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, printer);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view

        floatPointSpinnerArrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, floatPoint);
        floatPointSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view

        printerTypeSpinner.setAdapter(spinnerArrayAdapter);
        floatPointSpinner.setAdapter(floatPointSpinnerArrayAdapter);
        ServerUrL.add(ServerUrl.BO_SERVER_URL.getItem());
        branch.add(getString(R.string.all));
        branch.add(getString(R.string.pos_branch));
        final ArrayAdapter<String> dataAdapterBranch = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, branch);
        dataAdapterBranch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchSpinner.setAdapter(dataAdapterBranch);

        final ArrayAdapter<String> dataAdapterURlServer = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,ServerUrL );
        dataAdapterURlServer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SelectServer.setAdapter(dataAdapterURlServer);

        List<symbolWithCodeHashMap> somethingList = Arrays.asList(symbolWithCodeHashMap.values());
        Log.d("symbolWithCodeHashMap",somethingList.toString());
       for (int i=0;i<somethingList.size();i++) {
            ChooseCurrencyList regionTransportar = new ChooseCurrencyList(String.valueOf(somethingList.get(i)),String.valueOf(somethingList.get(i).getValue()));
            list.add(regionTransportar);
           ArrayCurrency.add(String.valueOf(somethingList.get(i)));
        }

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
                    showDialog(SetUpManagement.this);
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

        currencyCodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                currencyCodeSelect=currencyCodeArrayAdapter.getItem(position);
                currencySymbolSelect=String.valueOf(symbolWithCodeHashMap.valueOf(currencyCodeSelect).getValue());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        currencyCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                currencyCountrySelect=currencySymbolArrayAdapter.getItem(position);
                codeDebendCountry=countryWithCodeHasMap.valueOf(currencyCountrySelect).getValue();
                for (int i=0;i<codeList.length;i++){
                    if (codeDebendCountry.equals(codeList[i])){
                currencyCodeSpinner.setSelection(i);}}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

     CompanyStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

             CompanyStatusSelect=companyStatusArryAdapter.getItem(position);
         }

         @Override
         public void onNothingSelected(AdapterView<?> parent) {

         }
     });
        SelectServer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               serverUrl=SelectServer.getSelectedItem().toString();
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
                editor.putString(LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_BRANCH_ID, branchId+"");
                editor.putString(LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_SERVER_URL, serverUrl);
                //editor.putString(LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_DUPLICATE_INVOICE,)
              // Log.d("companySelectd",CompanyStatusSelect);
                SETTINGS.BO_SERVER_URL=serverUrl;
            //    SETTINGS.companyStatus=CompanyStatusSelect;

                editor.apply();


                SharedPreferences preferencesCompany= getSharedPreferences(POS_Company_status, MODE_PRIVATE);
                final SharedPreferences.Editor editorCompany = preferencesCompany.edit();
                editorCompany.putString(LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_COMPANY_STATUS,CompanyStatusSelect);
                editorCompany.apply();



                //currencyCode and currencySymbol
                SharedPreferences preferencesCurrency= getSharedPreferences(POS_Currency, MODE_PRIVATE);
                final SharedPreferences.Editor editorCurrency = preferencesCurrency.edit();
                editorCurrency.putString(LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_CURRENCY_CODE,currencyCodeSelect);
                editorCurrency.putString(LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_CURRENCY_SYMBOL,currencySymbolSelect);
                editorCurrency.putString(LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_COUNTRY,currencyCountrySelect);
                editorCurrency.putString(LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_LIST_CURRENCY_TYPE,jsonArray1.toString());
                editorCurrency.apply();


                if (Util.isFirstLaunch(SetUpManagement.this, false)) {
                    Intent i = new Intent(SetUpManagement.this,SetupNewPOSOnlineActivity.class);
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

                SharedPreferences SharedPreferencesCompanyStatus = getSharedPreferences(POS_Company_status, MODE_PRIVATE);
                final SharedPreferences.Editor editorCompanyStatus = SharedPreferencesCompanyStatus.edit();
                if (SharedPreferencesCompanyStatus != null) {
                    //CompanyStatus
                    if (SharedPreferencesCompanyStatus.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_COMPANY_STATUS)) {
                        String companyStatus = CompanyStatusSelect;
                        Log.d("CompanyStauts2",companyStatus);
                        editorCompanyStatus.putString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_COMPANY_STATUS, companyStatus);
                        CompanyStatus companyStatus1 = CompanyStatus.valueOf(companyStatus);
                        SETTINGS.company = companyStatus1;

                    }
                }

                editorCompanyStatus.apply();
                editor.apply();
                //    public PosSetting( boolean enableCurrency, boolean enableCreditCard, boolean enablePinPad, boolean enableCustomerMeasurement, int noOfFloatPoint, String printerType, String posVersionNo, String posDbVersionNo, int branchId) {

                PosSettingDbAdapter posSettingDbAdapter = new PosSettingDbAdapter(SetUpManagement.this);
                posSettingDbAdapter.open();
                PackageInfo pInfo = null;
                try {
                    pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                String verCode = pInfo.versionName;

                PosSetting posSetting = new PosSetting(currencyEnable,creditCardEnable,pinpadEnable,customerMeasurementEnable,noOfPoint,printerType,CompanyStatusSelect,pInfo.versionName,String.valueOf(DbHelper.DATABASE_VERSION),branchId);
               posSettingDbAdapter.updateEntry(posSetting);
                posSettingDbAdapter.close();
                Toast.makeText(SetUpManagement.this, R.string.success_edit_POS_setting, Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(i);
            }
        });

    }
    private void showDialog(Activity activity){
        if (ArrayCurrency.size() != 0) {
        int count=0;
        final AlertDialog.Builder builder = new AlertDialog.Builder(SetUpManagement.this);

        String[] mStringArray = new String[ArrayCurrency.size()];
        mStringArray = ArrayCurrency.toArray(mStringArray);
            if (checkedCurrency == null) {
                checkedCurrency = new boolean[mStringArray.length];
                for (int i = 0; i < checkedCurrency.length; i++) {
                    checkedCurrency[i] = false;
                }
            }
            builder.setTitle("select 4 Currency");
            builder.setMultiChoiceItems(mStringArray, checkedCurrency, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                    checkedCurrency[which] = isChecked;
                    String currentItem = ArrayCurrency.get(which);
                }
            });

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    jsonArray1 = new JSONArray();
                    int count =0;

                    for (int i = 0; i < checkedCurrency.length; i++) {

                        boolean checked = checkedCurrency[i];
                        if (checked) {
                           if (count<3){
                            try {
                                jsontemp = new JSONObject();
                                jsontemp.put("Key", list.get(i).getKey());


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            jsonArray1.put(jsontemp);
                               Log.d("jsonArray1",jsonArray1.toString());
                               count=count+1;
                        }
                        else {
                               checkedCurrency[i]=false;
                               AlertDialog dialog = builder.create();
                               dialog.show();
                               Toast.makeText(SetUpManagement.this, R.string.please_choose_three_currencies, Toast.LENGTH_SHORT).show();
                           }
                        }

                    }
                    if (count<3){
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        Toast.makeText(SetUpManagement.this, R.string.please_choose_three_currencies, Toast.LENGTH_SHORT).show();
                    }

                }
            });
            builder.setNeutralButton("Cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            Toast.makeText(SetUpManagement.this,R.string.there_are_no_currencies, Toast.LENGTH_SHORT).show();

        }



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

        SharedPreferences SharedPreferencesCompany = getSharedPreferences(POS_Company_status, MODE_PRIVATE);
        if (SharedPreferencesCompany != null) {
            //CompanyStatus
            if (SharedPreferencesCompany.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_COMPANY_STATUS)) {
                String companyStatus =SharedPreferencesCompany.getString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_COMPANY_STATUS,CompanyStatus.BO_AUTHORIZED_DEALER.name());
                Log.d("CompanyStatus3",companyStatus);
                CompanyStatus companyStatus1=CompanyStatus.valueOf(companyStatus);
                for (int i = 0; i < CompanyStautsList.length; i++) {
                    if (CompanyStautsList[i] == companyStatus1.name()) {
                        int spinnerPosition = spinnerArrayAdapter.getPosition(companyStatus1.name());
                        CompanyStatusSpinner.setSelection(spinnerPosition);

                    }
                }
            }
        }
    }






}
