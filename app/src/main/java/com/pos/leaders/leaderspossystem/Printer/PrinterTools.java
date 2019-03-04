package com.pos.leaders.leaderspossystem.Printer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.pos.leaders.leaderspossystem.Printer.SM_S230I.MiniPrinterFunctions;
import com.pos.leaders.leaderspossystem.Printer.SUNMI_T1.AidlUtil;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;

import HPRTAndroidSDK.HPRTPrinterHelper;
import POSAPI.POSInterfaceAPI;
import POSAPI.POSUSBAPI;
import POSSDK.POSSDK;

/**
 * Created by Win8.1 on 3/3/2019.
 */

public class PrinterTools {
    static POSSDK pos;


    public static void printAndOpenCashBox(String mainAns, final String mainMer, final String mainCli, int source, Context context, Activity c) {
        switch (SETTINGS.printer) {
            case BTP880:
                printAndOpenCashBoxBTP880(mainAns, mainMer, mainCli,context,c);
                break;
            case SUNMI_T1:
                printAndOpenCashBoxSUNMI_T1(mainAns, mainMer, mainCli,context,c);
                break;
           case HPRT_TP805:
                printAndOpenCashBoxHPRT_TP805(mainAns, mainMer, mainCli,context,c);
                break;
            case SM_S230I:
                printAndOpenCashBoxSM_S230I(mainAns, mainMer, mainCli,context,c);
                break;
        }

    }
    private static void printAndOpenCashBoxBTP880(final String mainAns, final String mainMer, final String mainCli, final Context context, final Activity activity) {
        final POSInterfaceAPI posInterfaceAPI = new POSUSBAPI(context);
        // final UsbPrinter printer = new UsbPrinter(1155, 30016);

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(context.getString(R.string.wait_for_finish_printing));

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                dialog.show();
                ////Hebrew 15 Windows-1255

                int i = posInterfaceAPI.OpenDevice();
                pos = new POSSDK(posInterfaceAPI);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                pos.systemFeedLine(2);
                pos.systemCutPaper(66, 0);
                pos.cashdrawerOpen(0, 20, 20);

                posInterfaceAPI.CloseDevice();
                dialog.cancel();
                activity.finish();

            }

            @Override
            protected Void doInBackground(Void... params) {
                InvoiceImg invoiceImg = new InvoiceImg(context);
                Log.d("payyyyy",SESSION._ORDERS.getPayment().toString());

                pos.imageStandardModeRasterPrint(invoiceImg.normalInvoice(SESSION._ORDERS.getOrderId(), SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, null), CONSTANT.PRINTER_PAGE_WIDTH);

                return null;
            }
        }.execute();
    }
    private static void printAndOpenCashBoxSUNMI_T1(String mainAns, final String mainMer, final String mainCli,Context context,Activity activity) {
        //AidlUtil.getInstance().connectPrinterService(this);
        if (AidlUtil.getInstance().isConnect()) {
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setTitle(context.getString(R.string.wait_for_finish_printing));

            dialog.show();
            InvoiceImg invoiceImg = new InvoiceImg(context);
            byte b = 0;
            try {
                    Bitmap bitmap = invoiceImg.normalInvoice(SESSION._ORDERS.getOrderId(), SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, null);
                    AidlUtil.getInstance().printBitmap(bitmap);

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
           activity.finish();
        } else {
            new android.support.v7.app.AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                    .setTitle(context.getString(R.string.printer))
                    .setMessage(context.getString(R.string.please_connect_the_printer))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                           // finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            //Toast.makeText(this, "Printer Connect Error!", Toast.LENGTH_LONG).show();
        }
    }
    private static void printAndOpenCashBoxHPRT_TP805(final String mainAns, final String mainMer, final String mainCli, final Context context, final Activity activity) {
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
                        HPRTPrinterHelper.CutPaper(HPRTPrinterHelper.HPRT_PARTIAL_CUT_FEED, 240);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                    activity.finish();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    InvoiceImg invoiceImg = new InvoiceImg(context);
                    byte b = 0;
                    try {
                            Bitmap bitmap = invoiceImg.normalInvoice(SESSION._ORDERS.getOrderId(), SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, null);
                            HPRTPrinterHelper.PrintBitmap(bitmap, b, b, 300);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
    }
    private static void printAndOpenCashBoxSM_S230I(String mainAns, final String mainMer, final String mainCli, final Context context, final Activity activity) {
        if (true) {
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setTitle(context.getString(R.string.wait_for_finish_printing));

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    dialog.show();
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    //feed paper

                    dialog.cancel();
                    activity.finish();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    InvoiceImg invoiceImg = new InvoiceImg(context);
                    byte b = 0;
                    try {
                        //// TODO: 13/06/2018 adding pinpad support

                            Bitmap bitmap = invoiceImg.normalInvoice(SESSION._ORDERS.getOrderId(), SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, null);
                            printSMS230(bitmap,context);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        }

    }
    private static void printSMS230(Bitmap bitmap, Context context) {
        String portSettings = "portable;escpos;l";
        String port = "BT:";
        int paperWidth = 576;
        paperWidth = 832; // 4inch (832 dot)
        paperWidth = 576; // 3inch (576 dot)1
        paperWidth = 384; // 2inch (384 dot)
        MiniPrinterFunctions.PrintBitmapImage(context, port, portSettings, bitmap, paperWidth, true, true);

    }
}


