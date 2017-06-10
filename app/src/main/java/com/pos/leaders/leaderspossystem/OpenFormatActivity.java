package com.pos.leaders.leaderspossystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.pos.leaders.leaderspossystem.OpenFormat.BKMVDATA;
import com.pos.leaders.leaderspossystem.OpenFormat.INI;
import com.pos.leaders.leaderspossystem.OpenFormat.OpenFrmt;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class OpenFormatActivity extends AppCompatActivity {

    private DatePicker dpFrom, dpTo;
    private Button btCreate;
    private DateTime from, to;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_format);

        dpFrom = (DatePicker) findViewById(R.id.openFormatActivity_dpFrom);
        dpTo = (DatePicker) findViewById(R.id.openFormatActivity_dpTo);

        btCreate = (Button) findViewById(R.id.openFormatActivity_btCreate);

        dpFrom.updateDate(2015,0,1);
        DateTime dt = new DateTime();

        dpTo.updateDate(dt.getYear(),dt.getMonthOfYear()-1,dt.getDayOfMonth());

        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour)
                from = new DateTime(dpFrom.getYear(),dpFrom.getMonth()+1,dpFrom.getDayOfMonth(),0,0);
                Log.i("From Date", dpFrom.getYear() + " - " + (dpFrom.getMonth()+1) + " - " + dpFrom.getDayOfMonth());
                to = new DateTime(dpTo.getYear(), dpTo.getMonth() + 1, dpTo.getDayOfMonth(), 0, 0);
                Log.i("From Date", dpTo.getYear() + " - " + (dpTo.getMonth()+1) + " - " + dpTo.getDayOfMonth());
                createReport();
                btCreate.setEnabled(false);
            }
        });
    }

    private void createReport() {
        try {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setTitle(getBaseContext().getString(R.string.wait_for_finish));

            final OpenFrmt openFrmt = new OpenFrmt();
            final BKMVDATA bkmvdata = new BKMVDATA(openFrmt.getBKMVDATA(), OpenFormatActivity.this);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        int row = bkmvdata.make(from, to);

                        INI ini = new INI(openFrmt.getINI(), OpenFormatActivity.this, row, bkmvdata.getFirstDate(), new Date().getTime(),
                                bkmvdata.cC100, bkmvdata.cD110, bkmvdata.cD120, bkmvdata.cM100, bkmvdata.cB100, bkmvdata.cA100, bkmvdata.cZ900);

                        ini.make();

                        openFrmt.Compress();

                        //region 5.4
                        Document document = new Document();
                        try {
                            BaseFont urName = BaseFont.createFont("assets/miriam_libre_regular.ttf", "UTF-8", BaseFont.EMBEDDED);
                            //Font font = new Font(urName, 12);
                            Font font = FontFactory.getFont("assets/miriam_libre_regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

                            //Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(openFrmt.get5dot4()), "UTF-8"));
                            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(openFrmt.get5dot4()));
                            document.open();


                            PdfPTable root = new PdfPTable(1);
                            root.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

                            Paragraph title = new Paragraph("הפקת קבצים במבנה אחיד עבור:", font);
                            PdfPCell _cell = new PdfPCell();
                            title.setAlignment(Element.ALIGN_CENTER);
                            _cell.addElement(title);
                            _cell.setBorder(Rectangle.NO_BORDER);
                            root.addCell(_cell);

                            Paragraph p1 = new Paragraph("מס' עודק מורשה / ח.פ: " + SETTINGS.companyID + "\n\r" + "שם בית העסק: " + SETTINGS.companyName, font);

                            PdfPCell __Cell = new PdfPCell();
                            __Cell.setBorder(Rectangle.NO_BORDER);
                            __Cell.addElement(p1);
                            root.addCell(__Cell);


                            Paragraph t1 = new Paragraph("** ביצוע ממשק פתוח הסתיים בהצלחה **", font);
                            t1.setAlignment(Element.ALIGN_CENTER);
                            PdfPCell _cell2 = new PdfPCell();
                            _cell2.setBorder(Rectangle.NO_BORDER);
                            _cell2.addElement(t1);
                            root.addCell(_cell2);

                            Paragraph p2 = new Paragraph("הנתונים נשמרו בנתיב : " + openFrmt.getBKMVDATA().getPath() + "\n\r"
                                    + "טווח תאריכים:\t מתאריך \t" + DateConverter.dateToString(from.toDate().getTime()).split(" ")[1] + "\t ועד תאריך:\t " + DateConverter.dateToString(to.toDate().getTime()).split(" ")[1] + "\n\r"
                                    + "פירוט כך סוגי הרשומות בקובץ BKMVDATA.TXT:", font);

                            PdfPCell _cell3 = new PdfPCell();
                            _cell3.setBorder(Rectangle.NO_BORDER);
                            _cell3.addElement(p2);
                            root.addCell(_cell3);

                            //MetaData
                            document.addAuthor("LeadPOS");
                            document.addCreationDate();
                            document.addCreator("LeadPOS");
                            document.addTitle("5.4");
                            document.addSubject("");


                            PdfPTable table = new PdfPTable(3); // 3 columns.
                            table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                            table.setWidthPercentage(100); //Width 100%
                            table.setSpacingBefore(10f); //Space before table
                            table.setSpacingAfter(10f); //Space after table

                            //Set Column widths
                            float[] columnWidths = {1f, 1f, 1f};
                            table.setWidths(columnWidths);


                            table.addCell(new Paragraph("קוד רשומה", font));
                            table.addCell(new Paragraph("תיאור רשומה", font));
                            table.addCell(new Paragraph("סך רשומות", font));

                            PdfPCell cell1 = new PdfPCell(new Paragraph("A100"));
                            PdfPCell cell2 = new PdfPCell(new Paragraph("רשומת פתיחה", font));
                            PdfPCell cell3 = new PdfPCell(new Paragraph(bkmvdata.cA100 + "", font));

                            //B100
                            PdfPCell cell4 = new PdfPCell(new Paragraph("B100"));
                            PdfPCell cell5 = new PdfPCell(new Paragraph("תנועת בהנהלת חשבונות", font));
                            PdfPCell cell6 = new PdfPCell(new Paragraph(bkmvdata.cB100 + "", font));

                            //B110
                            PdfPCell cell7 = new PdfPCell(new Paragraph("B110"));
                            PdfPCell cell8 = new PdfPCell(new Paragraph("חשבון בהנהלת חשבונות", font));
                            PdfPCell cell9 = new PdfPCell(new Paragraph("6"));


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
                            table.addCell(new Paragraph("כותרת מסמך", font));
                            table.addCell(bkmvdata.cC100 + "");

                            table.addCell("D110");
                            table.addCell(new Paragraph("פרטי מסמך", font));
                            table.addCell(bkmvdata.cD110 + "");

                            table.addCell("D120");
                            table.addCell(new Paragraph("פרטי קבלות", font));
                            table.addCell(bkmvdata.cD120 + "");

                            table.addCell("M100");
                            table.addCell(new Paragraph("פריטים במלאי", font));
                            table.addCell(bkmvdata.cM100 + "");

                            table.addCell("Z900");
                            table.addCell(new Paragraph("רשומת סיום", font));
                            table.addCell(bkmvdata.cZ900 + "");

                            table.addCell(" ");
                            table.addCell(new Paragraph("סה''כ : ", font));
                            table.addCell(row + "");


                            PdfPCell _tableCell = new PdfPCell();
                            _tableCell.addElement(table);
                            _tableCell.setBorder(Rectangle.NO_BORDER);
                            root.addCell(_tableCell);

                            Paragraph p3 = new Paragraph("הנתונים הופקו באמצעות תוכנת: LeadPOS " + " " + " " + " " + "מספר תעודת הרישום: 123456782" + "\n\r" + "\tבתאריך:\t" + DateConverter.dateToString(new Date().getTime()).split(" ")[1] + "\tבשעה: \t" + DateConverter.dateToString(new Date().getTime()).split(" ")[0], font);

                            PdfPCell _cell4 = new PdfPCell();
                            _cell4.setBorder(Rectangle.NO_BORDER);
                            _cell4.addElement(p3);
                            root.addCell(_cell4);


                            document.add(root);
                            document.close();
                            writer.close();
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        //endregion 5.4

                        make2dot6(openFrmt.get2dot6(), from.toDate(), to.toDate(), bkmvdata.c320, bkmvdata.c330, bkmvdata.a320, bkmvdata.a330);

                    } catch (Exception ex) {
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
                    openFolder(openFrmt.getOpenFormatDir());
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void make2dot6(File file, Date from, Date to, int c320, int c330, double a320, double a330){
        //region 6.2
        Document document = new Document();
        try {
            BaseFont urName = BaseFont.createFont("assets/miriam_libre_regular.ttf", "UTF-8", BaseFont.EMBEDDED);
            //Font font = new Font(urName, 12);
            Font font = FontFactory.getFont("assets/miriam_libre_regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            //Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(openFrmt.get5dot4()), "UTF-8"));
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();


            PdfPTable root = new PdfPTable(1);
            root.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

            Paragraph title = new Paragraph("הפקת קבצים במבנה אחיד עבור:", font);
            PdfPCell _cell = new PdfPCell();
            title.setAlignment(Element.ALIGN_CENTER);
            _cell.addElement(title);
            _cell.setBorder(Rectangle.NO_BORDER);
            root.addCell(_cell);

            Paragraph p1 = new Paragraph("מס' עודק מורשה / ח.פ: " + SETTINGS.companyID + "\n\r" + "שם בית העסק: " + SETTINGS.companyName, font);

            PdfPCell __Cell = new PdfPCell();
            __Cell.setBorder(Rectangle.NO_BORDER);
            __Cell.addElement(p1);
            root.addCell(__Cell);


            Paragraph p2 = new Paragraph("טווח תאריכים:\t מתאריך \t" + DateConverter.dateToString(from.getTime()).split(" ")[1] + "\t ועד תאריך:\t " + DateConverter.dateToString(to.getTime()).split(" ")[1], font);

            PdfPCell _cell3 = new PdfPCell();
            _cell3.setBorder(Rectangle.NO_BORDER);
            _cell3.addElement(p2);
            root.addCell(_cell3);

            //MetaData
            document.addAuthor("LeadPOS");
            document.addCreationDate();
            document.addCreator("LeadPOS");
            document.addTitle("6.2");
            document.addSubject("");


            PdfPTable table = new PdfPTable(4); // 4 columns.
            table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            table.setWidthPercentage(100); //Width 100%
            table.setSpacingBefore(10f); //Space before table
            table.setSpacingAfter(10f); //Space after table

            //Set Column widths
            float[] columnWidths = {1f, 1f, 1f,1f};
            table.setWidths(columnWidths);


            table.addCell(new Paragraph("מספר מסמך", font));
            table.addCell(new Paragraph("סוג מסמך", font));
            table.addCell(new Paragraph("סה''כ כמותי", font));
            table.addCell(new Paragraph("סה''כ כספי (בש''ח)", font));

            PdfPCell cell1 = new PdfPCell(new Paragraph("320"));
            PdfPCell cell2 = new PdfPCell(new Paragraph("חשבונית מס / קבלה", font));
            PdfPCell cell3 = new PdfPCell(new Paragraph(c320 + "", font));
            PdfPCell cell4 = new PdfPCell(new Paragraph(a320 + "", font));

            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);

            if(c330!=0) {
                //B100
                PdfPCell cell5 = new PdfPCell(new Paragraph("330"));
                PdfPCell cell6 = new PdfPCell(new Paragraph("חשבונית מס זיכוי", font));
                PdfPCell cell7 = new PdfPCell(new Paragraph(c330 + "", font));
                PdfPCell cell8 = new PdfPCell(new Paragraph("-"+a330 + "", font));


                table.addCell(cell5);
                table.addCell(cell6);
                table.addCell(cell7);
                table.addCell(cell8);
            }

            PdfPCell _tableCell = new PdfPCell();
            _tableCell.addElement(table);
            _tableCell.setBorder(Rectangle.NO_BORDER);
            root.addCell(_tableCell);

            Paragraph p3 = new Paragraph("הנתונים הופקו באמצעות תוכנת: LeadPOS " + " " + " " + " " + "מספר תעודת הרישום: 123456782" + "\n\r" + "\tבתאריך:\t" + DateConverter.dateToString(new Date().getTime()).split(" ")[1] + "\tבשעה: \t" + DateConverter.dateToString(new Date().getTime()).split(" ")[0], font);

            PdfPCell _cell4 = new PdfPCell();
            _cell4.setBorder(Rectangle.NO_BORDER);
            _cell4.addElement(p3);
            root.addCell(_cell4);


            document.add(root);
            document.close();
            writer.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //endregion 6.2
    }

    public void openFolder(String filePath){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/myFolder/");
        Uri uri = Uri.parse(filePath);
        intent.setDataAndType(uri, "file/*");
        startActivity(Intent.createChooser(intent, "Open folder"));
    }
}