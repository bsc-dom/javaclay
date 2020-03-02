package es.bsc.dataclay.commonruntime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.SessionID;
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
			// Choose location if needed
			// If object is already persistent -> it must have a Hint (location = hint here)
			// If object is not persistent -> location is choosen (provided backend id or
			// random, hash...).
			location = optionalDestBackendID;
			if (location == null) {
				location = chooseLocation(dcObject, alias);
			}
		}

		// Force registration due to alias
		if (alias != null) {
			// Add a new alias to an object.
			// Use cases:
			// 1 - object was persisted without alias and not yet registered -> we need to
			// register it with new alias.
			// 2 - object was persisted and it is already registered -> we only add a new
			// alias
			// 3 - object was persisted with an alias and it must be already registered ->
			// we add a new alias.

			// From client side, we cannot check if object is REGISTERED or not (we do not
			// have isPendingToRegister like EE)
			// Therefore, we call LogicModule with all information for registration.
			final RegistrationInfo regInfo = new RegistrationInfo(dcObject.getObjectID(), dcObject.getMetaClassID(),
					sessionID, dcObject.getDataSetID());

			// it is important to register the object once we are sure it is in EE.

			// Warning: logicModule.registerObject function checks if the object was already registered or not,
			// in case it was, it adds a new alias
			final ObjectID newID = logicModule.registerObject(regInfo, (ExecutionEnvironmentID) location, alias, Langs.LANG_JAVA);
			this.updateObjectID(dcObject, newID);
		}

		// ==== Make persistent === //
		if (!dcObject.isPersistent()) {
			// Serialize objects
			dcObject.setMasterLocation(location);

			final SerializedParametersOrReturn objectsToPersist = this.serializeMakePersistent(location, dcObject, null,
					recursive);

			// Avoid some race-conditions in communication (make persistent + execute where
			// execute arrives before).
			for (final Entry<Integer, ObjectWithDataParamOrReturn> param : objectsToPersist.getVolatileObjs().entrySet()) {
				super.volatileParametersBeingSend.add(param.getValue().getObjectID());
			}

			// Call EE
			final DataServiceAPI dsAPI = getRemoteExecutionEnvironment(location);
			dsAPI.makePersistent(sessionID, objectsToPersist);

			// Avoid some race-conditions in communication (make persistent + execute where
			// execute arrives before).
			for (final Entry<Integer, ObjectWithDataParamOrReturn> param : objectsToPersist.getVolatileObjs().entrySet()) {
				super.volatileParametersBeingSend.remove(param.getValue().getObjectID());
			}
			// =========================== //
		}

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
	public final SerializedParametersOrReturn serializeMakePersistent(final BackendID location,
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

		// client
		return serObject;
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
		this.logicModule.closeSession(this.getSessionID());
	}

	@Override
	protected DataClayObjectLoader getDataClayObjectLoader() {
		return this.dataClayObjLoader;
	}
}
