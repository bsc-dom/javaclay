
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
 * Wrapper for Boolean class. *
 */
public final class BooleanWrapper extends DataClayJavaWrapper {

	/** Boolean. */
	private Boolean booleanObject;

	/**
	 * Constructor used for deserialization
	 */
	public BooleanWrapper() {

	}

	/**
	 * Constructor
	 * @param newboolean
	 *            Boolean
	 */
	public BooleanWrapper(final Boolean newboolean) {
		this.setBoolean(newboolean);
	}

	@Override
	public Object getJavaObject() {
		return booleanObject;
	}

	/**
	 * Set the BooleanWrapper::boolean
	 * @param newBoolean
	 *            the boolean to set
	 */
	public void setBoolean(final Boolean newBoolean) {
		this.booleanObject = newBoolean;
	}

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {
		dcBuffer.writeBoolean(this.booleanObject);
		if (DataClaySerializationLib.DEBUG_ENABLED) { 
			DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized Boolean: data=" + booleanObject + ", writerIndex=" + dcBuffer.writerIndex());
		}
	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedObjs) {
		this.booleanObject = dcBuffer.readBoolean();
		if (DataClayDeserializationLib.DEBUG_ENABLED) { 
			DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Boolean deserialized: data="+  booleanObject + ", readerindex=" + dcBuffer.readerIndex());
		}
	}

	@Override
	public int hashCode() {
		return booleanObject.hashCode();
	}

	@Override
	public boolean equals(final Object object) {
		if (!(object instanceof BooleanWrapper)) {
			return false;
		}
		final BooleanWrapper value = (BooleanWrapper) object;
		return value.getJavaObject().equals(this.getJavaObject());
	}

	@Override
	public boolean isImmutable() {
		return true;
	}

	@Override
	public boolean isNull() {
		return booleanObject == null;
	}
}
