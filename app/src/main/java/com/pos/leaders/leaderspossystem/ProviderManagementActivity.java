package com.pos.leaders.leaderspossystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProviderDbAdapter;
import com.pos.leaders.leaderspossystem.Models.Provider;
import com.pos.leaders.leaderspossystem.Tools.ProviderCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.util.List;

public class ProviderManagementActivity extends AppCompatActivity {

    List<Provider> providers;
    ProviderDbAdapter providerDbAdapter;
    GridView gvProvider;
    Button btAddProvider, btCancel;
    public static int Provider_Management_View=0 ;
    public  static int  Provider_Management_Edit=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_provider_management);

        TitleBar.setTitleBar(this);

        gvProvider = (GridView) findViewById(R.id.providerManagementGVProvider);
        btAddProvider = (Button) findViewById(R.id.providerManagement_BTNewProvider);
        btCancel = (Button) findViewById(R.id.providerManagement_BTCancel);
        btAddProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProviderManagementActivity.this, AddNewProvider.class);
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
        providerDbAdapter = new ProviderDbAdapter(this);
        providerDbAdapter.open();
        providers = providerDbAdapter.getAllCustomer();
        final ProviderCatalogGridViewAdapter adapter = new ProviderCatalogGridViewAdapter(this, providers);
        gvProvider.setAdapter(adapter);
        gvProvider.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Provider_Management_Edit=0;
                Provider_Management_View=0;

                final String[] items = {
                        getString(R.string.view),
                        getString(R.string.edit),
                        getString(R.string.delete)
                };
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProviderManagementActivity.this);
                    builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            Intent intent;
                            switch (item) {
                                case 0:
                                    Provider_Management_View = 9;
                                    intent = new Intent(ProviderManagementActivity.this, AddNewProvider.class);
                                    intent.putExtra("id", providers.get(position).getProviderId());
                                    startActivity(intent);
                                case 1:
                                    Provider_Management_Edit = 10;
                                    intent = new Intent(ProviderManagementActivity.this, AddNewProvider.class);
                                    intent.putExtra("id", providers.get(position).getProviderId());
                                    startActivity(intent);
                                    break;

                                case 2:
                                    new AlertDialog.Builder(ProviderManagementActivity.this)
                                            .setTitle(getString(R.string.delete) + " " + getString(R.string.customer))
                                            .setMessage(getString(R.string.delete_customer_message))
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    providerDbAdapter.deleteEntry(providers.get(position).getProviderId());
                                                    providers.remove(providers.get(position));
                                                    gvProvider.setAdapter(adapter);
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
