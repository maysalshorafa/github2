package com.pos.leaders.leaderspossystem.CustomerAndClub;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Customer_M;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.CustmerCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.util.List;

public class CustmerManagementActivity extends AppCompatActivity {
    List<Customer_M> customers;
    CustomerDBAdapter customerDBAdapter;
    GridView gvCustomer;
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

        TitleBar.setTitleBar(this);

        gvCustomer = (GridView) findViewById(R.id.custmerManagement_GVCustmer);
            btAddCustmer = (Button) findViewById(R.id.custmerManagement_BTNewCustmer);
            btCancel = (Button) findViewById(R.id.custmerManagement_BTCancel);

            //region Buttons

            btAddCustmer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CustmerManagementActivity.this, AddNewCoustmer.class);
                    startActivity(intent);
                }
            });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

            //endregion


            customerDBAdapter= new CustomerDBAdapter(this);
            customerDBAdapter.open();
            customers = customerDBAdapter.getAllCustmer();
            customer = null;

            final CustmerCatalogGridViewAdapter adapter = new CustmerCatalogGridViewAdapter(this, customers);
            gvCustomer.setAdapter(adapter);
            gvCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    final String[] items = {
                            getString(R.string.view),
                            getString(R.string.edit),
                            getString(R.string.delete),
                           };
                    AlertDialog.Builder builder = new AlertDialog.Builder(CustmerManagementActivity.this);
                    builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            Intent intent;
                            switch (item) {
                                case 0:
                                    intent = new Intent(CustmerManagementActivity.this, AddNewCoustmer.class);
                                    intent.putExtra("id", customers.get(position).getId());


                                    startActivity(intent);
                                case 1:
                                    intent = new Intent(CustmerManagementActivity.this, AddNewCoustmer.class);
                                    intent.putExtra("id", customers.get(position).getId());
                                    startActivity(intent);
                                    break;

                                case 2:
                                    new AlertDialog.Builder(CustmerManagementActivity.this)
                                            .setTitle("Delete Custmer")
                                            .setMessage("Are you want to delete this Custmer?")
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    customerDBAdapter.deleteEntry(customers.get(position).getId());
                                                    customers.remove(customers.get(position));
                                                    gvCustomer.setAdapter(adapter);
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
