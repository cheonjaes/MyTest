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
 * <p>파일명		: BatchCommn.java</p>
 * <p>버　전		: 1.0</p>
 * <p>작성일		: 2012-12-10</p>
 * <p>작성자		: 정우현</p>
 * <p>UseCase명	: 배치공통 </p>
 * <p>프로그램명	: 배치공통 </p>
 * <p>내용		: 배치공통 </p>
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class BatchComm {
	
	public BatchComm() {
	}
	
	/**
	 * <p>메소드명		: chkFile</p>
	 * <p>설명			: 파일경로 체크</p>
	 * <p>메소드인수1 	: String filePath</p>
	 * <p>메소드리턴값	: N/A</p>
	 * <p>예외처리		: BatchException</p>
	 */
	public static void chkFile(String filePath) throws BatchException {
//		log.info("■ 파일경로 체크");
		if(!RUtil.isFileExist(filePath))
			throw new BatchException("경로에 해당하는 폴더(파일)이 존재하지 않습니다.[경로 : " + filePath + "]");
	}
	
	/**
	 * <p>메소드명		: chkFile</p>
	 * <p>설명			: 폴더생성</p>
	 * <p>메소드인수1 	: String path</p>
	 * <p>메소드리턴값	: N/A</p>
	 * <p>예외처리		: Exception</p>
	 */
	public static void mkDir(String path) throws Exception {
		File file = new File(path);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
	}
	
	/**
	 * <p>메소드명		: backup</p>
	 * <p>설명			: 파일 백업 (src=>dest)</p>
	 * <p>메소드인수1 	: source FullPath</p>
	 * <p>메소드인수2 	: target FullPath/p>
	 * <p>메소드리턴값	: boolean</p>
	 * <p>예외처리		: IOException, BatchException</p>
	 */
	public static boolean backup(String src, String dest) throws IOException, BatchException {
		if (RUtil.isFileExist(src)) {
			File srcFile = new File(src);
			File destFile = new File(dest);

			//재생성시 기존 파일 내용은 덮어써진다. 따라서 백업파일을 만들어놓는다.
			FileUtils.copyFile(srcFile, destFile);
			return true;
		}
		return false;
	}

	/**
	 * <p>메소드명		: isWorkDt</p>
	 * <p>설명			: 영업일 여부</p>
	 * <p>메소드인수1 	: String dt</p>
	 * <p>메소드인수2 	: Connection</p>
	 * <p>메소드리턴값	: boolean</p>
	 * <p>예외처리		: SQLException</p>
	 */
	public static boolean isWorkDt(String dt, Connection con) throws SQLException {
//		log.info("■ BatchComm.isWorkDt : 영업일 체크");
		
		StringBuffer sql = new StringBuffer();
		sql.append("\n	SELECT                     		");
		sql.append("\n		'X'			           		");
		sql.append("\n	FROM                       		");
		sql.append("\n		CBSCOMM.HT_WORK_CHAR_DD		");
		sql.append("\n	WHERE                      		");
		sql.append("\n		ATTD_DT = ?            		");
		sql.append("\n		AND OCPT_CD = 'B'      		");	// (직종코드:여신담당)
		sql.append("\n		AND CHAR_DIV_CD = 'AA0'		");	// (근무성격일코드:정상)
		
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
	 * <p>메소드명		: getWorkDt</p>
	 * <p>설명			: ParamDate + add 영업일을 가져온다.</p>
	 * <p>메소드인수1 	: String dt</p>
	 * <p>메소드인수2 	: Connection</p>
	 * <p>메소드리턴값	: boolean</p>
	 * <p>예외처리		: Exception</p>
	 */
	public static String getWorkDt(String dt, int add, Connection con) throws Exception {
//		log.info("■ BatchComm.getWorkDt : ("+add+") 영업일 조회");
		
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
				throw new Exception((add>0?"+":"")+add+"에 해당하는 영업일이 존재하지 않습니다.");
			}
		} finally {
			ConnMng.closeResultSet(rs);
			ConnMng.closeStatement(pstmt);
		}
	}
	
	/**
	 * <p>메소드명		: getRlDueDt</p>
	 * <p>설명			: 실청구일 조회(휴일이면 도래 영업일, 당일이 영업일이면 그대로 조회한 일자를 반환)</p>
	 * <p>메소드인수1 	: String dt</p>
	 * <p>메소드인수2 	: Connection</p>
	 * <p>메소드리턴값	: boolean</p>
	 * <p>예외처리		: Exception</p>
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
				throw new Exception(dt+"의 실청구일이 존재하지 않습니다.");
			}
		} finally {
			ConnMng.closeResultSet(rs);
			ConnMng.closeStatement(pstmt);
		}
	}

	// 입력된 날짜보다 작거나 같은 가장 최근 영업일
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
				throw new Exception(dt + "의 가장 최근 영업일이 존재하지 않습니다.");
			}
		} finally {
			ConnMng.closeResultSet(rs);
			ConnMng.closeStatement(pstmt);
		}
	}
	
	/**
	 * <p>메소드명		: getLastWorkDtM</p>
	 * <p>설명			: 월말영업일</p>
	 * <p>메소드인수1 	: String dt</p>
	 * <p>메소드인수2 	: Connection</p>
	 * <p>메소드리턴값	: boolean</p>
	 * <p>예외처리		: Exception</p>
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
				throw new Exception(dt+"의 월말영업일이 존재하지 않습니다.");
			}
		} finally {
			ConnMng.closeResultSet(rs);
			ConnMng.closeStatement(pstmt);
		}
	}
	
	/**
	 * <p>메소드명		: selectTrOrgInfo</p>
	 * <p>설명			: 거래기관정보</p>
	 * <p>메소드인수1 	: String trOrgCd</p>
	 * <p>메소드인수2 	: Connection</p>
	 * <p>메소드리턴값	: Map</p>
	 * <p>예외처리		: Exception</p>
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
				throw new Exception(trOrgCd+"에 해당하는 거래기관이 존재하지 않습니다.");
			}
		} finally {
			ConnMng.closeResultSet(rs);
			ConnMng.closeStatement(pstmt);
		}
	}
	
	/**
	 * <p>메소드명		: selectTrOrgInfo</p>
	 * <p>설명			: 거래기관정보</p>
	 * <p>메소드인수1 	: String trOrgCd</p>
	 * <p>메소드인수2 	: Connection</p>
	 * <p>메소드리턴값	: Map</p>
	 * <p>예외처리		: Exception</p>
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
				throw new Exception("수납유형("+rcptTpCd+"), 은행코드("+bankCd+")에 해당하는 거래기관이 존재하지 않습니다.");
			}
		} finally {
			ConnMng.closeResultSet(rs);
			ConnMng.closeStatement(pstmt);
		}
	}
	
	/**
	 * <p>메소드명		: selectTrOrgInfoDesc</p>
	 * <p>설명			: 거래기관정보(파일명까지)</p>
	 * <p>메소드인수1 	: String trOrgCd</p>
	 * <p>메소드인수2 	: Connection</p>
	 * <p>메소드리턴값	: Map</p>
	 * <p>예외처리		: Exception</p>
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
				throw new Exception(bankCd+"("+rcptTpCd+")에 해당하는 거래기관이 존재하지 않습니다.");
			}
		} finally {
			ConnMng.closeResultSet(rs);
			ConnMng.closeStatement(pstmt);
		}
	}
	
	/**
	 * <p>메소드명		: searchErrSmsList</p>
	 * <p>설명			: 배치등의 시스템 에러시 SMS전송 대상조회(담당자조회)</p>
	 * <p>메소드인수1 	: String clasCd</p>
	 * <p>메소드인수2 	: Connection</p>
	 * <p>메소드리턴값	: boolean</p>
	 * <p>예외처리		: SQLException</p>
	 */
	public static DOList searchErrSmsList(String clasCd, Connection con) throws SQLException {
		
		DOList list = new DOList();
		
		StringBuffer sql = new StringBuffer();
		sql.append("\n	SELECT                                                                    	");
		sql.append("\n		CD_DESC_KOR_NM	AS SEND_TEL_NO                                        	");	// 전송번호
		sql.append("\n		, ( SELECT CD_DESC_KOR_NM FROM CBSCOMM.CT_COMM_CD_DESC                	");
		sql.append("\n			WHERE CD_KIND_NO = 'R00141' AND CD_DESC_NO = '1')	AS RECV_TEL_NO	");	// 수신번호
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
	 * <p>메소드명		: mkChkFile</p>
	 * <p>설명			: chk파일 만들기</p>
	 * <p>메소드인수1 	: String filePath</p>
	 * <p>메소드리턴값	: N/A</p>
	 * <p>예외처리		: BatchException</p>
	 */
	public static void mkChkFile(String filePath, String fileName, String date, int cnt) throws BatchException {
		// 경로 존재하지 않을시 생성
		File file = new File(filePath);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new OutputStreamWriter
				(new FileOutputStream(filePath + fileName+".chk", false), "EUC_KR"), true);
			// 8자리(날짜) + 30자리(파일명) + 10자리(건수)
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
