
package es.bsc.dataclay.heap;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.ObjectID;

import org.apache.logging.log4j.LogManager;

/**
 * Objects lockers class.
 */
public final class LockerPool {

	/** Logger. */
	private final Logger logger = LogManager.getLogger("LockerPool");

	/** Indicates if debug is enabled. */
	private static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** Map of lockers for objects. */
	private final Map<ObjectID, ReentrantLock> lockers;

	/**
	 * Lockers constructor.
	 */
	public LockerPool() {
		lockers = new ConcurrentHashMap<>();
	}

	/**
	 * Lock the object with id provider
	 * @param objectID
	 *            ID of the object to lock
	 */
	public void lock(final ObjectID objectID) {
		ReentrantLock locker;

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Locking design.
		// ==============
		// Java ConcurrentHashMap guarantees that all operations done are thread-safe but NOT atomic. In our case,
		// imagine the code below and two threads located in the lines specified as comments:
		//
		// locker = lockers.get(objectID); //ObjectID is the same for both threads
		// if (locker == null) { //Thread 2 is here
		// locker = new ReentrantLock();
		// lockers.put(objectID, lock); //Thread 1 is here
		// }
		//
		// Thread 1 was the first to arrive and find out that there is no locker for the object "he" wants to lock. Therefore,
		// Thread 1 creates a new locker. During creation of the Locker, Thread 2 arrives and find no locker.
		// Thread 1 put the locker in the ConcurrentHashMap. Thread 2 also creates a locker and overrides Thread 1's locker.
		// This is a problem we should solve since both threads are using differents lockers for the same object, loosing the
		// concept of lockers.
		//
		// In order to solve that we need to create a synchronized block. We could create a synchronized block based in ObjectID since
		// Threads accessing/creating lockers with same ObjectIDs can have a problem. However, our current implementation does NOT
		// guarantee that two ObjectIDs representing same ID (ie with same UUID) are the same instances in Java memory.
		// Therefore we cannot create a synchronized block using ObjectID. We could think about forcing all ObjectIDs to be
		// unique ("same-instances") in memory (only one instance of ObjectID representing an object) but it ends in having same synchr.
		// but in the creation of the ObjectID (it can be in any request). Now, adding the synchronized block here is better
		// in order to only lock 'if-needed' instead of doing it at creation time.
		//
		// Then, what should we synchronize? Anything that is 'common' for all threads. In our case, the Locker Pool is unique
		// per runtime (server or client). For example:
		//
		// synchronized (this) {
		// locker = lockers.get(objectID);
		// if (locker == null) {
		// locker = new ReentrantLock();
		// lockers.put(objectID, lock);
		// }
		// }
		//
		// This would solve our problem. But it's a bottleneck! All threads that wants to lock anything, will end up locking
		// the pool. Since our problem is just at 'creation' of lockers we move the synchronized block inside the if and we
		// apply the design of 'double-check' of lockers:
		//
		// locker = lockers.get(objectID);
		// if (locker == null) {
		// synchronized (this) { // ensure only one locker is created per object
		// locker = lockers.get(objectID);
		// if (locker == null) {
		// locker = new ReentrantLock();
		// lockers.put(objectID, locker);
		// }
		// }
		// }
		//
		// All threads that try to 'create' lockers are going to have the overhead of synchronizing the locker pool. However,
		// it is not our critical path, since in our critical path everything is in cache and lockers exist.
		//
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		synchronized (this) { // ensure only one locker is created per object USE STRING INTERN SO ALL STRINGS ARE EQUAL!
			locker = lockers.get(objectID);
			if (locker == null) {
				//synchronized (this) { // ensure only one locker is created per object
					locker = lockers.get(objectID);
					if (locker == null) {
						locker = new ReentrantLock();
						lockers.put(objectID, locker);
					}
				//}
			}
		}
		if (DEBUG_ENABLED) {
			logger.debug("[==Lockers==] Waiting for lock " + objectID);
		}
		locker.lock(); // waits til the lock is available
		if (DEBUG_ENABLED) {
			logger.debug("[==Lockers==] Locked " + objectID);
		}
	}

	/**
	 * Unlock the object with id provider
	 * @param objectID
	 *            ID of the object to lock
	 */
	public void unlock(final ObjectID objectID) {
		/**
		 * Unlock object with object ID provided. If the locker does not exist, it means it was already released and cleaned from
		 * memory. Since our lockers are Reentrant, there can be many 'unlock' calls unlocking the same locker. If our Memory manager
		 * wants to clean a locker, it checks if it is unlocked and if so, it removes it.
		 */
		final ReentrantLock locker = lockers.get(objectID);
		if (DEBUG_ENABLED) {
			logger.debug("[==Lockers==] Unlocking " + objectID);
		}
		if (locker != null) {
			locker.unlock();
			if (DEBUG_ENABLED) {
				logger.debug("[==Lockers==] Unlocked " + objectID);
			}
		}
	}

	/**
	 * Clean lockers.
	 */
	public void cleanLockers() {
		int cleanedLockers = 0;
		if (DEBUG_ENABLED) {
			logger.debug("[==Lockers==] Number of lockers: {}", lockers.size());
		}

			for (final ObjectID objectID : new HashSet<>(lockers.keySet())) {
				if (DEBUG_ENABLED) {
					logger.debug("[==Lockers==] Going to remove locker " + objectID);
				}

				synchronized (this) { // prevent the object to be locked before cleaning
					final ReentrantLock locker = lockers.get(objectID);
					if (!locker.isLocked()) { // It could have been locked before
						lockers.remove(objectID);
						if (DEBUG_ENABLED) {
							logger.debug("[==Lockers==] Removed locker " + objectID);
						}
						cleanedLockers++;
					}
				}
			}

			if (DEBUG_ENABLED) {
				if (cleanedLockers > 0) {
					logger.debug("[==Lockers==] Lockers cleaned " + cleanedLockers);
				}
			}

	}

	/**
	 * Get number of lockers.
	 * @return Number of lockers
	 */
	public int numLockers() {
		return lockers.size();
	}
}
