package com.akazam.wap.framework.core.utils;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author xsun
 * @since 2006-11-24 14:02:58
 */
public class DateUtil
{
    public static final String SHORT_DATE = "yyyy-MM-dd";
    public static final String SHORT_DATE_ZMS = "yyyyMMdd";
    public static final String LONG_DATE = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat DF_SHORT_CN_ZMS = new SimpleDateFormat(SHORT_DATE_ZMS, Locale.US);
    public static final SimpleDateFormat DF_SHORT_CN = new SimpleDateFormat(SHORT_DATE, Locale.US);
    public static final SimpleDateFormat DF_CN = new SimpleDateFormat(LONG_DATE, Locale.US);

    private DateUtil() 
    {
    }

    /**
     * Calendar -> String
     */
    public static String format(Calendar cal) 
    {
        return format(cal.getTime());
    }
     
	/**
	 * 转换日期和时间为字符串，格式为： "yyyy-MM-dd HH:mm".
	 */
	public static String format(long d) {
		return new SimpleDateFormat(LONG_DATE).format(d);
	}
	
	public static String format(String formater,long d)
	{
		return new SimpleDateFormat(formater).format(d);
	}

    /**
     * Calendar,String -> String
     */
    public static String format(Calendar cal, String pattern) 
    {
        return format(cal.getTime(),pattern);
    }

    /**
     * Calendar,DateFormat -> String
     */
    public static String format(Calendar cal,DateFormat df)
    {
        return format(cal.getTime(),df);
    }

    /**
     * Date -> String
     */
    public static String format(Date date) 
    {
        return format(date, DF_CN);
    }

    /**
     * Date,String -> String
     */
    public static String format(Date date, String pattern) 
    {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return format(date,df);
    }

    /**
     * Date,DateFormat -> String
     */
    public static String format(Date date, DateFormat df) 
    {
        if(date == null) return "";

        if (df != null) 
        {
            return df.format(date);
        }
        return DF_CN.format(date);
    }

    /**
     * String -> Calendar
     */
    public static Calendar parse(String strDate)
    {
        return parse(strDate,null);
    }

    /**
     * String,DateFormate -> Calendar
     */
    public static Calendar parse(String strDate, DateFormat df) 
    {
        Date date = parseDate(strDate, df);
        if(date == null) return null;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    /**
     * String -> Date
     */
    public static Date parseDate(String strDate) 
    {
        return parseDate("yyyy-MM-dd HH:mm:ss",strDate);
    }

    /**
     * String,DateFormate -> Date
     */
    public static Date parseDate(String strDate, DateFormat df)
    {
        if(df == null)
            df = DF_CN;
        ParsePosition parseposition = new ParsePosition(0);

        return df.parse(strDate,parseposition);
    }
	public static Date parseDate(String format, String d) {
		try {
			return new SimpleDateFormat(format).parse(d);
		} catch (Exception e) {
		}
		return null;
	}
    public static Calendar parseDateString(String str,String format)
    {
      if (str == null)
      {
        return null;
      }
      Date date = null;
      SimpleDateFormat df = new SimpleDateFormat(format);
      try
      {
        date = df.parse(str);
      }
      catch (Exception ex)
      {

      }
      if (date == null)
      {
        return null;
      }
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      return cal;
    }
    /**
     * returns the current date in the default format
     */
    public static String getToday()
    {
        return format(new Date());
    }

    public static Date getYesterday() 
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);

        return cal.getTime();
    }

    public static Calendar getFirstDayOfMonth() 
    {
        Calendar cal = getNow();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);


        return cal;
    }
    
    public static Calendar getNow(){
    	return Calendar.getInstance();
    }
    /**
     * add some month from the date
     */
    public static Date addMonth(Date date, int n) throws Exception
    {
        Calendar cal = getNow();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }

    public static int daysBetween(Date returnDate) 
    {
        return daysBetween(null, returnDate);
    }

    public static int daysBetween(Date now, Date returnDate)
    {
        if(returnDate == null) return 0;

        Calendar cNow = getNow();
        Calendar cReturnDate = getNow();
        if(now != null) {
            cNow.setTime(now);
        }
        cReturnDate.setTime(returnDate);
        setTimeToMidnight(cNow);
        setTimeToMidnight(cReturnDate);
        long nowMs = cNow.getTimeInMillis();
        long returnMs = cReturnDate.getTimeInMillis();
        return millisecondsToDays(nowMs - returnMs);
    }

    private static int millisecondsToDays(long intervalMs) 
    {
        return (int) (intervalMs / (1000 * 86400));
    }

    private static void setTimeToMidnight(Calendar calendar) 
    {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
    }
    
    public static String formatDate(Object obj,String format)
    {
    	String result="";
    	try
    	{
    		Date date=(Date)obj;
    		result=format(date, format);
    	}
    	catch(Exception e)
    	{
    		
    	}
    	return result;
    }
    
    public static String formatDate(Object obj)
    {
       return formatDate(obj,SHORT_DATE);
    }
    
    public static String getSunday(String date){
    	Calendar c = DateUtil.parseDateString(date, "yyyy-MM-dd");
		int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayofweek == 0){
			dayofweek = 0;
		}
		c.add(Calendar.DATE, -dayofweek );
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(c.getTime());
    }
    
    /**
     * 根据粒度显示某个日期对应的第一天
     * @param d
     * @param interval
     * @return
     */
    public static Date getFirstDay(Date d, String interval){
    	if(interval==null) return null;
    	interval = interval.trim().toLowerCase();
    	
    	if(interval.equals("daily")) return d;
    	else if(interval.equals("weekly")) return weekdayDate(1, d);
    	else if(interval.equals("monthly")) return getFirstDayOfMonth(d);
    	else if(interval.equals("custom")) return d;
    	return null;
    }
    
    /**
     * 根据粒度显示某个日期对应的第一秒
     * @param d
     * @param interval
     * @return
     */
    public static Date getFirstSecond(Date d, String interval){
    	if(interval==null) return null;
    	interval = interval.trim().toLowerCase();
    	if(interval.trim().equals("custom")){
    		interval = "daily";
    	}
    	Date date = new Date();
    	if(interval.equals("daily")) date = d;
    	else if(interval.equals("weekly")) date = weekdayDate(1, d);
    	else if(interval.equals("monthly")) date = getFirstDayOfMonth(d);
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	setTimeToMidnight(cal);
    	return cal.getTime();
    }
    
    /**
     * 根据粒度显示某个日期对应的最后一秒
     * @param d
     * @param interval
     * @return
     */
    public static Date getLastSecond(Date d, String interval){
    	if(interval==null || d==null) return null;
    	interval = interval.trim().toLowerCase();
    	Date date = d;
    	Calendar cal = Calendar.getInstance();
    	if(interval.equals("daily")){
    		cal.setTime(d);
    		setTimeToMidnight(cal);
    		cal.add(Calendar.DATE, 1);
    		cal.add(Calendar.SECOND, -1);
    		return cal.getTime();
    	}
    	else if(interval.equals("weekly")){
    		date = weekdayDate(7, d);
    		cal.setTime(date);
    		setTimeToMidnight(cal);
    		cal.add(Calendar.DATE, 1);
    		cal.add(Calendar.SECOND, -1);
    		return cal.getTime();
    	}
    	else if(interval.equals("monthly")){
    		date = getFirstDayOfMonth(d);
    		cal.setTime(date);
    		setTimeToMidnight(cal);
    		cal.add(Calendar.MONTH, 1);
    		cal.add(Calendar.SECOND, -1);
    		return cal.getTime();
    	}
    	return null;
    }
    
    /**
     * by dzhu
     * 获取d所在周某天所对应的日期
     * 注意：星期一作为第一天
     * @param weekday
     * @param d
     * @return
     * 
     */
    public static Date weekdayDate(int weekday, Date d){
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);

		int wd = weekday;
		int twd = cal.get(Calendar.DAY_OF_WEEK);
		if(twd%7!=1){
			if(wd%7!=0)
				cal.set(Calendar.DAY_OF_WEEK, wd + 1);
			else{
				cal.add(Calendar.WEEK_OF_MONTH, 1);
				cal.set(Calendar.DAY_OF_WEEK, 1);
			}				
		}else{
			if(wd%7!=0){
				cal.add(Calendar.WEEK_OF_MONTH, -1);
				cal.set(Calendar.DAY_OF_WEEK, wd + 1);
			}
		}
		return 	cal.getTime();
    }
    
    /**
     * 获取日期所在月的第一天
     * @param d
     * @return
     */
    public static Date getFirstDayOfMonth(Date d){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(d);
    	cal.set(Calendar.DAY_OF_MONTH, 1);
    	return cal.getTime();
    }  
    
    /**
     * 获取日期所在年的第一天
     * @param d
     * @return
     */
    public static Date getFirstDayOfYear(Date d){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(d);
    	cal.set(Calendar.MONTH, 1);
    	cal.set(Calendar.DAY_OF_MONTH, 1);
    	cal.set(Calendar.SECOND, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	return cal.getTime();
    } 
    public static int getDaysBetweenDate(String startDate,String endDate)
    {
    	int count = 1;
    	Calendar cal = Calendar.getInstance();
    	
    	cal=DateUtil.parseDateString(startDate, "yyyy-MM-dd");
    	while(!startDate.equals(endDate))
    	{
    		cal.add(Calendar.DATE, 1);
    		Date curDate = cal.getTime();
    		startDate = DateUtil.format(curDate, "yyyy-MM-dd");
    		count++;
    		curDate = null;
    	}
    	return count;
    }
    
    public static long setHHMM2Long(String startDate,String splitstr)
    {
    	long s=Integer.parseInt(startDate.substring(0,startDate.indexOf(splitstr)))*3600;    //小时
    	s+=Integer.parseInt(startDate.substring(startDate.indexOf(splitstr)+1))*60;    //分钟
    	return s;
    }
    
    public static String getLong2HHMM(long time)
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        return sdf.format(time);
    }
    
    public static void main(String[] args) 
    {
        //System.out.println(DateUtils.getYesterday());
        Calendar cal = getNow();
//        int count = DateUtils.getDaysBetweenDate("2010-07-01", "2010-07-30");
//        System.out.println(cal.getTimeInMillis());
//        Long ts = cal.getTimeInMillis();
//        Date date = new Date(ts);
//        System.out.println(DateUtils.format(date));
//        System.out.println("----------");
//        cal.set(Calendar.MONTH, 11);
//        cal.set(Calendar.DAY_OF_MONTH, 10);
//        System.out.println(DateUtils.format(cal));
//        System.out.println(DateUtils.daysBetween(cal.getTime()));
//        System.out.println(DateUtils.parseDateString("2008-6-31", DateUtils.SHORT_DATE)==null );
//        System.out.println(DateUtils.format(DateUtils.parseDateString("2008-6-31", DateUtils.SHORT_DATE)));
//        System.out.println(DateUtils.getLastSecond(new Date(), "weekly"));
        //System.out.println(DateUtils.setHHMM2Long(cal.getTimeInMillis()));
        System.out.println(DateUtil.getLong2HHMM(cal.getTimeInMillis()));
    }
}