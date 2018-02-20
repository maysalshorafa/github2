package com.pos.leaders.leaderspossystem.Tools;

import android.os.Environment;

import java.io.File;

/**
 * Created by Win8.1 on 2/15/2018.
 */

public class SendLog {
    public static void sendLogFile(){
        String from = "lead2018pos@gmail.com";
        String to ="lead2018pos@gmail.com";
        final String password = "mais1234";
        String subject ="Log File for"+"Company Name :"+SETTINGS.companyName +"  "+ "to POS No:"+SETTINGS.posID+"  "+DateConverter.currentDateTime();
        String filename1="posexceptionfile.txt";
        File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename1);
        String file = filelocation.getAbsolutePath();
        GmailClient gmailClient = new GmailClient();
        gmailClient.sendFromGMail(from,password,to,subject,file);

    }

}
