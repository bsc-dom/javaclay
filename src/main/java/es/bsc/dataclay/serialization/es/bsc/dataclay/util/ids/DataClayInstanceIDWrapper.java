
package es.bsc.dataclay.serialization.es.bsc.dataclay.util.ids;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.java.DataClayJavaWrapper;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.MetaClassID;

import java.util.IdentityHashMap;
import java.util.ListIterator;
import java.util.Map;

/**
 * Wrapper for DataClayInstanceID class. *
 */
public final class DataClayInstanceIDWrapper extends DataClayJavaWrapper {

	/** OID. */
	private DataClayInstanceID idObject;

	/**
	 * Constructor used for deserialization
	 */
	public DataClayInstanceIDWrapper() {

	}

	/**
	 * Constructor
	 * @param newid
	 *            DataClayInstanceID to wrap
	 */
	public DataClayInstanceIDWrapper(final DataClayInstanceID newid) {
		this.setDataClayInstanceIDObject(newid);
	}

	@Override
	public Object getJavaObject() {
		return idObject;
	}

	/**
	 * @param newid
	 *            the id to set
	 */
	public void setDataClayInstanceIDObject(final DataClayInstanceID newid) {
		this.idObject = newid;
	}

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {
		this.idObject.serialize(dcBuffer, ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedObjs) {
		this.idObject = new DataClayInstanceID();
		this.idObject.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedObjs);
	}

	@Override
	public int hashCode() {
		return idObject.hashCode();
	}

	@Override
	public boolean equals(final Object object) {
		if (!(object instanceof DataClayInstanceIDWrapper)) {
			return false;
		}
		final DataClayInstanceIDWrapper value = (DataClayInstanceIDWrapper) object;
		return value.getJavaObject().equals(this.getJavaObject());
	}

	@Override
	public boolean isImmutable() {
		return true;
	}

	@Override
	public boolean isNull() {
		return idObject == null;
	}
}
