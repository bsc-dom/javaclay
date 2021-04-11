package es.bsc.dataclay.commonruntime;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import es.bsc.dataclay.dataservice.api.DataServiceAPI;
import es.bsc.dataclay.serialization.lib.SerializedParametersOrReturn;
import es.bsc.dataclay.util.management.metadataservice.ExecutionEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.DataClayExecutionObject;
import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.api.BackendID;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.dataservice.DataService;
import es.bsc.dataclay.exceptions.dataservice.ExecuteMethodException;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectNotExistException;
import es.bsc.dataclay.heap.ExecutionEnvironmentHeapManager;
import es.bsc.dataclay.heap.HeapManager;
import es.bsc.dataclay.loader.DataClayObjectLoader;
import es.bsc.dataclay.loader.ExecutionObjectLoader;
import es.bsc.dataclay.serialization.lib.ObjectWithDataParamOrReturn;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.classloaders.DataClayClassLoaderSrv;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.SessionID;
import es.bsc.dataclay.util.management.metadataservice.RegistrationInfo;
import es.bsc.dataclay.util.reflection.Reflector;
import es.bsc.dataclay.util.structs.LruCache;
import es.bsc.dataclay.util.structs.LruCacheByDate;
import es.bsc.dataclay.util.structs.Tuple;

/**
 * This class contains functions for node to interact with other nodes.
 */
public final class DataServiceRuntime extends DataClayRuntime {
	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger("DataServiceRuntime");

	/** Ref to DS if this lib serves to a DS. */
	public DataService dsRef;

	/** Information of session per thread. */
	private final ThreadLocal<SessionID> dsSessionIDs = new ThreadLocal<>();

	/** Cache of sessions. */
	private final LruCacheByDate<SessionID, Tuple<Tuple<DataSetID, Set<DataSetID>>, Calendar>> sessionsCache;

	/** Cache of datasets. */
	private final LruCache<DataSetID, Boolean> datasetsCache;

	/**
	 * References hold by sessions. Resource note: Maximum size of this map is maximum number of objects allowed in EE x
	 * sessions. TODO: think about compressing it.
	 */
	private final Map<ObjectID, Set<SessionID>> referencesHoldBySessions = new ConcurrentHashMap<>();

	/**
	 * Per each session, it's expiration date. This is used to control 'retained' objects from sessions in Garbage collection.
	 */
	private final Map<SessionID, Date> sessionsExpireDates = new ConcurrentHashMap<>();

	/**
	 * Sessions in quarantine. note: maximum size of this map is max number of sessions per EE: This map is needed to solve a
	 * race condition in Global Garbage collection (@see getReferenceCounting).
	 */
	private final Set<SessionID> quarantineSessions = ConcurrentHashMap.newKeySet();

	/**
	 * Indicates if tasks (garbage collectors,...) were scheduled or not. This is done to manage. multiple initializations.
	 */
	public boolean tasksScheduled = false;

	/**
	 * Constructor.
	 * 
	 * @param newdsRef
	 *            DS reference
	 */
	public DataServiceRuntime(final DataService newdsRef) {
		this.dsRef = newdsRef;

		this.dataClayObjLoader = new ExecutionObjectLoader(this);
		this.dataClayHeapManager = new ExecutionEnvironmentHeapManager(this);
		this.sessionsCache = new LruCacheByDate<>(Configuration.Flags.MAX_ENTRIES_DATASERVICE_CACHE.getIntValue());
		this.datasetsCache = new LruCache<>(Configuration.Flags.MAX_ENTRIES_DATASERVICE_CACHE.getIntValue());

		this.threadPool = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
			@Override
			public Thread newThread(final Runnable r) {
				final Thread t = Executors.defaultThreadFactory().newThread(r);
				t.setDaemon(true);
				t.setName(getDataService().dsName + "-MemoryGC");
				return t;
			}
		});
	}

	/**
	 * Initialize connections.
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
	@Override
	public void initialize(final String logicModuleHost, final int logicModulePort, final String originHostName)
			throws Exception {
		super.initialize(logicModuleHost, logicModulePort, originHostName);
		if (Configuration.Flags.MEMORY_GC_ENABLED.getBooleanValue() && !tasksScheduled) {
			// Create Repetitively task
			this.threadPool.scheduleAtFixedRate(dataClayHeapManager, 0,
					Configuration.Flags.MEMMGMT_CHECK_TIME_INTERVAL.getLongValue(), TimeUnit.MILLISECONDS);
			tasksScheduled = true;
		}
	}
	
	@Override
	public DataClayExecutionObject getOrNewPersistentInstance(final MetaClassID classID, final ObjectID objectID,
			final BackendID hint) {
		return (DataClayExecutionObject) this.dataClayObjLoader.getOrNewPersistentInstance(classID, objectID, hint);
	}

	/**
	 * Get object from memory or database and WAIT in case we are still waiting for it to be persisted.
	 * 
	 * @param objectID
	 *            ID of the object to get
	 * @param retry
	 *            Indicates if we should retry and wait.
	 * @return The the object.
	 */
	public DataClayExecutionObject getOrNewInstanceFromDB(final ObjectID objectID, final boolean retry) {
		return ((ExecutionObjectLoader) dataClayObjLoader).getOrNewPersistentInstanceFromDB(objectID, retry);
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
	@Override
	public DataClayObject getOrNewAndLoadVolatile(final MetaClassID classID, final ObjectID objectID,
			final BackendID hint, final ObjectWithDataParamOrReturn objWithData,
			final Map<MetaClassID, byte[]> ifaceBitMaps) {
		return dataClayObjLoader.getOrNewAndLoadVolatile(classID, objectID, hint, objWithData, ifaceBitMaps);
	}

	/**
	 * Deserialize data into instance.
	 * 
	 * @param instance
	 *            Instance to be filled.
	 * @param data
	 *            Data
	 * @param ifaceBitMaps
	 *            Interface bitmaps
	 */
	public void deserializeDataIntoInstance(final DataClayExecutionObject instance,
			final ObjectWithDataParamOrReturn data, final Map<MetaClassID, byte[]> ifaceBitMaps) {
		dataClayObjLoader.deserializeDataIntoInstance(instance, data, ifaceBitMaps);
	}

	@Override
	public SessionID getSessionID() {
		return dsSessionIDs.get();
	}

	/**
	 * Checks session is valid.
	 * 
	 * @param dataSetID
	 *            dataSetID to be accessed
	 * @param sessionID
	 *            id of the session
	 */
	public void checkSession(final DataSetID dataSetID, final SessionID sessionID) {
		Boolean isPublic = datasetsCache.get(dataSetID);
		// TODO: Redudant check if at newSession time is already checking this (July
		// 2018 jmarti)
		if (isPublic == null) {
			isPublic = getLogicModuleAPI().checkDataSetIsPublic(dataSetID);
			datasetsCache.put(dataSetID, isPublic);
		}
		if (isPublic) {
			return;
		}
		final Tuple<Tuple<DataSetID, Set<DataSetID>>, Calendar> sessionInfo = getSessionInfo(sessionID);
		final Set<DataSetID> accessibleDatasets = sessionInfo.getFirst().getSecond();
		if (!accessibleDatasets.contains(dataSetID)) {
			throw new ExecuteMethodException("Inaccessible dataset " + dataSetID);
		}
	}

	/**
	 * Get session info
	 * 
	 * @param sessionID
	 *            ID of session
	 * @return Session info.
	 */
	private Tuple<Tuple<DataSetID, Set<DataSetID>>, Calendar> getSessionInfo(final SessionID sessionID) {
		Tuple<Tuple<DataSetID, Set<DataSetID>>, Calendar> sessionInfo = sessionsCache.get(sessionID);
		if (sessionInfo == null) {
			sessionInfo = getLogicModuleAPI().getInfoOfSessionForDS(sessionID);
			sessionsCache.put(sessionID, sessionInfo);
		}
		return sessionInfo;
	}

	/**
	 * Set instance to be weak proxy.
	 * 
	 * @param instance
	 *            Instance to modify.
	 * @param newHint
	 *            Hint to set into instance.
	 */
	public void setWeakProxy(final DataClayExecutionObject instance, final ExecutionEnvironmentID newHint) {
		lock(instance.getObjectID());
		try {
			instance.setLoaded(false);
			if (DEBUG_ENABLED) {
				LOGGER.debug("[==Hint==] Setting hint (setWeakProxy) on instance " + instance.getObjectID()
						+ " the hint : " + getDSNameOfHint(newHint));
			}
			instance.setHint(newHint);
			Reflector.nullifyAllFields(instance);
		} finally {
			unlock(instance.getObjectID());
		}
	}

	/**
	 * All volatiles provided are under deserialization. This function solves problems of 'hashcode' and other special functions
	 * needed during deserializations. See executeImpl.
	 * 
	 * @param volatileMap
	 *            Volatiles under deserialization.
	 */
	@Override
	public void addVolatileUnderDeserialization(final Map<Integer, ObjectWithDataParamOrReturn> volatileMap) {
		underDeserializationVolatiles.put(Thread.currentThread().getId(), volatileMap);
	}

	/**
	 * Remove volatiles under deserialization.
	 */
	@Override
	public void removeVolatilesUnderDeserialization() {
		underDeserializationVolatiles.remove(Thread.currentThread().getId());
	}

	/**
	 * Check if there is a volatile object with ID provided and if so, deserialize it since it is needed.
	 * 
	 * @param volatileObj
	 *            Object to check.
	 * @param ifaceBitMaps
	 *            Interface bitmaps for deserialization.
	 * @return TRUE if it was filled and volatile, FALSE otherwise.
	 */
	private boolean checkAndFillVolatileUnderDeserialization(final DataClayExecutionObject volatileObj,
			final Map<MetaClassID, byte[]> ifaceBitMaps) {
		final ObjectID objectID = volatileObj.getObjectID();
		final Map<Integer, ObjectWithDataParamOrReturn> volatileMap = underDeserializationVolatiles
				.get(Thread.currentThread().getId());
		if (volatileMap != null) {
			for (final ObjectWithDataParamOrReturn volatileParamOrRet : volatileMap.values()) {
				if (volatileParamOrRet.getObjectID().equals(objectID)) {
					// Deserialize it
					getOrNewAndLoadVolatile(volatileObj.getMetaClassID(), objectID, volatileObj.getHint(),
							volatileParamOrRet, ifaceBitMaps);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected Object executeRemoteImplementationInternal(final DataClayObject object, final ImplementationID implID,
			final Object[] params) {

		if (DEBUG_ENABLED) {
			LOGGER.debug("[==Execution==] Object " + object.getObjectID()
					+ " was unloaded. Checking reason. System.id = " + System.identityHashCode(object));
		}

		final DataClayExecutionObject objectInWhichToExec = (DataClayExecutionObject) object;

		// ============================== PARAMS/RETURNS ========================== //
		// Check if object is being deserialized (params/returns)
		boolean volatileUnderDeserialization = false;
		volatileUnderDeserialization = checkAndFillVolatileUnderDeserialization(objectInWhichToExec, null);

		if (volatileUnderDeserialization) {
			if (DEBUG_ENABLED) {
				LOGGER.debug("[==Execution==] Object " + objectInWhichToExec.getObjectID()
						+ " is a volatile under deserialization, executing in " + "System.id = "
						+ System.identityHashCode(objectInWhichToExec));
			}
			return this.dsRef.runImplementation(objectInWhichToExec, implID, params);
		}

		ExecutionEnvironmentID remoteLocID = null;
		boolean usingHint = false;
		boolean inThisLocation = false;
		final ObjectID objectID = objectInWhichToExec.getObjectID();

		if (objectInWhichToExec.getHint() != null) {
			// ==================================== HINT ===================================
			// //
			final ExecutionEnvironmentID hint = (ExecutionEnvironmentID) objectInWhichToExec.getHint();
			inThisLocation = hint.equals(this.dsRef.getExecutionEnvironmentID());
			remoteLocID = hint;
			usingHint = true;
			if (DEBUG_ENABLED) {
				if (inThisLocation) {
					LOGGER.debug("[==Execution==] Object " + objectInWhichToExec.getObjectID()
							+ " has a Hint, pointing to this node. System.id = "
							+ System.identityHashCode(objectInWhichToExec));
				} else {
					LOGGER.debug("[==Execution==] Object " + objectInWhichToExec.getObjectID()
							+ " has a Hint, pointing to another node " + "(" + remoteLocID + "). System.id = "
							+ System.identityHashCode(objectInWhichToExec));
				}
			}
		} else {
			// Get
			remoteLocID = (ExecutionEnvironmentID) getLocation(objectID);
			// remoteLocID = getExecutionEnvironmentLocation(objectID);
			inThisLocation = remoteLocID.equals(this.dsRef.getExecutionEnvironmentID());
			if (DEBUG_ENABLED) {
				LOGGER.debug("[==Execution==] Object " + objectInWhichToExec.getObjectID()
						+ " is not a proxy and has no hint. Obtained actual location = " + remoteLocID + " System.id = "
						+ System.identityHashCode(objectInWhichToExec));
			}
		}

		// ==================================== THIS LOCATION
		// =================================== //
		if (inThisLocation) {
			if (DEBUG_ENABLED) {
				LOGGER.debug("[==Execution==] Object " + objectInWhichToExec.getObjectID()
						+ " is in this node. Loading it from Db. System.id = "
						+ System.identityHashCode(objectInWhichToExec));
			}
			try {
				((ExecutionObjectLoader) this.dataClayObjLoader).loadDataClayObjectFromDb(objectInWhichToExec, true);
				return this.dsRef.runImplementation(objectInWhichToExec, implID, params);
			} catch (final DbObjectNotExistException dbe) {
				if (DEBUG_ENABLED) {
					LOGGER.debug("[==Execution==] Object " + objectInWhichToExec.getObjectID()
							+ " is NOT in this node. Maybe hint or metadata information was wrong due to a movement. "
							+ " Obtaining exec.location again. " + " System.id = "
							+ System.identityHashCode(objectInWhichToExec));
				}
				// not actually here (maybe hint was wrong)
				// remoteLocID = getExecutionEnvironmentLocation(objectID);
				remoteLocID = (ExecutionEnvironmentID) getLocation(objectID);
			}

		}

		if (DEBUG_ENABLED) {
			final String methodSignature = objectInWhichToExec.getStubInfo().getImplementationByID(implID.toString())
					.getSignature();
			LOGGER.debug("[==JUMP==] Object " + objectInWhichToExec.getObjectID() + " of class "
					+ objectInWhichToExec.getClass().getName()
					+ " is NOT in this node. Going to do a remote call. Method to call: " + methodSignature
					+ ". System.id = " + System.identityHashCode(objectInWhichToExec));
		}
		// ==================================== REMOTE LOCATION
		// =================================== //
		return callExecuteToDS(objectInWhichToExec, params, implID, remoteLocID, usingHint);

	}

	@Override
	public BackendID makePersistent(final DataClayObject dcObject, final BackendID optionalDestBackendID,
			final boolean recursive, final String alias) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==MakePersistent==] Starting make persistent of object " + dcObject.getObjectID());
		}
		final SessionID sessionID = checkAndGetSession(new String[] {}, new Object[] {});
		final DataClayExecutionObject execObject = (DataClayExecutionObject) dcObject;

		BackendID location = execObject.getHint();
		if (location == null) {
			location = optionalDestBackendID;
			if (location == null) {
				location = chooseLocation(dcObject, alias);
			}
		}

		if (alias != null && execObject.isPendingToRegister()) {

			final RegistrationInfo regInfo = new RegistrationInfo(execObject.getObjectID(),
					execObject.getMetaClassID(), sessionID, execObject.getDataSetID(), alias);
			List<RegistrationInfo> regInfos = new ArrayList<>();
			regInfos.add(regInfo);
			// Location of object is 'this' EE.
			// TODO: Review if we use hint of the object or the hint of the runtime.
			final List<ObjectID> newID = logicModule.registerObjects(regInfos, (ExecutionEnvironmentID) dcObject.getHint(), Langs.LANG_JAVA);
			this.updateObjectID(dcObject, newID.get(0));
			execObject.setPendingToRegister(false);
			execObject.setAlias(alias);
		}

		return location;
	}

	@Override
	public final void synchronize(final DataClayObject dcObject, final Object[] params,
								  final ImplementationID remoteImplID) {
		final SessionID sessionID = checkAndGetSession(new String[] { }, new Object[] { });

		// ===== SERIALIZE PARAMETERS ===== //
		// Serialize parameters
		final SerializedParametersOrReturn serializedParams = serializeParams(dcObject, null, remoteImplID, params,
				false, null);
		ObjectID objectID = dcObject.getObjectID();
		this.dsRef.synchronize(sessionID, objectID, remoteImplID, serializedParams, null);
	}

	@Override
	public String getClassNameInternal(final MetaClassID classID) {
		return DataClayClassLoaderSrv.getClass(classID).getName();
	}

	/**
	 * Set session ID for thread
	 *
	 * @param sessionID
	 *            ID of session
	 */
	public void setCurrentThreadSessionID(final SessionID sessionID) {
		if (sessionID != null) {
			dsSessionIDs.set(sessionID);
		}
	}

	/**
	 * Remove session ID for thread
	 */
	public void removeCurrentThreadSessionID() {
		dsSessionIDs.remove();
	}

	// /**
	// * Get ID of storage of this node.
	// *
	// * @return storage location id
	// */
	// public StorageLocationID getStorageLocationIDOfDS() {
	// return dsRef.getStorageLocationID();
	// }

	/**
	 * Get ID of execution environment of this node.
	 * 
	 * @return execution environment id
	 */
	public ExecutionEnvironmentID getExecutionEnvironmentIDOfDS() {
		return dsRef.getExecutionEnvironmentID();
	}

	/**
	 * Get remote execution environment
	 *
	 * @param execLocationID
	 *            ID of remote execution environment
	 * @return Remote execution environment
	 */
	public final DataServiceAPI getRemoteDSAPI(final ExecutionEnvironmentID execLocationID) {
		if (execLocationID.equals(this.dsRef.getExecutionEnvironmentID())) {
			return dsRef;
		}
		return super.getRemoteDSAPI(execLocationID);
	}

	/**
	 * Directly store objects in DS.
	 * 
	 * @param sessionID
	 *            ID of session
	 * @param objsToStore
	 *            serialized objects to store.
	 */
	public void storeObjects(final SessionID sessionID, final List<ObjectWithDataParamOrReturn> objsToStore) {

		if (DEBUG_ENABLED) {
			for (final ObjectWithDataParamOrReturn obj : objsToStore) {
				LOGGER.debug("#Sending from client lib: " + obj.getObjectID());
			}
		}
		dsRef.storeObjects(sessionID, objsToStore, false, null);
	}

	/**
	 * Get Heap Manager
	 * 
	 * @return Heap Manager
	 */
	@Override
	public HeapManager getDataClayHeapManager() {
		return this.dataClayHeapManager;
	}

	/**
	 * Add a new Hard reference to the object provided. All code in stubs/exec classes using objects in dataClayheap are using
	 * weak references. In order to avoid objects to be GC without a flush in DB, HeapManager has hard-references to them and is
	 * the only one able to release them. This function creates the hard-reference.
	 * 
	 * @param object
	 *            Object to add
	 */
	public void retainInHeap(final DataClayObject object) {
		((ExecutionEnvironmentHeapManager) this.dataClayHeapManager).retainInHeap(object);
	}

	/**
	 * Release hard reference to object with ID provided. Without hard reference, the object can be Garbage collected by Java
	 * GC.
	 * 
	 * @param objectID
	 *            ID of the object
	 */
	public void releaseFromHeap(final ObjectID objectID) {
		((ExecutionEnvironmentHeapManager) this.dataClayHeapManager).releaseFromHeap(objectID);
	}


	@Override
	public void detachObjectFromSession(final ObjectID objectID, final ExecutionEnvironmentID hint) {
		final SessionID sessionID = getSessionID();
		Set<SessionID> objectSessions = this.referencesHoldBySessions.get(objectID);
		if (objectSessions != null) {
			objectSessions.remove(sessionID);
			LOGGER.debug("Detached object {} from session {}", objectID, sessionID);
		}
	}

	@Override
	public void deleteAlias(final DataClayObject dcObject) {
		LOGGER.debug("Removed alias from object " + dcObject.getObjectID());
		String alias = dcObject.getAlias();
		if (alias != null) {
			this.deleteAlias(alias);
		}
		dcObject.setAlias(null);
		((DataClayExecutionObject) dcObject).setDirty(true);

	}

	@Override
	public void federateToBackend(final DataClayObject dcObject,
								  final ExecutionEnvironmentID externalExecutionEnvironmentID,
								  final boolean recursive) {
		ObjectID objectID = dcObject.getObjectID();
		ExecutionEnvironmentID objectHint = (ExecutionEnvironmentID) dcObject.getHint();
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==FederateObject==] Starting federation of object " + objectID + " with ext.EE "
					+ externalExecutionEnvironmentID);
		}
		final SessionID sessionID = this.getSessionID();
		this.dsRef.federate(sessionID, objectID, externalExecutionEnvironmentID, recursive);
	}

	@Override
	public void unfederateFromBackend(final DataClayObject dcObject,
									  final ExecutionEnvironmentID externalExecutionEnvironmentID,
									  final boolean recursive) {
		ObjectID objectID = dcObject.getObjectID();
		ExecutionEnvironmentID objectHint = (ExecutionEnvironmentID) dcObject.getHint();
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==UnfederateObject==] Starting unfederation of object " + objectID + " with ext.EE "
					+ externalExecutionEnvironmentID);
		}
		final SessionID sessionID = this.getSessionID();
		this.dsRef.unfederate(sessionID, objectID, externalExecutionEnvironmentID, recursive);

	}

	@Override
	public boolean isDSLib() {
		return true;
	}

	@Override
	public ExecutionEnvironmentID getLocalBackend() {
		return null;
	}

	/**
	 * Get DataService reference
	 * 
	 * @return DataService reference
	 */
	public DataService getDataService() {
		return dsRef;
	}

	@Override
	public ExecutionEnvironmentID getHint() {
		return dsRef.getExecutionEnvironmentID();
	}

	@Override
	protected Map<MetaClassID, byte[]> getIfaceBitMaps() {
		return null;
	}


	/**
	 * Add +1 reference associated to thread session
	 * 
	 * @param objectID
	 *            ID of the object
	 */
	@Override
	public void addSessionReference(final ObjectID objectID) {
		if (Configuration.Flags.GLOBAL_GC_ENABLED.getBooleanValue()) {
			final SessionID sessionID = this.getSessionID();
			if (sessionID == null) {
				return; // session ID can be null in case of whenFederated
			}
			if (DEBUG_ENABLED) {
				LOGGER.debug("Object " + objectID + " is now being used by session " + sessionID);
			}

			Set<SessionID> referencesInSession = this.referencesHoldBySessions.get(objectID);
			if (referencesInSession == null) {
				// race condition: two objects in same session creates set of object ids
				this.lock(objectID);
				try {
					referencesInSession = this.referencesHoldBySessions.get(objectID);
					if (referencesInSession == null) {
						referencesInSession = ConcurrentHashMap.newKeySet();
						this.referencesHoldBySessions.put(objectID, referencesInSession);
					}
				} finally {
					this.unlock(objectID);
				}
			}
			referencesInSession.add(sessionID);

			// add expiration date of session if not present
			// IMPORTANT: if CHECK_SESSION=FALSE then we use a default expiration date for
			// all sessions
			// In this case, sessions must be explicitly closed otherwise GC is never going
			// to clean unused objects from
			// sessions.
			// Concurrency note: adding two times same expiration date is not a problem
			// since exp. date is the same. We avoid
			// locking.
			if (!sessionsExpireDates.containsKey(sessionID)) {
				final Date expirationDate;
				if (Configuration.Flags.CHECK_SESSION.getBooleanValue()) {
					final Calendar endDate = this.getSessionInfo(sessionID).getSecond();
					expirationDate = endDate.getTime();
				} else {
					expirationDate = Configuration.Flags.EXPIRATION_DATE_IF_NOCHECK_SESSION.getDate();
				}
				// === concurrency note === //
				// T1 is here, before put. This is a session that was already used and was
				// restarted.
				// T2 is in @getReferenceCounting and wants to remove session since it expired.
				// What if T2 removes it after the put?
				// Synchronization is needed to avoid this. It is not a big penalty if session
				// expiration date was already
				// added.
				synchronized (sessionID) {
					sessionsExpireDates.put(sessionID, expirationDate);
				}
			}
		}
	}

	/**
	 * Close session in EE. Subtract session references for GC.
	 * 
	 * @param sessionID
	 *            ID of session closing.
	 */
	public void closeSessionInEE(final SessionID sessionID) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("Closing session " + sessionID);
		}

		// Closing session means set expiration date to now
		sessionsExpireDates.put(sessionID, new Date()); // new date means current date

	}

	/**
	 * Get IDs of references retained by EE.
	 * 
	 * @return References retained by EE (sessions, alias...)
	 */
	public Set<ObjectID> getRetainedReferences() {
		LOGGER.debug("Getting retained references");

		final ExecutionEnvironmentHeapManager heapManger = (ExecutionEnvironmentHeapManager) this.dataClayHeapManager;

		final Set<ObjectID> retainedRefs = new HashSet<>();

		// memory references
		if (DEBUG_ENABLED && heapManger.getObjectIDsRetained().size() > 0) {
			LOGGER.debug("Adding memory references: " + heapManger.getObjectIDsRetained());
		}
		retainedRefs.addAll(heapManger.getObjectIDsRetained());

		if (DEBUG_ENABLED && this.referencesHoldBySessions.size() > 0) {
			LOGGER.debug("References hold by sessions: " + this.referencesHoldBySessions.keySet());
		}
		// session references
		final Date now = new Date();
		final Set<ObjectID> oidsUsingSession = new HashSet<>(this.referencesHoldBySessions.keySet()); // create a copy
		oidsUsingSession.removeAll(retainedRefs); // ignore references already retained
		final Set<SessionID> sessionsToClose = new HashSet<>();
		for (final ObjectID oid : oidsUsingSession) {
			final Set<SessionID> sessionsOfObj = this.referencesHoldBySessions.get(oid);
			if (DEBUG_ENABLED) {
				LOGGER.debug("Object {} is retained by sessions: {}", oid, sessionsOfObj);
			}
			final Iterator<SessionID> iterator = sessionsOfObj.iterator(); // use iterator to remove while iterating
			while (iterator.hasNext()) {
				final SessionID curSession = iterator.next();

				// ===== CHECK SESSION EXPIRED ===== //
				// ==== session counting design - Race condition ==== //
				// Race condition: object is send between two nodes and they are both notifying
				// 0 references. This is not
				// solved using quarantine in SL since during quarantine period they could do
				// the same and always send 0: while
				// one is
				// notifying 0, the other keeps the object, and send to the other before
				// notifying 0.
				// In order to avoid this, since session reference is added every time we
				// communicate
				// (even between nodes! not only client - node)
				// we do NOT remove session reference till GGC asks TWO times

				// Explicit closes of sessions set expire date to "now" but user can restart a
				// session
				// so, even if session is in quarantine, we must check date.

				boolean sessionExpired = false;
				final Date expireDate = this.sessionsExpireDates.get(curSession);
				if (DEBUG_ENABLED) {
					LOGGER.debug("Session {} expires at {}", curSession, expireDate);
				}
				if (expireDate != null && now.after(expireDate)) {
					if (DEBUG_ENABLED) {
						LOGGER.debug("Session {} is expired. It expired at {}", curSession, expireDate);
					}
					if (quarantineSessions.contains(curSession)) {
						if (DEBUG_ENABLED) {
							LOGGER.debug("Session " + curSession + " is in quarantine. Checking.");
						}
						// session is actually removed.
						sessionExpired = true;
						quarantineSessions.remove(curSession);

						// check again expiration date to see if it is expired. If expired, remove.
						final Date curExpireDate = this.sessionsExpireDates.get(curSession);
						if (curExpireDate != null && now.after(curExpireDate)) {
							if (DEBUG_ENABLED) {
								LOGGER.debug("Session " + curSession + " has actually expired.");
							}
							// do not remove expiration date from session till there is no objects in that
							// session
							// add it to sessions to close after all processing
							sessionsToClose.add(curSession);
						}

					} else {
						if (DEBUG_ENABLED) {
							LOGGER.debug("Session " + curSession + " has expired.  Adding session to quarantine.");
						}
						// add it to quarantine sessions
						quarantineSessions.add(curSession);
					}
				} else if (expireDate != null && !now.after(expireDate)) {
					if (DEBUG_ENABLED) {
						LOGGER.debug("Session " + curSession + " is not expired yet but has expiration date ");
					}
					// check if session was in quarantine: if so, remove it from there (session
					// restart)
					quarantineSessions.remove(curSession);
				} else {
					if (DEBUG_ENABLED) {
						LOGGER.debug("Session " + curSession + " has no expiration date.");
					}
				}
				if (sessionExpired) {
					if (DEBUG_ENABLED) {
						LOGGER.debug("Session " + curSession + " is actually expired for oid " + oid);
					}
					// close session
					iterator.remove(); // remove session reference from the object.

					// === concurrency note === //
					// when should we remove an entry in the referencesHoldBySessions map?
					// 1 - when no session is using the object
					// 2 - when object is not in memory
					// so, we check both here and we remove it if needed:
					// TODO: review this, what if after the check, sessions are added and we remove
					// it?
					if (sessionsOfObj.size() == 0) {
						if (DEBUG_ENABLED) {
							LOGGER.debug("Removing session reference to object {}", oid);
						}
						this.referencesHoldBySessions.remove(oid);
					} else if (sessionsOfObj.size() > 0) {
						if (DEBUG_ENABLED) {
							LOGGER.debug("Not removing session reference since object {} has more sessions", oid);
						}
					}

				} else {
					// add session reference
					retainedRefs.add(oid);
				}
			}
		}

		// Remove all expired sessions if, and only if, there is no object retained by
		// it. TODO: improve this implementation.
		for (final SessionID sessionToClose : sessionsToClose) {
			boolean sessionBeingUsed = false;
			for (final Set<SessionID> objSessions : referencesHoldBySessions.values()) {
				if (objSessions.contains(sessionToClose)) {
					sessionBeingUsed = true;
					break;
				}
			}
			if (!sessionBeingUsed) {
				this.sessionsExpireDates.remove(sessionToClose);
			}
		}

		return retainedRefs;
	}

		@Override
	protected DataClayObjectLoader getDataClayObjectLoader() {
		return this.dataClayObjLoader;
	}

	@Override
	public DataClayExecutionObject getFromHeap(final ObjectID objectID) {
		return (DataClayExecutionObject) this.dataClayHeapManager.getObject(objectID);
	}

	/**
	 * Finish cache threads.
	 * 
	 * @if some exception occurs
	 */
	public void finishCacheThreads() {
		try {
			this.sessionsCache.finishCacheThreads();
		} catch (final InterruptedException ex) {
			LOGGER.debug("finishCacheThreads error", ex);
		}
	}

	/**
	 * Clean caches.
	 */
	public void cleanCaches() {
		sessionsCache.clear();
		datasetsCache.clear();
		metaDataCache.clear();
	}

}
