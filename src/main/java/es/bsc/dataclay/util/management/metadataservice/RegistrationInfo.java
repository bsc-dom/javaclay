
package es.bsc.dataclay.util.management.metadataservice;

import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.SessionID;

/** Includes information for registering objects. */
public final class RegistrationInfo {

	/** ID of the object. */
	private ObjectID objectID;

	/** Class ID of the object. */
	private MetaClassID classID;

	/** ID of session that created the object from which to get DataSet (store DataSet). */
	private SessionID storeSessionID;

	/** Optionally, ID of dataset provided by user. */
	private DataSetID dataSetID;

	/** Optionally, alias of the objec to register.*/
	private String alias;


	/**
	 * Basic constructor
	 */
	public RegistrationInfo() {

	}

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
			final SessionID thesessionID, final DataSetID thedataSetID,
							final String thealias) {
		this.objectID = theobjectID;
		this.classID = theclassID;
		this.storeSessionID = thesessionID;
		this.dataSetID = thedataSetID;
		this.alias = thealias;
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

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	@Override
	public String toString() {
		return "{oid = " + objectID + ", classID = " + classID
				+ ", storeSessionID = " + storeSessionID
				+ ", dataSetId = " + dataSetID
				+ ", alias = " + alias
				+ "}";
	}

}
