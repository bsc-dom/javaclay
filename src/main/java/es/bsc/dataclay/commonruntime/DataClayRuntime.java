package es.bsc.dataclay.commonruntime;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

import es.bsc.dataclay.util.management.metadataservice.*;
import es.bsc.dataclay.util.structs.MemoryCache;
import es.bsc.dataclay.util.structs.Tuple;
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
import es.bsc.dataclay.exceptions.metadataservice.ObjectAlreadyRegisteredException;
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
import es.bsc.dataclay.util.management.stubs.ImplementationStubInfo;
import es.bsc.dataclay.util.management.stubs.StubInfo;
import es.bsc.dataclay.util.structs.MemoryCache;
import es.bsc.dataclay.util.structs.Triple;
import io.grpc.StatusRuntimeException;
import org.apache.logging.log4j.core.LifeCycle;
import es.bsc.dataclay.metadata.MetadataServiceAPI;

import javax.xml.crypto.Data;

/**
 * This class contains functions to interact with DataClay. This is an abstract
 * class in order to provide same functionalities
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

	/** Current dataClay instance ID. */
	private DataClayInstanceID dataClayInstanceID;

	/**
	 * DataClay Heap manager to manage GC, reference counting, lockers and others.
	 */
	protected HeapManager dataClayHeapManager;

	/** Pool of lockers. */
	protected final LockerPool lockerPool = new LockerPool();

	/** Execution Environments cache. */
	private Map<ExecutionEnvironmentID, ExecutionEnvironment> execEnvsCache = new ConcurrentHashMap<>();

	/** Cache of metaData. */
	public MemoryCache<ObjectID, MetaDataInfo> metaDataCache = new MemoryCache<>();

	/** Cache of alias -> oid. */
	protected MemoryCache<String, Triple<ObjectID, MetaClassID, BackendID>> aliasCache;

	/** DataClay object loader. */
	public DataClayObjectLoader dataClayObjLoader;

	/** Under deserialization volatiles per thread. */
	public final Map<ObjectID, ObjectWithDataParamOrReturn> underDeserializationVolatiles = new ConcurrentHashMap<>();

	/** Pool for tasks. Initialized in sub-classes. */
	protected ScheduledExecutorService threadPool;

	/**
	 * Set of object ids of volatile parameters that were send but did not arrive to
	 * any node yet.
	 */
	protected final Set<ObjectID> volatileParametersBeingSend = ConcurrentHashMap.newKeySet();

	public int misses = 0;
	public int hits = 0;

	protected MetadataServiceAPI metadataService;

	// CHECKSTYLE:ON

	// ================================================== //
	// ================= COMMUNICATION ================== //
	// ================================================== //

	/**
	 * Constructor.
	 */
	protected DataClayRuntime() {
		this.aliasCache = new MemoryCache<>();
	}

	/**
	 * Initialize session and connections.
	 * 
	 * @param logicModuleHost
	 *                        Name of the host of the logic module
	 * @param logicModulePort
	 *                        Port of the logic module
	 * @param originHostName
	 *                        Name of the host using the lib.
	 * @throws Exception
	 *                   if connection could not be done for some reason.
	 */
	protected void initialize(final String logicModuleHost, final int logicModulePort, final String originHostName)
			throws Exception {
		LOGGER.info("Connecting to LM at {}:{} ...", logicModuleHost, logicModulePort);
		grpcClient = new CommonGrpcClient(originHostName);
		logicModule = grpcClient.getLogicModuleAPI(logicModuleHost, logicModulePort);
		LOGGER.info("Connected to LM!");
		this.setInitialized(true);
	}

	public final MetadataServiceAPI getMetadataServiceAPI() {
		return this.metadataService;
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
	 * 
	 * @param lang             Language
	 * @param forceUpdateCache Indicates cache must be forcibly updated
	 * @return All execution locations information
	 */
	public final Map<ExecutionEnvironmentID, ExecutionEnvironment> getAllExecutionEnvironmentsInfo(
			final Langs lang, final boolean forceUpdateCache) {
		// Check cache
		if ((execEnvsCache != null && forceUpdateCache) || (execEnvsCache == null)) {
			execEnvsCache.putAll(logicModule.getAllExecutionEnvironmentsInfo(lang, true, this.isDSLib()));
		}
		return execEnvsCache;

	}

	/**
	 * Get ExecutionEnvironment information
	 *
	 * @param execLocationID
	 *                       Execution location ID
	 * @return Execution location information
	 */
	public final ExecutionEnvironment getExecutionEnvironmentInfo(final BackendID execLocationID) {
		ExecutionEnvironment execEnv = null;
		for (Langs language : Langs.values()) {
			Map<ExecutionEnvironmentID, ExecutionEnvironment> execEnvs = getAllExecutionEnvironmentsInfo(language,
					false);
			execEnv = execEnvs.get(execLocationID);
			if (execEnv == null) {

				LOGGER.debug(
						"Execution environment info " + execLocationID + " not found in cache: " + execEnvs.keySet());
				execEnvs = getAllExecutionEnvironmentsInfo(language, true);
				execEnv = execEnvs.get(execLocationID);
				if (execEnv != null) {
					break;
				}
			} else {
				break;
			}
		}
		return execEnv;
	}

	/**
	 * Get all execution environments in provided host
	 * 
	 * @param lang     Language
	 * @param hostname Host name
	 * @return Set of execution environments in provided host
	 */
	public Map<BackendID, ExecutionEnvironment> getAllExecutionEnvironmentsAtHost(final Langs lang,
			final String hostname) {
		Map<BackendID, ExecutionEnvironment> execEnvsAtHost = new HashMap<>();
		Map<ExecutionEnvironmentID, ExecutionEnvironment> execEnvs = getAllExecutionEnvironmentsInfo(lang, false);
		// check if there is any execution.env in that host, otherwise update cache
		for (ExecutionEnvironment env : execEnvs.values()) {
			LOGGER.debug("Checking if environment hostname {} matches required hostname {}", env.getHostname(),
					hostname);
			if (env.getHostname().equals(hostname)) {
				execEnvsAtHost.put(env.getDataClayID(), env);
			}
		}
		if (execEnvsAtHost.isEmpty()) {
			execEnvs = getAllExecutionEnvironmentsInfo(lang, true);
			for (ExecutionEnvironment env : execEnvs.values()) {
				if (env.getHostname().equals(hostname)) {
					execEnvsAtHost.put(env.getDataClayID(), env);
				}
			}
		}
		return execEnvsAtHost;
	}

	/**
	 * Get all execution environments in provided dataClay instance
	 * 
	 * @param lang               Language
	 * @param dataClayInstanceID ID of dataClay to check
	 * @param forceUpdate        Indicates exec envs must be updated
	 * @return Set of execution environments in provided dataClay instance
	 */
	public Map<ExecutionEnvironmentID, ExecutionEnvironment> getAllExecutionEnvironmentsAtDataClay(final Langs lang,
			final DataClayInstanceID dataClayInstanceID,
			final boolean forceUpdate) {
		Map<ExecutionEnvironmentID, ExecutionEnvironment> execEnvsAtDC = new HashMap<>();
		Map<ExecutionEnvironmentID, ExecutionEnvironment> execEnvs = null;
		if (!forceUpdate) {
			execEnvs = getAllExecutionEnvironmentsInfo(lang, false);
			// check if there is any execution.env in that host, otherwise update cache
			for (ExecutionEnvironment env : execEnvs.values()) {
				LOGGER.debug("Checking if environment hostname {} matches required hostname {}",
						env.getDataClayInstanceID(), dataClayInstanceID);
				if (env.getDataClayInstanceID().equals(dataClayInstanceID)) {
					execEnvsAtDC.put(env.getDataClayID(), env);
				}
			}
		}
		if (execEnvsAtDC.isEmpty()) {
			execEnvs = getAllExecutionEnvironmentsInfo(lang, true);
			for (ExecutionEnvironment env : execEnvs.values()) {
				if (env.getDataClayInstanceID().equals(dataClayInstanceID)) {
					execEnvsAtDC.put(env.getDataClayID(), env);
				}
			}
		}
		return execEnvsAtDC;
	}

	/**
	 * Get all backend names
	 * 
	 * @param lang             Language
	 * @param forceUpdateCache Indicates cache must be forcibly updated
	 * @return All backed names information
	 */
	public Set<String> getAllBackendsNames(final Langs lang, final boolean forceUpdateCache) {
		Set<String> result = null;
		result = new HashSet<>();
		Collection<ExecutionEnvironment> execEnvs = getAllExecutionEnvironmentsInfo(lang, true).values();
		DataClayInstanceID curDcID = getDataClayID();
		for (ExecutionEnvironment execEnv : execEnvs) {
			if (execEnv.getDataClayInstanceID().equals(curDcID)) {
				result.add(execEnv.getName() + ":" + execEnv.getHostname() + ":" + execEnv.getPort());
			}
		}
		return result;
	}

	/**
	 * Get exec. environments with name provided
	 * 
	 * @param lang        Language
	 * @param backendName Name of backend
	 * @return All backends with name provided
	 */
	public Set<BackendID> getBackendsWithName(final Langs lang, final String backendName) {
		Set<BackendID> execEnvsWithName = new HashSet<>();
		Map<ExecutionEnvironmentID, ExecutionEnvironment> execEnvs = getAllExecutionEnvironmentsInfo(Langs.LANG_JAVA,
				false);
		// check if there is any execution.env in that host, otherwise update cache
		DataClayInstanceID curDcID = getDataClayID();
		for (ExecutionEnvironment env : execEnvs.values()) {
			if (env.getName().equals(backendName) && env.getDataClayInstanceID().equals(curDcID)) {
				execEnvsWithName.add(env.getDataClayID());
			}
		}
		if (execEnvsWithName.isEmpty()) {
			execEnvs = getAllExecutionEnvironmentsInfo(Langs.LANG_JAVA, true);
			for (ExecutionEnvironment env : execEnvs.values()) {
				if (env.getName().equals(backendName) && env.getDataClayInstanceID().equals(curDcID)) {
					execEnvsWithName.add(env.getDataClayID());
				}
			}
		}
		return execEnvsWithName;
	}

	/**
	 * Get execution location
	 * 
	 * @param objectID
	 *                 ID of the object connected.
	 * @return ExecutionEnvironmentID by hash
	 */
	public final ExecutionEnvironmentID getBackendIDFromObjectID(final ObjectID objectID) {
		// Apply hash to choose which DS to go.
		List<BackendID> allEEs = new ArrayList<>(this.getAllExecutionEnvironmentsAtDataClay(Langs.LANG_JAVA,
				this.getDataClayID(), false).keySet());
		final int hashCode = objectID.hashCode();
		final int whichDS = hashCode % allEEs.size();
		final int hash = Math.abs(whichDS);
		final ExecutionEnvironmentID stLocID = (ExecutionEnvironmentID) allEEs.get(hash);
		return stLocID;
	}

	/**
	 * Get remote execution environment
	 * 
	 * @param execLocationID
	 *                       ID of remote execution environment
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
	 *                       ID of remote execution environment
	 * @return Remote execution environment
	 */
	public DataServiceAPI getRemoteDSAPI(final ExecutionEnvironmentID execLocationID) {
		ExecutionEnvironment execEnv = this.getExecutionEnvironmentInfo(execLocationID);
		try {
			return grpcClient.getDataServiceAPI(execEnv.getHostname(), execEnv.getPort());
		} catch (final InterruptedException ex) {
			LOGGER.debug("getRemoteExecutionEnvironmentForDS", ex);
			// Force to be runtime exc. so Clients can receive the exception
			throw new RuntimeException(ex.getCause());
		}
	}

	/**
	 * Get external dataClay info
	 * 
	 * @param extDataClayID
	 *                      id of the external dataClay instance
	 * @return info of the external dataClay instance
	 */
	public DataClayInstance getExternalDataClayInfo(final DataClayInstanceID extDataClayID) {
		return this.getLogicModuleAPI().getExternalDataClayInfo(extDataClayID);
	}

	/**
	 * Method that registers the info of a dataClay instance
	 * 
	 * @param dcHost
	 *               entry port host of the external dataClay
	 * @param dcPort
	 *               entry point port of the external dataClay
	 * @return ID of external registered dataClay.
	 */
	public DataClayInstanceID registerExternalDataClay(final String dcHost, final Integer dcPort) {
		return this.getLogicModuleAPI().registerExternalDataClay(dcHost, dcPort);

	}

	/**
	 * ADMIN usage only. Method that registers the info of a dataClay instance but
	 * with overriden authority for SSL connections.
	 * 
	 * @param adminAccountID  admin account id
	 * @param adminCredential admin credentials
	 * @param dcHost
	 *                        entry port host of the external dataClay
	 * @param dcPort
	 *                        entry point port of the external dataClay
	 * @param authority       authority to use
	 * @return ID of external registered dataClay.
	 */
	public DataClayInstanceID registerExternalDataClayOverrideAuthority(final AccountID adminAccountID,
			final PasswordCredential adminCredential, final String dcHost, final int dcPort, final String authority) {
		return this.getLogicModuleAPI().registerExternalDataClayOverrideAuthority(adminAccountID, adminCredential,
				dcHost, dcPort, authority);

	}

	/**
	 * Get external dataClay info
	 * 
	 * @param hostname
	 *                 host name of the external dataClay instance
	 * @param port
	 *                 port of the external dataClay instance.
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
		if (this.dataClayInstanceID == null) {
			dataClayInstanceID = this.getLogicModuleAPI().getDataClayID();
		}
		return dataClayInstanceID;
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
		LOGGER.info("Finishing connections ... ");
		LOGGER.debug("Stopping Grpc connections");
		this.grpcClient.finishClientConnections();
		LOGGER.debug("Stopping thread pool");
		this.threadPool.shutdown();
		this.threadPool = null;
		this.grpcClient = null;
		this.dataClayInstanceID = null;
		boolean aliveThreads = true;
		DataClayObject.clearStubInfosCache();
		DataClayObject.clearExecStubInfosCache();
		DataClayObject.clientRuntime = null;
		for (int i = 0; i < 10; i++) { // maximum retries
			boolean foundAliveThread = false;
			String threadName = null;
			for (Thread t : Thread.getAllStackTraces().keySet()) {
				threadName = t.getName();
				if (threadName.startsWith("grpc-")) {
					foundAliveThread = true;
					break;
				}
			}
			if (foundAliveThread) {
				LOGGER.warn("WARNING: Waiting for " + threadName + " thread to finish...");
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				aliveThreads = false;
				break;
			}
		}
		for (Thread t : Thread.getAllStackTraces().keySet()) {
			String threadName = t.getName();
			LOGGER.debug("Found alive thread while exiting: " + threadName);
		}
		if (aliveThreads) {
			LOGGER.warn("WARNING: Some threads still alive while exiting application.");
		}
		// ((LifeCycle) LogManager.getContext()).stop();
		this.setInitialized(false);
	}

	/**
	 * Get the IDs of the backends in which the object identified by the stub
	 * instance provided is located and the classname of
	 * the object.
	 * 
	 * @param objectID
	 *                 ID of the object
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
							"[==Metadata Cache==] Adding entry in metadata cache for {} : {}",
							objectID, mdInfo);
				}
			}
			if (mdInfo != null) {
				metaDataCache.put(objectID, mdInfo);
			}

		} else {
			hits++;
			if (DEBUG_ENABLED) {
				LOGGER.debug("[==Metadata Cache==] Found entry in metadata cache for {} : {}", objectID,
						mdInfo);
			}
		}
		return mdInfo;
	}

	/**
	 * Remove metadata of object from cache
	 *
	 * @param objectID
	 *                 ID of the object
	 */
	public final void removeObjectMetadataFromCache(final ObjectID objectID) {
		metaDataCache.remove(objectID);
	}

	// ================================================== //
	// ================= FUNCTIONS ================== //
	// ================================================== //

	/**
	 * Retrieves a copy of the specified object and all its subobjects
	 * 
	 * @param oid
	 *                  id of the object to be retrieved
	 * @param recursive
	 *                  retrieve a copy of the whole object copying also its
	 *                  subobjects or only the main object
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
	 *             target object where data is put
	 * @param from
	 *             object containing the data to put
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
	 * All volatiles provided are under deserialization. This function solves
	 * problems of 'hashcode' and other special functions
	 * needed during deserializations. See executeImpl.
	 * 
	 * @param volatileSet
	 *                    Volatiles under deserialization.
	 */
	public void addVolatileUnderDeserialization(final Collection<ObjectWithDataParamOrReturn> volatileSet) {
		for (ObjectWithDataParamOrReturn vol : volatileSet) {
			underDeserializationVolatiles.put(vol.getObjectID(), vol);
		}
	}

	/**
	 * Remove volatiles under deserialization.
	 * 
	 * @param volatileSet
	 *                    Volatiles under deserialization.
	 */
	public void removeVolatilesUnderDeserialization(final Collection<ObjectWithDataParamOrReturn> volatileSet) {
		for (ObjectWithDataParamOrReturn vol : volatileSet) {
			underDeserializationVolatiles.remove(vol.getObjectID());
		}

	}

	/**
	 * Get from Heap or create a new volatile in EE and load data on it.
	 * 
	 * @param classID
	 *                     ID of class of the object
	 * @param objectID
	 *                     ID of the object
	 * @param hint
	 *                     Hint of the object
	 * @param objWithData
	 *                     Data of the object
	 * @param ifaceBitMaps
	 *                     Interface bitmaps
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
	 *                   Names of parameters
	 * @param params
	 *                   Parameters to check
	 */
	public final void checkConnectionAndParams(final String[] paramNames, final Object[] params) {
		RuntimeUtils.checkConnection(logicModule);
		RuntimeUtils.checkNullParams(paramNames, params);
	}

	/**
	 * Check parameters and connections and session
	 * 
	 * @param paramNames
	 *                   Names of parameters
	 * @param params
	 *                   Parameters to check
	 * @return Session ID
	 */
	public final SessionID checkAndGetSession(final String[] paramNames, final Object[] params) {
		checkConnectionAndParams(paramNames, params);
		final SessionID sessionID = getSessionID();
		RuntimeUtils.checkSession(sessionID);
		return sessionID;
	}

	/**
	 * Method that gets info of an object given its ID if the object is accessible
	 * by using the given sesion.
	 * 
	 * @param alias
	 *              alias of the object
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
	 *              alias of the object
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
	 *                    alias of the object
	 * @param metaClassID
	 *                    if of the object's metaclass
	 * @param safe
	 *                    if true, check that alias exists
	 * @return The object identified by the proved alias and metaclass.
	 */
	public final DataClayObject getObjectByAlias(final String alias, MetaClassID metaClassID, boolean safe) {
		if (safe) {
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
	 *              alias of the object to be removed
	 */
	public final void deleteAlias(final String alias) {
		final SessionID sessionID = getSessionID();
		ObjectID objectID = logicModule.deleteAlias(sessionID, alias);
		aliasCache.remove(alias);
	}

	/**
	 * Method that deletes the alias of an object provided
	 * 
	 * @param dcObject the object
	 */
	public abstract void deleteAlias(final DataClayObject dcObject);

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
	 *                ID of class
	 * @return Class name
	 */
	public abstract String getClassNameInternal(final MetaClassID classID);

	/**
	 * Get class name from ID
	 * 
	 * @param classID
	 *                Class ID
	 * @return Class name
	 */
	public final String getClassName(final MetaClassID classID) {
		checkAndGetSession(new String[] { "classID" }, new Object[] { classID });

		return getClassNameInternal(classID);
	}

	/**
	 * Helper function to prepare information for new replica - version -
	 * consolidate algorithms
	 * 
	 * @param objectID          ID of the object
	 * @param objectHint        object hint
	 * @param optDestBackendID  Destination backend ID to get information from (can
	 *                          be null)
	 * @param optDestHostname   Destination hostname to get information from (can be
	 *                          null)
	 * @param differentLocation if true, indicates that destination backend should
	 *                          be different than any location of the object
	 * @return Tuple with destination backend API to call and:
	 *         Either information of dest backend with id provided, some exec env in
	 *         host provided or random exec env.
	 */
	private final Tuple<DataServiceAPI, ExecutionEnvironment> prepareNewReplicaVersionConsolidate(
			final ObjectID objectID,
			final BackendID objectHint,
			final BackendID optDestBackendID,
			final String optDestHostname,
			final boolean differentLocation) {
		// Get an arbitrary object location
		BackendID execLocationID = objectHint;
		if (objectHint == null) {
			execLocationID = getLocation(objectID);
		}
		// Get language from origin location
		Langs objectLanguage = this.getExecutionEnvironmentInfo(execLocationID).getLang();
		final DataServiceAPI dsAPI = getRemoteExecutionEnvironment(execLocationID);
		ExecutionEnvironmentID destBackendID = (ExecutionEnvironmentID) optDestBackendID;
		ExecutionEnvironment destBackend = null;
		if (destBackendID == null) {
			if (optDestHostname != null) {
				// Get some execution environment in that host
				Map<BackendID, ExecutionEnvironment> execsInHost = getAllExecutionEnvironmentsAtHost(objectLanguage,
						optDestHostname);
				if (execsInHost != null && !execsInHost.isEmpty()) {
					destBackend = execsInHost.values().iterator().next();
					destBackendID = destBackend.getDataClayID();
				}
			}
			if (destBackend == null) {
				// no destination backend specified, get one randomly in which object is
				// not registered
				// === RANDOM === //
				Map<ExecutionEnvironmentID, ExecutionEnvironment> backends = this
						.getAllExecutionEnvironmentsAtDataClay(objectLanguage, this.getDataClayID(), false);

				if (differentLocation) {
					Set<BackendID> locations = this.getAllLocations(objectID);
					for (Entry<ExecutionEnvironmentID, ExecutionEnvironment> eeEntry : backends.entrySet()) {
						ExecutionEnvironmentID eeID = eeEntry.getKey();
						ExecutionEnvironment execEnv = eeEntry.getValue();
						if (!locations.contains(eeID)) {
							destBackendID = eeID;
							destBackend = execEnv;
							break;
						}
					}
					if (destBackend == null) {
						LOGGER.debug("Could not find any different location for replica, updating available exec envs");

						backends = this.getAllExecutionEnvironmentsAtDataClay(objectLanguage, this.getDataClayID(),
								true);
						for (Entry<ExecutionEnvironmentID, ExecutionEnvironment> eeEntry : backends.entrySet()) {
							ExecutionEnvironmentID eeID = eeEntry.getKey();
							ExecutionEnvironment execEnv = eeEntry.getValue();
							if (!locations.contains(eeID)) {
								destBackendID = eeID;
								destBackend = execEnv;
								break;
							}
						}
					}
				}
				if (destBackend == null) {
					destBackendID = (ExecutionEnvironmentID) execLocationID;
					destBackend = this.getExecutionEnvironmentInfo(destBackendID);
				}
			}
		} else {
			destBackend = getExecutionEnvironmentInfo(destBackendID);
		}

		return new Tuple<>(dsAPI, destBackend);
	}

	/**
	 * Creates a persistent new version of an object and its subobjects (always
	 * recursive). If a Backend is provided the object
	 * is versioned to this backend, otherwise it is versioned to any backend
	 * 
	 * @param objectID
	 *                         ID of the object
	 * @param objectHint       object hint
	 * @param classID          ID of the class of the object
	 * @param dataSetID        ID of the dataset of the object
	 * @param optDestBackendID ID of the backend in which to store the version the
	 *                         object (optional)
	 * @param optDestHostname  Hostname of the backend in which to replicate the
	 *                         object (optional)
	 * @return Version ID and destination backend ID
	 */
	public final Tuple<ObjectID, BackendID> newVersion(final ObjectID objectID,
			final ExecutionEnvironmentID objectHint,
			final MetaClassID classID, final DataSetID dataSetID,
			final BackendID optDestBackendID, final String optDestHostname) {
		final SessionID sessionID = checkAndGetSession(new String[] { "ObjectID" }, new Object[] { objectID });

		// FIXME: retry in different replica location if it fails
		Tuple<DataServiceAPI, ExecutionEnvironment> destInfo = this.prepareNewReplicaVersionConsolidate(objectID,
				objectHint,
				optDestBackendID, optDestHostname, false);
		DataServiceAPI dsAPI = destInfo.getFirst();
		ExecutionEnvironment destBackend = destInfo.getSecond();
		ExecutionEnvironmentID destBackendID = destBackend.getDataClayID();
		ObjectID newVersionOID = dsAPI.newVersion(sessionID, objectID, destBackendID);

		// update cache of metadata info
		Set<ExecutionEnvironmentID> locations = new HashSet<>();
		locations.add((ExecutionEnvironmentID) destBackendID);
		MetaDataInfo newMetaDataInfo = new MetaDataInfo(newVersionOID,
				dataSetID, classID, false, locations, null, null);
		this.metaDataCache.put(newVersionOID, newMetaDataInfo);
		return new Tuple<>(newVersionOID, destBackendID);
	}

	/**
	 * Makes the object with finalVersionID the definitive version of the object
	 * with originalObjectID. The original version is
	 * deleted.
	 * 
	 * @param versionOID ID of version object to consolidate
	 * @param objectHint object hint
	 */
	public final void consolidateVersion(final ObjectID versionOID, final ExecutionEnvironmentID objectHint) {
		// FIXME: retry in different replica location if it fails
		final SessionID sessionID = checkAndGetSession(new String[] { "versionOID" }, new Object[] { versionOID });
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==Consolidate==] Consolidate version " + "with oid " + versionOID);
		}
		// Get object location
		BackendID execLocationID = objectHint;
		if (objectHint == null) {
			execLocationID = getLocation(versionOID);
		}
		// Get language from origin location
		final DataServiceAPI dsAPI = getRemoteExecutionEnvironment(execLocationID);
		dsAPI.consolidateVersion(sessionID, versionOID);
	}

	/**
	 * Replicates an object. If a Backend is provided the object is replicated to
	 * this backend, otherwise it is replicated to
	 * any backend
	 * 
	 * @param objectID
	 *                         ID of the object
	 * @param objectHint       object hint
	 * @param optDestBackendID
	 *                         ID of the backend in which to replicate the object
	 *                         (optional)
	 * @param optDestHostname  Hostname of the backend in which to replicate the
	 *                         object (optional)
	 * @param recursive
	 *                         Indicates if we should also replicate all sub-objects
	 *                         or not.
	 * @return The ID of the backend in which the replica was created or NULL if
	 *         some error is thrown.
	 * 
	 */
	public final BackendID newReplica(final ObjectID objectID, final BackendID objectHint,
			final BackendID optDestBackendID, final String optDestHostname,
			final boolean recursive) {
		// FIXME: retry in different replica location if it fails

		final SessionID sessionID = checkAndGetSession(new String[] {}, new Object[] {});

		Tuple<DataServiceAPI, ExecutionEnvironment> destInfo = this.prepareNewReplicaVersionConsolidate(objectID,
				objectHint,
				optDestBackendID, optDestHostname, true);
		DataServiceAPI dsAPI = destInfo.getFirst();
		ExecutionEnvironment destBackend = destInfo.getSecond();
		Set<ObjectID> replicatedObjects = dsAPI.newReplica(sessionID, objectID, destBackend.getDataClayID(), recursive);
		for (ObjectID replicatedObjectID : replicatedObjects) {
			// update metadata of the object in cache (if present!)
			MetaDataInfo mdInfo = getObjectMetadata(replicatedObjectID);
			if (mdInfo != null) {
				mdInfo.getLocations().add(destBackend.getDataClayID());
			}

			// Add replica location
			DataClayObject dcObj = this.getFromHeap(objectID);
			dcObj.addReplicaLocations(destBackend.getDataClayID());
			if (dcObj.getOriginLocation() == null) {
				// ONLY AT CLIENT SIDE: if it has origin location then keep that origin location
				// at client side there cannot be two replicas of same oid
				dcObj.setOriginLocation((ExecutionEnvironmentID) objectHint);
			}
		}
		return destBackend.getDataClayID();
	}

	/**
	 * Move the replica of an object from one backend to another.
	 * 
	 * @param objectID
	 *                      ID of the object
	 * @param classID
	 *                      Class ID of the object
	 * @param hint
	 *                      Hint of the object
	 * @param srcBackendID
	 *                      ID of the backend containing the replica
	 * @param destBackendID
	 *                      ID of the destination backend
	 * @param recursive
	 *                      Indicates if movement must be recursive or not.
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
		final RegistrationInfo regInfo = new RegistrationInfo(objectID, classID, sessionID, null, null);
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
		List<RegistrationInfo> regInfos = new ArrayList<>();
		regInfos.add(regInfo);
		logicModule.registerObjects(regInfos, (ExecutionEnvironmentID) hint, Langs.LANG_JAVA);

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
	 *                 ID of the object
	 * @param classID
	 *                 Class ID of the object
	 * @param hint
	 *                 Hint of the object
	 */
	public final void setObjectReadOnly(final ObjectID objectID, final MetaClassID classID, final BackendID hint) {
		final SessionID sessionID = checkAndGetSession(new String[] { "ObjectID" }, new Object[] { objectID });
		// TODO: call DS to set object read only
	}

	/**
	 * Set a persistent object as read write
	 * 
	 * @param objectID
	 *                 ID of the object
	 * @param classID
	 *                 Class ID of the object
	 * @param hint
	 *                 Hint of the object
	 */
	public final void setObjectReadWrite(final ObjectID objectID, final MetaClassID classID, final BackendID hint) {
		final SessionID sessionID = checkAndGetSession(new String[] { "ObjectID" }, new Object[] { objectID });
		// TODO: call DS to set object read only
	}

	/**
	 * Get the ID of some backend in which the object identified by the stub
	 * instance provided is located
	 * 
	 * @param objectID
	 *                 ID of the object
	 * @return ID of some backend in which the object is located or NULL if some
	 *         error is thrown.
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
			MetaDataInfo metadata = getObjectMetadata(objectID);
			if (metadata == null) {
				// no metadata available, throw exception
				// NOTE: if it is a volatile and hint failed, it means that object is actually
				// not registered
				throw new ObjectNotRegisteredException(objectID);
			}
			return metadata.getLocations().iterator().next();
		}
	}

	/**
	 * Get the IDs of the backends in which the object identified by the stub
	 * instance provided is located.
	 * 
	 * @param objectID
	 *                 ID of the object
	 * @return IDs of the backends in which the object is located or NULL if some
	 *         error is thrown.
	 */
	public final Set<BackendID> getAllLocations(final ObjectID objectID) {
		checkConnectionAndParams(new String[] { "ObjectID" }, new Object[] { objectID });
		LOGGER.debug("Getting all locations of object " + objectID);
		// First get object locations
		Set<BackendID> locations = new HashSet<>();
		DataClayObject dcObject = this.getFromHeap(objectID);
		if (dcObject != null) {
			Set<ExecutionEnvironmentID> replicaLocs = dcObject.getReplicaLocations();
			if (replicaLocs != null) {
				LOGGER.debug("Found replica locations:" + replicaLocs);
				locations.addAll(replicaLocs);
			}
			if (dcObject.getOriginLocation() != null) {
				LOGGER.debug("Found origin location:" + dcObject.getOriginLocation());
				locations.add(dcObject.getOriginLocation());
			}
		}
		final MetaDataInfo metadata = getObjectMetadata(objectID);
		if (metadata != null) {
			locations.addAll(metadata.getLocations());
			LOGGER.debug("Found metadata in cache, adding: " + metadata.getLocations());
			// Get Hint
			final DataClayObject obj = this.getFromHeap(objectID);
			if (obj != null) {
				final BackendID locationID = obj.getHint();
				if (locationID != null) {
					LOGGER.debug("Adding hint location: " + locationID);
					locations.add(locationID);
				}
			}
			LOGGER.debug("Found locations: " + locations);
			return locations;
		} else {
			// Get Hint
			final DataClayObject obj = this.getFromHeap(objectID);
			if (obj != null) {
				final BackendID locationID = obj.getHint();
				if (locationID != null) {
					LOGGER.debug("Not found metadata in cache, adding hint location: " + locationID);
					locations.add(locationID);
				} else {
					throw new DataClayRuntimeException(ERRORCODE.UNEXPECTED_EXCEPTION,
							"The object " + objectID + " is not initialized well, hint missing or not exist", true);
				}
			}
			LOGGER.debug("Found locations: " + locations);
			return locations;
		}
	}

	/**
	 * Get name of node associated to hint.
	 * 
	 * @param hint
	 *             Hint
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
	 *                            Object in which to run method that needs
	 *                            serialization of parameters
	 * @param ifaceBitMaps
	 *                            Interface bitmaps
	 * @param implID
	 *                            ImplementationID
	 * @param params
	 *                            Parameters or return to serialize
	 * @param forUpdate
	 *                            Indicates whether this serialization is for an
	 *                            update or not
	 * @param hintVolatiles
	 *                            Hint to set to volatiles
	 * @return Serialized parameters
	 */
	public final SerializedParametersOrReturn serializeParams(final DataClayObject objectInWhichToExec,
			final Map<MetaClassID, byte[]> ifaceBitMaps, final ImplementationID implID, final Object[] params,
			final boolean forUpdate, final BackendID hintVolatiles) {
		if (DEBUG_ENABLED) {
			String paramTypes = "";
			for (Object param : params) {
				if (param == null) {
					paramTypes = paramTypes + "null, ";
				} else {
					paramTypes = paramTypes + param.getClass().getName() + ", ";
				}
			}
			LOGGER.debug("[==Serialization==] Serializing parameters: " + paramTypes);

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
	 *                            Object in which to run method that needs
	 *                            serialization of parameters
	 * @param ifaceBitMaps
	 *                            Interface bitmaps
	 * @param implID
	 *                            ImplementationID
	 * @param ret
	 *                            Return to serialize
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
	 *                            Object in which to run method that needs
	 *                            deserialization of parameters.
	 * @param ifaceBitMaps
	 *                            Interface bitmaps
	 * @param implID
	 *                            ImplementationID
	 * @param serializedParams
	 *                            Parameters to deserialize
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
			LOGGER.debug("[==Serialization==] Setting wrapper params");
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
	 *                         Interface bitmaps
	 * @param serializedParams
	 *                         Objects to deserialize
	 * @return the deserialized parameters
	 */
	public final Object[] deserializeIntoHeap(final Map<MetaClassID, byte[]> ifaceBitMaps,
			final List<ObjectWithDataParamOrReturn> serializedParams) {
		if (serializedParams == null) {
			return null;
		}
		return DataClayDeserializationLib.deserializeParamsOrReturn(new SerializedParametersOrReturn(serializedParams),
				ifaceBitMaps, this);
	}

	/**
	 * Deserialize return.
	 * 
	 * @param objectInWhichToExec
	 *                            Object in which to run method that needs
	 *                            deserialization of return.
	 * @param ifaceBitMaps
	 *                            Interface bitmaps
	 * @param implID
	 *                            ImplementationID
	 * @param serializedReturn
	 *                            Return to deserialize
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
	 * Internal Method that executes an implementation depending on client or
	 * server.
	 * 
	 * @param objectInWhichToExec
	 *                            Object in which to exec
	 * @param implID
	 *                            Implementation ID
	 * @param params
	 *                            Parameters
	 * @return the result of execution.
	 * @note This function is called from a stub
	 */
	protected abstract Object executeRemoteImplementationInternal(final DataClayObject objectInWhichToExec,
			final ImplementationID implID, final Object[] params);

	/**
	 * Method that executes an implementation.
	 * 
	 * @param objectInWichToExec
	 *                           Object in which to exec
	 * @param implIDStr
	 *                           Implementation ID as string
	 * @param params
	 *                           Parameters
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
	 *                         Object used as a 'portal' to other DS.
	 * @param params
	 *                         Parameters to send
	 * @param remoteImplID
	 *                         ID of implementation to execute
	 * @param remoteLocationID
	 *                         Location in which to execute
	 * @param usingHint
	 *                         TRUE if using hint.
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
					LOGGER.debug("Execution failed in location " + execLocationID);
					// PREFER NOT TRIED LOCATION (In case Backend failed and we have replicas)

					Set<ExecutionEnvironmentID> locations = dcObject.getReplicaLocations();
					if (locations == null || locations.isEmpty()) {
						final MetaDataInfo metadata = getObjectMetadata(dcObject.getObjectID());
						if (metadata == null) {

							// no metadata available, throw exception
							// NOTE: if it is a volatile and hint failed, it means that object is actually
							// not registered
							throw new ObjectNotRegisteredException(dcObject.getObjectID());
						} else {
							locations = metadata.getLocations();
						}
					}

					boolean foundDifferentLocation = false;
					for (final ExecutionEnvironmentID curLoc : locations) {
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
						execLocationID = locations.iterator().next();
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
					// if there was a miss, it means that the persistent object in which we were
					// executing
					// was not in the choosen location. As you can see in the serialize parameters
					// function above
					// we provide the execution environment as hint to set to volatile parameters.
					// In EE, before
					// deserialization of volatiles we check if the persistent object in which to
					// execute a method is
					// there, if not, EE raises and exception. Therefore, if there was a miss, we
					// know that the
					// hint we set in volatile parameters is wrong, because they are going to be
					// deserialized/stored
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
	 * Synchronize
	 * 
	 * @param dcObject
	 *                     Object used as a 'portal' to other DS.
	 * @param params
	 *                     Parameters to send
	 * @param remoteImplID
	 *                     ID of implementation to execute
	 */
	public abstract void synchronize(final DataClayObject dcObject, final Object[] params,
			final ImplementationID remoteImplID);

	/**
	 * Check if string is UUID
	 * 
	 * @param string
	 *               string to check
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
	 *               string to check
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
	 *                                  ID of the object
	 * @param className
	 *                                  Name of the class of the object
	 * @param operationNameAndSignature
	 *                                  Name and Signature of the operation to be
	 *                                  executed
	 * @param params
	 *                                  parameters for the operation
	 * @param target
	 *                                  the backend where the execution must be
	 *                                  performed
	 * @return the resulting object corresponding to the execution of the operation
	 *         if it succeeds. null otherwise. <br>
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
	 * Check if instance exists in Heap or create a new PERSISTENT instance if
	 * needed
	 * 
	 * @param classID
	 *                 ID of the class in case it is needed (not need to query) if
	 *                 null, look for class id in metadata.
	 * @param objectID
	 *                 ID of object
	 * @param hint
	 *                 Can be null. Hint in case object is a volatile in another DS
	 *                 and we need information.
	 * @return Instance
	 */
	public abstract DataClayObject getOrNewPersistentInstance(final MetaClassID classID, final ObjectID objectID,
			final BackendID hint);

	/**
	 * Create a new instance in a remote server and persist it.
	 * 
	 * @param classID
	 *                 ID of the class of the instance to create
	 * @param stubInfo
	 *                 Stub information
	 * @param implID
	 *                 ID of the implementation of the constructor
	 * @param params
	 *                 Parameters to send to constructor
	 * @param locID
	 *                 (optional) Storage Location/ Execution Environment in which
	 *                 to store object.
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
			execLocationID = this.getAllExecutionEnvironmentsInfo(Langs.LANG_JAVA, false).keySet().iterator().next();
		}

		// Serialize parameters
		final SerializedParametersOrReturn serializedParams = serializeParams(portal, null, implID, params, false,
				execLocationID);

		final DataServiceAPI dsAPI = this.getRemoteExecutionEnvironment(execLocationID);
		return dsAPI.newPersistentInstance(sessionID, classID, implID, null, serializedParams);

	}

	/**
	 * This method creates a new Persistent Object using the provided stub instance
	 * and, if indicated, all its associated
	 * objects also Logic module API used for communication
	 * 
	 * @param dcObject
	 *                              Instance to make persistent
	 * @param optionalDestBackendID
	 *                              Indicates which is the destination backend
	 * @param recursive
	 *                              Indicates if make persistent is recursive
	 * @param alias
	 *                              Alias for the object
	 * @return ID of the backend in which te object was persisted.
	 * @note This function is called from a stub/execution class
	 */
	public abstract BackendID makePersistent(final DataClayObject dcObject, final BackendID optionalDestBackendID,
			final boolean recursive, final String alias);

	/**
	 * Federate an object with an external dataClay
	 *
	 * @param dcObject
	 *                           object to federate
	 * @param externalDataClayID
	 *                           id of the external dataClay ID
	 * @param recursive
	 *                           Indicates if subobjects should be federated as well
	 */
	public void federateObject(final DataClayObject dcObject,
			final DataClayInstanceID externalDataClayID,
			final boolean recursive) {

		ExecutionEnvironmentID externalExecutionEnvironmentID = this
				.getAllExecutionEnvironmentsAtDataClay(Langs.LANG_JAVA, externalDataClayID, false).keySet().iterator()
				.next();
		this.federateToBackend(dcObject, externalExecutionEnvironmentID, recursive);
	}

	/**
	 * Federate an object with an external dataClay
	 *
	 * @param dcObject
	 *                                       object to federate
	 * @param externalExecutionEnvironmentID
	 *                                       id of the external execution
	 *                                       environment id
	 * @param recursive
	 *                                       Indicates if subobjects should be
	 *                                       federated as well
	 */
	public abstract void federateToBackend(final DataClayObject dcObject,
			final ExecutionEnvironmentID externalExecutionEnvironmentID,
			final boolean recursive);

	/**
	 * Unfederate an object with an external dataClay
	 *
	 * @param dcObject
	 *                           object to unfederate
	 * @param externalDataClayID
	 *                           id of the external dataClay
	 * @param recursive
	 *                           Indicates if subobjects should be unfederated as
	 *                           well
	 */
	public void unfederateObject(final DataClayObject dcObject,
			final DataClayInstanceID externalDataClayID,
			final boolean recursive) {
		this.unfederateFromBackend(dcObject, null, recursive);
	}

	/**
	 * Unfederate an object with an external backend
	 *
	 * @param dcObject
	 *                                       object to unfederate
	 * @param externalExecutionEnvironmentID
	 *                                       id of the external execution
	 *                                       environment id
	 * @param recursive
	 *                                       Indicates if subobjects should be
	 *                                       unfederated as well
	 */
	public abstract void unfederateFromBackend(final DataClayObject dcObject,
			final ExecutionEnvironmentID externalExecutionEnvironmentID,
			final boolean recursive);

	/**
	 * Unfederate an object with all external dataClays
	 * 
	 * @param dcObject
	 *                  object to unfederate
	 * @param recursive
	 *                  Indicates if subobjects should be federated as well
	 */
	public void unfederateObjectWithAllDCs(final DataClayObject dcObject, final boolean recursive) {
		throw new UnsupportedOperationException();

	}

	/**
	 * Unfederate all objects belonging/federated with external dataClay with id
	 * provided
	 * 
	 * @param extDataClayID External dataClay ID
	 */
	public void unfederateAllObjects(final DataClayInstanceID extDataClayID) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Unfederate all objects belonging/federated with ANY external dataClay
	 */
	public void unfederateAllObjectsWithAllDCs() {
		throw new UnsupportedOperationException();

	}

	/**
	 * Federate all dataClay objects from specified current dataClay
	 * destination dataclay.
	 * 
	 * @param destinationDataClayID Destination dataclay id
	 */
	public void federateAllObjects(
			final DataClayInstanceID destinationDataClayID) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Import classes in namespace specified from an external dataClay
	 * 
	 * @param externalNamespace External namespace to get
	 * @param extDataClayID     External dataClay ID
	 */
	public void importModelsFromExternalDataClay(final String externalNamespace,
			final DataClayInstanceID extDataClayID) {
		logicModule.importModelsFromExternalDataClay(externalNamespace, extDataClayID);

	}

	/**
	 * Migrate (unfederate and federate) all current dataClay objects from specified
	 * external dataclay di to
	 * destination dataclay.
	 * 
	 * @param originDataClayID      Origin dataclay id
	 * @param destinationDataClayID Destination dataclay id
	 */
	public void migrateFederatedObjects(final DataClayInstanceID originDataClayID,
			final DataClayInstanceID destinationDataClayID) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Set DataSet ID
	 * 
	 * @param objectID
	 *                     ID of the object
	 * @param newDataSetID
	 *                     New DataSetID
	 */
	public final void setDataSetID(final ObjectID objectID, final DataSetID newDataSetID) {
		final SessionID sessionID = checkAndGetSession(new String[] { "newDataSetID" }, new Object[] { newDataSetID });
		this.logicModule.setDataSetID(sessionID, objectID, newDataSetID);

	}

	/**
	 * Recovers Object from OID and class ID
	 * 
	 * @param objectID
	 *                 ObjectID of the object
	 * @param classID
	 *                 ClassID of the object. Can be NULL. Null class ID makes
	 *                 dataClay to ask for object Mdata.
	 * @param hint
	 *                 Hint of the object. Can be NULL if no hint.
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
	 * Remove reference from Heap. Even if we remove it from the heap, the object
	 * won't be Garbage collected by JavaGC till
	 * HeapManager flushes the object and releases it.
	 * 
	 * @param objectID
	 *                 ID of the object
	 */
	public final void removeFromHeap(final ObjectID objectID) {
		this.dataClayHeapManager.removeFromHeap(objectID);
	}

	/**
	 * Add to Heap
	 * 
	 * @param dcObject
	 *                 the object
	 */
	public final void addToHeap(final DataClayObject dcObject) {
		this.dataClayHeapManager.addToHeap(dcObject);
	}

	/**
	 * Check if object exists in dataClay.
	 * 
	 * @param objectID
	 *                 ID of the object
	 * @return TRUE if object exists. FALSE otherwise.
	 */
	public boolean objectExistsInDataClay(final ObjectID objectID) {
		LOGGER.debug("Checking object {} exists", objectID);
		return this.logicModule.objectExistsInDataClay(objectID);
	}

	/**
	 * Get number of objects in dataClay
	 */
	public int getNumObjects() {
		LOGGER.debug("Getting number of objects in dataClay");
		return this.logicModule.getNumObjects();
	}

	/**
	 * Get from Heap
	 * 
	 * @param objectID
	 *                 ID of the object
	 * @return The object in Heap or null if not present
	 */
	public DataClayObject getFromHeap(final ObjectID objectID) {
		return this.dataClayHeapManager.getObject(objectID);
	}

	/**
	 * Check if there is an object with ID provided.
	 * 
	 * @param objectID
	 *                 ID of the object.
	 * @return TRUE if exists in memory. FALSE otherwise.
	 */
	public final boolean existsInHeap(final ObjectID objectID) {
		return dataClayHeapManager.existsObject(objectID);
	}

	/**
	 * Detach object from current session in use, i.e. remove reference from current
	 * session provided to object,
	 * "dear garbage-collector, current session is not using the object anymore"
	 *
	 * @param objectID ID of the object to detach
	 * @param hint     Hint of the object to detach (can be null)
	 */
	public abstract void detachObjectFromSession(final ObjectID objectID, final ExecutionEnvironmentID hint);

	/**
	 * ADVANCED FUNCTION. Try not to use it. This function flushes all objects in
	 * Heap.
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
	 * Get number of loaded objects in heap.
	 *
	 * @return number of loaded objects in heap.
	 */
	public final int numLoadedObjs() {
		return this.dataClayHeapManager.numLoadedObjs();
	}

	/**
	 * Activate tracing in dataClay services
	 * 
	 */
	public final void activateTracingInDataClayServices() {
		if (DataClayExtrae.extraeTracingIsEnabled()) { // sanity check, only activate if extrae was properly initialized
			this.logicModule.activateTracing(DataClayExtrae.getCurrentAvailableTaskID());
		}
	}

	/**
	 * Dectivate tracing
	 */
	public final void deactivateTracingInDataClayServices() {
		if (DataClayExtrae.extraeTracingIsEnabled()) { // sanity check, only activate if extrae was properly initialized
			this.logicModule.deactivateTracing();
		}
	}

	/**
	 * Activate tracing
	 */
	public final void activateTracing(
			final boolean initializeWrapper) {
		DataClayExtrae.initializeExtrae(initializeWrapper);
	}

	/**
	 * Deactivate tracing
	 */
	public final void deactivateTracing(final boolean finalizeWrapper) {
		DataClayExtrae.finishTracing(finalizeWrapper);
	}

	/**
	 * Get traces in dataClay services and store it in current workspace
	 */
	public final void getTracesInDataClayServices() {

		final Map<String, byte[]> traces = logicModule.getTraces();

		/*
		 * final Map<String, Map<String, byte[]>> filesPerHost = new HashMap<>();
		 * 
		 * for (final Entry<String, byte[]> traceFile : traces.entrySet()) {
		 * 
		 * }
		 */

		int curTask = 0;
		final String setPath = "set-0";
		final String traceMpitsPath = "TRACE.mpits";
		try {

			FileUtils.forceMkdir(new File(setPath));
			File traceMpitsFile = new File(traceMpitsPath);

			for (final Entry<String, byte[]> traceFile : traces.entrySet()) {

				final String fileName = traceFile.getKey();
				final byte[] fileBytes = traceFile.getValue();

				File tmpTraceFile = new File(setPath + File.separator + fileName);
				final String path = tmpTraceFile.getAbsolutePath();
				LOGGER.info("Storing file " + path);

				// Store Extrae temporary files
				FileUtils.writeByteArrayToFile(tmpTraceFile, fileBytes);

				if (fileName.endsWith(".mpit")) {
					final String newFilePointer = path + " named\n";
					LOGGER.info("Adding line to " + traceMpitsFile.getAbsolutePath() + " file: " + newFilePointer);
					FileUtils.writeStringToFile(traceMpitsFile, newFilePointer, Charset.defaultCharset(), true);
				}
				curTask++;
			}

			// cat trace.mpits
			Scanner input = new Scanner(traceMpitsFile);

			while (input.hasNextLine()) {
				LOGGER.info(input.nextLine());
			}
			input.close();

		} catch (final Exception e) {
			e.printStackTrace();
		}

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
	 *                 ID of object
	 */
	public final void lock(final ObjectID objectID) {
		this.lockerPool.lock(objectID);
	}

	/**
	 * Unlock object
	 * 
	 * @param objectID
	 *                 ID of object
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
	 *                 ID of the object
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
	 *                       Value to set
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
	 *                 DataClay object.
	 * @return Chosen location.
	 */
	protected BackendID chooseLocation(final DataClayObject dcObject, final String alias) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==Execution==] Using Hash execution location for " + dcObject.getObjectID());
		}

		final BackendID location;

		if (alias != null) {
			location = getBackendIDFromAlias(alias);
		} else {
			location = getBackendIDFromObjectID(dcObject.getObjectID());
		}

		dcObject.setHint(location);
		return location;
	}

	/**
	 * Update the object id in both DataClayObject and HeapManager
	 *
	 * @param dcObject
	 *                    DataClay object.
	 * @param newObjectID
	 *                    the new object id.
	 */
	protected void updateObjectID(DataClayObject dcObject, ObjectID newObjectID) {
		final ObjectID oldObjectID = dcObject.getObjectID();
		dcObject.setObjectIDUnsafe(newObjectID);
		dataClayHeapManager.removeFromHeap(oldObjectID);
		dataClayHeapManager.addToHeap(dcObject);
		;
	}
}
