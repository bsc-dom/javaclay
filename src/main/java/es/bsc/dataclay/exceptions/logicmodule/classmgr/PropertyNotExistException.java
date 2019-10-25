
/**
 * @file PropertyNotExistException.java
 * 
 * @date Sep 18, 2012
 */
package es.bsc.dataclay.exceptions.logicmodule.classmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.PropertyID;

/**
 * This class represents the exceptions produced in ClassManager module when some Property was not found for some reason.
 * 
 * @version 0.1
 */
public class PropertyNotExistException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = -3933727666506741445L;

	/**
	 * This exception is produced when a property with ID provided does not exist
	 * @param propertyID
	 *            ID of the property
	 */
	public PropertyNotExistException(final PropertyID propertyID) {
		super(ERRORCODE.PROPERTY_NOT_EXIST, "Property identified by " + propertyID.getId().toString()
				+ " was not found in the database for some reason. Make sure it is correclty stored.", false);
	}

	/**
	 * This exception is produced when a property with Namespace ID, class name, and name provided does not exist
	 * @param className
	 *            Name of the class containing the Property
	 * @param propname
	 *            Name of the property.
	 */
	public PropertyNotExistException(final String className, final String propname) {
		super(ERRORCODE.PROPERTY_NOT_EXIST, "Property " + propname + " in class " + className
				+ " was not found in the database for some reason. Make sure it is correclty stored.", false);
	}
}
