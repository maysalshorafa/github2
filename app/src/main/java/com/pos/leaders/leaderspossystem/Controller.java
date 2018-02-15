package com.pos.leaders.leaderspossystem;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.pos.leaders.leaderspossystem.Tools.SETTINGS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Controller extends Application {

    private static Thread.UncaughtExceptionHandler defaultHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        if (defaultHandler == null) {
            defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        }
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                Log.d("Test",e.toString());
                File externalStorageDir = Environment.getExternalStorageDirectory();
                File myFile = new File(externalStorageDir , "posexceptionfile.txt");

                if(myFile.exists())
                {
                    try
                    {
                        FileOutputStream fOut = new FileOutputStream(myFile,true);
                        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                        myOutWriter.append("\n"+"New Exception :"+"\n");
                        myOutWriter.append(e.toString());
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
                        myOutWriter.close();
                        fOut.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                defaultHandler.uncaughtException(t, e); //this will show crash dialog.
            }
        });
    }

}