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
 * <p>파일명		: RUtil.java</p>
 * <p>버　전		: 1.0</p>
 * <p>작성일		: 2012-10-17</p>
 * <p>작성자		: 정우현</p>
 * <p>UseCase명	: 청구입금 Util</p>
 * <p>프로그램명	: 청구입금 Util</p>
 * <p>내용		: 청구입금 Util</p>
 * <pre>
 * -----------------------------------------------------------
 * 2012.10.17 정우현 - 소스표준화, 최초작성
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
	 * <p>메소드명		: getKSTDate</p>
	 * <p>설　　명		: 현재 일자값을 가져온다.</p>
	 * <p>메소드 인수1	: N/A</p>
	 * <p>메소드 리턴값	: YYYY-MM-DD</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static String getKSTDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date();
		return formatter.format(curDate);
	}
	/**
	 * <p>메소드명		: getKSTDate</p>
	 * <p>설　　명		: 현재 일자값을 해당포맷으로 가져온다.</p>
	 * <p>메소드 인수1	: DateFormat (yyyyMMddhhmmss)</p>
	 * <p>메소드 리턴값	: String(Formatted Date)</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: isValidDt</p>
	 * <p>설　　명		: 날짜형식에 맞는지 검증</p>
	 * <p>메소드 인수1	: String date</p>
	 * <p>메소드 리턴값	: boolean</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: getDayWeek</p>
	 * <p>설　　명		: 특정날짜의 요일을 구함</p>
	 * <p>메소드 인수1	: String date</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
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
			weekDay = decode(tmp, new String[]{"1","일","2","월","3","화","4","수","5","목","6","금","7","토",""});
//			log.info("■ ["+date+"] "+weekDay+"요일 : "+tmp);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return weekDay;
	}

	/**
	 * <p>메소드명		: parseFdt</p>
	 * <p>설　　명		: DATE FORMAT</p>
	 * <p>메소드 인수1	: String date</p>
	 * <p>메소드 인수2	: String format</p>
	 * <p>메소드 리턴값	: String(Formatted Date)</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static String parseFdt(String date, String format) {
		date = date.replaceAll("[^0-9]", "");	// 숫자를 제외한 나머지 문자 제거
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
	 * <p>메소드명		: getFormatDt</p>
	 * <p>설　　명		: dv의 패턴을 넣은 DATE FORMAT을 리턴</p>
	 * <p>메소드 인수1	: String date</p>
	 * <p>메소드 인수2	: String dv</p>
	 * <p>메소드 리턴값	: String(Formatted Date)</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static String getFormatDt(String date, String dv) {
		String formattedDate = "";
		if (isNull(date)) {
			formattedDate = "";
		} else {
			date = date.replaceAll("[^0-9]", "");	// 숫자를 제외한 나머지 문자 제거
			formattedDate = date.substring(0, 4)+dv+date.substring(4, 6);
			if (date.length()==8) formattedDate += dv+date.substring(6);
		}
		return formattedDate;
	}
	/**
	 * <p>메소드명		: getFormatDt6</p>
	 * <p>설　　명		: DATE FORMAT을 리턴 YYMMDD</p>
	 * <p>메소드 인수1	: String date</p>
	 * <p>메소드 리턴값	: String(Formatted Date)</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static String getFormatDt6(String date) {
		String formattedDate = "";
		if (isNull(date)) {
			formattedDate = "";
		} else {
			date = date.replaceAll("[^0-9]", "");	// 숫자를 제외한 나머지 문자 제거
			formattedDate = date.substring(2, 6);   //YYMMDD			
		}
		return formattedDate;
	}
	
	/**
	 * <p>메소드명		: addDate</p>
	 * <p>설　　명		: 날짜계산 유틸</p>
	 * <p>메소드 인수1	: String date</p>
	 * <p>메소드 인수2	: int</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: addMonth</p>
	 * <p>설　　명		: 월 계산 유틸(오라클의  addMonths)</p>
	 * <p>메소드 인수1	: String yyyyMM/yyyyMMdd</p>
	 * <p>메소드 인수2	: int</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static String addMonth(String date, int add) {
		
		date = RUtil.nvl(date).replaceAll("[^0-9]", "");	// 숫자를 제외한 나머지 문자 제거
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
			
			cal.set(yy, mm - 1, dd);	// 자바의 월은 0~11이므로 실제 월에서 1을 빼준다
			cal.add(GregorianCalendar.MONTH, add);

			SimpleDateFormat formatter = new SimpleDateFormat(format);
			return formatter.format(cal.getTime());
		}
		catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * <p>메소드명		: dateDiff</p>
	 * <p>설　　명		: 두 날짜의 차이를 구함</p>
	 * <p>메소드 인수1	: String sdt</p>
	 * <p>메소드 인수2	: String edt</p>
	 * <p>메소드 리턴값	: day Count</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static long dateDiff(String sdt, String edt) {
		// 숫자를 제외한 나머지 문자 제거
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
	 * <p>메소드명		: monthDiff</p>
	 * <p>설　　명		: 두 월의 차이를 구함</p>
	 * <p>메소드 인수1	: String sdt</p>
	 * <p>메소드 인수2	: String edt</p>
	 * <p>메소드 리턴값	: month Count</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static long monthDiff(String sdt, String edt) {
		// 숫자를 제외한 나머지 문자 제거
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
	 * <p>메소드명		: isLeapYr</p>
	 * <p>설　　명		: 윤년 여부</p>
	 * <p>메소드 인수1	: String year</p>
	 * <p>메소드 리턴값	: boolean</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static boolean isLeapYr(String dt) {
		if (dt.length()<4) return false;
		
		// YYYY 이외 포맷으로 입력 했을 시 앞 4자리 'YYYY'만 사용
		dt = dt.substring(0, 4);
		int year = RUtil.parseInt(dt);
		
		return ((year%4==0) && (year%100!=0) || (year%400==0));
    }
	/**
	 * <p>메소드명		: lastMdt</p>
	 * <p>설　　명		: 월말일</p>
	 * <p>메소드 인수1	: String dt</p>
	 * <p>메소드 리턴값	: String dt</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static String lastMdt(String dt) {
		dt = dt.replaceAll("[^0-9]", "");
		dt = addMonth(dt, 1).substring(0,7)+"-01";	// 다음달 1일을 구해서 하루 뺀다.
		return addDate(dt, -1);
    }

	/**
	 * <p>메소드명		: isNull</p>
	 * <p>설　　명		: null 체크</p>
	 * <p>메소드 인수1	: String str</p>
	 * <p>메소드 리턴값	: boolean</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static boolean isNull(String str) {
		boolean bool = false;
		if(nvl(str).equals("")) {
			bool = true;
		}
		return bool;
	}
	/**
	 * <p>메소드명		: isNum</p>
	 * <p>설　　명		: 숫자 체크</p>
	 * <p>메소드 인수1	: String str</p>
	 * <p>메소드 리턴값	: boolean</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: replace</p>
	 * <p>설　　명		: replace 기능 (null 일 경우 "" 처리)</p>
	 * <p>메소드 인수1	: String str</p>
	 * <p>메소드 인수2	: String old(대상 문자)</p>
	 * <p>메소드 인수3	: String chg(대체 문자)</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: nvl</p>
	 * <p>설　　명		: Oracle의 nvl 기능</p>
	 * <p>메소드 인수1	: String str</p>
	 * <p>메소드 인수2	: String replace</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
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
	// Query NVL(오라클 프로젝트 일 경우) : Not Null Column 에 저장 시 Null값을 " " 처리
	// DB가 오라클 아닐경우 " " => "" 으로 바꿀 것 
	public static String qNvl(String str) {
		return nvl(str, " ");
	}

	/**
	 * <p>메소드명		: decode</p>
	 * <p>설　　명		: Oracle의 decode 기능</p>
	 * <p>메소드 인수1	: String str</p>
	 * <p>메소드 인수2	: String[] arr</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: like</p>
	 * <p>설　　명		: Oracle의 like 기능</p>
	 * <p>메소드 인수1	: String str</p>
	 * <p>메소드 인수2	: String cmpr</p>
	 * <p>메소드 리턴값	: boolean</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: in</p>
	 * <p>설　　명		: Oracle의 in 기능</p>
	 * <p>메소드 인수1	: String str</p>
	 * <p>메소드 인수2	: String[] cmpr</p>
	 * <p>메소드 리턴값	: boolean</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: in</p>
	 * <p>설　　명		: Oracle의 in 기능</p>
	 * <p>메소드 인수1	: int str</p>
	 * <p>메소드 인수2	: int[] cmpr</p>
	 * <p>메소드 리턴값	: boolean</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: trim</p>
	 * <p>설　　명		: Oracle의 trim 기능</p>
	 * <p>메소드 인수1	: String str</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static String trim(String str) {
		//str.replaceAll("/(^\s*)|(\s*$)/", "0");
		return nvl(str).trim();
	}
	/**
	 * <p>메소드명		: ltrim</p>
	 * <p>설　　명		: Oracle의 ltrim 기능</p>
	 * <p>메소드 인수1	: String str</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: rtrim</p>
	 * <p>설　　명		: Oracle의 rtrim 기능</p>
	 * <p>메소드 인수1	: String str</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: trimToNull</p>
	 * <p>설　　명		: trim을 수행하고, 공백인 값은 Null로 반환</p>
	 * <p>메소드 인수1	: String str</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static String trimToNull(String str) {
		str = nvl(str).trim();
		return (str.equals(""))?null:str;
	}

	/**
	 * <p>메소드명		: lpad</p>
	 * <p>설　　명		: Oracle의 lpad 기능</p>
	 * <p>메소드 인수1	: String str</p>
	 * <p>메소드 인수2	: int len</p>
	 * <p>메소드 인수3	: String p</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: rpad</p>
	 * <p>설　　명		: Oracle의 rpad 기능</p>
	 * <p>메소드 인수1	: String str</p>
	 * <p>메소드 인수2	: int len</p>
	 * <p>메소드 인수3	: String p</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: lpad</p>
	 * <p>설　　명		: Oracle의 lpad 기능</p>
	 * <p>메소드 인수1	: int num</p>
	 * <p>메소드 인수2	: int len</p>
	 * <p>메소드 인수3	: String p</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static String lpad(int num, int len, String p) {
		String str = String.valueOf(num);
		return lpad(str, len, p);
	}
	/**
	 * <p>메소드명		: rpad</p>
	 * <p>설　　명		: Oracle의 rpad 기능</p>
	 * <p>메소드 인수1	: int num</p>
	 * <p>메소드 인수2	: int len</p>
	 * <p>메소드 인수3	: String p</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static String rpad(int num, int len, String p) {
		String str = String.valueOf(num);
		return rpad(str, len, p);
	}

	/**
	 * <p>메소드명		: numFmt</p>
	 * <p>설　　명		: numberFormat</p>
	 * <p>메소드 인수1	: double amt</p>
	 * <p>메소드 인수2	: String pattern</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static String numFmt(String amt, String pattern) {
		return numFmt(RUtil.parseDouble(amt), pattern);
	}
	public static String numFmt(long amt, String pattern) {
		return numFmt(RUtil.parseDouble(amt+""), pattern);
	}
	public static String numFmt(double amt, String pattern) {
		// default pattern (다른 패턴을 쓰고 싶으면 param 에 직접 패턴을 입력하여 쓸 수 있음)
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
	 * <p>메소드명		: strFmt</p>
	 * <p>설　　명		: 마스킹 처리 (계좌번호, 핸드폰 등)</p>
	 * <p>메소드 인수1	: String str</p>
	 * <p>메소드 인수2	: String pattern</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
	 * Ex) 1. "#" 은 input문자그대로
	 *     2. "*" 은 가림용으로 쓰임
	 *     3. 그 외 문자는 패턴으로 인식 
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
	 * <p>메소드명		: substrB</p>
	 * <p>설　　명		: substrB byte로 substring</p>
	 * <p>메소드 인수1	: int st</p>
	 * <p>메소드 인수2	: String st</p>
	 * <p>메소드 인수3	: String en</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: lengthB</p>
	 * <p>설　　명		: Byte 수로 length를 구함</p>
	 * <p>메소드 인수1	: String st</p>
	 * <p>메소드 리턴값	: int</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: substrByLen</p>
	 * <p>설　　명		: len만큼 잘라내어 return</p>
	 * <p>메소드 인수1	: String str</p>
	 * <p>메소드 인수2	: int len</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: toCamel, unCamel</p>
	 * <p>설　　명		: CAMEL_CASE <=> DB_COL_CASE 변환</p>
	 * <p>메소드 인수1	: String str</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: round</p>
	 * <p>설　　명		: round 함수</p>
	 * <p>메소드 인수1	: double num</p>
	 * <p>메소드 인수2	: int i</p>
	 * <p>메소드 리턴값	: double</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: max</p>
	 * <p>설　　명		: 배열 중 큰값을 반환</p>
	 * <p>메소드 인수1	: Array</p>
	 * <p>메소드 리턴값	: maxValue</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: min</p>
	 * <p>설　　명		: 배열 중 작은값을 반환</p>
	 * <p>메소드 인수1	: Array</p>
	 * <p>메소드 리턴값	: mixValue</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: sumArray</p>
	 * <p>설　　명		: 배열의 합계</p>
	 * <p>메소드 인수1	: Array</p>
	 * <p>메소드 리턴값	: sum</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: sumArray</p>
	 * <p>설　　명		: 배열의 합계</p>
	 * <p>메소드 인수1	: Array</p>
	 * <p>메소드 인수2	: 시작 Index</p>
	 * <p>메소드 인수2	: 종료 Index</p>
	 * <p>메소드 리턴값	: sum</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: avgArray</p>
	 * <p>설　　명		: 배열의 평균</p>
	 * <p>메소드 인수1	: double[] arr</p>
	 * <p>메소드 리턴값	: avg</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static double avgArray(double[] arr) {
		return avgArray(arr, 0 , sizeOfArray(arr)-1);
	}
	/**
	 * <p>메소드명		: avgArray</p>
	 * <p>설　　명		: 배열의 평균</p>
	 * <p>메소드 인수1	: double[] arr</p>
	 * <p>메소드 인수2	: 시작 Index</p>
	 * <p>메소드 인수2	: 종료 Index</p>
	 * <p>메소드 리턴값	: avg</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static double avgArray(double[] arr, int idx1, int idx2) {
		double sum = 0.000;
		for (int i=idx1; i<=idx2; i++) {
			sum += arr[i];
		}
		return sum/((idx2-idx1)*1.0D);
	}
	
	/**
	 * <p>메소드명		: sizeOfList</p>
	 * <p>설　　명		: Size of List</p>
	 * <p>메소드 인수1 	: List</p>
	 * <p>메소드 리턴값	: int</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static int sizeOfList(List list) {
		return (list==null) ? 0 : list.size();
	}
	public static int size(List list) {
		return (list==null) ? 0 : list.size();
	}

	/**
	 * <p>메소드명		: sizeOfArray</p>
	 * <p>설　　명		: length of Array</p>
	 * <p>메소드 인수1 	: Array</p>
	 * <p>메소드 리턴값	: int</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: parseNature</p>
	 * <p>설　　명		: 자연수로 변환 (0보다 작으면 0으로..)</p>
	 * <p>메소드 인수1	: Object num</p>
	 * <p>메소드 리턴값	: long</p>
	 * <p>예외처리		: N/A</p>
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
	 * <p>메소드명		: parseInt</p>
	 * <p>설　　명		: convert int</p>
	 * <p>메소드 인수1	: String str</p>
	 * <p>메소드 리턴값	: int</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static int parseInt(String str) {
		str = nvl(str, "0").replaceAll("[^0-9.-]", "");
		int idx = str.indexOf(".");
		if (idx>0) str = str.substring(0, idx);
		return Integer.parseInt(str);
	}
	/**
	 * <p>메소드명		: parseLong</p>
	 * <p>설　　명		: convert Long</p>
	 * <p>메소드 인수1	: String str</p>
	 * <p>메소드 리턴값	: long</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static long parseLong(String str) {
		str = nvl(str, "0").replaceAll("[^0-9.-]", "");
		int idx = str.indexOf(".");
		if (idx>0) str = str.substring(0, idx);
		return Long.parseLong(str);
	}
	/**
	 * <p>메소드명		: parseDouble</p>
	 * <p>설　　명		: convert double</p>
	 * <p>메소드 인수1	: String str</p>
	 * <p>메소드 리턴값	: double</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static double parseDouble(String str) {
		str = nvl(str, "0").replaceAll("[^0-9.-]", "");
		return Double.parseDouble(str);
	}
	
	/**
	 * <p>메소드명		: isFileExist</p>
	 * <p>설　　명		: 파일패스 또는 폴더패스의 존재유무 확인</p>
	 * <p>메소드 인수1 	: fileNm</p>
	 * <p>메소드 리턴값	: boolean</p>
	 * <p>예외처리		: throws BatchException</p>
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
	 * <p>메소드명		: makeDir</p>
	 * <p>설　　명		: 디렉토리 미존재시 생성</p>
	 * <p>메소드 인수1 	: fileNm</p>
	 * <p>메소드 리턴값	: N/A</p>
	 * <p>예외처리		: throws IOException</p>
	 */
	public static void makeDir(String dirPath) throws IOException {
		if (!RUtil.isFileExist(dirPath)) {
			FileUtils.forceMkdir(new File(dirPath));
		}
	}
	
	/**
	 * <p>메소드명		: makeDir</p>
	 * <p>설　　명		: String '+str+'처리</p>
	 * <p>메소드 인수1 	: str</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static String addQuote(String str) {
		return "'"+RUtil.nvl(str)+"'";
	}
	
	/**
	 * <p>메소드명		: logArray</p>
	 * <p>설　　명		: Array의 내용을 toString</p>
	 * <p>메소드 인수1 	: str</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static void logArray(String[] arr) {
		log.info("■ arr.toString() # length="+sizeOfArray(arr)+" ----------------------------");
		for (int i=0; i<sizeOfArray(arr); i++) {
			log.info("Array["+i+"]"+arr[i]);
		}
	}
	public static void logArray(long[] arr) {
		log.info("■ arr.toString() # length="+sizeOfArray(arr)+" ----------------------------");
		for (int i=0; i<sizeOfArray(arr); i++) {
			log.info("Array["+i+"]"+arr[i]);
		}
	}
	/**
	 * <p>메소드명		: logSql</p>
	 * <p>설　　명		: preparedStatement SQL의 내용을 출력</p>
	 * <p>메소드 인수1 	: str</p>
	 * <p>메소드 리턴값	: String</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static void logSql(String sql, Object[] arr) {
		String[] splt = sql.split("\\?");
		String query = "";
		for (int i=0; i<arr.length; i++) {
			query += splt[i] + addQuote((String.valueOf(arr[i])));
		}
		query += splt[splt.length-1];
		log.info("■ Query Info ----------------------------");
		log.info(query);
	}
	public static void logSql(String sql, String[] arr) {
		String[] splt = sql.split("\\?");
		String query = "";
		for (int i=0; i<arr.length; i++) {
			query += splt[i] + addQuote(arr[i]);
		}
		query += splt[splt.length-1];
		log.info("■ Query Info ----------------------------");
		log.info(query);
	}
	
	/**
	 * 반각문자로 변경한다
     * @param src 변경할값
     * @return String 변경된값
     */
	public static String toHalfChar(String src) {
		StringBuffer strBuf = new StringBuffer();
		char c = 0;
		int nSrcLength = src.length();
		for (int i = 0; i < nSrcLength; i++) {
			c = src.charAt(i);
			//영문이거나 특수 문자 일경우.
			if (c >= '！' && c <= '～') {
				c -= 0xfee0;
			}
			else if (c == '　') {
				c = 0x20;
			}
			// 문자열 버퍼에 변환된 문자를 쌓는다
			strBuf.append(c);
		}
		return strBuf.toString();
	}
    
	/**
	 * 전각문자로 변경한다.
	 * @param src 변경할값
	 * @return String 변경된값
	 */
	public static String toFullChar(String src) {
		// 입력된 스트링이 null 이면 null 을 리턴
		if (src == null) return null;
		
		// 변환된 문자들을 쌓아놓을 StringBuffer 를 마련한다
		StringBuffer strBuf = new StringBuffer();
		char c = 0;
		int nSrcLength = src.length();
		for (int i = 0; i < nSrcLength; i++) {
			c = src.charAt(i);
			//영문이거나 특수 문자 일경우.
			if (c >= 0x21 && c <= 0x7e) {
				c += 0xfee0;
            }
			//공백일경우
			else if (c == 0x20) {
				c = 0x3000;
			}
			// 문자열 버퍼에 변환된 문자를 쌓는다
			strBuf.append(c);
		}
		return strBuf.toString();
	}
	
	/**
	 * Ebcdic 변환
	 * @param 변경할값, 한글시작idx, 한글len
	 * @return String 변경된값
	 */
	public static byte[] convEBCDIC(String txt, int[] strtIdx, int[] korLen) throws Exception {
		int korCnt = strtIdx.length;	// 한글갯수
		byte txtByte[] = txt.getBytes();
		byte rsByte[] = new byte[txtByte.length];
		//log.info("■ txtLen = "+txtByte.length);
		
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
		//log.info("■ rsLen = "+rsByte.length);
		return rsByte;
	}
	
	/**
	 * <p>메소드명		: getMValue</p>
	 * <p>설　　명		: Map값을 리턴</p>
	 * <p>메소드 인수1	: Map</p>
	 * <p>메소드 인수2	: String key</p>
	 * <p>메소드 리턴값	: String value</p>
	 * <p>예외처리		: N/A</p>
	 */
	public static String getMValue(Map map, String key) {
		String val = String.valueOf(map.get(key));
		return nvl(val);
	}
	
	// 암호화 (우리카드)
	public static String encRegiNo(String regiNo) {
		if (regiNo==null) return null;	// update시 공백이 들어가서 null로 줘야 함
		if (RUtil.isNull(regiNo)) return "";
		
		String cryptStr = "";
		try {
			//log.info("■ encRegiNo BF =>"+regiNo);
			cryptStr = Util.EncDecResiNoString("I", regiNo); 
			//log.info("■ encRegiNo AF =>"+cryptStr);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return cryptStr;
	}
	// 복호화 (우리카드)
	public static String decRegiNo(String regiKey) {
		if (regiKey==null) return null;
		if (RUtil.isNull(regiKey)) return "";
		
		String cryptStr = "";
		try {
			//log.info("■ decRegiNo BF =>"+regiKey);
			cryptStr = Util.EncDecResiNoString("S", regiKey);
			//log.info("■ decRegiNo AF =>"+cryptStr);
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
	// 복호화 마스킹 (우리카드)
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
	// 주민번호 마스킹
	public static String maskRegiNo(String str) {
		if (str.length()==13) {
			str = str.substring(0,6)+"-"+str.substring(6,7)+"******";
		} else if (str.length()==10) {
			str = str.substring(0,3)+"-"+str.substring(3,5)+"-"+str.substring(5);
		}
		return str;
	}
}
