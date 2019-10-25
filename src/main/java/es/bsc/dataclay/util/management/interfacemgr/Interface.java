
/**
 * @file Interface.java
 * @date Sep 21, 2012
 */
package es.bsc.dataclay.util.management.interfacemgr;

import java.util.Objects;
import java.util.Set;

import org.yaml.snakeyaml.Yaml;

import es.bsc.dataclay.util.MgrObject;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * This class represents a system interface.
 * 
 */
public final class Interface extends MgrObject<InterfaceID> {

	// === YAML SPECIFICATION === //
	// Properties must be public for YAML parsing.
	// CHECKSTYLE:OFF
	/** Name of the provider account. */
	// TODO MGR-REFACTORING: delete
	private String providerAccountName;
	/** The namespace of the class this interface refers to. */
	private String namespace;
	/** Name of the namespace of the class. */
	// TODO MGR-REFACTORING: delete
	private String classNamespace;
	/** The name of the class this interface refers to. */
	// TODO MGR-REFACTORING: delete
	private String className;
	/** Name of properties in interface. */
	private Set<String> propertiesInIface;
	/** Signature of operations in interface. */
	private Set<String> operationsSignatureInIface;
	// === DYNAMIC FIELDS === //
	/** ID of the account providing the interface. */
	private AccountID providerAccountID;
	/** The id of the namespace where the interface has been created. */
	private NamespaceID namespaceID;
	/** The id of the namespace where the class has been created. */
	private NamespaceID classNamespaceID;
	/** The id of the metaclass this interface refers to. */
	private MetaClassID metaClassID;
	/** The ids of the operations this interface can access. */
	private Set<OperationID> operationsIDs;
	/** The ids of the properties this interface can access. */
	private Set<PropertyID> propertiesIDs;
	// CHECKSTYLE:ON

	/**
	 * Basic constructor
	 */
	public Interface() {

	}

	/**
	 * Interface common constructor
	 * @param newproviderAccount
	 *            Account providing interface
	 * @param newnamespace
	 *            the Namespace where interface is being created
	 * @param newclassNamespace
	 *            Namespace of the class
	 * @param newclassName
	 *            the name of the class this interface refers to
	 * @param newpropertiesInIface
	 *            the signature of operations this interface can execute
	 * @param newoperationsSignatureInIface
	 *            the name of properties this interface can query
	 * @post Creates a new interface related with the corresponding metaclass of the given namespace. Configured to be able to
	 *       execute the provided set of operations in the metaclass, and
	 */
	public Interface(final String newproviderAccount, final String newnamespace,
			final String newclassNamespace, final String newclassName,
			final Set<String> newpropertiesInIface,
			final Set<String> newoperationsSignatureInIface) {
		this.setClassNamespace(newclassNamespace);
		this.setProviderAccountName(newproviderAccount);
		this.setNamespace(newnamespace);
		this.setClassName(newclassName);
		this.setPropertiesInIface(newpropertiesInIface);
		this.setOperationsSignatureInIface(newoperationsSignatureInIface);
	}

	/**
	 * @return the metaClassID
	 */
	public MetaClassID getMetaClassID() {
		return metaClassID;
	}

	/**
	 * @param newmetaClassID
	 *            the metaClassID to set
	 */
	public void setMetaClassID(final MetaClassID newmetaClassID) {
		this.metaClassID = newmetaClassID;
	}

	/**
	 * @return the operationsIDs
	 */
	public Set<OperationID> getOperationsIDs() {
		return getOperationIDs();
	}

	/**
	 * @param newoperationsIDs
	 *            the operationsIDs to set
	 */
	public void setOperationsIDs(final Set<OperationID> newoperationsIDs) {
		this.setOperationIDs(newoperationsIDs);
	}

	/**
	 * @return the propertiesIDs
	 */
	public Set<PropertyID> getPropertiesIDs() {
		return propertiesIDs;
	}

	/**
	 * @param newpropertiesIDs
	 *            the propertiesIDs to set
	 */
	public void setPropertiesIDs(final Set<PropertyID> newpropertiesIDs) {
		this.propertiesIDs = newpropertiesIDs;
	}

	/**
	 * Get the Interface::className
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Set the Interface::className
	 * @param newclassName
	 *            the className to set
	 */
	public void setClassName(final String newclassName) {
		this.className = newclassName;
	}

	/**
	 * Get the Interface::operationIDs
	 * @return the operationIDs (set of OperationID)
	 */
	private Set<OperationID> getOperationIDs() {
		return operationsIDs;
	}

	/**
	 * Set the Interface::operationIDs
	 * @param operationIDs
	 *            the set of OperationID
	 */
	private void setOperationIDs(final Set<OperationID> operationIDs) {
		this.operationsIDs = operationIDs;
	}

	/**
	 * Get the Interface::namespaceID
	 * @return the namespaceID
	 */
	public NamespaceID getNamespaceID() {
		return namespaceID;
	}

	/**
	 * Set the Interface::namespaceID
	 * @param newnamespaceID
	 *            the namespaceID to set
	 */
	public void setNamespaceID(final NamespaceID newnamespaceID) {
		this.namespaceID = newnamespaceID;
	}

	/**
	 * Get the Interface::namespace
	 * @return the namespace
	 */
	public String getNamespace() {
		return this.namespace;
	}

	/**
	 * Set the Interface::namespace
	 * @param newnamespace
	 *            the namespace to set
	 */
	public void setNamespace(final String newnamespace) {
		if (newnamespace == null) {
			throw new IllegalArgumentException("namespace cannot be null");
		}
		this.namespace = newnamespace;
	}

	/**
	 * Get the Interface::propertiesInIface
	 * @return the propertiesInIface
	 */
	public Set<String> getPropertiesInIface() {
		return this.propertiesInIface;
	}

	/**
	 * Set the Interface::propertiesInIface
	 * @param newpropertiesInIface
	 *            the propertiesInIface to set
	 */
	public void setPropertiesInIface(final Set<String> newpropertiesInIface) {
		this.propertiesInIface = newpropertiesInIface;
	}

	/**
	 * Get the Interface::operationsSignatureInIface
	 * @return the operationsSignatureInIface
	 */
	public Set<String> getOperationsSignatureInIface() {
		return this.operationsSignatureInIface;
	}

	/**
	 * Set the Interface::operationsSignatureInIface
	 * @param newoperationsSignatureInIface
	 *            the operationsSignatureInIface to set
	 */
	public void setOperationsSignatureInIface(final Set<String> newoperationsSignatureInIface) {
		this.operationsSignatureInIface = newoperationsSignatureInIface;
	}

	/**
	 * Get providerAccount
	 * @return the providerAccount
	 */
	public String getProviderAccountName() {
		return providerAccountName;
	}

	/**
	 * Set providerAccount
	 * @param newproviderAccount
	 *            the providerAccount to set
	 */
	public void setProviderAccountName(final String newproviderAccount) {
		this.providerAccountName = newproviderAccount;
	}

	/**
	 * Get providerAccountID
	 * @return the providerAccountID
	 */
	public AccountID getProviderAccountID() {
		return providerAccountID;
	}

	/**
	 * Set providerAccountID
	 * @param newproviderAccountID
	 *            the providerAccountID to set
	 */
	public void setProviderAccountID(final AccountID newproviderAccountID) {
		this.providerAccountID = newproviderAccountID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.providerAccountName, this.namespace, this.className,
				this.classNamespace, this.propertiesInIface, this.operationsSignatureInIface);
	}

	@Override
	public boolean equals(final Object other) {
		if (other instanceof Interface) {
			final Interface otherIface = (Interface) other;
			return otherIface.providerAccountName.equals(this.providerAccountName)
					&& otherIface.classNamespace.equals(this.classNamespace)
					&& otherIface.namespace.equals(this.namespace)
					&& otherIface.className.equals(this.className)
					&& otherIface.propertiesInIface.equals(this.propertiesInIface)
					&& otherIface.operationsSignatureInIface.equals(this.operationsSignatureInIface);
		}
		return false;
	}

	@Override
	public String toString() {
		final Yaml yaml = CommonYAML.getYamlObject();
		return yaml.dump(this);
	}

	/**
	 * Get classNamespace
	 * @return the classNamespace
	 */
	public String getClassNamespace() {
		return classNamespace;
	}

	/**
	 * Set classNamespace
	 * @param newclassNamespace
	 *            the classNamespace to set
	 */
	public void setClassNamespace(final String newclassNamespace) {
		this.classNamespace = newclassNamespace;
	}

	/**
	 * Get classNamespaceID
	 * @return the classNamespaceID
	 */
	public NamespaceID getClassNamespaceID() {
		return classNamespaceID;
	}

	/**
	 * Set classNamespaceID
	 * @param newclassNamespaceID
	 *            the classNamespaceID to set
	 */
	public void setClassNamespaceID(final NamespaceID newclassNamespaceID) {
		this.classNamespaceID = newclassNamespaceID;
	}
}
