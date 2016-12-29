package com.cbscap.util;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.cbscap.bc.s.cs.cs.ICustBC;
import com.cbscap.bc.s.cs.cs.StCustBaseTDO;
import com.cbscap.fw.bc.c.BCException;
import com.cbscap.fw.bc.c.CommonBCFactory;
import com.cbscap.fw.bc.c.IBizBC;
import com.cbscap.fw.dbutil.dbmanager.ISqlResult;
import com.cbscap.fw.dbutil.transaction.ITransaction;
import com.cbscap.fw.entity.Event;
import com.cbscap.fw.entity.c.DOList;
import com.cbscap.fw.util.format.ServerSysDate;
import com.cbscap.ilnk.entity.nice.Nice73070RecvEntity;
import com.cbscap.ilnk.entity.nice.Nice73070SendEntity;
import com.cbscap.ilnk.entity.nice.Nice73070ServiceCC;
import com.cbscap.sc.r.util.RUtil;
import com.cbscap.sc.s.ta.ty.TatyCC;
import com.cbscap.sc.s.ta.ty.TatyObj;

/**
 * 대출 공통
 * 작성일 : 2012.05.27
 * @author KiNG
 */
public class LoanComm
{
	
	private static CommonBCFactory factory = new CommonBCFactory();
	private static Logger log = Logger.getLogger(LoanComm.class.getName());
	private final static String CUST_BC ="com.cbscap.bc.s.cs.cs.CustBC"; //고객관련 BC
	
	/**
	 * 업무 단계 조회 구분자
	 */
	public enum BSN_STAG_DIV 
	{
		ESTM	// 견적(리스)
		,CNSL	// 상담
		,CONT	// 계약(리스)
		,LOAN	// 신청(대출)
	}
	
	/**
	 * 업무구분 : 모기지(M), 개인대출(P), 할부(A), 리스(L)
	 */
	public enum BSN_DIV
	{
		M //모기지
		,P //개인대출
		,A //할부
		,L //리스
	}
	
	/**
	 * <p>메소드명		: getCustExistFgByResiNo </p>
	 * <p>설명			: 고객기본에 존재여부 </p>
	 * <p>메소드인수1		: custResiNo 주민번호 </p>
	 * <p>메소드리턴값	: 존재여부  </p>
	 * <p>예외처리		: BCException</p>
	 */
	public static boolean getCustExistFgByResiNo(String custResiNo, Event event) throws BCException 
	{
		ITransaction tx = event.getTransaction();	

		boolean bExistFg = false;

		IBizBC bc = factory.getBc(CUST_BC);
		DOList doList = ((ICustBC)bc).exstCustExsiYn(custResiNo, tx);

		if(doList.size() > 0)
		{
			String strExistYN = ((String[])doList.get(0))[0];

			if (strExistYN.equals("Y"))
			{
				bExistFg = true;
			}
		} 
		return bExistFg;
	}

	/**
	 * <p>메소드명		: getCustNoByResiNo </p>
	 * <p>설명			: 고객번호 조회 </p>
	 * <p>메소드인수1		: 주민번호</p>
	 * <p>메소드리턴값	: 고객번호</p>
	 */
	public static String getCustNoByResiNo(String custResiNo, Event event) throws Exception
	{
		ITransaction tx = event.getTransaction();
		IBizBC bc = factory.getBc(CUST_BC);
		String strCustNo = "";

		StCustBaseTDO searchTDO = new StCustBaseTDO();
		searchTDO.setCustResiNo(custResiNo);

		DOList doList = bc.search(searchTDO, tx);

		if (doList != null && doList.size() > 0) 
		{
			StCustBaseTDO resultTDO = (StCustBaseTDO)doList.get(0);
			strCustNo = resultTDO.getCustNo().trim();
		}

		return strCustNo;
	}
	/**
	 * <p>메소드명		: getCustNoByBizNo </p>
	 * <p>설명			: 고객번호 조회 </p>
	 * <p>메소드인수1		: 사업자번호</p>
	 * <p>메소드리턴값	: 고객번호</p>
	 */
	public static String getCustNoByBizNo(String bizNo, Event event) throws Exception
	{
		ITransaction tx = event.getTransaction();
		IBizBC bc = factory.getBc(CUST_BC);
		String strCustNo = "";

		StCustBaseTDO searchTDO = new StCustBaseTDO();
		//searchTDO.setCustResiNo(bizNo);
		searchTDO.setBizNo(bizNo);  // [LOTTE] 2013.10.18 LSH 법인 사업자번호

		DOList doList = bc.search(searchTDO, tx);

		if (doList != null && doList.size() > 0) 
		{
			StCustBaseTDO resultTDO = (StCustBaseTDO)doList.get(0);
			strCustNo = resultTDO.getCustNo().trim();
		}

		return strCustNo;
	}
	/**
	 * <p>메소드명		: getResiNoByCustNo </p>
	 * <p>설명			: 주민번호 조회 </p>
	 * <p>메소드인수1		: 고객번호</p>
	 * <p>메소드리턴값	: 주민번호</p>
	 */
	public static String getResiNoByCustNo(String custNo, Event event) throws Exception
	{
		ITransaction tx = event.getTransaction();
		IBizBC bc = factory.getBc(CUST_BC);
		String strResiNo = " ";

		StCustBaseTDO searchTDO = new StCustBaseTDO();
		searchTDO.setCustNo(custNo);

		DOList doList = bc.search(searchTDO, tx);

		if (doList != null && doList.size() > 0) 
		{
			StCustBaseTDO resultTDO = (StCustBaseTDO)doList.get(0);
			strResiNo = resultTDO.getCustResiNo().trim();
		}

		return strResiNo;
	}
	
	/**
	 * <p>메소드명		: setCustBaseByResiNo</p>
	 * <p>설명			: 고객번호 조회</p>
	 * <p>메소드인수1		: 주민번호</p>
	 * <p>메소드리턴값	: 고객번호</p>
	 */
	public static StCustBaseTDO setCustBaseByResiNo(String custResiNo, Event event) throws Exception
	{
		ITransaction tx = event.getTransaction();
		IBizBC bc = factory.getBc(CUST_BC);
		

		StCustBaseTDO resultTDO = null;
		
		StCustBaseTDO searchTDO = new StCustBaseTDO();
		searchTDO.setCustResiNo(custResiNo);

		DOList doList = bc.search(searchTDO, tx);

		if (doList != null && doList.size() > 0) 
		{
			resultTDO = (StCustBaseTDO)doList.get(0);
		}

		return resultTDO;
	}
	
	/**
	 * ISqlResult를 String Array 로 변환
	 * @param cr
	 * @return DOList
	 */
    public static DOList getDOList(ISqlResult cr) 
    {
        DOList doList = new DOList();
        String[] result = null;

        if ((cr != null) && (cr.getRowCount() > 0)) 
        {
            for (int i = 0; i < cr.getRowCount(); i++) 
            {
                result = new String[cr.getColumnCount()];

                for (int j = 0; j < cr.getColumnCount(); j++) 
                {
                	result[j] = cr.getQueryString(i, j);
                }

                doList.add(result);
            }
        }
        return doList;
    }
    /**
	 * <p>메소드명		: getGenCustNo </p>
	 * <p>설명			: 고객번호 채번 </p>
	 * <p>메소드인수1		: N/A </p>
	 * <p>메소드리턴값	: strCustNo 고객번호 </p>
	 * <p>예외처리		: N/A </p>
	 */
	public static String getGenCustNo(Event event) throws Exception 
	{
		ITransaction tx = event.getTransaction();

		String strSysDate = ServerSysDate.getServerDate(ServerSysDate.YYYYMMDD);     // 작성일자

		String strCustNo = GenerateKey.getNextNumber("S025", strSysDate, event.getEmpNo(), tx);

		return strCustNo;
	}  
	/**
	 * <p>메소드명		: setCustBaseInsert </p>
	 * <p>설명			: 고객기본 입력 </p>
	 * <p>메소드인수1		: stCustBaseTDO 고객정보 </p>
	 * <p>메소드리턴값	: N/A </p>
	 * <p>예외처리		: N/A </p>
	 */
	public static void setCustBaseInsert(StCustBaseTDO stCustBaseTDO, Event event) throws Exception 
	{
		ITransaction tx = event.getTransaction();

		IBizBC iBizBC = factory.getBc(CUST_BC);

		iBizBC.insert(stCustBaseTDO, tx);
	}
	/**
	 * <p>메소드명		: makeCustNo </p>
	 * <p>설명			: 고객번호 채번모듈</p>
	 * <p>메소드인수1		: HashMap (resiNo,custTpCd,custNm, custNo,corpRegNo) </p>
	 * <p>메소드리턴값	: String strCustNo </p>
	 * <p>예외처리		: N/A </p>
	 */
	public static String makeCustNo(HashMap<String, String> mkCustNoParam, Event event) throws Exception 
	{
		boolean corpYn = !"1".equals(mkCustNoParam.get("custTpCd")) ;
		String strCustNo = null;
		if(!corpYn){
			strCustNo = getCustNoByResiNo(mkCustNoParam.get("resiNo"), event);
		}else{
			strCustNo = getCustNoByBizNo(mkCustNoParam.get("resiNo"), event);
		}
		
		//1. 고객번호 조회
		if(mkCustNoParam.get("custNo") == null || "".equals(mkCustNoParam.get("custNo"))
			|| "NULL".equals(mkCustNoParam.get("custNo")) ||"nu ll".equals(mkCustNoParam.get("custNo"))){

			if (strCustNo == null || strCustNo.trim().length() == 0)
			{
				//고객번호가 없는 경우는 고객번호 채번, 고객기본에 신규등록
				strCustNo = getGenCustNo(event);
				//고객번호 설정
				mkCustNoParam.put("custNo", strCustNo);
				StCustBaseTDO stCustBaseTDO = new StCustBaseTDO();
				stCustBaseTDO.setCustNo(mkCustNoParam.get("custNo"));
				if(!corpYn){
					stCustBaseTDO.setCustResiNo(mkCustNoParam.get("resiNo"));
				}else{
					stCustBaseTDO.setBizNo(mkCustNoParam.get("resiNo"));
					stCustBaseTDO.setCorpRegNo(mkCustNoParam.get("corpRegNo"));
				}
		 		stCustBaseTDO.setCustTpCd(mkCustNoParam.get("custTpCd")); //S00176, 고객유형 : 개인
		 		stCustBaseTDO.setCustNm(mkCustNoParam.get("custNm"));
				LoanComm.setCustBaseInsert(stCustBaseTDO, event);
			}else{
				//고객번호 설정
				mkCustNoParam.put("custNo", strCustNo);
			}
		}
		return strCustNo;
	}
	
	
	/**
	 * <p>메소드명		: makeCustNoRtnCustNm </p>
	 * <p>설명			: 고객번호 채번모듈</p>
	 * <p>메소드인수1		: HashMap (resiNo,custTpCd,custNm, custNo,corpRegNo) </p>
	 * <p>메소드리턴값	: String strCustNo, strCustNm </p>
	 * <p>예외처리		: N/A </p>
	 */
	public static String[] makeCustNoRtnCustNm(HashMap<String, String> mkCustNoParam, Event event) throws Exception 
	{
		boolean corpYn = !"1".equals(mkCustNoParam.get("custTpCd")) ;
		
		String[] strCustNo = new String[3];
		
		//암호화705
		String repKey = "";
		if( !RUtil.isNull(mkCustNoParam.get("resiNo")) ){
			repKey = Util.EncDecResiNoString("I", mkCustNoParam.get("resiNo"));
			log.info("LoanComm.Class >> Util.EncDecResiNoString('I', "+mkCustNoParam.get("resiNo")+") ==>"+repKey);
		}
		  
		if(!corpYn){
			strCustNo = getCustNoByResiNoRtnCustNm(repKey, event);
		}else{
			strCustNo = getCustNoByBizNoRtnCustNm(mkCustNoParam.get("resiNo"), event);
		}
		
		//1. 고객번호 조회
		if(mkCustNoParam.get("custNo") == null || "".equals(mkCustNoParam.get("custNo"))
			|| "NULL".equals(mkCustNoParam.get("custNo")) ||"nu ll".equals(mkCustNoParam.get("custNo"))){

			if (strCustNo[0] == null || strCustNo[0].trim().length() == 0)
			{
				//고객번호가 없는 경우는 고객번호 채번, 고객기본에 신규등록
				strCustNo[0] = getGenCustNo(event);
				//고객번호 설정
				mkCustNoParam.put("custNo", strCustNo[0]);
				StCustBaseTDO stCustBaseTDO = new StCustBaseTDO();
				stCustBaseTDO.setCustNo(mkCustNoParam.get("custNo"));
				if(!corpYn){
					stCustBaseTDO.setCustResiNo(repKey);
				}else{
					stCustBaseTDO.setBizNo(mkCustNoParam.get("resiNo"));
					stCustBaseTDO.setCorpRegNo(mkCustNoParam.get("corpRegNo"));
				}
		 		stCustBaseTDO.setCustTpCd(mkCustNoParam.get("custTpCd")); //S00176, 고객유형 : 개인
		 		stCustBaseTDO.setCustNm(mkCustNoParam.get("custNm"));
		 		
				//실명번호로 CI,DI 취득 전문(73070) 호출
				if(!corpYn) {
					String resiNo = (String)mkCustNoParam.get("resiNo");
					String msg = "";
					
					if( !RUtil.isNull(resiNo) && resiNo.length() == 13 ){
						try{
							ITransaction tx = event.getTransaction();
							
							Nice73070RecvEntity recvEntity = new Nice73070RecvEntity(); 
							Nice73070SendEntity sendEntity = new Nice73070SendEntity();
							
							sendEntity.setIndv_div_cd("1");        //개인구분코드 
							sendEntity.setCust_resi_no(resiNo);  //주민번호
							sendEntity.setSrch_rsn_cd("10");         //조회사유코드
							
							Nice73070ServiceCC gramCC = new Nice73070ServiceCC();
							
							recvEntity = gramCC.procSend(sendEntity, tx);
							
							if( !RUtil.isNull(recvEntity.getRes_code()) && "P000".equals(recvEntity.getRes_code()) ) {
								stCustBaseTDO.setCi(recvEntity.getCi1());
								stCustBaseTDO.setDi(recvEntity.getDi());
							}else{
								msg = "[73070전문 CI, DI 취득] 장애발생 내용 : "+recvEntity.getRes_msg()
										+"\n재수행 방법은 고객관리화면에서 고객식별정보취득 버튼을 이용하시기 바랍니다.";
							}
						}catch(Exception e){
							e.printStackTrace();
							
							strCustNo[2] = msg;
							
							log.error("[73070전문 CI, DI 취득] 장애발생 : "+strCustNo[0]
									+"\n[실명번호] "+ mkCustNoParam.get("resiNo").substring(0, 7)+"******");
						}
					}
				}
				
		 		// 고객정보 DB Insert
				LoanComm.setCustBaseInsert(stCustBaseTDO, event);
				 
				strCustNo[1] = mkCustNoParam.get("custNm");
			}else{
				//고객번호 설정
				mkCustNoParam.put("custNo", strCustNo[0]);
			}
			
		}
		return strCustNo;
	}	
	
	/**
	 * <p>메소드명		: getCustNoByResiNoRtnCustNm </p>
	 * <p>설명			: 고객번호 조회 </p>
	 * <p>메소드인수1		: 주민번호</p>
	 * <p>메소드리턴값	: 고객번호,고객명</p>
	 */
	public static String[] getCustNoByResiNoRtnCustNm(String custResiNo, Event event) throws Exception
	{
		ITransaction tx = event.getTransaction();
		IBizBC bc = factory.getBc(CUST_BC);
		String[] strCustNo = new String[3];

		StCustBaseTDO searchTDO = new StCustBaseTDO();
		searchTDO.setCustResiNo(custResiNo);

		DOList doList = bc.search(searchTDO, tx);

		String aaa = "";
		String bbb = "";
		
		if (doList != null && doList.size() > 0) 
		{
			StCustBaseTDO resultTDO = (StCustBaseTDO)doList.get(0);
			strCustNo[0] = resultTDO.getCustNo().trim();		// 고객번호
			strCustNo[1] = resultTDO.getCustNm().trim();		// 고객명
		}
		
		return strCustNo;
	}
	
	/**
	 * <p>메소드명		: getCustNoByBizNoRtnCustNm </p>
	 * <p>설명			: 고객번호 조회 </p>
	 * <p>메소드인수1		: 사업자번호</p>
	 * <p>메소드리턴값	: 고객번호, 고객명</p>
	 */
	public static String[] getCustNoByBizNoRtnCustNm(String bizNo, Event event) throws Exception
	{
		ITransaction tx = event.getTransaction();
		IBizBC bc = factory.getBc(CUST_BC);
		String[] strCustNo = new String[3];

		StCustBaseTDO searchTDO = new StCustBaseTDO();
		//searchTDO.setCustResiNo(bizNo);
		searchTDO.setBizNo(bizNo);  // [LOTTE] 2013.10.18 LSH 법인 사업자번호

		DOList doList = bc.search(searchTDO, tx);

		if (doList != null && doList.size() > 0) 
		{
			StCustBaseTDO resultTDO = (StCustBaseTDO)doList.get(0);
			strCustNo[0] = resultTDO.getCustNo().trim();		// 고객번호
			strCustNo[1] = resultTDO.getCustNm().trim();		// 고객명
		}

		return strCustNo;
	}
	
	/**
	 * <p>메소드명		: setTat </p>
	 * <p>설명			: TATY 반영 </p>
	 * <p>메소드인수1		: 업무구분 : 모기지(M), 개인대출(P), 할부(A), 리스(L) </p>
	 * <p>메소드인수2		: 업무 단계 조회 구분자(ESTM, CNSL, CONT, LOAN) </p>
	 * <p>메소드인수3		: 대출진행상태코드(S04004) </p>
	 * <p>메소드인수4		: 견적번호 </p>
	 * <p>메소드인수5		: 상담번호 </p>
	 * <p>메소드인수6		: 계약번호 </p>
	 * <p>메소드인수7		: 대출번호 </p>
	 * <p>메소드인수8		: 대출순번 </p>
	 * <p>메소드인수9		: Event </p>
	 */
	public static void setTat(BSN_DIV bsnDiv, 
								BSN_STAG_DIV searchDiv, 
								String loanStatCd, 
								String estmNo,
								String cnslNo,
								String contNo,
								String loanNo, 
								String loanSeq, 
								Event event) throws BCException, Exception
	{
		ITransaction tx = event.getTransaction();

		String[] checkArr = new String[5];

		if (searchDiv == BSN_STAG_DIV.ESTM)
		{
			checkArr[0] = estmNo;
			checkArr[2] = "estm_no";	//[2] =  checkArr[0] 구분값 : estm_no:견적번호, cnsl_no:상담번호  cont_no:계약번호 loan_no:대출번호
		}
		else if (searchDiv == BSN_STAG_DIV.CNSL)
		{
			checkArr[0] = cnslNo;
			checkArr[2] = "cnsl_no";	//[2] =  checkArr[0] 구분값 : estm_no:견적번호, cnsl_no:상담번호  cont_no:계약번호 loan_no:대출번호
		}
		else if (searchDiv == BSN_STAG_DIV.CONT)
		{
			checkArr[0] = contNo;
			checkArr[2] = "cont_no";	//[2] =  checkArr[0] 구분값 : estm_no:견적번호, cnsl_no:상담번호  cont_no:계약번호 loan_no:대출번호
		}
		else if (searchDiv == BSN_STAG_DIV.LOAN)
		{
			checkArr[0] = loanNo;
			checkArr[2] = "loan_no";	//[2] =  checkArr[0] 구분값 : estm_no:견적번호, cnsl_no:상담번호  cont_no:계약번호 loan_no:대출번호
		}
		else
		{
			throw new Exception("입력 구분이 잘못되었습니다.\r\n" + LoanComm.class.getName());
		}

		checkArr[3] = loanStatCd;   

		if (bsnDiv == BSN_DIV.M)
		{
			checkArr[4] = "M"; //모기지
		}
		else if (bsnDiv == BSN_DIV.P)
		{
			checkArr[4] = "P"; //개인대출
		}
		else if (bsnDiv == BSN_DIV.A)
		{
			checkArr[4] = "A"; //할부
		}
		else if (bsnDiv == BSN_DIV.L)
		{
			checkArr[4] = "L"; //리스
		}
		else
		{
			throw new Exception("업무 구분이 잘못되었습니다.\r\n" + LoanComm.class.getName());
		}

		TatyObj tatyObj = new TatyObj();

		//견적번호
		if (estmNo != null && estmNo.trim().length() > 0)
		{
			tatyObj.estmNo = estmNo;
		}
		//상담번호
		if (cnslNo != null && cnslNo.trim().length() > 0)
		{
			tatyObj.cnslNo = cnslNo;
		}
		//계약번호
		if (contNo != null && contNo.trim().length() > 0)
		{
			tatyObj.contNo = contNo;
		}
		//대출번호
		if (loanNo != null && loanNo.trim().length() > 0)
		{
			tatyObj.loanNo = loanNo;
			tatyObj.loanSeq = loanSeq;
		}
		
		TatyCC.tatMain(checkArr, tatyObj, event, tx);
	}
}
