package com.platform.base.frramework.trunk.util.date;


import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mylover on 6/16/16.
 */
public class DateUtil {

    public static final String[] DATE_PATTERN2 = new String[] { "yyyyMMddHHmmss" };

    /**
     * 取得时间 yyyyMMddHHmmss
     *
     * @param time
     * @return
     */
    public static String formatTimestamp(long time) {
        return DateFormatUtils.format(time, "yyyyMMddHHmmss");
    }


    /**
     * 获取当前时间
     * @return
     */
    public static String getCreateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = calendar.getTime();
        String timeCreate  = df.format(date);
        return timeCreate;
    }
    
    /**
     * 获取当前日期，格式：yyyy-MM-dd
     * @return
     */
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = calendar.getTime();
        String timeCreate  = df.format(date);
        return timeCreate;
    }
    
    public static String getIdWorkerTime() {
        return org.apache.commons.lang3.time.DateFormatUtils.format(new Date(), "yyyyMMdd");
    }

    /**
     * 判断是否符合日期格式 yyyyMMddHHmmss
     *
     * @param time
     * @return
     */
    public static boolean isFormatTime(String time) {
        try {
            Date d = DateUtils.parseDate(time, DATE_PATTERN2);
            return d != null ? true : false;
        } catch (ParseException e) {
            return false;
        }
    }
    /**
     * 
     * @Title: isLtFiveMin
     * @Description: time和当前时间相比是否小于5分钟
     * @param time
     * @return boolean    返回类型
     */
    public static boolean isLtFiveMin(Timestamp time){
    	Long s = (new Date().getTime() - time.getTime()) / (1000 * 60);
    	return s<5;
    }
    /**
	 * 获取前N小时的时间
	 * （作者：zhangding<zhangding@enn.com>）
	 * @param hours
	 * @return
	 */
	public static String beforeNHourToNowDate(int hours) {
		Calendar calendar = Calendar.getInstance();
		/* HOUR_OF_DAY 指示一天中的小时 */
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY)
				- hours);
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(calendar.getTime());
	}
	/**
	 * 
	 * @Title: dateStringPattern 
	 * @Description: 日期字符串格式化
	 * @author chenming<chenmingf@ennew.cn>
	 * @param date  日期字符串
	 * @param oldPattern 日期字符串现有格式
	 * @param newPattern 日期字符串将要转的格式
	 * @return String    返回类型
	 */
	public static String dateStringPattern(String date, String oldPattern, String newPattern) {   
        if (date == null || oldPattern == null || newPattern == null)   
            return "";   
        SimpleDateFormat sdf1 = new SimpleDateFormat(oldPattern) ; 
        SimpleDateFormat sdf2 = new SimpleDateFormat(newPattern) ;
        Date d = null ;    
        try{    
            d = sdf1.parse(date) ;
        }catch(Exception e){ 
        	return "";
        }    
        return sdf2.format(d);  
    }   
}
