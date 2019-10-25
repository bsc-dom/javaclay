
/**
 * @file GeneralException.java
 * 
 * @date Oct 22, 2012
 */

package es.bsc.dataclay.exceptions;

import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;

/**
 * This class represents the exceptions produced in Execution Environment.
 */
@SuppressWarnings("serial")
public abstract class LanguageExecutionException extends DataClayException {

	/**
	 * Constructor used for deserialization
	 */
	protected LanguageExecutionException() {
		super(ERRORCODE.LANGUAGE_EXECUTION_EXCEPTION);
	}

	/**
	 * Constructor
	 * 
	 * @param theLangException
	 *            Language exception
	 */
	protected LanguageExecutionException(final Throwable theLangException) {
		super(theLangException);
	}

}
