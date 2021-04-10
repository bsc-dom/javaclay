
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
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;

public abstract class SQLHandler<T extends DBHandlerConf> implements DBHandler {

	/** Logger. */
	private static final Logger logger = LogManager.getLogger("SQLHandler");

	/** Indicates if debug is enabled. */
	protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** Data sources. */
	protected SQLiteDataSource dataSource;

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
		initDataSource();
		if (!databaseExists()) {
			createDatabase();
		}
		createTables();
	}

	protected abstract void initDataSource();

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
		synchronized (dataSource) {
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
	}

	public T getConfiguration() {
		return configuration;
	}

	// ======================= STORE ======================= //

	@Override
	public void store(final ObjectID objectID, final byte[] bytes) {
		synchronized (dataSource) {
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
	}

	@Override
	public byte[] get(final ObjectID objectID) {
		synchronized (dataSource) {
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
	}

	@Override
	public boolean exists(final ObjectID objectID) {
		synchronized (dataSource) {
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

	}

	@Override
	public int count() {
		synchronized (dataSource) {
			int result = -1;

			final String sqlStatementStr = DataServiceDBSQLStatements.SqlStatements.COUNT_OBJECTS.getSqlStatement();
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(sqlStatementStr);
				ResultSet resultSet = null;
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				resultSet = selectStatement.executeQuery();
				final boolean hasResults = resultSet.next();
				if (hasResults) {
					result = resultSet.getInt(1);
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
	}

	@Override
	public void update(final ObjectID objectID, final byte[] newbytes) {
		synchronized (dataSource) {
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
		}
	}

	@Override
	public void delete(final ObjectID objectID) {
		synchronized (dataSource) {
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

	}



	@Override
	public void vacuum() {
		synchronized (dataSource) {
			final String sqlStatementStr = DataServiceDBSQLStatements.SqlStatements.VACUUM.getSqlStatement();
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement vacuumStatement = conn.prepareStatement(sqlStatementStr);
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + vacuumStatement);
				}
				vacuumStatement.executeUpdate();

			} catch (final SQLException e) {
				logger.debug("[==DB==] Exception in vacuum", e);
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
		} catch (final Exception e) {
			logger.debug("Ignored exception", e);
			e.printStackTrace();
		}
		return closed;
	}

	public SQLiteDataSource getDataSource() {
		return dataSource;
	}
}
