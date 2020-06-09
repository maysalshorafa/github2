package com.pos.leaders.leaderspossystem;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.Tools.CustomerCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.InvoiceManagementListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceManagementActivity extends AppCompatActivity {
    public static double debtAmount=0 , payAmount=0;
    public static ListView invoiceListView;
     EditText customer_id ;
    public static  GridView gvCustomer ;
   boolean userScrolled =false;
    public static  Customer customer;
    List<Customer> customerList = new ArrayList<>();
    List<Customer> AllCustmerList = new ArrayList<>();
    public static Context context = null;
    public static List<BoInvoice>invoiceList=new ArrayList<>();
    public static ArrayList<String>invoiceNumberList=new ArrayList<>();
    View previousView = null;
    public static  List<String>orderIds=new ArrayList<>();
    public static  LinearLayout customerLayOut;
    List<Long>invoiceNumberWantToPay=new ArrayList<>();
    Button btnOk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_invoice_management);
        TitleBar.setTitleBar(this);
        CustomerDBAdapter customerDBAdapter =new CustomerDBAdapter(this);
        customerDBAdapter.open();
        Log.d("token",SESSION.token+"");
        context=this;
        Bundle bundle = getIntent().getExtras();

        invoiceListView = (ListView)findViewById(R.id.invoiceManagement_LVInvoice);
        customer_id = (EditText) findViewById(R.id.customer_name);
        gvCustomer = (GridView) findViewById(R.id.popUp_gvCustomer);
        customerLayOut =(LinearLayout)findViewById(R.id.invoiceManagement_LVCustomerLayout);
        btnOk=(Button)findViewById(R.id.reciptPaymentInvoiceActivity);
        payAmount=0;
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0;i<InvoiceManagementListViewAdapter.mCheckStates.size();i++)
                {
                    if(InvoiceManagementListViewAdapter.mCheckStates.get(i)==true)
                    {
                        try {
                            payAmount+=Double.parseDouble(invoiceList.get(i).getDocumentsData().getString("total"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("teeeees",payAmount+"");

                    }
                }
                for(int i=0;i<InvoiceManagementListViewAdapter.mCheckStatesPartial.size();i++)
                {
                    if(InvoiceManagementListViewAdapter.mCheckStatesPartial.get(i)==true)
                    {
                            payAmount+=Double.parseDouble(InvoiceManagementListViewAdapter.partialValue.get(i).toString());
                        Log.d("teeeees",payAmount+"");

                    }
                }
            }
        });



        invoiceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                LinearLayout fr = (LinearLayout) view.findViewById(R.id.listInvoiceManagement_FLMore);
                if (previousView == null) {
                    fr.setVisibility(View.VISIBLE);
                    previousView = view;
                } else {
                    fr.setVisibility(View.VISIBLE);
                    previousView.findViewById(R.id.listInvoiceManagement_FLMore).setVisibility(View.GONE);
                    previousView.setBackgroundColor(getResources().getColor(R.color.transparent));
                    previousView = view;
                }
                previousView.setBackgroundColor(getResources().getColor(R.color.list_background_color));

            }});
        gvCustomer.setNumColumns(3);

        customer_id.setText("");
        customer_id.setHint("Search..");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        gvCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        gvCustomer.setOnScrollListener(new AbsListView.OnScrollListener() {
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

        customerList = customerDBAdapter.getAllCustomer();
        AllCustmerList = customerList;

        CustomerCatalogGridViewAdapter custmerCatalogGridViewAdapter = new CustomerCatalogGridViewAdapter(getApplicationContext(), customerList);

        gvCustomer.setAdapter(custmerCatalogGridViewAdapter);

        gvCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                customer= customerList.get(position);
                if(customer!=null) {
                    StartInvoiceConnectionGetCustomerDebt startInvoiceConnectionDebtAmount = new StartInvoiceConnectionGetCustomerDebt();
                    startInvoiceConnectionDebtAmount.execute(String.valueOf(customer.getCustomerId()));
                    final Dialog paymentReceiptDialog = new Dialog(InvoiceManagementActivity.this);
                    paymentReceiptDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    paymentReceiptDialog.show();
                    paymentReceiptDialog.setContentView(R.layout.recipt_cash_dialog);
                    final EditText etAmount = (EditText) paymentReceiptDialog.findViewById(R.id.recipthPaymentDialog_TECash);
                    final TextView tvDebtAmount = (TextView) paymentReceiptDialog.findViewById(R.id.TvTotalAmountDebt);
                    tvDebtAmount.setText(Util.makePrice(debtAmount));
                    final TextView totalAmountAfterPay = (TextView) paymentReceiptDialog.findViewById(R.id.totalAmountAfterPay);
                    Button btnOk = (Button)paymentReceiptDialog.findViewById(R.id.reciptPaymentDialog_BTOk);

                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String checkStr = etAmount.getText().toString();
                            if(!checkStr.matches("")){
                                StartInvoiceConnection startInvoiceConnection = new StartInvoiceConnection();
                                startInvoiceConnection.execute(String.valueOf(customer.getCustomerId()));
                                paymentReceiptDialog.dismiss();
                            }

                        }
                    });


                    etAmount.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String checkStr = etAmount.getText().toString();
                            if(!checkStr.matches("")){
                                double total=debtAmount-Double.parseDouble(etAmount.getText().toString());
                                totalAmountAfterPay.setText(Util.makePrice(total));
                            }
                        }
                    });

                }
            }
        });

        customer_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                gvCustomer.setTextFilterEnabled(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                customerList = new ArrayList<Customer>();
                String word = customer_id.getText().toString();

                if (!word.equals("")) {
                    for (Customer c : AllCustmerList) {

                        if (c.getFirstName().toLowerCase().contains(word.toLowerCase()) ||
                                c.getLastName().toLowerCase().contains(word.toLowerCase())||
                                c.getCustomerIdentity().toLowerCase().contains(word.toLowerCase())||
                                c.getPhoneNumber().toLowerCase().contains(word.toLowerCase())) {
                            customerList.add(c);

                        }
                    }
                } else {
                    customerList = AllCustmerList;
                }
                CustomerCatalogGridViewAdapter adapter = new CustomerCatalogGridViewAdapter(getApplicationContext(), customerList);
                gvCustomer.setAdapter(adapter);
                // Log.i("products", productList.toString());


            }
        });



    }
    public void saveInvoicePos(BoInvoice boInvoice){
       invoiceList.add(boInvoice);
    }
    //choose customer invoice
    private void callCustomerDialog() {
        orderIds=new ArrayList<>();
        CustomerDBAdapter customerDBAdapter =new CustomerDBAdapter(this);
        customerDBAdapter.open();
        final Dialog customerDialog = new Dialog(InvoiceManagementActivity.this);
        customerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customerDialog.show();
        customerDialog.setContentView(R.layout.pop_up);
        final EditText customer_id = (EditText) customerDialog.findViewById(R.id.customer_name);
        final GridView gvCustomer = (GridView) customerDialog.findViewById(R.id.popUp_gvCustomer);
        gvCustomer.setNumColumns(3);

       Button btn_cancel = (Button) customerDialog.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerDialog.dismiss();
            }
        });

        customer_id.setText("");
        customer_id.setHint("Search..");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        gvCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        gvCustomer.setOnScrollListener(new AbsListView.OnScrollListener() {
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

        customerList = customerDBAdapter.getAllCustomer();
        AllCustmerList = customerList;

        CustomerCatalogGridViewAdapter custmerCatalogGridViewAdapter = new CustomerCatalogGridViewAdapter(getApplicationContext(), customerList);

        gvCustomer.setAdapter(custmerCatalogGridViewAdapter);

        gvCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               customer= customerList.get(position);
                if(customer!=null) {
                    StartInvoiceConnection startInvoiceConnection = new StartInvoiceConnection();
                    startInvoiceConnection.execute(String.valueOf(customer.getCustomerId()));
                }
                customerDialog.dismiss();
            }
        });

        customer_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                gvCustomer.setTextFilterEnabled(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                customerList = new ArrayList<Customer>();
                String word = customer_id.getText().toString();

                if (!word.equals("")) {
                    for (Customer c : AllCustmerList) {

                        if (c.getFirstName().toLowerCase().contains(word.toLowerCase()) ||
                                c.getLastName().toLowerCase().contains(word.toLowerCase())||
                                c.getCustomerIdentity().toLowerCase().contains(word.toLowerCase())||
                                c.getPhoneNumber().toLowerCase().contains(word.toLowerCase())) {
                            customerList.add(c);

                        }
                    }
                } else {
                    customerList = AllCustmerList;
                }
                CustomerCatalogGridViewAdapter adapter = new CustomerCatalogGridViewAdapter(getApplicationContext(), customerList);
                gvCustomer.setAdapter(adapter);
                // Log.i("products", productList.toString());


            }
        });


    }

}
class StartInvoiceConnection extends AsyncTask<String,Void,String> {
    private MessageTransmit messageTransmit;
    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    BoInvoice invoice;
    StartInvoiceConnection() {
        messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
    }

    final ProgressDialog progressDialog = new ProgressDialog(InvoiceManagementActivity.context);
    final ProgressDialog progressDialog2 =new ProgressDialog(InvoiceManagementActivity.context);

    @Override
    protected void onPreExecute() {
        InvoiceManagementActivity.invoiceList=new ArrayList<>();
        InvoiceManagementActivity.invoiceNumberList=new ArrayList<>();
        progressDialog.setTitle("Please Wait");
        progressDialog2.setTitle("Please Wait");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {//args{key,uuid}
        String customerId=args[0];
        try {
            String url = ApiURL.Documents+"/unpaidInvoicesForCustomer/"+customerId;
         //   Log.d("invoiceUrl",url);

           String invoiceRes = messageTransmit.authGet(url,SESSION.token);
        // Log.d("invoiceRes",invoiceRes);
            JSONObject jsonObject = new JSONObject(invoiceRes);
            String msgData = jsonObject.getString(MessageKey.responseBody);
            if (msgData.startsWith("[")) {

                try {
                    JSONArray jsonArray = new JSONArray(msgData);
                    for (int i = 0; i <= jsonArray.length()-1 ; i++) {
                        msgData = jsonArray.getJSONObject(i).toString();
                        JSONObject msgDataJson =new JSONObject(msgData);
                        InvoiceManagementActivity.invoiceNumberList.add(msgDataJson.getString("docNum"));
                        invoice = new BoInvoice(DocumentType.INVOICE,msgDataJson.getJSONObject("documentsData"),msgDataJson.getString("docNum"));
                        InvoiceManagementActivity.invoiceList.add(invoice);
                    }
                    Log.d("invoices",InvoiceManagementActivity.invoiceList.toString()+"");
                    Log.d("invoicesOrderId",InvoiceManagementActivity.orderIds.toString());

                } catch (Exception e) {
                    Log.d("exception1",e.toString());
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog2.setTitle("Success.");
                    progressDialog2.show();
                    InvoiceManagementActivity.customerLayOut.setVisibility(View.GONE);
                    InvoiceManagementListViewAdapter invoiceManagementListViewAdapter = new InvoiceManagementListViewAdapter(InvoiceManagementActivity.context,R.layout.list_adapter_row_invoices_management,InvoiceManagementActivity.invoiceList,InvoiceManagementActivity.invoiceNumberList);
                    InvoiceManagementActivity.invoiceListView.setAdapter(invoiceManagementListViewAdapter);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    progressDialog2.cancel();
                    super.onPostExecute(aVoid);
                }
            }.execute();
        } else {
            //fail
            Toast.makeText(InvoiceManagementActivity.context, "Try Again.", Toast.LENGTH_SHORT).show();
        }
        progressDialog.cancel();
        super.onPostExecute(s);

        //endregion
    }

}
class StartInvoiceConnectionGetCustomerDebt extends AsyncTask<String,Void,String> {
    private MessageTransmit messageTransmit;
    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    BoInvoice invoice;
    StartInvoiceConnectionGetCustomerDebt() {
        messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
    }

    final ProgressDialog progressDialog = new ProgressDialog(InvoiceManagementActivity.context);
    final ProgressDialog progressDialog2 =new ProgressDialog(InvoiceManagementActivity.context);

    @Override
    protected void onPreExecute() {
        progressDialog.setTitle("Please Wait");
        progressDialog2.setTitle("Please Wait");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {//args{key,uuid}
        String customerId=args[0];
        try {
            String url = ApiURL.Documents+"/totalUnPaidForCustomer/"+customerId;
            //   Log.d("invoiceUrl",url);

            String invoiceRes = messageTransmit.authGet(url,SESSION.token);
           Log.d("invoiceRes",invoiceRes);
            JSONObject jsonObject = new JSONObject(invoiceRes);
            String msgData = jsonObject.getString(MessageKey.responseBody);
            InvoiceManagementActivity.debtAmount=Double.parseDouble(msgData);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null) {
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
                    super.onPostExecute(aVoid);
                }
            }.execute();
        } else {
            //fail
            Toast.makeText(InvoiceManagementActivity.context, "Try Again.", Toast.LENGTH_SHORT).show();
        }
        progressDialog.cancel();
        super.onPostExecute(s);

        //endregion
    }

}

