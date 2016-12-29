package com.cbscap.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.cbscap.fw.util.resource.ResourceLoader;

/**
 * <p>DB2의 Type2방식(db2 clients로 catalog한 정보이용)
 * Connection을 생성하고 DB Util매소드를 제공한다.
 * <br>db설정파일이 classpath내에 존재해야한다.
 * <br>디폴트 <b>config.properties</b>
 *
 * <pre>
 * <code>
 * config.properties 파일예
 *
 * db.driver=COM.ibm.db2.jdbc.app.DB2Driver
 * db.urlprefix=jdbc:db2:
 * db.dbname={$db name}
 * db.user={$user id}
 * db.password={$password}
 * </code>
 * </pre>
 *
 * <pre>
 * --------------------------------------------------------------
 * 변경이력
 * --------------------------------------------------------------
 * 20070920 홍길동 import문 정리
 * --------------------------------------------------------------
 * </pre>
 */
public class ConUtils {
	static final String CONFIG_FILE = "config";

	private static Logger log = Logger.getLogger(ConUtils.class.getName());

	private String driver = null;
	private String dbname = null;
	private String dbuser = null;
	private String dbpwd = null;
	private String urlprefix = null;

	/**
	 * default 연결속성 파일을 사용하여 DB설정을 한다.<br>
	 * config file명 : config.properties
	 */
	public ConUtils() {
		loadConfig(CONFIG_FILE);
	}


	/**
	 * 입력된 파일명으로 DB설정 properties을 얻는다.
	 * @param config properties 파일명. ex) config.properties -> "config"
	 */
	public ConUtils(String config)
	{
		loadConfig(config);
	}


	/**
	 * 직접 DB연결속성을 설정한다.
	 * <pre>
	 * default : driver=COM.ibm.db2.jdbc.app.DB2Driver
	 *           urlprefix=jdbc:db2:
	 * </pre>
	 * @param dbname ex) wric_d
	 * @param dbuser db user id
	 * @param dbpwd db user password
	 */
	public ConUtils(String dbname, String dbuser, String dbpwd)
	{
		this.urlprefix = "jdbc:db2:";
		this.driver = "COM.ibm.db2.jdbc.app.DB2Driver";
		init(dbname, dbuser, dbpwd);
	}

	/**
	 * db 연결설정이 담긴 properties파일을 로드해서 설정정보를 얻는다.
	 * @param config -properties파일명, 확장자는 제거함.
	 */
	private void loadConfig(String config)
	{
		ResourceLoader pl = new ResourceLoader(config);

//		PropertyLoader pl = new PropertyLoader(config);
		this.driver = pl.getString("db.driver", "COM.ibm.db2.jdbc.app.DB2Driver");
		this.urlprefix = pl.getString("db.urlprefix", "jdbc:db2:");
		this.dbname = pl.getString("db.dbname", "");
		this.dbuser = pl.getString("db.user", "");
		this.dbpwd = pl.getString("db.password", "");
	}

	/**
	 * db연결 정보를 설정한다.
	 * @param dbname catalog할때 사용한 db명, ex) wric_d
	 * @param dbuser db2연결 사용자아이디
	 * @param dbpwd db2연결 사용자비밀번호
	 */
	public void init(String dbname, String dbuser, String dbpwd)
	{
		setDBName(dbname);
		setDBUser(dbuser);
		setDBPwd(dbpwd);
	}

	/**
	 * DB connection을 생성한다.<br>
	 * 생성된 Connection은 auto commit이 false로 셋팅된다.<br>
	 * 따라서 Connection을 close할때 꼭 rollback 또는 commit을 해야한다.<br>
	 *
	 * <p>properties 또는 직접 파라미터로 셋팅한 연결정보를 사용한다.
	 * @return Connection
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException
	{
		Connection connection = null;
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection( getUrl(), getDBUser(), getDBPwd());
			connection.setAutoCommit(false);
		}catch (Exception e) {
			log.error("드라이버가 없습니다. : " + e.getMessage());
			e.printStackTrace();
			throw new SQLException("DB 연결을 할수 없습니다. : " + e.getMessage());
		}

		return connection;
	}

	/**
	 * PreparedStatement 또는 Statement를 close한다.
	 * <p>Exception은 발생하지 않는다.
	 * @param stmt
	 */
	public static void closeStatement(Statement stmt)
	{
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ResultSet을 close한다.
	 * <p>Exception은 발생하지 않는다.
	 * @param rs
	 */
	public static void closeResultSet(ResultSet rs)
	{
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Connection을 close한다.
	 * <p>Exception은 발생하지 않는다.
	 * @param con
	 */
	public static void closeConnection(Connection con)
	{
		try {
			if (con != null) {
				con.close();
				con = null;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Connection을 rollback한다.
	 * <p>Exception은 발생하지 않는다.
	 * @param con
	 */
	public static void userRollBack(Connection con) {
		try {
			if( con != null) {
				con.rollback();
			}
		}catch(Exception e) {
			//e.printStackTrace();
		}
	}

	/**
	 * <p> Connection의 commit을 할때 commit단위로 한다.
	 * <pre>
	 * ex) cnt 99 일때 100건단위 commit이면 commit을 안한다.
	 *     cnt 100 일때 100건단위 commit이면 commit을 한다.
	 * </pre>
	 * @param con - Connection
	 * @param cnt - 배치처리된 갯수
	 * @param cntOfCommit - commit단위, ex) 100건단위로 commmit
	 * @throws SQLException
	 */
	public static void userCommit(Connection con,int cnt, int cntOfCommit) throws SQLException{
		if(cnt % cntOfCommit == 0){
			userCommit(con);
		}
	}

	/**
	 * <p>Connection의 commit을  할때 commit단위 (디폴트 10000건으로됨) 로 한다.
	 * @param con - Connection
	 * @param cnt - 배치처리된 갯수
	 * @throws SQLException
	 */
	public static void userCommit(Connection con, int cnt) throws SQLException{
		userCommit(con,cnt,10000);
	}

	/**
	 * <p>Connection을 commit한다.
	 * @param con
	 * @throws SQLException
	 */
	public static void userCommit(Connection con) throws SQLException	{
		con.commit();
	}

	/**
	 * batch처리건의 총합을 계산한다.
	 * @param cnt
	 * @return
	 */
	public static int countBat(int[] cnt)
	{
		int length = cnt.length;
		int totCnt = 0;
		for(int i=0;i<length;i++)
		{
			if(cnt[i] == -2 || cnt[i] == -3)
			{
				totCnt++;
			}
			else if(cnt[i] >= 0)
			{
				totCnt += cnt[i];
			}
		}
		return totCnt;
	}

	/**
	 * <p>DB driver명을 셋팅한다.
	 * @param driver
	 * @deprecated properties 또는 init()매소드 사용을 권장함.
	 */
	public void setDriverClassName(String driver)
	{
		this.driver = driver;
	}

	/**
	 * <p>DB driver명을 얻는다.
	 * @return
	 */
	public String getDriverClassName()
	{
		return this.driver;
	}

	/**
	 * <p>DB User 아이디를 셋팅한다.
	 * @param dbuser
	 * @deprecated properties 또는 init()매소드 사용을 권장함.
	 */
	public void setDBUser(String dbuser)
	{
		this.dbuser = dbuser;
	}

	/**
	 * DB User명을 얻는다.
	 * @return
	 */
	public String getDBUser()
	{
		return this.dbuser;
	}

	/**
	 * DB User password를 셋팅한다.
	 * @param dbpwd
	 * @deprecated properties 또는 init()매소드 사용을 권장함.
	 */
	public void setDBPwd(String dbpwd)
	{
		this.dbpwd = dbpwd;
	}

	/**
	 * DB User password를 얻는다.
	 * @return
	 */
	public String getDBPwd()
	{
		return this.dbpwd;
	}

	/**
	 * DB명을 셋팅한다.
	 * @param dbname
	 * @deprecated properties 또는 init()매소드 사용을 권장함.
	 */
	public void setDBName(String dbname)
	{
		this.dbname = dbname;
	}

	/**
	 * DB명을 얻는다.
	 * @return
	 */
	public String getDBName()
	{
		return this.dbname;
	}

	/**
	 * url prefix셋팅
	 * @param urlprefix
	 * @deprecated properties 또는 init()매소드 사용을 권장함.
	 */
	public void setUrlprefix(String urlprefix)
	{
		this.urlprefix = urlprefix;
	}

	/**
	 * url prefix를 얻는다.
	 * @return
	 */
	public String getUrlprefix()
	{
		return this.urlprefix;
	}

	/**
	 * db url을 얻는다.
	 * @return
	 */
	public String getUrl()
	{
		return this.urlprefix + this.dbname;
	}
}
