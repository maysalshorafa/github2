package com.pos.leaders.leaderspossystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Coustmer extends AppCompatActivity {
    Button btAdd_coustmer, btshow_coustmer , addgroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coustmer);
        btAdd_coustmer = (Button) findViewById(R.id.add_new_coustmer);
        btshow_coustmer = (Button) findViewById(R.id.show_coustmer);
        addgroup = (Button) findViewById(R.id.new_group);
        btAdd_coustmer.setOnClickListener(new View.OnClickListener() {
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
       btshow_coustmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 22/10/2016 cancel and return to previous activity
                Intent intent = new Intent(Coustmer.this,CustmerMangmentActivity.class);
                //userDBAdapter.close();

                startActivity(intent);
            }
        });



    }
}
