
package es.bsc.dataclay.serialization.lib;

import java.util.IdentityHashMap;
import java.util.ListIterator;
import java.util.Map;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.serialization.DataClaySerializable;
import es.bsc.dataclay.serialization.buffer.DataClayByteArray;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.java.DataClayJavaWrapper;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.MetaClassID;

/**
 * Language-dependant params or returns.
 */
public final class ImmutableParamOrReturn implements DataClaySerializable {

	// CHECKSTYLE:OFF

	/** Wrapper including Java object. */
	protected DataClayJavaWrapper wrapper;

	/** Array of serialized bytes of object. */
	protected DataClayByteArray serializedBytes;

	// CHECKSTYLE:ON

	/**
	 * For yaml serialization.
	 */
	public ImmutableParamOrReturn() {

	}

	/**
	 * Constructor
	 * @param thewrapper
	 *            Wrapper containing Java object.
	 */
	public ImmutableParamOrReturn(final DataClayJavaWrapper thewrapper) {
		wrapper = thewrapper;
	}

	/**
	 * Constructor
	 * @param thebytes
	 *            Bytes of language object
	 */
	public ImmutableParamOrReturn(final DataClayByteArray thebytes) {
		this.serializedBytes = thebytes;
	}

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps, final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {
		// FORMAT: [ DATA ]
		wrapper.serialize(dcBuffer, false, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata, final Map<Integer, Object> curDeserializedJavaObjs) {
		// FORMAT: [ DATA ]
		wrapper.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedJavaObjs);
	}

	/**
	 * @param byteArray
	 *            Serialized bytes
	 */
	public void setSerializedBytes(final DataClayByteArray byteArray) {
		this.serializedBytes = byteArray;
	}

	/**
	 * @return Serialized bytes
	 */
	public DataClayByteArray getSerializedBytes() {
		return this.serializedBytes;
	}

	/**
	 * @return Wrapper
	 */
	public DataClayJavaWrapper getWrapper() {
		return wrapper;
	}

	/**
	 * @param thewrapper
	 *            the wrapper to set
	 */
	public void setWrapper(final DataClayJavaWrapper thewrapper) {
		this.wrapper = thewrapper;
	}
}
