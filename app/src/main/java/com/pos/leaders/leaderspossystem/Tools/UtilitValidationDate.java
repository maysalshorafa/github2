package com.pos.leaders.leaderspossystem.Tools;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.sql.Timestamp;

/**
 * Created by Win8.1 on 3/29/2020.
 */

public class UtilitValidationDate {

    public static String isValidDate(Timestamp orderDate){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        TimeZone tz = TimeZone.getDefault();
        Date timeZ = null,reslt=null;
        String TimeVal = null;
        TimeZone tz1 = TimeZone.getDefault();
        Date now = new Date();
        int offsetFromUtc = tz1.getOffset(now.getTime()) / 3600000;
        String m2tTimeZoneIs = Integer.toString(offsetFromUtc);
        Log.d("timeZone",m2tTimeZoneIs);
        int mins = (tz1.getOffset(now.getTime())%3600000)/600000;
        Log.d("minsZone",mins+"m");






    if (offsetFromUtc<0){
        TimeVal = "0"+Math.abs(Integer.parseInt(m2tTimeZoneIs))+":"+mins+"0"+":"+"00";
        Log.d("timeValabs",TimeVal);
        try {
            timeZ = sdf2.parse( TimeVal );
            reslt = format.parse(format.format(orderDate) );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("offsetFromUtcabs",offsetFromUtc+"");
        reslt.setHours(Math.abs(reslt.getHours() + offsetFromUtc) );
        reslt.setMinutes( reslt.getMinutes() + timeZ.getMinutes());
        reslt.setSeconds( reslt.getSeconds() + timeZ.getSeconds());
    }
			else {
        TimeVal="0"+m2tTimeZoneIs+":"+mins+"0"+":"+"00";
        Log.d("timeVal",TimeVal);
        try {
            timeZ = sdf2.parse( TimeVal );
            reslt = format.parse(format.format(orderDate) );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("offsetFromUtc",offsetFromUtc+"");
        reslt.setHours(reslt.getHours() + offsetFromUtc);
        reslt.setMinutes( reslt.getMinutes() + timeZ.getMinutes());
        reslt.setSeconds( reslt.getSeconds() + timeZ.getSeconds());
    }

    return format.format(reslt);
    }
}
