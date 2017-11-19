package com.pos.leaders.leaderspossystem;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.Switch;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.CustomerAndClub.Customer;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.AReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PermissionsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.SaleDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ScheduleWorkersDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UserDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.AReport;
import com.pos.leaders.leaderspossystem.Models.Permission.Permissions;
import com.pos.leaders.leaderspossystem.Models.Sale;
import com.pos.leaders.leaderspossystem.Models.User;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.InternetStatus;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.Service.SyncMessage;

import java.util.ArrayList;
import java.util.Date;

import static com.pos.leaders.leaderspossystem.SetupNewPOSOnlineActivity.BO_CORE_ACCESS_AUTH;
import static com.pos.leaders.leaderspossystem.SetupNewPOSOnlineActivity.BO_CORE_ACCESS_TOKEN;

public class TempDashBord  extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private boolean enableBackButton = true;
    Button mainScreen, report, product, department, users, backUp, customerClub, logOut, offers, settings;
    Button btZReport, btAReport;
    AReportDBAdapter aReportDBAdapter;
    User user = new User();
    UserDBAdapter userDBAdapter;
    ArrayList<Integer> permissions_name;
    ArrayList<Permissions> permissions = new ArrayList<Permissions>();
    ScheduleWorkersDBAdapter scheduleWorkersDBAdapter;
    Intent i;

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
        if (sharedpreferences.contains(MessageKey.PosId)) {
            int posID = Integer.parseInt(sharedpreferences.getString(MessageKey.PosId, "1"));
            SESSION.POS_ID_NUMBER = posID;
        }
        //load token from shared file
        sharedpreferences = getSharedPreferences(BO_CORE_ACCESS_TOKEN, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(MessageKey.Token)) {
            String token = sharedpreferences.getString(MessageKey.Token, null);
            SESSION.token = token;
        }


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



        mainScreen = (Button) findViewById(R.id.mainScreen);
        btAReport = (Button) findViewById(R.id.dashboard_btAreport);
        btZReport = (Button) findViewById(R.id.dashboard_btZreport);
        report = (Button) findViewById(R.id.report);
        product = (Button) findViewById(R.id.product);
        department = (Button) findViewById(R.id.department);
        offers = (Button) findViewById(R.id.offers);
        users = (Button) findViewById(R.id.users);
        backUp = (Button) findViewById(R.id.backUp);
        logOut = (Button) findViewById(R.id.logOut);
        customerClub = (Button) findViewById(R.id.coustmerClub);
        settings = (Button) findViewById(R.id.settings);

        EnableButtons();
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


        //region a report button
        btAReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AReport _aReport = new AReport();
                _aReport.setByUserID(SESSION._USER.getId());
                _aReport.setCreationDate(new Date().getTime());

                AReport aReport = getLastAReport();
                ZReport zReport = getLastZReport();

                if (aReport == null) {
                    _aReport.setLastZReportID(-1);
                    _aReport.setLastSaleID(-1);

                    ShowAReportDialog(_aReport);
                }
                else{
                    _aReport.setLastZReportID(zReport.getId());
                    _aReport.setLastSaleID(zReport.getEndSaleId());

                    ShowAReportDialog(_aReport);
                }

            }
        });
        //endregion a report button

        //region z report button
        btZReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sale lastSale;
                ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(TempDashBord.this);
                zReportDBAdapter.open();
                ZReport lastZReport = getLastZReport();

                if(lastZReport == null) {
                    lastZReport = new ZReport();
                    lastZReport.setEndSaleId(0);
                }
                SaleDBAdapter saleDBAdapter = new SaleDBAdapter(TempDashBord.this);
                saleDBAdapter.open();
                lastSale = saleDBAdapter.getLast();
                saleDBAdapter.close();

                ZReport z=new ZReport(0, DateConverter.stringToDate(DateConverter.currentDateTime()) , SESSION._USER,lastZReport.getEndSaleId()+1,lastSale);
                z.setByUser(SESSION._USER.getId());
                long zID = zReportDBAdapter.insertEntry(z.getCreationDate().getTime(), z.getByUser(), z.getStartSaleId(), z.getEndSaleId());
                z.setId(zID);
                lastZReport = new ZReport(z);
                zReportDBAdapter.close();
                PrintTools pt = new PrintTools(TempDashBord.this);

                //create and print z report
                Bitmap bmap = pt.createZReport(lastZReport.getId(), lastZReport.getStartSaleId(), lastZReport.getEndSaleId(), false);
                if(bmap!=null)
                    pt.PrintReport(bmap);

                Intent i=new Intent(TempDashBord.this,ReportZDetailsActivity.class);
                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_ID,lastZReport.getId());
                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_FORM,lastZReport.getStartSaleId());
                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_TO,lastZReport.getEndSaleId());
                startActivity(i);
                btZReport.setEnabled(false);
            }
        });
        //endregion z report button

        mainScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), MainActivity.class);
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
        department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), DepartmentActivity.class);
                startActivity(i);
            }
        });
        backUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), BackupActivity.class);
                startActivity(i);
            }
        });
        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), WorkerManagementActivity.class);
                i.putIntegerArrayListExtra("permissions_name", permissions_name);
                startActivity(i);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(i);
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
        offers.setEnabled(false);
        users.setEnabled(false);
        backUp.setEnabled(false);
        department.setEnabled(false);
        department.setEnabled(false);
        customerClub.setEnabled(false);
        settings.setEnabled(false);
        btAReport.setEnabled(false);
        btZReport.setEnabled(false);
        mainScreen.setEnabled(false);
    }

    public void EnableButtons() {
        for (Permissions p:permissions) {
            switch (p.getName()) {
                case Permissions.PERMISSIONS_MAIN_SCREEN:
                    if (needAReport()) {
                        btAReport.setEnabled(true);
                        btZReport.setEnabled(false);
                        mainScreen.setEnabled(false);
                    } else {
                        btAReport.setEnabled(false);
                        btZReport.setEnabled(true);
                        mainScreen.setEnabled(true);
                    }
                    break;

                case Permissions.PERMISSIONS_REPORT:
                    report.setEnabled(true);
                    break;
                case Permissions.PERMISSIONS_PRODUCT:
                    product.setEnabled(true);
                    break;
                case Permissions.PERMISSIONS_DEPARTMENT:
                    department.setEnabled(true);
                    break;
                case Permissions.PERMISSIONS_USER:
                    users.setEnabled(true);
                    break;
                case Permissions.PERMISSIONS_OFFER:
                    offers.setEnabled(true);
                    break;
                case Permissions.PERMISSIONS_BACK_UP:
                    backUp.setEnabled(true);
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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String str = extras.getString(LogInActivity.LEADPOS_MAKE_A_REPORT);
            if (str.equals(LogInActivity.LEADPOS_MAKE_A_REPORT)) {
                extras.clear();
            }
        }
        EnableButtons();
    }

    private boolean needAReport(){
        ZReport zReport = getLastZReport();
        AReport aReport = getLastAReport();


        if (aReport != null && zReport != null) {
            if (aReport.getLastZReportID() == zReport.getId()) {

            } else {
                return true;
            }

        } else if (aReport == null) {
            return true;
        }
        return false;
    }

    private ZReport getLastZReport(){
        ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(this);

        zReportDBAdapter.open();
        ZReport zReport = null;
        try {
            zReport = zReportDBAdapter.getLastRow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        zReportDBAdapter.close();
        return zReport;
    }

    private AReport getLastAReport(){
        AReportDBAdapter aReportDBAdapter = new AReportDBAdapter(this);
        aReportDBAdapter.open();
        AReport aReport = null;

        try {
            aReport = aReportDBAdapter.getLastRow();

        } catch (Exception e) {
            e.printStackTrace();
        }

        aReportDBAdapter.close();
        return aReport;
    }

    private void ShowAReportDialog(final AReport aReport) {
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

        discountDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                TempDashBord.this.onResume();
            }
        });

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
                    aReportDBAdapter.insertEntry(aReport.getCreationDate(), aReport.getByUserID(), aReport.getAmount(), aReport.getLastSaleID(), aReport.getLastZReportID());
                    aReportDBAdapter.close();
                    discountDialog.cancel();
                }
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
