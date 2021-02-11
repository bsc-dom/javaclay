
package es.bsc.dataclay.logic.logicmetadata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.commons.dbcp2.BasicDataSource;

import es.bsc.dataclay.exceptions.dbhandler.DbObjectAlreadyExistException;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;

/**
 * Data base connection.
 */
public final class LogicMetadataDB {

	/** DataSource. */
	private final SQLiteDataSource dataSource;

	/**
	 * LogicMetadataDB constructor.
	 * 
	 * @param managerName
	 *            Name of the LM service managing.
	 */
	public LogicMetadataDB(final SQLiteDataSource dataSource) {
		this.dataSource = dataSource;
		createTables();
	}

	/**
	 * Create tables of LogicMetadata.
	 */
	public void createTables() {
		synchronized (dataSource) {

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement createStatement = conn.prepareStatement(LogicMetadataSQLStatements.SqlStatements.CREATE_TABLE_LOGICMODULE.getSqlStatement());
				createStatement.execute();
			} catch (final SQLException e) {
				// ignore if type already exists
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
	 * Delete the tables. Just the other way around of createTables --much simpler.
	 */
	public void dropTables() {
		synchronized (dataSource) {

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement dropStatement = conn.prepareStatement(LogicMetadataSQLStatements.SqlStatements.DROP_TABLE_LOGICMODULE.getSqlStatement());
				dropStatement.execute();
			} catch (final SQLException e) {
				// ignore if type does not exists
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
	 * Store into database
	 * @param logicmoduleIDs
	 *            logic module ids
	 */
	public void store(final LogicMetadataIDs logicmoduleIDs) {
		synchronized (dataSource) {

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(LogicMetadataSQLStatements.SqlStatements.INSERT_LOGICMODULE.getSqlStatement());
				insertStatement.setObject(1, logicmoduleIDs.dcID.getId());
				insertStatement.setObject(2, logicmoduleIDs.dcAdminID.getId());
				if (logicmoduleIDs.dcRegistratorID == null) {
					insertStatement.setObject(3, null);
				} else {
					insertStatement.setObject(3, logicmoduleIDs.dcRegistratorID.getId());
				}
				if (logicmoduleIDs.dcPublicNamespaceID == null) {
					insertStatement.setObject(4, null);
				} else {
					insertStatement.setObject(4, logicmoduleIDs.dcPublicNamespaceID.getId());
				}
				if (logicmoduleIDs.dcPublicContractID == null) {
					insertStatement.setObject(5, null);
				} else {
					insertStatement.setObject(5, logicmoduleIDs.dcPublicContractID.getId());
				}
				insertStatement.executeUpdate();

			} catch (final Exception e) {
				e.printStackTrace();
				throw new DbObjectAlreadyExistException(logicmoduleIDs.dcID);
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
	 * Deserialize LogicMetadataIDs
	 * @param rs
	 *            Result set
	 * @return LogicMetadataIDs
	 */
	private LogicMetadataIDs deserializeLogicMetadataIDs(
			final ResultSet rs) {
		LogicMetadataIDs ids = null;
		try {
			// CHECKSTYLE:OFF
			ids = new LogicMetadataIDs();
			ids.dcID = new DataClayInstanceID((UUID)rs.getObject("dataclayinstanceid"));
			ids.dcAdminID = new AccountID((UUID)rs.getObject("adminid"));
			UUID curUUID = (UUID)rs.getObject("registratorid");
			if (curUUID != null) {
				ids.dcRegistratorID = new AccountID(curUUID);
			}
			curUUID = (UUID)rs.getObject("publicnamespaceid");
			if (curUUID != null) {
				ids.dcPublicNamespaceID = new NamespaceID(curUUID);
			}
			curUUID = (UUID)rs.getObject("publiccontractid");
			if (curUUID != null) {
				ids.dcPublicContractID = new ContractID(curUUID);
			}

		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return ids;
	}

	/**
	 * Get LogicModule metadata by ID
	 * @return The LogicModule metadata
	 */
	public LogicMetadataIDs getLogicMetadata() {
		synchronized (dataSource) {

			ResultSet rs = null;
			LogicMetadataIDs ids = null;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(LogicMetadataSQLStatements.SqlStatements.SELECT_LOGICMODULE.getSqlStatement());
				rs = selectStatement.executeQuery();
				if (rs.next()) {
					ids = this.deserializeLogicMetadataIDs(rs);

				}
			} catch (final SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			}

			return ids;
		}
	}

	/**
	 * Check if there is a LogicModule
	 * @return TRUE if exists. FALSE otherwise
	 */
	public boolean existsMetaData() {
		synchronized (dataSource) {

			ResultSet rs = null;
			boolean exists = false;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement existsStatement = conn.prepareStatement(LogicMetadataSQLStatements.SqlStatements.EXISTS_LOGICMODULE_BY_ID.getSqlStatement());
				rs = existsStatement.executeQuery();
				rs.next();
				exists = rs.getBoolean(1);

			} catch (final Exception e) {
				e.printStackTrace();
			} finally {
				try {
					rs.close();
					if (conn != null) {
						conn.close();
					}
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			}

			return exists;
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
