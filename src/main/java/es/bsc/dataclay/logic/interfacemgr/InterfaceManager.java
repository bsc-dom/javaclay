
/**
 * @file InterfaceManager.java
 * @date Sep 21, 2012
 */
package es.bsc.dataclay.logic.interfacemgr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.exceptions.logicmodule.interfacemgr.InterfaceNotExistsException;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.management.AbstractManager;
import es.bsc.dataclay.util.management.interfacemgr.Interface;
import es.bsc.dataclay.util.structs.MemoryCache;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;

/**
 * This class is responsible to manage interfaces: add, remove and modify.
 * 
 */
public final class InterfaceManager extends AbstractManager {

	/** Logger. */
	private static final Logger logger = LogManager.getLogger("managers.InterfaceManager");

	/** DbHandler for the management of Database. */
	private final InterfaceManagerDB interfaceDB;

	/** Interface cache . */
	private final MemoryCache<InterfaceID, Interface> interfaceCache;

	/**
	 * Instantiates an Interface Manager that uses the Interface DB in the provided path.
	 * @post Creates an Interface manager with the provided db path.
	 */
	public InterfaceManager(final SQLiteDataSource dataSource) {
		super(dataSource);
		this.interfaceDB = new InterfaceManagerDB(dataSource);
		this.interfaceDB.createTables();

		// Init cache
		this.interfaceCache = new MemoryCache<>();
	}

	/**
	 * This operation allows to register a new interface
	 * @param newInterface
	 *            Interface to register
	 * @return ID of the new interface created
	 * @throws Exception
	 *             if an exception occurs.
	 */
	public InterfaceID newInterface(final Interface newInterface) {

		final InterfaceID ifaceID = new InterfaceID();
		newInterface.setDataClayID(ifaceID);
		// Register it into the database
		interfaceDB.store(newInterface);

		// Update cache
		this.interfaceCache.put(newInterface.getDataClayID(), newInterface);

		return newInterface.getDataClayID();
	}

	/**
	 * This operation removes a specific interface.
	 * @param interfaceID
	 *            The id of the interface to be removed.
	 * @param namespaceID
	 *            the ID of the namespace where interface is created
	 * @returns true if the object has been removed properly, false if the object did not exist.
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             InterfaceNotExistsException: if the interface does not exist
	 */
	public void removeInterface(final NamespaceID namespaceID, final InterfaceID interfaceID) {
		final Interface curInterface = interfaceDB.getInterfaceByID(interfaceID);

		if (curInterface == null || !curInterface.getNamespaceID().equals(namespaceID)) {
			throw new InterfaceNotExistsException(interfaceID);
		} else {
			// Remove the interface from the database
			interfaceDB.deleteInterface(curInterface.getDataClayID());

			// Update cache
			this.interfaceCache.remove(interfaceID);
		}
	}

	// ============= OPERATIONS FOR CHECKING ================= //

	/**
	 * This operation verifies that all operations and properties can be accessed given the set of interfaces provided
	 * @param interfacesIDs
	 *            the interfaces ids
	 * @param operationsIDs
	 *            the set of operations
	 * @param propertiesIDs
	 *            the set of properties
	 * @return TRUE if all he properties and operations can be accessed from any of the specified interfaces. FALSE otherwise.
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             InterfaceNotExistsException: if the interface does not exist
	 */
	public boolean checkPropertiesAndOperationsInAnyOfInterfaces(final Set<InterfaceID> interfacesIDs,
			final Set<OperationID> operationsIDs, final Set<PropertyID> propertiesIDs) {

		final HashSet<OperationID> totalOperationsIDs = new HashSet<>();
		final HashSet<PropertyID> totalPropertiesIDs = new HashSet<>();
		for (final InterfaceID curInterfaceID : interfacesIDs) {
			// Try looking for interface in cache
			Interface curInterface = interfaceCache.get(curInterfaceID);
			if (curInterface == null) {
				// Query in db
				curInterface = interfaceDB.getInterfaceByID(curInterfaceID);
				if (curInterface == null) {
					throw new InterfaceNotExistsException(curInterfaceID);
				} else {
					// Update cache
					this.interfaceCache.put(curInterfaceID, curInterface);
				}
			}

			totalOperationsIDs.addAll(curInterface.getOperationsIDs());
			totalPropertiesIDs.addAll(curInterface.getPropertiesIDs());
			if (totalOperationsIDs.containsAll(operationsIDs) && totalPropertiesIDs.containsAll(propertiesIDs)) {
				return true;
			}
		}
		return false;
	}

	// ============ ESPECIAL OPS ============ //

	/**
	 * This operation retrieves the interfaces of a specific class.
	 * @param namespaceID
	 *            The id of the namespace of the class
	 * @param metaClassID
	 *            The id of the metaClass
	 * @return the list of interface ids related with the provided class
	 * @throws Exception
	 *             if an exception occurs.
	 */
	public HashSet<InterfaceID> getInterfacesOfClass(final NamespaceID namespaceID, final MetaClassID metaClassID) {

		final List<Interface> interfaces = interfaceDB.getInterfacesOfClass(namespaceID, metaClassID);

		// Init resulting list
		final HashSet<InterfaceID> result = new HashSet<>();
		for (final Interface curInterface : interfaces) {
			final InterfaceID interfaceID = curInterface.getDataClayID();
			result.add(interfaceID);

			// Update cache
			if (!this.interfaceCache.containsKey(interfaceID)) {
				this.interfaceCache.put(interfaceID, curInterface);
			}
		}
		return result;
	}

	/**
	 * This operation retrieves all the interfaces accessing a specific operation
	 * @param namespaceID
	 *            The id of the namespace of the metaclass (and the interface)
	 * @param metaClassID
	 *            The id of the metaclass of the operation (and the interface)
	 * @param operationID
	 *            The id of the operation to be looked for.
	 * @return The set of interfaces that access the provided operation.
	 * @throws Exception
	 *             if an exception occurs.
	 */
	public HashSet<InterfaceID> getInterfacesAccessingOperation(final NamespaceID namespaceID, final MetaClassID metaClassID,
			final OperationID operationID) {

		final List<Interface> interfaces = interfaceDB.getInterfacesOfClass(namespaceID, metaClassID);

		// Init resulting list
		final HashSet<InterfaceID> result = new HashSet<>();
		for (final Interface curInterface : interfaces) {
			// Check which interfaces access the provided operation
			final Set<OperationID> curInterfaceOps = curInterface.getOperationsIDs();
			if (curInterfaceOps.contains(operationID)) {
				final InterfaceID interfaceID = curInterface.getDataClayID();
				result.add(interfaceID);

				// Update cache
				if (!this.interfaceCache.containsKey(interfaceID)) {
					this.interfaceCache.put(interfaceID, curInterface);
				}
			}
		}
		return result;
	}

	/**
	 * This operation retrieves the subset of interfaces (fromt he set specified) that belong to any of the given classes
	 * @param interfacesIDs
	 *            the set of interfaces to be checked
	 * @param metaClassesIDs
	 *            the set of metaclasses to be checked
	 * @return The subset of interfaces that refer to any of the provided metaclasses.
	 * @throws Exception
	 *             if an exception occurs.
	 */
	public Set<InterfaceID> getSubsetInterfacesOfClasses(final Set<InterfaceID> interfacesIDs,
			final Set<MetaClassID> metaClassesIDs) {
		final Set<InterfaceID> result = new HashSet<>();
		for (final InterfaceID curInterfaceID : interfacesIDs) {
			// Get the current interface
			// Try looking for interface in cache
			Interface curInterface = interfaceCache.get(curInterfaceID);
			if (curInterface == null) {
				// Query in db
				curInterface = interfaceDB.getInterfaceByID(curInterfaceID);
			}
			if (curInterface != null) {
				if (metaClassesIDs.contains(curInterface.getMetaClassID())) {
					result.add(curInterfaceID);

					// Update cache
					if (!this.interfaceCache.containsKey(curInterfaceID)) {
						this.interfaceCache.put(curInterfaceID, curInterface);
					}
				}
			}
		}
		return result;
	}

	// =============== OPS FOR RETRIEVING INFO ============== //

	/**
	 * This operation returns a reference to the metaclass associated with a specific interface
	 * @param interfaceID
	 *            The id of the interface
	 * @return The id of the associated metaclass
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             InterfaceNotExistsException: if the interface does not exist
	 */
	public MetaClassID getMetaClassOfInterface(final InterfaceID interfaceID) {
		// Try looking for interface in cache
		Interface curInterface = interfaceCache.get(interfaceID);
		if (curInterface == null) {
			// Query in db
			curInterface = interfaceDB.getInterfaceByID(interfaceID);
			if (curInterface == null) {
				throw new InterfaceNotExistsException(interfaceID);
			} else {
				// Update cache
				this.interfaceCache.put(interfaceID, curInterface);
			}
		}
		return curInterface.getMetaClassID();
	}

	/**
	 * This operation returns all the information of a set of interfaces that belong to a specific namespace
	 * @param namespaceID
	 *            the id of the namespace
	 * @param interfacesIDs
	 *            the ids of the interfaces
	 * @return the interfaces info
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             InterfaceNotExistsException: if the interface does not exist
	 */
	public Map<InterfaceID, Interface> getInterfacesOfNamespaceInfo(final NamespaceID namespaceID,
			final Set<InterfaceID> interfacesIDs) {
		final Map<InterfaceID, Interface> result = new HashMap<>();

		for (final InterfaceID ifaceID : interfacesIDs) {
			// Get the current interface
			// Try looking for interface in cache
			Interface curInterface = interfaceCache.get(ifaceID);
			if (curInterface == null) {
				// Query in db
				curInterface = interfaceDB.getInterfaceByID(ifaceID);
			}

			if (curInterface == null || !curInterface.getNamespaceID().equals(namespaceID)) {
				logger.error("About to launch InterfaceNotExistsException for InterfaceID {}. "
						+ "Current interface {} (namespace {})",
						curInterface.getDataClayID(), namespaceID);
				throw new InterfaceNotExistsException(ifaceID);
			}

			// Update cache
			if (!this.interfaceCache.containsKey(ifaceID)) {
				this.interfaceCache.put(ifaceID, curInterface);
			}

			result.put(ifaceID, curInterface);
		}

		return result;
	}

	/**
	 * This operation returns all the information of the given interfaces
	 * @param interfacesIDs
	 *            the ids of the interfaces
	 * @return the interfaces info
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             InterfaceNotExistsException: if the interface does not exist
	 */
	public Map<InterfaceID, Interface> getInterfacesInfo(final Set<InterfaceID> interfacesIDs) {
		final Map<InterfaceID, Interface> result = new HashMap<>();

		for (final InterfaceID ifaceID : interfacesIDs) {
			// Get the current interface
			// Try looking for interface in cache
			final Interface curInterface = getInterfaceInfo(ifaceID);
			result.put(ifaceID, curInterface);
		}
		return result;
	}

	/**
	 * This operation returns all the information of the given interface
	 * @param ifaceID
	 *            the id of the interfaces
	 * @return the interface info
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             InterfaceNotExistsException: if the interface does not exist
	 */
	public Interface getInterfaceInfo(final InterfaceID ifaceID) {
		// Get the current interface
		// Try looking for interface in cache
		Interface curInterface = interfaceCache.get(ifaceID);
		if (curInterface == null) {
			// Query in db
			curInterface = interfaceDB.getInterfaceByID(ifaceID);
			if (curInterface == null) {
				throw new InterfaceNotExistsException(ifaceID);
			} else {
				// Update cache
				this.interfaceCache.put(ifaceID, curInterface);
			}
		}

		return curInterface;

	}

	/**
	 * Get ID of interface with specifications provided.
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
	 * @return ID of the interface
	 */
	public InterfaceID getInterfaceID(final String providerAccount, final String namespace, final String classname,
			final Set<String> propertiesInIface, final Set<String> operationsSignatureInIface) {
		final Interface result = interfaceDB.getInterfaceByNames(providerAccount, namespace, classname, propertiesInIface,
				operationsSignatureInIface);
		return result.getDataClayID();
	}

	/**
	 * Check if exists interface with specifications provided.
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
	 * @return TRUE if exists. FALSE otherwise.
	 */
	public boolean existsInterface(final String providerAccount, final String namespace, final String classname,
			final Set<String> propertiesInIface, final Set<String> operationsSignatureInIface) {
		final Interface result = interfaceDB.getInterfaceByNames(providerAccount, namespace, classname, propertiesInIface,
				operationsSignatureInIface);
		return result != null;
	}

	// ======== OTHER ======= //

	/**
	 * Method used for unit testing.
	 * @return The db handler reference of this manager.
	 */
	public InterfaceManagerDB getDbHandler() {
		return interfaceDB;
	}

	@Override
	public void cleanCaches() {
		this.interfaceCache.clear();
	}

}
