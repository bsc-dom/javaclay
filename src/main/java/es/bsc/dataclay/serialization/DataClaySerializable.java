
package es.bsc.dataclay.serialization;

import java.util.IdentityHashMap;
import java.util.ListIterator;
import java.util.Map;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.MetaClassID;

/**
 * All classes send to/from DataClay server must implement this interface to use the binary communication protocol.
 */
public interface DataClaySerializable {

	/**
	 * Serializes the object into the buffer provided using the interface represented in the bitmap specified.
	 * @param dcBuffer
	 *            Buffer in which to serialize the object
	 * @param ignoreUserTypes
	 *            Indicates if user types found during serialization must be ignored or not (for instance, non recursive make
	 *            persistent)
	 * @param ifaceBitMaps
	 *            Map of bitmaps representing the interfaces to use
	 * @param curSerializedObjs
	 *            Current serialized objects Object -> OID tag. This structure must be different during each serialization since OID
	 *            tags are not shared.
	 * @param pendingObjs
	 *            Pending objs.
	 * @param referenceCounting
	 *            Reference counting from this object.
	 */
	void serialize(final DataClayByteBuffer dcBuffer, boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting);

	/**
	 * Deserializes the object from the buffer provided using the interface represented in the bitmap specified.
	 * @param dcBuffer
	 *            Buffer from which to deserialize the object
	 * @param ifaceBitMaps
	 *            Map of bitmaps representing the interfaces to use
	 * @param metadata
	 *            Useful metadata of the object for execution
	 * @param curDeserializedJavaObjs
	 *            Currently deserialized Java objects
	 */
	void deserialize(final DataClayByteBuffer dcBuffer, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedJavaObjs);

}
