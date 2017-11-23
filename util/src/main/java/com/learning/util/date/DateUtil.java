package com.learning.util.date;

import com.learning.util.basic.ObjectUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by taylor on 8/7/16.
 * twitter: @taylorwang789
 */
public class DateUtil {


    public static Date toDate(String date , String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat) ;
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static long toMillisecond(String date , String dateFormat) {
        return toDate(date,dateFormat).getTime();
    }

    public static String toString(Date date , String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat) ;
        return sdf.format(date);
    }

    public static String todayNowMillisecond(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        long todayStart = 0;
        try {
            Date dt = sdf.parse(sdf.format(new Date()));
            todayStart = dt.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(System.currentTimeMillis() - todayStart);
    }
    public static synchronized String sequenceNumber(){
        Date today = new Date();
        String todayYmd = toString(today,"yyyyMMdd");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        long todayStart = 0;
        try {
            Date dt = sdf.parse(sdf.format(today));
            todayStart = dt.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return todayYmd+String.valueOf(System.currentTimeMillis() - todayStart);
    }

    public static Date getPreOrNextDate(Date date,Integer n){
        if (ObjectUtil.isEmpty(date) || ObjectUtil.isEmpty(n)){
            return null;
        }
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,n);
        return calendar.getTime();
    }

}
