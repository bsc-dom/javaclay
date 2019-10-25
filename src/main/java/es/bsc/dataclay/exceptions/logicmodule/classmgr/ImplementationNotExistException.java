
/**
 * @file ImplementationNotExistException.java
 *
 * @date Sep 18, 2012
 */
package es.bsc.dataclay.exceptions.logicmodule.classmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.ImplementationID;

/**
 * This class represents the exceptions produced in ClassManager module when some Implementation was not found for some reason.
 * 
 */
public class ImplementationNotExistException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = -2136319536086468179L;

	/**
	 * This exception is produced when an implementation with ID provided does not exist
	 * @param implementationID
	 *            ID of the implementation
	 */
	public ImplementationNotExistException(final ImplementationID implementationID) {
		super(ERRORCODE.IMPLEMENTATION_NOT_EXIST, "Implementation identified by " + implementationID.getId().toString()
				+ " was not found in the database for some reason. Make sure it is correclty stored.", false);
	}

	public ImplementationNotExistException(final String namespace, final String classname, final String signature, final int position) {
		super(ERRORCODE.IMPLEMENTATION_NOT_EXIST,
				"Implementation of signature " + signature + " in position " + position + " of class " + classname + " in namespace " + namespace + ", "
						+ " was not found in the database for some reason. Make sure it is correclty stored.",
				false);
	}
}
