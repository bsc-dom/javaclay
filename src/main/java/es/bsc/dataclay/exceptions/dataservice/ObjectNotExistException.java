
/**
 * @file ObjectNotExistException.java
 *
 * @date Nov 12, 2012
 */
package es.bsc.dataclay.exceptions.dataservice;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.ObjectID;

/**
* This class represents the 
* exceptions produced in DataService module when you look for
* an ObjectData that does not exist in the database.
*/
public class ObjectNotExistException extends DataClayException {


	/** Serial Version UID.*/
	private static final long serialVersionUID = -2685410664603921504L;

	/**
	 * This exception is produced when a object with ID provided does not exist
	 * @param objectID ID of the object
	 */
	public ObjectNotExistException(final ObjectID objectID) {
	    super(ERRORCODE.OBJECT_NOT_EXIST, "Object with ID " + objectID.getId().toString() + " does not exist ", false);
	}
	
	/**
	 * This exception is produced when a object with ID provided does not exist
	 * @param objectID ID of the object
	 */
	public ObjectNotExistException(final long objectID) {
	    super(ERRORCODE.OBJECT_NOT_EXIST, "Object with ID " + objectID + " does not exist ", false);
	}
	
}
