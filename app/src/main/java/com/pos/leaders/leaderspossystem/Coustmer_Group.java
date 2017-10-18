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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.GroupAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Coustmer_Group extends AppCompatActivity {
    EditText etGroupName ,etType , etParcent , etAmount ,etPoint ,etDescription;
    Button btAddGroup ,btCancel  ;
    GroupAdapter groupAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_coustmer__group);

        TitleBar.setTitleBar(this);

        etGroupName = (EditText) findViewById(R.id.group_name);
        etType = (EditText) findViewById(R.id.et_type);
        etParcent = (EditText) findViewById(R.id.et_parcent);
        etAmount = (EditText) findViewById(R.id.et_amount);
        etPoint = (EditText) findViewById(R.id.et_point);
        etDescription = (EditText) findViewById(R.id.ET_description);
        btCancel=(Button)findViewById(R.id.addGroup_BTCancel);

        btAddGroup = (Button) findViewById(R.id.add_group);
        btAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGroup(v);
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 22/10/2016 cancel and return to previous activity
                Intent intent = new Intent(Coustmer_Group.this, DashBoard.class);
                //	intent.putExtra("permissions_name",user.getPermtionName());

                //userDBAdapter.close();
                startActivity(intent);
            }
        });


    }


    private void addGroup(View v) {
        if(etGroupName.getText().equals("")){
            Toast.makeText(this, "please insert Group name", Toast.LENGTH_LONG).show();

        }
      else  if(etType.getText().equals("")){
            Toast.makeText(this, "please insert type", Toast.LENGTH_LONG).show();

        }
        if(etParcent.getText().equals("")){
            Toast.makeText(this, "please insert Parcent", Toast.LENGTH_LONG).show();

        }
        if(etAmount.getText().equals("")){
            Toast.makeText(this, "please insertAmount", Toast.LENGTH_LONG).show();

        }
        if(etPoint.getText().equals("")){
            Toast.makeText(this, "please insert Point", Toast.LENGTH_LONG).show();

        }
        if(etDescription.getText().equals("")){
            Toast.makeText(this, "please insert description", Toast.LENGTH_LONG).show();

        }

        else{
GroupAdapter groupadapter = new GroupAdapter(this);
            groupadapter.open();
            int i = groupadapter.insertEntry(etGroupName.getText().toString(),etType.getText().toString(),etParcent.getText().toString(),etAmount.getText().toString(),etPoint.getText().toString(),etDescription.getText().toString());
            groupadapter.close();
            if(i==1){
                Toast.makeText(this, "Succssfull add Group", Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
