
package es.bsc.dataclay.dbhandler.sql.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.dbhandler.sql.common.ConnectionWrapper;
import es.bsc.dataclay.util.Configuration;

/**
 * Objects instantiated by classes that implement the DataSource represent a particular DBMS or some other data source, such as
 * a file. A DataSource object represents a particular DBMS or some other data source, such as a file. If a company uses more
 * than one data source, it will deploy a separate DataSource object for each of them. This class represents multithreaded data
 * source.
 **/
public class DataSourceMultithread extends BasicDataSource {

	/** Logger. */
	private static final Logger logger = LogManager.getLogger("DataSourceMultithread");

	/** Indicates if debug is enabled. */
	protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** Connections. */
	private final ThreadLocal<ConnectionWrapper<PostgresHandlerConf>> threadConnection = new ThreadLocal<>();

	/** Number of connections. */
	private final AtomicInteger numberConnections = new AtomicInteger(0);
	
	/**
	 * Get connection
	 * 
	 * @return Connection
	 * @throws SQLException
	 *             if some exception occurred
	 */
	@Override
	public Connection getConnection() throws SQLException {
		try {
			ConnectionWrapper<PostgresHandlerConf> connection = threadConnection.get();
			if (connection == null) {
				connection = new MultiThreadConnectionWrapper(super.getConnection());
				threadConnection.set(connection);
			} else {
				numberConnections.incrementAndGet();
			}
			return connection;
		} catch (final SQLException sqlE) {
			sqlE.printStackTrace();
			logger.error("Error in getConnection method", sqlE);
			throw sqlE;
		}
	}
	
	class MultiThreadConnectionWrapper extends ConnectionWrapper<PostgresHandlerConf> {
		
		public MultiThreadConnectionWrapper(final Connection connection) {
			super(connection);
		}

		@Override
		public PreparedStatement prepareStatement(final String sql) throws SQLException {
			final PreparedStatement ps = super.prepareStatement(sql);
			return ps;
		}

		/**
		 * Close connections
		 * 
		 * @throws SQLException
		 *             If exception produced during close.
		 */
		@Override
		public void close() throws SQLException {
			final ConnectionWrapper<PostgresHandlerConf> connection = threadConnection.get();

			if (connection == null) {
				return;
			}
			final int numConns = numberConnections.decrementAndGet(); 
			if (numConns <= 0) {
				super.close();
				threadConnection.remove();
			}
		}
	}
}
