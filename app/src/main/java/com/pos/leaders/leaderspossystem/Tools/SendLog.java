package com.pos.leaders.leaderspossystem.Tools;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by Win8.1 on 2/15/2018.
 */

public class SendLog {
    public static void sendLogFile(){
        String from = "lead2018pos@gmail.com";
        String to ="lead2018pos@gmail.com";
        final String password = "lead2018POS@gmail.com";
        String subject ="Log File for"+"Company Name :"+SETTINGS.companyName +"  "+ "to POS No:"+SETTINGS.posID+"  "+DateConverter.currentDateTime();
        String filename1="PosLogcat.txt";
        File externalStorageDir = Environment.getExternalStorageDirectory();
        File myFile = new File(externalStorageDir , filename1);

        if(myFile.exists())
        {
            try
            {
                PrintWriter writer = new PrintWriter(myFile);//to empty file each time method invoke
                writer.print("");
                writer.close();
                FileOutputStream fOut = new FileOutputStream(myFile,true);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                Process process = Runtime.getRuntime().exec("logcat -d");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder log=new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    log.append(line);
                    log.append("\n\r");
                }
                myOutWriter.append(SETTINGS.companyName +"  "+ "to POS No:"+SETTINGS.posID+"\n\r"+"Logcat :"+ DateConverter.currentDateTime()+"\n\r"+log.toString());
                myOutWriter.close();
                fOut.close();
            } catch(Exception e1)
            {

            }
        }
        else
        {
            try {
                myFile.createNewFile();
                FileOutputStream fOut = new FileOutputStream(myFile,true);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append("Company Name :"+ SETTINGS.companyName+"\n");
                myOutWriter.append("POS No :"+ SETTINGS.posID+"\n");
                Process process = Runtime.getRuntime().exec("logcat -d");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder log=new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    log.append(line);
                    log.append("\n\r");
                }
                myOutWriter.append(SETTINGS.companyName +"  "+ "to POS No:"+SETTINGS.posID+"\n\r"+"Logcat :"+ DateConverter.currentDateTime()+"\n\r"+log.toString());
                myOutWriter.close();
                myOutWriter.close();
                fOut.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
        File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename1);
        String file = filelocation.getAbsolutePath();
        GmailClient gmailClient = new GmailClient();
        gmailClient.sendFromGMail(from,password,to,subject,file);

    }
    public static void sendListFile(String to,String pa,String fileName){
        String from = "lead2018pos@gmail.com";
        final String password = "lead2018POS@gmail.com";
        String subject ="List Invoices File for"+"Company Name :"+SETTINGS.companyName +"  "+ "to POS No:"+SETTINGS.posID+"  "+DateConverter.currentDateTime();
        String filename1=fileName;
        File path = new File( Environment.getExternalStorageDirectory(), pa );

        File filelocation = new File(path, filename1);
        String file = filelocation.getAbsolutePath();
        GmailClient gmailClient = new GmailClient();
        gmailClient.sendFromGMailInvoices(from,password,to,subject,file,fileName);

    }

}
