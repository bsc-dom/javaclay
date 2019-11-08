
package es.bsc.dataclay.serialization.lib;

import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.DataClayExecutionObject;
import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.api.BackendID;
import es.bsc.dataclay.commonruntime.DataClayRuntime;
import es.bsc.dataclay.communication.grpc.Utils;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages;
import es.bsc.dataclay.serialization.DataClaySerializable;
import es.bsc.dataclay.serialization.buffer.DataClayByteArray;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.java.DataClayJavaWrapper;
import es.bsc.dataclay.serialization.java.LanguageTypes;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;

/**
 * Serialization library.
 *
 */
public final class DataClaySerializationLib {


	/** Indicates if debug is enabled. */
	public static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/** Logger. */
	public static final Logger LOGGER = LogManager.getLogger("DataClaySerializationLib");
	
	/**
	 * Constructor
	 */
	private DataClaySerializationLib() {

	}

	/**
	 * Create buffer and serialize
	 * 
	 * @param instance
	 *            Instance to serialize
	 * @param ignoreUserTypes
	 *            Indicates if user types found during serialization must be ignored
	 *            or not (for instance, non recursive make persistent)
	 * @param ifaceBitMaps
	 *            Map of bitmaps representing the interfaces to use
	 * @param curSerializedObjs
	 *            Current serialized objects Object -> OID tag. This structure must
	 *            be different during each serialization since OID tags are not
	 *            shared.
	 * @param pendingObjs
	 *            Pending objs.
	 * @param returnNullIfNoRefCounting
	 *            If true, return null if object does not reference any other object
	 *            except language object. This is useful during GC of not dirty
	 *            objects without references.
	 * @return Byte array.
	 */
	public static DataClayByteArray createBufferAndSerialize(final DataClaySerializable instance,
			final boolean ignoreUserTypes, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs, final ListIterator<DataClayObject> pendingObjs,
			final boolean returnNullIfNoRefCounting) {
		final DataClayByteBuffer dcBuffer = SerializationLibUtils.newByteBuffer();
		// Serialize object values
		DataClayByteArray byteArray = null;
		try {
			final ReferenceCounting referenceCounting = new ReferenceCounting();

			instance.serialize(dcBuffer, ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs,
					referenceCounting);
			if (returnNullIfNoRefCounting) {
				if (referenceCounting.getReferenceCounting().isEmpty()) {
					return null;
				}
			}
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialization FINISHED: writerIndex=" + dcBuffer.writerIndex());
			}
			final byte[] serializeObj = dcBuffer.getArray();
			byteArray = new DataClayByteArray(serializeObj);
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			dcBuffer.release();
		}
		return byteArray;
	}

	/**
	 * Serialize parameter or return
	 * 
	 * @param paramReturn
	 *            parameter or return
	 * @param it
	 *            Iterator in which to add found objects
	 * @return Byte array containing serialization.
	 */
	public static DataClayByteArray serializeParameterOrReturn(final DataClaySerializable paramReturn,
			final ListIterator<DataClayObject> it) {
		final IdentityHashMap<Object, Integer> curSerializedObjs = new IdentityHashMap<>();

		curSerializedObjs.put(paramReturn, 0); // Add "this"
		if (DataClaySerializationLib.DEBUG_ENABLED) { 
			DataClaySerializationLib.LOGGER.debug("[Serialization] --> Added obj with identity =" + System.identityHashCode(paramReturn));
		}
		final DataClayByteArray byteArray = createBufferAndSerialize(paramReturn, false, null, curSerializedObjs, it,
				false);

		return byteArray;
	}

	/**
	 * Serialize DataClayObject with data.
	 * 
	 * @param dcObject
	 *            DCObject
	 * @param clientLib
	 *            Client lib
	 * @param ignoreUserTypes
	 *            Indicates if user types inside the instance must be ignored or not
	 * @param ifaceBitMaps
	 *            Interface bitmaps.
	 * @param curIt
	 *            Pending objects
	 * @param forUpdate
	 *            Indicates whether this serialization is for an update or not
	 * @param hint
	 *            Hint to set
	 * @param forcePendingToRegister
	 *            If TRUE, object is going to be set as pending to register. Take
	 *            into account that this function is also called to serialize
	 *            objects in 'moves','replicas',... and in that case this parameter
	 *            must be FALSE to avoid overriding the actual value of the instance
	 *            (actual pending or not).
	 * @return The serialization of the given object
	 */
	public static ObjectWithDataParamOrReturn serializeDataClayObjectWithData(final DataClayObject dcObject,
			final DataClayRuntime clientLib, final boolean ignoreUserTypes, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final ListIterator<DataClayObject> curIt, final boolean forUpdate, final BackendID hint,
			final boolean forcePendingToRegister) {

		ObjectWithDataParamOrReturn volatileParamReturn = null;
		clientLib.lock(dcObject.getObjectID());
		try {
			volatileParamReturn = new ObjectWithDataParamOrReturn(dcObject);
			final IdentityHashMap<Object, Integer> curSerializedObjs = new IdentityHashMap<>();
			curSerializedObjs.put(dcObject, 0); // Add "this"
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Added obj with identity =" + System.identityHashCode(dcObject));
			}
			final DataClayByteArray byteArray = createBufferAndSerialize(volatileParamReturn, false, null,
					curSerializedObjs, curIt, false);

			// === SETTING FLAGS === //
			if (!forUpdate) {
				dcObject.setIsPersistent(true);
				dcObject.setHint(hint);
			}

			// A persistent object with hint means that is pending to register (always?)
			if (DEBUG_ENABLED) {
				DataClayRuntime.LOGGER.debug(
						"[==Hint==] Setting hint on instance " + dcObject.getObjectID()
								+ " the hint : " + clientLib.getDSNameOfHint(hint));
			}
			if (dcObject instanceof DataClayExecutionObject && forcePendingToRegister) {
				final DataClayExecutionObject execObject = (DataClayExecutionObject) dcObject;
				execObject.setPendingToRegister(true);
			}

			volatileParamReturn.setSerializedBytes(byteArray);

			if (DEBUG_ENABLED) {
				DataClayRuntime.LOGGER.debug("[##Serialization##] Unlocking " + dcObject.getObjectID());
			}

		} finally {
			clientLib.unlock(dcObject.getObjectID());
		}
		return volatileParamReturn;
	}

	/**
	 * Serialize language parameter or return
	 * 
	 * @param wrapper
	 *            Language object
	 * @param clientLib
	 *            Client lib
	 * @param langParams
	 *            [out] All language params
	 * @param volatileParams
	 *            [out] All DCObject params
	 * @param immParams
	 *            [out] All immutable params
	 * @param persParams
	 *            [out] All persistent params
	 * @param alreadySerializedParams
	 *            Current serialized params
	 * @param it
	 *            Pending objects
	 * @param idx
	 *            Parameter index
	 * @param forUpdate
	 *            Indicates whether this serialization is for an update operation
	 * @param hint
	 *            Hint to add
	 * @param ifaceBitMaps
	 *            bitmap of accessible properties (interface) per class
	 */
	private static void serializeLangParameterOrReturn(final DataClayJavaWrapper wrapper,
			final DataClayRuntime clientLib, final Map<Integer, LanguageParamOrReturn> langParams,
			final Map<Integer, ImmutableParamOrReturn> immParams,
			final Map<Integer, ObjectWithDataParamOrReturn> volatileParams,
			final Map<Integer, PersistentParamOrReturn> persParams, final Set<ObjectID> alreadySerializedParams,
			final ListIterator<DataClayObject> it, final int idx, final boolean forUpdate, final BackendID hint,
			final Map<MetaClassID, byte[]> ifaceBitMaps) {

		if (wrapper.isImmutable()) {
			// ======= IMMUTABLE PARAMETERS ===== //
			if (DEBUG_ENABLED) {
				DataClayRuntime.LOGGER.debug("[##Serialization##] Serializing immutable param idx = " + idx);
			}

			final ImmutableParamOrReturn immParamReturn = new ImmutableParamOrReturn(wrapper);
			final DataClayByteArray byteArray = serializeParameterOrReturn(immParamReturn, it);
			immParamReturn.setSerializedBytes(byteArray);

			immParams.put(idx, immParamReturn);
			return;

		}

		if (wrapper.getJavaObject() instanceof DataClayObject) {
			// ====== DCOBJECT PARAMETERS ===== //
			if (DEBUG_ENABLED) {
				DataClayRuntime.LOGGER.debug("[##Serialization##] Serializing generic param idx = " + idx);
			}

			final DataClayObject dcObject = (DataClayObject) wrapper.getJavaObject();
			if (dcObject.isPersistent()) {
				if (DEBUG_ENABLED) {
					DataClayRuntime.LOGGER
							.debug("[##Serialization##] Serializing PERSISTENT DataClayObject param idx = " + idx
									+ " oid = " + dcObject.getObjectID() + " with hint "
									+ clientLib.getDSNameOfHint(dcObject.getHint()));
				}
				// ======= PERSISTENT PARAMETERS ===== //
				final PersistentParamOrReturn persParamOrReturn = new PersistentParamOrReturn(dcObject);
				persParams.put(idx, persParamOrReturn);
			} else {
				final ObjectWithDataParamOrReturn volatileParamReturn = serializeDataClayObjectWithData(
						(DataClayObject) wrapper.getJavaObject(), clientLib, false, ifaceBitMaps, it, forUpdate, hint, true);
				volatileParams.put(idx, volatileParamReturn);
				alreadySerializedParams.add(volatileParamReturn.getObjectID());
			}

			return;
		}

		// ====== LANGUAGE PARAMETERS ===== //
		if (DEBUG_ENABLED) {
			DataClayRuntime.LOGGER.debug("[##Serialization##] Serializing language param idx = " + idx);
		}

		final LanguageParamOrReturn langParamReturn = new LanguageParamOrReturn(wrapper);
		final DataClayByteArray byteArray = serializeParameterOrReturn(langParamReturn, it);

		langParamReturn.setSerializedBytes(byteArray);

		langParams.put(idx, langParamReturn);

	}

	/**
	 * Serialize parameters or return of an execution
	 * 
	 * @param wrappedParamsOrRet
	 *            Parameters or return to serialize in wrappers
	 * @param ifaceBitMaps
	 *            Interface bitmaps (for Client - DS communication)
	 * @param runtime
	 *            Runtime, to lock serialization of volatiles and find new
	 *            volatiles.
	 * @param forUpdate
	 *            Indicates whether this serialization is for an update operation
	 * @param hint
	 *            Hint to set in volatiles
	 * @param ignoreSubObjects
	 *            Indicates if sub-objects must be ignored. Only for make
	 *            persistent.
	 * @return Serialized parameters or return
	 */
	public static SerializedParametersOrReturn serializeParamsOrReturn(
			final List<DataClaySerializable> wrappedParamsOrRet, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayRuntime runtime, final boolean forUpdate, final BackendID hint,
			final boolean ignoreSubObjects) {

		if (DEBUG_ENABLED) {
			DataClayRuntime.LOGGER.debug("[##Serialization##] Serializing params or returns");
		}

		final Set<ObjectID> alreadySerializedParams = new HashSet<>();
		final List<DataClayObject> pendingObjs = new LinkedList<>();
		final ListIterator<DataClayObject> it = pendingObjs.listIterator();
		final Map<Integer, LanguageParamOrReturn> langParams = new HashMap<>();
		final Map<Integer, ImmutableParamOrReturn> immParams = new HashMap<>();
		final Map<Integer, ObjectWithDataParamOrReturn> volatileParams = new HashMap<>();
		final Map<Integer, PersistentParamOrReturn> persParams = new HashMap<>();

		int curIdx = 0;
		for (final DataClaySerializable wrappedParamOrRet : wrappedParamsOrRet) {
			if (wrappedParamOrRet == null) {
				continue; // DCobjs are null
			}
			if (wrappedParamOrRet instanceof DataClayJavaWrapper) {
				final DataClayJavaWrapper javaWrapper = (DataClayJavaWrapper) wrappedParamOrRet;
				if (javaWrapper.isNull()) {
					continue;
				}
				// ====== LANGUAGE/IMMUTABLE/GENERIC PARAMETERS ===== //
				if (DataClaySerializationLib.DEBUG_ENABLED) { 
					DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serializing language or immutable object with identity " +  System.identityHashCode(javaWrapper));
				}
				serializeLangParameterOrReturn(javaWrapper, runtime, langParams, immParams, volatileParams, persParams,
						alreadySerializedParams, it, curIdx, forUpdate, hint, ifaceBitMaps);
			} else {

				final DataClayObject dcObject = (DataClayObject) wrappedParamOrRet;
				// ====== DCOBJECT PARAMETERS / RETURN ===== //

				// ==== session counting design ==== //
				// every time we send a dataclayobject (to client or to other node) we annotate
				// that the object is being used by
				// the sending
				// session
				// this is done to avoid gc to clean objects being used by clients (and no one
				// else).
				// why during serialization? it is done here to control all kind of
				// communications: execute, get, replica, ...
				// the idea is to only add information about objects used by sessions here.
				// Also, here we know all associations
				// from the object.
				// moreover: adding session counting here and not only cliend - ee solves a race
				// condition (explained during
				// close session in
				// EE)
				// this function is not going to do anything in case of client's runtime
				// if object is volatile or language collection, associations are also added
				runtime.addSessionReference(dcObject.getObjectID());

				if (dcObject.isPersistent()) {
					if (DataClaySerializationLib.DEBUG_ENABLED) { 
						DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serializing persistent object with identity " +  System.identityHashCode(dcObject));
					}
					// ======= PERSISTENT PARAMETERS ===== //
					final PersistentParamOrReturn persParamOrReturn = new PersistentParamOrReturn(dcObject);
					persParams.put(curIdx, persParamOrReturn);
				} else {
					if (DataClaySerializationLib.DEBUG_ENABLED) { 
						DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serializing volatile object with identity " +  System.identityHashCode(dcObject));
					}
					final ObjectWithDataParamOrReturn volatileParamReturn = serializeDataClayObjectWithData(dcObject,
							runtime, false, ifaceBitMaps, it, forUpdate, hint, true);
					volatileParams.put(curIdx, volatileParamReturn);
					alreadySerializedParams.add(volatileParamReturn.getObjectID());
				}
			}
			curIdx++;
		}

		// ======= SUB OBJECTS ===== //
		// Serialize pending objects (They are only DataClay Objects) found in
		// "serializeAssociation".
		// Verify they were not already serialized
		if (!ignoreSubObjects) {
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serializing sub-objects, size = " + pendingObjs.size());
			}
			while (it.hasPrevious()) {
				final DataClayObject pendingObj = it.previous();
				it.remove();

				// Avoid serializing same object many times.
				if (alreadySerializedParams.contains(pendingObj.getObjectID())) {
					if (DataClaySerializationLib.DEBUG_ENABLED) { 
						DataClaySerializationLib.LOGGER.debug("[Serialization] --> Found sub-object already serialized with identity " + System.identityHashCode(pendingObj));
					}
					continue;
				}

				// if object is volatile or language collection, associations are also added
				runtime.addSessionReference(pendingObj.getObjectID());

				if (pendingObj.isPersistent()) {
					if (DataClaySerializationLib.DEBUG_ENABLED) { 
						DataClaySerializationLib.LOGGER.debug("[Serialization] --> Object already persistent with id = " + System.identityHashCode(pendingObj));
					}
					// ======= PERSISTENT PARAMETERS ===== //
					final PersistentParamOrReturn persParamOrReturn = new PersistentParamOrReturn(pendingObj);
					persParams.put(curIdx, persParamOrReturn);
				} else {
					if (DataClaySerializationLib.DEBUG_ENABLED) { 
						DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serializing volatile sub-object with id = " + System.identityHashCode(pendingObj));
					}
					final ObjectWithDataParamOrReturn volatileParamReturn = serializeDataClayObjectWithData(pendingObj,
							runtime, false, ifaceBitMaps, it, forUpdate, hint, true);
					volatileParams.put(curIdx, volatileParamReturn);
					alreadySerializedParams.add(volatileParamReturn.getObjectID());
				}

				// If param was serialized, add it to already serialized, to avoid serializing
				// same object
				// many times.
				if (!alreadySerializedParams.contains(pendingObj.getObjectID())) {
					alreadySerializedParams.add(pendingObj.getObjectID());
				}
				curIdx++;
			}
		} else { 
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> NOT serializing sub-objects, size = " + pendingObjs.size());
			}
		}
		if (DataClaySerializationLib.DEBUG_ENABLED) { 
			DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialization finished");
		}

		return new SerializedParametersOrReturn(wrappedParamsOrRet.size(), immParams, langParams, volatileParams,
				persParams);
	}

	/**
	 * Serialize for DB
	 * 
	 * @param objectID
	 *            ID of object to store/update
	 * @param objectMetaData
	 *            Object metadata
	 * @param theBytes
	 *            Serialized bytes
	 * @param isStore
	 *            Serializing for a store (not update) or not.
	 * @return Serialized bytes
	 */
	public static byte[] serializeForDB(final ObjectID objectID, final DataClayObjectMetaData objectMetaData,
			final DataClayByteArray theBytes, final boolean isStore) {
		final CommonMessages.PersistentObjectInDB.Builder builder = CommonMessages.PersistentObjectInDB.newBuilder();
		builder.setData(theBytes.getByteString());
		builder.setMetadata(Utils.getMetaData(objectMetaData));
		final CommonMessages.PersistentObjectInDB msg = builder.build();
		if (Configuration.Flags.PRETTY_PRINT_MESSAGES.getBooleanValue()) {
			Utils.printMsg(msg);
		}
		return msg.toByteArray();
	}

	/**
	 * Serialize for DB
	 * 
	 * @param instance
	 *            Object to serialize
	 * @param ignoreUserTypes
	 *            Indicates if user types must be ignored
	 * @param ifaceBitMaps
	 *            Iface bitmaps
	 * @param returnNullIfNoRefCounting
	 *            If true, return null if object does not reference any other object
	 *            except language object. This is useful during GC of not dirty
	 *            objects without references.
	 * @return Serialized bytes
	 */
	public static byte[] serializeForDBGarbageCollection(final DataClayExecutionObject instance,
			final boolean ignoreUserTypes, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final boolean returnNullIfNoRefCounting) {

		final IdentityHashMap<Object, Integer> curSerializedObjs = new IdentityHashMap<>();
		final List<DataClayObject> curPendingObjs = new LinkedList<>();
		final ListIterator<DataClayObject> curIt = curPendingObjs.listIterator();

		curSerializedObjs.put(instance, 0); // Add "this"
		if (DataClaySerializationLib.DEBUG_ENABLED) { 
			DataClaySerializationLib.LOGGER.debug("[Serialization] --> Added obj with identity =" + System.identityHashCode(instance));
		}
		final DataClayByteArray byteArray = createBufferAndSerialize(instance, ignoreUserTypes, ifaceBitMaps,
				curSerializedObjs, curIt, returnNullIfNoRefCounting);
		if (returnNullIfNoRefCounting && byteArray == null) {
			return null;
		}

		// Prepare metaData structures
		final DataClayObjectMetaData mdata = SerializationLibUtils.createMetaData(curSerializedObjs, null, 0);
		return serializeForDB(instance.getObjectID(), mdata, byteArray, false);
	}

	/**
	 * Modify refs (oids) in metadata
	 * 
	 * @param objWithData
	 *            Obj with data
	 * @param originalToVersion
	 *            Oids to modify
	 * @param hintsMap
	 *            Hint of objects.
	 */
	public static void modifyMetadataOIDs(final ObjectWithDataParamOrReturn objWithData,
			final Map<ObjectID, ObjectID> originalToVersion, final Map<ObjectID, ExecutionEnvironmentID> hintsMap) {
		// Here means that object is not loaded! (Execution Environment)
		// === LOAD OBJECT === //
		final DataClayObjectMetaData metadata = objWithData.getMetaData();
		metadata.modifyOids(originalToVersion, hintsMap);
	}

	/**
	 * Serialize association to another user type.
	 * 
	 * @param element
	 *            Element association.
	 * @param dcBuffer
	 *            Buffer in which to write bytes.
	 * @param ignoreUserTypes
	 *            IgnoreUserTypes flag.
	 * @param ifaceBitMaps
	 *            Interface bitmaps.
	 * @param curSerializedObjs
	 *            Current serialized objects (see Serialization mechanism doc.)
	 * @param pendingObjs
	 *            Pending objects (see Serialization mechanism doc.)
	 * @param referenceCounting
	 *            Reference counting from this object.
	 */
	public static void serializeAssociation(final DataClayObject element, final DataClayByteBuffer dcBuffer,
			final boolean ignoreUserTypes, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs, final ListIterator<DataClayObject> pendingObjs,
			final ReferenceCounting referenceCounting) {

		Integer tag = curSerializedObjs.get(element);
		if (tag == null) {
			pendingObjs.add(element);
			tag = new Integer(curSerializedObjs.size());
			curSerializedObjs.put(element, tag);
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Added obj with identity =" + System.identityHashCode(element));
			}
		} else { 
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Found obj with identity =" + System.identityHashCode(element));
			}
		}
		dcBuffer.writeVLQInt(tag.intValue());
		if (DataClaySerializationLib.DEBUG_ENABLED) { 
			DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized association tag: data=" + tag + ", writerIndex=" + dcBuffer.writerIndex());
		}
		// update reference counting
		final ObjectID associationObjectID = element.getObjectID();
		final ExecutionEnvironmentID hint = (ExecutionEnvironmentID) element.getHint();
		referenceCounting.incrementReferenceCounting(associationObjectID, hint);
		if (DEBUG_ENABLED) {
			DataClayObject.getLib();
			DataClayRuntime.LOGGER.debug("[##Serialization##] " + " Serializing association to " + element.getObjectID()
					+ " with tag " + tag);
		}

	}

	/**
	 * Serialize java field.
	 * 
	 * @param wrapper
	 *            Java type inside a DataClayObject wrapper
	 * @param dcBuffer
	 *            Buffer in which to write bytes.
	 * @param ignoreUserTypes
	 *            IgnoreUserTypes flag.
	 * @param ifaceBitMaps
	 *            Interface bitmaps.
	 * @param curSerializedObjs
	 *            Current serialized objects (see Serialization mechanism doc.)
	 * @param pendingObjs
	 *            Pending objects (see Serialization mechanism doc.)
	 * @param referenceCounting
	 *            Reference counting from this object.
	 */
	public static void serializeJavaField(final DataClayJavaWrapper wrapper, final DataClayByteBuffer dcBuffer,
			final boolean ignoreUserTypes, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs, final ListIterator<DataClayObject> pendingObjs,
			final ReferenceCounting referenceCounting) {
		if (wrapper.isImmutable()) {
			// ======= IMMUTABLE PARAMETERS ===== //
			wrapper.serialize(dcBuffer, ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs,
					referenceCounting);
			return;

		}
		// ====== LANGUAGE PARAMETERS ===== //

		// Check if was already serialized
		final Object javaObject = wrapper.getJavaObject();
		Integer tag = curSerializedObjs.get(javaObject);
		if (tag == null) {
			tag = new Integer(curSerializedObjs.size());
			dcBuffer.writeVLQInt(tag.intValue());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized NEW association tag: data=" + tag + ", writerIndex=" + dcBuffer.writerIndex());
			}
			curSerializedObjs.put(javaObject, tag);
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Added obj with identity =" + System.identityHashCode(javaObject));
			}
			wrapper.serialize(dcBuffer, ignoreUserTypes, ifaceBitMaps, curSerializedObjs, pendingObjs,
					referenceCounting);
		} else {
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Found obj with identity =" + System.identityHashCode(javaObject));
			}
			dcBuffer.writeVLQInt(tag.intValue());
			if (DataClaySerializationLib.DEBUG_ENABLED) { 
				DataClaySerializationLib.LOGGER.debug("[Serialization] --> Serialized association tag: data=" + tag + ", writerIndex=" + dcBuffer.writerIndex());
			}
		}

	}

	/**
	 * Serialize reference counting
	 * 
	 * @param dcBuffer
	 *            Buffer in which to write bytes.
	 * @param referenceCounting
	 *            Reference counting from this object.
	 */
	public static void serializeReferenceCounting(final DataClayByteBuffer dcBuffer,
			final ReferenceCounting referenceCounting) {
		referenceCounting.serializeReferenceCounting(dcBuffer);
	}

}
