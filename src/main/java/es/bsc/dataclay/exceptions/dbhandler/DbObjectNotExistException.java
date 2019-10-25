
/**
 * @file DbObjectNotExistException.java
 *
 * @date Oct 2, 2012
 */
package es.bsc.dataclay.exceptions.dbhandler;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.ID;

/**
 * This class represents the exception produced in DbHandler module when no Object is found in a query.
 * 
 * @version 0.1
 */
public class DbObjectNotExistException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = 6807261493252435154L;

	/**
	 * Exception produced in DbHandler module when no Object is found in a query.
	 * @param objectID
	 *            ID of the object
	 */
	public DbObjectNotExistException(final ID objectID) {
		super(ERRORCODE.OBJECT_NOT_EXISTS_IN_DB, "Object with ID: "
				+ objectID.getId().toString() + " cannot be found", false);
	}

	/**
	 * Exception produced in DbHandler module when no Object is found in a query.
	 */
	public DbObjectNotExistException() {
		super(ERRORCODE.OBJECT_NOT_EXISTS_IN_DB, "There is no object matching example provided", false);
	}

}
