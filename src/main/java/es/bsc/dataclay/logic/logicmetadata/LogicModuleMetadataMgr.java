
/**
 * @file MetaDataService.java
 * @date May 21, 2013
 */
package es.bsc.dataclay.logic.logicmetadata;

import es.bsc.dataclay.commonruntime.DataClayRuntime;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectAlreadyExistException;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectNotExistException;
import es.bsc.dataclay.exceptions.metadataservice.*;
import es.bsc.dataclay.logic.LogicModule;
import es.bsc.dataclay.metadataservice.MetaDataServiceDB;
import es.bsc.dataclay.metadataservice.ObjectMetaData;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.*;
import es.bsc.dataclay.util.management.AbstractManager;
import es.bsc.dataclay.util.management.metadataservice.DataClayInstance;
import es.bsc.dataclay.util.management.metadataservice.ExecutionEnvironment;
import es.bsc.dataclay.util.management.metadataservice.MetaDataInfo;
import es.bsc.dataclay.util.management.metadataservice.StorageLocation;
import es.bsc.dataclay.util.structs.MemoryCache;
import es.bsc.dataclay.util.structs.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public final class LogicModuleMetadataMgr extends AbstractManager {
	private static final Logger logger = LogManager.getLogger("LogicModuleMetadataMgr");

	/** Indicates if debug is enabled. */
	protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** LogicMetadataDB database. */
	private final LogicMetadataDB metadataDB;

	/** Cache for Storage Location mapping. */
	private final ConcurrentHashMap<StorageLocationID, StorageLocation> currentStorageLocations;

	/** Current Execution Environments mappings. */
	private final ConcurrentHashMap<ExecutionEnvironmentID, ExecutionEnvironment> currentExecutionEnvironments;
	private final ConcurrentHashMap<String, ExecutionEnvironmentID> currentJavaExecutionEnvironments;
	private final ConcurrentHashMap<String, ExecutionEnvironmentID> currentPythonExecutionEnvironments;

	/** External dataClay info cache. */
	private final MemoryCache<DataClayInstanceID, DataClayInstance> externalDataClaysCache;

	/** External dataClay IDs per host-port cache. */
	private final MemoryCache<Tuple<String, Integer>, DataClayInstanceID> dataClaysPerHostPortCache;


	/**
	 * Instantiates an MetaDataService that uses the Backend configuration provided.
	 *
	 * @param dataSource
	 *            Data source.
	 * @post Creates MetaDataService and initializes the backend.
	 */
	public LogicModuleMetadataMgr(final SQLiteDataSource dataSource) {
		super(dataSource);
		metadataDB = new LogicMetadataDB(dataSource);
		metadataDB.createTables();

		this.externalDataClaysCache = new MemoryCache<>();
		this.dataClaysPerHostPortCache = new MemoryCache<>();

		this.currentStorageLocations = new ConcurrentHashMap<>();
		this.currentExecutionEnvironments = new ConcurrentHashMap<>();
		this.currentJavaExecutionEnvironments = new ConcurrentHashMap<>();
		this.currentPythonExecutionEnvironments = new ConcurrentHashMap<>();
	}

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
	 * @param eeID
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

			for (int i = 0; i < dClayInfo.getHosts().size(); ++i) {
				final String host = dClayInfo.getHosts().get(i);
				final Integer port = dClayInfo.getPorts().get(i);
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
	 * Get all dataClays current dataClay has objects federated with
	 * @return dataClay IDs current dataClay has objects federated with
	 */
	public Set<DataClayInstanceID> getAllExternalDataClays() {
		return metadataDB.getAllExternalDataClays();
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
	 * Store into database
	 * @param logicmoduleIDs
	 *            logic module ids
	 */
	public void registerLogicModule(final LogicMetadataIDs logicmoduleIDs) {
		metadataDB.store(logicmoduleIDs);
	}

	/**
	 * Check if there is a LogicModule
	 * @return TRUE if exists. FALSE otherwise
	 */
	public boolean existsMetaData() {
		return metadataDB.existsMetaData();
	}

	/**
	 * Get LogicModule metadata by ID
	 * @return The LogicModule metadata
	 */
	public LogicMetadataIDs getLogicMetadata() {
		return metadataDB.getLogicMetadata();
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
	 * Close DbHandler
	 */
	public void closeDbHandler() {
		this.metadataDB.close();
	}

	@Override
	public void cleanCaches() {
		currentStorageLocations.clear();
		currentExecutionEnvironments.clear();
		currentJavaExecutionEnvironments.clear();
		currentPythonExecutionEnvironments.clear();
	}
}
