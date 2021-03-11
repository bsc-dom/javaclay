package es.bsc.dataclay.dbhandler.sql.sqlite;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sqlite.SQLiteConnection;
import org.sqlite.jdbc4.JDBC4PreparedStatement;

import es.bsc.dataclay.dbhandler.sql.common.SQLArray;
import es.bsc.dataclay.dbhandler.sql.common.SQLResultSetWrapper;

public class ExtendedPreparedStatement extends JDBC4PreparedStatement{

	/** Logger. */
	private static final Logger logger = LogManager.getLogger("SQLiteHandler");

	public ExtendedPreparedStatement(SQLiteConnection conn, String sql) throws SQLException {
		super(conn, sql);
	}

	@Override
	public ResultSet executeQuery() throws SQLException{
		return new SQLResultSetWrapper(super.executeQuery());

	}

	@Override
	public int executeUpdate() throws SQLException{
		return super.executeUpdate();

	}

	@Override
	public boolean execute() throws SQLException{

		logger.debug(this.sql);
		return super.execute();
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
