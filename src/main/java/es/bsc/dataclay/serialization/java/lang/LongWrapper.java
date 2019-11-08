
package es.bsc.dataclay.serialization.java.lang;

import java.util.IdentityHashMap;
import java.util.ListIterator;
import java.util.Map;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.java.DataClayJavaWrapper;
import es.bsc.dataclay.serialization.lib.DataClayDeserializationLib;
import es.bsc.dataclay.serialization.lib.DataClaySerializationLib;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.MetaClassID;

/**
 * Wrapper for Long class. *
 */
public final class LongWrapper extends DataClayJavaWrapper {

	/** Long. */
	private Long longObject;

	/**
	 * Constructor used for deserialization
	 */
	public LongWrapper() {

	}

	/**
	 * Constructor
	 * @param newlong
	 *            Long parameter.
	 */
	public LongWrapper(final Long newlong) {
		this.setLong(newlong);
	}

	@Override
	public Object getJavaObject() {
		return longObject;
	}

	/**
	 * Set the LongWrapper::long
	 * @param newLong
	 *            the long to set
	 */
	public void setLong(final Long newLong) {
		this.longObject = newLong;
	}

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {
		dcBuffer.writeLong(this.longObject);
		if (DataClaySerializationLib.DEBUG_ENABLED) { 
			DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized Long: data=" + longObject + ", writerIndex=" + dcBuffer.writerIndex());
		}
	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedObjs) {
		this.longObject = dcBuffer.readLong();
		if (DataClayDeserializationLib.DEBUG_ENABLED) { 
			DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Long deserialized: data="+  longObject + ", readerindex=" + dcBuffer.readerIndex());
		}
	}

	@Override
	public int hashCode() {
		return longObject.hashCode();
	}

	@Override
	public boolean equals(final Object object) {
		if (!(object instanceof LongWrapper)) {
			return false;
		}
		final LongWrapper value = (LongWrapper) object;
		return value.getJavaObject().equals(this.getJavaObject());
	}

	@Override
	public boolean isImmutable() {
		return true;
	}

	@Override
	public boolean isNull() {
		return longObject == null;
	}
}
