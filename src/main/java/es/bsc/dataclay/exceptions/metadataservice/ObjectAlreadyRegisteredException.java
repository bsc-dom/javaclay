
/**
 * @file ObjectAlreadyRegisteredException.java
 * 
 * @date May 28, 2013
 */
package es.bsc.dataclay.exceptions.metadataservice;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;

/**
 * This class represents the exceptions produced in MetaDataService module when some object specified is already registered.
 * 
 */
public class ObjectAlreadyRegisteredException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = 3916887391434457843L;

	/**
	 * This exception is produced when some object is already registered
	 * @param objectID
	 *            ID of the object
	 */
	public ObjectAlreadyRegisteredException(final ObjectID objectID) {
		super(ERRORCODE.OBJECT_ALREADY_REGISTERED, "Object " + objectID.toString() + " is already registered", false);
	}

	/**
	 * This exception is produced when some object is already registered
	 * @param alias
	 *            Alias of the object
	 * @param classID
	 *            Class ID of the object
	 */
	public ObjectAlreadyRegisteredException(final MetaClassID classID, final String alias) {
		super(ERRORCODE.OBJECT_ALREADY_REGISTERED, "There is an object with alias " + alias + " of class " + classID + " already registered", false);
	}
}
