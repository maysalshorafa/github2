package com.pos.leaders.leaderspossystem.Tools;

import android.widget.DatePicker;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


/**
 * Created by KARAM on 16/10/2016.
 * Using Unix-Date System 05/1/2017.
 */

public class DateConverter {

    private static final long MILLISECOND = 1000L;
    private static final long MINUTE = MILLISECOND * 60L;
    private static final long HOUR = MINUTE * 60L;
    private static final long DAY = HOUR * 24L;
    private static final long MONTH = DAY * 30L;
    private static final long YEAR = MONTH * 12L;

    private static final Locale local = new Locale("en");

    //region Unix-Date System

    public static String dateToString(long date){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy",local);
        //get current date time with Date()

        return dateFormat.format(date);
	}

	public static Date stringToDate(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",new Locale("en"," "));
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
        SimpleDateFormat dateFormat6 = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        SimpleDateFormat dateFormat4 = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy",Locale.ENGLISH);
        SimpleDateFormat dateFormat5 = new SimpleDateFormat("dd-MM-yyyy",Locale.ENGLISH);
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);

        //Log.i("Sale Date",date);
        try {
            return new Date(Long.parseLong(date));
        }
        catch (NumberFormatException nfe) {
           // Log.e(nfe.getLocalizedMessage(),nfe.getMessage());
        }
        try {
            //Log.i("Sale Date",dateFormat.parse(date).getTime()+"");
            return dateFormat.parse(date);
        } catch (ParseException p) {
           // Log.e(p.getLocalizedMessage(),p.getMessage());
        }
        try {
            //Log.i("Sale Date",dateFormat2.parse(date).getTime()+"");
            return dateFormat2.parse(date);
        } catch (ParseException pe) {
           // Log.e(pe.getLocalizedMessage(),pe.getMessage());
        }
        try {
           // Log.i("Sale Date",dateFormat3.parse(date).getTime()+"");
            return dateFormat3.parse(date);
        } catch (ParseException pex) {
          //  Log.e(pex.getLocalizedMessage(),pex.getMessage());
        }
        try {
           // Log.i("Sale Date",dateFormat4.parse(date).getTime()+"");
            return dateFormat4.parse(date);
        } catch (ParseException pex) {
           // Log.e(pex.getLocalizedMessage(),pex.getMessage());
        }
        try{
           // Log.i("Sale Date",dateFormat5.parse(date).getTime()+"");
            return dateFormat5.parse(date);
        } catch (ParseException pex) {
          //  Log.e(pex.getLocalizedMessage(),pex.getMessage());
        }
        try{
           // Log.i("Sale Date",dateFormat6.parse(date).getTime()+"");
            return dateFormat6.parse(date);
        } catch (ParseException pex) {
          //  Log.e(pex.getLocalizedMessage(),pex.getMessage());
        }
        //Log.i("aaa", Arrays.toString(SimpleDateFormat.getAvailableLocales()));
        return null;
	}

    public static String toDate(Date date){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            String d = dateFormat.format(date);
            return d;
        }
        catch (Exception e){

        }
        return "";
    }

    public static long dateToLong(Date date){
        return date.getTime();
    }

    public static long getDateDifferent(long d1,long d2){
        long d = (d1-d2);
        return d;
    }

    public static boolean validDateRange(long d1,long d2){
        long date=currentDate();
        if(d1>date&&d2<currentDate())
            return true;
        else
            return false;
    }

    public static long currentDate(){
        return new Date().getTime();
    }

    public static Date addMonth(Date date){
        return addUnitToDate(date,MONTH,1);
    }

    public static Date addMonth(Date date, int numberOfUnit){
        return addUnitToDate(date,MONTH,numberOfUnit);
    }

    public static Date addYear(Date date){
        return addUnitToDate(date,YEAR,1);
    }

    public static Date addYear(Date date, int numberOfUnit){
        return addUnitToDate(date,YEAR,numberOfUnit);
    }

    private static Date addUnitToDate(Date date,long unit,int numberOfUnit){
        long d = date.getTime() + (numberOfUnit * unit);
        return new Date(d);
    }

    //endregion unix date system

    public static String DateToString(Date d){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
        String date = dateFormat.format(d);
        return date;
    }
    public static String DateToString(long d){

        return DateToString(new Date(d));
    }
    /*


    public static Date StringToDate(String s){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(s);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
			//return new Date();
        }
        return convertedDate;
    }

	public static Date StringToDate2(String s){
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

		Date convertedDate = new Date();
		try {
			convertedDate = dateFormat.parse(s);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return new Date();
		}
		return convertedDate;
	}

	public static Date StringToDate3(String s){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date convertedDate = new Date();
		try {
			convertedDate = dateFormat.parse(s);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return new Date();
		}
		return convertedDate;
	}*/


    public static String currentDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",local);
        //get current date time with Date()
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String geDate(Date date){
        SimpleDateFormat localDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String d = localDateFormat.format(date);
        return d;
    }
    public static String getTime(Date date){
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss",local);
        String time = localDateFormat.format(date);
        return time;
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime()+1 - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

	public static Date getDateFromDatePicker(DatePicker datePicker){
		int day = datePicker.getDayOfMonth();
		int month = datePicker.getMonth();
		int year =  datePicker.getYear();

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);

		return calendar.getTime();
	}

	public static String getBeforeMonth() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",local);
		//get current date time with Date()
		Date date = new Date();
		long datelong=date.getTime();
		datelong=datelong-2629746000L;//1 month
		return dateFormat.format(new Date(datelong));
	}

	/*public static String getAfterMonth(Date date){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//get current date time with Date()
		// 1 month
		return dateFormat.format(new Date(date.getTime()+2629746000L));
	}*/

	public static Date getAfterMonth(Date date){
		// 1 month
       DateTime newDate = new DateTime(date);
		return (newDate.plusMonths(1).toDate());
	}

	public static boolean dateBetweenTwoDates(Date after,Date before,Date current){
		return current.after(after) && current.before(before);
	}

    public static int getCurrentMonth(){
        return DateTime.now().getMonthOfYear();
    }

    public static int getCurrentYear(){
        return DateTime.now().getYear();
    }

    public static int getYearWithTowChars(){
        return DateTime.now().getYear() % 100;
    }

    public static String getMMDDhhmm(){
        DateFormat dateFormat = new SimpleDateFormat("MMddHHmm",new Locale("en"));
        return dateFormat.format(new Date());
    }

    public static String getYYYYMMDD(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd",local);
        return dateFormat.format(date);
    }

    public static String getHHMM(Date date){
        DateFormat dateFormat = new SimpleDateFormat("HHmm",local);
        return dateFormat.format(date);
    }

}
