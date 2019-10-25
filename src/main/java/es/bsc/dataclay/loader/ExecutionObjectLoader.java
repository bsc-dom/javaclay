package es.bsc.dataclay.loader;

import java.util.Map;

import es.bsc.dataclay.DataClayExecutionObject;
import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.api.BackendID;
import es.bsc.dataclay.commonruntime.DataClayRuntime;
import es.bsc.dataclay.commonruntime.DataServiceRuntime;
import es.bsc.dataclay.communication.grpc.Utils;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages;
import es.bsc.dataclay.serialization.lib.DataClayDeserializationLib;
import es.bsc.dataclay.serialization.lib.ObjectWithDataParamOrReturn;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.classloaders.DataClayClassLoaderSrv;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;

/**
 * This class is responsable to create DataClayObjects and load them with data
 * coming from different resources. All possible constructions of DataClayObject
 * should be included here. All possible "filling instance" use-cases should be
 * managed here.
 * 
 *
 */
public class ExecutionObjectLoader extends DataClayObjectLoader {

	/** Runtime being managed. **/
	private final DataServiceRuntime runtime;

	/**
	 * Constructor
	 * 
	 * @param theruntime
	 *            Runtime being managed
	 */
	public ExecutionObjectLoader(final DataServiceRuntime theruntime) {
		this.runtime = theruntime;
	}

	@Override
	protected DataClayRuntime getRuntime() {
		return runtime;
	}

	@Override
	public DataClayExecutionObject newInstance(final MetaClassID classID, final ObjectID objectID) {
		logger.debug("New instance {}", objectID);
		return (DataClayExecutionObject) DataClayClassLoaderSrv.newInstance(classID, objectID);
	}

	/**
	 * Check if instance exists in objects map or create a new PERSISTENT instance
	 * if needed
	 * 
	 * @param classID
	 *            ID of the class in case it is needed (not need to query) if null,
	 *            look for class id in metadata.
	 * @param objectID
	 *            ID of object
	 * @param hint
	 *            Can be null. Hint in case object is a volatile in another DS and
	 *            we need information.
	 * @return Instance
	 */
	@Override
	public DataClayExecutionObject getOrNewPersistentInstance(final MetaClassID classID, final ObjectID objectID,
			final BackendID hint) {
		return (DataClayExecutionObject) super.getOrNewPersistentInstance(classID, objectID, hint);
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
	public void deserializeDataIntoInstance(final DataClayObject instance, final ObjectWithDataParamOrReturn data,
			final Map<MetaClassID, byte[]> ifaceBitMaps) {
		DataClayDeserializationLib.deserializeObjectWithData(data, (DataClayExecutionObject) instance, ifaceBitMaps,
				runtime, runtime.getSessionID(), true);
	}

	/**
	 * Get from DB and deserialize into instance
	 * 
	 * @param objectToFill
	 *            Instance
	 */
	private void getFromDBAndFill(final DataClayExecutionObject objectToFill) {
		final ObjectID objectID = objectToFill.getObjectID();
		final byte[] objectBytes = runtime.dsRef.getLocal(objectID);
		DataClayDeserializationLib.deserializeObjectFromDBBytes(objectToFill, objectBytes, runtime);
		objectToFill.setHint(runtime.getHint());
	}

	/**
	 * Get object from memory or database and WAIT in case we are still waiting for
	 * it to be persisted.
	 * 
	 * @param objectID
	 *            ID of the object to get
	 * @param retry
	 *            Indicates if we should retry and wait.
	 * @return The the object.
	 */
	public DataClayExecutionObject getOrNewPersistentInstanceFromDB(final ObjectID objectID, final boolean retry) {

		// Retry while object is not 'registered' (not talking about 'stored'!)
		// IMPORTANT: This is different than waiting for an object to be stored
		// If the object is not registered we still do not know the class id of the
		// instance
		// in which to load the bytes.
		// Due to concurrency we should read bytes and deserialize and unlock.
		// Therefore there is Two waiting loops. (can we do it better?, more locking?)
		boolean obtained = false;
		long curTimeWait = 0;
		DataClayExecutionObject instance = null;
		while (!obtained) {
			runtime.lock(objectID);
			try {
				logger.debug("[==GetInstance==] Going to check or create new instance {}", objectID);
				instance = runtime.getFromHeap(objectID);
				if (instance == null) {

					final byte[] objectBytes = runtime.dsRef.getLocal(objectID);
					final CommonMessages.PersistentObjectInDB persObjInDB = DataClayDeserializationLib
							.deserializeMessageFromDB(objectBytes);
					final DataClayObjectMetaData metadata = Utils.getMetaData(persObjInDB.getMetadata());

					final MetaClassID instanceClassID = metadata.getMetaClassID(0);
					instance = newInstance(instanceClassID, objectID);

					instance.initializeObjectAsPersistent();

					// use special function to avoid deserializing twice
					DataClayDeserializationLib.deserializeObjectFromDBBytesAux(instance, metadata,
							persObjInDB.getData(), runtime);

					// == set flags == //
					instance.setHint(runtime.getHint());
				}

				if (!instance.isLoaded()) {
					logger.debug("[==GetInstance==] Getting from db and filling instance {}", objectID);
					getFromDBAndFill(instance);
				}
				obtained = true;

			} catch (final Exception e) {
				if (!retry || curTimeWait >= Configuration.Flags.TIMEOUT_WAIT_REGISTERED.getLongValue()) {
					logger.debug("[==GetInstance==] Object {} not registered. Throwing error", objectID, e);
					throw e;
				}
				curTimeWait += Configuration.Flags.SLEEP_WAIT_REGISTERED.getLongValue();
				try {
					Thread.sleep(Configuration.Flags.SLEEP_WAIT_REGISTERED.getLongValue());
				} catch (final InterruptedException ie) {
					logger.debug("Interrupted while sleeping. Aborting", e);
				}
			} finally {
				runtime.unlock(objectID);
			}
		}
		return instance;
	}

	/**
	 * Load DataClayObject from Database
	 * 
	 * @param objectToFill
	 *            DataClayObject instance to fill
	 * @param retry
	 *            Indicates retry loading in case it is not in db.
	 */
	public void loadDataClayObjectFromDb(final DataClayExecutionObject objectToFill, final boolean retry) {

		final ObjectID objectID = objectToFill.getObjectID();
		if (DEBUG_ENABLED) {
			logger.debug(
					"[==DB==] Loading object from Db: " + objectID + " of class " + objectToFill.getClass().getName());
		}
		boolean obtained = false;
		long curTimeWait = 0;
		while (!obtained) {
			runtime.lock(objectID);
			try {
				if (!objectToFill.isLoaded()) { // If is here to avoid concurrency gaps
					getFromDBAndFill(objectToFill);
				}
				obtained = true;
			} catch (final Exception dbe) {
				if (!retry || curTimeWait >= Configuration.Flags.TIMEOUT_WAIT_REGISTERED.getLongValue()) {
					if (DEBUG_ENABLED) {
						logger.debug(
								"[==DB==] Object " + objectID + " not found in DB. Throwing error (not to retry). ");
					}
					throw dbe;
				}
				if (DEBUG_ENABLED) {
					logger.debug("[==DB==] Object " + objectID + " not found in DB. Retrying...");
				}
				curTimeWait += Configuration.Flags.SLEEP_WAIT_REGISTERED.getLongValue();
				try {
					Thread.sleep(Configuration.Flags.SLEEP_WAIT_REGISTERED.getLongValue());
				} catch (final InterruptedException ex) {
					logger.debug("loadDataClayObjectFromDb interrupted while sleeping", ex);
				}
			} finally {
				runtime.unlock(objectID);
			}
		}

		if (DEBUG_ENABLED) {
			logger.debug("[==DB==] Object successfully loaded from Db: {}", objectID);
		}
	}
}
