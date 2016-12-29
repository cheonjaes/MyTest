package com.cbscap.sc.r.util;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.cbscap.util.Util;

/**
 * <p>���ϸ�		: RUtil.java</p>
 * <p>������		: 1.0</p>
 * <p>�ۼ���		: 2012-10-17</p>
 * <p>�ۼ���		: ������</p>
 * <p>UseCase��	: û���Ա� Util</p>
 * <p>���α׷���	: û���Ա� Util</p>
 * <p>����		: û���Ա� Util</p>
 * <pre>
 * -----------------------------------------------------------
 * 2012.10.17 ������ - �ҽ�ǥ��ȭ, �����ۼ�
 * -----------------------------------------------------------
 * </pre>
 */
@SuppressWarnings({"rawtypes"})
public class RUtil {
	
	private static Logger log = Logger.getLogger(RUtil.class.getName());

	public final static String INSERT = "Insert";
	public final static String UPDATE = "RowUpdate";
	public final static String DELETE = "Delete";

	/**
	 * <p>�޼ҵ��		: getKSTDate</p>
	 * <p>��������		: ���� ���ڰ��� �����´�.</p>
	 * <p>�޼ҵ� �μ�1	: N/A</p>
	 * <p>�޼ҵ� ���ϰ�	: YYYY-MM-DD</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String getKSTDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date();
		return formatter.format(curDate);
	}
	/**
	 * <p>�޼ҵ��		: getKSTDate</p>
	 * <p>��������		: ���� ���ڰ��� �ش��������� �����´�.</p>
	 * <p>�޼ҵ� �μ�1	: DateFormat (yyyyMMddhhmmss)</p>
	 * <p>�޼ҵ� ���ϰ�	: String(Formatted Date)</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String getKSTDate(String format) {
		int millisPerHour = 60 * 60 * 1000; // 1hour(ms) = 60s * 60m * 1000ms
		SimpleDateFormat fmt = new SimpleDateFormat(format);
		SimpleTimeZone timeZone = new SimpleTimeZone(9 * millisPerHour, "KST");
		fmt.setTimeZone(timeZone);

		long time = System.currentTimeMillis();
		String str = fmt.format(new java.util.Date(time));
		return str;
	}
	/**
	 * <p>�޼ҵ��		: isValidDt</p>
	 * <p>��������		: ��¥���Ŀ� �´��� ����</p>
	 * <p>�޼ҵ� �μ�1	: String date</p>
	 * <p>�޼ҵ� ���ϰ�	: boolean</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static boolean isValidDt(String strDt) {
		String format = "yyyyMMdd";
		if (strDt.length()==10) format = "yyyy-MM-dd";
		return isValidDt(strDt, format);
	}
	public static boolean isValidDt(String strDt, String format) {
		
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.KOREA);
		Date date = null;

		try {
			date = formatter.parse(strDt);
		} catch (ParseException e) {
			return false;
		}

		if (!formatter.format(date).equals(strDt)) {
			return false;
		}
		return true;
	}
	/**
	 * <p>�޼ҵ��		: getDayWeek</p>
	 * <p>��������		: Ư����¥�� ������ ����</p>
	 * <p>�޼ҵ� �μ�1	: String date</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String getDayWeek(String date) {
		
		String weekDay = "";
    	date = RUtil.getFormatDt(date, "-");	// yyyy-MM-dd

    	try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dt = sdf.parse(date);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			
			String tmp = String.valueOf(cal.get(Calendar.DAY_OF_WEEK));
			weekDay = decode(tmp, new String[]{"1","��","2","��","3","ȭ","4","��","5","��","6","��","7","��",""});
//			log.info("�� ["+date+"] "+weekDay+"���� : "+tmp);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return weekDay;
	}

	/**
	 * <p>�޼ҵ��		: parseFdt</p>
	 * <p>��������		: DATE FORMAT</p>
	 * <p>�޼ҵ� �μ�1	: String date</p>
	 * <p>�޼ҵ� �μ�2	: String format</p>
	 * <p>�޼ҵ� ���ϰ�	: String(Formatted Date)</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String parseFdt(String date, String format) {
		date = date.replaceAll("[^0-9]", "");	// ���ڸ� ������ ������ ���� ����
		if (date.length()==6) date += "01";
		
		if (date.length()!=8) {
			return "";
		}
		//yyyyMMddHHmiss
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf2 = new SimpleDateFormat(format);
		Date tmpDate = null;
		
		try {
			tmpDate = sdf1.parse(date);
		} catch (ParseException e) {
			tmpDate = new Date();
		}
		return sdf2.format(tmpDate);
	}

	/**
	 * <p>�޼ҵ��		: getFormatDt</p>
	 * <p>��������		: dv�� ������ ���� DATE FORMAT�� ����</p>
	 * <p>�޼ҵ� �μ�1	: String date</p>
	 * <p>�޼ҵ� �μ�2	: String dv</p>
	 * <p>�޼ҵ� ���ϰ�	: String(Formatted Date)</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String getFormatDt(String date, String dv) {
		String formattedDate = "";
		if (isNull(date)) {
			formattedDate = "";
		} else {
			date = date.replaceAll("[^0-9]", "");	// ���ڸ� ������ ������ ���� ����
			formattedDate = date.substring(0, 4)+dv+date.substring(4, 6);
			if (date.length()==8) formattedDate += dv+date.substring(6);
		}
		return formattedDate;
	}
	/**
	 * <p>�޼ҵ��		: getFormatDt6</p>
	 * <p>��������		: DATE FORMAT�� ���� YYMMDD</p>
	 * <p>�޼ҵ� �μ�1	: String date</p>
	 * <p>�޼ҵ� ���ϰ�	: String(Formatted Date)</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String getFormatDt6(String date) {
		String formattedDate = "";
		if (isNull(date)) {
			formattedDate = "";
		} else {
			date = date.replaceAll("[^0-9]", "");	// ���ڸ� ������ ������ ���� ����
			formattedDate = date.substring(2, 6);   //YYMMDD			
		}
		return formattedDate;
	}
	
	/**
	 * <p>�޼ҵ��		: addDate</p>
	 * <p>��������		: ��¥��� ��ƿ</p>
	 * <p>�޼ҵ� �μ�1	: String date</p>
	 * <p>�޼ҵ� �μ�2	: int</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String addDate(String date, int cnt) {
    	String ymd = "";
    	date = RUtil.getFormatDt(date, "-");	// yyyy-MM-dd

    	try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dt = sdf.parse(date);

			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			cal.add(Calendar.DATE, cnt);
			ymd = sdf.format(cal.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ymd;
    }
	
	/**
	 * <p>�޼ҵ��		: addMonth</p>
	 * <p>��������		: �� ��� ��ƿ(����Ŭ��  addMonths)</p>
	 * <p>�޼ҵ� �μ�1	: String yyyyMM/yyyyMMdd</p>
	 * <p>�޼ҵ� �μ�2	: int</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String addMonth(String date, int add) {
		
		date = RUtil.nvl(date).replaceAll("[^0-9]", "");	// ���ڸ� ������ ������ ���� ����
		if (!RUtil.in(date.length(), new int[]{6, 8})) return "";
		
		String format = "yyyy-MM";
		
		try {
			int yy = parseInt(date.substring(0, 4));
			int mm = parseInt(date.substring(4, 6));
			int dd = 1;
			
			GregorianCalendar cal = new GregorianCalendar();

			if (date.length()==8) {
				dd = Integer.parseInt(date.substring(6));
				format = "yyyy-MM-dd";;
			}
			
			cal.set(yy, mm - 1, dd);	// �ڹ��� ���� 0~11�̹Ƿ� ���� ������ 1�� ���ش�
			cal.add(GregorianCalendar.MONTH, add);

			SimpleDateFormat formatter = new SimpleDateFormat(format);
			return formatter.format(cal.getTime());
		}
		catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * <p>�޼ҵ��		: dateDiff</p>
	 * <p>��������		: �� ��¥�� ���̸� ����</p>
	 * <p>�޼ҵ� �μ�1	: String sdt</p>
	 * <p>�޼ҵ� �μ�2	: String edt</p>
	 * <p>�޼ҵ� ���ϰ�	: day Count</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static long dateDiff(String sdt, String edt) {
		// ���ڸ� ������ ������ ���� ����
		sdt = RUtil.trim(sdt).replaceAll("[^0-9]", "");
		edt = RUtil.trim(edt).replaceAll("[^0-9]", "");
		if (RUtil.isNull(sdt) || RUtil.isNull(edt)) return 0;
		
		long diff = 0;
		
		try {
			SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
			Date dt1 = date.parse(sdt);
			Date dt2 = date.parse(edt);
			diff = (dt2.getTime()-dt1.getTime()) / (1000*60*60*24);
		} catch(ParseException e){
//			System.out.println("ParseIng_Error : " + e.getMessage());
		}
		return diff;
	}
	
	/**
	 * <p>�޼ҵ��		: monthDiff</p>
	 * <p>��������		: �� ���� ���̸� ����</p>
	 * <p>�޼ҵ� �μ�1	: String sdt</p>
	 * <p>�޼ҵ� �μ�2	: String edt</p>
	 * <p>�޼ҵ� ���ϰ�	: month Count</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static long monthDiff(String sdt, String edt) {
		// ���ڸ� ������ ������ ���� ����
		sdt = sdt.replaceAll("[^0-9]", "");
		edt = edt.replaceAll("[^0-9]", "");
		long diff = 0;

		int yy1 = parseInt(sdt.substring(0, 4));
		int yy2 = parseInt(edt.substring(0, 4));
		int mm1 = parseInt(sdt.substring(4, 6));
		int mm2 = parseInt(edt.substring(4, 6));
		int dd1 = parseInt(sdt.substring(6, 8));
		int dd2 = parseInt(edt.substring(6, 8));
		
		diff = (long) Math.floor((yy2-yy1)*12.0 + (mm2-mm1));
		if (dd1>dd2) {
			diff -= 1;
		}
		return diff;
    }
	
	/**
	 * <p>�޼ҵ��		: isLeapYr</p>
	 * <p>��������		: ���� ����</p>
	 * <p>�޼ҵ� �μ�1	: String year</p>
	 * <p>�޼ҵ� ���ϰ�	: boolean</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static boolean isLeapYr(String dt) {
		if (dt.length()<4) return false;
		
		// YYYY �̿� �������� �Է� ���� �� �� 4�ڸ� 'YYYY'�� ���
		dt = dt.substring(0, 4);
		int year = RUtil.parseInt(dt);
		
		return ((year%4==0) && (year%100!=0) || (year%400==0));
    }
	/**
	 * <p>�޼ҵ��		: lastMdt</p>
	 * <p>��������		: ������</p>
	 * <p>�޼ҵ� �μ�1	: String dt</p>
	 * <p>�޼ҵ� ���ϰ�	: String dt</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String lastMdt(String dt) {
		dt = dt.replaceAll("[^0-9]", "");
		dt = addMonth(dt, 1).substring(0,7)+"-01";	// ������ 1���� ���ؼ� �Ϸ� ����.
		return addDate(dt, -1);
    }

	/**
	 * <p>�޼ҵ��		: isNull</p>
	 * <p>��������		: null üũ</p>
	 * <p>�޼ҵ� �μ�1	: String str</p>
	 * <p>�޼ҵ� ���ϰ�	: boolean</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static boolean isNull(String str) {
		boolean bool = false;
		if(nvl(str).equals("")) {
			bool = true;
		}
		return bool;
	}
	/**
	 * <p>�޼ҵ��		: isNum</p>
	 * <p>��������		: ���� üũ</p>
	 * <p>�޼ҵ� �μ�1	: String str</p>
	 * <p>�޼ҵ� ���ϰ�	: boolean</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static boolean isNum(String str) {
		if (isNull(str)) {
			return false;
		}
		for (int i=0; i<str.length(); ++i) {
			if (!(Character.isDigit(str.charAt(i)))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>�޼ҵ��		: replace</p>
	 * <p>��������		: replace ��� (null �� ��� "" ó��)</p>
	 * <p>�޼ҵ� �μ�1	: String str</p>
	 * <p>�޼ҵ� �μ�2	: String old(��� ����)</p>
	 * <p>�޼ҵ� �μ�3	: String chg(��ü ����)</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String replaceAll(String str, String old, String chg) {
		str = nvl(str);
		old = nvl(old);
		chg = nvl(chg);

		str = str.replaceAll(old, chg);
		return str;
	}
	public static String replace(String str, String old, String chg) {
		str = nvl(str);
		old = nvl(old);
		chg = nvl(chg);
		
		int idx = str.indexOf(old);
		if (idx<0) return str;
		
		str = str.substring(0, idx) + chg + str.substring(idx+old.length());
		return str;
	}
	public static String replaceLast(String str, String old, String chg) {
		str = nvl(str);
		old = nvl(old);
		chg = nvl(chg);
		
		int idx = str.lastIndexOf(old);
		if (idx<0) return str;
		
		str = str.substring(0, idx) + chg + str.substring(idx+old.length());
		return str;
	}

	/**
	 * <p>�޼ҵ��		: nvl</p>
	 * <p>��������		: Oracle�� nvl ���</p>
	 * <p>�޼ҵ� �μ�1	: String str</p>
	 * <p>�޼ҵ� �μ�2	: String replace</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String nvl(String str, String replace) {
		if (str==null || str.trim().equals("") || str.toUpperCase().equals("NULL"))
			return replace;
		else
			return str;
	}
	public static String nvl(String str) {
		return nvl(str, "");
	}
	// Query NVL(����Ŭ ������Ʈ �� ���) : Not Null Column �� ���� �� Null���� " " ó��
	// DB�� ����Ŭ �ƴҰ�� " " => "" ���� �ٲ� �� 
	public static String qNvl(String str) {
		return nvl(str, " ");
	}

	/**
	 * <p>�޼ҵ��		: decode</p>
	 * <p>��������		: Oracle�� decode ���</p>
	 * <p>�޼ҵ� �μ�1	: String str</p>
	 * <p>�޼ҵ� �μ�2	: String[] arr</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String decode(String str, String[] arr) /*throws Exception*/ {
		if (arr.length<2) {
			return null;
			//throw new Exception ("[EUtil.decode] ERROR : not enough argument for Decode");
		}
		for (int i=0; i<arr.length; i+=2) {
			if (i==arr.length-1) {
				str = arr[i];
				break;
			}
			if (str.equals(arr[i])) {
				str = arr[i+1];
				break;
			}
		}
		return str;
	}

	/**
	 * <p>�޼ҵ��		: like</p>
	 * <p>��������		: Oracle�� like ���</p>
	 * <p>�޼ҵ� �μ�1	: String str</p>
	 * <p>�޼ҵ� �μ�2	: String cmpr</p>
	 * <p>�޼ҵ� ���ϰ�	: boolean</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static boolean like(String str, String cmpr) {
		boolean flag = false;

		if (isNull(str) || isNull(cmpr))
			flag = false;

		// case : like '%cmpr%'
		else if (cmpr.substring(0, 1).equals("%") && cmpr.substring(cmpr.length()-1).equals("%"))
			flag = (str.indexOf(cmpr.substring(1, cmpr.length()-1))==-1)?false:true;

		// case : like 'cmpr%'
		else if (cmpr.substring(cmpr.length()-1).equals("%"))
			flag = str.substring(0, cmpr.length()-1).equals(cmpr.substring(0, cmpr.length()-1));

		// case : like '%cmpr'
		else if (cmpr.substring(0, 1).equals("%"))
			flag = str.substring(str.length()- (cmpr.length()-1)).equals(cmpr.substring(1));

		// case : like 'cmpr'
		else
			flag = str.equals(cmpr);

		return flag;
	}

	/**
	 * <p>�޼ҵ��		: in</p>
	 * <p>��������		: Oracle�� in ���</p>
	 * <p>�޼ҵ� �μ�1	: String str</p>
	 * <p>�޼ҵ� �μ�2	: String[] cmpr</p>
	 * <p>�޼ҵ� ���ϰ�	: boolean</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static boolean in(String str, String[] cmpr) {
		boolean flag = false;
		for (int i=0; i<cmpr.length; i++) {
			if (RUtil.nvl(str, "").equals(cmpr[i])) {
				return true;
			}
		}
		return flag;
	}
	/**
	 * <p>�޼ҵ��		: in</p>
	 * <p>��������		: Oracle�� in ���</p>
	 * <p>�޼ҵ� �μ�1	: int str</p>
	 * <p>�޼ҵ� �μ�2	: int[] cmpr</p>
	 * <p>�޼ҵ� ���ϰ�	: boolean</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static boolean in(int num, int[] cmpr) {
		boolean flag = false;
		for (int i=0; i<cmpr.length; i++) {
			if (num==cmpr[i]) {
				return true;
			}
		}
		return flag;
	}

	/**
	 * <p>�޼ҵ��		: trim</p>
	 * <p>��������		: Oracle�� trim ���</p>
	 * <p>�޼ҵ� �μ�1	: String str</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String trim(String str) {
		//str.replaceAll("/(^\s*)|(\s*$)/", "0");
		return nvl(str).trim();
	}
	/**
	 * <p>�޼ҵ��		: ltrim</p>
	 * <p>��������		: Oracle�� ltrim ���</p>
	 * <p>�޼ҵ� �μ�1	: String str</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String ltrim(String str) {
		int i,j = 0;
		for (i=0; i<str.length(); i++) {
			if(str.charAt(i) == ' ') j = j+1;
			else break;
		}
		return str.substring(j, str.length());
	}
	/**
	 * <p>�޼ҵ��		: rtrim</p>
	 * <p>��������		: Oracle�� rtrim ���</p>
	 * <p>�޼ҵ� �μ�1	: String str</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String rtrim(String str) {
		int i,j = 0;
		for (i=str.length()-1; i>=0; i--) {
			if(str.charAt(i) == ' ') j = j+1;
			else break;
		}
		return str.substring(0, str.length()-j);
	}
	/**
	 * <p>�޼ҵ��		: trimToNull</p>
	 * <p>��������		: trim�� �����ϰ�, ������ ���� Null�� ��ȯ</p>
	 * <p>�޼ҵ� �μ�1	: String str</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String trimToNull(String str) {
		str = nvl(str).trim();
		return (str.equals(""))?null:str;
	}

	/**
	 * <p>�޼ҵ��		: lpad</p>
	 * <p>��������		: Oracle�� lpad ���</p>
	 * <p>�޼ҵ� �μ�1	: String str</p>
	 * <p>�޼ҵ� �μ�2	: int len</p>
	 * <p>�޼ҵ� �μ�3	: String p</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String lpad(String str, int len, String p) {
		str = nvl(str);
		for (int i=str.length(); i<len; i++) {
			str = p+str;
		}
		return str;
	}
	public static String lpadByte(String str, int len, String p) {
		str = nvl(str);
		str = RUtil.lpad("", len-str.getBytes().length, p) + str;
		return str;
	}
	/**
	 * <p>�޼ҵ��		: rpad</p>
	 * <p>��������		: Oracle�� rpad ���</p>
	 * <p>�޼ҵ� �μ�1	: String str</p>
	 * <p>�޼ҵ� �μ�2	: int len</p>
	 * <p>�޼ҵ� �μ�3	: String p</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String rpad(String str, int len, String p) {
		str = nvl(str);
		for (int i=str.length(); i<len; i++) {
			str = str+p;
		}
		return str;
	}
	public static String rpadByte(String str, int len, String p) {
		str = nvl(str);
		str = str + RUtil.rpad("", len-str.getBytes().length, p);
		return str;
	}
	/**
	 * <p>�޼ҵ��		: lpad</p>
	 * <p>��������		: Oracle�� lpad ���</p>
	 * <p>�޼ҵ� �μ�1	: int num</p>
	 * <p>�޼ҵ� �μ�2	: int len</p>
	 * <p>�޼ҵ� �μ�3	: String p</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String lpad(int num, int len, String p) {
		String str = String.valueOf(num);
		return lpad(str, len, p);
	}
	/**
	 * <p>�޼ҵ��		: rpad</p>
	 * <p>��������		: Oracle�� rpad ���</p>
	 * <p>�޼ҵ� �μ�1	: int num</p>
	 * <p>�޼ҵ� �μ�2	: int len</p>
	 * <p>�޼ҵ� �μ�3	: String p</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String rpad(int num, int len, String p) {
		String str = String.valueOf(num);
		return rpad(str, len, p);
	}

	/**
	 * <p>�޼ҵ��		: numFmt</p>
	 * <p>��������		: numberFormat</p>
	 * <p>�޼ҵ� �μ�1	: double amt</p>
	 * <p>�޼ҵ� �μ�2	: String pattern</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String numFmt(String amt, String pattern) {
		return numFmt(RUtil.parseDouble(amt), pattern);
	}
	public static String numFmt(long amt, String pattern) {
		return numFmt(RUtil.parseDouble(amt+""), pattern);
	}
	public static String numFmt(double amt, String pattern) {
		// default pattern (�ٸ� ������ ���� ������ param �� ���� ������ �Է��Ͽ� �� �� ����)
		if ( (pattern.toUpperCase()).equals("INT"))		pattern = "###,###,###,###,###,##0";
		if ( (pattern.toUpperCase()).equals("DOUBLE"))	pattern = "###,###,###,###,###,##0.000";

		NumberFormat  nf = NumberFormat.getCurrencyInstance();
		DecimalFormat df = (DecimalFormat) nf;

        String formatStr = "";

		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
		df.setDecimalSeparatorAlwaysShown(true);
		df.applyPattern(pattern);

		try {
			formatStr = df.format(amt);
		} catch (IllegalArgumentException e) {
			log.info("Util.toDecimalFormat() has error");
		}
		return formatStr;
	}
	
	/**
	 * <p>�޼ҵ��		: strFmt</p>
	 * <p>��������		: ����ŷ ó�� (���¹�ȣ, �ڵ��� ��)</p>
	 * <p>�޼ҵ� �μ�1	: String str</p>
	 * <p>�޼ҵ� �μ�2	: String pattern</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 * Ex) 1. "#" �� input���ڱ״��
	 *     2. "*" �� ���������� ����
	 *     3. �� �� ���ڴ� �������� �ν� 
	 */
	public static String strFmt(String str, String mask) {
		if (RUtil.isNull(str)) return "";
		
		String mStr = "";
		
		int mIdx =0 ;
		for (int i=0; i<mask.length(); i++) {
			try {
				if (mask.substring(i,i+1).equals("#")) {
					mStr += str.substring(mIdx,mIdx+1);
					mIdx++;
				} else if (mask.substring(i,i+1).equals("*")) {
					mStr += "*";
					mIdx++;
				} else {
					mStr += mask.substring(i,i+1);
				}
			} catch (StringIndexOutOfBoundsException ibe) {
				return mStr;
			}
		}
		return mStr;
	}
	
	/**
	 * <p>�޼ҵ��		: substrB</p>
	 * <p>��������		: substrB byte�� substring</p>
	 * <p>�޼ҵ� �μ�1	: int st</p>
	 * <p>�޼ҵ� �μ�2	: String st</p>
	 * <p>�޼ҵ� �μ�3	: String en</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String substrB(String str, int st) {
		int en = RUtil.lengthB(str);
		return substrB(str, st, en);
	}
	public static String substrB(String str, int st, int en) {
		
		byte[] strB = str.getBytes();
		byte[] rsStrB = new byte[en - st];
		String rsStr = "";

		try {
			for (int i=st; i<en; i++) {
				rsStrB[i-st] = strB[i];
			}
			rsStr = new String(rsStrB);
			return rsStr;
			
		} catch (NullPointerException ne) {
			ne.printStackTrace();
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * <p>�޼ҵ��		: lengthB</p>
	 * <p>��������		: Byte ���� length�� ����</p>
	 * <p>�޼ҵ� �μ�1	: String st</p>
	 * <p>�޼ҵ� ���ϰ�	: int</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static int lengthB(String str) {
		try {
			byte[] strB = str.getBytes();
			return strB.length;
		} catch (Exception e) {
			log.info(e.getMessage());
			return 0;
		}
	}

	/**
	 * <p>�޼ҵ��		: substrByLen</p>
	 * <p>��������		: len��ŭ �߶󳻾� return</p>
	 * <p>�޼ҵ� �μ�1	: String str</p>
	 * <p>�޼ҵ� �μ�2	: int len</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String substrByLen(String str, int len) {
    	int strLen = str.length();
    	int cnt = 0;

    	if (strLen <= len)	return str;

    	for (int i=len; i>=0; i--) {
    		if ((int) str.charAt(i) < 127)
               	break;
    		else
    			cnt = cnt + 1;
    	}

    	if (cnt==0)	cnt = 1;

    	if (cnt%2==0) {
    		len = len - 1;
    	}
    	return str.substring(0, len);
	}
	
	/**
	 * <p>�޼ҵ��		: toCamel, unCamel</p>
	 * <p>��������		: CAMEL_CASE <=> DB_COL_CASE ��ȯ</p>
	 * <p>�޼ҵ� �μ�1	: String str</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String toCamel(String str) {
		str = str.toLowerCase();
		
		Pattern ptn = Pattern.compile("\\_[a-z]{1}");
		Matcher mat = ptn.matcher(str);
        
        while (mat.find()) {
        	str = mat.replaceFirst(mat.group().replaceAll("\\_","").toUpperCase());
        	mat = ptn.matcher(str);
        }
        return str;
	}
	public static String unCamel(String str) {
		String regex = "([a-z])([A-Z])";
		String replacement = "$1_$2";
		return str.replaceAll(regex, replacement).toUpperCase();
	}

	/**
	 * <p>�޼ҵ��		: round</p>
	 * <p>��������		: round �Լ�</p>
	 * <p>�޼ҵ� �μ�1	: double num</p>
	 * <p>�޼ҵ� �μ�2	: int i</p>
	 * <p>�޼ҵ� ���ϰ�	: double</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static long round(long num, int i) {
		long rest = 1;
		for (int x = 0; x < Math.abs(i); x++)
			rest *= 10;
		
		if (i > 0)
			return Math.round(num / rest) * rest;
		else
			return (long) Math.round(num * rest) / rest;
	}
    
	/**
	 * <p>�޼ҵ��		: max</p>
	 * <p>��������		: �迭 �� ū���� ��ȯ</p>
	 * <p>�޼ҵ� �μ�1	: Array</p>
	 * <p>�޼ҵ� ���ϰ�	: maxValue</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static int max(int[] arr) {
		int maxVal = arr[0];
		for (int i=0; i<arr.length; i++) {
			if (maxVal<arr[i]) maxVal = arr[i];
		}
		return maxVal;
	}
	public static long max(long[] arr) {
		long maxVal = arr[0];
		for (int i=0; i<arr.length; i++) {
			if (maxVal<arr[i]) maxVal = arr[i];
		}
		return maxVal;
	}
	public static double max(double[] arr) {
		double maxVal = arr[0];
		for (int i=0; i<arr.length; i++) {
			if (maxVal<arr[i]) maxVal = arr[i];
		}
		return maxVal;
	}
	public static String max(String[] arr) {
		String maxVal = arr[0];
		for (int i=0; i<arr.length; i++) {
			if (arr[i].compareTo(maxVal)>0) {
				maxVal = arr[i];
			}
		}
		return maxVal;
	}
	/**
	 * <p>�޼ҵ��		: min</p>
	 * <p>��������		: �迭 �� �������� ��ȯ</p>
	 * <p>�޼ҵ� �μ�1	: Array</p>
	 * <p>�޼ҵ� ���ϰ�	: mixValue</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static long min(long[] arr) {
		long minVal = arr[0];
		for (int i=0; i<arr.length; i++) {
			if (minVal>arr[i]) minVal = arr[i];
		}
		return minVal;
	}
	public static int min(int[] arr) {
		int minVal = arr[0];
		for (int i=0; i<arr.length; i++) {
			if (minVal>arr[i]) minVal = arr[i];
		}
		return minVal;
	}
	public static double min(double[] arr) {
		double minVal = arr[0];
		for (int i=0; i<arr.length; i++) {
			if (minVal>arr[i]) minVal = arr[i];
		}
		return minVal;
	}
	public static String min(String[] arr) {
		String minVal = arr[0];
		for (int i=0; i<arr.length; i++) {
			if (minVal.compareTo(arr[i])>0) {
				minVal = arr[i];
			}
		}
		return minVal;
	}

    /**
	 * <p>�޼ҵ��		: sumArray</p>
	 * <p>��������		: �迭�� �հ�</p>
	 * <p>�޼ҵ� �μ�1	: Array</p>
	 * <p>�޼ҵ� ���ϰ�	: sum</p>
	 * <p>����ó��		: N/A</p>
	 */
    public static int sumArray(int[] arr) {
		return sumArray(arr, 0, sizeOfArray(arr)-1);
	}
	public static double sumArray(double[] arr) {
		return sumArray(arr, 0, sizeOfArray(arr)-1);
	}
	public static long sumArray(long[] arr) {
		return sumArray(arr, 0, sizeOfArray(arr)-1);
	}
	/**
	 * <p>�޼ҵ��		: sumArray</p>
	 * <p>��������		: �迭�� �հ�</p>
	 * <p>�޼ҵ� �μ�1	: Array</p>
	 * <p>�޼ҵ� �μ�2	: ���� Index</p>
	 * <p>�޼ҵ� �μ�2	: ���� Index</p>
	 * <p>�޼ҵ� ���ϰ�	: sum</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static int sumArray(int[] arr, int idx1, int idx2) {
		int sum = 0;
		for (int i=idx1; i<=idx2; i++) {
			sum += arr[i];
		}
		return sum;
	}
	public static double sumArray(double[] arr, int idx1, int idx2) {
		double sum = 0.000;
		for (int i=idx1; i<=idx2; i++) {
			sum += arr[i];
		}
		return sum;
	}
	public static long sumArray(long[] arr, int idx1, int idx2) {
		long sum = 0;
		for (int i=idx1; i<=idx2; i++) {
			sum += arr[i];
		}
		return sum;
	}
	/**
	 * <p>�޼ҵ��		: avgArray</p>
	 * <p>��������		: �迭�� ���</p>
	 * <p>�޼ҵ� �μ�1	: double[] arr</p>
	 * <p>�޼ҵ� ���ϰ�	: avg</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static double avgArray(double[] arr) {
		return avgArray(arr, 0 , sizeOfArray(arr)-1);
	}
	/**
	 * <p>�޼ҵ��		: avgArray</p>
	 * <p>��������		: �迭�� ���</p>
	 * <p>�޼ҵ� �μ�1	: double[] arr</p>
	 * <p>�޼ҵ� �μ�2	: ���� Index</p>
	 * <p>�޼ҵ� �μ�2	: ���� Index</p>
	 * <p>�޼ҵ� ���ϰ�	: avg</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static double avgArray(double[] arr, int idx1, int idx2) {
		double sum = 0.000;
		for (int i=idx1; i<=idx2; i++) {
			sum += arr[i];
		}
		return sum/((idx2-idx1)*1.0D);
	}
	
	/**
	 * <p>�޼ҵ��		: sizeOfList</p>
	 * <p>��������		: Size of List</p>
	 * <p>�޼ҵ� �μ�1 	: List</p>
	 * <p>�޼ҵ� ���ϰ�	: int</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static int sizeOfList(List list) {
		return (list==null) ? 0 : list.size();
	}
	public static int size(List list) {
		return (list==null) ? 0 : list.size();
	}

	/**
	 * <p>�޼ҵ��		: sizeOfArray</p>
	 * <p>��������		: length of Array</p>
	 * <p>�޼ҵ� �μ�1 	: Array</p>
	 * <p>�޼ҵ� ���ϰ�	: int</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static int sizeOfArray(Object[] arr) {
		return (arr==null) ? 0 : arr.length;
	}
	public static int sizeOfArray(String[] arr) {
		return (arr==null) ? 0 : arr.length;
	}
	public static int sizeOfArray(int[] arr) {
		return (arr==null) ? 0 : arr.length;
	}
	public static int sizeOfArray(long[] arr) {
		return (arr==null) ? 0 : arr.length;
	}
	public static int sizeOfArray(double[] arr) {
		return (arr==null) ? 0 : arr.length;
	}
	
	public static int size(Object[] arr) {
		return (arr==null) ? 0 : arr.length;
	}
	public static int size(String[] arr) {
		return (arr==null) ? 0 : arr.length;
	}
	public static int size(int[] arr) {
		return (arr==null) ? 0 : arr.length;
	}
	public static int size(long[] arr) {
		return (arr==null) ? 0 : arr.length;
	}
	public static int size(double[] arr) {
		return (arr==null) ? 0 : arr.length;
	}
	
	/**
	 * <p>�޼ҵ��		: parseNature</p>
	 * <p>��������		: �ڿ����� ��ȯ (0���� ������ 0����..)</p>
	 * <p>�޼ҵ� �μ�1	: Object num</p>
	 * <p>�޼ҵ� ���ϰ�	: long</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static long parseNature(Object num) {
		if (num==null) return 0;
		
		String strNum = num.toString();
			strNum = nvl(nvl(strNum, "0").replaceAll("[^0-9.-]", ""), "0");
			
		long tmpNum = Long.parseLong(strNum);
		if (tmpNum<0) tmpNum = 0;
		return tmpNum;
	}
	/**
	 * <p>�޼ҵ��		: parseInt</p>
	 * <p>��������		: convert int</p>
	 * <p>�޼ҵ� �μ�1	: String str</p>
	 * <p>�޼ҵ� ���ϰ�	: int</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static int parseInt(String str) {
		str = nvl(str, "0").replaceAll("[^0-9.-]", "");
		int idx = str.indexOf(".");
		if (idx>0) str = str.substring(0, idx);
		return Integer.parseInt(str);
	}
	/**
	 * <p>�޼ҵ��		: parseLong</p>
	 * <p>��������		: convert Long</p>
	 * <p>�޼ҵ� �μ�1	: String str</p>
	 * <p>�޼ҵ� ���ϰ�	: long</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static long parseLong(String str) {
		str = nvl(str, "0").replaceAll("[^0-9.-]", "");
		int idx = str.indexOf(".");
		if (idx>0) str = str.substring(0, idx);
		return Long.parseLong(str);
	}
	/**
	 * <p>�޼ҵ��		: parseDouble</p>
	 * <p>��������		: convert double</p>
	 * <p>�޼ҵ� �μ�1	: String str</p>
	 * <p>�޼ҵ� ���ϰ�	: double</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static double parseDouble(String str) {
		str = nvl(str, "0").replaceAll("[^0-9.-]", "");
		return Double.parseDouble(str);
	}
	
	/**
	 * <p>�޼ҵ��		: isFileExist</p>
	 * <p>��������		: �����н� �Ǵ� �����н��� �������� Ȯ��</p>
	 * <p>�޼ҵ� �μ�1 	: fileNm</p>
	 * <p>�޼ҵ� ���ϰ�	: boolean</p>
	 * <p>����ó��		: throws BatchException</p>
	 */
	public static boolean isFileExist(String fileNm) {
		try {
			File file = new File(fileNm);
			if (file.exists()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * <p>�޼ҵ��		: makeDir</p>
	 * <p>��������		: ���丮 ������� ����</p>
	 * <p>�޼ҵ� �μ�1 	: fileNm</p>
	 * <p>�޼ҵ� ���ϰ�	: N/A</p>
	 * <p>����ó��		: throws IOException</p>
	 */
	public static void makeDir(String dirPath) throws IOException {
		if (!RUtil.isFileExist(dirPath)) {
			FileUtils.forceMkdir(new File(dirPath));
		}
	}
	
	/**
	 * <p>�޼ҵ��		: makeDir</p>
	 * <p>��������		: String '+str+'ó��</p>
	 * <p>�޼ҵ� �μ�1 	: str</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String addQuote(String str) {
		return "'"+RUtil.nvl(str)+"'";
	}
	
	/**
	 * <p>�޼ҵ��		: logArray</p>
	 * <p>��������		: Array�� ������ toString</p>
	 * <p>�޼ҵ� �μ�1 	: str</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static void logArray(String[] arr) {
		log.info("�� arr.toString() # length="+sizeOfArray(arr)+" ----------------------------");
		for (int i=0; i<sizeOfArray(arr); i++) {
			log.info("Array["+i+"]"+arr[i]);
		}
	}
	public static void logArray(long[] arr) {
		log.info("�� arr.toString() # length="+sizeOfArray(arr)+" ----------------------------");
		for (int i=0; i<sizeOfArray(arr); i++) {
			log.info("Array["+i+"]"+arr[i]);
		}
	}
	/**
	 * <p>�޼ҵ��		: logSql</p>
	 * <p>��������		: preparedStatement SQL�� ������ ���</p>
	 * <p>�޼ҵ� �μ�1 	: str</p>
	 * <p>�޼ҵ� ���ϰ�	: String</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static void logSql(String sql, Object[] arr) {
		String[] splt = sql.split("\\?");
		String query = "";
		for (int i=0; i<arr.length; i++) {
			query += splt[i] + addQuote((String.valueOf(arr[i])));
		}
		query += splt[splt.length-1];
		log.info("�� Query Info ----------------------------");
		log.info(query);
	}
	public static void logSql(String sql, String[] arr) {
		String[] splt = sql.split("\\?");
		String query = "";
		for (int i=0; i<arr.length; i++) {
			query += splt[i] + addQuote(arr[i]);
		}
		query += splt[splt.length-1];
		log.info("�� Query Info ----------------------------");
		log.info(query);
	}
	
	/**
	 * �ݰ����ڷ� �����Ѵ�
     * @param src �����Ұ�
     * @return String ����Ȱ�
     */
	public static String toHalfChar(String src) {
		StringBuffer strBuf = new StringBuffer();
		char c = 0;
		int nSrcLength = src.length();
		for (int i = 0; i < nSrcLength; i++) {
			c = src.charAt(i);
			//�����̰ų� Ư�� ���� �ϰ��.
			if (c >= '��' && c <= '��') {
				c -= 0xfee0;
			}
			else if (c == '��') {
				c = 0x20;
			}
			// ���ڿ� ���ۿ� ��ȯ�� ���ڸ� �״´�
			strBuf.append(c);
		}
		return strBuf.toString();
	}
    
	/**
	 * �������ڷ� �����Ѵ�.
	 * @param src �����Ұ�
	 * @return String ����Ȱ�
	 */
	public static String toFullChar(String src) {
		// �Էµ� ��Ʈ���� null �̸� null �� ����
		if (src == null) return null;
		
		// ��ȯ�� ���ڵ��� �׾Ƴ��� StringBuffer �� �����Ѵ�
		StringBuffer strBuf = new StringBuffer();
		char c = 0;
		int nSrcLength = src.length();
		for (int i = 0; i < nSrcLength; i++) {
			c = src.charAt(i);
			//�����̰ų� Ư�� ���� �ϰ��.
			if (c >= 0x21 && c <= 0x7e) {
				c += 0xfee0;
            }
			//�����ϰ��
			else if (c == 0x20) {
				c = 0x3000;
			}
			// ���ڿ� ���ۿ� ��ȯ�� ���ڸ� �״´�
			strBuf.append(c);
		}
		return strBuf.toString();
	}
	
	/**
	 * Ebcdic ��ȯ
	 * @param �����Ұ�, �ѱ۽���idx, �ѱ�len
	 * @return String ����Ȱ�
	 */
	public static byte[] convEBCDIC(String txt, int[] strtIdx, int[] korLen) throws Exception {
		int korCnt = strtIdx.length;	// �ѱ۰���
		byte txtByte[] = txt.getBytes();
		byte rsByte[] = new byte[txtByte.length];
		//log.info("�� txtLen = "+txtByte.length);
		
		for (int i=0; i<rsByte.length; i++) {
			rsByte[i] = (byte) 0x40;
		}

		byte temp[] = new byte[2000];

		String tempS = "";
		int startP = 0;

		if (korCnt==0) {
			//log.info("0)"+txt);
			rsByte = txt.getBytes("Cp933");
			
		} else {
			for (int i=0; i<korCnt; i++) {
	
				System.arraycopy(txtByte,startP,temp,0,strtIdx[i]-startP);
				tempS = new String(temp);
				//log.info("1)"+tempS);
				temp = tempS.getBytes("Cp933");
	
				System.arraycopy(temp,0,rsByte,startP,strtIdx[i]-startP);
				tempS = "";
				temp = new byte[2000];
	
				System.arraycopy(txtByte,strtIdx[i],temp,0,korLen[i]);
				tempS = new String(temp).trim();
				//log.info("2)"+tempS);
				temp = tempS.getBytes("Cp933");
	
				System.arraycopy(temp,0,rsByte,strtIdx[i],temp.length);
				tempS = "";
				temp = new byte[2000];
	
				startP = strtIdx[i] + korLen[i];
			}
	
			System.arraycopy(txtByte,strtIdx[korCnt-1]+korLen[korCnt-1],temp,0,txtByte.length - (strtIdx[korCnt-1]+korLen[korCnt-1])  );
	
			tempS = new String(temp);
			//log.info("3)"+tempS);
			temp = tempS.getBytes("Cp933");
	
			System.arraycopy(temp,0,rsByte,strtIdx[korCnt-1]+korLen[korCnt-1],txtByte.length - (strtIdx[korCnt-1]+korLen[korCnt-1]) );
		}
		//log.info("�� rsLen = "+rsByte.length);
		return rsByte;
	}
	
	/**
	 * <p>�޼ҵ��		: getMValue</p>
	 * <p>��������		: Map���� ����</p>
	 * <p>�޼ҵ� �μ�1	: Map</p>
	 * <p>�޼ҵ� �μ�2	: String key</p>
	 * <p>�޼ҵ� ���ϰ�	: String value</p>
	 * <p>����ó��		: N/A</p>
	 */
	public static String getMValue(Map map, String key) {
		String val = String.valueOf(map.get(key));
		return nvl(val);
	}
	
	// ��ȣȭ (�츮ī��)
	public static String encRegiNo(String regiNo) {
		if (regiNo==null) return null;	// update�� ������ ���� null�� ��� ��
		if (RUtil.isNull(regiNo)) return "";
		
		String cryptStr = "";
		try {
			//log.info("�� encRegiNo BF =>"+regiNo);
			cryptStr = Util.EncDecResiNoString("I", regiNo); 
			//log.info("�� encRegiNo AF =>"+cryptStr);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return cryptStr;
	}
	// ��ȣȭ (�츮ī��)
	public static String decRegiNo(String regiKey) {
		if (regiKey==null) return null;
		if (RUtil.isNull(regiKey)) return "";
		
		String cryptStr = "";
		try {
			//log.info("�� decRegiNo BF =>"+regiKey);
			cryptStr = Util.EncDecResiNoString("S", regiKey);
			//log.info("�� decRegiNo AF =>"+cryptStr);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return cryptStr;
	}
	public static String decRegiNo(ResultSet rs, String colNm) {
		String cryptStr = "";
		try {
			cryptStr = decRegiNo(rs.getString(colNm));
		} catch (SQLException e) {
			log.info(e.getMessage());
		}
		return cryptStr;
	}
	public static String decRegiNo(ResultSet rs, int colIdx) {
		String cryptStr = "";
		try {
			cryptStr = decRegiNo(rs.getString(colIdx));
		} catch (SQLException e) {
			log.info(e.getMessage());
		}
		return cryptStr;
	}
	// ��ȣȭ ����ŷ (�츮ī��)
	public static String decMaskRegiNo(String regiKey) {
		String cryptStr = decRegiNo(regiKey);
		return maskRegiNo(cryptStr);
	}
	public static String decMaskRegiNo(ResultSet rs, String colNm) {
		String cryptStr = decRegiNo(rs, colNm);
		return maskRegiNo(cryptStr);
	}
	public static String decMaskRegiNo(ResultSet rs, int colIdx) {
		String cryptStr = decRegiNo(rs, colIdx);
		return maskRegiNo(cryptStr);
	}
	// �ֹι�ȣ ����ŷ
	public static String maskRegiNo(String str) {
		if (str.length()==13) {
			str = str.substring(0,6)+"-"+str.substring(6,7)+"******";
		} else if (str.length()==10) {
			str = str.substring(0,3)+"-"+str.substring(3,5)+"-"+str.substring(5);
		}
		return str;
	}
}
