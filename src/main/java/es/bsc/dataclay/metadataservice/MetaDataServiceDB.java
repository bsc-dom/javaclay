
package es.bsc.dataclay.metadataservice;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import es.bsc.dataclay.dbhandler.sql.DataServiceDBSQLStatements;
import es.bsc.dataclay.logic.server.LogicModuleSrv;
import es.bsc.dataclay.util.management.metadataservice.ExternalExecutionEnvironment;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.exceptions.dbhandler.DbHandlerException;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectAlreadyExistException;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectNotExistException;
import es.bsc.dataclay.metadataservice.MetaDataServiceSQLStatements.SqlStatements;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.StorageLocationID;
import es.bsc.dataclay.util.management.metadataservice.DataClayInstance;
import es.bsc.dataclay.util.management.metadataservice.ExecutionEnvironment;
import es.bsc.dataclay.util.management.metadataservice.StorageLocation;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteErrorCode;

/**
 * MetaData data base.
 */
public final class MetaDataServiceDB {
	private static final Logger logger = LogManager.getLogger("MetaDataService.db");

	/** Data source. */
	private final SQLiteDataSource dataSource;

	/**
	 * MetaDataServiceDB constructor.
	 * 
	 * @param dataSource data source
	 */
	public MetaDataServiceDB(final SQLiteDataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Create tables of MDS.
	 */
	public void createTables() {
		synchronized (dataSource) {

			Connection conn = null;
			try {
				conn = getConnection();
				for (final SqlStatements stmt : SqlStatements.values()) {
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
	 * Delete the tables of MDS. Just the other way around of createTables --much simpler.
	 */
	public void dropTables() {
		synchronized (dataSource) {

			final Connection conn = getConnection();
			try {
				for (final SqlStatements stmt : SqlStatements.values()) {
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
	 * Store ObjectMetaData into database
	 * 
	 * @param objectMD
	 *            ObjectMetaData
	 */
	public void store(final ObjectMetaData objectMD) {
		synchronized (dataSource) {

			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(SqlStatements.INSERT_METADATA.getSqlStatement())) {
				ps.setObject(1, objectMD.getDataClayID().getId());
				ps.setObject(2, objectMD.getMetaClassID().getId());
				ps.setObject(3, objectMD.getDataSetID().getId());
				final UUID[] theArray = new UUID[objectMD.getExecutionEnvironmentIDs().size()];
				int i = 0;
				for (final ExecutionEnvironmentID locID : objectMD.getExecutionEnvironmentIDs()) {
					theArray[i] = locID.getId();
					i++;
				}
				Array sqlArray = ps.getConnection().createArrayOf("uuid", theArray);
				ps.setArray(4, sqlArray);
				ps.setBoolean(5, objectMD.isReadOnly());
				if (objectMD.getAlias() != null) {
					ps.setString(6, objectMD.getAlias());
				}
				ps.setInt(7, objectMD.getLang().getNumber());
				ps.setObject(8, objectMD.getOwnerID().getId());

				try {
					ps.executeUpdate();
				} catch (final SQLException e) {
					/* A primary key constraint is violated */
					if (e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code) {
						throw new DbObjectAlreadyExistException(objectMD.getDataClayID());
					} else {
						LogicModuleSrv.doExit(e.getErrorCode());
					}
				}
			} catch (final DbObjectAlreadyExistException dbe) {
				throw dbe;
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Get object metadata by ID
	 * 
	 * @param objectID
	 *            ID of the object
	 * @return The ObjectMetaData or null if it does not exist
	 */
	public ObjectMetaData getByID(final ObjectID objectID) {
		synchronized (dataSource) {

			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(SqlStatements.SELECT_METADATA.getSqlStatement())) {
				ps.setObject(1, objectID.getId());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return deserializeObjectMetaData(rs);
					} else {
						return null;
					}
				}
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
				return null;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}
	
	/**
	 * Get all objects registered in system
	 * 
	 * @return The id of all objects registered
	 */
	public Set<ObjectID> getAllObjectIDs() {
		synchronized (dataSource) {

			final Set<ObjectID> result = new HashSet<>();
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(SqlStatements.SELECT_ALL_OIDS.getSqlStatement())) {
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						final ObjectID objectID = new ObjectID((UUID) rs.getObject("oid"));
						result.add(objectID);
					}
				}
				return result;
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
				return null;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Check if there is an object object metadata identified by ID provided
	 * 
	 * @param objectID
	 *            ID of the object
	 * @return TRUE if exists. FALSE otherwise
	 */
	public boolean existsByID(final ObjectID objectID) {
		synchronized (dataSource) {

			final Connection conn = getConnection();
			try (PreparedStatement existsStatement = conn
					.prepareStatement(SqlStatements.EXISTS_METADATA_BY_ID.getSqlStatement())) {

				existsStatement.setObject(1, objectID.getId());
				try (ResultSet rs = existsStatement.executeQuery()) {
					rs.next();
					return rs.getBoolean(1);
				}
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
				return false;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Check if there is any ObjectMetaData with DataSetID provided
	 * 
	 * @param datasetID
	 *            DataSetID
	 * @return TRUE if there is any ObjectMetaData with DataSetID provided. FALSE otherwise.
	 */
	public boolean existsByDataSetID(final DataSetID datasetID) {
		synchronized (dataSource) {

			final Connection conn = getConnection();
			try (PreparedStatement existsStatement = conn
					.prepareStatement(SqlStatements.EXISTS_METADATA_BY_DATASETID.getSqlStatement())) {

				existsStatement.setObject(1, datasetID.getId());
				try (ResultSet rs = existsStatement.executeQuery()) {
					rs.next();
					return rs.getBoolean(1);
				}
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
				return false;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Delete object metadata identified by ID provided (ignored if object does not exist)
	 * 
	 * @param objectID
	 *            ID of the object
	 */
	public void deleteByID(final ObjectID objectID) throws DbHandlerException {
		synchronized (dataSource) {

			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(SqlStatements.DELETE_METADATA.getSqlStatement())) {

				ps.setObject(1, objectID.getId());
				ps.executeUpdate();
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Update object metadata by ID
	 * 
	 * @param objectID
	 *            ID of the object to update
	 * @param newAlias
	 *            new alias of the object
	 * @throws DbObjectNotExistException
	 *             if object does not exist
	 */
	public void updateAliasByID(final ObjectID objectID, final String newAlias)
			throws DbObjectNotExistException {
		synchronized (dataSource) {

			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(SqlStatements.UPDATE_ALIAS_METADATA.getSqlStatement())) {
				ps.setString(1, newAlias);
				ps.setObject(2, objectID.getId());
				final int count = ps.executeUpdate();
				if (count == 0) {
					throw new DbObjectNotExistException(objectID);
				}
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
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
	 * Update object metadata by ID
	 * 
	 * @param objectID
	 *            ID of object to update
	 * @param newdatasetid
	 *            New dataset id
	 * @throws DbObjectNotExistException
	 *             if object does not exist
	 */
	public void updateDataSetIDByID(final ObjectID objectID, final DataSetID newdatasetid)
			throws DbObjectNotExistException {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(SqlStatements.UPDATE_DATASETID_METADATA.getSqlStatement())) {

				ps.setObject(1, newdatasetid.getId());
				ps.setObject(2, objectID.getId());
				final int count = ps.executeUpdate();
				if (count == 0) {
					throw new DbObjectNotExistException(objectID);
				}
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
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
	 * Update object metadata by ID
	 * 
	 * @param objectID
	 *            ID of object to update
	 * @param newLocs
	 *            New location ids
	 * @throws DbObjectNotExistException
	 *             if object does not exist
	 */
	public void updateLocationIDsByID(final ObjectID objectID, final Set<ExecutionEnvironmentID> newLocs)
			throws DbObjectNotExistException {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(SqlStatements.UPDATE_LOCS_METADATA.getSqlStatement())) {

				final Object[] theArray = new Object[newLocs.size()];
				int i = 0;
				for (final ExecutionEnvironmentID stLocID : newLocs) {
					theArray[i] = stLocID.getId();
					i++;
				}
				final Array sqlArray = ps.getConnection().createArrayOf("uuid", theArray);
				ps.setArray(1, sqlArray);
				ps.setObject(2, objectID.getId());
				final int count = ps.executeUpdate();
				if (count == 0) {
					throw new DbObjectNotExistException(objectID);
				}
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
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
	 * Update object metadata by ID
	 * 
	 * @param objectID
	 *            ID of object to update
	 * @param newisreadonly
	 *            new is read only value
	 * @throws DbObjectNotExistException
	 *             if object does not exist
	 */
	public void updateReadOnlyByID(final ObjectID objectID, final boolean newisreadonly)
			throws DbObjectNotExistException {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(SqlStatements.UPDATE_READONLY_METADATA.getSqlStatement())) {

				ps.setBoolean(1, newisreadonly);
				ps.setObject(2, objectID.getId());
				final int count = ps.executeUpdate();
				if (count == 0) {
					throw new DbObjectNotExistException(objectID);
				}
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
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
	 * Update object metadata by ID
	 * 
	 * @param objectID
	 *            ID of object to update
	 * @param newLocs
	 *            New storage location ids
	 * @throws DbObjectNotExistException
	 *             if object does not exist
	 */
	public void updateForReplicaByID(final ObjectID objectID, final Set<ExecutionEnvironmentID> newLocs,
			final boolean newReadOnly) throws DbObjectNotExistException {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn
					.prepareStatement(SqlStatements.UPDATE_FOR_REPLICA_METADATA.getSqlStatement())) {

				final Object[] theArray = new Object[newLocs.size()];
				int i = 0;
				for (final ExecutionEnvironmentID locID : newLocs) {
					theArray[i] = locID.getId();
					i++;
				}
				final Array sqlArray = ps.getConnection().createArrayOf("uuid", theArray);
				ps.setArray(1, sqlArray);
				ps.setBoolean(2, newReadOnly);
				ps.setObject(3, objectID.getId());
				final int count = ps.executeUpdate();
				if (count == 0) {
					throw new DbObjectNotExistException(objectID);
				}
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
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
	 * Get a set of objects metadata by its Class ID
	 * 
	 * @param classID
	 *            Class ID of the objects
	 * @return The ObjectMetaDatas with class id provided
	 */
	public ArrayList<ObjectMetaData> getByClass(final MetaClassID classID) {
		synchronized (dataSource) {
			final ArrayList<ObjectMetaData> resultList = new ArrayList<>();
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(SqlStatements.SELECT_METADATA_FOR_CLASS.getSqlStatement())) {

				ps.setObject(1, classID.getId());
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						final ObjectMetaData objectMD = deserializeObjectMetaData(rs);
						resultList.add(objectMD);
					}
				}
				return resultList;
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
				return null;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Get anb object by class - alias (null if it does not exist)
	 * 
	 * @param alias
	 *            alias of the object
	 * @return object instancing specified class and aliased with given alias. null if there is none.
	 */
	public ObjectMetaData getByAlias(final String alias) {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(SqlStatements.SELECT_METADATA_BY_ALIAS.getSqlStatement())) {
				ps.setString(1, alias);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return deserializeObjectMetaData(rs);
					} else {
						return null;
					}
				}
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
				return null;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Registers an object to be federated with an external dataClay instance
	 * 
	 * @param objectID
	 *            id of the object to be federated
	 * @param dataClayID
	 *            id of the external dataClay instance
	 * @return true if the object has been successfully registered, false if it was already registered previously
	 */
	public boolean insertFederatedObject(final ObjectID objectID, final DataClayInstanceID dataClayID) {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(SqlStatements.INSERT_FEDERATED_OBJECT.getSqlStatement())) {

				ps.setObject(1, objectID.getId());
				ps.setObject(2, dataClayID.getId());
				try {
					ps.executeUpdate();
				} catch (final SQLException e) {
					/* A primary key constraint is violated */
					if (e.getErrorCode() == SQLiteErrorCode.SQLITE_CONSTRAINT.code) {
						throw new DbObjectAlreadyExistException(dataClayID);
					} else {
						LogicModuleSrv.doExit(e.getErrorCode());
					}
				}
				return true;
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
				return false;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Delete federated object
	 * 
	 * @param objectID
	 *            id of the object to be deleted
	 * @param dataClayID
	 *            id of the external dataClay instance
	 * @return true if the object has been successfully deleted, false otherwise
	 */
	public boolean deleteFederatedObject(final ObjectID objectID, final DataClayInstanceID dataClayID) {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(SqlStatements.DELETE_FEDERATED_OBJECT.getSqlStatement())) {

				ps.setObject(1, objectID.getId());
				ps.setObject(2, dataClayID.getId());
				try {
					ps.executeUpdate();
				} catch (final SQLException e) {
					return false;
				}
				return true;
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
				return false;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Checks if the object is already federated with given external dataClay instance.
	 * 
	 * @param objectID
	 *            id of the object to be checked
	 * @param dataClayID
	 *            id of the external dataClay id
	 * @return true if the object is already registered as federated object with given external dataClay instance. false
	 *         otherwise.
	 */
	public boolean existsFederatedObjectWithDC(final ObjectID objectID, final DataClayInstanceID dataClayID) {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn
					.prepareStatement(SqlStatements.EXISTS_FEDERATED_OBJECT_WITH_DC.getSqlStatement())) {

				ps.setObject(1, objectID.getId());
				ps.setObject(2, dataClayID.getId());
				try (ResultSet rs = ps.executeQuery()) {
					rs.next();
					return rs.getBoolean(1);
				}
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
				return false;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Checks if the object is already federated.
	 * 
	 * @param objectID
	 *            id of the object to be checked
	 * @return true if the object is already registered as federated object with given external dataClay instance. false
	 *         otherwise.
	 */
	public boolean existsFederatedObject(final ObjectID objectID) {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(SqlStatements.EXISTS_FEDERATED_OBJECT.getSqlStatement())) {

				ps.setObject(1, objectID.getId());
				try (ResultSet rs = ps.executeQuery()) {
					rs.next();
					return rs.getBoolean(1);
				}
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
				return false;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Retrieves external target dataClays ids which the given object is federated with
	 * 
	 * @param objectID
	 *            id of the object to be checked
	 * @return ids of external dataClays which the object is federated with
	 */
	public Set<DataClayInstanceID> getDataClaysOurObjectIsFederatedWith(final ObjectID objectID) {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			final Set<DataClayInstanceID> result = new HashSet<>();

			try (PreparedStatement ps = conn
					.prepareStatement(SqlStatements.SELECT_TARGET_DATACLAYS_FEDERATED_OBJECT.getSqlStatement())) {

				ps.setObject(1, objectID.getId());
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						final DataClayInstanceID extDataClayID = new DataClayInstanceID(
								(UUID) rs.getObject("targetDataClayID"));
						result.add(extDataClayID);
					}
				}
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}

			return result;
		}
	}

	/**
	 * Method that retrieves all the objects federated/belonging to dataClay with ID provided.
	 * 
	 * @param extDataClayInstanceID
	 *            id of dataclay
	 * @return all the objects federated/belonging to dataClay with ID provided.
	 */
	public Set<ObjectID> getObjectsFederatedWithDataClay(final DataClayInstanceID extDataClayInstanceID) {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			final Set<ObjectID> result = new HashSet<>();

			try (PreparedStatement ps = conn
					.prepareStatement(SqlStatements.SELECT_OBJECTS_FEDERATED_WITH_DATACLAY.getSqlStatement())) {

				ps.setObject(1, extDataClayInstanceID.getId());
				ps.setObject(2, extDataClayInstanceID.getId());
				ps.setBoolean(3, false);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						final ObjectID objectID = new ObjectID(
								(UUID) rs.getObject("oid"));
						result.add(objectID);
					}
				}
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
			} catch (final Exception e) {
				e.printStackTrace();
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}

			return result;
		}
	}

	/**
	 * Registers an object that has been federated from an external dataClay instance.
	 * 
	 * @param objectID
	 *            ID of the external object
	 * @param srcDataClayID
	 *            id of the source dataClay instance federating this object
	 * @param unregisteredFlag Indicates object is unregistered
	 */
	public void insertExternalObject(final ObjectID objectID, final DataClayInstanceID srcDataClayID, 
			final boolean unregisteredFlag) {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(SqlStatements.INSERT_EXTERNAL_OBJECT.getSqlStatement())) {

				ps.setObject(1, objectID.getId());
				ps.setObject(2, srcDataClayID.getId());
				ps.setBoolean(3, unregisteredFlag);
				ps.executeUpdate();
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Delete a federated object from an external dataClay instance.
	 * 
	 * @param objectID
	 *            ID of the external object
	 */
	public void deleteExternalObject(final ObjectID objectID) {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(SqlStatements.DELETE_EXTERNAL_OBJECT.getSqlStatement())) {
				ps.setObject(1, objectID.getId());
				ps.executeUpdate();
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Checks whether the given object is externally federated
	 * 
	 * @param objectID
	 *            id of the object to be checked
	 * @param unregisteredFlag Indicates if object is unregistered or not
	 * @return true if it is an external object federated. false otherwise.
	 */
	public boolean existsExternalObject(final ObjectID objectID, final boolean unregisteredFlag) {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(SqlStatements.EXISTS_EXTERNAL_OBJECT.getSqlStatement())) {

				ps.setObject(1, objectID.getId());
				ps.setBoolean(2, unregisteredFlag);
				try (ResultSet rs = ps.executeQuery()) {
					rs.next();
					return rs.getBoolean(1);
				}
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
				return false;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Get unregistered external objects.
	 * @return id of external objects unregistered
	 */
	public Set<ObjectID> getUnregisteredExternalObjects() {
		synchronized (dataSource) {
			final Set<ObjectID> resultList = new HashSet<>();
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn
					.prepareStatement(SqlStatements.SELECT_UNREGISTERED_EXTERNAL_OBJECTS.getSqlStatement())) {
				ps.setBoolean(1, true);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						final ObjectID oid = new ObjectID(
								(UUID) rs.getObject("oid"));
						resultList.add(oid);
					}
				}
				return resultList;
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
				return null;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	
	/**
	 * Update external object flag unregistered
	 * 
	 * @param objectID
	 *            ID of the object to update
	 * @param unregisteredFlag
	 *            Value of flag unregistered
	 * @throws DbObjectNotExistException
	 *             if object does not exist
	 */
	public void updateUnregisteredFlagExternalObject(final ObjectID objectID, final boolean unregisteredFlag)
			throws DbObjectNotExistException {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn.prepareStatement(SqlStatements.UPDATE_UNREGISTER_EXTERNAL_OBJECT.getSqlStatement())) {

				ps.setBoolean(1, unregisteredFlag);
				ps.setObject(2, objectID.getId());
				final int count = ps.executeUpdate();
				if (count == 0) {
					throw new DbObjectNotExistException(objectID);
				}
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
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
	 * Retrieves external source dataClay id owning the given object.
	 * 
	 * @param objectID
	 *            id of the object to be checked
	 * @return id of the external source dataClay
	 */
	public DataClayInstanceID getExternalDataClayOfObject(final ObjectID objectID) {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn
					.prepareStatement(SqlStatements.SELECT_SOURCE_DATACLAY_EXTERNAL_OBJECT.getSqlStatement())) {

				ps.setObject(1, objectID.getId());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return new DataClayInstanceID((UUID) rs.getObject("srcDataClayID"));
					} else {
						return null;
					}
				}
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
				return null;
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
		}
	}

	/**
	 * Vacuum database.
	 */
	public void vacuum() {
		synchronized (dataSource) {
			final Connection conn = getConnection();
			try (PreparedStatement ps = conn
					.prepareStatement(SqlStatements.VACUUM.getSqlStatement())) {
				ps.executeUpdate();
			} catch (final SQLException e) {
				LogicModuleSrv.doExit(e.getErrorCode());
			} catch (final Exception e) {
				throw new DbHandlerException(e);
			} finally {
				closeConnection(conn);
			}
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
			LogicModuleSrv.doExit(e.getErrorCode());
			return null;
		}
	}

	private void closeConnection(final Connection conn) {
		try {
			conn.close();
		} catch (final SQLException e) {
			LogicModuleSrv.doExit(e.getErrorCode());
		}
	}

	/**
	 * Deserialize object metadata
	 * 
	 * @param rs
	 *            Result set
	 * @return Object MetaData
	 */
	private ObjectMetaData deserializeObjectMetaData(final ResultSet rs) {
		ObjectMetaData objectMD = null;
		try {
			final ObjectID objectID = new ObjectID((UUID) rs.getObject("oid"));
			final MetaClassID classID = new MetaClassID((UUID) rs.getObject("classid"));
			final DataSetID dataSetID = new DataSetID((UUID) rs.getObject("datasetid"));
			Array sqlArray = rs.getArray("stlocs");
			final UUID[] uuidArray = (UUID[]) sqlArray.getArray();
			final HashSet<ExecutionEnvironmentID> backendIDs = new HashSet<>();
			for (final UUID uuid : uuidArray) {
				final ExecutionEnvironmentID backendID = new ExecutionEnvironmentID(uuid);
				backendIDs.add(backendID);
			}
			final boolean isReadOnly = rs.getBoolean("isreadonly");
			final String alias = rs.getString("alias");
			final int langCode = rs.getInt("language");
			final Langs lang;

			if (Langs.LANG_PYTHON.getNumber() == langCode) {
				lang = Langs.LANG_PYTHON;
			} else if (Langs.LANG_JAVA.getNumber() == langCode) {
				lang = Langs.LANG_JAVA;
			} else {
				logger.warn("Deserializing language none!");
				lang = Langs.LANG_NONE;
			}

			final AccountID ownerID = new AccountID((UUID) rs.getObject("accountid"));

			objectMD = new ObjectMetaData(objectID, classID, dataSetID, backendIDs, isReadOnly, alias, lang, ownerID);

			return objectMD;
		} catch (final SQLException e) {
			LogicModuleSrv.doExit(e.getErrorCode());
			return null;
		} catch (final Exception e) {
			throw new DbHandlerException(e);
		}
	}

}
