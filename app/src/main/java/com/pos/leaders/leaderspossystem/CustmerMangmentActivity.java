package com.pos.leaders.leaderspossystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Customer_M;
import com.pos.leaders.leaderspossystem.Tools.CustmerCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CustmerMangmentActivity extends AppCompatActivity {
    android.support.v7.app.ActionBar actionBar;

    List<Customer_M> custmers;
    CustomerDBAdapter customerDBAdapter;
    GridView gvcustmer;
    Button btAddCustmer,btCancel;
    private static final int CHANGE_PASSWORD_DIALOG = 656;
    Customer_M customer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_custmer_mangment);


        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.title_bar,
                null);

        // Set up your ActionBar
        actionBar = getSupportActionBar();
        // TODO: Remove the redundant calls to getSupportActionBar()
        //       and use variable actionBar instead
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        Calendar ca = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        // You customization
        final int actionBarColor = getResources().getColor(R.color.primaryColor);
        actionBar.setBackgroundDrawable(new ColorDrawable(actionBarColor));

        final TextView actionBarTitle = (TextView) findViewById(R.id.date);
        actionBarTitle.setText(format.format(ca.getTime()));
        final TextView actionBarSent = (TextView) findViewById(R.id.posID);
        actionBarSent.setText("POSID  "+ SESSION.POS_ID_NUMBER);


        final TextView actionBarStaff = (TextView) findViewById(R.id.userName);
        actionBarStaff.setText(SESSION._USER.getFullName());
        final TextView actionBarLocations = (TextView) findViewById(R.id.userPermtions);
        actionBarLocations.setText(" "+SESSION._USER.getPermtionName());


        gvcustmer = (GridView) findViewById(R.id.custmerManagement_GVCustmer);
            btAddCustmer = (Button) findViewById(R.id.custmerManagement_BTNewCustmer);
            btCancel = (Button) findViewById(R.id.custmerManagement_BTCancel);

            //region Buttons

            btAddCustmer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CustmerMangmentActivity.this, AddNewCoustmer.class);
                    startActivity(intent);
                }
            });

            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CustmerMangmentActivity.this, Coustmer.class);
                    startActivity(intent);
                }
            });


            //endregion


            customerDBAdapter= new CustomerDBAdapter(this);
            customerDBAdapter.open();
            custmers = customerDBAdapter.getAllCustmer();
            customer = null;

            final CustmerCatalogGridViewAdapter adapter = new CustmerCatalogGridViewAdapter(this, custmers);
            gvcustmer.setAdapter(adapter);
            gvcustmer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    final String[] items = {
                            getString(R.string.view),
                            getString(R.string.edit),
                            getString(R.string.delete),
                           };
                    AlertDialog.Builder builder = new AlertDialog.Builder(CustmerMangmentActivity.this);
                    builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            Intent intent;
                            switch (item) {
                                case 0:
                                    intent = new Intent(CustmerMangmentActivity.this, AddNewCoustmer.class);
                                    intent.putExtra("id", custmers.get(position).getId());


                                    startActivity(intent);
                                case 1:
                                    intent = new Intent(CustmerMangmentActivity.this, AddNewCoustmer.class);
                                    intent.putExtra("id", custmers.get(position).getId());
                                    startActivity(intent);
                                    break;

                                case 2:
                                    new AlertDialog.Builder(CustmerMangmentActivity.this)
                                            .setTitle("Delete Custmer")
                                            .setMessage("Are you want to delete this Custmer?")
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    customerDBAdapter.deleteEntry(custmers.get(position).getId());
                                                    custmers.remove(custmers.get(position));
                                                    gvcustmer.setAdapter(adapter);
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
            });
        }

}
