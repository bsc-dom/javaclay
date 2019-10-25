
/**
 * @file ClassAlreadyExistsException.java
 *
 * @date Sep 18, 2012
 */

package es.bsc.dataclay.exceptions.logicmodule.classmgr;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;

/**
 * This class represents the exceptions produced in ClassManager module when you try to do some type of operation and the
 * RunTime doesn't recognize or doesn't support the language code.
 * 
 */
public class UnsupportedLanguage extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = 6926180767110638969L;

	/**
	 * This exception is produced when a class with name provided already exists
	 * @param lang
	 *            The language unsupported for the ongoing operation
	 */
	public UnsupportedLanguage(final Langs lang) {
		super(ERRORCODE.CLASS_UNSUPPORTED_LANGUAGE, "Language #" + lang.ordinal()
				+ " (" + lang.name() + ") is unsupported", false);
	}
}
