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
 * <p>���ڿ� �ڵ鸵 - trim, replace, ������������ �޸����̱� ��...
 * <p>���� �ڵ鸵 - �ݿø�,����,�ݳ��� ��...
 *
 * @author ���ȣ
 */
public class Util {
	
	/*
	private static CommonBCFactory factory;
    private Logger log = Logger.getLogger(this.getClass().getName());
    // BC factory���� BC�� ��µ� ���� argument�� static ���ڿ��� ����
    private final static String CNST_BC = "com.cbscap.bc.c.sc.cn.CnstBC";*/
    
	private static Logger log = Logger.getLogger(Util.class.getName());

	private static final String SEP 	= System.getProperty("file.separator"); //window : "\", unix : "/"
	public static String BATCH_CONFIG_ONLINE = "/app/cabis/WEB-INF/classes" + SEP + "config" + SEP + "batch.config";
	public static String BATCH_CONFIG_LOCAL  = "D:/cabis/workspace/CABIS/WebContent/WEB-INF/classes" + SEP + "config" + SEP + "batch.config";
	public static String OS 			= System.getProperty("os.name").toLowerCase();
	
    /**
     * �Է¹��ڿ��� ���ڷ� ��ȯ��������
     * @param str Ȯ���ҹ��ڿ�
     * @return ���ڿ���
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
     * <p>�־��� format�� ���̿� ���� Ư�����ڸ� ���ڿ��� ���ʿ� attach
     *
     * <pre><code>
     * format( "123", 4, '*' ) return "*123"
     * </code></pre>
     *
     * @param str ���ع��ڿ�
     * @param length �ѱ���
     * @param ch ä�﹮��
     * @return ���˵ȹ��ڿ�
     */
    public static String format(String str, int length, char ch) {
        if (str == null) {
            str = "";
        }

        if (str.length() > length) { // ó���Ϸ��� ���ڿ��� ���ر��̺��� �� ���

            //TODO Ȯ���� �߰�
        }

        StringBuffer buff = new StringBuffer(str);

        for (int i = buff.length(); i < length; i++) {
            buff.insert(0, ch);
        }

        return buff.toString();
    }

    /**
     * <p>�־��� format�� ���̿� ���� Ư�����ڸ� ���ڿ��� ���ʿ� attach
     * <pre><code>
     * format( 123, 4, '*' ) return "*123"
     * </code></pre>
     *
     * @param num ���ذ�
     * @param length �ѱ���
     * @param ch ä�﹮��
     * @return ���˵ȹ��ڿ�
     */
    public static String format(int num, int length, char ch) {
        return format(String.valueOf(num), length, ch);
    }

    /**
     * <p>�־��� format�� ���̿� ���� Ư�����ڸ� ���ڿ��� ������ �ʿ� attach
     *
     * <pre><code>
     * format( "123", 4, '*' , false) return "*123"
     * format( "123", 4, '*' , true) return "123*"
     * </code></pre>
     *
     * @param str format�� ���� ���ڿ�
     * @param length format�� ����
     * @param ch ä�﹮��
     * @param fillRightYn �����ʿ� ä��������� ����, true ������, false ����
     * @return format�� ���� ����� ���ڿ�
     */
    public static String format(String str, int length, char ch,
        boolean fillRightYn) {
        String rsltStr = null;

        StringBuffer buff = new StringBuffer(str);

        if (str == null) {
            str = "";
        }

        if (str.length() > length) { // ó���Ϸ��� ���ڿ��� ���ر��̺��� �� ���

            // ���ڿ��� ���غ��� ��� �״�� ����
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
     * <p>�־��� format�� ���̿� ���� Ư�����ڸ� ���ڿ��� ������ �ʿ� attach
     *
     * <pre><code>
     * format( 123, 4, '*' , false) return "*123"
     * format( 123, 4, '*' , true) return "123*"
     * </code></pre>
     *
     * @param num format�� ���� ����
     * @param length format�� ����
     * @param ch ä�﹮��
     * @param fillRightYn �����ʿ� ä��������� ����, true ������, false ����
     * @return format�� ���� ����� ���ڿ�
     */
    public static String format(int num, int length, char ch,
        boolean fillRightYn) {
        return format(String.valueOf(num), length, ch, fillRightYn);
    }

    /**
     * <p>��������
     * <pre><code>
     * trim( "abc ") return "abc"
     * trim( null ) return null
     * </code></pre>
     *
     * @param data ���������� ���ڿ�
     * @return �������ŵ� ���ڿ�
     */
    public static String trim(String data) {
        String result = null;

        if (data != null) {
            result = data.trim();
        }

        return result;
    }

    /**
     * <p>��������
     * <pre><code>
     * trimToStr( "abc ") return "abc"
     * trimToStr( null ) return ""
     * </code></pre>
     *
     * @param data ���������� ���ڿ�
     * @return �������ŵ� ���ڿ�
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
     * <p>�޼ҵ��                : replaceStr</p>
     * <p>����                : �־��� value(String)�� Ư�����ڿ��� ����ڿ��� ��ȯ</p>
     * <p>�޼ҵ��μ�1        : String value - �־��� ���ڿ�</p>
     * <p>�޼ҵ��μ�2        : String oldStr - ��ȯ�ϰ��� �ϴ� Ư�� ���ڿ�</p>
     * <p>�޼ҵ��μ�3        : String newStr - ��ȯ�ϰ��� �ϴ� ��� ���ڿ�</p>
     * <p>�޼ҵ帮�ϰ�        : String - Ư�����ڿ��� ��ȯ�� �� ����</p>
     */
    /**
     * <p>Ư�����ڿ��� ����ڿ��� ��ȯ
     *
     * <pre><code>
     * replaceStr( "ab111c", "111", "*") return "ab***c"
     * replaceStr( null ) throw NullPointExcpeiton
     * </code></pre>
     *
     * @fixme ��ó���߰�
     *
     * @param value �����ڿ�
     * @param oldStr ��ȯ�ϰ��� �ϴ� Ư�����ڿ�
     * @param newStr ��ȯ�ϰ��� �ϴ� ����ڿ�
     * @return Ư�����ڿ��� ��ȯ�� ��
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
     * �׽�Ʈ�� ���� �ӽø޼ҵ�
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
     * <p>���ڿ��� �ݾ״��� �޸��߰�
     *
     * <pre><code>
     * getComma( "1234") return "1,234"
     * getComma( null ) return null
     * getComma( "" ) return ""
     * getComma( "1,234") return "1,234"
     * </code></pre>
     *
     * @param str ���ڰ� ���ڿ�
     * @return �޸����� ���ڿ�
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
     * <p>yyyyMMdd, yyyyMM -> yyyy-MM-dd, yyyy-MM ��ȯ
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
     * @param dateStr yyyyMMdd, yyyyMM���� ����
     * @return yyyy-MM-dd, yyyy-MM ��ȯ��, ������ Ʋ���� �Է°��״�� ����
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
     * <p>�����ȣ checkDigit
     * <p>6�ڸ� ���ڰ����ڿ� �ƴ� ������̽��� null����
     * <p>null�Է½� NullPointException
     *
     * @todo nulló��
     * @param empNo 6�ڸ� ���ڰ����ڿ�
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
     * <p>�����ȣ ��ȿ�� ���� Ȯ��
     * <p>7�ڸ����ڰ� ���ڿ� �ƴѰ�� false
     * <p>null�Է½� NullPointException
     *
     * @todo nulló��
     * @param empNo Ȯ���һ����ȣ(7�ڸ�)
     * @return true��ȿ�ѻ����ȣ, false �߸��Ȼ����ȣ
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
     * <p>������ ���ڰ��� ���� �Ҽ��� ó���� ����
     *
     * @see BigDecimal
     * @param sDecimal ���ڰ� ���ڿ�
     * @param iDgtCnt ���ؼҼ��� �ڸ���
     * @param format BigDecimal����� ex) BigDecimal.ROUND_UP, BigDecimal.ROUND_DOWN
     * @param isNullToZero ���� null�ΰ�� "0"�������� ����, "Y" -> "0"����, �̿� null
     * @return ó���� �����
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

        if (isNullToZero.equals("Y") && (sRslt == null)) {	// ���̸� 0���� �ٲپ�� �� ���
            sRslt = "0";
        }

        return sRslt;
    }

    /**
     * <p>������ ���ڰ��� ���� �Ҽ��� ó���� ����
     *
     * @see BigDecimal
     * @param sDecimal ���ڰ� ���ڿ�
     * @param iDgtCnt ���ؼҼ��� �ڸ���
     * @param format BigDecimal����� ex) BigDecimal.ROUND_UP, BigDecimal.ROUND_DOWN
     * @return ó���� �����
     */
    public static String dgtFormat(String sDecimal, int iDgtCnt, int format) {
        return dgtFormat(sDecimal, iDgtCnt, format, "Y");
    }

    /**
     * <p>������ ���ڰ��� ���� �Ҽ��� ����ó��
     *
     * {@link #dgtFormat(String, int, int)}
     *
     * @param sDecimal ���ڰ� ���ڿ�
     * @param iDgtCnt ���ؼҼ��� �ڸ���
     * @return ����ȹ��ڿ�
     */
    public static String ceil(String sDecimal, int iDgtCnt) {
        return dgtFormat(sDecimal, iDgtCnt, BigDecimal.ROUND_CEILING, "Y");
    }

    /**
     * <p>������ ���ڰ��� ���� �Ҽ��� ����ó��
     *
     * {@link #dgtFormat(String, int, int)}
     *
     * @param sDecimal ���ڰ� ���ڿ�
     * @param iDgtCnt ���ؼҼ��� �ڸ���
     * @return ����ȹ��ڿ�
     */
    public static String round(String sDecimal, int iDgtCnt) {
        return dgtFormat(sDecimal, iDgtCnt, BigDecimal.ROUND_HALF_DOWN, "Y");
    }

    /**
     * <p>������ ���ڰ��� ���� �Ҽ��� ����ó��
     *
     * {@link #dgtFormat(String, int, int)}
     *
     * @param sDecimal ���ڰ� ���ڿ�
     * @param iDgtCnt ���ؼҼ��� �ڸ���
     * @return ����ȹ��ڿ�
     */
    public static String floor(String sDecimal, int iDgtCnt) {
        return dgtFormat(sDecimal, iDgtCnt, BigDecimal.ROUND_FLOOR, "Y");
    }

    /**
     * <p>��������, �μ��� null �̰ų� "" �ϰ�� "0"
     * @param data �������� ó���Ұ�
     * @return null���ڳ� �����ϰ�� "0" �׿ܿ� �������� �� ����
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
     * <p>��������
     * <p>�μ��� ""�̸� "NULL" ����
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
	         �����, ��ö�� 
	         ����� ��ȸ�� �������� ����ϱ� ���� �߰���
     * <p>�޼ҵ��                	: cnstStr</p>
     * <p>����                		: ������� �Է¹޾� ��ȸ�Ͽ� ������� ��ȯ</p>
     * <p>�޼ҵ��μ�1        	: String value - �־��� ���ڿ�</p>
     * <p>�޼ҵ��μ�2        	: ITransaction tx - ��ȯ�ϰ��� �ϴ� Ư�� ���ڿ�</p>
     * <p>�޼ҵ帮�ϰ�        		: String - Ư�����ڿ��� ��ȯ�� �� ����</p>
     * @throws BCException 
     * @throws SQLException 
     */
    public static String cnstStr(String value, Connection con) throws SQLException {
    	Statement stmt = null;
		ResultSet rs = null;
		String cnstCntn = "";
		//�ʿ� ��Ƽ� ����Ʈ�� ��ȯ�Ѵ�.
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
		log.info("�� ������ ��ȸ�̷� ����");
		if (RUtil.isNull(custNo) || RUtil.isNull(custNm)) {
			System.out.println("������ Audit Trail Log ���� �ʼ���(custNo, custNm)�� �����ϴ�.");
			return;
		}
		
		// ���������� �޾ƿ´�.
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		User user =  (User) session.getAttribute(ICbcFwConstant.CUSER);
		// ���α׷������� �޴´�.
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
		log.info("�� ������ ��ȸ�̷� ����(List)");
		// ���������� �޾ƿ´�.
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		User user =  (User) session.getAttribute(ICbcFwConstant.CUSER);
		// ���α׷������� �޴´�.
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
	 * �޼ҵ��		: insertCustInfoSearchHist
	 * ����			: �����ȣ�� ������ ��ȸ�̷� ���� (�ܰ���ȸ)
	 * �޼ҵ��μ�1 	: String loanNo
	 * �޼ҵ��μ�2 	: String loanSeq
	 * �޼ҵ��μ�3 	: ITransaction
	 * �޼ҵ帮�ϰ�	: N/A
	 * ����ó��		: Exception, BCException
	 */
	public static void insertCustInfoSearchHist(String loanNo, String loanSeq, ITransaction tx)
	throws Exception, BCException {
		
		// �����ȣ�� ����ȣ, ���� ��ȸ
		IBizBC bc = new CommonBCFactory().getBc("com.cbscap.bc.s.cs.cs.CustBC");
		StCustBaseTDO custInfo = ((ICustBC) bc).searchCustByLoanNo(new String[]{loanNo, loanSeq}, tx);
		
		// ������ ��ȸ�̷� ����
		custAuditTrail(custInfo.getCustNo(), custInfo.getCustNm(), "", loanNo, loanSeq, tx);
	}
	
	/**
	 * �޼ҵ��		: insertCustInfoSearchHistList
	 * ����			: �����ȣ�� ������ ��ȸ�̷� ����(����Ʈ��ȸ)
	 * �޼ҵ��μ�1 	: String schCond
	 * �޼ҵ��μ�2 	: ITransaction
	 * �޼ҵ帮�ϰ�	: N/A
	 * ����ó��		: Exception, BCException
	 */
	public static void insertCustInfoSearchHistList(String schCond, ITransaction tx)
	throws Exception, BCException {
		
		// ������ ��ȸ�̷� ����
		custAuditTrailList(schCond, tx);
	}
	
	/**
	 * �޼ҵ��		:
	 * ����			: �ֹι�ȣ ��üŰ ����
	 * �޼ҵ��μ�1 	: String divCd : S:��üŰ���ֹι�ȣ��ȸ, I:�ֹι�ȣ��ȣȭ����
	 * �޼ҵ��μ�2 	: String paramStr : S�϶� ��üŰ, I�϶� �ֹι�ȣ
	 * �޼ҵ��μ�3 	: String empNo : EMP_NO
	 * �޼ҵ��μ�4 	: Connection con
	 * �޼ҵ帮�ϰ�	: result[0]=�ڵ� (0:����, -1:����)
	 * �޼ҵ帮�ϰ�	: result[1]=�ڵ�޽���
	 * �޼ҵ帮�ϰ�	: result[2]=REPLACE_KEY (��üŰ)
	 * �޼ҵ帮�ϰ�	: result[3]=RESI_NO (�ֹι�ȣ)
	 * �޼ҵ帮�ϰ�	: result[4]=BRTH_DT (������� YYYYMMDD)
	 * �޼ҵ帮�ϰ�	: result[5]=SEX (���� ��:1 ��:2)
	 * ����ó��		: Exception, BCException
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
//				throw new Exception("�ֹι�ȣ�� �����ϴ�.");
//			}
//			if( paramStr.trim().length()<9 ){
//				throw new Exception("�ֹι�ȣ�� Ȯ�����ּ���.");
//			}
//
//			try {
//				// 1. �ֹε�Ϲ�ȣ�� �˻�
//				String sqlStmt1 =
//					" SELECT REPLACE_KEY,RESI_NO,BRTH_DT,SEX  \n"+
//					" FROM CBSCOMM.CT_RESI_NO_REPLACE         \n"+
//					" WHERE RESI_NO=?                         \n";
//				
//				pstmt1 = con.prepareStatement(sqlStmt1);
//				pstmt1.setString(1 , AES256Util.Encode(paramStr));
//				rs1 = pstmt1.executeQuery();
//
//				// 2-1. �����ϸ� ���� ����
//				if(rs1.next()){
//					result[0] = "0";
//					result[1] = "����(������)";
//					result[2] = rs1.getString("REPLACE_KEY");
//					result[3] = AES256Util.Decode(rs1.getString("RESI_NO"));
//					result[4] = rs1.getString("BRTH_DT");
//					result[5] = rs1.getString("SEX");
//				}
//				// 2-2. �������� ������ ����
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
//					log.info("��üŰ : " + tempNo);
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
//					result[1] = "����(����)";
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
//				// 1. ��üŰ �˻�
//				String sqlStmt1 =
//					" SELECT REPLACE_KEY,RESI_NO,BRTH_DT,SEX  \n"+
//					" FROM CBSCOMM.CT_RESI_NO_REPLACE         \n"+
//					" WHERE REPLACE_KEY=?                     \n";
//				
//				pstmt1 = con.prepareStatement(sqlStmt1);
//				pstmt1.setString(1 , paramStr);
//				rs1 = pstmt1.executeQuery();
//
//				// 2-1. �����ϸ� ��ȸ
//				if(rs1.next()){
//					result[0] = "0";
//					result[1] = "����";
//					result[2] = rs1.getString("REPLACE_KEY");
//					result[3] = AES256Util.Decode(rs1.getString("RESI_NO"));
//					result[4] = rs1.getString("BRTH_DT");
//					result[5] = rs1.getString("SEX");
//				}
//				// 2-2. �������� ������ �����ڵ�
//				else{
//					result[0] = "-1";
//					result[1] = "�ֹι�ȣ�� �������� �ʽ��ϴ�.";
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
//			result[1] = "���а��� Ȯ�����ּ���.";
//			return result;
//		}
//		
//		// 3. ����
//		return result;
//	}
	
	/**
	 * �޼ҵ��		:
	 * ����			: �ֹι�ȣ ��üŰ ����
	 * �޼ҵ��μ�1 	: String divCd : S:��üŰ���ֹι�ȣ��ȸ, I:�ֹι�ȣ��ȣȭ����
	 * �޼ҵ��μ�2 	: String paramStr : S�϶� ��üŰ, I�϶� �ֹι�ȣ
	 * �޼ҵ��μ�3 	: String empNo : EMP_NO
	 * �޼ҵ��μ�4 	: Connection con
	 * �޼ҵ帮�ϰ�	: result[0]=�ڵ� (0:����, -1:����)
	 * �޼ҵ帮�ϰ�	: result[1]=�ڵ�޽���
	 * �޼ҵ帮�ϰ�	: result[2]=REPLACE_KEY (��üŰ)
	 * �޼ҵ帮�ϰ�	: result[3]=RESI_NO (�ֹι�ȣ)
	 * �޼ҵ帮�ϰ�	: result[4]=BRTH_DT (������� YYYYMMDD)
	 * �޼ҵ帮�ϰ�	: result[5]=SEX (���� ��:1 ��:2)
	 * ����ó��		: Exception, BCException
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
//				throw new Exception("�ֹι�ȣ�� �����ϴ�.");
//			}
//			if( paramStr.trim().length()<9 ){
//				throw new Exception("�ֹι�ȣ�� Ȯ�����ּ���.");
//			}
//
//			try {
//				// 1. �ֹε�Ϲ�ȣ�� �˻�
//				String sqlStmt1 =
//					" SELECT REPLACE_KEY,RESI_NO,BRTH_DT,SEX  \n"+
//					" FROM CBSCOMM.CT_RESI_NO_REPLACE         \n"+
//					" WHERE RESI_NO=?                         \n";
//				
//				pstmt1 = con.prepareStatement(sqlStmt1);
//				pstmt1.setString(1 , AES256Util.Encode(paramStr));
//				rs1 = pstmt1.executeQuery();
//
//				// 2-1. �����ϸ� ���� ����
//				if(rs1.next()){
//					result[0] = "0";
//					result[1] = "����(������)";
//					result[2] = rs1.getString("REPLACE_KEY");
//					result[3] = AES256Util.Decode(rs1.getString("RESI_NO"));
//					result[4] = rs1.getString("BRTH_DT");
//					result[5] = rs1.getString("SEX");
//				}
//				// 2-2. �������� ������ ����
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
//					log.info("��üŰ : " + tempNo);
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
//					result[1] = "����(����)";
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
//				// 1. ��üŰ �˻�
//				String sqlStmt1 =
//					" SELECT REPLACE_KEY,RESI_NO,BRTH_DT,SEX  \n"+
//					" FROM CBSCOMM.CT_RESI_NO_REPLACE         \n"+
//					" WHERE REPLACE_KEY=?                     \n";
//				
//				pstmt1 = con.prepareStatement(sqlStmt1);
//				pstmt1.setString(1 , paramStr);
//				rs1 = pstmt1.executeQuery();
//
//				// 2-1. �����ϸ� ��ȸ
//				if(rs1.next()){
//					result[0] = "0";
//					result[1] = "����";
//					result[2] = rs1.getString("REPLACE_KEY");
//					result[3] = AES256Util.Decode(rs1.getString("RESI_NO"));
//					result[4] = rs1.getString("BRTH_DT");
//					result[5] = rs1.getString("SEX");
//				}
//				// 2-2. �������� ������ �����ڵ�
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
//			result[1] = "���а��� Ȯ�����ּ���.";
//			return paramStr;
//		}
//		
//		// 3. ����
//		if(divCd.equals("I")){
//			rslt = result[2];	
//		}else if(divCd.equals("S")){
//			rslt = result[3];
//		}
//		return rslt;
//	}
	
	
	
	
	/**
	 * �޼ҵ��		:
	 * ����			: �ֹι�ȣ ��üŰ ����
	 * �޼ҵ��μ�1 	: String divCd : S:��üŰ���ֹι�ȣ��ȸ, I:�ֹι�ȣ��ȣȭ����
	 * �޼ҵ��μ�2 	: String paramStr : S�϶� ��üŰ, I�϶� �ֹι�ȣ
	 * �޼ҵ帮�ϰ�	: result[0]=�ڵ� (0:����, -1:����)
	 * �޼ҵ帮�ϰ�	: result[1]=�ڵ�޽���
	 * �޼ҵ帮�ϰ�	: result[2]=REPLACE_KEY (��üŰ)
	 * �޼ҵ帮�ϰ�	: result[3]=RESI_NO (�ֹι�ȣ)
	 * �޼ҵ帮�ϰ�	: result[4]=BRTH_DT (������� YYYYMMDD)
	 * �޼ҵ帮�ϰ�	: result[5]=SEX (���� ��:1 ��:2)
	 * ����ó��		: Exception, BCException
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
//				throw new Exception("�ֹι�ȣ�� �����ϴ�.");
				result[0] = "0";
				result[1] = "��";
				result[2] = paramStr;
				result[3] = paramStr;
				return result;
			}
			if( paramStr.trim().length()<6 ){
				throw new Exception("�ֹι�ȣ�� Ȯ�����ּ���.");
			}
			Connection con = null;
			try {
				con = conMng.getConnection();
				// 1. �ֹε�Ϲ�ȣ�� �˻�
				String sqlStmt1 =
					" SELECT REPLACE_KEY,RESI_NO,BRTH_DT,SEX  \n"+
					" FROM CBSCOMM.CT_RESI_NO_REPLACE         \n"+
					" WHERE RESI_NO=?                         \n";
				
				pstmt1 = con.prepareStatement(sqlStmt1);
				pstmt1.setString(1 , AES256Util.Encode(paramStr));
				rs1 = pstmt1.executeQuery();

				// 2-1. �����ϸ� ���� ����
				if(rs1.next()){
					result[0] = "0";
					result[1] = "����(������)";
					result[2] = rs1.getString("REPLACE_KEY");
					result[3] = AES256Util.Decode(rs1.getString("RESI_NO"));
					result[4] = rs1.getString("BRTH_DT");
					result[5] = rs1.getString("SEX");
				}
				// 2-2. �������� ������ ����
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
					
					if(paramStr.length() > 6){ // �ֹι�ȣ 7�ڸ� �̻�
						pstmt2.setString(4 , paramStr.substring(6, 7));	
					}else{ // �ֹι�ȣ 6�ڸ�
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
					result[1] = "����(����)";
					result[2] = tempNo;
					result[3] = paramStr;
					result[4] = paramStr.substring(0, 6);
					
					if(paramStr.length() > 6){ // �ֹι�ȣ 7�ڸ� �̻�
						result[5] = paramStr.substring(6, 7);	
					}else{ // �ֹι�ȣ 6�ڸ�
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
				result[1] = "����(����)";
				result[2] = paramStr;
				result[3] = paramStr;
				return result;
			}
			if( !paramStr.substring(0, 1).equals("X") ){
				result[0] = "0";
				result[1] = "����(����)";
				result[2] = paramStr;
				result[3] = paramStr;
				return result;
			}
			Connection con = null;
			try {
				con = conMng.getConnection();
				// 1. ��üŰ �˻�
				String sqlStmt1 =
					" SELECT REPLACE_KEY,RESI_NO,BRTH_DT,SEX  \n"+
					" FROM CBSCOMM.CT_RESI_NO_REPLACE         \n"+
					" WHERE REPLACE_KEY=?                     \n";
				
				pstmt1 = con.prepareStatement(sqlStmt1);
				pstmt1.setString(1 , paramStr);
				rs1 = pstmt1.executeQuery();

				// 2-1. �����ϸ� ��ȸ
				if(rs1.next()){
					result[0] = "0";
					result[1] = "����";
					result[2] = rs1.getString("REPLACE_KEY");
					result[3] = AES256Util.Decode(rs1.getString("RESI_NO"));
					result[4] = rs1.getString("BRTH_DT");
					result[5] = rs1.getString("SEX");
				}
				// 2-2. �������� ������ �����ڵ�
				else{
					result[0] = "-1";
					result[1] = "�ֹι�ȣ�� �������� �ʽ��ϴ�.";
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
			result[1] = "���а��� Ȯ�����ּ���.";
			return result;
		}
		
		// 3. ����
		return result;
	}
	
	/**
	 * �޼ҵ��		:
	 * ����			: �ֹι�ȣ ��üŰ ����
	 * �޼ҵ��μ�1 	: String divCd : S:��üŰ���ֹι�ȣ��ȸ, I:�ֹι�ȣ��ȣȭ����
	 * �޼ҵ��μ�2 	: String paramStr : S�϶� ��üŰ, I�϶� �ֹι�ȣ
	 * �޼ҵ帮�ϰ�	: result[0]=�ڵ� (0:����, -1:����)
	 * �޼ҵ帮�ϰ�	: result[1]=�ڵ�޽���
	 * �޼ҵ帮�ϰ�	: result[2]=REPLACE_KEY (��üŰ)
	 * �޼ҵ帮�ϰ�	: result[3]=RESI_NO (�ֹι�ȣ)
	 * �޼ҵ帮�ϰ�	: result[4]=BRTH_DT (������� YYYYMMDD)
	 * �޼ҵ帮�ϰ�	: result[5]=SEX (���� ��:1 ��:2)
	 * ����ó��		: Exception, BCException
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
//				throw new Exception("�ֹι�ȣ�� �����ϴ�.");
			}
			if( paramStr.trim().length()<6 ){
				throw new Exception("�ֹι�ȣ�� Ȯ�����ּ���.");
			}
			Connection con = null;
			try {
				con = conMng.getConnection();
				// 1. �ֹε�Ϲ�ȣ�� �˻�
				String sqlStmt1 =
					" SELECT REPLACE_KEY,RESI_NO,BRTH_DT,SEX  \n"+
					" FROM CBSCOMM.CT_RESI_NO_REPLACE         \n"+
					" WHERE RESI_NO=?                         \n";
				
				pstmt1 = con.prepareStatement(sqlStmt1);
				pstmt1.setString(1 , AES256Util.Encode(paramStr));
				rs1 = pstmt1.executeQuery();

				// 2-1. �����ϸ� ���� ����
				if(rs1.next()){
					result[0] = "0";
					result[1] = "����(������)";
					result[2] = rs1.getString("REPLACE_KEY");
					result[3] = AES256Util.Decode(rs1.getString("RESI_NO"));
					result[4] = rs1.getString("BRTH_DT");
					result[5] = rs1.getString("SEX");
				}
				// 2-2. �������� ������ ����
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

					if(paramStr.length() > 6){ // �ֹι�ȣ 7�ڸ� �̻�
						pstmt2.setString(4 , paramStr.substring(6, 7));	
					}else{ // �ֹι�ȣ 6�ڸ�
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
					result[1] = "����(����)";
					result[2] = tempNo;
					result[3] = paramStr;
					result[4] = paramStr.substring(0, 6);
					
					if(paramStr.length() > 6){ // �ֹι�ȣ 7�ڸ� �̻�
						result[5] = paramStr.substring(6, 7);	
					}else{ // �ֹι�ȣ 6�ڸ�
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
				// 1. ��üŰ �˻�
				String sqlStmt1 =
					" SELECT REPLACE_KEY,RESI_NO,BRTH_DT,SEX  \n"+
					" FROM CBSCOMM.CT_RESI_NO_REPLACE         \n"+
					" WHERE REPLACE_KEY=?                     \n";
				
				pstmt1 = con.prepareStatement(sqlStmt1);
				pstmt1.setString(1 , paramStr);
				rs1 = pstmt1.executeQuery();

				// 2-1. �����ϸ� ��ȸ
				if(rs1.next()){
					result[0] = "0";
					result[1] = "����";
					result[2] = rs1.getString("REPLACE_KEY");
					result[3] = AES256Util.Decode(rs1.getString("RESI_NO"));
					result[4] = rs1.getString("BRTH_DT");
					result[5] = rs1.getString("SEX");
				}
				// 2-2. �������� ������ �����ڵ�
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
			result[1] = "���а��� Ȯ�����ּ���.";
			return paramStr;
		}
		
		// 3. ����
		if(divCd.equals("I")){
			rslt = result[2];	
		}else if(divCd.equals("S")){
			rslt = result[3];
		}
		return rslt;
	}
	
	// ���⼭���� �Ʒ����� ������ �Լ�
//	public static String[] EncDecResiNo(String divCd, String paramStr)
//			throws Exception, BCException {
//		String[] result = new String[6];
//		
//		if(divCd.equals("I")){
//			if (RUtil.isNull(paramStr)) {
//				throw new Exception("�ֹι�ȣ�� �����ϴ�.");
//			}
//			if( paramStr.trim().length()<9 ){
//				throw new Exception("�ֹι�ȣ�� Ȯ�����ּ���.");
//			}
//			result[0] = "0";
//			result[1] = "����(����)";
//			result[2] = paramStr;
//			result[3] = paramStr;
//			result[4] = paramStr.substring(0, 6);
//			result[5] = paramStr.substring(6, 7);
//		}else if(divCd.equals("S")){
//			result[0] = "0";
//			result[1] = "����(����)";
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
			throw new Exception("insertTable �� ���� �߻�");
		} finally {
		}
	}
}
