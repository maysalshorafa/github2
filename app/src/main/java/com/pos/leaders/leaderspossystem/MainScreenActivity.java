package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.SaleDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ScheduleWorkersDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.Sale;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SaleDetailsListViewAdapter;


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by KARAM on 20/10/2016.
 */

public class MainScreenActivity extends Activity {
	Button btPayment,btShowProducts,btProductManagement,btDepartmentManagement,
			btLogOut,btOfferManagement,btOfferRuleManagement,btWorkerManagement,
			btSalesManagement,btClearCart;
	TextView tvTotalPrice;
	ListView lvSalesDetails;
	static Sale sale;
	ScheduleWorkersDBAdapter scheduleWorkersDBAdapter;
	PaymentDBAdapter paymentDBAdapter;
	SaleDBAdapter saleDBAdapter;
	OrderDBAdapter orderDBAdapter;
	SaleDetailsListViewAdapter adapter;

	String barcodeScanned ="";
	double saleTotalPrice = 0;


    //PrinterController printerController=null;
    //private CashboxController cashboxController = null;
    int printFlag=-1;
    int printLanguage = 0;

    private void addToCart(Product p){
        SESSION._ORDERS.add(new Order(1,0,p));
        lvSalesDetails.setAdapter(adapter);
        calculateTotalPrice();
    }

    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i("DOWN TAG", ""+ event.getNumber());
		if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			ProductDBAdapter productDBAdapter=new ProductDBAdapter(MainScreenActivity.this);
			productDBAdapter.open();
			Product product=productDBAdapter.getProductByBarCode(barcodeScanned);
			final Intent intent=new Intent(MainScreenActivity.this,ProductsActivity.class);
			intent.putExtra("barcode", barcodeScanned);
			if(product!=null){
                addToCart(product);
			}
			else{
				new AlertDialog.Builder(MainScreenActivity.this)
						.setTitle("Add Product")
						.setMessage("Are you want to add this product?")
						.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {

								startActivity(intent);
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
			Toast.makeText(MainScreenActivity.this, "scanned code is: "+ barcodeScanned, Toast.LENGTH_LONG).show();
			barcodeScanned ="";
			return true;
		}
		else{
			barcodeScanned +=event.getNumber();

		}
		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		//Log.i("UP TAG", ""+ event.getNumber());
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            ProductDBAdapter productDBAdapter=new ProductDBAdapter(MainScreenActivity.this);
            productDBAdapter.open();
            Product product=productDBAdapter.getProductByBarCode(barcodeScanned);
            final Intent intent=new Intent(MainScreenActivity.this,ProductsActivity.class);
            intent.putExtra("barcode", barcodeScanned);
            if(product!=null){
                addToCart(product);
            }
            else{
                new AlertDialog.Builder(MainScreenActivity.this)
                        .setTitle("Add Product")
                        .setMessage("Are you want to add this product?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                startActivity(intent);
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
            Toast.makeText(MainScreenActivity.this, "scanned code is: "+ barcodeScanned, Toast.LENGTH_LONG).show();
            barcodeScanned ="";
            return true;
        }
		else{
		//	barcodeScanned+=event.getCharacters();
		}
		return true;
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
/*
        printFlag=printerController.PrinterController_Close();
        if (printFlag == 0) {
            printerController = null;
            Log.i("Printer","disconnect_Success");
            Toast.makeText(this, "disconnect_Success", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Log.e("Printer","disconnect_Failure");
            Toast.makeText(this, "disconnect_Failure", Toast.LENGTH_SHORT)
                    .show();
        }*/
    }
/*
    private void Controller() {
        // TODO Auto-generated method stub
        int flag = cashboxController.CashboxController_Controller();
        if (-1 == flag) {
            Toast.makeText(this, "CashboxController_Failure",
                    Toast.LENGTH_SHORT).show();
        } else if (0 == flag) {
            Toast.makeText(this, "CashboxController_Success",
                    Toast.LENGTH_SHORT).show();
        }
    }*/
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main_screen);

        //cashboxController = CashboxController.getInstance();
		// Get References of Views
		btPayment = (Button) findViewById(R.id.mainScreen_BTPayment);
		btShowProducts = (Button) findViewById(R.id.mainScreen_BTShowProducts);
		btProductManagement = (Button) findViewById(R.id.mainScreen_BTProductManagement);
		btDepartmentManagement = (Button) findViewById(R.id.mainScreen_BTDepartmentManagement);
		btLogOut = (Button) findViewById(R.id.mainScreen_BTLogOut);
		btOfferManagement=(Button)findViewById(R.id.mainScreen_BTOfferManagement);
		btOfferRuleManagement=(Button)findViewById(R.id.mainScreen_BTOfferRuleManagement);
		btWorkerManagement=(Button)findViewById(R.id.mainScreen_BTWorkerManagement);
		btSalesManagement=(Button)findViewById(R.id.mainScreen_BTSalesManagement);
		btClearCart=(Button)findViewById(R.id.mainScreen_BTClearCart);
        Button btPrint=(Button)findViewById(R.id.button2);

		tvTotalPrice = (TextView) findViewById(R.id.mainScreen_TVTotalPrice);
		lvSalesDetails = (ListView) findViewById(R.id.mainScreen_a);


        btPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ticket();
            }
        });
        //region printer controller
/*
        //connect
        if(printerController==null)
            printerController=PrinterController.getInstance(this);
        try {
            printFlag = printerController.PrinterController_Open();
        }
        catch (Exception ex) {

        }
        if(printFlag==0){
            Log.i("Printer","connect_Success");
            Toast.makeText(this, "connect_Success", Toast.LENGTH_SHORT)
                    .show();
        }
        else{
            Toast.makeText(this, "connect_Failure", Toast.LENGTH_SHORT)
                    .show();
            Log.e("Printer","connect_Failure");
        }
/**/

        //endregion


		if (SESSION._SALE != null) {
			sale = new Sale(SESSION._USER.getId(), new Date(), 0, false, 0,0);
		}
		else
		{
			SESSION._SALE=new Sale(SESSION._USER.getId(), new Date(), 0, false, 0,0);
		}

		if (SESSION._ORDERS != null) {
			adapter = new SaleDetailsListViewAdapter(getApplicationContext(), R.layout.list_adapter_row_main_screen_sales_details, SESSION._ORDERS);
			lvSalesDetails.setAdapter(adapter);
			lvSalesDetails.setFocusable(false);

			calculateTotalPrice();
		}
		else{
			SESSION._ORDERS=new ArrayList<Order>();
			adapter = new SaleDetailsListViewAdapter(getApplicationContext(), R.layout.list_adapter_row_main_screen_sales_details, SESSION._ORDERS);
		}
		lvSalesDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SESSION._ORDERS.get(position).increaseCount();
				lvSalesDetails.setAdapter(adapter);
				calculateTotalPrice();
				// // TODO: 20/10/2016 show dialog box for increes count and remove andd all the options
			}
		});

		scheduleWorkersDBAdapter = new ScheduleWorkersDBAdapter(this);
		scheduleWorkersDBAdapter.open();
		paymentDBAdapter=new PaymentDBAdapter(this);
		paymentDBAdapter.open();
		orderDBAdapter=new OrderDBAdapter(this);
		orderDBAdapter.open();
		saleDBAdapter=new SaleDBAdapter(this);
		saleDBAdapter.open();


		btProductManagement.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainScreenActivity.this, ProductsActivity.class);
				startActivity(intent);
			}
		});

		btDepartmentManagement.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainScreenActivity.this, DepartmentActivity.class);
				startActivity(intent);
			}
		});

		btPayment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//// TODO: 20/10/2016 payment process

				if(SESSION._SALE!=null&&SESSION._ORDERS.size()>0){
					Log.i("this cart ",SESSION._SALE.toString());
					Log.i("this cart have",SESSION._ORDERS.toString());

					final String[] items = {
							getString(R.string.pay_cash),
							getString(R.string.pay_by_credit_card),
							getString(R.string.checks),
							getString(R.string.integrated_payment)};

					AlertDialog.Builder builder = new AlertDialog.Builder(MainScreenActivity.this);
					builder.setTitle("Make your selection");
					builder.setItems(items, new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog, int item) {
							switch (item) {
								case 0:
									//cash
									final Dialog cashDialog=new Dialog(MainScreenActivity.this);
									cashDialog.setTitle(R.string.pay_cash);
									cashDialog.setContentView(R.layout.cash_payment_dialog);
									cashDialog.show();
									final EditText cashETCash=(EditText)cashDialog.findViewById(R.id.cashPaymentDialog_TECash);
									Button cashBTOk=(Button)cashDialog.findViewById(R.id.cashPaymentDialog_BTOk);
									cashBTOk.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											double pid=Double.parseDouble(cashETCash.getText().toString());
											double tot=SESSION._SALE.getTotalPrice();
											if(pid>=tot){
												SESSION._SALE.setTotalPaid(pid);
												Toast.makeText(MainScreenActivity.this, "return :"+(pid-tot), Toast.LENGTH_LONG).show();
												int saleId=saleDBAdapter.insertEntry(SESSION._SALE,1,"dd");
                                                SESSION._SALE.setId(saleId);
												for(Order o:SESSION._ORDERS){
													orderDBAdapter.insertEntry(o.getProductId(),o.getCount(),o.getUserOffer(),saleId);
												}
												paymentDBAdapter.insertEntry("cash",saleTotalPrice,saleId);
                                                ///Controller();
                                                //print();
                                                try {
                                                    Thread.sleep(200);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                clearCart();
												cashDialog.cancel();
											}
											else{
												Toast.makeText(MainScreenActivity.this, "not enough cash :It subtracts "+(-1*(pid-tot))+" "+getApplicationContext().getString(R.string.ins)+
														" to complete the transaction.", Toast.LENGTH_LONG).show();
											}
										}
									});
									Button cashBTCancel=(Button)cashDialog.findViewById(R.id.cashPaymentDialog_BTCancel);
									cashBTCancel.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											cashDialog.cancel();
										}
									});
									break;
								case 1:
									//// TODO: 25/10/2016 credit card
									Toast.makeText(getApplicationContext(),"this function not available.",Toast.LENGTH_LONG).show();
									break;
								case 2:
									//// TODO: 02/11/2016 Checks payment
									break;
								case 3:
									//// TODO: 25/10/2016 integrated payment (cash+credit card)
									Toast.makeText(getApplicationContext(),"this function not available.",Toast.LENGTH_LONG).show();
									break;
							}
						}
					});
					AlertDialog alert = builder.create();
					alert.show();

					//endregion
				}
			}
		});

		btShowProducts.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainScreenActivity.this, ChecksActivity.class);
				startActivity(intent);
			}
		});

		btLogOut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainScreenActivity.this, HomeActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

				try {
					scheduleWorkersDBAdapter.updateEntry(SESSION._SCHEDULEWORKERS.getId(), new Date());
					SESSION._SCHEDULEWORKERS.setExitTime(new Date());
					Log.i("Worker get out", SESSION._SCHEDULEWORKERS.toString());
				}
				catch (Exception ex) {
				}
				SESSION._LogOut();
				startActivity(intent);
			}
		});

		btOfferManagement.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainScreenActivity.this, OfferActivity.class);
				startActivity(intent);
			}
		});

		btOfferRuleManagement.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainScreenActivity.this,OfferRuleActivity.class);
				startActivity(intent);
			}
		});

		btWorkerManagement.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainScreenActivity.this,WorkerManagementActivity.class);
				startActivity(intent);
			}
		});

		btSalesManagement.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainScreenActivity.this,SalesManagementActivity.class);
				startActivity(intent);
			}
		});
		btClearCart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(MainScreenActivity.this)
						.setTitle("Clear Cart")
						.setMessage("Are you want to clear cart?")
						.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								clearCart();
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
		});
	}

	@Override
	public void onBackPressed() {
		/*new AlertDialog.Builder(this)
				.setMessage("Are you sure you want to exit?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//MainScreenActivity.this.finish();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				})
				.show();*/
	};

	public void clearCart(){
		SESSION._Rest();
		adapter = new SaleDetailsListViewAdapter(getApplicationContext(), R.layout.list_adapter_row_main_screen_sales_details, SESSION._ORDERS);
		lvSalesDetails.setAdapter(adapter);
		calculateTotalPrice();
	}
	protected void calculateTotalPrice() {
		saleTotalPrice = 0;
		for (Order o : SESSION._ORDERS) {
			saleTotalPrice += o.getItemTotalPrice();
		}
		SESSION._SALE.setTotalPrice(saleTotalPrice);
		tvTotalPrice.setText(saleTotalPrice + " " + getString(R.string.ins));
	}

	/**
	 * Called when you are no longer visible to the user.  You will next
	 * receive either {@link #onRestart}, {@link #onDestroy}, or nothing,
	 * depending on later user activity.
	 * <p>
	 * <p><em>Derived classes must call through to the super class's
	 * implementation of this method.  If they do not, an exception will be
	 * thrown.</em></p>
	 *
	 * @see #onRestart
	 * @see #onResume
	 * @see #onSaveInstanceState
	 * @see #onDestroy
	 */
	@Override
	protected void onStop() {
		super.onStop();
		scheduleWorkersDBAdapter.close();
		paymentDBAdapter.close();
		orderDBAdapter.close();
		saleDBAdapter.close();
	}
/*
    private void print() {
        String data = "";
        // byte[] test = new byte[] { 0x1B, 0x21, 0x38 };
        // printerController.Write_Command(test);
        // String str = null;
        // try {
        // str = new String(data.getBytes(), "GBK");
        // printerController.PrinterController_Print(str.getBytes());
        // } catch (UnsupportedEncodingException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        String head="חש' מס-קבלה ";
        head+=SESSION._SALE.getId();
        printerController.PrinterController_Font_Bold();
        printerController.PrinterController_Set_Center();
        printerController.PrinterController_Print(head.getBytes(Charset.forName("UTF-8")));
/*
        printerController.PrinterController_Font_Bold();
        printerController.PrinterController_Set_Center();
        printerController.PrinterController_Print(("-----------------\n").getBytes(Charset.forName("UTF-8")));
        printerController.PrinterController_Take_The_Paper(1);

        printerController.PrinterController_Font_Normal_mode();
        printerController.PrinterController_Set_Right();
        for (Order o :
                SESSION._ORDERS) {
            String s=o.getProduct().getName()+"       "+o.getCount()+"     "+o.getItemTotalPrice();
           // printerController.PrinterController_Take_The_Paper(1);
        }*/
/*
       // printerController.PrinterController_Take_The_Paper(2);
    }*/
/*
    private void ticket() {
        // TODO Auto-generated method stub
        printerController.PrinterController_PrinterLanguage(1);


        printerController.PrinterController_Font_Times();
        printerController.PrinterController_Font_Bold();

        printerController.PrinterController_Set_Center();
        printerController
                .PrinterController_Print("חש' מס-קבלה 001"
                        .getBytes());
        printerController.PrinterController_Linefeed();
        printerController
                .PrinterController_Print("--------------------------------"
                        .getBytes());
        printerController.PrinterController_Linefeed();
        printerController
                .PrinterController_Print("שם פריט           קמות   מחיר"
                        .getBytes());
        printerController.PrinterController_Linefeed();
       // printerController.PrinterController_Font_Normal_mode();
        printerController
                .PrinterController_Print("--------------------------------"
                        .getBytes());
        printerController.PrinterController_Linefeed();
        for (Order o :
                SESSION._ORDERS) {
            String s=o.getProduct().getName()+"       "+o.getCount()+"     "+o.getProduct().getPrice();
            printerController
                    .PrinterController_Print(s
                            .getBytes());
            printerController.PrinterController_Linefeed();
        }

        printerController
                .PrinterController_Print("--------------------------------"
                        .getBytes());
        printerController.PrinterController_Linefeed();
        printerController.PrinterController_reset();
        printerController.PrinterController_PrinterLanguage(1);


        printerController.PrinterController_Font_Times();
        printerController.PrinterController_Set_Left();
        printerController
                .PrinterController_Print(("סה''כ   "+saleTotalPrice)
                        .getBytes());
        printerController.PrinterController_Linefeed();

        //// TODO: 14/11/2016 print invoce  


        printerController.PrinterController_Linefeed();
        printerController.PrinterController_Linefeed();
        printerController.PrinterController_Linefeed();
    }*/

}
