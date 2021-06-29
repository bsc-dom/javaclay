
package es.bsc.dataclay.metadataservice;

import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.exceptions.metadataservice.ExecutionEnvironmentNotExistException;
import es.bsc.dataclay.exceptions.metadataservice.ObjectAlreadyRegisteredException;
import es.bsc.dataclay.exceptions.metadataservice.ObjectHasReplicas;
import es.bsc.dataclay.exceptions.metadataservice.ObjectNotRegisteredException;
import es.bsc.dataclay.metadataservice.MetaDataService;
import es.bsc.dataclay.metadataservice.MetaDataServiceDB;
import es.bsc.dataclay.metadataservice.ObjectMetaData;
import es.bsc.dataclay.test.AbstractManagerTest;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.DataSetID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.StorageLocationID;
import es.bsc.dataclay.util.management.metadataservice.DataClayInstance;
import es.bsc.dataclay.util.management.metadataservice.ExecutionEnvironment;
import es.bsc.dataclay.util.management.metadataservice.MetaDataInfo;
import es.bsc.dataclay.util.management.metadataservice.StorageLocation;
import es.bsc.dataclay.util.structs.Tuple;

@Ignore
public class MetaDataServiceTest extends AbstractManagerTest {

	private MetaDataService mdservice; // !<@brief MetaDataService instance tested.
	// !<@brief Path to the test databases.

	/** MetaDataService database. */
	private MetaDataServiceDB metadataDB;

	private static final int RANDMAX = 10; // !<@brief Maximum of Random numbers generated in tests
	private static final String HOSTBACKEND = "localhost";
	private static final String BACKENDNAME = "backend";
	private static final int TCP_PORTBACKEND = 12366;
	private ExecutionEnvironmentID backendID = new ExecutionEnvironmentID();
	private DataClayInstanceID dataClayInstanceID = new DataClayInstanceID();
	/**
	 * @brief This method is executed before each test. It is used create the test database.
	 * @author dgasull
	 * @throws RemoteException
	 */
	@Override
	@Before
	public final void before() {
		super.before();

		mdservice = new MetaDataService(dataSource);
		metadataDB = mdservice.getDbHandler();

		final ExecutionEnvironment backend = new ExecutionEnvironment(HOSTBACKEND,
				BACKENDNAME, TCP_PORTBACKEND, Langs.LANG_JAVA, dataClayInstanceID);
	}

	/**
	 * @brief This method is executed after each test.
	 * @author dgasull
	 * @throws Exception
	 */
	@Override
	@After
	public void after() throws Exception {
		// Delete db
		metadataDB.dropTables();
		metadataDB.close();
		super.after();
	}

	@Test
	public final void testGetObjectBackends() {
		// Prepare information of the object
		final ObjectID objectID = new ObjectID();
		final DataSetID datasetID = new DataSetID();
		final MetaClassID metaClassID = new MetaClassID();

		// Randomly choose the number of backends to register
		final int numBackends = new Random().nextInt(RANDMAX) + 1; // exclude 0
		final HashMap<ExecutionEnvironmentID, ExecutionEnvironment> backends = registerSetOfBackends(numBackends);

		// Register obejct
		registerObjectWithReplicas(objectID, metaClassID, datasetID, new HashSet<>(backends.keySet()));

		// Now get object backends
		final Map<ExecutionEnvironmentID, ExecutionEnvironment> objectBackends = mdservice.getObjectBackends(objectID);

		for (final ExecutionEnvironmentID curEntry : backends.keySet()) {
			assertTrue(objectBackends.containsKey(curEntry));
		}
	}

	@Test
	public final void testGetLocationsOfObjectsOfMetaClass() {
		// Randomly choose the number of backends to register
		final int numBackends = new Random().nextInt(RANDMAX) + 1; // at least 1
		final HashMap<ExecutionEnvironmentID, ExecutionEnvironment> backends = registerSetOfBackends(numBackends);

		// Randomly choose the number of objects per dataset and class
		final int numObjectsPerDataSetClass = new Random().nextInt(RANDMAX) + 1; // at least 1

		// Create two datasets
		final DataSetID datasetID1 = new DataSetID();
		final DataSetID datasetID2 = new DataSetID();
		final HashSet<DataSetID> datasetIDsTotal = new HashSet<>();
		datasetIDsTotal.add(datasetID1);
		datasetIDsTotal.add(datasetID2);

		// Create a metaclass
		final MetaClassID metaClassIDtoRequest = new MetaClassID();
		final HashSet<MetaClassID> metaClassIDsTotal = new HashSet<>();
		metaClassIDsTotal.add(metaClassIDtoRequest);

		// Register objects
		final Random rand = new Random();
		final HashSet<ObjectID> expectedObjects = new HashSet<>();
		for (int i = 0; i < numObjectsPerDataSetClass; i++) {
			for (final DataSetID datasetID : datasetIDsTotal) {
				for (final MetaClassID metaClassID : metaClassIDsTotal) {
					// Define an objectID
					final ObjectID objectID = new ObjectID();

					// Define a number of replicas for the object
					int numReplicas = rand.nextInt(numBackends);
					if (numReplicas == 0) {
						numReplicas++;
					}
					final HashSet<ExecutionEnvironmentID> replicaLocations = getSubsetOfLocationsForReplicas(
							new HashSet<>(backends.keySet()), numReplicas);

					// Register the current object
					final ObjectMetaData objMD = registerObjectWithReplicas(objectID, metaClassID, datasetID, replicaLocations);

					expectedObjects.add(objMD.getDataClayID());
				}
			}
		}

		// Query to MetaDataService
		final Map<ObjectID, MetaDataInfo> result = mdservice.getObjectsOfSpecificClass(metaClassIDtoRequest);

		assertTrue(result.keySet().equals(expectedObjects));
	}

	@Test
	public final void testRegisterObject() {
		// Prepare object
		final ObjectID objectID = new ObjectID();
		final MetaClassID metaClassID = new MetaClassID();
		final DataSetID datasetID = new DataSetID();
		final HashSet<ExecutionEnvironmentID> backendIDs = new HashSet<>();
		backendIDs.add(backendID);
		final AccountID ownerID = new AccountID();

		// Register object with MetaDataService
		mdservice.registerObject(objectID, metaClassID, datasetID, backendIDs, false, null,
				Langs.LANG_JAVA, ownerID);

		final ObjectMetaData result = metadataDB.getByID(objectID);
		assertTrue(result != null && result.getMetaClassID().equals(metaClassID) && result.getDataSetID().equals(datasetID)
				&& result.getExecutionEnvironmentIDs().contains(backendID) && !result.isReadOnly());
	}

	@Test
	public final void testChangeObjectID() {
		// Register the object
		final ObjectID objectID = new ObjectID();
		final MetaClassID metaClassID = new MetaClassID();
		final DataSetID datasetID = new DataSetID();
		final ObjectMetaData newObjMD = registerObjectWithoutReplicas(objectID, metaClassID, datasetID, backendID, false);

		final ObjectID newObjectID = new ObjectID();
		mdservice.changeObjectID(objectID, newObjectID);
		ObjectMetaData result = metadataDB.getByID(objectID);
		assertTrue(result == null);
		result = metadataDB.getByID(newObjectID);
		assertTrue(result != null);
		assertTrue(result.getDataSetID().equals(datasetID));
		assertTrue(result.getMetaClassID().equals(metaClassID));
		assertTrue(result.getExecutionEnvironmentIDs().equals(newObjMD.getExecutionEnvironmentIDs()));
	}

	@Test
	public final void testChangeObjectIDSameID() {
		// Register the object
		final ObjectID objectID = new ObjectID();
		final MetaClassID metaClassID = new MetaClassID();
		final DataSetID datasetID = new DataSetID();
		final ObjectMetaData newObjMD = registerObjectWithoutReplicas(objectID, metaClassID, datasetID, backendID, false);

		try {
			mdservice.changeObjectID(objectID, objectID);
		} catch (final ObjectAlreadyRegisteredException e) {
			// ignore
		}
		final ObjectMetaData result = metadataDB.getByID(objectID);
		assertTrue(result != null);
		assertTrue(result.getDataSetID().equals(datasetID));
		assertTrue(result.getMetaClassID().equals(metaClassID));
		assertTrue(result.getExecutionEnvironmentIDs().equals(newObjMD.getExecutionEnvironmentIDs()));
	}

	@Test
	public final void testUnregisterObject() {
		// Register the object
		final ObjectID objectID = new ObjectID();
		final MetaClassID metaClassID = new MetaClassID();
		final DataSetID datasetID = new DataSetID();
		registerObjectWithoutReplicas(objectID, metaClassID, datasetID, backendID, false);

		// Unregister the object with MetaDataService
		mdservice.unregisterObject(objectID);

		// Check
		assertTrue(!metadataDB.existsByID(objectID));
	}

	@Test
	public final void testMigrateObjectToBackend() {
		// Register the object
		final ObjectID objectID = new ObjectID();
		final MetaClassID metaClassID = new MetaClassID();
		final DataSetID datasetID = new DataSetID();
		final ObjectMetaData objectMD = registerObjectWithoutReplicas(objectID, metaClassID, datasetID, backendID, false);

		// Register new backend and store object in it
		final ExecutionEnvironment backend = new ExecutionEnvironment(HOSTBACKEND, BACKENDNAME + 1, TCP_PORTBACKEND + 1, Langs.LANG_JAVA,
				dataClayInstanceID);

		// Register second storage as a migration (although it is still in first backend)
		mdservice.migrateObjectToBackend(objectID, backendID, backend.getDataClayID());

		// Verify
		final ObjectMetaData newObject = metadataDB.getByID(objectMD.getDataClayID());
		assertTrue(newObject != null);
		assertTrue(newObject.getDataSetID().equals(objectMD.getDataSetID()));
		assertTrue(newObject.getMetaClassID().equals(objectMD.getMetaClassID()));
		assertTrue(newObject.getExecutionEnvironmentIDs().size() == 1
				&& newObject.getExecutionEnvironmentIDs().contains(backend.getDataClayID()));
	}

	@Test
	public final void testMigrateObjectsToBackend() {
		// Randomly choose the number of objects to register
		final int numObjs = new Random().nextInt(RANDMAX) + 1; // exclude 0

		// Prepare arrays for later use and verification
		final HashSet<ObjectMetaData> objMDspecs = new HashSet<>();
		final HashSet<ObjectID> objects = new HashSet<>();
		for (int i = 0; i < numObjs; ++i) {
			// Prepare s and Keys (for test)
			final ObjectID objectID = new ObjectID();
			final MetaClassID metaClassID = new MetaClassID();
			final DataSetID datasetID = new DataSetID();
			final HashSet<ExecutionEnvironmentID> backendIDs = new HashSet<>();
			backendIDs.add(backendID);
			final AccountID ownerID = new AccountID();

			final ObjectMetaData objectMDspec = new ObjectMetaData(objectID, metaClassID, datasetID, backendIDs,
					false, null, Langs.LANG_JAVA, ownerID);
			objMDspecs.add(objectMDspec);
			objects.add(objectID);

			// Register object
			registerObjectWithoutReplicas(objectID, metaClassID, datasetID, backendID, false);
		}

		// Register new backend
		final ExecutionEnvironment destBackend = new ExecutionEnvironment(HOSTBACKEND, BACKENDNAME + 1, TCP_PORTBACKEND + 1, Langs.LANG_JAVA,
				dataClayInstanceID);

		// Migrate objects to new backend with MetaDataService (we ignore handler ids, so we assume they are the same
		// in the destination backends)
		final Map<ExecutionEnvironmentID, Set<ObjectID>> newObjBackends = new HashMap<>();
		newObjBackends.put(destBackend.getDataClayID(), objects);
		assertTrue(mdservice.migrateObjectsToBackend(backendID, newObjBackends, true));

		// Verify migration
		for (final ObjectMetaData objectMD : objMDspecs) {
			final ObjectMetaData newObject = metadataDB.getByID(objectMD.getDataClayID());
			assertTrue(newObject != null);
			assertTrue(newObject.getMetaClassID().equals(objectMD.getMetaClassID()));
			assertTrue(newObject.getDataSetID().equals(objectMD.getDataSetID()));
			assertTrue(newObject.getDataClayID().equals(objectMD.getDataClayID()));
			assertTrue(newObject.getExecutionEnvironmentIDs().size() == 1);
			assertTrue(newObject.getExecutionEnvironmentIDs().contains(destBackend.getDataClayID()));
			assertTrue(!newObject.getExecutionEnvironmentIDs().contains(backendID));
			assertTrue(newObject.getLang().equals(Langs.LANG_JAVA));
		}

	}

	@Test
	public final void testCheckObjectInSrcNotInDest() {
		// Register the object
		final ObjectID objectID = new ObjectID();
		final MetaClassID metaClassID = new MetaClassID();
		final DataSetID datasetID = new DataSetID();
		registerObjectWithoutReplicas(objectID, metaClassID, datasetID, backendID, false);

		// Register new backend
		final ExecutionEnvironment destBackend = new ExecutionEnvironment(HOSTBACKEND, BACKENDNAME + 1, TCP_PORTBACKEND + 1, Langs.LANG_JAVA,
				dataClayInstanceID);

		// Check object in source and not in new backend
		assertTrue(mdservice.checkObjectInSrcNotInDest(objectID, backendID, destBackend.getDataClayID()));

	}

	@Test
	public final void testSetObjectReadOnly() {
		// Register the object
		final ObjectID objectID = new ObjectID();
		final MetaClassID metaClassID = new MetaClassID();
		final DataSetID datasetID = new DataSetID();
		registerObjectWithoutReplicas(objectID, metaClassID, datasetID, backendID, false);

		// Set read only
		mdservice.setObjectReadOnly(objectID);

		// Query and verify
		final ObjectMetaData result = metadataDB.getByID(objectID);
		assertTrue(result != null && result.getMetaClassID().equals(metaClassID) && result.getDataSetID().equals(datasetID)
				&& result.getExecutionEnvironmentIDs().contains(backendID) && result.isReadOnly());

	}

	@Test
	public final void testSetObjectReadWrite() {
		// Register the object
		final ObjectID objectID = new ObjectID();
		final MetaClassID metaClassID = new MetaClassID();
		final DataSetID datasetID = new DataSetID();
		registerObjectWithoutReplicas(objectID, metaClassID, datasetID, backendID, false);

		// Set read write
		mdservice.setObjectReadWrite(objectID);

		// Query and verify
		final ObjectMetaData result = metadataDB.getByID(objectID);
		assertTrue(result != null && result.getMetaClassID().equals(metaClassID) && result.getDataSetID().equals(datasetID)
				&& result.getExecutionEnvironmentIDs().contains(backendID) && !result.isReadOnly());
	}



	/*********************************** Exceptions ***********************************/

	@Test(expected = ObjectAlreadyRegisteredException.class)
	public final void testExceptionAlreadyRegisteredObject() {
		// Prepare object
		final ObjectID objectID = new ObjectID();
		final MetaClassID metaClassID = new MetaClassID();
		final DataSetID datasetID = new DataSetID();
		final HashSet<ExecutionEnvironmentID> backendIDs = new HashSet<>();
		backendIDs.add(backendID);
		final AccountID ownerID = new AccountID();

		// Register object with MetaDataService
		mdservice.registerObject(objectID, metaClassID, datasetID, backendIDs, false, null,
				Langs.LANG_JAVA, ownerID);
		final ObjectMetaData result = metadataDB.getByID(objectID);
		assertTrue(result != null && result.getMetaClassID().equals(metaClassID) && result.getDataSetID().equals(datasetID)
				&& result.getExecutionEnvironmentIDs().contains(backendID) && !result.isReadOnly());
		// Register object with MetaDataService
		mdservice.registerObject(objectID, metaClassID, datasetID, backendIDs, false, null,
				Langs.LANG_JAVA, ownerID);
	}

	@Test(expected = ObjectNotRegisteredException.class)
	public final void testExceptionChangeIDOfUnexistentObject() {
		mdservice.changeObjectID(new ObjectID(), new ObjectID());
	}

	@Test(expected = ObjectNotRegisteredException.class)
	public final void testExceptionUnregisterObjectNotExists() {
		mdservice.unregisterObject(new ObjectID());
	}

	@Test(expected = ExecutionEnvironmentNotExistException.class)
	public final void testExceptionMigrateObjectToBackendThatNotExist() {
		// Register the object
		final ObjectID objectID = new ObjectID();
		final MetaClassID metaClassID = new MetaClassID();
		final DataSetID datasetID = new DataSetID();
		registerObjectWithoutReplicas(objectID, metaClassID, datasetID, backendID, false);

		mdservice.migrateObjectToBackend(objectID, backendID, new ExecutionEnvironmentID());
	}

	@Test(expected = ExecutionEnvironmentNotExistException.class)
	public final void testExceptionMigrateObjectsToBackendThatNotExist() {
		// Register the object
		final ObjectID objectID = new ObjectID();
		final MetaClassID metaClassID = new MetaClassID();
		final DataSetID datasetID = new DataSetID();
		registerObjectWithoutReplicas(objectID, metaClassID, datasetID, backendID, false);

		final Map<ExecutionEnvironmentID, Set<ObjectID>> objectsDestBackends = new HashMap<>();
		final ExecutionEnvironmentID nonExistentBackendID = new ExecutionEnvironmentID();
		final Set<ObjectID> objects = new HashSet<>();
		objects.add(objectID);

		objectsDestBackends.put(nonExistentBackendID, objects);
		mdservice.migrateObjectsToBackend(backendID, objectsDestBackends, false);
	}

	@Test(expected = ObjectNotRegisteredException.class)
	public final void testExceptionMigrateObjectToBackendThatNotExist2() {
		// Register the object
		final ObjectID objectID = new ObjectID();
		final MetaClassID metaClassID = new MetaClassID();
		final DataSetID datasetID = new DataSetID();
		final ObjectMetaData objectMD = registerObjectWithoutReplicas(objectID, metaClassID, datasetID, backendID, false);

		mdservice.migrateObjectToBackend(objectMD.getDataClayID(), new ExecutionEnvironmentID(), backendID);
	}

	@Test(expected = ObjectNotRegisteredException.class)
	public final void testExceptionMigrateObjectThatNotRegistered() {
		// Register the object
		final ObjectID objectID = new ObjectID();
		final MetaClassID metaClassID = new MetaClassID();
		final DataSetID datasetID = new DataSetID();
		registerObjectWithoutReplicas(objectID, metaClassID, datasetID, backendID, false);

		mdservice.migrateObjectToBackend(new ObjectID(), new ExecutionEnvironmentID(), backendID);
	}


	@Test(expected = ObjectNotRegisteredException.class)
	public final void testExceptionSetObjectReadOnlyObjectNotRegistered() {
		// Register the object
		final ObjectID objectID = new ObjectID();
		final MetaClassID metaClassID = new MetaClassID();
		final DataSetID datasetID = new DataSetID();
		registerObjectWithoutReplicas(objectID, metaClassID, datasetID, backendID, false);

		mdservice.setObjectReadOnly(new ObjectID());
	}

	@Test(expected = ObjectNotRegisteredException.class)
	public final void testExceptionSetObjectReadWriteObjectNotRegistered() {
		// Register the object
		final ObjectID objectID = new ObjectID();
		final MetaClassID metaClassID = new MetaClassID();
		final DataSetID datasetID = new DataSetID();
		registerObjectWithoutReplicas(objectID, metaClassID, datasetID, backendID, false);

		mdservice.setObjectReadOnly(new ObjectID());
	}

	@Test(expected = ObjectHasReplicas.class)
	public final void testExceptionSetObjectReadWriteObjectHasReplicas() {
		// Register new backend
		final ExecutionEnvironment newBackend = new ExecutionEnvironment(HOSTBACKEND, BACKENDNAME + 1, TCP_PORTBACKEND + 1, Langs.LANG_JAVA,
				dataClayInstanceID);
		final ExecutionEnvironmentID newBackendID = newBackend.getDataClayID();

		// Register the object
		final ObjectID objectID = new ObjectID();
		final MetaClassID metaClassID = new MetaClassID();
		final DataSetID datasetID = new DataSetID();
		final HashSet<ExecutionEnvironmentID> backendIDs = new HashSet<>();
		backendIDs.add(backendID);
		backendIDs.add(newBackendID);
		final ObjectMetaData objectMD = registerObjectWithReplicas(objectID, metaClassID, datasetID, backendIDs);

		mdservice.setObjectReadWrite(objectMD.getDataClayID());
	}

	/********************* PRIVATE METHODS *********************/

	private ObjectMetaData registerObjectWithoutReplicas(final ObjectID objectID, final MetaClassID metaClassID,
			final DataSetID datasetID, final ExecutionEnvironmentID newBackendID, final boolean isReadOnly) {

		final HashSet<ExecutionEnvironmentID> backendIDs = new HashSet<>();
		backendIDs.add(newBackendID);
		final ObjectMetaData objectMd = new ObjectMetaData(objectID, metaClassID, datasetID, backendIDs,
				isReadOnly, null, Langs.LANG_JAVA, new AccountID());

		metadataDB.store(objectMd);

		final ObjectMetaData result = metadataDB.getByID(objectID);
		assertTrue(result.equals(objectMd));

		return objectMd;
	}

	private ObjectMetaData registerObjectWithReplicas(final ObjectID objectID, final MetaClassID metaClassID,
			final DataSetID datasetID, final HashSet<ExecutionEnvironmentID> backendIDs) {
		final ObjectMetaData objectMd = new ObjectMetaData(objectID, metaClassID, datasetID, backendIDs, true,
				null, Langs.LANG_JAVA, new AccountID());

		metadataDB.store(objectMd);

		final ObjectMetaData result = metadataDB.getByID(objectID);
		assertTrue(result.equals(objectMd));

		return objectMd;
	}

	private HashSet<ExecutionEnvironmentID> getSubsetOfLocationsForReplicas(final HashSet<ExecutionEnvironmentID> backendIDs, final int numReplicas) {
		final HashSet<ExecutionEnvironmentID> result = new HashSet<>();
		final Iterator<ExecutionEnvironmentID> itBackendIDs = backendIDs.iterator();
		for (int i = 0; i < numReplicas; i++) {
			result.add(itBackendIDs.next());
		}
		return result;
	}

	private HashMap<ExecutionEnvironmentID, ExecutionEnvironment> registerSetOfBackends(final int numBackends) {
		final HashMap<ExecutionEnvironmentID, ExecutionEnvironment> backends = new HashMap<>();
		for (int i = 1; i < numBackends + 1; ++i) {
			final ExecutionEnvironment backend = new ExecutionEnvironment(HOSTBACKEND, BACKENDNAME + i, TCP_PORTBACKEND + i, Langs.LANG_JAVA,
					dataClayInstanceID);
			backend.setDataClayID(new ExecutionEnvironmentID());
			backends.put(backend.getDataClayID(), backend);
		}
		return backends;
	}
}
