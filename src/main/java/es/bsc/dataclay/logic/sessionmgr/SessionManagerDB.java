
package es.bsc.dataclay.logic.sessionmgr;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.dbhandler.Utils;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectAlreadyExistException;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.DataContractID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.ids.SessionID;
import es.bsc.dataclay.util.management.sessionmgr.SessionContract;
import es.bsc.dataclay.util.management.sessionmgr.SessionDataContract;
import es.bsc.dataclay.util.management.sessionmgr.SessionImplementation;
import es.bsc.dataclay.util.management.sessionmgr.SessionInterface;
import es.bsc.dataclay.util.management.sessionmgr.SessionOperation;
import es.bsc.dataclay.util.management.sessionmgr.SessionProperty;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;

/**
 * Data base connection.
 */
public final class SessionManagerDB {

	/** Data source. */
	private final SQLiteDataSource dataSource;

	/** Logger. */
	private Logger logger;

	/** Indicates if debug is enabled. */
	private static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/**
	 * Constructor.
	 * 
	 * @param managerName
	 *            Name of the LM service managing.
	 */
	public SessionManagerDB(final SQLiteDataSource dataSource) {
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
				for (final SessionManagerSQLStatements.SqlStatements stmt : SessionManagerSQLStatements.SqlStatements.values()) {
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
				for (final SessionManagerSQLStatements.SqlStatements stmt : SessionManagerSQLStatements.SqlStatements.values()) {
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
	 * Store into database
	 * @param infoSession
	 *            Object to store
	 * @return UUID of object stored
	 */
	private UUID store(final SessionInterface infoSession) {

		UUID[] sessPropsUUIDs = null;
		if (infoSession.getSessionProperties() != null) {
			sessPropsUUIDs = new UUID[infoSession.getSessionProperties().size()];
			int i = 0;
			for (final SessionProperty sessProp : infoSession.getSessionProperties().values()) {
				sessPropsUUIDs[i] = this.store(sessProp);
				i++;
			}
		}

		UUID[] sessOpsUUIDs = null;
		if (infoSession.getSessionOperations() != null) {
			sessOpsUUIDs = new UUID[infoSession.getSessionOperations().size()];
			int i = 0;
			for (final SessionOperation sessOp : infoSession.getSessionOperations().values()) {
				sessOpsUUIDs[i] = this.store(sessOp);
				i++;
			}
		}

		Connection conn = null;
		final UUID uuid = UUID.randomUUID();
		infoSession.setId(uuid);
		try {
			conn = dataSource.getConnection();
			final PreparedStatement insertStatement = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.INSERT_SESSIONINTERFACE.getSqlStatement());
			insertStatement.setObject(1, uuid);
			// CHECKSTYLE:OFF

			insertStatement.setObject(2, infoSession.getInterfaceID().getId());
			if (sessPropsUUIDs != null) {
				final Array tArray = insertStatement.getConnection().createArrayOf("uuid", sessPropsUUIDs);
				insertStatement.setArray(3, tArray);
			} else {
				insertStatement.setArray(3, null);
			}

			if (sessOpsUUIDs != null) {
				final Array tArray = insertStatement.getConnection().createArrayOf("uuid", sessOpsUUIDs);
				insertStatement.setArray(4, tArray);
			} else {
				insertStatement.setArray(4, null);
			}

			insertStatement.setObject(5, infoSession.getClassOfInterface().getId());

			if (infoSession.getImportOfInterface() != null) {
				insertStatement.setObject(6, infoSession.getImportOfInterface().getId());
			} else {
				insertStatement.setObject(6, null);
			}

			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + insertStatement);
			}
			insertStatement.executeUpdate();

		} catch (final Exception e) {
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
		return uuid;
	}

	/**
	 * Store into database
	 * @param infoSession
	 *            Object to store
	 * @return UUID of object stored
	 */
	private UUID store(final SessionContract infoSession) {

		final UUID[] sessInterfaceIDs = new UUID[infoSession.getSessionInterfaces().size()];
		int i = 0;
		for (final SessionInterface sessionIface : infoSession.getSessionInterfaces().values()) {
			sessInterfaceIDs[i] = this.store(sessionIface);
			i++;
		}

		Connection conn = null;
		final UUID uuid = UUID.randomUUID();
		infoSession.setId(uuid);
		try {
			conn = dataSource.getConnection();
			final PreparedStatement insertStatement = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.INSERT_SESSIONCONTRACT.getSqlStatement());
			insertStatement.setObject(1, uuid);
			// CHECKSTYLE:OFF
			insertStatement.setObject(2, infoSession.getContractID().getId());

			final Array tArray = insertStatement.getConnection().createArrayOf("uuid", sessInterfaceIDs);
			insertStatement.setArray(3, tArray);

			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + insertStatement);
			}
			insertStatement.executeUpdate();

		} catch (final Exception e) {
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
		return uuid;

	}

	/**
	 * Store into database
	 * @param infoSession
	 *            Object to store
	 * @return UUID of object stored
	 */
	private UUID store(final SessionProperty infoSession) {
		Connection conn = null;
		final UUID uuid = UUID.randomUUID();
		infoSession.setId(uuid);
		try {
			conn = dataSource.getConnection();
			final PreparedStatement insertStatement = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.INSERT_SESSIONPROPERTY.getSqlStatement());
			insertStatement.setObject(1, uuid);
			insertStatement.setObject(2, infoSession.getPropertyID().getId());
			// CHECKSTYLE:OFF
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + insertStatement);
			}
			insertStatement.executeUpdate();

		} catch (final Exception e) {
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
		return uuid;

	}

	/**
	 * Store into database
	 * @param infoSession
	 *            Object to store
	 * @return UUID of object stored
	 */
	private UUID store(final SessionImplementation infoSession) {
		Connection conn = null;
		final UUID uuid = UUID.randomUUID();
		infoSession.setId(uuid);
		try {
			conn = dataSource.getConnection();
			final PreparedStatement insertStatement = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.INSERT_SESSIONIMPLEMENTATION.getSqlStatement());
			insertStatement.setObject(1, uuid);
			insertStatement.setObject(2, infoSession.getImplementationID().getId());
			// CHECKSTYLE:OFF
			insertStatement.setObject(3, infoSession.getNamespaceID().getId());
			insertStatement.setObject(4, infoSession.getRespAccountID().getId());
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + insertStatement);
			}
			insertStatement.executeUpdate();

		} catch (final Exception e) {
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
		return uuid;

	}

	/**
	 * Store into database
	 * @param infoSession
	 *            Object to store
	 * @return UUID of object stored
	 */
	private UUID store(final SessionOperation infoSession) {

		UUID localImplUUID = null;
		if (infoSession.getSessionLocalImplementation() != null) {
			localImplUUID = this.store(infoSession.getSessionLocalImplementation());
		}

		UUID remoteImplUUID = null;
		if (infoSession.getSessionRemoteImplementation() != null) {
			remoteImplUUID = this.store(infoSession.getSessionRemoteImplementation());
		}

		Connection conn = null;
		final UUID uuid = UUID.randomUUID();
		infoSession.setId(uuid);
		try {
			conn = dataSource.getConnection();
			final PreparedStatement insertStatement = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.INSERT_SESSIONOPERATION.getSqlStatement());
			insertStatement.setObject(1, uuid);
			insertStatement.setObject(2, infoSession.getOperationID().getId());
			// CHECKSTYLE:OFF

			insertStatement.setObject(3, localImplUUID);
			insertStatement.setObject(4, remoteImplUUID);

			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + insertStatement);
			}
			insertStatement.executeUpdate();

		} catch (final Exception e) {
			e.printStackTrace();
			throw new DbObjectAlreadyExistException(infoSession.getOperationID());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (final SQLException ex1) {
				ex1.printStackTrace();
			}
		}
		return uuid;

	}

	/**
	 * Store into database
	 * @param infoSession
	 *            Object to store
	 * @return UUID of object stored
	 */
	private UUID store(final SessionDataContract infoSession) {
		Connection conn = null;
		final UUID uuid = UUID.randomUUID();
		infoSession.setId(uuid);
		try {
			conn = dataSource.getConnection();
			final PreparedStatement insertStatement = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.INSERT_SESSIONDATACONTRACT.getSqlStatement());
			insertStatement.setObject(1, uuid);
			// CHECKSTYLE:OFF
			insertStatement.setObject(2, infoSession.getDataContractID().getId());
			insertStatement.setObject(3, infoSession.getDataSetOfProvider().getId());
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + insertStatement);
			}
			insertStatement.executeUpdate();

		} catch (final Exception e) {
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
		return uuid;

	}

	/**
	 * Store into database
	 * @param infoSession
	 *            Object to store
	 */
	public void store(final Session infoSession) {
		synchronized (dataSource) {

			final List<UUID> propKeysList = new ArrayList<>();
			final List<UUID> propValuesList = new ArrayList<>();
			int i = 0;
			for (final Entry<MetaClassID, Set<PropertyID>> entry : infoSession.getPropertiesOfClasses().entrySet()) {
				final MetaClassID classID = entry.getKey();
				for (final PropertyID propID : entry.getValue()) {
					propKeysList.add(classID.getId());
					propValuesList.add(propID.getId());
					i++;
				}
			}

			final UUID[] propKeys = propKeysList.toArray(new UUID[propKeysList.size()]);
			final UUID[] propValues = propValuesList.toArray(new UUID[propValuesList.size()]);
			final UUID[] sessContractKeys = new UUID[infoSession.getSessionContracts().size()];
			final UUID[] sessContractValues = new UUID[infoSession.getSessionContracts().size()];
			i = 0;
			for (final Entry<ContractID, SessionContract> entry : infoSession.getSessionContracts().entrySet()) {
				sessContractKeys[i] = entry.getKey().getId();
				sessContractValues[i] = this.store(entry.getValue());
				i++;
			}

			final UUID[] sessDataContractKeys = new UUID[infoSession.getSessionDataContracts().size()];
			final UUID[] sessDataContractValues = new UUID[infoSession.getSessionDataContracts().size()];
			i = 0;
			for (final Entry<DataContractID, SessionDataContract> entry : infoSession.getSessionDataContracts().entrySet()) {
				sessDataContractKeys[i] = entry.getKey().getId();
				sessDataContractValues[i] = this.store(entry.getValue());
				i++;
			}

			final UUID[] ifaceBitmapsKeys = new UUID[infoSession.getIfaceBitmaps().size()];
			final String[] ifaceBitmapsValues = new String[infoSession.getIfaceBitmaps().size()];
			i = 0;
			for (final Entry<MetaClassID, byte[]> entry : infoSession.getIfaceBitmaps().entrySet()) {
				ifaceBitmapsKeys[i] = entry.getKey().getId();
				ifaceBitmapsValues[i] = Utils.bytesToHex(entry.getValue());
				i++;
			}

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.INSERT_SESSIONINFO.getSqlStatement());
				insertStatement.setObject(1, infoSession.getDataClayID().getId());

				// CHECKSTYLE:OFF
				insertStatement.setObject(2, infoSession.getAccountID().getId());
				Array tArray = insertStatement.getConnection().createArrayOf("uuid", propKeys);
				insertStatement.setArray(3, tArray);
				tArray = insertStatement.getConnection().createArrayOf("uuid", propValues);
				insertStatement.setArray(4, tArray);
				tArray = insertStatement.getConnection().createArrayOf("uuid", sessContractKeys);
				insertStatement.setArray(5, tArray);
				tArray = insertStatement.getConnection().createArrayOf("uuid", sessContractValues);
				insertStatement.setArray(6, tArray);
				tArray = insertStatement.getConnection().createArrayOf("uuid", sessDataContractKeys);
				insertStatement.setArray(7, tArray);
				tArray = insertStatement.getConnection().createArrayOf("uuid", sessDataContractValues);
				insertStatement.setArray(8, tArray);

				insertStatement.setObject(9, infoSession.getDataContractIDofStore().getId());
				insertStatement.setString(10, infoSession.getLanguage().name());

				tArray = insertStatement.getConnection().createArrayOf("uuid", ifaceBitmapsKeys);
				insertStatement.setArray(11, tArray);

				tArray = insertStatement.getConnection().createArrayOf("varchar", ifaceBitmapsValues);
				insertStatement.setArray(12, tArray);

				insertStatement.setDate(13, new java.sql.Date(infoSession.getEndDate().getTimeInMillis()));

				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();

			} catch (final Exception e) {
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
	 * Deserialize
	 * @param rs
	 *            Result set
	 * @return SessionProperty
	 */
	private SessionProperty deserializeSessionProperty(
			final ResultSet rs) {
		SessionProperty session = null;
		try {
			// CHECKSTYLE:OFF
			final UUID uuid = (UUID)rs.getObject("id");
			final PropertyID propertyID = new PropertyID((UUID)rs.getObject("propertyID"));

			session = new SessionProperty();
			session.setId(uuid);
			session.setPropertyID(propertyID);

		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return session;
	}

	/**
	 * Deserialize
	 * @param rs
	 *            Result set
	 * @return SessionImplementation
	 */
	private SessionImplementation deserializeSessionImplementation(
			final ResultSet rs) {
		SessionImplementation Session = null;
		try {
			// CHECKSTYLE:OFF
			final UUID uuid = (UUID)rs.getObject("id");
			final ImplementationID implementationID = new ImplementationID((UUID)rs.getObject("implementationID"));
			final AccountID respAccountID = new AccountID((UUID)rs.getObject("respAccountID"));
			final NamespaceID namespaceID = new NamespaceID((UUID)rs.getObject("namespaceID"));

			Session = new SessionImplementation();
			Session.setId(uuid);
			Session.setRespAccountID(respAccountID);
			Session.setNamespaceID(namespaceID);
			Session.setImplementationID(implementationID);

		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return Session;
	}

	/**
	 * Deserialize
	 * @param rs
	 *            Result set
	 * @return SessionOperation
	 */
	private SessionOperation deserializeSessionOperation(
			final ResultSet rs) {
		SessionOperation Session = null;
		try {
			// CHECKSTYLE:OFF
			final UUID uuid = (UUID)rs.getObject("id");
			final OperationID operationID = new OperationID((UUID)rs.getObject("operationID"));
			final SessionImplementation sessionLocalImplementation = this.getSessionImplementationByID((UUID)rs.getObject("sessionLocalImplementation"));
			final SessionImplementation sessionRemoteImplementation = this.getSessionImplementationByID((UUID)rs.getObject("sessionRemoteImplementation"));

			Session = new SessionOperation();
			Session.setId(uuid);
			Session.setOperationID(operationID);
			Session.setSessionLocalImplementation(sessionLocalImplementation);
			Session.setSessionRemoteImplementation(sessionRemoteImplementation);

		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return Session;
	}

	/**
	 * Deserialize
	 * @param rs
	 *            Result set
	 * @return SessionContract
	 */
	private SessionContract deserializeSessionContract(
			final ResultSet rs) {
		SessionContract Session = null;
		try {
			// CHECKSTYLE:OFF
			final UUID uuid = (UUID)rs.getObject("id");
			final ContractID contractID = new ContractID((UUID)rs.getObject("contractID"));

			final Map<InterfaceID, SessionInterface> sessionInterfaces = new HashMap<>();
			final UUID[] uuids = (UUID[]) rs.getArray("sessionInterfaces").getArray();
			for (final UUID curUUID : uuids) {
				final SessionInterface sessIface = this.getSessionInterfaceByID(curUUID);
				final InterfaceID ifaceID = sessIface.getInterfaceID();
				sessionInterfaces.put(ifaceID, sessIface);
			}

			Session = new SessionContract();
			Session.setId(uuid);
			Session.setContractID(contractID);
			Session.setSessionInterfaces(sessionInterfaces);

		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return Session;
	}

	/**
	 * Deserialize
	 * @param rs
	 *            Result set
	 * @return SessionInterface
	 */
	private SessionInterface deserializeSessionInterface(
			final ResultSet rs) {
		SessionInterface iface = null;
		try {
			// CHECKSTYLE:OFF
			final UUID uuid = (UUID)rs.getObject("id");
			final InterfaceID interfaceID = new InterfaceID((UUID)rs.getObject("interfaceID"));
			final MetaClassID classOfInterface = new MetaClassID((UUID)rs.getObject("classOfInterface"));
			final InterfaceID importOfInterface = new InterfaceID((UUID)rs.getObject("importOfInterface"));

			final Map<PropertyID, SessionProperty> sessionProperties = new HashMap<>();
			final Map<OperationID, SessionOperation> sessionOperations = new HashMap<>();

			Array array = rs.getArray("sessionOperations");
			if (array != null) {
				final UUID[] opsUUIDs = (UUID[]) array.getArray();
				for (final UUID opUUID : opsUUIDs) {
					final SessionOperation op = this.getSessionOperationByID(opUUID);
					final OperationID opID = op.getOperationID();
					sessionOperations.put(opID, op);
				}
			}

			array = rs.getArray("sessionProperties");
			if (array != null) {
				final UUID[] propsUUIDs = (UUID[]) array.getArray();
				for (final UUID propUUID : propsUUIDs) {
					final SessionProperty prop = this.getSessionPropertyByID(propUUID);
					final PropertyID propID = prop.getPropertyID();
					sessionProperties.put(propID, prop);
				}
			}

			iface = new SessionInterface();
			iface.setClassOfInterface(classOfInterface);
			iface.setId(uuid);
			iface.setImportOfInterface(importOfInterface);
			iface.setInterfaceID(interfaceID);
			iface.setSessionOperations(sessionOperations);
			iface.setSessionProperties(sessionProperties);

		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return iface;
	}

	/**
	 * Deserialize
	 * @param rs
	 *            Result set
	 * @return SessionContract
	 */
	private SessionDataContract deserializeSessionDataContract(
			final ResultSet rs) {
		SessionDataContract Session = null;
		try {
			// CHECKSTYLE:OFF
			final UUID uuid = (UUID)rs.getObject("id");
			final DataContractID datacontractID = new DataContractID((UUID)rs.getObject("datacontractID"));
			final DataSetID datasetOfProvider = new DataSetID((UUID)rs.getObject("datasetOfProvider"));

			Session = new SessionDataContract();
			Session.setId(uuid);
			Session.setDataContractID(datacontractID);
			Session.setDataSetOfProvider(datasetOfProvider);

		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return Session;
	}

	/**
	 * Deserialize
	 * @param rs
	 *            Result set
	 * @return SessionContract
	 */
	private Session deserializeSession(
			final ResultSet rs) {
		Session Session = null;
		try {
			// CHECKSTYLE:OFF
			final SessionID sessionID = new SessionID((UUID)rs.getObject("id"));
			final AccountID accountID = new AccountID((UUID)rs.getObject("accountID"));

			final UUID[] propertiesOfClassesKeys = (UUID[]) rs.getArray("propertiesOfClassesKeys").getArray();
			final UUID[] propertiesOfClassesValues = (UUID[]) rs.getArray("propertiesOfClassesValues").getArray();
			final Map<MetaClassID, Set<PropertyID>> propertiesOfClasses = new HashMap<>();
			for (int i = 0; i < propertiesOfClassesKeys.length; ++i) {
				final MetaClassID classID = new MetaClassID(propertiesOfClassesKeys[i]);
				Set<PropertyID> propIDs = propertiesOfClasses.get(classID);
				if (propIDs == null) {
					propIDs = new HashSet<>();
					propertiesOfClasses.put(classID, propIDs);
				}
				final PropertyID propID = new PropertyID(propertiesOfClassesValues[i]);
				propIDs.add(propID);
			}

			final UUID[] sessionContractsKeys = (UUID[]) rs.getArray("sessionContractsKeys").getArray();
			final UUID[] sessionContractsValues = (UUID[]) rs.getArray("sessionContractsValues").getArray();
			final Map<ContractID, SessionContract> sessionContracts = new HashMap<>();
			for (int i = 0; i < sessionContractsKeys.length; ++i) {
				final ContractID contractID = new ContractID(sessionContractsKeys[i]);
				final SessionContract sessionContract = this.getSessionContractByID(sessionContractsValues[i]);
				sessionContracts.put(contractID, sessionContract);
			}

			final UUID[] sessionDataContractsKeys = (UUID[]) rs.getArray("sessionDataContractsKeys").getArray();
			final UUID[] sessionDataContractsValues = (UUID[]) rs.getArray("sessionDataContractsValues").getArray();
			final Map<DataContractID, SessionDataContract> sessionDataContracts = new HashMap<>();
			for (int i = 0; i < sessionDataContractsKeys.length; ++i) {
				final DataContractID datacontractID = new DataContractID(sessionDataContractsKeys[i]);
				final SessionDataContract sessionDataContract = this.getSessionDataContractByID(sessionDataContractsValues[i]);
				sessionDataContracts.put(datacontractID, sessionDataContract);
			}

			final Map<MetaClassID, byte[]> ifaceBitmaps = new HashMap<>();
			final UUID[] ifaceBitmapsKeys = (UUID[]) rs.getArray("ifaceBitmapsKeys").getArray();
			final String[] subRs = (String[]) rs.getArray("ifaceBitmapsValues").getArray();
			for (int i = 0; i < ifaceBitmapsKeys.length; ++i) {
				final MetaClassID classID = new MetaClassID(ifaceBitmapsKeys[i]);
				final byte[] values = Utils.hexStringToByteArray(subRs[i]);
				ifaceBitmaps.put(classID, values);
			}

			final DataContractID dataContractIDforStore = new DataContractID((UUID)rs.getObject("dataContractIDforStore"));
			final Langs language = Langs.valueOf(rs.getString("language"));
			final Calendar endDate = Calendar.getInstance();
			endDate.setTime(rs.getDate("endDate"));

			Session = new Session();
			Session.setAccountID(accountID);
			Session.setDataContractIDofStore(dataContractIDforStore);
			Session.setEndDate(endDate);
			Session.setIfaceBitmaps(ifaceBitmaps);
			Session.setLanguage(language);
			Session.setPropertiesOfClasses(propertiesOfClasses);
			Session.setSessionContracts(sessionContracts);
			Session.setSessionDataContracts(sessionDataContracts);
			Session.setDataClayID(sessionID);

		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return Session;
	}

	/**
	 * Get by ID
	 * @param uuid
	 *            ID
	 * @return The info
	 * @throws SQLException
	 *             if some exception occurs
	 */
	private SessionProperty getSessionPropertyByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		SessionProperty info = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.SELECT_SESSIONPROPERTY.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				info = deserializeSessionProperty(rs);
			}

		} catch (final SQLException e) {
			throw e;
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

		return info;
	}

	/**
	 * Get by ID
	 * @param uuid
	 *            ID
	 * @return The info
	 * @throws SQLException
	 *             if some exception occurs
	 */
	private SessionImplementation getSessionImplementationByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		SessionImplementation info = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.SELECT_SESSIONIMPLEMENTATION.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				info = deserializeSessionImplementation(rs);
			}

		} catch (final SQLException e) {
			throw e;
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

		return info;
	}

	/**
	 * Get by ID
	 * @param uuid
	 *            ID
	 * @return The info
	 * @throws SQLException
	 *             if some exception occurs
	 */
	private SessionOperation getSessionOperationByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		SessionOperation info = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.SELECT_SESSIONOPERATION.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				info = deserializeSessionOperation(rs);
			}

		} catch (final SQLException e) {
			throw e;
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

		return info;
	}

	/**
	 * Get by ID
	 * @param uuid
	 *            ID
	 * @return The info
	 * @throws SQLException
	 *             if some exception occurs
	 */
	private SessionInterface getSessionInterfaceByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		SessionInterface info = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.SELECT_SESSIONINTERFACE.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				info = deserializeSessionInterface(rs);
			}

		} catch (final SQLException e) {
			throw e;
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

		return info;
	}

	/**
	 * Get by ID
	 * @param uuid
	 *            ID
	 * @return The info
	 * @throws SQLException
	 *             if some exception occurs
	 */
	private SessionContract getSessionContractByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		SessionContract info = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.SELECT_SESSIONCONTRACT.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				info = deserializeSessionContract(rs);
			}

		} catch (final SQLException e) {
			throw e;
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

		return info;
	}

	/**
	 * Get by ID
	 * @param uuid
	 *            ID
	 * @return The info
	 * @throws SQLException
	 *             if some exception occurs
	 */
	private SessionDataContract getSessionDataContractByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		SessionDataContract info = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.SELECT_SESSIONDATACONTRACT.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				info = deserializeSessionDataContract(rs);
			}

		} catch (final SQLException e) {
			throw e;
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

		return info;
	}

	/**
	 * Get session info by ID
	 * @param sessionID
	 *            ID of the session
	 * @return The info of the session. Null if it is not registered.
	 */
	public Session getSessionByID(final SessionID sessionID) {
		synchronized (dataSource) {

			ResultSet rs = null;
			Session info = null;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.SELECT_SESSIONINFO.getSqlStatement());

				selectStatement.setObject(1, sessionID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				if (rs.next()) {
					info = deserializeSession(rs);
				}

			} catch (final SQLException e) {
				return null;
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

			return info;
		}
	}

	/**
	 * Get Sessions of account
	 * @param accountID
	 *            AccountID ID
	 * @return The Sessions
	 */
	public List<Session> getSessionsOfAccount(final AccountID accountID) {
		synchronized (dataSource) {

			ResultSet rs = null;
			final List<Session> result = new ArrayList<>();
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.SELECT_SESSIONS_OF_ACCOUNT.getSqlStatement());

				selectStatement.setObject(1, accountID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				while (rs.next()) {
					result.add(deserializeSession(rs));
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
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deleteSessionDataContract(final UUID uuid) {

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.DELETE_SESSIONDATACONTRACT.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();

		} catch (final Exception e) {
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

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deleteSessionImplementation(final UUID uuid) {

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.DELETE_SESSIONIMPLEMENTATION.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();

		} catch (final Exception e) {
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

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 * @throws SQLException
	 *             if some exception occurs
	 */
	private void deleteSessionOperation(final UUID uuid) throws SQLException {

		final SessionOperation sessOperation = this.getSessionOperationByID(uuid);
		this.deleteSessionImplementation(sessOperation.getSessionLocalImplementation().getId());
		this.deleteSessionImplementation(sessOperation.getSessionRemoteImplementation().getId());

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.DELETE_SESSIONOPERATION.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();

		} catch (final Exception e) {
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

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 */
	private void deleteSessionProperty(final UUID uuid) {

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.DELETE_SESSIONPROPERTY.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();

		} catch (final Exception e) {
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

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 * @throws SQLException
	 *             if some exception occurs
	 */
	private void deleteSessionInterface(final UUID uuid) throws SQLException {

		final SessionInterface sessIface = this.getSessionInterfaceByID(uuid);
		for (final SessionOperation sessOp : sessIface.getSessionOperations().values()) {
			this.deleteSessionOperation(sessOp.getId());
		}
		for (final SessionProperty sessProp : sessIface.getSessionProperties().values()) {
			this.deleteSessionProperty(sessProp.getId());
		}

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.DELETE_SESSIONINTERFACE.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();

		} catch (final Exception e) {
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

	/**
	 * Delete
	 * @param uuid
	 *            UUID of object to delete
	 * @throws SQLException
	 *             if some exception occurs
	 */
	private void deleteSessionContract(final UUID uuid) throws SQLException {

		final SessionContract sessContract = this.getSessionContractByID(uuid);
		for (final SessionInterface sessIface : sessContract.getSessionInterfaces().values()) {
			this.deleteSessionInterface(sessIface.getId());
		}

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.DELETE_SESSIONCONTRACT.getSqlStatement());
			stmt.setObject(1, uuid);
			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + stmt);
			}
			stmt.executeUpdate();

		} catch (final Exception e) {
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

	/**
	 * Delete accessedImplementation
	 * @param sessionID
	 *            ID of object to delete
	 */
	public void deleteSession(final SessionID sessionID) {
		synchronized (dataSource) {

			Connection conn = null;
			try {

				final Session sessInfo = this.getSessionByID(sessionID);
				for (final SessionContract sessContract : sessInfo.getSessionContracts().values()) {
					this.deleteSessionContract(sessContract.getId());
				}
				for (final SessionDataContract sessDataContract : sessInfo.getSessionDataContracts().values()) {
					this.deleteSessionDataContract(sessDataContract.getId());
				}

				conn = dataSource.getConnection();
				final PreparedStatement stmt = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.DELETE_SESSIONINFO.getSqlStatement());
				stmt.setObject(1, sessionID.getId());
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + stmt);
				}
				stmt.executeUpdate();

			} catch (final Exception e) {
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

	/**
	 * Check if session exists
	 * @param sessionID
	 *            session iD
	 * @return TRUE if exists.
	 */
	public boolean existsObjectByID(final SessionID sessionID) {
		synchronized (dataSource) {
			return this.getSessionByID(sessionID) != null;
		}
	}

	/**
	 * Store external session.
	 * @param infoSession
	 *            info of the external session to be stored
	 */
	public void storeExt(final Session infoSession) {
		synchronized (dataSource) {

			try (Connection conn = dataSource.getConnection()) {
				try (PreparedStatement insertStatement = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.INSERT_EXT_SESSION.getSqlStatement())) {
					insertStatement.setObject(1, infoSession.getExtDataClayID().getId());
					insertStatement.setObject(2, infoSession.getDataClayID().getId());
					insertStatement.setObject(3, infoSession.getAccountID().getId());
					insertStatement.setString(4, infoSession.getLanguage().name());
					insertStatement.setDate(5, new java.sql.Date(infoSession.getEndDate().getTimeInMillis()));
					insertStatement.executeUpdate();
				}
			} catch (final Exception e) {
				// Ignore
			}
		}
	}

	/**
	 * Get info about an external session
	 * @param extDataClayID
	 *            id of the external dataClay instance
	 * @return session info. null if there is no session for the given dataClay instance.
	 */
	public Session getExtSession(final DataClayInstanceID extDataClayID) {
		synchronized (dataSource) {

			try (Connection conn = dataSource.getConnection()) {
				try (PreparedStatement queryStatement = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.SELECT_EXT_SESSION.getSqlStatement())) {
					try (ResultSet rs = queryStatement.executeQuery()) {
						final SessionID sessionID = new SessionID((UUID) rs.getObject("sessionID"));
						final AccountID accountID = new AccountID((UUID) rs.getObject("accountID"));
						final Langs language = Langs.valueOf(rs.getString("language"));
						final Calendar endDate = Calendar.getInstance();
						endDate.setTime(rs.getDate("endDate"));
						final Session result = new Session();
						result.setDataClayID(sessionID);
						result.setAccountID(accountID);
						result.setExtDataClayID(extDataClayID);
						result.setLanguage(language);
						result.setEndDate(endDate);
						return result;
					}
				}
			} catch (final Exception e) {
				return null;
			}
		}
	}

	public void deleteExtSession(final DataClayInstanceID dataClayID) {
		synchronized (dataSource) {

			try (Connection conn = dataSource.getConnection()) {
				try (PreparedStatement deleteStatement = conn.prepareStatement(SessionManagerSQLStatements.SqlStatements.DELETE_EXT_SESSION.getSqlStatement())) {
					deleteStatement.setObject(1, dataClayID.getId());
					deleteStatement.executeUpdate();
				}
			} catch (final Exception ex) {
				// ignore
			}
		}
	}
}
