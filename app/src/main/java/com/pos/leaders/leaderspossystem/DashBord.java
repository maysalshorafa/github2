package com.pos.leaders.leaderspossystem;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.CustomerAndClub.Customer;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClosingReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.LincessDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PermissionsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PosSettingDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ScheduleWorkersDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.SettingsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportCountDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Elements.IButton;
import com.pos.leaders.leaderspossystem.Models.ClosingReport;
import com.pos.leaders.leaderspossystem.Models.Currency.Currency;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.Employee;
import com.pos.leaders.leaderspossystem.Models.OpiningReport;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Permission.Permissions;
import com.pos.leaders.leaderspossystem.Models.PosSetting;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Models.ZReportCount;
import com.pos.leaders.leaderspossystem.Printer.HPRT_TP805;
import com.pos.leaders.leaderspossystem.Printer.SUNMI_T1.AidlUtil;
import com.pos.leaders.leaderspossystem.Settings.SettingsActivity;
import com.pos.leaders.leaderspossystem.SettingsTab.SettingsTab;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.CompanyStatus;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.InternetStatus;
import com.pos.leaders.leaderspossystem.Tools.PrinterType;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.ThisApp;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.Tools.ViewDialogLincess;
import com.pos.leaders.leaderspossystem.syncposservice.DBHelper.Broker;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;
import com.pos.leaders.leaderspossystem.syncposservice.Model.BrokerMessage;
import com.pos.leaders.leaderspossystem.syncposservice.Service.SyncMessage;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sunmi.aidl.MSCardService;

import net.sf.andpdf.nio.ByteBuffer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.pos.leaders.leaderspossystem.SetupNewPOSOnlineActivity.BO_CORE_ACCESS_AUTH;
import static com.pos.leaders.leaderspossystem.SetupNewPOSOnlineActivity.BO_CORE_ACCESS_TOKEN;
import static com.pos.leaders.leaderspossystem.SetupNewPOSOnlineActivity.context;

public class DashBord extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private boolean enableBackButton = true;
    IButton salesCart, report, product, category, closingReport, customerClub, logOut, inventoryManagement, settings , schedule_workers;
    IButton users;
    IButton btZReport, btAReport;
    OpiningReportDBAdapter aReportDBAdapter;
    Employee user = new Employee();
    EmployeeDBAdapter userDBAdapter;
    ArrayList<Integer> permissions_name;
    ArrayList<Permissions> permissions = new ArrayList<Permissions>();
    ScheduleWorkersDBAdapter scheduleWorkersDBAdapter;
    Intent i;
    Order lastSale;
    Currency fCurrency;
    Currency sCurrency;
    Currency tCurrency;
    Currency forthCurrency;
    double firstCurrencyInDefaultValue = 0, secondCurrencyInDefaultValue = 0, thirdCurrencyInDefaultValue = 0, forthCurrencyInDefaultValue = 0;
    double aReportTotalAmount = 0;
    private MSCardService sendservice;
    long aReportId;
    double totalZReportAmount =0;
    boolean PRINTER_STATE=false;
    boolean fromDashBoard=false;
    int positionItem;
    private MessageTransmit messageTransmit = new MessageTransmit();
    public static ArrayList<Bitmap> bitmapList=new ArrayList<Bitmap>();
    Bitmap newBitmap =null;
    Context context=DashBord.this;

    ServiceConnection serviceConnection;
    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        try {
            TitleBar.removeTitleBard();
        } catch (IllegalArgumentException iae) {

        }
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_temp_dash_bord);
        ThisApp.setCurrentActivity(this);

        if (SyncMessage.isConnected(this)) {
            SESSION.internetStatus = InternetStatus.CONNECTED;
        } else {
            SESSION.internetStatus = InternetStatus.ERROR;
        }

        //run MSR Service
        Intent intent = new Intent();
        intent.setPackage("com.sunmi.mscardservice");
        intent.setAction("com.sunmi.mainservice.MainService");

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                sendservice = MSCardService.Stub.asInterface(service);
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {}
        };

        try {
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        } catch (Exception e) {
            Log.e("Sunmi MSC ", e.getMessage());
        }
        //sendable.run();

       AccessToken accessToken = new AccessToken(this);
       accessToken.execute(this);


        //load pos id from shared file
        SharedPreferences sharedpreferences = getSharedPreferences(BO_CORE_ACCESS_AUTH, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(MessageKey.PosId)) {
            int posID = sharedpreferences.getInt(MessageKey.syncNumber, 1);
            SESSION.POS_ID_NUMBER = posID;
        }
        //load token from shared file
        sharedpreferences = getSharedPreferences(BO_CORE_ACCESS_TOKEN, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(MessageKey.Token)) {
            String token = sharedpreferences.getString(MessageKey.Token, null);
            SESSION.token = token;
        }


        //end
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //   permissions_name = bundle.getString("permissions_name");
            permissions_name = getIntent().getIntegerArrayListExtra("permissions_name");
        }


        PermissionsDBAdapter permissionsDBAdapter = new PermissionsDBAdapter(this);
        permissionsDBAdapter.open();
        for (int p : permissions_name) {
            Permissions per = permissionsDBAdapter.getPermission(p);
            if (per != null) {
                permissions.add(per);
            }
        }
        permissionsDBAdapter.close();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    /*    try {
            updateCurrency();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        salesCart = (IButton) findViewById(R.id.mainScreen);
        btAReport = (IButton) findViewById(R.id.dashboard_btAreport);
        btZReport = (IButton) findViewById(R.id.dashboard_btZreport);
        report = (IButton) findViewById(R.id.report);
        product = (IButton) findViewById(R.id.product);
        category = (IButton) findViewById(R.id.department);
        inventoryManagement = (IButton) findViewById(R.id.dashboard_btInventory);
        users = (IButton) findViewById(R.id.users);
        schedule_workers = (IButton) findViewById(R.id.schedule_workers);
        closingReport = (IButton) findViewById(R.id.closingReport);
        // logOut = (IButton) findViewById(R.id.logOut);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        customerClub = (IButton) findViewById(R.id.coustmerClub);
        settings = (IButton) findViewById(R.id.settings);
        checkLincess();
        TitleBar.setTitleBar(this);


      /*if(!SETTINGS.havePosSetting){
        checkSettings(SESSION.token);
           SETTINGS.havePosSetting=true;
       }*/

        /*CustomerDBAdapter customerDBAdapter = new CustomerDBAdapter(DashBord.this);
        customerDBAdapter.open();
        //    public Customer(long customerId, String firstName, String lastName, String gender, String email, String job, String phoneNumber, String street, boolean hide, int city, long club, String houseNumber, String postalCode, String country, String countryCode,double balance,CustomerType customerType,String customerCode,String customerIdentity,int branchId) {

        Broker broker=new Broker(DashBord.this);
        broker.open();
      List<BrokerMessage> brokerMessages= broker.getAllNotSyncedCommand();
        for (int i=0;i<brokerMessages.size();i++){
            broker.Synced(brokerMessages.get(i).getId());
        }*/

        //region customerName report button
        btAReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final OpiningReport _aReport = new OpiningReport();
                _aReport.setByUserID(SESSION._EMPLOYEE.getEmployeeId());
                _aReport.setCreatedAt(new Timestamp(System.currentTimeMillis()));

                OpiningReport aReport = getLastAReport();
                ZReport zReport = getLastZReport();


                if (aReport == null) {
                    _aReport.setLastZReportID(-1);
                    _aReport.setLastOrderId(-1);

                  checkCurrency(_aReport);
               //ShowAReportDialog(_aReport);
                } else {
                    if(zReport==null){
                        _aReport.setLastZReportID(-1);
                        _aReport.setLastOrderId(-1);

                    }else {
                        _aReport.setLastZReportID(zReport.getzReportId());
                        _aReport.setLastOrderId(zReport.getEndOrderId());
                    }

                checkCurrency(_aReport);
               //     ShowAReportDialog(_aReport);
                }

            }
        });
        //endregion customerName report button

        //region z report button
        btZReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PRINTER_STATE){
                    new android.support.v7.app.AlertDialog.Builder(DashBord.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                            .setTitle(getString(R.string.printer))
                            .setMessage(getString(R.string.please_connect_the_printer))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    new AlertDialog.Builder(DashBord.this)
                                            .setTitle(getString(R.string.create_z_report))
                                            .setMessage(getString(R.string.create_z_report_message))
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    fromDashBoard=true;

                                                    OpiningReport opiningReport = Util.getLastAReport(getApplicationContext());
                                                    ClosingReportDBAdapter closingReportDBAdapter =new ClosingReportDBAdapter(getApplicationContext());
                                                    closingReportDBAdapter.open();
                                                    ClosingReport closingReport1 = closingReportDBAdapter.getClosingReportByOpiningReportId(opiningReport.getOpiningReportId());
                                                    if(closingReport1!=null){
                                                   /*     ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(DashBord.this);
                                                        zReportDBAdapter.open();
                                                        ZReport lastZReport = Util.getLastZReport(getApplicationContext());
                                                        if (lastZReport == null) {
                                                            lastZReport = new ZReport();
                                                            lastZReport.setEndOrderId(0);
                                                        }
                                                        ZReport z = new ZReport(0,  new Timestamp(System.currentTimeMillis()), SESSION._EMPLOYEE, lastZReport.getEndOrderId() + 1, lastSale);
                                                        z.setByUser(SESSION._EMPLOYEE.getEmployeeId());
                                                        double amount = zReportDBAdapter.getZReportAmount(z.getStartOrderId(), z.getEndOrderId());
                                                        try {
                                                            totalZReportAmount=zReportDBAdapter.getLastRow().getTotalPosSales()+amount;
                                                        } catch (Exception e) {
                                                            totalZReportAmount=amount;
                                                            e.printStackTrace();
                                                        }
                                                        //sales Amount
                                                        z.setTotalAmount(amount);
                                                        z.setTotalSales(amount);
                                                        z.setInvoiceReceiptAmount(amount);
                                                        z.setTotalPosSales(totalZReportAmount);*/
                                                        ZReport zReport= Util.insertZReport(getApplicationContext());
                                                        btZReport.setEnabled(false);
                                                        if(zReport!=null){
                                                            if (needAReport()) {
                                                                btAReport.setEnabled(true);
                                                                btZReport.setEnabled(false);
                                                                closingReport.setEnabled(false);
                                                                salesCart.setEnabled(false);
                                                            } else {

                                                                if (lastSale == null) {
                                                                    // btZReport.setEnabled(false);
                                                                } else
                                                                    btZReport.setEnabled(true);
                                                                salesCart.setEnabled(true);
                                                            }
                                                            Intent i = new Intent(DashBord.this, ReportZDetailsActivity.class);
//                                                            /* i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_ID, zReport.getzReportId());
//                                                             i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_FORM, zReport.getStartOrderId());
//                                                             i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_TO, zReport.getEndOrderId());
//                                                             i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_TOTAL_AMOUNT,zReport.getTotalSales());
//                                                             i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_AMOUNT,zReport.getTotalAmount());
//                                                            i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_FROM_DASH_BOARD,true);*/
                                                              i.putExtra("ObjectZReport",zReport);

                                                          //  finish();
                                                             startActivity(i);

                                                        }
                                                    }else {
                                                        new AlertDialog.Builder(DashBord.this)
                                                                .setTitle(getString(R.string.create_closing_report))
                                                                .setMessage(getString(R.string.you_must_create_closing_report_before_z_report))
                                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        Intent i = new Intent(DashBord.this, ClosingReportActivity.class);
                                                                        startActivity(i);
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
                                                    closingReportDBAdapter.close();
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
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
                else{
                    new AlertDialog.Builder(DashBord.this)
                            .setTitle(getString(R.string.create_z_report))
                            .setMessage(getString(R.string.create_z_report_message))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    OpiningReport opiningReport = Util.getLastAReport(getApplicationContext());
                                    ClosingReportDBAdapter closingReportDBAdapter =new ClosingReportDBAdapter(getApplicationContext());
                                    closingReportDBAdapter.open();
                                    ClosingReport closingReport1 = closingReportDBAdapter.getClosingReportByOpiningReportId(opiningReport.getOpiningReportId());
                                    if(closingReport1!=null){
                                       /* ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(DashBord.this);
                                        zReportDBAdapter.open();
                                        ZReport lastZReport = Util.getLastZReport(getApplicationContext());

                                        if (lastZReport == null) {
                                            lastZReport = new ZReport();
                                            lastZReport.setEndOrderId(0);
                                        }
                                        ZReport z = new ZReport(0,  new Timestamp(System.currentTimeMillis()), SESSION._EMPLOYEE, lastZReport.getEndOrderId() + 1, lastSale);
                                        z.setByUser(SESSION._EMPLOYEE.getEmployeeId());
                                        double amount = zReportDBAdapter.getZReportAmount(z.getStartOrderId(), z.getEndOrderId());
                                        try {
                                            totalZReportAmount=zReportDBAdapter.getLastRow().getTotalPosSales()+amount;
                                        } catch (Exception e) {
                                            totalZReportAmount=amount;
                                            e.printStackTrace();
                                        }
                                        z.setInvoiceReceiptAmount(amount);
                                        z.setTotalSales(amount);
                                        z.setTotalAmount(amount);
                                        z.setTotalPosSales(totalZReportAmount);*/
                                        ZReport zReport= Util.insertZReport(getApplicationContext());
                                        btZReport.setEnabled(false);
                                        if(zReport!=null){
                                            if (needAReport()) {
                                                btAReport.setEnabled(true);
                                                btZReport.setEnabled(false);
                                                closingReport.setEnabled(false);
                                                salesCart.setEnabled(false);
                                            }


                                                if (lastSale == null) {
                                                    // btZReport.setEnabled(false);
                                                } else {
                                                    btZReport.setEnabled(true);
                                                    salesCart.setEnabled(true);
                                                }
                                            Intent i = new Intent(DashBord.this, ReportZDetailsActivity.class);
//                                                            /* i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_ID, zReport.getzReportId());
//                                                             i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_FORM, zReport.getStartOrderId());
//                                                             i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_TO, zReport.getEndOrderId());
//                                                             i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_TOTAL_AMOUNT,zReport.getTotalSales());
//                                                             i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_AMOUNT,zReport.getTotalAmount());
//                                                            i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_FROM_DASH_BOARD,true);*/
                                            i.putExtra("ObjectZReport",zReport);

                                            //  finish();
                                            startActivity(i);
                                        }

                                    }else {
                                        new AlertDialog.Builder(DashBord.this)
                                                .setTitle(getString(R.string.create_closing_report))
                                                .setMessage(getString(R.string.you_must_create_closing_report_before_z_report))
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent i = new Intent(DashBord.this, ClosingReportActivity.class);
                                                        startActivity(i);
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
                                    closingReportDBAdapter.close();
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
        //endregion z report button

        salesCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), SalesCartActivity.class);
                startActivity(i);
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), ReportsManagementActivity.class);
                startActivity(i);
            }
        });
        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), ProductCatalogActivity.class);
                startActivity(i);
            }
        });
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] items = {
                        getString(R.string.categorys),
                        getString(R.string.categorys_offer)
                };
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DashBord.this);
                builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent;
                        switch (item) {
                            case 0:
                                i = new Intent(getApplicationContext(), CategoryActivity.class);
                                startActivity(i);
                                break;
                            case 1:
                                i = new Intent(getApplicationContext(), CategoryOfferActivity.class);
                                startActivity(i);
                                break;
                        }
                    }
                });
                android.app.AlertDialog alert = builder.create();
                alert.show();

            }
        });
        closingReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), ClosingReportActivity.class);
                startActivity(i);
            }
        });
        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), EmployeeManagementActivity.class);
                i.putIntegerArrayListExtra("permissions_name", permissions_name);
                startActivity(i);
            }
        });
        schedule_workers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), ScheduleWorkersActivity.class);
                startActivity(i);
            }
        });
        inventoryManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(getApplicationContext(), InventoryManagementActivity.class);
                startActivity(i);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
            }
        });
        settings.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                i = new Intent(getApplicationContext(), SettingsTab.class);
                startActivity(i);
                return false;
            }
        });
        customerClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), Customer.class);
                i.putIntegerArrayListExtra("permissions_name", permissions_name);
                startActivity(i);
            }
        });


        report.setEnabled(false);
        product.setEnabled(false);
        schedule_workers.setEnabled(false);
        inventoryManagement.setEnabled(false);
        users.setEnabled(false);
        closingReport.setEnabled(false);
        category.setEnabled(false);
        category.setEnabled(false);
        customerClub.setEnabled(false);
        settings.setEnabled(false);
        btAReport.setEnabled(false);
        btZReport.setEnabled(false);
        salesCart.setEnabled(false);

        if(!DbHelper.DATABASE_ENABEL_POS_SETTING){
            checkSettings(SESSION.token);
            Log.d("DATABASE_ENABEL_POS_SETTING","DATABASE_ENABEL_POS_SETTING");

        }
    }
public void checkLincess(){

    LincessDBAdapter lincessDBAdapter=new LincessDBAdapter(this);
    lincessDBAdapter.open();
    lincessDBAdapter.GetLincess();
    if (SETTINGS.dueDate!=null){
    if (SETTINGS.statusLincess.equals(CONSTANT.INACTIVE))
    { String sDate1=SETTINGS.dueDate;
        String[] parts = sDate1.split("-");
        String dateYear=new SimpleDateFormat("yyyy", Locale.US).format(new Date());
        String dateMonth=new SimpleDateFormat("MM", Locale.US).format(new Date());
        String dateDay=new SimpleDateFormat("dd", Locale.US).format(new Date());
        if (dateYear.equals(parts[0])){
            if (dateMonth.equals(parts[1])){
                if(dateDay.equals(parts[2])){
                    report.setEnabled(false);
                    product.setEnabled(false);
                    schedule_workers.setEnabled(false);
                    inventoryManagement.setEnabled(false);
                    users.setEnabled(false);
                    closingReport.setEnabled(false);
                    category.setEnabled(false);
                    category.setEnabled(false);
                    customerClub.setEnabled(false);
                    settings.setEnabled(false);
                    btAReport.setEnabled(false);
                    btZReport.setEnabled(false);
                    salesCart.setEnabled(false);
                    ViewDialogLincess alert = new ViewDialogLincess();
                    alert.showDialog(ThisApp.getCurrentActivity(), getResources().getString(R.string.text_dialog_lincess));
                }

            }}
        else{
        report.setEnabled(false);
        product.setEnabled(false);
        schedule_workers.setEnabled(false);
        inventoryManagement.setEnabled(false);
        users.setEnabled(false);
        closingReport.setEnabled(false);
        category.setEnabled(false);
        category.setEnabled(false);
        customerClub.setEnabled(false);
        settings.setEnabled(false);
        btAReport.setEnabled(false);
        btZReport.setEnabled(false);
        salesCart.setEnabled(false);}

    }
else if (SETTINGS.statusLincess.equals(CONSTANT.ACTIVE)){

        String sDate1=SETTINGS.dueDate;
        String[] parts = sDate1.split("-");

        String dateYear=new SimpleDateFormat("yyyy", Locale.US).format(new Date());
        String dateMonth=new SimpleDateFormat("MM", Locale.US).format(new Date());
        String dateDay=new SimpleDateFormat("dd", Locale.US).format(new Date());
        if (dateYear.equals(parts[0])){
            if (dateMonth.equals(parts[1])){
                int day1=Integer.parseInt(parts[2]);
                int day2=Integer.parseInt(dateDay);
                int different=day1-day2;
                if (different<=10 && different>0){
                    ViewDialogLincess alert = new ViewDialogLincess();
                    alert.showDialog(ThisApp.getCurrentActivity(),"ايام يرجى الاتصال بالشركة للتجديد الاشتراك"+different+ "متبقي");
                }
                }
            }
        else {
            EnableButtons();
        }
        }

    }else {
        String activationDate=CONSTANT.ACTIVATION_DATE_LICENCESS;
         String dueDateLicencess=CONSTANT.DUE_DATE_LICENCESS;
        Timestamp ts=null,ts2 = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
             ts = new Timestamp(((Date)df.parse(activationDate)).getTime());
             ts2 = new Timestamp(((Date)df.parse(dueDateLicencess)).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long LincesID = lincessDBAdapter.insertEntry(SETTINGS.companyID,CONSTANT.YEARLY_ACTIVATION_LICENSS,Integer.toString(SETTINGS.branchId), ts , ts2, CONSTANT.ACTIVE);
    }

    lincessDBAdapter.close();

}


    public void EnableButtons() {
        for (Permissions p : permissions) {
            switch (p.getName()) {
                case Permissions.PERMISSIONS_MAIN_SCREEN:
                    OpiningReportDBAdapter opiningReportDBAdapter=new OpiningReportDBAdapter(DashBord.this);
                    opiningReportDBAdapter.open();
                    ClosingReportDBAdapter closingReportDBAdapter=new ClosingReportDBAdapter(DashBord.this);
                    closingReportDBAdapter.open();
                    ClosingReport closingR=null;
                    try {
                        closingR=closingReportDBAdapter.getLastRow();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    OpiningReport opiningReport=null;
                    try {
                         opiningReport=opiningReportDBAdapter.getLastRow();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                   if(opiningReport==null) {
                        btAReport.setEnabled(true);
                        btZReport.setEnabled(false);
                        closingReport.setEnabled(false);
                        salesCart.setEnabled(false);
                    }else {
                       needAReport();
                   }

                    closingReportDBAdapter.close();
                    opiningReportDBAdapter.close();

                    break;

                case Permissions.PERMISSIONS_REPORT:
                    report.setEnabled(true);
                    break;
                case Permissions.PERMISSIONS_PRODUCT:
                    product.setEnabled(true);
                    break;
                case Permissions.PERMISSIONS_CATEGORY:
                    category.setEnabled(true);
                    break;
                case Permissions.PERMISSIONS_USER:
                    users.setEnabled(true);
                    break;
                case Permissions.PERMISSIONS_SCHEDULE_WORKERS:
                    schedule_workers.setEnabled(true);
                    break;
                case Permissions.PERMISSIONS_INVENTORY:
                    inventoryManagement.setEnabled(true);
                    break;
                case Permissions.PERMISSIONS_SETTINGS:
                    settings.setEnabled(true);
                    break;
                case Permissions.PERMISSIONS_USER_CLUB:
                    customerClub.setEnabled(true);
                    break;

            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        SplashScreenActivity.readSettings(this);
        OrderDBAdapter saleDBAdapter = new OrderDBAdapter(DashBord.this);
        saleDBAdapter.open();
        lastSale = saleDBAdapter.getLast();
        saleDBAdapter.close();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String str = extras.getString(LogInActivity.LEADPOS_MAKE_A_REPORT);
            if (str.equals(LogInActivity.LEADPOS_MAKE_A_REPORT)) {
                extras.clear();
            }
        }
        checkLincess();
       // EnableButtons();

        if (SETTINGS.company == null) {
            SETTINGS.company = CompanyStatus.BO_COMPANY;
        }
        if (SETTINGS.printer == null) {
            SETTINGS.printer = PrinterType.HPRT_TP805;
        }
        switch (SETTINGS.printer) {
            case HPRT_TP805:
                HPRT_TP805.setConnected(false);
                if (HPRT_TP805.connect(this)) {
                    //Toast.makeText(this, "Printer Connect Success!", Toast.LENGTH_LONG).show();
                } else {
                    PRINTER_STATE=true;
                    //   Toast.makeText(this, "Printer Connect Error!", Toast.LENGTH_LONG).show();
                }
                break;
            case BTP880:
                break;
            case SUNMI_T1:
                AidlUtil.getInstance().connectPrinterService(this);
                if (AidlUtil.getInstance().isConnect()) {
                    //Toast.makeText(this, "Printer Connect Success!", Toast.LENGTH_LONG).show();
                } else {
                    PRINTER_STATE=true;
                    //Toast.makeText(this, "Please connect the printer", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private boolean needAReport() {
        ClosingReportDBAdapter closingReportDBAdapter = new ClosingReportDBAdapter(DashBord.this);
        closingReportDBAdapter.open();
        ZReport zReport = getLastZReport();
        OpiningReport aReport = getLastAReport();
        ClosingReport closingR = null;
        try {
            closingR = closingReportDBAdapter.getLastRow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (aReport != null&&closingR!=null) {
            if (aReport.getOpiningReportId()!=closingR.getOpiningReportId()) {
                closingReport.setEnabled(true);
                btAReport.setEnabled(false);
                btZReport.setEnabled(true);
                salesCart.setEnabled(true);
            }else  if (aReport.getOpiningReportId()==closingR.getOpiningReportId()&&zReport.getCloseOpenReport().equalsIgnoreCase("open")) {
                closingReport.setEnabled(false);
                btAReport.setEnabled(true);
                btZReport.setEnabled(true);
                salesCart.setEnabled(true);
            }else  if (aReport.getOpiningReportId()==closingR.getOpiningReportId()&&zReport.getCloseOpenReport().equalsIgnoreCase("close")) {
                closingReport.setEnabled(false);
                btAReport.setEnabled(true);
                btZReport.setEnabled(false);
                salesCart.setEnabled(false);
            }
            return true;
        }else   if (aReport != null&&closingR==null) {
                closingReport.setEnabled(true);
                btAReport.setEnabled(false);
                btZReport.setEnabled(true);
                salesCart.setEnabled(true);

            return true;
        }


        /*if (aReport != null && zReport != null) {
            if (aReport.getLastZReportID() == zReport.getzReportId()) {

            } else {
                return true;
            }

        }*/
        closingReportDBAdapter.close();
        return false;
    }

    private ZReport getLastZReport() {
        ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(this);

        zReportDBAdapter.open();
        ZReport zReport = null;
        try {
            zReport = zReportDBAdapter.getLastRow();
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
    private void ShowAReportDialog(final OpiningReport aReport) {

        //there is no customerName report after z report
        enableBackButton = false;
        // a_report Dialog WithOut Currency
        if (!SETTINGS.enableCurrencies) {
            final List<Currency> currenciesList;
            List<CurrencyType> currencyTypesList = null;
            final CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(DashBord.this);
            currencyDBAdapter.open();
            CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(this);
            currencyTypeDBAdapter.open();
            currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
            currenciesList = currencyDBAdapter.getAllCurrencyLastUpdate(currencyTypesList);
            fCurrency=currenciesList.get(0);
            firstCurrencyInDefaultValue = 0;
            final Dialog discountDialog = new Dialog(DashBord.this);
            discountDialog.setTitle(R.string.opening_report);
            discountDialog.setContentView(R.layout.cash_payment_dialog);
            discountDialog.setCancelable(false);

            final Button btOK = (Button) discountDialog.findViewById(R.id.cashPaymentDialog_BTOk);
            final Button btCancel = (Button) discountDialog.findViewById(R.id.cashPaymentDialog_BTCancel);
            final EditText etFirstCurrencyAmount = (EditText) discountDialog.findViewById(R.id.cashPaymentDialog_TECash);
            final Switch sw = (Switch) discountDialog.findViewById(R.id.cashPaymentDialog_SwitchProportion);
            sw.setVisibility(View.GONE);
            discountDialog.setCanceledOnTouchOutside(false);

            btCancel.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    discountDialog.dismiss();
                }
            });

            discountDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    DashBord.this.onResume();
                }
            });
            etFirstCurrencyAmount.setHint(R.string.amount);
            etFirstCurrencyAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        btOK.callOnClick();
                    }
                    return false;
                }
            });
            etFirstCurrencyAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!etFirstCurrencyAmount.getText().toString().equals("")) {
                        firstCurrencyInDefaultValue = Double.parseDouble(etFirstCurrencyAmount.getText().toString());

                    } else {
                        firstCurrencyInDefaultValue = 0;
                    }
                }
            });



            btOK.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    String str = etFirstCurrencyAmount.getText().toString();
                    ZReport zReport1 = null;
                    ZReportDBAdapter zReportDBAdapter=new ZReportDBAdapter(DashBord.this);
                    zReportDBAdapter.open();
                    ZReportCountDbAdapter zReportCountDbAdapter=new ZReportCountDbAdapter(DashBord.this);
                    zReportCountDbAdapter.open();
                    try {
                        zReport1 = zReportDBAdapter.getLastRow();
                        Log.d("zReport1",zReport1.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!str.equals("")) {
                        aReport.setAmount(Double.parseDouble(str));
                        OpiningReportDBAdapter aReportDBAdapter = new OpiningReportDBAdapter(DashBord.this);
                        aReportDBAdapter.open();
                        final OpiningReportDetailsDBAdapter aReportDetailsDBAdapter = new OpiningReportDetailsDBAdapter(DashBord.this);
                        aReportDetailsDBAdapter.open();

                        ZReport zReport = null;
                        OpiningReport opiningReport=null;



                        final ArrayList<String> hintForCurrencyType = new ArrayList<String>();
                        final ArrayList<Double> hintForCurrencyAmount = new ArrayList<Double>();
                        Log.d("areportA",aReport.getLastZReportID()+"");
                        long id = aReportDBAdapter.insertEntry(aReport.getCreatedAt(), aReport.getByUserID(), aReport.getAmount(), aReport.getLastOrderId(), aReport.getLastZReportID());
                        Log.d("idOpinin",id+"");
                        opiningReport = aReportDBAdapter.getById(id);
                        Util.opiningReport(DashBord.this,opiningReport,hintForCurrencyType,hintForCurrencyAmount);
                        try {
                            zReport = zReportDBAdapter.getLastRow();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(zReport==null){
                            ZReport z = new ZReport();
                            z.setCreatedAt( new Timestamp(System.currentTimeMillis()));
                            z.setByUser(SESSION._EMPLOYEE.getEmployeeId());
                            z.setStartOrderId(0);
                            z.setTotalAmount(aReport.getAmount());
                            z.setFirstTypeAmount(aReport.getAmount());
                            z.setCloseOpenReport("open");
                            zReportDBAdapter.insertEntry(z);
                            ZReport Tz=null;
                          /*  try {
                           Tz=zReportDBAdapter.getLastRow();
                                Log.d("Tz",Tz.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ZReportCount zReportCount=new ZReportCount();
                            zReportCount.setzReportCountZReportId(Tz.getzReportId());
                            zReportCountDbAdapter.insertEntry(zReportCount);



                            zReportDBAdapter.insertEntry(z);*/
                            ZReportCount zReportCount=new ZReportCount();
                            try {
                                Tz = zReportDBAdapter.getLastRow();
                                Log.d("zReport1zReport1",Tz.toString());
                                opiningReport.setLastZReportID(Tz.getzReportId());
                                long opiningReportid=   aReportDBAdapter.upDateEntry(opiningReport);
                                OpiningReport opiningReport1 =aReportDBAdapter.getById(opiningReportid);
                                Log.d("finelTestZreport",opiningReport1.toString());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.d("testZreportCount",zReportCount.toString());
                            zReportCount.setzReportCountZReportId(Tz.getzReportId());
                            zReportCountDbAdapter.insertEntry(zReportCount);




                        }
                        else if(zReport.getCloseOpenReport().equalsIgnoreCase("close")){
                            ZReport z = new ZReport();
                            z.setCreatedAt( new Timestamp(System.currentTimeMillis()));
                            z.setByUser(SESSION._EMPLOYEE.getEmployeeId());
                            z.setStartOrderId(zReport.getEndOrderId()+1);
                            z.setTotalAmount(aReport.getAmount());
                            z.setFirstTypeAmount(aReport.getAmount());
                            z.setCloseOpenReport("open");
                            zReportDBAdapter.insertEntry(z);
                            ZReport Tz=null;
                            /*try {
                                Tz=zReportDBAdapter.getLastRow();
                                Log.d("Tzz",Tz.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ZReportCount zReportCount=new ZReportCount();
                            zReportCount.setzReportCountZReportId(Tz.getzReportId());
                            zReportCountDbAdapter.insertEntry(zReportCount);


                            z.setCloseOpenReport("open");
                            zReportDBAdapter.insertEntry(z);*/
                            ZReportCount zReportCount=new ZReportCount();
                            try {
                                Tz = zReportDBAdapter.getLastRow();
                                Log.d("zReport1zReport1zReport1",Tz.toString());
                                opiningReport.setLastZReportID(Tz.getzReportId());
                                aReportDBAdapter.upDateEntry(opiningReport);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.d("testZreportCount",zReportCount.toString());
                            zReportCount.setzReportCountZReportId(Tz.getzReportId());
                            zReportCountDbAdapter.insertEntry(zReportCount);




                        }
                        else if(zReport.getCloseOpenReport().equalsIgnoreCase("open")){
                            zReport.setTotalAmount(zReport.getTotalAmount()+aReport.getAmount());
                            zReport.setFirstTypeAmount(zReport.getFirstTypeAmount()+aReport.getAmount());
                            zReportDBAdapter.updateEntry(zReport);

                        }
                        if (firstCurrencyInDefaultValue > 0) {
                           // aReportDetailsDBAdapter.insertEntry(id, firstCurrencyInDefaultValue, fCurrency.getId(), firstCurrencyInDefaultValue *
                            aReportDetailsDBAdapter.insertEntry(id, firstCurrencyInDefaultValue, fCurrency.getId(), firstCurrencyInDefaultValue);
                        }
                        discountDialog.cancel();
                        checkLincess();
                        //EnableButtons();
                    }
                    zReportDBAdapter.close();
                    zReportCountDbAdapter.close();

                    if (aReportDBAdapter!=null){
                        aReportDBAdapter.close();
                    }

                }
            });
            discountDialog.show();}

        // With Currency Model
        else {
            firstCurrencyInDefaultValue = 0;
            secondCurrencyInDefaultValue = 0;
            thirdCurrencyInDefaultValue = 0;
            forthCurrencyInDefaultValue = 0;
            final Dialog aReportDialog = new Dialog(DashBord.this);
            aReportDialog.setTitle(R.string.opening_report);
            aReportDialog.setContentView(R.layout.areport_details_dialog);
            aReportDialog.setCancelable(false);




            //Getting default currencies name and values
            List<CurrencyType> currencyTypesList = null;
            final List<Currency> currenciesList;
            List<String> currenciesNames;
            CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(this);
            currencyTypeDBAdapter.open();
            currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
            currencyTypeDBAdapter.close();
            final CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(DashBord.this);
            currencyDBAdapter.open();
            currenciesList = currencyDBAdapter.getAllCurrencyLastUpdate(currencyTypesList);
            currencyDBAdapter.close();
            currenciesNames = new ArrayList<String>();
            for (int i = 0; i < currencyTypesList.size(); i++) {
                currenciesNames.add(currencyTypesList.get(i).getType());
            }

            for (int i=0;i<currenciesNames.size();i++){
                if (currenciesNames.get(i).equals(SETTINGS.currencyCode))
                    positionItem=i;
            }

            final OpiningReportDetailsDBAdapter aReportDetailsDBAdapter = new OpiningReportDetailsDBAdapter(DashBord.this);
            aReportDetailsDBAdapter.open();
            // Creating adapter for spinner
            final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currenciesNames);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            Spinner sFirstCurrency = (Spinner) aReportDialog.findViewById(R.id.aReportDetailsDialog_SPForFirstCurrency);
            Spinner sSecondCurrency = (Spinner) aReportDialog.findViewById(R.id.aReportDetailsDialog_SPForSecondCurrency);
            Spinner sThirdCurrency = (Spinner) aReportDialog.findViewById(R.id.aReportDetailsDialog_SPForThirdCurrency);
            Spinner sForthCurrency = (Spinner) aReportDialog.findViewById(R.id.aReportDetailsDialog_SPForForthCurrency);
            final Button btCancel = (Button) aReportDialog.findViewById(R.id.cashPaymentDialog_BTCancel);
            sFirstCurrency.setAdapter(dataAdapter);
            sSecondCurrency.setAdapter(dataAdapter);
            sThirdCurrency.setAdapter(dataAdapter);
            sForthCurrency.setAdapter(dataAdapter);

            sFirstCurrency.setSelection(positionItem);
            sSecondCurrency.setSelection(positionItem);
            sThirdCurrency.setSelection(positionItem);
            sForthCurrency.setSelection(positionItem);

            if (!currenciesList.isEmpty()){
            fCurrency = currenciesList.get(positionItem);
            sCurrency = currenciesList.get(positionItem);
            tCurrency = currenciesList.get(positionItem);
            forthCurrency = currenciesList.get(positionItem);}


            // define editText , Button ,ImageView ,Layout
            final EditText ETFirstCurrencyAmount = (EditText) aReportDialog.findViewById(R.id.aReportDetailsDialog_ETFirstCurrencyAmount);
            final EditText ETSecondCurrencyAmount = (EditText) aReportDialog.findViewById(R.id.aReportDetailsDialog_ETSecondCurrencyAmount);
            final EditText ETThirdCurrencyAmount = (EditText) aReportDialog.findViewById(R.id.aReportDetailsDialog_ETThirdCurrencyAmount);
            final EditText ETForthCurrencyAmount = (EditText) aReportDialog.findViewById(R.id.aReportDetailsDialog_ETForthCurrencyAmount);
            final Button btOKForCurrency = (Button) aReportDialog.findViewById(R.id.aReportDetailsDialog_BTOK);
            ImageView addSecondCurrency = (ImageView) aReportDialog.findViewById(R.id.addSecondCurrency);
            ImageView addThirdCurrency = (ImageView) aReportDialog.findViewById(R.id.addThirdCurrency);
            ImageView addForthCurrency = (ImageView) aReportDialog.findViewById(R.id.addForthCurrency);
            final LinearLayout secondCurrencyLayout = (LinearLayout) aReportDialog.findViewById(R.id.secondCurrencyLayout);
            final LinearLayout thirdCurrencyLayout = (LinearLayout) aReportDialog.findViewById(R.id.thirdCurrencyLayout);
            final LinearLayout forthCurrencyLayout = (LinearLayout) aReportDialog.findViewById(R.id.forhCurrencyLayout);
            aReportDialog.setCanceledOnTouchOutside(false);

            aReportDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    DashBord.this.onResume();
                }
            });
            btCancel.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    aReportDialog.dismiss();
                }
            });
            addSecondCurrency.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    secondCurrencyLayout.setVisibility(View.VISIBLE);
                }
            });
            addThirdCurrency.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    thirdCurrencyLayout.setVisibility(View.VISIBLE);
                }
            });
            addForthCurrency.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    forthCurrencyLayout.setVisibility(View.VISIBLE);
                }
            });
            sFirstCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!currenciesList.isEmpty())
                    fCurrency = currenciesList.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            sSecondCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!currenciesList.isEmpty())
                    sCurrency = currenciesList.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            sThirdCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!currenciesList.isEmpty())
                    tCurrency = currenciesList.get(position);
                }


                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            sForthCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!currenciesList.isEmpty())
                    forthCurrency = currenciesList.get(position);
                }


                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });


            ETFirstCurrencyAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        btOKForCurrency.callOnClick();
                    }
                    return false;
                }
            });
            ETFirstCurrencyAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!ETFirstCurrencyAmount.getText().toString().equals("")) {
                        firstCurrencyInDefaultValue = Double.parseDouble(ETFirstCurrencyAmount.getText().toString());

                    } else {
                        firstCurrencyInDefaultValue = 0;
                    }
                }
            });

            ETSecondCurrencyAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!ETSecondCurrencyAmount.getText().toString().equals("")) {
                        secondCurrencyInDefaultValue = Double.parseDouble(ETSecondCurrencyAmount.getText().toString());

                    } else {
                        secondCurrencyInDefaultValue = 0;
                    }
                }
            });

            ETThirdCurrencyAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!ETThirdCurrencyAmount.getText().toString().equals("")) {
                        thirdCurrencyInDefaultValue = Double.parseDouble(ETThirdCurrencyAmount.getText().toString());

                    } else {
                        thirdCurrencyInDefaultValue = 0;
                    }
                }
            });
            ETForthCurrencyAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!ETForthCurrencyAmount.getText().toString().equals("")) {
                        forthCurrencyInDefaultValue = Double.parseDouble(ETForthCurrencyAmount.getText().toString());

                    } else {
                        forthCurrencyInDefaultValue = 0;
                    }
                }
            });

            final List<CurrencyType> finalCurrencyTypesList = currencyTypesList;
            btOKForCurrency.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    ZReportCountDbAdapter zReportCountDbAdapter=new ZReportCountDbAdapter(DashBord.this);
                    zReportCountDbAdapter.open();
                    String str = ETFirstCurrencyAmount.getText().toString();
                    if (!str.equals("")) {
                        if (firstCurrencyInDefaultValue>0&&secondCurrencyInDefaultValue==0&&forthCurrencyInDefaultValue==0&&thirdCurrencyInDefaultValue==0){
                            aReportTotalAmount=firstCurrencyInDefaultValue;
                        }
                        else {
                        aReportTotalAmount = firstCurrencyInDefaultValue * fCurrency.getRate() + secondCurrencyInDefaultValue * sCurrency.getRate() + thirdCurrencyInDefaultValue * tCurrency.getRate() + forthCurrencyInDefaultValue * forthCurrency.getRate();}

                        OpiningReportDBAdapter aReportDBAdapter = new OpiningReportDBAdapter(DashBord.this);
                        aReportDBAdapter.open();
                        OpiningReport opiningReport = null;
                        ZReport zReport1=null;

                        try {
                            aReport.setAmount(aReportTotalAmount);
                            Log.d("ZreportIdfff",aReport.getLastZReportID()+"");

                            long id = aReportDBAdapter.insertEntry(aReport.getCreatedAt(), aReport.getByUserID(), aReport.getAmount(), aReport.getLastOrderId(),aReport.getLastZReportID());
                            opiningReport = aReportDBAdapter.getById(id);
                            aReportId = aReportDBAdapter.getLastRow().getOpiningReportId();
                            opiningReport= aReportDBAdapter.getById(aReportId);
                            ZReportDBAdapter zReportDBAdapter=new ZReportDBAdapter(DashBord.this);
                            zReportDBAdapter.open();
                            ZReport zReport = null;
                            try {
                                zReport = zReportDBAdapter.getLastRow();
                                Log.d("zReportzReport",zReport.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(zReport==null){
                                ZReport z = new ZReport();
                                z.setCreatedAt( new Timestamp(System.currentTimeMillis()));
                                z.setByUser(SESSION._EMPLOYEE.getEmployeeId());
                                z.setStartOrderId(0);
                                z.setTotalAmount(aReportTotalAmount);
                                Log.d("ff",fCurrency.getCurrencyCode());
                                Log.d("ddfd",finalCurrencyTypesList.get(0).getType());
                                if (firstCurrencyInDefaultValue!=0) {
                                    if (fCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(0).getType())) {
                                        z.setFirstTypeAmount(z.getFirstTypeAmount()+firstCurrencyInDefaultValue);
                                    } else if (fCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(1).getType())) {
                                        z.setSecondTypeAmount(z.getSecondTypeAmount()+firstCurrencyInDefaultValue);
                                    } else if (fCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(2).getType())) {
                                        z.setThirdTypeAmount(z.getThirdTypeAmount()+firstCurrencyInDefaultValue);
                                    } else if (fCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(3).getType())) {
                                        z.setFourthTypeAmount(z.getFourthTypeAmount()+firstCurrencyInDefaultValue);
                                    }
                                }

                                if (secondCurrencyInDefaultValue!=0){
                                    if (sCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(0).getType())){
                                        z.setFirstTypeAmount(z.getFirstTypeAmount()+secondCurrencyInDefaultValue);
                                    }
                                    else if (sCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(1).getType())){
                                        z.setSecondTypeAmount(z.getSecondTypeAmount()+secondCurrencyInDefaultValue);
                                    }
                                    else if (sCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(2).getType())){
                                        z.setThirdTypeAmount(z.getThirdTypeAmount()+secondCurrencyInDefaultValue);
                                    }
                                    else if (sCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(3).getType())){
                                        z.setFourthTypeAmount(z.getFourthTypeAmount()+secondCurrencyInDefaultValue);
                                    }}
                                if (thirdCurrencyInDefaultValue!=0){
                                    if (tCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(0).getType())){
                                        z.setFirstTypeAmount(z.getFirstTypeAmount()+thirdCurrencyInDefaultValue);
                                    }
                                    else if (tCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(1).getType())){
                                        z.setSecondTypeAmount(z.getSecondTypeAmount()+thirdCurrencyInDefaultValue);
                                    }
                                    else if (tCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(2).getType())){
                                        z.setThirdTypeAmount(z.getThirdTypeAmount()+thirdCurrencyInDefaultValue);
                                    }
                                    else if (tCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(3).getType())){
                                        z.setFourthTypeAmount(z.getFourthTypeAmount()+thirdCurrencyInDefaultValue);
                                    }}

                                if (forthCurrencyInDefaultValue!=0){
                                    if (tCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(0).getType())){
                                        z.setFirstTypeAmount(z.getFirstTypeAmount()+forthCurrencyInDefaultValue);
                                    }
                                    else if (forthCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(1).getType())){
                                        z.setSecondTypeAmount(z.getSecondTypeAmount()+forthCurrencyInDefaultValue);
                                    }
                                    else if (forthCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(2).getType())){
                                        z.setThirdTypeAmount(z.getThirdTypeAmount()+forthCurrencyInDefaultValue);
                                    }
                                    else if (forthCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(3).getType())){
                                        z.setFourthTypeAmount(z.getFourthTypeAmount()+forthCurrencyInDefaultValue);
                                    }}


                               /* z.setFirstTypeAmount(firstCurrencyInDefaultValue);
                                z.setSecondTypeAmount(secondCurrencyInDefaultValue);
                                z.setThirdTypeAmount(thirdCurrencyInDefaultValue);
                                z.setFourthTypeAmount(forthCurrencyInDefaultValue);*/
                                z.setCloseOpenReport("open");
                                zReportDBAdapter.insertEntry(z);
                                ZReportCount zReportCount=new ZReportCount();
                                try {
                                    zReport1 = zReportDBAdapter.getLastRow();
                                    Log.d("zReport1zReport1",zReport1.toString());
                                    opiningReport.setLastZReportID(zReport1.getzReportId());
                                 long opiningReportid=   aReportDBAdapter.upDateEntry(opiningReport);
                                    OpiningReport opiningReport1 =aReportDBAdapter.getById(opiningReportid);
                                    Log.d("finelTestZreport",opiningReport1.toString());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Log.d("testZreportCount",zReportCount.toString());
                                zReportCount.setzReportCountZReportId(zReport1.getzReportId());
                                zReportCountDbAdapter.insertEntry(zReportCount);


                            }
                            else if(zReport.getCloseOpenReport().equalsIgnoreCase("close")){
                                ZReport z = new ZReport();
                                z.setCreatedAt( new Timestamp(System.currentTimeMillis()));
                                z.setByUser(SESSION._EMPLOYEE.getEmployeeId());
                                z.setStartOrderId(zReport.getEndOrderId()+1);
                                z.setTotalAmount(aReport.getAmount());
                                if (firstCurrencyInDefaultValue!=0) {
                                    if (fCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(0).getType())) {
                                        z.setFirstTypeAmount(z.getFirstTypeAmount()+firstCurrencyInDefaultValue);
                                    } else if (fCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(1).getType())) {
                                        z.setSecondTypeAmount(z.getSecondTypeAmount()+firstCurrencyInDefaultValue);
                                    } else if (fCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(2).getType())) {
                                        z.setThirdTypeAmount(z.getThirdTypeAmount()+firstCurrencyInDefaultValue);
                                    } else if (fCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(3).getType())) {
                                        z.setFourthTypeAmount(z.getFourthTypeAmount()+firstCurrencyInDefaultValue);
                                    }
                                }

                                if (secondCurrencyInDefaultValue!=0){
                                    if (sCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(0).getType())){
                                        z.setFirstTypeAmount(z.getFirstTypeAmount()+secondCurrencyInDefaultValue);
                                    }
                                    else if (sCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(1).getType())){
                                        z.setSecondTypeAmount(z.getSecondTypeAmount()+secondCurrencyInDefaultValue);
                                    }
                                    else if (sCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(2).getType())){
                                        z.setThirdTypeAmount(z.getThirdTypeAmount()+secondCurrencyInDefaultValue);
                                    }
                                    else if (sCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(3).getType())){
                                        z.setFourthTypeAmount(z.getFourthTypeAmount()+secondCurrencyInDefaultValue);
                                    }}
                                if (thirdCurrencyInDefaultValue!=0){
                                    if (tCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(0).getType())){
                                        z.setFirstTypeAmount(z.getFirstTypeAmount()+thirdCurrencyInDefaultValue);
                                    }
                                    else if (tCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(1).getType())){
                                        z.setSecondTypeAmount(z.getSecondTypeAmount()+thirdCurrencyInDefaultValue);
                                    }
                                    else if (tCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(2).getType())){
                                        z.setThirdTypeAmount(z.getThirdTypeAmount()+thirdCurrencyInDefaultValue);
                                    }
                                    else if (tCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(3).getType())){
                                        z.setFourthTypeAmount(z.getFourthTypeAmount()+thirdCurrencyInDefaultValue);
                                    }}

                                if (forthCurrencyInDefaultValue!=0){
                                    if (tCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(0).getType())){
                                        z.setFirstTypeAmount(z.getFirstTypeAmount()+forthCurrencyInDefaultValue);
                                    }
                                    else if (forthCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(1).getType())){
                                        z.setSecondTypeAmount(z.getSecondTypeAmount()+forthCurrencyInDefaultValue);
                                    }
                                    else if (forthCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(2).getType())){
                                        z.setThirdTypeAmount(z.getThirdTypeAmount()+forthCurrencyInDefaultValue);
                                    }
                                    else if (forthCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(3).getType())){
                                        z.setFourthTypeAmount(z.getFourthTypeAmount()+forthCurrencyInDefaultValue);
                                    }}

                                /*z.setSecondTypeAmount(secondCurrencyInDefaultValue);
                                z.setThirdTypeAmount(thirdCurrencyInDefaultValue);
                                z.setFourthTypeAmount(forthCurrencyInDefaultValue);*/
                                z.setCloseOpenReport("open");
                                zReportDBAdapter.insertEntry(z);
                                ZReportCount zReportCount=new ZReportCount();
                                try {
                                    zReport1 = zReportDBAdapter.getLastRow();
                                    Log.d("zReport1zReport1zReport1",zReport1.toString());
                                    opiningReport.setLastZReportID(zReport1.getzReportId());
                                    aReportDBAdapter.upDateEntry(opiningReport);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Log.d("testZreportCount",zReportCount.toString());
                                zReportCount.setzReportCountZReportId(zReport1.getzReportId());
                                zReportCountDbAdapter.insertEntry(zReportCount);


                            }
                            else if(zReport.getCloseOpenReport().equalsIgnoreCase("open")){
                                zReport.setTotalAmount(zReport.getTotalAmount()+aReport.getAmount());

                                  if (firstCurrencyInDefaultValue!=0){
                                if (fCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(0).getType())){
                                    zReport.setFirstTypeAmount(zReport.getFirstTypeAmount()+firstCurrencyInDefaultValue);
                                }
                                else if (fCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(1).getType())){
                                    zReport.setSecondTypeAmount(zReport.getSecondTypeAmount()+firstCurrencyInDefaultValue);
                                }
                                else if (fCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(2).getType())){
                                    zReport.setThirdTypeAmount(zReport.getThirdTypeAmount()+firstCurrencyInDefaultValue);
                                }
                                else if (fCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(3).getType())){
                                    zReport.setFourthTypeAmount(zReport.getFourthTypeAmount()+firstCurrencyInDefaultValue);
                                }}


                                if (secondCurrencyInDefaultValue!=0) {
                                    if (sCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(0).getType())) {
                                        zReport.setFirstTypeAmount(zReport.getFirstTypeAmount() + secondCurrencyInDefaultValue);
                                    } else if (sCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(1).getType())) {
                                        zReport.setSecondTypeAmount(zReport.getSecondTypeAmount() + secondCurrencyInDefaultValue);
                                    } else if (sCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(2).getType())) {
                                        zReport.setThirdTypeAmount(zReport.getThirdTypeAmount() + secondCurrencyInDefaultValue);
                                    } else if (sCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(3).getType())) {
                                        zReport.setFourthTypeAmount(zReport.getFourthTypeAmount() + secondCurrencyInDefaultValue);
                                    }
                                }


                                if (thirdCurrencyInDefaultValue!=0){
                                if (tCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(0).getType())){
                                    zReport.setFirstTypeAmount(zReport.getFirstTypeAmount()+thirdCurrencyInDefaultValue);
                                }
                                else if (tCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(1).getType())){
                                    zReport.setSecondTypeAmount(zReport.getSecondTypeAmount()+thirdCurrencyInDefaultValue);
                                }
                                else if (tCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(2).getType())){
                                    zReport.setThirdTypeAmount(zReport.getThirdTypeAmount()+thirdCurrencyInDefaultValue);
                                }
                                else if (tCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(3).getType())) {
                                    zReport.setFourthTypeAmount(zReport.getFourthTypeAmount() + thirdCurrencyInDefaultValue);
                                }                                }

                                   if (forthCurrencyInDefaultValue!=0){
                                if (forthCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(0).getType())){
                                    zReport.setFirstTypeAmount(zReport.getFirstTypeAmount()+forthCurrencyInDefaultValue);
                                }
                                else if (forthCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(1).getType())){
                                    zReport.setSecondTypeAmount(zReport.getSecondTypeAmount()+forthCurrencyInDefaultValue);
                                }
                                else if (forthCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(2).getType())){
                                    zReport.setThirdTypeAmount(zReport.getThirdTypeAmount()+forthCurrencyInDefaultValue);
                                }
                                else if (forthCurrency.getCurrencyCode().equals(finalCurrencyTypesList.get(3).getType())){
                                    zReport.setFourthTypeAmount(zReport.getFourthTypeAmount()+forthCurrencyInDefaultValue);
                                }}

                               /* zReport.setFirstTypeAmount(zReport.getFirstTypeAmount()+firstCurrencyInDefaultValue);
                                zReport.setSecondTypeAmount(zReport.getSecondTypeAmount()+secondCurrencyInDefaultValue);
                                zReport.setThirdTypeAmount(zReport.getThirdTypeAmount()+thirdCurrencyInDefaultValue);
                                zReport.setFourthTypeAmount(zReport.getFourthTypeAmount()+forthCurrencyInDefaultValue);*/
                                zReportDBAdapter.updateEntry(zReport);
                                Log.d("finelTestZreport",zReport.toString());

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (firstCurrencyInDefaultValue > 0) {
                            Log.d("fCurrencyjj", String.valueOf(fCurrency.getId()));
                            aReportDetailsDBAdapter.insertEntry(aReportId, firstCurrencyInDefaultValue, fCurrency.getId(), firstCurrencyInDefaultValue * fCurrency.getRate());
                        }
                        if (secondCurrencyInDefaultValue > 0) {
                            aReportDetailsDBAdapter.insertEntry(aReportId, secondCurrencyInDefaultValue, sCurrency.getId(), secondCurrencyInDefaultValue * sCurrency.getRate());
                        }
                        if (thirdCurrencyInDefaultValue > 0) {
                            aReportDetailsDBAdapter.insertEntry(aReportId, thirdCurrencyInDefaultValue, tCurrency.getId(), thirdCurrencyInDefaultValue * tCurrency.getRate());
                        }
                        if (forthCurrencyInDefaultValue > 0) {
                            aReportDetailsDBAdapter.insertEntry(aReportId, forthCurrencyInDefaultValue, forthCurrency.getId(), forthCurrencyInDefaultValue * forthCurrency.getRate());
                        }
                        aReportDialog.cancel();
                        aReportDBAdapter.close();
                        aReportDetailsDBAdapter.close();
                        final ArrayList<String> hintForCurrencyType = new ArrayList<String>();
                        final ArrayList<Double> hintForCurrencyAmount = new ArrayList<Double>();
                        hintForCurrencyType.add(fCurrency.getName());
                        hintForCurrencyType.add(sCurrency.getName());
                        hintForCurrencyType.add(tCurrency.getName());
                        hintForCurrencyType.add(forthCurrency.getName());
                        hintForCurrencyAmount.add(firstCurrencyInDefaultValue);
                        hintForCurrencyAmount.add(secondCurrencyInDefaultValue);
                        hintForCurrencyAmount.add(thirdCurrencyInDefaultValue);
                        hintForCurrencyAmount.add(forthCurrencyInDefaultValue);
                        Util.opiningReport(DashBord.this,opiningReport,hintForCurrencyType,hintForCurrencyAmount);

                        checkLincess();
                  //     EnableButtons();
                    }
                    zReportCountDbAdapter.close();
                }
            });

            aReportDialog.show();


        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
            Intent intent = new Intent(DashBord.this, LogInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            SESSION._LogOut();
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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
                ProgressDialog progressDialog = ProgressDialog.show(DashBord.this, "", "Opening...");

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



    public void updateCurrency() throws JSONException, IOException {
        MessageTransmit messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(DashBord.this);
        currencyTypeDBAdapter.open();

        List<CurrencyType> currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
        currencyTypeDBAdapter.close();
        CurrencyDBAdapter currencyDBAdapter =new CurrencyDBAdapter(this);
        currencyDBAdapter.open();
        Currency lastCurrency = null;
        try {
            lastCurrency = currencyDBAdapter.getLastCurrency();
            Log.d("lastCurrencyUpdateDAsh",lastCurrency+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Timestamp timestamp =new Timestamp(System.currentTimeMillis());
        if (lastCurrency!=null){
            if (DateConverter.toDate(lastCurrency.getLastUpdate().getTime()).equals(DateConverter.toDate(timestamp.getTime()))) {
                //do nothing
            }else {
                String currencyRes = messageTransmit.getCurrency(ApiURL.Currencies,SESSION.token);
                ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                objectMapper.setDateFormat(dateFormat);
                JSONObject jsonObject = null;

                jsonObject = new JSONObject(currencyRes);
                try {
                    String msgData = jsonObject.getString(MessageKey.responseBody);


                    try {
                        JSONArray jsonArray = new JSONArray(msgData);
                        currencyDBAdapter.deleteOldRate(currencyTypesList);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            msgData = jsonArray.getJSONObject(i).toString();
                            Currency currency = null;
                            currency = objectMapper.readValue(msgData, Currency.class);
                            //currencyDBAdapter.deleteCurrencyList();
                            Long idCoulm=   currencyDBAdapter.insertEntry(currency);
                            Log.d("idCoulm",idCoulm+"");


                            //  currencyTypeDBAdapter.insertEntry(currency.getCurrencyCode());
                        }
                    } catch (Exception e) {
                        Log.d("Exception",e.toString());
                    }




                } catch (JSONException e) {
                    Log.d("Exception",e.toString());
                }
              //  currencyDBAdapter.deleteOldRate(currencyTypesList);


            }


        }
        else {
            String currencyRes = messageTransmit.getCurrency(ApiURL.Currencies,SESSION.token);;
            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            objectMapper.setDateFormat(dateFormat);
            JSONObject jsonObject = null;
            jsonObject = new JSONObject(currencyRes);
            try {
                String msgData = jsonObject.getString(MessageKey.responseBody);


                try {
                    JSONArray jsonArray = new JSONArray(msgData);
                    Log.d("jsonArraySize",jsonArray.length()+"Size");
                    Log.d("jsonArrayCurrency",jsonArray.toString());
                    currencyDBAdapter.deleteCurrencyList();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Log.d("i",i+" ");
                        msgData = jsonArray.getJSONObject(i).toString();
                        Currency currency = null;
                        currency = objectMapper.readValue(msgData, Currency.class);
                       // currencyDBAdapter.deleteCurrencyList();
                        Long idCoulm= currencyDBAdapter.insertEntry(currency);
                        Log.d("idCoulm",idCoulm.toString());
                      // currencyTypeDBAdapter.insertEntry(currency.getCurrencyCode());
                    }
                } catch (Exception e) {
                    Log.d("Exception",e.toString());
                }



            } catch (JSONException e) {
                Log.d("Exception",e.toString());
            }



        }




    }


    private void checkSettings(String token) {

        MessageTransmit messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
        try {
            String res = messageTransmit.authGet(ApiURL.PosSetting, token);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(res);

            }
            catch (JSONException e){
                JSONArray jsonArray = new JSONArray(res);
                jsonObject = jsonArray.getJSONObject(0);
            }
            if(jsonObject.getString(MessageKey.status).equals("200")) {

                JSONObject respnse;

                try {
                    if(jsonObject.getString(MessageKey.responseBody).equalsIgnoreCase("null")){
                        PosSettingDbAdapter posSettingDbAdapter = new PosSettingDbAdapter(context);
                        posSettingDbAdapter.open();
                        try {
                            PosSetting posSetting=posSettingDbAdapter.getPosSettingID();
                            addPosSetting(posSetting);
                            posSettingDbAdapter.close();
                        }
                        catch (Exception e){
                            Log.d("ExceptionNullSetting",e.toString());
                        }

                    }
                    else {

                        JSONObject responseBody = null;
                        responseBody = jsonObject.getJSONObject(MessageKey.responseBody);
                        Log.d("responesBoydy",responseBody.toString());
                       /*
                        Log.d("responesBoydy",jsonObject.getString(MessageKey.responseBody));
                        Log.d("enableCurrency",responseBody.getBoolean("enableCurrency")+"");
                        Log.d("enableCreditCard",responseBody.getBoolean("enableCreditCard")+"");
                        Log.d("enablePinPad",responseBody.getBoolean("enablePinPad")+"");
                        Log.d("enableCustomerMeasurement",responseBody.getBoolean("enableCustomerMeasurement")+"");
                        Log.d("noOfFloatPoint",responseBody.getInt("noOfFloatPoint") +"");
                        Log.d("printerType",responseBody.getString("printerType")+"");
                        Log.d("posVersionNo",responseBody.getInt("posVersionNo")+"");
                        Log.d("companyStatus",responseBody.getString("companyStatus")+"");
                        Log.d("posDbVersionNo",responseBody.getInt("posDbVersionNo")+"");
                        Log.d("branchId",responseBody.getInt("branchId")+"");
                        Log.d("currencyCode",responseBody.getString("currencyCode")+"");
                        Log.d("currencySymbol",responseBody.getString("currencySymbol")+"");
                        Log.d("country", responseBody.getString("country")+"");
                        Log.d("duplicateInvoice", responseBody.getString("duplicateInvoice")+"");*/
                        PosSettingDbAdapter posSettingDbAdapter = new PosSettingDbAdapter(context);
                        posSettingDbAdapter.open();
                        try {
                            if (posSettingDbAdapter.existsHaveColumnInTable()){
                               Log.d("have","have");
                            PosSetting posSetting=posSettingDbAdapter.getPosSettingID();
                            updatePosSetting(posSetting);
                            posSettingDbAdapter.close();
                        }
                       else {
                                Log.d("notHave","notHave");
                                posSettingDbAdapter.insertEntry(responseBody.getBoolean(MessageKey.enableCurrency),
                                        responseBody.getBoolean(MessageKey.enableCreditCard),
                                        responseBody.getBoolean(MessageKey.enablePinPad) ,responseBody.getBoolean(MessageKey.enableCustomerMeasurement)
                                        ,responseBody.getInt(MessageKey.noOfFloatPoint)  ,responseBody.getString(MessageKey.printerType),
                                        responseBody.getString(MessageKey.companyStatus)
                                      ,  responseBody.getString(MessageKey.posVersionNo),responseBody.getString(MessageKey.posDbVersionNo),
                                        responseBody.getInt(MessageKey.branchId), responseBody.getString(MessageKey.currencyCode),
                                        responseBody.getString(MessageKey.currencySymbol), responseBody.getString(MessageKey.country),
                                        responseBody.getBoolean(MessageKey.duplicateInvoice));
                            }
                        }
                        catch (Exception e){
                            Log.d("ExceptionSetting",e.toString());
                        }



                    }
                }
                catch (JSONException e){
                    Log.d("getPossSettingException",e.toString());
                }

            }
            else {
                Log.e("getPossSetting",jsonObject.getString(MessageKey.responseType));
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }


    private void addPosSetting(PosSetting posSetting) {

        MessageTransmit messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
        final String token = SESSION.token;
        try {

            ObjectMapper mapper = new ObjectMapper();
            posSetting.setPosDbVersionNo(String.valueOf(DbHelper.DATABASE_VERSION));
            String verCode = getVersionCode();
            posSetting.setPosVersionNo(verCode);
            /*posSetting.setCompanyStatus();*/
            String jsonString = mapper.writeValueAsString(posSetting);
            String res = messageTransmit.authPost(ApiURL.PosSetting,jsonString, token);
            updatePosVersion(posSetting);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(res);
                if(jsonObject.getString(MessageKey.status).equals("200")) {
                    DbHelper.DATABASE_ENABEL_POS_SETTING=true;
                }
                else {
                    Log.d("addPossSetting",jsonObject.getString(MessageKey.responseType));
                }
            }
            catch (JSONException e){
                Log.e("exceptionAddPosSetting",e.toString());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void updatePosSetting(PosSetting posSetting) {

        MessageTransmit messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
        final String token = SESSION.token;
        try {

            ObjectMapper mapper = new ObjectMapper();
            posSetting.setPosDbVersionNo(String.valueOf(DbHelper.DATABASE_VERSION));
            String verCode = getVersionCode();
            posSetting.setPosVersionNo(verCode);
            String jsonString = mapper.writeValueAsString(posSetting);
            if (SETTINGS.posID!=null) {
                String res = messageTransmit.authPut(ApiURL.PosSetting, jsonString, token, Long.parseLong(SETTINGS.posID));
                updatePosVersion(posSetting);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(res);
                    if (jsonObject.getString(MessageKey.status).equals("200")) {
                        DbHelper.DATABASE_ENABEL_POS_SETTING=true;
                    } else {
                        Log.d("updatePossSetting", jsonObject.getString(MessageKey.responseType));
                    }
                } catch (JSONException e) {
                    Log.e("exceptionUpdatePossSetting", e.toString());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void updatePosVersion(PosSetting posSetting){
        PosSettingDbAdapter posSettingDbAdapter=new PosSettingDbAdapter(DashBord.this);
        posSettingDbAdapter.open();
        long idPosSetting=posSettingDbAdapter.getPosSettingID().getPosSettingId();
        posSettingDbAdapter.updatePossVersion(posSetting,idPosSetting);
        posSettingDbAdapter.close();
    }

    private void checkCurrency(OpiningReport _aReport){
        CurrencyDBAdapter currencyDBAdapter =new CurrencyDBAdapter(DashBord.this);
        currencyDBAdapter.open();
        Currency lastCurrency = null;
        try {
            lastCurrency = currencyDBAdapter.getLastCurrency();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (lastCurrency!=null){
            ShowAReportDialog(_aReport);
        }
        else {
                Toast.makeText(DashBord.this, "Wait to get currency please try again!", Toast.LENGTH_LONG).show();
            try {
                updateCurrency();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getVersionCode(){
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String verCode = pInfo.versionName;
        return verCode;

    }

}

