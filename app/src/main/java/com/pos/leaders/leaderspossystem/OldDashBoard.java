package com.pos.leaders.leaderspossystem;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.CustomerAndClub.AddNewCustomer;
import com.pos.leaders.leaderspossystem.CustomerAndClub.Coustmer_Group;
import com.pos.leaders.leaderspossystem.CustomerAndClub.CustmerManagementActivity;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.AReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Dash_bord_adapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ScheduleWorkersDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UserDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.AReport;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyOperation;
import com.pos.leaders.leaderspossystem.Models.User;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Tools.InternetStatus;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.Service.SyncMessage;

import java.text.DateFormat;
import java.util.Date;

import static com.pos.leaders.leaderspossystem.SetupNewPOSOnlineActivity.BO_CORE_ACCESS_AUTH;
import static com.pos.leaders.leaderspossystem.SetupNewPOSOnlineActivity.BO_CORE_ACCESS_TOKEN;

public class OldDashBoard extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String PREFS_NAME = "Time_Pref";
    private boolean enableBackButton = true;
    DateFormat format;
    ImageView im;
    GridView grid;
    String permissions_name;
    AReportDBAdapter aReportDBAdapter;
    User user=new User();
    UserDBAdapter userDBAdapter;
    ScheduleWorkersDBAdapter scheduleWorkersDBAdapter;
    long currencyType;
    String[] dashbord_text = {
            "Main Screen",
            "Report",
            "Product",
            "Department",
            "Users",
            "Offers",
            "BackUp",
            "Tax",
            "Hours Of Work",
            "OldCustomer Club",
            "Log Out"

    };
    int[] imageId = {
            R.drawable.dash_bord_home,
            R.drawable.report,
            R.drawable.dash_bord_product,
            R.drawable.dash_bord_department,
            R.drawable.dash_bord_user,
            R.drawable.dash_bord_offer,
            R.drawable.dash_bord_backup,
            R.drawable.dash_bord_tax,
            R.drawable.dash_bord_hoursofwork,
            R.drawable.dash_bord_custmer,
            R.drawable.dash_bord_log_out


    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dash_board);

        if (SyncMessage.isConnected(this)) {
            SESSION.internetStatus = InternetStatus.CONNECTED;
        } else {
            SESSION.internetStatus = InternetStatus.ERROR;
        }
        TitleBar.setTitleBar(this);


        AccessToken accessToken = new AccessToken(this);
        accessToken.execute(this);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //load pos id from shared file
        SharedPreferences sharedpreferences = getSharedPreferences(BO_CORE_ACCESS_AUTH, Context.MODE_PRIVATE);
        if(sharedpreferences.contains(MessageKey.PosId)){
            int posID = Integer.parseInt(sharedpreferences.getString(MessageKey.PosId, "1"));
            SESSION.POS_ID_NUMBER = posID;
        }
        //load token from shared file
        sharedpreferences = getSharedPreferences(BO_CORE_ACCESS_TOKEN, Context.MODE_PRIVATE);
        if(sharedpreferences.contains(MessageKey.Token)){
            String token = sharedpreferences.getString(MessageKey.Token, null);
            SESSION.token = token;
        }
        Log.i("Token", SESSION.token);

        im = (ImageView) findViewById(R.id.home);
        Dash_bord_adapter adapter = new Dash_bord_adapter(OldDashBoard.this, dashbord_text, imageId);
        aReportDBAdapter = new AReportDBAdapter(this);
        userDBAdapter = new UserDBAdapter(this);
        userDBAdapter.open();
        aReportDBAdapter.open();
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "Rubik-Bold.ttf");
        grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(adapter);
        Bundle bundle = getIntent().getExtras();
        permissions_name = bundle.getString("permissions_name");
        Toast.makeText(OldDashBoard.this, permissions_name, Toast.LENGTH_LONG).show();

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                onClickedImage(position);
            }
        });
    }

    public void onClickedImage(int pos) {
        Intent i;
        switch (pos) {

            case 0://1.popUp selection 2.Main activity
                if (!(permissions_name.toLowerCase().contains("main screen"))) {
                    grid.getChildAt(pos).setClickable(false);
                } else {
                    i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra("permissions_name", permissions_name);
                    startActivity(i);

                }
                break;
            case 1:
                if (!(permissions_name.toLowerCase().contains("report"))) {
                    grid.getChildAt(pos).setClickable(false);
                } else {
                    i = new Intent(getApplicationContext(), ReportsManagementActivity.class);
                    startActivity(i);
                }
                break;
            case 2:
                if (!(permissions_name.toLowerCase().contains("product"))) {
                    grid.getChildAt(pos).setClickable(false);
                } else {
                    i = new Intent(getApplicationContext(), ProductCatalogActivity.class);
                    startActivity(i);
                }
                break;
            case 3:

                if (!(permissions_name.toLowerCase().contains("department"))) {
                    grid.getChildAt(pos).setClickable(false);
                } else {
                    i = new Intent(getApplicationContext(), DepartmentActivity.class);
                    startActivity(i);
                }
                break;
            case 4:
                if (!(permissions_name.toLowerCase().contains("users"))) {
                    grid.getChildAt(pos).setClickable(false);
                } else {
                    i = new Intent(getApplicationContext(), AddUserActivity.class);
                    startActivity(i);
                }
                break;
            case 5:
                if (!(permissions_name.toLowerCase().contains("offers"))) {
                    grid.getChildAt(pos).setClickable(false);
                } else {

                    /**   i = new Intent(getApplicationContext(), OfferActivity.class);
                     startActivity(i);
                     **/}
                break;
            case 6:
                if (!(permissions_name.toLowerCase().contains("back up"))) {
                    grid.getChildAt(pos).setClickable(false);
                } else {
                    i = new Intent(getApplicationContext(), BackupActivity.class);
                    startActivity(i);
                }
                break;
            case 7:

                break;
            case 8:

                break;
            case 9:
                if (!(permissions_name.toLowerCase().contains("user club"))) {
                    grid.getChildAt(pos).setClickable(false);
                } else {
                    final String[] items = {
                            "ADD Custmer",
                            "Show Custmer",
                            "ADD Club",
                    };
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(OldDashBoard.this);
                    builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            Intent intent;
                            switch (item) {
                                case 0:
                                    intent = new Intent(OldDashBoard.this, AddNewCustomer.class);
                                    startActivity(intent);
                                    break;
                                case 1:
                                    intent = new Intent(OldDashBoard.this, CustmerManagementActivity.class);
                                    intent.putExtra("permissions_name", permissions_name);
                                    startActivity(intent);
                                    break;
                                case 2:
                                    intent = new Intent(OldDashBoard.this, Coustmer_Group.class);
                                    startActivity(intent);
                                    break;

                            }
                        }
                    });
                    android.app.AlertDialog alert = builder.create();
                    alert.show();
                }
                break;
            case 10:
                Intent intent = new Intent(OldDashBoard.this, LogInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                try {
                    scheduleWorkersDBAdapter.updateEntry(SESSION._SCHEDULEWORKERS.getId(), new Date());
                    SESSION._SCHEDULEWORKERS.setExitTime(new Date().getTime());
                    Log.i("Worker get out", SESSION._SCHEDULEWORKERS.toString());
                } catch (Exception ex) {
                }
                SESSION._LogOut();
                startActivity(intent);
        }
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
            }
        }
    }

    private void createAReport() {

        final AReport _aReport = new AReport();
        ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(this);

        zReportDBAdapter.open();
        ZReport zReport = null;
        CurrencyOperation currencyOperation=null;
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


            if (aReport.getLastZReportID() == zReport.getId()) {
                //its have customerName report

            } else {
                _aReport.setLastZReportID(zReport.getId());
                _aReport.setLastSaleID(zReport.getEndSaleId());

                ShowAReportDialog(_aReport);
            }

        } else if(aReport==null) {
            _aReport.setByUserID(SESSION._USER.getId());
            _aReport.setCreationDate(new Date().getTime());
            _aReport.setLastZReportID(-1);
            _aReport.setLastSaleID(-1);

            ShowAReportDialog(_aReport);
        }
    }

    private void ShowAReportDialog(final AReport aReport) {
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(this);

        //there is no customerName report after z report
        enableBackButton = false;
        final Dialog discountDialog = new Dialog(OldDashBoard.this);
        discountDialog.setTitle(R.string.opening_report);
        discountDialog.setContentView(R.layout.cash_payment_dialog);
        discountDialog.setCancelable(false);

        final Button btOK = (Button) discountDialog.findViewById(R.id.cashPaymentDialog_BTOk);
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

        btOK.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                String str = et.getText().toString();
                if (!str.equals("")) {
                    aReport.setAmount(Double.parseDouble(str));
                    AReportDBAdapter aReportDBAdapter = new AReportDBAdapter(OldDashBoard.this);
                    aReportDBAdapter.open();
                    aReportDBAdapter.insertEntry(aReport.getCreationDate(),aReport.getByUserID(),aReport.getAmount(),aReport.getLastSaleID(),aReport.getLastZReportID());
                    aReportDBAdapter.close();
                    discountDialog.cancel();
                }
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                //discountDialog.cancel();
            }
        });
        discountDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currencyType = parent.getSelectedItemId();
        Toast.makeText(OldDashBoard.this, "mays" + currencyType, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}