package com.pos.leaders.leaderspossystem.Printer;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DashBoard;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.AReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.SaleDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UserDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.AReport;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.Sale;
import com.pos.leaders.leaderspossystem.Models.User;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import POSAPI.POSInterfaceAPI;
import POSAPI.POSUSBAPI;
import POSSDK.POSSDK;

/**
 * Created by KARAM on 12/01/2017.
 */

public class PrintTools {
    POSSDK pos;
    private Context context;
    public PrintTools(Context context){
        this.context=context;
    }
    public void PrintReport(Bitmap _bitmap){

        final POSInterfaceAPI posInterfaceAPI=new POSUSBAPI(context);
       // final UsbPrinter printer = new UsbPrinter(1155, 30016);
        final ProgressDialog dialog=new ProgressDialog(context);
        final Bitmap bitmap=_bitmap;
        dialog.setTitle(context.getString(R.string.wait_for_finish_printing));
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                dialog.show();
                ////Hebrew 15 Windows-1255

                int i = posInterfaceAPI.OpenDevice();
                pos=new POSSDK(posInterfaceAPI);

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
                pos.systemCutPaper(66,0);

               // pos.cashdrawerOpen(0,20,20);

                posInterfaceAPI.CloseDevice();
                dialog.cancel();

            }

            @Override
            protected Void doInBackground(Void... params) {
                pos.imageStandardModeRasterPrint(bitmap,CONSTANT.PRINTER_PAGE_WIDTH);
                //printer.PRN_PrintDotBitmap(bitmap, 0);
                return null;
            }
        }.execute();
    }


    public Bitmap createZReport(long id,long from,long to,boolean isCopy){
        Log.i("CZREPO", "id:" + id+ " ,from:" + from + " ,to" + to+"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        SaleDBAdapter saleDBAdapter=new SaleDBAdapter(context);
        saleDBAdapter.open();
        List<Sale> sales=saleDBAdapter.getBetween(from,to);
        saleDBAdapter.close();
        ZReportDBAdapter zReportDBAdapter=new ZReportDBAdapter(context);
        zReportDBAdapter.open();
        ZReport zReport=zReportDBAdapter.getByID(id);
        zReportDBAdapter.close();
        UserDBAdapter userDBAdapter=new UserDBAdapter(context);
        userDBAdapter.open();
        zReport.setUser(userDBAdapter.getUserByID((int)zReport.getByUser()));
        userDBAdapter.close();

        AReportDBAdapter aReportDBAdapter = new AReportDBAdapter(context);
        aReportDBAdapter.open();
        AReport aReport = aReportDBAdapter.getByLastZReport((int)id);
        aReportDBAdapter.close();





        List<Payment> payments = paymentList(sales);
        List<CashPayment> cashPayments = cashPaymentList(sales);

        double cash_plus=0,cash_minus=0;
        double check_plus=0,check_minus=0;
        double creditCard_plus=0,creditCard_minus=0;
        double usd_plus=0,usd_minus=0;
        double eur_plus=0,eur_minus=0;
        double gbp_plus=0,gbp_minus=0;

        for (CashPayment cashPayment : cashPayments) {
            int i=0;
            switch ((int) cashPayment.getCurrency_type()){

                case 1:
                    if(cashPayment.getAmount()>0)
                        usd_plus+=cashPayment.getAmount();
                    else
                        usd_minus+=cashPayment.getAmount();
                    break;
                case 2:
                    if(cashPayment.getAmount()>0)
                        eur_plus+=cashPayment.getAmount();
                    else
                        eur_minus+=cashPayment.getAmount();
                    break;
                case 3:
                    if(cashPayment.getAmount()>0)
                        gbp_plus+=cashPayment.getAmount();
                    else
                        gbp_minus+=cashPayment.getAmount();
                    break;
            }
        }
        for (Payment p : payments) {
            int i=0;
            switch (p.getPaymentWay()){

                case CONSTANT.CASH:
                    if(p.getAmount()>0)
                        cash_plus+=p.getAmount();
                    else
                        cash_minus+=p.getAmount();
                    break;
                case CONSTANT.CREDIT_CARD:
                    if(p.getAmount()>0)
                        creditCard_plus+=p.getAmount();
                    else
                        creditCard_minus+=p.getAmount();
                    break;
                case CONSTANT.CHECKS:
                    if(p.getAmount()>0)
                        check_plus+=p.getAmount();
                    else
                        check_minus+=p.getAmount();
                    break;
            }
        }

        return BitmapInvoice.zPrint(context, zReport,0,usd_minus,eur_plus,eur_minus,gbp_plus,gbp_minus, cash_plus, cash_minus, check_plus, check_minus, creditCard_plus, creditCard_minus, isCopy, aReport.getAmount());
    }

    private List<Payment> paymentList(List<Sale> sales){
        List<Payment> pl=new ArrayList<Payment>();
        PaymentDBAdapter paymentDBAdapter=new PaymentDBAdapter(context);
        paymentDBAdapter.open();
        for (Sale s : sales) {
            pl.addAll(paymentDBAdapter.getPaymentBySaleID(s.getId()));
        }
        paymentDBAdapter.close();
        return pl;
    }
    private List<CashPayment> cashPaymentList(List<Sale> sales){
        List<CashPayment> cl=new ArrayList<CashPayment>();
        CashPaymentDBAdapter cashPaymentDBAdapter=new CashPaymentDBAdapter(context);
        cashPaymentDBAdapter.open();
        for (Sale s : sales) {
            cl.addAll(cashPaymentDBAdapter.getPaymentBySaleID(s.getId()));
        }
        cashPaymentDBAdapter.close();
        return cl;
    }






    public Bitmap createXReport(long endSaleId, long id, User user, Date date) {
        SaleDBAdapter saleDBAdapter=new SaleDBAdapter(context);
        saleDBAdapter.open();
        List<Sale> sales=saleDBAdapter.getBetween(endSaleId,id);
        saleDBAdapter.close();

        AReportDBAdapter aReportDBAdapter = new AReportDBAdapter(context);
        aReportDBAdapter.open();
        AReport aReport = new AReport();
        try {
            aReport = aReportDBAdapter.getLastRow();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        aReportDBAdapter.close();


        List<Payment> payments = paymentList(sales);
        double cash_plus=0,cash_minus=0;
        double check_plus=0,check_minus=0;
        double creditCard_plus=0,creditCard_minus=0;

        for (Payment p : payments) {
            int i=0;
            switch (p.getPaymentWay()){

                case CONSTANT.CASH:
                    if(p.getAmount()>0)
                        cash_plus+=p.getAmount();
                    else
                        cash_minus+=p.getAmount();
                    break;
                case CONSTANT.CREDIT_CARD:
                    if(p.getAmount()>0)
                        creditCard_plus+=p.getAmount();
                    else
                        creditCard_minus+=p.getAmount();
                    break;
                case CONSTANT.CHECKS:
                    if(p.getAmount()>0)
                        check_plus+=p.getAmount();
                    else
                        check_minus+=p.getAmount();
                    break;
            }
        }

        return BitmapInvoice.xPrint(context,user,date.getTime(),cash_plus,cash_minus,check_plus,check_minus,creditCard_plus,creditCard_minus,aReport.getAmount());
    }
}
