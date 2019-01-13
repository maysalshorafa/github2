package com.pos.leaders.leaderspossystem;

/**
 * Created by Win8.1 on 3/28/2018.
 */

import android.content.Context;
import android.os.Environment;
import android.util.Log;

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
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyReturns;
import com.pos.leaders.leaderspossystem.Models.Employee;
import com.pos.leaders.leaderspossystem.Models.OpiningReport;
import com.pos.leaders.leaderspossystem.Models.OpiningReportDetails;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.ScheduleWorkers;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;
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
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Creates an accessible PDF with images and text.
 */

public class PdfUA {

   public static PdfPTable currencyTable = new PdfPTable(4);
    public static Employee user;
   public static ZReport zReport;
 /**   public static void  createZReportPdf(Context context, long id, long from, long to, boolean isCopy, double totalZReportAmount) throws IOException, DocumentException {
     Document document = new Document();

        String status ="-Source-";
        if(isCopy){
            status="-Copy-";
        }
        String fileName = "randompdf.pdf";
        final String APPLICATION_PACKAGE_NAME = context.getPackageName();
        File path = new File( Environment.getExternalStorageDirectory(), APPLICATION_PACKAGE_NAME );
        path.mkdirs();
        File file = new File(path, fileName);
    if(file.exists()){
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.close();
        }
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        BaseFont urName = BaseFont.createFont("assets/miriam_libre_bold.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font urFontName = new Font(urName, 30);
        BaseFont urName1 = BaseFont.createFont("assets/Rubik-Light.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName1, 24);

        PdfPTable headingTable = new PdfPTable(1);
        PdfPTable posTable = new PdfPTable(1);
        posTable.setWidthPercentage(115f);

        PdfPTable table = new PdfPTable(4);
        createZReport(table,context,id,from,to,urFontName); // fill ZReport info
        //heading table
        headingTable.deleteBodyRows();
        headingTable.setRunDirection(0);
        insertCell(headingTable, "P.C" + ":" + SETTINGS.companyID , Element.ALIGN_CENTER, 1, urFontName);
        insertCell(headingTable, status , Element.ALIGN_CENTER, 1, urFontName);
        insertCell(headingTable, DateConverter.dateToString(zReport.getCreatedAt()) , Element.ALIGN_CENTER, 1, urFontName);
        insertCell(headingTable, context.getString(R.string.cashiers) + user.getFullName() , Element.ALIGN_CENTER, 1, urFontName);
        insertCell(headingTable,  String.format("%06d", zReport.getCashPaymentId()) , Element.ALIGN_CENTER, 1, urFontName);
        insertCell(headingTable, "\n---------------------------" , Element.ALIGN_CENTER, 1, urFontName);
        document.add(headingTable);
        //end


        document.add(table);

        if(SETTINGS.enableCurrencies)// add space between two table
        {
            document.add(currencyTable);//add currency table
        }
        posTable.deleteBodyRows();
        posTable.setRunDirection(0);
        insertCell(posTable, context.getString(R.string.pos_sales)+" "+Util.makePrice(totalZReportAmount), Element.ALIGN_LEFT, 1, font);
        document.add(posTable);

            document.close();
    }**/
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



    public static void createZReport(PdfPTable table, Context context, long id, long from, long to,Font urFontName) throws IOException, DocumentException {
        double sheqle_plus = 0, sheqle_minus = 0;
        double usd_plus = 0, usd_minus = 0;
        double eur_plus = 0, eur_minus = 0;
        double gbp_plus = 0, gbp_minus = 0;
        Log.i("CZREPO", "id:" + id + " ,from:" + from + " ,to" + to + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        OrderDBAdapter saleDBAdapter = new OrderDBAdapter(context);
        saleDBAdapter.open();
        List<Order> sales = saleDBAdapter.getBetween(from, to);

        saleDBAdapter.close();
        ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(context);
        zReportDBAdapter.open();
         zReport = zReportDBAdapter.getByID(id);
        zReportDBAdapter.close();
        EmployeeDBAdapter userDBAdapter = new EmployeeDBAdapter(context);
        userDBAdapter.open();
        user = userDBAdapter.getEmployeeByID(zReport.getByUser());
        zReport.setUser(userDBAdapter.getEmployeeByID(zReport.getByUser()));
        userDBAdapter.close();
        OpiningReportDBAdapter aReportDBAdapter = new OpiningReportDBAdapter(context);
        aReportDBAdapter.open();
        OpiningReport aReport = aReportDBAdapter.getByLastZReport(id);
        /*try {
            aReportAmount = aReportDBAdapter.getLastRow().getAmount();
            aReportId = aReportDBAdapter.getLastRow().getCashPaymentId();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        double aReportDetailsForFirstCurrency=0;
        double aReportDetailsForSecondCurrency=0;
        double aReportDetailsForThirdCurrency=0;
        double aReportDetailsForForthCurrency=0;

        // get payment , cashPayment , returnList
        List<Payment> payments = PrintTools.paymentList(sales);
        List<CashPayment> cashPaymentList = PrintTools.cashPaymentList(sales);
        List<CurrencyReturns> currencyReturnList = PrintTools.returnPaymentList(sales);
        if (SETTINGS.enableCurrencies) {
            OpiningReportDetailsDBAdapter aReportDetailsDBAdapter=new OpiningReportDetailsDBAdapter(context);
            aReportDetailsDBAdapter.open();

            aReportDetailsForFirstCurrency = aReportDetailsDBAdapter.getLastRow(CONSTANT.Shekel, aReport.getOpiningReportId());
            aReportDetailsForSecondCurrency = aReportDetailsDBAdapter.getLastRow(CONSTANT.USD, aReport.getOpiningReportId());
            aReportDetailsForThirdCurrency = aReportDetailsDBAdapter.getLastRow(CONSTANT.GBP, aReport.getOpiningReportId());
            aReportDetailsForForthCurrency = aReportDetailsDBAdapter.getLastRow(CONSTANT.EUR, aReport.getOpiningReportId());
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
        table.setRunDirection(0);
        table.setWidthPercentage(118f);
        table.setWidths(new int[]{1, 1, 1,2});
        BaseFont urName = BaseFont.createFont("assets/Rubik-Light.ttf", "Identity-H",true,BaseFont.EMBEDDED);
        Font font = new Font(urName, 24);
        //insert column headings;
        insertCell(table, context.getString(R.string.total), Element.ALIGN_CENTER, 1, urFontName);
        insertCell(table, context.getString(R.string.out_put), Element.ALIGN_CENTER, 1, urFontName);
        insertCell(table, context.getString(R.string.in_put), Element.ALIGN_CENTER, 1, urFontName);
        insertCell(table, context.getString(R.string.details), Element.ALIGN_CENTER, 1, urFontName);

        //insert an empty row
        insertCell(table, Util.makePrice(cash_plus+cash_minus), Element.ALIGN_CENTER, 1, font);
        insertCell(table, Util.makePrice(cash_minus), Element.ALIGN_CENTER, 1, font);
        insertCell(table, Util.makePrice(cash_plus), Element.ALIGN_CENTER, 1, font);
        insertCell(table, context.getString(R.string.cash), Element.ALIGN_RIGHT, 1, font);

        insertCell(table, Util.makePrice(creditCard_plus+creditCard_minus), Element.ALIGN_CENTER, 1, font);
        insertCell(table, Util.makePrice(creditCard_minus), Element.ALIGN_CENTER, 1, font);
        insertCell(table, Util.makePrice(creditCard_plus), Element.ALIGN_CENTER, 1, font);
        insertCell(table, context.getString(R.string.credit_card), Element.ALIGN_CENTER, 1, font);

        insertCell(table,Util.makePrice(check_plus+cash_minus), Element.ALIGN_CENTER, 1, font);
        insertCell(table,Util.makePrice(check_minus), Element.ALIGN_CENTER, 1, font);
        insertCell(table, Util.makePrice(cash_plus), Element.ALIGN_CENTER, 1, font);
        insertCell(table, context.getString(R.string.checks), Element.ALIGN_CENTER, 1, font);

        insertCell(table,Util.makePrice(aReport.getAmount())+ "", Element.ALIGN_CENTER, 1, font);
        insertCell(table, "~", Element.ALIGN_CENTER, 1, font);
        insertCell(table,"~", Element.ALIGN_CENTER, 1, font);
        insertCell(table, "OpiningReport", Element.ALIGN_CENTER, 1, font);

        insertCell(table,Util.makePrice(cash_plus + cash_minus + creditCard_plus + creditCard_minus + check_plus + check_minus+Double.parseDouble(Util.makePrice(aReport.getAmount()))), Element.ALIGN_CENTER, 1, font);
        insertCell(table,  Util.makePrice(cash_minus + check_minus + creditCard_minus), Element.ALIGN_CENTER, 1, font);
        insertCell(table, Util.makePrice(cash_plus+check_plus+creditCard_plus), Element.ALIGN_CENTER, 1, font);
        insertCell(table, context.getString(R.string.total_amount), Element.ALIGN_CENTER, 1, font);

        insertCell(table,Util.makePrice(cash_plus + cash_minus + creditCard_plus + creditCard_minus + check_plus + check_minus), Element.ALIGN_CENTER, 1, font);
        insertCell(table,  Util.makePrice(cash_minus + check_minus + creditCard_minus), Element.ALIGN_CENTER, 1, font);
        insertCell(table, Util.makePrice(cash_plus+check_plus+creditCard_plus), Element.ALIGN_CENTER, 1, font);
        insertCell(table, context.getString(R.string.total_sales), Element.ALIGN_CENTER, 1, font);

        insertCell(table,"----------------------------------------------", Element.ALIGN_CENTER, 4, font);

        if(SETTINGS.enableCurrencies){
            currencyTable.setRunDirection(0);
            currencyTable.deleteBodyRows();
            currencyTable.setWidthPercentage(118f);
            currencyTable.setWidths(new int[]{1, 1, 1,2});
            insertCell(currencyTable, context.getString(R.string.total), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(currencyTable, context.getString(R.string.out_put), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(currencyTable, context.getString(R.string.in_put), Element.ALIGN_CENTER, 1, urFontName);
            insertCell(currencyTable, context.getString(R.string.currency), Element.ALIGN_CENTER, 1, urFontName);

            insertCell(currencyTable, Util.makePrice(usd_plus-usd_minus), Element.ALIGN_CENTER, 1, font);
            insertCell(currencyTable, Util.makePrice(usd_minus), Element.ALIGN_CENTER, 1, font);
            insertCell(currencyTable,Util.makePrice(usd_plus), Element.ALIGN_CENTER, 1, font);
            insertCell(currencyTable, context.getString(R.string.usd), Element.ALIGN_CENTER, 1, font);

            insertCell(currencyTable, Util.makePrice(eur_plus-eur_minus), Element.ALIGN_CENTER, 1, font);
            insertCell(currencyTable, Util.makePrice(eur_minus), Element.ALIGN_CENTER, 1, font);
            insertCell(currencyTable, Util.makePrice(eur_plus), Element.ALIGN_CENTER, 1, font);
            insertCell(currencyTable, context.getString(R.string.eur), Element.ALIGN_CENTER, 1, font);

            insertCell(currencyTable,Util.makePrice(gbp_plus-gbp_minus), Element.ALIGN_CENTER, 1, font);
            insertCell(currencyTable,Util.makePrice(gbp_minus), Element.ALIGN_CENTER, 1, font);
            insertCell(currencyTable, Util.makePrice(gbp_plus), Element.ALIGN_CENTER, 1, font);
            insertCell(currencyTable, context.getString(R.string.gbp), Element.ALIGN_CENTER, 1, font);

            insertCell(currencyTable,Util.makePrice(sheqle_plus-sheqle_minus), Element.ALIGN_CENTER, 1, font);
            insertCell(currencyTable, Util.makePrice(sheqle_minus), Element.ALIGN_CENTER, 1, font);
            insertCell(currencyTable, Util.makePrice(sheqle_plus), Element.ALIGN_CENTER, 1, font);
            insertCell(currencyTable, context.getString(R.string.shekel), Element.ALIGN_CENTER, 1, font);

        }
        insertCell(currencyTable,"----------------------------------------------", Element.ALIGN_CENTER, 4, font);
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
        insertCell(headingTable, context.getString(R.string.cashiers) + SESSION._EMPLOYEE.getFullName(), Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "Date"+":"+DateConverter.stringToDate(customerJson.getString("date")), Element.ALIGN_LEFT, 2, dateFont);

//        insertCell(headingTable, context.getString(R.string.date) + invoiceJsonObject.getString("date"), Element.ALIGN_CENTER, 1, font);

        //end

        //date table from , to
        PdfPTable dateTable = new PdfPTable(2);
        dateTable.setRunDirection(0);
        dateTable.setWidthPercentage(108f);

        insertCell(dateTable, context.getString(R.string.customer_name)+":"+customerInfo.getString("firstName")+customerInfo.getString("lastName"), Element.ALIGN_LEFT, 2, dateFont);
        insertCell(dateTable, "Receipt Numbers"+":"+jsonObject.getString("docNum"), Element.ALIGN_LEFT, 3, dateFont);
        //end
        insertCell(dateTable, "\n---------------------------" , Element.ALIGN_CENTER, 4, font);

        PdfPTable orderDetailsTable = new PdfPTable(3);
        orderDetailsTable.setRunDirection(0);
        orderDetailsTable.setWidthPercentage(108f);
        Log.d("customerJson",customerJson.toString());
        if (payment.getJSONObject(0).getString("paymentWay").equals("checks")) {
            insertCell(orderDetailsTable, context.getString(R.string.total_paid)+": "+payment.getJSONObject(0).getDouble("paidAmount"), Element.ALIGN_LEFT, 3, dateFont);
        }else {
            insertCell(orderDetailsTable, context.getString(R.string.total_paid) + ": " + payment.getJSONObject(0).getDouble("amount"), Element.ALIGN_LEFT, 3, dateFont);
        }
        insertCell(orderDetailsTable, "CustomerGeneralLedger"+":"+customerJson.getString("customerGeneralLedger"), Element.ALIGN_LEFT, 3, dateFont);
        insertCell(orderDetailsTable, "ReferenceInvoice"+":"+refNumber.get(0), Element.ALIGN_LEFT, 3, dateFont);
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
        insertCell(headingTable,context.getString(R.string.invoice)+" "+context.getString(R.string.no)+" "+customerJson.getString("reference"),Element.ALIGN_LEFT,1,dateFont);
        insertCell(headingTable, "\n---------------------------" , Element.ALIGN_CENTER, 4, font);


        if(source=="source"){
            insertCell(headingTable,  context.getString(R.string.source_invoice) , Element.ALIGN_CENTER, 1, font);

        }else {
            insertCell(headingTable,  context.getString(R.string.copy_invoice) , Element.ALIGN_CENTER, 1, font);

        }
        insertCell(headingTable,  context.getString(R.string.credit_invoice_doc) , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, jsonObject.getString("docNum") , Element.ALIGN_CENTER, 1, font);
        insertCell(headingTable, "\n---------------------------" , Element.ALIGN_CENTER, 4, font);
        PdfPTable orderDetailsTable = new PdfPTable(4);
        orderDetailsTable.setRunDirection(0);
        orderDetailsTable.setWidthPercentage(108f);
        Log.d("customerJson",customerJson.toString());

        JSONArray itemJson = customerJson.getJSONArray("cartDetailsList");
        insertCell(orderDetailsTable, context.getString(R.string.name), Element.ALIGN_LEFT, 1, dateFont);
        insertCell(orderDetailsTable,context.getString(R.string.qty), Element.ALIGN_LEFT, 1, dateFont);
        insertCell(orderDetailsTable, context.getString(R.string.price), Element.ALIGN_LEFT, 1, dateFont);
        insertCell(orderDetailsTable, context.getString(R.string.total), Element.ALIGN_LEFT, 1, dateFont);
        for (int a = 0 ; a<itemJson.length();a++) {
            JSONObject jsonObject1 = itemJson.getJSONObject(a);
            String sku = jsonObject1.getString("sku");
            Product product= productDBAdapter.getProductByBarCode(sku);
            insertCell(orderDetailsTable, product.getProductCode(), Element.ALIGN_LEFT, 1, dateFont);
            insertCell(orderDetailsTable, ""+jsonObject1.getInt("quantity"), Element.ALIGN_LEFT, 1, dateFont);
            insertCell(orderDetailsTable,Util.makePrice(product.getPrice()), Element.ALIGN_LEFT, 1, dateFont);
            insertCell(orderDetailsTable, Util.makePrice(product.getPrice()*jsonObject1.getInt("quantity")), Element.ALIGN_LEFT, 1, dateFont);
        }
        insertCell(orderDetailsTable, "\n---------------------------" , Element.ALIGN_CENTER, 4, font);
        insertCell(orderDetailsTable, "customerGeneralLedger"+":"+customerJson.getString("customerGeneralLedger"), Element.ALIGN_LEFT, 4, dateFont);
        insertCell(orderDetailsTable, "Date"+":"+DateConverter.stringToDate(customerJson.getString("date")), Element.ALIGN_LEFT, 4, dateFont);
        insertCell(orderDetailsTable, context.getString(R.string.total_paid)+":"+customerJson.getDouble("total"), Element.ALIGN_LEFT, 4, dateFont);

        //end

        //add table to document
        document.add(headingTable);
        document.add(orderDetailsTable);
        document.close();
        //end :)
    }
    public static void  printOpiningReport(Context context, OpiningReport opiningReport) throws IOException, DocumentException, JSONException {
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
        if(SETTINGS.enableCurrencies){
        List<OpiningReportDetails>opiningReportDetailsList=opiningReportDetailsDBAdapter.getListOpiningReport(opiningReport.getOpiningReportId());
        if(opiningReportDetailsList.size()>0) {
            Log.d("opiningList", opiningReportDetailsList.toString());
            for (int i = 0; i < opiningReportDetailsList.size(); i++) {
                String currencyType = "";
                if (opiningReportDetailsList.get(i).getType() == 0) {
                    currencyType = "Shekel";
                    Log.d("opiningList", opiningReportDetailsList.get(i).getAmount() + (""));

                    insertCell(dataTable, context.getString(R.string.opening_report) + " : " + opiningReportDetailsList.get(i).getAmount() + "  " + currencyType, Element.ALIGN_LEFT, 2, dateFont);

                } else if (opiningReportDetailsList.get(i).getType() == 1) {
                    currencyType = "USD";
                    Log.d("opiningList", opiningReportDetailsList.get(i).getAmount() + (""));
                    insertCell(dataTable, context.getString(R.string.opening_report) + " : " + opiningReportDetailsList.get(i).getAmount() + "  " + currencyType, Element.ALIGN_LEFT, 2, dateFont);
                } else if (opiningReportDetailsList.get(i).getType() == 2) {
                    currencyType = "GBP";
                    Log.d("opiningList", opiningReportDetailsList.get(i).getAmount() + (""));
                    insertCell(dataTable, context.getString(R.string.opening_report) + " : " + opiningReportDetailsList.get(i).getAmount() + "  " + currencyType, Element.ALIGN_LEFT, 2, dateFont);
                } else if (opiningReportDetailsList.get(i).getType() == 3) {
                    currencyType = "EUR";
                    Log.d("opiningList", opiningReportDetailsList.get(i).getAmount() + (""));
                    insertCell(dataTable, context.getString(R.string.opening_report) + " : " + opiningReportDetailsList.get(i).getAmount() + "  " + currencyType, Element.ALIGN_LEFT, 2, dateFont);
                }
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

}