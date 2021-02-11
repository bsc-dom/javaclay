
/**
 * @file NamespaceManager.java
 * @date Sep 5, 2012
 */

package es.bsc.dataclay.logic.namespacemgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.namespacemgr.AccountNotResponsibleOfNamespace;
import es.bsc.dataclay.exceptions.logicmodule.namespacemgr.ImportedInterfaceInUseException;
import es.bsc.dataclay.exceptions.logicmodule.namespacemgr.InterfaceAlreadyImportedException;
import es.bsc.dataclay.exceptions.logicmodule.namespacemgr.InterfaceNotImportedException;
import es.bsc.dataclay.exceptions.logicmodule.namespacemgr.NamespaceDoesNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.namespacemgr.NamespaceExistsException;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.management.AbstractManager;
import es.bsc.dataclay.util.management.namespacemgr.ImportedInterface;
import es.bsc.dataclay.util.management.namespacemgr.Namespace;
import es.bsc.dataclay.util.structs.LruCache;
import es.bsc.dataclay.util.structs.Tuple;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;

/**
 * This class is responsible for managing namespaces (add and remove).
 * 
 */

public final class NamespaceManager extends AbstractManager {

	/** Logger. */
	private static final Logger logger = LogManager.getLogger("managers.NamespaceManager");

	/** DbHandler for the management of Database. */
	private final NamespaceManagerDB namespaceDB;
	/** Namespace cache. */
	private final LruCache<NamespaceID, Namespace> namespaceCache;
	/** Namespaces of the cache indexed by name. */
	private final Map<String, NamespaceID> namespacesInCacheIndexedByName;

	/**
	 * Instantiates a Namespace Manager that uses the Namespace DB in the provided path.
	 * 
	 * @param dataSource
	 *            Manager/service name.
	 * @post Creates a Namespace manager and initializes the namespaceDB in the path provided.
	 */
	public NamespaceManager(final SQLiteDataSource dataSource) {
		super(dataSource);
		this.namespaceDB = new NamespaceManagerDB(dataSource);
		this.namespaceDB.createTables();

		// Init cache
		this.namespaceCache = new LruCache<>(
				Configuration.Flags.MAX_ENTRIES_NAMESPACE_MANAGER_CACHE.getIntValue());
		this.namespacesInCacheIndexedByName = new HashMap<>();
	}

	/**
	 * This operation creates a new namespace.
	 * 
	 * @param newNamespace
	 *            Namespace to create
	 * @post A namespace with the provided name and responsible is created
	 * @return namespaceID of the new namespace if the it was successfully created.
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             NamespaceExistsException if the namespace already exists
	 */
	public NamespaceID newNamespace(final Namespace newNamespace) {
		// Check that it does not already exist
		// (there is not another namespace with the same name, according
		// to the use case specification).
		if (namespaceDB.getNamespaceByName(newNamespace.getName()) != null) {
			throw new NamespaceExistsException(newNamespace.getName());
		}

		namespaceDB.store(newNamespace);
		final NamespaceID namespaceID = newNamespace.getDataClayID();

		// Update cache
		namespaceCache.put(namespaceID, newNamespace);
		namespacesInCacheIndexedByName.put(newNamespace.getName(), namespaceID);

		logger.info("Registered namespace {} with ID {}", newNamespace.getName(), namespaceID);

		return namespaceID;
	}

	/**
	 * Returns the language of the specified namespace name
	 * 
	 * @param namespaceName
	 *            the name of the namespace
	 * @return the language of the namespace
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             NamespaceDoesNotExistException: if the namespace does not exist
	 */
	public Langs getNamespaceLang(final String namespaceName) {
		final NamespaceID namespaceID = namespacesInCacheIndexedByName.get(namespaceName);
		final Namespace namespace;
		if (namespaceID == null) {
			namespace = namespaceDB.getNamespaceByName(namespaceName);
			// Update cache
			if (namespace != null) {
				namespaceCache.put(namespaceID, namespace);
				namespacesInCacheIndexedByName.put(namespaceName, namespaceID);
			}
		} else {
			namespace = namespaceCache.get(namespaceID);
		}
		if (namespace == null) {
			throw new NamespaceDoesNotExistException(namespaceName);
		}
		return namespace.getLanguage();
	}

	/**
	 * Returns the namespaceID of the specified namespace name
	 * 
	 * @param namespaceName
	 *            the name of the namespace
	 * @return the id of the namespace
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             NamespaceDoesNotExistException: if the namespace does not exist
	 */
	public NamespaceID getNamespaceID(final String namespaceName) {
		NamespaceID namespaceID = namespacesInCacheIndexedByName.get(namespaceName);
		if (namespaceID == null) {
			final Namespace curNamespace = namespaceDB.getNamespaceByName(namespaceName);
			if (curNamespace == null) {
				throw new NamespaceDoesNotExistException(namespaceName);
			}

			// Prepare result
			namespaceID = curNamespace.getDataClayID();

			// Update cache
			namespaceCache.put(namespaceID, curNamespace);
			namespacesInCacheIndexedByName.put(namespaceName, namespaceID);
		}

		return namespaceID;
	}

	/**
	 * This operation removes the indicated namespace
	 * 
	 * @param namespaceID
	 *            NamespaceID of the namespace to be removed
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             NamespaceDoesNotExistException: if the namespace does not exist
	 * 
	 */
	public void removeNamespace(final NamespaceID namespaceID) {
		final Namespace namespace = namespaceDB.getNamespaceByID(namespaceID);
		if (namespace == null) {
			throw new NamespaceDoesNotExistException(namespaceID);
		}
		namespace.setProviderAccountID(null); // Set the responsible to null in order to avoid removing it
		namespaceDB.deleteNamespaceByID(namespace.getDataClayID());

		// Update cache
		namespaceCache.remove(namespaceID);
		namespacesInCacheIndexedByName.remove(namespace.getName());
	}

	/**
	 * This operation retrieves the info of the given namespace
	 * 
	 * @param namespacesIDs
	 *            IDs of the namespaces to be retrieved
	 * @return the info of the namespaces
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             NamespaceDoesNotExistException: if the namespace does not exist
	 */
	public Map<NamespaceID, Namespace> getNamespacesInfo(final Set<NamespaceID> namespacesIDs) {
		final Map<NamespaceID, Namespace> result = new HashMap<>();
		for (final NamespaceID namespaceID : namespacesIDs) {
			final Namespace namespace = getNamespaceInfo(namespaceID);

			// Update result
			result.put(namespaceID, namespace);

		}
		return result;
	}

	/**
	 * This operation retrieves the info of the given namespace
	 * 
	 * @param namespaceID
	 *            IDs of the namespace to be retrieved
	 * @return the info of the namespace
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             NamespaceDoesNotExistException: if the namespace does not exist
	 */
	public Namespace getNamespaceInfo(final NamespaceID namespaceID) {
		Namespace namespace = namespaceCache.get(namespaceID);

		if (namespace == null) {
			namespace = namespaceDB.getNamespaceByID(namespaceID);

			if (namespace == null) {
				throw new NamespaceDoesNotExistException(namespaceID);
			}

			// Update cache
			namespaceCache.put(namespaceID, namespace);
			namespacesInCacheIndexedByName.put(namespace.getName(), namespaceID);
		}

		return namespace;
	}

	/**
	 * This operation checks whether an account is responsible for a namespace
	 * 
	 * @param responsible
	 *            AccountID of the responsible to be checked
	 * @param namespaceID
	 *            ID of the namespace to be checked
	 * @return true if accountID is responsible for namespaceID, false if not. <br>
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             NamespaceDoesNotExistException: if the namespace does not exist
	 */
	public Namespace checkNamespaceResponsibleAndGetInfo(final NamespaceID namespaceID, final AccountID responsible) {
		Namespace namespace = namespaceCache.get(namespaceID);
		if (namespace == null) {
			namespace = namespaceDB.getNamespaceByID(namespaceID);
			if (namespace == null) {
				throw new NamespaceDoesNotExistException(namespaceID);
			}

			// Update cache
			namespaceCache.put(namespaceID, namespace);
			namespacesInCacheIndexedByName.put(namespace.getName(), namespaceID);
		}

		if (Configuration.Flags.CHECK_NAMESPACE.getBooleanValue()) {
			if (!namespace.getProviderAccountID().equals(responsible)) {
				throw new AccountNotResponsibleOfNamespace(namespaceID, responsible);
			}
		}

		return namespace;
	}

	/**
	 * Register a set of new imported interfaces in the namespace
	 * 
	 * @param namespaceID
	 *            ID of the namespace in which the interface is imported
	 * @param newImportedInterfaces
	 *            Improted interfaces to register
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             NamespaceDoesNotExistException: if the namespace does not exist <br>
	 *             InterfaceAlreadyImportedException: If interface was already imported
	 */
	public void importInterfaces(final NamespaceID namespaceID,
			final Set<ImportedInterface> newImportedInterfaces) {
		// Check namespace exists
		Namespace namespace = namespaceCache.get(namespaceID);
		if (namespace == null) {
			namespace = namespaceDB.getNamespaceByID(namespaceID);
			if (namespace == null) {
				throw new NamespaceDoesNotExistException(namespaceID);
			}
		}

		// For each imported interface
		for (final ImportedInterface newImportedInterface : newImportedInterfaces) {

			// Verify the interface was not already imported
			if (namespace.existsImportedInterface(newImportedInterface.getInterfaceID(),
					newImportedInterface.getContractID())) {
				throw new InterfaceAlreadyImportedException(namespace.getName(), newImportedInterface.getInterfaceID(),
						newImportedInterface.getContractID());
			}

			namespace.addImportedInterface(newImportedInterface);
		}
		// Update the imported interfaces
		try {

			for (final ImportedInterface newImportedInterface : newImportedInterfaces) {
				namespaceDB.updateNamespaceAddImport(namespaceID, newImportedInterface);
			}

			// Update cache
			if (namespaceCache.containsKey(namespaceID)) {
				namespaceCache.get(namespaceID).setImportedInterfaces(namespace.getImportedInterfaces());
			} else {
				namespaceCache.put(namespaceID, namespace);
				namespacesInCacheIndexedByName.put(namespace.getName(), namespaceID);
			}

		} catch (final DbObjectNotExistException e) {
			throw new NamespaceDoesNotExistException(namespaceID);
		}
	}

	/**
	 * Removes the imported interface in contract with IDs provided in the Namespace with ID specified.
	 * 
	 * @param namespaceID
	 *            ID of the namespace containing the import
	 * @param importedInterfaces
	 *            the imported interfaces to be removed
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             NamespaceDoesNotExistException: if the namespace does not exist <br>
	 *             InterfaceNotImportedException: If interface with Name provided does not exist
	 */
	public void removeImportedInterfaces(final NamespaceID namespaceID,
			final Set<ImportedInterface> importedInterfaces) {
		// Check namespace exists
		Namespace namespace = namespaceCache.get(namespaceID);
		if (namespace == null) {
			namespace = namespaceDB.getNamespaceByID(namespaceID);
			if (namespace == null) {
				throw new NamespaceDoesNotExistException(namespaceID);
			}
		}

		for (final ImportedInterface curImport : importedInterfaces) {
			final InterfaceID interfaceID = curImport.getInterfaceID();
			final ContractID contractID = curImport.getContractID();
			// Remove the interface and check it exists
			if (namespace.removeImportedInterface(interfaceID, contractID) == null) {
				throw new InterfaceNotImportedException(namespace.getName(), interfaceID, contractID);
			}
		}

		// Update the imported interfaces
		try {

			for (final ImportedInterface curImport : importedInterfaces) {
				namespaceDB.updateNamespaceRemoveImport(namespaceID, curImport.getInterfaceID(), curImport.getContractID());
			}

			// Update cache
			if (namespaceCache.containsKey(namespaceID)) {
				namespaceCache.get(namespaceID).setImportedInterfaces(namespace.getImportedInterfaces());
			} else {
				namespaceCache.put(namespaceID, namespace);
				namespacesInCacheIndexedByName.put(namespace.getName(), namespaceID);
			}

		} catch (final DbObjectNotExistException e) {
			throw new NamespaceDoesNotExistException(namespaceID);
		}

	}

	/**
	 * Verify that there is an imported interface in contract with IDs provided and it is not used by any property, operation or
	 * implementation and is not the parent class of any other class.
	 * 
	 * @param namespaceID
	 *            ID of the namespace that imports
	 * @param className
	 *            name of the class
	 * @return The info of the imports of the class in namespace, which are not in use
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             NamespaceDoesNotExistException: if the namespace does not exist <br>
	 *             ImportedInterfaceInUseException: If an imported interface of the classname in namespace is in use
	 */
	public HashSet<ImportedInterface> checkImportsOfClassAreNotUsedAndGet(final NamespaceID namespaceID,
			final String className) {
		// Check namespace exists
		Namespace namespace = namespaceCache.get(namespaceID);
		if (namespace == null) {
			namespace = namespaceDB.getNamespaceByID(namespaceID);

			if (namespace == null) {
				throw new NamespaceDoesNotExistException(namespaceID);
			}

			// Update cache
			namespaceCache.put(namespaceID, namespace);
			namespacesInCacheIndexedByName.put(namespace.getName(), namespaceID);
		}

		// Seek for the imported interfaces
		final HashSet<ImportedInterface> result = new HashSet<>();
		final Map<Tuple<InterfaceID, ContractID>, ImportedInterface> importedInterfaces = namespace.getImportedInterfaces();
		for (final ImportedInterface importedInterface : importedInterfaces.values()) {
			if (importedInterface.getImportedClassName().equals(className)) {
				final InterfaceID interfaceID = importedInterface.getInterfaceID();
				final ContractID contractID = importedInterface.getContractID();
				// Verify there is no property, operation, implementation or subclass using the import
				if (importedInterface.inUse()) {
					throw new ImportedInterfaceInUseException(namespace.getName(), interfaceID, contractID);
				} else {
					result.add(importedInterface);
				}
			}
		}

		return result;
	}

	/**
	 * This operation gets information of the imported interfaces for a specific class in a specific namespace
	 * 
	 * @param namespaceID
	 *            ID of the namespace that imports
	 * @param className
	 *            the class to be checked
	 * @return The information of the imported interfaces
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             NamespaceDoesNotExistException: if the namespace does not exist
	 */
	public HashSet<ImportedInterface> getImportedInterfaces(final NamespaceID namespaceID, final String className) {
		// Check namespace exists
		Namespace namespace = namespaceCache.get(namespaceID);
		if (namespace == null) {
			namespace = namespaceDB.getNamespaceByID(namespaceID);

			if (namespace == null) {
				throw new NamespaceDoesNotExistException(namespaceID);
			}

			// Update cache
			namespaceCache.put(namespaceID, namespace);
			namespacesInCacheIndexedByName.put(namespace.getName(), namespaceID);
		}

		// Seek for the imported interfaces
		final HashSet<ImportedInterface> result = new HashSet<>();
		final Map<Tuple<InterfaceID, ContractID>, ImportedInterface> importedInterfaces = namespace.getImportedInterfaces();
		if (importedInterfaces != null) {
			for (final ImportedInterface importedInterface : importedInterfaces.values()) {
				if (importedInterface.getImportedClassName().equals(className)) {
					result.add(importedInterface);
				}
			}
		}

		return result;
	}

	/**
	 * This operation gets information of the imported interfaces in the namespace specified for the given metaclasses
	 * 
	 * @param namespaceID
	 *            the id of the namespace
	 * @param metaClassIDs
	 *            the ids of the metaclasses
	 * @return indexed table of the import information for every class
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             NamespaceDoesNotExistException: if the namespace does not exist <br>
	 *             ClassNotImportedException: if the class is not imported in namespace provided
	 */
	public Map<MetaClassID, Set<ImportedInterface>> getImportedInterfacesForMetaclasses(final NamespaceID namespaceID,
			final Set<MetaClassID> metaClassIDs) {
		// Check namespace exists
		Namespace namespace = namespaceCache.get(namespaceID);
		if (namespace == null) {
			namespace = namespaceDB.getNamespaceByID(namespaceID);

			if (namespace == null) {
				throw new NamespaceDoesNotExistException(namespaceID);
			}

			// Update cache
			namespaceCache.put(namespaceID, namespace);
			namespacesInCacheIndexedByName.put(namespace.getName(), namespaceID);
		}

		// Seek for the imported interfaces
		final Map<Tuple<InterfaceID, ContractID>, ImportedInterface> importedInterfaces = namespace.getImportedInterfaces();

		// Prepare result
		final Map<MetaClassID, Set<ImportedInterface>> result = new HashMap<>();
		for (final ImportedInterface importedInterface : importedInterfaces.values()) {
			final MetaClassID metaClassIDofImport = importedInterface.getClassOfImportID();
			if (metaClassIDs.contains(metaClassIDofImport)) {
				if (!result.containsKey(metaClassIDofImport)) {
					result.put(metaClassIDofImport, new HashSet<ImportedInterface>());
				}
				result.get(metaClassIDofImport).add(importedInterface);
			}
		}
		return result;
	}

	/**
	 * Get all the information of all the imports in the namespaces of the account with ID provided
	 * 
	 * @param accountID
	 *            ID of the account
	 * @return A set of import specifications of the imports in the namespaces of the account with ID provided. It can be empty.
	 * @throws Exception
	 *             if an exception occurs.
	 */
	public HashSet<ImportedInterface> getImportsOfAccount(final AccountID accountID) {

		// Prepare result
		final HashSet<ImportedInterface> result = new HashSet<>();

		// First get all the namespaces of the account
		final ArrayList<Namespace> namespacesOfAccount = (ArrayList<Namespace>) namespaceDB.getNamespacesWithProvider(accountID);

		// Now for each Namespace obtained get its imports
		for (int i = 0; i < namespacesOfAccount.size(); ++i) {
			final Map<Tuple<InterfaceID, ContractID>, ImportedInterface> importsOfNamespace = namespacesOfAccount.get(i)
					.getImportedInterfaces();
			for (final Entry<Tuple<InterfaceID, ContractID>, ImportedInterface> entry : importsOfNamespace.entrySet()) {
				final ImportedInterface importIface = entry.getValue();
				result.add(importIface); // If already present, not added
			}
		}
		return result;
	}

	/**
	 * Get namespace names that import the metaclass with id provided
	 * 
	 * @param classID
	 *            ID of the class
	 * @return Set of names of namespaces that import the metaclass with id provided
	 * @throws Exception
	 *             if some exception occurs.
	 */
	public Set<String> getNamespaceNamesThatImportsMetaClass(final MetaClassID classID) {

		final List<Namespace> namespacesOfAccount = namespaceDB.getAllNamespacesImportingClass(classID);

		final Set<String> result = new HashSet<>();
		// Now for each Namespace obtained get its imports
		for (final Namespace dom : namespacesOfAccount) {
			result.add(dom.getName());
		}
		return result;
	}

	/**
	 * Get all names of namespaces
	 * 
	 * @return set of namespaces names
	 */
	public Set<String> getNamespacesNames() {
		return namespaceDB.getNamespacesNames();
	}

	/**
	 * Get all the information of the imports in the given namespace of the account with ID provided
	 * 
	 * @param accountID
	 *            ID of the account
	 * @param namespaceID
	 *            ID of the namespace
	 * @return A set of import specifications of the imports in the namespace of the account with ID provided. It can be empty.
	 * @throws Exception
	 *             if an exception occurs.
	 */
	public HashSet<ImportedInterface> getImportsOfAccountInNamespace(final AccountID accountID,
			final NamespaceID namespaceID) {
		final List<Namespace> namespacesOfAccount = namespaceDB.getNamespacesWithProvider(accountID);
		final HashSet<ImportedInterface> result = new HashSet<>();

		// Now for each Namespace obtained get its imports
		for (int i = 0; i < namespacesOfAccount.size(); ++i) {
			final Map<Tuple<InterfaceID, ContractID>, ImportedInterface> importsOfNamespace = namespacesOfAccount.get(i)
					.getImportedInterfaces();
			for (final Entry<Tuple<InterfaceID, ContractID>, ImportedInterface> entry : importsOfNamespace.entrySet()) {
				final ImportedInterface importIface = entry.getValue();
				result.add(importIface); // If already present, not added
			}
		}
		return result;
	}

	/**
	 * Registers the properties with IDs provided as using an imported class
	 * 
	 * @param namespaceID
	 *            ID of the namespace that imports
	 * @param propertiesIDs
	 *            IDs of the properties using the import
	 * @param interfacesInContractOfImportedClass
	 *            the interfaces in contract of imported class
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             NamespaceDoesNotExistException: if the namespace does not exist <br>
	 *             InterfaceNotImportedException: If interface was not imported
	 */
	public void registerPropertiesUsingImportedClass(final NamespaceID namespaceID, final Set<PropertyID> propertiesIDs,
			final Map<ContractID, InterfaceID> interfacesInContractOfImportedClass) {

		// Check namespace exists
		Namespace namespace = namespaceCache.get(namespaceID);
		if (namespace == null) {
			namespace = namespaceDB.getNamespaceByID(namespaceID);
			if (namespace == null) {
				throw new NamespaceDoesNotExistException(namespaceID);
			}
		}

		for (final Entry<ContractID, InterfaceID> interfaceInContract : interfacesInContractOfImportedClass.entrySet()) {
			final ContractID contractID = interfaceInContract.getKey();
			final InterfaceID interfaceID = interfaceInContract.getValue();

			// Verify the imported interface exists
			if (!namespace.existsImportedInterface(interfaceID, contractID)) {
				throw new InterfaceNotImportedException(namespace.getName(), interfaceID, contractID);
			}

			// For each property ID
			for (final PropertyID propertyID : propertiesIDs) {
				// Register the property
				namespace.registerPropertyUsingImport(propertyID, interfaceID, contractID);
			}
		}

		// Update
		try {
			for (final Entry<ContractID, InterfaceID> interfaceInContract : interfacesInContractOfImportedClass.entrySet()) {
				final ContractID contractID = interfaceInContract.getKey();
				final InterfaceID interfaceID = interfaceInContract.getValue();
				for (final PropertyID propertyID : propertiesIDs) {
					namespaceDB.updateImportedInterfaceAddProperty(namespaceID, interfaceID, contractID, propertyID);
				}
			}

			// Update cache
			if (namespaceCache.containsKey(namespaceID)) {
				namespaceCache.get(namespaceID).setImportedInterfaces(namespace.getImportedInterfaces());
			} else {
				namespaceCache.put(namespaceID, namespace);
				namespacesInCacheIndexedByName.put(namespace.getName(), namespaceID);
			}
		} catch (final DbObjectNotExistException e) {
			throw new NamespaceDoesNotExistException(namespaceID);
		}
	}

	/**
	 * Registers the operations with IDs provided as using an imported class
	 * 
	 * @param namespaceID
	 *            ID of the namespace that imports
	 * @param operationsIDs
	 *            IDs of the operations using the import
	 * @param interfacesInContractOfImportedClass
	 *            the interfaces in contract of imported class
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             NamespaceDoesNotExistException: if the namespace does not exist <br>
	 *             InterfaceNotImportedException: If interface was not imported
	 */
	public void registerOperationsUsingImportedClass(final NamespaceID namespaceID, final Set<OperationID> operationsIDs,
			final Map<ContractID, InterfaceID> interfacesInContractOfImportedClass) {

		// Check namespace exists
		Namespace namespace = namespaceCache.get(namespaceID);
		if (namespace == null) {
			namespace = namespaceDB.getNamespaceByID(namespaceID);
			if (namespace == null) {
				throw new NamespaceDoesNotExistException(namespaceID);
			}
		}

		for (final Entry<ContractID, InterfaceID> interfaceInContract : interfacesInContractOfImportedClass.entrySet()) {
			final ContractID contractID = interfaceInContract.getKey();
			final InterfaceID interfaceID = interfaceInContract.getValue();

			// Verify the imported interface exists
			if (!namespace.existsImportedInterface(interfaceID, contractID)) {
				throw new InterfaceNotImportedException(namespace.getName(), interfaceID, contractID);
			}

			// For each operation ID
			for (final OperationID operationID : operationsIDs) {
				// Register the operation using import
				namespace.registerOperationUsingImport(operationID, interfaceID, contractID);
			}
		}

		// Update
		try {

			for (final Entry<ContractID, InterfaceID> interfaceInContract : interfacesInContractOfImportedClass.entrySet()) {
				final ContractID contractID = interfaceInContract.getKey();
				final InterfaceID interfaceID = interfaceInContract.getValue();
				for (final OperationID operationID : operationsIDs) {
					namespaceDB.updateImportedInterfaceAddOperation(namespaceID, interfaceID, contractID, operationID);
				}
			}

			// Update cache
			if (namespaceCache.containsKey(namespaceID)) {
				namespaceCache.get(namespaceID).setImportedInterfaces(namespace.getImportedInterfaces());
			} else {
				namespaceCache.put(namespaceID, namespace);
				namespacesInCacheIndexedByName.put(namespace.getName(), namespaceID);
			}
		} catch (final DbObjectNotExistException e) {
			throw new NamespaceDoesNotExistException(namespaceID);
		}
	}

	/**
	 * Registers the implementations with IDs provided as using an imported class
	 * 
	 * @param namespaceID
	 *            ID of the namespace that imports
	 * @param implementationsIDs
	 *            IDs of the implementations using the import
	 * @param interfacesInContractOfImportedClass
	 *            the interfaces in contract of imported class
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             NamespaceDoesNotExistException: if the namespace does not exist <br>
	 *             InterfaceNotImportedException: If interface was not imported
	 */
	public void registerImplementationsUsingImportedClass(final NamespaceID namespaceID,
			final Set<ImplementationID> implementationsIDs,
			final Map<ContractID, InterfaceID> interfacesInContractOfImportedClass) {

		// Check namespace exists
		Namespace namespace = namespaceCache.get(namespaceID);
		if (namespace == null) {
			namespace = namespaceDB.getNamespaceByID(namespaceID);
			if (namespace == null) {
				throw new NamespaceDoesNotExistException(namespaceID);
			}
		}

		for (final Entry<ContractID, InterfaceID> interfaceInContract : interfacesInContractOfImportedClass.entrySet()) {
			final ContractID contractID = interfaceInContract.getKey();
			final InterfaceID interfaceID = interfaceInContract.getValue();

			// Verify the imported interface exists
			if (!namespace.existsImportedInterface(interfaceID, contractID)) {
				throw new InterfaceNotImportedException(namespace.getName(), interfaceID, contractID);
			}

			// For each implementation ID
			for (final ImplementationID implID : implementationsIDs) {
				// Register the implementation
				namespace.registerImplementationUsingImport(implID, interfaceID, contractID);
			}
		}

		// Update
		try {

			for (final Entry<ContractID, InterfaceID> interfaceInContract : interfacesInContractOfImportedClass.entrySet()) {
				final ContractID contractID = interfaceInContract.getKey();
				final InterfaceID interfaceID = interfaceInContract.getValue();
				for (final ImplementationID implID : implementationsIDs) {
					namespaceDB.updateImportedInterfaceAddImplementation(namespaceID, interfaceID, contractID, implID);
				}
			}

			// Update cache
			if (namespaceCache.containsKey(namespaceID)) {
				namespaceCache.get(namespaceID).setImportedInterfaces(namespace.getImportedInterfaces());
			} else {
				namespaceCache.put(namespaceID, namespace);
				namespacesInCacheIndexedByName.put(namespace.getName(), namespaceID);
			}
		} catch (final DbObjectNotExistException e) {
			throw new NamespaceDoesNotExistException(namespaceID);
		}
	}

	/**
	 * Registers the classes with IDs provided as extending from an imported class
	 * 
	 * @param namespaceID
	 *            ID of the namespace that imports
	 * @param metaClassesIDs
	 *            IDs of the classes extending the import
	 * @param interfacesInContractOfImportedClass
	 *            the interfaces in contract of imported class
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             NamespaceDoesNotExistException: if the namespace does not exist <br>
	 *             InterfaceNotImportedException: If interface was not imported
	 */
	public void registerSubClassesUsingImportedClass(final NamespaceID namespaceID, final Set<MetaClassID> metaClassesIDs,
			final Map<ContractID, InterfaceID> interfacesInContractOfImportedClass) {

		// Check namespace exists
		Namespace namespace = namespaceCache.get(namespaceID);
		if (namespace == null) {
			namespace = namespaceDB.getNamespaceByID(namespaceID);
			if (namespace == null) {
				throw new NamespaceDoesNotExistException(namespaceID);
			}
		}

		for (final Entry<ContractID, InterfaceID> interfaceInContract : interfacesInContractOfImportedClass.entrySet()) {
			final ContractID contractID = interfaceInContract.getKey();
			final InterfaceID interfaceID = interfaceInContract.getValue();

			// Verify the imported interface exists
			if (!namespace.existsImportedInterface(interfaceID, contractID)) {
				throw new InterfaceNotImportedException(namespace.getName(), interfaceID, contractID);
			}

			// For each class ID
			for (final MetaClassID metaClassID : metaClassesIDs) {
				// Register the class
				namespace.registerSubClassUsingImport(metaClassID, interfaceID, contractID);
			}
		}

		// Update
		try {
			for (final Entry<ContractID, InterfaceID> interfaceInContract : interfacesInContractOfImportedClass.entrySet()) {
				final ContractID contractID = interfaceInContract.getKey();
				final InterfaceID interfaceID = interfaceInContract.getValue();
				for (final MetaClassID metaClassID : metaClassesIDs) {
					namespaceDB.updateImportedInterfaceAddSubClass(namespaceID, interfaceID, contractID, metaClassID);
				}
			}
			// Update cache
			if (namespaceCache.containsKey(namespaceID)) {
				namespaceCache.get(namespaceID).setImportedInterfaces(namespace.getImportedInterfaces());
			} else {
				namespaceCache.put(namespaceID, namespace);
				namespacesInCacheIndexedByName.put(namespace.getName(), namespaceID);
			}
		} catch (final DbObjectNotExistException e) {
			throw new NamespaceDoesNotExistException(namespaceID);
		}
	}

	/**
	 * Unregisters the operations with IDs provided from using any imports of the specified namespace
	 * 
	 * @param namespaceID
	 *            ID of the namespace that imports
	 * @param operationsIDs
	 *            IDs of the operations using the import
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             NamespaceDoesNotExistException: if the namespace does not exist
	 */
	public void unregisterOperationsFromUsingAnyImportedClassInNamespace(final NamespaceID namespaceID,
			final Set<OperationID> operationsIDs) {
		// Check namespace exists
		Namespace namespace = namespaceCache.get(namespaceID);
		if (namespace == null) {
			namespace = namespaceDB.getNamespaceByID(namespaceID);
			if (namespace == null) {
				throw new NamespaceDoesNotExistException(namespaceID);
			}
		}

		for (final Tuple<InterfaceID, ContractID> interfaceInContract : namespace.getImportedInterfaces().keySet()) {
			final InterfaceID interfaceID = interfaceInContract.getFirst();
			final ContractID contractID = interfaceInContract.getSecond();

			// For each property ID
			for (final OperationID operationID : operationsIDs) {
				// Unregister the property
				namespace.unregisterOperationUsingImport(operationID, interfaceID, contractID);
			}
		}

		// Update
		try {

			for (final Tuple<InterfaceID, ContractID> interfaceInContract : namespace.getImportedInterfaces().keySet()) {
				final InterfaceID interfaceID = interfaceInContract.getFirst();
				final ContractID contractID = interfaceInContract.getSecond();
				for (final OperationID operationID : operationsIDs) {
					namespaceDB.updateImportedInterfaceRemoveOperation(namespaceID, interfaceID, contractID, operationID);
				}
			}

			// Update cache
			if (namespaceCache.containsKey(namespaceID)) {
				namespaceCache.get(namespaceID).setImportedInterfaces(namespace.getImportedInterfaces());
			} else {
				namespaceCache.put(namespaceID, namespace);
				namespacesInCacheIndexedByName.put(namespace.getName(), namespaceID);
			}
		} catch (final DbObjectNotExistException e) {
			throw new NamespaceDoesNotExistException(namespaceID);
		}
	}

	/**
	 * Unregisters the implementations with IDs provided from using any imports of the specified namespace
	 * 
	 * @param namespaceID
	 *            ID of the namespace that imports
	 * @param implementationsIDs
	 *            IDs of the implementations using the import
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             NamespaceDoesNotExistException: if the namespace does not exist
	 */
	public void unregisterImplementationsFromUsingAnyImportedClassInNamespace(final NamespaceID namespaceID,
			final Set<ImplementationID> implementationsIDs) {
		// Check namespace exists
		Namespace namespace = namespaceCache.get(namespaceID);
		if (namespace == null) {
			namespace = namespaceDB.getNamespaceByID(namespaceID);
			if (namespace == null) {
				throw new NamespaceDoesNotExistException(namespaceID);
			}
		}

		for (final Tuple<InterfaceID, ContractID> interfaceInContract : namespace.getImportedInterfaces().keySet()) {
			final InterfaceID interfaceID = interfaceInContract.getFirst();
			final ContractID contractID = interfaceInContract.getSecond();

			// For each property ID
			for (final ImplementationID implementationID : implementationsIDs) {
				// Unregister the property
				namespace.unregisterImplementationUsingImport(implementationID, interfaceID, contractID);
			}
		}

		// Update
		try {

			for (final Tuple<InterfaceID, ContractID> interfaceInContract : namespace.getImportedInterfaces().keySet()) {
				final InterfaceID interfaceID = interfaceInContract.getFirst();
				final ContractID contractID = interfaceInContract.getSecond();
				for (final ImplementationID implementationID : implementationsIDs) {
					namespaceDB.updateImportedInterfaceRemoveImplementation(namespaceID, interfaceID, contractID, implementationID);
				}
			}

			// Update cache
			if (namespaceCache.containsKey(namespaceID)) {
				namespaceCache.get(namespaceID).setImportedInterfaces(namespace.getImportedInterfaces());
			} else {
				namespaceCache.put(namespaceID, namespace);
				namespacesInCacheIndexedByName.put(namespace.getName(), namespaceID);
			}
		} catch (final DbObjectNotExistException e) {
			throw new NamespaceDoesNotExistException(namespaceID);
		}
	}

	/**
	 * Unregisters the classes with IDs provided as using (by extension) from any imports of the specified namespace
	 * 
	 * @param namespaceID
	 *            ID of the namespace that imports
	 * @param metaClassesIDs
	 *            IDs of the classes extending the import
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             NamespaceDoesNotExistException: if the namespace does not exist
	 */
	public void unregisterSubClassesFromUsingAnyImportedClassInNamespace(final NamespaceID namespaceID,
			final Set<MetaClassID> metaClassesIDs) {
		// Check namespace exists
		Namespace namespace = namespaceCache.get(namespaceID);
		if (namespace == null) {
			namespace = namespaceDB.getNamespaceByID(namespaceID);
			if (namespace == null) {
				throw new NamespaceDoesNotExistException(namespaceID);
			}
		}

		for (final Tuple<InterfaceID, ContractID> interfaceInContract : namespace.getImportedInterfaces().keySet()) {
			final InterfaceID interfaceID = interfaceInContract.getFirst();
			final ContractID contractID = interfaceInContract.getSecond();

			// For each property ID
			for (final MetaClassID metaClassID : metaClassesIDs) {
				// Unregister the property
				namespace.unregisterSubClassUsingImport(metaClassID, interfaceID, contractID);
			}
		}

		// Update
		try {
			for (final Tuple<InterfaceID, ContractID> interfaceInContract : namespace.getImportedInterfaces().keySet()) {
				final InterfaceID interfaceID = interfaceInContract.getFirst();
				final ContractID contractID = interfaceInContract.getSecond();
				for (final MetaClassID metaClassID : metaClassesIDs) {
					namespaceDB.updateImportedInterfaceRemoveSubClass(namespaceID, interfaceID, contractID, metaClassID);
				}
			}
			// Update cache
			if (namespaceCache.containsKey(namespaceID)) {
				namespaceCache.get(namespaceID).setImportedInterfaces(namespace.getImportedInterfaces());
			} else {
				namespaceCache.put(namespaceID, namespace);
				namespacesInCacheIndexedByName.put(namespace.getName(), namespaceID);
			}
		} catch (final DbObjectNotExistException e) {
			throw new NamespaceDoesNotExistException(namespaceID);
		}
	}

	// ========= OTHER ========= //

	/**
	 * Method used for unit testing.
	 * 
	 * @return The db handler reference of this manager.
	 */
	public NamespaceManagerDB getDbHandler() {
		return namespaceDB;
	}

	@Override
	public void cleanCaches() {
		this.namespaceCache.clear();
	}

}
