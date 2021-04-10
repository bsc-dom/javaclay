
/**
 * @file DataService.java
 * @date Oct 23, 2012
 */
package es.bsc.dataclay.dataservice;

// CHECKSTYLE:OFF

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.util.CheckClassAdapter;

import org.objectweb.asm.ClassReader;

import es.bsc.dataclay.DataClayExecutionObject;
import es.bsc.dataclay.DataClayMockObject;
import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.commonruntime.DataServiceRuntime;
import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.dataservice.api.DataServiceAPI;
import es.bsc.dataclay.dataservice.server.DataServiceSrv;
import es.bsc.dataclay.dbhandler.DBHandler;
import es.bsc.dataclay.dbhandler.DBHandlerConf;
import es.bsc.dataclay.exceptions.DataClayClassNotFoundException;
import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.JavaExecutionException;
import es.bsc.dataclay.exceptions.dataservice.ClassDeploymentException;
import es.bsc.dataclay.exceptions.dataservice.CleanExecutionClassesDirException;
import es.bsc.dataclay.exceptions.dbhandler.DbObjectNotExistException;
import es.bsc.dataclay.exceptions.metadataservice.ObjectNotRegisteredException;
import es.bsc.dataclay.extrae.DataClayExtrae;
import es.bsc.dataclay.logic.api.LogicModuleAPI;
import es.bsc.dataclay.serialization.DataClaySerializable;
import es.bsc.dataclay.serialization.java.util.CollectionWrapper;
import es.bsc.dataclay.serialization.lib.DataClayDeserializationLib;
import es.bsc.dataclay.serialization.lib.DataClaySerializationLib;
import es.bsc.dataclay.serialization.lib.ObjectWithDataParamOrReturn;
import es.bsc.dataclay.serialization.lib.SerializedParametersOrReturn;
import es.bsc.dataclay.storagelocation.StorageLocationService;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.DataClayObjectMetaData;
import es.bsc.dataclay.util.FileAndAspectsUtils;
import es.bsc.dataclay.util.classloaders.DataClayClassLoaderSrv;
import es.bsc.dataclay.util.events.message.EventMessage;
import es.bsc.dataclay.util.events.type.DeletedObjEventType;
import es.bsc.dataclay.util.events.type.EventType;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.SessionID;
import es.bsc.dataclay.util.ids.StorageLocationID;
import es.bsc.dataclay.util.info.VersionInfo;
import es.bsc.dataclay.util.management.classmgr.MetaClass;
import es.bsc.dataclay.util.management.classmgr.Type;
import es.bsc.dataclay.util.management.metadataservice.ExecutionEnvironment;
import es.bsc.dataclay.util.management.metadataservice.MetaDataInfo;
import es.bsc.dataclay.util.management.metadataservice.RegistrationInfo;
import es.bsc.dataclay.util.management.metadataservice.StorageLocation;
import es.bsc.dataclay.util.management.stubs.ImplementationStubInfo;
import es.bsc.dataclay.util.management.stubs.StubInfo;
import es.bsc.dataclay.util.reflection.Reflector;
import es.bsc.dataclay.util.structs.LruCache;
import es.bsc.dataclay.util.structs.Tuple;
import io.grpc.StatusRuntimeException;

// CHECKSTYLE:ON

/**
 * This class is responsible to manage data of the objects stored in the system.
 */
public final class DataService implements DataServiceAPI {

    /**
     * DatService name.
     */
    public final String dsName;

    /**
     * DataService hostname.
     */
    private final String dsHostname;

    /**
     * DataService port.
     */
    private final int dsPort;

    // CHECKSTYLE:OFF
    /**
     * Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger("DataService");

    /**
     * Indicates if debug is enabled.
     */
    private static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

    /**
     * Server instance.
     */
    private final DataServiceSrv ownServer;

    /**
     * DataService StorageLocationID.
     */
    private StorageLocationID storageLocationID;

    /**
     * DataService StorageLocationID.
     */
    private ExecutionEnvironmentID executionEnvironmentID;

    /**
     * Runtime.
     */
    public DataServiceRuntime runtime;

    /**
     * Storage location associated to this service.
     */
    private final StorageLocationService storageLocation;

    /**
     * Lazy task runner.
     */
    private LazyTasksRunner lazyTasksRunner;

    /**
     * Timer.
     */
    private Timer lazyTasksTimer;

    /**
     * Timer to activate paraver.
     */
    private Timer activatePrvTimer;

    /**
     * Instantiates an DataService that uses the DB in the provided path.
     *
     * @param newdsName     DataService name
     * @param newdsHostname DataService host name
     * @param newdsTCPPort  DataService tcp port
     * @param dbHandlerconf Configuration of the DbHandler to use by the manager.
     * @param theownServer  Reference to server instance using this DataService
     *                      implementation.
     * @post Creates a Data Service and initializes the objectDB in the path
     * provided.
     */
    public DataService(final String newdsName, final String newdsHostname, final int newdsTCPPort,
                       final DBHandlerConf dbHandlerconf, final DataServiceSrv theownServer) {
        dsName = newdsName;
        dsHostname = newdsHostname;
        dsPort = newdsTCPPort;
        ownServer = theownServer;
        this.storageLocation = new StorageLocationService(dbHandlerconf);
        this.runtime = new DataServiceRuntime(this);
        initEEInfo();


    }

    /**
     * Initialize caches from persistent files
     */
    private void initEEInfo() {
        try {
            final FileInputStream fis = new FileInputStream(Configuration.Flags.STORAGE_PATH.getStringValue()
                    + File.separatorChar + "java_ds_" + this.dsName + ".info");
            final ObjectInputStream ois = new ObjectInputStream(fis);
            final ExecutionEnvironmentPersistentInfo persInfo = (ExecutionEnvironmentPersistentInfo) ois.readObject();
            this.executionEnvironmentID = persInfo.getExecutionEnvironmentID();
            this.storageLocationID = persInfo.getStorageLocationID();
            LOGGER.info("Initialized from file execution environment ID :" + this.executionEnvironmentID);
            LOGGER.info("Initialized from file storage location ID :" + this.storageLocationID);
            ois.close();
            fis.close();
        } catch (final IOException ioe) {
            this.executionEnvironmentID = new ExecutionEnvironmentID();
            this.storageLocationID = new StorageLocationID();
            LOGGER.info("Initialized execution environment ID :" + this.executionEnvironmentID);
            LOGGER.info("Initialized storage location ID :" + this.storageLocationID);
        } catch (final Exception ex) {
            LOGGER.debug("init EE information error", ex);
            return;
        }

    }

    /**
     * Store EE information.
     */
    public void persistEEInfo() {
        try {
            final FileOutputStream fos = new FileOutputStream(Configuration.Flags.STORAGE_PATH.getStringValue()
                    + File.separatorChar + "java_ds_" + this.dsName + ".info");
            final ObjectOutputStream oos = new ObjectOutputStream(fos);
            final ExecutionEnvironmentPersistentInfo eeInfo = new ExecutionEnvironmentPersistentInfo(
                    this.executionEnvironmentID, this.storageLocationID);
            oos.writeObject(eeInfo);
            oos.close();
            fos.close();
        } catch (final IOException ioe) {
            LOGGER.debug("persist EE info IO error", ioe);
        }

    }

    /**
     * Perform a local initialization, and perform the autoregisterDataService to
     * the LogicModule
     *
     * @param logicModuleHost    Logic Module host
     * @param tcpLogicModulePort Logic Module port
     * @param dataServiceName    DS name
     * @throws Exception If an error occurs during this process
     */
    public void initLocalWithAutoregistration(final String logicModuleHost, final int tcpLogicModulePort,
                                              final String dataServiceName) throws Exception {

        this.runtime.initialize(logicModuleHost, tcpLogicModulePort, dataServiceName);
        DataClayObject.setLib(runtime);
        if (Configuration.Flags.PREFETCHING_ENABLED.getBooleanValue()) {
            lazyTasksRunner = new LazyTasksRunner(this);
            lazyTasksTimer = new Timer();
            lazyTasksTimer.schedule(lazyTasksRunner, 0, Configuration.Flags.PREFETCHING_TASKS_INTERVAL.getLongValue());
        }

        if (activatePrvTimer != null) {
            activatePrvTimer.cancel();
        }

        final LogicModuleAPI lmAPI = this.runtime.getLogicModuleAPI();
        lmAPI.autoregisterSL(this.storageLocationID, dsName, dsHostname, dsPort);
        lmAPI.autoregisterEE(executionEnvironmentID, dsName, dsHostname, dsPort, Langs.LANG_JAVA);
        this.storageLocation.initialize(runtime, this.executionEnvironmentID);
    }

    /**
     * Returns the backendID of this DS.
     *
     * @return ID of the backend assigned to this DS.
     */
    public StorageLocationID getStorageLocationID() {
        return storageLocationID;
    }

    /**
     * Returns the backendID of this DS.
     *
     * @return ID of the backend assigned to this DS.
     */
    public ExecutionEnvironmentID getExecutionEnvironmentID() {
        return executionEnvironmentID;
    }


    @Override
    public void initBackendID(final StorageLocationID newbackendID) {
        this.storageLocationID = newbackendID;
    }

    @Override
    public void associateExecutionEnvironment(final ExecutionEnvironmentID newexecutionEnvironmentID) {
        LOGGER.debug("Associating " + newexecutionEnvironmentID + " to " + this.dsName);
        this.storageLocation.associateExecutionEnvironment(newexecutionEnvironmentID);

    }

    @Override
    public void deployMetaClasses(final String namespaceName, final Map<String, MetaClass> deploymentPack) {
        throw new UnsupportedOperationException("Deploy MetaClass not supported for Java DataService");
    }

    public static byte[] checkGeneratedClass(byte[] classBytes) {
        ClassReader cr = new ClassReader(classBytes);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        CheckClassAdapter.verify(cr, true, pw);

        return classBytes;
    }

    public static void validateClass(ClassReader reader, ClassLoader loader) {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);

        Exception error = null;
        try {
            CheckClassAdapter.verify(reader, loader, true, printWriter);
        } catch (Exception e) {
            error = e;
        }

        String contents = writer.toString();
        if (error != null || contents.length() > 0) {
            throw new IllegalStateException(writer.toString(), error);
        }
    }


    @Override
    public void deployClasses(final String namespaceName, final Map<Tuple<String, MetaClassID>, byte[]> classesToDeploy,
                              final Map<String, byte[]> classesAspects, final Map<String, byte[]> stubYamls) {
        runtime.setCurrentThreadSessionID(new SessionID());

        // Create temporary directories
        final String namespaceDir = Configuration.Flags.EXECUTION_CLASSES_PATH.getStringValue();

		// Create directories if needed
		FileAndAspectsUtils.createDirectory(namespaceDir);
		LOGGER.debug("Received " + classesToDeploy.size() + " classes ");

		for (final Entry<Tuple<String, MetaClassID>, byte[]> curEntry : classesToDeploy.entrySet()) {
			final String className = curEntry.getKey().getFirst();
			final String finalClassName = namespaceName + "." + className;
			LOGGER.debug("Storing class " + finalClassName);
			final byte[] classToDeploy = curEntry.getValue();
			try {
				FileAndAspectsUtils.storeClass(namespaceDir, finalClassName + ".class", classToDeploy);

            } catch (final Exception ex) {
                ex.printStackTrace();
                LOGGER.debug("deployClasses error (while storing .class files)", ex);
                throw new ClassDeploymentException(namespaceName, className, ex.getMessage());
            }
        }



		for (final Entry<Tuple<String, MetaClassID>, byte[]> curEntry : classesToDeploy.entrySet()) {
			final String className = curEntry.getKey().getFirst();
			final String finalClassName = namespaceName + "." + className;
			final byte[] classToDeploy = curEntry.getValue();
			//  Verify class
			LOGGER.debug("Checking class " + finalClassName);
			checkGeneratedClass(classToDeploy);
			DataClayClassLoaderSrv.getClass(finalClassName);
		}

        // Store yamls into final path
        for (final Entry<String, byte[]> curEntry : stubYamls.entrySet()) {
            final String className = curEntry.getKey();
            final String finalClassName = namespaceName + "." + className;
            final byte[] yamlToStore = curEntry.getValue();
            try {
                FileAndAspectsUtils.storeClass(namespaceDir, finalClassName + "Yaml.yaml", yamlToStore);
            } catch (final Exception ex) {
                LOGGER.debug("deployClasses error (while storing .yaml files)", ex);
                throw new ClassDeploymentException(namespaceName, className, ex.getMessage());
            }
        }
    }

    @Override
    public void enrichClass(final String namespaceName, final String className, final byte[] classToDeploy,
                            final byte[] classAspects, final byte[] stubYaml) {

        try {

            final String finalClassName = namespaceName + "." + className;

            final Tuple<File, String> targetDirAndClassFileName = getTargetDirAndClassFileName(finalClassName);
            final File targetDir = targetDirAndClassFileName.getFirst();
            final String classFileName = targetDirAndClassFileName.getSecond();
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            final File classFile = new File(targetDir.getAbsolutePath() + File.separatorChar + classFileName);
            // Update with the new one (e.g. when enriching)
            if (classFile.exists()) {
                classFile.delete();
            }

            // Create temporary directories
            final String namespaceDir = Configuration.Flags.EXECUTION_CLASSES_PATH.getStringValue();

            FileAndAspectsUtils.storeClass(namespaceDir, finalClassName + ".class", classToDeploy);

            // Replace YAML
            try {
                FileAndAspectsUtils.storeClass(namespaceDir, finalClassName + "Yaml.yaml", stubYaml);
            } catch (final Exception ex) {
                LOGGER.debug("enrichClass error (while storing .yaml file)", ex);
                throw new ClassDeploymentException(namespaceName, className, ex.getMessage());
            }

            // Reload all class loaders containing the class
            DataClayClassLoaderSrv.reloadClassLoader(namespaceName);

        } catch (final Exception ex) {
            LOGGER.debug("deployClasses error", ex);
            throw new ClassDeploymentException(namespaceName, className, ex.getMessage());
        }
    }

    /**
     * Returns the targetdir and the file name of the given class
     *
     * @param className name of the class
     * @return A tuple with the directory where the class resides and the actual
     * file name.
     */
    private Tuple<File, String> getTargetDirAndClassFileName(final String className) {
        String packageName = "";
        String classFileName;
        if (className.contains(".")) {
            packageName = className.substring(0, className.lastIndexOf("."));
            classFileName = className.substring(className.lastIndexOf(".") + 1, className.length());
        } else {
            classFileName = className;
        }
        classFileName += ".class";

        final String dirs = packageName.replace('.', File.separatorChar);
        final String execDir = Configuration.Flags.EXECUTION_CLASSES_PATH.getStringValue();
        final File targetDir = new File(execDir + File.separatorChar + dirs);
        return new Tuple<>(targetDir, classFileName);
    }

    @Override
    public ObjectID newPersistentInstance(final SessionID sessionID, final MetaClassID classID,
                                          final ImplementationID implementationID, final Map<MetaClassID, byte[]> ifaceBitMaps,
                                          final SerializedParametersOrReturn params) {

        runtime.setCurrentThreadSessionID(sessionID);
        if (Configuration.mockTesting) {
            DataClayMockObject.setCurrentThreadLib(this.runtime);
        }
        final Class<?> clazz = DataClayClassLoaderSrv.getClass(classID);

        final StubInfo stubInfo = DataClayObject.getStubInfoFromClass(clazz.getName());
        final ImplementationStubInfo execInfo = stubInfo.getImplementationByID(implementationID.toString());
        final Map<String, Type> argTypes = execInfo.getParams();
        final List<String> argOrders = execInfo.getParamsOrder();

        // Get class from signatures.
        Thread.currentThread().setContextClassLoader(DataClayClassLoaderSrv.execEnvironmentClassLoader);
        final Class<?>[] parameterTypes = new Class<?>[argTypes.size()];
        for (int i = 0; i < argTypes.size(); ++i) {
            final String typeSignature = argTypes.get(argOrders.get(i)).getSignatureOrDescriptor();
            parameterTypes[i] = Reflector.getClassFromSignatureAndArray(typeSignature,
                    DataClayClassLoaderSrv.execEnvironmentClassLoader);
        }

        // ============================ DESERIALIZE PARAMETERS
        // ============================ //
        // Since wrappers of parameters depend on the instance of the class and
        // the method cannot be static (or yes?) so we create a foo instance.
        final DataClayObject fooInstance = DataClayClassLoaderSrv.newInstance(classID, new ObjectID()); // just for
        // deser.
        final Object[] loadedparams = this.runtime.deserializeParams(fooInstance, ifaceBitMaps, implementationID,
                params);

        // ============================ NEW INSTANCE ============================ //
        DataClayObject instance = null;
        try {
            instance = (DataClayObject) clazz.getConstructor(parameterTypes).newInstance(loadedparams);
        } catch (final Exception ex) {
            LOGGER.debug("newPersistentInstance -> newInstance error", ex);
            throw new JavaExecutionException(ex);
        }

        // ============================ MAKE PERSISTENT ============================ //
        instance.makePersistent(true, this.executionEnvironmentID);

        runtime.removeCurrentThreadSessionID();
        if (Configuration.mockTesting) {
            DataClayMockObject.removeCurrentThreadLib();
        }
        return instance.getObjectID();

    }

    /**
     * Update hints in serialized objects provided to use current backend id
     * @param objects serialized objects to update
     */
    private void updateHintsToCurrentExecEnv(List<ObjectWithDataParamOrReturn> objects) {
        // NOTE: update hints of objects since they come from other backends in this call
        // Prepare hints to update
        Map<ObjectID, ExecutionEnvironmentID> hintsMapping = new HashMap<>();
        for (final ObjectWithDataParamOrReturn object : objects) {
            final ObjectID objectID = object.getObjectID();
            hintsMapping.put(objectID, this.executionEnvironmentID);
        }
        // Update hints
        for (final ObjectWithDataParamOrReturn object : objects) {
            final ObjectID objectID = object.getObjectID();
            // Set object's hint to current location
            final DataClayObjectMetaData metadata = object.getMetaData();
            metadata.modifyHints(hintsMapping);
            // make persistent - session references
            this.runtime.addSessionReference(objectID);
        }
    }

    @Override
    public void storeObjects(final SessionID sessionID,
                             final List<ObjectWithDataParamOrReturn> objects,
                             final boolean moving, final Set<ObjectID> idsWithAlias) {
        if (DEBUG_ENABLED) {
            LOGGER.debug("[==Store==] Storing " + objects.size() + " objects.");
            for (ObjectWithDataParamOrReturn objectWithDataParamOrReturn : objects) {
                LOGGER.debug("[==Store==] Storing: " + objectWithDataParamOrReturn);
            }
        }
        runtime.setCurrentThreadSessionID(sessionID);
        // NOTE: update hints of objects since they come from other backends in this call
        // Prepare hints to update
        this.updateHintsToCurrentExecEnv(objects);

        storeInMemory(sessionID, objects);

        if (moving) {

            // If moving and object that EXISTS in the ObjectsMap as a proxy means:
            // - The object is a Weak Proxy and we must deserialize data on it.
            // Why?
            // - If there is a proxy present in the ObjectsMap, it means it is a weak proxy,
            // and
            // we are storing an object into a DS that had the object once (and therefore it
            // turned to a
            // weak proxy).
            // A weak proxy, when is GC, it's removed and new instances pointing to the
            // moved object
            // will use the traditional mechanism of 'remote calls'. Object not in Map ->
            // remote call.

            for (final ObjectWithDataParamOrReturn object : objects) {
                final ObjectID objectID = object.getObjectID();
                DataClayExecutionObject instance = null;
                try {
                    instance = this.runtime.getFromHeap(objectID);
                    // Corner case: Client sends parameter to turn in proxy. DS1 has Proxy + Param
                    // obj. but
                    // param obj is actually a persistent object in DS2.
                    // There is a move from DS2 to DS1, the proxy is not saved in map by default
                    // (except weak)
                    // but the parameter is still there. So proxy flag is false but WE CAN USE THE
                    // OBJECT IN MEMORY
                    // :)

                    ///// NORMAL WEAK PROXY
                    if (instance != null && !instance.isLoaded()) {
                        if (DEBUG_ENABLED) {
                            LOGGER.debug("[==Store==] Found weak proxy for " + objectID
                                    + "during a move. Loading object and turning it into normal object");
                        }

                        runtime.deserializeDataIntoInstance(instance, object, null);
                    }

                } catch (final DbObjectNotExistException ex) {
                    LOGGER.debug("storeObjects error, not in db anymore", ex);
                    // TODO: if not in DB anymore due to a delete, what to do?
                } finally {
                    // Remove metadata from cache in case this dataservice has information that the
                    // object is in another.
                    runtime.removeObjectMetadataFromCache(objectID);
                }

            }
        }

        // FIXME: sessionID must have a counter in structure or remove entry once a
        // thread is removed (dgasull 2018)
        // clientLib.removeSessionIDForThread();
        // DataClayObject.removeLibForThread();

        if (DEBUG_ENABLED) {
            LOGGER.debug("[==Store==] End of Storing objects");
        }
    }

    /**
     * @param sessionID        ID of session
     * @param objectsToPersist objects to deserialize into heap
     * @return Deserialized instances
     * @brief Deserialize and load the data of objects provided into heap
     */
    private Object[] storeInMemory(final SessionID sessionID, final List<ObjectWithDataParamOrReturn> objectsToPersist) {

        final Map<MetaClassID, byte[]> ifaceBitMaps = null; // TODO: get for current session

        // Deserialize objects following same design as volatile but without
        // implementation ID (null) and
        // no instance to execute.
        return this.runtime.deserializeIntoHeap(ifaceBitMaps, objectsToPersist);

    }

    @Override
    public void makePersistent(final SessionID sessionID, final List<ObjectWithDataParamOrReturn> objectsToPersist) {
        try {

            if (DEBUG_ENABLED) {
                LOGGER.debug("[==Serialization==] Received serialized objects: " + objectsToPersist);
            }

            // create lots of objects here and stash them somewhere
            runtime.setCurrentThreadSessionID(sessionID);
            if (DEBUG_ENABLED) {
                LOGGER.debug("[==Make Persistent==] ** Starting make persistent **");

            }
            storeInMemory(sessionID, objectsToPersist);
            if (DEBUG_ENABLED) {
                LOGGER.debug("[==Make Persistent==] ** End of make persistent **");
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            LOGGER.debug(" Make persistent got exception", ex);
            throw ex;
        } finally {
            if (Configuration.mockTesting) {
                DataClayMockObject.removeCurrentThreadLib();
            }
            runtime.removeCurrentThreadSessionID();

        }

    }

    @Override
    public void federate(final SessionID sessionID, final ObjectID objectID,
                  final ExecutionEnvironmentID externalExecutionEnvironmentID,
                  final boolean recursive) {
        try {

            // create lots of objects here and stash them somewhere
            runtime.setCurrentThreadSessionID(sessionID);
            LOGGER.debug("----> Starting federation of " + objectID);

            // Get the original object.
            final Set<ObjectID> objectIDs = new HashSet<>();
            objectIDs.add(objectID);
            final List<ObjectWithDataParamOrReturn> serializedObjs = getObjects(sessionID, objectIDs, new HashSet<ObjectID>(), recursive, externalExecutionEnvironmentID,
                    1);

            // Store it in destination backend
            // TODO: check that current dataClay/EE has permission to federate the object (refederation use-case)
            // delegation mechanism?
            final DataServiceAPI dataServiceApi = runtime.getRemoteExecutionEnvironment(externalExecutionEnvironmentID);
            dataServiceApi.notifyFederation(sessionID, serializedObjs);

        } catch (final Exception ex) {
            LOGGER.debug(" Federate got exception", ex);
            throw ex;
        } finally {
            //Do not remove for function called inside methods
            //runtime.removeCurrentThreadSessionID();
        }
        LOGGER.debug("<---- Finished federation of " + objectID);
    }

    @Override
    public void notifyFederation(final SessionID sessionID, final List<ObjectWithDataParamOrReturn> objectsToPersist) {

        try {

            // create lots of objects here and stash them somewhere
            runtime.setCurrentThreadSessionID(sessionID);
            LOGGER.debug("----> Notified federation");


            // if objects received have alias, they must be registered
            List<RegistrationInfo> regInfos = new ArrayList<>();
            for (ObjectWithDataParamOrReturn objectWithDataParamOrReturn: objectsToPersist) {
                ObjectID objectID = objectWithDataParamOrReturn.getObjectID();
                DataClayObjectMetaData metaData = objectWithDataParamOrReturn.getMetaData();
                if (metaData.getAlias() != null) {
                    MetaClassID classID = objectWithDataParamOrReturn.getClassID();
                    // FIXME: session must be null since it does not exist in current dataClay
                    final RegistrationInfo regInfo = new RegistrationInfo(objectID, classID,
                            null, null, metaData.getAlias());
                    // TODO objectID must not be replaced here by new one created?
                    regInfos.add(regInfo);
                }
            }
            if (!regInfos.isEmpty()) {
                // LogicModule notify GCs about new alias
                this.runtime.getLogicModuleAPI().registerObjects(regInfos,
                        (ExecutionEnvironmentID) this.executionEnvironmentID, Langs.LANG_JAVA);
            }


            final Object[] federatedInstances = storeInMemory(sessionID, objectsToPersist);
            for (final Object federatedInstance : federatedInstances) {
                final DataClayObject federatedDataClayObj = (DataClayObject) federatedInstance;
                try {
                    federatedDataClayObj.whenFederated();
                } catch (final Exception e) {
                    if (DEBUG_ENABLED) {
                        LOGGER.debug("[==Federate==] Caugh exception during whenFederated: ", e);
                    }
                }
            }
            LOGGER.debug("<---- Finished notification of federation");


        } catch (final Exception ex) {
            LOGGER.debug(" Notify federation got exception", ex);
            throw ex;
        } finally {
            runtime.removeCurrentThreadSessionID();
        }

    }

    @Override
    public void unfederate(final SessionID sessionID, final ObjectID objectID,
                         final ExecutionEnvironmentID externalExecutionEnvironmentID,
                         final boolean recursive) {

        // TODO: redirect unfederation to owner if current dataClay is not the owner, check origLoc belongs to current dataClay
        try {
            LOGGER.debug("----> Starting unfederation of {} from {}", objectID, externalExecutionEnvironmentID);
            runtime.setCurrentThreadSessionID(sessionID);

            // Get the original object.
            final Set<ObjectID> objectIDs = new HashSet<>();
            objectIDs.add(objectID);
            final List<ObjectWithDataParamOrReturn> serializedObjs = getObjects(sessionID, objectIDs,
                    new HashSet<ObjectID>(), recursive, externalExecutionEnvironmentID, 2);

            Map<ExecutionEnvironmentID, Set<ObjectID>> unfederatePerBackend = new HashMap<>();
            for (ObjectWithDataParamOrReturn objectWithDataParamOrReturn : serializedObjs) {
                Set<ExecutionEnvironmentID> replicaLocs = objectWithDataParamOrReturn.getMetaData().getReplicaLocations();
                for (ExecutionEnvironmentID replicaLoc : replicaLocs) {
                    ExecutionEnvironment execEnv = this.runtime.getExecutionEnvironmentInfo(replicaLoc);
                    if (!execEnv.getDataClayInstanceID().equals(this.runtime.getDataClayID())) {
                        if (externalExecutionEnvironmentID != null && !replicaLoc.equals(externalExecutionEnvironmentID)) {
                            continue;
                        }
                        Set<ObjectID> objsInBackend = unfederatePerBackend.get(replicaLoc);
                        if (objsInBackend == null) {
                            objsInBackend = new HashSet<>();
                            unfederatePerBackend.put(replicaLoc, objsInBackend);
                        }
                        objsInBackend.add(objectWithDataParamOrReturn.getObjectID());
                    }
                }
            }
            for (Entry<ExecutionEnvironmentID, Set<ObjectID>> curEntry : unfederatePerBackend.entrySet()) {
                final DataServiceAPI dataServiceApi = runtime.getRemoteExecutionEnvironment(curEntry.getKey());
                dataServiceApi.notifyUnfederation(sessionID, curEntry.getValue());
            }

        } catch (final Exception ex) {
            LOGGER.debug(" Unfederate got exception", ex);
            throw ex;
        } finally {
            //Do not remove for function called inside methods
            //runtime.removeCurrentThreadSessionID();
        }
        LOGGER.debug("<---- Finished unfederation of " + objectID);
    }

    @Override
    public void notifyUnfederation(final SessionID sessionID, final Set<ObjectID> objectIDs) {
        try {
            // create lots of objects here and stash them somewhere
            if (sessionID != null) {
                runtime.setCurrentThreadSessionID(sessionID);
            }
            LOGGER.debug("----> Starting notification of unfederation of " + objectIDs);
            for (final ObjectID objectID : objectIDs) {
                final DataClayObject instance = runtime.getOrNewInstanceFromDB(objectID, true);
                instance.whenUnfederated();
                instance.setOriginLocation(null);
                if (instance.getAlias() != null) {
                    instance.setAlias(null);
                    try {
                        runtime.deleteAlias(instance.getAlias());
                    } catch (final Exception aliasNotExists) {
                        // ignore if still not registered
                    }
                }

            }
        } catch (final Exception ex) {
            LOGGER.debug("Notify unfederate got exception", ex);
            throw ex;
        } finally {
            runtime.removeCurrentThreadSessionID();

        }
        LOGGER.debug("<---- Finished notification of unfederation of " + objectIDs);

    }

    @Override
    public SerializedParametersOrReturn executeImplementation(final ObjectID objectID, final ImplementationID implID,
                                                              final SerializedParametersOrReturn params, final SessionID sessionID) {

        try {
            // create lots of objects here and stash them somewhere
            if (sessionID != null) {
                runtime.setCurrentThreadSessionID(sessionID);
            }
            if (DEBUG_ENABLED) {
                LOGGER.debug("[==Execution==] ** New execution! ** Executing in object " + objectID
                        + " the implementation " + implID);

            }

            // =================================== GET INSTANCE
            // ===================================//
            // We must instantiate an object of the execution class

            if (DEBUG_ENABLED) {
                LOGGER.debug("[==Execution==] Going to get or create instance for object with id " + objectID);
            }
            final DataClayObject instance = runtime.getOrNewInstanceFromDB(objectID, true);
            if (Configuration.Flags.CHECK_SESSION.getBooleanValue()) {
                this.runtime.checkSession(instance.getDataSetID(), sessionID);
            }

            if (DEBUG_ENABLED) {
                LOGGER.debug("[==Execution==] Deserializing parameters for execution of object with id " + objectID
                        + " and implementation " + implID);
            }

            final Map<MetaClassID, byte[]> ifaceBitMaps = null; // TODO: get for current session

            // ============================ DESERIALIZE PARAMETERS
            // ============================ //
            final Object[] loadedparams = this.runtime.deserializeParams(instance, ifaceBitMaps, implID, params);

            if (DEBUG_ENABLED) {
                LOGGER.debug("[==Execution==] Running method for object with id " + objectID + " and implementation "
                        + implID);
            }

            // ============================= EXECUTE =========================== //
            final Object result = this.runImplementation(instance, implID, loadedparams);

            if (DEBUG_ENABLED) {
                LOGGER.debug("[==Execution==] End of running method for object with id " + objectID
                        + " and implementation " + implID);
            }

            // =================== SERIALIZE RESULT ===================//
            if (result == null) {
                if (DEBUG_ENABLED) {
                    LOGGER.debug("[==Execution==] Returning NULL or nothing for execution of object with id " + objectID
                            + " and implementation " + implID);
                }
                return null;
            } else {
                if (DEBUG_ENABLED) {
                    LOGGER.debug("[==Execution==] Serializing return for execution of object with id " + objectID
                            + " and implementation " + implID);
                }
                return this.runtime.serializeReturn(instance, ifaceBitMaps, implID, result);
            }
        } catch (final StatusRuntimeException sterr) {
            LOGGER.debug("[==Execution==] Error at execution of object with id {} and implementation {}", objectID,
                    implID);
            LOGGER.debug("executeImplementation got a StatusRuntimeException", sterr);
            throw sterr;
        } catch (final DataClayException dbe) {
            LOGGER.debug("[==Execution==] Error at execution of object with id {} and implementation {}", objectID,
                    implID);
            LOGGER.debug("executeImplementation got a native DataClayException", dbe);
            dbe.printStackTrace();
            throw dbe;
        } catch (final Exception ex) {
            LOGGER.debug("[==Execution==] Error at execution of object with id {} and implementation {}", objectID,
                    implID);
            LOGGER.debug("executeImplementation got a native DataClayException", ex);
            throw new JavaExecutionException(ex);
        } catch (final Error err) {
            LOGGER.debug("executeImplementation ``true'' error", err);
            throw new JavaExecutionException(err);
        } finally {
            runtime.removeCurrentThreadSessionID();
            if (DEBUG_ENABLED) {
                LOGGER.debug("[==Execution==] ** End of execution ** End of execution of object with id " + objectID
                        + " and implementation " + implID);
            }
        }

    }

    /**
     * This function executes the method specified
     *
     * @param instance Instance in which to execute
     * @param implID   Information about the operation to execute
     * @param params   Parameter values used while invoking the operation
     * @return Serialized operation result.
     */
    public Object runImplementation(final DataClayObject instance, final ImplementationID implID,
                                    final Object[] params) {
        // =================== EXECUTION ===================//
        // In case we change namespace (executeInternal called from commonlib)
        final Object result = instance.run(implID, params);
        return result;

    }

    @Override
    public void synchronize(final SessionID sessionID, final ObjectID objectID, final ImplementationID implID,
                                    final SerializedParametersOrReturn params,
                                    final ExecutionEnvironmentID callingBackend) {
        LOGGER.debug("--> Synchronize started for object {} and calling backend {}", objectID, callingBackend);

        // first update field
        this.executeImplementation(objectID, implID, params, sessionID);

        // update source
        final DataClayObject instance = runtime.getOrNewInstanceFromDB(objectID, true);
        ExecutionEnvironmentID originalLocation = instance.getOriginLocation();
        if (originalLocation != null) {
            if (callingBackend == null || !originalLocation.equals(callingBackend)) {
                LOGGER.debug("--> Synchronize: calling object {} original location {} to synchronize", objectID, originalLocation);
                final DataServiceAPI dataServiceApi = runtime.getRemoteExecutionEnvironment(originalLocation);
                dataServiceApi.synchronize(sessionID, objectID, implID, params, this.executionEnvironmentID);
            }
        }

        Set<ExecutionEnvironmentID> replicaLocations = instance.getReplicaLocations();
        if (replicaLocations != null) {
            for (ExecutionEnvironmentID replicaLocation: replicaLocations) {
                if (replicaLocation != null) {
                    if (callingBackend == null || !replicaLocation.equals(callingBackend)) {
                        LOGGER.debug("--> Synchronize: calling object {} replica location {} to synchronize", objectID, replicaLocation);
                        final DataServiceAPI dataServiceApi = runtime.getRemoteExecutionEnvironment(replicaLocation);
                        dataServiceApi.synchronize(sessionID, objectID, implID, params, this.executionEnvironmentID);
                    }
                }
            }
        }
        LOGGER.debug("<-- Finished synchronize for object {} and calling backend {}", objectID, callingBackend);


    }

    /**
     * Update all objects in memory (or store them if new). Function called at
     * shutdown.
     */
    public void shutdownUpdate() {
        try {
            // =========== Close DataClayGC service ================ //
            if (DEBUG_ENABLED) {
                LOGGER.debug("[==Shutdown==] Shutdown DataClayGC (it can take a while)... ");
            }

            if (Configuration.Flags.PREFETCHING_ENABLED.getBooleanValue()) {
                // Lazy tasks shutdown
                this.lazyTasksTimer.cancel();
            }

            // =========== Update objects ================ //

            if (DEBUG_ENABLED) {
                LOGGER.debug("[==Shutdown==] Wait pending asynchronous (it can take a while)... ");
            }

            if (Configuration.mockTesting) {
                DataClayMockObject.setCurrentThreadLib(this.runtime);
            }

            // Wait for all async requests.
            runtime.waitForAsyncRequestToFinish();

            // FLUSH all objects
            runtime.flushAll();

            // shutdown GC threads
            this.storageLocation.shutDownGarbageCollector();

            // shutdown storage location
            this.storageLocation.closeDbHandler(this.executionEnvironmentID);

            if (Configuration.mockTesting) {
                DataClayMockObject.removeCurrentThreadLib();
            }

        } catch (final Exception ex) {
            LOGGER.debug("shutdownUpdate error", ex);
        }
    }

    @Override
    public void upsertObjects(final SessionID sessionID, final List<ObjectWithDataParamOrReturn> objectIDsAndBytes) {

        runtime.setCurrentThreadSessionID(sessionID);
        try {
            final List<ObjectWithDataParamOrReturn> objectsInOtherBackend = new ArrayList<>();
            final List<ObjectWithDataParamOrReturn> updatedObjectsHere = new ArrayList<>();

            // To check for replicas
            for (final ObjectWithDataParamOrReturn curEntry : objectIDsAndBytes) {

                final ObjectID objectID = curEntry.getObjectID();
                if (DEBUG_ENABLED) {
                    LOGGER.debug("[==Upsert==] Updated or inserted object " + objectID);
                }
                try {
                    // Update bytes at memory object
                    if (DEBUG_ENABLED) {
                        LOGGER.debug("[==Upsert==] Getting/Creating instance from upsert with id " + objectID);
                    }
                    final DataClayExecutionObject instance = runtime.getOrNewInstanceFromDB(objectID, false);
                    runtime.deserializeDataIntoInstance(instance, curEntry, null);
                    instance.setDirty(true); // IMPORTANT: instance now is dirty due to update!
                    updatedObjectsHere.add(curEntry);
                } catch (final DbObjectNotExistException e) {
                    // GET IN OTHER BACKEND
                    objectsInOtherBackend.add(curEntry);
                }
            }

            // NOTE: update hints of objects since they come from other backends in this call
            // Prepare hints to update
            this.updateHintsToCurrentExecEnv(updatedObjectsHere);

            upsertObjectsInOtherBackend(sessionID, objectsInOtherBackend);
        } finally {
            // Do not remove it since it might be called from inside
            /*
             * clientLib.removeSessionIDForThread(threadID); if (Configuration.MOCK_TESTING)
             * { DataClayMockObject.removeCurrentThreadLib(); }
             */
        }
    }

    /**
     * Update object in another backend.
     *
     * @param sessionID           ID of session
     * @param objectsInOtherNodes List of metadata of objects to update and its bytes. It is useful
     *                            to avoid multiple trips.
     * @throws RemoteException if some exception occurs
     */
    private void upsertObjectsInOtherBackend(final SessionID sessionID,
                                             final List<ObjectWithDataParamOrReturn> objectsInOtherNodes) {
        // Prepare to unify calls (only one call for DS)
        final Map<ExecutionEnvironmentID, List<ObjectWithDataParamOrReturn>> objectsPerBackend = new ConcurrentHashMap<>();

        for (final ObjectWithDataParamOrReturn curEntry : objectsInOtherNodes) {
            final ObjectID curObjectID = curEntry.getObjectID();
            final MetaDataInfo mdInfo = runtime.getObjectMetadata(curObjectID);
            if (mdInfo == null) {
                // TODO: review exception design, getObjectMetadata is returning null in case
                // object not registered.
                // WARNING: This exception should not happen here
                // NOTE: if it is a volatile and hint failed, it means that object is actually
                // not registered
                throw new ObjectNotRegisteredException(curObjectID);
            }
            final Set<ExecutionEnvironmentID> locations = mdInfo.getLocations();
            if (DEBUG_ENABLED) {
                LOGGER.debug(
                        "[==Upsert==] Updating objects in other backends. Locations of objects: " + locations);
            }
            // Update object at first location (NOT UPDATING REPLICAS!!!)
            final ExecutionEnvironmentID curLoc = locations.iterator().next();
            List<ObjectWithDataParamOrReturn> objectsInBackend = objectsPerBackend.get(curLoc);
            if (objectsInBackend == null) {
                objectsInBackend = new ArrayList<>();
                objectsPerBackend.put(curLoc, objectsInBackend);
            }
            objectsInBackend.add(curEntry);
        }

        // Now call
        for (final Entry<ExecutionEnvironmentID, List<ObjectWithDataParamOrReturn>> curEntry : objectsPerBackend
                .entrySet()) {
            ExecutionEnvironmentID backendID = curEntry.getKey();
            final List<ObjectWithDataParamOrReturn> objectsToUpdate = curEntry.getValue();
            final DataServiceAPI dataServiceApi = runtime.getRemoteExecutionEnvironment(backendID);
            dataServiceApi.upsertObjects(sessionID, objectsToUpdate);

        }
    }

    @Override
    public SerializedParametersOrReturn getCopyOfObject(final SessionID sessionID, final ObjectID objectID,
                                                        final boolean recursive) {
        if (DEBUG_ENABLED) {
            LOGGER.debug("[==GetCopyOfObject==] Retrieving copy of " + objectID);
        }

        // Get the data service of one of the backends that contains the original
        // object.
        final Set<ObjectID> objectIDs = new HashSet<>();
        objectIDs.add(objectID);

        final List<ObjectWithDataParamOrReturn> serializedObjs = getObjects(sessionID, objectIDs, new HashSet<ObjectID>(), recursive, null, 0);
        // Prepare OIDs
        if (DEBUG_ENABLED) {
            LOGGER.debug("[==GetCopyOfObject==] Objects obtained to return a copy of " + objectID);
        }

        final Map<ObjectID, ObjectID> originalToVersion = new ConcurrentHashMap<>();

        for (final ObjectWithDataParamOrReturn curEntry : serializedObjs) {
            final ObjectID origObjectID = curEntry.getObjectID();
            final ObjectID versionObjectID = new ObjectID();
            originalToVersion.put(origObjectID, versionObjectID);
        }

        for (final ObjectWithDataParamOrReturn curEntry : serializedObjs) {
            // Store version in this backend (if already stored, just skip it)
            final ObjectID origObjectID = curEntry.getObjectID();
            final ObjectID versionObjectID = originalToVersion.get(origObjectID);
            final DataClayObjectMetaData metadata = curEntry.getMetaData();
            metadata.modifyOids(originalToVersion);
            curEntry.setObjectID(versionObjectID);
        }

        if (DEBUG_ENABLED) {
            LOGGER.debug("[==GetCopyOfObject==] Updated OIDs and references to return a copy of " + objectID);
        }

        final SerializedParametersOrReturn serParamReturn = new SerializedParametersOrReturn(serializedObjs);

        return serParamReturn;
    }

    @Override
    public void updateObject(final SessionID sessionID, final ObjectID intoObjectID,
                             final SerializedParametersOrReturn fromObject) {
        try {
            // create lots of objects here and stash them somewhere
            if (sessionID != null) {
                runtime.setCurrentThreadSessionID(sessionID);
            }
            if (DEBUG_ENABLED) {
                LOGGER.debug("[==UpdateObject==] ** Updating object " + intoObjectID);
            }

            final DataClayObject instanceInto = runtime.getOrNewInstanceFromDB(intoObjectID, true);
            final Object[] aux = DataClayDeserializationLib.deserializeParamsOrReturn(fromObject, null, runtime);
            final DataClayObject instanceFrom = (DataClayObject) aux[0];

            if (Configuration.Flags.CHECK_SESSION.getBooleanValue()) {
                this.runtime.checkSession(instanceInto.getDataSetID(), sessionID);
            }

            // ============================= EXECUTE =========================== //
            if (DEBUG_ENABLED) {
                LOGGER.debug("[==UpdateObject==] ** Updating object " + intoObjectID + " from object "
                        + instanceFrom.getID());
            }

            instanceInto.setAll(instanceFrom);

            if (DEBUG_ENABLED) {
                LOGGER.debug("[==UpdateObject==] ** Updated object " + intoObjectID + " from object "
                        + instanceFrom.getID());
            }

        } catch (final Exception e) {
            if (DEBUG_ENABLED) {
                LOGGER.debug("[==UpdateObject==] Caugh exception during putObject: ", e);
            }
        } finally {
            if (Configuration.mockTesting) {
                DataClayMockObject.removeCurrentThreadLib();
            }
            runtime.removeCurrentThreadSessionID();
            if (DEBUG_ENABLED) {
                LOGGER.debug("[==UpdateObject==] ** End of updating object " + intoObjectID);
            }
        }
    }

    @Override
    public List<ObjectWithDataParamOrReturn> getObjects(final SessionID sessionID, final Set<ObjectID> objectIDs,
                                                        final Set<ObjectID> alreadyObtainedObjs,
                                                        final boolean recursive, final ExecutionEnvironmentID replicaDestBackendID,
                                                        final int updateReplicaLocs) {
        LOGGER.debug("----> Starting get objects " + objectIDs);

        final List<ObjectWithDataParamOrReturn> result = new ArrayList<>();
        try {
            runtime.setCurrentThreadSessionID(sessionID);
            final List<Tuple<ObjectID, ExecutionEnvironmentID>> objectsInOtherBackend = new ArrayList<>();
            for (final ObjectID objectID : objectIDs) {

                if (recursive) {
                    final List<Tuple<ObjectID, ExecutionEnvironmentID>> pendingObjs = new LinkedList<>();
                    pendingObjs.add(new Tuple<>(objectID, null));
                    final ListIterator<Tuple<ObjectID, ExecutionEnvironmentID>> it = pendingObjs
                            .listIterator(pendingObjs.size());
                    while (it.hasPrevious()) {
                        final Tuple<ObjectID, ExecutionEnvironmentID> idAndHint = it.previous();
                        final ObjectID curObID = idAndHint.getFirst();
                        final ExecutionEnvironmentID hint = idAndHint.getSecond();

                        if (DEBUG_ENABLED) {  LOGGER.debug("[==Get==] Getting " + curObID); }
                        it.remove();
                        if (alreadyObtainedObjs.contains(curObID)) {
                            // Already read
                            if (DEBUG_ENABLED) {  LOGGER.debug("[==Get==] Object {} already obtained", curObID); }
                            continue;
                        }

                        if (hint != null && !hint.equals(this.executionEnvironmentID)) {
                            // GET IN OTHER BACKEND
                            if (DEBUG_ENABLED) {
                                LOGGER.debug("[==Get==] {} is not in this backend obj hint {} != current loc {} ", curObID, hint, this.executionEnvironmentID);
                            }
                            objectsInOtherBackend.add(new Tuple<>(curObID, hint));
                            continue;
                        }

                        try {
                            final ObjectWithDataParamOrReturn objWithData = getObjectInternal(curObID, replicaDestBackendID,
                                    updateReplicaLocs);
                            if (objWithData != null) {
                                result.add(objWithData);
                                alreadyObtainedObjs.add(curObID);
                                // Get associated objects (recursive)
                                final DataClayObjectMetaData metadata = objWithData.getMetaData();
                                for (final Entry<Integer, ObjectID> associatedOIDEntry : metadata.getOids().entrySet()) {
                                    final Integer tag = associatedOIDEntry.getKey();
                                    final ObjectID associatedOID = associatedOIDEntry.getValue();
                                    if (!alreadyObtainedObjs.contains(associatedOID)) {
                                        it.add(new Tuple<>(associatedOID, metadata.getHint(tag)));
                                    }
                                }
                            }
                        } catch (final DbObjectNotExistException e) {
                            // GET IN OTHER BACKEND
                            if (DEBUG_ENABLED) {  LOGGER.debug("[==Get==] Not in this backend (wrong or null hint): " + curObID); }
                            objectsInOtherBackend.add(new Tuple<>(curObID, null));
                        }
                    }
                } else {
                    final ObjectWithDataParamOrReturn objWithData = getObjectInternal(objectID, replicaDestBackendID, updateReplicaLocs);
                    if (objWithData != null) {
                        result.add(objWithData);
                    }
                }
            }

            result.addAll(getObjectsInOtherBackend(sessionID, objectsInOtherBackend, alreadyObtainedObjs, true, replicaDestBackendID, updateReplicaLocs));


        } finally {
            /*
             * clientLib.removeSessionIDForThread(threadID); if (Configuration.MOCK_TESTING)
             * { DataClayMockObject.removeCurrentThreadLib(); }
             */
            // Don't remove since it might be called from inside function
        }
        LOGGER.debug("<---- Finished get objects " + objectIDs);

        return result;
    }

    /**
     * Get object internal function
     *
     * @param objectID   ID of the object ot get
     * @param replicaDestBackendID Destination backend of objects being obtained for replica
     * @param updateReplicaLocs If 1, provided replica dest backend id must be added to replica locs of obtained objects
     *                          If 2, provided replica dest backend id must be removed from replica locs
     *                          If 0, replicaDestBackendID field is ignored
     * @return Object with data
     */
    private ObjectWithDataParamOrReturn getObjectInternal(final ObjectID objectID,
                                                          final ExecutionEnvironmentID replicaDestBackendID,
                                                          final int updateReplicaLocs) {
        if (DEBUG_ENABLED) {
            LOGGER.debug("[==Get==] Getting/Creating instance for " + objectID);
        }
        ObjectWithDataParamOrReturn objWithData = null;
        // lock serialization to avoid GC serializing at same time without replica locs
        runtime.lock(objectID);
        try {
            final DataClayExecutionObject instance = runtime.getOrNewInstanceFromDB(objectID, false);
            final List<DataClayObject> pendingObjs = new LinkedList<>();
            final ListIterator<DataClayObject> it = pendingObjs.listIterator(pendingObjs.size());
            if (replicaDestBackendID != null && updateReplicaLocs == 1) { // update = 1 means new replica/federation
                if (instance.getReplicaLocations() != null && instance.getReplicaLocations().contains(replicaDestBackendID)) {
                    // already replicated
                    LOGGER.debug("[==Get==] WARNING: Already replicated " + objectID + ". Skipping get");
                    return null;
                }
            }
            objWithData = DataClaySerializationLib.serializeDataClayObjectWithData(instance, runtime, false, null, it,
                    false, instance.getHint(), false);
            if (replicaDestBackendID != null && updateReplicaLocs == 1) {
                    LOGGER.debug("[==Get==] Setting object {} replica location {}", objectID, replicaDestBackendID);
                    instance.addReplicaLocations(replicaDestBackendID);
                    instance.setDirty(true);
                    LOGGER.debug("[==Get==] Serializing object {} with dest replica locs {}", objectID, objWithData.getMetaData().getReplicaLocations());
                    LOGGER.debug("[==Get==] Setting object {} original location {} ", objectID, this.executionEnvironmentID);
                    objWithData.getMetaData().setOriginLocation(this.executionEnvironmentID);
            } else if (updateReplicaLocs == 2) {
                if (replicaDestBackendID != null) {
                    LOGGER.debug("[==Get==] Removing from object {} the replica location {}", objectID, replicaDestBackendID);
                    instance.removeReplicaLocation(replicaDestBackendID);
                } else {
                    LOGGER.debug("[==Get==] Removing from object {} all replica locations", objectID);
                    instance.clearReplicaLocations();

                }
                instance.setDirty(true);
            }


        } finally {
            runtime.unlock(objectID);
        }
        return objWithData;
    }

    /**
     * Get object in another backend. This function is called from DbHandler in a
     * recursive get.
     *
     * @param sessionID           ID of session
     * @param objectsInOtherNodes List of metadata of objects to read. It is useful to avoid
     *                            multiple trips.
     * @param recursive           Indicates is recursive
     * @param replicaDestBackendID Destination backend of objects being obtained for replica
     * @param updateReplicaLocs If 1, provided replica dest backend id must be added to replica locs of obtained objects
     *                          If 2, provided replica dest backend id must be removed from replica locs
     *                          If 0, replicaDestBackendID field is ignored
     * @return Map of serialized object where key is the objectID. Object is not serialized if flag getOnlyRefs=true
     */
    private List<ObjectWithDataParamOrReturn> getObjectsInOtherBackend(final SessionID sessionID,
                                                                       final List<Tuple<ObjectID, ExecutionEnvironmentID>> objectsInOtherNodes,
                                                                       final Set<ObjectID> alreadyObtainedObjs,
                                                                       final boolean recursive,
                                                                       final ExecutionEnvironmentID replicaDestBackendID,
                                                                       final int updateReplicaLocs) {

        final List<ObjectWithDataParamOrReturn> result = new ArrayList<>();

        // Prepare to unify calls (only one call per DS)
        final Map<ExecutionEnvironmentID, Set<ObjectID>> objectsPerBackend = new ConcurrentHashMap<>();

        for (final Tuple<ObjectID, ExecutionEnvironmentID> curObjectIDEntry : objectsInOtherNodes) {
            final ObjectID curObjectID = curObjectIDEntry.getFirst();
            final ExecutionEnvironmentID hint = curObjectIDEntry.getSecond();
            if (DEBUG_ENABLED) {
                LOGGER.debug("[==GetObjectsInOtherBackend==] Looking for metadata of " + curObjectID);
            }
            Set<ExecutionEnvironmentID> locations = null;
            if (hint != null) {
                locations = new HashSet<ExecutionEnvironmentID>();
                locations.add(hint);
            } else {
                final MetaDataInfo mdInfo = runtime.getObjectMetadata(curObjectID);
                if (mdInfo == null) {
                    // TODO: review exception design, getObjectMetadata is returning null in case
                    // object not registered.
                    // WARNING: This exception should not happen here
                    // NOTE: if it is a volatile and hint failed, it means that object is actually
                    // not registered
                    throw new ObjectNotRegisteredException(curObjectID);
                } else {
                    locations = mdInfo.getLocations();
                }
            }
            // Always obtain from the first location
            final ExecutionEnvironmentID backendSrc = locations.iterator().next();
            Set<ObjectID> objectsInBackend = objectsPerBackend.get(backendSrc);
            if (objectsInBackend == null) {
                objectsInBackend = new HashSet<>();
                objectsPerBackend.put(backendSrc, objectsInBackend);
            }
            objectsInBackend.add(curObjectID);
        }

        // Now call
        for (final Entry<ExecutionEnvironmentID, Set<ObjectID>> curEntry : objectsPerBackend.entrySet()) {
            final ExecutionEnvironmentID backendID = curEntry.getKey();
            final Set<ObjectID> objectsToGet = curEntry.getValue();

            if (replicaDestBackendID == null || !replicaDestBackendID.equals(backendID)) {
                // do not get objects already in destination backend
                final DataServiceAPI dataServiceApi = runtime.getRemoteExecutionEnvironment(backendID);
                if (DEBUG_ENABLED) {
                    LOGGER.debug("[==Get==] Get from other location, objects: " + objectsToGet);
                }
                final List<ObjectWithDataParamOrReturn> curResult = dataServiceApi.getObjects(sessionID, objectsToGet,
                        alreadyObtainedObjs, recursive, replicaDestBackendID, updateReplicaLocs);
                result.addAll(curResult);
            }

        }
        return result;
    }

    @Override
    public Map<ObjectID, ExecutionEnvironmentID> removeObjects(final SessionID sessionID, final Set<ObjectID> objectIDs,
                                                               final boolean recursive, final boolean moving, final ExecutionEnvironmentID newHint) {

        final Map<ObjectID, ExecutionEnvironmentID> removedObjs = new ConcurrentHashMap<>();
        try {
            runtime.setCurrentThreadSessionID(sessionID);
            if (Configuration.mockTesting) {
                DataClayMockObject.setCurrentThreadLib(this.runtime);
            }

            final List<ObjectID> objectsInOtherBackend = new ArrayList<>();
            for (final ObjectID objectID : objectIDs) {
                if (recursive) {
                    final List<ObjectID> pendingObjs = new LinkedList<>();
                    pendingObjs.add(objectID);
                    final ListIterator<ObjectID> it = pendingObjs.listIterator(pendingObjs.size());
                    while (it.hasPrevious()) {
                        final ObjectID curObID = it.previous();
                        if (DEBUG_ENABLED) {
                            LOGGER.debug("[==Remove==] Removing " + curObID);
                        }

                        it.remove();

                        if (removedObjs.get(curObID) != null) {
                            // Already removed
                            continue;
                        }
                        // If is in this DS, remove it.
                        removedObjs.put(curObID, executionEnvironmentID);
                        try {

                            // Get bytes from DB first
                            final byte[] objBytes = get(this.executionEnvironmentID, curObID);
                            // Remove and check if in memory
                            removeObjectInternal(sessionID, curObID, moving, newHint);
                            final DataClayObjectMetaData metadata = DataClayDeserializationLib
                                    .deserializeMetaDataFromDB(objBytes);
                            for (final ObjectID associatedOID : metadata.getOids().values()) {
                                if (removedObjs.get(associatedOID) == null) {
                                    it.add(associatedOID);
                                }
                            }

                        } catch (final DbObjectNotExistException ex) {
                            LOGGER.debug("Object {} not found in this node, adding to remove in another.", curObID);
                            LOGGER.debug("removeObjects error", ex);
                            // REMOVE IN OTHER BACKEND
                            objectsInOtherBackend.add(curObID);
                        }
                    }

                } else {
                    removeObjectInternal(sessionID, objectID, moving, newHint);
                }
            }

            if (recursive) {
                removedObjs
                        .putAll(removeObjectsInOtherBackend(sessionID, objectsInOtherBackend, true, moving, newHint));
            }

        } finally {
            runtime.removeCurrentThreadSessionID();
            if (Configuration.mockTesting) {
                DataClayMockObject.removeCurrentThreadLib();
            }

        }
        return removedObjs;

    }

    /**
     * Remove object internal function
     *
     * @param sessionID ID of session removing
     * @param objectID  ID of the object to remove
     * @param moving    Indicates remove was done by a movement.
     * @param newHint   New hint in case of movement
     * @throws DbObjectNotExistException if object is not in database
     */
    private void removeObjectInternal(final SessionID sessionID, final ObjectID objectID, final boolean moving,
                                      final ExecutionEnvironmentID newHint) {
        if (DEBUG_ENABLED) {
            LOGGER.debug("[==Remove==] Removing object " + objectID);
        }
        final DataClayExecutionObject instance = runtime.getOrNewInstanceFromDB(objectID, true);
        runtime.lock(objectID);
        try {
            if (moving) {
                this.runtime.setWeakProxy(instance, newHint);
            } else {

                instance.setPendingToRegister(true);
                instance.setOwnerSessionIDforVolatiles(sessionID);
                runtime.removeFromHeap(objectID); // remove old

                if (Configuration.Flags.NOTIFICATION_MANAGER_ACTIVE.getBooleanValue()) {

                    // === NOTIFY NOTIFICATION MANAGER === //
                    // Removed instance is send as a volatile object!
                    final List<DataClaySerializable> wrappedParams = new ArrayList<>();

                    wrappedParams.add(instance);
                    final SerializedParametersOrReturn serParams = DataClaySerializationLib.serializeParamsOrReturn(
                            wrappedParams, null, runtime, false, executionEnvironmentID, false);
                    serParams.removeReferencesForYaml();
                    final MetaDataInfo metadata = runtime.getObjectMetadata(objectID);
                    if (metadata == null) {
                        // TODO: review exception design, getObjectMetadata is returning null in case
                        // object not registered.
                        // WARNING: This exception should not happen here
                        // NOTE: if it is a volatile and hint failed, it means that object is actually
                        // not registered
                        throw new ObjectNotRegisteredException(objectID);
                    }
                    final MetaClassID classID = metadata.getMetaclassID();
                    final EventType eventType = new DeletedObjEventType(classID);
                    final EventMessage eventMsg = new EventMessage(objectID, eventType, serParams);
                    this.runtime.getLogicModuleAPI().adviseEvent(eventMsg);
                }

                // Set object to volatile
                instance.setNewObjectID();
                if (DEBUG_ENABLED) {
                    LOGGER.debug("[==Cache==] Added to objectsMap due to remove " + instance.getObjectID()
                            + " of class " + instance.getClass().getName() + ". System.id = "
                            + System.identityHashCode(instance));
                }
                runtime.addToHeap(instance);
            }

            // ==== REMOVE ==== //
            // Remove object from MD cache and Database
            if (DEBUG_ENABLED) {
                LOGGER.debug("[==Remove==] Removing metadata info from MetaData Cache: " + objectID);
            }
            runtime.removeObjectMetadataFromCache(objectID);
            this.storageLocation.delete(this.executionEnvironmentID, objectID);

        } finally {
            runtime.unlock(objectID);
        }

    }

    /**
     * Remove objects in another backend.
     *
     * @param sessionID           ID of session removing
     * @param objectsInOtherNodes List of objects to remove in other backends
     * @param recursive           Indicates is recursive
     * @param moving              Indicates objects are being removed due to a movement.
     * @param newHint             New hint to be set
     * @return ID of objects and for each object, its BackendID.
     */
    private Map<ObjectID, ExecutionEnvironmentID> removeObjectsInOtherBackend(final SessionID sessionID,
                                                                              final List<ObjectID> objectsInOtherNodes, final boolean recursive, final boolean moving,
                                                                              final ExecutionEnvironmentID newHint) {
        final Map<ObjectID, ExecutionEnvironmentID> result = new ConcurrentHashMap<>();

        // Prepare to unify calls (only one call for DS)
        final Map<ExecutionEnvironmentID, Set<ObjectID>> objectsPerBackend = new ConcurrentHashMap<>();
        for (final ObjectID curObjectID : objectsInOtherNodes) {
            final MetaDataInfo mdInfo = runtime.getObjectMetadata(curObjectID);
            if (mdInfo == null) {
                // TODO: review exception design, getObjectMetadata is returning null in case
                // object not registered.
                // WARNING: This exception should not happen here
                // NOTE: if it is a volatile and hint failed, it means that object is actually
                // not registered
                throw new ObjectNotRegisteredException(curObjectID);
            }
            final Set<ExecutionEnvironmentID> locations = mdInfo.getLocations();
            // Always obtain from the first location
            final ExecutionEnvironmentID backendSrc = locations.iterator().next();
            Set<ObjectID> objectsInBackend = objectsPerBackend.get(backendSrc);
            if (objectsInBackend == null) {
                objectsInBackend = new HashSet<>();
                objectsPerBackend.put(backendSrc, objectsInBackend);
            }
            objectsInBackend.add(curObjectID);
            if (DEBUG_ENABLED) {
                LOGGER.debug("[==Remove==] Removing metadata info from MetaData Cache: " + curObjectID);
            }
            runtime.removeObjectMetadataFromCache(curObjectID);
            result.put(curObjectID, backendSrc);
        }

        // Now call
        for (final Entry<ExecutionEnvironmentID, Set<ObjectID>> curEntry : objectsPerBackend.entrySet()) {
            final ExecutionEnvironmentID backendID = curEntry.getKey();
            final Set<ObjectID> objectsToRemove = curEntry.getValue();
            if (DEBUG_ENABLED) {
                LOGGER.debug("[==Remove==] Calling remove in other backend for: " + objectsToRemove);
            }
            final DataServiceAPI dataServiceApi = runtime.getRemoteExecutionEnvironment(backendID);
            dataServiceApi.removeObjects(sessionID, objectsToRemove, recursive, moving, newHint);

        }
        return result;
    }

    @Override
    public Set<ObjectID> newReplica(final SessionID sessionID, final ObjectID objectID,
                                                      final ExecutionEnvironmentID destBackendID,
                                                      final boolean recursive) {
        LOGGER.debug("----> Starting new replica of " + objectID);

        // Get the original object.
        final Set<ObjectID> objectIDs = new HashSet<>();
        objectIDs.add(objectID);
        final List<ObjectWithDataParamOrReturn> serializedObjs = getObjects(sessionID,
                objectIDs, new HashSet<ObjectID>(), recursive, destBackendID, 1);

        // Store it in destination backend
        final DataServiceAPI dataServiceApi = runtime.getRemoteExecutionEnvironment(destBackendID);
        dataServiceApi.storeObjects(sessionID, serializedObjs, false, null);
        for (ObjectWithDataParamOrReturn objectWithDataParamOrReturn : serializedObjs) {
            objectIDs.add(objectWithDataParamOrReturn.getObjectID());
        }
        LOGGER.debug("<---- Finished new replica of " + objectID);
        return objectIDs;
    }

    @Override
    public ObjectID newVersion(final SessionID sessionID, final ObjectID objectID,
                           final ExecutionEnvironmentID destBackendID) {
        if (DEBUG_ENABLED) {
            LOGGER.debug("----> Starting new version for {} to destination backend {}", objectID, destBackendID);
        }

        // Get the original object.
        final Set<ObjectID> objectIDs = new HashSet<>();
        objectIDs.add(objectID);
        final List<ObjectWithDataParamOrReturn> serializedObjs = getObjects(sessionID, objectIDs, new HashSet<ObjectID>(),true, null, 0);

        // Prepare OIDs
        if (DEBUG_ENABLED) {
            LOGGER.debug("[==Version==] Objects obtained to create version for " + objectID);
        }

        final Map<ObjectID, ObjectID> originalToVersion = new ConcurrentHashMap<>();
        for (final ObjectWithDataParamOrReturn curEntry : serializedObjs) {
            // Store version in this backend (if already stored, just skip it)
            final ObjectID origObjectID = curEntry.getObjectID();
            final ObjectID versionObjectID = new ObjectID();
            if (DEBUG_ENABLED) {
                LOGGER.debug("[==Version==] Creating version {} -> {} ", origObjectID, versionObjectID);
            }
            originalToVersion.put(origObjectID, versionObjectID);
        }

        for (final ObjectWithDataParamOrReturn curEntry : serializedObjs) {
            // Store version in dest backend (if already stored, just skip it)
            final ObjectID origObjectID = curEntry.getObjectID();
            final ObjectID versionObjectID = originalToVersion.get(origObjectID);
            final DataClayObjectMetaData metadata = curEntry.getMetaData();
            metadata.modifyOids(originalToVersion);
            if (metadata.getOriginalObjectID() == null) {
                // IMPORTANT: only set if not already set since consolidate
                // is always applied to original one
                metadata.setOriginalObjectID(origObjectID);
                LOGGER.debug("Setting root location of {} to {}", versionObjectID, this.executionEnvironmentID);
                metadata.setRootLocation(this.executionEnvironmentID);
            }
            curEntry.setObjectID(versionObjectID);
        }

        // Store versions
        if (DEBUG_ENABLED) {
            LOGGER.debug("[==Version==] Storing version for " + objectID);
        }
        if (destBackendID.equals(this.executionEnvironmentID)) {
            this.storeObjects(sessionID, serializedObjs, false, null);
        } else {
            final DataServiceAPI dataServiceApi = runtime.getRemoteExecutionEnvironment(destBackendID);
            dataServiceApi.storeObjects(sessionID, serializedObjs, false, null);
        }
        LOGGER.debug("<---- Finished new version of " + objectID);
        return originalToVersion.get(objectID);
    }

    @Override
    public void consolidateVersion(final SessionID sessionID, final ObjectID finalVersionObjectID) {

        if (DEBUG_ENABLED) {
            LOGGER.debug("----> Starting consolidate for " + finalVersionObjectID);
        }
        // Consolidate in this backend - the complete version is here
        final Set<ObjectID> objectIDs = new HashSet<>();
        objectIDs.add(finalVersionObjectID);
        final List<ObjectWithDataParamOrReturn> versionObjects = getObjects(sessionID, objectIDs, new HashSet<ObjectID>(),true, null, 0);

        ExecutionEnvironmentID rootLocation = null;
        final Map<ObjectID, ObjectID> versionToOriginal = new HashMap<>();
        for (final ObjectWithDataParamOrReturn versionObject : versionObjects) {
            final ObjectID versionID = versionObject.getObjectID();
            final DataClayObjectMetaData versionMetaData = versionObject.getMetaData();
            if (versionMetaData.getOriginalObjectID() != null) {
                versionToOriginal.put(versionID, versionMetaData.getOriginalObjectID());
            }

            if (versionID.equals(finalVersionObjectID)) {
                rootLocation = versionObject.getMetaData().getRootLocation();
                LOGGER.debug("Root location of {} is {}", finalVersionObjectID, rootLocation);
            }

        }
         // Update original objects
        for (final ObjectWithDataParamOrReturn versionObject : versionObjects) {
            final DataClayObjectMetaData versionMetaData = versionObject.getMetaData();
            // Modify metadata to use new Object IDs
            versionMetaData.modifyOids(versionToOriginal);
            final ObjectID originalObjectID = versionMetaData.getOriginalObjectID();
            if (originalObjectID != null) {
                versionObject.setObjectID(originalObjectID);
            }
        }
        try {
            runtime.setCurrentThreadSessionID(sessionID);
            // Update original objects (here and in other DSs - replicas)
            if (rootLocation.equals(this.executionEnvironmentID)) {
                this.upsertObjects(sessionID, versionObjects);
            } else {
                final DataServiceAPI dataServiceApi = runtime.getRemoteExecutionEnvironment(rootLocation);
                dataServiceApi.upsertObjects(sessionID, versionObjects);
            }
        } finally {
            runtime.removeCurrentThreadSessionID();
        }

    }

    @Override
    public Tuple<Map<StorageLocationID, Set<ObjectID>>, Set<ObjectID>> migrateObjectsToBackends(
            final Map<StorageLocationID, StorageLocation> backends) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<ObjectID> moveObjects(final SessionID sessionID, final ObjectID objectID,
                                     final ExecutionEnvironmentID destLocation, final boolean recursive) {
        final Set<ObjectID> updateMetadataof = new HashSet<>();

        try {
            runtime.setCurrentThreadSessionID(sessionID);
            if (Configuration.mockTesting) {
                DataClayMockObject.setCurrentThreadLib(this.runtime);
            }
            if (DEBUG_ENABLED) {
                LOGGER.debug("[==MOVE==] Moving object " + objectID + " to storage location: " + destLocation);
            }
            final Set<ObjectID> objectIDs = new HashSet<>();
            // Get the the original object.
            objectIDs.add(objectID);

            final List<ObjectWithDataParamOrReturn> serializedObjs = getObjects(sessionID, objectIDs, new HashSet<ObjectID>(), recursive, null, 0);

            final Set<ObjectID> objectsToRemove = new HashSet<>();
            final List<ObjectWithDataParamOrReturn> objectsToMove = new ArrayList<>();

            for (final ObjectWithDataParamOrReturn objFound : serializedObjs) {

                // The object is not here (it comes from another node due to GET) but
                // it must be stored here
                if (DEBUG_ENABLED) {
                    LOGGER.debug("[==MOVE==] Looking for metadata of " + objFound.getObjectID());
                }
                final MetaDataInfo metadata = runtime.getObjectMetadata(objFound.getObjectID());
                if (metadata == null) {
                    // TODO: review exception design, getObjectMetadata is returning null in case
                    // object not registered.
                    // WARNING: This exception should not happen here
                    // NOTE: if it is a volatile and hint failed, it means that object is actually
                    // not registered
                    throw new ObjectNotRegisteredException(objFound.getObjectID());
                }
                final ExecutionEnvironmentID objLocation = metadata.getLocations().iterator().next();

                if (objLocation.equals(destLocation)) {
                    if (DEBUG_ENABLED) {
                        LOGGER.debug("[==MOVE==] Ignoring move of object  " + objFound.getObjectID()
                                + " since it is already where it should be. ObjLoc = " + objLocation + " and DestLoc = "
                                + destLocation);
                    }
                    // object already in dest
                    continue;
                } else {
                    if (this.executionEnvironmentID.equals(destLocation)) {
                        if (DEBUG_ENABLED) {
                            LOGGER.debug("[==MOVE==] Ignoring move of object  " + objFound.getObjectID()
                                    + " since it is already where it should be" + " ObjLoc = " + objLocation
                                    + " and DestLoc = " + destLocation);
                        }
                    } else {
                        if (DEBUG_ENABLED) {
                            LOGGER.debug("[==MOVE==] Moving object  " + objFound.getObjectID()
                                    + " since dest.location is different to src.location and object is not in dest.location "
                                    + " ObjLoc = " + objLocation + " and DestLoc = " + destLocation);
                        }
                        // THE DESTINATION IS ANOTHER NODE: move.
                        objectsToMove.add(objFound);
                        objectsToRemove.add(objFound.getObjectID());
                        updateMetadataof.add(objFound.getObjectID());
                    }
                }

            }

            if (DEBUG_ENABLED) {
                LOGGER.debug("[==MOVE==] Finally moving OBJECTS:  " + objectsToRemove);
            }

            // REMOVE IN OTHER BACKEND
            final DataServiceAPI dataServiceApi = runtime.getRemoteExecutionEnvironment(destLocation);
            // Now move all objects to the destination location.
            dataServiceApi.storeObjects(sessionID, objectsToMove, true, null);

            // TODO: lock any execution in remove before storing objects in remote
            // dataservice
            // so anyone can modify it.

            // Remove after store in order to avoid wrong executions during the movement :)
            // Remove all objects in all source locations different to dest. location
            this.removeObjects(sessionID, objectIDs, recursive, true, destLocation);

            // Remove entries
            for (final ObjectID oid : objectsToRemove) {
                runtime.removeObjectMetadataFromCache(oid);
            }

            if (DEBUG_ENABLED) {
                LOGGER.debug("[==MOVE==] Move finalized ");
            }

        } catch (

                final Exception ex) {
            LOGGER.debug("moveObjects error", ex);

        } finally {
            runtime.removeCurrentThreadSessionID();
            if (Configuration.mockTesting) {
                DataClayMockObject.removeCurrentThreadLib();
            }
        }

        return updateMetadataof;

    }

    @Override
    public MetaClassID getClassIDFromObjectInMemory(final ObjectID objectID) {
        try {
            if (Configuration.mockTesting) {
                DataClayMockObject.setCurrentThreadLib(this.runtime);
            }
            final DataClayObject dcObject = runtime.getFromHeap(objectID);
            if (dcObject != null) {
                return dcObject.getMetaClassID();
            }
        } finally {
            if (Configuration.mockTesting) {
                DataClayMockObject.removeCurrentThreadLib();
            }
        }
        return null;
    }

    /**
     * Update or store object.
     *
     * @param instance Object to update in DB.
     */
    public void gcCollectObjectInternal(final DataClayExecutionObject instance) {
        // =================== UPDATE DATABASE ===================//
        // We update the set of associated objects if new objects were associated or
        // deleted.
        if (DEBUG_ENABLED) {
            LOGGER.debug("[==GC==] Collecting object " + instance.getObjectID());
        }

        if (instance.isPendingToRegister()) {
            if (DEBUG_ENABLED) {
                LOGGER.debug("[==GC==] Serializing pending instance ");
            }
        }

        if (instance.isPendingToRegister()) {
            final byte[] arrBytes = DataClaySerializationLib.serializeForDBGarbageCollection(instance, false, null,
                    false);
            registerAndStorePendingObject(instance, arrBytes, true);
            instance.setPendingToRegister(false);

        } else if (instance.isDirty()) {
            if (DEBUG_ENABLED) {
                LOGGER.debug(
                        "[==GC==] Going to update dirty object in database object with ID " + instance.getObjectID());
            }
            final byte[] arrBytes = DataClaySerializationLib.serializeForDBGarbageCollection(instance, false, null,
                    false);
            this.storageLocation.update(this.executionEnvironmentID, instance.getObjectID(), arrBytes, true);
            if (DEBUG_ENABLED) {
                final StringBuilder strBuilder = new StringBuilder();
                strBuilder.append("Updated object of class ");
                strBuilder.append(instance.getClass().getName());
                strBuilder.append(" with Object ID " + instance.getObjectID());
                strBuilder.append(" with size = " + arrBytes.length + " bytes");
                LOGGER.debug("[==GC==]" + strBuilder.toString());
            }

            // TODO: Think which DataSet should be assigned to object (dgasull 2017)
            /*
             * if (instance.getDataSetID() != null) {
             * this.runtime.getLogicModuleAPI().setDataSetIDFromGarbageCollector(instance.
             * getObjectID(), instance.getDataSetID()); }
             */

        } else {
            if (Configuration.Flags.GLOBAL_GC_ENABLED.getBooleanValue()) {
                if (DEBUG_ENABLED) {
                    LOGGER.debug(
                            "[==GC==] Going to notify not dirty object to GlobalGC with ID " + instance.getObjectID());
                }
                final byte[] arrBytes = DataClaySerializationLib.serializeForDBGarbageCollection(instance, false, null,
                        true);
                if (arrBytes != null) {
                    final byte[] refCountingBytes = DataClayDeserializationLib.extractReferenceCounting(arrBytes);
                    this.storageLocation.update(this.executionEnvironmentID, instance.getObjectID(), refCountingBytes,
                            false);

                } else {
                    LOGGER.debug("[==GC==] Found very volatile not modified (not sending to Disk) with ID "
                            + instance.getObjectID());
                }
            }
        }

    }

    /**
     * Register pending object
     *
     * @param instance Instance to register
     * @param sync     Indicates if register is synchronous or asynchronous
     */
    private void registerObject(final DataClayExecutionObject instance,
                                               final boolean sync) {
        // Inform MDS about new object !
        final Map<ObjectID, MetaClassID> storedObjs = new ConcurrentHashMap<>();
        storedObjs.put(instance.getObjectID(), instance.getMetaClassID());

        final RegistrationInfo regInfo = new RegistrationInfo(instance.getObjectID(), instance.getMetaClassID(),
                instance.getOwnerSessionIDforVolatiles(), instance.getDataSetID(), null);

        if (DEBUG_ENABLED) {
            LOGGER.debug("[==RegisterPending==] Going to register " + regInfo + " for instance "
                    + System.identityHashCode(instance));
        }
        try {
            if (sync) {
                List<RegistrationInfo> regInfos = new ArrayList<>();
                regInfos.add(regInfo);
                this.runtime.getLogicModuleAPI().registerObjects(regInfos, executionEnvironmentID, Langs.LANG_JAVA);
            } else {
                this.runtime.getLogicModuleAPI().registerObjectFromGC(regInfo, executionEnvironmentID, this.runtime);
            }
        } catch (final Exception e) {
            // object already registered due to add alias / it's a replica
            LOGGER.debug("[==RegisterPending==] Exception occurred while registering object, ignoring if already registered ", e);
        }
        LOGGER.debug("[==RegisterPending==] Object registered");
    }

    /**
     * Store pending object
     *
     * @param instance Instance to register
     * @param arrBytes Persistent object
     */
    private void storePendingObject(final DataClayExecutionObject instance, final byte[] arrBytes) {

        LOGGER.debug("[==RegisterPending==] Registering pending object with ID " + instance.getObjectID() + " of class "
                            + instance.getClass().getName() + ". System.id = " + System.identityHashCode(instance));

        this.storageLocation.store(this.executionEnvironmentID, instance.getObjectID(), arrBytes);


    }

    /**
     * Register and store pending object
     *
     * @param instance Instance to register
     * @param arrBytes Persistent object
     * @param sync     Indicates if register is synchronous or asynchronous
     */
    private void registerAndStorePendingObject(final DataClayExecutionObject instance, final byte[] arrBytes,
                                               final boolean sync) {
        storePendingObject(instance, arrBytes);
        registerObject(instance, sync);

	}

    /**
     * Register all pending objects
     */
    @Override
    public void registerPendingObjects() {
        throw new UnsupportedOperationException();
    }

    // ============================== LAZY TASKS ==================== //

    /**
     * Add lazy task
     *
     * @param objectID ID of the object
     * @param lazyTask Lazy task
     */
    public void addLazyTask(final ObjectID objectID, final LazyTask lazyTask) {
        this.lazyTasksRunner.addLazyTask(objectID, lazyTask);
    }

    // ============================== GLOBAL DISK GC ==================== //

    /**
     * Execute lazy task
     *
     * @param implID        ID of the task to execute
     * @param paramObjectID ID of the parameter
     * @param sessionID     ID of the session
     * @param classID       ID of the class
     */
    public void executeLazyTask(final ImplementationID implID, final ObjectID paramObjectID, final SessionID sessionID,
                                final MetaClassID classID) {
        // TODO: check session?
        try {
            // create lots of objects here and stash them somewhere
            if (sessionID != null) {
                runtime.setCurrentThreadSessionID(sessionID);
            }
            if (Configuration.mockTesting) {
                DataClayMockObject.setCurrentThreadLib(this.runtime);
            }

            if (DEBUG_ENABLED) {
                LOGGER.debug("[==Execution==] ** New execution ** Executing task " + implID);

            }

            // ============================ DESERIALIZE PARAMETERS
            // ============================ //
            final DataClayObject paramInstance = runtime.getOrNewInstanceFromDB(paramObjectID, true);
            final Object[] loadedparams = new Object[]{paramInstance};

            // =================================== GET INSTANCE
            // ===================================//
            // We must instantiate an object of the execution class

            // SIMILAR TO NEW PERSISTENT INSTANCE

            // final DataClayObject instance = DataClayClassLoaderSrv.newInstance(classID,
            // new ObjectID(),
            // DataClayObjectState.VOLATILE_SERVER);
            // this.runImplementation(instance, implID, loadedparams);

            final Class<?> clazz = DataClayClassLoaderSrv.getClass(classID);
            final StubInfo stubInfo = DataClayObject.getStubInfoFromClass(clazz.getName());

            final ImplementationStubInfo implStubInfo = stubInfo.getImplementationByID(implID.toString());
            final String signature = implStubInfo.getSignature();

            /*
             * final UserType paramType = (UserType) implStubInfo.getParameters().get(0);
             * final Class<?> paramClass =
             * DataClayClassLoaderSrv.getClass(paramType.getClassID());
             */

            // ============================= EXECUTE =========================== //

            final String methodName = signature.split("\\(")[0];

            Class<?> paramClass = paramInstance.getClass();
            boolean notExecuted = true;
            while (notExecuted) {
                try {
                    clazz.getMethod(methodName, paramClass).invoke(null, loadedparams);
                    notExecuted = false;
                } catch (final java.lang.NoSuchMethodException err) {
                    paramClass = paramClass.getSuperclass();
                }
            }

            // clazz.getMethod(methodName, paramClass).invoke(null, loadedparams);

        } catch (final StatusRuntimeException sterr) {
            LOGGER.debug("[==Execution==] Error at task of parameter object {} and implementation {}", paramObjectID,
                    implID);
            LOGGER.debug("executeLazyTask got a StatusRuntimeException", sterr);
            throw sterr;
        } catch (final DataClayException de) {
            LOGGER.debug("[==Execution==] Error at task of parameter object {} and implementation {}", paramObjectID,
                    implID);
            LOGGER.debug("executeLazyTask got a native DataClayException", de);
            throw de;
        } catch (final Exception ex) {
            LOGGER.debug("[==Execution==] Error at task of parameter object {} and implementation {}", paramObjectID,
                    implID);
            LOGGER.debug("executeLazyTask got an error", ex);
            throw new JavaExecutionException(ex);
        } catch (final Error err) {
            LOGGER.debug("executeLazyTask got a ``true'' error", err);
            throw new JavaExecutionException(err);
        } finally {
            if (Configuration.mockTesting) {
                DataClayMockObject.removeCurrentThreadLib();
            }
            runtime.removeCurrentThreadSessionID();
        }

    }

    @Override
    public boolean exists(final ObjectID objectID) {
        if (DEBUG_ENABLED) {
            LOGGER.debug("[==Exists==] Checking if object {} exists in current EE", objectID);
        }
        runtime.lock(objectID);  // RACE CONDITION: object is being unloaded but still not in SL
        try {
            final boolean inheap = this.runtime.existsInHeap(objectID);
            if (inheap) {
                    final DataClayExecutionObject obj = this.runtime.getFromHeap(objectID);
                    // object might be in heap but as a "proxy"
                    // since this function is used from SL after checking if the object is in
                    // database,
                    // we return false if the object is not loaded so the combination of SL exists
                    // and EE exists
                    // can tell if the object actually exists
                    // summary: the object only exist in EE if it is loaded.
                    return obj.isLoaded();
            } else {
                return false;
            }
        } finally {
            runtime.unlock(objectID);
        }
    }

    @Override
    public void closeSessionInDS(final SessionID sessionID) {
        this.runtime.closeSessionInEE(sessionID);
    }


    @Override
    public void detachObjectFromSession(final ObjectID objectID, final SessionID sessionID) {
        try {
            runtime.setCurrentThreadSessionID(sessionID);
            this.runtime.detachObjectFromSession(objectID, null);
        } finally {
            runtime.removeCurrentThreadSessionID();
        }
    }

    @Override
    public void deleteAlias(final SessionID sessionID, final ObjectID objectID) {
        try {
            runtime.setCurrentThreadSessionID(sessionID);
            this.runtime.deleteAlias(objectID, null);
        } finally {
            runtime.removeCurrentThreadSessionID();
        }
    }

    @Override
    public boolean existsInDB(final ObjectID objectID) {
        return this.storageLocation.exists(objectID);
    }

    @Override
    public void store(final ExecutionEnvironmentID eeID, final ObjectID objectID, final byte[] bytes) {
        this.storageLocation.store(eeID, objectID, bytes);
    }

    /**
     * Get from this DB
     *
     * @param objectID ID of the object
     * @return Bytes of object
     */
    public byte[] getLocal(final ObjectID objectID) {
        return this.storageLocation.get(this.executionEnvironmentID, objectID);
    }

    @Override
    public byte[] get(final ExecutionEnvironmentID eeID, final ObjectID objectID) {
        return this.storageLocation.get(eeID, objectID);
    }

    @Override
    public void update(final ExecutionEnvironmentID eeID, final ObjectID objectID, final byte[] newbytes,
                       final boolean dirty) {
        this.storageLocation.update(eeID, objectID, newbytes, dirty);
    }

    @Override
    public void delete(final ExecutionEnvironmentID eeID, final ObjectID objectID) {
        this.storageLocation.delete(eeID, objectID);
    }

    @Override
    public void updateRefs(final Map<ObjectID, Integer> updateCounterRefs) {
        this.storageLocation.updateRefs(updateCounterRefs);
    }

    @Override
    public Set<ObjectID> getRetainedReferences() {
        return runtime.getRetainedReferences();
    }


    // ============= OTHER =========== //

    @Override
    public void cleanExecutionClassDirectory() {
        cleanExecutionClasses();
    }

    /**
     * Static function for cleaning paths. Static so can be called even if DS is not
     * running.
     */
    public static void cleanExecutionClasses() {
        try {

            DataClayClassLoaderSrv.cleanCaches();
            File f = new File(Configuration.Flags.EXECUTION_CLASSES_PATH.getStringValue());
            if (f.exists()) {
                FileAndAspectsUtils.deleteFolderContent(f);
            }
        } catch (final Exception ex) {
            LOGGER.debug("cleanExecutionClasses error", ex);
            throw new CleanExecutionClassesDirException(ex.getLocalizedMessage());
        }
    }

    @Override
    public void cleanCaches() {
        runtime.cleanCaches();
    }

    /**
     * Get DbHandler. Used for testing purposes.
     *
     * @return DbHandler.
     */
    public DBHandler getDbHandler() {
        return this.storageLocation.getDbHandler(this.executionEnvironmentID);
    }

    @Override
    public void closeDbHandler() {
        this.storageLocation.closeDbHandler(this.executionEnvironmentID);
    }

    @Override
    public void activateTracing(final int currentAvailableTaskID) {
        DataClayExtrae.setCurrentAvailableTaskID(currentAvailableTaskID);
        DataClayExtrae.initializeExtrae(true);

    }

    @Override
    public void deactivateTracing() {
        DataClayExtrae.finishTracing(true);
    }

    @Override
    public Map<String, byte[]> getTraces() {
        return DataClayExtrae.getTraces();
    }

    /**
     * Finish cache threads.
     *
     * @if some exception occurs
     */
    public void finishCacheThreads() {
        this.runtime.finishCacheThreads();
    }

    /**
     * Finish connections in DS.
     *
     * @throws Exception if some exception occurs
     */
    public void finishClientConnections() {
        if (this.runtime != null) {
            this.runtime.finishConnections();
        }

    }

    /**
     * Unregister the data service from the logic module.
     */
    public void unregisterFromLogicModule() {
        runtime.getLogicModuleAPI().unregisterExecutionEnvironment(executionEnvironmentID);
        runtime.getLogicModuleAPI().unregisterStorageLocation(storageLocationID);
    }

    @Override
    public void shutDown() {
        ownServer.stopService();
    }

    @Override
    public void disconnectFromOthers() {
        ownServer.disconnectFromOthers();
    }

    /**
     * Return number of references pointing to object.
     *
     * @param objectID ID of object
     * @return Number of references pointing to object
     */
    public int getNumReferencesTo(final ObjectID objectID) {
        return this.storageLocation.getNumReferencesTo(objectID);
    }

    /**
     * Only for testing and SL representing a storage location, get storage location
     * service.
     *
     * @return storageLocation service
     */
    public StorageLocationService getStorageLocationService() {
        return this.storageLocation;
    }

    /**
     * Notify LM current execution environment left
     */
    public void notifyExecutionEnvironmentShutdown() {
        final LogicModuleAPI lmAPI = this.runtime.getLogicModuleAPI();
        lmAPI.notifyExecutionEnvironmentShutdown(this.executionEnvironmentID);
    }

    /**
     * Notify LM current storage location left
     */
    public void notifyStorageLocationShutdown() {
        final LogicModuleAPI lmAPI = this.runtime.getLogicModuleAPI();
        lmAPI.notifyStorageLocationShutdown(this.storageLocationID);
    }


    /**
     * Wait for all execution environments associated to current data service to finish and return
     */
    public void waitForExecutionEnvironmentsToFinish() {
        final LogicModuleAPI lmAPI = this.runtime.getLogicModuleAPI();
        while (true) {
            if (!lmAPI.existsActiveEnvironmentsForSL(this.storageLocationID)) {
                break;
            }
            try {
                LOGGER.info("Waiting for EEs associated to current SL to finish ...");
                Thread.sleep(Configuration.Flags.SLEEP_WAIT_SHUTDOWN.getLongValue());
            } catch (final InterruptedException ie) {
                LOGGER.warn("Connection to LM interrupted. Shutting down DS");
                break;
            }
        }
    }


    @Override
    public int getNumObjectsInEE() {
        return runtime.numLoadedObjs();
    }

    @Override
    public int getNumObjects() {
        return this.storageLocation.getNumObjects();
    }
}
