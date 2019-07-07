package com.pos.leaders.leaderspossystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.pos.leaders.leaderspossystem.Tools.TitleBar;

public class InventoryManagementActivity extends AppCompatActivity {
    Button provider , inventoryIn , inventoryOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_inventory_managment);
        TitleBar.setTitleBar(this);
        provider = (Button)findViewById(R.id.inventoryManagementActivityProviderBtn);
        inventoryIn=(Button)findViewById(R.id.inventoryManagementActivityBtnInDoc);
        inventoryOut=(Button)findViewById(R.id.inventoryManagementActivityBtnOutDoc);
        final String[] itemsWithProvider = {
                getString(R.string.view_provider),
                getString(R.string.add_new_provider),
        };
        provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InventoryManagementActivity.this);
                builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                builder.setItems(itemsWithProvider, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent;
                        switch (item) {
                            case 0:
                                intent = new Intent(InventoryManagementActivity.this, ProviderManagementActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(InventoryManagementActivity.this, AddNewProvider.class);
                                startActivity(intent);
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
