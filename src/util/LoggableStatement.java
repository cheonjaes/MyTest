package com.cbscap.util;

import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.sql.Connection;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

/**
 * A <code>LoggableStatement<code> is a {@link java.sql.PreparedStatement PreparedStatement} with added logging capability.
 * <p>
 * In addition to the methods declared in <code>PreparedStatement</code>,
 * <code>LoggableStatement<code> provides a method {@link #getQueryString} which can be used to get the query string in a format
 * suitable for logging.
 *
 * @author Jens Wyke (jens.wyke@se.ibm.com)
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class LoggableStatement implements PreparedStatement {
	private static Logger log = Logger.getLogger(LoggableStatement.class.getName());
	//private boolean debugFg = log.isDebugEnabled();

	/**
	 * used for storing parameter values needed for producing log
	 */
	private ArrayList parameterValues;

	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		// TODO Auto-generated method stub
	}


	@Override
	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		// TODO Auto-generated method stub
	}


	@Override
	public void setNString(int parameterIndex, String value) throws SQLException {
		// TODO Auto-generated method stub
	}


	@Override
	public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
		// TODO Auto-generated method stub
	}


	@Override
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		// TODO Auto-generated method stub
	}


	@Override
	public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
		// TODO Auto-generated method stub
	}


	@Override
	public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
		// TODO Auto-generated method stub
	}


	@Override
	public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
		// TODO Auto-generated method stub
	}


	@Override
	public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
		// TODO Auto-generated method stub
	}


	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
		// TODO Auto-generated method stub
	}


	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
		// TODO Auto-generated method stub
	}


	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
		// TODO Auto-generated method stub
	}


	@Override
	public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
		// TODO Auto-generated method stub
	}


	@Override
	public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
		// TODO Auto-generated method stub
	}


	@Override
	public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
	}


	@Override
	public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
		// TODO Auto-generated method stub
	}


	@Override
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
	}


	@Override
	public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
		// TODO Auto-generated method stub
	}


	@Override
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

	/**
	 *the query string with question marks as parameter placeholders
	 */
	private String sqlTemplate;

	/**
	 *  a statement created from a real database connection
	 */
	private PreparedStatement wrappedStatement;

	/**
	 * Constructs a LoggableStatement.
	 */
	public LoggableStatement(Connection connection, String sql) throws SQLException {
		wrappedStatement = connection.prepareStatement(sql);
		sqlTemplate = sql;
		parameterValues = new ArrayList();
	}


	public LoggableStatement(boolean updateFg, Connection connection, String sql) throws SQLException {
		if (updateFg) {
			wrappedStatement = connection.prepareStatement(sql);
		} else {
			wrappedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		}
		sqlTemplate = sql;
		parameterValues = new ArrayList();
	}

	/**
	 * Adds a set of parameters to the batch.
	 */
	public void addBatch() throws java.sql.SQLException {
		wrappedStatement.addBatch();
	}
	/**
	 * Adds a SQL command to the current batch of commmands for the statement.
	 * This method is optional.
	 */
	public void addBatch(String sql) throws java.sql.SQLException {
		wrappedStatement.addBatch(sql);
	}
	/**
	 * Cancels this <code>Statement</code> object if both the DBMS and
	 * driver support aborting an SQL statement.
	 */
	public void cancel() throws SQLException {
		wrappedStatement.cancel();
	}
	/**
	 * Makes the set of commands in the current batch empty.
	 * This method is optional.
	 */
	public void clearBatch() throws java.sql.SQLException {
		wrappedStatement.clearBatch();
	}
	/**
	 * Clears the current parameter values immediately.
	 */
	public void clearParameters() throws java.sql.SQLException {
		wrappedStatement.clearParameters();
	}
	/**
	 * Clears all the warnings reported on this <code>Statement</code>
	 * object. After a call to this method,
	 * the method <code>getWarnings</code> will return
	 * null until a new warning is reported for this Statement.
	 */
	public void clearWarnings() throws java.sql.SQLException {
		wrappedStatement.clearWarnings();
	}
	/**
	 * Releases this <code>Statement</code> object's database
	 * and JDBC resources immediately instead of waiting for
	 * this to happen when it is automatically closed.
	 */
	public void close() throws java.sql.SQLException {
		wrappedStatement.close();
	}
	/**
	 * Executes any kind of SQL statement.
	 */
	public boolean execute() throws java.sql.SQLException {
		return wrappedStatement.execute();
	}
	
	/**
	 * Executes a SQL statement that may return multiple results.
	 */
	public boolean execute(String sql) throws java.sql.SQLException {
		return wrappedStatement.execute(sql);
	}
	
	public int[] executeBatch() throws java.sql.SQLException {
		long start = System.currentTimeMillis();
		boolean isInfoEnabled = log.isInfoEnabled();
		try {
//			if(isInfoEnabled) {
//				log.info( this.getQueryString() );
//			}
			return wrappedStatement.executeBatch();
		}finally {
			if(isInfoEnabled) {
				log.info(" : 걸린시간 - " + (System.currentTimeMillis() - start));
			}
		}
	}
	/**
	 * Executes the SQL query in this <code>PreparedStatement</code> object
	 * and returns the result set generated by the query.
	 */
	public java.sql.ResultSet executeQuery() throws java.sql.SQLException {
		long start = System.currentTimeMillis();
		boolean isInfoEnabled = log.isInfoEnabled();
		try {
			if(isInfoEnabled) {
				log.info( this.getQueryString() );
			}
			return wrappedStatement.executeQuery();
		} finally {
			if(isInfoEnabled) {
				log.info("수행시간 - " + (System.currentTimeMillis() - start));
			}
		}
	}
	/**
	 * Executes a SQL statement that returns a single ResultSet.
	 */
	public java.sql.ResultSet executeQuery(String sql) throws java.sql.SQLException {
		return wrappedStatement.executeQuery(sql);
	}
	
	/**
	 * Executes the SQL INSERT, UPDATE or DELETE statement
	 */
	public int executeUpdate() throws java.sql.SQLException {
		long start = System.currentTimeMillis();
		int updateCnt = 0;
		boolean isInfoEnabled = log.isInfoEnabled();
		try {
			if (isInfoEnabled) {
				log.info( this.getQueryString() );
			}
			updateCnt = wrappedStatement.executeUpdate();
			return updateCnt;
		} finally {
			if(isInfoEnabled) {
				log.info("수행시간 - " + (System.currentTimeMillis() - start) +", 변경건수 : " + updateCnt );
			}
		}
	}
	
	/**
	 * Executes an SQL INSERT, UPDATE or DELETE statement
	 */
	public int executeUpdate(String sql) throws java.sql.SQLException {
		long start = System.currentTimeMillis();
		int updateCnt = 0;
		boolean isInfoEnabled = log.isInfoEnabled();
		try {
			if(isInfoEnabled) {
				log.info( sql );
			}

			updateCnt = wrappedStatement.executeUpdate(sql);
			return updateCnt;
		} finally {
			if(isInfoEnabled) {
				log.info("수행시간 - " + (System.currentTimeMillis() - start) +", 변경건수 : " + updateCnt );
			}
		}
	}
	
	public java.sql.Connection getConnection() throws java.sql.SQLException {
		return wrappedStatement.getConnection();
	}
	
	public int getFetchDirection() throws java.sql.SQLException {
		return wrappedStatement.getFetchDirection();
	}
	
	public int getFetchSize() throws java.sql.SQLException {
		return wrappedStatement.getFetchSize();
	}
	
	public int getMaxFieldSize() throws java.sql.SQLException {
		return wrappedStatement.getMaxFieldSize();
	}
	
	public int getMaxRows() throws java.sql.SQLException {
		return wrappedStatement.getMaxRows();
	}
	
	public java.sql.ResultSetMetaData getMetaData()
		throws java.sql.SQLException {
		return wrappedStatement.getMetaData();
	}
	
	public boolean getMoreResults() throws java.sql.SQLException {
		return wrappedStatement.getMoreResults();
	}
	
	public int getQueryTimeout() throws java.sql.SQLException {
		return wrappedStatement.getQueryTimeout();
	}
	
	public java.sql.ResultSet getResultSet() throws java.sql.SQLException {
		return wrappedStatement.getResultSet();
	}
	
	public int getResultSetConcurrency() throws java.sql.SQLException {
		return wrappedStatement.getResultSetConcurrency();
	}
	
	public int getResultSetType() throws java.sql.SQLException {
		return wrappedStatement.getResultSetType();
	}
	
	public int getUpdateCount() throws java.sql.SQLException {
		return wrappedStatement.getUpdateCount();
	}
	
	public java.sql.SQLWarning getWarnings() throws java.sql.SQLException {
		return wrappedStatement.getWarnings();
	}
	
	public void setArray(int i, java.sql.Array x) throws java.sql.SQLException {
		wrappedStatement.setArray(i, x);
		saveQueryParamValue(i, x);
	}
	
	public void setAsciiStream(int parameterIndex, java.io.InputStream x, int length) throws java.sql.SQLException {
		wrappedStatement.setAsciiStream(parameterIndex, x, length);
		saveQueryParamValue(parameterIndex, x);
	}
	
	public void setBigDecimal(int parameterIndex, java.math.BigDecimal x) throws java.sql.SQLException {
		wrappedStatement.setBigDecimal(parameterIndex, x);
		saveQueryParamValue(parameterIndex, x);
	}
	
	public void setBinaryStream(int parameterIndex, java.io.InputStream x, int length) throws java.sql.SQLException {
		wrappedStatement.setBinaryStream(parameterIndex, x, length);
		saveQueryParamValue(parameterIndex, x);
	}
	
	public void setBlob(int i, java.sql.Blob x) throws java.sql.SQLException {
		wrappedStatement.setBlob(i, x);
		saveQueryParamValue(i, x);
	}
	
	public void setBoolean(int parameterIndex, boolean x) throws java.sql.SQLException {
		wrappedStatement.setBoolean(parameterIndex, x);
		saveQueryParamValue(parameterIndex, new Boolean(x));
	}
	
	public void setByte(int parameterIndex, byte x) throws java.sql.SQLException {
		wrappedStatement.setByte(parameterIndex, x);
		saveQueryParamValue(parameterIndex, new Integer(x));
	}
	
	public void setBytes(int parameterIndex, byte[] x) throws java.sql.SQLException {
		wrappedStatement.setBytes(parameterIndex, x);
		saveQueryParamValue(parameterIndex, x);
	}
	
	public void setCharacterStream(int parameterIndex, java.io.Reader reader, int length) throws java.sql.SQLException {
		wrappedStatement.setCharacterStream(parameterIndex, reader, length);
		saveQueryParamValue(parameterIndex, reader);
	}
	
	public void setClob(int i, java.sql.Clob x) throws java.sql.SQLException {
		wrappedStatement.setClob(i, x);
		saveQueryParamValue(i, x);
	}
	
	public void setCursorName(String name) throws java.sql.SQLException {
		wrappedStatement.setCursorName(name);
	}
	
	public void setDate(int parameterIndex, java.sql.Date x) throws java.sql.SQLException {
		wrappedStatement.setDate(parameterIndex, x);
		saveQueryParamValue(parameterIndex, x);
	}
	
	public void setDate(int parameterIndex, java.sql.Date x, java.util.Calendar cal) throws java.sql.SQLException {
		wrappedStatement.setDate(parameterIndex, x, cal);
		saveQueryParamValue(parameterIndex, x);
	}
	
	public void setDouble(int parameterIndex, double x) throws java.sql.SQLException {
		wrappedStatement.setDouble(parameterIndex, x);
		saveQueryParamValue(parameterIndex, new Double(x));
	}
	
	public void setEscapeProcessing(boolean enable) throws java.sql.SQLException {
		wrappedStatement.setEscapeProcessing(enable);
	}
	
	public void setFetchDirection(int direction) throws java.sql.SQLException {
		wrappedStatement.setFetchDirection(direction);
	}
	
	public void setFetchSize(int rows) throws java.sql.SQLException {
		wrappedStatement.setFetchSize(rows);
	}
	
	public void setFloat(int parameterIndex, float x) throws java.sql.SQLException {
		wrappedStatement.setFloat(parameterIndex, x);
		saveQueryParamValue(parameterIndex, new Float(x));
	}
	
	public void setInt(int parameterIndex, int x) throws java.sql.SQLException {
		wrappedStatement.setInt(parameterIndex, x);
		saveQueryParamValue(parameterIndex, new Integer(x));
	}
	
	public void setLong(int parameterIndex, long x) throws java.sql.SQLException {
		wrappedStatement.setLong(parameterIndex, x);
		saveQueryParamValue(parameterIndex, new Long(x));
	}
	
	public void setMaxFieldSize(int max) throws java.sql.SQLException {
		wrappedStatement.setMaxFieldSize(max);
	}
	
	public void setMaxRows(int max) throws java.sql.SQLException {
		wrappedStatement.setMaxRows(max);
	}
	
	public void setNull(int parameterIndex, int sqlType) throws java.sql.SQLException {
		wrappedStatement.setNull(parameterIndex, sqlType);
		saveQueryParamValue(parameterIndex, null);
	}
	
	public void setNull(int paramIndex, int sqlType, String typeName) throws java.sql.SQLException {
		wrappedStatement.setNull(paramIndex, sqlType, typeName);
		saveQueryParamValue(paramIndex, null);
	}
	
	public void setObject(int parameterIndex, Object x) throws java.sql.SQLException {
		wrappedStatement.setObject(parameterIndex, x);
		saveQueryParamValue(parameterIndex, x);
	}
	
	public void setObject(int parameterIndex, Object x, int targetSqlType) throws java.sql.SQLException {
		wrappedStatement.setObject(parameterIndex, x, targetSqlType);
		saveQueryParamValue(parameterIndex, x);
	}
	
	public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws java.sql.SQLException {
		wrappedStatement.setObject(parameterIndex, x, targetSqlType, scale);
		saveQueryParamValue(parameterIndex, x);
	}
	
	public void setQueryTimeout(int seconds) throws java.sql.SQLException {
		wrappedStatement.setQueryTimeout(seconds);
	}
	
	public void setRef(int i, java.sql.Ref x) throws java.sql.SQLException {
		wrappedStatement.setRef(i, x);
		saveQueryParamValue(i, x);
	}
	
	public void setShort(int parameterIndex, short x) throws java.sql.SQLException {
		wrappedStatement.setShort(parameterIndex, x);
		saveQueryParamValue(parameterIndex, new Integer(x));
	}
	
	public void setString(int parameterIndex, String x) throws java.sql.SQLException {
		wrappedStatement.setString(parameterIndex, x);
		saveQueryParamValue(parameterIndex, x);
	}
	
	public void setTime(int parameterIndex, java.sql.Time x) throws java.sql.SQLException {
		wrappedStatement.setTime(parameterIndex, x);
		saveQueryParamValue(parameterIndex, x);
	}
	
	public void setTime(int parameterIndex, java.sql.Time x, java.util.Calendar cal) throws java.sql.SQLException {
		wrappedStatement.setTime(parameterIndex, x, cal);
		saveQueryParamValue(parameterIndex, x);
	}
	
	public void setTimestamp(int parameterIndex, java.sql.Timestamp x) throws java.sql.SQLException {
		wrappedStatement.setTimestamp(parameterIndex, x);
		saveQueryParamValue(parameterIndex, x);
	}
	
	public void setTimestamp(int parameterIndex, java.sql.Timestamp x, java.util.Calendar cal) throws java.sql.SQLException {
		wrappedStatement.setTimestamp(parameterIndex, x, cal);
		saveQueryParamValue(parameterIndex, x);
	}
	
	public void setUnicodeStream(int parameterIndex, java.io.InputStream x, int length) throws java.sql.SQLException {
		wrappedStatement.setUnicodeStream(parameterIndex, x, length);
		saveQueryParamValue(parameterIndex, x);
	}

	public String getQueryString() {

		StringBuffer buf = new StringBuffer();
		int qMarkCount = 0;
		
		StringTokenizer tok = new StringTokenizer(sqlTemplate+" ", "?");
		while (tok.hasMoreTokens()) {
			String oneChunk = tok.nextToken();
			buf.append(oneChunk);

			try {
				Object value;
				if (parameterValues.size() > 1 + qMarkCount) {
					value = parameterValues.get(1 + qMarkCount++);
				} else {
					if (tok.hasMoreTokens()) {
						value = null;
					} else {
						value = "";
					}
				}
				buf.append("" + value);
			} catch (Throwable e) {
				buf.append("ERROR WHEN PRODUCING QUERY STRING FOR LOG."+ e.toString());
				// catch this without whining, if this fails the only thing wrong is probably this class
			}
		}
		return buf.toString().trim();
	}

	private void saveQueryParamValue(int position, Object obj) {
		String strValue;
		if (obj instanceof String || obj instanceof Date) {
			// if we have a String or Date , include '' in the saved value
			strValue = "'" + obj + "'";
		} else {

			if (obj == null) {
				// convert null to the string null
				strValue = "null";
			} else {
				// unknown object (includes all Numbers), just call toString
				strValue = obj.toString();
			}
		}

		// if we are setting a position larger than current size of parameterValues, first make it larger
		while (position >= parameterValues.size()) {
			parameterValues.add(null);
		}
		// save the parameter
		parameterValues.set(position, strValue);
	}

	public void setURL(int parameterIndex, URL x) throws SQLException {
		wrappedStatement.setURL(parameterIndex, x);
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		return wrappedStatement.getParameterMetaData();
	}

	public int getResultSetHoldability() throws SQLException {
		return wrappedStatement.getResultSetHoldability();
	}

	public boolean getMoreResults(int current) throws SQLException {
		return wrappedStatement.getMoreResults(current);
	}

	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		return wrappedStatement.executeUpdate(sql, autoGeneratedKeys);
	}

	
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		return wrappedStatement.execute(sql, autoGeneratedKeys);
	}

	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		return wrappedStatement.executeUpdate(sql, columnIndexes);
	}

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		return wrappedStatement.execute(sql, columnIndexes);
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		return wrappedStatement.getGeneratedKeys();
	}

	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		return wrappedStatement.executeUpdate(sql, columnNames);
	}

	public boolean execute(String sql, String[] columnNames) throws SQLException {
		return wrappedStatement.execute(sql, columnNames);
	}
}
