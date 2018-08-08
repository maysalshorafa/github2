package com.pos.leaders.leaderspossystem.Offers;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateOfferActivity extends AppCompatActivity {

    EditText etStart, etEnd , etOfferName ,etRuleValue_1 , etActionValue_1;
    ImageView imForStartDateCalender , imForEndDateCalender , selectOfferResourceType , selectOfferProduct , selectOfferResourceTypeValue ,selectOfferActionType;
    Timestamp startTime , endTime;
    AutoCompleteTextView spOfferRule ,spOfferRule1 , spRuleValue_1 ,spRuleAndOr_1 , spActionValue1;
    List<String> offerRuleList,offerRuleList1 , productNameList , operationList ,actionList;
    List<Product> productList ;
    Button saveOffers;
    String rules ="";
    String product_sku="";
    long product_id;
    int quantity=0;
    double priceForProduct=0;
    String action_name ="";
    LinearLayout llActionValue , ruleLayOut , actionLayOut;
    Offer editableOffer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_offer);

        TitleBar.setTitleBar(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etStart = (EditText) findViewById(R.id.CreateOffer_etStart);
        etEnd = (EditText) findViewById(R.id.CreateOffer_etEnd);
        etOfferName =(EditText) findViewById(R.id.CreateOffer_etName);
        etStart = (EditText) findViewById(R.id.CreateOffer_etStart);
        etEnd = (EditText) findViewById(R.id.CreateOffer_etEnd);
        imForStartDateCalender = (ImageView) findViewById(R.id.ImOffersStartDateCalender);
        imForEndDateCalender = (ImageView) findViewById(R.id.ImOffersEndDateCalender);
        spOfferRule = (AutoCompleteTextView) findViewById(R.id.CreateOffer_spRuleName_1);
        spOfferRule1 = (AutoCompleteTextView) findViewById(R.id.CreateOffer_spRuleName_2);
        spRuleValue_1 = (AutoCompleteTextView) findViewById(R.id.CreateOffer_spRuleValue_1);
       // spRuleAndOr_1 = (Spinner) findViewById(R.id.CreateOffer_spRuleAndOr_1);
        etRuleValue_1 = (EditText)findViewById(R.id.CreateOffer_etRuleValue_1);
        spActionValue1 = (AutoCompleteTextView) findViewById(R.id.CreateOffer_spActionName_1);
        etActionValue_1 = (EditText) findViewById(R.id.CreateOffer_etActionValue_1);
        saveOffers =(Button)findViewById(R.id.addOffers);
        llActionValue = (LinearLayout)findViewById(R.id.actionValueForPriceType);
        ruleLayOut = (LinearLayout)findViewById(R.id.ruleLayOut);
        actionLayOut = (LinearLayout)findViewById(R.id.actionLayOut);
        selectOfferResourceType = (ImageView) findViewById(R.id.selectOfferResourceType);
        selectOfferProduct = (ImageView) findViewById(R.id.selectOfferProduct);
        selectOfferResourceTypeValue = (ImageView) findViewById(R.id.selectOfferResourceTypeValue);
        selectOfferActionType = (ImageView) findViewById(R.id.selectOfferActionType);

        // get all product
        ProductDBAdapter productDBAdapter = new ProductDBAdapter(this);
        productDBAdapter.open();
        productList=productDBAdapter.getAllProducts();
        OfferDBAdapter offerDBAdapter = new OfferDBAdapter(getApplicationContext());
        offerDBAdapter.open();
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            Product product=null;
            if (extras.containsKey("offerId")) {
                editableOffer = offerDBAdapter.getOfferById(extras.getLong("offerId"));
                Log.d("editableOffer",editableOffer.toString());
                Log.d("editableOffer1",editableOffer.getResourceType().getValue());

                try {
                  if(editableOffer.getResourceType().getValue().equalsIgnoreCase("PRODUCT")) {
                      Log.d("editableOffer22",editableOffer.getResourceId()+"");
                      long a = editableOffer.getResourceId();
                      product = productDBAdapter.getProductByID(a);
                      Log.d("editableOffer",product.toString());
                    }
                    spRuleValue_1.setText(product.getName());
                    etStart.setText(editableOffer.getStartDate()+"");
                    etEnd.setText(editableOffer.getEndDate()+"");
                    etOfferName.setText(editableOffer.getName());
                    saveOffers.setText(getString(R.string.update_offer));
                    spOfferRule.setText(editableOffer.getResourceType().getValue());
                    spOfferRule1.setText("Quantity");
                    String s = editableOffer.getDataAsJson().toString();
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject action = new JSONObject(jsonObject.get("action").toString());
                    JSONObject rules = new JSONObject(jsonObject.get("rules").toString());
                    String actionName = action.getString("name");
                    int quantity = rules.getInt("quantity");
                    etRuleValue_1.setText(quantity+"");
                    spActionValue1.setText(actionName);
                    if(actionName.equals(Action.PRICE_FOR_PRODUCT.getValue())) {
                        llActionValue.setVisibility(View.VISIBLE);
                        etActionValue_1.setText(action.getDouble("value")+"");
                    }

                }
                catch (Exception ex) {
                  Log.d("editableOffer",ex.toString());
                }
            }

            }

        //start date
        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Date date1 = new Date(myCalendar.getTime().getTime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);
                cal.set(Calendar.MILLISECOND, 0);
                startTime= new java.sql.Timestamp(date1.getTime());
                etStart.setText(startTime.toString());
            }

        };
        imForStartDateCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateOfferActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        final Calendar myCalendarEnd = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener endDate1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, monthOfYear);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Date date1 = new Date(myCalendarEnd.getTime().getTime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);
                cal.set(Calendar.MILLISECOND, 0);
                endTime= new java.sql.Timestamp(date1.getTime());
                etEnd.setText(endTime.toString());
            }

        };
        imForEndDateCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateOfferActivity.this, endDate1, myCalendarEnd
                        .get(Calendar.YEAR), myCalendarEnd.get(Calendar.MONTH),
                        myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        offerRuleList = new ArrayList<String>();
        offerRuleList.add("Product");
        offerRuleList.add("Quantity");
        offerRuleList.add("Price");
        offerRuleList1 = new ArrayList<String>();
        offerRuleList1.add("Quantity");
        offerRuleList1.add("Price");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, offerRuleList1);
        actionList = new ArrayList<String>();
        actionList.add(Action.PRICE_FOR_PRODUCT.getValue());
        actionList.add(Action.GET_GIFT_PRODUCT.getValue());
        // Creating adapter for spinner
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, offerRuleList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, offerRuleList1);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spOfferRule.setAdapter(dataAdapter);

        spOfferRule1.setAdapter(dataAdapter1);
        /// offer Rule value
        productNameList = new ArrayList<String>();
        for (int i = 0; i < productList.size(); i++) {
            productNameList.add(productList.get(i).getDisplayName());
        }

        // Creating adapter for spinner
        final ArrayAdapter<String> productDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, productNameList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spRuleValue_1.setAdapter(productDataAdapter);
        //end
        //operation spinner
        operationList = new ArrayList<String>();
        operationList.add(Operation.And.getVal());
        operationList.add(Operation.OR.getVal());
        final ArrayAdapter<String> opDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, operationList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
     //   spRuleAndOr_1.setAdapter(opDataAdapter);
        //end operation
        // action_name region
        final ArrayAdapter<String> actionDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, actionList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spActionValue1.setAdapter(actionDataAdapter);
        //end action_name
        spRuleValue_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spRuleValue_1.showDropDown();
            }
        });

        spRuleValue_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {

                Log.d("product",productList.get(position).toString());
                product_sku =productList.get(position).getSku();
                product_id = productList.get(position).getProductId();
            }
        });

        spOfferRule1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String ruleType = spOfferRule1.getText().toString();
                if(ruleType.equalsIgnoreCase("Quantity")){
                    Log.d("rule",etRuleValue_1.getText().toString()+"");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spActionValue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spActionValue1.showDropDown();
            }
        });
    spActionValue1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {

                String ac = spActionValue1.getText().toString();
                Log.d("rule",ac);
                if(ac.equals("Price for Product")){
                    llActionValue.setVisibility(View.VISIBLE);
                }else if(ac.equals(Action.GET_GIFT_PRODUCT.getValue())){
                    llActionValue.setVisibility(View.GONE);
                }

                if(ac.equals(Action.PRICE_FOR_PRODUCT.getValue())){
                    action_name =Action.PRICE_FOR_PRODUCT.getValue();
                }else if(ac.equals(Action.GET_GIFT_PRODUCT.getValue())){

                    action_name =Action.GET_GIFT_PRODUCT.getValue();
                }
            }
        });

        saveOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OfferDBAdapter offerDBAdapter = new OfferDBAdapter(getApplicationContext());
                offerDBAdapter.open();
                JSONObject data=new JSONObject();
                if(editableOffer==null){
                if(action_name.equalsIgnoreCase(Action.PRICE_FOR_PRODUCT.getValue())){
                    try {
                        data=  makeDataForPriceAndQuantityOffer();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else if(action_name.equalsIgnoreCase("Get gift product")){
                    try {
                        data = makeDataForGiftOffer();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (etOfferName.getText().toString().equals("")) {
                    etOfferName.setBackgroundResource(R.drawable.backtext);
                    Toast.makeText(getApplicationContext(), getString(R.string.insert_offer_name), Toast.LENGTH_LONG).show();
                } else if (etStart.getText().toString().equals("")) {
                    etStart.setBackgroundResource(R.drawable.backtext);
                    Toast.makeText(getApplicationContext(), getString(R.string.determine_start_date), Toast.LENGTH_LONG).show();
                } else if (etEnd.getText().toString().equals("")) {
                    etEnd.setBackgroundResource(R.drawable.backtext);
                    Toast.makeText(getApplicationContext(), getString(R.string.determine_end_date), Toast.LENGTH_LONG).show();
                } else if (spOfferRule.getText().toString().equals("")||spOfferRule1.getText().toString().equals("")||spRuleValue_1.getText().toString().equals("")||etRuleValue_1.getText().toString().equals("")) {
                    ruleLayOut.setBackgroundResource(R.drawable.backtext);
                    Toast.makeText(getApplicationContext(), getString(R.string.please_determine_offer_rule), Toast.LENGTH_LONG).show();
                } else if (spActionValue1.getText().toString().equals("")) {
                    actionLayOut.setBackgroundResource(R.drawable.backtext);
                    Toast.makeText(getApplicationContext(), getString(R.string.please_determine_offer_action), Toast.LENGTH_LONG).show();
                } else {
                    long i = 0;
                    try {
                        i = offerDBAdapter.insertEntry(etOfferName.getText().toString(),"ACTIVE",product_id, ResourceType.PRODUCT,startTime,endTime,data.toString(), SESSION._EMPLOYEE.getEmployeeId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (i > 0) {
                        Toast.makeText(getApplicationContext(), getString(R.string.success_adding_new_offers), Toast.LENGTH_LONG).show();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        onBackPressed();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.can_not_add_offers_please_try_again), Toast.LENGTH_SHORT).show();
                    }
                }
                }else {
                    data=new JSONObject();
                    if(action_name.equalsIgnoreCase(Action.PRICE_FOR_PRODUCT.getValue())){
                        try {
                            data=  makeDataForPriceAndQuantityOffer();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else if(action_name.equalsIgnoreCase(Action.GET_GIFT_PRODUCT.getValue())){
                        try {
                           data=makeDataForGiftOffer();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (etOfferName.getText().toString().equals("")) {
                        etOfferName.setBackgroundResource(R.drawable.backtext);
                        Toast.makeText(getApplicationContext(), getString(R.string.insert_offer_name), Toast.LENGTH_LONG).show();
                    } else if (etStart.getText().toString().equals("")) {
                        etStart.setBackgroundResource(R.drawable.backtext);
                        Toast.makeText(getApplicationContext(), getString(R.string.determine_start_date), Toast.LENGTH_LONG).show();
                    } else if (etEnd.getText().toString().equals("")) {
                        etEnd.setBackgroundResource(R.drawable.backtext);
                        Toast.makeText(getApplicationContext(), getString(R.string.determine_end_date), Toast.LENGTH_LONG).show();
                    }else if (spOfferRule.getText().toString().equals("")||spOfferRule1.getText().toString().equals("")||spRuleValue_1.getText().toString().equals("")||etRuleValue_1.getText().toString().equals("")) {
                        ruleLayOut.setBackgroundResource(R.drawable.backtext);
                        Toast.makeText(getApplicationContext(), getString(R.string.please_determine_offer_rule), Toast.LENGTH_LONG).show();
                    } else if (spActionValue1.getText().toString().equals("")) {
                        actionLayOut.setBackgroundResource(R.drawable.backtext);
                        Toast.makeText(getApplicationContext(), getString(R.string.please_determine_offer_action), Toast.LENGTH_LONG).show();
                    }  else {
                        if(startTime== null){
                            startTime = editableOffer.getStartDate();
                        }
                        if(endTime== null){
                            endTime = editableOffer.getEndDate();
                        }
                        if(data.length()!=0){
                            editableOffer.setOfferData(data.toString());

                        }
                        editableOffer.setName(etOfferName.getText().toString());
                        editableOffer.setStartDate(startTime);
                        editableOffer.setEndDate(endTime);
                        editableOffer.setResourceId(product_id);
                        try {
                            offerDBAdapter.updateEntry(editableOffer);
                            Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.success_to_update_offer), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } catch (Exception ex) {
                            onBackPressed();
                            Toast.makeText(getBaseContext(), getBaseContext().getString(R.string.error_to_update_offer), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });

        etRuleValue_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etRuleValue_1.getText().toString().equals("")) {
                    quantity=Integer.parseInt(etRuleValue_1.getText().toString());
                }
            }
        });
        etActionValue_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etActionValue_1.getText().toString().equals("")) {
                    priceForProduct=Double.parseDouble(etActionValue_1.getText().toString());
                }
            }
        });
        selectOfferResourceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             spOfferRule.showDropDown();
            }
        });
        selectOfferProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spRuleValue_1.showDropDown();
            }
        });
        selectOfferResourceTypeValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spOfferRule1.showDropDown();
            }
        });
        selectOfferActionType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spActionValue1.showDropDown();

            }
        });

    }
    private JSONObject makeDataForPriceAndQuantityOffer() throws JSONException {
        JSONObject rules = new JSONObject();
        rules.put(Rules.product_sku.getValue(),product_sku);
        rules.put(Rules.quantity.getValue(),quantity);
        JSONObject action = new JSONObject();
        action.put(Action.NAME.getValue(),action_name);
        action.put(Action.VALUE.getValue(),priceForProduct);
        JSONObject data = new JSONObject();
        data.put("rules",rules);
        data.put("action",action);
        return data;
    }
    private JSONObject makeDataForGiftOffer() throws JSONException {
        JSONObject rules = new JSONObject();
        rules.put(Rules.product_sku.getValue(),product_sku);
        rules.put(Rules.quantity.getValue(),quantity);
        JSONObject action = new JSONObject();
        action.put(Action.NAME.getValue(),action_name);
        JSONObject data = new JSONObject();
        data.put("rules",rules);
        data.put("action",action);
        return data;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
