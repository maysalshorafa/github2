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
import com.pos.leaders.leaderspossystem.DataBaseAdapter.AReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.AReportDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.AReport;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyReturns;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.ScheduleWorkers;
import com.pos.leaders.leaderspossystem.Models.Employee;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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
        AReportDBAdapter aReportDBAdapter = new AReportDBAdapter(context);
        aReportDBAdapter.open();
        AReport aReport = aReportDBAdapter.getByLastZReport(id);
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
            AReportDetailsDBAdapter aReportDetailsDBAdapter=new AReportDetailsDBAdapter(context);
            aReportDetailsDBAdapter.open();

            aReportDetailsForFirstCurrency = aReportDetailsDBAdapter.getLastRow(CONSTANT.Shekel, aReport.getaReportId());
            aReportDetailsForSecondCurrency = aReportDetailsDBAdapter.getLastRow(CONSTANT.USD, aReport.getaReportId());
            aReportDetailsForThirdCurrency = aReportDetailsDBAdapter.getLastRow(CONSTANT.GBP, aReport.getaReportId());
            aReportDetailsForForthCurrency = aReportDetailsDBAdapter.getLastRow(CONSTANT.EUR, aReport.getaReportId());
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
        insertCell(table, "AReport", Element.ALIGN_CENTER, 1, font);

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
<<<<<<< HEAD
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
//        insertCell(headingTable, context.getString(R.string.date) + jsonObject.getString("date"), Element.ALIGN_CENTER, 1, font);

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

=======
>>>>>>> LEAD-57

}
