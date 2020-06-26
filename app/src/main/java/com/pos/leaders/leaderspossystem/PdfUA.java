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
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CreditCardPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyOperationDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyReturnsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.DepositAndPullReportDetailsDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PosInvoiceDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.XReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.CreditCardPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.Currency;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyOperation;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyReturns;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.CustomerAssistant;
import com.pos.leaders.leaderspossystem.Models.CustomerType;
import com.pos.leaders.leaderspossystem.Models.DepositAndPullReport;
import com.pos.leaders.leaderspossystem.Models.Employee;
import com.pos.leaders.leaderspossystem.Models.InvoiceStatus;
import com.pos.leaders.leaderspossystem.Models.OpiningReport;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.PosInvoice;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.ScheduleWorkers;
import com.pos.leaders.leaderspossystem.Models.XReport;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Models.ZReportCount;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.Tools.UtilitValidationDate;

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
import java.util.Locale;
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
    static  int invoiceReceiptCount=0 ,invoiceCount=0 , CreditInvoiceCount=0 , firstTypeCount=0 ,secondTypeCount=0 , thirdTypeCount=0,fourthTypeCount=0 ,checkCount=0 , creditCardCount=0 ,receiptInvoiceAmountCheck=0 , cashCount=0,receiptInvoiceAmount=0;
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

        BaseFont urName = BaseFont.createFont("assets/arial.ttf", "Identity-H",true,BaseFont.EMBEDDED);
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
    public static void createZReport(Context context, ZReport zReport, ZReportCount zReportCount, boolean source) throws IOException, DocumentException {
        List<CurrencyType> currencyTypesList = null;
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(context);
        currencyTypeDBAdapter.open();
        currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
        currencyTypeDBAdapter.close();
        Order  order=SESSION._TEMP_ORDERS;
        if(zReportCount!=null) {
            getCountForZReport(context, zReport);
        }else {
            zReportCount=new ZReportCount();
        }
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
        BaseFont urName = BaseFont.createFont("assets/arial.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 24);
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);
        insertCell(headingTable,  SETTINGS.companyName+"" , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);

        if(source) {
            insertCell(headingTable, context.getString(R.string.source_invoice), Element.ALIGN_CENTER, 1, font);
        }else {
            insertCell(headingTable, context.getString(R.string.copy_invoice), Element.ALIGN_CENTER, 1, font);
        }
        Log.d("zReportDate",zReport.getCreatedAt()+"");
        String zReportDate= UtilitValidationDate.isValidDate(zReport.getCreatedAt());
        String zReportDateEnd= String.valueOf(new Timestamp(System.currentTimeMillis()));
        Log.d("zReportDateEnd",zReportDateEnd);
        insertCell(headingTable, context.getString(R.string.date) +":  "+zReport.getCreatedAt(), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.date_end_zreport) +":  "+zReportDateEnd, Element.ALIGN_CENTER, 1, font);
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

        insertCell(dataTable,  Util.makePrice(zReport.getInvoiceReceiptAmount()), Element.ALIGN_RIGHT,1, font);
        insertCell(dataTable,zReportCount.getInvoiceReceiptCount() + " ", Element.ALIGN_RIGHT, 1, font);
        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
            insertCell(dataTable, context.getString(R.string.invoice_recipte_company_status), Element.ALIGN_RIGHT, 2, font);}
        else {
            insertCell(dataTable, context.getString(R.string.invoice_receipt), Element.ALIGN_RIGHT, 2, font);
        }

        insertCell(dataTable,  Util.makePrice(zReport.getInvoiceAmount()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,zReportCount.getInvoiceCount() + " ", Element.ALIGN_RIGHT, 1, font);
        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
            insertCell(dataTable, context.getString(R.string.invoice_company_status), Element.ALIGN_RIGHT,2, font);
        }
        else {
            insertCell(dataTable, context.getString(R.string.invoice), Element.ALIGN_RIGHT,2, font);}

        insertCell(dataTable, Util.makePrice( zReport.getCreditInvoiceAmount()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,zReportCount.getCreditInvoiceCount() + " ", Element.ALIGN_RIGHT, 1, font);
        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
            insertCell(dataTable, context.getString(R.string.credit_invoice_doc_company_status), Element.ALIGN_RIGHT, 2, font);
        }
        else {
            insertCell(dataTable, context.getString(R.string.credit_invoice_doc), Element.ALIGN_RIGHT, 2, font);
        }
        insertCell(dataTable,  Util.makePrice(zReport.getTotalSales() ), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,  "~", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.total_sales), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        insertCell(dataTable, context.getString(R.string.total), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.method), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable,  Util.makePrice(zReport.getCashTotal() ), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,zReportCount.getCashCount() + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.cash), Element.ALIGN_RIGHT,2, font);
        ///// CreditCard region
        insertCell(dataTable, Util.makePrice(zReport.getCreditTotal()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,zReportCount.getCreditCount()+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.credit_card), Element.ALIGN_RIGHT, 2, font);

        ///check region
        insertCell(dataTable, Util.makePrice(zReport.getCheckTotal()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,zReportCount.getCheckCount()+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.checks), Element.ALIGN_RIGHT, 2, font);

        // Total Amount
        insertCell(dataTable,  Util.makePrice(zReport.getTotalAmount()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, "~", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.total_amount), Element.ALIGN_RIGHT, 2, font);


        insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        insertCell(dataTable, context.getString(R.string.total), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.currency), Element.ALIGN_RIGHT, 2, font);
        ///// firstType region
        insertCell(dataTable, Util.makePrice(zReport.getFirstTypeAmount()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,firstTypeCount+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, currencyTypesList.get(0).getType(), Element.ALIGN_RIGHT, 2, font);

        if (SETTINGS.enableCurrencies){
            ///// secondType region
            insertCell(dataTable, Util.makePrice(zReport.getSecondTypeAmount()), Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable,zReportCount.getSecondTypeCount()+" ", Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable, currencyTypesList.get(1).getType(), Element.ALIGN_RIGHT, 2, font);

            ///// thirdType region
            insertCell(dataTable, Util.makePrice(zReport.getThirdTypeAmount()), Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable,zReportCount.getThirdTypeCount()+" ", Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable, currencyTypesList.get(2).getType(), Element.ALIGN_RIGHT, 2, font);
            ///// fourthType region
            insertCell(dataTable, Util.makePrice(zReport.getFourthTypeAmount()), Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable,zReportCount.getFourthTypeCount()+" ", Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable, currencyTypesList.get(3).getType(), Element.ALIGN_RIGHT, 2, font);}




        if(checkList.size()>0){
            insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);
            insertCell(dataTable, context.getString(R.string.amount), Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable, context.getString(R.string.date), Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable, context.getString(R.string.checks), Element.ALIGN_RIGHT, 2, font);
            for(int i=0;i<checkList.size();i++){
                List<Check>list = checkList.get(i);

                for (int f =0;f<list.size();f++){
                    insertCell(dataTable,  Util.makePrice(list.get(f).getAmount()), Element.ALIGN_RIGHT, 1, font);
                    insertCell(dataTable, DateConverter.toDate(list.get(f).getCreatedAt())+" ", Element.ALIGN_RIGHT, 2, font);
                    insertCell(dataTable, list.get(f).getCheckNum()+" ", Element.ALIGN_RIGHT, 1, font);
                }
            }
            insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        }

        ////
        PdfPTable opiningReportTable = new PdfPTable(4);
        opiningReportTable.deleteBodyRows();
        opiningReportTable.setRunDirection(0);
        insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        insertCell(opiningReportTable, context.getString(R.string.opening_report), Element.ALIGN_RIGHT, 4, font);
        insertCell(opiningReportTable, opiningReportList.size()+" " , Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable,  Util.makePrice(aReportAmount), Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable, context.getString(R.string.amount), Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable,"----------------------------", Element.ALIGN_CENTER, 4, font);
        PdfPTable pullAndDepositAmount = new PdfPTable(4);
        pullAndDepositAmount.deleteBodyRows();
        pullAndDepositAmount.setRunDirection(0);

        insertCell(pullAndDepositAmount, context.getString(R.string.pull_report), Element.ALIGN_RIGHT, 4, font);
        insertCell(pullAndDepositAmount,  Util.makePrice(zReport.getPullReportAmount()), Element.ALIGN_RIGHT, 2, font);
        insertCell(pullAndDepositAmount, context.getString(R.string.amount), Element.ALIGN_RIGHT, 2, font);
        insertCell(pullAndDepositAmount,"----------------------------", Element.ALIGN_CENTER, 4, font);
        insertCell(pullAndDepositAmount, context.getString(R.string.deposit_report), Element.ALIGN_RIGHT, 4, font);
        insertCell(pullAndDepositAmount,  Util.makePrice(zReport.getDepositReportAmount()), Element.ALIGN_RIGHT, 2, font);
        insertCell(pullAndDepositAmount, context.getString(R.string.amount), Element.ALIGN_RIGHT, 2, font);
        insertCell(pullAndDepositAmount,"----------------------------", Element.ALIGN_CENTER, 4, font);

        PdfPTable opiningReportDetailsTable = new PdfPTable(2);
        opiningReportDetailsTable.deleteBodyRows();
        opiningReportDetailsTable.setRunDirection(0);
        if(SETTINGS.enableCurrencies) {
            insertCell(opiningReportDetailsTable, context.getString(R.string.amount), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, context.getString(R.string.type), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, Util.makePrice( aReportDetailsForFirstCurrency ), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, currencyTypesList.get(0).getType(), Element.ALIGN_RIGHT, 1, font);

            insertCell(opiningReportDetailsTable,  Util.makePrice(aReportDetailsForSecondCurrency ), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, currencyTypesList.get(1).getType(), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable,  Util.makePrice(aReportDetailsForThirdCurrency ), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, currencyTypesList.get(2).getType(), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable,  Util.makePrice(aReportDetailsForForthCurrency), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, currencyTypesList.get(3).getType(), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, "----------------------------", Element.ALIGN_CENTER, 2, font);
        }

        double salesaftertax=0;
        for (int i=0;i<SESSION._ORDER_DETAILES.size();i++){
            try {
                if (SESSION._ORDER_DETAILES.get(i).getProduct()!=null){
                    if(!SESSION._ORDER_DETAILES.get(i).getProduct().isWithTax()){

                    }else {
                        salesaftertax+=SESSION._ORDER_DETAILES.get(i).getPaidAmount();
                    }
                }}
            catch(NullPointerException e)
            {
                Log.d("NullPointerExceptionPdf",e.toString());
            }

        }





        PdfPTable accountInformation = new PdfPTable(4);
        accountInformation.deleteBodyRows();
        accountInformation.setRunDirection(0);
        insertCell(accountInformation, context.getString(R.string.accounting_information), Element.ALIGN_RIGHT, 4, font);

        insertCell(accountInformation,Util.makePrice(zReport.getSalesWithTax()) + " ", Element.ALIGN_RIGHT, 2, font);
        insertCell(accountInformation, context.getString(R.string.Sales_With_tax), Element.ALIGN_RIGHT,2, font);

        insertCell(accountInformation,Util.makePrice(zReport.getTotalTax())+ " ", Element.ALIGN_RIGHT, 2, font);
        insertCell(accountInformation, context.getString(R.string.tax), Element.ALIGN_RIGHT,2, font);

        insertCell(accountInformation,Util.makePrice(zReport.getSalesBeforeTax()) + " ", Element.ALIGN_RIGHT, 2, font);
        insertCell(accountInformation, context.getString(R.string.Sales_Without_tax), Element.ALIGN_RIGHT,2, font);


        insertCell(accountInformation,Util.makePrice(zReport.getSalesWithTax()+zReport.getSalesBeforeTax()+zReport.getTotalTax()) + " ", Element.ALIGN_RIGHT, 2, font);
        insertCell(accountInformation, context.getString(R.string.total_price), Element.ALIGN_RIGHT,2, font);
        insertCell(accountInformation, "----------------------------", Element.ALIGN_CENTER, 4, font);


        PdfPTable posSalesTable = new PdfPTable(2);
        posSalesTable.deleteBodyRows();
        posSalesTable.setRunDirection(0);
        insertCell(posSalesTable, context.getString(R.string.pos_sales), Element.ALIGN_RIGHT, 1, font);
        insertCell(posSalesTable, Util.makePrice(zReport.getTotalPosSales()), Element.ALIGN_RIGHT, 1, font);
        //add table to document
        document.add(headingTable);
        document.add(dataTable);
        document.add(opiningReportTable);
        if(SETTINGS.enableCurrencies){
            document.add(opiningReportDetailsTable);

        }
        document.add(pullAndDepositAmount);
        document.add(accountInformation);

        document.add(posSalesTable);
        document.close();
        //end :)


    }
    public static  void getCountForZReport(Context context, ZReport z) {
        List<CurrencyType> currencyTypesList = null;
        List<Currency> currencyList=new ArrayList<>();
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(context);
        currencyTypeDBAdapter.open();
        currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
        Log.d("currencyTypesListooo",currencyTypesList.toString());

        for (int i=0;i<currencyTypesList.size();i++){
            CurrencyDBAdapter currencyDBAdapter =new CurrencyDBAdapter(context);
            currencyDBAdapter.open();
            currencyList.add(currencyDBAdapter.getCurrencyByCode(currencyTypesList.get(i).getType()));
            currencyDBAdapter.close();
        }
        Log.d("currencyKdd",currencyList.toString());
        Log.d("hyyg",currencyList.get(0).getId()+"");

        currencyTypeDBAdapter.close();
        ZReportDBAdapter zReportDBAdapter =new ZReportDBAdapter(context);
        zReportDBAdapter.open();
        JSONObject res = new JSONObject();
        aReportAmount=0;
        opiningReportList=new ArrayList<>();
        aReportDetailsForFirstCurrency=0;
        aReportDetailsForSecondCurrency=0;
        aReportDetailsForThirdCurrency=0;
        aReportDetailsForForthCurrency=0;

        OpiningReportDBAdapter opiningReportDBAdapter = new OpiningReportDBAdapter(context);
        opiningReportDBAdapter.open();
        if(zReportDBAdapter.getProfilesCount()==1) {
            opiningReportList = opiningReportDBAdapter.getListByLastZReport(-1);

        }else {

            opiningReportList = opiningReportDBAdapter.getListByLastZReport(z.getzReportId());
        }
        for (int i=0;i<opiningReportList.size();i++){
            aReportAmount+=opiningReportList.get(i).getAmount();
        }
        if (SETTINGS.enableCurrencies) {
            OpiningReportDetailsDBAdapter aReportDetailsDBAdapter=new OpiningReportDetailsDBAdapter(context);
            aReportDetailsDBAdapter.open();
            for (int a=0 ;a<opiningReportList.size();a++) {
                //aReportAmount+=opiningReportList.get(a).getAmount();
                OpiningReport opiningReport = opiningReportList.get(a);
                aReportDetailsForFirstCurrency+= aReportDetailsDBAdapter.getLastRow((int) currencyList.get(0).getId(), opiningReport.getOpiningReportId());
                aReportDetailsForSecondCurrency+= aReportDetailsDBAdapter.getLastRow((int) currencyList.get(1).getId(), opiningReport.getOpiningReportId());
                aReportDetailsForThirdCurrency+= aReportDetailsDBAdapter.getLastRow((int) currencyList.get(2).getId(), opiningReport.getOpiningReportId());
                aReportDetailsForForthCurrency+= aReportDetailsDBAdapter.getLastRow((int) currencyList.get(3).getId(), opiningReport.getOpiningReportId());
            }

        }

        else {
            OpiningReportDetailsDBAdapter aReportDetailsDBAdapter=new OpiningReportDetailsDBAdapter(context);
            aReportDetailsDBAdapter.open();
            for (int a=0 ;a<opiningReportList.size();a++) {
                OpiningReport opiningReport = opiningReportList.get(a);
                aReportDetailsForFirstCurrency += aReportDetailsDBAdapter.getLastRow((int) currencyList.get(0).getId(), opiningReport.getOpiningReportId());
            }
        }
       /* checkList=new ArrayList<>();
        cashAmount=0;
        invoiceReceiptCount=0 ;invoiceCount=0; CreditInvoiceCount=0 ; ShekelCount=0 ;UsdCount=0 ;EurCount=0; GbpCount=0 ;checkCount=0 ; creditCardCount=0 ;receiptInvoiceAmountCheck=0 ; cashCount=0;receiptInvoiceAmount=0;
        OrderDBAdapter orderDb = new OrderDBAdapter(context);
        orderDb.open();
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
        }*/
      /*  List<Long>orderIds= new ArrayList<>();
        List<Payment> payments = paymentList(orderDb.getBetween(z.getStartOrderId(),z.getEndOrderId()),context);
        CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(context);
        cashPaymentDBAdapter.open();
        ChecksDBAdapter checksDBAdapter =new ChecksDBAdapter(context);
        checksDBAdapter.open();
        CreditCardPaymentDBAdapter creditCardPaymentDBAdapter =new CreditCardPaymentDBAdapter(context);
        creditCardPaymentDBAdapter.open();
        for (Payment p : payments) {
            long orderId = p.getOrderId();
            List<CashPayment>cashPaymentList=cashPaymentDBAdapter.getPaymentBySaleID(orderId);
            for(int i=0;i<cashPaymentList.size();i++){
                cashCount+=1;
                cashAmount+=p.getAmount();
            }
            List<Check>checkList=checksDBAdapter.getPaymentBySaleID(orderId);
            for(int i=0;i<checkList.size();i++){
                checkCount+=1;
            }
            List<CreditCardPayment>creditCardPayments=creditCardPaymentDBAdapter.getPaymentByOrderID(orderId);
            for(int i=0;i<creditCardPayments.size();i++){
                creditCardCount+=1;            }
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
                switch (cp.getCurrencyType()) {
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
        checkCount+=receiptInvoiceAmountCheck;*/

    }
    public static  void getCountMounthForZReport(Context context, ZReport z) {
        ZReportDBAdapter zReportDBAdapter =new ZReportDBAdapter(context);
        zReportDBAdapter.open();
        JSONObject res = new JSONObject();
        aReportAmount=0;
        opiningReportList=new ArrayList<>();
        aReportDetailsForFirstCurrency=0;
        aReportDetailsForSecondCurrency=0;
        aReportDetailsForThirdCurrency=0;
        aReportDetailsForForthCurrency=0;

        OpiningReportDBAdapter opiningReportDBAdapter = new OpiningReportDBAdapter(context);
        opiningReportDBAdapter.open();
        if(zReportDBAdapter.getProfilesCount()==1) {
            opiningReportList = opiningReportDBAdapter.getListByLastZReport(-1);

        }else {

            opiningReportList = opiningReportDBAdapter.getListByLastZReport(z.getzReportId());
        }
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
        invoiceReceiptCount=0 ;invoiceCount=0; CreditInvoiceCount=0 ; firstTypeCount=0 ;secondTypeCount=0 ;thirdTypeCount=0; fourthTypeCount=0 ;checkCount=0 ; creditCardCount=0 ;receiptInvoiceAmountCheck=0 ; cashCount=0;receiptInvoiceAmount=0;
        OrderDBAdapter orderDb = new OrderDBAdapter(context);
        orderDb.open();
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
        CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(context);
        cashPaymentDBAdapter.open();
        ChecksDBAdapter checksDBAdapter =new ChecksDBAdapter(context);
        checksDBAdapter.open();
        CreditCardPaymentDBAdapter creditCardPaymentDBAdapter =new CreditCardPaymentDBAdapter(context);
        creditCardPaymentDBAdapter.open();
        for (Payment p : payments) {
            long orderId = p.getOrderId();
            List<CashPayment>cashPaymentList=cashPaymentDBAdapter.getPaymentBySaleID(orderId);
            for(int i=0;i<cashPaymentList.size();i++){
                cashCount+=1;
                cashAmount+=p.getAmount();
            }
            List<Check>checkList=checksDBAdapter.getPaymentBySaleID(orderId);
            for(int i=0;i<checkList.size();i++){
                checkCount+=1;
            }
            List<CreditCardPayment>creditCardPayments=creditCardPaymentDBAdapter.getPaymentByOrderID(orderId);
            for(int i=0;i<creditCardPayments.size();i++){
                creditCardCount+=1;            }
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
            //Getting default currencies name and values
            List<CurrencyType> currencyTypesList = null;
            CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(context);
            currencyTypeDBAdapter.open();
            currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
            currencyTypeDBAdapter.close();
            List<CurrencyOperation>currencyOperationList=currencyOperationPaymentList(orderDb.getBetween(z.getStartOrderId(),z.getEndOrderId()),context);
            for (CurrencyOperation cp : currencyOperationList) {
            /*    switch (cp.getCurrencyType()) {
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
                }*/
                if (cp.getCurrencyType().equals(currencyTypesList.get(0).getType())){

                    firstTypeCount+=1;
                }
                else   if (cp.getCurrencyType().equals(currencyTypesList.get(1).getType())){
                    secondTypeCount+=1;
                }
                else   if (cp.getCurrencyType().equals(currencyTypesList.get(2).getType())){
                    thirdTypeCount+=1;
                }
                else if (cp.getCurrencyType().equals(currencyTypesList.get(3).getType())){
                    fourthTypeCount+=1;
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
        firstTypeCount+=receiptInvoiceAmount;
        checkCount+=receiptInvoiceAmountCheck;

    }
    public static void createUserReport(Context context ,PdfPTable table , List<ScheduleWorkers>scheduleWorkersList) throws IOException, DocumentException {
        Date date , startAt=null , endAt =null;
        table.setRunDirection(0);
        table.setWidthPercentage(118f);
        table.setWidths(new int[]{1, 1, 1,2});
        BaseFont urName = BaseFont.createFont("assets/arial.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font urFontName = new Font(urName, 24);
        BaseFont urName1 = BaseFont.createFont("assets/arial.ttf", "Identity-H",true,BaseFont.EMBEDDED);
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
        if (text!=null) {
            PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
            cell.setBorder(Rectangle.NO_BORDER);

            //set the cell alignment
            cell.setHorizontalAlignment(align);
            //set the cell column span in case you want to merge two or more cells+
            cell.setColspan(colspan);
            //in case there is no text and you wan to create an empty row
            if (text.trim().equalsIgnoreCase("")) {
                cell.setMinimumHeight(14f);
            }
            //add the call to the table
            table.addCell(cell);
        }
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

        BaseFont urName = BaseFont.createFont("assets/arial.ttf", "Identity-H",true,BaseFont.EMBEDDED);
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
    public static void  printReceiptReport(Context context, String res,String mainMer) throws IOException, DocumentException, JSONException {
        String str="";
        Log.d("rrrr",res);
        JSONObject jsonObject = new JSONObject(res);
        String documentsData = jsonObject.getString("documentsData");
        JSONObject customerJson = new JSONObject(documentsData);
        JSONArray refNumber = customerJson.getJSONArray("invoicesNumbers");
        JSONArray payments = customerJson.getJSONArray("payments");
        JSONObject payment = payments.getJSONObject(0);
        JSONArray paymentDetails = payment.getJSONArray("paymentDetails");
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

        BaseFont urName = BaseFont.createFont("assets/arial.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 30);
        Font dateFont = new Font(urName, 24);
        //heading table
        PdfPTable headingTable = new PdfPTable(1);
        PdfPTable creditCard = new PdfPTable(4);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);
        creditCard.deleteBodyRows();
        creditCard.setRunDirection(0);
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
        insertCell(orderDetailsTable, context.getString(R.string.total_paid)+": "+customerJson.getDouble("paidAmount"), Element.ALIGN_LEFT, 3, dateFont);
        insertCell(orderDetailsTable, "\n---------------------------" , Element.ALIGN_CENTER, 3, font);
        insertCell(orderDetailsTable, context.getString(R.string.cashiers) + SESSION._EMPLOYEE.getFullName(), Element.ALIGN_CENTER, 3, font);
        insertCell(orderDetailsTable, context.getString(R.string.date)+":"+DateConverter.stringToDate(customerJson.getString("date")), Element.ALIGN_LEFT, 3, dateFont);
        insertCell(dateTable, context.getString(R.string.reference_invoice)+":"+refNumber.get(0), Element.ALIGN_LEFT, 3, dateFont);
        insertCell(dateTable, context.getString(R.string.customer_ledger)+":"+customerJson.getString("customerGeneralLedger"), Element.ALIGN_LEFT, 3, dateFont);

        insertCell(orderDetailsTable, "\n---------------------------" , Element.ALIGN_CENTER, 3, font);

        if (paymentDetails.getJSONObject(0).getString("@type").equalsIgnoreCase("check")) {
            insertCell(orderDetailsTable, context.getString(R.string.amount), Element.ALIGN_LEFT, 1, dateFont);
            insertCell(orderDetailsTable, context.getString(R.string.date), Element.ALIGN_LEFT, 1, dateFont);
            insertCell(orderDetailsTable, context.getString(R.string.checks), Element.ALIGN_LEFT, 1, dateFont);
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
        Log.d("mainMerCredit",mainMer);
        for (String s : mainMer.split("\n")) {

           /* String[] tokens = s.split("\\s+");
            Log.i("split0", Arrays.toString(tokens));*/

            if(!s.replaceAll(" ","").equals("")) {
                if(s.contains("שם מסוף")){
                    continue;
                }
                else if(s.contains("מספר מסוף")){
                    continue;
                }
                else if(s.contains("גרסת תוכנה")){
                    continue;
                }
                else if(s.contains("מספר עסק בחברת האשראי")){
                    continue;
                }
                else if(s.contains("Powered")){
                    continue;
                }
                else  if(s.contains("מספר כרטיס")){
                    if(s.split("\\s+")[1].length()>4){
                        String head = "מספר כרטיס";
                        String ss = "";
                        for(int i=0;i<s.split("\\s+")[1].length()-4;i++) {
                            ss += "*";
                        }
                        ss += s.split("\\s+")[1].substring(s.split("\\s+")[1].length() - 4, s.split("\\s+")[1].length());
                        str += "\u200E" + ss + "\t" + head + "\n";
                        continue;
                    }
                }
                str += "\u200E" + s + "\n";
            }

            Log.i("cc row", s);
        }
        Log.d("str", str);
        if(str!=""){
            insertCell(creditCard,str, Element.ALIGN_CENTER, 4, font);
        }
        document.add(headingTable);
        document.add(dateTable);
        document.add(orderDetailsTable);
        document.add(creditCard);

        document.close();
    }
    public static void  printClosingReport(Context context, String res) throws IOException, DocumentException, JSONException {
        List<CurrencyType> currencyTypesList = null;
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(context);
        currencyTypeDBAdapter.open();
        currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
        currencyTypeDBAdapter.close();
        JSONObject jsonObject = new JSONObject(res);
        Log.d("jsonObjectiii",jsonObject.toString());
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

        BaseFont urName = BaseFont.createFont("assets/arial.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 30);
        Font dateFont = new Font(urName, 24);
        //heading table
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);
        insertCell(headingTable,  SETTINGS.companyName , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.date) +":  "+new Timestamp(System.currentTimeMillis()), Element.ALIGN_CENTER, 1, font);
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

        insertCell(dateTable, currencyTypesList.get(0).getType(), Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, jsonObject.getDouble("actualFirstType")+"", Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, jsonObject.getDouble("expectedFirstType")+"", Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable,  jsonObject.getDouble("actualFirstType")-jsonObject.getDouble("expectedFirstType")+"", Element.ALIGN_LEFT, 1, dateFont);
        if (SETTINGS.enableCurrencies){

            insertCell(dateTable, currencyTypesList.get(1).getType(), Element.ALIGN_LEFT, 1, dateFont);
            insertCell(dateTable, jsonObject.getDouble("actualSecondType")+"", Element.ALIGN_LEFT, 1, dateFont);
            insertCell(dateTable, jsonObject.getDouble("expectedSecondType")+"", Element.ALIGN_LEFT, 1, dateFont);
            insertCell(dateTable,  jsonObject.getDouble("actualSecondType")-jsonObject.getDouble("expectedSecondType")+"", Element.ALIGN_LEFT, 1, dateFont);

            insertCell(dateTable, currencyTypesList.get(2).getType(), Element.ALIGN_LEFT, 1, dateFont);
            insertCell(dateTable, jsonObject.getDouble("actualThirdType")+"", Element.ALIGN_LEFT, 1, dateFont);
            insertCell(dateTable, jsonObject.getDouble("expectedTirdType")+"", Element.ALIGN_LEFT, 1, dateFont);
            insertCell(dateTable,  jsonObject.getDouble("actualThirdType")-jsonObject.getDouble("expectedTirdType")+"", Element.ALIGN_LEFT, 1, dateFont);

            insertCell(dateTable, currencyTypesList.get(3).getType(), Element.ALIGN_LEFT, 1, dateFont);
            insertCell(dateTable, jsonObject.getDouble("actualFourthType")+"", Element.ALIGN_LEFT, 1, dateFont);
            insertCell(dateTable, jsonObject.getDouble("expectedFourthType")+"", Element.ALIGN_LEFT, 1, dateFont);
            insertCell(dateTable,  jsonObject.getDouble("actualFourthType")-jsonObject.getDouble("expectedFourthType")+"", Element.ALIGN_LEFT, 1, dateFont);}
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
        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
            insertCell(headingTable,  context.getString(R.string.credit_invoice_doc_company_status) +" : "+jsonObject.getString("docNum") , Element.ALIGN_CENTER, 1, font);
        }else {
            insertCell(headingTable,  context.getString(R.string.credit_invoice_doc) +" : "+jsonObject.getString("docNum") , Element.ALIGN_CENTER, 1, font);}
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

        BaseFont urName = BaseFont.createFont("assets/arial.ttf", "Identity-H",true,BaseFont.EMBEDDED);
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
        String opiningReportDate= UtilitValidationDate.isValidDate(opiningReport.getCreatedAt());
        insertCell(headingTable, context.getString(R.string.date) +":  "+opiningReportDate, Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.cashiers) + SESSION._EMPLOYEE.getFullName(), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.opening_report) , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.amount) +":"+ opiningReport.getAmount()+SETTINGS.currencyCode, Element.ALIGN_CENTER, 1, font);

        insertCell(headingTable, "\n---------------------------" , Element.ALIGN_CENTER, 4, font);
        OpiningReportDetailsDBAdapter opiningReportDetailsDBAdapter = new OpiningReportDetailsDBAdapter(context);
        opiningReportDetailsDBAdapter.open();
        if(currencyAmount.size()>0) {
            for (int i = 0; i < currencyAmount.size(); i++) {
                if(currencyAmount.get(i)>0) {
                    insertCell(dataTable, context.getString(R.string.opening_report) + " : " + currencyAmount.get(i) + "  " + currencyTypeList.get(i), Element.ALIGN_LEFT, 2, dateFont);
                }
            }}else {
            insertCell(dataTable, context.getString(R.string.opening_report) + " : "+  opiningReport.getAmount()+"  "+SETTINGS.currencyCode, Element.ALIGN_LEFT, 2, dateFont);

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

        BaseFont urName = BaseFont.createFont("assets/arial.ttf", "Identity-H",true,BaseFont.EMBEDDED);
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
    public static void createSalesManReport(Context context  , List<CustomerAssistant>customerAssistantList,double totalAmount,Timestamp from,Timestamp to) throws IOException, DocumentException {
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

        BaseFont urName = BaseFont.createFont("assets/arial.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 30);
        //heading table
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);
        PdfPTable dateTable =new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);

        insertCell(headingTable,  SETTINGS.companyName , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.date) +":  "+new Timestamp(System.currentTimeMillis()), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.cashiers) + SESSION._EMPLOYEE.getFullName(), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.sales_man_report), Element.ALIGN_CENTER, 1, font);

        insertCell(dateTable, context.getString(R.string.from) +":  "+from, Element.ALIGN_CENTER, 1, font);
        insertCell(dateTable, context.getString(R.string.to) +":  "+to, Element.ALIGN_CENTER, 1, font);
        insertCell(dateTable, context.getString(R.string.total_amount) +":  "+totalAmount, Element.ALIGN_CENTER, 1, font);

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
        document.add(dateTable);
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
        BaseFont urName = BaseFont.createFont("assets/arial.ttf", "Identity-H",true,BaseFont.EMBEDDED);
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

        BaseFont urName1 = BaseFont.createFont("assets/arial.ttf", "Identity-H",true,BaseFont.EMBEDDED);
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
        if (text!=null){
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
            table.addCell(cell);}

    }
    public static void createXReport(Context context, XReport xReport,ZReportCount zReportCount) throws IOException, DocumentException {
        List<CurrencyType> currencyTypesList = null;
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(context);
        currencyTypeDBAdapter.open();
        currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
        currencyTypeDBAdapter.close();
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
        ZReportDBAdapter zReportDBAdapter =new ZReportDBAdapter(context);
        zReportDBAdapter.open();
        ZReport zReport= null;
        try {
            zReport = zReportDBAdapter.getLastRow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getCountForZReport(context,zReport);
        BaseFont urName = BaseFont.createFont("assets/arial.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 24);
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);
        insertCell(headingTable,  SETTINGS.companyName , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);
        String xReportDate= UtilitValidationDate.isValidDate(xReport.getCreatedAt());
        insertCell(headingTable, context.getString(R.string.date) +":  "+xReportDate, Element.ALIGN_CENTER, 1, font);
        EmployeeDBAdapter employeeDBAdapter = new EmployeeDBAdapter(context);
        employeeDBAdapter.open();
        insertCell(headingTable, context.getString(R.string.user_name)+":  " +employeeDBAdapter.getEmployeesName(xReport.getByUser()), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.x_report) + " " +xReport.getxReportId(), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable,"----------------------------", Element.ALIGN_CENTER, 1, font);



        PdfPTable dataTable = new PdfPTable(4);
        dataTable.deleteBodyRows();
        dataTable.setRunDirection(0);
        insertCell(dataTable, context.getString(R.string.total), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.details), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable, Util.makePrice(xReport.getInvoiceReceiptAmount()), Element.ALIGN_RIGHT,1, font);
        insertCell(dataTable,zReportCount.getInvoiceReceiptCount() + " ", Element.ALIGN_RIGHT, 1, font);
        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
            insertCell(dataTable, context.getString(R.string.invoice_recipte_company_status), Element.ALIGN_RIGHT, 2, font);
        }
        else {
            insertCell(dataTable, context.getString(R.string.invoice_receipt), Element.ALIGN_RIGHT, 2, font);}

        insertCell(dataTable,  Util.makePrice(xReport.getInvoiceAmount()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,zReportCount.getInvoiceCount() + " ", Element.ALIGN_RIGHT, 1, font);
        if(SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
            insertCell(dataTable, context.getString(R.string.invoice_company_status), Element.ALIGN_RIGHT,2, font);
        }
        else {
            insertCell(dataTable, context.getString(R.string.invoice), Element.ALIGN_RIGHT,2, font);
        }

        insertCell(dataTable, Util.makePrice( xReport.getCreditInvoiceAmount()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,zReportCount.getCreditCount() + " ", Element.ALIGN_RIGHT, 1, font);
        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
            insertCell(dataTable, context.getString(R.string.credit_invoice_doc_company_status), Element.ALIGN_RIGHT, 2, font);
        }else {
            insertCell(dataTable, context.getString(R.string.credit_invoice_doc), Element.ALIGN_RIGHT, 2, font);}

        insertCell(dataTable, Util.makePrice( xReport.getTotalSales() ), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,  "~", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.total_sales), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        insertCell(dataTable, context.getString(R.string.total), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.currency), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable,  Util.makePrice(xReport.getCashTotal() ), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,zReportCount.getCashCount() + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.cash), Element.ALIGN_RIGHT,2, font);

        insertCell(dataTable, Util.makePrice(xReport.getCheckTotal()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,zReportCount.getCheckCount()+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.checks), Element.ALIGN_RIGHT, 2, font);

        ///// CreditCard region
        insertCell(dataTable, Util.makePrice(xReport.getCreditTotal()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,zReportCount.getCreditCount()+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.credit_card), Element.ALIGN_RIGHT, 2, font);


        insertCell(dataTable,  Util.makePrice(xReport.getTotalAmount()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, "~", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.total_amount), Element.ALIGN_RIGHT, 2, font);


        insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        ///check region
        insertCell(dataTable, context.getString(R.string.total), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.currency), Element.ALIGN_RIGHT, 2, font);

        ///// firstType region
        insertCell(dataTable, Util.makePrice(xReport.getFirstTypeAmount()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,zReportCount.getFirstTYpeCount()+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, currencyTypesList.get(0).getType(), Element.ALIGN_RIGHT, 2, font);

        if (SETTINGS.enableCurrencies){
            ///// secondType region
            insertCell(dataTable, Util.makePrice(xReport.getSecondTypeAmount()), Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable,zReportCount.getSecondTypeCount()+" ", Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable, currencyTypesList.get(1).getType(), Element.ALIGN_RIGHT, 2, font);

            ///// thirdType region
            insertCell(dataTable, Util.makePrice(xReport.getThirdTypeAmount()), Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable,zReportCount.getThirdTypeCount()+" ", Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable,currencyTypesList.get(2).getType(), Element.ALIGN_RIGHT, 2, font);
            ///// fourthType region
            insertCell(dataTable, Util.makePrice(xReport.getFourthTypeAmount()), Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable,zReportCount.getFourthTypeCount()+" ", Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable, currencyTypesList.get(3).getType(), Element.ALIGN_RIGHT, 2, font);}


        if(checkList.size()>0){
            insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);
            insertCell(dataTable, context.getString(R.string.amount), Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable, context.getString(R.string.date), Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable, context.getString(R.string.checks), Element.ALIGN_RIGHT, 2, font);
            for(int i=0;i<checkList.size();i++){
                List<Check>list = checkList.get(i);

                for (int f =0;f<list.size();f++){
                    insertCell(dataTable,  Util.makePrice(list.get(f).getAmount()), Element.ALIGN_RIGHT, 1, font);
                    insertCell(dataTable, DateConverter.toDate(list.get(f).getCreatedAt())+" ", Element.ALIGN_RIGHT, 2, font);
                    insertCell(dataTable, list.get(f).getCheckNum()+" ", Element.ALIGN_RIGHT, 1, font);
                }
            }
            insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        }




        ////
        PdfPTable opiningReportTable = new PdfPTable(4);
        opiningReportTable.deleteBodyRows();
        opiningReportTable.setRunDirection(0);
        insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        insertCell(opiningReportTable, context.getString(R.string.opening_report), Element.ALIGN_RIGHT, 4, font);
        insertCell(opiningReportTable, opiningReportList.size()+" " , Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable,  Util.makePrice(aReportAmount), Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable, context.getString(R.string.amount), Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        PdfPTable pullAndDepositAmount = new PdfPTable(4);
        pullAndDepositAmount.deleteBodyRows();
        pullAndDepositAmount.setRunDirection(0);

        insertCell(pullAndDepositAmount, context.getString(R.string.pull_report), Element.ALIGN_RIGHT, 4, font);
        insertCell(pullAndDepositAmount,  Util.makePrice(zReport.getPullReportAmount()), Element.ALIGN_RIGHT, 2, font);
        insertCell(pullAndDepositAmount, context.getString(R.string.amount), Element.ALIGN_RIGHT, 2, font);
        insertCell(pullAndDepositAmount,"----------------------------", Element.ALIGN_CENTER, 4, font);
        insertCell(pullAndDepositAmount, context.getString(R.string.deposit_report), Element.ALIGN_RIGHT, 4, font);
        insertCell(pullAndDepositAmount,  Util.makePrice(zReport.getDepositReportAmount()), Element.ALIGN_RIGHT, 2, font);
        insertCell(pullAndDepositAmount, context.getString(R.string.amount), Element.ALIGN_RIGHT, 2, font);
        insertCell(pullAndDepositAmount,"----------------------------", Element.ALIGN_CENTER, 4, font);

        PdfPTable opiningReportDetailsTable = new PdfPTable(2);
        opiningReportDetailsTable.deleteBodyRows();
        opiningReportDetailsTable.setRunDirection(0);
        if(SETTINGS.enableCurrencies) {
            insertCell(opiningReportDetailsTable, context.getString(R.string.amount), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, context.getString(R.string.type), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, aReportDetailsForFirstCurrency + " ", Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, currencyTypesList.get(0).getType(), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, aReportDetailsForSecondCurrency + " ", Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, currencyTypesList.get(1).getType(), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, aReportDetailsForThirdCurrency + " ", Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, currencyTypesList.get(2).getType(), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, aReportDetailsForForthCurrency + " ", Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, currencyTypesList.get(3).getType(), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, "----------------------------", Element.ALIGN_CENTER, 2, font);
        }
        PdfPTable accountInformation = new PdfPTable(4);
        accountInformation.deleteBodyRows();
        accountInformation.setRunDirection(0);
        insertCell(accountInformation, context.getString(R.string.accounting_information), Element.ALIGN_RIGHT, 4, font);

        insertCell(accountInformation,Util.makePrice(xReport.getSalesWithTax())+ " ", Element.ALIGN_RIGHT, 2, font);
        insertCell(accountInformation, context.getString(R.string.Sales_With_tax), Element.ALIGN_RIGHT,2, font);

        insertCell(accountInformation,Util.makePrice(xReport.getTotalTax())+ " ", Element.ALIGN_RIGHT, 2, font);
        insertCell(accountInformation, context.getString(R.string.tax), Element.ALIGN_RIGHT,2, font);
        insertCell(accountInformation,Util.makePrice(xReport.getSalesBeforeTax())+ " ", Element.ALIGN_RIGHT, 2, font);
        insertCell(accountInformation, context.getString(R.string.Sales_Without_tax), Element.ALIGN_RIGHT,2, font);

        insertCell(accountInformation,Util.makePrice(zReport.getSalesWithTax()+zReport.getSalesBeforeTax() +zReport.getTotalTax())+ " ", Element.ALIGN_RIGHT, 2, font);
        insertCell(accountInformation, context.getString(R.string.total_price), Element.ALIGN_RIGHT,2, font);
        insertCell(accountInformation, "----------------------------", Element.ALIGN_CENTER, 4, font);



        PdfPTable posSalesTable = new PdfPTable(2);
        posSalesTable.deleteBodyRows();
        posSalesTable.setRunDirection(0);
        insertCell(posSalesTable, context.getString(R.string.pos_sales), Element.ALIGN_RIGHT, 1, font);
        insertCell(posSalesTable, Util.makePrice(xReport.getTotalPosSales()), Element.ALIGN_RIGHT, 1, font);
        //add table to document
        document.add(headingTable);
        document.add(dataTable);
        document.add(opiningReportTable);
        if(SETTINGS.enableCurrencies){
            document.add(opiningReportDetailsTable);

        }
        document.add(pullAndDepositAmount);
        document.add(accountInformation);

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
        invoiceReceiptCount=0 ;invoiceCount=0; CreditInvoiceCount=0 ; firstTypeCount=0 ;secondTypeCount=0 ;thirdTypeCount=0; fourthTypeCount=0 ;checkCount=0 ; creditCardCount=0 ;receiptInvoiceAmountCheck=0 ; cashCount=0;receiptInvoiceAmount=0;
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
        CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(context);
        cashPaymentDBAdapter.open();
        ChecksDBAdapter checksDBAdapter =new ChecksDBAdapter(context);
        checksDBAdapter.open();
        CreditCardPaymentDBAdapter creditCardPaymentDBAdapter =new CreditCardPaymentDBAdapter(context);
        creditCardPaymentDBAdapter.open();
        for (Payment p : payments) {
            long orderId = p.getOrderId();
            List<CashPayment>cashPaymentList=cashPaymentDBAdapter.getPaymentBySaleID(orderId);
            for(int i=0;i<cashPaymentList.size();i++){
                cashCount+=1;
                cashAmount+=p.getAmount();
            }
            List<Check>checkList=checksDBAdapter.getPaymentBySaleID(orderId);
            for(int i=0;i<checkList.size();i++){
                checkCount+=1;
            }
            List<CreditCardPayment>creditCardPayments=creditCardPaymentDBAdapter.getPaymentByOrderID(orderId);
            for(int i=0;i<creditCardPayments.size();i++){
                creditCardCount+=1;            }
            /*int i = 0;
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
            }*/
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
            List<CurrencyType> currencyTypesList = null;
            CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(context);
            currencyTypeDBAdapter.open();
            currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
            currencyTypeDBAdapter.close();
            List<CurrencyOperation>currencyOperationList=currencyOperationPaymentList(orderDb.getBetween(x.getStartOrderId(),x.getEndOrderId()),context);
            for (CurrencyOperation cp : currencyOperationList) {
              /*  switch (cp.getCurrencyType()) {
                    case "ILS":
                        firstTypeCount+=1;
                        break;
                    case "USD":
                        secondTypeCount+=1;
                        break;
                    case "EUR":
                        thirdTypeCount+=1;

                        break;
                    case "GBP":
                        fourthTypeCount+=1;
                        break;
                }*/

                if (cp.getCurrencyType().equals(currencyTypesList.get(0).getType())){

                    firstTypeCount+=1;
                }
                else   if (cp.getCurrencyType().equals(currencyTypesList.get(1).getType())){
                    secondTypeCount+=1;
                }
                else   if (cp.getCurrencyType().equals(currencyTypesList.get(2).getType())){
                    thirdTypeCount+=1;
                }
                else if (cp.getCurrencyType().equals(currencyTypesList.get(3).getType())){
                    fourthTypeCount+=1;
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
        firstTypeCount+=receiptInvoiceAmount;
        checkCount+=receiptInvoiceAmountCheck;

    }
    public static void createMonthZReport( Context context,ZReport zReport,Date from ,Date to,ZReportCount zReportCount) throws IOException, DocumentException {
        List<CurrencyType> currencyTypesList = null;
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(context);
        currencyTypeDBAdapter.open();
        currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
        currencyTypeDBAdapter.close();
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
        BaseFont urName = BaseFont.createFont("assets/arial.ttf", "Identity-H",true,BaseFont.EMBEDDED);
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

        insertCell(dataTable, Util.makePrice(zReport.getInvoiceReceiptAmount()) + " ", Element.ALIGN_RIGHT,1, font);
        insertCell(dataTable,zReportCount.getInvoiceReceiptCount() + " ", Element.ALIGN_RIGHT, 1, font);
        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
            insertCell(dataTable, context.getString(R.string.invoice_recipte_company_status), Element.ALIGN_RIGHT, 2, font);
        }
        else {
            insertCell(dataTable, context.getString(R.string.invoice_receipt), Element.ALIGN_RIGHT, 2, font);}

        insertCell(dataTable, Util.makePrice(zReport.getInvoiceAmount())+ " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,zReportCount.getInvoiceCount() + " ", Element.ALIGN_RIGHT, 1, font);
        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
            insertCell(dataTable, context.getString(R.string.invoice_company_status), Element.ALIGN_RIGHT,2, font);
        }
        else {
            insertCell(dataTable, context.getString(R.string.invoice), Element.ALIGN_RIGHT, 2, font);
        }
        insertCell(dataTable,Util.makePrice( zReport.getCreditInvoiceAmount()) + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,zReportCount.getCreditInvoiceCount() + " ", Element.ALIGN_RIGHT, 1, font);
        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
            insertCell(dataTable, context.getString(R.string.credit_invoice_doc_company_status), Element.ALIGN_RIGHT, 2, font);
        }else {
            insertCell(dataTable, context.getString(R.string.credit_invoice_doc), Element.ALIGN_RIGHT, 2, font);
        }
        insertCell(dataTable, Util.makePrice(zReport.getTotalSales()) + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,  "~", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.total_sales), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        insertCell(dataTable, context.getString(R.string.total), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.method), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable,Util.makePrice(zReport.getCashTotal()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,zReportCount.getCashCount() + " ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.cash), Element.ALIGN_RIGHT,2, font);

        insertCell(dataTable, Util.makePrice(zReport.getCreditTotal()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,zReportCount.getCreditCount()+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.credit_card), Element.ALIGN_RIGHT, 2, font);


        insertCell(dataTable, Util.makePrice(zReport.getCheckTotal()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,zReportCount.getCheckCount()+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.checks), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable, Util.makePrice(zReport.getTotalAmount())+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, "~", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.total_amount), Element.ALIGN_RIGHT, 2, font);

        insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);


        insertCell(dataTable, context.getString(R.string.total), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, context.getString(R.string.currency), Element.ALIGN_RIGHT, 2, font);

        ///// firstType region
        insertCell(dataTable, Util.makePrice(zReport.getFirstTypeAmount()), Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable,zReportCount.getFirstTYpeCount()+" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(dataTable, currencyTypesList.get(0).getType(), Element.ALIGN_RIGHT, 2, font);
        if (SETTINGS.enableCurrencies && currencyTypesList.size()>1){

            ///// secondType region
            insertCell(dataTable, Util.makePrice(zReport.getSecondTypeAmount()), Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable,zReportCount.getSecondTypeCount()+" ", Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable, currencyTypesList.get(1).getType(), Element.ALIGN_RIGHT, 2, font);

            ///// third region
            insertCell(dataTable, Util.makePrice(zReport.getThirdTypeAmount()), Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable,zReportCount.getThirdTypeCount()+" ", Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable, currencyTypesList.get(2).getType(), Element.ALIGN_RIGHT, 2, font);
            ///// fourth region
            insertCell(dataTable, Util.makePrice(zReport.getFourthTypeAmount()), Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable,zReportCount.getFourthTypeCount()+" ", Element.ALIGN_RIGHT, 1, font);
            insertCell(dataTable, currencyTypesList.get(3).getType(), Element.ALIGN_RIGHT, 2, font);}





        insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);
        if(checkList.size()>0){
            //  insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);
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




        ////
       /* PdfPTable opiningReportTable = new PdfPTable(4);
        opiningReportTable.deleteBodyRows();
        opiningReportTable.setRunDirection(0);
        insertCell(dataTable,"----------------------------", Element.ALIGN_CENTER, 4, font);

        insertCell(opiningReportTable, context.getString(R.string.opening_report), Element.ALIGN_RIGHT, 4, font);
        insertCell(opiningReportTable, opiningReportList.size()+" " , Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable, context.getString(R.string.count), Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable, aReportAmount +" ", Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable, context.getString(R.string.amount), Element.ALIGN_RIGHT, 1, font);
        insertCell(opiningReportTable,"----------------------------", Element.ALIGN_CENTER, 4, font);*/

/* PdfPTable opiningReportDetailsTable = new PdfPTable(2);
        opiningReportDetailsTable.deleteBodyRows();
        opiningReportDetailsTable.setRunDirection(0);
        if(SETTINGS.enableCurrencies) {
            insertCell(opiningReportDetailsTable, context.getString(R.string.amount), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, context.getString(R.string.type), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, aReportDetailsForFirstCurrency + " ", Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, currencyTypesList.get(0).getType(), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, aReportDetailsForSecondCurrency + " ", Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable,currencyTypesList.get(1).getType() , Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, aReportDetailsForThirdCurrency + " ", Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, currencyTypesList.get(2).getType(), Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, aReportDetailsForForthCurrency + " ", Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable,currencyTypesList.get(3).getType() , Element.ALIGN_RIGHT, 1, font);
            insertCell(opiningReportDetailsTable, "----------------------------", Element.ALIGN_CENTER, 2, font);
        }*/

        PdfPTable accountInformation = new PdfPTable(4);
        accountInformation.deleteBodyRows();
        accountInformation.setRunDirection(0);
        insertCell(accountInformation, context.getString(R.string.accounting_information), Element.ALIGN_RIGHT, 4, font);

        insertCell(accountInformation,Util.makePrice(zReport.getSalesWithTax())+ " ", Element.ALIGN_RIGHT, 2, font);
        insertCell(accountInformation, context.getString(R.string.Sales_With_tax), Element.ALIGN_RIGHT,2, font);


        insertCell(accountInformation,Util.makePrice(zReport.getTotalTax())+ " ", Element.ALIGN_RIGHT, 2, font);
        insertCell(accountInformation, context.getString(R.string.tax), Element.ALIGN_RIGHT,2, font);



        insertCell(accountInformation,Util.makePrice(zReport.getSalesBeforeTax())+ " ", Element.ALIGN_RIGHT, 2, font);
        insertCell(accountInformation, context.getString(R.string.Sales_Without_tax), Element.ALIGN_RIGHT,2, font);


        insertCell(accountInformation,Util.makePrice(zReport.getSalesWithTax()+zReport.getSalesBeforeTax()+zReport.getTotalTax()) + " ", Element.ALIGN_RIGHT, 2, font);
        insertCell(accountInformation, context.getString(R.string.total_price), Element.ALIGN_RIGHT,2, font);
        insertCell(accountInformation, "----------------------------", Element.ALIGN_CENTER, 4, font);

        PdfPTable posSalesTable = new PdfPTable(2);
        posSalesTable.deleteBodyRows();
        posSalesTable.setRunDirection(0);
        insertCell(posSalesTable, context.getString(R.string.pos_sales), Element.ALIGN_RIGHT, 1, font);
        insertCell(posSalesTable,Util.makePrice(zReport.getTotalPosSales()) +" ", Element.ALIGN_RIGHT, 1, font);
        //add table to document
        document.add(headingTable);
        document.add(dataTable);
        //document.add(opiningReportTable);
        document.add(accountInformation);
      /*  if(SETTINGS.enableCurrencies){
            document.add(opiningReportDetailsTable);

        }
 */
        document.add(posSalesTable);
        document.close();
        //end :)


    }
    public static void createNormalInvoiceForCopy(Context context  , List<OrderDetails>orderDetailsList, Order order,boolean isCopy,String mainMer) throws IOException, DocumentException {
        order=SESSION._TEMP_ORDERS_COPY;
        orderDetailsList=SESSION._TEMP_ORDER_DETAILES_COPY;

        CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(context);
        cashPaymentDBAdapter.open();
        ChecksDBAdapter checksDBAdapter =new ChecksDBAdapter(context);
        checksDBAdapter.open();
        CreditCardPaymentDBAdapter creditCardPaymentDBAdapter =new CreditCardPaymentDBAdapter(context);
        creditCardPaymentDBAdapter.open();
        double totalPrice =0.0;
        double saleTotalPrice=0.0;
        double totalSaved=0.0;
        int count=0;
        String customerName="";





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
        BaseFont urName = BaseFont.createFont("assets/arial.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 26);
        BaseFont urName1 = BaseFont.createFont("assets/arial.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font urFontName1 = new Font(urName1, 22);
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);

        insertCell(headingTable,  SETTINGS.companyName , Element.ALIGN_CENTER, 1, urFontName1);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, urFontName1);
        insertCell(headingTable, context.getString(R.string.date) +":  "+new Timestamp(System.currentTimeMillis()), Element.ALIGN_CENTER, 1, urFontName1);
        try {
            if (order.getCustomer_name() == null) {
                customerName = context.getString(R.string.general_customer);
            } else if (order.getCustomer_name().equals("")) {
                customerName = context.getString(R.string.general_customer);
            } else {
                if(order.getCustomer().getFirstName()!=null&&order.getCustomer().getLastName()!=null){
                    customerName = order.getCustomer().getFirstName()+" "+order.getCustomer().getLastName();
                }else {
                    customerName = order.getCustomer_name();

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        insertCell(headingTable, context.getString(R.string.customer_name)+":  " + customerName, Element.ALIGN_CENTER, 1, urFontName1);
        if(isCopy) {
            insertCell(headingTable, context.getString(R.string.copyinvoice), Element.ALIGN_CENTER, 1, urFontName1);
        }else {
            insertCell(headingTable, context.getString(R.string.source_invoice), Element.ALIGN_CENTER, 1, urFontName1);
        }

        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
            insertCell(headingTable, context.getString(R.string.invoice_recipte_company_status)+": " +String.format(" %06d ", order.getOrderId()), Element.ALIGN_CENTER, 1, urFontName1);
        }
        else {
            insertCell(headingTable, context.getString(R.string.invoice_receipt)+": " +String.format(" %06d ", order.getOrderId()), Element.ALIGN_CENTER, 1, urFontName1);}
        insertCell(headingTable, "\n\n\n---------------------------" , Element.ALIGN_CENTER, 4, urFontName1);


        PdfPTable table = new PdfPTable(9);
        PdfPTable currencyTable = new PdfPTable(3);
        PdfPTable paidByTable = new PdfPTable(4);
        PdfPTable checkTable = new PdfPTable(3);
        PdfPTable creditCard = new PdfPTable(4);
        Font urFontName = new Font(urName1, 24);
        /*BaseFont boldFont = BaseFont.createFont("assets/arial.ttf", "Times-Bold",true,BaseFont.EMBEDDED);
        Font fontbld = new Font(boldFont, 24);*/
        table.deleteBodyRows();
        table.setRunDirection(0);
        currencyTable.deleteBodyRows();
        currencyTable.setRunDirection(0);
        paidByTable.deleteBodyRows();
        paidByTable.setRunDirection(0);
        creditCard.deleteBodyRows();
        creditCard.setRunDirection(0);
        checkTable.deleteBodyRows();
        checkTable.setRunDirection(0);



        String languageApp=getLanguageApp();
        if (languageApp.equals("ar")){
            table.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        }
        else {
            table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        }
        //insert column headings;
        insertCell(table, "%", Element.ALIGN_CENTER, 1, urFontName1);
        insertCell(table, context.getString(R.string.total), Element.ALIGN_CENTER, 2, urFontName1);
        insertCell(table, context.getString(R.string.price), Element.ALIGN_CENTER, 2, urFontName1);
        insertCell(table, context.getString(R.string.qty), Element.ALIGN_CENTER, 1, urFontName1);
        insertCell(table, context.getString(R.string.product), Element.ALIGN_CENTER, 3, urFontName1);

        double price_before_tax=0;
        ProductDBAdapter productDBAdapter = new ProductDBAdapter(context);
        for (int i=0;i<orderDetailsList.size();i++){
            productDBAdapter.open();
            price_before_tax+=orderDetailsList.get(i).getPaidAmountAfterTax();
            insertCell(table, "  " + Util.makePrice(orderDetailsList.get(i).getDiscount()), Element.ALIGN_CENTER, 1, urFontName); // insert date value
            insertCell(table, "  " + Util.makePrice(orderDetailsList.get(i).getItemTotalPrice()), Element.ALIGN_CENTER, 2, urFontName); // insert date value
            insertCell(table,"  "+ orderDetailsList.get(i).getUnitPrice(), Element.ALIGN_CENTER, 2, urFontName); // insert date value
            insertCell(table, orderDetailsList.get(i).getQuantity()+"", Element.ALIGN_CENTER, 1, urFontName); // insert date value

            Product product =productDBAdapter.getProductByID(orderDetailsList.get(i).getProductId());
            orderDetailsList.get(i).setProduct(product);
            if (orderDetailsList.get(i).getProduct().getDisplayName().equals("General"))
                orderDetailsList.get(i).getProduct().setProductCode(context.getString(R.string.general));
            insertCell(table, orderDetailsList.get(i).getProduct().getDisplayName(), Element.ALIGN_CENTER, 3, urFontName); // insert date value
            totalPrice+=orderDetailsList.get(i).getItemTotalPrice();

            count+=orderDetailsList.get(i).getQuantity();
            if (orderDetailsList.get(i).getProductSerialNumber()>0) {
                insertCell(table, context.getString(R.string.serial_no) + " : " + orderDetailsList.get(i).getProductSerialNumber(), Element.ALIGN_LEFT, 9, urFontName); // insert date value
            }
            productDBAdapter.close();

        }

        insertCell(table, "\n\n\n---------------------------" , Element.ALIGN_CENTER, 9, urFontName);
        insertCell(table,Util.makePrice(count)  ,Element.ALIGN_CENTER, 4, urFontName);
        insertCell(table, context.getString(R.string.product_quantity)  ,Element.ALIGN_CENTER, 5, urFontName);



        if (order.cartDiscount !=0) {
            insertCell(table, Util.makePrice(order.getTotalPrice() * 100 / (100 - order.cartDiscount)), Element.ALIGN_CENTER, 4, urFontName);
            insertCell(table, context.getString(R.string.price_before_discount), Element.ALIGN_CENTER, 5, urFontName);

            insertCell(table, Util.makePrice(order.cartDiscount), Element.ALIGN_CENTER, 4, urFontName);
            insertCell(table, context.getString(R.string.discount), Element.ALIGN_CENTER, 5, urFontName);

        }
        double totalPriceAfterDiscount= totalPrice- (totalPrice * (order.cartDiscount/100));
        Log.d("order.cartDiscount",totalPrice+"");
        insertCell(table,Util.makePrice(totalPriceAfterDiscount)  , Element.ALIGN_CENTER, 4, urFontName);
        insertCell(table, context.getString(R.string.total_price)  , Element.ALIGN_CENTER, 5, urFontName);


        double noTax =price_before_tax - (price_before_tax * (order.cartDiscount/100));
        Log.d("order.cartDiscount",noTax+"");
        insertCell(table,Util.makePrice(totalPriceAfterDiscount - price_before_tax) , Element.ALIGN_CENTER, 4, urFontName);
        insertCell(table, context.getString(R.string.tax) +" : "+Util.makePrice(SETTINGS.tax)+" "+"%" , Element.ALIGN_CENTER, 5, urFontName);


        insertCell(table, Util.makePrice(price_before_tax) , Element.ALIGN_CENTER,4, urFontName);
        insertCell(table, context.getString(R.string.price_before_tax) , Element.ALIGN_CENTER,5, urFontName);





        insertCell(table, "\n\n\n---------------------------" , Element.ALIGN_CENTER, 9, urFontName);

        insertCell(paidByTable,context.getString(R.string.paid_by), Element.ALIGN_RIGHT, 4, urFontName);
        insertCell(paidByTable,context.getString(R.string.rest), Element.ALIGN_CENTER, 1, urFontName);
        insertCell(paidByTable,context.getString(R.string.given), Element.ALIGN_CENTER, 1, urFontName);
        insertCell(paidByTable,context.getString(R.string.total), Element.ALIGN_CENTER, 1, urFontName);
        insertCell(paidByTable,context.getString(R.string.payment), Element.ALIGN_CENTER, 1, urFontName);
        double cash_plus = 0;
        double check_plus = 0;
        double creditCard_plus = 0;
        List<CashPayment>cashPaymentList=cashPaymentDBAdapter.getPaymentBySaleID(order.getOrderId());
        for(int i=0;i<cashPaymentList.size();i++){
            cash_plus+=cashPaymentList.get(i).getAmount()*cashPaymentList.get(i).getCurrencyRate();

        }
        List<Check>checkList=checksDBAdapter.getPaymentBySaleID(order.getOrderId());
        for(int i=0;i<checkList.size();i++){
            check_plus+=checkList.get(i).getAmount();
        }
        List<CreditCardPayment>creditCardPayments=creditCardPaymentDBAdapter.getPaymentByOrderID(order.getOrderId());
        for(int i=0;i<creditCardPayments.size();i++){
            creditCard_plus+=creditCardPayments.get(i).getAmount();
        }
        if(cashPaymentList.size()>0){
            insertCell(paidByTable,Util.makePrice(order.getTotalPrice()-cash_plus), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(paidByTable,Util.makePrice(cash_plus), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(paidByTable,Util.makePrice(order.getTotalPrice()), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(paidByTable,CONSTANT.CASH, Element.ALIGN_CENTER, 1, urFontName);
        }
        if(checkList.size()>0){
            insertCell(paidByTable,Util.makePrice(order.getTotalPrice()-check_plus), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(paidByTable,Util.makePrice(check_plus), Element.ALIGN_CENTER, 1, font);
            insertCell(paidByTable,Util.makePrice(order.getTotalPrice()), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(paidByTable,CONSTANT.CHECKS, Element.ALIGN_CENTER, 1, font);
        }
        if(creditCardPayments.size()>0){
            insertCell(paidByTable,Util.makePrice(order.getTotalPrice()-creditCard_plus), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(paidByTable,Util.makePrice(creditCard_plus), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(paidByTable,Util.makePrice(order.getTotalPrice()), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(paidByTable,CONSTANT.CREDIT_CARD, Element.ALIGN_CENTER, 1, urFontName);
        }
        insertCell(paidByTable, "\n\n\n---------------------------" , Element.ALIGN_CENTER, 4, urFontName);


        List<CurrencyType> currencyTypesList = null;
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(context);
        currencyTypeDBAdapter.open();
        currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
        currencyTypeDBAdapter.close();
        insertCell(currencyTable, context.getString(R.string.currency), Element.ALIGN_RIGHT, 3, urFontName);
        insertCell(currencyTable, context.getString(R.string.returned), Element.ALIGN_CENTER, 1, urFontName);
        insertCell(currencyTable, context.getString(R.string.given), Element.ALIGN_CENTER, 1, urFontName);
        insertCell(currencyTable, context.getString(R.string.type), Element.ALIGN_CENTER, 1, urFontName);
        double firstTypePaid = 0, firstTypeReturn = 0, secondTypePaid = 0, secondTypeReturn = 0, thirdTypePaid = 0, thirdTypeReturn = 0, fourthTypePaid = 0, fourthReturn = 0;
        CurrencyOperationDBAdapter currencyOperationDBAdapter = new CurrencyOperationDBAdapter(context);
        currencyOperationDBAdapter.open();
        List<CurrencyOperation> currencyOperationList = currencyOperationDBAdapter.getCurrencyOperationByOrderID(order.getOrderId());
        CurrencyReturnsDBAdapter currencyReturnDBAdapter = new CurrencyReturnsDBAdapter(context);
        currencyReturnDBAdapter.open();



        List<CurrencyReturns> currencyReturnsList = currencyReturnDBAdapter.getCurencyReturnBySaleID(order.getOrderId());
        for (int i = 0; i < currencyOperationList.size(); i++) {
            if (currencyOperationList.get(i).getPaymentWay().equalsIgnoreCase(CONSTANT.CASH)) {
                if (currencyOperationList.get(i).getCurrencyType().equals(currencyTypesList.get(0).getType()) ) {
                    firstTypePaid += currencyOperationList.get(i).getAmount();
                } if(SETTINGS.enableCurrencies) {
                    if (currencyOperationList.get(i).getCurrencyType().equals(currencyTypesList.get(1).getType())) {
                        secondTypePaid += currencyOperationList.get(i).getAmount();
                    } else if (currencyOperationList.get(i).getCurrencyType().equals(currencyTypesList.get(2).getType())) {
                        thirdTypePaid += currencyOperationList.get(i).getAmount();
                    } else if (currencyOperationList.get(i).getCurrencyType().equals(currencyTypesList.get(3).getType())) {
                        fourthTypePaid += currencyOperationList.get(i).getAmount();
                    }
                }}

        }
        for (int i = 0; i < currencyReturnsList.size(); i++) {
            if (currencyReturnsList.get(i).getCurrency_type() == 0) {
                firstTypeReturn += currencyReturnsList.get(i).getAmount();
            } if(SETTINGS.enableCurrencies) {
                if (currencyReturnsList.get(i).getCurrency_type() == 1) {
                    secondTypeReturn += currencyReturnsList.get(i).getAmount();
                } else if (currencyReturnsList.get(i).getCurrency_type() == 2) {
                    thirdTypeReturn += currencyReturnsList.get(i).getAmount();
                } else if (currencyReturnsList.get(i).getCurrency_type() == 3) {
                    fourthReturn += currencyReturnsList.get(i).getAmount();
                }}

        }
        if (firstTypePaid > 0 || firstTypeReturn > 0) {
            insertCell(currencyTable, Util.makePrice(firstTypeReturn), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(currencyTable,  Util.makePrice(firstTypePaid), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(currencyTable, currencyTypesList.get(0).getType(), Element.ALIGN_CENTER, 1, urFontName);
        }
        if(SETTINGS.enableCurrencies) {
            if (secondTypePaid > 0 || secondTypeReturn > 0) {
                insertCell(currencyTable, Util.makePrice(secondTypeReturn), Element.ALIGN_CENTER, 1, urFontName);
                insertCell(currencyTable,  Util.makePrice(secondTypePaid), Element.ALIGN_CENTER, 1, urFontName);
                insertCell(currencyTable, currencyTypesList.get(1).getType(), Element.ALIGN_CENTER, 1, urFontName);
            }
            if (thirdTypePaid > 0 || thirdTypeReturn > 0) {
                insertCell(currencyTable, Util.makePrice(thirdTypeReturn), Element.ALIGN_CENTER, 1, urFontName);
                insertCell(currencyTable,  Util.makePrice(thirdTypePaid), Element.ALIGN_CENTER, 1, urFontName);
                insertCell(currencyTable, currencyTypesList.get(2).getType(), Element.ALIGN_CENTER, 1, urFontName);
            }
            if (fourthTypePaid > 0 || fourthReturn > 0) {
                insertCell(currencyTable, Util.makePrice(fourthReturn), Element.ALIGN_CENTER, 1, urFontName);
                insertCell(currencyTable,  Util.makePrice(fourthTypePaid), Element.ALIGN_CENTER, 1, urFontName);
                insertCell(currencyTable, currencyTypesList.get(3).getType(), Element.ALIGN_CENTER, 1, urFontName);
            }}
        insertCell(currencyTable, "\n\n\n---------------------------" , Element.ALIGN_CENTER, 3, urFontName);


        if(checkList!=null){
            insertCell(checkTable, context.getString(R.string.amount), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(checkTable, context.getString(R.string.date), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(checkTable, context.getString(R.string.checks), Element.ALIGN_CENTER, 1, urFontName);
            for(int i=0;i<checkList.size();i++){
                insertCell(checkTable,Util.makePrice(checkList.get(i).getAmount()), Element.ALIGN_CENTER, 1, urFontName);
                insertCell(checkTable, DateConverter.toDate(new Date(checkList.get(i).getCreatedAt().getTime())), Element.ALIGN_CENTER, 1, urFontName);
                insertCell(checkTable, checkList.get(i).getCheckNum()+"", Element.ALIGN_CENTER, 1, urFontName);
            }

        }

        String str = "";
        for (String s : mainMer.split("\n")) {
            if(!s.replaceAll(" ","").equals("")) {
                if(s.contains("שם מסוף")){
                    continue;
                }
                else if(s.contains("מספר מסוף")){
                    continue;
                }
                else if(s.contains("גרסת תוכנה")){
                    continue;
                }
                else if(s.contains("מספר עסק בחברת האשראי")){
                    continue;
                }
                else if(s.contains("Powered")){
                    continue;
                }
                else if(s.contains("Powered")){
                    continue;
                }
                if(s.contains("מספר כרטיס")){
                    if(s.split("\\s+")[1].length()>4){
                        String head = "מספר כרטיס";
                        String ss = "";
                        for(int i=0;i<s.split("\\s+")[1].length()-4;i++) {
                            ss += "*";
                        }
                        ss += s.split("\\s+")[1].substring(s.split("\\s+")[1].length() - 4, s.split("\\s+")[1].length());

                        str += "\u200E" + ss + "\t" + head + "\n";
                        continue;
                    }
                }
                str += "\u200E" + s + "\n";
            }
            Log.i("cc row", s);
        }
        if(str!=""){
            insertCell(creditCard,str, Element.ALIGN_CENTER, 4, urFontName);

        }

        PdfPTable endOfInvoice = new PdfPTable(2);
        endOfInvoice.deleteBodyRows();
        endOfInvoice.setRunDirection(0);
        insertCell(endOfInvoice,context.getString(R.string.cashier)+":",Element.ALIGN_CENTER,1,urFontName);
        insertCell(endOfInvoice,SESSION._EMPLOYEE.getEmployeeName(),Element.ALIGN_CENTER,1,urFontName);

        insertCell(endOfInvoice, " ", Element.ALIGN_CENTER,2, urFontName);
        if(order.getCustomer()!=null) {

            if (order.getCustomer().getCustomerType().toString().equalsIgnoreCase(CustomerType.CREDIT.getValue())) {
                insertCell(endOfInvoice, context.getString(R.string.customer_ledger) + ":", Element.ALIGN_CENTER, 1, urFontName);

                insertCell(endOfInvoice, SESSION._TEMP_ORDERS_COPY.CustomerLedger + "", Element.ALIGN_CENTER, 1, urFontName);
            }
        }
        insertCell(endOfInvoice, " ", Element.ALIGN_CENTER,2, urFontName);

        insertCell(endOfInvoice, context.getString(R.string.date) + ":", Element.ALIGN_CENTER, 1, urFontName);

        insertCell(endOfInvoice,  DateConverter.DateToString(order.getCreatedAt())+"", Element.ALIGN_CENTER, 1, urFontName);
        insertCell(endOfInvoice, " ", Element.ALIGN_CENTER,2, urFontName);

        if(isCopy) {
            insertCell(endOfInvoice, context.getString(R.string.copy_date) + ":", Element.ALIGN_CENTER, 1, urFontName);

            insertCell(endOfInvoice, DateConverter.currentDateTime() + "", Element.ALIGN_CENTER, 1, urFontName);
            insertCell(endOfInvoice, " ", Element.ALIGN_CENTER,2, urFontName);

        }

        String s = SETTINGS.currencySymbol;
        insertCell(endOfInvoice, context.getString(R.string.total_saved)+ " :", Element.ALIGN_CENTER, 1, urFontName);

        insertCell(endOfInvoice,  String.format(new Locale("en"), "%.2f %s", order.getTotalSaved(), s), Element.ALIGN_CENTER, 1, urFontName);
        insertCell(endOfInvoice, " ", Element.ALIGN_CENTER,2, urFontName);

        insertCell(endOfInvoice, SETTINGS.returnNote, Element.ALIGN_CENTER, 1, urFontName);

        insertCell(endOfInvoice,  "", Element.ALIGN_CENTER, 1, urFontName);
        //add table to document
        document.add(headingTable);
        document.add(table);
        document.add(paidByTable);
        document.add(currencyTable);
        if(checkList.size()>0)
        {
            document.add(checkTable);
        }
        if(str.length()>0)
        {
            document.add(creditCard);
        }

        document.add(endOfInvoice);

        document.close();
    }
    public static void createNormalInvoice(Context context  , List<OrderDetails>orderDetailsList, Order order,boolean isCopy,String mainMer) throws IOException, DocumentException {
        order=SESSION._TEMP_ORDERS;
        orderDetailsList=SESSION._TEMP_ORDER_DETAILES;
        Log.d("mainMer",mainMer);
        Log.d("orderDetailsListPdfUA", String.valueOf(SESSION._TEMP_ORDER_DETAILES));
        Log.d("orderPdfUA", String.valueOf(SESSION._TEMP_ORDERS));
        CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(context);
        cashPaymentDBAdapter.open();
        ChecksDBAdapter checksDBAdapter =new ChecksDBAdapter(context);
        checksDBAdapter.open();
        CreditCardPaymentDBAdapter creditCardPaymentDBAdapter =new CreditCardPaymentDBAdapter(context);
        creditCardPaymentDBAdapter.open();
        double totalPrice =0.0;
        double saleTotalPrice=0.0;
        double totalSaved=0.0;
        int count=0;
        String customerName="";



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
        BaseFont urName = BaseFont.createFont("assets/arial.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 26);
        BaseFont urName1 = BaseFont.createFont("assets/arial.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font urFontName1 = new Font(urName1, 22);
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);

        insertCell(headingTable,  SETTINGS.companyName , Element.ALIGN_CENTER, 1, urFontName1);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, urFontName1);
        insertCell(headingTable, context.getString(R.string.date) +":  "+new Timestamp(System.currentTimeMillis()), Element.ALIGN_CENTER, 1, urFontName1);
        try {
            if (order.getCustomer_name() == null) {
                customerName = context.getString(R.string.general_customer);
            } else if (order.getCustomer_name().equals("")) {
                customerName = context.getString(R.string.general_customer);
            } else {
                if(order.getCustomer().getFirstName()!=null&&order.getCustomer().getLastName()!=null){
                    customerName = order.getCustomer().getFirstName()+" "+order.getCustomer().getLastName();
                }else {
                    customerName = order.getCustomer_name();

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        insertCell(headingTable, context.getString(R.string.customer_name)+":  " + customerName, Element.ALIGN_CENTER, 1, urFontName1);
        if(isCopy) {
            insertCell(headingTable, context.getString(R.string.copyinvoice), Element.ALIGN_CENTER, 1, urFontName1);
        }else {
            insertCell(headingTable, context.getString(R.string.source_invoice), Element.ALIGN_CENTER, 1, urFontName1);
        }
        Log.d("CompanySettingPDF",SETTINGS.company.name());
        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
            insertCell(headingTable, context.getString(R.string.invoice_recipte_company_status)+": " +String.format(" %06d ", order.getOrderId()), Element.ALIGN_CENTER, 1, urFontName1);
        }
        else {
            insertCell(headingTable, context.getString(R.string.invoice_receipt)+": " +String.format(" %06d ", order.getOrderId()), Element.ALIGN_CENTER, 1, urFontName1);}
        insertCell(headingTable, "\n\n\n---------------------------" , Element.ALIGN_CENTER, 4, urFontName1);


        PdfPTable table = new PdfPTable(9);
        PdfPTable currencyTable = new PdfPTable(3);
        PdfPTable paidByTable = new PdfPTable(4);
        PdfPTable checkTable = new PdfPTable(3);
        PdfPTable creditCard = new PdfPTable(4);
        Font urFontName = new Font(urName1, 24);
        table.deleteBodyRows();
        table.setRunDirection(0);
        currencyTable.deleteBodyRows();
        currencyTable.setRunDirection(0);
        paidByTable.deleteBodyRows();
        paidByTable.setRunDirection(0);
        creditCard.deleteBodyRows();
        creditCard.setRunDirection(0);
        checkTable.deleteBodyRows();
        checkTable.setRunDirection(0);

        String languageApp=getLanguageApp();
        if (languageApp.equals("ar")){
            table.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        }
        else {
            table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        }


        //insert column headings;
        insertCell(table, "%", Element.ALIGN_CENTER, 1, urFontName1);
        insertCell(table, context.getString(R.string.total), Element.ALIGN_CENTER, 2, urFontName1);
        insertCell(table, context.getString(R.string.price), Element.ALIGN_CENTER, 2, urFontName1);
        insertCell(table, context.getString(R.string.qty), Element.ALIGN_CENTER, 1, urFontName1);
        insertCell(table, context.getString(R.string.product), Element.ALIGN_CENTER, 3, urFontName1);

        double price_before_tax=0;
        ProductDBAdapter productDBAdapter = new ProductDBAdapter(context);
        for (int i=0;i<orderDetailsList.size();i++){
            productDBAdapter.open();
            price_before_tax+=orderDetailsList.get(i).getPaidAmountAfterTax();
            insertCell(table, "  " + Util.makePrice(orderDetailsList.get(i).getDiscount()), Element.ALIGN_CENTER, 1, urFontName); // insert date value
            insertCell(table, "  " + Util.makePrice(orderDetailsList.get(i).getItemTotalPrice()), Element.ALIGN_CENTER, 2, urFontName); // insert date value
            insertCell(table,"  "+ orderDetailsList.get(i).getUnitPrice(), Element.ALIGN_CENTER, 2, urFontName); // insert date value
            insertCell(table, orderDetailsList.get(i).getQuantity()+"", Element.ALIGN_CENTER, 1, urFontName); // insert date value

            Product product =productDBAdapter.getProductByID(orderDetailsList.get(i).getProductId());
            orderDetailsList.get(i).setProduct(product);
            if (orderDetailsList.get(i).getProduct().getDisplayName().equals("General"))
                orderDetailsList.get(i).getProduct().setProductCode(context.getString(R.string.general));
            insertCell(table, orderDetailsList.get(i).getProduct().getDisplayName(), Element.ALIGN_CENTER, 3, urFontName); // insert date value
            totalPrice+=orderDetailsList.get(i).getItemTotalPrice();
            saleTotalPrice += orderDetailsList.get(i).getUnitPrice();
            count+=orderDetailsList.get(i).getQuantity();
            if (orderDetailsList.get(i).getProductSerialNumber()>0) {
                insertCell(table, context.getString(R.string.serial_no) + " : " + orderDetailsList.get(i).getProductSerialNumber(), Element.ALIGN_LEFT, 9, urFontName); // insert date value
            }
            productDBAdapter.close();
        }
        totalSaved = (totalPrice - saleTotalPrice);
        Log.d("pricePDF",price_before_tax+"");
        Log.d("totalPriceCal",totalPrice+"");
        Log.d("saleTotalPriceCal",saleTotalPrice+"");
        Log.d("totalCalaculate",totalSaved+"");
        insertCell(table, "\n\n\n---------------------------" , Element.ALIGN_CENTER, 9, urFontName);
        insertCell(table,Util.makePrice(count)  , Element.ALIGN_CENTER, 4, urFontName);
        insertCell(table, context.getString(R.string.product_quantity)  , Element.ALIGN_CENTER, 5, urFontName);



        if (order.cartDiscount !=0) {
            insertCell(table, Util.makePrice(order.getTotalPrice() * 100 / (100 - order.cartDiscount)), Element.ALIGN_CENTER, 4, urFontName);
            insertCell(table, context.getString(R.string.price_before_discount), Element.ALIGN_CENTER, 5, urFontName);

            insertCell(table, Util.makePrice(order.cartDiscount)+ "%", Element.ALIGN_CENTER, 4, urFontName);
            insertCell(table, context.getString(R.string.discount), Element.ALIGN_CENTER, 5, urFontName);

        }
        double totalPriceAfterDiscount= totalPrice- (totalPrice * (order.cartDiscount/100));
        Log.d("order.cartDiscount",totalPrice+"");
        Log.d("ordet",totalPriceAfterDiscount+"");

        double noTax =price_before_tax - (price_before_tax * (order.cartDiscount/100));
        Log.d("noTaxIn",noTax+"");
        Log.d("ordeartDiscount",totalPriceAfterDiscount - price_before_tax+"");

        insertCell(table, Util.makePrice(price_before_tax) , Element.ALIGN_CENTER,4, urFontName);
        insertCell(table, context.getString(R.string.price_before_tax) , Element.ALIGN_CENTER,5, urFontName);


        insertCell(table,Util.makePrice(totalPriceAfterDiscount - price_before_tax) , Element.ALIGN_CENTER, 4, urFontName);
        insertCell(table, context.getString(R.string.tax) +" : "+Util.makePrice(SETTINGS.tax)+" "+"%" , Element.ALIGN_CENTER, 5, urFontName);

        insertCell(table,Util.makePrice(totalPriceAfterDiscount)  , Element.ALIGN_CENTER, 4, urFontName);
        insertCell(table, context.getString(R.string.total_price)  , Element.ALIGN_CENTER, 5, urFontName);



        insertCell(table, "\n\n\n---------------------------" , Element.ALIGN_CENTER, 9, urFontName);

        insertCell(paidByTable,context.getString(R.string.paid_by), Element.ALIGN_RIGHT, 4, urFontName);
        insertCell(paidByTable,context.getString(R.string.rest), Element.ALIGN_CENTER, 1, urFontName);
        insertCell(paidByTable,context.getString(R.string.given), Element.ALIGN_CENTER, 1, urFontName);
        insertCell(paidByTable,context.getString(R.string.total), Element.ALIGN_CENTER, 1, urFontName);
        insertCell(paidByTable,context.getString(R.string.payment), Element.ALIGN_CENTER, 1, urFontName);
        double cash_plus = 0;
        double check_plus = 0;
        double creditCard_plus = 0;
        List<CashPayment>cashPaymentList=cashPaymentDBAdapter.getPaymentBySaleID(order.getOrderId());
        for(int i=0;i<cashPaymentList.size();i++){
            cash_plus+=cashPaymentList.get(i).getAmount()*cashPaymentList.get(i).getCurrencyRate();
        }
        List<Check>checkList=checksDBAdapter.getPaymentBySaleID(order.getOrderId());
        for(int i=0;i<checkList.size();i++){
            check_plus+=checkList.get(i).getAmount();
        }
        List<CreditCardPayment>creditCardPayments=creditCardPaymentDBAdapter.getPaymentByOrderID(order.getOrderId());
        for(int i=0;i<creditCardPayments.size();i++){
            creditCard_plus+=creditCardPayments.get(i).getAmount();
        }
        if(cashPaymentList.size()>0){
            insertCell(paidByTable,Util.makePrice(order.getTotalPrice()-cash_plus), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(paidByTable,Util.makePrice(cash_plus), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(paidByTable,Util.makePrice(order.getTotalPrice()), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(paidByTable,CONSTANT.CASH, Element.ALIGN_CENTER, 1, urFontName);
        }
        if(checkList.size()>0){
            insertCell(paidByTable,Util.makePrice(order.getTotalPrice()-check_plus), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(paidByTable,Util.makePrice(check_plus), Element.ALIGN_CENTER, 1, font);
            insertCell(paidByTable,Util.makePrice(order.getTotalPrice()), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(paidByTable,CONSTANT.CHECKS, Element.ALIGN_CENTER, 1, font);
        }
        if(creditCardPayments.size()>0){
            insertCell(paidByTable,Util.makePrice(order.getTotalPrice()-creditCard_plus), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(paidByTable,Util.makePrice(creditCard_plus), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(paidByTable,Util.makePrice(order.getTotalPrice()), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(paidByTable,CONSTANT.CREDIT_CARD, Element.ALIGN_CENTER, 1, urFontName);
        }
        insertCell(paidByTable, "\n\n\n---------------------------" , Element.ALIGN_CENTER, 4, urFontName);

        List<CurrencyType> currencyTypesList = null;
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(context);
        currencyTypeDBAdapter.open();
        currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
        currencyTypeDBAdapter.close();

        insertCell(currencyTable, context.getString(R.string.currency), Element.ALIGN_RIGHT, 3, urFontName);
        insertCell(currencyTable, context.getString(R.string.returned), Element.ALIGN_CENTER, 1, urFontName);
        insertCell(currencyTable, context.getString(R.string.given), Element.ALIGN_CENTER, 1, urFontName);
        insertCell(currencyTable, context.getString(R.string.type), Element.ALIGN_CENTER, 1, urFontName);
        double firstTypePaid = 0, firstTypeReturn = 0, secondTypePaid = 0, secondTypeReturn = 0, thirdTypePaid = 0, thirdTypeReturn = 0, fourthTypePaid = 0, fourthTypeReturn = 0;
        CurrencyOperationDBAdapter currencyOperationDBAdapter = new CurrencyOperationDBAdapter(context);
        currencyOperationDBAdapter.open();
        List<CurrencyOperation> currencyOperationList = currencyOperationDBAdapter.getCurrencyOperationByOrderID(order.getOrderId());
        CurrencyReturnsDBAdapter currencyReturnDBAdapter = new CurrencyReturnsDBAdapter(context);
        currencyReturnDBAdapter.open();
        List<Currency> currencyList=new ArrayList<>();
            /*for (int i=0;i<currencyTypesList.size();i++){
                CurrencyDBAdapter currencyDBAdapter =new CurrencyDBAdapter(context);
                currencyDBAdapter.open();
                currencyList.add(currencyDBAdapter.getCurrencyByCode(currencyTypesList.get(i).getType()));
                currencyDBAdapter.close();
            }*/
        CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(context);
        currencyDBAdapter.open();
        currencyList = currencyDBAdapter.getAllCurrencyLastUpdate(currencyTypesList);
        currencyDBAdapter.close();


        List<CurrencyReturns> currencyReturnsList = currencyReturnDBAdapter.getCurencyReturnBySaleID(order.getOrderId());
        Log.d("currrencyreturnlistpdf",currencyReturnsList.toString());
        Log.d("currencyOperationLpdf",currencyOperationList.toString());
        Log.d("currencyTypesListtpdf",currencyTypesList.get(0).getType());
        for (int i = 0; i < currencyOperationList.size(); i++) {
            if (currencyOperationList.get(i).getPaymentWay().equalsIgnoreCase(CONSTANT.CASH)) {
                if (currencyOperationList.get(i).getCurrencyType().equalsIgnoreCase(currencyTypesList.get(0).getType()) ) {
                    firstTypePaid += currencyOperationList.get(i).getAmount();
                    Log.d("firstTypePaid",firstTypePaid+"");
                } if (SETTINGS.enableCurrencies){
                    if (currencyList.get(1)!=null&&currencyOperationList.get(i).getCurrencyType().equalsIgnoreCase(currencyTypesList.get(1).getType())) {
                        secondTypePaid += currencyOperationList.get(i).getAmount();
                        Log.d("secondTypePaid",secondTypePaid+"");
                    } else if (currencyList.get(2)!=null&&currencyOperationList.get(i).getCurrencyType().equalsIgnoreCase(currencyTypesList.get(2).getType())) {
                        thirdTypePaid += currencyOperationList.get(i).getAmount();
                        Log.d("thirdTypePaid",thirdTypePaid+"");
                    } else if (currencyList.get(3)!=null&&currencyOperationList.get(i).getCurrencyType().equalsIgnoreCase(currencyTypesList.get(3).getType())) {
                        fourthTypePaid += currencyOperationList.get(i).getAmount();
                        Log.d("fourthTypePaid",fourthTypePaid+"");
                    }}
            }

        }
        for (int i = 0; i < currencyReturnsList.size(); i++) {
            Log.d("huhu",currencyReturnsList.get(i).getCurrency_type()+"");
            if (currencyReturnsList.get(i).getCurrency_type() ==  currencyList.get(0).getId()) {
                firstTypeReturn += currencyReturnsList.get(i).getAmount();
            } if (SETTINGS.enableCurrencies){
                if ((currencyList.get(1)!=null)&&currencyReturnsList.get(i).getCurrency_type() == currencyList.get(1).getId()) {
                    secondTypeReturn += currencyReturnsList.get(i).getAmount();
                } else if ((currencyList.get(2)!=null)&& currencyReturnsList.get(i).getCurrency_type() == currencyList.get(2).getId()) {
                    thirdTypeReturn += currencyReturnsList.get(i).getAmount();
                } else if ((currencyList.get(3)!=null)&&currencyReturnsList.get(i).getCurrency_type() == currencyList.get(3).getId()) {
                    fourthTypeReturn += currencyReturnsList.get(i).getAmount();
                }}

        }


        if (firstTypePaid > 0 || firstTypeReturn > 0) {
            insertCell(currencyTable, Util.makePrice(firstTypeReturn), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(currencyTable,  Util.makePrice(firstTypePaid), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(currencyTable, currencyTypesList.get(0).getType(), Element.ALIGN_CENTER, 1, urFontName);
        }
        if (SETTINGS.enableCurrencies){
            if (secondTypePaid > 0 || secondTypeReturn > 0) {
                insertCell(currencyTable, Util.makePrice(secondTypeReturn), Element.ALIGN_CENTER, 1, urFontName);
                insertCell(currencyTable,  Util.makePrice(secondTypePaid), Element.ALIGN_CENTER, 1, urFontName);
                insertCell(currencyTable, currencyTypesList.get(1).getType(), Element.ALIGN_CENTER, 1, urFontName);
            }
            if (thirdTypePaid > 0 || thirdTypeReturn > 0) {
                insertCell(currencyTable, Util.makePrice(thirdTypeReturn), Element.ALIGN_CENTER, 1, urFontName);
                insertCell(currencyTable,  Util.makePrice(thirdTypePaid), Element.ALIGN_CENTER, 1, urFontName);
                insertCell(currencyTable, currencyTypesList.get(2).getType(), Element.ALIGN_CENTER, 1, urFontName);
            }
            if (fourthTypePaid > 0 || fourthTypeReturn > 0) {
                insertCell(currencyTable, Util.makePrice(fourthTypeReturn), Element.ALIGN_CENTER, 1, urFontName);
                insertCell(currencyTable,  Util.makePrice(fourthTypePaid), Element.ALIGN_CENTER, 1, urFontName);
                insertCell(currencyTable, currencyTypesList.get(3).getType(), Element.ALIGN_CENTER, 1, urFontName);
            }}
        insertCell(currencyTable, "\n\n\n---------------------------" , Element.ALIGN_CENTER, 3, urFontName);


        if(checkList!=null){
            insertCell(checkTable, context.getString(R.string.amount), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(checkTable, context.getString(R.string.date), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(checkTable, context.getString(R.string.checks), Element.ALIGN_CENTER, 1, urFontName);
            for(int i=0;i<checkList.size();i++){
                insertCell(checkTable,Util.makePrice(checkList.get(i).getAmount()), Element.ALIGN_CENTER, 1, urFontName);
                insertCell(checkTable, DateConverter.toDate(new Date(checkList.get(i).getCreatedAt().getTime())), Element.ALIGN_CENTER, 1, urFontName);
                insertCell(checkTable, checkList.get(i).getCheckNum()+"", Element.ALIGN_CENTER, 1, urFontName);
            }

        }

        String str = "";
        Log.d("mainMerCredit",mainMer);

        for (String s : mainMer.split("\n")) {

           /* String[] tokens = s.split("\\s+");
            Log.i("split0", Arrays.toString(tokens));*/

            if(!s.replaceAll(" ","").equals("")) {
                if(s.contains("שם מסוף")){
                    continue;
                }
                else if(s.contains("מספר מסוף")){
                    continue;
                }
                else if(s.contains("גרסת תוכנה")){
                    continue;
                }
                else if(s.contains("מספר עסק בחברת האשראי")){
                    continue;
                }
                else if(s.contains("Powered")){
                    continue;
                }
                else  if(s.contains("מספר כרטיס")){
                    if(s.split("\\s+")[1].length()>4){
                        String head = "מספר כרטיס";
                        String ss = "";
                        for(int i=0;i<s.split("\\s+")[1].length()-4;i++) {
                            ss += "*";
                        }
                        ss += s.split("\\s+")[1].substring(s.split("\\s+")[1].length() - 4, s.split("\\s+")[1].length());
                        str += "\u200E" + ss + "\t" + head + "\n";
                        continue;
                    }
                }
                str += "\u200E" + s + "\n";
            }

            Log.i("cc row", s);
        }
        Log.d("str", str);
        if(str!=""){
            insertCell(creditCard,str, Element.ALIGN_CENTER, 4, urFontName);
        }

        PdfPTable endOfInvoice = new PdfPTable(2);
        endOfInvoice.deleteBodyRows();
        endOfInvoice.setRunDirection(0);
        insertCell(endOfInvoice,context.getString(R.string.cashier)+":",Element.ALIGN_CENTER,1,urFontName);

        insertCell(endOfInvoice,SESSION._EMPLOYEE.getEmployeeName(),Element.ALIGN_CENTER,1,urFontName);
        insertCell(endOfInvoice, " ", Element.ALIGN_CENTER,2, urFontName);
        if(order.getCustomer()!=null) {

            if (order.getCustomer().getCustomerType().toString().equalsIgnoreCase(CustomerType.CREDIT.getValue())) {
                insertCell(endOfInvoice, context.getString(R.string.customer_ledger) + ":", Element.ALIGN_CENTER, 1, urFontName);

                insertCell(endOfInvoice, SESSION._ORDERS.CustomerLedger + "", Element.ALIGN_CENTER, 1, urFontName);
            }
        }
        insertCell(endOfInvoice, " ", Element.ALIGN_CENTER,2, urFontName);

        insertCell(endOfInvoice, context.getString(R.string.date) + ":", Element.ALIGN_CENTER, 1, urFontName);
        String dateOrderEndInvoice=UtilitValidationDate.isValidDate(order.getCreatedAt());

        insertCell(endOfInvoice,  dateOrderEndInvoice, Element.ALIGN_CENTER, 1, urFontName);
        insertCell(endOfInvoice, " ", Element.ALIGN_CENTER,2, urFontName);

        if(isCopy) {
            insertCell(endOfInvoice, context.getString(R.string.copy_date) + ":", Element.ALIGN_CENTER, 1, urFontName);

            insertCell(endOfInvoice, DateConverter.currentDateTime() + "", Element.ALIGN_CENTER, 1, urFontName);
            insertCell(endOfInvoice, " ", Element.ALIGN_CENTER,2, urFontName);

        }

        String s = SETTINGS.currencySymbol;
        insertCell(endOfInvoice, context.getString(R.string.total_saved)+ " :", Element.ALIGN_CENTER, 1, urFontName);
        Log.d("totalSavedPDF",order.getTotalSaved()+"");
        insertCell(endOfInvoice, order.getTotalSaved()+"", Element.ALIGN_CENTER, 1, urFontName);
        insertCell(endOfInvoice, " ", Element.ALIGN_CENTER,2, urFontName);

        insertCell(endOfInvoice, SETTINGS.returnNote, Element.ALIGN_CENTER, 1, urFontName);

        insertCell(endOfInvoice," ", Element.ALIGN_CENTER, 1, urFontName);
        //add table to document
        document.add(headingTable);
        document.add(table);
        document.add(paidByTable);
        document.add(currencyTable);
        if(checkList.size()>0)
        {
            document.add(checkTable);
        }
        if(str.length()>0)
        {
            document.add(creditCard);
        }

        document.add(endOfInvoice);

        document.close();
    }
    public static void  printInventoryReport(Context context, String res,String source) throws IOException, DocumentException, JSONException {
        ProductDBAdapter productDBAdapter =new ProductDBAdapter(context);
        productDBAdapter.open();
        JSONObject jsonObject = new JSONObject(res);
        JSONObject documentsData = jsonObject.getJSONObject("documentsData");;
        JSONObject customerInfo=null;
   /*     if(source=="inInventory") {
            customerInfo = new JSONObject(documentsData.getJSONObject("provider").toString());
        }*/
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

        BaseFont urName = BaseFont.createFont("assets/arial.ttf", "Identity-H",true,BaseFont.EMBEDDED);
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
        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
            insertCell(headingTable,context.getString(R.string.privet_company_status)  + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);
        }else {
            insertCell(headingTable, context.getString(R.string.private_company) + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);}
        if(employee!=null) {
            insertCell(headingTable, context.getString(R.string.cashiers) + employee.getFullName(), Element.ALIGN_CENTER, 1, font);
        }
        if(customerInfo!=null) {
            insertCell(headingTable, context.getString(R.string.provider) + ":" + customerInfo.getString("firstName") + customerInfo.getString("lastName"), Element.ALIGN_LEFT, 1, dateFont);
        }else {
            insertCell(headingTable, context.getString(R.string.provider) + ":" + context.getString(R.string.general), Element.ALIGN_LEFT, 1, dateFont);

        }
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
    public static void  printDepositAndPullReport(Context context, DepositAndPullReport depositAndPullReport, final ArrayList<String> currencyTypeList, final ArrayList<Double>currencyAmount, String type) throws IOException, DocumentException, JSONException {
        // create file , document region
        Document document = new Document();
        String fileName = "depositAndPullReport.pdf";
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

        BaseFont urName = BaseFont.createFont("assets/arial.ttf", "Identity-H",true,BaseFont.EMBEDDED);
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
        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
            insertCell(headingTable, context.getString(R.string.privet_company_status) + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);
        }
        else {
            insertCell(headingTable,context.getString(R.string.private_company) + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);
        }

        insertCell(headingTable, context.getString(R.string.cashiers) + SESSION._EMPLOYEE.getFullName(), Element.ALIGN_CENTER, 1, font);
        if(type.equals(context.getString(R.string.pull_report))){
            insertCell(headingTable, context.getString(R.string.pull_report) , Element.ALIGN_CENTER, 1, font);

        }else {
            insertCell(headingTable, context.getString(R.string.deposit_report) , Element.ALIGN_CENTER, 1, font);

        }
        insertCell(headingTable, context.getString(R.string.amount) +":"+ depositAndPullReport.getAmount()+SETTINGS.currencyCode, Element.ALIGN_CENTER, 1, font);

        insertCell(headingTable, "\n---------------------------" , Element.ALIGN_CENTER, 4, font);
        DepositAndPullReportDetailsDbAdapter depositAndPullReportDetailsDbAdapter = new DepositAndPullReportDetailsDbAdapter(context);
        depositAndPullReportDetailsDbAdapter.open();
        if(currencyAmount.size()>0) {
            for (int i = 0; i < currencyAmount.size(); i++) {
                if(currencyAmount.get(i)>0) {
                    if(type.equals("Pull")){
                        insertCell(dataTable, context.getString(R.string.pull_report) + " : " + currencyAmount.get(i) + "  " + currencyTypeList.get(i), Element.ALIGN_LEFT, 2, dateFont);
                    }
                    else {
                        insertCell(dataTable, context.getString(R.string.deposit_report) + " : " + currencyAmount.get(i) + "  " + currencyTypeList.get(i), Element.ALIGN_LEFT, 2, dateFont);

                    }
                }
            }}
        else {
            if(type.equals("Pull")) {
                insertCell(dataTable, context.getString(R.string.pull_report) + " : " + depositAndPullReport.getAmount() + "  " + SETTINGS.currencyCode, Element.ALIGN_LEFT, 2, dateFont);
            }else {
                insertCell(dataTable, context.getString(R.string.deposit_report) + " : " + depositAndPullReport.getAmount() + "  " + SETTINGS.currencyCode, Element.ALIGN_LEFT, 2, dateFont);

            }
        }

        //add table to document
        document.add(headingTable);
        document.add(dataTable);

        document.close();

        //en


    }
    public static void  printCustomerInvoicesReport(Context context, List<Object>list,double customerW) throws IOException, DocumentException, JSONException {
        // create file , document region
        Document document = new Document();
        String fileName = "customerInvoicesList.pdf";
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

        BaseFont urName = BaseFont.createFont("assets/arial.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 30);
        Font dateFont = new Font(urName, 10);
        //heading table
        PdfPTable headingTable = new PdfPTable(1);
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);
        insertCell(headingTable,  SETTINGS.companyName , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.cashiers) + SESSION._EMPLOYEE.getFullName(), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, context.getString(R.string.customer_ledger)+" : " + customerW, Element.ALIGN_CENTER, 1, font);

//        insertCell(headingTable, context.getString(R.string.date) + invoiceJsonObject.getString("date"), Element.ALIGN_CENTER, 1, font);

        //end

        //date table from , to
        PdfPTable dateTable = new PdfPTable(9);
        dateTable.setRunDirection(0);
        dateTable.setWidthPercentage(108f);

        insertCell(dateTable, context.getString(R.string.sale_id), Element.ALIGN_LEFT, 3, dateFont);
        insertCell(dateTable, context.getString(R.string.name), Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, context.getString(R.string.date), Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, context.getString(R.string.price), Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, context.getString(R.string.type), Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, context.getString(R.string.discount), Element.ALIGN_LEFT, 1, dateFont);
        insertCell(dateTable, context.getString(R.string.cancel_invoice_id), Element.ALIGN_LEFT, 1, dateFont);
        for(int i=0;i<list.size();i++){
            if(list.get(i) instanceof  Order){
                Order order =(Order) list.get(i);
                insertCell(dateTable, order.getOrderId()+"", Element.ALIGN_LEFT, 3, dateFont);
                insertCell(dateTable,order.getCustomer_name(), Element.ALIGN_LEFT, 1, dateFont);
                insertCell(dateTable, order.getCreatedAt()+"", Element.ALIGN_LEFT, 1, dateFont);
                insertCell(dateTable,order.getTotalPrice()+"", Element.ALIGN_LEFT, 1, dateFont);
                insertCell(dateTable,"INRC", Element.ALIGN_LEFT, 1, dateFont);
                insertCell(dateTable, order.getCartDiscount()+"", Element.ALIGN_LEFT, 1, dateFont);
                insertCell(dateTable, order.getCancellingOrderId()+"", Element.ALIGN_LEFT, 1, dateFont);
            }
            else {
                BoInvoice boInvoice =(BoInvoice)list.get(i);
                JSONObject jsonObject=boInvoice.getDocumentsData();
                JSONObject customerJson = jsonObject.getJSONObject("customer");

                insertCell(dateTable,boInvoice.getDocNum(), Element.ALIGN_LEFT, 3, dateFont);

                insertCell(dateTable, customerJson.getString("firstName"), Element.ALIGN_LEFT, 1, dateFont);

                Date date = DateConverter.stringToDate(jsonObject.getString("date"));
                insertCell(dateTable,DateConverter.geDate(date), Element.ALIGN_LEFT, 1, dateFont);


                if(boInvoice.getType().getValue().equalsIgnoreCase(DocumentType.RECEIPT.getValue())){
                    insertCell(dateTable, jsonObject.getString("paidAmount"), Element.ALIGN_LEFT, 1, dateFont);

                }else {
                    insertCell(dateTable, jsonObject.getString("total"), Element.ALIGN_LEFT, 1, dateFont);

                }


                if(boInvoice.getType().equals(DocumentType.INVOICE)) {
                    insertCell(dateTable,"IN", Element.ALIGN_LEFT, 1, dateFont);

                }else if(boInvoice.getType().equals(DocumentType.CREDIT_INVOICE)) {
                    insertCell(dateTable, "CIN", Element.ALIGN_LEFT, 1, dateFont);

                }
                else if(boInvoice.getType().equals(DocumentType.INVOICE_RECEIPT)){
                    insertCell(dateTable, "INRC", Element.ALIGN_LEFT, 1, dateFont);


                }
                else if(boInvoice.getType().equals(DocumentType.RECEIPT)){
                    insertCell(dateTable,"RC", Element.ALIGN_LEFT, 1, dateFont);

                }



                if(boInvoice.getType().getValue().equalsIgnoreCase(DocumentType.RECEIPT.getValue())){
                    insertCell(dateTable, 0+"", Element.ALIGN_LEFT, 1, dateFont);

                }else {
                    insertCell(dateTable, jsonObject.getString("cartDiscount"), Element.ALIGN_LEFT, 1, dateFont);

                }
                insertCell(dateTable, 0+"", Element.ALIGN_LEFT, 1, dateFont);
            }
        }

        insertCell(dateTable, "\n---------------------------" , Element.ALIGN_CENTER, 9, font);
        //end

        // schedule worker table
        //end

        //add table to document
        document.add(headingTable);
        document.add(dateTable);
        document.close();
        //end :)
    }

    public static String getLanguageApp(){
        String CurrentLang = Locale.getDefault().getLanguage();
        Log.d("deviceLanguage",CurrentLang);
        return CurrentLang;
    }

}
