
package es.bsc.dataclay.serialization.lib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.commonruntime.DataClayRuntime;
import es.bsc.dataclay.communication.grpc.Utils;
import es.bsc.dataclay.serialization.buffer.DataClayByteArray;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.serialization.buffer.HeapNettyBuffer;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;

/**
 * This class contains utility functions for serialization.
 */
public final class SerializationLibUtils {

	/** Indicates if debug is enabled. */
	protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/**
	 * Utility classes should have private constructor.
	 */
	private SerializationLibUtils() {

	}

	/**
	 * Creates a new byte buffer
	 * @return New byte buffer
	 */
	public static DataClayByteBuffer newByteBuffer() {
		return new HeapNettyBuffer();

	}

	/**
	 * Creates a new byte buffer with bytes provided
	 * @param byteArray
	 *            Byte array containing bytes
	 * @return Byte buffer to read bytes.
	 */
	public static DataClayByteBuffer newByteBuffer(final DataClayByteArray byteArray) {
		return new HeapNettyBuffer(byteArray.getByteArray());

	}

	/**
	 * This function creates a metadata of an object
	 * @param curSerObjs
	 *            Currently found objects during serialization
	 * @param hintForMissing
	 *            Hint for associations without hint (for MakePersistent)
	 * @param numRefsPointingToObj
	 *            Number of references pointing to object.
	 * @param thealias Alias of the object or null if none
	 * @param origObjectID Original object id in case of new version
	 * @param rootLocation root location of the object
	 * @param theoriginLocation origin location of the object in case of replica
	 * @param thereplicaLocations IDs of locations this object was replicated to
	 * @param theisReadOnly indicates if object is read only
	 * @return MetaData created.
	 */
	public static DataClayObjectMetaData createMetaData(
			final IdentityHashMap<Object, Integer> curSerObjs,
			final ExecutionEnvironmentID hintForMissing,
			final int numRefsPointingToObj, final ObjectID origObjectID,
			final ExecutionEnvironmentID rootLocation,
			final ExecutionEnvironmentID theoriginLocation,
			final Set<ExecutionEnvironmentID> thereplicaLocations,
			final String thealias, final boolean theisReadOnly) {

		// Prepare metaData structures
		final Map<Integer, ObjectID> tagsToOids = new HashMap<>();
		final Map<Integer, MetaClassID> tagsToClassIDs = new HashMap<>();
		final Map<Integer, ExecutionEnvironmentID> tagsToHint = new HashMap<>();

		for (final Entry<Object, Integer> foundObj : curSerObjs.entrySet()) {
			if (foundObj.getKey() instanceof DataClayObject) {
				final DataClayObject obj = (DataClayObject) foundObj.getKey();
				final Integer tag = foundObj.getValue();
				final MetaClassID classID = obj.getMetaClassID();
				final ObjectID objectID = obj.getObjectID();
				final ExecutionEnvironmentID hint = (ExecutionEnvironmentID) obj.getHint();

				tagsToOids.put(tag, objectID);
				tagsToClassIDs.put(tag, classID);
				if (hint != null) {
					if (DEBUG_ENABLED) {
						DataClayObject.getLib();
						DataClayRuntime.LOGGER.debug("[==Hint==] Setting hint " + hint
								+ " association for tag " + tag);
					}
					tagsToHint.put(tag, hint);
				} else {
					if (hintForMissing != null) {
						if (DEBUG_ENABLED) {
							DataClayObject.getLib();
							DataClayRuntime.LOGGER.debug("[==Hint==] Setting hint " + hintForMissing
									+ " association for tag " + tag);
						}
						tagsToHint.put(tag, hintForMissing);
					}
				}
			}

		}
		final DataClayObjectMetaData objData = new DataClayObjectMetaData(thealias, theisReadOnly,
				tagsToOids, tagsToClassIDs, tagsToHint, numRefsPointingToObj,
				origObjectID, rootLocation, theoriginLocation, thereplicaLocations);
		return objData;
	}

	public static byte[] serializeBinary(Object o) throws IOException {
		final byte[] res;
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try (ObjectOutput out = new ObjectOutputStream(bos)) {
			out.writeObject(o);
			out.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			res = bos.toByteArray();
			bos.close();
		}

		return res;
	}

	/**
	 * Reads a binary-serialized object from a byte array
	 *
	 * @param data
	 *            containing the serialized object
	 * @return the object read from the data
	 *
	 */
	public static Object deserializeBinary(byte[] data) throws Exception {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(data); ObjectInput in = new ObjectInputStream(bis)) {
			return in.readObject();
		} catch (Exception e) {
			throw e;
		}
	}
}
