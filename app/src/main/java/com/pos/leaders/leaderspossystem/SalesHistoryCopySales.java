package com.pos.leaders.leaderspossystem;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
        invoiceImageView = (ImageView) findViewById(R.id.salesHistory_ivCopyInvoice);

        Bitmap invoiceBitMab = SETTINGS.copyInvoiceBitMap;
        setImageBitmap(invoiceBitMab,invoiceImageView);
      //  invoiceImageView.setImageBitmap(invoiceBitMab);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap invoiceCancle=SETTINGS.copyInvoiceBitMap;
                PrintTools printTools = new PrintTools(SalesHistoryCopySales.this);
                printTools.PrintReport(invoiceCancle);
                onBackPressed();
            }
        });
    }
    public void setImageBitmap(Bitmap bm,ImageView imageView) {
        // if this is used frequently, may handle bitmaps explicitly
        // to reduce the intermediate drawable object
        imageView.setImageDrawable(new BitmapDrawable(getApplicationContext().getResources(), bm));
    }
}