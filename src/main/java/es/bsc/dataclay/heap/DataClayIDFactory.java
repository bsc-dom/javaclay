
package es.bsc.dataclay.heap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.CredentialID;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.DataContractID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.ids.ECAID;
import es.bsc.dataclay.util.ids.EventMessageID;
import es.bsc.dataclay.util.ids.EventObjsMeetConditionID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.ID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.ids.ResourceID;
import es.bsc.dataclay.util.ids.SessionID;
import es.bsc.dataclay.util.ids.StorageLocationID;

/**
 * This class create and manages all IDs in dataClay. It is necessary to avoid having thousands of IDs occupying the
 *        Heap.
 *
 */
public class DataClayIDFactory {

	/** This is map of Immutable representation of an ObjectID --> DataClayID. */
	private static final Map<String, ID> dataClayIDs = new ConcurrentHashMap<>();

	/**
	 * Create a new account ID.
	 * @param uuidStr
	 *            UUID string representation.
	 * @return AccountID.
	 */
	public static AccountID newAccountID(final String uuidStr) {
		ID id = dataClayIDs.get(uuidStr);
		if (id == null) {
			// For more information about locking design, see Pool
			synchronized (dataClayIDs) { // ensure only one is created per object
				id = dataClayIDs.get(uuidStr);
				if (id == null) {
					id = new AccountID(uuidStr);
					dataClayIDs.put(uuidStr, id);
				}
			}
		}
		return (AccountID) id;
	}

	/**
	 * Create a new contract ID.
	 * @param uuidStr
	 *            UUID string representation.
	 * @return ContractID.
	 */
	public static ContractID newContractID(final String uuidStr) {
		ID id = dataClayIDs.get(uuidStr);
		if (id == null) {
			// For more information about locking design, see Pool
			synchronized (dataClayIDs) { // ensure only one is created per object
				id = dataClayIDs.get(uuidStr);
				if (id == null) {
					id = new ContractID(uuidStr);
					dataClayIDs.put(uuidStr, id);
				}
			}
		}
		return (ContractID) id;
	}

	/**
	 * Create a new credential ID.
	 * @param uuidStr
	 *            UUID string representation.
	 * @return CredentialID.
	 */
	public static CredentialID newCredentialID(final String uuidStr) {
		ID id = dataClayIDs.get(uuidStr);
		if (id == null) {
			// For more information about locking design, see Pool
			synchronized (dataClayIDs) { // ensure only one is created per object
				id = dataClayIDs.get(uuidStr);
				if (id == null) {
					id = new CredentialID(uuidStr);
					dataClayIDs.put(uuidStr, id);
				}
			}
		}
		return (CredentialID) id;
	}

	/**
	 * Create a new DataClayInstanceID ID.
	 * @param uuidStr
	 *            UUID string representation.
	 * @return DataClayInstanceID.
	 */
	public static DataClayInstanceID newDataClayInstanceID(final String uuidStr) {
		ID id = dataClayIDs.get(uuidStr);
		if (id == null) {
			// For more information about locking design, see Pool
			synchronized (dataClayIDs) { // ensure only one is created per object
				id = dataClayIDs.get(uuidStr);
				if (id == null) {
					id = new DataClayInstanceID(uuidStr);
					dataClayIDs.put(uuidStr, id);
				}
			}
		}
		return (DataClayInstanceID) id;
	}

	/**
	 * Create a new DataContractID ID.
	 * @param uuidStr
	 *            UUID string representation.
	 * @return DataContractID.
	 */
	public static DataContractID newDataContractID(final String uuidStr) {
		ID id = dataClayIDs.get(uuidStr);
		if (id == null) {
			// For more information about locking design, see Pool
			synchronized (dataClayIDs) { // ensure only one is created per object
				id = dataClayIDs.get(uuidStr);
				if (id == null) {
					id = new DataContractID(uuidStr);
					dataClayIDs.put(uuidStr, id);
				}
			}
		}
		return (DataContractID) id;
	}

	/**
	 * Create a new DataSetID.
	 * @param uuidStr
	 *            UUID string representation.
	 * @return DataSetID.
	 */
	public static DataSetID newDataSetID(final String uuidStr) {
		ID id = dataClayIDs.get(uuidStr);
		if (id == null) {
			// For more information about locking design, see Pool
			synchronized (dataClayIDs) { // ensure only one is created per object
				id = dataClayIDs.get(uuidStr);
				if (id == null) {
					id = new DataSetID(uuidStr);
					dataClayIDs.put(uuidStr, id);
				}
			}
		}
		return (DataSetID) id;
	}

	/**
	 * Create a new ECAID.
	 * @param uuidStr
	 *            UUID string representation.
	 * @return ECAID.
	 */
	public static ECAID newECAID(final String uuidStr) {
		ID id = dataClayIDs.get(uuidStr);
		if (id == null) {
			// For more information about locking design, see Pool
			synchronized (dataClayIDs) { // ensure only one is created per object
				id = dataClayIDs.get(uuidStr);
				if (id == null) {
					id = new ECAID(uuidStr);
					dataClayIDs.put(uuidStr, id);
				}
			}
		}
		return (ECAID) id;
	}

	/**
	 * Create a new EventMessageID.
	 * @param uuidStr
	 *            UUID string representation.
	 * @return EventMessageID.
	 */
	public static EventMessageID newEventMessageID(final String uuidStr) {
		ID id = dataClayIDs.get(uuidStr);
		if (id == null) {
			// For more information about locking design, see Pool
			synchronized (dataClayIDs) { // ensure only one is created per object
				id = dataClayIDs.get(uuidStr);
				if (id == null) {
					id = new EventMessageID(uuidStr);
					dataClayIDs.put(uuidStr, id);
				}
			}
		}
		return (EventMessageID) id;
	}

	/**
	 * Create a new EventObjsMeetConditionID.
	 * @param uuidStr
	 *            UUID string representation.
	 * @return EventObjsMeetConditionID.
	 */
	public static EventObjsMeetConditionID newEventObjsMeetConditionID(final String uuidStr) {
		ID id = dataClayIDs.get(uuidStr);
		if (id == null) {
			// For more information about locking design, see Pool
			synchronized (dataClayIDs) { // ensure only one is created per object
				id = dataClayIDs.get(uuidStr);
				if (id == null) {
					id = new EventObjsMeetConditionID(uuidStr);
					dataClayIDs.put(uuidStr, id);
				}
			}
		}
		return (EventObjsMeetConditionID) id;
	}

	/**
	 * Create a new ExecutionEnvironmentID.
	 * @param uuidStr
	 *            UUID string representation.
	 * @return ExecutionEnvironmentID.
	 */
	public static ExecutionEnvironmentID newExecutionEnvironmentID(final String uuidStr) {
		ID id = dataClayIDs.get(uuidStr);
		if (id == null) {
			// For more information about locking design, see Pool
			synchronized (dataClayIDs) { // ensure only one is created per object
				id = dataClayIDs.get(uuidStr);
				if (id == null) {
					id = new ExecutionEnvironmentID(uuidStr);
					dataClayIDs.put(uuidStr, id);
				}
			}
		}
		return (ExecutionEnvironmentID) id;
	}

	/**
	 * Create a new ImplementationID.
	 * @param uuidStr
	 *            UUID string representation.
	 * @return ImplementationID.
	 */
	public static ImplementationID newImplementationID(final String uuidStr) {
		ID id = dataClayIDs.get(uuidStr);
		if (id == null) {
			// For more information about locking design, see Pool
			synchronized (dataClayIDs) { // ensure only one is created per object
				id = dataClayIDs.get(uuidStr);
				if (id == null) {
					id = new ImplementationID(uuidStr);
					dataClayIDs.put(uuidStr, id);
				}
			}
		}
		return (ImplementationID) id;
	}

	/**
	 * Create a new InterfaceID.
	 * @param uuidStr
	 *            UUID string representation.
	 * @return InterfaceID.
	 */
	public static InterfaceID newInterfaceID(final String uuidStr) {
		ID id = dataClayIDs.get(uuidStr);
		if (id == null) {
			// For more information about locking design, see Pool
			synchronized (dataClayIDs) { // ensure only one is created per object
				id = dataClayIDs.get(uuidStr);
				if (id == null) {
					id = new InterfaceID(uuidStr);
					dataClayIDs.put(uuidStr, id);
				}
			}
		}
		return (InterfaceID) id;
	}

	/**
	 * Create a new MetaClassID.
	 * @param uuidStr
	 *            UUID string representation.
	 * @return MetaClassID.
	 */
	public static MetaClassID newMetaClassID(final String uuidStr) {
		ID id = dataClayIDs.get(uuidStr);
		if (id == null) {
			// For more information about locking design, see Pool
			synchronized (dataClayIDs) { // ensure only one is created per object
				id = dataClayIDs.get(uuidStr);
				if (id == null) {
					id = new MetaClassID(uuidStr);
					dataClayIDs.put(uuidStr, id);
				}
			}
		}
		return (MetaClassID) id;
	}

	/**
	 * Create a new NamespaceID.
	 * @param uuidStr
	 *            UUID string representation.
	 * @return NamespaceID.
	 */
	public static NamespaceID newNamespaceID(final String uuidStr) {
		ID id = dataClayIDs.get(uuidStr);
		if (id == null) {
			// For more information about locking design, see Pool
			synchronized (dataClayIDs) { // ensure only one is created per object
				id = dataClayIDs.get(uuidStr);
				if (id == null) {
					id = new NamespaceID(uuidStr);
					dataClayIDs.put(uuidStr, id);
				}
			}
		}
		return (NamespaceID) id;
	}

	/**
	 * Create a new ObjectID.
	 * @param uuidStr
	 *            UUID string representation.
	 * @return ObjectID.
	 */
	public static ObjectID newObjectID(final String uuidStr) {
		ID id = dataClayIDs.get(uuidStr);
		if (id == null) {
			// For more information about locking design, see Pool
			synchronized (dataClayIDs) { // ensure only one is created per object
				id = dataClayIDs.get(uuidStr);
				if (id == null) {
					id = new ObjectID(uuidStr);
					dataClayIDs.put(uuidStr, id);
				}
			}
		}
		return (ObjectID) id;
	}

	/**
	 * Create a new OperationID.
	 * @param uuidStr
	 *            UUID string representation.
	 * @return OperationID.
	 */
	public static OperationID newOperationID(final String uuidStr) {
		ID id = dataClayIDs.get(uuidStr);
		if (id == null) {
			// For more information about locking design, see Pool
			synchronized (dataClayIDs) { // ensure only one is created per object
				id = dataClayIDs.get(uuidStr);
				if (id == null) {
					id = new OperationID(uuidStr);
					dataClayIDs.put(uuidStr, id);
				}
			}
		}
		return (OperationID) id;
	}

	/**
	 * Create a new PropertyID.
	 * @param uuidStr
	 *            UUID string representation.
	 * @return PropertyID.
	 */
	public static PropertyID newPropertyID(final String uuidStr) {
		ID id = dataClayIDs.get(uuidStr);
		if (id == null) {
			// For more information about locking design, see Pool
			synchronized (dataClayIDs) { // ensure only one is created per object
				id = dataClayIDs.get(uuidStr);
				if (id == null) {
					id = new PropertyID(uuidStr);
					dataClayIDs.put(uuidStr, id);
				}
			}
		}
		return (PropertyID) id;
	}

	/**
	 * Create a new ResourceID.
	 * @param uuidStr
	 *            UUID string representation.
	 * @return ResourceID.
	 */
	public static ResourceID newResourceID(final String uuidStr) {
		ID id = dataClayIDs.get(uuidStr);
		if (id == null) {
			// For more information about locking design, see Pool
			synchronized (dataClayIDs) { // ensure only one is created per object
				id = dataClayIDs.get(uuidStr);
				if (id == null) {
					id = new ResourceID(uuidStr);
					dataClayIDs.put(uuidStr, id);
				}
			}
		}
		return (ResourceID) id;
	}

	/**
	 * Create a new SessionID.
	 * @param uuidStr
	 *            UUID string representation.
	 * @return SessionID.
	 */
	public static SessionID newSessionID(final String uuidStr) {
		ID id = dataClayIDs.get(uuidStr);
		if (id == null) {
			// For more information about locking design, see Pool
			synchronized (dataClayIDs) { // ensure only one is created per object
				id = dataClayIDs.get(uuidStr);
				if (id == null) {
					id = new SessionID(uuidStr);
					dataClayIDs.put(uuidStr, id);
				}
			}
		}
		return (SessionID) id;
	}

	/**
	 * Create a new StorageLocationID.
	 * @param uuidStr
	 *            UUID string representation.
	 * @return StorageLocationID.
	 */
	public static StorageLocationID newStorageLocationID(final String uuidStr) {
		ID id = dataClayIDs.get(uuidStr);
		if (id == null) {
			// For more information about locking design, see Pool
			synchronized (dataClayIDs) { // ensure only one is created per object
				id = dataClayIDs.get(uuidStr);
				if (id == null) {
					id = new StorageLocationID(uuidStr);
					dataClayIDs.put(uuidStr, id);
				}
			}
		}
		return (StorageLocationID) id;
	}

	/**
	 * Create a new account ID.
	 * @return AccountID.
	 */
	public static AccountID newAccountID() {
		ID id = null;
		synchronized (dataClayIDs) { // ensure only one is created per object
			id = new AccountID();
			dataClayIDs.put(id.getId().toString(), id);
		}
		return (AccountID) id;
	}

	/**
	 * Create a new contract ID.
	 * @return ContractID.
	 */
	public static ContractID newContractID() {
		ID id = null;
		synchronized (dataClayIDs) { // ensure only one is created per object
			id = new ContractID();
			dataClayIDs.put(id.getId().toString(), id);
		}
		return (ContractID) id;
	}

	/**
	 * Create a new credential ID.
	 * @return CredentialID.
	 */
	public static CredentialID newCredentialID() {
		ID id = null;
		synchronized (dataClayIDs) { // ensure only one is created per object
			id = new CredentialID();
			dataClayIDs.put(id.getId().toString(), id);
		}
		return (CredentialID) id;
	}

	/**
	 * Create a new DataClayInstanceID ID.
	 * @return DataClayInstanceID.
	 */
	public static DataClayInstanceID newDataClayInstanceID() {
		ID id = null;
		synchronized (dataClayIDs) { // ensure only one is created per object
			id = new DataClayInstanceID();
			dataClayIDs.put(id.getId().toString(), id);
		}
		return (DataClayInstanceID) id;
	}

	/**
	 * Create a new DataContractID ID.
	 * @return DataContractID.
	 */
	public static DataContractID newDataContractID() {
		ID id = null;
		synchronized (dataClayIDs) { // ensure only one is created per object
			id = new DataContractID();
			dataClayIDs.put(id.getId().toString(), id);
		}
		return (DataContractID) id;
	}

	/**
	 * Create a new DataSetID.
	 * @return DataSetID.
	 */
	public static DataSetID newDataSetID() {
		ID id = null;
		synchronized (dataClayIDs) { // ensure only one is created per object
			id = new DataSetID();
			dataClayIDs.put(id.getId().toString(), id);
		}
		return (DataSetID) id;
	}

	/**
	 * Create a new ECAID.
	 * @return ECAID.
	 */
	public static ECAID newECAID() {
		ID id = null;
		synchronized (dataClayIDs) { // ensure only one is created per object
			id = new ECAID();
			dataClayIDs.put(id.getId().toString(), id);
		}
		return (ECAID) id;
	}

	/**
	 * Create a new EventMessageID.
	 * @return EventMessageID.
	 */
	public static EventMessageID newEventMessageID() {
		ID id = null;
		synchronized (dataClayIDs) { // ensure only one is created per object
			id = new EventMessageID();
			dataClayIDs.put(id.getId().toString(), id);
		}
		return (EventMessageID) id;
	}

	/**
	 * Create a new EventObjsMeetConditionID.
	 * @return EventObjsMeetConditionID.
	 */
	public static EventObjsMeetConditionID newEventObjsMeetConditionID() {
		ID id = null;
		synchronized (dataClayIDs) { // ensure only one is created per object
			id = new EventObjsMeetConditionID();
			dataClayIDs.put(id.getId().toString(), id);
		}
		return (EventObjsMeetConditionID) id;
	}

	/**
	 * Create a new ExecutionEnvironmentID.
	 * @return ExecutionEnvironmentID.
	 */
	public static ExecutionEnvironmentID newExecutionEnvironmentID() {
		ID id = null;
		synchronized (dataClayIDs) { // ensure only one is created per object
			id = new ExecutionEnvironmentID();
			dataClayIDs.put(id.getId().toString(), id);
		}
		return (ExecutionEnvironmentID) id;
	}

	/**
	 * Create a new ImplementationID.
	 * @return ImplementationID.
	 */
	public static ImplementationID newImplementationID() {
		ID id = null;
		synchronized (dataClayIDs) { // ensure only one is created per object
			id = new ImplementationID();
			dataClayIDs.put(id.getId().toString(), id);
		}
		return (ImplementationID) id;
	}

	/**
	 * Create a new InterfaceID.
	 * @return InterfaceID.
	 */
	public static InterfaceID newInterfaceID() {
		ID id = null;
		synchronized (dataClayIDs) { // ensure only one is created per object
			id = new InterfaceID();
			dataClayIDs.put(id.getId().toString(), id);
		}
		return (InterfaceID) id;
	}

	/**
	 * Create a new MetaClassID.
	 * @return MetaClassID.
	 */
	public static MetaClassID newMetaClassID() {
		ID id = null;
		synchronized (dataClayIDs) { // ensure only one is created per object
			id = new MetaClassID();
			dataClayIDs.put(id.getId().toString(), id);
		}
		return (MetaClassID) id;
	}

	/**
	 * Create a new NamespaceID.
	 * @return NamespaceID.
	 */
	public static NamespaceID newNamespaceID() {
		ID id = null;
		synchronized (dataClayIDs) { // ensure only one is created per object
			id = new NamespaceID();
			dataClayIDs.put(id.getId().toString(), id);
		}
		return (NamespaceID) id;
	}

	/**
	 * Create a new ObjectID.
	 * @return ObjectID.
	 */
	public static ObjectID newObjectID() {
		ID id = null;
		synchronized (dataClayIDs) { // ensure only one is created per object
			id = new ObjectID();
			dataClayIDs.put(id.getId().toString(), id);
		}
		return (ObjectID) id;
	}

	/**
	 * Create a new OperationID.
	 * @return OperationID.
	 */
	public static OperationID newOperationID() {
		ID id = null;
		synchronized (dataClayIDs) { // ensure only one is created per object
			id = new OperationID();
			dataClayIDs.put(id.getId().toString(), id);
		}
		return (OperationID) id;
	}

	/**
	 * Create a new PropertyID.
	 * @return PropertyID.
	 */
	public static PropertyID newPropertyID() {
		ID id = null;
		synchronized (dataClayIDs) { // ensure only one is created per object
			id = new PropertyID();
			dataClayIDs.put(id.getId().toString(), id);
		}
		return (PropertyID) id;
	}

	/**
	 * Create a new ResourceID.
	 * @return ResourceID.
	 */
	public static ResourceID newResourceID() {
		ID id = null;
		synchronized (dataClayIDs) { // ensure only one is created per object
			id = new ResourceID();
			dataClayIDs.put(id.getId().toString(), id);
		}
		return (ResourceID) id;
	}

	/**
	 * Create a new SessionID.
	 * @return SessionID.
	 */
	public static SessionID newSessionID() {
		ID id = null;
		synchronized (dataClayIDs) { // ensure only one is created per object
			id = new SessionID();
			dataClayIDs.put(id.getId().toString(), id);
		}
		return (SessionID) id;
	}

	/**
	 * Create a new StorageLocationID.
	 * @return StorageLocationID.
	 */
	public static StorageLocationID newStorageLocationID() {
		ID id = null;
		synchronized (dataClayIDs) { // ensure only one is created per object
			id = new StorageLocationID();
			dataClayIDs.put(id.getId().toString(), id);
		}
		return (StorageLocationID) id;
	}
}
