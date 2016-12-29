package util;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cbscap.batch.common.ConnMng;
import com.cbscap.bc.s.cs.cs.CustBC;
import com.cbscap.bc.s.cs.cs.ICustBC;
import com.cbscap.bc.s.cs.cs.StCustBaseTDO;
import com.cbscap.bc.s.cs.cs.StCustInfoReadHistTDO;
import com.cbscap.fw.bc.c.BCException;
import com.cbscap.fw.bc.c.CommonBCFactory;
import com.cbscap.fw.bc.c.IBizBC;
import com.cbscap.fw.dbutil.transaction.ITransaction;
import com.cbscap.fw.util.format.ServerSysDate;
import com.cbscap.fw.web.ICbcFwConstant;
import com.cbscap.fw.web.user.User;
import com.cbscap.sc.r.util.RUtil;
import com.cbscap.xutil.DateTime;


/**
 * <p>문자열 핸들링 - trim, replace, 숫자포맷으로 콤마붙이기 등...
 * <p>숫자 핸들링 - 반올림,버림,반내림 등...
 *
 * @author 김대호
 */
public class Util {
	
	/*
	private static CommonBCFactory factory;
    private Logger log = Logger.getLogger(this.getClass().getName());
    // BC factory에서 BC를 얻는데 사용될 argument를 static 문자열로 선언
    private final static String CNST_BC = "com.cbscap.bc.c.sc.cn.CnstBC";*/
    
	private static Logger log = Logger.getLogger(Util.class.getName());

	private static final String SEP 	= System.getProperty("file.separator"); //window : "\", unix : "/"
	public static String BATCH_CONFIG_ONLINE = "/app/cabis/WEB-INF/classes" + SEP + "config" + SEP + "batch.config";
	public static String BATCH_CONFIG_LOCAL  = "D:/cabis/workspace/CABIS/WebContent/WEB-INF/classes" + SEP + "config" + SEP + "batch.config";
	public static String OS 			= System.getProperty("os.name").toLowerCase();
	
    /**
     * 입력문자열이 숫자로 변환가능한지
     * @param str 확인할문자열
     * @return 숫자여부
     */
    public static boolean isDigit(String str) {
        boolean isDigit = false;

        try {
            if (str != null) {
                Integer.parseInt(str.trim());
                isDigit = true;
            }
        } catch (NumberFormatException e) {
            isDigit = false;
        }

        return isDigit;
    }

    /**
     * <p>주어진 format의 길이에 맞춰 특정문자를 문자열의 왼쪽에 attach
     *
     * <pre><code>
     * format( "123", 4, '*' ) return "*123"
     * </code></pre>
     *
     * @param str 기준문자열
     * @param length 총길이
     * @param ch 채울문자
     * @return 포맷된문자열
     */
    public static String format(String str, int length, char ch) {
        if (str == null) {
            str = "";
        }

        if (str.length() > length) { // 처리하려는 문자열이 기준길이보다 길 경우

            //TODO 확인후 추가
        }

        StringBuffer buff = new StringBuffer(str);

        for (int i = buff.length(); i < length; i++) {
            buff.insert(0, ch);
        }

        return buff.toString();
    }

    /**
     * <p>주어진 format의 길이에 맞춰 특정문자를 문자열의 왼쪽에 attach
     * <pre><code>
     * format( 123, 4, '*' ) return "*123"
     * </code></pre>
     *
     * @param num 기준값
     * @param length 총길이
     * @param ch 채울문자
     * @return 포맷된문자열
     */
    public static String format(int num, int length, char ch) {
        return format(String.valueOf(num), length, ch);
    }

    /**
     * <p>주어진 format의 길이에 맞춰 특정문자를 문자열의 지정한 쪽에 attach
     *
     * <pre><code>
     * format( "123", 4, '*' , false) return "*123"
     * format( "123", 4, '*' , true) return "123*"
     * </code></pre>
     *
     * @param str format에 맞출 문자열
     * @param length format의 길이
     * @param ch 채울문자
     * @param fillRightYn 오른쪽에 채울것인지의 여부, true 오른쪽, false 왼쪽
     * @return format에 맞춰 변경된 문자열
     */
    public static String format(String str, int length, char ch,
        boolean fillRightYn) {
        String rsltStr = null;

        StringBuffer buff = new StringBuffer(str);

        if (str == null) {
            str = "";
        }

        if (str.length() > length) { // 처리하려는 문자열이 기준길이보다 길 경우

            // 문자열이 기준보다 길면 그대로 리턴
        }

        if (!fillRightYn) {
            rsltStr = format(str, length, ch);
        } else {
            for (int i = buff.length(); i < length; i++) {
                buff.append(ch);
            }

            rsltStr = buff.toString();
        }

        return rsltStr;
    }

    /**
     * <p>주어진 format의 길이에 맞춰 특정문자를 문자열의 지정한 쪽에 attach
     *
     * <pre><code>
     * format( 123, 4, '*' , false) return "*123"
     * format( 123, 4, '*' , true) return "123*"
     * </code></pre>
     *
     * @param num format에 맞출 숫자
     * @param length format의 길이
     * @param ch 채울문자
     * @param fillRightYn 오른쪽에 채울것인지의 여부, true 오른쪽, false 왼쪽
     * @return format에 맞춰 변경된 문자열
     */
    public static String format(int num, int length, char ch,
        boolean fillRightYn) {
        return format(String.valueOf(num), length, ch, fillRightYn);
    }

    /**
     * <p>공백제거
     * <pre><code>
     * trim( "abc ") return "abc"
     * trim( null ) return null
     * </code></pre>
     *
     * @param data 공백제거할 문자열
     * @return 공백제거된 문자열
     */
    public static String trim(String data) {
        String result = null;

        if (data != null) {
            result = data.trim();
        }

        return result;
    }

    /**
     * <p>공백제거
     * <pre><code>
     * trimToStr( "abc ") return "abc"
     * trimToStr( null ) return ""
     * </code></pre>
     *
     * @param data 공백제거할 문자열
     * @return 공백제거된 문자열
     */
    public static String trimToStr(String data) {
        String result = " ";

        if (data != null  ) {
        	if (" ".equals(data) || "".equals(data)) {
			   return result;
			}
            result = data.trim();
        }

        return result;
    }

    /**
     * <p>메소드명                : replaceStr</p>
     * <p>설명                : 주어진 value(String)의 특정문자열을 대상문자열로 변환</p>
     * <p>메소드인수1        : String value - 주어진 문자열</p>
     * <p>메소드인수2        : String oldStr - 변환하고자 하는 특정 문자열</p>
     * <p>메소드인수3        : String newStr - 변환하고자 하는 대상 문자열</p>
     * <p>메소드리턴값        : String - 특정문자열이 변환된 값 리턴</p>
     */
    /**
     * <p>특정문자열을 대상문자열로 변환
     *
     * <pre><code>
     * replaceStr( "ab111c", "111", "*") return "ab***c"
     * replaceStr( null ) throw NullPointExcpeiton
     * </code></pre>
     *
     * @fixme 널처리추가
     *
     * @param value 원문자열
     * @param oldStr 변환하고자 하는 특정문자열
     * @param newStr 변환하고자 하는 대상문자열
     * @return 특정문자열이 변환된 값
     */
    public static String replaceStr(String value, String oldStr, String newStr) {
        int idx = 0;
        int curIdx = 0;
        StringBuffer result = new StringBuffer();

        curIdx = value.indexOf(oldStr, idx);

        while (curIdx >= 0) {
            // Replace string and append string.
            result.append(value.substring(idx, curIdx));
            result.append(newStr);

            // Increment search the string index.
            idx = curIdx + oldStr.length();
            curIdx = value.indexOf(oldStr, idx); // Add this line, this is the fixed point.
        }

        // After replace all of the oldStr, then if the string remains...
        // append it to result string.
        if (idx <= value.length()) {
            result.append(value.substring(idx, value.length())); // this..
        }

        return result.toString();
    }

    /**
     * 테스트를 위한 임시메소드
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("getComma : " + Util.getComma("-234567890.01"));

        System.out.println("getEmpNoCheckDgt : " + Util.getEmpNoCheckDgt("1111"));
        System.out.println("getEmpNoCheckDgt : " + Util.getEmpNoCheckDgt("456789"));
        System.out.println("getEmpNoCheckDgt : " + Util.getEmpNoCheckDgt("AAAAAA"));

        System.out.println("dgtFormat : " + Util.dgtFormat("1234.173", 1, 1));
        System.out.println("dgtFormat : " + Util.dgtFormat("1234.173", 1, 2));
        System.out.println("dgtFormat : " + Util.dgtFormat("1234.999", 1, 3));
        System.out.println("dgtFormat : " + Util.dgtFormat("1234.173", 1, 4));

    }

    /**
     * <p>문자열에 금액단위 콤마추가
     *
     * <pre><code>
     * getComma( "1234") return "1,234"
     * getComma( null ) return null
     * getComma( "" ) return ""
     * getComma( "1,234") return "1,234"
     * </code></pre>
     *
     * @param str 숫자값 문자열
     * @return 콤마붙인 문자열
     */
    public static String getComma(String str) {
        String sj = str;

        if (str == null) {
            return null;
        }

        if (str.trim().equals("")) {
            return "";
        }

        if (str.indexOf(",") != -1) {
            return str;
        }

        if (sj.startsWith("0")) {
            for (int i = 0; i < str.length(); i++) {
                sj = str.substring(i);

                if (!sj.startsWith("0")) {
                    break;
                }
            }
        }

        if (str.equals("0")) {
            return "0";
        } else {
            double nu = Double.parseDouble(sj);
            NumberFormat nf = NumberFormat.getInstance();
            String no = nf.format(nu);

            return no;
        }
    }

    /**
     * <p>yyyyMMdd, yyyyMM -> yyyy-MM-dd, yyyy-MM 변환
     *
     * <pre>
     * <code>
     * "20070101" return "2007-01-01"
     * "200701" return "2007-01"
     * "2007-01-01" return "2007-01-01"
     * null return null
     * </code>
     * </pre>
     *
     * @param dateStr yyyyMMdd, yyyyMM포맷 엄수
     * @return yyyy-MM-dd, yyyy-MM 변환값, 포맷이 틀리면 입력값그대로 리턴
     */
    public static String convertDateStr(String dateStr) {
       
    	if ((dateStr == null) ||
                ((dateStr.length() != 6) && (dateStr.length() != 8))) {
            return dateStr;
        }

        String year = dateStr.substring(0, 4);
        String month = dateStr.substring(4, 6);

        if (dateStr.length() == 6) {
            return year + "-" + month;
        }

        String day = dateStr.substring(6);

        return year + "-" + month + "-" + day;
    }

    /**
     * <p>사원번호 checkDigit
     * <p>6자리 숫자값문자열 아닌 모든케이스는 null리턴
     * <p>null입력시 NullPointException
     *
     * @todo null처리
     * @param empNo 6자리 숫자값문자열
     * @return
     */
    public static String getEmpNoCheckDgt(String empNo) {
        if (empNo.length() != 6) {
            return null;
        }

        try {
            double sum = 0;

            for (int i = 0; i < 6; i++) {
                sum += (Integer.parseInt(empNo.substring(i, i + 1)) * (i + 1));
            }

            String stringvalue = String.valueOf(sum / 11);

            return stringvalue.substring(stringvalue.indexOf(".") + 1,
                stringvalue.indexOf(".") + 2);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    /**
     * <p>사원번호 유효성 검증 확인
     * <p>7자리숫자값 문자열 아닌경우 false
     * <p>null입력시 NullPointException
     *
     * @todo null처리
     * @param empNo 확인할사원번호(7자리)
     * @return true유효한사원번호, false 잘못된사원번호
     */
    public static boolean isEmpNo(String empNo) {
        if (empNo.length() != 7) {
            return false;
        }

        try {
            double sum = 0;

            for (int i = 0; i < 6; i++) {
                sum += (Integer.parseInt(empNo.substring(i, i + 1)) * (i + 1));
            }

            String stringvalue = String.valueOf(sum / 11);
            String digit = stringvalue.substring(stringvalue.indexOf(".") + 1,
                    stringvalue.indexOf(".") + 2);

            if (digit.equals(empNo.substring(6, 7))) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * <p>숫자형 문자값을 기준 소숫점 처리후 리턴
     *
     * @see BigDecimal
     * @param sDecimal 숫자값 문자열
     * @param iDgtCnt 기준소수점 자릿수
     * @param format BigDecimal상수값 ex) BigDecimal.ROUND_UP, BigDecimal.ROUND_DOWN
     * @param isNullToZero 값이 null인경우 "0"리턴할지 여부, "Y" -> "0"리턴, 이외 null
     * @return 처리된 결과값
     */
    public static String dgtFormat(String sDecimal, int iDgtCnt, int format, String isNullToZero) {
        String sRslt = null;

        if (sDecimal != null) {
            sDecimal = Util.trim(sDecimal);

            if (sDecimal.length() > 0) {
                BigDecimal bd = new BigDecimal(sDecimal);
                bd = bd.setScale(iDgtCnt, format);
                sRslt = bd.toString();
            }
        }

        if (isNullToZero.equals("Y") && (sRslt == null)) {	// 널이면 0으로 바꾸어야 할 경우
            sRslt = "0";
        }

        return sRslt;
    }

    /**
     * <p>숫자형 문자값을 기준 소숫점 처리후 리턴
     *
     * @see BigDecimal
     * @param sDecimal 숫자값 문자열
     * @param iDgtCnt 기준소수점 자릿수
     * @param format BigDecimal상수값 ex) BigDecimal.ROUND_UP, BigDecimal.ROUND_DOWN
     * @return 처리된 결과값
     */
    public static String dgtFormat(String sDecimal, int iDgtCnt, int format) {
        return dgtFormat(sDecimal, iDgtCnt, format, "Y");
    }

    /**
     * <p>숫자형 문자값을 기준 소숫점 절사처리
     *
     * {@link #dgtFormat(String, int, int)}
     *
     * @param sDecimal 숫자값 문자열
     * @param iDgtCnt 기준소수점 자릿수
     * @return 절사된문자열
     */
    public static String ceil(String sDecimal, int iDgtCnt) {
        return dgtFormat(sDecimal, iDgtCnt, BigDecimal.ROUND_CEILING, "Y");
    }

    /**
     * <p>숫자형 문자값을 기준 소숫점 절사처리
     *
     * {@link #dgtFormat(String, int, int)}
     *
     * @param sDecimal 숫자값 문자열
     * @param iDgtCnt 기준소수점 자릿수
     * @return 절사된문자열
     */
    public static String round(String sDecimal, int iDgtCnt) {
        return dgtFormat(sDecimal, iDgtCnt, BigDecimal.ROUND_HALF_DOWN, "Y");
    }

    /**
     * <p>숫자형 문자값을 기준 소숫점 절사처리
     *
     * {@link #dgtFormat(String, int, int)}
     *
     * @param sDecimal 숫자값 문자열
     * @param iDgtCnt 기준소수점 자릿수
     * @return 절사된문자열
     */
    public static String floor(String sDecimal, int iDgtCnt) {
        return dgtFormat(sDecimal, iDgtCnt, BigDecimal.ROUND_FLOOR, "Y");
    }

    /**
     * <p>공백제거, 인수가 null 이거나 "" 일경우 "0"
     * @param data 공백제거 처리할값
     * @return null문자나 공백일경우 "0" 그외엔 공백제거 후 리턴
     */
    public static String nullToZero(String data) {
        String result = data;
        
        if (data == null) {
            result = "0";
        }else {
            result = data.trim();
            if (result.equals("") || result.equals("NULL")) {
                result = "0";
            }
        }
        return result;
    }

    /**
     * <p>공백제거
     * <p>인수가 ""이면 "NULL" 리턴
     *
     * <pre>
     * <code>
     *
     * nullToString(null) return "NULL"
     * nullToString(" 123 ") return "123"
     *
     * </code>
     * </pre>
     *
     * @param data
     * @return
     */
    public static String nullToString(String data) {
        String result = null;
        if (data != null) {
            result = data.trim();
        }else {
            result = "NULL";
        }
        return result;
    }
    
    /** [CXC] 2012.08.22
	         김재원, 이철희 
	         상수값 조회를 공통으로 사용하기 위해 추가함
     * <p>메소드명                	: cnstStr</p>
     * <p>설명                		: 상수명을 입력받아 조회하여 상수값을 반환</p>
     * <p>메소드인수1        	: String value - 주어진 문자열</p>
     * <p>메소드인수2        	: ITransaction tx - 변환하고자 하는 특정 문자열</p>
     * <p>메소드리턴값        		: String - 특정문자열이 변환된 값 리턴</p>
     * @throws BCException 
     * @throws SQLException 
     */
    public static String cnstStr(String value, Connection con) throws SQLException {
    	Statement stmt = null;
		ResultSet rs = null;
		String cnstCntn = "";
		//맵에 담아서 리스트로 반환한다.
		ResultSetMetaData rd;
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT CNST_CNTN"+"\n");
		sql.append("  FROM CBSCOMM.CT_CNST_INF"+"\n");
		sql.append(" WHERE CNST_NM = '"+value+"'"+"\n");
		sql.append("   "+"\n");
		
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql.toString());
		try{
			rd = rs.getMetaData();
	  		if(rs.next()) {
	  			 cnstCntn = rs.getString(rd.getColumnName(1));
			}
		}catch(SQLException e){
		  e.printStackTrace();
		}
		return cnstCntn;
    }
    
    /**
	 *  @param
	 */
	public static void custAuditTrail(String custNo, String custNm, String cnslNo, String loanNo, String loanSeq, ITransaction tx) {
		log.info("■ 고객정보 조회이력 저장");
		if (RUtil.isNull(custNo) || RUtil.isNull(custNm)) {
			System.out.println("고객정보 Audit Trail Log 생성 필수값(custNo, custNm)이 없습니다.");
			return;
		}
		
		// 세션정보를 받아온다.
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		User user =  (User) session.getAttribute(ICbcFwConstant.CUSER);
		// 프로그램정보를 받는다.
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		
		StCustInfoReadHistTDO tdo = new StCustInfoReadHistTDO();
			tdo.setReadDt(DateTime.getDateString());
			tdo.setReadTm(DateTime.getShortTimeString());
			tdo.setReadEmpNo(user.getUserNo());
			tdo.setReadDeptCd(user.getDeptCd());
			tdo.setReadMenuNo(String.valueOf(request.getAttribute("classId")));
			tdo.setCustNo(custNo);
			tdo.setCustNm(custNm);
			tdo.setCnslNo(cnslNo);
			tdo.setLoanNo(loanNo);
			tdo.setLoanSeq(loanSeq);
			tdo.setFrstRegEmpNo(user.getUserNo());
			tdo.setLastProcEmpNo(user.getUserNo());

		try {
			CustBC bc = new CustBC();
			bc.insertCustAuditTrail(tdo, tx);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  @param
	 */
	public static void custAuditTrailList(String schCond, ITransaction tx) {
		log.info("■ 고객정보 조회이력 저장(List)");
		// 세션정보를 받아온다.
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		User user =  (User) session.getAttribute(ICbcFwConstant.CUSER);
		// 프로그램정보를 받는다.
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		StCustInfoReadHistTDO tdo = new StCustInfoReadHistTDO();
			tdo.setReadDt(DateTime.getDateString());
			tdo.setReadTm(DateTime.getShortTimeString());
			tdo.setReadEmpNo(user.getUserNo());
			tdo.setReadDeptCd(user.getDeptCd());
			tdo.setReadMenuNo(String.valueOf(request.getAttribute("classId")));
			tdo.setCustNo("");
			tdo.setCustNm(schCond);
			tdo.setCnslNo("");
			tdo.setLoanNo("");
			tdo.setLoanSeq("");
			tdo.setFrstRegEmpNo(user.getUserNo());
			tdo.setLastProcEmpNo(user.getUserNo());

		try {
			CustBC bc = new CustBC();
			bc.insertCustAuditTrail(tdo, tx);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 메소드명		: insertCustInfoSearchHist
	 * 설명			: 대출번호로 고객정보 조회이력 저장 (단건조회)
	 * 메소드인수1 	: String loanNo
	 * 메소드인수2 	: String loanSeq
	 * 메소드인수3 	: ITransaction
	 * 메소드리턴값	: N/A
	 * 예외처리		: Exception, BCException
	 */
	public static void insertCustInfoSearchHist(String loanNo, String loanSeq, ITransaction tx)
	throws Exception, BCException {
		
		// 대출번호로 고객번호, 고객명 조회
		IBizBC bc = new CommonBCFactory().getBc("com.cbscap.bc.s.cs.cs.CustBC");
		StCustBaseTDO custInfo = ((ICustBC) bc).searchCustByLoanNo(new String[]{loanNo, loanSeq}, tx);
		
		// 고객정보 조회이력 저장
		custAuditTrail(custInfo.getCustNo(), custInfo.getCustNm(), "", loanNo, loanSeq, tx);
	}
	
	/**
	 * 메소드명		: insertCustInfoSearchHistList
	 * 설명			: 대출번호로 고객정보 조회이력 저장(리스트조회)
	 * 메소드인수1 	: String schCond
	 * 메소드인수2 	: ITransaction
	 * 메소드리턴값	: N/A
	 * 예외처리		: Exception, BCException
	 */
	public static void insertCustInfoSearchHistList(String schCond, ITransaction tx)
	throws Exception, BCException {
		
		// 고객정보 조회이력 저장
		custAuditTrailList(schCond, tx);
	}
	
	/**
	 * 메소드명		:
	 * 설명			: 주민번호 대체키 생성
	 * 메소드인수1 	: String divCd : S:대체키로주민번호조회, I:주민번호암호화삽입
	 * 메소드인수2 	: String paramStr : S일때 대체키, I일때 주민번호
	 * 메소드인수3 	: String empNo : EMP_NO
	 * 메소드인수4 	: Connection con
	 * 메소드리턴값	: result[0]=코드 (0:성공, -1:에러)
	 * 메소드리턴값	: result[1]=코드메시지
	 * 메소드리턴값	: result[2]=REPLACE_KEY (대체키)
	 * 메소드리턴값	: result[3]=RESI_NO (주민번호)
	 * 메소드리턴값	: result[4]=BRTH_DT (생년월일 YYYYMMDD)
	 * 메소드리턴값	: result[5]=SEX (성별 남:1 여:2)
	 * 예외처리		: Exception, BCException
	 */
//	public static String[] EncDecResiNo(String divCd, String paramStr, String empNo, Connection con)
//		throws Exception, BCException {
//		
//		String[] result = new String[6];
//		ResultSet rs1 = null;
//		PreparedStatement pstmt1 = null;
//		PreparedStatement pstmt2 = null;
//		
//		if(divCd.equals("I")){
//			if (RUtil.isNull(paramStr)) {
//				throw new Exception("주민번호가 없습니다.");
//			}
//			if( paramStr.trim().length()<9 ){
//				throw new Exception("주민번호를 확인해주세요.");
//			}
//
//			try {
//				// 1. 주민등록번호로 검색
//				String sqlStmt1 =
//					" SELECT REPLACE_KEY,RESI_NO,BRTH_DT,SEX  \n"+
//					" FROM CBSCOMM.CT_RESI_NO_REPLACE         \n"+
//					" WHERE RESI_NO=?                         \n";
//				
//				pstmt1 = con.prepareStatement(sqlStmt1);
//				pstmt1.setString(1 , AES256Util.Encode(paramStr));
//				rs1 = pstmt1.executeQuery();
//
//				// 2-1. 존재하면 리턴 세팅
//				if(rs1.next()){
//					result[0] = "0";
//					result[1] = "성공(존재함)";
//					result[2] = rs1.getString("REPLACE_KEY");
//					result[3] = AES256Util.Decode(rs1.getString("RESI_NO"));
//					result[4] = rs1.getString("BRTH_DT");
//					result[5] = rs1.getString("SEX");
//				}
//				// 2-2. 존재하지 않으면 저장
//				else{
//					CallableStatement cstmt = null;
//
//					cstmt = con.prepareCall("{CALL CBSCOMM.CP_GVNO_01(?,?,?,?,?,?,?,?,?,?)}");
//
//					cstmt.setString(1, "SCUS");
//					cstmt.setString(2, ServerSysDate.getServerDate(ServerSysDate.YYMMDD));
//					cstmt.setString(3, "");
//					cstmt.setString(4, "");
//					cstmt.setString(5, "");
//					cstmt.setString(6, "");
//					cstmt.setString(7, "");
//					cstmt.setString(8, "");
//
//					cstmt.registerOutParameter(9, java.sql.Types.VARCHAR);
//					cstmt.registerOutParameter(10, java.sql.Types.VARCHAR);
//
//					cstmt.executeUpdate();
//					String tempNo = cstmt.getString(9);
//					ConnMng.closeStatement(cstmt);
//					log.info("대체키 : " + tempNo);
//					
//					String sqlStmt2 =
//						" INSERT INTO CBSCOMM.CT_RESI_NO_REPLACE ( REPLACE_KEY,RESI_NO,BRTH_DT,SEX,FRST_REG_DT,FRST_REG_TM,FRST_REG_EMP_NO,LAST_PROC_DT,LAST_PROC_TM,LAST_PROC_EMP_NO ) \n"+
//						" VALUES (?,?,?,?,?,?,?,?,?,?) \n";
//					
//					pstmt2 = con.prepareStatement(sqlStmt2);
//					pstmt2.setString(1 , tempNo);
//					pstmt2.setString(2 , AES256Util.Encode(paramStr));
//					pstmt2.setString(3 , paramStr.substring(0, 6));
//					pstmt2.setString(4 , paramStr.substring(6, 7));
//					pstmt2.setString(5 , ServerSysDate.getServerDate(ServerSysDate.YYYYMMDD_WITH_DELIMETER));
//					pstmt2.setString(6 , ServerSysDate.getServerDate(ServerSysDate.YYYYMMDDHHMMSS).substring(8, 14));
//					pstmt2.setString(7 , ( RUtil.isNull(empNo) == true ? " " : empNo ));
//					pstmt2.setString(8 , ServerSysDate.getServerDate(ServerSysDate.YYYYMMDD_WITH_DELIMETER));
//					pstmt2.setString(9 , ServerSysDate.getServerDate(ServerSysDate.YYYYMMDDHHMMSS).substring(8, 14));
//					pstmt2.setString(7 , ( RUtil.isNull(empNo) == true ? " " : empNo ));
//					pstmt2.executeUpdate();
//					
//					result[0] = "0";
//					result[1] = "성공(생성)";
//					result[2] = tempNo;
//					result[3] = paramStr;
//					result[4] = paramStr.substring(0, 6);
//					result[5] = paramStr.substring(6, 7);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				ConnMng.closeResultSet(rs1);
//				ConnMng.closeStatement(pstmt1);
//				ConnMng.closeStatement(pstmt2);
//			}
//		}else if(divCd.equals("S")){
//			try {
//				// 1. 대체키 검색
//				String sqlStmt1 =
//					" SELECT REPLACE_KEY,RESI_NO,BRTH_DT,SEX  \n"+
//					" FROM CBSCOMM.CT_RESI_NO_REPLACE         \n"+
//					" WHERE REPLACE_KEY=?                     \n";
//				
//				pstmt1 = con.prepareStatement(sqlStmt1);
//				pstmt1.setString(1 , paramStr);
//				rs1 = pstmt1.executeQuery();
//
//				// 2-1. 존재하면 조회
//				if(rs1.next()){
//					result[0] = "0";
//					result[1] = "성공";
//					result[2] = rs1.getString("REPLACE_KEY");
//					result[3] = AES256Util.Decode(rs1.getString("RESI_NO"));
//					result[4] = rs1.getString("BRTH_DT");
//					result[5] = rs1.getString("SEX");
//				}
//				// 2-2. 존재하지 않으면 에러코드
//				else{
//					result[0] = "-1";
//					result[1] = "주민번호가 존재하지 않습니다.";
//				}
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			} finally {
//				ConnMng.closeResultSet(rs1);
//				ConnMng.closeStatement(pstmt1);
//				ConnMng.closeStatement(pstmt2);
//			}
//		}else{
//			result[0] = "-1";
//			result[1] = "구분값을 확인해주세요.";
//			return result;
//		}
//		
//		// 3. 리턴
//		return result;
//	}
	
	/**
	 * 메소드명		:
	 * 설명			: 주민번호 대체키 생성
	 * 메소드인수1 	: String divCd : S:대체키로주민번호조회, I:주민번호암호화삽입
	 * 메소드인수2 	: String paramStr : S일때 대체키, I일때 주민번호
	 * 메소드인수3 	: String empNo : EMP_NO
	 * 메소드인수4 	: Connection con
	 * 메소드리턴값	: result[0]=코드 (0:성공, -1:에러)
	 * 메소드리턴값	: result[1]=코드메시지
	 * 메소드리턴값	: result[2]=REPLACE_KEY (대체키)
	 * 메소드리턴값	: result[3]=RESI_NO (주민번호)
	 * 메소드리턴값	: result[4]=BRTH_DT (생년월일 YYYYMMDD)
	 * 메소드리턴값	: result[5]=SEX (성별 남:1 여:2)
	 * 예외처리		: Exception, BCException
	 */
//	public static String EncDecResiNoString(String divCd, String paramStr, String empNo, Connection con)
//		throws Exception, BCException {
//		
//		String[] result = new String[6];
//		ResultSet rs1 = null;
//		PreparedStatement pstmt1 = null;
//		PreparedStatement pstmt2 = null;
//		String rslt = "";
//		
//		if(divCd.equals("I")){
//			if (RUtil.isNull(paramStr)) {
//				throw new Exception("주민번호가 없습니다.");
//			}
//			if( paramStr.trim().length()<9 ){
//				throw new Exception("주민번호를 확인해주세요.");
//			}
//
//			try {
//				// 1. 주민등록번호로 검색
//				String sqlStmt1 =
//					" SELECT REPLACE_KEY,RESI_NO,BRTH_DT,SEX  \n"+
//					" FROM CBSCOMM.CT_RESI_NO_REPLACE         \n"+
//					" WHERE RESI_NO=?                         \n";
//				
//				pstmt1 = con.prepareStatement(sqlStmt1);
//				pstmt1.setString(1 , AES256Util.Encode(paramStr));
//				rs1 = pstmt1.executeQuery();
//
//				// 2-1. 존재하면 리턴 세팅
//				if(rs1.next()){
//					result[0] = "0";
//					result[1] = "성공(존재함)";
//					result[2] = rs1.getString("REPLACE_KEY");
//					result[3] = AES256Util.Decode(rs1.getString("RESI_NO"));
//					result[4] = rs1.getString("BRTH_DT");
//					result[5] = rs1.getString("SEX");
//				}
//				// 2-2. 존재하지 않으면 저장
//				else{
//					CallableStatement cstmt = null;
//
//					cstmt = con.prepareCall("{CALL CBSCOMM.CP_GVNO_01(?,?,?,?,?,?,?,?,?,?)}");
//
//					cstmt.setString(1, "SCUS");
//					cstmt.setString(2, ServerSysDate.getServerDate(ServerSysDate.YYMMDD));
//					cstmt.setString(3, "");
//					cstmt.setString(4, "");
//					cstmt.setString(5, "");
//					cstmt.setString(6, "");
//					cstmt.setString(7, "");
//					cstmt.setString(8, "");
//
//					cstmt.registerOutParameter(9, java.sql.Types.VARCHAR);
//					cstmt.registerOutParameter(10, java.sql.Types.VARCHAR);
//
//					cstmt.executeUpdate();
//					String tempNo = cstmt.getString(9);
//					ConnMng.closeStatement(cstmt);
//					log.info("대체키 : " + tempNo);
//					
//					String sqlStmt2 =
//						" INSERT INTO CBSCOMM.CT_RESI_NO_REPLACE ( REPLACE_KEY,RESI_NO,BRTH_DT,SEX,FRST_REG_DT,FRST_REG_TM,FRST_REG_EMP_NO,LAST_PROC_DT,LAST_PROC_TM,LAST_PROC_EMP_NO ) \n"+
//						" VALUES (?,?,?,?,?,?,?,?,?,?) \n";
//					
//					pstmt2 = con.prepareStatement(sqlStmt2);
//					pstmt2.setString(1 , tempNo);
//					pstmt2.setString(2 , AES256Util.Encode(paramStr));
//					pstmt2.setString(3 , paramStr.substring(0, 6));
//					pstmt2.setString(4 , paramStr.substring(6, 7));
//					pstmt2.setString(5 , ServerSysDate.getServerDate(ServerSysDate.YYYYMMDD_WITH_DELIMETER));
//					pstmt2.setString(6 , ServerSysDate.getServerDate(ServerSysDate.YYYYMMDDHHMMSS).substring(8, 14));
//					pstmt2.setString(7 , ( RUtil.isNull(empNo) == true ? " " : empNo ));
//					pstmt2.setString(8 , ServerSysDate.getServerDate(ServerSysDate.YYYYMMDD_WITH_DELIMETER));
//					pstmt2.setString(9 , ServerSysDate.getServerDate(ServerSysDate.YYYYMMDDHHMMSS).substring(8, 14));
//					pstmt2.setString(7 , ( RUtil.isNull(empNo) == true ? " " : empNo ));
//					pstmt2.executeUpdate();
//					
//					result[0] = "0";
//					result[1] = "성공(생성)";
//					result[2] = tempNo;
//					result[3] = paramStr;
//					result[4] = paramStr.substring(0, 6);
//					result[5] = paramStr.substring(6, 7);
//				}
//			} catch (Exception e) {
//				throw e;
//			} finally {
//				ConnMng.closeResultSet(rs1);
//				ConnMng.closeStatement(pstmt1);
//				ConnMng.closeStatement(pstmt2);
//			}
//		}else if(divCd.equals("S")){
//			try {
//				// 1. 대체키 검색
//				String sqlStmt1 =
//					" SELECT REPLACE_KEY,RESI_NO,BRTH_DT,SEX  \n"+
//					" FROM CBSCOMM.CT_RESI_NO_REPLACE         \n"+
//					" WHERE REPLACE_KEY=?                     \n";
//				
//				pstmt1 = con.prepareStatement(sqlStmt1);
//				pstmt1.setString(1 , paramStr);
//				rs1 = pstmt1.executeQuery();
//
//				// 2-1. 존재하면 조회
//				if(rs1.next()){
//					result[0] = "0";
//					result[1] = "성공";
//					result[2] = rs1.getString("REPLACE_KEY");
//					result[3] = AES256Util.Decode(rs1.getString("RESI_NO"));
//					result[4] = rs1.getString("BRTH_DT");
//					result[5] = rs1.getString("SEX");
//				}
//				// 2-2. 존재하지 않으면 에러코드
//				else{
//					result[3] = "";
//				}
//			} catch (Exception e) {
//				// TODO: handle exception
//				throw e;
//			} finally {
//				ConnMng.closeResultSet(rs1);
//				ConnMng.closeStatement(pstmt1);
//				ConnMng.closeStatement(pstmt2);
//			}
//		}else{
//			result[0] = "-1";
//			result[1] = "구분값을 확인해주세요.";
//			return paramStr;
//		}
//		
//		// 3. 리턴
//		if(divCd.equals("I")){
//			rslt = result[2];	
//		}else if(divCd.equals("S")){
//			rslt = result[3];
//		}
//		return rslt;
//	}
	
	
	
	
	/**
	 * 메소드명		:
	 * 설명			: 주민번호 대체키 생성
	 * 메소드인수1 	: String divCd : S:대체키로주민번호조회, I:주민번호암호화삽입
	 * 메소드인수2 	: String paramStr : S일때 대체키, I일때 주민번호
	 * 메소드리턴값	: result[0]=코드 (0:성공, -1:에러)
	 * 메소드리턴값	: result[1]=코드메시지
	 * 메소드리턴값	: result[2]=REPLACE_KEY (대체키)
	 * 메소드리턴값	: result[3]=RESI_NO (주민번호)
	 * 메소드리턴값	: result[4]=BRTH_DT (생년월일 YYYYMMDD)
	 * 메소드리턴값	: result[5]=SEX (성별 남:1 여:2)
	 * 예외처리		: Exception, BCException
	 */
	public static String[] EncDecResiNo(String divCd, String paramStr)
		throws Exception, BCException {
		
		String[] result = new String[6];
		ResultSet rs1 = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		
		ConnMng conMng = ( OS.indexOf("win") >= 0 ) ? new ConnMng(BATCH_CONFIG_LOCAL)
													: new ConnMng(BATCH_CONFIG_ONLINE);
		if(divCd.equals("I")){
			if (RUtil.isNull(paramStr)) {
//				throw new Exception("주민번호가 없습니다.");
				result[0] = "0";
				result[1] = "빈값";
				result[2] = paramStr;
				result[3] = paramStr;
				return result;
			}
			if( paramStr.trim().length()<6 ){
				throw new Exception("주민번호를 확인해주세요.");
			}
			Connection con = null;
			try {
				con = conMng.getConnection();
				// 1. 주민등록번호로 검색
				String sqlStmt1 =
					" SELECT REPLACE_KEY,RESI_NO,BRTH_DT,SEX  \n"+
					" FROM CBSCOMM.CT_RESI_NO_REPLACE         \n"+
					" WHERE RESI_NO=?                         \n";
				
				pstmt1 = con.prepareStatement(sqlStmt1);
				pstmt1.setString(1 , AES256Util.Encode(paramStr));
				rs1 = pstmt1.executeQuery();

				// 2-1. 존재하면 리턴 세팅
				if(rs1.next()){
					result[0] = "0";
					result[1] = "성공(존재함)";
					result[2] = rs1.getString("REPLACE_KEY");
					result[3] = AES256Util.Decode(rs1.getString("RESI_NO"));
					result[4] = rs1.getString("BRTH_DT");
					result[5] = rs1.getString("SEX");
				}
				// 2-2. 존재하지 않으면 저장
				else{
					CallableStatement cstmt = null;

					cstmt = con.prepareCall("{CALL CBSCOMM.CP_GVNO_01(?,?,?,?,?,?,?,?,?,?)}");

					cstmt.setString(1, "SCUS");
					cstmt.setString(2, ServerSysDate.getServerDate(ServerSysDate.YYMMDD));
					cstmt.setString(3, "");
					cstmt.setString(4, "");
					cstmt.setString(5, "");
					cstmt.setString(6, "");
					cstmt.setString(7, "");
					cstmt.setString(8, "");

					cstmt.registerOutParameter(9, java.sql.Types.VARCHAR);
					cstmt.registerOutParameter(10, java.sql.Types.VARCHAR);

					cstmt.executeUpdate();
					String tempNo = "X" + cstmt.getString(9);
					ConnMng.closeStatement(cstmt);
					
					String sqlStmt2 =
						" INSERT INTO CBSCOMM.CT_RESI_NO_REPLACE ( REPLACE_KEY,RESI_NO,BRTH_DT,SEX,FRST_REG_DT,FRST_REG_TM,FRST_REG_EMP_NO,LAST_PROC_DT,LAST_PROC_TM,LAST_PROC_EMP_NO,RESI_NO_6,RESI_NO_7,RESI_NO_8,RESI_NO_9,RESI_NO_10,RESI_NO_11,RESI_NO_12 ) \n"+
						" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) \n";
					
					pstmt2 = con.prepareStatement(sqlStmt2);
					pstmt2.setString(1 , tempNo);
					pstmt2.setString(2 , AES256Util.Encode(paramStr));
					pstmt2.setString(3 , paramStr.substring(0, 6));
					
					if(paramStr.length() > 6){ // 주민번호 7자리 이상
						pstmt2.setString(4 , paramStr.substring(6, 7));	
					}else{ // 주민번호 6자리
						pstmt2.setString(4 , " ");
					}
					
					pstmt2.setString(5 , ServerSysDate.getServerDate(ServerSysDate.YYYYMMDD_WITH_DELIMETER));
					pstmt2.setString(6 , ServerSysDate.getServerDate(ServerSysDate.YYYYMMDDHHMMSS).substring(8, 14));
					pstmt2.setString(7 , " ");
					pstmt2.setString(8 , ServerSysDate.getServerDate(ServerSysDate.YYYYMMDD_WITH_DELIMETER));
					pstmt2.setString(9 , ServerSysDate.getServerDate(ServerSysDate.YYYYMMDDHHMMSS).substring(8, 14));
					pstmt2.setString(10, " ");
					setParam(pstmt2, paramStr, paramStr.length());
					pstmt2.executeUpdate();
					
					result[0] = "0";
					result[1] = "성공(생성)";
					result[2] = tempNo;
					result[3] = paramStr;
					result[4] = paramStr.substring(0, 6);
					
					if(paramStr.length() > 6){ // 주민번호 7자리 이상
						result[5] = paramStr.substring(6, 7);	
					}else{ // 주민번호 6자리
						result[5] = " ";
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnMng.closeConnection(con);
				ConnMng.closeResultSet(rs1);
				ConnMng.closeStatement(pstmt1);
				ConnMng.closeStatement(pstmt2);
			}
		}else if(divCd.equals("S")){
			if (RUtil.isNull(paramStr)) {
				result[0] = "0";
				result[1] = "성공(생성)";
				result[2] = paramStr;
				result[3] = paramStr;
				return result;
			}
			if( !paramStr.substring(0, 1).equals("X") ){
				result[0] = "0";
				result[1] = "성공(생성)";
				result[2] = paramStr;
				result[3] = paramStr;
				return result;
			}
			Connection con = null;
			try {
				con = conMng.getConnection();
				// 1. 대체키 검색
				String sqlStmt1 =
					" SELECT REPLACE_KEY,RESI_NO,BRTH_DT,SEX  \n"+
					" FROM CBSCOMM.CT_RESI_NO_REPLACE         \n"+
					" WHERE REPLACE_KEY=?                     \n";
				
				pstmt1 = con.prepareStatement(sqlStmt1);
				pstmt1.setString(1 , paramStr);
				rs1 = pstmt1.executeQuery();

				// 2-1. 존재하면 조회
				if(rs1.next()){
					result[0] = "0";
					result[1] = "성공";
					result[2] = rs1.getString("REPLACE_KEY");
					result[3] = AES256Util.Decode(rs1.getString("RESI_NO"));
					result[4] = rs1.getString("BRTH_DT");
					result[5] = rs1.getString("SEX");
				}
				// 2-2. 존재하지 않으면 에러코드
				else{
					result[0] = "-1";
					result[1] = "주민번호가 존재하지 않습니다.";
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				ConnMng.closeConnection(con);
				ConnMng.closeResultSet(rs1);
				ConnMng.closeStatement(pstmt1);
				ConnMng.closeStatement(pstmt2);
			}
		}else{
			result[0] = "-1";
			result[1] = "구분값을 확인해주세요.";
			return result;
		}
		
		// 3. 리턴
		return result;
	}
	
	/**
	 * 메소드명		:
	 * 설명			: 주민번호 대체키 생성
	 * 메소드인수1 	: String divCd : S:대체키로주민번호조회, I:주민번호암호화삽입
	 * 메소드인수2 	: String paramStr : S일때 대체키, I일때 주민번호
	 * 메소드리턴값	: result[0]=코드 (0:성공, -1:에러)
	 * 메소드리턴값	: result[1]=코드메시지
	 * 메소드리턴값	: result[2]=REPLACE_KEY (대체키)
	 * 메소드리턴값	: result[3]=RESI_NO (주민번호)
	 * 메소드리턴값	: result[4]=BRTH_DT (생년월일 YYYYMMDD)
	 * 메소드리턴값	: result[5]=SEX (성별 남:1 여:2)
	 * 예외처리		: Exception, BCException
	 */
	public static String EncDecResiNoString(String divCd, String paramStr)
		throws Exception, BCException {
		
		String[] result = new String[6];
		ResultSet rs1 = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		String rslt = "";
		
		ConnMng conMng = ( OS.indexOf("win") >= 0 ) ? new ConnMng(BATCH_CONFIG_LOCAL)
													: new ConnMng(BATCH_CONFIG_ONLINE);
		
		if(divCd.equals("I")){
			if (RUtil.isNull(paramStr)) {
				return paramStr;
//				throw new Exception("주민번호가 없습니다.");
			}
			if( paramStr.trim().length()<6 ){
				throw new Exception("주민번호를 확인해주세요.");
			}
			Connection con = null;
			try {
				con = conMng.getConnection();
				// 1. 주민등록번호로 검색
				String sqlStmt1 =
					" SELECT REPLACE_KEY,RESI_NO,BRTH_DT,SEX  \n"+
					" FROM CBSCOMM.CT_RESI_NO_REPLACE         \n"+
					" WHERE RESI_NO=?                         \n";
				
				pstmt1 = con.prepareStatement(sqlStmt1);
				pstmt1.setString(1 , AES256Util.Encode(paramStr));
				rs1 = pstmt1.executeQuery();

				// 2-1. 존재하면 리턴 세팅
				if(rs1.next()){
					result[0] = "0";
					result[1] = "성공(존재함)";
					result[2] = rs1.getString("REPLACE_KEY");
					result[3] = AES256Util.Decode(rs1.getString("RESI_NO"));
					result[4] = rs1.getString("BRTH_DT");
					result[5] = rs1.getString("SEX");
				}
				// 2-2. 존재하지 않으면 저장
				else{
					CallableStatement cstmt = null;

					cstmt = con.prepareCall("{CALL CBSCOMM.CP_GVNO_01(?,?,?,?,?,?,?,?,?,?)}");

					cstmt.setString(1, "SCUS");
					cstmt.setString(2, ServerSysDate.getServerDate(ServerSysDate.YYMMDD));
					cstmt.setString(3, "");
					cstmt.setString(4, "");
					cstmt.setString(5, "");
					cstmt.setString(6, "");
					cstmt.setString(7, "");
					cstmt.setString(8, "");

					cstmt.registerOutParameter(9, java.sql.Types.VARCHAR);
					cstmt.registerOutParameter(10, java.sql.Types.VARCHAR);

					cstmt.executeUpdate();
					String tempNo = "X" + cstmt.getString(9);
					ConnMng.closeStatement(cstmt);
					
					String sqlStmt2 =
						" INSERT INTO CBSCOMM.CT_RESI_NO_REPLACE ( REPLACE_KEY,RESI_NO,BRTH_DT,SEX,FRST_REG_DT,FRST_REG_TM,FRST_REG_EMP_NO,LAST_PROC_DT,LAST_PROC_TM,LAST_PROC_EMP_NO,RESI_NO_6,RESI_NO_7,RESI_NO_8,RESI_NO_9,RESI_NO_10,RESI_NO_11,RESI_NO_12 ) \n"+
							" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) \n";
					
					pstmt2 = con.prepareStatement(sqlStmt2);
					pstmt2.setString(1 , tempNo);
					pstmt2.setString(2 , AES256Util.Encode(paramStr));
					pstmt2.setString(3 , paramStr.substring(0, 6));

					if(paramStr.length() > 6){ // 주민번호 7자리 이상
						pstmt2.setString(4 , paramStr.substring(6, 7));	
					}else{ // 주민번호 6자리
						pstmt2.setString(4 , " ");
					}

					pstmt2.setString(5 , ServerSysDate.getServerDate(ServerSysDate.YYYYMMDD_WITH_DELIMETER));
					pstmt2.setString(6 , ServerSysDate.getServerDate(ServerSysDate.YYYYMMDDHHMMSS).substring(8, 14));
					pstmt2.setString(7 , " ");
					pstmt2.setString(8 , ServerSysDate.getServerDate(ServerSysDate.YYYYMMDD_WITH_DELIMETER));
					pstmt2.setString(9 , ServerSysDate.getServerDate(ServerSysDate.YYYYMMDDHHMMSS).substring(8, 14));
					pstmt2.setString(10, " ");
					setParam(pstmt2, paramStr, paramStr.length());
					pstmt2.executeUpdate();
					
					result[0] = "0";
					result[1] = "성공(생성)";
					result[2] = tempNo;
					result[3] = paramStr;
					result[4] = paramStr.substring(0, 6);
					
					if(paramStr.length() > 6){ // 주민번호 7자리 이상
						result[5] = paramStr.substring(6, 7);	
					}else{ // 주민번호 6자리
						result[5] = " ";
					}
				}
			} catch (Exception e) {
				throw e;
			} finally {
				ConnMng.closeConnection(con);
				ConnMng.closeResultSet(rs1);
				ConnMng.closeStatement(pstmt1);
				ConnMng.closeStatement(pstmt2);
			}
		}else if(divCd.equals("S")){
			Connection con = null;
			try {
				if (RUtil.isNull(paramStr)) {
					return "";
				}
				if( !paramStr.substring(0, 1).equals("X") ){
					return paramStr;
				}
				con = conMng.getConnection();
				// 1. 대체키 검색
				String sqlStmt1 =
					" SELECT REPLACE_KEY,RESI_NO,BRTH_DT,SEX  \n"+
					" FROM CBSCOMM.CT_RESI_NO_REPLACE         \n"+
					" WHERE REPLACE_KEY=?                     \n";
				
				pstmt1 = con.prepareStatement(sqlStmt1);
				pstmt1.setString(1 , paramStr);
				rs1 = pstmt1.executeQuery();

				// 2-1. 존재하면 조회
				if(rs1.next()){
					result[0] = "0";
					result[1] = "성공";
					result[2] = rs1.getString("REPLACE_KEY");
					result[3] = AES256Util.Decode(rs1.getString("RESI_NO"));
					result[4] = rs1.getString("BRTH_DT");
					result[5] = rs1.getString("SEX");
				}
				// 2-2. 존재하지 않으면 에러코드
				else{
					result[3] = "";
				}
			} catch (Exception e) {
				// TODO: handle exception
				throw e;
			} finally {
				ConnMng.closeConnection(con);
				ConnMng.closeResultSet(rs1);
				ConnMng.closeStatement(pstmt1);
				ConnMng.closeStatement(pstmt2);
			}
		}else{
			result[0] = "-1";
			result[1] = "구분값을 확인해주세요.";
			return paramStr;
		}
		
		// 3. 리턴
		if(divCd.equals("I")){
			rslt = result[2];	
		}else if(divCd.equals("S")){
			rslt = result[3];
		}
		return rslt;
	}
	
	// 여기서부터 아래까지 껍때기 함수
//	public static String[] EncDecResiNo(String divCd, String paramStr)
//			throws Exception, BCException {
//		String[] result = new String[6];
//		
//		if(divCd.equals("I")){
//			if (RUtil.isNull(paramStr)) {
//				throw new Exception("주민번호가 없습니다.");
//			}
//			if( paramStr.trim().length()<9 ){
//				throw new Exception("주민번호를 확인해주세요.");
//			}
//			result[0] = "0";
//			result[1] = "성공(생성)";
//			result[2] = paramStr;
//			result[3] = paramStr;
//			result[4] = paramStr.substring(0, 6);
//			result[5] = paramStr.substring(6, 7);
//		}else if(divCd.equals("S")){
//			result[0] = "0";
//			result[1] = "성공(생성)";
//			result[2] = paramStr;
//			result[3] = paramStr;
//		}
//		return result;
//	}
//	
//	public static String EncDecResiNoString(String divCd, String paramStr)
//			throws Exception, BCException {
//		String result = "";
//		
//		result = paramStr;
//		
//		return result;
//	}
	
	public static void setParam(PreparedStatement pstmt2, String custReisNo, int length)
		throws Exception, BCException {

		try {
			String sqlStmt2 = "";

			for (int i = 6; i <= 13; i++) {
				if (i == 13) {
					break;
				}
				if(i > length){
					pstmt2.setString(i+5, " ");
					continue;
				}
//				sqlStmt2 = " UPDATE CBSCOMM.CT_RESI_NO_REPLACE \n" + " SET RESI_NO_" + Integer.toString(i) + " = ?                   \n" + " WHERE REPLACE_KEY = ?               \n";

				String tempResiNo = custReisNo.substring(0, i);
				
				pstmt2.setString(i+5, AES256Util.Encode(tempResiNo));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new Exception("insertTable 중 에러 발생");
		} finally {
		}
	}
}
