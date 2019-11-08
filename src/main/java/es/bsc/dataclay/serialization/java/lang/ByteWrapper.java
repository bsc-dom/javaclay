
package es.bsc.dataclay.serialization.java.lang;

import java.util.IdentityHashMap;
import java.util.ListIterator;
import java.util.Map;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.commonruntime.DataClayRuntime;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.java.DataClayJavaWrapper;
import es.bsc.dataclay.serialization.lib.DataClayDeserializationLib;
import es.bsc.dataclay.serialization.lib.DataClaySerializationLib;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.MetaClassID;

/**
 * Wrapper for Byte class. *
 */
public final class ByteWrapper extends DataClayJavaWrapper {

	/** Byte. */
	private Byte byteObject;

	/**
	 * Constructor used for deserialization
	 */
	public ByteWrapper() {

	}

	/**
	 * Constructor
	 * @param newbyte
	 *            The byte
	 */
	public ByteWrapper(final Byte newbyte) {
		this.setByte(newbyte);
	}

	@Override
	public Object getJavaObject() {
		return byteObject;
	}

	/**
	 * Set the ByteWrapper::byte
	 * @param newByte
	 *            the byte to set
	 */
	public void setByte(final Byte newByte) {
		this.byteObject = newByte;
	}

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {
		dcBuffer.writeByte(this.byteObject);
		if (DataClaySerializationLib.DEBUG_ENABLED) { 
			DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized Byte: data=" + byteObject + ", writerIndex=" + dcBuffer.writerIndex());
		}
	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedObjs) {
		this.byteObject = dcBuffer.readByte();
		if (DataClayDeserializationLib.DEBUG_ENABLED) { 
			DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Byte deserialized: data="+  byteObject + ", readerindex=" + dcBuffer.readerIndex());
		}
	}

	@Override
	public int hashCode() {
		return byteObject.hashCode();
	}

	@Override
	public boolean equals(final Object object) {
		if (!(object instanceof ByteWrapper)) {
			return false;
		}
		final ByteWrapper value = (ByteWrapper) object;
		return value.getJavaObject().equals(this.getJavaObject());
	}

	@Override
	public boolean isImmutable() {
		return true;
	}

	@Override
	public boolean isNull() {
		return byteObject == null;
	}
}
