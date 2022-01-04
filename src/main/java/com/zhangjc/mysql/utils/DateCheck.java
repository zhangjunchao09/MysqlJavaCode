package com.zhangjc.mysql.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateCheck {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        System.out.println("TimeZone : " + TimeZone.getDefault().getID());


        DateFormat dateFormat0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        DateFormat dateFormat8 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat8.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));


        try {
            long time = 1602725665838L;
            Date dateTime = new Date(time);
            String dateTimeStr = dateFormat0.format(dateTime);
            System.out.println(dateTimeStr);


            Date dateTime1 = dateFormat8.parse(dateTimeStr);
            String dateTimeStr1 = dateFormat8.format(dateTime);
            System.out.println(dateTimeStr1);

            long time2 = dateTime1.getTime();
            Date dateTime2 = new Date(time);
            String dateTimeStr2 = dateFormat0.format(dateTime2);

            Date dateTime3 = dateFormat8.parse(dateTimeStr2);
            String dateTimeStr3 = dateFormat8.format(dateTime);
            System.out.println(dateTimeStr3);
        } catch (ParseException e) {
            e.printStackTrace();
        }



    }
}
