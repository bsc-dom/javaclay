
package es.bsc.dataclay.util.management.datacontractmgr;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import es.bsc.dataclay.util.MgrObject;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.DataContractID;
import es.bsc.dataclay.util.ids.DataSetID;

/**
 * This class represents a system data contract.
 * 
 */
public final class DataContract extends MgrObject<DataContractID> {

	// === YAML SPECIFICATION === // 
	// Properties must be public for YAML parsing.
	//CHECKSTYLE:OFF
	/** Date when contract begins. */
	private Calendar beginDate;
	/** Date when contract expires. */
	private Calendar endDate;
	/** Whether the contract is private or public. */
	private boolean publicAvailable;
	
	// ==== DYNAMIC FIELDS ==== //
	/** ID of the provider account. */
	private AccountID providerAccountID;
	/** The dataset that provides the data contract. */
	private DataSetID providerDataSetID;
	/** The ids of the accounts of the applicants to this data contract. */
	private Set<AccountID> applicantsAccountsIDs;
	//CHECKSTYLE:ON

	// TODO PrivateDataContract and PublicDataContract should be 2 different classes extending the abstract type DataContract

	/**
	 * Basic constructor
	 */
	public DataContract() {

	}

	/**
	 * Builds a new private data contract without applicants, the date this data contract is valid from and the date this
	 *        contact expires.
	 * @param providerDataSetID
	 * 			Name of provided dataset
	 * @param newProviderAccount
	 * 			the account that provides data contract
	 * @param applicantsAccountIDs
	 * 			set of account ids of data contract's applicants
	 * @param beginDateOfContract
	 * 			the date this data contract is valid from
	 * @param endDateOfContract
	 * 			the date this data contract expires
	 * @note All the interfaces must belong to the same dataset (refer to metaclasses of the same dataset)
	 */
	@Deprecated
	public DataContract(final DataSetID providerDataSetID, final AccountID providerAccountID, 
			final Calendar beginDateOfContract, final Calendar endDateOfContract) {
		this.setProviderDataSetID(providerDataSetID);
		this.setProviderAccountID(providerAccountID);
		this.setApplicantsAccountsIDs(new HashSet<>());
		this.setBeginDate(beginDateOfContract);
		this.setEndDate(endDateOfContract);
		// TODO MGR-REFACTORING: add another constructor
		this.setPublicAvailable(false);
	}
	
	/**
	 * Builds a new public data contract without applicants, the date this data contract is valid from and the date this
	 *        contact expires.
	 * @param providerDataSetID
	 * 			Name of provided dataset
	 * @param newProviderAccount
	 * 			the account that provides data contract
	 * @param applicantsAccountIDs
	 * 			set of account ids of data contract's applicants
	 * @param beginDateOfContract
	 * 			the date this data contract is valid from
	 * @param endDateOfContract
	 * 			the date this data contract expires
	 * @note All the interfaces must belong to the same dataset (refer to metaclasses of the same dataset)
	 */
	@Deprecated
	public DataContract(final DataSetID providerDataSetID, final AccountID providerAccountID,
			final Set<AccountID> applicantsAccountIDs, final Calendar beginDateOfContract, final Calendar endDateOfContract) {
		this.setProviderDataSetID(providerDataSetID);
		this.setProviderAccountID(providerAccountID);
		this.setApplicantsAccountsIDs(applicantsAccountIDs);
		this.setBeginDate(beginDateOfContract);
		this.setEndDate(endDateOfContract);
		// TODO MGR-REFACTORING: add another constructor
		this.setPublicAvailable(true);
	}

	public DataContract(final DataSetID providerDataSetID, final AccountID providerAccountID,
			final Set<AccountID> applicantsAccountIDs, final Calendar beginDateOfContract, final Calendar endDateOfContract,
			boolean publicAvailable) {
		this.setProviderDataSetID(providerDataSetID);
		this.setProviderAccountID(providerAccountID);
		this.setApplicantsAccountsIDs(applicantsAccountIDs);
		this.setBeginDate(beginDateOfContract);
		this.setEndDate(endDateOfContract);
		// TODO MGR-REFACTORING: add another constructor
		this.setPublicAvailable(publicAvailable);
	}

	/**
	 * @return the providerDataSetID
	 */
	public DataSetID getProviderDataSetID() {
		return providerDataSetID;
	}

	/**
	 * @param datasetIDofProvider
	 *            the dataset id of the provider of the data contract
	 */
	public void setProviderDataSetID(final DataSetID datasetIDofProvider) {
		this.providerDataSetID = datasetIDofProvider;
	}

	/**
	 * @return the applicantsAccountsID
	 */
	public Set<AccountID> getApplicantsAccountsIDs() {
		return applicantsAccountsIDs;
	}

	/**
	 * @param accountsIDsofTheApplicants
	 *            the applicantAccountID to set
	 */
	public void setApplicantsAccountsIDs(final Set<AccountID> accountsIDsofTheApplicants) {
		this.applicantsAccountsIDs = accountsIDsofTheApplicants;
	}

	/**
	 * @return the beginDate
	 */
	public Calendar getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDateOfContract
	 *            the beginDate to set
	 */
	public void setBeginDate(final Calendar beginDateOfContract) {
		this.beginDate = beginDateOfContract;

		// When calling getTimeInMillis, the current calendar sets the field time needed
		// to be stored and compared afterwards
		this.beginDate.getTimeInMillis();
	}

	/**
	 * @return the endDate
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * @param endDateOfContract
	 *            the endDate to set
	 */
	public void setEndDate(final Calendar endDateOfContract) {
		this.endDate = endDateOfContract;

		// When calling getTimeInMillis, the current calendar sets the field time needed
		// to be stored and compared afterwards
		this.endDate.getTimeInMillis();
	}

	/**
	 * @return the isPublic
	 */
	public boolean isPublicAvailable() {
		return publicAvailable;
	}

	/**
	 * @param isContractPublic
	 *            whether the data contract must be set as public or not
	 */
	public void setPublicAvailable(final boolean isContractPublic) {
		this.publicAvailable = isContractPublic;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		final Calendar now = Calendar.getInstance();
		return beginDate.compareTo(now) <= 0 && endDate.compareTo(now) > 0;
	}

	/**
	 * Get providerAccount
	 * @return the providerAccount
	 */
	public AccountID getProviderAccountID() {
		return providerAccountID;
	}

	/**
	 * Set providerAccount
	 * @param newproviderAccount the providerAccount to set
	 */
	public void setProviderAccountID(final AccountID newproviderAccount) {
		this.providerAccountID = newproviderAccount;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.providerAccountID, this.providerDataSetID, this.applicantsAccountsIDs, this.publicAvailable);
	}
	
	@Override
	public boolean equals(final Object other) { 
		if (other instanceof DataContract) { 
			final DataContract otherContract = (DataContract) other;
			if (this.isPublicAvailable()) { 
				if (!otherContract.isPublicAvailable()) { 
					return false;
				} else { 
					return this.providerAccountID.equals(otherContract.providerAccountID)
							&& this.providerDataSetID.equals(otherContract.providerDataSetID)
							&& this.applicantsAccountsIDs.equals(otherContract.applicantsAccountsIDs);
				}
			} else { 
				if (otherContract.isPublicAvailable()) { 
					return false;
				} else { 
					return this.providerAccountID.equals(otherContract.providerAccountID)
							&& this.providerDataSetID.equals(otherContract.providerDataSetID);
				}
			}	
		} 
		return false;
	}
	
}
