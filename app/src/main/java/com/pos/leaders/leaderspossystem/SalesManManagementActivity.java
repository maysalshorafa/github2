package com.pos.leaders.leaderspossystem;

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

import com.pos.leaders.leaderspossystem.DataBaseAdapter.UserDBAdapter;
import com.pos.leaders.leaderspossystem.Models.User;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.WorkerGridViewAdapter;

import java.util.List;

public class SalesManManagementActivity extends AppCompatActivity {

    List<User> users;
    UserDBAdapter userDBAdapter;
    GridView gvSalesMan;
    Button btAddSalesMan,btCancel;
    private static final int CHANGE_PASSWORD_DIALOG = 656;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sales_man_management);

        TitleBar.setTitleBar(this);

        gvSalesMan = (GridView) findViewById(R.id.reportManagement_GVSalesMan);
        btAddSalesMan = (Button) findViewById(R.id.salesManManagement_BTNewSalesMan);
        btCancel = (Button) findViewById(R.id.salesManManagement_BTCancel);

        //region Buttons

        btAddSalesMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalesManManagementActivity.this, AddUserActivity.class);
                startActivity(intent);
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalesManManagementActivity.this, ReportsManagementActivity.class);
                startActivity(intent);
            }
        });


        //endregion


        userDBAdapter= new UserDBAdapter(this);
        userDBAdapter.open();
        users = userDBAdapter.getAllSalesMAn();
        user = null;

        final WorkerGridViewAdapter adapter = new WorkerGridViewAdapter(this, users);
        gvSalesMan.setAdapter(adapter);
        gvSalesMan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final String[] items = {
                        getString(R.string.view),
                        getString(R.string.view_sales),


                };

                AlertDialog.Builder builder = new AlertDialog.Builder(SalesManManagementActivity.this);
                builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent;
                        switch (item) {

                            case 0:
                                intent = new Intent(SalesManManagementActivity.this, AddUserActivity.class);
                                intent.putExtra("userId", users.get(position).getId());
                                startActivity(intent);
                            case 1:
                                intent = new Intent(SalesManManagementActivity.this, SalesAssistantDetailesSalesMangmentActivity.class);
                                intent.putExtra("userId", users.get(position).getId());
                                startActivity(intent);
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

}
