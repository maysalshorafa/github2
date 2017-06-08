package com.pos.leaders.leaderspossystem.OpenFormat;

import android.content.Context;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.AReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.SaleDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UserDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.AReport;
import com.pos.leaders.leaderspossystem.Models.Sale;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by KARAM on 20/04/2017.
 */

public class INI {

    private OutputStreamWriter writer;
    private Context context;
    private int countRowsBKMVDATA;
    private File file;

    private Date from;


    private Date to;

    private int c100,d110, d120 , m100 ,b100,a100, z900;

    public INI(File file, Context context, int countRowsBKMVDATA,long fromDate,long toDate,int c100,int d110, int d120, int m100,int b100,int a100,int z900) throws IOException {
        writer = new OutputStreamWriter(new FileOutputStream(file, true),
                "windows-1252");
        this.context = context;
        this.countRowsBKMVDATA = countRowsBKMVDATA;
        this.file = file;

        this.from = new Date(fromDate);
        this.to = new Date(toDate);

        this.c100 = c100;
        this.d110 = d110;
        this.d120 = d120;
        this.m100 = m100;
        this.b100 = b100;
        this.a100 = a100;
        this.z900 = z900;
    }

    public void make() throws IOException {
        String programName = "LeadPOS";
        String CompilerName = "LeadEngine.v.0.14";
        String programOwnerCP = "515287860";
        String productionName = "Leaders";
        int i = 1;
        String str = "A000" + Util.spaces(5) + String.format(Util.locale,"%015d", this.countRowsBKMVDATA) + SETTINGS.companyID + String.format(Util.locale,"%015d", i) + "&OF1.31&";
        str += String.format(Util.locale,"%08d", 17569158) + String.format("%20s", programName) + String.format("%20s", CompilerName) + programOwnerCP + String.format("%20s", productionName) + 2;
        str += String.format("%50s", file.getPath()) + 0 + "" + 1 + SETTINGS.companyID + String.format(Util.locale,"%09d", 0);
        str += Util.spaces(10) + String.format("%50s",SETTINGS.companyName) + Util.spaces(50) + Util.spaces(10) + Util.spaces(30) + Util.spaces(8) + Util.spaces(4);
        str += DateConverter.getYYYYMMDD(from) + DateConverter.getYYYYMMDD(to) + DateConverter.getYYYYMMDD(new Date()) + DateConverter.getHHMM(new Date());
        str += 0 + "" + 1 + String.format("%20s", "Zip Compress") + "ILS" + 0 + Util.spaces(46)+"\r\n";
        str += "A100" + String.format(Util.locale, "%015d", a100) + "\r\n";
        str += "Z900" + String.format(Util.locale, "%015d", z900) + "\r\n";
        str += "C100" + String.format(Util.locale, "%015d", c100) + "\r\n";
        str += "D110" + String.format(Util.locale, "%015d", d110) + "\r\n";
        str += "D120" + String.format(Util.locale, "%015d", d120) + "\r\n";
        str += "M100" + String.format(Util.locale, "%015d", m100) + "\r\n";
        str += "B100" + String.format(Util.locale, "%015d", b100) + "\r\n";
        str += "B110" + String.format(Util.locale, "%015d", 6) + "\r\n";
        writer.append(str);
        writer.flush();
        writer.close();
    }
}
