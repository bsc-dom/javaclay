
/**
 * @file SessionProperty.java
 * @date May 31, 2013
 */
package es.bsc.dataclay.util.management.sessionmgr;

import java.io.Serializable;
import java.util.UUID;

import es.bsc.dataclay.util.ids.PropertyID;

/**
 * This class represents a property in a Session.
 * 
 */
public final class SessionProperty implements Serializable {

	/** ID. */
	private UUID id;
	/** Serial version UID. */
	private static final long serialVersionUID = -1527665284676391440L;
	/** ID of the Property. */
	private PropertyID propertyID;

	/**
	 * Empty constructor for specification of requirements while validating sessions
	 */
	public SessionProperty() {

	}

	/**
	 * SessionProperty constructor
	 * @param newpropertyID
	 *            ID of the Property
	 */
	public SessionProperty(final PropertyID newpropertyID) {
		this.setPropertyID(newpropertyID);
	}

	/**
	 * Get the SessionProperty::propertyID
	 * @return the propertyID
	 */

	public PropertyID getPropertyID() {
		return propertyID;
	}

	/**
	 * Set the SessionProperty::propertyID
	 * @param newpropertyID
	 *            the propertyID to set
	 */
	public void setPropertyID(final PropertyID newpropertyID) {
		this.propertyID = newpropertyID;
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
