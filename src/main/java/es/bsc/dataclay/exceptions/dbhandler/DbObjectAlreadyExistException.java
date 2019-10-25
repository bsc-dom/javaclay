
/**
 * @file DbObjectAlreadyExistException.java
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
public class DbObjectAlreadyExistException extends DataClayException {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 1604579619852152262L;

	/**
	 * Exception produced in DbHandler module when no Object is found in a query.
	 * @param objectID
	 *            ID of the object
	 */
	public DbObjectAlreadyExistException(final ID objectID) {
		super(ERRORCODE.OBJECT_EXISTS_IN_DB, "Object with ID: "
				+ objectID.getId().toString() + " already stored", false);
	}

}
