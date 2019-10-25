
package es.bsc.dataclay.exceptions.logicmodule.accountmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;

/**
 * This class represents the exceptions produced in AccountManager module when invalid Credentials are provided: wrong
 * association Account-Credential...
 * 
 * @version 0.1
 */
public class InvalidCredentialsException extends DataClayException {

	/** Serial Version UID. */
	private static final long serialVersionUID = 4316939550863767680L;

	/**
	 * This Exception is called when some credentials are invalid for some reason
	 * @param msg
	 *            Message to show
	 */
	public InvalidCredentialsException(final String msg) {
		super(ERRORCODE.INVALID_CREDENTIALS, msg, false);
	}
}
