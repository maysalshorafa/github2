package com.pos.leaders.leaderspossystem;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
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
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.util.SerialInputOutputManager;
import com.itextpdf.text.DocumentException;
import com.pos.leaders.leaderspossystem.Balance.DeviceHelper;
import com.pos.leaders.leaderspossystem.Balance.DeviceSchema;
import com.pos.leaders.leaderspossystem.Balance.DevicesListAdapter;
import com.pos.leaders.leaderspossystem.Balance.Exception.CanNotOpenDeviceConnectionException;
import com.pos.leaders.leaderspossystem.Balance.Exception.NoDevicesAvailableException;
import com.pos.leaders.leaderspossystem.Balance.Exception.SendRequestException;
import com.pos.leaders.leaderspossystem.CreditCard.CreditCardActivity;
import com.pos.leaders.leaderspossystem.CustomerAndClub.AddNewCustomer;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CategoryDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ChecksDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClosingReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClosingReportDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClubAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CreditCardPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyOperationDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyReturnsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerAssetDB;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeePermissionsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferCategoryDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PosInvoiceDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductInventoryDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductOfferDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Sum_PointDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UsedPointDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ValueOfPointDB;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportCountDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.Category;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.ClosingReport;
import com.pos.leaders.leaderspossystem.Models.Club;
import com.pos.leaders.leaderspossystem.Models.CreditCardPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.Currency;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyOperation;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyReturns;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.Models.CustomerType;
import com.pos.leaders.leaderspossystem.Models.Documents;
import com.pos.leaders.leaderspossystem.Models.Employee;
import com.pos.leaders.leaderspossystem.Models.InvoiceStatus;
import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Models.OfferCategory;
import com.pos.leaders.leaderspossystem.Models.OpiningReport;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.OrderDocumentStatus;
import com.pos.leaders.leaderspossystem.Models.OrderDocuments;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.ProductUnit;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Models.ZReportCount;
import com.pos.leaders.leaderspossystem.Offers.OfferController;
import com.pos.leaders.leaderspossystem.Payment.MultiCurrenciesPaymentActivity;
import com.pos.leaders.leaderspossystem.Payment.PaymentTable;
import com.pos.leaders.leaderspossystem.Pinpad.PinpadActivity;
import com.pos.leaders.leaderspossystem.Printer.InvoiceImg;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;
import com.pos.leaders.leaderspossystem.Printer.PrinterTools;
import com.pos.leaders.leaderspossystem.Printer.SM_S230I.MiniPrinterFunctions;
import com.pos.leaders.leaderspossystem.Printer.SUNMI_T1.AidlUtil;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.CashActivity;
import com.pos.leaders.leaderspossystem.Tools.ConverterCurrency;
import com.pos.leaders.leaderspossystem.Tools.CreditCardTransactionType;
import com.pos.leaders.leaderspossystem.Tools.CustomerAssistantCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.CustomerCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.OldCashActivity;
import com.pos.leaders.leaderspossystem.Tools.ProductCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.SaleDetailsListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.ThisApp;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.Tools.symbolWithCodeHashMap;
import com.pos.leaders.leaderspossystem.Tools.updateCurrencyType;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;
import com.pos.leaders.leaderspossystem.syncposservice.Service.SyncMessage;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import net.sf.andpdf.nio.ByteBuffer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import HPRTAndroidSDK.HPRTPrinterHelper;
import POSAPI.POSInterfaceAPI;
import POSAPI.POSUSBAPI;
import POSSDK.POSSDK;

import static com.pos.leaders.leaderspossystem.Tools.SendLog.sendLogFile;

/**
 * Created by Karam on 21/11/2016.
 */

public class SalesCartActivity extends AppCompatActivity {
    private static final int REQUEST_CASH_ACTIVITY_WITH_CURRENCY_CODE = 590;
    private static final int REQUEST_CASH_ACTIVITY_CODE = 600;
    public static final int REQUEST_CHECKS_ACTIVITY_CODE = 753;
    public static final int REQUEST_CREDIT_CARD_ACTIVITY_CODE = 801;
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
    Button btnPayment, createInvoice , othersChoice;
    TextView tvTotalPrice;
    TextView tvTotalSaved;
    TextView salesSaleMan;
    static TextView customerBalance , productWeight , totalPriceForBalance;
    double PriceForWeight=0 , weightForProduct;
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
    int productCountLoad = 50;
    POSSDK pos;
    Button btn_cancel;
    LinearLayout ll;
    ImageView imv, btnCancel;
    public static String type="";

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
    TextView orderSalesMan, orderCount, orderTotalPrice, orderOfferName, Vat;
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
    List<Offer>validOffer = new ArrayList<>();
    CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(SalesCartActivity.this);
    ProductInventoryDbAdapter productInventoryDbAdapter;
    public static ArrayList<Bitmap> bitmapList=new ArrayList<Bitmap>();
    Bitmap newBitmap =null;
    boolean isWithSerialNo=false;
    ActionBar actionBar;
    ArrayList<DeviceSchema> devicesList = new ArrayList<>();
    DeviceSchema selectedDevice = null;
    int selectedDeviceIndex = -1;
    DevicesListAdapter devicesListAdapter;
    DeviceHelper deviceHelper;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private SerialInputOutputManager mSerialIoManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_temp);

        TitleBar.setTitleBar(this);

        ThisApp.setCurrentActivity(this);

        ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(SalesCartActivity.this);
        zReportDBAdapter.open();
        if(DbHelper.DATABASE_ENABEL_ALTER_COLUMN){
            Log.d("pooos",SESSION.POS_ID_NUMBER+"");
            zReportDBAdapter.upDatePosSalesV4();
            zReportDBAdapter.close();
            OpiningReport aReport = getLastAReport();
            ZReport zReport = getLastZReport();
            OrderDBAdapter orderDBAdapter = new OrderDBAdapter(getApplicationContext());
            orderDBAdapter.open();
            Order order = orderDBAdapter.getLast();
            orderDBAdapter.close();
            ClosingReport closingReport = getLastClosingReport();
            if(closingReport.getOpiningReportId()==aReport.getOpiningReportId()&&aReport.getLastZReportID()==zReport.getzReportId()){
                //insertZReport
                ZReport z = new ZReport();
                z.setCreatedAt( new Timestamp(System.currentTimeMillis()));
                z.setByUser(SESSION._EMPLOYEE.getEmployeeId());
                z.setStartOrderId(zReport.getEndOrderId()+1);
                Util.tempinsertZReport(z,getApplicationContext());
            }else if(closingReport.getOpiningReportId()!=aReport.getOpiningReportId()&&aReport.getLastZReportID()==zReport.getzReportId()){
                //insertZ and closing
                //insertZReport
                ZReport z = new ZReport();
                z.setCreatedAt( new Timestamp(System.currentTimeMillis()));
                z.setByUser(SESSION._EMPLOYEE.getEmployeeId());
                z.setStartOrderId(zReport.getEndOrderId()+1);
                ZReport zReport1 = Util.tempinsertZReport(z,getApplicationContext());
                ClosingReportDBAdapter closingReportDBAdapter= new ClosingReportDBAdapter(getApplicationContext());
                closingReportDBAdapter.open();
                long i =closingReportDBAdapter.insertEntry(0,0,0,new Timestamp(System.currentTimeMillis()),aReport.getOpiningReportId(),order.getOrderId(), SESSION._EMPLOYEE.getEmployeeId());
                closingReportDBAdapter.close();
                ClosingReportDetailsDBAdapter closingReportDetailsDBAdapter = new ClosingReportDetailsDBAdapter(getApplicationContext());
                closingReportDetailsDBAdapter.open();
                if(zReport1.getCheckTotal()>0) {
                    closingReportDetailsDBAdapter.insertEntry(i, 0, zReport1.getCheckTotal(), 0 - zReport1.getCheckTotal(), CONSTANT.CHECKS, SETTINGS.currencyCode);
                }else if(zReport1.getCreditTotal()>0) {
                    closingReportDetailsDBAdapter.insertEntry(i, 0, zReport1.getCreditTotal(), 0 - zReport1.getCreditTotal(), CONSTANT.CREDIT_CARD, SETTINGS.currencyCode);
                }
                closingReportDetailsDBAdapter.insertEntry(i,0,zReport1.getFirstTypeAmount(),0-zReport1.getFirstTypeAmount(),CONSTANT.CASH,SETTINGS.currencyCode);
                closingReportDetailsDBAdapter.close();
            }

            Util.addPosSetting(SalesCartActivity.this);

            DbHelper.DATABASE_ENABEL_ALTER_COLUMN=false;
        }
        if (!Util.isSyncServiceRunning(this)) {
            Intent intent = new Intent(SalesCartActivity.this, SyncMessage.class);
            intent.putExtra(SyncMessage.API_DOMAIN_SYNC_MESSAGE, SETTINGS.BO_SERVER_URL);
            startService(intent);
        }
//        currencyDBAdapter.open();
        context=SalesCartActivity.this;
        customerName_EditText = (EditText) findViewById(R.id.customer_textView);
        //getValid Offer
        offerDBAdapter=new OfferDBAdapter(SalesCartActivity.this);
        offerDBAdapter.open();
        validOffer=offerDBAdapter.getListOfValidOffers();
        offerDBAdapter.close();
        Button btAddProductShortLink = (Button) findViewById(R.id.mainActivity_btAddProductShortLink);
        btAddProductShortLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EmployeePermissionsDBAdapter employeeDBAdapter =new EmployeePermissionsDBAdapter(SalesCartActivity.this);
                employeeDBAdapter.open();
                List<Integer> employeePermition =employeeDBAdapter.getPermissions(SESSION._EMPLOYEE.getEmployeeId());
                employeeDBAdapter.close();
                if(employeePermition.contains(3)){
                    Intent a = new Intent(SalesCartActivity.this, ProductsActivity.class);
                    startActivity(a);}


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
        Vat=(TextView)findViewById(R.id.mainActivity_tvVat);
        //Vat.setText(SETTINGS.tax+" ");
        search_person = (ImageButton) findViewById(R.id.searchPerson);
        drawerLayout = (DrawerLayout) findViewById(R.id.mainActivity_drawerLayout);

        //region Init
        btnPauseSale = (Button) findViewById(R.id.mainActivity_BTNGeneralProduct);
        btnResumeSale = (Button) findViewById(R.id.mainActivity_BTNMultProduct);
        btnPercentProduct = (Button) findViewById(R.id.mainActivity_BTNPercentProduct);
        //  btnLastSales = (ImageButton) findViewById(R.id.mainActivity_BTNLastSales);
        btnCancel = (ImageView) findViewById(R.id.mainActivity_btnCancel);
        lvOrder = (ListView) findViewById(R.id.mainScreen_LVOrder);
        btnPayment = (Button) findViewById(R.id.mainActivity_btnPayment);
        /*btnCash = (Button) findViewById(R.id.mainActivity_btnCash);
        btnOtherWays = (Button) findViewById(R.id.mainActivity_btnOtherWays);
        btnCreditCard = (Button) findViewById(R.id.mainActivity_btnCreditCard);*/
        tvTotalPrice = (TextView) findViewById(R.id.mainActivity_tvTotalPrice);
        tvTotalSaved = (TextView) findViewById(R.id.mainActivity_tvTotalSaved);

        gvProducts = (GridView) findViewById(R.id.mainActivity_gvProducts);
        lvProducts = (ListView) findViewById(R.id.mainActivity_lvProducts);
        lvProducts.setVisibility(View.VISIBLE);
        //   lvcustmer.setVisibility(View.GONE);
        btnGrid = (ImageButton) findViewById(R.id.mainActivity_btnGrid);
        btnList = (ImageButton) findViewById(R.id.mainActivity_btnList);
        salesSaleMan = (TextView) findViewById(R.id.salesSaleMan);
        customerBalance = (TextView) findViewById(R.id.customerBalance);
        createInvoice = (Button)findViewById(R.id.mainActivity_BTNInvoice);
        othersChoice = (Button)findViewById(R.id.mainActivity_btnOthers);

        //    payment_by_customer_credit = (TextView)findViewById(R.id.mainActivity_payment_by_customer_credit);
        custmerAssetstIdList = new ArrayList<Long>();
        orderIdList = new ArrayList<OrderDetails>();
        orderId = new ArrayList<Long>();



        //cart discount init view
        llCartDiscount = (LinearLayout) findViewById(R.id.saleCart_llCartDiscount);
        tvCartDiscountValue = (TextView) findViewById(R.id.saleCart_llCartDiscountValue);
        tvTotalPriceBeforeCartDiscount = (TextView) findViewById(R.id.saleCart_tvTotalPriceBeforeCartDiscount);
      /*  productInventoryDbAdapter = new ProductInventoryDbAdapter(context);
        productInventoryDbAdapter.open();*/
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
        sum_pointDbAdapter = new Sum_PointDbAdapter(this);
        usedpointDbAdapter = new UsedPointDBAdapter(this);
        usedpointDbAdapter.open();
        sum_pointDbAdapter.open();
//
//        productDBAdapter.open();
        departmentDBAdapter.open();
        clubAdapter.open();

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
                lvProducts.setVisibility(View.VISIBLE);
                gvProducts.setVisibility(View.GONE);
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGrid = false;
                btnList.setImageDrawable(getResources().getDrawable(R.drawable.icon_listview_active));
                btnGrid.setImageDrawable(getResources().getDrawable(R.drawable.icon_gridview));
                lvProducts.setVisibility(View.GONE);
                gvProducts.setVisibility(View.VISIBLE);
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

        othersChoice.setOnClickListener(new View.OnClickListener() {
            int count=0;
            @Override
            public void onClick(View v) {
                final String[] items = {
                        getString(R.string.cancel_invoice),
                        getString(R.string.copyinvoice),
                        getString(R.string.replacement_invoice),getString(R.string.print)

                };

                final OrderDBAdapter orderDBAdapter =new OrderDBAdapter(context);
                orderDBAdapter.open();
                final  OrderDetailsDBAdapter orderDetailsDBAdapter = new OrderDetailsDBAdapter(context);
                orderDetailsDBAdapter.open();
                final Order lastOrder=orderDBAdapter.getLast();
                final Order getOrderDependId=orderDBAdapter.getOrderById(lastOrder.getOrderId());
                Log.d("getOrderDependId",getOrderDependId.toString());
                if(lastOrder!=null){
                    ChecksDBAdapter checksDBAdapter = new ChecksDBAdapter(SalesCartActivity.this);
                    checksDBAdapter.open();
                    final ArrayList<Check>checks=new ArrayList<Check>();
                    checks.addAll(checksDBAdapter.getPaymentBySaleID(lastOrder.getOrderId()));
                    checksDBAdapter.close();
                    AlertDialog.Builder builder = new AlertDialog.Builder(SalesCartActivity.this);
                    builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            Intent intent;
                            switch (item) {
                                case 0:
                                    if(SESSION._EMPLOYEE.getEmployeeId()!=2) {
                                        Toast.makeText(SalesCartActivity.this, getString(R.string.this_Operation_just_for_master_employee), Toast.LENGTH_LONG).show();

                                    }else {
                                        new android.support.v7.app.AlertDialog.Builder(SalesCartActivity.this)
                                                .setTitle(getString(R.string.cancel_invoice))
                                                .setMessage(getString(R.string.print_cancel_invoice))
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        long sID = orderDBAdapter.insertEntry(SESSION._EMPLOYEE.getEmployeeId(), new Timestamp(System.currentTimeMillis()), lastOrder.getReplacementNote(), true, lastOrder.getTotalPrice() * -1, lastOrder.getTotalPaidAmount() * -1, lastOrder.getCustomerId(), lastOrder.getCustomer_name(),lastOrder.getCartDiscount(),lastOrder.getNumberDiscount(),lastOrder.getOrderId(),lastOrder.getSalesBeforeTax()* -1,lastOrder.getSalesWithTax()* -1,lastOrder.getTotalSaved());
                                                        Order order = orderDBAdapter.getOrderById(sID);
                                                        lastOrder.setCancellingOrderId(sID);
                                                        Log.d("orderCancle",order.toString());
                                                        orderDBAdapter.updateEntry(lastOrder);
                                                        PaymentDBAdapter paymentDBAdapter1 = new PaymentDBAdapter(SalesCartActivity.this);
                                                        paymentDBAdapter1.open();
                                                        paymentDBAdapter1.insertEntry( lastOrder.getTotalPrice() * -1, sID,order.getOrderKey());
                                                        paymentDBAdapter1.close();
                                                        CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(SalesCartActivity.this);
                                                        cashPaymentDBAdapter.open();
                                                        cashPaymentDBAdapter.insertEntry(sID,lastOrder.getTotalPrice() * -1,0,new Timestamp(System.currentTimeMillis()),1,1);
                                                        cashPaymentDBAdapter.close();
                                                        CurrencyOperationDBAdapter currencyOperationDBAdapter = new CurrencyOperationDBAdapter(SalesCartActivity.this);
                                                        currencyOperationDBAdapter.open();
                                                        CurrencyReturnsDBAdapter currencyReturnsDBAdapter =new CurrencyReturnsDBAdapter(SalesCartActivity.this);
                                                        currencyReturnsDBAdapter.open();
                                                        List<OrderDetails>orderDetailsList=orderDetailsDBAdapter.getOrderBySaleID(lastOrder.getOrderId());
//                                                        Log.d("orderDetailsList",orderDetailsList.toString());
                                                        for(int i=0;i<orderDetailsList.size();i++){
                                                            OrderDetails o =orderDetailsList.get(i);
                                                            o.setOrderId(sID);
                                                            orderDetailsDBAdapter.insertEntryCancel(o);
                                                        }
                                                       if(SETTINGS.enableCurrencies){
                                                            currencyOperationDBAdapter.insertEntry(new Timestamp(System.currentTimeMillis()),sID,CONSTANT.DEBIT,lastOrder.getTotalPaidAmount() * -1,SETTINGS.currencyCode,CONSTANT.CASH);
                                                            currencyReturnsDBAdapter.insertEntry(lastOrder.getOrderId(),(lastOrder.getTotalPaidAmount()-lastOrder.getTotalPrice())*-1,new Timestamp(System.currentTimeMillis()),0);
                                                        }
                                                        Log.d("CncelInvoice",order.toString());
                                                  if (checks.size() > 0) {
                                                            try {
                                                                Intent i = new Intent(SalesCartActivity.this, SalesHistoryCopySales.class);
                                                                SETTINGS.copyInvoiceBitMap = invoiceImg.cancelingInvoice(order,orderDetailsList, false, checks);
                                                                startActivity(i);
                                                            } catch (Exception e) {
                                                                Log.d("exception", e.toString());
                                                                sendLogFile();
                                                            }
                                                        }else {
                                                            try {
                                                                Intent i = new Intent(SalesCartActivity.this, SalesHistoryCopySales.class);
                                                                SETTINGS.copyInvoiceBitMap = invoiceImg.cancelingInvoice(order, orderDetailsList,false, null);
                                                                startActivity(i);


                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                Log.d("exception", e.toString());
                                                                sendLogFile();
                                                            }
                                                        }
                                                        ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(SalesCartActivity.this);
                                                        zReportDBAdapter.open();
                                                        ZReportCountDbAdapter zReportCountDbAdapter = new ZReportCountDbAdapter(SalesCartActivity.this);
                                                        zReportCountDbAdapter.open();
                                                        ZReportCount zReportCount=null;
                                                        ZReport zReport1=null;
                                                        try {
                                                            zReportCount = zReportCountDbAdapter.getLastRow();
                                                            zReport1 = zReportDBAdapter.getLastRow();
                                                            Log.d("zReport1",zReport1.toString());

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        double SalesWitheTaxCancle=0,SalesWithoutTaxCancle=0,salesaftertaxCancle=0;
                                                        productDBAdapter.open();
                                                        for (int i=0;i<orderDetailsList.size();i++){
                                                            Product product = productDBAdapter.getProductByID(orderDetailsList.get(i).getProductId());
                                                            if (product.getCurrencyType().equals("0")){
                                                                updateCurrencyType.updateCurrencyToShekl(SalesCartActivity.this,product);
                                                            }
                                                                double rateCurrency=ConverterCurrency.getRateCurrency(product.getCurrencyType(),SalesCartActivity.this);
                                                              //  double rateCurrency=ConverterCurrency.getRateCurrency("ILS",SalesCartActivity.this);
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

                                                        }
                                                        productDBAdapter.close();
                                                        Log.d("zReportgetSalesBeforeTax",zReport1.getSalesBeforeTax()+"fji");
                                                        Log.d("zReportZreport",zReport1.getCashTotal()+"fji");
                                                        Log.d("zReportgetTOoo",lastOrder.getTotalPrice()+"fji");
                                                        zReport1.setCashTotal(zReport1.getCashTotal()-lastOrder.getTotalPrice());
                                                        zReport1.setFirstTypeAmount(zReport1.getFirstTypeAmount()-lastOrder.getTotalPrice());
                                                        zReport1.setSalesWithTax(zReport1.getSalesWithTax()-SalesWitheTaxCancle);

                                                        zReport1.setSalesBeforeTax(zReport1.getSalesBeforeTax()-SalesWithoutTaxCancle);
                                                        Log.d("Tax",zReport1.getTotalTax()+"fji");
                                                        Log.d("Tax1",Double.parseDouble(Util.makePrice(zReport1.getTotalTax()+(salesaftertaxCancle - SalesWitheTaxCancle)))+"fji1");

                                                        zReport1.setTotalTax(Double.parseDouble(Util.makePrice(zReport1.getTotalTax()- (salesaftertaxCancle - SalesWitheTaxCancle))));
                                                        zReportCount.setInvoiceReceiptCount(zReportCount.getInvoiceReceiptCount()+1);
                                                        zReportCount.setCashCount(zReportCount.getCashCount()-1);
                                                    //    zReportCount.setInvoiceReceiptCount(zReportCount.getInvoiceReceiptCount()-1);
                                                        zReportCount.setFirstTYpeCount(zReportCount.getFirstTYpeCount()-1);
                                                        zReportDBAdapter.updateEntry(zReport1);
                                                        zReportDBAdapter.close();
                                                        zReportCountDbAdapter.updateEntry(zReportCount);
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
                                    break;
                                case 1:
                                    if (SETTINGS.enableDuplicateInvoice){
                                    new android.support.v7.app.AlertDialog.Builder(SalesCartActivity.this)
                                            .setTitle(getString(R.string.copyinvoice))
                                            .setMessage(getString(R.string.print_copy_invoice))
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    CurrencyOperationDBAdapter currencyOperationDBAdapter = new CurrencyOperationDBAdapter(SalesCartActivity.this);
                                                    currencyOperationDBAdapter.open();
                                                    CurrencyReturnsDBAdapter currencyReturnsDBAdapter =new CurrencyReturnsDBAdapter(SalesCartActivity.this);
                                                    currencyReturnsDBAdapter.open();
                                                    Order copyOrder = orderDBAdapter.getLast();
                                                    OrderDetailsDBAdapter orderDetailsDBAdapter = new OrderDetailsDBAdapter(SalesCartActivity.this);
                                                    orderDetailsDBAdapter.open();
                                                    CashPaymentDBAdapter cashPaymentDBAdapter=new CashPaymentDBAdapter(SalesCartActivity.this);
                                                    cashPaymentDBAdapter.open();
                                                    PaymentDBAdapter paymentDBAdapter =new PaymentDBAdapter(SalesCartActivity.this);
                                                    paymentDBAdapter.open();
                                                    currencyOperationDBAdapter=new CurrencyOperationDBAdapter(SalesCartActivity.this);
                                                    currencyOperationDBAdapter.open();
                                                    currencyReturnsDBAdapter=new CurrencyReturnsDBAdapter(SalesCartActivity.this);
                                                    currencyReturnsDBAdapter.open();
                                                    List<OrderDetails>orderDetailsList=orderDetailsDBAdapter.getOrderBySaleID(copyOrder.getOrderId());
                                                    List<CashPayment>cashPaymentList=cashPaymentDBAdapter.getPaymentBySaleID(copyOrder.getOrderId());
                                                    cashPaymentDBAdapter.close();
                                                    List<Payment> paymentList =paymentDBAdapter.getPaymentBySaleID(copyOrder.getOrderId());
                                                    List<CurrencyOperation>currencyOperationList=currencyOperationDBAdapter.getCurrencyOperationByOrderID(copyOrder.getOrderId());
                                                    List<CurrencyReturns>currencyReturnsList=currencyReturnsDBAdapter.getCurencyReturnBySaleID(copyOrder.getOrderId());
                                                    Log.d("copyOrder",copyOrder.toString());
                                                    long orderId= orderDBAdapter.insertEntryDuplicate(copyOrder);
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
                                                    // orderDBAdapter.open();
                                                    orderDetailsDBAdapter.open();
                                                    Order order1 = orderDBAdapter.getOrderById(orderId);
                                                    orderDBAdapter.close();
                                                    SESSION._TEMP_ORDERS=order1;
                                                    Log.d("order1Du",SESSION._TEMP_ORDERS.toString());
                                                    SESSION._TEMP_ORDER_DETAILES=orderDetailsDBAdapter.getOrderBySaleID(order1.getOrderId());
                                                    Log.d("Temp",SESSION._TEMP_ORDER_DETAILES.toString());
                                                    List<CashPayment>cashPaymentList3=cashPaymentDBAdapter.getPaymentBySaleID(SESSION._TEMP_ORDERS.getOrderId());
                                                    Log.d("cashPaymentList3",cashPaymentList3.get(0).toString()+"Cddf");


                                                    ZReportDBAdapter zReportDBAdapter1 = new ZReportDBAdapter(SalesCartActivity.this);
                                                    zReportDBAdapter1.open();
                                                    ZReportCountDbAdapter zReportCountDbAdapter1 = new ZReportCountDbAdapter(SalesCartActivity.this);
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
                                                    productDBAdapter.open();
                                                    for (int i=0;i<orderDetailsList.size();i++){
                                                        Product product = productDBAdapter.getProductByID(orderDetailsList.get(i).getProductId());
                                                       if (product.getCurrencyType().equals("0")){
                                                           updateCurrencyType.updateCurrencyToShekl(SalesCartActivity.this,product);
                                                       }
                                                       //     double rateCurrency=ConverterCurrency.getRateCurrency("ILS",SalesCartActivity.this);
                                                            double rateCurrency=ConverterCurrency.getRateCurrency(product.getCurrencyType(),SalesCartActivity.this);
                                                            if(!product.isWithTax()){
                                                                if(copyOrder.getCartDiscount()>0){
                                                                    orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(copyOrder.getCartDiscount()/100))))));
                                                                    SalesWithoutTaxDuplicute+=(orderDetailsList.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                                    Log.d("SalesWithoutTaxCancle",SalesWithoutTaxDuplicute+"f");
                                                                }else {
                                                                    orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(orderDetailsList.get(i).getPaidAmount())));
                                                                    SalesWithoutTaxDuplicute += (orderDetailsList.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                                    Log.d("SalesWithoutTaxCancle",SalesWithoutTaxDuplicute+"f");
                                                                }
                                                            }else {
                                                                if(copyOrder.getCartDiscount()>0){

                                                                    orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(copyOrder.getCartDiscount()/100)))/ (1 + (SETTINGS.tax / 100)))));
                                                                    Log.d("salesaftertax", orderDetailsList.get(i).getPaidAmountAfterTax()+"ko2333"+orderDetailsList.get(i).getPaidAmount()+"ko2333"+(copyOrder.getCartDiscount()/100));
                                                                    salesaftertaxDuplicute+=((orderDetailsList.get(i).getPaidAmount()-(orderDetailsList.get(i).getPaidAmount()*(copyOrder.getCartDiscount()/100)))*rateCurrency);
                                                                    SalesWitheTaxDuplicute+=(orderDetailsList.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                                }else {
                                                                    orderDetailsList.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(orderDetailsList.get(i).getPaidAmount() / (1 + (SETTINGS.tax / 100)))));
                                                                    salesaftertaxDuplicute+=(orderDetailsList.get(i).getPaidAmount()*rateCurrency);
                                                                    SalesWitheTaxDuplicute+=(orderDetailsList.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                                }
                                                            }


                                                    }
                                                    productDBAdapter.close();
                                                    z.setCashTotal(z.getCashTotal()+copyOrder.getTotalPrice());
                                                    z.setInvoiceReceiptAmount(z.getInvoiceReceiptAmount()+copyOrder.getTotalPrice());
                                                    z.setFirstTypeAmount(z.getFirstTypeAmount()+copyOrder.getTotalPrice());
                                                    z.setSalesWithTax(Double.parseDouble(Util.makePrice(z.getSalesWithTax()+SalesWitheTaxDuplicute)));

                                                    z.setSalesBeforeTax(Double.parseDouble(Util.makePrice(z.getSalesBeforeTax()+SalesWithoutTaxDuplicute)));
                                                    z.setTotalTax(Double.parseDouble(Util.makePrice(z.getTotalTax()+((salesaftertaxDuplicute - SalesWitheTaxDuplicute)))));
                                                    zReportCount1.setCashCount(zReportCount1.getCashCount()+1);
                                                    zReportCount1.setInvoiceReceiptCount(zReportCount1.getInvoiceReceiptCount()+1);
                                                    zReportCount1.setFirstTYpeCount(zReportCount1.getFirstTYpeCount()+1);
                                                    zReportDBAdapter1.updateEntry(z);
                                                    zReportDBAdapter1.close();
                                                    zReportCountDbAdapter1.updateEntry(zReportCount1);
                                                    Activity a=getParent();
                                                    PrinterTools.printAndOpenCashBox("", "", "", 600,SalesCartActivity.this,a);
                                                }
                                            })
                                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // do nothing
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();}
                                    else{
                                        new android.support.v7.app.AlertDialog.Builder(SalesCartActivity.this)
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
                                    break;
                                case 2:
                                    OrderDBAdapter saleDBAdapter = new OrderDBAdapter(SalesCartActivity.this);
                                    saleDBAdapter.open();
                                    lastOrder.setReplacementNote(lastOrder.getReplacementNote() + 1);
                                    saleDBAdapter.updateEntry(lastOrder);
                                    saleDBAdapter.close();
                                    try {
                                        createReplacementInvoice createReplacementInvoice = new createReplacementInvoice(SalesCartActivity.this);
                                        createReplacementInvoice.execute(lastOrder);
                                       /* Intent i = new Intent(SalesCartActivity.this, SalesHistoryCopySales.class);
                                       // SETTINGS.copyInvoiceBitMap =invoiceImg.replacmentNote(lastOrder,false);
                                        startActivity(i);*/
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        Log.d("exception",lastOrder.toString());
                                        Log.d("exception",e.toString());
                                        sendLogFile();
                                    }
                                    break;
                                case 3:
                                    OrderDetailsDBAdapter orderDetailsDBAdapter = new OrderDetailsDBAdapter(SalesCartActivity.this);
                                    orderDetailsDBAdapter.open();
                                    final List<OrderDetails>orderDetailsList=orderDetailsDBAdapter.getOrderBySaleID(lastOrder.getOrderId());
                                    orderDetailsDBAdapter.close();

                                    new android.support.v7.app.AlertDialog.Builder(SalesCartActivity.this)
                                            .setTitle(getString(R.string.copyinvoice))
                                            .setMessage(getString(R.string.print_copy_invoice))
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    if (checks.size() > 0){
                                                        try {

                                                            Intent i = new Intent(SalesCartActivity.this, SalesHistoryCopySales.class);
                                                            SETTINGS.copyInvoiceBitMap = invoiceImg.normalInvoice(lastOrder.getOrderId(), orderDetailsList, lastOrder, true, SESSION._EMPLOYEE, checks,"");
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
                                                            SESSION._ORDER_DETAILES=orderDetailsList;
                                                            SESSION._ORDERS=lastOrder;
                                                            PrinterTools.printAndOpenCashBox("", "", "", 600,SalesCartActivity.this,getParent());
                                                            callClearCart();
                                                            /**Customer customer1 =sale.getCustomer();
                                                             Intent i = new Intent(OrdersManagementActivity.this, SalesHistoryCopySales.class);
                                                             SETTINGS.copyInvoiceBitMap =invoiceImg.copyInvoice(sale.getOrderId(), orders, sale, true, SESSION._EMPLOYEE, null);
                                                             startActivity(i);**/
                                                        }catch (Exception e){
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
                                    break;


                            }
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();



                }
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
                    // userScrolled = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount ==totalItemCount&& totalItemCount!=0) {

                    if(!userScrolled)
                    {
                        userScrolled=true;
                        loadMoreProduct();}
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
        productDBAdapter.open();
        productList = productDBAdapter.getTopProducts(0, 50);
        productDBAdapter.close();
        All_productsList = productList;
        productCatalogGridViewAdapter = new ProductCatalogGridViewAdapter(this, productList);
        gvProducts.setNumColumns(2);


        gvProducts.setAdapter(productCatalogGridViewAdapter);
        lvProducts.setAdapter(productCatalogGridViewAdapter);
        productCatalogGridViewAdapter.notifyDataSetChanged();
        //productCatalogGridViewAdapter.notifyDataSetChanged();

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
                productDBAdapter.open();
                productList = productDBAdapter.getTopProducts(productLoadItemOffset, productCountLoad);
                productDBAdapter.close();
                All_productsList = productList;
                productCatalogGridViewAdapter = new ProductCatalogGridViewAdapter(getApplicationContext(), productList);
                gvProducts.setAdapter(productCatalogGridViewAdapter);
                lvProducts.setAdapter(productCatalogGridViewAdapter);
                productCatalogGridViewAdapter.notifyDataSetChanged();
            }
        });
        departmentDBAdapter.open();
        List<Category> departments = departmentDBAdapter.getAllDepartments();
        departmentDBAdapter.close();
        final int co = 2;
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
                        productDBAdapter.open();
                        productList = productDBAdapter.getAllProductsByCategory(((Category) v.getTag()).getCategoryId(), productLoadItemOffset, productCountLoad);
                        productDBAdapter.close();
                        All_productsList = productList;
                        productCatalogGridViewAdapter = new ProductCatalogGridViewAdapter(getApplicationContext(), productList);
                        gvProducts.setAdapter(productCatalogGridViewAdapter);
                        lvProducts.setAdapter(productCatalogGridViewAdapter);
                        productCatalogGridViewAdapter.notifyDataSetChanged();
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
                        productDBAdapter.open();
                        productList = productDBAdapter.getAllProductsByCategory(((Category) v.getTag()).getCategoryId(), productLoadItemOffset, productCountLoad);
                        productDBAdapter.close();
                        All_productsList = productList;
                        productCatalogGridViewAdapter = new ProductCatalogGridViewAdapter(getApplicationContext(), productList);
                        gvProducts.setAdapter(productCatalogGridViewAdapter);
                        lvProducts.setAdapter(productCatalogGridViewAdapter);
                        productCatalogGridViewAdapter.notifyDataSetChanged();
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
                    productDBAdapter.open();
                    productList = productDBAdapter.getAllProductsByCategory(((Category) v.getTag()).getCategoryId(), productLoadItemOffset, productCountLoad);
                    productDBAdapter.close();
                    All_productsList = productList;
                    productCatalogGridViewAdapter = new ProductCatalogGridViewAdapter(getApplicationContext(), productList);
                    gvProducts.setAdapter(productCatalogGridViewAdapter);
                    lvProducts.setAdapter(productCatalogGridViewAdapter);
                    productCatalogGridViewAdapter.notifyDataSetChanged();
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
                        protected Void doInBackground(final String... params) {

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    productDBAdapter.open();
                                    productList.addAll(productDBAdapter.getAllProductsByHint(params[0], productList.size()-1, 50));
                                    productDBAdapter.close();
                                    // Stuff that updates the UI
                                    productCatalogGridViewAdapter.notifyDataSetChanged();
                                }
                            });

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Log.d("tttttttt",productList.toString());
                            Log.d("tttttttt",productList.size()+"");

                        /*    if(productList.size()==1){
                                productList.remove(1);
                            }*/
                            //if(productCatalogGridViewAdapter==null){
                            productCatalogGridViewAdapter = new ProductCatalogGridViewAdapter(getApplicationContext(), productList);
                            lvProducts.setAdapter(productCatalogGridViewAdapter);
                            gvProducts.setAdapter(productCatalogGridViewAdapter);
                            productCatalogGridViewAdapter.notifyDataSetChanged();
                            //    }
                        }
                    }.execute(word);
                } else {
                    productList = All_productsList;
                    ProductCatalogGridViewAdapter adapter = new ProductCatalogGridViewAdapter(getApplicationContext(), productList);
                    gvProducts.setAdapter(adapter);
                    lvProducts.setAdapter(adapter);
                    productCatalogGridViewAdapter.notifyDataSetChanged();

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
            if (SESSION._EMPLOYEE!=null)
            sale = new Order(SESSION._EMPLOYEE.getEmployeeId(), new Timestamp(System.currentTimeMillis()), 0, false, 0, 0);
        } else {
            if (SESSION._EMPLOYEE!=null)
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
                                        if ( Long.valueOf(SESSION._ORDER_DETAILES.get(indexOfItem).getProduct().getOfferId())==null){
                                            Log.d("offerIdNull","cashBTOkClick");
                                        }
                                        else {
                                        if( SESSION._ORDER_DETAILES.get(indexOfItem).getProduct().getOfferId()==0) {
                                            SESSION._ORDER_DETAILES.get(indexOfItem).setCount(pid);
                                            orderCount.setText(SESSION._ORDER_DETAILES.get(position).getQuantity() + "");
                                            orderTotalPrice.setText(selectedOrderOnCart.getPaidAmount() * selectedOrderOnCart.getQuantity() + SETTINGS.currencySymbol);
                                        }else {
                                            for (int i=0;i<pid-1;i++){
                                                try {
                                                    addToCart(SESSION._ORDER_DETAILES.get(indexOfItem).getProduct());
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }}
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
                        if (SESSION._ORDER_DETAILES.size() > 0) {
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
                                    selectedOrderOnCart.rowDiscount = 0;
                                    selectedOrderOnCart.setDiscount(0);
                                    double itemOriginalPrice = selectedOrderOnCart.getPaidAmount();
                                    String currencyType="";
                                    if (selectedOrderOnCart.getProduct().getCurrencyType().equals("0")){
                                        updateCurrencyType.updateCurrencyToShekl(SalesCartActivity.this,selectedOrderOnCart.getProduct());
                                    }

                                    type= String.valueOf(symbolWithCodeHashMap.valueOf(selectedOrderOnCart.getProduct().getCurrencyType()));

                                   /* if(selectedOrderOnCart.getProduct().getCurrencyType()==0) {
                                        currencyType="ILS";
                                        type=context.getString(R.string.ins);
                                    }
                                    if(selectedOrderOnCart.getProduct().getCurrencyType()==1) {
                                        currencyType="USD";
                                        type=context.getString(R.string.dolor_sign);

                                    }
                                    if(selectedOrderOnCart.getProduct().getCurrencyType()==2) {
                                        currencyType="GBP";
                                        type=context.getString(R.string.gbp);

                                    }
                                    if(selectedOrderOnCart.getProduct().getCurrencyType()==3) {
                                        currencyType="EUR";
                                        type=context.getString(R.string.eur);

                                    }*/
                                    priceAfterDiscount.setText( pad + type);

                                /*CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(SalesCartActivity.this);
                                currencyDBAdapter.open();
                                Currency currency = currencyDBAdapter.getCurrencyByCode(currencyType);*/
                                    totalPrice.setText(selectedOrderOnCart.getPaidAmount() + type);
                                    totalDiscount.setText(selectedOrderOnCart.getDiscount()*selectedOrderOnCart.getQuantity() + type);
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
                                            totalDiscount.setText(Util.makePrice((itemOriginalPrice - selectedOrderOnCart.getPaidAmount())) + type);
                                            priceAfterDiscount.setText(selectedOrderOnCart.getPaidAmount()+ type);
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
                                                        totalDiscount.setText(Util.makePrice(itemOriginalPrice * selectedOrderOnCart.getDiscount()/100) + type);
                                                        priceAfterDiscount.setText(itemOriginalPrice*(1-(selectedOrderOnCart.getDiscount())/100) + type);
                                                    } else {
                                                        Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                                        cashETCash.setBackgroundResource(R.drawable.backtext);
                                                    }

                                                } else {
                                                    totalDiscount.setText(Util.makePrice(itemOriginalPrice * selectedOrderOnCart.getDiscount()/100) + type);
                                                    priceAfterDiscount.setText(itemOriginalPrice*(1-(selectedOrderOnCart.getDiscount())/100) + type);
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
                                                        totalDiscount.setText(Util.makePrice(itemOriginalPrice * selectedOrderOnCart.getDiscount()/100) + type);
                                                        priceAfterDiscount.setText(itemOriginalPrice*(1-(selectedOrderOnCart.getDiscount())/100) + type);
                                                    } else {
                                                        Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                                        cashETCash.setBackgroundResource(R.drawable.backtext);
                                                    }


                                                } else {
                                                    totalDiscount.setText(Util.makePrice(itemOriginalPrice * selectedOrderOnCart.getDiscount()/100) + type);
                                                    priceAfterDiscount.setText(itemOriginalPrice*(1-(selectedOrderOnCart.getDiscount())/100) + type);
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

                    }
                });
            }
        });


        lvOrder.setAdapter(saleDetailsListViewAdapter);


        //endregion

        //region Payment

        //region Cash

        /**   btnCash.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        String s =(tvTotalSaved.getText().toString());

        if (s != null && s.length() > 0 && s.charAt(s.length() - 1) == '₪') {
        s = s.substring(0, s.length() - 1);
        }
        if (SESSION._ORDER_DETAILES.size() > 0) {
        if (SETTINGS.enableCurrencies) {
        //Intent intent = new Intent(SalesCartActivity.this, CashActivity.class);
        SESSION._ORDERS.totalSaved=Double.parseDouble(s);
        Log.d("testTotalSaved",s+"");
        Intent intent = new Intent(SalesCartActivity.this, MultiCurrenciesPaymentActivity.class);
        intent.putExtra(COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE, Double.parseDouble(Util.makePrice(saleTotalPrice)));
        startActivityForResult(intent, REQUEST_MULTI_CURRENCY_ACTIVITY_CODE);
        } else {
        SESSION._ORDERS.totalSaved=Double.parseDouble(s);
        Log.d("testTotalSaved",s+"");
        Intent intent = new Intent(SalesCartActivity.this, OldCashActivity.class);
        intent.putExtra(COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE, Double.parseDouble(Util.makePrice(saleTotalPrice)));
        startActivityForResult(intent, REQUEST_CASH_ACTIVITY_CODE);
        }
        }
        }
        });**/
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s =(tvTotalSaved.getText().toString());
                char c,c1,c2;
                 if (SETTINGS.currencySymbol.length()==1){
                c=SETTINGS.currencySymbol.charAt(0);
                     Log.d("cccc",c+"");
                     if (s != null && s.length() > 0 && s.charAt(s.length() - 1) == c) {
                         s = s.substring(0, s.length() - 1);
                     }}
                else if (SETTINGS.currencySymbol.length()==2){
                     c=SETTINGS.currencySymbol.charAt(0);
                     c1=SETTINGS.currencySymbol.charAt(1);
                     Log.d("cccc",c+"");
                     Log.d("cccc",c1+"");
                     if (s != null && s.length() > 0 && s.charAt(s.length() - 1) == c1) {
                         s = s.substring(0, s.length() - 1);
                         if (s != null && s.length() > 0 && s.charAt(s.length() - 1) == c){
                             s = s.substring(0, s.length() - 1);
                         }
                         Log.d("s",s+"");
                     }}

                     else if (SETTINGS.currencySymbol.length()==3){
                         c=SETTINGS.currencySymbol.charAt(0);
                         c1=SETTINGS.currencySymbol.charAt(1);
                         c2= c=SETTINGS.currencySymbol.charAt(2);
                         Log.d("cccc",c+"");
                         Log.d("cccc",c1+"");
                         if (s != null && s.length() > 0 && s.charAt(s.length() - 1) == c2) {
                             s = s.substring(0, s.length() - 1);
                             if (s != null && s.length() > 0 && s.charAt(s.length() - 1) == c1){
                                 s = s.substring(0, s.length() - 1);
                                 if (s != null && s.length() > 0 && s.charAt(s.length() - 1) == c){
                                     s = s.substring(0, s.length() - 1);
                                 }
                             }
                             Log.d("s",s+"");
                     }}




                Log.d("SSSs",s);
                if (SESSION._ORDER_DETAILES.size() > 0) {
                    //Intent intent = new Intent(SalesCartActivity.this, CashActivity.class);
                   SESSION._ORDERS.setTotalSaved(Double.parseDouble(s));
                    Log.d("testTotalSaved",s+"");
                    Intent intent = new Intent(SalesCartActivity.this, MultiCurrenciesPaymentActivity.class);
                    intent.putExtra(COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE, Double.parseDouble(Util.makePrice(saleTotalPrice)));
                    startActivityForResult(intent, REQUEST_MULTI_CURRENCY_ACTIVITY_CODE);

                }
            }
        });


        //endregion


        //region Credit Card

       /* btnCreditCard.setOnClickListener(new View.OnClickListener() {
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
        });*/

        //endregion


        //region Other Way

    /*    btnOtherWays.setOnClickListener(new View.OnClickListener() {
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
        });*/

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
                    s.setCartDiscount(SESSION._ORDERS.getCartDiscount());
                    if (SESSION._SALES == null)
                        SESSION._SALES = new ArrayList<Pair<Integer, Order>>();
                    else if (SESSION._SALES.size() == 0)
                        SESSION.TEMP_NUMBER = 0;

                    SESSION._SALES.add(new Pair<>(++SESSION.TEMP_NUMBER, s));
                    Util.printPauseInvoice(SalesCartActivity.this,SESSION._ORDER_DETAILES,SESSION._ORDERS.cartDiscount);
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
                final String[] items;
                if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
                 items = new String[]{
                         getString(R.string.invoice_company_status),
                         getString(R.string.receipt), getString(R.string.create_order_document), getString(R.string.view_order_document), getString(R.string.credit_invoice_doc_company_status), getString(R.string.view_credit_invoice_doc_company_status)

                 };}
                else {
                    items = new String[]{
                            getString(R.string.invoice),
                            getString(R.string.receipt), getString(R.string.create_order_document), getString(R.string.view_order_document), getString(R.string.create_credit_invoice_doc), getString(R.string.view_credit_invoice_doc)

                    };
                }
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
                                                    HashMap<String,Integer>productHashMap=new HashMap<String, Integer>();
                                                    if(invoiceJsonObject.getString("status").equals("200")) {
                                                        Log.d("testOrder",SESSION._ORDER_DETAILES.toString());
                                                        String s =(tvTotalSaved.getText().toString());

                                                        if (s != null && s.length() > 0 && s.charAt(s.length() - 1) == SETTINGS.currencySymbol.charAt(0)) {
                                                            s = s.substring(0, s.length() - 1);
                                                        }
                                                        SESSION._ORDERS.setTotalSaved(Double.parseDouble(s));
                                                        Log.d("invoiceSessionOrderDetial",SESSION._ORDER_DETAILES.toString());
                                                        Log.d("invoiceSessionOrder",SESSION._ORDERS.toString());
                                                        print(invoiceImg.Invoice(SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, invoiceNum, customerGeneralLedger));
                                                        clearCart();

                                                    }else if(invoiceJsonObject.getString("message").equals("Maximum allowed credit amount is 2000.0")){
                                                        Toast.makeText(SalesCartActivity.this,"Maximum allowed credit amount is 2000.0",Toast.LENGTH_LONG).show();
                                                    }
                                                    else if(invoiceJsonObject.getString("message").equals("Customer  not found with id :")){
                                                        Toast.makeText(SalesCartActivity.this,"Customer  not found",Toast.LENGTH_LONG).show();
                                                    }
                                                    else {
                                                        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
                                                            new android.support.v7.app.AlertDialog.Builder(SalesCartActivity.this)
                                                                    .setTitle(getString(R.string.invoice_company_status))
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
                                                double SalesWitheTax=0,SalesWithoutTax=0,salesaftertax=0;
                                                for (int i=0;i<SESSION._ORDER_DETAILES.size();i++){
                                               //  if (SESSION._ORDER_DETAILES.get(i).getProduct().getCurrencyType()==0){
//                                                        double rateCurrency = ConverterCurrency.getRateCurrency("ILS",SalesCartActivity.this);
                                                    if (SESSION._ORDER_DETAILES.get(i).getProduct().getCurrencyType().equals("0")){
                                                        updateCurrencyType.updateCurrencyToShekl(SalesCartActivity.this,SESSION._ORDER_DETAILES.get(i).getProduct());
                                                    }

                                                    double rateCurrency = ConverterCurrency.getRateCurrency(SESSION._ORDER_DETAILES.get(i).getProduct().getCurrencyType(),SalesCartActivity.this);
                                                        if(!SESSION._ORDER_DETAILES.get(i).getProduct().isWithTax()){
                                                            if(SESSION._ORDERS.getCartDiscount()>0){
                                                                SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100))))));
                                                                SalesWithoutTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                            }else {
                                                                SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(SESSION._ORDER_DETAILES.get(i).getPaidAmount())));
                                                                SalesWithoutTax += (SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                            }
                                                        }else {
                                                            if(SESSION._ORDERS.getCartDiscount()>0){

                                                                SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100)))/ (1 + (SETTINGS.tax / 100)))));
                                                                Log.d("salesaftertax", SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()+"ko2333"+SESSION._ORDER_DETAILES.get(i).getPaidAmount()+"ko2333"+(SESSION._ORDERS.getCartDiscount()/100));
                                                                salesaftertax+=((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100)))*rateCurrency);
                                                                SalesWitheTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                            }else {
                                                                SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(SESSION._ORDER_DETAILES.get(i).getPaidAmount() / (1 + (SETTINGS.tax / 100)))));
                                                                salesaftertax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*rateCurrency);
                                                                SalesWitheTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                            }
                                                        }
                                                   // }
/*
                                                    if (SESSION._ORDER_DETAILES.get(i).getProduct().getCurrencyType()==1){
                                                        double rateCurrency =ConverterCurrency.getRateCurrency("USD",SalesCartActivity.this);
                                                        if(SESSION._ORDER_DETAILES.get(i).getProduct().isWithTax()){
                                                            if(SESSION._ORDERS.getCartDiscount()>0){
                                                                SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100))))));
                                                                SalesWithoutTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                            }else {
                                                                SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(SESSION._ORDER_DETAILES.get(i).getPaidAmount())));
                                                                SalesWithoutTax += (SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                            }
                                                        }else {
                                                            if(SESSION._ORDERS.getCartDiscount()>0){

                                                                SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100)))/ (1 + (SETTINGS.tax / 100)))));
                                                                Log.d("salesaftertax", SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()+"ko2333"+SESSION._ORDER_DETAILES.get(i).getPaidAmount()+"ko2333"+(SESSION._ORDERS.getCartDiscount()/100));
                                                                salesaftertax+=((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100)))*rateCurrency);
                                                                SalesWitheTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                            }else {
                                                                SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(SESSION._ORDER_DETAILES.get(i).getPaidAmount() / (1 + (SETTINGS.tax / 100)))));
                                                                salesaftertax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*rateCurrency);
                                                                SalesWitheTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                            }
                                                        }
                                                    }



                                                    else     if (SESSION._ORDER_DETAILES.get(i).getProduct().getCurrencyType()==2){
                                                        double rateCurrency =ConverterCurrency.getRateCurrency("GBP",SalesCartActivity.this);
                                                        if(SESSION._ORDER_DETAILES.get(i).getProduct().isWithTax()){
                                                            if(SESSION._ORDERS.getCartDiscount()>0){
                                                                SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100))))));
                                                                SalesWithoutTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                            }else {
                                                                SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(SESSION._ORDER_DETAILES.get(i).getPaidAmount())));
                                                                SalesWithoutTax += (SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                            }
                                                        }else {
                                                            if(SESSION._ORDERS.getCartDiscount()>0){

                                                                SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100)))/ (1 + (SETTINGS.tax / 100)))));
                                                                Log.d("salesaftertax", SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()+"ko2333"+SESSION._ORDER_DETAILES.get(i).getPaidAmount()+"ko2333"+(SESSION._ORDERS.getCartDiscount()/100));
                                                                salesaftertax+=((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100)))*rateCurrency);
                                                                SalesWitheTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                            }else {
                                                                SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(SESSION._ORDER_DETAILES.get(i).getPaidAmount() / (1 + (SETTINGS.tax / 100)))));
                                                                salesaftertax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*rateCurrency);
                                                                SalesWitheTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                            }
                                                        }
                                                    }




                                                    else    if (SESSION._ORDER_DETAILES.get(i).getProduct().getCurrencyType()==3){
                                                        double rateCurrency =ConverterCurrency.getRateCurrency("EUR",SalesCartActivity.this);
                                                        if(SESSION._ORDER_DETAILES.get(i).getProduct().isWithTax()){
                                                            if(SESSION._ORDERS.getCartDiscount()>0){
                                                                SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100))))));
                                                                SalesWithoutTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                            }else {
                                                                SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(SESSION._ORDER_DETAILES.get(i).getPaidAmount())));
                                                                SalesWithoutTax += (SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                            }
                                                        }else {
                                                            if(SESSION._ORDERS.getCartDiscount()>0){

                                                                SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100)))/ (1 + (SETTINGS.tax / 100)))));
                                                                Log.d("salesaftertax", SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()+"ko2333"+SESSION._ORDER_DETAILES.get(i).getPaidAmount()+"ko2333"+(SESSION._ORDERS.getCartDiscount()/100));
                                                                salesaftertax+=((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100)))*rateCurrency);
                                                                SalesWitheTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                            }else {
                                                                SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(SESSION._ORDER_DETAILES.get(i).getPaidAmount() / (1 + (SETTINGS.tax / 100)))));
                                                                salesaftertax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*rateCurrency);
                                                                SalesWitheTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                                            }
                                                        }
                                                    }*/



                                                }

                                                try {
                                                    for(OrderDetails orderDetails:SESSION._ORDER_DETAILES){
                                                        orderDetails.setPaidAmount(0.0);
                                                    }
                                                    ObjectMapper mapper = new ObjectMapper();
                                                    customerData.put("customerId", SESSION._ORDERS.getCustomerId());
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
                                                        ZReportCountDbAdapter zReportCountDbAdapter = new ZReportCountDbAdapter(SalesCartActivity.this);
                                                        zReportCountDbAdapter.open();
                                                        ZReport zReport =null;
                                                        ZReportCount zReportCount =null;

                                                        try {
                                                            zReportCount=zReportCountDbAdapter.getLastRow();
                                                            zReport = zReportDBAdapter.getLastRow();
                                                            Log.d("zReport",zReport.toString());
                                                            PosInvoiceDBAdapter posInvoiceDBAdapter = new PosInvoiceDBAdapter(SalesCartActivity.this);
                                                            posInvoiceDBAdapter.open();
                                                            posInvoiceDBAdapter.insertEntry(SESSION._ORDERS.getTotalPrice(),zReport.getzReportId(),DocumentType.INVOICE.getValue(),InvoiceStatus.UNPAID.getValue(),invoiceNum, CONSTANT.CASH);
                                                            zReport.setInvoiceAmount(zReport.getInvoiceAmount()+SESSION._ORDERS.getTotalPrice());
                                                            zReport.setTotalSales(zReport.getTotalSales()+SESSION._ORDERS.getTotalPrice());
                                                            zReport.setTotalPosSales(zReport.getTotalPosSales()+SESSION._ORDERS.getTotalPrice());
                                                            zReport.setSalesBeforeTax(Double.parseDouble(Util.makePrice(zReport.getSalesBeforeTax() + (SalesWithoutTax))));
                                                            zReport.setSalesWithTax(Double.parseDouble(Util.makePrice(zReport.getSalesWithTax() + (SalesWitheTax))));
                                                            Log.d("setSalesWithTaxReport",zReport.getSalesWithTax()+"");
                                                            zReport.setTotalTax(Double.parseDouble(Util.makePrice(zReport.getTotalTax()+(salesaftertax - SalesWitheTax))));

                                                            Log.d("getTotalPos",zReport.getTotalPosSales()+"poss");
                                                            zReportCount.setInvoiceCount(zReportCount.getInvoiceCount()+1);

                                                            zReportDBAdapter.updateEntry(zReport);
                                                            zReportCountDbAdapter.updateEntry(zReportCount);

                                                        } catch (Exception e) {
                                                            PosInvoiceDBAdapter posInvoiceDBAdapter = new PosInvoiceDBAdapter(SalesCartActivity.this);
                                                            posInvoiceDBAdapter.open();
                                                            posInvoiceDBAdapter.insertEntry(SESSION._ORDERS.getTotalPrice(),-1,DocumentType.INVOICE.getValue(),InvoiceStatus.UNPAID.getValue(),invoiceNum, CONSTANT.CASH);
                                                            zReport.setInvoiceAmount(zReport.getInvoiceAmount()+SESSION._ORDERS.getTotalPrice());
                                                            zReport.setTotalSales(zReport.getTotalSales()+SESSION._ORDERS.getTotalPrice());
                                                            zReport.setTotalPosSales(zReport.getTotalPosSales()+SESSION._ORDERS.getTotalPrice());
                                                            zReport.setSalesBeforeTax(Double.parseDouble(Util.makePrice(zReport.getSalesBeforeTax() + (SalesWithoutTax))));
                                                            zReport.setSalesWithTax(Double.parseDouble(Util.makePrice(zReport.getSalesWithTax() + (SalesWitheTax))));
                                                            zReport.setTotalTax(Double.parseDouble(Util.makePrice(zReport.getTotalTax()+(salesaftertax - SalesWitheTax))));
                                                            zReportCount.setInvoiceCount(zReportCount.getInvoiceCount()+1);
                                                            zReportDBAdapter.updateEntry(zReport);
                                                            zReportCountDbAdapter.updateEntry(zReportCount);
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
                                                        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")) {
                                                            new android.support.v7.app.AlertDialog.Builder(SalesCartActivity.this)
                                                                    .setTitle(getString(R.string.invoice_company_status))
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
                if (SESSION._ORDERS != null && SESSION._ORDER_DETAILES != null&&SESSION._ORDER_DETAILES.size()>0) {
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
                        if (o.getProduct().getCurrencyType().equals("0")){
                            updateCurrencyType.updateCurrencyToShekl(SalesCartActivity.this,o.getProduct());
                        }
                        String currencyType=o.getProduct().getCurrencyType();
                       /* if(o.getProduct().getCurrencyType()==0) {
                            currencyType="ILS";
                        }
                        if(o.getProduct().getCurrencyType()==1) {
                            currencyType="USD";
                        }
                        if(o.getProduct().getCurrencyType()==2) {
                            currencyType="GBP";
                        }
                        if(o.getProduct().getCurrencyType()==3) {
                            currencyType="EUR";
                        }*/
                        CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(SalesCartActivity.this);
                        currencyDBAdapter.open();
                        Currency currency = currencyDBAdapter.getCurrencyByCode(currencyType);
                        currencyDBAdapter.close();
                        originalTotalPrice += (o.getUnitPrice() * o.getQuantity()*currency.getRate());
                        if(o.getDiscount()>0) {
                            discountAmount += o.getUnitPrice() * o.getQuantity()- o.getUnitPrice() * o.getQuantity() * o.getDiscount()/100;

                        }else {
                            discountAmount += o.getUnitPrice() * o.getQuantity();

                        }

                    }
                    discountAmount = discountAmount - (discountAmount * (SESSION._ORDERS.cartDiscount / 100));

                    totalPrice.setText(originalTotalPrice + SETTINGS.currencySymbol);

                    totalDiscount.setText(Util.makePrice(originalTotalPrice-discountAmount) + SETTINGS.currencySymbol);
                    priceAfterDiscount.setText(tvTotalPrice.getText().toString());
                    sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (sw.isChecked()) {
                                totalDiscount.setText(Util.makePrice(valueOfDiscount) + SETTINGS.currencySymbol);
                                priceAfterDiscount.setText(tvTotalPrice.getText().toString());
                                sw.setText(R.string.amount);
                                et.setText("");
                                et.setHint("0");
                            } else {
                                totalDiscount.setText(Util.makePrice(valueOfDiscount) + SETTINGS.currencySymbol);
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
                                          //  o.setDiscount(val);
                                        }
                                        double saleTotalPrice = 0;
                                        double SaleOriginalityPrice = 0;
                                        for (OrderDetails o : orderList) {
                                            saleTotalPrice += o.getItemTotalPrice();

                                            SaleOriginalityPrice += (o.getUnitPrice() * o.getQuantity())*(1-o.getDiscount()/100);
                                        }
                                        totalDiscount.setText(Util.makePrice(originalTotalPrice*val/100 ) + SETTINGS.currencySymbol);
                                        priceAfterDiscount.setText(originalTotalPrice*(1-val/100) + SETTINGS.currencySymbol);
                                    } else {
                                        totalDiscount.setText(Util.makePrice(valueOfDiscount) + SETTINGS.currencySymbol);
                                        priceAfterDiscount.setText(tvTotalPrice.getText().toString());
                                        Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                        et.setBackgroundResource(R.drawable.backtext);
                                    }

                                } else {
                                    totalDiscount.setText(Util.makePrice(valueOfDiscount) + SETTINGS.currencySymbol);
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
                                         //   o.setDiscount(o.getDiscount()+val);
                                        }
                                        double saleTotalPrice = 0;
                                        double SaleOriginalityPrice = 0;
                                        for (OrderDetails o : orderList) {
                                            saleTotalPrice += o.getItemTotalPrice();

                                            SaleOriginalityPrice += (o.getUnitPrice() * o.getQuantity())*(1-o.getDiscount()/100);
                                        }
                                        totalDiscount.setText(Util.makePrice(SaleOriginalityPrice*val/100 ) + SETTINGS.currencySymbol);
                                        priceAfterDiscount.setText(SaleOriginalityPrice*(1-val/100) + SETTINGS.currencySymbol);

                                    } else {
                                        totalDiscount.setText(Util.makePrice(valueOfDiscount) + SETTINGS.currencySymbol);
                                        priceAfterDiscount.setText(tvTotalPrice.getText().toString());
                                        Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                        et.setBackgroundResource(R.drawable.backtext);
                                    }
                                } else {
                                    totalDiscount.setText(Util.makePrice(valueOfDiscount) + SETTINGS.currencySymbol);
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
                                        for (OrderDetails o : SESSION._ORDER_DETAILES) {
                                         //   o.setDiscount(val);
                                        }
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
        super.onResume();
        refreshCart();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("orderJson")) {
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
                        customerDBAdapter.open();
                        Customer customer = customerDBAdapter.getCustomerByID(Long.parseLong(orderDocJsonObj.getJSONObject("documentsData").getJSONObject("customer").getString("customerId")));
                        order.setCustomer(customer);
                        SESSION._ORDERS=order;
                        Log.d("iiitems",items.toString());
                        for (int i=0;i<items.length();i++){
                            Product p = null;
                            JSONObject orderDetailsJson =items.getJSONObject(i);
                            if(Long.parseLong(orderDetailsJson.getString("productId"))==-1){
                                p=new Product(Long.parseLong(String.valueOf(-1)),"General","General",orderDetailsJson.getDouble("unitPrice"),"0","0",Long.parseLong(String.valueOf(1)),Long.parseLong(String.valueOf(1)));
                            }else {
                                productDBAdapter.open();
                                p = productDBAdapter.getProductByID(Long.parseLong(orderDetailsJson.getString("productId")));
                                productDBAdapter.close();
                            }
                            OrderDetails orderDetails= new OrderDetails(orderDetailsJson.getInt("quantity"),orderDetailsJson.getDouble("userOffer"),p,orderDetailsJson.getDouble("amount"),orderDetailsJson.getDouble("unitPrice"),orderDetailsJson.getDouble("discount"));
                            SESSION._ORDER_DETAILES.add(orderDetails);
                        }
                        saleDetailsListViewAdapter = new SaleDetailsListViewAdapter(getApplicationContext(), R.layout.list_adapter_row_main_screen_sales_details, SESSION._ORDER_DETAILES);
                        lvOrder.setAdapter(saleDetailsListViewAdapter);
                        if (SESSION._ORDERS.getCustomer() != null)
                            setCustomer(SESSION._ORDERS.getCustomer());
                        refreshCart();
                        getIntent().removeExtra("orderJson");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }}

        /* else  if (extras.containsKey("FROM_ACTIVITY")) {

             if (!extras.get("FROM_ACTIVITY").equals("")) {
                 SESSION._Rest();
                 clearCart();
             }
             //str = extras.getString("orderJson");
         }*/
        customerDBAdapter.close();
        }else {

          if(CurrencyReturnsCustomDialogActivity.REQUEST_CURRENCY_RETURN_ACTIVITY_CODE){
                //Log.d("REQUEST_CURRENCY_RETURN_ACTIVITY_CODE",CurrencyReturnsCustomDialogActivity.REQUEST_CURRENCY_RETURN_ACTIVITY_CODE+"");
                CurrencyReturnsCustomDialogActivity.REQUEST_CURRENCY_RETURN_ACTIVITY_CODE=false;
             SESSION._Rest();
             clearCart();
            }
        }}

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
                Log.d("session",SESSION._SALES.get(position).second.toString());
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
            addToCart(new Product(-1, getApplicationContext().getResources().getString(R.string.general), getApplicationContext().getResources().getString(R.string.general), Double.parseDouble(str), SESSION._EMPLOYEE.getEmployeeId(), "", "",ProductUnit.BARCODEWITHPRICE));
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
    boolean isGeneralPriceProduct=true;

    public void touchPadClick(View view) throws JSONException {
        TextView tirh = (TextView) this.findViewById(R.id.touchPadFragment_tvView);
        int count=0;
        ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(SalesCartActivity.this);
        zReportDBAdapter.open();
        ZReportCountDbAdapter zReportCountDbAdapter = new ZReportCountDbAdapter(SalesCartActivity.this);
        zReportCountDbAdapter.open();
        ZReportCount zReportCount=null;
        ZReport zReport1=null;
        try {
            zReportCount = zReportCountDbAdapter.getLastRow();
            zReport1 = zReportDBAdapter.getLastRow();

        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (view.getId()) {
            case R.id.touchPadFragment_bt0:
                touchPadPressed += 0;
                tirh.setText(touchPadPressed);
                break;
            case R.id.touchPadFragment_bt1:
                touchPadPressed += 1;
                tirh.setText(touchPadPressed);
                break;
            case R.id.touchPadFragment_bt2:
                touchPadPressed += 2;
                tirh.setText(touchPadPressed);
                break;
            case R.id.touchPadFragment_bt3:
                touchPadPressed += 3;
                tirh.setText(touchPadPressed);
                break;
            case R.id.touchPadFragment_bt4:
                touchPadPressed += 4;
                tirh.setText(touchPadPressed);
                break;
            case R.id.touchPadFragment_bt5:
                touchPadPressed += 5;
                tirh.setText(touchPadPressed);
                break;
            case R.id.touchPadFragment_bt6:
                touchPadPressed += 6;
                tirh.setText(touchPadPressed);
                break;
            case R.id.touchPadFragment_bt7:
                touchPadPressed += 7;
                tirh.setText(touchPadPressed);
                break;
            case R.id.touchPadFragment_bt8:
                touchPadPressed += 8;
                tirh.setText(touchPadPressed);
                break;
            case R.id.touchPadFragment_bt9:
                touchPadPressed += 9;
                tirh.setText(touchPadPressed);
                break;
            case R.id.touchPadFragment_btCE:
                if (!touchPadPressed.equals(""))
                    touchPadPressed = Util.removeLastChar(touchPadPressed);
                removeOrderItemSelection();
                refreshCart();
                tirh.setText(touchPadPressed);
                break;
            case R.id.touchPadFragment_btEnter:

                if (!touchPadPressed.equals("")){
                        addToCart(new Product(-1, getApplicationContext().getResources().getString(R.string.general), getApplicationContext().getResources().getString(R.string.general), Double.parseDouble(touchPadPressed), SESSION._EMPLOYEE.getEmployeeId(), "", "",ProductUnit.BARCODEWITHPRICE));

                }

                touchPadPressed = "";
                tirh.setText(touchPadPressed);
                break;

            case R.id.touchPadFragment_btGeneralPriceProduct:

                if (!touchPadPressed.equals("")){
                    if(SESSION._ORDER_DETAILES.size()>0&&SESSION._ORDER_DETAILES.get(SESSION._ORDER_DETAILES.size()-1).getProduct().getUnit().getValue().equalsIgnoreCase(ProductUnit.GENERALPRICEPRODUCT.getValue())){
                        SESSION._ORDER_DETAILES.get(SESSION._ORDER_DETAILES.size()-1).setUnitPrice(Double.parseDouble(touchPadPressed));
                        calculateTotalPrice();
                    }
                }

                touchPadPressed = "";
                tirh.setText(touchPadPressed);
                break;
            case R.id.touchPadFragment_btDot:
                if (touchPadPressed.indexOf(".") < 0)
                    touchPadPressed += ".";
                tirh.setText(touchPadPressed);
                break;
            case R.id.touchPadFragment_btCredit:
                if(SESSION._EMPLOYEE.getEmployeeId()!=2) {
                    Toast.makeText(this, "This Operation just for master employee !!", Toast.LENGTH_LONG).show();

                }else {
                    if (!touchPadPressed.equals("")) {
                        double newValue = Util.convertSign(Double.parseDouble(touchPadPressed));
                        touchPadPressed = String.valueOf(newValue);
                      //  Toast.makeText(this, touchPadPressed+"jojo", Toast.LENGTH_LONG).show();
                        tirh.setText(touchPadPressed);
                    }
                }
                break;
        }


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
        // SESSION._ORDERS.cartDiscount = 0;
        // SESSION._ORDERS.setCartDiscount(0);
        SESSION._Rest();
        customerName_EditText.setText("");
        Log.d("SESSION._ORDER_DETAILES", SESSION._ORDER_DETAILES.toString());
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
        SESSION._ORDERS.setCartDiscount(s.getCartDiscount());
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
                tvTotalSaved.setText(String.format(new Locale("en"), "%.2f", (totalSaved)) + " " + SETTINGS.currencySymbol);
                tvTotalPrice.setText(String.format(new Locale("en"), "%.2f", saleTotalPrice) + " " + SETTINGS.currencySymbol);
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
            tvTotalSaved.setText(String.format(new Locale("en"), "%.2f", (totalSaved)) + " " + SETTINGS.currencySymbol);
            tvTotalPrice.setText(String.format(new Locale("en"), "%.2f", saleTotalPrice) + " " + SETTINGS.currencySymbol);
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
        tvTotalSaved.setText(String.format(new Locale("en"), "%.2f", (totalSaved)) + " " + SETTINGS.currencySymbol);
        tvTotalPrice.setText(String.format(new Locale("en"), "%.2f", saleTotalPrice) + " " + SETTINGS.currencySymbol);
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
        Log.d("tvTotalPrice",tvTotalPrice.getText().toString());
       // String b = a.replace(SETTINGS.currencySymbol, "");
        double SaleOriginalityPrice = 0;


        saleTotalPrice = 0;
        for (OrderDetails o : SESSION._ORDER_DETAILES) {
            if(o.giftProduct) continue;
            if(!o.scannable) continue;
      try {
                if (o== null){
                }
                else {
                    calculateOfferForOrderDetails(o);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (o.getOffer() != null) {
                Log.i("hasOffer", "" + o.getOffer() + "" );
            }

                if (o.offerList!=null && !o.offerList.isEmpty()){
                    for (Offer offer : o.offerList) {
                        if(!offers.containsKey(offer.getOfferId())){
                            offers.put(offer.getOfferId(), offer);
                        }
                    }}

        }
        double price_before_tax=0;

        for (OrderDetails o : SESSION._ORDER_DETAILES) {
            if(o.getProduct()==null) {
                clearCart();
            }else {
                if (o.getProduct().getCurrencyType().equals("0")){
                    o.getProduct().setCurrencyType("ILS");
                    updateCurrencyType.updateCurrencyToShekl(SalesCartActivity.context,o.getProduct());
                }
                String currencyType=o.getProduct().getCurrencyType();
                /*if (o.getProduct().getCurrencyType() == 0) {
                    currencyType = "ILS";
                }
                if (o.getProduct().getCurrencyType() == 1) {
                    currencyType = "USD";
                }
                if (o.getProduct().getCurrencyType() == 2) {
                    currencyType = "GBP";
                }
                if (o.getProduct().getCurrencyType() == 3) {
                    currencyType = "EUR";
                }*/
                CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(SalesCartActivity.this);
                currencyDBAdapter.open();
                Currency currency = currencyDBAdapter.getCurrencyByCode(currencyType);
                currencyDBAdapter.close();

                    saleTotalPrice += o.getItemTotalPrice() * currency.getRate();

                    Log.d("saleTotalPrice", String.valueOf(saleTotalPrice));
                    SaleOriginalityPrice += (o.getUnitPrice() * o.getQuantity() * currency.getRate());
                    Log.d("SaleOriginalityPrice", String.valueOf(SaleOriginalityPrice));
            }
        }


        if (SESSION._ORDERS!=null&&SESSION._ORDERS.cartDiscount != 0&& SESSION._ORDER_DETAILES.size()>0) {
            //show the discount view
            llCartDiscount.setVisibility(View.VISIBLE);
            tvTotalPriceBeforeCartDiscount.setVisibility(View.VISIBLE);

            tvTotalPriceBeforeCartDiscount.setText(Util.makePrice(saleTotalPrice) + " " +SETTINGS.currencySymbol);
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

        double SalesWitheTax=0,SalesWithoutTax=0,salesaftertax=0;
        for (int i=0;i<SESSION._ORDER_DETAILES.size();i++){
            if (SESSION._ORDER_DETAILES.get(i).getProduct().getCurrencyType().equals("0")){
                updateCurrencyType.updateCurrencyToShekl(SalesCartActivity.context,SESSION._ORDER_DETAILES.get(i).getProduct());
            }
            //       if (SESSION._ORDER_DETAILES.get(i).getProduct().getCurrencyType()==0){
            //   double rateCurrency= ConverterCurrency.getRateCurrency("ILS",SalesCartActivity.this);
            double rateCurrency= ConverterCurrency.getRateCurrency(SESSION._ORDER_DETAILES.get(i).getProduct().getCurrencyType(),SalesCartActivity.this);
            if(!SESSION._ORDER_DETAILES.get(i).getProduct().isWithTax()){
                if(SESSION._ORDERS.getCartDiscount()>0){
                    SalesWithoutTax+=((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100)))*rateCurrency);
                }else {
                    SalesWithoutTax +=( SESSION._ORDER_DETAILES.get(i).getPaidAmount()*rateCurrency);
                    Log.d("tesddt","    "+SalesWithoutTax);

                }
            }else {
                if(SESSION._ORDERS.getCartDiscount()>0){
                    Log.d("salesaftertax", SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()+"ko2333"+SESSION._ORDER_DETAILES.get(i).getPaidAmount()+"ko2333"+(SESSION._ORDERS.getCartDiscount()/100));

                    salesaftertax+=((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100)))*rateCurrency);
                    SalesWitheTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100)))/ (1 + (SETTINGS.tax / 100))*rateCurrency;
                }else {
                    salesaftertax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*rateCurrency);
                    SalesWitheTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmount() / (1 + (SETTINGS.tax / 100))*rateCurrency);
                }
            }
        }
        double totalPriceAfterDiscount= saleTotalPrice- (saleTotalPrice * (SESSION._ORDERS.cartDiscount/100));
        Vat.setText(Util.makePrice(totalPriceAfterDiscount - SalesWitheTax));
        totalSaved = (SaleOriginalityPrice - saleTotalPrice);
        tvTotalSaved.setText(String.format(new Locale("en"), "%.2f", (totalSaved)) + " " +SETTINGS.currencySymbol);
        tvTotalPrice.setText(String.format(new Locale("en"), "%.2f", saleTotalPrice) + " " + SETTINGS.currencySymbol);
        SESSION._ORDERS.setNumberDiscount(Order.calculateNumberDiscount(saleTotalPrice,Double.parseDouble(Util.makePrice(saleTotalPrice))));
        SESSION._ORDERS.setTotalPrice(Double.parseDouble(Util.makePrice(saleTotalPrice)));
        Log.d("saleTotalPriceaftr",Util.makePrice(saleTotalPrice));
        Log.d("ORDER_DETAILEStest", SESSION._ORDER_DETAILES.toString());
        Log.d("ORDER_DETAILESize", String.valueOf(SESSION._ORDER_DETAILES.size()));
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
        Log.d("testOrderDe",p.toString());
        OrderDetails newOrderDetails = null;
        boolean isMatch = false;
        productDBAdapter.open();
        //test if cart have this order before insert to cart and order have'nt discount
        for (int i = 0; i < SESSION._ORDER_DETAILES.size(); i++) {
            OrderDetails o = SESSION._ORDER_DETAILES.get(i);
            Product p1=productDBAdapter.getProductByID(o.getProductId());
            o.setProduct(p1);
            Log.d("ORDER_DETAILS", o.toString());
            //Log.d("Product", p.toString());
            if(p.getProductId()!=-1){
                Log.d("ORDER_DETAILS1", o.toString());

                if(p.getUnit().equals(ProductUnit.QUANTITY)){
                    if (o.getProduct()!=null){
                    if(!o.getProduct().isWithSerialNumber()){
                        if(Long.valueOf(o.getProduct().getOfferId())==null){
                            Log.d("offerIdNull","addToCart");
                        }
                        else {
                        if (o.getProduct().equals(p) && o.getProduct().getProductId() != -1&&!o.giftProduct&&o.scannable&&o.getProduct().getOfferId()==0) {
                            SESSION._ORDER_DETAILES.get(i).setCount(SESSION._ORDER_DETAILES.get(i).getQuantity() + 1);
                            //getOfferCategoryForProduct
                            OfferCategoryDbAdapter offerCategoryDbAdapter = new OfferCategoryDbAdapter(SalesCartActivity.this);
                            offerCategoryDbAdapter.open();
                            List<OfferCategory>offerCategoryList=offerCategoryDbAdapter.getOfferCategoryByProductId(p.getProductId());
                            Log.d("offerCategoryListTest",offerCategoryList.toString());
                            offerCategoryDbAdapter.close();
                            if(offerCategoryList.size()>0){
                                SESSION._ORDER_DETAILES.get(i).setOfferCategory(offerCategoryList.get(offerCategoryList.size()-1).getOfferCategoryId());
                            }

                            newOrderDetails=SESSION._ORDER_DETAILES.get(i);
                            isMatch = true;
                            break;
                        }}
                    }}
                }

            }
        }
        productDBAdapter.close();
        if (!isMatch) {
            String currencyType=p.getCurrencyType();

          /*  if(p.getCurrencyType()==0) {
                currencyType="ILS";
            }
            if(p.getCurrencyType()==1) {
                currencyType="USD";
            }
            if(p.getCurrencyType()==2) {
                currencyType="GBP";
            }
            if(p.getCurrencyType()==3) {
                currencyType="EUR";
            }*/
         /*   CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(SalesCartActivity.this);
            currencyDBAdapter.open();*/
            final OrderDetails o = new OrderDetails(1, 0, p, p.getPriceWithTax() , p.getPriceWithTax(), 0);
            //getOfferCategoryForProduct
          /*  OfferCategoryDbAdapter offerCategoryDbAdapter = new OfferCategoryDbAdapter(SalesCartActivity.this);
            offerCategoryDbAdapter.open();
            List<OfferCategory>offerCategoryList=offerCategoryDbAdapter.getOfferCategoryByProductId(p.getProductId());
            if(offerCategoryList.size()>0){
                o.setOfferCategory(offerCategoryList.get(offerCategoryList.size()-1).getOfferCategoryId());

            }
            offerCategoryDbAdapter.close();
            Log.d("offerCategoryListTest1",offerCategoryList.toString());*/

            if(o.getProduct().isWithSerialNumber()){

                final Dialog productSerialNumberDialog = new Dialog(SalesCartActivity.this);
                productSerialNumberDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                productSerialNumberDialog.show();
                productSerialNumberDialog.setContentView(R.layout.product_serial_number);
                productSerialNumberDialog.show();
                final EditText serialNumber = (EditText) productSerialNumberDialog.findViewById(R.id.productSerialNumberDialog_EtSerialNumber);
                ImageView btn_cancel = (ImageView) productSerialNumberDialog.findViewById(R.id.closeDialog);
                Button btn_done = (Button) productSerialNumberDialog.findViewById(R.id.productSerialNumberDialog_BTOk);

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        productSerialNumberDialog.dismiss();
                    }
                });

                btn_done.setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
                        String productSerialNumber="";
                        if (serialNumber.getText().toString().matches("")) {
                            productSerialNumber="";
                        }else {
                            productSerialNumber=serialNumber.getText().toString();
                        }
                        o.setProductSerialNumber(0);
                        o.setSerialNumber(productSerialNumber);
                        productSerialNumberDialog.dismiss();
                        isWithSerialNo=false;
                    }
                });

            }
            if(o.getProduct().getUnit().equals(ProductUnit.WEIGHT)){
                PriceForWeight=o.getProduct().getPriceWithTax();
                final Dialog productWeightDialog = new Dialog(SalesCartActivity.this);
                productWeightDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                productWeightDialog.show();
                productWeightDialog.setContentView(R.layout.product_weight_dialog);
                productWeightDialog.show();
                final TextView productName = (TextView) productWeightDialog.findViewById(R.id.TvProductName);
                final TextView productPrice = (TextView) productWeightDialog.findViewById(R.id.TvPrice);
                final ImageView productWeighFromEditTextImg = (ImageView) productWeightDialog.findViewById(R.id.addWeightFromEditText);
                final EditText productWeightFromEditText = (EditText) productWeightDialog.findViewById(R.id.totalWeightFromEditText);

                productWeight = (TextView) productWeightDialog.findViewById(R.id.totalWeight);
                final TextView productDiscount = (TextView) productWeightDialog.findViewById(R.id.totalDiscount);
                totalPriceForBalance = (TextView) productWeightDialog.findViewById(R.id.totalPrice);
                ImageView btn_add_weight = (ImageView) productWeightDialog.findViewById(R.id.addWeight);
                ImageView btn_close = (ImageView) productWeightDialog.findViewById(R.id.closeDialog);
                Button btn_done = (Button) productWeightDialog.findViewById(R.id.productWeightDialog_BTOk);
                // Button btn_discount = (Button) productWeightDialog.findViewById(R.id.productWeightDialog_BTDiscount);


                devicesListAdapter = new DevicesListAdapter(this, R.layout.list_view_row_devices, devicesList);

                deviceHelper = new DeviceHelper(this);
                productName.setText(o.getProduct().getDisplayName());
                productPrice.setText(Util.makePrice(o.getProduct().getPriceWithTax()));
                refreshList();
                if(devicesList.size()>0) {
                    selectedDeviceIndex = 0;
                    selectedDevice = devicesList.get(0);

                    runTest(deviceHelper.getAvailableDrivers().get(0));
                }
             /*   btn_discount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SESSION._ORDER_DETAILES.size() > 0) {
                            selectedOrderOnCart=o;
                            if(selectedOrderOnCart.getUnitPrice()<=0) {
                                //todo show message cant implement discount on credit item
                                return;
                            }
                                if (SESSION._ORDER_DETAILES.contains(selectedOrderOnCart)) {
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
                                    double pad = Double.parseDouble(totalPriceForBalance.getText().toString());
                                    selectedOrderOnCart.rowDiscount = 0;
                                    selectedOrderOnCart.setDiscount(0);
                                    double itemOriginalPrice = selectedOrderOnCart.getPaidAmount();
                                    String currencyType="";
                                /*    if(selectedOrderOnCart.getProduct().getCurrencyType()==0) {
                                        currencyType="ILS";
                                        type=context.getString(R.string.ins);
                                    }
                                    if(selectedOrderOnCart.getProduct().getCurrencyType()==1) {
                                        currencyType="USD";
                                        type=context.getString(R.string.dolor_sign);
                                    }
                                    if(selectedOrderOnCart.getProduct().getCurrencyType()==2) {
                                        currencyType="GBP";
                                        type=context.getString(R.string.gbp);
                                    }
                                    if(selectedOrderOnCart.getProduct().getCurrencyType()==3) {
                                        currencyType="EUR";
                                        type=context.getString(R.string.eur);
                                    }*/
                                   /* priceAfterDiscount.setText( pad + type);
                                    CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(SalesCartActivity.this);
                                    currencyDBAdapter.open();
                                    Currency currency = currencyDBAdapter.getCurrencyByCode(currencyType);
                                    totalPrice.setText(totalPriceForBalance.getText().toString()+ type);
                                    totalDiscount.setText(selectedOrderOnCart.getDiscount()*selectedOrderOnCart.getQuantity() + type);
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
                                            totalDiscount.setText(Util.makePrice((itemOriginalPrice - selectedOrderOnCart.getPaidAmount())) + type);
                                            priceAfterDiscount.setText(totalPriceForBalance.getText().toString()+ type);
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
                                            double itemOriginalPrice = Double.parseDouble(totalPriceForBalance.getText().toString());
                                            double X = SESSION._EMPLOYEE.getPresent();
                                            if (sw.isChecked()) {
                                                discountT.setText(getString(R.string.price_after_discount));
                                                if (!(str.equals(""))) {
                                                    double d = Double.parseDouble(str);
                                                    double originalTotalPrice = 0;
                                                    double salePrice=0;
                                                    for (int i=0;i<SESSION._ORDER_DETAILES.size();i++) {
                                                        OrderDetails o =SESSION._ORDER_DETAILES.get(i);
                                                        salePrice+=(o.getProduct().getPriceWithTax() * o.getQuantity());
                                                        if(i!=SESSION._ORDER_DETAILES.indexOf(selectedOrderOnCart)){
                                                            originalTotalPrice += (Double.parseDouble(totalPriceForBalance.getText().toString())* o.getQuantity())*(1-o.getDiscount()/100);
                                                        }else {
                                                            originalTotalPrice += d;
                                                        }
                                                    }
                                                    originalTotalPrice= originalTotalPrice - (originalTotalPrice * (SESSION._ORDERS.cartDiscount / 100));
                                                    salePrice= salePrice - (salePrice * (X/ 100));
                                                    Log.d("teeestr",originalTotalPrice+":  "+salePrice);
                                                    if (originalTotalPrice>=salePrice){
                                                        discount = (1 - (d / (o.getProduct().getPriceWithTax()  * selectedOrderOnCart.getQuantity()))) * 100;
                                                        selectedOrderOnCart.setDiscount(discount);
                                                        totalDiscount.setText(Util.makePrice(itemOriginalPrice * selectedOrderOnCart.getDiscount()/100) + type);
                                                        priceAfterDiscount.setText(itemOriginalPrice*(1-(selectedOrderOnCart.getDiscount())/100) + type);
                                                        totalPriceForBalance.setText(itemOriginalPrice*(1-(selectedOrderOnCart.getDiscount())/100)*weightForProduct + type);
                                                        productDiscount.setText(str);
                                                    } else {
                                                        totalPriceForBalance.setText(itemOriginalPrice*weightForProduct + type);
                                                        Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                                        cashETCash.setBackgroundResource(R.drawable.backtext);
                                                    }
                                                } else {
                                                    totalPriceForBalance.setText(itemOriginalPrice*weightForProduct + type);
                                                    productDiscount.setText(Util.makePrice(itemOriginalPrice ));
                                                    totalDiscount.setText(Util.makePrice(itemOriginalPrice * selectedOrderOnCart.getDiscount()/100) + type);
                                                    priceAfterDiscount.setText(itemOriginalPrice*(1-(selectedOrderOnCart.getDiscount())/100) + type);
                                                }
                                            } else {
                                                if (!(str.equals(""))) {
                                                    double d = Double.parseDouble(str);
                                                    double originalTotalPrice = 0;
                                                    double salePrice=0;
                                                    for (int i=0;i<SESSION._ORDER_DETAILES.size();i++) {
                                                        OrderDetails o =SESSION._ORDER_DETAILES.get(i);
                                                        salePrice+=(o.getUnitPrice() * o.getQuantity());
                                                        if(i!=SESSION._ORDER_DETAILES.indexOf(selectedOrderOnCart)){
                                                            originalTotalPrice += ((o.getProduct().getPriceWithTax() ) * o.getQuantity())*(1-o.getDiscount()/100);
                                                        }else {
                                                            originalTotalPrice += ((o.getProduct().getPriceWithTax() ) * selectedOrderOnCart.getQuantity())*(1-d/100);
                                                        }
                                                    }
                                                    originalTotalPrice= originalTotalPrice - (originalTotalPrice * (SESSION._ORDERS.cartDiscount / 100));
                                                    salePrice= salePrice - (salePrice * (X/ 100));
                                                    if (originalTotalPrice>=salePrice){
                                                        discount= Double.parseDouble(str);
                                                        selectedOrderOnCart.setDiscount(discount);
                                                        Log.d("teeestr",itemOriginalPrice +"  "+ selectedOrderOnCart.getDiscount()/100);
                                                        totalDiscount.setText(Util.makePrice(itemOriginalPrice * selectedOrderOnCart.getDiscount()/100) + type);
                                                        priceAfterDiscount.setText(itemOriginalPrice*(1-(selectedOrderOnCart.getDiscount())/100) + type);
                                                        totalPriceForBalance.setText(itemOriginalPrice*(1-(selectedOrderOnCart.getDiscount())/100)*weightForProduct + type);
                                                        productDiscount.setText(str+"%");
                                                    } else {
                                                        totalPriceForBalance.setText(itemOriginalPrice*weightForProduct + type);
                                                        productDiscount.setText(0+"%");
                                                        Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.cant_do_this_function_discount), Toast.LENGTH_SHORT).show();
                                                        cashETCash.setBackgroundResource(R.drawable.backtext);
                                                    }
                                                } else {
                                                    totalPriceForBalance.setText(itemOriginalPrice*weightForProduct + type);
                                                    productDiscount.setText(0+"%");
                                                    totalDiscount.setText(Util.makePrice(itemOriginalPrice * selectedOrderOnCart.getDiscount()/100) + type);
                                                    priceAfterDiscount.setText(itemOriginalPrice*(1-(selectedOrderOnCart.getDiscount())/100) + type);
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
                                        }
                                    });*/
                                /*    cashBTOk.setOnClickListener(new View.OnClickListener() {
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
                                                        if(i!=indexOfItem){
                                                            originalTotalPrice += (Double.parseDouble(totalPriceForBalance.getText().toString()) * o.getQuantity())*(1-o.getDiscount()/100);
                                                        }else {
                                                            originalTotalPrice += d;
                                                        }
                                                    }
                                                    originalTotalPrice= originalTotalPrice - (originalTotalPrice * (SESSION._ORDERS.cartDiscount / 100));
                                                    salePrice= salePrice - (salePrice * (X/ 100));
                                                    if (originalTotalPrice>=salePrice){
                                                        discount = (1 - (d / (Double.parseDouble(totalPriceForBalance.getText().toString()) * selectedOrderOnCart.getQuantity()))) * 100;
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
                                                        salePrice+=(Double.parseDouble(totalPriceForBalance.getText().toString()) * o.getQuantity());
                                                        if(i!=SESSION._ORDER_DETAILES.indexOf(selectedOrderOnCart)){
                                                            originalTotalPrice += (Double.parseDouble(totalPriceForBalance.getText().toString()) * o.getQuantity())*(1-o.getDiscount()/100);
                                                        }else {
                                                            originalTotalPrice += (Double.parseDouble(totalPriceForBalance.getText().toString()) * selectedOrderOnCart.getQuantity())*(1-d/100);
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
                                        }
                                        }
                                    });*/
                                /*
                                } else {
                                    Toast.makeText(SalesCartActivity.this, getBaseContext().getString(R.string.please_select_item), Toast.LENGTH_SHORT);
                                }
                    }}
                });*/
                productWeightFromEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!(productWeightFromEditText.getText().toString().equals(""))){
                            double x=0;
                            x=Double.parseDouble(productWeightFromEditText.getText().toString());
                            totalPriceForBalance.setText((x*o.getProduct().getPriceWithTax())+"");

                        }}
                });
                btn_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(productWeightFromEditText.isShown()){
                            String s =productWeightFromEditText.getText().toString().trim();
                            if(!s.matches("")) {

                                SESSION._ORDER_DETAILES.get(SESSION._ORDER_DETAILES.size() - 1).setQuantity(Double.parseDouble(productWeightFromEditText.getText().toString()));
                                calculateTotalPrice();
                            }   }else {
                            SESSION._ORDER_DETAILES.get(SESSION._ORDER_DETAILES.size()-1).setQuantity( weightForProduct);
                            calculateTotalPrice();
                        }

                        productWeightDialog.dismiss();
                    }
                });
                productWeighFromEditTextImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        productWeightFromEditText.setVisibility(View.VISIBLE);
                    }
                });
                btn_add_weight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(devicesList.size()>0){
                            refreshList();
                            selectedDeviceIndex =0;
                            selectedDevice = devicesList.get(0);
                            runTest(deviceHelper.getAvailableDrivers().get(0));
                        }
                    }
                });
                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        productWeightDialog.dismiss();
                    }
                });
            }
            SESSION._ORDER_DETAILES.add(o);
            newOrderDetails=o;
            Log.d("teee",SESSION._ORDER_DETAILES.toString());

        }


        //restCategoryOffers();
        removeOrderItemSelection();
        refreshCart();
    }

    private void increaseItemOnCart(int index) {
        OrderDetails orderDetails = SESSION._ORDER_DETAILES.get(index);
        if (Long.valueOf(orderDetails.getProduct().getOfferId())==null){
            Log.d("offerdIdNull","increaseItemOnCart");
        }
        else {
        if(orderDetails.getProduct().getOfferId()==0) {
            SESSION._ORDER_DETAILES.get(index).increaseCount();
        }else {
            try {
                addToCart(orderDetails.getProduct());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(!orderDetails.scannable||orderDetails.giftProduct)
            restCategoryOffers();
        calculateTotalPrice();
    }}

    private void decreaseItemOnCart(int index) {
        OrderDetails orderDetails = SESSION._ORDER_DETAILES.get(index);
        if (Long.valueOf(orderDetails.getProduct().getOfferId())==null){
            Log.d("offerdIdNull","decreaseItemOnCart");
        }
        else {
        if(orderDetails.getProduct().getOfferId()==0) {
            SESSION._ORDER_DETAILES.get(index).decreaseCount();
        }
        else {
            removeFromCart(index);
        }
       /* if(!orderDetails.scannable||orderDetails.giftProduct)
            restCategoryOffers();
        restCategoryOffers();*/
        calculateTotalPrice();
    }}


    private void refreshCart() {
        calculateTotalPrice();
    }

    private void enterKeyPressed(String barcodeScanned) throws JSONException {
        Product product = new Product();
        char firstChar = barcodeScanned.charAt(0);
        productDBAdapter.open();

        if(firstChar=='2'&&barcodeScanned.length()==13){
            product = productDBAdapter.getProductByBarCode(barcodeScanned);
            if (product!=null&& !product.getUnit().equals("BARCODEWITHPRICE") ){
                product = productDBAdapter.getProductByBarCode(barcodeScanned);
            }
            else {
            String productNum = barcodeScanned.substring(1,7);
            Log.d("tttttt",productNum);
            Double newPrice = Double.parseDouble(barcodeScanned.substring(7))/1000;
            product = productDBAdapter.getProductByBarCode(productNum);
            if(product!=null){
                product.setPriceWithTax(newPrice);}
            }
        } else {
            product = productDBAdapter.getProductByBarCode(barcodeScanned);
        }
        productDBAdapter.close();
        final Intent intent = new Intent(SalesCartActivity.this, ProductsActivity.class);
        intent.putExtra("barcode", barcodeScanned);
        if (product != null) {
            addToCart(product);
        } else {
            EmployeePermissionsDBAdapter employeeDBAdapter =new EmployeePermissionsDBAdapter(SalesCartActivity.this);
            employeeDBAdapter.open();
            List<Integer> employeePermition =employeeDBAdapter.getPermissions(SESSION._EMPLOYEE.getEmployeeId());

            if(employeePermition.contains(3)){
              final AlertDialog addNewProductDialog = new AlertDialog.Builder(SalesCartActivity.this)
                        .setTitle("Add Product")
                        .setCancelable(false)
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
                addNewProductDialog.setOnKeyListener(new Dialog.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (keyCode == KeyEvent.ACTION_DOWN) {

                        }
                        else if (keyCode==KeyEvent.ACTION_UP){}
                        return true;
                    }
                });


            }
            else {
                final AlertDialog addNewProductDialog=    new AlertDialog.Builder(SalesCartActivity.this)
                        .setTitle("Add Product").setCancelable(false)
                        .setMessage(context.getString(R.string.this_product_not_in_your_permission))
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
                addNewProductDialog.setOnKeyListener(new Dialog.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (keyCode == KeyEvent.ACTION_DOWN) {

                        }
                        else if (keyCode==KeyEvent.ACTION_UP){}
                        return true;
                    }
                });
            }

        }
        barcodeScanned="";
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
//                dialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                All_productsList = productList;
                if(productCatalogGridViewAdapter==null){
                    productCatalogGridViewAdapter = new ProductCatalogGridViewAdapter(getApplicationContext(), All_productsList);
                    lvProducts.setAdapter(productCatalogGridViewAdapter);
                    gvProducts.setAdapter(productCatalogGridViewAdapter);
                    productCatalogGridViewAdapter.notifyDataSetChanged();
                }
                dialog.cancel();
                userScrolled=false;
            }

            @Override
            protected Void doInBackground(Void... params) {
                if (!searchWord.equals("")) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // productList.addAll(productDBAdapter.getAllProductsByHint(searchWord, productList.size()-1, 20));

                            // Stuff that updates the UI
                            productCatalogGridViewAdapter.notifyDataSetChanged();

                        }
                    });
                } else if (id == 0) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            productDBAdapter.open();
                            productList.addAll(productDBAdapter.getTopProducts( productList.size()-1, 50));
                            productDBAdapter.close();
                            // Stuff that updates the UI
                            productCatalogGridViewAdapter.notifyDataSetChanged();

                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            productDBAdapter.open();
                            productList.addAll(productDBAdapter.getAllProductsByCategory(id, productList.size()-1, 50));
                            productDBAdapter.close();
                            // Stuff that updates the UI
                            productCatalogGridViewAdapter.notifyDataSetChanged();

                        }
                    });
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
        final POSInterfaceAPI posInterfaceAPI = new POSUSBAPI(context);
        // final UsbPrinter printer = new UsbPrinter(1155, 30016);

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(context.getString(R.string.wait_for_finish_printing));

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

            }

            @Override
            protected Void doInBackground(Void... params) {
                PdfUA pdfUA = new PdfUA();

                try {
                    pdfUA.createNormalInvoice(context,SESSION._ORDER_DETAILES,SESSION._ORDERS,false,mainMer);
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try
                {
                    File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName());
                    File file = new File(path,"normalInvoice.pdf");
                    RandomAccessFile f = new RandomAccessFile(file, "r");
                    byte[] data = new byte[(int)f.length()];
                    f.readFully(data);
                    Log.d("bitmapsize",context.toString()+"");

                    PrinterTools.pdfLoadImages(data,context,"");
                }
                catch(Exception ignored)
                {

                }
                InvoiceImg invoiceImg = new InvoiceImg(context);
                Log.d("payyyyy",SESSION._ORDERS.getPayment().toString());

                //  pos.imageStandardModeRasterPrint(invoiceImg.normalInvoice(SESSION._ORDERS.getOrderId(), SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, null), CONSTANT.PRINTER_PAGE_WIDTH);

                return null;
            }
        }.execute();
    }

    private static HPRTPrinterHelper HPRTPrinter = new HPRTPrinterHelper();

    private void printAndOpenCashBoxHPRT_TP805(final String mainAns, final String mainMer, final String mainCli) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(context.getString(R.string.wait_for_finish_printing));

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                dialog.show();

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try
                {
                    File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName());
                    File file = new File(path,"normalInvoice.pdf");
                    RandomAccessFile f = new RandomAccessFile(file, "r");
                    byte[] data = new byte[(int)f.length()];
                    f.readFully(data);
                    Log.d("bitmapsize",context.toString()+"");

                    PrinterTools.pdfLoadImages(data,context,"");
                    MyTaskFour myTaskFour =new MyTaskFour();
                    myTaskFour.execute();

                }

                catch(Exception ignored)
                {

                }
                dialog.cancel();

            }


            @Override
            protected Void doInBackground(Void... params) {

                PdfUA pdfUA = new PdfUA();

                try {
                    Log.d("teeesdr",SESSION._ORDER_DETAILES.toString()+"");

                    pdfUA.createNormalInvoice(context,SESSION._ORDER_DETAILES,SESSION._ORDERS,false,mainMer);
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }
        }.execute();
    }
 private class MyTaskFour extends AsyncTask<Void, Void, Void> {
        public MyTaskFour() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.e("T4", "doInBackground");
            publishProgress();
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
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.e("T4", "onPostExecute");
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.e("T4", "onProgressUpdate");
            super.onProgressUpdate(values);
        }
    }
    private void printAndOpenCashBoxSUNMI_T1(String mainAns, final String mainMer, final String mainCli) {
        //AidlUtil.getInstance().connectPrinterService(this);
        if (AidlUtil.getInstance().isConnect()) {
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setTitle(context.getString(R.string.wait_for_finish_printing));

            dialog.show();
            PdfUA pdfUA = new PdfUA();

            try {
                pdfUA.createNormalInvoice(context,SESSION._ORDER_DETAILES,SESSION._ORDERS,false,mainMer);
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try
            {
                File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName());
                File file = new File(path,"normalInvoice.pdf");
                RandomAccessFile f = new RandomAccessFile(file, "r");
                byte[] data = new byte[(int)f.length()];
                f.readFully(data);
                Log.d("bitmapsize",context.toString()+"");

                PrinterTools.pdfLoadImages(data,context,"");
            }
            catch(Exception ignored)
            {

            }


            dialog.cancel();
        } else {
            new android.support.v7.app.AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                    .setTitle(context.getString(R.string.printer))
                    .setMessage(context.getString(R.string.please_connect_the_printer))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // finish();
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
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setTitle(context.getString(R.string.wait_for_finish_printing));

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    dialog.show();
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    //feed paper

                    dialog.cancel();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    InvoiceImg invoiceImg = new InvoiceImg(context);
                    byte b = 0;
                    try {
                        //// TODO: 13/06/2018 adding pinpad support
                        PdfUA pdfUA = new PdfUA();

                        try {
                            pdfUA.createNormalInvoice(context,SESSION._ORDER_DETAILES,SESSION._ORDERS,false,mainMer);
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try
                        {
                            File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName());
                            File file = new File(path,"normalInvoice.pdf");
                            RandomAccessFile f = new RandomAccessFile(file, "r");
                            byte[] data = new byte[(int)f.length()];
                            f.readFully(data);
                            Log.d("bitmapsize",context.toString()+"");

                            PrinterTools.pdfLoadImages(data,context,"");
                        }
                        catch(Exception ignored)
                        {

                        }
                        Bitmap bitmap = invoiceImg.normalInvoice(SESSION._ORDERS.getOrderId(), SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, null,"");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        }

    }

    public void printAndOpenCashBox(String mainAns, final String mainMer, final String mainCli, int source) {
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
        // if (source == REQUEST_CASH_ACTIVITY_CODE || source == REQUEST_CASH_ACTIVITY_WITH_CURRENCY_CODE)
        //   currencyReturnsCustomDialogActivity.show();

    }

    private CurrencyReturnsCustomDialogActivity currencyReturnsCustomDialogActivity;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* Inventory in=null;
        HashMap<String,Integer>productHashMap=new HashMap<String, Integer>();
        for(OrderDetails o :SESSION._ORDER_DETAILES){
            ProductInventory productInventory = productInventoryDbAdapter.getProductInventoryByID(o.getProduct().getProductId());
            if(productInventory!=null){
            if(o.getProduct().getProductId()!=-1){
                Log.d("ttttt",productInventory.getQty()+ "   "+o.getQuantity());
            productInventoryDbAdapter.updateEntry(o.getProduct().getProductId(),productInventory.getQty()-o.getQuantity());
            productHashMap.put(String.valueOf(o.getProduct().getProductId()),productInventory.getQty()-o.getQuantity());
            }
        }
        }
        try {
            in = inventoryDbAdapter.getLastRow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BoInventory inventory = new BoInventory(in.getName(),in.getInventoryId(),productHashMap,in.getBranchId(),in.getHide());
        sendToBroker(MessageType.UPDATE_INVENTORY, inventory, this.context);*/
        if (Long.valueOf(SESSION._ORDERS.getCustomerId()) == 0) {
            if (SESSION._ORDERS.getCustomer_name() == null) {

                if (customerName_EditText.getText().toString().equals("")) {
                    customerDBAdapter.open();
                    Customer customer = customerDBAdapter.getCustomerByName("guest");
                    customerDBAdapter.close();
                    SESSION._ORDERS.setCustomer(customer);
                    setCustomer(SESSION._ORDERS.getCustomer());

                } else {
                    customerDBAdapter.open();
                    Customer customer = customerDBAdapter.getCustomerByID(SESSION._ORDERS.getCustomerId());
                    customerDBAdapter.close();
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
                sum_pointDbAdapter.open();
                usedpointDbAdapter.open();
                clubPoint = ((int) (SESSION._ORDERS.getTotalPrice() / clubAmount) * clubPoint);
                long saleID = saleDBAdapter.insertEntry(SESSION._ORDERS, customerId, customerName,false,0,0);
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
                    long orderid=0;
                    if(!o.getProduct().isWithTax()){
                        if (Long.valueOf(o.getOfferId())==null){
                            orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(), 0, o.getProductSerialNumber(), o.getPaidAmount(), o.getSerialNumber());
                        }
                        else {
                            orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(), o.getOfferId(), o.getProductSerialNumber(), o.getPaidAmount(), o.getSerialNumber());
                        }
                    }else {

                        if (Long.valueOf(o.getOfferId())==null){
                            orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(), 0, o.getProductSerialNumber(), o.getPaidAmount() / (1 + (SETTINGS.tax / 100)), o.getSerialNumber());
                        }
                        else {
                            orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(), o.getOfferId(), o.getProductSerialNumber(), o.getPaidAmount() / (1 + (SETTINGS.tax / 100)), o.getSerialNumber());
                        }
                    }
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
                    customerDBAdapter.open();
                    Customer upDateCustomer = customer;
                    upDateCustomer.setBalance(SESSION._ORDERS.getTotalPrice() + customer.getBalance());
                    customerDBAdapter.updateEntry(upDateCustomer);
                    customerDBAdapter.close();
                }
                orderDBAdapter.close();
                custmerAssetDB.close();
                SESSION._ORDERS.setOrders(SESSION._ORDER_DETAILES);
                SESSION._ORDERS.setUser(SESSION._EMPLOYEE);

                PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(SalesCartActivity.this);
                paymentDBAdapter.open();

                long paymentID = paymentDBAdapter.insertEntry( saleTotalPrice, saleID,order.getOrderKey());

                paymentDBAdapter.close();

                Payment payment = new Payment(paymentID,  saleTotalPrice, saleID);
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
            usedpointDbAdapter.close();
            sum_pointDbAdapter.close();
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
                long saleID = saleDBAdapter.insertEntry(SESSION._ORDERS, customerId, customerName,false,0,0);
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
                    long orderid=0;
                    if(!o.getProduct().isWithTax()){
                        if (Long.valueOf(o.getOfferId())==null) {

                            orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(),0, o.getProductSerialNumber(), o.getPaidAmount(), o.getSerialNumber());
                        }
                        else {
                            orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(),0, o.getProductSerialNumber(), o.getPaidAmount(), o.getSerialNumber());
                        }
                    }
                    else {
                        if (Long.valueOf(o.getOfferId())==null){
                            orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(),0, o.getProductSerialNumber(), o.getPaidAmount() / (1 + (SETTINGS.tax / 100)), o.getSerialNumber());
                        }

                        else {
                            orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(), o.getOfferId(), o.getProductSerialNumber(), o.getPaidAmount() / (1 + (SETTINGS.tax / 100)), o.getSerialNumber());
                        }
                    }                    orderId.add(orderid);
                    //   orderDBAdapter.insertEntry(o.getProductSku(), o.getCount(), o.getUserOffer(), saleID, o.getPriceWithTax(), o.getOriginal_price(), o.getDiscount(),o.getCustmerAssestId());
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
                    customerDBAdapter.open();
                    Customer upDateCustomer = customer;
                    upDateCustomer.setBalance(SESSION._ORDERS.getTotalPrice() + customer.getBalance());
                    customerDBAdapter.updateEntry(upDateCustomer);
                    customerDBAdapter.close();
                }
                orderDBAdapter.close();
                custmerAssetDB.close();
                SESSION._ORDERS.setOrders(SESSION._ORDER_DETAILES);
                SESSION._ORDERS.setUser(SESSION._EMPLOYEE);

                PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(SalesCartActivity.this);
                paymentDBAdapter.open();

                long paymentID = paymentDBAdapter.insertEntry(saleTotalPrice, saleID,order.getOrderKey());

                paymentDBAdapter.close();

                Payment payment = new Payment(paymentID, saleTotalPrice, saleID);
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
                sum_pointDbAdapter.open();
                clubPoint = ((int) (SESSION._ORDERS.getTotalPrice() / clubAmount) * clubPoint);
                long saleID = saleDBAdapter.insertEntry(SESSION._ORDERS, customerId, customerName,false,0,0);
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
                    long orderid=0;
                    if(!o.getProduct().isWithTax()){
                        if (Long.valueOf(o.getOfferId())==null){
                            orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(), 0, o.getProductSerialNumber(), o.getPaidAmount(), o.getSerialNumber());}
                        else {
                            orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(), o.getOfferId(), o.getProductSerialNumber(), o.getPaidAmount(), o.getSerialNumber());
                        }}

                    else {
                        if (Long.valueOf(o.getOfferId())==null){
                            orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(), 0, o.getProductSerialNumber(), o.getPaidAmount() / (1 + (SETTINGS.tax / 100)), o.getSerialNumber());
                        }

                        else {
                            orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(), o.getOfferId(), o.getProductSerialNumber(), o.getPaidAmount() / (1 + (SETTINGS.tax / 100)), o.getSerialNumber());
                        }
                    }
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
                    customerDBAdapter.open();
                    Customer upDateCustomer = customer;
                    upDateCustomer.setBalance(SESSION._ORDERS.getTotalPrice() + customer.getBalance());
                    customerDBAdapter.updateEntry(upDateCustomer);
                    customerDBAdapter.close();
                }
                orderDBAdapter.close();

                PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(this);
                paymentDBAdapter.open();

                long paymentID = paymentDBAdapter.insertEntry( saleTotalPrice, saleID, order.getOrderKey());

                paymentDBAdapter.close();

                Payment payment = new Payment(paymentID,saleTotalPrice, saleID);
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
            sum_pointDbAdapter.close();
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
                usedpointDbAdapter.open();
                sum_pointDbAdapter.open();
                paymentDBAdapter.open();

                // Get data from CashActivityWithOutCurrency
                double totalPaidWithOutCurrency = data.getDoubleExtra(OldCashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_WITHOUT_CURRENCY_TOTAL_PAID, 0.0f);
                double excess = data.getDoubleExtra(OldCashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_WITHOUT_CURRENCY_EXCESS_VALUE, 0.0f);
                Log.d("ex", String.valueOf(excess));
                double totalPrice = data.getDoubleExtra(SalesCartActivity.COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE, 0.0f);
                SESSION._ORDERS.setTotalPrice(totalPrice);
                SESSION._ORDERS.setTotalPaidAmount(totalPaidWithOutCurrency);

                clubPoint = ((int) (SESSION._ORDERS.getTotalPrice() / clubAmount) * clubPoint);
                saleIDforCash = saleDBAdapter.insertEntry(SESSION._ORDERS, customerId, customerName,false,0,0);
                Order order = saleDBAdapter.getOrderById(saleIDforCash);
                SESSION._ORDERS.setOrderId(saleIDforCash);

                currencyReturnsCustomDialogActivity = new CurrencyReturnsCustomDialogActivity(this, excess, new Order(SESSION._ORDERS),"","","");


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
                    long orderid=0;
                    if(!o.getProduct().isWithTax()){

                        if (Long.valueOf(o.getOfferId())==null){
                            orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleIDforCash, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(),order.getOrderKey(),0,o.getProductSerialNumber(),o.getPaidAmount(),o.getSerialNumber());
                        }
                        else {
                            orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleIDforCash, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(),order.getOrderKey(),o.getOfferId(),o.getProductSerialNumber(),o.getPaidAmount(),o.getSerialNumber());
                        }

                    }
                    else {
                        if (Long.valueOf(o.getOfferId())==null){
                            orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleIDforCash, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(), 0, o.getProductSerialNumber(),o.getPaidAmount() / (1 + (SETTINGS.tax / 100)),o.getSerialNumber());
                        }
                        else {
                            orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleIDforCash, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(), o.getOfferId(), o.getProductSerialNumber(), o.getPaidAmount() / (1 + (SETTINGS.tax / 100)), o.getSerialNumber());
                        }

                        //   orderDBAdapter.insertEntry(o.getProductSku(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(),o.getCustomer_assistance_id());
                    }
                    orderId.add(orderid);}
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
                    customerDBAdapter.open();
                    Customer upDateCustomer = customer;
                    upDateCustomer.setBalance(SESSION._ORDERS.getTotalPrice() + customer.getBalance());
                    customerDBAdapter.updateEntry(upDateCustomer);
                    customerDBAdapter.close();
                }

                orderDBAdapter.close();
                custmerAssetDB.close();
                // End ORDER_DETAILS And CustomerAssistant Region
                // Payment Region
                long paymentID = paymentDBAdapter.insertEntry( saleTotalPrice, saleIDforCash,order.getOrderKey());

                Payment payment = new Payment(paymentID,saleTotalPrice, saleIDforCash);
                SESSION._ORDERS.setPayment(payment);
                SESSION._ORDERS.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                cashPaymentDBAdapter.insertEntry(saleIDforCash, saleTotalPrice, 0, new Timestamp(System.currentTimeMillis()),1,1);
                cashPaymentDBAdapter.close();
                paymentDBAdapter.close();
                printAndOpenCashBox("", "", "", REQUEST_CASH_ACTIVITY_CODE);
                currencyReturnsCustomDialogActivity.show();

                saleDBAdapter.close();
                return;
            }
            usedpointDbAdapter.close();
            sum_pointDbAdapter.close();
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



                PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(this);
                saleDBAdapter = new OrderDBAdapter(SalesCartActivity.this);
                orderDBAdapter = new OrderDetailsDBAdapter(SalesCartActivity.this);
                custmerAssetDB = new CustomerAssetDB(SalesCartActivity.this);
                saleDBAdapter.open();
                orderDBAdapter.open();
                custmerAssetDB.open();
                usedpointDbAdapter.open();
                sum_pointDbAdapter.open();
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

                saleIDforCash = saleDBAdapter.insertEntry(SESSION._ORDERS, customerId, customerName,false,0,0);
                Order order = saleDBAdapter.getOrderById(saleIDforCash);
                SESSION._ORDERS.setOrderId(saleIDforCash);
                currencyReturnsCustomDialogActivity = new CurrencyReturnsCustomDialogActivity(this, excess, new Order(SESSION._ORDERS),"","","");

                if (firstCurrencyAmount > 0) {
                    //         cashPaymentDBAdapter.insertEntry(saleIDforCash, firstCurrencyAmount, firstCurrencyId, new Timestamp(System.currentTimeMillis()));
                }
                if (secondCurrencyAmount > 0) {
                    //      cashPaymentDBAdapter.insertEntry(saleIDforCash, secondCurrencyAmount, secondCurrencyId, new Timestamp(System.currentTimeMillis()));
                }



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
                    long orderid=0;
                    if(!o.getProduct().isWithTax()){
                        if (Long.valueOf(o.getOfferId())==null){
                            orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleIDforCash, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(), 0, o.getProductSerialNumber(), o.getPaidAmount(), o.getSerialNumber());
                        }
                        else {
                            orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleIDforCash, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(), o.getOfferId(), o.getProductSerialNumber(), o.getPaidAmount(), o.getSerialNumber());
                        }}
                    else {
                        if (Long.valueOf(o.getOfferId())==null){
                            orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleIDforCash, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(),0, o.getProductSerialNumber(),o.getPaidAmount() / (1 + (SETTINGS.tax / 100)),o.getSerialNumber());
                        }
                        else {
                            orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleIDforCash, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(), o.getOfferId(), o.getProductSerialNumber(),o.getPaidAmount() / (1 + (SETTINGS.tax / 100)),o.getSerialNumber());

                        }
                        //   orderDBAdapter.insertEntry(o.getProductSku(), o.getQuantity(), o.getUserOffer(), saleID, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(),o.getCustomer_assistance_id());
                    } orderId.add(orderid);}
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
                    customerDBAdapter.open();
                    Customer upDateCustomer = customer;
                    upDateCustomer.setBalance(SESSION._ORDERS.getTotalPrice() + customer.getBalance());
                    customerDBAdapter.updateEntry(upDateCustomer);
                    customerDBAdapter.close();
                }
                orderDBAdapter.close();
                custmerAssetDB.close();
                // End ORDER_DETAILS And CustomerAssistant Region

                // Payment Region
                long paymentID = paymentDBAdapter.insertEntry( saleTotalPrice, saleIDforCash,order.getOrderKey());

                Payment payment = new Payment(paymentID,  saleTotalPrice, saleIDforCash);

                SESSION._ORDERS.setPayment(payment);

                paymentDBAdapter.close();
                if(CurrencyReturnsCustomDialogActivity.REQUEST_CURRENCY_RETURN_ACTIVITY_CODE){
                    printAndOpenCashBox("", "", "", REQUEST_CASH_ACTIVITY_CODE);
                    CurrencyReturnsCustomDialogActivity.REQUEST_CURRENCY_RETURN_ACTIVITY_CODE=false;

                }


                return;
            }
            usedpointDbAdapter.close();
            sum_pointDbAdapter.close();

        }
        //endregion
        if (requestCode == REQUEST_MULTI_CURRENCY_ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {
                ZReportDBAdapter zReportDBAdapter =new ZReportDBAdapter(context);
                zReportDBAdapter.open();
                ZReportCountDbAdapter zReportCountDbAdapter =new ZReportCountDbAdapter(SalesCartActivity.this);
                zReportCountDbAdapter.open();
                boolean trueCreditCard=false;
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

                    ZReport zReport =zReportDBAdapter.getLastRow();
                    Log.d("zReportSA",zReport.toString());
                    ZReportCount zReportCount =zReportCountDbAdapter.getLastRow();
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
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        PaymentTable paymentTable= objectMapper.readValue(jsonObject.toString(), PaymentTable.class);
                        paymentTableArrayList.add(paymentTable);
                        if( jsonObject.getDouble("tendered") >0) {
                            TotalPaidAmount += jsonObject.getDouble("tendered") * getCurrencyRate(jsonObject.getJSONObject("currency").getString("type"));
                        }
                        change = Math.abs(jsonObject.getDouble("due"));

                    }


                    double SalesWitheTax=0,SalesWithoutTax=0,salesaftertax=0;
                    for (int i=0;i<SESSION._ORDER_DETAILES.size();i++){
                        if (SESSION._ORDER_DETAILES.get(i).getProduct().getCurrencyType().equals("0")){
                            updateCurrencyType.updateCurrencyToShekl(SalesCartActivity.context,SESSION._ORDER_DETAILES.get(i).getProduct());
                        }
                        //       if (SESSION._ORDER_DETAILES.get(i).getProduct().getCurrencyType()==0){
                        //   double rateCurrency= ConverterCurrency.getRateCurrency("ILS",SalesCartActivity.this);
                        double rateCurrency= ConverterCurrency.getRateCurrency(SESSION._ORDER_DETAILES.get(i).getProduct().getCurrencyType(),SalesCartActivity.this);
                        if(!SESSION._ORDER_DETAILES.get(i).getProduct().isWithTax()){
                            if(SESSION._ORDERS.getCartDiscount()>0){
                                SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100))))));
                                SalesWithoutTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                            }else {
                                SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(SESSION._ORDER_DETAILES.get(i).getPaidAmount())));
                                SalesWithoutTax +=( SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                Log.d("tesddt","    "+SalesWithoutTax);

                            }
                        }else {
                            if(SESSION._ORDERS.getCartDiscount()>0){
                                SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100)))/ (1 + (SETTINGS.tax / 100)))));
                                Log.d("salesaftertax", SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()+"ko2333"+SESSION._ORDER_DETAILES.get(i).getPaidAmount()+"ko2333"+(SESSION._ORDERS.getCartDiscount()/100));

                                salesaftertax+=((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100)))*rateCurrency);
                                SalesWitheTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                            }else {
                                SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(SESSION._ORDER_DETAILES.get(i).getPaidAmount() / (1 + (SETTINGS.tax / 100)))));
                                salesaftertax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*rateCurrency);
                                SalesWitheTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                            }
                        }
                        //  }



/*
                        if (SESSION._ORDER_DETAILES.get(i).getProduct().getCurrencyType()==1){
                            double rateCurrency= ConverterCurrency.getRateCurrency("USD",SalesCartActivity.this);
                            if(SESSION._ORDER_DETAILES.get(i).getProduct().isWithTax()){
                                if(SESSION._ORDERS.getCartDiscount()>0){
                                    SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100))))));
                                    SalesWithoutTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                }else {
                                    SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(SESSION._ORDER_DETAILES.get(i).getPaidAmount())));
                                    SalesWithoutTax += (SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                }
                            }else {
                                if(SESSION._ORDERS.getCartDiscount()>0){
                                    SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100)))/ (1 + (SETTINGS.tax / 100)))));
                                    Log.d("salesaftertax", SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()+"ko2333"+SESSION._ORDER_DETAILES.get(i).getPaidAmount()+"ko2333"+(SESSION._ORDERS.getCartDiscount()/100));
                                    salesaftertax+=((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100)))*rateCurrency);
                                    SalesWitheTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                }else {
                                    SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(SESSION._ORDER_DETAILES.get(i).getPaidAmount() / (1 + (SETTINGS.tax / 100)))));
                                    salesaftertax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*rateCurrency);
                                    SalesWitheTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                }
                            }
                        }
                        if (SESSION._ORDER_DETAILES.get(i).getProduct().getCurrencyType()==2){
                            double rateCurrency= ConverterCurrency.getRateCurrency("GBP",SalesCartActivity.this);
                            if(SESSION._ORDER_DETAILES.get(i).getProduct().isWithTax()){
                                if(SESSION._ORDERS.getCartDiscount()>0){
                                    SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100))))));
                                    SalesWithoutTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                }else {
                                    SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(SESSION._ORDER_DETAILES.get(i).getPaidAmount())));
                                    SalesWithoutTax += (SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                }
                            }else {
                                if(SESSION._ORDERS.getCartDiscount()>0){
                                    SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100)))/ (1 + (SETTINGS.tax / 100)))));
                                    Log.d("salesaftertax", SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()+"ko2333"+SESSION._ORDER_DETAILES.get(i).getPaidAmount()+"ko2333"+(SESSION._ORDERS.getCartDiscount()/100));
                                    salesaftertax+=((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100)))*rateCurrency);
                                    SalesWitheTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                }else {
                                    SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(SESSION._ORDER_DETAILES.get(i).getPaidAmount() / (1 + (SETTINGS.tax / 100)))));
                                    salesaftertax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*rateCurrency);
                                    SalesWitheTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                }
                            }
                        }
                        if (SESSION._ORDER_DETAILES.get(i).getProduct().getCurrencyType()==3){
                            double rateCurrency= ConverterCurrency.getRateCurrency("EUR",SalesCartActivity.this);
                            if(SESSION._ORDER_DETAILES.get(i).getProduct().isWithTax()){
                                if(SESSION._ORDERS.getCartDiscount()>0){
                                    SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100))))));
                                    SalesWithoutTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                }else {
                                    SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(SESSION._ORDER_DETAILES.get(i).getPaidAmount())));
                                    SalesWithoutTax += (SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                }
                            }else {
                                if(SESSION._ORDERS.getCartDiscount()>0){
                                    SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100)))/ (1 + (SETTINGS.tax / 100)))));
                                    Log.d("salesaftertax", SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()+"ko2333"+SESSION._ORDER_DETAILES.get(i).getPaidAmount()+"ko2333"+(SESSION._ORDERS.getCartDiscount()/100));
                                    salesaftertax+=((SESSION._ORDER_DETAILES.get(i).getPaidAmount()-(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*(SESSION._ORDERS.getCartDiscount()/100)))*rateCurrency);
                                    SalesWitheTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                }else {
                                    SESSION._ORDER_DETAILES.get(i).setPaidAmountAfterTax(Double.parseDouble(Util.makePrice(SESSION._ORDER_DETAILES.get(i).getPaidAmount() / (1 + (SETTINGS.tax / 100)))));
                                    salesaftertax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmount()*rateCurrency);
                                    SalesWitheTax+=(SESSION._ORDER_DETAILES.get(i).getPaidAmountAfterTax()*rateCurrency);
                                }
                            }
                        }*/



                    }
                    SESSION._ORDERS.setSalesWithTax(Double.parseDouble(Util.makePrice(SalesWitheTax)));
                    SESSION._ORDERS.setSalesBeforeTax(Double.parseDouble(Util.makePrice(SalesWithoutTax)));
                    SESSION._ORDERS.setTotalPaidAmount(TotalPaidAmount);
                    saleIDforCash = saleDBAdapter.insertEntry(SESSION._ORDERS, customerId, customerName,false,SalesWithoutTax,SalesWitheTax);
                    zReport.setSalesBeforeTax(Double.parseDouble(Util.makePrice(zReport.getSalesBeforeTax() + (SalesWithoutTax))));
                    zReport.setSalesWithTax(Double.parseDouble(Util.makePrice(zReport.getSalesWithTax() + (SalesWitheTax))));
                    zReport.setTotalTax(Double.parseDouble(Util.makePrice(zReport.getTotalTax()+(salesaftertax - SalesWitheTax))));
                    Order order = saleDBAdapter.getOrderById(saleIDforCash);
                    SESSION._ORDERS.setOrderId(saleIDforCash);
                    SESSION._ORDERS.setNumberDiscount(order.getNumberDiscount());

                    if(saleTotalPrice<0){
                        currencyOperationDBAdapter.insertEntry(new Timestamp(System.currentTimeMillis()),saleIDforCash,CONSTANT.DEBIT,0,SETTINGS.currencyCode,CONSTANT.CASH);

                    }else {
                        for (int i = 0 ;i<paymentTableArrayList.size()-1;i++){
                            currencyOperationDBAdapter.insertEntry(new Timestamp(System.currentTimeMillis()),saleIDforCash,CONSTANT.DEBIT,paymentTableArrayList.get(i).getTendered(),paymentTableArrayList.get(i).getCurrency().getType(),paymentTableArrayList.get(i).getPaymentMethod());
                            Log.d("enableCurrency",SETTINGS.enableCurrencies+"");
                            if (SETTINGS.enableCurrencies){
                                if(paymentTableArrayList.get(i).getCurrency().getType().equalsIgnoreCase(currencyTypesList.get(1).getType())) {
                                    zReport.setSecondTypeAmount(zReport.getSecondTypeAmount() + paymentTableArrayList.get(i).getTendered());
                                    zReportCount.setSecondTypeCount(zReportCount.getSecondTypeCount()+1);
                                }else if(paymentTableArrayList.get(i).getCurrency().getType().equalsIgnoreCase(currencyTypesList.get(2).getType())) {
                                    zReport.setThirdTypeAmount(zReport.getThirdTypeAmount() + paymentTableArrayList.get(i).getTendered());
                                    zReportCount.setThirdTypeCount(zReportCount.getThirdTypeCount()+1);
                                }
                                else if(paymentTableArrayList.get(i).getCurrency().getType().equalsIgnoreCase(currencyTypesList.get(3).getType())) {
                                    zReport.setFourthTypeAmount(zReport.getFourthTypeAmount() + paymentTableArrayList.get(i).getTendered());
                                    zReportCount.setFourthTypeCount(zReportCount.getFourthTypeCount()+1);
                                }
                            }}
                    }
                    long paymentID = paymentDBAdapter.insertEntry(saleTotalPrice, saleIDforCash,order.getOrderKey());


                    for (int i = 0; i < jsonArray.length()- 1; i++) {
                        double totPay=0;

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(jsonObject.getString("paymentMethod").equalsIgnoreCase(CONSTANT.CASH)) {
                            cashPaymentDBAdapter.insertEntry(saleIDforCash, jsonObject.getDouble("tendered"), getCurrencyIdByType(jsonObject.getJSONObject("currency").getString("type")), new Timestamp(System.currentTimeMillis()), getCurrencyRate(jsonObject.getJSONObject("currency").getString("type")), jsonObject.getDouble("actualCurrencyRate"));
                            if( getCurrencyIdByType(jsonObject.getJSONObject("currency").getString("type"))==0){
                                if (jsonObject.getDouble("tendered")<0){
                                    zReport.setFirstTypeAmount(zReport.getFirstTypeAmount());
                                }
                                else {
                                    zReport.setFirstTypeAmount(zReport.getFirstTypeAmount()+ jsonObject.getDouble("tendered"));

                                }
                                Log.d("getShekelAmount",zReport.getFirstTypeAmount()+"");
                                zReportCount.setFirstTYpeCount(zReportCount.getFirstTYpeCount()+1);

                                Log.d("ShekelCount",zReportCount.getFirstTYpeCount()+1+"");
                            }
                            if (jsonObject.getDouble("tendered")>0){
                                zReport.setCashTotal(zReport.getCashTotal()+jsonObject.getDouble("tendered")* getCurrencyRate(jsonObject.getJSONObject("currency").getString("type")));}
                            zReportCount.setCashCount(zReportCount.getCashCount()+1);
                        }else if(jsonObject.getString("paymentMethod").equalsIgnoreCase(CONSTANT.CREDIT_CARD)){
                            trueCreditCard=true;
                            CreditCardPaymentDBAdapter creditCardPaymentDBAdapter = new CreditCardPaymentDBAdapter(this);
                            creditCardPaymentDBAdapter.open();
                            CreditCardPayment ccp = SESSION._TEMP_CREDITCARD_PAYMNET;
                            creditCardPaymentDBAdapter.insertEntry(saleIDforCash, ccp.getAmount(), ccp.getCreditCardCompanyName(), ccp.getTransactionType(), ccp.getLast4Digits(), ccp.getTransactionId(), ccp.getAnswer(), ccp.getPaymentsNumber()
                                    , ccp.getFirstPaymentAmount(), ccp.getOtherPaymentAmount(), ccp.getCreditCardCompanyName());

                            creditCardPaymentDBAdapter.close();
                            zReport.setCreditTotal(zReport.getCreditTotal()+ccp.getAmount());
                            zReportCount.setCreditCount(zReportCount.getCreditCount()+1);
                        }
                    }
                    if(SESSION._CHECKS_HOLDER.size()>0){

                        ChecksDBAdapter checksDBAdapter = new ChecksDBAdapter(this);
                        checksDBAdapter.open();
                        for (Check check : SESSION._CHECKS_HOLDER) {
                            if( check.getAmount()>0){
                                checksDBAdapter.insertEntry(check.getCheckNum(), check.getBankNum(), check.getBranchNum(), check.getAccountNum(), check.getAmount(), check.getCreatedAt(), saleIDforCash);
                                zReport.setCheckTotal(zReport.getCheckTotal()+check.getAmount());
                                Log.d("setCheckTotal",Double.toString(zReport.getCheckTotal()));
                                zReportCount.setCheckCount(zReportCount.getCheckCount()+1);
                            }
                        }
                        checksDBAdapter.close();
                    }

                    zReportDBAdapter.updateEntry(zReport);
                    zReportDBAdapter.close();
                    Log.d("testZreportCount",zReportCount.toString());
                    zReportCount.setInvoiceReceiptCount(zReportCount.getInvoiceReceiptCount()+1);
                    zReportCountDbAdapter.updateEntry(zReportCount);
                    cashPaymentDBAdapter.close();
                    // Club with point and amount
                  /*  if (clubType == 2) {
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
                    }*/
                    if (forSaleMan) {
                        custmerAssetDB.insertEntry(saleIDforCash, custmerSaleAssetstId, SESSION._ORDERS.getTotalPrice(), 0, "ORDER", SESSION._ORDERS.getCreatedAt());
                    }
                    // insert order region
                    int count=0;
                    for (OrderDetails o : SESSION._ORDER_DETAILES) {
                        long orderid=0;
                        if(!o.getProduct().isWithTax()){
                            Log.d("paidAmountS",o.getPaidAmount()+"");
                            if (Long.valueOf(o.getOfferId())==null) {
                                orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleIDforCash, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(), 0, o.getProductSerialNumber(), o.getPaidAmount(), o.getSerialNumber());
                            }
                            else {
                                orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleIDforCash, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(), o.getOfferId(), o.getProductSerialNumber(), o.getPaidAmount(), o.getSerialNumber());
                            }
                            count=count+1;
                        }else {
                            Log.d("paidAmountS",o.getPaidAmount()+"");
                            if (Long.valueOf(o.getOfferId())==null){
                                orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleIDforCash, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(), 0, o.getProductSerialNumber(), o.getPaidAmount() / (1 + (SETTINGS.tax / 100)), o.getSerialNumber());
                                //  SESSION._ORDER_DETAILES.get(count).setPaidAmountAfterTax( o.getPaidAmount() / (1 + (SETTINGS.tax / 100)));
                            }
                            else {
                                orderid = orderDBAdapter.insertEntry(o.getProductId(), o.getQuantity(), o.getUserOffer(), saleIDforCash, o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(), o.getCustomer_assistance_id(), order.getOrderKey(), o.getOfferId(), o.getProductSerialNumber(), o.getPaidAmount() / (1 + (SETTINGS.tax / 100)), o.getSerialNumber());
                                //  SESSION._ORDER_DETAILES.get(count).setPaidAmountAfterTax( o.getPaidAmount() / (1 + (SETTINGS.tax / 100)));
                            }
                            count=count+1;
                        }
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
                        customerDBAdapter.open();
                        Customer upDateCustomer = customer;
                        upDateCustomer.setBalance(SESSION._ORDERS.getTotalPrice() + customer.getBalance());
                        customerDBAdapter.updateEntry(upDateCustomer);
                        customerDBAdapter.close();
                    }
                    orderDBAdapter.close();
                    custmerAssetDB.close();
                    // End ORDER_DETAILS And CustomerAssistant Region

                    // Payment Region

                    Payment payment = new Payment(0, saleTotalPrice, saleIDforCash);

                    SESSION._ORDERS.setPayment(payment);

                    paymentDBAdapter.close();
                    Order order1 = new Order(SESSION._ORDERS);
                    order1.setPayment(payment);
                    SESSION._TEMP_ORDER_DETAILES=SESSION._ORDER_DETAILES;
                    SESSION._TEMP_ORDERS=SESSION._ORDERS;
                    if(saleTotalPrice<0){
                        if(!trueCreditCard) {
                            currencyReturnsCustomDialogActivity = new CurrencyReturnsCustomDialogActivity(this, saleTotalPrice*-1, order1,"","","");
                        }
                        else {
                            currencyReturnsCustomDialogActivity = new CurrencyReturnsCustomDialogActivity(this, saleTotalPrice*-1, order1,CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY,
                                    data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_MerchantNote),
                                    data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote));
                        }
                    }else {
                        if(!trueCreditCard) {
                            currencyReturnsCustomDialogActivity = new CurrencyReturnsCustomDialogActivity(this, change, order1,"","","");
                        }
                        else {
                            currencyReturnsCustomDialogActivity = new CurrencyReturnsCustomDialogActivity(this, change, order1,CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY,
                                    data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_MerchantNote),
                                    data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote));

                        }
                    }

                    currencyReturnsCustomDialogActivity.show();
                    Log.d("finelTestSalsCart",zReport.toString());

                    SESSION._Rest();
                    clearCart();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();

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
    public void  callClearCart(){
        clearCart();
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
customerDBAdapter.open();
        customerList = customerDBAdapter.getTopCustomer(0, 50);
        customerDBAdapter.close();
      //  AllCustmerList = customerList;
        AllCustmerList = new ArrayList<Customer>(customerList);
        custmerCatalogGridViewAdapter = new CustomerCatalogGridViewAdapter(getApplicationContext(), customerList);

        gvCustomer.setAdapter(custmerCatalogGridViewAdapter);

        gvCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setCustomer(customerList.get(position));
                SESSION._ORDERS.setCustomer_name(customerList.get(position).getFirstName());
                SESSION._ORDERS.setCustomer(customerList.get(position));
                SESSION._ORDERS.setCustomerId(customerList.get(position).getCustomerId());
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

    public void callPopupOrderSalesMan(final OrderDetails order) {
        EmployeeDBAdapter userDB = new EmployeeDBAdapter(this);
        userDB.open();
        /*final CustomerAssetDB customerAssistantDB = new CustomerAssetDB(this);
        customerAssistantDB.open();*/
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
        userDB.close();
        AllCustmerAssestList = custmerAssestList;


        CustomerAssistantCatalogGridViewAdapter adapter = new CustomerAssistantCatalogGridViewAdapter(getApplicationContext(), custmerAssestList);
        lvCustomerAssistant.setAdapter(adapter);


    }
    private void refreshList(){
        balanceValue="";
        devicesList.clear();

        if(getActiveDevices()!=null)
            devicesList.addAll(getActiveDevices());
        if(devicesList.size()>0){
        selectedDeviceIndex = 0;
        selectedDevice = devicesList.get(0);
    }
    }
    private void runTest(UsbSerialDriver driver) {
        deviceHelper.close();
        try {
            deviceHelper.openConnection(driver);
            mSerialIoManager = new SerialInputOutputManager(deviceHelper.port, mListener);
            mExecutor.submit(mSerialIoManager);

            deviceHelper.sendReadRequest();

        } catch (SendRequestException | CanNotOpenDeviceConnectionException | NoDevicesAvailableException e) {
            Toast.makeText(this, "Can`t run the test, Please check the USB port connection.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }private static String balanceValue = "";
    public SerialInputOutputManager.Listener mListener = new SerialInputOutputManager.Listener() {
        @Override
        public void onNewData(final byte[] data) {
            SalesCartActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    balanceValue += new String(data);
                    if(balanceValue.length()<DeviceHelper.DATA_LENGTH)
                        return;
                    if(balanceValue.length()>DeviceHelper.DATA_LENGTH){
                        balanceValue = "";
                        return;
                    }


                    final String balanceData = balanceValue;
                    final Context context = SalesCartActivity.this;

                    // closing device connection on receives data for ones

                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    productWeight.setText(balanceData);
                    weightForProduct=Double.parseDouble(balanceData);
                    totalPriceForBalance.setText(Util.makePrice(Double.parseDouble(balanceData)*PriceForWeight));

                   /* builder.setTitle("Test Result")
                            .setMessage("The result is: "+ balanceData)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    productWeight.setText(balanceData);
                                    ((SalesCartActivity) context).deviceHelper.close();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    ((SalesCartActivity) context).deviceHelper.close();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();*/
                }
            });
        }

        @Override
        public void onRunError(Exception e) {

        }
    };
    private ArrayList<DeviceSchema> getActiveDevices() {
        ArrayList<DeviceSchema> deviceList = new ArrayList<>();
        List<UsbSerialDriver> drivers = deviceHelper.getAvailableDrivers();

        if(drivers==null) return null;
        for(UsbSerialDriver driver:drivers){
            DeviceSchema dv = new DeviceSchema(driver.getDevice().getDeviceName(), driver.getDevice().getDeviceId(), "");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dv.setManufacture(driver.getDevice().toString());
            }
            deviceList.add(dv);
        }

        return deviceList;
    }

    public void callPopupForSalesMan() {
        EmployeeDBAdapter userDB = new EmployeeDBAdapter(this);
        userDB.open();
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
        userDB.close();
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
            CurrencyReturnsCustomDialogActivity.REQUEST_CURRENCY_RETURN_ACTIVITY_CODE=false;
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setCustomer(Customer customer) {
        clubAdapter.open();
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
        clubAdapter.close();
    }

    public double getCurrencyRate(String currencyType){
        double currencyRate=1;
        for (int i=0;i<currenciesList.size();i++){
            if(currenciesList.get(i).getCurrencyCode().equals(currencyType)) {
                currencyRate=currenciesList.get(i).getRate();

            }
        }
        return currencyRate;
    }

    public void calculateOfferForOrderDetails(OrderDetails orderDetails) throws JSONException {
        Log.d("validOffer",validOffer.toString());
        Log.d("orderDetailsoffer",orderDetails.toString());
        if (orderDetails.getProduct()!=null ){
            if(String.valueOf(orderDetails.getProduct().getOfferId())!= null){
                if (!String.valueOf(orderDetails.getProduct().getOfferId()).isEmpty()){
                    if (validOffer!=null){
                        for(int i=0;i<validOffer.size();i++) {
                            if (orderDetails.getProduct().getOfferId() == validOffer.get(i).getOfferId()) {
                                //execute offer
                                OfferController.execute(validOffer.get(i), orderDetails, this, SESSION._ORDER_DETAILES);
                            }
                        }
                    }
                    else {
                        Log.d("validOffer","null");
                    }
                }
                else {
                    Log.d("orderDetialsGetOffer","empty");
                }

            }
            else {
                Log.d("orderDetialsGetOffer","null");
            }
        }
        else {
            Log.d("orderDetails","null");
        }

        //getOfferCategoryForProduct
        /**    OfferCategoryDbAdapter offerCategoryDbAdapter = new OfferCategoryDbAdapter(SalesCartActivity.this);
         offerCategoryDbAdapter.open();
         List<OfferCategory>offerCategoryList=offerCategoryDbAdapter.getOfferCategoryByProductId(orderDetails.getProductId());
         //test if offerCategory be in active offer
         for(int i=0 ; i<validOffer.size();i++){
         Offer offer = validOffer.get(i);
         JSONObject offerRules =offer.getRules();
         JSONArray offerCategoryListInOffer = offerRules.getJSONArray("offerCategoryList");

         for(int a=0;a<offerCategoryListInOffer.length();a++){
         for (int b=0;b<offerCategoryList.size();b++){
         if(Long.parseLong((String) offerCategoryListInOffer.get(a))==offerCategoryList.get(b).getOfferCategoryId()){
         OrderDetails o=orderDetails;
         if (OfferController.check(offer, o)) {
         o= OfferController.execute(offer, o,this,SESSION._ORDER_DETAILES);
         return o;
         }
         }
         }
         }
         }
         /**List<Offer> offerList = OfferController.getOffersForResource(orderDetails.getProductId(),orderDetails.getProduct().getSku(),orderDetails.getProduct().getCategoryId(), getApplicationContext());
         orderDetails.offerList = offerList;
         if (offerList != null) {
         OrderDetails o=orderDetails;
         for (int i =0; i<offerList.size(); i++) {
         if (OfferController.check(offerList.get(i), o)) {
         o= OfferController.execute(offerList.get(i), o,this);
         }
         }
         return o;
         }**/
        return;
    }
    private ZReport getLastZReport() {
        ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(this);
        zReportDBAdapter.open();
        ZReport zReport = null;
        try {
            zReport = zReportDBAdapter.getLastRow();
            Log.d("zReportget",zReport.toString());
        } catch (Exception e) {
            Log.w("Z Report ", e.getMessage());
        }
        zReportDBAdapter.close();
        return zReport;
    }

    private OpiningReport getLastAReport() {
        OpiningReportDBAdapter aReportDBAdapter = new OpiningReportDBAdapter(this);
        aReportDBAdapter.open();
        OpiningReport aReport = null;

        try {
            aReport = aReportDBAdapter.getLastRow();

        } catch (Exception e) {
            Log.w("A Report ", e.getMessage());
        }

        aReportDBAdapter.close();
        return aReport;
    }
    private ClosingReport getLastClosingReport() {
        ClosingReportDBAdapter closingReportDBAdapter = new ClosingReportDBAdapter(this);
        closingReportDBAdapter.open();
        ClosingReport closingReport = null;

        try {
            closingReport = closingReportDBAdapter.getLastRow();

        } catch (Exception e) {
            Log.w("A Report ", e.getMessage());
        }

        closingReportDBAdapter.close();
        return closingReport;
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
    private void pdfLoadImages(final byte[] data)
    {
        bitmapList=new ArrayList<>();
        try
        {
            // run async
            new AsyncTask<Void, Void, String>()
            {
                // create and show a progress dialog
                ProgressDialog progressDialog = ProgressDialog.show(SalesCartActivity.this, "", "Opening...");

                @Override
                protected void onPostExecute(String html)
                {
                    Log.d("bitmapsize2222",bitmapList.size()+"");
                    newBitmap= combineImageIntoOne(bitmapList);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //after async close progress dialog
                    progressDialog.dismiss();
                    //load the html in the webview
                    //  wv.loadDataWithBaseURL("", html, "text/html","UTF-8", "");
                }

                @Override
                protected String doInBackground(Void... params)
                {
                    try
                    {
                        //create pdf document object from bytes
                        ByteBuffer bb = ByteBuffer.NEW(data);
                        PDFFile pdf = new PDFFile(bb);
                        //Get the first page from the pdf doc
                        PDFPage PDFpage = pdf.getPage(1, true);
                        //create a scaling value according to the WebView Width
                        final float scale = 800 / PDFpage.getWidth() * 0.80f;
                        //convert the page into a bitmap with a scaling value
                        Bitmap page = PDFpage.getImage((int)(PDFpage.getWidth() * scale), (int)(PDFpage.getHeight() * scale), null, true, true);
                        //save the bitmap to a byte array
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        page.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        stream.reset();
                        //convert the byte array to a base64 string
                        String base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        //create the html + add the first image to the html
                        String html = "<!DOCTYPE html><html><body bgcolor=\"#ffffff\"><img src=\"data:image/png;base64," + base64 + "\" hspace=328 vspace=4><br>";                        //loop though the rest of the pages and repeat the above
                        for(int i = 1; i <= pdf.getNumPages(); i++)
                        {
                            PDFpage = pdf.getPage(i, true);
                            page = PDFpage.getImage((int)(PDFpage.getWidth() * scale), (int)(PDFpage.getHeight() * scale), null, true, true);
                            page.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            bitmapList.add(page);
                            byteArray = stream.toByteArray();
                            stream.reset();
                            base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                            html += "<img src=\"data:image/png;base64,"+base64+"\" hspace=10 vspace=10><br>";
                        }
                        stream.close();
                        html += "</body></html>";
                        Log.d("mmmmmmm",bitmapList.size()+"");

                        return html;
                    }
                    catch (Exception e)
                    {
                        Log.d("error", e.toString());
                    }
                    return null;
                }
            }.execute();
            System.gc();// run GC
        }
        catch (Exception e)
        {
            Log.d("error", e.toString());
        }
    }
    private Bitmap combineImageIntoOne(ArrayList<Bitmap> bitmap) {
        int w = 0, h = 0;
        for (int i = 0; i < bitmap.size(); i++) {
            if (i < bitmap.size() - 1) {
                w = bitmap.get(i).getWidth() > bitmap.get(i + 1).getWidth() ? bitmap.get(i).getWidth() : bitmap.get(i + 1).getWidth();
            }
            h += bitmap.get(i).getHeight();
        }

        Bitmap temp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(temp);
        int top = 0;
        for (int i = 0; i < bitmap.size(); i++) {
            Log.d("HTML", "Combine: "+i+"/"+bitmap.size()+1);

            top = (i == 0 ? 0 : top+bitmap.get(i).getHeight());
            canvas.drawBitmap(bitmap.get(i), 0f, top, null);
        }
        return temp;
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

         }catch (IOException e) {
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

class createReplacementInvoice extends AsyncTask<Order,Void,String> {
    InvoiceImg invoiceImg;
    Context context;
    public createReplacementInvoice(Context context) {
        this.context =context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
         invoiceImg = new InvoiceImg(SalesCartActivity.context);
    }

    @Override
    protected String doInBackground(Order... args) {
        Order lastOrder= args[0];
        SETTINGS.copyInvoiceBitMap =invoiceImg.replacmentNote(lastOrder,false);
        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Intent i = new Intent(SalesCartActivity.context, SalesHistoryCopySales.class);
        // SETTINGS.copyInvoiceBitMap =invoiceImg.replacmentNote(lastOrder,false);
      context.startActivity(i);
        //endregion
    }

}
