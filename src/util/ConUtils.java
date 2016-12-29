package com.cbscap.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.cbscap.fw.util.resource.ResourceLoader;

/**
 * <p>DB2�� Type2���(db2 clients�� catalog�� �����̿�)
 * Connection�� �����ϰ� DB Util�żҵ带 �����Ѵ�.
 * <br>db���������� classpath���� �����ؾ��Ѵ�.
 * <br>����Ʈ <b>config.properties</b>
 *
 * <pre>
 * <code>
 * config.properties ���Ͽ�
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
 * �����̷�
 * --------------------------------------------------------------
 * 20070920 ȫ�浿 import�� ����
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
	 * default ����Ӽ� ������ ����Ͽ� DB������ �Ѵ�.<br>
	 * config file�� : config.properties
	 */
	public ConUtils() {
		loadConfig(CONFIG_FILE);
	}


	/**
	 * �Էµ� ���ϸ����� DB���� properties�� ��´�.
	 * @param config properties ���ϸ�. ex) config.properties -> "config"
	 */
	public ConUtils(String config)
	{
		loadConfig(config);
	}


	/**
	 * ���� DB����Ӽ��� �����Ѵ�.
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
	 * db ���ἳ���� ��� properties������ �ε��ؼ� ���������� ��´�.
	 * @param config -properties���ϸ�, Ȯ���ڴ� ������.
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
	 * db���� ������ �����Ѵ�.
	 * @param dbname catalog�Ҷ� ����� db��, ex) wric_d
	 * @param dbuser db2���� ����ھ��̵�
	 * @param dbpwd db2���� ����ں�й�ȣ
	 */
	public void init(String dbname, String dbuser, String dbpwd)
	{
		setDBName(dbname);
		setDBUser(dbuser);
		setDBPwd(dbpwd);
	}

	/**
	 * DB connection�� �����Ѵ�.<br>
	 * ������ Connection�� auto commit�� false�� ���õȴ�.<br>
	 * ���� Connection�� close�Ҷ� �� rollback �Ǵ� commit�� �ؾ��Ѵ�.<br>
	 *
	 * <p>properties �Ǵ� ���� �Ķ���ͷ� ������ ���������� ����Ѵ�.
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
			log.error("����̹��� �����ϴ�. : " + e.getMessage());
			e.printStackTrace();
			throw new SQLException("DB ������ �Ҽ� �����ϴ�. : " + e.getMessage());
		}

		return connection;
	}

	/**
	 * PreparedStatement �Ǵ� Statement�� close�Ѵ�.
	 * <p>Exception�� �߻����� �ʴ´�.
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
	 * ResultSet�� close�Ѵ�.
	 * <p>Exception�� �߻����� �ʴ´�.
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
	 * Connection�� close�Ѵ�.
	 * <p>Exception�� �߻����� �ʴ´�.
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
	 * Connection�� rollback�Ѵ�.
	 * <p>Exception�� �߻����� �ʴ´�.
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
	 * <p> Connection�� commit�� �Ҷ� commit������ �Ѵ�.
	 * <pre>
	 * ex) cnt 99 �϶� 100�Ǵ��� commit�̸� commit�� ���Ѵ�.
	 *     cnt 100 �϶� 100�Ǵ��� commit�̸� commit�� �Ѵ�.
	 * </pre>
	 * @param con - Connection
	 * @param cnt - ��ġó���� ����
	 * @param cntOfCommit - commit����, ex) 100�Ǵ����� commmit
	 * @throws SQLException
	 */
	public static void userCommit(Connection con,int cnt, int cntOfCommit) throws SQLException{
		if(cnt % cntOfCommit == 0){
			userCommit(con);
		}
	}

	/**
	 * <p>Connection�� commit��  �Ҷ� commit���� (����Ʈ 10000�����ε�) �� �Ѵ�.
	 * @param con - Connection
	 * @param cnt - ��ġó���� ����
	 * @throws SQLException
	 */
	public static void userCommit(Connection con, int cnt) throws SQLException{
		userCommit(con,cnt,10000);
	}

	/**
	 * <p>Connection�� commit�Ѵ�.
	 * @param con
	 * @throws SQLException
	 */
	public static void userCommit(Connection con) throws SQLException	{
		con.commit();
	}

	/**
	 * batchó������ ������ ����Ѵ�.
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
	 * <p>DB driver���� �����Ѵ�.
	 * @param driver
	 * @deprecated properties �Ǵ� init()�żҵ� ����� ������.
	 */
	public void setDriverClassName(String driver)
	{
		this.driver = driver;
	}

	/**
	 * <p>DB driver���� ��´�.
	 * @return
	 */
	public String getDriverClassName()
	{
		return this.driver;
	}

	/**
	 * <p>DB User ���̵� �����Ѵ�.
	 * @param dbuser
	 * @deprecated properties �Ǵ� init()�żҵ� ����� ������.
	 */
	public void setDBUser(String dbuser)
	{
		this.dbuser = dbuser;
	}

	/**
	 * DB User���� ��´�.
	 * @return
	 */
	public String getDBUser()
	{
		return this.dbuser;
	}

	/**
	 * DB User password�� �����Ѵ�.
	 * @param dbpwd
	 * @deprecated properties �Ǵ� init()�żҵ� ����� ������.
	 */
	public void setDBPwd(String dbpwd)
	{
		this.dbpwd = dbpwd;
	}

	/**
	 * DB User password�� ��´�.
	 * @return
	 */
	public String getDBPwd()
	{
		return this.dbpwd;
	}

	/**
	 * DB���� �����Ѵ�.
	 * @param dbname
	 * @deprecated properties �Ǵ� init()�żҵ� ����� ������.
	 */
	public void setDBName(String dbname)
	{
		this.dbname = dbname;
	}

	/**
	 * DB���� ��´�.
	 * @return
	 */
	public String getDBName()
	{
		return this.dbname;
	}

	/**
	 * url prefix����
	 * @param urlprefix
	 * @deprecated properties �Ǵ� init()�żҵ� ����� ������.
	 */
	public void setUrlprefix(String urlprefix)
	{
		this.urlprefix = urlprefix;
	}

	/**
	 * url prefix�� ��´�.
	 * @return
	 */
	public String getUrlprefix()
	{
		return this.urlprefix;
	}

	/**
	 * db url�� ��´�.
	 * @return
	 */
	public String getUrl()
	{
		return this.urlprefix + this.dbname;
	}
}
