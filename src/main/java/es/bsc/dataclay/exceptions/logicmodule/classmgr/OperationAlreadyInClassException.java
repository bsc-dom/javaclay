
/**
 * @file OperationAlreadyInClassException.java
 *
 * @date Sep 18, 2012
 */
package es.bsc.dataclay.exceptions.logicmodule.classmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;

/**
* This class represents the 
* exceptions produced in ClassManager module when you try to
* add a new Operation to a class which already has it in the database.
* @version 0.1
*/
public final class OperationAlreadyInClassException extends DataClayException {

	/** Serial version UID.*/
	private static final long serialVersionUID = 5339589954223522361L;

	/** ID of the namespace of the operation that is already in class.*/
	private NamespaceID originalNamespaceIDofOperation;
	/** ID of the metaclass of the operation that is already in class.*/
	private MetaClassID metaClassIDofOperation;
	/**
	 * This exception is produced when you try to
	 * add a new Operation to a class which already has it in the database.
	 * @param operation Name of the operation 
	 * @param originalNamespaceID ID of the namespace of the operation
	 * @param metaClassID ID of the Metaclass that already contains the operation. 
	 */
	public OperationAlreadyInClassException(final String operation, final NamespaceID originalNamespaceID, 
			final MetaClassID metaClassID) {
	    super(ERRORCODE.OPERATION_ALREADY_IN_CLASS,
	    		"Operation " + operation + " already exists in "
	    		+ " Class identified by  "  + metaClassID.getId().toString(), false);
	    this.originalNamespaceIDofOperation = originalNamespaceID;
	    this.metaClassIDofOperation = metaClassID;
	}
	
	/**
	 * Get the ID of the namespace of the operation
	 * @return ID of the namespace of the operation
	 */
	public NamespaceID getOriginalNamespaceIDofOperation() {
		return this.originalNamespaceIDofOperation;
	}
	
	/**
	 * Get the ID of the metaclass of the operation
	 * @return ID of the metaclass of the operation
	 */
	public MetaClassID getMetaClassIDofOperation() {
		return this.metaClassIDofOperation;
	}
}
