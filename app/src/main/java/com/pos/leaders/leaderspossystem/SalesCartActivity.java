package com.pos.leaders.leaderspossystem;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.CreditCard.CreditCardActivity;
import com.pos.leaders.leaderspossystem.CreditCard.MainCreditCardActivity;
import com.pos.leaders.leaderspossystem.CustomerAndClub.AddNewCustomer;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CategoryDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ChecksDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClubAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CreditCardPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyOperationDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerAssetDB;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PosInvoiceDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductOfferDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Sum_PointDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UsedPointDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ValueOfPointDB;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.Category;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.Club;
import com.pos.leaders.leaderspossystem.Models.CreditCardPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.Currency;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.Models.CustomerType;
import com.pos.leaders.leaderspossystem.Models.Documents;
import com.pos.leaders.leaderspossystem.Models.Employee;
import com.pos.leaders.leaderspossystem.Models.InvoiceStatus;
import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.OrderDocumentStatus;
import com.pos.leaders.leaderspossystem.Models.OrderDocuments;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Offers.OfferController;
import com.pos.leaders.leaderspossystem.Payment.MultiCurrenciesPaymentActivity;
import com.pos.leaders.leaderspossystem.Payment.PaymentTable;
import com.pos.leaders.leaderspossystem.Pinpad.PinpadActivity;
import com.pos.leaders.leaderspossystem.Printer.HPRT_TP805;
import com.pos.leaders.leaderspossystem.Printer.InvoiceImg;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;
import com.pos.leaders.leaderspossystem.Printer.SM_S230I.MiniPrinterFunctions;
import com.pos.leaders.leaderspossystem.Printer.SUNMI_T1.AidlUtil;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.CashActivity;
import com.pos.leaders.leaderspossystem.Tools.CreditCardTransactionType;
import com.pos.leaders.leaderspossystem.Tools.CustomerAssistantCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.CustomerCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.OldCashActivity;
import com.pos.leaders.leaderspossystem.Tools.ProductCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.SaleDetailsListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;
import com.pos.leaders.leaderspossystem.syncposservice.Service.SyncMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import HPRTAndroidSDK.HPRTPrinterHelper;
import POSAPI.POSInterfaceAPI;
import POSAPI.POSUSBAPI;
import POSSDK.POSSDK;

import static com.pos.leaders.leaderspossystem.Tools.CONSTANT.CASH;
import static com.pos.leaders.leaderspossystem.Tools.CONSTANT.CHECKS;
import static com.pos.leaders.leaderspossystem.Tools.CONSTANT.CREDIT_CARD;

/**
 * Created by Karam on 21/11/2016.
 */

public class SalesCartActivity extends AppCompatActivity {
    private static final int REQUEST_CASH_ACTIVITY_WITH_CURRENCY_CODE = 590;
    private static final int REQUEST_CASH_ACTIVITY_CODE = 600;
    private static final int REQUEST_CHECKS_ACTIVITY_CODE = 753;
    private static final int REQUEST_CREDIT_CARD_ACTIVITY_CODE = 801;
    private static final int REQUEST_PIN_PAD_ACTIVITY_CODE = 907;
    private static final int REQUEST_MULTI_CURRENCY_ACTIVITY_CODE = 444;
    private static final int REQUEST_CREDIT_ACTIVITY_CODE = 500;
    private static final int REQUEST_INVOICE = 900;

    public static final String COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE = "com_pos_leaders_cart_total_price";
    String transID = "";
    final InvoiceImg invoiceImg = new InvoiceImg(SalesCartActivity.this);
    TextView salesMan;
    private DrawerLayout drawerLayout;
    //private ActionBarDrawerToggle actionBarDrawerToggle;//
    // private NavigationView navigationView;

    //ImageButton    btnLastSales;
    Button btnPercentProduct, btnPauseSale, btnResumeSale;
    ImageButton search_person;
    Button btnCash, btnCreditCard, btnOtherWays , createInvoice;
    TextView tvTotalPrice;
    TextView tvTotalSaved;
    TextView salesSaleMan;
    static TextView customerBalance;
    TextView payment_by_customer_credit;
    EditText etSearch;
    ImageButton btnDone;
    ImageButton btnGrid, btnList;
    ScrollView scDepartment;
    LinearLayout llDepartments;
    static LinearLayout linearLayoutCustomerBalance;
    FrameLayout fragmentTouchPad;
    GridView gvProducts;
    ListView lvProducts;
    CategoryDBAdapter departmentDBAdapter;
    ProductDBAdapter productDBAdapter;
    OfferDBAdapter offerDBAdapter;
    ProductOfferDBAdapter productOfferDBAdapter;
    OrderDBAdapter saleDBAdapter;
    OrderDetailsDBAdapter orderDBAdapter;
    View prseedButtonDepartments;
    List<Product> productList;
    List<Product> All_productsList;
    // Customer Variable
    List<Customer> customerList;
    List<Customer> AllCustmerList;
    CustomerDBAdapter customerDBAdapter;
    ClubAdapter clubAdapter;
    Sum_PointDbAdapter sum_pointDbAdapter;
    UsedPointDBAdapter usedpointDbAdapter;
    ValueOfPointDB valueOfPointDB;
    Customer customer;
    long customerId = 0;
    long customerClubId = 0;
    String customerName = "";
    // Club
    int newPoint = 0;
    int clubAmount;
    int clubType;
    int clubPoint;
    int pointFromSale = 0;
    String clubName = "";
    double clubDiscount = 0.0;
    boolean equalUsedPoint = false;
    boolean biggerUsedPoint = false;
    boolean lessUsedPoint = false;
    //end
    double offerAmount = 0;
    long saleIDforCash;
    static List<Integer> offersIDsList;
    List<Offer> offersList;
    ProductCatalogGridViewAdapter productCatalogGridViewAdapter;
    CustomerCatalogGridViewAdapter custmerCatalogGridViewAdapter;
    //ProductCatalogListViewAdapter productCatalogListViewAdapter;
    String barcodeScanned = "";
    ListView lvOrder;
    static Order sale;
    SaleDetailsListViewAdapter saleDetailsListViewAdapter;
    View selectedIteminCartList;
    OrderDetails selectedOrderOnCart = null;
    private List<Employee> custmerAssestList;
    private List<Employee> AllCustmerAssestList;
    public CustomerAssetDB custmerAssetDB;
    public CustomerAssistantCatalogGridViewAdapter custmerAssestCatlogGridViewAdapter;
    private boolean isGrid = true;
    double saleTotalPrice = 0.0;

    double totalSaved = 0.0;
    double secondPrice = 0.0;
    boolean userScrolled = false;
    int productLoadItemOffset = 0;
    int productCountLoad = 60;
    POSSDK pos;
    Button btn_cancel;
    LinearLayout ll;
    ImageView imv, btnCancel;

    private String touchPadPressed = "";
    private boolean enableBackButton = true;

    PopupWindow popupWindow;
    EditText customer_id;
    //Drw drw=null;
    //String devicePath="/dev/ttySAC1";
    EditText customerName_EditText;
    ////offer varible
    boolean SumForRule3Status = false;
    int SumForRule3 = 0;
    boolean clubStatusForRule3 = false;
    boolean clubStatusForRule7 = false;
    boolean clubStatusForRule8 = false;
    boolean clubStatusForRule11 = false;

    int SumClubForRule3 = 0;
    int SumClubForRule7 = 0;
    int SumClubForRule8 = 0;
    int SumClubForRule11 = 0;
    double SumForClub = 0.0;
    boolean SumForRule11Status = false;
    int SumForRule11 = 0;

    Boolean availableRule3 = false;
    double parcentForRule3 = 0.0;
    double priceFoeRule7;
    double priceFoeRule8;

    long productIDForRule7;
    Boolean availableRule11 = false;
    double amountForRule11 = 0;
    double DiscountamountForRule11 = 0;
    double ParcentForRule8 = 0.0;
    long productIDForRule8;
    int priceForRule5;
    long productIdForRule5;
    long giftProductIdForRule5;
    boolean stausForRule5 = false;
    int Ppoint;
    double i = 0.0;
    String str;
    boolean forSaleMan = false;
    List<Long> custmerAssetstIdList;
    List<OrderDetails> orderIdList;
    List<Long> orderId;
    long custmerSaleAssetstId;
    TextView orderSalesMan, orderCount, orderTotalPrice, orderOfferName;
    ImageView deleteOrderSalesMan,mainActivity_btnRemoveCustomer;
    String fromEditText = "";
    static List<String> printedRows;
    double valueOfDiscount = 0;
    List<Currency> currenciesList;
    private List<CurrencyType> currencyTypesList = null;

    //Cart Discount View
    private LinearLayout llCartDiscount;
    private TextView tvCartDiscountValue,tvTotalPriceBeforeCartDiscount;
    String invoiceNum;
    double  customerGeneralLedger=0.0;
    boolean orderDocumentFlag=false;
    String orderDocNum ="";
    JSONObject invoiceJsonObject =new JSONObject();
    static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_temp);


        TitleBar.setTitleBar(this);

        if (!Util.isSyncServiceRunning(this)) {
            Intent intent = new Intent(SalesCartActivity.this, SyncMessage.class);
            intent.putExtra(SyncMessage.API_DOMAIN_SYNC_MESSAGE, SETTINGS.BO_SERVER_URL);
            startService(intent);
        }
        context=SalesCartActivity.this;
        customerName_EditText = (EditText) findViewById(R.id.customer_textView);

        Button btAddProductShortLink = (Button) findViewById(R.id.mainActivity_btAddProductShortLink);
        btAddProductShortLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SalesCartActivity.this, ProductsActivity.class);
                startActivity(i);
            }
        });
        //Getting default currencies name and values
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(this);
        currencyTypeDBAdapter.open();
        currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
        currencyTypeDBAdapter.close();
        //get currency value
        CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(SalesCartActivity.this);
        currencyDBAdapter.open();
        currenciesList = currencyDBAdapter.getAllCurrencyLastUpdate(currencyTypesList);
        currencyDBAdapter.close();
        search_person = (ImageButton) findViewById(R.id.searchPerson);
        drawerLayout = (DrawerLayout) findViewById(R.id.mainActivity_drawerLayout);

        //region Init
        btnPauseSale = (Button) findViewById(R.id.mainActivity_BTNGeneralProduct);
        btnResumeSale = (Button) findViewById(R.id.mainActivity_BTNMultProduct);
        btnPercentProduct = (Button) findViewById(R.id.mainActivity_BTNPercentProduct);
        //  btnLastSales = (ImageButton) findViewById(R.id.mainActivity_BTNLastSales);
        btnCancel = (ImageView) findViewById(R.id.mainActivity_btnCancel);
        lvOrder = (ListView) findViewById(R.id.mainScreen_LVOrder);

        btnCash = (Button) findViewById(R.id.mainActivity_btnCash);
        btnOtherWays = (Button) findViewById(R.id.mainActivity_btnOtherWays);
        btnCreditCard = (Button) findViewById(R.id.mainActivity_btnCreditCard);
        tvTotalPrice = (TextView) findViewById(R.id.mainActivity_tvTotalPrice);
        tvTotalSaved = (TextView) findViewById(R.id.mainActivity_tvTotalSaved);

        gvProducts = (GridView) findViewById(R.id.mainActivity_gvProducts);
        lvProducts = (ListView) findViewById(R.id.mainActivity_lvProducts);
        lvProducts.setVisibility(View.GONE);
        //   lvcustmer.setVisibility(View.GONE);
        btnGrid = (ImageButton) findViewById(R.id.mainActivity_btnGrid);
        btnList = (ImageButton) findViewById(R.id.mainActivity_btnList);
        salesSaleMan = (TextView) findViewById(R.id.salesSaleMan);
        customerBalance = (TextView) findViewById(R.id.customerBalance);
        createInvoice = (Button)findViewById(R.id.mainActivity_BTNInvoice);
    //    payment_by_customer_credit = (TextView)findViewById(R.id.mainActivity_payment_by_customer_credit);
        custmerAssetstIdList = new ArrayList<Long>();
        orderIdList = new ArrayList<OrderDetails>();
        orderId = new ArrayList<Long>();



        //cart discount init view
        llCartDiscount = (LinearLayout) findViewById(R.id.saleCart_llCartDiscount);
        tvCartDiscountValue = (TextView) findViewById(R.id.saleCart_llCartDiscountValue);
        tvTotalPriceBeforeCartDiscount = (TextView) findViewById(R.id.saleCart_tvTotalPriceBeforeCartDiscount);

        //fragmentTouchPad = (FrameLayout) findViewById(R.id.mainActivity_fragmentTochPad);

        //region  Init cash drawer
        /*
        try {
            drw = new Drw(devicePath, ComIO.Baudrate.BAUD_38400);
        }
        catch (UnsatisfiedLinkError e){
            Log.e("CashDrawer", "can not connect to cash drawer");
        }
        catch (ExceptionInInitializerError er){
            Log.e("CashDrawer", "can not connect to cash drawer");
        }
        */
        //endregion


        showTouchPad(false);

        //region Orders Frame


        //endregion

        //hshheot
        ll = (LinearLayout) findViewById(R.id.mainActivity_llButtonsSales);
        //ll.setVisibility(View.GONE);
        imv = (ImageView) findViewById(R.id.imageView6);
        //imv.setVisibility(View.GONE);


        llDepartments = (LinearLayout) findViewById(R.id.mainActivity_LLDepartment);
        linearLayoutCustomerBalance = (LinearLayout) findViewById(R.id.linearLayoutCustomerBalance);
        mainActivity_btnRemoveCustomer= (ImageView) findViewById(R.id.mainActivity_btnRemoveCustomer);
        departmentDBAdapter = new CategoryDBAdapter(this);
        productDBAdapter = new ProductDBAdapter(this);
        customerDBAdapter = new CustomerDBAdapter(this);
        clubAdapter = new ClubAdapter(this);
        valueOfPointDB = new ValueOfPointDB(this);
        sum_pointDbAdapter = new Sum_PointDbAdapter(this);
        usedpointDbAdapter = new UsedPointDBAdapter(this);
        usedpointDbAdapter.open();
        sum_pointDbAdapter.open();

        customerDBAdapter.open();
        productDBAdapter.open();
        departmentDBAdapter.open();
        clubAdapter.open();
        valueOfPointDB.open();

        offerDBAdapter = new OfferDBAdapter(this);
        productOfferDBAdapter = new ProductOfferDBAdapter(this);

        offersList = new ArrayList<Offer>();

        //// TODO: 25/12/2016 get all offer and make it on cart
        //// TODO: 25/12/2016 offer management activity
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                offerDBAdapter.open();
                productOfferDBAdapter.open();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //   offerDBAdapter.close();
                productOfferDBAdapter.close();

            }

            @Override
            protected Void doInBackground(Void... params) {
                // offersIDsList = offerDBAdapter.getAllOffersIDsByStatus(Offer.Active);
                // offersIDsList = offerDBAdapter.getAllOffersIDsByStatus(Offer.Active);
                return null;
            }
        }.execute();


        //endregion

        //region Grid List Button

        search_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPopup();
            }
        });
        btnGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGrid = true;
                btnList.setImageDrawable(getResources().getDrawable(R.drawable.icon_listview));
                btnGrid.setImageDrawable(getResources().getDrawable(R.drawable.icon_gridview_active));
                lvProducts.setVisibility(View.GONE);
                gvProducts.setVisibility(View.VISIBLE);
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGrid = false;
                btnList.setImageDrawable(getResources().getDrawable(R.drawable.icon_listview_active));
                btnGrid.setImageDrawable(getResources().getDrawable(R.drawable.icon_gridview));
                lvProducts.setVisibility(View.VISIBLE);
                gvProducts.setVisibility(View.GONE);
            }
        });
        salesSaleMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPopupForSalesMan();
            }
        });


        //endregion

        //region Clear Cart

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clubDiscount = 0;
                Context c = SalesCartActivity.this;
                if (SESSION._ORDER_DETAILES.size() > 0)
                    new AlertDialog.Builder(c)
                            .setTitle(c.getResources().getString(R.string.clearCartAlertTitle))
                            .setMessage(c.getResources().getString(R.string.clearCartAlertMessage))
                            .setPositiveButton(c.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    clearCart();
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton(c.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
            }
        });

        //endregion

        //region Products Catalog

        gvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    addToCart(productList.get(position));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    addToCart(productList.get(position));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        lvProducts.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                    loadMoreProduct();
                }
            }
        });
        gvProducts.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                    loadMoreProduct();
                }
            }
        });
        productList = productDBAdapter.getTopProducts(0, 50);
        All_productsList = productList;
        productCatalogGridViewAdapter = new ProductCatalogGridViewAdapter(this, productList);
        gvProducts.setNumColumns(2);

        gvProducts.setAdapter(productCatalogGridViewAdapter);
        lvProducts.setAdapter(productCatalogGridViewAdapter);

        //region Departments
        Button btAll = new Button(this);
        LinearLayout.LayoutParams btAllParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        btAllParams.setMargins(5, 5, 5, 5);
        btAll.setId(0);
        btAll.setText(getResources().getText(R.string.all));
        btAll.setTextAppearance(this, R.style.TextAppearance);
        btAll.setPressed(true);
        btAll.setLayoutParams(btAllParams);
        btAll.setBackground(getResources().getDrawable(R.drawable.bt_normal_pressed));
        llDepartments.addView(btAll);
        prseedButtonDepartments = btAll;
        btAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productLoadItemOffset = 0;
                prseedButtonDepartments.setPressed(false);
                prseedButtonDepartments.setBackground(getResources().getDrawable(R.drawable.bt_normal));
                v.setPressed(true);
                v.setBackground(getResources().getDrawable(R.drawable.bt_normal_pressed));
                prseedButtonDepartments = v;

                productList = productDBAdapter.getTopProducts(productLoadItemOffset, productCountLoad);
                All_productsList = productList;
                productCatalogGridViewAdapter = new ProductCatalogGridViewAdapter(getApplicationContext(), productList);
                gvProducts.setAdapter(productCatalogGridViewAdapter);
                lvProducts.setAdapter(productCatalogGridViewAdapter);
            }
        });

        List<Category> departments = departmentDBAdapter.getAllDepartments();
        int co = 2;
        for (int k = 0, r = 0, c = 0; k < departments.size(); k++) {
            if (k % 2 == 1) {
                LinearLayout ll = new LinearLayout(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                LinearLayout.LayoutParams btParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                btParams.setMargins(5, 5, 5, 5);
                ll.setLayoutParams(params);

                Button bt = new Button(this);
                bt.setText(departments.get(k - 1).getName());
                bt.setTag(departments.get(k - 1));

                bt.setLayoutParams(btParams);
                bt.setTextAppearance(this, R.style.TextAppearance);
                bt.setBackground(getResources().getDrawable(R.drawable.bt_normal));
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        productLoadItemOffset = 0;
                        prseedButtonDepartments.setPressed(false);
                        prseedButtonDepartments.setBackground(getResources().getDrawable(R.drawable.bt_normal));
                        v.setPressed(true);
                        v.setBackground(getResources().getDrawable(R.drawable.bt_normal_pressed));

                        prseedButtonDepartments = v;
                        productList = productDBAdapter.getAllProductsByCategory(((Category) v.getTag()).getCategoryId(), productLoadItemOffset, productCountLoad);
                        All_productsList = productList;
                        productCatalogGridViewAdapter = new ProductCatalogGridViewAdapter(getApplicationContext(), productList);
                        gvProducts.setAdapter(productCatalogGridViewAdapter);
                        lvProducts.setAdapter(productCatalogGridViewAdapter);
                    }
                });
                ll.addView(bt);
                Button bt2 = new Button(this);
                bt2.setText(departments.get(k).getName());
                bt2.setTag(departments.get(k));
                bt2.setLayoutParams(btParams);
                bt2.setTextAppearance(this, R.style.TextAppearance);
                bt2.setBackground(getResources().getDrawable(R.drawable.bt_normal));
                bt2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        productLoadItemOffset = 0;
                        prseedButtonDepartments.setPressed(false);
                        prseedButtonDepartments.setBackground(getResources().getDrawable(R.drawable.bt_normal));
                        v.setPressed(true);
                        v.setBackground(getResources().getDrawable(R.drawable.bt_normal_pressed));

                        prseedButtonDepartments = v;
                        productList = productDBAdapter.getAllProductsByCategory(((Category) v.getTag()).getCategoryId(), productLoadItemOffset, productCountLoad);
                        All_productsList = productList;
                        productCatalogGridViewAdapter = new ProductCatalogGridViewAdapter(getApplicationContext(), productList);
                        gvProducts.setAdapter(productCatalogGridViewAdapter);
                        lvProducts.setAdapter(productCatalogGridViewAdapter);
                    }
                });
                ll.addView(bt2);
                llDepartments.addView(ll);
                  /*View line = new View(this);
            line.setBackgroundColor(getResources().getColor(R.color.backgroundColor));
            llDepartments.addView(line);*/

                //add line
            }
        }

        if (departments.size() % 2 == 1) {
            LinearLayout ll = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
            LinearLayout.LayoutParams btParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
            btParams.setMargins(5, 5, 5, 5);
            ll.setLayoutParams(params);

            Button bt = new Button(this);
            bt.setText(departments.get(departments.size() - 1).getName());
            bt.setTag(departments.get(departments.size() - 1));
            bt.setLayoutParams(btParams);
            bt.setTextAppearance(this, R.style.TextAppearance);
            bt.setBackground(getResources().getDrawable(R.drawable.bt_normal));
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productLoadItemOffset = 0;
                    prseedButtonDepartments.setPressed(false);
                    prseedButtonDepartments.setBackground(getResources().getDrawable(R.drawable.bt_normal));
                    v.setPressed(true);
                    v.setBackground(getResources().getDrawable(R.drawable.bt_normal_pressed));

                    prseedButtonDepartments = v;
                    productList = productDBAdapter.getAllProductsByCategory(((Category) v.getTag()).getCategoryId(), productLoadItemOffset, productCountLoad);
                    All_productsList = productList;
                    productCatalogGridViewAdapter = new ProductCatalogGridViewAdapter(getApplicationContext(), productList);
                    gvProducts.setAdapter(productCatalogGridViewAdapter);
                    lvProducts.setAdapter(productCatalogGridViewAdapter);
                }
            });
            ll.addView(bt);
            llDepartments.addView(ll);
        }

        //endregion

        //endregion

        //region Search Box

        etSearch = (EditText) findViewById(R.id.mainActivity_etSearch);

       /* etSearch.setFocusable(false);
        etSearch.setEnabled(false);
        etSearch.setVisibility(EditText.VISIBLE);*/


        etSearch.setText("");
        etSearch.setHint("Search..");

        /**  etSearch.setOnKeyListener(new View.OnKeyListener() {
        @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_DOWN) {
        Log.i("pressed key", "Enter");
        btnDone.callOnClick();
        return true;
        //enterKeyPressed();
        } else if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP)
        return true;
        return false;

        //else
        //barcodeScanned +=event.getNumber();
        }
        /**  etSearch.setOnKeyListener(new View.OnKeyListener() {
        @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_DOWN) {
        Log.i("pressed key", "Enter");
        btnDone.callOnClick();
        return true;
        //enterKeyPressed();
        } else if (keyCode == event.KEYCODE_ENTER && event.getAction() == event.ACTION_UP)
        return true;
        return false;

        //else
        //barcodeScanned +=event.getNumber();
        }
        });**/

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String word = etSearch.getText().toString();
                if (!word.equals("")) {
                    productCountLoad = 80;
                    productLoadItemOffset = 0;
                    // Database query can be a time consuming task ..
                    // so its safe to call database query in another thread
                    // Handler, will handle this stuff
                    new AsyncTask<String, Void, Void>() {

                        @Override
                        protected void onPreExecute() {
                            productList = new ArrayList<Product>();
                            super.onPreExecute();
                        }

                        @Override
                        protected Void doInBackground(String... params) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            productList = productDBAdapter.getAllProductsByHint(params[0], productLoadItemOffset, productCountLoad);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            ProductCatalogGridViewAdapter adapter = new ProductCatalogGridViewAdapter(getApplicationContext(), productList);
                            gvProducts.setAdapter(adapter);
                            lvProducts.setAdapter(adapter);
                        }
                    }.execute(word);
                } else {
                    productList = All_productsList;
                    ProductCatalogGridViewAdapter adapter = new ProductCatalogGridViewAdapter(getApplicationContext(), productList);
                    gvProducts.setAdapter(adapter);
                    lvProducts.setAdapter(adapter);

                }

            }
        });

        //endregion

        //region Done Button

        btnDone = (ImageButton) findViewById(R.id.mainActivity_btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodeScanned = etSearch.getText().toString();
                if (!barcodeScanned.equals("")) {
                    try {
                        enterKeyPressed(barcodeScanned);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    barcodeScanned = "";
                    etSearch.setText("");
                    //etSearch.requestFocus();
                } else
                    Toast.makeText(SalesCartActivity.this, "Input is empty.", Toast.LENGTH_SHORT).show();
                //OpenCashBox();

            }
        });
        mainActivity_btnRemoveCustomer.
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        removeCustomer();
                    }
                });
  /**      payment_by_customer_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SESSION._ORDER_DETAILES.size() > 0&& SESSION._ORDERS.getCustomer().getCredit()>=saleTotalPrice) {
                    final Dialog customerCreditDialog = new Dialog(SalesCartActivity.this);
                    customerCreditDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    customerCreditDialog.show();
                    customerCreditDialog.setContentView(R.layout.activity_customer_credit);
                    TextView customerName = (TextView) customerCreditDialog.findViewById(R.id.customerCreditCustomerName);
                    customerName.setText(SESSION._ORDERS.getCustomer_name());
                    TextView totalPrice = (TextView) customerCreditDialog.findViewById(R.id.TvPrice);
                    totalPrice.setText(Util.makePrice(saleTotalPrice)+getString(R.string.ins));
                    TextView reamainCredit =(TextView)  customerCreditDialog.findViewById(R.id.TvRemainingCredit);
                    reamainCredit.setText(Util.makePrice(customer.getCredit()-saleTotalPrice)+getString(R.string.ins));
                    Button btnOk = (Button) customerCreditDialog.findViewById(R.id.customerCreditDialog_BTOk);
                    ImageView close = (ImageView) customerCreditDialog.findViewById(R.id.closeDialog);
                    TextView customerCredit = (TextView) customerCreditDialog.findViewById(R.id.TvTotalCustomerCredit);
                    customerCredit.setText(Util.makePrice(SESSION._ORDERS.getCustomer().getCredit())+getString(R.string.ins));
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                           customerCreditDialog.dismiss();
                        }
                    });
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SESSION._ORDERS.setTotalPaidAmount(SESSION._ORDERS.getTotalPrice());
                            saleDBAdapter = new OrderDBAdapter(SalesCartActivity.this);
                            saleDBAdapter.open();
                            long saleID = saleDBAdapter.insertEntry(SESSION._ORDERS, customerId, SESSION._ORDERS.getCustomer_name(),false);
                            orderDBAdapter = new OrderDetailsDBAdapter(SalesCartActivity.this);
                            custmerAssetDB = new CustomerAssetDB(SalesCartActivity.this);
                            orderDBAdapter.open();
                            custmerAssetDB.open();
                            SESSION._ORDERS.setOrderId(saleID);
                            if (forSaleMan) {
                                custmerAssetDB.insertEntry(saleID, custmerSaleAssetstId, SESSION._ORDERS.getTotalPrice(), 0, "ORDER", SESSION._ORDERS.getCreatedAt());
                            }
                            // insert order region
                            for (OrderDetails o : SESSION._ORDER_DETAILES) {
                                long orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id());
                                orderId.add(orderid);
                            }
                            // ORDER_DETAILS Sales man Region
                            for (int i = 0; i < orderIdList.size(); i++) {
                                OrderDetails order = orderIdList.get(i);
                                long customerAssestId = custmerAssetstIdList.get(i);
                                for (int j = 0; j < SESSION._ORDER_DETAILES.size(); j++) {
                                    OrderDetails o = SESSION._ORDER_DETAILES.get(j);
                                    long tempOrderId = orderId.get(i);
                                    if (o == order) {
                                        if (custmerAssetstIdList.get(i) != custmerSaleAssetstId) {
                                            o.setCustomer_assistance_id(custmerAssetstIdList.get(i));
                                            custmerAssetDB.insertEntry(tempOrderId, customerAssestId, o.getPaidAmount(), 0, "ORDER_DETAILS", SESSION._ORDERS.getCreatedAt());
                                        }
                                    }
                                }
                            }
                            //update customer balance
                            Customer upDateCustomer = SESSION._ORDERS.getCustomer();

                            if (SESSION._ORDERS.getTotalPrice() < 0 && customer != null) {
                                upDateCustomer.setBalance(SESSION._ORDERS.getTotalPrice() + customer.getBalance());
                            }
                            // upDate CustomerCredit
                                upDateCustomer.setCredit(SESSION._ORDERS.getCustomer().getCredit() - saleTotalPrice);
                                customerDBAdapter.updateEntry(upDateCustomer);
                            ///end
                            orderDBAdapter.close();
                            custmerAssetDB.close();
                            SESSION._ORDERS.setOrders(SESSION._ORDER_DETAILES);
                            SESSION._ORDERS.setUser(SESSION._EMPLOYEE);

                            PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(SalesCartActivity.this);
                            paymentDBAdapter.open();

                            long paymentID = paymentDBAdapter.insertEntry(CREDIT, saleTotalPrice, saleID);
                            paymentDBAdapter.close();

                            Payment payment = new Payment(paymentID, CREDIT, saleTotalPrice, saleID);
                            SESSION._ORDERS.setPayment(payment);

                            printAndOpenCashBox("", "", "", REQUEST_CREDIT_ACTIVITY_CODE);
                            saleDBAdapter.close();
                            clearCart();
                            customerCreditDialog.dismiss();
                            return;


                        }
                    });
                }
            }
});**/

        /**  etSearch.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
        etSearch.setFocusable(true);
        }});**/

        //endregion
/**
 actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
 drawerLayout.addDrawerListener(actionBarDrawerToggle);
 actionBarDrawerToggle.syncState();
 **/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //region ORDER_DETAILS List View


        if (SESSION._ORDERS != null) {
            sale = new Order(SESSION._EMPLOYEE.getEmployeeId(), new Timestamp(System.currentTimeMillis()), 0, false, 0, 0);
        } else {
            SESSION._ORDERS = new Order(SESSION._EMPLOYEE.getEmployeeId(), new Timestamp(System.currentTimeMillis()), 0, false, 0, 0);
        }

        if (SESSION._ORDER_DETAILES != null) {
            lvOrder.setFocusable(false);
            offerDBAdapter = new OfferDBAdapter(this);
            offerDBAdapter.open();
        } else {
            SESSION._ORDER_DETAILES = new ArrayList<OrderDetails>();
        }
        saleDetailsListViewAdapter = new SaleDetailsListViewAdapter(getApplicationContext(), R.layout.list_adapter_row_main_screen_sales_details, SESSION._ORDER_DETAILES);
        lvOrder.setAdapter(saleDetailsListViewAdapter);


        lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                removeOrderItemSelection();
                view.findViewById(R.id.rowSaleDetails_LLMethods).setVisibility(View.VISIBLE);
                view.findViewById(R.id.saleManLayout).setVisibility(View.VISIBLE);
                view.findViewById(R.id.offerLayout).setVisibility(View.VISIBLE);
                final double discount = SESSION._ORDER_DETAILES.get(position).getDiscount();
                if (discount > 0) {
                    view.findViewById(R.id.discountLayout).setVisibility(View.VISIBLE);

                } else {
                    view.findViewById(R.id.discountLayout).setVisibility(View.GONE);

                }
                if (SESSION._ORDER_DETAILES.get(position).getOffer() != null) {

                    view.findViewById(R.id.offerLayout).setVisibility(View.VISIBLE);

                } else {
                    view.findViewById(R.id.offerLayout).setVisibility(View.GONE);

                }
                selectedIteminCartList = view;
                selectedOrderOnCart = SESSION._ORDER_DETAILES.get(position);
                view.setBackgroundColor(getResources().getColor(R.color.list_background_color));
                saleDetailsListViewAdapter.setSelected(position);


                Button btnDelete = (Button) view.findViewById(R.id.rowSaleDetails_MethodsDelete);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeFromCart(position);
                    }
                });
                orderSalesMan = (TextView) view.findViewById(R.id.orderSaleMan);
                orderCount = (TextView) view.findViewById(R.id.rowSaleDetails_TVCount);
                orderTotalPrice = (TextView) view.findViewById(R.id.rowSaleDetails_TVTotalPrice);
                deleteOrderSalesMan = (ImageView) view.findViewById(R.id.deleteOrderSalesMan);

                orderSalesMan.
                        setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                callPopupOrderSalesMan(selectedOrderOnCart);
                            }
                        });
                deleteOrderSalesMan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < orderIdList.size(); i++) {
                            if (orderIdList.get(i) == selectedOrderOnCart) {
                                orderIdList.remove(i);
                                custmerAssetstIdList.remove(i);
                            }
                        }
                        orderSalesMan.setText(getString(R.string.sales_man));
                        deleteOrderSalesMan.setVisibility(View.GONE);
                    }
                });


                Button btnPlusOne = (Button) view.findViewById(R.id.rowSaleDetails_MethodsPlusOne);
                btnPlusOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        increaseItemOnCart(position);
                    }
                });
                Button btnMOne = (Button) view.findViewById(R.id.rowSaleDetails_MethodsMOne);
                btnMOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        decreaseItemOnCart(position);
                    }
                });
                Button btnEdit = (Button) view.findViewById(R.id.rowSaleDetails_MethodsEdit);
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedIteminCartList != null) {
                            if (SESSION._ORDER_DETAILES.contains(selectedOrderOnCart)) {
                                final Dialog cashDialog = new Dialog(SalesCartActivity.this);
                                cashDialog.setTitle(R.string.multi_count);
                                cashDialog.setContentView(R.layout.cash_payment_dialog);
                                cashDialog.show();

                                final Button cashBTOk = (Button) cashDialog.findViewById(R.id.cashPaymentDialog_BTOk);
                                final EditText cashETCash = (EditText) cashDialog.findViewById(R.id.cashPaymentDialog_TECash);
                                cashETCash.setHint(R.string.count);
                                cashETCash.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                    @Override
                                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                                            cashBTOk.callOnClick();
                                        }
                                        return false;
                                    }
                                });
                                cashBTOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String _count = cashETCash.getText().toString();
                                        int pid = 1;
                                        if (!(_count.equals("")))
                                            pid = Integer.parseInt(cashETCash.getText().toString());
                                        int indexOfItem = SESSION._ORDER_DETAILES.indexOf(selectedOrderOnCart);
                                        SESSION._ORDER_DETAILES.get(indexOfItem).setCount(pid);
                                        orderCount.setText(SESSION._ORDER_DETAILES.get(position).getQuantity() + "");
                                        orderTotalPrice.setText(selectedOrderOnCart.getPaidAmount() * selectedOrderOnCart.getQuantity() + getString(R.string.ins));
                                        calculateTotalPrice();

                                        cashDialog.cancel();
                                    }
                                });
                                Button cashBTCancel = (Button) cashDialog.findViewById(R.id.cashPaymentDialog_BTCancel);
                                cashBTCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        cashDialog.cancel();
                                    }
                                });

                                Switch s = (Switch) cashDialog.findViewById(R.id.cashPaymentDialog_SwitchProportion);
                                s.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.please_select_item), Toast.LENGTH_SHORT);
                            }
                        } else {
                            Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.please_add_item), Toast.LENGTH_SHORT);
                        }
                    }
                });
                Button btnDiscount = (Button) view.findViewById(R.id.rowSaleDetails_Dicount);
                btnDiscount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedOrderOnCart.getUnitPrice()<=0) {
                            //todo show message cant implement discount on credit item
                            return;
                        }
                        if (selectedIteminCartList != null) {
                            if (SESSION._ORDER_DETAILES.contains(selectedOrderOnCart)) {
                                final TextView discountPercentage = (TextView) view.findViewById(R.id.discountPercentage);
                                final TextView tvDiscountPercentage = (TextView) view.findViewById(R.id.tvDiscountPercentageAmount);
                                final Dialog cashDialog = new Dialog(SalesCartActivity.this);
                                cashDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                // cashDialog.setTitle(R.string.please_select_discount_offer);
                                cashDialog.show();
                                cashDialog.setContentView(R.layout.discount_dialog);
                                final Button cashBTOk = (Button) cashDialog.findViewById(R.id.cashPaymentDialog_BTOk);
                                final EditText cashETCash = (EditText) cashDialog.findViewById(R.id.cashPaymentDialog_TECash);
                                final Switch sw = (Switch) cashDialog.findViewById(R.id.cashPaymentDialog_SwitchProportion);
                                final TextView totalPrice = (TextView) cashDialog.findViewById(R.id.TvTotalPrice);
                                final TextView priceAfterDiscount = (TextView) cashDialog.findViewById(R.id.TvPriceAfterDiscount);
                                final TextView totalDiscount = (TextView) cashDialog.findViewById(R.id.totalDiscount);
                                final ImageView closeDialogImage = (ImageView) cashDialog.findViewById(R.id.closeDialog);
                                closeDialogImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        cashDialog.dismiss();
                                    }
                                });
                                List<OrderDetails> list = new ArrayList<OrderDetails>();
                                list.add(selectedOrderOnCart);
                                final TextView discountType = (TextView) cashDialog.findViewById(R.id.cashPaymentDialog_TVStatus);
                                discountType.append(":" + selectedOrderOnCart.getProduct().getDisplayName());
                                double pad = (selectedOrderOnCart.getPaidAmount());
                                priceAfterDiscount.setText( pad + getString(R.string.ins));
                                selectedOrderOnCart.rowDiscount = 0;
                                selectedOrderOnCart.setDiscount(0);
                                double itemOriginalPrice = selectedOrderOnCart.getPaidAmount();
                                totalPrice.setText(selectedOrderOnCart.getPaidAmount() + getString(R.string.ins));
                                totalDiscount.setText(selectedOrderOnCart.getDiscount()*selectedOrderOnCart.getQuantity() + getString(R.string.ins));
                                sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        double itemOriginalPrice = selectedOrderOnCart.getPaidAmount();
                                        if (isChecked) {
                                            sw.setText(getBaseContext().getString(R.string.amount));
                                            cashETCash.setText("");
                                            cashETCash.setHint("0");
                                        } else {
                                            sw.setText(getBaseContext().getString(R.string.proportion));
                                            cashETCash.setText("");
                                            cashETCash.setHint("0");
                                        }
                                        totalDiscount.setText(Util.makePrice((itemOriginalPrice - selectedOrderOnCart.getPaidAmount())) + getString(R.string.ins));
                                        priceAfterDiscount.setText(selectedOrderOnCart.getPaidAmount()+ getString(R.string.ins));
                                    }
                                });
                                cashETCash.setHint(R.string.proportion);
                                final List<OrderDetails> orderList = list;
                                cashETCash.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        cashETCash.setBackgroundResource(R.drawable.catalogproduct_item_bg);
                                        String str = cashETCash.getText().toString();
                                        double discount = 0;
                                        selectedOrderOnCart.rowDiscount = 0;
                                        selectedOrderOnCart.setDiscount(0);
                                        double itemOriginalPrice = selectedOrderOnCart.getPaidAmount();
                                        double X = SESSION._EMPLOYEE.getPresent();
                                        if (sw.isChecked()) {
                                            if (!(str.equals(""))) {
                                                double d = Double.parseDouble(str);
                                                double originalTotalPrice = 0;
                                                double salePrice=0;
                                                for (int i=0;i<SESSION._ORDER_DETAILES.size();i++) {
                                                    OrderDetails o =SESSION._ORDER_DETAILES.get(i);
                                                    salePrice+=(o.getUnitPrice() * o.getQuantity());
                                                    if(i!=position){
                                                    originalTotalPrice += (o.getUnitPrice() * o.getQuantity())*(1-o.getDiscount()/100);
                                                    }else {
                                                        originalTotalPrice += d;
                                                    }
                                                }
                                                originalTotalPrice= originalTotalPrice - (originalTotalPrice * (SESSION._ORDERS.cartDiscount / 100));
                                                salePrice= salePrice - (salePrice * (X/ 100));
                                                Log.d("teeestr",originalTotalPrice+":  "+salePrice);
                                                if (originalTotalPrice>=salePrice){
                                                    discount = (1 - (d / (selectedOrderOnCart.getUnitPrice() * selectedOrderOnCart.getQuantity()))) * 100;
                                                    selectedOrderOnCart.setDiscount(discount);
                                                    totalDiscount.setText(Util.makePrice(itemOriginalPrice * selectedOrderOnCart.getDiscount()/100) + getString(R.string.ins));
                                                    priceAfterDiscount.setText(itemOriginalPrice*(1-(selectedOrderOnCart.getDiscount())/100) + getString(R.string.ins));
                                                } else {
                                                    Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                                    cashETCash.setBackgroundResource(R.drawable.backtext);
                                                }

                                            } else {
                                                totalDiscount.setText(Util.makePrice(itemOriginalPrice * selectedOrderOnCart.getDiscount()/100) + getString(R.string.ins));
                                                priceAfterDiscount.setText(itemOriginalPrice*(1-(selectedOrderOnCart.getDiscount())/100) + getString(R.string.ins));
                                            }
                                        } else {
                                            if (!(str.equals(""))) {
                                                double d = Double.parseDouble(str);
                                                double originalTotalPrice = 0;
                                                double salePrice=0;
                                                for (int i=0;i<SESSION._ORDER_DETAILES.size();i++) {
                                                    OrderDetails o =SESSION._ORDER_DETAILES.get(i);
                                                    salePrice+=(o.getUnitPrice() * o.getQuantity());
                                                    if(i!=position){
                                                        originalTotalPrice += (o.getUnitPrice() * o.getQuantity())*(1-o.getDiscount()/100);
                                                    }else {
                                                        originalTotalPrice += (selectedOrderOnCart.getUnitPrice() * selectedOrderOnCart.getQuantity())*(1-d/100);
                                                    }
                                                }
                                                originalTotalPrice= originalTotalPrice - (originalTotalPrice * (SESSION._ORDERS.cartDiscount / 100));
                                                salePrice= salePrice - (salePrice * (X/ 100));
                                                if (originalTotalPrice>=salePrice){
                                                    discount= Double.parseDouble(str);
                                                    selectedOrderOnCart.setDiscount(discount);
                                                    Log.d("teeestr",itemOriginalPrice +"  "+ selectedOrderOnCart.getDiscount()/100);
                                                    totalDiscount.setText(Util.makePrice(itemOriginalPrice * selectedOrderOnCart.getDiscount()/100) + getString(R.string.ins));
                                                    priceAfterDiscount.setText(itemOriginalPrice*(1-(selectedOrderOnCart.getDiscount())/100) + getString(R.string.ins));
                                                } else {
                                                    Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                                    cashETCash.setBackgroundResource(R.drawable.backtext);
                                                }


                                            } else {
                                                totalDiscount.setText(Util.makePrice(itemOriginalPrice * selectedOrderOnCart.getDiscount()/100) + getString(R.string.ins));
                                                priceAfterDiscount.setText(itemOriginalPrice*(1-(selectedOrderOnCart.getDiscount())/100) + getString(R.string.ins));
                                            }
                                        }
                                        /*
                                        double X = SESSION._EMPLOYEE.getPresent();
                                        double discount = 0;
                                        if (!(str.equals(""))) {
                                            if (sw.isChecked()) {
                                                double d = Double.parseDouble(str);
                                                discount = (1 - (d / (selectedOrderOnCart.getUnitPrice() * selectedOrderOnCart.getQuantity()))) * 100;
                                            } else {
                                                discount= Double.parseDouble(str);
                                            }

                                        }
                                        if (discount <= X) {
                                            //   selectedOrderOnCart.rowDiscount = discount;
                                            selectedOrderOnCart.setDiscount(discount);
                                        } else {
                                            Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                            cashETCash.setBackgroundResource(R.drawable.backtext);
                                        }

                                        totalDiscount.setText(Util.makePrice(itemOriginalPrice * selectedOrderOnCart.getDiscount()/100) + getString(R.string.ins));
                                        priceAfterDiscount.setText(itemOriginalPrice*(1-(selectedOrderOnCart.getDiscount())/100) + getString(R.string.ins));
*/
                                    }
                                });
                                cashBTOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        double discount=0;
                                        double itemOriginalPrice = selectedOrderOnCart.getPaidAmount();
                                        String str = cashETCash.getText().toString();
                                        int indexOfItem = SESSION._ORDER_DETAILES.indexOf(selectedOrderOnCart);
                                        double X = SESSION._EMPLOYEE.getPresent();
                                        if (sw.isChecked()) {
                                            if (!(str.equals(""))) {
                                                double d = Double.parseDouble(str);
                                                double originalTotalPrice = 0;
                                                double salePrice=0;
                                                for (int i=0;i<SESSION._ORDER_DETAILES.size();i++) {
                                                    OrderDetails o =SESSION._ORDER_DETAILES.get(i);
                                                    salePrice+=(o.getUnitPrice() * o.getQuantity());
                                                    if(i!=position){
                                                        originalTotalPrice += (o.getUnitPrice() * o.getQuantity())*(1-o.getDiscount()/100);
                                                    }else {
                                                        originalTotalPrice += d;
                                                    }
                                                }
                                                originalTotalPrice= originalTotalPrice - (originalTotalPrice * (SESSION._ORDERS.cartDiscount / 100));
                                                salePrice= salePrice - (salePrice * (X/ 100));
                                                if (originalTotalPrice>=salePrice){
                                                    discount = (1 - (d / (selectedOrderOnCart.getUnitPrice() * selectedOrderOnCart.getQuantity()))) * 100;
                                                    SESSION._ORDER_DETAILES.get(indexOfItem).setDiscount(Double.parseDouble(Util.makePrice(discount)));
                                                    calculateTotalPrice();

                                                    cashDialog.cancel();
                                                } else {
                                                    Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                                    cashETCash.setBackgroundResource(R.drawable.backtext);
                                                }
                                            }
                                        } else {
                                            if (!(str.equals(""))) {
                                                double d = Double.parseDouble(str);
                                                double originalTotalPrice = 0;
                                                double salePrice=0;
                                                for (int i=0;i<SESSION._ORDER_DETAILES.size();i++) {
                                                    OrderDetails o =SESSION._ORDER_DETAILES.get(i);
                                                    salePrice+=(o.getUnitPrice() * o.getQuantity());
                                                    if(i!=position){
                                                        originalTotalPrice += (o.getUnitPrice() * o.getQuantity())*(1-o.getDiscount()/100);
                                                    }else {
                                                        originalTotalPrice += (selectedOrderOnCart.getUnitPrice() * selectedOrderOnCart.getQuantity())*(1-d/100);
                                                    }
                                                }
                                                originalTotalPrice= originalTotalPrice - (originalTotalPrice * (SESSION._ORDERS.cartDiscount / 100));
                                                salePrice= salePrice - (salePrice * (X/ 100));
                                                if (originalTotalPrice>=salePrice){
                                                    discount= Double.parseDouble(str);
                                                    SESSION._ORDER_DETAILES.get(indexOfItem).setDiscount(Double.parseDouble(Util.makePrice(discount)));

                                                    calculateTotalPrice();
                                                    cashDialog.cancel();
                                                }
                                            }
                                        }
                                      /*  if (sw.isChecked()) {
                                            if (!(str.equals(""))) {
                                                double d = Double.parseDouble(str);
                                                int count = SESSION._ORDER_DETAILES.get(indexOfItem).getQuantity();
                                                double discount = (1 - (d / (SESSION._ORDER_DETAILES.get(indexOfItem).getUnitPrice() * count)));

                                                if (discount <= (X / 100)) {
                                                    // SESSION._ORDER_DETAILES.get(indexOfItem).rowDiscount = (discount * 100);
                                                    SESSION._ORDER_DETAILES.get(indexOfItem).setDiscount(Double.parseDouble(Util.makePrice(discount * 100)));
                                                    calculateTotalPrice();

                                                    cashDialog.cancel();
                                                } else {
                                                    Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        } else {
                                            if (!(str.equals(""))) {
                                                float val = Float.parseFloat(str);
                                                if (val <= X) {
                                                    //    SESSION._ORDER_DETAILES.get(indexOfItem).rowDiscount = (val);
                                                    SESSION._ORDER_DETAILES.get(indexOfItem).setDiscount(Double.parseDouble(Util.makePrice(val)));

                                                    calculateTotalPrice();
                                                    cashDialog.cancel();
                                                } else {
                                                    Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }*/
                                    }
                                });
                            } else {
                                Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.please_select_item), Toast.LENGTH_SHORT);
                            }
                        } else {
                            Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.please_add_item), Toast.LENGTH_SHORT);
                        }

                    }
                });
            }
        });


        lvOrder.setAdapter(saleDetailsListViewAdapter);


        //endregion

        //region Payment

        //region Cash

        btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SESSION._ORDER_DETAILES.size() > 0) {
                    if (SETTINGS.enableCurrencies) {
                        //Intent intent = new Intent(SalesCartActivity.this, CashActivity.class);
                        String s =(tvTotalSaved.getText().toString());
                        if (s != null && s.length() > 0 && s.charAt(s.length() - 1) == '₪') {
                            s = s.substring(0, s.length() - 1);
                        }
                        SESSION._ORDERS.totalSaved=Double.parseDouble(s);
                        Intent intent = new Intent(SalesCartActivity.this, MultiCurrenciesPaymentActivity.class);
                        intent.putExtra(COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE, Double.parseDouble(Util.makePrice(saleTotalPrice)));
                        startActivityForResult(intent, REQUEST_MULTI_CURRENCY_ACTIVITY_CODE);
                    } else {
                        Intent intent = new Intent(SalesCartActivity.this, OldCashActivity.class);
                        intent.putExtra(COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE, Double.parseDouble(Util.makePrice(saleTotalPrice)));
                        startActivityForResult(intent, REQUEST_CASH_ACTIVITY_CODE);
                    }
                }
            }
        });


        //endregion


        //region Credit Card

        btnCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s =(tvTotalSaved.getText().toString());
                if (s != null && s.length() > 0 && s.charAt(s.length() - 1) == '₪') {
                    s = s.substring(0, s.length() - 1);
                }
                SESSION._ORDERS.totalSaved=Double.parseDouble(s);
                if (SESSION._ORDER_DETAILES.size() > 0 && SETTINGS.creditCardEnable) {
                    if (SETTINGS.pinpadEnable) {//pinpad is active
                        Log.i("CreditCard", "PinPad is active");
                        Intent intent = new Intent(SalesCartActivity.this, PinpadActivity.class);
                        intent.putExtra(PinpadActivity.LEADERS_POS_PIN_PAD_TOTAL_PRICE, Double.parseDouble(Util.makePrice(saleTotalPrice)));
                        startActivityForResult(intent, REQUEST_PIN_PAD_ACTIVITY_CODE);
                    } else {//old school
                        //final String __customerName = customerName_EditText.getText().toString();
                        Intent intent = new Intent(SalesCartActivity.this, MainCreditCardActivity.class);
                        intent.putExtra(MainCreditCardActivity.LEADERS_POS_CREDIT_CARD_TOTAL_PRICE, Double.parseDouble(Util.makePrice(saleTotalPrice)));
                        startActivityForResult(intent, REQUEST_CREDIT_CARD_ACTIVITY_CODE);
                    }

                }
            }
        });

        //endregion


        //region Other Way

        btnOtherWays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s =(tvTotalSaved.getText().toString());
                if (s != null && s.length() > 0 && s.charAt(s.length() - 1) == '₪') {
                    s = s.substring(0, s.length() - 1);
                }
                SESSION._ORDERS.totalSaved=Double.parseDouble(s);
                if (SESSION._ORDER_DETAILES.size() > 0) {
                    Intent intent = new Intent(SalesCartActivity.this, ChecksActivity.class);
                    customerName = customerName_EditText.getText().toString();

                    intent.putExtra("_Price", Double.parseDouble(Util.makePrice(saleTotalPrice)));
                    intent.putExtra("_custmer", customerName);

                    startActivityForResult(intent, REQUEST_CHECKS_ACTIVITY_CODE);
                }
            }
        });

        //endregion Other Way


        //endregion

        //region sale option buttons

        //// TODO: 07/02/2017 FLAG
        //region Pause ORDER
        btnPauseSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (SESSION._ORDER_DETAILES.size() != 0) {
                    Order s = new Order(SESSION._ORDERS);
                    s.setOrders(SESSION._ORDER_DETAILES);
                    if (SESSION._SALES == null)
                        SESSION._SALES = new ArrayList<Pair<Integer, Order>>();
                    else if (SESSION._SALES.size() == 0)
                        SESSION.TEMP_NUMBER = 0;

                    SESSION._SALES.add(new Pair<>(++SESSION.TEMP_NUMBER, s));


                    clearCart();
                    Toast.makeText(SalesCartActivity.this, getString(R.string.deal_number) + " " + SESSION.TEMP_NUMBER, Toast.LENGTH_SHORT).show();
                }
            }
        });
        //invoice Button region
        createInvoice.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                final String[] items = {
                        getString(R.string.invoice),
                        getString(R.string.receipt),getString(R.string.create_order_document),getString(R.string.view_order_document),getString(R.string.create_credit_invoice_doc),getString(R.string.view_credit_invoice_doc)

                };
                AlertDialog.Builder builder = new AlertDialog.Builder(SalesCartActivity.this);
                builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent;
                        switch (item) {
                            case 0:
                                if(SESSION._ORDERS.getCustomer()!=null&&SESSION._ORDERS.getCustomer().getCustomerType().getValue().equals(CustomerType.CREDIT.getValue())){
                                    ObjectMapper mapper = new ObjectMapper();
                                    final ArrayList<String> ordersIds = new ArrayList<>();
                                    if (SESSION._ORDER_DETAILES.size() > 0) {
                                        if (Long.valueOf(SESSION._ORDERS.getCustomerId()) == 0) {
                                            if (SESSION._ORDERS.getCustomer_name() == null) {
                                                if (customerName_EditText.getText().toString().equals("")) {
                                                    SESSION._ORDERS.setCustomer_name("");
                                                } else {
                                                    SESSION._ORDERS.setCustomer_name(customerName_EditText.getText().toString());
                                                }
                                            }
                                        }


                                        new AsyncTask<Void, Void, Void>(){
                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                            }
                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                try {
                                                    if(invoiceJsonObject.getString("status").equals("200")) {

                                                        print(invoiceImg.Invoice(SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, invoiceNum, customerGeneralLedger));
                                                        clearCart();

                                                    }else if(invoiceJsonObject.getString("message").equals("Maximum allowed credit amount is 2000.0")){
                                                        Toast.makeText(SalesCartActivity.this,"Maximum allowed credit amount is 2000.0",Toast.LENGTH_LONG).show();
                                                    }
                                                    else if(invoiceJsonObject.getString("message").equals("Customer  not found with id :")){
                                                        Toast.makeText(SalesCartActivity.this,"Customer  not found",Toast.LENGTH_LONG).show();
                                                    }
                                                    else {

                                                        new android.support.v7.app.AlertDialog.Builder(SalesCartActivity.this)
                                                                .setTitle(getString(R.string.invoice))
                                                                .setMessage(getString(R.string.cant_make_invoice_check_internet_connection))
                                                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                    }
                                                                })
                                                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        clearCart();

                                                                    }
                                                                })
                                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                                .show();

                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                            @Override
                                            protected Void doInBackground(Void... voids) {
                                                MessageTransmit transmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
                                                JSONObject customerData = new JSONObject();
                                                JSONObject userData = new JSONObject();
                                                try {
                                                    for(OrderDetails orderDetails:SESSION._ORDER_DETAILES){
                                                        orderDetails.setPaidAmount(0.0);
                                                    }
                                                    ObjectMapper mapper = new ObjectMapper();
                                                    customerData.put("customerId", SESSION._ORDERS.getCustomer().getCustomerId());
                                                    userData.put("employeeId",SESSION._EMPLOYEE.getEmployeeId());
                                                    userData.put("name",SESSION._EMPLOYEE.getEmployeeName());
                                                    Log.d("customer",customerData.toString());
                                                    Documents documents = new Documents("Invoice",new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()),SESSION._ORDERS.getTotalPrice(),0,SESSION._ORDERS.getTotalPrice(), InvoiceStatus.UNPAID,"test","test","ILS",SESSION._ORDERS.cartDiscount,SESSION._ORDERS.getNumberDiscount());
                                                    String doc = mapper.writeValueAsString(documents);
                                                    JSONObject docJson= new JSONObject(doc);

                                                    String type = docJson.getString("type");
                                                    docJson.remove("type");
                                                    docJson.put("@type",type);
                                                    docJson.put("customer",customerData);
                                                    docJson.put("user",userData);

                                                    JSONArray cart = new JSONArray(SESSION._ORDER_DETAILES.toString());
                                                    docJson.put("cartDetailsList",cart);
                                                    if(orderDocumentFlag){
                                                        JSONObject jsonObject = new JSONObject();
                                                        jsonObject.put("type",DocumentType.ORDER_DOCUMENT);
                                                        jsonObject.put("docId",orderDocNum);
                                                        List<JSONObject>jsonObjectList = new ArrayList<JSONObject>();
                                                        jsonObjectList.add(jsonObject);
                                                        JSONArray jsonArray = new JSONArray(jsonObjectList.toString());
                                                        docJson.put("referenceDocuments",jsonArray);
                                                    }
                                                    Log.d("Document vale", docJson.toString());
                                                    BoInvoice invoice = new BoInvoice(DocumentType.INVOICE,docJson,"");
                                                    Log.d("Invoice log",invoice.toString());
                                                    String res=transmit.authPost(ApiURL.Documents,invoice.toString(), SESSION.token);
                                                     invoiceJsonObject = new JSONObject(res);
                                                    String msgData = invoiceJsonObject.getString(MessageKey.responseBody);
                                                    JSONObject msgDataJson = new JSONObject(msgData);
                                                    JSONObject jsonObject1=msgDataJson.getJSONObject("documentsData");
                                                    invoiceNum = msgDataJson.getString("docNum");
                                                    Log.d("Invoice log res", res+"");
                                                    customerGeneralLedger=jsonObject1.getDouble("customerGeneralLedger");
                                                    Log.d("Invoice log res", customerGeneralLedger+"");
                                                    Log.d("Invoice Num", invoiceNum);
                                                    String status = invoiceJsonObject.getString("status");
                                                    if(status.equals("200")){
                                                    ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(SalesCartActivity.this);
                                                    zReportDBAdapter.open();
                                                    ZReport zReport =null;
                                                    try {
                                                         zReport = zReportDBAdapter.getLastRow();
                                                        PosInvoiceDBAdapter posInvoiceDBAdapter = new PosInvoiceDBAdapter(SalesCartActivity.this);
                                                        posInvoiceDBAdapter.open();
                                                        posInvoiceDBAdapter.insertEntry(SESSION._ORDERS.getTotalPrice(),zReport.getzReportId(),DocumentType.INVOICE.getValue(),InvoiceStatus.UNPAID.getValue(),invoiceNum, CONSTANT.CASH);
                                                    } catch (Exception e) {
                                                        PosInvoiceDBAdapter posInvoiceDBAdapter = new PosInvoiceDBAdapter(SalesCartActivity.this);
                                                        posInvoiceDBAdapter.open();
                                                        posInvoiceDBAdapter.insertEntry(SESSION._ORDERS.getTotalPrice(),-1,DocumentType.INVOICE.getValue(),InvoiceStatus.UNPAID.getValue(),invoiceNum, CONSTANT.CASH);
                                                        e.printStackTrace();
                                                    }

                                                    try {
                                                        Thread.sleep(100);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                    }

                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                return null;
                                            }
                                        }.execute();

                                    } else{
                                        Toast.makeText(SalesCartActivity.this, "There is no items into on cart.", Toast.LENGTH_SHORT).show();
                                    }}
                                else {
                                    if(SESSION._ORDERS.getCustomer()!=null){
                                        Toast.makeText(SalesCartActivity.this, "Customer Type is normal not credit!!.", Toast.LENGTH_SHORT).show();

                                    }else {
                                        Toast.makeText(SalesCartActivity.this, "Choose Customer Please.", Toast.LENGTH_SHORT).show();
                                    }
                                }


                                break;
                            case 1:
                                intent = new Intent(SalesCartActivity.this, InvoiceManagementActivity.class);
                                startActivity(intent);
                                break;
                            case 2:
                                if(SESSION._ORDERS.getCustomer()!=null){
                                    ObjectMapper mapper = new ObjectMapper();
                                    final ArrayList<String> ordersIds = new ArrayList<>();
                                    if (SESSION._ORDER_DETAILES.size() > 0) {
                                        if (Long.valueOf(SESSION._ORDERS.getCustomerId()) == 0) {
                                            if (SESSION._ORDERS.getCustomer_name() == null) {
                                                if (customerName_EditText.getText().toString().equals("")) {
                                                    SESSION._ORDERS.setCustomer_name("");
                                                } else {
                                                    SESSION._ORDERS.setCustomer_name(customerName_EditText.getText().toString());
                                                }
                                            }
                                        }
                                        final InvoiceImg invoiceImg1 = new InvoiceImg(getApplicationContext());
                                        new AsyncTask<Void, Void, Void>(){
                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                            }
                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                try {
                                                    if(invoiceJsonObject.getString("status").equals("200")) {

                                                        print(invoiceImg1.OrderDocument( SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE,invoiceNum));
                                                        clearCart();

                                                    }
                                                    else {
                                                        new android.support.v7.app.AlertDialog.Builder(SalesCartActivity.this)
                                                                .setTitle(getString(R.string.invoice))
                                                                .setMessage(getString(R.string.cant_make_invoice_check_internet_connection))
                                                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                    }
                                                                })
                                                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        clearCart();

                                                                    }
                                                                })
                                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                                .show();

                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                            @Override
                                            protected Void doInBackground(Void... voids) {
                                                MessageTransmit transmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
                                                JSONObject customerData = new JSONObject();
                                                try {
                                                    ObjectMapper mapper = new ObjectMapper();
                                                    customerData.put("customerId", SESSION._ORDERS.getCustomer().getCustomerId());
                                                    Log.d("customer",customerData.toString());
                                                    String docOrder = mapper.writeValueAsString(SESSION._ORDERS);
                                                    JSONObject orderJson = new JSONObject(docOrder);
                                                    for(OrderDetails orderDetails:SESSION._ORDER_DETAILES){
                                                        orderDetails.setPaidAmount(0.0);
                                                    }
                                                    JSONArray cart = new JSONArray(SESSION._ORDER_DETAILES.toString());
                                                    orderJson.put("cartDetailsList",cart);
                                                    Log.d("orderJson",orderJson.toString());

                                                    OrderDocuments documents = new OrderDocuments("OrderDocument",new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()),SESSION._ORDERS.getTotalPrice(),"test","ILS", OrderDocumentStatus.READY);
                                                    String doc = mapper.writeValueAsString(documents);
                                                    Log.d("doc",doc.toString());

                                                    JSONObject docJson= new JSONObject(doc);
                                                    String type = docJson.getString("type");
                                                    docJson.remove("type");
                                                    docJson.put("@type",type);
                                                    docJson.put("customer",customerData);
                                                    docJson.put("order",orderJson);
                                                    Log.d("Document vale", docJson.toString());
                                                    BoInvoice invoice = new BoInvoice(DocumentType.ORDER_DOCUMENT,docJson,"");
                                                    Log.d("Invoice log",invoice.toString());
                                                    String res=transmit.authPost(ApiURL.Documents,invoice.toString(), SESSION.token);
                                                    JSONObject jsonObject = new JSONObject(res);
                                                    String msgData = jsonObject.getString(MessageKey.responseBody);
                                                    invoiceJsonObject=jsonObject;
                                                    JSONObject msgDataJson = new JSONObject(msgData);
                                                    JSONObject jsonObject1=msgDataJson.getJSONObject("documentsData");
                                                    invoiceNum = msgDataJson.getString("docNum");
                                                    Log.d("Invoice log res", res+"");
                                                    Log.d("Invoice Num", invoiceNum);

                                                    try {
                                                        Thread.sleep(100);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }

                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                return null;
                                            }
                                        }.execute();
                                        //DocumentControl.sendOrderDocument(SalesCartActivity.this);
                                    } else{
                                        Toast.makeText(SalesCartActivity.this, "There is no items into on cart.", Toast.LENGTH_SHORT).show();
                                    }}
                                else {
                                    Toast.makeText(SalesCartActivity.this, "Choose Customer Please to Create Order Document.", Toast.LENGTH_SHORT).show();

                                }

                                break;
                            case 3:
                                Intent intent3 = new Intent(SalesCartActivity.this, OrderDocumentManagementActivity.class);
                                startActivity(intent3);
                            break ;
                            case 4:
                                Intent intent4 = new Intent(SalesCartActivity.this, CreateCreditInvoiceActivity.class);
                                startActivity(intent4);
                                break;
                            case 5:
                                Intent intent5= new Intent(SalesCartActivity.this, ViewCreditInvoiceActivity.class);
                                startActivity(intent5);
                                break;


                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        //endregion

        //region Resume ORDER

        btnResumeSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SESSION._SALES != null && SESSION._SALES.size() > 0) {
                    showAlertDialogResumePauseSale();
                } else {
                    Toast.makeText(SalesCartActivity.this, getString(R.string.there_is_no_sale_on_pause), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //endregion


        //region Percent Product

        btnPercentProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SESSION._ORDERS != null && SESSION._ORDER_DETAILES != null) {
                    final Dialog discountDialog = new Dialog(SalesCartActivity.this);
                    discountDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //discountDialog.setTitle(R.string.please_select_discount_offer);
                    discountDialog.setContentView(R.layout.discount_dialog);
                    discountDialog.show();
                    final Button btOK = (Button) discountDialog.findViewById(R.id.cashPaymentDialog_BTOk);
                    final EditText et = (EditText) discountDialog.findViewById(R.id.cashPaymentDialog_TECash);
                    final Switch sw = (Switch) discountDialog.findViewById(R.id.cashPaymentDialog_SwitchProportion);
                    final TextView totalPrice = (TextView) discountDialog.findViewById(R.id.TvTotalPrice);
                    final TextView priceAfterDiscount = (TextView) discountDialog.findViewById(R.id.TvPriceAfterDiscount);
                    final TextView totalDiscount = (TextView) discountDialog.findViewById(R.id.totalDiscount);
                    final TextView discountType = (TextView) discountDialog.findViewById(R.id.cashPaymentDialog_TVStatus);
                    discountType.setText(getString(R.string.discount));
                    final ImageView closeDialogImage = (ImageView) discountDialog.findViewById(R.id.closeDialog);
                    closeDialogImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            discountDialog.dismiss();
                        }
                    });
                    double originalTotalPrice = 0;
                    double discountAmount=0;
                    for (OrderDetails o : SESSION._ORDER_DETAILES) {
                        originalTotalPrice += (o.getUnitPrice() * o.getQuantity());
                        if(o.getDiscount()>0) {
                            discountAmount += o.getUnitPrice() * o.getQuantity()- o.getUnitPrice() * o.getQuantity() * o.getDiscount()/100;

                        }else {
                            discountAmount += o.getUnitPrice() * o.getQuantity();

                        }
                    }
                    discountAmount = discountAmount - (discountAmount * (SESSION._ORDERS.cartDiscount / 100));

                    totalPrice.setText(originalTotalPrice + getString(R.string.ins));

                    totalDiscount.setText(Util.makePrice(originalTotalPrice-discountAmount) + getString(R.string.ins));
                    priceAfterDiscount.setText(tvTotalPrice.getText().toString());
                    sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (sw.isChecked()) {
                                totalDiscount.setText(Util.makePrice(valueOfDiscount) + getString(R.string.ins));
                                priceAfterDiscount.setText(tvTotalPrice.getText().toString());
                                sw.setText(R.string.amount);
                                et.setText("");
                                et.setHint("0");
                            } else {
                                totalDiscount.setText(Util.makePrice(valueOfDiscount) + getString(R.string.ins));
                                priceAfterDiscount.setText(tvTotalPrice.getText().toString());
                                sw.setText(R.string.proportion);
                                et.setText("");
                                et.setHint("0");
                            }
                        }
                    });
                    et.setHint(R.string.proportion);
                    final List<OrderDetails> orderList = new ArrayList<OrderDetails>();
                    for (int i = 0; i < SESSION._ORDER_DETAILES.size(); i++) {
                        orderList.add(new OrderDetails(SESSION._ORDER_DETAILES.get(i)));
                    }
                    et.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            et.setBackgroundResource(R.drawable.catalogproduct_item_bg);
                            String str = et.getText().toString();
                            double X = SESSION._EMPLOYEE.getPresent();
                            if (sw.isChecked()) {
                                if (!(str.equals(""))) {
                                    double d = Double.parseDouble(str);
                                    double originalTotalPrice = 0;
                                    for (OrderDetails o : orderList) {
                                        originalTotalPrice += (o.getUnitPrice() * o.getQuantity())*(1-o.getDiscount()/100);
                                    }
                                    if ((1 - (d / originalTotalPrice) <= (X / 100))) {
                                        double val = (1 - (d / originalTotalPrice)) * 100;
                                        for (OrderDetails o : orderList) {
                                            //o.setDiscount(val);
                                        }
                                        double saleTotalPrice = 0;
                                        double SaleOriginalityPrice = 0;
                                        for (OrderDetails o : orderList) {
                                            saleTotalPrice += o.getItemTotalPrice();

                                            SaleOriginalityPrice += (o.getUnitPrice() * o.getQuantity())*(1-o.getDiscount()/100);
                                        }
                                        totalDiscount.setText(Util.makePrice(originalTotalPrice*val/100 ) + getString(R.string.ins));
                                        priceAfterDiscount.setText(originalTotalPrice*(1-val/100) + getString(R.string.ins));
                                    } else {
                                        totalDiscount.setText(Util.makePrice(valueOfDiscount) + getString(R.string.ins));
                                        priceAfterDiscount.setText(tvTotalPrice.getText().toString());
                                        Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                        et.setBackgroundResource(R.drawable.backtext);
                                    }

                                } else {
                                    totalDiscount.setText(Util.makePrice(valueOfDiscount) + getString(R.string.ins));
                                    priceAfterDiscount.setText(tvTotalPrice.getText().toString());
                                }
                            } else {
                                if (!(str.equals(""))) {
                                    float val = Float.parseFloat(str);
                                    double originalTotalPrice=0;
                                    double salePrice=0;
                                    for (OrderDetails o : orderList) {
                                        originalTotalPrice += (o.getUnitPrice() * o.getQuantity())*(1-o.getDiscount()/100);
                                        salePrice+=o.getUnitPrice()*o.getQuantity();
                                    }
                                    if (originalTotalPrice*(1-(val/100)) >=(1- (X/100))*salePrice) {
                                        for (OrderDetails o : orderList) {
                                            //o.setDiscount(val);
                                        }
                                        double saleTotalPrice = 0;
                                        double SaleOriginalityPrice = 0;
                                        for (OrderDetails o : orderList) {
                                            saleTotalPrice += o.getItemTotalPrice();

                                            SaleOriginalityPrice += (o.getUnitPrice() * o.getQuantity())*(1-o.getDiscount()/100);
                                        }
                                        totalDiscount.setText(Util.makePrice(SaleOriginalityPrice*val/100 ) + getString(R.string.ins));
                                        priceAfterDiscount.setText(SaleOriginalityPrice*(1-val/100) + getString(R.string.ins));

                                    } else {
                                        totalDiscount.setText(Util.makePrice(valueOfDiscount) + getString(R.string.ins));
                                        priceAfterDiscount.setText(tvTotalPrice.getText().toString());
                                        Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                        et.setBackgroundResource(R.drawable.backtext);
                                    }
                                } else {
                                    totalDiscount.setText(Util.makePrice(valueOfDiscount) + getString(R.string.ins));
                                    priceAfterDiscount.setText(tvTotalPrice.getText().toString());
                                }
                            }
                        }
                    });

                    btOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String str = et.getText().toString();
                            double X = SESSION._EMPLOYEE.getPresent();
                            if (sw.isChecked()) {
                                if (!(str.equals(""))) {

                                    double d = Double.parseDouble(str);
                                    double originalTotalPrice = 0;
                                    for (OrderDetails o : SESSION._ORDER_DETAILES) {
                                        originalTotalPrice += (o.getUnitPrice() * o.getQuantity())*(1-o.getDiscount()/100);
                                    }
                                    if ((1 - (d / originalTotalPrice) <= (X / 100))) {
                                        double val = (1 - (d / originalTotalPrice)) * 100;
                                        valueOfDiscount = val;
                                        SESSION._ORDERS.cartDiscount=val;
                                        refreshCart();
                                        discountDialog.cancel();
                                    } else {
                                        Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            } else {
                                if (!(str.equals(""))) {
                                    float val = Float.parseFloat(str);
                                    valueOfDiscount = val;
                                    double originalTotalPrice=0;
                                    double salePrice=0;
                                    for (OrderDetails o : orderList) {
                                        originalTotalPrice += (o.getUnitPrice() * o.getQuantity())*(1-o.getDiscount()/100);
                                        salePrice+=o.getUnitPrice()*o.getQuantity();
                                    }
                                    if (originalTotalPrice*(1-(val/100)) >=(1- (X/100))*salePrice){
                                        valueOfDiscount = val;
                                        SESSION._ORDERS.cartDiscount=val;
                                        refreshCart();
                                        discountDialog.cancel();
                                    } else {
                                        Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });

                } else {
                    Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.please_add_item), Toast.LENGTH_SHORT);
                }
            }
        });

        //endregion Percent Product


        etSearch.setFocusable(true);
        etSearch.requestFocus();
        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etSearch.setFocusable(true);
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        printedRows = new ArrayList<String>();
        printedRows.add(0, "סוג אשראי");
        printedRows.add(1, "מספר תשלומים");
        printedRows.add(2, "מספר כרטיס");
        printedRows.add(3, "שם מסוף");
        printedRows.add(4, "מספר אישור");
        printedRows.add(5, "UID");
        printedRows.add(6, "תאריך ושעת העסקה");
        printedRows.add(7, "מטבע");
        printedRows.add(8, "סכום העסקה");
        printedRows.add(9, "סוג העסקה");
        printedRows.add(10, "מספר שובר");
        printedRows.add(11, "סכום העסקה לאחר המרה");

        //check starting day report A
    }

    @Override
    protected void onResume() {
        refreshCart();
        super.onResume();

        Bundle extras = getIntent().getExtras();
     if (extras != null) {
         if(!extras.getString("orderJson").equals("")){
             try {
                 orderDocumentFlag=true;
                 clearCart();
                 Log.d("orderJson",extras.getString("orderJson"));
                 final JSONObject orderDocJsonObj =new JSONObject(extras.getString("orderJson"));
                 orderDocNum = orderDocJsonObj.getString("docNum");
                 final JSONObject jsonObject = new JSONObject(String.valueOf(orderDocJsonObj.getJSONObject("documentsData").getJSONObject("order")));
                 Log.d("documentsData",jsonObject.toString());
                 SETTINGS.orderDocument=orderDocJsonObj;
                 JSONArray items = jsonObject.getJSONArray("cartDetailsList");
                 final JSONObject customerJson = orderDocJsonObj.getJSONObject("documentsData").getJSONObject("customer");
                 JSONObject a =  orderDocJsonObj.getJSONObject("documentsData").getJSONObject("order");
                 Order order = new Order(a.getLong("byUser"),new Timestamp(Long.parseLong(orderDocJsonObj.getJSONObject("documentsData").getString("date"))),0,false,orderDocJsonObj.getJSONObject("documentsData").getDouble("total"),0);
                 SESSION._EMPLOYEE.setEmployeeId(a.getLong("byUser"));
                 Customer customer = customerDBAdapter.getCustomerByID(Long.parseLong(orderDocJsonObj.getJSONObject("documentsData").getJSONObject("customer").getString("customerId")));
                 order.setCustomer(customer);
                 SESSION._ORDERS=order;
                 for (int i=0;i<items.length();i++){
                     Product p = null;
                     JSONObject orderDetailsJson =items.getJSONObject(i);
                     if(Long.parseLong(orderDetailsJson.getString("productId"))==-1){
                         p=new Product(Long.parseLong(String.valueOf(-1)),"General","General",orderDetailsJson.getDouble("unitPrice"),"0","0",Long.parseLong(String.valueOf(1)),Long.parseLong(String.valueOf(1)));
                     }else {
                         p = productDBAdapter.getProductByBarCode(orderDetailsJson.getString("sku"));
                     }
                     OrderDetails orderDetails= new OrderDetails(orderDetailsJson.getInt("quantity"),orderDetailsJson.getDouble("userOffer"),p,orderDetailsJson.getDouble("amount"),orderDetailsJson.getDouble("unitPrice"),orderDetailsJson.getDouble("discount"));
                        SESSION._ORDER_DETAILES.add(orderDetails);
                 }
                 saleDetailsListViewAdapter = new SaleDetailsListViewAdapter(getApplicationContext(), R.layout.list_adapter_row_main_screen_sales_details, SESSION._ORDER_DETAILES);
                 lvOrder.setAdapter(saleDetailsListViewAdapter);
                 if (SESSION._ORDERS.getCustomer() != null)
                     setCustomer(SESSION._ORDERS.getCustomer());
                 refreshCart();
             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }
         //str = extras.getString("orderJson");
         }

    }

    //region fragment Touch Pad

    private void showAlertDialogResumePauseSale() {
        // Prepare grid view
        GridView gridView = new GridView(this);
        final Dialog builder = new Dialog(this);

        List<Integer> mList = new ArrayList<Integer>();
        for (int i = 0; i < SESSION._SALES.size(); i++) {
            mList.add(SESSION._SALES.get(i).first);
        }

        gridView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, mList));
        gridView.setNumColumns(5);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                resumeSale(SESSION._SALES.get(position).second);
                Toast.makeText(SalesCartActivity.this, getString(R.string.resume_deal_number) + " " + (SESSION._SALES.get(position).first), Toast.LENGTH_SHORT).show();
                SESSION._SALES.remove(SESSION._SALES.get(position));

                builder.cancel();
            }
        });

        // Set grid view to alertDialog


        builder.setContentView(gridView);
        //builder.setView(gridView);
        builder.setTitle(getString(R.string.list_of_pause_sales));
        builder.show();
    }

    public void quickPriceButtonClick(View view) throws JSONException {
        String str = ((Button) view).getText().toString();
        if (str.equals("")) {
            return;
        }
        if (Double.parseDouble(str) != 0)
            addToCart(new Product(-1, getApplicationContext().getResources().getString(R.string.general), getApplicationContext().getResources().getString(R.string.general), Double.parseDouble(str), SESSION._EMPLOYEE.getEmployeeId(), "", ""));
    }


    private void showTouchPad(boolean b) {
        if (!b) {
            TouchPadFragment fTP = new TouchPadFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.mainActivity_fragmentTochPad, fTP);
            transaction.commit();
        } else {
            HorizontalScrollView hsv = (HorizontalScrollView) findViewById(R.id.mainActivity_svDepartment);
        }
    }

    public void touchPadClick(View view) throws JSONException {
        switch (view.getId()) {
            case R.id.touchPadFragment_bt0:
                touchPadPressed += 0;
                break;
            case R.id.touchPadFragment_bt1:
                touchPadPressed += 1;
                break;
            case R.id.touchPadFragment_bt2:
                touchPadPressed += 2;
                break;
            case R.id.touchPadFragment_bt3:
                touchPadPressed += 3;
                break;
            case R.id.touchPadFragment_bt4:
                touchPadPressed += 4;
                break;
            case R.id.touchPadFragment_bt5:
                touchPadPressed += 5;
                break;
            case R.id.touchPadFragment_bt6:
                touchPadPressed += 6;
                break;
            case R.id.touchPadFragment_bt7:
                touchPadPressed += 7;
                break;
            case R.id.touchPadFragment_bt8:
                touchPadPressed += 8;
                break;
            case R.id.touchPadFragment_bt9:
                touchPadPressed += 9;
                break;
            case R.id.touchPadFragment_btCE:
                if (!touchPadPressed.equals(""))
                    touchPadPressed = Util.removeLastChar(touchPadPressed);
                removeOrderItemSelection();
                refreshCart();
                break;
            case R.id.touchPadFragment_btEnter:
                if (!touchPadPressed.equals(""))
                    addToCart(new Product(-1, getApplicationContext().getResources().getString(R.string.general), getApplicationContext().getResources().getString(R.string.general), Double.parseDouble(touchPadPressed), SESSION._EMPLOYEE.getEmployeeId(), "", ""));
                touchPadPressed = "";
                break;
            case R.id.touchPadFragment_btDot:
                if (touchPadPressed.indexOf(".") < 0)
                    touchPadPressed += ".";
                break;
            case R.id.touchPadFragment_btCredit:
                if (!touchPadPressed.equals("")) {
                    double newValue = Util.convertSign(Double.parseDouble(touchPadPressed));
                    touchPadPressed = String.valueOf(newValue);
                }
                break;
        }
        TextView tirh = (TextView) this.findViewById(R.id.touchPadFragment_tvView);
        tirh.setText(touchPadPressed);
    }

    //endregion

    private void removeOrderItemSelection() {
        saleDetailsListViewAdapter.setSelected(-1);
        if (selectedIteminCartList != null) {
            selectedIteminCartList.findViewById(R.id.rowSaleDetails_LLMethods).setVisibility(View.GONE);
            selectedIteminCartList.findViewById(R.id.saleManLayout).setVisibility(View.GONE);
            selectedIteminCartList.findViewById(R.id.discountLayout).setVisibility(View.GONE);
            selectedIteminCartList.setBackgroundColor(getResources().getColor(R.color.white));
            selectedOrderOnCart = null;
        }
    }

    public void clearCart() {
        mainActivity_btnRemoveCustomer.setVisibility(View.GONE);
        linearLayoutCustomerBalance.setVisibility(View.GONE);
        valueOfDiscount = 0.0;
        clubDiscount = 0;
        clubPoint = 0;
        clubAmount = 0;
        Ppoint = 0;
        salesSaleMan.setText(getString(R.string.sales_man));
        SESSION._ORDERS.cartDiscount = 0;
        SESSION._ORDERS.setCartDiscount(0);
        SESSION._Rest();
        customerName_EditText.setText("");
        saleDetailsListViewAdapter = new SaleDetailsListViewAdapter(getApplicationContext(), R.layout.list_adapter_row_main_screen_sales_details, SESSION._ORDER_DETAILES);
        lvOrder.setAdapter(saleDetailsListViewAdapter);
        custmerAssetstIdList = new ArrayList<Long>();
        orderIdList = new ArrayList<OrderDetails>();
        orderId = new ArrayList<Long>();
        offerDBAdapter = new OfferDBAdapter(this);
        offerDBAdapter.open();
        // Offer offer=offerDBAdapter.getAllValidOffers();


        calculateTotalPrice();
    }

    public void resumeSale(Order s) {
        if (SESSION._ORDER_DETAILES.size() != 0) {
            Order sa = new Order(SESSION._ORDERS);

            sa.setOrders(SESSION._ORDER_DETAILES);
            if (SESSION._SALES == null)
                SESSION._SALES = new ArrayList<Pair<Integer, Order>>();
            SESSION._SALES.add(new Pair<>(++SESSION.TEMP_NUMBER, sa));
            clearCart();
        }

        SESSION._ORDERS = new Order(s);
        SESSION._ORDER_DETAILES = s.getOrders();
        saleDetailsListViewAdapter = new SaleDetailsListViewAdapter(getApplicationContext(), R.layout.list_adapter_row_main_screen_sales_details, SESSION._ORDER_DETAILES);
        lvOrder.setAdapter(saleDetailsListViewAdapter);
        if (SESSION._ORDERS.getCustomer() != null)
            setCustomer(SESSION._ORDERS.getCustomer());
        refreshCart();
    }

    protected void calculateTotalPriceWithOffers(List<Offer> offers) {

        availableRule3 = false;
        parcentForRule3 = 0.0;
        priceFoeRule7 = 0;
        availableRule11 = false;
        amountForRule11 = 0;
        DiscountamountForRule11 = 0;
        ParcentForRule8 = 0.0;

        ///end of offer list
        ////start order calculation and excecute offer


        saleTotalPrice = 0;
        SumForClub = 0;
        double SaleOriginalityPrice = 0;

        for (OrderDetails o : SESSION._ORDER_DETAILES) {
            if (o.getProductId() == productIDForRule7) {
                if (SumForRule3Status || SumForRule11Status) {
                    saleTotalPrice += priceFoeRule7 * o.getQuantity();
                    SaleOriginalityPrice += (o.getUnitPrice() * o.getQuantity());
                    totalSaved = (SaleOriginalityPrice - saleTotalPrice);

                } else {
                    SumForRule3 += priceFoeRule7;
                    SumForRule11 += priceFoeRule7;


                }
                if (clubStatusForRule7) {
                    SumForClub += priceFoeRule7;
                }


            } else if (o.getProductId() == productIDForRule8) {
                if (SumForRule3Status || SumForRule11Status) {
                    saleTotalPrice += o.getItemTotalPrice() - o.getItemTotalPrice() * ParcentForRule8;
                    SaleOriginalityPrice += (o.getUnitPrice() * o.getQuantity());
                    totalSaved = (SaleOriginalityPrice - saleTotalPrice);

                } else {
                    SumForRule3 += o.getItemTotalPrice() - o.getItemTotalPrice() * ParcentForRule8;
                    SumForRule11 += o.getItemTotalPrice() - o.getItemTotalPrice() * ParcentForRule8;
                }
                if (clubStatusForRule8) {
                    SumForClub += o.getItemTotalPrice() - o.getItemTotalPrice() * ParcentForRule8;
                }

            } else if (o.getProductId() == productIdForRule5) {
                stausForRule5 = true;
                i = o.getItemTotalPrice();

            } else {
                saleTotalPrice += o.getItemTotalPrice();
                SumForClub += o.getItemTotalPrice();

                SaleOriginalityPrice += (o.getUnitPrice() * o.getQuantity());
            }
        }


        if (customerClubId != 0) {
            if (clubType == 1) {

                SumForClub = SumForClub * clubDiscount;


                saleTotalPrice = saleTotalPrice - SumForClub;
                if (SumForRule3Status) {
                    saleTotalPrice = saleTotalPrice - (int) saleTotalPrice * parcentForRule3;
                } else if (!SumForRule3Status) {

                    saleTotalPrice = saleTotalPrice - (int) saleTotalPrice * parcentForRule3;
                    saleTotalPrice += SumForRule3;


                }

                if (SumForRule11Status) {
                    offerAmount = ((saleTotalPrice / amountForRule11) * DiscountamountForRule11);

                    saleTotalPrice = saleTotalPrice - offerAmount;
                } else if (!SumForRule11Status) {

                    offerAmount = ((saleTotalPrice / amountForRule11) * DiscountamountForRule11);

                    saleTotalPrice = saleTotalPrice - offerAmount;
                    saleTotalPrice += SumForRule11;


                }
                totalSaved = (SaleOriginalityPrice - saleTotalPrice);
                tvTotalSaved.setText(String.format(new Locale("en"), "%.2f", (totalSaved)) + " " + getString(R.string.ins));
                tvTotalPrice.setText(String.format(new Locale("en"), "%.2f", saleTotalPrice) + " " + getString(R.string.ins));
                SESSION._ORDERS.setNumberDiscount(Order.calculateNumberDiscount(saleTotalPrice,Double.parseDouble(Util.makePrice(saleTotalPrice))));
                SESSION._ORDERS.setTotalPrice(Double.parseDouble(Util.makePrice(saleTotalPrice)));

            }

        } else {
            if (SumForRule3Status) {
                saleTotalPrice = saleTotalPrice - (int) saleTotalPrice * parcentForRule3;
            } else if (!SumForRule3Status) {

                saleTotalPrice = saleTotalPrice - (int) saleTotalPrice * parcentForRule3;
                saleTotalPrice += SumForRule3;


            }

            if (SumForRule11Status) {
                offerAmount = ((int) (saleTotalPrice / amountForRule11) * DiscountamountForRule11);

                saleTotalPrice = saleTotalPrice - offerAmount;
            } else if (!SumForRule11Status) {

                offerAmount = ((int) (saleTotalPrice / amountForRule11) * DiscountamountForRule11);

                saleTotalPrice = saleTotalPrice - offerAmount;
                saleTotalPrice += SumForRule11;


            }
            totalSaved = (SaleOriginalityPrice - saleTotalPrice);
            tvTotalSaved.setText(String.format(new Locale("en"), "%.2f", (totalSaved)) + " " + getString(R.string.ins));
            tvTotalPrice.setText(String.format(new Locale("en"), "%.2f", saleTotalPrice) + " " + getString(R.string.ins));
            SESSION._ORDERS.setNumberDiscount(Order.calculateNumberDiscount(saleTotalPrice,Double.parseDouble(Util.makePrice(saleTotalPrice))));
            SESSION._ORDERS.setTotalPrice(Double.parseDouble(Util.makePrice(saleTotalPrice)));
        }


        if (stausForRule5) {

            AlertDialog alertDialog1 = new AlertDialog.Builder(SalesCartActivity.this).create();
            alertDialog1.setTitle("Alert");
            alertDialog1.setMessage("Did you want to buy this product with offer and take gift");
            alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            saleTotalPrice += priceForRule5;
                            dialog.dismiss();

                            //   SaleOriginalityPrice += rule5.getPaidAmount() * o.getQuantity();
                        }
                    });
            alertDialog1.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            saleTotalPrice += i;
                            dialog.dismiss();

                            //   SaleOriginalityPrice += rule5.getPaidAmount() * o.getQuantity();
                        }
                    });
            alertDialog1.show();


        }

        totalSaved = (SaleOriginalityPrice - saleTotalPrice);
        tvTotalSaved.setText(String.format(new Locale("en"), "%.2f", (totalSaved)) + " " + getString(R.string.ins));
        tvTotalPrice.setText(String.format(new Locale("en"), "%.2f", saleTotalPrice) + " " + getString(R.string.ins));
        SESSION._ORDERS.setNumberDiscount(Order.calculateNumberDiscount(saleTotalPrice,Double.parseDouble(Util.makePrice(saleTotalPrice))));
        SESSION._ORDERS.setTotalPrice(Double.parseDouble(Util.makePrice(saleTotalPrice)));

        offerDBAdapter.close();
    }

    public double tempSaleTotalPrice = 0;
    public int OfferQanPerUnitForProduct = 0;
    public int OfferQanGiftPerUnitForProduct = 0;
    public int prevCount = 0;
    public int currCount = 0;
    static Map<Long, Offer> offers = new HashMap<Long, Offer>();
    protected void calculateTotalPrice()  {
        tempSaleTotalPrice = 0;
        String a = tvTotalPrice.getText().toString();
        String b = a.replace(getString(R.string.ins), "");
        double SaleOriginalityPrice = 0;


        saleTotalPrice = 0;
        for (OrderDetails o : SESSION._ORDER_DETAILES) {
            if(o.giftProduct) continue;
            if(!o.scannable) continue;

            try {
                calculateOfferForOrderDetails(o);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (o.getOffer() != null) {
                Log.d("hasOffer", o.getOffer() + "");
            }

            for (Offer offer : o.offerList) {
                if(!offers.containsKey(offer.getOfferId())){
                    offers.put(offer.getOfferId(), offer);
                }
            }
        }
        try {
            if (OfferController.executeCategoryOffers(SESSION._ORDER_DETAILES, new ArrayList<Offer>(offers.values()))) {
                //refreshCart();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (OrderDetails o : SESSION._ORDER_DETAILES) {
            saleTotalPrice += o.getItemTotalPrice();
            SaleOriginalityPrice += (o.getUnitPrice() * o.getQuantity());
        }

        if (SESSION._ORDERS.cartDiscount != 0&& SESSION._ORDER_DETAILES.size()>0) {
            //show the discount view
            llCartDiscount.setVisibility(View.VISIBLE);
            tvTotalPriceBeforeCartDiscount.setVisibility(View.VISIBLE);

            tvTotalPriceBeforeCartDiscount.setText(Util.makePrice(saleTotalPrice) + " " + getString(R.string.ins));
            tvCartDiscountValue.setText(Util.makePrice(SESSION._ORDERS.cartDiscount) + " %");
            //calculate new total price
            saleTotalPrice = saleTotalPrice - (saleTotalPrice * (SESSION._ORDERS.cartDiscount / 100));
        } else {
            //hide cart discount view
            llCartDiscount.setVisibility(View.GONE);
            tvTotalPriceBeforeCartDiscount.setVisibility(View.GONE);
        }

        if (customerClubId != 0) {
            if (clubType == 1) {
                //club type clubDiscount
                saleTotalPrice = saleTotalPrice - (saleTotalPrice * (clubDiscount / 100));
            } else if (clubType == 2 || clubType == 0) {
                //clubPoint = ((int) (saleTotalPrice / clubAmount) * clubPoint);
            }
        }

        totalSaved = (SaleOriginalityPrice - saleTotalPrice);
        tvTotalSaved.setText(String.format(new Locale("en"), "%.2f", (totalSaved)) + " " + getString(R.string.ins));
        tvTotalPrice.setText(String.format(new Locale("en"), "%.2f", saleTotalPrice) + " " + getString(R.string.ins));
        SESSION._ORDERS.setNumberDiscount(Order.calculateNumberDiscount(saleTotalPrice,Double.parseDouble(Util.makePrice(saleTotalPrice))));
        SESSION._ORDERS.setTotalPrice(Double.parseDouble(Util.makePrice(saleTotalPrice)));
        saleDetailsListViewAdapter.notifyDataSetChanged();

    }

    private void removeFromCart(int index) {
        OrderDetails orderDetails = SESSION._ORDER_DETAILES.get(index);

        if(!orderDetails.scannable||orderDetails.giftProduct)
            restCategoryOffers();
        SESSION._ORDER_DETAILES.remove(index);
        saleDetailsListViewAdapter.setSelected(-1);
        //restCategoryOffers();
        refreshCart();
    }

    void restCategoryOffers(){
        for (OrderDetails od : SESSION._ORDER_DETAILES) {
            od.setDiscount(0);

            od.scannable = true;
            od.giftProduct = false;
        }
    }

    private void addToCart(Product p) throws JSONException {
        boolean isMatch = false;

        //test if cart have this order before insert to cart and order have'nt discount
        for (int i = 0; i < SESSION._ORDER_DETAILES.size(); i++) {
            OrderDetails o = SESSION._ORDER_DETAILES.get(i);
            //Log.d("ORDER_DETAILS", o.toString());
            //Log.d("Product", p.toString());
            if (o.getProduct().equals(p) && o.getProduct().getProductId() != -1&&!o.giftProduct&&o.scannable) {
                SESSION._ORDER_DETAILES.get(i).setCount(SESSION._ORDER_DETAILES.get(i).getQuantity() + 1);
                isMatch = true;
                break;
            }
        }
        if (!isMatch) {
            SESSION._ORDER_DETAILES.add(new OrderDetails(1, 0, p, p.getPrice(), p.getPrice(), 0));
        }
        //restCategoryOffers();
        removeOrderItemSelection();
        refreshCart();
    }

    private void increaseItemOnCart(int index) {
        OrderDetails orderDetails = SESSION._ORDER_DETAILES.get(index);
        SESSION._ORDER_DETAILES.get(index).increaseCount();

        if(!orderDetails.scannable||orderDetails.giftProduct)
            restCategoryOffers();
        restCategoryOffers();
        calculateTotalPrice();
    }

    private void decreaseItemOnCart(int index) {
        OrderDetails orderDetails = SESSION._ORDER_DETAILES.get(index);
        SESSION._ORDER_DETAILES.get(index).decreaseCount();

        if(!orderDetails.scannable||orderDetails.giftProduct)
            restCategoryOffers();
        restCategoryOffers();
        calculateTotalPrice();
    }

    private void refreshCart() {
        calculateTotalPrice();
    }

    private void enterKeyPressed(String barcodeScanned) throws JSONException {
        Product product = productDBAdapter.getProductByBarCode(barcodeScanned);
        final Intent intent = new Intent(SalesCartActivity.this, ProductsActivity.class);
        intent.putExtra("barcode", barcodeScanned);
        if (product != null) {
            addToCart(product);
        } else {
            new AlertDialog.Builder(SalesCartActivity.this)
                    .setTitle("Add Product")
                    .setMessage("Are you want to add this product?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            startActivity(intent);
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
        barcodeScanned = "";
        etSearch.setText("");
    }

    private void loadMoreProduct() {
        productLoadItemOffset += productCountLoad;
        final int id = prseedButtonDepartments.getId();
        final String searchWord = etSearch.getText().toString();
        final ProgressDialog dialog = new ProgressDialog(SalesCartActivity.this);
        dialog.setTitle(getBaseContext().getString(R.string.wait_for_finish));

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                dialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                All_productsList = productList;
                ProductCatalogGridViewAdapter adapter = new ProductCatalogGridViewAdapter(getApplicationContext(), All_productsList);
                lvProducts.setAdapter(adapter);
                gvProducts.setAdapter(adapter);
                dialog.cancel();
            }

            @Override
            protected Void doInBackground(Void... params) {
                if (!searchWord.equals("")) {
                    productList.addAll(productDBAdapter.getAllProductsByHint(searchWord, productLoadItemOffset, productCountLoad));
                } else if (id == 0) {
                    productList.addAll(productDBAdapter.getTopProducts(productLoadItemOffset, productCountLoad));
                } else {
                    productList.addAll(productDBAdapter.getAllProductsByCategory(id, productLoadItemOffset, productCountLoad));
                }
                return null;
            }
        }.execute();
    }


    /*
        private void printAndOpenCashBoxWINTEC(String mainAns, final String mainMer, final String mainCli) {
            final UsbPrinter printer = new UsbPrinter(1155, 30016);

            final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle(getBaseContext().getString(R.string.wait_for_finish_printing));

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    dialog.show();
                    ////Hebrew 15 Windows-1255

                    SESSION._ORDERS.setTotal_price(saleTotalPrice);
                    printer.PRN_Init();
                    printer.PRN_PrintAndFeedLine(11);
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    printer.PRN_PrintAndFeedLine(11);
                    printer.PRN_HalfCutPaper();

                    //pos.cashdrawerOpen(0,20,20);
                    dialog.cancel();
                    clearCart();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    InvoiceImg invoiceImg = new InvoiceImg(MainActivity.this);
                    if (SESSION._ORDERS.getPayment().getPaymentWay().equals(CREDIT_CARD)) {

                        printer.PRN_PrintDotBitmap(invoiceImg.creditCardInvoice(SESSION._ORDERS, false, mainMer), 0);
                        printer.PRN_PrintAndFeedLine(11);
                        printer.PRN_HalfCutPaper();

                        printer.PRN_PrintDotBitmap(invoiceImg.creditCardInvoice(SESSION._ORDERS, false, mainCli), 0);
                    } else if (SESSION._CHECKS_HOLDER != null && SESSION._CHECKS_HOLDER.size() > 0) {
                        printer.PRN_PrintDotBitmap(invoiceImg.normalInvoice(SESSION._ORDERS.getCashPaymentId(), SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, SESSION._CHECKS_HOLDER), 0);
                    } else {
                        printer.PRN_PrintDotBitmap(invoiceImg.normalInvoice(SESSION._ORDERS.getCashPaymentId(), SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, null), 0);
                    }
                    return null;
                }
            }.execute();
        }
    */
    private void printAndOpenCashBoxBTP880(final String mainAns, final String mainMer, final String mainCli) {
        final POSInterfaceAPI posInterfaceAPI = new POSUSBAPI(SalesCartActivity.this);
        // final UsbPrinter printer = new UsbPrinter(1155, 30016);

        final ProgressDialog dialog = new ProgressDialog(SalesCartActivity.this);
        dialog.setTitle(getBaseContext().getString(R.string.wait_for_finish_printing));

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                dialog.show();
                ////Hebrew 15 Windows-1255

                int i = posInterfaceAPI.OpenDevice();
                pos = new POSSDK(posInterfaceAPI);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                pos.systemFeedLine(2);
                pos.systemCutPaper(66, 0);
                pos.cashdrawerOpen(0, 20, 20);

                posInterfaceAPI.CloseDevice();
                dialog.cancel();
                clearCart();
            }

            @Override
            protected Void doInBackground(Void... params) {
                InvoiceImg invoiceImg = new InvoiceImg(SalesCartActivity.this);
                if (mainAns.equals("PINPAD")) {
                    HashMap<String, String> clientNote = new HashMap<String, String>();
                    HashMap<String, String> sellerNote = new HashMap<String, String>();

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(mainMer);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Iterator<String> itr = null;
                    try {
                        itr = jsonObject.getJSONObject("receipt").keys();

                        while (itr.hasNext()) {
                            String key = itr.next();
                            if (printedRows.contains(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"))) {
                                if (jsonObject.getJSONObject("receipt").getJSONObject(key).getString("category").equals("BOTH")) {
                                    clientNote.put(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"), jsonObject.getJSONObject("receipt").getJSONObject(key).getString("value"));
                                    sellerNote.put(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"), jsonObject.getJSONObject("receipt").getJSONObject(key).getString("value"));
                                } else if (jsonObject.getJSONObject("receipt").getJSONObject(key).getString("category").equals("CLIENT")) {
                                    clientNote.put(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"), jsonObject.getJSONObject("receipt").getJSONObject(key).getString("value"));
                                } else if (jsonObject.getJSONObject("receipt").getJSONObject(key).getString("category").equals("SELLER")) {
                                    sellerNote.put(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"), jsonObject.getJSONObject("receipt").getJSONObject(key).getString("value"));
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Bitmap seller = invoiceImg.pinPadInvoice(SESSION._ORDERS, false, sellerNote);
                    pos.imageStandardModeRasterPrint(seller, CONSTANT.PRINTER_PAGE_WIDTH);
                    pos.systemFeedLine(2);
                    pos.systemCutPaper(66, 0);
                    Bitmap client = invoiceImg.pinPadInvoice(SESSION._ORDERS, false, clientNote);
                    pos.imageStandardModeRasterPrint(client, CONSTANT.PRINTER_PAGE_WIDTH);

                } else if (SESSION._ORDERS.getPayment().getPaymentWay().equals(CREDIT_CARD)) {
                    pos.imageStandardModeRasterPrint(invoiceImg.creditCardInvoice(SESSION._ORDERS, false, mainMer), CONSTANT.PRINTER_PAGE_WIDTH);
                    pos.systemFeedLine(2);
                    pos.systemCutPaper(66, 0);
                    pos.imageStandardModeRasterPrint(invoiceImg.creditCardInvoice(SESSION._ORDERS, false, mainCli), CONSTANT.PRINTER_PAGE_WIDTH);
                } else if (SESSION._CHECKS_HOLDER != null && SESSION._CHECKS_HOLDER.size() > 0) {
                    pos.imageStandardModeRasterPrint(invoiceImg.normalInvoice(SESSION._ORDERS.getOrderId(), SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, SESSION._CHECKS_HOLDER), CONSTANT.PRINTER_PAGE_WIDTH);
                }   else  {
                pos.imageStandardModeRasterPrint(invoiceImg.normalInvoice(SESSION._ORDERS.getOrderId(), SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, null), CONSTANT.PRINTER_PAGE_WIDTH);
                }
                return null;
            }
        }.execute();
    }

    private static HPRTPrinterHelper HPRTPrinter = new HPRTPrinterHelper();

    private void printAndOpenCashBoxHPRT_TP805(final String mainAns, final String mainMer, final String mainCli) {
        if (HPRT_TP805.connect(this)) {
            final ProgressDialog dialog = new ProgressDialog(SalesCartActivity.this);
            dialog.setTitle(getBaseContext().getString(R.string.wait_for_finish_printing));

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    dialog.show();

                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    try {
                        HPRTPrinterHelper.CutPaper(HPRTPrinterHelper.HPRT_PARTIAL_CUT_FEED, 240);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        HPRTPrinterHelper.OpenCashdrawer(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            HPRTPrinterHelper.OpenCashdrawer(1);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            try {
                                HPRTPrinterHelper.OpenCashdrawer(2);
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    }

                    dialog.cancel();
                    clearCart();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    InvoiceImg invoiceImg = new InvoiceImg(SalesCartActivity.this);
                    byte b = 0;
                    try {
                        if (mainAns.equals("PINPAD")) {
                            HashMap<String, String> clientNote = new HashMap<String, String>();
                            HashMap<String, String> sellerNote = new HashMap<String, String>();

                            JSONObject jsonObject = new JSONObject(mainMer);
                            Iterator<String> itr = null;
                            try {
                                itr = jsonObject.getJSONObject("receipt").keys();

                                while (itr.hasNext()) {
                                    String key = itr.next();
                                    if (printedRows.contains(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"))) {
                                        if (jsonObject.getJSONObject("receipt").getJSONObject(key).getString("category").equals("BOTH")) {
                                            clientNote.put(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"), jsonObject.getJSONObject("receipt").getJSONObject(key).getString("value"));
                                            sellerNote.put(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"), jsonObject.getJSONObject("receipt").getJSONObject(key).getString("value"));
                                        } else if (jsonObject.getJSONObject("receipt").getJSONObject(key).getString("category").equals("CLIENT")) {
                                            clientNote.put(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"), jsonObject.getJSONObject("receipt").getJSONObject(key).getString("value"));
                                        } else if (jsonObject.getJSONObject("receipt").getJSONObject(key).getString("category").equals("SELLER")) {
                                            sellerNote.put(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"), jsonObject.getJSONObject("receipt").getJSONObject(key).getString("value"));
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Bitmap seller = invoiceImg.pinPadInvoice(SESSION._ORDERS, false, sellerNote);

                            HPRTPrinterHelper.PrintBitmap(seller, b, b, 300);
                            try {
                                HPRTPrinterHelper.CutPaper(HPRTPrinterHelper.HPRT_PARTIAL_CUT_FEED, 240);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Bitmap client = invoiceImg.pinPadInvoice(SESSION._ORDERS, false, clientNote);
                            HPRTPrinterHelper.PrintBitmap(client, b, b, 300);


                        } else if (SESSION._ORDERS.getPayment().getPaymentWay().equals(CREDIT_CARD)) {
                            Bitmap bitmap = invoiceImg.creditCardInvoice(SESSION._ORDERS, false, mainMer);

                            HPRTPrinterHelper.PrintBitmap(bitmap, b, b, 300);

                            try {
                                HPRTPrinterHelper.CutPaper(HPRTPrinterHelper.HPRT_PARTIAL_CUT_FEED, 240);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Bitmap bitmap2 = invoiceImg.creditCardInvoice(SESSION._ORDERS, false, mainCli);
                            HPRTPrinterHelper.PrintBitmap(bitmap2, b, b, 300);
                        } else if (SESSION._CHECKS_HOLDER != null && SESSION._CHECKS_HOLDER.size() > 0) {
                            Bitmap bitmap = invoiceImg.normalInvoice(SESSION._ORDERS.getOrderId(), SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, SESSION._CHECKS_HOLDER);
                            HPRTPrinterHelper.PrintBitmap(bitmap, b, b, 300);
                        }  else {
                            Bitmap bitmap = invoiceImg.normalInvoice(SESSION._ORDERS.getOrderId(), SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, null);
                            HPRTPrinterHelper.PrintBitmap(bitmap, b, b, 300);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        } else {
            new android.support.v7.app.AlertDialog.Builder(SalesCartActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                    .setTitle(getString(R.string.printer))
                    .setMessage(getString(R.string.please_connect_the_printer))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            //Toast.makeText(this, "Please connect the printer", Toast.LENGTH_SHORT).show();
        }
    }

    private void printAndOpenCashBoxSUNMI_T1(String mainAns, final String mainMer, final String mainCli) {
        //AidlUtil.getInstance().connectPrinterService(this);
        if (AidlUtil.getInstance().isConnect()) {
            final ProgressDialog dialog = new ProgressDialog(SalesCartActivity.this);
            dialog.setTitle(getBaseContext().getString(R.string.wait_for_finish_printing));

            dialog.show();
            InvoiceImg invoiceImg = new InvoiceImg(SalesCartActivity.this);
            byte b = 0;
            try {
                if (mainAns.equals("PINPAD")) {
                    HashMap<String, String> clientNote = new HashMap<String, String>();
                    HashMap<String, String> sellerNote = new HashMap<String, String>();

                    JSONObject jsonObject = new JSONObject(mainMer);
                    Iterator<String> itr = null;
                    try {
                        itr = jsonObject.getJSONObject("receipt").keys();

                        while (itr.hasNext()) {
                            String key = itr.next();
                            if (printedRows.contains(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"))) {
                                if (jsonObject.getJSONObject("receipt").getJSONObject(key).getString("category").equals("BOTH")) {
                                    clientNote.put(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"), jsonObject.getJSONObject("receipt").getJSONObject(key).getString("value"));
                                    sellerNote.put(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"), jsonObject.getJSONObject("receipt").getJSONObject(key).getString("value"));
                                } else if (jsonObject.getJSONObject("receipt").getJSONObject(key).getString("category").equals("CLIENT")) {
                                    clientNote.put(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"), jsonObject.getJSONObject("receipt").getJSONObject(key).getString("value"));
                                } else if (jsonObject.getJSONObject("receipt").getJSONObject(key).getString("category").equals("SELLER")) {
                                    sellerNote.put(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"), jsonObject.getJSONObject("receipt").getJSONObject(key).getString("value"));
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Bitmap seller = invoiceImg.pinPadInvoice(SESSION._ORDERS, false, sellerNote);
                    AidlUtil.getInstance().printBitmap(seller);

                    //Thread.sleep(100);

                    AidlUtil.getInstance().feed();
                    AidlUtil.getInstance().cut();


                    Bitmap client = invoiceImg.pinPadInvoice(SESSION._ORDERS, false, clientNote);
                    AidlUtil.getInstance().printBitmap(client);

                } else if (SESSION._ORDERS.getPayment().getPaymentWay().equals(CREDIT_CARD)) {
                    Bitmap bitmap = invoiceImg.creditCardInvoice(SESSION._ORDERS, false, mainMer);

                    AidlUtil.getInstance().printBitmap(bitmap);

                    //Thread.sleep(100);

                    AidlUtil.getInstance().feed();
                    AidlUtil.getInstance().cut();
                    //Thread.sleep(100);

                    Bitmap bitmap2 = invoiceImg.creditCardInvoice(SESSION._ORDERS, false, mainCli);
                    AidlUtil.getInstance().printBitmap(bitmap2);
                    //Thread.sleep(100);
                } else if (SESSION._CHECKS_HOLDER != null && SESSION._CHECKS_HOLDER.size() > 0) {
                    Bitmap bitmap = invoiceImg.normalInvoice(SESSION._ORDERS.getOrderId(), SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, SESSION._CHECKS_HOLDER);
                    AidlUtil.getInstance().printBitmap(bitmap);
                } else  {
                    Bitmap bitmap = invoiceImg.normalInvoice(SESSION._ORDERS.getOrderId(), SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, null);
                    AidlUtil.getInstance().printBitmap(bitmap);
                }
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

            try {
                AidlUtil.getInstance().openCashBox();
            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
            clearCart();
        } else {
            new android.support.v7.app.AlertDialog.Builder(SalesCartActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                    .setTitle(getString(R.string.printer))
                    .setMessage(getString(R.string.please_connect_the_printer))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            //Toast.makeText(this, "Printer Connect Error!", Toast.LENGTH_LONG).show();
        }
    }

    private void printSMS230(Bitmap bitmap) {
        String portSettings = "portable;escpos;l";
        String port = "BT:";
        int paperWidth = 576;
        paperWidth = 832; // 4inch (832 dot)
        paperWidth = 576; // 3inch (576 dot)1
        paperWidth = 384; // 2inch (384 dot)
        MiniPrinterFunctions.PrintBitmapImage(SalesCartActivity.this, port, portSettings, bitmap, paperWidth, true, true);

    }

    private void printAndOpenCashBoxSM_S230I(String mainAns, final String mainMer, final String mainCli) {
        if (true) {
            final ProgressDialog dialog = new ProgressDialog(SalesCartActivity.this);
            dialog.setTitle(getBaseContext().getString(R.string.wait_for_finish_printing));

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    dialog.show();
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    //feed paper

                    dialog.cancel();
                    clearCart();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    InvoiceImg invoiceImg = new InvoiceImg(SalesCartActivity.this);
                    byte b = 0;
                    try {
                        //// TODO: 13/06/2018 adding pinpad support
                        if (SESSION._ORDERS.getPayment().getPaymentWay().equals(CREDIT_CARD)) {
                            Bitmap bitmap = invoiceImg.creditCardInvoice(SESSION._ORDERS, false, mainMer);
                            printSMS230(bitmap);
                            //cut
                            Bitmap bitmap2 = invoiceImg.creditCardInvoice(SESSION._ORDERS, false, mainCli);
                            printSMS230(bitmap2);
                        } else if (SESSION._CHECKS_HOLDER != null && SESSION._CHECKS_HOLDER.size() > 0) {
                            Bitmap bitmap = invoiceImg.normalInvoice(SESSION._ORDERS.getOrderId(), SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, SESSION._CHECKS_HOLDER);
                            printSMS230(bitmap);
                        } else {
                            Bitmap bitmap = invoiceImg.normalInvoice(SESSION._ORDERS.getOrderId(), SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, null);
                            printSMS230(bitmap);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        }

    }

    private void printAndOpenCashBox(String mainAns, final String mainMer, final String mainCli, int source) {
        switch (SETTINGS.printer) {
            case BTP880:
                printAndOpenCashBoxBTP880(mainAns, mainMer, mainCli);
                break;
            case HPRT_TP805:
                printAndOpenCashBoxHPRT_TP805(mainAns, mainMer, mainCli);
                break;
            case SUNMI_T1:
                printAndOpenCashBoxSUNMI_T1(mainAns, mainMer, mainCli);
                break;
            case SM_S230I:
                printAndOpenCashBoxSM_S230I(mainAns, mainMer, mainCli);
                break;
        }
        if (source == REQUEST_CASH_ACTIVITY_CODE || source == REQUEST_CASH_ACTIVITY_WITH_CURRENCY_CODE)
            currencyReturnsCustomDialogActivity.show();

    }

    private CurrencyReturnsCustomDialogActivity currencyReturnsCustomDialogActivity;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Long.valueOf(SESSION._ORDERS.getCustomerId()) == 0) {
            if (SESSION._ORDERS.getCustomer_name() == null) {

                if (customerName_EditText.getText().toString().equals("")) {
                    Customer customer = customerDBAdapter.getCustomerByName("guest");
                    SESSION._ORDERS.setCustomer(customer);
                    setCustomer(SESSION._ORDERS.getCustomer());

                } else {
                    Customer customer = customerDBAdapter.getCustomerByID(SESSION._ORDERS.getCustomerId());
                    SESSION._ORDERS.setCustomer(customer);
                    setCustomer(SESSION._ORDERS.getCustomer());
                }
            }
        }

        //region CreditCard
        if (requestCode == REQUEST_CREDIT_CARD_ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {
                if(orderDocumentFlag){
                    try {
                        updateOrderDocumentStatus();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                double totalPrice = data.getDoubleExtra(SalesCartActivity.COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE, 0.0f);
                SESSION._ORDERS.setTotalPrice(totalPrice);
                if (data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote).equals("anyType{}"))
                    return;
                SESSION._ORDERS.setTotalPaidAmount(SESSION._ORDERS.getTotalPrice());
                saleDBAdapter = new OrderDBAdapter(SalesCartActivity.this);
                saleDBAdapter.open();
                clubPoint = ((int) (SESSION._ORDERS.getTotalPrice() / clubAmount) * clubPoint);
                long saleID = saleDBAdapter.insertEntry(SESSION._ORDERS, customerId, customerName,false);
                Order order = saleDBAdapter.getOrderById(saleID);
                long tempSaleId = 0;
                // Club with point and amount
                if (clubType == 2) {
                    pointFromSale = ((int) (SESSION._ORDERS.getTotalPrice() * clubPoint) / clubAmount);
                    sum_pointDbAdapter.insertEntry(saleID, pointFromSale, customerId);
                }

                if (equalUsedPoint) {
                    saleTotalPrice = 0.0;
                    SESSION._ORDERS.setNumberDiscount(Order.calculateNumberDiscount(saleTotalPrice,Double.parseDouble(Util.makePrice(saleTotalPrice))));
                    SESSION._ORDERS.setTotalPrice(Double.parseDouble(Util.makePrice(saleTotalPrice)));
                    saleDBAdapter.updateEntry(SESSION._ORDERS);
                    usedpointDbAdapter.insertEntry(saleID, newPoint, customerId);
                }
                if (lessUsedPoint) {
                    saleTotalPrice = 0.0;
                    SESSION._ORDERS.setNumberDiscount(Order.calculateNumberDiscount(saleTotalPrice,Double.parseDouble(Util.makePrice(saleTotalPrice))));
                    SESSION._ORDERS.setTotalPrice(Double.parseDouble(Util.makePrice(saleTotalPrice)));
                    saleDBAdapter.updateEntry(SESSION._ORDERS);
                    usedpointDbAdapter.insertEntry(saleID, newPoint, customerId);
                }
                if (biggerUsedPoint) {
                    SESSION._ORDERS.setNumberDiscount(Order.calculateNumberDiscount(saleTotalPrice,Double.parseDouble(Util.makePrice(saleTotalPrice))));
                    SESSION._ORDERS.setTotalPrice(Double.parseDouble(Util.makePrice(saleTotalPrice)));
                    saleDBAdapter.updateEntry(SESSION._ORDERS);
                    usedpointDbAdapter.insertEntry(saleID, newPoint, customerId);
                }
                saleDBAdapter.close();
                CreditCardPaymentDBAdapter creditCardPaymentDBAdapter = new CreditCardPaymentDBAdapter(this);
                creditCardPaymentDBAdapter.open();
                CreditCardPayment ccp = SESSION._TEMP_CREDITCARD_PAYMNET;

                creditCardPaymentDBAdapter.insertEntry(saleID, ccp.getAmount(), ccp.getCreditCardCompanyName(), ccp.getTransactionType(), ccp.getLast4Digits(), ccp.getTransactionId(), ccp.getAnswer(), ccp.getPaymentsNumber()
                        , ccp.getFirstPaymentAmount(), ccp.getOtherPaymentAmount(), ccp.getCreditCardCompanyName());

                creditCardPaymentDBAdapter.close();

                orderDBAdapter = new OrderDetailsDBAdapter(SalesCartActivity.this);
                custmerAssetDB = new CustomerAssetDB(SalesCartActivity.this);
                orderDBAdapter.open();
                custmerAssetDB.open();
                SESSION._ORDERS.setOrderId(saleID);
                if (forSaleMan) {
                    tempSaleId = saleID;
                    custmerAssetDB.insertEntry(saleID, custmerSaleAssetstId, SESSION._ORDERS.getTotalPrice(), 0, "ORDER", SESSION._ORDERS.getCreatedAt());
                }
                // insert order region
                for (OrderDetails o : SESSION._ORDER_DETAILES) {
                    long orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(),order.getOrderKey());
                    orderId.add(orderid);
                    //   orderDBAdapter.insertEntry(o.getProductSku(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(),o.getCustomer_assistance_id());
                }
                // ORDER_DETAILS Sales man Region
                for (int i = 0; i < orderIdList.size(); i++) {
                    OrderDetails orderDetails = orderIdList.get(i);
                    long customerAssestId = custmerAssetstIdList.get(i);
                    for (int j = 0; j < SESSION._ORDER_DETAILES.size(); j++) {
                        OrderDetails o = SESSION._ORDER_DETAILES.get(j);
                        long tempOrderId = orderId.get(i);
                        if (o == orderDetails) {
                            if (custmerAssetstIdList.get(i) != custmerSaleAssetstId) {
                                o.setCustomer_assistance_id(custmerAssetstIdList.get(i));
                                custmerAssetDB.insertEntry(tempOrderId, customerAssestId, o.getPaidAmount(), 0, "ORDER_DETAILS", SESSION._ORDERS.getCreatedAt());
                            }
                        }
                    }
                }
                //update customer balance
                if (SESSION._ORDERS.getTotalPrice() < 0 && customer != null) {
                    Customer upDateCustomer = customer;
                    upDateCustomer.setBalance(SESSION._ORDERS.getTotalPrice() + customer.getBalance());
                    customerDBAdapter.updateEntry(upDateCustomer);
                }
                orderDBAdapter.close();
                custmerAssetDB.close();
                SESSION._ORDERS.setOrders(SESSION._ORDER_DETAILES);
                SESSION._ORDERS.setUser(SESSION._EMPLOYEE);

                PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(SalesCartActivity.this);
                paymentDBAdapter.open();

                long paymentID = paymentDBAdapter.insertEntry(CREDIT_CARD, saleTotalPrice, saleID,order.getOrderKey());

                paymentDBAdapter.close();

                Payment payment = new Payment(paymentID, CREDIT_CARD, saleTotalPrice, saleID);
                SESSION._ORDERS.setPayment(payment);


                Log.w("mainAns", data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY));
                Log.w("mainMer", data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_MerchantNote));
                Log.w("mainCli", data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote));


                printAndOpenCashBox(data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY),
                        data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_MerchantNote),
                        data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote), REQUEST_CREDIT_CARD_ACTIVITY_CODE);

                //get the invoice plugin
                //print invoice


                return;
            } else if (resultCode == RESULT_CANCELED) {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.fail))
                        .setMessage(getString(R.string.cant_finish_this_action))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }

        //endregion


        //region PinPad
        if (requestCode == REQUEST_PIN_PAD_ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {
                if(orderDocumentFlag){
                    try {
                        updateOrderDocumentStatus();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                CreditCardPayment ccp = new CreditCardPayment();

                //region prepare CC information
                JSONObject jsonObject = null;
                try {
                    String pinpadResult = data.getStringExtra(PinpadActivity.RESULT_INTENT_CODE_PIN_PAD_ACTIVITY_FULL_RESPONSE);
                    Log.i("PinPad Result", pinpadResult);
                    jsonObject = new JSONObject(pinpadResult);
                    JSONObject tr = jsonObject.getJSONObject("transaction");

                    ccp.setAmount(tr.getDouble("amount"));
                    ccp.setAnswer(pinpadResult);
                    ccp.setTransactionId(tr.getString("uid"));
                    ccp.setCreditCardCompanyName(tr.getString("cardBrand"));
                    ccp.setLast4Digits(tr.getString("cardNumber"));
                    ccp.setCardholder(tr.getString("cardHolderName"));
                    ccp.setPaymentsNumber(0);
                    ccp.setTransactionType(CreditCardTransactionType.NORMAL);

                    if (tr.getInt("numberOfPayments") > 0) {
                        ccp.setPaymentsNumber(tr.getInt("numberOfPayments") + 1);
                        ccp.setFirstPaymentAmount(tr.getDouble("firstPaymentAmount"));
                        ccp.setOtherPaymentAmount(tr.getDouble("paymentAmount"));
                        ccp.setTransactionType(CreditCardTransactionType.PAYMENTS);
                    }

                    if (!tr.getString("transactionType2").equals("CHARGE")) {
                        ccp.setTransactionType(CreditCardTransactionType.CREDIT);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //endregion

                //region save the transaction
                double totalPrice = data.getDoubleExtra(SalesCartActivity.COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE, 0.0f);
                SESSION._ORDERS.setTotalPrice(totalPrice);
                SESSION._ORDERS.setTotalPaidAmount(SESSION._ORDERS.getTotalPrice());
                saleDBAdapter = new OrderDBAdapter(SalesCartActivity.this);
                saleDBAdapter.open();
                clubPoint = ((int) (SESSION._ORDERS.getTotalPrice() / clubAmount) * clubPoint);
                long saleID = saleDBAdapter.insertEntry(SESSION._ORDERS, customerId, customerName,false);
                Order order = saleDBAdapter.getOrderById(saleID);
                saleDBAdapter.close();

                CreditCardPaymentDBAdapter creditCardPaymentDBAdapter = new CreditCardPaymentDBAdapter(this);
                creditCardPaymentDBAdapter.open();

                creditCardPaymentDBAdapter.insertEntry(saleID, ccp.getAmount(), ccp.getCreditCardCompanyName(), ccp.getTransactionType(), ccp.getLast4Digits(), ccp.getTransactionId(), ccp.getAnswer(), ccp.getPaymentsNumber()
                        , ccp.getFirstPaymentAmount(), ccp.getOtherPaymentAmount(), ccp.getCreditCardCompanyName());

                creditCardPaymentDBAdapter.close();

                orderDBAdapter = new OrderDetailsDBAdapter(SalesCartActivity.this);
                custmerAssetDB = new CustomerAssetDB(SalesCartActivity.this);
                orderDBAdapter.open();
                custmerAssetDB.open();
                SESSION._ORDERS.setOrderId(saleID);
                if (forSaleMan) {
                    custmerAssetDB.insertEntry(saleID, custmerSaleAssetstId, SESSION._ORDERS.getTotalPrice(), 0, "ORDER", SESSION._ORDERS.getCreatedAt());
                }
                // insert order region
                for (OrderDetails o : SESSION._ORDER_DETAILES) {
                    long orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(),order.getOrderKey());
                    orderId.add(orderid);
                    //   orderDBAdapter.insertEntry(o.getProductSku(), o.getCount(), o.getUserOffer(), saleID, o.getPrice(), o.getOriginal_price(), o.getDiscount(),o.getCustmerAssestId());
                }
                // ORDER_DETAILS Sales man Region
                for (int i = 0; i < orderIdList.size(); i++) {
                    OrderDetails orderDetails = orderIdList.get(i);
                    long customerAssestId = custmerAssetstIdList.get(i);
                    for (int j = 0; j < SESSION._ORDER_DETAILES.size(); j++) {
                        OrderDetails o = SESSION._ORDER_DETAILES.get(j);
                        long tempOrderId = orderId.get(i);
                        if (o == orderDetails) {
                            if (custmerAssetstIdList.get(i) != custmerSaleAssetstId) {
                                o.setCustomer_assistance_id(custmerAssetstIdList.get(i));
                                custmerAssetDB.insertEntry(tempOrderId, customerAssestId, o.getPaidAmount(), 0, "ORDER_DETAILS", SESSION._ORDERS.getCreatedAt());
                            }
                        }
                    }
                }
                //update customer balance
                if (SESSION._ORDERS.getTotalPrice() < 0 && customer != null) {
                    Customer upDateCustomer = customer;
                    upDateCustomer.setBalance(SESSION._ORDERS.getTotalPrice() + customer.getBalance());
                    customerDBAdapter.updateEntry(upDateCustomer);
                }
                orderDBAdapter.close();
                custmerAssetDB.close();
                SESSION._ORDERS.setOrders(SESSION._ORDER_DETAILES);
                SESSION._ORDERS.setUser(SESSION._EMPLOYEE);

                PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(SalesCartActivity.this);
                paymentDBAdapter.open();

                long paymentID = paymentDBAdapter.insertEntry(CREDIT_CARD, saleTotalPrice, saleID,order.getOrderKey());

                paymentDBAdapter.close();

                Payment payment = new Payment(paymentID, CREDIT_CARD, saleTotalPrice, saleID);
                SESSION._ORDERS.setPayment(payment);
                //endregion

                printAndOpenCashBox("PINPAD", jsonObject.toString(), "", REQUEST_PIN_PAD_ACTIVITY_CODE);

            } else if (resultCode == RESULT_CANCELED) {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.fail))
                        .setMessage(getString(R.string.cant_finish_this_action))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }


        }
        //endregion PinPad


        //region Checks

        if (requestCode == REQUEST_CHECKS_ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {
                if(orderDocumentFlag){
                    try {
                        updateOrderDocumentStatus();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                final double result = data.getDoubleExtra(ChecksActivity.LEAD_POS_RESULT_INTENT_CODE_CHECKS_ACTIVITY, 0.0f);
                double totalPrice = data.getDoubleExtra(SalesCartActivity.COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE, 0.0f);
                SESSION._ORDERS.setTotalPrice(totalPrice);
                SESSION._ORDERS.setTotalPaidAmount(result);
                saleDBAdapter = new OrderDBAdapter(SalesCartActivity.this);
                saleDBAdapter.open();
                clubPoint = ((int) (SESSION._ORDERS.getTotalPrice() / clubAmount) * clubPoint);
                long saleID = saleDBAdapter.insertEntry(SESSION._ORDERS, customerId, customerName,false);
                Order order = saleDBAdapter.getOrderById(saleID);
                // Club with point and amount
                if (clubType == 2 && clubAmount!=0) {
                    pointFromSale = ((int) (SESSION._ORDERS.getTotalPrice() * clubPoint) / clubAmount);
                    sum_pointDbAdapter.insertEntry(saleID, pointFromSale, customerId);
                }

                if (equalUsedPoint) {
                    saleTotalPrice = 0.0;
                    SESSION._ORDERS.setNumberDiscount(Order.calculateNumberDiscount(saleTotalPrice,Double.parseDouble(Util.makePrice(saleTotalPrice))));
                    SESSION._ORDERS.setTotalPrice(Double.parseDouble(Util.makePrice(saleTotalPrice)));
                    saleDBAdapter.updateEntry(SESSION._ORDERS);
                    usedpointDbAdapter.insertEntry(saleID, newPoint, customerId);
                }
                if (lessUsedPoint) {
                    saleTotalPrice = 0.0;
                    SESSION._ORDERS.setNumberDiscount(Order.calculateNumberDiscount(saleTotalPrice,Double.parseDouble(Util.makePrice(saleTotalPrice))));
                    SESSION._ORDERS.setTotalPrice(Double.parseDouble(Util.makePrice(saleTotalPrice)));
                    saleDBAdapter.updateEntry(SESSION._ORDERS);
                    usedpointDbAdapter.insertEntry(saleID, newPoint, customerId);
                }
                if (biggerUsedPoint) {
                    SESSION._ORDERS.setNumberDiscount(Order.calculateNumberDiscount(saleTotalPrice,Double.parseDouble(Util.makePrice(saleTotalPrice))));
                    SESSION._ORDERS.setTotalPrice(Double.parseDouble(Util.makePrice(saleTotalPrice)));
                    saleDBAdapter.updateEntry(SESSION._ORDERS);
                    usedpointDbAdapter.insertEntry(saleID, newPoint, customerId);
                }
                saleDBAdapter.close();

                orderDBAdapter = new OrderDetailsDBAdapter(SalesCartActivity.this);
                custmerAssetDB = new CustomerAssetDB(SalesCartActivity.this);
                orderDBAdapter.open();
                custmerAssetDB.open();
                SESSION._ORDERS.setOrderId(saleID);
                if (forSaleMan) {
                    custmerAssetDB.insertEntry(saleID, custmerSaleAssetstId, SESSION._ORDERS.getTotalPrice(), 0, "ORDER", SESSION._ORDERS.getCreatedAt());
                }

                // insert order region
                for (OrderDetails o : SESSION._ORDER_DETAILES) {
                    long orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(),order.getOrderKey());
                    orderId.add(orderid);
                    //   orderDBAdapter.insertEntry(o.getProductSku(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(),o.getCustomer_assistance_id());
                }
                // ORDER_DETAILS Sales man Region
                for (int i = 0; i < orderIdList.size(); i++) {
                    OrderDetails orderDetails = orderIdList.get(i);
                    long customerAssestId = custmerAssetstIdList.get(i);
                    for (int j = 0; j < SESSION._ORDER_DETAILES.size(); j++) {
                        OrderDetails o = SESSION._ORDER_DETAILES.get(j);
                        long tempOrderId = orderId.get(i);
                        if (o == orderDetails) {
                            if (custmerAssetstIdList.get(i) != custmerSaleAssetstId) {
                                o.setCustomer_assistance_id(custmerAssetstIdList.get(i));
                                custmerAssetDB.insertEntry(tempOrderId, customerAssestId, o.getPaidAmount(), 0, "ORDER_DETAILS", SESSION._ORDERS.getCreatedAt());
                            }
                        }
                    }
                }
                //update customer balance
                if (SESSION._ORDERS.getTotalPrice() < 0 && customer != null) {
                    Customer upDateCustomer = customer;
                    upDateCustomer.setBalance(SESSION._ORDERS.getTotalPrice() + customer.getBalance());
                    customerDBAdapter.updateEntry(upDateCustomer);
                }
                orderDBAdapter.close();

                PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(this);
                paymentDBAdapter.open();

                long paymentID = paymentDBAdapter.insertEntry(CHECKS, saleTotalPrice, saleID, order.getOrderKey());

                paymentDBAdapter.close();

                Payment payment = new Payment(paymentID, CHECKS, saleTotalPrice, saleID);
                SESSION._ORDERS.setPayment(payment);

                ChecksDBAdapter checksDBAdapter = new ChecksDBAdapter(this);
                checksDBAdapter.open();
                for (Check check : SESSION._CHECKS_HOLDER) {
                    checksDBAdapter.insertEntry(check.getCheckNum(), check.getBankNum(), check.getBranchNum(), check.getAccountNum(), check.getAmount(), check.getCreatedAt(), saleID);
                }
                checksDBAdapter.close();


                printAndOpenCashBox("", "", "", REQUEST_CHECKS_ACTIVITY_CODE);
                return;
            }
        }
        //endregion

        //region Cash Activity WithOut Currency Region
        if (requestCode == REQUEST_CASH_ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {
                if(orderDocumentFlag){
                    try {
                        updateOrderDocumentStatus();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(this);
                cashPaymentDBAdapter.open();
                PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(this);
                saleDBAdapter = new OrderDBAdapter(SalesCartActivity.this);
                orderDBAdapter = new OrderDetailsDBAdapter(SalesCartActivity.this);
                custmerAssetDB = new CustomerAssetDB(SalesCartActivity.this);
                long tempSaleId = 0;
                saleDBAdapter.open();
                orderDBAdapter.open();
                custmerAssetDB.open();
                paymentDBAdapter.open();

                // Get data from CashActivityWithOutCurrency
                double totalPaidWithOutCurrency = data.getDoubleExtra(OldCashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_WITHOUT_CURRENCY_TOTAL_PAID, 0.0f);
                double excess = data.getDoubleExtra(OldCashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_WITHOUT_CURRENCY_EXCESS_VALUE, 0.0f);
                double totalPrice = data.getDoubleExtra(SalesCartActivity.COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE, 0.0f);
                SESSION._ORDERS.setTotalPrice(totalPrice);
                SESSION._ORDERS.setTotalPaidAmount(totalPaidWithOutCurrency);

                clubPoint = ((int) (SESSION._ORDERS.getTotalPrice() / clubAmount) * clubPoint);
                saleIDforCash = saleDBAdapter.insertEntry(SESSION._ORDERS, customerId, customerName,false);
                Order order = saleDBAdapter.getOrderById(saleIDforCash);
                SESSION._ORDERS.setOrderId(saleIDforCash);

                currencyReturnsCustomDialogActivity = new CurrencyReturnsCustomDialogActivity(this, excess, new Order(SESSION._ORDERS));

                /// Club with point and amount
                if (clubType == 2) {
                    pointFromSale = ((int) (SESSION._ORDERS.getTotalPrice() * clubPoint) / clubAmount);
                    sum_pointDbAdapter.insertEntry(saleIDforCash, pointFromSale, customerId);
                }

                if (equalUsedPoint) {
                    saleTotalPrice = 0.0;
                    SESSION._ORDERS.setNumberDiscount(Order.calculateNumberDiscount(saleTotalPrice,Double.parseDouble(Util.makePrice(saleTotalPrice))));
                    SESSION._ORDERS.setTotalPrice(Double.parseDouble(Util.makePrice(saleTotalPrice)));
                    saleDBAdapter.updateEntry(SESSION._ORDERS);
                    usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, customerId);
                }
                if (lessUsedPoint) {
                    saleTotalPrice = 0.0;
                    SESSION._ORDERS.setNumberDiscount(Order.calculateNumberDiscount(saleTotalPrice,Double.parseDouble(Util.makePrice(saleTotalPrice))));
                    SESSION._ORDERS.setTotalPrice(Double.parseDouble(Util.makePrice(saleTotalPrice)));
                    saleDBAdapter.updateEntry(SESSION._ORDERS);
                    usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, customerId);
                }
                if (biggerUsedPoint) {
                    SESSION._ORDERS.setNumberDiscount(Order.calculateNumberDiscount(saleTotalPrice,Double.parseDouble(Util.makePrice(saleTotalPrice))));
                    SESSION._ORDERS.setTotalPrice(Double.parseDouble(Util.makePrice(saleTotalPrice)));
                    saleDBAdapter.updateEntry(SESSION._ORDERS);
                    usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, customerId);
                }
                // insert in ORDER_DETAILS , CustomerAssistant
                if (forSaleMan) {
                    tempSaleId = saleIDforCash;
                    custmerAssetDB.insertEntry(saleIDforCash, custmerSaleAssetstId, SESSION._ORDERS.getTotalPrice(), 0, "ORDER", SESSION._ORDERS.getCreatedAt());
                }
                // insert order region
                for (OrderDetails o : SESSION._ORDER_DETAILES) {
                    long orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleIDforCash, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(),order.getOrderKey());
                    orderId.add(orderid);
                    //   orderDBAdapter.insertEntry(o.getProductSku(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(),o.getCustomer_assistance_id());
                }
                // ORDER_DETAILS Sales man Region
                for (int i = 0; i < orderIdList.size(); i++) {
                    OrderDetails orderDetails = orderIdList.get(i);
                    long customerAssestId = custmerAssetstIdList.get(i);
                    for (int j = 0; j < SESSION._ORDER_DETAILES.size(); j++) {
                        OrderDetails o = SESSION._ORDER_DETAILES.get(j);
                        long tempOrderId = orderId.get(i);
                        if (o == orderDetails) {
                            if (custmerAssetstIdList.get(i) != custmerSaleAssetstId) {
                                o.setCustomer_assistance_id(custmerAssetstIdList.get(i));
                                custmerAssetDB.insertEntry(tempOrderId, customerAssestId, o.getPaidAmount(), 0, "ORDER_DETAILS", SESSION._ORDERS.getCreatedAt());
                            }
                        }
                    }
                }
                //update customer balance
                if (SESSION._ORDERS.getTotalPrice() < 0 && customer != null) {
                    Customer upDateCustomer = customer;
                    upDateCustomer.setBalance(SESSION._ORDERS.getTotalPrice() + customer.getBalance());
                    customerDBAdapter.updateEntry(upDateCustomer);
                }

                orderDBAdapter.close();
                custmerAssetDB.close();
                // End ORDER_DETAILS And CustomerAssistant Region
                // Payment Region
                long paymentID = paymentDBAdapter.insertEntry(CASH, saleTotalPrice, saleIDforCash,order.getOrderKey());

                Payment payment = new Payment(paymentID, CASH, saleTotalPrice, saleIDforCash);
                SESSION._ORDERS.setPayment(payment);
                SESSION._ORDERS.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                cashPaymentDBAdapter.insertEntry(saleIDforCash, saleTotalPrice, 0, new Timestamp(System.currentTimeMillis()),1,1);

                paymentDBAdapter.close();
                printAndOpenCashBox("", "", "", REQUEST_CASH_ACTIVITY_CODE);
                saleDBAdapter.close();
                return;
            }

        }
        //endregion

        //region Currency Cash Activity Region
        if (requestCode == REQUEST_CASH_ACTIVITY_WITH_CURRENCY_CODE) {
            if (resultCode == RESULT_OK) {
                if(orderDocumentFlag){
                    try {
                        updateOrderDocumentStatus();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(this);
                PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(this);
                saleDBAdapter = new OrderDBAdapter(SalesCartActivity.this);
                orderDBAdapter = new OrderDetailsDBAdapter(SalesCartActivity.this);
                custmerAssetDB = new CustomerAssetDB(SalesCartActivity.this);
                cashPaymentDBAdapter.open();
                saleDBAdapter.open();
                orderDBAdapter.open();
                custmerAssetDB.open();
                paymentDBAdapter.open();
                long tempSaleId = 0;


                // Get data from CashActivityWithCurrency and insert in Cash Payment
                double totalPaidWithCurrency = data.getDoubleExtra(CashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_TOTAL_PAID, 0.0f);
                SESSION._ORDERS.setTotalPaidAmount(totalPaidWithCurrency);
                double firstCurrencyAmount = data.getDoubleExtra(CashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_FIRST_CURRENCY_AMOUNT, 0.0f);
                double secondCurrencyAmount = data.getDoubleExtra(CashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_SECOND_CURRENCY_AMOUNT, 0.0f);
                double excess = data.getDoubleExtra(CashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_EXCESS_VALUE, 0.0f);
                long secondCurrencyId = data.getLongExtra(CashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_SECOND_CURRENCY_ID, 0);
                long firstCurrencyId = data.getLongExtra(CashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_FIRST_CURRENCY_ID, 0);

                saleIDforCash = saleDBAdapter.insertEntry(SESSION._ORDERS, customerId, customerName,false);
                Order order = saleDBAdapter.getOrderById(saleIDforCash);
                SESSION._ORDERS.setOrderId(saleIDforCash);
                currencyReturnsCustomDialogActivity = new CurrencyReturnsCustomDialogActivity(this, excess, new Order(SESSION._ORDERS));

                if (firstCurrencyAmount > 0) {
                    //         cashPaymentDBAdapter.insertEntry(saleIDforCash, firstCurrencyAmount, firstCurrencyId, new Timestamp(System.currentTimeMillis()));
                }
                if (secondCurrencyAmount > 0) {
                    //      cashPaymentDBAdapter.insertEntry(saleIDforCash, secondCurrencyAmount, secondCurrencyId, new Timestamp(System.currentTimeMillis()));
                }
                cashPaymentDBAdapter.close();


                // Club with point and amount
                if (clubType == 2) {
                    pointFromSale = ((int) (SESSION._ORDERS.getTotalPrice() * clubPoint) / clubAmount);
                    sum_pointDbAdapter.insertEntry(saleIDforCash, pointFromSale, customerId);
                }

                if (equalUsedPoint) {
                    saleTotalPrice = 0.0;
                    SESSION._ORDERS.setNumberDiscount(Order.calculateNumberDiscount(saleTotalPrice,Double.parseDouble(Util.makePrice(saleTotalPrice))));
                    SESSION._ORDERS.setTotalPrice(Double.parseDouble(Util.makePrice(saleTotalPrice)));
                    saleDBAdapter.updateEntry(SESSION._ORDERS);
                    usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, customerId);
                }
                if (lessUsedPoint) {
                    saleTotalPrice = 0.0;
                    SESSION._ORDERS.setNumberDiscount(Order.calculateNumberDiscount(saleTotalPrice,Double.parseDouble(Util.makePrice(saleTotalPrice))));
                    SESSION._ORDERS.setTotalPrice(Double.parseDouble(Util.makePrice(saleTotalPrice)));
                    saleDBAdapter.updateEntry(SESSION._ORDERS);
                    usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, customerId);
                }
                if (biggerUsedPoint) {
                    SESSION._ORDERS.setNumberDiscount(Order.calculateNumberDiscount(saleTotalPrice,Double.parseDouble(Util.makePrice(saleTotalPrice))));
                    SESSION._ORDERS.setTotalPrice(Double.parseDouble(Util.makePrice(saleTotalPrice)));
                    saleDBAdapter.updateEntry(SESSION._ORDERS);
                    usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, customerId);
                }
                if (forSaleMan) {
                    tempSaleId = saleIDforCash;
                    custmerAssetDB.insertEntry(saleIDforCash, custmerSaleAssetstId, SESSION._ORDERS.getTotalPrice(), 0, "ORDER", SESSION._ORDERS.getCreatedAt());
                }
                // insert order region
                for (OrderDetails o : SESSION._ORDER_DETAILES) {
                    long orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleIDforCash, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(),order.getOrderKey());
                    orderId.add(orderid);
                    //   orderDBAdapter.insertEntry(o.getProductSku(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(),o.getCustomer_assistance_id());
                }
                // ORDER_DETAILS Sales man Region
                for (int i = 0; i < orderIdList.size(); i++) {
                    OrderDetails orderDetails = orderIdList.get(i);
                    long customerAssestId = custmerAssetstIdList.get(i);
                    for (int j = 0; j < SESSION._ORDER_DETAILES.size(); j++) {
                        OrderDetails o = SESSION._ORDER_DETAILES.get(j);
                        long tempOrderId = orderId.get(i);
                        if (o == orderDetails) {
                            if (custmerAssetstIdList.get(i) != custmerSaleAssetstId) {
                                o.setCustomer_assistance_id(custmerAssetstIdList.get(i));
                                custmerAssetDB.insertEntry(tempOrderId, customerAssestId, o.getPaidAmount(), 0, "ORDER_DETAILS", SESSION._ORDERS.getCreatedAt());
                            }
                        }
                    }
                }
                //update customer balance
                if (SESSION._ORDERS.getTotalPrice() < 0 && customer != null) {
                    Customer upDateCustomer = customer;
                    upDateCustomer.setBalance(SESSION._ORDERS.getTotalPrice() + customer.getBalance());
                    customerDBAdapter.updateEntry(upDateCustomer);
                }
                orderDBAdapter.close();
                custmerAssetDB.close();
                // End ORDER_DETAILS And CustomerAssistant Region

                // Payment Region
                long paymentID = paymentDBAdapter.insertEntry(CASH, saleTotalPrice, saleIDforCash,order.getOrderKey());

                Payment payment = new Payment(paymentID, CASH, saleTotalPrice, saleIDforCash);

                SESSION._ORDERS.setPayment(payment);

                paymentDBAdapter.close();

                printAndOpenCashBox("", "", "", REQUEST_CASH_ACTIVITY_CODE);

                return;
            }
        }
        //endregion
        if (requestCode == REQUEST_MULTI_CURRENCY_ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {
                if(orderDocumentFlag){
                    try {
                        updateOrderDocumentStatus();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                JSONArray jsonArray = null;
                try {
                    CurrencyOperationDBAdapter currencyOperationDBAdapter = new CurrencyOperationDBAdapter(this);
                    currencyOperationDBAdapter.open();
                    ArrayList<PaymentTable>paymentTableArrayList=new ArrayList<>();
                    CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(this);
                    PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(this);
                    saleDBAdapter = new OrderDBAdapter(SalesCartActivity.this);
                    orderDBAdapter = new OrderDetailsDBAdapter(SalesCartActivity.this);
                    custmerAssetDB = new CustomerAssetDB(SalesCartActivity.this);
                    cashPaymentDBAdapter.open();
                    saleDBAdapter.open();
                    orderDBAdapter.open();
                    custmerAssetDB.open();
                    paymentDBAdapter.open();
                    double TotalPaidAmount = 0;
                    double change = 0;

                    String MultiCurrencyResult = data.getStringExtra(MultiCurrenciesPaymentActivity.RESULT_INTENT_CODE_CASH_MULTI_CURRENCY_ACTIVITY_FULL_RESPONSE);
                    double totalPrice = data.getDoubleExtra(SalesCartActivity.COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE, 0.0f);
                    SESSION._ORDERS.setTotalPrice(totalPrice);
                    jsonArray = new JSONArray(MultiCurrencyResult);
                    Log.d("MultiCurrencyResult", MultiCurrencyResult);
                    ObjectMapper objectMapper = new ObjectMapper();
                    for (int i = 0; i < jsonArray.length() - 1; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        PaymentTable paymentTable= objectMapper.readValue(jsonObject.toString(), PaymentTable.class);
                        paymentTableArrayList.add(paymentTable);
                        TotalPaidAmount += jsonObject.getDouble("tendered") * getCurrencyRate(jsonObject.getJSONObject("currency").getString("type"));
                        change = Math.abs(jsonObject.getDouble("change")) * getCurrencyRate(jsonObject.getJSONObject("currency").getString("type"));
                    }


                    SESSION._ORDERS.setTotalPaidAmount(TotalPaidAmount);
                    saleIDforCash = saleDBAdapter.insertEntry(SESSION._ORDERS, customerId, customerName,false);
                    Order order = saleDBAdapter.getOrderById(saleIDforCash);
                    Log.d("oooo",order.toString());

                    SESSION._ORDERS.setOrderId(saleIDforCash);
                    SESSION._ORDERS.setNumberDiscount(order.getNumberDiscount());
                    for (int i = 0 ;i<paymentTableArrayList.size();i++){
                        currencyOperationDBAdapter.insertEntry(new Timestamp(System.currentTimeMillis()),saleIDforCash,CONSTANT.DEBIT,paymentTableArrayList.get(i).getTendered(),paymentTableArrayList.get(i).getCurrency().getType());
                    }
                    currencyReturnsCustomDialogActivity = new CurrencyReturnsCustomDialogActivity(this, change, new Order(SESSION._ORDERS));
                    for (int i = 0; i < jsonArray.length() - 1; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        cashPaymentDBAdapter.insertEntry(saleIDforCash, jsonObject.getDouble("tendered"), getCurrencyIdByType(jsonObject.getJSONObject("currency").getString("type")), new Timestamp(System.currentTimeMillis()),getCurrencyRate(jsonObject.getJSONObject("currency").getString("type")),jsonObject.getDouble("actualCurrencyRate"));

                    }
                    cashPaymentDBAdapter.close();
                    // Club with point and amount
                    if (clubType == 2) {
                        pointFromSale = ((int) (SESSION._ORDERS.getTotalPrice() * clubPoint) / clubAmount);
                        sum_pointDbAdapter.insertEntry(saleIDforCash, pointFromSale, customerId);
                    }

                    if (equalUsedPoint) {
                        saleTotalPrice = 0.0;
                        SESSION._ORDERS.setNumberDiscount(Order.calculateNumberDiscount(saleTotalPrice,Double.parseDouble(Util.makePrice(saleTotalPrice))));
                        SESSION._ORDERS.setTotalPrice(Double.parseDouble(Util.makePrice(saleTotalPrice)));
                        saleDBAdapter.updateEntry(SESSION._ORDERS);
                        usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, customerId);
                    }
                    if (lessUsedPoint) {
                        saleTotalPrice = 0.0;
                        SESSION._ORDERS.setNumberDiscount(Order.calculateNumberDiscount(saleTotalPrice,Double.parseDouble(Util.makePrice(saleTotalPrice))));
                        SESSION._ORDERS.setTotalPrice(Double.parseDouble(Util.makePrice(saleTotalPrice)));
                        saleDBAdapter.updateEntry(SESSION._ORDERS);
                        usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, customerId);
                    }
                    if (biggerUsedPoint) {
                        SESSION._ORDERS.setNumberDiscount(Order.calculateNumberDiscount(saleTotalPrice,Double.parseDouble(Util.makePrice(saleTotalPrice))));
                        SESSION._ORDERS.setTotalPrice(Double.parseDouble(Util.makePrice(saleTotalPrice)));
                        saleDBAdapter.updateEntry(SESSION._ORDERS);
                        usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, customerId);
                    }
                    if (forSaleMan) {
                        custmerAssetDB.insertEntry(saleIDforCash, custmerSaleAssetstId, SESSION._ORDERS.getTotalPrice(), 0, "ORDER", SESSION._ORDERS.getCreatedAt());
                    }
                    // insert order region

                    for (OrderDetails o : SESSION._ORDER_DETAILES) {
                        long orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleIDforCash, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(),order.getOrderKey());
                        orderId.add(orderid);
                        //   orderDBAdapter.insertEntry(o.getProductSku(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(),o.getCustomer_assistance_id());
                    }
                    // ORDER_DETAILS Sales man Region
                    for (int i = 0; i < orderIdList.size(); i++) {
                        OrderDetails orderDetails = orderIdList.get(i);
                        long customerAssestId = custmerAssetstIdList.get(i);
                        for (int j = 0; j < SESSION._ORDER_DETAILES.size(); j++) {
                            OrderDetails o = SESSION._ORDER_DETAILES.get(j);
                            long tempOrderId = orderId.get(i);
                            if (o == orderDetails) {
                                if (custmerAssetstIdList.get(i) != custmerSaleAssetstId) {
                                    o.setCustomer_assistance_id(custmerAssetstIdList.get(i));
                                    custmerAssetDB.insertEntry(tempOrderId, customerAssestId, o.getItemTotalPrice(), 0, "ORDER_DETAILS", SESSION._ORDERS.getCreatedAt());
                                }
                            }
                        }
                    }
                    //update customer balance
                    if (SESSION._ORDERS.getTotalPrice() < 0 && customer != null) {
                        Customer upDateCustomer = customer;
                        upDateCustomer.setBalance(SESSION._ORDERS.getTotalPrice() + customer.getBalance());
                        customerDBAdapter.updateEntry(upDateCustomer);
                    }
                    orderDBAdapter.close();
                    custmerAssetDB.close();
                    // End ORDER_DETAILS And CustomerAssistant Region

                    // Payment Region
                    long paymentID = paymentDBAdapter.insertEntry(CASH, saleTotalPrice, saleIDforCash,order.getOrderKey());

                    Payment payment = new Payment(paymentID, CASH, saleTotalPrice, saleIDforCash);

                    SESSION._ORDERS.setPayment(payment);

                    paymentDBAdapter.close();

                    printAndOpenCashBox("", "", "", REQUEST_CASH_ACTIVITY_CODE);

                    return;
                } catch (Exception e) {

                }
            }
        }

    }
    private void updateOrderDocumentStatus() throws JSONException, IOException {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                }

                @Override
                protected Void doInBackground(Void... params) {

                    try {
                        MessageTransmit transmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
                        JSONObject jsonObject = SETTINGS.orderDocument;
                        JSONObject orderDocJsonObj = jsonObject.getJSONObject("documentsData");
                        orderDocJsonObj.remove("orderDocumentStatus");

                        orderDocJsonObj.put("orderDocumentStatus", OrderDocumentStatus.CLOSED);
                        String upDataOrderDocumentStatus=transmit.authPutInvoice(ApiURL.Documents,jsonObject.toString(), SESSION.token,jsonObject.getString("docNum"));
                        Log.d("invoiceRes",upDataOrderDocumentStatus.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return null;
                }
            }.execute();
    }

    private long getCurrencyIdByType(String type) {
        CurrencyTypeDBAdapter currency = new CurrencyTypeDBAdapter(this);
        currency.open();
        return currency.getCurrencyIdByType(type);
    }

    /**
     * Hides the soft keyboard
     */

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */


    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    /**
     * @Override public void onBackPressed() {
     * if(enableBackButton){
     * //stop all moves
     * }
     * //do not move from here :)
     * }
     **/


    private void callPopup() {
        final Dialog customerDialog = new Dialog(SalesCartActivity.this);
        customerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customerDialog.show();
        customerDialog.setContentView(R.layout.pop_up);
        customer_id = (EditText) customerDialog.findViewById(R.id.customer_name);
        final GridView gvCustomer = (GridView) customerDialog.findViewById(R.id.popUp_gvCustomer);
        gvCustomer.setNumColumns(3);

        btn_cancel = (Button) customerDialog.findViewById(R.id.btn_cancel);

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
                        Intent intent = new Intent(SalesCartActivity.this, AddNewCustomer.class);
                        startActivity(intent);

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

        customerList = customerDBAdapter.getTopCustomer(0, 50);
        AllCustmerList = customerList;

        custmerCatalogGridViewAdapter = new CustomerCatalogGridViewAdapter(getApplicationContext(), customerList);

        gvCustomer.setAdapter(custmerCatalogGridViewAdapter);

        gvCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setCustomer(customerList.get(position));
                SESSION._ORDERS.setCustomer_name(customerList.get(position).getFirstName());
                SESSION._ORDERS.setCustomer(customerList.get(position));
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

    public void callPopupOrderSalesMan(final OrderDetails order) {
        EmployeeDBAdapter userDB = new EmployeeDBAdapter(this);
        userDB.open();
        final CustomerAssetDB customerAssistantDB = new CustomerAssetDB(this);
        customerAssistantDB.open();
        final Dialog salesManDialog = new Dialog(SalesCartActivity.this);
        salesManDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        salesManDialog.show();
        salesManDialog.setContentView(R.layout.custmer_assest_popup);
        final EditText customerAssistant = (EditText) salesManDialog.findViewById(R.id.customerAssest_name);
        ListView lvCustomerAssistant = (ListView) salesManDialog.findViewById(R.id.customerAssistant_list_view);
        Button btn_cancel = (Button) salesManDialog.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salesManDialog.dismiss();
            }
        });


        ((Button) salesManDialog.findViewById(R.id.btn_add))
                .setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
                        Intent intent = new Intent(SalesCartActivity.this, AddEmployeeActivity.class);
                        startActivity(intent);

                        salesManDialog.dismiss();
                    }
                });


        customerAssistant.setText("");
        customerAssistant.setHint("Search..");

        customerAssistant.setFocusable(true);
        customerAssistant.requestFocus();
        customerAssistant.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                customerAssistant.setFocusable(true);
            }
        });

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        lvCustomerAssistant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                       @Override
                                                       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                           if (!orderIdList.contains(order)) {
                                                               custmerAssetstIdList.add(custmerAssestList.get(position).getEmployeeId());
                                                               orderIdList.add(order);
                                                           }
                                                           orderSalesMan.setText(custmerAssestList.get(position).getFullName());
                                                           deleteOrderSalesMan.setVisibility(View.VISIBLE);
                                                           salesManDialog.dismiss();
                                                       }
                                                   }
        );

        lvCustomerAssistant.setOnScrollListener(new AbsListView.OnScrollListener() {
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

        custmerAssestList = userDB.getAllSalesMAn();
        AllCustmerAssestList = custmerAssestList;


        CustomerAssistantCatalogGridViewAdapter adapter = new CustomerAssistantCatalogGridViewAdapter(getApplicationContext(), custmerAssestList);
        lvCustomerAssistant.setAdapter(adapter);


    }

    public void callPopupForSalesMan() {
        EmployeeDBAdapter userDB = new EmployeeDBAdapter(this);
        userDB.open();
        final CustomerAssetDB customerAssistantDb = new CustomerAssetDB(this);
        customerAssistantDb.open();
        final Dialog salesManDialog = new Dialog(SalesCartActivity.this);
        salesManDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        salesManDialog.show();
        salesManDialog.setContentView(R.layout.custmer_assest_popup);
        final EditText customerAssistant = (EditText) salesManDialog.findViewById(R.id.customerAssest_name);

        ListView lvCustomerAssistant = (ListView) salesManDialog.findViewById(R.id.customerAssistant_list_view);

        Button btn_cancel = (Button) salesManDialog.findViewById(R.id.btn_cancel);
        Button btnDelete = (Button) salesManDialog.findViewById(R.id.btn_delete);
        btnDelete.setVisibility(View.VISIBLE);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salesManDialog.dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forSaleMan = false;
                salesSaleMan.setText(getString(R.string.sales_man));
            }
        });

        ((Button) salesManDialog.findViewById(R.id.btn_add))
                .setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
                        Intent intent = new Intent(SalesCartActivity.this, AddEmployeeActivity.class);
                        startActivity(intent);

                        salesManDialog.dismiss();


                    }
                });


        customerAssistant.setText("");
        customerAssistant.setHint("Search..");

        customerAssistant.setFocusable(true);
        customerAssistant.requestFocus();
        customerAssistant.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                customerAssistant.setFocusable(true);
            }
        });

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        lvCustomerAssistant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                forSaleMan = true;
                custmerSaleAssetstId = custmerAssestList.get(position).getEmployeeId();
                salesSaleMan.setText(custmerAssestList.get(position).getFullName());
                salesManDialog.dismiss();
            }
        });
        lvCustomerAssistant.setOnScrollListener(new AbsListView.OnScrollListener() {
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

        custmerAssestList = userDB.getAllSalesMAn();
        AllCustmerAssestList = custmerAssestList;


        CustomerAssistantCatalogGridViewAdapter adapter = new CustomerAssistantCatalogGridViewAdapter(getApplicationContext(), custmerAssestList);
        lvCustomerAssistant.setAdapter(adapter);


    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getSource() == 257) {
            //barcode region
            if (event.getAction() == KeyEvent.ACTION_UP) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    try {
                        enterKeyPressed(barcodeScanned);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    barcodeScanned = "";

                    // barcodeScanned = "";
                    return true;
                } else {
                    Log.e("char", event.getDisplayLabel() + "");
                    Log.e("char key", (char) event.getUnicodeChar() + "");
                    if (validChar(event.getDisplayLabel())) {
                        barcodeScanned = barcodeScanned + event.getDisplayLabel();
                        Log.d("barcode", barcodeScanned);
                        return false;

                    }
                }
                return super.dispatchKeyEvent(event);
            }
        } else {
            //editText region
            if (validChar(event.getDisplayLabel())) {
                fromEditText = fromEditText + event.getDisplayLabel();
                Log.d("fromEditText", fromEditText);

            }
            return super.dispatchKeyEvent(event);
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return super.dispatchKeyEvent(event);
        }
        return true;
    }

    public static int CharToASCII(final char character) {
        return (int) character;
    }

    public static char ASCIIToChar(final int ascii) {
        return (char) ascii;
    }

    public boolean validChar(char c) {
        //- (Dash), $ (Dollar), % (Percentage), (Space), . (Point), / (Slash), + (Plus)
        String code39 = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789-$% ./+";
        return code39.contains(c + "");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setCustomer(Customer customer) {
        if(customer==null) return;
        this.customer = customer;
        this.customerName = customer.getFullName();
        this.customerClubId = customer.getClub();
        this.customerId = customer.getCustomerId();

        customerName_EditText.setText(customerName);

        Club club = clubAdapter.getGroupInfo(customerClubId);
        clubType = club.getType();
        clubName = club.getName();
        if (clubType == 1) {
            clubDiscount = club.getPercent();
        } else if (clubType == 2) {
            clubAmount = club.getAmount();
            clubPoint = club.getPoint();
        } else if (clubType == 0) {
        }

        calculateTotalPrice();
        if(customer.getCustomerType().equals(CustomerType.CREDIT)){

          StartGetCustomerGeneralLedgerConnection startGetCustomerGeneralLedgerConnection = new StartGetCustomerGeneralLedgerConnection();
            startGetCustomerGeneralLedgerConnection.execute(String.valueOf(customer.getCustomerId()));

        }
        mainActivity_btnRemoveCustomer.setVisibility(View.VISIBLE);

    }

    public double getCurrencyRate(String currencyType) {
        for (int i = 0; i < currenciesList.size(); i++) {
            if (currenciesList.get(i).getCountry().equals(currencyType)) {
                return currenciesList.get(i).getRate();
            }
        }
        return 1;
    }

    public OrderDetails calculateOfferForOrderDetails(OrderDetails orderDetails) throws JSONException {

        List<Offer> offerList = OfferController.getOffersForResource(orderDetails.getProductId(),orderDetails.getProduct().getSku(),orderDetails.getProduct().getCategoryId(), getApplicationContext());
        orderDetails.offerList = offerList;
        if (offerList != null) {
            OrderDetails o=orderDetails;
            for (int i =0; i<offerList.size(); i++) {
                if (OfferController.check(offerList.get(i), o)) {
                    o= OfferController.execute(offerList.get(i), o,this);
                }
            }
            return o;
        }
        return orderDetails;
    }

    private void removeCustomer() {
        Customer customer = null;
        SESSION._ORDERS.setCustomer(customer);
        customerName_EditText.setText("");
        calculateTotalPrice();
        linearLayoutCustomerBalance.setVisibility(View.GONE);
        mainActivity_btnRemoveCustomer.setVisibility(View.GONE);
        customerBalance.setText("");
    }
    private void print(Bitmap bitmap) {
        PrintTools printTools = new PrintTools(this);
        printTools.PrintReport(bitmap);
    }
}
class StartGetCustomerGeneralLedgerConnection extends AsyncTask<String,Void,String> {
    private MessageTransmit messageTransmit;
    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    BoInvoice invoice;
    StartGetCustomerGeneralLedgerConnection() {
        messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
    }

    final ProgressDialog progressDialog = new ProgressDialog(SalesCartActivity.context);
    final ProgressDialog progressDialog2 =new ProgressDialog(SalesCartActivity.context);

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
                Order.CustomerLedger=response.getDouble("creditAmount");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onPostExecute(String s) {
            SalesCartActivity.linearLayoutCustomerBalance.setVisibility(View.VISIBLE);
            SalesCartActivity.customerBalance.setText(Util.makePrice(Order.CustomerLedger));

        progressDialog.cancel();
        super.onPostExecute(s);

        //endregion
    }
}

