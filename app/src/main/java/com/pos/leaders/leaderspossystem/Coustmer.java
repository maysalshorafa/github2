package com.pos.leaders.leaderspossystem;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.Tools.SESSION;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Coustmer extends AppCompatActivity {
    Button btAddCoustmer, btshowCoustmer, addgroup ,btShowGroup;
    android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_coustmer);


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
        btAddCoustmer = (Button) findViewById(R.id.add_new_coustmer);
        btshowCoustmer = (Button) findViewById(R.id.show_coustmer);
        addgroup = (Button) findViewById(R.id.new_group);
        btShowGroup = (Button) findViewById(R.id.show_group);
        btAddCoustmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 22/10/2016 cancel and return to previous activity
                Intent intent = new Intent(Coustmer.this, AddNewCoustmer.class);
                //userDBAdapter.close();
                startActivity(intent);
            }
        });
        addgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 22/10/2016 cancel and return to previous activity
                Intent intent = new Intent(Coustmer.this,Coustmer_Group.class);
                //userDBAdapter.close();

                startActivity(intent);
            }
        });
       btshowCoustmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 22/10/2016 cancel and return to previous activity
                Intent intent = new Intent(Coustmer.this,CustmerMangmentActivity.class);
                //userDBAdapter.close();

                startActivity(intent);
            }
        });
        btShowGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 22/10/2016 cancel and return to previous activity
                Intent intent = new Intent(Coustmer.this,ClubMangmentActivity.class);
                //userDBAdapter.close();

                startActivity(intent);
            }
        });



    }
}
