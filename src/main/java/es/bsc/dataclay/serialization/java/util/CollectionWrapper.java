
package es.bsc.dataclay.serialization.java.util;

import java.util.BitSet;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.ListIterator;
import java.util.Map;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.commonruntime.DataClayRuntime;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.java.DataClayJavaWrapper;
import es.bsc.dataclay.serialization.java.LanguageTypes;
import es.bsc.dataclay.serialization.java.lang.ObjectWrapper;
import es.bsc.dataclay.serialization.lib.DataClayDeserializationLib;
import es.bsc.dataclay.serialization.lib.DataClaySerializationLib;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.classloaders.DataClayClassLoader;
import es.bsc.dataclay.util.classloaders.DataClayClassLoaderSrv;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;

/**
 * This class represents a java Collection in DataClay.
 */
public final class CollectionWrapper extends DataClayJavaWrapper {

	/** Collection. */
	@SuppressWarnings("rawtypes")
	private Collection collection;

	/**
	 * Constructor for recursive deserialization
	 */
	public CollectionWrapper() {

	}

	/**
	 * Constructor
	 * @param newcollection
	 *            Collection
	 */
	@SuppressWarnings("rawtypes")
	public CollectionWrapper(final Collection newcollection) {
		this.setCollection(newcollection);
	}

	/**
	 * Get the DataClayCollection::collection
	 * @return the collection
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Collection getJavaObject() {
		return collection;
	}

	/**
	 * Set the DataClayCollection::collection
	 * @param newcollection
	 *            the collection to set
	 */
	@SuppressWarnings("rawtypes")
	public void setCollection(final Collection newcollection) {
		this.collection = newcollection;
	}

	// CHECKSTYLE:OFF
	/**
	 * Get genericWrapper
	 * @return Generic wrapper
	 */
	private ObjectWrapper newGeneric() {
		return new ObjectWrapper();
	}

	/**
	 * Get genericWrapper
	 * @param newObj
	 *            generic object to wrap
	 */
	private ObjectWrapper newGeneric(final Object newObj) {
		return new ObjectWrapper(newObj);
	}

	/**
	 * Load class using proper class loader
	 * @param className
	 *            Name of class
	 * @return Class loaded
	 */
	private Class<?> loadClass(final String className) {
		final String fullClassName = className;
		Class<?> clazz = null;
		if (DataClayObject.getLib() != null && DataClayObject.getLib().isDSLib()) {
			clazz = DataClayClassLoaderSrv.getClass(fullClassName);
		} else {
			clazz = DataClayClassLoader.getClass(fullClassName);
		}

		return clazz;
	}

	/**
	 * Load class using proper class loader
	 * @param className
	 *            Name of class
	 * @return
	 */
	private Class<?> loadClass(final MetaClassID classID) {
		Class<?> clazz = null;
		if (DataClayObject.getLib() != null && DataClayObject.getLib().isDSLib()) {
			clazz = DataClayClassLoaderSrv.getClass(classID);
		} else {
			clazz = DataClayClassLoader.getClass(classID);
		}

		return clazz;
	}

	// CHECKSTYLE:ON

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps, final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {

		// Indicate if Java collection or not
		if (collection instanceof DataClayObject) {
			dcBuffer.writeByte((byte) LanguageTypes.DATACLAYOBJ.ordinal());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Collection type serialized: data=" + LanguageTypes.DATACLAYOBJ.ordinal() + ", writerIndex=" + dcBuffer.writerIndex());
			}
			final DataClayObject dcColl = (DataClayObject) collection;
			dcColl.getMetaClassID().serialize(dcBuffer, ignoreUserTypes,
					ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Collection class id serialized: data=" + dcColl.getMetaClassID() + ", writerIndex=" + dcBuffer.writerIndex());
			}
		} else {
			dcBuffer.writeByte((byte) LanguageTypes.JAVA_COLLECTION.ordinal());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Collection type serialized: data=" + LanguageTypes.JAVA_COLLECTION.ordinal() + ", writerIndex=" + dcBuffer.writerIndex());
			}
			dcBuffer.writeString(collection.getClass().getName());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Collection name serialized: data=" + collection.getClass().getName() + ", writerIndex=" + dcBuffer.writerIndex());
			}
		}

		final int collSize = collection.size();

		dcBuffer.writeVLQInt(collSize);
		if (DataClaySerializationLib.DEBUG_ENABLED) { 
			DataClaySerializationLib.LOGGER.debug("[Serialization] --> Collection size serialized: data=" + collSize + ", writerIndex=" + dcBuffer.writerIndex());
		}
		if (collSize > 0) {

			// === NULLS BITMAP === //
			// CHECKSTYLE:OFF
			int numBytes = 0;
			for (int i = 0; i < collSize; ++i) {
				if (i % 8 == 0) {
					numBytes++;
				}
			}
			// CHECKSTYLE:ON
			dcBuffer.writeVLQInt(numBytes);
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Collection bitmap size serialized: data=" + numBytes + ", writerIndex=" + dcBuffer.writerIndex());
			}
			final int bitSetIndex = dcBuffer.writerIndex();
			dcBuffer.writeBytes(new byte[numBytes]);
			final BitSet nullsBitSet = new BitSet(collSize);
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Collection bitmap serialized: data=" + nullsBitSet + ", writerIndex=" + dcBuffer.writerIndex());
			}
			// ===================== //

			int i = 0;
			for (final Object element : collection) {

				if (element != null) {
					nullsBitSet.set(i);
					// ==== ALL TYPES IN COLLECTIONS ARE GENERIC ==== //
					final ObjectWrapper wrapper = this.newGeneric(element);
					wrapper.serialize(dcBuffer,
							ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);

				}
				i++;
			}

			// === UPDATE NULLS BITMAP === //
			final int j = dcBuffer.writerIndex();
			dcBuffer.setWriterIndex(bitSetIndex);
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Modified writerIndex=" + dcBuffer.writerIndex());
			}
			dcBuffer.writeBytes(nullsBitSet.toByteArray());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Collection bitmap serialized: data=" + nullsBitSet + ", writerIndex=" + dcBuffer.writerIndex());
			}
			dcBuffer.setWriterIndex(j);
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Modified writerIndex=" + dcBuffer.writerIndex());
			}

		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedJavaObjs) {
		final byte collType = dcBuffer.readByte();
		if (DataClayDeserializationLib.DEBUG_ENABLED) { 
			DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Collection type deserialized: data="+  collType + ", readerindex=" + dcBuffer.readerIndex());
		}
		if (collType == LanguageTypes.JAVA_COLLECTION.ordinal()) {
			final String collName = dcBuffer.readString();
			if (DataClayDeserializationLib.DEBUG_ENABLED) { 
				DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Collection name deserialized: data="+  collName + ", readerindex=" + dcBuffer.readerIndex());
			}

			// Instantiate collection
			try {
				final Class<?> collClass = loadClass(collName);
				collection = (Collection) collClass.newInstance();
			} catch (final Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		} else {
			final MetaClassID classID = new MetaClassID();
			classID.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedJavaObjs);
			if (DataClayDeserializationLib.DEBUG_ENABLED) { 
				DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Collection class id deserialized: data="+  classID + ", readerindex=" + dcBuffer.readerIndex());
			}
			try {
				final Class<?> collClass = loadClass(classID);
				collection = (Collection) collClass.getConstructor(ObjectID.class).newInstance(new ObjectID());
			} catch (final Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		final int collSize = dcBuffer.readVLQInt();
		if (DataClayDeserializationLib.DEBUG_ENABLED) { 
			DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Collection size deserialized: data="+  collSize + ", readerindex=" + dcBuffer.readerIndex());
		}
		if (collSize > 0) {

			// === NULLS BITMAP === //
			final int bitMapSize = dcBuffer.readVLQInt();
			if (DataClayDeserializationLib.DEBUG_ENABLED) { 
				DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Collection bitmap size deserialized: data="+  bitMapSize + ", readerindex=" + dcBuffer.readerIndex());
			}
			final byte[] bytes = dcBuffer.readBytes(bitMapSize);
			final BitSet nullsBitMap = BitSet.valueOf(bytes);
			if (DataClayDeserializationLib.DEBUG_ENABLED) { 
				DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Collection bitmap deserialized: data="+  nullsBitMap + ", readerindex=" + dcBuffer.readerIndex());
			}
			// ===================== //

			// Fill collection
			for (int i = 0; i < collSize; ++i) {

				Object element = null;
				if (nullsBitMap.get(i)) {
					final ObjectWrapper wrapper = newGeneric();
					wrapper.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedJavaObjs);
					element = wrapper.getJavaObject();

				}
				collection.add(element);
			}
		}
	}

	@Override
	public int hashCode() {
		return collection.hashCode();
	}

	@Override
	public boolean equals(final Object object) {
		if (!(object instanceof CollectionWrapper)) {
			return false;
		}

		final CollectionWrapper value = (CollectionWrapper) object;
		for (final Object element : this.collection) {
			if (!value.getJavaObject().contains(element)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean isImmutable() {
		return false;
	}

	@Override
	public boolean isNull() {
		return collection == null;
	}

}
