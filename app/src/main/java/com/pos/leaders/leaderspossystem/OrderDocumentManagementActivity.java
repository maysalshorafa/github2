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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.Tools.CustomerCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.OrderDocumentManagementListViewAdapter;
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

public class OrderDocumentManagementActivity extends AppCompatActivity {
    public static ListView orderListView;
    EditText etSearch;
    ImageView chooseCustomer;
    TextView customerName;
    boolean userScrolled =false;
    public static Customer customer;
    List<Customer> customerList = new ArrayList<>();
    List<Customer> AllCustmerList = new ArrayList<>();
    public static Context context = null;
    public static List<BoInvoice> orderDocumentsList =new ArrayList<>();
    public static ArrayList<String> orderNumberList =new ArrayList<>();
    View previousView = null;
    public static  List<JSONObject>ordersJson=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ordere_document_management);
        TitleBar.setTitleBar(this);
        Log.d("token", SESSION.token+"");
        context=this;
        Bundle bundle = getIntent().getExtras();

        orderListView = (ListView)findViewById(R.id.orderDocumentManagement_LVOrderDocument);
        chooseCustomer = (ImageView)findViewById(R.id.chooseCustomer);
        customerName = (TextView)findViewById(R.id.customerName);
        chooseCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDocumentsList =new ArrayList<>();
                orderNumberList =new ArrayList<>();
                callCustomerDialog();


            }
        });
   orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                try { Intent intent = new Intent(OrderDocumentManagementActivity.this, SalesCartActivity.class);
                    BoInvoice invoice = orderDocumentsList.get(position);
                    JSONObject jsonObject = new JSONObject(invoice.toString());
                    intent.putExtra("orderJson",jsonObject.toString());
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }});




    }
    //choose customer invoice
    private void callCustomerDialog() {
        ordersJson=new ArrayList<>();
        CustomerDBAdapter customerDBAdapter =new CustomerDBAdapter(this);
        customerDBAdapter.open();
        final Dialog customerDialog = new Dialog(OrderDocumentManagementActivity.this);
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
                customerName.setText(customer.getFullName());
                if(customer!=null) {
                    StartOrderDocumentConnection startOrderDocumentConnection = new StartOrderDocumentConnection();
                    startOrderDocumentConnection.execute(String.valueOf(customer.getCustomerId()));
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
                                c.getLastName().toLowerCase().contains(word.toLowerCase())) {
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
class StartOrderDocumentConnection extends AsyncTask<String,Void,String> {
    private MessageTransmit messageTransmit;
    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    BoInvoice invoice;
    StartOrderDocumentConnection() {
        messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
    }

    final ProgressDialog progressDialog = new ProgressDialog(OrderDocumentManagementActivity.context);
    final ProgressDialog progressDialog2 =new ProgressDialog(OrderDocumentManagementActivity.context);

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
            String url = ApiURL.Documents+"/readyOrderDocumentForCustomer/"+customerId;
            //   Log.d("invoiceUrl",url);
            String invoiceRes = messageTransmit.authGet(url,SESSION.token);
            // Log.d("invoiceRes",invoiceRes);
            JSONObject jsonObject = new JSONObject(invoiceRes);
            String msgData = jsonObject.getString(MessageKey.responseBody);
            Log.d("invoices",msgData+"");

            if (msgData.startsWith("[")) {
                try {
                    JSONArray jsonArray = new JSONArray(msgData);

                    for (int i = 0; i < jsonArray.length() ; i++) {
                       String msgData1 = jsonArray.getJSONObject(i).toString();
                        JSONObject msgDataJson =new JSONObject(msgData1);
                        OrderDocumentManagementActivity.orderNumberList.add(msgDataJson.getString("docNum"));
                        invoice = new BoInvoice(DocumentType.ORDER_DOCUMENT,msgDataJson.getJSONObject("documentsData"),msgDataJson.getString("docNum"));
                        OrderDocumentManagementActivity.orderDocumentsList.add(invoice);
                    }
                    Log.d("invoices111",OrderDocumentManagementActivity.orderDocumentsList.toString()+"");



                } catch (Exception e) {
                    e.printStackTrace();
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
                    OrderDocumentManagementListViewAdapter orderDocumentManagementListViewAdapter = new OrderDocumentManagementListViewAdapter(OrderDocumentManagementActivity.context,R.layout.list_adapter_row_order_document_management,OrderDocumentManagementActivity.orderDocumentsList,OrderDocumentManagementActivity.orderNumberList);
                    OrderDocumentManagementActivity.orderListView.setAdapter(orderDocumentManagementListViewAdapter);
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
            Toast.makeText(OrderDocumentManagementActivity.context, "Try Again.", Toast.LENGTH_SHORT).show();
        }
        progressDialog.cancel();
        super.onPostExecute(s);

        //endregion
    }
}
