
/**
 * @file StorageException.java
 */
package storage;

/**
 * This class represents the exception to be reported when an error occurs in Severo storage lib.
 * 
 * @author jmarti
 */
public class StorageException extends Exception {
	/** Generated serial version UID. */
	private static final long serialVersionUID = 6182015160913789272L;

	/**
	 * @brief Default constructor.
	 */
	public StorageException() {
		super();
	}

	/**
	 * @brief Constructor from exception.
	 * @param ex
	 *            base exception.
	 */
	public StorageException(final Exception ex) {
		super(ex);
	}

	/**
	 * @brief Constructor with msg.
	 * @param msg
	 *            Message explaining the exception.
	 */
	public StorageException(final String msg) {
		super(msg);
	}
}
