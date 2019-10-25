
/**
 * @file OperationDepInfoAlreadyRegisteredException.java
 *
 * @date Sep 18, 2012
 */

package es.bsc.dataclay.exceptions.logicmodule.classmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;

/**
 * This class represents the exceptions produced in ClassManager module when you try to
 * register into a Operation language-dependant information which is already registered.
 */
public class OperationDepInfoAlreadyRegisteredException extends DataClayException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1154238458148711070L;

	/**
	 * This exception is produced when you try to
	 * register into a Operation language-dependant information which is already registered.
	 * @param language Language of the dependant information
	 */
	public OperationDepInfoAlreadyRegisteredException(final String language) {
		super(ERRORCODE.OP_LANG_DEP_INFO_ALREADY_REGISTERED, "Operation dependant information for " 
				+ language + " already exists ", false);
	}
}
