
/**
 * @file ImportedInterface.java
 * @date May 29, 2013
 */
package es.bsc.dataclay.util.management.namespacemgr;

import java.io.Serializable;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.serialization.DataClaySerializable;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;

/**
 * This class represents an imported interface in a Namespace.
 * 
 */
public final class ImportedInterface implements Serializable, DataClaySerializable {

	/** ID. */
	private UUID id;
	/** Serial version UID. */
	private static final long serialVersionUID = -5961304602399766492L;
	/** Name of the class of the imported interface. */
	private String importedClassName;
	/** ID of the interface to import. */
	private InterfaceID interfaceID;
	/** ID of the contract used to import the interface. */
	private ContractID contractID;
	/** ID of the class of the import. */
	private MetaClassID classOfImportID;
	/** ID of the namespace of the class of the import. */
	private NamespaceID namespaceIDofClass;
	/** Set of IDs of properties using import. */
	private HashSet<PropertyID> propertiesUsingImports;
	/** Set of IDs of operations using import. */
	private HashSet<OperationID> operationsUsingImports;
	/** Set IDs of of implementations using import. */
	private HashSet<ImplementationID> implementationsUsingImports;
	/** Set of IDs of metaclasses that are subclass of the import. */
	private HashSet<MetaClassID> subClassesOfImport;

	/**
	 * Empty constructor for queries
	 */
	public ImportedInterface() {

	}

	/**
	 * Imported interface constructor
	 * @param newimportedClassName
	 *            Name of the class of the imported interface
	 * @param newinterfaceID
	 *            ID of the interface to import
	 * @param newcontractID
	 *            ID of the contract used to import the interface
	 * @param newclassOfImportID
	 *            ID of the class of the import
	 * @param newnamespaceIDofClass
	 *            ID of the namespace of the class of the import
	 */
	public ImportedInterface(final String newimportedClassName,
			final InterfaceID newinterfaceID, final ContractID newcontractID,
			final MetaClassID newclassOfImportID, final NamespaceID newnamespaceIDofClass) {
		this.setImportedClassName(newimportedClassName);
		this.setClassOfImportID(newclassOfImportID);
		this.setInterfaceID(newinterfaceID);
		this.setContractID(newcontractID);
		this.setNamespaceIDofClass(newnamespaceIDofClass);
		this.setPropertiesUsingImports(new HashSet<PropertyID>());
		this.setOperationsUsingImports(new HashSet<OperationID>());
		this.setImplementationsUsingImports(new HashSet<ImplementationID>());
		this.setSubClassesOfImport(new HashSet<MetaClassID>());
	}

	/**
	 * Get the Namespace::propertiesUsingImports
	 * @return the propertiesUsingImports
	 */
	public HashSet<PropertyID> getPropertiesUsingImports() {
		return propertiesUsingImports;
	}

	/**
	 * Set the Namespace::propertiesUsingImports
	 * @param newpropertiesUsingImports
	 *            the propertiesUsingImports to set
	 */
	public void setPropertiesUsingImports(final HashSet<PropertyID> newpropertiesUsingImports) {
		if (newpropertiesUsingImports == null) {
			throw new IllegalArgumentException("propertiesUsingImports cannot be null");
		}
		this.propertiesUsingImports = newpropertiesUsingImports;
	}

	/**
	 * Get the Namespace::operationsUsingImports
	 * @return the operationsUsingImports
	 */
	public HashSet<OperationID> getOperationsUsingImports() {
		return operationsUsingImports;
	}

	/**
	 * Set the Namespace::operationsUsingImports
	 * @param newoperationsUsingImports
	 *            the operationsUsingImports to set
	 */
	public void setOperationsUsingImports(final HashSet<OperationID> newoperationsUsingImports) {
		if (newoperationsUsingImports == null) {
			throw new IllegalArgumentException("operationsUsingImports cannot be null");
		}
		this.operationsUsingImports = newoperationsUsingImports;
	}

	/**
	 * Get the Namespace::implementationsUsingImports
	 * @return the implementationsUsingImports
	 */
	public HashSet<ImplementationID> getImplementationsUsingImports() {
		return implementationsUsingImports;
	}

	/**
	 * Set the Namespace::implementationsUsingImports
	 * @param newimplementationsUsingImports
	 *            the implementationsUsingImports to set
	 */
	public void setImplementationsUsingImports(final HashSet<ImplementationID> newimplementationsUsingImports) {
		if (newimplementationsUsingImports == null) {
			throw new IllegalArgumentException("implementationsUsingImports cannot be null");
		}
		this.implementationsUsingImports = newimplementationsUsingImports;
	}

	/**
	 * Get the ImportedInterface::subClassesOfImport
	 * @return the subClassesOfImport
	 */
	public HashSet<MetaClassID> getSubClassesOfImport() {
		return subClassesOfImport;
	}

	/**
	 * Set the ImportedInterface::subClassesOfImport
	 * @param newsubClassesOfImport
	 *            the subClassesOfImport to set
	 */
	public void setSubClassesOfImport(final HashSet<MetaClassID> newsubClassesOfImport) {
		if (newsubClassesOfImport == null) {
			throw new IllegalArgumentException("SubClasses of import cannot be null");
		}
		this.subClassesOfImport = newsubClassesOfImport;
	}

	/**
	 * Specifies if an imported interface is in use or not
	 * @return TRUE if the interface is in use by some property, operation, implementation or subclass. FALSE otherwise.
	 */
	public boolean inUse() {
		return !(this.implementationsUsingImports.isEmpty() && this.operationsUsingImports.isEmpty()
				&& this.propertiesUsingImports.isEmpty() && this.subClassesOfImport.isEmpty());
	}

	/**
	 * Add property that uses an import
	 * @param propertyID
	 *            ID of the property
	 */
	public void addPropertyUsingImport(final PropertyID propertyID) {
		this.propertiesUsingImports.add(propertyID);
	}

	/**
	 * Add operation that uses an import
	 * @param operationID
	 *            ID of the operation
	 */
	public void addOperationUsingImport(final OperationID operationID) {
		this.operationsUsingImports.add(operationID);
	}

	/**
	 * Add implementation that uses an import
	 * @param implementationID
	 *            ID of the implementation
	 */
	public void addImplementationUsingImport(final ImplementationID implementationID) {
		this.implementationsUsingImports.add(implementationID);
	}

	/**
	 * Add subclass that uses an import
	 * @param metaClassID
	 *            ID of the class
	 */
	public void addSubClassUsingImport(final MetaClassID metaClassID) {
		this.subClassesOfImport.add(metaClassID);
	}

	/**
	 * Remove property that uses an import
	 * @param propertyID
	 *            ID of the property
	 */
	public void removePropertyUsingImport(final PropertyID propertyID) {
		this.propertiesUsingImports.remove(propertyID);
	}

	/**
	 * Remove operation that uses an import
	 * @param operationID
	 *            ID of the operation
	 */
	public void removeOperationUsingImport(final OperationID operationID) {
		this.operationsUsingImports.remove(operationID);
	}

	/**
	 * Remove implementation that uses an import
	 * @param implementationID
	 *            ID of the implementation
	 */
	public void removeImplementationUsingImport(final ImplementationID implementationID) {
		this.implementationsUsingImports.remove(implementationID);
	}

	/**
	 * Remove subclass that uses an import
	 * @param metaClassID
	 *            ID of the class
	 */
	public void removeSubClassUsingImport(final MetaClassID metaClassID) {
		this.subClassesOfImport.remove(metaClassID);
	}

	/**
	 * Get the ImportedInterface::importedClassName
	 * @return the importedClassName
	 */
	public String getImportedClassName() {
		return importedClassName;
	}

	/**
	 * Set the ImportedInterface::importedClassName
	 * @param newimportedClassName
	 *            the importedClassName to set
	 */
	public void setImportedClassName(final String newimportedClassName) {
		if (newimportedClassName == null) {
			throw new IllegalArgumentException("importedClassName cannot be null");
		}
		this.importedClassName = newimportedClassName;
	}

	/**
	 * Get the ImportedInterface::interfaceID
	 * @return the interfaceID
	 */
	public InterfaceID getInterfaceID() {
		return interfaceID;
	}

	/**
	 * Set the ImportedInterface::interfaceID
	 * @param newinterfaceID
	 *            the interfaceID to set
	 */
	public void setInterfaceID(final InterfaceID newinterfaceID) {
		if (newinterfaceID == null) {
			throw new IllegalArgumentException("interfaceID cannot be null");
		}
		this.interfaceID = newinterfaceID;
	}

	/**
	 * Get the ImportedInterface::contractID
	 * @return the contractID
	 */
	public ContractID getContractID() {
		return contractID;
	}

	/**
	 * Set the ImportedInterface::contractID
	 * @param newcontractID
	 *            the contractID to set
	 */
	public void setContractID(final ContractID newcontractID) {
		if (newcontractID == null) {
			throw new IllegalArgumentException("contractID cannot be null");
		}
		this.contractID = newcontractID;
	}

	/**
	 * Get the ImportedInterface::classOfImportID
	 * @return the classOfImportID
	 */
	public MetaClassID getClassOfImportID() {
		return classOfImportID;
	}

	/**
	 * Set the ImportedInterface::classOfImportID
	 * @param newclassOfImportID
	 *            the classOfImportID to set
	 */
	public void setClassOfImportID(final MetaClassID newclassOfImportID) {
		if (newclassOfImportID == null) {
			throw new IllegalArgumentException("classOfImportID cannot be null");
		}
		this.classOfImportID = newclassOfImportID;
	}

	/**
	 * Get the ImportedInterface::namespaceIDofClass
	 * @return the namespaceIDofClass
	 */
	public NamespaceID getNamespaceIDofClass() {
		return namespaceIDofClass;
	}

	/**
	 * Set the ImportedInterface::namespaceIDofClass
	 * @param newnamespaceIDofClass
	 *            the namespaceIDofClass to set
	 */
	public void setNamespaceIDofClass(final NamespaceID newnamespaceIDofClass) {
		if (newnamespaceIDofClass == null) {
			throw new IllegalArgumentException("namespaceIDofClass cannot be null");
		}
		this.namespaceIDofClass = newnamespaceIDofClass;
	}

	@Override
	public boolean equals(final Object t) {
		if (t instanceof ImportedInterface) {
			final ImportedInterface other = (ImportedInterface) t;
			return other.getImportedClassName().equals(this.getImportedClassName())
					&& other.getInterfaceID().equals(this.getInterfaceID())
					&& other.getContractID().equals(this.getContractID())
					&& other.getNamespaceIDofClass().equals(this.getNamespaceIDofClass())
					&& other.getClassOfImportID().equals(this.getClassOfImportID());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.interfaceID.hashCode(); // any arbitrary constant will do
	}

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs, final ListIterator<DataClayObject> pendingObjs,
			ReferenceCounting referenceCounting) {

		dcBuffer.writeString(importedClassName);
		interfaceID.serialize(dcBuffer, ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
		contractID.serialize(dcBuffer, ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
		classOfImportID.serialize(dcBuffer, ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
		namespaceIDofClass.serialize(dcBuffer, ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);

		dcBuffer.writeInt(propertiesUsingImports.size());
		for (final PropertyID propID : propertiesUsingImports) {
			propID.serialize(dcBuffer, ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
		}

		dcBuffer.writeInt(operationsUsingImports.size());
		for (final OperationID opID : operationsUsingImports) {
			opID.serialize(dcBuffer, ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
		}

		dcBuffer.writeInt(implementationsUsingImports.size());
		for (final ImplementationID implID : implementationsUsingImports) {
			implID.serialize(dcBuffer, ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
		}

		dcBuffer.writeInt(subClassesOfImport.size());
		for (final MetaClassID clazzID : subClassesOfImport) {
			clazzID.serialize(dcBuffer, ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
		}
	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedJavaObjs) {

		importedClassName = dcBuffer.readString();
		interfaceID = new InterfaceID();
		interfaceID.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedJavaObjs);
		contractID = new ContractID();
		contractID.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedJavaObjs);
		classOfImportID = new MetaClassID();
		classOfImportID.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedJavaObjs);
		namespaceIDofClass = new NamespaceID();
		namespaceIDofClass.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedJavaObjs);
		this.propertiesUsingImports = new HashSet<>();
		int size = dcBuffer.readInt();
		for (int i = 0; i < size; ++i) {
			final PropertyID propID = new PropertyID();
			propID.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedJavaObjs);
			propertiesUsingImports.add(propID);
		}

		this.operationsUsingImports = new HashSet<>();
		size = dcBuffer.readInt();
		for (int i = 0; i < size; ++i) {
			final OperationID opID = new OperationID();
			opID.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedJavaObjs);
			operationsUsingImports.add(opID);
		}

		this.implementationsUsingImports = new HashSet<>();
		size = dcBuffer.readInt();
		for (int i = 0; i < size; ++i) {
			final ImplementationID implID = new ImplementationID();
			implID.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedJavaObjs);
			implementationsUsingImports.add(implID);
		}

		this.subClassesOfImport = new HashSet<>();
		size = dcBuffer.readInt();
		for (int i = 0; i < size; ++i) {
			final MetaClassID clazzID = new MetaClassID();
			clazzID.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedJavaObjs);
			subClassesOfImport.add(clazzID);
		}
	}

	/**
	 * get id
	 * @return id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Set id
	 * @param newid
	 *            the id
	 */
	public void setId(final UUID newid) {
		this.id = newid;
	}
}
