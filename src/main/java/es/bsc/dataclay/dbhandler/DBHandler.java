
package es.bsc.dataclay.dbhandler;

import es.bsc.dataclay.util.ids.ObjectID;

/**
 * This class represents the functions that any handler of the storage of objects must implement.
 * 
 */
public interface DBHandler {

	// ==================== CREATION ====================//

	/**
	 * Store the object.
	 * 
	 * @param objectID
	 *            ID of the object
	 * @param bytes
	 *            Bytes of the object
	 */
	void store(final ObjectID objectID, final byte[] bytes);

	// ==================== GETTERS ====================//

	/**
	 * Get serialized object identified by ObjectID
	 * 
	 * @param objectID
	 *            ID of the object
	 * @return Bytes of the serialized object with ID provided.
	 */
	byte[] get(final ObjectID objectID);

	/**
	 * Check if object with ID provided exists.
	 * 
	 * @param objectID
	 *            ID of the object
	 * @return TRUE if object exists.
	 */
	boolean exists(final ObjectID objectID);

	// ==================== UPDATE ====================//

	/**
	 * Updates an object identified by the ID provided with the new values provided.
	 * 
	 * @param objectID
	 *            ID of the object.
	 * @param newbytes
	 *            New byte values
	 */
	void update(final ObjectID objectID, final byte[] newbytes);

	// ==================== DELETE ====================//

	/**
	 * Deletes and object from the database.
	 * 
	 * @param objectID
	 *            ID of the object to delete
	 */
	void delete(final ObjectID objectID);

	// ==================== QUERY ====================//

	// ==================== QUERY ====================//

	/**
	 * Verify if database is closed.
	 * 
	 * @return TRUE if database is closed. FALSE, otherwise.
	 */
	boolean isClosed();

	/**
	 * Close the database
	 * 
	 * @return TRUE if database was closed successfully. FALSE otherwise.
	 */
	boolean close();

	/**
	 * Open the database.
	 */
	void open();

}
