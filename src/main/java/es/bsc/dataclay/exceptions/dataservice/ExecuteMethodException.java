
/**
 * @file ExecuteMethodException.java
 *
 * @date Nov 12, 2012
 */
package es.bsc.dataclay.exceptions.dataservice;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;

/**
 * This class represents the exceptions produced in DataService module when you try to invoke a method.
 * 
 */
public class ExecuteMethodException extends DataClayException {

	/** Serial Version UID. */

	private static final long serialVersionUID = -7321563005526861669L;

	/**
	 * This exception contains all errors produced in a Java Reflection invoke call.
	 * @param errorMessage
	 *            Error message produced.
	 */
	public ExecuteMethodException(final String errorMessage) {
		super(ERRORCODE.EXECUTE_METHOD_ERROR, "Error produced while trying to execute a method: "
				+ errorMessage, true);
	}

}
