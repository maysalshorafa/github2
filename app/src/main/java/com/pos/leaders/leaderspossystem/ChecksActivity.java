package com.pos.leaders.leaderspossystem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.ChecksListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.DocumentControl;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by KARAM on 03/11/2016.
 */

public class ChecksActivity extends AppCompatActivity {

	public static final String LEAD_POS_RESULT_INTENT_CODE_CHECKS_ACTIVITY = "LEAD_POS_RESULT_INTENT_CODE_CHECKS_ACTIVITY";
	Button btAdd;
	Button btDone,btCancel;
    TextView tv , tvCheckCustomer , tvChecksRequired;
	ListView lvChecks;
	static List<Check> checkList = new ArrayList<Check>();
	ChecksListViewAdapter adapter;
	float historicX = Float.NaN, historicY = Float.NaN;
	static final int DELTA = 50;
    private double totalPrice;
	String customer_name;
	LinearLayout LlCustomer;
	enum Direction {LEFT, RIGHT;}
	double requiredAmount=0;
    Bundle extras;
	JSONObject invoiceJson=new JSONObject();
	BoInvoice invoice ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_checks);
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
        checkList = new ArrayList<Check>();

		getWindow().setLayout((int) (width * 0.9), (int) (height * 0.75));

        tv = (TextView) findViewById(R.id.checksActivity_TVPrice);
		tvChecksRequired = (TextView) findViewById(R.id.checksActivity_TVRequired);
		tvCheckCustomer = (TextView) findViewById(R.id.custmer_name);

		LlCustomer = (LinearLayout) findViewById(R.id.checkActivity_llCustomer);

		 extras = getIntent().getExtras();
        if (extras != null) {
            totalPrice = (double) extras.get("_Price");
			customer_name =(String)extras.get("_custmer");
            tv.setText(totalPrice + " " + getResources().getText(R.string.ins));
			tvCheckCustomer.setText(customer_name);
			if(extras.containsKey("checksReceipt")){
				try {
					invoiceJson=new JSONObject(extras.getString("invoice"));
					JSONObject docJson = invoiceJson.getJSONObject("documentsData");
					docJson.remove("@type");
					docJson.put("type","Invoice");
					invoiceJson.remove("documentsData");
					invoiceJson.put("documentsData",docJson);
					invoice=new BoInvoice(DocumentType.INVOICE,docJson,invoiceJson.getString("docNum"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				//sendReceipt();
			}
        } else {
            finish();
        }

        if(customer_name.equals("")|| customer_name ==""|| customer_name.equals(null)){
			LlCustomer.setVisibility(View.GONE);
		}

		btAdd = (Button) findViewById(R.id.checksActivity_BTAdd);
		btDone = (Button) findViewById(R.id.checksActivity_BTDone);
        btCancel = (Button) findViewById(R.id.checksActivity_BTCancel);
		lvChecks = (ListView) findViewById(R.id.checksActivity_LVChecks);
		lvChecks.setFocusable(true);
		Check check = new Check();
		check.setCreatedAt(new Timestamp(System.currentTimeMillis()) );
		Log.d("check",check.toString());
		checkList.add(check);
		getTotal();

        //init checks list holder
        if(SESSION._CHECKS_HOLDER==null||SESSION._CHECKS_HOLDER.size()==0){
            SESSION._CHECKS_HOLDER = new ArrayList<Check>();
        }
        else{
            checkList = SESSION._CHECKS_HOLDER;
        }
		LayoutInflater inflater = getLayoutInflater();
		ViewGroup header = (ViewGroup)inflater.inflate(R.layout.list_adapter_head_row_checks, lvChecks, false);
		lvChecks.addHeaderView(header, null, false);
        adapter = new ChecksListViewAdapter(this, R.layout.list_adapter_row_checks, checkList);
		getTotal();
        lvChecks.setAdapter(adapter);
		setListeners();
		final Calendar myCalendar = Calendar.getInstance();
		DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
								  int dayOfMonth) {
				// TODO Auto-generated method stub
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

			}

		};

	}

    private double getTotalPid(){
        double d=0.0f;
        for(Check c:checkList){
            d += c.getAmount();
        }
        return d;
    }
	private double getTotal(){
		double d=0.0f;
		for(Check c:checkList){
			d += c.getAmount();
			requiredAmount=totalPrice-d;
			if(requiredAmount>0){
				tvChecksRequired.setText(Util.makePrice(requiredAmount)+ " " + getResources().getText(R.string.ins));
			}else {
				tvChecksRequired.setText(0+" " + getResources().getText(R.string.ins));
			}

		}
		return d;
	}

	private void setListeners() {
		btDone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                Check _check = new Check();
				if(lvChecks.getChildCount()>1) {
                    ChecksListViewAdapter.ViewHolder lastItem = (ChecksListViewAdapter.ViewHolder) lvChecks.getChildAt(lvChecks.getChildCount() - 1).getTag();

                    checkList.get(lvChecks.getChildCount() - 2);
					Date utilDate=DateConverter.stringToDate(lastItem.getEtDate());
					Calendar cal = Calendar.getInstance();
					cal.setTime(utilDate);
                    _check = new Check(lastItem.getEtCheckNum(), lastItem.getEtBankNum(), lastItem.getEtBenchNum(), lastItem.getEtAccountNum(),
                            lastItem.getEtAmount(), new java.sql.Timestamp(utilDate.getTime()), false);
                    //lastItem.findViewById(R.id.listChecks_ETAmount);
                    checkList.set(lvChecks.getChildCount()-2,_check);
                }
                if (getTotalPid() == totalPrice) {
                    if (checkList != null && checkList.size() > 0) {

						if(extras.containsKey("checksReceipt")){
							List<Check> finalCheckList = new ArrayList<Check>();

							for (int i = 0; i <= checkList.size()-1; i++) {
								//ChecksListViewAdapter.ViewHolder item = (ChecksListViewAdapter.ViewHolder) lvChecks.getChildAt(i).getTag();
								if (i == 0) {
									_check = new Check(checkList.get(i).getCheckNum(), checkList.get(i).getBankNum(), checkList.get(i).getBranchNum(), checkList.get(i).getAccountNum(),
											checkList.get(i).getAmount(), new Timestamp(System.currentTimeMillis()), false);
								} else {
									//Date utilDate = DateConverter.stringToDate(item.getEtDate());
									_check = new Check(checkList.get(i).getCheckNum(), checkList.get(i).getBankNum(), checkList.get(i).getBranchNum(), checkList.get(i).getAccountNum(),
											checkList.get(i).getAmount(),  checkList.get(i).getCreatedAt(), false);
									/**	_check = new Check(item.getEtCheckNum(), item.getEtBankNum(), item.getEtBenchNum(), item.getEtAccountNum(),
									 item.getEtAmount(), new java.sql.Timestamp(utilDate.getTime()), false);**/
								}
								finalCheckList.add(_check);
							}
							SESSION._CHECKS_HOLDER = finalCheckList;
							DocumentControl.sendDoc(ChecksActivity.this,invoice, CONSTANT.CHECKS,totalPrice);
						}else {

							 List<Check> finalCheckList = new ArrayList<Check>();

							for (int i = 0; i <= checkList.size()-1; i++) {
								//ChecksListViewAdapter.ViewHolder item = (ChecksListViewAdapter.ViewHolder) lvChecks.getChildAt(i).getTag();
								if (i == 0) {
									_check = new Check(checkList.get(i).getCheckNum(), checkList.get(i).getBankNum(), checkList.get(i).getBranchNum(), checkList.get(i).getAccountNum(),
											checkList.get(i).getAmount(), new Timestamp(System.currentTimeMillis()), false);
								} else {
									//Date utilDate = DateConverter.stringToDate(item.getEtDate());
									_check = new Check(checkList.get(i).getCheckNum(), checkList.get(i).getBankNum(), checkList.get(i).getBranchNum(), checkList.get(i).getAccountNum(),
											checkList.get(i).getAmount(),  checkList.get(i).getCreatedAt(), false);
									/**	_check = new Check(item.getEtCheckNum(), item.getEtBankNum(), item.getEtBenchNum(), item.getEtAccountNum(),
									 item.getEtAmount(), new java.sql.Timestamp(utilDate.getTime()), false);**/
								}
								finalCheckList.add(_check);
							}
							SESSION._CHECKS_HOLDER = finalCheckList;
							Intent i = new Intent();
							i.putExtra(LEAD_POS_RESULT_INTENT_CODE_CHECKS_ACTIVITY, getTotalPid());
							i.putExtra( SalesCartActivity.COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE,totalPrice);
							setResult(RESULT_OK, i);
							finish();
						}
                    }
                    else{
                        Toast.makeText(ChecksActivity.this, getString(R.string.not_inserted_checks), Toast.LENGTH_SHORT).show();
                    }

                } else if (getTotalPid() > totalPrice) {
					if(extras.containsKey("checksReceipt")){
						double d = getTotalPid() - checkList.get(checkList.size() - 1).getAmount();
						double needAmount = totalPrice - d;
						ChecksListViewAdapter.ViewHolder lastItem = (ChecksListViewAdapter.ViewHolder) lvChecks.getChildAt(lvChecks.getChildCount() - 1).getTag();
						checkList.get(checkList.size() - 1).setAmount(needAmount);
						lastItem.setEtAmount(needAmount);
						Toast.makeText(ChecksActivity.this, getString(R.string.checks_amount_more_than_amount_need), Toast.LENGTH_SHORT).show();
						List<Check> finalCheckList = new ArrayList<Check>();
						for (int i = 0; i <= checkList.size()-1; i++) {
							//ChecksListViewAdapter.ViewHolder item = (ChecksListViewAdapter.ViewHolder) lvChecks.getChildAt(i).getTag();
							if (i == 0) {
								_check = new Check(checkList.get(i).getCheckNum(), checkList.get(i).getBankNum(), checkList.get(i).getBranchNum(), checkList.get(i).getAccountNum(),
										checkList.get(i).getAmount(), new Timestamp(System.currentTimeMillis()), false);
							} else {
								//Date utilDate = DateConverter.stringToDate(item.getEtDate());
								_check = new Check(checkList.get(i).getCheckNum(), checkList.get(i).getBankNum(), checkList.get(i).getBranchNum(), checkList.get(i).getAccountNum(),
										checkList.get(i).getAmount(),  checkList.get(i).getCreatedAt(), false);
								/**	_check = new Check(item.getEtCheckNum(), item.getEtBankNum(), item.getEtBenchNum(), item.getEtAccountNum(),
								 item.getEtAmount(), new java.sql.Timestamp(utilDate.getTime()), false);**/
							}
							finalCheckList.add(_check);
						}
						SESSION._CHECKS_HOLDER = finalCheckList;
						DocumentControl.sendDoc(ChecksActivity.this,invoice, CONSTANT.CHECKS,totalPrice);
					}else {
						double d = getTotalPid() - checkList.get(checkList.size() - 1).getAmount();
						double needAmount = totalPrice - d;
						ChecksListViewAdapter.ViewHolder lastItem = (ChecksListViewAdapter.ViewHolder) lvChecks.getChildAt(lvChecks.getChildCount() - 1).getTag();
						checkList.get(checkList.size() - 1).setAmount(needAmount);
						lastItem.setEtAmount(needAmount);
						Toast.makeText(ChecksActivity.this, getString(R.string.checks_amount_more_than_amount_need), Toast.LENGTH_SHORT).show();
						List<Check> finalCheckList = new ArrayList<Check>();
						for (int i = 0; i <= checkList.size()-1; i++) {
							//ChecksListViewAdapter.ViewHolder item = (ChecksListViewAdapter.ViewHolder) lvChecks.getChildAt(i).getTag();
							if (i == 0) {
								_check = new Check(checkList.get(i).getCheckNum(), checkList.get(i).getBankNum(), checkList.get(i).getBranchNum(), checkList.get(i).getAccountNum(),
										checkList.get(i).getAmount(), new Timestamp(System.currentTimeMillis()), false);
							} else {
								//Date utilDate = DateConverter.stringToDate(item.getEtDate());
								_check = new Check(checkList.get(i).getCheckNum(), checkList.get(i).getBankNum(), checkList.get(i).getBranchNum(), checkList.get(i).getAccountNum(),
										checkList.get(i).getAmount(),  checkList.get(i).getCreatedAt(), false);
							/**	_check = new Check(item.getEtCheckNum(), item.getEtBankNum(), item.getEtBenchNum(), item.getEtAccountNum(),
										item.getEtAmount(), new java.sql.Timestamp(utilDate.getTime()), false);**/
							}
							finalCheckList.add(_check);
						}
						SESSION._CHECKS_HOLDER = finalCheckList;
						Intent i = new Intent();
						i.putExtra(LEAD_POS_RESULT_INTENT_CODE_CHECKS_ACTIVITY, getTotalPid());
						setResult(RESULT_OK, i);
						finish();


					}
                } else {
                    Toast.makeText(ChecksActivity.this, getString(R.string.check_pid_error), Toast.LENGTH_SHORT).show();
                }
            }
		});

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

		btAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(getTotalPid() >= totalPrice){
					//btAdd.setEnabled(false);
					Toast.makeText(ChecksActivity.this,getString(R.string.dont_need_to_add_another_check),Toast.LENGTH_LONG).show();

				}else {

					Check check=new Check();
					int lsttchildID = lvChecks.getChildCount()-1;
					if(lvChecks.getChildCount()>1) {
						ChecksListViewAdapter.ViewHolder lastItem = (ChecksListViewAdapter.ViewHolder) lvChecks.getChildAt(lsttchildID).getTag();

//						Log.i("LOOOGT", checkList.toString());
						checkList.get(lsttchildID-1);
						Date utilDate=DateConverter.stringToDate(lastItem.getEtDate());
						Calendar cal = Calendar.getInstance();
						cal.setTime(utilDate);
						check = new Check(lastItem.getEtCheckNum(), lastItem.getEtBankNum(), lastItem.getEtBenchNum(), lastItem.getEtAccountNum(),
								lastItem.getEtAmount(), new java.sql.Timestamp(utilDate.getTime()), false);
						//lastItem.findViewById(R.id.listChecks_ETAmount);
						checkList.set(lsttchildID-1,check);
					}
					Check newCheck=new Check(check);
					if(check.getCreatedAt()!=null){
						Date date = DateConverter.getAfterMonth(new Date(check.getCreatedAt().getTime()));
						Calendar cal = Calendar.getInstance();
						cal.setTime(date);
						newCheck.setCreatedAt(new java.sql.Timestamp(date.getTime()));
						newCheck.setCheckNum(check.getCheckNum()+1);
					}
					checkList.add(newCheck);
					lvChecks.setAdapter(adapter);
					getTotal();
				}

			}
		});


/**
		lvChecks.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						historicX = event.getX();
						historicY = event.getY();
						return false;

					case MotionEvent.ACTION_UP:
						if (lvChecks.getChildAt(0) != null) {
							int heightOfEachItem = lvChecks.getChildAt(0).getHeight();
							int heightOfFirstItem = -lvChecks.getChildAt(0).getTop() + lvChecks.getFirstVisiblePosition()*heightOfEachItem;
							//IF YOU HAVE CHILDS IN LIST VIEW YOU START COUNTING
							//listView.getChildAt(0).getTop() will see top of child showed in screen
							//Dividing by height of view, you get how many views are not in the screen
							//It needs to be Math.ceil in this case because it sometimes only shows part of last view
							final int firstPosition = (int) Math.ceil(heightOfFirstItem / heightOfEachItem); // This is the same as child #0

							//Here you get your List position, use historic Y to get where the user went first
							final int wantedPosition = (int) Math.floor((historicY - lvChecks.getChildAt(0).getTop()) / heightOfEachItem) + firstPosition;
							//Here you get the actually position in the screen
							final int wantedChild = wantedPosition - firstPosition;
							//Depending on delta, go right or left
							if (event.getX() - historicX < -DELTA) {
								//If something went wrong, we stop it now
                                // (wantedChild < 1) ignore the header row
								if (wantedChild < 1 || wantedChild >= checkList.size()|| wantedChild >= lvChecks.getChildCount()) {
									return true;
								}
								new AlertDialog.Builder(ChecksActivity.this)
										.setTitle("Remove Check")
										.setMessage("Are you want to remove check?")
										.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int which) {
												//Start animation with 500 milliseconds of time
												lvChecks.getChildAt(wantedChild).startAnimation(outToLeftAnimation(500));
												//after 500 miliseconds remove from List the item and update the adapter.
												new java.util.Timer().schedule(
														new java.util.TimerTask() {
															@Override
															public void run() {
																checkList.remove(wantedPosition);
																runOnUiThread(new Runnable() {
																	@Override
																	public void run() {
																		// TODO Auto-generated method stub

																		adapter.notifyDataSetChanged();
																		lvChecks.deferNotifyDataSetChanged();
																	}
																});
															}
														},
														500
												);
											}
										})
										.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int which) {
												// do nothing
											}
										})
										.setIcon(android.R.drawable.ic_dialog_alert)
										.show();
							//	adapter = new ChecksListViewAdapter(getApplicationContext(), R.layout.list_adapter_row_checks, checkList);
								//lvChecks.setAdapter(adapter);
								return true;

							} /*else if (event.getX() - historicX > DELTA) {
								//If something went wrong, we stop it now
								if (wantedChild < 0 || wantedChild >= checkList.size() || wantedChild >= lvChecks.getChildCount()) {

									return true;
								}
								//Start animation with 500 miliseconds of time
								lvChecks.getChildAt(wantedChild).startAnimation(outToRightAnimation(500));
								//after 500 miliseconds remove from List the item and update the adapter.
								new java.util.Timer().schedule(
										new java.util.TimerTask() {
											@Override
											public void run() {
												checkList.remove(wantedPosition);
												runOnUiThread(new Runnable() {
													@Override
													public void run() {
														// TODO Auto-generated method stub

														adapter.notifyDataSetChanged();
														lvChecks.deferNotifyDataSetChanged();
													}
												});
											}
										},
										500
								);
								return true;
							}
						}
						return true;
					default:
						return false;
				}
			}
		});**/
		lvChecks.setOnTouchListener(new View.OnTouchListener() {
			// Setting on Touch Listener for handling the touch inside ScrollView
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Disallow the touch request for parent scroll on touch of child view
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
	}

	private void FunctionDeleteRowWhenSlidingLeft(View v) {
		new AlertDialog.Builder(ChecksActivity.this)
				.setTitle("Remove Check")
				.setMessage("Are you want to remove check?")
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//clearCart();
					}
				})
				.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// do nothing
					}
				})
				.setIcon(android.R.drawable.ic_dialog_alert)
				.show();
	}

	private Animation outToLeftAnimation(int duration) {
		Animation outtoLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoLeft.setDuration(duration);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}

	private Animation outToRightAnimation(int duration) {
		Animation outtoRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoRight.setDuration(duration);
		outtoRight.setInterpolator(new AccelerateInterpolator());
		return outtoRight;
	}

	public void delete(int pos){
		checkList.remove(pos);
		adapter.remove(pos);
		adapter = new ChecksListViewAdapter(this, R.layout.list_adapter_row_checks, checkList);
		lvChecks.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		getTotal();
	}
}
