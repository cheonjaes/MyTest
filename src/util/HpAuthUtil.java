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
 * <p>����       : �ڵ��� ������ �ϱ� ���� ��ƿ</p>
 * <p>Ŭ������   : HpAuthUtil</p>
 * <p>Ư�̻���   : ���� ��ƿ Ŭ����</p>
 */
public class HpAuthUtil {
    private static Logger log = Logger.getLogger(HpAuthUtil.class.getName());

    /**
     * <p>�޼ҵ��           : hpAuth</p>
     * <p>����              : ����� ������ �ޱ� ���ؼ� �������� �Է� �޾Ƽ� ����� ����(SWIS)</p>
     * <p>�޼ҵ��μ�1        : String jumin - �ֹι�ȣ</p>
     * <p>�޼ҵ��μ�2        : String hpCd - ��Ż��ڵ�</p>
     * <p>�޼ҵ��μ�3        : String hpPhone - �ڵ�����ȣ</p>
     * <p>�޼ҵ帮�ϰ�        : String - �����(0,1)</p>
     */
    public static String hpAuth(String jumin, String hpCd, String hpPhone) {

        String input = "";
        String output = "";
        /*parameter*/
        String CPCode = "B010011724"; /* ��������ĳ��Ż ID */
        String CPPasswd = "wooricap"; /* ��������ĳ��Ż PW */

        /*ItemInfo(��ǰ����) parameter*/
        String ItemType = "1"; /* ��ǰ Type - �ǹ�:2 ������:1 (default "1") */
        String ItemAmount = "0"; /* �����ݾ� (CP�� �δ� ������ ��쿡�� "0") */
        String ItemCount = "1"; /* ��ǰ���� (default "1") */
        String ItemCode = "12S0Ro0000";
                          /* ��ǰ�ڵ� (�ٳ����� �߱޹��� ��ǰ�ڵ�) CPCODE.xls���� */
        String ItemName = "ItemName"; /* ��ǰ�� (���Ƿ� �Է�)*/

        TeleditClient teledit = new TeleditClient(
                "/TMAX/jeus42/webhome/servlet_home/webapps/swis/WEB-INF/classes/");

        try {
            input = "ID=" + CPCode // CPID
                    + ";PWD=" + CPPasswd // Password
                    + ";ItemType=Amount" // ��ǰType("Amount"�� �̿�)
                    + ";ItemCount=1" // ��ǰ����("1"�� �̿�)
                    + ";ItemInfo=" + ItemType + "|" + ItemAmount + "|" +
                    ItemCount + "|" + ItemCode + "|" + ItemName // ��ǰ����(<ItemType>'|'<ItemAmount>'|'<ItemCount>'|'<ItemCode>'|'<ItemName>)
                    + ";SERVICE=TELEDIT" // TELEDIT: �ڵ��� ����(����)
                    + ";DstAddr=" + hpPhone // ��ȭ��ȣ
                    + ";Iden=" + jumin // �ֹι�ȣ
                    + ";Carrier=" + hpCd; // ��Ż�(SKT, KTF, LGT)

            output = teledit.simDeliver(input);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("�������������:" + e.getMessage());
        }

        return output;
    }


    public static String hpAuthSWCS(String jumin, String hpCd, String hpPhone) {
        String input = "";
        String output = "";
        /*parameter*/
        String CPCode = "B010011724"; /* ��������ĳ��Ż ID */
        String CPPasswd = "wooricap"; /* ��������ĳ��Ż PW */

        /*ItemInfo(��ǰ����) parameter*/
        String ItemType = "1"; /* ��ǰ Type - �ǹ�:2 ������:1 (default "1") */
        String ItemAmount = "0"; /* �����ݾ� (CP�� �δ� ������ ��쿡�� "0") */
        String ItemCount = "1"; /* ��ǰ���� (default "1") */
        String ItemCode = "12S0Ro0000";
                          /* ��ǰ�ڵ� (�ٳ����� �߱޹��� ��ǰ�ڵ�) CPCODE.xls���� */
        String ItemName = "ItemName"; /* ��ǰ�� (���Ƿ� �Է�)*/

        TeleditClient teledit = new TeleditClient(
                "/TMAX/jeus5/lib/application/");

        try {
            input = "ID=" + CPCode // CPID
                    + ";PWD=" + CPPasswd // Password
                    + ";ItemType=Amount" // ��ǰType("Amount"�� �̿�)
                    + ";ItemCount=1" // ��ǰ����("1"�� �̿�)
                    + ";ItemInfo=" + ItemType + "|" + ItemAmount + "|" +
                    ItemCount + "|" + ItemCode + "|" + ItemName // ��ǰ����(<ItemType>'|'<ItemAmount>'|'<ItemCount>'|'<ItemCode>'|'<ItemName>)
                    + ";SERVICE=TELEDIT" // TELEDIT: �ڵ��� ����(����)
                    + ";DstAddr=" + hpPhone // ��ȭ��ȣ
                    + ";Iden=" + jumin // �ֹι�ȣ
                    + ";Carrier=" + hpCd; // ��Ż�(SKT, KTF, LGT)

            output = teledit.simDeliver(input);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("�������������:" + e.getMessage());
        }

        return output;
    }

    /**
     * <p>�޼ҵ��           : hpAuthConfirm</p>
     * <p>����              : ���� ������ȣ�� �´��� Ȯ���ϱ� ���ؼ� ������ȣ�� �Է� �޾Ƽ� ����� ����(SWIS)</p>
     * <p>�޼ҵ��μ�1        : String ServerInfo - �ŷ� unique key</p>
     * <p>�޼ҵ��μ�2        : String OTP - ����ڰ� �Է��� ���� ��ȣ</p>
     * <p>�޼ҵ帮�ϰ�        : String - �����(0,1)</p>
     */
    public static String hpAuthConfirm(String ServerInfo, String OTP) {

        String input = "";
        String output = "";

        TeleditClient teledit = new TeleditClient(
                "/TMAX/jeus42/webhome/servlet_home/webapps/swis/WEB-INF/classes/");

        try {
            input = "ServerInfo=" + ServerInfo // �ŷ� unique key
                    + ";OTP=" + OTP; // ����ڰ� �Է��� ������ȣ

            output = teledit.simBill(input);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("��������:" + e.getMessage());
        }

        return output;
    }

    public static String hpAuthConfirmSWCS(String ServerInfo, String OTP) {
        String input = "";
        String output = "";

        TeleditClient teledit = new TeleditClient(
                "/TMAX/jeus5/lib/application/");

        try {
            input = "ServerInfo=" + ServerInfo // �ŷ� unique key
                    + ";OTP=" + OTP; // ����ڰ� �Է��� ������ȣ

            output = teledit.simBill(input);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("��������:" + e.getMessage());
        }
        return output;
    }

    /**
     * <p>�޼ҵ��           : hpAuthCprtPlc</p>
     * <p>����              : ����� ������ �ޱ� ���ؼ� �������� �Է� �޾Ƽ� ����� ����(���ޱ���)</p>
     * <p>�޼ҵ��μ�1        : String jumin - �ֹι�ȣ</p>
     * <p>�޼ҵ��μ�2        : String hpCd - ��Ż��ڵ�</p>
     * <p>�޼ҵ��μ�3        : String hpPhone - �ڵ�����ȣ</p>
     * <p>�޼ҵ帮�ϰ�        : String - �����(0,1)</p>
     */
    public static String hpAuthCprtPlc(String jumin, String hpCd,
                                       String hpPhone) {

        String input = "";
        String output = "";
        /*parameter*/
        String CPCode = "B010011724"; /* ��������ĳ��Ż ID */
        String CPPasswd = "wooricap"; /* ��������ĳ��Ż PW */

        /*ItemInfo(��ǰ����) parameter*/
        String ItemType = "1"; /* ��ǰ Type - �ǹ�:2 ������:1 (default "1") */
        String ItemAmount = "0"; /* �����ݾ� (CP�� �δ� ������ ��쿡�� "0") */
        String ItemCount = "1"; /* ��ǰ���� (default "1") */
        String ItemCode = "12S0Ro0000";
                          /* ��ǰ�ڵ� (�ٳ����� �߱޹��� ��ǰ�ڵ�) CPCODE.xls���� */
        String ItemName = "ItemName"; /* ��ǰ�� (���Ƿ� �Է�)*/

        TeleditClient teledit = new TeleditClient(
                "/TMAX/jeus42/webhome/servlet_home/webapps/cprtplc/WEB-INF/classes/");

        try {
            input = "ID=" + CPCode // CPID
                    + ";PWD=" + CPPasswd // Password
                    + ";ItemType=Amount" // ��ǰType("Amount"�� �̿�)
                    + ";ItemCount=1" // ��ǰ����("1"�� �̿�)
                    + ";ItemInfo=" + ItemType + "|" + ItemAmount + "|" +
                    ItemCount + "|" + ItemCode + "|" + ItemName // ��ǰ����(<ItemType>'|'<ItemAmount>'|'<ItemCount>'|'<ItemCode>'|'<ItemName>)
                    + ";SERVICE=TELEDIT" // TELEDIT: �ڵ��� ����(����)
                    + ";DstAddr=" + hpPhone // ��ȭ��ȣ
                    + ";Iden=" + jumin // �ֹι�ȣ
                    + ";Carrier=" + hpCd; // ��Ż�(SKT, KTF, LGT)

            output = teledit.simDeliver(input);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("�������������:" + e.getMessage());
        }

        return output;
    }

    /**
     * <p>�޼ҵ��           : hpAuthConfirmCprtPlc</p>
     * <p>����              : ���� ������ȣ�� �´��� Ȯ���ϱ� ���ؼ� ������ȣ�� �Է� �޾Ƽ� ����� ����(���ޱ���)</p>
     * <p>�޼ҵ��μ�1        : String ServerInfo - �ŷ� unique key</p>
     * <p>�޼ҵ��μ�2        : String OTP - ����ڰ� �Է��� ���� ��ȣ</p>
     * <p>�޼ҵ帮�ϰ�        : String - �����(0,1)</p>
     */
    public static String hpAuthConfirmCprtPlc(String ServerInfo, String OTP) {

        String input = "";
        String output = "";

        TeleditClient teledit = new TeleditClient(
                "/TMAX/jeus42/webhome/servlet_home/webapps/cprtplc/WEB-INF/classes/");

        try {
            input = "ServerInfo=" + ServerInfo // �ŷ� unique key
                    + ";OTP=" + OTP; // ����ڰ� �Է��� ������ȣ

            output = teledit.simBill(input);

        } catch (Exception e) {
            e.printStackTrace();
            log.info("��������:" + e.getMessage());
        }

        return output;
    }

    public static void main(String[] args) {
        // System.out.println("getComma : " + Util.getComma("-234567890.01"));
    }
    
    /**
     * <p>�޼ҵ��           : hpAuthSCI</p>
     * <p>����              : ����� ������ �ޱ� ���ؼ� �������� �Է� �޾Ƽ� ����� ����(SCI : ����ſ�������)</p>
     * <p>�޼ҵ��μ�0        : String jumin - �ֹι�ȣ</p>
     * <p>�޼ҵ��μ�1        : String custNm - ����</p>
     * <p>�޼ҵ��μ�2        : String hpCd - ��Ż��ڵ�</p>
     * <p>�޼ҵ��μ�3        : String hpPhone - �ڵ�����ȣ</p>
     * <p>�޼ҵ��μ�4        : String eaiSnum - EAI �Ϸù�ȣ</p>
     * <p>�޼ҵ帮�ϰ�        : String - �����(0,1)</p>
     */
	public static String[] hpAuthSCI(String[] param) {

		String[] output = null;
		// ��û����
    	SciHpCert1GramCC cert1Gram = new SciHpCert1GramCC();
    	SciHpCert1SendEntity cert1SendEntity = new SciHpCert1SendEntity();
    	SciHpCert1RecvEntity cert1RecvEntity = new SciHpCert1RecvEntity();
    	
		try {
			setCert1Entity(cert1SendEntity, param);
			cert1RecvEntity = cert1Gram.procSend(cert1SendEntity);
			output = convertCert1RecvEntityToArry(cert1RecvEntity);
		} catch (Exception e) {
            e.printStackTrace();
            log.info("�������������:" + e.getMessage());
        }
    	
    	return output;
    }

	/**
     * <p>�޼ҵ��           : hpAuthSCI</p>
     * <p>����              : ����� ������ �ޱ� ���ؼ� �������� �Է� �޾Ƽ� ����� ����(SCI : ����ſ�������)</p>
     * <p>�޼ҵ��μ�0        : String jumin - �ֹι�ȣ</p>
     * <p>�޼ҵ��μ�1        : String custNm - ����</p>
     * <p>�޼ҵ��μ�2        : String hpCd - ��Ż��ڵ�</p>
     * <p>�޼ҵ��μ�3        : String hpPhone - �ڵ�����ȣ</p>
     * <p>�޼ҵ��μ�4        : String eaiSnum - EAI �Ϸù�ȣ</p>
     * <p>�޼ҵ��μ�5        : String selfCnfmSnum - ����Ȯ�μ���</p>
     * <p>�޼ҵ��μ�6        : String authSnum - ��������</p>
     * <p>�޼ҵ��μ�7        : String askNo - ��û��ȣ</p>
     * <p>�޼ҵ帮�ϰ�        : String - �����(0,1)</p>
     */
	public static String[] hpAuthSCILMS(String[] param) {

		String[] output = null;
    	// LMS����������
    	SciHpCert2GramCC cert2Gram = new SciHpCert2GramCC();
    	SciHpCert2SendEntity cert2SendEntity = new SciHpCert2SendEntity();
    	SciHpCert2RecvEntity cert2RecvEntity = new SciHpCert2RecvEntity();
    	
		try {
			setCert2Entity(cert2SendEntity, param);
			cert2RecvEntity = cert2Gram.procSend(cert2SendEntity);
			output = convertCert2RecvEntityToArry(cert2RecvEntity);
		} catch (Exception e) {
            e.printStackTrace();
            log.info("�������������:" + e.getMessage());
        }
    	
    	return output;
    }
	
	/**
     * <p>�޼ҵ��           : setCert1Entity</p>
     * <p>����              : ������ ����������� ���� �۽����� ���� ����</p>
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
     * <p>�޼ҵ��           : setCert2Entity</p>
     * <p>����              : LMS ������ ���� ����</p>
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
     * <p>�޼ҵ��           : setCert3Entity</p>
     * <p>����              : LMS ������ ���� ����</p>
     */    
    public static void setCert3Entity(SciHpCert3SendEntity certEntity, String[] arrParam){
    	certEntity.setMco_id       ("SXLT020");
    	certEntity.setHdr_doc_cd   ("0200");
    	certEntity.setHdr_doc_id   ("342");
    	certEntity.setRsp_cd       ("");
    	certEntity.setSno          (arrParam[16]); // ����
    	certEntity.setRsdt_rg_no   (arrParam[11]); // �ֹι�ȣ
    	certEntity.setSvc_no       ("001001"); // ���񽺹�ȣ
    	certEntity.setAk_no        (ServerSysDate.getServerDate(ServerSysDate.YYYYMMDDHHMMSS)+"001"); // ��û��ȣ
    	certEntity.setPn           (arrParam[1]); // ����
    	certEntity.setMmt_tcc_co_dc(arrParam[3]); // ����籸��
    	certEntity.setPhon         (arrParam[4].trim()+arrParam[5].trim()+arrParam[6].trim()); // �޴�����ȣ
    	certEntity.setHs_vd_sno    (arrParam[13]); // ����Ȯ�μ��� !
    	certEntity.setCtf_sno      (arrParam[14]); // �������� !
    	certEntity.setLms_ctf_no   (arrParam[8]); // ������ȣ
    	certEntity.setRtrs_sno     (arrParam[15]); // �����ۼ��� !
    	certEntity.setAk_dtti      (arrParam[12]); // ��û�Ͻ� !
    }

    /**
     * <p>�޼ҵ��           : convertCert1RecvEntityToArry</p>
     * <p>����              : return �ϱ����� ����������ü�� arry �� ����</p>
     */    
    public static String[] convertCert1RecvEntityToArry(SciHpCert1RecvEntity recvEntity){
    	String[] output = new String[6];
    	output[0] = recvEntity.getRs_v();           // �����         
		output[1] = recvEntity.getPhon_ctf_rc();    // �޴�����������ڵ�   
		output[2] = recvEntity.getHs_vd_sno();      // ����Ȯ�μ���
		output[3] = recvEntity.getCtf_sno();        // ��������
		output[4] = recvEntity.getAk_no();          // ��û��ȣ
		output[5] = recvEntity.getAk_dtti();        // ��û�Ͻ�
		return output;
    }
    
    /**
     * <p>�޼ҵ��           : hpAuthConfirmSCI</p>
     * <p>����              : ���� ������ȣ�� �´��� Ȯ���ϱ� ���ؼ� ������ȣ�� �Է� �޾Ƽ� ����� ����</p>
     * <p>�޼ҵ��μ�0       : ����ȣ      
     * <p>�޼ҵ��μ�1       : ����       
     * <p>�޼ҵ��μ�2       : �������ڵ�    
     * <p>�޼ҵ��μ�3       : �̵���Ż�     
     * <p>�޼ҵ��μ�4       : �ڵ�����ȣ1    
     * <p>�޼ҵ��μ�5       : �ڵ�����ȣ2    
     * <p>�޼ҵ��μ�6       : �ڵ�����ȣ3    
     * <p>�޼ҵ��μ�7       : �ŷ�������ȣ    
     * <p>�޼ҵ��μ�8       : ������ȣ      
     * <p>�޼ҵ��μ�9       : �������������ڵ� 
     * <p>�޼ҵ��μ�10      : �������������޽���
     * <p>�޼ҵ��μ�11      : �ֹε�Ϲ�ȣ
     * <p>�޼ҵ��μ�12      : ��û�Ͻ�   
     * <p>�޼ҵ��μ�13      : ����Ȯ�μ���  
     * <p>�޼ҵ��μ�14      : ��������    
     * <p>�޼ҵ��μ�15      : �����ۼ��� 
     * <p>�޼ҵ��μ�16      : EAI ���� 
     * <p>�޼ҵ帮�ϰ�        : String[] - �����</p>
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
			log.info("�������������:" + e.getMessage());
		}

		return output;
	}
    
    /**
     * <p>�޼ҵ��           : convertCert2RecvEntityToArry</p>
     * <p>����              : return �ϱ����� ����������ü�� arry �� ����</p>
     */    
    public static String[] convertCert2RecvEntityToArry(SciHpCert2RecvEntity recvEntity){
    	String[] output = new String[6];
    	output[0] = recvEntity.getHs_vd_sno();      // ����Ȯ�μ���         
		output[1] = recvEntity.getPhon_ctf_rc();    // �޴�����������ڵ�   
		output[2] = recvEntity.getHs_vd_sno();      // ����Ȯ�μ���
		output[3] = recvEntity.getCtf_no();         // ��������
		output[4] = recvEntity.getAk_no();          // ��û��ȣ
		output[5] = recvEntity.getLms_fw_sno();     // LMS �߼ۼ���
		return output;
    }
    
    /**
     * <p>�޼ҵ��           : convertCert3RecvEntityToArry</p>
     * <p>����              : return �ϱ����� ����������ü�� arry �� ����</p>
     */    
    public static String[] convertCert3RecvEntityToArry(SciHpCert3RecvEntity recvEntity){
    	String[] output = new String[5];
    	output[0] = recvEntity.getCtf_vd_rc();      // ������ȣȮ�ΰ��    
		output[1] = recvEntity.getPhon_ctf_rc();    // �޴�����������ڵ�   
		output[2] = recvEntity.getHs_vd_sno();      // ����Ȯ�μ���
//		output[3] = recvEntity.getCtf_sno();        // ��������
		output[4] = recvEntity.getAk_no();          // ��û��ȣ
		return output;
    }
}
