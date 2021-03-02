
package es.bsc.dataclay.serialization.java.lang;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.commonruntime.DataClayRuntime;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.java.DataClayJavaWrapper;
import es.bsc.dataclay.serialization.java.LanguageTypes;
import es.bsc.dataclay.serialization.java.util.ArrayWrapper;
import es.bsc.dataclay.serialization.java.util.CollectionWrapper;
import es.bsc.dataclay.serialization.java.util.MapWrapper;
import es.bsc.dataclay.serialization.java.util.concurrent.atomic.AtomicIntegerWrapper;
import es.bsc.dataclay.serialization.lib.DataClayDeserializationLib;
import es.bsc.dataclay.serialization.lib.DataClaySerializationLib;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;

/**
 * Utility functions for wrapping Java classes into DataClay.
 */
public final class ObjectWrapper extends DataClayJavaWrapper {

	/** Object. */
	private Object genericObject;

	/**
	 * Constructor used for deserialization
	 */
	public ObjectWrapper() {

	}

	/**
	 * Constructor
	 * @param newobject
	 *            Object
	 */
	public ObjectWrapper(final Object newobject) {
		this.genericObject = newobject;
	}

	/**
	 * Get the object
	 * @return the genericObject
	 */
	@Override
	public Object getJavaObject() {
		return genericObject;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {

		final Class<?> returnType = genericObject.getClass();
		if (DataClayObject.class.isAssignableFrom(returnType)) {
			dcBuffer.writeByte((byte) LanguageTypes.DATACLAYOBJ.ordinal());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized generic type: data=" + LanguageTypes.DATACLAYOBJ.ordinal() + ", writerIndex=" + dcBuffer.writerIndex());
			}
			DataClaySerializationLib.serializeAssociation((DataClayObject) genericObject, dcBuffer,
					ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);

		} else if (returnType.equals(ObjectID.class)) {
			dcBuffer.writeByte((byte) LanguageTypes.DATACLAY_OID.ordinal());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized generic type: data=" + LanguageTypes.DATACLAY_OID.ordinal() + ", writerIndex=" + dcBuffer.writerIndex());
			}
			((ObjectID) genericObject).serialize(dcBuffer, ignoreUserTypes, ifaceBitMaps,
					curSerializedObjs, pendingObjs, referenceCounting);

		} else if (returnType.equals(ExecutionEnvironmentID.class)) {
			dcBuffer.writeByte((byte) LanguageTypes.DATACLAY_EXECID.ordinal());
			if (DataClaySerializationLib.DEBUG_ENABLED) {
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized generic type: data=" + LanguageTypes.DATACLAY_EXECID.ordinal() + ", writerIndex=" + dcBuffer.writerIndex());
			}
			((ExecutionEnvironmentID) genericObject).serialize(dcBuffer, ignoreUserTypes, ifaceBitMaps,
					curSerializedObjs, pendingObjs, referenceCounting);


		} else if (returnType.equals(Integer.class)) {
			// ---------------- JAVA INTEGER ---------------- //
			dcBuffer.writeByte((byte) LanguageTypes.JAVA_INTEGER.ordinal());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized generic type: data=" + LanguageTypes.JAVA_INTEGER.ordinal() + ", writerIndex=" + dcBuffer.writerIndex());
			}
			dcBuffer.writeInt((Integer) genericObject);

		} else if (returnType.equals(Boolean.class)) {
			// ---------------- JAVA BOOLEAN ---------------- //
			dcBuffer.writeByte((byte) LanguageTypes.JAVA_BOOLEAN.ordinal());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized generic type: data=" + LanguageTypes.JAVA_BOOLEAN.ordinal() + ", writerIndex=" + dcBuffer.writerIndex());
			}
			dcBuffer.writeBoolean((Boolean) genericObject);

		} else if (returnType.equals(Byte.class)) {
			// ---------------- JAVA BYTE ---------------- //
			dcBuffer.writeByte((byte) LanguageTypes.JAVA_BYTE.ordinal());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized generic type: data=" + LanguageTypes.JAVA_BYTE.ordinal() + ", writerIndex=" + dcBuffer.writerIndex());
			}
			dcBuffer.writeByte((Byte) genericObject);

		} else if (returnType.equals(Character.class)) {
			// ---------------- JAVA CHAR ---------------- //
			dcBuffer.writeByte((byte) LanguageTypes.JAVA_CHARACTER.ordinal());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized generic type: data=" + LanguageTypes.JAVA_CHARACTER.ordinal() + ", writerIndex=" + dcBuffer.writerIndex());
			}
			dcBuffer.writeChar((Character) genericObject);

		} else if (returnType.equals(Double.class)) {
			// ---------------- JAVA DOUBLE ---------------- //
			dcBuffer.writeByte((byte) LanguageTypes.JAVA_DOUBLE.ordinal());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized generic type: data=" + LanguageTypes.JAVA_DOUBLE.ordinal() + ", writerIndex=" + dcBuffer.writerIndex());
			}
			dcBuffer.writeDouble((Double) genericObject);

		} else if (returnType.equals(Float.class)) {
			// ---------------- JAVA FLOAT ---------------- //
			dcBuffer.writeByte((byte) LanguageTypes.JAVA_FLOAT.ordinal());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized generic type: data=" + LanguageTypes.JAVA_FLOAT.ordinal() + ", writerIndex=" + dcBuffer.writerIndex());
			}
			dcBuffer.writeFloat((Float) genericObject);

		} else if (returnType.equals(Long.class)) {
			// ---------------- JAVA LONG ---------------- //
			dcBuffer.writeByte((byte) LanguageTypes.JAVA_LONG.ordinal());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized generic type: data=" + LanguageTypes.JAVA_LONG.ordinal() + ", writerIndex=" + dcBuffer.writerIndex());
			}
			dcBuffer.writeLong((Long) genericObject);

		} else if (returnType.equals(Short.class)) {
			// ---------------- JAVA SHORT ---------------- //
			dcBuffer.writeByte((byte) LanguageTypes.JAVA_SHORT.ordinal());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized generic type: data=" + LanguageTypes.JAVA_SHORT.ordinal() + ", writerIndex=" + dcBuffer.writerIndex());
			}
			dcBuffer.writeShort((Short) genericObject);

		} else if (returnType.equals(String.class)) {
			// ---------------- JAVA STRING ---------------- //
			dcBuffer.writeByte((byte) LanguageTypes.JAVA_STRING.ordinal());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized generic type: data=" + LanguageTypes.JAVA_STRING.ordinal() + ", writerIndex=" + dcBuffer.writerIndex());
			}
			dcBuffer.writeString((String) genericObject);

		} else if (returnType.equals(AtomicInteger.class)) {
			dcBuffer.writeByte((byte) LanguageTypes.JAVA_ATOMIC_INTEGER.ordinal());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized generic type: data=" + LanguageTypes.JAVA_ATOMIC_INTEGER.ordinal() + ", writerIndex=" + dcBuffer.writerIndex());
			}
			DataClaySerializationLib.serializeJavaField(new AtomicIntegerWrapper((AtomicInteger) genericObject),
					dcBuffer, ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);

		} else if (returnType.isArray()) {
			// ---------------- ARRAY ---------------- //
			dcBuffer.writeByte((byte) LanguageTypes.JAVA_ARRAY.ordinal());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized generic type: data=" + LanguageTypes.JAVA_ARRAY.ordinal() + ", writerIndex=" + dcBuffer.writerIndex());
			}
			DataClaySerializationLib.serializeJavaField(new ArrayWrapper(genericObject),
					dcBuffer, ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);

		} else if (Collection.class.isAssignableFrom(returnType)) {
			// ---------------- COLLECTION ---------------- //

			dcBuffer.writeByte((byte) LanguageTypes.JAVA_COLLECTION.ordinal());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized generic type: data=" + LanguageTypes.JAVA_COLLECTION.ordinal() + ", writerIndex=" + dcBuffer.writerIndex());
			}
			DataClaySerializationLib.serializeJavaField(new CollectionWrapper((Collection) genericObject),
					dcBuffer, ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);

		} else if (Map.Entry.class.isAssignableFrom(returnType)) {
			dcBuffer.writeByte((byte) LanguageTypes.JAVA_MAP_ENTRY.ordinal());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized generic type: data=" + LanguageTypes.JAVA_MAP_ENTRY.ordinal() + ", writerIndex=" + dcBuffer.writerIndex());
			}
			DataClaySerializationLib.serializeJavaField(new es.bsc.dataclay.serialization.java.util.Map.EntryWrapper((Map.Entry) genericObject),
					dcBuffer, ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);

		} else if (Map.class.isAssignableFrom(returnType)) {
			// ---------------- MAP ---------------- //
			dcBuffer.writeByte((byte) LanguageTypes.JAVA_MAP.ordinal());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized generic type: data=" + LanguageTypes.JAVA_MAP.ordinal() + ", writerIndex=" + dcBuffer.writerIndex());
			}
			DataClaySerializationLib.serializeJavaField(new MapWrapper((Map) genericObject),
					dcBuffer, ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
		} else {
			DataClaySerializationLib.LOGGER.error("Type {} is not DataClay serializable. Ignoring.",
					genericObject.getClass().getName());
		}

	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedObjs) {

		final byte langTypeByte = dcBuffer.readByte();
		if (DataClayDeserializationLib.DEBUG_ENABLED) { 
			DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Generic objec type deserialized: data="+  langTypeByte + ", readerindex=" + dcBuffer.readerIndex());
		}
		if (langTypeByte == (byte) LanguageTypes.DATACLAYOBJ.ordinal()) {
			this.genericObject = DataClayDeserializationLib.deserializeAssociation(dcBuffer,
					ifaceBitMaps, metadata, curDeserializedObjs,
					DataClayObject.getLib());
		} else if (langTypeByte == (byte) LanguageTypes.DATACLAY_OID.ordinal()) {
			final ObjectID oid = new ObjectID();
			oid.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedObjs);
			this.genericObject = oid;
		} else if (langTypeByte == (byte) LanguageTypes.DATACLAY_EXECID.ordinal()) {
			final ExecutionEnvironmentID id = new ExecutionEnvironmentID();
			id.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedObjs);
			this.genericObject = id;
		} else if (langTypeByte == (byte) LanguageTypes.JAVA_INTEGER.ordinal()) {
			this.genericObject = dcBuffer.readInt();
		} else if (langTypeByte == (byte) LanguageTypes.JAVA_BOOLEAN.ordinal()) {
			this.genericObject = dcBuffer.readBoolean();
		} else if (langTypeByte == (byte) LanguageTypes.JAVA_BYTE.ordinal()) {
			this.genericObject = dcBuffer.readByte();
		} else if (langTypeByte == (byte) LanguageTypes.JAVA_CHARACTER.ordinal()) {
			this.genericObject = dcBuffer.readChar();
		} else if (langTypeByte == (byte) LanguageTypes.JAVA_DOUBLE.ordinal()) {
			this.genericObject = dcBuffer.readDouble();
		} else if (langTypeByte == (byte) LanguageTypes.JAVA_FLOAT.ordinal()) {
			this.genericObject = dcBuffer.readFloat();
		} else if (langTypeByte == (byte) LanguageTypes.JAVA_LONG.ordinal()) {
			this.genericObject = dcBuffer.readLong();
		} else if (langTypeByte == (byte) LanguageTypes.JAVA_SHORT.ordinal()) {
			this.genericObject = dcBuffer.readShort();
		} else if (langTypeByte == (byte) LanguageTypes.JAVA_STRING.ordinal()) {
			this.genericObject = dcBuffer.readString();
		} else if (langTypeByte == (byte) LanguageTypes.JAVA_ARRAY.ordinal()) {
			this.genericObject = DataClayDeserializationLib.deserializeJavaField(new ArrayWrapper(),
					dcBuffer, ifaceBitMaps, metadata,
					curDeserializedObjs);
		} else if (langTypeByte == (byte) LanguageTypes.JAVA_COLLECTION.ordinal()) {
			this.genericObject = DataClayDeserializationLib.deserializeJavaField(new CollectionWrapper(),
					dcBuffer, ifaceBitMaps, metadata,
					curDeserializedObjs);
		} else if (langTypeByte == (byte) LanguageTypes.JAVA_MAP.ordinal()) {
			this.genericObject = DataClayDeserializationLib.deserializeJavaField(new MapWrapper(),
					dcBuffer, ifaceBitMaps, metadata,
					curDeserializedObjs);
		} else if (langTypeByte == (byte) LanguageTypes.JAVA_MAP_ENTRY.ordinal()) {
			this.genericObject = DataClayDeserializationLib.deserializeJavaField(new es.bsc.dataclay.serialization.java.util.Map.EntryWrapper(),
					dcBuffer, ifaceBitMaps, metadata,
					curDeserializedObjs);
		} else if (langTypeByte == (byte) LanguageTypes.JAVA_ATOMIC_INTEGER.ordinal()) {
			this.genericObject = DataClayDeserializationLib.deserializeJavaField(new AtomicIntegerWrapper(),
					dcBuffer, ifaceBitMaps, metadata,
					curDeserializedObjs);
		} else {
			DataClayDeserializationLib.LOGGER.error("Type {} is not DataClay serializable. Ignoring.",
					genericObject.getClass().getName());
		}
		if (DataClayDeserializationLib.DEBUG_ENABLED) { 
			DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Generic objec deserialized: readerindex=" + dcBuffer.readerIndex());
		}
	}

	// CHECKSTYLE:OFF

	@Override
	public boolean isImmutable() {
		return false;
	}

	@Override
	public boolean isNull() {
		return genericObject == null;
	}
}
