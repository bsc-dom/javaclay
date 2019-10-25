
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
public final class LanguageParamOrReturn implements DataClaySerializable {
	// CHECKSTYLE:OFF

	/** Wrapper including Java object. */
	protected DataClayJavaWrapper wrapper;

	/** MetaData of object. */
	private DataClayObjectMetaData metaData;

	/** Array of serialized bytes of object. */
	protected DataClayByteArray serializedBytes;
	// CHECKSTYLE:ON

	/**
	 * For yaml serialization.
	 */
	public LanguageParamOrReturn() {

	}

	/**
	 * Constructor
	 * @param thewrapper
	 *            Wrapper containing Java object.
	 */
	public LanguageParamOrReturn(final DataClayJavaWrapper thewrapper) {
		wrapper = thewrapper;
	}

	/**
	 * Constructor
	 * @param themetaData
	 *            MetaData of the java object
	 * @param thebytes
	 *            Bytes of language object
	 */
	public LanguageParamOrReturn(final DataClayObjectMetaData themetaData,
			final DataClayByteArray thebytes) {
		this.metaData = themetaData;
		this.serializedBytes = thebytes;
	}

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {

		wrapper.serialize(dcBuffer, false, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);

		// Set metadata
		setMetaData(SerializationLibUtils.createMetaData(curSerializedObjs, null, 0));

	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata, final Map<Integer, Object> curDeserializedJavaObjs) {
		wrapper.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedJavaObjs);
	}

	/**
	 * @return the serializedBytes
	 */
	public DataClayByteArray getSerializedBytes() {
		return serializedBytes;
	}

	/**
	 * @param theserializedBytes
	 *            the serializedBytes to set
	 */
	public void setSerializedBytes(final DataClayByteArray theserializedBytes) {
		this.serializedBytes = theserializedBytes;
	}

	/**
	 * @return the metaData
	 */
	public DataClayObjectMetaData getMetaData() {
		return metaData;
	}

	/**
	 * @param themetaData
	 *            the metaData to set
	 */
	public void setMetaData(final DataClayObjectMetaData themetaData) {
		this.metaData = themetaData;
	}

	/**
	 * @return the wrapper
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
