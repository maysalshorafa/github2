package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferRuleDBAdapter;
import com.pos.leaders.leaderspossystem.Models.OfferRule;

/**
 * Created by KARAM on 24/10/2016.
 */
/**
public class OfferRuleActivity extends Activity {

	Button btAdd,btCancel;
	EditText etName;
	OfferRuleDBAdapter offerRuleDBAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_offer_rule);

		// Get References of Views
		btAdd = (Button) findViewById(R.id.offerRule_BTAdd);
		btCancel = (Button) findViewById(R.id.offerRule_BTCancel);
		etName = (EditText) findViewById(R.id.offerRule_ETName);

		offerRuleDBAdapter=new OfferRuleDBAdapter(this);
		offerRuleDBAdapter.open();

		btAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(etName.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(),"Please insert name",Toast.LENGTH_LONG).show();
				}
				else{
					offerRuleDBAdapter.insertEntry(etName.getText().toString());
					Toast.makeText(getApplicationContext(),"Added Rule Success ",Toast.LENGTH_LONG).show();
				}
				Toast.makeText(getApplicationContext(), "added offer rule", Toast.LENGTH_SHORT).show();
			}
		});

		btCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(OfferRuleActivity.this,MainScreenActivity.class);
				startActivity(intent);
			}
		});
	}
}
**/