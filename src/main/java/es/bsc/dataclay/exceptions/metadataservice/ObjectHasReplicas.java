
/**
 * @file ObjectHasReplicas.java
 * 
 * @date Jun 13, 2013
 */
package es.bsc.dataclay.exceptions.metadataservice;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.ObjectID;

/**
 * This class represents the exceptions produced in MetaDataService module when some object has replicas.
 * 
 */
public class ObjectHasReplicas extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = -6504656633936364278L;

	/**
	 * This exception is produced when some object has replicas.
	 * @param objectID
	 *            ID of the object
	 */
	public ObjectHasReplicas(final ObjectID objectID) {
		super(ERRORCODE.OBJECT_HAS_REPLICAS, "Object " + objectID.toString() + " has replicas", false);
	}

}
