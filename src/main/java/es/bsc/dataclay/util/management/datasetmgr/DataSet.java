
/**
 * @file DataSet.java
 * @date Mar 3, 2014
 */
package es.bsc.dataclay.util.management.datasetmgr;

import es.bsc.dataclay.util.MgrObject;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.DataSetID;

/**
 * This class represents a dataset.
 * 
 */
public final class DataSet extends MgrObject<DataSetID> {

	// === YAML SPECIFICATION === // 
	// Properties must be public for YAML parsing.
	//CHECKSTYLE:OFF
	/** DataSet name. */
	private String name;
	/** Is it public or not */
	private boolean isPublic;
	
	// ==== DYNAMIC FIELDS ==== //
	/** DataSet responsible. */
	private AccountID providerAccountID;
	//CHECKSTYLE:ON
	
	/**
	 * Creates an empty dataset
	 */
	public DataSet() {

	}

	/**
	 * DataSet constructor with name and responsible
	 * @param newname
	 *            Name to be set
	 * @param newproviderAccountName
	 *            Provider account to be set
	 * @post Creates a new dataset with provided name and responsible and generates a new DataSetID.
	 */
	@Deprecated
	public DataSet(final String newname, final String newproviderAccountName, final boolean setAsPublic) {
		this.setDataClayID(new DataSetID());
		this.setName(newname);
		this.setIsPublic(setAsPublic);
	}

	/**
	 * DataSet constructor with name and responsible
	 * @param newname
	 *            Name of the dataset
	 * @param providerAccountID
	 *            ID of the responsible provider
	 * @post Creates a new dataset with provided name and responsible and generates a new DataSetID.
	 */
	public DataSet(final String name, final AccountID providerAccountID, final boolean setAsPublic) {
		this.setDataClayID(new DataSetID());
		this.setName(name);
		this.setProviderAccountID(providerAccountID);
		this.setIsPublic(setAsPublic);
	}

	public void setIsPublic(boolean setAsPublic) {
		this.isPublic = setAsPublic;
	}
	
	public boolean getIsPublic() {
		return this.isPublic;
	}

	/**
	 * Get the name of this DataSet
	 * @return DataSet::name of container DataSet.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set the DataSet::name of this DataSet
	 * @param newname
	 *            New name to be set
	 */
	public void setName(final String newname) {
		this.name = newname;
	}

	/**
	 * Get the responsible of this DataSet
	 * @return DataSet::responsible of container DataSet.
	 */
	public AccountID getProviderAccountID() {
		return this.providerAccountID;
	}

	/**
	 * Set the DataSet::responsible of this DataSet.
	 * @param newResponsible
	 *            Responsible account to set
	 */
	public void setProviderAccountID(final AccountID newResponsible) {
		this.providerAccountID = newResponsible;
	}
}
