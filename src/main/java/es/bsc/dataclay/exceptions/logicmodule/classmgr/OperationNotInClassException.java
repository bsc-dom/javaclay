
/**
 * @file OperationNotInClassException.java
 *
 * @date Jun 18, 2013
 */
package es.bsc.dataclay.exceptions.logicmodule.classmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.OperationID;

/**
* This class represents the 
* exceptions produced in ClassManager module when some operation
* was not found in some class for some reason. 
* @version 0.1
*/
public class OperationNotInClassException extends DataClayException {

	/** Serial version UID.*/
	private static final long serialVersionUID = 1250423478994564847L;

	/**
	 * This exception is produced when some operation
	 * was not found in some class for some reason. 
	 * @param operationID ID of the operation
	 * @param metaClassID ID of the metaclass
	 */
	public OperationNotInClassException(final OperationID operationID, final MetaClassID metaClassID) {
	    super(ERRORCODE.OPERATION_NOT_IN_CLASS, "Operation identified by " + operationID.getId().toString()
	    		+ " was not found in class " + metaClassID.getId().toString(), false);
	}
	
	
	
}
