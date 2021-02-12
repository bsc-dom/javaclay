
package es.bsc.dataclay.logic.datacontractmgr;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
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
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.DataContractID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.management.datacontractmgr.DataContract;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;

/**
 * Data base connection.
 */
public final class DataContractManagerDB {

	/** Logger. */
	private Logger logger;

	/** Indicates if debug is enabled. */
	private static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** BasicDataSource. */
	private final SQLiteDataSource dataSource;

	/**
	 * MetaDataServiceDB constructor.
	 * 
	 * @param dataSource
	 *            Name of the LM service managing.
	 */
	public DataContractManagerDB(final SQLiteDataSource dataSource) {
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
				for (final DataContractManagerSQLStatements.SqlStatements stmt : DataContractManagerSQLStatements.SqlStatements.values()) {
					if (stmt.name().startsWith("CREATE_TABLE")) {
						final PreparedStatement updateStatement = conn.prepareStatement(stmt.getSqlStatement());
						updateStatement.execute();
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
				for (final DataContractManagerSQLStatements.SqlStatements stmt : DataContractManagerSQLStatements.SqlStatements.values()) {
					if (stmt.name().startsWith("DROP_TABLE")) {
						final PreparedStatement updateStatement = conn.prepareStatement(stmt.getSqlStatement());
						updateStatement.execute();
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
	 * Store datacontract into database
	 * @param datacontract
	 *            datacontract
	 */
	public void store(final DataContract datacontract) {
		synchronized (dataSource) {
			final UUID[] accountsUUIDs = new UUID[datacontract.getApplicantsAccountsIDs().size()];
			int i = 0;
			for (final AccountID accountID : datacontract.getApplicantsAccountsIDs()) {
				accountsUUIDs[i] = accountID.getId();
				i++;
			}

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(DataContractManagerSQLStatements.SqlStatements.INSERT_DATACONTRACT.getSqlStatement());
				insertStatement.setObject(1, datacontract.getDataClayID().getId());
				// CHECKSTYLE:OFF
				insertStatement.setTimestamp(2, new java.sql.Timestamp(datacontract.getBeginDate().getTimeInMillis()));
				insertStatement.setTimestamp(3, new java.sql.Timestamp(datacontract.getEndDate().getTimeInMillis()));
				insertStatement.setBoolean(4, datacontract.isPublicAvailable());
				insertStatement.setObject(5, datacontract.getProviderAccountID().getId());
				insertStatement.setObject(6, datacontract.getProviderDataSetID().getId());

				final Array tArray = insertStatement.getConnection().createArrayOf("uuid", accountsUUIDs);
				insertStatement.setArray(7, tArray);

				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();

			} catch (final Exception e) {
				e.printStackTrace();
				throw new DbObjectAlreadyExistException(datacontract.getDataClayID());
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
	 * @return DataContract
	 */
	private DataContract deserializeDataContract(
			final ResultSet rs) {
		DataContract result = null;
		try {
			// CHECKSTYLE:OFF
			final DataContractID contractID = new DataContractID((UUID)rs.getObject("id"));
			final Calendar beginDate = Calendar.getInstance();
			beginDate.setTimeInMillis(rs.getTimestamp("beginDate").getTime());
			final Calendar endDate = Calendar.getInstance();
			endDate.setTimeInMillis(rs.getTimestamp("endDate").getTime());
			final boolean publicAvailable = rs.getBoolean("publicAvailable");
			final AccountID providerAccountID = new AccountID((UUID)rs.getObject("providerAccountID"));
			final DataSetID providerDataSetID = new DataSetID((UUID)rs.getObject("providerDataSetID"));

			final UUID[] applicantsAccountsUUIDs = (UUID[]) rs.getArray("applicantsAccountsIDs").getArray();
			final Set<AccountID> applicantsAccountsIDs = new HashSet<>();
			for (final UUID uuid : applicantsAccountsUUIDs) {
				applicantsAccountsIDs.add(new AccountID(uuid));
			}

			result = new DataContract();
			result.setApplicantsAccountsIDs(applicantsAccountsIDs);
			result.setBeginDate(beginDate);
			result.setEndDate(endDate);
			result.setDataClayID(contractID);
			result.setProviderAccountID(providerAccountID);
			result.setProviderDataSetID(providerDataSetID);
			result.setPublicAvailable(publicAvailable);

		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Get by ID
	 * @param datacontractID
	 *            ID of object
	 * @return The object
	 * @throws SQLException
	 *             if not found
	 */
	public DataContract getDataContractByID(final DataContractID datacontractID) {
		synchronized (dataSource) {
			ResultSet rs = null;
			DataContract result = null;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(DataContractManagerSQLStatements.SqlStatements.SELECT_DATACONTRACT.getSqlStatement());

				selectStatement.setObject(1, datacontractID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				if (rs.next()) {
					result = deserializeDataContract(rs);

				}
			} catch (final SQLException e) {
				throw new DbObjectNotExistException(datacontractID);
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

			return result;
		}
	}

	/**
	 * Delete by ID
	 * @param datacontractID
	 *            ID of object
	 */
	public void deleteDataContractByID(final ContractID datacontractID) {
		synchronized (dataSource) {
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(DataContractManagerSQLStatements.SqlStatements.DELETE_DATACONTRACT.getSqlStatement());
				selectStatement.setObject(1, datacontractID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				selectStatement.executeUpdate();

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
	 * Update by ID
	 * @param datacontractID
	 *            ID of object
	 */
	public void updateDataContractsAddApplicant(final DataContractID datacontractID, final AccountID applicantAccountID) {
		synchronized (dataSource) {
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement stmt = conn.prepareStatement(DataContractManagerSQLStatements.SqlStatements.UPDATE_DATACONTRACT_ADD_APPLICANT.getSqlStatement());
				stmt.setObject(1, applicantAccountID.getId());
				stmt.setObject(2, datacontractID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + stmt);
				}
				stmt.executeUpdate();

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
	 * Get contracts of dataset
	 * @param dataSetID
	 *            DataSet id
	 * @return The DataContracts
	 */
	public List<DataContract> getDataContractsOfDataSet(final DataSetID dataSetID) {
		synchronized (dataSource) {
			ResultSet rs = null;
			final List<DataContract> result = new ArrayList<>();
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(DataContractManagerSQLStatements.SqlStatements.SELECT_ALL_DATACONTRACTS_OF_DATASET.getSqlStatement());
				selectStatement.setObject(1, dataSetID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				while (rs.next()) {
					result.add(deserializeDataContract(rs));
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

			return result;
		}
	}

	/**
	 * Get data contracts containing applicant
	 * @param applicantAccountID
	 *            Account ID
	 * @return The Contracts
	 */
	public List<DataContract> getDataContractsWithApplicant(final AccountID applicantAccountID) {
		synchronized (dataSource) {
			ResultSet rs = null;
			final List<DataContract> result = new ArrayList<>();
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn
						.prepareStatement(DataContractManagerSQLStatements.SqlStatements.SELECT_ALL_DATACONTRACTS_WITH_APPLICANT.getSqlStatement());
				final Array tArray = selectStatement.getConnection().createArrayOf("uuid", new UUID[]{applicantAccountID.getId()});
				selectStatement.setArray(1, tArray);
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				while (rs.next()) {
					result.add(deserializeDataContract(rs));
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

			return result;
		}
	}

	/**
	 * Get datacontracts containing provider
	 * @param providerAccountID
	 *            Account ID
	 * @return The Contracts
	 */
	public List<DataContract> getDataContractsWithProvider(final AccountID providerAccountID) {
		synchronized (dataSource) {
			ResultSet rs = null;
			final List<DataContract> result = new ArrayList<>();
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn
						.prepareStatement(DataContractManagerSQLStatements.SqlStatements.SELECT_ALL_DATACONTRACTS_WITH_PROVIDER.getSqlStatement());
				selectStatement.setObject(1, providerAccountID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				while (rs.next()) {
					result.add(deserializeDataContract(rs));
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

			return result;
		}
	}

	/**
	 * Get datacontracts containing applicant and data set
	 * @param applicantAccountID
	 *            Account ID
	 * @param dataSetID
	 *            ID of dataset
	 * @return The Contracts
	 */
	public List<DataContract> getContractsWithApplicantAndDataSet(final AccountID applicantAccountID,
			final DataSetID dataSetID) {
		synchronized (dataSource) {
			ResultSet rs = null;
			final List<DataContract> result = new ArrayList<>();
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn
						.prepareStatement(DataContractManagerSQLStatements.SqlStatements.SELECT_ALL_DATACONTRACTS_WITH_APPLICANT_AND_DATASET.getSqlStatement());

				selectStatement.setObject(1, applicantAccountID.getId());
				selectStatement.setObject(2, dataSetID.getId());

				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				while (rs.next()) {
					result.add(deserializeDataContract(rs));
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
