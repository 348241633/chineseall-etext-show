package com.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by eric on 2015-10-21.
 */
public class DateUtils {

    public static Date getNowDate() {
        return new Date();
    }

    public static Date getDateFormat(String date,String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date dd = null;
        try {
            dd = formatter.parse(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return dd;
    }

    /**
     * 返回yyyy-MM-dd HH:mm:ss 格式当前时间
     * @return
     */
    public static String getDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date Now = new Date();
        String NDate = formatter.format(Now);
        return NDate;
    }

    public static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date Now = new Date();
        String NDate = formatter.format(Now);
        return NDate;
    }

    public static String getDateStr(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        String NDate = formatter.format(date);
        return NDate;
    }

    public static String getDateNumbers() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date Now = new Date();
        String NDate = formatter.format(Now);
        return NDate;
    }

    public static String getIndexDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date Now = new Date();
        String NDate = formatter.format(Now);
        return NDate;
    }

    public static long getSystemNow(){
        Date now = new Date();
        return now.getTime();
    }


    /**
     * 生成短信、邮件发送批次号
     * @return
     */
    public static String getBatchNo() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    /**
     * 比较报名时间
     */
    public static boolean compareDate(String startDate,String endDate){
        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String startDay = startDate.substring(0, 10).replace("-","");
        String endDay = endDate.substring(0, 10).replace("-", "");
        if(Integer.valueOf(dateStr)<Integer.valueOf(startDay) || Integer.valueOf(dateStr)>Integer.valueOf(endDay))return false;
        String hour = new SimpleDateFormat("HH").format(new Date());
        String startHour = startDate.substring(11, 13);
        String endHour = endDate.substring(11, 13);
        return !(Integer.valueOf(hour) < Integer.valueOf(startHour) || Integer.valueOf(hour) >= Integer.valueOf(endHour));
    }

    public static void main(String ages[]) {
        System.out.print(compareDate("2016-09-17 08:00","2016-09-22 23:00"));
    }
}
