
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
 * Wrapper for Short class. *
 */
public final class ShortWrapper extends DataClayJavaWrapper {

	/** Short. */
	private Short shortObject;

	/**
	 * Constructor used for deserialization
	 */
	public ShortWrapper() {

	}

	/**
	 * Constructor
	 * @param newshort
	 *            The short
	 */
	public ShortWrapper(final Short newshort) {
		this.setShort(newshort);
	}

	@Override
	public Object getJavaObject() {
		return shortObject;
	}

	/**
	 * Set the ShortWrapper::short
	 * @param newShort
	 *            the short to set
	 */
	public void setShort(final Short newShort) {
		this.shortObject = newShort;
	}

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {
		dcBuffer.writeShort(this.shortObject);
	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedObjs) {
		this.shortObject = dcBuffer.readShort();
	}

	@Override
	public int hashCode() {
		return shortObject.hashCode();
	}

	@Override
	public boolean equals(final Object object) {
		if (!(object instanceof ShortWrapper)) {
			return false;
		}
		final ShortWrapper value = (ShortWrapper) object;
		return value.getJavaObject().equals(this.getJavaObject());
	}

	@Override
	public boolean isImmutable() {
		return true;
	}

	@Override
	public boolean isNull() {
		return shortObject == null;
	}
}
