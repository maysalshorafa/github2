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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.pos.leaders.leaderspossystem.SetupNewPOSOnlineActivity.BO_CORE_ACCESS_AUTH;
import static com.pos.leaders.leaderspossystem.SetupNewPOSOnlineActivity.BO_CORE_ACCESS_TOKEN;

public class TempDashBord  extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private boolean enableBackButton = true;
    Button mainScreen,report ,product ,department,users,backUp,customerClub,logOut,tax,offers,houseOfWork ,settings;
    AReportDBAdapter aReportDBAdapter;
    User user=new User();
    UserDBAdapter userDBAdapter;
    ArrayList<Integer> permissions_name;
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
        if(bundle!=null){
     //   permissions_name = bundle.getString("permissions_name");
      permissions_name = getIntent().getIntegerArrayListExtra("permissions_name");
        }
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
                if (permissions_name.contains(1)) {

                    mainScreen.setClickable(true);
                    mainScreen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //// TODO: 22/10/2016 cancel and return to previous activity
                            Intent i;
                            i = new Intent(getApplicationContext(), MainActivity.class);
                            i.putIntegerArrayListExtra("permissions_name",  permissions_name);
                            startActivity(i);
                        }
                    });

                }


 if (permissions_name.contains(2)) {


            report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //// TODO: 22/10/2016 cancel and return to previous activity
                    Intent i;
                    i = new Intent(getApplicationContext(), ReportsManagementActivity.class);
                    i.putIntegerArrayListExtra("permissions_name",  permissions_name);
                    startActivity(i);
                }
            });}


            else     if (permissions_name.contains(3)) {
                    product.setClickable(true);

                    product.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //// TODO: 22/10/2016 cancel and return to previous activity
                            Intent i;
                            i = new Intent(getApplicationContext(), ProductsActivity.class);
                            i.putIntegerArrayListExtra("permissions_name",  permissions_name);
                            startActivity(i);
                        }
                    });}


       if (permissions_name.contains(4)) {


            department.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //// TODO: 22/10/2016 cancel and return to previous activity
                    Intent i;
                    i = new Intent(getApplicationContext(), DepartmentActivity.class);
                    i.putIntegerArrayListExtra("permissions_name",  permissions_name);
                    startActivity(i);
                }
            });}

               if (permissions_name.contains(5)) {
                    users.setClickable(true);


                    users.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //// TODO: 22/10/2016 cancel and return to previous activity
                            Intent i;
                            i = new Intent(getApplicationContext(), WorkerManagementActivity.class);
                            i.putIntegerArrayListExtra("permissions_name",  permissions_name);
                            startActivity(i);
                        }
                    });}

             if (permissions_name.contains(6)) {
                  offers.setClickable(true);

                    }

             if (permissions_name.contains(7)) {
                    backUp.setClickable(true);


                    backUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //// TODO: 22/10/2016 cancel and return to previous activity
                            Intent i;
                            i = new Intent(getApplicationContext(), BackupActivity.class);
                            i.putIntegerArrayListExtra("permissions_name",  permissions_name);
                            startActivity(i);
                        }
                    });}
                 if (permissions_name.contains(8)) {


                    settings.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //// TODO: 22/10/2016 cancel and return to previous activity
                            Intent i;
                            i = new Intent(getApplicationContext(),SettingActivity.class);
                            i.putIntegerArrayListExtra("permissions_name",  permissions_name);
                            startActivity(i);
                        }
                    });}
if (permissions_name.contains(9)) {
                   customerClub.setClickable(true);

    customerClub.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //// TODO: 22/10/2016 cancel and return to previous activity
            Intent i;
            i = new Intent(getApplicationContext(),Coustmer.class);
            i.putIntegerArrayListExtra("permissions_name",  permissions_name);
            startActivity(i);
        }
    });
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
