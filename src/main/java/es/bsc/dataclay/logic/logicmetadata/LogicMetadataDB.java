
package es.bsc.dataclay.logic.logicmetadata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages;
import es.bsc.dataclay.exceptions.dbhandler.DbHandlerException;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectNotExistException;
import es.bsc.dataclay.logic.server.LogicModuleSrv;
import es.bsc.dataclay.util.ids.*;
import es.bsc.dataclay.util.management.metadataservice.DataClayInstance;
import es.bsc.dataclay.util.management.metadataservice.ExecutionEnvironment;
import es.bsc.dataclay.util.management.metadataservice.StorageLocation;
import org.apache.commons.dbcp2.BasicDataSource;

import es.bsc.dataclay.exceptions.dbhandler.DbObjectAlreadyExistException;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sqlite.SQLiteErrorCode;

/**
 * Data base connection.
 */
public final class LogicMetadataDB {

	private static final Logger logger = LogManager.getLogger("LogicMetadata.db");


	/** DataSource. */
	private final SQLiteDataSource dataSource;

	/**
	 * LogicMetadataDB constructor.
	 * 
	 * @param dataSource db
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
				conn = getConnection();
				for (final LogicMetadataSQLStatements.SqlStatements stmt : LogicMetadataSQLStatements.SqlStatements.values()) {
					if (stmt.name().startsWith("CREATE_TABLE")) {
						try (PreparedStatement ps = conn.prepareStatement(stmt.getSqlStatement())) {
							ps.execute();
						}
					}
				}
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Delete the tables. Just the other way around of createTables --much simpler.
	 */
	public void dropTables() {
		synchronized (dataSource) {

			final Connection conn = getConnection();
			try {
				for (final LogicMetadataSQLStatements.SqlStatements stmt : LogicMetadataSQLStatements.SqlStatements.values()) {
					if (stmt.name().startsWith("DROP_TABLE")) {
						try (PreparedStatement ps = conn.prepareStatement(stmt.getSqlStatement())) {
							ps.execute();
						}
					}
				}
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
			} finally {
				closeConnection(conn);
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
				closeConnection(conn);
			}
		}

	}


	/**
	 * Store a Storage Location into database
	 *
	 * @param stLoc
	 *            Storage Location
	 */
	public void store(final StorageLocation stLoc) {
		synchronized (dataSource) {

			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(LogicMetadataSQLStatements.SqlStatements.INSERT_STORAGE_LOCATION.getSqlStatement())) {

				ps.setObject(1, stLoc.getDataClayID().getId());
				ps.setString(2, stLoc.getHostname());
				ps.setString(3, stLoc.getName());
				ps.setInt(4, stLoc.getStorageTCPPort());

				ps.executeUpdate();
			} catch (final SQLException e) {
				/* A primary key constraint is violated */
				if (e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code) {
					throw new DbObjectAlreadyExistException(stLoc.getDataClayID());
				} else {
					throw new DbHandlerException(e);
				}
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Store a ExecutionEnvironment into database
	 *
	 * @param exeEnv
	 *            Execution Environment
	 */
	public void store(final ExecutionEnvironment exeEnv) {
		synchronized (dataSource) {

			final Connection conn = getConnection();
			try (PreparedStatement ps = conn
					.prepareStatement(LogicMetadataSQLStatements.SqlStatements.INSERT_EXECUTION_ENVIRONMENT.getSqlStatement())) {

				ps.setObject(1, exeEnv.getDataClayID().getId());
				ps.setString(2, exeEnv.getHostname());
				ps.setString(3, exeEnv.getName());
				ps.setInt(4, exeEnv.getLang().getNumber());
				ps.setInt(5, exeEnv.getPort());
				ps.setObject(6, exeEnv.getDataClayInstanceID().getId());

				ps.executeUpdate();
			} catch (final SQLException e) {
				/* A primary key constraint is violated */
				if (e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code) {
					throw new DbObjectAlreadyExistException(exeEnv.getDataClayID());
				} else {
					throw new DbHandlerException(e);
				}
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Get StorageLocation by ID
	 *
	 * @param storageLocationID
	 *            ID of the object
	 * @return The StorageLocation or null if it does not exist
	 */
	public StorageLocation getByID(final StorageLocationID storageLocationID) {
		synchronized (dataSource) {

			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(LogicMetadataSQLStatements.SqlStatements.SELECT_STORAGE_LOCATION.getSqlStatement())) {
				ps.setObject(1, storageLocationID.getId());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return deserializeStorageLocation(rs);
					} else {
						return null;
					}
				}
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Get ExecutionEnvironment by ID
	 *
	 * @param executionEnvironmentID
	 *            ID of the object
	 * @return The ExecutionEnvironment or null if it does not exist
	 */
	public ExecutionEnvironment getByID(final ExecutionEnvironmentID executionEnvironmentID) {
		synchronized (dataSource) {

			final Connection conn = getConnection();
			try (PreparedStatement ps = conn
					.prepareStatement(LogicMetadataSQLStatements.SqlStatements.SELECT_EXECUTION_ENVIRONMENT.getSqlStatement())) {

				ps.setObject(1, executionEnvironmentID.getId());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return deserializeExecutionEnvironment(rs);
					} else {
						return null;
					}
				}
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}


	/**
	 * Check if there is a backend identified by ID provided
	 *
	 * @param stLocID
	 *            ID of the storage location
	 * @return TRUE if exists. FALSE otherwise
	 */
	public boolean existsByID(final StorageLocationID stLocID) {
		synchronized (dataSource) {

			final Connection conn = getConnection();
			try (PreparedStatement existsStatement = conn
					.prepareStatement(LogicMetadataSQLStatements.SqlStatements.EXISTS_STORAGE_LOCATION_BY_ID.getSqlStatement())) {

				existsStatement.setObject(1, stLocID.getId());
				try (ResultSet rs = existsStatement.executeQuery()) {
					rs.next();
					return rs.getBoolean(1);
				}
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Check if there is a backend identified by ID provided
	 *
	 * @param execEnvID
	 *            ID of the execution environment
	 * @return TRUE if exists. FALSE otherwise
	 */
	public boolean existsByID(final ExecutionEnvironmentID execEnvID) {
		synchronized (dataSource) {

			final Connection conn = getConnection();
			try (PreparedStatement existsStatement = conn
					.prepareStatement(LogicMetadataSQLStatements.SqlStatements.EXISTS_EXECUTION_ENVIRONMENT_BY_ID.getSqlStatement())) {

				existsStatement.setObject(1, execEnvID.getId());
				try (ResultSet rs = existsStatement.executeQuery()) {
					rs.next();
					return rs.getBoolean(1);
				}
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}


	/**
	 * Delete storage location identified by ID provided (ignored if it does not exist)
	 *
	 * @param stLocID
	 *            ID of the storage location
	 */
	public void deleteByID(final StorageLocationID stLocID) {
		synchronized (dataSource) {

			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(LogicMetadataSQLStatements.SqlStatements.DELETE_STORAGE_LOCATION.getSqlStatement())) {

				ps.setObject(1, stLocID.getId());
				ps.executeUpdate();
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Delete execute environment identified by ID provided (ignored if it does not exist)
	 *
	 * @param execEnvID
	 *            ID of the backend
	 */
	public void deleteByID(final ExecutionEnvironmentID execEnvID) {
		synchronized (dataSource) {

			final Connection conn = getConnection();
			try (PreparedStatement ps = conn
					.prepareStatement(LogicMetadataSQLStatements.SqlStatements.DELETE_EXECUTION_ENVIRONMENT.getSqlStatement())) {

				ps.setObject(1, execEnvID.getId());
				ps.executeUpdate();
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}


	/**
	 * Update storage location host and port by ID
	 *
	 * @param id
	 *            ID of storage location
	 * @param newhost new host 
	 * @param newport new port
	 * @throws DbObjectNotExistException
	 *             if object does not exist
	 */
	public void updateStorageLocationByID(final StorageLocationID id, final String newhost,
										  final Integer newport) throws DbObjectNotExistException {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn
					.prepareStatement(LogicMetadataSQLStatements.SqlStatements.UPDATE_STORAGE_LOCATION.getSqlStatement())) {

				ps.setString(1, newhost);
				ps.setInt(2, newport);
				ps.setObject(3, id.getId());
				final int count = ps.executeUpdate();
				if (count == 0) {
					throw new DbObjectNotExistException(id);
				}
			} catch (final DbObjectNotExistException dbe) {
				throw dbe;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Update execution environment host and port by ID
	 *
	 * @param id
	 *            ID of storage location
	 * @param newhost new host 
	 * @param newport new port
	 * @throws DbObjectNotExistException
	 *             if object does not exist
	 */
	public void updateExecutionEnvironmentByID(final ExecutionEnvironmentID id, final String newhost,
											   final Integer newport) throws DbObjectNotExistException {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn
					.prepareStatement(LogicMetadataSQLStatements.SqlStatements.UPDATE_EXECUTION_ENVIRONMENT.getSqlStatement())) {

				ps.setString(1, newhost);
				ps.setInt(2, newport);
				ps.setObject(3, id.getId());
				final int count = ps.executeUpdate();
				if (count == 0) {
					throw new DbObjectNotExistException(id);
				}
			} catch (final DbObjectNotExistException dbe) {
				throw dbe;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}


	/**
	 * Get all Storage Locations
	 *
	 * @return The Storage Locations
	 */
	public List<StorageLocation> getAllStorageLocations() {
		synchronized (dataSource) {
			final ArrayList<StorageLocation> resultList = new ArrayList<>();
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(LogicMetadataSQLStatements.SqlStatements.SELECT_ALL_LOCS.getSqlStatement())) {

				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						final StorageLocation stLoc = deserializeStorageLocation(rs);
						resultList.add(stLoc);
					}
				}
				return resultList;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Get all Execution Environments
	 *
	 * @return The Execution Environments
	 */
	public List<ExecutionEnvironment> getAllExecutionEnvironments() {
		synchronized (dataSource) {
			final ArrayList<ExecutionEnvironment> resultList = new ArrayList<>();
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(LogicMetadataSQLStatements.SqlStatements.SELECT_ALL_EXECENVS.getSqlStatement())) {

				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						final ExecutionEnvironment execEnv = deserializeExecutionEnvironment(rs);
						resultList.add(execEnv);
					}
				}
				return resultList;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Get all Execution Environments of a specific language
	 *
	 * @return a list of Execution Environments
	 */
	public List<ExecutionEnvironment> getAllExecutionEnvironmentsByLang(final CommonMessages.Langs lang) {
		synchronized (dataSource) {
			final ArrayList<ExecutionEnvironment> resultList = new ArrayList<>();
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn
					.prepareStatement(LogicMetadataSQLStatements.SqlStatements.SELECT_ALL_EXECENVS_BY_LANG.getSqlStatement())) {
				ps.setInt(1, lang.getNumber());

				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						final ExecutionEnvironment execEnv = deserializeExecutionEnvironment(rs);
						resultList.add(execEnv);
					}
				}
				return resultList;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Get all DataClayInstance ids representing external dataclays
	 *
	 * @return a set of DataClayInstances ids
	 */
	public Set<DataClayInstanceID> getAllExternalDataClays() {
		synchronized (dataSource) {
			final Set<DataClayInstanceID> resultList = new HashSet<>();
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn
					.prepareStatement(LogicMetadataSQLStatements.SqlStatements.SELECT_ALL_DATACLAYS.getSqlStatement())) {
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						final DataClayInstanceID instanceID = new DataClayInstanceID(
								(UUID) rs.getObject("id"));
						resultList.add(instanceID);
					}
				}
				return resultList;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}


	/**
	 * Get a single (should be unique) Storage Location by name
	 *
	 * @param name
	 *            Name
	 * @return the storage location named with specified name (null if there is none).
	 */
	public StorageLocation getStorageLocationByName(final String name) {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(LogicMetadataSQLStatements.SqlStatements.SELECT_STLOC_BY_NAME.getSqlStatement())) {

				ps.setString(1, name);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return deserializeStorageLocation(rs);
					} else {
						return null;
					}
				}
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Get all Execution Environment by host name and language
	 *
	 * @param hostname
	 *            host name
	 * @param language
	 *            Language
	 * @return all Execution Environment by host name and language
	 */
	public Set<ExecutionEnvironmentID> getExecutionEnvironmentByHostnameAndLanguage(final String hostname,
																					final CommonMessages.Langs language) {
		synchronized (dataSource) {
			final Set<ExecutionEnvironmentID> resultList = new HashSet<>();
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn
					.prepareStatement(LogicMetadataSQLStatements.SqlStatements.SELECT_ALL_EXECENV_BY_HOSTNAME_AND_LANG.getSqlStatement())) {
				ps.setString(1, hostname);
				ps.setInt(2, language.getNumber());
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						final ExecutionEnvironmentID executionEnvironmentID = new ExecutionEnvironmentID(
								(UUID) rs.getObject("id"));
						resultList.add(executionEnvironmentID);
					}
				}
				return resultList;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Get all Execution Environments associated to DS name and language
	 *
	 * @param name
	 *            Name
	 * @param lang
	 *            Language
	 * @return All execution environment associated to specified name
	 */
	public Set<ExecutionEnvironmentID> getExecutionEnvironmentsByNameAndLang(final String name, final CommonMessages.Langs lang) {
		synchronized (dataSource) {
			final Set<ExecutionEnvironmentID> resultList = new HashSet<>();
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn
					.prepareStatement(LogicMetadataSQLStatements.SqlStatements.SELECT_EXECENV_BY_NAME_LANG.getSqlStatement())) {

				ps.setString(1, name);
				ps.setInt(2, lang.getNumber());
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						final ExecutionEnvironment execEnv = deserializeExecutionEnvironment(rs);
						resultList.add(execEnv.getDataClayID());
					}
				}
				return resultList;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Get a single (should be unique) Execution Environment by hostname and port
	 *
	 * @param hostname
	 *            Host name
	 * @param port
	 *            port
	 * @return the execution environment named with specified hostname and port
	 */
	public ExecutionEnvironment getExecutionEnvironmentByHostNameAndPort(final String hostname, final int port) {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn
					.prepareStatement(LogicMetadataSQLStatements.SqlStatements.SELECT_EXECENV_BY_HOSTNAME_AND_PORT.getSqlStatement())) {
				ps.setString(1, hostname);
				ps.setInt(2, port);
				logger.debug(ps.toString());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return deserializeExecutionEnvironment(rs);
					} else {
						return null;
					}
				} catch (final Exception e2) {
					throw e2;
				}
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Check if there is an execution environment identified by hostname and port provided
	 *
	 * @param hostname
	 *            Hostname of the backend
	 * @param port
	 *            port of the backend
	 * @return TRUE if exists. FALSE otherwise
	 */
	public boolean existsExecutionEnvironmentByHostPort(final String hostname, final int port) {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement existsStatement = conn
					.prepareStatement(LogicMetadataSQLStatements.SqlStatements.EXISTS_EXECUTION_ENVIRONMENT_BY_HOSTPORT.getSqlStatement())) {

				existsStatement.setString(1, hostname);
				existsStatement.setInt(2, port);
				try (ResultSet rs = existsStatement.executeQuery()) {
					rs.next();
					return rs.getBoolean(1);
				}
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Check if there is a storage location identified by hostname and port provided
	 *
	 * @param hostname
	 *            Hostname of the backend
	 * @param port
	 *            port of the backend
	 * @return TRUE if exists. FALSE otherwise
	 */
	public boolean existsStorageLocationByHostPort(final String hostname, final int port) {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement existsStatement = conn
					.prepareStatement(LogicMetadataSQLStatements.SqlStatements.EXISTS_STORAGE_LOCATION_BY_HOSTPORT.getSqlStatement())) {

				existsStatement.setString(1, hostname);
				existsStatement.setInt(2, port);
				try (ResultSet rs = existsStatement.executeQuery()) {
					rs.next();
					return rs.getBoolean(1);
				}
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Inserts the info of a new dataClay instance
	 *
	 * @param dataClayInstance
	 *            info of the dataClay instance
	 */
	public void insertDataClayInstance(final DataClayInstance dataClayInstance) {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(LogicMetadataSQLStatements.SqlStatements.INSERT_DATACLAY_INFO.getSqlStatement())) {

				ps.setObject(1, dataClayInstance.getDcID().getId());
				try {
					final List<String> hosts = dataClayInstance.getHosts();
					final List<Integer> ports = dataClayInstance.getPorts();
					for (int i = 0; i < hosts.size(); i++) {
						ps.setString(2, hosts.get(i));
						ps.setInt(3, ports.get(i));
						ps.executeUpdate();
						logger.debug(ps.toString());
					}
				} catch (final SQLException e) {
					/* A primary key constraint is violated */
					if (e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code) {
						throw new DbObjectAlreadyExistException(dataClayInstance.getDcID());
					} else {
						throw new DbHandlerException(e);
					}
				}
			} catch (final DbObjectAlreadyExistException dbe) {
				throw dbe;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Delete dataclay instance address
	 *
	 * @param host Host
	 * @param port Port
	 * @return true if the object has been successfully deleted, false otherwise
	 */
	public boolean deleteDataClayInstance(final String host, final Integer port) {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(LogicMetadataSQLStatements.SqlStatements.DELETE_DATACLAY.getSqlStatement())) {

				ps.setString(1, host);
				ps.setInt(2, port);
				try {
					ps.executeUpdate();
				} catch (final SQLException e) {
					return false;
				}
				return true;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Get dataClay info by id
	 *
	 * @param dClayID
	 *            id of the dataClay instance
	 * @return info of requested dataClay instance
	 */
	public DataClayInstance getDataClayInfo(final DataClayInstanceID dClayID) {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(LogicMetadataSQLStatements.SqlStatements.SELECT_DATACLAY_INFO_BY_ID.getSqlStatement())) {

				ps.setObject(1, dClayID.getId());
				logger.debug(ps.toString());

				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						logger.debug("Found dataclay instance with id {}. Deserializing.", dClayID);
						final DataClayInstance dcInstance = deserializeDataClayInfo(rs);
						final ArrayList<String> hosts = new ArrayList<>();
						final ArrayList<Integer> ports = new ArrayList<>();
						for (final String host : dcInstance.getHosts()) {
							hosts.add(host);
						}
						for (final Integer port : dcInstance.getPorts()) {
							ports.add(port);
						}
						while (rs.next()) {
							hosts.add(rs.getString("hostname"));
							ports.add(rs.getInt("port"));
						}
						dcInstance.setHosts(hosts);
						dcInstance.setPorts(ports);
						return dcInstance;
					} else {
						return null;
					}
				}
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Get dataClay id.
	 *
	 * Retrieves id of an external dataClay instance identified by host and port
	 *
	 * @param host host 
	 * @param port port
	 * @return id of external dataClay instance
	 */
	public DataClayInstanceID getDataClayID(final String host, final int port) {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(LogicMetadataSQLStatements.SqlStatements.SELECT_DATACLAY_ID_FROM_HOST_PORT.getSqlStatement())) {

				ps.setString(1, host);
				ps.setInt(2, port);
				logger.debug(ps.toString());

				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						final DataClayInstanceID id = new DataClayInstanceID(
								(UUID) rs.getObject("id"));
						return id;
					} else {
						return null;
					}
				}
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
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
	 * Deserialize Storage Location
	 *
	 * @param rs
	 *            Result set
	 * @return Storage Location
	 */
	private StorageLocation deserializeStorageLocation(final ResultSet rs) {
		StorageLocation storageLoc = null;
		try {
			final String hostname = rs.getString("hostname");
			final String name = rs.getString("name");
			final int storageTCPPort = rs.getInt("port");
			final StorageLocationID storageLocationID = new StorageLocationID((UUID) rs.getObject("id"));
			storageLoc = new StorageLocation(hostname, name, storageTCPPort);
			storageLoc.setDataClayID(storageLocationID);

			return storageLoc;
		} catch (final Exception e) {
			throw new DbHandlerException(e);
		}
	}

	/**
	 * Deserialize execution environment
	 *
	 * @param rs
	 *            Result set
	 * @return Execution environment
	 */
	private ExecutionEnvironment deserializeExecutionEnvironment(final ResultSet rs) {
		ExecutionEnvironment executionEnv = null;
		try {
			final ExecutionEnvironmentID executionEnvironmentID = new ExecutionEnvironmentID((UUID) rs.getObject("id"));
			final DataClayInstanceID dataClayInstanceID = new DataClayInstanceID((UUID) rs.getObject("dataClayInstanceID"));
			final String hostname = rs.getString("hostname");
			final String name = rs.getString("name");
			final int port = rs.getInt("port");
			final int langCode = rs.getInt("lang");
			final CommonMessages.Langs lang;
			if (CommonMessages.Langs.LANG_JAVA.getNumber() == langCode) {
				lang = CommonMessages.Langs.LANG_JAVA;
			} else if (CommonMessages.Langs.LANG_PYTHON.getNumber() == langCode) {
				lang = CommonMessages.Langs.LANG_PYTHON;
			} else {
				lang = CommonMessages.Langs.LANG_NONE;
			}
			executionEnv = new ExecutionEnvironment(hostname, name, port, lang, dataClayInstanceID);
			executionEnv.setDataClayID(executionEnvironmentID);

			return executionEnv;
		} catch (final Exception e) {
			throw new DbHandlerException(e);
		}
	}

	/**
	 * Deserialize dataclay info
	 * @param rs Result set
	 * @return Deserialized dataclay instance
	 */
	private DataClayInstance deserializeDataClayInfo(final ResultSet rs) {
		final List<String> hosts = new ArrayList<>();
		final List<Integer> ports = new ArrayList<>();
		try {
			final DataClayInstanceID dClayID = new DataClayInstanceID((UUID) rs.getObject("id"));
			do {
				final String host = rs.getString("hostname");
				final int port = rs.getInt("port");
				logger.debug("Adding {} and {} to dataclay instance", host, port);
				hosts.add(host);
				ports.add(port);
			} while (rs.next());
			return new DataClayInstance(dClayID, hosts.toArray(new String[hosts.size()]),
					ports.toArray(new Integer[ports.size()]));
		} catch (final Exception e) {
			throw new DbHandlerException(e);
		}
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
				existsStatement.close();
			} catch (final Exception e) {
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

			return exists;
		}
	}

	/**
	 * Close DB.
	 */
	public void close() {
		try {
			dataSource.close();
		} catch (final Exception e) {
			// TODO
			e.printStackTrace();
		}
	}

	private Connection getConnection() {
		try {
			return dataSource.getConnection();
		} catch (final SQLException e) {
			throw new DbHandlerException(e);
		}
	}

	private void closeConnection(final Connection conn) {
		try {
			conn.close();
		} catch (final SQLException e) {
			LogicModuleSrv.doExit(e.getErrorCode());
		}
	}

}
