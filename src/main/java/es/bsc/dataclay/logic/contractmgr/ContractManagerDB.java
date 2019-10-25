
package es.bsc.dataclay.logic.contractmgr;

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

import es.bsc.dataclay.exceptions.dbhandler.DbObjectAlreadyExistException;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectNotExistException;
import es.bsc.dataclay.logic.interfacemgr.InterfaceManagerDB;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.management.contractmgr.Contract;
import es.bsc.dataclay.util.management.contractmgr.InterfaceInContract;
import es.bsc.dataclay.util.management.contractmgr.OpImplementations;
import es.bsc.dataclay.util.management.interfacemgr.Interface;

/**
 * Data base connection.
 */
public final class ContractManagerDB {

	/** Logger. */
	private Logger logger;

	/** Indicates if debug is enabled. */
	private static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** SingleConnection. */
	private final BasicDataSource dataSource;

	/** Interface Manager DB needed for InterfaceInContract. TODO: this should be not together. */
	private final InterfaceManagerDB ifaceManagerDB;

	/**
	 * MetaDataServiceDB constructor.
	 * 
	 * @param managerName
	 *            Name of the LM service managing.
	 */
	public ContractManagerDB(final BasicDataSource dataSource) {
		this.dataSource = dataSource;
		ifaceManagerDB = new InterfaceManagerDB(dataSource);
		if (DEBUG_ENABLED) {
			logger = LogManager.getLogger("LMDB");
		}
	}

	/**
	 * Create tables of MDS.
	 */
	public void createTables() {

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			for (final ContractManagerSQLStatements.SqlStatements stmt : ContractManagerSQLStatements.SqlStatements.values()) {
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

	/**
	 * Delete the tables of MDS. Just the other way around of createTables --much simpler.
	 */
	public void dropTables() {

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			for (final ContractManagerSQLStatements.SqlStatements stmt : ContractManagerSQLStatements.SqlStatements.values()) {
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

	/**
	 * Store opImplementations into database
	 * @param opImplementations
	 *            opImplementations
	 * @return uuid of stored object
	 */
	public UUID store(final OpImplementations opImplementations) {
		Connection conn = null;
		final UUID uuid = UUID.randomUUID();
		opImplementations.setId(uuid);
		try {
			conn = dataSource.getConnection();
			final PreparedStatement insertStatement = conn.prepareStatement(ContractManagerSQLStatements.SqlStatements.INSERT_OPIMPLEMENTATIONS.getSqlStatement());
			insertStatement.setObject(1, uuid);
			// CHECKSTYLE:OFF
			insertStatement.setString(2, opImplementations.getOperationSignature());
			insertStatement.setInt(3, opImplementations.getNumLocalImpl());
			insertStatement.setInt(4, opImplementations.getNumRemoteImpl());
			insertStatement.setObject(5, opImplementations.getLocalImplementationID().getId());
			insertStatement.setObject(6, opImplementations.getRemoteImplementationID().getId());
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + insertStatement);
			}
			// CHECKSTYLE:ON
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
	 * Store ifaceInContract into database
	 * @param ifaceInContract
	 *            ifaceInContract
	 * @return uuid of stored object
	 */
	public UUID store(final InterfaceInContract ifaceInContract) {

		final UUID uuid = UUID.randomUUID();
		ifaceInContract.setId(uuid);

		UUID[] implsSpecs = null;
		if (ifaceInContract.getImplementationsSpecPerOperation() != null) {
			implsSpecs = new UUID[ifaceInContract.getImplementationsSpecPerOperation().size()];
			int i = 0;
			for (final OpImplementations opImpls : ifaceInContract.getImplementationsSpecPerOperation()) {
				implsSpecs[i] = this.store(opImpls);
				i++;
			}
		}

		UUID[] accessibleImplementationsUUIDsKeys = null;
		UUID[] accessibleImplementationsUUIDsValues = null;
		if (ifaceInContract.getAccessibleImplementations() != null) {
			accessibleImplementationsUUIDsKeys = new UUID[ifaceInContract.getAccessibleImplementations().size()];
			accessibleImplementationsUUIDsValues = new UUID[ifaceInContract.getAccessibleImplementations().size()];
			int i = 0;
			for (final Entry<OperationID, OpImplementations> entry : ifaceInContract.getAccessibleImplementations().entrySet()) {
				accessibleImplementationsUUIDsKeys[i] = entry.getKey().getId();
				accessibleImplementationsUUIDsValues[i] = this.store(entry.getValue());
				i++;
			}
		}

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement insertStatement = conn.prepareStatement(ContractManagerSQLStatements.SqlStatements.INSERT_IFACEINCONTRACT.getSqlStatement());
			insertStatement.setObject(1, uuid);
			// CHECKSTYLE:OFF
			insertStatement.setObject(2, ifaceInContract.getInterfaceID().getId());

			if (implsSpecs != null) {
				final Array tArray = insertStatement.getConnection().createArrayOf("uuid", implsSpecs);
				insertStatement.setArray(3, tArray);
			} else {
				insertStatement.setArray(3, null);
			}

			if (accessibleImplementationsUUIDsKeys != null) {
				final Array tArray = insertStatement.getConnection().createArrayOf("uuid", accessibleImplementationsUUIDsKeys);
				insertStatement.setArray(4, tArray);
			} else {
				insertStatement.setArray(4, null);
			}

			if (accessibleImplementationsUUIDsValues != null) {
				final Array tArray = insertStatement.getConnection().createArrayOf("uuid", accessibleImplementationsUUIDsValues);
				insertStatement.setArray(5, tArray);
			} else {
				insertStatement.setArray(5, null);
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
	 * Store contract into database
	 * @param contract
	 *            contract
	 */
	public void store(final Contract contract) {

		UUID[] ifaces = null;
		UUID[] ifacesKeys = null;
		if (contract.getInterfacesInContract() != null) {
			ifaces = new UUID[contract.getInterfacesInContract().size()];
			ifacesKeys = new UUID[contract.getInterfacesInContract().size()];
			int i = 0;
			for (final InterfaceInContract ifaceInContract : contract.getInterfacesInContract().values()) {
				ifaces[i] = this.store(ifaceInContract);
				ifacesKeys[i] = ifaceInContract.getIface().getDataClayID().getId();
				i++;
			}
		}

		UUID[] accountsUUIDs = null;
		if (contract.getApplicantsAccountsIDs() != null) {
			accountsUUIDs = new UUID[contract.getApplicantsAccountsIDs().size()];
			int i = 0;
			for (final AccountID accountID : contract.getApplicantsAccountsIDs()) {
				accountsUUIDs[i] = accountID.getId();
				i++;
			}
		}

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement insertStatement = conn.prepareStatement(ContractManagerSQLStatements.SqlStatements.INSERT_CONTRACT.getSqlStatement());
			insertStatement.setObject(1, contract.getDataClayID().getId());
			// CHECKSTYLE:OFF
			insertStatement.setString(2, contract.getNamespace());

			insertStatement.setTimestamp(3, new java.sql.Timestamp(contract.getBeginDate().getTimeInMillis()));
			insertStatement.setTimestamp(4, new java.sql.Timestamp(contract.getEndDate().getTimeInMillis()));
			insertStatement.setBoolean(5, contract.isPublicAvailable());
			insertStatement.setObject(6, contract.getProviderAccountID().getId());
			insertStatement.setObject(7, contract.getNamespaceID().getId());

			if (accountsUUIDs != null) {
				final Array tArray = insertStatement.getConnection().createArrayOf("uuid", accountsUUIDs);
				insertStatement.setArray(8, tArray);
			} else {
				insertStatement.setArray(8, null);
			}

			if (ifacesKeys != null) {
				final Array tArray = insertStatement.getConnection().createArrayOf("uuid", ifacesKeys);
				insertStatement.setArray(9, tArray);
			} else {
				insertStatement.setArray(9, null);
			}

			if (ifaces != null) {
				final Array tArray = insertStatement.getConnection().createArrayOf("uuid", ifaces);
				insertStatement.setArray(10, tArray);
			} else {
				insertStatement.setArray(10, null);
			}

			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + insertStatement);
			}
			insertStatement.executeUpdate();

		} catch (final Exception e) {
			e.printStackTrace();
			throw new DbObjectAlreadyExistException(contract.getDataClayID());
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
	 * Deserialize
	 * @param rs
	 *            Result set
	 * @return OpImplementations
	 */
	private OpImplementations deserializeOpImplementations(
			final ResultSet rs) {
		OpImplementations result = null;
		try {
			// CHECKSTYLE:OFF
			final UUID id = (UUID)rs.getObject("id");
			final String operationSignature = rs.getString("operationSignature");
			final int numLocalImpl = rs.getInt("numLocalImpl");
			final int numRemoteImpl = rs.getInt("numRemoteImpl");

			final ImplementationID localImplementationID = new ImplementationID((UUID)rs.getObject("localImplementationID"));
			final ImplementationID remoteImplementationID = new ImplementationID((UUID)rs.getObject("remoteImplementationID"));

			result = new OpImplementations();
			result.setId(id);
			result.setLocalImplementationID(localImplementationID);
			result.setNumLocalImpl(numLocalImpl);
			result.setNumRemoteImpl(numRemoteImpl);
			result.setOperationSignature(operationSignature);
			result.setRemoteImplementationID(remoteImplementationID);

		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Deserialize
	 * @param rs
	 *            Result set
	 * @return InterfaceInContract
	 */
	private InterfaceInContract deserializeInterfaceInContract(
			final ResultSet rs) {
		InterfaceInContract result = null;
		try {
			// CHECKSTYLE:OFF
			final UUID id = (UUID)rs.getObject("id");
			final UUID interfaceUUID = (UUID)rs.getObject("interface");
			final InterfaceID interfaceID = new InterfaceID(interfaceUUID);
			final Interface iface = ifaceManagerDB.getInterfaceByID(interfaceID);

			final Array array = rs.getArray("implementationsSpecPerOperation");
			UUID[] implementationsSpecPerOperationUUIDs = null;
			final Set<OpImplementations> implementationsSpecPerOperation = new HashSet<>();
			if (array != null) {
				implementationsSpecPerOperationUUIDs = (UUID[]) array.getArray();
				for (final UUID implSpecUUID : implementationsSpecPerOperationUUIDs) {
					implementationsSpecPerOperation.add(this.getOpImplementationsByID(implSpecUUID));
				}
			}

			final Array array1 = rs.getArray("accessibleImplementationsKeys");
			final Array array2 = rs.getArray("accessibleImplementationsValues");
			UUID[] accessibleImplementationsKeysUUIDs = null;
			UUID[] accessibleImplementationsValuesUUIDs = null;
			final Map<OperationID, OpImplementations> accessibleImplementations = new HashMap<>();
			if (array1 != null) {
				accessibleImplementationsKeysUUIDs = (UUID[]) array1.getArray();
				accessibleImplementationsValuesUUIDs = (UUID[]) array2.getArray();
				for (int i = 0; i < accessibleImplementationsKeysUUIDs.length; ++i) {
					final OperationID opID = new OperationID(accessibleImplementationsKeysUUIDs[i]);
					final OpImplementations opImpl = this.getOpImplementationsByID(accessibleImplementationsValuesUUIDs[i]);
					accessibleImplementations.put(opID, opImpl);
				}
			}

			result = new InterfaceInContract();
			result.setAccessibleImplementations(accessibleImplementations);
			result.setId(id);
			result.setIface(iface);
			result.setImplementationsSpecPerOperation(implementationsSpecPerOperation);
			result.setInterfaceID(interfaceID);

		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Deserialize
	 * @param rs
	 *            Result set
	 * @return Contract
	 */
	private Contract deserializeContract(
			final ResultSet rs) {
		Contract result = null;
		try {
			// CHECKSTYLE:OFF
			final ContractID contractID = new ContractID((UUID)rs.getObject("id"));
			final String namespace = rs.getString("namespace");
			final Calendar beginDate = Calendar.getInstance();
			beginDate.setTimeInMillis(rs.getTimestamp("beginDate").getTime());
			final Calendar endDate = Calendar.getInstance();
			endDate.setTimeInMillis(rs.getTimestamp("endDate").getTime());
			final boolean publicAvailable = rs.getBoolean("publicAvailable");
			final AccountID providerAccountID = new AccountID((UUID)rs.getObject("providerAccountID"));
			final NamespaceID namespaceID = new NamespaceID((UUID)rs.getObject("namespaceID"));

			final UUID[] applicantsAccountsUUIDs = (UUID[]) rs.getArray("applicantsAccountsIDs").getArray();
			final Set<AccountID> applicantsAccountsIDs = new HashSet<>();
			for (final UUID uuid : applicantsAccountsUUIDs) {
				applicantsAccountsIDs.add(new AccountID(uuid));
			}

			final Map<InterfaceID, InterfaceInContract> interfacesInContract = new HashMap<>();
			final List<InterfaceInContract> interfacesInContractSpecs = new ArrayList<>();

			final UUID[] interfacesInContractUUIDs = (UUID[]) rs.getArray("interfacesInContractValues").getArray();
			for (final UUID uuid : interfacesInContractUUIDs) {
				final InterfaceInContract ifaceInContract = this.getInterfaceInContractByID(uuid);
				interfacesInContractSpecs.add(ifaceInContract);
				interfacesInContract.put(ifaceInContract.getInterfaceID(), ifaceInContract);
			}

			result = new Contract();
			result.setApplicantsAccountsIDs(applicantsAccountsIDs);
			result.setBeginDate(beginDate);
			result.setDataClayID(contractID);
			result.setEndDate(endDate);
			result.setInterfacesInContract(interfacesInContract);
			result.setInterfacesInContractSpecs(interfacesInContractSpecs);
			result.setNamespace(namespace);
			result.setNamespaceID(namespaceID);
			result.setProviderAccountID(providerAccountID);
			result.setPublicAvailable(publicAvailable);

		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Get by ID
	 * @param uuid
	 *            UUID of object
	 * @return The object
	 * @throws SQLException
	 *             if not found
	 */
	private OpImplementations getOpImplementationsByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		OpImplementations result = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ContractManagerSQLStatements.SqlStatements.SELECT_OPIMPLEMENTATIONS.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				result = deserializeOpImplementations(rs);

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

		return result;
	}

	/**
	 * Get by ID
	 * @param uuid
	 *            UUID of object
	 * @return The object
	 * @throws SQLException
	 *             if not found
	 */
	private InterfaceInContract getInterfaceInContractByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		InterfaceInContract result = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ContractManagerSQLStatements.SqlStatements.SELECT_IFACEINCONTRACT.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				result = deserializeInterfaceInContract(rs);

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

		return result;
	}

	/**
	 * Get by ID
	 * @param contractID
	 *            ID of object
	 * @return The object
	 * @throws SQLException
	 *             if not found
	 */
	public Contract getContractByID(final ContractID contractID) {
		ResultSet rs = null;
		Contract result = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ContractManagerSQLStatements.SqlStatements.SELECT_CONTRACT.getSqlStatement());

			selectStatement.setObject(1, contractID.getId());
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				result = deserializeContract(rs);

			}
		} catch (final SQLException e) {
			throw new DbObjectNotExistException(contractID);
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

	/**
	 * Delete by ID
	 * @param uuid
	 *            UUID of object
	 * 
	 */
	private void deleteOpImplementationsByID(final UUID uuid) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ContractManagerSQLStatements.SqlStatements.DELETE_OPIMPLEMENTATIONS.getSqlStatement());

			selectStatement.setObject(1, uuid);
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

	/**
	 * Delete by ID
	 * @param uuid
	 *            UUID of object
	 */
	private void deleteInterfaceInContractByID(final UUID uuid) {

		final InterfaceInContract ifaceInContract;
		try {
			ifaceInContract = this.getInterfaceInContractByID(uuid);
			for (final OpImplementations opImpl : ifaceInContract.getAccessibleImplementations().values()) {
				this.deleteOpImplementationsByID(opImpl.getId());
			}
			for (final OpImplementations opImpl : ifaceInContract.getImplementationsSpecPerOperation()) {
				this.deleteOpImplementationsByID(opImpl.getId());
			}

		} catch (final SQLException e1) {
			e1.printStackTrace();
		}

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ContractManagerSQLStatements.SqlStatements.DELETE_IFACEINCONTRACT.getSqlStatement());

			selectStatement.setObject(1, uuid);
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

	/**
	 * Delete by ID
	 * @param contractID
	 *            ID of object
	 */
	public void deleteContractByID(final ContractID contractID) {

		final Contract contract = this.getContractByID(contractID);
		for (final InterfaceInContract ifaceInContract : contract.getInterfacesInContractSpecs()) {
			this.deleteInterfaceInContractByID(ifaceInContract.getId());
		}

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ContractManagerSQLStatements.SqlStatements.DELETE_CONTRACT.getSqlStatement());

			selectStatement.setObject(1, contractID.getId());
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

	/**
	 * Update by ID
	 * @param contractID
	 *            ID of object
	 */
	public void updateContractsAddApplicant(final ContractID contractID, final AccountID applicantAccountID) {

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement stmt = conn.prepareStatement(ContractManagerSQLStatements.SqlStatements.UPDATE_CONTRACT_ADD_APPLICANT.getSqlStatement());
			stmt.setObject(1, applicantAccountID.getId());
			stmt.setObject(2, contractID.getId());
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

	/**
	 * Get contracts of namespace
	 * @param namespaceID
	 *            contracts ID
	 * @return The Contracts
	 */
	public List<Contract> getContractsOfNamespace(final NamespaceID namespaceID) {
		ResultSet rs = null;
		final List<Contract> result = new ArrayList<>();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ContractManagerSQLStatements.SqlStatements.SELECT_ALL_CONTRACTS_OF_NAMESPACE.getSqlStatement());
			selectStatement.setObject(1, namespaceID.getId());
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			while (rs.next()) {
				result.add(deserializeContract(rs));
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

	/**
	 * Get contracts containing interface
	 * @param interfaceID
	 *            interface ID
	 * @return The Contracts
	 */
	public List<Contract> getContractsContainingInterface(final InterfaceID interfaceID) {
		ResultSet rs = null;
		final List<Contract> result = new ArrayList<>();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ContractManagerSQLStatements.SqlStatements.SELECT_ALL_CONTRACTS_WITH_INTERFACE.getSqlStatement());

			final Array tArray = selectStatement.getConnection().createArrayOf("uuid", new UUID[] { interfaceID.getId() });
			selectStatement.setArray(1, tArray);

			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			while (rs.next()) {
				result.add(deserializeContract(rs));
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

	/**
	 * Get contracts containing applicant
	 * @param applicantAccountID
	 *            Account ID
	 * @return The Contracts
	 */
	public List<Contract> getContractsWithApplicant(final AccountID applicantAccountID) {
		ResultSet rs = null;
		final List<Contract> result = new ArrayList<>();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ContractManagerSQLStatements.SqlStatements.SELECT_ALL_CONTRACTS_WITH_APPLICANT.getSqlStatement());
			final Array tArray = selectStatement.getConnection().createArrayOf("uuid", new UUID[] { applicantAccountID.getId() });
			selectStatement.setArray(1, tArray);

			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			while (rs.next()) {
				result.add(deserializeContract(rs));
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

	/**
	 * Get contracts containing provider
	 * @param providerAccountID
	 *            Account ID
	 * @return The Contracts
	 */
	public List<Contract> getContractsWithProvider(final AccountID providerAccountID) {
		ResultSet rs = null;
		final List<Contract> result = new ArrayList<>();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ContractManagerSQLStatements.SqlStatements.SELECT_ALL_CONTRACTS_WITH_PROVIDER.getSqlStatement());
			selectStatement.setObject(1, providerAccountID.getId());
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			while (rs.next()) {
				result.add(deserializeContract(rs));
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

	/**
	 * Get OpImplementations with implementation id
	 * @param implementationID
	 *            ID of implementation
	 * @return The OpImplementations
	 */
	public List<OpImplementations> getOpImplementationsWithImpl(final ImplementationID implementationID) {
		ResultSet rs = null;
		final List<OpImplementations> result = new ArrayList<>();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(ContractManagerSQLStatements.SqlStatements.SELECT_ALL_OPIMPLS_WITH_IMPL.getSqlStatement());
			selectStatement.setObject(1, implementationID.getId());
			selectStatement.setObject(2, implementationID.getId());
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			while (rs.next()) {
				result.add(deserializeOpImplementations(rs));
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

	/**
	 * Get contracts containing applicant and namespace
	 * @param applicantAccountID
	 *            Account ID
	 * @param namespaceID
	 *            ID of namespace
	 * @return The Contracts
	 */
	public List<Contract> getContractsWithApplicantAndNamespace(final AccountID applicantAccountID,
			final NamespaceID namespaceID) {
		ResultSet rs = null;
		final List<Contract> result = new ArrayList<>();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn
					.prepareStatement(ContractManagerSQLStatements.SqlStatements.SELECT_ALL_CONTRACTS_WITH_APPLICANT_AND_NAMESPACE.getSqlStatement());
			final Array tArray = selectStatement.getConnection().createArrayOf("uuid", new UUID[] { applicantAccountID.getId() });
			selectStatement.setArray(1, tArray);
			selectStatement.setObject(2, namespaceID.getId());
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			while (rs.next()) {
				result.add(deserializeContract(rs));
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
