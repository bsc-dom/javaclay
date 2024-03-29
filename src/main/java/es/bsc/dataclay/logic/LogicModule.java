
/**
 * @file LogicModule.java
 * @date Sep 17, 2012
 */
package es.bsc.dataclay.logic;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import es.bsc.dataclay.api.BackendID;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteHandlerConfig;
import es.bsc.dataclay.exceptions.metadataservice.*;
import es.bsc.dataclay.logic.logicmetadata.LogicModuleMetadataMgr;
import es.bsc.dataclay.logic.server.LogicModuleSrv;
import es.bsc.dataclay.util.management.metadataservice.*;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;
import org.yaml.snakeyaml.Yaml;

import es.bsc.dataclay.commonruntime.DataServiceRuntime;
import es.bsc.dataclay.communication.grpc.clients.CommonGrpcClient;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.dataservice.api.DataServiceAPI;
import es.bsc.dataclay.dbhandler.DBHandler;
import es.bsc.dataclay.dbhandler.DBHandlerConf;
import es.bsc.dataclay.dbhandler.sql.SQLHandler;
import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.DataClayRuntimeException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectAlreadyExistException;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.accountmgr.AccountAlreadyExistException;
import es.bsc.dataclay.exceptions.logicmodule.accountmgr.AccountNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.datacontractmgr.AccountAlreadyHasADataContractWithProvider;
import es.bsc.dataclay.exceptions.logicmodule.datasetmgr.DataSetExistsException;
import es.bsc.dataclay.exceptions.logicmodule.namespacemgr.AccountNotResponsibleOfNamespace;
import es.bsc.dataclay.exceptions.logicmodule.namespacemgr.NamespaceDoesNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.namespacemgr.NamespaceExistsException;
import es.bsc.dataclay.exceptions.logicmodule.sessionmgr.SessionNotExistException;
import es.bsc.dataclay.extrae.DataClayExtrae;
import es.bsc.dataclay.logic.accountmgr.AccountManager;
import es.bsc.dataclay.logic.accountmgr.AccountManagerDB;
import es.bsc.dataclay.logic.api.LogicModuleAPI;
import es.bsc.dataclay.logic.classmgr.ClassManager;
import es.bsc.dataclay.logic.classmgr.ClassManagerDB;
import es.bsc.dataclay.logic.contractmgr.ContractManager;
import es.bsc.dataclay.logic.contractmgr.ContractManagerDB;
import es.bsc.dataclay.logic.datacontractmgr.DataContractManager;
import es.bsc.dataclay.logic.datacontractmgr.DataContractManagerDB;
import es.bsc.dataclay.logic.datasetmgr.DataSetManager;
import es.bsc.dataclay.logic.datasetmgr.DataSetManagerDB;
import es.bsc.dataclay.logic.interfacemgr.InterfaceManager;
import es.bsc.dataclay.logic.interfacemgr.InterfaceManagerDB;
import es.bsc.dataclay.logic.logicmetadata.LogicMetadataDB;
import es.bsc.dataclay.logic.logicmetadata.LogicMetadataIDs;
import es.bsc.dataclay.logic.namespacemgr.NamespaceManager;
import es.bsc.dataclay.logic.namespacemgr.NamespaceManagerDB;
import es.bsc.dataclay.logic.notificationmgr.NotificationManager;
import es.bsc.dataclay.logic.notificationmgr.NotificationManagerDB;
import es.bsc.dataclay.logic.sessionmgr.SessionManager;
import es.bsc.dataclay.logic.sessionmgr.SessionManagerDB;
import es.bsc.dataclay.metadataservice.MetaDataService;
import es.bsc.dataclay.metadataservice.MetaDataServiceDB;
import es.bsc.dataclay.serialization.lib.DataClayDeserializationLib;
import es.bsc.dataclay.serialization.lib.ObjectWithDataParamOrReturn;
import es.bsc.dataclay.serialization.lib.SerializedParametersOrReturn;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.FileAndAspectsUtils;
import es.bsc.dataclay.util.configs.CfgAdminEnvLoader;
import es.bsc.dataclay.util.events.listeners.ECA;
import es.bsc.dataclay.util.events.message.EventMessage;
import es.bsc.dataclay.util.events.type.EventType;
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
import es.bsc.dataclay.util.management.accountmgr.AccountRole;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;
import es.bsc.dataclay.util.management.classmgr.AccessedImplementation;
import es.bsc.dataclay.util.management.classmgr.AccessedProperty;
import es.bsc.dataclay.util.management.classmgr.Implementation;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.classmgr.PrefetchingInformation;
import es.bsc.dataclay.util.management.classmgr.Property;
import es.bsc.dataclay.util.management.classmgr.Type;
import es.bsc.dataclay.util.management.classmgr.UserType;
import es.bsc.dataclay.util.management.contractmgr.Contract;
import es.bsc.dataclay.util.management.contractmgr.InterfaceInContract;
import es.bsc.dataclay.util.management.contractmgr.OpImplementations;
import es.bsc.dataclay.util.management.datacontractmgr.DataContract;
import es.bsc.dataclay.util.management.datasetmgr.DataSet;
import es.bsc.dataclay.util.management.interfacemgr.Interface;
import es.bsc.dataclay.util.management.namespacemgr.ImportedInterface;
import es.bsc.dataclay.util.management.namespacemgr.Namespace;
import es.bsc.dataclay.util.management.sessionmgr.SessionContract;
import es.bsc.dataclay.util.management.sessionmgr.SessionDataContract;
import es.bsc.dataclay.util.management.sessionmgr.SessionImplementation;
import es.bsc.dataclay.util.management.sessionmgr.SessionInfo;
import es.bsc.dataclay.util.management.sessionmgr.SessionInterface;
import es.bsc.dataclay.util.management.sessionmgr.SessionOperation;
import es.bsc.dataclay.util.management.sessionmgr.SessionProperty;
import es.bsc.dataclay.util.management.stubs.ImplementationStubInfo;
import es.bsc.dataclay.util.management.stubs.PropertyStubInfo;
import es.bsc.dataclay.util.management.stubs.StubClassLoader;
import es.bsc.dataclay.util.management.stubs.StubInfo;
import es.bsc.dataclay.util.reflection.Reflector;
import es.bsc.dataclay.util.structs.Triple;
import es.bsc.dataclay.util.structs.Tuple;
import es.bsc.dataclay.util.tools.java.JavaSpecGenerator;
import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * This class represents the entry point to the system.
 */
public class LogicModule implements LogicModuleAPI {

    /** Logger. */
    protected static final Logger LOGGER = LogManager.getLogger("LogicModule");

    /** Indicates if debug is enabled. */
    protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

    /** LogicModule hostname. */
    protected String hostname;
    /** LogicModule port. */
    protected int port;

    /** Grpc client. */
    public final CommonGrpcClient grpcClient;

	/** DataService APIs, with associated StorageLocation. */
	private final Map<StorageLocationID, Tuple<DataServiceAPI, StorageLocation>> storageLocations = new ConcurrentHashMap<>();

	/**
	 * DataService APIs, associated to ExecutionEnvironment (language dependent).
	 */
	private final Map<Langs, Map<ExecutionEnvironmentID, Tuple<DataServiceAPI, ExecutionEnvironment>>> execEnvironments = new ConcurrentHashMap<>();

	/** Active Backends splitted in execution environments per storage Locations. */
	private final Map<StorageLocationID, Set<ExecutionEnvironmentID>> activeBackends = new ConcurrentHashMap<>();
	
	/** Name of public namespace DataClay (for DataClay classes). */
	public static final String DC_PUBLIC_NAMESPACE = "DataClayNamespace";
	/** Name of DataClay registrator (for DataClay classes). */
	public static final String DC_REGISTRATOR = "DataClayRegistrator";
	/** Suffix for default dataset. */
	public static final String DEFAULT_DS_SUFFIX = "_ds";
	/** Suffix for default java namespace. */
	public static final String DEFAULT_JAVA_NS_SUFFIX = "_java_ns";
	/** Suffix for default python namespace. */
	public static final String DEFAULT_PYTHON_NS_SUFFIX = "_python_ns";

    /** Account Manager. */
    private final AccountManager accountMgrApi;
    /** Contract Manager. */
    private final ContractManager contractMgrApi;
    /** Interface Manager. */
    private final InterfaceManager interfaceMgrApi;
    /** Class Manager. */
    private final ClassManager classMgrApi;
    /** Namespace Manager. */
    private final NamespaceManager namespaceMgrApi;
    /** Session Manager. */
    private final SessionManager sessionMgrApi;
    /** MetaDataService. */
    private final MetaDataService metaDataSrvApi;
    /** DataSet Manager. */
    private final DataSetManager dataSetMgrApi;
    /** DataContract Manager. */
    private final DataContractManager datacontractMgrApi;
    /** NotificationManager. */
    private NotificationManager notificationMgrApi;
    /** LogicModule metadata mgr. */
    private LogicModuleMetadataMgr logicMetadataMgr;
    /**
     * LM public IDs (admin, contract, datasets...) This field contains all IDs that
     * must be the same even if we restart LM or for backups.
     */
    public LogicMetadataIDs publicIDs = new LogicMetadataIDs();

    /** Credentials of DataClay registrator (for DataClay classes). */
    public PasswordCredential dcCredentials = new PasswordCredential(DC_REGISTRATOR);

    /** IPs to be send to clients when information of a registered EE/SL is required. Can be null.*/
    private String exposedIPForClient;

    /** Account to register federation stuff. */
    private static final String FEDERATOR_ACCOUNT_USERNAME = "Federator";
    /** Name of dataset for external objects. */
    private static final String EXTERNAL_OBJECTS_DATASET_NAME = "ExternalObjects";
    /** Namespace of classes for federable Java class model. */
    private static final String JAVA_NAMESPACE_FOR_FEDERABLE_CLASSES = "JavaFederableNamespace";
    /** Namespace of classes for federable Python class model. */
    private static final String PY_NAMESPACE_FOR_FEDERABLE_CLASSES = "PythonFederableNamespace";

    /** Session ID for activity coming from external dataclays. */
    private static SessionID federationSessionID;

    /** Indicates if it is being shut down. */
    private boolean shuttingDown = false;

    /** Indicates Logicmodule database is in memory. */
    private boolean inMemory = false;

    /** SQL handler for management. */
    private SQLHandler<SQLiteHandlerConfig> mgmHandler;

    /** SQL handler for metadata. */
    private SQLHandler<SQLiteHandlerConfig> metadataHandler;

    /** Name of the LM */
    protected String name;

    /**
     * LogicModule constructor
     *
     * @param lmName
     *            LM name
     * @param thehostname
     *            Logic module host name
     * @param theport
     *            Logic Module port.
     * @param theexposedIPForClient
     * 			IPs to be send to clients when information of a registered EE/SL is required. Can be null.
     * @throws InterruptedException
     *             If GRPC client cannot be initialized for some reason
     */
    public LogicModule(final String lmName, final String thehostname, final int theport,
                       final boolean inMemory,
                       final String theexposedIPForClient) throws InterruptedException {

        // ==== Initialization note === //
        // Here we set all variables read from configuration files (parameters) and
        // we check if LM has already an admin, a public dataset, a public contract...
        // by asking
        // to LogicMetadataDB.sh
        // If LogicMetadataDB has information about this logic module then
        // it means that we do not need to create admin, install dataclay collections
        // and others.
        // Otherwise, if there is no information about current LogicModule in the
        // database, we create
        // all needed ids (including DataClayInstanceID) and others.
        this.inMemory = inMemory;

        this.name = lmName;

        SQLiteHandlerConfig mgmDbConf = new SQLiteHandlerConfig(Configuration.Flags.STORAGE_METADATA_PATH.getStringValue(), name + "_metadata", Configuration.Flags.SQLITE_IN_MEMORY.getBooleanValue());
        mgmHandler = (SQLHandler) mgmDbConf.getDBHandler();
        mgmHandler.open();

        SQLiteHandlerConfig metadataDbConf = new SQLiteHandlerConfig(Configuration.Flags.STORAGE_PATH.getStringValue(), name, Configuration.Flags.SQLITE_IN_MEMORY.getBooleanValue());
        metadataHandler = (SQLHandler) metadataDbConf.getDBHandler();
        metadataHandler.open();

		// TODO generalize when dbhandler will not be sql-based
		final SQLiteDataSource mgmDataSource = mgmHandler.getDataSource();
        final SQLiteDataSource metadataDataSource = metadataHandler.getDataSource();

        hostname = thehostname;
        port = theport;
        if (theexposedIPForClient != null && !theexposedIPForClient.isEmpty()) {
            this.exposedIPForClient = theexposedIPForClient;
        }
        grpcClient = new CommonGrpcClient(lmName);
        accountMgrApi = new AccountManager(mgmDataSource);
        sessionMgrApi = new SessionManager(mgmDataSource);
        namespaceMgrApi = new NamespaceManager(mgmDataSource);
        dataSetMgrApi = new DataSetManager(mgmDataSource);
        classMgrApi = new ClassManager(mgmDataSource);
        interfaceMgrApi = new InterfaceManager(mgmDataSource);
        contractMgrApi = new ContractManager(mgmDataSource);
        datacontractMgrApi = new DataContractManager(mgmDataSource);
        logicMetadataMgr = new LogicModuleMetadataMgr(mgmDataSource);
        metaDataSrvApi = new MetaDataService(metadataDataSource);
        // Tricky initialization for the *ByLang table of tables
        for (final Langs lang : Langs.values()) {
            execEnvironments.put(lang, new HashMap<ExecutionEnvironmentID, Tuple<DataServiceAPI, ExecutionEnvironment>>());
        }

        if (Configuration.Flags.NOTIFICATION_MANAGER_ACTIVE.getBooleanValue()) {
            notificationMgrApi = new NotificationManager(this, mgmDataSource);
        }

        if (!logicMetadataMgr.existsMetaData()) {
            this.prepareLogicModuleFirstTime(lmName, thehostname, theport);
        } else {
            // update registered nodes!
            LOGGER.info("Found logic module database already present, initializing");
            this.publicIDs = this.logicMetadataMgr.getLogicMetadata();
            //this.updateAPIsFromDB();
        }
        LOGGER.info("Initialized Logic Module for dataClay with id {}", publicIDs.dcID);
        LOGGER.info("Initialized Logic Module with hostname {} and port {}", hostname, port);

    }

    /**
     * Prepare Lm for the first time (not for restart or others)
     *
     * @param lmName
     *            LM name
     * @param thehostname
     *            Logic module host name
     * @param theport
     *            Logic Module port.
     * @throws InterruptedException
     *             If GRPC client cannot be initialized for some reason
     */
    private void prepareLogicModuleFirstTime(final String lmName, final String thehostname, final int theport) {

        // TODO: review uneeded code for checking if admin, datasets and others are
        // already created since
        // this function is called when LogicModule is restarted or using an existing
        // DB.

        publicIDs.dcID = new DataClayInstanceID();
        final Account adminAccount = CfgAdminEnvLoader.parseAdminUser();
        LOGGER.info("Creating admin user with name " + adminAccount.getUsername() + " and password "
                + adminAccount.getCredential().toString());
        try {
            publicIDs.dcAdminID = accountMgrApi.getAccountID(adminAccount.getUsername());
            LOGGER.info("Account admin already exists");
        } catch (final AccountNotExistException e) {
            // Create Admin account
            publicIDs.dcAdminID = new AccountID();
            adminAccount.setDataClayID(publicIDs.dcAdminID);
            final AccountManagerDB accountDbHandler = accountMgrApi.getDbHandler();
            try {
                accountDbHandler.store(adminAccount);
            } catch (final DbObjectAlreadyExistException err) {
                // ignore
            }
        }
        // Register dataSet for external objects (federated with us)
        final PasswordCredential fedCredential = new PasswordCredential(FEDERATOR_ACCOUNT_USERNAME);
        final Account federator = new Account(FEDERATOR_ACCOUNT_USERNAME, AccountRole.NORMAL_ROLE,
                fedCredential);
        try {
            federator.setDataClayID(newAccount(publicIDs.dcAdminID, adminAccount.getCredential(), federator));
        } catch (final AccountAlreadyExistException | NamespaceExistsException ne) {
            // namespace might exist in case of mf2c starting db
            federator.setDataClayID(accountMgrApi.getAccountID(FEDERATOR_ACCOUNT_USERNAME));
            LOGGER.info("Account Federator already exists");
        }

        final DataSet dataset = new DataSet(EXTERNAL_OBJECTS_DATASET_NAME, federator.getDataClayID(), true);
        DataContract dContract = null;
        try {
            dataset.setDataClayID(newDataSet(federator.getDataClayID(), federator.getCredential(), dataset));

            final Calendar beginDate = Calendar.getInstance();
            beginDate.add(Calendar.YEAR, -1);
            final Calendar endDate = Calendar.getInstance();
            endDate.add(Calendar.YEAR, 100);
            dContract = new DataContract(dataset.getDataClayID(), federator.getDataClayID(),
                    new HashSet<>(), beginDate, endDate, false);
            dContract.setPublicAvailable(true);

            newDataContract(federator.getDataClayID(), federator.getCredential(), dContract);

        } catch (final DataSetExistsException de) {
            // Ignore it, for testing purposes
            dataset.setDataClayID(dataSetMgrApi.getDataSetID(EXTERNAL_OBJECTS_DATASET_NAME));
            LOGGER.info("Dataset for external objects already exists");
        }

        // Register namespaces for the model of federated objects
        final Namespace jNamespace = new Namespace(JAVA_NAMESPACE_FOR_FEDERABLE_CLASSES, federator.getUsername(),
                Langs.LANG_JAVA);
        final Namespace pyNamespace = new Namespace(PY_NAMESPACE_FOR_FEDERABLE_CLASSES, federator.getUsername(),
                Langs.LANG_PYTHON);
        try {
            newNamespace(federator.getDataClayID(), federator.getCredential(), jNamespace);
            newNamespace(federator.getDataClayID(), federator.getCredential(), pyNamespace);
        } catch (final NamespaceExistsException ne) {
            // Ignore it
            LOGGER.info("Namespace for federated objects already exists");
        }

        if (Configuration.Flags.REGISTER_DATACLAY_CLASSES.getBooleanValue()) {
            registerDataClayPreinstalledClasses(adminAccount);
        }

        // Store LogicModule information once prepared
        this.logicMetadataMgr.registerLogicModule(publicIDs);

        final Set<DataSetID> dataSetIDs = new HashSet<>();
        dataSetIDs.add(dataset.getDataClayID());
        final Set<ContractID> contracts = new HashSet<>();
        federationSessionID = this.newSession(federator.getDataClayID(), fedCredential,
                contracts, dataSetIDs, dataset.getDataClayID(), Langs.LANG_JAVA).getSessionID();

    }

    /**
     * Method that registers a set of preinstalled dataClay class model, including a
     * dataClay normal account responsible of it, plus a public namespace for the
     * classes.
     *
     * @param adminAccount Admin account
     */
    private void registerDataClayPreinstalledClasses(final Account adminAccount) {
        try {
            // Create the account for dataClay classes
            try {
                publicIDs.dcRegistratorID = accountMgrApi.getAccountID(DC_REGISTRATOR);
            } catch (final AccountNotExistException e) {
                final Account dcRegistrator = new Account(DC_REGISTRATOR, AccountRole.NORMAL_ROLE, dcCredentials);
                publicIDs.dcRegistratorID = newAccount(publicIDs.dcAdminID, adminAccount.getCredential(),
                        dcRegistrator);
            }

            // Public Namespace ID
            try {
                publicIDs.dcPublicNamespaceID = namespaceMgrApi.getNamespaceID(DC_PUBLIC_NAMESPACE);
            } catch (final NamespaceDoesNotExistException e) {
                // Create new namespace ID
                final Namespace publicDataClayNamespace = new Namespace(DC_PUBLIC_NAMESPACE, DC_REGISTRATOR,
                        Langs.LANG_JAVA);
                publicIDs.dcPublicNamespaceID = newNamespace(publicIDs.dcRegistratorID, dcCredentials,
                        publicDataClayNamespace);
            }

            // Generate MetaClassSpec for DataClay Classes
            // COMPILE
            LOGGER.info("[LOGICMODULE] Installing classes located at : "
                    + Configuration.Flags.DATACLAY_INSTALLED_CLASSES_SRC_PATH.getStringValue());
            final File f = new File(Configuration.Flags.DATACLAY_INSTALLED_CLASSES_SRC_PATH.getStringValue());
            if (f.exists()) {
                // WARNING: do not remove this until a new script design is done. In case we
                // change Collections, this
                // is going to compile them again and make sure we use last collections. TODO:
                // better design.
                FileAndAspectsUtils.compileClasses(
                        Configuration.Flags.DATACLAY_INSTALLED_CLASSES_SRC_PATH.getStringValue(),
                        Configuration.Flags.DATACLAY_INSTALLED_CLASSES_BIN_PATH.getStringValue(),
                        new String[]{Configuration.Flags.INCLUDE_THIS_PROJECT.getStringValue()},
                        "test");
                Map<String, MetaClass> newClasses = new HashMap<>();
                final Map<MetaClassID, MetaClass> installedClasses = classMgrApi
                        .getInfoOfClassesInNamespace(publicIDs.dcPublicNamespaceID);
                if (installedClasses.isEmpty()) {
                    // Init class loader
                    final String regClassPath = Configuration.Flags.DATACLAY_INSTALLED_CLASSES_BIN_PATH
                            .getStringValue();
                    final Set<String> classNames = new HashSet<>();
                    final Set<String> exclusions = new HashSet<>();

                    final Path inputPath = Paths.get(regClassPath).normalize();
                    final Path fullSrcPath = inputPath.toAbsolutePath();
                    final String fullPath = fullSrcPath.toString().replace(File.separatorChar + "." + File.separatorChar,
                            File.separator);
                    exclusions.add("test");
                    StubClassLoader.getClasses(fullPath, new File(fullPath), classNames, ".class", exclusions);
                    LOGGER.info("[LOGICMODULE] Installing classes : " + classNames);
                    final JavaSpecGenerator jvspec = new JavaSpecGenerator(
                            Configuration.Flags.DATACLAY_INSTALLED_CLASSES_BIN_PATH.getStringValue());
                    for (final String className : classNames) {
                        if (newClasses.containsKey(className)) {
                            continue; // Avoid registering same class twice.
                        }
                        final Map<String, MetaClass> classes = jvspec.generateMetaClassSpecForRegisterClass(DC_PUBLIC_NAMESPACE,
                                className);
                        if (classes.isEmpty()) {
                            LOGGER.info("Could not create Class spec of {}.", className);
                        }
                        newClasses.putAll(classes);
                    }
                    // Install them
                    try {
                        newClasses = this.newClass(publicIDs.dcRegistratorID, dcCredentials, Langs.LANG_JAVA,
                                newClasses);
                    } catch (final Exception ex) {
                        if (DEBUG_ENABLED) {
                            LOGGER.debug("registerDataClayPreinstalledClasses error in newClass invocation", ex);
                        }
                    }
                }

                // Public Contract ID
                final Map<ContractID, Contract> contracts = this.getContractIDsOfProvider(publicIDs.dcRegistratorID,
                        dcCredentials, publicIDs.dcPublicNamespaceID);
                if (contracts.isEmpty()) {
                    // CONTRACT

                    // Create interfaces of the contract
                    final List<InterfaceInContract> theInterfacesInContract = new ArrayList<>();
                    for (final MetaClass metaClassInfo : newClasses.values()) {

                        // OPERATIONS
                        // We assume that local and remote Impl are the same (first one) (23 Oct 2013
                        // jmarti)
                        final Set<OpImplementations> opImpls = new HashSet<>();
                        final Set<String> accOpsSignatures = new HashSet<>();
                        for (final Operation opInfo : metaClassInfo.getOperations()) {
                            opImpls.add(new OpImplementations(opInfo.getNameAndDescriptor(), 0, 0));
                            accOpsSignatures.add(opInfo.getNameAndDescriptor());
                        }

                        // PROPERTIES
                        final Set<String> props = new HashSet<>();
                        for (final Property prop : metaClassInfo.getProperties()) {
                            props.add(prop.getName());
                        }

                        // Register an interface for the contract
                        final Interface iface = new Interface(DC_REGISTRATOR, DC_PUBLIC_NAMESPACE, DC_PUBLIC_NAMESPACE,
                                metaClassInfo.getName(), props, accOpsSignatures);

                        this.newInterface(publicIDs.dcRegistratorID, dcCredentials, iface);
                        final InterfaceInContract ifaceInContract = new InterfaceInContract(iface, opImpls);

                        theInterfacesInContract.add(ifaceInContract);
                    }

                    // Prepare the dates of begin and end of a contract
                    final Calendar beginDateOfContract = Calendar.getInstance();
                    beginDateOfContract.add(Calendar.YEAR, -1);
                    final Calendar endDateOfContract = Calendar.getInstance();
                    // TODO: This contract should last forever (jmarti 1 Jul 2016)
                    endDateOfContract.add(Calendar.YEAR, 100);

                    final Contract contract = new Contract(DC_PUBLIC_NAMESPACE, DC_REGISTRATOR, theInterfacesInContract,
                            beginDateOfContract, endDateOfContract);
                    publicIDs.dcPublicContractID = this.newContract(publicIDs.dcRegistratorID, dcCredentials, contract);
                } else {
                    publicIDs.dcPublicContractID = contracts.entrySet().iterator().next().getKey();
                }
            }
        } catch (final Exception ex) {
            LOGGER.debug("registerDataClayPreinstalledClasses error", ex);
        }
    }

    /**
     * Get remote DataService API
     *
     * @param backend
     *            Backend specification
     * @return API of remote DS
     * @throws Exception
     *             If DS not started
     */
    public DataServiceAPI getExecutionEnvironmentAPI(final ExecutionEnvironment backend) {
        final ExecutionEnvironmentID execID = backend.getDataClayID();
        final Langs lang = backend.getLang();
        final Tuple<DataServiceAPI, ExecutionEnvironment> dsAPI = execEnvironments.get(lang).get(execID);
        if (dsAPI == null) {
            LOGGER.warn("No DataServiceAPI. Current execution environments: {}", execEnvironments);
            throw new DataClayRuntimeException(ERRORCODE.DATASERVICE_BACKEND_NOT_RESPONDS,
                    "DataService with ID " + execID + " is not initialized for language " + lang.toString(), false);
        }
        return dsAPI.getFirst();
    }

    /**
     * Get DataService APIs for backend with support for a given language.
     *
     * @param lang
     *            The language required.
     * @return DataServiceAPIs, the ones compatible with the language
     */
    protected Map<ExecutionEnvironmentID, Tuple<DataServiceAPI, ExecutionEnvironment>> getExecutionEnvironments(final Langs lang) {
        if (DEBUG_ENABLED) {
            LOGGER.debug("[==GetExecutionEnvironments==] Execution environments in language " + lang + " : " + execEnvironments.get(lang).keySet());
        }
        return execEnvironments.get(lang);
    }

    /**
     * Get DataService APIs for backend with support for a given language.
     *
     * @return DataServiceAPIs
     */
    protected Map<StorageLocationID, Tuple<DataServiceAPI, StorageLocation>> getStorageLocations() {
        if (DEBUG_ENABLED) {
            LOGGER.debug("[==GetStorageLocations==] Storage locations : " + storageLocations.keySet());
        }
        return this.storageLocations;
    }

    /**
     * Update APIs for all registered environments/storage locations
     */
    protected void updateAPIsFromDB() {
        for (final ExecutionEnvironment execEnv : this.getAllExecutionEnvironmentsInfo(null, false, false).values()) {
            try {
                initRemoteTCPExecutionEnvironment(execEnv.getDataClayID(),
                        execEnv.getName(), execEnv.getHostname(), execEnv.getPort(), execEnv.getLang());
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (final StorageLocation stLoc : logicMetadataMgr.getAllStorageLocationsInfo().values()) {
            try {
                initRemoteTCPStorageLocation(stLoc.getDataClayID(),
                        stLoc.getName(), stLoc.getHostname(), stLoc.getStorageTCPPort());
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Resume traces in services
     *
     * @param dsName
     *            Name of the data service to be cleaned
     * @throws RemoteException
     *             if exception occurs
     */
    public void cleanCachesOfDataService(final String dsName) {
        storageLocations.get(dsName).getFirst().cleanCaches();
    }

    // ========== Initialization by DataServices ==========//

    /**
     * This method adds an StorageLocation to the list of DS/extras.
     * @param id ID of backend to use in case of registration
     * @param dsName
     *            Name of the DataService
     * @param dsHostname
     *            Hostname of the DataService
     * @param dsTCPPort
     *            The TCP port
     * @return The StorageLocation for the DataService.
     * @throws InterruptedException
     *             if some remote initialization was interrupted
     */
    private StorageLocation initRemoteTCPStorageLocation(final StorageLocationID id,
                                                         final String dsName, final String dsHostname,
                                                         final int dsTCPPort) throws InterruptedException {
        // Test connection
        final DataServiceAPI dsApi = grpcClient.getDataServiceAPI(dsHostname, dsTCPPort);
        final StorageLocation dsSL = new StorageLocation(dsHostname, dsName, dsTCPPort);
        dsSL.setDataClayID(id);
        storageLocations.put(id, new Tuple<>(dsApi, dsSL));
        return dsSL;
    }

    /**
     * This method adds an ExecutionEnvironment to the list of DS/extras.
     * @param execID Execution Environment ID
     * @param dsName
     *            Name of the DataService
     * @param dsHostname
     *            Hostname of the DataService
     * @param dsTCPPort
     *            The TCP port
     * @param dsLang
     *            The Language for the ExecutionEnvironment in this DataService
     * @return The StorageLocation for the DataService. (Itself for Java, the Java
     *         one for Python).
     * @throws InterruptedException
     *             if some remote initialization was interrupted
     */
    private ExecutionEnvironment initRemoteTCPExecutionEnvironment(final ExecutionEnvironmentID execID,
                                                                   final String dsName, final String dsHostname,
                                                                   final int dsTCPPort, final Langs dsLang) throws InterruptedException {

        final DataServiceAPI dsApi = grpcClient.getDataServiceAPI(dsHostname, dsTCPPort);
        final ExecutionEnvironment dsEE = new ExecutionEnvironment(dsHostname, dsName, dsTCPPort, dsLang,
                this.getDataClayID());
        dsEE.setDataClayID(execID);
        execEnvironments.get(dsLang).put(execID, new Tuple<>(dsApi, dsEE));
        return dsEE;
    }

    @Override
    public void checkAlive() {
        // Check database
        if (!metadataHandler.databaseExists()) {
            LogicModuleSrv.doExit(1);
        }
        return;
    }

    @Override
    public void publishAddress(final String thehostname, final int theport) {
        this.hostname = thehostname;
        this.port = theport;
        LOGGER.info("Published address {}:{}", thehostname, theport);
    }

	@Override
	public void autoregisterSL(final StorageLocationID id, final String dsName, 
			final String dsHostname, final Integer dsPort) {
		LOGGER.debug("==> Starting autoregisterSL with name " + dsName + ", hostname " + dsHostname + " and port " + dsPort);

        // ===================== TRY CONNECTION ======================= //
        final StorageLocation storageLoc;
        try {
            storageLoc = initRemoteTCPStorageLocation(id, dsName, dsHostname, dsPort);
        } catch (final Exception ex) {
            LOGGER.debug("autoregisterDataService error while creating DataService", ex);
            throw new DataClayRuntimeException(ERRORCODE.DATASERVICE_INIT_ERROR,
                    "Could not connect to DataService in the LogicModule", true);
        }
        if (storageLoc == null) {
            throw new DataClayRuntimeException(ERRORCODE.DATASERVICE_INIT_ERROR,
                    "The StorageLocation for this DataService is not satisfactory registered", true);
        }
        // ========== REGISTER ========== //
        try {
            logicMetadataMgr.registerStorageLocation(storageLoc);
            LOGGER.info("[LOGICMODULE] Registered StorageLocation named " + storageLoc + " as " + id.getId());
        } catch (final StorageLocationAlreadyExistsException e) {
            LOGGER.info("[LOGICMODULE] Found already registered StorageLocation " + storageLoc + " as "
                    + id.getId());
            logicMetadataMgr.updateStorageLocation(id, dsHostname, dsPort);
        }

		// Activate storage location (execution environments cannot be activated before SL is ready)
		this.activeBackends.put(id, new HashSet<ExecutionEnvironmentID>());
		LOGGER.debug("==> AutoregisterSL finished for SL " + storageLoc);

	}
	
	@Override
	public StorageLocationID getStorageLocationID(final String slName) {
		final StorageLocationID slID = logicMetadataMgr.getStorageLocationID(slName);
		if (!this.activeBackends.containsKey(slID)) {
			// Still not active
            LOGGER.debug("==> Not active storage location with name " + slName + " and id " + slID);
            throw new StorageLocationNotExistException(slName);
		}
        LOGGER.debug("==> Got StorageLocationID with name " + slName + " = " + slID);
		return slID;
	}

	@Override
	public StorageLocationID autoregisterEE(final ExecutionEnvironmentID id, final String eeName, 
			final String eeHostname, final Integer eePort, final Langs language) {
		LOGGER.debug("==> Starting autoregisterEE with dsName " + eeName + ", hostname " + eeHostname + " and port " + eePort);

		final StorageLocationID slID = getStorageLocationID(eeName);

		// ===================== TRY CONNECTION ======================= //
		// Obtain or Register the Execution Environment
		final ExecutionEnvironment executionEnv;
		try {
			executionEnv = initRemoteTCPExecutionEnvironment(id, eeName, eeHostname, eePort, language);
		} catch (final Exception ex) {
			LOGGER.debug("autoregisterDataService interrupted while initializing remote ExecutionEnvironment", ex);
			throw new DataClayRuntimeException(ERRORCODE.DATASERVICE_INIT_ERROR,
					"Could not create the DataService ExecutionEnvironment in the LogicModule", true);
		}
		// ========== REGISTER ========== //
		// check it was already registered or not
		boolean newRegistration = false;
		try {
            logicMetadataMgr.getExecutionEnvironmentInfo(executionEnv.getDataClayID());
            logicMetadataMgr.updateExecutionEnvironment(id, eeHostname, eePort);
			LOGGER.info("[LOGICMODULE] Found already registered EE. Updated to " + executionEnv + " as "
					+ id.getId());
		} catch (ExecutionEnvironmentNotExistException e) {
			newRegistration = true;
		}

		
		// ========== NEW REGISTRATION ========== //
		if (newRegistration) {
			try {
				// get all namespaces
				Set<String> allNamespaces = this.namespaceMgrApi.getNamespacesNames();
				// get all classes
				for (String namespace : allNamespaces) {

					NamespaceID namespaceID = this.namespaceMgrApi.getNamespaceID(namespace);
					final Set<NamespaceID> namespacesIDs = new HashSet<>();
					namespacesIDs.add(namespaceID);
					final Map<NamespaceID, Namespace> namespaceInfos = namespaceMgrApi.getNamespacesInfo(namespacesIDs);
					Namespace currentNamespace = namespaceInfos.values().iterator().next();
					// Check namespace belongs to current EE language
					if (currentNamespace.getLanguage().equals(language)) {
						final Map<MetaClassID, MetaClass> installedClasses = this.classMgrApi.getInfoOfClassesInNamespace(namespaceID);
						if (!installedClasses.isEmpty()) {
							LOGGER.info("[LOGICMODULE] Going to deploy registered classes in namespace " + namespace);
							final Set<MetaClass> allMetaClasses = new HashSet<>(installedClasses.values());
							final Set<String> classesToDeploy = new HashSet<>();
							for (final MetaClass curClass : allMetaClasses) {
								classesToDeploy.add(curClass.getName());
							}
							final Langs eeLang = executionEnv.getLang();
							if (eeLang.equals(Langs.LANG_JAVA)) {
								outsourceClassesDeploymentJava(allMetaClasses, classesToDeploy, namespaceInfos, null, executionEnv);
							} else if (eeLang.equals(Langs.LANG_PYTHON)) {
								outsourceClassesDeploymentPython(allMetaClasses, classesToDeploy, namespaceInfos, null,
										executionEnv);
							}
						}
					}
				}

                logicMetadataMgr.registerExecutionEnvironment(executionEnv);
				LOGGER.info("[LOGICMODULE] Registered ExecutionEnvironment " + executionEnv + " for language `"
						+ language + "` as " + id.getId() + " associated to storage location " + slID);

			} catch (final Exception ex) {
				LOGGER.debug("autoregisterDataService error during dataClay classes registration", ex);
			}
		}



		// Activate storage location (execution environments cannot be activated before SL is ready)
		this.activeBackends.get(slID).add(id);

		LOGGER.debug("==> Autoregister finished for EE " + executionEnv);
		return slID;
	}

    @Override
    public void unregisterStorageLocation(final StorageLocationID stLocID) {
        logicMetadataMgr.unregisterStorageLocation(stLocID);
        //TODO: remove from any cache in LM
    }

    @Override
    public void unregisterExecutionEnvironment(final ExecutionEnvironmentID execEnvID) {
        logicMetadataMgr.unregisterExecutionEnvironment(execEnvID);
        //TODO: remove from any cache in LM
    }

    @Override
    public void notifyExecutionEnvironmentShutdown(final ExecutionEnvironmentID execEnvID) {
        // TODO: check if metadata service should be the one with information about active EEs and SLs
        LOGGER.info("Notified shutdown of execution environment " + execEnvID);
        ExecutionEnvironment eeInfo = logicMetadataMgr.getExecutionEnvironmentInfo(execEnvID);
        StorageLocationID slID = this.getStorageLocationID(eeInfo.getName());
        this.activeBackends.get(slID).remove(execEnvID);
    }

    @Override
    public void notifyStorageLocationShutdown(final StorageLocationID stLocID) {
        LOGGER.info("Notified shutdown of storage location " + stLocID);
        this.activeBackends.remove(stLocID);
    }

    @Override
    public boolean existsActiveEnvironmentsForSL(final StorageLocationID stLocID) {
        if (this.activeBackends.isEmpty()) {
            return false;
        }
        int numEEs = this.activeBackends.get(stLocID).size();
        LOGGER.info("Found " + numEEs + " active execution environments for SL " + stLocID);
        return numEEs != 0;
    }

    /**
     * Wait for all registered Storage locations and Execution environments to shut down.
     */
    public void waitForAllNodesShutdown() {
        while (true) {
            int numBackends = this.activeBackends.size();
            if (numBackends == 0) {
                break;
            }
            try {
                LOGGER.info("Waiting for backends to shutdown. Active backends: " + numBackends);
                Thread.sleep(Configuration.Flags.SLEEP_WAIT_SHUTDOWN.getLongValue());
            } catch (final InterruptedException ie) {
                LOGGER.warn("Checking interrupted. Shutting down LM without waiting for backends.");
                break;
            }
        }
    }

    // ========== Generic batch operations ==========//

    @Override
    @SuppressWarnings("unchecked")
    public byte[] performSetOfNewAccounts(final AccountID adminID, final PasswordCredential adminCredential,
                                          final byte[] yamlFile) {
        final Yaml yaml = CommonYAML.getYamlObject();

        /*
         * The structure that we will return on the YAML response
         */
        final Map<String, UUID> returnIDs = new HashMap<>();
        // Maps with account-uuid pairs

        // The YAML should be a list of accounts
        final List<Object> newAccounts;
        try {
            newAccounts = (List<Object>) yaml.load(new String(yamlFile));
        } catch (final Exception e) {
            LOGGER.warn("Received exception during deserialization", e);
            LOGGER.debug("YAML document:\n{}", new String(yamlFile));
            throw new DataClayRuntimeException(ERRORCODE.UNEXPECTED_EXCEPTION, "Error when loading YAML file", true);
        }

        for (final Object objAccount : newAccounts) {
            if (!(objAccount instanceof Account)) {
                throw new DataClayRuntimeException(ERRORCODE.UNEXPECTED_EXCEPTION, "This call only accepts Accounts",
                        true);
            }
            final Account acc = (Account) objAccount;

            final AccountID accountID = newAccount(adminID, adminCredential, (Account) objAccount);
            returnIDs.put(acc.getUsername(), accountID.getId());
        }
        return yaml.dump(returnIDs).getBytes();
    }

    @Override
    @SuppressWarnings("unchecked")
    public byte[] performSetOfOperations(final AccountID performerID, final PasswordCredential performerCredential,
                                         final byte[] yamlFile) {
        final Yaml yaml = CommonYAML.getYamlObject();

        /*
         * The structure that we will return on the YAML response
         */
        final Map<String, Object> returnIDs = new HashMap<>();

        // Initialize the potentially empty list //
        // Namespaces
        final Map<String, UUID> namespaceIDs = new HashMap<>();
        returnIDs.put("namespaces", namespaceIDs);
        // DataSets
        final Map<String, UUID> datasetIDs = new HashMap<>();
        returnIDs.put("datasets", datasetIDs);
        // Contracts
        final Map<String, UUID> contractIDs = new HashMap<>();
        returnIDs.put("contracts", contractIDs);
        // DataContracts
        final Map<String, UUID> datacontractIDs = new HashMap<>();
        returnIDs.put("datacontracts", datacontractIDs);
        // Interfaces
        final Map<String, UUID> interfaceIDs = new HashMap<>();
        returnIDs.put("interfaces", interfaceIDs);
        // MetaClasses
        final Map<String, UUID> metaclassIDs = new HashMap<>();
        returnIDs.put("metaclasses", metaclassIDs);

        // The YAML should be a dictionary, indexed by name
        final Map<String, Object> operations;
        try {
            operations = (Map<String, Object>) yaml.load(new String(yamlFile));
        } catch (final Exception e) {
            LOGGER.warn("Received exception during deserialization", e);
            LOGGER.debug("YAML document:\n{}", new String(yamlFile));
            throw new DataClayRuntimeException(ERRORCODE.UNEXPECTED_EXCEPTION, "Error when loading YAML file", true);
        }

        // Note: This works well because snakeyaml implements the Map as a LinkedHashMap
        // and there is no anarchy on ordering. The registering must obey user-provided
        // order.
        for (final Entry<String, Object> elem : operations.entrySet()) {
            if (elem.getValue() instanceof Namespace) {
                final Namespace dom = (Namespace) elem.getValue();
                dom.setName(elem.getKey());

                final NamespaceID namespaceID = newNamespace(performerID, performerCredential, dom);
                namespaceIDs.put(elem.getKey(), namespaceID.getId());

            } else if (elem.getValue() instanceof DataSet) {
                LOGGER.info("[LM] Dataset " + elem.getKey() + " request to be created.");
                final DataSet dset = (DataSet) elem.getValue();
                dset.setName(elem.getKey());

                final DataSetID datasetID = newDataSet(performerID, performerCredential, dset);
                datasetIDs.put(elem.getKey(), datasetID.getId());

            } else if (elem.getValue() instanceof Contract) {
                final Contract contr = (Contract) elem.getValue();

                final ContractID contractID = newContract(performerID, performerCredential, contr);
                contractIDs.put(elem.getKey(), contractID.getId());
            } else if (elem.getValue() instanceof DataContract) {
                final DataContract dcontr = (DataContract) elem.getValue();

                final DataContractID datacontractID = newDataContract(performerID, performerCredential, dcontr);
                datacontractIDs.put(elem.getKey(), datacontractID.getId());
            } else if (elem.getValue() instanceof Interface) {
                final Interface iface = (Interface) elem.getValue();

                final InterfaceID interfaceID = newInterface(performerID, performerCredential, iface);
                interfaceIDs.put(elem.getKey(), interfaceID.getId());
            } else if (elem.getValue() instanceof MetaClass) {
                final MetaClass mclass = (MetaClass) elem.getValue();
                mclass.setName(elem.getKey());

                // newClass expects a Map
                final Map<String, MetaClass> singleElement = new HashMap<>();
                singleElement.put(elem.getKey(), mclass);

                // TODO: (LASTTODO) fix this to add language!
                final Map<String, MetaClass> metaclassResponse = newClass(performerID, performerCredential,
                        Langs.LANG_NONE, singleElement);

                // We are only interested in the provided element
                if (metaclassResponse.containsKey(elem.getKey())) {
                    metaclassIDs.put(elem.getKey(), metaclassResponse.get(elem.getKey()).getDataClayID().getId());
                }
            } else {
                throw new DataClayRuntimeException(ERRORCODE.UNEXPECTED_EXCEPTION,
                        "Malformed YAML --not a valid dataClay set of operations", true);
            }
        }

        LOGGER.debug(
                "Ready to return the following sets of IDs (count):\n" + "    - Namespaces:    #{}\n"
                        + "    - Datasets:      #{}\n" + "    - Contracts:     #{}\n" + "    - DataContracts: #{}\n"
                        + "    - Interfaces:    #{}\n" + "    - MetaClasses:   #{}",
                () -> namespaceIDs.size(), () -> datasetIDs.size(), () -> contractIDs.size(),
                () -> datacontractIDs.size(), () -> interfaceIDs.size(), () -> metaclassIDs.size());

        return yaml.dump(returnIDs).getBytes();
    }

    // ============== Account Manager ==============//

    @Override
    public AccountID newAccountNoAdmin(final Account newAccount) {

        // Create the new account (admin account validated during the newAccount op)
        final Account adminAccount = accountMgrApi.getAccount(publicIDs.dcAdminID);
        final AccountID accountID = accountMgrApi.newAccount(publicIDs.dcAdminID, adminAccount.getCredential(),
                newAccount);

        // Create a default dataset for the account
        final DataSet ds = new DataSet(newAccount.getUsername() + DEFAULT_DS_SUFFIX, accountID, false);
        newDataSet(accountID, newAccount.getCredential(), ds);
        final Calendar begin = Calendar.getInstance();
        begin.add(Calendar.YEAR, -1);
        final Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 100);
        final DataContract dc = new DataContract(ds.getDataClayID(), newAccount.getDataClayID(), new HashSet<>(),
                begin, end, false);
        newDataContract(accountID, newAccount.getCredential(), dc);

        // Create default namespaces for the account
        final Namespace ns1 = new Namespace(newAccount.getUsername() + DEFAULT_JAVA_NS_SUFFIX, newAccount.getUsername(),
                Langs.LANG_JAVA);
        try {
            newNamespace(accountID, newAccount.getCredential(), ns1);
        } catch (final NamespaceExistsException e) {
            // FIXME: if the namespace exists, it means database was exported from another
            // dataClay
            // in a federation model. Note that namespace Mgr and Class Mgr are databases
            // than can be 'exported'
            // to allow federation.
            // in this case, we could find a namespace with same user name if two dataClays
            // have
            // accounts with same name (Bob) and we export from one to another (we export
            // BobNamespace)
            // we will try to create BobNamespace in both cases but BobNamespace could be
            // already
            // exported.
            // this will not be necessary once we register models and namespaces with
            // predefined IDs.
            LOGGER.debug("Java namespace already exists (can be expected in Federation): ", e);
        }
        final Namespace ns2 = new Namespace(newAccount.getUsername() + DEFAULT_PYTHON_NS_SUFFIX,
                newAccount.getUsername(), Langs.LANG_PYTHON);
        try {
            newNamespace(accountID, newAccount.getCredential(), ns2);
        } catch (final NamespaceExistsException e) {
            // check comments in previous exception for Java
            LOGGER.debug("Python Namespace already exists (can be expected in Federation): ", e);
        }
        return accountID;
    }

    @Override
    public AccountID newAccount(final AccountID adminAccountID, final PasswordCredential adminCredential,
                                final Account newAccount) {

        // Create the new account (admin account validated during the newAccount op)
        final AccountID accountID = accountMgrApi.newAccount(adminAccountID, adminCredential, newAccount);

        // Create a default dataset for the account
        final DataSet ds = new DataSet(newAccount.getUsername() + DEFAULT_DS_SUFFIX, accountID, false);
        newDataSet(accountID, newAccount.getCredential(), ds);
        final Calendar begin = Calendar.getInstance();
        begin.add(Calendar.YEAR, -1);
        final Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 100);
        final DataContract dc = new DataContract(ds.getDataClayID(), newAccount.getDataClayID(), new HashSet<>(),
                begin, end, false);
        newDataContract(accountID, newAccount.getCredential(), dc);

        // Create default namespaces for the account
        final Namespace ns1 = new Namespace(newAccount.getUsername() + DEFAULT_JAVA_NS_SUFFIX, newAccount.getUsername(),
                Langs.LANG_JAVA);
        newNamespace(accountID, newAccount.getCredential(), ns1);
        final Namespace ns2 = new Namespace(newAccount.getUsername() + DEFAULT_PYTHON_NS_SUFFIX,
                newAccount.getUsername(), Langs.LANG_PYTHON);
        newNamespace(accountID, newAccount.getCredential(), ns2);
        return accountID;
    }

    @Override
    public AccountID getAccountID(final String accountName) {
        return accountMgrApi.getAccountID(accountName);
    }

    @Override
    public HashSet<AccountID> getAccountList(final AccountID adminAccountID, final PasswordCredential adminCredential) {
        HashSet<AccountID> accountsIDs = null;
        // Get the list of users
        accountsIDs = accountMgrApi.getAccountList(adminAccountID, adminCredential);
        return accountsIDs;
    }

    // ============== Session Manager ==============//

    @Override
    public SessionInfo newSession(final AccountID accountID, final PasswordCredential credential,
                                  final Set<ContractID> contracts, final Set<DataSetID> dataSetIDs, final DataSetID dataSetForStore,
                                  final Langs newsessionLang) {

        final boolean valid = accountMgrApi.validateAccount(accountID, credential, AccountRole.NORMAL_ROLE);
        if (!valid) {
            throw new DataClayRuntimeException(ERRORCODE.INVALID_CREDENTIALS);
        }

        return newSessionInternal(accountID, contracts, dataSetIDs, dataSetForStore, newsessionLang);

    }

    /**
     * New session internal function.
     *
     * @param accountID
     *            ID of account of the session
     * @param contracts
     *            Contracts of the session
     * @param dataSets
     *            names of datasets of the session
     * @param dataSetForStore
     *            Name of dataset for store
     * @param newsessionLang
     *            Session language
     * @return Information of new session.
     */
    private SessionInfo newSessionInternal(final AccountID accountID, final Set<ContractID> contracts,
                                           final Set<DataSetID> dataSets, final DataSetID dataSetForStore, final Langs newsessionLang) {

        // Get datasets info
        final Map<String, DataSet> datasetsInfo = dataSetMgrApi.getDataSetsInfo(dataSets);

        // Obtain Set of DataSetID
        final Set<DataSetID> datasetsIDs = new HashSet<>();
        for (final DataSet datasetInfo : datasetsInfo.values()) {
            final DataSetID dsID = datasetInfo.getDataClayID();
            if (datasetsIDs.contains(dsID)) {
                continue;
            }
            datasetsIDs.add(dsID);
            if (datasetInfo.getIsPublic()) {
                final DataContractID datacontractID = datacontractMgrApi.getPublicDataContractIDOfProvider(dsID);
                if (datacontractID == null) {
                    throw new DataClayRuntimeException(ERRORCODE.DATACONTRACT_NOT_PUBLIC,
                            " Dataset " + datasetInfo.getName() + " has no public conract.", false);
                }
                try {
                    datacontractMgrApi.registerToPublicDataContract(accountID, datacontractID);
                } catch (final AccountAlreadyHasADataContractWithProvider ex) {
                    // Ignore
                }
            }
        }

        // Validate data contracts and get info
        final Map<DataContractID, DataContract> dataContractsInfo = datacontractMgrApi
                .getInfoOfSomeActiveDataContractsForAccountWithProviders(accountID, datasetsIDs);

        // Validate dataSetForStore is among data contracts and calculate endDate from
        // them
        final Tuple<DataContractID, Calendar> checkedDataSetAmongDataContracts = validateDataSetForStore(
                dataContractsInfo, datasetsInfo, dataSetForStore);
        final DataContractID dataContractIDforStore = checkedDataSetAmongDataContracts.getFirst();
        Calendar endDate = checkedDataSetAmongDataContracts.getSecond();

        // Validate model contracts and get info
        final Map<ContractID, Tuple<Map<InterfaceID, InterfaceInContract>, Calendar>> infoOfInterfacesInContracts = contractMgrApi
                .getInfoOfMultipleContractsPerActiveContractsForAccount(accountID, contracts);

        // Index implementations and calculate endDate for the session
        final HashSet<InterfaceID> interfacesIDs = new HashSet<>();
        final HashSet<ImplementationID> implementationsIDs = new HashSet<>();
        for (final Tuple<Map<InterfaceID, InterfaceInContract>, Calendar> interfacesInContract : infoOfInterfacesInContracts
                .values()) {
            for (final Entry<InterfaceID, InterfaceInContract> curEntry : interfacesInContract.getFirst().entrySet()) {
                final InterfaceID interfaceID = curEntry.getKey();
                final InterfaceInContract interfaceInContract = curEntry.getValue();
                interfacesIDs.add(interfaceID);

                // Implementations
                for (final Entry<OperationID, OpImplementations> curOpImpls : interfaceInContract
                        .getAccessibleImplementations().entrySet()) {
                    implementationsIDs.add(curOpImpls.getValue().getRemoteImplementationID());
                    implementationsIDs.add(curOpImpls.getValue().getLocalImplementationID());
                }
            }

            // End date
            if (endDate == null || interfacesInContract.getSecond().before(endDate)) {
                endDate = interfacesInContract.getSecond();
            }
        }
        if (endDate.before(Calendar.getInstance())) {
            throw new DataClayRuntimeException(ERRORCODE.APPLIABLE_END_DATE_FOR_SESSION_IS_BEFORE_CURRENT_DATE);
        }

        // Get info of interfaces
        final Map<InterfaceID, Interface> infoOfInterfaces = interfaceMgrApi.getInterfacesInfo(interfacesIDs);

        // Get info of classes of interfaces
        final Map<MetaClassID, MetaClass> classesInfo = new HashMap<>();
        final Map<MetaClassID, Set<Interface>> interfacesPerClass = new HashMap<>();
        for (final Interface curInterface : infoOfInterfaces.values()) {
            final MetaClassID curClassID = curInterface.getMetaClassID();
            if (!classesInfo.containsKey(curClassID)) {
                final MetaClass classInfo = classMgrApi.getClassInfo(curClassID);
                classesInfo.put(curClassID, classInfo);
            }
            Set<Interface> ifacesOfClass = interfacesPerClass.get(curClassID);
            if (ifacesOfClass == null) {
                ifacesOfClass = new HashSet<>();
                interfacesPerClass.put(curClassID, ifacesOfClass);
            }
            ifacesOfClass.add(curInterface);
        }

        // CROSS-NAMESPACE STUFF
        // Get info of the implementations
        final Map<ImplementationID, Implementation> infoOfImplementations = classMgrApi
                .getInfoOfImplementations(implementationsIDs);

        // Get the info of namespaces of implementations
        final Map<ImplementationID, NamespaceID> namespaceIDsOfImplementations = new HashMap<>();
        for (final Entry<ImplementationID, Implementation> curImplementation : infoOfImplementations.entrySet()) {
            final ImplementationID curImplementationID = curImplementation.getKey();
            final NamespaceID curNamespaceID = curImplementation.getValue().getNamespaceID();
            namespaceIDsOfImplementations.put(curImplementationID, curNamespaceID);
        }
        final Set<NamespaceID> namespaceIDs = new HashSet<>(namespaceIDsOfImplementations.values());
        final Map<NamespaceID, Namespace> namespacesInfo = namespaceMgrApi.getNamespacesInfo(namespaceIDs);

        if (dataSets.size() == 0) {
            throw new DataClayRuntimeException(ERRORCODE.NULL_OR_EMPTY_PARAMETER_EXCEPTION, "Received information for #"
                    + namespacesInfo.size() + " namespace(s) and #" + dataSets.size() + " dataset(s)", true);
        }

        // Build model contract session stuff and bitsets for every class
        final Map<ContractID, SessionContract> sessionContracts = new HashMap<>();
        final Map<MetaClassID, BitSet> bitsetsPerClass = new HashMap<>();

        for (final Entry<ContractID, Tuple<Map<InterfaceID, InterfaceInContract>, Calendar>> curEntry : infoOfInterfacesInContracts
                .entrySet()) {

            final ContractID curContractID = curEntry.getKey();
            final Tuple<Map<InterfaceID, InterfaceInContract>, Calendar> curNamespaceAndInterfacesInContract = curEntry
                    .getValue();
            final Map<InterfaceID, InterfaceInContract> curInterfacesInContract = curNamespaceAndInterfacesInContract
                    .getFirst();

            // Build session interfaces for every contract
            final Map<InterfaceID, SessionInterface> sessionInterfaces = new HashMap<>();
            for (final Entry<InterfaceID, InterfaceInContract> curInterfaceInContractEntry : curInterfacesInContract
                    .entrySet()) {

                final InterfaceID curInterfaceID = curInterfaceInContractEntry.getKey();
                final InterfaceInContract curInterfaceInContract = curInterfaceInContractEntry.getValue();
                final Interface curInterface = infoOfInterfaces.get(curInterfaceID);

                // Add BitSet if it is the first time we explore the class of the interface
                if (!bitsetsPerClass.containsKey(curInterface.getMetaClassID())) {
                    bitsetsPerClass.put(curInterface.getMetaClassID(), new BitSet());
                }
                final BitSet curBitSet = bitsetsPerClass.get(curInterface.getMetaClassID());

                // Build session properties for every interface and update bitset of class
                final Map<PropertyID, SessionProperty> sessionProperties = new HashMap<>();
                final MetaClass classInfo = classesInfo.get(curInterface.getMetaClassID());
                final List<Property> propertiesInfo = classInfo.getProperties();
                for (final Property curProp : propertiesInfo) {
                    final PropertyID propertyID = curProp.getDataClayID();
                    if (curInterface.getPropertiesIDs().contains(propertyID)) {
                        final SessionProperty sessionProperty = new SessionProperty(propertyID);
                        sessionProperties.put(propertyID, sessionProperty);
                    }
                }

                setIfacesBitMap(curInterface, classesInfo, curBitSet, interfacesPerClass, 0);

                // Build session operations for every interface
                final Map<OperationID, SessionOperation> sessionOperations = new HashMap<>();
                for (final OperationID operationID : curInterface.getOperationsIDs()) {
                    final OpImplementations opImpls = curInterfaceInContract.getAccessibleImplementations()
                            .get(operationID);
                    final ImplementationID localImplID = opImpls.getLocalImplementationID();
                    final NamespaceID namespaceIDofLocalImplID = infoOfImplementations.get(localImplID)
                            .getNamespaceID();

                    final ImplementationID remoteImplID = opImpls.getRemoteImplementationID();
                    final NamespaceID namespaceIDofRemoteImplID = infoOfImplementations.get(remoteImplID)
                            .getNamespaceID();

                    final SessionImplementation localImpl = new SessionImplementation(localImplID,
                            namespaceIDofLocalImplID,
                            namespacesInfo.get(namespaceIDofLocalImplID).getProviderAccountID());
                    final SessionImplementation remoteImpl = new SessionImplementation(remoteImplID,
                            namespaceIDofRemoteImplID,
                            namespacesInfo.get(namespaceIDofRemoteImplID).getProviderAccountID());
                    final SessionOperation sessionOperation = new SessionOperation(operationID, localImpl, remoteImpl);

                    sessionOperations.put(operationID, sessionOperation);

                    implementationsIDs.add(localImplID);
                    implementationsIDs.add(remoteImplID);
                }

                // Build the current sessionInterface
                final SessionInterface sessionInterface = new SessionInterface(curInterfaceID,
                        curInterface.getMetaClassID());

                sessionInterface.setSessionProperties(sessionProperties);
                sessionInterface.setSessionOperations(sessionOperations);

                // Add sessionInterface to session interfaces
                sessionInterfaces.put(curInterfaceID, sessionInterface);
            }

            // Build the current session contract
            final SessionContract sessionContract = new SessionContract(curContractID);
            sessionContract.setSessionInterfaces(sessionInterfaces);

            // Add sessionContract to session contracts
            sessionContracts.put(curContractID, sessionContract);
        }

        // Build data contract session stuff
        final Map<DataContractID, SessionDataContract> sessionDataContracts = new HashMap<>();
        for (final Entry<DataContractID, DataContract> curDataContract : dataContractsInfo.entrySet()) {
            final DataContractID dataContractID = curDataContract.getKey();
            final DataContract dataContract = curDataContract.getValue();
            final DataSetID dataSetIDofProvider = dataContract.getProviderDataSetID();
            final SessionDataContract sessionDataContract = new SessionDataContract(dataContractID,
                    dataSetIDofProvider);
            sessionDataContracts.put(dataContractID, sessionDataContract);
        }

        // Finally register the new session
        // Generate bitmaps for interfaces of stubs
        final Map<MetaClassID, byte[]> bitmapsPerClass = new HashMap<>();
        for (final Entry<MetaClassID, BitSet> curEntry : bitsetsPerClass.entrySet()) {
            bitmapsPerClass.put(curEntry.getKey(), curEntry.getValue().toByteArray());
        }
        final SessionInfo result = sessionMgrApi.newSession(accountID, sessionContracts, sessionDataContracts,
                dataContractIDforStore, endDate, newsessionLang, bitmapsPerClass);
        return result;
    }

    @Override
    public void closeSession(final SessionID sessionID) {

        LOGGER.debug("==> Closing session " + sessionID);
        sessionMgrApi.closeSession(sessionID);

        // Broadcast closing of session
        for (final Tuple<DataServiceAPI, ExecutionEnvironment> elem : getExecutionEnvironments(Langs.LANG_JAVA).values()) {
            try {
                elem.getFirst().closeSessionInDS(sessionID);
            } catch (final StatusRuntimeException e) {
                // if unavailabe, ignore
                if (e.getStatus().getCode().equals(Status.UNAVAILABLE.getCode())) {
                    LOGGER.debug("Execution environment {} is not available. Not closing session there.", elem.getSecond());
                } else {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        for (final Tuple<DataServiceAPI, ExecutionEnvironment> elem : getExecutionEnvironments(Langs.LANG_PYTHON).values()) {
            try {
                elem.getFirst().closeSessionInDS(sessionID);
            } catch (final StatusRuntimeException e) {
                // if unavailabe, ignore
                if (e.getStatus().getCode().equals(Status.UNAVAILABLE.getCode())) {
                    LOGGER.debug("Execution environment {} is not available. Not closing session there.", elem.getSecond());
                } else {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        LOGGER.debug("<== Closed session " + sessionID);
    }

    /**
     * Set iface bitmap for session.
     *
     * @param curInterface
     *            Current interface
     * @param classesInfo
     *            Information of classes
     * @param curBitSet
     *            Current bitset being set
     * @param ifacesPerClass
     *            Interfaces per class
     * @param initialPosition
     *            Initial position (used for embbeded)
     */
    private void setIfacesBitMap(final Interface curInterface, final Map<MetaClassID, MetaClass> classesInfo,
                                 final BitSet curBitSet, final Map<MetaClassID, Set<Interface>> ifacesPerClass, final int initialPosition) {

        int curPosition = initialPosition;
        final MetaClass classInfo = classesInfo.get(curInterface.getMetaClassID());
        final List<Property> propertiesInfo = classInfo.getProperties();
        for (final Property curProp : propertiesInfo) {
            final PropertyID propertyID = curProp.getDataClayID();
            if (curInterface.getPropertiesIDs().contains(propertyID)) {
                // Update bitset for calculating the final bitmap afterwards

                // Also, in case of embeded this is setting the boolean indicating if it is null
                curBitSet.set(curPosition);

                // === EMBEDDED FIELDS === //
                /*
                 * if (curProp.getType().getClassID() != null) { MetaClass clazz =
                 * classesInfo.get(curProp.getType().getClassID()); if (clazz.isImmutable()) {
                 * // Get interface of the embedded class Set<Interface> newIfaces =
                 * ifacesPerClass.get(clazz.getID()); for (Interface iface : newIfaces) {
                 * setIfacesBitMap(iface, classesInfo, curBitSet, ifacesPerClass,
                 * curProp.getPosition() + 1); } curPosition = curPosition +
                 * clazz.getProperties().size(); } }
                 */
            }

            curPosition++;
        }
    }

    @Override
    public Tuple<Tuple<DataSetID, Set<DataSetID>>, Calendar> getInfoOfSessionForDS(final SessionID sessionID) {
        // TODO We have to ensure that only a DataService is able to access this method
        // (30 May 2014 jmarti)
        final HashSet<DataSetID> visibleDataSets = new HashSet<>();
        final SessionInfo sessionInfo = getSessionInfo(sessionID);
        DataSetID dataSetForStore = null;
        for (final SessionDataContract sessionDataContract : sessionInfo.getSessionDataContracts().values()) {
            visibleDataSets.add(sessionDataContract.getDataSetOfProvider());
            if (sessionDataContract.getDataContractID().equals(sessionInfo.getDataContractIDforStore())) {
                dataSetForStore = sessionDataContract.getDataSetOfProvider();
            }
        }
        return new Tuple<>(new Tuple<DataSetID, Set<DataSetID>>(dataSetForStore, visibleDataSets),
                sessionInfo.getEndDate());
    }

    // ============== Namespace Manager ==============//

    @Override
    public NamespaceID newNamespace(final AccountID accountID, final PasswordCredential credential,
                                    final Namespace newNamespace) {
        // Validate account
        if (!accountMgrApi.validateAccount(accountID, credential, AccountRole.NORMAL_ROLE)) {
            throw new DataClayRuntimeException(ERRORCODE.INVALID_CREDENTIALS);
        }

        // =========== CHECK AND SET IDS =========== //
        final String providerAccountName = newNamespace.getProviderAccountName();
        if (providerAccountName == null || providerAccountName.isEmpty()) {
            throw new DataClayRuntimeException(ERRORCODE.NULL_OR_EMPTY_PARAMETER_EXCEPTION,
                    "Namespace wrong specification: Provider account name cannot be null nor empty", false);
        }
        final AccountID provAccountID = accountMgrApi.getAccountID(providerAccountName);
        newNamespace.setProviderAccountID(provAccountID);

        final String namespaceName = newNamespace.getName();
        if (namespaceName == null || namespaceName.isEmpty()) {
            throw new DataClayRuntimeException(ERRORCODE.NULL_OR_EMPTY_PARAMETER_EXCEPTION,
                    "Namespace wrong specification: name cannot be null", false);
        }
        // =========== =========== //

        // Generate ID
        newNamespace.setDataClayID(new NamespaceID());
        // Set correctly the responsible account
        newNamespace.setProviderAccountID(accountID);

        // Create the new namespace
        NamespaceID newNamespaceID = namespaceMgrApi.newNamespace(newNamespace);
        LOGGER.info("[LOGICMODULE] Created new namespace called " + namespaceName);

        return newNamespaceID;


    }

    @Override
    public Set<String> getNamespaces(final AccountID accountID, final PasswordCredential credential) {

        // Validate account
        if (!accountMgrApi.validateAccount(accountID, credential, AccountRole.NORMAL_ROLE)) {
            throw new DataClayRuntimeException(ERRORCODE.INVALID_CREDENTIALS);
        }

        return namespaceMgrApi.getNamespacesNames();
    }

    @Override
    public void removeNamespace(final AccountID accountID, final PasswordCredential credential,
                                final String namespaceName) {
        // Get the namespaceID of the namespace
        final NamespaceID namespaceID = namespaceMgrApi.getNamespaceID(namespaceName);

        // Validate account (ADMIN || NAMESPACE RESPONSIBLE)
        try {
            accountMgrApi.validateAccount(accountID, credential, AccountRole.ADMIN_ROLE);
        } catch (final Exception ex) {
            checkNamespaceResponsible(accountID, credential, namespaceID);
        }

        // Check the namespace can be removed
        // Check namespace is not related with any contract
        if (!contractMgrApi.checkNamespaceHasNoContracts(namespaceID)) {
            throw new DataClayRuntimeException(ERRORCODE.NAMESPACE_WITH_CONTRACTS);
        }
        // Check namespace has nothing
        if (!classMgrApi.checkNamespaceHasNothing(namespaceID)) {
            throw new DataClayRuntimeException(ERRORCODE.NAMESPACE_WITH_CLASSES);
        }

        // Remove namespace
        namespaceMgrApi.removeNamespace(namespaceID);

    }

    @Override
    public NamespaceID getNamespaceID(final AccountID accountID, final PasswordCredential credential,
                                      final String namespaceName) {
        // Validate account
        if (!accountMgrApi.validateAccount(accountID, credential)) {
            throw new DataClayRuntimeException(ERRORCODE.INVALID_CREDENTIALS);
        }
        return namespaceMgrApi.getNamespaceID(namespaceName);

    }

    @Override
    public Langs getNamespaceLang(final AccountID accountID, final PasswordCredential credential,
                                  final String namespaceName) {
        // Validate account
        if (!accountMgrApi.validateAccount(accountID, credential)) {
            throw new DataClayRuntimeException(ERRORCODE.INVALID_CREDENTIALS);
        }
        return namespaceMgrApi.getNamespaceLang(namespaceName);
    }

    @Override
    public void importInterface(final AccountID accountID, final PasswordCredential credential,
                                final NamespaceID namespaceID, final ContractID contractID, final InterfaceID interfaceID) {
        // Check namespace responsible
        checkNamespaceResponsible(accountID, credential, namespaceID);

        // Check interface in contract of import and get contract info
        final Map<ContractID, InterfaceID> aux = new HashMap<>();
        aux.put(contractID, interfaceID);
        final Contract contractInfo = contractMgrApi
                .checkInterfacesInActiveContractsForAccountAndReturnContractsInfo(accountID, aux).get(contractID);
        final Map<InterfaceID, InterfaceInContract> interfacesInContract = contractInfo.getInterfacesInContract();

        // Check compatibility between namespaces (language)
        final HashSet<NamespaceID> namespacesIDs = new HashSet<>();
        namespacesIDs.add(namespaceID);
        namespacesIDs.add(contractInfo.getNamespaceID());

        // Init interfaces to be imported with the explicitly specified one
        final HashSet<InterfaceID> interfacesIDsToBeImported = new HashSet<>();
        interfacesIDsToBeImported.add(interfaceID);

        while (!interfacesIDsToBeImported.isEmpty()) {
            final Set<InterfaceID> nextInterfacesIDsToBeImported = new HashSet<>();
            final Map<InterfaceID, ImportedInterface> currentImportss = new HashMap<>();

            // Get info of current interfaces to be imported
            final Map<InterfaceID, Interface> interfacesInfo = interfaceMgrApi
                    .getInterfacesInfo(interfacesIDsToBeImported);

            for (final InterfaceID curInterfaceID : interfacesIDsToBeImported) {
                final Interface interfaceInfo = interfacesInfo.get(curInterfaceID);
                final InterfaceInContract interfaceInContract = interfacesInContract.get(curInterfaceID);
                final MetaClassID classIDofInterface = interfaceInfo.getMetaClassID();

                // Check interface actually comes from another namespace
                if (interfaceInfo.getNamespaceID().equals(namespaceID)) {
                    throw new DataClayRuntimeException(ERRORCODE.IMPORTED_INTERFACE_WAS_CREATED_IN_TARGET_NAMESPACE);
                }

                // Get info of class of interface and its includes filtering by real needs of
                // interface in contract (properties, operations, implementations)
                final Set<PropertyID> propertiesIDsOfInterface = interfaceInfo.getPropertiesIDs();
                final Map<OperationID, Set<ImplementationID>> opsIDsandImplsIDsOfInterfaceInContract = //
                        new HashMap<>();
                for (final Entry<OperationID, OpImplementations> curEntry : interfaceInContract
                        .getAccessibleImplementations().entrySet()) {
                    final HashSet<ImplementationID> implsIDs = new HashSet<>();
                    implsIDs.add(curEntry.getValue().getLocalImplementationID());
                    implsIDs.add(curEntry.getValue().getRemoteImplementationID());
                    opsIDsandImplsIDsOfInterfaceInContract.put(curEntry.getKey(), implsIDs);
                }
                final Triple<String, NamespaceID, Set<MetaClassID>> classInfoAndIncludesOfDifferentNamespace = classMgrApi
                        .getIncludesInDifferentNamespaces(classIDofInterface, propertiesIDsOfInterface,
                                opsIDsandImplsIDsOfInterfaceInContract, namespaceID);
                final String className = classInfoAndIncludesOfDifferentNamespace.getFirst();
                final NamespaceID namespaceIDofClass = classInfoAndIncludesOfDifferentNamespace.getSecond();
                final Set<MetaClassID> includesIDs = classInfoAndIncludesOfDifferentNamespace.getThird();

                // Check namespace of class is different than target namespace for the import
                if (namespaceID.equals(namespaceIDofClass)) {
                    throw new DataClayRuntimeException(
                            ERRORCODE.CLASS_OF_IMPORTED_INTERFACE_WAS_CREATED_IN_TARGET_NAMESPACE);
                }

                // Check there is no class created in target namespace with the same name as
                // imported class
                if (classMgrApi.getMetaClassID(namespaceID, className) != null) {
                    throw new DataClayRuntimeException(ERRORCODE.CLASS_WITH_SAME_NAME_IN_NAMESPACE);
                }

                // Filter subset of interfaces of the contract that are needed given the
                // includes
                // for the current one to be imported
                nextInterfacesIDsToBeImported.addAll(interfaceMgrApi.getSubsetInterfacesOfClasses(
                        new HashSet<>(contractInfo.getInterfacesInContract().keySet()), includesIDs));

                // Filter current interfaceID to be imported
                nextInterfacesIDsToBeImported.removeAll(interfacesIDsToBeImported);

                // Filter those that were already imported in the namespace
                final Set<ImportedInterface> interfacesImported = namespaceMgrApi
                        .getImportedInterfaces(namespaceIDofClass, className);
                for (final ImportedInterface importedInterface : interfacesImported) {
                    nextInterfacesIDsToBeImported.remove(importedInterface.getInterfaceID());
                }

                final ImportedInterface currentInterface = new ImportedInterface(className, curInterfaceID, contractID,
                        classIDofInterface, namespaceIDofClass);
                currentImportss.put(curInterfaceID, currentInterface);
            }

            final HashSet<ImportedInterface> importss = new HashSet<>(currentImportss.values());

            // Deploy imported class
            final HashSet<NamespaceID> domIDs = new HashSet<>();
            domIDs.add(namespaceID);
            for (final ImportedInterface impInterface : importss) {
                final NamespaceID domID = impInterface.getNamespaceIDofClass();
                domIDs.add(domID);
            }

            // Register current imports
            namespaceMgrApi.importInterfaces(namespaceID, new HashSet<>(currentImportss.values()));

            // Update interfacesIDsToBeImported
            interfacesIDsToBeImported.addAll(nextInterfacesIDsToBeImported);
            interfacesIDsToBeImported.removeAll(currentImportss.keySet());
        }

    }

    @Override
    public void importContract(final AccountID accountID, final PasswordCredential credential,
                               final NamespaceID namespaceID, final ContractID contractID) {
        // Check namespace responsible
        checkNamespaceResponsible(accountID, credential, namespaceID);

        // Check compatibility between namespaces (language)
        final LinkedList<ContractID> contractsIDs = new LinkedList<>();
        contractsIDs.add(contractID);
        final Contract contractInfo = contractMgrApi.getInfoOfSomeActiveContractsForAccount(contractsIDs, accountID)
                .get(contractID);
        final Map<InterfaceID, InterfaceInContract> interfacesInContract = contractInfo.getInterfacesInContract();

        // Check compatibility between namespaces
        final HashSet<NamespaceID> namespacesIDs = new HashSet<>();
        namespacesIDs.add(namespaceID);
        namespacesIDs.add(contractInfo.getNamespaceID());

        // Get info of interfaces
        final Map<InterfaceID, Interface> interfacesInfo = interfaceMgrApi
                .getInterfacesInfo(new HashSet<>(contractInfo.getInterfacesInContract().keySet()));

        // Check no interface was created on target namespace
        final HashSet<ImportedInterface> importss = new HashSet<>();
        for (final Entry<InterfaceID, Interface> curInterface : interfacesInfo.entrySet()) {
            final InterfaceID interfaceID = curInterface.getKey();
            final Interface interfaceInfo = curInterface.getValue();
            final InterfaceInContract interfaceInContract = interfacesInContract.get(interfaceID);
            final MetaClassID classIDofInterface = interfaceInfo.getMetaClassID();

            // Check interface was not created in same namespace as target one
            if (interfaceInfo.getNamespaceID().equals(namespaceID)) {
                throw new DataClayRuntimeException(ERRORCODE.IMPORTED_INTERFACE_WAS_CREATED_IN_TARGET_NAMESPACE);
            }

            // Get info of class of interface and its includes filtering by
            // real needs of
            // interface in contract (properties, operations,
            // implementations)
            final Set<PropertyID> propertiesIDsOfInterface = interfaceInfo.getPropertiesIDs();
            final Map<OperationID, Set<ImplementationID>> opsIDsandImplsIDsOfInterfaceInContract = //
                    new HashMap<>();
            for (final Entry<OperationID, OpImplementations> curEntry : interfaceInContract
                    .getAccessibleImplementations().entrySet()) {
                final HashSet<ImplementationID> implsIDs = new HashSet<>();
                implsIDs.add(curEntry.getValue().getLocalImplementationID());
                implsIDs.add(curEntry.getValue().getRemoteImplementationID());
                opsIDsandImplsIDsOfInterfaceInContract.put(curEntry.getKey(), implsIDs);
            }
            final Triple<String, NamespaceID, Set<MetaClassID>> classInfoAndIncludesOfDifferentNamespace = classMgrApi
                    .getIncludesInDifferentNamespaces(classIDofInterface, propertiesIDsOfInterface,
                            opsIDsandImplsIDsOfInterfaceInContract, namespaceID);
            final String className = classInfoAndIncludesOfDifferentNamespace.getFirst();
            final NamespaceID namespaceIDofClass = classInfoAndIncludesOfDifferentNamespace.getSecond();
            // BEWARE: If contract is correct, we do not have to check
            // includes are in contract in this point
            // HashSet<MetaClassID> includesIDs =
            // classInfoAndIncludesOfDifferentNamespace.getThird();

            if (namespaceIDofClass.equals(namespaceID)) {
                throw new DataClayRuntimeException(
                        ERRORCODE.CLASS_OF_IMPORTED_INTERFACE_WAS_CREATED_IN_TARGET_NAMESPACE);
            } else {
                final ImportedInterface importInterface = new ImportedInterface(className, interfaceID, contractID,
                        interfaceInfo.getMetaClassID(), namespaceIDofClass);
                importss.add(importInterface);
            }
        }

        // Deploy imported class
        final HashSet<NamespaceID> domIDs = new HashSet<>();
        domIDs.add(namespaceID);
        for (final ImportedInterface impInterface : importss) {
            final NamespaceID domID = impInterface.getNamespaceIDofClass();
            domIDs.add(domID);
        }

        namespaceMgrApi.importInterfaces(namespaceID, importss);

    }

    // ============== DataSet Manager ==============//

    @Override
    public DataSetID newDataSet(final AccountID accountID, final PasswordCredential credential,
                                final DataSet newDataSet) {

        // Validate account
        if (!accountMgrApi.validateAccount(accountID, credential, AccountRole.NORMAL_ROLE)) {
            throw new DataClayRuntimeException(ERRORCODE.INVALID_CREDENTIALS);
        }

        // ================================================ //
        if (Configuration.Flags.NOTIFICATION_MANAGER_ACTIVE.getBooleanValue()) {
            this.notificationMgrApi.removeSessionInCache(accountID);
        }

        final String dataSetName = newDataSet.getName();
        if (dataSetName == null || dataSetName.isEmpty()) {
            throw new DataClayRuntimeException(ERRORCODE.NULL_OR_EMPTY_PARAMETER_EXCEPTION,
                    "DataSet wrong specification: DataSet name cannot be null", false);
        }
        // =========== =========== //

        // Create ID
        if (newDataSet.getDataClayID() == null) {
            newDataSet.setDataClayID(new DataSetID());
        }
        // Set the responsible account correctly
        newDataSet.setProviderAccountID(accountID);

        // Create the new dataset
        final DataSetID dsID = dataSetMgrApi.newDataSet(newDataSet);

        LOGGER.info("[LOGICMODULE] Created new dataset called " + newDataSet.getName() + " with ID " + dsID);

        return dsID;
    }

    @Override
    public void removeDataSet(final AccountID accountID, final PasswordCredential credential,
                              final String datasetName) {
        // Get the namespaceID of the namespace
        final DataSetID datasetID = dataSetMgrApi.getDataSetID(datasetName);

        // Validate account (ADMIN || NAMESPACE RESPONSIBLE)
        try {
            accountMgrApi.validateAccount(accountID, credential, AccountRole.ADMIN_ROLE);
        } catch (final Exception ex) {
            checkDataSetResponsible(accountID, credential, datasetID);
        }

        // Check the dataset can be removed
        // Check dataset is not related with any contract
        if (!datacontractMgrApi.checkDataSetHasNoDataContracts(datasetID)) {
            throw new DataClayRuntimeException(ERRORCODE.DATASET_WITH_DATACONTRACTS);
        }
        // Check dataset has nothing
        if (!metaDataSrvApi.checkDatasetIsEmpty(datasetID)) {
            throw new DataClayRuntimeException(ERRORCODE.DATASET_NOT_EMPTY);
        }

        // Remove dataset
        dataSetMgrApi.removeDataSet(datasetID);

    }

    @Override
    public DataSetID getDataSetID(final AccountID accountID, final PasswordCredential credential,
                                  final String datasetName) {
        // Validate account
        if (!accountMgrApi.validateAccount(accountID, credential)) {
            throw new DataClayRuntimeException(ERRORCODE.INVALID_CREDENTIALS);
        }

        // Get the datasetID of the namespace
        return dataSetMgrApi.getDataSetID(datasetName);
    }

    @Override
    public boolean checkDataSetIsPublic(final DataSetID datasetID) {
        return dataSetMgrApi.checkDataSetIsPublic(datasetID);
    }

    @Override
    public Set<String> getPublicDataSets(final AccountID accountID, final PasswordCredential credential) {
        // Validate account
        if (!accountMgrApi.validateAccount(accountID, credential)) {
            throw new DataClayRuntimeException(ERRORCODE.INVALID_CREDENTIALS);
        }
        final Set<String> result = new HashSet<>();
        for (final DataSet ds : dataSetMgrApi.getPublicDataSets()) {
            result.add(ds.getName());
        }
        return result;
    }

    @Override
    public Set<String> getAccountDataSets(final AccountID accountID, final PasswordCredential credential) {
        // Validate account
        if (!accountMgrApi.validateAccount(accountID, credential)) {
            throw new DataClayRuntimeException(ERRORCODE.INVALID_CREDENTIALS);
        }
        final Set<String> result = new HashSet<>();
        for (final DataSet ds : dataSetMgrApi.getAllDataSetsOfAccount(accountID)) {
            result.add(ds.getName());
        }
        return result;
    }

    @Override
    public DataSetID getObjectDataSetID(final SessionID sessionID, final ObjectID oid) {
        // Check session if needed
        SessionInfo sessionInfo = null;
        if (Configuration.Flags.CHECK_SESSION.getBooleanValue()) {
            sessionInfo = getSessionInfo(sessionID);
        }

        final MetaDataInfo metadataInfo = getObjectMetadata(oid);
        if (metadataInfo == null) {
            throw new DataClayRuntimeException(ERRORCODE.OBJECT_NOT_EXIST);
        }

        // Check dataset if needed
        if (Configuration.Flags.CHECK_SESSION.getBooleanValue()) {
            checkDataSetAmongDataContracts(metadataInfo.getDatasetID(), sessionInfo.getSessionDataContracts());
        }

        return metadataInfo.getDatasetID();
    }

    // ============== Class Manager ==============//

    @Override
    public Map<String, MetaClass> newClass(final AccountID accountID, final PasswordCredential credential,
                                           final Langs language, final Map<String, MetaClass> newClasses) {
        return registerAndUpdateDependencies(accountID, credential, language, newClasses, null, null);
    }

	/**
	 * Manage the class deployment (to all DataServices) of Java classes.
	 *
	 * @param allMetaClasses
	 *            all the MetaClasses related to this deployment.
	 * @param classesToDeploy
	 *            the name for all the classes which should be deployed (this should
	 *            be a subset of the previous set).
	 * @param namespaceInfos
	 *            all the namespace information of the namespaces of the classes
	 *            (although the deployment should be only on a specific one).
	 * @param enrichmentNamespaceID
	 *            NamespaceID for the enrichment when applicable.
	 * @param specificLocationOnly
	 *            Indicates only to deploy to a specific location
	 * @throws RemoteException
	 *             from the ClassManager calls, when things go wrong.
	 */
	private void outsourceClassesDeploymentJava(final Set<MetaClass> allMetaClasses, final Set<String> classesToDeploy,
			final Map<NamespaceID, Namespace> namespaceInfos, final NamespaceID enrichmentNamespaceID,
			final ExecutionEnvironment specificLocationOnly) {

        final Map<Tuple<String, MetaClassID>, byte[]> bytecodes = new HashMap<>();
        final Map<String, byte[]> aspects = new HashMap<>();
        final Map<String, byte[]> yamls = new HashMap<>();

        Namespace namespaceInfo = null;
        if (enrichmentNamespaceID != null) {
            namespaceInfo = namespaceInfos.get(enrichmentNamespaceID);

        }
        for (final MetaClass curMetaClass : allMetaClasses) {
            final String className = curMetaClass.getName();
            final MetaClassID classID = curMetaClass.getDataClayID();
            if (classesToDeploy.contains(curMetaClass.getName())) {

                if (namespaceInfo == null) {
                    // WARNING!!!! Precondition: all classes belongs to the same namespace
                    namespaceInfo = namespaceInfos.get(curMetaClass.getNamespaceID());
                }
                /*
                 * Get the execution class for Java
                 */
                final Tuple<byte[], byte[]> classAndAspectsToDeploy = classMgrApi
                        .generateJavaExecutionClass(curMetaClass);
                bytecodes.put(new Tuple<>(className, classID), classAndAspectsToDeploy.getFirst());
                aspects.put(className, classAndAspectsToDeploy.getSecond());
                // Yamls
                final StubInfo execStubInfo = this.getStubInfoForExecutionClass(curMetaClass.getDataClayID());
                final Yaml yaml = CommonYAML.getYamlObject();
                final String yamlStr = yaml.dump(execStubInfo);
                yamls.put(className, yamlStr.getBytes());
            }
        }

		// First deploy the class at the adequate Execution Environments
		if (specificLocationOnly == null) {
			for (final Tuple<DataServiceAPI, ExecutionEnvironment> elem : getExecutionEnvironments(Langs.LANG_JAVA).values()) {
				LOGGER.debug("Sending classes to {}", elem.getSecond());
				elem.getFirst().deployClasses(namespaceInfo.getName(), bytecodes, aspects, yamls);
			}
		} else {
			final DataServiceAPI elem = getExecutionEnvironments(Langs.LANG_JAVA).get(specificLocationOnly.getDataClayID()).getFirst();
			LOGGER.debug("Sending classes to {}", specificLocationOnly);
			elem.deployClasses(namespaceInfo.getName(), bytecodes, aspects, yamls);
		}
	}

	/**
	 * Manage the class deployment (to all DataServices) of Java classes.
	 *
	 * @param allMetaClasses
	 *            all the MetaClasses related to this deployment.
	 * @param classesToDeploy
	 *            the name for all the classes which should be deployed (this should
	 *            be a subset of the previous set).
	 * @param namespaceInfos
	 *            all the namespace information of the namespaces of the classes
	 *            (although the deployment should be only on a specific one).
	 * @param enrichmentNamespaceID
	 *            NamespaceID for the enrichment when applicable.
	 * @param specificEnvironmentOnly
	 *            specific environment if required (optional)
	 *
	 * @throws RemoteException
	 *             from the ClassManager calls, when things go wrong.
	 */
	private void outsourceClassesDeploymentPython(final Set<MetaClass> allMetaClasses,
			final Set<String> classesToDeploy, final Map<NamespaceID, Namespace> namespaceInfos,
			final NamespaceID enrichmentNamespaceID, final ExecutionEnvironment specificEnvironmentOnly) {

        final Map<String, MetaClass> deploymentPack = new HashMap<>();

        Namespace namespaceInfo = null;
        if (enrichmentNamespaceID != null) {
            namespaceInfo = namespaceInfos.get(enrichmentNamespaceID);
        }

        for (final MetaClass curMetaClass : allMetaClasses) {
            final String className = curMetaClass.getName();
            if (classesToDeploy.contains(curMetaClass.getName())) {

                if (namespaceInfo == null) {
                    // WARNING!!!! Precondition: all classes belongs to the same namespace
                    namespaceInfo = namespaceInfos.get(curMetaClass.getNamespaceID());
                }

				deploymentPack.put(className, curMetaClass);
			}
		}
		if (specificEnvironmentOnly == null) {
			// First deploy the classes at the adequate Execution Environments
			for (final Tuple<DataServiceAPI, ExecutionEnvironment> elem : getExecutionEnvironments(Langs.LANG_PYTHON).values()) {
				LOGGER.debug("Sending classes to {}", elem.getFirst());
				elem.getFirst().deployMetaClasses(namespaceInfo.getName(), deploymentPack);
			}
		} else {
			final DataServiceAPI elem = getExecutionEnvironments(Langs.LANG_PYTHON).get(specificEnvironmentOnly.getDataClayID()).getFirst();
			LOGGER.debug("Sending classes to {}", specificEnvironmentOnly);
			elem.deployMetaClasses(namespaceInfo.getName(), deploymentPack);
		}
	}

    /**
     * This internal operation recursively registers all the required dependencies
     * of a metaClass to be usable.
     *
     * @param accountID
     *            ID of the account of the user that calls the operation
     * @param credentials
     *            Credentials of the account provided
     * @param language
     *            Language of the classes provided
     * @param metaClasses
     *            ifications of classes to create
     * @param enrichmentNamespaceID
     *            ID of the Namespace of the enrichment (can be NULL)
     * @param enrichedClassName
     *            if we are executing the method from en enrichment process, the
     *            name of the enriched class
     *
     * @return the id or information of the new metaclass if the operation succeeds.
     *         null otherwise.
     * @throws RemoteException
     *             if some exception occurs
     */
    public Map<String, MetaClass> registerAndUpdateDependencies(final AccountID accountID,
                                                                final PasswordCredential credentials, final Langs language, final Map<String, MetaClass> metaClasses,
                                                                final NamespaceID enrichmentNamespaceID, final String enrichedClassName) {

        // IDs structures
        final Map<String, MetaClassID> classIDsSet = new HashMap<>();
        final Map<String, PropertyID> propIDsSet = new HashMap<>();
        final Map<String, ImplementationID> implIDsSet = new HashMap<>();
        final Map<String, MetaClass> infoOfRegisteredClasses = new HashMap<>();
        final Set<String> createdClasses = new HashSet<>();
        final Map<NamespaceID, Namespace> namespaceInfos = new HashMap<>();

        for (final MetaClass curMetaClass : metaClasses.values()) {

            final String namespace = curMetaClass.getNamespace();
            final NamespaceID namespaceID = namespaceMgrApi.getNamespaceID(namespace);
            final Namespace namespaceInfo = namespaceMgrApi.getNamespaceInfo(namespaceID);
            // TODO: Check namespace responsible or imports
            namespaceInfos.put(namespaceID, namespaceInfo);
        }

        if (enrichmentNamespaceID != null) {
            final Namespace namespaceInfo = checkNamespaceResponsible(accountID, credentials, enrichmentNamespaceID);
            namespaceInfos.put(enrichmentNamespaceID, namespaceInfo);
        }

        // ===================================================== //
        // ====== CHECK CLASSES THAT EXISTS AND SET IDS ======== //
        // ===================================================== //

        final Set<MetaClass> metaClassesToInstall = new HashSet<>();
        for (final MetaClass curMetaClass : metaClasses.values()) {
            final String namespace = curMetaClass.getNamespace();
            final String signature = Reflector.getSignatureFromTypeName(curMetaClass.getName());
            final NamespaceID namespaceID = namespaceMgrApi.getNamespaceID(namespace);
            MetaClassID classID = null;

            final UserType uType = new UserType(namespace, curMetaClass.getName(), signature, signature, null);
            final boolean typeExists = checkTypeIsRegistered(accountID, credentials, uType);

            if (enrichedClassName != null && curMetaClass.getName().equals(enrichedClassName)) {
                // =================== ENRICHMENT SET IDS ====================== //

                // GET IDS OF THE CLASS (in case it is accessed)
                classID = classMgrApi.getMetaClassID(namespaceID, enrichedClassName);
                getIDsOfAlreadyRegisteredClass(accountID, credentials, namespaceID, curMetaClass, classIDsSet,
                        propIDsSet, implIDsSet, classID);

                // Set IDs
                // GENERATE IDS FOR PROPERTIES AND IMPLEMENTATIONS OF THE CLASS TO REGISTER
                generateIDsToRegister(curMetaClass, propIDsSet, implIDsSet, namespaceInfos, classID, namespaceID,
                        accountID);
                metaClassesToInstall.add(curMetaClass);

            } else if (!typeExists) {

                // =================== NEW CLASS ====================== //

                // Set ClassID
                classID = new MetaClassID();
                curMetaClass.setDataClayID(classID);
                classIDsSet.put(curMetaClass.getNamespace() + "$" + curMetaClass.getName(), classID);
                // GENERATE IDS FOR PROPERTIES AND IMPLEMENTATIONS OF THE CLASS TO REGISTER
                generateIDsToRegister(curMetaClass, propIDsSet, implIDsSet, namespaceInfos, classID, namespaceID,
                        accountID);
                metaClassesToInstall.add(curMetaClass);

            } else {
                // =================== ALREADY EXISTANT CLASS GET IDS ====================== //

                // GET IDS OF THE CLASS (in case it is accessed)
                classID = classMgrApi.getMetaClassID(namespaceID, curMetaClass.getName());
                getIDsOfAlreadyRegisteredClass(accountID, credentials, namespaceID, curMetaClass, classIDsSet,
                        propIDsSet, implIDsSet, classID);
            }
        }

        for (final MetaClass curMetaClass : metaClassesToInstall) {
            // Set ClassID of all associated metaclass
            // Also set PropertyIDs and ImplementationIDs for accessed props and impls
            setIDs(accountID, credentials, curMetaClass, classIDsSet, propIDsSet, implIDsSet, language);
        }

        for (final MetaClass curMetaClass : metaClassesToInstall) {
            final NamespaceID namespaceID = curMetaClass.getNamespaceID();
            // Update with classIDs dependencies
            updateDependencies(curMetaClass, classIDsSet, propIDsSet, implIDsSet, language);
            if (enrichedClassName == null
                    || (enrichedClassName != null && !curMetaClass.getName().equals(enrichedClassName))) {

                // Register class
                final MetaClass curInfo = newClassInternal(accountID, credentials, namespaceID, curMetaClass,
                        namespaceInfos.get(namespaceID), language);
                infoOfRegisteredClasses.put(curInfo.getName(), curInfo);
                createdClasses.add(curInfo.getName());
            }
        }

        if (createdClasses.size() > 0) {
            // There are classes to deploy
            if (language == Langs.LANG_JAVA) {
                outsourceClassesDeploymentJava(metaClassesToInstall, createdClasses, namespaceInfos,
                        enrichmentNamespaceID, null);

                // outsourceClassesDeploymentPython(metaClassesToInstall, createdClasses,
                // namespaceInfos, enrichmentNamespaceID,
                // null);

            } else if (language == Langs.LANG_PYTHON) {
                outsourceClassesDeploymentPython(metaClassesToInstall, createdClasses, namespaceInfos,
                        enrichmentNamespaceID, null);
                // @TODO: real cross-language in both directions
                // outsourceClassesDeploymentJava(languagelessMetaClassesToInstall,
                // createdClasses, namespaceInfos, enrichmentNamespaceID, null);
            } // else {
            // WARNING
            // IGNORE it: used for outsource of classes without language
            // throw new DataClayRuntimeException(ERRORCODE.CLASS_UNSUPPORTED_LANGUAGE,
            // "The provided class' language is not supported for registration");
            // }
        }

        // ===================================================== //
        // ====== SET ECAS IDS ======== //
        // ===================================================== //
        if (Configuration.Flags.NOTIFICATION_MANAGER_ACTIVE.getBooleanValue()) {
            for (final MetaClass curMetaClass : metaClassesToInstall) {
                final List<ECA> ecas = curMetaClass.getEcas();
                if (ecas == null) {
                    continue;
                }
                for (final ECA eca : ecas) {
                    final EventType evType = eca.getEventType();

                    // Initialize IDs
                    evType.init(accountID, credentials, curMetaClass.getNamespace(), this);

                    // Set operationIDs of filter and action
                    if (eca.getFilterMethodSignature() != null) {
                        final OperationID filterOpID = classMgrApi.getOperationID(curMetaClass.getDataClayID(),
                                eca.getFilterMethodSignature());
                        eca.setFilterMethod(filterOpID);
                    }

                    if (eca.getActionSignature() == null) {
                        throw new DataClayRuntimeException(ERRORCODE.NULL_OR_EMPTY_PARAMETER_EXCEPTION,
                                "ECA action signature cannot be null", false);
                    }

                    final OperationID actionOpID = classMgrApi.getOperationID(curMetaClass.getDataClayID(),
                            eca.getActionSignature());
                    eca.setAction(actionOpID);

                    // Set target class: the same used in new class
                    eca.setTargetClass(curMetaClass.getDataClayID());

                    // Now Register ECA
                    this.registerEventListenerImplementation(accountID, credentials, eca);

                }
            }
        }

        /******** RT: Store Prefetching Info ********/
        return infoOfRegisteredClasses;
    }

    /**
     * Generate IDs of properties, implementations, namespaces to register
     *
     * @param curMetaClass
     *            MetaClass to process
     * @param propIDsSet
     *            Currently set PropertyIDs (for enrichments)
     * @param implIDsSet
     *            Currently set ImplementationIDs (for enrichments)
     * @param namespaceInfos
     *            Information of namespaces of the classes to install
     * @param classID
     *            ID of the class to set
     * @param namespaceID
     *            ID of the namespace to set
     * @param accountID
     *            ID of the account
     */
    private void generateIDsToRegister(final MetaClass curMetaClass, final Map<String, PropertyID> propIDsSet,
                                       final Map<String, ImplementationID> implIDsSet, final Map<NamespaceID, Namespace> namespaceInfos,
                                       final MetaClassID classID, final NamespaceID namespaceID, final AccountID accountID) {
        for (final Property property : curMetaClass.getProperties()) {

            final String propNamespace = property.getNamespace();
            final NamespaceID propNamespaceID = namespaceMgrApi.getNamespaceID(propNamespace);
            property.setNamespaceID(propNamespaceID);
            property.setMetaClassID(classID);
            property.setDataClayID(new PropertyID());

            final String key = propNamespace + "$" + curMetaClass.getName() + "$" + property.getName();
            if (!propIDsSet.containsKey(key)) {
                // For accessed props
                propIDsSet.put(key, property.getDataClayID());
            }

        }
        for (final Operation operation : curMetaClass.getOperations()) {
            // Set IDs
            operation.setDataClayID(new OperationID());
            final String opNamespace = operation.getNamespace();
            final NamespaceID opNamespaceID = namespaceMgrApi.getNamespaceID(opNamespace);
            operation.setNamespaceID(opNamespaceID);

            for (final Implementation implementation : operation.getImplementations()) {

                final String implNamespace = implementation.getNamespace();
                final NamespaceID implNamespaceID = namespaceMgrApi.getNamespaceID(implNamespace);
                implementation.setNamespaceID(implNamespaceID);
                implementation.setDataClayID(new ImplementationID());
                implementation.setOperationID(operation.getDataClayID());
                implementation.setResponsibleAccountID(accountID);
                implementation.setMetaClassID(classID);
                final String key = implementation.getNamespace() + "$" + implementation.getClassName() + "$"
                        + implementation.getOpNameAndDescriptor() + "$" + implementation.getPosition();
                if (!implIDsSet.containsKey(key)) {
                    // For accessed impls
                    implIDsSet.put(key, implementation.getDataClayID());
                }

            }
            operation.setMetaClassID(classID);
        }
        curMetaClass.setNamespaceID(namespaceID);
        curMetaClass.setNamespace(namespaceInfos.get(namespaceID).getName());
    }

    /**
     * Set the Class IDs of all included types to register recursively
     *
     * @param accountID
     *            ID of the account of the user that calls the operation
     * @param credentials
     *            Credentials of the account provided
     * @param metaClass
     *            Current class spec
     * @param classIDsSet
     *            IDs of the classes
     * @param propsIDsSet
     *            Map of PropertyIDs by className + PropertyName. Used to set
     *            accessed properties.
     * @param implsIDsSet
     *            Map of ImplementationIDs by className + opName. Used to set
     *            accessed implementations.
     * @param language
     *            Language of the classes to register
     * @throws RemoteException
     *             if some exception occurs
     */
    private void setIDs(final AccountID accountID, final PasswordCredential credentials, final MetaClass metaClass,
                        final Map<String, MetaClassID> classIDsSet, final Map<String, PropertyID> propsIDsSet,
                        final Map<String, ImplementationID> implsIDsSet, final Langs language) {

        // =================================== //
        // ==== GET ALREADY INSTALLED IDS ==== //
        // =================================== //

        // ================ GET IDS FOR CLASSES ASSOCIATED TO ATTRIBUTES =============
        // //
        for (final Property property : metaClass.getProperties()) {
            checkClassIsAlreadyRegisteredAndGetID(accountID, credentials, metaClass, classIDsSet, propsIDsSet,
                    implsIDsSet, property.getType(), language);

            // ================ PROPERTY INCLUDES TYPES ============= //
            if (property.getType().getIncludes() != null) {
                for (final Type subincludeType : property.getType().getIncludes()) {
                    if (subincludeType instanceof UserType) {
                        checkClassIsAlreadyRegisteredAndGetID(accountID, credentials, metaClass, classIDsSet,
                                propsIDsSet, implsIDsSet, subincludeType, language);
                    }
                }
            }

        }
        // ================ GET IDS FOR CLASSES ASSOCIATED TO METHODS ============= //
        for (final Operation operation : metaClass.getOperations()) {

            // ================ RETURN TYPE ============= //
            final Type returnType = operation.getReturnType();
            if (returnType instanceof UserType) {
                checkClassIsAlreadyRegisteredAndGetID(accountID, credentials, metaClass, classIDsSet, propsIDsSet,
                        implsIDsSet, returnType, language);
            }
            if (returnType.getIncludes() != null) {
                for (final Type subincludeType : returnType.getIncludes()) {
                    if (subincludeType instanceof UserType) {
                        checkClassIsAlreadyRegisteredAndGetID(accountID, credentials, metaClass, classIDsSet,
                                propsIDsSet, implsIDsSet, subincludeType, language);
                    }
                }
            }

            // ================ PARAMETER TYPES ============= //
            for (final Type paramType : operation.getParams().values()) {
                if (paramType instanceof UserType) {
                    checkClassIsAlreadyRegisteredAndGetID(accountID, credentials, metaClass, classIDsSet, propsIDsSet,
                            implsIDsSet, paramType, language);
                }
                if (paramType.getIncludes() != null) {
                    for (final Type subincludeType : paramType.getIncludes()) {
                        if (subincludeType instanceof UserType) {
                            checkClassIsAlreadyRegisteredAndGetID(accountID, credentials, metaClass, classIDsSet,
                                    propsIDsSet, implsIDsSet, subincludeType, language);
                        }
                    }
                }
            }

            for (final Implementation impl : operation.getImplementations()) {
                // ================ IMPLEMENTATION INCLUDES TYPES ============= //
                for (final Type includeType : impl.getIncludes()) {
                    if (includeType instanceof UserType) {
                        checkClassIsAlreadyRegisteredAndGetID(accountID, credentials, metaClass, classIDsSet,
                                propsIDsSet, implsIDsSet, includeType, language);
                    }
                    if (includeType.getIncludes() != null) {
                        for (final Type subincludeType : includeType.getIncludes()) {
                            if (subincludeType instanceof UserType) {
                                checkClassIsAlreadyRegisteredAndGetID(accountID, credentials, metaClass, classIDsSet,
                                        propsIDsSet, implsIDsSet, subincludeType, language);
                            }
                        }
                    }
                }

                // TODO: (LASTTODO) check accessed properties types
                for (final AccessedProperty accProperty : impl.getAccessedProperties()) {
                    final String typeName = accProperty.getClassName();
                    final String signature = Reflector.getSignatureFromTypeName(typeName);
                    final boolean isUserType = !Reflector.isJavaPrimitiveOrArraySignature(signature);
                    if (isUserType) {
                        final Type accType = new UserType(accProperty.getNamespace(), typeName, signature, signature,
                                null);
                        checkClassIsAlreadyRegisteredAndGetID(accountID, credentials, metaClass, classIDsSet,
                                propsIDsSet, implsIDsSet, accType, language);
                    }
                }

                for (final AccessedImplementation accImpl : impl.getAccessedImplementations()) {
                    final String typeName = accImpl.getClassName();
                    final String signature = Reflector.getSignatureFromTypeName(typeName);
                    final boolean isUserType = !Reflector.isJavaPrimitiveOrArraySignature(signature);
                    if (isUserType) {
                        final Type accType = new UserType(accImpl.getNamespace(), typeName, signature, signature, null);
                        // JavaConstantSignature sig = new JavaConstantSignature(signature);
                        // JavaTypeInfo jTypeInfo = new JavaTypeInfo(sig);
                        // accType.addLanguageDepInfo(jTypeInfo);
                        checkClassIsAlreadyRegisteredAndGetID(accountID, credentials, metaClass, classIDsSet,
                                propsIDsSet, implsIDsSet, accType, language);
                    }
                }

                // ==== PREFETCHING INFO === //
                if (impl.getPrefetchingInfo() != null && impl.getPrefetchingInfo().getInjectPrefetchingCall()) {
                    final String typeName = impl.getClassName();
                    final String signature = Reflector.getSignatureFromTypeName(typeName);
                    final Type accType = new UserType(impl.getNamespace(), typeName, signature, signature, null);

                    checkClassIsAlreadyRegisteredAndGetID(accountID, credentials, metaClass, classIDsSet, propsIDsSet,
                            implsIDsSet, accType, language);

                }

            }
        }

    }

    /**
     * Recursively get the IDs of an already registered class (operationIDs,
     * propertyIDs, ...)
     *
     * @param accountID
     *            ID of the account registering the class that imports this class
     * @param credentials
     *            Credentials fo the account
     * @param namespaceID
     *            ID of the namespace in which the class is being registered
     * @param metaClass
     *            s of the class to register
     * @param classIDsSet
     *            Structure of imported class ids
     * @param propsIDsSet
     *            IDs of properties
     * @param implsIDsSet
     *            IDs of implementations
     * @param classID
     *            ID of the already registered class
     * @throws RemoteException
     *             if some exception occurs
     */
    private void getIDsOfAlreadyRegisteredClass(final AccountID accountID, final PasswordCredential credentials,
                                                final NamespaceID namespaceID, final MetaClass metaClass, final Map<String, MetaClassID> classIDsSet,
                                                final Map<String, PropertyID> propsIDsSet, final Map<String, ImplementationID> implsIDsSet,
                                                final MetaClassID classID) {
        // If already processed
        if (classIDsSet.containsValue(classID)) {
            return;
        }
        // Get PropertyIDs and ImplementationIDs
        final MetaClass classInfo = classMgrApi.getClassInfo(classID);
        final String className = classInfo.getName();

        // Modify the ID
        classIDsSet.put(classInfo.getNamespace() + "$" + className, classID);

        // Get ImplementationIDs
        for (final Operation opInfo : classInfo.getOperations()) {
            for (final Implementation implInfo : opInfo.getImplementations()) {
                implsIDsSet.put(
                        implInfo.getNamespace() + "$" + implInfo.getClassName() + "$"
                                + implInfo.getOpNameAndDescriptor() + "$" + implInfo.getPosition(),
                        implInfo.getDataClayID());
            }
        }
        // Get PropertyIDs
        for (final Property propInfo : classInfo.getProperties()) {
            propsIDsSet.put(propInfo.getNamespace() + "$" + propInfo.getClassName() + "$" + propInfo.getName(),
                    propInfo.getDataClayID());

            final MetaClassID propclassID = classMgrApi.getClassIDFromProperty(propInfo.getDataClayID());
            // If classID is null it is not a user type property
            if (propclassID != null) {
                // If not already checked
                getIDsOfAlreadyRegisteredClass(accountID, credentials, namespaceID, metaClass, classIDsSet, propsIDsSet,
                        implsIDsSet, propclassID);

            }
        }
    }

    /**
     * Check if specification provided is an already registered class and in case it
     * is, get the IDs (operationIDs, propertyIDs, ...)
     *
     * @param accountID
     *            ID of the account registering the class that imports this class
     * @param credentials
     *            Credentials fo the account
     * @param metaClass
     *            s of the class to register
     * @param classIDsSet
     *            Structure of imported class ids
     * @param propsIDsSet
     *            IDs of properties
     * @param implsIDsSet
     *            IDs of implementations
     * @param type
     *            ification of the already registered class
     * @param language
     *            Language of the new classes
     * @throws RemoteException
     *             if some exception occurs
     */
    private void checkClassIsAlreadyRegisteredAndGetID(final AccountID accountID, final PasswordCredential credentials,
                                                       final MetaClass metaClass, final Map<String, MetaClassID> classIDsSet,
                                                       final Map<String, PropertyID> propsIDsSet, final Map<String, ImplementationID> implsIDsSet, final Type type,
                                                       final Langs language) {
        // If type specification already contains Class ID, do not check.
        if (type instanceof UserType) {
            final UserType uType = (UserType) type;
            if (uType.getClassID() != null) {
                return;
            }
            final boolean typeExists = checkTypeIsRegistered(accountID, credentials, uType);
            // CHECKSTYLE:ON
            if (typeExists) {
                // GET IDS OF THE CLASS (in case it is accessed)
                if (type instanceof UserType) {
                    final NamespaceID namespaceID = namespaceMgrApi.getNamespaceID(uType.getNamespace());
                    MetaClassID classID = classMgrApi.getMetaClassID(namespaceID, uType.getTypeName());
                    if (classID == null) {
                        // if class manager cannot obtain the class id in namespaceID check imports
                        final Set<ImportedInterface> importedInterfaces = namespaceMgrApi
                                .getImportedInterfaces(namespaceID, uType.getTypeName());
                        classID = importedInterfaces.iterator().next().getClassOfImportID();
                    }

                    getIDsOfAlreadyRegisteredClass(accountID, credentials, namespaceID, metaClass, classIDsSet,
                            propsIDsSet, implsIDsSet, classID);
                }
            } else {
                LOGGER.trace("Class {} is not registered", type.getTypeName());
                return;
            }
        }
    }

    /**
     * Method that updates the dependencies of types with the corresponding classIDs
     *
     * @param metaClass
     *            the spec to be updated
     * @param newClassIDs
     *            the IDs to be used
     * @param propsIDsSet
     *            Map of PropertyIDs by className + PropertyName. Used to set
     *            accessed properties.
     * @param implsIDsSet
     *            Map of ImplementationIDs by className + opName. Used to set
     *            accessed implementations.
     * @param language
     *            Language of the class
     */
    private void updateDependencies(final MetaClass metaClass, final Map<String, MetaClassID> newClassIDs,
                                    final Map<String, PropertyID> propsIDsSet, final Map<String, ImplementationID> implsIDsSet,
                                    final Langs language) {

        // Update properties
        for (final Property property : metaClass.getProperties()) {
            final Type propertyType = property.getType();
            if (propertyType instanceof UserType) {
                final UserType uType = (UserType) propertyType;
                if (uType.getClassID() == null) {
                    final String typename = uType.getTypeName();
                    final String typenamespace = uType.getNamespace();
                    if (newClassIDs.get(typenamespace + "$" + typename) == null) {
                        LOGGER.error("Unable to find property type {}${} in class ids structure", typenamespace,
                                typename);
                    }
                    uType.setClassID(newClassIDs.get(typenamespace + "$" + typename));
                }
            }

            if (propertyType.getIncludes() != null) {
                for (final Type includeType : propertyType.getIncludes()) {
                    if (includeType instanceof UserType) {
                        final UserType uType = (UserType) includeType;
                        if (uType.getClassID() == null) {
                            final String typename = uType.getTypeName();
                            final String typenamespace = uType.getNamespace();
                            if (newClassIDs.get(typenamespace + "$" + typename) == null) {
                                LOGGER.error("Unable to find property includes type {}${} in class ids structure",
                                        typenamespace, typename);
                            }
                            uType.setClassID(newClassIDs.get(typenamespace + "$" + typename));
                        }
                    }
                }
            }
        }

        // Update operations
        for (final Operation operation : metaClass.getOperations()) {
            final Type returnType = operation.getReturnType();
            if (returnType instanceof UserType) {
                final UserType uType = (UserType) returnType;

                if (uType.getClassID() == null) {
                    final String typename = uType.getTypeName();
                    final String typenamespace = uType.getNamespace();
                    if (newClassIDs.get(typenamespace + "$" + typename) == null) {
                        LOGGER.error("Unable to find operation return type {}${} in class ids structure", typenamespace,
                                typename);
                    }
                    uType.setClassID(newClassIDs.get(typenamespace + "$" + typename));
                }
            }
            if (returnType.getIncludes() != null) {
                for (final Type includeType : returnType.getIncludes()) {
                    if (includeType instanceof UserType) {
                        final UserType uType = (UserType) includeType;
                        if (uType.getClassID() == null) {
                            final String typename = uType.getTypeName();
                            final String typenamespace = uType.getNamespace();
                            if (newClassIDs.get(typenamespace + "$" + typename) == null) {
                                LOGGER.error(
                                        "Unable to found operation return include type {}${} in class ids structure",
                                        typenamespace, typename);
                            }
                            uType.setClassID(newClassIDs.get(typenamespace + "$" + typename));
                        }
                    }
                }
            }

            for (final Type paramType : operation.getParams().values()) {
                if (paramType instanceof UserType) {
                    final UserType uType = (UserType) paramType;
                    if (uType.getClassID() == null) {
                        final String typename = uType.getTypeName();
                        final String typenamespace = uType.getNamespace();
                        if (newClassIDs.get(typenamespace + "$" + typename) == null) {
                            LOGGER.error("Unable to found operation param type {}${} in class ids structure",
                                    typenamespace, typename);
                        }
                        uType.setClassID(newClassIDs.get(typenamespace + "$" + typename));
                    }
                }
                if (paramType.getIncludes() != null) {
                    for (final Type includeType : paramType.getIncludes()) {
                        if (includeType instanceof UserType) {
                            final UserType uType = (UserType) includeType;
                            if (uType.getClassID() == null) {
                                final String typename = uType.getTypeName();
                                final String typenamespace = uType.getNamespace();
                                if (newClassIDs.get(typenamespace + "$" + typename) == null) {
                                    LOGGER.error(
                                            "Unable to found operation param include type {}${} in class ids structure",
                                            typenamespace, typename);
                                }
                                uType.setClassID(newClassIDs.get(typenamespace + "$" + typename));
                            }
                        }
                    }
                }
            }

            // Update implementations of current operation
            for (final Implementation impl : operation.getImplementations()) {
                for (final Type includeType : impl.getIncludes()) {
                    if (includeType instanceof UserType) {
                        final UserType uType = (UserType) includeType;
                        if (uType.getClassID() == null) {
                            final String typename = uType.getTypeName();
                            final String typenamespace = uType.getNamespace();
                            if (newClassIDs.get(typenamespace + "$" + typename) == null) {
                                LOGGER.error("Unable to found implementation include type {}${} in class ids structure",
                                        typenamespace, typename);
                            }
                            uType.setClassID(newClassIDs.get(typenamespace + "$" + typename));
                        }
                    }
                    if (includeType.getIncludes() != null) {
                        for (final Type subincludeType : includeType.getIncludes()) {
                            if (subincludeType instanceof UserType) {
                                final UserType uType = (UserType) subincludeType;
                                if (uType.getClassID() == null) {
                                    final String typename = uType.getTypeName();
                                    final String typenamespace = uType.getNamespace();
                                    if (newClassIDs.get(typenamespace + "$" + typename) == null) {
                                        LOGGER.error(
                                                "Unable to found implementation subinclude type {}${} in class ids structure",
                                                typenamespace, typename);
                                    }
                                    uType.setClassID(newClassIDs.get(typenamespace + "$" + typename));
                                }
                            }
                        }
                    }
                }

                // Update accessed properties
                for (final AccessedProperty accProperty : impl.getAccessedProperties()) {
                    if (accProperty.getPropertyID() == null && accProperty.getPropertyID() == null) {
                        final String propClassname = accProperty.getClassName();
                        final String propNamespace = accProperty.getNamespace();
                        final String propName = accProperty.getName();

                        final PropertyID accPropertyID = propsIDsSet
                                .get(propNamespace + "$" + propClassname + "$" + propName);

                        if (accPropertyID == null) {
                            throw new DataClayRuntimeException(ERRORCODE.PROPERTY_NOT_EXIST,
                                    "Property " + accProperty.getName() + " does not exist in registered class "
                                            + accProperty.getClassName() + ". You might be registering a class that "
                                            + " imports another class which is already registered. ",
                                    true);

                        }
                        accProperty.setPropertyID(accPropertyID);
                    }
                }

                // Update accessed implementations
                for (final AccessedImplementation accImpl : impl.getAccessedImplementations()) {
                    if (accImpl.getImplementationID() == null && accImpl.getImplementationID() == null) {
                        final String implClassname = accImpl.getClassName();
                        final String implNamespace = accImpl.getNamespace();
                        final String implOpSignature = accImpl.getOpSignature();
                        final int implementationPos = accImpl.getImplPosition();
                        final ImplementationID accImplID = implsIDsSet.get(
                                implNamespace + "$" + implClassname + "$" + implOpSignature + "$" + implementationPos);
                        if (accImplID == null) {
                            throw new DataClayRuntimeException(ERRORCODE.OPERATION_NOT_EXIST,
                                    "Operation " + accImpl.getOpSignature() + " does not exist in registered class "
                                            + accImpl.getClassName() + ". You might be registering a class that "
                                            + " imports another class which is already registered. ",
                                    true);
                        }
                        accImpl.setImplementationID(accImplID);
                    }
                }

                // Update prefetching info
                if (impl.getPrefetchingInfo() != null && impl.getPrefetchingInfo().getInjectPrefetchingCall()) {
                    final PrefetchingInformation pfInfo = impl.getPrefetchingInfo();
                    final String implClassname = pfInfo.getPrefetchingClassName();
                    final String implNamespace = pfInfo.getPrefetchingNameSpace();
                    final String implOpSignature = pfInfo.getPrefetchingMethodSignature();
                    final int implementationPos = 0;
                    final ImplementationID accImplID = implsIDsSet
                            .get(implNamespace + "$" + implClassname + "$" + implOpSignature + "$" + implementationPos);
                    if (accImplID == null) {
                        throw new DataClayRuntimeException(ERRORCODE.OPERATION_NOT_EXIST,
                                "Operation " + pfInfo.getPrefetchingMethodSignature()
                                        + " does not exist in registered class " + pfInfo.getPrefetchingClassName()
                                        + ". You might be registering a class that "
                                        + " imports another class which is already registered. ",
                                true);
                    }
                    pfInfo.setPrefetchingImplementationID(accImplID);
                    pfInfo.setPrefetchingClassID(newClassIDs.get(implNamespace + "$" + implClassname));
                }

            }
        }

        if (metaClass.getJavaClassInfo() != null) {
            for (final Type ifaceInclude : metaClass.getJavaClassInfo().getIncludes()) {
                if (ifaceInclude instanceof UserType) {
                    final UserType uType = (UserType) ifaceInclude;
                    if (uType.getClassID() == null) {
                        final String typename = uType.getTypeName();
                        final String typenamespace = uType.getNamespace();
                        if (newClassIDs.get(typenamespace + "$" + typename) == null) {
                            LOGGER.error(
                                    "Unable to found implementation subinclude type {}${} in class ids structure",
                                    typenamespace, typename);
                        }
                        uType.setClassID(newClassIDs.get(typenamespace + "$" + typename));
                    }
                }
            }
        }


        // Update parent
        final UserType parentType = metaClass.getParentType();
        if (parentType != null) {
            // We get the name without parsing since parent are not
            // signature
            final MetaClassID m = newClassIDs.get(parentType.getNamespace() + "$" + parentType.getTypeName());
            parentType.setClassID(m);
        }
    }

    /**
     * Creates a new metaclass in the system with the provided specifications and
     * associates it to the Namespace provided.
     *
     * @param accountID
     *            ID of the account of the user that calls the operation
     * @param credential
     *            Credential of the account provided
     * @param namespaceID
     *            id of the namespace in which to create the metaclass
     * @param namespaceInfo
     *            Information of namespace
     * @param newClass
     *            New class specifications
     * @param language
     *            Language of the new class
     * @return info of the new MetaClass if it was successfully created.
     */
    private MetaClass newClassInternal(final AccountID accountID, final PasswordCredential credential,
                                       final NamespaceID namespaceID, final MetaClass newClass, final Namespace namespaceInfo,
                                       final Langs language) {

        // Check language of namespace
        if (namespaceInfo.getLanguage() != language) {
            throw new DataClayRuntimeException(ERRORCODE.INCOMPATIBLE_LANGUAGE_FOR_CLASS_WITH_NAMESPACE);
        }

        // Register class in class manager
        final MetaClass newClassInfo = classMgrApi.newClass(accountID, namespaceID, namespaceInfo.getName(), newClass,
                language);
        return newClassInfo;

    }

    @Override
    public void removeClass(final AccountID accountID, final PasswordCredential credential,
                            final NamespaceID namespaceID, final String className) {
        // Check namespace responsible
        checkNamespaceResponsible(accountID, credential, namespaceID);

        Set<ImportedInterface> importedInterfaces = null;
        // Check the class is not in use and get the classID (checks the class belongs
        // to the namespace)
        MetaClassID classID = classMgrApi.checkClassNotInIncludesAndGetID(className, namespaceID);
        if (classID == null) {
            // if class manager cannot obtain the class id in namespaceID look for imports
            // and check they are not used
            importedInterfaces = namespaceMgrApi.checkImportsOfClassAreNotUsedAndGet(namespaceID, className);
            if (importedInterfaces.isEmpty()) {
                throw new DataClayRuntimeException(ERRORCODE.CLASS_NOT_CREATED_IN_NAMESPACE_NOR_IMPORTED);
            }
            classID = importedInterfaces.iterator().next().getClassOfImportID();
        }

        // Check there is no interface of the class related with a contract
        final Set<InterfaceID> interfacesIDs = interfaceMgrApi.getInterfacesOfClass(namespaceID, classID);
        for (final InterfaceID interfaceID : interfacesIDs) {
            if (!contractMgrApi.checkInterfaceHasNoContracts(interfaceID)) {
                throw new DataClayRuntimeException(ERRORCODE.INTERFACE_WITH_CONTRACTS);
            }
        }

        // Remove the interfaces of the class
        for (final InterfaceID interfaceID : interfacesIDs) {
            interfaceMgrApi.removeInterface(namespaceID, interfaceID);
        }

        if (importedInterfaces == null) {
            // Remove the class
            classMgrApi.removeClass(classID);
        } else {
            // Remote imports
            namespaceMgrApi.removeImportedInterfaces(namespaceID, importedInterfaces);
        }

        // Unregister associations with the imports it used if any
        final HashSet<MetaClassID> metaClassesIDs = new HashSet<>();
        metaClassesIDs.add(classID);
        namespaceMgrApi.unregisterSubClassesFromUsingAnyImportedClassInNamespace(namespaceID, metaClassesIDs);

    }

    @Override
    public void removeOperation(final AccountID accountID, final PasswordCredential credential,
                                final NamespaceID namespaceID, final String className, final String operationSignature) {
        // Check namespace responsible
        checkNamespaceResponsible(accountID, credential, namespaceID);

        // Get metaClassID either because it is created in namespace or imported in it
        final MetaClassID classID = getMetaClassID(namespaceID, className);

        // Get the operationID (checks the operation belongs to the class)
        final OperationID operationID = classMgrApi.getOperationID(classID, operationSignature);

        // Check there is no implementation accessing the operation
        if (classMgrApi.existsImplementationAccessingOperation(operationID)) {
            throw new DataClayRuntimeException(ERRORCODE.SOME_IMPLEMENTATIONS_ACCESS_OPERATION);
        }

        // Check there is no interface related with the operation
        final Set<InterfaceID> interfacesIDs = interfaceMgrApi.getInterfacesAccessingOperation(namespaceID, classID,
                operationID);
        if (!interfacesIDs.isEmpty()) {
            throw new DataClayRuntimeException(ERRORCODE.OPERATION_ALREADY_IN_INTERFACE);
        }

        // Remove the operation
        final Operation opInfo = classMgrApi.removeOperation(operationID);

        // Unregister associations with the imports it used
        // TODO Do this in a single op (24 Jul 2013 jmarti)
        final HashSet<OperationID> operationsIDs = new HashSet<>();
        operationsIDs.add(operationID);
        namespaceMgrApi.unregisterOperationsFromUsingAnyImportedClassInNamespace(namespaceID, operationsIDs);

        final HashSet<ImplementationID> implIDs = new HashSet<>();
        for (final Implementation impl : opInfo.getImplementations()) {
            implIDs.add(impl.getDataClayID());
        }
        namespaceMgrApi.unregisterImplementationsFromUsingAnyImportedClassInNamespace(namespaceID, implIDs);

    }

    @Override
    public void removeImplementation(final AccountID accountID, final PasswordCredential credential,
                                     final NamespaceID namespaceID, final ImplementationID implementationID) {
        // Check namespace responsible
        checkNamespaceResponsible(accountID, credential, namespaceID);

        // Check the method is not related with contracts
        if (!contractMgrApi.checkImplementationHasNoContracts(implementationID)) {
            throw new DataClayRuntimeException(ERRORCODE.IMPL_ACCESSIBLE_FROM_CONTRACTS);
        }

        // Remove the method
        classMgrApi.removeImplementation(implementationID);

        // Unregister associations with the imports it used
        final HashSet<ImplementationID> implementationsIDs = new HashSet<>();
        implementationsIDs.add(implementationID);
        namespaceMgrApi.unregisterImplementationsFromUsingAnyImportedClassInNamespace(namespaceID, implementationsIDs);

    }

    @Override
    public OperationID getOperationID(final AccountID accountID, final PasswordCredential credential,
                                      final NamespaceID namespaceID, final String className, final String operationSignature) {
        // Check namespace responsible
        checkNamespaceResponsible(accountID, credential, namespaceID);

        // Get metaClassID either because it is created in namespace or imported in it
        final MetaClassID classID = getMetaClassID(namespaceID, className);

        // Get the operationID (checks the operation belongs to the class)
        return classMgrApi.getOperationID(classID, operationSignature);

    }

    @Override
    public PropertyID getPropertyID(final AccountID accountID, final PasswordCredential credential,
                                    final NamespaceID namespaceID, final String className, final String propertyName) {
        // Check namespace responsible
        checkNamespaceResponsible(accountID, credential, namespaceID);

        // Get metaClassID either because it is created in namespace or imported in it
        final MetaClassID classID = getMetaClassID(namespaceID, className);

        // Get the propertyID (checks the property belongs to the class)
        return classMgrApi.getPropertyID(classID, propertyName);

    }

    @Override
    public MetaClassID getClassID(final AccountID accountID, final PasswordCredential credential,
                                  final NamespaceID namespaceID, final String className) {
        // Check namespace responsible
        checkNamespaceResponsible(accountID, credential, namespaceID);

        // Get the classID (checks the class belongs to the namespace)
        final MetaClassID classID = classMgrApi.getMetaClassID(namespaceID, className);
        if (classID == null) {
            throw new DataClayRuntimeException(ERRORCODE.CLASS_NOT_EXIST);
        } else {
            return classID;
        }

    }

    @Override
    public MetaClass getClassInfo(final AccountID accountID, final PasswordCredential credential,
                                  final NamespaceID namespaceID, final String className) {
        final MetaClassID classID = classMgrApi.getMetaClassID(namespaceID, className);
        if (classID == null) {
            throw new DataClayRuntimeException(ERRORCODE.CLASS_NOT_EXIST,
                    "Class " + className + " (at namespace: {" + namespaceID.toString() + "})", false);
        } else {
            return classMgrApi.getClassInfo(classID);
        }
    }

    @Override
    public Map<MetaClassID, MetaClass> getInfoOfClassesInNamespace(final AccountID accountID,
                                                                   final PasswordCredential credential, final NamespaceID namespaceID) {
        try {
            // Check namespace responsible
            checkNamespaceResponsible(accountID, credential, namespaceID);

            return classMgrApi.getInfoOfClassesInNamespace(namespaceID);
        } catch (final Exception ex) {
            LOGGER.debug("getInfoOfClassesInNamespace error", ex);
            throw ex;
        }

    }


    /**
     * Method that check if type with signature provided exists and set Class ID if
     * needed.
     *
     * @param accountID
     *            the account ID just in case it is an import and we need to check
     *            if it is registered to any of the corresponding contracts
     * @param credential
     *            the credential of the account
     * @param userType
     *            Type to check
     * @return TRUE if type is already registered in DataClay.
     */
    private boolean checkTypeIsRegistered(final AccountID accountID, final PasswordCredential credential,
                                          final UserType userType) {

        final String typeName = userType.getTypeName();
        final String namespace = userType.getNamespace();
        final NamespaceID namespaceID = namespaceMgrApi.getNamespaceID(namespace);
        // Get the metaClassID (checks the class belongs to the namespace)
        final MetaClassID metaClassID = classMgrApi.getMetaClassID(namespaceID, typeName);
        if (metaClassID != null) {
            return true;
        } else {
            // if class manager cannot obtain the class id in namespaceID check imports
            final Set<ImportedInterface> importedInterfaces = namespaceMgrApi.getImportedInterfaces(namespaceID,
                    typeName);
            if (!importedInterfaces.isEmpty()) {
                return true;
            }
        }
        return false;

    }

    // ============== Contract Manager ==============//

    // TODO ROLLBACK IF IT FAILS (2 Jul 2013 jmarti)
    @Override
    public ContractID newContract(final AccountID accountID, final PasswordCredential credential,
                                  final Contract newContract) {

        try {
            // =========== CHECK AND FILL WITH IDS =========== //
            // Set IDs of accounts and namespaces (check it is properly specified)
            // Namespace
            final String namespace = newContract.getNamespace();
            if (namespace == null || namespace.isEmpty()) {
                throw new DataClayRuntimeException(ERRORCODE.NULL_OR_EMPTY_PARAMETER_EXCEPTION,
                        "Contract specification: Namespace cannot null or empty", false);
            }
            final NamespaceID namespaceID = namespaceMgrApi.getNamespaceID(namespace);
            newContract.setNamespaceID(namespaceID);
            newContract.setProviderAccountID(accountID);

            // Provider account ID
            if (newContract.getProviderAccountID() == null) {
                throw new DataClayRuntimeException(ERRORCODE.NULL_OR_EMPTY_PARAMETER_EXCEPTION,
                        "Contract specification: Provider account id cannot be null or empty", false);
            }

            // Interfaces in contract
            final Map<InterfaceID, InterfaceInContract> ifacesWithIDs = new HashMap<>();
            final List<InterfaceInContract> ifacesInContract = newContract.getInterfacesInContractSpecs();
            for (final InterfaceInContract ifaceInContract : ifacesInContract) {
                // Get interface IDs

                final Interface ifaceSpec = ifaceInContract.getIface();

                /**** rtouma ****/
                // Set provider account name if null
                if (ifaceSpec.getProviderAccountName() == null || ifaceSpec.getProviderAccountName().isEmpty()) {
                    final AccountID providerID = ifaceSpec.getProviderAccountID();
                    if (providerID == null) {
                        throw new DataClayRuntimeException(ERRORCODE.NULL_OR_EMPTY_PARAMETER_EXCEPTION,
                                "Contract specification: Provider account ID cannot be null or empty", false);
                    }
                    final String providerName = accountMgrApi.getAccount(providerID).getUsername();
                    ifaceSpec.setProviderAccountName(providerName);
                }
                /****************/

                final InterfaceID ifaceID = interfaceMgrApi.getInterfaceID(ifaceSpec.getProviderAccountName(),
                        ifaceSpec.getNamespace(), ifaceSpec.getClassName(), ifaceSpec.getPropertiesInIface(),
                        ifaceSpec.getOperationsSignatureInIface());
                final Interface iface = interfaceMgrApi.getInterfaceInfo(ifaceID);
                ifaceInContract.setInterfaceID(iface.getDataClayID());

                // Set actual iface
                ifaceInContract.setIface(iface);
                final MetaClassID classID = iface.getMetaClassID();

                // OpImplementations
                final Map<OperationID, OpImplementations> opImpls = new HashMap<>();
                final Set<OpImplementations> implSpecOps = ifaceInContract.getImplementationsSpecPerOperation();
                for (final OpImplementations opImpl : implSpecOps) {
                    final String opSignature = opImpl.getOperationSignature();
                    final int numLocalImpl = opImpl.getNumLocalImpl();
                    final int numRemoteImpl = opImpl.getNumRemoteImpl();

                    final OperationID opID = classMgrApi.getOperationID(classID, opSignature);
                    final Operation operation = classMgrApi.getOperationInfo(opID);
                    int curImpl = 0;
                    for (final Implementation impl : operation.getImplementations()) {
                        if (curImpl == numLocalImpl) {
                            opImpl.setLocalImplementationID(impl.getDataClayID());
                        }
                        if (curImpl == numRemoteImpl) {
                            opImpl.setRemoteImplementationID(impl.getDataClayID());
                        }
                        if (opImpl.getLocalImplementationID() != null && opImpl.getRemoteImplementationID() != null) {
                            break;
                        }
                        curImpl++;
                    }
                    opImpls.put(opID, opImpl);

                }
                ifaceInContract.setAccessibleImplementations(opImpls);
                ifacesWithIDs.put(ifaceID, ifaceInContract);
            }
            newContract.setInterfacesInContract(ifacesWithIDs);
            // =========== =========== =========== =========== //

            // Validate proprietary and check it is the responsible of the namespace
            final Namespace namespaceInfo = checkNamespaceResponsible(accountID, credential,
                    newContract.getNamespaceID());

            // Update contract with the implementations of the default getters and setters
            if (namespaceInfo.getLanguage() == Langs.LANG_JAVA) { // Getters/setters do not apply to Python codes
                // (jcosta
                // 17/jun/2014)
                final Map<InterfaceID, Interface> infoOfInterfaces = interfaceMgrApi
                        .getInterfacesInfo(new HashSet<>(newContract.getInterfacesInContract().keySet()));
                final Map<MetaClassID, Set<PropertyID>> propertiesInInterfaces = new HashMap<>();
                for (final Interface ifaceInfo : infoOfInterfaces.values()) {
                    final MetaClassID curClassID = ifaceInfo.getMetaClassID();
                    final Set<PropertyID> curPropsIDs = ifaceInfo.getPropertiesIDs();
                    propertiesInInterfaces.put(curClassID, curPropsIDs);
                }
                final Map<PropertyID, Map<OperationID, ImplementationID>> implsOfGettersSetters = classMgrApi
                        .getImplementationsOfGettersAndSetters(propertiesInInterfaces);
                for (final Entry<InterfaceID, InterfaceInContract> ifaceInContract : newContract
                        .getInterfacesInContract().entrySet()) {
                    final InterfaceID curIfaceID = ifaceInContract.getKey();
                    final Interface ifaceInfo = infoOfInterfaces.get(curIfaceID);
                    final InterfaceInContract curIfaceInContract = ifaceInContract.getValue();
                    for (final PropertyID propertyInInterface : ifaceInfo.getPropertiesIDs()) {
                        final Map<OperationID, ImplementationID> implsOfGetterSetter = implsOfGettersSetters
                                .get(propertyInInterface);
                        for (final Entry<OperationID, ImplementationID> curImplOfGetterSetter : implsOfGetterSetter
                                .entrySet()) {
                            final OperationID opID = curImplOfGetterSetter.getKey();
                            final Operation op = classMgrApi.getOperationInfo(opID);
                            final OpImplementations opImplementationGetterSetter = new OpImplementations(
                                    op.getNameAndDescriptor(), 0, 0);
                            final ImplementationID implID = op.getImplementations().get(0).getDataClayID();
                            opImplementationGetterSetter.setLocalImplementationID(implID);
                            opImplementationGetterSetter.setRemoteImplementationID(implID);
                            curIfaceInContract.getAccessibleImplementations().put(curImplOfGetterSetter.getKey(),
                                    opImplementationGetterSetter);
                        }
                    }
                }
            }

            // Check contract
            final Map<InterfaceID, Interface> infoOfInterfacesInContract = checkContract(newContract);

            // Generate interfaces that must be automatically added in the contract
            final Map<InterfaceID, InterfaceInContract> interfacesInContract = updateInterfacesForTheContract(accountID,
                    credential, newContract.getNamespaceID(), newContract.getInterfacesInContract(),
                    infoOfInterfacesInContract);

            newContract.setInterfacesInContract(interfacesInContract);

            // Create the contract
            ContractID newContractID = new ContractID();
            newContract.setDataClayID(newContractID);

            if (!newContract.isPublicAvailable()) {
                newContractID = contractMgrApi.newPrivateContract(newContract);
            } else {
                newContractID = contractMgrApi.newPublicContract(newContract);
            }

            return newContractID;

        } catch (final ParseException e) {
            throw new DataClayRuntimeException(ERRORCODE.BAD_DATES);
        }
    }

    @Override
    public void registerToPublicContract(final AccountID accountID, final PasswordCredential credential,
                                         final ContractID contractID) {
        // Validate proprietary account
        if (!accountMgrApi.validateAccount(accountID, credential, AccountRole.NORMAL_ROLE)) {
            throw new DataClayRuntimeException(ERRORCODE.INVALID_CREDENTIALS);
        }

        contractMgrApi.registerToPublicContract(accountID, contractID);
    }

    @Override
    public ContractID registerToPublicContractOfNamespace(final AccountID accountID,
                                                          final PasswordCredential credential, final NamespaceID namespaceID) {
        // Validate proprietary account
        if (!accountMgrApi.validateAccount(accountID, credential, AccountRole.NORMAL_ROLE)) {
            throw new DataClayRuntimeException(ERRORCODE.INVALID_CREDENTIALS);
        }

        final Map<ContractID, Contract> pubContracts = contractMgrApi.getPublicContractIDsOfProvider(namespaceID);
        if (pubContracts.size() == 0) {
            throw new DataClayRuntimeException(ERRORCODE.CONTRACT_NOT_EXIST);
        }
        final ContractID pubContractID = pubContracts.keySet().iterator().next();
        contractMgrApi.registerToPublicContract(accountID, pubContractID);
        return pubContractID;
    }

    @Override
    public Map<ContractID, Contract> getContractIDsOfProvider(final AccountID accountID,
                                                              final PasswordCredential credential, final NamespaceID namespaceIDofProvider) {
        // Validate account and check it is the responsible of the namespace
        checkNamespaceResponsible(accountID, credential, namespaceIDofProvider);

        return contractMgrApi.getContractIDsOfProvider(namespaceIDofProvider);
    }

    @Override
    public Map<ContractID, Contract> getContractIDsOfApplicant(final AccountID applicantAccountID,
                                                               final PasswordCredential credential) {
        // Validate account
        if (!accountMgrApi.validateAccount(applicantAccountID, credential, AccountRole.NORMAL_ROLE)) {
            throw new DataClayRuntimeException(ERRORCODE.INVALID_CREDENTIALS);
        }

        return contractMgrApi.getContractIDsOfApplicant(applicantAccountID);

    }

    @Override
    public Map<ContractID, Contract> getContractIDsOfApplicantWithProvider(final AccountID applicantAccountID,
                                                                           final PasswordCredential credential, final NamespaceID namespaceIDofProvider) {
        // Validate account
        if (!accountMgrApi.validateAccount(applicantAccountID, credential, AccountRole.NORMAL_ROLE)) {
            throw new DataClayRuntimeException(ERRORCODE.INVALID_CREDENTIALS);
        }

        return contractMgrApi.getContractIDsOfApplicantWithProvider(applicantAccountID, namespaceIDofProvider);
    }

    // ============== DataContract Manager ==============//

    // TODO ROLLBACK IF IT FAILS (2 Jul 2013 jmarti)
    @Override
    public DataContractID newDataContract(final AccountID accountID, final PasswordCredential credential,
                                          final DataContract newDataContract) {
        try {

            // ================================================ //
            if (Configuration.Flags.NOTIFICATION_MANAGER_ACTIVE.getBooleanValue()) {
                this.notificationMgrApi.removeSessionInCache(accountID);
            }

            final DataSetID datasetID = newDataContract.getProviderDataSetID();

            // Validate proprietary and check it is the responsible of the namespace
            checkDataSetResponsible(accountID, credential, datasetID);

            // Check applicants exist (if any)
            if (newDataContract.isPublicAvailable()) {
                for (final AccountID applicantAccountID : newDataContract.getApplicantsAccountsIDs()) {
                    if (applicantAccountID != null) {
                        if (!accountMgrApi.existsAccount(applicantAccountID)) {
                            throw new DataClayRuntimeException(ERRORCODE.ACCOUNT_NOT_EXIST);
                        }
                    }
                }
            }

            // Create the contract
            DataContractID newDataContractID = new DataContractID();
            newDataContract.setDataClayID(newDataContractID);
            if (newDataContract.isPublicAvailable()) {
                newDataContractID = datacontractMgrApi.newPublicDataContract(newDataContract);
            } else {
                newDataContractID = datacontractMgrApi.newPrivateDataContract(newDataContract);
            }

            LOGGER.info("[LOGICMODULE] Created new datacontract called" + " with ID " + newDataContractID);

            return newDataContractID;

        } catch (final ParseException e) {
            throw new DataClayRuntimeException(ERRORCODE.BAD_DATES);
        }
    }

    @Override
    public void registerToPublicDataContract(final AccountID accountID, final PasswordCredential credential,
                                             final DataContractID datacontractID) {
        // Validate proprietary account
        if (!accountMgrApi.validateAccount(accountID, credential, AccountRole.NORMAL_ROLE)) {
            throw new DataClayRuntimeException(ERRORCODE.INVALID_CREDENTIALS);
        }

        datacontractMgrApi.registerToPublicDataContract(accountID, datacontractID);

    }

    @Override
    public Map<DataContractID, DataContract> getDataContractIDsOfProvider(final AccountID accountID,
                                                                          final PasswordCredential credential, final DataSetID datasetIDofProvider) {
        // Validate account and check it is the responsible of the namespace
        checkDataSetResponsible(accountID, credential, datasetIDofProvider);

        return datacontractMgrApi.getDataContractIDsOfProvider(datasetIDofProvider);

    }

    @Override
    public Map<DataContractID, DataContract> getDataContractIDsOfApplicant(final AccountID applicantAccountID,
                                                                           final PasswordCredential credential) {
        // Validate account
        if (!accountMgrApi.validateAccount(applicantAccountID, credential, AccountRole.NORMAL_ROLE)) {
            throw new DataClayRuntimeException(ERRORCODE.INVALID_CREDENTIALS);
        }

        return datacontractMgrApi.getDataContractIDsOfApplicant(applicantAccountID);

    }

    @Override
    public DataContract getDataContractInfoOfApplicantWithProvider(final AccountID applicantAccountID,
                                                                   final PasswordCredential credential, final DataSetID datasetIDofProvider) {
        // Validate account
        if (!accountMgrApi.validateAccount(applicantAccountID, credential, AccountRole.NORMAL_ROLE)) {
            throw new DataClayRuntimeException(ERRORCODE.INVALID_CREDENTIALS);
        }

        return datacontractMgrApi.getDataContractInfoOfApplicantWithProvider(applicantAccountID, datasetIDofProvider);

    }

    // ============== Interface Manager ==============//

    @Override
    public InterfaceID newInterface(final AccountID accountID, final PasswordCredential credential,
                                    final Interface newInterface) {
        // =========== CHECK AND FILL WITH IDS =========== //

        // Account ID
        if (newInterface.getProviderAccountID() == null) {
            final String accountName = newInterface.getProviderAccountName();
            if (accountName == null || accountName.isEmpty()) {
                throw new DataClayRuntimeException(ERRORCODE.BAD_INTERFACE, "Null or empty account name", false);
            }
            final AccountID accID = accountMgrApi.getAccountID(accountName);
            newInterface.setProviderAccountID(accID);
        }

        /**** rtouma ****/
        // Account name
        if (newInterface.getProviderAccountName() == null) {
            final AccountID accID = newInterface.getProviderAccountID();
            if (accID == null) {
                throw new DataClayRuntimeException(ERRORCODE.BAD_INTERFACE, "Null or empty account ID", false);
            }
            final String accName = accountMgrApi.getAccount(accID).getUsername();
            newInterface.setProviderAccountName(accName);
        }
        /****************/

        // Namespace
        final String namespace = newInterface.getNamespace();
        if (namespace == null || namespace.isEmpty()) {
            throw new DataClayRuntimeException(ERRORCODE.BAD_INTERFACE, "Null or empty interface namespace", false);
        }
        final NamespaceID namespaceID = namespaceMgrApi.getNamespaceID(namespace);
        newInterface.setNamespaceID(namespaceID);

        // Interface ID of class
        final String classNamespace = newInterface.getClassNamespace();
        if (classNamespace == null || classNamespace.isEmpty()) {
            throw new DataClayRuntimeException(ERRORCODE.BAD_INTERFACE, "Null or empty class namespace", false);
        }
        final NamespaceID classNamespaceID = namespaceMgrApi.getNamespaceID(classNamespace);
        newInterface.setClassNamespaceID(classNamespaceID);

        // Class ID
        final String className = newInterface.getClassName();
        if (className == null || className.isEmpty()) {
            throw new DataClayRuntimeException(ERRORCODE.BAD_INTERFACE, "Null or empty interface class", false);
        }
        MetaClassID metaClassID = classMgrApi.getMetaClassID(classNamespaceID, className);
        newInterface.setMetaClassID(metaClassID);

        // Property IDs
        final Set<PropertyID> propIDs = new HashSet<>();
        if (newInterface.getPropertiesInIface() == null) {
            throw new DataClayRuntimeException(ERRORCODE.BAD_INTERFACE, "Null properties list", false);
        }
        for (final String propName : newInterface.getPropertiesInIface()) {
            final PropertyID propID = classMgrApi.getPropertyID(metaClassID, propName);
            propIDs.add(propID);
        }
        newInterface.setPropertiesIDs(propIDs);

        // Operation IDs
        final Set<OperationID> opIDs = new HashSet<>();
        if (newInterface.getOperationsSignatureInIface() == null) {
            throw new DataClayRuntimeException(ERRORCODE.BAD_INTERFACE, "Null operations list", false);
        }
        for (final String opSignature : newInterface.getOperationsSignatureInIface()) {
            final OperationID opID = classMgrApi.getOperationID(metaClassID, opSignature);
            opIDs.add(opID);
        }
        newInterface.setOperationsIDs(opIDs);

        // =========== =========== //

        // Check interface exists
        if (interfaceMgrApi.existsInterface(newInterface.getProviderAccountName(), newInterface.getNamespace(),
                newInterface.getClassName(), newInterface.getPropertiesInIface(),
                newInterface.getOperationsSignatureInIface())) {
            throw new DataClayRuntimeException(ERRORCODE.BAD_INTERFACE, "Interface already exists", false);
        }

        // Check namespace responsible => validates account
        final HashSet<InterfaceID> interfacesOfImports = new HashSet<>();
        Namespace namespaceInfo = null;
        try {
            namespaceInfo = checkNamespaceResponsible(accountID, credential, namespaceID);
        } catch (final AccountNotResponsibleOfNamespace e) {
            // class ID does not exist in namespace, look for it in the imports
            final Set<ImportedInterface> importedInterfaces = namespaceMgrApi.getImportedInterfaces(namespaceID,
                    className);
            if (importedInterfaces.isEmpty()) {
                throw new DataClayRuntimeException(ERRORCODE.CLASS_NOT_CREATED_IN_NAMESPACE_NOR_IMPORTED,
                        "Class " + className + " not created in namespace {" + namespaceID + "}", false);
            }

            // Check imports
            final Map<ContractID, InterfaceID> interfacesInContracts = new HashMap<>();
            for (final ImportedInterface importedInterface : importedInterfaces) {
                if (metaClassID == null) { // first iteration
                    metaClassID = importedInterface.getClassOfImportID();
                }

                final InterfaceID previousValue = interfacesInContracts.put(importedInterface.getContractID(),
                        importedInterface.getInterfaceID());
                if (previousValue != null) {
                    // WARNING: This should not happen ever (18 Jun 2013 jmarti)
                    throw new DataClayRuntimeException(ERRORCODE.CONTRACT_DUPLICATION);
                }

                interfacesOfImports.add(importedInterface.getInterfaceID());
            }

            // Check interfaces in contracts
            if (!contractMgrApi.checkInterfacesInActiveContractsForAccount(accountID, interfacesInContracts)) {
                throw new DataClayRuntimeException(ERRORCODE.BAD_CONTRACT);
            }

        }

        final Set<PropertyID> propertiesIDs = newInterface.getPropertiesIDs();
        final Set<OperationID> operationsIDs = newInterface.getOperationsIDs();
        if (namespaceInfo.getLanguage() == Langs.LANG_JAVA) { // Getters/setters do not apply to Python codes (jcosta
            // 17/jun/2014)
            // Get operations IDs of default setters and getters
            final Set<OperationID> gettersAndSetters = classMgrApi.getOperationsIDsOfGettersAndSetters(metaClassID,
                    propertiesIDs);
            operationsIDs.addAll(gettersAndSetters);
        }

        final InterfaceID ifaceID = interfaceMgrApi.newInterface(newInterface);
        return ifaceID;

    }

    @Override
    public Interface getInterfaceInfo(final AccountID accountID, final PasswordCredential credential,
                                      final InterfaceID interfaceID) {
        // Validate account
        if (!accountMgrApi.validateAccount(accountID, credential, AccountRole.NORMAL_ROLE)) {
            throw new DataClayRuntimeException(ERRORCODE.INVALID_CREDENTIALS);
        }

        // Get contracts of account
        final Map<ContractID, Contract> contractsOfAccount = contractMgrApi.getContractIDsOfApplicant(accountID);

        // Check interface is present within the contracts of account
        boolean found = false;
        for (final Entry<ContractID, Contract> curContract : contractsOfAccount.entrySet()) {
            final Contract contractInfo = curContract.getValue();
            found = contractInfo.getInterfacesInContract().containsKey(interfaceID);
            if (found) {
                break;
            }
        }
        if (!found) {
            throw new DataClayRuntimeException(ERRORCODE.INTERFACE_NOT_ACCESSIBLE);
        }

        final HashSet<InterfaceID> interfacesIDs = new HashSet<>();
        interfacesIDs.add(interfaceID);
        final Interface ifaceInfo = interfaceMgrApi.getInterfacesInfo(interfacesIDs).values().iterator().next();

        return ifaceInfo;

    }

    @Override
    public void removeInterface(final AccountID accountID, final PasswordCredential credential,
                                final NamespaceID namespaceID, final InterfaceID interfaceID) {
        // Check namespace responsible
        checkNamespaceResponsible(accountID, credential, namespaceID);

        // Check there is no contract associated with the interface
        if (!contractMgrApi.checkInterfaceHasNoContracts(interfaceID)) {
            throw new DataClayRuntimeException(ERRORCODE.INTERFACE_WITH_CONTRACTS);
        }

        interfaceMgrApi.removeInterface(namespaceID, interfaceID);

    }

    // ============== Metadata Service ==============//

	@Override
	public List<ObjectID> registerObjects(final List<RegistrationInfo> regInfos, final ExecutionEnvironmentID backendID,
			final Langs lang) {
        LOGGER.debug("==> Registering " + regInfos.size() + " objects on EE " + backendID + " of language " + lang);
        List<ObjectID> newObjectIDs = new ArrayList<>();
        for (RegistrationInfo regInfo : regInfos) {
            ObjectID newOID = registerObject(regInfo, backendID, lang);
            newObjectIDs.add(newOID);
        }
        LOGGER.debug("==> Finished registration of objects ");
        return newObjectIDs;
    }

    private ObjectID registerObject(final RegistrationInfo regInfo, final ExecutionEnvironmentID backendID,
                                    final Langs lang) {

		LOGGER.debug("==> Registering object explicit call: " + regInfo);

		// Register the object in the metadataservice
		final HashSet<ExecutionEnvironmentID> backendIDs = new HashSet<>();
		backendIDs.add(backendID);
		ObjectID objectIDofNewObject = regInfo.getObjectID();
		final SessionID ownerSessionID = regInfo.getStoreSessionID();
		final MetaClassID metaClassID = regInfo.getClassID();
		final String alias = regInfo.getAlias();
		DataSetID datasetIDforStore = null;
		AccountID ownerAccountID = null;
		if (ownerSessionID == null) {
			// objects created in federation or without session
			// TODO: create a general dataClay dataset and account
			datasetIDforStore = dataSetMgrApi.getDataSetID(EXTERNAL_OBJECTS_DATASET_NAME);
			ownerAccountID = accountMgrApi.getAccountID(FEDERATOR_ACCOUNT_USERNAME);
		} else {
		    try {
                final SessionInfo sessionInfo = getSessionInfo(ownerSessionID);
                final Map<DataContractID, SessionDataContract> sessionDataInfo = sessionInfo.getSessionDataContracts();
                final DataContractID dataContractIDforStore = sessionInfo.getDataContractIDforStore();
                final SessionDataContract sessionDataContractOfStore = sessionDataInfo.get(dataContractIDforStore);
                datasetIDforStore = sessionDataContractOfStore.getDataSetOfProvider();
                ownerAccountID = sessionInfo.getAccountID();
            } catch (SessionNotExistException ex) {
                // TODO: create a general dataClay dataset and account
                datasetIDforStore = dataSetMgrApi.getDataSetID(EXTERNAL_OBJECTS_DATASET_NAME);
                ownerAccountID = accountMgrApi.getAccountID(FEDERATOR_ACCOUNT_USERNAME);
            }
		}

        if (regInfo.getDataSetID() != null) {
            // User specified dataset ID
            datasetIDforStore = regInfo.getDataSetID();
        }

        // If object is not registered, we register it with new alias included.
        final MetaDataInfo info = metaDataSrvApi.registerObject(objectIDofNewObject, metaClassID, datasetIDforStore,
                backendIDs, Configuration.Flags.READONLY_BY_DEFAULT.getBooleanValue(), alias, lang, ownerAccountID);
        objectIDofNewObject = info.getDataClayID();
        //TODO: notify GCs about new aliases

       /** if (alias != null && !alias.isEmpty()) {
            // notify alias reference since it is the first alias (with registration)
            // first makePeristent(alias)
            final Map<ObjectID, Integer> referenceCounting = new HashMap<>();
            referenceCounting.put(objectIDofNewObject, 1);
            notifyGarbageCollectors(referenceCounting);
        } **/

		LOGGER.debug("<== Finished registration of object with ID " + objectIDofNewObject);
        return objectIDofNewObject;
	}

    @Override
    public void unregisterObjects(final Set<ObjectID> objectsToUnregister) {
        for (final ObjectID objectID : objectsToUnregister) {
            LOGGER.debug("** Unregistering object: " + objectID);
            metaDataSrvApi.unregisterObject(objectID);
            // if object is external and marked as unregister = true
            if (metaDataSrvApi.externalObjectIsUnregistered(objectID)) {
                LOGGER.debug("** Unregistering External object: " + objectID);
                metaDataSrvApi.unregisterExternalObject(objectID);
            }
        }
        this.metaDataSrvApi.vacuumDB();
    }

    @Override
    public void registerObjectFromGC(final RegistrationInfo regInfo, final ExecutionEnvironmentID backendID,
                                     final DataServiceRuntime clientLib) {
        if (DEBUG_ENABLED) {
            LOGGER.debug("Registering object from GC: " + regInfo.getObjectID());
        }

        final HashSet<ExecutionEnvironmentID> backendIDs = new HashSet<>();
        backendIDs.add(backendID);

        final ObjectID objectIDofNewObject = regInfo.getObjectID();
        final MetaClassID metaClassID = regInfo.getClassID();
        final SessionID ownerSessionID = regInfo.getStoreSessionID();
        final SessionInfo sessionInfo = getSessionInfo(ownerSessionID);
        final Map<DataContractID, SessionDataContract> sessionDataInfo = sessionInfo.getSessionDataContracts();
        final DataContractID dataContractIDforStore = sessionInfo.getDataContractIDforStore();
        final SessionDataContract sessionDataContractOfStore = sessionDataInfo.get(dataContractIDforStore);
        DataSetID datasetIDforStore = sessionDataContractOfStore.getDataSetOfProvider();
        if (regInfo.getDataSetID() != null) {
            // User specified dataset ID
            datasetIDforStore = regInfo.getDataSetID();
        }
        try {
            metaDataSrvApi.registerObject(objectIDofNewObject, metaClassID, datasetIDforStore, backendIDs,
                    Configuration.Flags.READONLY_BY_DEFAULT.getBooleanValue(), null, Langs.LANG_JAVA,
                    sessionInfo.getAccountID());
        } catch (final SessionNotExistException sessExpiredExc) {
            LOGGER.debug("The session does not exist", sessExpiredExc);

            // ignore this session till design of what to do with objects persisted and
            // sessions closed before
            // actually registered: PULL hearth-bate from nodes, for instance.
        }
    }


    @Override
    public Map<ExecutionEnvironmentID, ExecutionEnvironment> getAllExecutionEnvironmentsInfo(final Langs execEnvLang,
                                                                                             final boolean getExternal,
                                                                                             final boolean fromBackend) {
        final Map<ExecutionEnvironmentID, ExecutionEnvironment> execEnvs = this.logicMetadataMgr
                .getAllExecutionEnvironmentsInfo(execEnvLang);

        if (!fromBackend && exposedIPForClient != null) {
            // All information send to client will use exposed IP configured
            for (final Entry<ExecutionEnvironmentID, ExecutionEnvironment> ee : execEnvs.entrySet()) {
                ee.getValue().setHostname(exposedIPForClient);
            }
        }
        if (getExternal) {
            for (DataClayInstanceID externalDataClayID : this.logicMetadataMgr.getAllExternalDataClays()) {

                // Get external logicmodule
                LogicModuleAPI externalLogicModule = null;
                try {
                    externalLogicModule = this.getExternalLogicModule(this.getExternalDataClayInfo(externalDataClayID));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // Get external exec envs
                Map<ExecutionEnvironmentID, ExecutionEnvironment> result = externalLogicModule.getAllExecutionEnvironmentsInfo(execEnvLang, false, false);
                execEnvs.putAll(result);
            }
        }


        if (DEBUG_ENABLED) {
            for (ExecutionEnvironment execEnv : execEnvs.values()) {
                LOGGER.debug("Found execution environment with ID {}: {}", execEnv.getDataClayID(), execEnv);
            }
        }

        return execEnvs;
    }


    /**
     * Get all storage locations information
     * @return Storage location infos
     */
    private Map<StorageLocationID, StorageLocation> getAllStorageLocationsInfo() {
        final Map<StorageLocationID, StorageLocation> stLocs = logicMetadataMgr.getAllStorageLocationsInfo();
        return stLocs;
    }

    @Override
    public StorageLocation getStorageLocationInfo(final StorageLocationID backendID,
                                                      final boolean fromBackend) {
        StorageLocation stLoc = logicMetadataMgr.getStorageLocationInfo(backendID);
        if (!fromBackend && exposedIPForClient != null) {
            // All information send to client will use exposed IP configured
            stLoc.setHostname(exposedIPForClient);
        }
        return stLoc;

    }

    @Override
    public ExecutionEnvironment getExecutionEnvironmentInfo(final ExecutionEnvironmentID backendID,
                                                            final boolean fromBackend) {
        ExecutionEnvironment execEnv = logicMetadataMgr.getExecutionEnvironmentInfo(backendID);
        if (!fromBackend && exposedIPForClient != null) {
            // All information send to client will use exposed IP configured
            execEnv.setHostname(exposedIPForClient);
        }
        LOGGER.debug("Found execution environment with ID {}: {}", backendID, execEnv);
        return execEnv;

    }

    @Override
    public Tuple<String, String> getObjectInfo(final SessionID sessionID, final ObjectID objectID) {

        // FIXME: Looks like this method is not used anymore! (July 2018 jmarti)

        // Get session info if needed
        SessionInfo sessionInfo = null;
        if (Configuration.Flags.CHECK_SESSION.getBooleanValue()) {
            sessionInfo = getSessionInfo(sessionID);
        }

        // Get object info
        final MetaDataInfo metadataInfo = getObjectMetadata(objectID);
        if (metadataInfo == null) {
            throw new DbObjectNotExistException(objectID);
        }

        // Check dataset if needed
        if (Configuration.Flags.CHECK_SESSION.getBooleanValue()) {
            checkDataSetAmongDataContracts(metadataInfo.getDatasetID(), sessionInfo.getSessionDataContracts());
        }

        return classMgrApi.getClassNameAndNamespace(metadataInfo.getMetaclassID());

    }

    @Override
    public Triple<ObjectID, MetaClassID, ExecutionEnvironmentID> getObjectFromAlias(final SessionID sessionID,
                                                                                    final String alias) {
        try {

			if (DEBUG_ENABLED) {
				LOGGER.debug("==> Starting get object from alias for alias " + alias);
			}

            // Get session if needed
            SessionInfo sessionInfo = null;
            if (Configuration.Flags.CHECK_SESSION.getBooleanValue()) {
                sessionInfo = getSessionInfo(sessionID);
            }

            if (DEBUG_ENABLED) {
                LOGGER.debug("Get object info from alias " + alias);
            }
            final Tuple<ObjectID, MetaDataInfo> infoOfObject = metaDataSrvApi.getObjectInfoFromAlias(alias);
            final MetaDataInfo metadataInfo = infoOfObject.getSecond();

            // Check dataset
            if (Configuration.Flags.CHECK_SESSION.getBooleanValue()) {
                if (DEBUG_ENABLED) {
                    LOGGER.debug("Check dataset is among datacontract for object with alias " + alias);
                }
                checkDataSetAmongDataContracts(metadataInfo.getDatasetID(), sessionInfo.getSessionDataContracts());
            }

            final ObjectID result = infoOfObject.getFirst();
            final MetaClassID mclassID = metadataInfo.getMetaclassID();
            final ExecutionEnvironmentID execID = metadataInfo.getLocations().iterator().next();
            if (DEBUG_ENABLED) {
                LOGGER.debug("Returning object with id {} and alias {}", result, alias);
            }
            return new Triple<>(result, mclassID, execID);
        } catch (final Exception ex) {
            LOGGER.debug("getObjectFromAlias error", ex);
            throw ex;
        }
    }

    @Override
    public ObjectID deleteAlias(final SessionID sessionID, final String alias) {
        try {
            if (DEBUG_ENABLED) {
                LOGGER.debug("Starting delete alias " + alias);
            }

            final Tuple<ObjectID, MetaDataInfo> infoOfObject = metaDataSrvApi.getObjectInfoFromAlias(alias);
            final MetaDataInfo metadataInfo = infoOfObject.getSecond();

            // Check session if needed
            if (Configuration.Flags.CHECK_SESSION.getBooleanValue()) {
                final SessionInfo sessionInfo = getSessionInfo(sessionID);
                if (DEBUG_ENABLED) {
                    LOGGER.debug("Check dataset is among datacontract for object with alias " + alias);
                }
                checkDataSetAmongDataContracts(metadataInfo.getDatasetID(), sessionInfo.getSessionDataContracts());
            }

            // Delete alias if can access object
            final ObjectID objectID = metaDataSrvApi.deleteAlias(alias);

            // Notify GC of each EE where a replica is located to add -1 reference
            // TODO: check how GC works with alias removal from DS
            /** final Map<ObjectID, Integer> referenceCounting = new HashMap<>();
            referenceCounting.put(objectID, -1);
            notifyGarbageCollectors(referenceCounting); **/

            return objectID;

        } catch (final Exception ex) {
            LOGGER.debug("deleteAlias error", ex);
            throw new DataClayRuntimeException(ERRORCODE.UNKNOWN_EXCEPTION, "deleteAlias failed", false);
        }
    }


    @Override
    public Map<ObjectID, MetaDataInfo> getObjectsMetaDataInfoOfClassForNM(final MetaClassID classID) {
        // TODO We have to ensure that only Notification Manager can access this method
        // (3 Mar 2016 jmarti)
        return metaDataSrvApi.getObjectsOfSpecificClass(classID);
    }

    @Override
    public DataClayInstanceID getDataClayID() {
        return publicIDs.dcID;
    }

    /**
     * Get API to connect with external LogicModule
     *
     * @param dcInfo
     *            DataClayInstance of external DataClay
     * @return API to connect with external LogicModule
     * @throws InterruptedException
     *             if connection failed
     */
    private LogicModuleAPI getExternalLogicModule(final DataClayInstance dcInfo) throws InterruptedException {

        LogicModuleAPI lmExternal = null;
        final List<String> hosts = dcInfo.getHosts();
        final List<Integer> ports = dcInfo.getPorts();
        final List<String> missedhosts = new ArrayList<>();
        final List<Integer> missedports = new ArrayList<>();

        try {
            for (int i = 0; i < hosts.size(); i++) {
                try {
                    LOGGER.debug("Getting dataClay at " + hosts.get(i) + ":" + ports.get(i) + "");
                    lmExternal = grpcClient.getLogicModuleAPI(hosts.get(i), ports.get(i));
                    lmExternal.checkAlive();
                    break;
                } catch (final Exception e) {
                    LOGGER.debug("Could not connect to dataClay " + hosts.get(i)
                            + ":" + ports.get(i) + ". Trying another address.");
                    missedhosts.add(hosts.get(i));
                    missedports.add(ports.get(i));

                    if (i + 1 == hosts.size()) {
                        throw e;
                    }
                }
            }
        } finally {
            // remove missed addresses from metadata
            if (missedhosts.size() > 0) {
                final int i = 0;
                for (final String missedhost : missedhosts) {
                    final Integer missedport = missedports.get(i);
                    LOGGER.debug("Unregistering " + hosts.get(i) + ":" + ports.get(i) + " external dataClay");
                    logicMetadataMgr.unregisterExternalDataClayAddress(missedhost, missedport);
                }
            }
        }
        return lmExternal;
    }

    @Override
    public DataClayInstanceID getExternalDataClayID(final String dcHost, final int dcPort) {
        try {
            if (DEBUG_ENABLED) {
                LOGGER.debug("Getting external dataClay ID at {}:{}", dcHost, dcPort);
            }
            // Get it from MDservice
            return logicMetadataMgr.getExternalDataClayID(dcHost, dcPort);
        } catch (final Exception ex) {
            LOGGER.debug("External dataClay at {}:{} not found. Returning null.", dcHost, dcPort);
            return null;
        }
    }

    @Override
    public DataClayInstance getExternalDataClayInfo(final DataClayInstanceID extDataClayID) {
        try {
            final DataClayInstance dcInstance = logicMetadataMgr.getExternalDataClayInfo(extDataClayID);
            return dcInstance;
        } catch (final ExternalDataClayNotRegisteredException edn) {
            return null;
        }
    }

    @Override
    public DataClayInstanceID registerExternalDataClay(final String thehostname, final int theport) {

        if (DEBUG_ENABLED) {
            LOGGER.debug("Registering external dataClay at {}:{}", thehostname, theport);

        }
        return registerExternalDataClayAux(thehostname, theport);
    }

    /**
     * Register external dataclay aux function
     * @param thehostname Hostname
     * @param theport Port
     * @return ID of the registered external dataclay
     */
    private DataClayInstanceID registerExternalDataClayAux(final String thehostname,
                                                           final int theport) {
        try {


            final DataClayInstanceID id = getExternalDataClayID(thehostname, theport);
            if (id != null) {
                if (DEBUG_ENABLED) {
                    LOGGER.debug("Already registered external dataClay at {}:{}", thehostname, theport);
                }
                return id; //Already registered
            }

            final LogicModuleAPI lmExternal = grpcClient.getLogicModuleAPI(thehostname, theport);

            String myHostname = this.hostname;
            if (exposedIPForClient != null) {
                myHostname = exposedIPForClient;
            }

            final DataClayInstanceID dcID = lmExternal.notifyRegistrationOfExternalDataClay(this.getDataClayID(),
                    myHostname, this.port);
            final DataClayInstance dcInstance = new DataClayInstance(dcID, thehostname, theport);
            logicMetadataMgr.registerExternalDataclay(dcInstance);
            if (DEBUG_ENABLED) {
                LOGGER.debug("Registered external dataClay {}", dcInstance);
            }
            return dcID;
        } catch (final Exception ex) {
            if (DEBUG_ENABLED) {
                LOGGER.debug("Exception produced during registration of external dataClay", ex);
            }
            return null;
        }
    }

    @Override
    public DataClayInstanceID registerExternalDataClayOverrideAuthority(final AccountID adminAccountID, final PasswordCredential adminCredential,
                                                                        final String thehostname, final int theport, final String authority) {

        // Validate admin account
        this.accountMgrApi.validateAccount(adminAccountID, adminCredential, AccountRole.ADMIN_ROLE);

        // NOTE: currently not implemented to beware of concurrent modifications of the SSL_AUTHORITY configuration field
        Configuration.Flags.SSL_TARGET_AUTHORITY.setValue(authority);

        return this.registerExternalDataClayAux(thehostname, theport);

    }

    @Override
    public DataClayInstanceID notifyRegistrationOfExternalDataClay(final DataClayInstanceID dataClayInstanceID,
                                                                   final String thehostname, final int theport) {
        try {
            final DataClayInstance dcInstance = new DataClayInstance(dataClayInstanceID, thehostname, theport);
            logicMetadataMgr.registerExternalDataclay(dcInstance);
            if (DEBUG_ENABLED) {
                LOGGER.debug("Notified registration of external dataClay {}", dcInstance);
            }
            return this.getDataClayID();
        } catch (final Exception ex) {
            if (DEBUG_ENABLED) {
                LOGGER.debug("Exception produced during registration of external dataClay", ex);
            }
            return null;
        }
    }

    @Override
    public Tuple<Namespace, Set<MetaClass>> getClassesInNamespace(final String namespaceName) {
        NamespaceID namespaceID = this.namespaceMgrApi.getNamespaceID(namespaceName);
        final Map<MetaClassID, MetaClass> classesToFederate = this.classMgrApi.getInfoOfClassesInNamespace(namespaceID);
        final Namespace namespaceToFederate = namespaceMgrApi.getNamespaceInfo(namespaceID);
        Set<MetaClass> metaClassesToFederate = new HashSet<MetaClass>(classesToFederate.values());
        return new Tuple<Namespace, Set<MetaClass>>(namespaceToFederate, metaClassesToFederate);

    }

    @Override
    public void importModelsFromExternalDataClay(final String extNamespaceName, final DataClayInstanceID extDataClayID) {

        // Get all information needed from external dataClay instance
        LogicModuleAPI externalLogicModule = null;
        try {
            externalLogicModule = this.getExternalLogicModule(this.getExternalDataClayInfo(extDataClayID));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Tuple<Namespace, Set<MetaClass>> toRegister = externalLogicModule.getClassesInNamespace(extNamespaceName);
        Namespace namespace = toRegister.getFirst();
        Set<MetaClass> classesToRegister = toRegister.getSecond();

        // Register namespace
        this.namespaceMgrApi.getDbHandler().store(namespace);

        // Register metaClasses
        ClassManagerDB classMgrDbHandler = this.classMgrApi.getDbHandler();
        Set<String> metaClassesToInstall = new HashSet<>();
        Map<NamespaceID, Namespace> namespaceInfos = new HashMap<>();
        namespaceInfos.put(namespace.getDataClayID(), namespace);
        for (MetaClass metaClass : classesToRegister) {
            classMgrDbHandler.storeMetaClass(metaClass);
            metaClassesToInstall.add(metaClass.getName());
        }
        // Outsource classes to dataservices
        Langs language = namespace.getLanguage();
        if (language == Langs.LANG_JAVA) {
            outsourceClassesDeploymentJava(classesToRegister, metaClassesToInstall, namespaceInfos,
                    null, null);
        } else if (language == Langs.LANG_PYTHON) {
            outsourceClassesDeploymentPython(classesToRegister, metaClassesToInstall, namespaceInfos,
                    null, null);
        }
    }

    // ============== Data Service ==============//

    @Override
    public void setDataSetID(final SessionID sessionID, final ObjectID objectID, final DataSetID dataSetID) {
        // Check session exists if needed
        if (Configuration.Flags.CHECK_SESSION.getBooleanValue()) {
            getSessionInfo(sessionID);
        }
        metaDataSrvApi.changeDataSetID(objectID, dataSetID);
    }

    @Override
    public void setDataSetIDFromGarbageCollector(final ObjectID objectID, final DataSetID dataSetID) {
        metaDataSrvApi.changeDataSetID(objectID, dataSetID);
    }

    @Override
    public List<ObjectID> moveObject(final SessionID sessionID, final ObjectID objectID,
                                     final ExecutionEnvironmentID srcLocationID, final ExecutionEnvironmentID destLocationID,
                                     final boolean recursive) {

        final List<ObjectID> allMovedObjects = new ArrayList<>();
        try {
            // Get object info
            final MetaDataInfo metadataInfo = getObjectMetadata(objectID);
            if (metadataInfo == null) {
                throw new DataClayRuntimeException(ERRORCODE.OBJECT_NOT_EXIST);
            }

            // Check session if needed
            if (Configuration.Flags.CHECK_SESSION.getBooleanValue()) {
                final SessionInfo sessionInfo = getSessionInfo(sessionID);
                final Map<DataContractID, SessionDataContract> sessionDataInfo = sessionInfo.getSessionDataContracts();

                // Check dataset
                checkDataSetAmongDataContracts(metadataInfo.getDatasetID(), sessionDataInfo);
            }

            // Check object is present in srcBackend
            final Map<ExecutionEnvironmentID, ExecutionEnvironment> locations = metaDataSrvApi
                    .getObjectBackends(objectID);
            if (!locations.containsKey(srcLocationID)) {
                throw new DataClayRuntimeException(ERRORCODE.OBJECT_NOT_IN_BACKEND);
            }

            // Move replica object from source to dest
            final ExecutionEnvironment srcLoc = locations.get(srcLocationID);

            final DataServiceAPI dataServiceApi = getExecutionEnvironmentAPI(srcLoc);
            final Set<ObjectID> movedObjs = dataServiceApi.moveObjects(sessionID, objectID, destLocationID, recursive);
            allMovedObjects.addAll(movedObjs);

            // Register the movement
            final Map<ObjectID, MetaDataInfo> newMetaDatas = new HashMap<>();
            for (final ObjectID movedObjID : movedObjs) {
                try {
                    metaDataSrvApi.migrateObjectToBackend(movedObjID, srcLocationID, destLocationID);
                    final MetaDataInfo mdInfo = metaDataSrvApi.getObjectMetaData(objectID);
                    newMetaDatas.put(objectID, mdInfo);
                } catch (final Exception ex) {
                    LOGGER.debug("Error in moveObject, ignoring", ex);
                    continue;
                }
            }

            // Update all md Caches in ALL nodes
            /*
             * for (Tuple<DataServiceAPI, StorageLocation> ds : getDsAPIs().values()) {
             * ds.getFirst().newMetaData(newMetaDatas); }
             */
        } catch (final Exception ex) {
            LOGGER.debug("moveObject error", ex);
            throw ex;
        }

        return allMovedObjects;
    }

    @Override
    public void setObjectReadOnly(final SessionID sessionID, final ObjectID objectID) {
        // Check session if needed
        if (Configuration.Flags.CHECK_SESSION.getBooleanValue()) {
            final SessionInfo sessionInfo = getSessionInfo(sessionID);
            final Map<DataContractID, SessionDataContract> sessionDataInfo = sessionInfo.getSessionDataContracts();

            // Get object info
            final MetaDataInfo metadataInfo = getObjectMetadata(objectID);
            if (metadataInfo == null) {
                throw new DataClayRuntimeException(ERRORCODE.OBJECT_NOT_EXIST);
            }

            // Check dataset
            checkDataSetAmongDataContracts(metadataInfo.getDatasetID(), sessionDataInfo);
        }

        // Perform op
        metaDataSrvApi.setObjectReadOnly(objectID);
    }

    @Override
    public void setObjectReadWrite(final SessionID sessionID, final ObjectID objectID) {
        // Check session if needed
        if (Configuration.Flags.CHECK_SESSION.getBooleanValue()) {
            final SessionInfo sessionInfo = getSessionInfo(sessionID);
            final Map<DataContractID, SessionDataContract> sessionDataInfo = sessionInfo.getSessionDataContracts();

            // Get object info
            final MetaDataInfo metadataInfo = getObjectMetadata(objectID);
            if (metadataInfo == null) {
                throw new DataClayRuntimeException(ERRORCODE.OBJECT_NOT_EXIST);
            }

            // Check dataset
            checkDataSetAmongDataContracts(metadataInfo.getDatasetID(), sessionDataInfo);
        }

        // Perform op
        metaDataSrvApi.setObjectReadWrite(objectID);

    }

    @Override
    public MetaDataInfo getMetadataByOID(final SessionID sessionID, final ObjectID objectID) {
        try {
            // Check session
            SessionInfo sessionInfo = null;
            if (Configuration.Flags.CHECK_SESSION.getBooleanValue()) {
                sessionInfo = getSessionInfo(sessionID);
            }

            // Get object info
            final MetaDataInfo metadataInfo = getObjectMetadata(objectID);
            if (metadataInfo == null) {
                // In this case, return null instead of Exception for performance purposes
                if (DEBUG_ENABLED) {
                    LOGGER.debug("[==GetMetadataByOID==] Object not found. Sending null.");
                }
                return null;
            }
            if (DEBUG_ENABLED) {
                LOGGER.debug("[==GetMetadataByOID==] Object found. Sending " + objectID + " metadata with locations "
                        + metadataInfo.getLocations());
            }

            // Check dataset
            if (Configuration.Flags.CHECK_SESSION.getBooleanValue()) {
                checkDataSetAmongDataContracts(metadataInfo.getDatasetID(), sessionInfo.getSessionDataContracts());
            }
            return metadataInfo;

        } catch (final Exception ex) {
            LOGGER.debug("getMetadataByOID error", ex);
            throw ex;
        }

    }

    @Override
    public MetaDataInfo getMetadataByOIDForDS(final ObjectID objectID) {
        try {
            // Get object info
            final MetaDataInfo metadataInfo = getObjectMetadata(objectID);
            return metadataInfo;

        } catch (final Exception ex) {
            LOGGER.debug("getMetadataByOIDForDS error", ex);
            throw ex;
        }

    }

    @Override
    public SerializedParametersOrReturn executeMethodOnTarget(final SessionID sessionID, final ObjectID objectID,
                                                              final String operationSignature, final SerializedParametersOrReturn params,
                                                              final ExecutionEnvironmentID backendID) {
        // Get object metadata
        final MetaDataInfo metadataInfo = getObjectMetadata(objectID);
        if (metadataInfo == null) {
            return null;
        }

        // Get model info
        final SessionInfo sessionInfo = getSessionInfo(sessionID);
        final Map<ContractID, SessionContract> sessionModelInfo = sessionInfo.getSessionContracts();

        // Check datasets in session
        if (Configuration.Flags.CHECK_SESSION.getBooleanValue()) {
            final Map<DataContractID, SessionDataContract> sessionDataInfo = sessionInfo.getSessionDataContracts();
            checkDataSetAmongDataContracts(metadataInfo.getDatasetID(), sessionDataInfo);
        }

        // Get method id from the class of object
        final MetaClassID classID = metadataInfo.getMetaclassID();
        final OperationID opID = classMgrApi.getOperationID(classID, operationSignature);

        // Find an implementation that matches with the operation signature for the
        // current session
        ImplementationID remoteImplementationID = null;
        for (final SessionContract sc : sessionModelInfo.values()) {
            for (final SessionInterface si : sc.getSessionInterfaces().values()) {
                for (final SessionOperation so : si.getSessionOperations().values()) {
                    if (so.getOperationID().equals(opID)) {
                        remoteImplementationID = so.getSessionRemoteImplementation().getImplementationID();
                    }
                }
            }
        }

        if (remoteImplementationID != null) {
            return executeMethodOnTargetInternal(sessionID, remoteImplementationID, objectID, metadataInfo, params,
                    backendID);
        } else {
            throw new DataClayRuntimeException(ERRORCODE.IMPLEMENTATION_NOT_EXIST);
        }
    }

    /**
     * Internal function for execute implementation.
     *
     * @param origSession
     *            ID of the original session corresponding to initial application
     *            launcher
     * @param remoteImplementationID
     *            ID of the implementation executed
     * @param objectID
     *            ID of the object
     * @param metadataInfo
     *            Info of the object
     * @param params
     *            Parameters to the method
     * @param targetBackend
     *            Backend where the task must be executed
     * @return Object resulting of the execution of the implementation (all of them
     *         sepparately) @
     */
    private SerializedParametersOrReturn executeMethodOnTargetInternal(final SessionID origSession,
                                                                       final ImplementationID remoteImplementationID, final ObjectID objectID, final MetaDataInfo metadataInfo,
                                                                       final SerializedParametersOrReturn params, final ExecutionEnvironmentID targetBackend) {

        final Set<ExecutionEnvironmentID> backendss = metadataInfo.getLocations();
        if (!backendss.contains(targetBackend)) {
            // TODO Should we create a temporal replica of the object for the execution? (17
            // Apr 2015 jmarti)
            throw new DataClayRuntimeException(ERRORCODE.OBJECT_NOT_IN_BACKEND,
                    "Object is not in the backend where the method has to be executed", false);
        }
        ExecutionEnvironment backend = this.getExecutionEnvironmentInfo(targetBackend, true);

        // Get the language for the current session
        final Langs sessionLang = getSessionInfo(origSession).getLanguage();

        // Execute the method
        // Limit retries depending on the current set of replicas of the object
        SerializedParametersOrReturn resultObj = null;
        final DataServiceAPI dataSrvApi = getExecutionEnvironmentAPI(backend);
        resultObj = dataSrvApi.executeImplementation(objectID, remoteImplementationID, params, origSession);

        return resultObj;

    }

    @Override
    public SerializedParametersOrReturn executeImplementation(final SessionID sessionID, final OperationID operationID,
                                                              final Triple<ImplementationID, ContractID, InterfaceID> remoteImplementation, final ObjectID objectID,
                                                              final SerializedParametersOrReturn params) {

        // ----------------- SESSION CHECKS ------------- //
        final MetaDataInfo metadataInfo;
        final SessionInfo sessionInfo;
        if (Configuration.Flags.CHECK_SESSION.getBooleanValue()) {
            final Tuple<SessionInfo, MetaDataInfo> checkedInfo = executionClientChecksAndGetInfo(sessionID, objectID,
                    remoteImplementation, operationID);
            sessionInfo = checkedInfo.getFirst();
            metadataInfo = checkedInfo.getSecond();
        } else {
            sessionInfo = getSessionInfo(sessionID);
            metadataInfo = getObjectMetadata(objectID);
        }
        if (metadataInfo == null) {
            return null;
        }

        // ------------------ EXECUTE ------------------ //

        // Get the language for the current session
        final Langs sessionLang = sessionInfo.getLanguage();

        // TODO: How should this be executed? in all backends or not? (jmarti 6 Apr
        // 2018)
        final ImplementationID remoteImplementationID = remoteImplementation.getFirst();
        final SerializedParametersOrReturn result = executeImplementationInternal(sessionID, remoteImplementationID,
                objectID, metadataInfo, params, sessionLang, false);

        return result;
    }

    /**
     * Common method to perform basic checks when processing an execution request
     * from a client stub.
     *
     * @param sessionID
     *            ID of the session
     * @param objectID
     *            ID of the object
     * @param remoteImplementation
     *            implementation to be executed
     * @param operationID
     *            ID of the operation to be executed
     * @return Info of the session and metadata
     * @throws RemoteException
     *             if any error occurs
     */
    private Tuple<SessionInfo, MetaDataInfo> executionClientChecksAndGetInfo(final SessionID sessionID,
                                                                             final ObjectID objectID, final Triple<ImplementationID, ContractID, InterfaceID> remoteImplementation,
                                                                             final OperationID operationID) {
        final ImplementationID remoteImplementationID = remoteImplementation.getFirst();
        final ContractID contractID = remoteImplementation.getSecond();
        final InterfaceID interfaceID = remoteImplementation.getThird();

        // TODO: Check if this method is still required (or methods calling this) (July
        // 2018 jmarti)

        // ---------------- SESSION CHECKS ------------------ //

        // Check session
        final SessionInfo sessionInfo = getSessionInfo(sessionID);
        final Map<ContractID, SessionContract> sessionModelInfo = sessionInfo.getSessionContracts();
        final Map<DataContractID, SessionDataContract> sessionDataInfo = sessionInfo.getSessionDataContracts();

        // Check dataset
        final MetaDataInfo metadataInfo = getObjectMetadata(objectID);
        if (metadataInfo == null) {
            throw new DataClayRuntimeException(ERRORCODE.OBJECT_NOT_EXIST);
        }
        checkDataSetAmongDataContracts(metadataInfo.getDatasetID(), sessionDataInfo);

        // Check interface and contract of implementation with the session
        final SessionContract sessionContract = sessionModelInfo.get(contractID);
        if (sessionContract == null || sessionContract.getSessionInterfaces() == null
                || sessionContract.getSessionInterfaces().isEmpty()) {
            throw new DataClayRuntimeException(ERRORCODE.SESSION_DOES_NOT_MATCH_REQ);
        }
        final SessionInterface sessionInterface = sessionContract.getSessionInterfaces().get(interfaceID);
        if (sessionInterface == null) {
            throw new DataClayRuntimeException(ERRORCODE.SESSION_DOES_NOT_MATCH_REQ);
        }

        // Check interface can access the operation
        final Map<OperationID, SessionOperation> sessionOperations = sessionInterface.getSessionOperations();
        final SessionOperation sessionOperation = sessionOperations.get(operationID);
        if (sessionOperation == null) {
            throw new DataClayRuntimeException(ERRORCODE.OPERATION_NOT_IN_INTERFACE);
        }
        final SessionImplementation sessionImplementation = sessionOperation.getSessionRemoteImplementation();
        if (!sessionImplementation.getImplementationID().equals(remoteImplementationID)) {
            throw new DataClayRuntimeException(ERRORCODE.INCOMPATIBLE_IMPLEMENTATION);
        }
        return new Tuple<>(sessionInfo, metadataInfo);
    }

    /**
     * Internal function for execute implementation.
     *
     * @param origSession
     *            ID of the original session corresponding to initial application
     *            launcher
     * @param remoteImplementationID
     *            ID of the implementation executed
     * @param objectID
     *            ID of the object
     * @param metadataInfo
     *            Info of the object
     * @param params
     *            Parameters to the method
     * @param sessionLang
     *            Session language
     * @param allBackends Execute in all backends (for replication purposes)
     * @return Object resulting of the execution of the implementation (all of them,
     *         sepparately) @
     */
    private SerializedParametersOrReturn executeImplementationInternal(final SessionID origSession,
                                                                       final ImplementationID remoteImplementationID, final ObjectID objectID, final MetaDataInfo metadataInfo,
                                                                       final SerializedParametersOrReturn params, final Langs sessionLang,
                                                                       final boolean allBackends) {

        // Randomly select a backend to execute
        // TODO Define a better backend selection policy (10 Jul 2013 jmarti)
        final Random rand = new Random();
        final Set<ExecutionEnvironmentID> locations = metadataInfo.getLocations();
        final Set<ExecutionEnvironmentID> backends;
        if (allBackends) {
            backends = locations;
        } else {
            final int index = rand.nextInt(locations.size());
            final ExecutionEnvironmentID backendID = locations
                    .toArray(new ExecutionEnvironmentID[locations.size()])[index];
            backends = new HashSet<>();
            backends.add(backendID);
        }

        SerializedParametersOrReturn resultObj = null;
        for (ExecutionEnvironmentID backendID : backends) {
            // Execute the method
            // Limit retries depending on the current set of replicas of the object
            final int maxRetries = Math.max(Configuration.Flags.MAX_EXECUTION_RETRIES.getShortValue(), locations.size());
            for (short i = 1; i <= maxRetries; i++) {
                try {
                    final ExecutionEnvironment backend = this.getExecutionEnvironmentInfo(backendID, true);
                    final DataServiceAPI dataSrvApi = getExecutionEnvironmentAPI(backend);
                    if (DEBUG_ENABLED) {
                        final Set<ImplementationID> implementationIDs = new HashSet<>();
                        implementationIDs.add(remoteImplementationID);
                        for (final Implementation impl : this.classMgrApi.getInfoOfImplementations(implementationIDs)
                                .values()) {
                            LOGGER.debug("Calling executeImplementation for object {} to  backend {}, method {}",
                                    objectID, backend, impl.getOpNameAndDescriptor());
                        }
                    }

                    resultObj = dataSrvApi.executeImplementation(objectID, remoteImplementationID, params, origSession);
                    break;
                } catch (final DataClayException nbEx) {
                    if (!allBackends && i < maxRetries) {
                        final int index = rand.nextInt(locations.size());
                        backendID = locations.toArray(new ExecutionEnvironmentID[locations.size()])[index];
                        LOGGER.debug("Retrying execution on object {} onto backend {}", objectID, backendID);
                    } else {
                        LOGGER.debug("Aborting executeImplementation procedure", nbEx);
                        throw nbEx;
                    }
                }
            }
        }
        return resultObj;
    }

    /**
     * Method that executes an action in a certain object. This method is called
     * from notification manager. Note that executions from this method have NO
     * session.
     *
     * @param objectID
     *            ID of the object
     * @param sessionID
     *            ID of the session of who triggered action
     * @param params
     *            the serialized parameters for the operation
     * @param opID
     *            id of the operation to be executed
     */
    public void executeNotificationAction(final ObjectID objectID, final SessionID sessionID,
                                          final SerializedParametersOrReturn params, final OperationID opID) {

        // TODO: Maybe we should check session here before waiting Data Service to do so
        // (July 2018 jmarti)

        // Get object metadata
        final MetaDataInfo metadataInfo = getObjectMetadata(objectID);

        // Get implementation information, to serialize parameteres
        final Operation op = this.classMgrApi.getOperationInfo(opID);
        final Map<String, Type> paramTypes = op.getParams();
        final String[] paramsSignatures = new String[paramTypes.size()];
        final MetaClassID[] paramsClassIDs = new MetaClassID[paramTypes.size()];
        int idx = 0;
        for (final Type paramType : paramTypes.values()) {
            paramsSignatures[idx] = paramType.getSignatureOrDescriptor();
            if (paramType instanceof UserType) {
                final UserType uType = (UserType) paramType;
                paramsClassIDs[idx] = uType.getClassID();
            }
            idx++;
        }

        // GET ANY IMPLEMENTATION
        final ImplementationID implementationID = op.getImplementations().get(0).getDataClayID();

        // ------------------ EXECUTE ------------------ //
        executeImplementationInternal(sessionID, implementationID, objectID, metadataInfo, params, Langs.LANG_JAVA,
                false);

    }

    /**
     * Method that executes a filterMethod in a certain object without parameters.
     * This method is called from notification manager. Note that executions from
     * this method have NO session.
     *
     * @param objectID
     *            ID of the object
     * @param sessionID
     *            ID of the session of who triggered action
     * @param params
     *            the serialized parameters for the operation
     * @param opID
     *            id of the operation to be executed
     * @return a boolean value with the result of the execution of the
     *         implementation (all of them, separately)
     */
    public boolean executeFilterMethod(final ObjectID objectID, final SessionID sessionID,
                                       final SerializedParametersOrReturn params, final OperationID opID) {
        boolean filterMethodResult = true;

        // TODO: Maybe we should check session info here instead of waiting to reach
        // Data Service backend checks (July 2018 jmarti)

        final MetaDataInfo metadataInfo = getObjectMetadata(objectID);
        // Get implementation information, to serialize parameteres
        final Operation op = this.classMgrApi.getOperationInfo(opID);
        final Map<String, Type> paramTypes = op.getParams();
        final String[] paramsSignatures = new String[paramTypes.size()];
        final MetaClassID[] paramsClassIDs = new MetaClassID[paramTypes.size()];
        int idx = 0;
        for (final Type paramType : paramTypes.values()) {
            paramsSignatures[idx] = paramType.getSignatureOrDescriptor();
            if (paramType instanceof UserType) {
                final UserType uType = (UserType) paramType;
                paramsClassIDs[idx] = uType.getClassID();
            }
            idx++;
        }

        // GET ANY IMPLEMENTATION
        final ImplementationID implementationID = op.getImplementations().get(0).getDataClayID();

        // ------------------ EXECUTE ------------------ //
        final SerializedParametersOrReturn result = executeImplementationInternal(sessionID, implementationID, objectID,
                metadataInfo, params, Langs.LANG_JAVA, false);
        filterMethodResult = DataClayDeserializationLib.deserializeReturnFilterMethod(result);
        return filterMethodResult;
    }

    @Override
    public boolean isPrefetchingEnabled() {
        return Configuration.Flags.PREFETCHING_ENABLED.getBooleanValue();
    }

    @Override
    public boolean objectExistsInDataClay(final ObjectID objectID) {
        LOGGER.debug("Checking if object {} exists", objectID);
        for (final Tuple<DataServiceAPI, StorageLocation> elem : this.getStorageLocations().values()) {
            if (elem.getFirst().existsInDB(objectID)) {
                LOGGER.debug("Found object {} in storage location {}", objectID, elem.getSecond().toString());
                return true;
            }
        }
        return false;

    }

    @Override
    public int getNumObjects() {
        LOGGER.debug("Getting number of objects in dataClay");
        int numObjs = 0;
        for (final Tuple<DataServiceAPI, StorageLocation> elem : this.getStorageLocations().values()) {
            numObjs += elem.getFirst().getNumObjects();
        }
        LOGGER.debug("Found {} objects in dataClay", numObjs);
        return numObjs;

    }

    /**
     * Get owner of the object
     *
     * @param objectID
     *            ID of the object
     * @return Owner of the object
     */
    public AccountID getOwner(final ObjectID objectID) {
        final MetaDataInfo metadataInfo = getObjectMetadata(objectID);
        return metadataInfo.getOwnerID();
    }

    /**
     * Initialize session of owner's object.
     *
     * @param ownerID
     *            ID of the owner.
     * @return Session ID
     */
    public SessionID initializeSessionAsOwnerOfObject(final AccountID ownerID) {
        final Set<DataSetID> dataSets = new HashSet<>();
        DataSetID dataSetForStore = null;

        // Contracts in which account is applicant
        final Map<ContractID, Contract> contracts = contractMgrApi.getContractIDsOfApplicant(ownerID);
        final Set<ContractID> contractIDs = new HashSet<>(contracts.keySet());

        // Contracts in which account is owner
        final Map<ContractID, Contract> contractsAsProvider = contractMgrApi.getContractIDsOfProvider(ownerID);
        contractIDs.addAll(contractsAsProvider.keySet());

        // DataSets in which Account is applicant
        final Map<DataContractID, DataContract> datacontracts = this.datacontractMgrApi
                .getDataContractIDsOfApplicant(ownerID);
        for (final DataContract dc : datacontracts.values()) {
            dataSets.add(dc.getProviderDataSetID());
            if (dataSetForStore == null) {
                dataSetForStore = dc.getProviderDataSetID();
            }
        }

        // DataSets of the account
        final List<DataSet> allDataSetsOfowner = dataSetMgrApi.getAllDataSetsOfAccount(ownerID);
        for (final DataSet ds : allDataSetsOfowner) {
            dataSets.add(ds.getDataClayID());
            if (dataSetForStore == null) {
                dataSetForStore = ds.getDataClayID();
            }

        }
        final SessionInfo sessInfo = newSessionInternal(ownerID, contractIDs, dataSets, dataSetForStore,
                Langs.LANG_JAVA);
        return sessInfo.getSessionID();
    }

    /**
     * Preface for the getStubs|getBabelStubs. Validate the account and get all
     * interfaces.
     *
     * @param applicantAccountID
     *            The applicant Account
     * @param applicantCredential
     *            The applicant Credential
     * @param contractsIDs
     *            The list of ContractIDs for the stubs to be retrieved.
     * @return A map containing all the required data to proceed to stub generation.
     * @throws RemoteException
     *             if a RemoteException occurs from AccountManager or
     *             SessionManager.
     */
    private Map<MetaClassID, LinkedHashMap<ContractID, Tuple<Contract, Interface>>> getStubsInternalPreface(
            final AccountID applicantAccountID, final PasswordCredential applicantCredential,
            final List<ContractID> contractsIDs) {
        // Validate applicant account
        if (!accountMgrApi.validateAccount(applicantAccountID, applicantCredential, AccountRole.NORMAL_ROLE)) {
            throw new DataClayRuntimeException(ERRORCODE.INVALID_CREDENTIALS);
        }

        // Get info of contracts
        final LinkedHashMap<ContractID, Contract> contractsInfo = contractMgrApi
                .getInfoOfSomeActiveContractsForAccount(contractsIDs, applicantAccountID);

        // Get info of interfaces
        final HashSet<InterfaceID> allInterfacesIDs = new HashSet<>();
        for (final Contract contractInfo : contractsInfo.values()) {
            allInterfacesIDs.addAll(contractInfo.getInterfacesInContract().keySet());
        }
        final Map<InterfaceID, Interface> interfacesInfo = interfaceMgrApi.getInterfacesInfo(allInterfacesIDs);

        // Update Interface with names of class, properties and signatures of operations
        /*
         * for (Interface ifaceInfo : interfacesInfo.values()) { MetaClass classInfo =
         * classMgrApi.getClassInfo(ifaceInfo.getMetaClassID());
         * ifaceInfo.setClassName(classInfo.getName());
         *
         * SortedSet<Property> props = classInfo.getProperties(); Map<PropertyID,
         * String> propsOfClass = new HashMap<PropertyID, String>(); for (Property prop
         * : props) { propsOfClass.put(prop.getID(), prop.getName()); }
         * HashMap<PropertyID, String> propsOfIface = new HashMap<PropertyID, String>();
         * for (PropertyID propID : ifaceInfo.getPropertiesIDs()) {
         * propsOfIface.put(propID, propsOfClass.get(propID)); }
         * ifaceInfo.setPropsNames(propsOfIface);
         *
         *
         * Map<OperationID, String> opsOfClass = new HashMap<OperationID, String>(); for
         * (Operation op : classInfo.getOperations()) { opsOfClass.put(op.getID(),
         * op.getName()); } HashMap<OperationID, String> opsOfIface = new
         * HashMap<OperationID, String>(); for (OperationID opID :
         * ifaceInfo.getOperationsIDs()) { opsOfIface.put(opID, opsOfClass.get(opID)); }
         * ifaceInfo.setOpsSignatures(opsOfIface); }
         */

        // Build structures for each metaclass to be "stubbed"
        final Map<MetaClassID, LinkedHashMap<ContractID, Tuple<Contract, Interface>>> ifacesAndContractsOfClasses = new HashMap<>();
        for (final Entry<ContractID, Contract> curContract : contractsInfo.entrySet()) {
            final ContractID contractID = curContract.getKey();
            final Contract contractInfo = curContract.getValue();

            for (final InterfaceID interfaceID : contractInfo.getInterfacesInContract().keySet()) {
                final Interface interfaceInfo = interfacesInfo.get(interfaceID);
                final MetaClassID metaClassID = interfaceInfo.getMetaClassID();

                // Track info of interfaces and contracts per class
                if (!ifacesAndContractsOfClasses.containsKey(metaClassID)) {
                    ifacesAndContractsOfClasses.put(metaClassID,
                            new LinkedHashMap<ContractID, Tuple<Contract, Interface>>());
                }
                ifacesAndContractsOfClasses.get(metaClassID).put(contractID, new Tuple<>(contractInfo, interfaceInfo));
            }
        }

        return ifacesAndContractsOfClasses;
    }

    @Override
    public Map<String, byte[]> getStubs(final AccountID applicantAccountID,
                                        final PasswordCredential applicantCredential, final Langs language, final List<ContractID> contractsIDs) {
        final Map<MetaClassID, LinkedHashMap<ContractID, Tuple<Contract, Interface>>> ifacesAndContractsOfClasses = getStubsInternalPreface(
                applicantAccountID, applicantCredential, contractsIDs);

        // Generate stub infos
        final Map<MetaClassID, StubInfo> stubInfos = new HashMap<>();
        final Map<String, byte[]> stubsBytesArrays = new HashMap<>();
        for (final Entry<MetaClassID, LinkedHashMap<ContractID, Tuple<Contract, Interface>>> ifacesAndContractsOfClass : ifacesAndContractsOfClasses
                .entrySet()) {
            final MetaClassID metaClassID = ifacesAndContractsOfClass.getKey();
            // Generate the stub info for the current class
            final StubInfo stubInfo = getStubInfoForMetaClass(applicantAccountID, metaClassID,
                    ifacesAndContractsOfClass.getValue());

            stubInfos.put(metaClassID, stubInfo);
        }

        // Generate stubs
        final Map<MetaClassID, Triple<String, byte[], byte[]>> stubAndIncludes = classMgrApi.generateStubs(language,
                stubInfos);

        for (final Entry<MetaClassID, Triple<String, byte[], byte[]>> curStub : stubAndIncludes.entrySet()) {

            final MetaClassID metaClassID = curStub.getKey();
            final Triple<String, byte[], byte[]> triple = curStub.getValue();

            final String className = triple.getFirst();
            final byte[] stubByteArray = triple.getSecond();
            final byte[] aspect = triple.getThird();
            stubsBytesArrays.put(className, stubByteArray);

            // Add aspectj
            if (aspect != null) {
                stubsBytesArrays.put(className + "Aspect", aspect);
            }
            // Add Yaml
            // Generate the Babel stub for the current class, and put it in the returning
            // dictionary
            final String myBabelStub = generateBabelStub(applicantAccountID, metaClassID,
                    ifacesAndContractsOfClasses.get(metaClassID));
            if (myBabelStub != null) {
                final byte[] yamldoc = myBabelStub.getBytes();
                stubsBytesArrays.put(className + "Yaml", yamldoc);
            }
        }

        return stubsBytesArrays;

    }

    @Override
    public byte[] getBabelStubs(final AccountID applicantAccountID, final PasswordCredential applicantCredential,
                                final List<ContractID> contractsIDs) {
        final Map<MetaClassID, LinkedHashMap<ContractID, Tuple<Contract, Interface>>> ifacesAndContractsOfClasses = getStubsInternalPreface(
                applicantAccountID, applicantCredential, contractsIDs);

        // Generate "Babel" stubs
        final Map<String, Object> stubsBytesArrays = new HashMap<>();

        for (final Entry<MetaClassID, LinkedHashMap<ContractID, Tuple<Contract, Interface>>> //
                ifacesAndContractsOfClass : ifacesAndContractsOfClasses.entrySet()) {
            final MetaClassID metaClassID = ifacesAndContractsOfClass.getKey();

            // Get the classname
            final String className = classMgrApi.getClassname(metaClassID);
            // Generate the Babel stub for the current class, and put it in the returning
            // dictionary
            stubsBytesArrays.put(className,
                    generateBabelStub(applicantAccountID, metaClassID, ifacesAndContractsOfClass.getValue()));
        }

        final Yaml yaml = CommonYAML.getYamlObject();
        return yaml.dump(stubsBytesArrays).getBytes();

    }

    @Override
    public String getClassNameForDS(final MetaClassID classID) {
        return classMgrApi.getClassname(classID);
    }

    @Override
    public Tuple<String, String> getClassNameAndNamespaceForDS(final MetaClassID classID) {
        try {
            return classMgrApi.getClassNameAndNamespace(classID);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void registerEventListenerImplementation(final AccountID accountID, final PasswordCredential credential,
                                                    final ECA newEventListener) {
        // Check account
        // TODO: validate account, who is allowed to register it?

        // Advises notification manager
        if (Configuration.Flags.NOTIFICATION_MANAGER_ACTIVE.getBooleanValue()) {
            notificationMgrApi.registerEventListenerImpl(newEventListener);
        } else {
            throw new DataClayException(ERRORCODE.UNEXPECTED_EXCEPTION, "Notification manager is not active", false);
        }

    }

    @Override
    public void adviseEvent(final EventMessage newEvent) {
        if (Configuration.Flags.NOTIFICATION_MANAGER_ACTIVE.getBooleanValue()) {
            notificationMgrApi.adviseEvent(newEvent);
        } else {
            throw new DataClayException(ERRORCODE.UNEXPECTED_EXCEPTION, "Notification manager is not active", false);
        }
    }

    /**
     * @return the metaDataSrvApi
     */
    public MetaDataService getMetaDataSrvApi() {
        return metaDataSrvApi;
    }

    /***** Register Listener *****/

    // ============== PRIVATE METHODS FOR NAMESPACES ==============//

    /**
     * Checks that the specified account is the responsible of the given namespace
     *
     * @param accountID
     *            the account to be checked
     * @param credential
     *            the account credentials
     * @param namespaceID
     *            the id of the namespace
     * @return Success if the account is valid and it is the responsible of the
     *         namespace
     */
    private Namespace checkNamespaceResponsible(final AccountID accountID, final PasswordCredential credential,
                                                final NamespaceID namespaceID) {
        // Validate account
        if (!accountMgrApi.validateAccount(accountID, credential, AccountRole.NORMAL_ROLE)) {
            throw new DataClayRuntimeException(ERRORCODE.INVALID_CREDENTIALS);
        }
        // Check namespace responsible
        return namespaceMgrApi.checkNamespaceResponsibleAndGetInfo(namespaceID, accountID);

    }

    // ============== PRIVATE METHODS FOR DATASETS ==============//

    /**
     * Shared method that checks the responsible of a dataset
     *
     * @param accountID
     *            ID of the account to be checked
     * @param credential
     *            credential of the account
     * @param datasetID
     *            ID of the dataset to be checked
     */
    private void checkDataSetResponsible(final AccountID accountID, final PasswordCredential credential,
                                         final DataSetID datasetID) {
        // Validate account
        if (!accountMgrApi.validateAccount(accountID, credential, AccountRole.NORMAL_ROLE)) {
            throw new DataClayRuntimeException(ERRORCODE.INVALID_CREDENTIALS);
        }
        // Check namespace responsible
        if (!dataSetMgrApi.checkDataSetResponsible(datasetID, accountID)) {
            throw new DataClayRuntimeException(ERRORCODE.BAD_DATASET_RESPONSIBLE);
        }

    }

    // ============== PRIVATE METHODS FOR CLASS MANAGEMENT AND IMPORTS
    // ==============//

    /**
     * Get metaclassID either because it is created in the namespace or imported in
     * it
     *
     * @param namespaceID
     *            ID of the namespace where the class should be present
     * @param className
     *            the name of the classes
     * @return ID of the metaclass
     */
    private MetaClassID getMetaClassID(final NamespaceID namespaceID, final String className) {
        MetaClassID result = null;
        // Check the class is not in use and get the classID (checks the class belongs
        // to the namespace)
        result = classMgrApi.getMetaClassID(namespaceID, className);
        if (result == null) {
            // if class manager cannot obtain the class id in namespaceID check imports
            final Set<ImportedInterface> importedInterfacess = namespaceMgrApi.getImportedInterfaces(namespaceID,
                    className);
            if (importedInterfacess.isEmpty()) {
                throw new DataClayRuntimeException(ERRORCODE.BAD_IMPORTS);
            } else {
                result = importedInterfacess.iterator().next().getClassOfImportID();
            }
        }

        return result;
    }

    // ============== PRIVATE METHODS FOR CONTRACT MANAGEMENT ==============//

    /**
     * Method that validates the provided contract in the context of the given
     * namespace
     *
     * @param contract
     *            the contract to validate
     * @return The info of the interfaces of the contract checked.
     */
    private Map<InterfaceID, Interface> checkContract(final Contract contract) {
        Map<InterfaceID, Interface> infoOfInterfacesInContract = null;
        // Get info of the interfaces for the contract
        infoOfInterfacesInContract = interfaceMgrApi.getInterfacesOfNamespaceInfo(contract.getNamespaceID(),
                new HashSet<>(contract.getInterfacesInContract().keySet()));

        // Checks for every interface
        final Map<InterfaceID, MetaClassID> metaClassesOfInterfaces = new HashMap<>();
        for (final Entry<InterfaceID, Interface> curEntry : infoOfInterfacesInContract.entrySet()) {
            final InterfaceID interfaceID = curEntry.getKey();
            final Interface curInterface = curEntry.getValue();

            // Check there is no previous interface associated with same
            // metaclass
            if (metaClassesOfInterfaces.containsValue(curInterface.getMetaClassID())) {
                throw new DataClayRuntimeException(ERRORCODE.SOME_INTERFACES_WITH_SAME_METACLASS);
            } else {
                metaClassesOfInterfaces.put(interfaceID, curInterface.getMetaClassID());
            }

            // For some strange reason, doing this new was required in order to avoid a
            // misbehaviour
            // Check YAML-related MixedTest.UpToInterfaceTest
            // abarcelo 2014-10-28
            final Map<InterfaceID, InterfaceInContract> interfacesInNewContract = new LinkedHashMap<>(
                    contract.getInterfacesInContract());
            // Update with a """more valid""" Map --??? It Works (TM)
            contract.setInterfacesInContract(interfacesInNewContract);

            // Check the implementations for the operations in the current
            // interface are right
            final InterfaceInContract curIfaceInContract = interfacesInNewContract.get(interfaceID);
            final Map<OperationID, OpImplementations> allOpsImplementations = curIfaceInContract
                    .getAccessibleImplementations();
            if (!allOpsImplementations.keySet().containsAll(curInterface.getOperationsIDs())) {
                throw new DataClayRuntimeException(ERRORCODE.SOME_OPERATION_WITH_NO_ACCESSIBLE_IMPL);
            }
            for (final Entry<OperationID, OpImplementations> curOpImpls : allOpsImplementations.entrySet()) {
                final OperationID operationID = curOpImpls.getKey();
                if (!curInterface.getOperationsIDs().contains(operationID)) {
                    throw new DataClayRuntimeException(ERRORCODE.OPERATION_NOT_IN_INTERFACE);
                }

                final OpImplementations curImpls = curOpImpls.getValue();

                final List<ImplementationID> implementationsOfOp = classMgrApi
                        .getImplementationsOfOperation(operationID);

                if (!implementationsOfOp.contains(curImpls.getLocalImplementationID())) {
                    throw new DataClayRuntimeException(ERRORCODE.IMPLEMENTATION_NOT_EXIST,
                            " Implementation with ID " + curImpls.getLocalImplementationID() + " does not exist",
                            false);
                } else if (!implementationsOfOp.contains(curImpls.getRemoteImplementationID())) {
                    throw new DataClayRuntimeException(ERRORCODE.IMPLEMENTATION_NOT_EXIST,
                            " Implementation with ID " + curImpls.getRemoteImplementationID() + " does not exist",
                            false);
                }
            }
        }

        return infoOfInterfacesInContract;

    }

    /**
     * This method updates the given interfaces of a contract by analyzing their
     * requirements (includes) and returns a merged set of interfaces for such a
     * contract.
     *
     * @param accountID
     *            ID of the account of the contract provider
     * @param credential
     *            Credential of the account
     * @param namespaceID
     *            ID of the namespace the contract provider
     * @param currentInterfacesInContract
     *            the current interfaces for the contract
     * @param infoOfInterfacesInContract
     *            the info of the current for the contract
     * @return Info of the resulting interfaces in contract.
     * @throws RemoteException
     *             if RemoteException occurs in ClassManager
     */
    private Map<InterfaceID, InterfaceInContract> updateInterfacesForTheContract(final AccountID accountID,
                                                                                 final PasswordCredential credential, final NamespaceID namespaceID,
                                                                                 final Map<InterfaceID, InterfaceInContract> currentInterfacesInContract,
                                                                                 final Map<InterfaceID, Interface> infoOfInterfacesInContract) {

        // Init result with current interfaces in contract
        final Map<InterfaceID, InterfaceInContract> objResult = new HashMap<>();
        objResult.putAll(currentInterfacesInContract);

        // Keep the metaclasses of the interfaces in contract
        final Map<MetaClassID, Triple<Interface, InterfaceInContract, MetaClassID>> //
                metaClassesOfInterfacesInContract = new HashMap<>();

        // Start with the current interfaces in contract
        Map<InterfaceID, InterfaceInContract> interfacesInContractToBeAnalyzed = //
                new HashMap<>(currentInterfacesInContract);

        while (!interfacesInContractToBeAnalyzed.isEmpty()) {
            // Analyze includes required for the interfaces in contract and
            // separate
            // considering if they are related with classes created in the
            // namespace or not (imports)
            final Set<MetaClass> importedIncludesInfo = new HashSet<>();
            final Set<MetaClass> nonImportedIncludesInfo = new HashSet<>();

            for (final Entry<InterfaceID, InterfaceInContract> curEntry : interfacesInContractToBeAnalyzed.entrySet()) {
                final InterfaceID curInterfaceID = curEntry.getKey();
                final InterfaceInContract curInterfaceInContract = curEntry.getValue();

                // Info of the current interface in contract
                final Interface curInterface = infoOfInterfacesInContract.get(curInterfaceID);
                final MetaClassID metaClassID = curInterface.getMetaClassID();
                final Set<PropertyID> propertyIDs = curInterface.getPropertiesIDs();
                final Set<OperationID> operationIDs = curInterface.getOperationsIDs();

                // Accessible implementations for current interface
                final Map<OperationID, OpImplementations> accessibleImpls = curInterfaceInContract
                        .getAccessibleImplementations();

                final Map<OperationID, Set<ImplementationID>> operationsAndImpls = //
                        new HashMap<>();
                for (final OperationID curOpID : operationIDs) {
                    final OpImplementations accessibleImplsForCurrentOp = accessibleImpls.get(curOpID);
                    final HashSet<ImplementationID> implsIDs = new HashSet<>();
                    implsIDs.add(accessibleImplsForCurrentOp.getLocalImplementationID());
                    implsIDs.add(accessibleImplsForCurrentOp.getRemoteImplementationID());
                    operationsAndImpls.put(curOpID, implsIDs);
                }

                // Get parent class ID
                final MetaClass curClassInfo = classMgrApi.getClassInfo(metaClassID);
                MetaClassID parentClassID = null;
                if (curClassInfo.getParentType() != null) {
                    parentClassID = curClassInfo.getParentType().getClassID();
                }

                // => CLASS MANAGER
                // Get all the needed "includes" for the current interface
                // in contract
                final Tuple<Set<MetaClass>, Set<MetaClass>> curResult = classMgrApi
                        .getClassIncludesOfOperationsPropsAndImpls(operationsAndImpls, propertyIDs, namespaceID,
                                parentClassID);

                // We have them separated considering if they are includes
                // present in namespace from imported classes
                // or they are classes created in itup
                importedIncludesInfo.addAll(curResult.getFirst());
                nonImportedIncludesInfo.addAll(curResult.getSecond());

                // Index interfaces in contract by metaclass and also with
                // the parent class info
                metaClassesOfInterfacesInContract.put(metaClassID,
                        new Triple<>(curInterface, curInterfaceInContract, parentClassID));
            }

            // Init interfacesInContract to be analyzed in next iteration
            interfacesInContractToBeAnalyzed = new HashMap<>();

            // Set of includes required that refer to classes already
            // created in namespace
            final Map<MetaClassID, MetaClass> setOfclassesRequiredCreatedInNamespace = new HashMap<>();
            for (final MetaClass mclassInfo : nonImportedIncludesInfo) {
                if (!metaClassesOfInterfacesInContract.containsKey(mclassInfo.getDataClayID())) {
                    setOfclassesRequiredCreatedInNamespace.put(mclassInfo.getDataClayID(), mclassInfo);
                }
            }
            if (!setOfclassesRequiredCreatedInNamespace.isEmpty()) {

                final Map<MetaClassID, Triple<Interface, InterfaceInContract, MetaClassID>> //
                        interfacesForClasses = registerReqInterfacesForClasses(accountID, credential, namespaceID,
                        setOfclassesRequiredCreatedInNamespace);

                for (final MetaClassID mclassID : interfacesForClasses.keySet()) {
                    final Triple<Interface, InterfaceInContract, MetaClassID> curInterface = interfacesForClasses
                            .get(mclassID);
                    interfacesInContractToBeAnalyzed.put(curInterface.getFirst().getDataClayID(),
                            curInterface.getSecond());
                    infoOfInterfacesInContract.put(curInterface.getFirst().getDataClayID(), curInterface.getFirst());
                }
            }

            // Set of includes required that are present in namespace due to
            // imported classes
            final Map<MetaClassID, String> setOfIncludesRequiredNotInNamespace = new HashMap<>();
            for (final MetaClass mclassInfo : importedIncludesInfo) {
                if (!metaClassesOfInterfacesInContract.containsKey(mclassInfo.getDataClayID())) {
                    setOfIncludesRequiredNotInNamespace.put(mclassInfo.getDataClayID(), mclassInfo.getName());
                }
            }
            if (!setOfIncludesRequiredNotInNamespace.isEmpty()) {

                final Map<MetaClassID, Triple<Interface, InterfaceInContract, MetaClassID>> //
                        ifacesForImportedIncludes = registerReqMergedInterfacesFromImports(accountID, credential, namespaceID,
                        metaClassesOfInterfacesInContract, setOfIncludesRequiredNotInNamespace);

                for (final MetaClassID mclassID : ifacesForImportedIncludes.keySet()) {
                    final Triple<Interface, InterfaceInContract, MetaClassID> curInterface = ifacesForImportedIncludes
                            .get(mclassID);
                    interfacesInContractToBeAnalyzed.put(curInterface.getFirst().getDataClayID(),
                            curInterface.getSecond());
                    infoOfInterfacesInContract.put(curInterface.getFirst().getDataClayID(), curInterface.getFirst());
                }
            }
        }

        // Update Interfaces In Contract depending on their inheritance
        // relationship
        for (final MetaClassID mclassID : metaClassesOfInterfacesInContract.keySet()) {
            final Triple<Interface, InterfaceInContract, MetaClassID> curInterfaceInContract = metaClassesOfInterfacesInContract
                    .get(mclassID);
            objResult.put(curInterfaceInContract.getFirst().getDataClayID(), curInterfaceInContract.getSecond());
        }

        return objResult;
    }

    /**
     * This method autogenerates the interfaces for a contract related with a
     * specific set of classes created on a specific namespace
     *
     * @param accountID
     *            Account performing the action
     * @param credential
     *            Credential of the account
     * @param namespaceID
     *            the id of the namespace
     * @param setOfClassesRequiredCreatedInNamespace
     *            the set of classes and its related info
     * @return The interfaces for a contract
     * @throws RemoteException
     *             if a RemoteException occurs in InterfaceManager or ClassManager
     */
    private Map<MetaClassID, Triple<Interface, InterfaceInContract, MetaClassID>> registerReqInterfacesForClasses(
            final AccountID accountID, final PasswordCredential credential, final NamespaceID namespaceID,
            final Map<MetaClassID, MetaClass> setOfClassesRequiredCreatedInNamespace) {

        final String namespace = namespaceMgrApi.getNamespaceInfo(namespaceID).getName();
        final String accountName = accountMgrApi.getAccount(accountID).getUsername();
        // Init result
        final Map<MetaClassID, Triple<Interface, InterfaceInContract, MetaClassID>> newInterfacesInContract = //
                new HashMap<>();

        for (final Entry<MetaClassID, MetaClass> curEntry : setOfClassesRequiredCreatedInNamespace.entrySet()) {
            final MetaClassID mclassID = curEntry.getKey();
            final MetaClass currentMclassInfo = curEntry.getValue();

            final Set<String> operationsSignatures = new HashSet<>();
            final Set<OpImplementations> opImpls = new HashSet<>();
            final Map<OperationID, OpImplementations> actualOpImpls = new HashMap<>();
            for (final Operation op : currentMclassInfo.getOperations()) {
                operationsSignatures.add(op.getNameAndDescriptor());
                final OpImplementations opImplem = new OpImplementations(op.getNameAndDescriptor(), 0, 0);
                opImpls.add(opImplem);
                final ImplementationID implID = op.getImplementations().get(0).getDataClayID();
                opImplem.setLocalImplementationID(implID);
                opImplem.setRemoteImplementationID(implID);
                actualOpImpls.put(op.getDataClayID(), opImplem);
            }

            final Set<String> propNames = new HashSet<>();
            for (final Property prop : currentMclassInfo.getProperties()) {
                propNames.add(prop.getName());
            }

            // Register the new Interface for the class
            final Interface newInterface = new Interface(accountName, namespace, namespace, currentMclassInfo.getName(),
                    propNames, operationsSignatures);

            this.newInterface(accountID, credential, newInterface);

            MetaClassID parentID = null;
            if (currentMclassInfo.getParentType() != null) {
                parentID = currentMclassInfo.getParentType().getClassID();
            }

            // Update the interfaces in contract
            final InterfaceInContract interfaceInContract = new InterfaceInContract(newInterface, opImpls);
            interfaceInContract.setAccessibleImplementations(actualOpImpls);
            newInterfacesInContract.put(mclassID, new Triple<>(newInterface, interfaceInContract, parentID));
        }

        return newInterfacesInContract;
    }

    /**
     * @breief Registers the required merged interfaces from the given imports being
     *         used for the interfaces In Contract
     * @param accountID
     *            the account that creates the contract
     * @param credential
     *            Account credentials
     * @param namespaceID
     *            the id for the contract
     * @param metaClassesOfInterfacesInContract
     *            interfaces in contract indexed by class
     * @param setOfIncludesRequiredNotCreatedInNamespaceButImported
     *            the set of classes needed to satisfy the interfaces in contract
     * @return Returns the info of the registered interfaces in contract register
     * @throws RemoteException
     *             if an exception occurs in NamespaceManager, ContractManager or
     *             InterfaceManager
     *
     */
    private Map<MetaClassID, Triple<Interface, InterfaceInContract, MetaClassID>> registerReqMergedInterfacesFromImports(
            final AccountID accountID, final PasswordCredential credential, final NamespaceID namespaceID,
            final Map<MetaClassID, Triple<Interface, InterfaceInContract, MetaClassID>> metaClassesOfInterfacesInContract,
            final Map<MetaClassID, String> setOfIncludesRequiredNotCreatedInNamespaceButImported) {

        // Init result
        final Map<MetaClassID, Triple<Interface, InterfaceInContract, MetaClassID>> newInterfacesInContract = //
                new HashMap<>();

        // Init the already done interfaces in contract
        final Map<MetaClassID, Triple<Interface, InterfaceInContract, MetaClassID>> alreadyDoneInterfacesInContract = //
                new HashMap<>(metaClassesOfInterfacesInContract);

        // => NAMESPACE MANAGER
        // Get imports and check that contain all the requirements
        final Map<MetaClassID, Set<ImportedInterface>> importsInfo = namespaceMgrApi
                .getImportedInterfacesForMetaclasses(namespaceID,
                        new HashSet<>(setOfIncludesRequiredNotCreatedInNamespaceButImported.keySet()));
        for (final Entry<MetaClassID, String> curInclude : setOfIncludesRequiredNotCreatedInNamespaceButImported
                .entrySet()) {
            final MetaClassID classID = curInclude.getKey();
            final String className = curInclude.getValue();
            if (!importsInfo.containsKey(classID) || importsInfo.get(classID).isEmpty()) {
                throw new DataClayRuntimeException(ERRORCODE.CLASS_NOT_IMPORTED,
                        "Class " + className + " not imported in namespace " + namespaceID, false);
            }
        }

        // => CONTRACT MANAGER
        // Get info of the interfaces in contract used for the imports
        final Map<ContractID, HashSet<InterfaceID>> interfacesInContractOfImports = new HashMap<>();
        final Set<InterfaceID> interfacesIDsOfImports = new HashSet<>();

        // For each class...
        for (final Set<ImportedInterface> importsInfoOfClass : importsInfo.values()) {
            // For each import for the class...
            for (final ImportedInterface importInfoOfClass : importsInfoOfClass) {
                final ContractID curContractID = importInfoOfClass.getContractID();
                final InterfaceID curInterfaceID = importInfoOfClass.getInterfaceID();
                if (!interfacesInContractOfImports.containsKey(curContractID)) {
                    final HashSet<InterfaceID> interfacesInCurContractOfImports = new HashSet<>();
                    interfacesInContractOfImports.put(curContractID, interfacesInCurContractOfImports);
                }
                interfacesInContractOfImports.get(curContractID).add(curInterfaceID);
                interfacesIDsOfImports.add(curInterfaceID);
            }
        }
        final Map<ContractID, Tuple<Map<InterfaceID, InterfaceInContract>, Calendar>> ifacesInContractOfImportedIncludes = //
                contractMgrApi.getInfoOfMultipleInterfacesPerActiveContractsForAccount(accountID,
                        interfacesInContractOfImports);

        // => INTERFACE MANAGER
        // Get info of the interfaces used for the imports
        final Map<InterfaceID, Interface> interfacesOfImportedIncludesInfo = interfaceMgrApi
                .getInterfacesInfo(interfacesIDsOfImports);

        // Index includes by class
        final Map<MetaClassID, Map<InterfaceID, InterfaceInContract>> infoOfInterfacesInContractOfImportedIncludesPerClass = //
                new HashMap<>();

        for (final Tuple<Map<InterfaceID, InterfaceInContract>, Calendar> curInterfacesInContract : ifacesInContractOfImportedIncludes
                .values()) {

            for (final Entry<InterfaceID, InterfaceInContract> curEntry : curInterfacesInContract.getFirst()
                    .entrySet()) {
                final InterfaceID interfaceID = curEntry.getKey();
                final InterfaceInContract curInterfaceInContract = curEntry.getValue();

                final MetaClassID mclassID = interfacesOfImportedIncludesInfo.get(interfaceID).getMetaClassID();
                if (!metaClassesOfInterfacesInContract.containsKey(mclassID)) {
                    if (!infoOfInterfacesInContractOfImportedIncludesPerClass.containsKey(mclassID)) {
                        final Map<InterfaceID, InterfaceInContract> aux = new HashMap<>();
                        infoOfInterfacesInContractOfImportedIncludesPerClass.put(mclassID, aux);
                    }
                    infoOfInterfacesInContractOfImportedIncludesPerClass.get(mclassID).put(interfaceID,
                            curInterfaceInContract);
                }
            }
        }

        // Build and register Interfaces and the necesssary info for the
        // contract
        for (final MetaClassID mclassID : infoOfInterfacesInContractOfImportedIncludesPerClass.keySet()) {
            if (!alreadyDoneInterfacesInContract.containsKey(mclassID)) {
                final Map<MetaClassID, Triple<Interface, InterfaceInContract, MetaClassID>> registeredClasses = //
                        buildRegisterInterfacesInContract(accountID, credential, namespaceID, mclassID,
                                alreadyDoneInterfacesInContract, interfacesOfImportedIncludesInfo,
                                infoOfInterfacesInContractOfImportedIncludesPerClass);
                alreadyDoneInterfacesInContract.putAll(registeredClasses);
                newInterfacesInContract.putAll(registeredClasses);
            }
        }
        return newInterfacesInContract;
    }

    /**
     * Registers an interface for the given class by merging the contents of all the
     * interfaces corresponding to the imports
     *
     * @param accountID
     *            Account performing the action
     * @param credential
     *            Credential of the account
     * @param namespaceID
     *            the ID of the namespace where merged interface is created
     * @param currentClassToRegister
     *            the ID of the class of the merged interface
     * @param alreadyDoneInterfacesInContract
     *            the classes that have been already registered with the same
     *            process (for recursive purposes)
     * @param interfacesOfImportedIncludesInfo
     *            the info of the interfaces of imported includes in the namespace
     * @param ifacesInContractOfImportedIncludesPerClass
     *            the info of the interfaces in contract that were used to import
     *            the includes in the namespace (indexed by class)
     * @return the auto-generated interfaces in contract for the class to be
     *         registered (and its parents if needed)
     * @throws RemoteException
     *             if a RemoteException occurs in InterfaceManager or ClassManager
     */
    private Map<MetaClassID, Triple<Interface, InterfaceInContract, MetaClassID>> buildRegisterInterfacesInContract(
            final AccountID accountID, final PasswordCredential credential, final NamespaceID namespaceID,
            final MetaClassID currentClassToRegister,
            final Map<MetaClassID, Triple<Interface, InterfaceInContract, MetaClassID>> alreadyDoneInterfacesInContract,
            final Map<InterfaceID, Interface> interfacesOfImportedIncludesInfo,
            final Map<MetaClassID, Map<InterfaceID, InterfaceInContract>> ifacesInContractOfImportedIncludesPerClass) {

        // Init result
        final Map<MetaClassID, Triple<Interface, InterfaceInContract, MetaClassID>> currentResult = //
                new HashMap<>();

        // Init stuff to be merged
        final Set<String> operationSignatures = new HashSet<>();
        final Set<String> propertiesNames = new HashSet<>();
        MetaClassID parentClassID = null;

        // Merge info of all the interfaces in contract used to import the
        // current class in the namespace
        final Map<InterfaceID, InterfaceInContract> ifacesInContractOfImportedIncludesForClassToBeRegistered = //
                ifacesInContractOfImportedIncludesPerClass.get(currentClassToRegister);

        for (final InterfaceID ifaceID : ifacesInContractOfImportedIncludesForClassToBeRegistered.keySet()) {
            final Interface curInterfaceOfImportedInclude = interfacesOfImportedIncludesInfo.get(ifaceID);

            // Get parent class ID
            final MetaClassID curClassID = curInterfaceOfImportedInclude.getMetaClassID();
            final MetaClass curClassInfo = classMgrApi.getClassInfo(curClassID);

            parentClassID = null;
            if (curClassInfo.getParentType() != null) {
                parentClassID = curClassInfo.getParentType().getClassID();
            }

            // If it is a child class, register parent first if it needs to be
            // registered (RECURSIVELY)
            if (parentClassID != null && ifacesInContractOfImportedIncludesPerClass.containsKey(parentClassID)
                    && !currentResult.containsKey(parentClassID)
                    && !alreadyDoneInterfacesInContract.containsKey(parentClassID)) {
                final Map<MetaClassID, Triple<Interface, InterfaceInContract, MetaClassID>> partialResult = //
                        buildRegisterInterfacesInContract(accountID, credential, namespaceID, parentClassID,
                                alreadyDoneInterfacesInContract, interfacesOfImportedIncludesInfo,
                                ifacesInContractOfImportedIncludesPerClass);
                currentResult.putAll(partialResult);
            }

            // Merge operations and properties for the interface
            operationSignatures.addAll(curInterfaceOfImportedInclude.getOperationsSignatureInIface());
            propertiesNames.addAll(curInterfaceOfImportedInclude.getPropertiesInIface());
        }

        // Merge operations and properties that are enrichments of the class in
        // the namespace
        final Tuple<Set<PropertyID>, Map<OperationID, Set<ImplementationID>>> enrichmentsOfClassInNamespace = classMgrApi
                .getEnrichmentsInNamespaceOfClass(currentClassToRegister, namespaceID);
        for (final PropertyID propID : enrichmentsOfClassInNamespace.getFirst()) {
            final Property prop = classMgrApi.getPropertyInfo(propID);
            propertiesNames.add(prop.getName());
        }
        for (final OperationID opID : enrichmentsOfClassInNamespace.getSecond().keySet()) {
            final Operation op = classMgrApi.getOperationInfo(opID);
            operationSignatures.add(op.getNameAndDescriptor());
        }

        // Select the implementations for the operations of the merged interface
        final Map<OperationID, OpImplementations> opImpls = selectImplementationsForMergedInterface(
                enrichmentsOfClassInNamespace.getSecond(), ifacesInContractOfImportedIncludesForClassToBeRegistered);

        // Register the merged interface for the current include to be
        // registered
        // as the merge of imported interfaces present in namespace for the
        // corresponding class
        final String namespaceOfAccount = namespaceMgrApi.getNamespaceInfo(namespaceID).getName();
        final MetaClass classInfo = classMgrApi.getClassInfo(currentClassToRegister);
        final String accountName = accountMgrApi.getAccount(accountID).getUsername();
        final InterfaceInContract interfaceInContract;
        final Interface newInterface = new Interface(accountName, namespaceOfAccount, classInfo.getNamespace(),
                classInfo.getName(), propertiesNames, operationSignatures);

        this.newInterface(accountID, credential, newInterface);

        final Set<OpImplementations> finalOps = new HashSet<>(opImpls.values());
        interfaceInContract = new InterfaceInContract(newInterface, finalOps);
        interfaceInContract.setAccessibleImplementations(opImpls);
        currentResult.put(currentClassToRegister, new Triple<>(newInterface, interfaceInContract, parentClassID));

        return currentResult;
    }

    /**
     * Select the implementations for operations from those ified in the given
     * interfaces in contract (currently it selects the first OpImplementations that
     * finds for every Operation it parses from the given interfaces in contracts)
     *
     * @param enrichedOperations
     *            the operations created as enrichments of the imported includes
     * @param infoOfInterfacesInContractOfImportedIncludesForClassToBeRegistered
     *            the information of the interfaces in contract related with the
     *            includes of the class accessible via imports
     * @return The selection of accessible implementations for each operation
     */
    private Map<OperationID, OpImplementations> selectImplementationsForMergedInterface(
            final Map<OperationID, Set<ImplementationID>> enrichedOperations,
            final Map<InterfaceID, InterfaceInContract> infoOfInterfacesInContractOfImportedIncludesForClassToBeRegistered) {

        final Map<OperationID, OpImplementations> result = new HashMap<>();

        // TODO Default behaviour is selecting the first available
        // implementation as for local and remote
        // we should discuss this (jmarti 3 Sep 2013)

        // Init opImpls of the operations created as enrichments of the imported
        // includes
        for (final Entry<OperationID, Set<ImplementationID>> entry : enrichedOperations.entrySet()) {
            final OperationID curEnrichmentID = entry.getKey();
            final Operation op = classMgrApi.getOperationInfo(curEnrichmentID);
            final OpImplementations newOpImpls = new OpImplementations(op.getNameAndDescriptor(), 0, 0);
            final ImplementationID implID = op.getImplementations().get(0).getDataClayID();
            newOpImpls.setLocalImplementationID(implID);
            newOpImpls.setRemoteImplementationID(implID);
            result.put(op.getDataClayID(), newOpImpls);
        }

        // Init opImpls of the operations already present from the imported
        // includes
        for (final InterfaceInContract curInterfaceInContract : infoOfInterfacesInContractOfImportedIncludesForClassToBeRegistered
                .values()) {
            final Map<OperationID, OpImplementations> curOpImpls = curInterfaceInContract
                    .getAccessibleImplementations();
            for (final Entry<OperationID, OpImplementations> curEntry : curOpImpls.entrySet()) {
                result.put(curEntry.getKey(), curEntry.getValue());
            }
        }

        return result;
    }

    // ============== PRIVATE METHODS FOR STUBS AND DATA MANAGEMENT ==============//

    /**
     * Return the object locations of an object
     *
     * @param objectID
     *            ID of the obejct
     * @return object locations of given object id
     * @throws RuntimeException
     *             if an exception occurs
     */
    private MetaDataInfo getObjectMetadata(final ObjectID objectID) {
        // Get object locations (replicas)
        final MetaDataInfo metadataInfo = metaDataSrvApi.getObjectMetaData(objectID);
        return metadataInfo;
    }

    /**
     * Checks provided session and returns its info
     *
     * @param sessionID
     *            ID of the session
     * @return The information of the session
     * @throws RemoteException
     *             if a RemoteException occurs from SessionManager
     */
    private SessionInfo getSessionInfo(final SessionID sessionID) {
        // Get session info
        final SessionInfo result = sessionMgrApi.getSessionInfo(sessionID);
        return result;
    }

    /**
     * Method that validates the dataset for store among the datacontracts and
     * calculates the more restrictive end date given all the data contracts.
     *
     * @param dataContractsInfo
     *            Info of the data contracts to be taken into account.
     * @param datasetsInfo
     *            Info of the datasets to be taken into account.
     * @param dataSetForStore
     *            Dataset defined for storing objects.
     * @return Success if the dataset ified for storing objects is among data
     *         contracts.
     */
    private Tuple<DataContractID, Calendar> validateDataSetForStore(
            final Map<DataContractID, DataContract> dataContractsInfo, final Map<String, DataSet> datasetsInfo,
            final DataSetID dataSetForStore) {
        DataContractID dataContractIDforStore = null;
        Calendar endDate = null;
        for (final Entry<DataContractID, DataContract> curDataContractEntry : dataContractsInfo.entrySet()) {
            final DataContractID curDataContractID = curDataContractEntry.getKey();
            final DataContract curDataContract = curDataContractEntry.getValue();
            final DataSetID curDataSetProvider = curDataContract.getProviderDataSetID();
            if (curDataSetProvider.equals(dataSetForStore)) {
                dataContractIDforStore = curDataContractID;
            }
            final Calendar curEndDate = curDataContract.getEndDate();
            if (endDate == null || curEndDate.before(endDate)) {
                endDate = curEndDate;
            }
        }
        if (dataContractIDforStore == null) {
            throw new DataClayRuntimeException(ERRORCODE.DATASET_FOR_STORE_NOT_AMONG_DATACONTRACTS);
        }
        if (endDate.before(Calendar.getInstance())) {
            throw new DataClayRuntimeException(ERRORCODE.APPLIABLE_END_DATE_FOR_SESSION_IS_BEFORE_CURRENT_DATE);
        }
        return new Tuple<>(dataContractIDforStore, endDate);
    }

    /**
     * Method that checks whether a dataset is accessible from the given data
     * contracts
     *
     * @param datasetID
     *            ID of the dataset to be checked
     * @param dataContractsInfo
     *            info of the available data contracts
     */
    private void checkDataSetAmongDataContracts(final DataSetID datasetID,
                                                final Map<DataContractID, SessionDataContract> dataContractsInfo) {
        boolean dataSetFound = false;
        for (final SessionDataContract dataContract : dataContractsInfo.values()) {
            dataSetFound = dataContract.getDataSetOfProvider().equals(datasetID);
            if (dataSetFound) {
                break;
            }
        }
        if (!dataSetFound) {
            throw new DataClayRuntimeException(ERRORCODE.DATASET_NOT_AMONG_DATACONTRACTS);
        }
    }

    /**
     * Retrieve the StubInfo from info of Interfaces and Contracts.
     *
     * @param applicantAccountID
     *            The applicantAccountID for the resulting StubInfo.
     * @param metaClassID
     *            The ID for the MetaClass. All provided Interfaces should be
     *            consistent with it.
     * @param infoOfInterfacesAndContractsOfClass
     *            Information for Interfaces and Contracts.
     * @return The StubInfo for the specified class.
     * @throws RemoteException
     *             thrown by ClassManager
     */
    private StubInfo getStubInfoForMetaClass(final AccountID applicantAccountID, final MetaClassID metaClassID,
                                             final LinkedHashMap<ContractID, Tuple<Contract, Interface>> infoOfInterfacesAndContractsOfClass) {
        if (applicantAccountID == null) {
            throw new DataClayRuntimeException(ERRORCODE.NULL_OR_EMPTY_PARAMETER_EXCEPTION,
                    "Applicant account ID is null (private function)", false);
        } else if (infoOfInterfacesAndContractsOfClass == null) {
            throw new DataClayRuntimeException(ERRORCODE.NULL_OR_EMPTY_PARAMETER_EXCEPTION,
                    "Info of interfaces and contracts of class is null (private function)", false);
        } else if (infoOfInterfacesAndContractsOfClass.isEmpty()) {
            throw new DataClayRuntimeException(ERRORCODE.NULL_OR_EMPTY_PARAMETER_EXCEPTION,
                    "Info of interfaces and contracts of class is empty (private function)", false);
        }

        // Merge of operations and properties
        final Map<String, ImplementationStubInfo> implsInInterfaces = new HashMap<>();
        final Map<String, ImplementationStubInfo> implsInInterfacesByID = new HashMap<>();

        final Map<String, PropertyStubInfo> propertiesInInterfaces = new HashMap<>();

        final List<String> propertyListForStub = new LinkedList<>();

        final MetaClass classInfo = classMgrApi.getClassInfo(metaClassID);

        for (final Entry<ContractID, Tuple<Contract, Interface>> curInfo : infoOfInterfacesAndContractsOfClass
                .entrySet()) {
            final ContractID contractID = curInfo.getKey();
            final Contract contractInfo = curInfo.getValue().getFirst();
            final Interface interfaceInfo = curInfo.getValue().getSecond();
            final InterfaceID interfaceID = interfaceInfo.getDataClayID();

            if (!metaClassID.equals(interfaceInfo.getMetaClassID())) {
                throw new DataClayRuntimeException(ERRORCODE.BAD_INTERFACE,
                        "All interfaces should be defined for the same MetaClass", false);
            }

            // Build the info for the properties of the stub
            final SortedSet<Property> propertiesInStub = new TreeSet<>();
            for (final PropertyID propertyID : interfaceInfo.getPropertiesIDs()) {
                final Property prop = classMgrApi.getPropertyInfo(propertyID);
                propertiesInStub.add(prop);

                if (!propertiesInInterfaces.containsKey(propertyID.toString())) {
                    final PropertyStubInfo psi = new PropertyStubInfo(propertyID, prop.getName(), prop.getType(),
                            prop.getGetterOperationID(), prop.getSetterOperationID(), prop.getNamespace(),
                            prop.getNamespaceID(), prop.getBeforeUpdate(), prop.getAfterUpdate(), prop.getInMaster());
                    propertiesInInterfaces.put(prop.getName(), psi);
                }
            }
            // Now add all the names of properties, sorted
            for (final Property prop : propertiesInStub) {
                propertyListForStub.add(prop.getName());
            }

            // Build the info for the operations of the stub
            for (final OperationID operationID : interfaceInfo.getOperationsIDs()) {
                // TODO: We could think about a better mechanism to select which is the remote
                // implementation
                // for the operations of the stub, now we are getting LAST one (6 Feb 2014
                // dgasull)
                final Map<OperationID, OpImplementations> opsImpls = contractInfo.getInterfacesInContract()
                        .get(interfaceID).getAccessibleImplementations();
                final OpImplementations opImpls = opsImpls.get(operationID);
                final Operation op = classMgrApi.getOperationInfo(operationID);

                // Get info of remote implementation to get responsible
                final HashSet<ImplementationID> implsIDs = new HashSet<>();
                implsIDs.add(opImpls.getRemoteImplementationID());
                final Implementation implInfo = classMgrApi.getInfoOfImplementations(implsIDs)
                        .get(opImpls.getRemoteImplementationID());

                final ImplementationStubInfo implStubInfo = new ImplementationStubInfo(implInfo.getNamespace(),
                        implInfo.getClassName(), op.getNameAndDescriptor(), op.getParams(), op.getParamsOrder(),
                        op.getReturnType(), op.getDataClayID(), opImpls.getLocalImplementationID(),
                        opImpls.getRemoteImplementationID(), contractID, interfaceID, applicantAccountID,
                        implInfo.getNamespaceID(), implInfo.getPosition());
                implsInInterfaces.put(implInfo.getOpNameAndDescriptor(), implStubInfo);
                implsInInterfacesByID.put(implStubInfo.getLocalImplID().toString(), implStubInfo);
                implsInInterfacesByID.put(implStubInfo.getRemoteImplID().toString(), implStubInfo);
            }
        }

        /*
         * // WARNING abarcelo -- Removed the language check because this function
         * should be agnostic // Before, the namespacesIDs were being tracked --and here
         * they were checked.
         *
         * // Get namespaces info Map<NamespaceID, Namespace> namespacesInfo =
         * namespaceMgrApi.getNamespacesInfo(namespacesIDs);
         * checkNamespacesLanguage(namespacesInfo); // TODO maybe the language check
         * should be done on getStubs (for non-Babel scenarios)?
         */

        String nameOfParentClass = null;
        if (classInfo.getParentType() != null) {
            nameOfParentClass = classInfo.getParentType().getTypeName();
        }

        return new StubInfo(classInfo.getNamespace(), classInfo.getName(), nameOfParentClass, applicantAccountID,
                classInfo.getDataClayID(), classInfo.getNamespaceID(), implsInInterfacesByID, implsInInterfaces,
                propertiesInInterfaces, propertyListForStub,
                new HashSet<>(infoOfInterfacesAndContractsOfClass.keySet()));
    }

    /**
     * Retrieve the StubInfo with all implementations and information.
     *
     * @param metaClassID
     *            The ID for the MetaClass. All provided Interfaces should be
     *            consistent with it.
     * @return The StubInfo for the specified class.
     * @throws RemoteException
     *             thrown by ClassManager
     */
    private StubInfo getStubInfoForExecutionClass(final MetaClassID metaClassID) {

        // Merge of operations and properties
        final Map<String, ImplementationStubInfo> implsInInterfaces = new HashMap<>();
        final Map<String, ImplementationStubInfo> implsInInterfacesByID = new HashMap<>();

        final Map<String, PropertyStubInfo> propertiesInInterfaces = new HashMap<>();
        final List<String> propertyListForStub = new LinkedList<>();
        final MetaClass classInfo = classMgrApi.getClassInfo(metaClassID);

        // Build the info for the properties of the stub
        final SortedSet<Property> propertiesInStub = new TreeSet<>();
        for (final Property prop : classInfo.getProperties()) {
            final PropertyID propertyID = prop.getDataClayID();
            propertiesInStub.add(prop);
            if (!propertiesInInterfaces.containsKey(propertyID.toString())) {
                propertiesInInterfaces.put(prop.getName(),
                        new PropertyStubInfo(propertyID, prop.getName(), prop.getType(), prop.getGetterOperationID(),
                                prop.getSetterOperationID(), prop.getNamespace(), prop.getNamespaceID(),
                                prop.getBeforeUpdate(), prop.getAfterUpdate(), prop.getInMaster()));
            }
        }
        // Now add all the names of properties, sorted
        for (final Property prop : propertiesInStub) {
            propertyListForStub.add(prop.getName());
        }

        // Build the info for the operations of the stub
        for (final Operation op : classInfo.getOperations()) {
            for (final Implementation impl : op.getImplementations()) {
                // Get info of remote implementation to get responsible
                final ImplementationStubInfo implStubInfo = new ImplementationStubInfo(impl.getNamespace(),
                        impl.getClassName(), op.getNameAndDescriptor(), op.getParams(), op.getParamsOrder(),
                        op.getReturnType(), op.getDataClayID(), impl.getDataClayID(), impl.getDataClayID(), null, null,
                        null, impl.getNamespaceID(), impl.getPosition());

                // Prepare opNameAndSignature : opName + $$ + numImpl + signature
                final String opNameAndSignatureExec = op.getName() + "$$" + impl.getPosition() + op.getDescriptor();
                implsInInterfaces.put(opNameAndSignatureExec, implStubInfo);
                implsInInterfacesByID.put(implStubInfo.getLocalImplID().toString(), implStubInfo);
                implsInInterfacesByID.put(implStubInfo.getRemoteImplID().toString(), implStubInfo);
            }
        }

        String nameOfParentClass = null;
        if (classInfo.getParentType() != null) {
            // Execution class must include namespace
            nameOfParentClass = classInfo.getNamespace() + "." + classInfo.getParentType().getTypeName();
        }
        return new StubInfo(classInfo.getNamespace(), classInfo.getName(), nameOfParentClass, null,
                classInfo.getDataClayID(), classInfo.getNamespaceID(), implsInInterfacesByID, implsInInterfaces,
                propertiesInInterfaces, propertyListForStub, null);
    }

    /**
     * Method that generates the "Babel" stub (language independent YAML-based)
     * corresponding to the interfaces in contracts provided.
     *
     * @param applicantAccountID
     *            the applicant for the stub
     * @param metaClassID
     *            The ID for the MetaClass. All provided Interfaces should be
     *            consistent with it.
     * @param infoOfInterfacesAndContractsOfClass
     *            info of the contracts related with the class of the stub to be
     *            generated
     * @return The yaml representation of the babel stub. FAIL otherwise.
     */
    private String generateBabelStub(final AccountID applicantAccountID, final MetaClassID metaClassID,
                                     final LinkedHashMap<ContractID, Tuple<Contract, Interface>> infoOfInterfacesAndContractsOfClass) {
        final StubInfo stubInfo = getStubInfoForMetaClass(applicantAccountID, metaClassID,
                infoOfInterfacesAndContractsOfClass);
        final Yaml yaml = CommonYAML.getYamlObject();
        final String babelStubStructure = yaml.dump(stubInfo);
        return babelStubStructure;

    }

    @Override
    public void activateTracing(final int currentAvailableTaskID) {
        LOGGER.info("Extrae activating trace with task ID {}", currentAvailableTaskID);

        synchronized (DataClayExtrae.class) { //All workers could try to do it
            if (DataClayExtrae.extraeTracingIsEnabled()) {
                LOGGER.info("Extrae already enabled");
                return;
            }
            try {
                int nextTaskID = 1;
                if (currentAvailableTaskID != 0) {
                    // A task ID available = 0 means that the client was activated only for dataclay without compss
                    // therefore LogicModule task id is 1
                    nextTaskID = currentAvailableTaskID;
                }

                LOGGER.info("Initializing extrae with next available task id {}", nextTaskID);
                DataClayExtrae.setCurrentAvailableTaskID(nextTaskID);
                DataClayExtrae.initializeExtrae(true);

                // DataServices Paraver traces
                for (final Tuple<DataServiceAPI, ExecutionEnvironment> curApi : this.getExecutionEnvironments(Langs.LANG_JAVA)
                        .values()) {
                    LOGGER.info("Activating Extrae in node {}", curApi.getSecond());
                    curApi.getFirst().activateTracing(DataClayExtrae.getAndIncrementCurrentAvailableTaskID());
                }
                for (final Tuple<DataServiceAPI, ExecutionEnvironment> curApi : this.getExecutionEnvironments(Langs.LANG_PYTHON)
                        .values()) {
                    LOGGER.info("Activating Extrae in node {}", curApi.getSecond());
                    curApi.getFirst().activateTracing(DataClayExtrae.getAndIncrementCurrentAvailableTaskID());

                }
            } catch (final Exception ex) {
                LOGGER.debug("activateTracing error", ex);
                throw ex;
            }
        }
    }

    @Override
    public void deactivateTracing() {
        if (DEBUG_ENABLED) {
            LOGGER.debug("** DEACTIVATING EXTRAE TRACING **");
        }
        // DataServices Paraver traces
        synchronized (DataClayExtrae.class) { //All workers could try to do it
            if (DataClayExtrae.extraeTracingIsEnabled()) { //sanity check
                LOGGER.debug("Starting deactivation of traces");
                for (final Tuple<DataServiceAPI, ExecutionEnvironment> curApi : this.getExecutionEnvironments(Langs.LANG_JAVA)
                        .values()) {
                    LOGGER.debug("Calling deactivate tracing to DS: " + curApi.getSecond());
                    curApi.getFirst().deactivateTracing();
                }
                for (final Tuple<DataServiceAPI, ExecutionEnvironment> curApi : this.getExecutionEnvironments(Langs.LANG_PYTHON)
                        .values()) {
                    LOGGER.debug("Calling deactivate tracing to EE: " + curApi.getSecond());
                    curApi.getFirst().deactivateTracing();
                }
                DataClayExtrae.finishTracing(true);
            }
        }
    }

    @Override
    public void cleanMetaDataCaches() {
        this.metaDataSrvApi.cleanCaches();
    }

    @Override
    public Map<String, byte[]> getTraces() {
        LOGGER.info("Getting Extrae traces");
        synchronized (DataClayExtrae.class) { //All workers could try to do it
            final Map<String, byte[]> allTraces = new HashMap<>();
            if (DataClayExtrae.isGeneratedTraces()) {
                // Call DSs
                for (final Tuple<DataServiceAPI, ExecutionEnvironment> curApi : this.getExecutionEnvironments(Langs.LANG_JAVA)
                        .values()) {
                    LOGGER.debug("Calling get traces to DS: " + curApi.getSecond());
                    allTraces.putAll(curApi.getFirst().getTraces());
                }
                for (final Tuple<DataServiceAPI, ExecutionEnvironment> curApi : this.getExecutionEnvironments(Langs.LANG_PYTHON)
                        .values()) {
                    LOGGER.debug("Calling get traces to DS: " + curApi.getSecond());
                    allTraces.putAll(curApi.getFirst().getTraces());
                }
                allTraces.putAll(DataClayExtrae.getTraces());
            }
            return allTraces;
        }

    }

    /**
     * Wait for all asynchronous request to finish.
     */
    public void waitAndProcessAllAsyncRequests() {
        grpcClient.waitAndProcessAllAsyncRequests();
    }

    @Override
    public void closeManagerDb() {

        this.accountMgrApi.getDbHandler().close();
        this.classMgrApi.getDbHandler().close();
        this.contractMgrApi.getDbHandler().close();
        this.datacontractMgrApi.getDbHandler().close();
        this.dataSetMgrApi.getDbHandler().close();
        this.namespaceMgrApi.getDbHandler().close();
        this.interfaceMgrApi.getDbHandler().close();
        if (this.notificationMgrApi != null) {
            this.notificationMgrApi.getNotificationDB().close();
        }
        this.sessionMgrApi.getDbHandler().close();

    }

    /**
     * Finish cache threads.
     *
     * @if some exception occurs
     */
    public void finishCacheThreads() {
        LOGGER.debug("Finishing client connections...");
        grpcClient.finishClientConnections();
        this.sessionMgrApi.finishCacheThreads();
        storageLocations.clear();
        execEnvironments.clear();
    }

    @Override
    public void closeDb() {
        if (Configuration.Flags.NOTIFICATION_MANAGER_ACTIVE.getBooleanValue()) {
            this.notificationMgrApi.closeManager();
        }
        this.metaDataSrvApi.closeDbHandler();
    }

    /**
     * Get manager db. For testing purposes.
     *
     * @return Manager db.
     */
    public AccountManagerDB getAccountManagerDB() {
        return this.accountMgrApi.getDbHandler();
    }

    /**
     * Get manager db. For testing purposes.
     *
     * @return Manager db.
     */
    public ClassManagerDB getClassManagerDB() {
        return this.classMgrApi.getDbHandler();
    }

    /**
     * Get manager db. For testing purposes.
     *
     * @return Manager db.
     */
    public ContractManagerDB getContractManagerDB() {
        return this.contractMgrApi.getDbHandler();
    }

    /**
     * Get manager db. For testing purposes.
     *
     * @return Manager db.
     */
    public DataContractManagerDB getDataContractManagerDB() {
        return this.datacontractMgrApi.getDbHandler();
    }

    /**
     * Get manager db. For testing purposes.
     *
     * @return Manager db.
     */
    public DataSetManagerDB getDataSetManagerDB() {
        return this.dataSetMgrApi.getDbHandler();
    }

    /**
     * Get manager db. For testing purposes.
     *
     * @return Manager db.
     */
    public NamespaceManagerDB getNamespaceManagerDB() {
        return this.namespaceMgrApi.getDbHandler();
    }

    /**
     * Get manager db. For testing purposes.
     *
     * @return Manager db.
     */
    public InterfaceManagerDB getInterfaceManagerDB() {
        return this.interfaceMgrApi.getDbHandler();
    }

    /**
     * Get manager db. For testing purposes.
     *
     * @return Manager db.
     */
    public NotificationManagerDB getNotificationManagerDB() {
        return this.notificationMgrApi.getNotificationDB();
    }

    /**
     * Get manager db. For testing purposes.
     *
     * @return Manager db.
     */
    public SessionManagerDB getSessionManagerDB() {
        return this.sessionMgrApi.getDbHandler();
    }

    /**
     * Get logic db. For testing purposes.
     *
     * @return metadataservice db.
     */
    public MetaDataServiceDB getMetaDataServiceDb() {
        return this.metaDataSrvApi.getDbHandler();
    }

    /**
     * Get Class manager (for testing purposes)
     *
     * @return ClassManager of this logicModule
     */
    public ClassManager getClassManager() {
        return this.classMgrApi;
    }

    @Override
    public ContractID getContractIDOfDataClayProvider(final AccountID accountID, final PasswordCredential credential) {
        // Validate account
        if (!accountMgrApi.validateAccount(accountID, credential)) {
            throw new DataClayRuntimeException(ERRORCODE.INVALID_CREDENTIALS);
        }
        if (publicIDs.dcPublicContractID == null) {
            throw new DataClayRuntimeException(ERRORCODE.CONTRACT_NOT_EXIST, "No dataClay public contract", false);
        }
        return publicIDs.dcPublicContractID;
    }

    /**
     * @return the shuttingDown
     */
    public boolean isShuttingDown() {
        return shuttingDown;
    }

    /**
     * @param theshuttingDown
     *            the shuttingDown to set
     */
    public void setShuttingDown(final boolean theshuttingDown) {
        this.shuttingDown = theshuttingDown;
    }

}
