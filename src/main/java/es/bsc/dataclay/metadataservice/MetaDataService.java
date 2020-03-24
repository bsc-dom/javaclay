
/**
 * @file MetaDataService.java
 * @date May 21, 2013
 */
package es.bsc.dataclay.metadataservice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.commonruntime.DataClayRuntime;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectAlreadyExistException;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectNotExistException;
import es.bsc.dataclay.exceptions.metadataservice.AliasAlreadyInUseException;
import es.bsc.dataclay.exceptions.metadataservice.ExecutionEnvironmentAlreadyExistsException;
import es.bsc.dataclay.exceptions.metadataservice.ExecutionEnvironmentNotExistException;
import es.bsc.dataclay.exceptions.metadataservice.ExternalDataClayNotRegisteredException;
import es.bsc.dataclay.exceptions.metadataservice.MultipleAliasesException;
import es.bsc.dataclay.exceptions.metadataservice.ObjectAlreadyRegisteredException;
import es.bsc.dataclay.exceptions.metadataservice.ObjectHasReplicas;
import es.bsc.dataclay.exceptions.metadataservice.ObjectNotRegisteredException;
import es.bsc.dataclay.exceptions.metadataservice.StorageLocationAlreadyExistsException;
import es.bsc.dataclay.exceptions.metadataservice.StorageLocationNotExistException;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.StorageLocationID;
import es.bsc.dataclay.util.management.AbstractManager;
import es.bsc.dataclay.util.management.metadataservice.DataClayInstance;
import es.bsc.dataclay.util.management.metadataservice.ExecutionEnvironment;
import es.bsc.dataclay.util.management.metadataservice.MetaDataInfo;
import es.bsc.dataclay.util.management.metadataservice.StorageLocation;
import es.bsc.dataclay.util.structs.LruCache;
import es.bsc.dataclay.util.structs.Tuple;

/**
 * This class is responsible to manage information related to an object (metaclassID of the object, backend in which is stored,
 * interface and contract used for creating the object...).
 */
public final class MetaDataService extends AbstractManager {
	private static final Logger logger = LogManager.getLogger("MetaDataService");

	/** Indicates if debug is enabled. */
	protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** Object cache. */
	private final LruCache<ObjectID, ObjectMetaData> objectMDCache;

	/** Federated objects cache. */
	private final LruCache<ObjectID, Set<DataClayInstanceID>> federatedObjectsCache;

	/** Object by Alias cache. */
	private final LruCache<String, ObjectMetaData> objectMDCacheByAlias;

	/** Cache for Storage Location mapping. */
	private final ConcurrentHashMap<StorageLocationID, StorageLocation> currentStorageLocations;

	/** Current Execution Environments mappings. */
	private final ConcurrentHashMap<ExecutionEnvironmentID, ExecutionEnvironment> currentExecutionEnvironments;
	private final ConcurrentHashMap<String, ExecutionEnvironmentID> currentJavaExecutionEnvironments;
	private final ConcurrentHashMap<String, ExecutionEnvironmentID> currentPythonExecutionEnvironments;

	/** External dataClay info cache. */
	private final LruCache<DataClayInstanceID, DataClayInstance> externalDataClaysCache;

	/** External dataClay IDs per host-port cache. */
	private final LruCache<Tuple<String, Integer>, DataClayInstanceID> dataClaysPerHostPortCache;

	/** MetaDataService database. */
	private final MetaDataServiceDB metadataDB;

	/**
	 * Instantiates an MetaDataService that uses the Backend configuration provided.
	 * 
	 * @param managerName
	 *            Manager/service name.
	 * @post Creates MetaDataService and initializes the backend.
	 */
	public MetaDataService(final BasicDataSource dataSource) {
		super(dataSource);
		metadataDB = new MetaDataServiceDB(dataSource);
		metadataDB.createTables();

		// Init caches
		this.objectMDCache = new LruCache<>(Configuration.Flags.MAX_ENTRIES_METADATASERVICE_CACHE.getIntValue());
		this.objectMDCacheByAlias = new LruCache<>(Configuration.Flags.MAX_ENTRIES_METADATASERVICE_CACHE.getIntValue());
		this.externalDataClaysCache = new LruCache<>(Configuration.Flags.MAX_ENTRIES_METADATASERVICE_CACHE.getIntValue());
		this.dataClaysPerHostPortCache = new LruCache<>(Configuration.Flags.MAX_ENTRIES_METADATASERVICE_CACHE.getIntValue());


		this.currentStorageLocations = new ConcurrentHashMap<>();
		this.currentExecutionEnvironments = new ConcurrentHashMap<>();
		this.currentJavaExecutionEnvironments = new ConcurrentHashMap<>();
		this.currentPythonExecutionEnvironments = new ConcurrentHashMap<>();
		this.federatedObjectsCache = new LruCache<>(
				Configuration.Flags.MAX_ENTRIES_METADATASERVICE_CACHE.getIntValue());
	}

	/**
	 * Checks whether an objects exists or not.
	 * 
	 * @param objectID
	 *            ID of the object to be checked.
	 * @return True if the objectID is registered in the service, false otherwise.
	 * @throws Exception
	 *             if any exception occurs
	 */
	public boolean existsObject(final ObjectID objectID) {
		// Check cache
		ObjectMetaData objectMD = objectMDCache.get(objectID);
		if (objectMD == null) {
			// Query by example
			objectMD = metadataDB.getByID(objectID);
			if (objectMD != null) {
				// Update cache if necessary
				if (DEBUG_ENABLED) {
					logger.debug("Updating cache of metadatas: {} -> {}", objectID, objectMD);
				}
				objectMDCache.put(objectID, objectMD);
				final String alias = objectMD.getAlias();
				objectMDCacheByAlias.put(alias, objectMD);
			}
		}
		return (objectMD != null);
	}

	/**
	 * This operation gets all the backend IDs that contain the object with ID provided.
	 * 
	 * @param objectID
	 *            ID of the object to query
	 * @return The set of backends corresponding to those containing a replica of the object specified. The result can be empty.
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             ObjectNotRegisteredException: if the object is not registered.
	 */
	public Map<ExecutionEnvironmentID, ExecutionEnvironment> getObjectBackends(final ObjectID objectID) {
		// Query object info
		final ObjectMetaData objectMD = getObjectBasicMetaData(objectID);
		if (objectMD == null) {
			// TODO: Need new design for this. Since getObjectBasicMetadata is in critical
			// path, it is returning
			// null instead of exception to avoid penalty for serializing an exception.
			throw new ObjectNotRegisteredException(objectID);
		}
		// Prepare result
		final Map<ExecutionEnvironmentID, ExecutionEnvironment> objectMDbackends = new ConcurrentHashMap<>();

		for (final ExecutionEnvironmentID curEntry : objectMD.getExecutionEnvironmentIDs()) {
			final ExecutionEnvironment backend = getExecutionEnvironment(curEntry);
			objectMDbackends.put(curEntry, backend);
		}
		return objectMDbackends;
	}

	/**
	 * This operation retrieves the metadata of an object.
	 * 
	 * @param objectID
	 *            ID of the object
	 * @return the metadata of the given object
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             ObjectNotRegisteredException: if the object is not registered
	 */
	public MetaDataInfo getObjectMetaData(final ObjectID objectID) {
		// Query object info
		final ObjectMetaData objectMD = getObjectBasicMetaData(objectID);
		if (objectMD == null) {
			return null; // do not send exception to avoid serializing it, critical path.
		}

		final MetaDataInfo result = buildMetaDataInfo(objectMD);
		return result;
	}

	/**
	 * Get all objects registered in system
	 * @return ids of all registered objects
	 */
	public Set<ObjectID> getAllObjectIDsRegistered() {
		return metadataDB.getAllObjectIDs();
	}

	/**
	 * Get object ID from alias
	 * 
	 * @param metaClassID
	 *            ID of the class of the object
	 * @param alias
	 *            Alias of the object
	 * @return the ID of the object if it is found and its metadata
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             ObjectNotRegisteredException: if the object is not registered
	 */
	public Tuple<ObjectID, MetaDataInfo> getObjectInfoFromAlias(final String alias) {
		ObjectMetaData objectMD = objectMDCacheByAlias.get(alias);
		if (objectMD != null) {
			final MetaDataInfo metadataInfo = buildMetaDataInfo(objectMD);
			if (DEBUG_ENABLED) {
				logger.debug("Get by alias: Obtained object " + objectMD.getDataClayID() + " from alias: " + alias);
			}
			return new Tuple<>(objectMD.getDataClayID(), metadataInfo);
		} else {
			if (DEBUG_ENABLED) {
				logger.debug("Alias " + alias + " not found in cache.");
			}
		}
		objectMD = metadataDB.getByAlias(alias);
		if (objectMD == null) {
			throw new ObjectNotRegisteredException(alias);
		}

		final MetaDataInfo metadataInfo = buildMetaDataInfo(objectMD);
		if (DEBUG_ENABLED) {
			logger.debug("Get by alias: Obtained object " + objectMD.getDataClayID() + " from alias: " + alias);
		}
		return new Tuple<>(objectMD.getDataClayID(), metadataInfo);
	}

	/**
	 * Delete alias of object
	 * 
	 * @param alias
	 *            Alias of the object
	 * @return ID of the object
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             ObjectNotRegisteredException: if the object is not registered
	 */
	public ObjectID deleteAlias(final String alias) {
		ObjectMetaData objectMD = objectMDCacheByAlias.remove(alias);
		if (objectMD == null) {
			if (DEBUG_ENABLED) {
				logger.debug("Alias " + alias + " not found in cache.");
			}

			objectMD = metadataDB.getByAlias(alias);

			if (objectMD == null) {
				throw new ObjectNotRegisteredException(alias);
			}
		}

		objectMD.setAlias(null);

		try {
			metadataDB.updateAliasByID(objectMD.getDataClayID(), null);
		} catch (final DbObjectNotExistException e) {
			// Ignore exception, maybe the object has been garbage collected
		}

		if (DEBUG_ENABLED) {
			logger.debug("Delete Alias: removed alias " + alias + " in object " + objectMD.getDataClayID());
		}
		return objectMD.getDataClayID();
	}

	/**
	 * Returns information about the objects instantiating a given class.
	 * 
	 * @param classID
	 *            the id of the class
	 * @return a map indexed by the object ids with their info
	 */
	public Map<ObjectID, MetaDataInfo> getObjectsOfSpecificClass(final MetaClassID classID) {
		final Map<ObjectID, MetaDataInfo> result = new ConcurrentHashMap<>();

		final ArrayList<ObjectMetaData> objects = metadataDB.getByClass(classID);
		for (final ObjectMetaData objectMD : objects) {
			final MetaDataInfo mdInfo = buildMetaDataInfo(objectMD);
			result.put(objectMD.getDataClayID(), mdInfo);
		}
		return result;
	}

	/**
	 * This operation registers the MetaData of an object.
	 * 
	 * @param objectID
	 *            ID of the object
	 * @param metaClassID
	 *            ID of the class of the object
	 * @param datasetIDofProvider
	 *            ID of the dataset where the object is created
	 * @param backendIDs
	 *            IDs of the backeds where the object is stored (replicas)
	 * @param isReadOnly
	 *            whether the object is readonly or not
	 * @param aliases
	 *            aliases for the object
	 * @param lang
	 *            Object language
	 * @param ownerID
	 *            Owner account ID
	 * @return MetaDataInfo of the registered object
	 * @throws ObjectAlreadyRegisteredException
	 *             if the object was already registered
	 * @throws AliasAlreadyInUseException if some of the aliases provided already exist
	 */
	public MetaDataInfo registerObject(final ObjectID objectID, final MetaClassID metaClassID,
			final DataSetID datasetIDofProvider, final Set<ExecutionEnvironmentID> backendIDs, final boolean isReadOnly,
			final String alias, final Langs lang, final AccountID ownerID)
					throws ObjectAlreadyRegisteredException, AliasAlreadyInUseException {

		ObjectID newObjectID = objectID;

		if (alias != null) {
			// Check there is no other object with same class and alias
			try {
				this.getObjectInfoFromAlias(alias);
				throw new AliasAlreadyInUseException(alias);
			} catch (final ObjectNotRegisteredException e) {
				// it's ok, object not registered
			}

			newObjectID = DataClayRuntime.getObjectIDFromAlias(alias);
		}



		final ObjectMetaData objectMD = new ObjectMetaData(newObjectID, metaClassID, datasetIDofProvider, backendIDs,
				isReadOnly, alias, lang, ownerID);

		if (DEBUG_ENABLED) {
			logger.debug("Registering object " + objectID + " with alias: " + alias + " and language " + lang + ".");
		}

		try {
			metadataDB.store(objectMD);
		} catch (final DbObjectAlreadyExistException e) {
			if (DEBUG_ENABLED) {
				logger.debug("Object " + objectID + " already registered");
			}
			throw new ObjectAlreadyRegisteredException(objectID);
		}

		final ObjectID result = objectMD.getDataClayID(); // which now equals to objectID

		// Update cache
		if (DEBUG_ENABLED) {
			logger.debug("[==Register object==] Updating cache of metadatas: {} -> {}", objectID, objectMD);
		}
		objectMDCache.put(result, objectMD);
		objectMDCacheByAlias.put(alias, objectMD);
		return buildMetaDataInfo(objectMD);
	}

	/**
	 * This operation allows to explicitly register an objectID replacement. If newObjectID is already registered, an exception
	 * is raised and the old ID is kept.
	 * 
	 * @param oldObjectID
	 *            original ID of the object
	 * @param newObjectID
	 *            final ID for the object
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             ObjectNotRegisteredException: if object does not exist ObjectAlreadyRegisteredException: if new id is already
	 *             being used
	 */
	public void changeObjectID(final ObjectID oldObjectID, final ObjectID newObjectID)
			throws ObjectNotRegisteredException, ObjectAlreadyRegisteredException {
		ObjectMetaData objectMD = null;

		// Query object info
		objectMD = getObjectBasicMetaData(oldObjectID);
		if (objectMD == null) {
			// TODO: Need new design for this. since getObjectBasicMetadata is in critical
			// path, it is returning
			// null instead of exception to avoid penalty for serializing an exception.
			throw new ObjectNotRegisteredException(oldObjectID);
		}

		// Store new one
		objectMD.setDataClayID(newObjectID);
		try {
			metadataDB.store(objectMD);
		} catch (final DbObjectAlreadyExistException e) {
			objectMD.setDataClayID(oldObjectID);
			throw new ObjectAlreadyRegisteredException(newObjectID);
		}

		// Delete old object if new one was stored correctly
		metadataDB.deleteByID(oldObjectID);

		// Update in Cache
		if (DEBUG_ENABLED) {
			logger.debug("[==Change objectID==] Removing from cache of metadatas: {}", oldObjectID);
		}
		objectMDCache.remove(oldObjectID);
		if (DEBUG_ENABLED) {
			logger.debug("[==Change objectID==] Adding to cache of metadatas: {} -> {}", newObjectID, objectMD);
		}
		objectMDCache.put(newObjectID, objectMD);

		// Since objectID is not Key for Alias cache, just update it
		final String alias = objectMD.getAlias();
		objectMDCacheByAlias.put(alias, objectMD);
	}

	/**
	 * This operation allows to explicitly register an dataSetID replacement.
	 * 
	 * @param objectID
	 *            original ID of the object
	 * @param newDataSetID
	 *            final DataSetID for the object
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             ObjectNotRegisteredException: if object does not exist
	 */
	public void changeDataSetID(final ObjectID objectID, final DataSetID newDataSetID)
			throws ObjectNotRegisteredException {

		// Query object info
		final ObjectMetaData objectMD = getObjectBasicMetaData(objectID);
		if (objectMD == null) {
			// TODO: Need new design for this. since getObjectBasicMetadata is in critical
			// path, it is returning
			// null instead of exception to avoid penalty for serializing an exception.
			throw new ObjectNotRegisteredException(objectID);
		}
		// Update it
		try {
			metadataDB.updateDataSetIDByID(objectID, newDataSetID);
		} catch (final DbObjectNotExistException e) {
			throw new ObjectNotRegisteredException(objectID);
		}

		// Update cache if necessary
		if (objectMDCache.containsKey(objectID)) {
			objectMDCache.get(objectID).setDataSetID(newDataSetID);
			final String alias = objectMD.getAlias();
			objectMDCacheByAlias.put(alias, objectMDCache.get(objectID));
		}
	}

	/**
	 * Registers a set of versions, copying parts of the metadata from the original objects
	 * 
	 * @param versionToOriginalMapping
	 *            Mapping from version objectID to original objectID
	 * @param backendID
	 *            Backend in which the version is stored
	 * @param lang
	 *            Language doing the operation (should match the object's one)
	 * @return the metadata of the original objects (it will be required for consolidate)
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             ObjectNotRegisteredException: if an object does not exist
	 */
	public Map<ObjectID, MetaDataInfo> registerVersions(final Map<ObjectID, ObjectID> versionToOriginalMapping,
			final ExecutionEnvironmentID backendID, final Langs lang) throws ObjectNotRegisteredException {
		final Map<ObjectID, MetaDataInfo> result = new ConcurrentHashMap<>();
		for (final Entry<ObjectID, ObjectID> versionToOriginal : versionToOriginalMapping.entrySet()) {
			final ObjectID versionID = versionToOriginal.getKey();
			final ObjectID originalID = versionToOriginal.getValue();

			// Get metadata of the original object and put it in the result
			final MetaDataInfo originalMD = getObjectMetaData(originalID);
			result.put(originalID, originalMD);

			// Register version metadata
			final Set<ExecutionEnvironmentID> backends = new HashSet<>();
			backends.add(backendID);
			final ObjectMetaData versionMD = new ObjectMetaData(versionID, originalMD.getMetaclassID(),
					originalMD.getDatasetID(), backends, false, null, lang, originalMD.getOwnerID());

			metadataDB.store(versionMD);

			// Store versionMD in the cache
			if (DEBUG_ENABLED) {
				logger.debug("[==New version==] Adding version to cache of metadatas: {} -> {}", versionID, versionMD);
			}
			objectMDCache.put(versionID, versionMD);
			final String alias = versionMD.getAlias();
			objectMDCacheByAlias.put(alias, versionMD);
		}
		return result;
	}

	/**
	 * This operation registers a new replica of an object
	 * 
	 * @param objectID
	 *            the object ID
	 * @param newBackendID
	 *            the location of the new replica
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             ObjectNotRegisteredException: if object does not exist ExecutionEnvironmentNotExistException: if destination
	 *             backend does not exist
	 */
	public void registerReplica(final ObjectID objectID, final ExecutionEnvironmentID newBackendID)
			throws ObjectNotRegisteredException, ExecutionEnvironmentNotExistException {

		// Verify dest. backend exists
		final boolean backendExists = metadataDB.existsByID(newBackendID);
		if (!backendExists) {
			throw new ExecutionEnvironmentNotExistException(newBackendID);
		}

		// Query object info
		final ObjectMetaData objectMD = getObjectBasicMetaData(objectID);
		if (objectMD == null) {
			// TODO: Need new design for this. since getObjectBasicMetadata is in critical
			// path, it is returning
			// null instead of exception to avoid penalty for serializing an exception.
			throw new ObjectNotRegisteredException(objectID);
		}
		// CHECKSTYLE:OFF
		// Check it is not present in destBackend
		if (objectMD.getExecutionEnvironmentIDs().contains(newBackendID)) {
			// We do nothing in this case, since we want to replicate all the possible
			// objects, i.e. if we find a replica
			// of a subobject in the backend, go to the next subobject instead of throwing
			// exception.
			// throw new ReplicaAlreadyRegisteredException(objectID, newBackendID);
			// CHECKSTYLE:ON
		} else {
			// Migrate
			try {
				final Set<ExecutionEnvironmentID> newBackendIDs = new HashSet<>(objectMD.getExecutionEnvironmentIDs());
				newBackendIDs.add(newBackendID);
				metadataDB.updateForReplicaByID(objectID, newBackendIDs, true);
			} catch (final DbObjectNotExistException e) {
				throw new ObjectNotRegisteredException(objectID);
			}

			// Update cache if necessary
			if (objectMDCache.containsKey(objectID)) {
				objectMDCache.get(objectID).getExecutionEnvironmentIDs().add(newBackendID);
				objectMDCache.get(objectID).setReadOnly(true);
				final String alias = objectMD.getAlias();
				objectMDCacheByAlias.put(alias, objectMDCache.get(objectID));
			}
		}

	}

	/**
	 * This operation unregisters an object with ID provided. This also unregisters all replicas and returns its locations.
	 * 
	 * @param objectID
	 *            ID of the object to unregister
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             ObjectNotRegisteredException: if object does not exist
	 */
	public void unregisterObject(final ObjectID objectID) throws ObjectNotRegisteredException {

		if (DEBUG_ENABLED) {
			logger.debug("Unregistering " + objectID);
		}

		// Query object info
		final ObjectMetaData objectMD = getObjectBasicMetaData(objectID);
		if (objectMD == null) {
			// TODO: Need new design for this. since getObjectBasicMetadata is in critical
			// path, it is returning
			// null instead of exception to avoid penalty for serializing an exception.
			throw new ObjectNotRegisteredException(objectID);
		}
		// Unregister object
		metadataDB.deleteByID(objectID);

		// Update cache if necessary
		objectMDCache.remove(objectID);
		final String alias = objectMD.getAlias();
		objectMDCacheByAlias.remove(alias);
	}

	/**
	 * Checks that given dataset has no object.
	 * 
	 * @param datasetID
	 *            the ID of the dataset to be checked
	 * @return Whether the dataset is empty or not.
	 * @throws Exception
	 *             if an exception occurs.
	 */
	public boolean checkDatasetIsEmpty(final DataSetID datasetID) {
		return !metadataDB.existsByDataSetID(datasetID);
	}

	/**
	 * This operation verifies that the object with ID provided exists in the backend with ID provided as source backend and not
	 * in the destination backend.
	 * 
	 * @param objectID
	 *            ID of the object
	 * @param srcBackend
	 *            ID of the source backend
	 * @param destBackend
	 *            ID of the destination backend
	 * @return TRUE if the object exists in the source backend and not in the destination backend. FALSE otherwise.
	 * @throws Exception
	 *             if an exception occurs ObjectNotRegisteredException: if the object does not exist
	 */
	public boolean checkObjectInSrcNotInDest(final ObjectID objectID, final ExecutionEnvironmentID srcBackend,
			final ExecutionEnvironmentID destBackend) throws ObjectNotRegisteredException {
		// Query object info
		final ObjectMetaData objectMD = getObjectBasicMetaData(objectID);
		if (objectMD == null) {
			// TODO: Need new design for this. since getObjectBasicMetadata is in critical
			// path, it is returning
			// null instead of exception to avoid penalty for serializing an exception.
			throw new ObjectNotRegisteredException(objectID);
		}
		final Set<ExecutionEnvironmentID> backendIDs = objectMD.getExecutionEnvironmentIDs();
		return backendIDs.contains(srcBackend) && !backendIDs.contains(destBackend);
	}

	/**
	 * This operation moves an object from source location to destination location
	 * 
	 * @param objectID
	 *            ID of the object to modify
	 * @param srcBackendID
	 *            ID of the backend of the replica to be moved
	 * @param destBackendID
	 *            ID of the backend in which the object is 'moved'
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             ObjectNotRegisteredException: if the object does not exist or it is not present in source backend
	 *             ExecutionEnvironmentNotExistException: if destination backend does not exist
	 */
	public void migrateObjectToBackend(final ObjectID objectID, final ExecutionEnvironmentID srcBackendID,
			final ExecutionEnvironmentID destBackendID)
					throws ObjectNotRegisteredException, ExecutionEnvironmentNotExistException {

		// Verify dest. backend exists
		if (!currentExecutionEnvironments.containsKey(destBackendID)) {
			final ExecutionEnvironment backend = metadataDB.getByID(destBackendID);
			if (backend == null) {
				throw new ExecutionEnvironmentNotExistException(destBackendID);
			}

			// Update cache
			currentExecutionEnvironments.put(destBackendID, backend);
			if (backend.getLang() == Langs.LANG_JAVA) {
				currentJavaExecutionEnvironments.put(backend.getAddress(), backend.getDataClayID());
			} else if (backend.getLang() == Langs.LANG_PYTHON) {
				currentPythonExecutionEnvironments.put(backend.getAddress(), backend.getDataClayID());
			}
		}

		// Query object info
		final ObjectMetaData objectMD = getObjectBasicMetaData(objectID);
		if (objectMD == null) {
			// TODO: Need new design for this. since getObjectBasicMetadata is in critical
			// path, it is returning
			// null instead of exception to avoid penalty for serializing an exception.
			throw new ObjectNotRegisteredException(objectID);
		}
		final Set<ExecutionEnvironmentID> backendIDs = objectMD.getExecutionEnvironmentIDs();

		// Check it is in srcBackend
		if (!backendIDs.contains(srcBackendID)) {
			if (DEBUG_ENABLED) {
				logger.debug("[==MOVE==] Migrating metadata FAILED of " + objectID + " from " + srcBackendID + " to "
						+ destBackendID + " due not registered in source.");
			}

			throw new ObjectNotRegisteredException(objectID);
		} else {
			// Check it is not present in destBackend
			if (backendIDs.contains(destBackendID)) {
				if (DEBUG_ENABLED) {
					logger.debug("[==MOVE==] Migrating metadata FAILED of " + objectID + " from " + srcBackendID
							+ " to " + destBackendID + " due already registered in destination.");
				}

				throw new ObjectAlreadyRegisteredException(objectID);
			} else {
				migrateObjectToBackend(objectID, objectMD, srcBackendID, destBackendID);
			}
		}
	}

	/**
	 * This operations removes the srcBackendID from the locations of the specified objects and adds the corresponding new
	 * location from newObjBackends to each of them. If unregisterBackend is set to true, the srcBackendID is unregistered.
	 * WARNING: This function is not checking whether the srcBackendID becomes totally empty after migrating specified
	 * objects!!! For that, we would need an structure to keep which objects has every backend
	 * 
	 * @param srcBackendID
	 *            ID of the unregistered backend from which objects are migrated
	 * @param newObjBackends
	 *            the objects for every new destination backend
	 * @param unregisterBackend
	 *            whether to unregister the srcBackendID or not
	 * @return TRUE if the backend has been successfully unregistered. FALSE otherwise.
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             ExecutionEnvironmentNotExistException: if backend does not exist
	 */
	public boolean migrateObjectsToBackend(final ExecutionEnvironmentID srcBackendID,
			final Map<ExecutionEnvironmentID, Set<ObjectID>> newObjBackends, final boolean unregisterBackend)
					throws ExecutionEnvironmentNotExistException {

		// Verify dest. backends exist
		for (final ExecutionEnvironmentID destBackendID : newObjBackends.keySet()) {
			// Verify dest. backend exists
			if (!currentExecutionEnvironments.containsKey(destBackendID)) {
				final ExecutionEnvironment backend = metadataDB.getByID(destBackendID);
				if (backend == null) {
					throw new ExecutionEnvironmentNotExistException(destBackendID);
				}

				// Update cache
				currentExecutionEnvironments.put(destBackendID, backend);
				if (backend.getLang() == Langs.LANG_JAVA) {
					currentJavaExecutionEnvironments.put(backend.getAddress(), backend.getDataClayID());
				} else if (backend.getLang() == Langs.LANG_PYTHON) {
					currentPythonExecutionEnvironments.put(backend.getAddress(), backend.getDataClayID());
				}
			}
		}

		// Register migrations
		for (final Entry<ExecutionEnvironmentID, Set<ObjectID>> curEntry : newObjBackends.entrySet()) {
			final ExecutionEnvironmentID destBackendID = curEntry.getKey();

			// Query objects info
			for (final ObjectID objectID : curEntry.getValue()) {
				final ObjectMetaData curObjectMetaData = getObjectBasicMetaData(objectID);
				if (curObjectMetaData == null) {
					// TODO: Need new design for this. since getObjectBasicMetadata is in critical
					// path, it is returning
					// null instead of exception to avoid penalty for serializing an exception.
					throw new ObjectNotRegisteredException(objectID);
				}
				migrateObjectToBackend(objectID, curObjectMetaData, srcBackendID, destBackendID);
			}
		}

		// Unregister backend if required
		if (unregisterBackend) {
			try {
				metadataDB.deleteByID(srcBackendID);

				// Update cache
				currentExecutionEnvironments.remove(srcBackendID);

				return true;
			} catch (final DbObjectNotExistException e) {
				throw new ExecutionEnvironmentNotExistException(srcBackendID);
			}
		}
		return false;
	}

	/**
	 * This operation modifies the permissions of the object with ID provided to read-only
	 * 
	 * @param objectID
	 *            ID of the object
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             ObjectNotRegisteredException: if the object does not exist
	 */
	public void setObjectReadOnly(final ObjectID objectID) throws ObjectNotRegisteredException {
		// Query object info
		final ObjectMetaData objectMD = getObjectBasicMetaData(objectID);
		if (objectMD == null) {
			// TODO: Need new design for this. since getObjectBasicMetadata is in critical
			// path, it is returning
			// null instead of exception to avoid penalty for serializing an exception.
			throw new ObjectNotRegisteredException(objectID);
		}
		if (!objectMD.isReadOnly()) {
			// Update it
			try {
				metadataDB.updateReadOnlyByID(objectID, true);

			} catch (final DbObjectNotExistException e) {
				throw new ObjectNotRegisteredException(objectID);
			}
		}

		// Update cache if necessary
		if (objectMDCache.containsKey(objectID)) {
			objectMDCache.get(objectID).setReadOnly(true);
			final String alias = objectMD.getAlias();
			objectMDCacheByAlias.put(alias, objectMDCache.get(objectID));
		}

	}

	/**
	 * This operation modifies the permissions of the object with ID provided to read-write
	 * 
	 * @param objectID
	 *            ID of the object
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             ObjectNotRegisteredException: if the object does not exist <br>
	 *             ObjectHasReplicas: if the object has replicas
	 */
	public void setObjectReadWrite(final ObjectID objectID) throws ObjectNotRegisteredException {
		// Query object info
		final ObjectMetaData objectMD = getObjectBasicMetaData(objectID);
		if (objectMD == null) {
			// TODO: Need new design for this. since getObjectBasicMetadata is in critical
			// path, it is returning
			// null instead of exception to avoid penalty for serializing an exception.
			throw new ObjectNotRegisteredException(objectID);
		}
		// WARNING: in 2.0 we assume that if an object has replicas it MUST be READ-ONLY
		// (jmarti 13 Jun 2013)
		if (objectMD.getExecutionEnvironmentIDs().size() > 1) {
			throw new ObjectHasReplicas(objectID);
		} else {
			if (objectMD.isReadOnly()) {
				// Update it
				try {
					metadataDB.updateReadOnlyByID(objectID, false);

				} catch (final DbObjectNotExistException e) {
					throw new ObjectNotRegisteredException(objectID);
				}
			}
		}

		// Update cache if necessary
		if (objectMDCache.containsKey(objectID)) {
			objectMDCache.get(objectID).setReadOnly(false);
			final String alias = objectMD.getAlias();
			objectMDCacheByAlias.put(alias, objectMDCache.get(objectID));
		}
	}

	// =========== OPS FOR BACKEND MANAGEMENT ============ //

	/**
	 * This operation register the Storage Location with ID provided.
	 * 
	 * @param newStLoc
	 *            Specifications of the Storage Location to register
	 * @return The ID of the new Storage Location registered
	 * @throws StorageLocationAlreadyExistsException
	 *             if the storage location is already registered
	 */
	public StorageLocationID registerStorageLocation(final StorageLocation newStLoc)
			throws StorageLocationAlreadyExistsException {
		// Verify backend does not exist

		final StorageLocation stLocation = metadataDB.getStorageLocationByName(newStLoc.getName());

		if (stLocation != null) {
			throw new StorageLocationAlreadyExistsException(newStLoc);
		}
		if (DEBUG_ENABLED) {
			logger.debug("Registering new backend: " + newStLoc);
		}

		// Store backend
		metadataDB.store(newStLoc);

		// Update cache
		currentStorageLocations.put(newStLoc.getDataClayID(), newStLoc);
		return newStLoc.getDataClayID();
	}

	/**
	 * Unregisters a storage location
	 * 
	 * @param stLocID
	 *            id of the storage location to be unregistered
	 */
	public void unregisterStorageLocation(final StorageLocationID stLocID) {
		metadataDB.deleteByID(stLocID);
	}

	/**
	 * Updates host and port of a storage location
	 * 
	 * @param stLocID
	 *            id of the storage location to be updated
	 * @param newhost New host 
	 * @param newport New port
	 */
	public void updateStorageLocation(final StorageLocationID stLocID, final String newhost, final Integer newport) {
		metadataDB.updateStorageLocationByID(stLocID, newhost, newport);
	}

	/**
	 * Updates host and port of a execution environment
	 * 
	 * @param id
	 *            id of the execution environment to be updated
	 * @param newhost New host 
	 * @param newport New port
	 */
	public void updateExecutionEnvironment(final ExecutionEnvironmentID eeID, final String newhost, final Integer newport) {
		metadataDB.updateExecutionEnvironmentByID(eeID, newhost, newport);
	}

	/**
	 * This operation register the Execution Environment with ID provided.
	 * 
	 * @param newBackend
	 *            Specifications of the Execution Environment to register
	 * @return The ID of the new Execution Environment registered
	 * @throws ExecutionEnvironmentAlreadyExistsException
	 *             if the execution environment is already registered
	 */
	public ExecutionEnvironmentID registerExecutionEnvironment(final ExecutionEnvironment newBackend)
			throws ExecutionEnvironmentAlreadyExistsException {
		// Verify backend does not exist
		final ExecutionEnvironment exeEnv = metadataDB.getByID(newBackend.getDataClayID());

		if (exeEnv != null) {
			throw new ExecutionEnvironmentAlreadyExistsException(newBackend);
		}
		if (DEBUG_ENABLED) {
			logger.debug("Registering new backend: " + newBackend);
		}

		// Store backend
		metadataDB.store(newBackend);

		// Update cache
		currentExecutionEnvironments.put(newBackend.getDataClayID(), newBackend);
		if (newBackend.getLang() == Langs.LANG_JAVA) {
			currentJavaExecutionEnvironments.put(newBackend.getAddress(), newBackend.getDataClayID());
		} else if (newBackend.getLang() == Langs.LANG_PYTHON) {
			currentPythonExecutionEnvironments.put(newBackend.getAddress(), newBackend.getDataClayID());
		}
		return newBackend.getDataClayID();
	}

	/**
	 * Unregisters an execution environment
	 * 
	 * @param execEnvID
	 *            id of the storage location to be unregistered
	 */
	public void unregisterExecutionEnvironment(final ExecutionEnvironmentID execEnvID) {
		metadataDB.deleteByID(execEnvID);
	}

	/**
	 * This operation queries a Storage Location by its name
	 * 
	 * @param stLocName
	 *            Name of the Storage Location to get
	 * @return The ID of the queried Storage Location
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             StorageLocationNotExistException: if a storage location with the given name does not exist
	 */
	public StorageLocationID getStorageLocationID(final String stLocName) {

		if (DEBUG_ENABLED) {
			logger.debug("Looking for Storage Location named: " + stLocName);
		}

		// Look up in the cache
		for (final Entry<StorageLocationID, StorageLocation> stLocEntry : currentStorageLocations.entrySet()) {
			if (stLocEntry.getValue().getName().equals(stLocName)) {
				return stLocEntry.getKey();
			}
		}

		// If it is not in the cache, query it
		final StorageLocation res = metadataDB.getStorageLocationByName(stLocName);
		if (res == null) {
			throw new StorageLocationNotExistException(stLocName);
		} else {
			// Update cache
			currentStorageLocations.put(res.getDataClayID(), res);
		}

		return res.getDataClayID();
	}

	/**
	 * This operation retrieves the info of a storage location
	 * 
	 * @param storageLocationID
	 *            ID of the storage location
	 * @return the info of the storage location
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             StorageLocationNotExistException: if the storage location does not exist
	 */
	public StorageLocation getStorageLocationInfo(final StorageLocationID storageLocationID) {
		final StorageLocation result = getStorageLocation(storageLocationID);
		return result;
	}

	/**
	 * This operation retrieves the info of all the current registered storage locations.
	 * 
	 * @return info of the registered storage locations indexed by their IDs
	 */
	public Map<StorageLocationID, StorageLocation> getAllStorageLocationsInfo() {

		final List<StorageLocation> backends = metadataDB.getAllStorageLocations();
		final Map<StorageLocationID, StorageLocation> result = new ConcurrentHashMap<>();
		for (final StorageLocation backend : backends) {
			result.put(backend.getDataClayID(), backend);
		}
		return result;
	}

	/**
	 * This operation retrieves the info of an execution environment
	 * 
	 * @param execEnvID
	 *            ID of the execution environment to get its info
	 * @return the info of the backend
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             ExecutionEnvironmentNotExistException: if backend does not exist
	 */
	public ExecutionEnvironment getExecutionEnvironmentInfo(final ExecutionEnvironmentID execEnvID) {
		final ExecutionEnvironment result = getExecutionEnvironment(execEnvID);
		return result;
	}

	/**
	 * This operation retrieves the info of all the current registered execution environments.
	 * @param execEnvLang language
	 * @return info of the registered execution environments indexed by their IDs
	 */
	public Map<ExecutionEnvironmentID, ExecutionEnvironment> getAllExecutionEnvironmentsInfo(final Langs execEnvLang) {
		final List<ExecutionEnvironment> execEnvs;
		if (execEnvLang == Langs.LANG_NONE || execEnvLang == null) {
			execEnvs = metadataDB.getAllExecutionEnvironments();
		} else {
			execEnvs = metadataDB.getAllExecutionEnvironmentsByLang(execEnvLang);
		}
		final Map<ExecutionEnvironmentID, ExecutionEnvironment> result = new ConcurrentHashMap<>();

		for (final ExecutionEnvironment execEnv : execEnvs) {
			if (execEnv.getLang().equals(execEnvLang)) {
				result.put(execEnv.getDataClayID(), execEnv);
			}
		}
		return result;
	}

	/**
	 * Retrieves a random execution environment
	 * 
	 * @return the execution environment spec
	 */
	public Tuple<ExecutionEnvironmentID, ExecutionEnvironment> getRandomExecutionEnvironmentInfo(
			final Langs execEnvLang) {

		// Get random backend from registered ones
		ExecutionEnvironmentID backendID = null;
		if (execEnvLang == Langs.LANG_JAVA) {
			if (!currentJavaExecutionEnvironments.isEmpty()) {
				final int index = new Random().nextInt(currentJavaExecutionEnvironments.size());
				backendID = (ExecutionEnvironmentID) currentJavaExecutionEnvironments.values().toArray()[index];
			}
		} else if (execEnvLang == Langs.LANG_PYTHON) {
			if (!currentPythonExecutionEnvironments.isEmpty()) {
				final int index = new Random().nextInt(currentPythonExecutionEnvironments.size());
				backendID = (ExecutionEnvironmentID) currentPythonExecutionEnvironments.values().toArray()[index];
			}
		}
		if (backendID == null) {
			final ExecutionEnvironment backend;
			final List<ExecutionEnvironment> backends = metadataDB.getAllExecutionEnvironmentsByLang(execEnvLang);
			if (backends.isEmpty()) {
				throw new ExecutionEnvironmentNotExistException(execEnvLang);
			}
			final int index = new Random().nextInt(backends.size());
			backend = backends.get(index);

			// Update cache
			for (final ExecutionEnvironment curBackend : backends) {
				currentExecutionEnvironments.put(curBackend.getDataClayID(), curBackend);
				if (execEnvLang == Langs.LANG_JAVA) {
					currentJavaExecutionEnvironments.put(curBackend.getAddress(), curBackend.getDataClayID());
				} else if (execEnvLang == Langs.LANG_PYTHON) {
					currentPythonExecutionEnvironments.put(curBackend.getAddress(), curBackend.getDataClayID());
				}
			}

			return new Tuple<>(backend.getDataClayID(), backend);
		} else {
			return new Tuple<>(backendID, getExecutionEnvironmentInfo(backendID));
		}
	}

	// ===== OBEJCT FEDERATION OPS ======= //

	/**
	 * Register external dataclay
	 * @param dataClay instance to register
	 */
	public void registerExternalDataclay(final DataClayInstance dataClay) {
		try {

			metadataDB.insertDataClayInstance(dataClay);

			// update cache 
			final DataClayInstance dClayInfo = metadataDB.getDataClayInfo(dataClay.getDcID());
			externalDataClaysCache.put(dataClay.getDcID(), dClayInfo);

			for (int i = 0; i < dClayInfo.getHosts().length; ++i) { 
				final String host = dClayInfo.getHosts()[i];
				final Integer port = dClayInfo.getPorts()[i];
				final Tuple<String, Integer> key = new Tuple<>(host, port);
				final DataClayInstanceID id = dataClaysPerHostPortCache.get(key);
				if (id == null) {
					dataClaysPerHostPortCache.put(key, dataClay.getDcID());
				}
			}
		} catch (final DbObjectAlreadyExistException ex1) {
			if (DEBUG_ENABLED) {
				logger.debug("External dataClay {} already registered", dataClay);
			}
			return;
		}
	}

	/**
	 * Unregister external dataClay address
	 * @param host Host
	 * @param port Port
	 */
	public void unregisterExternalDataClayAddress(final String host, final Integer port) {
		try {

			metadataDB.deleteDataClayInstance(host, port);
		} catch (final Exception ex1) {
			if (DEBUG_ENABLED) {
				logger.debug("External dataClay at {}:{} already unregistered: ", host, port, ex1);
			}
			return;
		}
	}

	/**
	 * Retrieves information about an external dataClay instance identified by provided id
	 * 
	 * @param extDataClayID
	 *            id of the external dataClay instance
	 * @return info of external dataClay instance
	 * @throws ExternalDataClayNotRegisteredException
	 *             if no info about dataClay instance is registered
	 */
	public DataClayInstance getExternalDataClayInfo(final DataClayInstanceID extDataClayID)
			throws ExternalDataClayNotRegisteredException {

		DataClayInstance dClayInfo = externalDataClaysCache.get(extDataClayID);
		if (dClayInfo == null) {
			dClayInfo = metadataDB.getDataClayInfo(extDataClayID);
			if (dClayInfo == null) {
				throw new ExternalDataClayNotRegisteredException(extDataClayID);
			}
			externalDataClaysCache.put(extDataClayID, dClayInfo);
		}
		return dClayInfo;
	}

	/**
	 * Retrieves id of an external dataClay instance identified by host and port
	 * 
	 * @param host host 
	 * @param port port
	 * @return id of external dataClay instance
	 * @throws ExternalDataClayNotRegisteredException
	 *             if no info about dataClay instance is registered
	 */
	public DataClayInstanceID getExternalDataClayID(final String host, final int port)
			throws ExternalDataClayNotRegisteredException {

		final Tuple<String, Integer> key = new Tuple<>(host, port);
		DataClayInstanceID id = dataClaysPerHostPortCache.get(key);
		if (id == null) {
			id = metadataDB.getDataClayID(host, port);
			if (id == null) {
				throw new ExternalDataClayNotRegisteredException(host, port);
			}
			dataClaysPerHostPortCache.put(key, id);
		}
		return id;
	}

	/**
	 * Registers an object to be federated with an external dataClay identified with the given ID
	 * 
	 * @param objectID
	 *            id of the object to be federated
	 * @param dataClayID
	 *            id of the external dataClay
	 * @return false if the object cannot be federated, either because it was already federated or dataClayID is not registered
	 *         true otherwise.
	 */
	public boolean federateObjectWith(final ObjectID objectID, final DataClayInstanceID dataClayID) {
		Set<DataClayInstanceID> dcIDs = federatedObjectsCache.get(objectID);
		if (dcIDs != null) {
			if (dcIDs.contains(dataClayID)) {
				return false;
			}
		}

		// Check object exists
		final ObjectMetaData objectMD = getObjectBasicMetaData(objectID);
		if (objectMD == null) {
			// TODO: Need new design for this. since getObjectBasicMetadata is in critical
			// path, it is returning
			// null instead of exception to avoid penalty for serializing an exception.
			throw new ObjectNotRegisteredException(objectID);
		}

		// Get external dataClay info
		final DataClayInstance di = getExternalDataClayInfo(dataClayID);
		if (di == null) {
			return false;
		}
		if (!metadataDB.insertFederatedObject(objectID, dataClayID)) {
			return false;
		}
		dcIDs = federatedObjectsCache.get(objectID);
		if (dcIDs == null) {
			dcIDs = new HashSet<>();
			federatedObjectsCache.put(objectID, dcIDs);
		}
		dcIDs.add(dataClayID);
		return true;
	}

	/**
	 * Unfederate object with an external dataClay identified with the given ID
	 * 
	 * @param objectID
	 *            id of the object to be unfederated
	 * @param dataClayID
	 *            id of the external dataClay
	 * @return false if the object cannot be unfederated, either because it was not federated or others. true otherwise.
	 */
	public boolean unfederateObjectWith(final ObjectID objectID, final DataClayInstanceID dataClayID) {

		// Check object exists
		final ObjectMetaData objectMD = getObjectBasicMetaData(objectID);
		if (objectMD == null) {
			// TODO: Need new design for this. since getObjectBasicMetadata is in critical
			// path, it is returning
			// null instead of exception to avoid penalty for serializing an exception.
			throw new ObjectNotRegisteredException(objectID);
		}

		// Get external dataClay info
		final DataClayInstance di = getExternalDataClayInfo(dataClayID);
		if (di == null) {
			return false;
		}

		if (!metadataDB.deleteFederatedObject(objectID, dataClayID)) {
			// object already unfederated
			return false;
		}

		federatedObjectsCache.remove(objectID);

		return true;
	}

	/**
	 * Checks whether the object is actually federated with dataClay instance identified with given ID
	 * 
	 * @param objectID
	 *            id of the object
	 * @param dataClayID
	 *            id of the external dataClay instance
	 * @return true if the object is federated. false otherwise.
	 */
	public boolean checkIsFederatedWith(final ObjectID objectID, final DataClayInstanceID dataClayID) {
		Set<DataClayInstanceID> dcIDs = federatedObjectsCache.get(objectID);
		if (dcIDs != null) {
			if (dcIDs.contains(dataClayID)) {
				return true;
			}
		}
		final boolean result = metadataDB.existsFederatedObjectWithDC(objectID, dataClayID);
		if (result) {
			dcIDs = federatedObjectsCache.get(objectID);
			if (dcIDs == null) {
				dcIDs = new HashSet<>();
				federatedObjectsCache.put(objectID, dcIDs);
			}
			dcIDs.add(dataClayID);
		}
		return result;
	}

	/**
	 * Get all dataClays object provided is federated with
	 * @param objectID ID of the object 
	 * @return dataClay IDs the object is federated with
	 */
	public Set<DataClayInstanceID> getDataClaysOurObjectIsFederatedWith(final ObjectID objectID) {
		return metadataDB.getDataClaysOurObjectIsFederatedWith(objectID);
	}

	/**
	 * Get all dataClays current dataClay has objects federated with
	 * @return dataClay IDs current dataClay has objects federated with
	 */
	public Set<DataClayInstanceID> getAllExternalDataClays() {
		return metadataDB.getAllExternalDataClays();
	}

	/**
	 * Registers an object that is federated from an external dataClay instance.
	 * 
	 * @param objectID
	 *            ID of the federated object in the external dataClay
	 * @param dataClayID
	 *            id of the external source dataClay
	 */
	public void registerExternalObject(final ObjectID objectID, final DataClayInstanceID dataClayID) {
		// Check external dataClay is registered
		getExternalDataClayInfo(dataClayID);

		metadataDB.insertExternalObject(objectID, dataClayID, false);
	}

	/**
	 * Unregisters an object that is federated from an external dataClay instance.
	 * 
	 * @param objectID
	 *            ID of the federated object in the external dataClay
	 */
	public void unregisterExternalObject(final ObjectID objectID) {
		metadataDB.deleteExternalObject(objectID);
	}

	/**
	 * Checks if the object is actually a federated object
	 * 
	 * @param objectID
	 *            id of the object
	 * @return TRUE if object is external.
	 */
	public boolean existsExternalObject(final ObjectID objectID) {
		return metadataDB.existsExternalObject(objectID, true) 
				|| metadataDB.existsExternalObject(objectID, false);
	}

	/**
	 * Checks if the object is actually a federated object with unregistered flag = false
	 * 
	 * @param objectID
	 *            id of the object
	 * @return TRUE if object is external.
	 */
	public boolean externalObjectIsRegistered(final ObjectID objectID) {
		return metadataDB.existsExternalObject(objectID, false);
	}

	/**
	 * Checks if the object is actually a federated object with unregistered flag = true
	 * 
	 * @param objectID
	 *            id of the object
	 * @return TRUE if object is external.
	 */
	public boolean externalObjectIsUnregistered(final ObjectID objectID) {
		return metadataDB.existsExternalObject(objectID, true);
	}

	/**
	 * Update external object to be marked as unregistered 
	 * 
	 * @param objectID
	 *            ID of the object to update
	 * @throws DbObjectNotExistException
	 *             if object does not exist
	 */
	public void markExternalObjectAsUnregistered(final ObjectID objectID) { 
		this.metadataDB.updateUnregisteredFlagExternalObject(objectID, true);
	}

	/**
	 * Update external object to be marked as registered 
	 * 
	 * @param objectID
	 *            ID of the object to update
	 * @throws DbObjectNotExistException
	 *             if object does not exist
	 */
	public void markExternalObjectAsRegistered(final ObjectID objectID) { 
		this.metadataDB.updateUnregisteredFlagExternalObject(objectID, false);
	}

	/**
	 * Get unregistered external objects.
	 * @return id of external objects unregistered
	 */
	public Set<ObjectID> getUnregisteredExternalObjects() {
		return metadataDB.getUnregisteredExternalObjects();
	}

	/**
	 * Method that retrieves the info of external source dataClay of this object
	 * 
	 * @param objectID
	 *            id of the object
	 * @return info of the external source dataClay
	 */
	public DataClayInstanceID getExternalSourceDataClayOfObject(final ObjectID objectID) {
		return metadataDB.getExternalDataClayOfObject(objectID);
	}

	/**
	 * Method that retrieves all the objects federated/belonging to dataClay with ID provided.
	 * 
	 * @param extDataClayInstanceID
	 *            id of dataclay
	 * @return all the objects federated/belonging to dataClay with ID provided.
	 */
	public Set<ObjectID> getObjectsFederatedWithDataClay(final DataClayInstanceID extDataClayInstanceID) {
		return metadataDB.getObjectsFederatedWithDataClay(extDataClayInstanceID);
	}

	// ========= PRIVATE FUNCTIONS ========= //

	/**
	 * Builds a MetaDataInfo structure from ObjectMetaData
	 * 
	 * @param objectMD
	 *            the object metadata
	 * @return MetaDataInfo representation of the given object metadata
	 */
	private MetaDataInfo buildMetaDataInfo(final ObjectMetaData objectMD) {
		final Map<ExecutionEnvironmentID, ExecutionEnvironment> objectMDlocations = new ConcurrentHashMap<>();
		for (final ExecutionEnvironmentID curLocID : objectMD.getExecutionEnvironmentIDs()) {
			final ExecutionEnvironment loc = getExecutionEnvironment(curLocID);
			objectMDlocations.put(curLocID, loc);
		}
		final MetaDataInfo result = new MetaDataInfo(objectMD.getDataClayID(), objectMD.getDataSetID(),
				objectMD.getMetaClassID(), objectMD.isReadOnly(), objectMDlocations, objectMD.getAlias(),
				objectMD.getOwnerID());

		return result;
	}

	/**
	 * This method returns the metadata of an object.
	 * 
	 * @param objectID
	 *            the ID of the object to be retrieved
	 * @return the metadata of the specified object
	 * @throws ObjectNotRegisteredException
	 *             if the object is not registered
	 */
	private ObjectMetaData getObjectBasicMetaData(final ObjectID objectID) {
		if (DEBUG_ENABLED) {
			logger.debug("Retrieval of ObjectBasicMetaData for {}", objectID);
		}

		ObjectMetaData objectMD = objectMDCache.get(objectID);
		if (objectMD == null) {
			// Query by example
			objectMD = metadataDB.getByID(objectID);
			if (objectMD != null) {
				// Update cache if necessary
				if (DEBUG_ENABLED) {
					logger.debug("[==Get metadata==] Adding version to cache of metadatas: {} -> {}", objectID, objectMD);
				}
				objectMDCache.put(objectID, objectMD);
				final String alias = objectMD.getAlias();
				objectMDCacheByAlias.put(alias, objectMD);
			} else {
				return null; // do not send exception to avoid serializing it, critical path.
			}

		} else { 
			if (DEBUG_ENABLED) {
				logger.debug("Found metadata {} in cache for {}", objectMD, objectID);
			}
		}

		/*
		 * } else { // Check cache objectMD = objectMDCache.get(objectID); if (objectMD == null) { // Query by example objectMD
		 * = metadataDB.getByID(objectID); if (objectMD != null) { // Update cache if necessary objectMDCache.put(objectID,
		 * objectMD); for (final String alias : objectMD.getAliases()) { objectMDCacheByAlias.put( new
		 * Tuple<>(objectMD.getMetaClassID(), alias), objectMD); } } else { throw new ObjectNotRegisteredException(objectID); }
		 * } }
		 */
		return objectMD;
	}

	/**
	 * Return the storage location specification identified by the provided ID
	 * 
	 * @param stLocID
	 *            ID of the storage location
	 * @return the storage location info
	 * @throws StorageLocationNotExistException:
	 *             if the storage location does not exist
	 */
	private StorageLocation getStorageLocation(final StorageLocationID stLocID) {

		// Check it from Cache
		StorageLocation storageLocation = currentStorageLocations.get(stLocID);
		if (storageLocation == null) {
			storageLocation = metadataDB.getByID(stLocID);
			if (storageLocation == null) {
				throw new StorageLocationNotExistException(stLocID);
			}
		}

		// Update cache
		currentStorageLocations.put(stLocID, storageLocation);

		return storageLocation;
	}

	/**
	 * Return the execution environment specification identified by the provided ID
	 * 
	 * @param execEnvID
	 *            ID of the execution environment
	 * @return the execution environment info
	 * @throws ExecutionEnvironmentNotExistException
	 *             if the execution environment does not exist
	 */
	private ExecutionEnvironment getExecutionEnvironment(final ExecutionEnvironmentID execEnvID) {
		// Check it from Cache
		ExecutionEnvironment execEnv = currentExecutionEnvironments.get(execEnvID);
		if (execEnv == null) {
			execEnv = metadataDB.getByID(execEnvID);
			if (execEnv == null) {
				throw new ExecutionEnvironmentNotExistException(execEnvID);
			}
		}

		// Update cache
		currentExecutionEnvironments.put(execEnvID, execEnv);
		if (execEnv.getLang() == Langs.LANG_JAVA) {
			currentJavaExecutionEnvironments.put(execEnv.getAddress(), execEnvID);
		} else if (execEnv.getLang() == Langs.LANG_PYTHON) {
			currentPythonExecutionEnvironments.put(execEnv.getAddress(), execEnvID);
		}

		return execEnv;
	}

	/**
	 * Migrate an object with ID and MetaData provided from the source backend specified to the destination backend specified.
	 * 
	 * @param objectID
	 *            ID of the object.
	 * @param objectMD
	 *            Object meta data.
	 * @param srcBackendID
	 *            ID of the source backend.
	 * @param destBackendID
	 *            ID of the destination backend.
	 * @throws ObjectNotRegisteredException
	 *             if object does not exist
	 */
	private void migrateObjectToBackend(final ObjectID objectID, final ObjectMetaData objectMD,
			final ExecutionEnvironmentID srcBackendID, final ExecutionEnvironmentID destBackendID)
					throws ObjectNotRegisteredException {

		if (DEBUG_ENABLED) {
			logger.debug(
					"[==MOVE==] Migrating metadata of " + objectID + " from " + srcBackendID + " to " + destBackendID);
		}

		// Copy metadata
		final Set<ExecutionEnvironmentID> newBackendIDs = new HashSet<>(objectMD.getExecutionEnvironmentIDs());
		newBackendIDs.remove(srcBackendID);
		newBackendIDs.add(destBackendID);

		// Update db
		try {
			metadataDB.updateLocationIDsByID(objectID, newBackendIDs);
		} catch (final DbObjectNotExistException e) {
			throw new ObjectNotRegisteredException(objectID);
		}

		// Update real metadata
		objectMD.getExecutionEnvironmentIDs().add(destBackendID);
		objectMD.getExecutionEnvironmentIDs().remove(srcBackendID);

		// Update cache
		final ObjectID theobjectID = objectID;
		if (objectMDCache.containsKey(theobjectID)) {
			objectMDCache.get(theobjectID).getExecutionEnvironmentIDs().remove(srcBackendID);
			objectMDCache.get(theobjectID).getExecutionEnvironmentIDs().add(destBackendID);
			String alias = objectMD.getAlias();
			objectMDCacheByAlias.put(alias, objectMDCache.get(theobjectID));
		}
	}

	// ====== Getters for testing purposes ====== //

	/**
	 * Get the MetaDataService::backendCache
	 * 
	 * @return the Backend Cache
	 */
	public Map<ExecutionEnvironmentID, ExecutionEnvironment> getBackendCache() {
		return this.currentExecutionEnvironments;
	}

	/**
	 * Get the MetaDataService::objectMDCache
	 * 
	 * @return the Object MetaData Cache
	 */
	public LruCache<ObjectID, ObjectMetaData> getObjectCache() {
		return this.objectMDCache;
	}

	// ====== OTHER ======= //

	/**
	 * Method used for unit testing.
	 * 
	 * @return The db handler reference of this manager.
	 */
	public MetaDataServiceDB getDbHandler() {
		return this.metadataDB;
	}

	/**
	 * Close DbHandler
	 */
	public void closeDbHandler() {
		this.metadataDB.close();
	}

	@Override
	public void cleanCaches() {
		objectMDCache.clear();
		objectMDCacheByAlias.clear();
		currentStorageLocations.clear();
		currentExecutionEnvironments.clear();
		currentJavaExecutionEnvironments.clear();
		currentPythonExecutionEnvironments.clear();
	}
}
