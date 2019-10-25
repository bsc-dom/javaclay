
/**
 * @file DataClayRuntimeException.java
 * 
 * @date Oct 22, 2012
 */

package es.bsc.dataclay.exceptions;

import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.serialization.DataClaySerializable;

/**
 * This class represents the exceptions produced.
 */
@SuppressWarnings("serial")
public class DataClayRuntimeException extends DataClayException implements DataClaySerializable {

	/**
	 * Empty constructor used for deserialization.
	 */
	public DataClayRuntimeException() {
		super();
	}

	/**
	 * DataClayRuntimeException constructor
	 * @param theerrorCode
	 *            The error code
	 */
	public DataClayRuntimeException(final ERRORCODE theerrorCode) {
		super(theerrorCode);
	}

	/**
	 * DataClayRuntimeException constructor
	 * @param theerrorCode
	 *            The error code
	 * @param theexceptionMessage
	 *            The exception message
	 * @param extendedMsg
	 *            Whether to show and extended msg (with stack trace) or not
	 * 
	 */
	public DataClayRuntimeException(final ERRORCODE theerrorCode, final String theexceptionMessage, final boolean extendedMsg) {
		super(theerrorCode, theexceptionMessage, extendedMsg);
	}

}
