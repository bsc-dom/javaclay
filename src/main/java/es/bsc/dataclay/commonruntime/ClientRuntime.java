package es.bsc.dataclay.commonruntime;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.api.BackendID;
import es.bsc.dataclay.api.DataClayException;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.dataservice.api.DataServiceAPI;
import es.bsc.dataclay.exceptions.metadataservice.ObjectNotRegisteredException;
import es.bsc.dataclay.heap.ClientHeapManager;
import es.bsc.dataclay.heap.HeapManager;
import es.bsc.dataclay.loader.ClientObjectLoader;
import es.bsc.dataclay.loader.DataClayObjectLoader;
import es.bsc.dataclay.serialization.DataClaySerializable;
import es.bsc.dataclay.serialization.lib.DataClaySerializationLib;
import es.bsc.dataclay.serialization.lib.ObjectWithDataParamOrReturn;
import es.bsc.dataclay.serialization.lib.SerializedParametersOrReturn;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.classloaders.DataClayClassLoader;
import es.bsc.dataclay.util.ids.*;
import es.bsc.dataclay.util.management.metadataservice.MetaDataInfo;
import es.bsc.dataclay.util.management.metadataservice.RegistrationInfo;
import es.bsc.dataclay.util.management.sessionmgr.SessionInfo;
import es.bsc.dataclay.util.management.stubs.StubInfo;

/**
 * This class contains functions for users to interact with DataClay.
 */
public final class ClientRuntime extends DataClayRuntime {

	/** Client session. */
	private SessionInfo clientSession;

	/** User-defined 'LOCAL' backend */
	private BackendID localBackend = null;

	/**
	 * Constructor for Mock tests.
	 */
	public ClientRuntime() {
		this.dataClayObjLoader = new ClientObjectLoader(this);
		this.dataClayHeapManager = new ClientHeapManager(this);
		this.threadPool = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
			@Override
			public Thread newThread(final Runnable r) {
				final Thread t = Executors.defaultThreadFactory().newThread(r);
				t.setDaemon(true);
				t.setName("Client-MemoryGC");
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
	 */
	@Override
	public void initialize(final String logicModuleHost, final int logicModulePort, final String originHostName)
			throws Exception {
		super.initialize(logicModuleHost, logicModulePort, originHostName);
		if (Configuration.Flags.MEMORY_GC_ENABLED.getBooleanValue()) {
			// Create Repetitively task
			this.threadPool.scheduleAtFixedRate(dataClayHeapManager, 0,
					Configuration.Flags.MEMMGMT_CHECK_TIME_INTERVAL.getLongValue(), TimeUnit.MILLISECONDS);
		}

	}

	@Override
	public DataClayObject getOrNewPersistentInstance(final MetaClassID classID, final ObjectID objectID,
			final BackendID hint) {

		MetaClassID theClassID = classID;
		if (theClassID == null) {
			final MetaDataInfo mdInfo = super.getObjectMetadata(objectID);
			if (mdInfo == null) {
				// WARNING: If this is a volatile, class id should not be null, so no metadata
				// should be search.
				// NOTE: if it is a volatile and hint failed, it means that object is actually
				// not registered
				throw new ObjectNotRegisteredException(objectID);
			}
			theClassID = mdInfo.getMetaclassID();
		}

		return this.dataClayObjLoader.getOrNewPersistentInstance(theClassID, objectID, hint);
	}

	/**
	 * Sets ID of backend to be identified as LOCAL backend.
	 * 
	 * @param backendID
	 *            ID of LOCAL backend.
	 */
	public void setLocalBackend(final BackendID backendID) {
		localBackend = backendID;
	}

	/**
	 * Retrieves current default backend
	 * 
	 * @return
	 */
	@Override
	public BackendID getLocalBackend() {
		return localBackend;
	}

	/**
	 * Set sessionID for client
	 * 
	 * @param newSessionfo
	 *            Info of session
	 */
	public void setSessionInfo(final SessionInfo newSessionfo) {
		this.clientSession = newSessionfo;
	}

	@Override
	public SessionID getSessionID() {
		return clientSession.getSessionID();
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

	@Override
	protected Object executeRemoteImplementationInternal(final DataClayObject objectInWhichToExec,
			final ImplementationID implID, final Object[] params) {
		if (DEBUG_ENABLED) {
			try {
				final StubInfo stubInfo = objectInWhichToExec.getStubInfo();
				final String methodSignature = stubInfo.getImplementationByID(implID.toString()).getSignature();
				LOGGER.debug("[==Execution==] ** New execution ** For method " + methodSignature
						+ ". Client requests a new remote execution for object " + objectInWhichToExec.getObjectID()
						+ " and implementation " + implID);
			} catch (final Exception ex) {
				LOGGER.debug("Error while retrieving debug information on executeRemoteImplementationInternal", ex);
			}
		}

		// ============================== PARAMS/RETURNS ========================== //
		boolean usingHint = false;
		BackendID execLocationID = null;
		// === HINT === //
		if (objectInWhichToExec.getHint() != null) {
			if (DEBUG_ENABLED) {
				LOGGER.debug("[==Execution==] Using HINT for " + objectInWhichToExec.getObjectID());
			}
			execLocationID = objectInWhichToExec.getHint();
			usingHint = true;
		} else {
			execLocationID = chooseLocation(objectInWhichToExec, null);
		}

		try {
			return super.callExecuteToDS(objectInWhichToExec, params, implID, execLocationID, usingHint);
		} finally {
			if (DEBUG_ENABLED) {
				LOGGER.debug("[==Execution==] ** Finished execution ** For object " + objectInWhichToExec.getObjectID()
				+ " and implementation " + implID);
			}
		}
	}

	@Override
	public BackendID makePersistent(final DataClayObject dcObject, final BackendID optionalDestBackendID,
			final boolean recursive, final String alias) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==MakePersistent==] Starting make persistent of object " + dcObject.getObjectID());
		}

		final SessionID sessionID = checkAndGetSession(new String[] {}, new Object[] {});
		BackendID location = dcObject.getHint();
		if (location == null) {
			location = optionalDestBackendID;
			if (location == null) {
				location = chooseLocation(dcObject, alias);
			} else {
				if (DEBUG_ENABLED) {
					LOGGER.debug("[==MakePersistent==] Destination backend id was specified by user: " + location);
				}
			}
		} else {
			if (DEBUG_ENABLED) {
				LOGGER.debug("[==MakePersistent==] Destination backend is hint: " + location);
			}
		}

		if (!dcObject.isPersistent()) {
			// Force registration due to alias
			if (alias != null) {
				if (DEBUG_ENABLED) {
					LOGGER.debug("[==MakePersistent==] Alias {} is not null, registering ", alias);
				}
				final RegistrationInfo regInfo = new RegistrationInfo(dcObject.getObjectID(), dcObject.getMetaClassID(),
						sessionID, dcObject.getDataSetID(), alias);
				// TODO ask DANI
				// it is important to register the object once we are sure it is in EE.
				List<RegistrationInfo> regInfos = new ArrayList<>();
				regInfos.add(regInfo);
				final List<ObjectID> newIDs = logicModule.registerObjects(regInfos,
						(ExecutionEnvironmentID) location, Langs.LANG_JAVA);
				ObjectID newID = newIDs.get(0);
				this.updateObjectID(dcObject, newID);
			}

			// Serialize objects
			dcObject.setMasterLocation(location);
			dcObject.setAlias(alias);
			List<ObjectWithDataParamOrReturn> objectsToPersist = this.serializeMakePersistent(location, dcObject, null, recursive);

			// Avoid some race-conditions in communication (make persistent + execute where
			// execute arrives before).
			for (final ObjectWithDataParamOrReturn param : objectsToPersist) {
				super.volatileParametersBeingSend.add(param.getObjectID());
			}

			// Call EE
			final DataServiceAPI dsAPI = getRemoteExecutionEnvironment(location);
			dsAPI.makePersistent(sessionID, objectsToPersist);

			// Avoid some race-conditions in communication (make persistent + execute where
			// execute arrives before).
			for (final ObjectWithDataParamOrReturn param : objectsToPersist) {
				super.volatileParametersBeingSend.remove(param.getObjectID());
			}
			// =========================== //
		}

		// update cache of metadata info
		Set<ExecutionEnvironmentID> locations = new HashSet<>();
		locations.add((ExecutionEnvironmentID) location);
		MetaDataInfo newMetaDataInfo = new MetaDataInfo(dcObject.getObjectID(),
				dcObject.getDataSetID(), dcObject.getMetaClassID(), false,
				locations, alias, null);
		this.metaDataCache.put(dcObject.getObjectID(), newMetaDataInfo);

		return location;
	}

	/**
	 * Serialize for make persistent.
	 * 
	 * @param location
	 *            Where objects will be stored
	 * @param objectToPersist
	 *            Object to persist
	 * @param ifaceBitMaps
	 *            Interface bitmaps
	 * @param recursive
	 *            Indicates if sub-objects must be serialized also.
	 * @return Serialized parameters
	 */
	public final List<ObjectWithDataParamOrReturn> serializeMakePersistent(final BackendID location,
																				 final DataClayObject objectToPersist, final Map<MetaClassID, byte[]> ifaceBitMaps,
																				 final boolean recursive) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[==Serialization==] Serializing for make persistent.");
		}

		final List<DataClaySerializable> wrapList = new ArrayList<>();
		wrapList.add(objectToPersist);

		// Serialize
		// IfaceBitMaps = null. From client stub is controlling it.
		final SerializedParametersOrReturn serObject = DataClaySerializationLib.serializeParamsOrReturn(wrapList,
				ifaceBitMaps, this, false, location, !recursive); // no hint volatiles since volatiles are not going to

		if (DEBUG_ENABLED) {
			LOGGER.debug("[==Serialization==] Serialized " + serObject);
		}

		//
		return new ArrayList<>(serObject.getVolatileObjs().values());
	}

	@Override
	public boolean isDSLib() {
		return false;
	}

	@Override
	public String getClassNameInternal(final MetaClassID classID) {
		return DataClayClassLoader.getClass(classID).getName();
	}

	@Override
	public BackendID getHint() {
		return null;
	}

	@Override
	protected Map<MetaClassID, byte[]> getIfaceBitMaps() {
		return clientSession.getIfaceBitmaps();
	}

	/**
	 * Close session.
	 */
	public void closeSession() {
		LOGGER.info("Closing session ... ");
		this.logicModule.closeSession(this.getSessionID());
		this.setSessionInfo(null);
	}

	@Override
	protected DataClayObjectLoader getDataClayObjectLoader() {
		return this.dataClayObjLoader;
	}

	@Override
	public void detachObjectFromSession(final ObjectID objectID, final ExecutionEnvironmentID hint) {
		final SessionID sessionID = getSessionID();
		// Get an arbitrary object location
		BackendID execLocationID = hint;
		if (hint == null) {
			execLocationID = getLocation(objectID);
		}
		final DataServiceAPI dsAPI = getRemoteExecutionEnvironment(execLocationID);
		dsAPI.detachObjectFromSession(objectID, sessionID);
	}

	@Override
	public void deleteAlias(final DataClayObject dcObject) {
		// Get an arbitrary object location
		final SessionID sessionID = getSessionID();
		BackendID execLocationID = dcObject.getHint();
		ObjectID objectID = dcObject.getObjectID();
		String alias = dcObject.getAlias();
		if (execLocationID == null) {
			execLocationID = getLocation(objectID);
		}
		final DataServiceAPI dsAPI = getRemoteExecutionEnvironment(execLocationID);
		dsAPI.deleteAlias(sessionID, objectID);
		if (alias != null) {
			aliasCache.remove(alias);
		}
		dcObject.setAlias(null);
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
		final SessionID sessionID = checkAndGetSession(new String[] {}, new Object[] {});
		// Get object location
		BackendID execLocationID = objectHint;
		if (objectHint == null) {
			execLocationID = getLocation(objectID);
		}

		// Get language from origin location
		final DataServiceAPI dsAPI = getRemoteExecutionEnvironment(execLocationID);
		dsAPI.federate(sessionID, objectID, externalExecutionEnvironmentID, recursive);
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
		final SessionID sessionID = checkAndGetSession(new String[] {}, new Object[] {});
		// Get object location
		BackendID execLocationID = objectHint;
		if (objectHint == null) {
			execLocationID = getLocation(objectID);
		}

		// Get language from origin location
		final DataServiceAPI dsAPI = getRemoteExecutionEnvironment(execLocationID);
		dsAPI.unfederate(sessionID, objectID, externalExecutionEnvironmentID, recursive);

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
		BackendID execLocationID = this.getLocation(objectID);
		final DataServiceAPI dsAPI = getRemoteExecutionEnvironment(execLocationID);
		dsAPI.synchronize(sessionID, objectID, remoteImplID, serializedParams, null);
	}
}
