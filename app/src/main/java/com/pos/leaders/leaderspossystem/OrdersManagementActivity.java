package com.pos.leaders.leaderspossystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.ChecksDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Printer.InvoiceImg;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.SaleManagementListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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

    private final static int DAY_MINUS_ONE_SECOND = 86399999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sales_management);
        TitleBar.setTitleBar(this);
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
        adapter = new SaleManagementListViewAdapter(this, R.layout.list_adapter_row_sales_management, _saleList);

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
                final Order sale = _saleList.get(position);
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
                        long sID = saleDBAdapter.insertEntry(SESSION._EMPLOYEE.getEmployeeId(), new Timestamp(System.currentTimeMillis()), sale.getReplacementNote(), true, sale.getTotalPrice() * -1, sale.getTotalPaidAmount() * -1, sale.getCustomerId(), sale.getCustomer_name());

                        saleDBAdapter.close();
                        PaymentDBAdapter paymentDBAdapter1 = new PaymentDBAdapter(OrdersManagementActivity.this);
                        paymentDBAdapter1.open();
                        paymentDBAdapter1.insertEntry(sale.getPayment().getPaymentWay(), sale.getTotalPrice() * -1, sID);
                        paymentDBAdapter1.close();
                        //// TODO: 19/01/2017 cancel this sale and print return note and mony back by the payment way
                    }
                });
                //endregion Cancellation ORDER Button

                previousView.setBackgroundColor(getResources().getColor(R.color.list_background_color));
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
        /*POSInterfaceAPI posInterfaceAPI = new POSUSBAPI(SalesManagementActivity.this);

        int i = posInterfaceAPI.OpenDevice();
        POSSDK pos = new POSSDK(posInterfaceAPI);

        pos.textSelectCharSetAndCodePage(POSSDK.CharacterSetUSA, 15);
        pos.systemSelectPrintMode(0);
        pos.systemFeedLine(1);
        pos.imageStandardModeRasterPrint(bitmap, CONSTANT.PRINTER_PAGE_WIDTH);
        pos.systemFeedLine(3);
        pos.systemCutPaper(66, 0);

        posInterfaceAPI.CloseDevice();*/
    }

}
/**class OutcomeDescComparator implements Comparator<ORDER>
{
    public int compare(ORDER fSale, ORDER lSale) {
        return lSale.getOrder_date().compareTo(fSale.getOrder_date());
    }
}*/
