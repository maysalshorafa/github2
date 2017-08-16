package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by KARAM on 19/01/2017.
 */

public class Util {

    private static final String INSTALLATION = "INSTALLATION.rd";
    private static final String LOG_TAG = "Tools_Util";
    public static String newline = System.getProperty("line.separator");
    public static Locale locale = new Locale("en");

    public synchronized static boolean isFirstLaunch(Context context,boolean CreateFile) {
        String sID = null;
        boolean launchFlag = false;
        if (sID == null) {
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {
                if (!installation.exists()) {
                    launchFlag = true;
                    if(CreateFile)
                        writeInstallationFile(installation);
                }
                else
                    sID = readInstallationFile(installation);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return launchFlag;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");// read only mode
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();

        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }



    public static String removeLastChar(String str) {
        return str.substring(0,str.length()-1);
    }


    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static File getStorageDir(String dirName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "LeadPOSBackup/" + dirName);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }

    public static String makePrice(double d){
        return String.format(new Locale("en"), "%.2f", d);
    }

    public static String spaces(int num) {
        String s = "";
        for (int i=0;i<num;i++){
            s += "\u0020";
        }
        return s;
    }

    public static String x12V99(double d){
        double x = d;
        if(d<0)
            x *= -1;

        String str = "";
        str += (String.format(locale, "%012d", ((Double)x).intValue()));
        int fl =  ((Double)((x - Math.floor(x) + 0.001) * 100)).intValue();
        str += (String.format(locale, "%02d", fl));
        return str;
    }
    public static String x9V99(double d){
        double x = d;
        if(d<0)
            x *= -1;

        String str = "";
        str += (String.format(locale, "%09d", ((Double)x).intValue()));
        int fl =  ((Double)((x - Math.floor(x) + 0.001) * 100)).intValue();
        str += (String.format(locale, "%02d", fl));
        return str;
    }
    public static String _8V99(double d){
        double x = d;
        if(d<0)
            x *= -1;

        String str = "";
        str += (String.format(locale, "%08d", ((Double)x).intValue()));
        int fl =  ((Double)((x - Math.floor(x) + 0.001) * 100)).intValue();
        str += (String.format(locale, "%02d", fl));
        return str;
    }

    public static File createFileFromInputStream(InputStream inputStream,String fileName) {

        try{
            File f = new File(fileName);
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        }catch (IOException e) {
            //Logging exception
        }

        return null;
    }

    private static boolean checkID(long id) {
        String sID = String.format(new Locale("en"),"%19d",id);
        String idPrefix = sID.substring(0, sID.length() - 16);
        return Long.parseLong(idPrefix) == SESSION.POS_ID_NUMBER;
    }

    private static long getCurrentLastID(SQLiteDatabase db, String tableName, String idField){
        Cursor cursor = db.rawQuery("select * from " + tableName + " order by id desc", null);
        if (cursor.getCount() < 1) // don`t have any sale yet
        {
            cursor.close();
            return 0;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long id = cursor.getLong(cursor.getColumnIndex(idField));
            if(Util.checkID(id))
                return id;
            cursor.moveToNext();
        }

        cursor.close();
        return 0;
    }

    public static long idHealth(SQLiteDatabase db, String tableName, String idField){
        long lastId = getCurrentLastID(db,tableName,idField);
        long newID = 0;
        if(lastId==0){
            newID = SESSION.POS_ID_NUMBER * SESSION.firstIDOffset;
        }
        else{
            newID = lastId + 1;
        }
        return newID;
    }

}
