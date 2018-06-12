package com.pos.leaders.leaderspossystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.ScheduleWorkersDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.Models.ScheduleWorkers;
import com.pos.leaders.leaderspossystem.Models.Employee;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ScheduleWorkersActivity extends AppCompatActivity {
    Button log_in  ,log_out ,cancel;
    EditText userPassWord;
    String passWord = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_schedule_workers);
        log_in = (Button) findViewById(R.id.btn_log_in);
        log_out = (Button) findViewById(R.id.btn_log_out);
        cancel = (Button) findViewById(R.id.btn_cancel);
        userPassWord = (EditText) findViewById(R.id.ET_userPassword);
        final EmployeeDBAdapter userDBAdapter = new EmployeeDBAdapter(this);
        userDBAdapter.open();
        final ScheduleWorkersDBAdapter scheduleWorkersDBAdapter = new ScheduleWorkersDBAdapter(this);
        scheduleWorkersDBAdapter.open();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleWorkersActivity.this,LogInActivity.class);
                startActivity(intent);
            }
        });
        userPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!userPassWord.getText().toString().equals("")) {
                    passWord = userPassWord.getText().toString();

                } else {
                    passWord = "";
                }
            }
        });
        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValidPassword =userDBAdapter.isValidPassword(passWord);
                if(isValidPassword){
                    // if password valid get userByPassword
                    Employee user = userDBAdapter.getEmployeesByPassword(passWord);
                        //login case
                        long scheduleID = scheduleWorkersDBAdapter.insertEntry(user.getEmployeeId());
                        if(scheduleID>0){
                            Toast.makeText(ScheduleWorkersActivity.this,getString(R.string.welcome)+user.getFullName()+getString(R.string.we_wish_to_you_a_happy_business_day),Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }
                }
                else {
                    //fail password
                    Toast.makeText(ScheduleWorkersActivity.this,getString(R.string.please_remember_your_password),Toast.LENGTH_LONG).show();

                }
            }});
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Employee user = userDBAdapter.getEmployeesByPassword(passWord);
                ScheduleWorkers scheduleWorkers = scheduleWorkersDBAdapter.getLastScheduleWorkersByUserID(user.getEmployeeId());
                scheduleWorkersDBAdapter.updateEntry(user.getEmployeeId(),new Date());
                long r=0,h=0,m=0,s=0;
                r= DateConverter.getDateDiff(new Date(scheduleWorkers.getStartTime()),new Date(), TimeUnit.MILLISECONDS);
                h=r/(1000*60*60);
                m=((r-(h*1000*60*60))/(1000*60));
                s=(r-(m*1000*60)-(h*1000*60*60))/(1000);
                Toast.makeText(ScheduleWorkersActivity.this,getString(R.string.thanks)+user.getFullName() +getString(R.string.the_number_of_hours_you_work_is)+String.format("%02d:%02d:%02d",h,m,s),Toast.LENGTH_LONG).show();
                onBackPressed();

            }});

    }

}
