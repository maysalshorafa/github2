package com.pos.leaders.leaderspossystem.Offers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

public class CreateOfferActivity extends AppCompatActivity {

    EditText etStart, etEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_offer);

        TitleBar.setTitleBar(this);

        etStart = (EditText) findViewById(R.id.CreateOffer_etStart);
        etEnd = (EditText) findViewById(R.id.CreateOffer_etEnd);


    }
}
