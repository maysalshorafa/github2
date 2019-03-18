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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ChecksDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyOperationDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyReturnsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.Customer;
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

import static com.pos.leaders.leaderspossystem.Tools.SendLog.sendLogFile;

/**
 * Created by KARAM on 26/10/2016.
 * Editing by KARAM on 10/04/2017.
 */
public class OrdersManagementActivity extends AppCompatActivity {

    int loadItemOffset = 0;
    int countLoad = 60;
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
    List<Order>filterOrderList;
    List<BoInvoice>filterBoInvoice;
    public static List<BoInvoice>invoiceList=new ArrayList<>();
    public static Context context = null;
    List<Object>list=new ArrayList<Object>();
    Spinner searchSpinner;

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
        searchSpinner=(Spinner)findViewById(R.id.searchSpinner);
        final ArrayList<Integer> idForSearchType = new ArrayList<Integer>();
        final ArrayList<String> hintForSearchType = new ArrayList<String>();
        hintForSearchType.add(getString(R.string.customer));
        hintForSearchType.add(getString(R.string.sale_id));
        hintForSearchType.add(getString(R.string.date));
        hintForSearchType.add(getString(R.string.price));
        hintForSearchType.add(getString(R.string.type));
        idForSearchType.add(0);
        idForSearchType.add(1);
        idForSearchType.add(2);
        idForSearchType.add(3);
        idForSearchType.add(4);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hintForSearchType);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(adapter1);
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
                Log.d("testInvoice",objectList.get(position-1).toString()+"");
                if(objectList.get(position-1) instanceof  Order){
                final Order sale = (Order) objectList.get(position-1);
                    CustomerDBAdapter customerDBAdapter = new CustomerDBAdapter(OrdersManagementActivity.this);
                    customerDBAdapter.open();
                    Customer customer =customerDBAdapter.getCustomerByID(sale.getCustomerId());
                    sale.setCustomer(customer);
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
                                            try {
                                                Intent i = new Intent(OrdersManagementActivity.this, SalesHistoryCopySales.class);
                                                SETTINGS.copyInvoiceBitMap = invoiceImg.normalInvoice(sale.getOrderId(), orders, sale, true, SESSION._EMPLOYEE, checks);
                                                startActivity(i);
                                            }catch (Exception e){
                                                Log.d("exception",sale.toString());

                                                Log.d("exception",sale.toString());
                                                sendLogFile();

                                            }

                                            // print(invoiceImg.normalInvoice(sale.getCashPaymentId(), orders, sale, true, SESSION._EMPLOYEE, checks));
                                        }
                                        else{
                                            try {
                                                Intent i = new Intent(OrdersManagementActivity.this, SalesHistoryCopySales.class);
                                                SETTINGS.copyInvoiceBitMap =invoiceImg.normalInvoice(sale.getOrderId(), orders, sale, true, SESSION._EMPLOYEE, null);
                                                startActivity(i);
                                            }catch (Exception e){
                                                Log.d("exception",sale.toString());
                                                Log.d("exception",e.toString());
                                                sendLogFile();
                                            }

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
                        try {
                            Intent i = new Intent(OrdersManagementActivity.this, SalesHistoryCopySales.class);
                            SETTINGS.copyInvoiceBitMap =invoiceImg.replacmentNote(sale,false);
                            startActivity(i);
                        }catch (Exception e){
                            Log.d("exception",sale.toString());
                            Log.d("exception",e.toString());
                            sendLogFile();
                        }
                    }
                });
                //endregion Replacement Note Button

                //region Cancellation ORDER Button

                Button btnCan = (Button) view.findViewById(R.id.listSaleManagement_BTCancel);
                    if(sale.getCancellingOrderId()>0){
                        btnCan.setVisibility(View.GONE);
                    }else {
                        btnCan.setVisibility(View.VISIBLE);
                    }

                btnCan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderDBAdapter saleDBAdapter = new OrderDBAdapter(OrdersManagementActivity.this);
                        saleDBAdapter.open();
                        sale.setPayment(new Payment(payments.get(0)));
                        long sID = saleDBAdapter.insertEntry(SESSION._EMPLOYEE.getEmployeeId(), new Timestamp(System.currentTimeMillis()), sale.getReplacementNote(), true, sale.getTotalPrice() * -1, sale.getTotalPaidAmount() * -1, sale.getCustomerId(), sale.getCustomer_name(),sale.getCartDiscount(),sale.getNumberDiscount(),sale.getOrderId());
                        Order order = saleDBAdapter.getOrderById(sID);
                        sale.setCancellingOrderId(sID);
                        saleDBAdapter.updateEntry(sale);
                        PaymentDBAdapter paymentDBAdapter1 = new PaymentDBAdapter(OrdersManagementActivity.this);
                        paymentDBAdapter1.open();
                        paymentDBAdapter1.insertEntry(sale.getPayment().getPaymentWay(), sale.getTotalPrice() * -1, sID,order.getOrderKey());
                        paymentDBAdapter1.close();
                        CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(OrdersManagementActivity.this);
                        cashPaymentDBAdapter.open();
                        cashPaymentDBAdapter.insertEntry(sID,sale.getTotalPrice() * -1,0,new Timestamp(System.currentTimeMillis()),1,1);
                        CurrencyOperationDBAdapter currencyOperationDBAdapter = new CurrencyOperationDBAdapter(OrdersManagementActivity.this);
                        currencyOperationDBAdapter.open();
                        CurrencyReturnsDBAdapter currencyReturnsDBAdapter =new CurrencyReturnsDBAdapter(OrdersManagementActivity.this);
                        currencyReturnsDBAdapter.open();
                        if(SETTINGS.enableCurrencies){
                            currencyOperationDBAdapter.insertEntry(new Timestamp(System.currentTimeMillis()),sID,CONSTANT.DEBIT,sale.getTotalPaidAmount() * -1,"ILS");
                            currencyReturnsDBAdapter.insertEntry(sale.getOrderId(),(sale.getTotalPaidAmount()-sale.getTotalPrice())*-1,new Timestamp(System.currentTimeMillis()),0);
                        }
                        if (checks.size() > 0)
                        try {
                            Intent i = new Intent(OrdersManagementActivity.this, SalesHistoryCopySales.class);
                            SETTINGS.copyInvoiceBitMap =invoiceImg.cancelingInvoice(sale,false,checks);
                            startActivity(i);
                        }catch (Exception e){
                            Log.d("exception",sale.toString());
                            Log.d("exception",e.toString());
                            sendLogFile();
                        }
                        else
                        try {
                            Intent i = new Intent(OrdersManagementActivity.this, SalesHistoryCopySales.class);
                            SETTINGS.copyInvoiceBitMap =invoiceImg.cancelingInvoice(sale,false,null);
                            startActivity(i);
                        }catch (Exception e){
                            Log.d("exception",sale.toString());
                            Log.d("exception",e.toString());
                            sendLogFile();
                        }


                        //// TODO: 19/01/2017 cancel this sale and print return note and mony back by the payment way
                    }
                });
                //endregion Cancellation ORDER Button

                previousView.setBackgroundColor(getResources().getColor(R.color.list_background_color));
                }else {
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

                    //  Log.d("testInvoice",objectList.get(position).toString());
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
                                            BoInvoice boInvoice = (BoInvoice) objectList.get(position-1);
                                            try {
                                                Intent i = new Intent(OrdersManagementActivity.this, SalesHistoryCopySales.class);
                                                SETTINGS.copyInvoiceBitMap = invoiceImg.copyInvoice(boInvoice);
                                                startActivity(i);
                                            }catch (Exception e){
                                                e.printStackTrace();
                                                Log.d("exception",e.toString());
                                                sendLogFile();

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
                    Button btnRN = (Button) view.findViewById(R.id.listSaleManagement_BTReturn);
                    btnRN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BoInvoice boInvoice = (BoInvoice) objectList.get(position-1);
                            try {
                                Intent i = new Intent(OrdersManagementActivity.this, SalesHistoryCopySales.class);
                                SETTINGS.copyInvoiceBitMap = invoiceImg.replaceInvoice(boInvoice);
                                startActivity(i);
                            }catch (Exception e){
                                e.printStackTrace();
                                Log.d("exception",e.toString());
                                sendLogFile();

                            }
                        }
                    });
                    Button btnCan = (Button) view.findViewById(R.id.listSaleManagement_BTCancel);
                    btnCan.setVisibility(View.INVISIBLE);

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

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                list=new ArrayList<Object>();
                filterOrderList = new ArrayList<Order>();
                String word = etSearch.getText().toString();
                if (!word.equals("")) {
                    // Database query can be a time consuming task ..
                    // so its safe to call database query in another thread
                    // Handler, will handle this stuff
                    new AsyncTask<String, Void, Void>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }

                        @Override
                        protected Void doInBackground(String... params) {
                            final String type = searchSpinner.getSelectedItem().toString();
                            filterOrderList = new ArrayList<Order>();
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            orderDBAdapter.open();
                            filterOrderList = orderDBAdapter.search(params[0], loadItemOffset,countLoad,type);
                        //    filterBoInvoice = searchInInvoiceList(params[0]);

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Log.d("filterOrderList",filterOrderList.toString());
                            list.addAll(filterOrderList);
                     //       list.addAll(filterBoInvoice);
                            SaleManagementListViewAdapter adapter = new SaleManagementListViewAdapter(OrdersManagementActivity.this, R.layout.list_adapter_row_sales_management, list);
                            lvOrders.setAdapter(adapter);
                        }
                    }.execute(word);
                } else {
                    filterOrderList = _saleList;
                    filterBoInvoice=invoiceList;
                    list.addAll(filterOrderList);
                    list.addAll(filterBoInvoice);
                    SaleManagementListViewAdapter adapter = new SaleManagementListViewAdapter(OrdersManagementActivity.this, R.layout.list_adapter_row_sales_management, list);
                    lvOrders.setAdapter(adapter);
                }

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
                final String type = searchSpinner.getSelectedItem().toString();
                orderDBAdapter.open();
                if (!searchWord.equals("")) {
                    _saleList.addAll(orderDBAdapter.search(searchWord, loadItemOffset, countLoad,type));
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
    public  ArrayList<BoInvoice> searchInInvoiceList(String hint){
        ArrayList<BoInvoice>newInvoiceList=new ArrayList<>();
        for (int i =0;i<invoiceList.size();i++){
            BoInvoice boInvoice = invoiceList.get(i);
            JSONObject documentData = boInvoice.getDocumentsData();
            try {
                JSONObject customerJson = documentData.getJSONObject("customer");
                if(customerJson.getString("firstName").contains(hint)||customerJson.getString("lastName").contains(hint)||documentData.getString("date").contains(hint)){
                    newInvoiceList.add(boInvoice);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return newInvoiceList;
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
                    Log.d("objictListTEst",  OrdersManagementActivity.invoiceList.toString()+"");


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
