
package storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import es.bsc.dataclay.api.Backend;
import es.bsc.dataclay.api.BackendID;
import es.bsc.dataclay.api.CallbackEvent;
import es.bsc.dataclay.api.CallbackHandler;
import es.bsc.dataclay.api.DataClay;
import es.bsc.dataclay.api.DataClayException;
import es.bsc.dataclay.commonruntime.ClientRuntime;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.SessionID;
import es.bsc.dataclay.util.info.VersionInfo;
import es.bsc.dataclay.util.management.metadataservice.MetaDataInfo;
import es.bsc.dataclay.util.structs.Triple;

/**
 * This class intends to offer a basic API based on Severo Ochoa project needs.
 * 
 * @author jmarti
 */
public final class StorageItf {
	/** Indicates if debug is enabled. */
	protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** Mapping from version "ObjectID" to the original object of its version sequence. */
	private static final Map<ObjectID, VersionInfo> versions = new ConcurrentHashMap<>();

	/**
	 * @brief Forbidden constructor
	 */
	private StorageItf() {

	}

	public static void init(final String configFilePath) throws StorageException {
		try {
			DataClay.setSessionFile(configFilePath);
			DataClay.init();
		} catch (final DataClayException ex) {
			throw new StorageException(ex.getLocalizedMessage());
		}
	}

	/**
	 * @brief Create a new replica of the given object.
	 * @param objectIDstr
	 *            objectID to be replicated.
	 * @param destHost
	 *            target location of the object replica.
	 * @throws StorageException
	 *             if an exception occurs
	 */
	public static void newReplica(final String objectIDstr, final String destHost) throws StorageException {
		if (Configuration.Flags.STORAGEITF_IGNORE_REPLICATION.getBooleanValue()) {
			System.out.println("[DATACLAY] WARNING: newReplica requests are ignored");
		} else {
			try {
				DataClay.newReplica(objectIDstr, destHost);
			} catch (final DataClayException e) {
				throw new StorageException(e);
			}
		}
		return;
	}

	/**
	 * @brief Create a new version of the object in the specified hostname. If no destination is specified random one is
	 *        selected.
	 * @param objectIDstr
	 *            object id to be versioned
	 * @param preserveSource
	 *            whether the source object is preserved or otherwise can be deleted.
	 * @param optDestHost
	 *            target location for the version of the object (if null, a random location will be chosen).
	 * @return the object id of the corresponding to the new version of the object.
	 * @throws StorageException
	 *             if an exception occurs
	 */
	public static String newVersion(final String objectIDstr, final boolean preserveSource, final String optDestHost) throws StorageException {
		// TODO preserveSource is currently ignored, but we could take advantage of it (jmarti 15-09-2017)
		try {
			ClientRuntime commonLib = DataClay.getCommonLib();
			final Map<BackendID, Backend> backendsByID = DataClay.getBackends();
			Map<BackendID, Backend> backendsByIDMatchLang = null;
			// Check object language to select destination backends

			final Triple<ObjectID, BackendID, MetaClassID> ids = DataClay.string2IDandHintID(objectIDstr);
			final ObjectID originalObjectID = ids.getFirst();
			final BackendID originalHint = ids.getSecond();
			final MetaClassID originalClassID = ids.getThird();

			final VersionInfo versionInfo = commonLib.newVersion(originalObjectID,
					originalClassID, originalHint, null, optDestHost);
			if (versionInfo == null) {
				throw new StorageException("Cannot create version of object " + originalObjectID + " in " + destBackendID
						+ " with session " + commonLib.getSessionID());

			}
			final ObjectID versionOID = versionInfo.getVersionOID();
			final ObjectID previousVersionOID = versionInfo.getVersionsMapping().get(versionOID);
			boolean alreadyVersioned = false;
			if (!versions.containsKey(previousVersionOID)) {
				// No previous version of the same original object exists, add it to the versions list
				versions.put(versionOID, versionInfo);
			} else {
				// Update the version info to map from the last version to the original object
				alreadyVersioned = true;
				final Map<ObjectID, ObjectID> oldVersionToOriginal = versions.get(previousVersionOID).getVersionsMapping();
				final LinkedHashMap<ObjectID, ObjectID> newVersionToOriginal = new LinkedHashMap<>();
				for (final Entry<ObjectID, ObjectID> newToPrevious : versionInfo.getVersionsMapping().entrySet()) {
					final ObjectID previousOID = newToPrevious.getValue();
					if (oldVersionToOriginal.containsKey(previousOID)) {
						newVersionToOriginal.put(newToPrevious.getKey(), oldVersionToOriginal.get(previousOID));
					} else {
						newVersionToOriginal.put(newToPrevious.getKey(), previousOID);
					}
				}
				final VersionInfo newVersionInfo = new VersionInfo();
				newVersionInfo.setVersionOID(versionOID);
				newVersionInfo.setVersionsMapping(newVersionToOriginal);
				final Map<ObjectID, MetaDataInfo> originalMD = versions.get(previousVersionOID).getOriginalMD();
				newVersionInfo.setOriginalMD(originalMD);
				versions.put(versionOID, newVersionInfo);
				// Here we remove the previous version info, since COMPSs will always consolidate the last one.
				versions.remove(previousVersionOID);
			}

			if (DEBUG_ENABLED) {
				if (alreadyVersioned) {
					System.out.println("[DATACLAY] Object " + originalObjectID + " already versioned in " + destBackendID);
				} else {
					System.out.println("[DATACLAY] Object " + originalObjectID + " versioned in " + destBackendID);
				}
				// System.out.println("[DATACLAY] Current versions " + versions.toString());
			}

			return DataClay.ids2String(versionOID, destBackendID, originalClassID);
		} catch (final Exception ex) {
			ex.printStackTrace();
			throw new StorageException(ex);
		}
	}

	/**
	 * @brief Consolidates a specific version of an object.
	 * @param finalVersionIDstr
	 *            the final version to be consolidated with the original object.
	 * @throws StorageException
	 *             if an exception occurs
	 */
	public static void consolidateVersion(final String finalVersionIDstr) throws StorageException {
		try {
			final ObjectID versionOID = DataClay.string2IDandHintID(finalVersionIDstr).getFirst();

			final VersionInfo versionInfo = versions.get(versionOID);
			if (versionInfo == null) {
				throw new StorageException("There is no version with ID " + versionOID);
			}
			DataClay.getCommonLib().consolidateVersion(versionInfo);
			versions.remove(versionOID);

			System.out.println("[DATACLAY] Consolidated version " + versionOID);
			// System.out.println("[DATACLAY] Current versions " + versions.toString());

		} catch (final Exception ex) {
			throw new StorageException(ex);
		}
	}

	/**
	 * @brief Returns all the current versions. This method is only used for testing.
	 * @return For each version, returns the mapping to the original object and subobjects.
	 * @throws StorageException
	 *             if an exception occurs
	 */
	public static Map<ObjectID, VersionInfo> getVersions() throws StorageException {
		return versions;
	}

	/**
	 * @brief Retrieves a random backend from the given set of backends.
	 * @param possibleObjects
	 *            set of backends.
	 * @return
	 * @return the randomly selected backend.
	 */
	private static <T> T getRandom(final Collection<T> possibleObjects) {
		final int position = new Random().nextInt(possibleObjects.size());
		int i = 0;
		for (final T id : possibleObjects) {
			if (i == position) {
				return id;
			}
			i++;
		}
		return null;
	}

	/**
	 * @brief If the object is accessible, initializes an instance of a stub with the given objectID.
	 * @param objectIDstr
	 *            ID of the object
	 * @return An instance of the stub representing the given objectID
	 * @throws StorageException
	 *             if an exception occurs
	 */
	public static Object getByID(final String objectIDstr) throws StorageException {
		try {
			return DataClay.getByID(objectIDstr);
		} catch (final Exception e) {
			throw new StorageException(e);
		}
	}

	/**
	 * @brief Gets any location of an object.
	 * @param objectIDstr
	 *            object to retrieve its location
	 * @return a location of the object.
	 * @throws StorageException
	 *             if an exception occurs
	 */
	public static String getLocation(final String objectIDstr) throws StorageException {
		try {
			return DataClay.getLocation(objectIDstr);
		} catch (final Exception e) {
			throw new StorageException(e);
		}
	}

	/**
	 * @brief Gets all the locations of an object.
	 * @param objectIDstr
	 *            object to retrieve its locations.
	 * @return locations of an object.
	 * @throws StorageException
	 *             if an exception occurs
	 */
	public static List<String> getLocations(final String objectIDstr) throws StorageException {
		try {
			final List<String> result;
			if (Configuration.Flags.STORAGEITF_IGNORE_REPLICATION.getBooleanValue()) {
				final String st = DataClay.getLocation(objectIDstr);
				result = new ArrayList<>();
				result.add(st);
			} else {
				result = DataClay.getLocations(objectIDstr);
			}
			return result;
		} catch (final Exception e) {
			throw new StorageException(e);
		}
	}

	/**
	 * @brief Getter for sessionID property.
	 * @return sessionID
	 */
	public static SessionID getSessionID() {
		return DataClay.getSessionID();
	}

	/**
	 * @brief Executes a method on a specific target assynchronously.
	 * @param objectIDstr
	 *            ID of the target object.
	 * @param method
	 *            method to be executed
	 * @param params
	 *            parameters for the operation.
	 * @param callback
	 *            callback handler to communicate the result when the execution finishes.
	 * @return an id of the executed request that will receive the callback handler with the corresponding response
	 * @throws StorageException
	 *             if an exception occurs.
	 */
	public static String executeTask(final String objectIDstr, final java.lang.reflect.Method method, final Object[] params, final CallbackHandler callback)
			throws StorageException {
		try {
			return DataClay.executeTask(objectIDstr, method, params, callback);
		} catch (final Exception e) {
			throw new StorageException(e);
		}
	}

	/**
	 * @brief Executes a method on a specific target assynchronously.
	 * @param objectIDstr
	 *            ID of the target object.
	 * @param operationSignature
	 *            signature of the method to be executed.
	 * @param params
	 *            parameters for the operation.
	 * @param destHost
	 *            destination host where the method has to be executed.
	 * @param callback
	 *            callback handler to communicate the result when the execution finishes.
	 * @return an id of the executed request that will receive the callback handler with the corresponding response
	 * @throws StorageException
	 *             if an exception occurs.
	 */
	public static String executeTask(final String objectIDstr, final String operationSignature, final Object[] params,
			final String destHost, final CallbackHandler callback) throws StorageException {
		try {
			return DataClay.executeTask(objectIDstr, operationSignature, params, callback);
		} catch (final Exception e) {
			throw new StorageException(e);
		}
	}

	/**
	 * Processes and retrieves the callback event produced by a task execution.
	 * 
	 * @param callbackEvent
	 *            the event to be processed
	 * @return The task result.
	 * @throws StorageException
	 *             if any exception occurs
	 */
	public static Object getResult(final CallbackEvent callbackEvent) throws StorageException {
		try {
			return DataClay.getResult(callbackEvent);
		} catch (final Exception e) {
			throw new StorageException(e);
		}
	}

	/**
	 * @brief Finish connections to DataClay.
	 * @throws StorageException
	 *             if an exception occurs
	 */
	public static void finish() throws StorageException {
		try {
			DataClay.finish();
		} catch (final Exception e) {
			throw new StorageException(e);
		}
	}
}
