package com.cbscap.util;

import org.apache.log4j.Logger;

import com.cbscap.fw.bc.c.BCException;
import com.cbscap.fw.bc.c.BCUtil;
import com.cbscap.fw.dbutil.dbmanager.DBBridgeFactory;
import com.cbscap.fw.dbutil.dbmanager.IDBBridge;
import com.cbscap.fw.dbutil.dbmanager.ISqlResult;
import com.cbscap.fw.dbutil.transaction.ITransaction;
import com.cbscap.fw.util.log.CallerInfo;

/**
 * <p>사용자 정의 쿼리를 이용하여 일련번호(MAX+1) 얻어오기
 * @author 김대호
 *
 */
public class Gvno
{

  /**
   * <p>테이블의 컬럼값의 max+1값을 입력된 길이에 맞게 0으로 채워서 채번
   * @param tblNm 채번대상 테이블
   * @param colNm 채번대상 컬럼
   * @param gvnoLength 채번길이
   * @param tx
   * @return
   * @throws BCException
   */
  public static String searchMax(String tblNm, String colNm, int gvnoLength,
                                 ITransaction tx) throws BCException
  {
    return searchMax(tblNm, colNm, gvnoLength, null, null, tx);
  }

  /**
   * <p>
   * 테이블의 컬럼값의 max+1값을 입력된 길이에 맞게 0으로 채워서 채번
   * <p>
   * 조회조건내의 채번할지 않할지 옵션으로 선택함.
   *
   * <pre>
   * <code>
   * searchMax( &quot;CBSSALE.TABLE_NAME&quot;, &quot;COLUM1&quot;, 8, &quot;COL2 &gt; 1000&quot;, &quot;1&quot;, tx)
   * ==&gt; select &lt;b&gt;COLUM1&lt;/b&gt;
   *     from &lt;b&gt;CBSSALE.TABLE_NAME&lt;/b&gt;
   *     &lt;b&gt;where COL2 &gt; 1000&lt;/b&gt;
   *     order by &lt;b&gt;COLUM1&lt;/b&gt; desc
   *     FETCH FIRST 1 ROWS ONLY
   * //조회결과값에 +1을 하여 리턴한다.
   *
   * searchMax( &quot;CBSSALE.TABLE_NAME&quot;, &quot;COLUM1&quot;, 8, &quot;&quot;, &lt;b&gt;null&lt;/b&gt;, tx)
   * ==&gt; select &lt;b&gt;COLUM1&lt;/b&gt;
   *     from &lt;b&gt;CBSSALE.TABLE_NAME&lt;/b&gt;
   *     order by &lt;b&gt;COLUM1&lt;/b&gt; desc
   *     FETCH FIRST 1 ROWS ONLY
   * //조회결과값에 +1을 하여 리턴한다.
   *
   *
   * </code>
   * </pre>
   *
   * @param tblNm
   *            채번대상 테이블
   * @param colNm
   *            채번대상 컬럼
   * @param gvnoLength
   *            채번길이
   * @param condition
   *            조회조건절
   * @param flag
   *            null or other, <b>null</b>이면 condition 으로 where절만듬.
   * @param tx
   * @return
   * @throws BCException
   */
  public static String searchMax(String tblNm, String colNm, int gvnoLength,
                                 String condition, String flag, ITransaction tx) throws
      BCException
  {
    String path = CallerInfo.getPath(new Throwable());
    Logger log = Logger.getLogger(path);
    String retVal = "";
    String sql = "";

    IDBBridge connector = DBBridgeFactory.getInstance().getDBBridge();

    if (flag == null)
      sql = String.valueOf(String.valueOf( (new StringBuffer("SELECT MAX("))
                                          .append(colNm).append(") FROM ").
                                          append(tblNm)
//                                              " ORDER BY ").append(colNm).
//                                          append(
//                                              " DESC FETCH FIRST 1 ROWS ONLY")
    		  								));
    else
      sql = String.valueOf(String.valueOf( (new StringBuffer("SELECT MAX("))
                                          .append(colNm).append(") FROM ").
                                          append(tblNm).append(
                                              " WHERE ").append(condition)
//                                              .append(" ORDER BY ")
//                                          .append(colNm).append(
//          " DESC FETCH FIRST 1 ROWS ONLY")
          ));

    ISqlResult result = connector.execute(sql, false, tx);
    BCUtil.processCrudError(result, path);
    if (result.next())
    {
      String lastNo = result.getString(1);
      retVal = makeGvno(lastNo, gvnoLength);
    }
    else
    {
      retVal = makeGvno(null, gvnoLength);
    }

    return retVal;
  }

  /**
   * 테이블에 두자리수, 한자리수가 혼재 되어있는경우
   * **/
  public static String searchMax01(String tblNm, String colNm, int gvnoLength,
          String condition, String flag, ITransaction tx) throws
	BCException
	{
	String path = CallerInfo.getPath(new Throwable());
	Logger log = Logger.getLogger(path);
	String retVal = "";
	String sql = "";
	
	IDBBridge connector = DBBridgeFactory.getInstance().getDBBridge();
	
	if (flag == null)
	sql = String.valueOf(String.valueOf( (new StringBuffer("SELECT MAX(TO_NUMBER("))
	                   .append(colNm).append(")) FROM ").
	                   append(tblNm)
	//                       " ORDER BY ").append(colNm).
	//                   append(
	//                       " DESC FETCH FIRST 1 ROWS ONLY")
							));
	else
	sql = String.valueOf(String.valueOf( (new StringBuffer("SELECT MAX(TO_NUMBER("))
	                   .append(colNm).append(")) FROM ").
	                   append(tblNm).append(
	                       " WHERE ").append(condition)
	//                       .append(" ORDER BY ")
	//                   .append(colNm).append(
	//" DESC FETCH FIRST 1 ROWS ONLY")
	));
	
	ISqlResult result = connector.execute(sql, false, tx);
	BCUtil.processCrudError(result, path);
	if (result.next())
	{
	String lastNo = result.getString(1);
	retVal = makeGvno(lastNo, gvnoLength);
	}
	else
	{
	retVal = makeGvno(null, gvnoLength);
	}
	
	return retVal;
}
  /**
   * <p>
   * 테이블의 컬럼값의 max+1값을 입력된 길이에 맞게 0으로 채워서 채번
   * <p>
   * 조회조건내의 채번할지 않할지 옵션으로 선택함.
   * <p>
   * order by절이 빠짐. 컬럼이 desc로 정렬되어 있다는 가정하에
   *
   * <pre>
   * <code>
   * searchMax( &quot;CBSSALE.TABLE_NAME&quot;, &quot;COLUM1&quot;, 8, &quot;COL2 &gt; 1000&quot;, &quot;1&quot;, tx)
   * ==&gt; select &lt;b&gt;COLUM1&lt;/b&gt;
   *     from &lt;b&gt;CBSSALE.TABLE_NAME&lt;/b&gt;
   *     &lt;b&gt;where COL2 &gt; 1000&lt;/b&gt;
   * //조회결과값에 +1을 하여 리턴한다.
   *
   * searchMax( &quot;CBSSALE.TABLE_NAME&quot;, &quot;COLUM1&quot;, 8, &quot;&quot;, &lt;b&gt;null&lt;/b&gt;, tx)
   * ==&gt; select &lt;b&gt;COLUM1&lt;/b&gt;
   *     from &lt;b&gt;CBSSALE.TABLE_NAME&lt;/b&gt;
   * //조회결과값에 +1을 하여 리턴한다.
   *
   *
   * </code>
   * </pre>
   *
   * @param tblNm
   *            채번대상 테이블
   * @param colNm
   *            채번대상 컬럼
   * @param gvnoLength
   *            채번길이
   * @param condition
   *            조회조건절
   * @param flag
   *            null or other, <b>null</b>이면 condition 으로 where절만듬.
   * @param tx
   * @return
   * @throws BCException
   */
  public static String searchMax(String tblNm, String selCol, String colNm,
                                 int gvnoLength, String condition, String flag,
                                 ITransaction tx) throws BCException
  {
    String path = CallerInfo.getPath(new Throwable());
    String retVal = "";
    String sql = "";
    Logger log = Logger.getLogger(path);

    IDBBridge connector = DBBridgeFactory.getInstance().getDBBridge();

    if (flag == null)
      sql = String.valueOf(String.valueOf( (new StringBuffer("SELECT "))
                                          .append(selCol).append(" FROM ").
                                          append(tblNm)));
    else
      sql = String.valueOf(String.valueOf( (new StringBuffer("SELECT "))
                                          .append(selCol).append(" FROM ").
                                          append(tblNm).append(
                                              " WHERE ").append(condition)));

    ISqlResult result = connector.execute(sql, false, tx);
    BCUtil.processCrudError(result, path);
    if (result.next())
    {
      String lastNo = result.getString(1);
      //log.debug("lastNo===>" + lastNo);
      retVal = makeGvno(lastNo, gvnoLength);
      //log.debug("retVal===>" + retVal);
    }
    else
    {
      //log.debug("gvnoLength===>" + gvnoLength);
      retVal = makeGvno(null, gvnoLength);
      //log.debug("retVal===>" + retVal);
    }

    return retVal;
  }

  /**
   * <p>
   * 테이블의 컬럼값의 max+1값을 입력된 길이에 맞게 0으로 채워서 채번
   * <p>
   * 조회조건내의 채번할지 않할지 옵션으로 선택함.
   *
   * <pre>
   * <code>
   * searchMax( &quot;CBSSALE.TABLE_NAME&quot;, &quot;COLUM1&quot;, 8, &quot;COL2 &gt; 1000&quot;, &quot;1&quot;, tx)
   * ==&gt; select &lt;b&gt;COLUM1&lt;/b&gt;
   *     from &lt;b&gt;CBSSALE.TABLE_NAME&lt;/b&gt;
   *     &lt;b&gt;where COL2 &gt; 1000&lt;/b&gt;
   *     order by &lt;b&gt;COLUM1&lt;/b&gt; desc
   *     FETCH FIRST 1 ROWS ONLY <b> </b>
   * //조회결과값에 +1을 하여 리턴한다.
   *
   * searchMax( &quot;CBSSALE.TABLE_NAME&quot;, &quot;COLUM1&quot;, 8, &quot;&quot;, &lt;b&gt;null&lt;/b&gt;, tx)
   * ==&gt; select &lt;b&gt;COLUM1&lt;/b&gt;
   *     from &lt;b&gt;CBSSALE.TABLE_NAME&lt;/b&gt;
   *     order by &lt;b&gt;COLUM1&lt;/b&gt; desc
   *     FETCH FIRST 1 ROWS ONLY <b> </b>
   * //조회결과값에 +1을 하여 리턴한다.
   *
   * </code>
   * </pre>
   *
   * @param tblNm
   *            채번대상 테이블
   * @param colNm
   *            채번대상 컬럼
   * @param gvnoLength
   *            채번길이
   * @param condition
   *            조회조건절
   * @param flag
   *            null or other, <b>null</b>이면 condition 으로 where절만듬.
   * @param tx
   * @return
   * @throws BCException
   */
  public static String searchMax2(String tblNm, String colNm, int gvnoLength,
                                  String condition, String flag,
                                  ITransaction tx) throws BCException
  {
    String path = CallerInfo.getPath(new Throwable());
    Logger log = Logger.getLogger(path);
    String retVal = "";
    String sql = "";

    IDBBridge connector = DBBridgeFactory.getInstance().getDBBridge();

    if (flag == null)
      sql = String.valueOf(String.valueOf( (new StringBuffer("SELECT  MAX(SNUM)"))
                                          .append(colNm).append(" FROM ").
                                          append(tblNm)));
    else
      sql = String.valueOf(String.valueOf( (new StringBuffer("SELECT MAX(SNUM)"))
                                          .append(colNm).append(" FROM ").
                                          append(tblNm).append(
                                              " WHERE ").append(condition)));

    ISqlResult result = connector.execute(sql, false, tx);
    BCUtil.processCrudError(result, path);
    if (result.next())
    {
      String lastNo = result.getString(1);
      retVal = makeGvno(lastNo, gvnoLength);
    }
    else
    {
      retVal = makeGvno(null, gvnoLength);
    }

    return retVal;
  }
  
  
  /**
   * <p>
   * 테이블의 컬럼값의 max+1값을 입력된 길이에 맞게 0으로 채워서 채번
   * <p>
   * 조회조건내의 채번할지 않할지 옵션으로 선택함.
   *
   * <pre>
   * <code>
   * searchMax( &quot;CBSSALE.TABLE_NAME&quot;, &quot;COLUM1&quot;, 8, &quot;COL2 &gt; 1000&quot;, &quot;1&quot;, tx)
   * ==&gt; select &lt;b&gt;COLUM1&lt;/b&gt;
   *     from &lt;b&gt;CBSSALE.TABLE_NAME&lt;/b&gt;
   *     &lt;b&gt;where COL2 &gt; 1000&lt;/b&gt;
   *     order by &lt;b&gt;COLUM1&lt;/b&gt; desc
   *     FETCH FIRST 1 ROWS ONLY <b> </b>
   * //조회결과값에 +1을 하여 리턴한다.
   *
   * searchMax( &quot;CBSSALE.TABLE_NAME&quot;, &quot;COLUM1&quot;, 8, &quot;&quot;, &lt;b&gt;null&lt;/b&gt;, tx)
   * ==&gt; select &lt;b&gt;COLUM1&lt;/b&gt;
   *     from &lt;b&gt;CBSSALE.TABLE_NAME&lt;/b&gt;
   *     order by &lt;b&gt;COLUM1&lt;/b&gt; desc
   *     FETCH FIRST 1 ROWS ONLY <b> </b>
   * //조회결과값에 +1을 하여 리턴한다.
   *
   * </code>
   * </pre>
   *
   * @param tblNm
   *            채번대상 테이블
   * @param colNm
   *            채번대상 컬럼
   * @param gvnoLength
   *            채번길이
   * @param condition
   *            조회조건절
   * @param flag
   *            null or other, <b>null</b>이면 condition 으로 where절만듬.
   * @param tx
   * @return
   * @throws BCException
   */
  public static String searchMax3(String tblNm, String colNm, int gvnoLength,
                                  String condition, String flag,
                                  ITransaction tx) throws BCException
  {
    String path = CallerInfo.getPath(new Throwable());
    Logger log = Logger.getLogger(path);
    String retVal = "";
    String sql = "";

    IDBBridge connector = DBBridgeFactory.getInstance().getDBBridge();

    if (flag == null)
      sql = String.valueOf(String.valueOf( (new StringBuffer("SELECT MAX("))
                                          .append(colNm).append(") FROM ").
                                          append(tblNm)));
//                                              " ORDER BY ").append(colNm).
//                                          append(
//                                              " DESC FETCH FIRST 1 ROWS ONLY  ")));
    else
      sql = String.valueOf(String.valueOf( (new StringBuffer("SELECT MAX("))
                                          .append(colNm).append(") FROM ").
                                          append(tblNm).append(
                                              " WHERE ").append(condition)));
//                                          append(" ORDER BY ")
//                                          .append(colNm).append(
//                                              " DESC FETCH FIRST 1 ROWS ONLY  ")));

    ISqlResult result = connector.execute(sql, false, tx);
    BCUtil.processCrudError(result, path);
    if (result.next())
    {
      String lastNo = result.getString(1);
      retVal = lastNo; // makeGvno(lastNo, gvnoLength);
    }
    else
    {
      retVal = makeGvno(null, gvnoLength);
    }

    return retVal;
  }

  /**
   * <p>채번길이로 0을 채운다.
   * <p>입력Max값에 +1을 한다.
   * <pre>
   * makeGvno( "10", 5 ) return "00011"
   * </pre>
   * @param lastNo 문자열
   * @param gvnoLength 채번길이
   * @return
   * @throws BCException
   */
  private static String makeGvno(String lastNo, int gvnoLength) throws
      BCException
  {
    StringBuffer retVal = new StringBuffer(gvnoLength);

    if (lastNo == null || (lastNo.trim()).equals(""))
    {
      if (gvnoLength == 0)
        return "1";
      for (int i = 1; i < gvnoLength; i++)
        retVal.append("0");

      retVal.append("1");
    }
    else
    {
      int tempNo = 0;
      try
      {
        //System.out.println("lastNo : ".concat(String.valueOf(String.valueOf(lastNo))));
        tempNo = Integer.parseInt(lastNo.trim());
        //System.out.println("tempNo : ".concat(String.valueOf(String.valueOf(tempNo))));
        if (gvnoLength == 0)
        {
          String s = String.valueOf(String.valueOf( (new StringBuffer(
              "")).append(tempNo + 1)));
          return s;
        }
      }
      catch (NumberFormatException ne)
      {
        throw new BCException(
            "\uCC44\uBC88\uD560 \uC218 \uC5C6\uB294 \uB370\uC774\uD130\uC785\uB2C8\uB2E4");
      }
      int lastNoLength = String.valueOf(String.valueOf(tempNo))
          .concat("").length();
      for (int i = 0; i < gvnoLength - lastNoLength; i++)
        retVal.append("0");

      retVal.append(String.valueOf(String.valueOf( (new StringBuffer(
          String.valueOf(String.valueOf(tempNo + 1)))).append(""))));
    }
    if (retVal.toString().length() > gvnoLength)
      return retVal.toString().substring(1, gvnoLength + 1);
    else
      return retVal.toString();
  }
  
  
  
  public static String search(String tblNm, String colNm, int gvnoLength,
          String condition, String flag, ITransaction tx) throws BCException
          {
	  			String path = CallerInfo.getPath(new Throwable());
	  			Logger log = Logger.getLogger(path);
	  			String retVal = "";
	  			String sql = "";

	  			IDBBridge connector = DBBridgeFactory.getInstance().getDBBridge();

	  			if (flag == null)
	  				sql = String.valueOf(String.valueOf( (new StringBuffer("SELECT MAX("))
	  						.append(colNm).append(") FROM ").append(tblNm)));
//	  						.append(" ORDER BY ").append(colNm).append(" DESC FETCH FIRST 1 ROWS ONLY")));
	  			else
	  				sql = String.valueOf(String.valueOf( (new StringBuffer("SELECT ")).append(colNm).append(" FROM ")
	  						.append(tblNm).append(" WHERE ").append(condition)));
//	  						.append(colNm).append(" DESC FETCH FIRST 1 ROWS ONLY")));

	  				ISqlResult result = connector.execute(sql, false, tx);
	  				
	  				BCUtil.processCrudError(result, path);
	  				
	  				
	  				if (result.next())
	  				{
	  					String lastNo = result.getString(1);
	  					retVal = lastNo;
	  				}
	  				else
	  				{
	  					 
	  				}

	  				return retVal;
          }
  
}
