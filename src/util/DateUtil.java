package com.cbscap.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.log4j.Logger;

/**
 * <p>공통으로 사용하는 날짜 관련 유틸
 * @author 김대호
 */
public class DateUtil {
	public final static String YYYY_MM_DD = "yyyy-MM-dd";
	public final static String YYYYMMDD = "yyyyMMdd";
	public final static String MMDD = "MMdd";

	private static Logger log = Logger.getLogger(DateUtil.class.getName());
	private static String _dateFormat = "yyyy-MM-dd";

	/**
	 * <p>주어진 날짜 Date의 년수를 입력년수만큼 증감시킨다.
	 * @param dateString yyyyMMdd, yyyyMM, yyyy !!포맷엄수
	 * @param addYear 1 1년후, -2 2년전
	 * @return 3가지 입력Date포맷으로 리턴, !!포맷에러, nuu입력시 null리턴
	 */
	public static String addYear(String dateString, int addYear) {
		try {
			if (dateString == null) {
				return null;
			}

			int year = 0;
			int month = 2;
			int day = 1;
			String format = "yyyyMMdd";

			GregorianCalendar cal = new GregorianCalendar();

			if (dateString.length() == 8) {
				year = Integer.parseInt(dateString.substring(0, 4));
				month = Integer.parseInt(dateString.substring(4, 6));
				day = Integer.parseInt(dateString.substring(6));
			}
			else if (dateString.length() == 6) {
				year = Integer.parseInt(dateString.substring(0, 4));
				month = Integer.parseInt(dateString.substring(4));

				format = "yyyyMM";
			}
			else if (dateString.length() == 4) {
				year = Integer.parseInt(dateString.substring(0, 4));

				format = "yyyy";
			}
			else {
				return null;
			}

			//MONTH 는 0 ~ 11 이므로 실제 데이터의 마이너스 1
			cal.set(year, month - 1, day);
			cal.add(GregorianCalendar.YEAR, addYear);

			SimpleDateFormat formatter = new SimpleDateFormat(format);

			return formatter.format(cal.getTime());

		}catch (Exception e) {
			return null;
		}
	}

	/**
	 * <p>주어진 날짜 Date의 월수를 입력월수만큼 증감시킨다.
	 * @param dateString yyyyMMdd, yyyyMM !!포맷엄수
	 * @param addMonth 1 한달후, -2 두달전
	 * @return 2가지 입력Date포맷으로 리턴, !!포맷에러, nuu입력시 null리턴
	 */
	public static String addMonth(String dateString, int addMonth) {
		try {
			if (dateString == null) {
				return null;
			}

			int year = 0;
			int month = 2;
			int day = 1;
			String format = "yyyyMMdd";

			GregorianCalendar cal = new GregorianCalendar();

			if (dateString.length() == 8) {
				year = Integer.parseInt(dateString.substring(0, 4));
				month = Integer.parseInt(dateString.substring(4, 6));
				day = Integer.parseInt(dateString.substring(6));
			}
			else if (dateString.length() == 6) {
				year = Integer.parseInt(dateString.substring(0, 4));
				month = Integer.parseInt(dateString.substring(4));

				format = "yyyyMM";
			}
			else {
				return null;
			}

			//MONTH 는 0 ~ 11 이므로 실제 데이터의 마이너스 1
			cal.set(year, month - 1, day);
			cal.add(GregorianCalendar.MONTH, addMonth);

			SimpleDateFormat formatter = new SimpleDateFormat(format);

			return formatter.format(cal.getTime());
		}
		catch (Exception e) {
			return null;
		}
	}

	/**
	 * 주어진 날짜 DATE 의 일수를 입력일수 만큼 증감.
	 * @param dateString yyyyMMdd형식의 날짜
	 * @param addDay 증감시킬 일수
	 * @return 포맷엄수, 포맷에러,null입력시 null리턴
	 */
	public static String addDay(String dateString, int addDay) {
		try {
			
			log.info("11 : " + dateString);
			log.info("11 : " + addDay);
			log.error("12 : " + addDay);
			log.debug("13 : " + addDay);
			System.out.println("11 : " + dateString);
			System.out.println("11 : " + addDay);
			
			if ( (dateString == null) || (dateString.length() != 8)) {
				return null;
			}

			
			
			int year = Integer.parseInt(dateString.substring(0, 4));
			int month = Integer.parseInt(dateString.substring(4, 6));
			int day = Integer.parseInt(dateString.substring(6, 8));

			log.info("2year : " +year);
			log.info("2month : " +month);
			log.info("2day : " +day);
			System.out.println("2year : " +year);
			System.out.println("2month : " +month);
			System.out.println("2day : " +day);
			
			GregorianCalendar cal = new GregorianCalendar();

			//MONTH 는 0 ~ 11 이므로 실제 데이터의 마이너스 1
			cal.set(year, month - 1, day);
			cal.add(GregorianCalendar.DAY_OF_YEAR, addDay);

			log.info("33 : " + cal.getTime());
			System.out.println("33 : " + cal.getTime());
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

			log.info("44 : " );
			log.info("44 : "  + formatter.format(cal.getTime()));
			System.out.println("44 : " );
			System.out.println("44 : "  + formatter.format(cal.getTime()));
			
			return formatter.format(cal.getTime());
		}
		catch (Exception e) {
			return null;
		}
	}

	/**
	 * 주어진 날짜 DATE(from, to) 의 기간 구하기
	 * @param from yyyyMMdd 형식
	 * @param to yyyyMMdd 형식
	 * @return 입력일자의 기간일수
	 */
	public static int daysBetween(String from, String to) {
		return daysBetween(from, to, "yyyyMMdd");
	}

	/**
	 * <p>주어진 날짜 DATE(from, to) 의 기간(일) 구하기 to - from
	 * <p>from, to 일자의 포맷은 일치해야한다.
	 * @param from 시작일자
	 * @param to 종료일자
	 * @param format 날짜포맷 ( yyyy-MM-dd, yyyyMMdd)
	 * @return
	 */
	public static int daysBetween(String from, String to, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format,
			java.util.Locale.KOREA);
		Date d1 = null;
		Date d2 = null;

		try {
			d1 = formatter.parse(from);
			d2 = formatter.parse(to);
		}
		catch (ParseException e) {
			return -1;
		}

		if (!formatter.format(d1).equals(from)) {
			return -1;
		}

		if (!formatter.format(d2).equals(to)) {
			return -1;
		}

		long duration = d2.getTime() - d1.getTime();

		if (duration < 0) {
			return -1;
		}

		return (int) (duration / (1000 * 60 * 60 * 24));
	}

	/**
	 * <p>주어진 날짜 DATE(from, to) 의 기간(달) 구하기 to - from
	 * <p>from,to사이의 월수 구하기
	 * @param from yyyyMMdd포맷의 date
	 * @param to yyyyMMdd포맷의 date
	 * @return 월수
	 */
	public static int monthsBetween(String from, String to) {
		return monthsBetween(from, to, "yyyyMMdd");
	}

	/**
	 * <p>주어진 날짜 DATE(from, to) 의 기간(달) 구하기 to - from
	 * <p>from,to사이의 월수 구하기
	 * @param from 시작일자
	 * @param to 종료일자
	 * @param format 날짜포맷 ( yyyy-MM-dd, yyyyMMdd 등)
	 * @return 월수
	 */
	public static int monthsBetween(String from, String to, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
		Date fromDate = null;
		Date toDate = null;

		try {
			fromDate = formatter.parse(from);
			toDate = formatter.parse(to);
		}
		catch (ParseException e) {
			return -1;
		}

		// if two date are same, return 0.
		if (fromDate.compareTo(toDate) == 0) {
			return 0;
		}

		SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
		SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.KOREA);
		SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.KOREA);

		int fromYear = Integer.parseInt(yearFormat.format(fromDate));
		int toYear = Integer.parseInt(yearFormat.format(toDate));
		int fromMonth = Integer.parseInt(monthFormat.format(fromDate));
		int toMonth = Integer.parseInt(monthFormat.format(toDate));
		int fromDay = Integer.parseInt(dayFormat.format(fromDate));
		int toDay = Integer.parseInt(dayFormat.format(toDate));

		int result = 0;
		result += ( (toYear - fromYear) * 12);
		result += (toMonth - fromMonth);

		//        if (((toDay - fromDay) < 0) ) result += fromDate.compareTo(toDate);
		// ceil과 floor의 효과
		if ( ( (toDay - fromDay) > 0)) {
			result += toDate.compareTo(fromDate);
		}

		return result;
	}

	/**
	 * 주어진 날짜 달의 마지막 일자 구하기
	 * @param dateString yyyyMMdd포맷의 날짜
	 * @return 월말일
	 */
	public static String lastDayOfMonth(String dateString) {
		return lastDayOfMonth(dateString, "yyyyMMdd");
	}

	/**
	 * 주어진 날짜 달의 마지막 일자 구하기
	 * @param dateString 일자
	 * @param format yyyyMMdd, yyyy-MM-dd등의 날짜포맷
	 * @return format형식에 맞는 일자데이트
	 */
	public static String lastDayOfMonth(String dateString, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);

		Date date = null;

		try {
			date = formatter.parse(dateString);

			SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy",
				Locale.KOREA);
			SimpleDateFormat monthFormat = new SimpleDateFormat("MM",
				Locale.KOREA);

			int year = Integer.parseInt(yearFormat.format(date));
			int month = Integer.parseInt(monthFormat.format(date));
			int day = lastDay(year, month);

			DecimalFormat fourDf = new DecimalFormat("0000");
			DecimalFormat twoDf = new DecimalFormat("00");
			String tempDate = String.valueOf(fourDf.format(year)) +
				String.valueOf(twoDf.format(month)) +
				String.valueOf(twoDf.format(day));
			date = formatter.parse(tempDate);
		}
		catch (ParseException e) {
			return null;
		}

		return formatter.format(date);
	}

	/**
	 * 주어진 년 월의 마지막 일자(월말) 구하기
	 * @param year
	 * @param month
	 * @return 월말일
	 * @throws ParseException
	 */
	public static int lastDay(int year, int month) throws ParseException {
		int day = 0;

		switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				day = 31;

				break;

			case 2:

				if ( (year % 4) == 0) {
					if ( ( (year % 100) == 0) && ( (year % 400) != 0)) {
						day = 28;
					}
					else {
						day = 29;
					}
				}
				else {
					day = 28;
				}

				break;

			default:
				day = 30;
		}

		return day;
	}

	/**
	 * <p>날짜포맷형식이 yyyyMMdd형식인지 여부리턴
	 * @param dateString 포맷검증할 date
	 * @return true - 날짜 형식이 맞고, 존재하는 날짜일 때. false - 날짜 형식이 맞지 않거나, 존재하지 않는 날짜일 때.
	 * @throws Exception
	 */
	public static boolean isValid(String dateString) throws Exception {
		return isValid(dateString, "yyyyMMdd");
	}

	/**
	 * <p>날짜포맷형식의 유효성 여부리턴
	 * @param dateString 포맷검증할 date
	 * @param format yyyyMMdd, yyyy-MM-dd 등의 날짜포맷
	 * @return - 날짜 형식이 맞고, 존재하는 날짜일 때. false - 날짜 형식이 맞지 않거나, 존재하지 않는 날짜일 때.
	 */
	public static boolean isValid(String dateString, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
		Date date = null;

		try {
			date = formatter.parse(dateString);
		}
		catch (ParseException e) {
			return false;
		}

		if (!formatter.format(date).equals(dateString)) {
			return false;
		}

		return true;
	}

	/**
	 * Gets a String version of the passed date in the format specified by dateFormat.
	 *
	 * @param date       the date to be converted to a string
	 * @return           a string version of the date passed in
	 */
	public static String getDateAsString(Date date) {
		SimpleDateFormat dateform = new SimpleDateFormat(_dateFormat);

		return dateform.format(date);
	}

	/**
	 * Gets a String version of the passed date in the format specified by dateFormat.
	 *
	 * @param date       the date to be converted to a string
	 * @param dateFormat date format
	 * @return           a string version of the date passed in
	 */
	public static String getDateAsString(Date date, String dateFormat) {
		SimpleDateFormat dateform = new SimpleDateFormat(dateFormat);

		return dateform.format(date);
	}

	/**
	 * 시작일부터 종료일까지의 날짜가 남긴 String 배열 리턴
	 * @param startDay 시작일
	 * @param endDay 종료일
	 * @param formatStr 날짜형식
	 * @return 처리된 String 배열
	 */
	public static String[] increaseDay(String startDay, String endDay,
									   String formatStr) {

		Calendar now = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		Date startDate;
		Date endDate;
		long diff = 0;

		try {
			startDate = formatter.parse(startDay);
			endDate = formatter.parse(endDay);
			//시작일과 종료일의 날수를 구함
			diff = (endDate.getTime() - startDate.getTime()) /
				(1000 * 60 * 60 * 24);

			//시작일이 종료일보다 이후인 경우
			if (diff < 0) {
				return null;
			}

			now.setTime(startDate);
		}
		catch (ParseException ex) {
			ex.printStackTrace();
		}

		String[] days = new String[ (int) diff + 1];
		int count = (int) diff;
		days[0] = formatter.format(now.getTime());

		for (int k = 0; k < count; k++) {
			now.add(Calendar.DAY_OF_YEAR, +1);
			days[k + 1] = formatter.format(now.getTime());
		}
		return days;
	}

	/**
	 * <p>메소드명	: getWeeks</p>
	 * <p>설명		: 주차를 구하는 함수</p>
	 * <p>메소드인수1	: String setDate - 날짜</p>
	 * <p>메소드인수2	: String format - 날짜형식 (yyyyMMddHHmm or yyyy-MM-dd HH:mm) </p>
	 * <p>메소드인수3	: String dateGuboon - 주차 구분 (M:월주차, Y:연주차)</p>
	 * <p>메소드리턴값	: int rDateInfo - 해당주차</p>
	 */
	/**
	 * <p>주차를 구하는 함수
	 * @param setDate 날짜
	 * @param format 날짜포맷 ( yyyyMMdd, yyyy-MM-dd 등)
	 * @param dateGuboon 주차 구분 ( <b>M</b>:월주차, <b>Y</b>:연주차)
	 * @return
	 */
	public static int getWeeks(String setDate, String format, String dateGuboon) {
		int rDateInfo = 0;
		try {

			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			Date d = formatter.parse(setDate);
			calendar.setTime(d);

			//Calendar Class는 날짜를 0달부터 인식하므로 반드시 +1을 해주어야함
			int monadd = calendar.get(Calendar.MONTH) + 1;

			//해당년도의 달에 속하는 주차를 알고 싶은경우
			if (dateGuboon.toUpperCase().compareTo("M") == 0) {
				int rWeekInMonth = calendar.get(Calendar.WEEK_OF_MONTH);
				rDateInfo = rWeekInMonth;
			}
			//해당년도에 속하는 주차를 알고 싶을 경우
			else if (dateGuboon.toUpperCase().compareTo("Y") == 0) {
				int rWeekInYear = calendar.get(Calendar.WEEK_OF_YEAR);
				rDateInfo = rWeekInYear;
				if (monadd == 12 && rWeekInYear == 1) {
					rDateInfo = 53;
				}
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return rDateInfo;

	}

	/**
	 * <p>입력된 날짜의 요일을 출력해 주는 함수
	 * @param dayStr 날짜
	 * @param format yyyyMMdd등의 포맷스트링
	 * @return 요일 스트링
	 */
	public static String getDayOfTheWeek(String dayStr, String format) {
		DateFormat df1 = new SimpleDateFormat(format);
		DateFormat df2 = new SimpleDateFormat("EEE");
		String yoil = df2.format(df1.parse(dayStr, new ParsePosition(0)));

		return yoil;
	}

	/**
	 * 시작일자 - 종료일자 의 차이인 리턴
	 * @param from 시작일자
	 * @param to 종료일자
	 * @param format 날짜포맷 String
	 * @return 0, -1, 1
	 */
	public static int compareDate(String from, String to, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format,
			java.util.Locale.KOREA);
		Date d1 = null;
		Date d2 = null;
		int rtVal = 0;
		try {
			d1 = formatter.parse(from);
			d2 = formatter.parse(to);
		}
		catch (ParseException e) {
			return -1;
		}

		long duration = d2.getTime() - d1.getTime();

		if (duration < 0) {
			rtVal = -1;
		}
		if (duration == 0) {
			rtVal = 0;
		}
		if (duration > 0) {
			rtVal = 1;
		}

		return rtVal;
	}

    /**
	 * 현재서버시간을 yyyyMMdd포맷으로 리턴.
	 * @param con
	 * @return yyyyMMdd형의 현재서버일자
	 */
	public static String[] getSysDate(Connection con) {
		return getSysDate(con, YYYYMMDD);
	}

    /**
     * 현재서비시간을 입력된 포맷으로 리턴
     * @param con
     * @param dateFormat yyyyMMdd, yyyy-MM-dd형식의 날짜포맷
     * @return dateFormat의 현재서버시
     */
    public static String[] getSysDate(Connection con, String dateFormat) {
		StringBuffer rSql = new StringBuffer();
		
		if (dateFormat == null || dateFormat.length() < 1) {
			dateFormat = YYYYMMDD; //default format
		}
		
		if (YYYY_MM_DD.equals(dateFormat)) { //현재시간을 YYYY-MM-DD, hhmmss 포맷으로 리턴
			rSql.append("SELECT TO_CHAR(SYSDATE, 'YYYY-MM-DD'), TO_CHAR(SYSDATE, 'HH24MISS') FROM DUAL");
		}
		else if (YYYYMMDD.equals(dateFormat)) { //현재시간을 YYYYMMDD, hhmmss 포맷으로 리턴
			rSql.append("SELECT TO_CHAR(SYSDATE, 'YYYYMMDD'), TO_CHAR(SYSDATE, 'HH24MISS') FROM DUAL");
		}
		else { //잘못된 포맷
			log.info("■ DateUtil.getSysDate => 잘못된 포맷");
			return null;
		}
		
		Statement stmt = null;
		ResultSet rs = null;
		String[] sysDate = new String[2];
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(rSql.toString());
			if (rs.next()) {
				sysDate[0] = rs.getString(1);
				sysDate[1] = rs.getString(2);
			}
		}
		catch (Exception se) {
			log.info(rSql.toString());
			se.printStackTrace();
		}
		finally {
			closeResultSet(rs);
			closeStatement(stmt);
		}
		return sysDate;
	}

    /**
	 * <p>statement종료. Util내부에서 사용함.
	 * @fixme Util사용자는 사용금지. 상관관계 확인후 private로 수정요!!
	 * @param stmt
	 */
	public static void closeStatement(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
	 * <p>RecordSet종료. Util내부에서 사용함.
	 * @fixme Util사용자는 사용금지. 상관관계 확인후 private로 수정요!!
	 * @param rs
	 */
	public static void closeResultSet(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 임시테스트
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("addDay : " + DateUtil.addDay("20030602", 299));
		System.out.println("addMonth : " + DateUtil.addMonth("20030228", 3));
		System.out.println("addYear : " + DateUtil.addYear("20030228", 3));
		System.out.println("daysBetween : " + DateUtil.daysBetween("20030602", "20030909"));
		System.out.println("monthsBetween : " + DateUtil.monthsBetween("20030602", "20030901"));
		System.out.println("lastDayOfMonth : " + DateUtil.lastDayOfMonth("20030602"));

	}
}
