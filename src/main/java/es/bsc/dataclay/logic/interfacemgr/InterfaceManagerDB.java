
package es.bsc.dataclay.logic.interfacemgr;

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

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.exceptions.dbhandler.DbObjectAlreadyExistException;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectNotExistException;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.management.interfacemgr.Interface;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;

/**
 * Data base connection.
 */
public final class InterfaceManagerDB {

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
	public InterfaceManagerDB(final SQLiteDataSource dataSource) {
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
				for (final InterfaceManagerSQLStatements.SqlStatements stmt : InterfaceManagerSQLStatements.SqlStatements.values()) {
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
				for (final InterfaceManagerSQLStatements.SqlStatements stmt : InterfaceManagerSQLStatements.SqlStatements.values()) {
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
	 * @param iface
	 *            Interface to store
	 */
	public void store(final Interface iface) {
		synchronized (dataSource) {

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement insertStatement = conn.prepareStatement(InterfaceManagerSQLStatements.SqlStatements.INSERT_INTERFACE.getSqlStatement());
				insertStatement.setObject(1, iface.getDataClayID().getId());
				// CHECKSTYLE:OFF
				insertStatement.setString(2, iface.getProviderAccountName());
				insertStatement.setString(3, iface.getNamespace());
				insertStatement.setString(4, iface.getClassNamespace());
				insertStatement.setString(5, iface.getClassName());
				Array tArray = insertStatement.getConnection().createArrayOf("varchar", iface.getPropertiesInIface().toArray());
				insertStatement.setArray(6, tArray);
				tArray = insertStatement.getConnection().createArrayOf("varchar", iface.getOperationsSignatureInIface().toArray());
				insertStatement.setArray(7, tArray);
				insertStatement.setObject(8, iface.getProviderAccountID().getId());
				insertStatement.setObject(9, iface.getNamespaceID().getId());
				insertStatement.setObject(10, iface.getClassNamespaceID().getId());
				insertStatement.setObject(11, iface.getMetaClassID().getId());

				if (iface.getOperationsIDs() != null) {
					final UUID[] opsUUIDs = new UUID[iface.getOperationsIDs().size()];
					int i = 0;
					for (final OperationID opID : iface.getOperationsIDs()) {
						opsUUIDs[i] = opID.getId();
						i++;
					}

					tArray = insertStatement.getConnection().createArrayOf("uuid", opsUUIDs);
					insertStatement.setArray(12, tArray);
				} else {
					insertStatement.setArray(12, null);
				}

				if (iface.getPropertiesIDs() != null) {

					final UUID[] propsUUIDs = new UUID[iface.getPropertiesIDs().size()];
					int i = 0;
					for (final PropertyID propID : iface.getPropertiesIDs()) {
						propsUUIDs[i] = propID.getId();
						i++;
					}

					tArray = insertStatement.getConnection().createArrayOf("uuid", propsUUIDs);
					insertStatement.setArray(13, tArray);
				} else {
					insertStatement.setArray(13, null);
				}

				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + insertStatement);
				}
				insertStatement.executeUpdate();

			} catch (final Exception e) {
				e.printStackTrace();
				throw new DbObjectAlreadyExistException(iface.getDataClayID());
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
	 * Deserialize Interface
	 * @param rs
	 *            Result set
	 * @return Interface
	 */
	private Interface deserializeInterface(
			final ResultSet rs) {
		Interface iface = null;
		try {
			// CHECKSTYLE:OFF
			final InterfaceID interfaceID = new InterfaceID((UUID)rs.getObject("id"));
			final String providerAccountName = rs.getString("providerAccountName");
			final String namespace = rs.getString("namespace");

			final String classNamespace = rs.getString("classNamespace");
			final String className = rs.getString("className");
			final String[] propertiesInIfaceArr = (String[]) rs.getArray("propertiesInIface").getArray();
			final Set<String> propertiesInIface = new HashSet<>(Arrays.asList(propertiesInIfaceArr));
			final String[] opsInIfaceArr = (String[]) rs.getArray("operationsSignatureInIface").getArray();
			final Set<String> operationsSignatureInIface = new HashSet<>(Arrays.asList(opsInIfaceArr));

			final AccountID providerAccountID = new AccountID((UUID)rs.getObject("providerAccountID"));
			final NamespaceID namespaceID = new NamespaceID((UUID)rs.getObject("namespaceID"));
			final NamespaceID classNamespaceID = new NamespaceID((UUID)rs.getObject("classNamespaceID"));
			final MetaClassID metaClassID = new MetaClassID((UUID)rs.getObject("metaClassID"));

			Array array = rs.getArray("operationsIDs");
			final Set<OperationID> operationsIDs = new HashSet<>();
			if (array != null) {
				final UUID[] opsUUIDs = (UUID[]) array.getArray();
				for (final UUID opUUID : opsUUIDs) {
					operationsIDs.add(new OperationID(opUUID));
				}
			}

			array = rs.getArray("propertiesIDs");
			final Set<PropertyID> propertyIDs = new HashSet<>();
			if (array != null) {
				final UUID[] propsUUIDs = (UUID[]) array.getArray();
				for (final UUID propUUID : propsUUIDs) {
					propertyIDs.add(new PropertyID(propUUID));
				}
			}

			iface = new Interface();
			iface.setClassName(className);
			iface.setClassNamespace(classNamespace);
			iface.setClassNamespaceID(classNamespaceID);
			iface.setDataClayID(interfaceID);
			iface.setMetaClassID(metaClassID);
			iface.setNamespace(namespace);
			iface.setNamespaceID(namespaceID);
			iface.setOperationsIDs(operationsIDs);
			iface.setOperationsSignatureInIface(operationsSignatureInIface);
			iface.setPropertiesIDs(propertyIDs);
			iface.setPropertiesInIface(propertiesInIface);
			iface.setProviderAccountID(providerAccountID);
			iface.setProviderAccountName(providerAccountName);

		} catch (final SQLException e) {
			e.printStackTrace();
		}
		return iface;
	}

	/**
	 * Get interface by ID
	 * @param ifaceID
	 *            ID of the interface
	 * @return The interface
	 */
	public Interface getInterfaceByID(final InterfaceID ifaceID) {
		synchronized (dataSource) {

			ResultSet rs = null;
			Interface iface = null;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(InterfaceManagerSQLStatements.SqlStatements.SELECT_INTERFACE.getSqlStatement());

				selectStatement.setObject(1, ifaceID.getId());
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				if (rs.next()) {
					iface = deserializeInterface(rs);
				}

			} catch (final SQLException e) {
				throw new DbObjectNotExistException(ifaceID);
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

			return iface;
		}
	}

	/**
	 * Delete
	 * @param id
	 *            ID of object to delete
	 */
	public void deleteInterface(final InterfaceID id) {
		synchronized (dataSource) {

			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement stmt = conn.prepareStatement(InterfaceManagerSQLStatements.SqlStatements.DELETE_INTERFACE.getSqlStatement());
				stmt.setObject(1, id.getId());
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
	 * Get interfaces of class
	 * @param namespaceID
	 *            Namespace ID
	 * @param classID
	 *            class ID
	 * @return The classes
	 */
	public List<Interface> getInterfacesOfClass(
			final NamespaceID namespaceID, final MetaClassID classID) {
		synchronized (dataSource) {

			ResultSet rs = null;
			final List<Interface> result = new ArrayList<>();
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(InterfaceManagerSQLStatements.SqlStatements.SELECT_IFACES_OF_CLASS.getSqlStatement());

				selectStatement.setObject(1, namespaceID.getId());
				selectStatement.setObject(2, classID.getId());

				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				while (rs.next()) {
					result.add(deserializeInterface(rs));
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
	 * Get interface by names
	 * @param providerAccount
	 *            Name of the account providing the interface
	 * @param namespace
	 *            Namespace of the class of the interface
	 * @param classname
	 *            Classname of the class of the interface
	 * @param propertiesInIface
	 *            Properties in interface
	 * @param operationsSignatureInIface
	 *            Operations in interface
	 * @return The interface
	 */
	public Interface getInterfaceByNames(final String providerAccount, final String namespace, final String classname,
			final Set<String> propertiesInIface, final Set<String> operationsSignatureInIface) {
		synchronized (dataSource) {

			ResultSet rs = null;
			Interface iface = null;
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				final PreparedStatement selectStatement = conn.prepareStatement(InterfaceManagerSQLStatements.SqlStatements.SELECT_IFACE_FROM_NAMES.getSqlStatement());

				selectStatement.setString(1, providerAccount);
				selectStatement.setString(2, namespace);
				// CHECKSTYLE:OFF
				selectStatement.setString(3, classname);

				Array tArray = selectStatement.getConnection().createArrayOf("varchar", propertiesInIface.toArray());
				selectStatement.setArray(4, tArray);
				tArray = selectStatement.getConnection().createArrayOf("varchar", operationsSignatureInIface.toArray());
				selectStatement.setArray(5, tArray);
				// CHECKSTYLE:ON
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Executing " + selectStatement);
				}
				rs = selectStatement.executeQuery();
				if (rs.next()) {
					iface = deserializeInterface(rs);
				}

			} catch (final SQLException e) {
				throw new DbObjectNotExistException();
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

			return iface;
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
	 * check if iface exists.
	 * @param dataClayID
	 *            ID of interface
	 * @return TRUE if exists.
	 */
	public boolean existsObjectByID(final InterfaceID dataClayID) {
		return this.getInterfaceByID(dataClayID) != null;
	}
}
