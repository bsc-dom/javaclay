package es.bsc.dataclay.commonruntime;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.api.BackendID;
import es.bsc.dataclay.api.DataClay;
import es.bsc.dataclay.communication.grpc.clients.CommonGrpcClient;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.dataservice.api.DataServiceAPI;
import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.DataClayRuntimeException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.exceptions.metadataservice.AliasAlreadyInUseException;
import es.bsc.dataclay.exceptions.metadataservice.ObjectNotRegisteredException;
import es.bsc.dataclay.extrae.DataClayExtrae;
import es.bsc.dataclay.heap.HeapManager;
import es.bsc.dataclay.heap.LockerPool;
import es.bsc.dataclay.loader.DataClayObjectLoader;
import es.bsc.dataclay.logic.api.LogicModuleAPI;
import es.bsc.dataclay.serialization.DataClaySerializable;
import es.bsc.dataclay.serialization.lib.DataClayDeserializationLib;
import es.bsc.dataclay.serialization.lib.DataClaySerializationLib;
import es.bsc.dataclay.serialization.lib.ObjectWithDataParamOrReturn;
import es.bsc.dataclay.serialization.lib.PersistentParamOrReturn;
import es.bsc.dataclay.serialization.lib.SerializedParametersOrReturn;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.SessionID;
import es.bsc.dataclay.util.info.VersionInfo;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;
import es.bsc.dataclay.util.management.classmgr.Type;
import es.bsc.dataclay.util.management.classmgr.UserType;
import es.bsc.dataclay.util.management.metadataservice.DataClayInstance;
import es.bsc.dataclay.util.management.metadataservice.ExecutionEnvironment;
import es.bsc.dataclay.util.management.metadataservice.MetaDataInfo;
import es.bsc.dataclay.util.management.metadataservice.RegistrationInfo;
import es.bsc.dataclay.util.management.stubs.ImplementationStubInfo;
import es.bsc.dataclay.util.management.stubs.StubInfo;
import es.bsc.dataclay.util.structs.LruCache;
import es.bsc.dataclay.util.structs.Triple;
import io.grpc.StatusRuntimeException;

/**
 * This class contains functions to interact with DataClay. This is an abstract class in order to provide same functionalities
 * to calls done from client-side and the ones done from a backend side.
 */
public abstract class DataClayRuntime {
	/** Logger. */
	public static final Logger LOGGER = LogManager.getLogger("DataClayRuntime");

	/** Indicates if debug is enabled. */
	public static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** Indicates if runtime was initialized. */
	private boolean initialized = false;

	/** GRPC client. */
	private CommonGrpcClient grpcClient;

	/** Logic Module API for communication. */
	protected LogicModuleAPI logicModule;

	/**
	 * DataClay Heap manager to manage GC, reference counting, lockers and others.
	 */
	protected HeapManager dataClayHeapManager;

	/** Pool of lockers. */
	protected final LockerPool lockerPool = new LockerPool();

	/** Information of all locations known (identified by a hash). */
	protected final Map<Integer, ExecutionEnvironmentID> execLocationsPerHash = new ConcurrentHashMap<>();

	/** Execution Environments cache. */
	protected final Map<ExecutionEnvironmentID, ExecutionEnvironment> execEnvironmentsCache = new ConcurrentHashMap<>();

	/** Cache of metaData. */
	public LruCache<ObjectID, MetaDataInfo> metaDataCache = new LruCache<>(Configuration.Flags.MAX_ENTRIES_DATASERVICE_CACHE.getIntValue());
	
	/** Cache of alias -> oid. */
	protected LruCache<String, Triple<ObjectID, MetaClassID, BackendID>> aliasCache;

	/** DataClay object loader. */
	public DataClayObjectLoader dataClayObjLoader;

	/** Under deserialization volatiles per thread. */
	public final Map<Long, Map<Integer, ObjectWithDataParamOrReturn>> underDeserializationVolatiles = new ConcurrentHashMap<>();

	/**
	 * Alias references found. TODO: this set is not cleaned. Important: modify this when design of removeAlias: we need to add
	 * 'alias' as DataClayObject field since alias can be added dynamically to a volatile. Or check updateAliases function.
	 * Also, ensure removeAlias in LM notifies EE and avoid race condition remove alias but continue using object and GlobalGC
	 * clean it.
	 */
	protected final Set<ObjectID> aliasReferences = ConcurrentHashMap.newKeySet();

	/** Pool for tasks. Initialized in sub-classes. */
	protected ScheduledExecutorService threadPool;

	/**
	 * Set of object ids of volatile parameters that were send but did not arrive to any node yet.
	 */
	protected final Set<ObjectID> volatileParametersBeingSend = new HashSet<>();

	/** Timer for paraver thread. */
	private Timer activatePrvTimer;

	public int misses = 0;
	public int hits = 0;

	// CHECKSTYLE:ON

	// ================================================== //
	// ================= COMMUNICATION ================== //
	// ================================================== //

	/**
	 * Constructor.
	 */
	protected DataClayRuntime() {
		this.aliasCache = new LruCache<>(Configuration.Flags.MAX_ENTRIES_CLIENT_CACHE.getIntValue());
	}

	/**
	 * Initialize session and connections.
	 * 
	 * @param logicModuleHost
	 *            Name of the host of the logic module
	 * @param logicModulePort
	 *            Port of the logic module
	 * @param originHostName
	 *            Name of the host using the lib.
	 * @throws Exception
	 *             if connection could not be done for some reason.
	 */
	protected void initialize(final String logicModuleHost, final int logicModulePort, final String originHostName)
			throws Exception {
		LOGGER.info("Connecting to LM at {}:{} ...", logicModuleHost, logicModulePort);
		grpcClient = new CommonGrpcClient(originHostName);
		logicModule = grpcClient.getLogicModuleAPI(logicModuleHost, logicModulePort);
		LOGGER.info("Connected to LM!");
		this.setInitialized(true);
	}

	/**
	 * Get the logic module API.
	 * 
	 * @return The logic module API
	 */
	public final LogicModuleAPI getLogicModuleAPI() {
		return this.logicModule;
	}

	/**
	 * Get all execution environments information.
	 * @param lang Language
	 * @return All execution locations information
	 */
	public final Map<ExecutionEnvironmentID, ExecutionEnvironment> getExecutionEnvironmentsInfo(final Langs lang) {
		final SessionID sessionID = checkAndGetSession(new String[] {}, new Object[] {});
		final Map<ExecutionEnvironmentID, ExecutionEnvironment> allEEs = logicModule
				.getExecutionEnvironmentsInfo(sessionID, lang, !this.isDSLib());
		return allEEs;
	
	}
	/**
	 * Prepare locations for calculating Hash.
	 */
	public final void prepareExecuteLocations() {
		final Map<ExecutionEnvironmentID, ExecutionEnvironment> allEEs = getExecutionEnvironmentsInfo(Langs.LANG_JAVA);
		// Connect them
		// Since we will use a Hash, prepare it.
		int i = 0;
		for (final Entry<ExecutionEnvironmentID, ExecutionEnvironment> ee : allEEs.entrySet()) {
			final ExecutionEnvironmentID execLocationID = ee.getKey();
			final ExecutionEnvironment execEnvironment = ee.getValue();
			execLocationsPerHash.put(i, execLocationID);
			execEnvironmentsCache.put(execLocationID, execEnvironment);
			i++;
		}

	}

	/**
	 * Get execution location
	 * 
	 * @param objectID
	 *            ID of the object connected.
	 * @return ExecutionEnvironmentID by hash
	 */
	public final ExecutionEnvironmentID getBackendIDFromObjectID(final ObjectID objectID) {

		if (execLocationsPerHash.isEmpty()) {
			prepareExecuteLocations();
		}

		// Apply hash to choose which DS to go.
		final int hashCode = objectID.hashCode();
		final int whichDS = hashCode % execLocationsPerHash.size();
		final int hash = Math.abs(whichDS);
		final ExecutionEnvironmentID stLocID = execLocationsPerHash.get(hash);
		return stLocID;
	}

	/**
	 * Get remote execution environment
	 * 
	 * @param execLocationID
	 *            ID of remote execution environment
	 * @return Remote execution environment
	 */
	public final DataServiceAPI getRemoteExecutionEnvironment(final BackendID execLocationID) {

		final ExecutionEnvironment execEnvironment = getExecutionEnvironmentInfo(execLocationID);
		try {
			return grpcClient.getDataServiceAPI(execEnvironment.getHostname(), execEnvironment.getPort());
		} catch (final InterruptedException ex) {
			LOGGER.debug("getRemoteExecutionEnvironment error", ex);
			// Force to be runtime exc. so Clients can receive the exception
			throw new RuntimeException(ex.getCause());
		}
	}

	/**
	 * Get remote execution environment
	 * 
	 * @param execLocationID
	 *            ID of remote execution environment
	 * @return Remote execution environment
	 */
	public final DataServiceAPI getRemoteExecutionEnvironmentForDS(final ExecutionEnvironmentID execLocationID) {
		ExecutionEnvironment execEnv = this.execEnvironmentsCache.get(execLocationID);
		if (execEnv == null) {
			execEnv = this.logicModule.getExecutionEnvironmentForDS(execLocationID);
			this.execEnvironmentsCache.put(execLocationID, execEnv);
		}
		try {
			return grpcClient.getDataServiceAPI(execEnv.getHostname(), execEnv.getPort());
		} catch (final InterruptedException ex) {
			LOGGER.debug("getRemoteExecutionEnvironmentForDS", ex);
			// Force to be runtime exc. so Clients can receive the exception
			throw new RuntimeException(ex.getCause());
		}
	}

	/**
	 * Get ExecutionEnvironment information
	 * 
	 * @param execLocationID
	 *            Execution location ID
	 * @return Execution location information
	 */
	public final ExecutionEnvironment getExecutionEnvironmentInfo(final BackendID execLocationID) {
		ExecutionEnvironment execEnvironment = this.execEnvironmentsCache.get(execLocationID);
		if (execEnvironment == null) {
			this.prepareExecuteLocations(); //update execute locations 
			execEnvironment = this.execEnvironmentsCache.get(execLocationID);
		}
		return execEnvironment;
	}

	/**
	 * Get external dataClay info
	 * 
	 * @param extDataClayID
	 *            id of the external dataClay instance
	 * @return info of the external dataClay instance
	 */
	public DataClayInstance getExternalDataClayInfo(final DataClayInstanceID extDataClayID) {
		return this.getLogicModuleAPI().getExternalDataClayInfo(extDataClayID);
	}
	

	/**
	 * Method that registers the info of a dataClay instance
	 * 
	 * @param dcHost
	 *            entry port host of the external dataClay
	 * @param dcPort
	 *            entry point port of the external dataClay
	 * @return ID of external registered dataClay.
	 */
	public DataClayInstanceID registerExternalDataClay(final String dcHost, final Integer dcPort) {
		return this.getLogicModuleAPI().registerExternalDataClay(dcHost, dcPort);

	}
	
	/**
	 * ADMIN usage only. Method that registers the info of a dataClay instance but with overriden authority for SSL connections.
	 * @param adminAccountID admin account id
	 * @param  adminCredential admin credentials
	 * @param dcHost
	 *            entry port host of the external dataClay
	 * @param dcPort
	 *            entry point port of the external dataClay
	 * @param authority authority to use
	 * @return ID of external registered dataClay.
	 */
	public DataClayInstanceID registerExternalDataClayOverrideAuthority(final AccountID adminAccountID,
			final PasswordCredential adminCredential, final String dcHost, final int dcPort, final String authority) {
		return this.getLogicModuleAPI().registerExternalDataClayOverrideAuthority(adminAccountID, adminCredential, dcHost, dcPort, authority);

	}
	
	/**
	 * Get external dataClay info
	 * 
	 * @param hostname
	 *            host name of the external dataClay instance
	 * @param port
	 *            port of the external dataClay instance.
	 * @return info of the external dataClay instance
	 */
	public DataClayInstanceID getExternalDataClayID(final String hostname, final int port) {
		return this.getLogicModuleAPI().getExternalDataClayID(hostname, port);
	}

	/**
	 * Get ID of the current instance of dataClay
	 * 
	 * @return the dataClay ID
	 */
	public DataClayInstanceID getDataClayID() {
		return this.getLogicModuleAPI().getDataClayID();
	}

	/**
	 * Get hint of current backend. If client, returns null.
	 * 
	 * @return ID of current backend.
	 */
	public abstract BackendID getHint();

	/**
	 * Finish connections to server
	 */
	public final void finishConnections() {
		LOGGER.debug("Stopping Grpc connections");
		this.grpcClient.finishClientConnections();
		LOGGER.debug("Stopping thread pool");
		this.threadPool.shutdown();
		if (activatePrvTimer != null) {
			LOGGER.debug("Shutdown paraver");
			activatePrvTimer.cancel();
		}

		this.setInitialized(false);
	}

	/**
	 * Get the IDs of the backends in which the object identified by the stub instance provided is located and the classname of
	 * the object.
	 * 
	 * @param objectID
	 *            ID of the object
	 * @return Object metadata.
	 */
	public final MetaDataInfo getObjectMetadata(final ObjectID objectID) {
		final SessionID sessionID = checkAndGetSession(new String[] { "ObjectID" }, new Object[] { objectID });
		MetaDataInfo mdInfo = metaDataCache.get(objectID);
		if (mdInfo == null) {
			misses++;

			if (DEBUG_ENABLED) {
				LOGGER.debug("[==Metadata Cache==] Not found entry in metadata cache for {}."
						+ "Asking LogicModule. WARNING: if the object is volatile, please "
						+ " note that it might be not registered yet (WIP).", objectID);
			}

			mdInfo = logicModule.getMetadataByOID(sessionID, objectID);
			if (DEBUG_ENABLED) {
				if (mdInfo == null) {
					LOGGER.warn("Cannot get metadata for {}", objectID);
				} else {
					LOGGER.debug(
							"[==Metadata Cache==] Adding entry in metadata cache for {} " + "with exec. locations: {}",
							objectID, mdInfo.getLocations().keySet());
				}
			}
			if (mdInfo != null) {
				metaDataCache.put(objectID, mdInfo);
			}

		} else {
			hits++;
			if (DEBUG_ENABLED) {
				LOGGER.debug("[==Metadata Cache==] Found entry in metadata cache for " + objectID);
			}
		}
		return mdInfo;
	}

	// ================================================== //
	// ================= FUNCTIONS ================== //
	// ================================================== //

	/**
	 * Retrieves a copy of the specified object and all its subobjects
	 * 
	 * @param oid
	 *            id of the object to be retrieved
	 * @param recursive
	 *            retrieve a copy of the whole object copying also its subobjects or only the main object
	 * @return a volatile instance of the object
	 */
	public final DataClayObject getCopyOfObject(final ObjectID oid, final boolean recursive) {
		final SessionID sessionID = getSessionID();

		// Get an arbitrary object location
		final BackendID execLocationID = getLocation(oid);
		final DataServiceAPI dsAPI = getRemoteExecutionEnvironment(execLocationID);

		// Retrieve objects
		final SerializedParametersOrReturn copiedObject = dsAPI.getCopyOfObject(sessionID, oid, recursive);
		final Object[] result = DataClayDeserializationLib.deserializeParamsOrReturn(copiedObject, null, this);

		return (DataClayObject) result[0];
	}

	/**
	 * Updates a specific object (into) with the fields of another one (from)
	 * 
	 * @param into
	 *            target object where data is put
	 * @param from
	 *            object containing the data to put
	 */
	public final void updateObject(final ObjectID into, final DataClayObject from) {
		final SessionID sessionID = getSessionID();

		// Get an arbitrary object location
		final BackendID execLocationID = getLocation(into);
		final DataServiceAPI dsAPI = getRemoteExecutionEnvironment(execLocationID);
		final List<DataClaySerializable> aux = new ArrayList<>();
		aux.add(from);

		final SerializedParametersOrReturn serFrom = DataClaySerializationLib.serializeParamsOrReturn(aux, null, this,
				true, execLocationID, false);

		final Map<Integer, ObjectWithDataParamOrReturn> volObjects = serFrom.getVolatileObjs();
		if (volObjects != null) {
			final Map<ObjectID, ObjectID> newIDs = new HashMap<>();

			// Set new ids to all volatile objects to decouple them from client-side
			// Notice that 'from' object will be set with 'into' object ID to
			// force the deserialization at the EE on 'into' object
			for (final Entry<Integer, ObjectWithDataParamOrReturn> curEntry : volObjects.entrySet()) {
				final ObjectID curObjectID = curEntry.getValue().getObjectID();
				if (!newIDs.containsKey(curObjectID)) {
					final ObjectID newID;
					if (curObjectID.equals(from.getObjectID())) {
						newID = into;
					} else {
						newID = new ObjectID();
					}
					newIDs.put(curObjectID, newID);
				}
				curEntry.getValue().setObjectID(newIDs.get(curObjectID));
			}
			for (final Entry<Integer, ObjectWithDataParamOrReturn> curEntry : volObjects.entrySet()) {
				final DataClayObjectMetaData md = curEntry.getValue().getMetaData();
				final Map<Integer, ObjectID> oids = md.getOids();
				for (final Entry<Integer, ObjectID> curOIDEntry : oids.entrySet()) {
					final ObjectID newID = newIDs.get(curOIDEntry.getValue());
					if (newID != null) {
						curOIDEntry.setValue(newID);
					}
				}
			}
		}

		dsAPI.updateObject(sessionID, into, serFrom);
	}

	/**
	 * All volatiles provided are under deserialization. This function solves problems of 'hashcode' and other special functions
	 * needed during deserializations. See executeImpl.
	 * 
	 * @param volatileMap
	 *            Volatiles under deserialization.
	 */
	public void addVolatileUnderDeserialization(final Map<Integer, ObjectWithDataParamOrReturn> volatileMap) {
		underDeserializationVolatiles.put(Thread.currentThread().getId(), volatileMap);
	}

	/**
	 * Remove volatiles under deserialization.
	 */
	public void removeVolatilesUnderDeserialization() {
		underDeserializationVolatiles.remove(Thread.currentThread().getId());
	}

	/**
	 * Get from Heap or create a new volatile in EE and load data on it.
	 * 
	 * @param classID
	 *            ID of class of the object
	 * @param objectID
	 *            ID of the object
	 * @param hint
	 *            Hint of the object
	 * @param objWithData
	 *            Data of the object
	 * @param ifaceBitMaps
	 *            Interface bitmaps
	 * @return Loaded volatile instance in EE.
	 */
	public DataClayObject getOrNewAndLoadVolatile(final MetaClassID classID, final ObjectID objectID,
			final BackendID hint, final ObjectWithDataParamOrReturn objWithData,
			final Map<MetaClassID, byte[]> ifaceBitMaps) {
		return dataClayObjLoader.getOrNewAndLoadVolatile(classID, objectID, hint, objWithData, ifaceBitMaps);
	}

	/**
	 * Get session ID
	 * 
	 * @return Session ID
	 */
	public abstract SessionID getSessionID();

	/**
	 * Check connection and parameters
	 * 
	 * @param paramNames
	 *            Names of parameters
	 * @param params
	 *            Parameters to check
	 */
	public final void checkConnectionAndParams(final String[] paramNames, final Object[] params) {
		RuntimeUtils.checkConnection(logicModule);
		RuntimeUtils.checkNullParams(paramNames, params);
	}

	/**
	 * Check parameters and connections and session
	 * 
	 * @param paramNames
	 *            Names of parameters
	 * @param params
	 *            Parameters to check
	 * @return Session ID
	 */
	public final SessionID checkAndGetSession(final String[] paramNames, final Object[] params) {
		checkConnectionAndParams(paramNames, params);
		final SessionID sessionID = getSessionID();
		RuntimeUtils.checkSession(sessionID);
		return sessionID;
	}

	/**
	 * Method that gets info of an object given its ID if the object is accessible by using the given sesion.
	 * 
	 * @param alias
	 *            alias of the object
	 * @return Currently id of object and hint.
	 */
	public final Triple<ObjectID, MetaClassID, BackendID> getObjectInfoByAlias(final String alias) {
		final SessionID sessionID = checkAndGetSession(new String[] { "Alias" }, new Object[] { alias });

		Triple<ObjectID, MetaClassID, BackendID> result = aliasCache.get(alias);
		if (result == null) {
			if (DEBUG_ENABLED) {
				LOGGER.debug("[==Alias==] Object with alias " + alias + " not found in cache. Asking LM.");
			}
			final Triple<ObjectID, MetaClassID, ExecutionEnvironmentID> aux = logicModule.getObjectFromAlias(sessionID,
					alias);
			result = new Triple<>(aux.getFirst(), aux.getSecond(), aux.getThird());
			aliasCache.put(alias, result);
		}
		return result;
	}

	/**
	 * Method that gets an object given its alias.
	 * 
	 * @param alias
	 *            alias of the object
	 * @return The object identified by alias provided
	 */
	public final DataClayObject getObjectByAlias(final String alias) {
		final ObjectID oid;
		final MetaClassID mid;
		final BackendID bid;

		final Triple<ObjectID, MetaClassID, BackendID> objInfo = getObjectInfoByAlias(alias);
		oid = objInfo.getFirst();
		mid = objInfo.getSecond();
		bid = objInfo.getThird();

		if (DEBUG_ENABLED) {
			LOGGER.debug("[==GetByAlias==] Creating instance from alias " + oid);
		}

		return this.getPersistedObjectByOID(oid, mid, bid);
	}

	/**
	 * Method that gets an object given its alias and metaclass id.
	 * 
	 * @param alias
	 *            alias of the object
	 * @param metaClassID
	 *            if of the object's metaclass
	 * @param safe
	 *            if true, check that alias exists
	 * @return The object identified by the proved alias and metaclass.
	 */
	public final DataClayObject getObjectByAlias(final String alias, MetaClassID metaClassID, boolean safe) {
		if(safe) {
			return this.getObjectByAlias(alias);
		}

		final ObjectID oid = getObjectIDFromAlias(alias);
		final BackendID bid = this.getBackendIDFromObjectID(oid);

		if (DEBUG_ENABLED) {
			LOGGER.debug("[==GetByAlias==] Creating instance from alias " + oid);
		}

		return this.getPersistedObjectByOID(oid, metaClassID, bid);
	}

	/**
	 * Method that deletes the alias of an object
	 * 
	 * @param alias
	 *            alias of the object to be removed
	 */
	public final void deleteAlias(final String alias) {
		final SessionID sessionID = checkAndGetSession(new String[] { "Alias" }, new Object[] { alias });
		logicModule.deleteAlias(sessionID, alias);
		aliasCache.remove(alias);
	}

	/**
	 * Method that gets DataSetID from an object with id provided
	 * 
	 * @param oid
	 *            ID of the object
	 * @return DataSet of the object
	 */
	public final DataSetID getDataSetIDFromObject(final ObjectID oid) {
		final SessionID sessionID = checkAndGetSession(new String[] { "ObjectID" }, new Object[] { oid });
		final DataSetID dsID = logicModule.getObjectDataSetID(sessionID, oid);
		return dsID;
	}

	/**
	 * Get class name from ID.
	 * 
	 * @param classID
	 *            ID of class
	 * @return Class name
	 */
	public abstract String getClassNameInternal(final MetaClassID classID);

	/**
	 * Get class name from ID
	 * 
	 * @param classID
	 *            Class ID
	 * @return Class name
	 */
	public final String getClassName(final MetaClassID classID) {
		checkAndGetSession(new String[] { "classID" }, new Object[] { classID });

		return getClassNameInternal(classID);
	}

	/**
	 * Creates a persistent new version of an object and its subobjects (always recursive). If a Backend is provided the object
	 * is versioned to this backend, otherwise it is versioned to any backend
	 * 
	 * @param objectID
	 *            ID of the object
	 * @param classID
	 *            Class ID of the object
	 * @param hint
	 *            Hint of the object
	 * @param optDestBackendID
	 *            ID of the backend in which to store the version the object (optional)
	 * @return The ID of the version or NULL if some error is thrown.
	 */
	public final VersionInfo newVersion(final ObjectID objectID, final MetaClassID classID, final BackendID hint,
			final BackendID optDestBackendID) {
		final SessionID sessionID = checkAndGetSession(new String[] { "ObjectID" }, new Object[] { objectID });

		// Make sure object is registered
		ensureObjectRegistered(sessionID, objectID, classID, hint);
		final VersionInfo result = logicModule.newVersion(sessionID, objectID, (ExecutionEnvironmentID) optDestBackendID);
		this.metaDataCache.remove(objectID);
		return result;
	}

	/**
	 * Makes the object with finalVersionID the definitive version of the object with originalObjectID. The original version is
	 * deleted.
	 * 
	 * @param version
	 *            Info about the version to consolidate, containing the OID of the root and the mapping versionOID-originalOID
	 *            for all the versioned objects
	 */
	public final void consolidateVersion(final VersionInfo version) {
		final SessionID sessionID = checkAndGetSession(new String[] { "version" }, new Object[] { version });
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==Consolidate==] Consolidate version " + "with oid " + version.getVersionOID());
		}
		logicModule.consolidateVersion(sessionID, version);

	}

	/**
	 * Replicates an object. If a Backend is provided the object is replicated to this backend, otherwise it is replicated to
	 * any backend
	 * 
	 * @param objectID
	 *            ID of the object
	 * @param classID
	 *            Class ID of the object
	 * @param hint
	 *            Hint of the object
	 * @param optDestBackendID
	 *            ID of the backend in which to replicate the object (optional)
	 * @param recursive
	 *            Indicates if we should also replicate all sub-objects or not.
	 * @return The ID of the backend in which the replica was created or NULL if some error is thrown.
	 * 
	 */
	public final BackendID newReplica(final ObjectID objectID, final MetaClassID classID, final BackendID hint,
			final BackendID optDestBackendID, final boolean recursive) {
		final SessionID sessionID = checkAndGetSession(new String[] { "ObjectID" }, new Object[] { objectID });

		// Make sure object is registered
		ensureObjectRegistered(sessionID, objectID, classID, hint);
		final ExecutionEnvironmentID backendID = logicModule.newReplica(sessionID, objectID, (ExecutionEnvironmentID) optDestBackendID,
						recursive);
		this.metaDataCache.remove(objectID);
		return backendID;
	}

	/**
	 * Move the replica of an object from one backend to another.
	 * 
	 * @param objectID
	 *            ID of the object
	 * @param classID
	 *            Class ID of the object
	 * @param hint
	 *            Hint of the object
	 * @param srcBackendID
	 *            ID of the backend containing the replica
	 * @param destBackendID
	 *            ID of the destination backend
	 * @param recursive
	 *            Indicates if movement must be recursive or not.
	 */
	public final void moveObject(final ObjectID objectID, final MetaClassID classID, final BackendID hint,
			final BackendID srcBackendID, final BackendID destBackendID, final boolean recursive) {

		if (DEBUG_ENABLED) {
			LOGGER.debug("[==MOVE==] Moving object " + objectID + " from " + srcBackendID + " to " + destBackendID
					+ ", recursive = " + recursive);
		}

		if (srcBackendID.equals(destBackendID)) {
			return; // ignore
		}

		final SessionID sessionID = checkAndGetSession(
				new String[] { "ObjectID", "SrcStorageLocationID", "DestStorageLocationID" },
				new Object[] { objectID, srcBackendID, destBackendID });

		// FIXME: new replica/version/consolidate/move algorithms should not require
		// registered metadata in
		// LogicModule since new make persistent implementation behaves like volatiles
		// and metadata is created
		// eventually, not synchronously. Currently, we try to register it and if it is
		// already registered, just
		// continue.
		// Make sure object is registered.
		final RegistrationInfo regInfo = new RegistrationInfo(objectID, classID, sessionID, null);
		// alias must be null
		// NOTE: LogicModule register object function does not return an exception for
		// already registered
		// object. We should never call registerObject for already registered objects
		// and that's dataClay
		// code (check isPendingToRegister in EE or isPersistent,.. see makePersistent),
		// and remember that,
		// this is a workaround, registerObject should never be called for
		// replica/version/consolidate algorithms,
		// we must change the algorithms to not depend on metadata.
		// Also, location in which to register the object is the hint (in case it is not
		// registered yet).
		logicModule.registerObject(regInfo, (ExecutionEnvironmentID) hint, null, Langs.LANG_JAVA);

		final List<ObjectID> movedObjs = logicModule.moveObject(sessionID, objectID,
				(ExecutionEnvironmentID) srcBackendID, (ExecutionEnvironmentID) destBackendID, recursive);

		final BackendID newhint = destBackendID;
		for (final ObjectID movedOID : movedObjs) {
			final DataClayObject obj = this.getFromHeap(movedOID);
			if (obj != null) {
				if (DEBUG_ENABLED) {
					LOGGER.debug("[==Hint==] Setting hint (moveObject) on instance " + obj.getObjectID()
					+ " the hint : " + getDSNameOfHint(newhint));
				}
				obj.setHint(newhint);
				if (obj.getMasterLocation() != null) {
					if (obj.getMasterLocation().equals(getHint())) {
						obj.setMasterLocation(newhint);
					}
				}
			}
			this.metaDataCache.remove(movedOID);
		}
	}

	/**
	 * Set a persistent object as read only Logic module API used for communication
	 * 
	 * @param objectID
	 *            ID of the object
	 * @param classID
	 *            Class ID of the object
	 * @param hint
	 *            Hint of the object
	 */
	public final void setObjectReadOnly(final ObjectID objectID, final MetaClassID classID, final BackendID hint) {
		final SessionID sessionID = checkAndGetSession(new String[] { "ObjectID" }, new Object[] { objectID });
		this.ensureObjectRegistered(sessionID, objectID, classID, hint);
		logicModule.setObjectReadOnly(sessionID, objectID);
		this.metaDataCache.remove(objectID);
	}

	/**
	 * Set a persistent object as read write
	 * 
	 * @param objectID
	 *            ID of the object
	 * @param classID
	 *            Class ID of the object
	 * @param hint
	 *            Hint of the object
	 */
	public final void setObjectReadWrite(final ObjectID objectID, final MetaClassID classID, final BackendID hint) {
		final SessionID sessionID = checkAndGetSession(new String[] { "ObjectID" }, new Object[] { objectID });
		this.ensureObjectRegistered(sessionID, objectID, classID, hint);
		logicModule.setObjectReadWrite(sessionID, objectID);
		this.metaDataCache.remove(objectID);
	}

	/**
	 * Ensure registration of an object. new replica/version/consolidate/move algorithms should not require registered metadata
	 * in LogicModule since new make persistent implementation behaves like volatiles and metadata is created eventually, not
	 * synchronously. Currently, we try to register it and if it is already registered, just continue.
	 * 
	 * @param sessionID
	 *            ID of session registering object
	 * @param objectID
	 *            ID of object to register
	 * @param classID
	 *            ID of class of the object
	 * @param hint
	 *            Hint of the object
	 */
	private void ensureObjectRegistered(final SessionID sessionID, final ObjectID objectID, final MetaClassID classID,
			final BackendID hint) {

		// FIXME: new replica/version/consolidate/move algorithms should not require
		// registered metadata in
		// LogicModule since new make persistent implementation behaves like volatiles
		// and metadata is created
		// eventually, not synchronously. Currently, we try to register it and if it is
		// already registered, just
		// continue.
		// Make sure object is registered.
		final RegistrationInfo regInfo = new RegistrationInfo(objectID, classID, sessionID, null);
		// alias must be null
		// NOTE: LogicModule register object function does not return an exception for
		// already registered
		// object. We should never call registerObject for already registered objects
		// and that's dataClay
		// code (check isPendingToRegister in EE or isPersistent,.. see makePersistent),
		// and remember that,
		// this is a workaround, registerObject should never be called for
		// replica/version/consolidate algorithms,
		// we must change the algorithms to not depend on metadata.
		// Also, location in which to register the object is the hint (in case it is not
		// registered yet).
		logicModule.registerObject(regInfo, (ExecutionEnvironmentID) hint, null, Langs.LANG_JAVA);
	}

	/**
	 * Get the ID of some backend in which the object identified by the stub instance provided is located
	 * 
	 * @param objectID
	 *            ID of the object
	 * @return ID of some backend in which the object is located or NULL if some error is thrown.
	 */
	public final BackendID getLocation(final ObjectID objectID) {
		checkConnectionAndParams(new String[] { "ObjectID" }, new Object[] { objectID });

		if (DEBUG_ENABLED) {
			LOGGER.debug("[==GetLocation==] Obtaining location for " + objectID);
		}
		BackendID locationID = null;

		// Get Hint
		final DataClayObject obj = this.getFromHeap(objectID);
		if (obj != null) {
			locationID = obj.getHint();
			if (DEBUG_ENABLED) {
				LOGGER.debug("[==GetLocation==] Obtained location for " + objectID + " from exec.hint: " + locationID);
			}
		}
		if (locationID != null) {
			return locationID;
		} else {
			// If the object is not initialized well trying to obtain location from metadata
			final MetaDataInfo metadata = getObjectMetadata(objectID);
			if (metadata == null) {
				// no metadata available, throw exception
				// NOTE: if it is a volatile and hint failed, it means that object is actually
				// not registered
				throw new ObjectNotRegisteredException(objectID);
			}
			return metadata.getLocations().keySet().iterator().next();
		}
	}

	/**
	 * Get the IDs of the backends in which the object identified by the stub instance provided is located.
	 * 
	 * @param objectID
	 *            ID of the object
	 * @return IDs of the backends in which the object is located or NULL if some error is thrown.
	 */
	public final Set<BackendID> getAllLocations(final ObjectID objectID) {
		checkConnectionAndParams(new String[] { "ObjectID" }, new Object[] { objectID });
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==GetAllLocations==] For " + objectID);
		}
		final MetaDataInfo metadata = getObjectMetadata(objectID);
		if (metadata != null) {
			return new HashSet<>(metadata.getLocations().keySet());
		} else {
			if (DEBUG_ENABLED) {
				LOGGER.debug("[==GetLocation==] Object" + objectID + "has not metadata");
			}
			// Get Hint
			final Set<BackendID> locations = new HashSet<>();
			final DataClayObject obj = this.getFromHeap(objectID);
			if (obj != null) {
				final BackendID locationID = obj.getHint();
				if (locationID != null) {
					if (DEBUG_ENABLED) {
						LOGGER.debug("[==GetLocation==] Obtained location for " + objectID + " from exec.hint: "
								+ locationID);
					}
					locations.add(locationID);
				} else {
					throw new DataClayRuntimeException(ERRORCODE.UNEXPECTED_EXCEPTION,
							"The object " + objectID + " is not initialized well, hint missing or not exist", true);
				}
			} // else, can be only a String from COMPSs.
			return locations;
		}
	}

	/**
	 * Get name of node associated to hint.
	 * 
	 * @param hint
	 *            Hint
	 * @return name of node associated to hint
	 */
	public final String getDSNameOfHint(final BackendID hint) {
		if (hint == null) {
			return null;
		}
		return this.getExecutionEnvironmentInfo(hint).getName();
	}

	/**
	 * Serialize parameters.
	 * 
	 * @param objectInWhichToExec
	 *            Object in which to run method that needs serialization of parameters
	 * @param ifaceBitMaps
	 *            Interface bitmaps
	 * @param implID
	 *            ImplementationID
	 * @param params
	 *            Parameters or return to serialize
	 * @param forUpdate
	 *            Indicates whether this serialization is for an update or not
	 * @param hintVolatiles
	 *            Hint to set to volatiles
	 * @return Serialized parameters
	 */
	public final SerializedParametersOrReturn serializeParams(final DataClayObject objectInWhichToExec,
			final Map<MetaClassID, byte[]> ifaceBitMaps, final ImplementationID implID, final Object[] params,
			final boolean forUpdate, final BackendID hintVolatiles) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==Serialization==] Serializing parameters");
		}

		// Wrap parameters
		final List<DataClaySerializable> wrappedParams = objectInWhichToExec.wrapParameters(implID, params);
		// Serialize parameters
		// IfaceBitMaps = null. From client stub is controlling it.
		if (wrappedParams.isEmpty()) {
			if (DEBUG_ENABLED) {
				LOGGER.debug("[==Serialization==] No parameters to serialize");
			}
			return null;
		} else {
			final SerializedParametersOrReturn serParams = DataClaySerializationLib
					.serializeParamsOrReturn(wrappedParams, ifaceBitMaps, this, forUpdate, hintVolatiles, false);
			return serParams;
		}
	}

	/**
	 * Serialize parameters.
	 * 
	 * @param objectInWhichToExec
	 *            Object in which to run method that needs serialization of parameters
	 * @param ifaceBitMaps
	 *            Interface bitmaps
	 * @param implID
	 *            ImplementationID
	 * @param ret
	 *            Return to serialize
	 * @return Serialized parameters
	 */
	public final SerializedParametersOrReturn serializeReturn(final DataClayObject objectInWhichToExec,
			final Map<MetaClassID, byte[]> ifaceBitMaps, final ImplementationID implID, final Object ret) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==Serialization==] Serializing return");
		}
		if (ret == null) {
			if (DEBUG_ENABLED) {
				LOGGER.debug("[==Serialization==] No return to serialize");
			}
			return null;
		}

		// Return of an object from DataClay is always persistent. Should not find any
		// volatile.

		// Wrap parameters
		final List<DataClaySerializable> wrapList = objectInWhichToExec.wrapReturn(implID, ret);

		// Serialize parameters
		// IfaceBitMaps = null. From client stub is controlling it.
		final SerializedParametersOrReturn serReturn = DataClaySerializationLib.serializeParamsOrReturn(wrapList,
				ifaceBitMaps, this, false, null, false); // no hint volatiles since volatiles are not going to client

		if (!serReturn.getVolatileObjs().isEmpty()) {
			LOGGER.error("Sending volatile objects to client or other DS from DS");
		}

		return serReturn;
	}

	/**
	 * Deserialize parameters.
	 * 
	 * @param objectInWhichToExec
	 *            Object in which to run method that needs deserialization of parameters.
	 * @param ifaceBitMaps
	 *            Interface bitmaps
	 * @param implID
	 *            ImplementationID
	 * @param serializedParams
	 *            Parameters to deserialize
	 * @return Serialized parameters
	 */
	public final Object[] deserializeParams(final DataClayObject objectInWhichToExec,
			final Map<MetaClassID, byte[]> ifaceBitMaps, final ImplementationID implID,
			final SerializedParametersOrReturn serializedParams) {
		if (serializedParams == null) {
			return null;
		}
		if (serializedParams.needWrappers()) {
			// Create wrappers for language/immutable parameters
			objectInWhichToExec.setWrappersParams(implID, serializedParams);
		}
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==Serialization==] Deserializing parameters");
		}
		return DataClayDeserializationLib.deserializeParamsOrReturn(serializedParams, ifaceBitMaps, this);
	}

	/**
	 * Deserialize int heap objects provided for a make persistent call.
	 * 
	 * @param ifaceBitMaps
	 *            Interface bitmaps
	 * @param serializedParams
	 *            Objects to deserialize
	 * @return the deserialized parameters
	 */
	public final Object[] deserializeMakePersistent(final Map<MetaClassID, byte[]> ifaceBitMaps,
			final SerializedParametersOrReturn serializedParams) {
		if (serializedParams == null) {
			return null;
		}
		return DataClayDeserializationLib.deserializeParamsOrReturn(serializedParams, ifaceBitMaps, this);
	}

	/**
	 * Deserialize return.
	 * 
	 * @param objectInWhichToExec
	 *            Object in which to run method that needs deserialization of return.
	 * @param ifaceBitMaps
	 *            Interface bitmaps
	 * @param implID
	 *            ImplementationID
	 * @param serializedReturn
	 *            Return to deserialize
	 * @return Deserialized return
	 */
	public final Object deserializeReturn(final DataClayObject objectInWhichToExec,
			final Map<MetaClassID, byte[]> ifaceBitMaps, final ImplementationID implID,
			final SerializedParametersOrReturn serializedReturn) {
		if (serializedReturn == null) {
			return null;
		}
		objectInWhichToExec.setWrappersReturn(implID, serializedReturn);
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==Serialization==] Deserializing return");
		}
		return DataClayDeserializationLib.deserializeParamsOrReturn(serializedReturn, ifaceBitMaps, this)[0];
	}

	/**
	 * Internal Method that executes an implementation depending on client or server.
	 * 
	 * @param objectInWhichToExec
	 *            Object in which to exec
	 * @param implID
	 *            Implementation ID
	 * @param params
	 *            Parameters
	 * @return the result of execution.
	 * @note This function is called from a stub
	 */
	protected abstract Object executeRemoteImplementationInternal(final DataClayObject objectInWhichToExec,
			final ImplementationID implID, final Object[] params);

	/**
	 * Method that executes an implementation.
	 * 
	 * @param objectInWichToExec
	 *            Object in which to exec
	 * @param implIDStr
	 *            Implementation ID as string
	 * @param params
	 *            Parameters
	 * @return the result of execution.
	 * @note This function is called from a stub
	 */
	public final Object executeRemoteImplementation(final DataClayObject objectInWichToExec, final String implIDStr,
			final Object[] params) {
		final SessionID sessionID = getSessionID();
		if (Configuration.Flags.CHECK_SESSION.getBooleanValue()) {
			RuntimeUtils.checkSession(sessionID);
		}
		final ImplementationID implID = new ImplementationID(implIDStr);
		return this.executeRemoteImplementationInternal(objectInWichToExec, implID, params);
	}

	/**
	 * Get interface bitmaps.
	 * 
	 * @return Interface bitmaps
	 */
	protected abstract Map<MetaClassID, byte[]> getIfaceBitMaps();

	/**
	 * Execute a remote implementation in Location specified.
	 * 
	 * @param dcObject
	 *            Object used as a 'portal' to other DS.
	 * @param params
	 *            Parameters to send
	 * @param remoteImplID
	 *            ID of implementation to execute
	 * @param remoteLocationID
	 *            Location in which to execute
	 * @param usingHint
	 *            TRUE if using hint.
	 * @return Result of execution.
	 */
	public final Object callExecuteToDS(final DataClayObject dcObject, final Object[] params,
			final ImplementationID remoteImplID, final BackendID remoteLocationID, final boolean usingHint) {

		final SessionID sessionID = getSessionID();
		// ===== SERIALIZE PARAMETERS ===== //
		// Between DS - DS, ifaceBitMaps = null
		// Serialize parameters
		SerializedParametersOrReturn serResult = null;
		boolean executed = false;
		BackendID execLocationID = remoteLocationID;
		DataServiceAPI dsAPI = null;
		short numMisses = 0;
		final Map<MetaClassID, byte[]> ifaceBitMaps = null; // TODO: add iface bitmaps? (too expensive?)
		final SerializedParametersOrReturn serializedParams = serializeParams(dcObject, ifaceBitMaps, remoteImplID,
				params, false, execLocationID);
		if (serializedParams != null && serializedParams.getVolatileObjs() != null) {
			for (final Entry<Integer, ObjectWithDataParamOrReturn> param : serializedParams.getVolatileObjs()
					.entrySet()) {
				volatileParametersBeingSend.add(param.getValue().getObjectID());
			}
		}
		while (!executed && numMisses < Configuration.Flags.MAX_EXECUTION_RETRIES.getShortValue()) {
			try {
				if (DEBUG_ENABLED) {
					final ExecutionEnvironment execEnv = this.getExecutionEnvironmentInfo(execLocationID);
					LOGGER.debug("[==JUMP==] Request execution to  " + execEnv.getName() + " for object "
							+ dcObject.getObjectID());
				}

				dsAPI = getRemoteExecutionEnvironment(execLocationID);
				serResult = dsAPI.executeImplementation(dcObject.getObjectID(), remoteImplID, serializedParams,
						sessionID);
				executed = true;
			} catch (StatusRuntimeException | OutOfMemoryError | DataClayException er) {

				// AliasAlreadyInUse should be thrown. TODO: define better exceptions.
				if (er instanceof AliasAlreadyInUseException) {
					// This can happen with registered methods calling makePersistent
					throw er;
				}

				// ===== POSSIBLE RACE CONDITION ===== //
				// There was a previous miss and we updated the metadata cache
				// It is possible that a race condition might happened. Same volatile was send
				// concurrently but data only goes in one of these calls.
				// Check if any persistent parameter is a volatile being send. If so, repeat the
				// call.
				boolean isRaceCondition = false;
				if (serializedParams != null && serializedParams.getPersistentRefs() != null) {
					for (final Entry<Integer, PersistentParamOrReturn> param : serializedParams.getPersistentRefs()
							.entrySet()) {
						if (volatileParametersBeingSend.contains(param.getValue().getObjectID())) {
							isRaceCondition = true;
							break;
						}
					}
				}

				if (!isRaceCondition) {
					// TODO Check if we really want numMisses to be incremented here
					numMisses++;

					// =================== UPDATE METADATA CACHE (due to a MISS) ===================
					// //
					// If remote DS sends a DbObjectNotExists means that it might be possible that
					// THIS DataService contains wrong information
					// in its cache and must remove it and seek for new one.
					metaDataCache.remove(dcObject.getObjectID());

					LOGGER.debug("Execution failed in location " + execLocationID);

					// PREFER NOT TRIED LOCATION (In case Backend failed and we have replicas)
					final MetaDataInfo metadata = getObjectMetadata(dcObject.getObjectID());
					if (metadata == null) {
						// no metadata available, throw exception
						// NOTE: if it is a volatile and hint failed, it means that object is actually
						// not registered
						throw new ObjectNotRegisteredException(dcObject.getObjectID());
					}

					boolean foundDifferentLocation = false;
					for (final ExecutionEnvironmentID curLoc : metadata.getLocations().keySet()) {
						LOGGER.debug("Found location " + curLoc);
						if (!curLoc.equals(execLocationID)) {
							execLocationID = curLoc;
							foundDifferentLocation = true;
							LOGGER.debug("Found different location " + execLocationID);
							break;
						}
					}
					if (!foundDifferentLocation) {
						LOGGER.debug("Using random location in retry: " + execLocationID);
						execLocationID = metadata.getLocations().keySet().iterator().next();
					}
					if (usingHint) {
						if (DEBUG_ENABLED) {
							LOGGER.debug("[==Hint==] Setting hint (Retry remote execution) on instance "
									+ dcObject.getObjectID() + " the hint : " + getDSNameOfHint(execLocationID));
						}
						dcObject.setHint(execLocationID);
					}

					if (DEBUG_ENABLED) {
						LOGGER.debug("[==Miss Jump==] MISS. The object " + dcObject.getObjectID()
						+ " was not in the exec.location " + execLocationID + ". Retrying execution.");
					}
				}

			}
		}



		if (serializedParams != null && serializedParams.getVolatileObjs() != null) {
			// update hints in volatiles
			for (final ObjectWithDataParamOrReturn volatil : serializedParams.getVolatileObjs().values()) {
				if (DEBUG_ENABLED) {
					LOGGER.debug("[==Hint==] Setting hint (exec) in object " + volatil.getObjectID() + " hint: "
							+ remoteLocationID);
				}
				if (numMisses > 0) {
					// if there was a miss, it means that the persistent object in which we were executing 
					// was not in the choosen location. As you can see in the serialize parameters function above
					// we provide the execution environment as hint to set to volatile parameters. In EE, before
					// deserialization of volatiles we check if the persistent object in which to execute a method is
					// there, if not, EE raises and exception. Therefore, if there was a miss, we know that the 
					// hint we set in volatile parameters is wrong, because they are going to be deserialized/stored
					// in the same location as the object with the method to execute
					volatil.getDataClayObject().setHint(remoteLocationID); 
				}
				volatileParametersBeingSend.remove(volatil.getObjectID());

			}

		}

		if (dsAPI == null) {
			throw new DataClayException(ERRORCODE.UNEXPECTED_EXCEPTION,
					"[dataClay] ERROR: Trying to execute remotely but"
							+ " not initialized/found. Please, check initialization of StorageItf or ClientManagementLib "
							+ " was successfull or contact administrator.",
							true);
		}

		if (!executed) {
			if (DEBUG_ENABLED) {
				LOGGER.error(
						"Trying to execute remotely object {} of class {}, "
								+ " but something went wrong. Maybe the object is still not stored "
								+ " (in case of asynchronous makepersistent) and waiting time is not enough."
								+ " Maybe the object does not exist anymore due to a remove. Or Maybe an "
								+ "exception happened in the server and the call failed.",
								dcObject.getObjectID(), dcObject.getClass().getName());
			}
			throw new RuntimeException("[dataClay] ERROR: Trying to execute remotely object " + dcObject.getObjectID()
			+ " of class " + dcObject.getClass().getName()
			+ " but something went wrong. Maybe the object is still not stored "
			+ " (in case of asynchronous makepersistent) and waiting time is not enough."
			+ " Maybe the object does not exist anymore due to a remove. Or Maybe an "
			+ "exception happened in the server and the call failed.");
		}

		// ===== DESERIALIZE RETURN ===== //
		// Deserialize return
		return deserializeReturn(dcObject, ifaceBitMaps, remoteImplID, serResult);
	}

	/**
	 * Execute a remote implementation in Location specified. 
	 * WARNING: This function is only intended for calls in federation - replicated fields. 
	 * A real call to remote dataClay would require a new design of exceptions/volatiles between dataClays.
	 * 
	 * @param dcObject
	 *            Object used as a 'portal' to other DS.
	 * @param params
	 *            Parameters to send
	 * @param remoteImplID
	 *            ID of implementation to execute
	 * @param dcID
	 *            External dataClay ID where to call
	 * @param allBackends
	 *            Whether to execute the implementaiton in all possible replicas of the object in external dataClay or not
	 */
	public final void synchronizeFederated(final DataClayObject dcObject, final Object[] params,
			final ImplementationID remoteImplID, final DataClayInstanceID dcID, final boolean allBackends) {

		// ===== SERIALIZE PARAMETERS ===== //
		// Between DC - DC, ifaceBitMaps = null
		// Serialize parameters
		LogicModuleAPI lmAPI = null;
		final DataClayInstance dcInfo = getExternalDataClayInfo(dcID);
		final SerializedParametersOrReturn serializedParams = serializeParams(dcObject, null, remoteImplID, params,
				false, null);
		final String[] hosts = dcInfo.getHosts();
		final Integer[] ports = dcInfo.getPorts();

		for (int i = 0; i < hosts.length; i++) {
			try {
				lmAPI = getCommonGrpcClient().getLogicModuleAPI(hosts[i], ports[i]);
				break;
			} catch (final InterruptedException e) {
				if (i + 1 == hosts.length) {
					throw new RuntimeException(
							"[dataClay] ERROR: " + " Cannot connect to external dataClay with ID " + dcID);
				}
			}
		}

		if (DEBUG_ENABLED) {
			LOGGER.debug("[==JUMP==] Request execution to external dataClay with id " + dcInfo.getDcID()
			+ " for object " + dcObject.getObjectID());
		}
		final DataClayInstanceID myID = getDataClayID();
		lmAPI.synchronizeFederatedObject(myID, dcObject.getObjectID(), remoteImplID, serializedParams, allBackends);
	}

	/**
	 * Check if string is UUID
	 * 
	 * @param string
	 *            string to check
	 * @return TRUE if it is an uuid. FALSE otherwise.
	 */
	private boolean isUUID(final String string) {
		try {
			UUID.fromString(string);
			return true;
		} catch (final Exception ex) {
			return false;
		}
	}

	/**
	 * Check if string is Compss DataClayID
	 * 
	 * @param string
	 *            string to check
	 * @return TRUE if it is an uuid. FALSE otherwise.
	 */
	private boolean isCompssDataClayID(final String string) {
		if (string.contains(":")) {
			final String[] ids = string.split(":");
			if (isUUID(ids[0])) {
				if (ids.length > 0) {
					if (isUUID(ids[1])) {
						return true;
					}
				} else {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Method that executes an implementation
	 * 
	 * @param objectID
	 *            ID of the object
	 * @param className
	 *            Name of the class of the object
	 * @param operationNameAndSignature
	 *            Name and Signature of the operation to be executed
	 * @param params
	 *            parameters for the operation
	 * @param target
	 *            the backend where the execution must be performed
	 * @return the resulting object corresponding to the execution of the operation if it succeeds. null otherwise. <br>
	 *         if the method is void, it returns null also but no ERROR is prompt.
	 */
	public final Object executeRemoteTask(final ObjectID objectID, final String className,
			final String operationNameAndSignature, final Object[] params, final BackendID target) {

		if (DEBUG_ENABLED) {
			LOGGER.debug("[==ExecuteTask==] Executing task " + operationNameAndSignature + " in object " + objectID
					+ " of class " + className);
		}

		checkAndGetSession(new String[] { "ObjectID", "ClassName", "operationNameAndSignature", "target" },
				new Object[] { objectID, className, operationNameAndSignature, target });

		// Get class ID
		final StubInfo stubInfo = DataClayObject.getStubInfoFromClass(className);
		final ImplementationStubInfo opStubInfo = stubInfo
				.getImplementationByNameAndSignature(operationNameAndSignature);
		final Map<String, Type> paramsSpecs = opStubInfo.getParams();
		final List<String> paramsOrder = opStubInfo.getParamsOrder();

		for (int i = 0; i < params.length; i++) {
			// Check if parameter is String
			if (params[i] instanceof String) {
				final String oid = (String) params[i];
				// CHECKSTYLE:OFF
				if (this.isCompssDataClayID(oid)) {
					// ======== TURN STRING UUIDS TO DATACLAY OBJECTS ======== //
					// It can be a persistent object
					// CHECKSTYLE:ON
					// Serialize it as an object
					try {
						final Triple<ObjectID, BackendID, MetaClassID> ids = DataClay.string2IDandHintID(oid);
						final ObjectID paramObjectID = ids.getFirst();
						final BackendID hint = ids.getSecond();
						final Type paramSpec = paramsSpecs.get(paramsOrder.get(i));
						final UserType paramSpecUType = (UserType) paramSpec;
						if (DEBUG_ENABLED) {
							LOGGER.debug("[==ExecuteTask==] Creating instance for param of task " + paramObjectID);
						}
						final DataClayObject instance = this.getPersistedObjectByOID(paramObjectID,
								paramSpecUType.getClassID(), hint);
						params[i] = instance;
					} catch (final Exception ex) {
						throw new RuntimeException(ex.getMessage());
					}
				}
			}
		}

		// Creating a portal and using its 'wrapParameters' method is much more
		// efficient than
		// providing a 'switch-case' serialization. The wrapParameters function cannot
		// be static generic call
		// since it depends on the class (like run)
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==ExecuteTask==] Creating portal instance for task " + objectID);
		}
		final DataClayObject portal = this.getPersistedObjectByOID(objectID, stubInfo.getClassID(), null);
		return callExecuteToDS(portal, params, opStubInfo.getRemoteImplID(), target, false);
	}

	/**
	 * Check if instance exists in Heap or create a new PERSISTENT instance if needed
	 * 
	 * @param classID
	 *            ID of the class in case it is needed (not need to query) if null, look for class id in metadata.
	 * @param objectID
	 *            ID of object
	 * @param hint
	 *            Can be null. Hint in case object is a volatile in another DS and we need information.
	 * @return Instance
	 */
	public abstract DataClayObject getOrNewPersistentInstance(final MetaClassID classID, final ObjectID objectID,
			final BackendID hint);

	/**
	 * Create a new instance in a remote server and persist it.
	 * 
	 * @param classID
	 *            ID of the class of the instance to create
	 * @param stubInfo
	 *            Stub information
	 * @param implID
	 *            ID of the implementation of the constructor
	 * @param params
	 *            Parameters to send to constructor
	 * @param locID
	 *            (optional) Storage Location/ Execution Environment in which to store object.
	 * @return ObjectID of persisted instance.
	 */
	public final ObjectID newRemotePersistentInstance(final MetaClassID classID, final StubInfo stubInfo,
			final ImplementationID implID, final Object[] params, final BackendID locID) {
		final SessionID sessionID = checkAndGetSession(new String[] { "classID", "stubInfo", "implID" },
				new Object[] { classID, stubInfo, implID });

		final ImplementationStubInfo implStubInfo = stubInfo.getImplementationByID(implID.toString());
		// Serialize parameters
		if (DEBUG_ENABLED) {
			LOGGER.debug(
					"[==NewRemote==] New remote persistent instance. Going to execute " + implStubInfo.getSignature());
			LOGGER.debug("[==Serialization==] Serializing parameters for: " + implStubInfo.getSignature());
		}

		// Creating a portal and using its 'wrapParameters' method is much more
		// efficient than
		// providing a 'switch-case' serialization. The wrapParameters function cannot
		// be static generic call
		// since it depends on the class (like run)
		final DataClayObject portal = getOrNewPersistentInstance(stubInfo.getClassID(), new ObjectID(), null);
		// IfaceBitMaps = null. From client stub is controlling it.

		BackendID execLocationID = locID;
		if (execLocationID == null) {
			// === RANDOM === //
			if (execEnvironmentsCache.isEmpty()) {
				prepareExecuteLocations();
			}
			execLocationID = execLocationsPerHash.values().iterator().next();
		}

		// Serialize parameters
		final SerializedParametersOrReturn serializedParams = serializeParams(portal, null, implID, params, false,
				execLocationID);

		final DataServiceAPI dsAPI = this.getRemoteExecutionEnvironment(execLocationID);
		return dsAPI.newPersistentInstance(sessionID, classID, implID, null, serializedParams);

	}

	/**
	 * This method creates a new Persistent Object using the provided stub instance and, if indicated, all its associated
	 * objects also Logic module API used for communication
	 * 
	 * @param dcObject
	 *            Instance to make persistent
	 * @param optionalDestBackendID
	 *            Indicates which is the destination backend
	 * @param recursive
	 *            Indicates if make persistent is recursive
	 * @param alias
	 *            Alias for the object
	 * @return ID of the backend in which te object was persisted.
	 * @note This function is called from a stub/execution class
	 */
	public abstract BackendID makePersistent(final DataClayObject dcObject, final BackendID optionalDestBackendID,
			final boolean recursive, final String alias);

	/**
	 * Federate an object with an external dataClay
	 * 
	 * @param objectID
	 *            id of the object
	 * @param extDataClayID
	 *            id of the external dataClay
	 * @param recursive
	 *            Indicates if subobjects should be federated as well
	 * @param classID
	 *            Class ID of the object
	 * @param hint
	 *            Hint of the object
	 */
	public void federateObject(final ObjectID objectID, final DataClayInstanceID extDataClayID, final boolean recursive,
			final MetaClassID classID, final BackendID hint) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==FederateObject==] Starting federation of object " + objectID + " with dataClay "
					+ extDataClayID);
		}
		final SessionID sessionID = checkAndGetSession(new String[] {}, new Object[] {});
		this.ensureObjectRegistered(sessionID, objectID, classID, hint);
		logicModule.federateObject(sessionID, objectID, extDataClayID, recursive);
	}

	/**
	 * Unfederate an object with an external dataClay
	 * 
	 * @param objectID
	 *            id of the object
	 * @param extDataClayID
	 *            id of the external dataClay
	 * @param recursive
	 *            Indicates if subobjects should be federated as well
	 */
	public void unfederateObject(final ObjectID objectID, final DataClayInstanceID extDataClayID, final boolean recursive) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==UnfederateObject==] Starting unfederation of object " + objectID + " with dataClay "
					+ extDataClayID);
		}
		final SessionID sessionID = checkAndGetSession(new String[] {}, new Object[] {});
		logicModule.unfederateObject(sessionID, objectID, extDataClayID, recursive);
		
		// FIXME: ALIAS CACHE SHOULD BE UPDATED FOR OBJECTS WITH ALIAS REMOVED?
	}
	
	/**
	 * Unfederate an object with all external dataClays
	 * 
	 * @param objectID
	 *            id of the object
	 * @param recursive
	 *            Indicates if subobjects should be federated as well
	 */
	public void unfederateObjectWithAllDCs(final ObjectID objectID, final boolean recursive) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==UnfederateObjectWithAllDCs==] Starting unfederation of object " + objectID 
					+ " with all external dataClays ");
		}
		final SessionID sessionID = checkAndGetSession(new String[] {}, new Object[] {});
		logicModule.unfederateObjectWithAllDCs(sessionID, objectID, recursive);
		// FIXME: ALIAS CACHE SHOULD BE UPDATED FOR OBJECTS WITH ALIAS REMOVED?
	}
	
	/**
	 * Unfederate all objects belonging/federated with external dataClay with id provided
	 * @param extDataClayID External dataClay ID
	 */
	public void unfederateAllObjects(final DataClayInstanceID extDataClayID) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==UnfederateAllObjects==] Starting unfederation of all objects with dataClay "
					+ extDataClayID);
		}
		final SessionID sessionID = checkAndGetSession(new String[] {}, new Object[] {});
		logicModule.unfederateAllObjects(sessionID, extDataClayID);
		
		// FIXME: ALIAS CACHE SHOULD BE UPDATED FOR OBJECTS WITH ALIAS REMOVED?

	}
	
	/**
	 * Unfederate all objects belonging/federated with ANY external dataClay 
	 */
	public void unfederateAllObjectsWithAllDCs() {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==UnfederateAllObjects==] Starting unfederation of all objects with ANY dataClay ");
		}
		final SessionID sessionID = checkAndGetSession(new String[] {}, new Object[] {});
		logicModule.unfederateAllObjectsWithAllDCs(sessionID);
		// FIXME: ALIAS CACHE SHOULD BE UPDATED FOR OBJECTS WITH ALIAS REMOVED?

	}
	
	/**
	 * Federate all dataClay objects from specified current dataClay
	 * destination dataclay. 
	 * @param destinationDataClayID Destination dataclay id
	 */
	public void federateAllObjects(
			final DataClayInstanceID destinationDataClayID) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==FederateAllObjects==] Starting federation of all objects from my dataClay to dataClay {} ", 
					destinationDataClayID);
		}
		final SessionID sessionID = checkAndGetSession(new String[] {}, new Object[] {});
		logicModule.federateAllObjects(sessionID, destinationDataClayID);
		// FIXME: ALIAS CACHE SHOULD BE UPDATED FOR OBJECTS WITH ALIAS REMOVED?

	}
	
	/**
	 * Migrate (unfederate and federate) all current dataClay objects from specified external dataclay di to
	 * destination dataclay. 
	 * @param originDataClayID Origin dataclay id
	 * @param destinationDataClayID Destination dataclay id
	 */
	public void migrateFederatedObjects(final DataClayInstanceID originDataClayID, 
			final DataClayInstanceID destinationDataClayID) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==MigrateFederatedObjects==] Starting migration of all objects from dataClay {} to dataClay {} ", 
					originDataClayID, destinationDataClayID);
		}
		final SessionID sessionID = checkAndGetSession(new String[] {}, new Object[] {});
		logicModule.migrateFederatedObjects(sessionID, originDataClayID, destinationDataClayID);
		// FIXME: ALIAS CACHE SHOULD BE UPDATED FOR OBJECTS WITH ALIAS REMOVED?

	}

	/**
	 * This function calls "filterObject" in backends for the type of the object provided. It means that given an object and
	 * some conditions, search for all objects of same class that accomplish the conditions provided.
	 * 
	 * @param dcObject
	 *            Instance in which filterObject function was called.
	 * @param conditions
	 *            Conditions to accomplish.
	 * @return All objects that belong to same class than object provided and accomplish conditions.
	 */
	@SuppressWarnings("unchecked")
	public List<Object> filterObject(final DataClayObject dcObject, final String conditions) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==FilterObject==] Filtering obejct " + dcObject.getID() + " with conditions " + conditions);
		}
		final SessionID sessionID = getSessionID();
		// ===== SERIALIZE PARAMETERS ===== //
		// Between DS - DS, ifaceBitMaps = null
		// Serialize parameters
		SerializedParametersOrReturn serResult = null;
		boolean executed = false;
		BackendID execLocationID = dcObject.getHint();
		DataServiceAPI dsAPI = null;
		short numMisses = 0;
		final Map<MetaClassID, byte[]> ifaceBitMaps = null; // TODO: add iface bitmaps? (too expensive?)

		while (!executed && numMisses < Configuration.Flags.MAX_EXECUTION_RETRIES.getShortValue()) {
			try {
				if (DEBUG_ENABLED) {
					final ExecutionEnvironment execEnv = this.getExecutionEnvironmentInfo(execLocationID);
					LOGGER.debug("[==JUMP==] Request execution to  " + execEnv.getName() + " for object "
							+ dcObject.getObjectID());
				}

				dsAPI = getRemoteExecutionEnvironment(execLocationID);
				serResult = dsAPI.filterObject(sessionID, dcObject.getObjectID(), conditions);
				executed = true;
			} catch (StatusRuntimeException | DataClayException er) {

				numMisses++;

				// =================== UPDATE METADATA CACHE (due to a MISS) ===================
				// //
				// If remote DS sends a DbObjectNotExists means that it might be possible that
				// THIS DataService contains wrong information
				// in its cache and must remove it and seek for new one.
				metaDataCache.remove(dcObject.getObjectID());

				execLocationID = getLocation(dcObject.getObjectID());
				// execLocationID = getExecutionEnvironmentLocation(dcObject.getObjectID());

				if (DEBUG_ENABLED) {
					LOGGER.debug("[==Hint==] Setting hint (Retry remote execution) on instance "
							+ dcObject.getObjectID() + " the hint : " + getDSNameOfHint(execLocationID));
				}
				dcObject.setHint(execLocationID);

				if (DEBUG_ENABLED) {
					LOGGER.debug("[==Miss Jump==] MISS. The object " + dcObject.getObjectID()
					+ " was not in the exec.location " + execLocationID + ". Retrying execution.");
				}
			}

		}

		if (dsAPI == null) {
			throw new DataClayException(ERRORCODE.UNEXPECTED_EXCEPTION,
					"[dataClay] ERROR: Trying to execute remotely but"
							+ " not initialized/found. Please, check initialization of StorageItf or ClientManagementLib "
							+ " was successfull or contact administrator.",
							true);
		}

		if (!executed) {
			throw new RuntimeException("[dataClay] ERROR: Trying to execute remotely object " + dcObject.getObjectID()
			+ " of class " + dcObject.getClass().getName()
			+ " but something went wrong. Maybe the object is still not stored "
			+ " (in case of asynchronous makepersistent) and waiting time is not enough."
			+ " Maybe the object does not exist anymore due to a remove. Or Maybe an "
			+ "exception happened in the server and the call failed.");
		}

		// ===== DESERIALIZE RETURN ===== //
		// Deserialize return
		return (List<Object>) DataClayDeserializationLib.deserializeParamsOrReturn(serResult, ifaceBitMaps, this)[0];
	}

	/**
	 * Set DataSet ID
	 * 
	 * @param objectID
	 *            ID of the object
	 * @param newDataSetID
	 *            New DataSetID
	 */
	public final void setDataSetID(final ObjectID objectID, final DataSetID newDataSetID) {
		final SessionID sessionID = checkAndGetSession(new String[] { "newDataSetID" }, new Object[] { newDataSetID });
		this.logicModule.setDataSetID(sessionID, objectID, newDataSetID);

	}

	/**
	 * Recovers Object from OID and class ID
	 * 
	 * @param objectID
	 *            ObjectID of the object
	 * @param classID
	 *            ClassID of the object. Can be NULL. Null class ID makes dataClay to ask for object Mdata.
	 * @param hint
	 *            Hint of the object. Can be NULL if no hint.
	 * @return Instance of the object.
	 */
	public final DataClayObject getPersistedObjectByOID(final ObjectID objectID, final MetaClassID classID,
			final BackendID hint) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==GetByOID==] Getting/Creating instance from getPersistedObjectByOID.");
		}
		return getOrNewPersistentInstance(classID, objectID, hint);
	}

	/**
	 * Wait for asynchronous requests to finish.
	 */
	public final void waitForAsyncRequestToFinish() {

		// Client traces
		grpcClient.waitAndProcessAllAsyncRequests();
	}

	/**
	 * Get Heap Manager
	 * 
	 * @return Heap Manager
	 */
	public abstract HeapManager getDataClayHeapManager();

	/**
	 * Remove reference from Heap. Even if we remove it from the heap, the object won't be Garbage collected by JavaGC till
	 * HeapManager flushes the object and releases it.
	 * 
	 * @param objectID
	 *            ID of the object
	 */
	public final void removeFromHeap(final ObjectID objectID) {
		this.dataClayHeapManager.removeFromHeap(objectID);
	}

	/**
	 * Add to Heap
	 * 
	 * @param dcObject
	 *            the object
	 */
	public final void addToHeap(final DataClayObject dcObject) {
		this.dataClayHeapManager.addToHeap(dcObject);
	}

	/**
	 * Check if object exists in dataClay.
	 * 
	 * @param objectID
	 *            ID of the object
	 * @return TRUE if object exists. FALSE otherwise.
	 */
	public boolean objectExistsInDataClay(final ObjectID objectID) {
		return this.logicModule.objectExistsInDataClay(objectID);
	}

	/**
	 * Get from Heap
	 * 
	 * @param objectID
	 *            ID of the object
	 * @return The object in Heap or null if not present
	 */
	public DataClayObject getFromHeap(final ObjectID objectID) {
		return this.dataClayHeapManager.getObject(objectID);
	}

	/**
	 * Check if there is an object with ID provided.
	 * 
	 * @param objectID
	 *            ID of the object.
	 * @return TRUE if exists in memory. FALSE otherwise.
	 */
	public final boolean existsInHeap(final ObjectID objectID) {
		return dataClayHeapManager.existsObject(objectID);
	}

	/**
	 * ADVANCED FUNCTION. Try not to use it. This function flushes all objects in Heap.
	 */
	public final void flushAll() {
		this.dataClayHeapManager.flushAll();
	}

	/**
	 * Get number of objects in heap.
	 * 
	 * @return Heap size.
	 */
	public final int heapSize() {
		return this.dataClayHeapManager.heapSize();
	}

	/**
	 * Activate tracing in dataClay services
	 * 
	 */
	public final void activateTracingInDataClayServices() {
		if (DataClayExtrae.extraeTracingIsEnabled()) { //sanity check, only activate if extrae was properly initialized
			this.logicModule.activateTracing(DataClayExtrae.getCurrentAvailableTaskID());
		}
	}

	/**
	 * Dectivate tracing
	 */
	public final void deactivateTracingInDataClayServices() {
		if (DataClayExtrae.extraeTracingIsEnabled()) { //sanity check, only activate if extrae was properly initialized
			this.logicModule.deactivateTracing();
		}
	}

	/**
	 * Activate tracing
	 */
	public final void activateTracing() {
		DataClayExtrae.initializeExtrae(false);
	}

	/**
	 * Deactivate tracing
	 */
	public final void deactivateTracing() {
		DataClayExtrae.finishTracing();
	}

	/**
	 * Get traces in dataClay services and store it in current workspace
	 */
	public final void getTracesInDataClayServices() {
		final Map<String, byte[]> traces = logicModule.getTraces();
		final String setPath = Configuration.Flags.TRACES_DEST_PATH.getStringValue() + File.separatorChar + "set-0";
		final String traceMpitsPath = Configuration.Flags.TRACES_DEST_PATH.getStringValue() + File.separatorChar + "TRACE.mpits";
		try {
			FileUtils.forceMkdir(new File(setPath));
			for (final Entry<String, byte[]> traceFile : traces.entrySet()) { 
				final String fileName = traceFile.getKey();
				final byte[] fileBytes = traceFile.getValue();

					final String path = setPath + File.separator + fileName;
					if (DEBUG_ENABLED) { 
						LOGGER.debug("Storing file " + path);
					}
					// Store Extrae temporary files
					FileUtils.writeByteArrayToFile(new File(path), fileBytes);
					
					if (fileName.endsWith(".mpit")) {
						final String newFilePointer = path + " named\n";
						if (DEBUG_ENABLED) { 
							LOGGER.debug("Adding line to " + traceMpitsPath + " file: " + newFilePointer);
						}
						FileUtils.writeStringToFile(new File(traceMpitsPath), newFilePointer,Charset.defaultCharset(), true);
					}
					
			}
		} catch (final IOException e) {
			if (DEBUG_ENABLED) { 
				LOGGER.debug("Exception produced while storing file ", e);
			}
		}
		
		
		
		
	}

	/**
	 * @return the metaDataCache
	 */
	public final LruCache<ObjectID, MetaDataInfo> getMetaDataCache() {
		return metaDataCache;
	}

	/**
	 * @param themetaDataCache
	 *            the metaDataCache to set
	 */
	public final void setMetaDataCache(final LruCache<ObjectID, MetaDataInfo> themetaDataCache) {
		this.metaDataCache = themetaDataCache;
	}

	/**
	 * @return GRPC client.
	 */
	public final CommonGrpcClient getCommonGrpcClient() {
		return this.grpcClient;
	}

	/**
	 * Get DataClay object loader.
	 * 
	 * @return Object loader.
	 */
	protected abstract DataClayObjectLoader getDataClayObjectLoader();

	/**
	 * Lock object
	 * 
	 * @param objectID
	 *            ID of object
	 */
	public final void lock(final ObjectID objectID) {
		this.lockerPool.lock(objectID);
	}

	/**
	 * Unlock object
	 * 
	 * @param objectID
	 *            ID of object
	 */
	public final void unlock(final ObjectID objectID) {
		this.lockerPool.unlock(objectID);
	}

	/**
	 * Clean lockers
	 */
	public final void cleanLockers() {
		this.lockerPool.cleanLockers();
	}

	/**
	 * Get number of lockers
	 * 
	 * @return Number of lockers
	 */
	public final int numLockers() {
		return this.lockerPool.numLockers();
	}

	/**
	 * @return TRUE if library is for DataClay, FALSE for clients.
	 */
	public abstract boolean isDSLib();

	/**
	 * @return the 'LOCAL' location if defined
	 */
	public abstract BackendID getLocalBackend();

	/**
	 * ONLY for EE. Add +1 reference associated to thread session
	 * 
	 * @param objectID
	 *            ID of the object
	 */
	public void addSessionReference(final ObjectID objectID) {
		// do nothing: TODO: specialize it in a better way
	}

	/**
	 * Indicates if runtime was initialized or not
	 * 
	 * @return TRUE if runtime is initialized. FALSE otherwise.
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Set initialized flag
	 * 
	 * @param theinitialized
	 *            Value to set
	 */
	public void setInitialized(final boolean theinitialized) {
		this.initialized = theinitialized;
	}

	public static ObjectID getObjectIDFromAlias(String alias) {
		return new ObjectID(UUID.nameUUIDFromBytes(alias.getBytes()));
	}

	protected ExecutionEnvironmentID getBackendIDFromAlias(String alias) {
		return this.getBackendIDFromObjectID(getObjectIDFromAlias(alias));
	}

	/**
	 * Choose execution/make persistent location.
	 *
	 * @param dcObject
	 *            DataClay object.
	 * @return Chosen location.
	 */
	protected BackendID chooseLocation(final DataClayObject dcObject, final String alias) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==Execution==] Using Hash execution location for " + dcObject.getObjectID());
		}

		final BackendID location;

		if(alias != null) {
			location = getBackendIDFromAlias(alias);
		}else {
			location = getBackendIDFromObjectID(dcObject.getObjectID());
		}

		dcObject.setHint(location);
		return location;
	}

	/**
	 * Update the object id in both DataClayObject and HeapManager
	 *
	 * @param dcObject
	 *            DataClay object.
	 * @param newObjectID
	 *            the new object id.
	 */
	protected void updateObjectID(DataClayObject dcObject, ObjectID newObjectID) {
		final ObjectID oldObjectID = dcObject.getObjectID();
		dcObject.setObjectIDUnsafe(newObjectID);
		dataClayHeapManager.updateObjectID(oldObjectID, newObjectID);
	}
}
