
package es.bsc.dataclay.serialization.java.lang;

import java.util.IdentityHashMap;
import java.util.ListIterator;
import java.util.Map;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.java.DataClayJavaWrapper;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.MetaClassID;

/**
 * Wrapper for Float class. *
 */
public final class FloatWrapper extends DataClayJavaWrapper {

	/** Float. */
	private Float floatObject;

	/**
	 * Constructor used for deserialization
	 */
	public FloatWrapper() {

	}

	/**
	 * Constructor
	 * @param newfloat
	 *            The float.
	 */
	public FloatWrapper(final Float newfloat) {
		this.setFloat(newfloat);
	}

	@Override
	public Object getJavaObject() {
		return floatObject;
	}

	/**
	 * Set the FloatWrapper::float
	 * @param newFloat
	 *            the float to set
	 */
	public void setFloat(final Float newFloat) {
		this.floatObject = newFloat;
	}

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {
		dcBuffer.writeFloat(this.floatObject);
	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedObjs) {
		this.floatObject = dcBuffer.readFloat();
	}

	@Override
	public int hashCode() {
		return floatObject.hashCode();
	}

	@Override
	public boolean equals(final Object object) {
		if (!(object instanceof FloatWrapper)) {
			return false;
		}
		final FloatWrapper value = (FloatWrapper) object;
		return value.getJavaObject().equals(this.getJavaObject());
	}

	@Override
	public boolean isImmutable() {
		return true;
	}

	@Override
	public boolean isNull() {
		return floatObject == null;
	}
}
