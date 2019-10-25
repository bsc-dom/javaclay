
package es.bsc.dataclay.util.classloaders;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.MetaClassID;

import org.apache.logging.log4j.LogManager;

/**
 * Locker of class.
 */
public final class SyncClass {
	
	/** Logger. */
	private final Logger logger = LogManager.getLogger(SyncClass.class);
	/** Log flag. */
	private final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();
	
	/** Number of threads waiting for the class locker. */
    private int waiting = 0;
    
    /** Class locker. */
    private final Lock mutex;
    
    /** ID of the class. */
    private final MetaClassID metaclassID;
    
    /**
     * Constructor
     * @param newMetaClassID ID of the class locker.
     */
    public SyncClass(final MetaClassID newMetaClassID) { 
    	mutex = new ReentrantLock();
    	metaclassID = newMetaClassID;
    	if (DEBUG_ENABLED) { 
    		logger.debug("NEW LOCK " + metaclassID);
    	}
    }
    
    /**
     * Lock class.
     */
    public void lock() {
    	if (DEBUG_ENABLED) {
    		logger.debug("WAITING LOCK " + metaclassID);
    	}
        waiting++;
        mutex.lock();
    	if (DEBUG_ENABLED) {
    		logger.debug("LOCKED " + metaclassID);
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
    		logger.debug("UNLOCKED " + metaclassID);
    	}
        mutex.unlock();
        waiting--;
    }
    
    /**
     * Get class id of locker
     * @return ID of the class
     */
    public MetaClassID getMetaClassID() { 
    	return metaclassID;
    }
}
