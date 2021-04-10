
package es.bsc.dataclay.heap;

import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Locker of class.
 */
public final class ObjectLock {

	/** Logger. */
	private final Logger logger = LogManager.getLogger("LockerPool");
	/** Log flag. */
	private final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** Number of threads waiting for the locker. */
    private int waiting = 0;

    /** locker. */
    private final Lock mutex;

    /** ID of the object. */
    private final ObjectID objectID;

    /**
     * Constructor
     * @param objectID ID of the locker.
     */
    public ObjectLock(final ObjectID objectID) {
    	mutex = new ReentrantLock();
    	this.objectID = objectID;
    	if (DEBUG_ENABLED) {
    		logger.debug("NEW LOCK " + objectID);
    	}
    }
    
    /**
     * Lock class.
     */
    public void lock() {
    	if (DEBUG_ENABLED) {
    		logger.debug("WAITING LOCK " + objectID);
    	}
        waiting++;
        mutex.lock();
    	if (DEBUG_ENABLED) {
    		logger.debug("LOCKED " + objectID);
    	}
    }
    
    /**
     * Get number of threads waiting.
     * @return number of threads waiting
     */
    public int getNumThreadsWaiting() { 
    	return waiting;
    }
    
    /**
     * Unlock class.
     */
    public void unlock() {
    	if (DEBUG_ENABLED) {
    		logger.debug("UNLOCKED " + objectID);
    	}
        mutex.unlock();
        waiting--;
    }
    
    /**
     * Get class id of locker
     * @return ID of the object
     */
    public ObjectID getObjectID() {
    	return objectID;
    }
}
