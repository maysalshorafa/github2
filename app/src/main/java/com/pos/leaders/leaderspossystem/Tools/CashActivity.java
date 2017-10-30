package com.pos.leaders.leaderspossystem.Tools;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.SaleDBAdapter;
import com.pos.leaders.leaderspossystem.MainActivity;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.R;

/**
 * Created by Karam on 26/11/2016.
 */

//AppCompatActivity
public class CashActivity extends AppCompatActivity implements View.OnTouchListener {
    public static final String LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY = "LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY";
    ImageView iv_20, iv_50, iv_100, iv_200;
    TextView tvB20, tvB50, tvB100, tvB200;
    TextView custmer_name;

    ImageView cent10, cent50, cent100, cent200, cent500, cent1000;
    TextView tvC10, tvC50, tvC100, tvC200, tvC500, tvC1000;
    TextView tv, tvTotalInserted, tvExcess;
    String custmer_nameS;

    Button btnDone;

    double totalPrice = 0.0;
    float totalPid = 0.0f;

    private float x1, x2;
    private float y1, y2;
    static final int MIN_DISTANCE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_cash);

        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //wlp.gravity = Gravity.CENTER_VERTICAL;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = (float) 0.6;
        window.setAttributes(wlp);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.9), (int) (height * 0.95));
        custmer_name=(TextView)findViewById(R.id.custmer_name);

        iv_20 = (ImageView) findViewById(R.id.cashActivity_Bill20);
        iv_20.setOnTouchListener(this);
        tvB20 = (TextView) findViewById(R.id.cashActivity_TVDB20);
        iv_50 = (ImageView) findViewById(R.id.cashActivity_Bill50);
        iv_50.setOnTouchListener(this);
        tvB50 = (TextView) findViewById(R.id.cashActivity_TVDB50);
        iv_100 = (ImageView) findViewById(R.id.cashActivity_Bill100);
        iv_100.setOnTouchListener(this);
        tvB100 = (TextView) findViewById(R.id.cashActivity_TVDB100);
        iv_200 = (ImageView) findViewById(R.id.cashActivity_Bill200);
        iv_200.setOnTouchListener(this);
        tvB200 = (TextView) findViewById(R.id.cashActivity_TVDB200);

        cent10 = (ImageView) findViewById(R.id.cashActivity_Cent10);
        cent10.setOnTouchListener(this);
        tvC10 = (TextView) findViewById(R.id.cashActivity_TVDC10);
        cent50 = (ImageView) findViewById(R.id.cashActivity_Cent50);
        cent50.setOnTouchListener(this);
        tvC50 = (TextView) findViewById(R.id.cashActivity_TVDC50);
        cent100 = (ImageView) findViewById(R.id.cashActivity_Cent100);
        cent100.setOnTouchListener(this);
        tvC100 = (TextView) findViewById(R.id.cashActivity_TVDC100);
        cent200 = (ImageView) findViewById(R.id.cashActivity_Cent200);
        cent200.setOnTouchListener(this);
        tvC200 = (TextView) findViewById(R.id.cashActivity_TVDC200);
        cent500 = (ImageView) findViewById(R.id.cashActivity_Cent500);
        cent500.setOnTouchListener(this);
        tvC500 = (TextView) findViewById(R.id.cashActivity_TVDC500);
        cent1000 = (ImageView) findViewById(R.id.cashActivity_Cent1000);
        cent1000.setOnTouchListener(this);
        tvC1000 = (TextView) findViewById(R.id.cashActivity_TVDC1000);

        btnDone=(Button)findViewById(R.id.cashActivity_BTNDone);
        tv = (TextView) findViewById(R.id.cashActivity_TVRequired);
        tvExcess = (TextView) findViewById(R.id.cashActivity_TVExcess);
        tvExcess.setText(0 + " " + getResources().getText(R.string.ins));
        tvExcess.setTextColor(getResources().getColor(R.color.primaryColor));
        tvTotalInserted = (TextView) findViewById(R.id.cashActivity_TVTotalInserted);
        tvTotalInserted.setText(0.0 + " " + getResources().getText(R.string.ins));


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTotalInserted.setText(tv.getText());
                tvExcess.setText(0 + " " + getResources().getText(R.string.ins));
                tvExcess.setTextColor(getResources().getColor(R.color.primaryColor));
                btnDone.setEnabled(true);
                btnDone.setBackground(getResources().getDrawable(R.drawable.bt_green_enabled));
                btnDone.setPadding(50,10,50,10);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            totalPrice = (double) extras.get("_Price");
            Toast.makeText(CashActivity.this,"total price"+totalPrice,Toast.LENGTH_LONG).show();
            custmer_nameS= (String) extras.get("_custmer");
            tv.setText(totalPrice + " " + getResources().getText(R.string.ins));


            custmer_name.setText(custmer_nameS);
            custmer_nameS="";
        } else {
            finish();
        }

        calcTotalInserted();


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 01/12/2016 return how match inserted money
                Intent i=new Intent();
                i.putExtra(LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY,totalPid);
                setResult(RESULT_OK,i);
              finish();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                double deltaX = (x2 - x1) * (x2 - x1);
                double deltaY = (y2 - y1) * (y2 - y1);

                incDec(v, !(Math.sqrt(deltaX) > MIN_DISTANCE || Math.sqrt(deltaY) > MIN_DISTANCE));

                break;
        }
        return true;
    }

    private void incDec(View v, boolean inc) {
        switch (v.getId()) {
            case R.id.cashActivity_Bill20:
                incDecOnView(tvB20, inc);
                break;
            case R.id.cashActivity_Bill50:
                incDecOnView(tvB50, inc);
                break;
            case R.id.cashActivity_Bill100:
                incDecOnView(tvB100, inc);
                break;
            case R.id.cashActivity_Bill200:
                incDecOnView(tvB200, inc);
                break;
            case R.id.cashActivity_Cent10:
                incDecOnView(tvC10,inc);
                break;
            case R.id.cashActivity_Cent50:
                incDecOnView(tvC50,inc);
                break;
            case R.id.cashActivity_Cent100:
                incDecOnView(tvC100,inc);
                break;
            case R.id.cashActivity_Cent200:
                incDecOnView(tvC200,inc);
                break;
            case R.id.cashActivity_Cent500:
                incDecOnView(tvC500,inc);
                break;
            case R.id.cashActivity_Cent1000:
                incDecOnView(tvC1000,inc);
                break;
        }
        calcTotalInserted();
    }

    private void incDecOnView(TextView tv, boolean inc) {
        if (inc) {
            int count = Integer.parseInt(tv.getText().toString());
            count++;
            tv.setText(count + "");
        } else {
            int count = Integer.parseInt(tv.getText().toString());
            if (count == 0)
                return;
            count--;
            tv.setText(count + "");
        }
    }

    private void calcTotalInserted() {
        totalPid = 0;
        totalPid += (Integer.parseInt(tvB20.getText().toString()) * 20.0);
        totalPid += (Integer.parseInt(tvB50.getText().toString()) * 50.0);
        totalPid += (Integer.parseInt(tvB100.getText().toString()) * 100.0);
        totalPid += (Integer.parseInt(tvB200.getText().toString()) * 200.0);
        totalPid += (Integer.parseInt(tvC10.getText().toString()) * 0.1);
        totalPid += (Integer.parseInt(tvC50.getText().toString()) * 0.5);
        totalPid += (Integer.parseInt(tvC100.getText().toString()) * 1.0);
        totalPid += (Integer.parseInt(tvC200.getText().toString()) * 2.0);
        totalPid += (Integer.parseInt(tvC500.getText().toString()) * 5.0);
        totalPid += (Integer.parseInt(tvC1000.getText().toString()) * 10.0);
        tvTotalInserted.setText(totalPid + " " + getResources().getText(R.string.ins));

        float deltaPrice = (float)(totalPid -(float)totalPrice );
        if (deltaPrice >= 0) {
            btnDone.setEnabled(true);
            btnDone.setBackground(getResources().getDrawable(R.drawable.bt_green_enabled));
            btnDone.setPadding(50,10,50,10);
            tvExcess.setTextColor(getResources().getColor(R.color.primaryColor));
        } else {
            btnDone.setEnabled(false);
            btnDone.setBackground(getResources().getDrawable(R.drawable.btn_secondary));
            btnDone.setPadding(50,10,50,10);
            tvExcess.setTextColor(getResources().getColor(R.color.dangerColor));
        }
        tvExcess.setText(String.format("%.2f",deltaPrice) + " " + getResources().getText(R.string.ins));
    }
}