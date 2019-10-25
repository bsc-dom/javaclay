
/**
 * @file StubInfo.java
 * 
 * @date Jun 10, 2012
 */
package es.bsc.dataclay.util.management.stubs;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * Stub information. This information belongs to each stub.
 * 
 */
public final class StubInfo {

	/** Namespace of the class. */
	private String namespace;
	/** Name of the class. */
	private String className;
	/** Fully qualified name of parent class. */
	private String parentClassName;
	/** ID of the account applicant of the contract. */
	private AccountID applicantID;
	/** ID of class. */
	private MetaClassID classID;
	/** ID of namespace. */
	private NamespaceID namespaceID;
	/** The IDs of the contracts used for this stub. */
	private Set<ContractID> contracts;
	/** Implementations by ID. */
	private Map<String, ImplementationStubInfo> implementationsByID;
	/** Map Impl.Name and Signature -> ImplementationID string of implementation. Used in executeTasks and others. */
	private Map<String, ImplementationStubInfo> implementations;
	/** Information of the properties in the stub. */
	private Map<String, PropertyStubInfo> properties;
	/** Information of the Properties, ordering and extra null fields. */
	private List<String> propertyListWithNulls;

	/**
	 * StubInfo empty constructor
	 */
	public StubInfo() {

	}

	/**
	 * StubInfo constructor
	 * @param newnamespace Namespace of the stub
	 * @param newclassName Name of class of the stub
	 * @param newparentClassName Name of parent class of the stub (used to seek for impls)
	 * @param newapplicantID
	 *            ID of the account applicant of the contract.
	 * @param newclassID ID of class
	 * @param newnamespaceID ID of the namespace
	 * @param newimplementationsByID Implementations by id
	 * @param newimplementations
	 *            Information of the implementations in the stub.
	 * @param newproperties
	 *            Information of the properties in the stub.
	 * @param newpropertyListWithNulls
	 *            Information of the property ordered list (plus null fields) in the stub.
	 * @param newContracts
	 *            The IDs of the contracts used for this stub.
	 */
	public StubInfo(final String newnamespace, 
			final String newclassName, final String newparentClassName,
			final AccountID newapplicantID, 
			final MetaClassID newclassID,
			final NamespaceID newnamespaceID,
			final Map<String, ImplementationStubInfo> newimplementationsByID,
			final Map<String, ImplementationStubInfo> newimplementations,
			final Map<String, PropertyStubInfo> newproperties,
			final List<String> newpropertyListWithNulls,
			final Set<ContractID> newContracts) {
		this.setParentClassName(newparentClassName);
		this.setNamespace(newnamespace);
		this.setClassName(newclassName);
		this.setClassID(newclassID);
		this.setNamespaceID(newnamespaceID);
		this.setApplicantID(newapplicantID);
		this.setProperties(newproperties);
		this.setPropertyListWithNulls(newpropertyListWithNulls);
		this.setContracts(newContracts);
		this.setImplementations(newimplementations);
		this.setImplementationsByID(newimplementationsByID);
	}

	/**
	 * Get the StubInfoForGeneration::contracts
	 * @return the contracts
	 */
	public Set<ContractID> getContracts() {
		return this.contracts;
	}

	/**
	 * Set the StubInfoForGeneration::contracts
	 * @param newContracts
	 *            New contracts
	 */
	public void setContracts(final Set<ContractID> newContracts) {
		this.contracts = newContracts;
	}

	/**
	 * Get implementation Stub Info from impl id
	 * @param implID ID of impl.
	 * @return Implementation stub info
	 */
	public ImplementationStubInfo getImplementationByID(final String implID) {
		final ImplementationStubInfo impl = implementationsByID.get(implID);
		if (impl == null && this.getParentClassName() != null) { 
			// Check in parent stub infos
			final StubInfo parentStubInfo = DataClayObject.getStubInfoFromClass(this.getParentClassName());
			return parentStubInfo.getImplementationByID(implID);
		}
		return impl;
	}
	
	/**
	 * Get implementation Stub Info from op name and descriptor
	 * @param implNameAndDesc Implementation name and descriptor
	 * @return Implementation stub info
	 */
	public ImplementationStubInfo getImplementationByNameAndSignature(final String implNameAndDesc) { 
		final ImplementationStubInfo impl = implementations.get(implNameAndDesc);
		if (impl == null && this.getParentClassName() != null) { 
			// Check in parent stub infos
			final StubInfo parentStubInfo = DataClayObject.getStubInfoFromClass(this.getParentClassName());
			return parentStubInfo.getImplementationByNameAndSignature(implNameAndDesc);
		}
		return impl;
	}


	/**
	 * Set the StubInfoForGeneration::implementations
	 * @param newimplementations
	 *            the implementations to set
	 */
	public void setImplementations(final Map<String, ImplementationStubInfo> newimplementations) {
		this.implementations = newimplementations;
	}

	/**
	 * Get the StubInfo::properties
	 * @return the properties
	 */
	public Map<String, PropertyStubInfo> getProperties() {
		return properties;
	}

	/**
	 * Set the StubInfo::properties
	 * @param newproperties
	 *            the properties to set
	 */
	public void setProperties(final Map<String, PropertyStubInfo> newproperties) {
		this.properties = newproperties;
	}

	/**
	 * Get the StubInfo::proeprtyListWithNulls
	 * @return the propertyListWithNulls
	 */
	public List<String> getPropertyListWithNulls() {
		return propertyListWithNulls;
	}

	/**
	 * Set the StubInfo::propertyListWithNulls
	 * @param newpropertyListWithNulls 
	 *               the list of property names to set
	 */
	public void setPropertyListWithNulls(final List<String> newpropertyListWithNulls) {
		this.propertyListWithNulls = newpropertyListWithNulls;
	}

	/**
	 * Check if there is an PropertyStubInfo with name provided
	 * @param propertyName
	 *            Property name
	 * @return TRUE if there is an Property with name provided. FALSE otherwise.
	 */
	public boolean containsProperty(final String propertyName) {
		return this.properties.containsKey(propertyName);
	}

	/**
	 * Get the StubInfoForGeneration::applicantID
	 * @return the applicantID
	 */
	public AccountID getApplicantID() {
		return applicantID;
	}

	/**
	 * Set the StubInfoForGeneration::applicantID
	 * @param newapplicantID
	 *            the applicantID to set
	 */
	public void setApplicantID(final AccountID newapplicantID) {
		this.applicantID = newapplicantID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(namespace, className);
	}
	
	@Override
	public String toString() { 
		return CommonYAML.getYamlObject().dump(this);
	}

	/**
	 * Get the StubInfo::namespace
	 * @return the namespace
	 */
	public String getNamespace() {
		return this.namespace;
	}

	/**
	 * Set the StubInfo::namespace
	 * @param newnamespace the namespace to set
	 */
	public void setNamespace(final String newnamespace) {
		this.namespace = newnamespace;
	}

	/**
	 * Get the StubInfo::className
	 * @return the className
	 */
	public String getClassName() {
		return this.className;
	}

	/**
	 * Set the StubInfo::className
	 * @param newclassName the className to set
	 */
	public void setClassName(final String newclassName) {
		this.className = newclassName;
	}

	/**
	 * Get the StubInfo::classID
	 * @return the classID
	 */
	public MetaClassID getClassID() {
		return this.classID;
	}

	/**
	 * Set the StubInfo::classID
	 * @param newclassID the classID to set
	 */
	public void setClassID(final MetaClassID newclassID) {
		this.classID = newclassID;
	}

	/**
	 * Get the StubInfo::namespaceID
	 * @return the namespaceID
	 */
	public NamespaceID getNamespaceID() {
		return this.namespaceID;
	}

	/**
	 * Set the StubInfo::namespaceID
	 * @param newnamespaceID the namespaceID to set
	 */
	public void setNamespaceID(final NamespaceID newnamespaceID) {
		this.namespaceID = newnamespaceID;
	}

	/**
	 * Get parentClassName
	 * @return the parentClassName
	 */
	public String getParentClassName() {
		return parentClassName;
	}

	/**
	 * Set parentClassName
	 * @param newparentClassName the parentClassName to set
	 */
	public void setParentClassName(final String newparentClassName) {
		this.parentClassName = newparentClassName;
	}

	/**
	 * Get property with name provided
	 * @param name Name of the propery
	 * @return Property stub info.
	 */
	public PropertyStubInfo getPropertyWithName(final String name) {
		return this.properties.get(name);
	}

	/**
	 * Get implementationsByID
	 * @return the implementationsByID
	 */
	public Map<String, ImplementationStubInfo> getImplementationsByID() {
		return implementationsByID;
	}

	/**
	 * Set implementationsByID
	 * @param newimplementationsByID the implementationsByID to set
	 */
	public void setImplementationsByID(final Map<String, ImplementationStubInfo> newimplementationsByID) {
		this.implementationsByID = newimplementationsByID;
	}

	/**
	 * Get implementations
	 * @return the implementations
	 */
	public Map<String, ImplementationStubInfo> getImplementations() {
		return implementations;
	}


}
