
package es.bsc.dataclay.dbhandler.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.dbhandler.DBHandler;
import es.bsc.dataclay.dbhandler.DBHandlerConf;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectNotExistException;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.ObjectID;

public abstract class SQLHandler<T extends DBHandlerConf> implements DBHandler {

	/** Logger. */
	private static final Logger logger = LogManager.getLogger("SQLHandler");

	/** Indicates if debug is enabled. */
	protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** Data sources. */
	protected BasicDataSource dataSource;

	/** Root data source for superuser operations (drop db, create db...). */
	protected BasicDataSource rootDataSource;

	/** Configuration of the connection. */
	protected final T configuration;

	/**
	 * Constructor.
	 * 
	 * @param connConfig
	 *            Configuration.
	 */
	public SQLHandler(final T connConfig) {
		this.configuration = connConfig;
	}

	/**
	 * Open database.
	 */
	@Override
	public void open() {
		openRoot();
		initDataSource();
		if (!databaseExists()) {
			createDatabase();
		}
		createTables();
	}

	/**
	 * Open root session to database.
	 */
	public void openRoot() {
		initRootDataSource();
	}

	protected abstract void initDataSource();

	protected abstract void initRootDataSource();

	public abstract boolean databaseExists();

	public abstract void createDatabase();

	/**
	 * Drop all databases. FOR TESTING PURPOSES.
	 */
	public abstract void dropAllDatabases();

	/**
	 * Initialize database
	 */
	public void createTables() {
		final String sqlStatementStr = DataServiceDBSQLStatements.SqlStatements.CREATE_TABLE_DATASERVICE
				.getSqlStatement();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement createStatement = conn.prepareStatement(sqlStatementStr);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing {}", createStatement);
			}
			createStatement.execute();

		} catch (final SQLException e) {
			logger.debug("[==DB==] Ignored exception in createTables", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	protected Connection getRootConnection() {
		Connection conn = null;
		boolean connected = false;
		int retry = 0;
		final short maxRetries = Configuration.Flags.MAX_RETRY_DATABASE_CONN.getShortValue();
		while (!connected) {
			try {
				conn = rootDataSource.getConnection();
				connected = true;
			} catch (final SQLException e) {
				logger.debug("Catched exception in-mid retries", e);
				retry++;
				if (retry > maxRetries) {
					logger.error("maxRetries reached, aborting connection to database");
					return null;
				}
				logger.info("DB not started yet. Retrying...");
				try {
					Thread.sleep(Configuration.Flags.RETRY_DATABASE_CONN_TIME.getLongValue());
				} catch (final InterruptedException interrupt_e) {
					logger.error("Interrupted while waiting between reconnection, aborting.", interrupt_e);
				}
			}
		}
		return conn;
	}

	public T getConfiguration() {
		return configuration;
	}

	// ======================= STORE ======================= //

	@Override
	public void store(final ObjectID objectID, final byte[] bytes) {
		final String sqlStatementStr = DataServiceDBSQLStatements.SqlStatements.INSERT_OBJECT.getSqlStatement();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement insertStatement = conn.prepareStatement(sqlStatementStr);
			insertStatement.setObject(1, objectID.getId());
			insertStatement.setBytes(2, bytes);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing {}", insertStatement);
			}
			insertStatement.executeUpdate();

		} catch (final Exception e) {
			// Ignore
			// FOR REPLICA DESIGN
			logger.debug("[==DB==] Ignored exception", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
	}

	@Override
	public byte[] get(final ObjectID objectID) {
		byte[] result = null;

		final String sqlStatementStr = DataServiceDBSQLStatements.SqlStatements.SELECT_OBJECT.getSqlStatement();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(sqlStatementStr);
			ResultSet resultSet = null;
			selectStatement.setObject(1, objectID.getId());
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing {}", selectStatement);
			}
			resultSet = selectStatement.executeQuery();
			final boolean hasResults = resultSet.next();
			if (hasResults) {
				result = resultSet.getBytes(1);
			} else {
				throw new DbObjectNotExistException(objectID);
			}
		} catch (final SQLException e) {
			logger.debug("[==DB==] Ignored exception in get", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
		return result;
	}

	@Override
	public boolean exists(final ObjectID objectID) {
		boolean result = false;

		final String sqlStatementStr = DataServiceDBSQLStatements.SqlStatements.EXISTS_OBJECT.getSqlStatement();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(sqlStatementStr);
			ResultSet resultSet = null;
			selectStatement.setObject(1, objectID.getId());
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			resultSet = selectStatement.executeQuery();
			final boolean hasResults = resultSet.next();
			if (hasResults) {
				result = resultSet.getBoolean(1);
			}
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executed " + selectStatement + " with return " + result);
			}

		} catch (final SQLException e) {
			logger.debug("[==DB==] Ignored exception in get", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
		return result;
	}

	@Override
	public void update(final ObjectID objectID, final byte[] newbytes) {
		// CHECKSTYLE:OFF
		final String sqlStatementStr = DataServiceDBSQLStatements.SqlStatements.UPDATE_OBJECT.getSqlStatement();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement updateStatement = conn.prepareStatement(sqlStatementStr);

			updateStatement.setBytes(1, newbytes);
			updateStatement.setObject(2, objectID.getId());
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + updateStatement);
			}
			updateStatement.executeUpdate();

		} catch (final SQLException e) {
			logger.debug("SQL Exception while performing update operation on object {}", objectID);
			logger.debug("update received SQL error", e);
			throw new DbObjectNotExistException(objectID);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
		// CHECKSTYLE:ON
	}

	@Override
	public void delete(final ObjectID objectID) {
		final String sqlStatementStr = DataServiceDBSQLStatements.SqlStatements.DELETE_OBJECT.getSqlStatement();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement deleteStatement = conn.prepareStatement(sqlStatementStr);
			deleteStatement.setObject(1, objectID.getId());
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + deleteStatement);
			}
			final int returnValue = deleteStatement.executeUpdate();
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executed " + deleteStatement + " with return " + returnValue);
			}
			if (returnValue == 0) {
				throw new DbObjectNotExistException(objectID);
			}

		} catch (final SQLException e) {
			logger.debug("[==DB==] Exception in delete", e);
			throw new DbObjectNotExistException(objectID);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}

	}

	@Override
	public boolean isClosed() {
		return dataSource.isClosed();
	}

	@Override
	public boolean close() {
		boolean closed = true;
		try {
			// They can be null if not opened.
			if (dataSource != null) {
				dataSource.close();
				closed = closed && dataSource.isClosed();
			}
			if (rootDataSource != null) {
				rootDataSource.close();
				closed = closed && rootDataSource.isClosed();
			}
		} catch (final Exception e) {
			logger.debug("Ignored exception", e);
			e.printStackTrace();
		}
		return closed;
	}

	public BasicDataSource getDataSource() {
		return dataSource;
	}
}
