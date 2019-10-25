
/**
 * @file PropertyDepInfoAlreadyRegisteredException.java
 *
 * @date Sep 18, 2012
 */

package es.bsc.dataclay.exceptions.logicmodule.classmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;

/**
 * This class represents the exceptions produced in ClassManager module when you try to register into a Property
 * language-dependant information which is already registered.
 * 
 */
public class PropertyDepInfoAlreadyRegisteredException extends DataClayException {

	/**
	 * Generated Serial UID.
	 */
	private static final long serialVersionUID = 1385159186674107628L;

	/**
	 * This exception is produced when you try to register into a Property language-dependant information which is
	 *        already registered.
	 * @param language
	 *            Language of the dependant information
	 */
	public PropertyDepInfoAlreadyRegisteredException(final String language) {
		super(ERRORCODE.PROP_LANG_DEP_INFO_ALREADY_REGISTERED, "Property dependant information for "
				+ language + " already exists ", false);
	}
}
