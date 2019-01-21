package com.pos.leaders.leaderspossystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ChecksDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Printer.InvoiceImg;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.SaleManagementListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KARAM on 26/10/2016.
 * Editing by KARAM on 10/04/2017.
 */
public class OrdersManagementActivity extends AppCompatActivity {

    int loadItemOffset = 0;
    int countLoad = 20;
    boolean userScrolled = false;
    String searchWord = "";

    TextView customer;
    ListView lvOrders;
    OrderDBAdapter orderDBAdapter;
    EmployeeDBAdapter userDBAdapter;
    PaymentDBAdapter paymentDBAdapter;
    EditText etSearch;
    SaleManagementListViewAdapter adapter;
    View previousView = null;

    List<Order> _saleList;
    List<OrderDetails> orders;
    List<Check> checks;
    List<Order> All_orders;
    public static List<BoInvoice>invoiceList=new ArrayList<>();
    public static Context context = null;

    private final static int DAY_MINUS_ONE_SECOND = 86399999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sales_management);
        TitleBar.setTitleBar(this);
        context=this;
        lvOrders = (ListView) findViewById(R.id.saleManagement_LVSales);

        etSearch = (EditText) findViewById(R.id.etSearch);
        etSearch.setText("");
        etSearch.setHint("Search..");
        etSearch.setFocusable(true);
        etSearch.requestFocus();


        orderDBAdapter = new OrderDBAdapter(this);

        userDBAdapter = new EmployeeDBAdapter(this);
        paymentDBAdapter = new PaymentDBAdapter(this);

        orderDBAdapter.open();
        StartInvoiceAndCreditInvoiceConnection startInvoiceConnection = new StartInvoiceAndCreditInvoiceConnection();
        startInvoiceConnection.execute("1");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        _saleList = orderDBAdapter.lazyLoad(loadItemOffset, countLoad);

        orderDBAdapter.close();
        for (Order s : _saleList) {
            userDBAdapter.open();
            s.setUser(userDBAdapter.getEmployeeByID(s.getByUser()));
            userDBAdapter.close();

            paymentDBAdapter.open();
            try {
                s.setPayment(paymentDBAdapter.getPaymentBySaleID(s.getOrderId()).get(0));
            }
            catch (Exception ex){
                //_saleList.remove(s);
                ex.printStackTrace();
            }
            paymentDBAdapter.close();
        }
        Log.i("log", _saleList.toString());
        /*for (ORDER s : saleList) {
			if(DateConverter.dateBetweenTwoDates(from, to, s.getOrder_date())) {
				_saleList.add(s);
			}
		}*/
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.list_adapter_head_row_order, lvOrders, false);
        lvOrders.addHeaderView(header, null, false);
        final List<Object>objectList = new ArrayList<Object>();
        objectList.addAll(_saleList);
        objectList.addAll(invoiceList);
        Log.d("objictList",objectList.toString());
            adapter = new SaleManagementListViewAdapter(this, R.layout.list_adapter_row_sales_management, objectList);

        lvOrders.setAdapter(adapter);

        lvOrders.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                    loadItemOffset+=countLoad;
                    LoadMore();
                    Log.i("loadmore", "load");
                }
            }
        });

        lvOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(position!=_saleList.size()){
                if(objectList.get(position) instanceof  Order){
                final Order sale = (Order) objectList.get(position);
                OrderDetailsDBAdapter orderDBAdapter = new OrderDetailsDBAdapter(OrdersManagementActivity.this);
                orderDBAdapter.open();
                orders = orderDBAdapter.getOrderBySaleID(sale.getOrderId());
                orderDBAdapter.close();
                ProductDBAdapter productDBAdapter = new ProductDBAdapter(OrdersManagementActivity.this);
                productDBAdapter.open();
                for (OrderDetails o : orders) {
                    if (o.getProductId() != -1) {
                        o.setProduct(productDBAdapter.getProductByID(o.getProductId()));
                    } else {
                        o.setProduct(new Product(-1, getApplicationContext().getResources().getString(R.string.general),getApplicationContext().getResources().getString(R.string.general), o.getUnitPrice(), SESSION._EMPLOYEE.getEmployeeId()));
                    }
                }
                productDBAdapter.close();

                PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(OrdersManagementActivity.this);
                paymentDBAdapter.open();
                final List<Payment> payments = paymentDBAdapter.getPaymentBySaleID(sale.getOrderId());

                sale.setPayment(payments.get(0));

                paymentDBAdapter.close();

                checks = new ArrayList<Check>();

                for (Payment p : payments) {
                    switch (p.getPaymentWay()) {
                        case CONSTANT.CHECKS:
                            ChecksDBAdapter checksDBAdapter = new ChecksDBAdapter(OrdersManagementActivity.this);
                            checksDBAdapter.open();
                            checks.addAll(checksDBAdapter.getPaymentBySaleID(sale.getOrderId()));
                            checksDBAdapter.close();
                            break;
                        case CONSTANT.CASH:
                            break;
                        case CONSTANT.CREDIT_CARD:
                            break;
                    }
                }
                LinearLayout fr = (LinearLayout) view.findViewById(R.id.listSaleManagement_FLMore);
                if (previousView == null) {
                    fr.setVisibility(View.VISIBLE);
                    previousView = view;
                } else {
                    fr.setVisibility(View.VISIBLE);
                    previousView.findViewById(R.id.listSaleManagement_FLMore).setVisibility(View.GONE);
                    previousView.setBackgroundColor(getResources().getColor(R.color.transparent));
                    previousView = view;
                }
                final InvoiceImg invoiceImg = new InvoiceImg(OrdersManagementActivity.this);


                sale.setOrders(orders);
                sale.setUser(SESSION._EMPLOYEE);
                //region Print Button
                final Button print = (Button) view.findViewById(R.id.listSaleManagement_BTView);
                print.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //send customerName copy from the in voice
                        new AlertDialog.Builder(OrdersManagementActivity.this)
                                .setTitle(getString(R.string.copyinvoice))
                                .setMessage(getString(R.string.print_copy_invoice))
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (checks.size() > 0){
                                            Intent i = new Intent(OrdersManagementActivity.this, SalesHistoryCopySales.class);
                                            SETTINGS.copyInvoiceBitMap = invoiceImg.normalInvoice(sale.getOrderId(), orders, sale, true, SESSION._EMPLOYEE, checks);
                                            startActivity(i);
                                            // print(invoiceImg.normalInvoice(sale.getCashPaymentId(), orders, sale, true, SESSION._EMPLOYEE, checks));
                                        }
                                        else{
                                            Intent i = new Intent(OrdersManagementActivity.this, SalesHistoryCopySales.class);
                                            SETTINGS.copyInvoiceBitMap =invoiceImg.normalInvoice(sale.getOrderId(), orders, sale, true, SESSION._EMPLOYEE, null);
                                            startActivity(i);
                                         // print(invoiceImg.normalInvoice(sale.getCashPaymentId(), orders, sale, true, SESSION._EMPLOYEE, null));

                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
                //endregion Print Button

                //region Replacement Note Button

                Button btnRN = (Button) view.findViewById(R.id.listSaleManagement_BTReturn);
                btnRN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderDBAdapter saleDBAdapter = new OrderDBAdapter(OrdersManagementActivity.this);
                        saleDBAdapter.open();
                        sale.setReplacementNote(sale.getReplacementNote() + 1);
                        saleDBAdapter.updateEntry(sale);
                        saleDBAdapter.close();

                        print(invoiceImg.replacmentNote(sale, false));
                    }
                });
                //endregion Replacement Note Button

                //region Cancellation ORDER Button

                Button btnCan = (Button) view.findViewById(R.id.listSaleManagement_BTCancel);
                btnCan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderDBAdapter saleDBAdapter = new OrderDBAdapter(OrdersManagementActivity.this);
                        saleDBAdapter.open();
                        saleDBAdapter.deleteEntry(sale.getOrderId());
                        if (checks.size() > 0)
                            print(invoiceImg.cancelingInvoice(sale, false, checks));
                        else
                            print(invoiceImg.cancelingInvoice(sale, false, null));
                        sale.setPayment(new Payment(payments.get(0)));
                        long sID = saleDBAdapter.insertEntry(SESSION._EMPLOYEE.getEmployeeId(), new Timestamp(System.currentTimeMillis()), sale.getReplacementNote(), true, sale.getTotalPrice() * -1, sale.getTotalPaidAmount() * -1, sale.getCustomerId(), sale.getCustomer_name(),sale.getCartDiscount(),sale.getNumberDiscount());

                        saleDBAdapter.close();
                        PaymentDBAdapter paymentDBAdapter1 = new PaymentDBAdapter(OrdersManagementActivity.this);
                        paymentDBAdapter1.open();
                        paymentDBAdapter1.insertEntry(sale.getPayment().getPaymentWay(), sale.getTotalPrice() * -1, sID,sale.getOrderKey());
                        paymentDBAdapter1.close();
                        //// TODO: 19/01/2017 cancel this sale and print return note and mony back by the payment way
                    }
                });
                //endregion Cancellation ORDER Button

                previousView.setBackgroundColor(getResources().getColor(R.color.list_background_color));
                }}else {
                }
            }
        });

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etSearch.setFocusable(true);
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //lvOrders.setTextFilterEnabled(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String word = etSearch.getText().toString();
                loadItemOffset = 0;
                searchWord = word;
                _saleList.clear();
                LoadMore();
            }
        });

    }

    private void LoadMore(){

        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setTitle(getBaseContext().getString(R.string.wait_for_finish));



        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {

                dialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
                dialog.cancel();
            }

            @Override
            protected Void doInBackground(Void... params) {
                orderDBAdapter.open();
                if (!searchWord.equals("")) {
                    _saleList.addAll(orderDBAdapter.search(searchWord, loadItemOffset, countLoad));
                } else {
                    searchWord = "";
                    _saleList.addAll(orderDBAdapter.lazyLoad(loadItemOffset, countLoad));
                }
                orderDBAdapter.close();
                return null;
            }
        }.execute();

        adapter.notifyDataSetChanged();
    }




    private void print(Bitmap bitmap) {
        PrintTools printTools = new PrintTools(this);
        printTools.PrintReport(bitmap);
    }


}
class StartInvoiceAndCreditInvoiceConnection extends AsyncTask<String,Void,String> {
    private MessageTransmit messageTransmit;
    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    BoInvoice invoice;
    StartInvoiceAndCreditInvoiceConnection() {
        messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
    }

    final ProgressDialog progressDialog = new ProgressDialog(OrdersManagementActivity.context);
    final ProgressDialog progressDialog2 =new ProgressDialog(OrdersManagementActivity.context);

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
            String url = ApiURL.Documents+"/InvoicesAndCreditInvoice/";
            String invoiceRes = messageTransmit.authGet(url,SESSION.token);
            JSONObject jsonObject = new JSONObject(invoiceRes);
            String msgData = jsonObject.getString(MessageKey.responseBody);
            if (msgData.startsWith("[")) {
                try {
                    JSONArray jsonArray = new JSONArray(msgData);

                    for (int i = 0; i < jsonArray.length() ; i++) {
                        msgData = jsonArray.getJSONObject(i).toString();
                        JSONObject msgDataJson =new JSONObject(msgData);
                        Log.d("tttt",msgDataJson.getString("type"));
                        if(msgDataJson.getString("type").equals("INVOICE")){
                        invoice = new BoInvoice(DocumentType.INVOICE,msgDataJson.getJSONObject("documentsData"),msgDataJson.getString("docNum"));

                        }else  if(msgDataJson.getString("type").equals("CREDIT_INVOICE")){
                            invoice = new BoInvoice(DocumentType.CREDIT_INVOICE,msgDataJson.getJSONObject("documentsData"),msgDataJson.getString("docNum"));
                        }
                        OrdersManagementActivity.invoiceList.add(invoice);
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
            Toast.makeText(CreateCreditInvoiceActivity.context, "Try Again.", Toast.LENGTH_SHORT).show();
        }
        progressDialog.cancel();
        super.onPostExecute(s);

        //endregion
    }
}
