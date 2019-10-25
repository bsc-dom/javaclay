
/**
 * @file EventObjsMeetConditionID.java
 * @date Jul 26, 2012
 */
package es.bsc.dataclay.util.ids;

import java.util.IdentityHashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;

@SuppressWarnings("serial")
/** This class represents Event Objs meet condition ID. */
public final class EventObjsMeetConditionID extends ID {


	/** Constructor. */
	public EventObjsMeetConditionID() {
		super.setId(UUID.randomUUID());
	}

	/**
	 * Constructs a new id with the UUID provided
	 * @param uuid
	 *            Universal ID of the new id
	 * @see ID(UUID)
	 */
	public EventObjsMeetConditionID(final UUID uuid) {
		super.setId(uuid);
	}

	/**
	 * Constructs a new *ID from a UUID string.
	 * @param uuidStr
	 *            Universal ID for the new *ID, string representation.
	 * @detail This function is used for the implicit parsing from YAML sources.
	 */
	public EventObjsMeetConditionID(final String uuidStr) {
		if (uuidStr == null) {
			super.setId(UUID.randomUUID());
		} else {
			super.setId(UUID.fromString(uuidStr));
		}
	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedJavaObjs) {
		super.deserializeIDBase(dcBuffer);
	}

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer,
			final boolean ignoreUserTypes, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {
		super.serializeBase(dcBuffer);
	}

}
