package com.pos.leaders.leaderspossystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.SettingsDBAdapter;

public class SetupNewPOSActivity extends AppCompatActivity {

    EditText etPC,etName,etPOSID,etTax,etINote,etED,etCCUN, etCCPASS;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
            Toast.makeText(this, "please insert company pc", Toast.LENGTH_LONG).show();
        } else if(etName.getText().equals("")){
            Toast.makeText(this, "please insert company name", Toast.LENGTH_LONG).show();
        } else if(etPOSID.getText().equals("")){
            Toast.makeText(this, "please insert PosId", Toast.LENGTH_LONG).show();
        } else if ((etTax.getText().equals("")) || (Float.parseFloat(etTax.getText().toString())) < 1) {
            Toast.makeText(this, "please insert Tax", Toast.LENGTH_LONG).show();
        } else if(etINote.getText().equals("")){
            Toast.makeText(this, "please insert Note", Toast.LENGTH_LONG).show();
        } else if(etED.getText().equals("")){
            Toast.makeText(this, "please insert company pc", Toast.LENGTH_LONG).show();
        } else{
            SettingsDBAdapter settingsDBAdapter=new SettingsDBAdapter(this);
            settingsDBAdapter.open();
         /**   int i = settingsDBAdapter.updateEntry(etPC.getText().toString(),etName.getText().toString(),etPOSID.getText().toString(),
                    Float.parseFloat(etTax.getText().toString()),etINote.getText().toString(),Integer.parseInt(etED.getText().toString()),
                    etCCUN.getText().toString(),etCCPASS.getText().toString());
            settingsDBAdapter.close();
            if(i==1){
                Util.isFirstLaunch(this, true);
                finish();
            }
            else{
                Toast.makeText(this, getString(R.string.try_again), Toast.LENGTH_SHORT);
            }**/
        }
    }
}
