
package es.bsc.dataclay.serialization.java.util;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.java.DataClayJavaWrapper;
import es.bsc.dataclay.serialization.java.LanguageTypes;
import es.bsc.dataclay.serialization.lib.DataClayDeserializationLib;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.classloaders.DataClayClassLoader;
import es.bsc.dataclay.util.classloaders.DataClayClassLoaderSrv;
import es.bsc.dataclay.util.ids.MetaClassID;

/**
 * This class represents a java Map in DataClay.
 */
public final class MapWrapper extends DataClayJavaWrapper {

	/** Map. */
	@SuppressWarnings("rawtypes")
	private Map map;

	/**
	 * Constructor used for recursive deserialization
	 */
	public MapWrapper() {

	}

	/**
	 * Constructor
	 * @param newmap
	 *            Map
	 */
	@SuppressWarnings("rawtypes")
	public MapWrapper(final Map newmap) {
		this.setMap(newmap);
	}

	/**
	 * Get the DataClayMap::map
	 * @return the map
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Map getJavaObject() {
		return map;
	}

	/**
	 * Set the DataClayMap::map
	 * @param newmap
	 *            the map to set
	 */
	@SuppressWarnings("rawtypes")
	public void setMap(final Map newmap) {
		this.map = newmap;
	}

	// CHECKSTYLE:OFF

	/**
	 * Create new collection
	 * @param param
	 *            Parameter for collection
	 */
	private CollectionWrapper newCollection(final Object param) {
		return new CollectionWrapper((Collection<?>) param);
	}

	/**
	 * Create new collection
	 */
	private CollectionWrapper newCollection() {
		return new CollectionWrapper();
	}

	/**
	 * Load class using proper class loader
	 * @param className
	 *            Name of class
	 * @return
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

	@SuppressWarnings("unchecked")
	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps, final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {

		// Indicate if Java collection or not
		if (map instanceof DataClayObject) {
			dcBuffer.writeByte((byte) LanguageTypes.DATACLAYOBJ.ordinal());
			final DataClayObject mapDc = (DataClayObject) map;
			mapDc.getMetaClassID().serialize(dcBuffer, ignoreUserTypes,
					ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
		} else {
			dcBuffer.writeByte((byte) LanguageTypes.JAVA_MAP.ordinal());
			dcBuffer.writeString(map.getClass().getName());
		}

		final LinkedList<Object> keys = new LinkedList<>();
		final LinkedList<Object> values = new LinkedList<>();
		for (final Object entry : map.entrySet()) {
			final Entry<Object, Object> castEntry = (Entry<Object, Object>) entry;
			keys.add(castEntry.getKey());
			values.add(castEntry.getValue());
		}

		// Serialize keys
		newCollection(keys).serialize(dcBuffer, ignoreUserTypes, ifaceBitMaps,
				curSerializedObjs, pendingObjs, referenceCounting);

		// Serialize values
		newCollection(values).serialize(dcBuffer, ignoreUserTypes, ifaceBitMaps,
				curSerializedObjs, pendingObjs, referenceCounting);

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
		if (collType == LanguageTypes.JAVA_MAP.ordinal()) {
			final String collName = dcBuffer.readString();
			if (DataClayDeserializationLib.DEBUG_ENABLED) { 
				DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Collection name deserialized: data="+  collName + ", readerindex=" + dcBuffer.readerIndex());
			}
			// Instantiate map
			try {
				final Class<?> mapClass = loadClass(collName);
				map = (Map) mapClass.newInstance();
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
				final Class<?> mapClass = loadClass(classID);
				map = (Map) mapClass.newInstance();
			} catch (final Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		// Keys

		final CollectionWrapper keysWrap = newCollection();
		keysWrap.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedJavaObjs);
		final LinkedList<Object> keys = (LinkedList<Object>) keysWrap.getJavaObject();

		// Values
		final CollectionWrapper valuesWrap = newCollection();
		valuesWrap.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedJavaObjs);
		final LinkedList<Object> values = (LinkedList<Object>) valuesWrap.getJavaObject();

		final Iterator<Object> valuesIt = values.iterator();
		for (final Object key : keys) {
			final Object value = valuesIt.next();
			map.put(key, value);
		}
	}

	@Override
	public int hashCode() {
		return map.hashCode();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(final Object object) {
		if (!(object instanceof MapWrapper)) {
			return false;
		}

		final MapWrapper otherMap = (MapWrapper) object;
		for (final Object entrySet : map.entrySet()) {
			final Map.Entry entry = (Map.Entry) entrySet;
			final Object key = entry.getKey();
			final Object value = entry.getValue();
			final Object otherValue = otherMap.getJavaObject().get(key);
			if (value == null) {
				if (otherValue != null) {
					return false;
				}
			} else {
				if (!otherMap.getJavaObject().get(key).equals(value)) {
					return false;
				}
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
		return map == null;
	}
}
