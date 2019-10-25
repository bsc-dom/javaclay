
package es.bsc.dataclay.serialization.java.util;

import java.util.AbstractMap;
import java.util.IdentityHashMap;
import java.util.ListIterator;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.java.DataClayJavaWrapper;
import es.bsc.dataclay.serialization.java.lang.ObjectWrapper;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.MetaClassID;

/** Class created only for containing Wrapper Entry type. */
public class Map {

	/**
	 * This class represents a java entry Map in DataClay.
	 */
	public static final class EntryWrapper extends DataClayJavaWrapper {

		/** Entry. */
		@SuppressWarnings("rawtypes")
		private java.util.Map.Entry entry;

		/**
		 * Constructor used for recursive deserialization
		 */
		public EntryWrapper() {

		}

		/**
		 * Constructor
		 * @param newEntry
		 *            Entry
		 */
		@SuppressWarnings("rawtypes")
		public EntryWrapper(final java.util.Map.Entry newEntry) {
			this.setEntry(newEntry);
		}

		/**
		 * Get the DataClayMap::entry
		 * @return the entry
		 */
		@SuppressWarnings("rawtypes")
		@Override
		public java.util.Map.Entry getJavaObject() {
			return entry;
		}

		/**
		 * Set the DataClayMap::entry
		 * @param newentry
		 *            the entry to set
		 */
		@SuppressWarnings("rawtypes")
		public void setEntry(final java.util.Map.Entry newentry) {
			this.entry = newentry;
		}

		@Override
		public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
				final java.util.Map<MetaClassID, byte[]> ifaceBitMaps, final IdentityHashMap<Object, Integer> curSerializedObjs,
				final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {

			if (entry.getKey() == null) {
				dcBuffer.writeByte((byte) 0);
			} else {
				dcBuffer.writeByte((byte) 1);
				new ObjectWrapper(entry.getKey()).serialize(dcBuffer, ignoreUserTypes,
						ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
			}

			if (entry.getValue() == null) {
				dcBuffer.writeByte((byte) 0);
			} else {
				dcBuffer.writeByte((byte) 1);
				new ObjectWrapper(entry.getValue()).serialize(dcBuffer, ignoreUserTypes,
						ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
			}
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public void deserialize(final DataClayByteBuffer dcBuffer,
				final java.util.Map<MetaClassID, byte[]> ifaceBitMaps,
				final DataClayObjectMetaData metadata,
				final java.util.Map<Integer, Object> curDeserializedJavaObjs) {
			Object key = null;
			Object value = null;
			byte isNull = dcBuffer.readByte();
			if (isNull == 1) {
				final ObjectWrapper genWrapper = new ObjectWrapper();
				genWrapper.deserialize(dcBuffer, ifaceBitMaps, metadata,
						curDeserializedJavaObjs);
				key = genWrapper.getJavaObject();
			}

			isNull = dcBuffer.readByte();
			if (isNull == 1) {
				final ObjectWrapper genWrapper = new ObjectWrapper();
				genWrapper.deserialize(dcBuffer, ifaceBitMaps, metadata,
						curDeserializedJavaObjs);
				value = genWrapper.getJavaObject();
			}

			entry = new AbstractMap.SimpleEntry(key, value);
		}

		@Override
		public int hashCode() {
			return entry.hashCode();
		}

		@Override
		public boolean equals(final Object object) {
			if (!(object instanceof EntryWrapper)) {
				return false;
			}
			final EntryWrapper otherEntry = (EntryWrapper) object;
			return otherEntry.getJavaObject().equals(this.getJavaObject());
		}

		@Override
		public boolean isImmutable() {
			return false;
		}

		@Override
		public boolean isNull() {
			return entry == null;
		}
	}
}
