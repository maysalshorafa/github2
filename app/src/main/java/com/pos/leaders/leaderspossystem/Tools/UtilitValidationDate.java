package com.pos.leaders.leaderspossystem.Tools;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Win8.1 on 3/29/2020.
 */

public class UtilitValidationDate {

    public static String isValidDate(Timestamp orderDate){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        Date timeZ = null,reslt=null;
        String TimeVal = null;
        TimeZone tz1 = TimeZone.getDefault();
        Date now = new Date();
        int offsetFromUtc = tz1.getOffset(now.getTime()) / 3600000;
        String m2tTimeZoneIs = Integer.toString(offsetFromUtc);
        int mins = (tz1.getOffset(now.getTime())%3600000)/600000;


        ////if offset negative number
    if (offsetFromUtc<0){
        TimeVal = "0"+Math.abs(Integer.parseInt(m2tTimeZoneIs))+":"+mins+"0"+":"+"00";
        try {
            timeZ = sdf2.parse( TimeVal );
            reslt = format.parse(format.format(orderDate) );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        reslt.setHours(Math.abs(reslt.getHours() + offsetFromUtc) );
        reslt.setMinutes( reslt.getMinutes() + timeZ.getMinutes());
        reslt.setSeconds( reslt.getSeconds() + timeZ.getSeconds());
    }

    else {
        TimeVal="0"+m2tTimeZoneIs+":"+mins+"0"+":"+"00";
        try {
            timeZ = sdf2.parse( TimeVal );
            reslt = format.parse(format.format(orderDate) );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        reslt.setHours(reslt.getHours() + offsetFromUtc);
        reslt.setMinutes( reslt.getMinutes() + timeZ.getMinutes());
        reslt.setSeconds( reslt.getSeconds() + timeZ.getSeconds());
    }

    return format.format(reslt);
    }
}
