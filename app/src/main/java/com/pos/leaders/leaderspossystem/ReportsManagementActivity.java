package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.SaleDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Sale;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.OpenFormat.BKMVDATA;
import com.pos.leaders.leaderspossystem.OpenFormat.INI;
import com.pos.leaders.leaderspossystem.OpenFormat.OpenFrmt;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;

import org.joda.time.DateTime;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class ReportsManagementActivity extends Activity {
    Button btnZ, btnZView,btnX, btnSales,btnExFiles;

    ZReportDBAdapter zReportDBAdapter;
    SaleDBAdapter saleDBAdapter;

    ZReport lastZReport=null;
    Sale lastSale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reports_management);

        //region Init

        btnZ = (Button) findViewById(R.id.reportManagementActivity_btnZ);
        btnZ.setText("Z " + getString(R.string.report));
        btnZView = (Button) findViewById(R.id.reportManagementActivity_btnZView);
        btnZView.setText("Z " + getString(R.string.report) + " " + getString(R.string.view));
        btnX=(Button)findViewById(R.id.reportManagementActivity_btnX);
        btnX.setText("X " + getString(R.string.report));
        btnSales =(Button)findViewById(R.id.reportManagementActivity_btnSaleManagement);
        btnExFiles = (Button) findViewById(R.id.reportManagementActivity_btnExtractingFiles);

        zReportDBAdapter = new ZReportDBAdapter(this);
        saleDBAdapter = new SaleDBAdapter(this);

        //endregion init

        zReportDBAdapter.open();
        saleDBAdapter.open();
        lastSale = saleDBAdapter.getLast();

        try {
            lastZReport = zReportDBAdapter.getLastRow();

            if (lastZReport.getEndSaleId() == lastSale.getId()){
                btnZ.setEnabled(false);
            }

            else{
                btnZ.setEnabled(true);
            }
        } catch (Exception ex) {
            Log.e(ex.getLocalizedMessage(), ex.getMessage());
        }
        btnZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(long id, Date creationDate, User user, Sale startSale, Sale endSale)
                if(lastZReport == null) {
                    lastZReport = new ZReport();
                    lastZReport.setEndSaleId(0);
                }

                ZReport z=new ZReport(0,DateConverter.stringToDate(DateConverter.currentDateTime()) , SESSION._USER,lastZReport.getEndSaleId()+1,lastSale);
                z.setByUser(SESSION._USER.getId());
                long zID=zReportDBAdapter.insertEntry(z);
                z.setId(zID);
                lastZReport=new ZReport(z);

                PrintTools pt=new PrintTools(ReportsManagementActivity.this);

                //create and print z report
                pt.PrintReport(pt.createZReport(lastZReport.getId(),lastZReport.getStartSaleId(),lastZReport.getEndSaleId(),false));

                Intent i=new Intent(ReportsManagementActivity.this,ReportZDetailsActivity.class);
                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_ID,lastZReport.getId());
                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_FORM,lastZReport.getStartSaleId());
                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_TO,lastZReport.getEndSaleId());
                startActivity(i);
                btnZ.setEnabled(false);

                //// TODO: 30/03/2017 check error
                //Backup backup = new Backup(ReportsManagementActivity.this, String.format(new Locale("en"), new Date().getTime() + ""));
                //backup.encBackupDB();

            }
        });
        btnX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrintTools pt=new PrintTools(ReportsManagementActivity.this);

                //create and print x report
                pt.PrintReport(pt.createXReport(lastZReport.getEndSaleId()+1,lastSale.getId(),SESSION._USER,new Date()));

            }
        });
        btnZView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReportsManagementActivity.this,ZReportActivity.class);
                startActivity(intent);
            }
        });
        btnSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReportsManagementActivity.this,SalesManagementActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnExFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DateTime[] dates = new DateTime[2];
                DateTime dt = new DateTime();
                final int mYear = dt.getYear();
                final int mMonth = dt.getMonthOfYear();
                final int mDay = dt.getDayOfMonth();
                DatePickerDialog datePickerDialog = new DatePickerDialog(ReportsManagementActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Toast.makeText(getApplicationContext(), dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, Toast.LENGTH_SHORT).show();
                                dates[0] = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);


                                DatePickerDialog datePickerDialogTo = new DatePickerDialog(ReportsManagementActivity.this, new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        Toast.makeText(getApplicationContext(), dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, Toast.LENGTH_SHORT).show();
                                        dates[1] = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);
                                        try {
                                            final ProgressDialog progressDialog = new ProgressDialog(ReportsManagementActivity.this);
                                            progressDialog.setTitle(getBaseContext().getString(R.string.wait_for_finish));
                                            new AsyncTask<Void, Void, Void>() {
                                                @Override
                                                protected Void doInBackground(Void... params) {
                                                    try {
                                                        OpenFrmt openFrmt = new OpenFrmt();
                                                        BKMVDATA bkmvdata = new BKMVDATA(openFrmt.getBKMVDATA(), ReportsManagementActivity.this);
                                                        int row = bkmvdata.make(dates[0], dates[1]);

                                                        INI ini = new INI(openFrmt.getINI(), ReportsManagementActivity.this, row, bkmvdata.getFirstDate(), new Date().getTime(),
                                                                bkmvdata.cC100, bkmvdata.cD110, bkmvdata.cD120, bkmvdata.cM100, bkmvdata.cB100, bkmvdata.cA100, bkmvdata.cZ900);

                                                        ini.make();

                                                        openFrmt.Compress();
                                                        Document document = new Document();
                                                        try
                                                        {


                                                            BaseFont baseFont = BaseFont.createFont("file:///android_asset/miriam_libre_regular.ttf", BaseFont.IDENTITY_H, true);
                                                            Font font = new Font(baseFont);
                                                            /*
                                                            context.getAssets(),"miriam_libre_regular.ttf"
                                                             */
                                                            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(openFrmt.get5dot4()));

                                                            document.open();

                                                            Paragraph title = new Paragraph("הפקת קבצים במבנה אחיד עבור:",font);
                                                            title.setAlignment(Element.ALIGN_CENTER);
                                                            document.add(title);

                                                            Paragraph p1 = new Paragraph("מס' עודק מורשה / ח.פ: "+ SETTINGS.companyID+"\n\r"+"שם בית העסק: "+SETTINGS.companyName,font);
                                                            p1.setAlignment(Element.ALIGN_RIGHT);
                                                            document.add(p1);

                                                            Paragraph t1 = new Paragraph("** ביצוע ממשק פתוח הסתיים בהצלחה **",font);
                                                            t1.setAlignment(Element.ALIGN_CENTER);
                                                            document.add(t1);

                                                            Paragraph p2 = new Paragraph("הנתונים נשמרו בנתיב : " + openFrmt.getBKMVDATA().getPath() + "\n\r"
                                                                    + "טווח תאריכים: מתאריך " + DateConverter.dateToString(dates[0].toDate().getTime()).split(" ")[0]+" ועד תאריך: "+DateConverter.dateToString(dates[1].toDate().getTime()).split(" ")[0]+"\n\r"
                                                                    + "פירוט כך סוגי הרשומות בקובץ BKMVDATA.TXT:",font);
                                                            p2.setAlignment(Element.ALIGN_RIGHT);
                                                            document.add(p2);

                                                            //MetaData
                                                            document.addAuthor("LeadPOS");
                                                            document.addCreationDate();
                                                            document.addCreator("LeadPOS");
                                                            document.addTitle("5.4");
                                                            document.addSubject("");



                                                            PdfPTable table = new PdfPTable(3); // 3 columns.

                                                            table.setWidthPercentage(100); //Width 100%
                                                            table.setSpacingBefore(10f); //Space before table
                                                            table.setSpacingAfter(10f); //Space after table

                                                            //Set Column widths
                                                            float[] columnWidths = {1f, 1f, 1f};
                                                            table.setWidths(columnWidths);

                                                            table.addCell("קוד רשומה");
                                                            table.addCell("תיאור רשומה");
                                                            table.addCell("סך רשומות");

                                                            PdfPCell cell1 = new PdfPCell(new Paragraph("A100"));
                                                            cell1.setPaddingLeft(10);
                                                            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                                                            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

                                                            PdfPCell cell2 = new PdfPCell(new Paragraph("רשומת פתיחה"));
                                                            cell2.setPaddingLeft(10);
                                                            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                                                            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

                                                            PdfPCell cell3 = new PdfPCell(new Paragraph(bkmvdata.cA100));
                                                            cell3.setPaddingLeft(10);
                                                            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                                                            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

                                                            //B100
                                                            PdfPCell cell4 = new PdfPCell(new Paragraph("B100"));
                                                            cell4.setPaddingLeft(10);
                                                            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                                                            cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

                                                            PdfPCell cell5 = new PdfPCell(new Paragraph("תנועת בהנהלת חשבונות"));
                                                            cell5.setPaddingLeft(10);
                                                            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                                                            cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);

                                                            PdfPCell cell6 = new PdfPCell(new Paragraph(bkmvdata.cB100));
                                                            cell6.setPaddingLeft(10);
                                                            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                                                            cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);

                                                            //B110
                                                            PdfPCell cell7 = new PdfPCell(new Paragraph("B110"));
                                                            cell7.setPaddingLeft(10);
                                                            cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
                                                            cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);

                                                            PdfPCell cell8 = new PdfPCell(new Paragraph("חשבון בהנהלת חשבונות"));
                                                            cell8.setPaddingLeft(10);
                                                            cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
                                                            cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);

                                                            PdfPCell cell9 = new PdfPCell(new Paragraph("6"));
                                                            cell9.setPaddingLeft(10);
                                                            cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
                                                            cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);




                                                            table.addCell(cell1);
                                                            table.addCell(cell2);
                                                            table.addCell(cell3);
                                                            table.addCell(cell4);
                                                            table.addCell(cell5);
                                                            table.addCell(cell6);
                                                            table.addCell(cell7);
                                                            table.addCell(cell8);
                                                            table.addCell(cell9);

                                                            table.addCell("C100");
                                                            table.addCell("כותרת מסמך");
                                                            table.addCell(bkmvdata.cC100 + "");

                                                            table.addCell("D110");
                                                            table.addCell("פרטי מסמך");
                                                            table.addCell(bkmvdata.cD110 + "");

                                                            table.addCell("D120");
                                                            table.addCell("פרטי קבלות");
                                                            table.addCell(bkmvdata.cD120 + "");

                                                            table.addCell("M100");
                                                            table.addCell("פריטים במלאי");
                                                            table.addCell(bkmvdata.cM100 + "");

                                                            table.addCell("Z900");
                                                            table.addCell("רשומת סיום");
                                                            table.addCell(bkmvdata.cZ900 + "");

                                                            table.addCell(" ");
                                                            table.addCell("סה''כ : ");
                                                            table.addCell(row + "");

                                                            document.add(table);

                                                            Paragraph p3 = new Paragraph("הנתונים הופקו באמצעות תוכנת: LeadPOS "+" "+" "+" "+"מספר תעודת הרישום: 123456782"+"\n\r"+"בתאריך:"+DateConverter.dateToString(new Date().getTime()).split(" ")[0]+ "בשעה: "+DateConverter.dateToString(new Date().getTime()).split(" ")[1]);
                                                            p3.setAlignment(Element.ALIGN_RIGHT);
                                                            document.add(p3);



                                                            document.close();
                                                            writer.close();
                                                        } catch (DocumentException e) {
                                                            e.printStackTrace();
                                                        } catch (FileNotFoundException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                    catch (Exception ex){
                                                        ex.printStackTrace();
                                                    }
                                                    return null;
                                                }

                                                @Override
                                                protected void onPreExecute() {
                                                    progressDialog.show();
                                                }

                                                @Override
                                                protected void onPostExecute(Void aVoid) {
                                                    progressDialog.cancel();
                                                }
                                            }.execute();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, mYear, mMonth, mDay);
                                datePickerDialogTo.setTitle(getBaseContext().getString(R.string.to));
                                datePickerDialogTo.show();

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.setTitle(getBaseContext().getString(R.string.from));
                datePickerDialog.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        zReportDBAdapter.open();
        saleDBAdapter.open();
        super.onDestroy();
    }
}
