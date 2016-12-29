package com.cbscap.util;

import  java.util.*;
import java.text.*;

public class LoanDateUtil {

    private static final long	MILLISECOND_OF_DAY	= 24 * 60 * 60 * 1000;

	private static final int	GMT_OFFSET			= 9 * 60 * 60 * 1000;						// GMT를 기준으로 9시간 후

	private static final Locale	CURRENT_LOCALE		= Locale.KOREA;

	private static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";

    private static final SimpleTimeZone SIMPLE_TIME_ZONE = new SimpleTimeZone(GMT_OFFSET, "KST");
    
    public static Calendar toCalendar(String strDate) {
        int year  = Integer.parseInt(strDate.substring(0,4));
        int month = Integer.parseInt(strDate.substring(4,6));
        int day   = Integer.parseInt(strDate.substring(6,8));
        
        Calendar calendar = Calendar.getInstance();
        
        calendar.set(year, month-1, day, 0, 0, 0);    
        
        return calendar;
    }// END OF toCalendar()
    
    public static String getCurrentDate() {
        
        Calendar calendar = Calendar.getInstance(SIMPLE_TIME_ZONE);
        
        return getFormatDate(calendar, DEFAULT_DATE_FORMAT);
    }// END OF getCurrentDate()
    
    public static String getCurrentDate(String strDateFormat) {
        
        Calendar calendar = Calendar.getInstance(SIMPLE_TIME_ZONE);
        
        return getFormatDate(calendar, strDateFormat);
    }// END OF getCurrentDate()
    
    public static String getFormatDate(Calendar calDate, String strDateFormat) {
        Date date = calDate.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strDateFormat);
		
		simpleDateFormat.setTimeZone(SIMPLE_TIME_ZONE);

		return simpleDateFormat.format(date);
    }// END OF getFormatDate()
    
    public static int getDayOfWeek(String strDate) {
        
        Calendar calendar = toCalendar(strDate);
        
        return calendar.get(Calendar.DAY_OF_WEEK)-1;  //  0.SUN ... 6.SAT
    }// END OF getDayOfWeek()
    
    public static String getAddDate(String strDate, int intDateField, int intOffSet) {
        
        Calendar calendar = toCalendar(strDate);
        
        calendar.add(intDateField, intOffSet);
        
        return getFormatDate(calendar, DEFAULT_DATE_FORMAT);
    }// END OF getAddDate()
    
    public static int getDayTerm(String strDateFrom, String strDateTo) {
        
        Calendar calendarFrom = toCalendar(strDateFrom);
        Calendar calendarTo   = toCalendar(strDateTo  );
        
        long lngFrom = calendarFrom.getTimeInMillis();
        long lngTo   = calendarTo.getTimeInMillis();
        
        return (int) ((lngTo - lngFrom) / MILLISECOND_OF_DAY);
    }// END OF getDayTerm()
    
    public static String getLastDate(String strDate) {
        
        Calendar calendar = toCalendar(strDate.substring(0,6)+"01");
        
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        
        return getFormatDate(calendar, DEFAULT_DATE_FORMAT);
    }// END OF getDayTerm()
    
    //  특정일자의 차이(개월(000)&일(00)) GET!!!
    public static String getMonthDayTerm(String strDateFrom, String strDateTo) {
        String monthDay = "";
        
        String tmpAddDate   = strDateFrom;
        String tmpBefDate   = strDateFrom;
        int intAddMonth     = 0;
        int intAddDay       = 0;
        
        //while(Integer.parseInt(tmpDateFrom)<Integer.parseInt(strDateTo)) {
        while(Integer.parseInt(tmpAddDate)<Integer.parseInt(strDateTo)) {    
            tmpAddDate = getAddDate(strDateFrom, Calendar.MONTH, ++intAddMonth);
            
            if(Integer.parseInt(tmpAddDate)<Integer.parseInt(strDateTo)) {
                
            }else if(Integer.parseInt(tmpAddDate)==Integer.parseInt(strDateTo)) {
                break;
            }else if(Integer.parseInt(tmpAddDate)>Integer.parseInt(strDateTo)) {
                intAddMonth--;
                intAddDay = getDayTerm(tmpBefDate, strDateTo);
                break;
            }
            
            tmpBefDate = tmpAddDate;
        }// END OF while LOOP
        
        monthDay = getPadData(""+intAddMonth, "L", 3, "0") + getPadData(""+intAddDay, "L", 2, "0");
        
        return monthDay;
    }// END OF getMonthDayTerm();
	
	//  데이터 채워넣기
	public static String getPadData(String strData, String strLeftRightFlag, int intCompleteLength, String strPutData) {
	    if(strData == null) {
	        strData = "";
	    }
	    
	    StringBuffer stb = new StringBuffer();
	    stb.append(strData);
	    
	    while(stb.toString().getBytes().length<intCompleteLength) {
	        if("L".equals(strLeftRightFlag)) {
	            stb.insert(0, strPutData);
	        }else if("R".equals(strLeftRightFlag)) {
	            stb.append(strPutData);
	        }
	    }
	    
	    strData = stb.toString();
	    
	    return strData;
	}
	
    public static void main(String args[]) {
        //System.out.println("getCurrentDate.getLastDate::>" + getLastDate(getCurrentDate()));
//        System.out.println("getPadData(1,L,3,0)::>" + getPadData("1","L",3,"0"));
//        System.out.println("getPadData(1,R,3,0)::>" + getPadData("1","R",3,"0"));
//        System.out.println("getPadData(1,L,3,00)::>" + getPadData("1","L",3,"00"));
//        System.out.println("getPadData(1,R,3,00)::>" + getPadData("1","R",3,"00"));
        
        System.out.println("getMonthDayTerm(20070801,20071025)::>" + getMonthDayTerm("20070801","20071025"));
        System.out.println("getMonthDayTerm(20070801,20070825)::>" + getMonthDayTerm("20070801","20070825"));
        System.out.println("getMonthDayTerm(20070801,20061025)::>" + getMonthDayTerm("20070801","20061025"));
    }
}
