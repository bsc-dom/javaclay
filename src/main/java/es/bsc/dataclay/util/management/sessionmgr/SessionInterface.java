
/**
 * @file SessionInterface.java
 * @date May 31, 2013
 */
package es.bsc.dataclay.util.management.sessionmgr;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;

/**
 * This class represents an interface in a Session.
 * 
 */
public final class SessionInterface implements Serializable {

	/** ID. */
	private UUID id;
	/** Serial version UID. */
	private static final long serialVersionUID = 7949682815712165618L;
	/** ID of the interface. */
	private InterfaceID interfaceID;
	/** Session Properties in the interface. */
	private Map<PropertyID, SessionProperty> sessionProperties;
	/** Session Operations in the interface. */
	private Map<OperationID, SessionOperation> sessionOperations;
	/** ID of the MetaClass from which the Session interface was created. */
	private MetaClassID classOfInterface;
	/** ID of the Imported Interface from which the Session interface was created. */
	private InterfaceID importOfInterface;

	/**
	 * Empty constructor for specification of requirements while validating sessions
	 */
	public SessionInterface() {

	}

	/**
	 * Session interface constructor
	 * @param newinterfaceID
	 *            ID of the interface
	 * @param newclassOfInterface
	 *            ID of the MetaClass from which the Session interface was created
	 */
	public SessionInterface(final InterfaceID newinterfaceID, final MetaClassID newclassOfInterface) {
		this.setInterfaceID(newinterfaceID);
		this.setClassOfInterface(newclassOfInterface);
	}

	/**
	 * Get the SessionInterface::interfaceID
	 * @return the interfaceID
	 */

	public InterfaceID getInterfaceID() {
		return interfaceID;
	}

	/**
	 * Set the SessionInterface::interfaceID
	 * @param newinterfaceID
	 *            the interfaceID to set
	 */
	public void setInterfaceID(final InterfaceID newinterfaceID) {
		this.interfaceID = newinterfaceID;
	}

	/**
	 * Get the SessionInterface::sessionProperties
	 * @return the sessionProperties
	 */

	public Map<PropertyID, SessionProperty> getSessionProperties() {
		return sessionProperties;
	}

	/**
	 * Set the SessionInterface::sessionProperties
	 * @param newsessionProperties
	 *            the sessionProperties to set
	 */
	public void setSessionProperties(final Map<PropertyID, SessionProperty> newsessionProperties) {
		this.sessionProperties = newsessionProperties;
	}

	/**
	 * Get the SessionInterface::sessionOperations
	 * @return the sessionOperations
	 */

	public Map<OperationID, SessionOperation> getSessionOperations() {
		return sessionOperations;
	}

	/**
	 * Set the SessionInterface::sessionOperations
	 * @param newsessionOperations
	 *            the sessionOperations to set
	 */
	public void setSessionOperations(final Map<OperationID, SessionOperation> newsessionOperations) {
		this.sessionOperations = newsessionOperations;
	}

	/**
	 * Get the SessionInterface::classOfInterface
	 * @return the classOfInterface
	 */

	public MetaClassID getClassOfInterface() {
		return classOfInterface;
	}

	/**
	 * Set the SessionInterface::classOfInterface
	 * @param newclassOfInterface
	 *            the classOfInterface to set
	 */
	public void setClassOfInterface(final MetaClassID newclassOfInterface) {
		this.classOfInterface = newclassOfInterface;
	}

	/**
	 * Get the SessionInterface::importOfInterface
	 * @return the importOfInterface
	 */

	public InterfaceID getImportOfInterface() {
		return importOfInterface;
	}

	/**
	 * Set the SessionInterface::importOfInterface
	 * @param newimportOfInterface
	 *            the importOfInterface to set
	 */
	public void setImportOfInterface(final InterfaceID newimportOfInterface) {
		this.importOfInterface = newimportOfInterface;
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
