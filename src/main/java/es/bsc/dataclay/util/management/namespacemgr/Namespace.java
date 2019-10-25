
/**
 * @file Namespace.java
 * @date Sept 5, 2012
 * 
 */
package es.bsc.dataclay.util.management.namespacemgr;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.util.MgrObject;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.structs.Tuple;

/**
 * This class represents a namespace.
 * 
 */
public final class Namespace extends MgrObject<NamespaceID> {
	// === YAML SPECIFICATION === // 
	// Properties must be public for YAML parsing.
	//CHECKSTYLE:OFF
	/** Name of the user provider. */
	// TODO MGR-REFACTORING: delete
	private String providerAccountName;
	/** Namespace name. */
	private String name;
	
	// ==== DYNAMIC FIELDS ==== //
	/** Namespace responsible. */
	private AccountID responsible;
	/** Imported interfaces. */
	private Map<Tuple<InterfaceID, ContractID>, ImportedInterface> importedInterfaces;
	/** Language of data model in the namespace. */
	private Langs language;
	//CHECKSTYLE:ON

	/**
	 * Creates an empty namespace
	 * 
	 * @note This function does not generate a NamespaceID. It is necessary for the queries by example used in db4o.
	 */
	public Namespace() {

	}

	/**
	 * Namespace constructor with name and responsible
	 * @param newname
	 *            Name to be set
	 * @param newproviderAccount
	 *            Owner account to be set
	 * @param newLanguage
	 *            Language of the data model of the namespace.
	 * @post Creates a new namespace with provided name and responsible and generates a new NamespaceID.
	 */
	public Namespace(final String newname, final String newproviderAccount, final Langs newLanguage) {
		this.setName(newname);
		this.setProviderAccountName(newproviderAccount);
		this.setLanguage(newLanguage);
		this.setImportedInterfaces(new HashMap<Tuple<InterfaceID, ContractID>, ImportedInterface>());
	}

	/**
	 * Get the name of this Namespace
	 * @return Namespace::name of container Namespace.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the Namespace::name of this Namespace
	 * @param newname
	 *            New name to be set
	 */
	public void setName(final String newname) {
		this.name = newname;
	}

	/**
	 * Get the responsible of this Namespace
	 * @return Namespace::responsible of container Namespace.
	 */
	public AccountID getProviderAccountID() {
		return responsible;
	}

	/**
	 * Set the Namespace::responsible of this Namespace.
	 * @param newResponsible
	 *            Responsible account to set
	 * 
	 */
	public void setProviderAccountID(final AccountID newResponsible) {
		this.responsible = newResponsible;
	}

	/**
	 * Get the Namespace::importedInterfaces
	 * @return the importedInterfaces
	 */

	public Map<Tuple<InterfaceID, ContractID>, ImportedInterface> getImportedInterfaces() {
		return importedInterfaces;
	}

	/**
	 * Set the Namespace::importedInterfaces
	 * @param newimportedInterfaces
	 *            the importedInterfaces to set
	 */
	public void setImportedInterfaces(final Map<Tuple<InterfaceID, ContractID>, ImportedInterface> newimportedInterfaces) {
		this.importedInterfaces = newimportedInterfaces;
	}

	/**
	 * Removes an imported interface from this namespace
	 * @param interfaceToRemove
	 *            ID of the interface
	 * @param contractID
	 *            ID of the contract of the interface to remove
	 * @return The imported interface removed or null if it was not imported.
	 */
	public ImportedInterface removeImportedInterface(final InterfaceID interfaceToRemove, final ContractID contractID) {
		final Tuple<InterfaceID, ContractID> key = new Tuple<InterfaceID, ContractID>(interfaceToRemove, contractID);
		return this.importedInterfaces.remove(key);
	}

	/**
	 * Check if there is an imported interface with the ID provided
	 * @param importedInterface
	 *            ID of the imported interface
	 * @param contractID
	 *            ID of the contract of the imported interface
	 * @return TRUE if the interface was already improted. FALSE otherwise.
	 */
	public boolean existsImportedInterface(final InterfaceID importedInterface, final ContractID contractID) {
		final Tuple<InterfaceID, ContractID> key = new Tuple<InterfaceID, ContractID>(importedInterface, contractID);
		return this.importedInterfaces.containsKey(key);
	}

	/**
	 * Adds a new imported interface
	 * @param importedInterface
	 *            Specifications of the imported interface to add
	 * @pre The imported interface is not in the set of imported interfaces
	 */
	public void addImportedInterface(final ImportedInterface importedInterface) {
		final Tuple<InterfaceID, ContractID> key = new Tuple<InterfaceID, ContractID>(importedInterface.getInterfaceID(),
				importedInterface.getContractID());
		this.importedInterfaces.put(key, importedInterface);
	}

	/**
	 * Registers the property with ID provided as using the import with IDs specified
	 * @param propertyID
	 *            ID of the property using the import
	 * @param importedInterface
	 *            ID of the imported interface used
	 * @param contractID
	 *            ID of the contract of the imported interface
	 */
	public void registerPropertyUsingImport(final PropertyID propertyID, final InterfaceID importedInterface,
			final ContractID contractID) {
		final Tuple<InterfaceID, ContractID> key = new Tuple<InterfaceID, ContractID>(importedInterface, contractID);
		this.importedInterfaces.get(key).addPropertyUsingImport(propertyID);
	}

	/**
	 * Registers the operation with ID provided as using the import with IDs specified
	 * @param operationID
	 *            ID of the operation using the import
	 * @param importedInterface
	 *            ID of the imported interface used
	 * @param contractID
	 *            ID of the contract of the imported interface
	 */
	public void registerOperationUsingImport(final OperationID operationID, final InterfaceID importedInterface,
			final ContractID contractID) {
		final Tuple<InterfaceID, ContractID> key = new Tuple<InterfaceID, ContractID>(importedInterface, contractID);
		this.importedInterfaces.get(key).addOperationUsingImport(operationID);
	}

	/**
	 * Registers the implementation with ID provided as using the import with IDs specified
	 * @param implementationID
	 *            ID of the implementation using the import
	 * @param importedInterface
	 *            ID of the imported interface used
	 * @param contractID
	 *            ID of the contract of the imported interface
	 */
	public void registerImplementationUsingImport(final ImplementationID implementationID, final InterfaceID importedInterface,
			final ContractID contractID) {
		final Tuple<InterfaceID, ContractID> key = new Tuple<InterfaceID, ContractID>(importedInterface, contractID);
		this.importedInterfaces.get(key).addImplementationUsingImport(implementationID);
	}

	/**
	 * Registers the subclass with ID provided as extending the import with ID specified
	 * @param metaclassID
	 *            ID of the subclass using the import
	 * @param importedInterface
	 *            ID of the imported interface used
	 * @param contractID
	 *            ID of the contract of the imported interface
	 */
	public void registerSubClassUsingImport(final MetaClassID metaclassID, final InterfaceID importedInterface,
			final ContractID contractID) {
		final Tuple<InterfaceID, ContractID> key = new Tuple<InterfaceID, ContractID>(importedInterface, contractID);
		this.importedInterfaces.get(key).addSubClassUsingImport(metaclassID);
	}

	/**
	 * Unregisters the property with ID provided from using the import with IDs specified
	 * @param propertyID
	 *            ID of the property using the import
	 * @param importedInterface
	 *            ID of the imported interface used
	 * @param contractID
	 *            ID of the contract of the imported interface
	 */
	public void unregisterPropertyUsingImport(final PropertyID propertyID, final InterfaceID importedInterface,
			final ContractID contractID) {
		final Tuple<InterfaceID, ContractID> key = new Tuple<InterfaceID, ContractID>(importedInterface, contractID);
		this.importedInterfaces.get(key).removePropertyUsingImport(propertyID);
	}

	/**
	 * Unregisters the operation with ID provided from using the import with IDs specified
	 * @param operationID
	 *            ID of the operation using the import
	 * @param importedInterface
	 *            ID of the imported interface used
	 * @param contractID
	 *            ID of the contract of the imported interface
	 */
	public void unregisterOperationUsingImport(final OperationID operationID, final InterfaceID importedInterface,
			final ContractID contractID) {
		final Tuple<InterfaceID, ContractID> key = new Tuple<InterfaceID, ContractID>(importedInterface, contractID);
		this.importedInterfaces.get(key).removeOperationUsingImport(operationID);
	}

	/**
	 * Unregisters the implementation with ID provided from using the import with IDs specified
	 * @param implementationID
	 *            ID of the implementation using the import
	 * @param importedInterface
	 *            ID of the imported interface used
	 * @param contractID
	 *            ID of the contract of the imported interface
	 */
	public void unregisterImplementationUsingImport(final ImplementationID implementationID,
			final InterfaceID importedInterface, final ContractID contractID) {
		final Tuple<InterfaceID, ContractID> key = new Tuple<InterfaceID, ContractID>(importedInterface, contractID);
		this.importedInterfaces.get(key).removeImplementationUsingImport(implementationID);
	}

	/**
	 * Unregisters the subclass with ID provided as extending the import with ID specified
	 * @param metaclassID
	 *            ID of the subclass using the import
	 * @param importedInterface
	 *            ID of the imported interface used
	 * @param contractID
	 *            ID of the contract of the imported interface
	 */
	public void unregisterSubClassUsingImport(final MetaClassID metaclassID, final InterfaceID importedInterface,
			final ContractID contractID) {
		final Tuple<InterfaceID, ContractID> key = new Tuple<InterfaceID, ContractID>(importedInterface, contractID);
		this.importedInterfaces.get(key).removeSubClassUsingImport(metaclassID);
	}

	/**
	 * Get the imported interface with IDs provided
	 * @param importedInterface
	 *            ID of the imported interface
	 * @param contractID
	 *            ID of the contract of the imported interface
	 * @return The imported interface with IDs provided or Null if there is no imported interface with the IDs provided.
	 */
	public ImportedInterface getImportedInterface(final InterfaceID importedInterface, final ContractID contractID) {
		final Tuple<InterfaceID, ContractID> key = new Tuple<InterfaceID, ContractID>(importedInterface, contractID);
		return this.importedInterfaces.get(key);
	}

	/**
	 * Get the imported interfaces with Class ID provided
	 * @param metaClassID
	 *            ID of the class
	 * @return The imported interface with Class ID provided or empty if there is no imported interface with the Class ID
	 *         provided.
	 */
	public HashMap<Tuple<InterfaceID, ContractID>, ImportedInterface> getImportedInterfacesOfMetaClass(
			final MetaClassID metaClassID) {
		final HashMap<Tuple<InterfaceID, ContractID>, ImportedInterface> importsOfMetaClass = 
				new HashMap<Tuple<InterfaceID, ContractID>, ImportedInterface>();
		for (Entry<Tuple<InterfaceID, ContractID>, ImportedInterface> entry : this.importedInterfaces.entrySet()) {
			final ImportedInterface importedIface = entry.getValue();
			if (importedIface.getClassOfImportID().equals(metaClassID)) {
				importsOfMetaClass.put(entry.getKey(), entry.getValue());
			}
		}
		return importsOfMetaClass;
	}

	/**
	 * Get the Namespace::language
	 * @return the language
	 */
	public Langs getLanguage() {
		return language;
	}

	/**
	 * Set the Namespace::language
	 * @param newlanguage
	 *            the language to set
	 */
	public void setLanguage(final Langs newlanguage) {
		this.language = newlanguage;
	}

	/**
	 * Get providerAccountName
	 * @return the providerAccountName
	 */
	public String getProviderAccountName() {
		return providerAccountName;
	}

	/**
	 * Set providerAccountName
	 * @param newproviderAccountName the providerAccountName to set
	 */
	public void setProviderAccountName(final String newproviderAccountName) {
		this.providerAccountName = newproviderAccountName;
	}

}
