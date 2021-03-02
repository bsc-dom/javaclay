
/**
 * @file DataService.java
 * @date Oct 23, 2012
 */
package es.bsc.dataclay.storagelocation;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.commonruntime.DataServiceRuntime;
import es.bsc.dataclay.dataservice.api.DataServiceAPI;
import es.bsc.dataclay.dbhandler.DBHandler;
import es.bsc.dataclay.dbhandler.DBHandlerConf;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.ObjectID;

// CHECKSTYLE:ON

/**
 * This class is responsible to manage data of the objects stored in the system.
 * 
 */
public final class StorageLocationService {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger("StorageLocation");

	/** Indicates if debug is enabled. */
	private static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** Databases of objects. */
	private final Map<ExecutionEnvironmentID, DBHandler> objectDBs = new ConcurrentHashMap<>();

	/** Database basic configuration. */
	private final DBHandlerConf dbBaseConf;

	/** Disk Garbage collector. */
	private DataClayDiskGC diskGC;
	
	/** Runtime used to connect to other EE, SL or LM. */
	private DataServiceRuntime runtime;
	
	/** Associated execution environment IDs. */
	private final Set<ExecutionEnvironmentID> associatedExecutionEnvironmentIDs = new HashSet<>();

	/**
	 * Constructor
	 * 
	 * @param dbHandlerconf
	 *            Configuration for the db handler
	 */
	public StorageLocationService(final DBHandlerConf dbHandlerconf) {
		this.dbBaseConf = dbHandlerconf;
	}

	/**
	 * Get DBHandler for execution environment ID provided
	 * 
	 * @param executionEnvID
	 *            ID of EE
	 * @return DBHandler
	 */
	private DBHandler getDBHandler(final ExecutionEnvironmentID executionEnvID) {
		DBHandler dbHandler = this.objectDBs.get(executionEnvID);
		if (dbHandler == null) {
			synchronized (this) {
				dbHandler = this.objectDBs.get(executionEnvID);
				if (dbHandler == null) {
					dbBaseConf.setDbname(executionEnvID.toString());
					dbHandler = dbBaseConf.getDBHandler();
					dbHandler.open();
					this.objectDBs.put(executionEnvID, dbHandler);
				}
			}
		}
		return dbHandler;
	}

	/**
	 * Initialize
	 * 
	 * @param theruntime
	 *            Runtime to be used in Storage Location.
	 * @param associatedEEID
	 *            Associated EE ID.
	 */
	public void initialize(final DataServiceRuntime theruntime, final ExecutionEnvironmentID associatedEEID) {
		associateExecutionEnvironment(associatedEEID);
		runtime = theruntime;
		if (Configuration.Flags.GLOBAL_GC_ENABLED.getBooleanValue()) {
			diskGC = new DataClayDiskGC(this, theruntime, associatedEEID);
		}
	}

	/**
	 * Store the object.
	 * 
	 * @param eeID
	 *            ID of the EE triggering the call
	 * @param objectID
	 *            ID of the object
	 * @param bytes
	 *            Bytes of the object
	 */
	public void store(final ExecutionEnvironmentID eeID, final ObjectID objectID, final byte[] bytes) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[StorageLocation] Storing object {} into {} ", objectID, eeID);
		}
		getDBHandler(eeID).store(objectID, bytes);
		if (Configuration.Flags.GLOBAL_GC_ENABLED.getBooleanValue()) {
			this.diskGC.addToQueueReferenceCounting(eeID, objectID, bytes, false, true);
		}
	}

	/**
	 * Get from DB
	 * 
	 * @param eeID
	 *            ID of the EE triggering the call
	 * @param objectID
	 *            ID of the object
	 * @return Bytes of object
	 */
	public byte[] get(final ExecutionEnvironmentID eeID, final ObjectID objectID) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[StorageLocation] Getting object {} from {} ", objectID, eeID);
		}
		final byte[] bytes = getDBHandler(eeID).get(objectID);
		if (Configuration.Flags.GLOBAL_GC_ENABLED.getBooleanValue()) {
			this.diskGC.addToQueueReferenceCounting(eeID, objectID, bytes, true, false);
		}

		return bytes;
	}

	/**
	 * Check if object exists in DB
	 * 
	 * @param eeID
	 *            ID of the EE triggering the call
	 * @param objectID
	 *            ID of the object
	 * @return TRUE if object exists. FALSE otherwise.
	 */
	public boolean exists(final ExecutionEnvironmentID eeID, final ObjectID objectID) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[StorageLocation] Checking if exists the object {} in {} ", objectID, eeID);
		}
		return getDBHandler(eeID).exists(objectID);
	}

	/**
	 * Updates an object identified by the ID provided with the new values provided.
	 * 
	 * @param eeID
	 *            ID of the EE triggering the call
	 * @param objectID
	 *            ID of the object.
	 * @param newbytes
	 *            New byte values
	 * @param dirty
	 *            Indicates object has been modified. If false, it means that bytes only contains reference counting
	 *            information. DESIGN NOTE: in order to be able to find out which references where removed in complex objects
	 *            (arrays, collections) GlobalGc decreases all pointed references in a Get procedure and increase them again
	 *            (except removed ones) during update. While in EE, objects have memory references so they cannot be removed
	 *            neither.
	 * 
	 */
	public void update(final ExecutionEnvironmentID eeID, final ObjectID objectID, final byte[] newbytes, final boolean dirty) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[StorageLocation] Updating object " + objectID + ". Dirty: " + dirty);
		}
		if (dirty) {
			getDBHandler(eeID).update(objectID, newbytes);
		}
		if (Configuration.Flags.GLOBAL_GC_ENABLED.getBooleanValue()) {
			this.diskGC.addToQueueReferenceCounting(eeID, objectID, newbytes, false, dirty);
		}
	}

	/**
	 * Deletes and object from the database.
	 * 
	 * @param eeID
	 *            ID of the EE triggering the call
	 * @param objectID
	 *            ID of the object to delete
	 */
	public void delete(final ExecutionEnvironmentID eeID, final ObjectID objectID) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[StorageLocation] Deleting object {} from  {} ", objectID, eeID);
		}
		getDBHandler(eeID).delete(objectID);
	}

	/**
	 * Update counters of references.
	 * 
	 * @param updateCounterRefs
	 *            Update counter of references.
	 */
	public void updateRefs(final Map<ObjectID, Integer> updateCounterRefs) {
		if (DEBUG_ENABLED) {
			LOGGER.debug("[StorageLocation] Updating references with " + updateCounterRefs.toString());
		}
		if (Configuration.Flags.GLOBAL_GC_ENABLED.getBooleanValue()) {
			this.diskGC.updateRefs(updateCounterRefs);
		}
	}

	/**
	 * Get DbHandler. Used for testing purposes.
	 * 
	 * @param eeID
	 *            ID of EE
	 * @return DbHandler.
	 */
	public DBHandler getDbHandler(final ExecutionEnvironmentID eeID) {
		return getDBHandler(eeID);
	}

	/**
	 * Associate execution environment id to this SL
	 * 
	 * @param executionEnvironmentID
	 *            ID to associate
	 */
	public void associateExecutionEnvironment(final ExecutionEnvironmentID executionEnvironmentID) {
		this.associatedExecutionEnvironmentIDs.add(executionEnvironmentID);
	}

	/**
	 * Get associated execution environments id to this SL
	 * 
	 * @return associated execution environments
	 */
	public Set<ExecutionEnvironmentID> getAssociateExecutionEnvironments() {
		return this.associatedExecutionEnvironmentIDs;
	}

	/**
	 * Close DbHandler
	 */
	public void closeDbHandler(final ExecutionEnvironmentID eeID) {
		if (!getDBHandler(eeID).isClosed()) {
			getDBHandler(eeID).close();
		}
	}

	/**
	 * Shutdown storage location
	 */
	public void shutDownGarbageCollector() {
		// WARNING: due to design of GlobalGC, shutdown of SL should be always being done before EE shutdown i.e. Java always
		// closed
		// before
		// EE. TODO: check what happens if we close Java SL and Python EE still available and doing things.
		if (Configuration.Flags.GLOBAL_GC_ENABLED.getBooleanValue()) {
			this.diskGC.shutDown();
		}
	}

	/**
	 * Return number of references pointing to object.
	 * 
	 * @param objectID
	 *            ID of object
	 * @return Number of references pointing to object
	 */
	public int getNumReferencesTo(final ObjectID objectID) {
		if (Configuration.Flags.GLOBAL_GC_ENABLED.getBooleanValue()) {
			return this.diskGC.getNumReferencesTo(objectID);
		}
		return 0;
	}

	/**
	 * Check if the object exists in SL or in any EE memory associated to current SL 
	 * @param objectID ID of the object to check
	 * @return TRUE if the object either exists in SL disk or in EE memory.
	 */
	public boolean exists(final ObjectID objectID) {
				
		for (final ExecutionEnvironmentID associatedExecutionEnvironmentID : getAssociateExecutionEnvironments()) {
			
			// Check if object exists in disk
			if (this.getDbHandler(associatedExecutionEnvironmentID).exists(objectID)) { 
				return true;
			}

			// Check if object exists in EE memory
			final DataServiceAPI dsAPI = runtime.getRemoteDSAPI(associatedExecutionEnvironmentID);
			if (dsAPI.existsInEE(objectID)) { 
				return true;
			}
		}
		return false;
	}
}
