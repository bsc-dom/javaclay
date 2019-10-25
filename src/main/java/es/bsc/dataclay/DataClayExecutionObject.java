package es.bsc.dataclay;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.commonruntime.DataServiceRuntime;
import es.bsc.dataclay.dataservice.LazyTask;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.SessionID;

/**
 * This class represents a Object in a DataClay's execution environment.
 */
@SuppressWarnings("serial")
public class DataClayExecutionObject extends DataClayObject {

	/** Logger. */
	private static final Logger logger = LogManager.getLogger("DataClayExecutionObject");

	// CHECKSTYLE:OFF

	/**
	 * Indicates if the object has been modified since loaded from disk. Only
	 * server-side.
	 */
	private final AtomicBoolean dirty = new AtomicBoolean(false);

	/**
	 * Owner session ID. Set when a volatile is assigned and used during garbage
	 * collection.
	 */
	private SessionID ownerSessionIDforVolatiles;

	/** Creation time stamp. Used for GC algorithms. */
	private long creationTimeStamp;

	/** Number of references pointing to instance. */
	public int numRefs;

	/** Indicates if the object is pending to register or not. */
	private boolean pendingToRegister;

	// CHECKSTYLE:ON

	/**
	 * Basic constructor
	 */
	public DataClayExecutionObject() {
		init();
		initializeObjectAsVolatile();
	}

	/**
	 * Constructor
	 * 
	 * @param theobjectID
	 *            ID of the object
	 */
	public DataClayExecutionObject(final ObjectID theobjectID) {
		super(theobjectID);
		init();
	}

	/**
	 * Initialize object
	 */
	private void init() {
		this.setIsPersistent(true); // inside DC all objects are persistent.
	}

	/**
	 * Initialize object with state 'persistent' with proper flags. See same
	 * function in DataClayExecutionObject for a different initialization. This
	 * design is intended to be clear with object state. Usually, since constructors
	 * are calling initializeObjectAsVolatile, this function 'overrides' flags set
	 */
	@Override
	public void initializeObjectAsPersistent() {
		setIsPersistent(true);

		// by default, loaded = true for volatiles created inside executions
		// this function is used for objects being deserialized and therefore they might
		// be unloaded
		// same happens for pending to register flag.
		setLoaded(false);
		setPendingToRegister(false);

	}

	/**
	 * Initialize object with state 'volatile' with proper flags. Usually, volatile
	 * state is created by a stub, app, exec class,.. See same function in
	 * DataClayExecutionObject for a different initialization. This design is
	 * intended to be clear with object state.
	 */
	public void initializeObjectAsVolatile() {
		// == volatiles flags == //
		setCreationTimeStamp(System.currentTimeMillis());
		this.setLoaded(true);
		this.setPendingToRegister(true);
		this.setHint(getLib().getHint());
		this.setMasterLocation(this.getHint());
		this.setOwnerSessionIDforVolatiles(getLib().getSessionID()); // only if needed
	}

	/**
	 * Set dirty flag to true
	 */
	public void setAsDirty() {
		this.dirty.set(true);
	}

	/**
	 * Get dirty flag
	 * 
	 * @return TRUE if object is dirty. False otherwise.
	 */
	public boolean isDirty() {
		return dirty.get();
	}

	/**
	 * Set dirty flag
	 * 
	 * @param thedirtyValue
	 *            dirty flag value
	 */
	public void setDirty(final boolean thedirtyValue) {
		this.dirty.set(thedirtyValue);

	}

	/**
	 * Get the DataClayObject::isLoaded
	 * 
	 * @return the isLoaded
	 */
	public final boolean isLoaded() {
		if (!isPrefetchingAccess()) {
			incrementAccessCount();
			if (isLoaded.get()) {
				incrementHitCount();
			}
		}
		return isLoaded.get();
	}

	/**
	 * @return the ownerSessionIDforVolatiles
	 */
	public SessionID getOwnerSessionIDforVolatiles() {
		return ownerSessionIDforVolatiles;
	}

	/**
	 * @param newownerSessionIDforVolatiles
	 *            the ownerSessionIDforVolatiles to set
	 */
	public void setOwnerSessionIDforVolatiles(final SessionID newownerSessionIDforVolatiles) {
		this.ownerSessionIDforVolatiles = newownerSessionIDforVolatiles;
	}

	@Override
	public void setDataSetID(final DataSetID newDataSetID) {
		this.dataSetID = newDataSetID;
		// No remote call to set dataset like in super class.
	}

	/**
	 * @return the creationTimeStamp
	 */
	public long getCreationTimeStamp() {
		return creationTimeStamp;
	}

	/**
	 * @param thecreationTimeStamp
	 *            the creationTimeStamp to set
	 */
	public void setCreationTimeStamp(final long thecreationTimeStamp) {
		this.creationTimeStamp = thecreationTimeStamp;
	}

	/**
	 * @param thenumRefs
	 *            the numRefs to set
	 */
	public void setNumRefs(final int thenumRefs) {
		this.numRefs = thenumRefs;
	}

	@Override
	public List<Object> filter(final String conditions) {
		return filterObject(conditions);
	}

	/**
	 * @return the pendingToRegister
	 */
	public boolean isPendingToRegister() {
		return pendingToRegister;
	}

	/**
	 * @param ispendingToRegister
	 *            the pendingToRegister to set
	 */
	public void setPendingToRegister(final boolean ispendingToRegister) {
		this.pendingToRegister = ispendingToRegister;
	}

	/**
	 * Add lazy task to be run in DataClay.
	 * 
	 * @param objectID
	 *            ID of the object running the task
	 * @param implID
	 *            ID of the implementation to run
	 * @param classID
	 *            ID of the class in which the task is located
	 */
	protected void addLazyTask(final ObjectID objectID, final ImplementationID implID, final MetaClassID classID) {		
		/*if (pendingToRegister) {
			return;
		}*/
		
		// If prefetching is not activated in dataClay, do not add lazy task (even if it exists in the registered model)
		if (!Configuration.Flags.PREFETCHING_ENABLED.getBooleanValue()) {
			return;
		}

		if (DEBUG_ENABLED) {
			logger.debug("[==PREFETCHING==] Adding lazy task.");
		}

		final SessionID sessID = getLib().getSessionID();
		logger.info("[==PREFETCHING==] Adding lazy task for session {} and implementation {} at {}", sessID, implID,
				new Date(System.currentTimeMillis()).toString());

		// Only called in DS
		final LazyTask lazyTask = new LazyTask(sessID, objectID, implID, classID);
		final DataServiceRuntime dsRuntime = (DataServiceRuntime) getLib();
		dsRuntime.getDataService().addLazyTask(objectID, lazyTask);
		/****
		 * Add several times (make sure to have as many threads as duplicate tasks)
		 ****/
	}
}
