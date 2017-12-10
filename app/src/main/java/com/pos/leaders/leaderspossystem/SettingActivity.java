package com.pos.leaders.leaderspossystem;

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

import com.pos.leaders.leaderspossystem.DataBaseAdapter.SettingsDBAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SettingActivity extends AppCompatActivity {
    EditText etCompanyName, etPrivateCompany, etTax, etTerminalNumber, etTerminalPassword,etInvoiceNote;
    Button btSave, btCancel;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);

        TitleBar.setTitleBar(this);

        etCompanyName = (EditText) findViewById(R.id.settings_etCompanyName);
        etPrivateCompany = (EditText) findViewById(R.id.settings_etPC);
        etPrivateCompany.setEnabled(false);
        etTax = (EditText) findViewById(R.id.settings_etTax);
        etInvoiceNote = (EditText) findViewById(R.id.settings_etInvoiceNote);
        etTerminalNumber = (EditText) findViewById(R.id.settings_etTNum);
        etTerminalNumber.setEnabled(false);
        etTerminalPassword = (EditText) findViewById(R.id.settings_etTPass);
        etTerminalPassword.setEnabled(false);

        btSave = (Button) findViewById(R.id.settings_btSave);
        btCancel = (Button) findViewById(R.id.settings_btCancel);

        getSettings();

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsDBAdapter settingsDBAdapter = new SettingsDBAdapter(SettingActivity.this);
                settingsDBAdapter.open();

                String CompanyName = etCompanyName.getText().toString();
                String PrivateCompany = etPrivateCompany.getText().toString();

                float Tax = Float.parseFloat(etTax.getText().toString());

                String TerminalNumber = etTerminalNumber.getText().toString();
                String TerminalPassword = etTerminalPassword.getText().toString();
                String InvoiceNote = etInvoiceNote.getText().toString();
                settingsDBAdapter.updateEntry(PrivateCompany, CompanyName, "", Tax,InvoiceNote , 0, TerminalNumber, TerminalPassword);
                settingsDBAdapter.GetSettings();

                settingsDBAdapter.close();
                getSettings();
                Toast.makeText(SettingActivity.this, getBaseContext().getString(R.string.success_to_save_settings), Toast.LENGTH_SHORT).show();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getSettings() {
        etCompanyName.setText(SETTINGS.companyName);
        etPrivateCompany.setText(SETTINGS.companyID);
        etTax.setText(SETTINGS.tax + "");
        etTerminalNumber.setText(SETTINGS.ccNumber);
        etTerminalPassword.setText(SETTINGS.ccPassword);
        etInvoiceNote.setText(SETTINGS.returnNote);
    }
}
