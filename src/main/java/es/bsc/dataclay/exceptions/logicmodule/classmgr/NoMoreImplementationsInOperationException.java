
/**
 * @file NoMoreImplementationsInOperationException.java
 *
 * @date Nov 8, 2012
 */
package es.bsc.dataclay.exceptions.logicmodule.classmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.OperationID;

/**
 * This class represents the exceptions produced in ClassManager module when you try to remove an implementation from an
 * Operation that just have this implementation.
 * 
 * @version 0.1
 */
public class NoMoreImplementationsInOperationException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = -4446529024888192748L;

	/**
	 * This exception is produced when trying to remove the last implementation with ID provided from the operation with
	 *        ID provided.
	 * @param implementationID
	 *            ID of the implementation to remove.
	 * @param operationID
	 *            ID of the operation to remove.
	 */
	public NoMoreImplementationsInOperationException(final ImplementationID implementationID, final OperationID operationID) {
		super(ERRORCODE.NO_OTHER_IMPL_FOR_OPERATION,
				"Implementation identified by " + implementationID.getId().toString() + " is the only implementation in "
						+ " Operation identified by  " + operationID.getId().toString(),
				false);
	}
}
