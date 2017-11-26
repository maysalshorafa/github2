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
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.FragmentTransaction;
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
import com.pos.leaders.leaderspossystem.CustomerAndClub.AddNewCustomer;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ChecksDBAdapter;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CreditCardPaymentDBAdapter;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerAssetDB;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.DepartmentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClubAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductOfferDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule11DBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule3DbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule5DBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule8DBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.SaleDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Sum_PointDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UsedPointDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UserDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ValueOfPointDB;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.Models.Department;
import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule11;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule3;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule5;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule8;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.Sale;
import com.pos.leaders.leaderspossystem.Models.User;
import com.pos.leaders.leaderspossystem.Printer.InvoiceImg;
import com.pos.leaders.leaderspossystem.Printer.SUNMI_T1.AidlUtil;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Models.Group;
import com.pos.leaders.leaderspossystem.Tools.CreditCardTransactionType;
import com.pos.leaders.leaderspossystem.Tools.CustmerAssestCatlogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Printer.HPRT_TP805;
import com.pos.leaders.leaderspossystem.Tools.ProductCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.CustomerCatalogGridViewAdapter;

import com.pos.leaders.leaderspossystem.Models.Offers.Rule7;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule7DbAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.SaleDetailsListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.CashActivity;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.Models.ValueOfPoint;
import com.pos.leaders.leaderspossystem.syncposservice.Service.SyncMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import HPRTAndroidSDK.HPRTPrinterHelper;
import POSAPI.POSInterfaceAPI;
import POSAPI.POSUSBAPI;
import POSSDK.POSSDK;

import static com.pos.leaders.leaderspossystem.Tools.CONSTANT.CASH;
import static com.pos.leaders.leaderspossystem.Tools.CONSTANT.CHECKS;
import static com.pos.leaders.leaderspossystem.Tools.CONSTANT.CREDIT_CARD;
import static java.lang.Thread.sleep;


/**
 * Created by Karam on 21/11/2016.
 */

public class MainActivity extends AppCompatActivity{
    private static final int REQUEST_CASH_ACTIVITY_CODE = 590;
    private static final int REQUEST_CHECKS_ACTIVITY_CODE = 753;
    private static final int REQUEST_CREDIT_CARD_ACTIVITY_CODE = 801;
    public static final String COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE = "com_pos_leaders_cart_total_price";
    String transID="";
    String a ="";
    TextView salesMan;
    TextView custmer_name, club_name,information;
    private DrawerLayout drawerLayout;
   //private ActionBarDrawerToggle actionBarDrawerToggle;//
   // private NavigationView navigationView;

    //ImageButton    btnLastSales;
    Button btnPercentProduct,btnPauseSale,btnResumeSale;
    ImageButton search_person;
    Button btnCancel, btnCash, btnCreditCard, btnOtherWays   ;
    TextView tvTotalPrice, tvTotalSaved , saleman;
    EditText etSearch;
    ImageButton btnDone;
    ImageButton btnGrid, btnList, used_point;
    ScrollView scDepartment;
    LinearLayout llDepartments;
    FrameLayout fragmentTouchPad;
    GridView gvProducts;
    ListView lvProducts;
    DepartmentDBAdapter departmentDBAdapter;
    ProductDBAdapter productDBAdapter;
    OfferDBAdapter offerDBAdapter;
    ProductOfferDBAdapter productOfferDBAdapter;
    SaleDBAdapter saleDBAdapter;
    OrderDBAdapter orderDBAdapter;
    View prseedButtonDepartments;
    List<Product> productList;
    List<Product> All_productsList;
    List<Customer>custmer_List;
    List<Customer> All_custmerList;
    CustomerDBAdapter customerDBAdapter;
    ClubAdapter groupDbAdapter;
    Group group=new Group(this);
    Sum_PointDbAdapter sum_pointDbAdapter;
    UsedPointDBAdapter usedpointDbAdapter;
    ValueOfPointDB valueOfPointDB;
    int newPoint=0;
    int aPoint=0;
    int unUsedPointForCustmer=0 ;
    int amount;
    int type;
    int point ;
    double offerAmount=0;
    long saleIDforCash ;
    String cInformation;
    double parcent=0.0;


    static List<Integer> offersIDsList;

    boolean equleUsedPoint=false;
    boolean biggerUsedPoint=false;
    boolean lessUsedPoint=false;
    List<Offer> offersList;

    long _custmer_id;
    ProductCatalogGridViewAdapter productCatalogGridViewAdapter;
    CustomerCatalogGridViewAdapter custmerCatalogGridViewAdapter;
    //ProductCatalogListViewAdapter productCatalogListViewAdapter;
    String barcodeScanned = "";
    ListView lvOrder;
    static Sale sale;
    SaleDetailsListViewAdapter saleDetailsListViewAdapter;
    View selectedIteminCartList;
    Order selectedOrderOnCart =null;
    private List<User> custmerAssestList;
    private List<User> AllCustmerAssestList;
    private  ListView lvcustmerAssest;
    private  EditText custmerAssest;
    public CustomerAssetDB custmerAssetDB ;
    public CustmerAssestCatlogGridViewAdapter custmerAssestCatlogGridViewAdapter;
    private boolean isGrid = true;
    double saleTotalPrice = 0.0;
    double totalSaved = 0.0;
    double secondPrice= 0.0;
    boolean userScrolled=false;
    int productLoadItemOffset=0;
    int productCountLoad=60;
    POSSDK pos;
    Button btn_cancel;
    LinearLayout ll;
    ImageView imv;
    int club_id;
    private String touchPadPressed = "";
    private boolean enableBackButton = true;

    PopupWindow popupWindow;
    EditText custmer_id;
    //Drw drw=null;
    //String devicePath="/dev/ttySAC1";
    EditText customerName_EditText;
////offer varible
    boolean SumForRule3Status=false;
    int SumForRule3=0;
    boolean clubStatusForRule3=false;
    boolean clubStatusForRule7=false;
    boolean clubStatusForRule8=false;
    boolean clubStatusForRule11=false;

    int SumClubForRule3=0;
    int SumClubForRule7=0;
    int SumClubForRule8=0;
    int SumClubForRule11=0;
double SumForClub=0.0;
    boolean SumForRule11Status=false;
    int SumForRule11=0;

    Boolean availableRule3=false;
    double parcentForRule3=0.0;
    double priceFoeRule7;
    double priceFoeRule8;

    long productIDForRule7;
    Boolean availableRule11=false;
    double amountForRule11=0;
    double DiscountamountForRule11=0;
    double ParcentForRule8=0.0;
    long productIDForRule8;
    int priceForRule5;
    long productIdForRule5;
    long giftProductIdForRule5;
    boolean stausForRule5=false;
    int Ppoint;
    double i=0.0;
    String str;
    boolean forSaleMan=false;
    boolean forOrderSaleMan=false;

    long custmerAssetstId;

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
            intent.putExtra(SyncMessage.API_DOMAIN_SYNC_MESSAGE, "http://185.118.252.26:8080/leadBO/");
            startService(intent);
        }



        used_point=(ImageButton)findViewById(R.id.usedPoint);
        custmer_name=(TextView)findViewById(R.id.cName);
        club_name=(TextView)findViewById(R.id.cClubName);
        information=(TextView)findViewById(R.id.cInformation);
        customerName_EditText =(EditText) findViewById(R.id.customer_textView);
        //a=customerName_EditText.getText().toString();

        search_person=(ImageButton)findViewById(R.id.searchPerson);
        drawerLayout = (DrawerLayout) findViewById(R.id.mainActivity_drawerLayout);
     //   navigationView = (NavigationView) findViewById(R.id.mainActivity_navigationView);
        //((MenuItem)(navigationView.findViewById(R.id.menuItem_ZReport))).setTitle("Z"+getString(R.string.reports));

        //region Init
        btnPauseSale = (Button) findViewById(R.id.mainActivity_BTNGeneralProduct);
        btnResumeSale = (Button) findViewById(R.id.mainActivity_BTNMultProduct);
        btnPercentProduct = (Button) findViewById(R.id.mainActivity_BTNPercentProduct);
      //  btnLastSales = (ImageButton) findViewById(R.id.mainActivity_BTNLastSales);
        btnCancel = (Button) findViewById(R.id.mainActivity_btnCancel);
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
        saleman=(TextView)findViewById(R.id.saleMan);
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
        showQuickPricePad();

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
        customerDBAdapter=new CustomerDBAdapter(this);
        groupDbAdapter=new ClubAdapter(this);
        valueOfPointDB =new ValueOfPointDB(this);
        sum_pointDbAdapter=new Sum_PointDbAdapter(this);
        usedpointDbAdapter=new UsedPointDBAdapter(this);
usedpointDbAdapter.open();
        sum_pointDbAdapter.open();

        customerDBAdapter.open();
        productDBAdapter.open();
        departmentDBAdapter.open();
        groupDbAdapter.open();
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
                offerDBAdapter.close();
                productOfferDBAdapter.close();

            }

            @Override
            protected Void doInBackground(Void... params) {
                offersIDsList = offerDBAdapter.getAllOffersIDsByStatus(Offer.Active);
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
        saleman.setOnClickListener(new View.OnClickListener() {
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
                parcent=0;
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
        btAllParams.setMargins(5,5,5,5);
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
        for(int k=0,r=0,c=0;k<departments.size();k++){
            if(k%2==1){
                LinearLayout ll = new LinearLayout(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                LinearLayout.LayoutParams btParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                btParams.setMargins(5,5,5,5);
                ll.setLayoutParams(params);

                Button bt = new Button(this);
                bt.setText(departments.get(k-1).getName());
                bt.setTag(departments.get(k-1));

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
                        productList = productDBAdapter.getAllProductsByDepartment(((Department)v.getTag()).getId(), productLoadItemOffset, productCountLoad);
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
                        productList = productDBAdapter.getAllProductsByDepartment(((Department)v.getTag()).getId(), productLoadItemOffset, productCountLoad);
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

        if(departments.size()%2==1){
            LinearLayout ll = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
            LinearLayout.LayoutParams btParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
            btParams.setMargins(5,5,5,5);
            ll.setLayoutParams(params);

            Button bt = new Button(this);
            bt.setText(departments.get(departments.size()-1).getName());
            bt.setTag(departments.get(departments.size()-1));
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
                    productList = productDBAdapter.getAllProductsByDepartment(((Department)v.getTag()).getId(), productLoadItemOffset, productCountLoad);
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
        etSearch.setOnKeyListener(new View.OnKeyListener() {
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
                productList = new ArrayList<Product>();
                String word = etSearch.getText().toString();

                if (!word.equals("")) {
                    for (Product p : All_productsList) {
                        if (p.getName().toLowerCase().contains(word.toLowerCase()) ||
                                p.getDescription().toLowerCase().contains(word.toLowerCase()) ||
                                p.getBarCode().toLowerCase().contains(word.toLowerCase())) {
                            productList.add(p);
                        }
                    }
                } else {
                    productList = All_productsList;
                }
                // Log.i("products", productList.toString());
                productCatalogGridViewAdapter = new ProductCatalogGridViewAdapter(getBaseContext(), productList);
                gvProducts.setAdapter(productCatalogGridViewAdapter);
                lvProducts.setAdapter(productCatalogGridViewAdapter);

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
                    enterKeyPressed();
                    barcodeScanned = "";
                    etSearch.setText("");
                    etSearch.requestFocus();
                } else
                    Toast.makeText(MainActivity.this, "Null", Toast.LENGTH_SHORT).show();
                //OpenCashBox();

            }
        });

        //endregion
/**
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
       drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
**/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //region Order List View


        if (SESSION._SALE != null) {
            sale = new Sale(SESSION._USER.getId(), new Date(), 0, false, 0, 0);
        } else {
            SESSION._SALE = new Sale(SESSION._USER.getId(), new Date(), 0, false, 0, 0);
        }

        if (SESSION._ORDERS != null) {
            lvOrder.setFocusable(false);
            offerDBAdapter=new OfferDBAdapter(this);
            offerDBAdapter.open();
            List<Offer>offerList=offerDBAdapter.getAllOffersByStatus(1);

                  //  Offer offer=offerDBAdapter.getAllValidOffers();


            if(offerList!=null){
                calculateTotalPriceWithOffers(offerList);
            }

           else{
                calculateTotalPrice();}
        } else {
            SESSION._ORDERS = new ArrayList<Order>();
        }
        saleDetailsListViewAdapter = new SaleDetailsListViewAdapter(getApplicationContext(), R.layout.list_adapter_row_main_screen_sales_details, SESSION._ORDERS);
        lvOrder.setAdapter(saleDetailsListViewAdapter);


        lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                removeOrderItemSelection();
                view.findViewById(R.id.rowSaleDetails_LLMethods).setVisibility(View.VISIBLE);
                view.findViewById(R.id.saleManLayout).setVisibility(View.VISIBLE);;

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

                salesMan = (TextView) view.findViewById(R.id.saleMan);
               salesMan.
                       setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callPopupOrderSalesMan();
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
                                final Dialog cashDialog = new Dialog(MainActivity.this);
                                cashDialog.setTitle(R.string.multi_count);
                                cashDialog.setContentView(R.layout.cash_payment_dialog);
                                cashDialog.show();
                                final Button cashBTOk = (Button) cashDialog.findViewById(R.id.cashPaymentDialog_BTOk);
                                final EditText cashETCash = (EditText) cashDialog.findViewById(R.id.cashPaymentDialog_TECash);
                                final Switch sw = (Switch) cashDialog.findViewById(R.id.cashPaymentDialog_SwitchProportion);
                                sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked) {
                                            sw.setText(getBaseContext().getString(R.string.amount));
                                        } else {
                                            sw.setText(getBaseContext().getString(R.string.proportion));
                                        }
                                    }
                                });
                                cashETCash.setHint(R.string.proportion);
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
                                        String str = cashETCash.getText().toString();
                                        int indexOfItem = SESSION._ORDERS.indexOf(selectedOrderOnCart);
                                        double X = SESSION._USER.getPresent();
                                        if (sw.isChecked()) {
                                            double d = Double.parseDouble(str);
                                            int count = SESSION._ORDERS.get(indexOfItem).getCount();
                                            double discount = (1 - (d / (SESSION._ORDERS.get(indexOfItem).getOriginal_price() * count)));

                                            if (discount <= (X / 100)) {
                                                SESSION._ORDERS.get(indexOfItem).setDiscount(discount * 100);
                                                refreshCart();
                                                cashDialog.cancel();
                                            } else {
                                                Toast.makeText(MainActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            if (!(str.equals(""))) {
                                                float val = Float.parseFloat(str);
                                                if (val <= X) {
                                                    int count = SESSION._ORDERS.get(indexOfItem).getCount();
                                                    SESSION._ORDERS.get(indexOfItem).setDiscount(val);
                                                    //SESSION._ORDERS.get(indexOfItem).setPrice(((SESSION._ORDERS.get(indexOfItem).getOriginal_price()*count) * ((1 - (val / 100))) / count));

                                                    refreshCart();
                                                    cashDialog.cancel();
                                                } else {
                                                    Toast.makeText(MainActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    }
                                });
                                Button cashBTCancel = (Button) cashDialog.findViewById(R.id.cashPaymentDialog_BTCancel);
                                cashBTCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        cashDialog.cancel();
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

                // // TODO: 20/10/2016 show dialog box for increes count and remove andd all the options
            }
        });


        lvOrder.setAdapter(saleDetailsListViewAdapter);


        //endregion

        offerDBAdapter=new OfferDBAdapter(this);
        offerDBAdapter.open();
       // Offer offer=offerDBAdapter.getAllValidOffers();
        List<Offer>offerList=offerDBAdapter.getAllOffersByStatus(1);

        if(offerList!=null){
            calculateTotalPriceWithOffers(offerList);
        }
        else{
            calculateTotalPrice();}
        //region Payment

        //region Cash

        btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SESSION._ORDERS.size()>0) {
                    Intent intent = new Intent(MainActivity.this, CashActivity.class);
                    intent.putExtra(COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE, saleTotalPrice);
                    startActivityForResult(intent, REQUEST_CASH_ACTIVITY_CODE);
                }
            }
        });

        used_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double unusedPointMoney=0.0;
                ValueOfPoint valueOfPoint=valueOfPointDB.getValue();
                double value= valueOfPoint.getValue();
                Toast.makeText(MainActivity.this,"value Of Point "+value,Toast.LENGTH_LONG).show();
                double newPrice=aPoint*value;
                if(saleTotalPrice==newPrice){
                   equleUsedPoint=true;
                    newPoint= (int) (saleTotalPrice/value);
                    saleTotalPrice=0;
                    tvTotalPrice.setText(String.format(new Locale("en"),"%.2f",0.0) + " " + getString(R.string.ins));
                }
                if(saleTotalPrice<newPrice){
lessUsedPoint=true;
 unusedPointMoney=newPrice-saleTotalPrice;
                    Toast.makeText(MainActivity.this,"uou have money more than sale totale price"+"   "+unusedPointMoney,Toast.LENGTH_LONG).show();
                     newPoint= (int) (saleTotalPrice/value);
                    saleTotalPrice=0;
                    tvTotalPrice.setText(String.format(new Locale("en"),"%.2f",saleTotalPrice) + " " + getString(R.string.ins));
                }
                if(saleTotalPrice>newPrice){
                    biggerUsedPoint=true;
saleTotalPrice=saleTotalPrice-newPrice;
                    unusedPointMoney=newPrice-saleTotalPrice;
                    Toast.makeText(MainActivity.this,"uou have money more than sale totale price"+unusedPointMoney,Toast.LENGTH_LONG).show();
                    tvTotalPrice.setText(String.format(new Locale("en"),"%.2f",saleTotalPrice) + " " + getString(R.string.ins));
                }
   }
        });
        //endregion


        //region Credit Card

        btnCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SESSION._ORDERS.size() > 0) {
                    final String __customerName = customerName_EditText.getText().toString();
                    final Context c = MainActivity.this;
                    new AlertDialog.Builder(c)
                            .setTitle(c.getResources().getString(R.string.clearCartAlertTitle))
                            //.setMessage(c.getResources().getString(R.string.clearCartAlertMessage))
                            .setPositiveButton(c.getResources().getString(R.string.by_card), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(c, CreditCardActivity.class);
                                    intent.putExtra(CreditCardActivity.LEADERS_POS_CREDIT_CARD_TOTAL_PRICE, saleTotalPrice);
                                    intent.putExtra(CreditCardActivity.LEADERS_POS_CREDIT_CARD_CUSTOMER, __customerName);

                                    intent.putExtra(CreditCardActivity.LEADERS_POS_CREDIT_CARD_TYPE, CreditCardActivity.LEADERS_POS_CREDIT_CARD_ACTIVITY_BY_PASS_CARD);
                                    startActivityForResult(intent, REQUEST_CREDIT_CARD_ACTIVITY_CODE);
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton(c.getResources().getString(R.string.by_phone), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    a = customerName_EditText.getText().toString();

                                    Intent intent = new Intent(c, CreditCardActivity.class);
                                    intent.putExtra(CreditCardActivity.LEADERS_POS_CREDIT_CARD_TOTAL_PRICE, saleTotalPrice);
                                    intent.putExtra(CreditCardActivity.LEADERS_POS_CREDIT_CARD_CUSTOMER, __customerName);

                                    intent.putExtra(CreditCardActivity.LEADERS_POS_CREDIT_CARD_TYPE, CreditCardActivity.LEADERS_POS_CREDIT_CARD_ACTIVITY_BY_PHONE);
                                    startActivityForResult(intent, REQUEST_CREDIT_CARD_ACTIVITY_CODE);
                                    dialog.cancel();
                                }
                            })
                            .setIcon(android.R.drawable.sym_contact_card)
                            .show();
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
                    a = customerName_EditText.getText().toString();

                    intent.putExtra("_Price", saleTotalPrice);
                    intent.putExtra("_custmer", a);

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
                    Sale s = new Sale(SESSION._SALE);
                    s.setOrders(SESSION._ORDERS);
                    if (SESSION._SALES == null)
                        SESSION._SALES = new ArrayList<Pair<Integer, Sale>>();
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
                    discountDialog.setTitle(R.string.please_select_discount_offer);
                    discountDialog.setContentView(R.layout.cash_payment_dialog);
                    discountDialog.show();

                    final Button btOK = (Button) discountDialog.findViewById(R.id.cashPaymentDialog_BTOk);
                    final Button btCancel = (Button) discountDialog.findViewById(R.id.cashPaymentDialog_BTCancel);
                    final EditText et = (EditText) discountDialog.findViewById(R.id.cashPaymentDialog_TECash);
                    final Switch sw = (Switch) discountDialog.findViewById(R.id.cashPaymentDialog_SwitchProportion);

                    sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (sw.isChecked()) {
                                sw.setText(R.string.amount);
                                et.setHint(R.string.amount);
                            } else {
                                sw.setText(R.string.proportion);
                                et.setHint(R.string.proportion);
                            }
                        }
                    });
                    et.setHint(R.string.proportion);
                    et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE) {
                                btOK.callOnClick();
                            }
                            return false;
                        }
                    });

                    btOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String str = et.getText().toString();
                            double X = SESSION._USER.getPresent();
                            if (sw.isChecked()) {
                                double d = Double.parseDouble(str);
                                double originalTotalPrice = 0;
                                for (Order o : SESSION._ORDERS) {
                                    originalTotalPrice += (o.getOriginal_price() * o.getCount());
                                }
                                if ((1 - (d / originalTotalPrice) <= (X / 100))) {
                                    double val = (1 - (d / originalTotalPrice)) * 100;
                                    for (Order o : SESSION._ORDERS) {
                                        o.setDiscount(val);
                                    }
                                    refreshCart();
                                    discountDialog.cancel();
                                } else {
                                    Toast.makeText(MainActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if (!(str.equals(""))) {
                                    float val = Float.parseFloat(str);
                                    if (val <= X) {
                                        for (Order o : SESSION._ORDERS) {
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

                    btCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            discountDialog.cancel();
                        }
                    });


                } else {
                    Toast.makeText(MainActivity.this, getBaseContext().getString(R.string.please_add_item), Toast.LENGTH_SHORT);
                }
            }
        });

        //endregion Percent Product


        //region last sale button

/**        btnLastSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SalesManagementActivity.class);
                i.putExtra("_custmer", a);

                startActivity(i);
            }
        });**/

        //endregion last sale button

        //endregion

       /*region navigation menu items

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = new Intent();
                switch (item.getItemId()) {
                    case R.id.menuItem_Product:
                        intent = new Intent(MainActivity.this, ProductCatalogActivity.class);
                        break;
                    case R.id.menuItem_Department:
                        intent = new Intent(MainActivity.this, DepartmentActivity.class);
                        break;
                    case R.id.menuItem_Users:
                        intent = new Intent(MainActivity.this, WorkerManagementActivity.class);
                        break;

                    case R.id.menuItem_Reports:
                        intent = new Intent(MainActivity.this, ReportsManagementActivity.class);
                        intent.putExtra("permissions_name",str);

                        break;
                    case R.id.menuItem_Setting:
                        // TODO: 30/03/2017 Settings Activity
                        intent = new Intent(MainActivity.this, SettingActivity.class);
                        break;
                    case R.id.menuItem_Backup:
                        intent = new Intent(MainActivity.this, BackupActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.menuItem_Custmer_Club:
                        intent = new Intent(MainActivity.this, OldCustomer.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                startActivity(intent);
                return false;
            }
        });
        //endregion
*/
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

        List<Integer>  mList = new ArrayList<Integer>();
        for (int i = 0; i < SESSION._SALES.size(); i++) {
            mList.add(SESSION._SALES.get(i).first);
        }

        gridView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, mList));
        gridView.setNumColumns(5);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                resumeSale(SESSION._SALES.get(position).second);
                Toast.makeText(MainActivity.this, getString(R.string.resume_deal_number)+" "+(SESSION._SALES.get(position).first), Toast.LENGTH_SHORT).show();
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

    public void quickPriceButtonClick(View view){
        String str = ((Button) view).getText().toString();
        if(str.equals("")){
            return;
        }
        if(Double.parseDouble(str)!=0)
            addToCart(new Product(-1, getApplicationContext().getResources().getString(R.string.general), Double.parseDouble(str), SESSION._USER.getId()));
    }

     private void showQuickPricePad(){
         QuickPricePadFragment fTP = new QuickPricePadFragment();
         FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
         transaction.add(R.id.mainActivity_fragmentQuickPricePad, fTP);
         transaction.commit();
     }

    private void showTouchPad(boolean b) {
        if(!b) {
            TouchPadFragment fTP = new TouchPadFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.mainActivity_fragmentTochPad, fTP);
            transaction.commit();
        }
        else {
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
                if(!touchPadPressed.equals(""))
                    touchPadPressed = Util.removeLastChar(touchPadPressed);
                removeOrderItemSelection();
                refreshCart();
                break;
            case R.id.touchPadFragment_btEnter:
                if(!touchPadPressed.equals(""))
                    addToCart(new Product(-1, getApplicationContext().getResources().getString(R.string.general), Double.parseDouble(touchPadPressed), SESSION._USER.getId()));
                touchPadPressed = "";
                break;
            case R.id.touchPadFragment_btDot:
                if(touchPadPressed.indexOf(".")<0)
                    touchPadPressed += ".";
                break;
        }
        TextView tirh=(TextView)this.findViewById(R.id.touchPadFragment_tvView);
        tirh.setText(touchPadPressed);
    }

    //endregion
/**
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
**/
    private void removeOrderItemSelection(){
       saleDetailsListViewAdapter.setSelected(-1);
        if(selectedIteminCartList!=null) {
            selectedIteminCartList.findViewById(R.id.rowSaleDetails_LLMethods).setVisibility(View.GONE);
            selectedIteminCartList.findViewById(R.id.saleMan).setVisibility(View.GONE);

            selectedIteminCartList.setBackgroundColor(getResources().getColor(R.color.white));
           selectedOrderOnCart=null;
        }
    }

    public void clearCart() {
        parcent=0;
        point=0;
        amount=0;
        Ppoint=0;
        saleman.setText("Sales Man");
        SESSION._Rest();
        club_name.setText("");
        custmer_name.setText("");
       information.setText("");
        customerName_EditText.setText("");
        saleDetailsListViewAdapter = new SaleDetailsListViewAdapter(getApplicationContext(), R.layout.list_adapter_row_main_screen_sales_details, SESSION._ORDERS);
        lvOrder.setAdapter(saleDetailsListViewAdapter);
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

    public void resumeSale(Sale s) {
        if (SESSION._ORDERS.size() != 0) {
            Sale sa = new Sale(SESSION._SALE);

            sa.setOrders(SESSION._ORDERS);
            if (SESSION._SALES == null)
                SESSION._SALES = new ArrayList<Pair<Integer, Sale>>();
            SESSION._SALES.add(new Pair<>(++SESSION.TEMP_NUMBER, sa));
            clearCart();
        }

        SESSION._SALE = new Sale(s);
        SESSION._ORDERS = s.getOrders();
        saleDetailsListViewAdapter = new SaleDetailsListViewAdapter(getApplicationContext(), R.layout.list_adapter_row_main_screen_sales_details, SESSION._ORDERS);
        lvOrder.setAdapter(saleDetailsListViewAdapter);
        refreshCart();
    }




    protected void calculateTotalPriceWithOffers(List <Offer>offers) {

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

        for (Order o : SESSION._ORDERS) {
            if (o.getProductId() == productIDForRule7) {
                if (SumForRule3Status || SumForRule11Status) {
                    saleTotalPrice += priceFoeRule7 * o.getCount();
                    SaleOriginalityPrice += (o.getOriginal_price() * o.getCount());
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
                    SaleOriginalityPrice += (o.getOriginal_price() * o.getCount());
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

                SaleOriginalityPrice += (o.getOriginal_price() * o.getCount());
            }
        }


        if (club_id != 0) {
            if (type == 1) {

                SumForClub = SumForClub * parcent;


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

                            //   SaleOriginalityPrice += rule5.getPrice() * o.getCount();
                        }
                    });
            alertDialog1.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            saleTotalPrice += i;
                            dialog.dismiss();

                            //   SaleOriginalityPrice += rule5.getPrice() * o.getCount();
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
        for (Order o : SESSION._ORDERS){
            if(o.getProduct().getOffersIDs()!=null){
                offersList.get(o.getProduct().getOffersIDs().get(0)).getRule().execute(SESSION._ORDERS,offersList.get(0));
            }
        }
    }


    protected void calculateTotalPrice() {

        //scanOffers();



        if(club_id==0){
            saleTotalPrice = 0;
            double SaleOriginalityPrice=0;
            for (Order o : SESSION._ORDERS) {
                saleTotalPrice += o.getItemTotalPrice();

                SaleOriginalityPrice += (o.getOriginal_price() * o.getCount());
            }
            totalSaved =(SaleOriginalityPrice-saleTotalPrice);
            tvTotalSaved.setText(String.format(new Locale("en"),"%.2f",(totalSaved))+" "+ getString(R.string.ins));
            tvTotalPrice.setText(String.format(new Locale("en"),"%.2f",saleTotalPrice) + " " + getString(R.string.ins));
            SESSION._SALE.setTotalPrice(saleTotalPrice);

        }
        else {

            saleTotalPrice = 0;
            double SaleOriginalityPrice=0;
            for (Order o : SESSION._ORDERS) {
                saleTotalPrice += o.getItemTotalPrice();
                SaleOriginalityPrice += (o.getOriginal_price() * o.getCount());
            }

            if(type==1){

                saleTotalPrice=saleTotalPrice-(int)saleTotalPrice*parcent;
                totalSaved =(SaleOriginalityPrice-saleTotalPrice);

                tvTotalPrice.setText(String.format(new Locale("en"),"%.2f",saleTotalPrice) + " " + getString(R.string.ins));
                tvTotalSaved.setText(String.format(new Locale("en"),"%.2f",(totalSaved))+" "+ getString(R.string.ins));

                SESSION._SALE.setTotalPrice(saleTotalPrice);
            }

            else if(type==2) {

                tvTotalPrice.setText(String.format(new Locale("en"),"%.2f",saleTotalPrice) + " " + getString(R.string.ins));


                totalSaved =(SaleOriginalityPrice-saleTotalPrice);
                tvTotalSaved.setText(String.format(new Locale("en"),"%.2f",(totalSaved))+" "+ getString(R.string.ins));
                //  point=  ( (int)(sale/amount)*point);

                SESSION._SALE.setTotalPrice(saleTotalPrice);

            }
        }



    }

    private void removeFromCart(int index) {
        SESSION._ORDERS.remove(index);
        saleDetailsListViewAdapter.setSelected(-1);
        refreshCart();
    }

    private void addToCart(Product p) {
        /*if(p.getOffersIDs()==null){
            ProductOfferDBAdapter productOfferDBAdapter = new ProductOfferDBAdapter(this);
            productOfferDBAdapter.open();
            p.setOffersIDs(productOfferDBAdapter.getProductOffers(p.getId(),offersIDsList));
            productOfferDBAdapter.close();
        }*/
        SESSION._ORDERS.add(new Order(1, 0, p, p.getPrice(), p.getPrice(), 0));
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
        offerDBAdapter=new OfferDBAdapter(this);
        offerDBAdapter.open();
       // Offer offer=offerDBAdapter.getAllValidOffers();
        List<Offer>offerList=offerDBAdapter.getAllOffersByStatus(1);
        offerList=null;
        if(offerList!=null){

            calculateTotalPriceWithOffers(offerList);
        }
        else{

            calculateTotalPrice();}

    }

    /**   private boolean getOffers(){
        for (Offer o : offersList) {//offer list
            if (o.getRuleId() == 0) {//offer type x on price y
                for (Order or : SESSION._ORDERS) {//loop into all product at cart
                    //if (o.getProducts().contains(or.getProduct())) {//product is in the offer
                    if (or.getCount() >= o.getX()) {//count > x
                        or.getProduct().setPrice(o.getY() / o.getX());//set the new price
                    }
                    //}
                }
            }
        }
        return false;
    }**/


    private void enterKeyPressed() {
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
    }

    private void loadMoreProduct(){
        productLoadItemOffset+=productCountLoad;
        final int id=prseedButtonDepartments.getId();
        final ProgressDialog dialog=new ProgressDialog(MainActivity.this);
        dialog.setTitle(getBaseContext().getString(R.string.wait_for_finish));

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                dialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                All_productsList=productList;
                productCatalogGridViewAdapter.notifyDataSetChanged();
                dialog.cancel();
            }

            @Override
            protected Void doInBackground(Void... params) {
                if(id==0){
                    productList.addAll(productDBAdapter.getTopProducts(productLoadItemOffset,productCountLoad));
                }
                else{
                    productList.addAll(productDBAdapter.getAllProductsByDepartment(id,productLoadItemOffset,productCountLoad));
                }
                return null;
            }
        }.execute();
    }


    private void printAndOpenCashBoxBTP880(String mainAns, final String mainMer, final String mainCli){
        final POSInterfaceAPI posInterfaceAPI = new POSUSBAPI(MainActivity.this);
        // final UsbPrinter printer = new UsbPrinter(1155, 30016);

        final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle(getBaseContext().getString(R.string.wait_for_finish_printing));

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                dialog.show();
                ////Hebrew 15 Windows-1255

                SESSION._SALE.setTotalPrice(saleTotalPrice);
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
                    pos.imageStandardModeRasterPrint(invoiceImg.normalInvoice(SESSION._SALE.getId(), SESSION._ORDERS, SESSION._SALE, false, SESSION._USER, SESSION._CHECKS_HOLDER), CONSTANT.PRINTER_PAGE_WIDTH);
                } else {
                    pos.imageStandardModeRasterPrint(invoiceImg.normalInvoice(SESSION._SALE.getId(), SESSION._ORDERS, SESSION._SALE, false, SESSION._USER, null), CONSTANT.PRINTER_PAGE_WIDTH);
                }
                return null;
            }
        }.execute();
    }

    private static HPRTPrinterHelper HPRTPrinter=new HPRTPrinterHelper();
    private void printAndOpenCashBoxHPRT_TP805(String mainAns, final String mainMer, final String mainCli) {
        if (HPRT_TP805.connect(this)) {
            final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle(getBaseContext().getString(R.string.wait_for_finish_printing));

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    dialog.show();
                    SESSION._SALE.setTotalPrice(saleTotalPrice);

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
                        if (SESSION._SALE.getPayment().getPaymentWay().equals(CREDIT_CARD)) {
                            Bitmap bitmap = invoiceImg.creditCardInvoice(SESSION._SALE, false, mainMer);

                            HPRTPrinterHelper.PrintBitmap(bitmap, b, b, 300);

                            HPRTPrinterHelper.FeedPaperToCutPosition(HPRTPrinterHelper.HPRT_FULL_CUT);
                            Bitmap bitmap2 = invoiceImg.creditCardInvoice(SESSION._SALE, false, mainCli);
                            HPRTPrinterHelper.PrintBitmap(bitmap2, b, b, 300);
                        } else if (SESSION._CHECKS_HOLDER != null && SESSION._CHECKS_HOLDER.size() > 0) {
                            Bitmap bitmap = invoiceImg.normalInvoice(SESSION._SALE.getId(), SESSION._ORDERS, SESSION._SALE, false, SESSION._USER, SESSION._CHECKS_HOLDER);
                            HPRTPrinterHelper.PrintBitmap(bitmap, b, b, 300);
                        } else {
                            Bitmap bitmap = invoiceImg.normalInvoice(SESSION._SALE.getId(), SESSION._ORDERS, SESSION._SALE, false, SESSION._USER, null);
                            HPRTPrinterHelper.PrintBitmap(bitmap, b, b, 300);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        }
    }

    private void printAndOpenCashBoxSUNMI_T1(String mainAns,final String mainMer,final String mainCli){
        AidlUtil.getInstance().connectPrinterService(this);
        if(AidlUtil.getInstance().isConnect()){
            final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle(getBaseContext().getString(R.string.wait_for_finish_printing));

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    dialog.show();
                    SESSION._SALE.setTotalPrice(saleTotalPrice);
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    try {
                        //cut
                        AidlUtil.getInstance().feed();
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
                }

                @Override
                protected Void doInBackground(Void... params) {
                    InvoiceImg invoiceImg = new InvoiceImg(MainActivity.this);
                    byte b = 0;
                    try {
                        if (SESSION._SALE.getPayment().getPaymentWay().equals(CREDIT_CARD)) {
                            Bitmap bitmap = invoiceImg.creditCardInvoice(SESSION._SALE, false, mainMer);

                            AidlUtil.getInstance().printBitmap(bitmap);

                            HPRTPrinterHelper.FeedPaperToCutPosition(HPRTPrinterHelper.HPRT_FULL_CUT);
                            Bitmap bitmap2 = invoiceImg.creditCardInvoice(SESSION._SALE, false, mainCli);
                            AidlUtil.getInstance().printBitmap(bitmap2);
                        } else if (SESSION._CHECKS_HOLDER != null && SESSION._CHECKS_HOLDER.size() > 0) {
                            Bitmap bitmap = invoiceImg.normalInvoice(SESSION._SALE.getId(), SESSION._ORDERS, SESSION._SALE, false, SESSION._USER, SESSION._CHECKS_HOLDER);
                            AidlUtil.getInstance().printBitmap(bitmap);
                        } else {
                            Bitmap bitmap = invoiceImg.normalInvoice(SESSION._SALE.getId(), SESSION._ORDERS, SESSION._SALE, false, SESSION._USER, null);
                            AidlUtil.getInstance().printBitmap(bitmap);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        }
        else {
            Toast.makeText(this, "Printer Connect Error!", Toast.LENGTH_LONG).show();
        }
    }

    private void printAndOpenCashBox(String mainAns, final String mainMer, final String mainCli) {
        switch (SETTINGS.printer){
            case BTP880:
                printAndOpenCashBoxBTP880(mainAns, mainMer, mainCli);
                break;
            case HPRT_TP805:
                printAndOpenCashBoxHPRT_TP805(mainAns, mainMer, mainCli);
                break;
            case SUNMI_T1:
                printAndOpenCashBoxSUNMI_T1(mainAns, mainMer, mainCli);
                break;
        }

        currencyReturnsCustomDialogActivity.show();

    }
    private CurrencyReturnsCustomDialogActivity currencyReturnsCustomDialogActivity;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (Long.valueOf(SESSION._SALE.getCustomer_id())==0) {
            if (SESSION._SALE.getCustomer_name() == null) {
                if (customerName_EditText.getText().toString().equals("")) {
                    SESSION._SALE.setCustomer_name("");
                }else{
                    SESSION._SALE.setCustomer_name(customerName_EditText.getText().toString());
                }
            }
        }

        if (requestCode == REQUEST_CREDIT_CARD_ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {

                if(data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote).equals("anyType{}"))
                SESSION._SALE.setTotalPaid(SESSION._SALE.getTotalPrice());
                saleDBAdapter = new SaleDBAdapter(MainActivity.this);
                saleDBAdapter.open();
                point = ((int) (SESSION._SALE.getTotalPrice() / amount) * point);
                long saleID = saleDBAdapter.insertEntry(SESSION._SALE, _custmer_id, a);
                if(club_id==2) {
                    sum_pointDbAdapter.insertEntry(saleID, point, _custmer_id);
                }
                /**  Point Ppoint=sum_pointDbAdapter.getPointInfo(saleID);
                 cInformation= String.valueOf(Ppoint.getPoint());
                 information.setText(cInformation);**/
                saleDBAdapter.close();

                orderDBAdapter = new OrderDBAdapter(MainActivity.this);
                custmerAssetDB=new CustomerAssetDB(MainActivity.this);
                orderDBAdapter.open();
                custmerAssetDB.open();
                SESSION._SALE.setId(saleID);
                for (Order o : SESSION._ORDERS) {
                    long orderid=orderDBAdapter.insertEntry(o.getProductId(), o.getCount(), o.getUserOffer(), saleID, o.getPrice(), o.getOriginal_price(), o.getDiscount(),o.getCustmerAssestId());

                    //   orderDBAdapter.insertEntry(o.getProductId(), o.getCount(), o.getUserOffer(), saleID, o.getPrice(), o.getOriginal_price(), o.getDiscount(),o.getCustmerAssestId());
                    if(forOrderSaleMan){
                        o.setCustmerAssestId(custmerAssetstId);
                        custmerAssetDB.insertEntry(orderid,o.getCustmerAssestId(),o.getPrice(),0,"Order",SESSION._SALE.getSaleDate().getTime());
                    }
                }
                if (forSaleMan){
                    custmerAssetDB.insertEntry(saleID,custmerAssetstId,SESSION._SALE.getTotalPrice(),0,"Sale",SESSION._SALE.getSaleDate().getTime());

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

                CreditCardPaymentDBAdapter creditCardPaymentDBAdapter = new CreditCardPaymentDBAdapter(this);
                creditCardPaymentDBAdapter.open();
                creditCardPaymentDBAdapter.insertEntry(saleID, saleTotalPrice, "", CreditCardTransactionType.NORMAL,
                        "0000", "", data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote), 0, 0, 0, "");
                creditCardPaymentDBAdapter.close();

                printAndOpenCashBox(data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY),
                        data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_MerchantNote),
                        data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote));

                //get the invoice plugin
                //print invoice

                Log.w("mainAns", data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY));
                Log.w("mainMer", data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_MerchantNote));
                Log.w("mainCli", data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote));

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
        if (requestCode == REQUEST_CHECKS_ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {

                final double result = data.getDoubleExtra(ChecksActivity.LEAD_POS_RESULT_INTENT_CODE_CHECKS_ACTIVITY, 0.0f);
                SESSION._SALE.setTotalPaid(result);
                saleDBAdapter = new SaleDBAdapter(MainActivity.this);
                saleDBAdapter.open();
                point = ((int) (SESSION._SALE.getTotalPrice() / amount) * point);
                long saleID = saleDBAdapter.insertEntry(SESSION._SALE, _custmer_id, a);
                if(club_id==2){
                sum_pointDbAdapter.insertEntry(saleID, point, _custmer_id);}
                saleDBAdapter.close();

                orderDBAdapter = new OrderDBAdapter(MainActivity.this);
                custmerAssetDB=new CustomerAssetDB(MainActivity.this);
                orderDBAdapter.open();
                custmerAssetDB.open();
                SESSION._SALE.setId(saleID);
                for (Order o : SESSION._ORDERS) {
                    long orderid=orderDBAdapter.insertEntry(o.getProductId(), o.getCount(), o.getUserOffer(), saleID, o.getPrice(), o.getOriginal_price(), o.getDiscount(),o.getCustmerAssestId());
                    if(forOrderSaleMan){
                    o.setCustmerAssestId(custmerAssetstId);
                        custmerAssetDB.insertEntry(orderid,o.getCustmerAssestId(),o.getPrice(),0,"Order",SESSION._SALE.getSaleDate().getTime());
                    }
                }
                if (forSaleMan){
                    custmerAssetDB.insertEntry(saleID,custmerAssetstId,SESSION._SALE.getTotalPrice(),0,"Sale",SESSION._SALE.getSaleDate().getTime());

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
                    checksDBAdapter.insertEntry(check.getCheckNum(), check.getBankNum(), check.getBranchNum(), check.getAccountNum(), check.getAmount(),check.getDate(), saleID);
                }
                checksDBAdapter.close();


                printAndOpenCashBox("", "", "");
                return;
            }
        }
        if (requestCode == REQUEST_CASH_ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {
                CashPaymentDBAdapter cashPaymentDBAdapter=new CashPaymentDBAdapter(this);
                cashPaymentDBAdapter.open();
                double totalPaid = data.getDoubleExtra(CashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_TOTAL_PAID, 0.0f);

                SESSION._SALE.setTotalPaid(totalPaid);

                saleDBAdapter = new SaleDBAdapter(MainActivity.this);
                orderDBAdapter = new OrderDBAdapter(MainActivity.this);
                custmerAssetDB = new CustomerAssetDB(MainActivity.this);
                PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(this);

                saleDBAdapter.open();
                point = ((int) (SESSION._SALE.getTotalPrice() / amount) * point);

                double firstCurrencyAmount = data.getDoubleExtra(CashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_FIRST_CURRENCY_AMOUNT, 0.0f);
                double secondCurrencyAmount = data.getDoubleExtra(CashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_SECOND_CURRENCY_AMOUNT, 0.0f);
                double excess = data.getDoubleExtra(CashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_EXCESS_VALUE, 0.0f);
                currencyReturnsCustomDialogActivity  = new CurrencyReturnsCustomDialogActivity(this, excess);
                long secondCurrencyId = data.getLongExtra(CashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_SECOND_CURRENCY_ID, 0);
                long firstCurrencyId = data.getLongExtra(CashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_FIRST_CURRENCY_ID, 0);

                saleIDforCash = saleDBAdapter.insertEntry(SESSION._SALE, _custmer_id, a);

                if (firstCurrencyAmount>0) {
                    cashPaymentDBAdapter.insertEntry(saleIDforCash, firstCurrencyAmount, firstCurrencyId, new Date());
                }
                if (secondCurrencyAmount>0){
                    cashPaymentDBAdapter.insertEntry(saleIDforCash,secondCurrencyAmount,secondCurrencyId,new Date());
                }
                cashPaymentDBAdapter.close();
                if(club_id==2){
                sum_pointDbAdapter.insertEntry(saleIDforCash, point, _custmer_id);}
                saleDBAdapter.close();

                if (equleUsedPoint) {
                    saleTotalPrice = 0.0;

                    SESSION._SALE.setTotalPaid(0.0);
                    saleDBAdapter.updateEntry(SESSION._SALE);

                    usedpointDbAdapter.insertEntry(saleIDforCash,newPoint,_custmer_id);
                }
                else if(biggerUsedPoint){
                    usedpointDbAdapter.insertEntry(saleIDforCash,aPoint,_custmer_id);
                }
                else if(lessUsedPoint) {
                    saleTotalPrice=0.0;

                    usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, _custmer_id);
                } else if (biggerUsedPoint) {
                    usedpointDbAdapter.insertEntry(saleIDforCash, aPoint, _custmer_id);
                } else if (lessUsedPoint) {
                    SESSION._SALE.setTotalPaid(0.0);
                    saleDBAdapter.updateEntry(SESSION._SALE);
                    usedpointDbAdapter.insertEntry(saleIDforCash, newPoint, _custmer_id);
                }


                orderDBAdapter.open();
                custmerAssetDB.open();
                SESSION._SALE.setId(saleIDforCash);

                for (Order o : SESSION._ORDERS) {
                    long orderid=orderDBAdapter.insertEntry(o.getProductId(), o.getCount(), o.getUserOffer(), saleIDforCash, o.getPrice(), o.getOriginal_price(), o.getDiscount(),o.getCustmerAssestId());

                    if(forOrderSaleMan){
                        o.setCustmerAssestId(custmerAssetstId);
                        custmerAssetDB.insertEntry(orderid,o.getCustmerAssestId(),o.getPrice(),0, "Order",SESSION._SALE.getSaleDate().getTime());
                    }
                }
                if (forSaleMan){
                    custmerAssetDB.insertEntry(saleIDforCash,custmerAssetstId,SESSION._SALE.getTotalPrice(),0,"Sale",SESSION._SALE.getSaleDate().getTime());
                }
                orderDBAdapter.close();
                custmerAssetDB.close();
                paymentDBAdapter.open();

                long paymentID = paymentDBAdapter.insertEntry(CASH, saleTotalPrice, saleIDforCash);

                Payment payment = new Payment(paymentID, CASH, saleTotalPrice, saleIDforCash);

                SESSION._SALE.setPayment(payment);

                paymentDBAdapter.close();

                printAndOpenCashBox("", "", "");
                return;
            }

        }
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
    @Override
    public void onBackPressed() {
        if(enableBackButton){
            //stop all moves
        }
        //do not move from here :)
    }**/



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
        custmer_id = (EditText) popupView.findViewById(R.id.customer_name);
        final GridView gvCustomer = (GridView) popupView.findViewById(R.id.popUp_gvCustomer);
        gvCustomer.setNumColumns(3);

        btn_cancel=(Button) popupView.findViewById(R.id.btn_cancel) ;

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


                    } });



        custmer_id.setText("");
        custmer_id.setHint("Search..");

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

        custmer_List = customerDBAdapter.getTopCustomer(0, 50);
        All_custmerList = custmer_List;

        custmerCatalogGridViewAdapter = new CustomerCatalogGridViewAdapter(getApplicationContext(), custmer_List);

        gvCustomer.setAdapter(custmerCatalogGridViewAdapter);

        gvCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parcent=0;
                a= custmer_List.get(position).getCustmerName();
                  customerName_EditText.setText(a);
                custmer_name.setText(a);

                _custmer_id =custmer_List.get(position).getId();
                club_id = (int) custmer_List.get(position).getClub();
                if (club_id != 0) {
                    Group group = groupDbAdapter.getGroupInfo(club_id);
                    type = group.getType();
                    if (type == 1) {
                        parcent = group.getParcent();

                        club_name.setText(group.getname());
                        information.setText(parcent + "");
                    } else if (type == 2) {

                        club_name.setText(group.getname());
                        amount = group.getAmount();
                        point = group.getPoint();
                        Ppoint = sum_pointDbAdapter.getPointInfo(_custmer_id);

                        cInformation = String.valueOf(Ppoint);

                        unUsedPointForCustmer = usedpointDbAdapter.getUnusedPointInfo(_custmer_id);
                        aPoint = Ppoint - unUsedPointForCustmer;
                        information.setText(aPoint + " ");
                    } else if (type == 3) {
                        club_name.setText(group.getname());
                        information.setText(getString(R.string.general));
                    }
                }
                popupWindow.dismiss();
            }
        });



        custmer_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                gvCustomer.setTextFilterEnabled(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

            @Override
            public void afterTextChanged(Editable s) {
                custmer_List = new ArrayList<Customer>();
                String word = custmer_id.getText().toString();

                if (!word.equals("")) {
                    for (Customer c : All_custmerList) {

                        if (c.getCustmerName().toLowerCase().contains(word.toLowerCase()) ||
                                c.getPhoneNumber().toLowerCase().contains(word.toLowerCase()) ||
                                c.getStreet().toLowerCase().contains(word.toLowerCase())) {
                            custmer_List.add(c);

                        }
                    }
                }
                else {
                    custmer_List=All_custmerList;
                }
                CustomerCatalogGridViewAdapter adapter = new CustomerCatalogGridViewAdapter(getApplicationContext(), custmer_List);
                gvCustomer.setAdapter(adapter);
                // Log.i("products", productList.toString());



            }
        });


    }

    public void callPopupOrderSalesMan() {
        UserDBAdapter userDB=new UserDBAdapter(this);
        userDB.open();
        final CustomerAssetDB custmerAssetDB=new CustomerAssetDB(this);
        custmerAssetDB.open();

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.custmer_assest_popup, null);
        popupWindow = new PopupWindow(popupView, 800, ActionBar.LayoutParams.WRAP_CONTENT,
                true);

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        custmerAssest = (EditText) popupView.findViewById(R.id.customerAssest_name);

        lvcustmerAssest=(ListView) popupView.findViewById(R.id.custmerAssest_list_view);

        Button  btn_cancel=(Button) popupView.findViewById(R.id.btn_cancel) ;
        Button  btnDelete=(Button) popupView.findViewById(R.id.btn_delete) ;

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forOrderSaleMan=false;
                salesMan.setText(getString(R.string.sales_man));
            }
        });

        ((Button) popupView.findViewById(R.id.btn_add))
                .setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
                        Intent intent = new Intent(MainActivity.this, AddUserActivity.class);
                        startActivity(intent);

                        popupWindow.dismiss();


                    } });



        custmerAssest.setText("");
        custmerAssest.setHint("Search..");

        custmerAssest.setFocusable(true);
        custmerAssest.requestFocus();
        custmerAssest.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                custmerAssest.setFocusable(true);
            }
        });

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        lvcustmerAssest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                forOrderSaleMan=true;
                custmerAssetstId=custmerAssestList.get(position).getId();
                salesMan.setText(custmerAssestList.get(position).getFullName());
                popupWindow.dismiss();
            }
        });
        lvcustmerAssest.setOnScrollListener(new AbsListView.OnScrollListener() {
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


        CustmerAssestCatlogGridViewAdapter adapter = new CustmerAssestCatlogGridViewAdapter(getApplicationContext(), custmerAssestList);
        lvcustmerAssest.setAdapter(adapter);



    }

    public void callPopupForSalesMan() {
        UserDBAdapter userDB=new UserDBAdapter(this);
        userDB.open();
        final CustomerAssetDB custmerAssetDB=new CustomerAssetDB(this);
        custmerAssetDB.open();

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.custmer_assest_popup, null);
        popupWindow = new PopupWindow(popupView, 800, ActionBar.LayoutParams.WRAP_CONTENT,
                true);

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        custmerAssest = (EditText) popupView.findViewById(R.id.customerAssest_name);

        lvcustmerAssest=(ListView) popupView.findViewById(R.id.custmerAssest_list_view);

      Button  btn_cancel=(Button) popupView.findViewById(R.id.btn_cancel) ;
        Button  btnDelete=(Button) popupView.findViewById(R.id.btn_delete) ;

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
     forSaleMan=false;
                saleman.setText("Sales Man");
            }
        });

        ((Button) popupView.findViewById(R.id.btn_add))
                .setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
                        Intent intent = new Intent(MainActivity.this, AddUserActivity.class);
                       startActivity(intent);

                        popupWindow.dismiss();


                    } });



        custmerAssest.setText("");
        custmerAssest.setHint("Search..");

        custmerAssest.setFocusable(true);
        custmerAssest.requestFocus();
        custmerAssest.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                custmerAssest.setFocusable(true);
            }
        });

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        lvcustmerAssest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                forSaleMan=true;
                custmerAssetstId=custmerAssestList.get(position).getId();
                 saleman.setText(custmerAssestList.get(position).getFullName());
popupWindow.dismiss();
            }
        });
        lvcustmerAssest.setOnScrollListener(new AbsListView.OnScrollListener() {
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


        CustmerAssestCatlogGridViewAdapter adapter = new CustmerAssestCatlogGridViewAdapter(getApplicationContext(), custmerAssestList);
        lvcustmerAssest.setAdapter(adapter);



    }



}