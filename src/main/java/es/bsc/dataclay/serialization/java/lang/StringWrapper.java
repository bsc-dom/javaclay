
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
 * Wrapper for String class. *
 */
public final class StringWrapper extends DataClayJavaWrapper {

	/** String. */
	private String stringObject;

	/**
	 * Constructor used for deserialization
	 */
	public StringWrapper() {

	}

	/**
	 * Constructor
	 * @param newstring
	 *            String object.
	 */
	public StringWrapper(final String newstring) {
		this.setString(newstring);
	}

	@Override
	public Object getJavaObject() {
		return stringObject;
	}

	/**
	 * Set the StringWrapper::string
	 * @param newString
	 *            the string to set
	 */
	public void setString(final String newString) {
		this.stringObject = newString;
	}

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {
		dcBuffer.writeString(this.stringObject);
		if (DataClaySerializationLib.DEBUG_ENABLED) { 
			DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized String: data=" + stringObject + ", writerIndex=" + dcBuffer.writerIndex());
		}
	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedObjs) {
		this.stringObject = dcBuffer.readString();
		if (DataClayDeserializationLib.DEBUG_ENABLED) { 
			DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> String deserialized: data="+  stringObject + ", readerindex=" + dcBuffer.readerIndex());
		}
	}

	@Override
	public int hashCode() {
		return stringObject.hashCode();
	}

	@Override
	public boolean equals(final Object object) {
		if (!(object instanceof StringWrapper)) {
			return false;
		}
		final StringWrapper value = (StringWrapper) object;
		return value.getJavaObject().equals(this.getJavaObject());
	}

	@Override
	public boolean isImmutable() {
		return true;
	}

	@Override
	public boolean isNull() {
		return stringObject == null;
	}
}
