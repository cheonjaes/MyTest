package com.cbscap.util;

import kr.co.danal.rnd.TeleditClient;

import org.apache.log4j.Logger;

import com.cbscap.fw.util.format.ServerSysDate;
import com.cbscap.ilnk.entity.sci.SciHpCert1GramCC;
import com.cbscap.ilnk.entity.sci.SciHpCert1RecvEntity;
import com.cbscap.ilnk.entity.sci.SciHpCert1SendEntity;
import com.cbscap.ilnk.entity.sci.SciHpCert2GramCC;
import com.cbscap.ilnk.entity.sci.SciHpCert2RecvEntity;
import com.cbscap.ilnk.entity.sci.SciHpCert2SendEntity;
import com.cbscap.ilnk.entity.sci.SciHpCert3GramCC;
import com.cbscap.ilnk.entity.sci.SciHpCert3RecvEntity;
import com.cbscap.ilnk.entity.sci.SciHpCert3SendEntity;

/**
 * <p>내용       : 핸드폰 인증을 하기 위한 유틸</p>
 * <p>클래스명   : HpAuthUtil</p>
 * <p>특이사항   : 공통 유틸 클래스</p>
 */
public class HpAuthUtil {
    private static Logger log = Logger.getLogger(HpAuthUtil.class.getName());

    /**
     * <p>메소드명           : hpAuth</p>
     * <p>설명              : 사용자 인증을 받기 위해서 고객정보를 입력 받아서 결과값 리턴(SWIS)</p>
     * <p>메소드인수1        : String jumin - 주민번호</p>
     * <p>메소드인수2        : String hpCd - 통신사코드</p>
     * <p>메소드인수3        : String hpPhone - 핸드폰번호</p>
     * <p>메소드리턴값        : String - 결과값(0,1)</p>
     */
    public static String hpAuth(String jumin, String hpCd, String hpPhone) {

        String input = "";
        String output = "";
        /*parameter*/
        String CPCode = "B010011724"; /* 씨엑스씨캐피탈 ID */
        String CPPasswd = "wooricap"; /* 씨엑스씨캐피탈 PW */

        /*ItemInfo(상품정보) parameter*/
        String ItemType = "1"; /* 상품 Type - 실물:2 컨텐츠:1 (default "1") */
        String ItemAmount = "0"; /* 인증금액 (CP측 부담 인증일 경우에는 "0") */
        String ItemCount = "1"; /* 상품개수 (default "1") */
        String ItemCode = "12S0Ro0000";
                          /* 상품코드 (다날에서 발급받은 상품코드) CPCODE.xls참조 */
        String ItemName = "ItemName"; /* 상품명 (임의로 입력)*/

        TeleditClient teledit = new TeleditClient(
                "/TMAX/jeus42/webhome/servlet_home/webapps/swis/WEB-INF/classes/");

        try {
            input = "ID=" + CPCode // CPID
                    + ";PWD=" + CPPasswd // Password
                    + ";ItemType=Amount" // 상품Type("Amount"만 이용)
                    + ";ItemCount=1" // 상품개수("1"만 이용)
                    + ";ItemInfo=" + ItemType + "|" + ItemAmount + "|" +
                    ItemCount + "|" + ItemCode + "|" + ItemName // 상품정보(<ItemType>'|'<ItemAmount>'|'<ItemCount>'|'<ItemCode>'|'<ItemName>)
                    + ";SERVICE=TELEDIT" // TELEDIT: 핸드폰 결제(인증)
                    + ";DstAddr=" + hpPhone // 전화번호
                    + ";Iden=" + jumin // 주민번호
                    + ";Carrier=" + hpCd; // 통신사(SKT, KTF, LGT)

            output = teledit.simDeliver(input);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("사용자인증에러:" + e.getMessage());
        }

        return output;
    }


    public static String hpAuthSWCS(String jumin, String hpCd, String hpPhone) {
        String input = "";
        String output = "";
        /*parameter*/
        String CPCode = "B010011724"; /* 씨엑스씨캐피탈 ID */
        String CPPasswd = "wooricap"; /* 씨엑스씨캐피탈 PW */

        /*ItemInfo(상품정보) parameter*/
        String ItemType = "1"; /* 상품 Type - 실물:2 컨텐츠:1 (default "1") */
        String ItemAmount = "0"; /* 인증금액 (CP측 부담 인증일 경우에는 "0") */
        String ItemCount = "1"; /* 상품개수 (default "1") */
        String ItemCode = "12S0Ro0000";
                          /* 상품코드 (다날에서 발급받은 상품코드) CPCODE.xls참조 */
        String ItemName = "ItemName"; /* 상품명 (임의로 입력)*/

        TeleditClient teledit = new TeleditClient(
                "/TMAX/jeus5/lib/application/");

        try {
            input = "ID=" + CPCode // CPID
                    + ";PWD=" + CPPasswd // Password
                    + ";ItemType=Amount" // 상품Type("Amount"만 이용)
                    + ";ItemCount=1" // 상품개수("1"만 이용)
                    + ";ItemInfo=" + ItemType + "|" + ItemAmount + "|" +
                    ItemCount + "|" + ItemCode + "|" + ItemName // 상품정보(<ItemType>'|'<ItemAmount>'|'<ItemCount>'|'<ItemCode>'|'<ItemName>)
                    + ";SERVICE=TELEDIT" // TELEDIT: 핸드폰 결제(인증)
                    + ";DstAddr=" + hpPhone // 전화번호
                    + ";Iden=" + jumin // 주민번호
                    + ";Carrier=" + hpCd; // 통신사(SKT, KTF, LGT)

            output = teledit.simDeliver(input);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("사용자인증에러:" + e.getMessage());
        }

        return output;
    }

    /**
     * <p>메소드명           : hpAuthConfirm</p>
     * <p>설명              : 고객의 인증번호가 맞는지 확인하기 위해서 인증번호를 입력 받아서 결과값 리턴(SWIS)</p>
     * <p>메소드인수1        : String ServerInfo - 거래 unique key</p>
     * <p>메소드인수2        : String OTP - 사용자가 입력한 인증 번호</p>
     * <p>메소드리턴값        : String - 결과값(0,1)</p>
     */
    public static String hpAuthConfirm(String ServerInfo, String OTP) {

        String input = "";
        String output = "";

        TeleditClient teledit = new TeleditClient(
                "/TMAX/jeus42/webhome/servlet_home/webapps/swis/WEB-INF/classes/");

        try {
            input = "ServerInfo=" + ServerInfo // 거래 unique key
                    + ";OTP=" + OTP; // 사용자가 입력한 인증번호

            output = teledit.simBill(input);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("인증에러:" + e.getMessage());
        }

        return output;
    }

    public static String hpAuthConfirmSWCS(String ServerInfo, String OTP) {
        String input = "";
        String output = "";

        TeleditClient teledit = new TeleditClient(
                "/TMAX/jeus5/lib/application/");

        try {
            input = "ServerInfo=" + ServerInfo // 거래 unique key
                    + ";OTP=" + OTP; // 사용자가 입력한 인증번호

            output = teledit.simBill(input);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("인증에러:" + e.getMessage());
        }
        return output;
    }

    /**
     * <p>메소드명           : hpAuthCprtPlc</p>
     * <p>설명              : 사용자 인증을 받기 위해서 고객정보를 입력 받아서 결과값 리턴(제휴광장)</p>
     * <p>메소드인수1        : String jumin - 주민번호</p>
     * <p>메소드인수2        : String hpCd - 통신사코드</p>
     * <p>메소드인수3        : String hpPhone - 핸드폰번호</p>
     * <p>메소드리턴값        : String - 결과값(0,1)</p>
     */
    public static String hpAuthCprtPlc(String jumin, String hpCd,
                                       String hpPhone) {

        String input = "";
        String output = "";
        /*parameter*/
        String CPCode = "B010011724"; /* 씨엑스씨캐피탈 ID */
        String CPPasswd = "wooricap"; /* 씨엑스씨캐피탈 PW */

        /*ItemInfo(상품정보) parameter*/
        String ItemType = "1"; /* 상품 Type - 실물:2 컨텐츠:1 (default "1") */
        String ItemAmount = "0"; /* 인증금액 (CP측 부담 인증일 경우에는 "0") */
        String ItemCount = "1"; /* 상품개수 (default "1") */
        String ItemCode = "12S0Ro0000";
                          /* 상품코드 (다날에서 발급받은 상품코드) CPCODE.xls참조 */
        String ItemName = "ItemName"; /* 상품명 (임의로 입력)*/

        TeleditClient teledit = new TeleditClient(
                "/TMAX/jeus42/webhome/servlet_home/webapps/cprtplc/WEB-INF/classes/");

        try {
            input = "ID=" + CPCode // CPID
                    + ";PWD=" + CPPasswd // Password
                    + ";ItemType=Amount" // 상품Type("Amount"만 이용)
                    + ";ItemCount=1" // 상품개수("1"만 이용)
                    + ";ItemInfo=" + ItemType + "|" + ItemAmount + "|" +
                    ItemCount + "|" + ItemCode + "|" + ItemName // 상품정보(<ItemType>'|'<ItemAmount>'|'<ItemCount>'|'<ItemCode>'|'<ItemName>)
                    + ";SERVICE=TELEDIT" // TELEDIT: 핸드폰 결제(인증)
                    + ";DstAddr=" + hpPhone // 전화번호
                    + ";Iden=" + jumin // 주민번호
                    + ";Carrier=" + hpCd; // 통신사(SKT, KTF, LGT)

            output = teledit.simDeliver(input);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("사용자인증에러:" + e.getMessage());
        }

        return output;
    }

    /**
     * <p>메소드명           : hpAuthConfirmCprtPlc</p>
     * <p>설명              : 고객의 인증번호가 맞는지 확인하기 위해서 인증번호를 입력 받아서 결과값 리턴(제휴광장)</p>
     * <p>메소드인수1        : String ServerInfo - 거래 unique key</p>
     * <p>메소드인수2        : String OTP - 사용자가 입력한 인증 번호</p>
     * <p>메소드리턴값        : String - 결과값(0,1)</p>
     */
    public static String hpAuthConfirmCprtPlc(String ServerInfo, String OTP) {

        String input = "";
        String output = "";

        TeleditClient teledit = new TeleditClient(
                "/TMAX/jeus42/webhome/servlet_home/webapps/cprtplc/WEB-INF/classes/");

        try {
            input = "ServerInfo=" + ServerInfo // 거래 unique key
                    + ";OTP=" + OTP; // 사용자가 입력한 인증번호

            output = teledit.simBill(input);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("인증에러:" + e.getMessage());
        }

        return output;
    }

    public static void main(String[] args) {
        // System.out.println("getComma : " + Util.getComma("-234567890.01"));
    }
    
    /**
     * <p>메소드명           : hpAuthSCI</p>
     * <p>설명              : 사용자 인증을 받기 위해서 고객정보를 입력 받아서 결과값 리턴(SCI : 서울신용평가정보)</p>
     * <p>메소드인수0        : String jumin - 주민번호</p>
     * <p>메소드인수1        : String custNm - 고객명</p>
     * <p>메소드인수2        : String hpCd - 통신사코드</p>
     * <p>메소드인수3        : String hpPhone - 핸드폰번호</p>
     * <p>메소드인수4        : String eaiSnum - EAI 일련번호</p>
     * <p>메소드리턴값        : String - 결과값(0,1)</p>
     */
	public static String[] hpAuthSCI(String[] param) {

		String[] output = null;
		// 요청전문
    	SciHpCert1GramCC cert1Gram = new SciHpCert1GramCC();
    	SciHpCert1SendEntity cert1SendEntity = new SciHpCert1SendEntity();
    	SciHpCert1RecvEntity cert1RecvEntity = new SciHpCert1RecvEntity();
    	
		try {
			setCert1Entity(cert1SendEntity, param);
			cert1RecvEntity = cert1Gram.procSend(cert1SendEntity);
			output = convertCert1RecvEntityToArry(cert1RecvEntity);
		} catch (Exception e) {
            e.printStackTrace();
            log.info("사용자인증에러:" + e.getMessage());
        }
    	
    	return output;
    }

	/**
     * <p>메소드명           : hpAuthSCI</p>
     * <p>설명              : 사용자 인증을 받기 위해서 고객정보를 입력 받아서 결과값 리턴(SCI : 서울신용평가정보)</p>
     * <p>메소드인수0        : String jumin - 주민번호</p>
     * <p>메소드인수1        : String custNm - 고객명</p>
     * <p>메소드인수2        : String hpCd - 통신사코드</p>
     * <p>메소드인수3        : String hpPhone - 핸드폰번호</p>
     * <p>메소드인수4        : String eaiSnum - EAI 일련번호</p>
     * <p>메소드인수5        : String selfCnfmSnum - 본인확인순번</p>
     * <p>메소드인수6        : String authSnum - 인증순번</p>
     * <p>메소드인수7        : String askNo - 요청번호</p>
     * <p>메소드리턴값        : String - 결과값(0,1)</p>
     */
	public static String[] hpAuthSCILMS(String[] param) {

		String[] output = null;
    	// LMS재전송전문
    	SciHpCert2GramCC cert2Gram = new SciHpCert2GramCC();
    	SciHpCert2SendEntity cert2SendEntity = new SciHpCert2SendEntity();
    	SciHpCert2RecvEntity cert2RecvEntity = new SciHpCert2RecvEntity();
    	
		try {
			setCert2Entity(cert2SendEntity, param);
			cert2RecvEntity = cert2Gram.procSend(cert2SendEntity);
			output = convertCert2RecvEntityToArry(cert2RecvEntity);
		} catch (Exception e) {
            e.printStackTrace();
            log.info("사용자인증에러:" + e.getMessage());
        }
    	
    	return output;
    }
	
	/**
     * <p>메소드명           : setCert1Entity</p>
     * <p>설명              : 서신평 사용자인증을 위한 송신전문 정보 세팅</p>
     */    
    public static void setCert1Entity(SciHpCert1SendEntity certEntity, String[] param){
    	certEntity.setMco_id       ("SXLT020");
    	certEntity.setHdr_doc_cd   ("0200");
    	certEntity.setHdr_doc_id   ("340");
    	certEntity.setRsp_cd       ("");
    	certEntity.setSno          (param[4]);
    	certEntity.setRsdt_rg_no   (param[0]);
    	certEntity.setSvc_no       ("001001");
    	certEntity.setAk_no        (ServerSysDate.getServerDate(ServerSysDate.YYYYMMDDHHMMSS)+"001");
    	certEntity.setPn           (param[1]);
    	certEntity.setMmt_tcc_co_dc(param[2]);
    	certEntity.setPhon         (param[3]);
    	certEntity.setAk_dtti      (ServerSysDate.getServerDate(ServerSysDate.YYYYMMDDHHMMSS));
    }
    
    /**
     * <p>메소드명           : setCert2Entity</p>
     * <p>설명              : LMS 재전송 정보 세팅</p>
     */    
    public static void setCert2Entity(SciHpCert2SendEntity certEntity, String[] param){
    	certEntity.setMco_id       ("SXLT020");
    	certEntity.setHdr_doc_cd   ("0200");
    	certEntity.setHdr_doc_id   ("341");
    	certEntity.setRsp_cd       ("");
    	certEntity.setSno          (param[4]);
    	certEntity.setRsdt_rg_no   (param[0]);
    	certEntity.setSvc_no       ("001001");
    	certEntity.setAk_no        (param[7]);
    	certEntity.setPn           (param[1]);
    	certEntity.setMmt_tcc_co_dc(param[2]);
    	certEntity.setPhon         (param[3]);
    	certEntity.setHs_vd_sno    (param[5]);
    	certEntity.setCtf_sno      (param[6]);
    }
    
    /**
     * <p>메소드명           : setCert3Entity</p>
     * <p>설명              : LMS 재전송 정보 세팅</p>
     */    
    public static void setCert3Entity(SciHpCert3SendEntity certEntity, String[] arrParam){
    	certEntity.setMco_id       ("SXLT020");
    	certEntity.setHdr_doc_cd   ("0200");
    	certEntity.setHdr_doc_id   ("342");
    	certEntity.setRsp_cd       ("");
    	certEntity.setSno          (arrParam[16]); // 순번
    	certEntity.setRsdt_rg_no   (arrParam[11]); // 주민번호
    	certEntity.setSvc_no       ("001001"); // 서비스번호
    	certEntity.setAk_no        (ServerSysDate.getServerDate(ServerSysDate.YYYYMMDDHHMMSS)+"001"); // 요청번호
    	certEntity.setPn           (arrParam[1]); // 성명
    	certEntity.setMmt_tcc_co_dc(arrParam[3]); // 이통사구분
    	certEntity.setPhon         (arrParam[4].trim()+arrParam[5].trim()+arrParam[6].trim()); // 휴대폰번호
    	certEntity.setHs_vd_sno    (arrParam[13]); // 본인확인순번 !
    	certEntity.setCtf_sno      (arrParam[14]); // 인증순번 !
    	certEntity.setLms_ctf_no   (arrParam[8]); // 인증번호
    	certEntity.setRtrs_sno     (arrParam[15]); // 재전송순번 !
    	certEntity.setAk_dtti      (arrParam[12]); // 요청일시 !
    }

    /**
     * <p>메소드명           : convertCert1RecvEntityToArry</p>
     * <p>설명              : return 하기위해 수신전문객체를 arry 에 담음</p>
     */    
    public static String[] convertCert1RecvEntityToArry(SciHpCert1RecvEntity recvEntity){
    	String[] output = new String[6];
    	output[0] = recvEntity.getRs_v();           // 결과값         
		output[1] = recvEntity.getPhon_ctf_rc();    // 휴대폰인증결과코드   
		output[2] = recvEntity.getHs_vd_sno();      // 본인확인순번
		output[3] = recvEntity.getCtf_sno();        // 인증순번
		output[4] = recvEntity.getAk_no();          // 요청번호
		output[5] = recvEntity.getAk_dtti();        // 요청일시
		return output;
    }
    
    /**
     * <p>메소드명           : hpAuthConfirmSCI</p>
     * <p>설명              : 고객의 인증번호가 맞는지 확인하기 위해서 인증번호를 입력 받아서 결과값 리턴</p>
     * <p>메소드인수0       : 고객번호      
     * <p>메소드인수1       : 고객명       
     * <p>메소드인수2       : 고객유형코드    
     * <p>메소드인수3       : 이동통신사     
     * <p>메소드인수4       : 핸드폰번호1    
     * <p>메소드인수5       : 핸드폰번호2    
     * <p>메소드인수6       : 핸드폰번호3    
     * <p>메소드인수7       : 거래고유번호    
     * <p>메소드인수8       : 인증번호      
     * <p>메소드인수9       : 사용자인증결과코드 
     * <p>메소드인수10      : 사용자인증결과메시지
     * <p>메소드인수11      : 주민등록번호
     * <p>메소드인수12      : 요청일시   
     * <p>메소드인수13      : 본인확인순번  
     * <p>메소드인수14      : 인증순번    
     * <p>메소드인수15      : 재전송순번 
     * <p>메소드인수16      : EAI 순번 
     * <p>메소드리턴값        : String[] - 결과값</p>
     */
	public static String[] hpAuthConfirmSCI(String[] arrParam) {

		String[] output = null;

		SciHpCert3GramCC certGram = new SciHpCert3GramCC();
		SciHpCert3SendEntity certSendEntity = new SciHpCert3SendEntity();
		SciHpCert3RecvEntity certRecvEntity = new SciHpCert3RecvEntity();

		try {
			setCert3Entity(certSendEntity, arrParam);
			certRecvEntity = certGram.procSend(certSendEntity);
			output = convertCert3RecvEntityToArry(certRecvEntity);

		} catch (Exception e) {
			e.printStackTrace();
			log.info("사용자인증에러:" + e.getMessage());
		}

		return output;
	}
    
    /**
     * <p>메소드명           : convertCert2RecvEntityToArry</p>
     * <p>설명              : return 하기위해 수신전문객체를 arry 에 담음</p>
     */    
    public static String[] convertCert2RecvEntityToArry(SciHpCert2RecvEntity recvEntity){
    	String[] output = new String[6];
    	output[0] = recvEntity.getHs_vd_sno();      // 본인확인순번         
		output[1] = recvEntity.getPhon_ctf_rc();    // 휴대폰인증결과코드   
		output[2] = recvEntity.getHs_vd_sno();      // 본인확인순번
		output[3] = recvEntity.getCtf_no();         // 인증순번
		output[4] = recvEntity.getAk_no();          // 요청번호
		output[5] = recvEntity.getLms_fw_sno();     // LMS 발송순번
		return output;
    }
    
    /**
     * <p>메소드명           : convertCert3RecvEntityToArry</p>
     * <p>설명              : return 하기위해 수신전문객체를 arry 에 담음</p>
     */    
    public static String[] convertCert3RecvEntityToArry(SciHpCert3RecvEntity recvEntity){
    	String[] output = new String[5];
    	output[0] = recvEntity.getCtf_vd_rc();      // 인증번호확인결과    
		output[1] = recvEntity.getPhon_ctf_rc();    // 휴대폰인증결과코드   
		output[2] = recvEntity.getHs_vd_sno();      // 본인확인순번
//		output[3] = recvEntity.getCtf_sno();        // 인증순번
		output[4] = recvEntity.getAk_no();          // 요청번호
		return output;
    }
}
