
package es.bsc.dataclay.util.management.metadataservice;

import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.SessionID;

/** Includes information for registering objects. */
public final class RegistrationInfo {

	/** ID of the object. */
	private final ObjectID objectID;

	/** Class ID of the object. */
	private final MetaClassID classID;

	/** ID of session that created the object from which to get DataSet (store DataSet). */
	private final SessionID storeSessionID;

	/** Optionally, ID of dataset provided by user. */
	private final DataSetID dataSetID;

	/**
	 * Constructor
	 * @param theobjectID
	 *            ID of the object.
	 * @param theclassID
	 *            Class ID of the object.
	 * @param thesessionID
	 *            ID of session that created the object from which to get DataSet (store DataSet).
	 * @param thedataSetID
	 *            Optionally, ID of dataset provided by user.
	 */
	public RegistrationInfo(final ObjectID theobjectID, final MetaClassID theclassID,
			final SessionID thesessionID, final DataSetID thedataSetID) {
		this.objectID = theobjectID;
		this.classID = theclassID;
		this.storeSessionID = thesessionID;
		this.dataSetID = thedataSetID;
	}

	/**
	 * @return the objectID
	 */
	public ObjectID getObjectID() {
		return objectID;
	}

	/**
	 * @return the classID
	 */
	public MetaClassID getClassID() {
		return classID;
	}

	/**
	 * @return the storeSessionID
	 */
	public SessionID getStoreSessionID() {
		return storeSessionID;
	}

	/**
	 * @return the dataSetID
	 */
	public DataSetID getDataSetID() {
		return dataSetID;
	}

	@Override
	public String toString() {
		return "{oid = " + objectID + ", classID = " + classID
				+ ", storeSessionID = " + storeSessionID
				+ ", dataSetId = " + dataSetID + "}";
	}

}
