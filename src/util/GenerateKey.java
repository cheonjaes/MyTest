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
 * <p>파일명        : GenerateKey.java</p>
 * <p>버젼          : 1.1.1</p>
 * <p>작성일        : 2003.10.06</p>
 * <p>작성자        : 조윤효</p>
 * <p>Use Case명    : 채번</p>
 * <p>내용          : 채번을 위한 공통클래스</p>
 *
 * <p>수정자 : 김대호</p>
 * <p>수정일자 : 2006.01.12</p>
 * <p>내용 : 채번값을 rollback 없이 commit할수 있는 메소드 추가(getNextNumber1())</p>
 */

/**
 * <p>채번
 */
@SuppressWarnings("rawtypes")
public class GenerateKey {
    static Logger log;
    static {
        log = Logger.getLogger(GenerateKey.class.getName());
    }

    /**
     * <p>분류종류번호와 분류번호 1~6을 넣고 채번된 값을 반환한다.
     * @param clasKindNo 채번코드번호
     * @param clasNo1 분류번호1
     * @param clasNo2 분류번호2
     * @param clasNo3 분류번호3
     * @param clasNo4 분류번호4
     * @param clasNo5 분류번호5
     * @param clasNo6 분류번호6
     * @param empNo 채번을 요청한 담당자번호
     * @param tx
     * @return
     * @throws BCException
     */
    public static String getNextNumber(String clasKindNo,String clasNo1,String clasNo2,String clasNo3
        ,String clasNo4,String clasNo5,String clasNo6,String empNo, ITransaction tx) throws BCException {
        IDBBridge connector = DBBridgeFactory.getInstance().getDBBridge();
        String path = CallerInfo.getPath(new Throwable());
        log.info(path+"시작");

        ISqlMeta data = connector.getCrudData(SqlConst.PROCEDURE);
        data.setProgNo("CBCGO_GVNOBC");
        data.setListNo(11);

        if(log.isDebugEnabled()){
	        StringBuffer debug = new StringBuffer();
	        debug.append("\n========== 채번 (CBSCOMM.CP_GVNO_01(?,?,?,?,?,?,?,?,?,?)) ============");
	        debug.append("\n분류종류번호:");
	        debug.append(clasKindNo);
	        debug.append("\n분류번호1  :");
	        debug.append(clasNo1);
	        debug.append("\n분류번호2  :");
	        debug.append(clasNo2);
	        debug.append("\n분류번호3  :");
	        debug.append(clasNo3);
	        debug.append("\n분류번호4  :");
	        debug.append(clasNo4);
	        debug.append("\n분류번호5  :");
	        debug.append(clasNo5);
	        debug.append("\n분류번호6  :");
	        debug.append(clasNo6);
	        debug.append("\n사원번호   :");
	        debug.append(empNo);
	        debug.append("\n===============================================");
	        log.debug(debug.toString());
        }

        data.setString(1,clasKindNo); // 분류종류번호
        data.setString(2,clasNo1); // 분류번호1
        data.setString(3,clasNo2); // 분류번호2
        data.setString(4,clasNo3); // 분류번호3
        data.setString(5,clasNo4); // 분류번호4
        data.setString(6,clasNo5); // 분류번호5
        data.setString(7,clasNo6); // 분류번호6
        data.setString(8,empNo); // 사원번호
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
        	log.debug("채번 결과====>"+sData);
        }
        return sData;
    }

    /**
     * <p>분류종류번호와 분류번호 1~5을 넣고 채번된 값을 반환한다.
     * @param clasKindNo 분류종류번호
     * @param clasNo1 분류번호1
     * @param clasNo2 분류번호2
     * @param clasNo3 분류번호3
     * @param clasNo4 분류번호4
     * @param clasNo5 분류번호5
     * @param empNo 사원번호
     * @param tx
     * @return
     * @throws BCException 업무로직상의 에러및 DB에러를 처리하기 위한
     */
    public static String getNextNumber(String clasKindNo,String clasNo1,String clasNo2,String clasNo3
        ,String clasNo4,String clasNo5,String empNo,ITransaction tx) throws BCException
    {
        return getNextNumber(clasKindNo,clasNo1,clasNo2,clasNo3,clasNo4,clasNo5,"",empNo,tx);
    }

    /**
     * <p>분류종류번호와 분류번호 1~4을 넣고 채번된 값을 반환한다.
     * @param clasKindNo 분류종류번호
     * @param clasNo1 분류번호1
     * @param clasNo2 분류번호2
     * @param clasNo3 분류번호3
     * @param clasNo4 분류번호4
     * @param empNo 사원번호
     * @param tx
     * @return 채번값
     * @throws BCException 업무로직상의 에러및 DB에러를 처리하기 위한
     */
    public static String getNextNumber(String clasKindNo,String clasNo1,String clasNo2,String clasNo3
        ,String clasNo4,String empNo,ITransaction tx) throws BCException
    {
        return getNextNumber(clasKindNo,clasNo1,clasNo2,clasNo3,clasNo4,"","",empNo,tx);
    }


    /**
     * <p>분류종류번호와 분류번호 1~3을 넣고 채번된 값을 반환한다.
     * @param clasKindNo 분류종류번호
     * @param clasNo1 분류번호1
     * @param clasNo2 분류번호2
     * @param clasNo3 분류번호3
     * @param empNo 사원번호
     * @param tx
     * @return 채번값
     * @throws BCException 업무로직상의 에러및 DB에러를 처리하기 위한
     */
    public static String getNextNumber(String clasKindNo,String clasNo1,String clasNo2,String clasNo3
        ,String empNo,ITransaction tx) throws BCException
    {
        return getNextNumber(clasKindNo,clasNo1,clasNo2,clasNo3,"","","",empNo,tx);
    }

    /**
     * <p>분류종류번호와 분류번호 1~2을 넣고 채번된 값을 반환한다.
     * @param clasKindNo 분류종류번호
     * @param clasNo1 분류번호1
     * @param clasNo2 분류번호2
     * @param empNo 사원번호
     * @param tx
     * @return 채번값
     * @throws BCException 업무로직상의 에러및 DB에러를 처리하기 위한
     */
    public static String getNextNumber(String clasKindNo,String clasNo1,String clasNo2
        ,String empNo,ITransaction tx) throws BCException
    {
        return getNextNumber(clasKindNo,clasNo1,clasNo2,"","","","",empNo,tx);
    }

    /**
     * <p>분류종류번호와 분류번호를 넣고 채번된 값을 반환한다.
     * @param clasKindNo 분류종류번호
     * @param clasNo1 분류번호1
     * @param empNo 사원번호
     * @param tx
     * @return 채번값
     * @throws BCException 업무로직상의 에러및 DB에러를 처리하기 위한
     */
    public static String getNextNumber(String clasKindNo,String clasNo1,String empNo,ITransaction tx)
        throws BCException
    {
        return getNextNumber(clasKindNo,clasNo1,"","","","","",empNo,tx);
    }

    /**
     * <p>분류종류번호와 분류번호 1~5을 넣고 채번된 값을 반환한다.
     * <p><b>!!채번값에 대해 rollback되지 않는다.</b>
     * <p> [2013-12-30 정우현] 프로시저 내부에 별도의 독립된 Transaction을 구현하여
     *             비지니스로직의 에러여부와 상관없이 채번값은 독립적으로 COMMIT 된다.</p>
     * @param clasKindNo 분류종류번호
     * @param clasNo1 분류번호1
     * @param clasNo2 분류번호2
     * @param clasNo3 분류번호3
     * @param clasNo4 분류번호4
     * @param clasNo5 분류번호5
     * @param clasNo6 분류번호6
     * @param empNo 사원번호
     * @param tx
     * @return
     * @throws BCException
     */
    public static String getNextNumber1(String clasKindNo,String clasNo1,String clasNo2,String clasNo3
        ,String clasNo4,String clasNo5,String clasNo6,String empNo, ITransaction tx) throws BCException {
    	String path = CallerInfo.getPath(new Throwable());
    	log.info(path+"시작");
    	
        IDBBridge connector = DBBridgeFactory.getInstance().getDBBridge();
        
        ISqlMeta data = connector.getCrudData(SqlConst.PROCEDURE);
        data.setProgNo("CBCGO_GVNOBC");
        data.setListNo(12);

        if( log.isDebugEnabled()){
	        StringBuffer debug = new StringBuffer();
	        debug.append("\n========== 채번 (CBSCOMM.CP_GVNO_02(?,?,?,?,?,?,?,?,?,?)) ============");
	        debug.append("\n분류종류번호:");
	        debug.append(clasKindNo);
	        debug.append("\n분류번호1  :");
	        debug.append(clasNo1);
	        debug.append("\n분류번호2  :");
	        debug.append(clasNo2);
	        debug.append("\n분류번호3  :");
	        debug.append(clasNo3);
	        debug.append("\n분류번호4  :");
	        debug.append(clasNo4);
	        debug.append("\n분류번호5  :");
	        debug.append(clasNo5);
	        debug.append("\n분류번호6  :");
	        debug.append(clasNo6);
	        debug.append("\n사원번호   :");
	        debug.append(empNo);
	        debug.append("\n===============================================");
	        log.debug(debug.toString());
        }

        data.setString(1,clasKindNo); // 분류종류번호
        data.setString(2,clasNo1); // 분류번호1
        data.setString(3,clasNo2); // 분류번호2
        data.setString(4,clasNo3); // 분류번호3
        data.setString(5,clasNo4); // 분류번호4
        data.setString(6,clasNo5); // 분류번호5
        data.setString(7,clasNo6); // 분류번호6
        data.setString(8,empNo); // 사원번호
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
        	log.debug("채번 결과====>"+sData);
        }
        return sData;
    }
}
