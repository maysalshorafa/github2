package com.pos.leaders.leaderspossystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PrinterStub extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_stub);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            extras.getByteArray("");


        }
    }
}
