package es.bsc.dataclay.commonruntime;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.DataClayMockObject;
import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.FileAndAspectsUtils;
import es.bsc.dataclay.util.ProcessEnvironment;
import es.bsc.dataclay.util.events.listeners.ECA;
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
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.ids.SessionID;
import es.bsc.dataclay.util.management.accountmgr.Account;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.contractmgr.Contract;
import es.bsc.dataclay.util.management.datacontractmgr.DataContract;
import es.bsc.dataclay.util.management.datasetmgr.DataSet;
import es.bsc.dataclay.util.management.interfacemgr.Interface;
import es.bsc.dataclay.util.management.metadataservice.ExecutionEnvironment;
import es.bsc.dataclay.util.management.namespacemgr.Namespace;
import es.bsc.dataclay.util.management.sessionmgr.SessionInfo;
import es.bsc.dataclay.util.management.stubs.StubClassLoader;
import es.bsc.dataclay.util.management.stubs.StubInfo;
import es.bsc.dataclay.util.tools.java.JavaSpecGenerator;

/**
 * This class contains all those functions that the User needs to interact with
 * the system to register users, classes, namespaces...
 * 
 * 
 */
public final class ClientManagementLib {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger("ClientManagementLib");
	/** Path of configuration file. */
	private static final String CONFIGFILEPATH = "." + File.separatorChar + "cfgfiles" + File.separatorChar
			+ "client.properties";
	/** Environment variable where configuration file can be specified. */
	public static final String CONFIGFILEPATH_ENV = "DATACLAYCLIENTCONFIG";
	/** Field name of host in configuration file. */
	private static final String KEYLOGICHOST = "HOST";
	/** Field name of tcp port in configuration file. */
	private static final String KEYLOGICTCPPORT = "TCPPORT";
	/** Suffix of java classes. */
	private static final String JAVACLASSEXT = ".class";
	/** Suffix of aspect classes. */
	private static final String JAVAASPECTEXT = ".aj";
	/** Suffix of Babel Stubs. */
	private static final String BABELEXT = ".yaml";
	/** DataClayClientLib instance. */
	private static ClientRuntime clientLib;

	/** Lock for newClass. */
	private static Lock newClassLock = new ReentrantLock();

	/**
	 * ClientManagementLib constructor
	 */
	private ClientManagementLib() {
		throw new IllegalAccessError("This class cannot be instantiated");
	}

	/**
	 * Get Manifest version
	 * 
	 * @return Version of the library
	 */
	public static String getManifestInfo() {
		final Package pkg = ClientManagementLib.class.getPackage();
		return pkg.getImplementationVersion();
	}

	/**
	 * Initialize connections with LM.
	 * 
	 * @param configFilePathArg
	 *            optional path of config file. If a null or empty string is
	 *            provided, the method will try looking for it in the path defined
	 *            by dataClayCLIENTCONFIG environment variable, or the default path
	 *            CONFIGFILEPATH
	 * @return TRUE if connections were successfully initiliazed. FALSE, otherwise.
	 */
	public static boolean initializeCMLib(final String configFilePathArg) {
		String configFilePath = configFilePathArg;
		try {
			File configFile = null;
			boolean fileExists = false;
			if (configFilePath != null && !configFilePath.isEmpty()) {
				LOGGER.info("Initializing client library with provided file located at {}", configFilePath);
				configFile = new File(configFilePath);
				fileExists = configFile.isFile() && configFile.exists();
			}
			if (!fileExists) {
				configFilePath = ProcessEnvironment.getInstance().get(CONFIGFILEPATH_ENV);
				if (configFilePath != null && !configFilePath.isEmpty()) {
					configFile = new File(configFilePath);
					fileExists = configFile.isFile() && configFile.exists();
				}

				if (fileExists) {
					LOGGER.info("Found {}. Initializing client library with properties located at {}",
							CONFIGFILEPATH_ENV, configFilePath);
				} else {
					final Path path = Paths.get(CONFIGFILEPATH).normalize();
					LOGGER.warn("Unsuccessful while trying to load file from the environment variable `{}={}`. "
							+ "Fallback to default location: {}", CONFIGFILEPATH_ENV, CONFIGFILEPATH, path.toAbsolutePath());
					configFilePath = path.toAbsolutePath().toString();
					configFile = new File(configFilePath);

				}
			}

			final FileInputStream configFileStr = new FileInputStream(configFile);
			final Properties prop = new Properties();
			prop.load(configFileStr);
			configFileStr.close();

			final String host = prop.getProperty(KEYLOGICHOST);
			final Integer port = new Integer(prop.getProperty(KEYLOGICTCPPORT));
			if (clientLib != null) {
				// If commonLib is not null, it means it was previously initialized and we are
				// restarting it. So, close it
				// before.
				LOGGER.info("Previous connection found during start of DataClay libraries."
						+ " Closing it before new connection.");
				clientLib.finishConnections();
			}
			clientLib = new ClientRuntime();
			clientLib.initialize(host, port, "CL");

			if (Configuration.mockTesting) {
				DataClayMockObject.setCurrentThreadLib(clientLib);
			}
			final Thread shutdownHook = new Thread() {
				@Override
				public void run() {
					try {
						if (clientLib != null) {
							clientLib.finishConnections();
							clientLib = null;
							LOGGER.info("Client Library closed by SHUTDOWN HOOK ");
						}
					} catch (final Exception e) {
						LOGGER.error("Error during shutdown hook", e);
					}
				}
			};
			shutdownHook.setName("CL-ShutdownHook");
			Runtime.getRuntime().addShutdownHook(shutdownHook);

		} catch (final Exception ex) {
			LOGGER.error("Cannot initialize client library. Is client.properties valid? Has init() been called?", ex);
			return false;
		}
		return true;
	}

	/**
	 * Finish connections to server
	 * 
	 * @throws Exception
	 *             if some exception occurs
	 */
	public static void finishConnections() throws Exception {
		if (clientLib != null) {
			LOGGER.info("Finishing connections ... ");
			clientLib.finishConnections();
			clientLib = null;
		}
	}

	// ============== Account Manager ==============//

	/**
	 * Get the common library
	 * 
	 * @return The common lib
	 */
	public static ClientRuntime getDataClayClientLib() {
		return clientLib;
	}

	/**
	 * This operation creates a new account in the system with the provided
	 * username.
	 * 
	 * @param newAcc
	 *            Specifications of account to create
	 * @return AccountID of the new account if the it was successfully created. Null
	 *         otherwise.
	 */
	public static AccountID newAccount(final Account newAcc) {

		clientLib.checkConnectionAndParams(new String[] { "AdminAccountID", "AdminCredential", "Account" },
				new Object[] { newAcc });

		AccountID result = null;
		try {
			result = clientLib.getLogicModuleAPI().newAccountNoAdmin(newAcc);
		} catch (final Exception ex) {
			LOGGER.warn("Error during newAccount", ex);
		}
		return result;
	}

	/**
	 * Method that retrieves the id of an account given its user name
	 * 
	 * @param userName
	 *            Name of the user
	 * @return the id of Account class if the operation succeeds. null otherwise
	 * @exception RemoteException
	 *                if some exception occurs
	 */
	public static AccountID getAccountID(final String userName) {

		clientLib.checkConnectionAndParams(new String[] { "UserName" }, new Object[] { userName });
		AccountID result = null;
		try {
			result = clientLib.getLogicModuleAPI().getAccountID(userName);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getAccountID", ex);
		}
		return result;
	}

	/**
	 * Method that retrieves the list of non admin accounts registered in the
	 * system.
	 * 
	 * @param adminAccountID
	 *            ID of the account of the user that calls the operation
	 * @param adminCredential
	 *            Credential of the adminAccount provided
	 * @return IDs of existent accounts (admin not included)
	 */
	public static Set<AccountID> getNonAdminAccountList(final AccountID adminAccountID,
			final PasswordCredential adminCredential) {
		clientLib.checkConnectionAndParams(new String[] { "AdminAccountID", "AdminCredential" },
				new Object[] { adminAccountID, adminCredential });
		Set<AccountID> result = null;
		try {
			result = clientLib.getLogicModuleAPI().getAccountList(adminAccountID, adminCredential);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getNonAdminAccountList", ex);
		}
		return result;
	}

	// ============== Session Manager ==============//

	/**
	 * This operation creates a new session
	 * 
	 * @param accountID
	 *            ID of the account that generates the session
	 * @param credential
	 *            credentials of the account
	 * @param stubsInfo
	 *            Stubs info to use in new Session
	 * @param dataSets
	 *            Accessible datasets for the session (user must have one data
	 *            contract for each dataset)
	 * @param dataSetForStore
	 *            Dataset for store (dataset must be include among the dataSets)
	 * @return the Session ID of the new session
	 */
	public static SessionID newSession(final AccountID accountID, final PasswordCredential credential,
			final Map<String, StubInfo> stubsInfo, final Set<String> dataSets, final String dataSetForStore) {

		clientLib.checkConnectionAndParams(
				new String[] { "AccountID", "Credential", "StubsInfo", "DataSets", "DataSetForStore" },
				new Object[] { accountID, credential, stubsInfo, dataSets, dataSetForStore });
		SessionID result = null;
		try {
			if (!dataSets.contains(dataSetForStore)) {
				LOGGER.warn("Dataset for store {} should be included among the datasets (currently: {})",
						dataSetForStore, dataSets);
			}

			// Init result
			final HashSet<ContractID> contracts = new HashSet<>();

			// For each stub in classpath, extract info of their contracts
			for (final Entry<String, StubInfo> entry : stubsInfo.entrySet()) {
				final StubInfo stubInfo = entry.getValue();
				contracts.addAll(stubInfo.getContracts());
			}
			if (contracts.size() == 0) {
				LOGGER.warn("The session has no model contracts");
			}

			// Call to Logic module
			result = newSessionCommon(accountID, credential, contracts, dataSets, dataSetForStore);

		} catch (final Exception ex) {
			LOGGER.warn("Error during newSession", ex);
		}
		return result;
	}

	/**
	 * This operation creates a new session
	 * 
	 * @param accountID
	 *            ID of the account that generates the session
	 * @param credential
	 *            credentials of the account
	 * @param classPathOfStubs
	 *            class path where stubs (interfaces in contracts) can be found
	 * @param dataSets
	 *            Accessible datasets for the session (user must have one data
	 *            contract for each dataset)
	 * @param dataSetForStore
	 *            Dataset for store (dataset must be include among the dataSets)
	 * @return the Session ID of the new session
	 */
	public static SessionID newSession(final AccountID accountID, final PasswordCredential credential,
			final String classPathOfStubs, final Set<String> dataSets, final String dataSetForStore) {
		clientLib.checkConnectionAndParams(
				new String[] { "AccountID", "Credential", "ClassPathOfStubs", "DataSets", "DataSetForStore" },
				new Object[] { accountID, credential, classPathOfStubs, dataSets, dataSetForStore });
		SessionID result = null;
		try {
			if (!dataSets.contains(dataSetForStore)) {
				LOGGER.warn("Dataset for store {} should be included among the datasets (currently: {})",
						dataSetForStore, dataSets);
			}
			// Get info of stubs in classpath
			final Map<String, StubInfo> stubsInfo = StubClassLoader.getStubInfosFromClassPath(classPathOfStubs);

			// Init result
			final HashSet<ContractID> contracts = new HashSet<>();

			// For each stub in classpath, extract info of their contracts
			for (final Entry<String, StubInfo> entry : stubsInfo.entrySet()) {
				final StubInfo stubInfo = entry.getValue();
				contracts.addAll(stubInfo.getContracts());
			}
			if (contracts.size() == 0) {
				LOGGER.warn("The session has no model contracts");
			}

			// Call to Logic module
			result = newSessionCommon(accountID, credential, contracts, dataSets, dataSetForStore);

		} catch (final Exception ex) {
			LOGGER.warn("Error during newSession", ex);
		}
		return result;
	}

	/**
	 * This operation creates a new session
	 * 
	 * @param accountID
	 *            ID of the account that generates the session
	 * @param credential
	 *            credentials of the account
	 * @param contracts
	 *            All contracts to be used in the new session to be created
	 * @param dataSets
	 *            Accessible datasets for the session (user must have one data
	 *            contract for each dataset)
	 * @param dataSetForStore
	 *            Dataset for store (dataset must be include among the dataSets)
	 * @return the Session ID of the new session
	 */
	public static SessionID newSession(final AccountID accountID, final PasswordCredential credential,
			final Set<ContractID> contracts, final Set<String> dataSets, final String dataSetForStore) {
		clientLib.checkConnectionAndParams(
				new String[] { "AccountID", "Credential", "Contracts", "DataSets", "DataSetForStore" },
				new Object[] { accountID, credential, contracts, dataSets, dataSetForStore });
		SessionID result = null;
		try {
			if (!dataSets.contains(dataSetForStore)) {
				LOGGER.warn("Dataset for store {} should be included among the datasets (currently: {})",
						dataSetForStore, dataSets);
			}
			if (contracts.size() == 0) {
				LOGGER.warn("The session has no model contracts");
			}
			result = newSessionCommon(accountID, credential, contracts, dataSets, dataSetForStore);
		} catch (final Exception ex) {
			LOGGER.warn("Error during newSession", ex);
		}
		return result;
	}

	/**
	 * Private function with common functionalities to initialize a session from
	 * different API calls.
	 * 
	 * @param accountID
	 *            ID of the account.
	 * @param credential
	 *            Credentials of the account.
	 * @param contracts
	 *            Contract IDs to use in the new session.
	 * @param dataSets
	 *            Data set names in session.
	 * @param dataSetForStore
	 *            Data set to use for storing or creating new objects during
	 *            session.
	 * @return ID of the sessionc created.
	 */
	private static SessionID newSessionCommon(final AccountID accountID, final PasswordCredential credential,
			final Set<ContractID> contracts, final Set<String> dataSets, final String dataSetForStore) {
		final Set<DataSetID> dataSetIDs = new HashSet<>();
		final DataSetID dataSetForStoreID = getDatasetID(accountID, credential, dataSetForStore);
		for (final String datasetName : dataSets) {
			dataSetIDs.add(getDatasetID(accountID, credential, datasetName));
		}
		// Call to Logic module
		LOGGER.debug("Calling new session with datasets :" + dataSetIDs);
		final SessionInfo sessInfo = clientLib.getLogicModuleAPI().newSession(accountID, credential, contracts,
				dataSetIDs, dataSetForStoreID, Langs.LANG_JAVA);
		clientLib.setSessionInfo(sessInfo);
		DataClayObject.setLib(clientLib);
		return sessInfo.getSessionID();
	}

	/**
	 * Finish session
	 */
	public static void closeSession() {
		try {
			clientLib.closeSession();
		} catch (final Exception ex) {

		}
	}

	/**
	 * Configure LogicModule to give provided address in case external dataClays require to know how
	 * to access current dataClay
	 * @param hostname Hostname to be published (given to external dataClays)
	 * @param port Port to be published
	 */
	public static void publishAddress(final String hostname, final int port) { 
		clientLib.checkConnectionAndParams(new String[] { "hostname", "port" },
				new Object[] { hostname, port });
		clientLib.getLogicModuleAPI().publishAddress(hostname, port);
	}
	
	// ============== Namespace Manager ==============//

	/**
	 * Method that creates a new namespace in the system. The account doing the
	 * action must have admin role.
	 * 
	 * @param accountID
	 *            the account id of the account or the responsible of the namespace
	 * @param credential
	 *            the credential of the account or the responsible of the namespace
	 * @param newNamespace
	 *            the new namespace
	 * @return the id of the new namespace if the operation succeeds. null otherwise
	 */
	public static NamespaceID newNamespace(final AccountID accountID, final PasswordCredential credential,
			final Namespace newNamespace) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "newNamespace" },
				new Object[] { accountID, credential, newNamespace });
		NamespaceID result = null;
		try {
			result = clientLib.getLogicModuleAPI().newNamespace(accountID, credential, newNamespace);
		} catch (final Exception ex) {
			LOGGER.warn("Error during newNamespace", ex);
		}
		return result;
	}

	/**
	 * This method removes a namespace from the system by checking it has no active
	 * contract associated with it, and no classes registered on it.
	 * 
	 * @param accountID
	 *            the account id of the responsible of the namespace
	 * @param credential
	 *            credentials of the account
	 * @param namespaceName
	 *            the name of the namespace to be removed
	 * @return True if the namespace is the namespace is correctly removed. False
	 *         otherwise.
	 */
	public static boolean removeNamespace(final AccountID accountID, final PasswordCredential credential,
			final String namespaceName) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "NamespaceName" },
				new Object[] { accountID, credential, namespaceName });
		try {
			clientLib.getLogicModuleAPI().removeNamespace(accountID, credential, namespaceName);
			return true;
		} catch (final Exception ex) {
			LOGGER.warn("Error during removeNamespace", ex);
		}
		return false;
	}

	/**
	 * Retrieves the id of a namespace identified by name provided
	 * 
	 * @param accountID
	 *            ID of an account asking for the ID
	 * @param credential
	 *            credential of account
	 * @param namespaceName
	 *            Name of the namespace
	 * @return ID of the namespace or NULL if an error happened.
	 * @throws RemoteException
	 *             If some exception occurs
	 */
	public static NamespaceID getNamespaceID(final AccountID accountID, final PasswordCredential credential,
			final String namespaceName) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "NamespaceName" },
				new Object[] { accountID, credential, namespaceName });
		NamespaceID result = null;
		try {
			result = clientLib.getLogicModuleAPI().getNamespaceID(accountID, credential, namespaceName);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getNamespaceID", ex);
		}
		return result;
	}

	/**
	 * Retrieves the id of a namespace identified by name provided
	 * 
	 * @param accountID
	 *            ID of an account asking for the language
	 * @param credential
	 *            credential of account
	 * @param namespaceName
	 *            Name of the namespace
	 * @return ID of the namespace or NULL if an error happened.
	 * @throws RemoteException
	 *             If some exception occurs
	 */
	public static Langs getNamespaceLanguage(final AccountID accountID, final PasswordCredential credential,
			final String namespaceName) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "NamespaceName" },
				new Object[] { accountID, credential, namespaceName });
		Langs result = null;
		try {
			result = clientLib.getLogicModuleAPI().getNamespaceLang(accountID, credential, namespaceName);
		} catch (final Exception ex) {
			LOGGER.warn("Error during newgetNamespaceLanguage", ex);
		}
		return result;
	}

	/**
	 * Retrieves available namespaces from the account provided.
	 * 
	 * @param accountID
	 *            ID of the account retreiving the information.
	 * @param credential
	 *            Credentials of the account.
	 * @return set of namespaces names available from the account provided.
	 */
	public static Set<String> getNamespaces(final AccountID accountID, final PasswordCredential credential) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential" },
				new Object[] { accountID, credential });
		Set<String> result = null;
		try {
			result = clientLib.getLogicModuleAPI().getNamespaces(accountID, credential);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getNamespaces", ex);
		}
		return result;
	}

	/**
	 * Imports a class from a specific interface in a specific contract into the
	 * given namespace
	 * 
	 * @param accountID
	 *            ID of the account
	 * @param credential
	 *            credential of the account
	 * @param namespaceID
	 *            ID of the namespace where the class will be imported
	 * @param contractID
	 *            ID of the contract
	 * @param interfaceID
	 *            ID of the interface
	 * @return TRUE if the interface has been successuflly imported, FALSE otherwise
	 */
	public static boolean importInterface(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final ContractID contractID, final InterfaceID interfaceID) {
		clientLib.checkConnectionAndParams(
				new String[] { "AccountID", "Credential", "NamespaceID", "ContractID", "InterfaceID" },
				new Object[] { accountID, credential, namespaceID, contractID, interfaceID });
		try {
			clientLib.getLogicModuleAPI().importInterface(accountID, credential, namespaceID, contractID, interfaceID);
			return true;
		} catch (final Exception ex) {
			LOGGER.warn("Error during importInterface", ex);
		}
		return false;
	}

	/**
	 * Imports all the classes represented by the interfaces of the given contract
	 * into a specific namespace
	 * 
	 * @param accountID
	 *            ID of the account
	 * @param credential
	 *            credential of the account
	 * @param namespaceID
	 *            ID of the namespace where classes will be imported
	 * @param contractID
	 *            ID of the contract
	 * @return TRUE if the interface has been successuflly imported, FALSE otherwise
	 */
	public static boolean importContract(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final ContractID contractID) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "NamespaceID", "ContractID" },
				new Object[] { accountID, credential, namespaceID, contractID });
		try {
			clientLib.getLogicModuleAPI().importContract(accountID, credential, namespaceID, contractID);
			return true;
		} catch (final Exception ex) {
			LOGGER.warn("Error during importContract", ex);
		}
		return false;
	}

	// ============== DataSet Manager ============== //
	/**
	 * Method that creates a new namespace in the system. The account doing the
	 * action must have admin role.
	 * 
	 * @param accountID
	 *            the account id of the account or the responsible of the namespace
	 * @param credential
	 *            the credential of the account or the responsible of the namespace
	 * @param dataSet
	 *            the new dataset
	 * @return the id of the new namespace if the operation succeeds. null otherwise
	 */
	public static DataSetID newDataSet(final AccountID accountID, final PasswordCredential credential,
			final DataSet dataSet) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "DataSet" },
				new Object[] { accountID, credential, dataSet });
		DataSetID result = null;
		try {
			result = clientLib.getLogicModuleAPI().newDataSet(accountID, credential, dataSet);
		} catch (final Exception ex) {
			LOGGER.warn("Error during newDataSet", ex);
		}
		return result;
	}

	/**
	 * This method removes a dataset from the system by checking it has no active
	 * data contract associated with it, and no objects registered in it.
	 * 
	 * @param accountID
	 *            the account id of the responsible of the dataset (or admin)
	 * @param credential
	 *            credentials of the account
	 * @param datasetName
	 *            the name of the dataset to be removed
	 * @return True if the dataset is correctly removed. False otherwise.
	 */
	public static boolean removeDataset(final AccountID accountID, final PasswordCredential credential,
			final String datasetName) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "DataSetName" },
				new Object[] { accountID, credential, datasetName });
		try {
			clientLib.getLogicModuleAPI().removeDataSet(accountID, credential, datasetName);
			return true;
		} catch (final Exception ex) {
			LOGGER.warn("Error during removeDataset", ex);
		}
		return false;
	}

	/**
	 * Retrieves the id of a dataset identified by name provided
	 * 
	 * @param accountID
	 *            ID of an account asking for the ID
	 * @param credential
	 *            credential of account
	 * @param datasetName
	 *            Name of the dataset
	 * @return ID of the dataset or NULL if an error happened.
	 */
	public static DataSetID getDatasetID(final AccountID accountID, final PasswordCredential credential,
			final String datasetName) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "DataSetName" },
				new Object[] { accountID, credential, datasetName });
		DataSetID result = null;
		try {
			result = clientLib.getLogicModuleAPI().getDataSetID(accountID, credential, datasetName);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getDatasetID", ex);
		}
		return result;
	}

	/**
	 * Get all public datasets (which the account can be registered to)
	 * 
	 * @param accountID
	 *            id of the account performing request
	 * @param credential
	 *            credential of account performing request
	 * @return Set of names of public datasets
	 */
	public static Set<String> getPublicDatasets(final AccountID accountID, final PasswordCredential credential) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential" },
				new Object[] { accountID, credential });
		Set<String> result = null;
		try {
			result = clientLib.getLogicModuleAPI().getPublicDataSets(accountID, credential);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getPublicDatasets", ex);
		}
		return result;
	}

	/**
	 * Get datasets provided by the given account
	 * 
	 * @param accountID
	 *            id of the account
	 * @param credential
	 *            credential of the account
	 * @return Set of names of account's datasets.
	 */
	public static Set<String> getAccountDatasets(final AccountID accountID, final PasswordCredential credential) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential" },
				new Object[] { accountID, credential });
		Set<String> result = null;
		try {
			result = clientLib.getLogicModuleAPI().getAccountDataSets(accountID, credential);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getAccountDatasets", ex);
		}
		return result;
	}

	// ============== Class Manager ==============//

	/**
	 * This operation creates a new metaclass in the system of the provided
	 * className from the given classPath and associate it to the Namespace
	 * provided.
	 * 
	 * @param accountID
	 *            ID of the account of the user that calls the operation
	 * @param credentials
	 *            Credentials of the account provided
	 * @param namespace
	 *            Namespace in which to create the metaclass
	 * @param className
	 *            name of the class (packages included if necessary)
	 * @param classPath
	 *            class path of the class
	 * @param srcPath
	 * 			  path to the source files of the classes
	 * @param libPath
	 * 			  path to the libraries of the application (including dataclayClient)
	 * @return the information of all registered classes if the operation succeeds.
	 *         null otherwise.
	 */
	public static Map<String, MetaClass> newClass(final AccountID accountID, final PasswordCredential credentials,
			final String namespace, final String className, final String classPath, final String srcPath,
			final String libPath) {
		clientLib.checkConnectionAndParams(
				new String[] { "AccountID", "Credential", "Namespace", "ClassName", "ClassPath" },
				new Object[] { accountID, credentials, namespace, className, classPath });
		final Set<String> classNames = new HashSet<>();
		classNames.add(className);
		final Map<String, MetaClass> specsGen = generateSpecs(namespace, classPath, classNames);

		return newClassInternal(accountID, credentials, specsGen, classPath, srcPath, libPath);
	}

	/**
	 * This operation creates a new metaclass in the system for each class with name
	 * provided from the given classPath and associate it to the Namespace provided.
	 * 
	 * @param accountID
	 *            ID of the account of the user that calls the operation
	 * @param credentials
	 *            Credentials of the account provided
	 * @param namespace
	 *            Namespace in which to create the metaclass
	 * @param classNames
	 *            name of classes (packages included if necessary)
	 * @param classPath
	 *            class path of the classes
	 * @param srcPath
	 * 			  path to the source files of the classes
	 * @param libPath
	 * 			  path to the libraries of the application (including dataclayClient)
	 * @return the information of all registered classes if the operation succeeds.
	 *         null otherwise.
	 */
	public static Map<String, MetaClass> newClasses(final AccountID accountID, final PasswordCredential credentials,
			final String namespace, final Set<String> classNames, final String classPath, final String srcPath, 
			final String libPath) {
		clientLib.checkConnectionAndParams(
				new String[] { "AccountID", "Credential", "Namespace", "ClassNames", "ClassPath" },
				new Object[] { accountID, credentials, namespace, classNames, classPath });
		final Map<String, MetaClass> specsGen = generateSpecs(namespace, classPath, classNames);
		return newClassInternal(accountID, credentials, specsGen, classPath, srcPath, libPath);
	}

	/**
	 * Generate Specifications of classes
	 * 
	 * @param namespace
	 *            Namespace of class to register
	 * @param classPath
	 *            Path in where classes are located
	 * @param classNames
	 *            Names of classes to analyze
	 * @return Specifications of classes
	 */
	public static Map<String, MetaClass> generateSpecs(final String namespace, final String classPath,
			final Set<String> classNames) {
		clientLib.checkConnectionAndParams(new String[] { "Namespace", "ClassNames", "ClassPath" },
				new Object[] { namespace, classNames, classPath });
		newClassLock.lock();
		try {
			final JavaSpecGenerator specGen = new JavaSpecGenerator(classPath);
			final Map<String, MetaClass> newClasses = new HashMap<>();
			for (final String className : classNames) {
				if (newClasses.containsKey(className)) {
					continue; // Avoid registering same class twice.
				}
				final Map<String, MetaClass> classes = specGen.generateMetaClassSpecForRegisterClass(namespace,
						className);
				if (classes.isEmpty()) {
					LOGGER.info("Could not create Class spec of {}.", className);
				}
				newClasses.putAll(classes);
			}
			if (newClasses.isEmpty()) {
				LOGGER.info("Could not create any Class spec.");
			} else {
				/*
				 * for (MetaClass newClass : newClasses.values()) {
				 * newClass.setNamespace(namespace); }
				 */
				return newClasses;
			}
		} catch (final Exception ex) {
			ex.printStackTrace();
			LOGGER.warn("Error during generateSpecs", ex);
		} finally {
			newClassLock.unlock();
		}
		return null;
	}

	/**
	 * Internal function for newClass
	 * 
	 * @param accountID
	 *            ID of registrator
	 * @param credentials
	 *            Credentials
	 * @param newClasses
	 *            Specifications of classes to register
	 * @param classPath
	 *            Class path of new classes to register
	 * @param srcPath
	 * 			  path to the source files of the classes
	 * @param libPath
	 * 			  path to the libraries of the application (including dataclayClient)
	 * @return Registered classes
	 */
	public static Map<String, MetaClass> newClassInternal(final AccountID accountID,
			final PasswordCredential credentials, final Map<String, MetaClass> newClasses, final String classPath,
			final String srcPath, final String libPath) {

		/******** RT: Generate Prefetching Info ********/
		//if (Configuration.Flags.PREFETCHING_ENABLED.getBooleanValue()) {
		//	PrefetchingSpecGenerator.generateAndStorePrefetchingSpec(newClasses, classPath, srcPath, libPath);
		//}
		/***********************************************/

		final Map<String, MetaClass> result = clientLib.getLogicModuleAPI().newClass(accountID, credentials,
				Langs.LANG_JAVA, newClasses);
		return result;
	}

	/**
	 * Method that removes a class from the specified namespace (either removing the
	 * imports or because it was actually created in the namespace)
	 * 
	 * @param accountID
	 *            ID of the account responsible of the namespace of the class
	 * @param credential
	 *            Credential of the account responsible of the namespace of the
	 *            class
	 * @param namespaceID
	 *            ID of the namespace where the class is present
	 * @param className
	 *            name of the class to be removed from the namespace
	 * @return TRUE if it was successfully removed. FALSE, otherwise.
	 */
	public static boolean removeClass(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final String className) {

		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "NamespaceID", "ClassName" },
				new Object[] { accountID, credential, namespaceID, className });

		try {
			clientLib.getLogicModuleAPI().removeClass(accountID, credential, namespaceID, className);
			return true;
		} catch (final Exception ex) {
			LOGGER.warn("Error during removeClass", ex);
		}
		return false;
	}

	/**
	 * Method that removes an operation from the specified namespace
	 * 
	 * @param accountID
	 *            ID of the account responsible of the namespace of the class
	 *            containing the operation
	 * @param credential
	 *            Credential of the account responsible of the namespace of the
	 *            class containing the operation
	 * @param namespaceID
	 *            ID of the namespace of the operation.
	 * @param className
	 *            name of the class of the operation
	 * @param operationSignature
	 *            signature of the operation.
	 * @return TRUE if it was successfully removed. FALSE, otherwise.
	 */
	public static boolean removeOperation(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final String className, final String operationSignature) {

		clientLib.checkConnectionAndParams(
				new String[] { "AccountID", "Credential", "NamespaceID", "ClassName", "OperationSignature" },
				new Object[] { accountID, credential, namespaceID, className, operationSignature });

		try {
			clientLib.getLogicModuleAPI().removeOperation(accountID, credential, namespaceID, className,
					operationSignature);
			return true;
		} catch (final Exception ex) {
			LOGGER.warn("Error during removeOperation", ex);
		}
		return false;
	}

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
	 * @return TRUE if it was successfully removed. FALSE, otherwise.
	 */
	public static boolean removeImplementation(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final ImplementationID implementationID) {

		clientLib.checkConnectionAndParams(
				new String[] { "AccountID", "Credential", "NamespaceID", "ImplementationID" },
				new Object[] { accountID, credential, namespaceID, implementationID });

		try {
			clientLib.getLogicModuleAPI().removeImplementation(accountID, credential, namespaceID, implementationID);
			return true;
		} catch (final Exception ex) {
			LOGGER.warn("Error during removeImplementation", ex);
		}
		return false;
	}

	/**
	 * Method that retrieves the id of an operation given its signature
	 * 
	 * @param accountID
	 *            the account of the responsible of the namespace
	 * @param credential
	 *            the credentials of the account
	 * @param namespaceID
	 *            the ID of the namespace
	 * @param className
	 *            the name of the class
	 * @param operationSignature
	 *            signature of the operation
	 * @return the operation id if the operation succeeds. null otherwise.
	 */
	public static OperationID getOperationID(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final String className, final String operationSignature) {

		clientLib.checkConnectionAndParams(
				new String[] { "AccountID", "Credential", "NamespaceID", "ClassName", "OperationSignature" },
				new Object[] { accountID, credential, namespaceID, className, operationSignature });

		OperationID result = null;
		try {
			result = clientLib.getLogicModuleAPI().getOperationID(accountID, credential, namespaceID, className,
					operationSignature);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getOperationID", ex);
		}
		return result;
	}

	/**
	 * Method that retrieves the id of a property given its signature
	 * 
	 * @param accountID
	 *            the account of the responsible of the namespace
	 * @param credential
	 *            the credentials of the account
	 * @param namespaceID
	 *            the ID of the namespace
	 * @param className
	 *            the name of the class
	 * @param propertyName
	 *            the name of the property
	 * @return the id of the property if the operation succeeds. null otherwise
	 */
	public static PropertyID getPropertyID(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final String className, final String propertyName) {
		clientLib.checkConnectionAndParams(
				new String[] { "AccountID", "Credential", "NamespaceID", "ClassName", "PropertyName" },
				new Object[] { accountID, credential, namespaceID, className, propertyName });
		PropertyID result = null;
		try {
			result = clientLib.getLogicModuleAPI().getPropertyID(accountID, credential, namespaceID, className,
					propertyName);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getPropertyID", ex);
		}
		return result;
	}

	/**
	 * Method that retrieves the id of a class given its name
	 * 
	 * @param accountID
	 *            the account of the responsible of the namespace
	 * @param credential
	 *            the credentials of the account
	 * @param namespaceID
	 *            the ID of the namespace of the class
	 * @param className
	 *            the name of the class
	 * @return the id of the class if the operation succeeds. null otherwise
	 */
	public static MetaClassID getClassID(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final String className) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "NamespaceID", "ClassName" },
				new Object[] { accountID, credential, namespaceID, className });
		MetaClassID result = null;
		try {
			result = clientLib.getLogicModuleAPI().getClassID(accountID, credential, namespaceID, className);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getClassID", ex);
		}
		return result;
	}

	/**
	 * Method that retrieves the info of a class given its name.
	 * 
	 * @param accountID
	 *            the account of the responsible of the namespace.
	 * @param credential
	 *            the credentials of the account.
	 * @param namespaceID
	 *            the ID of the namespace of the class.
	 * @param className
	 *            the name of the class.
	 * @return the id of the class if the operation succeeds. null otherwise.
	 */
	public static MetaClass getClassInfo(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final String className) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "NamespaceID", "ClassName" },
				new Object[] { accountID, credential, namespaceID, className });
		MetaClass result = null;
		try {
			result = clientLib.getLogicModuleAPI().getClassInfo(accountID, credential, namespaceID, className);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getClassInfo", ex);
		}
		return result;
	}

	/**
	 * Method that retrieves the info of the classes registered in a specific
	 * namespace.
	 * 
	 * @param accountID
	 *            the account of the responsible of the namespace.
	 * @param credential
	 *            the credentials of the account.
	 * @param namespaceID
	 *            the ID of the namespace of the class.
	 * @return The info of the classes registered in the given namespace.
	 */
	public static Map<MetaClassID, MetaClass> getClassesInfoInNamespace(final AccountID accountID,
			final PasswordCredential credential, final NamespaceID namespaceID) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "NamespaceID" },
				new Object[] { accountID, credential, namespaceID });
		Map<MetaClassID, MetaClass> result = null;
		try {
			result = clientLib.getLogicModuleAPI().getInfoOfClassesInNamespace(accountID, credential, namespaceID);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getClassesInfoInNamespace", ex);
		}
		return result;
	}

	// ============== Contract Manager ==============//

	/**
	 * Method to register a new private contract.
	 * 
	 * @param proprietaryAccountID
	 *            The account of the contract provider
	 * @param proprietaryCredential
	 *            The credential of the contract provider
	 * @param contract
	 *            The specification of a contract. It includes the interfaces that
	 *            will be part of the contract, the date the contract starts (it
	 *            becomes active), and the date the contract expires
	 * @return the contract id if the operation succeeds. null otherwise.
	 */
	public static ContractID newPrivateContract(final AccountID proprietaryAccountID,
			final PasswordCredential proprietaryCredential, final Contract contract) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "Contract" },
				new Object[] { proprietaryAccountID, proprietaryCredential, contract });
		ContractID result = null;
		try {
			result = clientLib.getLogicModuleAPI().newContract(proprietaryAccountID, proprietaryCredential, contract);
		} catch (final Exception ex) {
			LOGGER.warn("Error during newPrivateContract", ex);
		}
		return result;
	}

	/**
	 * Method that registers a new contract
	 * 
	 * @param proprietaryAccountID
	 *            The account of the contract provider
	 * @param proprietaryCredential
	 *            The credential of the contract provider
	 * @param contract
	 *            The specification of a contract. It includes the interfaces that
	 *            will be part of the contract, the date the contract starts (it
	 *            becomes active), and the date the contract expires
	 * @return the contract id if the operation succeeds. null otherwise.
	 */
	public static ContractID newPublicContract(final AccountID proprietaryAccountID,
			final PasswordCredential proprietaryCredential, final Contract contract) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "Contract" },
				new Object[] { proprietaryAccountID, proprietaryCredential, contract });
		ContractID result = null;
		try {
			result = clientLib.getLogicModuleAPI().newContract(proprietaryAccountID, proprietaryCredential, contract);
		} catch (final Exception ex) {
			LOGGER.warn("Error during newPublicContract", ex);
		}
		return result;
	}

	/**
	 * Return all contracts info of public DataClay provider.
	 * 
	 * @param accountID
	 *            ID of the account querying.
	 * @param credential
	 *            Credential of the user.
	 * @return The info of the contracts of the namespace provider.
	 */
	public static ContractID getContractOfDataClayProvider(final AccountID accountID,
			final PasswordCredential credential) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential" },
				new Object[] { accountID, credential });
		ContractID result = null;
		try {
			result = clientLib.getLogicModuleAPI().getContractIDOfDataClayProvider(accountID, credential);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getContractOfDataClayProvider", ex);
		}
		return result;
	}

	/**
	 * Return all contracts info of the namespace provider.
	 * 
	 * @param accountID
	 *            ID of the account of the responsible of the namespace.
	 * @param credential
	 *            Credential of the user.
	 * @param namespaceID
	 *            ID of the namespace provider.
	 * @return The info of the contracts of the namespace provider.
	 */
	public static Map<ContractID, Contract> getContractsOfProvider(final AccountID accountID,
			final PasswordCredential credential, final NamespaceID namespaceID) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "NamespaceID" },
				new Object[] { accountID, credential, namespaceID });
		Map<ContractID, Contract> result = null;
		try {
			result = clientLib.getLogicModuleAPI().getContractIDsOfProvider(accountID, credential, namespaceID);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getContractsOfProvider", ex);
		}
		return result;
	}

	/**
	 * Return the info of all the contracts of the user (as applicant) specified.
	 * 
	 * @param accountID
	 *            ID of the account of the user.
	 * @param credential
	 *            Credential of the user.
	 * @return @return The info of the contracts of the user specified (as
	 *         applicant).
	 */
	public static Map<ContractID, Contract> getContractsOfApplicant(final AccountID accountID,
			final PasswordCredential credential) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential" },
				new Object[] { accountID, credential });
		Map<ContractID, Contract> result = null;
		try {
			result = clientLib.getLogicModuleAPI().getContractIDsOfApplicant(accountID, credential);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getContractsOfApplicant", ex);
		}
		return result;
	}

	/**
	 * Return the info of all the contracts of the user (as applicant) specified
	 * with the namespace provider specified.
	 * 
	 * @param accountID
	 *            ID of the account of the user.
	 * @param credential
	 *            Credential of the user.
	 * @param namespaceIDofProvider
	 *            ID of the namespace that provides the contracts to be retrieved.
	 * @return The info of the contracts of the user specified with the given
	 *         namespace provider.
	 */
	public static Map<ContractID, Contract> getContractsOfApplicant(final AccountID accountID,
			final PasswordCredential credential, final NamespaceID namespaceIDofProvider) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "NamespaceIDofProvider" },
				new Object[] { accountID, credential, namespaceIDofProvider });
		Map<ContractID, Contract> result = null;
		try {
			result = clientLib.getLogicModuleAPI().getContractIDsOfApplicantWithProvider(accountID, credential,
					namespaceIDofProvider);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getContractsOfApplicant", ex);
		}
		return result;
	}

	/**
	 * Method to register an account to a contract
	 * 
	 * @param accountID
	 *            The account of the applicant for the contract
	 * @param credential
	 *            The credential of the applicant for the contract
	 * @param contractID
	 *            ID of the contract in which to register.
	 * @return TRUE if the account was successfully registered to contract. FALSE,
	 *         otherwise.
	 */
	public static boolean registerToPublicContract(final AccountID accountID, final PasswordCredential credential,
			final ContractID contractID) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "ContractID" },
				new Object[] { accountID, credential, contractID });
		try {
			clientLib.getLogicModuleAPI().registerToPublicContract(accountID, credential, contractID);
			return true;
		} catch (final Exception ex) {
			LOGGER.warn("Error during registerToPublicContract", ex);
		}
		return false;
	}

	/**
	 * Method to register an account to a contract
	 * 
	 * @param accountID
	 *            The account of the applicant for the contract
	 * @param credential
	 *            The credential of the applicant for the contract
	 * @param namespaceID
	 *            ID of the namespace to check for a public contract
	 * @return the contractID corresponding to the public contract of the namespace
	 */
	public static ContractID registerToPublicContractOfNamespace(final AccountID accountID,
			final PasswordCredential credential, final NamespaceID namespaceID) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "NamespaceID" },
				new Object[] { accountID, credential, namespaceID });
		try {
			return clientLib.getLogicModuleAPI().registerToPublicContractOfNamespace(accountID, credential,
					namespaceID);
		} catch (final Exception ex) {
			LOGGER.warn("Error during registerToPublicContractOfNamespace", ex);
		}
		return null;
	}

	/**
	 * Method that allows to retrieve the stubs
	 * 
	 * @param applicantAccountID
	 *            the applicant of the contracts
	 * @param applicantCredential
	 *            the credentials of the applicant
	 * @param contractsIDs
	 *            the contracts ids of the user
	 * @return Byte arrays representing stubs and aspects
	 */
	public static Map<String, byte[]> getStubs(final AccountID applicantAccountID,
			final PasswordCredential applicantCredential, final LinkedList<ContractID> contractsIDs) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "ContractIDs" },
				new Object[] { applicantAccountID, applicantCredential, contractsIDs });
		Map<String, byte[]> stubs = null;
		try {
			// Get the stubs
			stubs = clientLib.getLogicModuleAPI().getStubs(applicantAccountID, applicantCredential, Langs.LANG_JAVA,
					contractsIDs);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getStubs", ex);
		}
		return stubs;
	}

	/**
	 * Method that allows to retrieve the stubs and prototypes of the given contract
	 * and store them to certain directory
	 * 
	 * @param applicantAccountID
	 *            the applicant of the contracts
	 * @param applicantCredential
	 *            the credentials of the applicant
	 * @param contractsIDs
	 *            the contracts ids of the user
	 * @param targetDirectoryPath
	 *            the directory path where stubs are saved
	 * @return Set of name of classes of stubs stored
	 */
	public static Set<String> getAndStoreStubs(final AccountID applicantAccountID,
			final PasswordCredential applicantCredential, final LinkedList<ContractID> contractsIDs,
			final String targetDirectoryPath) {
		clientLib.checkConnectionAndParams(
				new String[] { "AccountID", "Credential", "ContractIDs", "TargetDirectoryPath" },
				new Object[] { applicantAccountID, applicantCredential, contractsIDs, targetDirectoryPath });
		final Set<String> stubsStored = new HashSet<>();
		try {

			// Clear stubs infos cache
			DataClayObject.clearStubInfosCache();

			// Get the stubs
			final Map<String, byte[]> stubs = getStubs(applicantAccountID, applicantCredential, contractsIDs);

			// Create the target directory and store them in it
			FileAndAspectsUtils.createDirectory(targetDirectoryPath);
			for (final Entry<String, byte[]> curEntry : stubs.entrySet()) {
				final String className = curEntry.getKey();
				String extension = JAVACLASSEXT;
				if (className.endsWith("Aspect")) {
					extension = JAVAASPECTEXT;
					final String javaVersion = System.getProperty("java.version");
					if (!javaVersion.startsWith("1.7.") && !javaVersion.startsWith("1.8.")) {
						continue;
					}
				}
				if (className.endsWith("Yaml")) {
					extension = BABELEXT;
				}
				if (FileAndAspectsUtils.storeClass(targetDirectoryPath, className + extension,
						curEntry.getValue()) != null) {
					stubsStored.add(className);
				}
			}

		} catch (final Exception ex) {
			LOGGER.warn("Error during getAndStoreStubs", ex);
		}
		return stubsStored;
	}

	/**
	 * Method that allows to retrieve the stubs FOR ENRICHMENT of the given contract
	 * and store them to certain directory
	 * 
	 * @param applicantAccountID
	 *            the applicant of the contracts
	 * @param applicantCredential
	 *            the credentials of the applicant
	 * @param contractsIDs
	 *            the contracts ids of the user
	 * @param targetDirectoryPath
	 *            the directory path where stubs are saved
	 * @return TRUE if it succeeds, FALSE otherwise
	 */
	public static boolean getAndStoreStubsForEnrichment(final AccountID applicantAccountID,
			final PasswordCredential applicantCredential, final LinkedList<ContractID> contractsIDs,
			final String targetDirectoryPath) {
		throw new UnsupportedOperationException();
		// TODO: REVIEW enrichment stuff (jmarti 2018)
		/**
		 * try { // Get the stubs Map<String, byte[]> stubs =
		 * commonLib.getLogicModuleAPI().getStubsForEnrichment(applicantAccountID,
		 * applicantCredential, Langs.LANG_JAVA, contractsIDs);
		 * 
		 * // Create the target directory and store them in it
		 * FileAndAspectsUtils.createDirectory(targetDirectoryPath); for (Entry<String,
		 * byte[]> curEntry : stubs.entrySet()) { String className = curEntry.getKey();
		 * String extension = ".java"; if
		 * (FileAndAspectsUtils.storeClass(targetDirectoryPath, className + extension,
		 * curEntry.getValue()) == null) { return false; } } return true; } catch
		 * (Exception ex) { <<process the exception>> } return false;
		 **/
	}

	// ============== Interface Manager ==============//
	// PRIORITY - newInterface

	/**
	 * Method that registers a new interface
	 * 
	 * @param accountID
	 *            the account of the responsible of the namespace of the class
	 * @param credential
	 *            the credentials of the account
	 * @param interfaceSpec
	 *            Interface specification
	 * @return ID of the new interface created or NULL if it failed.
	 */
	public static InterfaceID newInterface(final AccountID accountID, final PasswordCredential credential,
			final Interface interfaceSpec) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "Interface" },
				new Object[] { accountID, credential, interfaceSpec });
		InterfaceID result = null;
		try {
			result = clientLib.getLogicModuleAPI().newInterface(accountID, credential, interfaceSpec);
		} catch (final Exception ex) {
			LOGGER.warn("Error during newInterface", ex);
		}
		return result;
	}

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
	 * @return TRUE if it was successfully removed. FALSE, otherwise.
	 */
	public static boolean removeInterface(final AccountID accountID, final PasswordCredential credential,
			final NamespaceID namespaceID, final InterfaceID interfaceID) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "NamespaceID", "InterfaceID" },
				new Object[] { accountID, credential, namespaceID, interfaceID });
		try {
			clientLib.getLogicModuleAPI().removeInterface(accountID, credential, namespaceID, interfaceID);
			return true;
		} catch (final Exception ex) {
			LOGGER.warn("Error during removeInterface", ex);
		}
		return false;
	}

	/**
	 * Call that performs a series of account creations, described by one YAML file.
	 * 
	 * @param adminID
	 *            The admin AccountID, in order to perform the newAccount calls.
	 * @param credential
	 *            The credential for admin account.
	 * @param yamlRequest
	 *            The YAML file containing the new accounts information.
	 * @return A YAML payload containing the ID for the new accounts.
	 */
	public static byte[] performSetOfNewAccounts(final AccountID adminID, final PasswordCredential credential,
			final byte[] yamlRequest) {
		byte[] yamlResponse = null;
		try {
			yamlResponse = clientLib.getLogicModuleAPI().performSetOfNewAccounts(adminID, credential, yamlRequest);
		} catch (final Exception ex) {
			LOGGER.warn("Error during performSetOfNewAccounts", ex);
		}
		return yamlResponse;
	}

	/**
	 * Call that performs a series of operations (user type), described by one YAML
	 * file.
	 * 
	 * @param performerID
	 *            The performer AccountID, user responsible for all the operations.
	 * @param credential
	 *            The credential for the performer account.
	 * @param yamlRequest
	 *            The YAML file containing the information for the set of
	 *            operations.
	 * @return A YAML payload containing the new dataClay IDs, resulting from this
	 *         call.
	 */
	public static byte[] performSetOfOperations(final AccountID performerID, final PasswordCredential credential,
			final byte[] yamlRequest) {
		byte[] yamlResponse = null;
		try {
			yamlResponse = clientLib.getLogicModuleAPI().performSetOfOperations(performerID, credential, yamlRequest);
		} catch (final Exception ex) {
			LOGGER.warn("Error during performSetOfOperations", ex);
		}
		return yamlResponse;
	}

	/**
	 * Method that retrieves the info of the interface if the account is registered
	 * in a contract that contains it
	 * 
	 * @param accountID
	 *            ID of the account registered in a contract with the interface
	 *            present
	 * @param credential
	 *            the credential of the account
	 * @param interfaceID
	 *            the ID of the interface to be retrieved
	 * @return info of the interface
	 */
	public static Interface getInterfaceInfo(final AccountID accountID, final PasswordCredential credential,
			final InterfaceID interfaceID) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "InterfaceID" },
				new Object[] { accountID, credential, interfaceID });
		Interface result = null;
		try {
			result = clientLib.getLogicModuleAPI().getInterfaceInfo(accountID, credential, interfaceID);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getInterfaceInfo", ex);
		}
		return result;
	}

	// ============== Data Contract Manager ==============//

	/**
	 * Method to register a new private data contract.
	 * 
	 * @param proprietaryAccountID
	 *            The account of the contract provider
	 * @param proprietaryCredential
	 *            The credential of the contract provider
	 * @param datacontract
	 *            The specification of a contract. It includes the date the contract
	 *            starts (it becomes active) and the date the contract expires
	 * @return the contract id if the operation succeeds. null otherwise.
	 */
	public static DataContractID newPrivateDataContract(final AccountID proprietaryAccountID,
			final PasswordCredential proprietaryCredential, final DataContract datacontract) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "DataContract" },
				new Object[] { proprietaryAccountID, proprietaryCredential, datacontract });
		DataContractID result = null;
		try {
			result = clientLib.getLogicModuleAPI().newDataContract(proprietaryAccountID, proprietaryCredential,
					datacontract);
		} catch (final Exception ex) {
			LOGGER.warn("Error during newPrivateDataContract", ex);
		}
		return result;
	}

	/**
	 * Method that registers a new public data contract
	 * 
	 * @param proprietaryAccountID
	 *            The account of the contract provider
	 * @param proprietaryCredential
	 *            The credential of the contract provider
	 * @param datacontract
	 *            The specification of a contract. It includes the date the contract
	 *            starts (it becomes active) and the date the contract expires
	 * @return the contract id if the operation succeeds. null otherwise.
	 */
	public static DataContractID newPublicDataContract(final AccountID proprietaryAccountID,
			final PasswordCredential proprietaryCredential, final DataContract datacontract) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "DataContract" },
				new Object[] { proprietaryAccountID, proprietaryCredential, datacontract });
		DataContractID result = null;
		try {
			result = clientLib.getLogicModuleAPI().newDataContract(proprietaryAccountID, proprietaryCredential,
					datacontract);
		} catch (final Exception ex) {
			LOGGER.warn("Error during newPublicDataContract", ex);
		}
		return result;
	}

	/**
	 * Return all contracts info of the dataset provider.
	 * 
	 * @param accountID
	 *            ID of the account of the responsible of the dataset.
	 * @param credential
	 *            Credential of the user.
	 * @param datasetID
	 *            ID of the dataset provider.
	 * @return The info of the contracts of the namespace provider.
	 */
	public static Map<DataContractID, DataContract> getDataContractsOfProvider(final AccountID accountID,
			final PasswordCredential credential, final DataSetID datasetID) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "DataSetID" },
				new Object[] { accountID, credential, datasetID });
		Map<DataContractID, DataContract> result = null;
		try {
			result = clientLib.getLogicModuleAPI().getDataContractIDsOfProvider(accountID, credential, datasetID);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getDataContractsOfProvider", ex);
		}
		return result;
	}

	/**
	 * Return the info of all the data contracts of the user (as applicant)
	 * specified.
	 * 
	 * @param accountID
	 *            ID of the account of the user.
	 * @param credential
	 *            Credential of the user.
	 * @return @return The info of the data contracts of the user specified (as
	 *         applicant).
	 */

	public static Map<DataContractID, DataContract> getDataContractsOfApplicant(final AccountID accountID,
			final PasswordCredential credential) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential" },
				new Object[] { accountID, credential });
		Map<DataContractID, DataContract> result = null;
		try {
			result = clientLib.getLogicModuleAPI().getDataContractIDsOfApplicant(accountID, credential);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getDataContractsOfApplicant", ex);
		}
		return result;
	}

	/**
	 * Return the info of all the data contracts of the user (as applicant)
	 * specified with the dataset provider specified.
	 * 
	 * @param accountID
	 *            ID of the account of the user.
	 * @param credential
	 *            Credential of the user.
	 * @param datasetIDofProvider
	 *            ID of the dataset that provides the contracts to be retrieved.
	 * @return The info of the data contracts of the user specified with the given
	 *         dataset provider.
	 */
	public static DataContract getDataContractInfoOfApplicantWithProvider(final AccountID accountID,
			final PasswordCredential credential, final DataSetID datasetIDofProvider) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "DataSetID" },
				new Object[] { accountID, credential, datasetIDofProvider });
		DataContract result = null;
		try {
			result = clientLib.getLogicModuleAPI().getDataContractInfoOfApplicantWithProvider(accountID, credential,
					datasetIDofProvider);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getDataContractInfoOfApplicantWithProvider", ex);
		}
		return result;
	}

	/**
	 * Method to register an account to a data contract
	 * 
	 * @param accountID
	 *            The account of the applicant for the data contract
	 * @param credential
	 *            The credential of the applicant for the data contract
	 * @param datacontractID
	 *            ID of the contract in which to register.
	 * @return TRUE if the account was successfully registered to data contract.
	 *         FALSE, otherwise.
	 */
	public static boolean registerToPublicDataContract(final AccountID accountID, final PasswordCredential credential,
			final DataContractID datacontractID) {
		clientLib.checkConnectionAndParams(new String[] { "AccountID", "Credential", "DataContractID" },
				new Object[] { accountID, credential, datacontractID });
		try {
			clientLib.getLogicModuleAPI().registerToPublicDataContract(accountID, credential, datacontractID);
			return true;
		} catch (final Exception ex) {
			LOGGER.warn("Error during registerToPublicDataContract", ex);
		}
		return false;
	}

	/**
	 * Method that retrieves the info of the execution environments of a specific
	 * language
	 *
	 * @param language
	 *            language of the backends to be retrieved
	 * @param forceUpdateCache Indicates cache of EEs must be updated
	 * @return info of the of the execution environments of a specific language, indexed by their ID
	 */
	public static Map<ExecutionEnvironmentID, ExecutionEnvironment> getExecutionEnvironmentsInfo(final Langs language,
																								 boolean forceUpdateCache) {
		try {
			if (language == null || language.equals(Langs.LANG_NONE)) {
				LOGGER.error("A specific language must be provided");
				return null;
			}
			return clientLib.getAllExecutionEnvironmentsInfo(language, forceUpdateCache);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getExecutionEnvironmentsInfo", ex);
			return null;
		}
	}

	/**
	 * Method that retrieves the info of the registered backends assuming that they
	 * might have 1 storage location and/or 1 exec environment
	 * 
	 * @param accountID
	 *            ID of the account
	 * @param credential
	 *            credential of the account
	 * @param backendLanguage
	 *            Language of the backend.
	 * @return info of the registered backends indexed by their names
	 */
	public static Set<String> getBackendNames(final AccountID accountID, final PasswordCredential credential,
			final Langs backendLanguage) {
		return clientLib.getAllBackendsNames(backendLanguage, true);
	}

	/**
	 * Register Event listener implementation i.e. method that must be executed
	 * every time a certain event (and its conditions) occurs.
	 * 
	 * @param accountID
	 *            ID of the account of the user registering event listener.
	 * @param credentials
	 *            Credentials of the user
	 * @param eventListenerImpl
	 *            Event listener implementation. The user must be the responsible of
	 *            the implementation.
	 */
	public static void registerEventListenerImpl(final AccountID accountID, final PasswordCredential credentials,
			final ECA eventListenerImpl) {
		clientLib.getLogicModuleAPI().registerEventListenerImplementation(accountID, credentials, eventListenerImpl);
	}

	// ============== Listener Registering ==============//

	

	// ============== UTILS ==============//

	/**
	 * Method that registers the info of a dataClay instance
	 * 
	 * @param dcHost
	 *            entry port host of the external dataClay
	 * @param dcPort
	 *            entry point port of the external dataClay
	 * @return ID of external registered dataClay.
	 */
	public static DataClayInstanceID registerExternalDataClay(final String dcHost, final int dcPort) {
		clientLib.checkConnectionAndParams(new String[] { "dcID", "dcName", "dcHost", "dcPort" },
				new Object[] { dcHost, dcPort });
		try {
			return clientLib.registerExternalDataClay(dcHost, dcPort);
		} catch (final Exception ex) {
			LOGGER.warn("Error during registerExternalDataClay", ex);
			return null;
		}
	}
	
	/**
	 * ADMIN usage only. Method that registers the info of a dataClay instance but with overriden authority for SSL connections.
	 * @param adminAccountID admin account id
	 * @param  adminCredential admin credentials
	 * @param dcHost
	 *            entry port host of the external dataClay
	 * @param dcPort
	 *            entry point port of the external dataClay
	 * @param authority authority to use
	 * @return ID of external registered dataClay.
	 */
	public static DataClayInstanceID registerExternalDataClayOverrideAuthority(final AccountID adminAccountID,
			final PasswordCredential adminCredential, final String dcHost, final int dcPort, final String authority) {
		clientLib.checkConnectionAndParams(new String[] { "AdminAccountID", "AdminCredential", "dcHost", "dcPort" , "authority"},
				new Object[] { adminAccountID, adminCredential, dcHost, dcPort, authority });
		try {
			return clientLib.registerExternalDataClayOverrideAuthority(adminAccountID, adminCredential, dcHost, dcPort, authority);
		} catch (final Exception ex) {
			LOGGER.warn("Error during registerExternalDataClay", ex);
			return null;
		}
	}
	
	/**
	 * Method that updates the info of a dataClay instance (adds new "access")
	 * 
	 * @param dcID ID of the dataclay to update
	 * @param dcHost
	 *            entry port host of the external dataClay
	 * @param dcPort
	 *            entry point port of the external dataClay
	 */
	public static void updateExternalDataClay(final DataClayInstanceID dcID, final String dcHost, final int dcPort) {
		clientLib.checkConnectionAndParams(new String[] { "dcID", "dcHost", "dcPort" },
				new Object[] { dcID, dcHost, dcPort });
		try {
			clientLib.getLogicModuleAPI().notifyRegistrationOfExternalDataClay(dcID, dcHost, dcPort);
		} catch (final Exception ex) {
			LOGGER.warn("Error during updateExternalDataClay", ex);
		}
	}

	/**
	 * Tries to connect to an external dataClay instance and retrieve its ID. *
	 * 
	 * @param dcHost
	 *            hostname of the external dataClay instance
	 * @param dcPort
	 *            port of the external dataClay instance
	 * @return id of the external dataClay instance
	 */
	public static DataClayInstanceID getExternalDataClayID(final String dcHost, final int dcPort) {
		try {
			return clientLib.getExternalDataClayID(dcHost, dcPort);
		} catch (final Exception ex) {
			LOGGER.warn("Error during getExternalDataClayID", ex);
			return null;
		}
	}

	/**
	 * Method that retrieves the current dataClay identifier
	 * 
	 * @return id of the current dataClay (current dataClay is which this clientLib
	 *         is being connected to)
	 */
	public static DataClayInstanceID getDataClayID() {
		return clientLib.getDataClayID();
	}

	/**
	 * Unfederate all objects belonging/federated with external dataClay with id provided
	 * @param extDataClayID External dataClay ID
	 */
	public static void unfederateAllObjects(final DataClayInstanceID extDataClayID) {
		clientLib.unfederateAllObjects(extDataClayID);
	}
	
	/**
	 * Unfederate all objects belonging/federated with ANY external dataClay 
	 */
	public static void unfederateAllObjectsWithAllDCs() {
		clientLib.unfederateAllObjectsWithAllDCs();
	}
	
	/**
	 * Migrate (unfederate and federate) all current dataClay objects from specified external dataclay di to
	 * destination dataclay. 
	 * @param originDataClayID Origin dataclay id
	 * @param destinationDataClayID Destination dataclay id
	 */
	public static void migrateFederatedObjects(final DataClayInstanceID originDataClayID, 
			final DataClayInstanceID destinationDataClayID) {
		clientLib.migrateFederatedObjects(originDataClayID, destinationDataClayID);
	}

	/**
	 * Federate all dataClay objects from specified current dataClay
	 * destination dataclay. 
	 * @param destinationDataClayID Destination dataclay id
	 */
	public static void federateAllObjects(
			final DataClayInstanceID destinationDataClayID) {
		clientLib.federateAllObjects(destinationDataClayID);
	}

	/**
	 * Import classes in namespace specified from an external dataClay
	 * @param externalNamespace External namespace to get
	 * @param extDataClayID External dataClay ID
	 */
	public static void importModelsFromExternalDataClay(final String externalNamespace,
															 final DataClayInstanceID extDataClayID) {
		clientLib.importModelsFromExternalDataClay(externalNamespace, extDataClayID);
	}
	
	/**
	 * Activate tracing in dataClay services
	 * 
	 */
	public final static void activateTracingInDataClayServices() {
		clientLib.activateTracingInDataClayServices();
	}
	
	/**
	 * Dectivate tracing
	 */
	public final static void deactivateTracingInDataClayServices() {
		clientLib.deactivateTracingInDataClayServices();
	}

	/**
	 * Activate tracing
	 */
	public final static void activateTracing(
			final boolean initializeWrapper) {
		clientLib.activateTracing(initializeWrapper);
	}

	/**
	 * Deactivate tracing
	 */
	public final static void deactivateTracing(final boolean finalizeWrapper) {
		clientLib.deactivateTracing(finalizeWrapper);
	}
	
	/**
	 * Get traces in dataClay services and store it in current workspace
	 */
	public final static void getTracesInDataClayServices() {
		clientLib.getTracesInDataClayServices();
	}
	
	/**
	 * Wait for asynchronous requests to finish.
	 */
	public static void waitForAsyncRequestToFinish() {
		if (clientLib != null) {
			// Register pending objects
			clientLib.waitForAsyncRequestToFinish();
		}
	}
}
