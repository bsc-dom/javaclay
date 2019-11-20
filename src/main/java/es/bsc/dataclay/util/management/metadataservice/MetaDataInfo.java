
package es.bsc.dataclay.util.management.metadataservice;

import java.util.Map;

import es.bsc.dataclay.util.MgrObject;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;

/**
 * Class used to retrieve object metadata info.
 */
public final class MetaDataInfo
		extends MgrObject<ObjectID> {

	/** Whether the object is read only or not. */
	private boolean isReadOnly;

	/** DataSetID of the object. */
	private DataSetID datasetID;

	/** MetaClassID of the object. */
	private MetaClassID metaclassID;

	/** Info of [execution] environments of the object. */
	private Map<ExecutionEnvironmentID, ExecutionEnvironment> locations;

	/** User Aliases for the object. */
	private String alias;

	/** Owner account ID. */
	private AccountID ownerID;

	/**
	 * Emtpy constructor for serialization
	 */
	public MetaDataInfo() {

	}

	/**
	 * Basic constructor
	 * @param newdatasetID
	 *            the dataset ID of the object
	 * @param newmetaClassID
	 *            the class ID of the object
	 * @param readOnly
	 *            whether the object is readonly or not
	 * @param newlocations
	 *            locations of the object
	 * @param newenvironments
	 *            execution environments of object
	 * @param newAliases
	 *            aliases of the object
	 * @param newownerID
	 *            New owner id
	 */
	public MetaDataInfo(final ObjectID newdataClayID, final DataSetID newdatasetID, final MetaClassID newmetaClassID, final boolean readOnly,
			final Map<ExecutionEnvironmentID, ExecutionEnvironment> newlocations, final String newAlias, final AccountID newownerID) {
		this.setDataClayID(newdataClayID);
		this.setDatasetID(newdatasetID);
		this.setMetaclassID(newmetaClassID);
		this.setIsReadOnly(readOnly);
		this.setLocations(newlocations);
		this.setAlias(newAlias);
		this.setOwnerID(newownerID);
	}

	/**
	 * Get the MetaDataInfo::isReadOnly
	 * @return the isReadOnly
	 */
	public boolean getIsReadOnly() {
		return isReadOnly;
	}

	/**
	 * Set the MetaDataInfo::isReadOnly
	 * @param newisReadOnly
	 *            the isReadOnly to set
	 */
	public void setIsReadOnly(final boolean newisReadOnly) {
		this.isReadOnly = newisReadOnly;
	}

	/**
	 * Get the MetaDataInfo::datasetID
	 * @return the datasetID
	 */
	public DataSetID getDatasetID() {
		return datasetID;
	}

	/**
	 * Set the MetaDataInfo::datasetID
	 * @param newdatasetID
	 *            the datasetID to set
	 */
	public void setDatasetID(final DataSetID newdatasetID) {
		this.datasetID = newdatasetID;
	}

	/**
	 * Get the MetaDataInfo::metaclassID
	 * @return the metaclassID
	 */
	public MetaClassID getMetaclassID() {
		return metaclassID;
	}

	/**
	 * Set the MetaDataInfo::metaclassID
	 * @param newmetaclassID
	 *            the metaclassID to set
	 */
	public void setMetaclassID(final MetaClassID newmetaclassID) {
		this.metaclassID = newmetaclassID;
	}

	/**
	 * Get the MetaDataInfo::environments
	 * @return the environments
	 */
	public Map<ExecutionEnvironmentID, ExecutionEnvironment> getLocations() {
		return locations;
	}

	/**
	 * Set the MetaDataInfo::environments
	 * @param newlocations
	 *            the locations to set
	 */
	public void setLocations(final Map<ExecutionEnvironmentID, ExecutionEnvironment> newlocations) {
		this.locations = newlocations;
	}

	/**
	 * Get the MetaDataInfo::aliases
	 * @return the aliases
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * Set the MetaDataInfo::aliases
	 * @param newaliases
	 *            the aliases to set
	 */
	public void setAlias(final String newAlias) {
		this.alias = newAlias;
	}

	/**
	 * Get the MetaDataInfo::ownerID
	 * @return the ownerID
	 */
	public AccountID getOwnerID() {
		return ownerID;
	}

	/**
	 * Set the MetaDataInfo::ownerID
	 * @param theOwnerID
	 *            the ownerID to set
	 */
	public void setOwnerID(final AccountID theOwnerID) {
		this.ownerID = theOwnerID;
	}
}
