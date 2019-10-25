
package es.bsc.dataclay.util.management.contractmgr;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import es.bsc.dataclay.util.MgrObject;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * This class represents a system contract.
 */
public final class Contract extends MgrObject<ContractID> {

	// ==== YAML SPECIFICATIONS ==== //
	/** Namespace providing contract. */
	// TODO MGR-REFACTORING: delete
	private String namespace;
	/** Name of the user provider. */
	//private String providerAccountName;
	/** Date when contract begins. */
	private Calendar beginDate;
	/** Date when contract expires. */
	private Calendar endDate;
	/** Whether the contract is private or public. */
	private boolean publicAvailable;
	
	/** Set of interfaces associated to this contract. */
	private List<InterfaceInContract> interfacesInContractSpecs 
		= new ArrayList<>();

	// ==== DYNAMIC FIELDS ==== //
	/** ID of the provider account. */
	private AccountID providerAccountID;
	/** The namespace that provides the contract. */
	private NamespaceID namespaceID;
	/** The ids of the accounts of the applicants to this contract. */
	private Set<AccountID> applicantsAccountsIDs;
	/** The interfaces in contract. see @link{src.contractmgr.InterfaceInContract}. */
	private Map<InterfaceID, InterfaceInContract> interfacesInContract;

	/**
	 * Basic constructor
	 */
	public Contract() {

	}

	/**
	 * Builds a new contract
	 * @param namespaceOfProvider
	 *            the namespace of the provider of the contract
	 * @param accountOfProvider Account name of provider
	 * @param newapplicantsNames
	 *            the applicants account names
	 * @param newInterfacesInContract
	 *            the interfaces this contract can access and accessible method implementations
	 * @param beginDateOfContract
	 *            the date this contract is valid from
	 * @param endDateOfContract
	 *            the date this contract expires
	 */
	public Contract(final String namespaceOfProvider, 
			final AccountID providerID, final Set<AccountID> newapplicantsIDs, 
			final List<InterfaceInContract> newInterfacesInContract,
			final Calendar beginDateOfContract, 
			final Calendar endDateOfContract) {		
		// set the applicant account id
		this.setProviderAccountID(providerID);
		this.setApplicantsAccountsIDs(newapplicantsIDs);
		this.setNamespace(namespaceOfProvider);
		this.setInterfacesInContractSpecs(newInterfacesInContract);
		this.setInterfacesInContract(new HashMap<InterfaceID, InterfaceInContract>());
		this.setBeginDate(beginDateOfContract);
		this.setEndDate(endDateOfContract);
		this.setPublicAvailable(false);

	}

	/**
	 * Builds a new public contract
	 * @param namespaceOfProvider
	 *            the namespace of the provider of the contract
	 * @param accountOfProvider Account name of provider
	 * @param newInterfacesInContract
	 *            the interfaces this contract can access and accessible method implementations
	 * @param beginDateOfContract
	 *            the date this contract is valid from
	 * @param endDateOfContract
	 *            the date this contract expires
	 */
	public Contract(final String namespaceOfProvider, final String accountOfProvider, 
			final List<InterfaceInContract> newInterfacesInContract, 
			final Calendar beginDateOfContract,
			final Calendar endDateOfContract) {
		// set the applicant account id
		this.setNamespace(namespaceOfProvider);
		this.setApplicantsAccountsIDs(new HashSet<AccountID>());
		this.setInterfacesInContractSpecs(newInterfacesInContract);
		this.setInterfacesInContract(new HashMap<InterfaceID, InterfaceInContract>());
		this.setBeginDate(beginDateOfContract);
		this.setEndDate(endDateOfContract);
		this.setPublicAvailable(true);

	}

//	/**
//	 * Get the Contract::provider
//	 * @return the provider
//	 */
//	public String getProviderAccountName() {
//		return this.providerAccountName;
//	}
//
//	/**
//	 * Set the Contract::provider
//	 * @param newprovider the provider to set
//	 */
//	public void setProviderAccountName(final String newprovider) {
//		this.providerAccountName = newprovider;
//	}

	/**
	 * Get the Contract::namespace
	 * @return the namespace
	 */
	public String getNamespace() {
		return this.namespace;
	}

	/**
	 * Set the Contract::namespace
	 * @param newnamespace the namespace to set
	 */
	public void setNamespace(final String newnamespace) {
		this.namespace = newnamespace;
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

	//CHECKSTYLE:ON
	// ==== DYNAMIC FIELDS ==== //
	
	/**
	 * Get the Contract::interfacesInContractSpecs
	 * @return the interfacesInContractSpecs
	 */
	public List<InterfaceInContract> getInterfacesInContractSpecs() {
		return this.interfacesInContractSpecs;
	}

	/**
	 * Set the Contract::interfacesInContractSpecs
	 * @param newinterfacesInContractSpecs the interfacesInContractSpecs to set
	 */
	public void setInterfacesInContractSpecs(final List<InterfaceInContract> newinterfacesInContractSpecs) {
		this.interfacesInContractSpecs = newinterfacesInContractSpecs;
	}

	/**
	 * @return the interfacesInContract
	 */
	public Map<InterfaceID, InterfaceInContract> getInterfacesInContract() {
		return interfacesInContract;
	}

	/**
	 * @param newInterfacesInContract
	 *            the interfacesInContract to set
	 */
	public void setInterfacesInContract(final Map<InterfaceID, InterfaceInContract> newInterfacesInContract) {
		this.interfacesInContract = newInterfacesInContract;
	}

	/**
	 * Get the Contract::providerAccount
	 * @return the providerAccount
	 */
	public AccountID getProviderAccountID() {
		return this.providerAccountID;
	}

	/**
	 * Set the Contract::providerAccount
	 * @param newproviderAccount the providerAccount to set
	 */
	public void setProviderAccountID(final AccountID newproviderAccount) {
		this.providerAccountID = newproviderAccount;
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
	 *            whether the contract must be set as public or not
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
	 * Get the Contract::namespaceID
	 * @return the namespaceID
	 */
	public NamespaceID getNamespaceID() {
		return namespaceID;
	}

	/**
	 * Set the Contract::namespaceID
	 * @param newnamespaceID the namespaceID to set
	 */
	public void setNamespaceID(final NamespaceID newnamespaceID) {
		this.namespaceID = newnamespaceID;
	}
	
	@Override
	public String toString() { 
		return CommonYAML.getYamlObject().dump(this);
	}
	
	@Override
	public int hashCode() { 
//		return Objects.hash(this.providerAccountName, this.namespace, this.applicantsNames, this.publicAvailable);
		return Objects.hash(this.namespace, this.applicantsAccountsIDs, this.publicAvailable);
	}
	
	@Override
	public boolean equals(final Object other) { 
		if (other instanceof Contract) { 
			final Contract otherContract = (Contract) other;
			if (this.isPublicAvailable()) { 
				if (!otherContract.isPublicAvailable()) { 
					return false;
				} else { 
					return this.providerAccountID.equals(otherContract.getProviderAccountID())
							&& this.namespace.equals(otherContract.getNamespace())
							&& this.applicantsAccountsIDs.equals(otherContract.getApplicantsAccountsIDs());
				}
			} else { 
				if (otherContract.isPublicAvailable()) { 
					return false;
				} else { 
					return this.providerAccountID.equals(otherContract.getProviderAccountID())
							&& this.namespace.equals(otherContract.getNamespace());
				}
			}	
		} 
		return false;
	}
}
