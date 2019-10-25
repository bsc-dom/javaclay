
/**
 * @file AnnotationDepInfoAlreadyRegisteredException.java
 *
 * @date Sep 18, 2012
 */

package es.bsc.dataclay.exceptions.logicmodule.classmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;

/**
 * This class represents the exceptions produced in ClassManager module when you try to register into a Annotation
 * language-dependant information which is already registered.
 */
public class AnnotationDepInfoAlreadyRegisteredException extends DataClayException {

	/**
	 * Generated Serial UID.
	 */
	private static final long serialVersionUID = 1385159186675107623L;

	/**
	 * This exception is produced when you try to register into a Annotation language-dependant information which is
	 *        already registered.
	 * @param language
	 *            Language of the dependant information
	 */
	public AnnotationDepInfoAlreadyRegisteredException(final String language) {
		super(ERRORCODE.PROP_LANG_DEP_INFO_ALREADY_REGISTERED, "Annotation dependant information for "
				+ language + " already exists ", false);
	}
}
