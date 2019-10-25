
package es.bsc.dataclay.serialization.java.util.concurrent.atomic;

import java.util.IdentityHashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.java.DataClayJavaWrapper;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.MetaClassID;

/**
 * This class represents an AtomicInteger in DataClay.
 */
public final class AtomicIntegerWrapper extends DataClayJavaWrapper {

	/** Atomic Integer. */
	private AtomicInteger atomicInteger;

	/**
	 * Constructor used for recursive deserialization
	 */
	public AtomicIntegerWrapper() {

	}

	/**
	 * Constructor
	 * @param newatomicInteger
	 *            AtomicInteger
	 */
	public AtomicIntegerWrapper(final AtomicInteger newatomicInteger) {
		this.setAtomicInteger(newatomicInteger);
	}

	/**
	 * Get the JavaAtomicInteger::atomicInteger
	 * @return the atomicInteger
	 */
	public AtomicInteger getAtomicInteger() {
		return atomicInteger;
	}

	/**
	 * Set the JavaAtomicInteger::atomicInteger
	 * @param newatomicInteger
	 *            the atomicInteger to set
	 */
	public void setAtomicInteger(final AtomicInteger newatomicInteger) {
		this.atomicInteger = newatomicInteger;
	}

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps, final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {

		dcBuffer.writeInt(this.atomicInteger.get());

	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata, final Map<Integer, Object> curDeserializedJavaObjs) {
		final int value = dcBuffer.readInt();
		this.atomicInteger = new AtomicInteger(value);
	}

	@Override
	public AtomicInteger getJavaObject() {
		return this.atomicInteger;
	}

	@Override
	public boolean isImmutable() {
		return false;
	}

	@Override
	public boolean isNull() {
		return atomicInteger == null;
	}

}
