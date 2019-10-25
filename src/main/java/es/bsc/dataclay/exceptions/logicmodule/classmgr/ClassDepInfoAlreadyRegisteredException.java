
/**
 * @file ClassDepInfoAlreadyRegisteredException.java
 *
 * @date Sep 18, 2012
 */

package es.bsc.dataclay.exceptions.logicmodule.classmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;

/**
 * This class represents the exceptions produced in ClassManager module when you try to register into a class language-dependant
 * information which is already registered.
 * 
 */
public class ClassDepInfoAlreadyRegisteredException extends DataClayException {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 5745970101415663066L;

	/**
	 * This exception is produced when you try to register into a class language-dependant information which is already
	 *        registered.
	 * @param language
	 *            Language of the dependant information
	 */
	public ClassDepInfoAlreadyRegisteredException(final String language) {
		super(ERRORCODE.CLASS_LANG_DEP_INFO_ALREADY_REGISTERED, "Class dependant information for "
				+ language + " already exists ", false);
	}
}
