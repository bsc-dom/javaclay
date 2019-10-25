
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
 * Wrapper for Double class. *
 */
public final class DoubleWrapper extends DataClayJavaWrapper {

	/** Double. */
	private Double doubleObject;

	/**
	 * Constructor used for deserialization
	 */
	public DoubleWrapper() {

	}

	/**
	 * Constructor
	 * @param newdouble
	 *            The double.
	 */
	public DoubleWrapper(final Double newdouble) {
		this.setDouble(newdouble);
	}

	@Override
	public Object getJavaObject() {
		return doubleObject;
	}

	/**
	 * Set the DoubleWrapper::double
	 * @param newDouble
	 *            the double to set
	 */
	public void setDouble(final Double newDouble) {
		this.doubleObject = newDouble;
	}

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {
		dcBuffer.writeDouble(this.doubleObject);
	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedObjs) {
		this.doubleObject = dcBuffer.readDouble();
	}

	@Override
	public int hashCode() {
		return doubleObject.hashCode();
	}

	@Override
	public boolean equals(final Object object) {
		if (!(object instanceof DoubleWrapper)) {
			return false;
		}
		final DoubleWrapper value = (DoubleWrapper) object;
		return value.getJavaObject().equals(this.getJavaObject());
	}

	@Override
	public boolean isImmutable() {
		return true;
	}

	@Override
	public boolean isNull() {
		return doubleObject == null;
	}
}
