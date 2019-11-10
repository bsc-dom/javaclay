
package es.bsc.dataclay.serialization.lib;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import es.bsc.dataclay.DataClayExecutionObject;
import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.commonruntime.DataClayRuntime;
import es.bsc.dataclay.commonruntime.DataServiceRuntime;
import es.bsc.dataclay.communication.grpc.Utils;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages;
import es.bsc.dataclay.serialization.DataClaySerializable;
import es.bsc.dataclay.serialization.buffer.DataClayByteArray;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.java.DataClayJavaWrapper;
import es.bsc.dataclay.serialization.java.lang.BooleanWrapper;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.SessionID;

/**
 * Serialization library for communications.
 *
 */
public final class DataClayDeserializationLib {

	/** Indicates if debug is enabled. */
	public static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** Logger. */
	public static final Logger LOGGER = LogManager.getLogger("DataClayDeserializationLib");
	
	/**
	 * Constructor
	 */
	private DataClayDeserializationLib() {

	}

	/**
	 * Create buffer and deserialize
	 * 
	 * @param byteArray
	 *            Byte array
	 * @param instance
	 *            instance Instance to deserialize
	 * @param ifaceBitMaps
	 *            Map of bitmaps representing the interfaces to use
	 * @param metadata
	 *            Object metadata (e.g. for language objects)
	 * @param curDeserializedJavaObjs
	 *            Currently deserialized Java objects
	 * @return
	 */
	public static void createBufferAndDeserialize(final DataClayByteArray byteArray,
			final DataClaySerializable instance, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata, final Map<Integer, Object> curDeserializedJavaObjs) {
		final DataClayByteBuffer dcBuffer = SerializationLibUtils.newByteBuffer(byteArray);
		// Serialize object values
		try {
			instance.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedJavaObjs);
			if (DataClayDeserializationLib.DEBUG_ENABLED) { 
				DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Deserialization FINISHED: readerIndex=" + dcBuffer.readerIndex());
			}
		} finally {
			dcBuffer.release();
		}
	}

	/**
	 * Deserialize grpc message in DB
	 * 
	 * @param objectBytes
	 *            Object bytes
	 * @return Deserialized grpc message
	 */
	public static CommonMessages.PersistentObjectInDB deserializeMessageFromDB(final byte[] objectBytes) {
		// Here means that object is not loaded! (Execution Environment)
		// === LOAD OBJECT === //
		final CommonMessages.PersistentObjectInDB persObjInDB;
		try {
			persObjInDB = CommonMessages.PersistentObjectInDB.parseFrom(objectBytes);
		} catch (final InvalidProtocolBufferException e) {
			if (DEBUG_ENABLED) {
				e.printStackTrace();
			}
			throw new RuntimeException(e);
		}
		return persObjInDB;
	}

	/**
	 * Deserialize object from bytes into ObjectWithDataParamOrReturn
	 * 
	 * @param objectID
	 *            ID of object
	 * @param objectBytes
	 *            Object bytes
	 * @param theLib
	 *            Library to use
	 * @return ObjectWithDataParamOrReturn serialized.
	 */
	public static ObjectWithDataParamOrReturn deserializeObjectFromDBBytesIntoObjectData(final ObjectID objectID,
			final byte[] objectBytes, final DataClayRuntime theLib) {

		final CommonMessages.PersistentObjectInDB persObjInDB;
		try {
			persObjInDB = CommonMessages.PersistentObjectInDB.parseFrom(objectBytes);
		} catch (final InvalidProtocolBufferException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		final DataClayObjectMetaData metadata = Utils.getMetaData(persObjInDB.getMetadata());
		final DataClayByteArray byteArray = new DataClayByteArray(persObjInDB.getData());

		final ObjectWithDataParamOrReturn serializedObject = new ObjectWithDataParamOrReturn(objectID,
				metadata.getMetaClassID(0), metadata, byteArray);

		return serializedObject;
	}

	/**
	 * Deserialize object from bytes
	 * 
	 * @param objectToFill
	 *            Object to fill
	 * @param objectBytes
	 *            Object bytes
	 * @param theLib
	 *            Library to use
	 */
	public static void deserializeObjectFromDBBytes(final DataClayExecutionObject objectToFill,
			final byte[] objectBytes, final DataServiceRuntime theLib) {
		// Here means that object is not loaded! (Execution Environment)
		final CommonMessages.PersistentObjectInDB persObjInDB = deserializeMessageFromDB(objectBytes);
		final DataClayObjectMetaData metadata = Utils.getMetaData(persObjInDB.getMetadata());
		deserializeObjectFromDBBytesAux(objectToFill, metadata, persObjInDB.getData(), theLib);
	}

	/**
	 * Given metadata and data, deserialize into instance. This function is used if no class id is provided.
	 * 
	 * @param instance
	 *            Instance where object is deserialized
	 * @param metadata
	 *            Object metadata
	 * @param data
	 *            Object data
	 * @param theLib
	 *            Library to use
	 */
	public static void deserializeObjectFromDBBytesAux(final DataClayExecutionObject instance,
			final DataClayObjectMetaData metadata, final ByteString data, final DataServiceRuntime theLib) {

		final Map<Integer, Object> curDeSerJavaObjs = new HashMap<>();
		final DataClayByteArray byteArray = new DataClayByteArray(data);
		DataClayDeserializationLib.createBufferAndDeserialize(byteArray, instance, null, metadata, curDeSerJavaObjs);
		instance.setLoaded(true);
		instance.setIsPersistent(true);

		// GARBAGE COLLECTOR RACE CONDITION
		// ================================
		// If some object was cleaned and removed from GC retained refs, it does NOT
		// mean it was removed
		// from Weak references map (Heap) because we will ONLY remove an entry in Heap
		// if it is
		// pointing to NULL, i.e. the Java GC removed it.
		// So, if some execution is requested after we remove an entry from retained
		// refs (we cleaned and send
		// the object to disk), we check if the
		// object is in Heap (see executeImplementation in DS) and therefore, we created
		// a new reference
		// making impossible for Java GC to clean the reference. We will add the object
		// to retained references
		// again once it is deserialized from DB.
		theLib.retainInHeap(instance); // Force object to be in heap.

	}

	/**
	 * Deserialize MetaData of object from database
	 * 
	 * @param objectBytes
	 *            Object bytes
	 * @return Deserialized metadata
	 */
	public static DataClayObjectMetaData deserializeMetaDataFromDB(final byte[] objectBytes) {
		// Here means that object is not loaded! (Execution Environment)
		// === LOAD OBJECT === //
		final CommonMessages.PersistentObjectInDB persObjInDB = deserializeMessageFromDB(objectBytes);
		final DataClayObjectMetaData metadata = Utils.getMetaData(persObjInDB.getMetadata());
		return metadata;
	}

	/**
	 * Special method for deserializing boolean return of a Filter method.
	 * 
	 * @param serializedObjs
	 *            Return of filter method
	 * @return TRUE if filter method returns TRUE, FALSE otherwise.
	 */
	public static boolean deserializeReturnFilterMethod(final SerializedParametersOrReturn serializedObjs) {
		final ImmutableParamOrReturn immReturn = serializedObjs.getImmObjs().get(0);

		immReturn.setWrapper(new BooleanWrapper());
		DataClayDeserializationLib.createBufferAndDeserialize(immReturn.getSerializedBytes(), immReturn, null, null,
				null);
		return (boolean) immReturn.getWrapper().getJavaObject();
	}

	/**
	 * Deserialize object into a memory instance. ALSO called from executeImplementation in case of 'executions during
	 * deserialization'. THIS FUNCTION SHOULD NEVER BE CALLED FROM CLIENT SIDE.
	 * 
	 * @param paramOrRet
	 *            Param/return bytes and metadata
	 * @param object
	 *            Object in which to deserialize data.
	 * @param ifaceBitMaps
	 *            Interface bitmaps
	 * @param theLib
	 *            The client lib
	 * @param ownerSessionID
	 *            Can be null. ID of owner session of the object. Used for volatiles pending to register.
	 * @param forceDeserialization
	 *            Check if the object is loaded or not. If FALSE and loaded, then no deserialization is happening. If TRUE, then
	 *            deserialization is forced.
	 * 
	 */
	public static void deserializeObjectWithData(final ObjectWithDataParamOrReturn paramOrRet,
			final DataClayExecutionObject object, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayRuntime theLib, final SessionID ownerSessionID, final boolean forceDeserialization) {

		// FORCE DESERIALIZATION NOTE.
		// Volatiles in server can be created in two ways: received from client or in
		// some execution.
		// If created during execution, flag loaded = true, but if received, flag loaded
		// should be false until
		// the object is properly loaded. In order to control the first case,
		// constructor of the object is
		// setting loaded to true and here we set it to false. Therefore we need to
		// "force" deserialization sometimes.
		// Also happening in update of objects and cleaning in GC.

		// Lock object until deserialization in case another instance is waiting for
		// using it so much.
		theLib.lock(object.getObjectID());
		try {
			if (forceDeserialization || !object.isLoaded()) { // Double check to avoid race-conditions
				final Map<Integer, Object> curDeSerJavaObjs = new HashMap<>();
				final DataClayObjectMetaData metadata = paramOrRet.getMetaData();
				DataClayDeserializationLib.createBufferAndDeserialize(paramOrRet.getSerializedBytes(), object, null,
						metadata, curDeSerJavaObjs);
				// Now object is loaded
				object.setLoaded(true);
				object.setIsPersistent(true);
				if (ownerSessionID != null) {
					object.setOwnerSessionIDforVolatiles(ownerSessionID);
				}
			}

		} finally {
			theLib.unlock(object.getObjectID());
		}
	}

	/**
	 * Deserialize object into a non-persistent memory instance IN CLIENT It ensures the object is not marked as persistent.
	 * 
	 * @param paramOrRet
	 *            Param/return bytes and metadata
	 * @param object
	 *            Object in which to deserialize data.
	 * @param ifaceBitMaps
	 *            Interface bitmaps
	 * @param theLib
	 *            The client lib
	 * @param ownerSessionID
	 *            Can be null. ID of owner session of the object. Used for volatiles pending to register.
	 */
	public static void deserializeObjectWithDataInClient(final ObjectWithDataParamOrReturn paramOrRet,
			final DataClayObject object, final Map<MetaClassID, byte[]> ifaceBitMaps, final DataClayRuntime theLib,
			final SessionID ownerSessionID) {

		// Lock object until deserialization in case another instance is waiting for
		// using it so much.
		theLib.lock(object.getObjectID());
		try {
			final Map<Integer, Object> curDeSerJavaObjs = new HashMap<>();
			final DataClayObjectMetaData metadata = paramOrRet.getMetaData();
			DataClayDeserializationLib.createBufferAndDeserialize(paramOrRet.getSerializedBytes(), object, null,
					metadata, curDeSerJavaObjs);

			// Since deserialize association is creating associations as persistent, we
			// force the deserialization to set this flag to false (and hint to null)
			object.setIsPersistent(false);
			object.setHint(null);

		} finally {
			theLib.unlock(object.getObjectID());
		}
	}

	/**
	 * Deserialize parameters of an execution
	 * 
	 * @param serializedParamsOrReturn
	 *            Parameters or return to deserialize
	 * @param ifaceBitMaps
	 *            Interface bitmaps (for Client - DS communication)
	 * @param theLib
	 *            DataClayLib to use
	 * @return Parameters
	 */
	public static Object[] deserializeParamsOrReturn(final SerializedParametersOrReturn serializedParamsOrReturn,
			final Map<MetaClassID, byte[]> ifaceBitMaps, final DataClayRuntime theLib) {

		final int numParams = serializedParamsOrReturn.getNumParams();
		final Object[] deserializedParams = new Object[numParams];

		// ============================ VOLATILES ============================ //

		// Deserialize all volatiles first (ONLY FOR EXECUTION ENVIRONMENTS)
		final Map<Integer, ObjectWithDataParamOrReturn> volatiles = serializedParamsOrReturn.getVolatileObjs();
		boolean firstDeserialization = true;
		// All volatiles are DataClayObjects
		for (final Entry<Integer, ObjectWithDataParamOrReturn> volatileEntry : volatiles.entrySet()) {
			// TODO: move this to a private function only for EE
			// CLIENTS CANNOT DESERIALIZE VOLATILES
			if (firstDeserialization) {
				// Any method execution run during deserialization should use this map of
				// volatiles to seek
				// for the values needed.
				theLib.addVolatileUnderDeserialization(volatiles);
				firstDeserialization = false;
			}
			final Integer paramIdx = volatileEntry.getKey();
			final ObjectWithDataParamOrReturn volatileParamOrRet = volatileEntry.getValue();
			final ObjectID volatileObjID = volatileParamOrRet.getObjectID();
			if (DEBUG_ENABLED) {
				DataClayRuntime.LOGGER.debug("[##Deserialization##] Deserializing volatile server param idx = "
						+ paramIdx + " oid = " + volatileObjID);
			}

			final DataClayObject volatileObj = theLib.getOrNewAndLoadVolatile(volatileParamOrRet.getClassID(),
					volatileObjID, theLib.getHint(), volatileParamOrRet, ifaceBitMaps);

			// For makePersistent or federate methods, we must know that there is a session using the object which
			// is the one that persisted/federated. Normally, we know a client is using an object if we serialize
			// it to send to him but in that case client has created/federated the object.
			theLib.addSessionReference(volatileObj.getObjectID());

			if (paramIdx < numParams) {
				deserializedParams[paramIdx] = volatileObj;
			}

		}

		// ============================ LANGUAGE ============================ //

		// Language objects can only be Language parameters and NOT sub-objects of
		// DataClayObjects
		// since all language objectes are 'embedded' and 'copied'.
		final Map<Integer, LanguageParamOrReturn> langObjs = serializedParamsOrReturn.getLangObjs();
		for (final Entry<Integer, LanguageParamOrReturn> languageEntry : langObjs.entrySet()) {
			final Integer paramIdx = languageEntry.getKey();
			final LanguageParamOrReturn langObj = languageEntry.getValue();
			if (DEBUG_ENABLED) {
				DataClayRuntime.LOGGER
						.debug("[##Deserialization##] Deserializing language param or return " + paramIdx);
			}

			final DataClayByteArray bytes = langObj.getSerializedBytes();

			final Map<Integer, Object> curDeserializedJavaObjs = new HashMap<>();

			DataClayDeserializationLib.createBufferAndDeserialize(bytes, langObj, ifaceBitMaps, langObj.getMetaData(),
					curDeserializedJavaObjs);
			if (paramIdx < numParams) {
				deserializedParams[paramIdx] = langObj.getWrapper().getJavaObject();
			}
		}

		// ============================ IMMUTABLE ============================ //

		final Map<Integer, ImmutableParamOrReturn> immObjs = serializedParamsOrReturn.getImmObjs();
		for (final Entry<Integer, ImmutableParamOrReturn> immEntry : immObjs.entrySet()) {
			final Integer paramIdx = immEntry.getKey();
			final ImmutableParamOrReturn immObj = immEntry.getValue();
			final DataClayByteArray bytes = immObj.getSerializedBytes();
			if (DEBUG_ENABLED) {
				DataClayRuntime.LOGGER
						.debug("[##Deserialization##] Deserializing immutable param or return " + paramIdx);
			}
			DataClayDeserializationLib.createBufferAndDeserialize(bytes, immObj, ifaceBitMaps, null, null);
			if (paramIdx < numParams) {
				deserializedParams[paramIdx] = immObj.getWrapper().getJavaObject();
			}
		}

		// ============================ PERSISTENT PARAMS ============================
		// //
		final Map<Integer, PersistentParamOrReturn> persObjs = serializedParamsOrReturn.getPersistentRefs();
		for (final Entry<Integer, PersistentParamOrReturn> persEntry : persObjs.entrySet()) {
			final Integer paramIdx = persEntry.getKey();
			final PersistentParamOrReturn persParamOrReturn = persEntry.getValue();
			final ObjectID persObjID = persParamOrReturn.getObjectID();
			if (DEBUG_ENABLED) {
				DataClayRuntime.LOGGER.debug("[##Deserialization##] Deserializing persistent param or return idx = "
						+ paramIdx + " oid = " + persObjID);
			}

			// Get hint if present in Message.
			ExecutionEnvironmentID hint = null;
			if (persParamOrReturn.getHint() != null) {
				if (DEBUG_ENABLED) {
					DataClayRuntime.LOGGER.debug("[==Hint==] On deserialize persistent param or return instance "
							+ persObjID + " setting the hint : " + persParamOrReturn.getHint());
				}
				hint = persParamOrReturn.getHint();
			}
			DataClayInstanceID extDataClayID = null;
			if (persParamOrReturn.getExtDataClayID() != null) {
				if (DEBUG_ENABLED) {
					DataClayRuntime.LOGGER
							.debug("[==ExtDataClayID==] On deserialize persistent param or return instance " + persObjID
									+ " setting the extDataClayID : " + extDataClayID);
				}
				extDataClayID = persParamOrReturn.getExtDataClayID();
			}

			if (DEBUG_ENABLED) {
				DataClayRuntime.LOGGER
						.debug("== Getting/Creating persistent instance from deserialization of params or return.");
			}

			final DataClayObject persObj = theLib.getOrNewPersistentInstance(persParamOrReturn.getClassID(), persObjID,
					hint);

			// Important: getOrNewInstance might need the ClassID in case it is the first
			// time we
			// deserialize the object. In that case, getOrNewInstance is asking MetaData
			// cache or LM for
			// information which is more updated than the HINT in the message. So, if there
			// is no hint
			// set in the object, we can set the HINT in the message. If there is a hint in
			// the object,
			// it means it was obtaiend from metadata and it is more accurate.
			/*
			 * if (persObj.getHint() == null) { persObj.setHint(hint); }
			 */

			// ==== FLAGS ==== //
			persObj.setIsPersistent(true);

			if (paramIdx < numParams) {
				deserializedParams[paramIdx] = persObj;
			}
		}

		if (!firstDeserialization) {
			// remove volatiles under deserialization if exists. TODO: move this to a
			// private function only for EE
			theLib.removeVolatilesUnderDeserialization();
		}

		if (DEBUG_ENABLED) {
			DataClayRuntime.LOGGER.debug("[##Deserialization##] Deserializing of params/return finished");
		}

		return deserializedParams;

	}

	/**
	 * Deserialize java field
	 * 
	 * @param wrapper
	 *            Wrapper used for deserialization
	 * @param dcBuffer
	 *            Buffer from which to read
	 * @param ifaceBitMaps
	 *            Interface bitmaps
	 * @param metadata
	 *            DataClay object metadata
	 * @param curDeserializedObjs
	 *            Current deserialized objects
	 * @return Deserialized java object
	 */
	public static Object deserializeJavaField(final DataClayJavaWrapper wrapper, final DataClayByteBuffer dcBuffer,
			final Map<MetaClassID, byte[]> ifaceBitMaps, final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedObjs) {

		if (wrapper.isImmutable()) {
			// ======= IMMUTABLE PARAMETERS ===== //
			wrapper.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedObjs);
			return wrapper.getJavaObject();

		}
		// ====== LANGUAGE PARAMETERS ===== //

		final Integer tag = dcBuffer.readVLQInt();
		if (DataClayDeserializationLib.DEBUG_ENABLED) { 
			DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Association tag deserialized: data="+  tag + ", readerindex=" + dcBuffer.readerIndex());
		}
		Object javaObject = curDeserializedObjs.get(tag);
		if (javaObject == null) {
			wrapper.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedObjs);
			javaObject = wrapper.getJavaObject();
		} else { 
			if (DataClayDeserializationLib.DEBUG_ENABLED) { 
				DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Found obj with identity =" + System.identityHashCode(javaObject));
			}
		}
		return javaObject;
	}

	/**
	 * 
	 * @param dcBuffer
	 *            Buffer from which to read
	 * @param ifaceBitMaps
	 *            Interface bitmaps
	 * @param metadata
	 *            DataClay object metadata
	 * @param curDeserializedObjs
	 *            Current deserialized objects
	 * @param theLib
	 *            Library to use
	 * @return Deserialized association
	 */
	public static DataClayObject deserializeAssociation(final DataClayByteBuffer dcBuffer,
			final Map<MetaClassID, byte[]> ifaceBitMaps, final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedObjs, final DataClayRuntime theLib) {
		final Integer tag = new Integer(dcBuffer.readVLQInt());
		if (DataClayDeserializationLib.DEBUG_ENABLED) { 
			DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Association tag deserialized: data="+  tag + ", readerindex=" + dcBuffer.readerIndex());
		}
		final ObjectID theObjectID = metadata.getObjectID(tag);
		final MetaClassID localMetaClassID = metadata.getMetaClassID(tag);
		final ExecutionEnvironmentID hint = metadata.getHint(tag);
		DataClayObject obj = null;

		// GET INSTANCE
		if (DEBUG_ENABLED) {
			DataClayRuntime.LOGGER.debug("== Getting/Creating persistent instance from deserialization of assoc. "
					+ System.identityHashCode(obj));
		}
		obj = theLib.getOrNewPersistentInstance(localMetaClassID, theObjectID, hint); // Associations are always
																						// persistent

		/*
		 * if (hint != null) { if (DEBUG_ENABLED) { theLib.logger.debug("[==Hint==] Setting hint (deserialize association)" +
		 * " for tag " + tag + " on instance " + obj.getObjectID() + " the hint : " + theLib.getDSNameOfHint(hint)); }
		 * obj.setHint(hint); }
		 */
		curDeserializedObjs.put(tag, obj);
		if (DataClayDeserializationLib.DEBUG_ENABLED) { 
			DataClayDeserializationLib.LOGGER.debug("[Deserialization] --> Added obj with identity =" + System.identityHashCode(obj));
		}
		return obj;
	}

	/**
	 * Extract bytes representing the reference counting without deserializing it. @see DataClayDiskGC:queueReferenceCounting
	 * 
	 * @param objectBytes
	 *            Bytes of the object
	 * @return Bytes representing the reference counting.
	 */
	public static byte[] extractReferenceCounting(final byte[] objectBytes) {
		final CommonMessages.PersistentObjectInDB persObjInDB = deserializeMessageFromDB(objectBytes);
		final byte[] bytes = persObjInDB.getData().toByteArray();
		final DataClayByteArray byteArray = new DataClayByteArray(bytes);
		final DataClayByteBuffer dcBuffer = SerializationLibUtils.newByteBuffer(byteArray);
		try {
			final int indx = dcBuffer.readInt();
			dcBuffer.setReaderIndex(indx);
			final int length = bytes.length - indx;
			return dcBuffer.readBytes(length);
		} finally {
			dcBuffer.release();
		}
	}
}
