package com.cbscap.batch.r.zz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.cbscap.batch.common.BatchException;
import com.cbscap.batch.common.ConnMng;
import com.cbscap.fw.entity.c.DOList;
import com.cbscap.sc.r.util.DaoUtil;
import com.cbscap.sc.r.util.RUtil;


/**
 * <p>���ϸ�		: BatchCommn.java</p>
 * <p>������		: 1.0</p>
 * <p>�ۼ���		: 2012-12-10</p>
 * <p>�ۼ���		: ������</p>
 * <p>UseCase��	: ��ġ���� </p>
 * <p>���α׷���	: ��ġ���� </p>
 * <p>����		: ��ġ���� </p>
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class BatchComm {
	
	public BatchComm() {
	}
	
	/**
	 * <p>�޼ҵ��		: chkFile</p>
	 * <p>����			: ���ϰ�� üũ</p>
	 * <p>�޼ҵ��μ�1 	: String filePath</p>
	 * <p>�޼ҵ帮�ϰ�	: N/A</p>
	 * <p>����ó��		: BatchException</p>
	 */
	public static void chkFile(String filePath) throws BatchException {
//		log.info("�� ���ϰ�� üũ");
		if(!RUtil.isFileExist(filePath))
			throw new BatchException("��ο� �ش��ϴ� ����(����)�� �������� �ʽ��ϴ�.[��� : " + filePath + "]");
	}
	
	/**
	 * <p>�޼ҵ��		: chkFile</p>
	 * <p>����			: ��������</p>
	 * <p>�޼ҵ��μ�1 	: String path</p>
	 * <p>�޼ҵ帮�ϰ�	: N/A</p>
	 * <p>����ó��		: Exception</p>
	 */
	public static void mkDir(String path) throws Exception {
		File file = new File(path);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
	}
	
	/**
	 * <p>�޼ҵ��		: backup</p>
	 * <p>����			: ���� ��� (src=>dest)</p>
	 * <p>�޼ҵ��μ�1 	: source FullPath</p>
	 * <p>�޼ҵ��μ�2 	: target FullPath/p>
	 * <p>�޼ҵ帮�ϰ�	: boolean</p>
	 * <p>����ó��		: IOException, BatchException</p>
	 */
	public static boolean backup(String src, String dest) throws IOException, BatchException {
		if (RUtil.isFileExist(src)) {
			File srcFile = new File(src);
			File destFile = new File(dest);

			//������� ���� ���� ������ ���������. ���� ��������� �������´�.
			FileUtils.copyFile(srcFile, destFile);
			return true;
		}
		return false;
	}

	/**
	 * <p>�޼ҵ��		: isWorkDt</p>
	 * <p>����			: ������ ����</p>
	 * <p>�޼ҵ��μ�1 	: String dt</p>
	 * <p>�޼ҵ��μ�2 	: Connection</p>
	 * <p>�޼ҵ帮�ϰ�	: boolean</p>
	 * <p>����ó��		: SQLException</p>
	 */
	public static boolean isWorkDt(String dt, Connection con) throws SQLException {
//		log.info("�� BatchComm.isWorkDt : ������ üũ");
		
		StringBuffer sql = new StringBuffer();
		sql.append("\n	SELECT                     		");
		sql.append("\n		'X'			           		");
		sql.append("\n	FROM                       		");
		sql.append("\n		CBSCOMM.HT_WORK_CHAR_DD		");
		sql.append("\n	WHERE                      		");
		sql.append("\n		ATTD_DT = ?            		");
		sql.append("\n		AND OCPT_CD = 'B'      		");	// (�����ڵ�:���Ŵ��)
		sql.append("\n		AND CHAR_DIV_CD = 'AA0'		");	// (�ٹ��������ڵ�:����)
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		try {
			pstmt = con.prepareStatement(sql.toString());
				pstmt.setObject(1, RUtil.getFormatDt(dt, "-"));
			rs = pstmt.executeQuery();
			return rs.next();
			
		} finally {
			ConnMng.closeResultSet(rs);
			ConnMng.closeStatement(pstmt);
		}
	}
	
	/**
	 * <p>�޼ҵ��		: getWorkDt</p>
	 * <p>����			: ParamDate + add �������� �����´�.</p>
	 * <p>�޼ҵ��μ�1 	: String dt</p>
	 * <p>�޼ҵ��μ�2 	: Connection</p>
	 * <p>�޼ҵ帮�ϰ�	: boolean</p>
	 * <p>����ó��		: Exception</p>
	 */
	public static String getWorkDt(String dt, int add, Connection con) throws Exception {
//		log.info("�� BatchComm.getWorkDt : ("+add+") ������ ��ȸ");
		
		StringBuffer sql = new StringBuffer();
		sql.append("\n	WITH TMP AS (                                            	");
		sql.append("\n		SELECT                                               	");
		sql.append("\n			  ? 			AS DT                            	");
		sql.append("\n			, TO_NUMBER(?) 	AS VAR                           	");
		sql.append("\n		FROM                                                 	");
		sql.append("\n			DUAL                                             	");
		sql.append("\n	)                                                        	");
		sql.append("\n	SELECT                                                   	");
		sql.append("\n		ATTD_DT                                              	");
		sql.append("\n	FROM (                                                   	");
		sql.append("\n		SELECT                                               	");
		sql.append("\n			ATTD_DT                                          	");
		sql.append("\n			, B.VAR                                          	");
		sql.append("\n			, ROW_NUMBER() OVER(ORDER BY ATTD_DT ASC)	AS RN	");
		sql.append("\n		FROM                                                 	");
		sql.append("\n			CBSCOMM.HT_WORK_CHAR_DD A                        	");
		sql.append("\n			, TMP B                                          	");
		sql.append("\n		WHERE                                                	");
		sql.append("\n			ATTD_DT > B.DT                                   	");
		sql.append("\n			AND OCPT_CD = 'B'                                	");
		sql.append("\n			AND CHAR_DIV_CD = 'AA0'                          	");
		sql.append("\n		)                                                    	");
		sql.append("\n	WHERE                                                    	");
		sql.append("\n		0 < VAR AND RN = VAR                                 	");
		sql.append("\n	UNION ALL                                                	");
		sql.append("\n	SELECT                                                   	");
		sql.append("\n		ATTD_DT                                              	");
		sql.append("\n	FROM (                                                   	");
		sql.append("\n		SELECT                                               	");
		sql.append("\n			ATTD_DT                                          	");
		sql.append("\n			, B.VAR                                          	");
		sql.append("\n			, ROW_NUMBER() OVER(ORDER BY ATTD_DT DESC)	AS RN	");
		sql.append("\n		FROM                                                 	");
		sql.append("\n			CBSCOMM.HT_WORK_CHAR_DD A                        	");
		sql.append("\n			, TMP B                                          	");
		sql.append("\n		WHERE                                                	");
		sql.append("\n			ATTD_DT < B.DT                                   	");
		sql.append("\n			AND OCPT_CD = 'B'                                	");
		sql.append("\n			AND CHAR_DIV_CD = 'AA0'                          	");
		sql.append("\n		)                                                    	");
		sql.append("\n	WHERE                                                    	");
		sql.append("\n		0 > VAR AND RN = -1 * VAR                            	");
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		try {
			pstmt = con.prepareStatement(sql.toString());
				pstmt.setObject(1, RUtil.getFormatDt(dt, "-"));
				pstmt.setObject(2, add);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				return rs.getString(1);
			} else {
				RUtil.logSql(sql.toString(), new String[]{RUtil.getFormatDt(dt, "-"), add+""});
				throw new Exception((add>0?"+":"")+add+"�� �ش��ϴ� �������� �������� �ʽ��ϴ�.");
			}
		} finally {
			ConnMng.closeResultSet(rs);
			ConnMng.closeStatement(pstmt);
		}
	}
	
	/**
	 * <p>�޼ҵ��		: getRlDueDt</p>
	 * <p>����			: ��û���� ��ȸ(�����̸� ���� ������, ������ �������̸� �״�� ��ȸ�� ���ڸ� ��ȯ)</p>
	 * <p>�޼ҵ��μ�1 	: String dt</p>
	 * <p>�޼ҵ��μ�2 	: Connection</p>
	 * <p>�޼ҵ帮�ϰ�	: boolean</p>
	 * <p>����ó��		: Exception</p>
	 */
	public static String getRlDueDt(String dt, Connection con) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		sql.append("\n	SELECT                                               	");
		sql.append("\n		MIN(ATTD_DT)                                     	");
		sql.append("\n	FROM                                                 	");
		sql.append("\n		CBSCOMM.HT_WORK_CHAR_DD A                        	");
		sql.append("\n	WHERE                                                	");
		sql.append("\n		ATTD_DT >= ?                                     	");
		sql.append("\n		AND OCPT_CD = 'B'                                	");
		sql.append("\n		AND CHAR_DIV_CD = 'AA0'                          	");
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setObject(1, RUtil.getFormatDt(dt, "-"));

			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			} else {
				throw new Exception(dt+"�� ��û������ �������� �ʽ��ϴ�.");
			}
		} finally {
			ConnMng.closeResultSet(rs);
			ConnMng.closeStatement(pstmt);
		}
	}

	// �Էµ� ��¥���� �۰ų� ���� ���� �ֱ� ������
	public static String getLastSaleDt(String dt, Connection con) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("\n SELECT MAX(ATTD_DT)					");
		sql.append("\n   FROM CBSCOMM.HT_WORK_CHAR_DD A		");
		sql.append("\n  WHERE ATTD_DT    <= ?				");
		sql.append("\n    AND OCPT_CD     = 'B'				");
		sql.append("\n    AND CHAR_DIV_CD = 'AA0'			");

		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setObject(1, RUtil.getFormatDt(dt, "-"));

			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			} else {
				throw new Exception(dt + "�� ���� �ֱ� �������� �������� �ʽ��ϴ�.");
			}
		} finally {
			ConnMng.closeResultSet(rs);
			ConnMng.closeStatement(pstmt);
		}
	}
	
	/**
	 * <p>�޼ҵ��		: getLastWorkDtM</p>
	 * <p>����			: ����������</p>
	 * <p>�޼ҵ��μ�1 	: String dt</p>
	 * <p>�޼ҵ��μ�2 	: Connection</p>
	 * <p>�޼ҵ帮�ϰ�	: boolean</p>
	 * <p>����ó��		: Exception</p>
	 */
	public static String getLastWorkDtM(String dt, Connection con) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		sql.append("\n	SELECT                                               	");
		sql.append("\n		MAX(ATTD_DT)                                     	");
		sql.append("\n	FROM                                                 	");
		sql.append("\n		CBSCOMM.HT_WORK_CHAR_DD A                        	");
		sql.append("\n	WHERE                                                	");
		sql.append("\n		ATTD_DT LIKE ?||'%'                              	");
		sql.append("\n		AND OCPT_CD = 'B'                                	");
		sql.append("\n		AND CHAR_DIV_CD = 'AA0'                          	");
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setObject(1, RUtil.getFormatDt(dt, "-").substring(0, 7));

			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			} else {
				throw new Exception(dt+"�� ������������ �������� �ʽ��ϴ�.");
			}
		} finally {
			ConnMng.closeResultSet(rs);
			ConnMng.closeStatement(pstmt);
		}
	}
	
	/**
	 * <p>�޼ҵ��		: selectTrOrgInfo</p>
	 * <p>����			: �ŷ��������</p>
	 * <p>�޼ҵ��μ�1 	: String trOrgCd</p>
	 * <p>�޼ҵ��μ�2 	: Connection</p>
	 * <p>�޼ҵ帮�ϰ�	: Map</p>
	 * <p>����ó��		: Exception</p>
	 */
	public static Map selectTrOrgInfo(String trOrgCd, Connection con) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		sql.append("\n	SELECT                                           	");
		sql.append("\n		  TR_ORG_CD                                  	");
		sql.append("\n		, TR_ORG_NM                                  	");
		sql.append("\n		, ACCT_UNIT_CD                               	");
		sql.append("\n		, RCPT_TP_CD                                 	");
		sql.append("\n		, CYCL_CD                                    	");
		sql.append("\n		, BANK_CD                                    	");
		sql.append("\n		, MO_ACNT_NO                                 	");
		sql.append("\n		, VNDR_CD                                    	");
		sql.append("\n		, ASK_FEE                                    	");
		sql.append("\n		, OAMT_FEE                                   	");
		sql.append("\n	FROM                                             	");
		sql.append("\n		CBSRQST.RT_TR_ORG_BASE A                     	");
		sql.append("\n	WHERE                                            	");
		sql.append("\n		TR_ORG_CD = ?                                	");
		sql.append("\n		AND TO_CHAR(SYSDATE, 'YYYY-MM-DD')           	");
		sql.append("\n			BETWEEN STRT_DUE_DT AND END_DUE_DT       	");
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		Map map = new HashMap();
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setObject(1, trOrgCd);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				map.put("trOrgCd"   , rs.getString("TR_ORG_CD"));
				map.put("trOrgNm"   , rs.getString("TR_ORG_NM"));
				map.put("acctUnitCd", rs.getString("ACCT_UNIT_CD"));
				map.put("rcptTpCd"  , rs.getString("RCPT_TP_CD"));
				map.put("cyclCd"    , rs.getString("CYCL_CD"));
				map.put("bankCd"    , rs.getString("BANK_CD"));
				map.put("moAcntNo"  , rs.getString("MO_ACNT_NO"));
				map.put("vndrCd"    , rs.getString("VNDR_CD"));
				map.put("askFee"    , rs.getLong  ("ASK_FEE"));
				map.put("oamtFee"   , rs.getLong  ("OAMT_FEE"));
				return map;
			} else {
				throw new Exception(trOrgCd+"�� �ش��ϴ� �ŷ������ �������� �ʽ��ϴ�.");
			}
		} finally {
			ConnMng.closeResultSet(rs);
			ConnMng.closeStatement(pstmt);
		}
	}
	
	/**
	 * <p>�޼ҵ��		: selectTrOrgInfo</p>
	 * <p>����			: �ŷ��������</p>
	 * <p>�޼ҵ��μ�1 	: String trOrgCd</p>
	 * <p>�޼ҵ��μ�2 	: Connection</p>
	 * <p>�޼ҵ帮�ϰ�	: Map</p>
	 * <p>����ó��		: Exception</p>
	 */
	public static Map selectTrOrgInfo(String rcptTpCd, String bankCd, Connection con) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		sql.append("\n	SELECT                                       	");
		sql.append("\n		  TR_ORG_CD                              	");
		sql.append("\n		, TR_ORG_NM                              	");
		sql.append("\n		, ACCT_UNIT_CD                           	");
		sql.append("\n		, RCPT_TP_CD                             	");
		sql.append("\n		, CYCL_CD                                	");
		sql.append("\n		, BANK_CD                                	");
		sql.append("\n		, MO_ACNT_NO                             	");
		sql.append("\n		, VNDR_CD                                	");
		sql.append("\n		, ASK_FEE                                	");
		sql.append("\n		, OAMT_FEE                               	");
		sql.append("\n	FROM                                         	");
		sql.append("\n		CBSRQST.RT_TR_ORG_BASE A                 	");
		sql.append("\n	WHERE                                        	");
		sql.append("\n		TR_ORG_CD IN (CBSRQST.RF_TR_ORG_CD(?, ?))	");
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		Map map = new HashMap();
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setObject(1, rcptTpCd);
			pstmt.setObject(2, bankCd);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				map.put("trOrgCd"   , rs.getString("TR_ORG_CD"));
				map.put("trOrgNm"   , rs.getString("TR_ORG_NM"));
				map.put("acctUnitCd", rs.getString("ACCT_UNIT_CD"));
				map.put("rcptTpCd"  , rs.getString("RCPT_TP_CD"));
				map.put("cyclCd"    , rs.getString("CYCL_CD"));
				map.put("bankCd"    , rs.getString("BANK_CD"));
				map.put("moAcntNo"  , rs.getString("MO_ACNT_NO"));
				map.put("vndrCd"    , rs.getString("VNDR_CD"));
				map.put("askFee"    , rs.getLong  ("ASK_FEE"));
				map.put("oamtFee"   , rs.getLong  ("OAMT_FEE"));
				return map;
			} else {
				throw new Exception("��������("+rcptTpCd+"), �����ڵ�("+bankCd+")�� �ش��ϴ� �ŷ������ �������� �ʽ��ϴ�.");
			}
		} finally {
			ConnMng.closeResultSet(rs);
			ConnMng.closeStatement(pstmt);
		}
	}
	
	/**
	 * <p>�޼ҵ��		: selectTrOrgInfoDesc</p>
	 * <p>����			: �ŷ��������(���ϸ����)</p>
	 * <p>�޼ҵ��μ�1 	: String trOrgCd</p>
	 * <p>�޼ҵ��μ�2 	: Connection</p>
	 * <p>�޼ҵ帮�ϰ�	: Map</p>
	 * <p>����ó��		: Exception</p>
	 */
	public static Map selectTrOrgInfoDesc(String rcptTpCd, String bankCd, String clasGb, Connection con) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		sql.append("\n	SELECT                                           	");
		sql.append("\n		  TR_ORG_CD                                  	");
		sql.append("\n		, TR_ORG_NM                                  	");
		sql.append("\n		, ACCT_UNIT_CD                               	");
		sql.append("\n		, RCPT_TP_CD                                 	");
		sql.append("\n		, CYCL_CD                                    	");
		sql.append("\n		, BANK_CD                                    	");
		sql.append("\n		, MO_ACNT_NO                                 	");
		sql.append("\n		, VNDR_CD                                    	");
		sql.append("\n		, ASK_FEE                                    	");
		sql.append("\n		, OAMT_FEE                                   	");
		sql.append("\n		, C.CD_DESC_SHRT_NM	AS GRAM_CD               	");
		sql.append("\n		, C.CD_DESC_ETC_NM	AS FILE_NM               	");
		sql.append("\n	FROM                                             	");
		sql.append("\n		CBSRQST.RT_TR_ORG_BASE A                     	");
		sql.append("\n			LEFT OUTER JOIN CBSCOMM.CT_COMM_CD_DESC C	");
		sql.append("\n				ON  C.CD_KIND_NO = 'R00017'          	");
		sql.append("\n				AND C.CLAS1 = A.BANK_CD              	");
		sql.append("\n				AND C.CLAS2 = ?                      	");
		sql.append("\n	WHERE                                            	");
		sql.append("\n		TR_ORG_CD IN (CBSRQST.RF_TR_ORG_CD(?, ?))    	");
		sql.append("\n		AND TO_CHAR(SYSDATE, 'YYYY-MM-DD')           	");
		sql.append("\n			BETWEEN STRT_DUE_DT AND END_DUE_DT       	");
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		Map map = new HashMap();
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setObject(1, clasGb);
			pstmt.setObject(2, rcptTpCd);
			pstmt.setObject(3, bankCd);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				DaoUtil.convRsMap(rs);
				// trOrgCd, trOrgNm, acctUnitCd, rcptTpCd, cyclCd
				// bankCd, moAcntNo, vndrCd, askFee, oamtFee
				// gramCd, fileNm
				return map;
			} else {
				throw new Exception(bankCd+"("+rcptTpCd+")�� �ش��ϴ� �ŷ������ �������� �ʽ��ϴ�.");
			}
		} finally {
			ConnMng.closeResultSet(rs);
			ConnMng.closeStatement(pstmt);
		}
	}
	
	/**
	 * <p>�޼ҵ��		: searchErrSmsList</p>
	 * <p>����			: ��ġ���� �ý��� ������ SMS���� �����ȸ(�������ȸ)</p>
	 * <p>�޼ҵ��μ�1 	: String clasCd</p>
	 * <p>�޼ҵ��μ�2 	: Connection</p>
	 * <p>�޼ҵ帮�ϰ�	: boolean</p>
	 * <p>����ó��		: SQLException</p>
	 */
	public static DOList searchErrSmsList(String clasCd, Connection con) throws SQLException {
		
		DOList list = new DOList();
		
		StringBuffer sql = new StringBuffer();
		sql.append("\n	SELECT                                                                    	");
		sql.append("\n		CD_DESC_KOR_NM	AS SEND_TEL_NO                                        	");	// ���۹�ȣ
		sql.append("\n		, ( SELECT CD_DESC_KOR_NM FROM CBSCOMM.CT_COMM_CD_DESC                	");
		sql.append("\n			WHERE CD_KIND_NO = 'R00141' AND CD_DESC_NO = '1')	AS RECV_TEL_NO	");	// ���Ź�ȣ
		sql.append("\n	FROM                                                                      	");
		sql.append("\n		CBSCOMM.CT_COMM_CD_DESC                                               	");
		sql.append("\n	WHERE                                                                     	");
		sql.append("\n		CD_KIND_NO = 'R00148'                                                 	");
		sql.append("\n		AND CLAS1 = ?                                                         	");
		sql.append("\n	ORDER BY                                                                  	");
		sql.append("\n		CD_DESC_NO                                                            	");
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setObject(1, clasCd);

			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String[] arr = new String[2];
				arr[0] = rs.getString("SEND_TEL_NO");
				arr[1] = rs.getString("RECV_TEL_NO");
				
				list.add(arr);
			}
		} finally {
			ConnMng.closeResultSet(rs);
			ConnMng.closeStatement(pstmt);
		}
		return list;
	}
	
	/*** Connection ***/
	public static void commit(Connection con) throws SQLException {
		if (con!=null) ConnMng.userCommit(con);
	}

	public static void rollback(Connection con) {
		try {
			if (con!=null) con.rollback();
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}
	
	public static void release(Connection con) {
		ConnMng.closeConnection(con);
	}
	
	/**
	 * <p>�޼ҵ��		: mkChkFile</p>
	 * <p>����			: chk���� �����</p>
	 * <p>�޼ҵ��μ�1 	: String filePath</p>
	 * <p>�޼ҵ帮�ϰ�	: N/A</p>
	 * <p>����ó��		: BatchException</p>
	 */
	public static void mkChkFile(String filePath, String fileName, String date, int cnt) throws BatchException {
		// ��� �������� ������ ����
		File file = new File(filePath);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new OutputStreamWriter
				(new FileOutputStream(filePath + fileName+".chk", false), "EUC_KR"), true);
			// 8�ڸ�(��¥) + 30�ڸ�(���ϸ�) + 10�ڸ�(�Ǽ�)
			printWriter.print(unformat(date) + rpad(fileName, 30, " ") + lpad(Integer.toString(cnt), 10, " "));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			printWriter.close();			
		}
	}
	
	public static String rpad(String str, int len, String p) {
		str = nvl(str);
		for (int i=str.length(); i<len; i++) {
			str = str+p;
		}
		return str;
	}
	
	public static String lpad(String str, int len, String p) {
		str = nvl(str);
		for (int i=str.length(); i<len; i++) {
			str = p+str;
		}
		return str;
	}
	
	public static String nvl(String str) {
		return nvl(str, "");
	}
	
	public static String nvl(String str, String replace) {
		if (str==null || str.trim().equals("") || str.toUpperCase().equals("NULL"))
			return replace;
		else
			return str;
	}
	
	public static String unformat(String value)
    {
        if (value == null)
        {
            return null;
        }

        if (value.length() < 10)
        {
            return value;
        }

        StringBuffer sBuf = new StringBuffer(8);
        sBuf.append(value.substring(0, 4)).append(value.substring(5, 7)).append(value.substring(
                8, 10));

        return sBuf.toString();

    }
}
