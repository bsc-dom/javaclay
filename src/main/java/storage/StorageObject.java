
package storage;

import java.util.Set;

import es.bsc.dataclay.api.BackendID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;

/**
 * This class intends to offer a basic API for DCObject.
 */
public abstract class StorageObject implements StubItf {

	// CHECKSTYLE:OFF

	public StorageObject() {
	}

	public StorageObject(String alias) {
	}

	/**
	 * @brief Get the ObjectID of the instance
	 * @return The ObjectID
	 */
	public ObjectID getObjectID() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @brief Get metaclass ID
	 * @return Class ID
	 */
	public MetaClassID getMetaClassID() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getID() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @brief Store this object into DataClay.
	 */
	public void makePersistent() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @brief Store this object into DataClay.
	 * @param optionalBackendID optional backend where the object must be stored
	 */
	public void makePersistent(final BackendID optionalBackendID) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @brief Store this object into DataClay with an alias.
	 * @param alias the alias associated to this object
	 */
	@Override
	public void makePersistent(final String alias) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @brief Store this object into DataClay.
	 * @param recursive
	 *            Indicates if all referenced objects from this objects that are not already persistent must also be stored. If
	 *            true, all referenced objects are also stored. If the object is already persistent i.e. contains a DataClay
	 *            objectID this function will fail.
	 */
	public void makePersistent(boolean recursive) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @brief Store this object into DataClay.
	 * @param recursive
	 *            Indicates if all referenced objects from this objects that are not already persistent must also be stored. If
	 *            true, all referenced objects are also stored. If the object is already persistent i.e. contains a DataClay
	 *            objectID this function will fail.
	 * @param optionalBackendID
	 *            ID of the backend in which the object should be stored. If null, any backend is accepted.
	 */
	public void makePersistent(final boolean recursive, final BackendID optionalBackendID) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @brief Store this object into DataClay.
	 * @param alias
	 *            alias for the object
	 * @param optionalBackendID
	 *            ID of the backend in which the object should be stored. If null, any backend is accepted.
	 */
	public void makePersistent(final String alias, final BackendID optionalBackendID) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @brief Store this object into DataClay.
	 * @param alias
	 *            alias for the object
	 * @param recursive
	 *            Indicates if all referenced objects from this objects that are not already persistent must also be stored. If
	 *            true, all referenced objects are also stored. If the object is already persistent i.e. contains a DataClay
	 *            objectID this function will fail.
	 */
	public void makePersistent(final String alias, final boolean recursive) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @brief Store this object into DataClay.
	 * @param alias
	 *            alias for the object
	 * @param optionalBackendID
	 *            ID of the backend in which the object should be stored. If null, any backend is accepted.
	 * @param recursive
	 *            Indicates if all referenced objects from this objects that are not already persistent must also be stored. If
	 *            true, all referenced objects are also stored. If the object is already persistent i.e. contains a DataClay
	 *            objectID this function will fail.
	 */
	public void makePersistent(final String alias, final BackendID optionalBackendID, final boolean recursive) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deletePersistent() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @brief Removes this object from DataClay. Remember that their sub-objects won't be removed.
	 * @param recursive
	 *            Indicates remove must be recursive or not
	 */
	public void deletePersistent(final boolean recursive) {
		throw new UnsupportedOperationException();
	}

	
	/**
	 * @brief Creates a new replica of this persistent object and its subobjects in a certain backend.
	 * @param recursive
	 *            Indicates if all sub-objects must be replicated as well.
	 * @return The ID of the backend in which the replica was created.
	 */
	public BackendID newReplica() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @brief Creates a new replica of this persistent object in a certain backend.
	 * @param recursive
	 *            Indicates if all sub-objects must be replicated as well.
	 * @return The ID of the backend in which the replica was created.
	 */
	public BackendID newReplica(final boolean recursive) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @brief Creates a new replica of this persistent object.
	 * @param optionalBackendID
	 *            ID of the backend in which to create the replica. If null, any backend is accepted. If the object is not
	 *            persistent i.e. does not contain a DataClay objectID this function will fail.
	 * @return The ID of the backend in which the replica was created.
	 */
	public BackendID newReplica(final BackendID optionalBackendID) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @brief Creates a new replica of this persistent object.
	 * @param optionalBackendID
	 *            ID of the backend in which to create the replica. If null, any backend is accepted. If the object is not
	 *            persistent i.e. does not contain a DataClay objectID this function will fail.
	 * @param recursive
	 *            Indicates if all sub-objects must be replicated as well.
	 * @return The ID of the backend in which the replica was created.
	 */
	public BackendID newReplica(final BackendID optionalBackendID, boolean recursive) {
		throw new UnsupportedOperationException();
	}


	/**
	 * @brief Moves a persistent object and referenced objects from the source location to the destination location specified.
	 *        If the object is not persistent i.e. does not contain a DataClay objectID this function will fail.
	 * @param srcLocID
	 *            of the source location in which the object is stored.
	 * @param destLocID
	 *            of the destination location in which the object should be moved.
	 */
	public void moveObject(final BackendID srcLocID, final BackendID destLocID) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @brief Moves a persistent object from the source location to the destination location specified. If the object is not
	 *        persistent i.e. does not contain a DataClay objectID this function will fail.
	 * @param srcLocID
	 *            of the source location in which the object is stored.
	 * @param destLocID
	 *            of the destination location in which the object should be moved.
	 * @param recursive
	 *            Indicates if all sub-objects must be moved as well.
	 */
	public void moveObject(final BackendID srcLocID, final BackendID destLocID,
			final boolean recursive) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @brief Sets this persistent object to be read only. If the object is not persistent i.e. does not contain a DataClay
	 *        objectID this function will fail.
	 */
	public void setObjectReadOnly() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @brief Sets this persistent object to be read write. If the object is not persistent i.e. does not contain a DataClay
	 *        objectID this function will fail.
	 */
	public void setObjectReadWrite() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @brief Gets the location of this persistent object.
	 * @return ID of the backend in which the object is stored. If the object is not persistent i.e. does not contain a DataClay
	 *         objectID this function will fail.
	 */
	public BackendID getLocation() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @brief Gets the location of this persistent object and its replicas.
	 * @return A set of IDs of the backend in which this object or its replicas are stored. If the object is not persistent i.e.
	 *         does not contain a DataClay objectID this function will fail.
	 */
	public Set<BackendID> getAllLocations() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @brief Execute remote method
	 * @param methodSignature
	 *            Signature of the method
	 * @param implIDAsStr
	 *            ImplementationID as string
	 * @param params
	 *            Parameters to send
	 * @return Return value.
	 */
	public Object executeRemoteImplementation(final String methodSignature, final String implIDAsStr,
			final Object[] params) {
		throw new UnsupportedOperationException();
	}
}
