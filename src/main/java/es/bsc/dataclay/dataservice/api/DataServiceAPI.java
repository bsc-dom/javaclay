
/**
 * @file DataServiceAPI.java
 * @date Oct 8, 2012
 */
package es.bsc.dataclay.dataservice.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import es.bsc.dataclay.serialization.lib.ObjectWithDataParamOrReturn;
import es.bsc.dataclay.serialization.lib.SerializedParametersOrReturn;
import es.bsc.dataclay.util.CommonManager;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.SessionID;
import es.bsc.dataclay.util.ids.StorageLocationID;
import es.bsc.dataclay.util.info.VersionInfo;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.metadataservice.MetaDataInfo;
import es.bsc.dataclay.util.management.metadataservice.RegistrationInfo;
import es.bsc.dataclay.util.management.metadataservice.StorageLocation;
import es.bsc.dataclay.util.structs.Tuple;

/**
 * This interface define the methods of the Data Service that can be executed remotely.
 */
public interface DataServiceAPI extends CommonManager {

	/**
	 * Initializes the backend ID of this DataService
	 * 
	 * @param backendID
	 *            ID of the backend
	 */
	void initBackendID(final StorageLocationID backendID);

	/**
	 * Associate execution environment to this DS.
	 * 
	 * @param executionEnvironmentID
	 *            ID to associate
	 */
	void associateExecutionEnvironment(final ExecutionEnvironmentID executionEnvironmentID);

	/**
	 * Deploys a series of MetaClass containers to a given namespace.
	 * 
	 * @param namespaceName
	 *            Name of the namespace classes belongs to.
	 * @param deploymentPack
	 *            A map containing name of the class and the MetaClass container.
	 */
	void deployMetaClasses(final String namespaceName, final Map<String, MetaClass> deploymentPack);

	/**
	 * Deploys the given class in the path corresponding to the given namespace.
	 * 
	 * @param namespaceName
	 *            Name of the namespace classes belongs to.
	 * @param classesToDeploy
	 *            bytecode of the classes to be deployed.
	 * @param classesAspects
	 *            bytes of the aspects of this class
	 * @param stubYamls
	 *            Stub yamls.
	 */
	void deployClasses(final String namespaceName, final Map<Tuple<String, MetaClassID>, byte[]> classesToDeploy,
			final Map<String, byte[]> classesAspects, final Map<String, byte[]> stubYamls);

	/**
	 * Enriches the given class in the path corresponding to the given namespace.
	 * 
	 * @param namespaceName
	 *            Name of the namespace which class belongs to.
	 * @param className
	 *            Name of the class to deploy
	 * @param classToDeploy
	 *            bytecode of the class to be deployed.
	 * @param classAspects
	 *            bytes of the aspects of this class
	 * @param stubYaml
	 *            Stub yaml
	 */
	void enrichClass(final String namespaceName, final String className, final byte[] classToDeploy,
			final byte[] classAspects, byte[] stubYaml);

	/**
	 * Create an instance of the class with id provided using constructor with id and params specified and store it.
	 * 
	 * @param sessionID
	 *            Session ID to use
	 * @param classID
	 *            Id of the class to use
	 * @param implementationID
	 *            ID of the implementation of the constructor
	 * @param ifaceBitMaps
	 *            Interface bitmaps (for parameters)
	 * @param params
	 *            Parameters
	 * @return ObjectID of the persisted instance.
	 */
	ObjectID newPersistentInstance(final SessionID sessionID, final MetaClassID classID,
			final ImplementationID implementationID, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final SerializedParametersOrReturn params);

	/**
	 * Store objects into Database.
	 * 
	 * @param sessionID
	 *            SessionID sessionID
	 * @param objects
	 *            Serialized objects to store (still volatile parameters)
	 * @param moving
	 *            If true, indicates that objects are being moved from another DS.
	 * @param idsWithAlias
	 *            Objects with alias
	 */
	void storeObjects(final SessionID sessionID, final List<ObjectWithDataParamOrReturn> objects, final boolean moving,
			final Set<ObjectID> idsWithAlias);


	/**
	 * Retrieves the given object and all subobjects as volatile new objects with new OIDs
	 * 
	 * @param sessionID
	 *            ID of the current session
	 * @param objectID
	 *            ID of the object to retrieve
	 * @param recursive
	 *            retrieve a copy of the whole object copying also its subobjects or only the main object
	 * @return a copy of the object
	 * 
	 */
	SerializedParametersOrReturn getCopyOfObject(final SessionID sessionID, final ObjectID objectID,
			final boolean recursive);

	/**
	 * Method that updates an object (into) with contents obtained from object (from)
	 * 
	 * @param sessionID
	 *            id of the session requesting this method
	 * @param intoObjectID
	 *            id of the object to put contents into
	 * @param fromObject
	 *            object to get contents from
	 */
	void updateObject(final SessionID sessionID, final ObjectID intoObjectID,
			final SerializedParametersOrReturn fromObject);

	/**
	 * Get the serialized objects with id provided
	 * 
	 * @param sessionID
	 *            ID of session
	 * @param objectIDs
	 *            IDs of the objects to get
	 * @param recursive
	 *            Indicates if, per each object to get, also obtain its associated objects.
	 * @param removeHints
	 *            Indicates hint must be removed because we getting object for a move/federation...
	 * @param getOnlyRefs Indicates object must not be serialized. Only the object ID is needed (the reference)
	 * @return Map of serialized object where key is the objectID. Object is not serialized if flag getOnlyRefs=true
	 */
	Map<ObjectID, ObjectWithDataParamOrReturn> getObjects(final SessionID sessionID, final Set<ObjectID> objectIDs,
			final boolean recursive, final boolean removeHints, final boolean getOnlyRefs);


	/**
	 * Retrieve the elements contained in the given object that match with specified conditions.
	 * 
	 * @param sessionID
	 *            id of the session
	 * @param objectID
	 *            id of the iterable object
	 * @param conditions
	 *            conditions to be checked
	 * @return list of objects that match the specified conditions
	 */
	SerializedParametersOrReturn filterObject(final SessionID sessionID, final ObjectID objectID,
			final String conditions);


	/**
	 * This function will deserialize make persistent "parameters" (i.e. object to persist and subobjects if needed) into
	 * dataClay memory heap using the same design as for volatile parameters. Eventually, dataClay GC will collect them, and
	 * then they will be registered in LogicModule if needed (if objects were created with alias, they must have metadata
	 * already).
	 * 
	 * @param sessionID
	 *            ID of session of make persistent call
	 * @param objectsToPersist
	 *            objects to store.
	 */
	void makePersistent(final SessionID sessionID, final List<ObjectWithDataParamOrReturn> objectsToPersist);

	// ==================== FEDERATION ====================//

	/**
	 * New federated object arrives and must be stored in current backend.
	 * 
	 * @param sessionID
	 *            ID of the session sending the object
	 * @param objectsToPersist
	 *            Data of the object to persist
	 */
	void federate(final SessionID sessionID, final List<ObjectWithDataParamOrReturn> objectsToPersist);

	/**
	 * Unfederate objects with ID provided.
	 * 
	 * @param sessionID
	 *            ID of the session.
	 * @param objectIDs
	 *            ID of the objects to unfederate.
	 */
	void unfederate(final SessionID sessionID, final Set<ObjectID> objectIDs);

	/**
	 * This function executes a method.
	 * 
	 * @param objectID
	 *            ID of the object with the information to use by the implementation
	 * @param implID
	 *            Implementation ID of operation to execute
	 * @param params
	 *            Serialized parameter values used while invoking the operation
	 * @param sessionID
	 *            ID of the session of the execution
	 * @return Serialized operation result (all objects serialized, sepparately).
	 */
	SerializedParametersOrReturn executeImplementation(final ObjectID objectID, final ImplementationID implID,
			final SerializedParametersOrReturn params, final SessionID sessionID);

	/**
	 * This operation creates a new version of the object with ID provided in the backend specified
	 * 
	 * @param sessionID
	 *            Session
	 * @param objectID
	 *            ID of the object
	 * @param metadataInfo
	 *            Metadata of the object, including the backends where the root object to be versioned is located
	 * @return The OID of the version root and the mapping from version OID to original OID for each versioned object
	 */
	Tuple<ObjectID, Map<ObjectID, ObjectID>> newVersion(final SessionID sessionID, final ObjectID objectID,
			final MetaDataInfo metadataInfo);

	/**
	 * Consolidates all the objects in versionInfo, being the current data service the one containing all the versioned objects.
	 * For each versioned object, its OID is set to the original one according to the mapping in versionInfo, and the
	 * consolidated object is stored in the same locations as the original one (before versioning). The versions are deleted.
	 * 
	 * @param sessionID
	 *            ID of session
	 * @param versionInfo
	 *            info of the version
	 */
	void consolidateVersion(final SessionID sessionID, final VersionInfo versionInfo);

	/**
	 * Updates objects or insert if they do not exist with the values in objectBytes. NOTE: This function is recursive, it is
	 * going to other DSs if needed.
	 * 
	 * @param sessionID
	 *            ID of session needed.
	 * @param objectBytes
	 *            Map of objects to update.
	 */
	void upsertObjects(final SessionID sessionID, final List<ObjectWithDataParamOrReturn> objectBytes);

	/**
	 * This operation creates a new replica of the object with ID provided in the backend specified
	 * 
	 * @param sessionID
	 *            Session
	 * @param objectID
	 *            ID of the object
	 * @param destBackendID
	 * 			  ID of destination backend
	 * @param registerMetaData Indicates that registration of metadata of replica must be forced
	 * @param recursive
	 *            Indicates if all sub-objects must be replicated as well.
	 * @return ids of replicated objects
	 */
	Set<ObjectID> newReplica(final SessionID sessionID, final ObjectID objectID,
					final ExecutionEnvironmentID destBackendID,
					final boolean registerMetaData,
					final boolean recursive);

	/**
	 * Move object from this location to the one specified
	 * 
	 * @param sessionID
	 *            Session ID
	 * @param objectID
	 *            ID of the object to move
	 * @param destStLocation
	 *            Destination location
	 * @param recursive
	 *            Indicates if all sub-objects (in this location or others) must be moved as well.
	 * @return Set of moved objects
	 */
	Set<ObjectID> moveObjects(final SessionID sessionID, final ObjectID objectID,
			final ExecutionEnvironmentID destStLocation, final boolean recursive);

	/**
	 * This operation removes the objects with IDs provided NOTE: This function is recursive, it is going to other DSs if
	 * needed.
	 * 
	 * @param sessionID
	 *            Session ID
	 * @param objectIDs
	 *            ID of the objects to remove
	 * @param recursive
	 *            Indicates if remove is recursive or not.
	 * @param moving
	 *            Indicates remove is caused by a movement of an object.
	 * @param newHint
	 *            New hint in case of move.
	 * @return IDs of the objects removed and their backends.
	 * 
	 */
	Map<ObjectID, ExecutionEnvironmentID> removeObjects(final SessionID sessionID, final Set<ObjectID> objectIDs,
			final boolean recursive, final boolean moving, final ExecutionEnvironmentID newHint);

	/**
	 * Get ClassID from object in memory. Used in case the object is still pending to register and Hints point to this DS.
	 * 
	 * @param objectID
	 *            ID of the object
	 * @return Class ID of the object
	 */
	MetaClassID getClassIDFromObjectInMemory(final ObjectID objectID);

	/**
	 * Migrate every object of this backend to one of the backends specified
	 * 
	 * @param backends
	 *            Specifications of the backends
	 * @return For each backend specified, the set of IDs of migrated objects (ands their new handler IDs) from this backend to
	 *         it. Also return the objects that could not be migrated at all.
	 */
	Tuple<Map<StorageLocationID, Set<ObjectID>>, Set<ObjectID>> migrateObjectsToBackends(
			final Map<StorageLocationID, StorageLocation> backends);

	/**
	 * Register all pending objects
	 */
	void registerPendingObjects();

	/**
	 * Check if the object exists EE memory
	 * @param objectID ID of the object to check
	 * @return TRUE if the object exists in EE memory.
	 */
	boolean existsInEE(final ObjectID objectID);
	
	// STORAGE LOCATION/DB HANDLER FUNCTIONS

	/**
	 * Check if the object exists in SL or in any EE memory associated to current SL 
	 * @param objectID ID of the object to check
	 * @return TRUE if the object either exists in SL disk or in EE memory.
	 */
	boolean exists(final ObjectID objectID);
	
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
	void store(final ExecutionEnvironmentID eeID, final ObjectID objectID, final byte[] bytes);

	// ==================== GETTERS ====================//

	/**
	 * Get serialized object identified by ObjectID
	 * 
	 * @param eeID
	 *            ID of the EE triggering the call
	 * @param objectID
	 *            ID of the object
	 * @return Bytes of the serialized object with ID provided.
	 */
	byte[] get(final ExecutionEnvironmentID eeID, final ObjectID objectID);

	// ==================== UPDATE ====================//

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
	 *            dirty Indicates object has been modified. If false, it means that bytes only contains reference counting
	 *            information. DESIGN NOTE: in order to be able to find out which references where removed in complex objects
	 *            (arrays, collections) GlobalGc decreases all pointed references in a Get procedure and increase them again
	 *            (except removed ones) during update. While in EE, objects have memory references so they cannot be removed
	 *            neither.
	 */
	void update(final ExecutionEnvironmentID eeID, final ObjectID objectID, final byte[] newbytes, final boolean dirty);

	// ==================== DELETE ====================//

	/**
	 * Deletes and object from the database.
	 * 
	 * @param eeID
	 *            ID of the EE triggering the call
	 * @param objectID
	 *            ID of the object to delete
	 */
	void delete(final ExecutionEnvironmentID eeID, final ObjectID objectID);

	// Garbage collection
	/**
	 * Close session in DS. Used to notify that some objects are not longer 'retained' by sessions.
	 * 
	 * @param sessionID
	 *            ID of session.
	 */
	void closeSessionInDS(final SessionID sessionID);

	/**
	 * Update counters of references.
	 * 
	 * @param updateCounterRefs
	 *            Update counter of references.
	 */
	void updateRefs(final Map<ObjectID, Integer> updateCounterRefs);

	/**
	 * Get IDs of references retained by EE.
	 * 
	 * @return References retained by EE (sessions, alias...)
	 */
	Set<ObjectID> getRetainedReferences();

	/**
	 * Deletes all the classes in the execution class directory
	 */
	void cleanExecutionClassDirectory();

	/**
	 * Close DBHandler.
	 */
	void closeDbHandler();

	/**
	 * Shutdown server. Called from Logic Module.
	 */
	void shutDown();

	/**
	 * Disconnect server from others servers. Called from Logic Module.
	 */
	void disconnectFromOthers();
	
	/**
	 * Activate tracing.
	 * @param currentAvailableTaskID Current starting task ID in Extrae
	 */
	void activateTracing(final int currentAvailableTaskID);

	/**
	 * Deactivate Extrae tracing
	 */
	void deactivateTracing();

	/**
	 * Get Extrae traces (mpits and set files)
	 * @return Extrae traces (mpits and set files)
	 */
	Map<String, byte[]> getTraces();
}
