package es.bsc.dataclay.api;

/**
 * This class represents the exception to be reported when an error occurs in dataClay.
 * 
 */
public class DataClayException extends Exception {
	/** Generated serial version UID. */
	private static final long serialVersionUID = 6182015160913789272L;

	/**
	 * Default constructor.
	 */
	public DataClayException() {
		super();
	}

	/**
	 * Constructor from exception.
	 * 
	 * @param ex
	 *            base exception.
	 */
	public DataClayException(final Exception ex) {
		super(ex);
	}

	/**
	 * Constructor with msg.
	 * 
	 * @param msg
	 *            Message explaining the exception.
	 */
	public DataClayException(final String msg) {
		super(msg);
	}
}
