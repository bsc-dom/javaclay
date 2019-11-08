
package es.bsc.dataclay.serialization.lib;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;

/**
 * Represents a persistent param or return.
 */
public final class PersistentParamOrReturn {

	/** DataClay object being send. */
	private DataClayObject dcObject;

	/** DataClayObject ID. */
	private ObjectID objectID;

	/** Hint. */
	private ExecutionEnvironmentID hint;

	/** ID of class of the object. */
	private MetaClassID classID;

	/** External dataClay ID. */
	private DataClayInstanceID extDataClayID;

	/**
	 * For yaml deserialization
	 */
	public PersistentParamOrReturn() {

	}

	/**
	 * Constructor
	 * @param thedataClayObjectID
	 *            The Object ID
	 */
	public PersistentParamOrReturn(final ObjectID thedataClayObjectID) {
		this.objectID = thedataClayObjectID;
	}

	/**
	 * Constructor
	 * @param thedataClayObject
	 *            The Object
	 */
	public PersistentParamOrReturn(final DataClayObject thedataClayObject) {
		this.dcObject = thedataClayObject;
		this.objectID = thedataClayObject.getObjectID();
		this.hint = (ExecutionEnvironmentID) thedataClayObject.getHint();
		this.classID = thedataClayObject.getMetaClassID();
		this.extDataClayID = thedataClayObject.getExternalDataClayID();
	}

	/**
	 * Constructor
	 * @param theObjectID
	 *            The ObjectID
	 * @param thehint
	 *            Hint
	 * @param theclassID
	 *            The class id (FOR ''persistent volatiles'')
	 * @param theextDataClayID
	 *            id of the external dataClay ID where the original object resides
	 */
	public PersistentParamOrReturn(final ObjectID theObjectID,
			final ExecutionEnvironmentID thehint,
			final MetaClassID theclassID,
			final DataClayInstanceID theextDataClayID) {
		this.objectID = theObjectID;
		this.hint = thehint;
		this.classID = theclassID;
		this.extDataClayID = theextDataClayID;
	}

	/**
	 * @return the objectID
	 */
	public ObjectID getObjectID() {
		return objectID;
	}

	/**
	 * @return the dcObject
	 */
	public DataClayObject getDcObject() {
		return dcObject;
	}

	/**
	 * @return the hint
	 */
	public ExecutionEnvironmentID getHint() {
		return hint;
	}

	/**
	 * 
	 * @return class id
	 */
	public MetaClassID getClassID() {
		return classID;
	}

	/**
	 * @return extDataClayID
	 */
	public DataClayInstanceID getExtDataClayID() {
		return extDataClayID;
	}

	/**
	 * Set dcObject
	 * @param newdcObject
	 *            the dcObject to set
	 */
	public void setDcObject(final DataClayObject newdcObject) {
		this.dcObject = newdcObject;
	}

	/**
	 * Set objectID
	 * @param newobjectID
	 *            the objectID to set
	 */
	public void setObjectID(final ObjectID newobjectID) {
		this.objectID = newobjectID;
	}

	/**
	 * Set hint
	 * @param newhint
	 *            the hint to set
	 */
	public void setHint(final ExecutionEnvironmentID newhint) {
		this.hint = newhint;
	}

	/**
	 * Set classID
	 * @param newclassID
	 *            the classID to set
	 */
	public void setClassID(final MetaClassID newclassID) {
		this.classID = newclassID;
	}

	/**
	 * Set extDataClayID
	 * @param newextDataClayID
	 *            external dataClay ID to set
	 */
	public void setExtDataClayID(DataClayInstanceID newextDataClayID) {
		this.extDataClayID = newextDataClayID;
	}
	
	public String toString() { 
		StringBuilder strb = new StringBuilder();
		strb.append("OBJECTID = " + objectID + "\n");
		strb.append("CLASSID = " + classID + "\n");
		strb.append("HINT = " + hint + "\n");
		strb.append("EXTDATACLAYID = " + this.extDataClayID + "\n");
		return strb.toString();
	}

}
