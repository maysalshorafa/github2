package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.DepartmentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferRuleDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductOfferDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Department;
import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Models.OfferRule;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.DepartmentGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.ProductCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by KARAM on 24/10/2016.
 */
/**
public class OfferActivity extends Activity {

	private static final int DIALOG_ID = 99;
	private static final int DIALOG_ID2 = 100;
	EditText etStartDate, etEndDate, etX, etY, etP, etY2,etName;
	Spinner spRule;
	int mYear, mMonth, mDay;
	Button btAdd,btCancel,btSelectProduct;
	OfferDBAdapter offerDBAdapter;
    ProductOfferDBAdapter productOfferDBAdapter;
    String str[];
    int sprSelectedItemId=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		//region Initialize
		super.onCreate(savedInstanceState);

		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_offer);

		etStartDate = (EditText) findViewById(R.id.offer_ETStartAt);
		etEndDate = (EditText) findViewById(R.id.offer_ETEndAt);
		etX=(EditText)findViewById(R.id.offer_ETX);
		etY=(EditText)findViewById(R.id.offer_ETY);
		etP=(EditText)findViewById(R.id.offer_ETP);
		etY2=(EditText)findViewById(R.id.offer_ETY2);
		etName=(EditText)findViewById(R.id.offer_ETName);
		spRule = (Spinner) findViewById(R.id.offer_SPINNERRule);
		btAdd=(Button)findViewById(R.id.offer_BTAdd);
		btCancel=(Button)findViewById(R.id.offer_BTCancel);
        btSelectProduct=(Button)findViewById(R.id.offer_BTSelectProducts);


        offerDBAdapter=new OfferDBAdapter(this);
        offerDBAdapter.open();
        productOfferDBAdapter=new ProductOfferDBAdapter(this);
        productOfferDBAdapter.open();

		//endregion

		//region Spinner
        str = getResources().getStringArray(R.array.offers_list);

		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, str);

		// Drop down layout style - list view with radio button
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		spRule.setAdapter(dataAdapter);

        spRule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sprSelectedItemId=position;
                etX.setEnabled(false);
                etY.setEnabled(false);
                etP.setEnabled(false);
                etY2.setEnabled(false);
                switch (position){
                    case 0://x on price y
                        etX.setEnabled(true);
                        etY.setEnabled(true);
                        break;
                    case 1://buy x and get the least price with p
                        etX.setEnabled(true);
                        etP.setEnabled(true);
                        break;
                    case 2://p on every invoice
                        etP.setEnabled(true);
                        break;
                    case 3://buy x and get the least price with y
                        etX.setEnabled(true);
                        etY.setEnabled(true);
                        break;
                    case 4://gift on x on price y
                        etX.setEnabled(true);
                        etY.setEnabled(true);
                        break;
                    case 5://gift on total price y
                        etY.setEnabled(true);
                        break;
                    case 6://product special price y
                        etY.setEnabled(true);
                        break;
                    case 7://p to product
                        etP.setEnabled(true);
                        break;
                    case 8://multilevel discount
                        break;
                    case 9://multilevel price
                        break;
                    case 10://Buy Y Pay y2
                        etY.setEnabled(true);
                        etY2.setEnabled(true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



		//endregion

		//region Date
		Date d = new Date();
		Log.i("current date", d.toString());
		//Mon Oct 24 08:45:01 GMT 2016
		//("yyyy-mm-dd hh:mm:ss")
		mYear = Integer.parseInt(d.toString().split(" ")[5]);
		mMonth = d.getMonth();
		mDay = Integer.parseInt(d.toString().split(" ")[2]);

		etStartDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_ID);
			}
		});

		etEndDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_ID2);
			}
		});
		//endregion

		//region Buttons

        //region Add Button
		btAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(etName.getText().toString().equals("")){
                    Toast.makeText(OfferActivity.this, "Please insert name of this offer", Toast.LENGTH_SHORT).show();
                }
                else if(DateConverter.dateBetweenTwoDates(DateConverter.stringToDate(etStartDate.getText().toString()),DateConverter.stringToDate(etEndDate.getText().toString()),new Date())){
                    Toast.makeText(OfferActivity.this, getResources().getText(R.string.not_valid_date), Toast.LENGTH_SHORT).show();
                }
                else if(SESSION._TEMPOFFERPRODUCTS==null||SESSION._TEMPOFFERPRODUCTS.size()==0){
                    Toast.makeText(OfferActivity.this, getResources().getText(R.string.not_selected_products), Toast.LENGTH_SHORT).show();
                }
                else {

                    final ProgressDialog dialog0=new ProgressDialog(OfferActivity.this);
                    dialog0.setTitle(getBaseContext().getString(R.string.wait_for_finish));
                    new AsyncTask<Void,Void,Void>(){

                        @Override
                        protected void onPreExecute() {
                            dialog0.show();
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            dialog0.cancel();
                        }

                        @Override
                        protected Void doInBackground(Void... params) {
                            Offer o = addOffer(sprSelectedItemId,SESSION._TEMPOFFERPRODUCTS);
                            int offerId = offerDBAdapter.insertEntry(o);
                            productOfferDBAdapter.insertEntry(o.getProducts(), offerId);
                            return null;
                        }
                    }.execute();

                }
                finish();

			}
		});
        //endregion

		btCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

        btSelectProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SESSION._TEMPOFFERPRODUCTS==null)
                    SESSION._TEMPOFFERPRODUCTS=new ArrayList<Product>();
                Intent intent=new Intent(OfferActivity.this,OfferProductCartActivity.class);
                startActivity(intent);
            }
        });

		//endregion

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == DIALOG_ID) {
			return new DatePickerDialog(this, onDateSetListener, mYear, mMonth, mDay);
		} else if (id == DIALOG_ID2) {
			return new DatePickerDialog(this, onDateSetListenerEnd, mYear, mMonth, mDay);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener onDateSetListenerEnd = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
			mYear = year;
			mMonth = month;
			mDay = dayOfMonth;
			etEndDate.setText(year + "-" + month + "-" + dayOfMonth);
		}
	};

	private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
			mYear = year;
			mMonth = month;
			mDay = dayOfMonth;
			etStartDate.setText(year + "-" + month + "-" + dayOfMonth);
		}
	};

	private Offer addOffer(int item,List<Product> lp){
        Offer offer=new Offer(etName.getText().toString(),DateConverter.stringToDate(etStartDate.getText().toString()),
                DateConverter.stringToDate(etEndDate.getText().toString()),new Date(),true, SESSION._EMPLOYEE.getId(),item);
        double x=0,y=0,y2=0,p=0;

        x=Double.parseDouble((!etX.getText().toString().equals("")) ? etX.getText().toString() :"0");

        y=Double.parseDouble((!etY.getText().toString().equals("")) ? etY.getText().toString() :"0");
        p=Double.parseDouble((!etP.getText().toString().equals("")) ? etP.getText().toString() :"0");
        y2=Double.parseDouble((!etY2.getText().toString().equals("")) ? etY2.getText().toString() :"0");


        offer.setProducts(lp);
        switch (item) {
            case 0://x on price y
                offer.setX(x);
                offer.setY(y);
                break;
            case 1://buy x and get the least price with p
                offer.setP(p);
                offer.setX(x);
                break;
            case 2://p on every invoice
                offer.setP(p);
                break;
            case 3://buy x and get the least price with y
                offer.setX(x);
                offer.setY(y);
                break;
            case 4://gift on x on price y
                offer.setX(x);
                offer.setY(y);
                break;
            case 5://gift on total price y
                offer.setY(y);
                break;
            case 6://product special price y
                offer.setY(y);
                break;
            case 7://p to product
                offer.setP(p);
                break;
            case 8://multilevel discount
                break;
            case 9://multilevel price
                break;
            case 10://Buy Y Pay y2
                offer.setY(y);
                offer.setZ(y2);
                break;
            default:
                break;
        }
        Log.i("Offer for add",offer.toString());
        return offer;
	}
}**/
