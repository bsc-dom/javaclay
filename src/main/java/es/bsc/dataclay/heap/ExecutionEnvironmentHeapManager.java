
package es.bsc.dataclay.heap;

import java.lang.ref.Reference;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import es.bsc.dataclay.DataClayExecutionObject;
import es.bsc.dataclay.DataClayMockObject;
import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.commonruntime.DataServiceRuntime;
import es.bsc.dataclay.dataservice.DataService;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.reflection.Reflector;

/**
 * This class is responsible to manage objects in EE memory and update or store
 * them in disk.
 */
public class ExecutionEnvironmentHeapManager extends HeapManager {

	/** Memory percentage to consider that there is pressure */
	private static final float GC_MEMORY_PRESSURE_PERCENT = Configuration.Flags.MEMMGMT_PRESSURE_FRACTION
			.getFloatValue();

	/** Minimum time to be collected. */
	private static final long GC_MIN_TIME = Configuration.Flags.MEMMGMT_MIN_OBJECT_TIME.getLongValue();

	/** Indicates if a flushAll is being processed. */
	private boolean isFlushingAll = false;

	/** Indicates GC is being processed. */
	private boolean isProcessingGC = false;

	/** EE being monitorized. */
	private final DataService dataService;

	/** Total number of objects cleaned. */
	private static final AtomicInteger NUM_OBJS_CLEANED = new AtomicInteger(0);

	/** List to retain objects. */
	/**
	 * It is very important to be a sorted list, so first elements to arrive are
	 * cleaned before, in any deserialization from DB or parameter, objects
	 * deserialized first are referrers to objects deserialized later. Second ones
	 * cannot be GC if first ones are not cleaned. During GC,we should know that
	 * somehow. It's a hint but improves GC a lot. List must be thread-safe!! NOTE:
	 * Java concurrent hash map is more efficient than list. However, no insertion
	 * order is mantained. ConcurrentHashMap gain in performance is more than being
	 * unable to clean some objecs since it is in Critical Path (creation of
	 * objects).
	 **/
	protected final Map<ObjectID, DataClayObject> retainedObjects;

	/**
	 * Contructor.
	 * 
	 * @param DataServiceRuntime
	 *            The DataServiceRuntime.
	 */
	public ExecutionEnvironmentHeapManager(final DataServiceRuntime theruntime) {
		super(theruntime);
		dataService = theruntime.getDataService();
		retainedObjects = new ConcurrentHashMap<>();
	}

	/**
	 * Get object ids retained
	 * 
	 * @return object ids retained
	 */
	public final Set<ObjectID> getObjectIDsRetained() {
		if (DEBUG_ENABLED) {
			logger.trace("[==GC==] Retained refs: " + this.retainedObjects.size());
			logger.trace("[==GC==] Inmemory refs: " + this.inmemoryObjects.size());
		}
		return this.inmemoryObjects.keySet();
	}

	/**
	 * Check if memory is under pressure
	 * 
	 * @return Memory used percent
	 */
	private float getMemoryUsed() {
		// Check memory
		// Getting the runtime reference from system
		final Runtime runtime = Runtime.getRuntime();
		final long usedMemory = runtime.totalMemory() - runtime.freeMemory();
		final float usedMemoryPercent = (float) usedMemory / (float) runtime.totalMemory();
		return usedMemoryPercent;
	}

	/**
	 * This function nullifies an object in order to allow Java GC to work.
	 * 
	 * @param object
	 *            Object to nullify
	 */
	private void nullifyObject(final DataClayExecutionObject object) {
		if (DEBUG_ENABLED) {
			logger.trace("[==GC==] Nullify all fields of " + object.getObjectID() + " of class "
					+ object.getClass().getName());
		}
		Reflector.nullifyAllFields(object);
	}

	@Override
	public DataClayExecutionObject getObject(final ObjectID objectID) {
		return (DataClayExecutionObject) super.getObject(objectID);
	}

	@Override
	public void addToHeap(final DataClayObject dcObject) {
		super.addToHeapMap(dcObject);
		// Retain object in heap
		this.retainInHeap(dcObject);

	}

	/**
	 * Add a new Hard reference to the object provided. All code in stubs/exec
	 * classes using objects in dataClayheap are using weak references. In order to
	 * avoid objects to be GC without a flush in DB, HeapManager has hard-references
	 * to them and is the only one able to release them. This function creates the
	 * hard-reference.
	 * 
	 * @param object
	 *            Object to add
	 */
	public void retainInHeap(final DataClayObject object) {
		if (DEBUG_ENABLED) {
			logger.debug(
					"[==Heap==] Adding Hard Reference from HeapManager for object with ID " + object.getObjectID());
		}
		this.retainedObjects.put(object.getObjectID(), object);
	}

	/**
	 * Release hard reference to object with ID provided. Without hard reference,
	 * the object can be Garbage collected by Java GC.
	 * 
	 * @param objectID
	 *            ID of the object
	 */
	public final void releaseFromHeap(final ObjectID objectID) {
		if (DEBUG_ENABLED) {
			logger.debug("[==Heap==] Releasing Hard Reference from HeapManager for object with ID " + objectID);
		}
		this.retainedObjects.remove(objectID);
	}

	/**
	 * Update all objects in memory (or store them if new). Function called at
	 * shutdown.
	 */
	@Override
	public void flushAll() {

		// If there is another 'flush', return.
		if (this.isFlushingAll) {
			return;
		}

		this.isFlushingAll = true;
		if (DEBUG_ENABLED) {
			logger.debug("[==Shutdown==] Wait GC in progress (it can take a while)... ");
		}
		while (this.isProcessingGC) {
			try {
				Thread.sleep(Configuration.Flags.MEMMGMT_WAIT_TO_SHUTDOWN.getLongValue());
			} catch (final InterruptedException ex) {
				logger.debug("flushAll interrupted while sleeping", ex);
			}
		}
		// While map is not empty (objects can be added during and update)
		final Set<ObjectID> updatedOids = new HashSet<>();
		final Map<ObjectID, DataClayObject> objectsToUpdate = this.retainedObjects;
		if (DEBUG_ENABLED) {
			logger.debug("[==Shutdown==] Remaining in Heap " + heapSize());
			logger.debug("[==Shutdown==] Updating all objects  (Found " + objectsToUpdate.size() + ") ... ");
		}
		for (final DataClayObject curObj : objectsToUpdate.values()) {
			final DataClayExecutionObject obj = (DataClayExecutionObject) curObj;
			if (obj.isLoaded() && obj.getObjectID() != null && obj.isPersistent()) {
				if (DEBUG_ENABLED) {
					logger.debug("[==Shutdown==] Updating object. OID = " + obj.getObjectID() + " Loaded = "
							+ obj.isLoaded() + " isPersistent = " + obj.isPersistent());
				}
				dataService.gcCollectObjectInternal(obj);
			}
		}

		if (DEBUG_ENABLED) {
			logger.debug("[==Shutdown==] Updated " + updatedOids.size());

			// Clean all objects
			for (final ObjectID oid : updatedOids) {
				runtime.removeFromHeap(oid);
			}

			int remainingRefs = 0;
			int remainingUnloadedObjs = 0;
			for (final Entry<ObjectID, Reference<DataClayObject>> entry : this.inmemoryObjects.entrySet()) {
				final Reference<DataClayObject> ref = entry.getValue();
				if (ref != null) {
					final DataClayExecutionObject obj = (DataClayExecutionObject) ref.get();
					if (obj == null) {
						remainingRefs++;
					} else if (!obj.isLoaded()) {
						remainingUnloadedObjs++;
					}
				}
			}

			logger.debug("[==Shutdown==] All objects updated.");
			logger.debug("[==Shutdown==] Heap Remaining objects : " + runtime.heapSize());
			logger.debug("[==Shutdown==] Heap Remaining unloaded objects : " + remainingUnloadedObjs);
			logger.debug("[==Shutdown==] Heap Remaining weak references pointing to null : " + remainingRefs);
			logger.debug("[==Shutdown==] Number of lockers : " + runtime.numLockers());
			logger.debug("[==Shutdown==] Done!");

		}
	}

	/**
	 * Clean object (except if not loaded or being used)
	 * 
	 * @param object
	 *            Object to clean
	 */
	private void cleanObject(final DataClayExecutionObject object) {

		// Lock object (not locking executions!)
		// Lock is needed in case object is being nullified and some threads requires to
		// load it from disk.
		if (DEBUG_ENABLED) {
			logger.debug("[==GC==] Cleaning object " + object.getObjectID());
		}
		runtime.lock(object.getObjectID());
		if (DEBUG_ENABLED) {
			logger.debug("[==GC==] Start Collection of " + object.getObjectID() + " of class "
					+ object.getClass().getName() + ": locking.");
		}
		try {

			// Is object loaded? (always yes, sanity check)
			if (!object.isLoaded()) {
				// not collecting but remove it from retained references.
				if (DEBUG_ENABLED) {
					logger.debug("[==GC==] Not Collecting " + object.getObjectID() + " of class "
							+ object.getClass().getName() + ": is not loaded.");
					logger.debug(
							"[==GC==] Removing " + object.getObjectID() + " of class " + object.getClass().getName()
									+ " from Retained references. Currently retained: " + retainedObjects.size());
				}
				retainedObjects.remove(object.getObjectID());
				return;
			}

			// RACE CONDITION 1
			// Only remove from retained if object is dirty. Updating the object or using it
			// is possible even if we are in
			// this "locked" block since there is no lock in setters, ... During the
			// cleaning process it is possible that we
			// have a thread running a method on it. Since we set loaded to False, any new
			// "get" or "set" in the state of the
			// object will cause a loadFromDB. LoadFromDB is a function that has a lock also
			// and during this cleaning it will be
			// waiting. In the end, the worst case will be that the HeapManager decided to
			// clean an object and the object is
			// going to be used during or just after the cleaning. As explained, this race
			// condition is controlled using lock +
			// isLoaded
			// flag.
			object.setLoaded(false);

			// Is object dirty? Only collect if dirty or pending to register!
			// Collect it
			if (DEBUG_ENABLED) {
				logger.debug(
						"[==GC==] ** Collecting " + object.getObjectID() + " of class " + object.getClass().getName());
			}

			// This function is flushing all changes into disk, and storing and registering
			// if volatile
			dataService.gcCollectObjectInternal(object);

			// Set unloaded and nullify all fields
			this.nullifyObject(object);
			object.setDirty(false); // after nullify - it is not dirty
			final int numObjsCleaned = NUM_OBJS_CLEANED.incrementAndGet();
			if (numObjsCleaned % 100 == 0) {
				logger.info("[==GC==] Number of objects cleaned by GC: " + numObjsCleaned);
			}

			// RACE CONDITION 2
			// If some object was cleaned and removed from GC retained refs, it does NOT
			// mean it was removed
			// from Weak references map (Heap) because we will ONLY remove an entry in Heap
			// if it is
			// pointing to NULL, i.e. the Java GC removed it.
			// So, if some execution is requested after we remove an entry from retained
			// refs (we cleaned and send
			// the object to disk), we check if the
			// object is in Heap (see executeImplementation in DS) and therefore, we created
			// a new reference
			// making impossible for Java GC to clean the reference. We will add the object
			// to retained references
			// again once it is deserialized from DB.
			// No lock-race condition can happen here, loadDataClayObjectFromDb function is
			// locked, so it is not possible
			// to remove a retained reference after "retaining it" from deserialize from DB.
			// Remove it from Retained refs to allow Java GC action.
			if (DEBUG_ENABLED) {
				logger.debug("[==GC==] Removing " + object.getObjectID() + " of class " + object.getClass().getName()
						+ " from Retained references. Currently retained: " + retainedObjects.size());
			}
			releaseFromHeap(object.getObjectID());

		} catch (Exception | Error ex) {
			logger.debug("cleanObject error", ex);
		} finally {
			if (DEBUG_ENABLED) {
				logger.debug("[==GC==] Collecting " + object.getObjectID() + " of class " + object.getClass().getName()
						+ ": unlocking.");
			}
			// Unlock it
			runtime.unlock(object.getObjectID());
		}
	}

	@Override
	public final void run() {
		this.isProcessingGC = true;
		if (isFlushingAll) {
			if (DEBUG_ENABLED) {
				logger.debug("[==GC==] Not collecting due to shutdown or other GC.");
			}
			return;
		}
		try {
			float usedMemoryPercent = this.getMemoryUsed();
			if (usedMemoryPercent > GC_MEMORY_PRESSURE_PERCENT) {

				if (DEBUG_ENABLED) {
					logger.debug("[==GC==] Memory GC started. Retained objects: " + retainedObjects.size());
				}
				final long threadID = Thread.currentThread().getId();
				if (Configuration.mockTesting) {
					DataClayMockObject.setCurrentThreadLib(this.runtime);
				}
				final long timestamp = System.currentTimeMillis();
				for (final DataClayObject curObject : retainedObjects.values()) {

					final DataClayExecutionObject object = (DataClayExecutionObject) curObject;

					usedMemoryPercent = this.getMemoryUsed();
					if (DEBUG_ENABLED && logger.isTraceEnabled()) {
						final Runtime runtime = Runtime.getRuntime();
						final long usedMemory = runtime.totalMemory() - runtime.freeMemory();
						final int mb = 1024 * 1024;
						final StringBuilder sb = new StringBuilder();
						sb.append("[==GC==] Checking memory pressure: ");
						sb.append(" Used Memory (MB):" + usedMemory / mb);
						sb.append(", Free Memory (MB):" + runtime.freeMemory() / mb);
						sb.append(", Total Memory (MB):" + runtime.totalMemory() / mb);
						if (runtime.maxMemory() == Long.MAX_VALUE) {
							sb.append(", Max Memory: Unlimited");
						} else {
							sb.append(",Max Memory (MB):" + runtime.maxMemory() / mb);
						}
						logger.trace(sb.toString());

						if (this.retainedObjects.size() > 0) {
							logger.trace("[==GC==] Number of objects retained in memory : " + retainedObjects.size()
									+ ". Number of weak refs in memory : " + this.inmemoryObjects.size());

							final Map<String, Integer> countByClass = new ConcurrentHashMap<>();
							for (final DataClayObject obj : retainedObjects.values()) {
								final String className = obj.getClass().getName();
								final Integer count = countByClass.get(className);
								if (count == null) {
									countByClass.put(className, 1);
								} else {
									countByClass.put(className, count + 1);
								}
							}

							logger.trace("[==GC==] Objects in memory : " + countByClass);
						} else {
							logger.trace("[==GC==] No objects to collect.");
						}
						logger.trace("[==GC==] ++++ CURRENT MEMORY PRESSURE: " + (usedMemoryPercent * 100) + " %");
					}

					// Check memory
					if (this.getMemoryUsed() < GC_MEMORY_PRESSURE_PERCENT) {
						if (DEBUG_ENABLED && logger.isTraceEnabled()) {
							logger.trace("[==GC==] Not collecting more since there is enough memory");
						}
						break;
					}

					// 1 - Is object too young?
					if ((timestamp - object.getCreationTimeStamp()) < GC_MIN_TIME) {
						if (DEBUG_ENABLED) {
							logger.debug("[==GC==] Not collecting " + object.getObjectID() + " of class "
									+ object.getClass().getName() + " because it is too young.");
						}
						continue;
					}

					// 2 - Is object loaded or being used by any thread? (check inside clean
					// function)
					cleanObject(object);

					if (this.isFlushingAll) {
						break;
					}

				}

				this.cleanReferencesAndLockers();

				if (Configuration.mockTesting) {
					DataClayMockObject.removeCurrentThreadLib();
				}
				if (DEBUG_ENABLED) {
					logger.debug("[==GC==] Memory GC finished.");
				}

				// Notify JVM to clean weak references as soon as possible.
				System.gc();
			}
		} catch (Exception | Error ex) {
			ex.printStackTrace();
			logger.debug("run error", ex);
		} finally {
			this.isProcessingGC = false;
		}
	}

}
