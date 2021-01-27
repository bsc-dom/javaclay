
package es.bsc.dataclay.dbhandler.sql.sqlite;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.DelegatingConnection;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sqlite.Function;

import es.bsc.dataclay.dbhandler.sql.SQLHandler;
import es.bsc.dataclay.dbhandler.sql.common.ConnectionWrapper;

public class SQLiteHandler extends SQLHandler<SQLiteHandlerConfig> {
	
	private static final Logger LOGGER = LogManager.getLogger(SQLiteHandler.class);

	public SQLiteHandler(final SQLiteHandlerConfig connConfig) {
		super(connConfig);
	}

	@Override
	protected void initDataSource() {
		this.dataSource = rootDataSource;
	}

	@Override
	protected void initRootDataSource() {
		if (rootDataSource == null) {
			rootDataSource = new SQLiteDataSource();
			String url = "jdbc:sqlite:";
			if(configuration.isInMemory()) {
				url += ":memory:";
			}else {
				url  += configuration.getDbPath();
			}
			LOGGER.debug("SQLITE DB URL = " + url);
			setConfiguration(rootDataSource, url);
		}
	}

	@Override
	public boolean databaseExists() {
		return new File(configuration.getDbPath()).exists();
	}

	@Override
	public void createDatabase() {

	}

	private void setConfiguration(final BasicDataSource ds, final String url) {
		ds.setUrl(url);
		ds.setDriverClassName(ExtendedSQLiteDriver.class.getCanonicalName());
		ds.setMaxTotal(1); // KEEP MaxTotal TO 1 to avoid the exception "[SQLITE_BUSY] database file is locked"
	}

	@Override
	public void dropAllDatabases() {
		try {
			FileUtils.deleteDirectory(new File(configuration.getFolder()));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}

// Solves locking due to managers are opening more than one connection from the datasource
class SQLiteDataSource extends BasicDataSource {
	
	private UncloseableConnection connection = null;

	@Override
	public Connection getConnection() throws SQLException {
		if(connection == null || connection.isClosed()) {
			final Connection conn = super.getConnection();
			// Getting the original SQLiteConnection to add the CHR function
			final PoolableConnection poolableConn = (PoolableConnection)((DelegatingConnection<?>)conn).getDelegate();
			Function.create(poolableConn.getDelegate(), "CHR", CHR);
			connection = new UncloseableConnection(conn);
		}
		return connection;
	}

	// Setting the delegating connection accessible
	@Override
	protected DataSource createDataSourceInstance() throws SQLException {
		final PoolingDataSource<?> pds = (PoolingDataSource<?>)super.createDataSourceInstance();
		pds.setAccessToUnderlyingConnectionAllowed(true);
		return pds;
	}

	private static final Function CHR = new Function(){
		// The CHR function is equivalent to sqlite's char function
		// and it's created for compatibility with Postgres CHR
		@Override
		protected void xFunc() throws SQLException {
			result((char)value_int(0) + "");
		}
	};
}

// TODO managers should not close the connection to a sqlite db
class UncloseableConnection extends ConnectionWrapper<SQLiteHandlerConfig>{

	private Map<String, PreparedStatement> preparedStatements = new ConcurrentHashMap<String, PreparedStatement>();
	public UncloseableConnection(final Connection connection) {
		super(connection);
	}

	@Override
	public void close() {
		// Ignore, needed because managers are closing the only possible sqlite connection
	}

	public void closeForReal() throws SQLException{
		super.close();
	}

	@Override
	public PreparedStatement prepareStatement(final String sql) throws SQLException {
		final PreparedStatement ps = super.prepareStatement(sql);
		//RACE CONDITION, FINALIZER IN PREPARED-STATEMENT LOCKS THE CONNECTION (COULD BE USED BY SOMEONE)
		preparedStatements.put(sql, ps);

		return ps;
	}
}
