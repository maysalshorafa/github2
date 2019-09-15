package com.pos.leaders.leaderspossystem;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClosingReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.DepositAndPullReportDetailsDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.DrawerDepositAndPullReportDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.XReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.ClosingReport;
import com.pos.leaders.leaderspossystem.Models.Currency.Currency;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.DepositAndPullReport;
import com.pos.leaders.leaderspossystem.Models.OpiningReport;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.XReport;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ReportsManagementActivity  extends AppCompatActivity {
    public static final String COM_LEADPOS_XREPORT_FORM = "COM_LEADPOS_XREPORT_FORM";
    public static final String COM_LEADPOS_XREPORT_TO = "COM_LEADPOS_XREPORT_TO";
    public static final String COM_LEADPOS_XREPORT_ID = "COM_LEADPOS_XREPORT_ID";
    public static final String COM_LEADPOS_XREPORT_FLAG = "COM_LEADPOS_XREPORT_FLAG";
    public static final String COM_LEADPOS_XREPORT_TOTAL_AMOUNT = "COM_LEADPOS_XREPORT_TOTAL_AMOUNT";
    public static final String COM_LEADPOS_XREPORT_AMOUNT = "COM_LEADPOS_XREPORT_AMOUNT";

    Button btnZ, btnZView,btnX, btnOrder,btnExFiles ,btnSalesMan , btnInvoice , btnClosingReport , btnMonthZReportView,btnOffers,btnPullReport,btnDepositReport;
    double pullReportTotalAmount=0;
    ZReportDBAdapter zReportDBAdapter;
    OrderDBAdapter saleDBAdapter;

    ZReport lastZReport=null;
    Order lastSale;
    String str;
    double totalZReportAmount =0;
    long pullReportId=0;
    OpiningReportDBAdapter opiningReportDBAdapter = new OpiningReportDBAdapter(ReportsManagementActivity.this);
    ClosingReportDBAdapter closingReportDBAdapter = new ClosingReportDBAdapter(ReportsManagementActivity.this);
    OpiningReport lastOpiningReport =null;
    ClosingReport closingReport=null;
    Currency fCurrency;
    Currency sCurrency;
    Currency tCurrency;
    Currency forthCurrency;
    double firstCurrencyInDefaultValue = 0, secondCurrencyInDefaultValue = 0, thirdCurrencyInDefaultValue = 0, forthCurrencyInDefaultValue = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.report_mangment);

        TitleBar.setTitleBar(this);

        btnZ = (Button) findViewById(R.id.reportManagementActivity_btnZ);
        btnSalesMan=(Button)findViewById(R.id.reportManagementActivity_btnSalesManReport);
        btnZ.setText("Z " + getString(R.string.report));
        btnZView = (Button) findViewById(R.id.reportManagementActivity_btnZView);
        btnZView.setText("Z " + getString(R.string.report) + " " + getString(R.string.view));
        btnX=(Button)findViewById(R.id.reportManagementActivity_btnX);
        btnX.setText("X " + getString(R.string.report));

        btnOrder =(Button)findViewById(R.id.reportManagementActivity_btnSaleManagement);
        btnExFiles = (Button) findViewById(R.id.reportManagementActivity_btnExtractingFiles);
        btnInvoice = (Button) findViewById(R.id.reportManagementActivity_btnInvoice);
        btnClosingReport = (Button) findViewById(R.id.reportManagementActivity_btnClosingReport);
        btnMonthZReportView = (Button)findViewById(R.id.reportManagementActivity_btnMonthZReportView);
        btnPullReport = (Button)findViewById(R.id.reportManagementActivity_btnPullReport);
        btnDepositReport=(Button)findViewById(R.id.reportManagementActivity_btnDepositReport);
        zReportDBAdapter = new ZReportDBAdapter(this);
        saleDBAdapter = new OrderDBAdapter(this);
        btnOffers = (Button)findViewById(R.id.reportManagementActivity_btnOffers);


        //endregion init

        zReportDBAdapter.open();
        saleDBAdapter.open();
        lastSale = saleDBAdapter.getLast();
        if (lastSale == null) {
            btnZ.setEnabled(false);
            btnX.setEnabled(false);
        }
        else {

            try {
                lastZReport = zReportDBAdapter.getLastRow();

                if (lastZReport.getEndOrderId() == lastSale.getOrderId()) {
                    btnZ.setEnabled(false);
                } else {
                    btnZ.setEnabled(true);
                }
                // dis enable X Report if no row insert in ZReport Table
                if (lastZReport == null) {
                    btnX.setEnabled(false);
                }

            } catch (Exception ex) {
                Log.e(ex.getLocalizedMessage(), ex.getMessage());
            }
        }
        if(lastZReport==null) {btnX.setEnabled(false);}
        try {
            opiningReportDBAdapter.open();
            closingReportDBAdapter.open();
            lastOpiningReport = opiningReportDBAdapter.getLastRow();
            closingReport=closingReportDBAdapter.getLastRow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(closingReport!=null) {
            if (lastOpiningReport.getOpiningReportId() == closingReport.getOpiningReportId()) {
                btnClosingReport.setEnabled(false);
            } else {
                btnClosingReport.setEnabled(true);
            }
        }
        btnZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ReportsManagementActivity.this)
                        .setTitle(getString(R.string.create_z_report))
                        .setMessage(getString(R.string.create_z_report_message))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                OpiningReport opiningReport = Util.getLastAReport(getApplicationContext());
                                ClosingReportDBAdapter closingReportDBAdapter =new ClosingReportDBAdapter(getApplicationContext());
                                closingReportDBAdapter.open();
                                ClosingReport closingReport = closingReportDBAdapter.getClosingReportByOpiningReportId(opiningReport.getOpiningReportId());
                                if(closingReport!=null){
                                ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(ReportsManagementActivity.this);
                                zReportDBAdapter.open();
                                ZReport lastZReport = Util.getLastZReport(getApplicationContext());

                                if (lastZReport == null) {
                                    lastZReport = new ZReport();
                                    lastZReport.setEndOrderId(0);
                                }
                                ZReport z = new ZReport(0,  new Timestamp(System.currentTimeMillis()), SESSION._EMPLOYEE, lastZReport.getEndOrderId() + 1, lastSale);
                                z.setByUser(SESSION._EMPLOYEE.getEmployeeId());
                                double amount = zReportDBAdapter.getZReportAmount(z.getStartOrderId(), z.getEndOrderId());
                                    try {
                                        totalZReportAmount=zReportDBAdapter.getLastRow().getTotalPosSales()+amount;
                                    } catch (Exception e) {
                                        totalZReportAmount=amount;

                                        e.printStackTrace();
                                    }
                                    z.setInvoiceReceiptAmount(amount);
                                    z.setTotalAmount(amount);
                                    z.setTotalSales(amount);
                                    z.setTotalPosSales(totalZReportAmount);
                                ZReport zReport= Util.insertZReport(z,getApplicationContext());
                                Intent i = new Intent(ReportsManagementActivity.this, ReportZDetailsActivity.class);
                                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_ID, zReport.getzReportId());
                                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_FORM, zReport.getStartOrderId());
                                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_TO, zReport.getEndOrderId());
                                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_TOTAL_AMOUNT,totalZReportAmount);
                                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_AMOUNT,amount);


                                startActivity(i);
                                btnZ.setEnabled(false);
                                }else {
                                    new AlertDialog.Builder(ReportsManagementActivity.this)
                                            .setTitle(getString(R.string.create_closing_report))
                                            .setMessage(getString(R.string.you_must_create_closing_report_before_z_report))
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent i = new Intent(ReportsManagementActivity.this, ClosingReportActivity.class);
                                                    startActivity(i);
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
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


                //// TODO: 30/03/2017 check error
                //Backup backup = new Backup(ReportsManagementActivity.this, String.format(new Locale("en"), new Date().getTime() + ""));
                //backup.encBackupDB();

            }
        });
        btnX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastZReport!=null) {
                    double totalXreportAmount = 0;
                    XReportDBAdapter xReportDBAdapter = new XReportDBAdapter(ReportsManagementActivity.this);
                    xReportDBAdapter.open();
                    XReport x = new XReport(0, new Timestamp(System.currentTimeMillis()), SESSION._EMPLOYEE, lastZReport.getEndOrderId() + 1, lastSale);
                    x.setByUser(SESSION._EMPLOYEE.getEmployeeId());
                    double amount = xReportDBAdapter.getXReportAmount(x.getStartOrderId(), x.getEndOrderId());
                    try {
                        totalXreportAmount = xReportDBAdapter.getLastRow().getTotalPosSales() + amount;
                    } catch (Exception e) {
                        totalXreportAmount = amount;

                        e.printStackTrace();
                    }
                    x.setInvoiceReceiptAmount(amount);
                    x.setTotalAmount(amount);
                    x.setTotalSales(amount);
                    x.setTotalPosSales(totalXreportAmount);
                    XReport xReport = Util.insertXReport(x, getApplicationContext());
                    Intent i = new Intent(ReportsManagementActivity.this, ReportZDetailsActivity.class);
                    i.putExtra(COM_LEADPOS_XREPORT_ID, xReport.getxReportId());
                    i.putExtra(COM_LEADPOS_XREPORT_FORM, xReport.getStartOrderId());
                    i.putExtra(COM_LEADPOS_XREPORT_TO, xReport.getEndOrderId());
                    i.putExtra(COM_LEADPOS_XREPORT_TOTAL_AMOUNT, totalXreportAmount);
                    i.putExtra(COM_LEADPOS_XREPORT_AMOUNT, amount);
                    i.putExtra(COM_LEADPOS_XREPORT_FLAG, true);

                    startActivity(i);
                }

            }
        });
        btnMonthZReportView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReportsManagementActivity.this,MonthZReportView.class);
                startActivity(intent);
            }
        });
        btnZView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReportsManagementActivity.this,ZReportActivity.class);
                intent.putExtra("permissions_name",str);

                startActivity(intent);
            }
        });
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportsManagementActivity.this, OrdersManagementActivity.class);
                startActivity(intent);
            }
        });

        btnExFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportsManagementActivity.this, OpenFormatActivity.class);
                startActivity(intent);
            }
        });
        btnSalesMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportsManagementActivity.this, SalesManManagementActivity.class);
                startActivity(intent);
            }
        });
        btnInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = {
                        getString(R.string.invoice),
                        getString(R.string.receipt),getString(R.string.order_document),getString(R.string.create_credit_invoice_doc),getString(R.string.view_credit_invoice_doc)

                };
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ReportsManagementActivity.this);
                builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                Intent intent = new Intent(ReportsManagementActivity.this, SalesCartActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                Intent intent1 = new Intent(ReportsManagementActivity.this, InvoiceManagementActivity.class);
                                startActivity(intent1);
                                break;
                            case 2:

                                Intent intent2 = new Intent(ReportsManagementActivity.this, OrderDocumentManagementActivity.class);
                                startActivity(intent2);
                                break;
                            case 3:
                                Intent intent3 = new Intent(ReportsManagementActivity.this, CreateCreditInvoiceActivity.class);
                                startActivity(intent3);
                                break;
                            case 4:
                                Intent intent4= new Intent(ReportsManagementActivity.this, ViewCreditInvoiceActivity.class);
                                startActivity(intent4);
                                break;


                        }
                    }
                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });
        btnClosingReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportsManagementActivity.this, ClosingReportActivity.class);
                startActivity(intent);
            }
        });
        btnOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportsManagementActivity.this, OfferManagementActivity.class);
                startActivity(intent);
            }
        });
        final String[] pullReportItem = {
                getString(R.string.view_pull_report),
                getString(R.string.add_pull_report),
        };
        final String[] depositReportItem = {
                getString(R.string.view_deposit_report),
                getString(R.string.add_deposit_report),
        };
        btnPullReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ReportsManagementActivity.this);
                builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                builder.setItems(pullReportItem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent;
                        switch (item) {
                            case 0:
                                intent = new Intent(ReportsManagementActivity.this, DepositAndPullReportView.class);
                                startActivity(intent);
                                break;
                            case 1:
                                firstCurrencyInDefaultValue = 0;
                                secondCurrencyInDefaultValue = 0;
                                thirdCurrencyInDefaultValue = 0;
                                forthCurrencyInDefaultValue = 0;
                                final Dialog pullReportDialog = new Dialog(ReportsManagementActivity.this);
                                pullReportDialog.setTitle(R.string.pull_report);
                                pullReportDialog.setContentView(R.layout.areport_details_dialog);
                                pullReportDialog.setCancelable(false);
                                //Getting default currencies name and values
                                List<CurrencyType> currencyTypesList = null;
                                final List<Currency> currenciesList;
                                List<String> currenciesNames;
                                CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(ReportsManagementActivity.this);
                                currencyTypeDBAdapter.open();
                                currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
                                currencyTypeDBAdapter.close();
                                final CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(ReportsManagementActivity.this);
                                currencyDBAdapter.open();
                                currenciesList = currencyDBAdapter.getAllCurrencyLastUpdate(currencyTypesList);
                                currenciesNames = new ArrayList<String>();
                                for (int i = 0; i < currencyTypesList.size(); i++) {
                                    currenciesNames.add(currencyTypesList.get(i).getType());
                                }

                                final DepositAndPullReportDetailsDbAdapter depositAndPullReportDetailsDbAdapter = new DepositAndPullReportDetailsDbAdapter(ReportsManagementActivity.this);
                              depositAndPullReportDetailsDbAdapter.open();
                                // Creating adapter for spinner
                                final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ReportsManagementActivity.this, android.R.layout.simple_spinner_item, currenciesNames);

                                // Drop down layout style - list view with radio button
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // attaching data adapter to spinner
                                Spinner sFirstCurrency = (Spinner) pullReportDialog.findViewById(R.id.aReportDetailsDialog_SPForFirstCurrency);
                                Spinner sSecondCurrency = (Spinner) pullReportDialog.findViewById(R.id.aReportDetailsDialog_SPForSecondCurrency);
                                Spinner sThirdCurrency = (Spinner) pullReportDialog.findViewById(R.id.aReportDetailsDialog_SPForThirdCurrency);
                                Spinner sForthCurrency = (Spinner) pullReportDialog.findViewById(R.id.aReportDetailsDialog_SPForForthCurrency);
                                final Button btCancel = (Button) pullReportDialog.findViewById(R.id.cashPaymentDialog_BTCancel);
                                sFirstCurrency.setAdapter(dataAdapter);
                                sSecondCurrency.setAdapter(dataAdapter);
                                sThirdCurrency.setAdapter(dataAdapter);
                                sForthCurrency.setAdapter(dataAdapter);
                                fCurrency = currenciesList.get(0);
                                sCurrency = currenciesList.get(0);
                                tCurrency = currenciesList.get(0);
                                forthCurrency = currenciesList.get(0);
                                // define editText , Button ,ImageView ,Layout
                                final EditText ETFirstCurrencyAmount = (EditText) pullReportDialog.findViewById(R.id.aReportDetailsDialog_ETFirstCurrencyAmount);
                                final EditText ETSecondCurrencyAmount = (EditText) pullReportDialog.findViewById(R.id.aReportDetailsDialog_ETSecondCurrencyAmount);
                                final EditText ETThirdCurrencyAmount = (EditText) pullReportDialog.findViewById(R.id.aReportDetailsDialog_ETThirdCurrencyAmount);
                                final EditText ETForthCurrencyAmount = (EditText) pullReportDialog.findViewById(R.id.aReportDetailsDialog_ETForthCurrencyAmount);
                                final Button btOKForCurrency = (Button) pullReportDialog.findViewById(R.id.aReportDetailsDialog_BTOK);
                                ImageView addSecondCurrency = (ImageView) pullReportDialog.findViewById(R.id.addSecondCurrency);
                                ImageView addThirdCurrency = (ImageView) pullReportDialog.findViewById(R.id.addThirdCurrency);
                                ImageView addForthCurrency = (ImageView) pullReportDialog.findViewById(R.id.addForthCurrency);
                                final LinearLayout secondCurrencyLayout = (LinearLayout) pullReportDialog.findViewById(R.id.secondCurrencyLayout);
                                final LinearLayout thirdCurrencyLayout = (LinearLayout) pullReportDialog.findViewById(R.id.thirdCurrencyLayout);
                                final LinearLayout forthCurrencyLayout = (LinearLayout) pullReportDialog.findViewById(R.id.forhCurrencyLayout);
                                pullReportDialog.setCanceledOnTouchOutside(false);

                                pullReportDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        ReportsManagementActivity.this.onResume();
                                    }
                                });
                                btCancel.setOnClickListener(new View.OnClickListener()

                                {
                                    @Override
                                    public void onClick(View v) {
                                        pullReportDialog.dismiss();
                                    }
                                });
                                addSecondCurrency.setOnClickListener(new View.OnClickListener()

                                {
                                    @Override
                                    public void onClick(View v) {
                                        secondCurrencyLayout.setVisibility(View.VISIBLE);
                                    }
                                });
                                addThirdCurrency.setOnClickListener(new View.OnClickListener()

                                {
                                    @Override
                                    public void onClick(View v) {
                                        thirdCurrencyLayout.setVisibility(View.VISIBLE);
                                    }
                                });
                                addForthCurrency.setOnClickListener(new View.OnClickListener()

                                {
                                    @Override
                                    public void onClick(View v) {
                                        forthCurrencyLayout.setVisibility(View.VISIBLE);
                                    }
                                });
                                sFirstCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        fCurrency = currenciesList.get(position);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                                sSecondCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                        sCurrency = currenciesList.get(position);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });

                                sThirdCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        tCurrency = currenciesList.get(position);
                                    }


                                    @Override
                                    public void onNothingSelected(AdapterView<?> arg0) {
                                    }
                                });
                                sForthCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        forthCurrency = currenciesList.get(position);
                                    }


                                    @Override
                                    public void onNothingSelected(AdapterView<?> arg0) {
                                    }
                                });


                                ETFirstCurrencyAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                    @Override
                                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                                            btOKForCurrency.callOnClick();
                                        }
                                        return false;
                                    }
                                });
                                ETFirstCurrencyAmount.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        if (!ETFirstCurrencyAmount.getText().toString().equals("")) {
                                            firstCurrencyInDefaultValue = Double.parseDouble(ETFirstCurrencyAmount.getText().toString());

                                        } else {
                                            firstCurrencyInDefaultValue = 0;
                                        }
                                    }
                                });

                                ETSecondCurrencyAmount.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        if (!ETSecondCurrencyAmount.getText().toString().equals("")) {
                                            secondCurrencyInDefaultValue = Double.parseDouble(ETSecondCurrencyAmount.getText().toString());

                                        } else {
                                            secondCurrencyInDefaultValue = 0;
                                        }
                                    }
                                });

                                ETThirdCurrencyAmount.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        if (!ETThirdCurrencyAmount.getText().toString().equals("")) {
                                            thirdCurrencyInDefaultValue = Double.parseDouble(ETThirdCurrencyAmount.getText().toString());

                                        } else {
                                            thirdCurrencyInDefaultValue = 0;
                                        }
                                    }
                                });
                                ETForthCurrencyAmount.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        if (!ETForthCurrencyAmount.getText().toString().equals("")) {
                                            forthCurrencyInDefaultValue = Double.parseDouble(ETForthCurrencyAmount.getText().toString());

                                        } else {
                                            forthCurrencyInDefaultValue = 0;
                                        }
                                    }
                                });

                                btOKForCurrency.setOnClickListener(new View.OnClickListener()

                                {
                                    @Override
                                    public void onClick(View v) {
                                        String str = ETFirstCurrencyAmount.getText().toString();
                                        if (!str.equals("")) {
                                            ZReportDBAdapter zreportDbAdapter = new ZReportDBAdapter(ReportsManagementActivity.this);
                                            zreportDbAdapter.open();
                                            ZReport z=new ZReport();
                                            try {
                                                 z = zreportDbAdapter.getLastRow();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            pullReportTotalAmount = firstCurrencyInDefaultValue * fCurrency.getRate() + secondCurrencyInDefaultValue * sCurrency.getRate() + thirdCurrencyInDefaultValue * tCurrency.getRate() + forthCurrencyInDefaultValue * forthCurrency.getRate();
                                            DrawerDepositAndPullReportDbAdapter pullReportDBAdapter = new DrawerDepositAndPullReportDbAdapter(ReportsManagementActivity.this);
                                            pullReportDBAdapter.open();
                                            if(z.getzReportId()>0) {
                                                long id = pullReportDBAdapter.insertEntry(new Timestamp(System.currentTimeMillis()), SESSION._EMPLOYEE.getEmployeeId(), pullReportTotalAmount, "Pull", z.getzReportId());

                                            }else {
                                                long id = pullReportDBAdapter.insertEntry(new Timestamp(System.currentTimeMillis()), SESSION._EMPLOYEE.getEmployeeId(), pullReportTotalAmount, "Pull", -1);

                                            }
                                            DepositAndPullReport pullReport = null;
                                            try {
                                                pullReportId = pullReportDBAdapter.getLastRow().getDepositAndPullReportId();
                                                pullReport= pullReportDBAdapter.getById(pullReportId);
                                                pullReportDBAdapter.close();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            if (firstCurrencyInDefaultValue > 0) {
                                                depositAndPullReportDetailsDbAdapter.insertEntry(pullReportId, firstCurrencyInDefaultValue, fCurrency.getName());
                                            }
                                            if (secondCurrencyInDefaultValue > 0) {
                                                depositAndPullReportDetailsDbAdapter.insertEntry(pullReportId, secondCurrencyInDefaultValue, sCurrency.getName());
                                            }
                                            if (thirdCurrencyInDefaultValue > 0) {
                                                depositAndPullReportDetailsDbAdapter.insertEntry(pullReportId, thirdCurrencyInDefaultValue, tCurrency.getName());
                                            }
                                            if (forthCurrencyInDefaultValue > 0) {
                                                depositAndPullReportDetailsDbAdapter.insertEntry(pullReportId, forthCurrencyInDefaultValue, forthCurrency.getName());
                                            }

                                            pullReportDialog.cancel();
                                            pullReportDBAdapter.close();
                                            depositAndPullReportDetailsDbAdapter.close();
                                            final ArrayList<String> hintForCurrencyType = new ArrayList<String>();
                                            final ArrayList<Double> hintForCurrencyAmount = new ArrayList<Double>();
                                            hintForCurrencyType.add(fCurrency.getName());
                                            hintForCurrencyType.add(sCurrency.getName());
                                            hintForCurrencyType.add(tCurrency.getName());
                                            hintForCurrencyType.add(forthCurrency.getName());
                                            hintForCurrencyAmount.add(firstCurrencyInDefaultValue);
                                            hintForCurrencyAmount.add(secondCurrencyInDefaultValue);
                                            hintForCurrencyAmount.add(thirdCurrencyInDefaultValue);
                                            hintForCurrencyAmount.add(forthCurrencyInDefaultValue);
                                            Util.pullAndDepositReport(ReportsManagementActivity.this,pullReport,hintForCurrencyType,hintForCurrencyAmount,"Pull");

                                        }
                                    }
                                });

                                pullReportDialog.show();
                                break;

                        }

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        btnDepositReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ReportsManagementActivity.this);
                builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                builder.setItems(depositReportItem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent;
                        switch (item) {
                            case 0:
                                intent = new Intent(ReportsManagementActivity.this, DepositAndPullReportView.class);
                                startActivity(intent);
                                break;
                            case 1:
                                firstCurrencyInDefaultValue = 0;
                                secondCurrencyInDefaultValue = 0;
                                thirdCurrencyInDefaultValue = 0;
                                forthCurrencyInDefaultValue = 0;
                                final Dialog pullReportDialog = new Dialog(ReportsManagementActivity.this);
                                pullReportDialog.setTitle(R.string.deposit_report);
                                pullReportDialog.setContentView(R.layout.areport_details_dialog);
                                pullReportDialog.setCancelable(false);
                                //Getting default currencies name and values
                                List<CurrencyType> currencyTypesList = null;
                                final List<Currency> currenciesList;
                                List<String> currenciesNames;
                                CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(ReportsManagementActivity.this);
                                currencyTypeDBAdapter.open();
                                currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
                                currencyTypeDBAdapter.close();
                                final CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(ReportsManagementActivity.this);
                                currencyDBAdapter.open();
                                currenciesList = currencyDBAdapter.getAllCurrencyLastUpdate(currencyTypesList);
                                currenciesNames = new ArrayList<String>();
                                for (int i = 0; i < currencyTypesList.size(); i++) {
                                    currenciesNames.add(currencyTypesList.get(i).getType());
                                }

                                final DepositAndPullReportDetailsDbAdapter depositAndPullReportDetailsDbAdapter = new DepositAndPullReportDetailsDbAdapter(ReportsManagementActivity.this);
                                depositAndPullReportDetailsDbAdapter.open();
                                // Creating adapter for spinner
                                final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ReportsManagementActivity.this, android.R.layout.simple_spinner_item, currenciesNames);

                                // Drop down layout style - list view with radio button
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // attaching data adapter to spinner
                                Spinner sFirstCurrency = (Spinner) pullReportDialog.findViewById(R.id.aReportDetailsDialog_SPForFirstCurrency);
                                Spinner sSecondCurrency = (Spinner) pullReportDialog.findViewById(R.id.aReportDetailsDialog_SPForSecondCurrency);
                                Spinner sThirdCurrency = (Spinner) pullReportDialog.findViewById(R.id.aReportDetailsDialog_SPForThirdCurrency);
                                Spinner sForthCurrency = (Spinner) pullReportDialog.findViewById(R.id.aReportDetailsDialog_SPForForthCurrency);
                                final Button btCancel = (Button) pullReportDialog.findViewById(R.id.cashPaymentDialog_BTCancel);
                                sFirstCurrency.setAdapter(dataAdapter);
                                sSecondCurrency.setAdapter(dataAdapter);
                                sThirdCurrency.setAdapter(dataAdapter);
                                sForthCurrency.setAdapter(dataAdapter);
                                fCurrency = currenciesList.get(0);
                                sCurrency = currenciesList.get(0);
                                tCurrency = currenciesList.get(0);
                                forthCurrency = currenciesList.get(0);
                                // define editText , Button ,ImageView ,Layout
                                final EditText ETFirstCurrencyAmount = (EditText) pullReportDialog.findViewById(R.id.aReportDetailsDialog_ETFirstCurrencyAmount);
                                final EditText ETSecondCurrencyAmount = (EditText) pullReportDialog.findViewById(R.id.aReportDetailsDialog_ETSecondCurrencyAmount);
                                final EditText ETThirdCurrencyAmount = (EditText) pullReportDialog.findViewById(R.id.aReportDetailsDialog_ETThirdCurrencyAmount);
                                final EditText ETForthCurrencyAmount = (EditText) pullReportDialog.findViewById(R.id.aReportDetailsDialog_ETForthCurrencyAmount);
                                final Button btOKForCurrency = (Button) pullReportDialog.findViewById(R.id.aReportDetailsDialog_BTOK);
                                ImageView addSecondCurrency = (ImageView) pullReportDialog.findViewById(R.id.addSecondCurrency);
                                ImageView addThirdCurrency = (ImageView) pullReportDialog.findViewById(R.id.addThirdCurrency);
                                ImageView addForthCurrency = (ImageView) pullReportDialog.findViewById(R.id.addForthCurrency);
                                final LinearLayout secondCurrencyLayout = (LinearLayout) pullReportDialog.findViewById(R.id.secondCurrencyLayout);
                                final LinearLayout thirdCurrencyLayout = (LinearLayout) pullReportDialog.findViewById(R.id.thirdCurrencyLayout);
                                final LinearLayout forthCurrencyLayout = (LinearLayout) pullReportDialog.findViewById(R.id.forhCurrencyLayout);
                                pullReportDialog.setCanceledOnTouchOutside(false);

                                pullReportDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        ReportsManagementActivity.this.onResume();
                                    }
                                });
                                btCancel.setOnClickListener(new View.OnClickListener()

                                {
                                    @Override
                                    public void onClick(View v) {
                                        pullReportDialog.dismiss();
                                    }
                                });
                                addSecondCurrency.setOnClickListener(new View.OnClickListener()

                                {
                                    @Override
                                    public void onClick(View v) {
                                        secondCurrencyLayout.setVisibility(View.VISIBLE);
                                    }
                                });
                                addThirdCurrency.setOnClickListener(new View.OnClickListener()

                                {
                                    @Override
                                    public void onClick(View v) {
                                        thirdCurrencyLayout.setVisibility(View.VISIBLE);
                                    }
                                });
                                addForthCurrency.setOnClickListener(new View.OnClickListener()

                                {
                                    @Override
                                    public void onClick(View v) {
                                        forthCurrencyLayout.setVisibility(View.VISIBLE);
                                    }
                                });
                                sFirstCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        fCurrency = currenciesList.get(position);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                                sSecondCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                        sCurrency = currenciesList.get(position);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });

                                sThirdCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        tCurrency = currenciesList.get(position);
                                    }


                                    @Override
                                    public void onNothingSelected(AdapterView<?> arg0) {
                                    }
                                });
                                sForthCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        forthCurrency = currenciesList.get(position);
                                    }


                                    @Override
                                    public void onNothingSelected(AdapterView<?> arg0) {
                                    }
                                });


                                ETFirstCurrencyAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                    @Override
                                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                                            btOKForCurrency.callOnClick();
                                        }
                                        return false;
                                    }
                                });
                                ETFirstCurrencyAmount.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        if (!ETFirstCurrencyAmount.getText().toString().equals("")) {
                                            firstCurrencyInDefaultValue = Double.parseDouble(ETFirstCurrencyAmount.getText().toString());

                                        } else {
                                            firstCurrencyInDefaultValue = 0;
                                        }
                                    }
                                });

                                ETSecondCurrencyAmount.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        if (!ETSecondCurrencyAmount.getText().toString().equals("")) {
                                            secondCurrencyInDefaultValue = Double.parseDouble(ETSecondCurrencyAmount.getText().toString());

                                        } else {
                                            secondCurrencyInDefaultValue = 0;
                                        }
                                    }
                                });

                                ETThirdCurrencyAmount.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        if (!ETThirdCurrencyAmount.getText().toString().equals("")) {
                                            thirdCurrencyInDefaultValue = Double.parseDouble(ETThirdCurrencyAmount.getText().toString());

                                        } else {
                                            thirdCurrencyInDefaultValue = 0;
                                        }
                                    }
                                });
                                ETForthCurrencyAmount.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        if (!ETForthCurrencyAmount.getText().toString().equals("")) {
                                            forthCurrencyInDefaultValue = Double.parseDouble(ETForthCurrencyAmount.getText().toString());

                                        } else {
                                            forthCurrencyInDefaultValue = 0;
                                        }
                                    }
                                });

                                btOKForCurrency.setOnClickListener(new View.OnClickListener()

                                {
                                    @Override
                                    public void onClick(View v) {
                                        String str = ETFirstCurrencyAmount.getText().toString();
                                        if (!str.equals("")) {
                                            ZReportDBAdapter zreportDbAdapter = new ZReportDBAdapter(ReportsManagementActivity.this);
                                            zreportDbAdapter.open();
                                            ZReport z=new ZReport();
                                            try {
                                                z = zreportDbAdapter.getLastRow();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            pullReportTotalAmount = firstCurrencyInDefaultValue * fCurrency.getRate() + secondCurrencyInDefaultValue * sCurrency.getRate() + thirdCurrencyInDefaultValue * tCurrency.getRate() + forthCurrencyInDefaultValue * forthCurrency.getRate();
                                            DrawerDepositAndPullReportDbAdapter pullReportDBAdapter = new DrawerDepositAndPullReportDbAdapter(ReportsManagementActivity.this);
                                            pullReportDBAdapter.open();
                                            if(z.getzReportId()>0) {
                                                long id = pullReportDBAdapter.insertEntry(new Timestamp(System.currentTimeMillis()), SESSION._EMPLOYEE.getEmployeeId(), pullReportTotalAmount, "Deposit", z.getzReportId());

                                            }else {
                                                long id = pullReportDBAdapter.insertEntry(new Timestamp(System.currentTimeMillis()), SESSION._EMPLOYEE.getEmployeeId(), pullReportTotalAmount, "Deposit", -1);

                                            }                                            DepositAndPullReport pullReport = null;
                                            try {
                                                pullReportId = pullReportDBAdapter.getLastRow().getDepositAndPullReportId();
                                                pullReport= pullReportDBAdapter.getById(pullReportId);
                                                pullReportDBAdapter.close();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            if (firstCurrencyInDefaultValue > 0) {
                                                depositAndPullReportDetailsDbAdapter.insertEntry(pullReportId, firstCurrencyInDefaultValue, fCurrency.getName());
                                            }
                                            if (secondCurrencyInDefaultValue > 0) {
                                                depositAndPullReportDetailsDbAdapter.insertEntry(pullReportId, secondCurrencyInDefaultValue, sCurrency.getName());
                                            }
                                            if (thirdCurrencyInDefaultValue > 0) {
                                                depositAndPullReportDetailsDbAdapter.insertEntry(pullReportId, thirdCurrencyInDefaultValue, tCurrency.getName());
                                            }
                                            if (forthCurrencyInDefaultValue > 0) {
                                                depositAndPullReportDetailsDbAdapter.insertEntry(pullReportId, forthCurrencyInDefaultValue, forthCurrency.getName());
                                            }

                                            pullReportDialog.cancel();
                                            pullReportDBAdapter.close();
                                            depositAndPullReportDetailsDbAdapter.close();
                                            final ArrayList<String> hintForCurrencyType = new ArrayList<String>();
                                            final ArrayList<Double> hintForCurrencyAmount = new ArrayList<Double>();
                                            hintForCurrencyType.add(fCurrency.getName());
                                            hintForCurrencyType.add(sCurrency.getName());
                                            hintForCurrencyType.add(tCurrency.getName());
                                            hintForCurrencyType.add(forthCurrency.getName());
                                            hintForCurrencyAmount.add(firstCurrencyInDefaultValue);
                                            hintForCurrencyAmount.add(secondCurrencyInDefaultValue);
                                            hintForCurrencyAmount.add(thirdCurrencyInDefaultValue);
                                            hintForCurrencyAmount.add(forthCurrencyInDefaultValue);
                                            Util.pullAndDepositReport(ReportsManagementActivity.this,pullReport,hintForCurrencyType,hintForCurrencyAmount,"Deposit");

                                        }
                                    }
                                });

                                pullReportDialog.show();
                                break;

                        }

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();
      /**  if (extras != null) {
            str = extras.getString("permissions_name");


        }**/

    }

    @Override
    protected void onDestroy() {
        zReportDBAdapter.open();
        saleDBAdapter.open();
        super.onDestroy();
    }
}
