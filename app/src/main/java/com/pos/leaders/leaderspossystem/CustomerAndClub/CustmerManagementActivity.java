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
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.CustomerCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.util.List;

public class CustmerManagementActivity extends AppCompatActivity {
    List<Customer> customers;
    CustomerDBAdapter customerDBAdapter;
    GridView gvCustomer;
    Button btAddCustmer, btCancel;
    public static int Customer_Management_View=0 ;
    public  static int  Customer_Management_Edit=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_custmer_mangment);

        TitleBar.setTitleBar(this);

        gvCustomer = (GridView) findViewById(R.id.customerManagementGVCustomer);
        btAddCustmer = (Button) findViewById(R.id.customerManagement_BTNewCustomer);
        btCancel = (Button) findViewById(R.id.customerManagement_BTCancel);
        btAddCustmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustmerManagementActivity.this, AddNewCustomer.class);
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
        customerDBAdapter = new CustomerDBAdapter(this);
        customerDBAdapter.open();
        customers = customerDBAdapter.getAllCustomer();
        final CustomerCatalogGridViewAdapter adapter = new CustomerCatalogGridViewAdapter(this, customers);
        gvCustomer.setAdapter(adapter);
        gvCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Customer_Management_View=0;
                Customer_Management_Edit=0;

                final String[] items = {
                        getString(R.string.view),
                        getString(R.string.edit),
                        getString(R.string.delete),
                };
                //List With CustomerMeasurement
                final String[] itemsWithCustomerMeasurement = {
                        getString(R.string.view),
                        getString(R.string.edit),
                        getString(R.string.delete),
                        getString(R.string.add_customer_measurement),
                        getString(R.string.show_customer_measurement),

                };
                if(!SETTINGS.enableCustomerMeasurement){
                AlertDialog.Builder builder = new AlertDialog.Builder(CustmerManagementActivity.this);
                builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent;
                        switch (item) {
                            case 0:
                                Customer_Management_View=9;
                                intent = new Intent(CustmerManagementActivity.this, AddNewCustomer.class);
                                intent.putExtra("id", customers.get(position).getId());
                                startActivity(intent);
                            case 1:
                                Customer_Management_Edit=10;
                                intent = new Intent(CustmerManagementActivity.this, AddNewCustomer.class);
                                intent.putExtra("id", customers.get(position).getId());
                                startActivity(intent);
                                break;

                            case 2:
                                new AlertDialog.Builder(CustmerManagementActivity.this)
                                        .setTitle(getString(R.string.delete)+" "+getString(R.string.customer))
                                        .setMessage(getString(R.string.delete_customer_message))
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
                // Alert Dialog With Customer Measurement
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CustmerManagementActivity.this);
                    builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                    builder.setItems(itemsWithCustomerMeasurement, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            Intent intent;
                            switch (item) {
                                case 0:
                                    Customer_Management_View=9;
                                    intent = new Intent(CustmerManagementActivity.this, AddNewCustomer.class);
                                    intent.putExtra("id", customers.get(position).getId());
                                    startActivity(intent);
                                case 1:
                                    Customer_Management_Edit=10;
                                    intent = new Intent(CustmerManagementActivity.this, AddNewCustomer.class);
                                    intent.putExtra("id", customers.get(position).getId());
                                    startActivity(intent);
                                    break;

                                case 2:
                                    new AlertDialog.Builder(CustmerManagementActivity.this)
                                            .setTitle(getString(R.string.delete)+" "+getString(R.string.customer))
                                            .setMessage(getString(R.string.delete_customer_message))
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
                                case 3:
                                    intent = new Intent(CustmerManagementActivity.this, AddCustomerMeasurement.class);
                                    intent.putExtra("id", customers.get(position).getId());
                                    startActivity(intent);
                                    break;
                                case 4 :
                                    intent = new Intent(CustmerManagementActivity.this, CustomerMeasurementManagementActivity.class);
                                    intent.putExtra("id", customers.get(position).getId());
                                    startActivity(intent);

                                    break;

                            }
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }


            }
        });
    }

}
