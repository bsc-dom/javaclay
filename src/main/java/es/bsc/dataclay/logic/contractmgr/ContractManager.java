
package es.bsc.dataclay.logic.contractmgr;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.dbcp2.BasicDataSource;

import es.bsc.dataclay.exceptions.dbhandler.DbObjectNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.contractmgr.AccountAlreadyRegisteredInContract;
import es.bsc.dataclay.exceptions.logicmodule.contractmgr.AccountNotRegisteredInContract;
import es.bsc.dataclay.exceptions.logicmodule.contractmgr.ContractNotActiveException;
import es.bsc.dataclay.exceptions.logicmodule.contractmgr.ContractNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.contractmgr.ContractNotPublicException;
import es.bsc.dataclay.exceptions.logicmodule.contractmgr.InterfaceNotInContractException;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.management.AbstractManager;
import es.bsc.dataclay.util.management.contractmgr.Contract;
import es.bsc.dataclay.util.management.contractmgr.InterfaceInContract;
import es.bsc.dataclay.util.management.contractmgr.OpImplementations;
import es.bsc.dataclay.util.structs.LruCache;
import es.bsc.dataclay.util.structs.Tuple;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;

/**
 * This class is responsible to manage contracts: add, remove and modify.
 * 
 */
public final class ContractManager extends AbstractManager {

	/** Db of Contracts. */
	private final ContractManagerDB contractDB;

	/** Contract cache. */
	private final LruCache<ContractID, Contract> contractCache;

	/** Contract cache by account. */
	private final LruCache<AccountID, LinkedList<Contract>> contractsOfAccountCache;

	/**
	 * Instantiates an Contract Manager that uses the Backend configuration provided.
	 * @param managerName
	 *            Manager/service name.
	 * @post Creates an Contract manager and hash initializes the backend.
	 */
	public ContractManager(final SQLiteDataSource dataSource) {
		super(dataSource);

		this.contractDB = new ContractManagerDB(dataSource);
		this.contractDB.createTables();

		// Init cache
		// this.lastContract = null;
		this.contractCache = new LruCache<>(Configuration.Flags.MAX_ENTRIES_CONTRACT_MANAGER_CACHE.getIntValue());
		this.contractsOfAccountCache = new LruCache<>(Configuration.Flags.MAX_ENTRIES_CONTRACT_MANAGER_CACHE.getIntValue());

	}

	/**
	 * Method that creates a new private contract with the provided account. The contract contains a set of Interfaces with
	 *        its accessible methods, operations and properties. The Interfaces must refer to Metaclasses of the same Namespace,
	 *        which is also provided in the method.
	 * @param newContract
	 *            Information of the contract to create
	 * @return the id of the contract
	 * @throws ParseException
	 *             parse exception.
	 */
	public ContractID newPrivateContract(final Contract newContract) throws ParseException {

		// Register it into the database
		contractDB.store(newContract);

		// Update cache
		final ContractID contractID = newContract.getDataClayID();
		this.contractCache.put(contractID, newContract);

		for (final AccountID accountIDofTheApplicant : newContract.getApplicantsAccountsIDs()) {
			if (!this.contractsOfAccountCache.containsKey(accountIDofTheApplicant)) {
				this.contractsOfAccountCache.put(accountIDofTheApplicant, new LinkedList<Contract>());
			}
			this.contractsOfAccountCache.get(accountIDofTheApplicant).add(newContract);
		}
		// this.lastContract = newContract;

		return contractID;
	}

	/**
	 * Method that creates a new public contract. The contract contains a set of Interfaces with its accessible methods,
	 *        operations and properties. The Interfaces must refer to Metaclasses of the same Namespace, which is also provided in
	 *        the method.
	 * @param newContract
	 *            Information of the contract to create
	 * @return the id of the contract
	 * @throws ParseException
	 *             parse exception.
	 */
	public ContractID newPublicContract(final Contract newContract) throws ParseException {

		// Register it into the database
		contractDB.store(newContract);

		// Update cache
		final ContractID result = newContract.getDataClayID();
		this.contractCache.put(result, newContract);
		// this.lastContract = newContract;

		return result;
	}

	/**
	 * Method that allows an account to register to a public contract.
	 * @param applicantAccountID
	 *            the id of the applicant account
	 * @param contractID
	 *            the id of the contract
	 * @throws Exception
	 *             if some exception occurs: <br>
	 *             ContractNotExistException: if the contract does not exist <br>
	 *             ContractNotPublicException: if the contract is not public <br>
	 *             ContractNotActiveException: if the contract has expired <br>
	 *             AccountAlreadyRegisteredInContract if the account is already registered to the contract
	 */
	public void registerToPublicContract(final AccountID applicantAccountID, final ContractID contractID) {
		// look for the contract in cache
		Contract contract = contractCache.get(contractID);
		if (contract == null) {
			// query the contract
			contract = contractDB.getContractByID(contractID);
		}
		// check contract exists
		if (contract == null) {
			throw new ContractNotExistException(contractID);
		} else {
			// check it is public
			if (!contract.isPublicAvailable()) {
				throw new ContractNotPublicException(contractID);
			} else {
				// check it is active
				if (!contract.isActive()) {
					throw new ContractNotActiveException(contractID);
				} else {
					// get its applicants
					final Set<AccountID> applicantsAccountsIDs = contract.getApplicantsAccountsIDs();

					// check applicant is not already registered
					if (!applicantsAccountsIDs.add(applicantAccountID)) {
						throw new AccountAlreadyRegisteredInContract(applicantAccountID, contractID);
					} else {
						// update the modified part of the contract
						contract.setApplicantsAccountsIDs(applicantsAccountsIDs);
						try {
							contractDB.updateContractsAddApplicant(contractID, applicantAccountID);

							// Update cache
							final ContractID aux = contractID;
							this.contractCache.put(aux, contract);
							for (final AccountID accountID : contract.getApplicantsAccountsIDs()) {
								if (!contractsOfAccountCache.containsKey(accountID)) {
									contractsOfAccountCache.put(accountID, new LinkedList<Contract>());
								}
								contractsOfAccountCache.get(accountID).add(contract);
							}
							// this.lastContract = contract;

						} catch (final DbObjectNotExistException e) {
							throw new ContractNotExistException(contractID);

						}
					}
				}
			}
		}
	}

	// ============= OPERATIONS FOR CHECKING ================= //

	/**
	 * This method checks whether the given contracts are active
	 * @param modelContracts
	 *            Contracts to be checked
	 * @throws Exception
	 *             if some exception occurs: <br>
	 *             ContractNotExistException: if some contract does not exist. <br>
	 *             ContractNotActiveException: if some contract is not active.
	 */
	public void checkActiveContracts(final HashSet<ContractID> modelContracts) {
		for (final ContractID contractID : modelContracts) {
			// look for the contract in cache
			Contract contract = contractCache.get(contractID);
			if (contract == null) {
				// query the contract
				contract = contractDB.getContractByID(contractID);
			}
			// check contract exists
			if (contract == null) {
				throw new ContractNotExistException(contractID);
			} else {
				// check it is active
				if (!contract.isActive()) {
					throw new ContractNotActiveException(contractID);
				}
			}
		}
	}

	/**
	 * This method checks if there is no contract associated with the provided namespace
	 * @param namespaceID
	 *            the namespace to be checked
	 * @return true if there is no contract related with the namespace, false otherwise
	 * @throws Exception
	 *             if some exception occurs
	 */
	public boolean checkNamespaceHasNoContracts(final NamespaceID namespaceID) {
		// query the contract by using a prototype
		final List<Contract> contractsInNamespace = contractDB.getContractsOfNamespace(namespaceID);
		return contractsInNamespace.isEmpty();
	}

	/**
	 * This method checks that the provided interfaceID is not accessible from any contract.
	 * @param interfaceID
	 *            the id of the interface
	 * @return true if there is no contract related with the interface, false otherwise
	 * @throws Exception
	 *             if some exception occurs
	 */
	public boolean checkInterfaceHasNoContracts(final InterfaceID interfaceID) {

		// Query contracts that access the provided interface
		final List<Contract> contracts = contractDB.getContractsContainingInterface(interfaceID);

		// Update cache
		for (final Contract contract : contracts) {
			final ContractID contractID = contract.getDataClayID();
			this.contractCache.put(contractID, contract);
			for (final AccountID accountID : contract.getApplicantsAccountsIDs()) {
				if (!contractsOfAccountCache.containsKey(accountID)) {
					contractsOfAccountCache.put(accountID, new LinkedList<Contract>());
				}
				contractsOfAccountCache.get(accountID).add(contract);
			}
		}

		return contracts.isEmpty();
	}

	/**
	 * This method checks if the provided implementation is accessible from any contract.
	 * @param implementationID
	 *            the id of the implementation
	 * @return true if there is no contract related with the implementation, false otherwise
	 * @throws Exception
	 *             if some exception occurs
	 */
	public boolean checkImplementationHasNoContracts(final ImplementationID implementationID) {
		// Init the prototype for implementationID as local implementation
		final OpImplementations opImpls = new OpImplementations();
		opImpls.setLocalImplementationID(implementationID);

		// Query the interfacesInContract that has some operation with the implementationID as local implementation
		final List<OpImplementations> opImplementations = contractDB.getOpImplementationsWithImpl(implementationID);
		if (!opImplementations.isEmpty()) {
			return false;
		}

		return true;
	}

	/**
	 * This method checks if the provided implementation is accessible from any contract of the user provided.
	 * @param implementationID
	 *            the id of the implementation
	 * @param accountID
	 *            ID of account to check.
	 * @return true if there is a contract related with the implementation an user, false otherwise
	 * @throws Exception
	 *             if some exception occurs
	 */
	public boolean checkImplementationHasContractForAccount(
			final ImplementationID implementationID, final AccountID accountID) {

		final Map<ContractID, Contract> contracts = this.getInfoOfAllActiveContractsForAccount(accountID);

		for (final Contract contract : contracts.values()) {
			for (final InterfaceInContract ifaceInCon : contract.getInterfacesInContract().values()) {
				for (final OpImplementations opImpl : ifaceInCon.getAccessibleImplementations().values()) {

					if (opImpl.getLocalImplementationID().equals(implementationID)) {
						return true;
					} else if (opImpl.getRemoteImplementationID().equals(implementationID)) {
						return true;
					}
				}
			}
		}

		return true;
	}

	/**
	 * This method checks whether the provided account is registered in the contracts provided and such contracts are still
	 *        active and contain a specific interface.
	 * @param accountID
	 *            the account ID
	 * @param interfacesInContracts
	 *            the interface to be checked for each contract
	 * @return true if the provided account is registered in all the contracts provided, they are active, and every contract
	 *         contains the corresponding interface provided
	 * @throws Exception
	 *             if some exception occurs: <br>
	 *             ContractNotExistException: if the contract does not exist
	 */
	public boolean checkInterfacesInActiveContractsForAccount(final AccountID accountID,
			final Map<ContractID, InterfaceID> interfacesInContracts) {

		for (final Entry<ContractID, InterfaceID> curEntry : interfacesInContracts.entrySet()) {
			final ContractID contractID = curEntry.getKey();
			final Contract contract = getContractAndUpdateCacheIfNecessary(contractID);

			final InterfaceID ifaceID = curEntry.getValue();
			// check the account id is associated with the contract
			// && the contract is active && it contains the corresponding interface
			if (!(contract.getApplicantsAccountsIDs().contains(accountID) && contract.isActive() && contract
					.getInterfacesInContract().containsKey(ifaceID))) {
				return false;
			}

		}
		return true;
	}

	/**
	 * This method checks whether the provided account is registered in the contracts provided and such contracts are still
	 *        active and contain a specific interface. Finally returns the info of the contracts.
	 * @param accountID
	 *            ID of the account
	 * @param interfacesInContract
	 *            the interface to be checked for each contract
	 * @return info of the contracts
	 * @throws Exception
	 *             if some exception occurs
	 */
	public Map<ContractID, Contract> checkInterfacesInActiveContractsForAccountAndReturnContractsInfo(
			final AccountID accountID, final Map<ContractID, InterfaceID> interfacesInContract) {

		final HashMap<ContractID, Contract> result = new HashMap<>();
		for (final Entry<ContractID, InterfaceID> curEntry : interfacesInContract.entrySet()) {
			final ContractID contractID = curEntry.getKey();
			final Contract contract = getContractAndUpdateCacheIfNecessary(contractID);

			final InterfaceID interfaceID = curEntry.getValue();

			// check the account id is associated with the contract
			if (!contract.getApplicantsAccountsIDs().contains(accountID)) {
				throw new AccountNotRegisteredInContract(accountID, contractID);
			}
			// check the contract is active
			if (!contract.isActive()) {
				throw new ContractNotActiveException(contractID);
			}
			// check it contains the corresponding interface
			if (!contract.getInterfacesInContract().containsKey(interfaceID)) {
				throw new InterfaceNotInContractException(interfaceID, contractID);
			}

			// Update result
			result.put(contractID, contract);
		}
		return result;
	}

	// =============== OPS FOR RETRIEVING INFO ============== //

	/**
	 * This method returns the info of a contract
	 * @param contractID
	 *            ID of the contract
	 * @return info of the contract
	 * @throws Exception
	 *             if some exception occurs: <br>
	 *             ContractNotExistException: if the contract does not exist
	 */
	public Contract getContractInfo(final ContractID contractID) {
		final Contract contract = getContractAndUpdateCacheIfNecessary(contractID);
		return contract;
	}

	/**
	 * This method returns the info of the contracts if they are still active and the given account is registered on them.
	 * @param contractsIDs
	 *            IDs of the contracts
	 * @param accountID
	 *            ID of the account that must be registered in the contract
	 * @return the information about the contracts
	 * @throws Exception
	 *             if some exception occurs: <br>
	 *             ContractNotExistException: if the contract does not exist <br>
	 *             AccountNotRegisteredInContract: if the account is not registered in the contract <br>
	 *             ContractNotActiveException: if the contract is not active
	 */
	public LinkedHashMap<ContractID, Contract> getInfoOfSomeActiveContractsForAccount(
			final List<ContractID> contractsIDs, final AccountID accountID) {

		final LinkedHashMap<ContractID, Contract> result = new LinkedHashMap<>();
		for (final ContractID contractID : contractsIDs) {
			final Contract contract = getContractAndUpdateCacheIfNecessary(contractID);

			if (!contract.getApplicantsAccountsIDs().contains(accountID)) {
				throw new AccountNotRegisteredInContract(accountID, contractID);
			}
			if (!contract.isActive()) {
				throw new ContractNotActiveException(contractID);
			}
			result.put(contractID, contract);
		}
		return result;
	}

	/**
	 * This method returns the info of all the active contracts which account is applicant of.
	 * @param accountID
	 *            ID of the account that must be registered in the contract
	 * @return the information about the contracts
	 * @throws Exception
	 *             if some exception occurs
	 */
	public Map<ContractID, Contract> getInfoOfAllActiveContractsForAccount(final AccountID accountID) {
		// Check contracts in cache
		final List<Contract> contracts;
		if (contractsOfAccountCache.containsKey(accountID)) {
			contracts = contractsOfAccountCache.get(accountID);
		} else {
			contracts = contractDB.getContractsWithApplicant(accountID);
			contractsOfAccountCache.put(accountID, new LinkedList<>(contracts));
		}

		// Update result
		final HashMap<ContractID, Contract> result = new HashMap<>();
		for (final Contract contract : contracts) {
			final ContractID contractID = contract.getDataClayID();
			if (contract.isActive()) {
				result.put(contractID, contract);
			}
		}
		return result;
	}

	/**
	 * Get info of all contracts of the namespace provided indexed by their id.
	 * @param namespaceIDofProvider
	 *            the namespace of the contract
	 * @return The info of the contracts of the account provided in the namespace provided
	 * @throws Exception
	 *             if some exception occurs
	 */
	public Map<ContractID, Contract> getContractIDsOfProvider(final NamespaceID namespaceIDofProvider) {
		final List<Contract> contracts = contractDB.getContractsOfNamespace(namespaceIDofProvider);
		final Map<ContractID, Contract> finalContracts = new HashMap<>();
		for (final Contract contract : contracts) {
			finalContracts.put(contract.getDataClayID(), contract);
		}

		return finalContracts;
	}

	/**
	 * Get info of all contracts of the namespace provided indexed by their id.
	 * @param namespaceIDofProvider
	 *            the namespace of the contract
	 * @return The info of the contracts of the account provided in the namespace provided
	 * @throws Exception
	 *             if some exception occurs
	 */
	public Map<ContractID, Contract> getPublicContractIDsOfProvider(final NamespaceID namespaceIDofProvider) {
		final List<Contract> contracts = contractDB.getContractsOfNamespace(namespaceIDofProvider);
		final Map<ContractID, Contract> finalContracts = new HashMap<>();
		for (final Contract contract : contracts) {
			if (contract.isPublicAvailable()) {
				finalContracts.put(contract.getDataClayID(), contract);
			}
		}
		return finalContracts;
	}

	/**
	 * Get all contract IDs of the user provided (as applicant)
	 * @param applicantAccountID
	 *            the applicant user
	 * @return The info of the contracts of the user (as applicant)
	 * @throws Exception
	 *             if some exception occurs
	 */
	public Map<ContractID, Contract> getContractIDsOfApplicant(final AccountID applicantAccountID) {

		final List<Contract> contracts;
		if (contractsOfAccountCache.containsKey(applicantAccountID)) {
			contracts = contractsOfAccountCache.get(applicantAccountID);
		} else {
			contracts = contractDB.getContractsWithApplicant(applicantAccountID);
		}

		final Map<ContractID, Contract> finalContracts = new HashMap<>();
		for (final Contract contract : contracts) {

			finalContracts.put(contract.getDataClayID(), contract);
		}

		return finalContracts;
	}

	/**
	 * Get all contract IDs of the user provided (as provider)
	 * @param providerAccountID
	 *            the provider user
	 * @return The info of the contracts of the user (as provider)
	 * @throws Exception
	 *             if some exception occurs
	 */
	public Map<ContractID, Contract> getContractIDsOfProvider(final AccountID providerAccountID) {

		final List<Contract> contracts = contractDB.getContractsWithProvider(providerAccountID);

		final Map<ContractID, Contract> finalContracts = new HashMap<>();
		for (final Contract contract : contracts) {
			finalContracts.put(contract.getDataClayID(), contract);
		}

		return finalContracts;
	}

	/**
	 * Get all contract IDs of the user provided (as applicant) with a namespace provider.
	 * @param applicantAccountID
	 *            the applicant user.
	 * @param namespaceIDofProvider
	 *            the ID of the namespace provider.
	 * @return The info of the contracts of the user (as applicant) with the given namespace.
	 * @throws Exception
	 *             if some exception occurs
	 */
	public Map<ContractID, Contract> getContractIDsOfApplicantWithProvider(final AccountID applicantAccountID,
			final NamespaceID namespaceIDofProvider) {

		final List<Contract> contracts = contractDB.getContractsWithApplicantAndNamespace(applicantAccountID, namespaceIDofProvider);
		final Map<ContractID, Contract> finalContracts = new HashMap<>();
		for (final Contract contract : contracts) {
			finalContracts.put(contract.getDataClayID(), contract);
		}

		return finalContracts;
	}

	/**
	 * This method checks whether the provided account is registered in the contracts provided and such contracts are still
	 *        active and contain the given interfaces. Finally, returns the information of such interfaces per contract.
	 * @param accountID
	 *            the account ID
	 * @param interfacesInContracts
	 *            the multiple interfaces per contract to be retrieved
	 * @return all the info related with the given interfaces in contract (if contracts are active and account is actually
	 *         registered on them)
	 * @throws Exception
	 *             if some exception occurs: <br>
	 *             ContractNotExistException: if the contract does not exist <br>
	 *             AccountNotRegisteredInContract: if the account is not registred in some of the contracts <br>
	 *             ContractNotActiveException: if any of the contracts is not active <br>
	 *             InterfaceNotInContractException: if any of the interfaces is not in the corresponding contract
	 */
	public Map<ContractID, InterfaceInContract> getInfoOfSubsetOfInterfacesThatAreInActiveContractsForAccount(
			final AccountID accountID, final Map<ContractID, InterfaceID> interfacesInContracts) {

		final HashMap<ContractID, InterfaceInContract> result = new HashMap<>();

		for (final Entry<ContractID, InterfaceID> curEntry : interfacesInContracts.entrySet()) {
			final ContractID contractID = curEntry.getKey();
			final InterfaceID interfaceID = curEntry.getValue();

			final Contract contract = getContractAndUpdateCacheIfNecessary(contractID);

			// check the account id is associated with the contract
			// && the contract is active && it contains the corresponding interface
			if (contract.getApplicantsAccountsIDs().contains(accountID) && contract.isActive()
					&& contract.getInterfacesInContract().containsKey(interfaceID)) {
				result.put(contractID, contract.getInterfacesInContract().get(interfaceID));
			}
		}
		return result;
	}

	/**
	 * This method checks whether the provided account is registered in the contracts provided and such contracts are still
	 *        active. Finally, returns the information of the interfaces in contract.
	 * @param accountID
	 *            the account ID
	 * @param contracts
	 *            the multiple contracts to be checked
	 * @return all the info related with the given contracts (if contracts are active and account is actually registered to them)
	 * @throws Exception
	 *             if some exception occurs: <br>
	 *             ContractNotExistException: if the contract does not exist <br>
	 *             AccountNotRegisteredInContract: if the account is not registred in some of the contracts <br>
	 *             ContractNotActiveException: if any of the contracts is not active <br>
	 */
	public Map<ContractID, Tuple<Map<InterfaceID, InterfaceInContract>, Calendar>> //
			getInfoOfMultipleContractsPerActiveContractsForAccount(final AccountID accountID, final Set<ContractID> contracts) {
		final Map<ContractID, Tuple<Map<InterfaceID, InterfaceInContract>, Calendar>> result = //
				new HashMap<>();

		for (final ContractID contractID : contracts) {
			final Contract contract = getContractAndUpdateCacheIfNecessary(contractID);
			if (!(contract.getApplicantsAccountsIDs().contains(accountID))) {
				throw new AccountNotRegisteredInContract(accountID, contractID);
			}
			if (!contract.isActive()) {
				throw new ContractNotActiveException(contractID);
			}

			result.put(contractID,
					new Tuple<>(contract.getInterfacesInContract(),
							contract.getEndDate()));
		}
		return result;
	}

	/**
	 * This method returns the information of those interfaces in (still active) contract having the given account registered
	 *        on them. If the any of the conditions is not satisfied, no error is produced but the info of such interface in
	 *        contract is not returned.
	 * @param accountID
	 *            the account ID
	 * @param interfacesInContracts
	 *            the interfaces per contract to be retrieved
	 * @return from the given interfaces in contracts, the info of those that are in active contracts and account is registered
	 * @throws Exception
	 *             if some exception occurs.
	 */
	public Map<ContractID, Tuple<Map<InterfaceID, InterfaceInContract>, Calendar>> //
			getInfoOfMultipleInterfacesPerActiveContractsForAccount(final AccountID accountID,
					final Map<ContractID, HashSet<InterfaceID>> interfacesInContracts) {

		final Map<ContractID, Tuple<Map<InterfaceID, InterfaceInContract>, Calendar>> result = //
				new HashMap<>();

		for (final Entry<ContractID, HashSet<InterfaceID>> curEntry : interfacesInContracts.entrySet()) {
			final ContractID contractID = curEntry.getKey();
			final Contract contract = getContractAndUpdateCacheIfNecessary(contractID);
			if (!(contract.getApplicantsAccountsIDs().contains(accountID))) {
				throw new AccountNotRegisteredInContract(accountID, contractID);
			}
			if (!contract.isActive()) {
				throw new ContractNotActiveException(contractID);
			}

			final HashMap<InterfaceID, InterfaceInContract> infoOfInterfacesInContract = new HashMap<>();
			for (final InterfaceID interfaceID : curEntry.getValue()) {
				// check the account id is associated with the contract
				// && the contract is active && it contains the corresponding interface

				if (!contract.getInterfacesInContract().containsKey(interfaceID)) {
					throw new InterfaceNotInContractException(interfaceID, contractID);
				}
				infoOfInterfacesInContract.put(interfaceID, contract.getInterfacesInContract().get(interfaceID));
			}
			result.put(contractID, new Tuple<Map<InterfaceID, InterfaceInContract>, Calendar>(infoOfInterfacesInContract,
					contract.getEndDate()));
		}
		return result;
	}

	// ============= OTHER =========== //

	/**
	 * Retrieves a contract and updates cache if necessary
	 * @param contractID
	 *            ID of the contract to be retrieved
	 * @return The contract with the ID passed
	 */
	private Contract getContractAndUpdateCacheIfNecessary(final ContractID contractID) {
		// Try getting contract from cache
		/*
		 * if (lastContract != null && lastContract.getID().getId().equals(contractID.getId())) { return lastContract; }
		 */

		boolean isCached = true;
		Contract contract = contractCache.get(contractID);

		if (contract == null) {
			isCached = false;

			// query the contract by ID
			contract = contractDB.getContractByID(contractID);
			if (contract == null) {
				throw new ContractNotExistException(contractID);
			}
		}

		// Update cache if necessary
		final ContractID aux = contract.getDataClayID();
		if (!isCached) {
			this.contractCache.put(aux, contract);
		}
		for (final AccountID accountID : contract.getApplicantsAccountsIDs()) {
			if (!contractsOfAccountCache.containsKey(accountID)) {
				contractsOfAccountCache.put(accountID, new LinkedList<Contract>());
			}
			contractsOfAccountCache.get(accountID).add(contract);
		}
		// this.lastContract = contract;
		return contract;
	}

	/**
	 * Method used for unit testing.
	 * @return The db handler reference of this manager.
	 */
	public ContractManagerDB getDbHandler() {
		return contractDB;
	}

	@Override
	public void cleanCaches() {
		this.contractCache.clear();
	}

}
