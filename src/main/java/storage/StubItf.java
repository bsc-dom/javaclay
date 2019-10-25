
/**
 * 
 */
package storage;

/**
 * @author jmarti
 * 
 */
public interface StubItf {

	/**
	 * @brief Delete persistent from stub.
	 */
	void deletePersistent();

	/**
	 * @brief Get ID in string format from stub.
	 * @return ID of the persistent object in string format
	 */
	String getID();

	/**
	 * @brief Make persistent from stub.
	 * @param alias
	 *            alias for the persistent object
	 */
	void makePersistent(final String alias);
}
