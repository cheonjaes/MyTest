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
 * <p>�������� ����ϴ� ��¥ ���� ��ƿ
 * @author ���ȣ
 */
public class DateUtil {
	public final static String YYYY_MM_DD = "yyyy-MM-dd";
	public final static String YYYYMMDD = "yyyyMMdd";
	public final static String MMDD = "MMdd";

	private static Logger log = Logger.getLogger(DateUtil.class.getName());
	private static String _dateFormat = "yyyy-MM-dd";

	/**
	 * <p>�־��� ��¥ Date�� ����� �Է³����ŭ ������Ų��.
	 * @param dateString yyyyMMdd, yyyyMM, yyyy !!���˾���
	 * @param addYear 1 1����, -2 2����
	 * @return 3���� �Է�Date�������� ����, !!���˿���, nuu�Է½� null����
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

			//MONTH �� 0 ~ 11 �̹Ƿ� ���� �������� ���̳ʽ� 1
			cal.set(year, month - 1, day);
			cal.add(GregorianCalendar.YEAR, addYear);

			SimpleDateFormat formatter = new SimpleDateFormat(format);

			return formatter.format(cal.getTime());

		}catch (Exception e) {
			return null;
		}
	}

	/**
	 * <p>�־��� ��¥ Date�� ������ �Է¿�����ŭ ������Ų��.
	 * @param dateString yyyyMMdd, yyyyMM !!���˾���
	 * @param addMonth 1 �Ѵ���, -2 �δ���
	 * @return 2���� �Է�Date�������� ����, !!���˿���, nuu�Է½� null����
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

			//MONTH �� 0 ~ 11 �̹Ƿ� ���� �������� ���̳ʽ� 1
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
	 * �־��� ��¥ DATE �� �ϼ��� �Է��ϼ� ��ŭ ����.
	 * @param dateString yyyyMMdd������ ��¥
	 * @param addDay ������ų �ϼ�
	 * @return ���˾���, ���˿���,null�Է½� null����
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

			//MONTH �� 0 ~ 11 �̹Ƿ� ���� �������� ���̳ʽ� 1
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
	 * �־��� ��¥ DATE(from, to) �� �Ⱓ ���ϱ�
	 * @param from yyyyMMdd ����
	 * @param to yyyyMMdd ����
	 * @return �Է������� �Ⱓ�ϼ�
	 */
	public static int daysBetween(String from, String to) {
		return daysBetween(from, to, "yyyyMMdd");
	}

	/**
	 * <p>�־��� ��¥ DATE(from, to) �� �Ⱓ(��) ���ϱ� to - from
	 * <p>from, to ������ ������ ��ġ�ؾ��Ѵ�.
	 * @param from ��������
	 * @param to ��������
	 * @param format ��¥���� ( yyyy-MM-dd, yyyyMMdd)
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
	 * <p>�־��� ��¥ DATE(from, to) �� �Ⱓ(��) ���ϱ� to - from
	 * <p>from,to������ ���� ���ϱ�
	 * @param from yyyyMMdd������ date
	 * @param to yyyyMMdd������ date
	 * @return ����
	 */
	public static int monthsBetween(String from, String to) {
		return monthsBetween(from, to, "yyyyMMdd");
	}

	/**
	 * <p>�־��� ��¥ DATE(from, to) �� �Ⱓ(��) ���ϱ� to - from
	 * <p>from,to������ ���� ���ϱ�
	 * @param from ��������
	 * @param to ��������
	 * @param format ��¥���� ( yyyy-MM-dd, yyyyMMdd ��)
	 * @return ����
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
		// ceil�� floor�� ȿ��
		if ( ( (toDay - fromDay) > 0)) {
			result += toDate.compareTo(fromDate);
		}

		return result;
	}

	/**
	 * �־��� ��¥ ���� ������ ���� ���ϱ�
	 * @param dateString yyyyMMdd������ ��¥
	 * @return ������
	 */
	public static String lastDayOfMonth(String dateString) {
		return lastDayOfMonth(dateString, "yyyyMMdd");
	}

	/**
	 * �־��� ��¥ ���� ������ ���� ���ϱ�
	 * @param dateString ����
	 * @param format yyyyMMdd, yyyy-MM-dd���� ��¥����
	 * @return format���Ŀ� �´� ���ڵ���Ʈ
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
	 * �־��� �� ���� ������ ����(����) ���ϱ�
	 * @param year
	 * @param month
	 * @return ������
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
	 * <p>��¥���������� yyyyMMdd�������� ���θ���
	 * @param dateString ���˰����� date
	 * @return true - ��¥ ������ �°�, �����ϴ� ��¥�� ��. false - ��¥ ������ ���� �ʰų�, �������� �ʴ� ��¥�� ��.
	 * @throws Exception
	 */
	public static boolean isValid(String dateString) throws Exception {
		return isValid(dateString, "yyyyMMdd");
	}

	/**
	 * <p>��¥���������� ��ȿ�� ���θ���
	 * @param dateString ���˰����� date
	 * @param format yyyyMMdd, yyyy-MM-dd ���� ��¥����
	 * @return - ��¥ ������ �°�, �����ϴ� ��¥�� ��. false - ��¥ ������ ���� �ʰų�, �������� �ʴ� ��¥�� ��.
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
	 * �����Ϻ��� �����ϱ����� ��¥�� ���� String �迭 ����
	 * @param startDay ������
	 * @param endDay ������
	 * @param formatStr ��¥����
	 * @return ó���� String �迭
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
			//�����ϰ� �������� ������ ����
			diff = (endDate.getTime() - startDate.getTime()) /
				(1000 * 60 * 60 * 24);

			//�������� �����Ϻ��� ������ ���
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
	 * <p>�޼ҵ��	: getWeeks</p>
	 * <p>����		: ������ ���ϴ� �Լ�</p>
	 * <p>�޼ҵ��μ�1	: String setDate - ��¥</p>
	 * <p>�޼ҵ��μ�2	: String format - ��¥���� (yyyyMMddHHmm or yyyy-MM-dd HH:mm) </p>
	 * <p>�޼ҵ��μ�3	: String dateGuboon - ���� ���� (M:������, Y:������)</p>
	 * <p>�޼ҵ帮�ϰ�	: int rDateInfo - �ش�����</p>
	 */
	/**
	 * <p>������ ���ϴ� �Լ�
	 * @param setDate ��¥
	 * @param format ��¥���� ( yyyyMMdd, yyyy-MM-dd ��)
	 * @param dateGuboon ���� ���� ( <b>M</b>:������, <b>Y</b>:������)
	 * @return
	 */
	public static int getWeeks(String setDate, String format, String dateGuboon) {
		int rDateInfo = 0;
		try {

			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			Date d = formatter.parse(setDate);
			calendar.setTime(d);

			//Calendar Class�� ��¥�� 0�޺��� �ν��ϹǷ� �ݵ�� +1�� ���־����
			int monadd = calendar.get(Calendar.MONTH) + 1;

			//�ش�⵵�� �޿� ���ϴ� ������ �˰� �������
			if (dateGuboon.toUpperCase().compareTo("M") == 0) {
				int rWeekInMonth = calendar.get(Calendar.WEEK_OF_MONTH);
				rDateInfo = rWeekInMonth;
			}
			//�ش�⵵�� ���ϴ� ������ �˰� ���� ���
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
	 * <p>�Էµ� ��¥�� ������ ����� �ִ� �Լ�
	 * @param dayStr ��¥
	 * @param format yyyyMMdd���� ���˽�Ʈ��
	 * @return ���� ��Ʈ��
	 */
	public static String getDayOfTheWeek(String dayStr, String format) {
		DateFormat df1 = new SimpleDateFormat(format);
		DateFormat df2 = new SimpleDateFormat("EEE");
		String yoil = df2.format(df1.parse(dayStr, new ParsePosition(0)));

		return yoil;
	}

	/**
	 * �������� - �������� �� ������ ����
	 * @param from ��������
	 * @param to ��������
	 * @param format ��¥���� String
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
	 * ���缭���ð��� yyyyMMdd�������� ����.
	 * @param con
	 * @return yyyyMMdd���� ���缭������
	 */
	public static String[] getSysDate(Connection con) {
		return getSysDate(con, YYYYMMDD);
	}

    /**
     * ���缭��ð��� �Էµ� �������� ����
     * @param con
     * @param dateFormat yyyyMMdd, yyyy-MM-dd������ ��¥����
     * @return dateFormat�� ���缭����
     */
    public static String[] getSysDate(Connection con, String dateFormat) {
		StringBuffer rSql = new StringBuffer();
		
		if (dateFormat == null || dateFormat.length() < 1) {
			dateFormat = YYYYMMDD; //default format
		}
		
		if (YYYY_MM_DD.equals(dateFormat)) { //����ð��� YYYY-MM-DD, hhmmss �������� ����
			rSql.append("SELECT TO_CHAR(SYSDATE, 'YYYY-MM-DD'), TO_CHAR(SYSDATE, 'HH24MISS') FROM DUAL");
		}
		else if (YYYYMMDD.equals(dateFormat)) { //����ð��� YYYYMMDD, hhmmss �������� ����
			rSql.append("SELECT TO_CHAR(SYSDATE, 'YYYYMMDD'), TO_CHAR(SYSDATE, 'HH24MISS') FROM DUAL");
		}
		else { //�߸��� ����
			log.info("�� DateUtil.getSysDate => �߸��� ����");
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
	 * <p>statement����. Util���ο��� �����.
	 * @fixme Util����ڴ� ������. ������� Ȯ���� private�� ������!!
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
	 * <p>RecordSet����. Util���ο��� �����.
	 * @fixme Util����ڴ� ������. ������� Ȯ���� private�� ������!!
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
	 * �ӽ��׽�Ʈ
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
