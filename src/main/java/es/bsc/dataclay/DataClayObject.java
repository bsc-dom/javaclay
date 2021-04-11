package es.bsc.dataclay;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.Spliterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import es.bsc.dataclay.api.BackendID;
import es.bsc.dataclay.api.DataClay;
import es.bsc.dataclay.commonruntime.ClientRuntime;
import es.bsc.dataclay.commonruntime.DataClayRuntime;
import es.bsc.dataclay.logic.classmgr.bytecode.java.constants.ByteCodeFieldNames;
import es.bsc.dataclay.serialization.DataClaySerializable;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.java.DataClayJavaWrapper;
import es.bsc.dataclay.serialization.lib.DataClayDeserializationLib;
import es.bsc.dataclay.serialization.lib.DataClaySerializationLib;
import es.bsc.dataclay.serialization.lib.SerializedParametersOrReturn;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.classloaders.DataClayClassLoader;
import es.bsc.dataclay.util.classloaders.DataClayClassLoaderSrv;
import es.bsc.dataclay.util.filtering.ASTParser;
import es.bsc.dataclay.util.filtering.Condition;
import es.bsc.dataclay.util.filtering.ConditionParser;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.management.stubs.StubInfo;
import es.bsc.dataclay.util.yaml.CommonYAML;
import storage.StorageObject;
import storage.StubItf;

/**
 * This class represents a Object in DataClay.
 */
public class DataClayObject extends StorageObject implements DataClaySerializable, Serializable, StubItf {
	/** Indicates if debug is enabled. */
	protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** Logger. */
	public static final Logger logger = LogManager.getLogger("DataClayObject");

	/** Serial version UID. */
	private static final long serialVersionUID = 1772790872967833442L;

	// CHECKSTYLE:OF
	/** Client runtime. */
	public static DataClayRuntime clientRuntime = new ClientRuntime();

	/** ObjectID of the stub instance. */
	protected ObjectID objectID;

	/** Indicates if the object is registered or not. */
	protected AtomicBoolean isPersistent = new AtomicBoolean(false);

	/** Indicates if the object was filled with data. */
	protected AtomicBoolean isLoaded = new AtomicBoolean(false);

	/** HINT: Indicates object Execution Location. */
	private BackendID hint;

	/**
	 * ID of dataset of object. Used for user to specify which DataSet to use in store.
	 */
	protected DataSetID dataSetID;

	/** StubInfos cache. */
	private static Map<String, StubInfo> stubInfosCache = new ConcurrentHashMap<>();

	/** Exec.StubInfos cache. */
	private static Map<String, StubInfo> execStubInfosCache = new ConcurrentHashMap<>();

	/** Main storage location for the object **/
	private BackendID masterLocation;

	/** 'LOCAL' location */
	public static BackendID LOCAL;

	/** Alias of the object. */
	private String alias;

	/** Indicates if object is read only. */
	private boolean isReadOnly;

	/** dataClay instance */
	private DataClayInstanceID externalDataClayID;

	/** Original objectID if object was versioned. */
	private ObjectID originalObjectID;

	/** ID of original object location. */
	private ExecutionEnvironmentID rootLocation;

	/** ID of origin object location (replicas): origin of the replica. */
	private ExecutionEnvironmentID originLocation;

	/** IDs of locations this replica-object was replicated to. */
	private Set<ExecutionEnvironmentID> replicaLocations;

	/**
	 * RT PREFETCHING FIELDS
	 */

	private static transient boolean isPrefetchingAccess = false;
	
	private static transient long accessCount = 0;
	
	private static transient long hitCount = 0;

	// CHECKSTYLE:ON

	/**
	 * Constructor
	 */
	public DataClayObject() {
		this.init(new ObjectID());
		// Initialize as volatile
		initializeObjectAsVolatile();
	}

	/**
	 * Constructor
	 * 
	 * @param theobjectID
	 *            ID of the object
	 */
	public DataClayObject(final ObjectID theobjectID) {
		this.init(theobjectID);
	}

	/**
	 * Initialize object
	 * 
	 * @param theobjectID
	 *            ID of the object
	 */
	private void init(final ObjectID theobjectID) {
		final ObjectID oid = theobjectID;
		this.setObjectIDUnsafe(oid);
		// Add object to dataClay's heap
		getLib().addToHeap(this);
	}

	/**
	 * Initialize object with state 'persistent' with proper flags. See same function in DataClayExecutionObject for a different
	 * initialization. This design is intended to be clear with object state. Usually, since constructors are calling
	 * initializeObjectAsVolatile, this function 'overrides' flags set
	 */
	public void initializeObjectAsPersistent() {
		this.setIsPersistent(true);
	}

	/**
	 * Initialize object with state 'volatile' with proper flags. Usually, volatile state is created by a stub, app, exec
	 * class,.. See same function in DataClayExecutionObject for a different initialization. This design is intended to be clear
	 * with object state.
	 */
	public void initializeObjectAsVolatile() {
		this.setLoaded(true);
	}

	/**
	 * Set the DataClayObject::isLoaded
	 * 
	 * @param newisLoaded
	 *            the isLoaded to set
	 */
	public final void setLoaded(final boolean newisLoaded) {
		this.isLoaded.set(newisLoaded);
	}

	/**
	 * Generic static method to instantiate a DataClayObject by its alias
	 * 
	 * @param classID
	 *            id of real class of the instance
	 * @param alias
	 *            alias of the instance
	 * @param safe
	 *            if true, checks that an object with the provided alias exists
	 * @return the reference to persistent object of given class with given alias
	 */
	protected static DataClayObject getByAlias(final MetaClassID classID, final String alias, boolean safe) {
		return getLib().getObjectByAlias(alias, classID, safe);
	}

	
	/**
	 * This method can be used from registered methods to access objects by alias, this method
	 * is parameterized in order to provide to the users a way to implement and compile an application using 
	 * a model to be registered (extending DataClayObject).
	 * 
	 * @param alias
	 *            alias of object to be requested
	 * @param <E> type of the object requested
	 * @return The object identified by the provided alias
	 */
	public static <E> E getByAliasExt(final String alias) {
		throw new UnsupportedOperationException("DataClayObject must be specialized");
	}

	/**
	 * This method can be used from registered methods to access objects by alias, this method
	 * is parameterized in order to provide to the users a way to implement and compile an application using 
	 * a model to be registered (extending DataClayObject).
	 *
	 * @param alias
	 *            alias of object to be requested
	 * @param safe
	 *            if true, checks that an object with the provided alias exists
	 * @param <E> type of the object requested
	 * @return The object identified by the provided alias
	 */
	public static <E> E getByAliasExt(final String alias, boolean safe) {
		throw new UnsupportedOperationException("DataClayObject must be specialized");
	}
	
	/**
	 * Deletes an alias of an object of a specific class
	 * 
	 * @param className
	 *            class name of the object
	 * @param alias
	 *            alias of the object
	 */
	public static void deleteAlias(final String className, final String alias) {
		if (DEBUG_ENABLED) {
			logger.debug("Deleting alias " + alias + " of object of class " + className);
		}
		getLib().deleteAlias(alias);
	}

	/**
	 * Method to be specialized
	 * 
	 * @param alias
	 *            alias to be removed from this object
	 */
	public static void deleteAlias(final String alias) {
		if (DEBUG_ENABLED) {
			logger.debug("Deleting alias " + alias);
		}
		getLib().deleteAlias(alias);
	}


	/**
	 * Delete object's alias
	 */
	public void deleteAlias() {
		if (DEBUG_ENABLED) {
			logger.debug("Deleting alias in backend: " + alias);
		}
		getLib().deleteAlias(this);
	}

	/**
	 * Set the object's id
	 *
	 * This method is labeled as unsafe as the user should be aware
	 * it is just possible to change ids of non-persistent objects.
	 *
	 * There no warranty that, once persisted, an object will keep
	 * the same id.
	 *
	 * @param newObjectID
	 *        The ObjectID to set
	 */
	public void setObjectIDUnsafe(final ObjectID newObjectID) {
		this.objectID = newObjectID;
	}

	/**
	 * Debug start of running method
	 * 
	 * @param opNameAndDesc
	 *            Operation name and descriptor
	 */
	public final void debugStart(final String opNameAndDesc) {
		logger.debug("[==Internal Exec==] Starting method " + opNameAndDesc + " for " + objectID);
	}

	/**
	 * Debug end of running method
	 * 
	 * @param opNameAndDesc
	 *            Operation name and descriptor
	 */
	public final void debugEnd(final String opNameAndDesc) {
		logger.debug("[==Internal Exec==] Finished method " + opNameAndDesc + " for " + objectID);
	}

	/**
	 * Set a new object ID (used for remove objects)
	 */
	public final void setNewObjectID() {
		this.objectID = new ObjectID();
	}

	/**
	 * Get DataService client library
	 * 
	 * @return DataService client library
	 */
	public static final DataClayRuntime getLib() {
		if (Configuration.mockTesting) {
			final DataClayRuntime mockLib = DataClayMockObject.getLib();
			if (mockLib == null) {
				// WARNING: this is intended to support client threads apps simulation
				// since in a real environment clientLib static field is not overriden
				// by servers lib.
				return clientRuntime;
			} else {
				return mockLib;
			}
		} else {
			return clientRuntime;
		}

	}

	/**
	 * Set runtime
	 * 
	 * @param theruntime
	 *            the runtime to set
	 */
	public static void setLib(final DataClayRuntime theruntime) {
		clientRuntime = theruntime;
		LOCAL = clientRuntime.getLocalBackend();
	}

	/**
	 * Get the ObjectID of the instance
	 * 
	 * @return The ObjectID
	 */
	@Override
	public final ObjectID getObjectID() {
		return objectID;
	}

	/**
	 * Indicates the object is persistent.
	 * 
	 * @return TRUE if it is persistent. FALSE otherwise.
	 */
	public final boolean isPersistent() {
		return this.isPersistent.get();
	}

	/**
	 * Set the DataClayObject:newisPersistent
	 * 
	 * @param newisPersistent
	 *            the isPersistent to set
	 */
	public final void setIsPersistent(final boolean newisPersistent) {
		this.isPersistent.set(newisPersistent);
	}

	// CHECKSTYLE:OFF

	/**
	 * Get metaclass ID
	 * 
	 * @return Class ID
	 */
	@Override
	public MetaClassID getMetaClassID() {
		final StubInfo stubInfo = getStubInfo();
		if (stubInfo == null) {
			return null;
		}
		return stubInfo.getClassID();
	}

	/**
	 * Get metaclass ID of the specified class name
	 * 
	 * @param className
	 *            name of the class
	 * @return ID of the class
	 */
	public static MetaClassID getMetaClassID(final String className) {
		return getStubInfoFromClass(className).getClassID();
	}

	/**
	 * Runs the method with ID provided and params specified
	 * 
	 * @param implID
	 *            ID of the method to invoke
	 * @param params
	 *            Parameters of the method
	 * @return Result of the method
	 */
	public Object run(final ImplementationID implID, final Object[] params) {
		throw new UnsupportedOperationException("DataClayObject must be specialized");
	}

	/**
	 * Synchronize
	 * @param implID
	 *            implementation ID to be invoked on federated object
	 * @param params
	 *            Parameters of the method
	 */
	public void synchronize(final ImplementationID implID, final Object[] params) {
		getLib().synchronize(this, params, implID);
	}

	/**
	 * Wraps fields for serialization
	 * 
	 * @param wrapFields
	 *            [out] Wrapped fields
	 */
	public void wrapFieldsSerialization(final List<DataClaySerializable> wrapFields) {
		throw new UnsupportedOperationException("DataClayObject must be specialized");
	}

	/**
	 * Wraps fields for deserialization
	 * 
	 * @param wrapFields
	 *            [out] Wrapped fields
	 */
	public void wrapFieldsDeserialization(final List<DataClaySerializable> wrapFields) {
		throw new UnsupportedOperationException("DataClayObject must be specialized");
	}

	/**
	 * Set fields
	 * 
	 * @param fieldsToSet
	 *            Fields to set, in order.
	 */
	public void setFieldsDeserialization(final Queue<Object> fieldsToSet) {
		throw new UnsupportedOperationException("DataClayObject must be specialized");
	}

	/**
	 * Wraps parameters for serialization
	 * 
	 * @param implID
	 *            ID of the method to invoke
	 * @param params
	 *            Parameters of the method
	 * @return Wrapped parameters
	 */
	public List<DataClaySerializable> wrapParameters(final ImplementationID implID, final Object[] params) {
		throw new UnsupportedOperationException("DataClayObject must be specialized");
	}

	/**
	 * Wraps return for serialization
	 * 
	 * @param implID
	 *            ID of the method to invoke
	 * @param ret
	 *            Return of the method
	 * @return Wrapped return
	 */
	public List<DataClaySerializable> wrapReturn(final ImplementationID implID, final Object ret) {
		throw new UnsupportedOperationException("DataClayObject must be specialized");
	}

	/**
	 * Unwraps parameters for deserialization
	 * 
	 * @param implID
	 *            ID of the method to invoke
	 * @param wrapParams
	 *            Wrapped Parameters of the method
	 */
	public void setWrappersParams(final ImplementationID implID, final SerializedParametersOrReturn wrapParams) {
		throw new UnsupportedOperationException("DataClayObject must be specialized");
	}

	/**
	 * Unwraps return for deserialization
	 * 
	 * @param implID
	 *            ID of the method to invoke
	 * @param wrapReturn
	 *            Wrapped return of the method
	 */
	public void setWrappersReturn(final ImplementationID implID, final SerializedParametersOrReturn wrapReturn) {
		throw new UnsupportedOperationException("DataClayObject must be specialized");
	}

	/**
	 * This method set all fields with values of fields of provided object.
	 * 
	 * @param otherObject
	 *            object with values of fields to set.
	 */
	public void setAll(final DataClayObject otherObject) {
		throw new UnsupportedOperationException("DataClayObject must be specialized");
	}

	/**
	 * Retrieves a copy of this object
	 * 
	 * @return a non-persistent copy of this object
	 */
	public DataClayObject dcClone() {
		return DataClayObject.getLib().getCopyOfObject(this.objectID, false);
	}

	/**
	 * Retrieves a copy of this object
	 * 
	 * @param recursive
	 *            whether to also copy its subobjects or not
	 * @return a non-persistent copy of this object
	 */
	public DataClayObject dcClone(final boolean recursive) {
		return DataClayObject.getLib().getCopyOfObject(this.objectID, recursive);
	}

	/**
	 * Retrieves a copy of this object (but not the subobjects)
	 * 
	 * @param alias
	 *            alias of the object to be retrieved
	 * @return a non-persistent copy of this object
	 */
	public static DataClayObject dcCloneByAlias(final String alias) {
		final DataClayObject byAlias = DataClayObject.getLib().getObjectByAlias(alias);
		return byAlias.dcClone();
	}

	/**
	 * Retrieves a copy of this object
	 * 
	 * @param alias
	 *            alias of the object to be retrieved
	 * @param recursive
	 *            whether to also copy its subobjects or not
	 * @return a non-persistent copy of this object
	 */
	public static DataClayObject dcCloneByAlias(final String alias, final boolean recursive) {
		final DataClayObject byAlias = DataClayObject.getLib().getObjectByAlias(alias);
		return byAlias.dcClone(recursive);
	}

	/**
	 * Updates this object alias with the values in argument object
	 * 
	 * @param fromObject
	 *            object to be copied to this object
	 */
	public final void dcUpdate(final DataClayObject fromObject) {
		if (fromObject == null) {
			return;
		}
		DataClayObject.getLib().updateObject(this.getObjectID(), fromObject);
	}

	/**
	 * Updates the object identified by the given alias with the values in argument object
	 *
	 * @param alias
	 *            alias of the object to be updated
	 * @param fromObject
	 *            object to be copied to this object
	 */
	public static final void dcUpdateByAlias(final String alias, final DataClayObject fromObject) {
		final DataClayObject byAlias = DataClayObject.getLib().getObjectByAlias(alias);
		byAlias.dcUpdate(fromObject);
	}

	/**
	 * Equivalent to {@link #makePersistent(String) makePersistent}
	 * 
	 * @param alias
	 *            alias for the object
	 */
	public final void dcPut(final String alias) {
		this.makePersistent(alias);
	}

	/**
	 * Equivalent to {@link #makePersistent(String, BackendID) makePersistent}
	 * 
	 * @param alias
	 *            alias for the object
	 * @param optBackendID
	 *            optional backend where to persist this object
	 */
	public final void dcPut(final String alias, final BackendID optBackendID) {
		this.makePersistent(alias, optBackendID);
	}

	/**
	 * Equivalent to {@link #makePersistent(String, boolean) makePersistent}
	 * 
	 * @param alias
	 *            alias for the object
	 * @param recursive
	 *            whether subobjects are also persisted or not
	 */
	public final void dcPut(final String alias, final boolean recursive) {
		this.makePersistent(alias, recursive);
	}

	/**
	 * Equivalent to {@link #makePersistent(String, BackendID, boolean) makePersistent}
	 * 
	 * @param alias
	 *            alias for the object
	 * @param optBackendID
	 *            optional backend where to persist this object
	 * @param recursive
	 *            whether subobjects are also persisted or not
	 */
	public final void dcPut(final String alias, final BackendID optBackendID, final boolean recursive) {
		this.makePersistent(alias, optBackendID, recursive);
	}

	@Override
	public String getID() {
		if (isPersistent.get()) {
			return DataClay.ids2String(this.objectID, this.hint, this.getMetaClassID());
		} else {
			return null;
		}
	}

	@Override
	public final void makePersistent() {
		final BackendID backendID = DataClayObject.getLib().makePersistent(this, null, true, null);
		if (masterLocation == null) {
			masterLocation = backendID;
		}
	}

	@Override
	public final void makePersistent(final BackendID optBackendID) {
		final BackendID backendID = DataClayObject.getLib().makePersistent(this, optBackendID, true, null);
		if (masterLocation == null) {
			masterLocation = backendID;
		}
	}

	@Override
	public final void makePersistent(final String alias) {
		if (alias != null && alias.isEmpty()) {
			throw new IllegalArgumentException("Alias cannot be empty");
		}
		final BackendID backendID = DataClayObject.getLib().makePersistent(this, null, true, alias);
		if (masterLocation == null) {
			masterLocation = backendID;
		}
	}

	/**
	 * Store this object into DataClay.
	 * 
	 * @param recursive
	 *            Indicates if all referenced objects from this objects that are not already persistent must also be stored. If
	 *            true, all referenced objects are also stored. If the object is already persistent i.e. contains a DataClay
	 *            objectID this function will fail.
	 */
	@Override
	public final void makePersistent(final boolean recursive) {
		final BackendID backendID = DataClayObject.getLib().makePersistent(this, null, recursive, null);
		if (masterLocation == null) {
			masterLocation = backendID;
		}
	}

	/**
	 * Store this object into DataClay.
	 * 
	 * @param recursive
	 *            Indicates if all referenced objects from this objects that are not already persistent must also be stored. If
	 *            true, all referenced objects are also stored. If the object is already persistent i.e. contains a DataClay
	 *            objectID this function will fail.
	 * @param optionalBackendID
	 *            ID of the backend in which the object must be stored. If null, any backend is accepted.
	 */
	@Override
	public final void makePersistent(final boolean recursive, final BackendID optionalBackendID) {
		final BackendID backendID = DataClayObject.getLib().makePersistent(this, optionalBackendID, recursive, null);
		if (masterLocation == null) {
			masterLocation = backendID;
		}
	}

	/**
	 * Store this object into DataClay.
	 * 
	 * @param alias
	 *            alias for the object
	 * @param destBackendID
	 *            ID of the backend in which the object must be stored. If null, any backend is accepted.
	 */
	@Override
	public final void makePersistent(final String alias, final BackendID destBackendID) {
		if (alias != null && alias.isEmpty()) {
			throw new IllegalArgumentException("Alias cannot be empty");
		}
		final BackendID backendID = DataClayObject.getLib().makePersistent(this, destBackendID, true, alias);
		if (masterLocation == null) {
			masterLocation = backendID;
		}
	}

	/**
	 * Store this object into DataClay.
	 * 
	 * @param alias
	 *            alias for the object
	 * @param recursive
	 *            Indicates if all referenced objects from this objects that are not already persistent must also be stored. If
	 *            true, all referenced objects are also stored. If the object is already persistent i.e. contains a DataClay
	 *            objectID this function will fail.
	 */
	@Override
	public final void makePersistent(final String alias, final boolean recursive) {
		if (alias != null && alias.isEmpty()) {
			throw new IllegalArgumentException("Alias cannot be empty");
		}
		final BackendID backendID = DataClayObject.getLib().makePersistent(this, null, recursive, alias);
		if (masterLocation == null) {
			masterLocation = backendID;
		}
	}

	/**
	 * Store this object into DataClay.
	 * 
	 * @param alias
	 *            alias for the object
	 * @param destBackendID
	 *            ID of the backend in which the object must be stored. If null, any backend is accepted.
	 * @param recursive
	 *            Indicates if all referenced objects from this objects that are not already persistent must also be stored. If
	 *            true, all referenced objects are also stored. If the object is already persistent i.e. contains a DataClay
	 *            objectID this function will fail.
	 */
	@Override
	public final void makePersistent(final String alias, final BackendID destBackendID, final boolean recursive) {
		if (alias != null && alias.isEmpty()) {
			throw new IllegalArgumentException("Alias cannot be empty");
		}
		final BackendID backendID = DataClayObject.getLib().makePersistent(this, destBackendID, recursive, alias);
		if (masterLocation == null) {
			masterLocation = backendID;
		}
	}

	/**
	 * Creates a new replica of this persistent object and its subobjects in a certain backend.
	 *
	 * @return The ID of the backend in which the replica was created.
	 */
	@Override
	public final BackendID newReplica() {
		return DataClayObject.getLib().newReplica(this.objectID, this.hint, null,
				null,true);
	}

	/**
	 * Creates a new replica of this persistent object in a certain backend.
	 *
	 * @param recursive
	 *            Indicates if all sub-objects must be replicated as well.
	 * @return The ID of the backend in which the replica was created.
	 */
	@Override
	public final BackendID newReplica(final boolean recursive) {
		return DataClayObject.getLib().newReplica(this.objectID, this.hint,
				null, null, recursive);
	}

	/**
	 * Creates a new replica of this persistent object.
	 * 
	 * @param optionalBackendID
	 *            ID of the backend in which to create the replica. If null, any backend is accepted. If the object is not
	 *            persistent i.e. does not contain a DataClay objectID this function will fail.
	 * @return The ID of the backend in which the replica was created.
	 */
	@Override
	public final BackendID newReplica(final BackendID optionalBackendID) {
		return DataClayObject.getLib().newReplica(this.objectID, this.hint,
				optionalBackendID, null, true);
	}

	/**
	 * Create a new replica of this persistent object.
	 * 
	 * @param optionalBackendID
	 *            ID of the backend in which to create the replica. If null, any backend is accepted. If the object is not
	 *            persistent i.e. does not contain a DataClay objectID this function will fail.
	 * @param recursive
	 *            Indicates if all sub-objects must be replicated as well.
	 * @return The ID of the backend in which the replica was created.
	 */
	@Override
	public final BackendID newReplica(final BackendID optionalBackendID, final boolean recursive) {
		return DataClayObject.getLib().newReplica(this.objectID, this.hint,
				optionalBackendID,
				null, recursive);
	}

	/**
	 * Moves a persistent object and referenced objects from the source location to the destination location specified. If the
	 * object is not persistent i.e. does not contain a DataClay objectID this function will fail.
	 * 
	 * @param srcLocID
	 *            of the source location in which the object is stored.
	 * @param destLocID
	 *            of the destination location in which the object should be moved.
	 */
	@Override
	public final void moveObject(final BackendID srcLocID, final BackendID destLocID) {
		DataClayObject.getLib().moveObject(this.objectID, this.getMetaClassID(), this.hint, srcLocID, destLocID, true);
		if (masterLocation != null && srcLocID.equals(masterLocation)) {
			masterLocation = destLocID;
		}
	}

	/**
	 * Moves a persistent object from the source location to the destination location specified. If the object is not persistent
	 * i.e. does not contain a DataClay objectID this function will fail.
	 * 
	 * @param srcLocID
	 *            of the source location in which the object is stored.
	 * @param destLocID
	 *            of the destination location in which the object should be moved.
	 * @param recursive
	 *            Indicates if all sub-objects must be moved as well.
	 */
	@Override
	public final void moveObject(final BackendID srcLocID, final BackendID destLocID, final boolean recursive) {
		DataClayObject.getLib().moveObject(this.objectID, this.getMetaClassID(), this.hint, srcLocID, destLocID,
				recursive);
		if (masterLocation != null && srcLocID.equals(masterLocation)) {
			masterLocation = destLocID;
		}
	}

	/**
	 * Sets this persistent object to be read only. If the object is not persistent i.e. does not contain a DataClay objectID
	 * this function will fail.
	 */
	@Override
	public final void setObjectReadOnly() {
		// Make sure object is registered
		DataClayObject.getLib().setObjectReadOnly(this.objectID, this.getMetaClassID(), this.hint);
	}

	/**
	 * Sets this persistent object to be read write. If the object is not persistent i.e. does not contain a DataClay objectID
	 * this function will fail.
	 */
	@Override
	public final void setObjectReadWrite() {
		DataClayObject.getLib().setObjectReadWrite(this.objectID, this.getMetaClassID(), this.hint);
	}

	/**
	 * Gets the location of this persistent object.
	 * 
	 * @return ID of the backend in which the object is stored. If the object is not persistent i.e. does not contain a DataClay
	 *         objectID this function will fail.
	 */
	@Override
	public final BackendID getLocation() {
		return DataClayObject.getLib().getLocation(this.objectID);
	}

	/**
	 * Gets the location of this persistent object and its replicas.
	 * 
	 * @return A set of IDs of the backend in which this object or its replicas are stored. If the object is not persistent i.e.
	 *         does not contain a DataClay objectID this function will fail.
	 */
	@Override
	public final Set<BackendID> getAllLocations() {
		return new HashSet<>(DataClayObject.getLib().getAllLocations(this.objectID));
	}

	/**
	 * Federates this object with an external dataClay instance
	 * 
	 * @param extDataClayID
	 *            id of the external dataClay instance
	 * @param recursive
	 *            whether to federate recursively or not
	 */
	public void federate(final DataClayInstanceID extDataClayID, final boolean recursive) {
		DataClayObject.getLib().federateObject(this, extDataClayID, recursive);
	}
	
	/**
	 * Unfederate this object with the provided external dataClay
	 * @param extDataClayID  id of the external dataClay instance
	 * @param recursive
	 *            whether to unfederate recursively or not
	 */
	public void unfederate(final DataClayInstanceID extDataClayID, final boolean recursive) {
		//FIXME: specific unfederation
		DataClayObject.getLib().unfederateObject(this, null, recursive);
	}

	/**
	 * Unfederate this object with all external dataClays is registered with
	 * @param recursive
	 *            whether to unfederate recursively or not
	 */
	public void unfederate(final boolean recursive) { 
		DataClayObject.getLib().unfederateObject(this, null, recursive);
	}
	
	/**
	 * Federates this object with an external dataClay instance
	 * 
	 * @param extDataClayID
	 *            id of the external dataClay instance
	 */
	public void federate(final DataClayInstanceID extDataClayID) {
		DataClayObject.getLib().federateObject(this, extDataClayID, true);
	}


	/**
	 * Unfederate this object with the provided external dataClay
	 * @param extDataClayID  id of the external dataClay instance
	 */
	public void unfederate(final DataClayInstanceID extDataClayID) {
		//FIXME: specific unfederation
		DataClayObject.getLib().unfederateObject(this, null, true);
	}

	/**
	 * Federates this object with an external backend instance
	 *
	 * @param extBackendID
	 *            id of the external backend to federate
	 */
	public void federateToBackend(final BackendID extBackendID) {
		DataClayObject.getLib().federateToBackend(this,
				(ExecutionEnvironmentID) extBackendID, true);
	}

	/**
	 * Federates this object with an external backend instance
	 *
	 * @param extBackendID
	 *            id of the external backend to federate
	 * @param recursive Indicates if federation is recursive or not
	 */
	public void federateToBackend(final BackendID extBackendID, final boolean recursive) {
		DataClayObject.getLib().federateToBackend(this,
				(ExecutionEnvironmentID) extBackendID, recursive);
	}

	/**
	 * Unfederates this object with an external backend instance
	 *
	 * @param extBackendID
	 *            id of the external backend to federate
	 */
	public void unfederateFromBackend(final BackendID extBackendID) {
		DataClayObject.getLib().unfederateFromBackend(this,
				(ExecutionEnvironmentID) extBackendID, true);
	}


	/**
	 * Unfederates this object with an external backend instance
	 *
	 * @param extBackendID
	 *            id of the external backend to federate
	 * @param recursive Indicates if unfederation is recursive or not

	 */
	public void unfederateFromBackend(final BackendID extBackendID, final boolean recursive) {
		DataClayObject.getLib().unfederateFromBackend(this,
				(ExecutionEnvironmentID) extBackendID, recursive);
	}


	/**
	 * Retrieve dataClay ID
	 * @param hostname Name of external dataclay 
	 * @param port Port of external dataclay
	 * @return ID of external dataclay
	 */
	public final DataClayInstanceID getExternalDataClayID(final String hostname, final int port) {
		return getLib().getExternalDataClayID(hostname, port);
	}


	/**
	 * Detach object from session, i.e. remove reference from current session provided to current object,
	 * "dear garbage-collector, the current session is not using this object anymore"
	 *
	 */
	public final void sessionDetach() {
		getLib().detachObjectFromSession(this.getObjectID(), (ExecutionEnvironmentID) this.getHint());
	}

	/**
	 * Override this function with code to be executed when a federated object arrives into a destination dataClay.
	 */
	public void whenFederated() {
		if (DEBUG_ENABLED) {
			logger.debug(
					"[==Federation==] Class " + this.getClass().getName() + " has no whenFederated behaviour defined.");
		}

	}

	/**
	 * Override this function with code to be executed when a federate object is unfederated in destination dataClay.
	 */
	public void whenUnfederated() {
		if (DEBUG_ENABLED) {
			logger.debug(
					"[==Federation==] Class " + this.getClass().getName() + " has no whenUnfederated behaviour defined.");
		}
	}

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps, final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, final ReferenceCounting referenceCounting) {

		// === reference counting information ===
		// First integer represent the position in the buffer in which reference
		// counting starts
		// this is done to avoid "holding" unnecessary information during a store or
		// update in disk.
		// in new serialization, this will be done through padding
		// TODO: use padding instead once new serialization is implemented (dgasull
		// pierlauro 2018)
		dcBuffer.writeInt(0); // index is updated in the end
		
		// === master location ==
		if (masterLocation != null) {
			dcBuffer.writeLong(masterLocation.getId().getLeastSignificantBits());
			dcBuffer.writeLong(masterLocation.getId().getMostSignificantBits());
		} else {
			dcBuffer.writeLong(0L);
			dcBuffer.writeLong(0L);
		}

		final List<DataClaySerializable> wrapFields = new ArrayList<>();
		this.wrapFieldsSerialization(wrapFields);

		// Calculate nulls bit set size
		final int numBytes = (int) Math.ceil(wrapFields.size() / 8.0F);
		final BitSet notNullsBitSet = new BitSet(numBytes);
		dcBuffer.writeVLQInt(numBytes);
		
		final int curWriterIndx = dcBuffer.writerIndex();
		dcBuffer.writeBytes(new byte[numBytes]);
		BitSet ifaceBitSet = null;
		if (ifaceBitMaps != null) {
			ifaceBitSet = BitSet.valueOf(ifaceBitMaps.get(getMetaClassID()));
		}

		for (int i = 0; i < wrapFields.size(); ++i) {
			if (ifaceBitSet == null || ifaceBitSet.get(i)) {
				final DataClaySerializable wrappedField = wrapFields.get(i);
				if (wrappedField == null) {
					continue;
				}

				if (wrappedField instanceof DataClayJavaWrapper) {
					// ====== JAVA/IMMUTABLE/GENERIC FIELD ===== //
					final DataClayJavaWrapper javaWrapper = (DataClayJavaWrapper) wrappedField;
					if (javaWrapper.isNull()) {
						continue;
					}
					notNullsBitSet.set(i);
					DataClaySerializationLib.serializeJavaField(javaWrapper, dcBuffer, ignoreUserTypes, ifaceBitMaps,
							curSerializedObjs, pendingObjs, referenceCounting);

				} else {
					if (ignoreUserTypes) {
						continue;
					}
					final DataClayObject dcObject = (DataClayObject) wrappedField;
					// ====== DCOBJECT FIELD ===== //
					notNullsBitSet.set(i);
					DataClaySerializationLib.serializeAssociation(dcObject, dcBuffer, ignoreUserTypes, ifaceBitMaps,
							curSerializedObjs, pendingObjs, referenceCounting);
				}
			}
		}

		int curIdx = dcBuffer.writerIndex();
		dcBuffer.setWriterIndex(curWriterIndx);
		dcBuffer.writeBytes(notNullsBitSet.toByteArray());
		dcBuffer.setWriterIndex(curIdx);
		// == reference counting == //
		// TODO: IMPORTANT: this should be removed in new serialization by using
		// paddings to directly access reference counters inside metadata.

		curIdx = dcBuffer.writerIndex();
		dcBuffer.setWriterIndex(0);
		dcBuffer.writeInt(curIdx);
		dcBuffer.setWriterIndex(curIdx);
		DataClaySerializationLib.serializeReferenceCounting(this, dcBuffer, referenceCounting);

	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata, final Map<Integer, Object> curDeserializedJavaObjs) {

		// == reference counting == //
		// see serialize function to understand why we "ignore" first 4 bytes
		dcBuffer.readInt();
		if (DataClayDeserializationLib.DEBUG_ENABLED) { 
			DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Ignored 4 first bytes deserialized: readerindex=" + dcBuffer.readerIndex());
		}
		// === master location ==
		final long masterLocationLeast = dcBuffer.readLong();
		if (DataClayDeserializationLib.DEBUG_ENABLED) { 
			DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Master location least bytes deserialized: data="+  masterLocationLeast + ", readerindex=" + dcBuffer.readerIndex());
		}
		final long masterLocationMost = dcBuffer.readLong();
		if (DataClayDeserializationLib.DEBUG_ENABLED) { 
			DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Master location most bytes deserialized: data="+  masterLocationMost + ", readerindex=" + dcBuffer.readerIndex());
		}
		if (masterLocationLeast == 0L && masterLocationMost == 0L) {
			masterLocation = null;
		} else {
			final UUID uuid = new UUID(masterLocationMost, masterLocationLeast);
			masterLocation = new ExecutionEnvironmentID(uuid);
		}

		final List<DataClaySerializable> wrapFields = new ArrayList<>();
		this.wrapFieldsDeserialization(wrapFields);

		BitSet notNullsBitSet = null;
		BitSet ifaceBitSet = null;
		final int notNullsBitSetLength = dcBuffer.readVLQInt();
		if (DataClayDeserializationLib.DEBUG_ENABLED) { 
			DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Bitmap size deserialized: data="+  notNullsBitSetLength + ", readerindex=" + dcBuffer.readerIndex());
		}
		if (notNullsBitSetLength > 0) {
			notNullsBitSet = BitSet.valueOf(dcBuffer.readBytes(notNullsBitSetLength));
			if (DataClayDeserializationLib.DEBUG_ENABLED) { 
				DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Bitmap deserialized: data="+  notNullsBitSet + ", readerindex=" + dcBuffer.readerIndex());
			}
		}
		if (ifaceBitMaps != null) {
			ifaceBitSet = BitSet.valueOf(ifaceBitMaps.get(getMetaClassID()));
		}

		int curFieldIdx = 0;
		final Queue<Object> queueFields = new LinkedList<>();
		for (final DataClaySerializable wrapper : wrapFields) {

			final boolean isInIface = ifaceBitSet == null || ifaceBitSet.get(curFieldIdx);
			final boolean isNotNull = notNullsBitSet == null || notNullsBitSet.get(curFieldIdx);
			if (isInIface && isNotNull) {
				if (wrapper == null) {
					// ====== DCOBJECT FIELD ===== //
					// Means association, no wrappers
					final DataClayObject obj = DataClayDeserializationLib.deserializeAssociation(dcBuffer, ifaceBitMaps,
							metadata, curDeserializedJavaObjs, getLib());
					queueFields.add(obj);
				} else {
					// ====== JAVA/IMMUTABLE/PRIMITIVE FIELD ===== //
					final DataClayJavaWrapper javaWrapper = (DataClayJavaWrapper) wrapper;
					final Object javaObj = DataClayDeserializationLib.deserializeJavaField(javaWrapper, dcBuffer,
							ifaceBitMaps, metadata, curDeserializedJavaObjs);
					queueFields.add(javaObj);
				}
			} else if (isInIface && !isNotNull) {
				queueFields.add(null);
			}

			curFieldIdx++;
		}

		// Set fields
		this.setFieldsDeserialization(queueFields);

		// == ignore rest of bytes (like reference counting) == //
		// do not clear buffer since this can be an embedded object (maybe?).

	}

	/*
	 * @Override protected void finalize() throws Throwable { if (this.isServerObject) { this.ds.gcCollectObject(this); } }
	 */

	/**
	 * Get the DataClayObject::hint
	 * 
	 * @return the hint
	 */
	public BackendID getHint() {
		return hint;
	}

	/**
	 * Set the DataClayObject::hint
	 * 
	 * @param newhint
	 *            the hint to set
	 */
	public void setHint(final BackendID newhint) {
		this.hint = newhint;
	}

	/**
	 * Clear exec stub infos cache for enrichments.
	 */
	public static final void clearExecStubInfosCache() {
		execStubInfosCache.clear();
	}

	/**
	 * Clear stub infos cache for enrichments.
	 */
	public static final void clearStubInfosCache() {
		stubInfosCache.clear();
	}

	/**
	 * Get stub information of class
	 * 
	 * @return Stub information
	 */
	public final StubInfo getStubInfo() {
		return getStubInfoFromClass(this.getClass().getName());
	}

	/**
	 * Checks if class provided is stub or not.
	 * 
	 * @param clazz
	 *            Class to check.
	 * @return True if it is an stub. False otherwise.
	 */
	public static final boolean isStub(final Class<?> clazz) {
		try {
			clazz.getField(ByteCodeFieldNames.IS_STUB_FIELDNAME);
			return true;
		} catch (final NoSuchFieldException e2) {
			return false;
		}
	}

	/**
	 * Get stubinfo for class with name provided
	 * 
	 * @param className
	 *            Name of the class
	 * @return Stub information.
	 */
	public static final StubInfo getStubInfoFromClass(final String className) {
		StubInfo stubInfo = null;
		if (getLib().isDSLib()) {
			stubInfo = execStubInfosCache.get(className);
		} else {
			stubInfo = stubInfosCache.get(className);
		}
		if (stubInfo == null) {
			try {
				Class<?> currentClass = null;
				if (getLib().isDSLib()) {
					currentClass = DataClayClassLoaderSrv.getClass(className);
				} else {
					currentClass = DataClayClassLoader.getClass(className);
				}
				final Yaml yaml = CommonYAML.getYamlObject();
				final String path = currentClass.getSimpleName() + "Yaml.yaml";
				logger.debug("Loading babel stub information from " + path);
				final InputStream ios = currentClass.getResourceAsStream(path);
				stubInfo = (StubInfo) yaml.load(ios);
				if (getLib().isDSLib()) {
					execStubInfosCache.put(className, stubInfo);
				} else {
					stubInfosCache.put(className, stubInfo);
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return stubInfo;
	}

	/**
	 * Execute remote method
	 * 
	 * @param methodSignature
	 *            Signature of the method
	 * @param implIDAsStr
	 *            ImplementationID as string
	 * @param params
	 *            Parameters to send
	 * @return Return value.
	 */
	@Override
	public Object executeRemoteImplementation(final String methodSignature, final String implIDAsStr,
			final Object[] params) {
		return getLib().executeRemoteImplementation(this, implIDAsStr, params);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DataClayObject)) {
			return false;
		}
		return this.objectID.equals(((DataClayObject) obj).getObjectID());

	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.objectID);
	}

	/**
	 * Get DataSetID
	 * 
	 * @return DataSetID
	 */
	public DataSetID getDataSetID() {
		return dataSetID;
	}

	/**
	 * Set object DataSetID
	 * 
	 * @param newDataSetID
	 *            ID of DataSet this object belongs to
	 */
	public void setDataSetID(final DataSetID newDataSetID) {
		this.dataSetID = newDataSetID;
		if (this.isPersistent()) {
			getLib().setDataSetID(this.objectID, newDataSetID);
		}
	}

	/**
	 * @return the masterLocation
	 */
	public final BackendID getMasterLocation() {
		return masterLocation;
	}

	/**
	 * If called from client side will not have any effect.
	 * 
	 * @param newMasterLocation
	 *            the masterLocation to set
	 */
	public void setMasterLocation(final BackendID newMasterLocation) {
		this.masterLocation = newMasterLocation;
	}

	/**
	 * Get alias
	 * @return the alias of the object
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * Set the alias of the object
	 * @param alias the alias of the object
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * Get if object is read only
	 * @return boolean indicating if object is read only or not
	 */
	public boolean isReadOnly() {
		return isReadOnly;
	}

	/**
	 * Set if object is read only
	 * @param readOnly boolean indicating if object is read only or not
	 */
	public void setReadOnly(boolean readOnly) {
		this.isReadOnly = readOnly;
	}


	/**
	 * @return the original object id in case of new version
	 */
	public final ObjectID getOriginalObjectID() {
		return this.originalObjectID;
	}

	/**
	 *
	 * @param newOriginalObjectID
	 *            the original object id  to set
	 */
	public void setOriginalObjectID(final ObjectID newOriginalObjectID) {
		this.originalObjectID = newOriginalObjectID;
	}

	/**
	 *
	 * @return root location of the object or null if current is original
	 */
	public ExecutionEnvironmentID getRootLocation() {
		return rootLocation;
	}

	/**
	 * Set root location of the object
	 * @param rootLocation root location to set
	 */
	public void setRootLocation(ExecutionEnvironmentID rootLocation) {
		this.rootLocation = rootLocation;
	}

	/**
	 *
	 * @return origin location of the object or null if current is original
	 */
	public ExecutionEnvironmentID getOriginLocation() {
		return originLocation;
	}

	/**
	 * Set origin location of the object
	 * @param originLocation origin location to set
	 */
	public void setOriginLocation(ExecutionEnvironmentID originLocation) {
		this.originLocation = originLocation;
	}

	/**
	 * Get all replica locations
	 * @return Replica locations
	 */
	public Set<ExecutionEnvironmentID> getReplicaLocations() {
		return replicaLocations;
	}

	/**
	 * Set replica locations
	 * @param replicaLocations replica locations to set
	 */
	public void setReplicaLocations(Set<ExecutionEnvironmentID> replicaLocations) {
		this.replicaLocations = replicaLocations;
	}

	/**
	 * Add replica location
	 * @param replicaLocation replica location to add
	 */
	public void addReplicaLocations(ExecutionEnvironmentID replicaLocation) {
		if (this.replicaLocations == null) {
			this.replicaLocations = ConcurrentHashMap.newKeySet();
		}
		this.replicaLocations.add(replicaLocation);
	}

	/**
	 * Remove replica location
	 * @param replicaLocation replica location to add
	 */
	public void removeReplicaLocation(ExecutionEnvironmentID replicaLocation) {
		this.replicaLocations.remove(replicaLocation);
	}

	/**
	 * Clear replica locations
	 */
	public void clearReplicaLocations() {
		this.replicaLocations.clear();
	}

	/**
	 * @return true if masterlocation is the current backend, false otherwise
	 */
	public final boolean isMasterLocation() {
		if (masterLocation != null) {
			return masterLocation.equals(DataClayObject.getLib().getHint());
		}
		return false;
	}

	/**
	 * @return id of the external dataClay instance where the object resides. null if it is local.
	 */
	public DataClayInstanceID getExternalDataClayID() {
		return externalDataClayID;
	}

	/**
	 * @param newID
	 *            id of the external dataClay instance
	 */
	public void setExternalDataClayID(final DataClayInstanceID newID) {
		this.externalDataClayID = newID;
	}
	
	/**
	 * RT PREFETCHING FIELDS
	 */
	
	public boolean isPrefetchingAccess() {
		return isPrefetchingAccess;
	}
	
	public static void setPrefetchingAccess(final boolean value) {
		isPrefetchingAccess = value;
	}
	
	public static long getAccessCount() {
		return accessCount;
	}
	
	public static long getHitCount() {
		return hitCount;
	}
	
	protected void incrementAccessCount() {
		accessCount++;
	}

	protected void incrementHitCount() {
		hitCount++;
	}
	
	public static void resetObjectAccessStats() {
		accessCount = 0;
		hitCount = 0;
	}

	public static void printObjectAccessStats() {
		logger.debug("[==PerfetchingInfo==] Object Hit Ratio: "
				+ (double)hitCount / (double)accessCount
				+ "(" + hitCount + "/" + accessCount + ")");
	}
}
