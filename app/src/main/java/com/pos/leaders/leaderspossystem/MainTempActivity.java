package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class MainTempActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main_temp);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title_bar);
    }
}
