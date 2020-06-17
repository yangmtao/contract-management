package com.bj.common.util;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
    /**
     * 国际化时间格式 2017-11-7T17:02:07+08:00
     *
     * @Date:17:01 2017/11/7
     */
    public static final String YMDHMSXXX = "yyyy-MM-dd'T'HH:mm:ssXXX";
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String YMD = "yyyy-MM-dd";

    /**
     * dd-MM-yyyy
     */
    public static final String DMY = "dd-MM-yyyy";
    /**
     * MM-dd-yyyy
     */
    public static final String MDY = "MM-dd-yyyy";
    /**
     * dd-MM-yyyy HH:mm
     */
    public static final String DMYHM = "dd-MM-yyyy HH:mm";



    /**
     * 时间格式化
     *
     * @param date
     * @param formatPattern
     * @return java.lang.String
     * @Date:17:10 2017/11/7
     */
    public static String format(Date date, String formatPattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat f = new SimpleDateFormat(formatPattern);
        return f.format(date);
    }

    /**
     * 文本转换成时间
     *
     * @param text
     * @param formatPattern
     * @return java.util.Date
     * @Date:17:11 2017/11/7
     */
    public static Date parse(String text, String formatPattern)  {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        try {
            SimpleDateFormat f = new SimpleDateFormat(formatPattern);
            return f.parse(text);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return   new Date();
    }
    /**
     * 时间格式化
     *
     * @param date
     * @param formatPattern
     * @return java.util.Date
     * @Date:17:11 2017/11/7
     */
    public static Date parseDate(Date date, String formatPattern) throws ParseException {
        return parse(format(date,formatPattern),formatPattern);
    }



    /**
     * 当前时间+day,指定hour，分秒置为零
     *
     * @param day
     * @param hour
     * @return
     */
    public static Date addDay(int day, int hour) {
        // 取时间
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * 当前时间+day,时分秒置为零
     *
     * @param day
     * @return
     */
    public static Date addDay(int day) {
        // 取时间
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        // 把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.DATE, day);
        // 这个时间就是日期往后推一天的结果
        return calendar.getTime();
    }


    /**
     * 指定时间+day 时分秒置为零
     *
     * @param day
     * @param date
     * @return
     */
    public static Date addDay(int day, Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        // 把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.DATE, day);
        // 这个时间就是日期往后推一天的结果
        return calendar.getTime();
    }

    /**
     * 指定时间+day 时分秒不变
     *
     * @param day
     * @param date
     * @return
     */
    public static Date addDay2(int day, Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        // 把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.DATE, day);
        // 这个时间就是日期往后推一天的结果
        return calendar.getTime();
    }
    /**
     * 两个时间比较，获取符合要求的时间
     * @param pattern 时间格式化模板,例："yyyy-MM-dd HH:mm:ss"
     * @param referenceTime 参照时间
     * @param comparisonTime 被比较的时间
     * @param flag true 返回小的时间,false 返回大的时间
     * @return resultTime
     */
    public static String compareTime(String pattern,String referenceTime,String comparisonTime,boolean flag){
        String resultTime="";
        SimpleDateFormat sdf= new SimpleDateFormat(pattern);
        try {
            if(StringUtils.isNotBlank(referenceTime)){
                long referenceTimeLong = sdf.parse(referenceTime).getTime();
                if(StringUtils.isNotBlank(comparisonTime)){
                    long comparisonTimeLong= sdf.parse(comparisonTime).getTime();
                    if(flag){
                        if(comparisonTimeLong < referenceTimeLong){
                            resultTime=comparisonTime;
                        }else{
                            resultTime=referenceTime;
                        }
                    }else if (!flag){
                        if(comparisonTimeLong > referenceTimeLong){
                            resultTime=comparisonTime;
                        }else{
                            resultTime=referenceTime;
                        }
                    }
                }
            }else{
                resultTime=comparisonTime;
            }
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }finally {
            return resultTime;
        }
    }
    /**
     * 获取当前时间标准的时间字符串
     * @return
     */
    public static  String  getStandNowTime(){
        return DateUtil.format(new Date(), DateUtil.YMDHMS);
    }

    public static  String  getYearMonthDay(){
        return DateUtil.format(new Date(),"yyyy-MM-dd");
    }

    public static String nowDate(){
        return new SimpleDateFormat(YMDHMS).format(new Date());
    }

    public static void main(String[] args) {
//        int year= LocalDate.now().getYear();
//        int month = LocalDate.now().getMonthValue();
//        int nextMonth = LocalDate.now().plusMonths(1l).getMonthValue();
//        System.out.println(year);
//        System.out.println(month);
//        System.out.println(nextMonth);
//        //开始日期
//        LocalDate startLocalDate = LocalDate.parse("2019-12-05",DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        //设置截止日期
//        LocalDate endLocalDate = startLocalDate.plusMonths(1l);
//        System.out.println(endLocalDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        LocalDate endLocalDate = LocalDate.parse("2020-04-01",DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(-1l);
        System.out.println(endLocalDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}
