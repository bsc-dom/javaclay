
package es.bsc.dataclay.logic.datacontractmgr;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbcp2.BasicDataSource;

import es.bsc.dataclay.exceptions.dbhandler.DbObjectNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.datacontractmgr.AccountAlreadyHasADataContractWithProvider;
import es.bsc.dataclay.exceptions.logicmodule.datacontractmgr.AccountHasNoDataContractWithProvider;
import es.bsc.dataclay.exceptions.logicmodule.datacontractmgr.DataContractNotActiveException;
import es.bsc.dataclay.exceptions.logicmodule.datacontractmgr.DataContractNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.datacontractmgr.DataContractNotPublicException;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.DataContractID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.management.AbstractManager;
import es.bsc.dataclay.util.management.datacontractmgr.DataContract;

/**
 * This class is responsible to manage datacontracts: add, remove and modify.
 * 
 */
public final class DataContractManager extends AbstractManager {

	/** Db of DataContracts. */
	private final DataContractManagerDB datacontractDB;

	/**
	 * Instantiates an DataContract Manager that uses the Backend configuration
	 * provided.
	 * 
	 * @param managerName
	 *            Manager/service name.
	 * @post Creates an DataContract manager and hash initializes the backend.
	 */
	public DataContractManager(final BasicDataSource dataSource) {
		super(dataSource);

		this.datacontractDB = new DataContractManagerDB(dataSource);
		this.datacontractDB.createTables();
	}

	/**
	 * Method that creates a new private datacontract with the provided information.
	 * 
	 * @param newDataContract
	 *            Specifications of the datacontract
	 * @return the id of the datacontract
	 * @throws ParseException
	 *             parse exception.
	 */
	public DataContractID newPrivateDataContract(final DataContract newDataContract) throws ParseException {

		final DataSetID datasetIDofProvider = newDataContract.getProviderDataSetID();
		if (newDataContract.isPublicAvailable()) {
			for (final AccountID applicantAccountID : newDataContract.getApplicantsAccountsIDs()) {
				// Check user has no contract with the same provider
				if (checkIfApplicantAlreadyHasAContractWithProviderInternal(applicantAccountID, datasetIDofProvider)) {
					throw new AccountAlreadyHasADataContractWithProvider(applicantAccountID, datasetIDofProvider);
				}
			}
		}

		// Register it into the database
		datacontractDB.store(newDataContract);

		return newDataContract.getDataClayID();
	}

	/**
	 * Method that creates a new public datacontract.
	 * 
	 * @param newDataContract
	 *            Specifications of the datacontract
	 * @return the id of the datacontract
	 * @throws ParseException
	 *             parse exception.
	 */
	public DataContractID newPublicDataContract(final DataContract newDataContract) throws ParseException {

		// Register it into the database
		datacontractDB.store(newDataContract);

		// Update cache
		final DataContractID datacontractID = newDataContract.getDataClayID();

		return datacontractID;
	}

	/**
	 * Method that allows an account to register to a public datacontract.
	 * 
	 * @param applicantAccountID
	 *            the id of the applicant account
	 * @param datacontractID
	 *            the id of the datacontract
	 * @throws Exception
	 *             if some exception occurs: <br>
	 *             DataContractNotExistException: if the datacontract does not exist
	 *             <br>
	 *             DataContractNotPublicException: if the datacontract is not public
	 *             <br>
	 *             DataContractNotActiveException: if the datacontract has expired
	 *             <br>
	 *             AccountAlreadyRegisteredInDataContract if the account is already
	 *             registered to the datacontract
	 */
	public void registerToPublicDataContract(final AccountID applicantAccountID, final DataContractID datacontractID) {
		// look for the datacontract
		final DataContract datacontract = datacontractDB.getDataContractByID(datacontractID);

		// check datacontract exists
		if (datacontract == null) {
			throw new DataContractNotExistException(datacontractID);
		} else {
			// check it is public
			if (!datacontract.isPublicAvailable()) {
				throw new DataContractNotPublicException(datacontractID);
			} else {
				// check it is active
				if (!datacontract.isActive()) {
					throw new DataContractNotActiveException(datacontractID);
				} else {
					final DataSetID datasetIDofProvider = datacontract.getProviderDataSetID();
					// Check user has no contract with the same provider
					if (checkIfApplicantAlreadyHasAContractWithProviderInternal(applicantAccountID,
							datasetIDofProvider)) {
						throw new AccountAlreadyHasADataContractWithProvider(applicantAccountID, datasetIDofProvider);
					}

					// get its applicants
					final Set<AccountID> applicantsAccountsIDs = datacontract.getApplicantsAccountsIDs();
					applicantsAccountsIDs.add(applicantAccountID);

					// update the modified part of the datacontract
					datacontract.setApplicantsAccountsIDs(applicantsAccountsIDs);
					try {
						datacontractDB.updateDataContractsAddApplicant(datacontractID, applicantAccountID);

					} catch (final DbObjectNotExistException e) {
						throw new DataContractNotExistException(datacontractID);

					}
				}
			}
		}
	}

	// ============= OPERATIONS FOR CHECKING ================= //

	/**
	 * This method checks if there is no datacontract associated with the provided
	 * dataset
	 * 
	 * @param datasetID
	 *            the dataset to be checked
	 * @return true if there is no datacontract related with the dataset, false
	 *         otherwise
	 * @throws Exception
	 *             if some exception occurs
	 */
	public boolean checkDataSetHasNoDataContracts(final DataSetID datasetID) {
		// query the datacontract by using a prototype
		final List<DataContract> datacontractsInDataSet = datacontractDB.getDataContractsOfDataSet(datasetID);
		return datacontractsInDataSet.isEmpty();
	}

	// =============== OPS FOR RETRIEVING INFO ============== //

	/**
	 * Get all datacontracts IDs of the user provided (as applicant) with a set of
	 * datasets providers.
	 * 
	 * @param applicantAccountID
	 *            the applicant user.
	 * @param dataSetsIDs
	 *            the IDs of the datasets' providers.
	 * @return The info of the datacontracts of the user (as applicant) with the
	 *         given datasets.
	 * @throws Exception
	 *             if some exception occurs
	 */
	public Map<DataContractID, DataContract> getInfoOfSomeActiveDataContractsForAccountWithProviders(
			final AccountID applicantAccountID, final Set<DataSetID> dataSetsIDs) {
		final Map<DataContractID, DataContract> result = new HashMap<>();
		for (final DataSetID dataSetID : dataSetsIDs) {
			final DataContract curInfo = getDataContractInfoOfApplicantWithProviderInternal(applicantAccountID,
					dataSetID);
			if (!curInfo.isActive()) {
				throw new DataContractNotActiveException(curInfo.getDataClayID());
			}
			result.put(curInfo.getDataClayID(), curInfo);
		}
		return result;
	}

	/**
	 * Get info of all datacontracts of the dataset provided indexed by their id.
	 * 
	 * @param datasetIDofProvider
	 *            the dataset of the datacontract
	 * @return The info of the datacontracts of the account provided in the dataset
	 *         provided
	 * @throws Exception
	 *             if some exception occurs
	 */
	public Map<DataContractID, DataContract> getDataContractIDsOfProvider(final DataSetID datasetIDofProvider) {

		final List<DataContract> datacontracts = datacontractDB.getDataContractsOfDataSet(datasetIDofProvider);
		final Map<DataContractID, DataContract> result = new HashMap<>();
		for (final DataContract contract : datacontracts) {
			result.put(contract.getDataClayID(), contract);
		}
		return result;
	}

	/**
	 * Get info of the expected contract for a public dataset.
	 * 
	 * @param datasetIDofProvider
	 *            the dataset of the datacontract
	 * @return The info of the datacontracts of the account provided in the dataset
	 *         provided
	 * @throws Exception
	 *             if some exception occurs
	 */
	public DataContractID getPublicDataContractIDOfProvider(final DataSetID datasetIDofProvider) {

		DataContractID result = null;
		final List<DataContract> datacontracts = datacontractDB.getDataContractsOfDataSet(datasetIDofProvider);
		if (!datacontracts.isEmpty()) {
			final DataContract datacontract = datacontracts.iterator().next();
			result = datacontract.getDataClayID();
		}
		return result;
	}

	/**
	 * Get all datacontract IDs of the user provided (as applicant)
	 * 
	 * @param applicantAccountID
	 *            the applicant user
	 * @return The info of the datacontracts of the user (as applicant)
	 * @throws Exception
	 *             if some exception occurs
	 */
	public Map<DataContractID, DataContract> getDataContractIDsOfApplicant(final AccountID applicantAccountID) {

		List<DataContract> datacontracts = datacontractDB.getDataContractsWithApplicant(applicantAccountID);

		final Map<DataContractID, DataContract> result = new HashMap<>();
		for (final DataContract contract : datacontracts) {
			result.put(contract.getDataClayID(), contract);
		}
		return result;
	}

	/**
	 * Get datacontract info of the user provided (as applicant) with a dataset
	 * provider.
	 * 
	 * @param applicantAccountID
	 *            the applicant user.
	 * @param datasetIDofProvider
	 *            the ID of the dataset provider.
	 * @return The info of the datacontracts of the user (as applicant) with the
	 *         given dataset.
	 * @throws Exception
	 *             if some exception occurs
	 */
	public DataContract getDataContractInfoOfApplicantWithProvider(final AccountID applicantAccountID,
			final DataSetID datasetIDofProvider) {
		return getDataContractInfoOfApplicantWithProviderInternal(applicantAccountID, datasetIDofProvider);
	}

	// ============= OTHER =========== //

	/**
	 * Internal method to retrieve the info a contract between a certain a applicant
	 * an a certain dataset
	 * 
	 * @param applicantAccountID
	 *            ID of the applicant
	 * @param datasetIDofProvider
	 *            ID of the dataset providing the contract
	 * @return the info of the data contract between applicant and provider
	 */
	private DataContract getDataContractInfoOfApplicantWithProviderInternal(final AccountID applicantAccountID,
			final DataSetID datasetIDofProvider) {

		final List<DataContract> datacontracts = datacontractDB.getContractsWithApplicantAndDataSet(applicantAccountID,
				datasetIDofProvider);
		if (datacontracts.isEmpty()) {
			throw new AccountHasNoDataContractWithProvider(applicantAccountID, datasetIDofProvider);
		}
		final DataContractID datacontractID = datacontracts.get(0).getDataClayID();
		final Map<DataContractID, DataContract> result = new HashMap<>();
		for (final DataContract contract : datacontracts) {
			result.put(contract.getDataClayID(), contract);
		}
		return result.get(datacontractID);
	}

	/**
	 * Internal method to retrieve the info a contract between a certain a applicant
	 * an a certain dataset
	 * 
	 * @param applicantAccountID
	 *            ID of the applicant
	 * @param datasetIDofProvider
	 *            ID of the dataset providing the contract
	 * @return the info of the data contract between applicant and provider
	 */
	private boolean checkIfApplicantAlreadyHasAContractWithProviderInternal(final AccountID applicantAccountID,
			final DataSetID datasetIDofProvider) {
		final List<DataContract> datacontracts = datacontractDB.getContractsWithApplicantAndDataSet(applicantAccountID,
				datasetIDofProvider);
		return !datacontracts.isEmpty();
	}

	/**
	 * Method used for unit testing.
	 * 
	 * @return The db handler reference of this manager.
	 */
	public DataContractManagerDB getDbHandler() {
		return datacontractDB;
	}

	@Override
	public void cleanCaches() {

	}

}
