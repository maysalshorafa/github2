package com.pos.leaders.leaderspossystem;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.pos.leaders.leaderspossystem.Printer.PrintTools;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;

public class SalesHistoryCopySales extends AppCompatActivity {
    Button btCancel, btPrint;
    ImageView invoiceImageView;
    String status;
    Bitmap invoiceBitMab = null,invoiceCancle=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sales_history_copy_sales);
        Bundle extras = getIntent().getExtras();
        btCancel = (Button) findViewById(R.id.salesHistory_btCancel);
        btPrint = (Button) findViewById(R.id.salesHistory_btPrint);
        // btPrint.setVisibility(View.INVISIBLE);
        invoiceImageView = (ImageView) findViewById(R.id.salesHistory_ivCopyInvoice);
        invoiceBitMab = SETTINGS.copyInvoiceBitMap;
        invoiceImageView.setImageBitmap(invoiceBitMab);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invoiceCancle=invoiceBitMab;
                PrintTools printTools = new PrintTools(SalesHistoryCopySales.this);
                printTools.PrintReport(SETTINGS.copyInvoiceBitMap);
                onBackPressed();
            }
        });
    }
}