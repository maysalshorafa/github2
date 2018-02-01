package com.pos.leaders.leaderspossystem.Printer;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.AReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.AReportDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyReturnsDBAdapter;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.SaleDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UserDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.AReport;

import com.pos.leaders.leaderspossystem.Models.AReportDetails;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyReturns;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.Sale;
import com.pos.leaders.leaderspossystem.Models.User;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Printer.SM_S230I.MiniPrinterFunctions;
import com.pos.leaders.leaderspossystem.Printer.SUNMI_T1.AidlUtil;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.PrinterType;
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
    private Context context;

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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dialog.cancel();
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
            Toast.makeText(context, "Printer Connect Error!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(context, "Printer Connect Error!", Toast.LENGTH_LONG).show();
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



    public Bitmap createZReport(long id, long from, long to, boolean isCopy , double totalZReportAmount ) {
        /*double aReportAmount = 0;
        long aReportId = 0;*/
        double sheqle_plus = 0, sheqle_minus = 0;
        double usd_plus = 0, usd_minus = 0;
        double eur_plus = 0, eur_minus = 0;
        double gbp_plus = 0, gbp_minus = 0;
        Log.i("CZREPO", "id:" + id + " ,from:" + from + " ,to" + to + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        SaleDBAdapter saleDBAdapter = new SaleDBAdapter(context);
        saleDBAdapter.open();
        List<Sale> sales = saleDBAdapter.getBetween(from, to);

        saleDBAdapter.close();
        ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(context);
        zReportDBAdapter.open();
        ZReport zReport = zReportDBAdapter.getByID(id);
        zReportDBAdapter.close();
        UserDBAdapter userDBAdapter = new UserDBAdapter(context);
        userDBAdapter.open();
        zReport.setUser(userDBAdapter.getUserByID(zReport.getByUser()));
        userDBAdapter.close();
        AReportDBAdapter aReportDBAdapter = new AReportDBAdapter(context);
        aReportDBAdapter.open();
        AReport aReport = aReportDBAdapter.getByLastZReport(id);
        /*try {
            aReportAmount = aReportDBAdapter.getLastRow().getAmount();
            aReportId = aReportDBAdapter.getLastRow().getId();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        double aReportDetailsForFirstCurrency=0;
        double aReportDetailsForSecondCurrency=0;
        double aReportDetailsForThirdCurrency=0;
        double aReportDetailsForForthCurrency=0;

        // get payment , cashPayment , returnList
        List<Payment> payments = paymentList(sales);
        List<CashPayment> cashPaymentList = cashPaymentList(sales);
        List<CurrencyReturns> currencyReturnList = returnPaymentList(sales);
        if (SETTINGS.enableCurrencies) {
        AReportDetailsDBAdapter aReportDetailsDBAdapter=new AReportDetailsDBAdapter(context);
        aReportDetailsDBAdapter.open();

            aReportDetailsForFirstCurrency = aReportDetailsDBAdapter.getLastRow(CONSTANT.Shekel, aReport.getId());
            aReportDetailsForSecondCurrency = aReportDetailsDBAdapter.getLastRow(CONSTANT.USD, aReport.getId());
            aReportDetailsForThirdCurrency = aReportDetailsDBAdapter.getLastRow(CONSTANT.GBP, aReport.getId());
            aReportDetailsForForthCurrency = aReportDetailsDBAdapter.getLastRow(CONSTANT.EUR, aReport.getId());
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

            for (CashPayment cp : cashPaymentList) {
                switch ((int) cp.getCurrency_type()) {

                    case CONSTANT.Shekel:
                        if (cp.getAmount() > 0)
                            sheqle_plus += cp.getAmount();
                        break;
                    case CONSTANT.USD:
                        if (cp.getAmount() > 0)
                            usd_plus += cp.getAmount();
                        break;
                    case CONSTANT.EUR:
                        if (cp.getAmount() > 0)
                            eur_plus += cp.getAmount();

                        break;
                    case CONSTANT.GBP:
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

        //endregion Currency summary

        return BitmapInvoice.zPrint(context, zReport, usd_plus+aReportDetailsForSecondCurrency, usd_minus, eur_plus+aReportDetailsForForthCurrency, eur_minus, gbp_plus+aReportDetailsForThirdCurrency, gbp_minus, sheqle_plus+aReportDetailsForFirstCurrency, sheqle_minus, cash_plus, cash_minus, check_plus, check_minus, creditCard_plus, creditCard_minus, isCopy, Double.parseDouble(Util.makePrice(aReport.getAmount())),totalZReportAmount);
        //return BitmapInvoice.zPrint(context, zReport, cash_plus, cash_minus, check_plus, check_minus, creditCard_plus, creditCard_minus, isCopy, aReport.getAmount());

    }

    // get Payment List
    public List<Payment> paymentList(List<Sale> sales) {
        List<Payment> pl = new ArrayList<Payment>();
        PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(context);
        CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(context);
        CurrencyReturnsDBAdapter currencyReturnsDBAdapter = new CurrencyReturnsDBAdapter(context);
        paymentDBAdapter.open();
        for (Sale s : sales) {
            List<Payment> payments = paymentDBAdapter.getPaymentBySaleID(s.getId());
            /**
             if (SETTINGS.enableCurrencies) {
             for (Payment _p : payments) {
             _p.setCashPayments(cashPaymentDBAdapter.getPaymentBySaleID(_p.getSaleId()));
             _p.setCurrencyReturns(currencyReturnsDBAdapter.getCurencyReturnBySaleID(_p.getSaleId()));
             }
             }**/

            pl.addAll(payments);
        }
        paymentDBAdapter.close();
        return pl;
    }

    // get Cash Payment List
    private List<CashPayment> cashPaymentList(List<Sale> sales) {
        List<CashPayment> pl = new ArrayList<CashPayment>();
        CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(context);
        cashPaymentDBAdapter.open();
        for (Sale s : sales) {
            List<CashPayment> payments = cashPaymentDBAdapter.getPaymentBySaleID(s.getId());
            pl.addAll(payments);
        }
        cashPaymentDBAdapter.close();
        return pl;
    }

    // get Currency Return  List
    private List<CurrencyReturns> returnPaymentList(List<Sale> sales) {
        List<CurrencyReturns> pl = new ArrayList<CurrencyReturns>();
        CurrencyReturnsDBAdapter currencyDBAdapter = new CurrencyReturnsDBAdapter(context);
        currencyDBAdapter.open();
        for (Sale s : sales) {
            List<CurrencyReturns> payments = currencyDBAdapter.getCurencyReturnBySaleID(s.getId());
            pl.addAll(payments);
        }
        currencyDBAdapter.close();
        return pl;
    }

    public Bitmap createXReport(long endSaleId, long id, User user, Date date) {
        long aReportId = 0;
        double usd_plus = 0, usd_minus = 0;
        double eur_plus = 0, eur_minus = 0;
        double gbp_plus = 0, gbp_minus = 0;
        double sheqle_plus = 0, sheqle_minus = 0;
        double aReportAmount = 0;
        SaleDBAdapter saleDBAdapter = new SaleDBAdapter(context);
        saleDBAdapter.open();


        List<Sale> sales = saleDBAdapter.getBetween(endSaleId, id);

        saleDBAdapter.close();

        AReportDBAdapter aReportDBAdapter = new AReportDBAdapter(context);
        aReportDBAdapter.open();
        AReport aReport = aReportDBAdapter.getByLastZReport((int) id);
        try {
            aReportAmount = aReportDBAdapter.getLastRow().getAmount();
            aReportId = aReportDBAdapter.getLastRow().getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        double aReportDetailsForFirstCurrency=0;
        double aReportDetailsForSecondCurrency=0;
        double aReportDetailsForThirdCurrency=0;
        double aReportDetailsForForthCurrency=0;

        // get payment , cashPayment , returnList
        List<Payment> payments = paymentList(sales);
        List<CashPayment> cashPaymentList = cashPaymentList(sales);
        List<CurrencyReturns> currencyReturnList = returnPaymentList(sales);
        if (SETTINGS.enableCurrencies) {
            AReportDetailsDBAdapter aReportDetailsDBAdapter=new AReportDetailsDBAdapter(context);
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

            for (CashPayment cp : cashPaymentList) {
                switch ((int) cp.getCurrency_type()) {

                    case CONSTANT.Shekel:
                        if (cp.getAmount() > 0)
                            sheqle_plus += cp.getAmount();
                        break;
                    case CONSTANT.USD:
                        if (cp.getAmount() > 0)
                            usd_plus += cp.getAmount();
                        break;
                    case CONSTANT.EUR:
                        if (cp.getAmount() > 0)
                            eur_plus += cp.getAmount();

                        break;
                    case CONSTANT.GBP:
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
