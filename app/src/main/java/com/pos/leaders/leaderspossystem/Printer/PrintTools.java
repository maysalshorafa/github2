package com.pos.leaders.leaderspossystem.Printer;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyOperationDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyReturnsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyOperation;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyReturns;
import com.pos.leaders.leaderspossystem.Models.Employee;
import com.pos.leaders.leaderspossystem.Models.OpiningReport;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Printer.SM_S230I.MiniPrinterFunctions;
import com.pos.leaders.leaderspossystem.Printer.SUNMI_T1.AidlUtil;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

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

    public Bitmap createXReport(long endSaleId, long id, Employee user, Date date) {
        long aReportId = 0;
        double usd_plus = 0, usd_minus = 0;
        double eur_plus = 0, eur_minus = 0;
        double gbp_plus = 0, gbp_minus = 0;
        double sheqle_plus = 0, sheqle_minus = 0;
        double aReportAmount = 0;
        OrderDBAdapter saleDBAdapter = new OrderDBAdapter(context);
        saleDBAdapter.open();


        List<Order> sales = saleDBAdapter.getBetween(endSaleId, id);

        saleDBAdapter.close();

        OpiningReportDBAdapter aReportDBAdapter = new OpiningReportDBAdapter(context);
        aReportDBAdapter.open();
        OpiningReport aReport = aReportDBAdapter.getByLastZReport((int) id);
        try {
            aReportAmount = aReportDBAdapter.getLastRow().getAmount();
            aReportId = aReportDBAdapter.getLastRow().getOpiningReportId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        double aReportDetailsForFirstCurrency=0;
        double aReportDetailsForSecondCurrency=0;
        double aReportDetailsForThirdCurrency=0;
        double aReportDetailsForForthCurrency=0;

        // get payment , cashPayment , returnList
        List<Payment> payments = paymentList(sales);
        List<CurrencyOperation>currencyOperationList=currencyOperationPaymentList(sales);
        List<CurrencyReturns> currencyReturnList = returnPaymentList(sales);
        if (SETTINGS.enableCurrencies) {
            OpiningReportDetailsDBAdapter aReportDetailsDBAdapter=new OpiningReportDetailsDBAdapter(context);
            aReportDetailsDBAdapter.open();

            aReportDetailsForFirstCurrency = aReportDetailsDBAdapter.getLastRow(CONSTANT.Shekel, aReportId);
            aReportDetailsForSecondCurrency = aReportDetailsDBAdapter.getLastRow(CONSTANT.USD, aReportId);
            aReportDetailsForThirdCurrency = aReportDetailsDBAdapter.getLastRow(CONSTANT.GBP, aReportId);
            aReportDetailsForForthCurrency = aReportDetailsDBAdapter.getLastRow(CONSTANT.EUR, aReportId);
        }
        double cash_plus = 0, cash_minus = 0;
        double check_plus = 0, check_minus = 0;
        double creditCard_plus = 0, creditCard_minus = 0;
        aReportDBAdapter.close();
        for (Payment p : payments) {
            int i = 0;
            switch (p.getPaymentWay()) {

                case CONSTANT.CASH:
                    if (p.getAmount() > 0)
                        cash_plus += p.getAmount();
                    else
                        cash_minus += p.getAmount();
                    break;
                case CONSTANT.CREDIT_CARD:
                    if (p.getAmount() > 0)
                        creditCard_plus += p.getAmount();
                    else
                        creditCard_minus += p.getAmount();
                    break;
                case CONSTANT.CHECKS:
                    if (p.getAmount() > 0)
                        check_plus += p.getAmount();
                    else
                        check_minus += p.getAmount();
                    break;
            }
        }


//with Currency
        if (SETTINGS.enableCurrencies) {

            for (CurrencyOperation cp : currencyOperationList) {
                switch (cp.getCurrency_type()) {

                    case "ILS":
                        if (cp.getAmount() > 0)
                            sheqle_plus += cp.getAmount();
                        break;
                    case "USD":
                        if (cp.getAmount() > 0)
                            usd_plus += cp.getAmount();
                        break;
                    case "EUR":
                        if (cp.getAmount() > 0)
                            eur_plus += cp.getAmount();

                        break;
                    case "GBP":
                        if (cp.getAmount() > 0)
                            gbp_plus += cp.getAmount();
                        break;
                }
            }
            for (CurrencyReturns cp : currencyReturnList) {
                switch ((int) cp.getCurrency_type()) {

                    case CONSTANT.Shekel:
                        if (cp.getAmount() > 0)
                            sheqle_minus += cp.getAmount();
                        break;
                    case CONSTANT.USD:
                        if (cp.getAmount() > 0)
                            usd_minus += cp.getAmount();
                        break;
                    case CONSTANT.EUR:
                        if (cp.getAmount() > 0)
                            eur_minus += cp.getAmount();

                        break;
                    case CONSTANT.GBP:
                        if (cp.getAmount() > 0)
                            gbp_minus += cp.getAmount();
                        break;
                }
            }

        }
        return BitmapInvoice.xPrint(context, user, date.getTime(), usd_plus+aReportDetailsForSecondCurrency, usd_minus, eur_plus+aReportDetailsForForthCurrency, eur_minus, gbp_plus+aReportDetailsForThirdCurrency, gbp_minus, sheqle_plus+aReportDetailsForFirstCurrency, sheqle_minus, cash_plus, cash_minus, check_plus, check_minus, creditCard_plus, creditCard_minus, Double.parseDouble(Util.makePrice(aReportAmount)));

    }

}
