
/**
 * @file SessionDataContract.java
 * @date Mar 3, 2014
 */
package es.bsc.dataclay.util.management.sessionmgr;

import java.io.Serializable;
import java.util.UUID;

import es.bsc.dataclay.util.ids.DataContractID;
import es.bsc.dataclay.util.ids.DataSetID;

/**
 * This class represents a data contract in a Session.
 * 
 */
public final class SessionDataContract implements Serializable {

	/** Serial version UID. */
	private static final long serialVersionUID = 8306819974684183939L;

	/** ID. */
	private UUID id;
	/** ID of the DataContract. */
	private DataContractID datacontractID;
	/** ID of the DataSet of the provider of the contract. */
	private DataSetID datasetOfProvider;

	/**
	 * Empty constructor for specification of requirements while validating sessions
	 */
	public SessionDataContract() {

	}

	/**
	 * Session contract constructor
	 * @param newdatacontractID
	 *            ID of the DataContract
	 * @param newdatasetOfProvider
	 *            ID of the DataSet of the provider of the data contract
	 */
	public SessionDataContract(final DataContractID newdatacontractID, final DataSetID newdatasetOfProvider) {
		this.setDataContractID(newdatacontractID);
		this.setDataSetOfProvider(newdatasetOfProvider);
	}

	/**
	 * Get the SessionDataContract::datacontractID
	 * @return the datacontractID
	 */

	public DataContractID getDataContractID() {
		return datacontractID;
	}

	/**
	 * Set the SessionDataContract::datacontractID
	 * @param newdatacontractID
	 *            the datacontractID to set
	 */
	public void setDataContractID(final DataContractID newdatacontractID) {
		this.datacontractID = newdatacontractID;
	}

	/**
	 * Get the SessionDataContract::datasetOfProvider
	 * @return the datasetOfProvider
	 */

	public DataSetID getDataSetOfProvider() {
		return datasetOfProvider;
	}

	/**
	 * Set the SessionDataContract::datasetOfProvider
	 * @param newdatasetOfProvider
	 *            the datasetOfProvider to set
	 */
	public void setDataSetOfProvider(final DataSetID newdatasetOfProvider) {
		this.datasetOfProvider = newdatasetOfProvider;
	}

	/**
	 * get id
	 * @return id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Set id
	 * @param newid
	 *            the id
	 */
	public void setId(final UUID newid) {
		this.id = newid;
	}
}
