package com.pos.leaders.leaderspossystem;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.AReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Dash_bord_adapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ScheduleWorkersDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UserDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.AReport;
import com.pos.leaders.leaderspossystem.Models.User;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Reports.UserAttendanceReport;
import com.pos.leaders.leaderspossystem.Tools.SESSION;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import jxl.write.DateTime;

public class DashBoard extends AppCompatActivity {
    public static final String PREFS_NAME = "Time_Pref";
    private boolean enableBackButton = true;

    ImageView im;
    GridView grid;
    String permissions_name;
    PopupWindow popupWindow;
    EditText custmer_id;
    Button btLogOut;
    AReportDBAdapter aReportDBAdapter;
    User user=new User();
    UserDBAdapter userDBAdapter;
    AReport aReport=new AReport();
    ScheduleWorkersDBAdapter scheduleWorkersDBAdapter;

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
            "Coustmer Club"

    };
    int[] imageId = {
            R.drawable.home,
            R.drawable.reports,
            R.drawable.products,
            R.drawable.departments,
            R.drawable.users,
            R.drawable.offers,
            R.drawable.backup,
            R.drawable.tax,
            R.drawable.hours,
            R.drawable.coustmer


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_dash_board);
        //this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title_bar);

        im = (ImageView) findViewById(R.id.home);
        Dash_bord_adapter adapter = new Dash_bord_adapter(DashBoard.this, dashbord_text, imageId);
        aReportDBAdapter=new AReportDBAdapter(this);
        userDBAdapter=new UserDBAdapter(this);
        userDBAdapter.open();
        aReportDBAdapter.open();
        btLogOut = (Button) findViewById(R.id.log_out);

        grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(adapter);
        Bundle bundle = getIntent().getExtras();
        permissions_name = bundle.getString("permissions_name");
        Toast.makeText(DashBoard.this,permissions_name,Toast.LENGTH_LONG).show();

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                // Send intent to SingleViewActivity
                onClickedImage(position);


                // Intent i = new Intent(getApplicationContext(), SingleViewActivity.class);
                // Pass image index
                // i.putExtra("id", position);
                //startActivity(i);
            }
        });



        btLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoard.this, LogInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                try {
                    scheduleWorkersDBAdapter.updateEntry(SESSION._SCHEDULEWORKERS.getId(), new Date());
                    SESSION._SCHEDULEWORKERS.setExitTime(new Date());
                    Log.i("Worker get out", SESSION._SCHEDULEWORKERS.toString());
                }
                catch (Exception ex) {
                }
                SESSION._LogOut();
                startActivity(intent);
            }
        });
        }


  /**  public void alertdialoge() {
        AlertDialog alertDialog1 = new AlertDialog.Builder(DashBoard.this).create();
        alertDialog1.setTitle("Alert");
        alertDialog1.setMessage("This Payment With Custmer");
        alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                   callPopup();

                    }
                });
        alertDialog1.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        dialog.dismiss();
                    }
                });
        alertDialog1.show();


    }**/

    public void onClickedImage(int pos) {
        Intent i;
        switch (pos) {

            case 0://1.popUp selection 2.Main activity


                if (!(permissions_name.toLowerCase().contains("main screen"))) {
                    //im.setVisibility(View.GONE);
                    grid.getChildAt(pos).setClickable(false);
                    //       im.setClickable(false);


                } else {
                    i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra("permissions_name",permissions_name);
                    startActivity(i);
 /**
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

  Date dateToday = new Date();

  String date = sdf.format(dateToday);
  String s = Objects.toString(aReport.getCreationDate(), null); if(!(user.getId()==aReport.getByUserID()&& date==s)){

       final AlertDialog alertDialog = new AlertDialog.Builder(DashBoard.this).create();
       alertDialog.setTitle("Alert");
       alertDialog.setMessage("Did you create A report?");
       alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
               new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
                       Intent i = new Intent(getApplicationContext(), MainActivity.class);
                       startActivity(i);
                       dialog.dismiss();
                   }
               });
       alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
               new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
                       Intent i = new Intent(getApplicationContext(), UserAttendanceReport.class);
                       startActivity(i);
                       dialog.dismiss();
                   }
               });
       alertDialog.show();**/


                    }





                break;
            case 1:
                if (!(permissions_name.toLowerCase().contains("report"))) {
                    //im.setVisibility(View.GONE);
                    grid.getChildAt(pos).setClickable(false);
                    //       im.setClickable(false);


                } else {
                    i = new Intent(getApplicationContext(), ReportsManagementActivity.class);
                    startActivity(i);
                }
                break;
            case 2:
                if (!(permissions_name.toLowerCase().contains("product"))) {
                    //im.setVisibility(View.GONE);
                    grid.getChildAt(pos).setClickable(false);
                    //       im.setClickable(false);


                } else {
                    i = new Intent(getApplicationContext(), ProductCatalogActivity.class);
                    startActivity(i);
                }
                break;
            case 3:

                if (!(permissions_name.toLowerCase().contains("department"))) {
                    //im.setVisibility(View.GONE);
                    grid.getChildAt(pos).setClickable(false);
                    //       im.setClickable(false);


                } else {
                    i = new Intent(getApplicationContext(), DepartmentActivity.class);
                    startActivity(i);
                }
                break;
            case 4:
                if (!(permissions_name.toLowerCase().contains("users"))) {
                    //im.setVisibility(View.GONE);
                    grid.getChildAt(pos).setClickable(false);
                    //       im.setClickable(false);


                } else {
                    i = new Intent(getApplicationContext(), AddUserActivity.class);
                    startActivity(i);
                }
                break;
            case 5:
                if (!(permissions_name.toLowerCase().contains("offers"))) {
                    //im.setVisibility(View.GONE);
                    grid.getChildAt(pos).setClickable(false);
                    //       im.setClickable(false);


                } else {

                 /**   i = new Intent(getApplicationContext(), OfferActivity.class);
                    startActivity(i);
                **/}
                break;
            case 6:
                if (!(permissions_name.toLowerCase().contains("back up"))) {
                    //im.setVisibility(View.GONE);
                    grid.getChildAt(pos).setClickable(false);
                    //       im.setClickable(false);


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
                if (!(permissions_name.toLowerCase().contains("customer club"))) {
                    //im.setVisibility(View.GONE);
                    grid.getChildAt(pos).setClickable(false);
                    //       im.setClickable(false);


                } else {




                    final String[] items = {
                           "ADD Custmer",
                            "Show Custmer",
                           "ADD Club",
                            };
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DashBoard.this);
                    builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            Intent intent;
                            switch (item) {
                                case 0:
                                    intent = new Intent(DashBoard.this, AddNewCoustmer.class);
                                    startActivity(intent);
                                    break;
                                case 1:
                                    intent = new Intent(DashBoard.this, CustmerMangmentActivity.class);
                                    intent.putExtra("permissions_name",permissions_name);
                                    startActivity(intent);
                                    break;
                                case 2:
                                    intent = new Intent(DashBoard.this, Coustmer_Group.class);
                                    startActivity(intent);
                                    break;

                            }
                        }
                    });
                    android.app.AlertDialog alert = builder.create();
                    alert.show();
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
        final Dialog discountDialog = new Dialog(DashBoard.this);
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
                    AReportDBAdapter aReportDBAdapter = new AReportDBAdapter(DashBoard.this);
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

/**    private void callPopup() {

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.pop_up, null);

        popupWindow = new PopupWindow(popupView,
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT,
                true);

        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        custmer_id = (EditText) popupView.findViewById(R.id.custmer_id);

        ((Button) popupView.findViewById(R.id.btn_add))
                .setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(View arg0) {
                        Toast.makeText(getApplicationContext(),
                                custmer_id.getText().toString(),Toast.LENGTH_LONG).show();
                     //   String s= custmer_id.getText().toString();

                 Intent i = new Intent(DashBoard.this, MainActivity.class);
                 //       i.putExtra("custmer_id",s);
                        startActivity(i);

                        popupWindow.dismiss();

                }});


    }**/
}