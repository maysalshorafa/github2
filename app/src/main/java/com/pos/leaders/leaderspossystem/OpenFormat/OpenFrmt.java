package com.pos.leaders.leaderspossystem.OpenFormat;

import android.os.Environment;

import com.pos.leaders.leaderspossystem.Tools.Compress;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;

import java.io.File;

/**
 * Created by KARAM on 20/04/2017.
 */

public class OpenFrmt {

    //https://developer.android.com/reference/android/os/Environment.html#getExternalStorageDirectory()

    private static final String OPENFORMAT_DIRECTORY_FULL = File.separator + "OPENFRMT" + File.separator +
            SETTINGS.companyID.substring(0, 8) + "." + DateConverter.getYearWithTowChars() + File.separator +
            DateConverter.getMMDDhhmm();
    private static File openFormatDir = new File(Environment.getExternalStorageDirectory() + OPENFORMAT_DIRECTORY_FULL);

    private File fileINI;
    private File fileBKMVDATE;
    private File file5dot4;
    private File file2dot6;


    public OpenFrmt() throws Exception {
        makeFiles();
    }

    public void Compress(){
        new Compress(new String[]{getBKMVDATA().getPath(), getINI().getPath()}, openFormatDir + File.separator + "OpenFormat.zip").zip();
    }

    private boolean initFolder(){
        if(!openFormatDir.exists()){
            if (openFormatDir.mkdirs()) {
                //success to create root folder
                return true;
            } else {
                //fail to create the root folder
                return false;
            }
        }
        return true;
    }

    private boolean makeFiles() throws Exception {
        if(initFolder()){
            fileINI = new File(openFormatDir + File.separator + "INI.txt");
            if (!fileINI.createNewFile())
                throw new Exception();
            fileBKMVDATE = new File(openFormatDir + File.separator + "BKMVDATA.txt");
            if (!fileBKMVDATE.createNewFile())
                throw new Exception();
            file5dot4 = new File(openFormatDir + File.separator + "file5dot4.pdf");
            if (!file5dot4.createNewFile())
                throw new Exception();
            file2dot6 = new File(openFormatDir + File.separator + "file2dot6.pdf");
            if (!file2dot6.createNewFile())
                throw new Exception();
            return true;
        }
        return false;
    }

    public File getINI(){
        return fileINI;
    }

    public File getBKMVDATA(){
        return fileBKMVDATE;
    }

    public File get5dot4(){
        return file5dot4;
    }

    public File get2dot6() {
        return file2dot6;
    }

    public String getOpenFormatDir(){
        return openFormatDir.getPath();
    }

}
