package com.pos.leaders.leaderspossystem;

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
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

import com.pos.leaders.leaderspossystem.CreditCard.CreditCardActivity;
import com.pos.leaders.leaderspossystem.CreditCard.MainCreditCardActivity;
import com.pos.leaders.leaderspossystem.CustomerAndClub.AddNewCustomer;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ChecksDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClubAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CreditCardPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerAssetDB;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.DepartmentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductOfferDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule11DBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule3DbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule5DBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule7DbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule8DBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Sum_PointDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UsedPointDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UserDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ValueOfPointDB;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.Club;
import com.pos.leaders.leaderspossystem.Models.CreditCardPayment;
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.Models.Department;
import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule11;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule3;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule5;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule7;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule8;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.User;
import com.pos.leaders.leaderspossystem.Pinpad.PinpadActivity;
import com.pos.leaders.leaderspossystem.Printer.HPRT_TP805;
import com.pos.leaders.leaderspossystem.Printer.InvoiceImg;
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
import com.pos.leaders.leaderspossystem.syncposservice.Service.SyncMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
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

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CASH_ACTIVITY_WITH_CURRENCY_CODE = 590;
    private static final int REQUEST_CASH_ACTIVITY_CODE = 600;
    private static final int REQUEST_CHECKS_ACTIVITY_CODE = 753;
    private static final int REQUEST_CREDIT_CARD_ACTIVITY_CODE = 801;
    private static final int REQUEST_PIN_PAD_ACTIVITY_CODE = 907;
    public static final String COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE = "com_pos_leaders_cart_total_price";
    String transID = "";

    TextView salesMan;
    private DrawerLayout drawerLayout;
    //private ActionBarDrawerToggle actionBarDrawerToggle;//
    // private NavigationView navigationView;

    //ImageButton    btnLastSales;
    Button btnPercentProduct, btnPauseSale, btnResumeSale;
    ImageButton search_person;
    Button  btnCash, btnCreditCard, btnOtherWays;
    TextView tvTotalPrice, tvTotalSaved, salesSaleMan;
    EditText etSearch;
    ImageButton btnDone;
    ImageButton btnGrid, btnList;
    ScrollView scDepartment;
    LinearLayout llDepartments;
    FrameLayout fragmentTouchPad;
    GridView gvProducts;
    ListView lvProducts;
    DepartmentDBAdapter departmentDBAdapter;
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
    private List<User> custmerAssestList;
    private List<User> AllCustmerAssestList;
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
    ImageView imv ,btnCancel;

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
    TextView orderSalesMan;
    ImageView deleteOrderSalesMan;
    String fromEditText="";

    double valueOfDiscount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_temp);


        TitleBar.setTitleBar(this);

        if (!Util.isSyncServiceRunning(this)) {
            Intent intent = new Intent(MainActivity.this, SyncMessage.class);
            intent.putExtra(SyncMessage.API_DOMAIN_SYNC_MESSAGE, SETTINGS.BO_SERVER_URL);
            startService(intent);
        }

        customerName_EditText = (EditText) findViewById(R.id.customer_textView);

        Button btAddProductShortLink = (Button) findViewById(R.id.mainActivity_btAddProductShortLink);
        btAddProductShortLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProductsActivity.class);
                startActivity(i);
            }
        });

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
        custmerAssetstIdList = new ArrayList<Long>();
        orderIdList=new ArrayList<OrderDetails>();
        orderId=new ArrayList<Long>();
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
        departmentDBAdapter = new DepartmentDBAdapter(this);
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
                Context c = MainActivity.this;
                if (SESSION._ORDERS.size() > 0)
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
                addToCart(productList.get(position));
            }
        });
        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addToCart(productList.get(position));
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

        List<Department> departments = departmentDBAdapter.getAllDepartments();
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
                        productList = productDBAdapter.getAllProductsByDepartment(((Department) v.getTag()).getDepartmentId(), productLoadItemOffset, productCountLoad);
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
                        productList = productDBAdapter.getAllProductsByDepartment(((Department) v.getTag()).getDepartmentId(), productLoadItemOffset, productCountLoad);
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
                    productList = productDBAdapter.getAllProductsByDepartment(((Department) v.getTag()).getDepartmentId(), productLoadItemOffset, productCountLoad);
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
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
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
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
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
                            productList=productDBAdapter.getAllProductsByHint(params[0],productLoadItemOffset,productCountLoad);
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
                    enterKeyPressed(barcodeScanned);
                    barcodeScanned = "";
                    etSearch.setText("");
                    //etSearch.requestFocus();
                } else
                    Toast.makeText(MainActivity.this, "Input is empty.", Toast.LENGTH_SHORT).show();
                //OpenCashBox();

            }
        });
      /**  etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               etSearch.setFocusable(true);
        }});**/

        //endregion
/**
 actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
 drawerLayout.addDrawerListener(actionBarDrawerToggle);
 actionBarDrawerToggle.syncState();
 **/
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //region Order List View


        if (SESSION._SALE != null) {
            sale = new Order(SESSION._USER.getUserId(), new Timestamp(System.currentTimeMillis()), 0, false, 0, 0);
        } else {
            SESSION._SALE = new Order(SESSION._USER.getUserId(), new Timestamp(System.currentTimeMillis()), 0, false, 0, 0);
        }

        if (SESSION._ORDERS != null) {
            lvOrder.setFocusable(false);
            offerDBAdapter = new OfferDBAdapter(this);
            offerDBAdapter.open();
            List<Offer> offerList = offerDBAdapter.getAllOffersByStatus(1);

            //  Offer offer=offerDBAdapter.getAllValidOffers();


            if (offerList != null) {
                calculateTotalPriceWithOffers(offerList);
            } else {
                calculateTotalPrice();
            }
        } else {
            SESSION._ORDERS = new ArrayList<OrderDetails>();
        }
        saleDetailsListViewAdapter = new SaleDetailsListViewAdapter(getApplicationContext(), R.layout.list_adapter_row_main_screen_sales_details, SESSION._ORDERS);
        lvOrder.setAdapter(saleDetailsListViewAdapter);


        lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                removeOrderItemSelection();
                view.findViewById(R.id.rowSaleDetails_LLMethods).setVisibility(View.VISIBLE);
                view.findViewById(R.id.saleManLayout).setVisibility(View.VISIBLE);
                double discount = SESSION._ORDERS.get(position).getDiscount();
                if (discount > 0) {
                    view.findViewById(R.id.discountLayout).setVisibility(View.VISIBLE);

                } else {
                    view.findViewById(R.id.discountLayout).setVisibility(View.GONE);

                }
                selectedIteminCartList = view;
                selectedOrderOnCart = SESSION._ORDERS.get(position);
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
                deleteOrderSalesMan=(ImageView)view.findViewById(R.id.deleteOrderSalesMan);
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
                        for (int i=0 ; i<orderIdList.size();i++) {
                            if(orderIdList.get(i)==selectedOrderOnCart){
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
                //discount Button.
                Button btnEdit = (Button) view.findViewById(R.id.rowSaleDetails_MethodsEdit);
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedIteminCartList != null) {
                            if (SESSION._ORDERS.contains(selectedOrderOnCart)) {
                                final Dialog cashDialog = new Dialog(MainActivity.this);
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
                                        int indexOfItem = SESSION._ORDERS.indexOf(selectedOrderOnCart);
                                        SESSION._ORDERS.get(indexOfItem).setCount(pid);
                                        refreshCart();

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
                                Toast.makeText(MainActivity.this, getBaseContext().getString(R.string.please_select_item), Toast.LENGTH_SHORT);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, getBaseContext().getString(R.string.please_add_item), Toast.LENGTH_SHORT);
                        }
                    }
                });
                Button btnDiscount = (Button) view.findViewById(R.id.rowSaleDetails_Dicount);
                btnDiscount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedIteminCartList != null) {
                            if (SESSION._ORDERS.contains(selectedOrderOnCart)) {
                                final TextView discountPercentage = (TextView) view.findViewById(R.id.discountPercentage);
                                final TextView tvDiscountPercentage = (TextView) view.findViewById(R.id.tvDiscountPercentageAmount);
                                final Dialog cashDialog = new Dialog(MainActivity.this);
                                cashDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                // cashDialog.setTitle(R.string.please_select_discount_offer);
                                cashDialog.show();
                                cashDialog.setContentView(R.layout.discount_dialog);
                                final Button cashBTOk = (Button) cashDialog.findViewById(R.id.cashPaymentDialog_BTOk);
                                final EditText cashETCash = (EditText) cashDialog.findViewById(R.id.cashPaymentDialog_TECash);
                                final Switch sw = (Switch) cashDialog.findViewById(R.id.cashPaymentDialog_SwitchProportion);
                                final TextView totalPrice =(TextView)cashDialog.findViewById(R.id.TvTotalPrice);
                                final TextView priceAfterDiscount =(TextView)cashDialog.findViewById(R.id.TvPriceAfterDiscount);
                                final TextView totalDiscount =(TextView)cashDialog.findViewById(R.id.totalDiscount);
                                final ImageView closeDialogImage =(ImageView)cashDialog.findViewById(R.id.closeDialog);
                                closeDialogImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        cashDialog.dismiss();
                                    }
                                });
                                totalPrice.setText(Util.makePrice(selectedOrderOnCart.getUnitPrice()*selectedOrderOnCart.getQuantity())+getString(R.string.ins));
                                List<OrderDetails>list=new ArrayList<OrderDetails>();
                                list.add(selectedOrderOnCart);
                                final TextView discountType =(TextView)cashDialog.findViewById(R.id. cashPaymentDialog_TVStatus);
                                discountType.append(":"+selectedOrderOnCart.getProduct().getName());
                                totalDiscount.setText(Util.makePrice(selectedOrderOnCart.getDiscount()));
                                priceAfterDiscount.setText(Util.makePrice(selectedOrderOnCart.getPaidAmount()*selectedOrderOnCart.getQuantity())+getString(R.string.ins));
                                sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked) {
                                            sw.setText(getBaseContext().getString(R.string.amount));
                                            totalDiscount.setText(Util.makePrice(selectedOrderOnCart.getDiscount()));
                                            priceAfterDiscount.setText(Util.makePrice(selectedOrderOnCart.getPaidAmount()*selectedOrderOnCart.getQuantity())+getString(R.string.ins));
                                            cashETCash.setText("0");
                                        } else {
                                            sw.setText(getBaseContext().getString(R.string.proportion));
                                            totalDiscount.setText(Util.makePrice(selectedOrderOnCart.getDiscount()));
                                            priceAfterDiscount.setText(Util.makePrice(selectedOrderOnCart.getPaidAmount()*selectedOrderOnCart.getQuantity())+getString(R.string.ins));
                                            cashETCash.setText("0");
                                        }
                                    }
                                });
                                cashETCash.setHint(R.string.proportion);
                                final List<OrderDetails>orderList=list;
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
                                        int indexOfItem = SESSION._ORDERS.indexOf(selectedOrderOnCart);
                                        double X = SESSION._USER.getPresent();
                                        if (sw.isChecked()) {
                                            if (!(str.equals(""))) {
                                                double d = Double.parseDouble(str);
                                                int count = SESSION._ORDERS.get(indexOfItem).getQuantity();
                                                double discount = (1 - (d / (SESSION._ORDERS.get(indexOfItem).getUnitPrice() * count)));

                                                if (discount <= (X / 100)) {
                                                    double originalTotalPrice = 0;
                                                    for (OrderDetails o : orderList) {
                                                        originalTotalPrice += (o.getUnitPrice() * o.getQuantity());
                                                    }
                                                    double val = (1 - (d / originalTotalPrice)) * 100;
                                                    for (OrderDetails o : orderList) {
                                                        o.setDiscount(val);
                                                    }
                                                    double saleTotalPrice = 0;
                                                    double SaleOriginalityPrice = 0;
                                                    for (OrderDetails o : orderList) {
                                                        saleTotalPrice += o.getItemTotalPrice();

                                                        SaleOriginalityPrice += (o.getUnitPrice() * o.getQuantity());
                                                    }
                                                    totalDiscount.setText(Util.makePrice(SaleOriginalityPrice - saleTotalPrice)+getString(R.string.ins));
                                                    priceAfterDiscount.setText(Util.makePrice(saleTotalPrice)+getString(R.string.ins));
                                                } else {
                                                    totalDiscount.setText(Util.makePrice(selectedOrderOnCart.getDiscount()));
                                                    priceAfterDiscount.setText(Util.makePrice(selectedOrderOnCart.getPaidAmount()*selectedOrderOnCart.getQuantity())+getString(R.string.ins));
                                                    Toast.makeText(MainActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                                    cashETCash.setBackgroundResource(R.drawable.backtext);

                                                }

                                            }else {
                                                totalDiscount.setText(Util.makePrice(selectedOrderOnCart.getDiscount()));
                                                priceAfterDiscount.setText(Util.makePrice(selectedOrderOnCart.getPaidAmount()*selectedOrderOnCart.getQuantity())+getString(R.string.ins));
                                            }
                                        } else {
                                            if (!(str.equals(""))) {
                                                float val = Float.parseFloat(str);
                                                if (val <= X) {
                                                    int count = SESSION._ORDERS.get(indexOfItem).getQuantity();
                                                    for (OrderDetails o : orderList) {
                                                        o.setDiscount(val);
                                                    }

                                                    double saleTotalPrice = 0;
                                                    double SaleOriginalityPrice = 0;
                                                    for (OrderDetails o : orderList) {
                                                        saleTotalPrice += o.getItemTotalPrice();

                                                        SaleOriginalityPrice += (o.getUnitPrice() * o.getQuantity());
                                                    }
                                                    totalDiscount.setText(Util.makePrice(SaleOriginalityPrice - saleTotalPrice)+getString(R.string.ins));
                                                    priceAfterDiscount.setText(Util.makePrice(saleTotalPrice)+getString(R.string.ins));
                                                } else {
                                                    totalDiscount.setText(Util.makePrice(selectedOrderOnCart.getDiscount()));
                                                    priceAfterDiscount.setText(Util.makePrice(selectedOrderOnCart.getPaidAmount()*selectedOrderOnCart.getQuantity())+getString(R.string.ins));
                                                    Toast.makeText(MainActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                                    cashETCash.setBackgroundResource(R.drawable.backtext);

                                                }


                                            }else {
                                                totalDiscount.setText(Util.makePrice(selectedOrderOnCart.getDiscount()));
                                                priceAfterDiscount.setText(Util.makePrice(selectedOrderOnCart.getPaidAmount()*selectedOrderOnCart.getQuantity())+getString(R.string.ins));
                                            }
                                        }
                                    }
                                });
                                cashBTOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String str = cashETCash.getText().toString();
                                        int indexOfItem = SESSION._ORDERS.indexOf(selectedOrderOnCart);
                                        double X = SESSION._USER.getPresent();
                                        if (sw.isChecked()) {
                                            if (!(str.equals(""))) {
                                                double d = Double.parseDouble(str);
                                                int count = SESSION._ORDERS.get(indexOfItem).getQuantity();
                                                double discount = (1 - (d / (SESSION._ORDERS.get(indexOfItem).getUnitPrice() * count)));

                                                if (discount <= (X / 100)) {
                                                    SESSION._ORDERS.get(indexOfItem).setDiscount(discount * 100);
                                                    refreshCart();
                                                    cashDialog.cancel();
                                                } else {
                                                    Toast.makeText(MainActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                                }
                                            }} else {
                                            if (!(str.equals(""))) {
                                                float val = Float.parseFloat(str);
                                                if (val <= X) {
                                                    int count = SESSION._ORDERS.get(indexOfItem).getQuantity();
                                                    SESSION._ORDERS.get(indexOfItem).setDiscount(val);
                                                    //SESSION._ORDERS.get(indexOfItem).setPaidAmount(((SESSION._ORDERS.get(indexOfItem).getUnitPrice()*count) * ((1 - (val / 100))) / count));

                                                    refreshCart();
                                                    cashDialog.cancel();
                                                } else {
                                                    Toast.makeText(MainActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(MainActivity.this, getBaseContext().getString(R.string.please_select_item), Toast.LENGTH_SHORT);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, getBaseContext().getString(R.string.please_add_item), Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
        });


        lvOrder.setAdapter(saleDetailsListViewAdapter);


        //endregion

        offerDBAdapter = new OfferDBAdapter(this);
        offerDBAdapter.open();
        // Offer offer=offerDBAdapter.getAllValidOffers();
        List<Offer> offerList = offerDBAdapter.getAllOffersByStatus(1);

        if (offerList != null) {
            calculateTotalPriceWithOffers(offerList);
        } else {
            calculateTotalPrice();
        }
        //region Payment

        //region Cash

        btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SESSION._ORDERS.size() > 0) {
                    if (SETTINGS.enableCurrencies) {
                        Intent intent = new Intent(MainActivity.this, CashActivity.class);
                        intent.putExtra(COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE, saleTotalPrice);
                        startActivityForResult(intent, REQUEST_CASH_ACTIVITY_WITH_CURRENCY_CODE);
                    } else {
                        Intent intent = new Intent(MainActivity.this, OldCashActivity.class);
                        intent.putExtra(COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE, saleTotalPrice);
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
                if (SESSION._ORDERS.size() > 0 && SETTINGS.creditCardEnable) {
                    if (SETTINGS.pinpadEnable) {//pinpad is active
                        Log.i("CreditCard", "PinPad is active");
                        Intent intent = new Intent(MainActivity.this, PinpadActivity.class);
                        intent.putExtra(PinpadActivity.LEADERS_POS_PIN_PAD_TOTAL_PRICE, saleTotalPrice);
                        startActivityForResult(intent, REQUEST_PIN_PAD_ACTIVITY_CODE);
                    } else {//old school
                        //final String __customerName = customerName_EditText.getText().toString();
                        Intent intent = new Intent(MainActivity.this, MainCreditCardActivity.class);
                        intent.putExtra(MainCreditCardActivity.LEADERS_POS_CREDIT_CARD_TOTAL_PRICE, saleTotalPrice);
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
                if (SESSION._ORDERS.size() > 0) {
                    Intent intent = new Intent(MainActivity.this, ChecksActivity.class);
                    customerName = customerName_EditText.getText().toString();

                    intent.putExtra("_Price", saleTotalPrice);
                    intent.putExtra("_custmer", customerName);

                    startActivityForResult(intent, REQUEST_CHECKS_ACTIVITY_CODE);
                }
            }
        });

        //endregion Other Way


        //endregion

        //region sale option buttons

        //// TODO: 07/02/2017 FLAG
        //region Pause Sale
        btnPauseSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (SESSION._ORDERS.size() != 0) {
                    Order s = new Order(SESSION._SALE);
                    s.setOrders(SESSION._ORDERS);
                    if (SESSION._SALES == null)
                        SESSION._SALES = new ArrayList<Pair<Integer, Order>>();
                    else if (SESSION._SALES.size() == 0)
                        SESSION.TEMP_NUMBER = 0;

                    SESSION._SALES.add(new Pair<>(++SESSION.TEMP_NUMBER, s));


                    clearCart();
                    Toast.makeText(MainActivity.this, getString(R.string.deal_number) + " " + SESSION.TEMP_NUMBER, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //endregion

        //region Resume Sale

        btnResumeSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SESSION._SALES != null && SESSION._SALES.size() > 0) {
                    showAlertDialogResumePauseSale();
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.there_is_no_sale_on_pause), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //endregion





        //region Percent Product

        btnPercentProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SESSION._SALE != null && SESSION._ORDERS != null) {
                    final Dialog discountDialog = new Dialog(MainActivity.this);
                    discountDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //discountDialog.setTitle(R.string.please_select_discount_offer);
                    discountDialog.setContentView(R.layout.discount_dialog);
                    discountDialog.show();
                    final Button btOK = (Button) discountDialog.findViewById(R.id.cashPaymentDialog_BTOk);
                    final EditText et = (EditText) discountDialog.findViewById(R.id.cashPaymentDialog_TECash);
                    final Switch sw = (Switch) discountDialog.findViewById(R.id.cashPaymentDialog_SwitchProportion);
                    final TextView totalPrice =(TextView)discountDialog.findViewById(R.id.TvTotalPrice);
                    final TextView priceAfterDiscount =(TextView)discountDialog.findViewById(R.id.TvPriceAfterDiscount);
                    final TextView totalDiscount =(TextView)discountDialog.findViewById(R.id.totalDiscount);
                    final TextView discountType =(TextView)discountDialog.findViewById(R.id. cashPaymentDialog_TVStatus);
                    discountType.setText(getString(R.string.discount));
                    final ImageView closeDialogImage =(ImageView)discountDialog.findViewById(R.id.closeDialog);
                    closeDialogImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            discountDialog.dismiss();
                        }
                    });
                    double originalTotalPrice = 0;
                    for (OrderDetails o : SESSION._ORDERS) {
                        originalTotalPrice += (o.getUnitPrice() * o.getQuantity());
                    }
                    totalPrice.setText(Util.makePrice(originalTotalPrice)+getString(R.string.ins));
                    totalDiscount.setText(Util.makePrice(valueOfDiscount)+getString(R.string.ins));
                    priceAfterDiscount.setText(tvTotalPrice.getText().toString());
                    sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (sw.isChecked()) {
                                totalDiscount.setText(Util.makePrice(valueOfDiscount)+getString(R.string.ins));
                                priceAfterDiscount.setText(tvTotalPrice.getText().toString());
                                sw.setText(R.string.amount);
                                et.setText("0");
                            } else {
                                totalDiscount.setText(Util.makePrice(valueOfDiscount)+getString(R.string.ins));
                                priceAfterDiscount.setText(tvTotalPrice.getText().toString());
                                sw.setText(R.string.proportion);
                                et.setText("0");
                            }
                        }
                    });
                    et.setHint(R.string.proportion);
                    final List<OrderDetails>orderList=new ArrayList<OrderDetails>();
                    for (int i=0;i<SESSION._ORDERS.size();i++){
                        orderList.add(new OrderDetails(SESSION._ORDERS.get(i)));
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
                            double X = SESSION._USER.getPresent();
                            if (sw.isChecked()) {
                                if (!(str.equals(""))) {
                                    double d = Double.parseDouble(str);
                                    double originalTotalPrice = 0;
                                    for (OrderDetails o : orderList) {
                                        originalTotalPrice += (o.getUnitPrice() * o.getQuantity());
                                    }
                                    if ((1 - (d / originalTotalPrice) <= (X / 100))) {
                                        double val = (1 - (d / originalTotalPrice)) * 100;
                                        for (OrderDetails o : orderList) {
                                            o.setDiscount(val);
                                        }
                                        double saleTotalPrice = 0;
                                        double SaleOriginalityPrice = 0;
                                        for (OrderDetails o : orderList) {
                                            saleTotalPrice += o.getItemTotalPrice();

                                            SaleOriginalityPrice += (o.getUnitPrice() * o.getQuantity());
                                        }
                                        totalDiscount.setText(Util.makePrice(SaleOriginalityPrice - saleTotalPrice)+getString(R.string.ins));
                                        priceAfterDiscount.setText(Util.makePrice(saleTotalPrice)+getString(R.string.ins));
                                    } else {
                                        totalDiscount.setText(Util.makePrice(valueOfDiscount)+getString(R.string.ins));
                                        priceAfterDiscount.setText(tvTotalPrice.getText().toString());
                                        Toast.makeText(MainActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                        et.setBackgroundResource(R.drawable.backtext);
                                    }

                                }else {
                                    totalDiscount.setText(Util.makePrice(valueOfDiscount)+getString(R.string.ins));
                                    priceAfterDiscount.setText(tvTotalPrice.getText().toString());
                                }
                            } else {
                                if (!(str.equals(""))) {
                                    float val = Float.parseFloat(str);
                                    if (val <= X) {
                                        for (OrderDetails o : orderList) {
                                            o.setDiscount(val);
                                        }
                                        double saleTotalPrice = 0;
                                        double SaleOriginalityPrice = 0;
                                        for (OrderDetails o : orderList) {
                                            saleTotalPrice += o.getItemTotalPrice();

                                            SaleOriginalityPrice += (o.getUnitPrice() * o.getQuantity());
                                        }
                                        totalDiscount.setText(Util.makePrice(SaleOriginalityPrice - saleTotalPrice)+getString(R.string.ins));
                                        priceAfterDiscount.setText(Util.makePrice(saleTotalPrice)+getString(R.string.ins));

                                    } else {
                                        totalDiscount.setText(Util.makePrice(valueOfDiscount)+getString(R.string.ins));
                                        priceAfterDiscount.setText(tvTotalPrice.getText().toString());
                                        Toast.makeText(MainActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                        et.setBackgroundResource(R.drawable.backtext);
                                    }
                                }else {
                                    totalDiscount.setText(Util.makePrice(valueOfDiscount)+getString(R.string.ins));
                                    priceAfterDiscount.setText(tvTotalPrice.getText().toString());
                                }
                            }
                        }
                    });

                    btOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String str = et.getText().toString();
                            double X = SESSION._USER.getPresent();
                            if (sw.isChecked()) {
                                if (!(str.equals(""))) {

                                double d = Double.parseDouble(str);
                                double originalTotalPrice = 0;
                                for (OrderDetails o : SESSION._ORDERS) {
                                    originalTotalPrice += (o.getUnitPrice() * o.getQuantity());
                                }
                                if ((1 - (d / originalTotalPrice) <= (X / 100))) {
                                    double val = (1 - (d / originalTotalPrice)) * 100;
                                    valueOfDiscount=val;
                                    for (OrderDetails o : SESSION._ORDERS) {
                                        o.setDiscount(val);
                                        }
                                        refreshCart();
                                        discountDialog.cancel();
                                    } else {
                                        Toast.makeText(MainActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            } else {
                                if (!(str.equals(""))) {
                                    float val = Float.parseFloat(str);
                                    valueOfDiscount=val;
                                    if (val <= X) {
                                        valueOfDiscount=val;
                                        for (OrderDetails o : SESSION._ORDERS) {
                                            o.setDiscount(val);
                                        }
                                        refreshCart();
                                        discountDialog.cancel();
                                    } else {
                                        Toast.makeText(MainActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });

                } else {
                    Toast.makeText(MainActivity.this, getBaseContext().getString(R.string.please_add_item), Toast.LENGTH_SHORT);
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


        //check starting day report A
    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();
        /** if (extras != null) {
         str = extras.getString("permissions_name");
         }*/

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
                Toast.makeText(MainActivity.this, getString(R.string.resume_deal_number) + " " + (SESSION._SALES.get(position).first), Toast.LENGTH_SHORT).show();
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

    public void quickPriceButtonClick(View view) {
        String str = ((Button) view).getText().toString();
        if (str.equals("")) {
            return;
        }
        if (Double.parseDouble(str) != 0)
            addToCart(new Product(-1, getApplicationContext().getResources().getString(R.string.general), Double.parseDouble(str), SESSION._USER.getUserId(),""));
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

    public void touchPadClick(View view) {
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
                    addToCart(new Product(-1, getApplicationContext().getResources().getString(R.string.general), Double.parseDouble(touchPadPressed), SESSION._USER.getUserId(),""));
                touchPadPressed = "";
                break;
            case R.id.touchPadFragment_btDot:
                if (touchPadPressed.indexOf(".") < 0)
                    touchPadPressed += ".";
                break;
            case R.id.touchPadFragment_btCredit:
                if (!touchPadPressed.equals("")){
                    double newValue = Util.convertSign(Double.parseDouble(touchPadPressed));
                    touchPadPressed= String.valueOf(newValue);
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
        valueOfDiscount=0.0;
        clubDiscount = 0;
        clubPoint = 0;
        clubAmount = 0;
        Ppoint = 0;
        salesSaleMan.setText(getString(R.string.sales_man));
        SESSION._Rest();
        customerName_EditText.setText("");
        saleDetailsListViewAdapter = new SaleDetailsListViewAdapter(getApplicationContext(), R.layout.list_adapter_row_main_screen_sales_details, SESSION._ORDERS);
        lvOrder.setAdapter(saleDetailsListViewAdapter);
        custmerAssetstIdList = new ArrayList<Long>();
        orderIdList=new ArrayList<OrderDetails>();
        orderId=new ArrayList<Long>();
        offerDBAdapter = new OfferDBAdapter(this);
        offerDBAdapter.open();
        // Offer offer=offerDBAdapter.getAllValidOffers();

        List<Offer> offerList = offerDBAdapter.getAllOffersByStatus(1);
        if (offerList != null) {

            calculateTotalPriceWithOffers(offerList);
        } else {
            calculateTotalPrice();

        }

    }

    public void resumeSale(Order s) {
        if (SESSION._ORDERS.size() != 0) {
            Order sa = new Order(SESSION._SALE);

            sa.setOrders(SESSION._ORDERS);
            if (SESSION._SALES == null)
                SESSION._SALES = new ArrayList<Pair<Integer, Order>>();
            SESSION._SALES.add(new Pair<>(++SESSION.TEMP_NUMBER, sa));
            clearCart();
        }

        SESSION._SALE = new Order(s);
        SESSION._ORDERS = s.getOrders();
        saleDetailsListViewAdapter = new SaleDetailsListViewAdapter(getApplicationContext(), R.layout.list_adapter_row_main_screen_sales_details, SESSION._ORDERS);
        lvOrder.setAdapter(saleDetailsListViewAdapter);
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


        Rule3DbAdapter rule3DbAdapter = new Rule3DbAdapter(this);
        Rule7DbAdapter rule7DbAdapter = new Rule7DbAdapter(this);
        Rule11DBAdapter rule11DbAdapter = new Rule11DBAdapter(this);
        Rule8DBAdapter rule8DbAdapter = new Rule8DBAdapter(this);
        Rule5DBAdapter rule5DBAdapter = new Rule5DBAdapter(this);


        rule3DbAdapter.open();
        rule7DbAdapter.open();
        rule11DbAdapter.open();
        rule8DbAdapter.open();
        rule5DBAdapter.open();

        ArrayList<Offer> templist = new ArrayList<Offer>();
        for (int i = 0; i < templist.size(); i++) {
            templist.add(offers.get(i));
        }
        for (int i = 0; i < offers.size(); i++) {
            Offer offer = offers.get(i);


            ////////get rule3 information


            if (offer.getRuleName().equals(Rule.RULE3)) {

                Rule3 rule3 = rule3DbAdapter.getParcentForRule3(offer.getRuleID());
                if (rule3.getContain() == 1) {
                    SumForRule3Status = true;
                    availableRule3 = true;
                    parcentForRule3 = rule3.getPercent();
                } else if (rule3.getContain() == 0) {
                    availableRule3 = true;
                    parcentForRule3 = rule3.getPercent();

                }


///get Rule7 information
            } else if (offer.getRuleName().equals(Rule.RULE7)) {

                ProductOfferDBAdapter offersProducts = new ProductOfferDBAdapter(this);
                offersProducts.open();
                Rule7 rule7 = rule7DbAdapter.getPriceForRule7(offer.getRuleID());
                priceFoeRule7 = rule7.getPrice();

                productIDForRule7 = rule7.getProduct_id();

                if (rule7.getContain_club() == 1) {
                    clubStatusForRule7 = true;

                } else {
                    clubStatusForRule7 = false;
                }
            }

            /////Get Rule11 information

            else if (offer.getRuleName().equals(Rule.RULE11)) {
                Rule11 rule11 = rule11DbAdapter.getAmountForRule11(offer.getRuleID());

                if (rule11.getContain() == 1) {
                    SumForRule11Status = true;
                    availableRule11 = true;
                    amountForRule11 = rule11.getAmount();
                    DiscountamountForRule11 = rule11.getDiscountAmount();
                } else if (rule11.getContain() == 0) {
                    availableRule11 = true;
                    amountForRule11 = rule11.getAmount();
                    DiscountamountForRule11 = rule11.getDiscountAmount();
                }
                if (rule11.getClubContain() == 1) {
                    clubStatusForRule11 = true;
                } else {
                    clubStatusForRule11 = false;
                }

            }
            ///Get Rule8 information
            else if (offer.getRuleName().equals(Rule.RULE8)) {
                ProductOfferDBAdapter offersProducts = new ProductOfferDBAdapter(this);
                offersProducts.open();
                Rule8 rule8 = rule8DbAdapter.getParcentForRule8(offer.getRuleID());
                ParcentForRule8 = rule8.getPercent();
                productIDForRule8 = rule8.getProductID();

                if (rule8.getContainClub() == 1) {
                    clubStatusForRule8 = true;
                } else {
                    clubStatusForRule8 = false;
                }
            }
            //////Get Rule5 information
            else if (offer.getRuleName().equals(Rule.RULE5)) {
                ProductOfferDBAdapter offersProducts = new ProductOfferDBAdapter(this);
                offersProducts.open();

                final Rule5 rule5 = rule5DBAdapter.getGiftForRule5(offer.getRuleID());
                productIdForRule5 = rule5.getProductID();
                priceForRule5 = rule5.getPrice();
                giftProductIdForRule5 = rule5.getGift_id();

            }
        }

        ///end of offer list
        ////start order calculation and excecute offer


        saleTotalPrice = 0;
        SumForClub = 0;
        double SaleOriginalityPrice = 0;

        for (OrderDetails o : SESSION._ORDERS) {
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
                SESSION._SALE.setTotalPrice(saleTotalPrice);

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
            SESSION._SALE.setTotalPrice(saleTotalPrice);
        }


        if (stausForRule5) {

            AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this).create();
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
        SESSION._SALE.setTotalPrice(saleTotalPrice);


        rule3DbAdapter.close();
        rule7DbAdapter.close();
        rule8DbAdapter.close();
        rule11DbAdapter.close();
        rule5DBAdapter.close();
        offerDBAdapter.close();
    }

    protected void scanOffers() throws Exception {
        for (OrderDetails o : SESSION._ORDERS) {
            if (o.getProduct().getOffersIDs() != null) {
                offersList.get(o.getProduct().getOffersIDs().get(0)).getRule().execute(SESSION._ORDERS, offersList.get(0));
            }
        }
    }

    protected void calculateTotalPrice() {

        //scanOffers();


        if (customerClubId == 0) {
            saleTotalPrice = 0;
            double SaleOriginalityPrice = 0;
            for (OrderDetails o : SESSION._ORDERS) {
                saleTotalPrice += o.getItemTotalPrice();

                SaleOriginalityPrice += (o.getUnitPrice() * o.getQuantity());
            }
            totalSaved = (SaleOriginalityPrice - saleTotalPrice);
            tvTotalSaved.setText(String.format(new Locale("en"), "%.2f", (totalSaved)) + " " + getString(R.string.ins));
            tvTotalPrice.setText(String.format(new Locale("en"), "%.2f", saleTotalPrice) + " " + getString(R.string.ins));
            SESSION._SALE.setTotalPrice(saleTotalPrice);

        } else {

            saleTotalPrice = 0;
            double SaleOriginalityPrice = 0;
            for (OrderDetails o : SESSION._ORDERS) {
                saleTotalPrice += o.getItemTotalPrice();
                SaleOriginalityPrice += (o.getUnitPrice() * o.getQuantity());
            }

            if (clubType == 1) {
                saleTotalPrice = saleTotalPrice - (int) saleTotalPrice * clubDiscount;
                totalSaved = (SaleOriginalityPrice - saleTotalPrice);
                tvTotalPrice.setText(String.format(new Locale("en"), "%.2f", saleTotalPrice) + " " + getString(R.string.ins));
                tvTotalSaved.setText(String.format(new Locale("en"), "%.2f", (totalSaved)) + " " + getString(R.string.ins));
            }
            else if (clubType == 2 || clubType == 0) {
                tvTotalPrice.setText(String.format(new Locale("en"), "%.2f", saleTotalPrice) + " " + getString(R.string.ins));
                totalSaved = (SaleOriginalityPrice - saleTotalPrice);
                tvTotalSaved.setText(String.format(new Locale("en"), "%.2f", (totalSaved)) + " " + getString(R.string.ins));
                //  clubPoint=  ( (int)(sale/clubAmount)*clubPoint);
            }
            SESSION._SALE.setTotalPrice(saleTotalPrice);
        }


    }

    private void removeFromCart(int index) {
        SESSION._ORDERS.remove(index);
        saleDetailsListViewAdapter.setSelected(-1);
        refreshCart();
    }

    private void addToCart(Product p) {
        List<OrderDetails>orderList = new ArrayList<OrderDetails>();
        /*if(p.getOffersIDs()==null){
            ProductOfferDBAdapter productOfferDBAdapter = new ProductOfferDBAdapter(this);
            productOfferDBAdapter.open();
            p.setOffersIDs(productOfferDBAdapter.getProductOffers(p.getCashPaymentId(),offersIDsList));
            productOfferDBAdapter.close();
        }*/
        //test if cart have this order before insert to cart and order have'nt discount
        for(int i=0;i<SESSION._ORDERS.size();i++){
            OrderDetails o = SESSION._ORDERS.get(i);
            Log.d("Order",o.toString());
            Log.d("Product",p.toString());
            if(o.getProduct().equals(p)&&o.getDiscount()==0&&o.getProduct().getProductId()!=-1){
                orderList.add(o);
            }
        }
        if(orderList.size()>0){
            orderList.get(0).setCount(orderList.get(0).getQuantity()+1);
        }else {
            SESSION._ORDERS.add(new OrderDetails(1, 0, p, p.getPrice(), p.getPrice(),valueOfDiscount));

        }

        removeOrderItemSelection();
        refreshCart();
    }

    private void increaseItemOnCart(int index) {
        SESSION._ORDERS.get(index).increaseCount();
        refreshCart();
    }

    private void decreaseItemOnCart(int index) {
        SESSION._ORDERS.get(index).decreaseCount();
        refreshCart();
    }

    private void refreshCart() {

        // getOffers();
        saleDetailsListViewAdapter.notifyDataSetChanged();
        //lvOrder.setAdapter(saleDetailsListViewAdapter);
        offerDBAdapter = new OfferDBAdapter(this);
        offerDBAdapter.open();
        // Offer offer=offerDBAdapter.getAllValidOffers();
        List<Offer> offerList = offerDBAdapter.getAllOffersByStatus(1);
        offerList = null;
        if (offerList != null) {

            calculateTotalPriceWithOffers(offerList);
        } else {

            calculateTotalPrice();
        }

    }

    private void enterKeyPressed(String barcodeScanned) {
        Product product = productDBAdapter.getProductByBarCode(barcodeScanned);
        final Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
        intent.putExtra("barcode", barcodeScanned);
        if (product != null) {
            addToCart(product);
        } else {
            new AlertDialog.Builder(MainActivity.this)
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
        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
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
                }else if (id == 0) {
                    productList.addAll(productDBAdapter.getTopProducts(productLoadItemOffset, productCountLoad));
                } else {
                    productList.addAll(productDBAdapter.getAllProductsByDepartment(id, productLoadItemOffset, productCountLoad));
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

                    SESSION._SALE.setTotal_price(saleTotalPrice);
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
                    if (SESSION._SALE.getPayment().getPaymentWay().equals(CREDIT_CARD)) {

                        printer.PRN_PrintDotBitmap(invoiceImg.creditCardInvoice(SESSION._SALE, false, mainMer), 0);
                        printer.PRN_PrintAndFeedLine(11);
                        printer.PRN_HalfCutPaper();

                        printer.PRN_PrintDotBitmap(invoiceImg.creditCardInvoice(SESSION._SALE, false, mainCli), 0);
                    } else if (SESSION._CHECKS_HOLDER != null && SESSION._CHECKS_HOLDER.size() > 0) {
                        printer.PRN_PrintDotBitmap(invoiceImg.normalInvoice(SESSION._SALE.getCashPaymentId(), SESSION._ORDERS, SESSION._SALE, false, SESSION._USER, SESSION._CHECKS_HOLDER), 0);
                    } else {
                        printer.PRN_PrintDotBitmap(invoiceImg.normalInvoice(SESSION._SALE.getCashPaymentId(), SESSION._ORDERS, SESSION._SALE, false, SESSION._USER, null), 0);
                    }
                    return null;
                }
            }.execute();
        }
    */
    private void printAndOpenCashBoxBTP880(String mainAns, final String mainMer, final String mainCli) {
        final POSInterfaceAPI posInterfaceAPI = new POSUSBAPI(MainActivity.this);
        // final UsbPrinter printer = new UsbPrinter(1155, 30016);

        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
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
                InvoiceImg invoiceImg = new InvoiceImg(MainActivity.this);
                if (SESSION._SALE.getPayment().getPaymentWay().equals(CREDIT_CARD)) {
                    pos.imageStandardModeRasterPrint(invoiceImg.creditCardInvoice(SESSION._SALE, false, mainMer), CONSTANT.PRINTER_PAGE_WIDTH);
                    pos.systemFeedLine(2);
                    pos.systemCutPaper(66, 0);
                    pos.imageStandardModeRasterPrint(invoiceImg.creditCardInvoice(SESSION._SALE, false, mainCli), CONSTANT.PRINTER_PAGE_WIDTH);
                } else if (SESSION._CHECKS_HOLDER != null && SESSION._CHECKS_HOLDER.size() > 0) {
                    pos.imageStandardModeRasterPrint(invoiceImg.normalInvoice(SESSION._SALE.getOrderId(), SESSION._ORDERS, SESSION._SALE, false, SESSION._USER, SESSION._CHECKS_HOLDER), CONSTANT.PRINTER_PAGE_WIDTH);
                } else {
                    pos.imageStandardModeRasterPrint(invoiceImg.normalInvoice(SESSION._SALE.getOrderId(), SESSION._ORDERS, SESSION._SALE, false, SESSION._USER, null), CONSTANT.PRINTER_PAGE_WIDTH);
                }
                return null;
            }
        }.execute();
    }

    private static HPRTPrinterHelper HPRTPrinter = new HPRTPrinterHelper();

    private void printAndOpenCashBoxHPRT_TP805(final String mainAns, final String mainMer, final String mainCli) {
        if (HPRT_TP805.connect(this)) {
            final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
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
                    InvoiceImg invoiceImg = new InvoiceImg(MainActivity.this);
                    byte b = 0;
                    try {
                        if (mainAns.equals("PINPAD")) {
                            Map<String, String> clientNote = new ArrayMap<>();
                            Map<String, String> sellerNote = new ArrayMap<>();

                            JSONObject jsonObject = new JSONObject(mainMer);
                            Iterator<String> itr = null;
                            try {
                                itr = jsonObject.getJSONObject("receipt").keys();

                                while (itr.hasNext()) {
                                    String key = itr.next();
                                    if (jsonObject.getJSONObject("receipt").getJSONObject(key).getString("category").equals("BOTH")) {
                                        clientNote.put(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"), jsonObject.getJSONObject("receipt").getJSONObject(key).getString("value"));
                                        sellerNote.put(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"), jsonObject.getJSONObject("receipt").getJSONObject(key).getString("value"));
                                    } else if (jsonObject.getJSONObject("receipt").getJSONObject(key).getString("category").equals("CLIENT")) {
                                        clientNote.put(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"), jsonObject.getJSONObject("receipt").getJSONObject(key).getString("value"));
                                    } else if (jsonObject.getJSONObject("receipt").getJSONObject(key).getString("category").equals("SELLER")) {
                                        sellerNote.put(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"), jsonObject.getJSONObject("receipt").getJSONObject(key).getString("value"));
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Bitmap seller = invoiceImg.pinPadInvoice(SESSION._SALE, false, sellerNote);


                            HPRTPrinterHelper.PrintBitmap(seller, b, b, 300);

                            try {
                                HPRTPrinterHelper.CutPaper(HPRTPrinterHelper.HPRT_PARTIAL_CUT_FEED, 240);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Bitmap client = invoiceImg.pinPadInvoice(SESSION._SALE, false, clientNote);
                            HPRTPrinterHelper.PrintBitmap(client, b, b, 300);



                        } else if (SESSION._SALE.getPayment().getPaymentWay().equals(CREDIT_CARD)) {
                            Bitmap bitmap = invoiceImg.creditCardInvoice(SESSION._SALE, false, mainMer);

                            HPRTPrinterHelper.PrintBitmap(bitmap, b, b, 300);

                            try {
                                HPRTPrinterHelper.CutPaper(HPRTPrinterHelper.HPRT_PARTIAL_CUT_FEED, 240);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Bitmap bitmap2 = invoiceImg.creditCardInvoice(SESSION._SALE, false, mainCli);
                            HPRTPrinterHelper.PrintBitmap(bitmap2, b, b, 300);
                        } else if (SESSION._CHECKS_HOLDER != null && SESSION._CHECKS_HOLDER.size() > 0) {
                            Bitmap bitmap = invoiceImg.normalInvoice(SESSION._SALE.getOrderId(), SESSION._ORDERS, SESSION._SALE, false, SESSION._USER, SESSION._CHECKS_HOLDER);
                            HPRTPrinterHelper.PrintBitmap(bitmap, b, b, 300);
                        } else {
                            Bitmap bitmap = invoiceImg.normalInvoice(SESSION._SALE.getOrderId(), SESSION._ORDERS, SESSION._SALE, false, SESSION._USER, null);
                            HPRTPrinterHelper.PrintBitmap(bitmap, b, b, 300);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        }
        else{
            Toast.makeText(this, "Please connect the printer", Toast.LENGTH_SHORT).show();
        }
    }

    private void printAndOpenCashBoxSUNMI_T1(String mainAns, final String mainMer, final String mainCli) {
        //AidlUtil.getInstance().connectPrinterService(this);
        if (AidlUtil.getInstance().isConnect()) {
            final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle(getBaseContext().getString(R.string.wait_for_finish_printing));

            dialog.show();
            InvoiceImg invoiceImg = new InvoiceImg(MainActivity.this);
            byte b = 0;
            try {
                if (SESSION._SALE.getPayment().getPaymentWay().equals(CREDIT_CARD)) {
                    Bitmap bitmap = invoiceImg.creditCardInvoice(SESSION._SALE, false, mainMer);

                    AidlUtil.getInstance().printBitmap(bitmap);

                    //Thread.sleep(100);

                    AidlUtil.getInstance().feed();
                    AidlUtil.getInstance().cut();
                    //Thread.sleep(100);

                    Bitmap bitmap2 = invoiceImg.creditCardInvoice(SESSION._SALE, false, mainCli);
                    AidlUtil.getInstance().printBitmap(bitmap2);
                    //Thread.sleep(100);
                } else if (SESSION._CHECKS_HOLDER != null && SESSION._CHECKS_HOLDER.size() > 0) {
                    Bitmap bitmap = invoiceImg.normalInvoice(SESSION._SALE.getOrderId(), SESSION._ORDERS, SESSION._SALE, false, SESSION._USER, SESSION._CHECKS_HOLDER);
                    AidlUtil.getInstance().printBitmap(bitmap);
                } else {
                    Bitmap bitmap = invoiceImg.normalInvoice(SESSION._SALE.getOrderId(), SESSION._ORDERS, SESSION._SALE, false, SESSION._USER, null);
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
            Toast.makeText(this, "Printer Connect Error!", Toast.LENGTH_LONG).show();
        }
    }

    private void printSMS230(Bitmap bitmap) {
        String portSettings = "portable;escpos;l";
        String port = "BT:";
        int paperWidth = 576;
        paperWidth = 832; // 4inch (832 dot)
        paperWidth = 576; // 3inch (576 dot)1
        paperWidth = 384; // 2inch (384 dot)
        MiniPrinterFunctions.PrintBitmapImage(MainActivity.this, port,portSettings, bitmap, paperWidth, true, true);

    }

    private void printAndOpenCashBoxSM_S230I(String mainAns, final String mainMer, final String mainCli) {
        if (true) {
            final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
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
                    InvoiceImg invoiceImg = new InvoiceImg(MainActivity.this);
                    byte b = 0;
                    try {
                        if (SESSION._SALE.getPayment().getPaymentWay().equals(CREDIT_CARD)) {
                            Bitmap bitmap = invoiceImg.creditCardInvoice(SESSION._SALE, false, mainMer);

                            printSMS230(bitmap);
                            //cut
                            Bitmap bitmap2 = invoiceImg.creditCardInvoice(SESSION._SALE, false, mainCli);
                            printSMS230(bitmap2);
                        } else if (SESSION._CHECKS_HOLDER != null && SESSION._CHECKS_HOLDER.size() > 0) {
                            Bitmap bitmap = invoiceImg.normalInvoice(SESSION._SALE.getOrderId(), SESSION._ORDERS, SESSION._SALE, false, SESSION._USER, SESSION._CHECKS_HOLDER);
                            printSMS230(bitmap);
                        } else {
                            Bitmap bitmap = invoiceImg.normalInvoice(SESSION._SALE.getOrderId(), SESSION._ORDERS, SESSION._SALE, false, SESSION._USER, null);
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

    private void printAndOpenCashBox(String mainAns, final String mainMer, final String mainCli,int source) {
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
        if(source==REQUEST_CASH_ACTIVITY_CODE||source==REQUEST_CASH_ACTIVITY_WITH_CURRENCY_CODE)
            currencyReturnsCustomDialogActivity.show();

    }

    private CurrencyReturnsCustomDialogActivity currencyReturnsCustomDialogActivity;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Long.valueOf(SESSION._SALE.getCustomerId()) == 0) {
            if (SESSION._SALE.getCustomer_name() == null) {
                if (customerName_EditText.getText().toString().equals("")) {
                    SESSION._SALE.setCustomer_name("");
                } else {
                    SESSION._SALE.setCustomer_name(customerName_EditText.getText().toString());
                }
            }
        }

        //region CreditCard
        if (requestCode == REQUEST_CREDIT_CARD_ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {

                if (data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote).equals("anyType{}"))
                    return;
                SESSION._SALE.setTotalPaidAmount(SESSION._SALE.getTotalPrice());
                saleDBAdapter = new OrderDBAdapter(MainActivity.this);
                saleDBAdapter.open();
                clubPoint = ((int) (SESSION._SALE.getTotalPrice() / clubAmount) * clubPoint);
                long saleID = saleDBAdapter.insertEntry(SESSION._SALE, customerId, customerName);
                long tempSaleId=0;
                // Club with point and amount
                if (clubType == 2) {
                    pointFromSale = ((int) (SESSION._SALE.getTotalPrice() * clubPoint) / clubAmount);
                    sum_pointDbAdapter.insertEntry(saleIDforCash, pointFromSale, customerId);
                }

                if (equalUsedPoint) {
                    saleTotalPrice = 0.0;
                    SESSION._SALE.setTotalPrice(saleTotalPrice);
                    saleDBAdapter.updateEntry(SESSION._SALE);
                    usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, customerId);
                }
                if (lessUsedPoint) {
                    saleTotalPrice = 0.0;
                    SESSION._SALE.setTotalPrice(saleTotalPrice);
                    saleDBAdapter.updateEntry(SESSION._SALE);
                    usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, customerId);
                }
                if (biggerUsedPoint) {
                    SESSION._SALE.setTotalPrice(saleTotalPrice);
                    saleDBAdapter.updateEntry(SESSION._SALE);
                    usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, customerId);
                }
                saleDBAdapter.close();
                CreditCardPaymentDBAdapter creditCardPaymentDBAdapter = new CreditCardPaymentDBAdapter(this);
                creditCardPaymentDBAdapter.open();
                CreditCardPayment ccp = SESSION._TEMP_CREDITCARD_PAYMNET;

                creditCardPaymentDBAdapter.insertEntry(saleID, ccp.getAmount(), ccp.getCreditCardCompanyName(), ccp.getTransactionType(), ccp.getLast4Digits(), ccp.getTransactionId(), ccp.getAnswer(), ccp.getPaymentsNumber()
                        , ccp.getFirstPaymentAmount(), ccp.getOtherPaymentAmount(), ccp.getCreditCardCompanyName());

                creditCardPaymentDBAdapter.close();

                orderDBAdapter = new OrderDetailsDBAdapter(MainActivity.this);
                custmerAssetDB = new CustomerAssetDB(MainActivity.this);
                orderDBAdapter.open();
                custmerAssetDB.open();
                SESSION._SALE.setOrderId(saleID);
                if (forSaleMan) {
                    tempSaleId =saleID;
                    custmerAssetDB.insertEntry(saleID, custmerSaleAssetstId, SESSION._SALE.getTotalPrice(), 0, "Sale", SESSION._SALE.getCreatedAt());
                }
                // insert order region
                for (OrderDetails o : SESSION._ORDERS) {
                    long orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleIDforCash, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id());
                    orderId.add(orderid);
                    //   orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(),o.getCustomer_assistance_id());
                }
                // Order Sales man Region
                for (int i=0;i<orderIdList.size();i++) {
                    OrderDetails order = orderIdList.get(i);
                    long customerAssestId= custmerAssetstIdList.get(i);
                    for (int j = 0 ; j< SESSION._ORDERS.size();j++) {
                        OrderDetails o = SESSION._ORDERS.get(j);
                        long tempOrderId =orderId.get(i);
                        if (o==order) {
                            if (custmerAssetstIdList.get(i) != custmerSaleAssetstId) {
                                o.setCustomer_assistance_id(custmerAssetstIdList.get(i));
                                custmerAssetDB.insertEntry(tempOrderId, customerAssestId, o.getPaidAmount(), 0, "Order", SESSION._SALE.getCreatedAt());
                            }
                        }
                    }
                }
                orderDBAdapter.close();
                custmerAssetDB.close();
                SESSION._SALE.setOrders(SESSION._ORDERS);
                SESSION._SALE.setUser(SESSION._USER);

                PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(MainActivity.this);
                paymentDBAdapter.open();

                long paymentID = paymentDBAdapter.insertEntry(CREDIT_CARD, saleTotalPrice, saleID);

                paymentDBAdapter.close();

                Payment payment = new Payment(paymentID, CREDIT_CARD, saleTotalPrice, saleID);
                SESSION._SALE.setPayment(payment);


                Log.w("mainAns", data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY));
                Log.w("mainMer", data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_MerchantNote));
                Log.w("mainCli", data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote));


                printAndOpenCashBox(data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY),
                        data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_MerchantNote),
                        data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote),REQUEST_CREDIT_CARD_ACTIVITY_CODE);

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
                SESSION._SALE.setTotalPaidAmount(SESSION._SALE.getTotalPrice());
                saleDBAdapter = new OrderDBAdapter(MainActivity.this);
                saleDBAdapter.open();
                clubPoint = ((int) (SESSION._SALE.getTotalPrice() / clubAmount) * clubPoint);
                long saleID = saleDBAdapter.insertEntry(SESSION._SALE, customerId, customerName);
                long tempSaleId;
                saleDBAdapter.close();

                CreditCardPaymentDBAdapter creditCardPaymentDBAdapter = new CreditCardPaymentDBAdapter(this);
                creditCardPaymentDBAdapter.open();

                creditCardPaymentDBAdapter.insertEntry(saleID, ccp.getAmount(), ccp.getCreditCardCompanyName(), ccp.getTransactionType(), ccp.getLast4Digits(), ccp.getTransactionId(), ccp.getAnswer(), ccp.getPaymentsNumber()
                        , ccp.getFirstPaymentAmount(), ccp.getOtherPaymentAmount(), ccp.getCreditCardCompanyName());

                creditCardPaymentDBAdapter.close();

                orderDBAdapter = new OrderDetailsDBAdapter(MainActivity.this);
                custmerAssetDB = new CustomerAssetDB(MainActivity.this);
                orderDBAdapter.open();
                custmerAssetDB.open();
                SESSION._SALE.setOrderId(saleID);
                if (forSaleMan) {
                    tempSaleId =saleID;
                    custmerAssetDB.insertEntry(saleID, custmerSaleAssetstId, SESSION._SALE.getTotalPrice(), 0, "Sale", SESSION._SALE.getCreatedAt());
                }
                // insert order region
                for (OrderDetails o : SESSION._ORDERS) {
                    long orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleIDforCash, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id());
                    orderId.add(orderid);
                    //   orderDBAdapter.insertEntry(o.getProductId(), o.getCount(), o.getUserOffer(), saleID, o.getPrice(), o.getOriginal_price(), o.getDiscount(),o.getCustmerAssestId());
                }
                // Order Sales man Region
                for (int i=0;i<orderIdList.size();i++) {
                    OrderDetails order = orderIdList.get(i);
                    long customerAssestId= custmerAssetstIdList.get(i);
                    for (int j = 0 ; j< SESSION._ORDERS.size();j++) {
                        OrderDetails o = SESSION._ORDERS.get(j);
                        long tempOrderId =orderId.get(i);
                        if (o==order) {
                            if (custmerAssetstIdList.get(i) != custmerSaleAssetstId) {
                                o.setCustomer_assistance_id(custmerAssetstIdList.get(i));
                                custmerAssetDB.insertEntry(tempOrderId, customerAssestId, o.getPaidAmount(), 0, "Order", SESSION._SALE.getCreatedAt());
                            }
                        }
                    }
                }
                orderDBAdapter.close();
                custmerAssetDB.close();
                SESSION._SALE.setOrders(SESSION._ORDERS);
                SESSION._SALE.setUser(SESSION._USER);

                PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(MainActivity.this);
                paymentDBAdapter.open();

                long paymentID = paymentDBAdapter.insertEntry(CREDIT_CARD, saleTotalPrice, saleID);

                paymentDBAdapter.close();

                Payment payment = new Payment(paymentID, CREDIT_CARD, saleTotalPrice, saleID);
                SESSION._SALE.setPayment(payment);
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

                final double result = data.getDoubleExtra(ChecksActivity.LEAD_POS_RESULT_INTENT_CODE_CHECKS_ACTIVITY, 0.0f);
                SESSION._SALE.setTotalPaidAmount(result);
                saleDBAdapter = new OrderDBAdapter(MainActivity.this);
                saleDBAdapter.open();
                clubPoint = ((int) (SESSION._SALE.getTotalPrice() / clubAmount) * clubPoint);
                long saleID = saleDBAdapter.insertEntry(SESSION._SALE, customerId, customerName);
                long tempSaleId=0;
                // Club with point and amount
                if (clubType == 2) {
                    pointFromSale = ((int) (SESSION._SALE.getTotalPrice() * clubPoint) / clubAmount);
                    sum_pointDbAdapter.insertEntry(saleIDforCash, pointFromSale, customerId);
                }

                if (equalUsedPoint) {
                    saleTotalPrice = 0.0;
                    SESSION._SALE.setTotalPrice(saleTotalPrice);
                    saleDBAdapter.updateEntry(SESSION._SALE);
                    usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, customerId);
                }
                if (lessUsedPoint) {
                    saleTotalPrice = 0.0;
                    SESSION._SALE.setTotalPrice(saleTotalPrice);
                    saleDBAdapter.updateEntry(SESSION._SALE);
                    usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, customerId);
                }
                if (biggerUsedPoint) {
                    SESSION._SALE.setTotalPrice(saleTotalPrice);
                    saleDBAdapter.updateEntry(SESSION._SALE);
                    usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, customerId);
                }
                saleDBAdapter.close();

                orderDBAdapter = new OrderDetailsDBAdapter(MainActivity.this);
                custmerAssetDB = new CustomerAssetDB(MainActivity.this);
                orderDBAdapter.open();
                custmerAssetDB.open();
                SESSION._SALE.setOrderId(saleID);
                if (forSaleMan) {
                    tempSaleId =saleID;
                    custmerAssetDB.insertEntry(saleID, custmerSaleAssetstId, SESSION._SALE.getTotalPrice(), 0, "Sale", SESSION._SALE.getCreatedAt());
                }

                // insert order region
                for (OrderDetails o : SESSION._ORDERS) {
                    long orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleIDforCash, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id());
                    orderId.add(orderid);
                    //   orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(),o.getCustomer_assistance_id());
                }
                // Order Sales man Region
                for (int i=0;i<orderIdList.size();i++) {
                    OrderDetails order = orderIdList.get(i);
                    long customerAssestId= custmerAssetstIdList.get(i);
                    for (int j = 0 ; j< SESSION._ORDERS.size();j++) {
                        OrderDetails o = SESSION._ORDERS.get(j);
                        long tempOrderId =orderId.get(i);
                        if (o==order) {
                            if (custmerAssetstIdList.get(i) != custmerSaleAssetstId) {
                                o.setCustomer_assistance_id(custmerAssetstIdList.get(i));
                                custmerAssetDB.insertEntry(tempOrderId, customerAssestId, o.getPaidAmount(), 0, "Order", SESSION._SALE.getCreatedAt());
                            }
                        }
                    }
                }

                orderDBAdapter.close();

                PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(this);
                paymentDBAdapter.open();

                long paymentID = paymentDBAdapter.insertEntry(CHECKS, saleTotalPrice, saleID);

                paymentDBAdapter.close();

                Payment payment = new Payment(paymentID, CHECKS, saleTotalPrice, saleID);
                SESSION._SALE.setPayment(payment);

                ChecksDBAdapter checksDBAdapter = new ChecksDBAdapter(this);
                checksDBAdapter.open();
                for (Check check : SESSION._CHECKS_HOLDER) {
                    checksDBAdapter.insertEntry(check.getCheckNum(), check.getBankNum(), check.getBranchNum(), check.getAccountNum(), check.getAmount(), check.getCreatedAt(), saleID);
                }
                checksDBAdapter.close();


                printAndOpenCashBox("", "", "",REQUEST_CHECKS_ACTIVITY_CODE);
                return;
            }
        }
        //endregion

        //region Cash Activity WithOut Currency Region
        if (requestCode == REQUEST_CASH_ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(this);
                saleDBAdapter = new OrderDBAdapter(MainActivity.this);
                orderDBAdapter = new OrderDetailsDBAdapter(MainActivity.this);
                custmerAssetDB = new CustomerAssetDB(MainActivity.this);
                long tempSaleId=0;
                saleDBAdapter.open();
                orderDBAdapter.open();
                custmerAssetDB.open();
                paymentDBAdapter.open();

                // Get data from CashActivityWithOutCurrency
                double totalPaidWithOutCurrency = data.getDoubleExtra(OldCashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_WITHOUT_CURRENCY_TOTAL_PAID, 0.0f);
                double excess = data.getDoubleExtra(OldCashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_WITHOUT_CURRENCY_EXCESS_VALUE, 0.0f);

                SESSION._SALE.setTotalPaidAmount(totalPaidWithOutCurrency);

                clubPoint = ((int) (SESSION._SALE.getTotalPrice() / clubAmount) * clubPoint);
                saleIDforCash = saleDBAdapter.insertEntry(SESSION._SALE, customerId, customerName);
                SESSION._SALE.setOrderId(saleIDforCash);

                currencyReturnsCustomDialogActivity = new CurrencyReturnsCustomDialogActivity(this, excess,new Order(SESSION._SALE));

                /// Club with point and amount
                if (clubType == 2) {
                    pointFromSale = ((int) (SESSION._SALE.getTotalPrice() * clubPoint) / clubAmount);
                    sum_pointDbAdapter.insertEntry(saleIDforCash, pointFromSale, customerId);
                }

                if (equalUsedPoint) {
                    saleTotalPrice = 0.0;
                    SESSION._SALE.setTotalPrice(saleTotalPrice);
                    saleDBAdapter.updateEntry(SESSION._SALE);
                    usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, customerId);
                }
                if (lessUsedPoint) {
                    saleTotalPrice = 0.0;
                    SESSION._SALE.setTotalPrice(saleTotalPrice);
                    saleDBAdapter.updateEntry(SESSION._SALE);
                    usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, customerId);
                }
                if (biggerUsedPoint) {
                    SESSION._SALE.setTotalPrice(saleTotalPrice);
                    saleDBAdapter.updateEntry(SESSION._SALE);
                    usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, customerId);
                }
                // insert in Order , CustomerAssistant
                if (forSaleMan) {
                    tempSaleId =saleIDforCash;
                    custmerAssetDB.insertEntry(saleIDforCash, custmerSaleAssetstId, SESSION._SALE.getTotalPrice(), 0, "Sale", SESSION._SALE.getCreatedAt());
                }
                // insert order region
                for (OrderDetails o : SESSION._ORDERS) {
                    long orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleIDforCash, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id());
                    orderId.add(orderid);
                    //   orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(),o.getCustomer_assistance_id());
                }
                // Order Sales man Region
                for (int i=0;i<orderIdList.size();i++) {
                    OrderDetails order = orderIdList.get(i);
                    long customerAssestId= custmerAssetstIdList.get(i);
                    for (int j = 0 ; j< SESSION._ORDERS.size();j++) {
                        OrderDetails o = SESSION._ORDERS.get(j);
                        long tempOrderId =orderId.get(i);
                        if (o==order) {
                            if (custmerAssetstIdList.get(i) != custmerSaleAssetstId) {
                                o.setCustomer_assistance_id(custmerAssetstIdList.get(i));
                                custmerAssetDB.insertEntry(tempOrderId, customerAssestId, o.getPaidAmount(), 0, "Order", SESSION._SALE.getCreatedAt());
                            }
                        }
                    }
                }
                for (OrderDetails o : SESSION._ORDERS) {
                    if(o.getPaidAmount()<0&&customer!=null){
                        Customer upDateCustomer=customer;
                        upDateCustomer.setBalance(o.getPaidAmount());
                      customerDBAdapter.updateEntry(upDateCustomer);
                        Toast.makeText(MainActivity.this, upDateCustomer.toString(), Toast.LENGTH_SHORT).show();

                        Log.d("amount,id",o.getPaidAmount()+customer.getCustmerName()+"");

                    }else {
                        Toast.makeText(MainActivity.this, "fff", Toast.LENGTH_SHORT).show();

                        Log.d("amount,id","fff");

                    }
                    long orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleIDforCash, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id());
                    orderId.add(orderid);
                    //   orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(),o.getCustomer_assistance_id());
                }
                orderDBAdapter.close();
                custmerAssetDB.close();
                // End Order And CustomerAssistant Region
                // Payment Region
                long paymentID = paymentDBAdapter.insertEntry(CASH, saleTotalPrice, saleIDforCash);

                Payment payment = new Payment(paymentID, CASH, saleTotalPrice, saleIDforCash);
                SESSION._SALE.setPayment(payment);
                SESSION._SALE.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                paymentDBAdapter.close();
                printAndOpenCashBox("", "", "",REQUEST_CASH_ACTIVITY_CODE);
                saleDBAdapter.close();
                return;
            }

        }
        //endregion

        //region Currency Cash Activity Region
        if (requestCode == REQUEST_CASH_ACTIVITY_WITH_CURRENCY_CODE) {
            if (resultCode == RESULT_OK) {
                CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(this);
                PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(this);
                saleDBAdapter = new OrderDBAdapter(MainActivity.this);
                orderDBAdapter = new OrderDetailsDBAdapter(MainActivity.this);
                custmerAssetDB = new CustomerAssetDB(MainActivity.this);
                cashPaymentDBAdapter.open();
                saleDBAdapter.open();
                orderDBAdapter.open();
                custmerAssetDB.open();
                paymentDBAdapter.open();
                long tempSaleId=0;


                // Get data from CashActivityWithCurrency and insert in Cash Payment
                double totalPaidWithCurrency = data.getDoubleExtra(CashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_TOTAL_PAID, 0.0f);
                SESSION._SALE.setTotalPaidAmount(totalPaidWithCurrency);
                double firstCurrencyAmount = data.getDoubleExtra(CashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_FIRST_CURRENCY_AMOUNT, 0.0f);
                double secondCurrencyAmount = data.getDoubleExtra(CashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_SECOND_CURRENCY_AMOUNT, 0.0f);
                double excess = data.getDoubleExtra(CashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_EXCESS_VALUE, 0.0f);
                long secondCurrencyId = data.getLongExtra(CashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_SECOND_CURRENCY_ID, 0);
                long firstCurrencyId = data.getLongExtra(CashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_FIRST_CURRENCY_ID, 0);

                saleIDforCash = saleDBAdapter.insertEntry(SESSION._SALE, customerId, customerName);
                SESSION._SALE.setOrderId(saleIDforCash);
                currencyReturnsCustomDialogActivity = new CurrencyReturnsCustomDialogActivity(this, excess,new Order(SESSION._SALE));

                if (firstCurrencyAmount > 0) {
                    cashPaymentDBAdapter.insertEntry(saleIDforCash, firstCurrencyAmount, firstCurrencyId, new Timestamp(System.currentTimeMillis()));
                }
                if (secondCurrencyAmount > 0) {
                    cashPaymentDBAdapter.insertEntry(saleIDforCash, secondCurrencyAmount, secondCurrencyId, new Timestamp(System.currentTimeMillis()));
                }
                cashPaymentDBAdapter.close();


                // Club with point and amount
                if (clubType == 2) {
                    pointFromSale = ((int) (SESSION._SALE.getTotalPrice() * clubPoint) / clubAmount);
                    sum_pointDbAdapter.insertEntry(saleIDforCash, pointFromSale, customerId);
                }

                if (equalUsedPoint) {
                    saleTotalPrice = 0.0;
                    SESSION._SALE.setTotalPrice(saleTotalPrice);
                    saleDBAdapter.updateEntry(SESSION._SALE);
                    usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, customerId);
                }
                if (lessUsedPoint) {
                    saleTotalPrice = 0.0;
                    SESSION._SALE.setTotalPrice(saleTotalPrice);
                    saleDBAdapter.updateEntry(SESSION._SALE);
                    usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, customerId);
                }
                if (biggerUsedPoint) {
                    SESSION._SALE.setTotalPrice(saleTotalPrice);
                    saleDBAdapter.updateEntry(SESSION._SALE);
                    usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, customerId);
                }
                if (forSaleMan) {
                    tempSaleId =saleIDforCash;
                    custmerAssetDB.insertEntry(saleIDforCash, custmerSaleAssetstId, SESSION._SALE.getTotalPrice(), 0, "Sale", SESSION._SALE.getCreatedAt());
                }
                // insert order region
                for (OrderDetails o : SESSION._ORDERS) {
                    long orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleIDforCash, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id());
                    orderId.add(orderid);
                    //   orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(),o.getCustomer_assistance_id());
                }
                // Order Sales man Region
                for (int i=0;i<orderIdList.size();i++) {
                    OrderDetails order = orderIdList.get(i);
                    long customerAssestId= custmerAssetstIdList.get(i);
                    for (int j = 0 ; j< SESSION._ORDERS.size();j++) {
                        OrderDetails o = SESSION._ORDERS.get(j);
                        long tempOrderId =orderId.get(i);
                        if (o==order) {
                            if (custmerAssetstIdList.get(i) != custmerSaleAssetstId) {
                                o.setCustomer_assistance_id(custmerAssetstIdList.get(i));
                                custmerAssetDB.insertEntry(tempOrderId, customerAssestId, o.getPaidAmount(), 0, "Order", SESSION._SALE.getCreatedAt());
                            }
                        }
                    }
                }
                orderDBAdapter.close();
                custmerAssetDB.close();
                // End Order And CustomerAssistant Region

                // Payment Region
                long paymentID = paymentDBAdapter.insertEntry(CASH, saleTotalPrice, saleIDforCash);

                Payment payment = new Payment(paymentID, CASH, saleTotalPrice, saleIDforCash);

                SESSION._SALE.setPayment(payment);

                paymentDBAdapter.close();

                printAndOpenCashBox("", "", "",REQUEST_CASH_ACTIVITY_CODE);

                return;
            }
        }
        //endregion

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

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.pop_up, null);


        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //wlp.gravity = Gravity.CENTER_VERTICAL;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = (float) 0.6;
        window.setAttributes(wlp);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        popupWindow = new PopupWindow(popupView, (int) (width * 0.8), (int) (height * 0.8), true);

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        customer_id = (EditText) popupView.findViewById(R.id.customer_name);
        final GridView gvCustomer = (GridView) popupView.findViewById(R.id.popUp_gvCustomer);
        gvCustomer.setNumColumns(3);

        btn_cancel = (Button) popupView.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        ((Button) popupView.findViewById(R.id.btn_add))
                .setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
                        Intent intent = new Intent(MainActivity.this, AddNewCustomer.class);
                        startActivity(intent);

                        popupWindow.dismiss();


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
                customer = customerList.get(position);
                customerName = customer.getCustmerName();
                customerClubId = customer.getClub();
                customerId = customer.getCustomerId();
                customerName_EditText.setText(customerName);
                // get club Information
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

                popupWindow.dismiss();
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

                        if (c.getCustmerName().toLowerCase().contains(word.toLowerCase()) ||
                                c.getPhoneNumber().toLowerCase().contains(word.toLowerCase()) ||
                                c.getStreet().toLowerCase().contains(word.toLowerCase())) {
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
        UserDBAdapter userDB = new UserDBAdapter(this);
        userDB.open();
        final CustomerAssetDB customerAssistantDB = new CustomerAssetDB(this);
        customerAssistantDB.open();

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.custmer_assest_popup, null);
        popupWindow = new PopupWindow(popupView, 800, ActionBar.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        final EditText customerAssistant = (EditText) popupView.findViewById(R.id.customerAssest_name);
        ListView lvCustomerAssistant = (ListView) popupView.findViewById(R.id.customerAssistant_list_view);
        Button btn_cancel = (Button) popupView.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


        ((Button) popupView.findViewById(R.id.btn_add))
                .setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
                        Intent intent = new Intent(MainActivity.this, AddUserActivity.class);
                        startActivity(intent);

                        popupWindow.dismiss();
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
                                                           if(!orderIdList.contains(order)){
                                                               custmerAssetstIdList.add(custmerAssestList.get(position).getUserId());
                                                               orderIdList.add(order);
                                                           }

                                                           orderSalesMan.setText(custmerAssestList.get(position).getFullName());
                                                           deleteOrderSalesMan.setVisibility(View.VISIBLE);
                                                           popupWindow.dismiss();
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
        UserDBAdapter userDB = new UserDBAdapter(this);
        userDB.open();
        final CustomerAssetDB customerAssistantDb = new CustomerAssetDB(this);
        customerAssistantDb.open();

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.custmer_assest_popup, null);
        popupWindow = new PopupWindow(popupView, 800, ActionBar.LayoutParams.WRAP_CONTENT,
                true);

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        final EditText customerAssistant = (EditText) popupView.findViewById(R.id.customerAssest_name);

        ListView lvCustomerAssistant = (ListView) popupView.findViewById(R.id.customerAssistant_list_view);

        Button btn_cancel = (Button) popupView.findViewById(R.id.btn_cancel);
        Button btnDelete = (Button) popupView.findViewById(R.id.btn_delete);
        btnDelete.setVisibility(View.VISIBLE);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forSaleMan = false;
                salesSaleMan.setText(getString(R.string.sales_man));
            }
        });

        ((Button) popupView.findViewById(R.id.btn_add))
                .setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
                        Intent intent = new Intent(MainActivity.this, AddUserActivity.class);
                        startActivity(intent);

                        popupWindow.dismiss();


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
                custmerSaleAssetstId = custmerAssestList.get(position).getUserId();
                salesSaleMan.setText(custmerAssestList.get(position).getFullName());
                popupWindow.dismiss();
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
        if(event.getSource()==257){
            //barcode region
           if(event.getAction()==KeyEvent.ACTION_UP) {
               if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                   enterKeyPressed(barcodeScanned);
                   barcodeScanned="";

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
       }else {
            //editText region
           if (validChar(event.getDisplayLabel())) {
               fromEditText = fromEditText + event.getDisplayLabel();
               Log.d("fromEditText", fromEditText);

           }
            return super.dispatchKeyEvent(event);

       }
       if(event.getKeyCode()==KeyEvent.KEYCODE_BACK) {
           return super.dispatchKeyEvent(event);
       }

        return true;
    }

    public static int CharToASCII(final char character){
        return (int)character;
    }

    public static char ASCIIToChar(final int ascii){
        return (char)ascii;
    }

    public boolean validChar(char c) {
        //- (Dash), $ (Dollar), % (Percentage), (Space), . (Point), / (Slash), + (Plus)
        String code39="AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789-$% ./+";
        return code39.contains(c+"");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
