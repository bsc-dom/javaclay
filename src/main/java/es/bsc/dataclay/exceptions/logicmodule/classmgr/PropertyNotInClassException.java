
/**
 * @file PropertyNotInClassException.java
 * 
 * @date Jun 18, 2013
 */
package es.bsc.dataclay.exceptions.logicmodule.classmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.PropertyID;

/**
 * This class represents the exceptions produced in ClassManager module when some Property was not found in some class for some
 * reason.
 * 
 * @version 0.1
 */
public class PropertyNotInClassException extends DataClayException {
	/** Serial version UID. */
	private static final long serialVersionUID = 6500902529816380407L;

	/**
	 * This exception is produced when some Property was not found in some class for some reason.
	 * @param propertyID
	 *            ID of the property
	 * @param metaClassID
	 *            ID of the metaclass
	 */
	public PropertyNotInClassException(final PropertyID propertyID, final MetaClassID metaClassID) {
		super(ERRORCODE.PROPERTY_NOT_IN_CLASS, "Property identified by " + propertyID.getId().toString()
				+ " was not found in class " + metaClassID.getId().toString(), false);
	}

}
