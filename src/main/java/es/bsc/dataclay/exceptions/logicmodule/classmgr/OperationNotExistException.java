
/**
 * @file OperationNotExistException.java
 *
 * @date Sep 18, 2012
 */
package es.bsc.dataclay.exceptions.logicmodule.classmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.OperationID;

/**
 * This class represents the exceptions produced in ClassManager module when some Operation was not found for some reason.
 * 
 * @version 0.1
 */
public class OperationNotExistException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = -5978910373986524770L;

	/**
	 * This exception is produced when an operation with ID provided does not exist
	 * @param operationID
	 *            ID of the operation
	 */
	public OperationNotExistException(final OperationID operationID) {
		super(ERRORCODE.OPERATION_NOT_EXIST, "Operation identified by " + operationID.getId().toString()
				+ " was not found in the database for some reason. Make sure it is correclty stored.", false);
	}

	/**
	 * This exception is produced when an operation with namespace, class name and name provided does not exist
	 * @param metaClassName
	 *            Name of the class that should contain the operation
	 * @param operationSignature
	 *            Signature of the operation
	 */
	public OperationNotExistException(final String metaClassName,
			final String operationSignature) {
		super(ERRORCODE.OPERATION_NOT_EXIST, "Operation " + operationSignature
				+ " in class named " + metaClassName
				+ " was not found in the database for some reason. Make sure it is correclty stored.", false);
	}

}
