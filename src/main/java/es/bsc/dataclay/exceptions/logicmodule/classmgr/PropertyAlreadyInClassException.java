
/**
 * @file PropertyAlreadyInClassException.java
 *
 * @date Sep 18, 2012
 */
package es.bsc.dataclay.exceptions.logicmodule.classmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.MetaClassID;

/**
* This class represents the 
* exceptions produced in ClassManager module when you try to
* add a new Property to a class which already has it in the database.
* @version 0.1
*/
public class PropertyAlreadyInClassException extends DataClayException {

	/** Serial version UID.*/
	private static final long serialVersionUID = -5948465336120857395L;

	/**
	 * This exception is produced when you try to
	 * add a new Property to a class which already has it in the database.
	 * @param name Name of the property
	 * @param metaClassID ID of the class that already has the property
	 */
	public PropertyAlreadyInClassException(final String name, final MetaClassID metaClassID) {
	    super(ERRORCODE.PROPERTY_ALREADY_IN_CLASS, 
	    		"Property with name " + name + " already exists in "
	    		+ " Class identified by  "  + metaClassID.getId().toString(), false);
	}
}
