package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.SettingsDBAdapter;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Tools.PrinterType;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.pos.leaders.leaderspossystem.SetUpManagement.POS_Management;

/**
 * Created by KARAM on 19/11/2016.
 */

public class SplashScreenActivity extends Activity {
    // TODO: 21/11/2016 return splash time out to 2500 ms
    private static final int SPLASH_SCREEN_TIME_OUT = 1500;
    private ZReport lastZReport = null;
    public static boolean  currencyEnable , creditCardEnable , customerMeasurementEnable ;
    public static int floatPoint;
    public static String printerType ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.splash_screen);
        ImageView iv=(ImageView)findViewById(R.id.splashScreen_iv);


        iv.setImageResource(R.drawable.white_color_logo);
        /// sharedPreferences for Setting

/*
        try {
            importDatabase("/storage/emulated/0/POSDB.db");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //// TODO: 22/04/2017 Move to report activity
        //// FIXME: 22/04/2017 Delete me
/*
        SettingsDBAdapter settingsDBAdapter = new SettingsDBAdapter(SplashScreenActivity.this);
        settingsDBAdapter.open();
        settingsDBAdapter.GetSettings();

        settingsDBAdapter.close();

        final ProgressDialog progressDialog = new ProgressDialog(SplashScreenActivity.this);
        progressDialog.setTitle(getBaseContext().getString(R.string.wait_for_finish));
        final DateTime dates[] = new DateTime[2];
        dates[0] = new DateTime(2015, 4, 20, 0, 0);
        dates[1] = new DateTime(2018, 4, 20, 0, 0);
*/
        /*
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    OpenFrmt openFrmt = new OpenFrmt();
                    BKMVDATA bkmvdata = new BKMVDATA(openFrmt.getBKMVDATA(), SplashScreenActivity.this);
                    int row = bkmvdata.make(dates[0], dates[1]);

                    INI ini = new INI(openFrmt.getINI(), SplashScreenActivity.this, row, bkmvdata.getFirstDate(), new Date().getTime(),
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
                                + "טווח תאריכים:\t מתאריך \t" + DateConverter.dateToString(dates[0].toDate().getTime()).split(" ")[1] + "\t ועד תאריך:\t " + DateConverter.dateToString(dates[1].toDate().getTime()).split(" ")[1] + "\n\r"
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

                    make2dot6(openFrmt.get2dot6(), dates[0].toDate(), dates[1].toDate(), bkmvdata.c320, bkmvdata.c330, bkmvdata.a320, bkmvdata.a330);

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
            }
        }.execute();
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
        firstRun();
    }

    private void firstRun(){
        Log.i("state", Util.isFirstLaunch(this, false) + "");
        if(Util.isFirstLaunch(this,false)){
            //first launch

            //Intent intent = new Intent(SplashScreenActivity.this, InitActivity.class);
            Intent intent = new Intent(SplashScreenActivity.this, InitActivity.class);
            startActivity(intent);

        }
        else{
            if(newPosManagement()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //Redirect to login activity
                        SettingsDBAdapter settingsDBAdapter = new SettingsDBAdapter(SplashScreenActivity.this);
                        settingsDBAdapter.open();
                        settingsDBAdapter.GetSettings();
                        settingsDBAdapter.close();

                        Intent intent = new Intent(SplashScreenActivity.this, LogInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, SPLASH_SCREEN_TIME_OUT);
            } else {
                Intent i = new Intent(SplashScreenActivity.this, SetUpManagement.class);
                startActivity(i);
            }
        }
    }

    private void checkFirstRun() {

        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;


        // Get current version code
        int currentVersionCode = 0;
        try {
            currentVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            // handle exception
            e.printStackTrace();
            return;
        }

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {
            //load data to settings static class and flag with true
            SETTINGS.LOADED_DATA=true;
            // This is just customerName normal run
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {
            // TODO: 19/01/2017 This is customerName new install (or the user cleared the shared preferences)

        } else if (currentVersionCode > savedVersionCode) {
            // TODO: 19/01/2017 This is an upgrade
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).commit();



    }

/*
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
*/

    private void importDatabase(String inputFileName) throws IOException
    {
        InputStream mInput = new FileInputStream(inputFileName);
        String outFileName = "/data/data/com.pos.leaders.leaderspossystem/databases/POSDB.db";
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }
    private boolean newPosManagement() {
        /// sharedPreferences for Setting
        SharedPreferences cSharedPreferences = getSharedPreferences(POS_Management, MODE_PRIVATE);
        if(cSharedPreferences!=null){
            //CreditCard
            if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CREDIT_CARD)) {
                creditCardEnable =cSharedPreferences.getBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CREDIT_CARD,false);
                SETTINGS.creditCardEnable=creditCardEnable;
            }
            else {
                return false;
            }
            // end CreditCard
            //Currency
            if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY)) {
                currencyEnable =cSharedPreferences.getBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY,false);
                SETTINGS.enableCurrencies=currencyEnable;

            }
            else {
                return false;
            }
            //end
            //CustomerMeasurement
            if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CUSTOMER_MEASUREMENT)) {
                customerMeasurementEnable =cSharedPreferences.getBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CUSTOMER_MEASUREMENT,false);
                SETTINGS.enableCustomerMeasurement=customerMeasurementEnable;
            }
            else {
                return false;
            }
            //end
            //FloatPoint
            if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_FLOAT_POINT)) {
                floatPoint =cSharedPreferences.getInt(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_FLOAT_POINT,2);
                SETTINGS.decimalNumbers=floatPoint;
            }
            else {
                return false;
            }
            //end
            //PrinterType
            if (cSharedPreferences.contains(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PRINTER_TYPE)) {
                printerType =cSharedPreferences.getString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PRINTER_TYPE,PrinterType.HPRT_TP805.name());
                PrinterType printer = PrinterType.valueOf(printerType);
                SETTINGS.printer=printer;
            }
            else {
                Intent i = new Intent(SplashScreenActivity.this, SetUpManagement.class);
                startActivity(i);
            }

        }else {
            Intent i = new Intent(SplashScreenActivity.this, SetUpManagement.class);
            startActivity(i);
        }
   return true;
    }

}

