package com.pos.leaders.leaderspossystem;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class TestPHPandAndroid extends AppCompatActivity {

    private Button btn;
    private EditText name,address,age;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_phpand_android);

        btn = (Button)findViewById(R.id.button10);
        name = (EditText)findViewById(R.id.editText4);
        address = (EditText)findViewById(R.id.editText5);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            senddate();
            }
        });
    }

    public void senddate(){

        final String send_name = name.getText().toString();
        final String send_addres = address.getText().toString();

        class Async extends AsyncTask<Void,Void,String>{

            ProgressDialog progressDialog;

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "up date", Toast.LENGTH_LONG).show();
              //  Toast.makeText(TestPHPandAndroid.this,s,Toast.LENGTH_LONG).show();


            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(TestPHPandAndroid.this,"","please waiiting",false,false);

            }

            @Override
            protected String doInBackground(Void... voids) {

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.name,send_name);
                hashMap.put(Config.address,send_addres);


                RequestHander requestHander = new RequestHander();
                String post = requestHander.sendPostRequest(Config.insert_url,hashMap);
                Log.e("in background method","ttttttttttttttt");

                return post;
            }
        }
       Async async = new Async();
        async.execute();
    }


}
