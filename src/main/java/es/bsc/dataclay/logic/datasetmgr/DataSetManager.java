
/**
 * @file DataSetManager.java
 * @date Mar 3, 2014
 */

package es.bsc.dataclay.logic.datasetmgr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbcp2.BasicDataSource;

import es.bsc.dataclay.exceptions.logicmodule.datasetmgr.DataSetDoesNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.datasetmgr.DataSetExistsException;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.management.AbstractManager;
import es.bsc.dataclay.util.management.datasetmgr.DataSet;

/**
 * This class is responsible for managing datasets (add and remove).
 * 
 */
public final class DataSetManager extends AbstractManager {

	/** DbHandler for the management of Database. */
	private final DataSetManagerDB datasetDB;

	/**
	 * Instantiates a DataSet Manager that uses the DataSet DB in the provided path.
	 * 
	 * @param managerName
	 *            Manager/service name.
	 * @post Creates a DataSet manager and initializes the datasetDB in the path
	 *       provided.
	 */
	public DataSetManager(final BasicDataSource dataSource) {
		super(dataSource);
		this.datasetDB = new DataSetManagerDB(dataSource);
		this.datasetDB.createTables();
	}

	/**
	 * This operation creates a new dataset.
	 * 
	 * @param newDataSet
	 *            Information of the dataset to create
	 * @post A dataset with the provided name and responsible is created
	 * @return datasetID of the new dataset if the it was successfully created.
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             DataSetExistsException if the dataset already exists
	 */
	public DataSetID newDataSet(final DataSet newDataSet) {
		// Check that it does not already exist
		// (there is not another dataset with the same name, according
		// to the use case specification).
		final DataSet prototype = new DataSet();
		prototype.setName(newDataSet.getName());
		if (datasetDB.getDataSetByName(newDataSet.getName()) != null) {
			throw new DataSetExistsException(newDataSet.getName());
		}

		datasetDB.store(newDataSet);

		// Prepare result
		final DataSetID result = newDataSet.getDataClayID();

		return result;
	}

	/**
	 * Get all DataSets of account
	 * 
	 * @param accountID
	 *            ID of account
	 * @return DataSets of the account.
	 */
	public List<DataSet> getAllDataSetsOfAccount(final AccountID accountID) {
		final List<DataSet> allDataSets = datasetDB.getDataSetsWithProvider(accountID);
		return allDataSets;
	}

	/**
	 * Returns the datasetID of the specified dataset name
	 * 
	 * @param datasetName
	 *            the name of the dataset
	 * @return the datasetID of the dataset
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             DataSetDoesNotExistException: if the dataset does not exist
	 */
	public DataSetID getDataSetID(final String datasetName) {
		DataSetID datasetID = null;
		final DataSet curDataSet = datasetDB.getDataSetByName(datasetName);
		if (curDataSet == null) {
			throw new DataSetDoesNotExistException(datasetName);
		}
		// Prepare result
		datasetID = curDataSet.getDataClayID();

		return datasetID;
	}

	/**
	 * Returns the datasetIDs of the specified datasets names
	 * 
	 * @param datasetsNames
	 *            the names of the datasets
	 * @return the info related with the given datasets
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             DataSetDoesNotExistException: if a dataset does not exist
	 */
	public Map<String, DataSet> getDataSetsInfo(final Set<DataSetID> datasetIDs) {
		final HashMap<String, DataSet> result = new HashMap<>();
		for (final DataSetID dataSetID : datasetIDs) {
			final DataSet curDataSet = getDataSetInfo(dataSetID);
			result.put(curDataSet.getName(), curDataSet);
		}
		return result;
	}

	public DataSet getDataSetInfo(final DataSetID dataSetID) {
		// Try in cache
		DataSet curDataSet = null;
		// Query in db
		curDataSet = datasetDB.getDataSetByID(dataSetID);

		if (curDataSet == null) {
			throw new DataSetDoesNotExistException(dataSetID);
		}

		return curDataSet;
	}

	public DataSet getDataSetInfo(final String datasetName) {
		return datasetDB.getDataSetByName(datasetName);
	}

	/**
	 * This operation removes the indicated dataset
	 * 
	 * @param datasetID
	 *            DataSetID of the dataset to be removed
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             DataSetDoesNotExistException: if the dataset does not exist
	 * 
	 */
	public void removeDataSet(final DataSetID datasetID) {
		final DataSet dataset = datasetDB.getDataSetByID(datasetID);
		if (dataset == null) {
			throw new DataSetDoesNotExistException(datasetID);
		}
		dataset.setProviderAccountID(null); // Set the responsible to null in order to avoid removing it
		datasetDB.deleteDataSetByID(dataset.getDataClayID());

	}

	/**
	 * This operation checks whether an account is responsible for a dataset
	 * 
	 * @param responsible
	 *            AccountID of the responsible to be checked
	 * @param datasetID
	 *            ID of the dataset to be checked
	 * @return true if accountID is responsible for datasetID, false if not. <br>
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             DataSetDoesNotExistException: if the dataset does not exist
	 */
	public boolean checkDataSetResponsible(final DataSetID datasetID, final AccountID responsible) {
		DataSet dataset = datasetDB.getDataSetByID(datasetID);
		if (dataset == null) {
			throw new DataSetDoesNotExistException(datasetID);
		}
		return dataset.getProviderAccountID().equals(responsible);
	}

	/**
	 * This operation checks whether a dataset is p ublic or not
	 * 
	 * @param datasetID
	 *            ID of the dataset to be checked
	 * @return true if the dataset is public, false otherwise. <br>
	 * @throws Exception
	 *             if an exception occurs: <br>
	 *             DataSetDoesNotExistException: if the dataset does not exist
	 */
	public boolean checkDataSetIsPublic(final DataSetID datasetID) {
		DataSet dataset = datasetDB.getDataSetByID(datasetID);
		if (dataset == null) {
			throw new DataSetDoesNotExistException(datasetID);
		}

		return dataset.getIsPublic();
	}

	/**
	 * This operation retrieves all public datasets
	 * 
	 * @return set of dataset names that are public
	 */
	public Set<DataSet> getPublicDataSets() {
		return datasetDB.getPublicDataSets();
	}

	// ========= OTHER ========= //

	/**
	 * Method used for unit testing.
	 * 
	 * @return The db handler reference of this manager.
	 */
	public DataSetManagerDB getDbHandler() {
		return datasetDB;
	}

	@Override
	public void cleanCaches() {

	}

}
