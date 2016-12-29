package com.cbscap.util;

import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;

import com.cbscap.fw.bc.c.BCException;
import com.cbscap.fw.bc.c.BCUtil;
import com.cbscap.fw.dbutil.dbmanager.DBBridgeFactory;
import com.cbscap.fw.dbutil.dbmanager.IDBBridge;
import com.cbscap.fw.dbutil.dbmanager.ISqlMeta;
import com.cbscap.fw.dbutil.dbmanager.ISqlResult;
import com.cbscap.fw.dbutil.dbmanager.SqlConst;
import com.cbscap.fw.dbutil.transaction.ITransaction;
import com.cbscap.fw.util.log.CallerInfo;


/**
 * <p>���ϸ�        : GenerateKey.java</p>
 * <p>����          : 1.1.1</p>
 * <p>�ۼ���        : 2003.10.06</p>
 * <p>�ۼ���        : ����ȿ</p>
 * <p>Use Case��    : ä��</p>
 * <p>����          : ä���� ���� ����Ŭ����</p>
 *
 * <p>������ : ���ȣ</p>
 * <p>�������� : 2006.01.12</p>
 * <p>���� : ä������ rollback ���� commit�Ҽ� �ִ� �޼ҵ� �߰�(getNextNumber1())</p>
 */

/**
 * <p>ä��
 */
@SuppressWarnings("rawtypes")
public class GenerateKey {
    static Logger log;
    static {
        log = Logger.getLogger(GenerateKey.class.getName());
    }

    /**
     * <p>�з�������ȣ�� �з���ȣ 1~6�� �ְ� ä���� ���� ��ȯ�Ѵ�.
     * @param clasKindNo ä���ڵ��ȣ
     * @param clasNo1 �з���ȣ1
     * @param clasNo2 �з���ȣ2
     * @param clasNo3 �з���ȣ3
     * @param clasNo4 �з���ȣ4
     * @param clasNo5 �з���ȣ5
     * @param clasNo6 �з���ȣ6
     * @param empNo ä���� ��û�� ����ڹ�ȣ
     * @param tx
     * @return
     * @throws BCException
     */
    public static String getNextNumber(String clasKindNo,String clasNo1,String clasNo2,String clasNo3
        ,String clasNo4,String clasNo5,String clasNo6,String empNo, ITransaction tx) throws BCException {
        IDBBridge connector = DBBridgeFactory.getInstance().getDBBridge();
        String path = CallerInfo.getPath(new Throwable());
        log.info(path+"����");

        ISqlMeta data = connector.getCrudData(SqlConst.PROCEDURE);
        data.setProgNo("CBCGO_GVNOBC");
        data.setListNo(11);

        if(log.isDebugEnabled()){
	        StringBuffer debug = new StringBuffer();
	        debug.append("\n========== ä�� (CBSCOMM.CP_GVNO_01(?,?,?,?,?,?,?,?,?,?)) ============");
	        debug.append("\n�з�������ȣ:");
	        debug.append(clasKindNo);
	        debug.append("\n�з���ȣ1  :");
	        debug.append(clasNo1);
	        debug.append("\n�з���ȣ2  :");
	        debug.append(clasNo2);
	        debug.append("\n�з���ȣ3  :");
	        debug.append(clasNo3);
	        debug.append("\n�з���ȣ4  :");
	        debug.append(clasNo4);
	        debug.append("\n�з���ȣ5  :");
	        debug.append(clasNo5);
	        debug.append("\n�з���ȣ6  :");
	        debug.append(clasNo6);
	        debug.append("\n�����ȣ   :");
	        debug.append(empNo);
	        debug.append("\n===============================================");
	        log.debug(debug.toString());
        }

        data.setString(1,clasKindNo); // �з�������ȣ
        data.setString(2,clasNo1); // �з���ȣ1
        data.setString(3,clasNo2); // �з���ȣ2
        data.setString(4,clasNo3); // �з���ȣ3
        data.setString(5,clasNo4); // �з���ȣ4
        data.setString(6,clasNo5); // �з���ȣ5
        data.setString(7,clasNo6); // �з���ȣ6
        data.setString(8,empNo); // �����ȣ
        data.register(9,Types.VARCHAR);
        data.register(10,Types.VARCHAR);

        ISqlResult result = connector.execute(data,tx);
        BCUtil.processCrudError(result,path);

        List list = result.getList();
        Object errMsg = list.get(1);
        if(errMsg != null && !errMsg.toString().equals("null")) {
            BCUtil.processCrudError(errMsg.toString(),path);
        }
        String sData = list.get(0).toString();

        if(log.isDebugEnabled()){
        	log.debug("ä�� ���====>"+sData);
        }
        return sData;
    }

    /**
     * <p>�з�������ȣ�� �з���ȣ 1~5�� �ְ� ä���� ���� ��ȯ�Ѵ�.
     * @param clasKindNo �з�������ȣ
     * @param clasNo1 �з���ȣ1
     * @param clasNo2 �з���ȣ2
     * @param clasNo3 �з���ȣ3
     * @param clasNo4 �з���ȣ4
     * @param clasNo5 �з���ȣ5
     * @param empNo �����ȣ
     * @param tx
     * @return
     * @throws BCException ������������ ������ DB������ ó���ϱ� ����
     */
    public static String getNextNumber(String clasKindNo,String clasNo1,String clasNo2,String clasNo3
        ,String clasNo4,String clasNo5,String empNo,ITransaction tx) throws BCException
    {
        return getNextNumber(clasKindNo,clasNo1,clasNo2,clasNo3,clasNo4,clasNo5,"",empNo,tx);
    }

    /**
     * <p>�з�������ȣ�� �з���ȣ 1~4�� �ְ� ä���� ���� ��ȯ�Ѵ�.
     * @param clasKindNo �з�������ȣ
     * @param clasNo1 �з���ȣ1
     * @param clasNo2 �з���ȣ2
     * @param clasNo3 �з���ȣ3
     * @param clasNo4 �з���ȣ4
     * @param empNo �����ȣ
     * @param tx
     * @return ä����
     * @throws BCException ������������ ������ DB������ ó���ϱ� ����
     */
    public static String getNextNumber(String clasKindNo,String clasNo1,String clasNo2,String clasNo3
        ,String clasNo4,String empNo,ITransaction tx) throws BCException
    {
        return getNextNumber(clasKindNo,clasNo1,clasNo2,clasNo3,clasNo4,"","",empNo,tx);
    }


    /**
     * <p>�з�������ȣ�� �з���ȣ 1~3�� �ְ� ä���� ���� ��ȯ�Ѵ�.
     * @param clasKindNo �з�������ȣ
     * @param clasNo1 �з���ȣ1
     * @param clasNo2 �з���ȣ2
     * @param clasNo3 �з���ȣ3
     * @param empNo �����ȣ
     * @param tx
     * @return ä����
     * @throws BCException ������������ ������ DB������ ó���ϱ� ����
     */
    public static String getNextNumber(String clasKindNo,String clasNo1,String clasNo2,String clasNo3
        ,String empNo,ITransaction tx) throws BCException
    {
        return getNextNumber(clasKindNo,clasNo1,clasNo2,clasNo3,"","","",empNo,tx);
    }

    /**
     * <p>�з�������ȣ�� �з���ȣ 1~2�� �ְ� ä���� ���� ��ȯ�Ѵ�.
     * @param clasKindNo �з�������ȣ
     * @param clasNo1 �з���ȣ1
     * @param clasNo2 �з���ȣ2
     * @param empNo �����ȣ
     * @param tx
     * @return ä����
     * @throws BCException ������������ ������ DB������ ó���ϱ� ����
     */
    public static String getNextNumber(String clasKindNo,String clasNo1,String clasNo2
        ,String empNo,ITransaction tx) throws BCException
    {
        return getNextNumber(clasKindNo,clasNo1,clasNo2,"","","","",empNo,tx);
    }

    /**
     * <p>�з�������ȣ�� �з���ȣ�� �ְ� ä���� ���� ��ȯ�Ѵ�.
     * @param clasKindNo �з�������ȣ
     * @param clasNo1 �з���ȣ1
     * @param empNo �����ȣ
     * @param tx
     * @return ä����
     * @throws BCException ������������ ������ DB������ ó���ϱ� ����
     */
    public static String getNextNumber(String clasKindNo,String clasNo1,String empNo,ITransaction tx)
        throws BCException
    {
        return getNextNumber(clasKindNo,clasNo1,"","","","","",empNo,tx);
    }

    /**
     * <p>�з�������ȣ�� �з���ȣ 1~5�� �ְ� ä���� ���� ��ȯ�Ѵ�.
     * <p><b>!!ä������ ���� rollback���� �ʴ´�.</b>
     * <p> [2013-12-30 ������] ���ν��� ���ο� ������ ������ Transaction�� �����Ͽ�
     *             �����Ͻ������� �������ο� ������� ä������ ���������� COMMIT �ȴ�.</p>
     * @param clasKindNo �з�������ȣ
     * @param clasNo1 �з���ȣ1
     * @param clasNo2 �з���ȣ2
     * @param clasNo3 �з���ȣ3
     * @param clasNo4 �з���ȣ4
     * @param clasNo5 �з���ȣ5
     * @param clasNo6 �з���ȣ6
     * @param empNo �����ȣ
     * @param tx
     * @return
     * @throws BCException
     */
    public static String getNextNumber1(String clasKindNo,String clasNo1,String clasNo2,String clasNo3
        ,String clasNo4,String clasNo5,String clasNo6,String empNo, ITransaction tx) throws BCException {
    	String path = CallerInfo.getPath(new Throwable());
    	log.info(path+"����");
    	
        IDBBridge connector = DBBridgeFactory.getInstance().getDBBridge();
        
        ISqlMeta data = connector.getCrudData(SqlConst.PROCEDURE);
        data.setProgNo("CBCGO_GVNOBC");
        data.setListNo(12);

        if( log.isDebugEnabled()){
	        StringBuffer debug = new StringBuffer();
	        debug.append("\n========== ä�� (CBSCOMM.CP_GVNO_02(?,?,?,?,?,?,?,?,?,?)) ============");
	        debug.append("\n�з�������ȣ:");
	        debug.append(clasKindNo);
	        debug.append("\n�з���ȣ1  :");
	        debug.append(clasNo1);
	        debug.append("\n�з���ȣ2  :");
	        debug.append(clasNo2);
	        debug.append("\n�з���ȣ3  :");
	        debug.append(clasNo3);
	        debug.append("\n�з���ȣ4  :");
	        debug.append(clasNo4);
	        debug.append("\n�з���ȣ5  :");
	        debug.append(clasNo5);
	        debug.append("\n�з���ȣ6  :");
	        debug.append(clasNo6);
	        debug.append("\n�����ȣ   :");
	        debug.append(empNo);
	        debug.append("\n===============================================");
	        log.debug(debug.toString());
        }

        data.setString(1,clasKindNo); // �з�������ȣ
        data.setString(2,clasNo1); // �з���ȣ1
        data.setString(3,clasNo2); // �з���ȣ2
        data.setString(4,clasNo3); // �з���ȣ3
        data.setString(5,clasNo4); // �з���ȣ4
        data.setString(6,clasNo5); // �з���ȣ5
        data.setString(7,clasNo6); // �з���ȣ6
        data.setString(8,empNo); // �����ȣ
        data.register(9,Types.VARCHAR);
        data.register(10,Types.VARCHAR);

        ISqlResult result = connector.execute(data,tx);
        BCUtil.processCrudError(result,path);

        List list = result.getList();
        Object errMsg = list.get(1);
        if(errMsg != null && !errMsg.toString().equals("null")) {
            BCUtil.processCrudError(errMsg.toString(),path);
        }

        String sData = list.get(0).toString();
        if(log.isDebugEnabled()){
        	log.debug("ä�� ���====>"+sData);
        }
        return sData;
    }
}
