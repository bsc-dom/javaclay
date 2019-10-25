
/**
 * 
 */
package es.bsc.dataclay.serialization;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;

/**
 * List of references wrapper for serialization. Used to share between different languages in DataClay.
 */
public final class DataClayRefList implements DataClaySerializable {

	/** List of references to serialize. */
	private List<ObjectID> references;

	/**
	 * Empty constructor for deserialization.
	 */
	public DataClayRefList() {

	}

	/**
	 * Constructor
	 * @param newrefs
	 *            References
	 */
	public DataClayRefList(final List<ObjectID> newrefs) {
		this.setReferences(newrefs);
	}

	/**
	 * @return the references
	 */
	public List<ObjectID> getReferences() {
		return references;
	}

	/**
	 * @param newreferences
	 *            the references to set
	 */
	public void setReferences(final List<ObjectID> newreferences) {
		if (newreferences == null) {
			throw new IllegalArgumentException("References cannot be null");
		}
		this.references = newreferences;
	}

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {

		dcBuffer.writeInt(this.references.size());
		for (final ObjectID ref : references) {
			ref.serialize(dcBuffer, ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
		}
	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata, final Map<Integer, Object> curDeserializedJavaObjs) {

		final int numRefs = dcBuffer.readInt();
		this.references = new ArrayList<>(numRefs);
		for (int i = 0; i < numRefs; ++i) {
			final ObjectID ref = new ObjectID();
			ref.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedJavaObjs);
			references.add(ref);
		}

	}

}
