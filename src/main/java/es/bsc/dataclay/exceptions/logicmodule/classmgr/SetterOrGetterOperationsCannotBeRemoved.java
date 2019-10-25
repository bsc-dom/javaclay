
/**
 * @file SetterOrGetterOperationsCannotBeRemoved.java
 *
 * @date Nov 28, 2013
 */
package es.bsc.dataclay.exceptions.logicmodule.classmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.OperationID;

/**
* This class represents the exceptions produced in ClassManager module when trying to remove a default
* getter or setter operation.
* @version 0.1
*/
public class SetterOrGetterOperationsCannotBeRemoved extends DataClayException {

	/** Serial version UID.*/
	private static final long serialVersionUID = 2879113392456982243L;

	/**
	 * This exception is produced when an operation with ID provided does not exist
	 * @param operationID ID of the operation
	 */
	public SetterOrGetterOperationsCannotBeRemoved(final OperationID operationID) {
	    super(ERRORCODE.DEFAULT_GETTER_SETTER_OPERATION_CANNOT_BE_REMOVED, "Operation identified by " 
	    		+ operationID.getId().toString() + " is a default getter or setter, cannot be removed.", false);
	}	
}
