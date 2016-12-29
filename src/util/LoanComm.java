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
 * ���� ����
 * �ۼ��� : 2012.05.27
 * @author KiNG
 */
public class LoanComm
{
	
	private static CommonBCFactory factory = new CommonBCFactory();
	private static Logger log = Logger.getLogger(LoanComm.class.getName());
	private final static String CUST_BC ="com.cbscap.bc.s.cs.cs.CustBC"; //������ BC
	
	/**
	 * ���� �ܰ� ��ȸ ������
	 */
	public enum BSN_STAG_DIV 
	{
		ESTM	// ����(����)
		,CNSL	// ���
		,CONT	// ���(����)
		,LOAN	// ��û(����)
	}
	
	/**
	 * �������� : �����(M), ���δ���(P), �Һ�(A), ����(L)
	 */
	public enum BSN_DIV
	{
		M //�����
		,P //���δ���
		,A //�Һ�
		,L //����
	}
	
	/**
	 * <p>�޼ҵ��		: getCustExistFgByResiNo </p>
	 * <p>����			: ���⺻�� ���翩�� </p>
	 * <p>�޼ҵ��μ�1		: custResiNo �ֹι�ȣ </p>
	 * <p>�޼ҵ帮�ϰ�	: ���翩��  </p>
	 * <p>����ó��		: BCException</p>
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
	 * <p>�޼ҵ��		: getCustNoByResiNo </p>
	 * <p>����			: ����ȣ ��ȸ </p>
	 * <p>�޼ҵ��μ�1		: �ֹι�ȣ</p>
	 * <p>�޼ҵ帮�ϰ�	: ����ȣ</p>
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
	 * <p>�޼ҵ��		: getCustNoByBizNo </p>
	 * <p>����			: ����ȣ ��ȸ </p>
	 * <p>�޼ҵ��μ�1		: ����ڹ�ȣ</p>
	 * <p>�޼ҵ帮�ϰ�	: ����ȣ</p>
	 */
	public static String getCustNoByBizNo(String bizNo, Event event) throws Exception
	{
		ITransaction tx = event.getTransaction();
		IBizBC bc = factory.getBc(CUST_BC);
		String strCustNo = "";

		StCustBaseTDO searchTDO = new StCustBaseTDO();
		//searchTDO.setCustResiNo(bizNo);
		searchTDO.setBizNo(bizNo);  // [LOTTE] 2013.10.18 LSH ���� ����ڹ�ȣ

		DOList doList = bc.search(searchTDO, tx);

		if (doList != null && doList.size() > 0) 
		{
			StCustBaseTDO resultTDO = (StCustBaseTDO)doList.get(0);
			strCustNo = resultTDO.getCustNo().trim();
		}

		return strCustNo;
	}
	/**
	 * <p>�޼ҵ��		: getResiNoByCustNo </p>
	 * <p>����			: �ֹι�ȣ ��ȸ </p>
	 * <p>�޼ҵ��μ�1		: ����ȣ</p>
	 * <p>�޼ҵ帮�ϰ�	: �ֹι�ȣ</p>
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
	 * <p>�޼ҵ��		: setCustBaseByResiNo</p>
	 * <p>����			: ����ȣ ��ȸ</p>
	 * <p>�޼ҵ��μ�1		: �ֹι�ȣ</p>
	 * <p>�޼ҵ帮�ϰ�	: ����ȣ</p>
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
	 * ISqlResult�� String Array �� ��ȯ
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
	 * <p>�޼ҵ��		: getGenCustNo </p>
	 * <p>����			: ����ȣ ä�� </p>
	 * <p>�޼ҵ��μ�1		: N/A </p>
	 * <p>�޼ҵ帮�ϰ�	: strCustNo ����ȣ </p>
	 * <p>����ó��		: N/A </p>
	 */
	public static String getGenCustNo(Event event) throws Exception 
	{
		ITransaction tx = event.getTransaction();

		String strSysDate = ServerSysDate.getServerDate(ServerSysDate.YYYYMMDD);     // �ۼ�����

		String strCustNo = GenerateKey.getNextNumber("S025", strSysDate, event.getEmpNo(), tx);

		return strCustNo;
	}  
	/**
	 * <p>�޼ҵ��		: setCustBaseInsert </p>
	 * <p>����			: ���⺻ �Է� </p>
	 * <p>�޼ҵ��μ�1		: stCustBaseTDO ������ </p>
	 * <p>�޼ҵ帮�ϰ�	: N/A </p>
	 * <p>����ó��		: N/A </p>
	 */
	public static void setCustBaseInsert(StCustBaseTDO stCustBaseTDO, Event event) throws Exception 
	{
		ITransaction tx = event.getTransaction();

		IBizBC iBizBC = factory.getBc(CUST_BC);

		iBizBC.insert(stCustBaseTDO, tx);
	}
	/**
	 * <p>�޼ҵ��		: makeCustNo </p>
	 * <p>����			: ����ȣ ä�����</p>
	 * <p>�޼ҵ��μ�1		: HashMap (resiNo,custTpCd,custNm, custNo,corpRegNo) </p>
	 * <p>�޼ҵ帮�ϰ�	: String strCustNo </p>
	 * <p>����ó��		: N/A </p>
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
		
		//1. ����ȣ ��ȸ
		if(mkCustNoParam.get("custNo") == null || "".equals(mkCustNoParam.get("custNo"))
			|| "NULL".equals(mkCustNoParam.get("custNo")) ||"nu ll".equals(mkCustNoParam.get("custNo"))){

			if (strCustNo == null || strCustNo.trim().length() == 0)
			{
				//����ȣ�� ���� ���� ����ȣ ä��, ���⺻�� �űԵ��
				strCustNo = getGenCustNo(event);
				//����ȣ ����
				mkCustNoParam.put("custNo", strCustNo);
				StCustBaseTDO stCustBaseTDO = new StCustBaseTDO();
				stCustBaseTDO.setCustNo(mkCustNoParam.get("custNo"));
				if(!corpYn){
					stCustBaseTDO.setCustResiNo(mkCustNoParam.get("resiNo"));
				}else{
					stCustBaseTDO.setBizNo(mkCustNoParam.get("resiNo"));
					stCustBaseTDO.setCorpRegNo(mkCustNoParam.get("corpRegNo"));
				}
		 		stCustBaseTDO.setCustTpCd(mkCustNoParam.get("custTpCd")); //S00176, ������ : ����
		 		stCustBaseTDO.setCustNm(mkCustNoParam.get("custNm"));
				LoanComm.setCustBaseInsert(stCustBaseTDO, event);
			}else{
				//����ȣ ����
				mkCustNoParam.put("custNo", strCustNo);
			}
		}
		return strCustNo;
	}
	
	
	/**
	 * <p>�޼ҵ��		: makeCustNoRtnCustNm </p>
	 * <p>����			: ����ȣ ä�����</p>
	 * <p>�޼ҵ��μ�1		: HashMap (resiNo,custTpCd,custNm, custNo,corpRegNo) </p>
	 * <p>�޼ҵ帮�ϰ�	: String strCustNo, strCustNm </p>
	 * <p>����ó��		: N/A </p>
	 */
	public static String[] makeCustNoRtnCustNm(HashMap<String, String> mkCustNoParam, Event event) throws Exception 
	{
		boolean corpYn = !"1".equals(mkCustNoParam.get("custTpCd")) ;
		
		String[] strCustNo = new String[3];
		
		//��ȣȭ705
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
		
		//1. ����ȣ ��ȸ
		if(mkCustNoParam.get("custNo") == null || "".equals(mkCustNoParam.get("custNo"))
			|| "NULL".equals(mkCustNoParam.get("custNo")) ||"nu ll".equals(mkCustNoParam.get("custNo"))){

			if (strCustNo[0] == null || strCustNo[0].trim().length() == 0)
			{
				//����ȣ�� ���� ���� ����ȣ ä��, ���⺻�� �űԵ��
				strCustNo[0] = getGenCustNo(event);
				//����ȣ ����
				mkCustNoParam.put("custNo", strCustNo[0]);
				StCustBaseTDO stCustBaseTDO = new StCustBaseTDO();
				stCustBaseTDO.setCustNo(mkCustNoParam.get("custNo"));
				if(!corpYn){
					stCustBaseTDO.setCustResiNo(repKey);
				}else{
					stCustBaseTDO.setBizNo(mkCustNoParam.get("resiNo"));
					stCustBaseTDO.setCorpRegNo(mkCustNoParam.get("corpRegNo"));
				}
		 		stCustBaseTDO.setCustTpCd(mkCustNoParam.get("custTpCd")); //S00176, ������ : ����
		 		stCustBaseTDO.setCustNm(mkCustNoParam.get("custNm"));
		 		
				//�Ǹ��ȣ�� CI,DI ��� ����(73070) ȣ��
				if(!corpYn) {
					String resiNo = (String)mkCustNoParam.get("resiNo");
					String msg = "";
					
					if( !RUtil.isNull(resiNo) && resiNo.length() == 13 ){
						try{
							ITransaction tx = event.getTransaction();
							
							Nice73070RecvEntity recvEntity = new Nice73070RecvEntity(); 
							Nice73070SendEntity sendEntity = new Nice73070SendEntity();
							
							sendEntity.setIndv_div_cd("1");        //���α����ڵ� 
							sendEntity.setCust_resi_no(resiNo);  //�ֹι�ȣ
							sendEntity.setSrch_rsn_cd("10");         //��ȸ�����ڵ�
							
							Nice73070ServiceCC gramCC = new Nice73070ServiceCC();
							
							recvEntity = gramCC.procSend(sendEntity, tx);
							
							if( !RUtil.isNull(recvEntity.getRes_code()) && "P000".equals(recvEntity.getRes_code()) ) {
								stCustBaseTDO.setCi(recvEntity.getCi1());
								stCustBaseTDO.setDi(recvEntity.getDi());
							}else{
								msg = "[73070���� CI, DI ���] ��ֹ߻� ���� : "+recvEntity.getRes_msg()
										+"\n����� ����� ������ȭ�鿡�� ���ĺ�������� ��ư�� �̿��Ͻñ� �ٶ��ϴ�.";
							}
						}catch(Exception e){
							e.printStackTrace();
							
							strCustNo[2] = msg;
							
							log.error("[73070���� CI, DI ���] ��ֹ߻� : "+strCustNo[0]
									+"\n[�Ǹ��ȣ] "+ mkCustNoParam.get("resiNo").substring(0, 7)+"******");
						}
					}
				}
				
		 		// ������ DB Insert
				LoanComm.setCustBaseInsert(stCustBaseTDO, event);
				 
				strCustNo[1] = mkCustNoParam.get("custNm");
			}else{
				//����ȣ ����
				mkCustNoParam.put("custNo", strCustNo[0]);
			}
			
		}
		return strCustNo;
	}	
	
	/**
	 * <p>�޼ҵ��		: getCustNoByResiNoRtnCustNm </p>
	 * <p>����			: ����ȣ ��ȸ </p>
	 * <p>�޼ҵ��μ�1		: �ֹι�ȣ</p>
	 * <p>�޼ҵ帮�ϰ�	: ����ȣ,����</p>
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
			strCustNo[0] = resultTDO.getCustNo().trim();		// ����ȣ
			strCustNo[1] = resultTDO.getCustNm().trim();		// ����
		}
		
		return strCustNo;
	}
	
	/**
	 * <p>�޼ҵ��		: getCustNoByBizNoRtnCustNm </p>
	 * <p>����			: ����ȣ ��ȸ </p>
	 * <p>�޼ҵ��μ�1		: ����ڹ�ȣ</p>
	 * <p>�޼ҵ帮�ϰ�	: ����ȣ, ����</p>
	 */
	public static String[] getCustNoByBizNoRtnCustNm(String bizNo, Event event) throws Exception
	{
		ITransaction tx = event.getTransaction();
		IBizBC bc = factory.getBc(CUST_BC);
		String[] strCustNo = new String[3];

		StCustBaseTDO searchTDO = new StCustBaseTDO();
		//searchTDO.setCustResiNo(bizNo);
		searchTDO.setBizNo(bizNo);  // [LOTTE] 2013.10.18 LSH ���� ����ڹ�ȣ

		DOList doList = bc.search(searchTDO, tx);

		if (doList != null && doList.size() > 0) 
		{
			StCustBaseTDO resultTDO = (StCustBaseTDO)doList.get(0);
			strCustNo[0] = resultTDO.getCustNo().trim();		// ����ȣ
			strCustNo[1] = resultTDO.getCustNm().trim();		// ����
		}

		return strCustNo;
	}
	
	/**
	 * <p>�޼ҵ��		: setTat </p>
	 * <p>����			: TATY �ݿ� </p>
	 * <p>�޼ҵ��μ�1		: �������� : �����(M), ���δ���(P), �Һ�(A), ����(L) </p>
	 * <p>�޼ҵ��μ�2		: ���� �ܰ� ��ȸ ������(ESTM, CNSL, CONT, LOAN) </p>
	 * <p>�޼ҵ��μ�3		: ������������ڵ�(S04004) </p>
	 * <p>�޼ҵ��μ�4		: ������ȣ </p>
	 * <p>�޼ҵ��μ�5		: ����ȣ </p>
	 * <p>�޼ҵ��μ�6		: ����ȣ </p>
	 * <p>�޼ҵ��μ�7		: �����ȣ </p>
	 * <p>�޼ҵ��μ�8		: ������� </p>
	 * <p>�޼ҵ��μ�9		: Event </p>
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
			checkArr[2] = "estm_no";	//[2] =  checkArr[0] ���а� : estm_no:������ȣ, cnsl_no:����ȣ  cont_no:����ȣ loan_no:�����ȣ
		}
		else if (searchDiv == BSN_STAG_DIV.CNSL)
		{
			checkArr[0] = cnslNo;
			checkArr[2] = "cnsl_no";	//[2] =  checkArr[0] ���а� : estm_no:������ȣ, cnsl_no:����ȣ  cont_no:����ȣ loan_no:�����ȣ
		}
		else if (searchDiv == BSN_STAG_DIV.CONT)
		{
			checkArr[0] = contNo;
			checkArr[2] = "cont_no";	//[2] =  checkArr[0] ���а� : estm_no:������ȣ, cnsl_no:����ȣ  cont_no:����ȣ loan_no:�����ȣ
		}
		else if (searchDiv == BSN_STAG_DIV.LOAN)
		{
			checkArr[0] = loanNo;
			checkArr[2] = "loan_no";	//[2] =  checkArr[0] ���а� : estm_no:������ȣ, cnsl_no:����ȣ  cont_no:����ȣ loan_no:�����ȣ
		}
		else
		{
			throw new Exception("�Է� ������ �߸��Ǿ����ϴ�.\r\n" + LoanComm.class.getName());
		}

		checkArr[3] = loanStatCd;   

		if (bsnDiv == BSN_DIV.M)
		{
			checkArr[4] = "M"; //�����
		}
		else if (bsnDiv == BSN_DIV.P)
		{
			checkArr[4] = "P"; //���δ���
		}
		else if (bsnDiv == BSN_DIV.A)
		{
			checkArr[4] = "A"; //�Һ�
		}
		else if (bsnDiv == BSN_DIV.L)
		{
			checkArr[4] = "L"; //����
		}
		else
		{
			throw new Exception("���� ������ �߸��Ǿ����ϴ�.\r\n" + LoanComm.class.getName());
		}

		TatyObj tatyObj = new TatyObj();

		//������ȣ
		if (estmNo != null && estmNo.trim().length() > 0)
		{
			tatyObj.estmNo = estmNo;
		}
		//����ȣ
		if (cnslNo != null && cnslNo.trim().length() > 0)
		{
			tatyObj.cnslNo = cnslNo;
		}
		//����ȣ
		if (contNo != null && contNo.trim().length() > 0)
		{
			tatyObj.contNo = contNo;
		}
		//�����ȣ
		if (loanNo != null && loanNo.trim().length() > 0)
		{
			tatyObj.loanNo = loanNo;
			tatyObj.loanSeq = loanSeq;
		}
		
		TatyCC.tatMain(checkArr, tatyObj, event, tx);
	}
}
