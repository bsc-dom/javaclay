
/**
 * @file LogicModule.java
 * @date Nov 16, 2012
 */
package es.bsc.dataclay.logic.api;

import java.util.*;

import es.bsc.dataclay.commonruntime.DataServiceRuntime;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.serialization.lib.SerializedParametersOrReturn;
import es.bsc.dataclay.util.events.listeners.ECA;
import es.bsc.dataclay.util.events.message.EventMessage;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.DataContractID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.ids.SessionID;
import es.bsc.dataclay.util.ids.StorageLocationID;
import es.bsc.dataclay.util.info.VersionInfo;
import es.bsc.dataclay.util.management.accountmgr.Account;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.contractmgr.Contract;
import es.bsc.dataclay.util.management.datacontractmgr.DataContract;
import es.bsc.dataclay.util.management.datasetmgr.DataSet;
import es.bsc.dataclay.util.management.interfacemgr.Interface;
import es.bsc.dataclay.util.management.metadataservice.DataClayInstance;
import es.bsc.dataclay.util.management.metadataservice.ExecutionEnvironment;
import es.bsc.dataclay.util.management.metadataservice.MetaDataInfo;
import es.bsc.dataclay.util.management.metadataservice.RegistrationInfo;
import es.bsc.dataclay.util.management.metadataservice.StorageLocation;
import es.bsc.dataclay.util.management.namespacemgr.Namespace;
import es.bsc.dataclay.util.management.sessionmgr.SessionInfo;
import es.bsc.dataclay.util.structs.Triple;
import es.bsc.dataclay.util.structs.Tuple;

/**
 * This interface define the methods of the Logic Module that can be executed remotely.
 * 
 */
public interface LogicModuleAPI {

	/**
	 * Register storage location 
	 * @param id ID of the storage location to register
	 * @param dsName Name 
	 * @param dsHostname Hostname
	 * @param dsPort Port
	 */
	void autoregisterSL(final StorageLocationID id, final String dsName, 
			final String dsHostname, final Integer dsPort);


	/**
	 * Get ID of storage location with name provided
	 * @param slName Storage Location name
	 * @return ID of the storage location
	 */
	StorageLocationID getStorageLocationID(final String slName);
	
	/**
	 * Register execution environment
	 * @param id ID of execution environment to register
	 * @param eeName Name 
	 * @param eeHostname Hostname
	 * @param eePort Port
	 * @param language Language
	 * @return Storage Location ID associated to EE
	 */
	StorageLocationID autoregisterEE(final ExecutionEnvironmentID id, final String eeName, 
			final String eeHostname, final Integer eePort, final Langs language);

	/**
	 * Unregister storage location with ID provided
	 * @param stLocID ID storage location to unregister
	 */
	void unregisterStorageLocation(final StorageLocationID stLocID);

	/**
	 * Unregister execution environment with ID provided
	 * @param execEnvID ID of execution environment to unregister
	 */
	void unregisterExecutionEnvironment(final ExecutionEnvironmentID execEnvID);

	/**
	 * Check logic module is alive.
	 */
	void checkAlive();
	
	/**
	 * Configure LogicModule to give provided address in case external dataClays require to know how
	 * to access current dataClay
	 * @param hostname Hostname to be published (given to external dataClays)
	 * @param port Port to be published
	 */
	void publishAddress(String hostname, int port);

	// ========== Generic batch operations ==========//

	/**
	 * Perform a series of new account creations, described by a YAML parameter.
	 * 
	 * @param adminID
	 *            ID of the account of the user that desires to perform the operations.
	 * @param adminCredential
	 *            Credential of the admin's account.
	 * @param yamlFile
	 *            A YAML file containing all the new users.
	 * @return A YAML file with the ID for the created users.
	 */
	byte[] performSetOfNewAccounts(final AccountID adminID, final PasswordCredential adminCredential,
			final byte[] yamlFile);

	/**
	 * Perform a series of operations, described by a YAML parameter.
	 * 
	 * @param performerID
	 *            ID of the account of the user that desires to perform the operations.
	 * @param performerCredential
	 *            Credential of the performer's account.
	 * @param yamlFile
	 *            A YAML file containing all the operations.
	 * @return A YAML file with the result for all the operations.
	 */
	byte[] performSetOfOperations(final AccountID performerID, final PasswordCredential performerCredential,
			final byte[] yamlFile);

	// ============== Account Manager ==============//

	/**
	 * This operation creates a new account in the system without admin credentials.
	 * 
	 * @param newAccount
	 *            Specifications of the account to create
	 * @return AccountID of the new account if the it was successfully created.
	 */
	AccountID newAccountNoAdmin(final Account newAccount);

	/**
	 * This operation creates a new account in the system with the provided username.
	 * 
	 * @param adminAccountID
	 *            ID of the account of the user that calls the operation
	 * @param adminCredential
	 *            Credential of the adminAccount provided
	 * @param newAccount
	 *            Specifications of the account to create
	 * @return AccountID of the new account if the it was successfully created.
	 */
	AccountID newAccount(final AccountID adminAccountID, final PasswordCredential adminCredential,
			final Account newAccount);

	/**
	 * Method that retrieves the id of an account given its name
	 * 
	 * @param accountName
	 *            the user name of the account
	 * @return the account id if the operation succeeds. null otherwise.
	 */
	AccountID getAccountID(final String accountName);

	/**
	 * The list of users registered in the system
	 * 
	 * @param adminAccountID
	 *            ID of the account of the user that calls the operation
	 * @param adminCredential
	 *            Credential of the adminAccount provided
	 * @return the IDs of the users registered in the system.
	 */
	Set<AccountID> getAccountList(final AccountID adminAccountID, final PasswordCredential adminCredential);

	// ============== Session Manager =============//

	/**
	 * Method that registeres a new session for the given account considering the provided interfaces in contract
	 * 
	 * @param accountID
	 *            ID of the account
	 * @param credential
	 *            credentials for the account
	 * @param contracts
	 *            contracts to be considered
	 * @param dataSetIDs
	 *            Accessible datasets for the session (user must have one data contract for each dataset)
	 * @param dataSetForStore
	 *            Dataset for store (dataset must be include among the dataSets)
	 * @param newsessionLang
	 *            language for the sesssion
	 * @return Information of the new session
	 */
	SessionInfo newSession(final AccountID accountID, final PasswordCredential credential,
			final Set<ContractID> contracts, final Set<DataSetID> dataSetIDs, final DataSetID dataSetForStore,
			final Langs newsessionLang);

	/**
	 * Close session.
	 * 
	 * @param sessionID
	 *            ID of session to close.
	 */
	void closeSession(final SessionID sessionID);

	/**
	 * Checks provided session and returns visible datasets
	 * 
	 * @param sessionID
	 *            ID of the session
	 * @return Visible datasets from this session.
	 */
	Tuple<Tuple<DataSetID, Set<DataSetID>>, Calendar> getInfoOfSessionForDS(final SessionID sessionID);

	// ============== Namespace Manager ==============//

	/**
	 * Method that creates a new namespace in the system.
	 * 
	 * @param accountID
	 *            the account id of the account
	 * @param credential
	 *            the credential of the account
	 * @param newNamespace
	 *            Specifications of the new namespace
	 * @return the id of the new namespace
	 */
	NamespaceID newNamespace(final AccountID accountID, final PasswordCredential credential,
			final Namespace newNamespace);

	/**
	 * This method removes a namespace from the system by checking it has no active contract associated with it, and no classes
	 * registered on it.
	 * 
	 * @param accountID
	 *            the account id either of the responsible of the namespace or the the admin account
	 * @param credential
	 *            the credential of the account
	 * @param namespaceName
	 *            the name of the namespace
	 */
	void removeNamespace(final AccountID accountID, final PasswordCredential credential, final String namespaceName);

	/**
	 * Retrieves the id of a namespace identified by name provided
	 * 
	 * @param accountID
	 *            Requester account id
	 * @param credential
	 *            Requester credential
	 * @param namespaceName
	 *            Name of the namespace
	 * @return ID of the namespace or NULL if an error happened.
	 */
	NamespaceID getNamespaceID(final AccountID accountID, final PasswordCredential credential,
			final String namespaceName);

	/**
	 * Retrieves the names of the available namespaces
	 * 
	 * @param accountID
	 *            Requester account id
	 * @param credential
	 *            Requester credential
	 * @return set of namespaces names
	 */
	Set<String> getNamespaces(final AccountID accountID, final PasswordCredential credential);

	/**
	 * Retrieves the language of a namespace identified by name provided
	 * 
	 * @param accountID
	 *            Requester account id
	 * @param credential
	 *            Requester credential
	 * @param namespaceName
	 *            Name of the namespace
	 * @return Language of the namespace or NULL if an error happened.
	 */
	Langs getNamespaceLang(final AccountID accountID, final PasswordCredential credential, final String namespaceName);

	/**
	 * Retrieves the id of a dataset of the object with ID provided.
	 * 
	 * @param sessionID
	 *            ID of session
	 * @param oid
	 *            Object ID
	 * @return ID of the dataset or NULL if an error happened.
	 * @throws Exception
	 *             if an exception occurs.
	 */
	DataSetID getObjectDataSetID(final SessionID sessionID, final ObjectID oid);

	/**
	 * Imports an interface in contract into a specific namespace
	 * 
	 * @param accountID
	 *            the account that performs the action
	 * @param credential
	 *            credentials of the account
	 * @param namespaceID
	 *            ID of namespace where interface in contract is imported
	 * @param contractID
	 *            ID of the contract
	 * @param interfaceID
	 *            ID of the interface
	 */
	void importInterface(final AccountID accountID, final PasswordCredential credential, final NamespaceID namespaceID,
			final ContractID contractID, final InterfaceID interfaceID);

	/**
	 * Imports the interfaces of the contract into a specific namespace
	 * 
	 * @param accountID
	 *            the account that performs the action
	 * @param credential
	 *            credentials of the account
	 * @param namespaceID
	 *            ID of namespace where interface in contract is imported
	 * @param contractID
	 *            ID of the contract
	 */
	void importContract(final AccountID accountID, final PasswordCredential credential, final NamespaceID namespaceID,
			final ContractID contractID);

	// ============== DataSet Manager ==============//

	/**
	 * Method that creates a new dataset in the system.
	 * 
	 * @param accountID
	 *            the account id of the account
	 * @param credential
	 *            the credential of the account
	 * @param dataset
	 *            Specifications of the dataset to create
	 * @return the id of the new dataset
	 */
	DataSetID newDataSet(final AccountID accountID, final PasswordCredential credential, final DataSet dataset);

	/**
	 * This method removes a dataset from the system by checking it has no active data contract associated with it and no
	 * objects registered in it.
	 * 
	 * @param accountID
	 *            the account id either of the responsible of the dataset or the the admin account
	 * @param credential
	 *            the credential of the account
	 * @param datasetName
	 *            the name of the dataset
	 */
	void removeDataSet(final AccountID accountID, final PasswordCredential credential, final String datasetName);

	/**
	 * Retrieves the id of a dataset identified by name provided
	 * 
	 * @param accountID
	 *            ID of the account responsible of the given dataset
	 * @param credential
	 *            Credential of the account
	 * @param datasetName
	 *            Name of the dataset
	 * @return ID of the namespace or NULL if an error happened.
	 */
	DataSetID getDataSetID(final AccountID accountID, final PasswordCredential credential, final String datasetName);

	/**
	 * Checks whether the dataset is public or not
	 * 
	 * @param dataSetID
	 *            ID of the dataset to be chcecked
	 * @return true if the dataset is public, false otherwise.
	 */
	boolean checkDataSetIsPublic(final DataSetID dataSetID);

	/**
	 * Retrieves the publicly available datasets
	 * 
	 * @param accountID
	 *            ID of the account
	 * @param credential
	 *            Credential of the account
	 * @return set of public dataset names
	 */
	Set<String> getPublicDataSets(final AccountID accountID, final PasswordCredential credential);

	/**
	 * Retrieves the datasets provided by the given account
	 * 
	 * @param accountID
	 *            ID of the account
	 * @param credential
	 *            Credential of the account
	 * @return set of names of account's datasets
	 */
	Set<String> getAccountDataSets(final AccountID accountID, final PasswordCredential credential);

	// ============== Class Manager ==============//

	/**
	 * This operation creates a new metaclass in the system with the provided specifications and associate it to the Namespace
	 * provided.
	 * 
	 * @param accountID
	 *            ID of the account of the user that calls the operation
	 * @param credential
	 *            Credential of the account provided
	 * @param language
	 *            Language of the classes provided
	 * @param newClasses
	 *            Specifications of classes to create
	 * @return info of the new MetaClass if it was successfully created.
	 */
	Map<String, MetaClass> newClass(final AccountID accountID, final PasswordCredential credential,
			final Langs language, final Map<String, MetaClass> newClasses);

	/**
	 * This operation creates a new metaclass in the system with the provided specifications and associate it to the Namespace
	 * provided.
	 * 
	 * @param accountID
	 *            ID of the account of the user that calls the operation
	 * @param credential
	 *            Credential of the account provided
	 * @param className
	 *            Name of the class from which to get ID
	 * @param language
	 *            Language of the classes provided
	 * @param newClasses
	 *            Specifications of classes to create
	 * @return metaClassID of the new MetaClass if it was successfully created.
	 */
	MetaClassID newClassID(final AccountID accountID, final PasswordCredential credential, final String className,
			final Langs language, final Map<String, MetaClass> newClasses);

	/**
	 * Method that removes a class given its name and the name of the namespace where it belongs to
	 * 
	 * @param accountID
	 *            ID of the account responsible of the namespace of the class
	 * @param credential
	 *            Credential of the account responsible of the namespace of the class
	 * @param namespaceID
	 *            ID of the namespace of the class to remove
	 * @param className
	 *            Name of the class to remove
	 */
	void removeClass(final AccountID accountID, final PasswordCredential credential, final NamespaceID namespaceID,
			final String className);

	/**
	 * Method that removes an operation given its signature
	 * 
	 * @param accountID
	 *            the account of the responsible of the namespace
	 * @param credential
	 *            the credentials of the account
	 * @param namespaceID
	 *            the ID of the namespace of the class of the operation
	 * @param className
	 *            the name of the class of the opreation
	 * @param operationSignature
	 *            the signature of the operation
	 */
	void removeOperation(final AccountID accountID, final PasswordCredential credential, final NamespaceID namespaceID,
			final String className, final String operationSignature);

	/**
	 * Method that removes an implementation of a certain operation
	 * 
	 * @param accountID
	 *            the account of the responsible of the namespace
	 * @param credential
	 *            the credentials of the account
	 * @param namespaceID
	 *            ID of the namespace of the implementation
	 * @param implementationID
	 *            the id of the implementation
	 */
	void removeImplementation(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final ImplementationID implementationID);

	/**
	 * Method that retrieves the id of an operation given its signature
	 * 
	 * @param accountID
	 *            the account of the responsible of the namespace
	 * @param credential
	 *            the credentials of the account
	 * @param namespaceID
	 *            the id of the namespace
	 * @param className
	 *            the name of the class
	 * @param operationSignature
	 *            signature of the operation
	 * @return the operation id if the operation succeeds. null otherwise.
	 */
	OperationID getOperationID(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final String className, final String operationSignature);

	/**
	 * Method that retrieves the id of a property given its signature
	 * 
	 * @param accountID
	 *            the account of the responsible of the namespace
	 * @param credential
	 *            the credentials of the account
	 * @param namespaceID
	 *            the id of the namespace
	 * @param className
	 *            the name of the class
	 * @param propertyName
	 *            the name of the property
	 * @return the property id if the operation succeeds. null otherwise.
	 */
	PropertyID getPropertyID(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final String className, final String propertyName);

	/**
	 * Method that retrieves the id of a class given its name
	 * 
	 * @param accountID
	 *            the account of the responsible of the namespace
	 * @param credential
	 *            the credentials of the account
	 * @param namespaceID
	 *            the id of the namespace
	 * @param className
	 *            the name of the class
	 * @return the class id if the operation succeeds. null otherwise.
	 */
	MetaClassID getClassID(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final String className);

	/**
	 * Method that retrieves the id of a class given its name
	 * 
	 * @param accountID
	 *            the account of the responsible of the namespace
	 * @param credential
	 *            the credentials of the account
	 * @param namespaceID
	 *            the id of the namespace
	 * @param className
	 *            the name of the class
	 * @return the class id if the operation succeeds. null otherwise.
	 */
	MetaClass getClassInfo(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final String className);

	/**
	 * Method that retrieves the info of the classes registered in specific namespace.
	 * 
	 * @param accountID
	 *            the account of the responsible of the namespace
	 * @param credential
	 *            the credentials of the account
	 * @param namespaceID
	 *            the id of the namespace
	 * @return The info of the classes registered in the given namespace if the operation succeeds. null otherwise.
	 */
	Map<MetaClassID, MetaClass> getInfoOfClassesInNamespace(final AccountID accountID,
			final PasswordCredential credential, final NamespaceID namespaceID);

	// ============== Contract Manager ==============//

	/**
	 * Method to register a new contract.
	 * 
	 * @param accountID
	 *            The account of the contract provider
	 * @param credential
	 *            Credential of the account
	 * @param newContract
	 *            the specification of the contract (dates, interfaces, etc.)
	 * @return the contract id if the operation succeeds. null otherwise.
	 */
	ContractID newContract(final AccountID accountID, final PasswordCredential credential, final Contract newContract);

	/**
	 * Method to register an account to a contract
	 * 
	 * @param accountID
	 *            The account of the applicant for the contract
	 * @param credential
	 *            The credential of the applicant for the contract
	 * @param contractID
	 *            ID of the contract in which to register.
	 */
	void registerToPublicContract(final AccountID accountID, final PasswordCredential credential,
			final ContractID contractID);

	/**
	 * Method to register an account to a public contract given a namespace
	 * 
	 * @param accountID
	 *            The account of the applicant for the contract
	 * @param credential
	 *            The credential of the applicant for the contract
	 * @param namespaceID
	 *            ID of the namespace in which to register (in case it provides a public contract)
	 * @return ID of the public contract associated with the namespace
	 */
	ContractID registerToPublicContractOfNamespace(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID);

	/**
	 * Method that returns all contract IDs of a user (as applicant).
	 * 
	 * @param applicantAccountID
	 *            Account ID of the user that queries for its contract IDs
	 * @param credential
	 *            Credential of the account
	 * @return A list of Contract IDs of the account provided.
	 */
	Map<ContractID, Contract> getContractIDsOfApplicant(final AccountID applicantAccountID,
			final PasswordCredential credential);

	/**
	 * Method that returns all contract IDs of a namespace (as provider).
	 * 
	 * @param accountID
	 *            ID of the responsible of the namespace.
	 * @param credential
	 *            credentails of the responsible of the namespace.
	 * @param namespaceIDofProvider
	 *            ID of the namespace provider
	 * @return A list of Contract IDs of the namespace as provider.
	 */
	Map<ContractID, Contract> getContractIDsOfProvider(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceIDofProvider);

	/**
	 * Method that returns all contract IDs of a user (as applicant) with the given namespace provider.
	 * 
	 * @param applicantAccountID
	 *            Account ID of the user that queries for its contract IDs.
	 * @param credential
	 *            Credential of the account.
	 * @param namespaceIDofProvider
	 *            ID of the namespace that provides the contracts to be retrieved.
	 * @return A list of Contract IDs of the specified account with the given namespace provider.
	 */
	Map<ContractID, Contract> getContractIDsOfApplicantWithProvider(final AccountID applicantAccountID,
			final PasswordCredential credential, final NamespaceID namespaceIDofProvider);

	// ============== Interface Manager =========//

	/**
	 * Creates a new Data Contract
	 * 
	 * @param accountID
	 *            ID of the responsible of the dataset
	 * @param credential
	 *            credential of the account responsible of dataset
	 * @param newDataContract
	 *            specifications of the datacontract to create
	 * @return The id of the new contract if it success, error otherwise.
	 * 
	 */
	DataContractID newDataContract(AccountID accountID, PasswordCredential credential,
			final DataContract newDataContract);

	/**
	 * Method that registers a certain account to a public data contract.
	 * 
	 * @param accountID
	 *            ID of the account that wants to be registered to the contract
	 * @param credential
	 *            the credential of the acount
	 * @param datacontractID
	 *            the ID of the public data contract
	 */
	void registerToPublicDataContract(AccountID accountID, PasswordCredential credential,
			DataContractID datacontractID);

	/**
	 * Method that retrieves the data contracts that a dataset provides.
	 * 
	 * @param accountID
	 *            ID of a responsible of the data contract (or admin)
	 * @param credential
	 *            credential of the account
	 * @param datasetIDofProvider
	 *            ID of the dataset providing the contracts
	 * @return The set of data contracts provided by the dataset.
	 */
	Map<DataContractID, DataContract> getDataContractIDsOfProvider(AccountID accountID, PasswordCredential credential,
			DataSetID datasetIDofProvider);

	/**
	 * Method that retrieves the data contracts that an account has (as applicant).
	 * 
	 * @param applicantAccountID
	 *            ID of the account to be checked
	 * @param credential
	 *            credential of the account
	 * @return The set of data contracts that the account has applied to.
	 */
	Map<DataContractID, DataContract> getDataContractIDsOfApplicant(AccountID applicantAccountID,
			PasswordCredential credential);

	/**
	 * Method that retrieves the data contract of an account (as applicant) with a certain dataset.
	 * 
	 * @param applicantAccountID
	 *            ID of the account to be checked.
	 * @param credential
	 *            credential of the account
	 * @param datasetIDofProvider
	 *            ID of the dataset as provider
	 * @return the data contract the account has applied to with the given dataset
	 */
	DataContract getDataContractInfoOfApplicantWithProvider(AccountID applicantAccountID, PasswordCredential credential,
			DataSetID datasetIDofProvider);

	/**
	 * Method that registers a new interface
	 * 
	 * @param accountID
	 *            the account of the responsible of the namespace of the class
	 * @param credential
	 *            the credentials of the account
	 * @param newInterface
	 *            specifications of interface to create
	 * @return ID of the new interface created or NULL if it failed.
	 */
	InterfaceID newInterface(final AccountID accountID, final PasswordCredential credential,
			final Interface newInterface);

	/**
	 * Method that retrieves the info of the interface if the account is registered in a contract that contains it
	 * 
	 * @param accountID
	 *            ID of the account registered in a contract with the interface present
	 * @param credential
	 *            the credential of the account
	 * @param interfaceID
	 *            the ID of the interface to be retrieved
	 * @return info of the interface
	 */
	Interface getInterfaceInfo(final AccountID accountID, final PasswordCredential credential,
			final InterfaceID interfaceID);

	/**
	 * Method that removes a specific interface
	 * 
	 * @param accountID
	 *            the account of the responsible of the namespace of the interface
	 * @param credential
	 *            the credentials of the acount
	 * @param namespaceID
	 *            ID of the namespace of the interface
	 * @param interfaceID
	 *            the id of the interface to be removed
	 */
	void removeInterface(final AccountID accountID, final PasswordCredential credential, final NamespaceID namespaceID,
			final InterfaceID interfaceID);

	// ============== MetaData Service ==========//

	/**
	 * Register object in MDS
	 * 
	 * @param regInfo
	 *            Registration info
	 * @param backendID
	 *            ID of the backend in which the object is stored
	 * @param alias
	 *            Alias (can be null)
	 * @param lang
	 *            Language
	 */
	ObjectID registerObject(final RegistrationInfo regInfo, final ExecutionEnvironmentID backendID, final String alias,
			final Langs lang);

	/**
	 * Register objects in MDS
	 * 
	 * @param regInfo
	 *            Registration info
	 * @param backendID
	 *            ID of the backend in which the objects were stored
	 * @param clientLib
	 *            DataService objects Map to be modified since this call is ASYNCHRONOUS. Thus, modified by GRPC (see Logic
	 *            module grpc client)
	 */
	void registerObjectFromGC(final RegistrationInfo regInfo, final ExecutionEnvironmentID backendID,
			final DataServiceRuntime clientLib);

	/**
	 * Method that retrieves the info of all the registered backends
	 * 
	 * @param sessionID
	 *            ID of session asking
	 * @param fromClient Indicates information was requested from client
	 * @param exeEnvLang Language of execution environments to get information of
	 * @return the info of the registered Storage Locations in a table indexed by their IDs
	 */
	Map<ExecutionEnvironmentID, ExecutionEnvironment> getExecutionEnvironmentsInfo(final SessionID sessionID,
			final Langs exeEnvLang, final boolean fromClient);

	/**
	 * Method that retrieves the names of registered backends by language
	 * 
	 * @param accountID
	 *            ID of account
	 * @param credential
	 *            credentials of the account
	 * @param exeEnvLang
	 *            Language of exec environments.
	 * @return the info of the registered Storage Locations in a table indexed by their IDs
	 */
	Set<String> getExecutionEnvironmentsNames(final AccountID accountID, final PasswordCredential credential,
			final Langs exeEnvLang);

	// ============== Federated objects ==============//

	// ============== MetaData Service ==========//
	
	/**
	 * Retrieves the backend specification
	 * 
	 * @param backendID
	 *            ID of the backend
	 * @return the specification of the backend
	 */
	StorageLocation getStorageLocationForDS(final StorageLocationID backendID);

	/**
	 * Retrieves the backend specification
	 * 
	 * @param backendID
	 *            ID of the backend
	 * @return the specification of the backend
	 */
	ExecutionEnvironment getExecutionEnvironmentForDS(final ExecutionEnvironmentID backendID);

	/**
	 * Method that tries to register an external instance of dataClay assigning a new ID to it. If if is already registered,
	 * method is ignored.
	 * 
	 * @param hostname
	 *            host name of the main service of the external dataClay instance.
	 * @param port
	 *            port of the external main service of the dataClay instance
	 * @return ID of dataClay registered.
	 */
	DataClayInstanceID registerExternalDataClay(final String hostname, final int port);
	
	/**
	 * For system-admin users only. Method that registers an external dataclay overriding authority specified.
	 * @param adminAccountID Admin account iD
	 * @param adminCredential Admin credentials
	 * @param hostname Hostname
	 * @param port Port
	 * @param authority Authority 
	 * @return ID of the dataClay registered
	 */
	DataClayInstanceID registerExternalDataClayOverrideAuthority(final AccountID adminAccountID, final PasswordCredential adminCredential,
			final String hostname, final int port, final String authority);
	
	/**
	 * When you register an external DataClay, we notify external dataClay about the registration and we get the ID of the
	 * external dataClay. Destination dataClay then, is aware of all dataClays 'connected' to him.
	 * 
	 * @param dataClayInstanceID
	 *            ID of dataClay registering current dataClay as external
	 * @param hostname
	 *            Host name
	 * @param port
	 *            Port
	 * @return ID of current dataClay or Null if not allowed or some error ocurred
	 */
	DataClayInstanceID notifyRegistrationOfExternalDataClay(
			final DataClayInstanceID dataClayInstanceID, final String hostname, final int port);

	/**
	 * Tries to connect to an external dataClay instance and retrieve its ID. *
	 * 
	 * @param dcHost
	 *            hostname of the external dataClay instance
	 * @param dcPort
	 *            port of the external dataClay instance
	 * @return id of the external dataClay instance
	 */
	DataClayInstanceID getExternalDataClayID(final String dcHost, final int dcPort);

	/**
	 * Method that retrieves the info of an external dataClay instance
	 * 
	 * @param extDataClayID
	 *            id of the external (already registered) dataClay
	 * @return info of dataClay instance identified by specified ID. null if it is not registered in the system.
	 */
	DataClayInstance getExternalDataClayInfo(final DataClayInstanceID extDataClayID);

	/**
	 * Get all classes from namespace with name provided in current dataClay.
	 * @param namespaceName Name of the namespace to get data from
	 * @return Namespace information, information of all classes registered in namespace and code of classes
	 * to deploy
	 */
	Tuple<Namespace, Set<MetaClass>> getClassesInNamespace(final String namespaceName);

	/**
	 * Get namespace information, classes to register and code to deploy from another dataClay instance and
	 * register it into current dataClay
	 * @param extNamespaceName Name of the external namespace to get
	 * @param extDataClayID External dataClay ID to get namespace/classes from
	 * @throws Exception If registration fails
	 */
	void registerClassesInNamespaceFromExternalDataClay(final String extNamespaceName, final DataClayInstanceID extDataClayID);


	/**
	 * Method that federates an object with an external dataClay instance
	 * 
	 * @param sessionID
	 *            id of the current session
	 * @param objectID
	 *            id of the object to be federated
	 * @param extDataClayID
	 *            ID of the external dataClay instance
	 * @param recursive
	 *            whether subobjects should be also federated or not
	 */
	void federateObject(final SessionID sessionID, final ObjectID objectID, final DataClayInstanceID extDataClayID,
			final boolean recursive);

	/**
	 * Method that notifies current dataClay instance of a federated object from another dataClay
	 * 
	 * @param srcDataClayID
	 *            id of source dataClay instance
	 * @param srcDcHost
	 *            hostname of source dataClay instance
	 * @param srcDcPort
	 *            port of source dataClay instance
	 * @param providedobjectsInfo
	 *            metadata of objects to federate
	 * @param federatedObjects Serialized data of objects to federate
	 * 
	 */
	void notifyFederatedObjects(final DataClayInstanceID srcDataClayID, final String srcDcHost,
			final int srcDcPort, final Map<ObjectID, MetaDataInfo> providedobjectsInfo, 
			final Map<Langs, SerializedParametersOrReturn> federatedObjects);

	/**
	 * Checks if the given object is federated with given external dataClay instance
	 * 
	 * @param objectID
	 *            id of the object
	 * @param extDataClayID
	 *            id of the dataClay instance
	 * @return TRUE if the object is federated with the given external dataClay instance. FALSE otherwise.
	 */
	boolean checkObjectIsFederatedWithDataClayInstance(final ObjectID objectID, final DataClayInstanceID extDataClayID);

	/**
	 * Method that allows an external dataClay instance to request the execution of an implementation on a given object. This
	 * object must be a valid object shared with such an external dataClay.
	 * 
	 * @param extDataClayID
	 *            id of the external dataClay instance
	 * @param objectID
	 *            id of the target object
	 * @param implID
	 *            ID of the implementation to be executed
	 * @param params
	 *            parameters for the execution
	 * @param allBackends Indicates if execution must be done in all replicas
	 */
	void synchronizeFederatedObject(final DataClayInstanceID extDataClayID,
			final ObjectID objectID, final ImplementationID implID, final SerializedParametersOrReturn params,
			final boolean allBackends);

	/**
	 * Retrieve external dataClays which the given object has been federated with.
	 * 
	 * @param objectID
	 *            id of the federated object to be checked
	 * @return known dataClay instances having this federated object
	 */
	Set<DataClayInstanceID> getDataClaysObjectIsFederatedWith(final ObjectID objectID); 
	
	/**
	 * Retrieve source dataClay
	 * 
	 * @param objectID
	 *            id of the federated object to be checked
	 * @return origin dataClay or null if object does not come from federation.
	 */
	DataClayInstanceID getExternalSourceDataClayOfObject(final ObjectID objectID); 


	/**
	 * Method that unfederates an object
	 * 
	 * @param sessionID
	 *            id of the current session
	 * @param objectID
	 *            id of the object to be unfederated
	 * @param externalDataClayID
	 *            ID of the external dataClay to unfederate the object with.
	 * @param recursive
	 *            whether subobjects should be also unfederated or not
	 */
	void unfederateObject(final SessionID sessionID, final ObjectID objectID,
			final DataClayInstanceID externalDataClayID,
			final boolean recursive);
	
	
	/**
	 * Method that unfederates all objects belonging/shared with external dataClay provided.
	 * 
	 * @param sessionID
	 *            id of the current session
	 * @param externalDataClayID
	 *            ID of the external dataClay to unfederate the object with.
	 */
	void unfederateAllObjects(final SessionID sessionID,
			final DataClayInstanceID externalDataClayID);

	/**
	 * Federate all objects belonging to current dataClay with external dataClay provided
	 * @param sessionID
	 *            id of the current session
	 * @param externalDestinationDataClayID ID of external dataClay to federated the objects
	 */
	public void federateAllObjects(final SessionID sessionID,
			final DataClayInstanceID externalDestinationDataClayID);
	
	/**
	 * Method that moves all objects (belonging to current dataClay) and 
	 * federated TO a certain external dataClay to another external dataClay.
	 * 
	 * @param sessionID
	 *            id of the current session
	 * @param externalOriginDataClayID ID of external dataClay objects are federated to
	 * @param externalDestinationDataClayID ID of external dataClay to federated the objects
	 *            
	 */
	void migrateFederatedObjects(final SessionID sessionID,
			final DataClayInstanceID externalOriginDataClayID, 
			final DataClayInstanceID externalDestinationDataClayID);
	

	/**
	 * Method that unfederates all objects belonging/shared with ANY external dataClay provided.
	 * 
	 * @param sessionID
	 *            id of the current session
	 */
	void unfederateAllObjectsWithAllDCs(final SessionID sessionID);

	
	/**
	 * Method that unfederates a concrete object belonging/shared with ANY external dataClay provided.
	 * 
	 * @param sessionID
	 *            id of the current session
	 * @param objectID id of the object to unfederate with all DCs   
	 * @param recursive Indicates to unfederate all sub-objects with any external dataClay also.       
	 */
	void unfederateObjectWithAllDCs(final SessionID sessionID, final ObjectID objectID, 
			final boolean recursive);
	
	
	
	/**
	 * Method that notifies current dataClay instance to unfederate objects with source dataclay provided.
	 * 
	 * @param srcDataClayID
	 *            id of source dataClay instance
	 * @param objectsIDs
	 *            ids of objects to federate
	 * 
	 */
	void notifyUnfederatedObjects(final DataClayInstanceID srcDataClayID,
			final Set<ObjectID> objectsIDs);

	// ============== Data Service ==============//

	/**
	 * Method to check whether an object is accessible with the specified session and returns its classname and namespace of the
	 * class.
	 * 
	 * @param sessionID
	 *            ID of the session
	 * @param objectID
	 *            ID of object
	 * @return a tuple [class name, namespace] containing the class of the object, and the namespace of the class.
	 */
	Tuple<String, String> getObjectInfo(final SessionID sessionID, final ObjectID objectID);

	/**
	 * Retrieves the objectID corresponding to the object with the specified alias.
	 * 
	 * @param sessionID
	 *            ID of the session
	 * @param alias
	 *            alias of the object
	 * @return ID of the resulting object, class ID, and Hint for fast execution.
	 */
	Triple<ObjectID, MetaClassID, ExecutionEnvironmentID> getObjectFromAlias(final SessionID sessionID,
			final String alias);

	/**
	 * Removes the alias of an object
	 * 
	 * @param sessionID
	 *            ID of the session
	 * @param alias
	 *            alias of the object
	 */
	void deleteAlias(final SessionID sessionID, final String alias);

	/**
	 * Retrieves the information of the objects instantiating the given class.
	 * 
	 * @param classID
	 *            the id of the class
	 * @return the information of the objects of the class indexed by Object ID
	 */
	Map<ObjectID, MetaDataInfo> getObjectsMetaDataInfoOfClassForNM(final MetaClassID classID);

	/**
	 * Set DataSetID from GC
	 * 
	 * @param objectID
	 *            ID of the object
	 * @param dataSetID
	 *            ID of the dataset
	 */
	void setDataSetIDFromGarbageCollector(final ObjectID objectID, final DataSetID dataSetID);

	/**
	 * Set dataset id of object
	 * 
	 * @param sessionID
	 *            ID of session
	 * @param objectID
	 *            ID of object
	 * @param dataSetID
	 *            ID of dataset
	 */
	void setDataSetID(final SessionID sessionID, final ObjectID objectID, final DataSetID dataSetID);

	/**
	 * Method that creates a persistent new version of the object. If a destination backend is given, it tries to store the
	 * object in it.
	 * 
	 * @param sessionID
	 *            ID of the session
	 * @param objectID
	 *            ID of the object
	 * @param optionalDestBackendID
	 *            optionally a preferred destination backend
	 * @return the information about the new version required for consolidate
	 */
	VersionInfo newVersion(final SessionID sessionID, final ObjectID objectID,
			final ExecutionEnvironmentID optionalDestBackendID);

	/**
	 * Makes the object with finalVersionID the definitive version of the object with originalObjectID. The original version is
	 * deleted.
	 * 
	 * @param sessionID
	 *            ID of the session
	 * @param version
	 *            Info about the version containing ID of the root of the version and mapping versionID-originalID for all the
	 *            versioned objects
	 */
	void consolidateVersion(final SessionID sessionID, final VersionInfo version);

	/**
	 * Method that creates a new replica of the object. If a destination backend is given, it tries to replicate the object in
	 * it.
	 * 
	 * @param sessionID
	 *            ID of the session
	 * @param objectID
	 *            ID of the object
	 * @param optionalDestBackendID
	 *            optionally a preferred destination backend
	 * @param recursive
	 *            Indicates if sub-objects must be also replicated or not.
	 * @return ID of the backend where replica has been eventually registered.
	 */
	ExecutionEnvironmentID newReplica(final SessionID sessionID, final ObjectID objectID,
			final ExecutionEnvironmentID optionalDestBackendID, final boolean recursive);

	/**
	 * Method that moves an object from location to location.
	 * 
	 * @param sessionID
	 *            ID of the session
	 * @param objectID
	 *            ID of the object
	 * @param srcBackendID
	 *            ID of the source location
	 * @param destBackendID
	 *            ID of the destination location
	 * @param recursive
	 *            Indicates if sub-objects (int the src location or others) must be also moved or not.
	 * @return List of moved objects.
	 */
	List<ObjectID> moveObject(final SessionID sessionID, final ObjectID objectID,
			final ExecutionEnvironmentID srcBackendID, final ExecutionEnvironmentID destBackendID,
			final boolean recursive);

	/**
	 * Method that sets the object as read only
	 * 
	 * @param sessionID
	 *            ID of the session
	 * @param objectID
	 *            ID of the object
	 */
	void setObjectReadOnly(final SessionID sessionID, final ObjectID objectID);

	/**
	 * Method that sets the object as read-write
	 * 
	 * @param sessionID
	 *            ID of the session
	 * @param objectID
	 *            ID of the object
	 */
	void setObjectReadWrite(final SessionID sessionID, final ObjectID objectID);

	/**
	 * Method that retrieves the locations of all the replicas of a specific object and its classname.
	 * 
	 * @param sessionID
	 *            ID of the session
	 * @param objectID
	 *            ID of the object
	 * @return MetaDataInfo of the object
	 */
	MetaDataInfo getMetadataByOID(final SessionID sessionID, final ObjectID objectID);

	/**
	 * Get metadata by oid. TODO: functions without session ids (garbage collector for isntance) should be used in different way
	 * (dgasull)
	 * 
	 * @param objectID
	 *            Object ID
	 * @return Metadata info for EE.
	 */
	MetaDataInfo getMetadataByOIDForDS(ObjectID objectID);

	/**
	 * Method that executes an implementation
	 * 
	 * @param sessionID
	 *            ID of the session
	 * @param operationID
	 *            the operation to be executed
	 * @param remoteImplementation
	 *            info of the implementation to be executed
	 * @param objectID
	 *            the object on which the implementation is executed
	 * @param params
	 *            the serialized parameters for the operation
	 * @return Result of the operation (all objects serialized) or null if void.
	 */
	SerializedParametersOrReturn executeImplementation(final SessionID sessionID, final OperationID operationID,
			final Triple<ImplementationID, ContractID, InterfaceID> remoteImplementation, final ObjectID objectID,
			final SerializedParametersOrReturn params);

	/**
	 * Method that executes a method on a specific target (using an arbitrary implementation given the session info).
	 * 
	 * @param sessionID
	 *            ID of the session.
	 * @param objectID
	 *            ID of the object on which the implementation is executed
	 * @param operationSignature
	 *            the operation signature of the method to be executed
	 * @param params
	 *            the serialized parameters for the method
	 * @param backendID
	 *            the backend where the method must be executed
	 * @return Result of the operation (all objects serialized) or null if void.
	 */
	SerializedParametersOrReturn executeMethodOnTarget(final SessionID sessionID, final ObjectID objectID,
			final String operationSignature, final SerializedParametersOrReturn params,
			final ExecutionEnvironmentID backendID);

	
	/**
	 * Indicates if dataClay has prefetching enabled. 
	 * @return TRUE if prefetching is enabled in dataClay. FALSE otherwise. 
	 */
	boolean isPrefetchingEnabled();
	
	/**
	 * Method that retrieves the ID of current dataClay instance
	 * 
	 * @return ID of current dataclay
	 */
	DataClayInstanceID getDataClayID();

	/**
	 * Method that allows to retrieve the stubs of the given contracts (merging interfaces if necessary)
	 * 
	 * @param applicantAccountID
	 *            the applicant of the contract
	 * @param applicantCredential
	 *            the credentials of the applicant
	 * @param language
	 *            the language of the stub to generate
	 * @param contractsIDs
	 *            IDs of the contracts
	 * @return a Map of byte arrays with bytecode (or similar) for each stub
	 */
	Map<String, byte[]> getStubs(final AccountID applicantAccountID, final PasswordCredential applicantCredential,
			final Langs language, final List<ContractID> contractsIDs);

	/**
	 * Method that allows to retrieve the "Babel" (language independent YAML-based) stubs of the given contracts (merging
	 * interfaces if necessary).
	 * 
	 * @param applicantAccountID
	 *            the applicant of the contract
	 * @param applicantCredential
	 *            the credentials of the applicant
	 * @param contractsIDs
	 *            IDs of the contracts
	 * @return a byte array with a YAML document, with each
	 */
	byte[] getBabelStubs(final AccountID applicantAccountID, final PasswordCredential applicantCredential,
			final List<ContractID> contractsIDs);

	/**
	 * Get class name for Data Service
	 * 
	 * @param classID
	 *            ID of the class
	 * @return Name of the class
	 */
	String getClassNameForDS(final MetaClassID classID);

	/**
	 * Get class name and namespace name for Data Service
	 * 
	 * @param classID
	 *            ID of the class
	 * @return Class name and namespace name of the class
	 */
	Tuple<String, String> getClassNameAndNamespaceForDS(final MetaClassID classID);

	/**
	 * Registers an event listener implementation
	 * 
	 * @param accountID
	 *            ID of account registering the event listener
	 * @param credential
	 *            Credentials of the account
	 * @param newEventListener
	 *            Event listener implementation to register.
	 */
	void registerEventListenerImplementation(final AccountID accountID, final PasswordCredential credential,
			final ECA newEventListener);

	/**
	 * Check if object exists in dataClay (in any EE memory or SL)
	 * @param objectID ID of the object to check 
	 * @return TRUE if the object exists. FALSE otherwise. 
	 */
	boolean objectExistsInDataClay(final ObjectID objectID);
	
	/***** Register Listener *****/

	/**
	 * Advises Notification Manager new event has occurred.
	 * 
	 * @param newEvent
	 *            New event
	 */
	void adviseEvent(final EventMessage newEvent);

	/**
	 * Activate tracing.
	 * @param currentAvailableTaskID Current starting task ID in Extrae
	 */
	void activateTracing(final int currentAvailableTaskID);

	/**
	 * Deactivate Extrae tracing
	 */
	void deactivateTracing();

	/**
	 * Get Extrae traces (mpits and set files)
	 * @return Extrae traces (mpits and set files)
	 */
	Map<String, byte[]> getTraces();
	
	/**
	 * Clean MD caches
	 */
	void cleanMetaDataCaches();

	/**
	 * Close mgr db.
	 */
	void closeManagerDb();

	/**
	 * Close logic db.
	 */
	void closeDb();

	/**
	 * Get contract ID of DataClay classes
	 * 
	 * @param accountID
	 *            ID of the account querying
	 * @param credential
	 *            Credentials
	 * @return Contract ID of DataClay provider
	 */
	ContractID getContractIDOfDataClayProvider(AccountID accountID, PasswordCredential credential);

	/**
	 * Unregister objects (called from GlobalGC)
	 * 
	 * @param objectsToUnregister
	 *            Objects to unregister
	 */
	void unregisterObjects(Set<ObjectID> objectsToUnregister);
}
