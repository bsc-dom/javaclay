
package es.bsc.dataclay.serialization.java.util;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.ListIterator;
import java.util.Map;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.java.DataClayJavaWrapper;
import es.bsc.dataclay.serialization.java.LanguageTypes;
import es.bsc.dataclay.serialization.java.lang.ObjectWrapper;
import es.bsc.dataclay.serialization.lib.DataClayDeserializationLib;
import es.bsc.dataclay.serialization.lib.DataClaySerializationLib;
import es.bsc.dataclay.serialization.lib.SerializationLibUtils;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.classloaders.DataClayClassLoader;
import es.bsc.dataclay.util.classloaders.DataClayClassLoaderSrv;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.management.stubs.StubInfo;
import es.bsc.dataclay.util.reflection.Reflector;

/**
 * This class represents an array of objects (also for multidimensional arrays) in DataClay.
 */
public final class ArrayWrapper extends DataClayJavaWrapper {

	/** Object array. */
	private Object objectArray;

	/**
	 * Constructor used for deserialization
	 */
	public ArrayWrapper() {

	}

	/**
	 * Constructor
	 * @param newobjectArray
	 *            Object array
	 */
	public ArrayWrapper(final Object newobjectArray) {
		this.setObjectArray(newobjectArray);
	}

	/**
	 * Get the DataClayObjectArray::objectArray
	 * @return the objectArray
	 */
	@Override
	public Object getJavaObject() {
		return objectArray;
	}

	/**
	 * Set the DataClayObjectArray::objectArray
	 * @param newobjectArray
	 *            the objectArray to set
	 */
	public void setObjectArray(final Object newobjectArray) {
		this.objectArray = newobjectArray;
	}

	/**
	 * Get class given the class ID provided
	 * @param classID
	 *            ID of the class to get
	 * @return yhe class identified by class id provided
	 */
	// CHECKSTYLE:OFF
	private Class<?> getClass(final MetaClassID classID) {
		if (DataClayObject.getLib().isDSLib()) {
			return DataClayClassLoaderSrv.getClass(classID);
		} else {
			return DataClayClassLoader.getClass(classID);
		}
	}

	// CHECKSTYLE:ON

	@SuppressWarnings("rawtypes")
	@Override
	public void serialize(final DataClayByteBuffer dcBuffer, final boolean ignoreUserTypes,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {

		// Serialize dimension and size
		final int arrayLength = Array.getLength(objectArray);
		dcBuffer.writeVLQInt(arrayLength);
		// Check array type
		final Class<?> compType = objectArray.getClass().getComponentType();
		final boolean isPrimitiveArray = compType.isPrimitive();
		if (isPrimitiveArray) {
			if (Configuration.Flags.MULTIDIM_ARRAY_STREAM_SERIALIZATION.getBooleanValue()) {
				// Serialize directly
				try {
					dcBuffer.writeByte((byte) LanguageTypes.JAVA_PRIMITIVE_ARRAY.ordinal());
					dcBuffer.writeByteArray(SerializationLibUtils.serializeBinary(objectArray));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			} else {
				// Serialize class tag of component type
				if (compType.equals(int.class)) {
					dcBuffer.writeByte((byte) LanguageTypes.INT.ordinal());
				} else if (compType.equals(byte.class)) {
					dcBuffer.writeByte((byte) LanguageTypes.BYTE.ordinal());
				} else if (compType.equals(char.class)) {
					dcBuffer.writeByte((byte) LanguageTypes.CHAR.ordinal());
				} else if (compType.equals(boolean.class)) {
					dcBuffer.writeByte((byte) LanguageTypes.BOOL.ordinal());
				} else if (compType.equals(short.class)) {
					dcBuffer.writeByte((byte) LanguageTypes.SHORT.ordinal());
				} else if (compType.equals(long.class)) {
					dcBuffer.writeByte((byte) LanguageTypes.LONG.ordinal());
				} else if (compType.equals(float.class)) {
					dcBuffer.writeByte((byte) LanguageTypes.FLOAT.ordinal());
				} else if (compType.equals(double.class)) {
					dcBuffer.writeByte((byte) LanguageTypes.DOUBLE.ordinal());
				}
			}
		} else {
			if (compType.isArray()) {
				// Get component type
				Class<?> deepComponentType = compType;
				while (deepComponentType.isArray()) {
					deepComponentType = deepComponentType.getComponentType();
				}

				// primitive multi-dimensional array and binary direct serialization
				if (deepComponentType.isPrimitive()
						&& Configuration.Flags.MULTIDIM_ARRAY_STREAM_SERIALIZATION.getBooleanValue()) {
					try {
						dcBuffer.writeByte((byte) LanguageTypes.JAVA_PRIMITIVE_ARRAY.ordinal());
						dcBuffer.writeByteArray(SerializationLibUtils.serializeBinary(objectArray));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				} else {
					dcBuffer.writeByte((byte) LanguageTypes.JAVA_ARRAY.ordinal());
					final String name = deepComponentType.getName();
					if (Reflector.isJavaPrimitiveOrArrayTypeName(name)) {
						dcBuffer.writeByte((byte) 0);
						dcBuffer.writeString(compType.getName());
					} else {
						// array of dataClay objects
						dcBuffer.writeByte((byte) 1);
						dcBuffer.writeVLQInt(Reflector.countOccurrences(compType.getName(), "["));
						final StubInfo stubInfo = DataClayObject.getStubInfoFromClass(deepComponentType.getName());
						final MetaClassID compTypeID = stubInfo.getClassID();
						compTypeID.serialize(dcBuffer, ignoreUserTypes, ifaceBitMaps,
								curSerializedObjs, pendingObjs, referenceCounting);
					}
				}
			} else if (compType.equals(Integer.class)) {
				dcBuffer.writeByte((byte) LanguageTypes.JAVA_INTEGER.ordinal());
			} else if (compType.equals(Byte.class)) {
				dcBuffer.writeByte((byte) LanguageTypes.JAVA_BYTE.ordinal());
			} else if (compType.equals(Character.class)) {
				dcBuffer.writeByte((byte) LanguageTypes.JAVA_CHARACTER.ordinal());
			} else if (compType.equals(Boolean.class)) {
				dcBuffer.writeByte((byte) LanguageTypes.JAVA_BOOLEAN.ordinal());
			} else if (compType.equals(Short.class)) {
				dcBuffer.writeByte((byte) LanguageTypes.JAVA_SHORT.ordinal());
			} else if (compType.equals(Long.class)) {
				dcBuffer.writeByte((byte) LanguageTypes.JAVA_LONG.ordinal());
			} else if (compType.equals(Float.class)) {
				dcBuffer.writeByte((byte) LanguageTypes.JAVA_FLOAT.ordinal());
			} else if (compType.equals(Double.class)) {
				dcBuffer.writeByte((byte) LanguageTypes.JAVA_DOUBLE.ordinal());
			} else if (compType.equals(Object.class)) {
				dcBuffer.writeByte((byte) LanguageTypes.JAVA_OBJECT.ordinal());
			} else if (compType.equals(String.class)) {
				dcBuffer.writeByte((byte) LanguageTypes.JAVA_STRING.ordinal());
			} else if (Collection.class.isAssignableFrom(compType)) {
				dcBuffer.writeByte((byte) LanguageTypes.JAVA_COLLECTION.ordinal());
				dcBuffer.writeString(compType.getName());
			} else if (Map.class.isAssignableFrom(compType)) {
				dcBuffer.writeByte((byte) LanguageTypes.JAVA_MAP.ordinal());
				dcBuffer.writeString(compType.getName());
			} else {
				dcBuffer.writeByte((byte) LanguageTypes.DATACLAYOBJ.ordinal());
				final StubInfo stubInfo = DataClayObject.getStubInfoFromClass(compType.getName());
				final MetaClassID compTypeID = stubInfo.getClassID();
				// MetaClassID compTypeID = (MetaClassID)
				// Reflector.getStaticField(compType, StubFieldNames.MCLASSID_FIELDNAME);
				compTypeID.serialize(dcBuffer, ignoreUserTypes, ifaceBitMaps,
						curSerializedObjs, pendingObjs, referenceCounting);

			}
		}
		if (arrayLength > 0) {

			// Create nulls bitset if not primitive array
			BitSet nullsBitSet = null;
			int bitSetIndex = -1;
			if (!isPrimitiveArray) {
				// CHECKSTYLE:OFF
				int numBytes = 0;
				for (int i = 0; i < arrayLength; ++i) {
					if (i % 8 == 0) {
						numBytes++;
					}
				}
				// CHECKSTYLE:ON
				dcBuffer.writeVLQInt(numBytes);
				bitSetIndex = dcBuffer.writerIndex();
				dcBuffer.writeBytes(new byte[numBytes]);
				nullsBitSet = new BitSet(arrayLength);
			}

			// Serialize each element
			for (int i = 0; i < arrayLength; ++i) {
				final Object element = Array.get(objectArray, i);
				if (!isPrimitiveArray && element != null) {
					nullsBitSet.set(i);
				}
				if (element == null) {
					continue;
				}
				if (isPrimitiveArray) {
					if (compType.equals(int.class)) {
						dcBuffer.writeInts((int[]) objectArray);
						break;
					} else if (compType.equals(byte.class)) {
						dcBuffer.writeByteArray((byte[]) objectArray);
						break;
					} else if (compType.equals(char.class)) {
						dcBuffer.writeChars((char[]) objectArray);
						break;
					} else if (compType.equals(boolean.class)) {
						dcBuffer.writeBooleans((boolean[]) objectArray);
						break;
					} else if (compType.equals(short.class)) {
						dcBuffer.writeShorts((short[]) objectArray);
						break;
					} else if (compType.equals(long.class)) {
						dcBuffer.writeLongs((long[]) objectArray);
						break;
					} else if (compType.equals(float.class)) {
						dcBuffer.writeFloats((float[]) objectArray);
						break;
					} else if (compType.equals(double.class)) {
						dcBuffer.writeDoubles((double[]) objectArray);
						break;
					}
				} else if (compType.equals(String.class)) {
					dcBuffer.writeStrings((String[]) objectArray);
					break;
				} else if (compType.equals(Integer.class)) {
					dcBuffer.writeInt((int) element);
				} else if (compType.equals(Byte.class)) {
					dcBuffer.writeByte((byte) element);
				} else if (compType.equals(Character.class)) {
					dcBuffer.writeChar((char) element);
				} else if (compType.equals(Boolean.class)) {
					dcBuffer.writeBoolean((boolean) element);
				} else if (compType.equals(Short.class)) {
					dcBuffer.writeShort((short) element);
				} else if (compType.equals(Long.class)) {
					dcBuffer.writeLong((long) element);
				} else if (compType.equals(Float.class)) {
					dcBuffer.writeFloat((float) element);
				} else if (compType.equals(Double.class)) {
					dcBuffer.writeDouble((double) element);
				} else if (compType.equals(Object.class)) {
					// ==== GENERIC ==== //
					final ObjectWrapper wrapper = new ObjectWrapper(element);
					wrapper.serialize(dcBuffer,
							ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
				} else if (compType.isArray()) {
					// ==== ARRAY ==== //
					// Check if was already serialized
					Integer tag = curSerializedObjs.get(element);
					if (tag == null) {
						tag = new Integer(curSerializedObjs.size());
						dcBuffer.writeVLQInt(tag.intValue());
						curSerializedObjs.put(element, tag);
						new ArrayWrapper(element).serialize(dcBuffer,
								ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
					} else {
						dcBuffer.writeVLQInt(tag.intValue());
					}
				} else if (Collection.class.isAssignableFrom(compType)) {
					// ==== COLLECTION ==== //
					// Check if was already serialized
					Integer tag = curSerializedObjs.get(element);
					if (tag == null) {
						tag = new Integer(curSerializedObjs.size());
						dcBuffer.writeVLQInt(tag.intValue());
						curSerializedObjs.put(element, tag);
						new CollectionWrapper((Collection) element).serialize(dcBuffer,
								ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
					} else {
						dcBuffer.writeVLQInt(tag.intValue());
					}

				} else if (Map.class.isAssignableFrom(compType)) {
					// ==== MAP ==== //
					// Check if was already serialized
					Integer tag = curSerializedObjs.get(element);
					if (tag == null) {
						tag = new Integer(curSerializedObjs.size());
						dcBuffer.writeVLQInt(tag.intValue());
						curSerializedObjs.put(element, tag);
						new MapWrapper((Map) element).serialize(dcBuffer,
								ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
					} else {
						dcBuffer.writeVLQInt(tag.intValue());
					}

				} else {
					// ==== USER TYPE ==== //
					DataClaySerializationLib.serializeAssociation((DataClayObject) element, dcBuffer, ignoreUserTypes,
							ifaceBitMaps, curSerializedObjs, pendingObjs, referenceCounting);
				}
			}
			if (!isPrimitiveArray) {
				final int j = dcBuffer.writerIndex();
				dcBuffer.setWriterIndex(bitSetIndex);
				dcBuffer.writeBytes(nullsBitSet.toByteArray());
				dcBuffer.setWriterIndex(j);
			}
		}
	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedObjs) {

		// Get size
		final int arrLength = dcBuffer.readVLQInt();
		if (DataClayDeserializationLib.DEBUG_ENABLED) { 
			DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Array length deserialized: data="+  arrLength + ", readerindex=" + dcBuffer.readerIndex());
		}
		// Get component type
		final byte compTypeByte = dcBuffer.readByte();
		if (DataClayDeserializationLib.DEBUG_ENABLED) { 
			DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Array type deserialized: data="+  compTypeByte + ", readerindex=" + dcBuffer.readerIndex());
		}
		// Get component type class
		Class<?> compType = null;
		if (compTypeByte == (byte) LanguageTypes.JAVA_PRIMITIVE_ARRAY.ordinal()
				&& Configuration.Flags.MULTIDIM_ARRAY_STREAM_SERIALIZATION.getBooleanValue()) {
			try {
				objectArray = SerializationLibUtils.deserializeBinary(dcBuffer.readByteArray());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		} else if (compTypeByte == (byte) LanguageTypes.INT.ordinal()) {
			compType = int.class;
		} else if (compTypeByte == (byte) LanguageTypes.FLOAT.ordinal()) {
			compType = float.class;
		} else if (compTypeByte == (byte) LanguageTypes.BOOL.ordinal()) {
			compType = boolean.class;
		} else if (compTypeByte == (byte) LanguageTypes.BYTE.ordinal()) {
			compType = byte.class;
		} else if (compTypeByte == (byte) LanguageTypes.CHAR.ordinal()) {
			compType = char.class;
		} else if (compTypeByte == (byte) LanguageTypes.DOUBLE.ordinal()) {
			compType = double.class;
		} else if (compTypeByte == (byte) LanguageTypes.SHORT.ordinal()) {
			compType = short.class;
		} else if (compTypeByte == (byte) LanguageTypes.LONG.ordinal()) {
			compType = long.class;
		} else if (compTypeByte == (byte) LanguageTypes.JAVA_INTEGER.ordinal()) {
			compType = Integer.class;
		} else if (compTypeByte == (byte) LanguageTypes.JAVA_FLOAT.ordinal()) {
			compType = Float.class;
		} else if (compTypeByte == (byte) LanguageTypes.JAVA_BOOLEAN.ordinal()) {
			compType = Boolean.class;
		} else if (compTypeByte == (byte) LanguageTypes.JAVA_BYTE.ordinal()) {
			compType = Byte.class;
		} else if (compTypeByte == (byte) LanguageTypes.JAVA_CHARACTER.ordinal()) {
			compType = Character.class;
		} else if (compTypeByte == (byte) LanguageTypes.JAVA_DOUBLE.ordinal()) {
			compType = Double.class;
		} else if (compTypeByte == (byte) LanguageTypes.JAVA_SHORT.ordinal()) {
			compType = Short.class;
		} else if (compTypeByte == (byte) LanguageTypes.JAVA_LONG.ordinal()) {
			compType = Long.class;
		} else if (compTypeByte == (byte) LanguageTypes.JAVA_STRING.ordinal()) {
			compType = String.class;
		} else if (compTypeByte == (byte) LanguageTypes.JAVA_OBJECT.ordinal()) {
			compType = Object.class;
		} else if (compTypeByte == (byte) LanguageTypes.JAVA_COLLECTION.ordinal()) {
			final String specificCollName = dcBuffer.readString();
			if (DataClayDeserializationLib.DEBUG_ENABLED) { 
				DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Array type name deserialized: data="+  specificCollName + ", readerindex=" + dcBuffer.readerIndex());
			}
			try {
				compType = ClassLoader.getSystemClassLoader().loadClass(specificCollName);
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		} else if (compTypeByte == (byte) LanguageTypes.JAVA_MAP.ordinal()) {
			final String specificMapName = dcBuffer.readString();
			if (DataClayDeserializationLib.DEBUG_ENABLED) { 
				DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Array type name deserialized: data="+  specificMapName + ", readerindex=" + dcBuffer.readerIndex());
			}
			try {
				compType = ClassLoader.getSystemClassLoader().loadClass(specificMapName);
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		} else if (compTypeByte == (byte) LanguageTypes.JAVA_ARRAY.ordinal()) {
			final byte isJava = dcBuffer.readByte();
			if (isJava == (byte) 0) {
				final String arraySignature = dcBuffer.readString();
				if (DataClayDeserializationLib.DEBUG_ENABLED) { 
					DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Array signature deserialized: data="+  arraySignature + ", readerindex=" + dcBuffer.readerIndex());
				}
				if (DataClayObject.getLib().isDSLib()) {
					compType = Reflector.getClassFromSignatureAndArray(arraySignature,
							DataClayClassLoaderSrv.execEnvironmentClassLoader);
				} else {

					final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
					compType = Reflector.getClassFromSignatureAndArray(arraySignature,
							classLoader);
				}
			} else {
				final int arrayDimension = dcBuffer.readVLQInt();
				if (DataClayDeserializationLib.DEBUG_ENABLED) { 
					DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Array dimension deserialized: data="+  arrayDimension + ", readerindex=" + dcBuffer.readerIndex());
				}
				final MetaClassID classID = new MetaClassID();
				classID.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedObjs);
				if (DataClayDeserializationLib.DEBUG_ENABLED) { 
					DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Array class id deserialized: data="+  classID + ", readerindex=" + dcBuffer.readerIndex());
				}
				compType = this.getClass(classID);
				if (arrayDimension > 0) {
					final int[] dimensions = new int[arrayDimension];
					compType = Array.newInstance(compType, dimensions).getClass();
				}
			}

		} else if (compTypeByte == (byte) LanguageTypes.DATACLAYOBJ.ordinal()) {
			final MetaClassID classID = new MetaClassID();
			classID.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedObjs);
			if (DataClayDeserializationLib.DEBUG_ENABLED) { 
				DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Array class id deserialized: data="+  classID + ", readerindex=" + dcBuffer.readerIndex());
			}
			compType = this.getClass(classID);
		}
		// Create new array
		this.objectArray = Array.newInstance(compType, arrLength);
		final boolean isPrimitiveArray = compType.isPrimitive();
		if (arrLength > 0) {
			BitSet nullsBitMap = null;
			if (!isPrimitiveArray) {
				// Read nulls bit set
				final int bitMapSize = dcBuffer.readVLQInt();
				if (DataClayDeserializationLib.DEBUG_ENABLED) { 
					DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Array bitmap size deserialized: data="+  bitMapSize + ", readerindex=" + dcBuffer.readerIndex());
				}
				final byte[] bytes = dcBuffer.readBytes(bitMapSize);
				nullsBitMap = BitSet.valueOf(bytes);
				if (DataClayDeserializationLib.DEBUG_ENABLED) { 
					DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Array bitmap deserialized: data="+  nullsBitMap + ", readerindex=" + dcBuffer.readerIndex());
				}
			}

			for (int i = 0; i < arrLength; ++i) {
				Object arrayElement = null;
				if (!isPrimitiveArray && !nullsBitMap.get(i)) {
					continue;
				}
				if (isPrimitiveArray) {
					if (compType.equals(int.class)) {
						objectArray = dcBuffer.readInts(arrLength);
						break;
					} else if (compType.equals(byte.class)) {
						objectArray = dcBuffer.readByteArray();
						break;
					} else if (compType.equals(char.class)) {
						objectArray = dcBuffer.readChars(arrLength);
						break;
					} else if (compType.equals(boolean.class)) {
						objectArray = dcBuffer.readBooleans(arrLength);
						break;
					} else if (compType.equals(short.class)) {
						objectArray = dcBuffer.readShorts(arrLength);
						break;
					} else if (compType.equals(long.class)) {
						objectArray = dcBuffer.readLongs(arrLength);
						break;
					} else if (compType.equals(float.class)) {
						objectArray = dcBuffer.readFloats(arrLength);
						break;
					} else if (compType.equals(double.class)) {
						objectArray = dcBuffer.readDoubles(arrLength);
						break;
					}
				} else {
					if (compType.equals(String.class)) {
						objectArray = dcBuffer.readStrings(arrLength);
						break;
					} else if (compType.equals(Integer.class)) {
						arrayElement = dcBuffer.readInt();
					} else if (compType.equals(Byte.class)) {
						arrayElement = dcBuffer.readByte();
					} else if (compType.equals(Character.class)) {
						arrayElement = dcBuffer.readChar();
					} else if (compType.equals(Boolean.class)) {
						arrayElement = dcBuffer.readBoolean();
					} else if (compType.equals(Short.class)) {
						arrayElement = dcBuffer.readShort();
					} else if (compType.equals(Long.class)) {
						arrayElement = dcBuffer.readLong();
					} else if (compType.equals(Float.class)) {
						arrayElement = dcBuffer.readFloat();
					} else if (compType.equals(Double.class)) {
						arrayElement = dcBuffer.readDouble();
					} else if (compType.equals(Object.class)) {
						// ==== GENERIC ==== //
						final ObjectWrapper wrapper = new ObjectWrapper();
						wrapper.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedObjs);
						arrayElement = wrapper.getJavaObject();

					} else if (compType.isArray()) {
						// ==== ARRAY ==== //
						final Integer tag = dcBuffer.readVLQInt();
						arrayElement = curDeserializedObjs.get(tag);
						if (arrayElement == null) {
							final ArrayWrapper wrapper = new ArrayWrapper();
							wrapper.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedObjs);
							arrayElement = wrapper.getJavaObject();
						}
					} else if (Collection.class.isAssignableFrom(compType)) {
						// ==== COLLECTION ==== //
						final Integer tag = dcBuffer.readVLQInt();
						arrayElement = curDeserializedObjs.get(tag);
						if (arrayElement == null) {
							final CollectionWrapper wrapper = new CollectionWrapper();
							wrapper.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedObjs);
							arrayElement = wrapper.getJavaObject();
						}
					} else if (Map.class.isAssignableFrom(compType)) {
						// ==== MAP ==== //
						final Integer tag = dcBuffer.readVLQInt();
						arrayElement = curDeserializedObjs.get(tag);
						if (arrayElement == null) {
							final MapWrapper wrapper = new MapWrapper();
							wrapper.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedObjs);
							arrayElement = wrapper.getJavaObject();
						}
					} else {
						arrayElement = DataClayDeserializationLib.deserializeAssociation(dcBuffer,
								ifaceBitMaps, metadata,
								curDeserializedObjs, DataClayObject.getLib());
					}
				}
				try {
					Array.set(objectArray, i, arrayElement);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public int hashCode() {
		return objectArray.hashCode();
	}

	@Override
	public boolean equals(final Object object) {
		if (!(object instanceof ArrayWrapper)) {
			return false;
		}
		final ArrayWrapper value = (ArrayWrapper) object;
		return Arrays.deepEquals((Object[]) value.getJavaObject(), (Object[]) objectArray);
	}

	@Override
	public boolean isImmutable() {
		return false;
	}

	@Override
	public boolean isNull() {
		return objectArray == null;
	}
}
