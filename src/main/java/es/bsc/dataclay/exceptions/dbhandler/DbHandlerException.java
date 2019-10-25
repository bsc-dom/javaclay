
/**
 * @file DbHandlerException.java
 *
 * @date Oct 2, 2012
 */
package es.bsc.dataclay.exceptions.dbhandler;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;

/**
 * This class represents the exceptions produced in DbHandler module.
 * 
 * @version 0.1
 */
public class DbHandlerException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = 3884853912275870660L;

	/**
	 * Exception produced in the backend
	 * @param e
	 *            Exception
	 */
	public DbHandlerException(final Exception e) {
		super(ERRORCODE.DB_EXCEPTION, e.toString(), true);
	}

	/**
	 * Exception produced in the backend with a message
	 * @param msg
	 *            Message to show
	 */
	public DbHandlerException(final String msg) {
		super(ERRORCODE.DB_EXCEPTION, msg, true);
	}
}
