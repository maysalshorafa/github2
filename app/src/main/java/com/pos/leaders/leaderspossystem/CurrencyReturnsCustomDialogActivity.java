package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyReturnsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Currency.Currency;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Printer.InvoiceImg;
import com.pos.leaders.leaderspossystem.Printer.PrinterTools;
import com.pos.leaders.leaderspossystem.Printer.SUNMI_T1.AidlUtil;
import com.pos.leaders.leaderspossystem.Tools.PrinterType;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static com.pos.leaders.leaderspossystem.SalesCartActivity.printedRows;


public class CurrencyReturnsCustomDialogActivity extends Dialog {

    private Activity c;
    private Button done;
    private TextView tvExcess;
    private Spinner returnSpener;
    private double excess = 0;
    Currency rCurrency;
    private Order sale;
    public static String firstCredit="";
    public static String secondCredit="" ;
    public static String thirdCredit="";
    public static boolean REQUEST_CURRENCY_RETURN_ACTIVITY_CODE = true;


    public CurrencyReturnsCustomDialogActivity() {
        super(null);
    }

    public CurrencyReturnsCustomDialogActivity(@NonNull Context context, @StyleRes int themeResId, Activity c) {
        super(context, themeResId);
        this.c = c;
    }

    public CurrencyReturnsCustomDialogActivity(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, Activity c) {
        super(context, cancelable, cancelListener);
        this.c = c;
    }

    public CurrencyReturnsCustomDialogActivity(Activity a, double excess, Order sale,String firstCredit,String secondCredit ,String thirdCredit) {
        super(a);
        this.c = a;
        this.excess = excess;
        this.sale = sale;
        this.firstCredit=firstCredit;
        this.secondCredit=secondCredit;
       this.thirdCredit=thirdCredit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        done = (Button) findViewById(R.id.btn_done);

        setCanceledOnTouchOutside(false);
        tvExcess = (TextView) findViewById(R.id.cashActivity_TVExcess);
        returnSpener = (Spinner) findViewById(R.id.spinnerForReturnValue);
        if (!SETTINGS.enableCurrencies) {
            returnSpener.setVisibility(View.INVISIBLE);
        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SETTINGS.enableCurrencies) {
                    CurrencyReturnsDBAdapter currencyReturnsDBAdapter = new CurrencyReturnsDBAdapter(getContext());
                    currencyReturnsDBAdapter.open();
                    double returnCurrencyValue = Double.parseDouble(tvExcess.getText().toString());
                    currencyReturnsDBAdapter.insertEntry(sale.getOrderId(), returnCurrencyValue, new Timestamp(System.currentTimeMillis()), rCurrency.getId());
                    ZReportDBAdapter zReportDBAdapter =new ZReportDBAdapter(getContext());
                    zReportDBAdapter.open();
                    try {
                        ZReport zReport =zReportDBAdapter.getLastRow();
                        if(rCurrency.getId()==0) {
                            zReport.setShekelAmount(zReport.getShekelAmount() - returnCurrencyValue);
                        }else if (rCurrency.getId()==1){
                            zReport.setUsdAmount(zReport.getUsdAmount()-returnCurrencyValue);
                        }else if(rCurrency.getId()==2){
                            zReport.setEurAmount(zReport.getEurAmount()-returnCurrencyValue);
                        }else if(rCurrency.getId()==3){
                            zReport.setGbpAmount(zReport.getGbpAmount()-returnCurrencyValue);
                        }
                        zReportDBAdapter.updateEntry(zReport);
                        zReportDBAdapter.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    REQUEST_CURRENCY_RETURN_ACTIVITY_CODE=true;
                    currencyReturnsDBAdapter.close();
                    if(SETTINGS.printer.equals(PrinterType.SUNMI_T1)){
                        printAndOpenCashBoxSUNMI_T1(firstCredit,secondCredit,thirdCredit);
                    }else {
                        if (secondCredit != "") {
                            PrinterTools.printAndOpenCashBox(firstCredit, secondCredit, thirdCredit, 600, getContext(), c);

                        } else {
                            PrinterTools.printAndOpenCashBox("", "", "", 600, getContext(), c);

                        }
                    }

                }

                cancel();
            }
        });

        if (SETTINGS.enableCurrencies) {
            CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(this.c);
            currencyTypeDBAdapter.open();

            List<CurrencyType> currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
            currencyTypeDBAdapter.close();
            CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(getContext());
            currencyDBAdapter.open();
            final List<Currency> currencyList = currencyDBAdapter.getAllCurrencyLastUpdate(currencyTypesList);
            currencyDBAdapter.close();

            final List<String> currency = new ArrayList<String>();
            for (int i = 0; i < currencyTypesList.size(); i++) {
                currency.add(currencyTypesList.get(i).getType());
            }

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, currency);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            rCurrency = currencyList.get(0);

            // attaching data adapter to spinner
            returnSpener.setAdapter(dataAdapter);
            returnSpener.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    Currency currency = currencyList.get(arg2);
                    tvExcess.setText(Util.makePrice(excess / currency.getRate()));
                    rCurrency = currencyList.get(arg2);

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });
        }

        tvExcess.setText(String.format(new Locale("en"), "%.2f", excess));
    }
    private void printAndOpenCashBoxSUNMI_T1(String mainAns, final String mainMer, final String mainCli) {
        //AidlUtil.getInstance().connectPrinterService(this);
        if (AidlUtil.getInstance().isConnect()) {
            final ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setTitle(getContext().getString(R.string.wait_for_finish_printing));

            dialog.show();
            InvoiceImg invoiceImg = new InvoiceImg(getContext());
            byte b = 0;
            try {
                if (mainAns.equals("PINPAD")) {
                    HashMap<String, String> clientNote = new HashMap<String, String>();
                    HashMap<String, String> sellerNote = new HashMap<String, String>();

                    JSONObject jsonObject = new JSONObject(mainMer);
                    Iterator<String> itr = null;
                    try {
                        itr = jsonObject.getJSONObject("receipt").keys();

                        while (itr.hasNext()) {
                            String key = itr.next();
                            if (printedRows.contains(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"))) {
                                if (jsonObject.getJSONObject("receipt").getJSONObject(key).getString("category").equals("BOTH")) {
                                    clientNote.put(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"), jsonObject.getJSONObject("receipt").getJSONObject(key).getString("value"));
                                    sellerNote.put(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"), jsonObject.getJSONObject("receipt").getJSONObject(key).getString("value"));
                                } else if (jsonObject.getJSONObject("receipt").getJSONObject(key).getString("category").equals("CLIENT")) {
                                    clientNote.put(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"), jsonObject.getJSONObject("receipt").getJSONObject(key).getString("value"));
                                } else if (jsonObject.getJSONObject("receipt").getJSONObject(key).getString("category").equals("SELLER")) {
                                    sellerNote.put(jsonObject.getJSONObject("receipt").getJSONObject(key).getString("name"), jsonObject.getJSONObject("receipt").getJSONObject(key).getString("value"));
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Bitmap seller = invoiceImg.pinPadInvoice(SESSION._ORDERS, false, sellerNote);
                    AidlUtil.getInstance().printBitmap(seller);

                    //Thread.sleep(100);

                    AidlUtil.getInstance().feed();
                    AidlUtil.getInstance().cut();


                    Bitmap client = invoiceImg.pinPadInvoice(SESSION._ORDERS, false, clientNote);
                    AidlUtil.getInstance().printBitmap(client);

                } else if (mainMer!="") {
                    Bitmap bitmap = invoiceImg.creditCardInvoice(SESSION._ORDERS, false, mainMer);

                    AidlUtil.getInstance().printBitmap(bitmap);

                    //Thread.sleep(100);

                    AidlUtil.getInstance().feed();
                    AidlUtil.getInstance().cut();
                    //Thread.sleep(100);

                    Bitmap bitmap2 = invoiceImg.creditCardInvoice(SESSION._ORDERS, false, mainCli);
                    AidlUtil.getInstance().printBitmap(bitmap2);
                    //Thread.sleep(100);
                } else if (SESSION._CHECKS_HOLDER != null && SESSION._CHECKS_HOLDER.size() > 0) {
                    Bitmap bitmap = invoiceImg.normalInvoice(SESSION._ORDERS.getOrderId(), SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, SESSION._CHECKS_HOLDER);
                    AidlUtil.getInstance().printBitmap(bitmap);
                } else  {
                    Bitmap bitmap = invoiceImg.normalInvoice(SESSION._TEMP_ORDERS.getOrderId(), SESSION._TEMP_ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, null);
                    AidlUtil.getInstance().printBitmap(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                //cut
                AidlUtil.getInstance().print3Line();
                AidlUtil.getInstance().cut();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                AidlUtil.getInstance().openCashBox();
            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
        } else {
            new android.support.v7.app.AlertDialog.Builder(getContext(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                    .setTitle(getContext().getString(R.string.printer))
                    .setMessage(getContext().getString(R.string.please_connect_the_printer))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            //Toast.makeText(this, "Printer Connect Error!", Toast.LENGTH_LONG).show();
        }
    }
}
