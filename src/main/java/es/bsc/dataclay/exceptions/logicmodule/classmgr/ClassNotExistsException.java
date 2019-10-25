
/**
 * @file ClassNotExistsException.java
 *
 * @date Oct 7, 2013
 */
package es.bsc.dataclay.exceptions.logicmodule.classmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.MetaClassID;

/**
* This class represents the 
* exceptions produced in ClassManager module when you try to
* retrieve a class that does not exist in the database.
*/
public class ClassNotExistsException extends DataClayException {
	
	/** Serial version UID.*/
	private static final long serialVersionUID = -4035406754468694572L;

	/**
	 * This exception is produced when a class with ID provided does not exist
	 * @param metaClassID ID of the class
	 */
	public ClassNotExistsException(final MetaClassID metaClassID) {
	    super(ERRORCODE.CLASS_NOT_EXIST, "Class with ID " + metaClassID.getId().toString() + " does not exist ", false);
	}
	
	/**
	 * This exception is produced when a class with name provided does not exist
	 * @param className Name of the class
	 */
	public ClassNotExistsException(final String className) {
	    super(ERRORCODE.CLASS_NOT_EXIST, "Class " + className + " does not exist ", false);
	}
}
