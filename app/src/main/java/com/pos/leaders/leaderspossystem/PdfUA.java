package com.pos.leaders.leaderspossystem;

/**
 * Created by Win8.1 on 3/28/2018.
 */

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ChecksDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PosInvoiceDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.XReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyOperation;
import com.pos.leaders.leaderspossystem.Models.CustomerAssistant;
import com.pos.leaders.leaderspossystem.Models.Employee;
import com.pos.leaders.leaderspossystem.Models.InvoiceStatus;
import com.pos.leaders.leaderspossystem.Models.OpiningReport;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.PosInvoice;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.ScheduleWorkers;
import com.pos.leaders.leaderspossystem.Models.XReport;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.pos.leaders.leaderspossystem.Tools.Util.currencyOperationPaymentList;
import static com.pos.leaders.leaderspossystem.Tools.Util.paymentList;

/**
 * Creates an accessible PDF with images and text.
 */

public class PdfUA {

    public static PdfPTable currencyTable = new PdfPTable(4);
    public static Employee user;
    public static ZReport zReport;
    static  int invoiceReceiptCount=0 ,invoiceCount=0 , CreditInvoiceCount=0 , ShekelCount=0 ,UsdCount=0 , EurCount=0,GbpCount=0 ,checkCount=0 , creditCardCount=0 ,receiptInvoiceAmountCheck=0 , cashCount=0,receiptInvoiceAmount=0;
    static  double cashAmount=0;
    static  int xInvoiceReceiptCount=0 ,xInvoiceCount=0 , xCreditInvoiceCount=0 , xShekelCount=0 ,xUsdCount=0 , xEurCount=0,xGbpCount=0 ,xCheckCount=0 , xCreditCardCount=0 ,xReceiptInvoiceAmountCheck=0 , xCashCount=0,xReceiptInvoiceAmount=0;
    static  double xCashAmount=0;

    static List<List<Check>> checkList = new ArrayList<>();
    static List<OpiningReport> opiningReportList=new ArrayList<>();
    static double aReportDetailsForFirstCurrency=0;
    static double aReportDetailsForSecondCurrency=0;
    static double aReportDetailsForThirdCurrency=0;
    static double aReportDetailsForForthCurrency=0;
    static double aReportAmount = 0;
    public static void  printUserReport(Context context, List<ScheduleWorkers>scheduleWorkersArrayList,long userId,Date from ,Date to) throws IOException, DocumentException {
        EmployeeDBAdapter userDBAdapter = new EmployeeDBAdapter(context);
        userDBAdapter.open();
        Employee user = userDBAdapter.getEmployeeByID(userId);
        // create file , document region
        Document document = new Document();
        String fileName = "randompdf.pdf";
        final String APPLICATION_PACKAGE_NAME = context.getPackageName();
        File path = new File( Environment.getExternalStorageDirectory(), APPLICATION_PACKAGE_NAME );
        path.mkdirs();
        File file = new File(path, fileName);
        if(file.exists()){
            PrintWriter writer = new PrintWriter(file);//to empty file each time method invoke
            writer.print("");
            writer.close();
        }

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();        //end region

        BaseFont urName = BaseFont.createFont("assets/miriam_libre_regular.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 30);
        Font dateFont = new Font(urName, 24);
        //heading table
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);
        insertCell(headingTable,  SETTINGS.companyName , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.user_name)+":" + user.getFullName() , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.cashiers) + SESSION._EMPLOYEE.getFullName(), Element.ALIGN_CENTER, 1, font);
        //end

        //date table from , to
        PdfPTable dateTable = new PdfPTable(4);
        dateTable.setRunDirection(0);
        dateTable.setWidthPercentage(108f);
        long r=0,h=0,m=0,s=0;
        for (int i=0;i<scheduleWorkersArrayList.size();i++){
            ScheduleWorkers sw = scheduleWorkersArrayList.get(i);
            if(sw.getExitTime()>0&&sw.getStartTime()>0) {
                r += DateConverter.getDateDiff(new Date(sw.getStartTime()), new Date(sw.getExitTime()), TimeUnit.MILLISECONDS);
            }
        }

        h=r/(1000*60*60);
        m=((r-(h*1000*60*60))/(1000*60));
        s=(r-(m*1000*60)-(h*1000*60*60))/(1000);
        insertCell(dateTable, context.getString(R.string.from)+":"+DateConverter.geDate(from), Element.ALIGN_LEFT, 2, dateFont);
        insertCell(dateTable, context.getString(R.string.to)+":"+DateConverter.geDate(to) , Element.ALIGN_CENTER,2, dateFont);
        insertCell(dateTable, "\n---------------------------" , Element.ALIGN_CENTER, 4, font);
        //end

        // schedule worker table
        PdfPTable table = new PdfPTable(4);
        createUserReport(context,table,scheduleWorkersArrayList);
        //end

        //total amount table
        PdfPTable totalTable = new PdfPTable(1);
        totalTable.setWidthPercentage(115f);
        insertCell(totalTable, "\n---------------------------" , Element.ALIGN_CENTER, 1, font);
        insertCell(totalTable, context.getString(R.string.total)+":" + String.format("%02d:%02d:%02d",h,m,s), Element.ALIGN_LEFT, 1, dateFont);
        //end

        //add table to document
        document.add(headingTable);
        document.add(dateTable);
        document.add(table);
        document.add(totalTable);
        document.close();
        //end :)
    }
    public static void createZReport( Context context,ZReport zReport,boolean source) throws IOException, DocumentException {
        Document document = new Document();
        String fileName = "zreport.pdf";
        final String APPLICATION_PACKAGE_NAME = context.getPackageName();
        File path = new File( Environment.getExternalStorageDirectory(), APPLICATION_PACKAGE_NAME );
        path.mkdirs();
        File file = new File(path, fileName);
        if(file.exists()){
            PrintWriter writer = new PrintWriter(file);//to empty file each time method invoke
            writer.print("");
            writer.close();
        }

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();        //end region
        //end region
        getCountForZReport(context,zReport);
        BaseFont urName = BaseFont.createFont("assets/miriam_libre_regular.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 24);
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);
        insertCell(headingTable,  SETTINGS.companyName , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);
        if(source) {
            insertCell(headingTable, context.getString(R.string.source_invoice), Element.ALIGN_CENTER, 1, font);
        }else {
            insertCell(headingTable, context.getString(R.string.copy_invoice), Element.ALIGN_CENTER, 1, font);
        }
        insertCell(headingTable, context.getString(R.string.date) +":  "+zReport.getCreatedAt(), Element.ALIGN_CENTER, 1, font);
        EmployeeDBAdapter employeeDBAdapter = new EmployeeDBAdapter(context);
        employeeDBAdapter.open();
        insertCell(headingTable, context.getString(R.string.user_name)+":  " +employeeDBAdapter.getEmployeesName( zReport.getByUser()), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.z_number) + " " +zReport.getzReportId(), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable,"----------------------------", Element.ALIGN_CENTER, 1, font);



        PdfPTable dataTable = new PdfPTable(4);
        dataTable.deleteBodyRows();
        dataTable.setRunDirection(0);
        insertCell(dataTable, context.getString(R.string.total), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.details), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable, zReport.getInvoiceReceiptAmount() + " ", Element.ALIGN_RIGHT,1, font);
        insertCell(dataTable,invoiceReceiptCount + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.invoice_receipt), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable, zReport.getInvoiceAmount() + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,invoiceCount + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.invoice), Element.ALIGN_RIGHT,2, font);

        insertCell(dataTable, zReport.getCreditInvoiceAmount() + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,creditCardCount + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.credit_invoice_doc), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable, zReport.getTotalSales() + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,  "~", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.total_sales), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        insertCell(dataTable, context.getString(R.string.total), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.currency), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable, cashAmount + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,cashCount + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.cash), Element.ALIGN_RIGHT,2, font);

        ///// shekel region
        insertCell(dataTable, Util.makePrice(zReport.getShekelAmount()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,ShekelCount+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.shekel), Element.ALIGN_RIGHT, 2, font);

        ///// usd region
        insertCell(dataTable, Util.makePrice(zReport.getUsdAmount()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,UsdCount+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.usd), Element.ALIGN_RIGHT, 2, font);

        ///// eur region
        insertCell(dataTable, Util.makePrice(zReport.getEurAmount()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,EurCount+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.eur), Element.ALIGN_RIGHT, 2, font);
        ///// Gbp region
        insertCell(dataTable, Util.makePrice(zReport.getGbpAmount()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,GbpCount+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.gbp), Element.ALIGN_RIGHT, 2, font);
        ///// CreditCard region
        insertCell(dataTable, Util.makePrice(zReport.getCreditTotal()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,creditCardCount+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.credit_card), Element.ALIGN_RIGHT, 2, font);
        insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        ///check region
        insertCell(dataTable, context.getString(R.string.total), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.currency), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable, Util.makePrice(zReport.getCheckTotal()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,checkCount+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.checks), Element.ALIGN_RIGHT, 2, font);
        if(checkList.size()>0){
            insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);
            insertCell(dataTable, context.getString(R.string.amount), Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable, context.getString(R.string.date), Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable, context.getString(R.string.checks), Element.ALIGN_RIGHT, 2, font);
            for(int i=0;i<checkList.size();i++){
                List<Check>list = checkList.get(i);

                for (int f =0;f<list.size();f++){
                    insertCell(dataTable, list.get(f).getAmount()+" ", Element.ALIGN_RIGHT, 1, font);
                    insertCell(dataTable, DateConverter.toDate(list.get(f).getCreatedAt())+" ", Element.ALIGN_RIGHT, 2, font);
                    insertCell(dataTable, list.get(f).getCheckNum()+" ", Element.ALIGN_RIGHT, 1, font);
                }
            }
            insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        }
        insertCell(dataTable, zReport.getTotalAmount()+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, "~", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.total_amount), Element.ALIGN_RIGHT, 2, font);



        ////
        PdfPTable opiningReportTable = new PdfPTable(4);
        opiningReportTable.deleteBodyRows();
        opiningReportTable.setRunDirection(0);
        insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        insertCell(opiningReportTable, context.getString(R.string.opening_report), Element.ALIGN_RIGHT, 4, font);
        insertCell(opiningReportTable, opiningReportList.size()+" " , Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable, aReportAmount +" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable, context.getString(R.string.amount), Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        PdfPTable opiningReportDetailsTable = new PdfPTable(2);
        opiningReportDetailsTable.deleteBodyRows();
        opiningReportDetailsTable.setRunDirection(0);
        if(SETTINGS.enableCurrencies) {
            insertCell(opiningReportDetailsTable, context.getString(R.string.amount), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, context.getString(R.string.type), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, aReportDetailsForFirstCurrency + " ", Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, context.getString(R.string.shekel), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, aReportDetailsForSecondCurrency + " ", Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, context.getString(R.string.usd), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, aReportDetailsForThirdCurrency + " ", Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, context.getString(R.string.gbp), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, aReportDetailsForForthCurrency + " ", Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, context.getString(R.string.eur), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, "----------------------------", Element.ALIGN_CENTER, 2, font);
        }
        PdfPTable posSalesTable = new PdfPTable(2);
        posSalesTable.deleteBodyRows();
        posSalesTable.setRunDirection(0);
        insertCell(posSalesTable, context.getString(R.string.pos_sales), Element.ALIGN_RIGHT, 1, font);
        insertCell(posSalesTable,zReport.getTotalPosSales() +" ", Element.ALIGN_RIGHT, 1, font);
        //add table to document
        document.add(headingTable);
        document.add(dataTable);
        document.add(opiningReportTable);
        if(SETTINGS.enableCurrencies){
            document.add(opiningReportDetailsTable);

        }
        document.add(posSalesTable);
        document.close();
        //end :)


    }
    public static void getCountForZReport(Context context, ZReport z) {
        aReportAmount=0;
        opiningReportList=new ArrayList<>();
        aReportDetailsForFirstCurrency=0;
        aReportDetailsForSecondCurrency=0;
        aReportDetailsForThirdCurrency=0;
        aReportDetailsForForthCurrency=0;

        OpiningReportDBAdapter opiningReportDBAdapter = new OpiningReportDBAdapter(context);
        opiningReportDBAdapter.open();
        opiningReportList = opiningReportDBAdapter.getListByLastZReport(z.getzReportId()-1);
        for (int i=0;i<opiningReportList.size();i++){
            aReportAmount+=opiningReportList.get(i).getAmount();
        }
        if (SETTINGS.enableCurrencies) {
            OpiningReportDetailsDBAdapter aReportDetailsDBAdapter=new OpiningReportDetailsDBAdapter(context);
            aReportDetailsDBAdapter.open();
            for (int a=0 ;a<opiningReportList.size();a++) {
                //aReportAmount+=opiningReportList.get(a).getAmount();
                OpiningReport opiningReport = opiningReportList.get(a);
                aReportDetailsForFirstCurrency+= aReportDetailsDBAdapter.getLastRow(CONSTANT.Shekel, opiningReport.getOpiningReportId());
                aReportDetailsForSecondCurrency+= aReportDetailsDBAdapter.getLastRow(CONSTANT.USD, opiningReport.getOpiningReportId());
                aReportDetailsForThirdCurrency+= aReportDetailsDBAdapter.getLastRow(CONSTANT.GBP, opiningReport.getOpiningReportId());
                aReportDetailsForForthCurrency+= aReportDetailsDBAdapter.getLastRow(CONSTANT.EUR, opiningReport.getOpiningReportId());
            }

        }
        checkList=new ArrayList<>();
        cashAmount=0;
        invoiceReceiptCount=0 ;invoiceCount=0; CreditInvoiceCount=0 ; ShekelCount=0 ;UsdCount=0 ;EurCount=0; GbpCount=0 ;checkCount=0 ; creditCardCount=0 ;receiptInvoiceAmountCheck=0 ; cashCount=0;receiptInvoiceAmount=0;
        OrderDBAdapter orderDb = new OrderDBAdapter(context);
        orderDb.open();
        ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(context);
        zReportDBAdapter.open();
        invoiceReceiptCount = orderDb.getBetween(z.getStartOrderId(),z.getEndOrderId()).size();
        if(zReportDBAdapter.getProfilesCount()==0){
            PosInvoiceDBAdapter posInvoiceDBAdapter =new PosInvoiceDBAdapter(context);
            posInvoiceDBAdapter.open();
            List<PosInvoice>posInvoiceList = posInvoiceDBAdapter.getPosInvoiceList(-1, InvoiceStatus.UNPAID.getValue());
            invoiceCount+=posInvoiceList.size();

            List<PosInvoice>posCreditInvoiceList = posInvoiceDBAdapter.getPosInvoiceListByType(-1, DocumentType.CREDIT_INVOICE.getValue(),CONSTANT.CASH);
            CreditInvoiceCount+=posCreditInvoiceList.size();

            List<PosInvoice>posReceiptList = posInvoiceDBAdapter.getPosInvoiceListByType(-1, DocumentType.RECEIPT.getValue(),CONSTANT.CHECKS);
            receiptInvoiceAmountCheck+=posReceiptList.size();

        }else {
            ZReport zReport1=null;
            try {
                zReport1 = zReportDBAdapter.getLastRow();
            } catch (Exception e) {
                e.printStackTrace();
            }
            PosInvoiceDBAdapter posInvoiceDBAdapter =new PosInvoiceDBAdapter(context);
            posInvoiceDBAdapter.open();
            List<PosInvoice>posInvoiceList = posInvoiceDBAdapter.getPosInvoiceList(zReport1.getzReportId(), InvoiceStatus.UNPAID.getValue());
            invoiceCount+=posInvoiceList.size();

            List<PosInvoice>posCreditInvoiceList = posInvoiceDBAdapter.getPosInvoiceListByType(zReport1.getzReportId(), DocumentType.CREDIT_INVOICE.getValue(),CONSTANT.CASH);
            CreditInvoiceCount+=posCreditInvoiceList.size();

            List<PosInvoice>posReceiptListCheck = posInvoiceDBAdapter.getPosInvoiceListByType(zReport1.getzReportId(), DocumentType.RECEIPT.getValue(),CONSTANT.CHECKS);
            receiptInvoiceAmountCheck+=posReceiptListCheck.size();
        }
        List<Long>orderIds= new ArrayList<>();
        List<Payment> payments = paymentList(orderDb.getBetween(z.getStartOrderId(),z.getEndOrderId()),context);
        for (Payment p : payments) {
            int i = 0;
            switch (p.getPaymentWay()) {

                case CONSTANT.CASH:
                    cashCount+=1;
                    cashAmount+=p.getAmount();
                    break;
                case CONSTANT.CREDIT_CARD:
                    creditCardCount+=1;
                    break;
                case CONSTANT.CHECKS:
                    checkCount+=1;
                    orderIds.add(p.getOrderId());
                    break;
            }
        }
        if(orderIds.size()>0){
            for (int id = 0;id<orderIds.size();id++){
                ChecksDBAdapter checkDb= new ChecksDBAdapter(context);
                checkDb.open();
                List<Check> c = checkDb.getPaymentBySaleID(orderIds.get(id));
                checkList.add(c);
            }
        }
        //with Currency
        if (SETTINGS.enableCurrencies) {
            List<CurrencyOperation>currencyOperationList=currencyOperationPaymentList(orderDb.getBetween(z.getStartOrderId(),z.getEndOrderId()),context);
            for (CurrencyOperation cp : currencyOperationList) {
                switch (cp.getCurrency_type()) {
                    case "ILS":
                        ShekelCount+=1;
                        break;
                    case "USD":
                        UsdCount+=1;
                        break;
                    case "EUR":
                        EurCount+=1;

                        break;
                    case "GBP":
                        GbpCount+=1;
                        break;
                }
            }

        }
        if(zReportDBAdapter.getProfilesCount()==0){
            PosInvoiceDBAdapter posInvoiceDBAdapter =new PosInvoiceDBAdapter(context);
            posInvoiceDBAdapter.open();
            List<PosInvoice>posReceiptList = posInvoiceDBAdapter.getPosInvoiceListByType(-1, DocumentType.RECEIPT.getValue(),CONSTANT.CASH);
            receiptInvoiceAmount+=posReceiptList.size();

        }else {
            ZReport zReport1=null;
            try {
                zReport1 = zReportDBAdapter.getLastRow();
            } catch (Exception e) {
                e.printStackTrace();
            }
            PosInvoiceDBAdapter posInvoiceDBAdapter =new PosInvoiceDBAdapter(context);
            posInvoiceDBAdapter.open();
            List<PosInvoice>posReceiptList = posInvoiceDBAdapter.getPosInvoiceListByType(zReport1.getzReportId(), DocumentType.RECEIPT.getValue(),CONSTANT.CASH);
            receiptInvoiceAmount+=posReceiptList.size();

        }
        ShekelCount+=receiptInvoiceAmount;
        checkCount+=receiptInvoiceAmountCheck;

    }
    public static void createUserReport(Context context ,PdfPTable table , List<ScheduleWorkers>scheduleWorkersList) throws IOException, DocumentException {
        Date date , startAt=null , endAt =null;
        table.setRunDirection(0);
        table.setWidthPercentage(118f);
        table.setWidths(new int[]{1, 1, 1,2});
        BaseFont urName = BaseFont.createFont("assets/carmelitregular.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font urFontName = new Font(urName, 24);
        BaseFont urName1 = BaseFont.createFont("assets/miriam_libre_bold.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font urFontName1 = new Font(urName1, 22);

        //insert column headings;
        insertCell(table, context.getString(R.string.date), Element.ALIGN_CENTER, 1, urFontName1);
        insertCell(table, context.getString(R.string.start_at), Element.ALIGN_CENTER, 1, urFontName1);
        insertCell(table, context.getString(R.string.end_at), Element.ALIGN_CENTER, 1, urFontName1);
        insertCell(table, context.getString(R.string.total), Element.ALIGN_CENTER, 1, urFontName1);
        for (int i=0;i<scheduleWorkersList.size();i++){
            date= new Date(scheduleWorkersList.get(i).getDate());
            insertCell(table, "  " +DateConverter.geDate(date), Element.ALIGN_CENTER, 1, urFontName); // insert date value

            if(scheduleWorkersList.get(i).getStartTime()>0){
                startAt= new Date(scheduleWorkersList.get(i).getStartTime());
                insertCell(table, DateConverter.getTime(startAt), Element.ALIGN_CENTER, 1, urFontName);
            }else {
                insertCell(table, "", Element.ALIGN_CENTER, 1, urFontName);

            }
            if(scheduleWorkersList.get(i).getExitTime()>0){
                endAt= new Date(scheduleWorkersList.get(i).getExitTime());
                insertCell(table, DateConverter.getTime(endAt), Element.ALIGN_CENTER, 1, urFontName);
            }else {
                insertCell(table, "", Element.ALIGN_CENTER, 1, urFontName);

            }
            if(scheduleWorkersList.get(i).getStartTime()>0 && scheduleWorkersList.get(i).getExitTime()>0){
                long h, m, s, ms, d;
                d = DateConverter.getDateDiff(startAt, endAt, TimeUnit.MILLISECONDS);
                h = DateConverter.getDateDiff(startAt, endAt, TimeUnit.MILLISECONDS);
                m = DateConverter.getDateDiff(startAt, endAt, TimeUnit.MILLISECONDS);
                s = DateConverter.getDateDiff(startAt, endAt, TimeUnit.MILLISECONDS);
                ms = DateConverter.getDateDiff(startAt, endAt, TimeUnit.MILLISECONDS);
                d=d/(1000*60*60*24);
                h=h/(1000*60*60);
                m=((m-(h*1000*60*60))/(1000*60));
                s=(s-(m*1000*60)-(h*1000*60*60))/(1000);
                insertCell(table, String.format("%02d:%02d:%02d", h, m, s), Element.ALIGN_CENTER, 1, urFontName);

            }else {
                insertCell(table, String.format(""), Element.ALIGN_CENTER, 1, urFontName);

            }

        }


    }
    public static void insertCell(PdfPTable table, String text, int align, int colspan, Font font){
        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        cell.setBorder(Rectangle.NO_BORDER);

        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(14f);
        }
        //add the call to the table
        table.addCell(cell);

    }
    public static void  printCustomerWalletReport(Context context, String res) throws IOException, DocumentException, JSONException {
        JSONObject jsonObject = new JSONObject(res);
        String documentsData = jsonObject.getString("documentsData");
        JSONObject customerJson = new JSONObject(documentsData);
        JSONObject customerInfo = new JSONObject(customerJson.getJSONObject("customer").toString());

        // create file , document region
        Document document = new Document();
        String fileName = "customerwallet.pdf";
        final String APPLICATION_PACKAGE_NAME = context.getPackageName();
        File path = new File( Environment.getExternalStorageDirectory(), APPLICATION_PACKAGE_NAME );
        path.mkdirs();
        File file = new File(path, fileName);
        if(file.exists()){
            PrintWriter writer = new PrintWriter(file);//to empty file each time method invoke
            writer.print("");
            writer.close();
        }

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();        //end region
        //end region

        BaseFont urName = BaseFont.createFont("assets/miriam_libre_regular.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 30);
        Font dateFont = new Font(urName, 24);
        //heading table
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);
        insertCell(headingTable,  SETTINGS.companyName , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.cashiers) + SESSION._EMPLOYEE.getFullName(), Element.ALIGN_CENTER, 1, font);
//        insertCell(headingTable, context.getString(R.string.date) + invoiceJsonObject.getString("date"), Element.ALIGN_CENTER, 1, font);

        //end

        //date table from , to
        PdfPTable dateTable = new PdfPTable(2);
        dateTable.setRunDirection(0);
        dateTable.setWidthPercentage(108f);

        insertCell(dateTable, context.getString(R.string.customer_name)+":"+customerInfo.getString("firstName")+customerInfo.getString("lastName"), Element.ALIGN_LEFT, 2, dateFont);
        insertCell(dateTable, "Invoices Numbers"+":"+jsonObject.getString("docNum"), Element.ALIGN_LEFT, 2, dateFont);
        insertCell(dateTable, context.getString(R.string.total_paid)+":"+customerJson.getDouble("paidAmount"), Element.ALIGN_LEFT, 2, dateFont);

        insertCell(dateTable, "\n---------------------------" , Element.ALIGN_CENTER, 4, font);
        //end

        // schedule worker table
        //end

        //add table to document
        document.add(headingTable);
        document.add(dateTable);
        document.close();
        //end :)
    }
    public static void  printReceiptReport(Context context, String res) throws IOException, DocumentException, JSONException {
        Log.d("rrrr",res);
        JSONObject jsonObject = new JSONObject(res);
        String documentsData = jsonObject.getString("documentsData");
        JSONObject customerJson = new JSONObject(documentsData);
        JSONArray refNumber = customerJson.getJSONArray("invoicesNumbers");
        JSONArray payment = customerJson.getJSONArray("payments");
        JSONObject customerInfo = new JSONObject(customerJson.getJSONObject("customer").toString());

        // create file , document region
        Document document = new Document();
        String fileName = "receipt.pdf";
        final String APPLICATION_PACKAGE_NAME = context.getPackageName();
        File path = new File( Environment.getExternalStorageDirectory(), APPLICATION_PACKAGE_NAME );
        path.mkdirs();
        File file = new File(path, fileName);
        if(file.exists()){
            PrintWriter writer = new PrintWriter(file);//to empty file each time method invoke
            writer.print("");
            writer.close();
        }

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();        //end region
        //end region

        BaseFont urName = BaseFont.createFont("assets/miriam_libre_regular.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 30);
        Font dateFont = new Font(urName, 24);
        //heading table
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);
        insertCell(headingTable,  SETTINGS.companyName , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "\n---------------------------" , Element.ALIGN_CENTER, 1, font);

//        insertCell(headingTable, context.getString(R.string.date) + invoiceJsonObject.getString("date"), Element.ALIGN_CENTER, 1, font);

        //end

        //date table from , to
        PdfPTable dateTable = new PdfPTable(2);
        dateTable.setRunDirection(0);
        dateTable.setWidthPercentage(108f);

        insertCell(dateTable, context.getString(R.string.receipt_numbers)+":"+jsonObject.getString("docNum"), Element.ALIGN_LEFT, 3, dateFont);
        insertCell(dateTable, "\n---------------------------" , Element.ALIGN_CENTER, 4, font);

        insertCell(dateTable, context.getString(R.string.customer_name)+":"+customerInfo.getString("firstName")+customerInfo.getString("lastName"), Element.ALIGN_LEFT, 2, dateFont);

        //end
        insertCell(dateTable, "\n---------------------------" , Element.ALIGN_CENTER, 4, font);

        PdfPTable orderDetailsTable = new PdfPTable(3);
        orderDetailsTable.setRunDirection(0);
        orderDetailsTable.setWidthPercentage(108f);
        Log.d("customerJson",customerJson.toString());
        if (payment.getJSONObject(0).getString("paymentWay").equals("checks")) {
            insertCell(orderDetailsTable, context.getString(R.string.payment)+": "+context.getString(R.string.checks), Element.ALIGN_LEFT, 3, dateFont);
            insertCell(orderDetailsTable, context.getString(R.string.total_paid)+": "+customerJson.getDouble("paidAmount"), Element.ALIGN_LEFT, 3, dateFont);
        }else {
            insertCell(orderDetailsTable, context.getString(R.string.payment)+": "+context.getString(R.string.cash), Element.ALIGN_LEFT, 3, dateFont);
            insertCell(orderDetailsTable, context.getString(R.string.total_paid) + ": " + payment.getJSONObject(0).getDouble("amount"), Element.ALIGN_LEFT, 3, dateFont);
        }
        insertCell(orderDetailsTable, "\n---------------------------" , Element.ALIGN_CENTER, 3, font);
        insertCell(orderDetailsTable, context.getString(R.string.cashiers) + SESSION._EMPLOYEE.getFullName(), Element.ALIGN_CENTER, 3, font);
        insertCell(orderDetailsTable, context.getString(R.string.date)+":"+DateConverter.stringToDate(customerJson.getString("date")), Element.ALIGN_LEFT, 3, dateFont);
        insertCell(dateTable, context.getString(R.string.reference_invoice)+":"+refNumber.get(0), Element.ALIGN_LEFT, 3, dateFont);
        insertCell(dateTable, context.getString(R.string.customer_ledger)+":"+customerJson.getString("customerGeneralLedger"), Element.ALIGN_LEFT, 3, dateFont);

        insertCell(orderDetailsTable, "\n---------------------------" , Element.ALIGN_CENTER, 3, font);

        if (payment.getJSONObject(0).getString("paymentWay").equals("checks")) {
            insertCell(orderDetailsTable, context.getString(R.string.amount), Element.ALIGN_LEFT, 1, dateFont);
            insertCell(orderDetailsTable, context.getString(R.string.date), Element.ALIGN_LEFT, 1, dateFont);
            insertCell(orderDetailsTable, context.getString(R.string.checks), Element.ALIGN_LEFT, 1, dateFont);
            JSONArray paymentDetails= payment.getJSONObject(0).getJSONArray("paymentDetails");
            for(int i =0 ; i<paymentDetails.length();i++){
                JSONObject jsonObject1 = paymentDetails.getJSONObject(i);
                insertCell(orderDetailsTable, jsonObject1.getDouble("amount")+"", Element.ALIGN_LEFT, 1, dateFont);
                insertCell(orderDetailsTable, DateConverter.toDate(new Date(jsonObject1.getLong("createdAt"))), Element.ALIGN_LEFT, 1, dateFont);
                insertCell(orderDetailsTable, jsonObject1.getInt("checkNum")+"", Element.ALIGN_LEFT, 1, dateFont);
            }
            //end

            //add table to document


            //end :)
        }
        document.add(headingTable);
        document.add(dateTable);
        document.add(orderDetailsTable);
        document.close();
    }
    public static void  printClosingReport(Context context, String res) throws IOException, DocumentException, JSONException {
        JSONObject jsonObject = new JSONObject(res);
        // create file , document region
        Document document = new Document();
        String fileName = "closingreport.pdf";
        final String APPLICATION_PACKAGE_NAME = context.getPackageName();
        File path = new File( Environment.getExternalStorageDirectory(), APPLICATION_PACKAGE_NAME );
        path.mkdirs();
        File file = new File(path, fileName);
        if(file.exists()){
            PrintWriter writer = new PrintWriter(file);//to empty file each time method invoke
            writer.print("");
            writer.close();
        }

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();        //end region
        //end region

        BaseFont urName = BaseFont.createFont("assets/miriam_libre_regular.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 30);
        Font dateFont = new Font(urName, 24);
        //heading table
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);
        insertCell(headingTable,  SETTINGS.companyName , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.cashiers) + SESSION._EMPLOYEE.getFullName(), Element.ALIGN_CENTER, 1, font);

        //end

        //date table from , to
        PdfPTable dateTable = new PdfPTable(4);
        dateTable.setRunDirection(0);
        dateTable.setWidthPercentage(108f);
        insertCell(dateTable, context.getString(R.string.method), Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, context.getString(R.string.actual), Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, context.getString(R.string.expected), Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, context.getString(R.string.different), Element.ALIGN_LEFT, 1, dateFont);

        insertCell(dateTable, CONSTANT.CHECKS, Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, jsonObject.getDouble("actualCheck")+"", Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, jsonObject.getDouble("expectedCheck")+"", Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable,  jsonObject.getDouble("actualCheck")-jsonObject.getDouble("expectedCheck")+"", Element.ALIGN_LEFT, 1, dateFont);

        insertCell(dateTable, CONSTANT.CREDIT_CARD, Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, jsonObject.getDouble("actualCredit")+"", Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, jsonObject.getDouble("expectedCredit")+"", Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable,  jsonObject.getDouble("actualCredit")-jsonObject.getDouble("expectedCredit")+"", Element.ALIGN_LEFT, 1, dateFont);

        insertCell(dateTable, "Shekel", Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, jsonObject.getDouble("actualShekel")+"", Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, jsonObject.getDouble("expectedShekel")+"", Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable,  jsonObject.getDouble("actualShekel")-jsonObject.getDouble("expectedShekel")+"", Element.ALIGN_LEFT, 1, dateFont);

        insertCell(dateTable, "USD", Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, jsonObject.getDouble("actualUsd")+"", Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, jsonObject.getDouble("expectedUsd")+"", Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable,  jsonObject.getDouble("actualUsd")-jsonObject.getDouble("expectedUsd")+"", Element.ALIGN_LEFT, 1, dateFont);

        insertCell(dateTable, "EUR", Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, jsonObject.getDouble("actualEur")+"", Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, jsonObject.getDouble("expectedEur")+"", Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable,  jsonObject.getDouble("actualEur")-jsonObject.getDouble("expectedEur")+"", Element.ALIGN_LEFT, 1, dateFont);

        insertCell(dateTable, "GBP", Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, jsonObject.getDouble("actualGbp")+"", Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, jsonObject.getDouble("expectedGbp")+"", Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable,  jsonObject.getDouble("actualGbp")-jsonObject.getDouble("expectedGbp")+"", Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, "\n---------------------------" , Element.ALIGN_CENTER, 4, font);

        //end

        // schedule worker table
        //end

        //add table to document
        document.add(headingTable);
        document.add(dateTable);
        document.close();
        //end :)
    }
    public static void  printCreditInvoiceReport(Context context, String res,String source) throws IOException, DocumentException, JSONException {
        ProductDBAdapter productDBAdapter =new ProductDBAdapter(context);
        productDBAdapter.open();
        JSONObject jsonObject = new JSONObject(res);
        JSONObject customerJson = jsonObject.getJSONObject("documentsData");;
        JSONObject customerInfo = new JSONObject(customerJson.getJSONObject("customer").toString());
        JSONObject userInfo = new JSONObject(customerJson.getJSONObject("user").toString());

        // create file , document region
        Document document = new Document();
        String fileName = "creditinvoice.pdf";
        final String APPLICATION_PACKAGE_NAME = context.getPackageName();
        File path = new File( Environment.getExternalStorageDirectory(), APPLICATION_PACKAGE_NAME );
        path.mkdirs();
        File file = new File(path, fileName);
        if(file.exists()){
            PrintWriter writer = new PrintWriter(file);//to empty file each time method invoke
            writer.print("");
            writer.close();
        }

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();        //end region
        //end region

        BaseFont urName = BaseFont.createFont("assets/miriam_libre_regular.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 30);
        Font dateFont = new Font(urName, 24);
        //heading table
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);
        EmployeeDBAdapter employeeDBAdapter =new EmployeeDBAdapter(context);
        employeeDBAdapter.open();
        Employee employee = employeeDBAdapter.getEmployeeByID(userInfo.getLong("employeeId"));
        insertCell(headingTable,  SETTINGS.companyName , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);
        if(employee!=null) {
            insertCell(headingTable, context.getString(R.string.cashiers) + employee.getFullName(), Element.ALIGN_CENTER, 1, font);
        }
        insertCell(headingTable, context.getString(R.string.customer_name)+":"+customerInfo.getString("firstName")+customerInfo.getString("lastName"), Element.ALIGN_LEFT, 1, dateFont);
        insertCell(headingTable,context.getString(R.string.reference_invoice)+" "+" "+customerJson.getString("reference"),Element.ALIGN_LEFT,1,dateFont);
        insertCell(headingTable, "\n---------------------------" , Element.ALIGN_CENTER, 1, font);


        if(source=="source"){
            insertCell(headingTable,  context.getString(R.string.source_invoice) , Element.ALIGN_CENTER, 1, font);
            insertCell(headingTable, "\n---------------------------" , Element.ALIGN_CENTER, 1, font);

        }else {
            insertCell(headingTable,  context.getString(R.string.copy_invoice) , Element.ALIGN_CENTER, 1, font);
            insertCell(headingTable, "\n---------------------------" , Element.ALIGN_CENTER, 1, font);

        }
        insertCell(headingTable,  context.getString(R.string.credit_invoice_doc) +" : "+jsonObject.getString("docNum") , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "\n---------------------------" , Element.ALIGN_CENTER, 1, font);
        PdfPTable orderDetailsTable = new PdfPTable(5);
        orderDetailsTable.setRunDirection(0);
        orderDetailsTable.setWidthPercentage(108f);
        Log.d("customerJson",customerJson.toString());

        JSONArray itemJson = customerJson.getJSONArray("cartDetailsList");
        insertCell(orderDetailsTable, context.getString(R.string.product), Element.ALIGN_LEFT, 1, dateFont);
        insertCell(orderDetailsTable,context.getString(R.string.qty), Element.ALIGN_LEFT, 1, dateFont);
        insertCell(orderDetailsTable, context.getString(R.string.price), Element.ALIGN_LEFT, 1, dateFont);
        insertCell(orderDetailsTable, context.getString(R.string.total), Element.ALIGN_LEFT, 1, dateFont);
        insertCell(orderDetailsTable, context.getString(R.string.discount), Element.ALIGN_LEFT, 1, dateFont);
        int q = 0;
        for (int a = 0 ; a<itemJson.length();a++) {
            JSONObject jsonObject1 = itemJson.getJSONObject(a);
            String sku = jsonObject1.getString("sku");
            Product product= productDBAdapter.getProductByBarCode(sku);
            if(product==null){
                insertCell(orderDetailsTable, context.getString(R.string.general_product), Element.ALIGN_LEFT, 1, dateFont);
            }else {
                insertCell(orderDetailsTable, product.getDisplayName(), Element.ALIGN_LEFT, 1, dateFont);
            }
            insertCell(orderDetailsTable, ""+jsonObject1.getInt("quantity"), Element.ALIGN_LEFT, 1, dateFont);
            q+=jsonObject1.getInt("quantity");
            insertCell(orderDetailsTable,Util.makePrice(jsonObject1.getDouble("unitPrice")), Element.ALIGN_LEFT, 1, dateFont);
            insertCell(orderDetailsTable, Util.makePrice(jsonObject1.getDouble("unitPrice")*jsonObject1.getInt("quantity")), Element.ALIGN_LEFT, 1, dateFont);
            insertCell(orderDetailsTable, Util.makePrice(jsonObject1.getDouble("discount"))+"%", Element.ALIGN_LEFT, 1, dateFont);

        }
        insertCell(orderDetailsTable, "\n---------------------------" , Element.ALIGN_CENTER, 5, font);

        insertCell(orderDetailsTable, context.getString(R.string.product_quantity)+" : "+q , Element.ALIGN_CENTER, 1, dateFont);
        if(customerJson.getDouble("cartDiscount")>0){
            insertCell(orderDetailsTable, context.getString(R.string.cart_discount)+" "+customerJson.getDouble("cartDiscount") , Element.ALIGN_CENTER, 1, dateFont);
        }
        insertCell(orderDetailsTable, context.getString(R.string.total_price)+" : "+customerJson.getDouble("total") , Element.ALIGN_CENTER,1, dateFont);
        insertCell(orderDetailsTable, context.getString(R.string.customer_ledger)+":"+customerJson.getString("customerGeneralLedger"), Element.ALIGN_LEFT, 5, dateFont);
        insertCell(orderDetailsTable, "\n---------------------------" , Element.ALIGN_CENTER, 5, font);
        insertCell(orderDetailsTable, "Date"+":"+DateConverter.stringToDate(customerJson.getString("date")), Element.ALIGN_LEFT, 5, dateFont);
        insertCell(orderDetailsTable, context.getString(R.string.total_paid)+":"+customerJson.getDouble("total"), Element.ALIGN_LEFT, 5, dateFont);

        //end

        //add table to document
        document.add(headingTable);
        document.add(orderDetailsTable);
        document.close();
        //end :)
    }
    public static void  printOpiningReport(Context context, OpiningReport opiningReport, final ArrayList<String> currencyTypeList, final ArrayList<Double>currencyAmount) throws IOException, DocumentException, JSONException {
        // create file , document region
        Document document = new Document();
        String fileName = "opiningreport.pdf";
        final String APPLICATION_PACKAGE_NAME = context.getPackageName();
        File path = new File( Environment.getExternalStorageDirectory(), APPLICATION_PACKAGE_NAME );
        path.mkdirs();
        File file = new File(path, fileName);
        if(file.exists()){
            PrintWriter writer = new PrintWriter(file);//to empty file each time method invoke
            writer.print("");
            writer.close();
        }

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();        //end region
        //end region

        BaseFont urName = BaseFont.createFont("assets/miriam_libre_regular.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 30);
        Font dateFont = new Font(urName, 24);
        //heading table
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);

        PdfPTable dataTable = new PdfPTable(2);
        dataTable.deleteBodyRows();
        dataTable.setRunDirection(0);

        insertCell(headingTable,  SETTINGS.companyName , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.cashiers) + SESSION._EMPLOYEE.getFullName(), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.opening_report) , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.amount) +":"+ opiningReport.getAmount()+"Shekel", Element.ALIGN_CENTER, 1, font);

        insertCell(headingTable, "\n---------------------------" , Element.ALIGN_CENTER, 4, font);
        OpiningReportDetailsDBAdapter opiningReportDetailsDBAdapter = new OpiningReportDetailsDBAdapter(context);
        opiningReportDetailsDBAdapter.open();
        if(currencyAmount.size()>0) {
            for (int i = 0; i < currencyAmount.size(); i++) {
                if(currencyAmount.get(i)>0) {
                    insertCell(dataTable, context.getString(R.string.opening_report) + " : " + currencyAmount.get(i) + "  " + currencyTypeList.get(i), Element.ALIGN_LEFT, 2, dateFont);
                }
            }}else {
            insertCell(dataTable, context.getString(R.string.opening_report) + " : "+  opiningReport.getAmount()+"  "+"Shekel", Element.ALIGN_LEFT, 2, dateFont);

        }

        //add table to document
        document.add(headingTable);
        document.add(dataTable);

        document.close();

        //en


    }
    public static void  printLogInLogOutUserReport(Context context, JSONObject jsonObject) throws IOException, DocumentException, JSONException {
        // create file , document region
        Document document = new Document();
        String fileName = "loginreport.pdf";
        final String APPLICATION_PACKAGE_NAME = context.getPackageName();
        File path = new File( Environment.getExternalStorageDirectory(), APPLICATION_PACKAGE_NAME );
        path.mkdirs();
        File file = new File(path, fileName);
        if(file.exists()){
            PrintWriter writer = new PrintWriter(file);//to empty file each time method invoke
            writer.print("");
            writer.close();
        }

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();        //end region
        //end region

        BaseFont urName = BaseFont.createFont("assets/miriam_libre_regular.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 30);
        Font dateFont = new Font(urName, 24);
        //heading table
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);

        PdfPTable dataTable = new PdfPTable(2);
        dataTable.deleteBodyRows();
        dataTable.setRunDirection(0);

        insertCell(headingTable,  SETTINGS.companyName , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.schedule_workers) , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.date) +":  "+new Timestamp(System.currentTimeMillis()), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.user_name)+":  " + jsonObject.get("user_name"), Element.ALIGN_CENTER, 1, font);
        if(jsonObject.get("case").equals("logIn")) {
            insertCell(headingTable, context.getString(R.string.log_in), Element.ALIGN_CENTER, 1, font);
        }else  if(jsonObject.get("case").equals("logOut")){
            insertCell(headingTable, context.getString(R.string.log_out), Element.ALIGN_CENTER, 1, font);
            insertCell(headingTable, context.getString(R.string.hours_of_work)+" "+jsonObject.get("hours_of_work"), Element.ALIGN_CENTER, 1, font);
        }
        insertCell(headingTable, "\n---------------------------" , Element.ALIGN_CENTER, 4, font);
        Log.d("Uuu",jsonObject.toString());

        //add table to document
        document.add(headingTable);
        document.close();

        //en


    }
    public static void createSalesManReport(Context context  , List<CustomerAssistant>customerAssistantList) throws IOException, DocumentException {
        Document document = new Document();
        String fileName = "salesmanreport.pdf";
        final String APPLICATION_PACKAGE_NAME = context.getPackageName();
        File path = new File( Environment.getExternalStorageDirectory(), APPLICATION_PACKAGE_NAME );
        path.mkdirs();
        File file = new File(path, fileName);
        if(file.exists()){
            PrintWriter writer = new PrintWriter(file);//to empty file each time method invoke
            writer.print("");
            writer.close();
        }

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();        //end region
        //end region

        BaseFont urName = BaseFont.createFont("assets/miriam_libre_regular.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 30);
        //heading table
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);
        insertCell(headingTable,  SETTINGS.companyName , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.date) +":  "+new Timestamp(System.currentTimeMillis()), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.cashiers) + SESSION._EMPLOYEE.getFullName(), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.sales_man_report), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "\n\n\n---------------------------" , Element.ALIGN_CENTER, 4, font);
        Font urFontName = new Font(urName, 22);
        PdfPTable table = new PdfPTable(new float[] { 2,2, 2});
        table.deleteBodyRows();
        table.setRunDirection(0);
        //insert column headings;
        insertCellSalesMan(table, context.getString(R.string.type), Element.ALIGN_CENTER, 1, urFontName);
        insertCellSalesMan(table, context.getString(R.string.amount), Element.ALIGN_CENTER, 1, urFontName);
        insertCellSalesMan(table, context.getString(R.string.date), Element.ALIGN_CENTER, 1, urFontName);
        for (int i=0;i<customerAssistantList.size();i++){
            if(customerAssistantList.get(i).getSalesCase().equals("ORDER_DETAILS")) {
                insertCellSalesMan(table, context.getString(R.string.order_details), Element.ALIGN_CENTER, 1, urFontName); // insert date value
            }else {
                insertCellSalesMan(table, context.getString(R.string.order), Element.ALIGN_CENTER, 1, urFontName); // insert date value
            }
            insertCellSalesMan(table,Util.makePrice(customerAssistantList.get(i).getAmount()), Element.ALIGN_CENTER, 1, urFontName); // insert date value
            insertCellSalesMan(table, DateConverter.toDate(customerAssistantList.get(i).getCreatedAt()), Element.ALIGN_CENTER, 1, urFontName); // insert date value

        }
        //add table to document
        document.add(headingTable);
      document.add(table);
        document.close();
    }

    public static void createPauseInvoice(Context context  , List<OrderDetails>orderDetailsList) throws IOException, DocumentException {
        double totalPrice =0.0;
        int count=0;
        // create file , document region
        Document document = new Document();
        String fileName = "pauseInvoice.pdf";
        final String APPLICATION_PACKAGE_NAME = context.getPackageName();
        File path = new File( Environment.getExternalStorageDirectory(), APPLICATION_PACKAGE_NAME );
        path.mkdirs();
        File file = new File(path, fileName);
        if(file.exists()){
            PrintWriter writer = new PrintWriter(file);//to empty file each time method invoke
            writer.print("");
            writer.close();
        }

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();        //end region
        //end region
        Date date;
        BaseFont urName = BaseFont.createFont("assets/miriam_libre_bold.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 30);
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);
        insertCell(headingTable,  SETTINGS.companyName , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.date) +":  "+new Timestamp(System.currentTimeMillis()), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.user_name)+":  " + SESSION._EMPLOYEE.getFirstName(), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.pause_invoice), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "\n\n\n---------------------------" , Element.ALIGN_CENTER, 4, font);

        BaseFont urName1 = BaseFont.createFont("assets/miriam_libre_regular.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font urFontName1 = new Font(urName1, 22);
        PdfPTable table = new PdfPTable(7);
        Font urFontName = new Font(urName1, 24);

        table.deleteBodyRows();
        table.setRunDirection(0);
        //insert column headings;
        insertCell(table, "%" , Element.ALIGN_CENTER, 1, urFontName1);
        insertCell(table, context.getString(R.string.total), Element.ALIGN_CENTER, 1, urFontName1);
        insertCell(table, context.getString(R.string.price), Element.ALIGN_CENTER, 1, urFontName1);
        insertCell(table, context.getString(R.string.qty), Element.ALIGN_CENTER, 1, urFontName1);
        insertCell(table, context.getString(R.string.product), Element.ALIGN_CENTER, 3, urFontName1);

        for (int i=0;i<orderDetailsList.size();i++){
            insertCell(table, "  " + orderDetailsList.get(i).getDiscount(), Element.ALIGN_CENTER, 1, urFontName); // insert date value
            insertCell(table, "  " + orderDetailsList.get(i).getItemTotalPrice(), Element.ALIGN_CENTER, 1, urFontName); // insert date value
            insertCell(table,"  "+ orderDetailsList.get(i).getUnitPrice(), Element.ALIGN_CENTER, 1, urFontName); // insert date value
            insertCell(table, orderDetailsList.get(i).getQuantity()+"", Element.ALIGN_CENTER, 1, urFontName); // insert date value
            insertCell(table, orderDetailsList.get(i).getProduct().getDisplayName(), Element.ALIGN_CENTER, 3, urFontName); // insert date value
            totalPrice+=orderDetailsList.get(i).getItemTotalPrice();
            count+=orderDetailsList.get(i).getQuantity();
        }
        insertCell(table, "\n\n\n---------------------------" , Element.ALIGN_CENTER, 7, font);
        insertCell(table, context.getString(R.string.product_quantity)+" : "+count  , Element.ALIGN_CENTER, 7, font);
        insertCell(table, context.getString(R.string.total_price)+" : "+totalPrice  , Element.ALIGN_CENTER, 7, font);
        //add table to document
        document.add(headingTable);
        document.add(table);
        document.close();
    }
    public static void insertCellSalesMan(PdfPTable table, String text, int align, int colspan, Font font){
        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        // cell.setBorder(Rectangle.NO_BORDER);

        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        cell.setPadding(4);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(14f);
        }
        //add the call to the table
        table.addCell(cell);

    }
    public static void createXReport(Context context, XReport xReport) throws IOException, DocumentException {
        Document document = new Document();
        String fileName = "xreport.pdf";
        final String APPLICATION_PACKAGE_NAME = context.getPackageName();
        File path = new File( Environment.getExternalStorageDirectory(), APPLICATION_PACKAGE_NAME );
        path.mkdirs();
        File file = new File(path, fileName);
        if(file.exists()){
            PrintWriter writer = new PrintWriter(file);//to empty file each time method invoke
            writer.print("");
            writer.close();
        }

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();        //end region
        //end region
        getCountForXReport(context,xReport);
        BaseFont urName = BaseFont.createFont("assets/miriam_libre_regular.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 24);
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);
        insertCell(headingTable,  SETTINGS.companyName , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.date) +":  "+xReport.getCreatedAt(), Element.ALIGN_CENTER, 1, font);
        EmployeeDBAdapter employeeDBAdapter = new EmployeeDBAdapter(context);
        employeeDBAdapter.open();
        insertCell(headingTable, context.getString(R.string.user_name)+":  " +employeeDBAdapter.getEmployeesName( xReport.getByUser()), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.x_report) + " " +xReport.getxReportId(), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable,"----------------------------", Element.ALIGN_CENTER, 1, font);



        PdfPTable dataTable = new PdfPTable(4);
        dataTable.deleteBodyRows();
        dataTable.setRunDirection(0);
        insertCell(dataTable, context.getString(R.string.total), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.details), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable, xReport.getInvoiceReceiptAmount() + " ", Element.ALIGN_RIGHT,1, font);
        insertCell(dataTable,invoiceReceiptCount + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.invoice_receipt), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable, xReport.getInvoiceAmount() + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,invoiceCount + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.invoice), Element.ALIGN_RIGHT,2, font);

        insertCell(dataTable, xReport.getCreditInvoiceAmount() + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,creditCardCount + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.credit_invoice_doc), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable, xReport.getTotalSales() + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,  "~", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.total_sales), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        insertCell(dataTable, context.getString(R.string.total), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.currency), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable, cashAmount + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,cashCount + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.cash), Element.ALIGN_RIGHT,2, font);

        ///// shekel region
        insertCell(dataTable, Util.makePrice(xReport.getShekelAmount()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,ShekelCount+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.shekel), Element.ALIGN_RIGHT, 2, font);

        ///// usd region
        insertCell(dataTable, Util.makePrice(xReport.getUsdAmount()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,UsdCount+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.usd), Element.ALIGN_RIGHT, 2, font);

        ///// eur region
        insertCell(dataTable, Util.makePrice(xReport.getEurAmount()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,EurCount+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.eur), Element.ALIGN_RIGHT, 2, font);
        ///// Gbp region
        insertCell(dataTable, Util.makePrice(xReport.getGbpAmount()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,GbpCount+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.gbp), Element.ALIGN_RIGHT, 2, font);
        ///// CreditCard region
        insertCell(dataTable, Util.makePrice(xReport.getCreditTotal()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,creditCardCount+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.credit_card), Element.ALIGN_RIGHT, 2, font);
        insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        ///check region
        insertCell(dataTable, context.getString(R.string.total), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.currency), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable, Util.makePrice(xReport.getCheckTotal()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,checkCount+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.checks), Element.ALIGN_RIGHT, 2, font);
        if(checkList.size()>0){
            insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);
            insertCell(dataTable, context.getString(R.string.amount), Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable, context.getString(R.string.date), Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable, context.getString(R.string.checks), Element.ALIGN_RIGHT, 2, font);
            for(int i=0;i<checkList.size();i++){
                List<Check>list = checkList.get(i);

                for (int f =0;f<list.size();f++){
                    insertCell(dataTable, list.get(f).getAmount()+" ", Element.ALIGN_RIGHT, 1, font);
                    insertCell(dataTable, DateConverter.toDate(list.get(f).getCreatedAt())+" ", Element.ALIGN_RIGHT, 2, font);
                    insertCell(dataTable, list.get(f).getCheckNum()+" ", Element.ALIGN_RIGHT, 1, font);
                }
            }
            insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        }
        insertCell(dataTable, xReport.getTotalAmount()+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, "~", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.total_amount), Element.ALIGN_RIGHT, 2, font);



        ////
        PdfPTable opiningReportTable = new PdfPTable(4);
        opiningReportTable.deleteBodyRows();
        opiningReportTable.setRunDirection(0);
        insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        insertCell(opiningReportTable, context.getString(R.string.opening_report), Element.ALIGN_RIGHT, 4, font);
        insertCell(opiningReportTable, opiningReportList.size()+" " , Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable, aReportAmount +" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable, context.getString(R.string.amount), Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        PdfPTable opiningReportDetailsTable = new PdfPTable(2);
        opiningReportDetailsTable.deleteBodyRows();
        opiningReportDetailsTable.setRunDirection(0);
        if(SETTINGS.enableCurrencies) {
            insertCell(opiningReportDetailsTable, context.getString(R.string.amount), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, context.getString(R.string.type), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, aReportDetailsForFirstCurrency + " ", Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, context.getString(R.string.shekel), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, aReportDetailsForSecondCurrency + " ", Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, context.getString(R.string.usd), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, aReportDetailsForThirdCurrency + " ", Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, context.getString(R.string.gbp), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, aReportDetailsForForthCurrency + " ", Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, context.getString(R.string.eur), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, "----------------------------", Element.ALIGN_CENTER, 2, font);
        }
        PdfPTable posSalesTable = new PdfPTable(2);
        posSalesTable.deleteBodyRows();
        posSalesTable.setRunDirection(0);
        insertCell(posSalesTable, context.getString(R.string.pos_sales), Element.ALIGN_RIGHT, 1, font);
        insertCell(posSalesTable,xReport.getTotalPosSales() +" ", Element.ALIGN_RIGHT, 1, font);
        //add table to document
        document.add(headingTable);
        document.add(dataTable);
        document.add(opiningReportTable);
        if(SETTINGS.enableCurrencies){
            document.add(opiningReportDetailsTable);

        }
        document.add(posSalesTable);
        document.close();
        //end :)


    }
    public static void getCountForXReport(Context context, XReport x) {
        aReportAmount=0;
        opiningReportList=new ArrayList<>();
        aReportDetailsForFirstCurrency=0;
        aReportDetailsForSecondCurrency=0;
        aReportDetailsForThirdCurrency=0;
        aReportDetailsForForthCurrency=0;

        OpiningReportDBAdapter opiningReportDBAdapter = new OpiningReportDBAdapter(context);
        opiningReportDBAdapter.open();
        opiningReportList = opiningReportDBAdapter.getListByLastZReport(x.getxReportId()-1);
        for (int i=0;i<opiningReportList.size();i++){
            aReportAmount+=opiningReportList.get(i).getAmount();
        }
        if (SETTINGS.enableCurrencies) {
            OpiningReportDetailsDBAdapter aReportDetailsDBAdapter=new OpiningReportDetailsDBAdapter(context);
            aReportDetailsDBAdapter.open();
            for (int a=0 ;a<opiningReportList.size();a++) {
                //aReportAmount+=opiningReportList.get(a).getAmount();
                OpiningReport opiningReport = opiningReportList.get(a);
                aReportDetailsForFirstCurrency+= aReportDetailsDBAdapter.getLastRow(CONSTANT.Shekel, opiningReport.getOpiningReportId());
                aReportDetailsForSecondCurrency+= aReportDetailsDBAdapter.getLastRow(CONSTANT.USD, opiningReport.getOpiningReportId());
                aReportDetailsForThirdCurrency+= aReportDetailsDBAdapter.getLastRow(CONSTANT.GBP, opiningReport.getOpiningReportId());
                aReportDetailsForForthCurrency+= aReportDetailsDBAdapter.getLastRow(CONSTANT.EUR, opiningReport.getOpiningReportId());
            }

        }
        checkList=new ArrayList<>();
        cashAmount=0;
        invoiceReceiptCount=0 ;invoiceCount=0; CreditInvoiceCount=0 ; ShekelCount=0 ;UsdCount=0 ;EurCount=0; GbpCount=0 ;checkCount=0 ; creditCardCount=0 ;receiptInvoiceAmountCheck=0 ; cashCount=0;receiptInvoiceAmount=0;
        OrderDBAdapter orderDb = new OrderDBAdapter(context);
        orderDb.open();
        XReportDBAdapter xReportDBAdapter = new XReportDBAdapter(context);
        xReportDBAdapter.open();
        invoiceReceiptCount = orderDb.getBetween(x.getStartOrderId(),x.getEndOrderId()).size();
        if(xReportDBAdapter.getProfilesCount()==0){
            PosInvoiceDBAdapter posInvoiceDBAdapter =new PosInvoiceDBAdapter(context);
            posInvoiceDBAdapter.open();
            List<PosInvoice>posInvoiceList = posInvoiceDBAdapter.getPosInvoiceList(-1, InvoiceStatus.UNPAID.getValue());
            invoiceCount+=posInvoiceList.size();

            List<PosInvoice>posCreditInvoiceList = posInvoiceDBAdapter.getPosInvoiceListByType(-1, DocumentType.CREDIT_INVOICE.getValue(),CONSTANT.CASH);
            CreditInvoiceCount+=posCreditInvoiceList.size();

            List<PosInvoice>posReceiptList = posInvoiceDBAdapter.getPosInvoiceListByType(-1, DocumentType.RECEIPT.getValue(),CONSTANT.CHECKS);
            receiptInvoiceAmountCheck+=posReceiptList.size();

        }else {
            XReport xReport1=null;
            try {
                xReport1 = xReportDBAdapter.getLastRow();
            } catch (Exception e) {
                e.printStackTrace();
            }
            PosInvoiceDBAdapter posInvoiceDBAdapter =new PosInvoiceDBAdapter(context);
            posInvoiceDBAdapter.open();
            List<PosInvoice>posInvoiceList = posInvoiceDBAdapter.getPosInvoiceList(xReport1.getxReportId(), InvoiceStatus.UNPAID.getValue());
            invoiceCount+=posInvoiceList.size();

            List<PosInvoice>posCreditInvoiceList = posInvoiceDBAdapter.getPosInvoiceListByType(xReport1.getxReportId(), DocumentType.CREDIT_INVOICE.getValue(),CONSTANT.CASH);
            CreditInvoiceCount+=posCreditInvoiceList.size();

            List<PosInvoice>posReceiptListCheck = posInvoiceDBAdapter.getPosInvoiceListByType(xReport1.getxReportId(), DocumentType.RECEIPT.getValue(),CONSTANT.CHECKS);
            receiptInvoiceAmountCheck+=posReceiptListCheck.size();
        }
        List<Long>orderIds= new ArrayList<>();
        List<Payment> payments = paymentList(orderDb.getBetween(x.getStartOrderId(),x.getEndOrderId()),context);
        for (Payment p : payments) {
            int i = 0;
            switch (p.getPaymentWay()) {

                case CONSTANT.CASH:
                    cashCount+=1;
                    cashAmount+=p.getAmount();
                    break;
                case CONSTANT.CREDIT_CARD:
                    creditCardCount+=1;
                    break;
                case CONSTANT.CHECKS:
                    checkCount+=1;
                    orderIds.add(p.getOrderId());
                    break;
            }
        }
        if(orderIds.size()>0){
            for (int id = 0;id<orderIds.size();id++){
                ChecksDBAdapter checkDb= new ChecksDBAdapter(context);
                checkDb.open();
                List<Check> c = checkDb.getPaymentBySaleID(orderIds.get(id));
                checkList.add(c);
            }
        }
        //with Currency
        if (SETTINGS.enableCurrencies) {
            List<CurrencyOperation>currencyOperationList=currencyOperationPaymentList(orderDb.getBetween(x.getStartOrderId(),x.getEndOrderId()),context);
            for (CurrencyOperation cp : currencyOperationList) {
                switch (cp.getCurrency_type()) {
                    case "ILS":
                        ShekelCount+=1;
                        break;
                    case "USD":
                        UsdCount+=1;
                        break;
                    case "EUR":
                        EurCount+=1;

                        break;
                    case "GBP":
                        GbpCount+=1;
                        break;
                }
            }

        }
        if(xReportDBAdapter.getProfilesCount()==0){
            PosInvoiceDBAdapter posInvoiceDBAdapter =new PosInvoiceDBAdapter(context);
            posInvoiceDBAdapter.open();
            List<PosInvoice>posReceiptList = posInvoiceDBAdapter.getPosInvoiceListByType(-1, DocumentType.RECEIPT.getValue(),CONSTANT.CASH);
            receiptInvoiceAmount+=posReceiptList.size();

        }else {
            XReport xReport1=null;
            try {
                xReport1 = xReportDBAdapter.getLastRow();
            } catch (Exception e) {
                e.printStackTrace();
            }
            PosInvoiceDBAdapter posInvoiceDBAdapter =new PosInvoiceDBAdapter(context);
            posInvoiceDBAdapter.open();
            List<PosInvoice>posReceiptList = posInvoiceDBAdapter.getPosInvoiceListByType(xReport1.getxReportId(), DocumentType.RECEIPT.getValue(),CONSTANT.CASH);
            receiptInvoiceAmount+=posReceiptList.size();

        }
        ShekelCount+=receiptInvoiceAmount;
        checkCount+=receiptInvoiceAmountCheck;

    }
    public static void createMonthZReport( Context context,ZReport zReport,Date from ,Date to) throws IOException, DocumentException {
        Document document = new Document();
        String fileName = "mounthzreport.pdf";
        final String APPLICATION_PACKAGE_NAME = context.getPackageName();
        File path = new File( Environment.getExternalStorageDirectory(), APPLICATION_PACKAGE_NAME );
        path.mkdirs();
        File file = new File(path, fileName);
        if(file.exists()){
            PrintWriter writer = new PrintWriter(file);//to empty file each time method invoke
            writer.print("");
            writer.close();
        }

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();        //end region
        //end region
        getCountForZReport(context,zReport);
        BaseFont urName = BaseFont.createFont("assets/miriam_libre_regular.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 24);
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);
        insertCell(headingTable,  SETTINGS.companyName , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.from) +":  "+DateConverter.toDate(from), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.to) +":  "+DateConverter.toDate(to), Element.ALIGN_CENTER, 1, font);
        EmployeeDBAdapter employeeDBAdapter = new EmployeeDBAdapter(context);
        employeeDBAdapter.open();
        insertCell(headingTable, context.getString(R.string.user_name)+":  " +employeeDBAdapter.getEmployeesName( zReport.getByUser()), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable,"----------------------------", Element.ALIGN_CENTER, 1, font);

        PdfPTable dataTable = new PdfPTable(4);
        dataTable.deleteBodyRows();
        dataTable.setRunDirection(0);
        insertCell(dataTable, context.getString(R.string.total), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.details), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable, zReport.getInvoiceReceiptAmount() + " ", Element.ALIGN_RIGHT,1, font);
        insertCell(dataTable,invoiceReceiptCount + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.invoice_receipt), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable, zReport.getInvoiceAmount() + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,invoiceCount + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.invoice), Element.ALIGN_RIGHT,2, font);

        insertCell(dataTable, zReport.getCreditInvoiceAmount() + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,creditCardCount + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.credit_invoice_doc), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable, zReport.getTotalSales() + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,  "~", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.total_sales), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        insertCell(dataTable, context.getString(R.string.total), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.currency), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable, cashAmount + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,cashCount + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.cash), Element.ALIGN_RIGHT,2, font);

        ///// shekel region
        insertCell(dataTable, Util.makePrice(zReport.getShekelAmount()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,ShekelCount+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.shekel), Element.ALIGN_RIGHT, 2, font);

        ///// usd region
        insertCell(dataTable, Util.makePrice(zReport.getUsdAmount()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,UsdCount+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.usd), Element.ALIGN_RIGHT, 2, font);

        ///// eur region
        insertCell(dataTable, Util.makePrice(zReport.getEurAmount()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,EurCount+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.eur), Element.ALIGN_RIGHT, 2, font);
        ///// Gbp region
        insertCell(dataTable, Util.makePrice(zReport.getGbpAmount()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,GbpCount+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.gbp), Element.ALIGN_RIGHT, 2, font);
        ///// CreditCard region
        insertCell(dataTable, Util.makePrice(zReport.getCreditTotal()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,creditCardCount+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.credit_card), Element.ALIGN_RIGHT, 2, font);
        insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        ///check region
        insertCell(dataTable, context.getString(R.string.total), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.currency), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable, Util.makePrice(zReport.getCheckTotal()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,checkCount+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.checks), Element.ALIGN_RIGHT, 2, font);
        if(checkList.size()>0){
            insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);
            insertCell(dataTable, context.getString(R.string.amount), Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable, context.getString(R.string.date), Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable, context.getString(R.string.checks), Element.ALIGN_RIGHT, 2, font);
            for(int i=0;i<checkList.size();i++){
                List<Check>list = checkList.get(i);

                for (int f =0;f<list.size();f++){
                    insertCell(dataTable, Util.makePrice(list.get(f).getAmount())+" ", Element.ALIGN_RIGHT, 1, font);
                    insertCell(dataTable, DateConverter.toDate(list.get(f).getCreatedAt())+" ", Element.ALIGN_RIGHT, 2, font);
                    insertCell(dataTable, list.get(f).getCheckNum()+" ", Element.ALIGN_RIGHT, 1, font);
                }
            }
            insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        }
        insertCell(dataTable, Util.makePrice(zReport.getTotalAmount())+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, "~", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.total_amount), Element.ALIGN_RIGHT, 2, font);



        ////
        PdfPTable opiningReportTable = new PdfPTable(4);
        opiningReportTable.deleteBodyRows();
        opiningReportTable.setRunDirection(0);
        insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        insertCell(opiningReportTable, context.getString(R.string.opening_report), Element.ALIGN_RIGHT, 4, font);
        insertCell(opiningReportTable, opiningReportList.size()+" " , Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable, aReportAmount +" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable, context.getString(R.string.amount), Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        PdfPTable opiningReportDetailsTable = new PdfPTable(2);
        opiningReportDetailsTable.deleteBodyRows();
        opiningReportDetailsTable.setRunDirection(0);
        if(SETTINGS.enableCurrencies) {
            insertCell(opiningReportDetailsTable, context.getString(R.string.amount), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, context.getString(R.string.type), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, aReportDetailsForFirstCurrency + " ", Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, context.getString(R.string.shekel), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, aReportDetailsForSecondCurrency + " ", Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, context.getString(R.string.usd), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, aReportDetailsForThirdCurrency + " ", Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, context.getString(R.string.gbp), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, aReportDetailsForForthCurrency + " ", Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, context.getString(R.string.eur), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, "----------------------------", Element.ALIGN_CENTER, 2, font);
        }
        PdfPTable posSalesTable = new PdfPTable(2);
        posSalesTable.deleteBodyRows();
        posSalesTable.setRunDirection(0);
        insertCell(posSalesTable, context.getString(R.string.pos_sales), Element.ALIGN_RIGHT, 1, font);
        insertCell(posSalesTable,Util.makePrice(zReport.getTotalPosSales()) +" ", Element.ALIGN_RIGHT, 1, font);
        //add table to document
        document.add(headingTable);
        document.add(dataTable);
        document.add(opiningReportTable);
        if(SETTINGS.enableCurrencies){
            document.add(opiningReportDetailsTable);

        }
        document.add(posSalesTable);
        document.close();
        //end :)


    }
    public static void createNormalInvoice(Context context  , List<OrderDetails>orderDetailsList) throws IOException, DocumentException {
        double totalPrice =0.0;
        int count=0;
        // create file , document region
        Document document = new Document();
        String fileName = "normalInvoice.pdf";
        final String APPLICATION_PACKAGE_NAME = context.getPackageName();
        File path = new File( Environment.getExternalStorageDirectory(), APPLICATION_PACKAGE_NAME );
        path.mkdirs();
        File file = new File(path, fileName);
        if(file.exists()){
            PrintWriter writer = new PrintWriter(file);//to empty file each time method invoke
            writer.print("");
            writer.close();
        }

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();        //end region
        //end region
        Date date;
        BaseFont urName = BaseFont.createFont("assets/miriam_libre_bold.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 30);
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);
        insertCell(headingTable,  SETTINGS.companyName , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.date) +":  "+new Timestamp(System.currentTimeMillis()), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.user_name)+":  " + SESSION._EMPLOYEE.getFirstName(), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.pause_invoice), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "\n\n\n---------------------------" , Element.ALIGN_CENTER, 4, font);

        BaseFont urName1 = BaseFont.createFont("assets/miriam_libre_regular.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font urFontName1 = new Font(urName1, 22);
        PdfPTable table = new PdfPTable(7);
        Font urFontName = new Font(urName1, 24);

        table.deleteBodyRows();
        table.setRunDirection(0);
        //insert column headings;
        insertCell(table, "%", Element.ALIGN_CENTER, 1, urFontName1);
        insertCell(table, context.getString(R.string.total), Element.ALIGN_CENTER, 1, urFontName1);
        insertCell(table, context.getString(R.string.price), Element.ALIGN_CENTER, 1, urFontName1);
        insertCell(table, context.getString(R.string.qty), Element.ALIGN_CENTER, 1, urFontName1);
        insertCell(table, context.getString(R.string.product), Element.ALIGN_CENTER, 3, urFontName1);

        for (int i=0;i<orderDetailsList.size();i++){
            insertCell(table, "  " + orderDetailsList.get(i).getDiscount(), Element.ALIGN_CENTER, 1, urFontName); // insert date value
            insertCell(table, "  " + orderDetailsList.get(i).getItemTotalPrice(), Element.ALIGN_CENTER, 1, urFontName); // insert date value
            insertCell(table,"  "+ orderDetailsList.get(i).getUnitPrice(), Element.ALIGN_CENTER, 1, urFontName); // insert date value
            insertCell(table, orderDetailsList.get(i).getQuantity()+"", Element.ALIGN_CENTER, 1, urFontName); // insert date value
            if (orderDetailsList.get(i).getProduct().getDisplayName().equals("General"))
                orderDetailsList.get(i).getProduct().setProductCode(context.getString(R.string.general));
            insertCell(table, orderDetailsList.get(i).getProduct().getDisplayName(), Element.ALIGN_CENTER, 3, urFontName); // insert date value
            totalPrice+=orderDetailsList.get(i).getItemTotalPrice();
            count+=orderDetailsList.get(i).getQuantity();
        }
        insertCell(table, "\n\n\n---------------------------" , Element.ALIGN_CENTER, 7, font);
        insertCell(table, context.getString(R.string.product_quantity)+" : "+count  , Element.ALIGN_CENTER, 7, font);
        insertCell(table, context.getString(R.string.total_price)+" : "+totalPrice  , Element.ALIGN_CENTER, 7, font);
        //add table to document
        document.add(headingTable);
        document.add(table);
        document.close();
    }
    public static void  printInventoryReport(Context context, String res,String source) throws IOException, DocumentException, JSONException {
        ProductDBAdapter productDBAdapter =new ProductDBAdapter(context);
        productDBAdapter.open();
        JSONObject jsonObject = new JSONObject(res);
        JSONObject documentsData = jsonObject.getJSONObject("documentsData");;
        JSONObject customerInfo = new JSONObject(documentsData.getJSONObject("provider").toString());
      //  JSONObject userInfo = new JSONObject(documentsData.getJSONObject("user").toString());

        // create file , document region
        Document document = new Document();
        String fileName = "inventoryReport.pdf";
        final String APPLICATION_PACKAGE_NAME = context.getPackageName();
        File path = new File( Environment.getExternalStorageDirectory(), APPLICATION_PACKAGE_NAME );
        path.mkdirs();
        File file = new File(path, fileName);
        if(file.exists()){
            PrintWriter writer = new PrintWriter(file);//to empty file each time method invoke
            writer.print("");
            writer.close();
        }

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();        //end region
        //end region

        BaseFont urName = BaseFont.createFont("assets/miriam_libre_regular.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 30);
        Font dateFont = new Font(urName, 24);
        //heading table
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);
        EmployeeDBAdapter employeeDBAdapter =new EmployeeDBAdapter(context);
        employeeDBAdapter.open();
        Employee employee = employeeDBAdapter.getEmployeeByID(documentsData.getLong("byEmployee"));
        insertCell(headingTable,  SETTINGS.companyName , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);
        if(employee!=null) {
            insertCell(headingTable, context.getString(R.string.cashiers) + employee.getFullName(), Element.ALIGN_CENTER, 1, font);
        }
        insertCell(headingTable, context.getString(R.string.provider)+":"+customerInfo.getString("firstName")+customerInfo.getString("lastName"), Element.ALIGN_LEFT, 1, dateFont);
        insertCell(headingTable, "\n---------------------------" , Element.ALIGN_CENTER, 1, font);


        if(source=="inInventory"){
            insertCell(headingTable,  context.getString(R.string.inventory_in) , Element.ALIGN_CENTER, 1, font);
            insertCell(headingTable, "\n---------------------------" , Element.ALIGN_CENTER, 1, font);

        }else {
            insertCell(headingTable,  context.getString(R.string.inventory_out) , Element.ALIGN_CENTER, 1, font);
            insertCell(headingTable, "\n---------------------------" , Element.ALIGN_CENTER, 1, font);

        }
        insertCell(headingTable,  context.getString(R.string.inventory_doc) +" : "+jsonObject.getString("docNum") , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "\n---------------------------" , Element.ALIGN_CENTER, 1, font);
        PdfPTable orderDetailsTable = new PdfPTable(3);
        orderDetailsTable.setRunDirection(0);
        orderDetailsTable.setWidthPercentage(108f);

        JSONObject itemJson = documentsData.getJSONObject("productsIdWithQuantityList");
        HashMap<String, Long> productHashMap = new Gson().fromJson(itemJson.toString(), HashMap.class);
        insertCell(orderDetailsTable, context.getString(R.string.product), Element.ALIGN_LEFT, 1, dateFont);
        insertCell(orderDetailsTable,context.getString(R.string.qty), Element.ALIGN_LEFT, 1, dateFont);
        insertCell(orderDetailsTable, context.getString(R.string.price), Element.ALIGN_LEFT, 1, dateFont);

        for (int a = 0 ; a<itemJson.length();a++) {
            double qty = (double) productHashMap.values().toArray()[a];

            Product product= productDBAdapter.getProductByID(Long.parseLong((String) productHashMap.keySet().toArray()[a]) );
            if(product==null){
                insertCell(orderDetailsTable, context.getString(R.string.general_product), Element.ALIGN_LEFT, 1, dateFont);
            }else {
                insertCell(orderDetailsTable, product.getDisplayName(), Element.ALIGN_LEFT, 1, dateFont);
            }
            insertCell(orderDetailsTable, ""+qty, Element.ALIGN_LEFT, 1, dateFont);
            insertCell(orderDetailsTable,Util.makePrice(product.getCostPrice()*qty), Element.ALIGN_LEFT, 1, dateFont);
        }
        insertCell(orderDetailsTable, "\n---------------------------" , Element.ALIGN_CENTER, 3, font);

        //end

        //add table to document
        document.add(headingTable);
        document.add(orderDetailsTable);
        document.close();
        //end :)
    }

}
