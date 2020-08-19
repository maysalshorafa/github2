package com.pos.leaders.leaderspossystem;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.Models.InvoiceStatus;
import com.pos.leaders.leaderspossystem.Models.ReciptObject;
import com.pos.leaders.leaderspossystem.Payment.MultiCurrenciesPaymentActivity;
import com.pos.leaders.leaderspossystem.Tools.CustomerCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.ReciptManagementListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.ThisApp;
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

import static com.pos.leaders.leaderspossystem.SalesCartActivity.REQUEST_RECIPT_ACTIVITY_CODE;

public class InvoiceManagementActivity extends AppCompatActivity {
    public static double debtAmount=0  ,pay=0, totalAmount=0;
    public static ListView invoiceListView;
     EditText customer_id ;
    public static  GridView gvCustomer ;
   boolean userScrolled =false;
    public static  Customer customer;
    List<Customer> customerList = new ArrayList<>();
    List<Customer> AllCustmerList = new ArrayList<>();
    public static Context context = null;
    public static List<BoInvoice>invoiceList=new ArrayList<>();
    public  static List<ReciptObject>reciptObjectList=new ArrayList<>();
    public  static List<String> partiallyString =new ArrayList<>();

    public static ArrayList<String>invoiceNumberList=new ArrayList<>();
    View previousView = null;
    public static  List<String>orderIds=new ArrayList<>();
    public static  LinearLayout customerLayOut;
    List<Long>invoiceNumberWantToPay=new ArrayList<>();
    TextView payAmount ,RequiredAmount;
    public static boolean CheckAll=false;
    Button btnOk , addRecipt,btnCancel,btnCheckAllRecipt;
    public static  List<BoInvoice>newInvoiceList=new ArrayList<BoInvoice>();
    public static  ReciptManagementListViewAdapter adapter;
    int count=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.recipt_mangment_activity);
        TitleBar.setTitleBar(this);
        CustomerDBAdapter customerDBAdapter =new CustomerDBAdapter(this);
        customerDBAdapter.open();
        Log.d("token",SESSION.token+"");
        context=this;
        ThisApp.setCurrentActivity(this);
        Bundle bundle = getIntent().getExtras();
        LayoutInflater inflater = getLayoutInflater();
        invoiceListView = (ListView) findViewById(R.id.invoiceManagement_LVInvoice);
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.list_adapter_head_row_invoice_management, invoiceListView, false);
        invoiceListView.addHeaderView(header, null, false);
        customer_id = (EditText) findViewById(R.id.customer_name);
        gvCustomer = (GridView) findViewById(R.id.popUp_gvCustomer);
        customerLayOut =(LinearLayout)findViewById(R.id.invoiceManagement_LVCustomerLayout);
        btnOk=(Button)findViewById(R.id.reciptPaymentInvoiceActivity);
        btnCancel=(Button)findViewById(R.id.reciptPaymentInvoiceCancelActivity);
        payAmount=(TextView)findViewById(R.id.payAmount);
        RequiredAmount=(TextView)findViewById(R.id.requoredAmount);
        invoiceList=new ArrayList<>();
        invoiceNumberList=new ArrayList<>();
        partiallyString=new ArrayList<>();
        reciptObjectList=new ArrayList<>();
        btnCheckAllRecipt=(Button) findViewById(R.id.checkAllRecipt);
        btnCheckAllRecipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newInvoiceList=new ArrayList<BoInvoice>();

                for (int i=0;i<InvoiceManagementActivity.reciptObjectList.size();i++){
                    JSONObject jsonObject = invoiceList.get(i).getDocumentsData();
                    InvoiceManagementActivity.reciptObjectList.get(i).setAll(true);
                    try {
                        InvoiceManagementActivity.reciptObjectList.get(i).setAllAmount(jsonObject.getDouble("balanceDue"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                ReciptManagementListViewAdapter invoiceManagementListViewAdapter = new ReciptManagementListViewAdapter(InvoiceManagementActivity.context, R.layout.list_adapter_row_recipt, InvoiceManagementActivity.invoiceList, InvoiceManagementActivity.invoiceNumberList, InvoiceManagementActivity.reciptObjectList, InvoiceManagementActivity.partiallyString);
                invoiceListView.setAdapter(invoiceManagementListViewAdapter);


                for(int i=0;i<InvoiceManagementActivity.reciptObjectList.size();i++) {
                    ReciptObject reciptObject = InvoiceManagementActivity.reciptObjectList.get(i);
                    BoInvoice boInvoice = invoiceList.get(i);
                    JSONObject jsonObject =boInvoice.getDocumentsData();

                    if(reciptObject.isAll()){
                        try {
                            jsonObject.remove("invoiceStatus");
                            jsonObject.put("invoiceStatus", InvoiceStatus.PAID.getValue());
                            jsonObject.put("balanceDue",0);
                            jsonObject.remove("totalPaid");
                            jsonObject.put("totalPaid",reciptObject.getAllAmount());
                            boInvoice.setDocumentsData(jsonObject);
                            newInvoiceList.add(boInvoice);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        totalAmount+=reciptObject.getAllAmount();
                    }
                }
                RequiredAmount.setText(Util.makePrice(totalAmount-pay));
                Log.d("totalAmount111",totalAmount+"    "+ pay);

                if(totalAmount!=pay){
                    Toast.makeText(InvoiceManagementActivity.this,"insert the required amount",Toast.LENGTH_LONG).show();
                }else {
                    Intent intent=new Intent(InvoiceManagementActivity.this,MultiCurrenciesPaymentActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("com_pos_leaders_cart_total_price",totalAmount);
                    intent.putExtra("Receipt","ReceiptWay");
                    intent.putExtra("customer",customer.toString());
                    startActivityForResult(intent, REQUEST_RECIPT_ACTIVITY_CODE);
                    //v.getContext().startActivity(intent);


                }

            }

        });



        addRecipt=(Button)findViewById(R.id.addRecipt);
        addRecipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v1) {
                final Dialog customerRecpDialog = new Dialog(context);
                customerRecpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customerRecpDialog.show();
                customerRecpDialog.setContentView(R.layout.recipt_dialog_layout);
                final EditText   customer_amount = (EditText) customerRecpDialog.findViewById(R.id.customer_email);
                final  Button button = (Button)customerRecpDialog.findViewById(R.id.done);
                button.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {
                        if(customer_amount.getText().toString()!=""){
                            double amount =Double.parseDouble(customer_amount.getText().toString());
                            Intent intent=new Intent(InvoiceManagementActivity.this,MultiCurrenciesPaymentActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("com_pos_leaders_cart_total_price",amount);
                            intent.putExtra("Receipt","ReceiptWay");
                            intent.putExtra("customer",customer.toString());
                            v1.getContext().startActivity(intent);

                            customerRecpDialog.dismiss();
                        }


                    }
                });




            }});

        btnCancel.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              InvoiceManagementActivity.newInvoiceList=null;
              finish();


                         }
       });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalAmount=0;
               newInvoiceList=new ArrayList<BoInvoice>();

                for(int i=0;i<InvoiceManagementActivity.reciptObjectList.size();i++) {
                    ReciptObject reciptObject = InvoiceManagementActivity.reciptObjectList.get(i);
                    BoInvoice boInvoice = invoiceList.get(i);
                     JSONObject jsonObject =boInvoice.getDocumentsData();

                    if(reciptObject.isAll()){
                        try {
                            jsonObject.remove("invoiceStatus");
                            jsonObject.put("invoiceStatus", InvoiceStatus.PAID.getValue());
                           jsonObject.put("balanceDue",0);
                            jsonObject.remove("totalPaid");
                            jsonObject.put("totalPaid",reciptObject.getAllAmount());
                            boInvoice.setDocumentsData(jsonObject);
                            newInvoiceList.add(boInvoice);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        totalAmount+=reciptObject.getAllAmount();
                    }


                    else if(reciptObject.isPartially()){
                        try {

                            double d= Double.parseDouble(jsonObject.getString("totalPaid"));

                            double x =d+reciptObject.getPartialAmount();
                            Log.d("SUMPPINVOice",x+"");
                            if(x==jsonObject.getDouble("total")){
                                Log.d("totalPInvoice",jsonObject.getDouble("total")+"");
                                jsonObject.remove("totalPaid");
                                jsonObject.put("totalPaid",d+reciptObject.getPartialAmount());

                                jsonObject.remove("invoiceStatus");
                                jsonObject.put("invoiceStatus", InvoiceStatus.PAID.getValue());
                            }else {
                                Log.d("totalPInvoice2",jsonObject.getDouble("total")+"");
                                jsonObject.remove("totalPaid");
                                jsonObject.put("totalPaid",d+reciptObject.getPartialAmount());
                                jsonObject.remove("invoiceStatus");
                                jsonObject.put("invoiceStatus", InvoiceStatus.PARTIALLY_PAID.getValue());
                            }


                            boInvoice.setDocumentsData(jsonObject);
                            Log.d("testTotalPaid",boInvoice.toString());
                            newInvoiceList.add(boInvoice);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        totalAmount+=reciptObject.getPartialAmount();
                    }




                    /*else if(reciptObject.isPartially()){
                        try {Log.d("totalAmountParially",jsonObject.getDouble("totalPaid")+"");
                            jsonObject.remove("invoiceStatus");
                            jsonObject.put("invoiceStatus", InvoiceStatus.PARTIALLY_PAID.getValue());
                            double d= Double.parseDouble(jsonObject.getString("totalPaid"));
                            jsonObject.remove("totalPaid");
                            jsonObject.put("totalPaid",d+reciptObject.getPartialAmount());
                            boInvoice.setDocumentsData(jsonObject);
                            Log.d("testTotalPaid",boInvoice.toString());
                            newInvoiceList.add(boInvoice);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        totalAmount+=reciptObject.getPartialAmount();
                    }*/

                }
                RequiredAmount.setText(Util.makePrice(totalAmount-pay));
                Log.d("totalAmount111",totalAmount+"    "+ pay);

                if(totalAmount!=pay){
                    Toast.makeText(InvoiceManagementActivity.this,"insert the required amount",Toast.LENGTH_LONG).show();
                }else {
                    Intent intent=new Intent(InvoiceManagementActivity.this,MultiCurrenciesPaymentActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("com_pos_leaders_cart_total_price",totalAmount);
                    intent.putExtra("Receipt","ReceiptWay");
                    intent.putExtra("customer",customer.toString());
                    startActivityForResult(intent, REQUEST_RECIPT_ACTIVITY_CODE);
                    //v.getContext().startActivity(intent);


           }


            }
        });



        gvCustomer.setNumColumns(3);

        customer_id.setText("");
        customer_id.setHint("Search..");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        invoiceListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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

                }
            }
        });


        invoiceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                adapter.notifyDataSetChanged();
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
                    StartGetCustomerGeneralLedgerConnectionForInvoice startInvoiceConnectionDebtAmount = new StartGetCustomerGeneralLedgerConnectionForInvoice();
                    startInvoiceConnectionDebtAmount.execute(String.valueOf(customer.getCustomerId()));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final Dialog paymentReceiptDialog = new Dialog(InvoiceManagementActivity.this);
                    paymentReceiptDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    paymentReceiptDialog.show();
                    paymentReceiptDialog.setContentView(R.layout.recipt_cash_dialog);
                    final EditText etAmount = (EditText) paymentReceiptDialog.findViewById(R.id.recipthPaymentDialog_TECash);
                    final TextView tvDebtAmount = (TextView) paymentReceiptDialog.findViewById(R.id.TvTotalAmountDebt);
                    tvDebtAmount.setText(Util.makePrice(InvoiceManagementActivity.debtAmount));
                    final TextView totalAmountAfterPay = (TextView) paymentReceiptDialog.findViewById(R.id.totalAmountAfterPay);
                    Button btnOk = (Button)paymentReceiptDialog.findViewById(R.id.reciptPaymentDialog_BTOk);
                    ImageView imageView =(ImageView) paymentReceiptDialog.findViewById(R.id.closeDialog);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                paymentReceiptDialog.dismiss();
                            }


                    });


                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pay= Double.parseDouble(etAmount.getText().toString());
                            payAmount.setText( etAmount.getText().toString());
                            RequiredAmount.setText(etAmount.getText().toString());
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
                                if (Util.isDouble(etAmount.getText().toString())){
                                    double x=0;
                                    double total=debtAmount-Double.parseDouble(etAmount.getText().toString());
                                    totalAmountAfterPay.setText(Util.makePrice(total));
                                }



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
                        ReciptObject reciptObject =new ReciptObject(false,false,0,0);
                        InvoiceManagementActivity.reciptObjectList.add(reciptObject);
                        InvoiceManagementActivity.partiallyString.add("Partial");

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
                    InvoiceManagementActivity.invoiceListView.invalidate();
                  InvoiceManagementActivity.adapter = new ReciptManagementListViewAdapter(InvoiceManagementActivity.context,R.layout.list_adapter_row_recipt,InvoiceManagementActivity.invoiceList,InvoiceManagementActivity.invoiceNumberList,InvoiceManagementActivity.reciptObjectList,InvoiceManagementActivity.partiallyString);
                  InvoiceManagementActivity.invoiceListView.setAdapter(InvoiceManagementActivity.adapter);

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


class StartGetCustomerGeneralLedgerConnectionForInvoice extends AsyncTask<String,Void,String> {
    private MessageTransmit messageTransmit;

    StartGetCustomerGeneralLedgerConnectionForInvoice() {
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
    protected String doInBackground(String... args) {
        String customerId=args[0];
        try {
            String url = "GeneralLedger/"+customerId;

            String invoiceRes = messageTransmit.authGet(url,SESSION.token);
            JSONObject jsonObject = new JSONObject(invoiceRes);
            String msgData = jsonObject.getString(MessageKey.responseBody);
            JSONObject response = new JSONObject(msgData);
            InvoiceManagementActivity.debtAmount=response.getDouble("creditAmount");

        }catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onPostExecute(String s) {

        progressDialog.cancel();
        super.onPostExecute(s);

        //endregion
    }

}


