package es.bsc.dataclay.dbhandler.sql.sqlite;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.sqlite.SQLiteConnection;
import org.sqlite.jdbc4.JDBC4PreparedStatement;

import es.bsc.dataclay.dbhandler.sql.common.SQLArray;
import es.bsc.dataclay.dbhandler.sql.common.SQLResultSetWrapper;

public class ExtendedSQLiteDriver extends org.sqlite.JDBC{
	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		return privateCreateConnection(url, info);
	}

	// Equivalent to the JDBC's createConnection
	private static Connection privateCreateConnection(String url, Properties prop) throws SQLException {
		if (!isValidURL(url))
			return null;

		prop.setProperty("journal_mode", "MEMORY");
		prop.setProperty("synchronous", "OFF");
		url = url.trim();
		return new ExtendedSQLiteConnection(url, extractAddress(url), prop);
	}

	private static String extractAddress(String url) {
		// if no file name is given use a memory database
		return PREFIX.equalsIgnoreCase(url) ? ":memory:" : url.substring(PREFIX.length());
	}
}

class ExtendedSQLiteConnection extends org.sqlite.SQLiteConnection{

	public ExtendedSQLiteConnection(String url, String fileName, Properties info) throws SQLException {
		super(url, fileName, info);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int rst, int rsc, int rsh) throws SQLException {
		checkOpen();
		checkCursor(rst, rsc, rsh);

		return new ExtendedPreparedStatement(this, sql);
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		return new SQLArray(typeName, elements);
	}

	@Override
	public void close() {
		//System.out.println("CLOSE");
	}
}

class ExtendedPreparedStatement extends JDBC4PreparedStatement{

	public ExtendedPreparedStatement(SQLiteConnection conn, String sql) throws SQLException {
		super(conn, sql);
	}

	@Override
	public ResultSet executeQuery() throws SQLException{
		return new SQLResultSetWrapper(super.executeQuery());
	}

	@Override
	public void setArray(int i, Array x) throws SQLException{
		if(x == null) {
			setString(i, null);
			return;
		}
		setString(i, x.toString());
	}

	public String toString() {
		return this.sql;
	}
}
