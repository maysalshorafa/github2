package com.pos.leaders.leaderspossystem.CustomerAndClub;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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

import com.itextpdf.text.DocumentException;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CityDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClubAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.Models.City;
import com.pos.leaders.leaderspossystem.Models.Club;
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.Models.Wallet;
import com.pos.leaders.leaderspossystem.Models.WalletStatus;
import com.pos.leaders.leaderspossystem.PdfUA;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import net.sf.andpdf.nio.ByteBuffer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class AddNewCustomer extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String street="" , job="" , email="" , houseNo="" , postalCode="" , country="" , countryCode="";
    int cityId=0;
    EditText etCustomerFirstName, etCustomerLastName, etStreet, etJob, etEmail, etPhoneNo, etHouseNumber, etPostalCode, etCountry, etCountryCode ,etCustomerCredit;
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
    TextView advance ,tvCustomerBalance;
    LinearLayout CustomerBalance ;
    public static Context context = null;
    Bitmap page=null ;
    public static final String SAMPLE_FILE = "customerwallet.pdf";
    ArrayList<Bitmap> bitmapList=new ArrayList<Bitmap>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_new_coustmer);
        TitleBar.setTitleBar(this);
        init();
        context=this;
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
            etCustomerCredit.setEnabled(false);
            CustomerBalance.setVisibility(View.VISIBLE);
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
            etCustomerCredit.setText(customer.getCredit()+"");
            btAddCustomer.setText(getResources().getText(R.string.edit));
            tvCustomerBalance.setText(customer.getBalance()+"");

            if(secondCustomerInformation.getVisibility()== View.VISIBLE) {
                if (customer.getGender().equalsIgnoreCase(getString(R.string.male))) {
                    radioGender.check(R.id.male);
                } else if (customer.getGender().equalsIgnoreCase(getString(R.string.female))) {
                    radioGender.check(R.id.female);
                }

                for (int i = 0; i < cityList.size(); i++) {
                    City city = cityList.get(i);
                    if (city.getCityId() == customer.getCity()) {
                        selectCitySpinner.setSelection(i);

                    }
                }
            }
            for (int i = 0; i < groupList.size(); i++) {
                Club group = groupList.get(i);
                if (group.getClubId() == customer.getClub()) {
                    selectClubSpinner.setSelection(i);

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
                secondCustomerInformation.setVisibility(View.VISIBLE);
                advanceFeature.setVisibility(View.INVISIBLE);
                advance.setVisibility(View.INVISIBLE);
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
                                etCustomerFirstName.setBackgroundResource(R.drawable.backtext);
                                Toast.makeText(getApplicationContext(), getString(R.string.please_insert_first_name), Toast.LENGTH_LONG).show();
                            } else if (etCustomerLastName.getText().toString().equals("")) {
                                etCustomerLastName.setBackgroundResource(R.drawable.backtext);
                                Toast.makeText(getApplicationContext(), getString(R.string.please_insert_last_name), Toast.LENGTH_LONG).show();
                            } else if (etPhoneNo.getText().toString().equals("")) {
                                etPhoneNo.setBackgroundResource(R.drawable.backtext);
                                Toast.makeText(getApplicationContext(), getString(R.string.please_insert_phone_no), Toast.LENGTH_LONG).show();
                            }  else if (!customerDBAdapter.availableCustomerPhoneNo(etPhoneNo.getText().toString())) {
                                etPhoneNo.setBackgroundResource(R.drawable.backtext);
                                Toast.makeText(getApplicationContext(), getString(R.string.please_insert_phone_no), Toast.LENGTH_LONG).show();
                            } else if (etCustomerCredit.getText().toString().equals("")) {
                                etCustomerCredit.setBackgroundResource(R.drawable.backtext);
                            Toast.makeText(getApplicationContext(), getString(R.string.please_insert_customer_credit), Toast.LENGTH_LONG).show();
                        }else {
                                long i = 0;
                                try {
                                    i = customerDBAdapter.insertEntry(etCustomerFirstName.getText().toString(),
                                            etCustomerLastName.getText().toString(), gender, email, job, etPhoneNo.getText().toString(), street, cityId, clubID, houseNo, etPostalCode.getText().toString(),
                                           country, countryCode,0,Double.parseDouble(etCustomerCredit.getText().toString()));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (i > 0) {
                                    Customer customer = customerDBAdapter.getCustomerByID(i);
                                    Wallet wallet = new Wallet(WalletStatus.ACTIVE,Double.parseDouble(etCustomerCredit.getText().toString()),i);
                                    StartConnection startConnection = new StartConnection();
                                    startConnection.execute(customer.toString(),wallet.toString());
                                    Toast.makeText(getApplicationContext(), getString(R.string.success_adding_new_customer), Toast.LENGTH_LONG).show();
                                    try
                                    {
                                        File path = new File( Environment.getExternalStorageDirectory(), getPackageName() );
                                        File file = new File(path,SAMPLE_FILE);
                                        RandomAccessFile f = new RandomAccessFile(file, "r");
                                        byte[] data = new byte[(int)f.length()];
                                        f.readFully(data);
                                        pdfLoadImages(data);
                                        //pdfLoadImages1(data);
                                    }
                                    catch(Exception ignored)
                                    {
                                    }
                                    try {
                                        Thread.sleep(500);
                                      //  print(page);

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
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
                            etCustomerFirstName.setBackgroundResource(R.drawable.backtext);
                            Toast.makeText(getApplicationContext(), getString(R.string.please_insert_first_name), Toast.LENGTH_LONG).show();
                        } else if (etCustomerLastName.getText().toString().equals("")) {
                            etCustomerLastName.setBackgroundResource(R.drawable.backtext);
                            Toast.makeText(getApplicationContext(), getString(R.string.please_insert_last_name), Toast.LENGTH_LONG).show();
                        } else if (etPhoneNo.getText().toString().equals("")) {
                            etCustomerLastName.setBackgroundResource(R.drawable.backtext);
                            Toast.makeText(getApplicationContext(), getString(R.string.please_insert_phone_no), Toast.LENGTH_LONG).show();
                        }  else if (etCustomerCredit.getText().toString().equals("")) {
                            etCustomerCredit.setBackgroundResource(R.drawable.backtext);
                            Toast.makeText(getApplicationContext(), getString(R.string.please_insert_customer_credit), Toast.LENGTH_LONG).show();
                        }else {
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
                                customer.setCredit(Double.parseDouble(etCustomerCredit.getText().toString()));
                                customerDBAdapter.updateEntry(customer);
                                Toast.makeText(getApplicationContext(), getString(R.string.success_edit_customer), Toast.LENGTH_SHORT).show();

                                Log.i("success Edit", customer.toString());
                                Customer customer = customerDBAdapter.getCustomerByID(customerId);
                                Wallet wallet = new Wallet(WalletStatus.ACTIVE,Double.parseDouble(etCustomerCredit.getText().toString()),customerId);
                                UpdateCustomerAndWalletStartConnection startConnection = new UpdateCustomerAndWalletStartConnection();
                                startConnection.execute(customer.toString(),wallet.toString(),String.valueOf(customerId));
                                try
                                {
                                    File path = new File( Environment.getExternalStorageDirectory(), getPackageName() );
                                    File file = new File(path,SAMPLE_FILE);
                                    RandomAccessFile f = new RandomAccessFile(file, "r");
                                    byte[] data = new byte[(int)f.length()];
                                    f.readFully(data);
                                    pdfLoadImages(data);


                                    //pdfLoadImages1(data);
                                }
                                catch(Exception ignored)
                                {
                                }
                                try {
                                    Thread.sleep(500);
                                    //print(page);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }                            }
                            catch (Exception ex) {
                                Log.d("exset",ex.toString());
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
        etCustomerCredit = (EditText)findViewById(R.id.etCustomerCredit);
        btAddCustomer = (Button) findViewById(R.id.add_Custmer);
        btCancel = (Button) findViewById(R.id.addCustmer_BTCancel);
        advanceFeature=(ImageView)findViewById(R.id.advanceFeature);
        advance=(TextView)findViewById(R.id.advance);
        customerDBAdapter = new CustomerDBAdapter(this);
        customerDBAdapter.open();
        selectCitySpinner = (Spinner) findViewById(R.id.customerCitySpinner);
        selectClubSpinner = (Spinner) findViewById(R.id.customerClubSpinner);
        CustomerBalance = (LinearLayout)findViewById(R.id.CustomerBalance);
        tvCustomerBalance = (TextView)findViewById(R.id.addNewCustomerBalanceValue);
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
    private void pdfLoadImages(final byte[] data) {
        bitmapList=new ArrayList<Bitmap>();
        try {
            // run async
            new AsyncTask<Void, Void, String>() {
                // create and show a progress dialog
                ProgressDialog progressDialog = ProgressDialog.show(AddNewCustomer.this, "", "Opening...");

                @Override
                protected void onPostExecute(String html) {
                    print();
                    onBackPressed();
                    //after async close progress dialog
                    progressDialog.dismiss();
                    //load the html in the webview
                    //	wv1.loadDataWithBaseURL("", html, "randompdf/html", "UTF-8", "");
                }

                @Override
                protected String doInBackground(Void... params) {
                    try {
                        //create pdf document object from bytes
                        ByteBuffer bb = ByteBuffer.NEW(data);
                        PDFFile pdf = new PDFFile(bb);
                        //Get the first page from the pdf doc
                        PDFPage PDFpage = pdf.getPage(1, true);
                        //create a scaling value according to the WebView Width
                        final float scale = 800 / PDFpage.getWidth() * 0.80f;
                        //convert the page into a bitmap with a scaling value
                        page = PDFpage.getImage((int) (PDFpage.getWidth() * scale), (int) (PDFpage.getHeight() * scale), null, true, true);
                        //save the bitmap to a byte array
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        page.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        stream.reset();
                        //convert the byte array to a base64 string
                        String base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        //create the html + add the first image to the html
                        String html = "<!DOCTYPE html><html><body bgcolor=\"#ffffff\"><img src=\"data:image/png;base64," + base64 + "\" hspace=328 vspace=4><br>";
                        //loop though the rest of the pages and repeat the above
                        for (int i = 0; i <= pdf.getNumPages(); i++) {
                            PDFpage = pdf.getPage(i, true);
                            page = PDFpage.getImage((int) (PDFpage.getWidth() * scale), (int) (PDFpage.getHeight() * scale), null, true, true);
                            page.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            bitmapList.add(page);
                            byteArray = stream.toByteArray();
                            stream.reset();
                            base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                            html += "<img src=\"data:image/png;base64," + base64 + "\" hspace=10 vspace=10><br>";

                        }

                        stream.close();
                        html += "</body></html>";
                        return html;
                    } catch (Exception e) {
                        Log.d("error", e.toString());
                    }
                    return null;
                }
            }.execute();
            System.gc();// run GC
        } catch (Exception e) {
            Log.d("error", e.toString());
        }
    }
public  void print(){
            PrintTools pt=new PrintTools(AddNewCustomer.this);
            for (int i= 1;i<bitmapList.size(); i++) {
                Log.d("bitmapsize",bitmapList.size()+"");
                pt.PrintReport(bitmapList.get(i));

            }

}

}
class StartConnection extends AsyncTask<String,Void,String> {
    private MessageTransmit messageTransmit;

    StartConnection() {
        messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
    }

    final ProgressDialog progressDialog = new ProgressDialog(AddNewCustomer.context);
    final ProgressDialog progressDialog2 =new ProgressDialog(AddNewCustomer.context);

    @Override
    protected void onPreExecute() {
        progressDialog.setTitle("Please Wait");
        progressDialog2.setTitle("Please Wait");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {//args{key,uuid}
        String customer = args[0];
        String wallet = args[1];

        String initRes = "";
        String customerRes = "";

        try {
            customerRes = messageTransmit.authPost(ApiURL.Customer, customer, SESSION.token);
            JSONObject customerJson = new JSONObject(customerRes);
            if (customerJson.getInt(MessageKey.status) == 200) {
                initRes = messageTransmit.authPost(ApiURL.Wallet, wallet,SESSION.token);
                Log.e("messageResult", initRes);
                JSONObject jsonObject = new JSONObject(initRes);
                if (jsonObject.getInt(MessageKey.status) == 200) {
                    JSONObject responseBody = jsonObject.getJSONObject(MessageKey.responseBody);
                    Log.d("CustomerReciptResult",responseBody.toString());
                    try {
                        PdfUA pdfUA = new PdfUA();

                        pdfUA.printCustomerWalletReport(AddNewCustomer.context,responseBody.toString());

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onPostExecute(String s) {

        //region rr
        //the async task is finished
        if (s != null) {
            //success

            final String token = SESSION.token;

            //null response
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog2.setTitle("Success.");
                   progressDialog2.show();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    progressDialog2.cancel();
                    /**  SetupNewPOSOnlineActivity.restart = true;
                     final Intent i = new Intent(SetupNewPOSOnlineActivity.context, SplashScreenActivity.class);
                     i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                     SetupNewPOSOnlineActivity.context.startActivity(i);
                     **/
                    super.onPostExecute(aVoid);
                }
            }.execute();
        } else {
            //fail
            Toast.makeText(AddNewCustomer.context, "Try Again.", Toast.LENGTH_SHORT).show();
        }
       progressDialog.cancel();
        super.onPostExecute(s);

        //endregion
    }

}
class UpdateCustomerAndWalletStartConnection extends AsyncTask<String,Void,String> {
    private MessageTransmit messageTransmit;

    UpdateCustomerAndWalletStartConnection() {
        messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
    }

    final ProgressDialog progressDialog = new ProgressDialog(AddNewCustomer.context);
    final ProgressDialog progressDialog2 =new ProgressDialog(AddNewCustomer.context);

    @Override
    protected void onPreExecute() {
        progressDialog.setTitle("Please Wait");
        progressDialog2.setTitle("Please Wait");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {//args{key,uuid}
        String customer = args[0];
        String wallet = args[1];
        String customerId = args[2];


        String initRes = "";
        String customerRes = "";

        try {
            customerRes = messageTransmit.authPut(ApiURL.Customer, customer, SESSION.token,Long.parseLong(customerId));
            JSONObject customerJson = new JSONObject(customerRes);
            if (customerJson.getInt(MessageKey.status) == 200) {
                initRes = messageTransmit.authPut(ApiURL.Wallet, wallet,SESSION.token,Long.parseLong(customerId));
                Log.e("messageResult", initRes);
                JSONObject jsonObject = new JSONObject(initRes);
                if (jsonObject.getInt(MessageKey.status) == 200) {
                    JSONObject responseBody = jsonObject.getJSONObject(MessageKey.responseBody);
                    Log.d("CustomerReciptResult",responseBody.toString());
                    try {
                        PdfUA pdfUA = new PdfUA();

                        pdfUA.printCustomerWalletReport(AddNewCustomer.context,responseBody.toString());

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onPostExecute(String s) {

        //region rr
        //the async task is finished
        if (s != null) {
            //success

            final String token = SESSION.token;

            //null response
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog2.setTitle("Success.");
                    progressDialog2.show();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    progressDialog2.cancel();
                    /**  SetupNewPOSOnlineActivity.restart = true;
                     final Intent i = new Intent(SetupNewPOSOnlineActivity.context, SplashScreenActivity.class);
                     i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                     SetupNewPOSOnlineActivity.context.startActivity(i);
                     **/
                    super.onPostExecute(aVoid);
                }
            }.execute();
        } else {
            //fail
            Toast.makeText(AddNewCustomer.context, "Try Again.", Toast.LENGTH_SHORT).show();
        }
        progressDialog.cancel();
        super.onPostExecute(s);

        //endregion
    }

}

