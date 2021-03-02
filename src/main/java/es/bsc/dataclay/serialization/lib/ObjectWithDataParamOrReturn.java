
package es.bsc.dataclay.serialization.lib;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.ListIterator;
import java.util.Map;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.serialization.DataClaySerializable;
import es.bsc.dataclay.serialization.buffer.DataClayByteArray;
import es.bsc.dataclay.serialization.buffer.DataClayByteBuffer;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.ReferenceCounting;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;

/**
 * DataClay object with Data param or return (volatiles, make persistent...).
 */
public final class ObjectWithDataParamOrReturn implements DataClaySerializable {

	/** DataClayObject. */
	private DataClayObject dataClayObject;

	/** DataClayObject ID. */
	private ObjectID objectID;

	/** ID of class of the object. */
	private MetaClassID classID;

	/** MetaData of object. */
	private DataClayObjectMetaData metaData;

	/** Array of serialized bytes of object. */
	private DataClayByteArray serializedBytes;

	/**
	 * For yaml serialization.
	 */
	public ObjectWithDataParamOrReturn() {

	}

	/**
	 * Constructor
	 * @param thedataClayObject
	 *            The Object
	 */
	public ObjectWithDataParamOrReturn(final DataClayObject thedataClayObject) {
		this.dataClayObject = thedataClayObject;
		this.objectID = this.dataClayObject.getObjectID();
		this.classID = this.dataClayObject.getMetaClassID();
	}

	/**
	 * Constructor
	 * @param theObjectID
	 *            Object id
	 * @param theclassID
	 *            Class ID
	 * @param themetadata
	 *            MetaData
	 * @param thebytes
	 *            Bytes of object
	 */
	public ObjectWithDataParamOrReturn(final ObjectID theObjectID,
			final MetaClassID theclassID,
			final DataClayObjectMetaData themetadata,
			final DataClayByteArray thebytes) {
		this.serializedBytes = thebytes;
		this.objectID = theObjectID;
		this.classID = theclassID;
		this.metaData = themetadata;
	}

	@Override
	public void serialize(final DataClayByteBuffer dcBuffer,
			final boolean ignoreUserTypes, final Map<MetaClassID, byte[]> ifaceBitMaps,
			final IdentityHashMap<Object, Integer> curSerializedObjs,
			final ListIterator<DataClayObject> pendingObjs, ReferenceCounting referenceCounting) {

		// FORMAT: [ METADATA | DATA ]

		// Serialize object values
		dataClayObject.serialize(dcBuffer, ignoreUserTypes, null, curSerializedObjs, pendingObjs, referenceCounting);

		// Prepare metaData structures
		setMetaData(SerializationLibUtils.createMetaData(curSerializedObjs,
				null, 0, dataClayObject.getOriginalObjectID(),
				dataClayObject.getRootLocation(),
				dataClayObject.getOriginLocation(), dataClayObject.getReplicaLocations(),
				dataClayObject.getAlias(), dataClayObject.isReadOnly()));

	}

	@Override
	public void deserialize(final DataClayByteBuffer dcBuffer,
			final Map<MetaClassID, byte[]> ifaceBitMaps,
			final DataClayObjectMetaData metadata,
			final Map<Integer, Object> curDeserializedJavaObjs) {
		dataClayObject.deserialize(dcBuffer, ifaceBitMaps, metadata, curDeserializedJavaObjs);
	}

	/**
	 * Get object ID
	 * @return Object ID
	 */
	public ObjectID getObjectID() {
		return objectID;
	}

	/**
	 * Get dataClayObject
	 * @return DataClayObject
	 */
	public DataClayObject getDataClayObject() {
		return dataClayObject;
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
	 * @return the classID
	 */
	public MetaClassID getClassID() {
		return classID;
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
	 * Set object ID
	 * @param newObjectID
	 *            Object ID
	 */
	public void setObjectID(final ObjectID newObjectID) {
		this.objectID = newObjectID;
	}

	/**
	 * Set dataClayObject
	 * @param newdataClayObject
	 *            the dataClayObject to set
	 */
	public void setDataClayObject(final DataClayObject newdataClayObject) {
		this.dataClayObject = newdataClayObject;
	}

	/**
	 * Set classID
	 * @param newclassID
	 *            the classID to set
	 */
	public void setClassID(final MetaClassID newclassID) {
		this.classID = newclassID;
	}
	
	public String toString() { 
		StringBuilder strb = new StringBuilder();
		strb.append("{");
		strb.append("	OBJECTID : " + objectID + "\n");
		strb.append("	CLASSID : " + classID + "\n");
		strb.append("	DATA : " + Arrays.toString(serializedBytes.getByteString().toByteArray()) + "\n");
		strb.append("	METADATA : " + metaData + "\n");
		strb.append("}");
		return strb.toString();
	}

	public int hashCode() {
		return this.objectID.hashCode();
	}
}
