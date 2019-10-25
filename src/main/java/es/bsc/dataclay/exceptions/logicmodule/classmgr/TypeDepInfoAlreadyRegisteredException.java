
/**
 * @file TypeDepInfoAlreadyRegisteredException.java
 *
 * @date Sep 18, 2012
 */

package es.bsc.dataclay.exceptions.logicmodule.classmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;

/**
 * This class represents the exceptions produced in ClassManager module when you try to register into a Type language-dependant
 * information which is already registered.
 * 
 */
public class TypeDepInfoAlreadyRegisteredException extends DataClayException {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -1827977793046927734L;

	/**
	 * This exception is produced when you try to register into a Type language-dependant information which is already
	 *        registered.
	 * @param language
	 *            Language of the dependant information
	 */
	public TypeDepInfoAlreadyRegisteredException(final String language) {
		super(ERRORCODE.TYPE_LANG_DEP_INFO_ALREADY_REGISTERED, "Type dependant information for "
				+ language + " already exists ", false);
	}
}
