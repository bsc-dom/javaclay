package es.bsc.dataclay.dbhandler.sql.common;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PreparedStatementWrapper implements PreparedStatement{
	final PreparedStatement ps;

	public PreparedStatementWrapper(final PreparedStatement ps) {
		this.ps = ps;
	}

	@Override
	public ResultSet executeQuery() throws SQLException{
		return new SQLResultSetWrapper(ps.executeQuery());
	}

	@Override
	public void setArray(int i, Array x) throws SQLException{
		if(x == null) {
			setString(i, null);
			return;
		}
		setString(i, x.toString());
	}

	@Override
	public String toString() {
		return this.ps.toString();
	}

	public int executeUpdate() throws SQLException{
		return ps.executeUpdate();
	}
	public void setNull(int arg0,int arg1,java.lang.String arg2) throws SQLException{
		ps.setNull(arg0,arg1,arg2);
	}
	public void setNull(int arg0,int arg1) throws SQLException{
		ps.setNull(arg0,arg1);
	}
	public void setBigDecimal(int arg0,java.math.BigDecimal arg1) throws SQLException{
		ps.setBigDecimal(arg0,arg1);
	}
	public void setString(int arg0,java.lang.String arg1) throws SQLException{
		ps.setString(arg0,arg1);
	}
	public void setBytes(int arg0,byte[] arg1) throws SQLException{
		ps.setBytes(arg0,arg1);
	}
	public void setDate(int arg0,java.sql.Date arg1,java.util.Calendar arg2) throws SQLException{
		ps.setDate(arg0,arg1,arg2);
	}
	public void setDate(int arg0,java.sql.Date arg1) throws SQLException{
		ps.setDate(arg0,arg1);
	}
	public void setAsciiStream(int arg0,java.io.InputStream arg1,int arg2) throws SQLException{
		ps.setAsciiStream(arg0,arg1,arg2);
	}
	public void setAsciiStream(int arg0,java.io.InputStream arg1,long arg2) throws SQLException{
		ps.setAsciiStream(arg0,arg1,arg2);
	}
	public void setAsciiStream(int arg0,java.io.InputStream arg1) throws SQLException{
		ps.setAsciiStream(arg0,arg1);
	}
	public void setUnicodeStream(int arg0,java.io.InputStream arg1,int arg2) throws SQLException{
		ps.setUnicodeStream(arg0,arg1,arg2);
	}
	public void setBinaryStream(int arg0,java.io.InputStream arg1,int arg2) throws SQLException{
		ps.setBinaryStream(arg0,arg1,arg2);
	}
	public void setBinaryStream(int arg0,java.io.InputStream arg1,long arg2) throws SQLException{
		ps.setBinaryStream(arg0,arg1,arg2);
	}
	public void setBinaryStream(int arg0,java.io.InputStream arg1) throws SQLException{
		ps.setBinaryStream(arg0,arg1);
	}
	public void clearParameters() throws SQLException{
		ps.clearParameters();
	}
	public void setObject(int arg0,java.lang.Object arg1,int arg2) throws SQLException{
		ps.setObject(arg0,arg1,arg2);
	}
	public void setObject(int arg0,java.lang.Object arg1,int arg2,int arg3) throws SQLException{
		ps.setObject(arg0,arg1,arg2,arg3);
	}
	public void setObject(int arg0,java.lang.Object arg1) throws SQLException{
		ps.setObject(arg0,arg1);
	}
	public void setObject(int arg0,java.lang.Object arg1,java.sql.SQLType arg2) throws SQLException{
		ps.setObject(arg0,arg1,arg2);
	}
	public void setObject(int arg0,java.lang.Object arg1,java.sql.SQLType arg2,int arg3) throws SQLException{
		ps.setObject(arg0,arg1,arg2,arg3);
	}
	public void addBatch() throws SQLException{
		ps.addBatch();
	}
	public void setCharacterStream(int arg0,java.io.Reader arg1,int arg2) throws SQLException{
		ps.setCharacterStream(arg0,arg1,arg2);
	}
	public void setCharacterStream(int arg0,java.io.Reader arg1,long arg2) throws SQLException{
		ps.setCharacterStream(arg0,arg1,arg2);
	}
	public void setCharacterStream(int arg0,java.io.Reader arg1) throws SQLException{
		ps.setCharacterStream(arg0,arg1);
	}
	public void setRef(int arg0,java.sql.Ref arg1) throws SQLException{
		ps.setRef(arg0,arg1);
	}
	public void setBlob(int arg0,java.io.InputStream arg1,long arg2) throws SQLException{
		ps.setBlob(arg0,arg1,arg2);
	}
	public void setBlob(int arg0,java.sql.Blob arg1) throws SQLException{
		ps.setBlob(arg0,arg1);
	}
	public void setBlob(int arg0,java.io.InputStream arg1) throws SQLException{
		ps.setBlob(arg0,arg1);
	}
	public void setClob(int arg0,java.io.Reader arg1,long arg2) throws SQLException{
		ps.setClob(arg0,arg1,arg2);
	}
	public void setClob(int arg0,java.sql.Clob arg1) throws SQLException{
		ps.setClob(arg0,arg1);
	}
	public void setClob(int arg0,java.io.Reader arg1) throws SQLException{
		ps.setClob(arg0,arg1);
	}
	public java.sql.ResultSetMetaData getMetaData() throws SQLException{
		return ps.getMetaData();
	}
	public java.sql.ParameterMetaData getParameterMetaData() throws SQLException{
		return ps.getParameterMetaData();
	}
	public void setRowId(int arg0,java.sql.RowId arg1) throws SQLException{
		ps.setRowId(arg0,arg1);
	}
	public void setNString(int arg0,java.lang.String arg1) throws SQLException{
		ps.setNString(arg0,arg1);
	}
	public void setNCharacterStream(int arg0,java.io.Reader arg1,long arg2) throws SQLException{
		ps.setNCharacterStream(arg0,arg1,arg2);
	}
	public void setNCharacterStream(int arg0,java.io.Reader arg1) throws SQLException{
		ps.setNCharacterStream(arg0,arg1);
	}
	public void setNClob(int arg0,java.io.Reader arg1,long arg2) throws SQLException{
		ps.setNClob(arg0,arg1,arg2);
	}
	public void setNClob(int arg0,java.sql.NClob arg1) throws SQLException{
		ps.setNClob(arg0,arg1);
	}
	public void setNClob(int arg0,java.io.Reader arg1) throws SQLException{
		ps.setNClob(arg0,arg1);
	}
	public void setSQLXML(int arg0,java.sql.SQLXML arg1) throws SQLException{
		ps.setSQLXML(arg0,arg1);
	}
	public long executeLargeUpdate() throws SQLException{
		return ps.executeLargeUpdate();
	}
	public void setTime(int arg0,java.sql.Time arg1,java.util.Calendar arg2) throws SQLException{
		ps.setTime(arg0,arg1,arg2);
	}
	public void setTime(int arg0,java.sql.Time arg1) throws SQLException{
		ps.setTime(arg0,arg1);
	}
	public void setURL(int arg0,java.net.URL arg1) throws SQLException{
		ps.setURL(arg0,arg1);
	}
	public boolean execute() throws SQLException{
		return ps.execute();
	}
	public void setBoolean(int arg0,boolean arg1) throws SQLException{
		ps.setBoolean(arg0,arg1);
	}
	public void setByte(int arg0,byte arg1) throws SQLException{
		ps.setByte(arg0,arg1);
	}
	public void setShort(int arg0,short arg1) throws SQLException{
		ps.setShort(arg0,arg1);
	}
	public void setInt(int arg0,int arg1) throws SQLException{
		ps.setInt(arg0,arg1);
	}
	public void setLong(int arg0,long arg1) throws SQLException{
		ps.setLong(arg0,arg1);
	}
	public void setFloat(int arg0,float arg1) throws SQLException{
		ps.setFloat(arg0,arg1);
	}
	public void setDouble(int arg0,double arg1) throws SQLException{
		ps.setDouble(arg0,arg1);
	}
	public void setTimestamp(int arg0,java.sql.Timestamp arg1,java.util.Calendar arg2) throws SQLException{
		ps.setTimestamp(arg0,arg1,arg2);
	}
	public void setTimestamp(int arg0,java.sql.Timestamp arg1) throws SQLException{
		ps.setTimestamp(arg0,arg1);
	}
	public java.sql.ResultSet executeQuery(java.lang.String arg0) throws SQLException{
		return ps.executeQuery(arg0);
	}
	public int executeUpdate(java.lang.String arg0,java.lang.String[] arg1) throws SQLException{
		return ps.executeUpdate(arg0,arg1);
	}
	public int executeUpdate(java.lang.String arg0,int[] arg1) throws SQLException{
		return ps.executeUpdate(arg0,arg1);
	}
	public int executeUpdate(java.lang.String arg0,int arg1) throws SQLException{
		return ps.executeUpdate(arg0,arg1);
	}
	public int executeUpdate(java.lang.String arg0) throws SQLException{
		return ps.executeUpdate(arg0);
	}
	public void addBatch(java.lang.String arg0) throws SQLException{
		ps.addBatch(arg0);
	}
	public long executeLargeUpdate(java.lang.String arg0) throws SQLException{
		return ps.executeLargeUpdate(arg0);
	}
	public long executeLargeUpdate(java.lang.String arg0,java.lang.String[] arg1) throws SQLException{
		return ps.executeLargeUpdate(arg0,arg1);
	}
	public long executeLargeUpdate(java.lang.String arg0,int arg1) throws SQLException{
		return ps.executeLargeUpdate(arg0,arg1);
	}
	public long executeLargeUpdate(java.lang.String arg0,int[] arg1) throws SQLException{
		return ps.executeLargeUpdate(arg0,arg1);
	}
	public int getMaxFieldSize() throws SQLException{
		return ps.getMaxFieldSize();
	}
	public void setMaxFieldSize(int arg0) throws SQLException{
		ps.setMaxFieldSize(arg0);
	}
	public int getMaxRows() throws SQLException{
		return ps.getMaxRows();
	}
	public void setMaxRows(int arg0) throws SQLException{
		ps.setMaxRows(arg0);
	}
	public void setEscapeProcessing(boolean arg0) throws SQLException{
		ps.setEscapeProcessing(arg0);
	}
	public int getQueryTimeout() throws SQLException{
		return ps.getQueryTimeout();
	}
	public void setQueryTimeout(int arg0) throws SQLException{
		ps.setQueryTimeout(arg0);
	}
	public void cancel() throws SQLException{
		ps.cancel();
	}
	public java.sql.SQLWarning getWarnings() throws SQLException{
		return ps.getWarnings();
	}
	public void clearWarnings() throws SQLException{
		ps.clearWarnings();
	}
	public void setCursorName(java.lang.String arg0) throws SQLException{
		ps.setCursorName(arg0);
	}
	public java.sql.ResultSet getResultSet() throws SQLException{
		return ps.getResultSet();
	}
	public int getUpdateCount() throws SQLException{
		return ps.getUpdateCount();
	}
	public boolean getMoreResults() throws SQLException{
		return ps.getMoreResults();
	}
	public boolean getMoreResults(int arg0) throws SQLException{
		return ps.getMoreResults(arg0);
	}
	public void setFetchDirection(int arg0) throws SQLException{
		ps.setFetchDirection(arg0);
	}
	public int getFetchDirection() throws SQLException{
		return ps.getFetchDirection();
	}
	public void setFetchSize(int arg0) throws SQLException{
		ps.setFetchSize(arg0);
	}
	public int getFetchSize() throws SQLException{
		return ps.getFetchSize();
	}
	public int getResultSetConcurrency() throws SQLException{
		return ps.getResultSetConcurrency();
	}
	public int getResultSetType() throws SQLException{
		return ps.getResultSetType();
	}
	public void clearBatch() throws SQLException{
		ps.clearBatch();
	}
	public int[] executeBatch() throws SQLException{
		return ps.executeBatch();
	}
	public java.sql.Connection getConnection() throws SQLException{
		return ps.getConnection();
	}
	public java.sql.ResultSet getGeneratedKeys() throws SQLException{
		return ps.getGeneratedKeys();
	}
	public int getResultSetHoldability() throws SQLException{
		return ps.getResultSetHoldability();
	}
	public boolean isClosed() throws SQLException{
		return ps.isClosed();
	}
	public void setPoolable(boolean arg0) throws SQLException{
		ps.setPoolable(arg0);
	}
	public boolean isPoolable() throws SQLException{
		return ps.isPoolable();
	}
	public void closeOnCompletion() throws SQLException{
		ps.closeOnCompletion();
	}
	public boolean isCloseOnCompletion() throws SQLException{
		return ps.isCloseOnCompletion();
	}
	public long getLargeUpdateCount() throws SQLException{
		return ps.getLargeUpdateCount();
	}
	public void setLargeMaxRows(long arg0) throws SQLException{
		ps.setLargeMaxRows(arg0);
	}
	public long getLargeMaxRows() throws SQLException{
		return ps.getLargeMaxRows();
	}
	public long[] executeLargeBatch() throws SQLException{
		return ps.executeLargeBatch();
	}
	public boolean execute(java.lang.String arg0,int[] arg1) throws SQLException{
		return ps.execute(arg0,arg1);
	}
	public boolean execute(java.lang.String arg0,int arg1) throws SQLException{
		return ps.execute(arg0,arg1);
	}
	public boolean execute(java.lang.String arg0,java.lang.String[] arg1) throws SQLException{
		return ps.execute(arg0,arg1);
	}
	public boolean execute(java.lang.String arg0) throws SQLException{
		return ps.execute(arg0);
	}
	public void close() throws SQLException{
		ps.close();
	}
	public boolean isWrapperFor(java.lang.Class arg0) throws SQLException{
		return ps.isWrapperFor(arg0);
	}
	public java.lang.Object unwrap(java.lang.Class arg0) throws SQLException{
		return ps.unwrap(arg0);
	}
}
