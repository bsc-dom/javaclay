
/**
 * @file GeneralException.java
 * 
 * @date Oct 22, 2012
 */

package es.bsc.dataclay.exceptions;

/**
 * This class represents the exceptions produced.
 */
@SuppressWarnings("serial")
public class DataClayClassNotFoundException extends RuntimeException {

	/**
	 * Empty constructor.
	 */
	public DataClayClassNotFoundException() {
		super(null, null, false, false);
	}

	public DataClayClassNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
