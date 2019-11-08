
package es.bsc.dataclay.dbhandler.sql.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.dbhandler.sql.SQLHandler;

/**
 * This class handles the storage of objects in a Postgres database.
 * 
 */
public class PostgresHandler extends SQLHandler<PostgresHandlerConf> {

	/** Logger. */
	private static final Logger logger = LogManager.getLogger("PostgresHandler");

	/** Create database. */
	private static final String CREATE_DATABASE_STATEMENT = "CREATE DATABASE ";
	/** Check database exists. */
	private static final String CHECK_DATABASE_EXISTS = "SELECT 1 from pg_database WHERE datname=";
	/** Revoke future connections to DB prefix. */
	private static final String REVOKE_FUTURE_CONNS_TO_DB_INIT = "REVOKE CONNECT ON DATABASE";
	/** Revoke future connections to DB end. */
	private static final String REVOKE_FUTURE_CONNS_TO_DB_END = "FROM public";
	/** REMOVE all current connections to database. */
	private static final String REMOVE_CONNS_TO_DB = "SELECT pg_terminate_backend(pg_stat_activity.pid) "
			+ "FROM pg_stat_activity WHERE pg_stat_activity.datname = ";

	/** Get databases names. */
	private static final String GET_DB_NAMES = "SELECT datname from pg_database";
	/** Drop database. */
	private static final String DROP_DATABASE = "DROP DATABASE IF EXISTS ";

	/**
	 * Constructor of the class
	 * 
	 * @param postgresConfig
	 *            Postgres configuration
	 */
	public PostgresHandler(final PostgresHandlerConf postgresConfig) {
		super(postgresConfig);
	}

	@Override
	protected void initDataSource() {
		if (dataSource == null) {
			final String url = "jdbc:postgresql://" + configuration.getHost() + ":" + configuration.getPort() + "/"
					+ configuration.getDbname();
			dataSource = new DataSourceMultithread();
			setConfiguration(dataSource, url, configuration.getUser(), configuration.getPassword());
		}
	}

	@Override
	protected void initRootDataSource() {
		if (rootDataSource == null) {
			final String url = "jdbc:postgresql://" + configuration.getHost() + ":" + configuration.getPort() + "/";
			rootDataSource = new BasicDataSource();
			setConfiguration(rootDataSource, url, configuration.getUser(), configuration.getPassword());
		}
	}

	@Override
	public boolean databaseExists() {
		final Connection conn = getRootConnection();
		try {
			final PreparedStatement stmt = conn
					.prepareStatement(CHECK_DATABASE_EXISTS + "'" + configuration.getDbname() + "'");
			ResultSet resultSet = null;
			logger.debug("Executing {}", stmt);
			resultSet = stmt.executeQuery();
			return resultSet.next();
		} catch (final SQLException e) {
			logger.debug("Ignored exception", e);
		} finally {
			try {
				conn.close();
			} catch (final SQLException e) {
				logger.debug("Ignored exception", e);
			}
		}
		return false;
	}

	@Override
	public void createDatabase() {
		final Connection conn = getRootConnection();
		try {
			// Create database
			final PreparedStatement stmt = conn
					.prepareStatement(CREATE_DATABASE_STATEMENT + "\"" + configuration.getDbname() + "\"");
			logger.debug("Executing {}", stmt);
			stmt.execute();
		} catch (final SQLException e) {
			logger.error("Ignored SQLException", e);
		} finally {
			try {
				conn.close();
			} catch (final SQLException e) {
				logger.error("Ignored SQLException during close", e);
			}
		}
	}

	/**
	 * Drop all databases. FOR TESTING PURPOSES.
	 */
	@Override
	public void dropAllDatabases() {
		final List<String> allDbs = new ArrayList<>();
		final Connection conn = getRootConnection();
		try {

			final PreparedStatement stmt = conn.prepareStatement(GET_DB_NAMES);
			ResultSet resultSet = null;
			logger.debug("Executing {}", stmt);
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				final String datname = resultSet.getString("datname");
				allDbs.add(datname);
			}
		} catch (final SQLException e) {
			logger.debug("Ignored exception", e);
		} finally {
			try {
				conn.close();
			} catch (final SQLException e) {
				logger.debug("Ignored exception", e);
			}
		}
		for (final String db : allDbs) {
			if (db.startsWith("template") || db.equals("postgres")) {
				continue;
			}
			logger.debug("Dropping {}", db);
			this.dropDatabase(db);
		}
	}

	private boolean dropDatabase(final String dbName) {
		final Connection conn = getRootConnection();
		try {

			PreparedStatement stmt = conn.prepareStatement(
					REVOKE_FUTURE_CONNS_TO_DB_INIT + " \"" + dbName + "\" " + REVOKE_FUTURE_CONNS_TO_DB_END);
			if (DEBUG_ENABLED) {
				logger.debug("Executing " + stmt);
			}
			stmt.execute();

			stmt = conn.prepareStatement(REMOVE_CONNS_TO_DB + "'" + dbName + "'");
			if (DEBUG_ENABLED) {
				logger.debug("Executing " + stmt);
			}
			stmt.execute();

			stmt = conn.prepareStatement(CHECK_DATABASE_EXISTS + "'" + dbName + "'");
			ResultSet resultSet = null;
			if (DEBUG_ENABLED) {
				logger.debug("Executing " + stmt);
			}
			resultSet = stmt.executeQuery();
			final boolean hasResults = resultSet.next();
			if (hasResults) {
				// Drop database
				stmt = conn.prepareStatement(DROP_DATABASE + "\"" + dbName + "\"");
				if (DEBUG_ENABLED) {
					logger.debug("Executing " + stmt);
				}
				stmt.execute();
			} else {
				return false;
			}
		} catch (final SQLException e) {
			logger.debug("Ignored exception in dropDatabase", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				logger.debug("SQL Exception while closing connection", ex1);
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "PostgresHandler(" + configuration.getHost() + ":" + configuration.getPort() + ","
				+ configuration.getDbname() + ")";
	}

	private void setConfiguration(final BasicDataSource ds, final String url, final String user,
			final String password) {
		ds.setUrl(url);
		ds.setUsername(user);
		ds.setPassword(password);
		// ds.setValidationQueryTimeout(5);
		ds.setDriverClassName("es.bsc.dataclay.dbhandler.sql.postgres.ExtendedPostgresDriver");

		ds.setMaxTotal(-1);
		ds.setMaxIdle(-1);
		// ds.setMaxWaitMillis(30000L);
		ds.setPoolPreparedStatements(true);
		ds.setMaxOpenPreparedStatements(-1);
		// ds.setMinEvictableIdleTimeMillis(1000L);
		// ds.setDefaultAutoCommit(true);
		// ds.setTestOnBorrow(true);
		// ds.setValidationQuery("SELECT 1");
		/*
		 * ds.setTestOnBorrow(true); ds.setValidationQuery("SELECT 1");
		 * ds.setValidationQueryTimeout(10); ds.setMinEvictableIdleTimeMillis(500);
		 * ds.setTimeBetweenEvictionRunsMillis(60000); ds.setMinIdle(0);
		 * ds.setMaxTotal(100); ds.setMaxIdle(100); ds.setPoolPreparedStatements(true);
		 * ds.setMaxOpenPreparedStatements(100); ds.setInitialSize(16);
		 * ds.setDefaultAutoCommit(true);
		 */
		ds.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
	}
}
