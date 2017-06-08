package com.pos.leaders.leaderspossystem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
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
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.CreditCard.CreditCardActivity;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.AReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ChecksDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.DepartmentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductOfferDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.SaleDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.AReport;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.Department;
import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.Sale;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Printer.InvoiceImg;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.CashActivity;
import com.pos.leaders.leaderspossystem.Tools.ProductCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SaleDetailsListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    String transID="";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;//
    private NavigationView navigationView;

    ImageButton btnPauseSale, btnResumeSale, btnPercentProduct, btnLastSales;

    Button btnCancel, btnCash, btnCreditCard, btnOtherWays;
    TextView tvTotalPrice, tvTotalSaved;
    EditText etSearch;
    Button btnDone;
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
    SaleDBAdapter saleDBAdapter;
    OrderDBAdapter orderDBAdapter;
    View prseedButtonDepartments;
    List<Product> productList;
    List<Product> All_productsList;

    List<Offer> offersList;
    ProductCatalogGridViewAdapter productCatalogGridViewAdapter;
    //ProductCatalogListViewAdapter productCatalogListViewAdapter;

    String barcodeScanned = "";

    ListView lvOrder;
    static Sale sale;
    SaleDetailsListViewAdapter saleDetailsListViewAdapter;
    View selectedIteminCartList;
    Order selectedOrderOnCart =null;

    private boolean isGrid = true;
    double saleTotalPrice = 0.0;
    double totalSaved = 0.0;


    boolean userScrolled=false;
    int productLoadItemOffset=0;
    int productCountLoad=60;

    POSSDK pos;

    LinearLayout ll;
    ImageView imv;


    private String touchPadPressed = "";
    private boolean enableBackButton = true;


    //Drw drw=null;
    //String devicePath="/dev/ttySAC1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.mainActivity_drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.mainActivity_navigationView);
        //((MenuItem)(navigationView.findViewById(R.id.menuItem_ZReport))).setTitle("Z"+getString(R.string.reports));

        //region Init
        btnPauseSale = (ImageButton) findViewById(R.id.mainActivity_BTNGeneralProduct);
        btnResumeSale = (ImageButton) findViewById(R.id.mainActivity_BTNMultProduct);
        btnPercentProduct = (ImageButton) findViewById(R.id.mainActivity_BTNPercentProduct);
        btnLastSales = (ImageButton) findViewById(R.id.mainActivity_BTNLastSales);
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
        btnGrid = (ImageButton) findViewById(R.id.mainActivity_btnGrid);
        btnList = (ImageButton) findViewById(R.id.mainActivity_btnList);
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
        ll.setVisibility(View.GONE);
        imv = (ImageView) findViewById(R.id.imageView6);
        imv.setVisibility(View.GONE);


        llDepartments = (LinearLayout) findViewById(R.id.mainActivity_LLDepartment);
        departmentDBAdapter = new DepartmentDBAdapter(this);
        productDBAdapter = new ProductDBAdapter(this);
        productDBAdapter.open();
        departmentDBAdapter.open();

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
              /*  offersList=offerDBAdapter.getAllOffers();
                for(int i=0;i<offersList.size();i++){

                    //vheck it
                    //List<Product> lp=productOfferDBAdapter.getAllProductOffer(offersList.get(i));
                    //offersList.get(i).setProducts(lp);
                }*/
                return null;
            }
        }.execute();


        //endregion

        //region Grid List Button

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


        //endregion

        //region Clear Cart

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        gvProducts.setNumColumns(5);

        gvProducts.setAdapter(productCatalogGridViewAdapter);
        lvProducts.setAdapter(productCatalogGridViewAdapter);

        //region Departments
        Button btAll = new Button(this);
        btAll.setId(0);
        btAll.setText(getResources().getText(R.string.all));
        btAll.setTextAppearance(this, R.style.TextAppearance);
        btAll.setPressed(true);
        btAll.setBackground(getResources().getDrawable(R.drawable.btn_secondary));
        llDepartments.addView(btAll);
        prseedButtonDepartments = btAll;
        btAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productLoadItemOffset = 0;
                prseedButtonDepartments.setPressed(false);
                prseedButtonDepartments.setBackgroundColor(getBaseContext().getResources().getColor(R.color.secondaryColor));
                v.setPressed(true);
                v.setBackgroundColor(getBaseContext().getResources().getColor(R.color.secondaryDarkColor));

                prseedButtonDepartments = v;

                productList = productDBAdapter.getTopProducts(productLoadItemOffset, productCountLoad);
                All_productsList = productList;
                productCatalogGridViewAdapter = new ProductCatalogGridViewAdapter(getApplicationContext(), productList);
                gvProducts.setAdapter(productCatalogGridViewAdapter);
                lvProducts.setAdapter(productCatalogGridViewAdapter);
            }
        });


        for (Department d : departmentDBAdapter.getAllDepartments()) {
            Button bt = new Button(this);
            bt.setId(d.getId());
            bt.setPadding(15, 0, 15, 0);
            bt.setText(d.getName());
            bt.setTextAppearance(this, R.style.TextAppearance);
            bt.setBackground(getResources().getDrawable(R.drawable.btn_secondary));
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productLoadItemOffset = 0;
                    prseedButtonDepartments.setPressed(false);
                    prseedButtonDepartments.setBackgroundColor(getBaseContext().getResources().getColor(R.color.secondaryColor));
                    v.setPressed(true);
                    v.setBackgroundColor(getBaseContext().getResources().getColor(R.color.secondaryDarkColor));

                    prseedButtonDepartments = v;
                    productList = productDBAdapter.getAllProductsByDepartment(v.getId(), productLoadItemOffset, productCountLoad);
                    All_productsList = productList;
                    productCatalogGridViewAdapter = new ProductCatalogGridViewAdapter(getApplicationContext(), productList);
                    gvProducts.setAdapter(productCatalogGridViewAdapter);
                    lvProducts.setAdapter(productCatalogGridViewAdapter);
                }
            });


            View line = new View(this);
            line.setBackgroundColor(getResources().getColor(R.color.backgroundColor));
            line.setMinimumWidth(1);
            llDepartments.addView(line);
            llDepartments.addView(bt);

            //add line
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

        btnDone = (Button) findViewById(R.id.mainActivity_btnDone);
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

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //region Order List View


        if (SESSION._SALE != null) {
            sale = new Sale(SESSION._USER.getId(), new Date(), 0, false, 0, 0);
        } else {
            SESSION._SALE = new Sale(SESSION._USER.getId(), new Date(), 0, false, 0, 0);
        }

        if (SESSION._ORDERS != null) {
            lvOrder.setFocusable(false);
            calculateTotalPrice();
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

        calculateTotalPrice();

        //region Payment

        //region Cash

        btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CashActivity.class);
                intent.putExtra("_Price", saleTotalPrice);
                startActivityForResult(intent, REQUEST_CASH_ACTIVITY_CODE);
            }
        });

        //endregion


        //region Credit Card

        btnCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*
                Intent i = new Intent(MainActivity.this, ChecksActivity.class);
                startActivity(i);
*/

                final Context c = MainActivity.this;
                new AlertDialog.Builder(c)
                        .setTitle(c.getResources().getString(R.string.clearCartAlertTitle))
                        //.setMessage(c.getResources().getString(R.string.clearCartAlertMessage))
                        .setPositiveButton(c.getResources().getString(R.string.by_card), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(c, CreditCardActivity.class);
                                intent.putExtra("_Price", saleTotalPrice);
                                intent.putExtra(CreditCardActivity.LEADERS_POS_CREDIT_CARD_TYPE, CreditCardActivity.LEADERS_POS_CREDIT_CARD_ACTIVITY_BY_PASS_CARD);
                                startActivityForResult(intent, REQUEST_CREDIT_CARD_ACTIVITY_CODE);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton(c.getResources().getString(R.string.by_phone), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(c, CreditCardActivity.class);
                                intent.putExtra("_Price", saleTotalPrice);
                                intent.putExtra(CreditCardActivity.LEADERS_POS_CREDIT_CARD_TYPE, CreditCardActivity.LEADERS_POS_CREDIT_CARD_ACTIVITY_BY_PHONE);
                                startActivityForResult(intent ,REQUEST_CREDIT_CARD_ACTIVITY_CODE);
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.sym_contact_card)
                        .show();
            }
        });

        //endregion


        //region Other Way

        btnOtherWays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ChecksActivity.class);
                intent.putExtra("_Price", saleTotalPrice);
                startActivityForResult(intent, REQUEST_CHECKS_ACTIVITY_CODE);
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

        btnLastSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SalesManagementActivity.class);
                startActivity(i);
            }
        });

        //endregion last sale button

        //endregion

        //region navigation menu items

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
                    case R.id.menuItem_Offers:
                        SESSION._TEMPOFFERPRODUCTS = null;
                        intent = new Intent(MainActivity.this, OfferActivity.class);
                        break;
                    case R.id.menuItem_Reports:
                        intent = new Intent(MainActivity.this, ReportsManagementActivity.class);
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
                }
                startActivity(intent);
                return false;
            }
        });
        //endregion

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
        if (extras != null) {
            String str = extras.getString(LogInActivity.LEADPOS_MAKE_A_REPORT);
            if (str.equals(LogInActivity.LEADPOS_MAKE_A_REPORT)) {
                extras.clear();
                createAReport();
                //make shure the user can not do any thinks if the report a does not created
                //get the last a report and last z report check if the a report
            }
        }

    }

    private void createAReport() {
        final AReport _aReport = new AReport();
        ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(this);
        zReportDBAdapter.open();
        ZReport zReport = null;
        try {
            zReport = zReportDBAdapter.getLastRow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        zReportDBAdapter.close();

        AReportDBAdapter aReportDBAdapter = new AReportDBAdapter(this);
        aReportDBAdapter.open();
        AReport aReport = null;

        try {
            aReport = aReportDBAdapter.getLastRow();
        } catch (Exception e) {
            e.printStackTrace();
        }

        aReportDBAdapter.close();


        if (aReport != null && zReport != null) {
            _aReport.setByUserID(SESSION._USER.getId());
            _aReport.setCreationDate(new Date().getTime());


            if (aReport.getLastZReportID() == (int) zReport.getId()) {
                //its have a report
            } else {
                _aReport.setLastZReportID((int) zReport.getId());
                _aReport.setLastSaleID((int)zReport.getEndSaleId());
                ShowAReportDialog(_aReport);
            }
        } else {
            _aReport.setLastZReportID(-1);
            _aReport.setLastSaleID(-1);
            ShowAReportDialog(_aReport);
        }
    }

    private void ShowAReportDialog(final AReport aReport){
        //there is no a report after z report
        enableBackButton = false;
        final Dialog discountDialog = new Dialog(MainActivity.this);
        discountDialog.setTitle(R.string.opening_report);
        discountDialog.setContentView(R.layout.cash_payment_dialog);
        discountDialog.setCancelable(false);

        final Button btOK = (Button) discountDialog.findViewById(R.id.cashPaymentDialog_BTOk);
        //btOK.setBackground(getBaseContext().getResources().getDrawable(R.drawable.btn_primary));
        final Button btCancel = (Button) discountDialog.findViewById(R.id.cashPaymentDialog_BTCancel);
        btCancel.setVisibility(View.GONE);
        final EditText et = (EditText) discountDialog.findViewById(R.id.cashPaymentDialog_TECash);
        final Switch sw = (Switch) discountDialog.findViewById(R.id.cashPaymentDialog_SwitchProportion);
        sw.setVisibility(View.GONE);
        discountDialog.setCanceledOnTouchOutside(false);


        et.setHint(R.string.amount);
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
                if (!str.equals("")) {
                    aReport.setAmount(Double.parseDouble(str));
                    AReportDBAdapter aReportDBAdapter = new AReportDBAdapter(MainActivity.this);
                    aReportDBAdapter.open();
                    aReportDBAdapter.insertEntry(aReport);
                    aReportDBAdapter.close();
                    discountDialog.cancel();
                }
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //discountDialog.cancel();
            }
        });
        discountDialog.show();
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
        view.getId();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeOrderItemSelection(){
        saleDetailsListViewAdapter.setSelected(-1);
        if(selectedIteminCartList!=null) {
            selectedIteminCartList.findViewById(R.id.rowSaleDetails_LLMethods).setVisibility(View.GONE);
            selectedIteminCartList.setBackgroundColor(getResources().getColor(R.color.white));
            selectedOrderOnCart=null;
        }
    }

    public void clearCart() {
        SESSION._Rest();
        saleDetailsListViewAdapter = new SaleDetailsListViewAdapter(getApplicationContext(), R.layout.list_adapter_row_main_screen_sales_details, SESSION._ORDERS);
        lvOrder.setAdapter(saleDetailsListViewAdapter);
        calculateTotalPrice();
    }

    public void resumeSale(Sale s){
        if (SESSION._ORDERS.size() != 0) {
            Sale sa = new Sale(SESSION._SALE);
            sa.setOrders(SESSION._ORDERS);
            if (SESSION._SALES == null)
                SESSION._SALES = new ArrayList<Pair<Integer,Sale>>();
            SESSION._SALES.add(new Pair<>(++SESSION.TEMP_NUMBER,sa));
            clearCart();
        }

        SESSION._SALE = new Sale(s);
        SESSION._ORDERS = s.getOrders();
        saleDetailsListViewAdapter = new SaleDetailsListViewAdapter(getApplicationContext(), R.layout.list_adapter_row_main_screen_sales_details, SESSION._ORDERS);
        lvOrder.setAdapter(saleDetailsListViewAdapter);
        refreshCart();
    }

    protected void calculateTotalPrice() {
        saleTotalPrice = 0;
        double SaleOriginalityPrice=0;
        for (Order o : SESSION._ORDERS) {
            saleTotalPrice += o.getItemTotalPrice();
            SaleOriginalityPrice += (o.getOriginal_price() * o.getCount());
        }
        totalSaved =(SaleOriginalityPrice-saleTotalPrice);
        SESSION._SALE.setTotalPrice(saleTotalPrice);
        tvTotalSaved.setText(String.format(new Locale("en"),"%.2f",(totalSaved))+" "+ getString(R.string.ins));
        tvTotalPrice.setText(String.format(new Locale("en"),"%.2f",saleTotalPrice) + " " + getString(R.string.ins));
    }

    private void removeFromCart(int index) {
        SESSION._ORDERS.remove(index);
        saleDetailsListViewAdapter.setSelected(-1);
        refreshCart();
    }

    private void addToCart(Product p) {
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
        calculateTotalPrice();
    }

    private boolean getOffers(){
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
    }

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
        Toast.makeText(MainActivity.this, "scanned code is: " + barcodeScanned, Toast.LENGTH_LONG).show();
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

    private void printAndOpenCashBox(String mainAns, final String mainMer, final String mainCli) {
        final POSInterfaceAPI posInterfaceAPI=new POSUSBAPI(MainActivity.this);
       // final UsbPrinter printer = new UsbPrinter(1155, 30016);

        final ProgressDialog dialog=new ProgressDialog(MainActivity.this);
        dialog.setTitle(getBaseContext().getString(R.string.wait_for_finish_printing));

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                dialog.show();
                ////Hebrew 15 Windows-1255

                SESSION._SALE.setTotalPrice(saleTotalPrice);
                int i = posInterfaceAPI.OpenDevice();
                pos=new POSSDK(posInterfaceAPI);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                pos.systemFeedLine(2);
                pos.systemCutPaper(66,0);
                pos.cashdrawerOpen(0,20,20);

                posInterfaceAPI.CloseDevice();
                dialog.cancel();
                clearCart();
            }

            @Override
            protected Void doInBackground(Void... params) {
                InvoiceImg invoiceImg = new InvoiceImg(MainActivity.this);
                if (SESSION._SALE.getPayment().getPaymentWay().equals(CREDIT_CARD)){
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CREDIT_CARD_ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {
                SESSION._SALE.setTotalPaid(SESSION._SALE.getTotalPrice());
                saleDBAdapter = new SaleDBAdapter(MainActivity.this);
                saleDBAdapter.open();
                int saleID = saleDBAdapter.insertEntry(SESSION._SALE);
                saleDBAdapter.close();

                orderDBAdapter = new OrderDBAdapter(MainActivity.this);
                orderDBAdapter.open();
                SESSION._SALE.setId(saleID);
                for (Order o : SESSION._ORDERS) {
                    orderDBAdapter.insertEntry(o.getProductId(),o.getCount(),o.getUserOffer(),saleID,o.getPrice(),o.getOriginal_price(),o.getDiscount());
                }
                orderDBAdapter.close();

                SESSION._SALE.setOrders(SESSION._ORDERS);
                SESSION._SALE.setUser(SESSION._USER);

                PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(MainActivity.this);
                paymentDBAdapter.open();

                int paymentID = paymentDBAdapter.insertEntry(CREDIT_CARD, saleTotalPrice, saleID);
                paymentDBAdapter.close();

                Payment payment = new Payment(paymentID, CREDIT_CARD, saleTotalPrice, saleID);
                SESSION._SALE.setPayment(payment);

                printAndOpenCashBox(data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY),
                        data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_MerchantNote),
                        data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote));

                //get the invoice plugin
                //print invoice
                Log.w("mainAns", data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY));
                Log.w("mainMer", data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_MerchantNote));
                Log.w("mainCli", data.getStringExtra(CreditCardActivity.LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote));

                return;
            }
            else if (resultCode == RESULT_CANCELED) {
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
                saleDBAdapter=new SaleDBAdapter(MainActivity.this);
                saleDBAdapter.open();
                int saleID=saleDBAdapter.insertEntry(SESSION._SALE);
                saleDBAdapter.close();

                orderDBAdapter=new OrderDBAdapter(MainActivity.this);
                orderDBAdapter.open();
                SESSION._SALE.setId(saleID);
                for(Order o:SESSION._ORDERS){
                    orderDBAdapter.insertEntry(o.getProductId(),o.getCount(),o.getUserOffer(),saleID,o.getPrice(),o.getOriginal_price(),o.getDiscount());
                }
                orderDBAdapter.close();

                PaymentDBAdapter paymentDBAdapter=new PaymentDBAdapter(this);
                paymentDBAdapter.open();

                int paymentID=paymentDBAdapter.insertEntry(CHECKS,saleTotalPrice,saleID);
                paymentDBAdapter.close();

                Payment payment = new Payment(paymentID, CHECKS, saleTotalPrice, saleID);
                SESSION._SALE.setPayment(payment);

                ChecksDBAdapter checksDBAdapter = new ChecksDBAdapter(this);
                checksDBAdapter.open();
                for(Check check:SESSION._CHECKS_HOLDER){
                    checksDBAdapter.insertEntry(check.getCheckNum(), check.getBankNum(), check.getBranchNum(), check.getAccountNum(), check.getAmount(), saleID);
                }
                checksDBAdapter.close();


                printAndOpenCashBox("","","");
                return;
            }
        }
        if(requestCode== REQUEST_CASH_ACTIVITY_CODE){
            if(resultCode==RESULT_OK){
                final float result = data.getFloatExtra(CashActivity.LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY,0.0f);
                SESSION._SALE.setTotalPaid(result);

                saleDBAdapter=new SaleDBAdapter(MainActivity.this);
                orderDBAdapter=new OrderDBAdapter(MainActivity.this);
                PaymentDBAdapter paymentDBAdapter=new PaymentDBAdapter(this);

                    saleDBAdapter.open();


                    int saleID=saleDBAdapter.insertEntry(SESSION._SALE);
                    saleDBAdapter.close();


                    orderDBAdapter.open();
                    SESSION._SALE.setId(saleID);

                    for (Order o : SESSION._ORDERS) {
                        orderDBAdapter.insertEntry(o.getProductId(), o.getCount(), o.getUserOffer(), saleID, o.getPrice(), o.getOriginal_price(), o.getDiscount());
                    }
                    orderDBAdapter.close();


                    paymentDBAdapter.open();

                    int paymentID=paymentDBAdapter.insertEntry(CASH,saleTotalPrice,saleID);
                    Payment payment = new Payment(paymentID, CASH, saleTotalPrice, saleID);

                    SESSION._SALE.setPayment(payment);

                    paymentDBAdapter.close();

                printAndOpenCashBox("","","");
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

    @Override
    public void onBackPressed() {
        if(!enableBackButton){
            //stop all moves
        }
        //do not move from here :)
    }
}