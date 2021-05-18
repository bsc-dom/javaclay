
package es.bsc.dataclay.logic.namespacemgr;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.management.namespacemgr.ImportedInterface;
import es.bsc.dataclay.util.management.namespacemgr.Namespace;
import es.bsc.dataclay.util.structs.Tuple;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;

/**
 * Data base connection.
 */
public final class NamespaceManagerDB {

	/** Logger. */
	private Logger logger;

	/** Indicates if debug is enabled. */
	private static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** SingleConnection. */
	private final SQLiteDataSource dataSource;

	/**
	 * MetaDataServiceDB constructor.
	 *
	 */
	public NamespaceManagerDB(final SQLiteDataSource dataSource) {
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
				for (final NamespaceManagerSQLStatements.SqlStatements stmt : NamespaceManagerSQLStatements.SqlStatements.values()) {
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
				for (final NamespaceManagerSQLStatements.SqlStatements stmt : NamespaceManagerSQLStatements.SqlStatements.values()) {
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
	 * Store ImportedInterface into database
	 * @param importedIface
	 *            importedIface
	 * @return UUID of stored object
	 */
	private UUID store(final ImportedInterface importedIface) {

		final UUID uuid = UUID.randomUUID();
		importedIface.setId(uuid);

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement insertStatement = conn.prepareStatement(NamespaceManagerSQLStatements.SqlStatements.INSERT_IMPORTEDIFACE.getSqlStatement());
			insertStatement.setObject(1, uuid);
			// CHECKSTYLE:OFF
			insertStatement.setString(2, importedIface.getImportedClassName());
			insertStatement.setObject(3, importedIface.getInterfaceID().getId());
			insertStatement.setObject(4, importedIface.getContractID().getId());
			insertStatement.setObject(5, importedIface.getClassOfImportID().getId());
			insertStatement.setObject(6, importedIface.getNamespaceIDofClass().getId());

			final UUID[] propertiesUsingImports = new UUID[importedIface.getPropertiesUsingImports().size()];
			int i = 0;
			for (final PropertyID propID : importedIface.getPropertiesUsingImports()) {
				propertiesUsingImports[i] = propID.getId();
				i++;
			}
			Array tArray = insertStatement.getConnection().createArrayOf("uuid", propertiesUsingImports);
			insertStatement.setArray(7, tArray);

			final UUID[] operationsUsingImports = new UUID[importedIface.getOperationsUsingImports().size()];
			i = 0;
			for (final OperationID opID : importedIface.getOperationsUsingImports()) {
				operationsUsingImports[i] = opID.getId();
				i++;
			}
			tArray = insertStatement.getConnection().createArrayOf("uuid", operationsUsingImports);
			insertStatement.setArray(8, tArray);

			final UUID[] implementationsUsingImports = new UUID[importedIface.getImplementationsUsingImports().size()];
			i = 0;
			for (final ImplementationID implID : importedIface.getImplementationsUsingImports()) {
				implementationsUsingImports[i] = implID.getId();
				i++;
			}
			tArray = insertStatement.getConnection().createArrayOf("uuid", implementationsUsingImports);
			insertStatement.setArray(9, tArray);

			final UUID[] subClassesOfImport = new UUID[importedIface.getSubClassesOfImport().size()];
			i = 0;
			for (final MetaClassID classID : importedIface.getSubClassesOfImport()) {
				subClassesOfImport[i] = classID.getId();
				i++;
			}
			tArray = insertStatement.getConnection().createArrayOf("uuid", subClassesOfImport);
			insertStatement.setArray(10, tArray);

			// CHECKSTYLE:ON
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + insertStatement);
			}
			insertStatement.executeUpdate();
			insertStatement.close();
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
	 * Store namespace into database
	 * @param namespace
	 *            namespace
	 */
	public void store(final Namespace namespace) {
		synchronized (dataSource) {

			UUID[] importedIfaces = null;
			UUID[] importedIfacesKeysFirst = null;
			UUID[] importedIfacesKeysSecond = null;
			if (namespace.getImportedInterfaces() != null) {
				importedIfaces = new UUID[namespace.getImportedInterfaces().size()];
				importedIfacesKeysFirst = new UUID[importedIfaces.length];
				importedIfacesKeysSecond = new UUID[importedIfaces.length];

				int i = 0;
				for (final Entry<Tuple<InterfaceID, ContractID>, ImportedInterface> curEntry : namespace.getImportedInterfaces().entrySet()) {
					final InterfaceID ifaceID = curEntry.getKey().getFirst();
					final ContractID contractID = curEntry.getKey().getSecond();
					final ImportedInterface importedIface = curEntry.getValue();
					final UUID importedIfaceUUID = this.store(importedIface);
					importedIfaces[i] = importedIfaceUUID;
					importedIfacesKeysFirst[i] = ifaceID.getId();
					importedIfacesKeysSecond[i] = contractID.getId();
					i++;
				}
			}

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(NamespaceManagerSQLStatements.SqlStatements.INSERT_NAMESPACE.getSqlStatement());
				insertStatement.setObject(1, namespace.getDataClayID().getId());
				// CHECKSTYLE:OFF
				insertStatement.setString(2, namespace.getProviderAccountName());
				insertStatement.setString(3, namespace.getName());
				insertStatement.setObject(4, namespace.getProviderAccountID().getId());

				if (importedIfaces != null) {
					Array tArray = insertStatement.getConnection().createArrayOf("uuid", importedIfacesKeysFirst);
					insertStatement.setArray(5, tArray);
					tArray = insertStatement.getConnection().createArrayOf("uuid", importedIfacesKeysSecond);
					insertStatement.setArray(6, tArray);
					tArray = insertStatement.getConnection().createArrayOf("uuid", importedIfaces);
					insertStatement.setArray(7, tArray);
				} else {
					insertStatement.setArray(5, null);
					insertStatement.setArray(6, null);
					insertStatement.setArray(7, null);

				}

				insertStatement.setString(8, namespace.getLanguage().name());

				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();
				insertStatement.close();
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
	 * @return ImportedInterface
	 */
	private ImportedInterface deserializeImportedInterface(
			final ResultSet rs) {
		ImportedInterface result = null;
		try {
			// CHECKSTYLE:OFF
			final UUID uuid = (UUID)rs.getObject("id");
			final String importedClassName = rs.getString("importedClassName");
			final InterfaceID interfaceID = new InterfaceID((UUID)rs.getObject("interfaceID"));
			final ContractID contractID = new ContractID((UUID)rs.getObject("contractID"));
			final MetaClassID classOfImportID = new MetaClassID((UUID)rs.getObject("classOfImportID"));
			final NamespaceID namespaceIDofClass = new NamespaceID((UUID)rs.getObject("namespaceIDofClass"));
			final UUID[] propertiesUsingImportsUUIDs = (UUID[]) rs.getArray("propertiesUsingImports").getArray();

			final HashSet<PropertyID> propertiesUsingImports = new HashSet<>();
			for (final UUID theuuid : propertiesUsingImportsUUIDs) {
				propertiesUsingImports.add(new PropertyID(theuuid));
			}

			final UUID[] operationsUsingImportsUUIDs = (UUID[]) rs.getArray("operationsUsingImports").getArray();
			final HashSet<OperationID> operationsUsingImports = new HashSet<>();
			for (final UUID theuuid : operationsUsingImportsUUIDs) {
				operationsUsingImports.add(new OperationID(theuuid));
			}

			final UUID[] implementationsUsingImportsUUIDs = (UUID[]) rs.getArray("implementationsUsingImports").getArray();
			final HashSet<ImplementationID> implementationsUsingImports = new HashSet<>();
			for (final UUID theuuid : implementationsUsingImportsUUIDs) {
				implementationsUsingImports.add(new ImplementationID(theuuid));
			}

			final UUID[] subClassesOfImportUUIDs = (UUID[]) rs.getArray("subClassesOfImport").getArray();
			final HashSet<MetaClassID> subClassesOfImport = new HashSet<>();
			for (final UUID theuuid : subClassesOfImportUUIDs) {
				subClassesOfImport.add(new MetaClassID(theuuid));
			}

			result = new ImportedInterface();
			result.setClassOfImportID(classOfImportID);
			result.setContractID(contractID);
			result.setNamespaceIDofClass(namespaceIDofClass);
			result.setId(uuid);
			result.setImplementationsUsingImports(implementationsUsingImports);
			result.setImportedClassName(importedClassName);
			result.setInterfaceID(interfaceID);
			result.setOperationsUsingImports(operationsUsingImports);
			result.setPropertiesUsingImports(propertiesUsingImports);
			result.setSubClassesOfImport(subClassesOfImport);

		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Deserialize
	 * @param rs
	 *            Result set
	 * @return Namespace
	 */
	private Namespace deserializeNamespace(
			final ResultSet rs) {
		Namespace result = null;
		try {
			// CHECKSTYLE:OFF
			final NamespaceID namespaceID = new NamespaceID((UUID)rs.getObject("id"));
			final String providerAccountName = rs.getString("providerAccountName");
			final String name = rs.getString("name");
			final AccountID responsibleAccountID = new AccountID((UUID)rs.getObject("responsible"));

			final Map<Tuple<InterfaceID, ContractID>, ImportedInterface> importedInterfaces = new HashMap<>();

			final Array arr = rs.getArray("importedInterfacesFirstKey");
			if (arr != null) {
				final UUID[] importedInterfacesFirstKey = (UUID[]) arr.getArray();
				final UUID[] importedInterfacesSecondKey = (UUID[]) rs.getArray("importedInterfacesSecondKey").getArray();
				final UUID[] importedInterfacesValues = (UUID[]) rs.getArray("importedInterfacesValues").getArray();
				for (int i = 0; i < importedInterfacesFirstKey.length; ++i) {
					final InterfaceID ifaceID = new InterfaceID(importedInterfacesFirstKey[i]);
					final ContractID contractID = new ContractID(importedInterfacesSecondKey[i]);
					final ImportedInterface importedInterface = this.getImportedInterfaceByID(importedInterfacesValues[i]);
					importedInterfaces.put(new Tuple<>(ifaceID, contractID), importedInterface);

				}
			}

			final Langs language = Langs.valueOf(rs.getString("language"));

			result = new Namespace(name, providerAccountName, language);
			result.setDataClayID(namespaceID);
			result.setImportedInterfaces(importedInterfaces);
			result.setProviderAccountID(responsibleAccountID);

		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Get by ID
	 * @param uuid
	 *            ID of object
	 * @return The object
	 * @throws SQLException
	 *             if not found
	 */
	private ImportedInterface getImportedInterfaceByID(final UUID uuid) throws SQLException {
		ResultSet rs = null;
		ImportedInterface result = null;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(NamespaceManagerSQLStatements.SqlStatements.SELECT_IMPORTEDIFACE.getSqlStatement());

			selectStatement.setObject(1, uuid);
			if (DEBUG_ENABLED) {
				logger.debug("[==DB==] Executing " + selectStatement);
			}
			rs = selectStatement.executeQuery();
			if (rs.next()) {
				result = deserializeImportedInterface(rs);
			}
			selectStatement.close();
		} catch (final SQLException e) {
			throw e;
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

	/**
	 * Get by ID
	 * @param namespaceID
	 *            ID of object
	 * @return The object
	 * @throws SQLException
	 *             if not found
	 */
	public Namespace getNamespaceByID(final NamespaceID namespaceID) {
		synchronized (dataSource) {

			ResultSet rs = null;
			Namespace result = null;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(NamespaceManagerSQLStatements.SqlStatements.SELECT_NAMESPACE.getSqlStatement());

				selectStatement.setObject(1, namespaceID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				if (rs.next()) {
					result = deserializeNamespace(rs);

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
	 * Delete by ID
	 * @param uuid
	 *            ID of object
	 */
	private void deleteImportedInterfaceByID(final UUID uuid) {

		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			final PreparedStatement selectStatement = conn.prepareStatement(NamespaceManagerSQLStatements.SqlStatements.DELETE_IMPORTEDIFACE.getSqlStatement());
			selectStatement.setObject(1, uuid);
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

	/**
	 * Delete by ID
	 * @param namespaceID
	 *            ID of object
	 */
	public void deleteNamespaceByID(final NamespaceID namespaceID) {
		synchronized (dataSource) {

			final Namespace namespace = this.getNamespaceByID(namespaceID);
			for (final ImportedInterface importedIface : namespace.getImportedInterfaces().values()) {
				this.deleteImportedInterfaceByID(importedIface.getId());
			}

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(NamespaceManagerSQLStatements.SqlStatements.DELETE_NAMESPACE.getSqlStatement());
				selectStatement.setObject(1, namespaceID.getId());
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
	 * Get by name
	 * @return The object
	 * @throws SQLException
	 *             if not found
	 */
	public Set<String> getNamespacesNames() {
		synchronized (dataSource) {

			ResultSet rs = null;
			final Set<String> result = new HashSet<>();
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(NamespaceManagerSQLStatements.SqlStatements.SELECT_NAMESPACES_NAMES.getSqlStatement());

				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				while (rs.next()) {
					result.add(rs.getString(1));
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
	 * Get by name
	 * @param name
	 *            the name
	 * @return The object
	 * @throws SQLException
	 *             if not found
	 */
	public Namespace getNamespaceByName(final String name) {
		synchronized (dataSource) {

			ResultSet rs = null;
			Namespace result = null;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(NamespaceManagerSQLStatements.SqlStatements.SELECT_NAMESPACE_BY_NAME.getSqlStatement());

				selectStatement.setString(1, name);
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				if (rs.next()) {
					result = deserializeNamespace(rs);

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
	 * Get by name and id
	 * @param name
	 *            the name
	 * @param namespaceID
	 *            ID of the namespace
	 * @return The object
	 * @throws SQLException
	 *             if not found
	 */
	public Namespace getNamespaceByNameAndID(final String name, final NamespaceID namespaceID) {
		synchronized (dataSource) {

			ResultSet rs = null;
			Namespace result = null;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(NamespaceManagerSQLStatements.SqlStatements.SELECT_ALL_NAMESPACES_OF_ACCOUNT_AND_ID.getSqlStatement());

				selectStatement.setString(1, name);
				selectStatement.setObject(2, namespaceID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				if (rs.next()) {
					result = deserializeNamespace(rs);

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
	 * Update by ID
	 * @param namespaceID
	 *            ID of object
	 * @param importedIface
	 *            Iface added
	 */
	public void updateNamespaceAddImport(final NamespaceID namespaceID, final ImportedInterface importedIface) {
		synchronized (dataSource) {

			final UUID importedIfaceUUID = this.store(importedIface);
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement stmt = conn.prepareStatement(NamespaceManagerSQLStatements.SqlStatements.UPDATE_NAMESPACE_ADD_IMPORTEDIFACE.getSqlStatement());

				stmt.setObject(1, importedIface.getInterfaceID().getId());
				stmt.setObject(2, importedIface.getContractID().getId());
				stmt.setObject(3, importedIfaceUUID);
				stmt.setObject(4, namespaceID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + stmt);
				}
				stmt.executeUpdate();
				stmt.close();

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
	 * @param namespaceID
	 *            ID of object
	 * @param ifaceID
	 *            Iface ID
	 * @param contractID
	 *            Contract ID
	 */
	public void updateNamespaceRemoveImport(final NamespaceID namespaceID, final InterfaceID ifaceID, final ContractID contractID) {
		synchronized (dataSource) {

			final Namespace namespace = this.getNamespaceByID(namespaceID);
			final ImportedInterface importedIface = namespace.getImportedInterface(ifaceID, contractID);
			this.deleteImportedInterfaceByID(importedIface.getId());

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement stmt = conn.prepareStatement(NamespaceManagerSQLStatements.SqlStatements.UPDATE_NAMESPACE_REMOVE_IMPORTEDIFACE.getSqlStatement());

				stmt.setObject(1, ifaceID.getId());
				stmt.setObject(2, contractID.getId());
				stmt.setObject(3, importedIface.getId());
				stmt.setObject(4, namespaceID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + stmt);
				}
				stmt.executeUpdate();
				stmt.close();

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
	 * Get Namespaces containing provider
	 * @param providerAccountID
	 *            Account ID
	 * @return The Namespace
	 */
	public List<Namespace> getNamespacesWithProvider(final AccountID providerAccountID) {
		synchronized (dataSource) {

			ResultSet rs = null;
			final List<Namespace> result = new ArrayList<>();
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(NamespaceManagerSQLStatements.SqlStatements.SELECT_ALL_NAMESPACES_OF_ACCOUNT.getSqlStatement());
				selectStatement.setObject(1, providerAccountID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				while (rs.next()) {
					result.add(deserializeNamespace(rs));
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
	 * Get Namespaces using class
	 * @param classID
	 *            class id
	 * @return The Namespace
	 */
	public List<Namespace> getAllNamespacesImportingClass(final MetaClassID classID) {
		synchronized (dataSource) {

			ResultSet rs = null;
			final List<Namespace> result = new ArrayList<>();
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(NamespaceManagerSQLStatements.SqlStatements.SELECT_ALL_NAMESPACES_IMPORT_IFACE.getSqlStatement());
				selectStatement.setObject(1, classID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				while (rs.next()) {
					result.add(deserializeNamespace(rs));
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
	 * Update by ID
	 * @param namespaceID
	 *            ID of object
	 * @param ifaceID
	 *            Iface ID
	 * @param contractID
	 *            Contract ID
	 * @param propertyID
	 *            Property ID
	 */
	public void updateImportedInterfaceAddProperty(final NamespaceID namespaceID, final InterfaceID ifaceID, final ContractID contractID,
			final PropertyID propertyID) {
		synchronized (dataSource) {

			final Namespace namespace = this.getNamespaceByID(namespaceID);
			final ImportedInterface importedIface = namespace.getImportedInterface(ifaceID, contractID);
			final UUID importedIfaceUUID = importedIface.getId();

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement stmt = conn.prepareStatement(NamespaceManagerSQLStatements.SqlStatements.UPDATE_IMPORTEDIFACE_ADD_PROPERTY.getSqlStatement());
				stmt.setObject(1, propertyID.getId());
				stmt.setObject(2, importedIfaceUUID);
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + stmt);
				}
				stmt.executeUpdate();
				stmt.close();

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
	 * @param namespaceID
	 *            ID of object
	 * @param ifaceID
	 *            Iface ID
	 * @param contractID
	 *            Contract ID
	 * @param operationID
	 *            Operation ID
	 */
	public void updateImportedInterfaceAddOperation(final NamespaceID namespaceID, final InterfaceID ifaceID, final ContractID contractID,
			final OperationID operationID) {
		synchronized (dataSource) {

			final Namespace namespace = this.getNamespaceByID(namespaceID);
			final ImportedInterface importedIface = namespace.getImportedInterface(ifaceID, contractID);
			final UUID importedIfaceUUID = importedIface.getId();

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement stmt = conn.prepareStatement(NamespaceManagerSQLStatements.SqlStatements.UPDATE_IMPORTEDIFACE_ADD_OPERATION.getSqlStatement());
				stmt.setObject(1, operationID.getId());
				stmt.setObject(2, importedIfaceUUID);
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + stmt);
				}
				stmt.executeUpdate();
				stmt.close();

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
	 * @param namespaceID
	 *            ID of object
	 * @param ifaceID
	 *            Iface ID
	 * @param contractID
	 *            Contract ID
	 * @param implementationID
	 *            Implementation ID
	 */
	public void updateImportedInterfaceAddImplementation(final NamespaceID namespaceID, final InterfaceID ifaceID, final ContractID contractID,
			final ImplementationID implementationID) {
		synchronized (dataSource) {

			final Namespace namespace = this.getNamespaceByID(namespaceID);
			final ImportedInterface importedIface = namespace.getImportedInterface(ifaceID, contractID);
			final UUID importedIfaceUUID = importedIface.getId();

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement stmt = conn.prepareStatement(NamespaceManagerSQLStatements.SqlStatements.UPDATE_IMPORTEDIFACE_ADD_IMPLEMENTATION.getSqlStatement());
				stmt.setObject(1, implementationID.getId());
				stmt.setObject(2, importedIfaceUUID);
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + stmt);
				}
				stmt.executeUpdate();
				stmt.close();

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
	 * @param namespaceID
	 *            ID of object
	 * @param ifaceID
	 *            Iface ID
	 * @param contractID
	 *            Contract ID
	 * @param subClassID
	 *            MetaClass ID
	 */
	public void updateImportedInterfaceAddSubClass(final NamespaceID namespaceID, final InterfaceID ifaceID, final ContractID contractID,
			final MetaClassID subClassID) {
		synchronized (dataSource) {

			final Namespace namespace = this.getNamespaceByID(namespaceID);
			final ImportedInterface importedIface = namespace.getImportedInterface(ifaceID, contractID);
			final UUID importedIfaceUUID = importedIface.getId();

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement stmt = conn.prepareStatement(NamespaceManagerSQLStatements.SqlStatements.UPDATE_IMPORTEDIFACE_ADD_SUBCLASS.getSqlStatement());
				stmt.setObject(1, subClassID.getId());
				stmt.setObject(2, importedIfaceUUID);
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + stmt);
				}
				stmt.executeUpdate();
				stmt.close();

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
	 * @param namespaceID
	 *            ID of object
	 * @param ifaceID
	 *            Iface ID
	 * @param contractID
	 *            Contract ID
	 * @param propertyID
	 *            Property ID
	 */
	public void updateImportedInterfaceRemoveProperty(final NamespaceID namespaceID, final InterfaceID ifaceID, final ContractID contractID,
			final PropertyID propertyID) {
		synchronized (dataSource) {

			final Namespace namespace = this.getNamespaceByID(namespaceID);
			final ImportedInterface importedIface = namespace.getImportedInterface(ifaceID, contractID);
			final UUID importedIfaceUUID = importedIface.getId();

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement stmt = conn.prepareStatement(NamespaceManagerSQLStatements.SqlStatements.UPDATE_IMPORTEDIFACE_REMOVE_PROPERTY.getSqlStatement());
				stmt.setObject(1, propertyID.getId());
				stmt.setObject(2, importedIfaceUUID);
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + stmt);
				}
				stmt.executeUpdate();
				stmt.close();

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
	 * @param namespaceID
	 *            ID of object
	 * @param ifaceID
	 *            Iface ID
	 * @param contractID
	 *            Contract ID
	 * @param operationID
	 *            Operation ID
	 */
	public void updateImportedInterfaceRemoveOperation(final NamespaceID namespaceID, final InterfaceID ifaceID, final ContractID contractID,
			final OperationID operationID) {
		synchronized (dataSource) {

			final Namespace namespace = this.getNamespaceByID(namespaceID);
			final ImportedInterface importedIface = namespace.getImportedInterface(ifaceID, contractID);
			final UUID importedIfaceUUID = importedIface.getId();

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement stmt = conn.prepareStatement(NamespaceManagerSQLStatements.SqlStatements.UPDATE_IMPORTEDIFACE_REMOVE_OPERATION.getSqlStatement());
				stmt.setObject(1, operationID.getId());
				stmt.setObject(2, importedIfaceUUID);
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + stmt);
				}
				stmt.executeUpdate();
				stmt.close();

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
	 * @param namespaceID
	 *            ID of object
	 * @param ifaceID
	 *            Iface ID
	 * @param contractID
	 *            Contract ID
	 * @param implementationID
	 *            Implementation ID
	 */
	public void updateImportedInterfaceRemoveImplementation(final NamespaceID namespaceID, final InterfaceID ifaceID, final ContractID contractID,
			final ImplementationID implementationID) {
		synchronized (dataSource) {

			final Namespace namespace = this.getNamespaceByID(namespaceID);
			final ImportedInterface importedIface = namespace.getImportedInterface(ifaceID, contractID);
			final UUID importedIfaceUUID = importedIface.getId();

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement stmt = conn.prepareStatement(NamespaceManagerSQLStatements.SqlStatements.UPDATE_IMPORTEDIFACE_REMOVE_IMPLEMENTATION.getSqlStatement());
				stmt.setObject(1, implementationID.getId());
				stmt.setObject(2, importedIfaceUUID);
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + stmt);
				}
				stmt.executeUpdate();
				stmt.close();

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
	 * @param namespaceID
	 *            ID of object
	 * @param ifaceID
	 *            Iface ID
	 * @param contractID
	 *            Contract ID
	 * @param subClassID
	 *            MetaClass ID
	 */
	public void updateImportedInterfaceRemoveSubClass(final NamespaceID namespaceID, final InterfaceID ifaceID, final ContractID contractID,
			final MetaClassID subClassID) {
		synchronized (dataSource) {

			final Namespace namespace = this.getNamespaceByID(namespaceID);
			final ImportedInterface importedIface = namespace.getImportedInterface(ifaceID, contractID);
			final UUID importedIfaceUUID = importedIface.getId();

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement stmt = conn.prepareStatement(NamespaceManagerSQLStatements.SqlStatements.UPDATE_IMPORTEDIFACE_REMOVE_SUBCLASS.getSqlStatement());
				stmt.setObject(1, subClassID.getId());
				stmt.setObject(2, importedIfaceUUID);
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + stmt);
				}
				stmt.executeUpdate();
				stmt.close();

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
	 * Check if namespace exists
	 * @param dataClayID
	 *            ID of namespace
	 * @return TRUE if exists.
	 */
	public boolean existsObjectByID(final NamespaceID dataClayID) {
		return this.getNamespaceByID(dataClayID) != null;
	}
}
