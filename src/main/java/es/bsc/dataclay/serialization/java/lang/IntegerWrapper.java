
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
 * Wrapper for Integer class. *
 */
public final class IntegerWrapper extends DataClayJavaWrapper {

	/** Integer. */
	private Integer integer;

	/**
	 * Constructor used for deserialization
	 */
	public IntegerWrapper() {

	}

	/**
	 * Constructor
	 * @param newinteger
	 *            The integer.
	 */
	public IntegerWrapper(final Integer newinteger) {
		this.setInteger(newinteger);
	}

	@Override
	public Object getJavaObject() {
		return integer;
	}

	/**
	 * Set the IntegerWrapper::integer
	 * @param newInteger
	 *            the integer to set
	 */
	public void setInteger(final Integer newInteger) {
		this.integer = newInteger;
	}

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {
		dcBuffer.writeInt(this.integer);
	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedObjs) {
		this.integer = dcBuffer.readInt();
	}

	@Override
	public int hashCode() {
		return integer.hashCode();
	}

	@Override
	public boolean equals(final Object object) {
		if (!(object instanceof IntegerWrapper)) {
			return false;
		}
		final IntegerWrapper value = (IntegerWrapper) object;
		return value.getJavaObject().equals(this.getJavaObject());
	}

	@Override
	public boolean isImmutable() {
		return true;
	}

	@Override
	public boolean isNull() {
		return integer == null;
	}
}
