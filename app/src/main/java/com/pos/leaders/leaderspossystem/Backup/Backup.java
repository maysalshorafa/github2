package com.pos.leaders.leaderspossystem.Backup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CategoryDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PosSettingDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.LogInActivity;
import com.pos.leaders.leaderspossystem.Models.Category;
import com.pos.leaders.leaderspossystem.Models.PosSetting;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.SetUpManagement;
import com.pos.leaders.leaderspossystem.Tools.BufferDbEmail;
import com.pos.leaders.leaderspossystem.Tools.CompanyStatus;
import com.pos.leaders.leaderspossystem.Tools.PrinterType;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by KARAM on 14/01/2017.
 */

public class Backup{

    private static final String KEY = "1thisisa2newkey3";
    private final String folderName;
    private File downloadDir;
    private static final String LOG_TAG = "Backup_Backup";
    public static final String FULL_BACKUP_FILE_NAME = "Full_Backup.data";
    static Context context;

    public Backup(Context c,String folderName){
        this.context = c;
        this.folderName=folderName;
        downloadDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"/Backup/" + folderName);
        downloadDir.mkdirs();
    }

    public boolean backupProductOnDepartment(Category department) {
        List<Category> dep = new ArrayList<Category>();
        dep.add(department);
        return backupProductOnDepartment(dep);
    }

    public boolean backupProductOnDepartment(List<Category> deps){
        ProductDBAdapter productDBAdapter=new ProductDBAdapter(context);
        productDBAdapter.open();

        for (Category d : deps) {
            File f=new File(downloadDir,"product_"+d.getName()+"_export.xls");
            List<Product> products=productDBAdapter.getAllProductsByCategory(d.getCategoryId());
            try {
                WritableWorkbook excelFile = Workbook.createWorkbook(f);
                WritableSheet excelTable=excelFile.createSheet(d.getName(),0);
                excelTable.addCell(new Label(0,0,"ID"));
                excelTable.addCell(new Label(1,0,"Name"));
                excelTable.addCell(new Label(2,0,"Barcode"));
                excelTable.addCell(new Label(3,0,"Price"));

                for (int i=0;i<products.size();i++){
                    excelTable.addCell(new Label(0,i+1,products.get(i).getProductId()+""));
                    excelTable.addCell(new Label(1,i+1,products.get(i).getDisplayName()));
                    excelTable.addCell(new Label(2,i+1,products.get(i).getSku()));
                    excelTable.addCell(new Label(3,i+1,String.format(new Locale("en"),"%.2f",products.get(i).getPriceWithTax())));
                }

                excelFile.write();
                excelFile.close();


            } catch (WriteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        productDBAdapter.close();
        return true;
    }

    public boolean backupDepartment(){
        CategoryDBAdapter departmentDBAdapter=new CategoryDBAdapter(context);
        departmentDBAdapter.open();

        List<Category> departments=departmentDBAdapter.getAllDepartments();
        File f=new File(downloadDir,"Departments_export.xls");
        try {
            WritableWorkbook excelFile = Workbook.createWorkbook(f);
            WritableSheet excelTable=excelFile.createSheet("Departments",0);
            excelTable.addCell(new Label(0,0,"ID"));
            excelTable.addCell(new Label(1,0,"Name"));

            for (int i=0;i<departments.size();i++){
                excelTable.addCell(new Label(0,i+1,departments.get(i).getCategoryId()+""));
                excelTable.addCell(new Label(1,i+1,departments.get(i).getName()));
            }
            excelFile.write();
            excelFile.close();
            return true;

        } catch (WriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean backupProducts(){
        ProductDBAdapter productDBAdapter=new ProductDBAdapter(context);
        productDBAdapter.open();
        List<Product> products = productDBAdapter.getAllProducts();
        List<Product> resultSet = new ArrayList<Product>();

        //downloads directory on application path

        File f=new File(downloadDir,"products_export_.xls");
        try {
            WritableWorkbook excelFile=Workbook.createWorkbook(f);
            WritableSheet excelTable=excelFile.createSheet("Products",0);
            excelTable.addCell(new Label(0,0,"ID"));
            excelTable.addCell(new Label(1,0,"Name"));
            excelTable.addCell(new Label(2,0,"Barcode"));
            excelTable.addCell(new Label(3,0,"Price"));

            for (int i=0;i<products.size();i++){
                excelTable.addCell(new Label(0,i+1,products.get(i).getProductId()+""));
                excelTable.addCell(new Label(1,i+1,products.get(i).getDisplayName()));
                excelTable.addCell(new Label(2,i+1,products.get(i).getSku()));
                excelTable.addCell(new Label(3,i+1,String.format(new Locale("en"),"%.2f",products.get(i).getPriceWithTax())));
            }
            excelFile.write();
            excelFile.close();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean copyFile(File from,File to){
    return false;
    }

    private void _copyFile(File src, File dst) throws IOException {
        try {
            FileInputStream var2 = new FileInputStream(src);
            FileOutputStream var3 = new FileOutputStream(dst);
            byte[] var4 = new byte[1024];

            int var5;
            while((var5 = var2.read(var4)) > 0) {
                var3.write(var4, 0, var5);
            }
            var2.close();
            var3.close();
        } catch (IOException e) {
            Log.e("Error",e.getMessage());
            e.printStackTrace();
        }

    }

    private void copyDB(){
        final File inFileName = new File("/data/data/com.pos.leaders.leaderspossystem/databases/");
        File dbFile = new File(inFileName,"POSDB.db");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(dbFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        File outFileName = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS+ "/Backup/" + folderName+"/full_backup/");

        if(!outFileName.exists()){
            Log.w(LOG_TAG, "folder does not exists");
            Log.w(LOG_TAG, "trying to create folder : "+outFileName.getAbsolutePath());
            try{
                if(outFileName.mkdirs()){
                    Log.w(LOG_TAG, "folder has been created");
                }else{
                    Log.w(LOG_TAG, "can not create the folder : "+outFileName.getAbsolutePath());
                }
            }
            catch (Exception npe){
                Log.w(LOG_TAG, npe.toString());
            }
        }

        // Open the empty db as the output stream
        File of=new File(outFileName,"db_backup.db");
        try {

            OutputStream output = new FileOutputStream(of);

            // Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;

            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();
        }
        catch (Exception ex){
            Log.e("error",ex.getMessage());
        }
    }

    public boolean encBackupDB(){
        copyDB();
        File outFileName = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/Backup/" + folderName+"/full_backup/");
        outFileName.mkdirs();
        File of=new File(outFileName,"db_backup.db");


        File inputFile = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS+ "/Backup/" + folderName+"/full_backup/");
        inputFile = new File(inputFile, "db_backup.db");
        File encryptedFile = new File(outFileName,FULL_BACKUP_FILE_NAME);
        //File decryptedFile = new File("/storage/sdcard0/Android/data/com.pos.leaders.leaderspossystem/files/Download/Backup/pos.db");

        try {
            CryptoUtils.encrypt(KEY, inputFile, encryptedFile);
            if(Util.isExternalStorageWritable()) {
                File f = Util.getStorageDir("Backup");
                File file = new File(f, "full.c");
                _copyFile(encryptedFile, file);
            } else {
                Toast.makeText(context, context.getString(R.string.there_is_no_disk_on_key), Toast.LENGTH_LONG).show();
            }
            //CryptoUtils.decrypt(key, encryptedFile, decryptedFile);
        } catch (CryptoException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return false;
        } catch (IOException e) {
            Log.e(LOG_TAG,e.getMessage());
            e.printStackTrace();
        }
        of.delete();
        return true;
    }

    public static boolean decBackupDB(File encSrc){
        final File inFileName = new File("/data/data/com.pos.leaders.leaderspossystem/databases/");
        inFileName.mkdirs();
        File dbFile = new File(inFileName,"POSDB.db");
        try{
            CryptoUtils.decrypt(KEY, encSrc, dbFile);
            return true;
        }
        catch (CryptoException ex){
            ex.printStackTrace();
            return false;
        }
    }
    public static void BackupBufferDB() throws IOException {
        final String inFileName = "/data/data/com.pos.leaders.leaderspossystem/databases/BufferDb.db";
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);

        String outFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/BufferDb.db";

        // Open the empty db as the output stream
        OutputStream output = new FileOutputStream(outFileName);

        // Transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer))>0){
            output.write(buffer, 0, length);
        }

        // Close the streams
        output.flush();
        output.close();
        fis.close();
        BufferDbEmail.sendLogFileBufferDb();
    }

    public static void BackupPossSettingFile() throws IOException {
        final String inFileName = "/data/data/com.pos.leaders.leaderspossystem/shared_prefs/POS_Management.xml";
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);

        String outFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/POS_Management.xml";

        // Open the empty db as the output stream
        OutputStream output = new FileOutputStream(outFileName);

        // Transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer))>0){
            output.write(buffer, 0, length);
        }

        // Close the streams
        output.flush();
        output.close();
        fis.close();
        BufferDbEmail.sendLogFileBufferDb();
        PosSettingDbAdapter posSettingDbAdapter=new PosSettingDbAdapter(context);
        posSettingDbAdapter.open();
        if (!posSettingDbAdapter.existsHaveColumnInTable()){

        }
    }
    public static void BackupPOSDB() throws IOException {
        final String inFileName = "/data/data/com.pos.leaders.leaderspossystem/databases/POSDB.db";
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);

        String outFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/POSDB.db";

        // Open the empty db as the output stream
        OutputStream output = new FileOutputStream(outFileName);

        // Transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer))>0){
            output.write(buffer, 0, length);
        }

        // Close the streams
        output.flush();
        output.close();
        fis.close();
        BufferDbEmail.sendLogFilePOSDB();

    }
}
