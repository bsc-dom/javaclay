
package es.bsc.dataclay.serialization.util.ids;

import java.util.IdentityHashMap;
import java.util.ListIterator;
import java.util.Map;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.java.DataClayJavaWrapper;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;

/**
 * Wrapper for OID class. *
 */
public final class ObjectIDWrapper extends DataClayJavaWrapper {

	/** OID. */
	private ObjectID oidObject;

	/**
	 * Constructor used for deserialization
	 */
	public ObjectIDWrapper() {

	}

	/**
	 * Constructor
	 * @param newoid
	 *            ObjectID to wrap
	 */
	public ObjectIDWrapper(final ObjectID newoid) {
		this.setOidObject(newoid);
	}

	@Override
	public Object getJavaObject() {
		return oidObject;
	}

	/**
	 * Set the ObjectIDWrapper::oidObject
	 * @param newoid
	 *            the oid to set
	 */
	public void setOidObject(final ObjectID newoid) {
		this.oidObject = newoid;
	}

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {
		this.oidObject.serialize(dcBuffer, ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedObjs) {
		this.oidObject = new ObjectID();
		this.oidObject.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedObjs);
	}

	@Override
	public int hashCode() {
		return oidObject.hashCode();
	}

	@Override
	public boolean equals(final Object object) {
		if (!(object instanceof ObjectIDWrapper)) {
			return false;
		}
		final ObjectIDWrapper value = (ObjectIDWrapper) object;
		return value.getJavaObject().equals(this.getJavaObject());
	}

	@Override
	public boolean isImmutable() {
		return true;
	}

	@Override
	public boolean isNull() {
		return oidObject == null;
	}
}
