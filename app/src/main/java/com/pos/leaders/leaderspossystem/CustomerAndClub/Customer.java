package com.pos.leaders.leaderspossystem.CustomerAndClub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.util.ArrayList;


public class Customer extends AppCompatActivity {

    Button btAddCustomer, btShowCustomer, addGroup,btShowGroup,btCancel;
    ArrayList<Integer> permissions_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_coustmer);

        TitleBar.setTitleBar(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            permissions_name = getIntent().getIntegerArrayListExtra("permissions_name");
        }
        btAddCustomer = (Button) findViewById(R.id.add_new_customer);
        btShowCustomer = (Button) findViewById(R.id.show_customer);
        addGroup = (Button) findViewById(R.id.new_group);
        btShowGroup = (Button) findViewById(R.id.show_group);
        btCancel = (Button) findViewById(R.id.customerManagement_BTCancel);
        btAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 22/10/2016 cancel and return to previous activity
                Intent intent = new Intent(Customer.this, AddNewCoustmer.class);
                //userDBAdapter.close();
                startActivity(intent);
            }
        });
        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 22/10/2016 cancel and return to previous activity
                Intent intent = new Intent(Customer.this,Coustmer_Group.class);
                //userDBAdapter.close();

                startActivity(intent);
            }
        });
       btShowCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 22/10/2016 cancel and return to previous activity
                Intent intent = new Intent(Customer.this,CustmerManagementActivity.class);
                startActivity(intent);
            }
        });
        btShowGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 22/10/2016 cancel and return to previous activity
                Intent intent = new Intent(Customer.this,ClubMangmentActivity.class);
                //userDBAdapter.close();

                startActivity(intent);
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }

}
