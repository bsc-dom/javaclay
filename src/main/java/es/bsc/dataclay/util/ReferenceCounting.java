
package es.bsc.dataclay.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import es.bsc.dataclay.api.BackendID;
import es.bsc.dataclay.serialization.buffer.DataClayByteArray;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.lib.SerializationLibUtils;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.ObjectID;

public class ReferenceCounting {

	/** Pointed references. */
	private final Map<BackendID, Map<ObjectID, Integer>> referenceCounting = new HashMap<>();

	/**
	 * Reference counting constructor
	 */
	public ReferenceCounting() {

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
	 * 
	 * @param dcBuffer
	 *            Buffer in which to write bytes.
	 * @param referenceCounting
	 *            Reference counting from this object.
	 */
	public void serializeReferenceCounting(final DataClayByteBuffer dcBuffer) {

		// TODO: IMPORTANT: this should be removed in new serialization by using paddings to directly access reference counters
		// inside
		// metadata.
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
