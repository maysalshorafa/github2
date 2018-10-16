package com.pos.leaders.leaderspossystem.CustomerAndClub;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.CustomerCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class CustmerManagementActivity extends AppCompatActivity {
    List<Customer> customers;
    CustomerDBAdapter customerDBAdapter;
    GridView gvCustomer;
    Button btAddCustmer, btCancel;
    public static int Customer_Management_View=0 ;
    public  static int  Customer_Management_Edit=0;
    String upDateCustomerMxaCredit="";

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
                        getString(R.string.delete),getString(R.string.edit_max_customer_credit)
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
                                intent.putExtra("id", customers.get(position).getCustomerId());
                                startActivity(intent);
                            case 1:
                                Customer_Management_Edit=10;
                                intent = new Intent(CustmerManagementActivity.this, AddNewCustomer.class);
                                intent.putExtra("id", customers.get(position).getCustomerId());
                                startActivity(intent);
                                break;

                            case 2:
                                new AlertDialog.Builder(CustmerManagementActivity.this)
                                        .setTitle(getString(R.string.delete)+" "+getString(R.string.customer))
                                        .setMessage(getString(R.string.delete_customer_message))
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                customerDBAdapter.deleteEntry(customers.get(position).getCustomerId());
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
                                final Dialog cashCreditDialog = new Dialog(CustmerManagementActivity.this);
                                cashCreditDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                cashCreditDialog.show();
                                cashCreditDialog.setContentView(R.layout.activity_customer_credit);
                                final EditText etAmount = (EditText) cashCreditDialog.findViewById(R.id.maxCashCreditDialog);
                                Button btnOk = (Button)cashCreditDialog.findViewById(R.id.maxCashCreditDialog_BTOk);
                                btnOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(etAmount.getText().toString()!=null){
                                            new AsyncTask<Void, Void, Void>(){
                                                @Override
                                                protected void onPreExecute() {
                                                    super.onPreExecute();
                                                }
                                                @Override
                                                protected void onPostExecute(Void aVoid) {
                                                    JSONObject res = null;
                                                    try {
                                                        res = new JSONObject(upDateCustomerMxaCredit);
                                                        if(res.getString(MessageKey.status).equals("200")) {
                                                            Toast.makeText(CustmerManagementActivity.this, "Success Update max customer credit", Toast.LENGTH_LONG).show();
                                                        }else {
                                                            Toast.makeText(CustmerManagementActivity.this, "Failed Update max customer credit", Toast.LENGTH_LONG).show();

                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                    cashCreditDialog.dismiss();
                                                }
                                                @Override
                                                protected Void doInBackground(Void... voids) {
                                                    MessageTransmit transmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
                                                    try {
                                                         upDateCustomerMxaCredit=transmit.authUpdateGeneralLedger(ApiURL.GeneralLedger, String.valueOf(customers.get(position).getCustomerId()), SESSION.token,Double.parseDouble(etAmount.getText().toString()));
                                                        Log.i("log", upDateCustomerMxaCredit);


                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                    return null;
                                                }
                                            }.execute();
                                        }

                                    }
                                });
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
                                    intent.putExtra("id", customers.get(position).getCustomerId());
                                    startActivity(intent);
                                case 1:
                                    Customer_Management_Edit=10;
                                    intent = new Intent(CustmerManagementActivity.this, AddNewCustomer.class);
                                    intent.putExtra("id", customers.get(position).getCustomerId());
                                    startActivity(intent);
                                    break;

                                case 2:
                                    new AlertDialog.Builder(CustmerManagementActivity.this)
                                            .setTitle(getString(R.string.delete)+" "+getString(R.string.customer))
                                            .setMessage(getString(R.string.delete_customer_message))
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    customerDBAdapter.deleteEntry(customers.get(position).getCustomerId());
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
                                    intent.putExtra("id", customers.get(position).getCustomerId());
                                    startActivity(intent);
                                    break;
                                case 4 :
                                    intent = new Intent(CustmerManagementActivity.this, CustomerMeasurementManagementActivity.class);
                                    intent.putExtra("id", customers.get(position).getCustomerId());
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
