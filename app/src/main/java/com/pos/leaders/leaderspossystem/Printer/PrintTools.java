package com.pos.leaders.leaderspossystem.Printer;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyOperationDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyReturnsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyOperation;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyReturns;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.XReport;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Printer.SM_S230I.MiniPrinterFunctions;
import com.pos.leaders.leaderspossystem.Printer.SUNMI_T1.AidlUtil;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import HPRTAndroidSDK.HPRTPrinterHelper;
import POSAPI.POSInterfaceAPI;
import POSAPI.POSUSBAPI;
import POSSDK.POSSDK;

/**
 * Created by KARAM on 12/01/2017.
 */

public class PrintTools {
    POSSDK pos;
    private static Context context;

    public PrintTools(Context context) {
        this.context = context;
    }

    private void Print_BTB880(Bitmap _bitmap) {
        final POSInterfaceAPI posInterfaceAPI = new POSUSBAPI(context);
        // final UsbPrinter printer = new UsbPrinter(1155, 30016);
        final ProgressDialog dialog = new ProgressDialog(context);
        final Bitmap bitmap = _bitmap;
        dialog.setTitle(context.getString(R.string.wait_for_finish_printing));
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                dialog.show();
                ////Hebrew 15 Windows-1255

                int i = posInterfaceAPI.OpenDevice();
                pos = new POSSDK(posInterfaceAPI);

                pos.textSelectCharSetAndCodePage(POSSDK.CharacterSetUSA, 15);

                pos.systemSelectPrintMode(0);

                pos.systemFeedLine(1);
                //printer.PRN_Init();
                //printer.PRN_PrintAndFeedLine(11);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //printer.PRN_PrintAndFeedLine(11);
                //printer.PRN_HalfCutPaper();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                pos.systemFeedLine(2);
                pos.systemCutPaper(66, 0);

                // pos.cashdrawerOpen(0,20,20);

                posInterfaceAPI.CloseDevice();



            }

            @Override
            protected Void doInBackground(Void... params) {

                pos.imageStandardModeRasterPrint(bitmap, CONSTANT.PRINTER_PAGE_WIDTH);
                //printer.PRN_PrintDotBitmap(bitmap, 0);
                return null;
            }
        }.execute();
    }

    private void Print_WINTEC(Bitmap _bitmap) {
        final POSInterfaceAPI posInterfaceAPI = new POSUSBAPI(context);
        // final UsbPrinter printer = new UsbPrinter(1155, 30016);
        final ProgressDialog dialog = new ProgressDialog(context);
        final Bitmap bitmap = _bitmap;
        dialog.setTitle(context.getString(R.string.wait_for_finish_printing));
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                dialog.show();
                ////Hebrew 15 Windows-1255

                int i = posInterfaceAPI.OpenDevice();
                pos = new POSSDK(posInterfaceAPI);

                pos.textSelectCharSetAndCodePage(POSSDK.CharacterSetUSA, 15);

                pos.systemSelectPrintMode(0);

                pos.systemFeedLine(1);
                //printer.PRN_Init();
                //printer.PRN_PrintAndFeedLine(11);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //printer.PRN_PrintAndFeedLine(11);
                //printer.PRN_HalfCutPaper();
                pos.systemFeedLine(2);
                pos.systemCutPaper(66, 0);

                // pos.cashdrawerOpen(0,20,20);

                posInterfaceAPI.CloseDevice();
                dialog.cancel();

            }

            @Override
            protected Void doInBackground(Void... params) {
                pos.imageStandardModeRasterPrint(bitmap, CONSTANT.PRINTER_PAGE_WIDTH);
                //printer.PRN_PrintDotBitmap(bitmap, 0);
                return null;
            }
        }.execute();
    }


    private void Print_TP805(Bitmap _bitmap) {
        if (HPRT_TP805.connect(context)) {
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setTitle(context.getString(R.string.wait_for_finish_printing));
            final Bitmap bitmap = _bitmap;
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    dialog.show();
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    try {
                        HPRTPrinterHelper.CutPaper(HPRTPrinterHelper.HPRT_PARTIAL_CUT_FEED, 240);
                        HPRTPrinterHelper.ClearPageModePrintAreaData();

                        try {
                            HPRTPrinterHelper.OpenCashdrawer(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                            try {
                                HPRTPrinterHelper.OpenCashdrawer(1);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                                try {
                                    HPRTPrinterHelper.OpenCashdrawer(2);
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                            }
                        }
                        dialog.cancel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                }

                @Override
                protected Void doInBackground(Void... params) {
                    byte b = 0;
                    try {
                        HPRTPrinterHelper.PrintBitmap(bitmap, b, b, 300);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();

        } else {
          //  Toast.makeText(context, "Printer Connect Error!", Toast.LENGTH_SHORT).show();
        }
    }

    private void Print_SUNMI_T1(Bitmap _bitmap) {
        final Bitmap bitmap = _bitmap;
        AidlUtil.getInstance().connectPrinterService(context);
        if (AidlUtil.getInstance().isConnect()) {
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setTitle(context.getString(R.string.wait_for_finish_printing));

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    dialog.show();
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    try {
                        //cut
                        AidlUtil.getInstance().feed();
                        AidlUtil.getInstance().cut();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dialog.cancel();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        AidlUtil.getInstance().printBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        } else {
          //  Toast.makeText(context, "Printer Connect Error!", Toast.LENGTH_LONG).show();
        }
    }


    private void Print_SM_S230I(Bitmap _bitmap) {
        final Bitmap bitmap = _bitmap;
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(context.getString(R.string.wait_for_finish_printing));

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                dialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //feed and cut

                dialog.cancel();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    printSMS230(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private void printSMS230(Bitmap bitmap) {
        String portSettings = "portable;escpos;l";
        String port = "BT:";
        int paperWidth = 576;
        paperWidth = 832; // 4inch (832 dot)
        paperWidth = 576; // 3inch (576 dot)1
        paperWidth = 384; // 2inch (384 dot)
        MiniPrinterFunctions.PrintBitmapImage(context, port,portSettings, bitmap, paperWidth, true, true);

    }


    public void PrintReport(Bitmap _bitmap){
        switch (SETTINGS.printer) {
            case BTP880:
                Print_BTB880(_bitmap);
                break;
            case HPRT_TP805:
                Print_TP805(_bitmap);
                break;
            case SUNMI_T1:
                Print_SUNMI_T1(_bitmap);
                break;
            case SM_S230I:
                Print_SM_S230I(_bitmap);
                break;
        }
    }



    public Bitmap createZReport(ZReport zReport , boolean isCopy  ) {
        return BitmapInvoice.zPrint(context, zReport,  isCopy);
        //return BitmapInvoice.zPrint(context, zReport, cash_plus, cash_minus, check_plus, check_minus, creditCard_plus, creditCard_minus, isCopy, aReport.getAmount());

    }
    public Bitmap createMonthZReport(ZReport zReport , boolean isCopy,Date from, Date to  ) {
        return BitmapInvoice.monthZPrint(context, zReport,  isCopy,from,to);
        //return BitmapInvoice.zPrint(context, zReport, cash_plus, cash_minus, check_plus, check_minus, creditCard_plus, creditCard_minus, isCopy, aReport.getAmount());

    }

    // get Payment List
    public static List<Payment> paymentList(List<Order> sales) {
        List<Payment> pl = new ArrayList<Payment>();
        PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(context);
        CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(context);
        CurrencyReturnsDBAdapter currencyReturnsDBAdapter = new CurrencyReturnsDBAdapter(context);
        paymentDBAdapter.open();
        for (Order s : sales) {
            List<Payment> payments = paymentDBAdapter.getPaymentBySaleID(s.getOrderId());
            /**
             if (SETTINGS.enableCurrencies) {
             for (Payment _p : payments) {
             _p.setCashPayments(cashPaymentDBAdapter.getPaymentBySaleID(_p.getOrderId()));
             _p.setCurrencyReturns(currencyReturnsDBAdapter.getCurencyReturnBySaleID(_p.getOrderId()));
             }
             }**/

            pl.addAll(payments);
        }
        paymentDBAdapter.close();
        return pl;
    }

    // get Cash Payment List
    public static List<CashPayment> cashPaymentList(List<Order> sales) {
        List<CashPayment> pl = new ArrayList<CashPayment>();
        CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(context);
        cashPaymentDBAdapter.open();
        for (Order s : sales) {
            List<CashPayment> payments = cashPaymentDBAdapter.getPaymentBySaleID(s.getOrderId());
            pl.addAll(payments);
        }
        cashPaymentDBAdapter.close();
        return pl;
    }

    // get Currency Return  List
    public static List<CurrencyReturns> returnPaymentList(List<Order> sales) {
        List<CurrencyReturns> pl = new ArrayList<CurrencyReturns>();
        CurrencyReturnsDBAdapter currencyDBAdapter = new CurrencyReturnsDBAdapter(context);
        currencyDBAdapter.open();
        for (Order s : sales) {
            List<CurrencyReturns> payments = currencyDBAdapter.getCurencyReturnBySaleID(s.getOrderId());
            pl.addAll(payments);
        }
        currencyDBAdapter.close();
        return pl;
    }
    public static List<CurrencyOperation> currencyOperationPaymentList(List<Order> sales) {
        List<CurrencyOperation> pl = new ArrayList<CurrencyOperation>();
        CurrencyOperationDBAdapter currencyOperationDBAdapter = new CurrencyOperationDBAdapter(context);
        currencyOperationDBAdapter.open();
        for (Order s : sales) {
            List<CurrencyOperation> payments = currencyOperationDBAdapter.getCurrencyOperationByOrderID(s.getOrderId());
            pl.addAll(payments);
        }
        currencyOperationDBAdapter.close();
        return pl;
    }

    public Bitmap createXReport(XReport xReport , boolean isCopy  ) {
        return BitmapInvoice.xPrint(context, xReport,  isCopy);
        //return BitmapInvoice.zPrint(context, zReport, cash_plus, cash_minus, check_plus, check_minus, creditCard_plus, creditCard_minus, isCopy, aReport.getAmount());

    }
}
