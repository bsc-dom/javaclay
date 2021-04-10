
package es.bsc.dataclay.util.classloaders;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.MetaClassID;

import org.apache.logging.log4j.LogManager;

/** Class lockers. */
public final class ClassLockers {
	
	/** Logger. */
	private static final Logger logger = LogManager.getLogger(ClassLockers.class);
	/** Log flag. */
	private static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();
	
	/** Map of lockers for objects. */
	private final Map<MetaClassID, SyncClass> lockers;

	/**
	 * Class lockers constructor.
	 */
	public ClassLockers() { 
		lockers = new ConcurrentHashMap<MetaClassID, SyncClass>();
	}
	
	/**
	 * Get locker for class with ID provided. 
	 * @param classID ID of the class locker to get.
	 * @return Locker of the class provided.
	 */
	public synchronized SyncClass getLocker(final MetaClassID classID) { 
		SyncClass locker = lockers.get(classID);
		if (locker == null) { 
			locker = new SyncClass(classID);
			lockers.put(classID, locker);
		}
		return locker;
	}
	
	/**
	 * Get number of lockers.
	 * @return Number of lockers.
	 */
	public synchronized int numLockers() { 
		return lockers.size();
	}
	
	/**
	 * Try to remove locker provided if no other thread is waiting
	 * @param locker Locker to remove.
	 */
	public synchronized void tryRemoveLocker(final SyncClass locker) { 
		// Remove locker if no threads are waiting for it.
		final int numThreadsWaiting = locker.getNumThreadsWaiting();
		if (numThreadsWaiting == 0) {
	    	if (DEBUG_ENABLED) { 
	    		logger.debug("REMOVE LOCK " + locker.getMetaClassID());
	    	}
			this.lockers.remove(locker.getMetaClassID());
		}
	}
	
	/**
	 * Clear all lockers.
	 */
	public void clear() {
    	if (DEBUG_ENABLED) { 
    		logger.debug("CLEAR CLASS LOCKS ");
    	}
		this.lockers.clear();
	}


}
