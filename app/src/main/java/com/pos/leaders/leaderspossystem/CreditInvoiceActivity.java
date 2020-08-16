package com.pos.leaders.leaderspossystem;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.ListView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.CustomerAndClub.AddNewCustomer;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.Tools.CustomerCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.InvoiceManagementListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreditInvoiceActivity extends AppCompatActivity {
    Button creditInvoiceByCustomer , creditInvoiceByInvoice , creditInvoiceByProduct ;
    boolean userScrolled =false;
    List<Customer> customerList =new ArrayList<>();
    Customer customer = new Customer();
    Customer customerN =new Customer();
    static ListView invoiceListView ;
    public static List<BoInvoice>invoiceList=new ArrayList<>();
    static Context context;
    public static ArrayList<String>invoiceNumberList=new ArrayList<>();
    List<Customer> AllCustomerList;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Remove title bar
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);

            // Remove notification bar
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_credit_invoice);

            TitleBar.setTitleBar(this);
        final OrderDBAdapter orderDBAdapter = new OrderDBAdapter(this);
        orderDBAdapter.open();
        context=this;
        creditInvoiceByCustomer = (Button)findViewById(R.id.creditInvoiceManagementActivity_btnByCustomer);
        creditInvoiceByInvoice = (Button)findViewById(R.id.creditInvoiceManagementActivity_btnByInvoice);
        creditInvoiceByProduct =(Button)findViewById(R.id.creditInvoiceManagementActivity_btnByProduct);
        invoiceListView =(ListView)findViewById(R.id.creditInvoiceManagementActivity_salesListView);
        invoiceList=new ArrayList<>();
        creditInvoiceByCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invoiceNumberList=new ArrayList<>();
                customer= callPopupForChooseCustomer();



            }
        });
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
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                try { Intent intent = new Intent(CreditInvoiceActivity.this, SalesCartActivity.class);
                    BoInvoice invoice = invoiceList.get(position);
                    JSONObject jsonObject = new JSONObject(invoice.toString());
                    Log.d("tttttfgh",jsonObject.toString());
                    intent.putExtra("creditInvoiceJson",jsonObject.toString());
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }});

    }
    private Customer callPopupForChooseCustomer() {
        CustomerDBAdapter customerDBAdapter = new CustomerDBAdapter(CreditInvoiceActivity.this);
        customerDBAdapter.open();
        final Dialog customerDialog = new Dialog(CreditInvoiceActivity.this);
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

        ((Button) customerDialog.findViewById(R.id.btn_add))
                .setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
                        Intent intent = new Intent(CreditInvoiceActivity.this, AddNewCustomer.class);
                        startActivity(intent);

                        customerDialog.dismiss();


                    }
                });


        customer_id.setText("");
        customer_id.setHint("Search..");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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

      customerList = customerDBAdapter.getTopCustomer(0, 50);
        AllCustomerList = customerList;

        CustomerCatalogGridViewAdapter  custmerCatalogGridViewAdapter = new CustomerCatalogGridViewAdapter(getApplicationContext(), customerList);

        gvCustomer.setAdapter(custmerCatalogGridViewAdapter);

        gvCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              customerN=  customerList.get(position);
                StartCreditInvoiceConnectionForCustomer startCreditInvoiceConnectionForCustomer = new  StartCreditInvoiceConnectionForCustomer();
                startCreditInvoiceConnectionForCustomer.execute(String.valueOf(customerN.getCustomerId()));
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
                    for (Customer c : AllCustomerList) {
                        Log.d("testCustomer",c.toString());
                        if (c.getFirstName().toLowerCase().contains(word.toLowerCase()) ||
                                c.getLastName().toLowerCase().contains(word.toLowerCase())||
                                c.getPhoneNumber().toLowerCase().contains(word.toLowerCase())||
                                String.valueOf(c.getCustomerIdentity()).contains(word.toLowerCase())) {
                            customerList.add(c);

                        }
                    }
                } else {
                    customerList = AllCustomerList;

                }
                CustomerCatalogGridViewAdapter adapter = new CustomerCatalogGridViewAdapter(getApplicationContext(), customerList);
                gvCustomer.setAdapter(adapter);


            }
        });

return  customerN;
    }

}
class StartCreditInvoiceConnectionForCustomer extends AsyncTask<String,Void,String> {
    private MessageTransmit messageTransmit;
    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    BoInvoice invoice;
    StartCreditInvoiceConnectionForCustomer() {
        messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
    }

    final ProgressDialog progressDialog = new ProgressDialog(CreditInvoiceActivity.context);
    final ProgressDialog progressDialog2 =new ProgressDialog(CreditInvoiceActivity.context);

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
            String url = ApiURL.Documents+"/InvoicesForCustomer/"+customerId;
            String invoiceRes = messageTransmit.authGet(url,SESSION.token);
            JSONObject jsonObject = new JSONObject(invoiceRes);
            String msgData = jsonObject.getString(MessageKey.responseBody);
            if (msgData.startsWith("[")) {
                try {
                    JSONArray jsonArray = new JSONArray(msgData);

                    for (int i = 0; i <= jsonArray.length() ; i++) {
                        msgData = jsonArray.getJSONObject(i).toString();
                        JSONObject msgDataJson =new JSONObject(msgData);
                        CreditInvoiceActivity.invoiceNumberList.add(msgDataJson.getString("docNum"));
                        invoice = new BoInvoice(DocumentType.INVOICE,msgDataJson.getJSONObject("documentsData"),msgDataJson.getString("docNum"));
                        CreditInvoiceActivity.invoiceList.add(invoice);
                    }

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
                    InvoiceManagementListViewAdapter invoiceManagementListViewAdapter = new InvoiceManagementListViewAdapter(CreditInvoiceActivity.context,R.layout.list_adapter_row_invoices_management,CreditInvoiceActivity.invoiceList, CreditInvoiceActivity.invoiceNumberList);
                    CreditInvoiceActivity.invoiceListView.setAdapter(invoiceManagementListViewAdapter);
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
            Toast.makeText(CreditInvoiceActivity.context, "Try Again.", Toast.LENGTH_SHORT).show();
        }
        progressDialog.cancel();
        super.onPostExecute(s);

        //endregion
    }



}

