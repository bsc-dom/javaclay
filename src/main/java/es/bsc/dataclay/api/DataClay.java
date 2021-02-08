package es.bsc.dataclay.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.commons.Method;

import es.bsc.dataclay.DataClayMockObject;
import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.commonruntime.ClientManagementLib;
import es.bsc.dataclay.commonruntime.ClientRuntime;
import es.bsc.dataclay.commonruntime.DataClayRuntime;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.exceptions.metadataservice.ObjectNotRegisteredException;
import es.bsc.dataclay.extrae.DataClayExtrae;
import es.bsc.dataclay.tool.GetBackends;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ProcessEnvironment;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.SessionID;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;
import es.bsc.dataclay.util.management.metadataservice.MetaDataInfo;
import es.bsc.dataclay.util.structs.Triple;

/**
 * This class contains the dataClay public API.
 */
public final class DataClay {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger("DataClay.api");

	/** Default config file location */
	private static final String CONFIGFILEPATH = "." + File.separatorChar + "cfgfiles" + File.separatorChar
			+ "session.properties";

	/** Environment variable where configuration file can be specified. */
	public static final String CONFIGFILEPATH_ENV = "DATACLAYSESSIONCONFIG";

	/** Indicates if debug is enabled. */
	protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();
	/** Prop name for account. */
	private static final String ACCOUNT_PROP = "Account";
	/** Prop name for password. */
	private static final String PASSWORD_PROP = "Password";
	/** Prop name for stubs classpath. */
	private static final String CLASSPATH_STUBS_PROP = "StubsClasspath";
	/** Prop name for datasets. */
	private static final String DATASETS_PROP = "DataSets";
	/** Prop name for default dataset for store. */
	private static final String DATASET_STORE = "DataSetForStore";
	/** Prop name for defining client.properties path. */
	private static final String DATACLAY_CLIENT_CONFIG = "DataClayClientConfig";
	/** Prop name for defining global.properties path. */
	private static final String DATACLAY_GLOBAL_CONFIG = "DataClayGlobalConfig";
	/** Prop name for defining if tracing is enabled. */
	private static final String TRACING_ENABLED = "Tracing";
	/** Prop name for defining first available Extrae task ID. */
	private static final String EXTRAE_STARTING_TASK_ID = "ExtraeStartingTaskID";
	/** Indicates path to extrae wrapper library. */
	private static final String JAVACLAY_EXTRAE_WRAPPER_LIB_PROP = "javaClayExtraeWrapperLib";
	/** Prop name for the contracts (optional, used under Python). */
	private static final String CONTRACTS_PROP = "Contracts";
	/** Prop name to specify the "local" backend */
	private static final String LOCAL_BACKEND = "LocalBackend";

	/** Token that user might use to specify the 'local' location in a data op */
	private static final String LOCALTOKEN = "LOCAL";

	/** Separator token for datasets. */
	public static final String DATASET_SEPARATOR_TOKEN = ":";

	/** Indicates Extrae was initialized by COMPSs. */
	private static boolean EXTRAE_COMPSS = false;

	/**
	 * UserClientLib for the session. WARNING: CURRENTLY IS PUBLIC IN ORDER TO ALLOW
	 * THREADS CREATED IN CLIENT TO USE THE COMMONLIB. Check design.
	 */
	public static ClientRuntime commonLib;
	/** Backends by name. */
	private static Map<String, Set<BackendID>> pyBackendsByName;
	private static Map<String, Set<BackendID>> jBackendsByName;
	/** Execution Environments by hostname. */
	private static Map<String, Set<BackendID>> pyBackendsByHostname;
	private static Map<String, Set<BackendID>> jBackendsByHostname;
	/** Backends by ID. */
	private static ConcurrentHashMap<BackendID, Backend> backendsByID;
	private static ConcurrentHashMap<BackendID, Backend> jBackendsByID;
	private static ConcurrentHashMap<BackendID, Backend> pyBackendsByID;

	/** LOCAL BackendID */
	public static BackendID jLOCAL;
	public static BackendID pLOCAL;

	/**
	 * Forbidden constructor
	 */
	private DataClay() {

	}

	private static String setCfgFilePath = null;

	public static void setSessionFile(final String path) throws DataClayException {
		if (path == null || path.isEmpty()) {
			throw new DataClayException("Null or empty config file path");
		}
		setCfgFilePath = path;
	}

	/**
	 * Finish connections to DataClay.
	 *
	 * @throws DataClayException
	 *             if an exception occurs
	 */
	public static void finish() throws DataClayException {
		try {

			if (DataClayExtrae.extraeTracingIsEnabled()) {
				LOGGER.info("Extrae is active, deactivating it...");
				if (EXTRAE_COMPSS) {
					if (DataClayExtrae.getWrapperTaskID() == 0) {
						LOGGER.info("Calling deactivate extrae in dataclay services");
						DataClay.deactivateTracingInDataClayServices();
						LOGGER.info("Getting traces in dataclay services");

						deactivateTracing(true);
						getTracesInDataClayServices();
					}  else {
						deactivateTracing(false);
					}

				} else {

					if (DataClayExtrae.getWrapperTaskID() == 0) {
						LOGGER.info("Calling deactivate extrae in dataclay services");
						DataClay.deactivateTracingInDataClayServices();
						LOGGER.info("Getting traces in dataclay services");
						deactivateTracing(true);
						getTracesInDataClayServices();


						// Merge

						// -- Linux --
						String command = "mpi2prv -keep-mpits -no-syn -f TRACE.mpits -o ./trace/dctrace.prv";
						// Run a shell command
						System.out.println(command);
						Process process = Runtime.getRuntime().exec(command);

						// Run a shell script
						// Process process = Runtime.getRuntime().exec("path/to/hello.sh");

						// -- Windows --
						// Run a command
						//Process process = Runtime.getRuntime().exec("cmd /c dir C:\\Users\\mkyong");
						BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
						BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
						String line;
						while ((line = input.readLine()) != null) {
							System.out.println(line);
						}
						while ((line = err.readLine()) != null) {
							System.err.println(line);
						}
						System.out.flush();
						System.err.flush();
						try {
							process.waitFor();  // wait for process to complete
						} catch (InterruptedException e) {
							System.err.println(e);  // "Can'tHappen"
						}

					} else {
						deactivateTracing(true);
					}

				}


			}
		} catch (final Exception e) {
			e.printStackTrace();
		}

		try {
			ClientManagementLib.finishConnections();
		} catch (final Exception e) {
			throw new DataClayException(e);
		}
	}

	/**
	 * Method that initializes the lib.
	 *
	 * @throws DataClayException
	 *             if an exception occurs
	 */
	public static void init() throws DataClayException {
		initInternal(1, Langs.LANG_JAVA);
	}

	/**
	 * Initialize dataClay waiting for the following number of backends of language provided.
	 * @param numBackends
	 *            Number of backends to wait for
	 * @param language language of backend to wait for
	 * @throws DataClayException
	 *             if an exception occurs
	 */
	public static void init(final int numBackends, final Langs language) throws DataClayException {
		initInternal(numBackends, language);
	}

	/**
	 * Initialize dataClay waiting for the following number of backends of language provided.
	 * @param numBackends
	 *            Number of backends to wait for
	 * @param language language of backend to wait for
	 * @throws DataClayException
	 *             if an exception occurs
	 */
	private static void initInternal(final int numBackends, final Langs language) throws DataClayException {
		try {
			final File f;
			String configFilePath;
			if (setCfgFilePath != null) {
				configFilePath = setCfgFilePath;
			} else {
				configFilePath = ProcessEnvironment.getInstance().get(CONFIGFILEPATH_ENV);
			}
			if (configFilePath != null && !configFilePath.isEmpty()) {
				f = new File(configFilePath);
				if (f.isFile() && f.exists()) {
					LOGGER.info("Found {}. Initializing session with properties located at {}", CONFIGFILEPATH_ENV,
							configFilePath);
				}
			} else {
				final Path path = Paths.get(CONFIGFILEPATH).normalize();
				LOGGER.info("Session file not found or {}  not properly set Trying default location {}",
						CONFIGFILEPATH_ENV, path.toAbsolutePath());
				configFilePath = path.toAbsolutePath().toString();
				f = new File(configFilePath);
			}

			final FileInputStream configFile = new FileInputStream(f);
			final Properties prop = new Properties();
			prop.load(configFile);
			configFile.close();

			// Parse global properties
			final String globalConfig = prop.getProperty(DATACLAY_GLOBAL_CONFIG);
			if (globalConfig != null) {
				Configuration.Flags.reloadGlobalConfiguration(globalConfig);
			}

			// Parse config properties
			String dcClientConfig = prop.getProperty(DATACLAY_CLIENT_CONFIG);
			if (dcClientConfig != null) {
				dcClientConfig = dcClientConfig.trim();
				ClientManagementLib.initializeCMLib(dcClientConfig);
			} else {
				ClientManagementLib.initializeCMLib(null);
			}

			// Wait for one DS to be ready
			try {
				while ( GetBackends.getBackends("admin","admin",language).size() < numBackends) {
					System.out.println("[dataClay] Waiting for backend to be ready...");
					Thread.sleep(2000L); //sleep 2 seconds
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}

			final String account = prop.getProperty(ACCOUNT_PROP).trim();
			final AccountID accountID = ClientManagementLib.getAccountID(account);

			final String password = prop.getProperty(PASSWORD_PROP).trim();
			final PasswordCredential credential = new PasswordCredential(password);

			final String dataSetsStr = prop.getProperty(DATASETS_PROP);
			final String[] dataSetsArray = dataSetsStr.split(DATASET_SEPARATOR_TOKEN);
			final HashSet<String> dataSets = new HashSet<>();
			for (final String dataSet : dataSetsArray) {
				dataSets.add(dataSet.trim());
			}
			final String dataSetForStore = prop.getProperty(DATASET_STORE).trim();

			// The properties may contain the list of contracts
			final String allContracts = prop.getProperty(CONTRACTS_PROP);

			// Init session
			final SessionID sessionID;
			if (allContracts == null) {
				// No contracts specified, walk the stubs
				final String classPathOfStubs = prop.getProperty(CLASSPATH_STUBS_PROP);
				sessionID = ClientManagementLib.newSession(accountID, credential, classPathOfStubs, dataSets,
						dataSetForStore);
			} else {
				// Contracts are a comma-separated field, used in Python code
				final Set<ContractID> contracts = new HashSet<>();
				for (final String contract : allContracts.split(",")) {
					contracts.add(new ContractID(contract.trim()));
				}

				sessionID = ClientManagementLib.newSession(accountID, credential, contracts, dataSets, dataSetForStore);
			}

			// Init all backend structures
			initBackendsInternalInfo(accountID, credential);

			// Init commonLib
			commonLib = ClientManagementLib.getDataClayClientLib();

			// Init local backend if needed
			final String localBackendName = prop.getProperty(LOCAL_BACKEND);
			if (localBackendName != null) {
				Set<BackendID> localBackends = jBackendsByName.get(localBackendName);
				if (localBackends != null && !localBackends.isEmpty()) {
					BackendID bkID = null;
					// Get first backend ID, which is an almost random policy
					for (final BackendID aBkID : localBackends) {
						bkID = aBkID;
						break;
					}

					commonLib.setLocalBackend(bkID);
					jLOCAL = bkID;
				}

				localBackends = pyBackendsByName.get(localBackendName);
				if (localBackends != null && !localBackends.isEmpty()) {
					BackendID bkID = null;
					// Get first backend ID, which is an almost random policy
					for (final BackendID aBkID : localBackends) {
						bkID = aBkID;
						break;
					}

					commonLib.setLocalBackend(bkID);
					pLOCAL = bkID;
				}
			}

			LOGGER.info("Session: {}", sessionID);
			LOGGER.info("dataClay ID: {}", commonLib.getDataClayID());
			LOGGER.info("Backends: {}", getJBackendsByName());
			LOGGER.info("Python Backends: {}", getPyBackendsByName());
			if (localBackendName != null) {
				LOGGER.info("Default {} backend: {}", LOCALTOKEN, localBackendName);
			}


			String javaClayExtraeWrapperLib = prop.getProperty(JAVACLAY_EXTRAE_WRAPPER_LIB_PROP);
			if (javaClayExtraeWrapperLib!= null) {
				Configuration.Flags.JAVACLAY_EXTRAE_WRAPPER_LIB.setValue(javaClayExtraeWrapperLib);
			}
			/***************************** TRACING **********************************/
			/**
			 * ### READ #### 
			 * Activating tracing with tracing_enabled property set True and starting task id = 0
			 * means we are only tracing dataClay. dataClay client will not increment current available
			 * task ID and will send a 0 to LM, which will understand the 0 as  "only dataClay tracing"
			 * since for compss it is never 0.
			 * Activating tracing with tracing_enabled property set True and starting task id != 0 means
			 * we are tracing COMPSs  and dataClay.
			 * Current client will not initialize Extrae or increment task id since COMPSs already initializes
			 * it for us (as a worker).
			 * In any case, none of them needs to add synchronization event or increment the available task id (only services).
			 * Incrementing available task id is useful to send to N EE/DS nodes.
			 * Remember that activating traces does not mean the application will be traced if Aspects are not properly applied.
			 */
			final String tracingEnabledStr = prop.getProperty(TRACING_ENABLED);
			boolean tracingEnabled = false;
			if (tracingEnabledStr != null) {
				tracingEnabled = Boolean.valueOf(tracingEnabledStr);
			}
			if (tracingEnabled) {
				LOGGER.info("Extrae tracing requested");
				final String strStartingTaskID = prop.getProperty(EXTRAE_STARTING_TASK_ID);
				// Trace with Extrae if enabled. Application will NOT be traced if no 
				// initialization was done (COMPSs initializes it for us) 
				// or Paraver aspects injection were NOT applied.
				LOGGER.info("Found Starting task id = " + strStartingTaskID);
				if (strStartingTaskID == null || strStartingTaskID.equals("0")) {
					DataClay.activateTracing(true);
					if (DataClayExtrae.getWrapperTaskID() == 0) {
						LOGGER.info("Activating extrae in all nodes");
						DataClay.activateTracingInDataClayServices();
					}

				} else {
					EXTRAE_COMPSS = true;

					DataClayExtrae.setCurrentAvailableTaskID(Integer.valueOf(strStartingTaskID));

					// Starting task ID specified, Extrae is supposed to be previously initialized
					DataClay.activateTracing(false);
					LOGGER.info("Found Extrae tracing Task ID specified: {}", strStartingTaskID);
					if (DataClayExtrae.getWrapperTaskID() == 0) { // only in master node (app client withou compss)
						LOGGER.info("Activating extrae in all nodes");
						DataClay.activateTracingInDataClayServices();
					}
				}

			}

			/****************************************************************************/

		} catch (final Exception ex) {
			LOGGER.debug("Exception during init()", ex);
			throw new DataClayException(ex);
		}
	}

	/**
	 * This method initializes the structures containing backend info (st loc and
	 * exec env) present in any hostname.
	 *
	 * @throws DataClayException
	 *             if an exception occurs
	 */
	private static void initBackendsInternalInfo(final AccountID accountID, final PasswordCredential credential)
			throws DataClayException {
		try {
			backendsByID = new ConcurrentHashMap<>();

			jBackendsByName = new ConcurrentHashMap<>();
			jBackendsByHostname = new ConcurrentHashMap<>();

			jBackendsByID = new ConcurrentHashMap<>(
					ClientManagementLib.getExecutionEnvironmentsInfo(accountID, credential, Langs.LANG_JAVA));

			backendsByID.putAll(jBackendsByID);

			for (final Backend backend : jBackendsByID.values()) {
				final BackendID bkID = backend.getDataClayID();

				final String hostname = backend.getHostname();
				final String name = backend.getName();

				Set<BackendID> setExecEnvsPerHostname = jBackendsByHostname.get(hostname);
				Set<BackendID> setExecEnvsPerName = jBackendsByName.get(name);

				if (setExecEnvsPerHostname == null) {
					setExecEnvsPerHostname = new HashSet<>();
					jBackendsByHostname.put(hostname, setExecEnvsPerHostname);
				}

				if (setExecEnvsPerName == null) {
					setExecEnvsPerName = new HashSet<>();
					jBackendsByName.put(hostname, setExecEnvsPerName);
				}

				setExecEnvsPerHostname.add(bkID);
				setExecEnvsPerName.add(bkID);
			}

			LOGGER.info("Java Backends: {}", jBackendsByID);

			pyBackendsByName = new ConcurrentHashMap<>();
			pyBackendsByHostname = new ConcurrentHashMap<>();

			pyBackendsByID = new ConcurrentHashMap<>(
					ClientManagementLib.getExecutionEnvironmentsInfo(accountID, credential, Langs.LANG_PYTHON));

			backendsByID.putAll(pyBackendsByID);

			for (final Backend backend : pyBackendsByID.values()) {
				final BackendID bkID = backend.getDataClayID();

				final String hostname = backend.getHostname();
				final String name = backend.getName();

				Set<BackendID> setExecEnvsPerHostname = pyBackendsByHostname.get(hostname);
				Set<BackendID> setExecEnvsPerName = pyBackendsByName.get(name);

				if (setExecEnvsPerHostname == null) {
					setExecEnvsPerHostname = new HashSet<>();
					pyBackendsByHostname.put(hostname, setExecEnvsPerHostname);
				}

				if (setExecEnvsPerName == null) {
					setExecEnvsPerName = new HashSet<>();
					pyBackendsByName.put(hostname, setExecEnvsPerName);
				}

				setExecEnvsPerHostname.add(bkID);
				setExecEnvsPerName.add(bkID);
			}

			LOGGER.info("Python Backends: {}", pyBackendsByID);

		} catch (final Exception ex) {
			LOGGER.debug("Backend initialization error", ex);
			throw new DataClayException(ex);
		}
	}

	/**
	 * If the object is accessible, initializes an instance of a stub with the given
	 * objectID.
	 *
	 * @param objectIDstr
	 *            ID of the object
	 * @return An instance of the stub representing the given objectID
	 * @throws DataClayException
	 *             if an exception occurs
	 */
	public static Object getByID(final String objectIDstr) throws DataClayException {
		try {

			final Triple<ObjectID, BackendID, MetaClassID> ids = string2IDandHintID(objectIDstr);
			final ObjectID objectID = ids.getFirst();
			final BackendID hint = ids.getSecond();
			final MetaClassID classID = ids.getThird();

			if (DEBUG_ENABLED) {
				DataClayRuntime.LOGGER
						.debug("[==GetByID from StorageItf==] Creating instance from oid string " + objectIDstr);
			}

			// Class id is null, so asking metadata
			final DataClayObject result = commonLib.getPersistedObjectByOID(objectID, classID, hint);
			return result;

		} catch (final Exception ex) {
			LOGGER.debug("getbyID error", ex);
			throw new DataClayException(ex);
		}
	}

	/**
	 * Gets any location of an object.
	 *
	 * @param objectIDstr
	 *            object to retrieve its location
	 * @return a location of the object.
	 * @throws DataClayException
	 *             if an exception occurs
	 */
	public static String getLocation(final String objectIDstr) throws DataClayException {
		try {
			if (DEBUG_ENABLED) {
				DataClayRuntime.LOGGER.debug("[dataClay] Get location for " + objectIDstr);
			}
			if (objectIDstr == null || objectIDstr.trim().isEmpty()) {
				throw new DataClayException("ERROR in getLocation: Null or empty object string : " + objectIDstr);
			}
			final Triple<ObjectID, BackendID, MetaClassID> objectData = string2IDandHintID(objectIDstr);
			return backendsByID.get(objectData.getSecond()).getHostname();
		} catch (final DataClayException e) {
			throw e;
		} catch (final Exception ex) {
			LOGGER.debug("getLocation error", ex);
			throw new DataClayException(ex);
		}
	}

	/**
	 * Gets all the locations of an object.
	 *
	 * @param objectIDstr
	 *            object to retrieve its locations.
	 * @return locations of an object.
	 * @throws DataClayException
	 *             if an exception occurs
	 */
	public static List<String> getLocations(final String objectIDstr) throws DataClayException {
		try {
			if (DEBUG_ENABLED) {
				DataClayRuntime.LOGGER.debug("[dataClay] GetLocations for " + objectIDstr);
			}
			if (objectIDstr == null || objectIDstr.trim().isEmpty()) {
				throw new DataClayException("ERROR in getLocations: Null or empty object string : " + objectIDstr);
			}
			final Triple<ObjectID, BackendID, MetaClassID> objectData = string2IDandHintID(objectIDstr);
			final ObjectID objectID = objectData.getFirst();
			final BackendID hint = objectData.getSecond();

			// Get locations of the object
			final Set<BackendID> currentLocations = commonLib.getAllLocations(objectID);
			if (currentLocations == null || currentLocations.isEmpty()) {
				if (hint == null) {
					throw new DataClayException("ERROR in getLocations: object not accessible or does not exist");
				}
				if (DEBUG_ENABLED) {
					LOGGER.debug("No locations for object {}, assuming volatile and returning hint {}", objectID, hint);
				}
				return Arrays.asList(backendsByID.get(hint).getHostname());
			}
			final LinkedList<String> result = new LinkedList<>();
			for (final BackendID location : currentLocations) {
				result.add(backendsByID.get(location).getHostname());
			}
			return result;
		} catch (final DataClayException e) {
			throw e;
		} catch (final Exception ex) {
			LOGGER.debug("Failed while performing getLocations on {}", objectIDstr);
			LOGGER.debug("Current error:", ex);
			throw new DataClayException(ex);
		}
	}

	/**
	 * Move a replica from source host to dest host.
	 *
	 * @param objectIDstr
	 *            object which replica must be moved.
	 * @param srcHost
	 *            source location of the object replica.
	 * @param destHost
	 *            target location of the object replica.
	 * @throws DataClayException
	 *             if an exception occurs. E.g. if source or dest hosts have no
	 *             backend registered.
	 */
	public static void moveReplica(final String objectIDstr, final String srcHost, final String destHost)
			throws DataClayException {
		try {
			if (srcHost == null || srcHost.trim().isEmpty()) {
				throw new DataClayException("ERROR in moveReplica: srcHost '" + srcHost + "' is null or empty.");
			}
			if (destHost == null || destHost.trim().isEmpty()) {
				throw new DataClayException("ERROR in moveReplica: destHost '" + destHost + "' is null or empty.");
			}
			if (srcHost.equals(destHost)) {
				throw new DataClayException(
						"ERROR in moveReplica: src " + srcHost + " and dest " + destHost + " are the same.");
			}
			final Triple<ObjectID, BackendID, MetaClassID> ids = string2IDandHintID(objectIDstr);
			final ObjectID objectID = ids.getFirst();
			final BackendID hint = ids.getSecond();
			final MetaClassID classID = ids.getThird();
			final Set<BackendID> currentBackends = commonLib.getAllLocations(objectID);
			final BackendID oneBK = currentBackends.iterator().next();
			final Langs lang = backendsByID.get(oneBK).getLang();

			// Select backend for the provided src host
			BackendID srcLocID = null;
			Set<BackendID> srcBackends = null;
			switch (lang) {
				case LANG_JAVA:
					if (LOCALTOKEN.equals(srcHost)) {
						srcLocID = jLOCAL;
					} else {
						srcBackends = jBackendsByHostname.get(srcHost);
					}
					break;
				case LANG_PYTHON:
					if (LOCALTOKEN.equals(srcHost)) {
						srcLocID = pLOCAL;
					} else {
						srcBackends = pyBackendsByHostname.get(srcHost);
					}
					break;
				default:
					throw new DataClayException("ERROR in moveReplica: unsupported language");
			}
			if (srcLocID == null) {
				if (srcBackends == null || srcBackends.isEmpty()) {
					throw new DataClayException("ERROR in moveReplica: src host " + srcHost + " has no backends");
				}
				for (final BackendID bkID : srcBackends) {
					if (currentBackends.contains(bkID)) { // find backend containing a replica
						srcLocID = bkID;
						break;
					}
				}
			}
			if (srcLocID == null) {
				throw new DataClayException("ERROR in moveReplica: no replica found in src host " + srcHost);
			}

			// Select dest backend for the provided dest host
			BackendID destLocID = null;
			Set<BackendID> destBackends = null;
			switch (lang) {
				case LANG_JAVA:
					if (LOCALTOKEN.equals(destHost)) {
						destLocID = jLOCAL;
					} else {
						destBackends = jBackendsByHostname.get(destHost);
					}
					break;
				case LANG_PYTHON:
					if (LOCALTOKEN.equals(destHost)) {
						destLocID = pLOCAL;
					} else {
						destBackends = pyBackendsByHostname.get(destHost);
					}
					break;
				default:
					throw new DataClayException("ERROR in moveReplica: unsupported language");
			}
			if (destLocID == null) {
				if (destBackends == null || destBackends.isEmpty()) {
					throw new DataClayException("ERROR in moveReplica: dest host " + destHost + " has no backends");
				}
				for (final BackendID bkID : destBackends) {
					if (!currentBackends.contains(bkID)) { // find backend NOT containing a replica
						destLocID = bkID;
						break;
					}
				}
			}
			if (destLocID == null) {
				throw new DataClayException(
						"ERROR in moveReplica: no suitable backend found (or replica already present in dest)");
			}

			// Move replica
			commonLib.moveObject(objectID, classID, hint, srcLocID, destLocID, true);

			LOGGER.info("Object {} moved from {} to {}", objectID, srcLocID, destLocID);

		} catch (

				final Exception e) {
			throw new DataClayException(e);
		}
	}

	/**
	 * Create a new replica of the given object.
	 *
	 * @param objectIDstr
	 *            objectID to be replicated.
	 * @param destHost
	 *            target location of the object replica.
	 * @throws DataClayException
	 *             if an exception occurs
	 */
	public static void newReplica(final String objectIDstr, final String destHost) throws DataClayException {
		try {
			final Triple<ObjectID, BackendID, MetaClassID> ids = string2IDandHintID(objectIDstr);
			final ObjectID objectID = ids.getFirst();
			final BackendID hint = ids.getSecond();
			final MetaClassID classID = ids.getThird();
			commonLib.newReplica(objectID,null, destHost, false, true);

		} catch (final Exception e) {
			throw new DataClayException(e);
		}

	}

	/**
	 * Executes a method on a specific target assynchronously.
	 *
	 * @param objectIDstr
	 *            ID of the target object.
	 * @param method
	 *            method to be executed
	 * @param params
	 *            parameters for the operation.
	 * @param callback
	 *            callback handler to communicate the result when the execution
	 *            finishes.
	 * @return an id of the executed request that will receive the callback handler
	 *         with the corresponding response
	 * @throws DataClayException
	 *             if an exception occurs.
	 */
	public static String executeTask(final String objectIDstr, final java.lang.reflect.Method method,
									 final Object[] params, final CallbackHandler callback) throws DataClayException {
		final Method m = Method.getMethod(method);
		return executeTask(objectIDstr, m.getName() + m.getDescriptor(), params, callback);
	}

	/**
	 * Executes a method on a specific target assynchronously.
	 *
	 * @param objectIDstr
	 *            ID of the target object.
	 * @param operationSignature
	 *            signature of the method to be executed.
	 * @param params
	 *            parameters for the operation.
	 * @param callback
	 *            callback handler to communicate the result when the execution
	 *            finishes.
	 * @return an id of the executed request that will receive the callback handler
	 *         with the corresponding response
	 * @throws DataClayException
	 *             if an exception occurs.
	 */
	public static String executeTask(final String objectIDstr, final String operationSignature, final Object[] params,
									 final CallbackHandler callback) throws DataClayException {
		if (DEBUG_ENABLED) {
			DataClayRuntime.LOGGER.debug("[dataClay] Executing task for " + objectIDstr);
		}
		final Triple<ObjectID, BackendID, MetaClassID> ids = string2IDandHintID(objectIDstr);
		final ObjectID objectID = ids.getFirst();
		final BackendID backendID = ids.getSecond();
		MetaClassID classID = ids.getThird();

		// Lock, since this function is called concurrently
		if (classID == null) {
			commonLib.lock(objectID);
			final MetaDataInfo metadata = commonLib.getObjectMetadata(objectID);
			if (metadata == null) {
				// no metadata available, throw exception
				// NOTE: if it is a volatile and hint failed, it means that object is actually
				// not registered
				throw new ObjectNotRegisteredException(objectID);
			}
			commonLib.unlock(objectID);
			classID = metadata.getMetaclassID();
		}
		final String className = commonLib.getClassName(classID);

		// Generate unique id for the request
		final UUID id = UUID.randomUUID();

		new Thread("" + id) {
			@Override
			public void run() {
				try {
					if (Configuration.mockTesting) {
						DataClayMockObject.setCurrentThreadLib(ClientManagementLib.getDataClayClientLib());
					}
					final Object result = commonLib.executeRemoteTask(objectID, className, operationSignature, params,
							backendID);
					if (className == null || className.isEmpty()) {
						throw new DataClayException(
								"Cannot get object info of " + objectID + " with session " + commonLib.getSessionID());
					}
					callback.eventListener(new CallbackEvent(this.getName(), CallbackEvent.EventType.SUCCESS, result,
							className, operationSignature));
				} catch (final Exception ex) {
					LOGGER.debug("executeTask->run() error", ex);
					callback.eventListener(
							new CallbackEvent(this.getName(), CallbackEvent.EventType.FAIL, ex.getMessage()));
				}
			}
		}.start();

		return "" + id;
	}

	/**
	 * Processes and retrieves the callback event produced by a task execution.
	 *
	 * @param callbackEvent
	 *            the event to be processed
	 * @return The task result.
	 * @throws DataClayException
	 *             if any exception occurs
	 */
	public static Object getResult(final CallbackEvent callbackEvent) throws DataClayException {
		try {
			return callbackEvent.getContent();
		} catch (final Exception ex) {
			throw new DataClayException(ex);
		}
	}

	/**
	 * Translates from string representation of an objectID to an ObjectID.
	 *
	 * @param objectIDstr
	 *            string representation of the object.
	 * @return the ObjectID built from its string representation.
	 * @throws DataClayException
	 */
	public static Triple<ObjectID, BackendID, MetaClassID> string2IDandHintID(final String objectIDstr)
			throws DataClayException {
		if (objectIDstr == null || objectIDstr.trim().isEmpty()) {
			throw new DataClayException("ERROR: malformed objectIDstr '" + objectIDstr + "'");
		}
		final String[] idsStr = objectIDstr.split(":");
		final ObjectID objectID = new ObjectID(UUID.fromString(idsStr[0]));
		BackendID hint = null;
		if (idsStr.length > 1 && !idsStr[1].isEmpty()) {
			hint = new ExecutionEnvironmentID(UUID.fromString(idsStr[1]));
		}
		MetaClassID classID = null;
		if (idsStr.length > 2 && !idsStr[2].isEmpty()) {
			classID = new MetaClassID(UUID.fromString(idsStr[2]));
		}
		final Triple<ObjectID, BackendID, MetaClassID> ids = new Triple<>(objectID, hint, classID);
		return ids;
	}

	/**
	 * Translates from ObjectID to string representation.
	 *
	 * @param objectID
	 *            ID of the object.
	 * @param hint
	 *            hint where the object should be
	 * @param classID
	 *            class id of the object
	 * @return string representation of the giving object.
	 */
	public static String ids2String(final ObjectID objectID, final BackendID hint, final MetaClassID classID) {
		String strHint = "";
		String strClass = "";
		if (hint != null) {
			strHint = hint.toString();
		}
		if (classID != null) {
			strClass = classID.toString();
		}
		return objectID.getId().toString() + ":" + strHint + ":" + strClass;
	}

	/**
	 * Retrieves the info of a backend
	 *
	 * @param backendID
	 *            id of the backend
	 * @return info of the backend
	 */
	public static Backend getBackendInfo(final BackendID backendID) {
		return backendsByID.get(backendID);
	}

	/**
	 * Retrieves the info of all known backends indexed by ID
	 *
	 * @return map of backends indexed by ID
	 */
	public static Map<BackendID, Backend> getBackends() {
		return backendsByID;
	}

	/**
	 * Retrieves the info of all Java backends
	 *
	 * @return map of backends indexed by ID
	 */
	public static Map<BackendID, Backend> getJBackends() {
		return jBackendsByID;
	}

	/**
	 * Retrieves the info of all Python backends
	 *
	 * @return map of backends indexed by ID
	 */
	public static Map<BackendID, Backend> getPyBackends() {
		return pyBackendsByID;
	}

	/**
	 * Getter for commonLib property.
	 *
	 * @return common lib reference
	 */
	public static ClientRuntime getCommonLib() {
		return commonLib;
	}

	/**
	 * Getter for sessionID property.
	 *
	 * @return sessionID
	 */
	public static SessionID getSessionID() {
		return commonLib.getSessionID();
	}

	public static int getMDmisses() {
		return commonLib.misses;
	}

	public static int getMDhits() {
		return commonLib.hits;
	}

	public static Map<String, Set<BackendID>> getPyBackendsByName() {
		return pyBackendsByName;
	}

	public static Map<String, Set<BackendID>> getJBackendsByName() {
		return jBackendsByName;
	}

	public static Map<String, Set<BackendID>> getPyBackendsByHostname() {
		return pyBackendsByHostname;
	}

	public static Map<String, Set<BackendID>> getJBackendsByHostname() {
		return jBackendsByHostname;
	}


	/**
	 * Retrieves current dataClay instance ID
	 *
	 * @return id of current dataClay instance
	 */
	public static DataClayInstanceID getDataClayID() {
		return ClientManagementLib.getDataClayID();
	}

	/**
	 * Retrieves the id of the external dataClay which Logic Module is located at
	 * provided host and listening on specified port.
	 *
	 * @param dcHost
	 *            host where the external dataClay is located.
	 * @param dcPort
	 *            port where the external dataClay is listening.
	 * @return id of the external dataClay
	 */
	public static DataClayInstanceID getDataClayID(final String dcHost, final int dcPort) {
		return ClientManagementLib.getExternalDataClayID(dcHost, dcPort);
	}

	/**
	 * Registers an external dataClay to enable future object federation with it.
	 * Also returns its dataClay instance id.
	 *
	 * @param dcHost
	 *            host where the external dataClay is located.
	 * @param dcPort
	 *            port where the external dataClay is listening.
	 * @return id of the external dataClay
	 */
	public static DataClayInstanceID registerDataClay(final String dcHost, final int dcPort) {
		return ClientManagementLib.registerExternalDataClay(dcHost, dcPort);
	}

	/**
	 * Activate tracing in dataClay services
	 *
	 */
	public static void activateTracingInDataClayServices() {
		ClientManagementLib.activateTracingInDataClayServices();
	}

	/**
	 * Dectivate tracing
	 */
	public static void deactivateTracingInDataClayServices() {
		ClientManagementLib.deactivateTracingInDataClayServices();
	}

	/**
	 * Activate tracing
	 */
	public static void activateTracing(
			final boolean initializeWrapper) {
		ClientManagementLib.activateTracing(initializeWrapper);
	}

	/**
	 * Dectivate tracing
	 */
	public static void deactivateTracing(final boolean finalizeWrapper) {
		ClientManagementLib.deactivateTracing(finalizeWrapper);
	}

	/**
	 * Get traces in dataClay services and store it in current workspace
	 */
	public final static void getTracesInDataClayServices() {
		ClientManagementLib.getTracesInDataClayServices();
	}

	/**
	 * Unfederate all objects belonging/federated with external dataClay with id provided
	 * @param extDataClayID External dataClay ID
	 */
	public static void unfederateAllObjects(final DataClayInstanceID extDataClayID) {
		ClientManagementLib.unfederateAllObjects(extDataClayID);
	}

	/**
	 * Unfederate all objects belonging/federated with ANY external dataClay 
	 */
	public static void unfederateAllObjects() {
		ClientManagementLib.unfederateAllObjectsWithAllDCs();
	}

	/**
	 * Migrate (unfederate and federate) all current dataClay objects from specified external dataclay di to
	 * destination dataclay. 
	 * @param originDataClayID Origin dataclay id
	 * @param destinationDataClayID Destination dataclay id
	 */
	public static void migrateFederatedObjects(final DataClayInstanceID originDataClayID,
											   final DataClayInstanceID destinationDataClayID) {
		ClientManagementLib.migrateFederatedObjects(originDataClayID, destinationDataClayID);
	}

	/**
	 * Federate all dataClay objects from specified current dataClay
	 * destination dataclay. 
	 * @param destinationDataClayID Destination dataclay id
	 */
	public static void federateAllObjects(
			final DataClayInstanceID destinationDataClayID) {
		ClientManagementLib.federateAllObjects(destinationDataClayID);
	}

	/**
	 * Import classes in namespace specified from an external dataClay
	 * @param externalNamespace External namespace to get
	 * @param extDataClayID External dataClay ID
	 */
	public static void importModelsFromExternalDataClay(final String externalNamespace,
														final DataClayInstanceID extDataClayID) {
		ClientManagementLib.importModelsFromExternalDataClay(externalNamespace, extDataClayID);
	}

}
