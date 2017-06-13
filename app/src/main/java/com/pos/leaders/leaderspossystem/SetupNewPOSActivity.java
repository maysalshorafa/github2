package com.pos.leaders.leaderspossystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.SettingsDBAdapter;
import com.pos.leaders.leaderspossystem.Tools.Util;

public class SetupNewPOSActivity extends AppCompatActivity {

    EditText etPC,etName,etPOSID,etTax,etINote,etED,etCCUN, etCCPASS;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_new_pos);

        etPC = (EditText) findViewById(R.id.setupNewPOS_etPC);
        etName = (EditText) findViewById(R.id.setupNewPOS_etName);
        etPOSID = (EditText) findViewById(R.id.setupNewPOS_etPOSID);
        etTax = (EditText) findViewById(R.id.setupNewPOS_etTax);
        etINote = (EditText) findViewById(R.id.setupNewPOS_etInvoiceNote);
        etED = (EditText) findViewById(R.id.setupNewPOS_etInvoiceED);

        etCCUN = (EditText) findViewById(R.id.setupNewPOS_etCCUN);
        etCCPASS = (EditText) findViewById(R.id.setupNewPOS_etCCPAS);

        btnSave = (Button) findViewById(R.id.setupNewPOS_btSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(v);
            }
        });

    }

    public void save(View view){
        if(etPC.getText().equals("")){
            Toast.makeText(this, "", Toast.LENGTH_SHORT);
        } else if(etName.getText().equals("")){
            Toast.makeText(this, "", Toast.LENGTH_SHORT);
        } else if(etPOSID.getText().equals("")){
            Toast.makeText(this, "", Toast.LENGTH_SHORT);
        } else if ((etTax.getText().equals("")) || (Float.parseFloat(etTax.getText().toString())) < 1) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT);
        } else if(etINote.getText().equals("")){
            Toast.makeText(this, "", Toast.LENGTH_SHORT);
        } else if(etED.getText().equals("")){
            Toast.makeText(this, "", Toast.LENGTH_SHORT);
        } else{
            SettingsDBAdapter settingsDBAdapter=new SettingsDBAdapter(this);
            settingsDBAdapter.open();
            int i = settingsDBAdapter.updateEntry(etName.getText().toString(),etName.getText().toString(),etPOSID.getText().toString(),
                    Float.parseFloat(etTax.getText().toString()),etINote.getText().toString(),Integer.parseInt(etED.getText().toString()),
                    etCCUN.getText().toString(),etCCPASS.getText().toString());
            settingsDBAdapter.close();
            if(i==1){
                Util.isFirstLaunch(this, true);
                finish();
            }
            else{
                Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
