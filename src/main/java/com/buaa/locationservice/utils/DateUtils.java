package com.buaa.locationservice.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期处理
 */
public class DateUtils {
    //1903141452 转为 2019-03-14 14:52
    public static Date getDateYYMMDDHHMM(String dateStr) throws ParseException {
        dateStr = "20"+dateStr;
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.parse(dateStr);
    }

    //yyyy-MM-dd HH:mm:ss
    public static Date getDateYYYYMMDDHHMMSS(String dateStr) throws ParseException {
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(dateStr);
    }

    //yyyy-MM-dd
    public static Date getDateYYYYMMDD(String dateStr) throws ParseException {
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateStr);
    }

    //1903141452 转为 2019-03-14 14:52
    public static Date UTCToLocal(String datestr) throws ParseException {
        datestr = "20"+datestr;
        TimeZone utcZone = TimeZone.getTimeZone("UTC");
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyyMMddHHmm");
        sdf.setTimeZone(utcZone);
        Date date = sdf.parse(datestr);
        //System.out.println(sdf.format(date));
        SimpleDateFormat sdf1 =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        String localTime = sdf1.format(date.getTime());
        //System.out.println(localTime);

        return sdf1.parse(localTime);
    }

    //   078/0138,078是calendar Day 从每年1月1日开计数，含周末，例如1月1日为001
    //   0138分为01与38，01是小时，38是分钟
    //   如果末尾带Z是接受设备数据时间
    //   结果由utc转为北京时间（+8h）
    public static String StringToDateStr(String str) {
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tmp = str;
        if (tmp.endsWith("Z")) {
            tmp = tmp.replaceAll("Z","");
        }
        String[] tmps = tmp.split("/");
        int days = Integer.parseInt(tmps[0]);
        int hours = Integer.parseInt(tmps[1].substring(0,2));
        int minutes = Integer.parseInt(tmps[1].substring(2,4));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH,0);
        cal.set(Calendar.DATE,1);
        cal.add(Calendar.DATE,days-1);
        cal.set(Calendar.HOUR_OF_DAY,hours);
        cal.set(Calendar.MINUTE,minutes);
        cal.set(Calendar.SECOND,0);
        cal.add(Calendar.HOUR_OF_DAY,8);
        return sdf.format(cal.getTime());
    }
}
