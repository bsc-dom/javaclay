package es.bsc.dataclay.dbhandler.sql.common;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.Properties;

public class SQLConnectionWrapper implements Connection{
	final Connection connection;
	
	public SQLConnectionWrapper(final Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return new PreparedStatementWrapper(connection.prepareStatement(sql, 0, 0, 0));
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		return new SQLArray(typeName, elements);
	}
	
	public void abort(java.util.concurrent.Executor arg0) throws SQLException{
		connection.abort(arg0);
	}
	public java.sql.Statement createStatement(int arg0,int arg1,int arg2) throws SQLException{
		return connection.createStatement(arg0,arg1,arg2);
	}
	public java.sql.Statement createStatement(int arg0,int arg1) throws SQLException{
		return connection.createStatement(arg0,arg1);
	}
	public java.sql.Statement createStatement() throws SQLException{
		return connection.createStatement();
	}
	public java.sql.PreparedStatement prepareStatement(java.lang.String arg0,java.lang.String[] arg1) throws SQLException{
		return connection.prepareStatement(arg0,arg1);
	}
	public java.sql.PreparedStatement prepareStatement(java.lang.String arg0,int arg1) throws SQLException{
		return connection.prepareStatement(arg0,arg1);
	}
	public java.sql.PreparedStatement prepareStatement(java.lang.String arg0,int arg1,int arg2) throws SQLException{
		return connection.prepareStatement(arg0,arg1,arg2);
	}
	public java.sql.PreparedStatement prepareStatement(java.lang.String arg0,int[] arg1) throws SQLException{
		return connection.prepareStatement(arg0,arg1);
	}
	public java.sql.CallableStatement prepareCall(java.lang.String arg0,int arg1,int arg2) throws SQLException{
		return connection.prepareCall(arg0,arg1,arg2);
	}
	public java.sql.CallableStatement prepareCall(java.lang.String arg0,int arg1,int arg2,int arg3) throws SQLException{
		return connection.prepareCall(arg0,arg1,arg2,arg3);
	}
	public java.sql.CallableStatement prepareCall(java.lang.String arg0) throws SQLException{
		return connection.prepareCall(arg0);
	}
	public java.lang.String nativeSQL(java.lang.String arg0) throws SQLException{
		return connection.nativeSQL(arg0);
	}
	public void setAutoCommit(boolean arg0) throws SQLException{
		connection.setAutoCommit(arg0);
	}
	public boolean getAutoCommit() throws SQLException{
		return connection.getAutoCommit();
	}
	public void commit() throws SQLException{
		connection.commit();
	}
	public void rollback() throws SQLException{
		connection.rollback();
	}
	public void rollback(java.sql.Savepoint arg0) throws SQLException{
		connection.rollback(arg0);
	}
	public boolean isClosed() throws SQLException{
		return connection.isClosed();
	}
	public java.sql.DatabaseMetaData getMetaData() throws SQLException{
		return connection.getMetaData();
	}
	public void setCatalog(java.lang.String arg0) throws SQLException{
		connection.setCatalog(arg0);
	}
	public java.lang.String getCatalog() throws SQLException{
		return connection.getCatalog();
	}
	public void setTransactionIsolation(int arg0) throws SQLException{
		connection.setTransactionIsolation(arg0);
	}
	public int getTransactionIsolation() throws SQLException{
		return connection.getTransactionIsolation();
	}
	public java.sql.SQLWarning getWarnings() throws SQLException{
		return connection.getWarnings();
	}
	public void clearWarnings() throws SQLException{
		connection.clearWarnings();
	}
	public java.util.Map getTypeMap() throws SQLException{
		return connection.getTypeMap();
	}
	public void setTypeMap(java.util.Map arg0) throws SQLException{
		connection.setTypeMap(arg0);
	}
	public void setHoldability(int arg0) throws SQLException{
		connection.setHoldability(arg0);
	}
	public int getHoldability() throws SQLException{
		return connection.getHoldability();
	}
	public java.sql.Savepoint setSavepoint() throws SQLException{
		return connection.setSavepoint();
	}
	public java.sql.Savepoint setSavepoint(java.lang.String arg0) throws SQLException{
		return connection.setSavepoint(arg0);
	}
	public void releaseSavepoint(java.sql.Savepoint arg0) throws SQLException{
		connection.releaseSavepoint(arg0);
	}
	public java.sql.Clob createClob() throws SQLException{
		return connection.createClob();
	}
	public java.sql.Blob createBlob() throws SQLException{
		return connection.createBlob();
	}
	public java.sql.NClob createNClob() throws SQLException{
		return connection.createNClob();
	}
	public java.sql.SQLXML createSQLXML() throws SQLException{
		return connection.createSQLXML();
	}
	public boolean isValid(int arg0) throws SQLException{
		return connection.isValid(arg0);
	}
	public java.lang.String getClientInfo(java.lang.String arg0) throws SQLException{
		return connection.getClientInfo(arg0);
	}
	public java.util.Properties getClientInfo() throws SQLException{
		return connection.getClientInfo();
	}
	public java.sql.Struct createStruct(java.lang.String arg0,java.lang.Object[] arg1) throws SQLException{
		return connection.createStruct(arg0,arg1);
	}
	public void setSchema(java.lang.String arg0) throws SQLException{
		connection.setSchema(arg0);
	}
	public java.lang.String getSchema() throws SQLException{
		return connection.getSchema();
	}
	public void setNetworkTimeout(java.util.concurrent.Executor arg0,int arg1) throws SQLException{
		connection.setNetworkTimeout(arg0,arg1);
	}
	public int getNetworkTimeout() throws SQLException{
		return connection.getNetworkTimeout();
	}
	public void setReadOnly(boolean arg0) throws SQLException{
		connection.setReadOnly(arg0);
	}
	public void close() throws SQLException{
		connection.close();
	}
	public boolean isReadOnly() throws SQLException{
		return connection.isReadOnly();
	}
	public boolean isWrapperFor(java.lang.Class arg0) throws SQLException{
		return connection.isWrapperFor(arg0);
	}
	public java.lang.Object unwrap(java.lang.Class arg0) throws SQLException{
		return connection.unwrap(arg0);
	}

	@Override
	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		connection.setClientInfo(name, value);
	}

	@Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		connection.setClientInfo(properties);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
