
package es.bsc.dataclay.heap;

import java.lang.ref.Reference;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import es.bsc.dataclay.DataClayExecutionObject;
import es.bsc.dataclay.api.DataClay;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.commonruntime.DataClayRuntime;
import es.bsc.dataclay.commonruntime.RuntimeUtils;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.ObjectID;

/**
 * Heap manager for dataClay runtimes.
 *
 *         This class is intended to manage all dataClay objects in runtime's memory.
 *
 */
public abstract class HeapManager extends TimerTask {
	protected static final Logger logger = LogManager.getLogger("heap.HeapManager");

	/** Indicates if debug is enabled. */
	protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** Runtime being monitorized. */
	protected final DataClayRuntime runtime;

	// DESIGN NOTE: Actually, we need a WeakHashMap but since hashCode function can be defined by users (DataClayObject hashCode can
	// be overrided, we use ObjectID as a key and we check if the GC removed the reference every time we want to get (see
	// getObject).
	/** Map of objects in memory. */
	protected final Map<ObjectID, Reference<DataClayObject>> inmemoryObjects;

	/**
	 * Constructor.
	 * 
	 * @param theruntime
	 *            Runtime to manage.
	 */
	public HeapManager(final DataClayRuntime theruntime) {
		runtime = theruntime;
		inmemoryObjects = new ConcurrentHashMap<>();
	}

	/**
	 * Clean references in Heap pointing to null (weak references) and Lockers not being used.
	 */
	public void cleanReferencesAndLockers() {
		int cleanedObjects = 0;
		final Iterator<Entry<ObjectID, Reference<DataClayObject>>> it = inmemoryObjects.entrySet().iterator();
		while (it.hasNext()) {
			final Entry<ObjectID, Reference<DataClayObject>> entryRef = it.next();
			final Reference<DataClayObject> ref = entryRef.getValue();
			if (ref == null || ref.get() == null) {
				it.remove(); // Remove element
				cleanedObjects++;
			}
		}

		runtime.cleanLockers();

		if (DEBUG_ENABLED) {
			if (cleanedObjects > 0) {
				logger.debug("[==Heap==] Cleaned " + cleanedObjects + " weak references");
				cleanedObjects = 0;
			}
		}
	}

	/**
	 * Get object from Heap
	 * @param objectID
	 *            ID of object
	 * @return DataClayObject or NULL if not found.
	 */
	public DataClayObject getObject(final ObjectID objectID) {
		DataClayObject obj = null;
		Reference<DataClayObject> ref = null;
		if (DEBUG_ENABLED) {
			logger.debug("[==Heap==] Checking if object is in cache " + objectID);
		}
		ref = this.inmemoryObjects.get(objectID);
		if (ref == null) {
			if (DEBUG_ENABLED) {
				logger.debug("[==Heap==] MISS in Heap with oid " + objectID);
			}
		} else if (ref.get() != null) {
			if (DEBUG_ENABLED) {
				logger.debug("[==Heap==] HIT in Heap with ID " + objectID + " system.id= " + System.identityHashCode(obj));
			}
			obj = ref.get();
		} else {
			if (DEBUG_ENABLED) {
				logger.debug("[==Heap==] MISS Found weak reference pointing to null (currently being GC) for " + objectID);
			}
		}
		return obj;
	}

	/**
	 * Create a weak reference of the object provided and add it into the heap.
	 * @param dcObject
	 *            Object to add.
	 */
	protected void addToHeapMap(final DataClayObject dcObject) {
		final Reference<DataClayObject> newRef = RuntimeUtils.newReference(
				dcObject);
		this.inmemoryObjects.put(dcObject.getObjectID(), newRef);
	}

	/**
	 * Add object into Heap. This function can be different in EE or client. Therefore, it is abstract.
	 * @param dcObject
	 *            the object to add
	 */
	public abstract void addToHeap(final DataClayObject dcObject);

	/**
	 * Remove reference from Heap. Even if we remove it from the heap, the object won't be Garbage collected by JavaGC till
	 *        HeapManager flushes the object and releases it.
	 * @param objectID
	 *            ID of the object
	 */
	public final void removeFromHeap(final ObjectID objectID) {
		this.inmemoryObjects.remove(objectID);
		if (DEBUG_ENABLED) {
			logger.debug("[==Heap==] Removed from Heap " + objectID);
		}
	}

	/**
	 * Check if there is an object with ID provided.
	 * @param objectID
	 *            ID of the object.
	 * @return TRUE if exists in memory. FALSE otherwise.
	 */
	public final boolean existsObject(final ObjectID objectID) {
		return this.inmemoryObjects.containsKey(objectID);
	}

	/**
	 * Get number of objects in memory.
	 * @return Number of objects in memory.
	 */
	public final int heapSize() {
		return this.inmemoryObjects.size();
	}

	/**
	 * Get number of loaded objects in memory.
	 * @return Number of loaded objects in memory.
	 */
	public final int numLoadedObjs() {
		int loadedObjects = 0;
		for (Reference<DataClayObject> ref : this.inmemoryObjects.values()) {
			DataClayObject dcObj = ref.get();
			if (((DataClayExecutionObject) dcObj).isLoaded()) {
				loadedObjects++;
			}
		}
		return loadedObjects;
	}

	/**
	 * Flush all objects in Heap.
	 */
	public abstract void flushAll();

	/**
	 * Update an objectID.
	 * 
	 * @param oldObjectID
	 *            ID of the object to be updated.
	 * @param newObjectID
	 *            new ID of the object.
	 */
	public void updateObjectID(final ObjectID oldObjectID, final ObjectID newObjectID) {
		inmemoryObjects.put(newObjectID, inmemoryObjects.remove(oldObjectID));
	}
}
