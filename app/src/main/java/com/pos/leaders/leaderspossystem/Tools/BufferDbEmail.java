package com.pos.leaders.leaderspossystem.Tools;

import android.os.Environment;

import java.io.File;

/**
 * Created by Win8.1 on 1/9/2019.
 */

public class BufferDbEmail {
    public static void sendLogFileBufferDb(){
        String from = "lead2018pos@gmail.com";
        String to ="lead2018pos@gmail.com";
        final String password = "mais1234";
        String subject ="Log File for"+"Company Name :"+SETTINGS.companyName +"  "+ "to POS No:"+SETTINGS.posID+"  "+DateConverter.currentDateTime();
        String filename1="BufferDb.db";
        File filelocation = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename1);
        String file = filelocation.getAbsolutePath();
        BufferGmailClient gmailClient = new BufferGmailClient();
        gmailClient.sendFromGMail(from,password,to,subject,file);

    }
    public static void sendLogFilePOSDB(){
        String from = "lead2018pos@gmail.com";
        String to ="lead2018pos@gmail.com";
        final String password = "mais1234";
        String subject ="Log File for"+"Company Name :"+SETTINGS.companyName +"  "+ "to POS No:"+SETTINGS.posID+"  "+DateConverter.currentDateTime();
        String filename1="POSDB.db";
        File filelocation = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename1);
        String file = filelocation.getAbsolutePath();
        PosDbGmailClient gmailClient = new PosDbGmailClient();
        gmailClient.sendFromGMail(from,password,to,subject,file);

    }
}
