
package es.bsc.dataclay.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.api.BackendID;
import es.bsc.dataclay.serialization.buffer.DataClayByteArray;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.lib.DataClaySerializationLib;
import es.bsc.dataclay.serialization.lib.SerializationLibUtils;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.ObjectID;

public class ReferenceCounting {

	/** Pointed references. */
	private final Map<BackendID, Map<ObjectID, Integer>> referenceCounting = new HashMap<>();

	/** External reference: alias, .... */
	private int externalReferences;

	/**
	 * Reference counting constructor
	 */
	public ReferenceCounting() {

	}


	/**
	 * Get number of external references
	 * @return externalReferences number of external references
	 */
	public int getExternalReferences() {
		return externalReferences;
	}

	/**
	 * Set number of external references
	 * @param theexternalReferences number of external references
	 */
	public void setExternalReferences(int theexternalReferences) {
		this.externalReferences = theexternalReferences;
	}


	/**
	 * Add +1 to reference counting
	 * 
	 * @param objectID
	 *            Object id to increment counter
	 * @param hint
	 *            If not null, is added to proper reference counting, otherwise to Null entry (GlobalGC need to ask LM)
	 */
	public void incrementReferenceCounting(
			final ObjectID objectID, final BackendID hint) {
		Map<ObjectID, Integer> referenceCountingPerLocation = referenceCounting.get(hint);
		if (referenceCountingPerLocation == null) {
			// if hint is null, key is NULL, means no location specified.
			referenceCountingPerLocation = new HashMap<>();
			referenceCounting.put(hint, referenceCountingPerLocation);
		}
		final Integer numReferences = referenceCountingPerLocation.get(objectID);
		if (numReferences == null) {
			referenceCountingPerLocation.put(objectID, 1);
		} else {
			referenceCountingPerLocation.put(objectID, numReferences + 1);
		}
	}

	/**
	 * Serialize reference counting
	 * @param dcObject object being serialized with ref counting
	 * @param dcBuffer
	 *            Buffer in which to write bytes.
	 */
	public void serializeReferenceCounting(final DataClayObject dcObject, final DataClayByteBuffer dcBuffer) {

		// TODO: IMPORTANT: this should be removed in new serialization by using paddings to directly access reference counters
		// inside
		// metadata.
		this.externalReferences = 0;
		if (dcObject.getAlias() != null && !dcObject.getAlias().isEmpty()) {
			DataClayObject.logger.debug("Found alias reference : " + dcObject.getAlias());
			this.externalReferences++;
		}

		// check if object was federated
		DataClayInstanceID curDataClayID = DataClayObject.getLib().getDataClayID();
		if (dcObject.getReplicaLocations() != null && dcObject.getReplicaLocations().size() != 0) {
			for (ExecutionEnvironmentID replicaLoc : dcObject.getReplicaLocations()) {
				DataClayInstanceID replicaDcID = DataClayObject.getLib().getExecutionEnvironmentInfo(replicaLoc).getDataClayInstanceID();
				if (!curDataClayID.equals(replicaDcID)) {
					DataClayObject.logger.debug("Found federation reference to {}", replicaDcID);
					this.externalReferences++;
					break;
				}
			}
		}

		dcBuffer.writeInt(this.externalReferences);
		dcBuffer.writeInt(referenceCounting.size()); // size of map
		for (final Entry<BackendID, Map<ObjectID, Integer>> curCounting : referenceCounting.entrySet()) {
			final ExecutionEnvironmentID hint = (ExecutionEnvironmentID) curCounting.getKey();
			if (hint == null) {
				dcBuffer.writeBoolean(true); // is null, null key means no hint for this reference counting.
			} else {
				dcBuffer.writeBoolean(false);
				// TODO: use string for Python parsing. should we use string for all UUIDs? New serialization.
				dcBuffer.writeString(hint.getId().toString());
			}
			final Map<ObjectID, Integer> referenceCountingPerHint = curCounting.getValue();
			dcBuffer.writeInt(referenceCountingPerHint.size()); // size of map
			for (final Entry<ObjectID, Integer> curEntry : referenceCountingPerHint.entrySet()) {
				final ObjectID associatedOID = curEntry.getKey();
				final Integer numRefs = curEntry.getValue();
				// TODO: use string for Python parsing. should we use string for all UUIDs? New serialization.
				dcBuffer.writeString(associatedOID.getId().toString());
				dcBuffer.writeInt(numRefs);
			}
		}

	}

	/**
	 * Deserialize reference counting
	 * 
	 * @param referrerObjectID
	 *            ID of referrer object.
	 * @param bytes
	 *            Bytes representing the reference counting
	 * @return Deserialized map.
	 */
	public void deserializeReferenceCounting(
			final ObjectID referrerObjectID, final byte[] bytes) {
		final DataClayByteArray byteArray = new DataClayByteArray(bytes);
		final DataClayByteBuffer dcBuffer = SerializationLibUtils.newByteBuffer(byteArray);
		this.externalReferences = dcBuffer.readInt();
		try {
			final int mapSize = dcBuffer.readInt();
			for (int i = 0; i < mapSize; i++) {
				ExecutionEnvironmentID hint = null;
				// is null key?
				final boolean isNull = dcBuffer.readBoolean();
				if (!isNull) {
					final String hintUUID = dcBuffer.readString();
					hint = new ExecutionEnvironmentID(hintUUID);
				}
				final Map<ObjectID, Integer> refCounting = new HashMap<>();
				referenceCounting.put(hint, refCounting);
				final int refCountingSize = dcBuffer.readInt();
				for (int j = 0; j < refCountingSize; j++) {
					final String oidUUID = dcBuffer.readString();
					final ObjectID oid = new ObjectID(oidUUID);
					final Integer counting = dcBuffer.readInt();
					refCounting.put(oid, counting);
				}
			}

		} finally {
			dcBuffer.release();
		}
	}

	/**
	 * Get ref. counting
	 * 
	 * @return Ref. counting
	 */
	public Map<BackendID, Map<ObjectID, Integer>> getReferenceCounting() {
		return referenceCounting;
	}
}
