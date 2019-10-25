
package es.bsc.dataclay.util.management.stubs;

import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.management.classmgr.Type;

/**
 * This class represents the information about a property in a contract in a stub.
 * 
 */
public final class PropertyStubInfo {

	/** Namespace of the property (can be different from Stub in case of enrichments). */
	private String namespace;
	/** Name of the property. */
	private String propertyName;
	
	/** ID of the namespace. */
	private NamespaceID namespaceID;
	/** ID of the Property. */
	private PropertyID propertyID;
	/** Type of the Property. */
	private Type propertyType;
	
	/** ID of the getter operation. */
	private OperationID getterOperationID;
	/** ID of the setter operation. */
	private OperationID setterOperationID;

	private String beforeUpdate, afterUpdate;

	private boolean inMaster;

	/**
	 * Empty constructor
	 */
	public PropertyStubInfo() {

	}

	/**
	 * PropertyStubSpec constructor
	 * @param newpropertyID
	 *            ID of the Property
	 * @param newpropertyName
	 *            Name of the Property
	 * @param newpropertyType
	 *            Type of the Property
	 * @param newgetterOperationID
	 *            OperationID of the getter operation
	 * @param newsetterOperationID
	 *            OperationID of the setter operation
	 * @param newnamespace Namespace of the property (different for enrichments)
	 * @param newnamespaceID ID of the namespace      
	 */
	public PropertyStubInfo(final PropertyID newpropertyID, final String newpropertyName, 
			final Type newpropertyType, final OperationID newgetterOperationID, 
			final OperationID newsetterOperationID, final String newnamespace, 
			final NamespaceID newnamespaceID, final String beforeUpdate,
			final String afterUpdate, boolean inMaster) {
		this.setNamespace(newnamespace);
		this.setPropertyID(newpropertyID);
		this.setPropertyName(newpropertyName);
		this.setPropertyType(newpropertyType);
		this.setGetterOperationID(newgetterOperationID);
		this.setSetterOperationID(newsetterOperationID);
		this.setBeforeUpdate(beforeUpdate);
		this.setAfterUpdate(afterUpdate);
		this.setInMaster(inMaster);
	}

	/**
	 * Get the PropertyStubSpec::propertyID
	 * @return the propertyID
	 */

	public PropertyID getPropertyID() {
		return propertyID;
	}

	/**
	 * Set the PropertyStubSpec::propertyID
	 * @param newpropertyID
	 *            the propertyID to set
	 */
	public void setPropertyID(final PropertyID newpropertyID) {
		this.propertyID = newpropertyID;
	}

	@Override
	public boolean equals(final Object t) {
		if (t instanceof PropertyStubInfo) {
			final PropertyStubInfo other = (PropertyStubInfo) t;
			return other.getPropertyID().equals(this.getPropertyID());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.getPropertyID().hashCode();
	}

	/**
	 * Get the PropertyStubInfo::propertyName
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Set the PropertyStubInfo::propertyName
	 * @param newpropertyName
	 *            the propertyName to set
	 */
	public void setPropertyName(final String newpropertyName) {
		// can be null for virtual stubs
		this.propertyName = newpropertyName;
	}
	
	/**
	 * Get the PropertyStubInfo::propertyType
	 * @return the propertyType
	 */
	public Type getPropertyType() {
		return propertyType;
	}
	
	/**
	 * Set the PropertyStubInfo::propertyType
	 * @param newpropertyType 
	 *            the propertyType to set
	 */
	public void setPropertyType(final Type newpropertyType) {
		this.propertyType = newpropertyType;
	}
	
	/**
	 * Get the PropertyStubInfo::getterOperationID
	 * @return the OperationID for the getter operation
	 */
	public OperationID getGetterOperationID() {
		return this.getterOperationID;
	}
	
	/**
	 * Set the PropertyStubInfo::getterOperationID
	 * @param newgetterOperationID
	 *            the new OperationID for the getter
	 */	
	public void setGetterOperationID(final OperationID newgetterOperationID) {
		this.getterOperationID = newgetterOperationID;
	}
	
	/**
	 * Get the PropertyStubInfo::setterOperationID
	 * @return the OperationID for the setter operation
	 */
	public OperationID getSetterOperationID() {
		return this.setterOperationID;
	}
	
	/**
	 * Set the PropertyStubInfo::setterOperationID
	 * @param newsetterOperationID
	 *            the new OperationID for the setter
	 */
	public void setSetterOperationID(final OperationID newsetterOperationID) {
		this.setterOperationID = newsetterOperationID;
	}

	/**
	 * Get namespace
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Set namespace
	 * @param newnamespace the namespace to set
	 */
	public void setNamespace(final String newnamespace) {
		this.namespace = newnamespace;
	}

	/**
	 * Get namespaceID
	 * @return the namespaceID
	 */
	public NamespaceID getNamespaceID() {
		return namespaceID;
	}

	/**
	 * Set namespaceID
	 * @param newnamespaceID the namespaceID to set
	 */
	public void setNamespaceID(final NamespaceID newnamespaceID) {
		this.namespaceID = newnamespaceID;
	}

	public String getAfterUpdate() {
		return afterUpdate;
	}

	public void setAfterUpdate(String afterUpdate) {
		this.afterUpdate = afterUpdate;
	}

	public String getBeforeUpdate() {
		return beforeUpdate;
	}

	public void setBeforeUpdate(String beforeUpdate) {
		this.beforeUpdate = beforeUpdate;
	}

	public boolean getInMaster() {
		return inMaster;
	}

	public void setInMaster(boolean inMaster) {
		this.inMaster = inMaster;
	}
}
