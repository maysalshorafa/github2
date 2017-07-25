package com.pos.leaders.leaderspossystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.GroupAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.SettingsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UserDBAdapter;
import com.pos.leaders.leaderspossystem.Tools.Util;

public class Coustmer_Group extends AppCompatActivity {
    EditText etGroupName ,etType , etParcent , etAmount ,etPoint ,etDescription;
    Button btAddGroup ;
    GroupAdapter groupAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coustmer__group);
        etGroupName = (EditText) findViewById(R.id.group_name);
        etType = (EditText) findViewById(R.id.et_type);
        etParcent = (EditText) findViewById(R.id.et_parcent);
        etAmount = (EditText) findViewById(R.id.et_amount);
        etPoint = (EditText) findViewById(R.id.et_point);
        etDescription = (EditText) findViewById(R.id.ET_description);

        btAddGroup = (Button) findViewById(R.id.add_group);
        btAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGroup(v);
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
