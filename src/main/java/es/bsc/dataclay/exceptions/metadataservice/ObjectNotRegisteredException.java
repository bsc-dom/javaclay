
/**
 * @file ObjectNotRegisteredException.java
 * 
 * @date May 28, 2013
 */
package es.bsc.dataclay.exceptions.metadataservice;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.ObjectID;

/**
 * This class represents the exceptions produced in MetaDataService module when some object specified is not registered.
 * 
 */
public class ObjectNotRegisteredException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = -6451431733093674376L;

	/**
	 * This exception is produced when some object is not registered
	 * @param objectID
	 *            ID of the object requested
	 */
	public ObjectNotRegisteredException(final ObjectID objectID) {
		super(ERRORCODE.OBJECT_NOT_EXIST, "Object " + objectID.toString() + " is not registered", false);
	}

	/**
	 * This exception is produced when asking for an object with specific alias and it does not exist.
	 * @param alias
	 *            alias of the object requested
	 */
	public ObjectNotRegisteredException(final String alias) {
		super(ERRORCODE.OBJECT_NOT_EXIST, "No object with alias " + alias, false);
	}

}
