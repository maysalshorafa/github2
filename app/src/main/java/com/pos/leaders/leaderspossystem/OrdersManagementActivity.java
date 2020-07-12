package com.pos.leaders.leaderspossystem;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import com.pos.leaders.leaderspossystem.CustomerAndClub.AddNewCustomer;
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
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportCountDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyOperation;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyReturns;
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Models.ZReportCount;
import com.pos.leaders.leaderspossystem.Printer.InvoiceImg;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;
import com.pos.leaders.leaderspossystem.Printer.PrinterTools;
import com.pos.leaders.leaderspossystem.Printer.SUNMI_T1.AidlUtil;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.ConverterCurrency;
import com.pos.leaders.leaderspossystem.Tools.CustomerCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.DocumentControl;
import com.pos.leaders.leaderspossystem.Tools.PrinterType;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.SaleManagementListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SendLog;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.pos.leaders.leaderspossystem.Tools.SendLog.sendLogFile;

/**
 * Created by KARAM on 26/10/2016.
 * Editing by KARAM on 10/04/2017.
 */
public class OrdersManagementActivity extends AppCompatActivity {
    public static  double customerWallet=0;
    Button btSendToEmail;
    int loadItemOffset = 0;
    int countLoad = 60;
    Long customerId;
    String searchWord = "";
    public static  ListView lvOrders;
    OrderDBAdapter orderDBAdapter;
    EmployeeDBAdapter userDBAdapter;
    PaymentDBAdapter paymentDBAdapter;
    EditText etSearch ,customer_id;
    String reciveEmail="";
    public static  SaleManagementListViewAdapter adapter;
    View previousView = null;

    List<Order> _saleList;
    List<OrderDetails> orders;
    List<Check> checks;
    public static  List<Order>filterOrderList;
    List<BoInvoice>filterBoInvoice;
    public static List<BoInvoice>invoiceList=new ArrayList<>();
    public static Context context = null;
    public static   List<Object>list=new ArrayList<Object>();
    Spinner searchSpinner;
    final Calendar myCalendar = Calendar.getInstance();
    List<Object>objectList = new ArrayList<Object>();
    OrderDetailsDBAdapter orderDetailsDBAdapter;
    private final static int DAY_MINUS_ONE_SECOND = 86399999;
    public static  GridView gvCustomer ;
    boolean userScrolled =false;
    public static  Customer customer;
    List<Customer> customerList = new ArrayList<>();
    List<Customer> AllCustmerList = new ArrayList<>();
    CustomerDBAdapter customerDBAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sales_management);
        TitleBar.setTitleBar(this);
        ThisApp.setCurrentActivity(this);
        context=this;
        btSendToEmail = (Button)findViewById(R.id.salesHistory_btToEmail);
        lvOrders = (ListView) findViewById(R.id.saleManagement_LVSales);

        etSearch = (EditText) findViewById(R.id.etSearch);
        searchSpinner=(Spinner)findViewById(R.id.searchSpinner);
        orderDetailsDBAdapter=new OrderDetailsDBAdapter(OrdersManagementActivity.this);
        orderDetailsDBAdapter.open();
        customerDBAdapter=new CustomerDBAdapter(OrdersManagementActivity.this);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        final ArrayList<Integer> idForSearchType = new ArrayList<Integer>();

        final ArrayList<String> hintForSearchType = new ArrayList<String>();
        hintForSearchType.add(getString(R.string.all));
        hintForSearchType.add(getString(R.string.customer));
        hintForSearchType.add(getString(R.string.sale_id));
        hintForSearchType.add(getString(R.string.date));
        hintForSearchType.add(getString(R.string.price));
        hintForSearchType.add(getString(R.string.type));
        hintForSearchType.add(getString(R.string.serial_no));

        idForSearchType.add(0);
        idForSearchType.add(1);
        idForSearchType.add(2);
        idForSearchType.add(3);
        idForSearchType.add(4);
        idForSearchType.add(5);
        idForSearchType.add(6);

        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hintForSearchType);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(adapter1);

        orderDBAdapter = new OrderDBAdapter(this);

        userDBAdapter = new EmployeeDBAdapter(this);
        paymentDBAdapter = new PaymentDBAdapter(this);

        orderDBAdapter.open();

        _saleList = orderDBAdapter.lazyLoad(loadItemOffset, countLoad);

        orderDBAdapter.close();
        for (Order s : _saleList) {
            userDBAdapter.open();
            s.setUser(userDBAdapter.getEmployeeByID(s.getByUser()));
            userDBAdapter.close();

            paymentDBAdapter.open();
            if (paymentDBAdapter.getPaymentBySaleID(s.getOrderId()).size() > 0) {
                try {
                    s.setPayment(paymentDBAdapter.getPaymentBySaleID(s.getOrderId()).get(0));
                } catch (Exception ex) {
                    //_saleList.remove(s);
                    ex.printStackTrace();
                }
                paymentDBAdapter.close();
            }
        }
        /*for (ORDER s : saleList) {
			if(DateConverter.dateBetweenTwoDates(from, to, s.getOrder_date())) {
				_saleList.add(s);
			}
		}*/


        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.list_adapter_head_row_order, lvOrders, false);
        lvOrders.addHeaderView(header, null, false);
        objectList.addAll(_saleList);
        list=objectList;
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
                    LoadMore();
                    Log.d("loadmore",loadItemOffset+"");
                }
            }
        });


        btSendToEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog customerDialog = new Dialog(OrdersManagementActivity.this);
                customerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customerDialog.show();
                customerDialog.setContentView(R.layout.customer_email_layout);
                final EditText   customer_email = (EditText) customerDialog.findViewById(R.id.customer_email);
                ((Button) customerDialog.findViewById(R.id.done))
                        .setOnClickListener(new View.OnClickListener() {

                            @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                            public void onClick(View arg0) {
                                if(customer_email.getText().toString()!=""){
                                    reciveEmail=customer_email.getText().toString();
                                    new AsyncTask<Void, Void, Void>(){
                                        @Override
                                        protected void onPreExecute() {
                                            super.onPreExecute();
                                        }
                                        @Override
                                        protected void onPostExecute(Void aVoid) {



                                            try
                                            {
                                                SendLog.sendListFile(reciveEmail,context.getPackageName());
                                                //pdfLoadImages1(data);
                                            }
                                            catch(Exception ignored) {


                                            }}

                                        @Override
                                        protected Void doInBackground(Void... voids) {
                                            try {
                                                PdfUA pdfUA = new PdfUA();

                                                try {
                                                    pdfUA.printCustomerInvoicesReport(context,list,customerWallet);
                                                } catch (DocumentException e) {
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    Thread.sleep(100);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }



                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            return null;
                                        }
                                    }.execute();
                                    customerDialog.dismiss();
                                }


                            }
                        });


                customer_email.setText(customer.getEmail());




            }
        });


        lvOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d("testInvoice",list.size()+"" +position);
                if(list.get(position-1) instanceof  Order){
                    final Order sale = (Order) list.get(position-1);
                    Log.d("testttt",sale.toString());

                    CustomerDBAdapter customerDBAdapter = new CustomerDBAdapter(OrdersManagementActivity.this);
                    customerDBAdapter.open();
                    Customer customer=null;
                    if(sale.getCustomerId()==0){
                        customer= customerDBAdapter.getCustomerByName("guest");
                    }else {
                        customer =customerDBAdapter.getCustomerByID(sale.getCustomerId());
                    }
                    sale.setCustomer(customer);
                    final OrderDetailsDBAdapter orderDBAdapter = new OrderDetailsDBAdapter(OrdersManagementActivity.this);
                    orderDBAdapter.open();
                    orders = orderDBAdapter.getOrderBySaleID(sale.getOrderId());
                    orderDBAdapter.close();
                    final ProductDBAdapter productDBAdapter = new ProductDBAdapter(OrdersManagementActivity.this);
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

                    if(payments.size()>0) {
                        sale.setPayment(payments.get(0));
                    }
                    paymentDBAdapter.close();

                    checks = new ArrayList<Check>();

                    for (Payment p : payments) {
                        //  switch (p.getPaymentWay()) {
                        ChecksDBAdapter checksDBAdapter = new ChecksDBAdapter(OrdersManagementActivity.this);
                        checksDBAdapter.open();
                        checks.addAll(checksDBAdapter.getPaymentBySaleID(sale.getOrderId()));
                        checksDBAdapter.close();
                         /*   case CONSTANT.CHECKS:
                                ChecksDBAdapter checksDBAdapter = new ChecksDBAdapter(OrdersManagementActivity.this);
                                checksDBAdapter.open();
                                checks.addAll(checksDBAdapter.getPaymentBySaleID(sale.getOrderId()));
                                checksDBAdapter.close();
                                break;
                            case CONSTANT.CASH:
                                break;
                            case CONSTANT.CREDIT_CARD:
                                break;*/
                        // }
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

                    final Button duplicate = (Button) view.findViewById(R.id.listSaleManagement_BTDuplicate);

                    duplicate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //send customerName copy from the in voice

                            if (SETTINGS.enableDuplicateInvoice){
                                new AlertDialog.Builder(OrdersManagementActivity.this)
                                        .setTitle(getString(R.string.copyinvoice))
                                        .setMessage(getString(R.string.print_copy_invoice))
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                OrderDBAdapter oDb = new OrderDBAdapter(OrdersManagementActivity.this);
                                                oDb.open();
                                                OrderDetailsDBAdapter orderDetailsDBAdapter = new OrderDetailsDBAdapter(OrdersManagementActivity.this);
                                                orderDetailsDBAdapter.open();
                                                CashPaymentDBAdapter cashPaymentDBAdapter=new CashPaymentDBAdapter(OrdersManagementActivity.this);
                                                cashPaymentDBAdapter.open();
                                                PaymentDBAdapter paymentDBAdapter =new PaymentDBAdapter(OrdersManagementActivity.this);
                                                paymentDBAdapter.open();
                                                CurrencyOperationDBAdapter  currencyOperationDBAdapter=new CurrencyOperationDBAdapter(OrdersManagementActivity.this);
                                                currencyOperationDBAdapter.open();
                                                CurrencyReturnsDBAdapter  currencyReturnsDBAdapter=new CurrencyReturnsDBAdapter(OrdersManagementActivity.this);
                                                currencyReturnsDBAdapter.open();
                                                List<OrderDetails>orderDetailsList=orderDetailsDBAdapter.getOrderBySaleID(sale.getOrderId());

                                                List<CashPayment>cashPaymentList=cashPaymentDBAdapter.getPaymentBySaleID(sale.getOrderId());
                                                List<Payment> paymentList =paymentDBAdapter.getPaymentBySaleID(sale.getOrderId());
                                                List<CurrencyOperation>currencyOperationList=currencyOperationDBAdapter.getCurrencyOperationByOrderID(sale.getOrderId());
                                                List<CurrencyReturns>currencyReturnsList=currencyReturnsDBAdapter.getCurencyReturnBySaleID(sale.getOrderId());
                                                Log.d("copyOrder",sale.toString());
                                                long orderId= oDb.insertEntryDuplicate(sale);
                                                for (int i=0;i<orderDetailsList.size();i++){
                                                    orderDetailsDBAdapter.open();
                                                    OrderDetails o =orderDetailsList.get(i);
                                                    o.setOrderId(orderId);
                                                    orderDetailsDBAdapter.insertEntryDuplicate(o);
                                                    orderDetailsDBAdapter.close();
                                                }
                                                for (int i=0;i<cashPaymentList.size();i++){
                                                    cashPaymentDBAdapter.open();
                                                    CashPayment cashPayment=cashPaymentList.get(i);
                                                    cashPayment.setOrderId(orderId);
                                                    cashPaymentDBAdapter.insertEntryDuplicate(cashPayment);
                                                    cashPaymentDBAdapter.close();
                                                }
                                                for (int i=0;i<paymentList.size();i++){
                                                    paymentDBAdapter.open();
                                                    Payment payment=paymentList.get(i);
                                                    payment.setOrderId(orderId);
                                                    paymentDBAdapter.insertEntryDuplicate(payment);
                                                    paymentDBAdapter.close();
                                                }
                                                for (int i=0;i<currencyOperationList.size();i++){
                                                    currencyOperationDBAdapter.open();
                                                    CurrencyOperation currencyOperation=currencyOperationList.get(i);
                                                    currencyOperation.setOperationId(orderId);
                                                    currencyOperationDBAdapter.insertEntryDuplicate(currencyOperation);
                                                    currencyOperationDBAdapter.close();
                                                }
                                                for (int i=0;i<currencyReturnsList.size();i++){
                                                    currencyReturnsDBAdapter.open();
                                                    CurrencyReturns currencyReturns=currencyReturnsList.get(i);
                                                    currencyReturns.setOrderId(orderId);
                                                    currencyReturnsDBAdapter.insertEntryDuplicate(currencyReturns);
                                                    currencyReturnsDBAdapter.close();
                                                }
                                                oDb.open();
                                                orderDetailsDBAdapter.open();
                                                Order order1 = oDb.getOrderById(orderId);
                                                SESSION._TEMP_ORDERS=order1;
                                                SESSION._TEMP_ORDER_DETAILES=orderDetailsDBAdapter.getOrderBySaleID(order1.getOrderId());
                                                ZReportDBAdapter zReportDBAdapter1 = new ZReportDBAdapter(OrdersManagementActivity.this);
                                                zReportDBAdapter1.open();
                                                ZReportCountDbAdapter zReportCountDbAdapter1 = new ZReportCountDbAdapter(OrdersManagementActivity.this);
                                                zReportCountDbAdapter1.open();
                                                ZReportCount zReportCount1=null;
                                                ZReport z=null;
                                                try {
                                                    zReportCount1 = zReportCountDbAdapter1.getLastRow();
                                                    z= zReportDBAdapter1.getLastRow();

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                                double SalesWitheTaxDuplicute=0,SalesWithoutTaxDuplicute=0,salesaftertaxDuplicute=0;
                                                for (int i=0;i<orderDetailsList.size();i++){
                                                    productDBAdapter.open();
                                                    Product product = productDBAdapter.getProductByID(orderDetailsList.get(i).getProductId());
                                                    //     if (product.getCurrencyType()==0){
                                                    double rateCurrency=ConverterCurrency.getRateCurrency(product.getCurrencyType(),OrdersManagementActivity.this);
                                                    if(!product.isWithTax()){
                                                        if(order1.getCartDiscount()>0){
                                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order1.getCartDiscount()/100))))));
                                                            SalesWithoutTaxDuplicute+=(orderDetailsList.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                            Log.d("SalesWithoutTaxCancle",SalesWithoutTaxDuplicute+"f");
                                                        }else {
                                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(orderDetailsList.get(i).getPaidAmount())));
                                                            SalesWithoutTaxDuplicute += (orderDetailsList.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                            Log.d("SalesWithoutTaxCancle",SalesWithoutTaxDuplicute+"f");
                                                        }
                                                    }else {
                                                        if(order1.getCartDiscount()>0){

                                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order1.getCartDiscount()/100)))/ (1 + (SETTINGS.tax / 100)))));
                                                            Log.d("salesaftertax", orderDetailsList.get(i).getPaidAmountAfterTax()+"ko2333"+orderDetailsList.get(i).getPaidAmount()+"ko2333"+(order1.getCartDiscount()/100));
                                                            salesaftertaxDuplicute+=((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order1.getCartDiscount()/100)))*rateCurrency);
                                                            Log.d("salesaftertax",salesaftertaxDuplicute+"ko22222");
                                                            SalesWitheTaxDuplicute+=(orderDetailsList.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                        }else {
                                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(orderDetailsList.get(i).getPaidAmount() / (1 + (SETTINGS.tax / 100)))));
                                                            salesaftertaxDuplicute+=(orderDetailsList.get(i).getPaidAmount()*rateCurrency);
                                                            Log.d("salesaftertax",salesaftertaxDuplicute+"ko");
                                                            SalesWitheTaxDuplicute+=(orderDetailsList.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                        }
                                                    }
                                                    //    }

                                            /*  else   if (product.getCurrencyType()==1){
                                                    if(product.isWithTax()){
                                                        if(order1.getCartDiscount()>0){
                                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order1.getCartDiscount()/100))))));
                                                            SalesWithoutTaxDuplicute+=(orderDetailsList.get(i).getPaidAmountAfterTax()*3.491);
                                                            Log.d("SalesWithoutTaxCancle",SalesWithoutTaxDuplicute+"f");
                                                        }else {
                                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(orderDetailsList.get(i).getPaidAmount())));
                                                            SalesWithoutTaxDuplicute += (orderDetailsList.get(i).getPaidAmountAfterTax()*3.491);
                                                            Log.d("SalesWithoutTaxCancle",SalesWithoutTaxDuplicute+"f");
                                                        }
                                                    }else {
                                                        if(order1.getCartDiscount()>0){

                                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order1.getCartDiscount()/100)))/ (1 + (SETTINGS.tax / 100)))));
                                                            Log.d("salesaftertax", orderDetailsList.get(i).getPaidAmountAfterTax()+"ko2333"+orderDetailsList.get(i).getPaidAmount()+"ko2333"+(order1.getCartDiscount()/100));
                                                            salesaftertaxDuplicute+=((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order1.getCartDiscount()/100)))*3.491);
                                                            Log.d("salesaftertax",salesaftertaxDuplicute+"ko22222");
                                                            SalesWitheTaxDuplicute+=(orderDetailsList.get(i).getPaidAmountAfterTax()*3.491);
                                                        }else {
                                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(orderDetailsList.get(i).getPaidAmount() / (1 + (SETTINGS.tax / 100)))));
                                                            salesaftertaxDuplicute+=(orderDetailsList.get(i).getPaidAmount()*3.491);
                                                            Log.d("salesaftertax",salesaftertaxDuplicute+"ko");
                                                            SalesWitheTaxDuplicute+=(orderDetailsList.get(i).getPaidAmountAfterTax()*3.491);
                                                        }
                                                    }
                                                }

                                                else   if (product.getCurrencyType()==2){
                                                    if(product.isWithTax()){
                                                        if(order1.getCartDiscount()>0){
                                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order1.getCartDiscount()/100))))));
                                                            SalesWithoutTaxDuplicute+=(orderDetailsList.get(i).getPaidAmountAfterTax()*4.5974);
                                                            Log.d("SalesWithoutTaxCancle",SalesWithoutTaxDuplicute+"f");
                                                        }else {
                                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(orderDetailsList.get(i).getPaidAmount())));
                                                            SalesWithoutTaxDuplicute += (orderDetailsList.get(i).getPaidAmountAfterTax()*4.5974);
                                                            Log.d("SalesWithoutTaxCancle",SalesWithoutTaxDuplicute+"f");
                                                        }
                                                    }else {
                                                        if(order1.getCartDiscount()>0){

                                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order1.getCartDiscount()/100)))/ (1 + (SETTINGS.tax / 100)))));
                                                            Log.d("salesaftertax", orderDetailsList.get(i).getPaidAmountAfterTax()+"ko2333"+orderDetailsList.get(i).getPaidAmount()+"ko2333"+(order1.getCartDiscount()/100));
                                                            salesaftertaxDuplicute+=((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order1.getCartDiscount()/100)))*3.491);
                                                            Log.d("salesaftertax",salesaftertaxDuplicute+"ko22222");
                                                            SalesWitheTaxDuplicute+=(orderDetailsList.get(i).getPaidAmountAfterTax()*4.5974);
                                                        }else {
                                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(orderDetailsList.get(i).getPaidAmount() / (1 + (SETTINGS.tax / 100)))));
                                                            salesaftertaxDuplicute+=(orderDetailsList.get(i).getPaidAmount()*4.5974);
                                                            Log.d("salesaftertax",salesaftertaxDuplicute+"ko");
                                                            SalesWitheTaxDuplicute+=(orderDetailsList.get(i).getPaidAmountAfterTax()*4.5974);
                                                        }
                                                    }
                                                }


                                                else   if (product.getCurrencyType()==3){
                                                    if(product.isWithTax()){
                                                        if(order1.getCartDiscount()>0){
                                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order1.getCartDiscount()/100))))));
                                                            SalesWithoutTaxDuplicute+=(orderDetailsList.get(i).getPaidAmountAfterTax()*4.1002);
                                                            Log.d("SalesWithoutTaxCancle",SalesWithoutTaxDuplicute+"f");
                                                        }else {
                                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(orderDetailsList.get(i).getPaidAmount())));
                                                            SalesWithoutTaxDuplicute += (orderDetailsList.get(i).getPaidAmountAfterTax()*4.1002);
                                                            Log.d("SalesWithoutTaxCancle",SalesWithoutTaxDuplicute+"f");
                                                        }
                                                    }else {
                                                        if(order1.getCartDiscount()>0){

                                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order1.getCartDiscount()/100)))/ (1 + (SETTINGS.tax / 100)))));
                                                            Log.d("salesaftertax", orderDetailsList.get(i).getPaidAmountAfterTax()+"ko2333"+orderDetailsList.get(i).getPaidAmount()+"ko2333"+(order1.getCartDiscount()/100));
                                                            salesaftertaxDuplicute+=((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order1.getCartDiscount()/100)))*3.491);
                                                            Log.d("salesaftertax",salesaftertaxDuplicute+"ko22222");
                                                            SalesWitheTaxDuplicute+=(orderDetailsList.get(i).getPaidAmountAfterTax()*4.1002);
                                                        }else {
                                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(orderDetailsList.get(i).getPaidAmount() / (1 + (SETTINGS.tax / 100)))));
                                                            salesaftertaxDuplicute+=(orderDetailsList.get(i).getPaidAmount()*4.1002);
                                                            Log.d("salesaftertax",salesaftertaxDuplicute+"ko");
                                                            SalesWitheTaxDuplicute+=(orderDetailsList.get(i).getPaidAmountAfterTax()*4.1002);
                                                        }
                                                    }
                                                }*/


                                                }
                                                z.setCashTotal(z.getCashTotal()+sale.getTotalPrice());
                                                z.setInvoiceReceiptAmount(z.getInvoiceReceiptAmount()+sale.getTotalPrice());
                                                z.setFirstTypeAmount(z.getFirstTypeAmount()+sale.getTotalPrice());
                                                z.setSalesWithTax(Double.parseDouble(Util.makePrice(z.getSalesWithTax()+SalesWitheTaxDuplicute)));
                                                z.setSalesBeforeTax(Double.parseDouble(Util.makePrice(z.getSalesBeforeTax()+SalesWithoutTaxDuplicute)));
                                                z.setTotalTax(Double.parseDouble(Util.makePrice(z.getTotalTax()+(salesaftertaxDuplicute - SalesWitheTaxDuplicute))));
                                                zReportCount1.setCashCount(zReportCount1.getCashCount()+1);
                                                zReportCount1.setInvoiceReceiptCount(zReportCount1.getInvoiceReceiptCount()+1);
                                                zReportCount1.setFirstTYpeCount(zReportCount1.getFirstTYpeCount()+1);
                                                zReportDBAdapter1.updateEntry(z);
                                                zReportCountDbAdapter1.updateEntry(zReportCount1);
                                                Activity a=getParent();
                                                PrinterTools.printAndOpenCashBox("", "", "", 600,OrdersManagementActivity.this,a);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            } else {
                                new android.support.v7.app.AlertDialog.Builder(OrdersManagementActivity.this)
                                        .setTitle(getString(R.string.copyinvoice))
                                        .setMessage("يرجى تفعيلها من اعدادات النظام")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

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
                        }
                    });
                    //region Print Button
                    final Button print = (Button) view.findViewById(R.id.listSaleManagement_BTPrint);
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
                                                    //  Log.d("ItemId",sale.getOrderId()+"");
                                                    Intent i = new Intent(OrdersManagementActivity.this, SalesHistoryCopySales.class);
                                                    SETTINGS.copyInvoiceBitMap = invoiceImg.normalInvoice(sale.getOrderId(), orders, sale, true, SESSION._EMPLOYEE, checks,"");
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

                                                    SESSION._TEMP_ORDER_DETAILES_COPY=orders;
                                                    SESSION._TEMP_ORDERS_COPY=sale;

                                                    //   Log.d("ItemId",sale.getOrderId()+"");
                                                    if(SETTINGS.printer.equals(PrinterType.SUNMI_T1)){

                                                        printAndOpenCashBoxSUNMI_T1("","","");


                                                    }else {
                                                        PrinterTools.printAndOpenCashBoxForCopy("", "", "", 600,OrdersManagementActivity.this,getParent());
                                                    }
                                                    //  SESSION._TEMP_ORDER_DETAILES=new ArrayList<OrderDetails>();
                                                    //   SESSION._TEMP_ORDERS=new Order();
                                                    /**Customer customer1 =sale.getCustomer();
                                                     Intent i = new Intent(OrdersManagementActivity.this, SalesHistoryCopySales.class);
                                                     SETTINGS.copyInvoiceBitMap =invoiceImg.copyInvoice(sale.getOrderId(), orders, sale, true, SESSION._EMPLOYEE, null);
                                                     startActivity(i);**/
                                                }catch (Exception e){
                                                    Log.d("exception",sale.toString());
                                                    Log.d("exception",e.toString());
                                                    e.printStackTrace();
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

                    final Button btnCan = (Button) view.findViewById(R.id.listSaleManagement_BTCancel);
                    if(sale.getCancellingOrderId()>0){
                        btnCan.setVisibility(View.GONE);
                    }else {
                        btnCan.setVisibility(View.VISIBLE);
                    }

                    btnCan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btnCan.setEnabled(false);
                            ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(OrdersManagementActivity.this);
                            zReportDBAdapter.open();
                            ZReportCountDbAdapter zReportCountDbAdapter = new ZReportCountDbAdapter(OrdersManagementActivity.this);
                            zReportCountDbAdapter.open();
                            ZReportCount zReportCount=null;
                            ZReport zReport1=null;
                            try {
                                zReportCount = zReportCountDbAdapter.getLastRow();
                                zReport1 = zReportDBAdapter.getLastRow();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            OrderDBAdapter saleDBAdapter = new OrderDBAdapter(OrdersManagementActivity.this);
                            saleDBAdapter.open();
                            if(payments.size()>0) {
                                sale.setPayment(new Payment(payments.get(0)));
                            }
                            long sID = saleDBAdapter.insertEntry(SESSION._EMPLOYEE.getEmployeeId(), new Timestamp(System.currentTimeMillis()), sale.getReplacementNote(), true, sale.getTotalPrice() * -1, sale.getTotalPaidAmount() * -1, sale.getCustomerId(), sale.getCustomer_name(),sale.getCartDiscount(),sale.getNumberDiscount(),sale.getOrderId(),0,0,0);
                            Order order = saleDBAdapter.getOrderById(sID);
                            sale.setCancellingOrderId(sID);
                            saleDBAdapter.updateEntry(sale);
                            PaymentDBAdapter paymentDBAdapter1 = new PaymentDBAdapter(OrdersManagementActivity.this);
                            paymentDBAdapter1.open();
                            paymentDBAdapter1.insertEntry( sale.getTotalPrice() * -1, sID,order.getOrderKey());
                            paymentDBAdapter1.close();
                            CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(OrdersManagementActivity.this);
                            cashPaymentDBAdapter.open();
                            cashPaymentDBAdapter.insertEntry(sID,sale.getTotalPrice() * -1,0,new Timestamp(System.currentTimeMillis()),1,1);
                            CurrencyOperationDBAdapter currencyOperationDBAdapter = new CurrencyOperationDBAdapter(OrdersManagementActivity.this);
                            currencyOperationDBAdapter.open();
                            CurrencyReturnsDBAdapter currencyReturnsDBAdapter =new CurrencyReturnsDBAdapter(OrdersManagementActivity.this);
                            currencyReturnsDBAdapter.open();
                            List<OrderDetails>orderDetailsList=orderDetailsDBAdapter.getOrderBySaleID(sale.getOrderId());
                            if(SETTINGS.enableCurrencies){
                                currencyOperationDBAdapter.insertEntry(new Timestamp(System.currentTimeMillis()),sID,CONSTANT.DEBIT,sale.getTotalPaidAmount() * -1,"ILS",CONSTANT.CASH);
                                currencyReturnsDBAdapter.insertEntry(sale.getOrderId(),(sale.getTotalPaidAmount()-sale.getTotalPrice())*-1,new Timestamp(System.currentTimeMillis()),0);
                            }
                            if (checks.size() > 0)
                                try {
                                    Intent i = new Intent(OrdersManagementActivity.this, SalesHistoryCopySales.class);
                                    SETTINGS.copyInvoiceBitMap =invoiceImg.cancelingInvoice(order,orderDetailsList,false,checks);
                                    startActivity(i);
                                }catch (Exception e){
                                    Log.d("exception",order.toString());
                                    Log.d("exception",e.toString());
                                    sendLogFile();
                                }
                            else
                                try {
                                    Intent i = new Intent(OrdersManagementActivity.this, SalesHistoryCopySales.class);
                                    SETTINGS.copyInvoiceBitMap =invoiceImg.cancelingInvoice(order,orderDetailsList,false,null);
                                    startActivity(i);
                                }catch (Exception e){
                                    Log.d("exception",order.toString());
                                    Log.d("exception",e.toString());
                                    sendLogFile();
                                }
                            double SalesWitheTaxCancle=0,SalesWithoutTaxCancle=0,salesaftertaxCancle=0;
                            for (int i=0;i<orderDetailsList.size();i++){
                                productDBAdapter.open();
                                Product product = productDBAdapter.getProductByID(orderDetailsList.get(i).getProductId());
                                // if (product.getCurrencyType()==0){
                                double rateCurrency=ConverterCurrency.getRateCurrency(product.getCurrencyType(),OrdersManagementActivity.this);
                                if(!product.isWithTax()){
                                    if(order.getCartDiscount()>0){
                                        orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order.getCartDiscount()/100))))));
                                        SalesWithoutTaxCancle+=(orderDetailsList.get(i).getPaidAmountAfterTax()*rateCurrency);
                                        Log.d("SalesWithoutTaxCancle",SalesWithoutTaxCancle+"f");
                                    }else {
                                        orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(orderDetailsList.get(i).getPaidAmount())));
                                        SalesWithoutTaxCancle += (orderDetailsList.get(i).getPaidAmountAfterTax()*rateCurrency);
                                        Log.d("SalesWithoutTaxCancle",SalesWithoutTaxCancle+"f");
                                    }
                                }else {
                                    if(order.getCartDiscount()>0){

                                        orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order.getCartDiscount()/100)))/ (1 + (SETTINGS.tax / 100)))));
                                        Log.d("salesaftertax", orderDetailsList.get(i).getPaidAmountAfterTax()+"ko2333"+orderDetailsList.get(i).getPaidAmount()+"ko2333"+(order.getCartDiscount()/100));
                                        salesaftertaxCancle+=((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order.getCartDiscount()/100)))*rateCurrency);
                                        Log.d("salesaftertax",salesaftertaxCancle+"ko22222");
                                        SalesWitheTaxCancle+=(orderDetailsList.get(i).getPaidAmountAfterTax()*rateCurrency);
                                    }else {
                                        orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(orderDetailsList.get(i).getPaidAmount() / (1 + (SETTINGS.tax / 100)))));
                                        salesaftertaxCancle+=(orderDetailsList.get(i).getPaidAmount()*rateCurrency);
                                        Log.d("salesaftertax",salesaftertaxCancle+"ko");
                                        SalesWitheTaxCancle+=(orderDetailsList.get(i).getPaidAmountAfterTax()*rateCurrency);
                                    }
                                }
                                //}




                      /*      else     if (product.getCurrencyType()==1){
                                    if(product.isWithTax()){
                                        if(order.getCartDiscount()>0){
                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order.getCartDiscount()/100))))));
                                            SalesWithoutTaxCancle+=(orderDetailsList.get(i).getPaidAmountAfterTax()*3.491);
                                            Log.d("SalesWithoutTaxCancle",SalesWithoutTaxCancle+"f");
                                        }else {
                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(orderDetailsList.get(i).getPaidAmount())));
                                            SalesWithoutTaxCancle += (orderDetailsList.get(i).getPaidAmountAfterTax()*3.491);
                                            Log.d("SalesWithoutTaxCancle",SalesWithoutTaxCancle+"f");
                                        }
                                    }else {
                                        if(order.getCartDiscount()>0){

                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order.getCartDiscount()/100)))/ (1 + (SETTINGS.tax / 100)))));
                                            Log.d("salesaftertax", orderDetailsList.get(i).getPaidAmountAfterTax()+"ko2333"+orderDetailsList.get(i).getPaidAmount()+"ko2333"+(order.getCartDiscount()/100));
                                            salesaftertaxCancle+=((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order.getCartDiscount()/100)))*3.491);
                                            Log.d("salesaftertax",salesaftertaxCancle+"ko22222");
                                            SalesWitheTaxCancle+=(orderDetailsList.get(i).getPaidAmountAfterTax()*3.491);
                                        }else {
                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(orderDetailsList.get(i).getPaidAmount() / (1 + (SETTINGS.tax / 100)))));
                                            salesaftertaxCancle+=(orderDetailsList.get(i).getPaidAmount()*3.491);
                                            Log.d("salesaftertax",salesaftertaxCancle+"ko");
                                            SalesWitheTaxCancle+=(orderDetailsList.get(i).getPaidAmountAfterTax()*3.491);
                                        }
                                    }}


                                else     if (product.getCurrencyType()==2){
                                    if(product.isWithTax()){
                                        if(order.getCartDiscount()>0){
                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order.getCartDiscount()/100))))));
                                            SalesWithoutTaxCancle+=(orderDetailsList.get(i).getPaidAmountAfterTax()*4.5974);
                                            Log.d("SalesWithoutTaxCancle",SalesWithoutTaxCancle+"f");
                                        }else {
                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(orderDetailsList.get(i).getPaidAmount())));
                                            SalesWithoutTaxCancle += (orderDetailsList.get(i).getPaidAmountAfterTax()*4.5974);
                                            Log.d("SalesWithoutTaxCancle",SalesWithoutTaxCancle+"f");
                                        }
                                    }else {
                                        if(order.getCartDiscount()>0){

                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order.getCartDiscount()/100)))/ (1 + (SETTINGS.tax / 100)))));
                                            Log.d("salesaftertax", orderDetailsList.get(i).getPaidAmountAfterTax()+"ko2333"+orderDetailsList.get(i).getPaidAmount()+"ko2333"+(order.getCartDiscount()/100));
                                            salesaftertaxCancle+=((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order.getCartDiscount()/100)))*4.5974);
                                            Log.d("salesaftertax",salesaftertaxCancle+"ko22222");
                                            SalesWitheTaxCancle+=(orderDetailsList.get(i).getPaidAmountAfterTax()*4.5974);
                                        }else {
                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(orderDetailsList.get(i).getPaidAmount() / (1 + (SETTINGS.tax / 100)))));
                                            salesaftertaxCancle+=(orderDetailsList.get(i).getPaidAmount()*4.5974);
                                            Log.d("salesaftertax",salesaftertaxCancle+"ko");
                                            SalesWitheTaxCancle+=(orderDetailsList.get(i).getPaidAmountAfterTax()*4.5974);
                                        }
                                    }}

                                else     if (product.getCurrencyType()==3){
                                    if(product.isWithTax()){
                                        if(order.getCartDiscount()>0){
                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order.getCartDiscount()/100))))));
                                            SalesWithoutTaxCancle+=(orderDetailsList.get(i).getPaidAmountAfterTax()*4.1002);
                                            Log.d("SalesWithoutTaxCancle",SalesWithoutTaxCancle+"f");
                                        }else {
                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(orderDetailsList.get(i).getPaidAmount())));
                                            SalesWithoutTaxCancle += (orderDetailsList.get(i).getPaidAmountAfterTax()*4.1002);
                                            Log.d("SalesWithoutTaxCancle",SalesWithoutTaxCancle+"f");
                                        }
                                    }else {
                                        if(order.getCartDiscount()>0){

                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order.getCartDiscount()/100)))/ (1 + (SETTINGS.tax / 100)))));
                                            Log.d("salesaftertax", orderDetailsList.get(i).getPaidAmountAfterTax()+"ko2333"+orderDetailsList.get(i).getPaidAmount()+"ko2333"+(order.getCartDiscount()/100));
                                            salesaftertaxCancle+=((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(order.getCartDiscount()/100)))*4.1002);
                                            Log.d("salesaftertax",salesaftertaxCancle+"ko22222");
                                            SalesWitheTaxCancle+=(orderDetailsList.get(i).getPaidAmountAfterTax()*4.1002);
                                        }else {
                                            orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(orderDetailsList.get(i).getPaidAmount() / (1 + (SETTINGS.tax / 100)))));
                                            salesaftertaxCancle+=(orderDetailsList.get(i).getPaidAmount()*4.1002);
                                            Log.d("salesaftertax",salesaftertaxCancle+"ko");
                                            SalesWitheTaxCancle+=(orderDetailsList.get(i).getPaidAmountAfterTax()*4.1002);
                                        }
                                    }}*/


                            }
                            zReport1.setSalesWithTax(zReport1.getSalesWithTax()-SalesWitheTaxCancle);

                            zReport1.setSalesBeforeTax(zReport1.getSalesBeforeTax()-SalesWithoutTaxCancle);
                            Log.d("Tax",zReport1.getTotalTax()+"fji");
                            Log.d("Tax1",Double.parseDouble(Util.makePrice(zReport1.getTotalTax()+(salesaftertaxCancle - SalesWitheTaxCancle)))+"fji1");

                            zReport1.setTotalTax(Double.parseDouble(Util.makePrice(zReport1.getTotalTax()- (salesaftertaxCancle - SalesWitheTaxCancle))));

                            zReport1.setCashTotal(zReport1.getCashTotal()-sale.getTotalPrice());
                            zReport1.setInvoiceReceiptAmount(zReport1.getInvoiceReceiptAmount()-sale.getTotalPrice());
                            zReport1.setFirstTypeAmount(zReport1.getFirstTypeAmount()-sale.getTotalPrice());
                            zReportCount.setCashCount(zReportCount.getCashCount()-1);
                            //   zReportCount.setInvoiceReceiptCount(zReportCount.getInvoiceReceiptCount()-1);
                            zReportCount.setFirstTYpeCount(zReportCount.getFirstTYpeCount()-1);
                            zReportDBAdapter.updateEntry(zReport1);
                            zReportCountDbAdapter.updateEntry(zReportCount);
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
                    final Button print = (Button) view.findViewById(R.id.listSaleManagement_BTPrint);
                    print.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //send customerName copy from the in voice
                            new AlertDialog.Builder(OrdersManagementActivity.this)
                                    .setTitle(getString(R.string.copyinvoice))
                                    .setMessage(getString(R.string.print_copy_invoice))
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            BoInvoice boInvoice = (BoInvoice) list.get(position-1);
                                            try {
                                                if (boInvoice.getType().equals(DocumentType.RECEIPT)) {
                                                   DocumentControl.sendREc(getApplicationContext(), boInvoice, "");
                                                } else{

                                                    Intent i = new Intent(OrdersManagementActivity.this, SalesHistoryCopySales.class);
                                                    SETTINGS.copyInvoiceBitMap = invoiceImg.copyInvoice(boInvoice);
                                                    startActivity(i);
                                                }
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
                    final BoInvoice boInvoice = (BoInvoice) list.get(position-1);
                    Button btnRN = (Button) view.findViewById(R.id.listSaleManagement_BTReturn);

                    if(boInvoice.getType().getValue().equalsIgnoreCase(DocumentType.RECEIPT.getValue())){
                        btnRN.setVisibility(View.INVISIBLE);
                    }else {
                        btnRN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
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
                    }
                    Button btnCan = (Button) view.findViewById(R.id.listSaleManagement_BTCancel);
                    btnCan.setVisibility(View.INVISIBLE);
                    Button btnDublicate= (Button) view.findViewById(R.id.listSaleManagement_BTDuplicate);
                    btnDublicate.setVisibility(View.INVISIBLE);

                }
            }
        });
        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list=new ArrayList<Object>();
                filterOrderList = new ArrayList<Order>();
                invoiceList=new ArrayList<BoInvoice>();
                //etSearch.setText("");
                if(searchSpinner.getSelectedItem().toString().equals(getString(R.string.date))){
                    new DatePickerDialog(OrdersManagementActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }


                if(searchSpinner.getSelectedItem().toString().equals(context.getString(R.string.customer))){

                    callPopup();

                }
                else  {
                    filterOrderList = orderDBAdapter.search(etSearch.getText().toString(), loadItemOffset, countLoad, searchSpinner.getSelectedItem().toString());
                    list.addAll(filterOrderList);
                    SaleManagementListViewAdapter   adapter = new SaleManagementListViewAdapter(OrdersManagementActivity.this, R.layout.list_adapter_row_sales_management, list);
                    lvOrders.setAdapter(adapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
                invoiceList=new ArrayList<BoInvoice>();
                adapter.notifyDataSetChanged();
                final String word = etSearch.getText().toString();

                if (!word.equals("")) {
                    Log.d("testEtSearch",word+"999");

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
                            orderDBAdapter.open();
                            Log.d("teeest",params[0]);
                            List<Order>nerOrderWithSerialNo=new ArrayList<Order>();
                            if(type.equals(getString(R.string.serial_no))){
                                nerOrderWithSerialNo = orderDBAdapter.search(params[0], loadItemOffset,countLoad,getString(R.string.all));
                                for (int i=0;i<nerOrderWithSerialNo.size();i++){
                                    Order order = nerOrderWithSerialNo.get(i);
                                    List<OrderDetails>orderDetailsList = orderDetailsDBAdapter.getOrderBySaleID(order.getOrderId());
                                    for(int a=0;a<orderDetailsList.size();a++){
                                        if(orderDetailsList.get(a).getSerialNumber()==word){
                                            filterOrderList.add(nerOrderWithSerialNo.get(i));
                                        }
                                    }
                                }

                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    list.addAll(filterOrderList);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }.execute(word);
                } else {
                    filterOrderList = _saleList;
                    filterBoInvoice=invoiceList;
                    list.addAll(filterOrderList);
                    list.addAll(filterBoInvoice);
                    adapter = new SaleManagementListViewAdapter(OrdersManagementActivity.this, R.layout.list_adapter_row_sales_management, list);
                    lvOrders.setAdapter(adapter);
                }

            }
        });


    }
    private void callPopup() {
        list=new ArrayList<Object>();
        filterOrderList = new ArrayList<Order>();
        final Dialog customerDialog = new Dialog(OrdersManagementActivity.this);
        customerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customerDialog.show();
        customerDialog.setContentView(R.layout.pop_up);
        customer_id = (EditText) customerDialog.findViewById(R.id.customer_name);
        final GridView gvCustomer = (GridView) customerDialog.findViewById(R.id.popUp_gvCustomer);
        gvCustomer.setNumColumns(3);

        Button   btn_cancel = (Button) customerDialog.findViewById(R.id.btn_cancel);

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
                        Intent intent = new Intent(OrdersManagementActivity.this, AddNewCustomer.class);
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
        customerDBAdapter.open();
        customerList = customerDBAdapter.getTopCustomer(0, 50);
        customerDBAdapter.close();
        //  AllCustmerList = customerList;
        AllCustmerList = new ArrayList<Customer>(customerList);
        CustomerCatalogGridViewAdapter custmerCatalogGridViewAdapter = new CustomerCatalogGridViewAdapter(getApplicationContext(), customerList);

        gvCustomer.setAdapter(custmerCatalogGridViewAdapter);

        gvCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                customer= customerList.get(position);
                if(customer!=null) {
                    orderDBAdapter =new OrderDBAdapter(OrdersManagementActivity.this);
                    orderDBAdapter.open();
                    Log.d("customer",customer.toString());
                    StartInvoiceAndCreditInvoiceConnection startInvoiceConnection = new StartInvoiceAndCreditInvoiceConnection();
                    startInvoiceConnection.execute(String.valueOf(customer.getCustomerId()));
                    StartGetCustomerGeneralLedgerConnectionForOrderActivity customerW = new StartGetCustomerGeneralLedgerConnectionForOrderActivity();
                    customerW.execute(String.valueOf(customer.getCustomerId()));
                    //  list.addAll(invoiceList);

//
                    // adapter.notifyDataSetChanged();
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
                    if (AllCustmerList.size()!=0 && !AllCustmerList.isEmpty()) {
                        for (Customer c : AllCustmerList) {
                            if (c.getFirstName()!=null){
                                if(c.getFirstName().toLowerCase().contains(word.toLowerCase()) ||
                                        c.getLastName().toLowerCase().contains(word.toLowerCase()) ||
                                        c.getPhoneNumber().toLowerCase().contains(word.toLowerCase()) ||
                                        String.valueOf(c.getCustomerIdentity()).contains(word.toLowerCase())) {
                                    customerList.add(c);

                                }}
                        }
                    }} else {
                    customerList = AllCustmerList;
                }
                CustomerCatalogGridViewAdapter adapter = new CustomerCatalogGridViewAdapter(getApplicationContext(), customerList);
                gvCustomer.setAdapter(adapter);
                // Log.i("products", productList.toString());


            }
        });


    }
    private void LoadMore(){
        filterOrderList=new ArrayList<>();
        loadItemOffset += countLoad;
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setTitle(getBaseContext().getString(R.string.wait_for_finish));



        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {

                dialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                if(adapter==null){
                    adapter = new SaleManagementListViewAdapter(getApplicationContext(), R.layout.list_adapter_row_sales_management, objectList);
                    lvOrders.setAdapter(adapter);
                    Log.d("objectList",objectList.size()+"lvOrders");
                }

                dialog.cancel();
                userScrolled=false;
            }

            @Override
            protected Void doInBackground(Void... params) {
                Log.d("testEtSearch",searchWord+"888");
                //   final String type = searchSpinner.getSelectedItem().toString();
//                orderDBAdapter.open();
                if (!searchWord.equals("")) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // _saleList.addAll(orderDBAdapter.search(searchWord, loadItemOffset, countLoad, type));
                            // Stuff that updates the UI
                            adapter.notifyDataSetChanged();

                        }
                    });
                }
                else {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            orderDBAdapter=new OrderDBAdapter(getApplicationContext());
                            orderDBAdapter.open();
//                    searchWord = "";
                            filterOrderList.addAll(orderDBAdapter.lazyLoad(filterOrderList.size()-1, 80));
                            _saleList =  filterOrderList;
                            objectList.addAll(_saleList);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
//                orderDBAdapter.close();
                return null;
            }
        }.execute();


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
    private void updateLabel() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        etSearch.setText(String.format(new Locale("en"),format.format(myCalendar.getTime())));
    }
    private void printAndOpenCashBoxSUNMI_T1(String mainAns, final String mainMer, final String mainCli) {
        //AidlUtil.getInstance().connectPrinterService(this);
        // if (AidlUtil.getInstance().isConnect()) {
        final ProgressDialog dialog = new ProgressDialog(OrdersManagementActivity.this);
        dialog.setTitle(getApplicationContext().getString(R.string.wait_for_finish_printing));

        dialog.show();
        InvoiceImg invoiceImg = new InvoiceImg(getApplicationContext());
        byte b = 0;
        try {

            Bitmap bitmap = invoiceImg.copyNormalInvoice(SESSION._TEMP_ORDERS_COPY.getOrderId(), SESSION._TEMP_ORDER_DETAILES_COPY, SESSION._ORDERS, false, SESSION._EMPLOYEE, SESSION._CHECKS_HOLDER,mainMer);
            AidlUtil.getInstance().printBitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //cut
            AidlUtil.getInstance().print3Line();
            AidlUtil.getInstance().cut();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(mainMer!=""){

            Bitmap bitmap2 = invoiceImg.copyNormalInvoice(SESSION._TEMP_ORDERS_COPY.getOrderId(), SESSION._TEMP_ORDER_DETAILES_COPY, SESSION._TEMP_ORDERS_COPY, false, SESSION._EMPLOYEE, null,mainMer);
            AidlUtil.getInstance().printBitmap(bitmap2);
            try {
                //cut
                AidlUtil.getInstance().print3Line();
                AidlUtil.getInstance().cut();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            AidlUtil.getInstance().openCashBox();
        } catch (Exception e) {
            e.printStackTrace();
        }


        dialog.cancel();
  /*   } else {
            new android.support.v7.app.AlertDialog.Builder(getContext(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                    .setTitle(getContext().getString(R.string.printer))
                    .setMessage(getContext().getString(R.string.please_connect_the_printer))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            //Toast.makeText(this, "Printer Connect Error!", Toast.LENGTH_LONG).show();
        }*/
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
            String url = ApiURL.Documents+"/DocumentsForCustomer/"+customerId;
            String invoiceRes = messageTransmit.authGet(url, SESSION.token);
            JSONObject jsonObject = new JSONObject(invoiceRes);
            String msgData = jsonObject.getString(MessageKey.responseBody);
            Log.d("testApi",msgData.toString());
            if (msgData.startsWith("[")) {
                try {
                    JSONArray jsonArray = new JSONArray(msgData);
                    Log.d("jsonOrderMangmentAct",jsonArray.length()+"");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        msgData = jsonArray.getJSONObject(i).toString();
                        JSONObject msgDataJson = new JSONObject(msgData);
                        if (msgDataJson.getString("type").equals("INVOICE")) {
                            invoice = new BoInvoice(DocumentType.INVOICE, msgDataJson.getJSONObject("documentsData"), msgDataJson.getString("docNum"));
                            OrdersManagementActivity.invoiceList.add(invoice);


                        } else if (msgDataJson.getString("type").equals("CREDIT_INVOICE")) {
                            invoice = new BoInvoice(DocumentType.CREDIT_INVOICE, msgDataJson.getJSONObject("documentsData"), msgDataJson.getString("docNum"));
                            OrdersManagementActivity.invoiceList.add(invoice);

                        }
                        else if (msgDataJson.getString("type").equals("RECEIPT")) {
                            invoice = new BoInvoice(DocumentType.RECEIPT, msgDataJson.getJSONObject("documentsData"), msgDataJson.getString("docNum"));
                            OrdersManagementActivity.invoiceList.add(invoice);

                        }  else if (msgDataJson.getString("type").equals("INVOICE_RECEIPT")) {
                            invoice = new BoInvoice(DocumentType.INVOICE_RECEIPT, msgDataJson.getJSONObject("documentsData"), msgDataJson.getString("docNum"));
                            OrdersManagementActivity.invoiceList.add(invoice);

                        }

                    }

              Log.d("invoiceOrderListS", OrdersManagementActivity.invoiceList.size()+"");

                } catch (Exception e) {
                    Log.d("exception1", e.toString());
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
                    OrdersManagementActivity.list.addAll(OrdersManagementActivity.invoiceList);
                    SaleManagementListViewAdapter  adapter = new SaleManagementListViewAdapter(OrdersManagementActivity.context, R.layout.list_adapter_row_sales_management,OrdersManagementActivity.list );

                    OrdersManagementActivity.lvOrders.setAdapter(adapter);
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
class StartGetCustomerGeneralLedgerConnectionForOrderActivity extends AsyncTask<String,Void,String> {
    private MessageTransmit messageTransmit;
    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    BoInvoice invoice;
    StartGetCustomerGeneralLedgerConnectionForOrderActivity() {
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
            String url = "GeneralLedger/"+customerId;

            String invoiceRes = messageTransmit.authGet(url,SESSION.token);
            JSONObject jsonObject = new JSONObject(invoiceRes);
            String msgData = jsonObject.getString(MessageKey.responseBody);
            JSONObject response = new JSONObject(msgData);
            OrdersManagementActivity.customerWallet=response.getDouble("creditAmount");

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
