package es.bsc.dataclay.loader;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.DataClayExecutionObject;
import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.api.BackendID;
import es.bsc.dataclay.commonruntime.DataClayRuntime;
import es.bsc.dataclay.serialization.lib.ObjectWithDataParamOrReturn;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;

/**
 * This class is responsable to create DataClayObjects and load them with data
 * coming from different resources. All possible constructions of DataClayObject
 * should be included here. All possible "filling instance" use-cases should be
 * managed here. Most of lockers should be located here.
 * 
 *
 */
public abstract class DataClayObjectLoader {
	protected static final Logger logger = LogManager.getLogger("DataClayObjectLoader");

	/** Indicates if debug is enabled. */
	protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/**
	 * Constructor
	 */
	public DataClayObjectLoader() {

	}

	/**
	 * Create a new instance using proper class loader
	 * 
	 * @param classID
	 *            ID of class
	 * @param objectID
	 *            ID of object
	 * @return Instance
	 */
	protected abstract DataClayObject newInstance(final MetaClassID classID, final ObjectID objectID);

	/**
	 * Get runtime
	 * 
	 * @return The runtime being managed.
	 */
	protected abstract DataClayRuntime getRuntime();

	/**
	 * Check if instance exists in Heap or create a new PERSISTENT instance if
	 * needed
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
	public DataClayObject getOrNewPersistentInstance(final MetaClassID classID, final ObjectID objectID,
			final BackendID hint) {
		// Check if already exists, if so, return it, otherwise, return new instance.
		DataClayObject obj = null;
		final DataClayRuntime runtime = getRuntime();
		obj = runtime.getFromHeap(objectID);
		if (obj == null) {
			runtime.lock(objectID);
			try {
				obj = runtime.getFromHeap(objectID);
				if (obj == null) {
					obj = newInstanceInternal(classID, objectID, hint);
				}

				// == set flags == //
				obj.initializeObjectAsPersistent();

			} finally {
				runtime.unlock(objectID);
			}
		}
		return obj;
	}

	/**
	 * Create a new instance using proper class loader
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
	protected DataClayObject newInstanceInternal(final MetaClassID classID, final ObjectID objectID,
			final BackendID hint) {

		if (DEBUG_ENABLED) {
			logger.debug("[==NewInstance==] Object " + objectID + " not in cache. Creating new instance.");
		}

		DataClayObject obj = null;
		final DataClayRuntime runtime = getRuntime();
		// Get class ID of object
		final MetaClassID theClassID = classID;
		final BackendID theHint = hint;
		obj = newInstance(theClassID, objectID);
		if (theHint != null) {
			if (DEBUG_ENABLED) {
				logger.debug("[==Hint==] Setting hint (getOrNewInstanceInternal) on instance " + obj.getObjectID()
						+ " the hint : " + runtime.getDSNameOfHint(hint));
			}
			obj.setHint(theHint);
		}

		if (DEBUG_ENABLED) {
			logger.debug("[==NewInstance==] Created new instance for " + objectID + ".");
		}

		return obj;
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

		// Check if already exists, if so, return it, otherwise, return new instance.
		// RACE CONDITION DESIGN
		// There are two objects A and B, A -> B, A is persistent and B is volatile.
		// There are two threads T1 and T2, T1 is executing a method on A that uses B,
		// when deserializing A, B is loaded into
		// heap as a persistent object (all associations are persistent). However, it is
		// actually a volatile send by T2.
		// When a volatile server is received and a persistent instance is found, this
		// persistent instance should be "replaced"
		// by the new volatile server.
		DataClayObject volatileObj = null;
		if (DEBUG_ENABLED) {
			logger.debug("Persistent object " + objectID + " under deserialization.");
		}
		getRuntime().lock(objectID);
		try {

			volatileObj = getRuntime().getFromHeap(objectID);
			if (volatileObj == null) {
				volatileObj = newInstanceInternal(classID, objectID, hint);
			}

			deserializeDataIntoInstance(volatileObj, objWithData, ifaceBitMaps);

			// force flags as volatile
			// WARNING: RACE CONDITION at EE - during deserialization of volatiles the
			// object may be created and
			// loaded in Heap but not "fully deserialized" yet so even if any execution find
			// it in the
			// heap, object might
			// be not ready (null fields, and no, so is loaded cannot
			// be true till object was fully deserialized)
			volatileObj.initializeObjectAsVolatile();
			if (DEBUG_ENABLED) {
				logger.debug("Persistent object " + objectID + " is not under deserialization anymore.");
			}
		} finally {
			getRuntime().unlock(objectID);
		}

		return volatileObj;
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
	public abstract void deserializeDataIntoInstance(final DataClayObject instance,
			final ObjectWithDataParamOrReturn data, final Map<MetaClassID, byte[]> ifaceBitMaps);

}
