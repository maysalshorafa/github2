package com.pos.leaders.leaderspossystem.Tools;

import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.IdsCounterDBAdapter;
import com.pos.leaders.leaderspossystem.syncposservice.Service.SyncMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by KARAM on 19/01/2017.
 */

public class Util {

    private static final String INSTALLATION = "INSTALLATION.rd";
    private static final String LOG_TAG = "Tools_Util";
    public static String newline = System.getProperty("line.separator");
    public static Locale locale = new Locale("en");

    public synchronized static boolean isFirstLaunch(Context context, boolean CreateFile) {
        String sID = null;
        boolean launchFlag = false;
        if (sID == null) {
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {
                if (!installation.exists()) {
                    launchFlag = true;
                    if (CreateFile)
                        writeInstallationFile(installation);
                } else
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
        return str.substring(0, str.length() - 1);
    }
    public static double convertSign(Double d) {
        return d*-1;
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

    public static String makePrice(double d) {
        String format = "0.";
        if (SETTINGS.decimalNumbers == 0) {
            format = "0";
        } else {
            for (int i = SETTINGS.decimalNumbers; i > 0; i--) {
                format += "0";
            }
        }
        DecimalFormat form = new DecimalFormat(format);
        return form.format(d);
    }

    public static String spaces(int num) {
        String s = "";
        for (int i = 0; i < num; i++) {
            s += "\u0020";
        }
        return s;
    }

    public static String x12V99(double d) {
        double x = d;
        if (d < 0)
            x *= -1;

        String str = "";
        str += (String.format(locale, "%012d", ((Double) x).intValue()));
        int fl = ((Double) ((x - Math.floor(x) + 0.001) * 100)).intValue();
        str += (String.format(locale, "%02d", fl));
        return str;
    }

    public static String x9V99(double d) {
        double x = d;
        if (d < 0)
            x *= -1;

        String str = "";
        str += (String.format(locale, "%09d", ((Double) x).intValue()));
        int fl = ((Double) ((x - Math.floor(x) + 0.001) * 100)).intValue();
        str += (String.format(locale, "%02d", fl));
        return str;
    }

    public static String _8V99(double d) {
        double x = d;
        if (d < 0)
            x *= -1;

        String str = "";
        str += (String.format(locale, "%08d", ((Double) x).intValue()));
        int fl = ((Double) ((x - Math.floor(x) + 0.001) * 100)).intValue();
        str += (String.format(locale, "%02d", fl));
        return str;
    }

    public static File createFileFromInputStream(InputStream inputStream, String fileName) {

        try {
            File f = new File(fileName);
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        } catch (IOException e) {
            //Logging exception
        }

        return null;
    }

    private static boolean checkID(long id) {
        String sID = String.format(new Locale("en"), "%019d", id);
        String idPrefix = sID.substring(0, sID.length() - 16);
        return Long.parseLong(idPrefix) == SESSION.POS_ID_NUMBER;
    }

    private static long getCurrentLastID(SQLiteDatabase db, String tableName, String idField) {
        long i = 0;
        Cursor cursor = db.rawQuery("select  " + tableName + "  from  " + IdsCounterDBAdapter.IDS_COUNTER_TABLE_NAME + ";", null);
        if (cursor.getCount() < 1) // don`t have any sale yet
        {
            cursor.close();
            i = (long) SESSION.POS_ID_NUMBER * SESSION.firstIDOffset;
        } else {
            cursor.moveToFirst();
            i = Long.parseLong(cursor.getString(0));
        }
        ContentValues values = new ContentValues();
        if (i == 0) {
            i = (long) SESSION.POS_ID_NUMBER * SESSION.firstIDOffset;
            values.put(tableName, i);
        } else {
            i = i + 1;
            values.put(tableName, i);
        }
        db.update(IdsCounterDBAdapter.IDS_COUNTER_TABLE_NAME, values, null, null);
        return i;

    }

    public static long idHealth(SQLiteDatabase db, String tableName, String idField) {
        return getCurrentLastID(db, tableName, idField);
    }

    private static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSyncServiceRunning(Context context) {
        return isMyServiceRunning(SyncMessage.class, context);
    }
    public static void killSyncService(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SyncMessage.class.getName().equals(service.service.getClassName())) {
                context.stopService(new Intent().setComponent(service.service));
            }
        }
    }

    // Methods To Test Input Value Type
    public static final boolean isDouble(String value) {

        Pattern pattern = Pattern.compile("^[+-]?(\\d+)(\\D)(\\d+)");
        Matcher matcher = pattern.matcher(value);
        boolean result = matcher.matches();
        return result;

    }

    public static final boolean isFloat(String value) {
        Pattern pattern = Pattern.compile("^[+-]?(\\d+)(\\D)(\\d+)");
        Matcher matcher = pattern.matcher(value);
        boolean result = matcher.matches();
        return result;

    }

    public static final boolean isDecimal(String value) {
        Pattern pattern = Pattern.compile("^[+-]?(\\d+)(\\D)(\\d+)");
        Matcher matcher = pattern.matcher(value);
        boolean result = matcher.matches();
        return result;

    }

    public static final boolean isBoolean(String value) {

        if (value.equals("1") || value.equals("0")) {
            return true;
        }
        return false;

    }

    public static final boolean isBit(String value) {
        Pattern pattern = Pattern.compile("^(0|1)$");
        Matcher matcher = pattern.matcher(value);
        boolean result = matcher.matches();
        return result;

    }

    public static final boolean isInteger(String value) {

        Pattern pattern = Pattern.compile("[+-]?\\b[0-9]+\\b");
        Matcher matcher = pattern.matcher(value);
        boolean result = matcher.matches();
        return result;
    }

    public static final boolean isString(String value) {
        Pattern pattern = Pattern.compile("^((?!badword).)*$");
        Matcher matcher = pattern.matcher(value);
        boolean result = matcher.matches();
        return result;
    }

    public static final boolean isEmail(String value) {
        Pattern pattern = Pattern.compile("/^\\w+([\\.-]?\\w+)@\\w+([\\.-]?\\w+)(\\.\\w{2,3})+$/");

        Matcher matcher = pattern.matcher(value);
        boolean result = matcher.matches();
        return result;
    }

    public static final boolean isLong(String value) {
        Pattern pattern = Pattern.compile("^-?\\d{1,19}$");
        Matcher matcher = pattern.matcher(value);
        boolean result = matcher.matches();
        return result;
    }


}
