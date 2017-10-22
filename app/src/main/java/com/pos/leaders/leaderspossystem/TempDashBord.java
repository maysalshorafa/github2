package com.pos.leaders.leaderspossystem;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.AReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ScheduleWorkersDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UserDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.AReport;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyOperation;
import com.pos.leaders.leaderspossystem.Models.User;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Tools.InternetStatus;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.TempCashActivty;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.Service.SyncMessage;

import java.util.Date;

import static com.pos.leaders.leaderspossystem.SetupNewPOSOnlineActivity.BO_CORE_ACCESS_AUTH;
import static com.pos.leaders.leaderspossystem.SetupNewPOSOnlineActivity.BO_CORE_ACCESS_TOKEN;

public class TempDashBord  extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private boolean enableBackButton = true;
    Button mainScreen,report ,product ,department,users,backUp,customerClub,logOut,tax,offers,houseOfWork ,settings;
    String permissions_name;
    AReportDBAdapter aReportDBAdapter;
    User user=new User();
    UserDBAdapter userDBAdapter;
    ScheduleWorkersDBAdapter scheduleWorkersDBAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_temp_dash_bord);

        if (SyncMessage.isConnected(this)) {
            SESSION.internetStatus = InternetStatus.CONNECTED;
        } else {
            SESSION.internetStatus = InternetStatus.ERROR;
        }
        TitleBar.setTitleBar(this);


        AccessToken accessToken = new AccessToken(this);
        accessToken.execute(this);
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
        mainScreen=(Button) findViewById(R.id.mainScreen);
        report=(Button) findViewById(R.id.report);
        product=(Button) findViewById(R.id.product);
        department=(Button) findViewById(R.id.department);
        tax=(Button) findViewById(R.id.tax);
        offers=(Button) findViewById(R.id.offers);
        users=(Button) findViewById(R.id.users);
        backUp=(Button) findViewById(R.id.backUp);
        logOut=(Button) findViewById(R.id.logOut);
        houseOfWork=(Button) findViewById(R.id.houseOfWork);
        customerClub=(Button) findViewById(R.id.coustmerClub);
        settings=(Button) findViewById(R.id.settings);
        aReportDBAdapter = new AReportDBAdapter(this);
        userDBAdapter = new UserDBAdapter(this);
        userDBAdapter.open();
        aReportDBAdapter.open();
        Bundle bundle = getIntent().getExtras();
        permissions_name = bundle.getString("permissions_name");
        Toast.makeText(TempDashBord.this, permissions_name, Toast.LENGTH_LONG).show();
       onClickedImage();

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TempDashBord.this, LogInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                try {
                    scheduleWorkersDBAdapter.updateEntry(SESSION._SCHEDULEWORKERS.getId(), new Date());
                    SESSION._SCHEDULEWORKERS.setExitTime(new Date());
                    Log.i("Worker get out", SESSION._SCHEDULEWORKERS.toString());
                } catch (Exception ex) {
                }
                SESSION._LogOut();
                startActivity(intent);
            }
        });

    }
    public void onClickedImage() {
                if ((permissions_name.toLowerCase().contains("main screen"))) {

                    mainScreen.setClickable(true);
                    mainScreen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //// TODO: 22/10/2016 cancel and return to previous activity
                            Intent i;
                            i = new Intent(getApplicationContext(), MainActivity.class);
                            i.putExtra("permissions_name", permissions_name);
                            startActivity(i);
                        }
                    });

                }

 if ((permissions_name.toLowerCase().contains("report"))) {


            report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //// TODO: 22/10/2016 cancel and return to previous activity
                    Intent i;
                    i = new Intent(getApplicationContext(), ReportsManagementActivity.class);
                    i.putExtra("permissions_name", permissions_name);
                    startActivity(i);
                }
            });}


            else     if ((permissions_name.toLowerCase().contains("product"))) {
                    product.setClickable(true);

                    product.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //// TODO: 22/10/2016 cancel and return to previous activity
                            Intent i;
                            i = new Intent(getApplicationContext(), ProductsActivity.class);
                            i.putExtra("permissions_name", permissions_name);
                            startActivity(i);
                        }
                    });}


       if ((permissions_name.toLowerCase().contains("department"))) {


            department.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //// TODO: 22/10/2016 cancel and return to previous activity
                    Intent i;
                    i = new Intent(getApplicationContext(), DepartmentActivity.class);
                    i.putExtra("permissions_name", permissions_name);
                    startActivity(i);
                }
            });}

               if ((permissions_name.toLowerCase().contains("users"))) {
                    users.setClickable(true);


                    users.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //// TODO: 22/10/2016 cancel and return to previous activity
                            Intent i;
                            i = new Intent(getApplicationContext(), AddUserActivity.class);
                            i.putExtra("permissions_name", permissions_name);
                            startActivity(i);
                        }
                    });}

             if ((permissions_name.toLowerCase().contains("offers"))) {
                  offers.setClickable(true);

                    }

             if ((permissions_name.toLowerCase().contains("back up"))) {
                    backUp.setClickable(true);


                    backUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //// TODO: 22/10/2016 cancel and return to previous activity
                            Intent i;
                            i = new Intent(getApplicationContext(), BackupActivity.class);
                            i.putExtra("permissions_name", permissions_name);
                            startActivity(i);
                        }
                    });}
                 if ((permissions_name.toLowerCase().contains("settings"))) {


                    settings.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //// TODO: 22/10/2016 cancel and return to previous activity
                            Intent i;
                            i = new Intent(getApplicationContext(),SettingActivity.class);
                            i.putExtra("permissions_name", permissions_name);
                            startActivity(i);
                        }
                    });}


if ((permissions_name.toLowerCase().contains("customer club"))) {
                   customerClub.setClickable(true);

                    final String[] items = {
                            "ADD Custmer",
                            "Show Custmer",
                            "ADD Club",
                            "Show Club",
                    };
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(TempDashBord.this);
                    builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            Intent intent;
                            switch (item) {
                                case 0:
                                    intent = new Intent(TempDashBord.this, AddNewCoustmer.class);
                                    startActivity(intent);
                                    break;
                                case 1:
                                    intent = new Intent(TempDashBord.this, CustmerMangmentActivity.class);
                                    intent.putExtra("permissions_name", permissions_name);
                                    startActivity(intent);
                                    break;
                                case 2:
                                    intent = new Intent(TempDashBord.this, Coustmer_Group.class);
                                    startActivity(intent);
                                    break;
                                case 3:
                                    intent = new Intent(TempDashBord.this, ClubMangmentActivity.class);
                                    startActivity(intent);
                                    break;

                            }
                        }
                    });
                    android.app.AlertDialog alert = builder.create();
                    alert.show();
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
                //its have a report

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

        //there is no a report after z report
        enableBackButton = false;
        final Dialog discountDialog = new Dialog(TempDashBord.this);
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
                    AReportDBAdapter aReportDBAdapter = new AReportDBAdapter(TempDashBord.this);
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

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
