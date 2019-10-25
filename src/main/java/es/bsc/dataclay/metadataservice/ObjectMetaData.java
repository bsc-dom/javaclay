
/**
 * @file ObjectMetaData.java
 * @date May 21, 2013
 */
package es.bsc.dataclay.metadataservice;

import java.util.HashSet;
import java.util.Set;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.util.MgrObject;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;

/**
 * This class is represents the metadata of an object.
 */
public final class ObjectMetaData extends MgrObject<ObjectID> {

	/** ID of the metaclass of the object. */
	private MetaClassID metaClassID;
	/** ID of the dataset. */
	private DataSetID datasetID;
	/** IDs of the locations where the object is replicated. */
	private Set<ExecutionEnvironmentID> execEnvIDs;
	/** Indicates if the object is read only. */
	private boolean isReadOnly;
	/** User Aliases for the object. */
	private Set<String> aliases;
	/** Language of the object (important prior to starting an execution). */
	private Langs lang;
	/** Owner account ID. */
	private AccountID ownerID;

	/**
	 * ObjectMetaData constructor
	 * @param newobjectID
	 *            ID of the object
	 * @param newmetaClassID
	 *            ID of the metaclass
	 * @param newdatasetID
	 *            ID of the dataset
	 * @param newLocIDs
	 *            IDs of the locations in which the object is stored
	 * @param newisReadOnly
	 *            Indicates if the object is read only
	 * @param newaliases
	 *            Aliases for the object
	 * @param newlang
	 *            Object language
	 * @param newownerID
	 *            New owner account ID
	 */
	public ObjectMetaData(final ObjectID newobjectID, final MetaClassID newmetaClassID,
			final DataSetID newdatasetID, final Set<ExecutionEnvironmentID> newLocIDs,
			final boolean newisReadOnly, final Set<String> newaliases, final Langs newlang,
			final AccountID newownerID) {
		this.setDataClayID(newobjectID);
		if (newdatasetID == null) {
			throw new IllegalArgumentException("Bad object metadata, store dataset is null");
		}
		this.setMetaClassID(newmetaClassID);
		this.setDataSetID(newdatasetID);
		this.setExecutionEnvironmentIDs(new HashSet<>(newLocIDs));
		this.setReadOnly(newisReadOnly);
		this.setLang(newlang);
		if (newaliases == null) {
			this.setAliases(new HashSet<String>());
		} else {
			this.setAliases(new HashSet<>(newaliases));
		}
		this.setOwnerID(newownerID);
	}

	/**
	 * Get the ObjectMetaData::metaClassID
	 * @return the metaClassID
	 */

	public MetaClassID getMetaClassID() {
		return this.metaClassID;
	}

	/**
	 * Set the ObjectMetaData::metaClassID
	 * @param newmetaClassID
	 *            the metaClassID to set
	 */
	public void setMetaClassID(final MetaClassID newmetaClassID) {
		if (newmetaClassID == null) {
			throw new IllegalArgumentException("MetaClass ID cannot be null");
		}
		this.metaClassID = newmetaClassID;
	}

	/**
	 * Get the ObjectMetaData::execEnvIDs
	 * @return the Execution Environment IDs
	 */

	public Set<ExecutionEnvironmentID> getExecutionEnvironmentIDs() {
		return this.execEnvIDs;
	}

	/**
	 * Set the ObjectMetaData::execEnvIDs
	 * @param newExecEnvsIDs
	 *            the Execution Environment IDs to set
	 */
	public void setExecutionEnvironmentIDs(final Set<ExecutionEnvironmentID> newExecEnvsIDs) {
		if (newExecEnvsIDs == null) {
			throw new IllegalArgumentException("ExecutionEnvironment IDs cannot be null");
		}
		if (newExecEnvsIDs.isEmpty()) {
			throw new IllegalArgumentException("ExecutionEnvironment IDs cannot be empty");
		}
		this.execEnvIDs = newExecEnvsIDs;
	}

	/**
	 * Get the ObjectMetaData::datasetID
	 * @return the datasetID
	 */

	public DataSetID getDataSetID() {
		return this.datasetID;
	}

	/**
	 * Set the ObjectMetaData::datasetID
	 * @param newdatasetID
	 *            the datasetID to set
	 */
	public void setDataSetID(final DataSetID newdatasetID) {
		if (newdatasetID == null) {
			throw new IllegalArgumentException("DataSet ID cannot be null");
		}
		this.datasetID = newdatasetID;
	}

	/**
	 * Get the ObjectMetaData::isReadOnly
	 * @return the isReadOnly
	 */

	public boolean isReadOnly() {
		return this.isReadOnly;
	}

	/**
	 * Set the ObjectMetaData::isReadOnly
	 * @param newisReadOnly
	 *            the isReadOnly to set
	 */
	public void setReadOnly(final boolean newisReadOnly) {
		this.isReadOnly = newisReadOnly;
	}

	/**
	 * Get the ObjectMetaData::aliases
	 * @return the aliases
	 */
	public Set<String> getAliases() {
		return aliases;
	}

	/**
	 * Set the ObjectMetaData::aliases
	 * @param newaliases
	 *            the aliases to set
	 */
	public void setAliases(final Set<String> newaliases) {
		Set<String> thealiases = newaliases;
		if (newaliases == null) {
			thealiases = new HashSet<>();
		}
		if (thealiases.contains(null)) {
			throw new IllegalArgumentException("An alias cannot be null");
		}
		this.aliases = thealiases;
	}

	/**
	 * @return the ownerID
	 */
	public AccountID getOwnerID() {
		return ownerID;
	}

	/**
	 * @param theOwnerID
	 *            the ownerID to set
	 */
	public void setOwnerID(final AccountID theOwnerID) {
		this.ownerID = theOwnerID;
	}

	/**
	 * Get the ObjectMetaData::lang
	 * @return the lang property
	 */
	public Langs getLang() {
		return lang;
	}

	/**
	 * Set the ObjectMetaData::lang
	 * @param newlang
	 *            the lang to set
	 */
	public void setLang(final Langs newlang) {
		this.lang = newlang;
	}


	@Override
	public boolean equals(final Object t) {
		if (t instanceof ObjectMetaData) {
			final ObjectMetaData other = (ObjectMetaData) t;
			return other.getDataClayID().getId().equals(this.getDataClayID().getId());
		}
		return false;
	}


	@Override
	public int hashCode() {
		return this.getDataClayID().hashCode();
	}
	
	@Override
	public String toString() { 
		return "[objectID = " + this.getDataClayID() + ", classID = " + this.getMetaClassID() 
			+ ", dataSetID = " + this.getDataSetID() + ", locations = " + this.getExecutionEnvironmentIDs().toString()
			+ ", isReadOnly = " + this.isReadOnly() + ", aliases = " + this.getAliases() 
			+ ", language = " + this.getLang() + ", owner = " + this.getOwnerID() + "]";
	}
}
