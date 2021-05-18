
package es.bsc.dataclay.logic.datasetmgr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.exceptions.dbhandler.DbObjectAlreadyExistException;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectNotExistException;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.management.datasetmgr.DataSet;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;
/**
 * Data base connection.
 */
public final class DataSetManagerDB {

	/** Logger. */
	private Logger logger;

	/** Indicates if debug is enabled. */
	private static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** SingleConnection. */
	private final SQLiteDataSource dataSource;

	/**
	 * MetaDataServiceDB constructor.
	 * 
	 * @param dataSource
	 *            Name of the LM service managing.
	 */
	public DataSetManagerDB(final SQLiteDataSource dataSource) {
		this.dataSource = dataSource;
		if (DEBUG_ENABLED) {
			logger = LogManager.getLogger("LMDB");
		}
	}

	/**
	 * Create tables of MDS.
	 */
	public void createTables() {
		synchronized (dataSource) {

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				for (final DataSetManagerSQLStatements.SqlStatements stmt : DataSetManagerSQLStatements.SqlStatements.values()) {
					if (stmt.name().startsWith("CREATE_TABLE")) {
						final PreparedStatement updateStatement = conn.prepareStatement(stmt.getSqlStatement());
						updateStatement.execute();
						updateStatement.close();
					}
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					ex1.printStackTrace();
				}
			}
		}
	}

	/**
	 * Delete the tables of MDS. Just the other way around of createTables --much simpler.
	 */
	public void dropTables() {
		synchronized (dataSource) {

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				for (final DataSetManagerSQLStatements.SqlStatements stmt : DataSetManagerSQLStatements.SqlStatements.values()) {
					if (stmt.name().startsWith("DROP_TABLE")) {
						final PreparedStatement updateStatement = conn.prepareStatement(stmt.getSqlStatement());
						updateStatement.execute();
						updateStatement.close();
					}
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					ex1.printStackTrace();
				}
			}
		}
	}

	/**
	 * Store dataSet into database
	 * @param dataSet
	 *            dataSet
	 */
	public void store(final DataSet dataSet) {
		synchronized (dataSource) {

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(
						DataSetManagerSQLStatements.SqlStatements.INSERT_DATASET.getSqlStatement());
				insertStatement.setObject(1, dataSet.getDataClayID().getId());
				// CHECKSTYLE:OFF
				insertStatement.setString(2, dataSet.getName());
				insertStatement.setObject(3, dataSet.getProviderAccountID().getId());
				insertStatement.setBoolean(4, dataSet.getIsPublic());

				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
			} catch (final Exception e) {
				e.printStackTrace();
				throw new DbObjectAlreadyExistException(dataSet.getDataClayID());
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException ex1) {
					ex1.printStackTrace();
				}
			}
		}

	}

	/**
	 * Deserialize
	 * @param rs
	 *            Result set
	 * @return DataSet
	 */
	private DataSet deserializeDataSet(
			final ResultSet rs) {
		DataSet result = null;
		try {
			// CHECKSTYLE:OFF
			final DataSetID dsID = new DataSetID((UUID)rs.getObject("id"));
			final String name = rs.getString("name");
			final AccountID providerAccountID = new AccountID((UUID)rs.getObject("providerAccountID"));
			final boolean isPublic = rs.getBoolean("isPublic");

			result = new DataSet();
			result.setDataClayID(dsID);
			result.setName(name);
			result.setProviderAccountID(providerAccountID);
			result.setIsPublic(isPublic);

		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Get by ID
	 * @param datasetID
	 *            ID of object
	 * @return The object
	 * @throws SQLException
	 *             if not found
	 */
	public DataSet getDataSetByID(final DataSetID datasetID) {
		synchronized (dataSource) {

			ResultSet rs = null;
			DataSet result = null;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(
						DataSetManagerSQLStatements.SqlStatements.SELECT_DATASET.getSqlStatement());

				selectStatement.setObject(1, datasetID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				if (rs.next()) {
					result = deserializeDataSet(rs);
				}
				selectStatement.close();

			} catch (final SQLException e) {
				throw new DbObjectNotExistException(datasetID);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			}

			return result;
		}
	}

	/**
	 * Delete by ID
	 * @param dataSetID
	 *            ID of object
	 */
	public void deleteDataSetByID(final DataSetID dataSetID) {
		synchronized (dataSource) {

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(
						DataSetManagerSQLStatements.SqlStatements.DELETE_DATASET.getSqlStatement());
				selectStatement.setObject(1, dataSetID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				selectStatement.executeUpdate();
				selectStatement.close();

			} catch (final SQLException e) {
				// not found, ignore
				// e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Get datasets containing provider
	 * @param providerAccountID
	 *            Account ID
	 * @return The DataSets
	 */
	public List<DataSet> getDataSetsWithProvider(final AccountID providerAccountID) {
		synchronized (dataSource) {

			ResultSet rs = null;
			final List<DataSet> result = new ArrayList<>();
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(
						DataSetManagerSQLStatements.SqlStatements.SELECT_ACCOUNT_DATASETS.getSqlStatement());
				selectStatement.setObject(1, providerAccountID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				while (rs.next()) {
					result.add(deserializeDataSet(rs));
				}
				selectStatement.close();

			} catch (final SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			}

			return result;
		}
	}

	/**
	 * Get by name
	 * @param name
	 *            the name
	 * @return The object
	 * @throws SQLException
	 *             if not found
	 */
	public DataSet getDataSetByName(final String name) {
		synchronized (dataSource) {

			ResultSet rs = null;
			DataSet result = null;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(
						DataSetManagerSQLStatements.SqlStatements.SELECT_DATASET_BY_NAME.getSqlStatement());

				selectStatement.setString(1, name);
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				if (rs.next()) {
					result = deserializeDataSet(rs);

				}
				selectStatement.close();

			} catch (final SQLException e) {
				// ignore
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			}

			return result;
		}
	}

	/**
	 * Get public datasets
	 * @return Set of datasets
	 */
	public Set<DataSet> getPublicDataSets() {
		synchronized (dataSource) {

			ResultSet rs = null;
			final Set<DataSet> result = new HashSet<>();
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(
						DataSetManagerSQLStatements.SqlStatements.SELECT_PUBLIC_DATASETS.getSqlStatement());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				while (rs.next()) {
					result.add(deserializeDataSet(rs));
				}
				selectStatement.close();

			} catch (final SQLException e) {
				// ignore
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			}

			return result;
		}
	}

	/**
	 * Close DB.
	 */
	public void close() {
		try {
			dataSource.close();
		}catch(Exception e) {
			// TODO
			e.printStackTrace();
		}
	}
}
